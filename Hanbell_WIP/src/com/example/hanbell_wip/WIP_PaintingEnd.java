package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Setting.SpinnerData;
import com.example.hanbell_wip.WIP_TrackIn_CRM.wiptrackincrmAdapterTab2_2;

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
import java.sql.Connection;
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

public class WIP_PaintingEnd extends Activity {

	private MESDB db = new MESDB();

	ListView lv1,lv2;
	Button btnConfirm,btnDefect, btnExit, btnTemporary,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1;
	Button btnNG;	//20190523 C1793
	Spinner spBid, spDefect;
	EditText editInput,editCompID,editMaterialID,editProductName,editColer,editMessage,editVi,editCustomerName;
	TextView  h1, h2, h3,h4, h5, h6,h9,h10,txtLVI,tvCM,tvPdr,tvDefect;
	TextView tvNG;	//20190523 C1793
	LinearLayout tab1, tab2, tab3;
	WIPPaintingEndAdapter adapter;
	WIPPaintingEndAdapterTab2 adapterTab2;
	PrefercesService prefercesService;
	Map<String,String> params;

	String msProductOrderId="",  msProductId, msProductType="", msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID,msSampletimes,msStepSEQ,msVersion="",msVersionName="" 	, msPdr="", msPdrName="",msOrderType="";
	int miQty;
	// 该物料的HashMap记录
	static int miRowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisData = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBid = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsMid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSid= new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSidTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSysColer = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefect = new ArrayList<HashMap<String, String>>();
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");
	
	private Boolean mbIsFW = false; //是否为服务报工MES单号
	private String msCrmno = "";	//服务报工MES单号

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_painting_end);

		// 取得控件
try {
	

		spBid = (Spinner) findViewById(R.id.wippaintingend_spBid);	
		spDefect= (Spinner) findViewById(R.id.wippaintingend_spDefect);	
		editInput = (EditText) findViewById(R.id.wippaintingend_tvInput);	
		editCompID = (EditText) findViewById(R.id.wippaintingend_tvCompid);	
		editMaterialID = (EditText) findViewById(R.id.wippaintingend_tvMaterialid);	
		editProductName = (EditText) findViewById(R.id.wippaintingend_tvProductName);	
		editColer=(EditText) findViewById(R.id.wippaintingend_tvColer);
		editMessage=(EditText) findViewById(R.id.wippaintingend_tvMessage);
		editVi =(EditText) findViewById(R.id.wippaintingend_tvvi);
		editCustomerName= (EditText) findViewById(R.id.wippaintingend_tvCustomerName);	
		tab1 = (LinearLayout) findViewById(R.id.wippaintingend_tab1);
		tab2 = (LinearLayout) findViewById(R.id.wippaintingend_tab2);
		tab3 = (LinearLayout) findViewById(R.id.wippaintingend_tab3);
		txtLVI = (TextView) findViewById(R.id.wippaintingend_lbvi);
		h1 = (TextView) findViewById(R.id.wippaintingend_h1);
		h2 = (TextView) findViewById(R.id.wippaintingend_h2);
		h3 = (TextView) findViewById(R.id.wippaintingend_h3);
		h4 = (TextView) findViewById(R.id.wippaintingend_h4);
		h5 = (TextView) findViewById(R.id.wippaintingend_h5);
		h10 = (TextView) findViewById(R.id.wippaintingend_h10);
		h9 = (TextView) findViewById(R.id.wippaintingend_h9);
		tvCM= (TextView) findViewById(R.id.wippaintingend_tvCM);
		tvPdr= (TextView) findViewById(R.id.wippaintingend_tvPdr);
		tvDefect= (TextView) findViewById(R.id.wippaintingend_tvDefect);
		
		tvNG= (TextView) findViewById(R.id.wippaintingend_tvNG);		//20190523 C1793
		btnNG = (Button) findViewById(R.id.wippaintingend_btnNG);		//20190523 C1793
		
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
		h10.setTextColor(Color.WHITE);
		h10.setBackgroundColor(Color.DKGRAY);
		h10.setBackgroundColor(Color.DKGRAY);
		lv1 = (ListView) findViewById(R.id.wippaintingend_lv1);
		lv2 = (ListView) findViewById(R.id.wippaintingend_lv2);
		btnConfirm = (Button) findViewById(R.id.wippaintingend_btnConfirm);
		btnTemporary = (Button) findViewById(R.id.wippaintingend_btnTemporary);
		btnDefect = (Button) findViewById(R.id.wippaintingend_btnDefect);
		btnExit = (Button) findViewById(R.id.wippaintingend_btnExit);		
		btnTab1 = (Button) findViewById(R.id.wippaintingend_btnTab1);
		btnTab2 = (Button) findViewById(R.id.wippaintingend_btnTab2);
		btnTab3 = (Button) findViewById(R.id.wippaintingend_btnTab3);		
		btnTab2_0 = (Button) findViewById(R.id.wippaintingend_btnTab2_0_OK);
		btnTab2_1 = (Button) findViewById(R.id.wippaintingend_btnTab2_1_OK);		
		adapter = new WIPPaintingEndAdapter(lsCompTable, this);
		lv1.setAdapter(adapter);		
		adapterTab2 = new WIPPaintingEndAdapterTab2(lsSidTable, this);
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

//		VISIBLE:0  意思是可见的
//		INVISIBILITY:4 意思是不可见的，但还占着原来的空间
//		GONE:8  意思是不可见的，不占用原来的布局空间
		//冷媒滑块显示，奇他不显示
		try {
			if ((!params.get("StepName").contains("入库前检验") && !params.get("StepName").contains("出货前检验")))
			{
				tvNG.setVisibility(8);
				btnNG.setVisibility(8);
			}
			//入库检验站需要合格和不合格按钮！
			if ((params.get("StepName").contains("入库前检验")||params.get("StepName").contains("出货前检验")) && (!params.get("StepName").contains("P机")) && (!params.get("StepName").contains("涡旋")) )
			{
				List<SpinnerData> lst = new ArrayList<SpinnerData>();
				btnConfirm.setVisibility(0);
				btnConfirm.setText("合格");
				btnConfirm.getLayoutParams().width=130;
				btnDefect.setVisibility(0);
				// 读取报工人员
				 String sSql ="";
				if(params.get("StepName").equals("冷媒入库前检验站")||params.get("StepName").equals("冷媒出货前检验站"))
				{	
					if(params.get("StepName").equals("冷媒入库前检验站"))
					{
						sSql  = "SELECT ITEMID DEFECTID, ITEMNAME DEFECTNAME FROM MDEFECTITEM  WHERE ITEMID LIKE 'RKLM%' ORDER BY CAST(SYSID AS INT)  ";
					}else if(params.get("StepName").equals("冷媒出货前检验站"))
					{
						sSql  = "SELECT ITEMID DEFECTID, ITEMNAME DEFECTNAME FROM MDEFECTITEM  WHERE ITEMID LIKE 'OQCLM%' ORDER BY CAST(SYSID AS INT)  ";
					}
					String sResult = db.GetData(sSql, lsDefect);
					if (sResult != "") {
						MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
						finish();
					}
					
					for (int i = 0; i < lsDefect.size(); i++) {
						if(i==0){
							SpinnerData c = new SpinnerData("", "");
							lst.add(c);
						}
						SpinnerData c = new SpinnerData(lsDefect.get(i)
								.get("DEFECTID").toString(), lsDefect.get(i)
								.get("DEFECTNAME").toString());
						lst.add(c);
					}
	
					ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,
							android.R.layout.simple_spinner_item, lst);
					Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spDefect.setAdapter(Adapter);
			  }else {
					spDefect.setVisibility(8);
					tvDefect.setVisibility(8);
			  }	
			}else {
				btnDefect.setVisibility(8);	
				spDefect.setVisibility(8);
				tvDefect.setVisibility(8);
			}
			
			spDefect.setVisibility(8);	//20190523 mark 这个控制留著，但不使用
			tvDefect.setVisibility(8);	//20190523 mark 这个控制留著，但不使用
			
			
			
			//滑块站需要看滑块
			if (params.get("StepName").contains("冷媒滑块"))
			{
				txtLVI.setVisibility(0);
				editVi.setVisibility(0);			
			}else {
				txtLVI.setVisibility(8);
				editVi.setVisibility(8);				
			}
			if (params.get("StepName").contains("冷媒滑块")||params.get("StepName").contains("冷媒清洗"))
			{
				tvPdr.setText("机壳");
			}else {
				tvPdr.setText("颜色");
			}

			// 读取报工人员
			String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
			String sResult = db.GetData(sSql, lsuser);
			if (sResult != "") {
				MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
				finish();
			}
		
			if ( lsuser.size()==0) {
				MESCommon.showMessage(WIP_PaintingEnd.this, "请先进行人员设备报工！");
			
			}
		}
		catch (Exception e) {
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
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
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
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
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
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
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});
		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wippaintingend_editTab2_0);
					if(lsCompTable.size()>0)
					{lsCompTable.get(miRowNum).put("DATAVALUE",edit.getText().toString());
					  lsCompTable.get(miRowNum).put("DISPLAYVALUE",edit.getText().toString());
						
					if(lsCompTable.get(miRowNum).get("ISJUDGE").toString().trim().equals("Y"))
					{
						if(!edit.getText().toString().trim().equals(""))
						{
							    String sminValueString="";
							    String smaxValueString="";
								if(lsCompTable.get(miRowNum).get("SPECMINVALUE").toString().equals(""))
								{
									sminValueString="0";
								}else
								{
									sminValueString=lsCompTable.get(miRowNum).get("SPECMINVALUE").toString();
								}
								if(lsCompTable.get(miRowNum).get("SPECMAXVALUE").toString().equals(""))
								{
									smaxValueString="9999999999";
								}else 
								{									
									smaxValueString=lsCompTable.get(miRowNum).get("SPECMAXVALUE").toString()	;
								}
							
						  if (Double.parseDouble(edit.getText().toString().trim())>=Double.parseDouble(sminValueString)&&
									Double.parseDouble(edit.getText().toString().trim())<=Double.parseDouble(smaxValueString)	)
							{
								 lsCompTable.get(miRowNum).put("FINALVALUE", "OK");
							}else
							{
								 lsCompTable.get(miRowNum).put("FINALVALUE", "NG");
							}					
							
						}else
						{
							
							lsCompTable.get(miRowNum).put("FINALVALUE", "");
						}
					}else
					{
					   lsCompTable.get(miRowNum).put("FINALVALUE", "OK" );
					}
					hintKbTwo();			
					adapter.notifyDataSetChanged();				
					showRow(miRowNum+1);
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wippaintingend_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wippaintingend_radioButtonNG);
					if(lsCompTable.size()>0)
					{
					if( rdoOK.isChecked())
					{   
						lsCompTable.get(miRowNum).put("DISPLAYVALUE",rdoOK.getText().toString());
						lsCompTable.get(miRowNum).put("DATAVALUE", "True");
						lsCompTable.get(miRowNum).put("FINALVALUE", "OK" );
						
					}else
					{
						lsCompTable.get(miRowNum).put("DISPLAYVALUE",rdoNG.getText().toString());
						lsCompTable.get(miRowNum).put("DATAVALUE", "False" );
						if(lsCompTable.get(miRowNum).get("ISJUDGE").toString().trim().equals("Y"))
						{
						   lsCompTable.get(miRowNum).put("FINALVALUE", "NG" );
						}else
						{
						   lsCompTable.get(miRowNum).put("FINALVALUE", "OK" );
						}
					}		
					hintKbTwo();			
					adapter.notifyDataSetChanged();				
					showRow(miRowNum+1);
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});

		spBid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				try {	
				SpinnerData sBid = (SpinnerData) spBid.getSelectedItem();
			
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
					MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
					finish();
				}
		
				for(int i=0;i<lsSid.size();i++)
				{
					HashMap<String, String> hs = new HashMap<String, String>();	
					hs.put("MIDNAME", lsSid.get(i).get("AS_MNAME").toString());
					hs.put("SIDNAME", lsSid.get(i).get("AS_SNAME").toString());				
					lsSidTable.add(hs);
					if ( lsSid.get(i).get("AS_INDEX").toString().equals("A4")) {
						msVersionName=lsSid.get(i).get("AS_MNAME").toString();
						msVersion=lsSid.get(i).get("AS_SNAME").toString();
					}
					if ( lsSid.get(i).get("AS_INDEX").toString().equals("A3")) {
						msPdr=lsSid.get(i).get("AS_MNAME").toString();
						msPdrName=lsSid.get(i).get("AS_SNAME").toString();
					}
				}
				adapterTab2.notifyDataSetChanged();
				
				if(msStepId.equals("冷媒清洗站"))
				{
				   if(!msPdrName.equals(""))
				    {
				      editColer.setText(msPdrName);
				    }
				 }else {
					 //除开滑块，和清洗，其他地方从新查一次涂装颜色。
					if(msStepId.contains("冷媒")||msStepId.contains("涂装"))
					{
						lsSysColer.clear();
					   String   sSql=" SELECT  AS_SNAME AS COLER  FROM ERP_ASSEMBLESPECIFICATION WHERE PRODUCTCOMPID='"+msProductCompId+"' AND AS_MNAME LIKE'%颜色%'  ;";
					   String	sError= db.GetData(sSql,  lsSysColer);
						 if (sError != "") {
								MESCommon.showMessage(WIP_PaintingEnd.this, sError);
								return ;
							 }
						 if(lsSysColer.size()>0)
						 {
						  editColer.setText(lsSysColer.get(0).get("COLER").toString());
						 }
					}
				  }
				if(msStepId.equals("冷媒滑块次组立站"))
				{
				   if(!msPdrName.equals(""))
				    {
				      editColer.setText(msPdrName);
				    }		
					if (params.get("StepName").contains("冷媒滑块"))
					{
						txtLVI.setVisibility(0);
						editVi.setVisibility(0);						
						
					    if(!msVersion.equals(""))
					    {
					      txtLVI.setText(msVersionName) ;
						  editVi.setText(msVersion.substring(0,msVersion.length()-2));
					    }					 
					}else {
				
						txtLVI.setVisibility(8);
						editVi.setVisibility(8);
					}
				    spBid.setEnabled(false)	;
				 }
				} catch (Exception e) {
					// TODO: handle exception
				}
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		// 控件事件
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				WIPPaintingEndAdapter.ViewHolder holder = (WIPPaintingEndAdapter.ViewHolder) arg1.getTag();
				showRow(position);
			
			}
		});
	
		// 控件事件
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				WIPPaintingEndAdapterTab2.ViewHolder holder = (WIPPaintingEndAdapterTab2.ViewHolder) arg1.getTag();
			}
		});
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					EditText txtInput = (EditText) findViewById(R.id.wippaintingend_tvInput);
					String strCompID = txtInput.getText().toString().trim().toUpperCase();
					if ( lsuser.size()==0) {
						MESCommon.showMessage(WIP_PaintingEnd.this, "请先进行人员设备报工！");
						return false;
					}
					if (strCompID.length() == 0) {
						txtInput.setText("");
						MESCommon.show(WIP_PaintingEnd.this, "请扫描条码!");
						return false;
					}
					try {
						//<-----判断是否为服务报工MES单号
						String sSQL = "", sResult = "";
						List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
						sSQL = "SELECT * FROM CRM_HK_FW006 WHERE CRMNO='"+strCompID+"'";
						sResult = db.GetData(sSQL, ls);
						if (!sResult.equals( "")) {
							MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
							setFocus(editCompID);
							return false;
						}
						mbIsFW = ls.size() == 0 ? false : true;	//是否为服务报工MES单号
						//-------------------------->
						
						
						if (!mbIsFW)
						{
							//非服务报工代码
							lsCompID.clear();
							sResult = db.GetProductSerialNumber(strCompID,"",msProductOrderId, "QF","制造号码","装配", lsCompID);
							if (!sResult.equals( "")) {
								MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
								setFocus(editCompID);
								return false;
							}
							lsProcess.clear();
							String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
							
							sResult = db.GetProductProcess(sOrderID,lsCompID.get(0).get("PRODUCTCOMPID").toString(),	lsProcess);
							if (sResult != "") {
								MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
								setFocus(editCompID);
								return false;
							}
							if(lsProcess.size()==0)
							{
								MESCommon.show(WIP_PaintingEnd.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
								Clear();
								setFocus(editCompID);
								return false;
							}
							else {
								if(lsProcess.get(0).get("PROCESSSTATUS").toString().equals("已完成"))
								{
									//主体流程已完成，并且PDA工站不等于冷媒出库前装配站
									if(!params.get("StepID").equals("冷媒出货前检验站"))
									{									
									  MESCommon.show(WIP_PaintingEnd.this,"制造号码【"+txtInput.getText().toString().trim()+"】,已整体工艺加工完成！");
				              		  Clear();
									  setFocus(editCompID);
				                      return false;
									}else {
										List<HashMap<String, String>> lsJudResult = new ArrayList<HashMap<String, String>>();
										String  sSql="SELECT A.*,B.ANALYSISJUDGEMENTRESULT FROM ANALYSISWAITLIST A INNER JOIN ANALYSISRESULT_M B ON A.ANALYSISFORMSID=B.ANALYSISFORMSID WHERE A.PRODUCTORDERID='"+sOrderID+"' AND  A.PRODUCTCOMPID='"+txtInput.getText().toString().toUpperCase().trim()+"' AND A.SOURCESTEP= '冷媒出货前检验站' ORDER BY A.MODIFYTIME DESC ;";
										String sError= db.GetData(sSql,  lsJudResult);
										 if (sError != "") {
												MESCommon.showMessage(WIP_PaintingEnd.this, sError);
												return false;
											 }
										 if(lsJudResult.size()>0)
										 {
											 if(lsJudResult.get(0).get("ANALYSISJUDGEMENTRESULT").equals("合格"))
											 {
												 MESCommon.show(WIP_PaintingEnd.this,"制造号码【"+txtInput.getText().toString().trim()+"】,已完成冷媒出货前检验作业！");
							              		  Clear();
												  setFocus(editCompID);
							                      return false;
											 } 
										 }	
									}							
								}else
								{
									if (!lsProcess.get(0).get("STEPID").toString().equals(params.get("StepID"))) {
										MESCommon.show(WIP_PaintingEnd.this, "工件目前工序为【"+lsProcess.get(0).get("STEPID").toString()+"】，不能在【"+params.get("StepID")+"】报工！");
										Clear();
										setFocus(editCompID);
										return false;
									}	
								}
							}
							if (lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
		                    {
		              		  MESCommon.show(WIP_PaintingEnd.this,"所刷条码不是制造号码!");
		              		  Clear();
							  setFocus(editCompID);
		                      return false;
		                    }
							lsProduct.clear();
							String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
							String sError= db.GetData(sSql,  lsProduct);
							 if (sError != "") {
									MESCommon.showMessage(WIP_PaintingEnd.this, sError);
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
							List<HashMap<String, String>> lsPlan = new ArrayList<HashMap<String, String>>();
							lsPlan.clear();						
							sSQL = "SELECT PRODUCTORDERTYPE FROM PROCESS_PRE   WHERE PRODUCTORDERID='"+lsCompID.get(0).get("PRODUCTORDERID").toString()+"'  ";
							sError = db.GetData(sSQL, lsPlan);
							if (!sError.equals("")) {
								MESCommon.show(WIP_PaintingEnd.this, sError);
								editInput.setText("");
								setFocus(editInput);
								return false;
							}
							if(lsPlan.size()>0)
							{				
								msOrderType=lsPlan.get(0).get("PRODUCTORDERTYPE").toString();							
							}
							
							if(!msOrderType.equals("一般制令"))
							{
								btnDefect.setEnabled(false);
							}else {
								btnDefect.setEnabled(true);
							}
							editCompID.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
							editMaterialID.setText(lsProcess.get(0).get("PRODUCTID").toString());
							editProductName.setText(lsProduct.get(0).get("PRODUCTNAME").toString());					
							msProductType=lsProduct.get(0).get("PRODUCTTYPE").toString();
							//改变默认的单行模式  
							editProductName.setSingleLine(false);  
							//水平滚动设置为False  
							editProductName.setHorizontallyScrolling(false);	
							editColer.setText(lsProcess.get(0).get("COLER").toString());
							editMessage.setText(lsProcess.get(0).get("PMMESSAGE").toString());
							editMessage.setSingleLine(false);
							editMessage.setHorizontallyScrolling(false);
							msProductOrderId=lsProcess.get(0).get("PRODUCTORDERID").toString();
							msProductId=lsProcess.get(0).get("PRODUCTID").toString();
							msProductCompId=lsProcess.get(0).get("PRODUCTCOMPID").toString();
							msProductSerialNumber=lsProcess.get(0).get("PRODUCTSERIALNUMBER").toString();
							msStepId=lsProcess.get(0).get("STEPID").toString();		
							msStepSEQ=lsProcess.get(0).get("STEPSEQ").toString();	
							msEqpId=params.get("EQPID").toString();;
							miQty=Integer.parseInt(lsProcess.get(0).get("STARTQTY").toString().trim());
						}
						else
						{
							//服务报工代码
							List<HashMap<String, String>> lsCRM_HK_FW006 = new ArrayList<HashMap<String, String>>();
							lsCompID.clear();
							sResult = db.GetProductSerialNumber_CRM(txtInput.getText().toString().trim(),"",msProductOrderId, "QF","MES单号","装配", lsCompID);
							if (!sResult.equals(""))
							{
								txtInput.setText("");
								MESCommon.show(WIP_PaintingEnd.this,sResult);
		                        return false;
							}
							if (lsCompID.size() == 0)
							{
								txtInput.setText("");
								MESCommon.show(WIP_PaintingEnd.this,"MES单号查无资料!");
		                        return false;
							}
							
							String sSql="",sError="";
							//读取
							lsCRM_HK_FW006.clear();
							sSql = "SELECT A.CUSTOMREQUEST, A.DISASSEMBLEMEMO, A.RESPONSIBILITY,A.hdcptype FROM CRM_HK_FW006 A WHERE A.CRMNO='"+txtInput.getText().toString().trim().toUpperCase()+"'";
							sError= db.GetData(sSql,  lsCRM_HK_FW006);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_PaintingEnd.this, sError);
								return false;
							}
							if (lsCRM_HK_FW006.size() == 0)
							{
								MESCommon.showMessage(WIP_PaintingEnd.this, "MES单号查无资料!");
								return false;
							}
							lsProduct.clear();
							sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsCompID.get(0).get("MATERIALID").toString()+"'  ;";
							sError= db.GetData(sSql,  lsProduct);
							if (sError != "") 
							{
								MESCommon.showMessage(WIP_PaintingEnd.this, sError);
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
							
							editCompID.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
							editMaterialID.setText(lsProduct.get(0).get("PRODUCTID").toString());
							editProductName.setText(lsProduct.get(0).get("PRODUCTNAME").toString());					
							msProductType="服务维修";
						}
						
//						lsAnalysisData.clear();
//						//判断有没有待最终判定的记录，如果有不能继续进行
//						 sSql=" SELECT * FROM ANALYSISWAITLIST WHERE   SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID = '"+msProductCompId+"' AND QCTYPE = '制程检验' AND QC_ITEM = '自主检验' AND ANALYSISSTATUS = '待最终判定'  ;";
//						 sError= db.GetData(sSql,  lsAnalysisData);
//						 if (sError != "") {
//								MESCommon.showMessage(WIP_PaintingEnd.this, sError);
//								return false;
//						 }
//						 if(lsAnalysisData.size()>0)
//						 {
//								MESCommon.showMessage(WIP_PaintingEnd.this, "制造号码：【"+msProductCompId+"】,正在等待最终判定，不能进行报工作业！");
//								return false;
//						 }
//						 
						//初始化检验项目
						lsAnalysisData.clear();
						lsCompTable.clear();
						if(!params.get("StepID").equals("冷媒出货前检验站"))
						{
						  sResult = db.GetAnalysisData(msProductOrderId,msProductId,msProductCompId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
						}else {
							sResult = db.GetAnalysisData(msProductOrderId,msProductId,msProductCompId,msProductSerialNumber,"冷媒出货前检验站",MESCommon.UserId ,msEqpId,"成品检验",lsAnalysisData);
						}
						if (sResult != "") {
							MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
							setFocus(editCompID);
							Clear();
							return false;
						}
						
						if(lsAnalysisData.size()==0)
						{
							List<HashMap<String, String>> lsdtAnalysisformsID = new ArrayList<HashMap<String, String>>();
						    sSQL = "SELECT * FROM ANALYSISWAITLIST WHERE  SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID ='"+msProductCompId+"'  AND QCTYPE = '制程检验' AND QC_ITEM ='自主检验'  AND ANALYSISSTATUS = '待检验' AND FORMSSTATUS = '待处理' ";
					        sResult = db.GetData(sSQL, lsdtAnalysisformsID);
					    	if (sResult != "") {
								MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
								finish();
							}else {
								msAnalysisformsID=lsdtAnalysisformsID.get(0).get("ANALYSISFORMSID").toString();
								msSampletimes=lsdtAnalysisformsID.get(0).get("SAMPLETIMES").toString();
							}							
						}		
						
						for(int i=0;i<lsAnalysisData.size();i++)
						{
							msAnalysisformsID=lsAnalysisData.get(i).get("ANALYSISFORMSID").toString();
							msSampletimes=lsAnalysisData.get(i).get("SAMPLETIMES").toString();
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
								    sSQL = "SELECT * FROM PROCESS_STEP_P WHERE  PRODUCTCOMPID ='"+msProductCompId+"' and MATERIALMAINTYPE='滑块' ";
							        sResult = db.GetData(sSQL, lsdtStep_P);
							        if(lsdtStep_P.size()>0)
							        {
										hs.put("FINALVALUE", "OK");
										hs.put("DISPLAYVALUE",lsdtStep_P.get(0).get("FINEPROCESSID").toString().trim());
										hs.put("DATAVALUE", lsdtStep_P.get(0).get("FINEPROCESSID").toString());
							        }
								}else{
							hs.put("FINALVALUE", "OK");
							hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
							hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
							}
						}					
						lsCompTable.add(hs);					
					}				
						adapter.notifyDataSetChanged();
						//根据产品等信息 读取装配规范
						lsBid.clear();
	 				    String sSql = "SELECT DISTINCT AS_BID, AS_INDEX ||'  '|| AS_BNAME AS AS_BNAME FROM ERP_ASSEMBLESPECIFICATION  WHERE PRODUCTID='" +msProductId + "' " +
									" AND   PRODUCTCOMPID='" +msProductCompId + "' AND PRODUCTORDERID='" + msProductOrderId + "' ORDER BY AS_INDEX ";
						sResult = db.GetData(sSql, lsBid);
						if (sResult != "") {
							MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
							setFocus(editCompID);
						}
	
						List<SpinnerData> lst = new ArrayList<SpinnerData>();
						SpinnerData c = new SpinnerData("预设", "预设");
						lst.add(c);
						for (int i = 0; i < lsBid.size(); i++) {
							c = new SpinnerData(lsBid.get(i).get("AS_BID").toString(), lsBid.get(i).get("AS_BNAME").toString());
							
							lst.add(c);
						}
	
						ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(WIP_PaintingEnd.this,	android.R.layout.simple_spinner_item, lst);
						Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spBid.setAdapter(Adapter);
						setFocus(editCompID);
						
						if(msStepId.equals("冷媒滑块次组立站"))
						{
							spBid.setEnabled(false)	;						
						}						
						if(msStepId.contains("冷媒")||msStepId.contains("涂装"))
						{
							lsSysColer.clear();
						    sSql=" SELECT  AS_SNAME AS COLER  FROM ERP_ASSEMBLESPECIFICATION WHERE PRODUCTCOMPID='"+msProductCompId+"' AND AS_MNAME LIKE'%颜色%'  ;";
							String sError= db.GetData(sSql,  lsSysColer);
							 if (sError != "") {
									MESCommon.showMessage(WIP_PaintingEnd.this, sError);
									return false;
								 }
							 if(lsSysColer.size()>0)
							 {
							  editColer.setText(lsSysColer.get(0).get("COLER").toString());
							 }
						}
						if(!params.get("StepID").equals("冷媒出货前检验站"))
						{
							if (!mbIsFW)
							{
								//非服务报工TrackIn
								String sSetStepInResule = db.SetStepInbyJWJ(msProductOrderId ,msProductCompId,"",
										msStepId, msStepSEQ, msEqpId, "", "", "", "",
										MESCommon.UserId, MESCommon.UserName, miQty, "");
								if (!sSetStepInResule.equals("")) {
									MESCommon.showMessage(WIP_PaintingEnd.this, sSetStepInResule);							
								    return false;
								}
							}
							else
							{
								//服务报工TrackIn
								//报工开始
								String sSetStepInResule = db.SetStepInbyCRM(msProductOrderId ,msProductCompId,
										msStepId, msEqpId, MESCommon.UserId, MESCommon.UserName);
								if (!sSetStepInResule.equals("")) {
									MESCommon.showMessage(WIP_PaintingEnd.this, "MES单号【"+txtInput.getText().toString().trim()+"】报工开始失败！" + sSetStepInResule);							
								    return false;
								}
							}
								
						}
						
					} catch (Exception e) {
						MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
						setFocus(editCompID);
						return false;
					}
				}			
				
				return false;
				}
			
		});
		
		// editCompID
		editCompID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try {
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
					String sResult="合格";	
					if (editCompID.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_PaintingEnd.this, "请先扫描条码在进行报工作业！");
						return;
					}
					if (params.get("StepName").contains("冷媒滑块"))
					{
						if (editVi.getText().toString().trim().equals("")){
						MESCommon.show(WIP_PaintingEnd.this, "请先输入滑块编号在进行报工作业！");
						return;}
					}
					for(int i=0;i<lsCompTable.size();i++)
					{
						if(lsCompTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsCompTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{
								MESCommon.show(WIP_PaintingEnd.this, "请输入["+lsCompTable.get(i).get("ITEMALIAS").toString()+"]检验结果");
								btnTab2.performClick();
								return;
							}
						}
						if(lsCompTable.get(i).get("FINALVALUE").toString().trim().equals("NG"))
						{
							sResult="不合格";
							break;
						}
					}
					if(sResult.equals("不合格"))
					{  
						AlertDialog alert=	new AlertDialog.Builder(WIP_PaintingEnd.this).setTitle("确认").setMessage("最终判定与自主检验不相同,是否确认【检验合格】继续报工！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("合格", lsCompTable, lsAnalysisData);
				            	//执行最终判定
					            db.FinalSaveData(msAnalysisformsID,msSampletimes,"合格",MESCommon.UserId ,MESCommon.UserName,"");
					        	if(!params.get("StepID").equals("冷媒出货前检验站"))
								{
					        		if (!mbIsFW)
					        		{
							            String sSetStepOutResule=db.SetStepOutbyJWJ(msProductOrderId, msProductCompId, msStepId, msStepSEQ, msEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName, miQty, 0, 0, 0,"",false);
										
										if (!sSetStepOutResule.equals("")) 
										{
											MESCommon.show(WIP_PaintingEnd.this, sSetStepOutResule);
											return;
										}
					        		}
					        		else
					        		{
					        			String sSetStepOutResule=db.SetStepOutbyCRM(msProductOrderId, msProductCompId, msStepId, msEqpId,MESCommon.UserId, MESCommon.UserName,"");	
						 	   			if (!sSetStepOutResule.equals("")) 
						 	   			{
						 	   				MESCommon.show(WIP_PaintingEnd.this, "报工完成失败！" + sSetStepOutResule);
						 	   				return;
						 	   			}
					        		}
								}
								if (!mbIsFW) InsertINSTOCKANALYSIS("合格");
					            Toast.makeText(WIP_PaintingEnd.this, "报工完成!", Toast.LENGTH_SHORT).show();
				                  
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
						 Save(sResult, lsCompTable, lsAnalysisData);
						 //执行最终判定
			         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
	
			         	if(!params.get("StepID").equals("冷媒出货前检验站"))
						{
			         		if (!mbIsFW)
			        		{
			         			String sSetStepOutResule=db.SetStepOutbyJWJ(msProductOrderId, msProductCompId, msStepId, msStepSEQ, msEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName, miQty, 0, 0, 0,"",false);
								if (!sSetStepOutResule.equals("")) 
								{
									MESCommon.show(WIP_PaintingEnd.this, sSetStepOutResule);
									return;
								}
			        		}
			         		else
			         		{
			         			String sSetStepOutResule=db.SetStepOutbyCRM(msProductOrderId, msProductCompId, msStepId, msEqpId,MESCommon.UserId, MESCommon.UserName,"");	
				 	   			if (!sSetStepOutResule.equals("")) 
				 	   			{
				 	   				MESCommon.show(WIP_PaintingEnd.this, "报工完成失败！" + sSetStepOutResule);
				 	   				return;
				 	   			}
			         		}
			         			
						}
			         	if (!mbIsFW) InsertINSTOCKANALYSIS(sResult);
					   Toast.makeText(WIP_PaintingEnd.this, "报工完成!", Toast.LENGTH_SHORT).show();                  
	                   Clear();
					}

				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});

		
		btnDefect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (editCompID.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_PaintingEnd.this, "请先扫描条码在进行报工作业！");
						return;
					}
						AlertDialog alert=	new AlertDialog.Builder(WIP_PaintingEnd.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	//入库前检验不良，不用填写自主检验信息
//				            	for(int i=0;i<lsCompTable.size();i++)
//								{
//									if(lsCompTable.get(i).get("ISNEED").toString().trim().equals("Y"))
//									{
//										if(lsCompTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
//										{
//											MESCommon.show(WIP_PaintingEnd.this, "请输入["+lsCompTable.get(i).get("ITEMALIAS").toString()+"]检验结果");
//											btnTab2.performClick();
//											return;
//										}
//									}
//								}
				            	if(params.get("StepName").equals("冷媒入库前检验站")||params.get("StepName").equals("冷媒出货前检验站"))
								{
									SpinnerData Defect = (SpinnerData) spDefect.getSelectedItem();
									if(Defect.text.equals(""))
									{
										MESCommon.showMessage(WIP_PaintingEnd.this, "制造号码检验不合格,请选择不良原因在进行报工作业！");
										return;
									}
								}
				            	//不合格插入拆修站
			                    String sNewStep = "";
			                    if (msProductType.equals("机体装配") )
			                    {
			                        sNewStep = "机体拆修站";
			                    }
			                    else if (msProductType .equals( "冷媒装配"))
			                    {
			                        sNewStep = "冷媒拆修站";
			                    }
			                    else if (msProductType .equals("机组装配"))
			                    {
			                    }
			                    else if (msProductType .equals("P机体装配"))
			                    {
			                    	sNewStep = "P机体拆修站";
			                    }
			                    else if (msProductType .equals("P机组装配"))
			                    {
			                    	sNewStep = "P机组拆修站";
			                    }
			                    List<HashMap<String, String>> lsProcessPlan = new ArrayList<HashMap<String, String>>();
			                    String  sSql = "SELECT * FROM PROCESS_PROCESSPLAN WHERE PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID='" +msProductCompId  + "' AND PRODUCTID= '" +msProductId  + "' AND CAST( STEPSEQ AS INT )>" + msStepSEQ + " ORDER BY CAST( STEPSEQ AS INT ) DESC";
			                    db.GetData(sSql,lsProcessPlan);
			                  
			                    sSql = "";
			                    for(int i=0;i<lsProcessPlan.size();i++)
								{
			                    	 sSql = sSql + " UPDATE  PROCESS_PROCESSPLAN SET  CHILDPLANSEQ='" + Integer.toString( Integer.parseInt((lsProcessPlan.get(i).get("STEPSEQ").toString()) + 1)) + "',  STEPSEQ='" + Integer.toString(Integer.parseInt(lsProcessPlan.get(i).get("STEPSEQ").toString()) + 1) + "'  WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID='" +lsProcessPlan.get(i).get("PRODUCTCOMPID").toString() + "' AND PRODUCTID= '" +lsProcessPlan.get(i).get("PRODUCTID").toString()  + "' AND  STEPSEQ ='" + lsProcessPlan.get(i).get("STEPSEQ").toString() + "' ; ";
								}
			                    sSql = sSql + " INSERT INTO PROCESS_PROCESSPLAN (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, PLANID, CHILDPLANID, CHILDPLANSEQ, STEPID, STEPSEQ, ISPASS, EQPID, ISGROUP, ISNEEDFIRSTLOT, ISUPLOAD, ERPSTEPID, MODIFYUSERID, MODIFYUSER, MODIFYTIME) SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, PLANID, '" + sNewStep + "', '" +Integer.toString(Integer.parseInt(msStepSEQ) + 1) + "', '" + sNewStep + "', '" + Integer.toString(Integer.parseInt(msStepSEQ) + 1) + "', ISPASS, EQPID, ISGROUP, ISNEEDFIRSTLOT,'N' ISUPLOAD,'' ERPSTEPID,  '" +MESCommon.UserId + "', '" + MESCommon.UserName + "', " + MESCommon.ModifyTime + " FROM PROCESS_PROCESSPLAN  WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID='" +msProductCompId  + "' AND PRODUCTID= '" + msProductId + "' AND  STEPSEQ ='" + msStepSEQ + "' ;";
			                    String sResult= db.ExecuteSQL(sSql);
			                    if(!sResult.equals(""))
			                    {
			                    	MESCommon.show(WIP_PaintingEnd.this, sResult);
									return;
			                    }
			                    Save("不合格", lsCompTable, lsAnalysisData);
								//执行最终判定
				         	    db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
				         	   if(!params.get("StepID").equals("冷媒出货前检验站"))
								{
							       String sSetStepOutResule=db.SetStepOutbyJWJ(msProductOrderId,msProductCompId, msStepId, msStepSEQ, msEqpId, "", "", "", "",MESCommon.UserId, MESCommon.UserName, miQty, 0, 0, 0,"不合格",false);
									
								    if (!sSetStepOutResule.equals("")) 
									{
										MESCommon.show(WIP_PaintingEnd.this, sSetStepOutResule);
										return;
									}
								}
							    Toast.makeText(WIP_PaintingEnd.this, "报工完成!", Toast.LENGTH_SHORT).show();
							    InsertINSTOCKANALYSIS("不合格");
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
					
				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});

		
		// btnRemove
		Button btnTemporary = (Button) findViewById(R.id.wippaintingend_btnTemporary);
		btnTemporary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
						if (editCompID.getText().toString().trim().equals("") ) {
							MESCommon.show(WIP_PaintingEnd.this, "请先扫描条码在进行报工暂存！");
							return;
						}					
						Save( "",lsCompTable, lsAnalysisData);					
		         	    Toast.makeText(WIP_PaintingEnd.this, "暂存完成!", Toast.LENGTH_SHORT).show();
		              
		              Clear();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}			
			}
		});
		// btnExit
		Button btnExit = (Button) findViewById(R.id.wippaintingend_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
				}
			}
		});

} catch (Exception e) {
	MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wip__painting_end, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_StorpTrackIn) {
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

	public static class WIPPaintingEndAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public WIPPaintingEndAdapter(List<HashMap<String, String>> items,
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
	
			TextView tvAnalySisItem;
			TextView tvValue;
			TextView tvType;
			TextView tvSPCMinValue;
			TextView tvSPCMaxValue;
			TextView tvFinalValue;
			TextView tvISNeed;
			TextView tvISJudge;
		}
	}
	
	public static class WIPPaintingEndAdapterTab2 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public WIPPaintingEndAdapterTab2(List<HashMap<String, String>> items,
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
				convertView = inflater.inflate(R.layout.activity_wip_painting_end_tab2_listview, null);
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
			if (iRow < lsCompTable.size()) {
				miRowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wippaintingend_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wippaintingend_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wippaintingend_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wippaintingend_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wippaintingend_tab2_1);

				if (lsCompTable.get(iRow).get("RESULTTYPE").toString().equals("数值")) {
					tab2_0.setVisibility(View.VISIBLE);
					tab2_1.setVisibility(View.INVISIBLE);
					if(!lsCompTable.get(iRow).get("DATAVALUE").toString().equals(""))
					{
						edit.setText((String) lsCompTable.get(iRow).get("DATAVALUE"));
					}else{
					edit.setText((String) lsAnalysisData.get(iRow).get("DEFAULTSVALUE"));}
					setFocus(edit);		
				} else if (lsCompTable.get(iRow).get("RESULTTYPE").toString().equals("布尔")) {
					tab2_0.setVisibility(View.INVISIBLE);
					tab2_1.setVisibility(View.VISIBLE);
					//设置rad的显示信息

					rdoOK.setText(lsAnalysisData.get(iRow).get("TRUEWORD").toString());
					rdoNG.setText(lsAnalysisData.get(iRow).get("FALSEWORD").toString());
					if(!lsCompTable.get(iRow).get("DATAVALUE").toString().equals("")){
					if (lsCompTable.get(iRow).get("DATAVALUE").toString().toUpperCase().equals("TRUE")) {
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
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
		}
	}
	
	private void Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
		
			List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		 	
           String  sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
           String   sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_PaintingEnd.this, sError);
			 }
             if (lsResule_M.size() > 0)
             {
                 sSQL =sSQL+ " UPDATE ANALYSISRESULT_M SET CHIEFANALYSISUSERID='" +MESCommon.UserId + "', CHIEFANALYSISUSER='" +MESCommon.UserName + "', CHIEFANALYSISTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ,ANALYSISJUDGEMENTRESULT='" + sResult + "',DATACOMPLETESTATUS='已完成',MODIFYUSERID='" + MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)  , TOREMENBER=''  WHERE   ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "' ;";
                 sSQL =sSQL+  " delete from ANALYSISRESULT_MD where ANALYSISFORMSID='" + msAnalysisformsID + "' and SAMPLETIMES='" + msSampletimes + "' ;";
	             sSQL =sSQL+  " delete from ANALYSISRESULT_MDD where ANALYSISFORMSID='" + msAnalysisformsID + "' and ANALYSISTIMESINDEX='0' and SAMPLETIMES='" + msSampletimes + "' ;";

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
	         		
	         		sSQL =sSQL+  "INSERT INTO ANALYSISRESULT_MD (ANALYSISFORMSID, SAMPLETIMES, ANALYSISMITEM, ANALYSISITEM, RESULTTYPE, JUDGEMENTSTANDARD, DATANUM, ISREQUIRED, ISJUDGEMENT, SAMPLINGNUM, ANALYSISUSERID, ANALYSISUSER, ANALYSISTIME, FAILURENUM, ANALYSISAVGVALUE, ANALYSISMAXVALUE, ANALYSISMINVALUE, MODIFYUSERID, MODIFYUSER, MODIFYTIME, SPECMINVALUE, SPECMAXVALUE, TARGETVALUE, OFFSETVALUE,OFFSETMINVALUE,OFFSETMAXVALUE,ISSHOWINIDCARD,ISCOVERSHOWINIDCARD)VALUES " +
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
         	      MESCommon.showMessage(WIP_PaintingEnd.this,sMessage); 
         	      return;
         	     }
             }
         	if (!mbIsFW) InsertSTEP_P();
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
		}
	}
	
	private  String InsertSTEP_P( )
	{
		String sResult="";
		try {
			if(msStepId.equals("冷媒滑块次组立站"))
			{   //editProductID.getText().toString(),msStepId, 
				String  sSql=" DELETE FROM PROCESS_STEP_P WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+editInput .getText().toString()+"' AND STEPID='"+msStepId+"'  ;";
				sSql=sSql + " DELETE FROM PROCESS_STEP_NOBARCODE WHERE  PRODUCTORDERID='"+msProductOrderId+"' AND  PRODUCTCOMPID ='"+editInput.getText().toString()+"' AND STEPID ='"+msStepId+"' ;";
				   
				//冷媒滑块次组你加入到PROCESS_STEP_P
                    String sPNameString=editVi.getText().toString()+"滑块";
			    	sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, PRODUCTSERIALNUMBER_OLD,PRODUCTSERIALNUMBER,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME,MATERIALMAINTYPE,TRACETYPE,LOTID,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,ISCOMPEXIST,ISCOMPREPEATED,PROCESS_PRODUCTIDTYPE, ISCHIEFCOMP, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( "
                            + MESCommon.SysId + ",'"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msProductId + "','"
                            + msStepId+ "','"
                            + msStepSEQ + "','"
                            + msEqpId+ "','"
                            + editVi.getText().toString() + "','"
                            + editVi.getText().toString()+msProductCompId  + "','"
                            + ""  + "','"
                            + sPNameString+ "','"
                            + "滑块" + "','"
                            + "" + "','"
                            + ""+ "','"
                            + editVi.getText().toString() + "','"
                            + editVi.getText().toString() + "','"
                            + "" + "','"
                            + "" + "','"
                            + "Y" + "','"
                            + "N" + "','"
                            + "" + "','"
                            + "N" + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
				    
			    	 String sError= db.ExecuteSQL(sSql);
					 if (sError != "") {
							
							return sResult =sError;
						 }
					}
	
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
			return sResult =e.toString();
		}
		return sResult;
	}
	
	private  String InsertINSTOCKANALYSIS(String sResult )
	{
		try {
			
			if(msStepId.equals("冷媒入库前检验站"))
			{   SpinnerData Defect = (SpinnerData) spDefect.getSelectedItem();
				String  sSql="";
			    	sSql = sSql + " INSERT INTO PROCESS_INSTOCKANALYSIS (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, RESULT, DESCRIPTION, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES (  "
                            + MESCommon.SysId + ",'"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msProductId + "','"
                            + msStepId+ "','"
                            + msStepSEQ + "','"
                            + msEqpId+ "','"
                            + sResult + "','"
                            + Defect.text + "','"
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
				    
			    	 String sError= db.ExecuteSQL(sSql);
					 if (sError != "") {							
							return sError;
						 }
			}else if(msStepId.equals("机体入库前检验站"))
			{
				String  sSql="";
				String sANALYSIS_TIMES="1";
				List<HashMap<String, String>> lsPROCESS_BFINSTOCKANALYSIS = new ArrayList<HashMap<String, String>>();
				sSql="SELECT CASE WHEN  max(ANALYSIS_TIMES) IS NULL THEN '0' ELSE max(ANALYSIS_TIMES)  END ANALYSIS_TIMES  FROM PROCESS_BFINSTOCKANALYSIS WHERE PRODUCTORDERID='"+msProductOrderId+"' AND PRODUCTCOMPID ='"+msProductCompId+"' ";
				   String   sError = db.GetData(sSql, lsPROCESS_BFINSTOCKANALYSIS);
					 if (sError != "") {
						MESCommon.showMessage(WIP_PaintingEnd.this, sError);
					 }
				if (lsPROCESS_BFINSTOCKANALYSIS.size()>0) {
					sANALYSIS_TIMES= Integer.toString( Integer.parseInt(lsPROCESS_BFINSTOCKANALYSIS.get(0).get("ANALYSIS_TIMES").toString().trim())+1);
				}
		    	sSql = sSql + " INSERT INTO PROCESS_BFINSTOCKANALYSIS (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID, ANALYSIS_TIMES, ANALYSISRESULT, DESCRIPTION, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES (  "
                        + MESCommon.SysId + ",'"
                        + msProductOrderId + "','"
                        + msProductCompId + "','"
                        + msProductId + "','"
                        + msStepId+ "','"
                        + msStepSEQ + "','"
                        + msEqpId+ "','"+sANALYSIS_TIMES+"','"
                        + sResult + "','','"
                        + MESCommon.UserId + "','"
                        +MESCommon.UserName + "',"
                        + MESCommon.ModifyTime + ");";
			    
		    	  sError= db.ExecuteSQL(sSql);
				 if (sError != "") {							
						return sError;
					 }
			}
	
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
			return e.toString();
		}
		return "";
	}
	
	public void Clear() {
		try {
			List<SpinnerData> lst = new ArrayList<SpinnerData>();
			ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,	android.R.layout.simple_spinner_item, lst);
			Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spBid.setAdapter(Adapter);
			if(params.get("StepName").equals("冷媒入库前检验站")||params.get("StepName").equals("冷媒出货前检验站"))
			{
			  MESCommon.setSpinnerItemSelectedByValue(spDefect,""); 
			}
			editCompID.setText("");
			editMaterialID.setText("");
			editProductName.setText("");
			editColer.setText("");
			editMessage.setText("");
			editCustomerName.setText("");
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";
			miQty=0;
            lsCompID .clear();lsCompTable.clear();
            adapter.notifyDataSetChanged();
            lsProcess .clear(); lsAnalysisData .clear();
            lsBid .clear(); lsMid.clear(); lsSid.clear(); lsSidTable.clear();
            btnTab1.performClick();
            setFocus(editInput);
            editInput.setText("");
            editVi.setText("");
            msVersion="";
		} catch (Exception e) {
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e) {
			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
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
	
	//服务报工扫描代码
//	private void FW_TrackIn(String sCompidseq)
//	{
//		String sResult = "", sSQL = "";
//		List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
//		try {
//			EditText txtInput = (EditText) findViewById(R.id.wippaintingend_tvInput);
//			lsCompID.clear();
//			sResult = db.GetProductSerialNumber_CRM(sCompidseq,"",msProductOrderId, "QF","MES单号","装配", lsCompID);
//			if (!sResult.equals(""))
//			{
//				txtInput.setText("");
//				MESCommon.show(WIP_PaintingEnd.this,sResult);
//                return;
//			}
//			if (lsCompID.size() == 0)
//			{
//				txtInput.setText("");
//				MESCommon.show(WIP_PaintingEnd.this,"MES单号查无资料!");
//                return;
//			}
//			
//			String sSql="",sError="";
//			//读取
//			lsCRM_HK_FW006.clear();
//			sSql = "SELECT A.CUSTOMREQUEST, A.DISASSEMBLEMEMO, A.RESPONSIBILITY,A.hdcptype FROM CRM_HK_FW006 A WHERE A.CRMNO='"+txtInput.getText().toString().trim().toUpperCase()+"'";
//			sError= db.GetData(sSql,  lsCRM_HK_FW006);
//			if (sError != "") 
//			{
//				MESCommon.showMessage(WIP_PaintingEnd.this, sError);
//				return;
//			}
//			if (lsCRM_HK_FW006.size() == 0)
//			{
//				MESCommon.showMessage(WIP_PaintingEnd.this, "MES单号查无资料!");
//				return;
//			}
//			lsProduct.clear();
//			sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsCompID.get(0).get("MATERIALID").toString()+"'  ;";
//			sError= db.GetData(sSql,  lsProduct);
//			if (sError != "") 
//			{
//				MESCommon.showMessage(WIP_PaintingEnd.this, sError);
//				return;
//			}
//			
//			//初始化工件信息
//			//editCrmno.setText(lsCompID.get(0).get("CRMNO").toString());
//			//editMaterialID.setText(lsProduct.get(0).get("PRODUCTID").toString());
//			//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
//			//editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());	
//			msCrmno = lsCompID.get(0).get("CRMNO").toString();
//			msProductOrderId=lsCompID.get(0).get("PRODUCTORDERID").toString();
//			msProductOrderId = msCrmno;
//			msProductId=lsProduct.get(0).get("PRODUCTID").toString();
//			msProductCompId=lsCompID.get(0).get("PRODUCTCOMPID").toString();
//			msProductSerialNumber=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
//			msStepId = params.get("StepID");
//			msStepSEQ = "1";
//			msEqpId = params.get("EQPID");
//			miQty=1;
//			String sFWType = lsCRM_HK_FW006.get(0).get("hdcptype").toString();	//服务产线类别
//			//判断是否为报工暂停
//			String sStepPause = db.IsStepPause(msCrmno, msProductCompId, msStepId, msEqpId);
////			mbIsStepPause = sStepPause.equals("false") ? false: true;
////			if (mbIsStepPause)
////				mMenuItem.setTitle("解除暂停");
////			else
////				mMenuItem.setTitle("报工暂停");
//			
////						lsAnalysisFinalData.clear();
////						//判断有没有待最终判定的记录，如果有不能继续进行
////						sSql=" SELECT * FROM ANALYSISWAITLIST WHERE   SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID = '"+msProductCompId+"' AND QCTYPE = '制程检验' AND QC_ITEM = '自主检验' AND ANALYSISSTATUS = '待最终判定'  ;";
////						sError= db.GetData(sSql,  lsAnalysisFinalData);
////						if (sError != "") {
////							MESCommon.showMessage(WIP_TrackIn_CRM.this, sError);
////							return false;
////						}
////						if(lsAnalysisFinalData.size()>0)
////						{
////							MESCommon.showMessage(WIP_TrackIn_CRM.this, "制造号码：【"+msProductCompId+"】,正在等待最终判定，不能进行报工作业！");
////							return false;
////						}
//			
//			//初始化检验项目
//			lsAnalysisData.clear();
//			sResult = db.GetAnalysisData(msProductOrderId,msProductId,msProductCompId,"",msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
//			if (sResult != "") {
//				MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
//				finish();
//			}
//			
//			if(lsAnalysisData.size()==0)
//			{
//				List<HashMap<String, String>> lsdtAnalysisformsID = new ArrayList<HashMap<String, String>>();
//				String sSQL = "SELECT * FROM ANALYSISWAITLIST WHERE PRODUCTORDERID='"+msProductOrderId+"' AND SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID ='"+msProductCompId+"'  AND QCTYPE = '制程检验' AND QC_ITEM ='自主检验'  AND ANALYSISSTATUS = '待检验' AND FORMSSTATUS = '待处理' ";
//		        sResult = db.GetData(sSQL, lsdtAnalysisformsID);
//		    	if (sResult != "") {
//					MESCommon.showMessage(WIP_PaintingEnd.this, sResult);
//					finish();
//				}else {
//					msAnalysisformsID=lsdtAnalysisformsID.get(0).get("ANALYSISFORMSID").toString();
//					msSampletimes=lsdtAnalysisformsID.get(0).get("SAMPLETIMES").toString();
//				}
//			}
//			lsAnalysisTable.clear();						
//			for(int i=0;i<lsAnalysisData.size();i++)
//			{
//				msAnalysisformsID=lsAnalysisData.get(i).get("ANALYSISFORMSID").toString();
//				msSampletimes=lsAnalysisData.get(i).get("SAMPLETIMES").toString();
////				msSUPPLYLOTID=lsAnalysisData.get(i).get("SUPPLYLOTID").toString();
////				msSUPPLYID=lsAnalysisData.get(i).get("SUPPLYID").toString();
////				msQC_ITEM=lsAnalysisData.get(i).get("QC_ITEM").toString();
////				msQCTYPE=lsAnalysisData.get(i).get("QCTYPE").toString();
//				HashMap<String, String> hs = new HashMap<String, String>();				
//				hs.put("ANALYSISITEM", lsAnalysisData.get(i).get("ANALYSISITEM").toString());
//				hs.put("ITEMALIAS", lsAnalysisData.get(i).get("ITEMALIAS").toString());
//				hs.put("RESULTTYPE", lsAnalysisData.get(i).get("RESULTTYPE").toString());
//				hs.put("SPECMINVALUE", lsAnalysisData.get(i).get("SPECMINVALUE").toString());
//				hs.put("SPECMAXVALUE", lsAnalysisData.get(i).get("SPECMAXVALUE").toString());
//				hs.put("ISNEED", lsAnalysisData.get(i).get("ISNEED").toString());
//				hs.put("ISJUDGE", lsAnalysisData.get(i).get("ISJUDGE").toString());
//				
//				//先判断类型
//				if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("数值")) {
//					//检查是否有输入检测结果值。
//					if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
//					{ //是否必须判断
//						if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
//						{
//							String sminValueString="";
//						    String smaxValueString="";
//							if(lsAnalysisData.get(i).get("SPECMINVALUE").toString().equals(""))
//							{
//								sminValueString="-9999999999";
//							}else
//							{
//								sminValueString=lsAnalysisData.get(i).get("SPECMINVALUE").toString();
//							}
//							if(lsAnalysisData.get(i).get("SPECMAXVALUE").toString().equals(""))
//							{
//								smaxValueString="9999999999";
//							}else {									
//								smaxValueString=lsAnalysisData.get(i).get("SPECMAXVALUE").toString()	;
//							}
//							
//							if (Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())>=Double.parseDouble(sminValueString)&&
//									Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())<=Double.parseDouble(smaxValueString)	)
//							{
//								hs.put("FINALVALUE", "OK");
//							}else
//							{
//								hs.put("FINALVALUE", "NG");
//							}
//						}else
//						{
//							hs.put("FINALVALUE", "OK");
//						}
//						hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DATAVALUE").toString().trim());
//						hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
//				     }
//					 else
//					 {   //检查是否有默认值，如果没有输入值已默认值为默认结果
//						if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
//						{//是否必须判断
//							if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
//							{
//								String sminValueString="";
//							    String smaxValueString="";
//								if(lsAnalysisData.get(i).get("SPECMINVALUE").toString().equals(""))
//								{
//									sminValueString="-9999999999";
//								}else
//								{
//									sminValueString=lsAnalysisData.get(i).get("SPECMINVALUE").toString();
//								}
//								if(lsAnalysisData.get(i).get("SPECMAXVALUE").toString().equals(""))
//								{
//									smaxValueString="9999999999";
//								}else {									
//									smaxValueString=lsAnalysisData.get(i).get("SPECMAXVALUE").toString()	;
//								}
//								
//								if (Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())>=Double.parseDouble(sminValueString)&&
//										Double.parseDouble(lsAnalysisData.get(i).get("DATAVALUE").toString())<=Double.parseDouble(smaxValueString)	)
//								{
//									hs.put("FINALVALUE", "OK");
//								}else
//								{
//									hs.put("FINALVALUE", "NG");
//								}
////								if (Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())>=Double.parseDouble(lsAnalysisData.get(i).get("SPECMINVALUE").toString())&&
////										Double.parseDouble(lsAnalysisData.get(i).get("DEFAULTSVALUE").toString())<=Double.parseDouble(lsAnalysisData.get(i).get("SPECMAXVALUE").toString())	)
////								{
////									hs.put("FINALVALUE", "OK");
////								}else
////								{
////									hs.put("FINALVALUE", "NG");
////								}
//							}else
//							{
//								hs.put("FINALVALUE", "OK");
//							}
//							hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
//							hs.put("DATAVALUE", lsAnalysisData.get(i).get("DEFAULTSVALUE").toString());
//						}else
//						{  //是否必须判断
//							if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
//							{
//							hs.put("FINALVALUE", "");
//							}else
//							{
//								hs.put("FINALVALUE", "OK");
//							}
//							hs.put("DISPLAYVALUE","");
//							hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
//						}
//					  }						
//					} else if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("布尔")) {
//						if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
//						{   //检查到资料库有该检验项目的存储值
//							//是否必须判断
//							if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
//							{   
//								//必须判，如果是true则最终判断为OK,elseNG，显示名字为trueword or falseword
//								if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
//								{
//									hs.put("FINALVALUE", "OK");	
//									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
//								}else{
//									hs.put("FINALVALUE", "NG");
//								    hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());	
//								}
//							}else
//							{   //不判断，则最终判断为肯定为OK,显示名字为trueword or falseword
//								hs.put("FINALVALUE", "OK");
//								if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
//								{											
//								hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
//								}else {
//								hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
//								}
//							}
//							//判断不判断，如果在我们资料库有值，一定带的值是资料库中的。
//							hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
//						}else
//						{    //检查到资料库没有该检验项目的存储值
//							//检查是否有默认值，如果没有输入值已默认值为默认结果
//							if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
//							{
//								  //是否必须判断
//								if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
//								{//必须判，如果是true则最终判断为OK,elseNG，显示名字为trueword or falseword
//									if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE"))
//									{
//										hs.put("FINALVALUE", "OK");	
//										hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
//										hs.put("DATAVALUE", "True");
//									}else{
//										hs.put("FINALVALUE", "NG");
//									    hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());	
//									    hs.put("DATAVALUE", "False");
//									}
//								}
//								else
//								{ 
//									//不判断，有则最终判断为肯定为OK,显示名字为trueword or falseword
//									hs.put("FINALVALUE", "OK");
//									if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE"))
//									{											
//									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
//									}else {
//									hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
//									}
//									hs.put("DATAVALUE", lsAnalysisData.get(i).get("DEFAULTSVALUE").toString());
//								}
//								
//							}//没有预设值，默认是OK，最终是合格，显示名字带trueword
//							else{
//								hs.put("FINALVALUE", "OK");
//								hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
//								hs.put("DATAVALUE", "True");
//							}
//						}
//			}else//除开数值，和布尔
//			{
//				if(lsAnalysisData.get(i).get("ANALYSISITEM").toString().toUpperCase().equals("VI")){
//					//查询滑块值
//						List<HashMap<String, String>> lsdtStep_P = new ArrayList<HashMap<String, String>>();
//						String sSQL = "SELECT * FROM PROCESS_STEP_P WHERE  PRODUCTCOMPID ='"+msProductCompId+"' and MATERIALMAINTYPE='滑块' ";
//				        sResult = db.GetData(sSQL, lsdtStep_P);
//				        if(lsdtStep_P.size()>0)
//				        {
//							hs.put("FINALVALUE", "OK");
//							hs.put("DISPLAYVALUE",lsdtStep_P.get(0).get("FINEPROCESSID").toString().trim());
//							hs.put("DATAVALUE", lsdtStep_P.get(0).get("FINEPROCESSID").toString());
//				        }
//				        else{
//							hs.put("FINALVALUE", "OK");
//							hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
//							hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
//						}
//					}else{
//						hs.put("FINALVALUE", "OK");
//						hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
//						hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
//					}
//				}					
//
//				lsAnalysisTable.add(hs);
//			}
//			adapter.notifyDataSetChanged();
//			
//			//报工开始
//			String sSetStepInResule = db.SetStepInbyCRM(msProductOrderId ,msProductCompId,
//					msStepId, msEqpId, MESCommon.UserId, MESCommon.UserName);
//			if (!sSetStepInResule.equals("")) {
//				MESCommon.showMessage(WIP_PaintingEnd.this, "MES单号【"+txtInput.getText().toString().trim()+"】报工开始失败！" + sSetStepInResule);							
//			    return;
//			}
//			
//			//初始化工件信息
//			editCrmno.setText(lsCompID.get(0).get("CRMNO").toString());
//			editMaterialID.setText(lsProduct.get(0).get("PRODUCTID").toString());
//			//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
//			editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());
//			//服务特殊资讯
//			editCustomRequest.setText(lsCRM_HK_FW006.get(0).get("CUSTOMREQUEST").toString());
//			editDisassembleMemo.setText(lsCRM_HK_FW006.get(0).get("DISASSEMBLEMEMO").toString());
//			for (int i=0; i<lsResponsibility.size(); i++)
//			{
//				String sTemp = lsResponsibility.get(i).get("ITEM").toString();
//				if (sTemp.equals(lsCRM_HK_FW006.get(0).get("RESPONSIBILITY").toString()))
//				{
//					spResponsibility.setSelection(i+1);
//					break;
//				}
//			}
//			
//			editInput.setText("");
//			setFocus(editCrmno);
//		
//		} catch (Exception e) {
//			// TODO: handle exception
//			MESCommon.showMessage(WIP_PaintingEnd.this, e.toString());
//			return;
//		}
//	}
}

