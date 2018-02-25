package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;

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

public class WIP_CompReplace extends Activity {

	private MESDB db = new MESDB();

	//ListView lv0;
	Button btnConfirm, btnExit;
	EditText editInput,editSEQ,editProductserialnumber,editMaterialID,editMaterialName,editNewMaterialID,editNewMaterialName;
	TextView  h7,h8,h9,h10;
	//wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="", msOldSEQ="",  msProductId,  msProductCompId,  msProductSerialNumber,  msStepId="",  msISCOMPID3="",
	  msEqpId="",msAnalysisformsID,msSampletimes,msStepSEQ="",msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE,msBomflag="";
	int miQty;
	// �����ϵ�HashMap��¼
	static int milv0RowNum = 0;

	private List<HashMap<String, String>> lsTable = new ArrayList<HashMap<String, String>>();

	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	 List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsodtBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_p = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_pf = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDoubleMsg = new ArrayList<HashMap<String, String>>();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_compreplace);

		// ȡ�ÿؼ�
		
			try{
		editInput = (EditText) findViewById(R.id.wipcompreplace_tvnewProductserialnumber);	
	
		editNewMaterialName = (EditText) findViewById(R.id.wipcompreplace_tvnewMaterialName);	
		editNewMaterialID = (EditText) findViewById(R.id.wipcompreplace_tvnewMaterialid);	
		editProductserialnumber = (EditText) findViewById(R.id.wipcompreplace_tvProductserialnumber);	
		editSEQ = (EditText) findViewById(R.id.wipcompreplace_tvSEQ);	
		editMaterialName = (EditText) findViewById(R.id.wipcompreplace_tvMaterialName);	
		editMaterialID = (EditText) findViewById(R.id.wipcompreplace_tvMaterialid);	
		
		btnConfirm = (Button) findViewById(R.id.wipcompreplace_btnConfirm);	
		btnExit = (Button) findViewById(R.id.wipcompreplace_btnExit);			
		editMaterialName = (EditText) findViewById(R.id.wipcompreplace_tvMaterialName);	
		editMaterialID = (EditText) findViewById(R.id.wipcompreplace_tvMaterialid);	
		// ***********************************************Start
		prefercesService  =new PrefercesService(this);  
	    params=prefercesService.getPreferences();  
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("������Ա��"+MESCommon.UserName); 
	    actionBar.setTitle("�㲿���޸�");
		
	    Intent intent = this.getIntent();
	    String MaterialId = intent.getStringExtra("MaterialId");
	    String MaterialMame = intent.getStringExtra("MaterialMame"); 
	    String PRODUCTSERIALNUMBER = intent.getStringExtra("PRODUCTSERIALNUMBER"); 
	    msOldSEQ = intent.getStringExtra("SEQ"); 
	    msProductOrderId= intent.getStringExtra("ProductOrderId"); 
	    msProductId= intent.getStringExtra("ProductId"); 
	    msProductCompId	= intent.getStringExtra("ProductCompId"); 
	    msISCOMPID3= intent.getStringExtra("ISCOMPID3"); 
	    editProductserialnumber.setText(PRODUCTSERIALNUMBER) ;	
		editMaterialName.setText(MaterialMame) ;	
		editMaterialID .setText(MaterialId) ;	
		setFocus(editInput);
				
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// ��ѯ������
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipcompreplace_tvnewProductserialnumber);
					String   sResult="";
					String   sMaterialId = "";
					String   sMaterialMame ="";
					String   sMaterialType = "";
					String   sRAWPROCESSID="";
					String   sFINEPROCESSID="";
					String   sSUPPLYID="";
					String   sLNO="";
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_CompReplace.this, "��ɨ������!");
						txtInput.setText("");
						setFocus(editNewMaterialName);
						return false;
					}
					lsCompID.clear();
					if(msISCOMPID3.equals("Y"))
					{
						sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","�������","װ��", lsCompID);
					    if (!sResult.equals(""))
	                    {   MESCommon.show(WIP_CompReplace.this,sResult);
	                        setFocus(editNewMaterialName);
					    	return false;
	                    }
					    //COMPID3��Ϊ�գ���ʾ�Ѿ�������/˫��/��������������
					    if (!lsCompID.get(0).get("COMPID3").toString().equals("") && lsCompID.get(0).get("COMPID3").toString().equals(txtInput.getText().toString().trim()) )
			            {
		        		  MESCommon.show(WIP_CompReplace.this,"�����滻����Ϊ������/˫��/�����������룺��"+txtInput.getText().toString().trim()+"����");
		        		  txtInput.setText("");
		        		  setFocus(editNewMaterialName);
		                  return false;
			            }
					    lsDoubleMsg.clear();
					    //PROCESSSTATUS<>'�����' δ��Ӧ�ò������ɵ�˫�ζ���
						String sSQL = "SELECT B.PRODUCTID,C.PRODUCTNAME,D.LEVEL2 FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID INNER JOIN MPRODUCT C ON B.PRODUCTID=C.PRODUCTID LEFT JOIN MSTKINVMAS D ON B.PRODUCTID=D.ITNBR  WHERE A.PRODUCTCOMPID='"+ lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString() + "'  ";
						String sError = db.GetData(sSQL, lsDoubleMsg);
						if (sError != "") {
							MESCommon.showMessage(WIP_CompReplace.this, sError);
							return false;
						}
					   txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					   sMaterialId = lsDoubleMsg.get(0).get("PRODUCTID").toString();
					   sMaterialMame = lsDoubleMsg.get(0).get("PRODUCTNAME").toString();
					   sMaterialType =lsDoubleMsg.get(0).get("LEVEL2").toString();
					   sRAWPROCESSID=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
					   sFINEPROCESSID=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
					   sSUPPLYID="HZ";
					   sLNO="";					 
					}
					else
					{
						sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","�㲿��","װ��", lsCompID);
					    if (!sResult.equals(""))
	                    {   MESCommon.show(WIP_CompReplace.this,sResult);
	                        setFocus(editNewMaterialName);
					    	return false;
	                    }
			        	if (lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("Y") )
			            {
		        		  MESCommon.show(WIP_CompReplace.this,"�����滻�������!");
		        		  txtInput.setText("");
		        		  setFocus(editNewMaterialName);
		                  return false;
			            }
	                    
						if(lsCompID.size()>0)
						{
							if (!lsCompID.get(0).get("FINEPROCESSID").toString().equals("")) {	
							
								if(!lsCompID.get(0).get("MATERIALID").toString().equals(""))
								{
							      String sFinId=getCompid(sMaterialId);
							      if(!sFinId.equals(""))
							      {
							    	  if(sFinId.equals("���Ʒ���ͼ�"))
							    	  {
							    			txtInput.setText(lsCompID.get(0).get("RAWPROCESSID").toString());
							    	  }else {
							    			txtInput.setText(lsCompID.get(0).get("FINEPROCESSID").toString());
									}
							    		  
							      }
								}else {
									txtInput.setText(lsCompID.get(0).get("FINEPROCESSID").toString());
								}
										
							}else  if (!lsCompID.get(0).get("RAWPROCESSID").toString().equals(""))
							{
								txtInput.setText(lsCompID.get(0).get("RAWPROCESSID").toString());
							}
							else if (!lsCompID.get(0).get("LOTID").toString().equals(""))
							{
								txtInput.setText(lsCompID.get(0).get("LOTID").toString());
							}
						}
						   sMaterialId = lsCompID.get(0).get("MATERIALID").toString();
						   sMaterialMame = lsCompID.get(0).get("MATERIALNAME").toString();
						   sMaterialType = lsCompID.get(0).get("MATERIALMAINTYPE").toString();
						   sRAWPROCESSID=lsCompID.get(0).get("RAWPROCESSID").toString();
						   sFINEPROCESSID=lsCompID.get(0).get("FINEPROCESSID").toString();
						   sSUPPLYID=lsCompID.get(0).get("SUPPLYID").toString();
						   sLNO=lsCompID.get(0).get("FURNACENO").toString();
					}
					String sCheckResult="";
					if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
					{
						sCheckResult=checkExist(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
					}
					if(!sCheckResult.equals(""))
					{
						MESCommon.show(WIP_CompReplace.this,sCheckResult);
						 return false;
					}					
							
					lsBom.clear();
					List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
					String sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
					String sError = db.GetData(sSQL, lscheckERPBOMSystem);
					if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
					{
						sSQL = "SELECT * FROM ERP_MBOM WHERE PRODUCTID ='" +msProductId + "' AND PRODUCTORDERID = '" + msProductOrderId + "' AND MATERIALID = '" + sMaterialId + "'";
					    sResult = db.GetData(sSQL, lsBom);
						if (sResult != "") {
							MESCommon.showMessage(WIP_CompReplace.this, sResult);
							
						} 
						if(lsBom.size()==0)
						{

							AlertDialog alert=	new AlertDialog.Builder(WIP_CompReplace.this).setTitle("ȷ��").setMessage("�������롾" +editInput.getText().toString() + "�������ϡ�"+sMaterialId+"�� ���������嵥!,�Ƿ�����滻!")
									.setPositiveButton("����",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					                // TODO Auto-generated method stub  			
					            	msBomflag="N";
					            	editNewMaterialID.setText(lsCompID.get(0).get("MATERIALID").toString());	
						            editNewMaterialName.setText(lsCompID.get(0).get("MATERIALNAME").toString());	
									editSEQ.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
									setFocus(editNewMaterialName);		
					            }  
					        })  
							.setNeutralButton("ȡ��",new DialogInterface.OnClickListener() {  
							            @Override  
							            public void onClick(DialogInterface dialog,int which) {  
							                // TODO Auto-generated method stub  
							            	editInput.setText("");
											setFocus(editNewMaterialName);	
							            	return  ;
							            }  
							 }).show();
						}else
						{
							editNewMaterialID.setText(sMaterialId);	
				            editNewMaterialName.setText(sMaterialMame);	
							editSEQ.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
							setFocus(editNewMaterialName);		
						}
					}else {
						editNewMaterialID.setText(sMaterialId);	
			            editNewMaterialName.setText(sMaterialMame);	
						editSEQ.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						setFocus(editNewMaterialName);				
					}					
					} catch (Exception e) {
						MESCommon.showMessage(WIP_CompReplace.this, e.toString());
						return false;
					}
				}
					
				return false;
			}
		});
	
		editNewMaterialName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				setFocus(editInput);
			}
		});
	

		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
					if(editSEQ.getText().toString().trim().equals(""))
					{
						MESCommon.showMessage(WIP_CompReplace.this, "����ɨ��Ҫ�滻�������ǩ���ڽ��������滻��");
						return ;
					}
				    String sResult=	UpdateSTEP_P();
					if(!sResult.equals(""))
					{	
						MESCommon.showMessage(WIP_CompReplace.this, sResult);
						return ;
					}
					close(v,"OK");
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompReplace.this, e.toString());
				}
			}
		});

        // btnExit
		Button btnExit = (Button) findViewById(R.id.wipcompreplace_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					 finish(); // �ر�Activity  
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompReplace.this, e.toString());
				}
			}
		});
			} catch (Exception e) {
				MESCommon.showMessage(WIP_CompReplace.this, e.toString());
			}
	}

	
	
	public void close(View v,String result)  
	{  
	 Intent intent = new Intent();  
	 intent.putExtra("result", result);  
	 this.setResult(RESULT_OK, intent); // ���ý������  
	 this.finish(); // �ر�Activity  
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
			MESCommon.showMessage(WIP_CompReplace.this, e.toString());
			return e.toString();
		}
	}
	
	/// <summary>
    /// �滻����
    /// </summary>
    /// <returns></returns>
    private String UpdateSTEP_P()
    {
    	String sResult="";       
    	String sSql = "";
        try
        {
        	//�����滻��������AND PRODUCTORDERID='"+msProductOrderId+"'����Ϊ������������ڿ�
        	//��Ϊ���ع��������Ҫ������������ϡ����ˡ�
            List<HashMap<String, String>> lsoDtOLD_P = new ArrayList<HashMap<String, String>>();
            //��ѯ��ǰ���ϵĹ���OLDID
            sSql = " SELECT * FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER='" +msOldSEQ+ "'  AND PRODUCTCOMPID='" +msProductCompId+ "'   ;";
            db.GetData(sSql,lsoDtOLD_P);

            List<HashMap<String, String>> lsoDt = new ArrayList<HashMap<String, String>>();
            //�������б�ʾ���������豣��������
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE PRODUCTSERIALNUMBER='" +msOldSEQ + "'  AND  SERIALNUMBER_P ='" +msOldSEQ + "' ;";
            db.GetData(sSql,lsoDt);
          
            //�滻�����Ǵ�������װ�õ�
            if (lsoDt.size() > 0)
            {
                sSql = "";
                //��Ҫ���¡�P��-PF���µ�������Ϣ��
                //�ѱ��滻-PF�����ϲ���PROCESS_STEP_REPLACE����PRODUCTSERIALNUMBERΪ��������������Ѷ�ʶ����Ƶ�PROCESS_STEP_REPLACE��                 
                sSql = sSql + "INSERT INTO PROCESS_STEP_REPLACE (SYSID,PRODUCTORDERID,PRODUCTCOMPID,STEPID,EQPID,PRODUCTSERIALNUMBER_OLD,  PRODUCTSERIALNUMBER, PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,MODIFYUSERID,MODIFYUSER,MODIFYTIME) " +
                       "SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID,  STEPID,  EQPID, PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER , PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, MODIFYUSERID, MODIFYUSER, convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) MODIFYTIME FROM PROCESS_STEP_P " +
                       "WHERE   PRODUCTSERIALNUMBER ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId+ "'  ;";

                sSql = sSql + "INSERT INTO PROCESS_STEP_REPLACE (SYSID,PRODUCTORDERID,PRODUCTCOMPID,STEPID,EQPID,PRODUCTSERIALNUMBER_OLD, SERIALNUMBER_OLD_P, PRODUCTSERIALNUMBER, SERIALNUMBER_P,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,MODIFYUSERID,MODIFYUSER,MODIFYTIME) " +
                       "SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID,  STEPID,  EQPID, PRODUCTSERIALNUMBER_OLD ,SERIALNUMBER_OLD_P, PRODUCTSERIALNUMBER,SERIALNUMBER_P, PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, MODIFYUSERID, MODIFYUSER, convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) MODIFYTIME FROM PROCESS_STEP_PF " +
                       "WHERE   SERIALNUMBER_P ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId + "'   ;";


                //ɾ�����滻P��������Ϣ
                sSql = sSql + " DELETE PROCESS_STEP_P  WHERE   PRODUCTSERIALNUMBER ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId + "'   ;";
                //ɾ�����滻PF��������Ϣ
                sSql = sSql + " DELETE PROCESS_STEP_PF  WHERE   PRODUCTSERIALNUMBER ='" +msOldSEQ + "' AND SERIALNUMBER_P ='" +msOldSEQ + "' AND PRODUCTCOMPID='" + msProductCompId + "'   ;";

                //�����滻��P�����ϱ��
                sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID,PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER ,PROCESS_PRODUCTID, PROCESS_PRODUCTIDTYPE, PROCESS_PRODUCTNAME, MATERIALMAINTYPE, ISCHIEFCOMP,TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) " +
                     " VALUES (" +MESCommon.SysId+ ", '" + msProductOrderId + " ', '" + msProductCompId + "','" + msProductId + "', 'װ����Ϣ','' ,'','" +editProductserialnumber.getText().toString().trim()+ "', '" +editSEQ.getText().toString().trim()+ "', '" +lsCompID.get(0).get("MATERIALID").toString() + "','', '" + lsCompID.get(0).get("MATERIALNAME").toString() + "', " +
                     " '" + lsCompID.get(0).get("MATERIALMAINTYPE").toString() + "', 'N', '" + lsCompID.get(0).get("TRACETYPE").toString() + "', '" + lsCompID.get(0).get("LOTID").toString() + "','" + lsCompID.get(0).get("RAWPROCESSID").toString() + "', '" + lsCompID.get(0).get("FINEPROCESSID").toString() + "', '" + lsCompID.get(0).get("SUPPLYID").toString() + "', '" + lsCompID.get(0).get("FURNACENO").toString() + "','"+msBomflag+"', '" +MESCommon.UserId+ "', '" + MESCommon.UserName + "', " + MESCommon.ModifyTime + ") ;";

                //�����滻��PF�����ϱ�� 
                sSql = sSql + " INSERT INTO PROCESS_STEP_PF (SYSID, PRODUCTORDERID, PRODUCTCOMPID, STEPID, EQPID,PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER,SERIALNUMBER_OLD_P, SERIALNUMBER_P, PROCESS_PRODUCTID, PROCESS_PRODUCTNAME, MATERIALMAINTYPE,TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) " +
                              " VALUES (" + MESCommon.SysId + ", '" + msProductOrderId + " ', '" + msProductCompId + "', 'װ����Ϣ', '','" + editProductserialnumber.getText().toString().trim() + "','" +  editSEQ .getText().toString().trim()+ "', '" +  lsoDt.get(0).get("SERIALNUMBER_OLD_P").toString().trim() + "', '" +editSEQ .getText().toString().trim() + "', '" + lsCompID.get(0).get("MATERIALID").toString() + "', '" + lsCompID.get(0).get("MATERIALNAME").toString() + "', " +
                              " '" + lsCompID.get(0).get("MATERIALMAINTYPE").toString().trim() + "', '" + lsCompID.get(0).get("TRACETYPE").toString() + "', '" + lsCompID.get(0).get("LOTID").toString() + "','" + lsCompID.get(0).get("RAWPROCESSID").toString() + "', '" + lsCompID.get(0).get("FINEPROCESSID").toString() + "', '" + lsCompID.get(0).get("SUPPLYID").toString() + "', '" + lsCompID.get(0).get("FURNACENO").toString() + "', '"+msBomflag+"','" + MESCommon.UserId + "', '" + MESCommon.UserName + "', " +MESCommon.ModifyTime + ") ;";
                //��ѯҪ�滻�ġ�PF�����װ��������Ϣ
              
                  
                //�����滻��ָ��������
                sSql = sSql + " UPDATE  PROCESS_STEP_PF SET PRODUCTORDERID='" + msProductOrderId + "',  PRODUCTCOMPID ='" + msProductCompId + "',MODIFYUSERID ='" + MESCommon.UserId  + "', MODIFYUSER='" + MESCommon.UserName + "', MODIFYTIME= " + MESCommon.ModifyTime + ",   PRODUCTSERIALNUMBER_OLD='" + editProductserialnumber.getText().toString().trim() + "'  WHERE  PRODUCTSERIALNUMBER ='" + editSEQ.getText().toString().trim() + "' ;";
                    
                
            }
            //��ʾ���滻����ѹ�ƺ�׷������
            else
            {
            	 List<HashMap<String, String>> lsoDt_PF = new  ArrayList<HashMap<String, String>>();
                //ֻ��SERIALNUMBER_P�б�ʾ�ǲ�׷�ٵ�����
                sSql = "SELECT * FROM PROCESS_STEP_PF WHERE   SERIALNUMBER_P ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId+ "'   ;";
                db.GetData(sSql,lsoDt_PF);
                sSql = "";
                if (lsoDt_PF.size() > 0)
                {
                    //����滻����ѹ�ƺ�����ϣ���Ҫ��ѯ�滻�������Ƿ���ѹ�Ƶ����ϡ����������Ҫͬʱ���µ�P��
                    //�ѱ��滻-PF�����ϲ���PROCESS_STEP_REPLACE����PRODUCTSERIALNUMBERΪ��������������Ѷ�ʶ����Ƶ�PROCESS_STEP_REPLACE��                 
                    sSql = sSql + "INSERT INTO PROCESS_STEP_REPLACE (SYSID,PRODUCTORDERID,PRODUCTCOMPID,STEPID,EQPID,PRODUCTSERIALNUMBER_OLD, SERIALNUMBER_OLD_P, PRODUCTSERIALNUMBER, SERIALNUMBER_P,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,MODIFYUSERID,MODIFYUSER,MODIFYTIME) " +
                          "SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID,  STEPID,  EQPID, PRODUCTSERIALNUMBER_OLD ,SERIALNUMBER_OLD_P, PRODUCTSERIALNUMBER,SERIALNUMBER_P, PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, MODIFYUSERID, MODIFYUSER, convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) MODIFYTIME FROM PROCESS_STEP_PF " +
                          "WHERE   SERIALNUMBER_P ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId+ "'    ;";

                    //ɾ�����滻PF��������Ϣ
                    sSql = sSql + " DELETE PROCESS_STEP_PF  WHERE   SERIALNUMBER_P ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId + "'   ;";

                    //�����滻��PF�����ϱ�� 
                    sSql = sSql + " INSERT INTO PROCESS_STEP_PF (SYSID, PRODUCTORDERID, PRODUCTCOMPID, STEPID, EQPID,PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER,SERIALNUMBER_OLD_P, SERIALNUMBER_P, PROCESS_PRODUCTID, PROCESS_PRODUCTNAME, MATERIALMAINTYPE,TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) " +
                                  " VALUES (" +MESCommon.SysId + ", '" + msProductOrderId + " ', '" + msProductCompId + "', 'װ����Ϣ', '','" + editProductserialnumber.getText().toString().trim() + "','" + lsoDt_PF.get(0).get("PRODUCTSERIALNUMBER").toString().trim() + "', '" +  editProductserialnumber.getText().toString().trim()+ "', '" +editSEQ .getText().toString().trim() + "', '" + lsCompID.get(0).get("MATERIALID").toString() + "', '" + lsCompID.get(0).get("MATERIALNAME").toString() + "', " +
                                  " '" + lsCompID.get(0).get("MATERIALMAINTYPE").toString().trim() + "', '" + lsCompID.get(0).get("TRACETYPE").toString() + "', '" + lsCompID.get(0).get("LOTID").toString() + "', '" + lsCompID.get(0).get("RAWPROCESSID").toString() + "', '" + lsCompID.get(0).get("FINEPROCESSID").toString() + "', '" + lsCompID.get(0).get("SUPPLYID").toString() + "', '" + lsCompID.get(0).get("FURNACENO").toString() + "', '"+msBomflag+"','" +MESCommon.UserId + "', '" + MESCommon.UserName + "', " + MESCommon.ModifyTime + ") ;";
                }
                //��ʾ��PROCESS_STEP_P���汻�滻�ġ�
                else
                {
                	if( lsCompID.get(0).get("MATERIALNAME").toString().contains("����"))
					{
					  sResult=UpdateSTKCOMP(msProductCompId,editInput.getText().toString().trim());
					  if(!sResult.equals(""))
						{
							return  sResult;
						}
					}
                    sSql = sSql + "INSERT INTO PROCESS_STEP_REPLACE (SYSID,PRODUCTORDERID,PRODUCTCOMPID,STEPID,EQPID,PRODUCTSERIALNUMBER_OLD, SERIALNUMBER_OLD_P, PRODUCTSERIALNUMBER, SERIALNUMBER_P,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID,FINEPROCESSID,SUPPLYID,LNO,MODIFYUSERID,MODIFYUSER,MODIFYTIME) " +
                           "SELECT SYSID, PRODUCTORDERID, PRODUCTCOMPID,  STEPID,  EQPID, PRODUCTSERIALNUMBER_OLD, '',PRODUCTSERIALNUMBER,'' ,PROCESS_PRODUCTID,PROCESS_PRODUCTNAME, MATERIALMAINTYPE,RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, MODIFYUSERID, MODIFYUSER, convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) MODIFYTIME FROM PROCESS_STEP_P " +
                           "WHERE   PRODUCTSERIALNUMBER ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId + "'   ;";

                    //ɾ�����滻P��������Ϣ
                    sSql = sSql + " DELETE PROCESS_STEP_P  WHERE   PRODUCTSERIALNUMBER ='" +msOldSEQ + "'  AND PRODUCTCOMPID='" +msProductCompId + "'   ;";

                    //�����滻��P�����ϱ��

                    if(msISCOMPID3.equals("Y"))
                    {
                    sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID,PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER, PROCESS_PRODUCTID, PROCESS_PRODUCTIDTYPE, PROCESS_PRODUCTNAME, MATERIALMAINTYPE, ISCHIEFCOMP,TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,ISCOMPID3,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) " +
                                  " VALUES (" +MESCommon.SysId + ", '" + msProductOrderId + " ', '" + msProductCompId + "','" + msProductId + "', 'װ����Ϣ','' ,'','" + editProductserialnumber.getText().toString().trim() + "', '" + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString().trim() + "', '" + lsDoubleMsg.get(0).get("PRODUCTID").toString() + "','', '" + lsDoubleMsg.get(0).get("PRODUCTNAME").toString() + "', " +
                                  " '" + lsDoubleMsg.get(0).get("LEVEL2").toString() + "', 'N', '" + lsCompID.get(0).get("TRACETYPE").toString() + "', '','" + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString().trim() + "', '" +lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString().trim() + "', 'HZ', '','Y' ,'"+msBomflag+"','" + MESCommon.UserId + "', '" + MESCommon.UserName + "', " +MESCommon.ModifyTime + ") ;";
              
                    }else {
                    	 sSql = sSql + "INSERT INTO PROCESS_STEP_P (SYSID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTID, STEPID, STEPSEQ, EQPID,PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER, PROCESS_PRODUCTID, PROCESS_PRODUCTIDTYPE, PROCESS_PRODUCTNAME, MATERIALMAINTYPE, ISCHIEFCOMP,TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO,BOMFLAGE, MODIFYUSERID, MODIFYUSER, MODIFYTIME) " +
                                 " VALUES (" +MESCommon.SysId + ", '" + msProductOrderId + " ', '" + msProductCompId + "','" + msProductId + "', 'װ����Ϣ','' ,'','" + editProductserialnumber.getText().toString().trim() + "', '" + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString().trim() + "', '" + lsCompID.get(0).get("MATERIALID").toString() + "','', '" + lsCompID.get(0).get("MATERIALNAME").toString() + "', " +
                                 " '" + lsCompID.get(0).get("MATERIALMAINTYPE").toString() + "', 'N', '" + lsCompID.get(0).get("TRACETYPE").toString() + "', '" + lsCompID.get(0).get("LOTID").toString() + "','" + lsCompID.get(0).get("RAWPROCESSID").toString() + "', '" + lsCompID.get(0).get("FINEPROCESSID").toString() + "', '" + lsCompID.get(0).get("SUPPLYID").toString() + "', '" + lsCompID.get(0).get("FURNACENO").toString() + "', '"+msBomflag+"','" + MESCommon.UserId + "', '" + MESCommon.UserName + "', " +MESCommon.ModifyTime + ") ;";
                          
					}
                }
            }
           db.ExecuteSQL(sSql);
           return sResult;
        }
        catch (Exception ex)
        {
            return sResult=ex.toString();
        }
    }
    public String UpdateSTKCOMP(String  sPRODUCTCOMPID,  String sCOMPID) {
		String sResult = "";
		String sSQL="";
		try {

			List<HashMap<String, String>> lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> lscheckERPBOMSystem = new ArrayList<HashMap<String, String>>();
			List<HashMap<String, String>> lscheckExistSTKCOMP = new ArrayList<HashMap<String, String>>();
			lscheckExistSTKCOMP.clear();
			if(!msISCOMPID3.equals("Y"))
			{
				 sSQL= "SELECT * FROM STKCOMP WHERE  COMPIDSEQ='" + sCOMPID + "' OR COMPID='"+ sCOMPID + "' OR COMPID2='"+ sCOMPID + "'";		
				 sResult = db.GetData(sSQL, lscheckExistSTKCOMP);
				if (sResult != "") {
					MESCommon.showMessage(WIP_CompReplace.this, sResult);
					return sResult = sResult;
				}
				if(lscheckExistSTKCOMP.size()>0)
				{
					sCOMPID=lscheckExistSTKCOMP.get(0).get("COMPIDSEQ").toString();
				}
			}
			
			sSQL = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER  WHERE PARAMETERID='CHECK_ERPBOM'   ";
			sResult = db.GetData(sSQL, lscheckERPBOMSystem);
			if (sResult != "") {
				MESCommon.showMessage(WIP_CompReplace.this, sResult);
				return sResult = sResult;
			}
			//���BOM���趨���ϱ�־
			if(lscheckERPBOMSystem.get(0).get("PARAMETERVALUE").toString().equals("Y"))
			{	lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.clear();
				sSQL = "SELECT * FROM PROCESS_PRODUCTORDER_PRINTBARCODE WHERE PRODUCTCOMPID ='" + sPRODUCTCOMPID + "' AND PRODUCTORDERID='"+msProductOrderId+"' ";
				sResult = db.GetData(sSQL, lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE);	
				if (sResult != "") {
					MESCommon.showMessage(WIP_CompReplace.this, sResult);
					return sResult = sResult;
				}
			    if(msISCOMPID3.equals("Y"))
			    {
					List<HashMap<String, String>> lsDoubleProduct = new ArrayList<HashMap<String, String>>();
					sSQL = "SELECT B.PRODUCTID FROM PROCESS A INNER JOIN PROCESS_PRE B ON A.PRODUCTORDERID=B.PRODUCTORDERID WHERE  A.PRODUCTCOMPID='"+ sCOMPID + "' AND A.PRODUCTORDERID='"+msProductOrderId+"' ";
					sResult = db.GetData(sSQL, lsDoubleProduct);	
					if (sResult != "") {
						MESCommon.showMessage(WIP_CompReplace.this, sResult);
						return sResult = sResult;
					}
					  if (!lsDoubleProduct.get(0).get("PRODUCTID").toString().trim().equals(lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString()) ) {
							return sResult = "�û��������Ϻţ�"+lsDoubleProduct.get(0).get("PRODUCTID").toString().trim() + " �������趨�������Ϻţ�" + lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString()+ "����ͬ����ȷ�ϣ�";
						   }
			    }
				else 
				{
				  if (!lscheckExistSTKCOMP.get(0).get("ITNBR2").toString().trim().equals(lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString()) && !lscheckExistSTKCOMP.get(0).get("ITNBR").toString().trim().equals(lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString())) {
					if(!lscheckExistSTKCOMP.get(0).get("ITNBR2").toString().trim().equals(""))
					{
					  return sResult = "�û��������Ϻţ�"+ lscheckExistSTKCOMP.get(0).get("ITNBR2").toString().trim() + " �������趨�������Ϻţ�" + lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString()+ "����ͬ����ȷ�ϣ�";
					} 
					else if(!lscheckExistSTKCOMP.get(0).get("ITNBR").toString().trim().equals(""))
					{
						  return sResult = "�û��������Ϻţ�"+ lscheckExistSTKCOMP.get(0).get("ITNBR").toString().trim() + " �������趨�������Ϻţ�" + lsoDtPROCESS_PRODUCTORDER_PRINTBARCODE.get(0).get("PROCESS_PRODUCTID").toString()+ "����ͬ����ȷ�ϣ�";
					}
				  }
			    }
			}
		   if(msISCOMPID3.equals("Y"))
		    {
			   sSQL = "UPDATE STKCOMP SET COMPID3=''  WHERE COMPID3='" + sPRODUCTCOMPID + "' ;";
			   sSQL = sSQL+ "UPDATE STKCOMP SET COMPID3='"+sPRODUCTCOMPID+"'  WHERE PRODUCTCOMPID='" +sCOMPID  + "' ;";
		    }else {
			   sSQL = "UPDATE STKCOMP SET PRODUCTCOMPID=''  WHERE PRODUCTCOMPID='" + sPRODUCTCOMPID + "' ;";
			   sSQL = sSQL+ "UPDATE STKCOMP SET PRODUCTCOMPID='"+sPRODUCTCOMPID+"'  WHERE COMPIDSEQ='" +sCOMPID  + "' ;";
			}
		    sResult = db.ExecuteSQL(sSQL);
			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompReplace.this, e.toString());
			return sResult = e.toString();
		}

	}
	public void Clear() {
		try {
		
     		msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";msBomflag="";
			miQty=0;
            lsCompID .clear();lsProcess .clear(); 
            lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();
            msISCOMPID3="";
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_CompReplace.this, e.toString());
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
	
	
	
	public String checkExist(String sSerialNumber)
	{
		String sResult="";
		String sSql="";
		List<HashMap<String, String>> lsExistcheck=new ArrayList<HashMap<String, String>>();
		
		try {
			lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER = '" + sSerialNumber + "' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���װ��ʹ�ã�";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')='' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���������װ��ʹ�ã�";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')!=''";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���װ��ʹ�ã�";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')!=''";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���װ��ʹ�ã�";
            }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompReplace.this, e.toString());
			return sResult=e.toString();
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

