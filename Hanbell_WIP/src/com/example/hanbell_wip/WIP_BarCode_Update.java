package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.WIP_TrackIn.SpinnerData;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.RatingBar.OnRatingBarChangeListener;
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

public class WIP_BarCode_Update extends Activity {

	private MESDB db = new MESDB();

	ListView lv;
	Button btnConfirm, btnExit, btnRemove,btnClear;
	CheckBox cksd,ckdl,ckfl;
	EditText editInput,editPlanTime, editProductCompid, editSerialnumberID,editSerialnumberIDTwo;
	TextView h0, h1, h2 ,tvOne,tvTwo;
	WIPBarcodeAdapter adapter;
	PrefercesService prefercesService;
	Map<String, String> params;
	String msPROCESS_PRODUCTID = "", msPROCESS_PRODUCTNAME = "",
			msOrderID = "", msPRODUCTID = "",msOrderType="",msCOMPIDSEQ="",msBomflag="";
	static int miRowNum = 0;
	int miQty = 0;
	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> mlsDetail = new ArrayList<HashMap<String, String>>();	//物料明细
	List<HashMap<String, String>> mls = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> lscheckSDDL = new ArrayList<HashMap<String, String>>();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_barcode_update);
		try{
		prefercesService = new PrefercesService(this);
		params = prefercesService.getPreferences();
		ActionBar actionBar = getActionBar();
		actionBar.setSubtitle("报工人员：" + MESCommon.UserName);
		actionBar.setTitle(params.get("StepName"));
		// 取得控件

		editInput = (EditText) findViewById(R.id.wipbarcode_tvInput);
		editPlanTime = (EditText) findViewById(R.id.wipbarcode_tvPlanTime);
		editProductCompid = (EditText) findViewById(R.id.wipbarcode_tvProductCompid);
		editSerialnumberID = (EditText) findViewById(R.id.wipbarcode_tvSerialnumber);
		editSerialnumberIDTwo= (EditText) findViewById(R.id.wipbarcode_tvSerialnumbertwo);
		cksd= (CheckBox) findViewById(R.id.wipbarcode_ckboxsd);
		ckdl= (CheckBox) findViewById(R.id.wipbarcode_ckboxdl);
		ckfl= (CheckBox) findViewById(R.id.wipbarcode_ckboxfl);
		h0 = (TextView) findViewById(R.id.wipbarcode_h0);
		h1 = (TextView) findViewById(R.id.wipbarcode_h1);
		h2 = (TextView) findViewById(R.id.wipbarcode_h2);
		tvOne= (TextView) findViewById(R.id.wipbarcode_tvone);
		tvTwo= (TextView) findViewById(R.id.wipbarcode_tvtwo);
		h0.setTextColor(Color.WHITE);
		h0.setBackgroundColor(Color.DKGRAY);
		h1.setTextColor(Color.WHITE);
		h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE);
		h2.setBackgroundColor(Color.DKGRAY);
		h2.setBackgroundColor(Color.DKGRAY);

		lv = (ListView) findViewById(R.id.wipbarcode_lv);
		btnConfirm = (Button) findViewById(R.id.wipbarcode_btnConfirm);
		btnRemove = (Button) findViewById(R.id.wipbarcode_btnRemove);
		btnExit = (Button) findViewById(R.id.wipbarcode_btnExit);
		btnClear=(Button) findViewById(R.id.wipbarcode_btnClear);
		adapter = new WIPBarcodeAdapter(lsCompTable, this);
		lv.setAdapter(adapter);
	   	tvTwo.setVisibility(8);
    	editSerialnumberIDTwo.setVisibility(8);

		String date = sDateFormatShort.format(new java.util.Date());
		// 读取报工人员
		String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"
				+ params.get("EQPID")
				+ "' AND ISNULL(WORKDATE,'')='"
				+ date
				+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
		String sResult = db.GetData(sSql, lsuser);
		if (sResult != "") {
			MESCommon.showMessage(WIP_BarCode_Update.this, sResult);
			finish();
		}

		if (lsuser.size() == 0) {
			MESCommon.showMessage(WIP_BarCode_Update.this, "请先进行人员设备报工！");

		}
		 if(!params.get("StepID").contains("发料") &&!params.get("StepID").contains("领料"))
		 {
			 MESCommon.show(WIP_BarCode_Update.this, "当前工站不为发料站，请调整后再进行发料作业！！");
		 }
			//滑块站需要看滑块
			if (params.get("StepName").contains("机体") && !params.get("StepName").contains("P机"))
			{   //0,显示；
				cksd.setVisibility(0);
				ckdl.setVisibility(0);
				ckfl.setVisibility(0);
		    	lv.getLayoutParams().height=270;
			}else {
				//8,不显示，不占位置
				cksd.setVisibility(8);
				ckdl.setVisibility(8);
				ckfl.setVisibility(8);
				lv.getLayoutParams().height=310;
			}
		 setFocus(editInput);

		// ***********************************************Start
		// 控件事件
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				WIPBarcodeAdapter.ViewHolder holder = (WIPBarcodeAdapter.ViewHolder) arg1
						.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				WIPBarcodeAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				// 将CheckBox的选中状况记录下来
				if (holder.cb.isChecked()) {
					lsCompTable.get(position).put("CHECKFLAG", "Y");
				} else {
					lsCompTable.get(position).put("CHECKFLAG", "N");
				}
			}
		});
		
		cksd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
                if(isChecked){ 
                	ckdl.setChecked(false);
                	ckfl.setEnabled(true);
                	tvOne.setText("一段机体");
                	tvTwo.setVisibility(0);
                	editSerialnumberIDTwo.setVisibility(0);
                	lv.getLayoutParams().height=210;
                	//一dip相当于2的后端设定
                	Toast.makeText(WIP_BarCode_Update.this, "确认一段/二段机已经组装完成!",Toast.LENGTH_SHORT).show();
                }else {
          		ckfl.setEnabled(false);
              	ckfl.setChecked(false);
              	tvOne.setText("机壳条码");
            	tvTwo.setVisibility(8);
            	    editProductCompid.setText("");
					editSerialnumberID.setText("");
					editSerialnumberIDTwo.setText("");	
					msOrderID="";
					msOrderType="";
            	editSerialnumberIDTwo.setVisibility(8);
            	lv.getLayoutParams().height=270;
				}
            } 
        }); 
		ckdl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub 
            	  if(isChecked){ 
                  	cksd.setChecked(false);
                  	ckfl.setEnabled(true);
                 	tvOne.setText("一段机体");
                  }else {
            		ckfl.setEnabled(false);
                	ckfl.setChecked(false);
                   	tvOne.setText("机壳条码");
                    editProductCompid.setText("");
					editSerialnumberID.setText("");
					editSerialnumberIDTwo.setText("");	
					msOrderID="";
					msOrderType="";
				}
            } 
        }); 
	
		ckfl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override 
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                // TODO Auto-generated method stub               
                if(cksd.isChecked()||ckdl.isChecked()){ 
                	ckfl.setEnabled(true);
                }else {
                	ckfl.setEnabled(false);
                	ckfl.setChecked(false);
                	
				}
            } 
        }); 
		
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {						
					
					EditText txtInput = (EditText) findViewById(R.id.wipbarcode_tvInput);
					String strCompID = txtInput.getText().toString().trim().toUpperCase();
					editInput.setText(strCompID);
					if (lsuser.size() == 0) {
						MESCommon.showMessage(WIP_BarCode_Update.this,"请先进行人员设备报工！");
						return false;
					}
					 if(!params.get("StepID").contains("发料") &&!params.get("StepID").contains("领料"))
					 {
						 MESCommon.show(WIP_BarCode_Update.this, "当前工站不为发料站，请调整后再进行发料作业！！");
							editInput.setText("");
							setFocus(editInput);
					     return false;
					 }

					if (strCompID.length() == 0) {
						MESCommon.show(WIP_BarCode_Update.this, "请扫描条码!");
						txtInput.setText("");
						return false;
					}					
					// 绑定制造号码；
					if (editProductCompid.getText().toString().equals("")) {

						for (int i = 0; i < lsCompTable.size(); i++) {
							if (lsCompTable.get(i).get("ProductCompID").toString().equals(txtInput.getText().toString().trim())) {
								MESCommon.show(WIP_BarCode_Update.this, "制造号码["+ txtInput.getText().toString().trim()+ "] 已在清单中,请选择新的制造号码!");
								editInput.setText("");
								setFocus(editInput);
								return false;
							}

						}
                        //查核制令是否下线
						String sError = CheckProductOrder(editInput.getText().toString());
						if (!sError.equals("")) {
							MESCommon.show(WIP_BarCode_Update.this, sError);
							editInput.setText("");
							setFocus(editInput);
							return false;
						}
						
						List<HashMap<String, String>> lsPlan = new ArrayList<HashMap<String, String>>();
						lsPlan.clear();
						String sSQL = "SELECT  A.PRODUCTORDERID,A.PRODUCTCOMPID, B.PRODUCTTIME,B.PRODUCTORDERTYPE   FROM PROCESS A  INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID AND A.PROCESSSTATUS<>'已完成'  WHERE A.PRODUCTCOMPID='"+editInput.getText().toString()+"'  ";
						sError = db.GetData(sSQL, lsPlan);
						if (!sError.equals("")) {
							MESCommon.show(WIP_BarCode_Update.this, sError);
							editInput.setText("");
							setFocus(editInput);
							return false;
						}
						if(lsPlan.size()>0)
						{
							editPlanTime.setText(lsPlan.get(0).get("PRODUCTTIME").toString());
							msOrderID=lsPlan.get(0).get("PRODUCTORDERID").toString();
							msOrderType=lsPlan.get(0).get("PRODUCTORDERTYPE").toString();
							
						}
						//查核生管是否设定机壳主料
						sError = CheckProductCOMP(editInput.getText().toString());
						if (!sError.equals("")) {
							MESCommon.show(WIP_BarCode_Update.this, sError);
							editInput.setText("");
							setFocus(editInput);
							return false;
						}
						
						if(lsPlan.size()>0)
						{
							editPlanTime.setText(lsPlan.get(0).get("PRODUCTTIME").toString());
						}
						editProductCompid.setText(editInput.getText().toString());
						editInput.setText("");

					}
					// 绑定机壳/制造号码，双段或者动力。需要绑定制造号码
					else {
						if(!ckfl .isChecked())
						{
							lsCompID.clear();
							String sResult="";
							String sNumString="";
							lscheckSDDL.clear();
							if(cksd.isChecked()||ckdl.isChecked())
							{
							   //查核双段的绑定信息。
							
								//20170823因为双段机改变了生产工艺，会把一段二段机体料同时发下去，组织后在合成双段机。也有库存的机头需要刷。
								//只去seq和PRODUCTCOMPID
								String sSQL = "SELECT * FROM STKCOMP  WHERE PRODUCTCOMPID='"+editInput.getText().toString().toUpperCase()+"'  OR COMPIDSEQ= '"+editInput.getText().toString().toUpperCase()+"'  ";
								String sError = db.GetData(sSQL, lscheckSDDL);
								if(!sError.equals(""))
								{
									MESCommon.show(WIP_BarCode_Update.this, sError);
									editInput.setText("");
									setFocus(editInput);
									return false;
								}
								if(lscheckSDDL.size()>0)
								{
									if(!lscheckSDDL.get(0).get("COMPID3").toString().equals(""))
									{
									    MESCommon.show(WIP_BarCode_Update.this, "条码:【"+editInput.getText().toString().toUpperCase()+"】,已经被双段/动力:【"+lscheckSDDL.get(0).get("COMPID3").toString()+"】绑定！");
										editInput.setText("");
										setFocus(editInput);
										return false;
									}
									//制造号码栏位不为空,并且等于制造号码，证明刷入的是机体制造号码
									if(!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals("")&&lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals(editInput.getText().toString().toUpperCase()))
									{
										sNumString=lscheckSDDL.get(0).get("PRODUCTCOMPID").toString();
										
									}
									else  if(!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals("")&&!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals(lscheckSDDL.get(0).get("COMPID2").toString()))
									{//这种情况肯定是刷入了机壳条码。并且绑定了一个整机了。
										MESCommon.show(WIP_BarCode_Update.this, "机壳条码:【"+editInput.getText().toString().toUpperCase()+"】,已经被【"+lscheckSDDL.get(0).get("PRODUCTCOMPID").toString()+"】 绑定，请确认！！");
										editInput.setText("");
										setFocus(editInput);
										return false;
									}
									else 
									{
										sNumString=lscheckSDDL.get(0).get("COMPIDSEQ").toString();
									}
								}else
								{
								    MESCommon.show(WIP_BarCode_Update.this, "条码:【"+editInput.getText().toString().toUpperCase()+"】,在系统中无记录，条码只能是制造号码，或者条码编号！");
									editInput.setText("");
									setFocus(editInput);
									return false;
								} 
							}
							//一般发料
							else {
							   sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim().toUpperCase(), "", "", "QF","零部件","装配",  lsCompID);
							   if (!sResult.equals("")) {
									MESCommon.show(WIP_BarCode_Update.this, sResult);
									editInput.setText("");
									setFocus(editInput);
									return false;
								}
							   if(lsCompID.size()>0)
							   {
							      sNumString=lsCompID.get(0).get("FINEPROCESSID").toString();
							   }
								
							}
							 for (int i = 0; i < lsCompTable.size(); i++) {
									if (lsCompTable.get(i).get("Serialnumber").toString().equals(sNumString)) {
										MESCommon.show(WIP_BarCode_Update.this, "条码["+ txtInput.getText().toString().trim()+ "] 已在清单中,请确认!");
										editInput.setText("");
										setFocus(editInput);
										return false;
									}
		
								}
							String	sError="";
							if(!cksd.isChecked()&&!ckdl.isChecked())
							{
								//一般机查核主料是否一致
								//查核机壳是否与生管设定的主料一致,是否绑定过制造号码！
								if (msPROCESS_PRODUCTID.equals("")) {
									MESCommon.show(WIP_BarCode_Update.this,  "制造号码，没有设定机壳主料！");
									return false;
								}
								
								sError=ChecksBOM( lsCompID,editInput.getText().toString().trim().toUpperCase());
							}
							if (!sError.equals("")) 
							{

								 AlertDialog alert=	new AlertDialog.Builder(WIP_BarCode_Update.this).setTitle("确认").setMessage(sError)
											.setPositiveButton("加入",new DialogInterface.OnClickListener() {  
							            @Override  
							            public void onClick(DialogInterface dialog,int which) {  
							                // TODO Auto-generated method stub  
							            	if(cksd.isChecked())
											{			                     
												if(!editSerialnumberID.getText().toString().equals(""))
												{
													editSerialnumberIDTwo.setText(editInput.getText().toString());
												}else  {
													editSerialnumberID.setText(editInput.getText().toString());   
												}
											}else if (ckdl.isChecked())
											{
												if(editSerialnumberID.getText().toString().equals(""))
												{
													editSerialnumberID.setText(editInput.getText().toString());
												}
											}else 
											{
												editSerialnumberID.setText(editInput.getText().toString());
											}
							            	msBomflag="N";
							            	String sError =BindHashMap();
											if (sError != "") {
												MESCommon.showMessage(WIP_BarCode_Update.this, sError);
												return ;
											}
							            }  
							        })  
									.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
									            @Override  
									            public void onClick(DialogInterface dialog,int which) {  
									                // TODO Auto-generated method stub  
									            	return ;
									            }  
									 }).show();  
						
							}	
							//查核一致
							else 
							{
								if(cksd.isChecked())
								{			                     
									if(!editSerialnumberID.getText().toString().equals(""))
									{
										editSerialnumberIDTwo.setText(editInput.getText().toString());
									}else  {
										editSerialnumberID.setText(editInput.getText().toString());   
									}
								}else if (ckdl.isChecked())
								{
									if(editSerialnumberID.getText().toString().equals(""))
									{
										editSerialnumberID.setText(editInput.getText().toString());
									}
								}else {
									editSerialnumberID.setText(editInput.getText().toString());
								}
								 sError =BindHashMap();
								if (sError != "") {
									MESCommon.showMessage(WIP_BarCode_Update.this, sError);
									return false;
								}
							}	
						}					
						else
						{
							editSerialnumberID.setText(editInput.getText().toString().toUpperCase());   
							String sError =BindHashMap();
							if (sError != "") {
								MESCommon.showMessage(WIP_BarCode_Update.this, sError);
								return false;
							}
						}
							
					}
				
				} catch (Exception e) {
					MESCommon.show(WIP_BarCode_Update.this, e.toString());
					editInput.setText("");
					setFocus(editInput);
					return false;
				}
			}
			return false;
		}
	});

		editPlanTime
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						// TODO Auto-generated method stub
						setFocus(editInput);
					}
				});

		// btnOK

		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
					String sLastProductCompid="";
					if (lsCompTable.size() <= 0) {
						MESCommon.show(WIP_BarCode_Update.this, "请先扫描条码在进行报工！");
						return;
					}				
					for (int i = 0; i < lsCompTable.size(); i++)
					{
						lsProcess.clear();
						String sResult = db.GetProductProcess("",lsCompTable.get(i).get("ProductCompID").toString(),lsProcess);
						if (sResult != "") {
							MESCommon.showMessage(WIP_BarCode_Update.this,sResult);
							finish();
						}
						if (lsProcess.size() == 0) {
							MESCommon.showMessage(WIP_BarCode_Update.this,	"制造号码：【"+ lsCompTable.get(i).get("ProductCompID").toString()+ "】,没有设定生产工艺流程");
							return;
						}
						String sProductOrderid=lsProcess.get(0).get("PRODUCTORDERID").toString();
						String sProductCompId = lsProcess.get(0).get("PRODUCTCOMPID").toString();
						String sProductId = lsProcess.get(0).get("PRODUCTID").toString();
						String sStepId = lsProcess.get(0).get("STEPID").toString();
						String sEqpId =params.get("EQPID").toString();
						String sStepSEQ = lsProcess.get(0).get("STEPSEQ").toString();						
						//不需要判定是不是重工制令，都要发料，过站
                        if(sStepId.equals("机体领料站") || sStepId.equals("冷媒领料站") || sStepId.equals("P机体领料站") || sStepId.equals("P机组领料站"))
                        {			
                        	String sSerialnumberID="";
                        	//库存直接发料Y,
                        	if(lsCompTable.get(i).get("ZJFL").toString().equals("Y"))
                        	{	
                        		List<HashMap<String, String>> lsSysid = new ArrayList<HashMap<String, String>>();
                        		String sCompidSEQ="";
                        		String sSysid="";
                        		String sSQL="SELECT " + MESCommon.SysId +" AS SYSID";
                        		String sError = db.GetData(sSQL, lsSysid);
    							if (sError != "") {
    								MESCommon.showMessage(WIP_BarCode_Update.this, sError);
    								return ;
    							}
    							if (lsSysid.size()>0) {
    								sSysid=lsSysid.get(0).get("SYSID").toString();
    								sCompidSEQ="C"+lsSysid.get(0).get("SYSID").toString();
								}
    							
                        		sSQL=" DELETE FROM STKCOMP  WHERE  PRODUCTCOMPID ='"+lsCompTable.get(i).get("SerialnumberID").toString()+"' ;";
                        		sSQL = sSQL +  " INSERT INTO STKCOMP (FROMID, COMPID, COMPIDSEQ, STATUS, LOTQTY, ITNBR, ITDSC, USERNO, KEYINDATE, PRODUCTCOMPID, MODIFYUSERID, MODIFYUSER, MODIFYTIME)"+
                        		" VALUES ('"+sSysid+"',  '"+sCompidSEQ+"', '"+sCompidSEQ+"', 'QF', 1, '"+lsCompTable.get(i).get("MATERIALID").toString()+"', '"+lsCompTable.get(i).get("MATERIALNAME").toString()+"', '"+MESCommon.UserName+"',"+ MESCommon.ModifyTime+", '"+lsCompTable.get(i).get("SerialnumberID").toString()+"', '"+MESCommon.UserId+"', '"+MESCommon.UserName+"', "+ MESCommon.ModifyTime+") ";
                        		 sError= db.ExecuteSQL(sSQL);
                       		 if (sError != "") {
                       				MESCommon.showMessage(WIP_BarCode_Update.this, sError);
    								return ;
                       			 }
                        	}
                    		if(lsCompTable.get(i).get("SDDL").toString().equals("Y"))
                        	{
	                    		sSerialnumberID=lsCompTable.get(i).get("SerialnumberID").toString()+","+"Y";								 
	                    	}else
	                    	{
	                    		sSerialnumberID=lsCompTable.get(i).get("SerialnumberID").toString();
	                    	}							
                        	if(!sLastProductCompid.equals(""))
                        	{
                        		if(sLastProductCompid==sProductCompId)
                        		{
                        			return;	                        			
                        		}	                        	
                        	}
                        	
                            String sSetStepInResule = db.SetStepInbyJWJ(sProductOrderid, sProductCompId,sSerialnumberID, sStepId, sStepSEQ,sEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName,1, "");
                            if (!sSetStepInResule.equals("")) 
							{
								MESCommon.show(WIP_BarCode_Update.this, sSetStepInResule);
								return;
							}
							String sSetStepOutResule = db.SetStepOutbyJWJ(sProductOrderid,sProductCompId, sStepId, sStepSEQ, sEqpId, "",	"", "", "", MESCommon.UserId,MESCommon.UserName, 1, 0, 0, 0,"", false);
							if (!sSetStepOutResule.equals("")) 
							{
								MESCommon.show(WIP_BarCode_Update.this, sSetStepOutResule);
								return;
							}
							else {
								//只有不是双段或者动力的才进行机壳的出库记录
								if(	!lsCompTable.get(i).get("SDDL").toString().equals("Y"))
								{
									sResult = GetScanInfo(lsCompTable.get(i).get("SerialnumberID").toString());									
								}
							}
							sLastProductCompid=sProductCompId;
						}
                        else 
                        {	//双段或者解绑后重新绑定，都会存在工序不在领料站。	
                        	String sSql="";

    							//制造号码已过站，重新绑定新的机壳时候只需要绑定即可！
                        	   if(lsCompTable.get(i).get("SDDL").toString().equals("Y"))
                            	{
    								sSql = "UPDATE STKCOMP SET COMPID3='"+ sProductCompId+ "' WHERE PRODUCTCOMPID='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ; UPDATE STKCOMP SET COMPID3='"+ sProductCompId+ "' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ;";
    								sResult=db.ExecuteSQL(sSql);
                            	}else
                            	{
                            		sSql = "UPDATE STKCOMP SET PRODUCTCOMPID='"+ sProductCompId+ "' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ;";
    								sResult=db.ExecuteSQL(sSql);
                            	}
							
                        	
				            //更新COMPID3没成功！
							if (!sResult.equals("")) 
							{
								  if(lsCompTable.get(i).get("SDDL").toString().equals("Y"))
	                        	{
									sSql = "UPDATE STKCOMP SET COMPID3='' WHERE COMPID3='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ; UPDATE STKCOMP SET COMPID3='' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ; ";
									db.ExecuteSQL(sSql);
	                        	}else {
	                        		sSql = "UPDATE STKCOMP SET PRODUCTCOMPID='' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ;";
									db.ExecuteSQL(sSql);
								}
								MESCommon.show(WIP_BarCode_Update.this, sResult);
								return;
							}
							//绑定发料成功后加入出库作业扣帐逻辑
							else 
							{
								//只有不是双段或者动力的才进行机壳的出库记录
								if(	!lsCompTable.get(i).get("SDDL").toString().equals("Y"))
								{
									sResult = GetScanInfo(lsCompTable.get(i).get("SerialnumberID").toString());									
								}
							}
						}
					
                    	if(lsCompTable.get(i).get("OrderType").toString() .equals("一般制令")){
                           	sResult=InsertSTEP_P(lsCompTable, i, sProductOrderid,sProductCompId,sProductId,sStepId,sStepSEQ,sEqpId);
						}
						if (!sResult.equals("")) 
						{
							MESCommon.show(WIP_BarCode_Update.this, sResult);
							return;
						}
				    }
						
					Toast.makeText(WIP_BarCode_Update.this, "发料成功!",Toast.LENGTH_SHORT).show();
					Clear();

				} catch (Exception e) {
					MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
				}
			}
		});

		// btnRemove

		btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (lsCompTable.size() > 0) {
						List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
						Boolean isSelect = false;

						for (int i = 0; i < lsCompTable.size(); i++) {
							if (lsCompTable.get(i).get("CHECKFLAG").toString().equals("Y")) {
								isSelect = true;
							}
							lsCompTableCopy.add(lsCompTable.get(i));
						}
						if (!isSelect) {
							MESCommon.show(WIP_BarCode_Update.this,	"请选择要删除的零部件");
							return;
						}
						for (int i = lsCompTableCopy.size() - 1; i >= 0; i--) {
							if (lsCompTableCopy.get(i).get("CHECKFLAG")	.toString().equals("Y")) 
							{
								lsCompTable.remove(i);
							}
						}
						adapter.notifyDataSetChanged();

					}
				} catch (Exception e) {
					// showMessage(e.toString());
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
					MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
				}
			}
		});

		// btnClear

		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Clear();
					
				} catch (Exception e) {
					MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
		}
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wip__clean, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_UNbarcode) {
			 Intent intent = new Intent();  
			 intent.setClass(this.getApplicationContext(), WIP_UNBarCode.class);  
			 startActivityForResult(intent, 1);  
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	public static class WIPBarcodeAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public WIPBarcodeAdapter(List<HashMap<String, String>> items,
				Context context) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);
			isSelected = new HashMap<Integer, Boolean>();
			// 初始化数据
			initData();
		}

		// 初始化isSelected的数据
		private void initData() {
			for (int i = 0; i < items.size(); i++) {
				getIsSelected().put(i, false);
			}
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			miRowNum = position;
			if (convertView == null) {
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(
						R.layout.activity_wip_barcode_listview, null);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.wipbarcodelv_cb);
				holder.tvProductCompID = (TextView) convertView
						.findViewById(R.id.wipbarcodelv_tvProductCompid);
				holder.tvSerialnumber = (TextView) convertView
						.findViewById(R.id.wipbarcodelv_tvSerialnumber);

				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
			}

			// 设置list中TextView的显示
			holder.tvProductCompID.setText(getItem(position).get("ProductCompID").toString());
			holder.tvSerialnumber.setText(getItem(position).get("Serialnumber").toString());

			if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
				// 将CheckBox的选中状况记录下来
				getIsSelected().put(position, true);
			} else {
				// 将CheckBox的选中状况记录下来
				getIsSelected().put(position, false);
			}
			// 根据isSelected来设置checkbox的选中状况
			holder.cb.setChecked(getIsSelected().get(position));
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public HashMap<String, String> getItem(int arg0) {
			// TODO Auto-generated method stub
			return (HashMap<String, String>) items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		public void setSelect(int position) {
			iPosition = position;
		}

		public int getSelect() {
			return iPosition;
		}

		public static HashMap<Integer, Boolean> getIsSelected() {
			return isSelected;
		}

		public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
			WIPBarcodeAdapter.isSelected = isSelected;
		}

		public static class ViewHolder {
			CheckBox cb;
			TextView tvProductCompID;
			TextView tvSerialnumber;

		}
	}

	public void Clear() {
		try {
			editInput.setText("");
		    editPlanTime.setText("");
			editProductCompid.setText("");
			editSerialnumberID.setText("");
			editSerialnumberIDTwo.setText("");
			lsCompID.clear();
			lsCompTable.clear();
			adapter.notifyDataSetChanged();
			miQty = 0;
			msOrderType="";
			msOrderID="";
			cksd.setChecked(false);
			setFocus(editInput);
		} catch (Exception e) {
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
		}

	}

	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		} catch (Exception e) {
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
		}

	}

	private  String InsertSTEP_P(List<HashMap<String, String>> lstable ,Integer  icount ,String sProductOrderId,String sProductCompId,String sProductId,String sStepId,String sStepSEQ,String sEqpId)
	{
	
		String sResult="";
		try {	
		
			String  sSql=" DELETE FROM PROCESS_STEP_P WHERE PRODUCTORDERID='"+sProductOrderId+"' AND  PRODUCTCOMPID ='"+sProductCompId+"' AND STEPID ='"+sStepId+"' AND PRODUCTSERIALNUMBER='"+lstable.get(icount).get("SerialnumberID").toString()+"' ;";
			     	sSql = sSql +  " INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER_OLD,PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,ISCOMPEXIST,PROCESS_PRODUCTIDTYPE, ISCHIEFCOMP,ISCOMPID3,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
                            + MESCommon.SysId + ",'"
                            + sProductOrderId + "','"
                            + sProductCompId + "','"
                            + sProductId + "','"
                            + sStepId+ "','"
                            + sStepSEQ + "','"
                            + sEqpId+ "','"
                            + lstable.get(icount).get("Serialnumber").toString()  + "','"
                            + lstable.get(icount).get("SerialnumberID").toString()+ "','"
                            + lstable.get(icount).get("MATERIALID").toString() + "','"
                            + lstable.get(icount).get("MATERIALNAME").toString() + "','"
                            + lstable.get(icount).get("MATERIALMAINTYPE").toString() + "','"
                            + lstable.get(icount).get("TRACETYPE").toString() + "','"
                            + lstable.get(icount).get("LOTID").toString() + "','"
                            + lstable.get(icount).get("RAWPROCESSID").toString() + "','"
                            + lstable.get(icount).get("FINEPROCESSID").toString() + "','"
                            + lstable.get(icount).get("SUPPLYID").toString() + "','"
                            + lstable.get(icount).get("FURNACENO").toString() + "','','"
                            + "" + "','"
                            + "N" + "','"
                            + lstable.get(icount).get("SDDL").toString() + "','"
                            + lstable.get(icount).get("BOMFLAGE").toString() + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + "); ";
				    //双段/动力去次组力表看是否有次组力装配工件
			     	  if(lstable.get(icount).get("SDDL").toString().equals("Y"))
					{
			     		sSql = sSql +  " UPDATE  PROCESS_STEP_PF SET  PRODUCTORDERID='"+sProductOrderId+"',PRODUCTCOMPID='"+sProductCompId+"' ,PRODUCTSERIALNUMBER_OLD='"+lstable.get(icount).get("Serialnumber").toString()+"', PRODUCTSERIALNUMBER='"+lstable.get(icount).get("SerialnumberID").toString()+"'  WHERE SYSID=(SELECT SYSID   FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P='"+lstable.get(icount).get("SerialnumberID").toString()+"') ;";           
					}
		
			String sError= db.ExecuteSQL(sSql);
		 if (sError != "") {
				
				return sResult =sError;
			 }
			
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
			return sResult =e.toString();
		}
		return sResult;
	}
	
	public String CheckProductOrder(String snumber) {
		String sResult = "";
		try {
			List<HashMap<String, String>> lscheckOrder = new ArrayList<HashMap<String, String>>();			
			lscheckOrder.clear();
           //查核所有对应的制令
			String sSQL = "SELECT B.PROCESSSTATE,B.PRODUCTORDERID  FROM PROCESS_PRE_P A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID WHERE A.PRODUCTCOMPID='"+ snumber + "' ";
			String sError = db.GetData(sSQL, lscheckOrder);
			if (sError != "") {
				MESCommon.showMessage(WIP_BarCode_Update.this, sError);
				return sResult = sError;
			}
			if (lscheckOrder.size() == 0) {
				return sResult = "制造号码：【"+snumber+"】不存在，请联系生管！";
			}else {
				Boolean bFlag=true;
				//因为有重工制令，所以加入状态查核未下线的。默认以前制造号码对应的制令已经完成，制造号码肯定是生产中或者已完成。所以如果有未下线的认为就是我要绑定的制令
				for(int i=0;i<lscheckOrder.size();i++)
				{
					if(lscheckOrder.get(i).get("PROCESSSTATE").toString().equals("未下线"))
					{
						bFlag=false;
						break;
					}
					
				}
				if(!bFlag)
				{
					return sResult = "制造号码：【"+snumber+"】,所对应的制令【"+lscheckOrder.get(0).get("PRODUCTORDERID").toString()+"】还未下线,请联系生管下线后在进行发料作业！";
				}
			}
			
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
			return sResult = e.toString();
		}

	}
	
	public String CheckProductCOMP(String snumber) {
		String sResult = "";
		try {
			List<HashMap<String, String>> lscheckExist = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> lscheckExistSTKCOMP = new ArrayList<HashMap<String, String>>();
			lscheckExist.clear();
			String sSQL = "SELECT A.* FROM PROCESS_PRODUCTORDER_PRINTBARCODE A INNER JOIN PROCESS B ON A.PRODUCTORDERID =B.PRODUCTORDERID AND A.PRODUCTCOMPID =B.PRODUCTCOMPID AND B.PROCESSSTATUS <>'已完成' AND A.PRODUCTCOMPID ='"
					+ snumber + "' ";
			String sError = db.GetData(sSQL, lscheckExist);
			if (sError != "") {
				MESCommon.showMessage(WIP_BarCode_Update.this, sError);
				return sResult = sError;
			}
			if (lscheckExist.size() == 0) {
				return sResult = "生管未针对制造号码["+snumber+"],设定机壳主料，请检查！";
			}
			lscheckExistSTKCOMP.clear();
			if(!msOrderType.equals("重工制令"))
			{
				if(cksd.isChecked()||ckdl.isChecked())
				{
					sSQL = "SELECT * FROM STKCOMP WHERE COMPID3  ='" + snumber+ "' ";
				}else {
					sSQL = "SELECT * FROM STKCOMP WHERE PRODUCTCOMPID  ='" + snumber+ "' or   COMPID3  ='" + snumber+ "'";
				}
			
				sError = db.GetData(sSQL, lscheckExistSTKCOMP);
				if (sError != "") {
					MESCommon.showMessage(WIP_BarCode_Update.this, sError);
					return sResult = sError;
				}
				if (lscheckExistSTKCOMP.size() > 0) {
					return sResult = "制造号码：【" + snumber + "】已经有绑定记录，请确认！";
				}
			}
			msPROCESS_PRODUCTID = lscheckExist.get(0).get("PROCESS_PRODUCTID").toString().trim();
			msPROCESS_PRODUCTNAME = lscheckExist.get(0).get("PROCESS_PRODUCTNAME").toString().trim();
			msOrderID = lscheckExist.get(0).get("PRODUCTORDERID").toString().trim();
			msPRODUCTID = lscheckExist.get(0).get("PRODUCTID").toString().trim();
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
			return sResult = e.toString();
		}

	}

	public String ChecksBOM(List<HashMap<String, String>> lscheckExistSTKCOMP ,String snumber) {
		String sResult = "";
		String sError = "";
		String sSQL="";
		try {

//			if(cksd.isChecked()||ckdl.isChecked())
//			{
//				if(!lscheckExistSTKCOMP.get(0).get("COMPID3").toString().trim().equals(""))
//				{
//					return sResult = "该条码["+snumber+"],在系统中已经存在绑定制造号码【"+lscheckExistSTKCOMP.get(0).get("COMPID3").toString()+"】的记录,请确认！";
//				}
//			}
//			else {
//				if(!lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString().trim().equals("")&&(!lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString().trim().equals(lscheckExistSTKCOMP.get(0).get("COMPID2").toString().trim())))
//				{   
//					return sResult = "该机壳条码["+snumber+"],在系统中已经存在绑定制造号码【"+lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString()+"】的记录,请确认！";
//				}
//			}
			
			List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
			sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
			sError = db.GetData(sSQL, lscheckERPBOMSystem);
			if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
			{
				if(cksd.isChecked()||ckdl.isChecked())
			    {
					//双段不查核机壳设定主料。
//					List<HashMap<String, String>> lsDoubleProduct = new ArrayList<HashMap<String, String>>();
//					sSQL = "SELECT B.PRODUCTID FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID  WHERE A.PRODUCTCOMPID='"+ snumber + "' ";
//					sError = db.GetData(sSQL, lsDoubleProduct);	
//					  if (!lsDoubleProduct.get(0).get("PRODUCTID").toString().trim().equals(msPROCESS_PRODUCTID) ) {
//					    return  "该机壳物料料号："+ lsDoubleProduct.get(0).get("PRODUCTID").toString().trim() + " 与生管设定绑定物料料号：" + msPROCESS_PRODUCTID+ "不相同,是否确认继续绑定！";		
//						
//					  }
			    }
				else 
				{
				  if (!lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim().equals(msPROCESS_PRODUCTID) )
				  {
					  if(!lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim().equals(""))
						{
						  return  "该机壳物料料号："+ lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim() + " 与生管设定绑定物料料号：" + msPROCESS_PRODUCTID+ "不相同,是否确认继续绑定！";
						
						}		  
				   }
			    }
			
			}
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
			return sResult = e.toString();
		}

	}

	
	public String BindHashMap() {
		String sResult = "";
		String sError = "";
		String sSQL="";
		try {
			HashMap<String, String> hs = new HashMap<String, String>();
			if(msOrderType.equals("重工制令"))
			{	
				//会议决定重工不发料
					
			}
			if ((!editProductCompid.getText().toString().equals("")&& !editSerialnumberID.getText().toString().equals(""))||(!editProductCompid.getText().toString().equals("")&& !editSerialnumberIDTwo.getText().toString().equals(""))) {
				//双段机/动力都先放入一段栏位值。所以
				editProductCompid.setText(editProductCompid.getText().toString().trim().toUpperCase());
				editSerialnumberID.setText(editSerialnumberID.getText().toString().trim().toUpperCase());
				editSerialnumberIDTwo.setText(editSerialnumberIDTwo.getText().toString().trim().toUpperCase());
				
				hs.put("ProductCompID", editProductCompid.getText().toString());
				if((cksd.isChecked()||ckdl.isChecked())&&!ckfl .isChecked())
			    {
					//为空或者为null表示还没有变成整机
					if(lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals("")||lscheckSDDL.get(0).get("PRODUCTCOMPID").toString()==null)
				    {   hs.put("Serialnumber",lscheckSDDL.get(0).get("COMPIDSEQ").toString());
					    hs.put("SerialnumberID", lscheckSDDL.get(0).get("COMPIDSEQ").toString());
					    if(!lscheckSDDL.get(0).get("ITNBR2").toString().equals(""))
					    {
							hs.put("MATERIALID", lscheckSDDL.get(0).get("ITNBR2").toString());
							hs.put("MATERIALNAME", lscheckSDDL.get(0).get("ITDSC2").toString());
					    }else  if(!lscheckSDDL.get(0).get("ITNBR").toString().equals(""))
					    {
					    	hs.put("MATERIALID", lscheckSDDL.get(0).get("ITNBR").toString());
							hs.put("MATERIALNAME", lscheckSDDL.get(0).get("ITDSC").toString());
						}
						hs.put("MATERIALMAINTYPE", lscheckSDDL.get(0).get("MATERIALMAINTYPE").toString());
						hs.put("RAWPROCESSID",lscheckSDDL.get(0).get("COMPID").toString());
						hs.put("FINEPROCESSID",lscheckSDDL.get(0).get("COMPID2").toString());
						hs.put("SUPPLYID", lscheckSDDL.get(0).get("SUPPLYID").toString());
						hs.put("FURNACENO", lscheckSDDL.get(0).get("LNO").toString());
						hs.put("LOTID", lscheckSDDL.get(0).get("LOTID").toString());
						
					}else {
					//双段/动力勾选，并且没有勾选库存发料，lsCompID为空所以需要从新取值。从lscheckSDDL取值;
					List<HashMap<String, String>> lsDoubleMsg = new ArrayList<HashMap<String, String>>();
					if(!editSerialnumberID.getText().toString().equals("")&&editSerialnumberIDTwo.getText().toString().equals(""))
					{
					     sSQL = "SELECT B.PRODUCTID,C.PRODUCTNAME,D.LEVEL2 FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID INNER JOIN MPRODUCT C ON B.PRODUCTID=C.PRODUCTID LEFT JOIN MSTKINVMAS D ON B.PRODUCTID=D.ITNBR  WHERE A.PRODUCTCOMPID='"+ editSerialnumberID.getText().toString() + "' ";
					}else if(!editSerialnumberID.getText().toString().equals("")&&!editSerialnumberIDTwo.getText().toString().equals(""))
					{
						 sSQL = "SELECT B.PRODUCTID,C.PRODUCTNAME,D.LEVEL2 FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID INNER JOIN MPRODUCT C ON B.PRODUCTID=C.PRODUCTID LEFT JOIN MSTKINVMAS D ON B.PRODUCTID=D.ITNBR  WHERE A.PRODUCTCOMPID='"+ editSerialnumberIDTwo.getText().toString() + "' ";
					}
					
					sError = db.GetData(sSQL, lsDoubleMsg);
					if (sError != "") {
						MESCommon.showMessage(WIP_BarCode_Update.this, sError);
						return sError;
					}
					//制造号码，
					if(lsDoubleMsg.size()>0)
					{
						hs.put("Serialnumber",lscheckSDDL.get(0).get("PRODUCTCOMPID").toString());
					    hs.put("SerialnumberID", lscheckSDDL.get(0).get("PRODUCTCOMPID").toString());
						hs.put("MATERIALID", lsDoubleMsg.get(0).get("PRODUCTID").toString());
						hs.put("MATERIALNAME", lsDoubleMsg.get(0).get("PRODUCTNAME").toString());
						hs.put("MATERIALMAINTYPE", lsDoubleMsg.get(0).get("LEVEL2").toString());
						hs.put("RAWPROCESSID",lscheckSDDL.get(0).get("PRODUCTCOMPID").toString());
						hs.put("FINEPROCESSID",lscheckSDDL.get(0).get("PRODUCTCOMPID").toString());
						hs.put("SUPPLYID", "HZ");
						hs.put("FURNACENO", "");
						hs.put("LOTID", "");
					}//机壳
					else {
						hs.put("Serialnumber",lscheckSDDL.get(0).get("COMPIDSEQ").toString());
					    hs.put("SerialnumberID", lscheckSDDL.get(0).get("COMPIDSEQ").toString());
					    if(!lscheckSDDL.get(0).get("ITNBR2").toString().equals(""))
					    {
							hs.put("MATERIALID", lscheckSDDL.get(0).get("ITNBR2").toString());
							hs.put("MATERIALNAME", lscheckSDDL.get(0).get("ITDSC2").toString());
					    }else  if(!lscheckSDDL.get(0).get("ITNBR").toString().equals(""))
					    {
					    	hs.put("MATERIALID", lscheckSDDL.get(0).get("ITNBR").toString());
							hs.put("MATERIALNAME", lscheckSDDL.get(0).get("ITDSC").toString());
						}
						hs.put("MATERIALMAINTYPE", lscheckSDDL.get(0).get("MATERIALMAINTYPE").toString());
						hs.put("RAWPROCESSID",lscheckSDDL.get(0).get("COMPID").toString());
						hs.put("FINEPROCESSID",lscheckSDDL.get(0).get("COMPID2").toString());
						hs.put("SUPPLYID", lscheckSDDL.get(0).get("SUPPLYID").toString());
						hs.put("FURNACENO", lscheckSDDL.get(0).get("LNO").toString());
						hs.put("LOTID", lscheckSDDL.get(0).get("LOTID").toString());
				     	}
					}
					hs.put("CHECKFLAG", "N");	
					hs.put("OrderType",  msOrderType);
					hs.put("TRACETYPE", "C");
					
					hs.put("SDDL", "Y");
					hs.put("ZJFL", "N");
					hs.put("BOMFLAGE", msBomflag);
					
			    }
				else if((cksd.isChecked()||ckdl.isChecked())&&ckfl .isChecked())
			    {
					List<HashMap<String, String>> lsDoubleMsg = new ArrayList<HashMap<String, String>>();
					 sSQL = "SELECT PROCESS_PRODUCTID, PROCESS_PRODUCTNAME  FROM PROCESS_PRODUCTORDER_PRINTBARCODE WHERE PRODUCTORDERID='"+msOrderID+"' AND  PRODUCTCOMPID='"+editProductCompid.getText().toString() + "' ";
					 sError = db.GetData(sSQL, lsDoubleMsg);
					if (sError != "") {
						MESCommon.showMessage(WIP_BarCode_Update.this, sError);
						return sError;
					}
					//
					hs.put("Serialnumber",editSerialnumberID.getText().toString());
					hs.put("SerialnumberID", editSerialnumberID.getText().toString());
					hs.put("CHECKFLAG", "N");	
					if(lsDoubleMsg.size()>0)
					{
						hs.put("MATERIALID", lsDoubleMsg.get(0).get("PROCESS_PRODUCTID").toString());
						hs.put("MATERIALNAME", lsDoubleMsg.get(0).get("PROCESS_PRODUCTNAME").toString());
						hs.put("MATERIALMAINTYPE", "空压机体");
					}
					hs.put("OrderType",  msOrderType);
					hs.put("TRACETYPE", "C");
					hs.put("LOTID", "");
					hs.put("RAWPROCESSID",editSerialnumberID.getText().toString());
					hs.put("FINEPROCESSID",editSerialnumberID.getText().toString());
					hs.put("SUPPLYID", "HZ");
					hs.put("FURNACENO", "");
					hs.put("SDDL", "Y");
					hs.put("ZJFL", "Y");
					hs.put("BOMFLAGE", msBomflag);
			    }
				else
				{
					if(!lsCompID.get(0).get("FINEPROCESSID").toString().equals(""))
					{
						hs.put("Serialnumber",lsCompID.get(0).get("FINEPROCESSID").toString());
					}else if(!lsCompID.get(0).get("RAWPROCESSID").toString().equals(""))
					{
						hs.put("Serialnumber",lsCompID.get(0).get("RAWPROCESSID").toString());
					}									
					hs.put("SerialnumberID", lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					hs.put("OrderType",  msOrderType);
					hs.put("CHECKFLAG", "N");						
					hs.put("MATERIALID", lsCompID.get(0).get("MATERIALID").toString());
					hs.put("MATERIALNAME", lsCompID.get(0).get("MATERIALNAME").toString());
					hs.put("MATERIALMAINTYPE", lsCompID.get(0).get("MATERIALMAINTYPE").toString());
					hs.put("TRACETYPE", lsCompID.get(0).get("TRACETYPE").toString());
					hs.put("LOTID", lsCompID.get(0).get("LOTID").toString());
					hs.put("RAWPROCESSID", lsCompID.get(0).get("RAWPROCESSID").toString());
					hs.put("FINEPROCESSID",lsCompID.get(0).get("FINEPROCESSID").toString());
					hs.put("SUPPLYID", lsCompID.get(0).get("SUPPLYID").toString());
					hs.put("FURNACENO", lsCompID.get(0).get("FURNACENO").toString());
					hs.put("SDDL", "N");
					hs.put("ZJFL", "N");
					hs.put("BOMFLAGE", msBomflag);
				}
				lsCompTable.add(hs);
				adapter.notifyDataSetChanged();
				if(cksd.isChecked())
				{
					 if (!editSerialnumberID.getText().toString().equals("")&&!editSerialnumberIDTwo.getText().toString().equals(""))
					{
						    editProductCompid.setText("");
							editSerialnumberID.setText("");
							editSerialnumberIDTwo.setText("");	
							msOrderID="";
							msOrderType="";
							cksd.setChecked(false);
					}	
			    }
				if(ckdl.isChecked())
				{
					 if (!editSerialnumberID.getText().toString().equals(""))
					{
						    editProductCompid.setText("");
							editSerialnumberID.setText("");
							editSerialnumberIDTwo.setText("");	
							msOrderID="";
							msOrderType="";
							ckdl.setChecked(false);
					}	
			    }
				if (!cksd.isChecked()&&!ckdl.isChecked())
				{
					editProductCompid.setText("");
					editSerialnumberID.setText("");
					editSerialnumberIDTwo.setText("");	
					msOrderID="";
					msOrderType="";
				}
				
				msBomflag="";
				editInput.setText("");
				setFocus(editPlanTime);		
			}
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_BarCode_Update.this, e.toString());
			return sResult = e.toString();
		}

	}

	private String GetScanInfo(String sScanId)
	{
		String sResult = "";
		String sSql = "";
		
		try {
				//先查STKCOMP
				mls.clear();
				sSql = "SELECT DISTINCT A.STOCKID, A.SUPPLYID, A.ITNBR, A.LOTID, A.LOTIDSEQ, A.COMPID, A.COMPIDSEQ, " +
						"A.LOTQTY, A.STOCKTIME, A.SHIPTYPE, B.ITDSC " +
						"FROM STKINSTOCK A LEFT JOIN MSTKINVMAS B ON A.ITNBR=B.ITNBR WHERE A.COMPIDSEQ='" + sScanId + "'";
				sResult = db.GetData(sSql, mls);
				if (!sResult.equals("")) return sResult;
				if (mls.size() == 0)
				{
					return "条码「" + sScanId + "」查无入库资料!";
				}
				String sStockid = mls.get(0).get("STOCKID").toString();
				String sSupplyId = mls.get(0).get("SUPPLYID").toString();
				String sItnbr = mls.get(0).get("ITNBR").toString();
				String sItdsc = mls.get(0).get("ITDSC").toString();
				String sLotid = mls.get(0).get("LOTID").toString();
				String sCompid = mls.get(0).get("COMPID").toString();
				String sLotidseq = mls.get(0).get("LOTIDSEQ").toString();
				String sCompidseq = mls.get(0).get("COMPIDSEQ").toString();
				String sLotQty = mls.get(0).get("LOTQTY").toString();
				String sStockTime = mls.get(0).get("STOCKTIME").toString();
				String sShipType = mls.get(0).get("SHIPTYPE").toString();
				
				sSql = "INSERT INTO STKINOUTSTOCK (SYSID, STOCKID, INOUTTYPE, STKFUNC, STKFUNCID, " +
						"SUPPLYID, ITNBR, LOTID, LOTIDSEQ, LOTQTY, COMPID, COMPIDSEQ, SHIPTYPE, " +
						"STOCKTIME, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES (" +
            			"" + MESCommon.SysId + "," +
            			"'" + sStockid + "'," +
            			"'" + "OUT" + "'," +
            			"'" + "" + "'," +
            			"'" + "" + "'," +
            			"'" + sSupplyId + "'," +
            			"'" + sItnbr + "'," +
            			"'" + sLotid + "'," +
            			"'" + sLotidseq + "'," +
            			"" + sLotQty + "," +
            			"'" + sCompid + "'," +
            			"'" + sCompidseq + "'," +
            			"'" + sShipType + "'," +
            			"'" + sStockTime + "'," +
            			"'" + MESCommon.UserId + "'," +
            			"'" + MESCommon.UserName + "'," +
            			"" + MESCommon.ModifyTime + ")";
				sResult = db.ExecuteSQL(sSql);
				if (sResult != "")
				{
					return sResult ;
				}
				sSql = "DELETE FROM STKINSTOCK WHERE STOCKID='" + sStockid + "' AND ITNBR='" + sItnbr + "' AND COMPIDSEQ='" + sCompidseq + "'";
				sResult = db.ExecuteSQL(sSql);
				if (sResult != "")
				{
					MESCommon.showMessage(WIP_BarCode_Update.this,sResult);
					return sResult;
				}
				
			    return sResult;
			}
		catch (Exception e)
		{
			MESCommon.showMessage(WIP_BarCode_Update.this,e.toString());
			return e.toString();
		}
	}	

}
