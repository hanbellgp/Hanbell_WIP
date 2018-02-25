package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.WIP_PaintingEnd.SpinnerData;
import com.example.hanbell_wip.WIP_PaintingEnd.WIPPaintingEndAdapter.ViewHolder;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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

public class WIP_TrackIn_Pre extends Activity {

	private MESDB db = new MESDB();

	ListView lv0,lv1,lv2;
	Button btnConfirm, btnExit,btnAdd, btnTemporary,btnDelete,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1;
	Spinner spBid;
	EditText editInput,editMaterialID,editProductModel;
	TextView  h1, h2, h3,h4, h5, h6,h7,h8,h9,h10,h11;
	LinearLayout tab1, tab2, tab3;
	wiptrackinpreAdapter adapter;
	wiptrackinpreAdapterTab2 adapterTab2;
	wiptrackinpreAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",msProductOrderIdold="",  msProductId="",  msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID="",msSampletimes,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE,msMMT="";

	// 该物料的HashMap记录
	static int milv0RowNum = 0;int milv1RowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisTable = new ArrayList<HashMap<String, String>>();

	private List<HashMap<String, String>> lsAnalysisData = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBid = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsMid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSidTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsodtBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_pf = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProductOrder = new ArrayList<HashMap<String, String>>();
	
	private List<HashMap<String, String>> lsProductOrderTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSysid = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsMMT = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBOM = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsOrder = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsExistcheck=new ArrayList<HashMap<String, String>>();
	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_trackin_pre);
try{
		// 取得控件
		spBid = (Spinner) findViewById(R.id.wiptrackinpre_spBid);	
	
	
		editInput = (EditText) findViewById(R.id.wiptrackinpre_tvInput);		
		editMaterialID = (EditText) findViewById(R.id.wiptrackinpre_tvMaterialid);	
		editProductModel = (EditText) findViewById(R.id.wiptrackinpre_tvProductModel);	
		tab1 = (LinearLayout) findViewById(R.id.wiptrackinpre_tab1);
		tab2 = (LinearLayout) findViewById(R.id.wiptrackinpre_tab2);
		tab3 = (LinearLayout) findViewById(R.id.wiptrackinpre_tab3);
		h1 = (TextView) findViewById(R.id.wiptrackinpre_h1);
		h2 = (TextView) findViewById(R.id.wiptrackinpre_h2);
		h3 = (TextView) findViewById(R.id.wiptrackinpre_h3);
		h4 = (TextView) findViewById(R.id.wiptrackinpre_h4);
		h5 = (TextView) findViewById(R.id.wiptrackinpre_h5);
		h6 = (TextView) findViewById(R.id.wiptrackinpre_h6);
		h7= (TextView) findViewById(R.id.wiptrackinpre_h7);
		h8= (TextView) findViewById(R.id.wiptrackinpre_h8);
		h9= (TextView) findViewById(R.id.wiptrackinpre_h9);
		h10= (TextView) findViewById(R.id.wiptrackinpre_h10);
		h11= (TextView) findViewById(R.id.wiptrackinpre_h11);
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
		h11.setBackgroundColor(Color.DKGRAY);
		lv0 = (ListView) findViewById(R.id.wiptrackinpre_lv0);
		lv1 = (ListView) findViewById(R.id.wiptrackinpre_lv1);
		lv2 = (ListView) findViewById(R.id.wiptrackinpre_lv2);
		btnConfirm = (Button) findViewById(R.id.wiptrackinpre_btnConfirm);
		btnExit = (Button) findViewById(R.id.wiptrackinpre_btnExit);	
		btnAdd = (Button) findViewById(R.id.wiptrackinpre_btnAdd);	
		btnDelete=(Button) findViewById(R.id.wiptrackinpre_btnTab0_Delete);
		btnTab1 = (Button) findViewById(R.id.wiptrackinpre_btnTab1);
		btnTab2 = (Button) findViewById(R.id.wiptrackinpre_btnTab2);
		btnTab3 = (Button) findViewById(R.id.wiptrackinpre_btnTab3);		
		btnTab2_0 = (Button) findViewById(R.id.wiptrackinpre_btnTab2_0_OK);
		btnTab2_1 = (Button) findViewById(R.id.wiptrackinpre_btnTab2_1_OK);
		adapterTab0 = new wiptrackinpreAdapterTab0(lsCompTable, this);
		lv0.setAdapter(adapterTab0);
		adapter = new wiptrackinpreAdapter(lsAnalysisTable, this);
		lv1.setAdapter(adapter);		
		adapterTab2 = new wiptrackinpreAdapterTab2(lsSidTable, this);
		lv2.setAdapter(adapterTab2);
		
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
			MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
			finish();
		}
	
		if ( lsuser.size()==0) {
			MESCommon.showMessage(WIP_TrackIn_Pre.this, "请先进行人员设备报工！");
		
		}
//		// 读取生产制令
//		sSql = "SELECT   DISTINCT   A.PRODUCTORDERID ,A.PRODUCTID  FROM PROCESS_PRE A INNER JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID AND B.PRODUCTTYPE='"+params.get("ProductType").toString()+"' WHERE A.PROCESSSTATE  <> '已完成'";
//		sResult = db.GetData(sSql, lsProductOrder);
//		if (sResult != "") {
//			MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
//			finish();
//		}
//		ArrayList<SpinnerData>	lst = new ArrayList<SpinnerData>();
//		for (int i = 0; i < lsProductOrder.size(); i++) {
//			SpinnerData c = new SpinnerData(lsProductOrder.get(i).get("PRODUCTID").toString(), lsProductOrder.get(i).get("PRODUCTORDERID").toString());
//			lst.add(c);
//		}

//		ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,	android.R.layout.simple_spinner_item, lst);
//		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spProductOrder.setAdapter(Adapter);
		
		 sSql = " SELECT MMT  FROM MSTEP_MMT  WHERE STEPID ='"+params.get("StepID")+"' ";
		 sResult = db.GetData(sSql, lsMMT);
		 if(lsMMT.size()==0)
		 {
			 MESCommon.show(WIP_TrackIn_Pre.this, "当前工站不为次组立工站，请调整后再刷入工件号码！");
				
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
				}
			}
		});
		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wiptrackinpre_editTab2_0);
					if(lsAnalysisTable.size()>0)
					{ lsAnalysisTable.get(milv1RowNum).put("DATAVALUE",edit.getText().toString());
					  lsAnalysisTable.get(milv1RowNum).put("DISPLAYVALUE",edit.getText().toString());
						
						if(lsAnalysisTable.get(milv1RowNum).get("ISJUDGE").toString().trim().equals("Y"))
						{
							if(!edit.getText().toString().trim().equals(""))
							{
							 	if (Double.parseDouble(edit.getText().toString().trim())>=Double.parseDouble(lsAnalysisTable.get(milv1RowNum).get("SPECMINVALUE").toString())&&
										Double.parseDouble(edit.getText().toString().trim())<=Double.parseDouble(lsAnalysisTable.get(milv1RowNum).get("SPECMAXVALUE").toString())	)
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
					}
					hintKbTwo();
					adapter.notifyDataSetChanged();		
					showRow(milv1RowNum+1);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackinpre_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackinpre_radioButtonNG);
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
			try{
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
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
				MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinpreAdapterTab0.ViewHolder holder = (wiptrackinpreAdapterTab0.ViewHolder) arg1.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				wiptrackinpreAdapterTab0.getIsSelected().put(position,
						holder.cb.isChecked());
				milv0RowNum=position;
				if(holder.cb.isChecked())
				{
				lsCompTable	.get(position).put("CHECKFLAG","Y"); 
				}else
				{
				lsCompTable.get(position).put("CHECKFLAG","N"); 
				}
			}
		});
		// 控件事件
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinpreAdapter.ViewHolder holder = (wiptrackinpreAdapter.ViewHolder) arg1.getTag();
				showRow(arg2);
				}});

		
	
		// 控件事件
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinpreAdapterTab2.ViewHolder holder = (wiptrackinpreAdapterTab2.ViewHolder) arg1.getTag();
			}
		});
		
		
		 
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					
					// 查询交货单
					try {
						
						EditText txtInput = (EditText) findViewById(R.id.wiptrackinpre_tvInput);
						
						 if(lsMMT.size()==0)
						 {
							 MESCommon.show(WIP_TrackIn_Pre.this, "当前工站不为次组立工站，请调整后再刷入工件号码！");
							 setFocus(editInput)	;
							    return false;
						 }
						if(lsuser.size()==0)
						{   MESCommon.show(WIP_TrackIn_Pre.this, "请先进行设备人员报工!");
						setFocus(editInput)	;
						    return false;
						}
						if (txtInput.getText().toString().trim().length() == 0) {
							txtInput.setText("");
							MESCommon.show(WIP_TrackIn_Pre.this, "请扫描条码!");
							return false;
						}
						lsCompID.clear();
					
						String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","零部件","装配", lsCompID);			
						if (sResult != "") {
							MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
							setFocus(editInput)	;	
							return false;
						}	
						if(lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("Y"))
						{
						MESCommon.show(WIP_TrackIn_Pre.this,"次组立装配时,扫描条码不能为制造号码类型！");
						setFocus(editInput)	;	
							return false;
						}
						txtInput.setText(lsCompID.get(0).get("FINEPROCESSID").toString());
						msProductSerialNumber=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();					
						if(lsAnalysisTable.size()==0)
						{
							if(params.get("StepName") .contains("机体"))
							{
							     msProductId="35325-A00";
							}else {
								 msProductId="31330-A53111-SH";
							}					    	
							
						
						
							editMaterialID.setText("");
							editProductModel.setText("");	
							//次组站没有制令，制造号码，无法做ERPBOM查核
							//sResult	=GetBom(lsCompID.get(0).get("MATERIALID").toString());
							
	//						// 次组站没有制令，制造号码，无法读取成品料号，和机型。
	//						lsProductOrderTable.clear();
	//						String sSql = "SELECT DISTINCT A.PRODUCTID,A.PRODUCTNAME,A.PRODUCTMODEL  FROM MPRODUCT A  INNER JOIN PROCESS B ON A.PRODUCTID=B.PRODUCTID WHERE B.PRODUCTORDERID='"+sorder.value+"' AND A.PRODUCTID='"+sorder.text+"' " ;
	//						sResult = MESDB.GetData(sSql, lsProductOrderTable);
	//						if (sResult != "") {
	//							MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
	//							finish();
	//						}
	//						if(lsProductOrderTable.size()>0)
	//						{
	//							editMaterialID.setText(lsProductOrderTable.get(0).get("PRODUCTNAME").toString());
	//							editProductModel.setText(lsProductOrderTable.get(0).get("PRODUCTMODEL").toString());	
	//						}else
	//						{
	//							editMaterialID.setText("");
	//							editProductModel.setText("");	
	//						}
							msProductOrderId="";
							msProductCompId="";				
							msStepId=params.get("StepID").toString();	
							msEqpId=params.get("EQPID").toString();
						
						//初始化检验项目
						lsAnalysisData.clear();
						
						sResult = db.GetAnalysisDatabyProductSerialNumber(msProductOrderId,msProductId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
						if (sResult != "") {
							MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
							finish();
						}
				
						if (lsAnalysisTable.size()==0)
						{
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
							}else//除开数值，和文本
							{
								hs.put("FINALVALUE", "OK");
								hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
								hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
							}				
								
							lsAnalysisTable.add(hs);
							}
							adapter.notifyDataSetChanged();
							//根据产品等信息 读取装配规范
							lsBid.clear();
		 				    String sSql = "SELECT DISTINCT AS_BID, AS_INDEX ||'  '|| AS_BNAME AS AS_BNAME FROM ERP_ASSEMBLESPECIFICATION  WHERE PRODUCTID='" +msProductId + "' " +
										" AND   PRODUCTCOMPID='" +msProductCompId + "' AND PRODUCTORDERID='" + msProductOrderId + "' ORDER BY AS_INDEX ";
							sResult = db.GetData(sSql, lsBid);
							if (sResult != "") {
								MESCommon.showMessage(WIP_TrackIn_Pre.this, sResult);
								setFocus(editInput);
							}
							List<SpinnerData> lst = new ArrayList<SpinnerData>();
							SpinnerData c = new SpinnerData("预设", "预设");
							lst.add(c);
							for (int i = 0; i < lsBid.size(); i++) {
								c = new SpinnerData(lsBid.get(i).get("AS_BID").toString(), lsBid.get(i).get("AS_BNAME").toString());
								lst.add(c);
							}
			
							ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(WIP_TrackIn_Pre.this,	android.R.layout.simple_spinner_item, lst);
							Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spBid.setAdapter(Adapter);
							if(!sResult.equals(""))
							{
								MESCommon.show(WIP_TrackIn_Pre.this, sResult);
								return false;
							}
						  }
						}
						for(int i=0;i<lsCompTable.size();i++)
						{
							if(editInput.getText().toString().trim().equals(lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString()))
							{
								
								MESCommon.show(WIP_TrackIn_Pre.this, "当前物料[" + editInput.getText().toString().trim() + "] 已在物料清单中!");
								return false ;
							}		
						}
						if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
						{
							sResult= checkExist(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());	
						}
					
						if(!sResult.equals(""))
						{
							
							AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn_Pre.this).setTitle("确认").setMessage(sResult+",是否继续加入!")
									.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					                // TODO Auto-generated method stub  
					    
					            	InsertCompTable("Y");			
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
							InsertCompTable("N");			
						}
						editInput.setText("");
						setFocus(editInput)	;			

					} catch (Exception e) {
						MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
						setFocus(editMaterialID);
						return false;
					}
				}
					
				return false;
			}
		});
		editMaterialID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
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
					if(lsCompTable.size()==0){
						MESCommon.show(WIP_TrackIn_Pre.this, "请先扫描条码在进行报工！");
						return;
					}
					boolean bISCHIEF=false;
					
					 if(msStepId.equals("冷媒马达壳次组立站"))
					 {
						 for(int i=0;i<lsCompTable.size();i++)
						{
							if(lsCompTable.get(i).get("MaterialMame").trim().contains("定部"))
							{
								bISCHIEF=true;
								break;
							}
						}
							 
						 if(!bISCHIEF)
						{
							MESCommon.show(WIP_TrackIn_Pre.this, "没有装配【定部】，请添加后在保存！");
							return;
						}
					 }
					 
//					for(int i=0;i<lsCompTable.size();i++)
//					{
//						
//						if(lsCompTable.get(i).get("ISCHIEF").trim().equals("Y"))
//						{
//							bISCHIEF=true;
//							break;
//						}
//					}
//					if(!bISCHIEF)
//					{
//						MESCommon.show(WIP_TrackIn_Pre.this, "没有需要保留的工件！");
//						return;
//					}
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{		
								MESCommon.show(WIP_TrackIn_Pre.this, "请输入【"+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"】检验结果");
								btnTab2.performClick();
							
								return;
							}
						}						
					}
//					查核物料大类,对应是否装配完整！
//					String sMainTypelist="";
//					for(int i=0;i< lsCompTable.size();i++)
//					{
//						sMainTypelist=sMainTypelist+lsCompTable.get(i).get("MaterialType").toString()+",";
//					}
//			 	    String sMessage= db.CheckMaterial(sMainTypelist,msStepId,msProductId,msProductCompId);
//			 	   if (!sMessage.equals("") ) {
//						MESCommon.show(WIP_TrackIn_Pre.this, sMessage);
//						return;
//					}
			 	   if(sResult.equals("不合格"))
					{
						
						AlertDialog alert=	new AlertDialog.Builder(WIP_TrackIn_Pre.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("不合格", lsAnalysisTable, lsAnalysisData);
				            	//执行最终判定
					            db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
					           
								 Toast.makeText(WIP_TrackIn_Pre.this, "报工完成!", Toast.LENGTH_SHORT).show();
				                  
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
					
					} else
					{
						//插入零部件
					if (InsertSTEP_PF()) {
					 Save(sResult, lsAnalysisTable, lsAnalysisData);
						//执行最终判定
		         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");

	                  Toast.makeText(WIP_TrackIn_Pre.this, "报工完成!", Toast.LENGTH_SHORT).show();		                  
	                  Clear();
					}
					}		 	   
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
				}
			}
		});

		
		// 		
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {				
						openNewActivity2(v);						
									
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
				}
			}
		});
		
		// btnExit
		Button btnExit = (Button) findViewById(R.id.wiptrackinpre_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
							MESCommon.show(WIP_TrackIn_Pre.this, "请选择要删除的零部件");
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
					MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
				}
				
			}
		});
	
} catch (Exception e) {
	MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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
	         String sResult = "" ;  
             Boolean bfalg=true;
	         for (CompInformation myClass : arrayList) {  
	        	
	        		for(int i=0;i<lsCompTable.size();i++)
					{
						if(myClass.PRODUCTSERIALNUMBER.equals(lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString()))
						{
							
							MESCommon.show(WIP_TrackIn_Pre.this, "当前物料[" + myClass.PRODUCTSERIALNUMBER + "] 已在物料清单中!");
							bfalg=false;
							break;
						}		
					}
	        		if(bfalg)
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
						lsCompTable.add(0,  hs);	
	        		}
	         }  
	     	adapterTab0.notifyDataSetChanged();		
			setFocus(editInput);
			editInput.setText("");	
		 }  
			} catch (Exception e) {
				MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
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

	public static class wiptrackinpreAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;


		public wiptrackinpreAdapter(List<HashMap<String, String>> items,
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
		public void  setSelectitem(int selectitem)
        {
        	this.selectitem=selectitem;
        }
		private int selectitem=-1;
		@Override
 		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		public static class ViewHolder {
	
			TextView tvISNeed;
			TextView tvAnalySisItem;
			TextView tvValue;
			TextView tvType;
			TextView tvSPCMinValue;
			TextView tvSPCMaxValue;
			TextView tvFinalValue;	
			TextView tvISJudge;
		}
	
	}
	
	public static class wiptrackinpreAdapterTab2 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinpreAdapterTab2(List<HashMap<String, String>> items,
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

	public static class wiptrackinpreAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinpreAdapterTab0(List<HashMap<String, String>> items,
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
			
			if (convertView == null) {
			
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(R.layout.activity_wip_track_in_tab0_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackinlv0_cb);
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
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvMaterialMame.setText(getItem(position).get("MaterialMame").toString());	
			holder.tvMaterialID.setText(getItem(position).get("MaterialId").toString());	
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
			wiptrackinpreAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvSerialnumberId;
			TextView tvMaterialMame;
			TextView tvMaterialID;
		}
	}

	
	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wiptrackinpre_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wiptrackinpre_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wiptrackinpre_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wiptrackinpre_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wiptrackinpre_tab2_1);

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
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
		}
	}
	
	private void Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
			List<HashMap<String, String>> lswaitlist=new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		
			String sSQL = "SELECT * FROM ANALYSISWAITLIST where ANALYSISFORMSID='" + msAnalysisformsID + "'";
             String sError = db.GetData(sSQL, lswaitlist);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn_Pre.this, sError);
			 }
             sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
             sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_TrackIn_Pre.this, sError);
			 }
			 sSQL="";
             if (lsResule_M.size() > 0)
             {
                 sSQL = "UPDATE ANALYSISRESULT_M SET CHIEFANALYSISUSERID='" +MESCommon.UserId + "', CHIEFANALYSISUSER='" +MESCommon.UserName + "', CHIEFANALYSISTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ,ANALYSISJUDGEMENTRESULT='" + sResult + "',DATACOMPLETESTATUS='已完成',MODIFYUSERID='" + MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)  , TOREMENBER=''  WHERE   ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "' ;";
                
             }
             sSQL = sSQL+ " delete from ANALYSISRESULT_MD where ANALYSISFORMSID='" + msAnalysisformsID + "' and SAMPLETIMES='" + msSampletimes + "' ;";
         
             sSQL =sSQL+ " delete from ANALYSISRESULT_MDD where ANALYSISFORMSID='" + msAnalysisformsID + "' and ANALYSISTIMESINDEX='0' and SAMPLETIMES='" + msSampletimes + "' ;";
             db.ExecuteSQL(sSQL);
           
             sSQL="";
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
         		
         		 sSQL = "INSERT INTO ANALYSISRESULT_MD (ANALYSISFORMSID, SAMPLETIMES, ANALYSISMITEM, ANALYSISITEM, RESULTTYPE, JUDGEMENTSTANDARD, DATANUM, ISREQUIRED, ISJUDGEMENT, SAMPLINGNUM, ANALYSISUSERID, ANALYSISUSER, ANALYSISTIME, FAILURENUM, ANALYSISAVGVALUE, ANALYSISMAXVALUE, ANALYSISMINVALUE, MODIFYUSERID, MODIFYUSER, MODIFYTIME, SPECMINVALUE, SPECMAXVALUE, TARGETVALUE, OFFSETVALUE,OFFSETMINVALUE,OFFSETMAXVALUE,ISSHOWINIDCARD,ISCOVERSHOWINIDCARD)VALUES " +
         		 		"( " + "'" + msAnalysisformsID + "'," + "'" + msSampletimes + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISMITEM").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISITEM").toString().trim() + "'," + 
         				 "'" +listAnalysisTemp.get(i).get("RESULTTYPE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("ITEMSPEC").toString().trim()+ "'," + "'" + listAnalysisTemp.get(i).get("SAMPLESIZE").toString().trim() +
         		 		"'," + "'" + listAnalysisTemp.get(i).get("ISNEED").toString().trim() + "'," + "'" +listAnalysisTemp.get(i).get("ISJUDGE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("SAMPLINGQTY").toString().trim() + "',"
         				 + "'" +MESCommon.UserId+ "'," + "'" +MESCommon.UserName  + "', convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)," + "'"+sFailureNum+"'," + 
         		 		"'" + sAnalysisAvgvalue + "'," + "'" + sAnalysisMaxvalue + "'," + "'" +sAnalysisMinvalue + "'," + "'" +MESCommon.UserId + "'," + "'" + MESCommon.UserName + "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)," 
         		 		+ "'" + listAnalysisTemp.get(i).get("SPECMINVALUE").toString().trim() + "'," + 	"'" +listAnalysisTemp.get(i).get("SPECMAXVALUE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("TARGETVALUE").toString().trim() + "'," + 
         		 		"'" +listAnalysisTemp.get(i).get("OFFSETVALUE").toString().trim() + "'," + "'" + listAnalysisTemp.get(i).get("OFFSETMINVALUE").toString().trim() + "'," +
         		 		"'" + listAnalysisTemp.get(i).get("OFFSETMAXVALUE").toString().trim() + "','" + listAnalysisTemp.get(i).get("ISSHOWINIDCARD").toString().trim() + "','" + listAnalysisTemp.get(i).get("ISCOVERSHOWINIDCARD").toString().trim() + "') ;";
         		
         		 
         		sSQL = sSQL + "INSERT INTO ANALYSISRESULT_MDD (ANALYSISFORMSID,SAMPLETIMES, ANALYSISMITEM, ANALYSISITEM, ANALYSISTIMESINDEX, SAMPLINGNUMINDEX, DATAVALUE, MODIFYUSERID, MODIFYUSER, MODIFYTIME)VALUES " +
         				"( " + "'" + msAnalysisformsID + "'," + "'" + msSampletimes + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISMITEM").toString().trim()  + "'," + "'" + listAnalysisTemp.get(i).get("ANALYSISITEM").toString().trim() + "'," + "'0'," + 
         				"'1'," + "'" + listTemp.get(i).get("DATAVALUE").toString().trim() + "'," + "'" + MESCommon.UserId + "'," + "'" + MESCommon.UserName + "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) );";
         		 db.ExecuteSQL(sSQL);
			}
         	//执行最终判定
         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
         	 Toast.makeText(WIP_TrackIn_Pre.this, "报工完成!", Toast.LENGTH_SHORT).show();
             
            
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
		}
	}
	public String checkExist(String sSerialNumber)
	{
		String sResult="";
		String sSql="";
		try {
			lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER = '" + sSerialNumber + "' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "刷入条码：【" + sSerialNumber + "】,已经被装配使用";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')='' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "刷入条码：【" + sSerialNumber + "】,已经被次组立装配使用";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')!=''";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "刷入条码：【" + sSerialNumber + "】,已经被装配使用";
            }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
			return sResult=e.toString();
		}
		
	}
	
	private  String GetBom(String sMaterialid)
	{
		String sResult="";
		String sError="";
		String    sSql="";
		try {
			List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
			String sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
			sError = db.GetData(sSQL, lscheckERPBOMSystem);
			if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
			{
				lsBom.clear();
				sSQL = "SELECT * FROM ERP_MBOM WHERE PRODUCTID ='" +msProductId + "' AND PRODUCTORDERID = '" + msProductOrderId + "' AND MATERIALID = '" + sMaterialid + "'";
				sError = db.GetData(sSQL, lsBom);	
			
				 if (!sError.equals("")) 
				 {
					return 	sError;				 
				 }
				if(lsBom.size()==0){
					return sResult="当前物料【"+sMaterialid+"】不在ERPBOM物料清单中" ;}
			}
			return sResult ;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
			return	e.toString() ;
		}

	}
	private  Boolean InsertSTEP_PF()
	{
		try {
			if(lsCompTable.size()>0)
			{ 
				lsSysid.clear();
				String sSql = "SELECT " + MESCommon.SysId + " AS SYSID";
				String sresult = db.GetData(sSql, lsSysid);
			    sSql="";
			
			   
			    String sSEQ ="";
				String sProductSerialNumber="";
//				    for(int i=0;i<lsCompTable.size();i++)
//				    {
//				    	if(lsCompTable.get(i).get("ISCHIEF").equals("Y"))
//				    	{
//				    	   sSEQ=lsCompTable.get(i).get("SEQ").toString() ;
//				    	   sProductSerialNumber=lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString() ;
//				    	   break;
//				    	}
//				    }
				    
			    for(int i=0;i<lsCompTable.size();i++)
			    {
				
			    	sSql = sSql + "INSERT INTO PROCESS_STEP_PF (SYSID,ANALYSISFORMSID, PRODUCTORDERID, PRODUCTCOMPID, STEPID, EQPID, PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER,SERIALNUMBER_OLD_P, SERIALNUMBER_P, PROCESS_PRODUCTID, PROCESS_PRODUCTNAME, MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, ISCOMPEXIST,ISCOMPREPEATED,MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( '"
                            + lsSysid.get(0).get("SYSID").toString() + "','"
                            + msAnalysisformsID + "','"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msStepId+ "','"
                            + msEqpId+ "','"
                            + sProductSerialNumber + "','"
                            + sSEQ+ "','"
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
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                           + MESCommon.ModifyTime + ");";
			    	
			    	 if (lsCompTable.get(i).get("ISCOMPREPEATED").toString().equals("Y")) {
			      	    	
				    	 sSql = sSql + " UPDATE  PROCESS_STEP_PF SET ISCOMPREPEATED='Y'  WHERE SERIALNUMBER_P='" + lsCompTable.get(i).get("SEQ").toString()  + "'   ;";
				    	 sSql = sSql + " UPDATE  PROCESS_STEP_P SET ISCOMPREPEATED='Y'  WHERE PRODUCTSERIALNUMBER='" + lsCompTable.get(i).get("SEQ").toString()  + "'   ;";
			    	 }
			    	 
//			    	   	if(lsCompTable.get(i).get("ISCOMPEXIST").toString().equals("N"))
//				    	{
//				    		sSql = sSql + "INSERT INTO PROCESS_STEP_NOBARCODE (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
//		                            + MESCommon.SysId + ",'"
//		                            + msProductOrderId + "','"
//		                            + msProductCompId + "','"
//		                            + msProductId + "','"
//		                            + msStepId+ "','"
//		                            + "" + "','"
//		                            + msEqpId+ "','"
//		                            + lsCompTable.get(i).get("FINEPROCESSID").toString() + "','"
//		                            + lsCompTable.get(i).get("MaterialId").toString() + "','"
//		                            + lsCompTable.get(i).get("MaterialMame").toString() + "','"
//		                            + lsCompTable.get(i).get("MaterialType").toString() + "','"
//		                            + MESCommon.UserId + "','"
//		                            + MESCommon.UserName + "',"
//		                            + MESCommon.ModifyTime + ");";
//				    	}
			    }
			    
			    String sError= db.ExecuteSQL(sSql);
			 if (sError != "") {
					MESCommon.showMessage(WIP_TrackIn_Pre.this, sError);
					return false; 
				 }
			}
			return true; 
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
			return false; 
		}
	}
	
	private  Boolean InsertCompTable(String sISCOMPREPEATED)
	{
		try {
	
		
			String   sMaterialId = lsCompID.get(0).get("MATERIALID").toString();
			String   sMaterialMame = lsCompID.get(0).get("MATERIALNAME").toString();
			String   sMaterialType = lsCompID.get(0).get("MATERIALMAINTYPE").toString();
			String   sRAWPROCESSID=lsCompID.get(0).get("RAWPROCESSID").toString();
			String   sFINEPROCESSID=lsCompID.get(0).get("FINEPROCESSID").toString();
			String   sTracetype=lsCompID.get(0).get("TRACETYPE").toString();
			String   sLotID=lsCompID.get(0).get("LOTID").toString();
			String   sSUPPLYID=lsCompID.get(0).get("SUPPLYID").toString();
			String   sLNO=lsCompID.get(0).get("FURNACENO").toString();

			
			HashMap<String, String> hs = new HashMap<String, String>();				
			hs.put("MaterialId",sMaterialId);
			hs.put("MaterialMame", sMaterialMame);
			if (!sFINEPROCESSID.equals("")) {
			
				if(!sMaterialId.equals(""))
				{
			      String sFinId=getCompid(sMaterialId);
			      if(!sFinId.equals(""))
			      {
			    	  if(sFinId.equals("半成品方型件"))
			    	  {
			    		  hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);	
			    	
			    	  }else {
			    		  hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);					    		
					}				    	 
			      }
				}else {
					hs.put("PRODUCTSERIALNUMBER",sFINEPROCESSID);	
					
				}
			}else  if (!sRAWPROCESSID.equals("")&&sFINEPROCESSID.equals(""))
			{
				hs.put("PRODUCTSERIALNUMBER",sRAWPROCESSID);
			}
			else if (!sLotID.equals("")&&sRAWPROCESSID.equals("")&&sFINEPROCESSID.equals(""))
			{
				hs.put("PRODUCTSERIALNUMBER",sLotID);
			}
			hs.put("SEQ",msProductSerialNumber);
			hs.put("MaterialType", sMaterialType);
			hs.put("CHECKFLAG", "N");
			hs.put("TRACETYPE", sTracetype);
			hs.put("LOTID", sLotID);
			hs.put("RAWPROCESSID", sRAWPROCESSID);
			hs.put("FINEPROCESSID", sFINEPROCESSID);
			hs.put("SUPPLYID", sSUPPLYID);
			hs.put("FURNACENO", sLNO);
			if(sMaterialMame.contains(msMMT) )
			{
				hs.put("ISCHIEF","Y" );
			}
			else
			{
				hs.put("ISCHIEF","N" );
			}
			hs.put("ISCOMPEXIST", "Y");
			hs.put("ISCOMPREPEATED", sISCOMPREPEATED);
			lsCompTable.add(hs);							
			adapterTab0.notifyDataSetChanged();
			
			return true ;
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
			return false ;
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
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
			return e.toString();
		}
	}
	public void Clear() {
		try {
			msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			lsAnalysisTable .clear();lsOrder.clear();
            lsCompID .clear();lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_pf.clear();lsProduct.clear();
            adapterTab0.notifyDataSetChanged();
            btnTab1.performClick();
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn_Pre.this, e.toString());
		}

	}

	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
			

		} catch (Exception e) {

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

