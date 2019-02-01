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
import com.example.hanbell_wip.WIP_TrackIn.wiptrackinAdapterTab0;
import com.example.hanbell_wip.WIP_TrackIn.wiptrackinAdapterTab0.ViewHolder;

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

public class WIP_OQC_QCANALYSIS extends Activity {

	private MESDB db = new MESDB();

	ListView lv0,lv1,lv2;
	Button btnConfirm, btnDelete,btnExit,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1;
	Spinner spBid,spLcSeq;
	EditText editInput,editLCID,editProductModel,editCustomer ;

	//	CheckBox cb ;
	TextView  h1, h2, h3,h4, h5, h9,h11,ht0,ht1,ht2,ht3,ht4;
	LinearLayout tab1, tab2, tab3;
	wipoqcAdapter adapter;
	wipoqcAdapterTab2 adapterTab2;
	wipoqcAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",  msProductId,msProductName, msProductModel,  msProductCompId,  msProductSerialNumber,  msStepId,  msBomflag="" ,msRepeter="",
	  msEqpId,msAnalysisformsID="",msSampletimes="",msStepSEQ,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE,msCustomer;
	String msSpecialAdoption = "";	//是否特采
	String msLcSeq="";//选中的序号
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
	private List<HashMap<String, String>> lsDefect = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsLcSeq = new ArrayList<HashMap<String, String>>();//LC序号
	private List<HashMap<String, String>> lsLcComp = new ArrayList<HashMap<String, String>>();//LC制造号码
	HashMap<String, String> mapSeqQty=new HashMap<String, String> ();//seq与数量的对应关系
	HashMap<String, String> mapItnbrSeq=new HashMap<String, String> ();//件号与序号的对应关系
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip__oqc__qcanalysis);

		// 取得控件

		try {

			spBid = (Spinner) findViewById(R.id.wipoqc_spBid);	
			editInput = (EditText) findViewById(R.id.wipoqc_tvInput);	
//			editProductCompID = (EditText) findViewById(R.id.wipoqc_tvProductCompid);	
			editLCID = (EditText) findViewById(R.id.wipoqc_tvLCID);	//LC序号
//			editMaterialID = (EditText) findViewById(R.id.wipoqc_tvMaterialid);	
			editProductModel = (EditText) findViewById(R.id.wipoqc_tvProductModel);	
			editCustomer = (EditText) findViewById(R.id.wipoqc_tvCustomer);	
//			editMsheel= (EditText) findViewById(R.id.wipoqc_tvMCheel);	
//			editDsheel= (EditText) findViewById(R.id.wipoqc_tvDCheel);	
//			editMDsheel= (EditText) findViewById(R.id.wipoqc_tvMDCheel);	
//			editPMMessage= (EditText) findViewById(R.id.wipoqc_tvPMMessage);	
//			editColer= (EditText) findViewById(R.id.wipoqc_tvProductColer);	
//			cb= (CheckBox)findViewById(R.id.cb);
			tab1 = (LinearLayout) findViewById(R.id.wipoqc_tab1);
			tab2 = (LinearLayout) findViewById(R.id.wipoqc_tab2);
			tab3 = (LinearLayout) findViewById(R.id.wipoqc_tab3);
//			ht0 = (TextView) findViewById(R.id.wipoqc_ht0);
			ht1 = (TextView) findViewById(R.id.wipoqc_ht1);
			ht2 = (TextView) findViewById(R.id.wipoqc_ht2);
			ht3 = (TextView) findViewById(R.id.wipoqc_ht3);
			ht4 = (TextView) findViewById(R.id.wipoqc_ht4);
			h1 = (TextView) findViewById(R.id.wipoqc_h1);
			h2 = (TextView) findViewById(R.id.wipoqc_h2);
			h3 = (TextView) findViewById(R.id.wipoqc_h3);
			h4 = (TextView) findViewById(R.id.wipoqc_h4);
			h5 = (TextView) findViewById(R.id.wipoqc_h5);
			h9= (TextView) findViewById(R.id.wipoqc_h9);
			h11= (TextView) findViewById(R.id.wipoqc_h11);
//			tvM= (TextView) findViewById(R.id.wipoqc_tvM);
//			tvD= (TextView) findViewById(R.id.wipoqc_tvD);
//			tvPM= (TextView) findViewById(R.id.wipoqc_tvPM);
			spLcSeq = (Spinner) findViewById(R.id.wipoqc_spLcSeq);
			
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
			h9.setTextColor(Color.WHITE);
			h9.setBackgroundColor(Color.DKGRAY);
			h11.setTextColor(Color.WHITE);
			h11.setBackgroundColor(Color.DKGRAY);
			h11.setBackgroundColor(Color.DKGRAY);
			lv0 = (ListView) findViewById(R.id.wipoqc_lv0);
			lv1 = (ListView) findViewById(R.id.wipoqc_lv1);
			lv2 = (ListView) findViewById(R.id.wipoqc_lv2);
			btnConfirm = (Button) findViewById(R.id.wipoqc_btnConfirm);	
			btnDelete=(Button) findViewById(R.id.wwipoqc_btnTab0_Delete);
			btnExit=(Button) findViewById(R.id.wipoqc_btnExit);
			btnTab1 = (Button) findViewById(R.id.wipoqc_btnTab1);
			btnTab2 = (Button) findViewById(R.id.wipoqc_btnTab2);
			btnTab3 = (Button) findViewById(R.id.wipoqc_btnTab3);		
			btnTab2_0 = (Button) findViewById(R.id.wipoqc_btnTab2_0_OK);
			btnTab2_1 = (Button) findViewById(R.id.wipoqc_btnTab2_1_OK);
			
			adapterTab0 = new wipoqcAdapterTab0(lsCompTable, this);
			lv0.setAdapter(adapterTab0);		
			adapter = new wipoqcAdapter(lsAnalysisTable, this);
			lv1.setAdapter(adapter);		
			adapterTab2 = new wipoqcAdapterTab2(lsSidTable, this);
			lv2.setAdapter(adapterTab2);
//			editMsheel.setVisibility(8);
//			editDsheel.setVisibility(8);
//			editMDsheel.setVisibility(8);
//			tvD.setVisibility(8);
//			tvM.setVisibility(8);

			
			// ***********************************************Start
			prefercesService  =new PrefercesService(this);  
		    params=prefercesService.getPreferences();  
		    ActionBar actionBar=getActionBar();
			actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
			actionBar.setTitle("出货检验");
		 	btnTab1.setBackgroundColor(0x88999999);
			btnTab2.setBackgroundColor(0x00000000);
			btnTab3.setBackgroundColor(0x00000000);
			

			String date = sDateFormatShort.format(new java.util.Date());	
			// 读取报工人员
			String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
			String sResult = db.GetData(sSql, lsuser);
			if (sResult != "") {
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
				finish();
			}
		
			if ( lsuser.size()==0) {
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, "请先进行人员设备报工！");
			
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				}
			}
		});
		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wipoqc_editTab2_0);
					if(lsAnalysisTable.size()>0)
					{
						 lsAnalysisTable.get(milv1RowNum).put("DATAVALUE",edit.getText().toString());
						 lsAnalysisTable.get(milv1RowNum).put("DISPLAYVALUE",edit.getText().toString());
						if(lsAnalysisTable.get(milv1RowNum).get("ISJUDGE").toString().trim().equals("Y"))
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
								
								
								showRow(milv1RowNum + 1);
							}else
							{
								 lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "");
							}
						}else
						{
						   lsAnalysisTable.get(milv1RowNum).put("FINALVALUE", "OK" );
						}
						hintKbTwo();
						adapter.notifyDataSetChanged();		
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wipoqc_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wipoqc_radioButtonNG);
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				}
			}
		});
		// 添加Spinner事件监听
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
						wipoqcAdapterTab0.ViewHolder holder = (wipoqcAdapterTab0.ViewHolder) arg1.getTag();
						// 改变CheckBox的状态
						holder.cb.toggle();
						// 将CheckBox的选中状况记录下来
						wipoqcAdapterTab0.getIsSelected().put(position,
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
						MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
					}
					}
					
				});
		// 控件事件
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wipoqcAdapter.ViewHolder holder = (wipoqcAdapter.ViewHolder) arg1.getTag();
				showRow(position);
				
			}
		});
	
		// 控件事件
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wipoqcAdapterTab2.ViewHolder holder = (wipoqcAdapterTab2.ViewHolder) arg1.getTag();
			}
		});
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipoqc_tvInput);
//					if ( lsuser.size()==0) {
//						MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, "请先进行人员设备报工！");
//						return false;
//					}
					if (txtInput.getText().toString().trim().length() == 0) {
						txtInput.setText("");
						MESCommon.show(WIP_OQC_QCANALYSIS.this, "请扫描条码!");
						return false;
					}
					//覆盖添加
					if(editLCID.getText().toString().length()>0) {
						AlertDialog alert=	new AlertDialog.Builder(WIP_OQC_QCANALYSIS.this).setTitle("确认").setMessage("已有LC单号是否继续加入,确认将清除现有数据!")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				            Clear();
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
						return false  ;
					}
					String sResult="";

					//查询ERP的LC单
					if (editLCID.getText().toString().equals("")) {
						lsLcSeq.clear();
						String sLCID=txtInput.getText().toString().trim();
						String sSql="SELECT trseq,itnbr,itdsc,shpqy1,cusna FROM  cdrdta "
								+ "LEFT JOIN cdrhad ON cdrdta.shpno=cdrhad.shpno " + 
								"    LEFT JOIN   cdrcus ON cdrhad.cusno=cdrcus.cusno "
								+ "WHERE cdrdta.shpno='"+sLCID+"' ORDER BY  cdrdta.trseq";//查询
						sResult = db.ErpGetData(sSql, lsLcSeq);
						if (sResult != "") {
							MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
							finish();						
						}
						if(lsLcSeq.size()>0)
						{							
						editLCID.setText(sLCID);
						msCustomer=lsLcSeq.get(0).get("cusna");
						editCustomer.setText(msCustomer);//客户	
						}
						else {
							txtInput.setText("");
							MESCommon.show(WIP_OQC_QCANALYSIS.this, "请确认LC单是否正确!");
							return false;
						}
						ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
		
						for (int i = 0; i < lsLcSeq.size(); i++) {
							SpinnerData c = new SpinnerData(lsLcSeq.get(i)
									.get("trseq").toString(), lsLcSeq.get(i)
									.get("trseq").toString());
							lst.add(c);
							mapSeqQty.put( lsLcSeq.get(i).get("trseq").toString(),  lsLcSeq.get(i).get("shpqy1").toString());
							mapItnbrSeq.put( lsLcSeq.get(i).get("trseq").toString(),  lsLcSeq.get(i).get("itnbr").toString());
						}
						

						ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
								WIP_OQC_QCANALYSIS.this,
								android.R.layout.simple_spinner_item, lst);
						Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spLcSeq.setAdapter(Adapter1);
						// 设置显示当前选择的项
						MESCommon.setSpinnerItemSelectedByValue(spLcSeq,"trseq");						
						
						editInput.setText("");
					}
					else {//刷制造号码
						lsProcess.clear();
						SpinnerData LcSeq = (SpinnerData) spLcSeq.getSelectedItem();
						msLcSeq=LcSeq.text;
						lsCompID.clear();				 
						 sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","制造号码","装配", lsCompID);
						
						if(lsCompID.size()<=0)
						{   
								MESCommon.show(WIP_OQC_QCANALYSIS.this, "物料条码【" + txtInput.getText().toString().trim() + "】， 不存在!");
							    return false;
						}else
						{  						
							lsProcess.clear();
						String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
						sResult = db.GetProductProcess(sOrderID,txtInput.getText().toString().trim(), lsProcess);
						if (sResult != "") {
							MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
							finish();
							
						}							
						if(lsProcess.size()==0)
						{
							MESCommon.show(WIP_OQC_QCANALYSIS.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
							Clear();
							return false;
						}
						
						msProductOrderId=lsProcess.get(0).get("PRODUCTORDERID").toString();
						msProductCompId=lsProcess.get(0).get("PRODUCTCOMPID").toString();
						msProductSerialNumber=lsProcess.get(0).get("PRODUCTSERIALNUMBER").toString();
						
						//查询制造号码是否被出货过						
						lsLcComp.clear();
						String  sSql=" SELECT * FROM HZ_OQC_QCANALYSIS WHERE PRODUCTCOMPID ='"+msProductCompId+"'  ";
						String sError= db.GetData(sSql,  lsLcComp);
						 if (sError != "") {
								MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
								return false;
							 }
						if (lsLcComp.size()>0) {
							AlertDialog alert=	new AlertDialog.Builder(WIP_OQC_QCANALYSIS.this).setTitle("确认").setMessage("条码[" + msProductCompId + "] 已绑定,LC单号["+lsLcComp.get(0).get("LCNO").toString()+"]!"+",是否继续加入!")
							.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
			            @Override  
			            public void onClick(DialogInterface dialog,int which) {  

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
						 
						 
						lsProduct.clear();
						  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ";
						 sError= db.GetData(sSql,  lsProduct);
				
						 if (sError != "") {
								MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
								return false;
							 }

							if(!lsProduct.get(0).get("PRODUCTID").toString().equals(mapItnbrSeq.get(msLcSeq)))
							{
								MESCommon.show(WIP_OQC_QCANALYSIS.this,"制造号码【"+txtInput.getText().toString().trim()+"】的件号和LC序号对应的件号不一致！");
								return false;
							}
							msProductModel=lsProduct.get(0).get("PRODUCTMODEL").toString();
							editProductModel.setText(msProductModel);//设置机型
							msProductId=lsProduct.get(0).get("PRODUCTID").toString();
							msProductName=lsProduct.get(0).get("PRODUCTNAME").toString();
							for(int i=0;i<lsCompTable.size();i++)
							{
								if(txtInput.getText().toString().trim().equals(lsCompTable.get(i).get("SEQ").toString())&&msLcSeq.equals(lsCompTable.get(i).get("ISSP").toString()))
								{
									MESCommon.show(WIP_OQC_QCANALYSIS.this, "条码[" + lsCompTable.get(i).get("SEQ").toString() + "] 已绑定,LC序号["+lsCompTable.get(i).get("ISSP").toString()+"]!");
								    return false;

								}
							}


								exeLstCompTable( );
							
						}

							editInput.setText("");
							setFocus(editLCID);
					}					
					} catch (Exception e) {
						MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
						return false;
					}
				}			
				return false;
			}
		});
		editLCID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				setFocus(editInput);
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
							MESCommon.show(WIP_OQC_QCANALYSIS.this, "请选择要删除的制造号码");
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				}
			}
		});
		
	
		// btnOK
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {

					
					if (editLCID.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_OQC_QCANALYSIS.this, "请先扫描条码在进行报工！");
						return;
					}
					
						Save();
					  Toast.makeText(WIP_OQC_QCANALYSIS.this, "报工完成!", Toast.LENGTH_SHORT).show();                  
					  Clear();
			 	   	   
				} catch (Exception e) {
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
					MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this,  e.toString());
		}
	}
//添加进列表
		public static class wipoqcAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wipoqcAdapterTab0(List<HashMap<String, String>> items,
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
				holder.tvMaterialID = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialID);
				holder.tvMaterialMame = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialMame);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvISSP.setText(getItem(position).get("ISSP").toString());	//是否特采
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvMaterialID.setText(getItem(position).get("MaterialId").toString());
			holder.tvMaterialMame.setText(getItem(position).get("MaterialMame").toString());	

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
			wipoqcAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvISSP;
			TextView tvSerialnumberId;
			TextView tvMaterialID;
			TextView tvMaterialMame;
		}
	}

		private  void exeLstCompTable()
		{
			try {		
				
				String sError ="";

					 sError=BindHashMap();
						if (sError != "") {
							MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
							return ;
						}					
				
			
			} catch (Exception e) {
				// TODO: handle exception
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
				String  sPRODUCTCOMPID=lsCompID.get(0).get("PRODUCTCOMPID").toString();
		
				//取得提示的零部件编号
				String sNewNumber="";
				HashMap<String, String> hs = new HashMap<String, String>();	
				hs.put("MaterialId",msProductId);
				hs.put("MaterialMame", msProductName);
				hs.put("SEQ", sSEQ);
				hs.put("PRODUCTSERIALNUMBER",sPRODUCTCOMPID);
				hs.put("PRODUCTMODEL",msProductModel);
				hs.put("CUSTOMER",msCustomer);
				sNewNumber=sPRODUCTCOMPID;
//				if (!sFINEPROCESSID.equals("")) {
//					
//					if(!sMaterialId.equals(""))
//					{
//				      String sFinId=getCompid(sMaterialId);
//				      if(!sFinId.equals(""))
//				      {
//				    	  if(sFinId.equals("半成品方型件"))
//				    	  {
//				    		  //方型件不打精加工号了，采用粗加工！
//				    		  hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);	
//				    		  sNewNumber=sRAWPROCESSID;
//				    	  }else {
//				    		  hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);		
//				    		  sNewNumber=sFINEPROCESSID;
//						}				    	 
//				      }else {
//				    	  hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);		
//			    		  sNewNumber=sFINEPROCESSID;
//					  }
//					}else {
//						hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);	
//						sNewNumber=sFINEPROCESSID;
//					}
//				}else  if (!sRAWPROCESSID.equals(""))
//				{
//					hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);
//					sNewNumber=sRAWPROCESSID;
//				}
//				else if (!sLotID.equals(""))
//				{
//					hs.put("PRODUCTSERIALNUMBER",sLotID);
//					sNewNumber=sLotID;
//				}
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
	
				hs.put("ISSP",msLcSeq );	//是否为特采
				lsCompTable.add(0,  hs);							
				adapterTab0.notifyDataSetChanged();	
				Toast.makeText(WIP_OQC_QCANALYSIS.this, "制造号码：【"+sNewNumber+"】,加入成功！", Toast.LENGTH_SHORT).show();	
				editInput.setText("");
				msRepeter="";
				msBomflag="";
//				setFocus(editProductCompID);
				return sResult;
			} catch (Exception e) {
				// TODO: handle exception
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
				return e.toString();
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

	public static class wipoqcAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wipoqcAdapter(List<HashMap<String, String>> items,
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
	
	public static class wipoqcAdapterTab2 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wipoqcAdapterTab2(List<HashMap<String, String>> items,
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
				holder.tvAnalySidID = (TextView) convertView.findViewById(R.id.wippaintingendlv2_tvAnalySidID);
				holder.tvAnalySidItem = (TextView) convertView.findViewById(R.id.wippaintingendlv2_tvAnalySidItem);
			
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


	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wipoqc_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wipoqc_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wipoqc_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wipoqc_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wipoqc_tab2_1);

				
				if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("数值")) {
					tab2_0.setVisibility(View.VISIBLE);
					tab2_1.setVisibility(View.INVISIBLE);
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
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
		}
	}
	



	private void Save() {
		try {
             String  sSQL ="";
				for(int i=0;i<lsCompTable.size();i++)
				{
	         		sSQL += "INSERT INTO HZ_OQC_QCANALYSIS (SYSID,LCNO, LCSEQ, PRODUCTCOMPID, PRODUCTID, PRODUCTNAME, PRODUCTMODEL,CUSTOMER, MODIFYUSERID, MODIFYUSER, MODIFYTIME)VALUES " +
	         				"( " + MESCommon.SysId  + "," + "'" +editLCID.getText().toString() + "'," + "'" + lsCompTable.get(i).get("ISSP").toString()  + "'," + "'" + lsCompTable.get(i).get("SEQ").toString()+ "','" + lsCompTable.get(i).get("MaterialId").toString()+"','"
	         				+ lsCompTable.get(i).get("MaterialMame").toString()+"','" +lsCompTable.get(i).get("PRODUCTMODEL").toString()+ "','" +lsCompTable.get(i).get("CUSTOMER").toString()+ "','"
	         				+ MESCommon.UserId + "'," + "'" + MESCommon.UserName + "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) );";
				}
	         	 String sMessage=	 db.ExecuteSQL(sSQL);
         	     if(!sMessage.equals(""))
         	     {
         	      MESCommon.showMessage(WIP_OQC_QCANALYSIS.this,sMessage); 
         	      return;
         	     }
             
       	   
       	     
       	
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
		}
	}
	
	public void Clear() {
		try {
			List<SpinnerData> lst = new ArrayList<SpinnerData>();
			ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,	android.R.layout.simple_spinner_item, lst);
			Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBid.setAdapter(Adapter);
			spLcSeq.setAdapter(Adapter);
			
//			editProductCompID.setText("");
//			editMaterialID.setText("");
			editLCID.setText("");
			editProductModel.setText("");
			editCustomer.setText("");
//			editMsheel.setText("");
//			editDsheel.setText("");
//			editMDsheel.setText("");
//	        editColer.setText("");
//			editPMMessage.setText("");
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			miQty=0;
            lsCompID .clear();lsAnalysisTable.clear();lsProcess .clear(); lsAnalysisData .clear();
            lsBid .clear(); lsMid.clear(); lsSid.clear(); lsSidTable.clear();lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();lsDefect.clear();
            btnTab1.performClick();
            adapterTab0.notifyDataSetChanged();
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		 } catch (Exception e) {
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this,  e.toString());
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
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
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
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
				return sResult=sError;
			 }
			 if(lscheckExist.size()>0)
			 {
				 return sResult="扫描条码【"+number+"】,已经被【"+lscheckExist.get(0).get("PRODUCTSERIALNUMBER_OLD").toString()+"】次组立装配过";
			 }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
		
		
		
}
