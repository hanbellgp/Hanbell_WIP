package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Start.SpinnerData;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.view.LayoutInflater;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import android.content.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WIP_InStock extends Activity {

	private MESDB db = new MESDB();

	ListView lv;
	Button btnConfirm, btnExit, btnRemove;
	TextView tvCM;
	EditText editCompID,editMaterialID,editProductName,editInput,editDescripTion,editCustomerName,editColer;
	Spinner spWareh;

	PrefercesService prefercesService;
	Map<String, String> params;
	String msProductOrderId="", msProductId, msProductCompId,
			msProductSerialNumber, msStepId, msEqpId, msAnalysisformsID,
			msSampletimes, msStepSEQ;
	static int miRowNum = 0;
	int miQty = 0;
	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_instock);
		try{
		prefercesService = new PrefercesService(this);
		params = prefercesService.getPreferences();
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle(params.get("StepName"));
		// 取得控件
		editInput = (EditText) findViewById(R.id.wipinstock_tvInput);
		editCompID= (EditText) findViewById(R.id.wipinstock_tvProductCompid);
		editMaterialID= (EditText) findViewById(R.id.wipinstock_tvProductid);
		editProductName= (EditText) findViewById(R.id.wipinstock_tvProductName);
		tvCM= (TextView) findViewById(R.id.wipinstock_tvCM);
		editCustomerName= (EditText) findViewById(R.id.wipinstock_tvCustomerName);
		editDescripTion=(EditText) findViewById(R.id.wipinstock_tvDescripTion);
		editColer=(EditText) findViewById(R.id.wipinstock_tvColer);
		spWareh= (Spinner) findViewById(R.id.wipinstock_spWareh);
		btnConfirm = (Button) findViewById(R.id.wipinstock_btnConfirm);
		btnExit = (Button) findViewById(R.id.wipinstock_btnExit);
		

		// ***********************************************Start
		String date = sDateFormatShort.format(new java.util.Date());
		// 读取报工人员
		String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"
				+ params.get("EQPID")
				+ "' AND ISNULL(WORKDATE,'')='"
				+ date
				+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND PROCESSUSERID='"
				+ MESCommon.UserId + "' ";
		String sResult = db.GetData(sSql, lsuser);		
		if (sResult != "") {
			MESCommon.showMessage(WIP_InStock.this, sResult);
			finish();
		}
	
		if ( lsuser.size()==0) {
			MESCommon.showMessage(WIP_InStock.this, "请先进行人员设备报工！");
		
		}
		
		// 库别				
		List<SpinnerData> lst = new ArrayList<SpinnerData>();
		
			SpinnerData c = new SpinnerData("W01", "成品仓");		
			lst.add(c);
			c = new  SpinnerData("ASRS03", "自动仓");	
			lst.add(c);
			c = new  SpinnerData("XTM01", "新塔厂的原料仓库");	
			lst.add(c);

		ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,
				android.R.layout.simple_spinner_item, lst);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWareh.setAdapter(Adapter);
		MESCommon.setSpinnerItemSelectedByValue(spWareh,"W01");
		
		

		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					EditText txtInput = (EditText) findViewById(R.id.wipinstock_tvInput);
					if ( lsuser.size()==0) {
						MESCommon.showMessage(WIP_InStock.this, "请先进行人员设备报工！");
						return false;
					}
					String strCompID = txtInput.getText().toString().trim();
				    if (strCompID.length() == 0) {
						MESCommon.show(WIP_InStock.this, "请扫描条码!");
						return false;
					}
					lsCompID.clear();
					String sResult = db.GetProductSerialNumber(strCompID,"", msProductOrderId,"QF","制造号码","装配",	lsCompID);
					if (!sResult.equals( "")) {
						MESCommon.showMessage(WIP_InStock.this, sResult);
						setFocus(editCompID);
						return false;
					}
					if(lsCompID.size()>0)
					{
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					}
					lsProcess.clear();
					String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
					sResult = db.GetProductProcess(sOrderID,txtInput.getText().toString().trim(), lsProcess);
					if (sResult != "") {
						MESCommon.showMessage(WIP_InStock.this, sResult);
						finish();
						
					}			
					if(lsProcess.size()==0)
					{
						MESCommon.show(WIP_InStock.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
						Clear();
						return false;
					}
					if(lsProcess.get(0).get("STEPID").toString().contains("入库前检验")&&lsProcess.get(0).get("PROCESSSTATUS").toString().equals("已完成"))
					{
						
					}else {
						MESCommon.show(WIP_InStock.this, "工件目前工序为【"+lsProcess.get(0).get("STEPID").toString()+"】，不能在【"+params.get("StepID")+"】报工！");
						Clear();
						return false;
					}	
					if (lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
                    {
              		  MESCommon.show(WIP_InStock.this,"请刷入制造号码!");
              		  Clear();
					  setFocus(editCompID);
                      return false;
                    }
					lsProduct.clear();
					String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
					String sError= db.GetData(sSql,  lsProduct);
					 if (sError != "") {
							MESCommon.showMessage(WIP_InStock.this, sError);
							return false;
						 }
					if(!lsProcess.get(0).get("CUSTOMERNAME").toString().equals(""))
					{ 
						tvCM.setVisibility(0);
						editCustomerName.setVisibility(0);			
						editCustomerName.setText(lsProcess.get(0).get("CUSTOMERNAME").toString());
					}else {
						tvCM.setVisibility(8);
						editCustomerName.setVisibility(8);		
					}
					//初始化工件信息
					editCompID.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					editMaterialID.setText(lsProcess.get(0).get("PRODUCTID").toString());
					editProductName.setText(lsProduct.get(0).get("PRODUCTNAME").toString());					
					//改变默认的单行模式  
					editProductName.setSingleLine(false);  
					//水平滚动设置为False  
					editProductName.setHorizontallyScrolling(false);	
					editColer.setText(lsProcess.get(0).get("COLER").toString());
					editDescripTion.setText(lsProcess.get(0).get("PMMESSAGE").toString());
					editDescripTion.setSingleLine(false);
					editDescripTion.setHorizontallyScrolling(false);
					msProductOrderId = lsProcess.get(0).get("PRODUCTORDERID").toString();
					msProductId = lsProcess.get(0).get("PRODUCTID").toString();
					msProductCompId = lsProcess.get(0).get("PRODUCTCOMPID").toString();
					msProductSerialNumber = lsProcess.get(0).get("PRODUCTSERIALNUMBER").toString();
					msStepId = lsProcess.get(0).get("STEPID").toString();
					msStepSEQ = lsProcess.get(0).get("STEPSEQ").toString();
					msEqpId = lsProcess.get(0).get("EQPID").toString();
					miQty = Integer.parseInt(lsProcess.get(0).get("STARTQTY").toString().trim());
					
					
					List<HashMap<String, String>> lsINSTOCK = new ArrayList<HashMap<String, String>>();
					
					sSql=" SELECT * FROM PROCESS_INSTOCK WHERE PRODUCTORDERID='"+msProductOrderId+"' AND PRODUCTCOMPID='"+msProductCompId+"' ;";
					sError= db.GetData(sSql,  lsINSTOCK);
					 if (sError != "") {
							MESCommon.showMessage(WIP_InStock.this, sError);	
							 Clear();
							setFocus(editCompID);
							return false;
						 }
					 if(lsINSTOCK.size()>0)
					 {
						
							MESCommon.showMessage(WIP_InStock.this, "制造号码：【"+msProductCompId+"】,已经入库完成请重新选择要入库的制造号码！");							
							setFocus(editCompID);
							 Clear();
							return false;
					 }
					setFocus(editCompID);
				}
				return false;
			}
		});
		// editDescripTion
		editCompID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try {
					editInput.setText("");
					setFocus(editInput);
					
				} catch (Exception e) {

				}
			}
		});

		// btnOK

		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					if (lsCompID.size() <= 0) {
						MESCommon.show(WIP_InStock.this, "请先扫描条码在进行入库报工！");
						return;
					}	
					SpinnerData spW = (SpinnerData) spWareh.getSelectedItem();
					List<HashMap<String, String>> lsStepwrcode = new ArrayList<HashMap<String, String>>();
					String  sSql="SELECT * FROM MSTEP_MERPSTEP WHERE STEPID ='"+params.get("StepID").toString()+"'  ;";
					String sError= db.GetData(sSql,  lsStepwrcode);
					 if (sError != "") {
							MESCommon.showMessage(WIP_InStock.this, sError);
							 Clear();
							return ;
						 }
					 String sWrcode = "";
					 if(lsStepwrcode.size()>0)
					 {
						 sWrcode= lsStepwrcode.get(0).get("WRCODE").toString();
					 }
						
		            String sInPno = MESDB.ERPInStorage(msProductOrderId, msProductCompId,MESCommon.UserId, sWrcode, spW.value);
		            
                    if(!sInPno.equals(""))
                    {
                    	if (sInPno.substring(0, 2).equals("OK")) {
                    		 InsertINSTOCK();
                    		 Toast.makeText(WIP_InStock.this,"入库成功，入库单号：" +sInPno.substring(1, sInPno.length()-2), Toast.LENGTH_SHORT).show();	
						}else if (sInPno.substring(0, 2).equals("NG"))  {
							
							MESCommon.showMessage(WIP_InStock.this, sInPno.substring(1, sInPno.length()-2));
							return;
						}
                       
                    }
				    //执行最终判定                
                    Clear();            
				
				} catch (Exception e) {
					MESCommon.showMessage(WIP_InStock.this, e.toString());
				}
			}
		});


		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_InStock.this, e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(WIP_InStock.this, e.toString());
		}
	}

	public class SpinnerData {

		private String value = "";
		private String text = "";

		public SpinnerData() {
			value = "";
			text = "";
		}

		public SpinnerData(String _value, String _text) {
			value = _value;
			text = _text;
		}

		@Override
		public String toString() {

			return text;
		}

		public String getValue() {
			return value;
		}

		public String getText() {
			return text;
		}
	}

	private  String InsertINSTOCK()
	{
		try {
			SpinnerData spW = (SpinnerData) spWareh.getSelectedItem();
				String  sSql="";
			    	sSql = sSql + "INSERT INTO PROCESS_INSTOCK (PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID,QTY,ISFINISH,STOCKTYPE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES (  '"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msProductId + "','1','Y','"
                            + spW.value + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
				    
			    	 String sError= db.ExecuteSQL(sSql);
					 if (sError != "") {							
							return sError;
						 }					
	
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_InStock.this, e.toString());
			return e.toString();
		}
		return "";
	}
	
	public void Clear() {
		try {
			editInput.setText("");
			lsCompID.clear();
			lsCompTable.clear();
			lsProcess.clear();
			msProductOrderId = "";
			msProductId = "";
			msProductCompId = "";
			msProductSerialNumber = "";
			msStepId = "";
			msEqpId = "";
			msAnalysisformsID = "";
			msSampletimes = "";
			msStepSEQ = "";
			miQty = 0;
			editColer.setText("");
			editCompID.setText("");
			editCustomerName.setText("");
			editDescripTion.setText("");
			editInput.setText("");;
			editMaterialID.setText("");
			editProductName.setText("");
			setFocus(editInput);
		} catch (Exception e) {
			MESCommon.showMessage(WIP_InStock.this, e.toString());
		}

	}

	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(WIP_InStock.this, e.toString());
		}

	}

}
