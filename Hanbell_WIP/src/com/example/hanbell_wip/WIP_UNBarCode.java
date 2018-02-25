package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Setting.SpinnerData;



import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
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

public class WIP_UNBarCode extends Activity {

	private MESDB db = new MESDB();

	Button  btnExit, btnUnbind;
	EditText editInput,editProductCompID,editMaterialID,editProductModel ,editProductSerNumber,editRawNumber,editFinNumber;
	
	// 该物料的HashMap记录
	static int milv0RowNum = 0;

	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsCompID3 = new ArrayList<HashMap<String, String>>();	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_unbar_code);
try{
		// 取得控件
		editInput = (EditText) findViewById(R.id.wipunBarcode_tvInput);	
		editProductCompID = (EditText) findViewById(R.id.wipunBarcode_tvProductCompid);	
		editMaterialID = (EditText) findViewById(R.id.wipunBarcode_tvMaterialid);	
		editProductModel= (EditText) findViewById(R.id.wipunBarcode_tvProductModel);	
		editProductSerNumber= (EditText) findViewById(R.id.wipunBarcode_tvProductSernumber);
		editRawNumber= (EditText) findViewById(R.id.wipunBarcode_tvRawnumber);
		editFinNumber= (EditText) findViewById(R.id.wipunBarcode_tvFinNumber);
		btnExit = (Button) findViewById(R.id.wipunBarcode_btnExit);			
		btnUnbind=(Button) findViewById(R.id.wipunBarcode_btnUnbind);

		// ***********************************************Start
		
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("制造号码解绑");
	
		// 取得控件	 
		String date = sDateFormatShort.format(new java.util.Date());	
	
		
		
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipunBarcode_tvInput);
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_UNBarCode.this, "请扫描条码!");
						txtInput.setText("");
						setFocus(editProductCompID);
						return false;
					}					
					editInput.setText(txtInput.getText().toString().trim().toUpperCase());
					lsCompID.clear();						
					String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","制造号码","装配", lsCompID);
				    if (!sResult.equals(""))
                    {   MESCommon.show(WIP_UNBarCode.this,sResult);
                        setFocus(editProductCompID);
				    	return false;
                    }
	            	if (lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
	                {
            		   MESCommon.show(WIP_UNBarCode.this,"请刷入制造号码!");
            		   txtInput.setText("");
            		   setFocus(editProductCompID);
                       return false;
	                }
	            	lsProcess.clear();
					String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
					sResult = db.GetProductProcess(sOrderID,txtInput.getText().toString().trim(), lsProcess);
					if (sResult != "") {
						MESCommon.showMessage(WIP_UNBarCode.this, sResult);
						finish();							
					}			
					if(lsProcess.size()==0)
					{
						MESCommon.show(WIP_UNBarCode.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
						Clear();
						return false;
					}
					lsProduct.clear();
					String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
					String sError= db.GetData(sSql,  lsProduct);
					 if (sError != "") {
							MESCommon.showMessage(WIP_UNBarCode.this, sError);
							return false;
						 }
					 lsCompID3.clear();
					 sSql=" SELECT * FROM STKCOMP WHERE COMPID3 ='"+lsCompID.get(0).get("PRODUCTCOMPID").toString()+"'  ;";
				     sError= db.GetData(sSql,  lsCompID3);
						 if (sError != "")
						 {
								MESCommon.showMessage(WIP_UNBarCode.this, sError);
								return false;
						 }
						 
					//初始化工件信息
					editProductCompID.setText(lsCompID.get(0).get("PRODUCTCOMPID").toString());
					editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());						
					editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());	
					if(lsCompID3.size()>0)
					{
						editProductSerNumber.setText(lsCompID3.get(0).get("PRODUCTCOMPID").toString());	
						editRawNumber.setText(lsCompID3.get(0).get("PRODUCTCOMPID").toString());
						editFinNumber.setText(lsCompID3.get(0).get("PRODUCTCOMPID").toString());
					}else {
						editProductSerNumber.setText(lsCompID.get(0).get("COMPIDSEQ").toString());	
						editRawNumber.setText(lsCompID.get(0).get("RAWPROCESSID").toString());
						editFinNumber.setText(lsCompID.get(0).get("FINEPROCESSID").toString());
					}					
					setFocus(editProductCompID);
					} catch (Exception e) {
						MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
						return false;
					}
				}
					
				return false;
			}
		});
	
		editProductCompID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				setFocus(editInput);
			}
		});
	

		   // btnUnbind
		btnUnbind.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (editProductCompID.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_UNBarCode.this, "请扫描条码!");
						setFocus(editProductCompID);
						return ;
					}					
					
					AlertDialog alert=	new AlertDialog.Builder(WIP_UNBarCode.this).setTitle("确认").setMessage("确定要对制造号码【"+editProductCompID.getText().toString()+"】,进行解绑作业？")
							.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					                // TODO Auto-generated method stub  
					            	return ;
					            }  
					 })
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
			            @Override  
			            public void onClick(DialogInterface dialog,int which) {  
			                // TODO Auto-generated method stub  
			            	String   sSQL="";	
			            	if(lsCompID3 .size()>0)
			            	{
			            		 sSQL =  "UPDATE STKCOMP SET COMPID3 ='' WHERE  COMPID3='"+editProductCompID.getText().toString()+"' ;";	
			            	}
			            	else 
			            	{
			            		sSQL =  " UPDATE STKCOMP SET PRODUCTCOMPID ='' WHERE  PRODUCTCOMPID='"+editProductCompID.getText().toString()+"' ; " ;
							}  
			            	   sSQL =sSQL+  " INSERT INTO PROCESS_STEP_REPLACE (SYSID,PRODUCTORDERID,PRODUCTCOMPID,STEPID,EQPID,PRODUCTSERIALNUMBER_OLD,  PRODUCTSERIALNUMBER, PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,MODIFYUSERID,MODIFYUSER,MODIFYTIME) " +
			                        "SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID,  STEPID,  EQPID, PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER , PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, '"+MESCommon.UserId+"', '"+MESCommon.UserName+"', "+MESCommon.ModifyTime+" FROM PROCESS_STEP_P " +
			                        "WHERE  PRODUCTCOMPID='"+editProductCompID.getText().toString()+"'  AND PRODUCTSERIALNUMBER ='" +editProductSerNumber.getText().toString() + "'   ;";
			            	 
			            	   for(int i=0;i<lsCompID3.size();i++)
			            		{
					            	 if(!lsCompID3.get(i).get("PRODUCTCOMPID").toString().equals(""))
					            	 {
					            		 sSQL =  sSQL+ " UPDATE PROCESS_STEP_PF SET PRODUCTORDERID='',PRODUCTCOMPID='' ,PRODUCTSERIALNUMBER_OLD='', PRODUCTSERIALNUMBER=''  WHERE PRODUCTCOMPID='"+editProductCompID.getText().toString()+"'  AND PRODUCTSERIALNUMBER='"+lsCompID3.get(i).get("COMPIDSEQ").toString()+"' ;  " ;
					            		  sSQL =  sSQL+ "  DELETE FROM PROCESS_STEP_P WHERE PRODUCTCOMPID='"+editProductCompID.getText().toString()+"' AND PRODUCTSERIALNUMBER ='" +lsCompID3.get(i).get("PRODUCTCOMPID").toString()+ "'  ; "  ;
					            		
					            	 }else  if(!lsCompID3.get(i).get("COMPIDSEQ").toString().equals("")){
					            		  sSQL =  sSQL+ " DELETE FROM PROCESS_STEP_P WHERE PRODUCTCOMPID='"+editProductCompID.getText().toString()+"' AND PRODUCTSERIALNUMBER ='" +lsCompID3.get(i).get("COMPIDSEQ").toString()+ "'  ; "  ;
									}
			            	     }
			            	   
			                sSQL =sSQL+  "DELETE PROCESS_STEP_P WHERE  PRODUCTCOMPID='"+editProductCompID.getText().toString()+"'  AND PRODUCTSERIALNUMBER ='" +editProductSerNumber.getText().toString() + "' ; " ;

				            String sError = db.ExecuteSQL(sSQL);
							 if (sError != "") {
								MESCommon.showMessage(WIP_UNBarCode.this, sError);
								return;
							 } 
							 Toast.makeText(WIP_UNBarCode.this, "解绑成功!", Toast.LENGTH_SHORT).show();
			                  
				            Clear();
			            }  
			        }) .show() ;
					
				
				} catch (Exception e) {
					MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
				}
			}
		});

        // btnExit
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
				}
			}
		});
	
		} catch (Exception e) {
			MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
		}

	}


	

	public void Clear() {
		try {
		
			
			editProductCompID.setText("");
			editMaterialID.setText("");						
			editProductModel.setText("");		
			editProductSerNumber.setText("");	
			editRawNumber.setText("");
			editFinNumber.setText("");
            lsCompID .clear();lsProcess .clear(); 
            lsProduct.clear();
            lsCompID3.clear();
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e) {
			MESCommon.showMessage(WIP_UNBarCode.this, e.toString());
		}
	}		
		
}

