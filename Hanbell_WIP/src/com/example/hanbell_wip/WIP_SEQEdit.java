package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Setting.SpinnerData;



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

public class WIP_SEQEdit extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button  btnExit, btnDelete,btnReplace;
	EditText editInput,editMaterialID;
	TextView  h7,h8,h9,h10;
	CheckBox cb;
	wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	Map<String,String> params;
	String msProductOrderId="",  msProductId,  msProductCompId,  msProductSerialNumber="",msProductSerialNumber_old="" ,msSERIALNUMBER_OLD_P="", msStepId="",  
	  msEqpId="",msAnalysisformsID,msSampletimes,msStepSEQ="",msSUPPLYLOTID,msSUPPLYID,msQC_ITEM,msQCTYPE, msSysid="";
	 
	int miQty;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	 List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsodtBom = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_p = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcessStep_pf = new ArrayList<HashMap<String, String>>();
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_seqedit);
		try{
		// 取得控件
		editInput = (EditText) findViewById(R.id.wipseqedit_tvInput);		
		editMaterialID = (EditText) findViewById(R.id.wipseqedit_tvMaterialid);	
		cb	= (CheckBox) findViewById(R.id.wipseqedit_cb);	
		h7= (TextView) findViewById(R.id.wipseqedit_h7);
		h8= (TextView) findViewById(R.id.wipseqedit_h8);
		h9= (TextView) findViewById(R.id.wipseqedit_h9);
		h10= (TextView) findViewById(R.id.wipseqedit_h10);
		h7.setTextColor(Color.WHITE);
		h7.setBackgroundColor(Color.DKGRAY);
		h8.setTextColor(Color.WHITE);
		h8.setBackgroundColor(Color.DKGRAY);
		h9.setTextColor(Color.WHITE);
		h9.setBackgroundColor(Color.DKGRAY);
		h10.setTextColor(Color.WHITE);
		h10.setBackgroundColor(Color.DKGRAY);
		h10.setBackgroundColor(Color.DKGRAY);
		lv0 = (ListView) findViewById(R.id.wipseqedit_lv0);

		btnDelete = (Button) findViewById(R.id.wipseqedit_btnDelete);	
		btnExit = (Button) findViewById(R.id.wipseqedit_btnExit);			
		adapter = new wiptrackinAdapter(lsCompTable, this);
		lv0.setAdapter(adapter);		
		
		
		// ***********************************************Start
		prefercesService  =new PrefercesService(this);  
	    params=prefercesService.getPreferences();  
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("次组立装配信息");
	    msStepId=params.get("StepID");
		// 取得控件
	 
		String date = sDateFormatShort.format(new java.util.Date());	
	
		// 控件事件
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinAdapter.ViewHolder holder = (wiptrackinAdapter.ViewHolder) arg1.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
			
				wiptrackinAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				milv0RowNum=position;
				if(holder.cb.isChecked())
				{
				  lsCompTable.get(position).put("CHECKFLAG","Y"); 
				}else
				{
					 lsCompTable.get(position).put("CHECKFLAG","N"); 
				}
				for(int i=0;i<lsCompTable.size();i++)
				{ if(i!=position)
					{
				      lsCompTable.get(i).put("CHECKFLAG","N"); 
					}
				}
				adapter.notifyDataSetChanged();
				
			}
		});
		
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipseqedit_tvInput);
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_SEQEdit.this, "请扫描条码!");
						txtInput.setText("");
						setFocus(editMaterialID );
						return false;
					}					
					//刷主件
					if(!cb.isChecked())
					{
						lsCompID.clear();						
						String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","零部件","装配", lsCompID);
					    if (!sResult.equals(""))
	                    {   MESCommon.show(WIP_SEQEdit.this,sResult);
	                        setFocus(editMaterialID);
					    	return false;
	                    }
		            	
						//初始化工件信息					
						editMaterialID.setText(lsCompID.get(0).get("MATERIALNAME").toString());									
						//改变默认的单行模式  
						editMaterialID.setSingleLine(false);  
						//水平滚动设置为False  
						editMaterialID.setHorizontallyScrolling(false);	
						GetProcessSTEP_P();//初始零部件
						editInput.setText("");
						setFocus(editMaterialID);
					}
					//刷零部件
					else
					{	if(lsCompTable.size()==0)
						{
							MESCommon.show(WIP_SEQEdit.this,"没有要加入的次组立组！");
							cb.setChecked(false);
							return false;
						}
						lsCompID.clear();				 
					    String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"",msProductOrderId, "QF", "零部件","装配",lsCompID);
						if(!sResult.equals(""))
						{
							MESCommon.show(WIP_SEQEdit.this,sResult);
							return false;
						}
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						String   sMaterialId = lsCompID.get(0).get("MATERIALID").toString();
						String   sMaterialMame = lsCompID.get(0).get("MATERIALNAME").toString();
						String   sMaterialType = lsCompID.get(0).get("MATERIALMAINTYPE").toString();
						String   sTracetype=lsCompID.get(0).get("TRACETYPE").toString();
						String   sLotID=lsCompID.get(0).get("LOTID").toString();
						String   sRAWPROCESSID=lsCompID.get(0).get("RAWPROCESSID").toString();
						String   sFINEPROCESSID=lsCompID.get(0).get("FINEPROCESSID").toString();
						String   sSUPPLYID=lsCompID.get(0).get("SUPPLYID").toString();
						String   sLNO=lsCompID.get(0).get("FURNACENO").toString();
						String sSEQ="";
						if (!sFINEPROCESSID.equals("")) {
						
							sSEQ=sFINEPROCESSID;
						}else  if (!sRAWPROCESSID.equals(""))
						{sSEQ=sRAWPROCESSID;
				
						}
						else if (!sLotID.equals(""))
						{sSEQ=sLotID;
						
						}
						
						String sCheckResult="";
						if(lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("Y"))
						{
							MESCommon.show(WIP_SEQEdit.this,"装配零部件时,不能装配条码为：【"+txtInput.getText().toString().trim()+"】的制造号码！");
							return false;
						}
						
						if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
						{
							sCheckResult=checkExist(txtInput.getText().toString().trim());
						}
						if(!sCheckResult.equals(""))
						{
							MESCommon.show(WIP_SEQEdit.this,sCheckResult);
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
								MESCommon.showMessage(WIP_SEQEdit.this, sResult);
								
							} 
							if(lsBom.size()==0)
							{
								MESCommon.show(WIP_SEQEdit.this, "物料条码【" +editInput.getText().toString() + "】的物料【"+sMaterialId+"】， 不在物料清单中!");
								editInput.setText("");
								setFocus(editInput);				
								return false ;
							}
						}
						
						List<HashMap<String, String>> lsdt = new ArrayList<HashMap<String, String>>();
						  sSQL = "SELECT PARAMETERVALUE  FROM MSYSTEMPARAMETER WHERE PARAMETERID='STEPCHECKMMT' ";
			            sResult = db.GetData(sSQL, lsdt);
			            if(lsdt.get(0).get("PARAMETERVALUE").toString().equals("Y"))
			            {
							lsodtBom.clear();
						     sSQL = "SELECT  B.PRODUCTID, A.PRODUCTGROUPID,A.STEPID,A.MATERIALMAINTYPE,A.NUMTYPE,A.ISCHIEF FROM MSTEP_PRODUCTGROUP_MMT A ,MPRODUCTGROUP_P B WHERE A.PRODUCTGROUPID=B.PRODUCTGROUPID  AND A.MATERIALMAINTYPE='"+sMaterialType+"' AND A.STEPID='" +params.get("StepID") + "' AND B.PRODUCTID='" + msProductId + "' ";
				            sResult = db.GetData(sSQL, lsodtBom);
							if (sResult != "") {
								MESCommon.showMessage(WIP_SEQEdit.this, sResult);
								
							} 
							if(lsodtBom.size()>0)
							{
								
								for(int i=0;i<lsCompTable.size();i++)
								{
									if(editInput.getText().toString().trim().equals(lsCompTable.get(i).get("SEQ").toString()))
									{
										
										MESCommon.show(WIP_SEQEdit.this, "物料条码[" + sSEQ + "] 已在物料清单中!");
										return false ;
									}
									if(lsodtBom.get(0).get("ISCHIEF").toString().equals("Y") && lsCompTable.get(i).get("ISCHIEF").toString().equals("Y"))
									{
										MESCommon.show(WIP_SEQEdit.this, "需保留条码物料已在物料清单中!");
										return false ;
									}
							
									if(sMaterialId.equals(lsCompTable.get(i).get("MaterialId").toString()))
									{
										if(lsodtBom.get(0).get("NUMTYPE").toString().trim().toUpperCase().equals("S"))
										{
											lsCompTable.remove(i);
											break;
										}
									}			
								}
							}
			            }
						
						for(int i=0;i<lsCompTable.size();i++)
						{
							if(txtInput.getText().toString().trim().equals(lsCompTable.get(i).get("SEQ").toString()))
							{
								MESCommon.show(WIP_SEQEdit.this, "物料条码[" + sSEQ + "] 已在物料清单中!");
							    return false;
							}								
							
						}
						
						
						HashMap<String, String> hs = new HashMap<String, String>();				
						hs.put("MaterialId",sMaterialId);
						hs.put("MaterialMame", sMaterialMame);
						hs.put("PRODUCTSERIALNUMBER",sSEQ);
						hs.put("SEQ",lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						hs.put("MaterialType", sMaterialType);
						hs.put("CHECKFLAG", "N");
						hs.put("ISCOMPEXIST", "");
						hs.put("TRACETYPE", sTracetype);
						hs.put("LOTID", sLotID);
						hs.put("RAWPROCESSID", sRAWPROCESSID);
						hs.put("FINEPROCESSID", sFINEPROCESSID);
						hs.put("SUPPLYID", sSUPPLYID);
						hs.put("FURNACENO", sLNO);
						hs.put("IS_P", "Y");
						lsCompTable.add(hs);							
						adapter.notifyDataSetChanged();	
						InsertSTEP_P();
						Toast.makeText(WIP_SEQEdit.this, "零部件：【"+txtInput.getText().toString().trim()+"】,加入成功！", Toast.LENGTH_SHORT).show();	
						editInput.setText("");
						setFocus(editMaterialID);
					}
					cb.setChecked(false);
					} catch (Exception e) {
						MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
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
	

		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (lsCompTable.size() > 0) {
						final List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
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
							MESCommon.show(WIP_SEQEdit.this, "请选择要删除的零部件");
							return;
						}
						AlertDialog alert=	new AlertDialog.Builder(WIP_SEQEdit.this).setTitle("确认").setMessage("是否确认删除选择的零部件！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	String  sSql="";
				            	for(int i=lsCompTableCopy.size()-1;i>=0;i--)
								{
									if (lsCompTableCopy.get(i).get("CHECKFLAG").toString().equals("Y")) {
										
									   sSql=sSql+" DELETE FROM  PROCESS_STEP_PF  WHERE  SERIALNUMBER_P='"+lsCompTableCopy.get(i).get("SEQ").toString()+"'  ;";	
										lsCompTable.remove(i);
									}
								}
				                String sError= db.ExecuteSQL(sSql);
				    			
								adapter.notifyDataSetChanged();
								
				            }  
				        })  
						.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
						            @Override  
						            public void onClick(DialogInterface dialog,int which) {  
						                // TODO Auto-generated method stub  
						            	return  ;
						            }  
						 }).show();
						
						
					
					}	
				} catch (Exception e) {
					MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
				}
			}
		});

        // btnExit
		Button btnExit = (Button) findViewById(R.id.wipseqedit_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
				}
			}
		});
	
		} catch (Exception e) {
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
	
		}
	}


	// 有返回值的Activity  
	public void openNewActivity2(View v)  
	{  try{
	 Intent intent = new Intent();  
	 intent.setClass(this.getApplicationContext(), WIP_CompReplace.class);  
	 intent.putExtra("MaterialId", lsCompTable.get(milv0RowNum).get("MaterialId").toString());  
	 intent.putExtra("MaterialMame", lsCompTable.get(milv0RowNum).get("MaterialMame").toString());  
	 intent.putExtra("PRODUCTSERIALNUMBER", lsCompTable.get(milv0RowNum).get("PRODUCTSERIALNUMBER").toString());  
	 intent.putExtra("SEQ", lsCompTable.get(milv0RowNum).get("SEQ").toString());  
	 intent.putExtra("ProductId",msProductId);  
	 intent.putExtra("ProductOrderId", msProductOrderId);  
	 intent.putExtra("ProductCompId", msProductCompId);  
	 startActivityForResult(intent, 1);  
	} catch (Exception e) {
		MESCommon.showMessage(WIP_SEQEdit.this, e.toString());

	}
	}  
	  
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  try{
	 // requestCode用于区分业务  
	 // resultCode用于区分某种业务的执行情况  
	 if (1 == requestCode && RESULT_OK == resultCode)  
	 {  
	  String result = data.getStringExtra("result");  
	  if(result.equals("OK"))
	  {
		  GetProcessSTEP_P();
	  }
	 } 
	} catch (Exception e) {
		MESCommon.showMessage(WIP_SEQEdit.this, e.toString());

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

	public static class wiptrackinAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapter(List<HashMap<String, String>> items,
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
				convertView = inflater.inflate(R.layout.activity_wip_compedit_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wipcompeditlv0_cb);
				holder.tvSerialnumberId = (TextView) convertView.findViewById(R.id.wipcompeditlv0_tvSerialnumberId);
				holder.tvMaterialName = (TextView) convertView.findViewById(R.id.wipcompeditlv0_tvMaterialName);
				holder.tvMaterialId= (TextView) convertView.findViewById(R.id.wipcompeditlv0_tvMaterialId);
				
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvMaterialName.setText(getItem(position).get("MaterialMame").toString());	
			holder.tvMaterialId.setText(getItem(position).get("MaterialId").toString());	
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
			wiptrackinAdapter.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvSerialnumberId;
			TextView tvMaterialName;
			TextView tvMaterialId;
		}
	}
	
	
	private  void GetProcessSTEP_P()
	{
		try {	
			lsCompTable.clear();
			lsProcessStep_p.clear();
			String  sSql=" SELECT * FROM PROCESS_STEP_PF  WHERE SYSID=(SELECT SYSID FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P='"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"')  ;";
			String sError= db.GetData(sSql,  lsProcessStep_p);
			 if (sError != "") {
					MESCommon.showMessage(WIP_SEQEdit.this, sError);
					return;
				 }
	        if(lsProcessStep_p.size()>0)
	        {
				for(int i=0;i<lsProcessStep_p.size();i++)
			    {
					msSysid=lsProcessStep_p.get(i).get("SYSID").toString();
					msAnalysisformsID=lsProcessStep_p.get(i).get("ANALYSISFORMSID").toString();
					msProductOrderId=lsProcessStep_p.get(i).get("PRODUCTORDERID").toString();
					msProductCompId=lsProcessStep_p.get(i).get("PRODUCTCOMPID").toString();
					msProductSerialNumber=lsProcessStep_p.get(i).get("PRODUCTSERIALNUMBER").toString();
					msProductSerialNumber_old=lsProcessStep_p.get(i).get("PRODUCTSERIALNUMBER_OLD").toString();
					HashMap<String, String> hs = new HashMap<String, String>();				
					hs.put("MaterialId",lsProcessStep_p.get(i).get("PROCESS_PRODUCTID").toString());
					hs.put("MaterialMame", lsProcessStep_p.get(i).get("PROCESS_PRODUCTNAME").toString());
					if (!lsProcessStep_p.get(i).get("FINEPROCESSID").toString().equals("")) {
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("FINEPROCESSID").toString());
					    msSERIALNUMBER_OLD_P=lsProcessStep_p.get(i).get("FINEPROCESSID").toString();
					}else  if (!lsProcessStep_p.get(i).get("RAWPROCESSID").toString().equals(""))
					{
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("RAWPROCESSID").toString());
						 msSERIALNUMBER_OLD_P=lsProcessStep_p.get(i).get("RAWPROCESSID").toString();
					}
					else if (!lsProcessStep_p.get(i).get("LOTID").toString().equals(""))
					{
						hs.put("PRODUCTSERIALNUMBER",lsProcessStep_p.get(i).get("LOTID").toString());
						 msSERIALNUMBER_OLD_P=lsProcessStep_p.get(i).get("LOTID").toString();
					}
					hs.put("SEQ",lsProcessStep_p.get(i).get("PRODUCTSERIALNUMBER").toString());
					hs.put("MaterialType",lsProcessStep_p.get(i).get("MATERIALMAINTYPE").toString());
					hs.put("CHECKFLAG","N");
					hs.put("RAWPROCESSID",lsProcessStep_p.get(i).get("RAWPROCESSID").toString());
					hs.put("FINEPROCESSID",lsProcessStep_p.get(i).get("FINEPROCESSID").toString());
					hs.put("SUPPLYID",lsProcessStep_p.get(i).get("SUPPLYID").toString());
					hs.put("FURNACENO",lsProcessStep_p.get(i).get("LNO").toString());
					lsCompTable.add(hs);
			    }
			
	        }
	    	adapter.notifyDataSetChanged();				
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
		}
	}
	
	private  String InsertSTEP_P()
	{
		String sResult="";
		try {
			if(lsCompID.size()>0)
			{    
			    String	sSql =  "INSERT INTO PROCESS_STEP_PF (SYSID, ANALYSISFORMSID, PRODUCTORDERID, PRODUCTCOMPID, STEPID, EQPID, PRODUCTSERIALNUMBER_OLD, PRODUCTSERIALNUMBER, SERIALNUMBER_OLD_P, SERIALNUMBER_P, PROCESS_PRODUCTID, PROCESS_PRODUCTNAME, MATERIALMAINTYPE, TRACETYPE, LOTID, RAWPROCESSID, FINEPROCESSID, SUPPLYID, LNO, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ( '"
                            + msSysid+ "','"
                            + msAnalysisformsID + "','"
                            + msProductOrderId + "','"
                            + msProductCompId + "','"
                            + msStepId+ "','"
                            + msEqpId+ "','"
                            + msProductSerialNumber_old + "','"
                            + msProductSerialNumber + "','"
                            + msSERIALNUMBER_OLD_P + "','"
                            + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+ "','"
                            + lsCompID.get(0).get("MATERIALID").toString() + "','"
                            + lsCompID.get(0).get("MATERIALNAME").toString() + "','"
                            + lsCompID.get(0).get("MATERIALMAINTYPE").toString() + "','"
                            + lsCompID.get(0).get("TRACETYPE").toString() + "','"
                            + lsCompID.get(0).get("LOTID").toString() + "','"
                            + lsCompID.get(0).get("RAWPROCESSID").toString() + "','"
                            + lsCompID.get(0).get("FINEPROCESSID").toString() + "','"
                            + lsCompID.get(0).get("SUPPLYID").toString() + "','"
                            + lsCompID.get(0).get("FURNACENO").toString() + "','"
                            + MESCommon.UserId + "','"
                            + MESCommon.UserName + "',"
                            + MESCommon.ModifyTime + ");";
				    
                 String sError= db.ExecuteSQL(sSql);
     			 if (sError != "") {
     					
     					return sResult =sError;
     				 }
			}
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
			return sResult =e.toString();
		}
		return sResult;
	}
	

	public void Clear() {
		try {
		
			
			editInput.setText("");
			editMaterialID.setText("");
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			miQty=0;
            lsCompID .clear();lsProcess .clear(); 
           lsCompTable.clear();
            lsodtBom .clear();lsProcessStep_p.clear();lsProduct.clear();
            setFocus(editInput);
		} catch (Exception e) {
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e) {
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());

		}
	}
	
	
	public String checkExist(String number)
	{
		String sResult="";
		try {
			List<HashMap<String, String>> lscheckExist=new ArrayList<HashMap<String, String>>();
			
		 	 String sSQL = "SELECT PRODUCTSERIALNUMBER AS NUMBER  FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER= '"+number+"'  UNION SELECT PRODUCTSERIALNUMBER AS NUMBER  FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '"+number+"'   ";
		 	
             String sError = db.GetData(sSQL, lscheckExist);
			 if (sError != "") {
				MESCommon.showMessage(WIP_SEQEdit.this, sError);
				return sResult=sError;
			 }
			 if(lscheckExist.size()>0)
			 {
				 return sResult="扫描条码【"+number+"】,已经组装装配过，请重新扫描新的条码！";
			 }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_SEQEdit.this, e.toString());
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

