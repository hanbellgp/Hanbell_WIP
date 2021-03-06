//#####更新记录#####
//20190107 C1793:修改PDA不符合文字类型的输入问题
//
//
//#####更新记录#####

package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Setting.SpinnerData;
import com.example.hanbell_wip.WIP_Defect.WIPDefectAdapter;
import com.example.hanbell_wip.WIP_PaintingEnd.WIPPaintingEndAdapter.ViewHolder;

import android.R.integer;
import android.R.string;
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
import android.text.InputType;
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
import java.lang.annotation.Retention;
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

public class WIP_TrackIn extends Activity {

	private MESDB db = new MESDB();

	ListView lv0,lv1,lv2;
	Button btnConfirm, btnAdd, btnTemporary,btnDelete,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1, btnSP;
	Spinner spBid;
	EditText editInput,editProductCompID,editMaterialID,editProductModel,editMsheel,editDsheel,editMDsheel,editPMMessage,editCustomerName;

	//	CheckBox cb ;
	TextView  h1, h2, h3,h4, h5, h6,h7,h8,h9,h10,h11,h12,tvM,tvD,tvPM,tvCM;
	LinearLayout tab1, tab2, tab3;
	wiptrackinAdapter adapter;
	wiptrackinAdapterTab2 adapterTab2;
	wiptrackinAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",  msProductId,  msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID="",msSampletimes="",msStepSEQ,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE,msBomflag="" ,msRepeter="",
	msAnalysisResult="";
	String msSpecialAdoption = "";	//是否特采
	int miQty;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;int milv1RowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisData = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisFinalData = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBid = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsMid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSidTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsodtBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_p = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_pf = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProductCUS = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefect = new ArrayList<HashMap<String, String>>();
	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_track_in);

		// 取得控件

		try {

			spBid = (Spinner) findViewById(R.id.wiptrackin_spBid);	
		
			editInput = (EditText) findViewById(R.id.wiptrackin_tvInput);	
			editProductCompID = (EditText) findViewById(R.id.wiptrackin_tvProductCompid);	
			editMaterialID = (EditText) findViewById(R.id.wiptrackin_tvMaterialid);	
			editProductModel = (EditText) findViewById(R.id.wiptrackin_tvProductModel);	
			editMsheel= (EditText) findViewById(R.id.wiptrackin_tvMCheel);	
			editDsheel= (EditText) findViewById(R.id.wiptrackin_tvDCheel);	
			editMDsheel= (EditText) findViewById(R.id.wiptrackin_tvMDCheel);	
			editPMMessage= (EditText) findViewById(R.id.wiptrackin_tvPMMessage);	
			editCustomerName= (EditText) findViewById(R.id.wiptrackin_tvCustomerName);	
		
//			cb= (CheckBox)findViewById(R.id.cb);
			tab1 = (LinearLayout) findViewById(R.id.wiptrackin_tab1);
			tab2 = (LinearLayout) findViewById(R.id.wiptrackin_tab2);
			tab3 = (LinearLayout) findViewById(R.id.wiptrackin_tab3);
			h1 = (TextView) findViewById(R.id.wiptrackin_h1);
			h2 = (TextView) findViewById(R.id.wiptrackin_h2);
			h3 = (TextView) findViewById(R.id.wiptrackin_h3);
			h4 = (TextView) findViewById(R.id.wiptrackin_h4);
			h5 = (TextView) findViewById(R.id.wiptrackin_h5);
			h6 = (TextView) findViewById(R.id.wiptrackin_h6);
			h7= (TextView) findViewById(R.id.wiptrackin_h7);
			h8= (TextView) findViewById(R.id.wiptrackin_h8);
			h9= (TextView) findViewById(R.id.wiptrackin_h9);
			h10= (TextView) findViewById(R.id.wiptrackin_h10);
			h11= (TextView) findViewById(R.id.wiptrackin_h11);
			h12= (TextView) findViewById(R.id.wiptrackin_h12);
			tvM= (TextView) findViewById(R.id.wiptrackin_tvM);
			tvD= (TextView) findViewById(R.id.wiptrackin_tvD);
			tvPM= (TextView) findViewById(R.id.wiptrackin_tvPM);
			tvCM= (TextView) findViewById(R.id.wiptrackin_tvCM);
			h1.setTextColor(Color.WHITE);
			h1.setBackgroundColor(Color.DKGRAY);
			h2.setTextColor(Color.WHITE);
			h2.setBackgroundColor(Color.DKGRAY);
			h3.setTextColor(Color.WHITE);
			h3.setBackgroundColor(Color.DKGRAY);
			h3.setBackgroundColor(Color.DKGRAY);
			h4.setTextColor(Color.WHITE);
			h4.setBackgroundColor(Color.DKGRAY);		
			h5.setTextColor(Color.WHITE);
			h5.setBackgroundColor(Color.DKGRAY);
			h6.setTextColor(Color.WHITE);
			h6.setBackgroundColor(Color.DKGRAY);
			h7.setTextColor(Color.WHITE);
			h7.setBackgroundColor(Color.DKGRAY);
			h8.setTextColor(Color.WHITE);
			h8.setBackgroundColor(Color.DKGRAY);
			h9.setTextColor(Color.WHITE);
			h9.setBackgroundColor(Color.DKGRAY);
			h10.setTextColor(Color.WHITE);
			h10.setBackgroundColor(Color.DKGRAY);
			h11.setTextColor(Color.WHITE);
			h11.setBackgroundColor(Color.DKGRAY);
			h12.setTextColor(Color.WHITE);
			h12.setBackgroundColor(Color.DKGRAY);
			lv0 = (ListView) findViewById(R.id.wiptrackin_lv0);
			lv1 = (ListView) findViewById(R.id.wiptrackin_lv1);
			lv2 = (ListView) findViewById(R.id.wiptrackin_lv2);
			btnConfirm = (Button) findViewById(R.id.wiptrackin_btnConfirm);
			btnTemporary = (Button) findViewById(R.id.wiptrackin_btnTemporary);
			btnAdd = (Button) findViewById(R.id.wiptrackin_btnAdd);			
			btnDelete=(Button) findViewById(R.id.wiptrackin_btnTab0_Delete);
			btnTab1 = (Button) findViewById(R.id.wiptrackin_btnTab1);
			btnTab2 = (Button) findViewById(R.id.wiptrackin_btnTab2);
			btnTab3 = (Button) findViewById(R.id.wiptrackin_btnTab3);		
			btnTab2_0 = (Button) findViewById(R.id.wiptrackin_btnTab2_0_OK);
			btnTab2_1 = (Button) findViewById(R.id.wiptrackin_btnTab2_1_OK);
			btnSP = (Button) findViewById(R.id.wiptrackin_btnSP);
			adapterTab0 = new wiptrackinAdapterTab0(lsCompTable, this);
			lv0.setAdapter(adapterTab0);
			adapter = new wiptrackinAdapter(lsAnalysisTable, this);
			lv1.setAdapter(adapter);		
			adapterTab2 = new wiptrackinAdapterTab2(lsSidTable, this);
			lv2.setAdapter(adapterTab2);
			editMsheel.setVisibility(8);
			editDsheel.setVisibility(8);
			editMDsheel.setVisibility(8);
			editPMMessage.setVisibility(8);
			editCustomerName.setVisibility(8);
			tvD.setVisibility(8);
			tvM.setVisibility(8);
			tvPM.setVisibility(8);
			tvCM.setVisibility(8);
			
			// ***********************************************Start
			prefercesService  =new PrefercesService(this);  
		    params=prefercesService.getPreferences();  
		    ActionBar actionBar=getActionBar();
			actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
			actionBar.setTitle(params.get("StepName"));
		 	btnTab1.setBackgroundColor(0x88999999);
			btnTab2.setBackgroundColor(0x00000000);
			btnTab3.setBackgroundColor(0x00000000);
			String date = sDateFormatShort.format(new java.util.Date());	
			// 读取报工人员
			String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
			String sResult = db.GetData(sSql, lsuser);
			if (sResult != "") {
				MESCommon.showMessage(WIP_TrackIn.this, sResult);
				finish();
			}
		
			if ( lsuser.size()==0) {
				MESCommon.showMessage(WIP_TrackIn.this, "请先进行人员设备报工！");
			
			}
		
		// btnTab1
		btnTab1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					btnTab1.setBackgroundColor(0x88999999);
					btnTab2.setBackgroundColor(0x00000000);
					btnTab3.setBackgroundColor(0x00000000);
					tab1.setVisibility(View.VISIBLE);
					tab2.setVisibility(View.INVISIBLE);
					tab3.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		// btnTab2
		btnTab2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					btnTab1.setBackgroundColor(0x00000000);
					btnTab2.setBackgroundColor(0x88999999);
					btnTab3.setBackgroundColor(0x00000000);
					tab1.setVisibility(View.INVISIBLE);
					tab2.setVisibility(View.VISIBLE);
					tab3.setVisibility(View.INVISIBLE);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		// btnTab3
		btnTab3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					btnTab1.setBackgroundColor(0x00000000);
					btnTab2.setBackgroundColor(0x00000000);
					btnTab3.setBackgroundColor(0x88999999);
					tab1.setVisibility(View.INVISIBLE);
					tab2.setVisibility(View.INVISIBLE);
					tab3.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wiptrackin_editTab2_0);
					if(lsAnalysisTable.size()>0)
					{
						lsAnalysisTable.get(milv1RowNum).put("DATAVALUE",edit.getText().toString());
						lsAnalysisTable.get(milv1RowNum).put("DISPLAYVALUE",edit.getText().toString());
						//只有数值类型需做Judge判定，文字类型不做判定 20190107 C1793
						if(lsAnalysisTable.get(milv1RowNum).get("ISJUDGE").toString().trim().equals("Y") &&
							lsAnalysisTable.get(milv1RowNum).get("RESULTTYPE").toString().equals("数值"))
						{
							if(!edit.getText().toString().trim().equals(""))
							{   String sminValueString="";
							    String smaxValueString="";
								if(lsAnalysisTable.get(milv1RowNum).get("SPECMINVALUE").toString().equals(""))
								{
									sminValueString="0";
								}else
								{
									sminValueString=lsAnalysisTable.get(milv1RowNum).get("SPECMINVALUE").toString();
								}
								if(lsAnalysisTable.get(milv1RowNum).get("SPECMAXVALUE").toString().equals(""))
								{
									smaxValueString="9999999999";
								}else {									
									smaxValueString=lsAnalysisTable.get(milv1RowNum).get("SPECMAXVALUE").toString()	;
								}
							
								if (Double.parseDouble(edit.getText().toString().trim())>=Double.parseDouble(sminValueString)&&
										Double.parseDouble(edit.getText().toString().trim())<=Double.parseDouble(smaxValueString)	)
								{
									 lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "OK");
								}else
								{
									 lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "NG");
								}					
								
							}else
							{
								 lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "");
							}
						}else
						{
						   lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "OK" );
						}
						showRow(milv1RowNum + 1);
						hintKbTwo();
						adapter.notifyDataSetChanged();		
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackin_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackin_radioButtonNG);
					if(lsAnalysisTable.size()>0)
					{
						if( rdoOK.isChecked())
						{   
							lsAnalysisTable.get(milv1RowNum).put("DISPLAYVALUE",rdoOK.getText().toString());
							lsAnalysisTable.get(milv1RowNum).put("DATAVALUE", "True");
							lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "OK" );
							
						}else
						{
							lsAnalysisTable.get(milv1RowNum).put("DISPLAYVALUE",rdoNG.getText().toString());
							lsAnalysisTable.get(milv1RowNum).put("DATAVALUE", "False" );
							if(lsAnalysisTable.get(milv1RowNum).get("ISJUDGE").toString().trim().equals("Y"))
							{
							   lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "NG" );
							}else
							{
							   lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "OK" );
							}
						}	
						hintKbTwo();
						adapter.notifyDataSetChanged();				
						showRow(milv1RowNum+1);
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		//装配规范查询触发事件
		spBid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SpinnerData sBid = (SpinnerData) spBid.getSelectedItem();
				try {
				// 读取工序
				lsSid.clear();
				lsSidTable.clear();
				
	
				 String sResult="";
				 if(sBid.value.equals("预设"))
				 {
					 sResult= db.GetERP_ASSEMBLESPECIFICATION(msProductId,msProductCompId,msProductOrderId,"预设","",params.get("StepID"), lsSid);	 
				 }else {
					  sResult= db.GetERP_ASSEMBLESPECIFICATION(msProductId,msProductCompId,msProductOrderId,"",sBid.value,params.get("StepID"), lsSid);
				 }
				if (sResult != "") {
					MESCommon.showMessage(WIP_TrackIn.this, sResult);
					finish();
				}
				for(int i=0;i<lsSid.size();i++)
				{
					HashMap<String, String> hs = new HashMap<String, String>();	
					hs.put("MIDNAME", lsSid.get(i).get("AS_MNAME").toString());
					hs.put("SIDNAME", lsSid.get(i).get("AS_SNAME").toString());				
					lsSidTable.add(hs);
				}
				adapterTab2.notifyDataSetChanged();
			
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		// 控件事件
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				try{
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinAdapterTab0.ViewHolder holder = (wiptrackinAdapterTab0.ViewHolder) arg1.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				wiptrackinAdapterTab0.getIsSelected().put(position,
						holder.cb.isChecked());
				milv0RowNum=position;
				if(!lsCompTable.get(milv0RowNum).get("IS_INSERT").equals("N"))
				{
					if(holder.cb.isChecked())
					{
					lsCompTable	.get(position).put("CHECKFLAG","Y"); 
					}else
					{
					lsCompTable.get(position).put("CHECKFLAG","N"); 
					}
				}else {					
					holder.cb.setChecked(false);
				}
			} catch (Exception e) {
				MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			}
			}
			
		});
		// 控件事件
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinAdapter.ViewHolder holder = (wiptrackinAdapter.ViewHolder) arg1.getTag();
				showRow(position);
				
			}
		});
	
		// 控件事件
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinAdapterTab2.ViewHolder holder = (wiptrackinAdapterTab2.ViewHolder) arg1.getTag();
			}
		});
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.wiptrackin_tvInput);
					if ( lsuser.size()==0) {
						MESCommon.showMessage(WIP_TrackIn.this, "请先进行人员设备报工！");
						return false;
					}
					if (txtInput.getText().toString().trim().length() == 0) {
						txtInput.setText("");
						MESCommon.show(WIP_TrackIn.this, "请扫描条码!");
						return false;
					}
				

					lsCompID.clear();
					String sTempXID = txtInput.getText().toString();
					String sResult="";
					if(editProductCompID.getText().toString().equals("")){
					 sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"",msProductOrderId, "QF","制造号码","装配", lsCompID);
					
					}else
					{
				      sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","零部件","装配", lsCompID);
					}
					if (!sResult.equals(""))
                    {
					 txtInput.setText("");
                    }else
                    {
                    	  if (editProductCompID.getText().toString().equals("") &&lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
                          {
                    		  MESCommon.show(WIP_TrackIn.this,"请先刷入制造号码!");
                    		  txtInput.setText("");
                              return false;
                          }
                    }
					 
					if(lsCompID.size()>0)
					{
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						
					} else
					if (lsCompID.size() == 0 && editProductCompID.getText().toString().equals(""))
                    {
						MESCommon.show(WIP_TrackIn.this,sResult);
                        return false;
                    }
                    else if (lsCompID.size() == 0 && !editProductCompID.getText().toString().equals("") )
                    {
//                    	cb.setEnabled(true);
                    	MESCommon.show(WIP_TrackIn.this,sResult+",是否需手动添加！");
                        return false;
                    }
                    else
                    {
                    	txtInput.setText(sTempXID);
                    }
					
					//刷主件
					if(editProductCompID.getText().toString().equals(""))
					{

						lsProcess.clear();
						String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
						sResult = db.GetProductProcess(sOrderID,txtInput.getText().toString().trim(), lsProcess);
						
						if (sResult != "") {
							MESCommon.showMessage(WIP_TrackIn.this, sResult);
							finish();
							
						}			
						if(lsProcess.size()==0)
						{
							MESCommon.show(WIP_TrackIn.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
							Clear();
							return false;
						}
						else {
							if(lsProcess.get(0).get("PROCESSSTATUS").toString().equals("已完成"))
							{
								//主体流程已完成，并且PDA工站不等于冷媒出库前装配站
								if(!params.get("StepID").equals("冷媒出库前装配站"))
								{									
								  MESCommon.show(WIP_TrackIn.this,"制造号码【"+txtInput.getText().toString().trim()+"】,已整体工艺加工完成！");
			              		  Clear();
								  setFocus(editProductCompID);
			                      return false;
								}else {
									List<HashMap<String, String>> lsJudResult = new ArrayList<HashMap<String, String>>();
									String  sSql="SELECT A.*,B.ANALYSISJUDGEMENTRESULT FROM ANALYSISWAITLIST A INNER JOIN ANALYSISRESULT_M B ON A.ANALYSISFORMSID=B.ANALYSISFORMSID WHERE A.PRODUCTORDERID='"+sOrderID+"' AND  A.PRODUCTCOMPID='"+txtInput.getText().toString().toUpperCase().trim()+"' AND A.SOURCESTEP= '冷媒出库前装配站' ORDER BY A.MODIFYTIME DESC  ;";
									String sError= db.GetData(sSql,  lsJudResult);
						
									 if (sError != "") {
											MESCommon.showMessage(WIP_TrackIn.this, sError);
											return false;
										 }
									 if(lsJudResult.size()>0)
									 {
										 if(lsJudResult.get(0).get("ANALYSISJUDGEMENTRESULT").equals("合格"))
										 {
											 MESCommon.show(WIP_TrackIn.this,"制造号码【"+txtInput.getText().toString().trim()+"】,已完成冷媒出库前装配！");
						              		  Clear();
											  setFocus(editProductCompID);
						                      return false;
										 } 
									 }								
								}
							}else
							{	//	冷媒出库前装配站不判断工序					
								if(!lsProcess.get(0).get("STEPID").toString().equals(params.get("StepID")))
								{
								
									MESCommon.show(WIP_TrackIn.this, "工件目前工序为【"+lsProcess.get(0).get("STEPID").toString()+"】，不能在【"+params.get("StepID")+"】报工！");
									Clear();
									return false;
								}	
							}
							//finishcode 请勿删除此标记：依需求新增代码
							if (params.get("StepName").contains("机体") && !params.get("StepName").contains("P机") && !params.get("StepName").contains("涡旋"))
							{
								//判断一些控件是否显示；生管有设定值时候
								if((lsProcess.get(0).get("MWHEEL").toString().equals("")&& lsProcess.get(0).get("DWHEEL").toString().equals(""))&&!lsProcess.get(0).get("PMMESSAGE").toString().equals(""))
								{	tvPM.setVisibility(0);
									editPMMessage.setVisibility(0);
									editPMMessage.setText(lsProcess.get(0).get("PMMESSAGE").toString());
									lv0.getLayoutParams().height=225;
								}
								//判断一些控件是否显示；粗砂轮或者精砂轮有设定值时候
								if((!lsProcess.get(0).get("MWHEEL").toString().equals("")|| !lsProcess.get(0).get("DWHEEL").toString().equals(""))&&!lsProcess.get(0).get("PMMESSAGE").toString().equals(""))
								{   
									tvD.setVisibility(0);
								    tvM.setVisibility(0);
									editMsheel.setVisibility(0);
									editDsheel.setVisibility(0);
									editMDsheel.setVisibility(0);								
									tvPM.setVisibility(0);
									editPMMessage.setVisibility(0);
									editPMMessage.setText(lsProcess.get(0).get("PMMESSAGE").toString());
									
									editMsheel.setText(lsProcess.get(0).get("MWHEEL").toString());
									editDsheel.setText(lsProcess.get(0).get("DWHEEL").toString());
									editMDsheel.setText(lsProcess.get(0).get("WHEELMESSAGE").toString());
									lv0.getLayoutParams().height=165;
								  
								}
								//判断一些控件是否显示；粗砂轮或者精砂轮有设定值时候
								if((!lsProcess.get(0).get("MWHEEL").toString().equals("")|| !lsProcess.get(0).get("DWHEEL").toString().equals(""))&&lsProcess.get(0).get("PMMESSAGE").toString().equals(""))
								{   
									tvD.setVisibility(0);
								    tvM.setVisibility(0);
									editMsheel.setVisibility(0);
									editDsheel.setVisibility(0);
									editMDsheel.setVisibility(0);
									editMsheel.setText(lsProcess.get(0).get("MWHEEL").toString());
									editDsheel.setText(lsProcess.get(0).get("DWHEEL").toString());
									editMDsheel.setText(lsProcess.get(0).get("WHEELMESSAGE").toString());
									lv0.getLayoutParams().height=225;
								  
								}
								//判断一些控件是否显示；粗砂轮或者精砂轮有设定值时候
								if(lsProcess.get(0).get("MWHEEL").toString().equals("")&&lsProcess.get(0).get("DWHEEL").toString().equals("")&&lsProcess.get(0).get("PMMESSAGE").toString().equals(""))
								 {
									tvPM.setVisibility(8);
									editPMMessage.setVisibility(8);
									editPMMessage.setText("");
									tvD.setVisibility(8);
								    tvM.setVisibility(8);
									editMsheel.setVisibility(8);
									editDsheel.setVisibility(8);
									editMDsheel.setVisibility(8);
									editMsheel.setText("");
									editDsheel.setText("");
									editMDsheel.setText("");
									lv0.getLayoutParams().height=280;
								}
							}
							if (params.get("StepName").contains("冷媒"))
							{
								if(!lsProcess.get(0).get("CUSTOMERNAME").toString().equals(""))
								{ 
									tvCM.setVisibility(0);
									editCustomerName.setVisibility(0);			
									editCustomerName.setText(lsProcess.get(0).get("CUSTOMERNAME").toString());
									lv0.getLayoutParams().height=225;
								}else
								{	tvCM.setVisibility(8);
								    editCustomerName.setVisibility(8);	
									editCustomerName.setText("");
									lv0.getLayoutParams().height=280;
								}
							}
						}
						lsProduct.clear();
						String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ";
						String sError= db.GetData(sSql,  lsProduct);
				
						 if (sError != "") {
								MESCommon.showMessage(WIP_TrackIn.this, sError);
								return false;
							 }
						
							 
						//初始化工件信息
						editProductCompID.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						editMaterialID.setText(lsProcess.get(0).get("PRODUCTID").toString());
						//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
						editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());	
						
						msProductOrderId=lsProcess.get(0).get("PRODUCTORDERID").toString();
						msProductId=lsProcess.get(0).get("PRODUCTID").toString();
						msProductCompId=lsProcess.get(0).get("PRODUCTCOMPID").toString();
						msProductSerialNumber=lsProcess.get(0).get("PRODUCTSERIALNUMBER").toString();
						if(	params.get("StepID").equals("冷媒出库前装配站"))
						{
						    msStepId="冷媒出库前装配站";	
						}else {
							msStepId=lsProcess.get(0).get("STEPID").toString();	
						}
						msStepSEQ=lsProcess.get(0).get("STEPSEQ").toString();
						
						msEqpId=params.get("EQPID").toString();;
						miQty=Integer.parseInt(lsProcess.get(0).get("STARTQTY").toString().trim());

						db.GetERP_ASSEMBLESPECIFICATION(msProductId,msProductCompId,msProductOrderId,"预设","",params.get("StepID"), lsSid);
						 lsProductCUS.clear();
						
					     sSql=" SELECT DISTINCT  PRODUCTCUS FROM ERP_ASSEMBLESPECIFICATION WHERE PRODUCTORDERID ='"+lsProcess.get(0).get("PRODUCTORDERID").toString()+"' AND PRODUCTCOMPID= '"+lsProcess.get(0).get("PRODUCTCOMPID").toString()+"' ";
						 sError= db.GetData(sSql,  lsProductCUS);
					   
						 if (sError != "") {
								MESCommon.showMessage(WIP_TrackIn.this, sError);
								return false;
							 }
						//冷媒的机型从装配规范里面取的。
						if(lsProductCUS.size()>0)
						{
							editProductModel.setText(lsProductCUS.get(0).get("PRODUCTCUS").toString());	
						}
						lsAnalysisFinalData.clear();
						//判断有没有待最终判定的记录，如果有不能继续进行
						 sSql=" SELECT * FROM ANALYSISWAITLIST WHERE   SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID = '"+msProductCompId+"' AND QCTYPE = '制程检验' AND QC_ITEM = '自主检验' AND ANALYSISSTATUS = '待最终判定'  ";
						 sError= db.GetData(sSql,  lsAnalysisFinalData);
						
						 if (sError != "") {
								MESCommon.showMessage(WIP_TrackIn.this, sError);
								return false;
						 }
						 if(lsAnalysisFinalData.size()>0)
						 {
								MESCommon.showMessage(WIP_TrackIn.this, "制造号码：【"+msProductCompId+"】,正在等待最终判定，不能进行报工作业！");
								return false;
						 }
						 
						//初始化检验项目
						lsAnalysisData.clear(); 
						sResult = db.GetAnalysisData(msProductOrderId,msProductId,msProductCompId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
						
						if (sResult != "") {
							MESCommon.showMessage(WIP_TrackIn.this, sResult);
							finish();
						}
						
						if(lsAnalysisData.size()==0)
						{
							List<HashMap<String, String>> lsdtAnalysisformsID = new ArrayList<HashMap<String, String>>();
							String sSQL = "SELECT * FROM ANALYSISWAITLIST WHERE  SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID ='"+msProductCompId+"'  AND QCTYPE = '制程检验' AND QC_ITEM ='自主检验'  AND ANALYSISSTATUS = '待检验' AND FORMSSTATUS = '待处理' ";
					        sResult = db.GetData(sSQL, lsdtAnalysisformsID);
					   
					    	if (sResult != "") {
								MESCommon.showMessage(WIP_TrackIn.this, sResult);
								finish();
							}else {
								msAnalysisformsID=lsdtAnalysisformsID.get(0).get("ANALYSISFORMSID").toString();
								msSampletimes=lsdtAnalysisformsID.get(0).get("SAMPLETIMES").toString();
							}							
						}						
						lsAnalysisTable.clear();						
						for(int i=0;i<lsAnalysisData.size();i++)
						{
							msAnalysisformsID=lsAnalysisData.get(i).get("ANALYSISFORMSID").toString();
							msSampletimes=lsAnalysisData.get(i).get("SAMPLETIMES").toString();
							msSUPPLYLOTID=lsAnalysisData.get(i).get("SUPPLYLOTID").toString();
							msSUPPLYID=lsAnalysisData.get(i).get("SUPPLYID").toString();
							msQC_ITEM=lsAnalysisData.get(i).get("QC_ITEM").toString();
							msQCTYPE=lsAnalysisData.get(i).get("QCTYPE").toString();
							HashMap<String, String> hs = new HashMap<String, String>();				
							hs.put("ANALYSISITEM", lsAnalysisData.get(i).get("ANALYSISITEM").toString());
							hs.put("ITEMALIAS", lsAnalysisData.get(i).get("ITEMALIAS").toString());
							hs.put("RESULTTYPE", lsAnalysisData.get(i).get("RESULTTYPE").toString());
							hs.put("SPECMINVALUE", lsAnalysisData.get(i).get("SPECMINVALUE").toString());
							hs.put("SPECMAXVALUE", lsAnalysisData.get(i).get("SPECMAXVALUE").toString());
							hs.put("ISNEED", lsAnalysisData.get(i).get("ISNEED").toString());
							hs.put("ISJUDGE", lsAnalysisData.get(i).get("ISJUDGE").toString());
							
		
							//先判断类型
							if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("数值")) {
								//检查是否有输入检测结果值。
								if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
								{ //是否必须判断
									if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
									{
										if (Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())>=Double.parseDouble(lsAnalysisData.get(i).get("SPECMINVALUE").toString())&&
												Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())<=Double.parseDouble(lsAnalysisData.get(i).get("SPECMAXVALUE").toString())	)
										{
											hs.put("FINALVALUE", "OK");
										}else
										{
											hs.put("FINALVALUE", "NG");
										}
									}else
									{
										hs.put("FINALVALUE", "OK");
									}
									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DATAVALUE").toString().trim());
									hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
							     }
								 else
								 {   //检查是否有默认值，如果没有输入值已默认值为默认结果
									if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
									{//是否必须判断
										if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
										{
											if (Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())>=Double.parseDouble(lsAnalysisData.get(i).get("SPECMINVALUE").toString())&&
													Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())<=Double.parseDouble(lsAnalysisData.get(i).get("SPECMAXVALUE").toString())	)
											{
												hs.put("FINALVALUE", "OK");
											}else
											{
												hs.put("FINALVALUE", "NG");
											}
										}else
										{
											hs.put("FINALVALUE", "OK");
										}
										hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
										hs.put("DATAVALUE", lsAnalysisData.get(i).get("DEFAULTSVALUE").toString());
									}else
									{  //是否必须判断
										if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
										{
										hs.put("FINALVALUE", "");
										}else
										{
											hs.put("FINALVALUE", "OK");
										}
										hs.put("DISPLAYVALUE","");
										hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
									}
								  }						
								} else if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("布尔")) {
									if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
									{   //检查到资料库有该检验项目的存储值
										//是否必须判断
										if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
										{   
											//必须判，如果是true则最终判断为OK,elseNG，显示名字为trueword or falseword
											if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
											{
												hs.put("FINALVALUE", "OK");	
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											}else{
												hs.put("FINALVALUE", "NG");
											    hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());	
											}
										}else
										{   //不判断，则最终判断为肯定为OK,显示名字为trueword or falseword
											hs.put("FINALVALUE", "OK");
											if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
											{											
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											}else {
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
											}
										}
										//判断不判断，如果在我们资料库有值，一定带的值是资料库中的。
										hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
									}else
									{    //检查到资料库没有该检验项目的存储值
										//检查是否有默认值，如果没有输入值已默认值为默认结果
										if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
										{
											  //是否必须判断
											if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
											{//必须判，如果是true则最终判断为OK,elseNG，显示名字为trueword or falseword
												if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE"))
												{
													hs.put("FINALVALUE", "OK");	
													hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
													hs.put("DATAVALUE", "True");
												}else{
													hs.put("FINALVALUE", "NG");
												    hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());	
												    hs.put("DATAVALUE", "False");
												}
											}
											else
											{ 
												//不判断，有则最终判断为肯定为OK,显示名字为trueword or falseword
												hs.put("FINALVALUE", "OK");
												if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE"))
												{											
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
												}else {
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
												}
												hs.put("DATAVALUE", lsAnalysisData.get(i).get("DEFAULTSVALUE").toString());
											}
											
										}//没有预设值，默认是OK，最终是合格，显示名字带trueword
										else{
											hs.put("FINALVALUE", "OK");
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											hs.put("DATAVALUE", "True");
										}
									}
						}else//除开数值，和布尔
						{
							if(lsAnalysisData.get(i).get("ANALYSISITEM").toString().toUpperCase().equals("VI")){
								//查询滑块值
								List<HashMap<String, String>> lsdtStep_P = new ArrayList<HashMap<String, String>>();
								String sSQL = "SELECT * FROM PROCESS_STEP_P WHERE  PRODUCTCOMPID ='"+msProductCompId+"' and MATERIALMAINTYPE='滑块' ";
						        sResult = db.GetData(sSQL, lsdtStep_P);
						        if(lsdtStep_P.size()>0)
						        {
									hs.put("FINALVALUE", "OK");
									hs.put("DISPLAYVALUE",lsdtStep_P.get(0).get("FINEPROCESSID").toString().trim());
									hs.put("DATAVALUE", lsdtStep_P.get(0).get("FINEPROCESSID").toString());
						        }
						        else{
									hs.put("FINALVALUE", "OK");
									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
									hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
								}
							}else{
								hs.put("FINALVALUE", "OK");
								//20190107 C1793
								if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DATAVALUE").toString().trim());
								else
									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
								
								hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
							}
						}					

						lsAnalysisTable.add(hs);
						}
						adapter.notifyDataSetChanged();
						//根据产品等信息 读取装配规范
						lsBid.clear();
	 				   sSql = "SELECT DISTINCT AS_BID, AS_INDEX ||'  '|| AS_BNAME AS AS_BNAME FROM ERP_ASSEMBLESPECIFICATION  WHERE PRODUCTID='" +msProductId + "' " +
									" AND   PRODUCTCOMPID='" +msProductCompId + "' AND PRODUCTORDERID='" + msProductOrderId + "' ORDER BY AS_INDEX ";
						sResult = db.GetData(sSql, lsBid);
						
						if (sResult != "") {
							MESCommon.showMessage(WIP_TrackIn.this, sResult);
							setFocus(editInput);
						}
						List<SpinnerData> lst = new ArrayList<SpinnerData>();
						SpinnerData c = new SpinnerData("预设", "预设");
						lst.add(c);
						for (int i = 0; i < lsBid.size(); i++) {
							c = new SpinnerData(lsBid.get(i).get("AS_BID").toString(), lsBid.get(i).get("AS_BNAME").toString());
							lst.add(c);
						}

						ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(WIP_TrackIn.this,	android.R.layout.simple_spinner_item, lst);
						Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spBid.setAdapter(Adapter);
						if(!params.get("StepID").equals("冷媒出库前装配站"))
						{
							String sSetStepInResule = db.SetStepInbyJWJ(msProductOrderId ,msProductCompId,"",
									msStepId, msStepSEQ, msEqpId, "", "", "", "",
									MESCommon.UserId, MESCommon.UserName, miQty, "");
							
							if (!sSetStepInResule.equals("")) {
								MESCommon.showMessage(WIP_TrackIn.this, sSetStepInResule);							
							    return false;
							}
						}
						//FirstInsertSTEP_P();
						GetProcessSTEP_P();//初始零部件
						editInput.setText("");
						setFocus(editProductCompID);
					}
					//刷零部件
					else
					{	lsCompID.clear();				 
						sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","" , "QF","零部件", "装配",lsCompID);
						
						if(lsCompID.size()<=0)
						{   
								MESCommon.show(WIP_TrackIn.this, "物料条码【" + txtInput.getText().toString().trim() + "】， 不存在,是否需手动添加!");
							    return false;
						}else
						{  
							String sCheckResult="";
							if(lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("Y"))
							{
								MESCommon.show(WIP_TrackIn.this,"装配零部件时,不能装配条码为：【"+txtInput.getText().toString().trim()+"】的制造号码！");
								return false;
							}
							for(int i=0;i<lsCompTable.size();i++)
							{
								if(txtInput.getText().toString().trim().equals(lsCompTable.get(i).get("SEQ").toString()))
								{
									MESCommon.show(WIP_TrackIn.this, "物料条码[" + lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString() + "] 已在物料清单中!");
								    return false;
								}								
								
							}
							//是否特采检查
							Check_SpecialAdoption(txtInput.getText().toString().trim());
							if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
							{
								sCheckResult=checkExist(txtInput.getText().toString().trim());
							}
							if(!sCheckResult.equals(""))
							{
								
								AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn.this).setTitle("确认").setMessage(sCheckResult+",是否继续加入!")
										.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
						            @Override  
						            public void onClick(DialogInterface dialog,int which) {  
						                // TODO Auto-generated method stub  
						            	msRepeter="Y";
						            	exeLstCompTable();
						            }  
						        })  
								.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
								            @Override  
								            public void onClick(DialogInterface dialog,int which) {  
								                // TODO Auto-generated method stub  
								            	editInput.setText("");
								            	return  ;
								            }  
								 }).show();
							}
							else {
								exeLstCompTable( );
							}
						}

					}

					} catch (Exception e) {
						MESCommon.showMessage(WIP_TrackIn.this, e.toString());
						 db.InsertSqlLog("WIP_TrackInex--20", e.toString());
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
	
		// btnConfirm
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					msAnalysisResult="合格";
					
					if (editProductCompID.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_TrackIn.this, "请先扫描条码在进行报工！");
						return;
					}
					//专配检查
					String sCheckMsg = Check_HZ_SPECIALMATCH();
					if(!sCheckMsg.equals(""))
					{
						MESCommon.show(WIP_TrackIn.this, sCheckMsg);
						return;
					}
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{
								MESCommon.show(WIP_TrackIn.this, "请输入["+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"]检验结果");
								btnTab2.performClick();
								return;
							}
						}
						if(lsAnalysisTable.get(i).get("FINALVALUE").toString().trim().equals("NG"))
						{
							msAnalysisResult="不合格";
							break;
						}
					}
				  //机体查核物料装配是否整齐
			 	   String sMessage=CheckProductComList();			
			 	   if (!sMessage.equals("") ) {
			 		  MESCommon.show(WIP_TrackIn.this, sMessage);
						return;
//			 		  AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn.this).setTitle("确认").setMessage(sMessage)
//								.setPositiveButton("继续",new DialogInterface.OnClickListener() {  
//				            @Override  
//				            public void onClick(DialogInterface dialog,int which) {  
//				            	if(msAnalysisResult.equals("不合格"))
//								   {
//										AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
//												.setPositiveButton("确认",new DialogInterface.OnClickListener() {  
//								            @Override  
//								            public void onClick(DialogInterface dialog,int which) {  
//								                // TODO Auto-generated method stub  
//								                 String sError=Save("不合格", lsAnalysisTable, lsAnalysisData);
//							                	 if (!sError.equals("")) 
//													{
//														MESCommon.show(WIP_TrackIn.this, sError);
//														return;
//													}	
//								            	//执行最终判定
//									             db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
//												 Toast.makeText(WIP_TrackIn.this, "报工完成!", Toast.LENGTH_SHORT).show();
//								                  
//									            Clear();
//								            }  
//								        })  
//										.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
//										            @Override  
//										            public void onClick(DialogInterface dialog,int which) {  
//										                // TODO Auto-generated method stub  
//										            	return ;
//										            }  
//										 }).show();					
//								   } 
//							 	   else  
//							 	   {
//										String sError= Save(msAnalysisResult, lsAnalysisTable, lsAnalysisData);
//									    if (!sError.equals("")) 
//										{
//											MESCommon.show(WIP_TrackIn.this, sError);
//											return;
//										}	
//										//执行最终判定
//							         	db.FinalSaveData(msAnalysisformsID,msSampletimes,msAnalysisResult,MESCommon.UserId ,MESCommon.UserName,"");
//							         	if(!params.get("StepID").equals("冷媒出库前装配站"))
//										{
//							         	  String sSetStepOutResule=db.SetStepOutbyJWJ(msProductOrderId, msProductCompId, msStepId, msStepSEQ, msEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName, miQty, 0, 0, 0,"",false);	
//							         	  if (!sSetStepOutResule.equals("")) 
//											{
//												MESCommon.show(WIP_TrackIn.this, sSetStepOutResule);
//												return;
//											}					
//										  }
//							         	  Toast.makeText(WIP_TrackIn.this, "报工完成!", Toast.LENGTH_SHORT).show();                  
//						                  Clear();
//									}
//				            }  
//				        })  
//						.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
//						            @Override  
//						            public void onClick(DialogInterface dialog,int which) {  
//						                // TODO Auto-generated method stub  
//						            	return ;
//						            }  
//						 }).show();	
					}else
					{						

				 	   if(msAnalysisResult.equals("不合格"))
					   {
							
							AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
									.setPositiveButton("确认",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					                // TODO Auto-generated method stub  
					                 String sError=Save("不合格", lsAnalysisTable, lsAnalysisData);
				                	 if (!sError.equals("")) 
										{
											MESCommon.show(WIP_TrackIn.this, sError);
											return;
										}	
					            	//执行最终判定
						             db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
									 Toast.makeText(WIP_TrackIn.this, "报工完成!", Toast.LENGTH_SHORT).show();
					                  
						            Clear();
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
				 	   else  
				 	   {
							String sError= Save(msAnalysisResult, lsAnalysisTable, lsAnalysisData);
						    if (!sError.equals("")) 
							{
								MESCommon.show(WIP_TrackIn.this, sError);
								return;
							}	
							//执行最终判定
				         	db.FinalSaveData(msAnalysisformsID,msSampletimes,msAnalysisResult,MESCommon.UserId ,MESCommon.UserName,"");
				         	if(!params.get("StepID").equals("冷媒出库前装配站"))
							{
				         	  String sSetStepOutResule=db.SetStepOutbyJWJ(msProductOrderId, msProductCompId, msStepId, msStepSEQ, msEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName, miQty, 0, 0, 0,"",false);	
				         	  if (!sSetStepOutResule.equals("")) 
								{
									MESCommon.show(WIP_TrackIn.this, sSetStepOutResule);
									return;
								}					
							  }
				         	  Toast.makeText(WIP_TrackIn.this, "报工完成!", Toast.LENGTH_SHORT).show();                  
			                  Clear();
						}
				    }
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});

		// btnRemove
		Button btnTemporary = (Button) findViewById(R.id.wiptrackin_btnTemporary);
		btnTemporary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
						if (editProductCompID.getText().toString().trim().equals("") ) {
							MESCommon.showMessage(WIP_TrackIn.this, "请先扫描条码在进行报工暂存！");
							return;
						}
						//专配检查
						String sCheckMsg = Check_HZ_SPECIALMATCH();
						if(!sCheckMsg.equals(""))
						{
							MESCommon.show(WIP_TrackIn.this, sCheckMsg);
							return;
						}
						Save("", lsAnalysisTable, lsAnalysisData);
					
					    Toast.makeText(WIP_TrackIn.this, "暂存成功!", Toast.LENGTH_SHORT).show();
					    Clear();

				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}			
			}
		});
		// btnExit
	
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

						if(editProductCompID.getText().toString().equals(""))
						{
							MESCommon.show(WIP_TrackIn.this, "请先扫描制造号码,在进行零部件添加！");
							return;
						}					
						openNewActivity2(v);						
									
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (lsCompTable.size() > 0) {
						List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
						Boolean isSelect=false;
					
						for(int i=0;i<lsCompTable.size();i++)
						{
							if (lsCompTable.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								isSelect=true;
							
							}
							lsCompTableCopy.add(lsCompTable.get(i));
						}
						if(!isSelect)
						{
							MESCommon.show(WIP_TrackIn.this, "请选择要删除的零部件");
							return;
						}
						for(int i=lsCompTableCopy.size()-1;i>=0;i--)
						{
							if (lsCompTableCopy.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								lsCompTable.remove(i);
								
							}
						}					
						adapterTab0.notifyDataSetChanged();
					
					}					
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		
		//btnSP 不合格单查看
		btnSP.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Boolean isSelect=false;
					
					for(int i=0;i<lsCompTable.size();i++)
					{
						if (lsCompTable.get(i).get("CHECKFLAG").toString().equals("Y") && lsCompTable.get(i).get("ISSP").toString().equals("Y")) {
							isSelect=true;
							// 开启新Activity,并传参数
							Intent iNew = new Intent(WIP_TrackIn.this, WIP_TrackIn_SP.class);
							iNew.putExtra("COMPID", lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString());
							iNew.putExtra("COMPIDSEQ", lsCompTable.get(i).get("SEQ").toString());
							startActivity(iNew);
						}
					}
					if(!isSelect)
					{
						MESCommon.show(WIP_TrackIn.this, "请选择要查看的零部件");
						return;
					}
									
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn.this, e.toString());
				}
			}
		});
		
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn.this,  e.toString());
		}
		
	}

	// 有返回值的Activity  
	public void openNewActivity2(View v)  
	{  
	 Intent intent = new Intent();  
	 intent.setClass(this.getApplicationContext(), WIP_CompAdd.class);   
	 startActivityForResult(intent, 1);  
		
	}  

	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
	 // requestCode用于区分业务  
	 // resultCode用于区分某种业务的执行情况  
		try{
	 if (1 == requestCode && RESULT_OK == resultCode)  
	 {  
		 ArrayList<CompInformation> arrayList = (ArrayList<CompInformation>) data.getSerializableExtra("key");  
         String result = "" ;  
       
     	
         for (CompInformation myClass : arrayList) {  
        	  Boolean bflag=true;
	        	 for(int i=0;i<lsCompTable.size();i++)
	     		{
	     			if(myClass.PRODUCTSERIALNUMBER.equals(lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString()))
	     			{
	     				
	     				MESCommon.show(WIP_TrackIn.this, "物料条码[" + myClass.PRODUCTSERIALNUMBER + "] 已在物料清单中!");
	     				bflag=false;
	     				break  ;
	     			}
	     		}
        	 if(bflag)
        	 {
				HashMap<String, String> hs = new HashMap<String, String>();				
				hs.put("MaterialId",myClass.MaterialId);
				hs.put("MaterialMame", myClass.MaterialMame);
				hs.put("SEQ", myClass.SEQ);
				hs.put("PRODUCTSERIALNUMBER",myClass.PRODUCTSERIALNUMBER);
				hs.put("MaterialType", myClass.MaterialType);
				hs.put("CHECKFLAG", myClass.CHECKFLAG);
				hs.put("ISCOMPEXIST", myClass.ISCOMPEXIST);
				hs.put("RAWPROCESSID", myClass.RAWPROCESSID);
				hs.put("FINEPROCESSID", myClass.FINEPROCESSID);
				hs.put("SUPPLYID", myClass.SUPPLYID);
				hs.put("FURNACENO", myClass.FURNACENO);
				hs.put("IS_INSERT", myClass.IS_INSERT);
				hs.put("TRACETYPE", myClass.TRACETYPE);
				hs.put("LOTID", myClass.LOTID);
				hs.put("ISCOMPREPEATED", myClass.ISCOMPREPEATED);
				hs.put("ISSP", "");
				lsCompTable.add(0,  hs);							
				
        	 }
        	    adapterTab0.notifyDataSetChanged();		
				setFocus(editProductCompID);
				editInput.setText("");	
         }  
	 }  
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn.this,  e.toString());
		}
	}  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wip__track_in, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_Temporary) {
			 Intent intent = new Intent();  
			 intent.setClass(this.getApplicationContext(),WIP_StopTrackout .class);  
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

	public static class wiptrackinAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapter(List<HashMap<String, String>> items,
				Context context) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);	

		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;		
			try {

	//			if (convertView == null) {
	//				
					// 获得ViewHolder对象
					holder = new ViewHolder();
					// 导入布局并赋值给convertview
					convertView = inflater.inflate(R.layout.activity_wip_painting_end_listview, null);
					
					holder.tvAnalySisItem = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvAnalySisItem);
					holder.tvValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvValue);
					holder.tvType = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvType);
					holder.tvSPCMinValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvSPCMinValue);
					holder.tvSPCMaxValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvSPCMaxValue);
					holder.tvFinalValue=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvFinalValue);
					holder.tvISNeed=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvISNeed);
					holder.tvISJudge=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvISJudge);
					// 为view设置标签
					convertView.setTag(holder);
	//			} else {
	//				// 取出holder
	//				holder = (ViewHolder) convertView.getTag();
	//				
	//			}
				// 设置list中TextView的显示
				holder.tvAnalySisItem.setText(getItem(position).get("ITEMALIAS").toString());
				holder.tvValue.setText(getItem(position).get("DISPLAYVALUE").toString());		
				holder.tvType.setText(getItem(position).get("RESULTTYPE").toString());
				holder.tvSPCMinValue.setText(getItem(position).get("SPECMINVALUE").toString());
				holder.tvSPCMaxValue.setText(getItem(position).get("SPECMAXVALUE").toString());
				holder.tvFinalValue.setText(getItem(position).get("FINALVALUE").toString());
				holder.tvISNeed.setText(getItem(position).get("ISNEED").toString());
				holder.tvISJudge.setText(getItem(position).get("ISJUDGE").toString());
				if(getItem(position).get("ISJUDGE").toString().trim().equals("Y"))
				{
					if(!getItem(position).get("FINALVALUE").toString().trim().equals(""))
					{
						if(!getItem(position).get("FINALVALUE").toString().equals("OK"))
						{
						   holder.tvValue.setBackgroundColor(0xFFFF0000);
						}else
						{ holder.tvValue.setBackgroundColor(0x00000000);}
					}
				}
			 
			} catch (Exception e) {
			
			}
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
		public static class ViewHolder {
			TextView tvISNeed;
			TextView tvISJudge;
			TextView tvAnalySisItem;
			TextView tvValue;
			TextView tvType;
			TextView tvSPCMinValue;
			TextView tvSPCMaxValue;
			TextView tvFinalValue;	
			
		}
	}
	
	public static class wiptrackinAdapterTab2 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapterTab2(List<HashMap<String, String>> items,
				Context context) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);	

		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;		
			
			try {
				
			
			if (convertView == null) {
			
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(R.layout.activity_wip_track_in_tab2_listview, null);
				holder.tvAnalySidID = (TextView) convertView.findViewById(R.id.wiptrackinlv2_tvAnalySidID);
				holder.tvAnalySidItem = (TextView) convertView.findViewById(R.id.wiptrackinlv2_tvAnalySidItem);
			
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvAnalySidID.setText(getItem(position).get("MIDNAME").toString());	
			holder.tvAnalySidItem.setText(getItem(position).get("SIDNAME").toString());	
			} catch (Exception e) {
				// TODO: handle exception
			}
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
		public static class ViewHolder {
	
			TextView tvAnalySidItem;
			TextView tvAnalySidID;
		}
	}

	public static class wiptrackinAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapterTab0(List<HashMap<String, String>> items,
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
			try {
				
			
			if (convertView == null) {
			
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(R.layout.activity_wip_track_in_tab0_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackinlv0_cb);
				holder.tvISSP = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvISSP);
				holder.tvSerialnumberId = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvSerialnumberId);
				holder.tvMaterialMame = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialMame);
				holder.tvMaterialID = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialID);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvISSP.setText(getItem(position).get("ISSP").toString());	//是否特采
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvMaterialMame.setText(getItem(position).get("MaterialMame").toString());	
			holder.tvMaterialID.setText(getItem(position).get("MaterialId").toString());

			// 将CheckBox的选中状况记录下来
			if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
				// 将CheckBox的选中状况记录下来
				getIsSelected().put(position, true);
			} else {
				// 将CheckBox的选中状况记录下来
				getIsSelected().put(position, false);
			}
			// 根据isSelected来设置checkbox的选中状况
			holder.cb.setChecked(getIsSelected().get(position));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
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
			wiptrackinAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvISSP;
			TextView tvSerialnumberId;
			TextView tvMaterialMame;
			TextView tvMaterialID;
		}
	}

	private  void exeLstCompTable()
	{
		try {		
			

			    String   sMaterialId = lsCompID.get(0).get("MATERIALID").toString();

				lsBom.clear();
				List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
				String sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
				String sError = db.GetData(sSQL, lscheckERPBOMSystem);
				if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
				{
					  sSQL = "SELECT * FROM ERP_MBOM WHERE PRODUCTID ='" +msProductId + "' AND PRODUCTORDERID = '" + msProductOrderId + "' AND MATERIALID = '" + sMaterialId + "'";
					  String  sResult = db.GetData(sSQL, lsBom);
					if (sResult != "") {
						MESCommon.showMessage(WIP_TrackIn.this, sResult);
						
					} 
	
					if(lsBom.size()==0)
					{
						 AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn.this).setTitle("确认").setMessage("物料条码【" +editInput.getText().toString() + "】的物料【"+sMaterialId+"】， 不在物料清单中,是否确定加入!")
									.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					            	msBomflag="N";
					            
					            	String sError=BindHashMap();
									if (sError != "") {
										MESCommon.showMessage(WIP_TrackIn.this, sError);
										return ;
									}								
							
					            }  
					        })  
							.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
							            @Override  
							            public void onClick(DialogInterface dialog,int which) {  
							                // TODO Auto-generated method stub  
											editInput.setText("");
											setFocus(editInput);		
							            	return ;
							            }  
							 }).show();  
						return  ;
					}else {
						 sError=BindHashMap();
						if (sError != "") {
							MESCommon.showMessage(WIP_TrackIn.this, sError);
							return ;
						}								
				
					}
				}else {
					 sError=BindHashMap();
					if (sError != "") {
						MESCommon.showMessage(WIP_TrackIn.this, sError);
						return ;
					}								
			
				}

			
			
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
		}
	}
	
	public String BindHashMap( )
	{
		String sResult = "";
		String sError = "";
		String sSQL="";
		try {
			String   sSEQ = lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
			String   sMaterialId = lsCompID.get(0).get("MATERIALID").toString();
			String   sMaterialMame = lsCompID.get(0).get("MATERIALNAME").toString();
			String   sMaterialType = lsCompID.get(0).get("MATERIALMAINTYPE").toString();
			String   sTracetype=lsCompID.get(0).get("TRACETYPE").toString();
			String   sLotID=lsCompID.get(0).get("LOTID").toString();
			String   sRAWPROCESSID=lsCompID.get(0).get("RAWPROCESSID").toString();
			String   sFINEPROCESSID=lsCompID.get(0).get("FINEPROCESSID").toString();
			String   sSUPPLYID=lsCompID.get(0).get("SUPPLYID").toString();
			String   sLNO=lsCompID.get(0).get("FURNACENO").toString();
			//查询轴承座次组立的排气间隙值
			List<HashMap<String, String>> lsdtStep_PF = new ArrayList<HashMap<String, String>>();
			sSQL = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P ='"+sSEQ+"' ";
	        sResult = db.GetData(sSQL, lsdtStep_PF);
            if(lsdtStep_PF.size()>0)
            {	
			   List<HashMap<String, String>> lsdtMDD = new ArrayList<HashMap<String, String>>();
			   List<HashMap<String, String>> lsAnalysisTableCOPY = new ArrayList<HashMap<String, String>>();
			   sSQL = "SELECT * FROM ANALYSISRESULT_MDD WHERE ANALYSISFORMSID='"+lsdtStep_PF.get(0).get("ANALYSISFORMSID").toString()+"' AND ANALYSISITEM IN ('排气间隙确认-公','排气间隙确认-母')";
		       sResult = db.GetData(sSQL, lsdtMDD);
		         if(lsdtMDD.size()>0)
		          { 

		        	 for(int i=0;i<lsAnalysisTable.size();i++)
						{ 
	        			 HashMap<String, String> hs = new HashMap<String, String>(); 	
							hs.put("ANALYSISITEM", lsAnalysisTable.get(i).get("ANALYSISITEM").toString());
							hs.put("ITEMALIAS", lsAnalysisTable.get(i).get("ITEMALIAS").toString());
							hs.put("RESULTTYPE", lsAnalysisTable.get(i).get("RESULTTYPE").toString());
							hs.put("SPECMINVALUE", lsAnalysisTable.get(i).get("SPECMINVALUE").toString());
							hs.put("SPECMAXVALUE", lsAnalysisTable.get(i).get("SPECMAXVALUE").toString());
							hs.put("ISNEED", lsAnalysisTable.get(i).get("ISNEED").toString());
							hs.put("ISJUDGE", lsAnalysisTable.get(i).get("ISJUDGE").toString());	
							hs.put("FINALVALUE", lsAnalysisTable.get(i).get("FINALVALUE").toString().trim());
							hs.put("DISPLAYVALUE",lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim());
							hs.put("DATAVALUE", lsAnalysisTable.get(i).get("DATAVALUE").toString());
							lsAnalysisTableCOPY.add(hs);							
					     }
	        			
											  	
			        	 lsAnalysisTable.clear();
		        		 for(int i=0;i<lsAnalysisTableCOPY.size();i++)
							{ 
	        			     HashMap<String, String> hs = new HashMap<String, String>(); 	
							hs.put("ANALYSISITEM", lsAnalysisTableCOPY.get(i).get("ANALYSISITEM").toString());
							hs.put("ITEMALIAS", lsAnalysisTableCOPY.get(i).get("ITEMALIAS").toString());
							hs.put("RESULTTYPE", lsAnalysisTableCOPY.get(i).get("RESULTTYPE").toString());
							hs.put("SPECMINVALUE", lsAnalysisTableCOPY.get(i).get("SPECMINVALUE").toString());
							hs.put("SPECMAXVALUE", lsAnalysisTableCOPY.get(i).get("SPECMAXVALUE").toString());
							hs.put("ISNEED", lsAnalysisTableCOPY.get(i).get("ISNEED").toString());
							hs.put("ISJUDGE", lsAnalysisTableCOPY.get(i).get("ISJUDGE").toString());
	        			    for(int j=0;j<lsdtMDD.size();j++)
								{
									if(lsAnalysisTableCOPY.get(i).get("ANALYSISITEM").toString().equals(lsdtMDD.get(j).get("ANALYSISITEM").toString()))
									{
										
										hs.put("FINALVALUE", "OK");
										hs.put("DISPLAYVALUE",lsdtMDD.get(j).get("DATAVALUE").toString().trim());
										hs.put("DATAVALUE", lsdtMDD.get(j).get("DATAVALUE").toString());
										
										break;
									}else {
										
										hs.put("FINALVALUE", lsAnalysisTableCOPY.get(i).get("FINALVALUE").toString().trim());
										hs.put("DISPLAYVALUE",lsAnalysisTableCOPY.get(i).get("DISPLAYVALUE").toString().trim());
										hs.put("DATAVALUE", lsAnalysisTableCOPY.get(i).get("DATAVALUE").toString());
									}								
								}
	        			 lsAnalysisTable.add(hs);
						}
	        		 adapter.notifyDataSetChanged();  
	        		 lsAnalysisTableCOPY.clear();
		         }
		     	
		     	
            }
			//取得提示的零部件编号
			String sNewNumber="";
			HashMap<String, String> hs = new HashMap<String, String>();	
			hs.put("MaterialId",sMaterialId);
			hs.put("MaterialMame", sMaterialMame);
			hs.put("SEQ", sSEQ);
			if (!sFINEPROCESSID.equals("")) {
				
				if(!sMaterialId.equals(""))
				{
			      String sFinId=getCompid(sMaterialId);
			      if(!sFinId.equals(""))
			      {
			    	  if(sFinId.equals("半成品方型件"))
			    	  {
			    		  //方型件不打精加工号了，采用粗加工！
			    		  hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);	
			    		  sNewNumber=sRAWPROCESSID;
			    	  }else {
			    		  hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);		
			    		  sNewNumber=sFINEPROCESSID;
					}				    	 
			      }else {
			    	  hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);		
		    		  sNewNumber=sFINEPROCESSID;
				  }
				}else {
					hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);	
					sNewNumber=sFINEPROCESSID;
				}
			}else  if (!sRAWPROCESSID.equals(""))
			{
				hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);
				sNewNumber=sRAWPROCESSID;
			}
			else if (!sLotID.equals(""))
			{
				hs.put("PRODUCTSERIALNUMBER",sLotID);
				sNewNumber=sLotID;
			}
			hs.put("MaterialType", sMaterialType);
			hs.put("CHECKFLAG", "N");			
			hs.put("ISCOMPREPEATED",msRepeter);			
			hs.put("ISCOMPEXIST", "Y");
			hs.put("TRACETYPE", sTracetype);
			hs.put("LOTID", sLotID);
			hs.put("RAWPROCESSID", sRAWPROCESSID);
			hs.put("FINEPROCESSID", sFINEPROCESSID);
			hs.put("SUPPLYID", sSUPPLYID);
			hs.put("FURNACENO", sLNO);
			hs.put("IS_INSERT", "Y");
			hs.put("BOMFLAGE", msBomflag);
			hs.put("ISSP", msSpecialAdoption);	//是否为特采
			lsCompTable.add(0,  hs);							
			adapterTab0.notifyDataSetChanged();	
			Toast.makeText(WIP_TrackIn.this, "零部件：【"+sNewNumber+"】,加入成功！", Toast.LENGTH_SHORT).show();	
			editInput.setText("");
			msRepeter="";
			msBomflag="";
			setFocus(editProductCompID);
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return sResult = e.toString();
		}

	}
	private  String getCompid(String sProductid)
	{
		try {		
			String sResult="";
			List<HashMap<String, String>> lsProductType = new ArrayList<HashMap<String, String>>();
			String sSQL = "SELECT PRODUCTTYPE FROM MPRODUCT WHERE  PRODUCTID='"+sProductid+"'   ";
			db.GetData(sSQL, lsProductType);
			if(lsProductType.size()>0)
			{
			    return sResult=lsProductType.get(0).get("PRODUCTTYPE").toString();
			}
		    return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return e.toString();
		}
	}
	
	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wiptrackin_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackin_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackin_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wiptrackin_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wiptrackin_tab2_1);

				
				if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("数值")) {
					tab2_0.setVisibility(View.VISIBLE);
					tab2_1.setVisibility(View.INVISIBLE);
					edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);	//只允许输入数值 20190107 C1793
					if(!lsAnalysisTable.get(iRow).get("DATAVALUE").toString().equals(""))
					{
						edit.setText((String) lsAnalysisTable.get(iRow).get("DATAVALUE"));
					}else{
						edit.setText((String) lsAnalysisData.get(iRow).get("DEFAULTSVALUE"));}
						setFocus(edit);
				} else if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("文字")) {
					tab2_0.setVisibility(View.VISIBLE);
					tab2_1.setVisibility(View.INVISIBLE);
					edit.setInputType(InputType.TYPE_CLASS_TEXT);			//无限制输入格式 20190107 C1793
					if(!lsAnalysisTable.get(iRow).get("DATAVALUE").toString().equals(""))
					{
						edit.setText((String) lsAnalysisTable.get(iRow).get("DATAVALUE"));
					}else{
						edit.setText((String) lsAnalysisData.get(iRow).get("DEFAULTSVALUE"));}
						setFocus(edit);
				} else if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("布尔")) {
					tab2_0.setVisibility(View.INVISIBLE);
					tab2_1.setVisibility(View.VISIBLE);
					//设置rad的显示信息

					rdoOK.setText(lsAnalysisData.get(iRow).get("TRUEWORD").toString());
					rdoNG.setText(lsAnalysisData.get(iRow).get("FALSEWORD").toString());
					if(!lsAnalysisTable.get(iRow).get("DATAVALUE").toString().equals("")){
					if (lsAnalysisTable.get(iRow).get("DATAVALUE").toString().toUpperCase().equals("TRUE")) {
						rdoOK.setChecked(true);
					} else {
						rdoNG.setChecked(true);
					}
					}else{
					if (lsAnalysisData.get(iRow).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE")) {
						rdoOK.setChecked(true);
					} else {
						rdoNG.setChecked(true);
					}}
				}
				
			}
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
		}
	}
	
	private  void GetProcessSTEP_P()
	{
		try {			
			lsProcessStep_p.clear();
			String  sSql=" SELECT STEPID,  PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,PRODUCTSERIALNUMBER,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,ISCOMPEXIST ,CASE WHEN ISCOMPREPEATED IS NULL THEN '' ELSE ISCOMPREPEATED END ISCOMPREPEATED  ,CASE WHEN  BOMFLAGE IS NULL THEN '' ELSE BOMFLAGE END BOMFLAGE FROM PROCESS_STEP_P WHERE PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+msProductCompId+"' ORDER BY  CAST ( STEPSEQ AS INT) DESC ";
			String sError= db.GetData(sSql,  lsProcessStep_p);

			 if (sError != "") {
					MESCommon.showMessage(WIP_TrackIn.this, sError);
					return;
				 }
	        if(lsProcessStep_p.size()>0)
	        {
				for(int i=0;i<lsProcessStep_p.size();i++)
			    {
					HashMap<String, String> hs = new HashMap<String, String>();				
					
					hs.put("MaterialId",lsProcessStep_p.get(i).get("PROCESS_PRODUCTID").toString());
					hs.put("MaterialMame", lsProcessStep_p.get(i).get("PROCESS_PRODUCTNAME").toString());
					if (!lsProcessStep_p.get(i).get("FINEPROCESSID").toString().equals("")) {
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("FINEPROCESSID").toString());
					}else  if (!lsProcessStep_p.get(i).get("RAWPROCESSID").toString().equals(""))
					{
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("RAWPROCESSID").toString());
					}
					else if (!lsProcessStep_p.get(i).get("LOTID").toString().equals(""))
					{
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("LOTID").toString());
					}
					hs.put("SEQ",lsProcessStep_p.get(i).get("PRODUCTSERIALNUMBER").toString());
					hs.put("MaterialType",lsProcessStep_p.get(i).get("MATERIALMAINTYPE").toString());
					hs.put("CHECKFLAG","N");
					hs.put("TRACETYPE", lsProcessStep_p.get(i).get("TRACETYPE").toString());
					hs.put("LOTID", lsProcessStep_p.get(i).get("LOTID").toString());
					hs.put("RAWPROCESSID",lsProcessStep_p.get(i).get("RAWPROCESSID").toString());
					hs.put("FINEPROCESSID",lsProcessStep_p.get(i).get("FINEPROCESSID").toString());
					hs.put("SUPPLYID",lsProcessStep_p.get(i).get("SUPPLYID").toString());
					hs.put("FURNACENO",lsProcessStep_p.get(i).get("LNO").toString());
					hs.put("ISCOMPEXIST",lsProcessStep_p.get(i).get("ISCOMPEXIST").toString());
					hs.put("ISCOMPREPEATED",lsProcessStep_p.get(i).get("ISCOMPREPEATED").toString());
					hs.put("BOMFLAGE",lsProcessStep_p.get(i).get("BOMFLAGE").toString());
					//已经存在的不在重复插入
					if(lsProcessStep_p.get(i).get("STEPID").toString().equals(msStepId))
					{
					    hs.put("IS_INSERT","Y");
					}else {
						hs.put("IS_INSERT","N");
					}
					hs.put("ISSP","");
					lsCompTable.add(hs);
			    }
			
	        }
	    	adapterTab0.notifyDataSetChanged();				
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
		}
	}
	
	private  String CheckProductComList()
	{
		try {	
			String sReuslt="";
			String sMessage="";
			if(msStepId .contains("机体整机装配"))
			{
				//过滤掉双段机/动力系统不查核
				List<HashMap<String, String>> lsSDDL = new ArrayList<HashMap<String, String>>();
				String sSQL = "SELECT * FROM STKCOMP WHERE COMPID3='"+editProductCompID.getText().toString().toUpperCase().trim()+"' ";
		        String  sError = db.GetData(sSQL, lsSDDL);
		        if(lsSDDL.size()>0)
			     {//过滤掉双段机/动力系统不查核
		        	return "";
			     }
			       
				List<HashMap<String, String>> lsCompTable_Copy = new ArrayList<HashMap<String, String>>();
				for(int i=0;i< lsCompTable.size();i++)
				{
	    		    HashMap<String, String> hs = new HashMap<String, String>();	
		   			hs.put("SEQ", lsCompTable.get(i).get("SEQ").toString());				   			
		   			hs.put("MaterialType", lsCompTable.get(i).get("MaterialType").toString());
		   			lsCompTable_Copy.add(0,  hs);		
				}
				
				String sMainTypelist="机壳 ,轴承座 ,公转子,母转子 ,轴封";
				String[] sList=sMainTypelist.split(",");
				for(int i=0;i< lsCompTable.size();i++)
				{
					//用物料带来来包含物料清单，因为物料大类可能写的比较范围大
					String SS=lsCompTable.get(i).get("MaterialType").toString().trim();
					if(SS.contains("轴承座"))
					{
				    	//,查找次组立下的公母转子
				    	List<HashMap<String, String>> lsdtStep_PF = new ArrayList<HashMap<String, String>>();
						sSQL = "SELECT * FROM PROCESS_STEP_PF WHERE ANALYSISFORMSID=(SELECT MAX( ANALYSISFORMSID) FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P ='"+lsCompTable.get(i).get("SEQ").toString()+"'  )  AND SERIALNUMBER_P<>'"+lsCompTable.get(i).get("SEQ").toString()+"' ";
				    	//sSQL = "SELECT * FROM PROCESS_STEP_PF WHERE ANALYSISFORMSID=(SELECT MAX( ANALYSISFORMSID) FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P ='"+lsCompTable.get(i).get("SEQ").toString()+"')  AND SERIALNUMBER_P<>'"+lsCompTable.get(i).get("SEQ").toString()+"'   and PRODUCTCOMPID = '"+msProductCompId+"'  "; 
				    	sError = db.GetData(sSQL, lsdtStep_PF);
				       if(lsdtStep_PF.size()>0)
				       {
				    	   if (!lsdtStep_PF.get(0).get("PRODUCTCOMPID").toString().trim().equals(""))
					       {  
				    		   if (!lsdtStep_PF.get(0).get("PRODUCTCOMPID").toString().trim().equals(msProductCompId))
					    	   {
					    			return sReuslt="次组立后的轴承座："+lsCompTable.get(i).get("SEQ").toString().trim()+"已经被制造号码："+lsdtStep_PF.get(0).get("PRODUCTCOMPID").toString()+"装配使用,不能重复装配！ ";
					    	   }
				    	   }
				    	   for(int j=0;j<lsdtStep_PF.size();j++)
				    	   {
				    		   HashMap<String, String> hs = new HashMap<String, String>();	
					   			hs.put("SEQ", lsdtStep_PF.get(j).get("SERIALNUMBER_P").toString());				   			
					   			hs.put("MaterialType", lsdtStep_PF.get(j).get("MATERIALMAINTYPE").toString());
					   			lsCompTable_Copy.add(0,  hs);			
				    	   }
				       }
					}
		        
				}
				for (int j=0;j<sList.length ;j++)
				{	
					Boolean bFlage=false;
					for(int i=0;i< lsCompTable_Copy.size();i++)
					{
						//用物料带来来包含物料清单，因为物料大类可能写的比较范围大
						String SS=lsCompTable_Copy.get(i).get("MaterialType").toString().trim();
						if(SS.contains(sList[j].trim()))
						{
							bFlage=true;
							break;
						}
				
					}
					if(!bFlage)
					{
						sMessage=sMessage+"【"+sList[j]+"】, ";					
					}
				}
				if(sMessage.equals(""))
				{
					return sReuslt;
				}else {
					
					return sReuslt="物料大类："+sMessage+" 还没有装配！";
				}
			}
			return sReuslt;
	 	   
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	private  String InsertSTEP_P( String stype)
	{
		String sResult="";
		try {
			if(lsCompTable.size()>0)
			{   //editProductID.getText().toString(),msStepId, 
				String  sSql=" DELETE FROM PROCESS_STEP_P WHERE PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+editProductCompID.getText().toString()+"' AND STEPID ='"+msStepId+"' ;";
				sSql=sSql + " DELETE FROM PROCESS_STEP_NOBARCODE WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+editProductCompID.getText().toString()+"' AND STEPID ='"+msStepId+"' ;";
				
				for(int i=0;i<lsCompTable.size();i++)
			    {
				    if(!lsCompTable.get(i).get("IS_INSERT").toString().equals("N"))
				    {
				    	
			    	sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER_OLD,PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,ISCOMPEXIST,ISCOMPREPEATED,BOMFLAGE, PROCESS_PRODUCTIDTYPE, ISCHIEFCOMP,MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
                            + MESCommon.SysId + ",'"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msProductId + "','"
                            + msStepId+ "','"
                            + msStepSEQ + "','"
                            + msEqpId+ "','"
                            + lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString() + "','"
                            + lsCompTable.get(i).get("SEQ").toString() + "','"
                            + lsCompTable.get(i).get("MaterialId").toString() + "','"
                            + lsCompTable.get(i).get("MaterialMame").toString() + "','"
                            + lsCompTable.get(i).get("MaterialType").toString() + "','"
                            + lsCompTable.get(i).get("TRACETYPE").toString() + "','"
                            + lsCompTable.get(i).get("LOTID").toString() + "','"
                            + lsCompTable.get(i).get("RAWPROCESSID").toString() + "','"
                            + lsCompTable.get(i).get("FINEPROCESSID").toString() + "','"
                            + lsCompTable.get(i).get("SUPPLYID").toString() + "','"
                            + lsCompTable.get(i).get("FURNACENO").toString() + "','"
                            + lsCompTable.get(i).get("ISCOMPEXIST").toString() + "','"
                            + lsCompTable.get(i).get("ISCOMPREPEATED").toString() + "','"
                            + lsCompTable.get(i).get("BOMFLAGE").toString() + "','"
                            + "" + "','"
                            + "N" + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
			    	
				    	if (!stype.equals("")) //暂存时不更新
				    	{
					    	//,更新PROCESS_STEP_PF 级次组立的检验项目相关资料
					    	List<HashMap<String, String>> lsdtStep_PF = new ArrayList<HashMap<String, String>>();
					    	//170510更新，因为次组立有重复的条码，所以只筛选没有装配的制造号码
							String sSQL = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P ='"+lsCompTable.get(i).get("SEQ").toString()+"'  AND ISNULL(PRODUCTORDERID,'')='' ";
					        sResult = db.GetData(sSQL, lsdtStep_PF);
				            if(lsdtStep_PF.size()>0)
				            {
				             	 
				            	sSql = sSql+ "UPDATE ANALYSISWAITLIST SET PRODUCTID='"+msProductId+"', PRODUCTORDERID='"+msProductOrderId+"', PRODUCTCOMPID='"+msProductCompId +"' , PRODUCTSERIALNUMBER='"+lsCompTable.get(i).get("SEQ").toString()+"'  WHERE ANALYSISFORMSID='"+lsdtStep_PF.get(0).get("ANALYSISFORMSID").toString()+"' ; ";
					      	    sSql = sSql+ " UPDATE ANALYSISRESULT_M SET PRODUCTID='"+msProductId+"', PRODUCTORDERID='"+msProductOrderId+"', PRODUCTCOMPID='"+msProductCompId+"' , PRODUCTSERIALNUMBER='"+lsCompTable.get(i).get("SEQ").toString()+"'  WHERE ANALYSISFORMSID='"+lsdtStep_PF.get(0).get("ANALYSISFORMSID").toString()+"' ; ";
					      	    if (lsCompTable.get(i).get("ISCOMPREPEATED").toString().equals("Y")) {
					      	    	
							      	 sSql = sSql + " UPDATE  PROCESS_STEP_PF SET  ISCOMPREPEATED='Y' , PRODUCTORDERID='" +msProductOrderId + "' ,PRODUCTCOMPID ='" +msProductCompId + "',PRODUCTSERIALNUMBER_OLD='"+lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString()+"', PRODUCTSERIALNUMBER ='" + lsCompTable.get(i).get("SEQ").toString()  + "'  WHERE SYSID=(SELECT  min(SYSID)  FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P='" + lsCompTable.get(i).get("SEQ").toString()  + "')   ;";
								}else {
                                      sSql = sSql + " UPDATE  PROCESS_STEP_PF SET PRODUCTORDERID='" +msProductOrderId + "' ,PRODUCTCOMPID ='" +msProductCompId + "',PRODUCTSERIALNUMBER_OLD='"+lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString()+"', PRODUCTSERIALNUMBER ='" + lsCompTable.get(i).get("SEQ").toString()  + "'  WHERE SYSID=(SELECT   min(SYSID)  FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P='" + lsCompTable.get(i).get("SEQ").toString()  + "')   ;";
								}
					      	   
					        }
				            if (lsCompTable.get(i).get("ISCOMPREPEATED").toString().equals("Y")) 
				            {
				      	    	
						    	 sSql = sSql + " UPDATE  PROCESS_STEP_P SET ISCOMPREPEATED='Y'  WHERE PRODUCTSERIALNUMBER='" + lsCompTable.get(i).get("SEQ").toString()  + "'   ;";
						    }
				            
				            //,更新PROCESS_STEP_P_ANALYSISITEM 装配检验项目相关资料
					    	List<HashMap<String, String>> lsPROCESS_STEP_P_ANALYSISITEM = new ArrayList<HashMap<String, String>>();
							sSQL = "SELECT TOP 1 * FROM PROCESS_STEP_P_ANALYSISITEM WHERE PRODUCTSERIALNUMBER IN (SELECT  DISTINCT SERIALNUMBER_P  FROM PROCESS_STEP_PF WHERE ANALYSISFORMSID IN ( SELECT  ANALYSISFORMSID FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P  ='"+lsCompTable.get(i).get("SEQ").toString()+"' AND ISNULL(PRODUCTORDERID,'')=''  )  AND ISNULL(PRODUCTORDERID,'')=''  ) AND ISNULL(PRODUCTORDERID,'')=''  ORDER BY ANALYSISFORMSID DESC ";
					        sResult = db.GetData(sSQL, lsPROCESS_STEP_P_ANALYSISITEM);
				            if(lsPROCESS_STEP_P_ANALYSISITEM.size()>0)
				            {
				            	sSql = sSql+ "UPDATE ANALYSISWAITLIST SET PRODUCTID='"+msProductId+"', PRODUCTORDERID='"+msProductOrderId+"', PRODUCTCOMPID='"+msProductCompId +"'   WHERE ANALYSISFORMSID='"+lsPROCESS_STEP_P_ANALYSISITEM.get(0).get("ANALYSISFORMSID").toString()+"' ; ";
					      	    sSql = sSql+ " UPDATE ANALYSISRESULT_M SET PRODUCTID='"+msProductId+"', PRODUCTORDERID='"+msProductOrderId+"', PRODUCTCOMPID='"+msProductCompId+"'   WHERE ANALYSISFORMSID='"+lsPROCESS_STEP_P_ANALYSISITEM.get(0).get("ANALYSISFORMSID").toString()+"' ; ";
                                sSql = sSql + " UPDATE  PROCESS_STEP_P_ANALYSISITEM SET PRODUCTORDERID='" +msProductOrderId + "' ,PRODUCTCOMPID ='" +msProductCompId + "'            WHERE ANALYSISFORMSID='"+lsPROCESS_STEP_P_ANALYSISITEM.get(0).get("ANALYSISFORMSID").toString()+"' ; ";
 
					        }
				    	}
				    	
				    }
				 
//			    	if(lsCompTable.get(i).get("ISCOMPEXIST").toString().equals("N"))
//			    	{
//			    		sSql = sSql + "INSERT INTO PROCESS_STEP_NOBARCODE (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
//	                            + MESCommon.SysId + ",'"
//	                            + msProductOrderId + "','"
//	                            + msProductCompId + "','"
//	                            + msProductId + "','"
//	                            + msStepId+ "','"
//	                            + msStepSEQ + "','"
//	                            + msEqpId+ "','"
//	                            + lsCompTable.get(i).get("FINEPROCESSID").toString() + "','"
//	                            + lsCompTable.get(i).get("MaterialId").toString() + "','"
//	                            + lsCompTable.get(i).get("MaterialMame").toString() + "','"
//	                            + lsCompTable.get(i).get("MaterialType").toString() + "','"
//	                            + MESCommon.UserId + "','"
//	                            +MESCommon.UserName + "',"
//	                            + MESCommon.ModifyTime + ");";
//			    	}
			    	
			    }
			    
			 String sError= db.ExecuteSQL(sSql);
			 if (sError != "") {
					
					return sResult =sError;
				 }
			}
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return sResult =e.toString();
		}
		return sResult;
	}

	private String Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
		
			 List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		 
             String  sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
             String   sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn.this, sError);
			 }
             if (lsResule_M.size() > 0)
             {
                 sSQL =sSQL+ "UPDATE ANALYSISRESULT_M SET CHIEFANALYSISUSERID='" +MESCommon.UserId + "', CHIEFANALYSISUSER='" +MESCommon.UserName + "', CHIEFANALYSISTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ,ANALYSISJUDGEMENTRESULT='" + sResult + "',DATACOMPLETESTATUS='已完成',MODIFYUSERID='" + MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)  , TOREMENBER=''  WHERE   ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "' ;";
                       
	             sSQL = sSQL+ " delete from ANALYSISRESULT_MD where ANALYSISFORMSID='" + msAnalysisformsID + "' and SAMPLETIMES='" + msSampletimes + "' ;";
	           
	             sSQL =sSQL+ " delete from ANALYSISRESULT_MDD where ANALYSISFORMSID='" + msAnalysisformsID + "' and ANALYSISTIMESINDEX='0' and SAMPLETIMES='" + msSampletimes + "' ;";
	          

	         	for(int i=0;i<listTemp.size();i++)
				{
	         		String sFailureNum="0";
	                String sAnalysisAvgvalue="";
	                String sAnalysisMaxvalue="";
	                String sAnalysisMinvalue="";
	         		if(!listTemp.get(i).get("FINALVALUE").toString().trim().equals("OK"))
	         		{
	         			sFailureNum="1";
	         		}
	         		if(listTemp.get(i).get("RESULTTYPE").toString().equals("数值"))
	         		{
	         			  sAnalysisAvgvalue=listTemp.get(i).get("DATAVALUE").toString();
	                      sAnalysisMaxvalue=listTemp.get(i).get("DATAVALUE").toString();
	                      sAnalysisMinvalue=listTemp.get(i).get("DATAVALUE").toString();
	         		}
	         		
	         		 sSQL =sSQL+ "INSERT INTO ANALYSISRESULT_MD (ANALYSISFORMSID, SAMPLETIMES, ANALYSISMITEM, ANALYSISITEM, RESULTTYPE, JUDGEMENTSTANDARD, DATANUM, ISREQUIRED, ISJUDGEMENT, SAMPLINGNUM, ANALYSISUSERID, ANALYSISUSER, ANALYSISTIME, FAILURENUM, ANALYSISAVGVALUE, ANALYSISMAXVALUE, ANALYSISMINVALUE, MODIFYUSERID, MODIFYUSER, MODIFYTIME, SPECMINVALUE, SPECMAXVALUE, TARGETVALUE, OFFSETVALUE,OFFSETMINVALUE,OFFSETMAXVALUE,ISSHOWINIDCARD,ISCOVERSHOWINIDCARD)VALUES " +
	         		 		"( " + "'" + msAnalysisformsID + "'," + "'" + msSampletimes + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISMITEM").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISITEM").toString().trim() + "'," + 
	         				 "'" +listAnalysisTemp.get(i).get("RESULTTYPE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("ITEMSPEC").toString().trim()+ "'," + "'" + listAnalysisTemp.get(i).get("SAMPLESIZE").toString().trim() +
	         		 		"'," + "'" + listAnalysisTemp.get(i).get("ISNEED").toString().trim() + "'," + "'" +listAnalysisTemp.get(i).get("ISJUDGE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("SAMPLINGQTY").toString().trim() + "',"
	         				 + "'" + MESCommon.UserId + "'," + "'" +MESCommon.UserName + "', convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)," + "'"+sFailureNum+"'," + 
	         		 		"'" + sAnalysisAvgvalue + "'," + "'" + sAnalysisMaxvalue + "'," + "'" +sAnalysisMinvalue + "'," + "'" +MESCommon.UserId + "'," + "'" + MESCommon.UserName + "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)," 
	         		 		+ "'" + listAnalysisTemp.get(i).get("SPECMINVALUE").toString().trim() + "'," + 	"'" +listAnalysisTemp.get(i).get("SPECMAXVALUE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("TARGETVALUE").toString().trim() + "'," + 
	         		 		"'" +listAnalysisTemp.get(i).get("OFFSETVALUE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("OFFSETMINVALUE").toString().trim() + "'," +
	         		 		"'" + listAnalysisTemp.get(i).get("OFFSETMAXVALUE").toString().trim() + "','" + listAnalysisTemp.get(i).get("ISSHOWINIDCARD").toString().trim() + "','" + listAnalysisTemp.get(i).get("ISCOVERSHOWINIDCARD").toString().trim() + "') ;";
	         		
	         		 
	         		sSQL = sSQL + "INSERT INTO ANALYSISRESULT_MDD (ANALYSISFORMSID,SAMPLETIMES, ANALYSISMITEM, ANALYSISITEM, ANALYSISTIMESINDEX, SAMPLINGNUMINDEX, DATAVALUE, MODIFYUSERID, MODIFYUSER, MODIFYTIME)VALUES " +
	         				"( " + "'" + msAnalysisformsID + "'," + "'" + msSampletimes + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISMITEM").toString().trim()  + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISITEM").toString().trim() + "'," + "'0'," + 
	         				"'1'," + "'" + listTemp.get(i).get("DATAVALUE").toString().trim() + "'," + "'" + MESCommon.UserId + "'," + "'" + MESCommon.UserName + "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) );";
				}
	         	 String sMessage=	 db.ExecuteSQL(sSQL);
         	     if(!sMessage.equals(""))
         	     {
         	      MESCommon.showMessage(WIP_TrackIn.this,sMessage); 
         	      return sMessage;
         	     }
             }
       	     String sMessage= InsertSTEP_P(sResult);//插入零部件
       	     if(!sMessage.equals(""))
       	     {
       	      MESCommon.showMessage(WIP_TrackIn.this,sMessage); 
       	      return sMessage;
       	     }
       	  return ""; 
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return e.toString();
		}
	}
	
	public void Clear() {
		try {
			List<SpinnerData> lst = new ArrayList<SpinnerData>();
			ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,	android.R.layout.simple_spinner_item, lst);
			Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBid.setAdapter(Adapter);
			
			
			editProductCompID.setText("");
			editMaterialID.setText("");
			editProductModel.setText("");
			editMsheel.setText("");
			editDsheel.setText("");
			editMDsheel.setText("");
			editCustomerName.setText("");
			editPMMessage.setText("");
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			miQty=0;
            lsCompID .clear();lsAnalysisTable.clear();lsProcess .clear(); lsAnalysisData .clear();
            lsBid .clear(); lsMid.clear(); lsSid.clear(); lsSidTable.clear();lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();lsProductCUS.clear();lsDefect.clear();
            adapterTab0.notifyDataSetChanged();
            btnTab1.performClick();
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn.this,  e.toString());
		}
	}
	
	public String checkExist(String number)
	{
		String sResult="";
		try {
			List<HashMap<String, String>> lscheckExist=new ArrayList<HashMap<String, String>>();
			
		 	 String sSQL = "SELECT PRODUCTSERIALNUMBER AS NUMBER ,PRODUCTCOMPID FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER= '"+number+"'  ";
		 	
             String sError = db.GetData(sSQL, lscheckExist);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn.this, sError);
				return sResult=sError;
			 }
			 if(lscheckExist.size()>0)
			 {
				 return sResult="扫描条码【"+number+"】,已经被【"+lscheckExist.get(0).get("PRODUCTCOMPID").toString()+"】组装装配过";
			 }
			 //PRODUCTSERIALNUMBER和——p不一样表示不是次组立的轴承座！提示被次组立装配使用了。
			 sSQL = "SELECT SERIALNUMBER_P  AS NUMBER, PRODUCTSERIALNUMBER_OLD  FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P= '"+number+"' and  PRODUCTSERIALNUMBER!=''   ";
			 	
             sError = db.GetData(sSQL, lscheckExist);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn.this, sError);
				return sResult=sError;
			 }
			 if(lscheckExist.size()>0)
			 {
				 return sResult="扫描条码【"+number+"】,已经被【"+lscheckExist.get(0).get("PRODUCTSERIALNUMBER_OLD").toString()+"】次组立装配过";
			 }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return sResult=e.toString();
		}
		
	}
	
	//此方法只是关闭软键盘
	private void hintKbTwo() {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
		 if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		}
		
	//储存时检查是否符合「专配HZ_SPECIALMATCH」规则
	//查HZ_SPECIALMATCH  里，物料条码COMPIDSEQ（精加工号PRODUCTSERIALNUMBER）对应的专配物料编号INFORMATION
	private String Check_HZ_SPECIALMATCH()
	{
		String sResult = "", sSQL = "";
		List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
		try {
//			//1.先组合已扫描的零部件，再查询专配表HZ_SPECIALMATCH
//			//2.若某个零部件存在于专配表HZ_SPECIALMATCH，则
//			String sCompids = "";
//			for(int i=0;i<lsCompTable.size();i++)
//		    {
//				String sCompid = lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString();
//		    	sCompids += sCompids.equals("") ? "'" + sCompid + "'" : ",'" + sCompid + "'";
//		    }
//			if (!sCompids.equals(""))
//			{
//				sSQL = "SELECT DISTINCT PRODUCTSERIALNUMBER ID1,INFORMATION ID2 FROM HZ_SPECIALMATCH WHERE PRODUCTSERIALNUMBER IN (" + sCompids + ") OR INFORMATION IN (" + sCompids + ")";
//				sResult = db.GetData(sSQL, ls);
//				if (!sResult.equals("")) return sResult;
//				if (ls.size() != 0)
//				{
//					//有专配零件,检查专配规划
//					for(int i=0;i<ls.size();i++)
//				    {
//						String sID1 = ls.get(i).get("ID1").toString();
//						String sID2 = ls.get(i).get("ID2").toString();
//				    	if (!sCompids.contains("'" + sID1 + "'") || !sCompids.contains("'" + sID2 + "'"))
//				    	{
//				    		sResult += sResult.equals("") ? "以下零部件不符合专配规则：" : "";
//				    		if (sCompids.contains("'" + sID1 + "'"))
//				    		{
//				    			sResult += "\n*" + sID1 + " 应专配 " + sID2;
//				    		}
//				    		else
//				    		{
//				    			sResult += "\n*" + sID2 + " 应专配 " + sID1;
//				    		}
//				    	}
//				    }
//				}
//			}
			
			return sResult;
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return e.toString();
		}
	}
	//扫描时检查是否为不合格单判定特采品
	private void Check_SpecialAdoption(String sCompidseq)
	{
		String sResult = "", sSQL = "";
		List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
		try {
			msSpecialAdoption = "";
			sSQL = "SELECT B.PROJECTID, B.PRODUCTID ITNBR, B.PRODUCTSERIALNUMBER COMPID, B.COMPIDSEQ, B.MODIFYTIME FROM FLOW_FORM_UQF_S_NOW A INNER JOIN ANALYSISRESULT_QCD B ON A.PROJECTID=B.PROJECTID " +
					"WHERE A.ANALYSISJUDGEMENTRESULT='特采' AND B.COMPIDSEQ='"+sCompidseq+"'";
			sResult = db.GetData(sSQL, ls);
			if (ls.size() != 0)
			{
				String sCompid = ls.get(0).get("COMPID").toString();
				msSpecialAdoption = "Y";
				MESCommon.showMessage(WIP_TrackIn.this, "工件编号：" + sCompid + " 为特采品！");
				return;
			}
			
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn.this, e.toString());
			return;
		}
	}
		
		
}

