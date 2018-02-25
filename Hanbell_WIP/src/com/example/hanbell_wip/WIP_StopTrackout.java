package com.example.hanbell_wip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hanbell_wip.Class.MESCommon;
import com.example.hanbell_wip.Class.MESDB;
import com.example.hanbell_wip.EQP_Start.SpinnerData;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WIP_StopTrackout extends Activity {
	private MESDB db = new MESDB();

	Button  btnExit, btnConfirm;
	EditText editInput,editProductCompID;
	Spinner spDesction;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;

	private	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();	
	private	List<HashMap<String, String>> lsDefect = new ArrayList<HashMap<String, String>>();	
	
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_stoptrackout);
try{
		// 取得控件
		editInput = (EditText) findViewById(R.id.wipStopTrackIn_tvInput);	
		editProductCompID = (EditText) findViewById(R.id.wipStopTrackIn_tvProductCompid);	
		spDesction=(Spinner)findViewById(R.id.wipStopTrackIn_spDescription);	
		btnExit = (Button) findViewById(R.id.wipStopTrackIn_btnExit);			
		btnConfirm=(Button) findViewById(R.id.wipStopTrackIn_btnConfirm);

		// ***********************************************Start
		
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("制造号码停止报工");
	
		// 取得控件	 
		String date = sDateFormatShort.format(new java.util.Date());	
	
		// 读取缺陷备注
		lsDefect.clear();
		String sSql = "SELECT PARAMETERVALUE FROM MSYSTEMPARAMETER WHERE PARAMETERID='DEFECTTRACKIN' ";
		String	sResult = MESDB.GetData(sSql, lsDefect);
		if (sResult != "") {
			MESCommon.showMessage(WIP_StopTrackout.this, sResult);
		
		}
		ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefect.size(); i++) {
			SpinnerData c = new SpinnerData(lsDefect.get(i)
					.get("PARAMETERVALUE").toString(), lsDefect
					.get(i).get("PARAMETERVALUE").toString());
			lst.add(c);
		}

		ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(WIP_StopTrackout.this,
				android.R.layout.simple_spinner_item, lst);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDesction.setAdapter(Adapter);
		
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.wipStopTrackIn_tvInput);
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_StopTrackout.this, "请扫描条码!");
						txtInput.setText("");
						setFocus(editProductCompID);
						return false;
					}					
					
						lsCompID.clear();						
						String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","制造号码","装配", lsCompID);
					    if (!sResult.equals(""))
	                    {   MESCommon.show(WIP_StopTrackout.this,sResult);
	                        setFocus(editProductCompID);
					    	return false;
	                    }
		            	if (lsCompID.get(0).get("ISPRODUCTCOMPID").toString().equals("N") )
		                {
	            		   MESCommon.show(WIP_StopTrackout.this,"请刷入制造号码!");
	            		   txtInput.setText("");
	            		   setFocus(editProductCompID);
	                       return false;
		                }
		            	lsProcess.clear();
						String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
						sResult = db.GetProductProcess(sOrderID,txtInput.getText().toString().trim(), lsProcess);
						if (sResult != "") {
							MESCommon.showMessage(WIP_StopTrackout.this, sResult);
							finish();							
						}			
						if(lsProcess.size()==0)
						{
							MESCommon.show(WIP_StopTrackout.this, " 制造号码【"+txtInput.getText().toString().trim() +"】，没有设定生产工艺流程！");
							Clear();
							return false;
						}
						lsProduct.clear();
						String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
						String sError= db.GetData(sSql,  lsProduct);
						 if (sError != "") {
								MESCommon.showMessage(WIP_StopTrackout.this, sError);
								return false;
							 }
						//初始化工件信息
						editProductCompID.setText(lsCompID.get(0).get("PRODUCTCOMPID").toString());

						setFocus(editProductCompID);
						
					} catch (Exception e) {
						MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
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
	

		   // btnUnbind
	btnConfirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							SpinnerData spDes = (SpinnerData) spDesction.getSelectedItem();
							
							if (editProductCompID.getText().toString().trim().length() == 0) {
								MESCommon.show(WIP_StopTrackout.this, "请扫描条码!");
								setFocus(editProductCompID);
								return ;
							}					
							if (spDes.text == "") {
								MESCommon.show(WIP_StopTrackout.this, "请选择备注说明!");
								return;
							}   	   
		            	String   sSQL =  "UPDATE PROCESS SET DESCRIPTION ='"+spDes.value+"' WHERE  PRODUCTCOMPID='"+editProductCompID.getText().toString()+"' "; 
			            String sError = db.ExecuteSQL(sSQL);
						 if (sError != "") {
							MESCommon.showMessage(WIP_StopTrackout.this, sError);
							return;
						 } 
						 Toast.makeText(WIP_StopTrackout.this, "确认成功!", Toast.LENGTH_SHORT).show();  
						 finish();
						} catch (Exception e) {
							MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
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
					MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
				}
			}
		});
	
} catch (Exception e) {
	MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
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

	public void Clear() {
		try {
		
			
			editProductCompID.setText("");		
            lsCompID .clear();lsProcess .clear(); 
            lsProduct.clear();         
            setFocus(editInput);
            editInput.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);
		

		} catch (Exception e) {
			MESCommon.showMessage(WIP_StopTrackout.this, e.toString());
		}
	}	

}
