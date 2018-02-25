package com.example.hanbell_wip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.Class.CompInformation;
import com.example.hanbell_wip.Class.MESCommon;
import com.example.hanbell_wip.Class.MESDB;
import com.example.hanbell_wip.Class.PrefercesService;
import com.example.hanbell_wip.WIP_ANALYSISITEM.wiptrackinpreAdapter;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WIP_ANALYSISITEM extends Activity {


	private MESDB db = new MESDB();

	ListView lv0;
	Button btnConfirm, btnExit,btnTab2_0, btnTab2_1;
	EditText editInput,editMaterialName;
	TextView  h1, h2, h3,h10,h11;
	wiptrackinpreAdapter adapter;
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",msProductOrderIdold="",  msProductId="",  msProductCompId,  msProductSerialNumber,  msStepId,  
	  msEqpId,msAnalysisformsID="",msSampletimes,msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE,msMMT="";

	// �����ϵ�HashMap��¼
	static int milv0RowNum = 0;int milv1RowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsAnalysisData = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSysid = new ArrayList<HashMap<String, String>>();
	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_analysisitem);
try{
		// ȡ�ÿؼ�
		editInput = (EditText) findViewById(R.id.wipanalysisitem_tvInput);		
		editMaterialName = (EditText) findViewById(R.id.wipanalysisitem_tvMaterialName);	
		h10 = (TextView) findViewById(R.id.wipanalysisitem_h10);
		h11 = (TextView) findViewById(R.id.wipanalysisitem_h11);
		h3 = (TextView) findViewById(R.id.wipanalysisitem_h3);
		h1 = (TextView) findViewById(R.id.wipanalysisitem_h1);
		h2 = (TextView) findViewById(R.id.wipanalysisitem_h2);
		h3 = (TextView) findViewById(R.id.wipanalysisitem_h3);		
		lv0 = (ListView) findViewById(R.id.wipanalysisitem_lv0);
		h10.setTextColor(Color.WHITE);
		h10.setBackgroundColor(Color.DKGRAY);
		h11.setTextColor(Color.WHITE);
		h11.setBackgroundColor(Color.DKGRAY);
		h1.setTextColor(Color.WHITE);
		h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE);
		h2.setBackgroundColor(Color.DKGRAY);
		h3.setTextColor(Color.WHITE);
		h3.setBackgroundColor(Color.DKGRAY);
		h3.setBackgroundColor(Color.DKGRAY);
		btnConfirm = (Button) findViewById(R.id.wipanalysisitem_btnConfirm);
		btnExit = (Button) findViewById(R.id.wipanalysisitem_btnExit);	
		btnTab2_0 = (Button) findViewById(R.id.wipanalysisitem_btnTab2_0_OK);
		btnTab2_1 = (Button) findViewById(R.id.wipanalysisitem_btnTab2_1_OK);	
		adapter = new wiptrackinpreAdapter(lsAnalysisTable, this);
		lv0.setAdapter(adapter);		

		
		// ***********************************************Start
		prefercesService  =new PrefercesService(this);  
	    params=prefercesService.getPreferences();  
	    ActionBar actionBar=getActionBar();
		actionBar.setSubtitle("������Ա��"+MESCommon.UserName); 
		actionBar.setTitle(params.get("StepName"));
		String date = sDateFormatShort.format(new java.util.Date());	
		
		// ��ȡ������Ա
		String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'��ɾ��' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
		String sResult = db.GetData(sSql, lsuser);
		if (sResult != "") {
			MESCommon.showMessage(WIP_ANALYSISITEM.this, sResult);
			finish();
		}
	
		if ( lsuser.size()==0) {
			MESCommon.showMessage(WIP_ANALYSISITEM.this, "���Ƚ�����Ա�豸������");
		
		}

		btnTab2_0.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					EditText edit = (EditText) findViewById(R.id.wipanalysisitem_editTab2_0);
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
					MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
				}
			}
		});
		btnTab2_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					RadioButton rdoOK = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonOK);
					RadioButton rdoNG = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonNG);
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
					MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
				}
			}
		});
		
		// �ؼ��¼�
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				wiptrackinpreAdapter.ViewHolder holder = (wiptrackinpreAdapter.ViewHolder) arg1.getTag();
				showRow(arg2);
				}});

		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					
					// ��ѯ������
					try {
						
						EditText txtInput = (EditText) findViewById(R.id.wipanalysisitem_tvInput);						
						
						if(lsuser.size()==0)
						{   MESCommon.show(WIP_ANALYSISITEM.this, "���Ƚ����豸��Ա����!");
						setFocus(editInput)	;
						    return false;
						}
						if (txtInput.getText().toString().trim().length() == 0) {
							txtInput.setText("");
							MESCommon.show(WIP_ANALYSISITEM.this, "��ɨ������!");
							return false;
						}
						lsCompID.clear();					
						String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","�㲿��","װ��", lsCompID);			
						if (sResult != "") {
							MESCommon.showMessage(WIP_ANALYSISITEM.this, sResult);
							setFocus(editInput)	;	
							return false;
						}							
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						msProductSerialNumber=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();					
						if(lsAnalysisTable.size()==0)
						{
							if(params.get("StepName") .contains("����"))
							{
							     msProductId="35325-A00";
							}else {
								 msProductId="31330-A53111-SH";
							}			    	
							
						editMaterialName.setText(lsCompID.get(0).get("MATERIALNAME").toString());
							
						//��ʼ��������Ŀ
						lsAnalysisData.clear();
						msStepId=params.get("StepID").toString();	
						msEqpId=params.get("EQPID").toString();
						//��ѯ�Ƿ��Ѿ��������
						List<HashMap<String, String>> lscheckAnal = new ArrayList<HashMap<String, String>>();
						lscheckAnal.clear();
						String sSQL = "SELECT * FROM ANALYSISWAITLIST where PRODUCTSERIALNUMBER='"+msProductSerialNumber+"'  AND ANALYSISSTATUS='�����' ";
			             String sError = db.GetData(sSQL, lscheckAnal);
						 if (sError != "") {
							MESCommon.showMessage(WIP_ANALYSISITEM.this, sError);
							return false;
						 }
						 if(lscheckAnal.size()>0)
						 {
								MESCommon.showMessage(WIP_ANALYSISITEM.this, "���롾"+msProductSerialNumber+"��,�Ѿ����װ����飡������ѡ��");
								return false;
						 }
						 if(msStepId.equals("��ý���������վ"))
						 {
							 if(!lsCompID.get(0).get("MATERIALNAME").toString().contains("����"))
							 {
								 MESCommon.showMessage(WIP_ANALYSISITEM.this, "���롾"+msProductSerialNumber+"��,���ǡ�����������ɨ����ȷ�����룡");
									return false;
							 }
						 }
						

						sResult = db.GetAnalysisDatabyProductSerialNumber(msProductOrderId,msProductId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"��������",lsAnalysisData);
						if (sResult != "") {
							MESCommon.showMessage(WIP_ANALYSISITEM.this, sResult);
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
							}else//������ֵ�����ı�
							{
								hs.put("FINALVALUE", "OK");
								hs.put("DISPLAYVALUE",lsAnalysisData.get(i).get("DEFAULTSVALUE").toString().trim());
								hs.put("DATAVALUE", lsAnalysisData.get(i).get("DATAVALUE").toString());
							}				
								
							lsAnalysisTable.add(hs);
							}
							adapter.notifyDataSetChanged();
							
						setFocus(editMaterialName)	;	
						}
				     }						

					} catch (Exception e) {
						MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
						setFocus(editMaterialName);
						return false;
					}
				}
					
				return false;
			}
		});
		editMaterialName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
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
					
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{		
								MESCommon.show(WIP_ANALYSISITEM.this, "�����롾"+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"��������");				
								return;
							}
						}						
					}

			 	   if(sResult.equals("���ϸ�"))
					{
						
						AlertDialog alert=	new AlertDialog.Builder(WIP_ANALYSISITEM.this).setTitle("ȷ��").setMessage("���鲻�ϸ�,�Ƿ�ȷ�ϼ���������")
								.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("���ϸ�", lsAnalysisTable, lsAnalysisData);
				            	//ִ�������ж�
					            db.FinalSaveData(msAnalysisformsID,msSampletimes,"���ϸ�",MESCommon.UserId ,MESCommon.UserName,"");
								Toast.makeText(WIP_ANALYSISITEM.this, "����ɹ�!", Toast.LENGTH_SHORT).show();
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
						//�����¼
					if (InsertPROCESS_STEP_P_ANALYSISITEM()) {
					 Save(sResult, lsAnalysisTable, lsAnalysisData);
						//ִ�������ж�
		         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
	                  Toast.makeText(WIP_ANALYSISITEM.this, "����ɹ�!", Toast.LENGTH_SHORT).show();		                  
	                  Clear();
					}
					}		 	   
				} catch (Exception e) {
					MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
				}
			}
		});

		
		// btnExit
		Button btnExit = (Button) findViewById(R.id.wipanalysisitem_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
				}
			}
		});
		

} catch (Exception e) {
	MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
}
	}


	public static class wiptrackinpreAdapter extends BaseAdapter {
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// ������
		private Context context;
		// �������벼��
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
	
	
	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wipanalysisitem_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wipanalysisitem_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wipanalysisitem_tab2_1);

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
			MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
		}
	}
	
	private void Save(String sResult,List<HashMap<String, String>> listTemp,List<HashMap<String, String>> listAnalysisTemp) {
		try {
			List<HashMap<String, String>> lswaitlist=new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> lsResule_M=new ArrayList<HashMap<String, String>>();
		
			String sSQL = "SELECT * FROM ANALYSISWAITLIST where ANALYSISFORMSID='" + msAnalysisformsID + "'";
             String sError = db.GetData(sSQL, lswaitlist);
			 if (sError != "") {
				MESCommon.showMessage(WIP_ANALYSISITEM.this, sError);
			 }
             sSQL = "SELECT * FROM ANALYSISRESULT_M where ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "'";
             sError = db.GetData(sSQL, lsResule_M);
			 if (sError != "") {
				MESCommon.showMessage(WIP_ANALYSISITEM.this, sError);
			 }
			 sSQL="";
             if (lsResule_M.size() > 0)
             {
                 sSQL = "UPDATE ANALYSISRESULT_M SET CHIEFANALYSISUSERID='" +MESCommon.UserId + "', CHIEFANALYSISUSER='" +MESCommon.UserName + "', CHIEFANALYSISTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ,ANALYSISJUDGEMENTRESULT='" + sResult + "',DATACOMPLETESTATUS='�����',MODIFYUSERID='" + MESCommon.UserId + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME=convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)  , TOREMENBER=''  WHERE   ANALYSISFORMSID='" + msAnalysisformsID + "' AND SAMPLETIMES='" + msSampletimes + "' ;";
                
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
         		if(listTemp.get(i).get("RESULTTYPE").toString().equals("��ֵ"))
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
         	//ִ�������ж�
         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
         	 Toast.makeText(WIP_ANALYSISITEM.this, "�������!", Toast.LENGTH_SHORT).show();
             
            
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
		}
	}


	private  Boolean InsertPROCESS_STEP_P_ANALYSISITEM()
	{
		try {
			if(lsCompID.size()>0)
			{ 
				lsSysid.clear();
				String sSql = "SELECT " + MESCommon.SysId + " AS SYSID";
				String sresult = db.GetData(sSql, lsSysid);
			    sSql="";

			    	sSql =  "INSERT INTO PROCESS_STEP_P_ANALYSISITEM (SYSID,ANALYSISFORMSID, PRODUCTORDERID, PRODUCTCOMPID, STEPID, EQPID, PRODUCTSERIALNUMBER, PROCESS_PRODUCTID, PROCESS_PRODUCTNAME, MATERIALMAINTYPE,MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( '"
                            + lsSysid.get(0).get("SYSID").toString() + "','"
                            + msAnalysisformsID + "','"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msStepId+ "','"
                            + msEqpId+ "','"
                            + msProductSerialNumber + "','"
                            + lsCompID.get(0).get("MATERIALID").toString() + "','"
                            + lsCompID.get(0).get("MATERIALNAME").toString() + "','"
                            + lsCompID.get(0).get("MATERIALMAINTYPE").toString() + "','"                        
                            + MESCommon.UserId + "','"
                            +MESCommon.UserName + "',"
                           + MESCommon.ModifyTime + ");";
			   String  sError= db.ExecuteSQL(sSql);
			 if (sError != "") {
					MESCommon.showMessage(WIP_ANALYSISITEM.this, sError);
					return false; 
				 }
			    
		     }
			return true; 
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
			return false; 
		}
	}
	

	public void Clear() {
		try {
			msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			lsAnalysisTable .clear();
            lsCompID .clear();
     
            adapter.notifyDataSetChanged();        
            setFocus(editInput);
            editInput.setText("");
            editMaterialName.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_ANALYSISITEM.this, e.toString());
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
	
	//�˷���ֻ�ǹر������
	private void hintKbTwo() {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
		 if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		}
}