package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Setting.SpinnerData;

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

@SuppressLint("NewApi") public class EQP_Start extends Activity {

	private MESDB db = new MESDB();

	ListView lv;
	Button btnConfirm, btnExit, btnReset,btnTrackOut;
	Spinner spProducttype, spStep, spEQP, spClass;
	EditText editUserID, editUserName, editStartTime, editEndTime,editUserIDaa;
	TextView h0, h1, h2, h3,h4;
	EQPStartAdapter adapter;
    PrefercesService	 prefercesService;
	// 该物料的HashMap记录
	 static int miRowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsTableUser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsExistUser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefProductType = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefStep = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefEQP = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefClass = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsEqpResult_D = new ArrayList<HashMap<String, String>>();
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");
    String datetime = "convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setContentView(R.layout.activity_eqp_start);

		try {
		// 取得控件
		editUserID = (EditText) findViewById(R.id.eqpsatart_tvUserID);
		editUserIDaa= (EditText) findViewById(R.id.eqpsatart_tvUserIDaa);
		editUserName = (EditText) findViewById(R.id.eqpsatart_tvUserName);
		editStartTime = (EditText) findViewById(R.id.eqpsatart_tvStartTime);
		editEndTime = (EditText) findViewById(R.id.eqpsatart_tvEndTime);
		spProducttype = (Spinner) findViewById(R.id.eqpsatart_spProductType);
		spStep = (Spinner) findViewById(R.id.eqpsatart_spStep);
		spEQP = (Spinner) findViewById(R.id.eqpsatart_spEQP);
		spClass = (Spinner) findViewById(R.id.eqpsatart_spClass);
		h0 = (TextView) findViewById(R.id.eqpstart_h0);
		h1 = (TextView) findViewById(R.id.eqpstart_h1);
		h2 = (TextView) findViewById(R.id.eqpstart_h2);
		h3 = (TextView) findViewById(R.id.eqpstart_h3);
		h4 = (TextView) findViewById(R.id.eqpstart_h4);
		h0.setTextColor(Color.WHITE);
		h0.setBackgroundColor(Color.DKGRAY);
		h1.setTextColor(Color.WHITE);
		h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE);
		h2.setBackgroundColor(Color.DKGRAY);
		h3.setTextColor(Color.WHITE);
		h3.setBackgroundColor(Color.DKGRAY);
		h3.setBackgroundColor(Color.DKGRAY);
		h4.setTextColor(Color.WHITE);
		h4.setBackgroundColor(Color.DKGRAY);
		h4.setBackgroundColor(Color.DKGRAY);
		lv = (ListView) findViewById(R.id.eqpsatart_lv);
		btnConfirm = (Button) findViewById(R.id.eqpsatart_btnConfirm);
		btnReset = (Button) findViewById(R.id.eqpsatart_btnRemove);
		btnExit = (Button) findViewById(R.id.eqpsatart_btnExit);
		btnTrackOut= (Button) findViewById(R.id.eqpsatart_btnTrackOut);
	    ActionBar actionBar=getActionBar();
		actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
		actionBar.setTitle("人员设备报工");
		adapter = new EQPStartAdapter(lsuser, this);
		lv.setAdapter(adapter);
	    prefercesService  =new PrefercesService(this);  
        Map<String,String> params=prefercesService.getPreferences();  
		String sProducttype=params.get("ProductType");
	    String sStep=params.get("StepID");
	    String sEqp=params.get("EQPID");
		// ***********************************************Start
	
		// 控件事件
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
	
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				EQPStartAdapter.ViewHolder holder = (EQPStartAdapter.ViewHolder) arg1
						.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				EQPStartAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				miRowNum=position;
				if(holder.cb.isChecked())
				{
					lsuser	.get(position).put("CHECKFLAG","Y"); 
				}else
				{
					lsuser.get(position).put("CHECKFLAG","N"); 
				}
			     
			}
		});

		// 读取产品类型
		lsDefProductType.clear();
		String sSql = "SELECT DISTINCT  PRODUCTTYPE ,PRODUCTTYPE  FROM MPRODUCT  ORDER BY PRODUCTTYPE ";
		String sResult = db.GetData(sSql, lsDefProductType);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Start.this, sResult);
		
		}
		List<SpinnerData> lst = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefProductType.size(); i++) {

			SpinnerData c = new SpinnerData(lsDefProductType.get(i)
					.get("PRODUCTTYPE").toString(), lsDefProductType.get(i)
					.get("PRODUCTTYPE").toString());
			lst.add(c);
		}

		ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,
				android.R.layout.simple_spinner_item, lst);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spProducttype.setAdapter(Adapter);
		MESCommon.setSpinnerItemSelectedByValue(spProducttype,sProducttype);
		// 读取工序
		lsDefStep.clear();
		 sSql = "SELECT   DISTINCT  STEPID,STEPID FROM MSTEPBYMATERIALTYPE  WHERE MATERIALTYPE like'%"
				+ sProducttype + "%' ORDER BY STEPID";
		 sResult = MESDB.GetData(sSql, lsDefStep);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Start.this, sResult);
			finish();
		}
		 lst = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefStep.size(); i++) {
			SpinnerData c = new SpinnerData(lsDefStep.get(i)
					.get("STEPID").toString(), lsDefStep.get(i)
					.get("STEPID").toString());
			lst.add(c);
		}

		ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
				EQP_Start.this,
				android.R.layout.simple_spinner_item, lst);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spStep.setAdapter(Adapter1);
		MESCommon.setSpinnerItemSelectedByValue(spStep,sStep);
		//读取设备
		lsDefEQP.clear();
		sSql = "SELECT DISTINCT B.EQPID,B.EQPNAME FROM  MSTEP_P A INNER JOIN MEQP B ON A.PARAMETERCODE=B.EQPID WHERE A.PARAMETERNAME='EQP' AND A.STEPID LIKE '%"
						+ sStep+ "%'  ORDER BY B.EQPID ";
		sResult = db.GetData(sSql, lsDefEQP);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Start.this, sResult);
			finish();
		}
		ArrayList<SpinnerData> lst1 = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefEQP.size(); i++) {
			SpinnerData c1 = new SpinnerData(lsDefEQP.get(i).get("EQPID")
					.toString(), lsDefEQP.get(i).get("EQPNAME").toString());
			lst1.add(c1);
		}
	    Adapter1 = new ArrayAdapter<SpinnerData>(
				this, android.R.layout.simple_spinner_item, lst1);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spEQP.setAdapter(Adapter1);
		MESCommon.setSpinnerItemSelectedByValue(spEQP,sEqp);
		

		// 读取班次
		lsDefClass.clear();
		sSql = "SELECT  DISTINCT WORKID,STARTTIME, ENDTIME FROM MPROCESSTIME WHERE PRODUCTTYPE  ='"
				+ sProducttype + "' ";
		sResult = MESDB.GetData(sSql, lsDefClass);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Start.this, sResult);
		
		}
		ArrayList<SpinnerData> lst2 = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefClass.size(); i++) {
			SpinnerData c2 = new SpinnerData(lsDefClass.get(i)
					.get("WORKID").toString(), lsDefClass
					.get(i).get("WORKID").toString());
			lst2.add(c2);
		}

		ArrayAdapter<SpinnerData> Adapter2 = new ArrayAdapter<SpinnerData>(
				EQP_Start.this,
				android.R.layout.simple_spinner_item, lst2);
		Adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spClass.setAdapter(Adapter2);
		setFocus(editUserID);
		
		// 添加Spinner事件监听
		spProducttype
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						SpinnerData productType = (SpinnerData) spProducttype
								.getSelectedItem();

						// 读取工序
						lsDefStep.clear();
						String sSql = "SELECT   DISTINCT  STEPID,STEPID FROM MSTEPBYMATERIALTYPE  WHERE MATERIALTYPE ='"
								+ productType.text + "' ORDER BY STEPID";
						String sResult = MESDB.GetData(sSql, lsDefStep);
						if (sResult != "") {
							MESCommon.showMessage(EQP_Start.this, sResult);
						
						}
						ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
						for (int i = 0; i < lsDefStep.size(); i++) {
							SpinnerData c = new SpinnerData(lsDefStep.get(i)
									.get("STEPID").toString(), lsDefStep.get(i)
									.get("STEPID").toString());
							lst.add(c);
						}

						ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
								EQP_Start.this,
								android.R.layout.simple_spinner_item, lst);
						Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spStep.setAdapter(Adapter1);

						
						// 设置显示当前选择的项
						arg0.setVisibility(View.VISIBLE);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		spStep.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				SpinnerData step = (SpinnerData) spStep.getSelectedItem();
				lsDefEQP.clear();
				// 读取机台
				String sSql = "SELECT DISTINCT B.EQPID,B.EQPNAME FROM  MSTEP_P A INNER JOIN MEQP B ON A.PARAMETERCODE=B.EQPID WHERE A.PARAMETERNAME='EQP' AND A.STEPID LIKE '%"
						+ step.value + "%'  ORDER BY B.EQPID ";
				String sResult = db.GetData(sSql, lsDefEQP);
				if (sResult != "") {
					MESCommon.showMessage(EQP_Start.this, sResult);
			
				}
				ArrayList<SpinnerData> lst1 = new ArrayList<SpinnerData>();
				if(lsDefEQP.size()>0)
				{
					for (int i = 0; i < lsDefEQP.size(); i++) {
						SpinnerData c1 = new SpinnerData(lsDefEQP.get(i)
								.get("EQPID").toString(), lsDefEQP.get(i)
								.get("EQPNAME").toString());
						lst1.add(c1);
					}
				}else
				{
					SpinnerData c1 = new SpinnerData("","");
					lst1.add(c1);
				}
				ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
						EQP_Start.this, android.R.layout.simple_spinner_item,
						lst1);
				Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spEQP.setAdapter(Adapter1);
								

				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		spEQP.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				SpinnerData sEQP = (SpinnerData) spEQP.getSelectedItem();
				SpinnerData productType = (SpinnerData) spProducttype
						.getSelectedItem();
				lsEqpResult_D.clear();
				// 读取机台工件信息
				 String sSql = " SELECT A.*,ISNULL (B.REMAINQTY,A.TRACKINQTY)  AS REMAINQTY  FROM"
						+ " (SELECT PRODUCTORDERID,PRODUCTCOMPID,PRODUCTID,TRACKINQTY FROM EQP_PROCESS WHERE TRACKINTIME IS NOT NULL AND TRACKOUTTIME IS NULL AND  EQPID ='" + sEQP.value + "')A "
						+ "  LEFT JOIN (SELECT  EQPID,PRODUCTORDERID,PRODUCTCOMPID,MIN(CASE  ISNULL (REMAINQTY,'') WHEN '' THEN QTY ELSE REMAINQTY END   ) AS REMAINQTY FROM EQP_RESULT_D   WHERE PROCESSSTATUS ='结束'AND  EQPID ='" + sEQP.value + "' AND WORKID !='' GROUP BY EQPID,PRODUCTORDERID,PRODUCTCOMPID)B ON A.PRODUCTORDERID=B.PRODUCTORDERID AND A.PRODUCTCOMPID=B.PRODUCTCOMPID ";
				 String sResult = db.GetData(sSql, lsEqpResult_D);
				 if (sResult != "") {
						MESCommon.showMessage(EQP_Start.this, sResult);
						
					}
				
				// 读取工序
				lsDefClass.clear();
				sSql = "SELECT  DISTINCT WORKID,STARTTIME, ENDTIME FROM MPROCESSTIME WHERE PRODUCTTYPE  ='"
						+ productType.text + "' ";
				sResult = MESDB.GetData(sSql, lsDefClass);
				if (sResult != "") {
					MESCommon.showMessage(EQP_Start.this, sResult);
				
				}
				ArrayList<SpinnerData> lst2 = new ArrayList<SpinnerData>();
				for (int i = 0; i < lsDefClass.size(); i++) {
					SpinnerData c2 = new SpinnerData(lsDefClass.get(i)
							.get("WORKID").toString(), lsDefClass
							.get(i).get("WORKID").toString());
					lst2.add(c2);
				}

				ArrayAdapter<SpinnerData> Adapter2 = new ArrayAdapter<SpinnerData>(
						EQP_Start.this,
						android.R.layout.simple_spinner_item, lst2);
				Adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spClass.setAdapter(Adapter2);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		spClass.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String date = sDateFormatShort.format(new java.util.Date());
				SpinnerData productType = (SpinnerData) spProducttype
						.getSelectedItem();
				SpinnerData sclass = (SpinnerData) spClass.getSelectedItem();
				SpinnerData sEQP = (SpinnerData) spEQP.getSelectedItem();
				lsDefClass.clear();
				// 读取班次
				String sSql = "SELECT  DISTINCT WORKID,STARTTIME, ENDTIME FROM MPROCESSTIME WHERE PRODUCTTYPE ='"
						+ productType.value
						+ "'  AND WORKID='"
						+ sclass.value
						+ "' ORDER BY WORKID ";

				String sResult = db.GetData(sSql, lsDefClass);
				if (sResult != "") {
					MESCommon.showMessage(EQP_Start.this, sResult);
				
				}

				editStartTime.setText(lsDefClass.get(0).get("STARTTIME")
						.toString());
				editEndTime
						.setText(lsDefClass.get(0).get("ENDTIME").toString());
				
				lsTableUser.clear();
				lsuser.clear();
				// 读取已报工人员
				 sSql = " SELECT PROCESSUSERID, PROCESSUSER,LOGINTIME FROM EQP_RESULT_USER WHERE EQPID ='"
						+ sEQP.value
						+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')='' AND  WORKID='"
						+ sclass.value
						+ "' AND  WORKDATE ='"
						+ date
						+ "'   AND ISNULL(STATUS,'')<>'已删除' ";
				 sResult = db.GetData(sSql, lsTableUser);
				if (sResult != "") {
					MESCommon.showMessage(EQP_Start.this, sResult);
				
				}
				for (int i = 0; i < lsTableUser.size(); i++) {
					HashMap<String, String> hs = new HashMap<String, String>();
					hs.put("STATE", "登入");
					hs.put("USERID", lsTableUser.get(i).get("PROCESSUSERID")
							.toString());
					hs.put("USERNAME", lsTableUser.get(i).get("PROCESSUSER")
							.toString());
					hs.put("TIMES", lsTableUser.get(i).get("LOGINTIME").toString());
					hs.put("CHECKFLAG", "N");
					lsuser.add(hs);
				}
				adapter.notifyDataSetChanged();
				setFocus(editUserID);
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		editUserID.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
				
			
					if (editUserID.getText().toString().trim().length() == 0) {
						MESCommon.show(EQP_Start.this, "请扫描条码!");
						return false;
					}
					lsExistUser.clear();
					String sSql = "SELECT USERID,USERNAME,DEPARTMENTID FROM MUSER WHERE  USERID='"+ editUserID.getText().toString().trim() + "'";
					String sResult = db.GetData(sSql, lsExistUser);
					if (sResult != "") {
						MESCommon.showMessage(EQP_Start.this, sResult);
						finish();
					}
					if (lsExistUser.size() == 0) {
						MESCommon.show(EQP_Start.this, "该人员不存在!");
						editUserID.setText("");						
						return false;
					}
					editUserName.setText(lsExistUser.get(0).get("USERNAME").toString());
					// 检查是否已存在
					for (int i = 0; i < lsuser.size(); i++) {				
						if (lsuser.get(i).get("USERID").toString().equals(editUserID.getText().toString().trim()))
						{
							MESCommon.show(EQP_Start.this, "该人员已经登入!");
							setFocus( editUserIDaa);
							editUserID.setText("");
							editUserName.setText("");
							return false;
						}
					}
					String date = sDateFormat.format(new java.util.Date());
					HashMap<String, String> hs = new HashMap<String, String>();
					hs.put("STATE", "");
					hs.put("USERID", editUserID.getText().toString().trim());
					hs.put("USERNAME", lsExistUser.get(0).get("USERNAME").toString());
					hs.put("TIMES", date);
					hs.put("DEPARTMENTID",  lsExistUser.get(0).get("DEPARTMENTID").toString());
					hs.put("CHECKFLAG", "N");
					lsuser.add(hs);
					adapter.notifyDataSetChanged();
					editUserID.setText("");
					editUserName.setText("");
					setFocus( editUserIDaa);
				
					
				}
				return false;
			}
		});
		// editUserName
		editUserIDaa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try {
					setFocus(editUserID);
					
				} catch (Exception e) {

				}
			}
		});

		// btnOK
		Button btnOK = (Button) findViewById(R.id.eqpsatart_btnConfirm);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					SpinnerData seqp = (SpinnerData) spEQP.getSelectedItem();
					SpinnerData sclass = (SpinnerData) spClass.getSelectedItem();
					String date = sDateFormatShort.format(new java.util.Date());
					SpinnerData sEQP = (SpinnerData) spEQP.getSelectedItem();
					if (seqp.text == "") {
						MESCommon.show(EQP_Start.this, "请选择报工设备!");
						finish();
						return;
					}
					if (sclass.text == "") {
						MESCommon.show(EQP_Start.this, "请选择班次!");
						finish();
						return;
					}
					if (lsuser.size() == 0) {
						MESCommon.show(EQP_Start.this, "请添加报工人员!");
						return;
					}
					boolean bflag=true;
					for(int i=0;i<lsuser.size();i++)
					{
						if(	!lsuser.get(i).get("STATE").equals("登入"))
						{
							bflag=false; 
						}
					}
					if(bflag)
					{
						MESCommon.show(EQP_Start.this, "没有可以登入的人员，请添加报工人员!");
						return;
					}
					Boolean IsNextDay = false;
				    if (Integer.parseInt(editEndTime.getText().toString().substring(0, 2)) < Integer.parseInt(editStartTime.getText().toString().substring(0, 2)))
	                {
	                    IsNextDay = true;
	                }
				    Calendar cal = Calendar.getInstance();
				    String sStartdate = sDateFormatShort.format(new java.util.Date());
				    if(IsNextDay)
				    {
				    	cal.add(Calendar.DAY_OF_MONTH, +1);
				    }
				    String sEnddate =sDateFormatShort.format(cal.getInstance().getTime());
					String sProcessStart = sStartdate+" "	+ editStartTime.getText().toString();
					String sProcessEnd = sEnddate+" "+ editEndTime.getText().toString();
					lsExistUser.clear();
					String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE ISNULL (STATUS,'')<>'已删除' AND  ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''  AND EQPID='"
							+ seqp.value
							+ "'  AND WORKID='"
							+ sclass.value
							+ "' AND  WORKDATE ='" + sStartdate + "' ";
					String sResult = db.GetData(sSql, lsExistUser);
					if (sResult != "") {
						MESCommon.showMessage(EQP_Start.this, sResult);
						finish();
					}
					Boolean bExist=false;
				
				    
					for (int i = 0; i < lsuser.size(); i++) {
						
						bExist=true;
						for (int j = 0; j < lsExistUser.size(); j++) {
							if (lsuser.get(i).get("USERID").toString().equals(lsExistUser.get(j).get("PROCESSUSERID").toString()))
							{bExist=false;
								break;
							}
						}
						if(bExist)
						{
							sSql ="INSERT INTO EQP_RESULT_USER(SYSID,EQPID,WORKDATE,WORKID,WORKIDSTARTTIME,WORKIDENDTIME,DEPARTMENTID,LOGINTIME,PROCESSUSERID,PROCESSUSER,MODIFYUSERID,MODIFYUSER,MODIFYTIME,WORK_TOTALTIME_LEN)" +
									"VALUES( "+MESCommon.SysId+"," +
									"'"+seqp.value+"','"+sStartdate+"','"+sclass.value+"','"+sProcessStart+"','"+sProcessEnd+"','"+lsuser.get(i).get("DEPARTMENTID").toString()+"',"+datetime+",'"+lsuser.get(i).get("USERID").toString()+"'," +
											"'"+lsuser.get(i).get("USERNAME").toString()+"','"+MESCommon.UserId+"','"+MESCommon.UserName+"','"+sDateFormat.format(new java.util.Date())+"',convert(varchar,CAST ((round (DATEDIFF(minute,'"+sProcessStart+"','"+sProcessEnd+"')/60.0,1))AS  DECIMAL(18,1)))) ;";
						
							sResult = db.ExecuteSQL(sSql);
							if (sResult != "") {
								MESCommon.showMessage(EQP_Start.this, sResult);
								finish();
							}
						}
					}
					lsuser.clear();
					
					lsTableUser.clear();
					lsuser.clear();
					// 读取已报工人员
					 sSql = " SELECT PROCESSUSERID, PROCESSUSER,LOGINTIME FROM EQP_RESULT_USER WHERE EQPID ='"
							+ sEQP.value
							+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')='' AND  WORKID='"
							+ sclass.value
							+ "' AND  WORKDATE ='"
							+ date
							+ "'   AND ISNULL(STATUS,'')<>'已删除' ";
					 sResult = db.GetData(sSql, lsTableUser);
					if (sResult != "") {
						MESCommon.showMessage(EQP_Start.this, sResult);
					
					}
					for (int i = 0; i < lsTableUser.size(); i++) {
						HashMap<String, String> hs = new HashMap<String, String>();
						hs.put("STATE", "登入");
						hs.put("USERID", lsTableUser.get(i).get("PROCESSUSERID")
								.toString());
						hs.put("USERNAME", lsTableUser.get(i).get("PROCESSUSER")
								.toString());
						hs.put("TIMES", lsTableUser.get(i).get("LOGINTIME").toString());
						hs.put("CHECKFLAG", "N");
						lsuser.add(hs);
					}
					adapter.notifyDataSetChanged();
				    editUserID.setText("");
				    editUserName.setText("");

					Toast.makeText(EQP_Start.this, "人员设备报工成功!", Toast.LENGTH_SHORT).show();
	                  
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Start.this, e.toString());
				}
			}
		});

		btnTrackOut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {			
		         	
		             SpinnerData seqp = (SpinnerData) spEQP.getSelectedItem();
					 SpinnerData sClass = (SpinnerData) spClass.getSelectedItem();
					 String date = sDateFormatShort.format(new java.util.Date());
					 String dateNow = sDateFormat.format(new java.util.Date());
					 String datetime = "convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)";
						
					 if (seqp.text == "") {
							MESCommon.show(EQP_Start.this, "请选择报工设备!");
							return;
					 }
					if (sClass.text == "") {
						MESCommon.show(EQP_Start.this, "请选择班次!");
						return;
					}
				    boolean ISUserSelect=false;
				    String sUserIds="";
					for (int i = 0; i <lsuser.size(); i++) 
					{
						if(lsuser.get(i).get("CHECKFLAG").toString().equals("Y"))
						{
							ISUserSelect=true;
							sUserIds += "'"+lsuser.get(i).get("USERID").toString() + "',";
						}		
					}
					if(!ISUserSelect)
					{
						MESCommon.show(EQP_Start.this, "请选择登出人员!");
						return;
					}                    
						
					for (int i = 0; i < lsuser.size(); i++) {
						if(lsuser.get(i).get("CHECKFLAG").toString().equals("Y"))
						{
							String sSql = "UPDATE EQP_RESULT_USER SET LOGOUTTIME="+datetime+" , WORK_AVAILTIME_LEN=convert(varchar,(CAST ((round (DATEDIFF(minute,'"+lsuser.get(i).get("TIMES").toString()+"',"+datetime+")/60.0,1))AS  DECIMAL(18,1)))) "
									+ "  WHERE EQPID='"+seqp.value+"' AND WORKDATE='"+date+"' AND WORKID='"+sClass.value+"' AND PROCESSUSERID='"+lsuser.get(i).get("USERID").toString()+"'  AND ISNULL (LOGOUTTIME,'')='' ";
									
						    String	 sResult = db.ExecuteSQL(sSql);
							if (sResult != "") {
								MESCommon.showMessage(EQP_Start.this, sResult);
								return;
							}
							
						
					     }	
					}
					lsTableUser.clear();
					lsuser.clear();
					// 读取人员
					String sSql = " SELECT PROCESSUSERID, PROCESSUSER,LOGINTIME FROM EQP_RESULT_USER WHERE EQPID ='"
							+ seqp.value
							+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')='' AND  WORKID='"
							+ sClass.value
							+ "' AND EQPID= '"+seqp.value+"'  AND ISNULL(STATUS,'')<>'已删除'   AND  WORKDATE ='"+ date+"' ";
					String sResult = db.GetData(sSql, lsTableUser);
					if (sResult != "") {
						MESCommon.showMessage(EQP_Start.this, sResult);
						finish();
					}
					for (int i = 0; i < lsTableUser.size(); i++) {
						HashMap<String, String> hs = new HashMap<String, String>();
						hs.put("STATE", "登入");
						hs.put("USERID", lsTableUser.get(i).get("PROCESSUSERID").toString());
						hs.put("USERNAME", lsTableUser.get(i).get("PROCESSUSER").toString());
						hs.put("TIMES", lsTableUser.get(i).get("LOGINTIME").toString());
						hs.put("CHECKFLAG", "N");
						lsuser.add(hs);
					}
					adapter.notifyDataSetChanged();
					Toast.makeText(EQP_Start.this, "人员登出成功!", Toast.LENGTH_SHORT).show();
	                  
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Start.this, e.toString());
				}
			}
		});
		// btnRemove
		Button btnRemove = (Button) findViewById(R.id.eqpsatart_btnRemove);
		btnRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
				
					List<HashMap<String, String>> lsuserCopy = new ArrayList<HashMap<String, String>>();
					Boolean isSelect=false;
				
					for(int i=0;i<lsuser.size();i++)
					{
						if (lsuser.get(i).get("CHECKFLAG").toString()
								.equals("Y")) {
							isSelect=true;
						if (lsuser.get(i).get("STATE").toString()
								.equals("登入")) {
							MESCommon.show(EQP_Start.this, "已经登入人员不能删除，只能登出！");
							return;
						
						}
						}
						lsuserCopy.add(lsuser.get(i));
					}
					if(!isSelect)
					{
						MESCommon.show(EQP_Start.this, "请选择要删除的报工人员");
						return;
					}
					for(int i=lsuserCopy.size()-1;i>=0;i--)
					{
						if (lsuserCopy.get(i).get("CHECKFLAG").toString()
								.equals("Y")) {
							lsuser.remove(i);
							
						}
					}					
					adapter.notifyDataSetChanged();
					
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Start.this, e.toString());
				}
			}
		});
		// btnExit
		Button btnExit = (Button) findViewById(R.id.eqpsatart_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Start.this, e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(EQP_Start.this,  e.toString());
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

	
	
	public static class EQPStartAdapter extends BaseAdapter {
		
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;

		int iPosition = -1;

		public EQPStartAdapter(List<HashMap<String, String>> items,
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
			try{
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			miRowNum = position;
			if (convertView == null) {
			
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(
						R.layout.activity_eqp_start_listview, null);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.eqpstartlv_cb);
				holder.tvState = (TextView) convertView
						.findViewById(R.id.eqpstartlv_tvState);
				holder.tvUserid = (TextView) convertView
						.findViewById(R.id.eqpstartlv_tvUserid);
				holder.tvUserName = (TextView) convertView
						.findViewById(R.id.eqpstartlv_tvUserName);
				holder.tvTimes = (TextView) convertView
						.findViewById(R.id.eqpstartlv_tvTimes);

				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置list中TextView的显示
			holder.tvState.setText(getItem(position).get("STATE").toString());
			holder.tvUserid.setText(getItem(position).get("USERID").toString());
			holder.tvUserName.setText(getItem(position).get("USERNAME")
					.toString());
			holder.tvTimes.setText(getItem(position).get("TIMES").toString());

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
			EQPStartAdapter.isSelected = isSelected;
		}

		public static class ViewHolder {
			CheckBox cb;
			TextView tvState;
			TextView tvUserid;
			TextView tvUserName;
			TextView tvTimes;

		}
	}

	public void setFocus(EditText editText) {
		try {

			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(EQP_Start.this, e.toString());
		}

	}
}
