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
	// �����ϵ�HashMap��¼
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> mlsDetail = new ArrayList<HashMap<String, String>>();	//������ϸ
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
		actionBar.setSubtitle("������Ա��" + MESCommon.UserName);
		actionBar.setTitle(params.get("StepName"));
		// ȡ�ÿؼ�

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
		// ��ȡ������Ա
		String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"
				+ params.get("EQPID")
				+ "' AND ISNULL(WORKDATE,'')='"
				+ date
				+ "' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'��ɾ��' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
		String sResult = db.GetData(sSql, lsuser);
		if (sResult != "") {
			MESCommon.showMessage(WIP_BarCode_Update.this, sResult);
			finish();
		}

		if (lsuser.size() == 0) {
			MESCommon.showMessage(WIP_BarCode_Update.this, "���Ƚ�����Ա�豸������");

		}
		 if(!params.get("StepID").contains("����") &&!params.get("StepID").contains("����"))
		 {
			 MESCommon.show(WIP_BarCode_Update.this, "��ǰ��վ��Ϊ����վ����������ٽ��з�����ҵ����");
		 }
			//����վ��Ҫ������
			if (params.get("StepName").contains("����") && !params.get("StepName").contains("P��"))
			{   //0,��ʾ��
				cksd.setVisibility(0);
				ckdl.setVisibility(0);
				ckfl.setVisibility(0);
		    	lv.getLayoutParams().height=270;
			}else {
				//8,����ʾ����ռλ��
				cksd.setVisibility(8);
				ckdl.setVisibility(8);
				ckfl.setVisibility(8);
				lv.getLayoutParams().height=310;
			}
		 setFocus(editInput);

		// ***********************************************Start
		// �ؼ��¼�
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				WIPBarcodeAdapter.ViewHolder holder = (WIPBarcodeAdapter.ViewHolder) arg1
						.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				WIPBarcodeAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				// ��CheckBox��ѡ��״����¼����
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
                	tvOne.setText("һ�λ���");
                	tvTwo.setVisibility(0);
                	editSerialnumberIDTwo.setVisibility(0);
                	lv.getLayoutParams().height=210;
                	//һdip�൱��2�ĺ���趨
                	Toast.makeText(WIP_BarCode_Update.this, "ȷ��һ��/���λ��Ѿ���װ���!",Toast.LENGTH_SHORT).show();
                }else {
          		ckfl.setEnabled(false);
              	ckfl.setChecked(false);
              	tvOne.setText("��������");
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
                 	tvOne.setText("һ�λ���");
                  }else {
            		ckfl.setEnabled(false);
                	ckfl.setChecked(false);
                   	tvOne.setText("��������");
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
					// ��ѯ������
					try {						
					
					EditText txtInput = (EditText) findViewById(R.id.wipbarcode_tvInput);
					String strCompID = txtInput.getText().toString().trim().toUpperCase();
					editInput.setText(strCompID);
					if (lsuser.size() == 0) {
						MESCommon.showMessage(WIP_BarCode_Update.this,"���Ƚ�����Ա�豸������");
						return false;
					}
					 if(!params.get("StepID").contains("����") &&!params.get("StepID").contains("����"))
					 {
						 MESCommon.show(WIP_BarCode_Update.this, "��ǰ��վ��Ϊ����վ����������ٽ��з�����ҵ����");
							editInput.setText("");
							setFocus(editInput);
					     return false;
					 }

					if (strCompID.length() == 0) {
						MESCommon.show(WIP_BarCode_Update.this, "��ɨ������!");
						txtInput.setText("");
						return false;
					}					
					// ��������룻
					if (editProductCompid.getText().toString().equals("")) {

						for (int i = 0; i < lsCompTable.size(); i++) {
							if (lsCompTable.get(i).get("ProductCompID").toString().equals(txtInput.getText().toString().trim())) {
								MESCommon.show(WIP_BarCode_Update.this, "�������["+ txtInput.getText().toString().trim()+ "] �����嵥��,��ѡ���µ��������!");
								editInput.setText("");
								setFocus(editInput);
								return false;
							}

						}
                        //��������Ƿ�����
						String sError = CheckProductOrder(editInput.getText().toString());
						if (!sError.equals("")) {
							MESCommon.show(WIP_BarCode_Update.this, sError);
							editInput.setText("");
							setFocus(editInput);
							return false;
						}
						
						List<HashMap<String, String>> lsPlan = new ArrayList<HashMap<String, String>>();
						lsPlan.clear();
						String sSQL = "SELECT  A.PRODUCTORDERID,A.PRODUCTCOMPID, B.PRODUCTTIME,B.PRODUCTORDERTYPE   FROM PROCESS A  INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID AND A.PROCESSSTATUS<>'�����'  WHERE A.PRODUCTCOMPID='"+editInput.getText().toString()+"'  ";
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
						//��������Ƿ��趨��������
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
					// �󶨻���/������룬˫�λ��߶�������Ҫ���������
					else {
						if(!ckfl .isChecked())
						{
							lsCompID.clear();
							String sResult="";
							String sNumString="";
							lscheckSDDL.clear();
							if(cksd.isChecked()||ckdl.isChecked())
							{
							   //���˫�εİ���Ϣ��
							
								//20170823��Ϊ˫�λ��ı����������գ����һ�ζ��λ�����ͬʱ����ȥ����֯���ںϳ�˫�λ���Ҳ�п��Ļ�ͷ��Ҫˢ��
								//ֻȥseq��PRODUCTCOMPID
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
									    MESCommon.show(WIP_BarCode_Update.this, "����:��"+editInput.getText().toString().toUpperCase()+"��,�Ѿ���˫��/����:��"+lscheckSDDL.get(0).get("COMPID3").toString()+"���󶨣�");
										editInput.setText("");
										setFocus(editInput);
										return false;
									}
									//���������λ��Ϊ��,���ҵ���������룬֤��ˢ����ǻ����������
									if(!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals("")&&lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals(editInput.getText().toString().toUpperCase()))
									{
										sNumString=lscheckSDDL.get(0).get("PRODUCTCOMPID").toString();
										
									}
									else  if(!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals("")&&!lscheckSDDL.get(0).get("PRODUCTCOMPID").toString().equals(lscheckSDDL.get(0).get("COMPID2").toString()))
									{//��������϶���ˢ���˻������롣���Ұ���һ�������ˡ�
										MESCommon.show(WIP_BarCode_Update.this, "��������:��"+editInput.getText().toString().toUpperCase()+"��,�Ѿ�����"+lscheckSDDL.get(0).get("PRODUCTCOMPID").toString()+"�� �󶨣���ȷ�ϣ���");
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
								    MESCommon.show(WIP_BarCode_Update.this, "����:��"+editInput.getText().toString().toUpperCase()+"��,��ϵͳ���޼�¼������ֻ����������룬���������ţ�");
									editInput.setText("");
									setFocus(editInput);
									return false;
								} 
							}
							//һ�㷢��
							else {
							   sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim().toUpperCase(), "", "", "QF","�㲿��","װ��",  lsCompID);
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
										MESCommon.show(WIP_BarCode_Update.this, "����["+ txtInput.getText().toString().trim()+ "] �����嵥��,��ȷ��!");
										editInput.setText("");
										setFocus(editInput);
										return false;
									}
		
								}
							String	sError="";
							if(!cksd.isChecked()&&!ckdl.isChecked())
							{
								//һ�����������Ƿ�һ��
								//��˻����Ƿ��������趨������һ��,�Ƿ�󶨹�������룡
								if (msPROCESS_PRODUCTID.equals("")) {
									MESCommon.show(WIP_BarCode_Update.this,  "������룬û���趨�������ϣ�");
									return false;
								}
								
								sError=ChecksBOM( lsCompID,editInput.getText().toString().trim().toUpperCase());
							}
							if (!sError.equals("")) 
							{

								 AlertDialog alert=	new AlertDialog.Builder(WIP_BarCode_Update.this).setTitle("ȷ��").setMessage(sError)
											.setPositiveButton("����",new DialogInterface.OnClickListener() {  
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
									.setNeutralButton("ȡ��",new DialogInterface.OnClickListener() {  
									            @Override  
									            public void onClick(DialogInterface dialog,int which) {  
									                // TODO Auto-generated method stub  
									            	return ;
									            }  
									 }).show();  
						
							}	
							//���һ��
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
						MESCommon.show(WIP_BarCode_Update.this, "����ɨ�������ڽ��б�����");
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
							MESCommon.showMessage(WIP_BarCode_Update.this,	"������룺��"+ lsCompTable.get(i).get("ProductCompID").toString()+ "��,û���趨������������");
							return;
						}
						String sProductOrderid=lsProcess.get(0).get("PRODUCTORDERID").toString();
						String sProductCompId = lsProcess.get(0).get("PRODUCTCOMPID").toString();
						String sProductId = lsProcess.get(0).get("PRODUCTID").toString();
						String sStepId = lsProcess.get(0).get("STEPID").toString();
						String sEqpId =params.get("EQPID").toString();
						String sStepSEQ = lsProcess.get(0).get("STEPSEQ").toString();						
						//����Ҫ�ж��ǲ����ع������Ҫ���ϣ���վ
                        if(sStepId.equals("��������վ") || sStepId.equals("��ý����վ") || sStepId.equals("P��������վ") || sStepId.equals("P��������վ"))
                        {			
                        	String sSerialnumberID="";
                        	//���ֱ�ӷ���Y,
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
								//ֻ�в���˫�λ��߶����ĲŽ��л��ǵĳ����¼
								if(	!lsCompTable.get(i).get("SDDL").toString().equals("Y"))
								{
									sResult = GetScanInfo(lsCompTable.get(i).get("SerialnumberID").toString());									
								}
							}
							sLastProductCompid=sProductCompId;
						}
                        else 
                        {	//˫�λ��߽������°󶨣�������ڹ���������վ��	
                        	String sSql="";

    							//��������ѹ�վ�����°��µĻ���ʱ��ֻ��Ҫ�󶨼��ɣ�
                        	   if(lsCompTable.get(i).get("SDDL").toString().equals("Y"))
                            	{
    								sSql = "UPDATE STKCOMP SET COMPID3='"+ sProductCompId+ "' WHERE PRODUCTCOMPID='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ; UPDATE STKCOMP SET COMPID3='"+ sProductCompId+ "' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ;";
    								sResult=db.ExecuteSQL(sSql);
                            	}else
                            	{
                            		sSql = "UPDATE STKCOMP SET PRODUCTCOMPID='"+ sProductCompId+ "' WHERE COMPIDSEQ='"+ lsCompTable.get(i).get("SerialnumberID").toString() + "'  ;";
    								sResult=db.ExecuteSQL(sSql);
                            	}
							
                        	
				            //����COMPID3û�ɹ���
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
							//�󶨷��ϳɹ�����������ҵ�����߼�
							else 
							{
								//ֻ�в���˫�λ��߶����ĲŽ��л��ǵĳ����¼
								if(	!lsCompTable.get(i).get("SDDL").toString().equals("Y"))
								{
									sResult = GetScanInfo(lsCompTable.get(i).get("SerialnumberID").toString());									
								}
							}
						}
					
                    	if(lsCompTable.get(i).get("OrderType").toString() .equals("һ������")){
                           	sResult=InsertSTEP_P(lsCompTable, i, sProductOrderid,sProductCompId,sProductId,sStepId,sStepSEQ,sEqpId);
						}
						if (!sResult.equals("")) 
						{
							MESCommon.show(WIP_BarCode_Update.this, sResult);
							return;
						}
				    }
						
					Toast.makeText(WIP_BarCode_Update.this, "���ϳɹ�!",Toast.LENGTH_SHORT).show();
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
							MESCommon.show(WIP_BarCode_Update.this,	"��ѡ��Ҫɾ�����㲿��");
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
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// ������
		private Context context;
		// �������벼��
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public WIPBarcodeAdapter(List<HashMap<String, String>> items,
				Context context) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);
			isSelected = new HashMap<Integer, Boolean>();
			// ��ʼ������
			initData();
		}

		// ��ʼ��isSelected������
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
				// ���ViewHolder����
				holder = new ViewHolder();
				// ���벼�ֲ���ֵ��convertview
				convertView = inflater.inflate(
						R.layout.activity_wip_barcode_listview, null);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.wipbarcodelv_cb);
				holder.tvProductCompID = (TextView) convertView
						.findViewById(R.id.wipbarcodelv_tvProductCompid);
				holder.tvSerialnumber = (TextView) convertView
						.findViewById(R.id.wipbarcodelv_tvSerialnumber);

				// Ϊview���ñ�ǩ
				convertView.setTag(holder);
			} else {
				// ȡ��holder
				holder = (ViewHolder) convertView.getTag();
			}

			// ����list��TextView����ʾ
			holder.tvProductCompID.setText(getItem(position).get("ProductCompID").toString());
			holder.tvSerialnumber.setText(getItem(position).get("Serialnumber").toString());

			if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
				// ��CheckBox��ѡ��״����¼����
				getIsSelected().put(position, true);
			} else {
				// ��CheckBox��ѡ��״����¼����
				getIsSelected().put(position, false);
			}
			// ����isSelected������checkbox��ѡ��״��
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
				    //˫��/����ȥ���������Ƿ��д�����װ�乤��
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
           //������ж�Ӧ������
			String sSQL = "SELECT B.PROCESSSTATE,B.PRODUCTORDERID  FROM PROCESS_PRE_P A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID WHERE A.PRODUCTCOMPID='"+ snumber + "' ";
			String sError = db.GetData(sSQL, lscheckOrder);
			if (sError != "") {
				MESCommon.showMessage(WIP_BarCode_Update.this, sError);
				return sResult = sError;
			}
			if (lscheckOrder.size() == 0) {
				return sResult = "������룺��"+snumber+"�������ڣ�����ϵ���ܣ�";
			}else {
				Boolean bFlag=true;
				//��Ϊ���ع�������Լ���״̬���δ���ߵġ�Ĭ����ǰ��������Ӧ�������Ѿ���ɣ��������϶��������л�������ɡ����������δ���ߵ���Ϊ������Ҫ�󶨵�����
				for(int i=0;i<lscheckOrder.size();i++)
				{
					if(lscheckOrder.get(i).get("PROCESSSTATE").toString().equals("δ����"))
					{
						bFlag=false;
						break;
					}
					
				}
				if(!bFlag)
				{
					return sResult = "������룺��"+snumber+"��,����Ӧ�����"+lscheckOrder.get(0).get("PRODUCTORDERID").toString()+"����δ����,����ϵ�������ߺ��ڽ��з�����ҵ��";
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
			String sSQL = "SELECT A.* FROM PROCESS_PRODUCTORDER_PRINTBARCODE A INNER JOIN PROCESS B ON A.PRODUCTORDERID =B.PRODUCTORDERID AND A.PRODUCTCOMPID =B.PRODUCTCOMPID AND B.PROCESSSTATUS <>'�����' AND A.PRODUCTCOMPID ='"
					+ snumber + "' ";
			String sError = db.GetData(sSQL, lscheckExist);
			if (sError != "") {
				MESCommon.showMessage(WIP_BarCode_Update.this, sError);
				return sResult = sError;
			}
			if (lscheckExist.size() == 0) {
				return sResult = "����δ����������["+snumber+"],�趨�������ϣ����飡";
			}
			lscheckExistSTKCOMP.clear();
			if(!msOrderType.equals("�ع�����"))
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
					return sResult = "������룺��" + snumber + "���Ѿ��а󶨼�¼����ȷ�ϣ�";
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
//					return sResult = "������["+snumber+"],��ϵͳ���Ѿ����ڰ�������롾"+lscheckExistSTKCOMP.get(0).get("COMPID3").toString()+"���ļ�¼,��ȷ�ϣ�";
//				}
//			}
//			else {
//				if(!lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString().trim().equals("")&&(!lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString().trim().equals(lscheckExistSTKCOMP.get(0).get("COMPID2").toString().trim())))
//				{   
//					return sResult = "�û�������["+snumber+"],��ϵͳ���Ѿ����ڰ�������롾"+lscheckExistSTKCOMP.get(0).get("PRODUCTCOMPID").toString()+"���ļ�¼,��ȷ�ϣ�";
//				}
//			}
			
			List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
			sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
			sError = db.GetData(sSQL, lscheckERPBOMSystem);
			if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
			{
				if(cksd.isChecked()||ckdl.isChecked())
			    {
					//˫�β���˻����趨���ϡ�
//					List<HashMap<String, String>> lsDoubleProduct = new ArrayList<HashMap<String, String>>();
//					sSQL = "SELECT B.PRODUCTID FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID  WHERE A.PRODUCTCOMPID='"+ snumber + "' ";
//					sError = db.GetData(sSQL, lsDoubleProduct);	
//					  if (!lsDoubleProduct.get(0).get("PRODUCTID").toString().trim().equals(msPROCESS_PRODUCTID) ) {
//					    return  "�û��������Ϻţ�"+ lsDoubleProduct.get(0).get("PRODUCTID").toString().trim() + " �������趨�������Ϻţ�" + msPROCESS_PRODUCTID+ "����ͬ,�Ƿ�ȷ�ϼ����󶨣�";		
//						
//					  }
			    }
				else 
				{
				  if (!lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim().equals(msPROCESS_PRODUCTID) )
				  {
					  if(!lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim().equals(""))
						{
						  return  "�û��������Ϻţ�"+ lscheckExistSTKCOMP.get(0).get("MATERIALID").toString().trim() + " �������趨�������Ϻţ�" + msPROCESS_PRODUCTID+ "����ͬ,�Ƿ�ȷ�ϼ����󶨣�";
						
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
			if(msOrderType.equals("�ع�����"))
			{	
				//��������ع�������
					
			}
			if ((!editProductCompid.getText().toString().equals("")&& !editSerialnumberID.getText().toString().equals(""))||(!editProductCompid.getText().toString().equals("")&& !editSerialnumberIDTwo.getText().toString().equals(""))) {
				//˫�λ�/�������ȷ���һ����λֵ������
				editProductCompid.setText(editProductCompid.getText().toString().trim().toUpperCase());
				editSerialnumberID.setText(editSerialnumberID.getText().toString().trim().toUpperCase());
				editSerialnumberIDTwo.setText(editSerialnumberIDTwo.getText().toString().trim().toUpperCase());
				
				hs.put("ProductCompID", editProductCompid.getText().toString());
				if((cksd.isChecked()||ckdl.isChecked())&&!ckfl .isChecked())
			    {
					//Ϊ�ջ���Ϊnull��ʾ��û�б������
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
					//˫��/������ѡ������û�й�ѡ��淢�ϣ�lsCompIDΪ��������Ҫ����ȡֵ����lscheckSDDLȡֵ;
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
					//������룬
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
					}//����
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
						hs.put("MATERIALMAINTYPE", "��ѹ����");
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
				//�Ȳ�STKCOMP
				mls.clear();
				sSql = "SELECT DISTINCT A.STOCKID, A.SUPPLYID, A.ITNBR, A.LOTID, A.LOTIDSEQ, A.COMPID, A.COMPIDSEQ, " +
						"A.LOTQTY, A.STOCKTIME, A.SHIPTYPE, B.ITDSC " +
						"FROM STKINSTOCK A LEFT JOIN MSTKINVMAS B ON A.ITNBR=B.ITNBR WHERE A.COMPIDSEQ='" + sScanId + "'";
				sResult = db.GetData(sSql, mls);
				if (!sResult.equals("")) return sResult;
				if (mls.size() == 0)
				{
					return "���롸" + sScanId + "�������������!";
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
