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

public class WIP_TrackIn_CRM extends Activity {

	private MESDB db = new MESDB();

	ListView lv0,lv1,lv2_1,lv2_2;
	Button btnConfirm, btnAdd, btnTemporary,btnDelete,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1,btnSP;
	Spinner spResponsibility;	//责任判定
	EditText editInput,editCrmno,editMaterialID,editProductModel,editMsheel,editDsheel,editMDsheel,editPMMessage,editCustomerName;
	EditText editCustomRequest, editDisassembleMemo;
	
	//	CheckBox cb ;
	TextView  h1, h2, h3, h6,h7,h8,h9,h10,h11,h12,tvM,tvD,tvPM,tvCM,lv2_1_1, lv2_1_2,lv2_2_1,lv2_2_2;
	LinearLayout tab1, tab2, tab3;
	wiptrackincrmAdapter adapter;
	wiptrackincrmAdapterTab2_1 adapterTab2_1;
	wiptrackincrmAdapterTab2_2 adapterTab2_2;
	wiptrackincrmAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String,String> params;
	String  msCrmno="", msProductOrderId="",  msProductId,  msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID="",msSampletimes="",msStepSEQ,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE;
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
	private List<HashMap<String, String>> lsCRM_HK_FW006 = new ArrayList<HashMap<String, String>>();
	
	//责任判定
	private List<HashMap<String, String>> lsResponsibility = new ArrayList<HashMap<String, String>>();
	//故障类别
	private List<HashMap<String, String>> lsTroubleType = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsTroubleTypeResult = new ArrayList<HashMap<String, String>>();
	//泄漏位置
	private List<HashMap<String, String>> lsPosition = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsPositionResult = new ArrayList<HashMap<String, String>>();
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");
	
	private Boolean mbUserLogin = false;
	private Boolean mbIsStepPause = false;
	private MenuItem mMenuItem;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_track_in_crm);

		// 取得控件
		String sSql="", sResult="";
		try {

			spResponsibility = (Spinner) findViewById(R.id.wiptrackincrm_spResponsibility);
			
			editInput = (EditText) findViewById(R.id.wiptrackincrm_tvInput);	
			editCrmno = (EditText) findViewById(R.id.wiptrackincrm_tvCrmno);	
			editMaterialID = (EditText) findViewById(R.id.wiptrackincrm_tvMaterialid);	
			editProductModel = (EditText) findViewById(R.id.wiptrackincrm_tvProductModel);	
			editMsheel= (EditText) findViewById(R.id.wiptrackincrm_tvMCheel);	
			editDsheel= (EditText) findViewById(R.id.wiptrackincrm_tvDCheel);	
			editMDsheel= (EditText) findViewById(R.id.wiptrackincrm_tvMDCheel);	
			editPMMessage= (EditText) findViewById(R.id.wiptrackincrm_tvPMMessage);	
			editCustomerName= (EditText) findViewById(R.id.wiptrackincrm_tvCustomerName);	
			editCustomRequest= (EditText) findViewById(R.id.wiptrackincrm_tvCustomRequest);
			editDisassembleMemo= (EditText) findViewById(R.id.wiptrackincrm_tvDisassembleMemo);
			
			tab1 = (LinearLayout) findViewById(R.id.wiptrackincrm_tab1);
			tab2 = (LinearLayout) findViewById(R.id.wiptrackincrm_tab2);
			tab3 = (LinearLayout) findViewById(R.id.wiptrackincrm_tab3);
			h1 = (TextView) findViewById(R.id.wiptrackincrm_h1);
			h2 = (TextView) findViewById(R.id.wiptrackincrm_h2);
			h3 = (TextView) findViewById(R.id.wiptrackincrm_h3);
			h6 = (TextView) findViewById(R.id.wiptrackincrm_h6);
			h7= (TextView) findViewById(R.id.wiptrackincrm_h7);
			h8= (TextView) findViewById(R.id.wiptrackincrm_h8);
			h9= (TextView) findViewById(R.id.wiptrackincrm_h9);
			h10= (TextView) findViewById(R.id.wiptrackincrm_h10);
			h11= (TextView) findViewById(R.id.wiptrackincrm_h11);
			h12= (TextView) findViewById(R.id.wiptrackincrm_h12);
			tvM= (TextView) findViewById(R.id.wiptrackincrm_tvM);
			tvD= (TextView) findViewById(R.id.wiptrackincrm_tvD);
			tvPM= (TextView) findViewById(R.id.wiptrackincrm_tvPM);
			tvCM= (TextView) findViewById(R.id.wiptrackincrm_tvCM);
			//lv2_1_1= (TextView) findViewById(R.id.wiptrackincrm_lv2_1_1);
			lv2_1_2= (TextView) findViewById(R.id.wiptrackincrm_lv2_1_2);
			//lv2_2_1= (TextView) findViewById(R.id.wiptrackincrm_lv2_2_1);
			lv2_2_2= (TextView) findViewById(R.id.wiptrackincrm_lv2_2_2);
			
			h1.setTextColor(Color.WHITE);
			h1.setBackgroundColor(Color.DKGRAY);
			h2.setTextColor(Color.WHITE);
			h2.setBackgroundColor(Color.DKGRAY);
			h3.setTextColor(Color.WHITE);
			h3.setBackgroundColor(Color.DKGRAY);
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
			//lv2_1_1.setTextColor(Color.WHITE);
			//lv2_1_1.setBackgroundColor(Color.DKGRAY);
			lv2_1_2.setTextColor(Color.WHITE);
			lv2_1_2.setBackgroundColor(Color.DKGRAY);
			//lv2_2_1.setTextColor(Color.WHITE);
			//lv2_2_1.setBackgroundColor(Color.DKGRAY);
			lv2_2_2.setTextColor(Color.WHITE);
			lv2_2_2.setBackgroundColor(Color.DKGRAY);
			
			lv0 = (ListView) findViewById(R.id.wiptrackincrm_lv0);
			lv1 = (ListView) findViewById(R.id.wiptrackincrm_lv1);
			lv2_1 = (ListView) findViewById(R.id.wiptrackincrm_lv2_1);
			lv2_2 = (ListView) findViewById(R.id.wiptrackincrm_lv2_2);
			btnConfirm = (Button) findViewById(R.id.wiptrackincrm_btnConfirm);
			btnTemporary = (Button) findViewById(R.id.wiptrackincrm_btnTemporary);
			btnAdd = (Button) findViewById(R.id.wiptrackincrm_btnAdd);			
			btnDelete=(Button) findViewById(R.id.wiptrackincrm_btnTab0_Delete);
			btnTab1 = (Button) findViewById(R.id.wiptrackincrm_btnTab1);
			btnTab2 = (Button) findViewById(R.id.wiptrackincrm_btnTab2);
			btnTab3 = (Button) findViewById(R.id.wiptrackincrm_btnTab3);		
			btnTab2_0 = (Button) findViewById(R.id.wiptrackincrm_btnTab2_0_OK);
			btnTab2_1 = (Button) findViewById(R.id.wiptrackincrm_btnTab2_1_OK);
			btnSP = (Button) findViewById(R.id.wiptrackincrm_btnSP);
			adapterTab0 = new wiptrackincrmAdapterTab0(lsCompTable, this);
			lv0.setAdapter(adapterTab0);
			adapter = new wiptrackincrmAdapter(lsAnalysisTable, this);
			lv1.setAdapter(adapter);
			
			//责任判定
			sSql = "SELECT A.PHRASEID ITEM FROM MPHRASE A WHERE A.PHRASETYPE='FW_RESPONSIBILITY' ORDER BY CAST(A.SEQ AS INT)";
			sResult = db.GetData(sSql, lsResponsibility);
			if (sResult != "") {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
				finish();
			}
			List<SpinnerData> lst = new ArrayList<SpinnerData>();
			SpinnerData c = new SpinnerData("", "");
			lst.add(c);
			for (int i = 0; i < lsResponsibility.size(); i++) {
				c = new SpinnerData(lsResponsibility.get(i).get("ITEM").toString(), lsResponsibility.get(i).get("ITEM").toString());
				lst.add(c);
			}
			ArrayAdapter<SpinnerData> AdapterSP = new ArrayAdapter<SpinnerData>(WIP_TrackIn_CRM.this,	android.R.layout.simple_spinner_item, lst);
			AdapterSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spResponsibility.setAdapter(AdapterSP);
			//故障类别
			sSql = "SELECT 'N' CHECKFLAG, A.PHRASEID ITEM FROM MPHRASE A WHERE A.PHRASETYPE='FW_TROUBLETYPE' ORDER BY CAST(A.SEQ AS INT)";
			sResult = db.GetData(sSql, lsTroubleType);
			if (sResult != "") {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
				finish();
			}
			//泄漏位置
//			sSql = "SELECT 'N' CHECKFLAG, A.PHRASEID ITEM FROM MPHRASE A WHERE A.PHRASETYPE='FW_POSITION' ORDER BY CAST(A.SEQ AS INT)";
//			sResult = db.GetData(sSql, lsPosition);
//			if (sResult != "") {
//				MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
//				finish();
//			}
			adapterTab2_1 = new wiptrackincrmAdapterTab2_1(lsTroubleType, this);
			lv2_1.setAdapter(adapterTab2_1);
//			adapterTab2_2 = new wiptrackincrmAdapterTab2_2(lsPosition, this);
//			lv2_2.setAdapter(adapterTab2_2);
			
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
			sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
			sResult = db.GetData(sSql, lsuser);
			if (sResult != "") {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
				finish();
			}
		
			if ( lsuser.size()==0) {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
				//finish();
				mbUserLogin = false;
			}
			else
			{
				mbUserLogin = true;
			}
			//依站別決定控件是否可使用
			msStepId = params.get("StepID");
			if (msStepId.equals("服务拆解站"))
			{
				editCustomRequest.setEnabled(true);
				editDisassembleMemo.setEnabled(true);
				spResponsibility.setEnabled(true);
				lv2_1.setEnabled(true);
				lv2_2.setEnabled(false);
			}
			else if (msStepId.equals("服务组装站") || msStepId.equals("服务清洗站") || msStepId.equals("一厂涂装站") || msStepId.equals("服务返修站"))
			{
				editCustomRequest.setEnabled(false);
				editDisassembleMemo.setEnabled(false);
				spResponsibility.setEnabled(false);
				lv2_1.setEnabled(false);
				lv2_2.setEnabled(false);
			}
			else if (msStepId.equals("服务后制程站"))
			{
				editCustomRequest.setEnabled(false);
				editDisassembleMemo.setEnabled(false);
				spResponsibility.setEnabled(false);
				lv2_1.setEnabled(false);
				lv2_2.setEnabled(true);
			}
			
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn_CRM.this,  e.toString());
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});
		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wiptrackincrm_editTab2_0);
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
									sminValueString="-9999999999";
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackincrm_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackincrm_radioButtonNG);
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});
		
		// 控件事件
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				try{
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackincrmAdapterTab0.ViewHolder holder = (wiptrackincrmAdapterTab0.ViewHolder) arg1.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				wiptrackincrmAdapterTab0.getIsSelected().put(position,
						holder.cb.isChecked());
				milv0RowNum=position;
//				if(!lsCompTable.get(milv0RowNum).get("IS_INSERT").equals("N"))
//				{
//					if(holder.cb.isChecked())
//					{
//					lsCompTable	.get(position).put("CHECKFLAG","Y"); 
//					}else
//					{
//					lsCompTable.get(position).put("CHECKFLAG","N"); 
//					}
//				}else {					
//					holder.cb.setChecked(false);
//				}
				if(holder.cb.isChecked())
				{
				lsCompTable	.get(position).put("CHECKFLAG","Y"); 
				}else
				{
				lsCompTable.get(position).put("CHECKFLAG","N"); 
				}
			} catch (Exception e) {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
			}
			}
			
		});
		// 控件事件
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackincrmAdapter.ViewHolder holder = (wiptrackincrmAdapter.ViewHolder) arg1.getTag();
				showRow(position);
				
			}
		});
		// 控件事件
		lv2_1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				try{
					// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
					wiptrackincrmAdapterTab2_1.ViewHolder holder = (wiptrackincrmAdapterTab2_1.ViewHolder) arg1.getTag();
					// 改变CheckBox的状态
					holder.cb.toggle();
					// 将CheckBox的选中状况记录下来
					wiptrackincrmAdapterTab2_1.getIsSelected().put(position,
							holder.cb.isChecked());
					if(holder.cb.isChecked())
					{
						lsTroubleType.get(position).put("CHECKFLAG","Y"); 
					}
					else
					{
						lsTroubleType.get(position).put("CHECKFLAG","N"); 
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
			
		});
		// 控件事件
		lv2_2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				try{
					// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
					wiptrackincrmAdapterTab2_2.ViewHolder holder = (wiptrackincrmAdapterTab2_2.ViewHolder) arg1.getTag();
					// 改变CheckBox的状态
					holder.cb.toggle();
					// 将CheckBox的选中状况记录下来
					wiptrackincrmAdapterTab2_2.getIsSelected().put(position,
							holder.cb.isChecked());
					if(holder.cb.isChecked())
					{
						lsPosition.get(position).put("CHECKFLAG","Y"); 
					}
					else
					{
						lsPosition.get(position).put("CHECKFLAG","N"); 
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
			
		});
		
		//****** 扫描条码 ***********************************
		//一定要先扫描「制造号码」，再扫描零部件，否则需报错
		//**************************************************
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String sResult="";
				
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					try {
						if (!mbUserLogin)
						{
							MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
							return false;
						}
						EditText txtInput = (EditText) findViewById(R.id.wiptrackincrm_tvInput);
//						if ( lsuser.size()==0) {
//							MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
//							return false;
//						}
						if (txtInput.getText().toString().trim().length() == 0) {
							txtInput.setText("");
							MESCommon.show(WIP_TrackIn_CRM.this, "请扫描条码!");
							return false;
						}
						//检查是否重复扫CRMNO
						if (txtInput.getText().toString().trim().equals(editCrmno.getText().toString()))
						{
							txtInput.setText("");
							MESCommon.show(WIP_TrackIn_CRM.this, "MES单号重复扫描!");
							return false;
						}
						
//						lsCompID.clear();
//						String sTempXID = txtInput.getText().toString().toUpperCase();
//						
//						//先描制造号码，才能扫描零部件
//						if(editCrmno.getText().toString().equals(""))
//						{
//							sResult = db.GetProductSerialNumber_CRM(txtInput.getText().toString().trim(),"",msProductOrderId, "QF","MES单号","装配", lsCompID);
//						}
//						else
//						{
//							sResult = db.GetProductSerialNumber_CRM(txtInput.getText().toString().trim(),"","", "QF","零部件","装配", lsCompID);
//						}
//						if (!sResult.equals(""))
//	                    {
//							txtInput.setText("");
//	                    }
//						else
//	                    {
//							if (editCrmno.getText().toString().equals("") &&lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
//	                        {
//								MESCommon.show(WIP_TrackIn_CRM.this,"请先刷入MES单号!");	
//	                    		txtInput.setText("");
//	                            return false;
//	                        }
//	                    }
//						 
//						if(lsCompID.size()>0)
//						{
//							txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
//							
//						}
//							
//						if (lsCompID.size() == 0 && editCrmno.getText().toString().equals(""))
//	                    {
//							MESCommon.show(WIP_TrackIn_CRM.this,sResult);
//	                        return false;
//	                    }
//	                    else if (lsCompID.size() == 0 && !editCrmno.getText().toString().equals("") )
//	                    {
//	//                    	cb.setEnabled(true);
//	                    	MESCommon.show(WIP_TrackIn_CRM.this,sResult+",是否需手动添加！");
//	                        return false;
//	                    }
//	                    else
//	                    {
//	                    	txtInput.setText(sTempXID);
//	                    }
						
						//刷主件
						if(editCrmno.getText().toString().equals(""))
						{
							lsCompID.clear();
							sResult = db.GetProductSerialNumber_CRM(txtInput.getText().toString().trim(),"",msProductOrderId, "QF","MES单号","装配", lsCompID);
							if (!sResult.equals(""))
							{
								txtInput.setText("");
								MESCommon.show(WIP_TrackIn_CRM.this,sResult);
		                        return false;
							}
							if (lsCompID.size() == 0)
							{
								txtInput.setText("");
								MESCommon.show(WIP_TrackIn_CRM.this,"MES单号查无资料!");
		                        return false;
							}
							
							String sSql="",sError="";
							//读取
							lsCRM_HK_FW006.clear();
							sSql = "SELECT A.CUSTOMREQUEST, A.DISASSEMBLEMEMO, A.RESPONSIBILITY,A.hdcptype FROM CRM_HK_FW006 A WHERE A.CRMNO='"+txtInput.getText().toString().trim().toUpperCase()+"'";
							sError= db.GetData(sSql,  lsCRM_HK_FW006);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
								return false;
							}
							if (lsCRM_HK_FW006.size() == 0)
							{
								MESCommon.showMessage(WIP_TrackIn_CRM.this, "MES单号查无资料!");
								return false;
							}
							lsProduct.clear();
							sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsCompID.get(0).get("MATERIALID").toString()+"'  ;";
							sError= db.GetData(sSql,  lsProduct);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
								return false;
							}
							
							//初始化工件信息
							//editCrmno.setText(lsCompID.get(0).get("CRMNO").toString());
							//editMaterialID.setText(lsProduct.get(0).get("PRODUCTID").toString());
							//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
							//editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());	
							msCrmno = lsCompID.get(0).get("CRMNO").toString();
							msProductOrderId=lsCompID.get(0).get("PRODUCTORDERID").toString();
							msProductOrderId = msCrmno;
							msProductId=lsProduct.get(0).get("PRODUCTID").toString();
							msProductCompId=lsCompID.get(0).get("PRODUCTCOMPID").toString();
							msProductSerialNumber=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
							msStepId = params.get("StepID");
							msStepSEQ = "1";
							msEqpId = params.get("EQPID");
							miQty=1;
							String sFWType = lsCRM_HK_FW006.get(0).get("hdcptype").toString();	//服务产线类别
							//判断是否为报工暂停
							String sStepPause = db.IsStepPause(msCrmno, msProductCompId, msStepId, msEqpId);
							mbIsStepPause = sStepPause.equals("false") ? false: true;
							if (mbIsStepPause)
								mMenuItem.setTitle("解除暂停");
							else
								mMenuItem.setTitle("报工暂停");
							
	//						lsAnalysisFinalData.clear();
	//						//判断有没有待最终判定的记录，如果有不能继续进行
	//						sSql=" SELECT * FROM ANALYSISWAITLIST WHERE   SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID = '"+msProductCompId+"' AND QCTYPE = '制程检验' AND QC_ITEM = '自主检验' AND ANALYSISSTATUS = '待最终判定'  ;";
	//						sError= db.GetData(sSql,  lsAnalysisFinalData);
	//						if (sError != "") {
	//							MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
	//							return false;
	//						}
	//						if(lsAnalysisFinalData.size()>0)
	//						{
	//							MESCommon.showMessage(WIP_TrackIn_CRM.this, "制造号码：【"+msProductCompId+"】,正在等待最终判定，不能进行报工作业！");
	//							return false;
	//						}
							
							//初始化检验项目
							lsAnalysisData.clear();
							sResult = db.GetAnalysisData(msProductOrderId,msProductId,msProductCompId,"",msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
							if (sResult != "") {
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
								finish();
							}
							
							if(lsAnalysisData.size()==0)
							{
								List<HashMap<String, String>> lsdtAnalysisformsID = new ArrayList<HashMap<String, String>>();
								String sSQL = "SELECT * FROM ANALYSISWAITLIST WHERE PRODUCTORDERID='"+msProductOrderId+"' AND SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID ='"+msProductCompId+"'  AND QCTYPE = '制程检验' AND QC_ITEM ='自主检验'  AND ANALYSISSTATUS = '待检验' AND FORMSSTATUS = '待处理' ";
						        sResult = db.GetData(sSQL, lsdtAnalysisformsID);
						    	if (sResult != "") {
									MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
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
											String sminValueString="";
										    String smaxValueString="";
											if(lsAnalysisData.get(i).get("SPECMINVALUE").toString().equals(""))
											{
												sminValueString="-9999999999";
											}else
											{
												sminValueString=lsAnalysisData.get(i).get("SPECMINVALUE").toString();
											}
											if(lsAnalysisData.get(i).get("SPECMAXVALUE").toString().equals(""))
											{
												smaxValueString="9999999999";
											}else {									
												smaxValueString=lsAnalysisData.get(i).get("SPECMAXVALUE").toString()	;
											}
											
											if (Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())>=Double.parseDouble(sminValueString)&&
													Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())<=Double.parseDouble(smaxValueString)	)
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
												String sminValueString="";
											    String smaxValueString="";
												if(lsAnalysisData.get(i).get("SPECMINVALUE").toString().equals(""))
												{
													sminValueString="-9999999999";
												}else
												{
													sminValueString=lsAnalysisData.get(i).get("SPECMINVALUE").toString();
												}
												if(lsAnalysisData.get(i).get("SPECMAXVALUE").toString().equals(""))
												{
													smaxValueString="9999999999";
												}else {									
													smaxValueString=lsAnalysisData.get(i).get("SPECMAXVALUE").toString()	;
												}
												
												if (Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())>=Double.parseDouble(sminValueString)&&
														Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())<=Double.parseDouble(smaxValueString)	)
												{
													hs.put("FINALVALUE", "OK");
												}else
												{
													hs.put("FINALVALUE", "NG");
												}
//												if (Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())>=Double.parseDouble(lsAnalysisData.get(i).get("SPECMINVALUE").toString())&&
//														Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())<=Double.parseDouble(lsAnalysisData.get(i).get("SPECMAXVALUE").toString())	)
//												{
//													hs.put("FINALVALUE", "OK");
//												}else
//												{
//													hs.put("FINALVALUE", "NG");
//												}
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
										hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
										hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
									}
								}					
	
								lsAnalysisTable.add(hs);
							}
							adapter.notifyDataSetChanged();
							
							//报工开始
							String sSetStepInResule = db.SetStepInbyCRM(msProductOrderId ,msProductCompId,
									msStepId, msEqpId, MESCommon.UserId, MESCommon.UserName);
							if (!sSetStepInResule.equals("")) {
								MESCommon.showMessage(WIP_TrackIn_CRM.this, "MES单号【"+txtInput.getText().toString().trim()+"】报工开始失败！" + sSetStepInResule);							
							    return false;
							}
							
							//初始化工件信息
							editCrmno.setText(lsCompID.get(0).get("CRMNO").toString());
							editMaterialID.setText(lsProduct.get(0).get("PRODUCTID").toString());
							//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
							editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());
							//服务特殊资讯
							editCustomRequest.setText(lsCRM_HK_FW006.get(0).get("CUSTOMREQUEST").toString());
							editDisassembleMemo.setText(lsCRM_HK_FW006.get(0).get("DISASSEMBLEMEMO").toString());
							for (int i=0; i<lsResponsibility.size(); i++)
							{
								String sTemp = lsResponsibility.get(i).get("ITEM").toString();
								if (sTemp.equals(lsCRM_HK_FW006.get(0).get("RESPONSIBILITY").toString()))
								{
									spResponsibility.setSelection(i+1);
									break;
								}
							}
							//故障类别
							lsTroubleTypeResult.clear();
							sSql = "SELECT * FROM CRM_HK_FW006_P WHERE FWTYPE='故障类别' AND CRMNO='" + msCrmno + "'";
							sError = db.GetData(sSql, lsTroubleTypeResult);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
								return false;
							}
							for (int i=0; i<lsTroubleTypeResult.size(); i++)
							{
								for (int j=0; j<lsTroubleType.size(); j++)
								{
									if (lsTroubleTypeResult.get(i).get("FWITEM").toString().equals(lsTroubleType.get(j).get("ITEM").toString()))
									{
										lsTroubleType.get(j).put("CHECKFLAG","Y"); 
									}
								}
							}
							adapterTab2_1.notifyDataSetChanged();
							//泄漏位置
							lsPosition.clear();
							sSql = "SELECT 'N' CHECKFLAG, A.PHRASEVALUE ITEM FROM MPHRASE A WHERE A.PHRASETYPE='FW_POSITION' AND A.PHRASEID='" + sFWType + "' ORDER BY CAST(A.SEQ AS INT)";
							sResult = db.GetData(sSql, lsPosition);
							if (sResult != "") {
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
								finish();
							}
							adapterTab2_2 = new wiptrackincrmAdapterTab2_2(lsPosition, WIP_TrackIn_CRM.this);
							lv2_2.setAdapter(adapterTab2_2);
							lsPositionResult.clear();
							sSql = "SELECT * FROM CRM_HK_FW006_P WHERE FWTYPE='泄漏位置' AND CRMNO='" + msCrmno + "'";
							sError = db.GetData(sSql, lsPositionResult);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
								return false;
							}
							for (int i=0; i<lsPositionResult.size(); i++)
							{
								for (int j=0; j<lsPosition.size(); j++)
								{
									if (lsPositionResult.get(i).get("FWITEM").toString().equals(lsPosition.get(j).get("ITEM").toString()))
									{
										lsPosition.get(j).put("CHECKFLAG","Y"); 
									}
								}
							}
				            adapterTab2_2.notifyDataSetChanged();
							
							GetProcessSTEP_P();//初始零部件
							editInput.setText("");
							setFocus(editCrmno);
						}
						//刷零部件
						else
						{	
							//服务组装站、服务返修站才能增删零部件
							if (!msStepId.equals("服务组装站") && !msStepId.equals("服务返修站"))
							{
								MESCommon.show(WIP_TrackIn_CRM.this, "服务组装站、服务返修站才能增加、删除零部件!");
							    return false;
							}
														
							lsCompID.clear();
							String sScanId = txtInput.getText().toString().trim().toUpperCase();
							sResult = db.GetProductSerialNumber_CRM(sScanId,"","" , "QF","零部件", "装配",lsCompID);
							if(lsCompID.size()<=0)
							{   
									MESCommon.show(WIP_TrackIn_CRM.this, "物料条码【" + sScanId + "】， 不存在,是否需手动添加!");
								    return false;
							}else
							{  
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
								
								String sCheckResult="";
								if(lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("Y"))
								{
									MESCommon.show(WIP_TrackIn_CRM.this,"装配零部件时,不能装配条码为：【"+sScanId+"】的制造号码！");
									return false;
								}
								for(int i=0;i<lsCompTable.size();i++)
								{
									if(sScanId.equals(lsCompTable.get(i).get("SEQ").toString()))
									{
										MESCommon.show(WIP_TrackIn_CRM.this, "物料条码[" + lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString() + "] 已在物料清单中!");
									    return false;
									}								
									
								}
								//是否特采检查
								Check_SpecialAdoption(sSEQ);
								if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
								{
									sCheckResult=checkExist(sScanId);
								}
								if(!sCheckResult.equals(""))
								{
									
									AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn_CRM.this).setTitle("确认").setMessage(sCheckResult+",是否继续加入!")
											.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
							            @Override  
							            public void onClick(DialogInterface dialog,int which) {  
							                // TODO Auto-generated method stub  
							    
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
							            	exeLstCompTable(   sSEQ ,   sMaterialId ,   sMaterialMame ,	   sMaterialType ,   sTracetype,   sLotID,   sRAWPROCESSID,	   sFINEPROCESSID,   sSUPPLYID,	   sLNO ,"Y");
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
									exeLstCompTable(   sSEQ ,   sMaterialId ,   sMaterialMame ,	   sMaterialType ,   sTracetype,   sLotID,   sRAWPROCESSID,	   sFINEPROCESSID,   sSUPPLYID,	   sLNO,"" );
								}
							}
	
						}

					} catch (Exception e) {
						MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
						return false;
					}
				}
					
				return false;
			}
		});
	
		editCrmno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
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
					String sResult="合格";
					
					if (!mbUserLogin)
					{
						MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
						return;
					}
					//基本检查
					if (editCrmno.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_TrackIn_CRM.this, "请先扫描条码在进行报工！");
						return;
					}
					//报工暂停不能过站
					if (mbIsStepPause)
					{
						MESCommon.show(WIP_TrackIn_CRM.this, "MES单号：" + msCrmno + "\n为「报工暂停」状态，不能执行报工完成！");
						return;
					}
					//专配检查
					String sCheckMsg = Check_HZ_SPECIALMATCH();
					if(!sCheckMsg.equals(""))
					{
						MESCommon.show(WIP_TrackIn_CRM.this, sCheckMsg);
						return;
					}
					//检查自主检资料，确认是否合格
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{
								MESCommon.show(WIP_TrackIn_CRM.this, "请输入["+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"]检验结果");
								btnTab2.performClick();
								return;
							}
						}
						if(lsAnalysisTable.get(i).get("FINALVALUE").toString().trim().equals("NG"))
						{
							sResult="不合格";
							break;
						}
						//若齿轮比（不为空白，且!=0.0，则主齿轮齿数、副齿轮齿数必填）
						String sItem = lsAnalysisTable.get(i).get("ANALYSISITEM").toString().trim();
						String sValue = lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim();
						double dValue = 0;
						
						if(sItem.equals("齿轮比"))
						{
							try {
								dValue = Double.parseDouble(sValue);
							} catch (Exception e1) {
								dValue = 0;
							}
							if (dValue>0)
							{
								//检查主齿轮齿数、副齿轮齿数是否有值
								for(int j=0;j<lsAnalysisTable.size();j++)
								{
									String sItem1 = lsAnalysisTable.get(j).get("ANALYSISITEM").toString().trim();
									String sValue1 = lsAnalysisTable.get(j).get("DISPLAYVALUE").toString().trim();
									if ((sItem1.equals("主齿轮齿数") || sItem1.equals("副齿轮齿数")) && sValue1.equals(""))
									{
										MESCommon.show(WIP_TrackIn_CRM.this, "请输入["+sItem1+"]");
										btnTab2.performClick();
										return;
									}
								}
							}
						}
						
					}
				  
//					String sMainTypelist="";
//					for(int i=0;i< lsCompTable.size();i++)
//					{
//						sMainTypelist=sMainTypelist+lsCompTable.get(i).get("MaterialType").toString()+",";
//					}
//			 	    String sMessage= db.CheckMaterial(sMainTypelist,msStepId,msProductId, editCrmno.getText().toString());
//			 	   if (!sMessage.equals("") ) {
//						MESCommon.show(WIP_TrackIn_CRM.this, sMessage);
//						return;
//					}
			 	  
			 	   	if(sResult.equals("不合格"))
					{
						//不合格则停在当站，然后怎么继续？
			 	   		AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn_CRM.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("不合格", lsAnalysisTable, lsAnalysisData);
				            	//执行最终判定(自主检查的最终判定)
					            db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
								Toast.makeText(WIP_TrackIn_CRM.this, "报工完成!", Toast.LENGTH_SHORT).show();
				                  
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
			 	   		//合格则过站：SetStepOutbyJWJ (MES_API)
			 	   		Save(sResult, lsAnalysisTable, lsAnalysisData);
						//执行最终判定(自主检查的最终判定)
			 	   		db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
				 	   	String sSetStepOutResule=db.SetStepOutbyCRM(msProductOrderId, msProductCompId, msStepId, msEqpId,MESCommon.UserId, MESCommon.UserName,"");	
		 	   			if (!sSetStepOutResule.equals("")) 
		 	   			{
		 	   				MESCommon.show(WIP_TrackIn_CRM.this, "报工完成失败！" + sSetStepOutResule);
		 	   				return;
		 	   			}
			 	   		Toast.makeText(WIP_TrackIn_CRM.this, "报工完成!", Toast.LENGTH_SHORT).show();                  
			 	   		Clear();
					}
			 	   
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});

		// 暂存
		Button btnTemporary = (Button) findViewById(R.id.wiptrackincrm_btnTemporary);
		btnTemporary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!mbUserLogin)
					{
						MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
						return;
					}
					if (editCrmno.getText().toString().trim().equals("") ) {
						MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先扫描条码在进行报工暂存！");
						return;
					}
					//专配检查
					String sCheckMsg = Check_HZ_SPECIALMATCH();
					if(!sCheckMsg.equals(""))
					{
						MESCommon.show(WIP_TrackIn_CRM.this, sCheckMsg);
						return;
					}
					Save("", lsAnalysisTable, lsAnalysisData);
				
				    Toast.makeText(WIP_TrackIn_CRM.this, "暂存成功!", Toast.LENGTH_SHORT).show();
				    Clear();

				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}			
			}
		});
	
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!mbUserLogin)
					{
						MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
						return;
					}
					if(editCrmno.getText().toString().equals(""))
					{
						MESCommon.show(WIP_TrackIn_CRM.this, "请先扫描制造号码,在进行零部件添加！");
						return;
					}
					//服务组装站、服务返修站才能增删零部件
					if (!msStepId.equals("服务组装站") && !msStepId.equals("服务返修站"))
					{
						MESCommon.show(WIP_TrackIn_CRM.this, "服务组装站、服务返修站才能增加、删除零部件!");
					    return;
					}
					openNewActivity2(v);						
									
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (!mbUserLogin)
					{
						MESCommon.showMessage(WIP_TrackIn_CRM.this, "请先进行人员设备报工！");
						return;
					}
					if (lsCompTable.size() > 0) {
						
						//服务组装站、服务返修站才能增删零部件
						if (!msStepId.equals("服务组装站") && !msStepId.equals("服务返修站"))
						{
							MESCommon.show(WIP_TrackIn_CRM.this, "服务组装站、服务返修站才能增加、删除零部件!");
						    return;
						}
						
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
							MESCommon.show(WIP_TrackIn_CRM.this, "请选择要删除的零部件");
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
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
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
							Intent iNew = new Intent(WIP_TrackIn_CRM.this, WIP_TrackIn_SP.class);
							iNew.putExtra("COMPID", lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString());
							iNew.putExtra("COMPIDSEQ", lsCompTable.get(i).get("SEQ").toString());
							startActivity(iNew);
						}
					}
					if(!isSelect)
					{
						MESCommon.show(WIP_TrackIn_CRM.this, "请选择要查看的零部件");
						return;
					}
									
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
				}
			}
		});
	
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
	     				
	     				MESCommon.show(WIP_TrackIn_CRM.this, "物料条码[" + myClass.PRODUCTSERIALNUMBER + "] 已在物料清单中!");
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
				setFocus(editCrmno);
				editInput.setText("");	
         }  
	 }  
	}  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wip__track_in__crm, menu);
		mMenuItem= menu.findItem(R.id.menu_wiptrackincrm_Pause);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.menu_wiptrackincrm_Pause) {
//			 Intent intent = new Intent();  
//			 intent.setClass(this.getApplicationContext(),WIP_StopTrackout .class);  
//			 startActivityForResult(intent, 1);  
//			return true;
			
			//基本检查
			if (editCrmno.getText().toString().trim().equals("") ) {
				MESCommon.show(WIP_TrackIn_CRM.this, "请先扫描条码在进行报工作业！");
				return true;
			}
			
 	   		AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn_CRM.this).setTitle("确认").setMessage("是否要「" + mMenuItem.getTitle() + "」")
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
	            @Override  
	            public void onClick(DialogInterface dialog,int which) {  
	            	if (!mbIsStepPause)
	    			{
	    				String sResult = db.SetStepPause(msCrmno, msProductCompId, msStepId, msStepSEQ, msEqpId, MESCommon.UserId, MESCommon.UserName);
	    				if (sResult.equals(""))
	    				{
	    					mMenuItem.setTitle("解除暂停");
	    					mbIsStepPause=true;
	    				}
	    			}
	    			else
	    			{
	    				String sResult = db.SetStepResume(msCrmno, msProductCompId, msStepId, msStepSEQ, msEqpId, MESCommon.UserId, MESCommon.UserName);
	    				if (sResult.equals(""))
	    				{
	    					mMenuItem.setTitle("报工暂停");
	    					mbIsStepPause=false;
	    				}
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

	public static class wiptrackincrmAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackincrmAdapter(List<HashMap<String, String>> items,
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
	
	public static class wiptrackincrmAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackincrmAdapterTab0(List<HashMap<String, String>> items,
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
			wiptrackincrmAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvISSP;
			TextView tvSerialnumberId;
			TextView tvMaterialMame;
			TextView tvMaterialID;
		}
	}

	public static class wiptrackincrmAdapterTab2_1 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackincrmAdapterTab2_1(List<HashMap<String, String>> items,
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
				convertView = inflater.inflate(R.layout.activity_wip_track_in_crm_tab2_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackincrmlv2_cb);
				holder.tvItem = (TextView) convertView.findViewById(R.id.wiptrackincrmlv2_tvItem);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvItem.setText(getItem(position).get("ITEM").toString());

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
			wiptrackincrmAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvItem;
		}
	}

	public static class wiptrackincrmAdapterTab2_2 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackincrmAdapterTab2_2(List<HashMap<String, String>> items,
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
				convertView = inflater.inflate(R.layout.activity_wip_track_in_crm_tab2_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackincrmlv2_cb);
				holder.tvItem = (TextView) convertView.findViewById(R.id.wiptrackincrmlv2_tvItem);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvItem.setText(getItem(position).get("ITEM").toString());

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
			wiptrackincrmAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvItem;
		}
	}

	private  void exeLstCompTable(String   sSEQ ,String   sMaterialId ,String   sMaterialMame ,	String   sMaterialType ,String   sTracetype,String   sLotID,String   sRAWPROCESSID,	String   sFINEPROCESSID,String   sSUPPLYID,	String   sLNO,String sRepeated )
	{
		try {		
			
			String sSQL = "", sResult = "";
			
			//BOM确认
			lsBom.clear();
			List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
			sSQL = "SELECT ISCHECKERPBOM,PRODUCTORDERID FROM CRM_HK_FW006 WHERE CRMNO='" + msCrmno + "'";
			sResult = db.GetData(sSQL, lscheckERPBOMSystem);
			if(lscheckERPBOMSystem.get(0).get("ISCHECKERPBOM").toString().equals("Y"))
			{
				String sProductOrderId = lscheckERPBOMSystem.get(0).get("PRODUCTORDERID").toString();	//这里要抓CRM_HK_FW006
				
				if (sProductOrderId.equals(""))
				{
					MESCommon.show(WIP_TrackIn_CRM.this, "MES单号尚未输入制令编号，不能增加零部件!");
					editInput.setText("");
					setFocus(editInput);				
					return;
				}
		        sSQL = "SELECT * FROM ERP_MBOM WHERE PRODUCTID ='" +msProductId + "' AND PRODUCTORDERID = '" + sProductOrderId + "' AND MATERIALID = '" + sMaterialId + "'";
		        sResult = db.GetData(sSQL, lsBom);
				if (sResult != "") {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, sResult);
					editInput.setText("");
					setFocus(editInput);				
					return;
				} 
			
				if(lsBom.size()==0)
				{
					MESCommon.show(WIP_TrackIn_CRM.this, "物料条码【" +editInput.getText().toString() + "】的物料【"+sMaterialId+"】， 不在制令【"+msProductOrderId+"】的物料清单中!");
					editInput.setText("");
					setFocus(editInput);				
					return;
				}
			}
			//取得提示的零部件编号
			String sNewNumber="";
			HashMap<String, String> hs = new HashMap<String, String>();	
			hs.put("MaterialId",sMaterialId);
			hs.put("MaterialMame", sMaterialMame);
			hs.put("SEQ", sSEQ);
			if (!sFINEPROCESSID.equals("")) {
				hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);
				sNewNumber=sFINEPROCESSID;
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
			hs.put("ISCOMPREPEATED", sRepeated);			
			hs.put("ISCOMPEXIST", "Y");
			hs.put("TRACETYPE", sTracetype);
			hs.put("LOTID", sLotID);
			hs.put("RAWPROCESSID", sRAWPROCESSID);
			hs.put("FINEPROCESSID", sFINEPROCESSID);
			hs.put("SUPPLYID", sSUPPLYID);
			hs.put("FURNACENO", sLNO);
			hs.put("IS_INSERT", "Y");
			hs.put("ISSP", msSpecialAdoption);
			lsCompTable.add(0,  hs);							
			adapterTab0.notifyDataSetChanged();	
			Toast.makeText(WIP_TrackIn_CRM.this, "零部件：【"+sNewNumber+"】,加入成功！", Toast.LENGTH_SHORT).show();	
			editInput.setText("");
			setFocus(editCrmno);
			
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
		}
	}
	
	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wiptrackincrm_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackincrm_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackincrm_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wiptrackincrm_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wiptrackincrm_tab2_1);

				
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
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
		}
	}
	
	//功能：初始化装配零部件明细「PROCESS_STEP_P装配零部件明细」
	private  void GetProcessSTEP_P()
	{
		try {			
			lsProcessStep_p.clear();
			String  sSql=" SELECT DISTINCT STEPID,  PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,PRODUCTSERIALNUMBER,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,ISCOMPEXIST ,ISCOMPREPEATED  FROM PROCESS_STEP_P WHERE PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+msProductCompId+"' ORDER BY  CAST ( STEPSEQ AS INT) DESC ;";
			String sError= db.GetData(sSql,  lsProcessStep_p);
			 if (sError != "") {
					MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
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
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
		}
	}
	
	//功能：储存「PROCESS_STEP_P装配零部件明细」
	private  String InsertSTEP_P( String stype)
	{
		String sResult="";
		try {
			if(lsCompTable.size()>0)
			{   //editProductID.getText().toString(),msStepId, 
				String  sSql=" DELETE FROM PROCESS_STEP_P WHERE PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+msProductCompId+"' AND STEPID ='"+msStepId+"' ;";
				sSql=sSql + " DELETE FROM PROCESS_STEP_NOBARCODE WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+msProductCompId+"' AND STEPID ='"+msStepId+"' ;";
				
				for(int i=0;i<lsCompTable.size();i++)
			    {
				    if(!lsCompTable.get(i).get("IS_INSERT").toString().equals("N"))
				    {
				    	
			    	sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER_OLD,PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,ISCOMPEXIST,ISCOMPREPEATED,PROCESS_PRODUCTIDTYPE, ISCHIEFCOMP, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
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
                            + "" + "','"
                            + "N" + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
			    	
				    	if (!stype.equals("")) //暂存时不更新
				    	{
					    	//,更新PROCESS_STEP_PF 级次组立的检验项目相关资料
					    	List<HashMap<String, String>> lsdtStep_PF = new ArrayList<HashMap<String, String>>();
							String sSQL = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P ='"+lsCompTable.get(i).get("SEQ").toString()+"' ";
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
				            if (lsCompTable.get(i).get("ISCOMPREPEATED").toString().equals("Y")) {
				      	    	
						    	 sSql = sSql + " UPDATE  PROCESS_STEP_P SET ISCOMPREPEATED='Y'  WHERE PRODUCTSERIALNUMBER='" + lsCompTable.get(i).get("SEQ").toString()  + "'   ;";
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
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
			return sResult =e.toString();
		}
		return sResult;
	}

	//功能：储存「自主检验」及「零部件明细」
	private void Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
		
			 List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		 
             String  sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
             String   sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
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
         	      MESCommon.showMessage(WIP_TrackIn_CRM.this,sMessage); 
         	      return;
         	     }
             }
       	     String sMessage= InsertSTEP_P(sResult);//插入零部件
       	     if(!sMessage.equals(""))
       	     {
       	      MESCommon.showMessage(WIP_TrackIn_CRM.this,sMessage); 
       	      return;
       	     }
       	     //储存服务特殊资讯
       	     SpinnerData oResponsibility = (SpinnerData) spResponsibility.getSelectedItem();
       	     sSQL = "UPDATE CRM_HK_FW006 SET CUSTOMREQUEST='" + editCustomRequest.getText().toString() + "', DISASSEMBLEMEMO='" + editDisassembleMemo.getText().toString() + "', RESPONSIBILITY='" + oResponsibility.value + "'," + 
       	     		" MODIFYUSERID='" +MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=" + MESCommon.ModifyTime +
       	     		" WHERE CRMNO='" + msCrmno + "';";
       	     
       	     //故障类别
       	     if (lv2_1.isEnabled())
       	     {
       	    	 sSQL += "DELETE FROM CRM_HK_FW006_P WHERE CRMNO='" + msCrmno + "' AND FWTYPE='故障类别';";
       	    	 for (int i=0; i<lsTroubleType.size(); i++)
       	    	 {
       	    		 if (lsTroubleType.get(i).get("CHECKFLAG").equals("Y"))
       	    		 {
       	    			 String sTrouble = lsTroubleType.get(i).get("ITEM").toString();
	       	    		 sSQL += "INSERT INTO CRM_HK_FW006_P(SYSID,CRMNO,PRODUCTCOMPID,FWTYPE,FWITEM,MODIFYUSERID,MODIFYUSER,MODIFYTIME) VALUES(" +
	    				 		"" + MESCommon.SysId + "," +
	    				 		"'" + msCrmno + "'," +
	    				 		"'" + msProductCompId + "'," +
	    				 		"'" + "故障类别" + "'," +
	    				 		"'" + sTrouble + "'," +
	    				 		"'" + MESCommon.UserId + "'," +
	    				 		"'" + MESCommon.UserName + "'," +
	    				 		"" + MESCommon.ModifyTime + ");";
       	    		 }
       	    	 }
       	     }
       	     //泄漏位置
       	     if (lv2_2.isEnabled())
       	     {
       	    	 sSQL += "DELETE FROM CRM_HK_FW006_P WHERE CRMNO='" + msCrmno + "' AND FWTYPE='泄漏位置';";
       	    	 for (int i=0; i<lsPosition.size(); i++)
       	    	 {
       	    		 if (lsPosition.get(i).get("CHECKFLAG").equals("Y"))
       	    		 {
       	    			 String sPosition = lsPosition.get(i).get("ITEM").toString();
	       	    		 sSQL += "INSERT INTO CRM_HK_FW006_P(SYSID,CRMNO,PRODUCTCOMPID,FWTYPE,FWITEM,MODIFYUSERID,MODIFYUSER,MODIFYTIME) VALUES(" +
	    				 		"" + MESCommon.SysId + "," +
	    				 		"'" + msCrmno + "'," +
	    				 		"'" + msProductCompId + "'," +
	    				 		"'" + "泄漏位置" + "'," +
	    				 		"'" + sPosition + "'," +
	    				 		"'" + MESCommon.UserId + "'," +
	    				 		"'" + MESCommon.UserName + "'," +
	    				 		"" + MESCommon.ModifyTime + ");";
       	    		 }
       	    	 }
       	     }
       	     
       	     sMessage=db.ExecuteSQL(sSQL);
	  	     if(!sMessage.equals(""))
	  	     {
	  	    	 MESCommon.showMessage(WIP_TrackIn_CRM.this,sMessage); 
	  	    	 return;
	  	     }
       	
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
		}
	}
	
	public void Clear() {
		try {
//			List<SpinnerData> lst = new ArrayList<SpinnerData>();
//			ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,	android.R.layout.simple_spinner_item, lst);
//			Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//			spBid.setAdapter(Adapter);
			
//			SpinnerData sResponsibility = (SpinnerData) spResponsibility.getSelectedItem();
//			sResponsibility.
			
			spResponsibility.setSelection(0);
			editCrmno.setText("");
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
            lsBid.clear(); lsMid.clear(); lsSid.clear(); lsSidTable.clear();lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();lsProductCUS.clear();lsDefect.clear();
            adapterTab0.notifyDataSetChanged();
            
            editCustomRequest.setText("");
            editDisassembleMemo.setText("");
            //将lsTroubleType, lsPosition设成未设定
            for (int i=0; i<lsTroubleType.size(); i++)
            {
            	lsTroubleType.get(i).put("CHECKFLAG","N");
            }
            for (int i=0; i<lsPosition.size(); i++)
            {
            	lsPosition.get(i).put("CHECKFLAG","N");
            }
            
            adapterTab2_1.notifyDataSetChanged();
            adapterTab2_2.notifyDataSetChanged();
            
            btnTab1.performClick();
            setFocus(editInput);
            editInput.setText("");
            
            mMenuItem.setTitle("报工暂停");
            mbIsStepPause = false;
            
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
		}

	}
	
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e)
		{

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
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
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
				MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
				return sResult=sError;
			 }
			 if(lscheckExist.size()>0)
			 {
				 return sResult="扫描条码【"+number+"】,已经被【"+lscheckExist.get(0).get("PRODUCTSERIALNUMBER_OLD").toString()+"】次组立装配过";
			 }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
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
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
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
				MESCommon.showMessage(WIP_TrackIn_CRM.this, "工件编号：" + sCompid + " 为特采品！");
				return;
			}
			
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_CRM.this, e.toString());
			return;
		}
	}
	
}

