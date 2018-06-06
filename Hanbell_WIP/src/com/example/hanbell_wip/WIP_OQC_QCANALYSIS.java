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

	ListView lv1,lv2;
	Button btnConfirm, btnExit,btnTab1, btnTab2, btnTab3,btnTab2_0, btnTab2_1;
	Spinner spBid;
	EditText editInput,editProductCompID,editMaterialID,editProductModel,editMsheel,editDsheel,editMDsheel,editPMMessage ,editColer;

	//	CheckBox cb ;
	TextView  h1, h2, h3,h4, h5, h9,h11,tvM,tvD,tvPM;
	LinearLayout tab1, tab2, tab3;
	wipoqcAdapter adapter;
	wipoqcAdapterTab2 adapterTab2;
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",  msProductId,  msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID="",msSampletimes="",msStepSEQ,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE;
	int miQty;
	// �����ϵ�HashMap��¼
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
	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip__oqc__qcanalysis);

		// ȡ�ÿؼ�

		try {

			spBid = (Spinner) findViewById(R.id.wipoqc_spBid);	
			editInput = (EditText) findViewById(R.id.wipoqc_tvInput);	
			editProductCompID = (EditText) findViewById(R.id.wipoqc_tvProductCompid);	
			editMaterialID = (EditText) findViewById(R.id.wipoqc_tvMaterialid);	
			editProductModel = (EditText) findViewById(R.id.wipoqc_tvProductModel);	
			editMsheel= (EditText) findViewById(R.id.wipoqc_tvMCheel);	
			editDsheel= (EditText) findViewById(R.id.wipoqc_tvDCheel);	
			editMDsheel= (EditText) findViewById(R.id.wipoqc_tvMDCheel);	
			editPMMessage= (EditText) findViewById(R.id.wipoqc_tvPMMessage);	
			editColer= (EditText) findViewById(R.id.wipoqc_tvProductColer);	
//			cb= (CheckBox)findViewById(R.id.cb);
			tab1 = (LinearLayout) findViewById(R.id.wipoqc_tab1);
			tab2 = (LinearLayout) findViewById(R.id.wipoqc_tab2);
			tab3 = (LinearLayout) findViewById(R.id.wipoqc_tab3);
			h1 = (TextView) findViewById(R.id.wipoqc_h1);
			h2 = (TextView) findViewById(R.id.wipoqc_h2);
			h3 = (TextView) findViewById(R.id.wipoqc_h3);
			h4 = (TextView) findViewById(R.id.wipoqc_h4);
			h5 = (TextView) findViewById(R.id.wipoqc_h5);
			h9= (TextView) findViewById(R.id.wipoqc_h9);
			h11= (TextView) findViewById(R.id.wipoqc_h11);
			tvM= (TextView) findViewById(R.id.wipoqc_tvM);
			tvD= (TextView) findViewById(R.id.wipoqc_tvD);
			tvPM= (TextView) findViewById(R.id.wipoqc_tvPM);
		
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
			lv1 = (ListView) findViewById(R.id.wipoqc_lv1);
			lv2 = (ListView) findViewById(R.id.wipoqc_lv2);
			btnConfirm = (Button) findViewById(R.id.wipoqc_btnConfirm);	
			btnExit=(Button) findViewById(R.id.wipoqc_btnExit);
			btnTab1 = (Button) findViewById(R.id.wipoqc_btnTab1);
			btnTab2 = (Button) findViewById(R.id.wipoqc_btnTab2);
			btnTab3 = (Button) findViewById(R.id.wipoqc_btnTab3);		
			btnTab2_0 = (Button) findViewById(R.id.wipoqc_btnTab2_0_OK);
			btnTab2_1 = (Button) findViewById(R.id.wipoqc_btnTab2_1_OK);
	
			adapter = new wipoqcAdapter(lsAnalysisTable, this);
			lv1.setAdapter(adapter);		
			adapterTab2 = new wipoqcAdapterTab2(lsSidTable, this);
			lv2.setAdapter(adapterTab2);
			editMsheel.setVisibility(8);
			editDsheel.setVisibility(8);
			editMDsheel.setVisibility(8);
			tvD.setVisibility(8);
			tvM.setVisibility(8);

			
			// ***********************************************Start
			prefercesService  =new PrefercesService(this);  
		    params=prefercesService.getPreferences();  
		    ActionBar actionBar=getActionBar();
			actionBar.setSubtitle("������Ա��"+MESCommon.UserName); 
			actionBar.setTitle("��������");
		 	btnTab1.setBackgroundColor(0x88999999);
			btnTab2.setBackgroundColor(0x00000000);
			btnTab3.setBackgroundColor(0x00000000);
			
			
			String date = sDateFormatShort.format(new java.util.Date());	
			// ��ȡ������Ա
			String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'��ɾ��' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
			String sResult = db.GetData(sSql, lsuser);
			if (sResult != "") {
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
				finish();
			}
		
			if ( lsuser.size()==0) {
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, "���Ƚ�����Ա�豸������");
			
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
		// ����Spinner�¼�����
		spBid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SpinnerData sBid = (SpinnerData) spBid.getSelectedItem();
				try {
				// ��ȡ����
				lsSid.clear();
				lsSidTable.clear();
				
	
				 String sResult="";
				 if(sBid.value.equals("Ԥ��"))
				 {
					 sResult= db.GetERP_ASSEMBLESPECIFICATION(msProductId,msProductCompId,msProductOrderId,"Ԥ��","",params.get("StepID"), lsSid);	 
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
			
				// ������ʾ��ǰѡ�����
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
		
		// �ؼ��¼�
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				wipoqcAdapter.ViewHolder holder = (wipoqcAdapter.ViewHolder) arg1.getTag();
				showRow(position);
				
			}
		});
	
		// �ؼ��¼�
		lv2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				wipoqcAdapterTab2.ViewHolder holder = (wipoqcAdapterTab2.ViewHolder) arg1.getTag();
			}
		});
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// ��ѯ������
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipoqc_tvInput);
					if ( lsuser.size()==0) {
						MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, "���Ƚ�����Ա�豸������");
						return false;
					}
					if (txtInput.getText().toString().trim().length() == 0) {
						txtInput.setText("");
						MESCommon.show(WIP_OQC_QCANALYSIS.this, "��ɨ������!");
						return false;
					}
				
					lsCompID.clear();
					String sTempXID = txtInput.getText().toString();
					String sResult="";
					if(editProductCompID.getText().toString().equals("")){
					 sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"",msProductOrderId, "QF","�������","װ��", lsCompID);
					}
					if (!sResult.equals(""))
                    {
						  MESCommon.show(WIP_OQC_QCANALYSIS.this,sResult);
                		  txtInput.setText("");
                          return false;
				
                    }
					if(lsCompID.size()>0)
					{
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					}
					//ˢ����
					if(editProductCompID.getText().toString().equals(""))
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
							MESCommon.show(WIP_OQC_QCANALYSIS.this, " ������롾"+txtInput.getText().toString().trim() +"����û���趨�����������̣�");
							Clear();
							return false;
						}
						if(lsProcess.get(0).get("STEPID").toString().contains("���ǰ����")&&lsProcess.get(0).get("PROCESSSTATUS").toString().equals("�����"))
						{
							
						}else {
							MESCommon.show(WIP_OQC_QCANALYSIS.this, "����Ŀǰ����Ϊ��"+lsProcess.get(0).get("STEPID").toString()+"���������ڡ�"+params.get("StepID")+"��������");
							Clear();
							return false;
						}	
						if (params.get("StepName").contains("����"))
						{
							//�ж�һЩ�ؼ��Ƿ���ʾ��
							if(!lsProcess.get(0).get("MWHEEL").toString().equals("")|| !lsProcess.get(0).get("DWHEEL").toString().equals(""))
							{   
								tvD.setVisibility(0);
							    tvM.setVisibility(0);
								editMsheel.setVisibility(0);
								editDsheel.setVisibility(0);
								editMDsheel.setVisibility(0);
								editMsheel.setText(lsProcess.get(0).get("MWHEEL").toString());
								editDsheel.setText(lsProcess.get(0).get("DWHEEL").toString());
								editMDsheel.setText(lsProcess.get(0).get("WHEELMESSAGE").toString());
						
							}else {
								tvD.setVisibility(8);
							    tvM.setVisibility(8);
								editMsheel.setVisibility(8);
								editDsheel.setVisibility(8);
								editMDsheel.setVisibility(8);
								editMsheel.setText("");
								editDsheel.setText("");
								editMDsheel.setText("");
							
							}
						
						}
						editColer.setText(lsProcess.get(0).get("COLER").toString());
						
						lsProduct.clear();
						String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
						String sError= db.GetData(sSql,  lsProduct);
						 if (sError != "") {
								MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
								return false;
							 }
						//��ʼ��������Ϣ
						editProductCompID.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						editMaterialID.setText(lsProcess.get(0).get("PRODUCTID").toString());
						//editMaterialID.setText(lsProduct.get(0).get("PRODUCTNAME").toString());
						editProductModel.setText(lsProduct.get(0).get("PRODUCTMODEL").toString());					
						msProductOrderId=lsProcess.get(0).get("PRODUCTORDERID").toString();
						msProductId=lsProcess.get(0).get("PRODUCTID").toString();
						msProductCompId=lsProcess.get(0).get("PRODUCTCOMPID").toString();
						msProductSerialNumber=lsProcess.get(0).get("PRODUCTSERIALNUMBER").toString();
						msStepId="�����������վ";		
						msStepSEQ=Integer.toString( Integer.parseInt(lsProcess.get(0).get("STEPSEQ").toString().trim()) +1);
						
						msEqpId=params.get("EQPID").toString();;
						miQty=Integer.parseInt(lsProcess.get(0).get("STARTQTY").toString().trim());
						
						if(lsProcess.get(0).get("STEPID").toString().contains("���ǰ����")&&lsProcess.get(0).get("PROCESSSTATUS").toString().equals("�����"))
						{
							lsAnalysisFinalData.clear();
							//�ж���û�д������ж��ļ�¼������в��ܼ�������
							 sSql=" SELECT * FROM ANALYSISRESULT_M WHERE   SOURCESTEP='"+msStepId+"' and  PRODUCTID ='"+msProductId+"' AND PRODUCTCOMPID = '"+msProductCompId+"' AND QCTYPE = '�Ƴ̼���' AND QC_ITEM = '��Ʒ����' AND ANALYSISJUDGEMENTRESULT = '�ϸ�'  ;";
							 sError= db.GetData(sSql,  lsAnalysisFinalData);
							 if (sError != "") {
									MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
									  Clear();
									return false;
							 }
							 if(lsAnalysisFinalData.size()>0)
							 {
									MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, "������룺��"+msProductCompId+"��,�Ѿ���ɳ��������ҵ������Ҫ�ٽ��г�����飡");
									  Clear();
									return false;
							 }
							
						}
						
						//��ʼ��������Ŀ
						lsAnalysisData.clear();
						sResult = db.GetAnalysisData_Out(msProductOrderId,msProductId,msProductCompId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"����",lsAnalysisData);
						if (sResult != "") {
							MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
							finish();
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
							
		
							//���ж�����
							if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("��ֵ")) {
								//����Ƿ�����������ֵ��
								if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
								{ //�Ƿ�����ж�
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
								 {   //����Ƿ���Ĭ��ֵ�����û������ֵ��Ĭ��ֵΪĬ�Ͻ��
									if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
									{//�Ƿ�����ж�
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
									{  //�Ƿ�����ж�
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
								} else if (lsAnalysisData.get(i).get("RESULTTYPE").toString().equals("����")) {
									if(!lsAnalysisData.get(i).get("DATAVALUE").toString().trim().equals(""))
									{   //��鵽���Ͽ��иü�����Ŀ�Ĵ洢ֵ
										//�Ƿ�����ж�
										if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
										{   
											//�����У������true�������ж�ΪOK,elseNG����ʾ����Ϊtrueword or falseword
											if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
											{
												hs.put("FINALVALUE", "OK");	
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											}else{
												hs.put("FINALVALUE", "NG");
											    hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());	
											}
										}else
										{   //���жϣ��������ж�Ϊ�϶�ΪOK,��ʾ����Ϊtrueword or falseword
											hs.put("FINALVALUE", "OK");
											if (lsAnalysisData.get(i).get("DATAVALUE").toString().toUpperCase().equals("TRUE"))
											{											
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											}else {
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
											}
										}
										//�жϲ��жϣ�������������Ͽ���ֵ��һ������ֵ�����Ͽ��еġ�
										hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
									}else
									{    //��鵽���Ͽ�û�иü�����Ŀ�Ĵ洢ֵ
										//����Ƿ���Ĭ��ֵ�����û������ֵ��Ĭ��ֵΪĬ�Ͻ��
										if(!lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim().equals(""))
										{
											  //�Ƿ�����ж�
											if(lsAnalysisData.get(i).get("ISJUDGE").toString().trim().equals("Y"))
											{//�����У������true�������ж�ΪOK,elseNG����ʾ����Ϊtrueword or falseword
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
												//���жϣ����������ж�Ϊ�϶�ΪOK,��ʾ����Ϊtrueword or falseword
												hs.put("FINALVALUE", "OK");
												if (lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().toUpperCase().equals("TRUE"))
												{											
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
												}else {
												hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("FALSEWORD").toString().trim());
												}
												hs.put("DATAVALUE", lsAnalysisData.get(i).get("DEFAULTSVALUE").toString());
											}
											
										}//û��Ԥ��ֵ��Ĭ����OK�������Ǻϸ���ʾ���ִ�trueword
										else{
											hs.put("FINALVALUE", "OK");
											hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("TRUEWORD").toString().trim());
											hs.put("DATAVALUE", "True");
										}
									}
						}else//������ֵ���Ͳ���
						{
							if(lsAnalysisData.get(i).get("ANALYSISITEM").toString().toUpperCase().equals("VI")){
								//��ѯ����ֵ
									List<HashMap<String, String>> lsdtStep_P = new ArrayList<HashMap<String, String>>();
									String sSQL = "SELECT * FROM PROCESS_STEP_P WHERE  PRODUCTCOMPID ='"+msProductCompId+"' and MATERIALMAINTYPE='����' ";
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

						lsAnalysisTable.add(hs);
						}
						adapter.notifyDataSetChanged();
						//���ݲ�Ʒ����Ϣ ��ȡװ��淶
						lsBid.clear();
	 				   sSql = "SELECT DISTINCT AS_BID, AS_INDEX ||'  '|| AS_BNAME AS AS_BNAME FROM ERP_ASSEMBLESPECIFICATION  WHERE PRODUCTID='" +msProductId + "' " +
									" AND   PRODUCTCOMPID='" +msProductCompId + "' AND PRODUCTORDERID='" + msProductOrderId + "' ORDER BY AS_INDEX ";
						sResult = db.GetData(sSql, lsBid);
						if (sResult != "") {
							MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sResult);
							setFocus(editInput);
						}
						List<SpinnerData> lst = new ArrayList<SpinnerData>();
						SpinnerData c = new SpinnerData("Ԥ��", "Ԥ��");
						lst.add(c);
						for (int i = 0; i < lsBid.size(); i++) {
							c = new SpinnerData(lsBid.get(i).get("AS_BID").toString(), lsBid.get(i).get("AS_BNAME").toString());
							lst.add(c);
						}

						ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(WIP_OQC_QCANALYSIS.this,	android.R.layout.simple_spinner_item, lst);
						Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spBid.setAdapter(Adapter);
									
						editInput.setText("");
						setFocus(editProductCompID);
					}
					} catch (Exception e) {
						MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, e.toString());
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
	
		// btnOK
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String sResult="�ϸ�";
					
					if (editProductCompID.getText().toString().trim().equals("") ) {
						MESCommon.show(WIP_OQC_QCANALYSIS.this, "����ɨ�������ڽ��б�����");
						return;
					}
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{
								MESCommon.show(WIP_OQC_QCANALYSIS.this, "������["+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"]������");
								btnTab2.performClick();
								return;
							}
						}
						if(lsAnalysisTable.get(i).get("FINALVALUE").toString().trim().equals("NG"))
						{
							sResult="���ϸ�";
							break;
						}
					}
					
			 	   if(sResult.equals("���ϸ�"))
					{
						
						AlertDialog alert=	new AlertDialog.Builder(WIP_OQC_QCANALYSIS.this).setTitle("ȷ��").setMessage("���鲻�ϸ�,�Ƿ�ȷ�ϼ���������")
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("���ϸ�", lsAnalysisTable, lsAnalysisData);
				            	//ִ�������ж�
					             db.FinalSaveData(msAnalysisformsID,msSampletimes,"���ϸ�",MESCommon.UserId ,MESCommon.UserName,"");
					            
								 Toast.makeText(WIP_OQC_QCANALYSIS.this, "�������!", Toast.LENGTH_SHORT).show();
				                  
					            Clear();
				            }  
				        })  
						.setNeutralButton("ȡ��",new DialogInterface.OnClickListener() {  
						            @Override  
						            public void onClick(DialogInterface dialog,int which) {  
						                // TODO Auto-generated method stub  
						            	return ;
						            }  
						 }).show();
					
					} else
					{
					 Save(sResult, lsAnalysisTable, lsAnalysisData);
						//ִ�������ж�
		         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
					  Toast.makeText(WIP_OQC_QCANALYSIS.this, "�������!", Toast.LENGTH_SHORT).show();                  
	                  Clear();
						
					}

			 	   	   
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
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// ������
		private Context context;
		// �������벼��
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
					// ���ViewHolder����
					holder = new ViewHolder();
					// ���벼�ֲ���ֵ��convertview
					convertView = inflater.inflate(R.layout.activity_wip_painting_end_listview, null);
					
					holder.tvAnalySisItem = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvAnalySisItem);
					holder.tvValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvValue);
					holder.tvType = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvType);
					holder.tvSPCMinValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvSPCMinValue);
					holder.tvSPCMaxValue = (TextView) convertView.findViewById(R.id.wippaintingendlv_tvSPCMaxValue);
					holder.tvFinalValue=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvFinalValue);
					holder.tvISNeed=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvISNeed);
					holder.tvISJudge=(TextView) convertView.findViewById(R.id.wippaintingendlv_tvISJudge);
					// Ϊview���ñ�ǩ
					convertView.setTag(holder);
	//			} else {
	//				// ȡ��holder
	//				holder = (ViewHolder) convertView.getTag();
	//				
	//			}
				// ����list��TextView����ʾ
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
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// ������
		private Context context;
		// �������벼��
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
			
				// ���ViewHolder����
				holder = new ViewHolder();
				// ���벼�ֲ���ֵ��convertview
				convertView = inflater.inflate(R.layout.activity_wip_track_in_tab2_listview, null);
				holder.tvAnalySidID = (TextView) convertView.findViewById(R.id.wippaintingendlv2_tvAnalySidID);
				holder.tvAnalySidItem = (TextView) convertView.findViewById(R.id.wippaintingendlv2_tvAnalySidItem);
			
				// Ϊview���ñ�ǩ
				convertView.setTag(holder);
			} else {
				// ȡ��holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// ����list��TextView����ʾ
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

				
				if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("��ֵ")) {
					tab2_0.setVisibility(View.VISIBLE);
					tab2_1.setVisibility(View.INVISIBLE);
					if(!lsAnalysisTable.get(iRow).get("DATAVALUE").toString().equals(""))
					{
						edit.setText((String) lsAnalysisTable.get(iRow).get("DATAVALUE"));
					}else{
					edit.setText((String) lsAnalysisData.get(iRow).get("DEFAULTSVALUE"));}
					setFocus(edit);		
				} else if (lsAnalysisTable.get(iRow).get("RESULTTYPE").toString().equals("����")) {
					tab2_0.setVisibility(View.INVISIBLE);
					tab2_1.setVisibility(View.VISIBLE);
					//����rad����ʾ��Ϣ

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
	



	private void Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
		
			 List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		 
             String  sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
             String   sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_OQC_QCANALYSIS.this, sError);
			 }
             if (lsResule_M.size() > 0)
             {
                 sSQL =sSQL+ "UPDATE ANALYSISRESULT_M SET CHIEFANALYSISUSERID='" +MESCommon.UserId + "', CHIEFANALYSISUSER='" +MESCommon.UserName + "', CHIEFANALYSISTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ,ANALYSISJUDGEMENTRESULT='" + sResult + "',DATACOMPLETESTATUS='�����',MODIFYUSERID='" + MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)  , TOREMENBER=''  WHERE   ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "' ;";
                       
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
	         		if(listTemp.get(i).get("RESULTTYPE").toString().equals("��ֵ"))
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
         	      MESCommon.showMessage(WIP_OQC_QCANALYSIS.this,sMessage); 
         	      return;
         	     }
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
			
			
			editProductCompID.setText("");
			editMaterialID.setText("");
			editProductModel.setText("");
			editMsheel.setText("");
			editDsheel.setText("");
			editMDsheel.setText("");
	        editColer.setText("");
			editPMMessage.setText("");
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			miQty=0;
            lsCompID .clear();lsAnalysisTable.clear();lsProcess .clear(); lsAnalysisData .clear();
            lsBid .clear(); lsMid.clear(); lsSid.clear(); lsSidTable.clear();lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();lsDefect.clear();
            btnTab1.performClick();
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
	
	

	//�˷���ֻ�ǹر�������
	private void hintKbTwo() {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
		 if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		}
		
		
		
}
