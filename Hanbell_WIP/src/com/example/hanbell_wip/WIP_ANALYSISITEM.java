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

	// 该物料的HashMap记录
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
		// 取得控件
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
		actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
		actionBar.setTitle(params.get("StepName"));
		String date = sDateFormatShort.format(new java.util.Date());	
		
		// 读取报工人员
		String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM EQP_RESULT_USER WHERE EQPID ='"+params.get("EQPID")+"' AND ISNULL(WORKDATE,'')='"+date+"' AND ISNULL(LOGINTIME,'')!='' AND  ISNULL (LOGOUTTIME,'')=''   AND ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"+ MESCommon.UserId + "' OR PROCESSUSERID='"+ MESCommon.UserId.toUpperCase()+"') ";
		String sResult = db.GetData(sSql, lsuser);
		if (sResult != "") {
			MESCommon.showMessage(WIP_ANALYSISITEM.this, sResult);
			finish();
		}
	
		if ( lsuser.size()==0) {
			MESCommon.showMessage(WIP_ANALYSISITEM.this, "请先进行人员设备报工！");
		
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
		
		// 控件事件
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinpreAdapter.ViewHolder holder = (wiptrackinpreAdapter.ViewHolder) arg1.getTag();
				showRow(arg2);
				}});

		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					
					// 查询交货单
					try {
						
						EditText txtInput = (EditText) findViewById(R.id.wipanalysisitem_tvInput);						
						
						if(lsuser.size()==0)
						{   MESCommon.show(WIP_ANALYSISITEM.this, "请先进行设备人员报工!");
						setFocus(editInput)	;
						    return false;
						}
						if (txtInput.getText().toString().trim().length() == 0) {
							txtInput.setText("");
							MESCommon.show(WIP_ANALYSISITEM.this, "请扫描条码!");
							return false;
						}
						lsCompID.clear();					
						String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","零部件","装配", lsCompID);			
						if (sResult != "") {
							MESCommon.showMessage(WIP_ANALYSISITEM.this, sResult);
							setFocus(editInput)	;	
							return false;
						}							
						txtInput.setText(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
						msProductSerialNumber=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();					
						if(lsAnalysisTable.size()==0)
						{
							if(params.get("StepName") .contains("机体"))
							{
							     msProductId="35325-A00";
							}else {
								 msProductId="31330-A53111-SH";
							}			    	
							
						editMaterialName.setText(lsCompID.get(0).get("MATERIALNAME").toString());
							
						//初始化检验项目
						lsAnalysisData.clear();
						msStepId=params.get("StepID").toString();	
						msEqpId=params.get("EQPID").toString();
						//查询是否已经检验过。
						List<HashMap<String, String>> lscheckAnal = new ArrayList<HashMap<String, String>>();
						lscheckAnal.clear();
						String sSQL = "SELECT * FROM ANALYSISWAITLIST where PRODUCTSERIALNUMBER='"+msProductSerialNumber+"'  AND ANALYSISSTATUS='已完成' ";
			             String sError = db.GetData(sSQL, lscheckAnal);
						 if (sError != "") {
							MESCommon.showMessage(WIP_ANALYSISITEM.this, sError);
							return false;
						 }
						 if(lscheckAnal.size()>0)
						 {
								MESCommon.showMessage(WIP_ANALYSISITEM.this, "条码【"+msProductSerialNumber+"】,已经完成装配检验！请重新选择！");
								return false;
						 }
						 if(msStepId.equals("冷媒电机次组立站"))
						 {
							 if(!lsCompID.get(0).get("MATERIALNAME").toString().contains("定部"))
							 {
								 MESCommon.showMessage(WIP_ANALYSISITEM.this, "条码【"+msProductSerialNumber+"】,不是【定部】，请扫描正确的条码！");
									return false;
							 }
						 }
						

						sResult = db.GetAnalysisDatabyProductSerialNumber(msProductOrderId,msProductId,msProductSerialNumber,msStepId,MESCommon.UserId ,msEqpId,"自主检验",lsAnalysisData);
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
					String sResult="合格";		
					
					for(int i=0;i<lsAnalysisTable.size();i++)
					{
						if(lsAnalysisTable.get(i).get("ISNEED").toString().trim().equals("Y"))
						{
							if(lsAnalysisTable.get(i).get("DISPLAYVALUE").toString().trim().equals(""))
							{		
								MESCommon.show(WIP_ANALYSISITEM.this, "请输入【"+lsAnalysisTable.get(i).get("ITEMALIAS").toString()+"】检验结果");				
								return;
							}
						}						
					}

			 	   if(sResult.equals("不合格"))
					{
						
						AlertDialog alert=	new AlertDialog.Builder(WIP_ANALYSISITEM.this).setTitle("确认").setMessage("检验不合格,是否确认继续报工！")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				                // TODO Auto-generated method stub  
				            	Save("不合格", lsAnalysisTable, lsAnalysisData);
				            	//执行最终判定
					            db.FinalSaveData(msAnalysisformsID,msSampletimes,"不合格",MESCommon.UserId ,MESCommon.UserName,"");
								Toast.makeText(WIP_ANALYSISITEM.this, "储存成功!", Toast.LENGTH_SHORT).show();
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
						//插入记录
					if (InsertPROCESS_STEP_P_ANALYSISITEM()) {
					 Save(sResult, lsAnalysisTable, lsAnalysisData);
						//执行最终判定
		         	 db.FinalSaveData(msAnalysisformsID,msSampletimes,sResult,MESCommon.UserId ,MESCommon.UserName,"");
	                  Toast.makeText(WIP_ANALYSISITEM.this, "储存成功!", Toast.LENGTH_SHORT).show();		                  
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
	
	
	private void showRow(int iRow) {
		try {
			if (iRow < lsAnalysisTable.size()) {
				milv1RowNum=iRow;
				EditText edit = (EditText) findViewById(R.id.wipanalysisitem_editTab2_0);
				RadioButton rdoOK = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonOK);
				RadioButton rdoNG = (RadioButton) findViewById(R.id.wipanalysisitem_radioButtonNG);
				LinearLayout tab2_0 = (LinearLayout) findViewById(R.id.wipanalysisitem_tab2_0);
				LinearLayout tab2_1 = (LinearLayout) findViewById(R.id.wipanalysisitem_tab2_1);

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
         	 Toast.makeText(WIP_ANALYSISITEM.this, "报工完成!", Toast.LENGTH_SHORT).show();
             
            
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