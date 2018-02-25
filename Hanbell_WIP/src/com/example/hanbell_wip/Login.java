package com.example.hanbell_wip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.Class.MESCommon;
import com.example.hanbell_wip.Class.MESDB;
import com.example.hanbell_wip.Class.PrefercesService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends Activity {

	private MESDB db = new MESDB();
	EditText editUserId,editPW;
	Button btnConfirm,btnExit;
	CheckBox cBox;
	PrefercesService	 prefercesService;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		try{
		//取得控件
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnExit = (Button) findViewById(R.id.btnExit);
		editUserId = (EditText) findViewById(R.id.editUserId);
		editPW = (EditText) findViewById(R.id.editPW);
		cBox=(CheckBox) findViewById(R.id.cb);
		//取参数值
		//Intent i = getIntent();
		//editUserId.setText(i.getStringExtra("ID"));
		
		//***********************************************Start
		editUserId.setSelectAllOnFocus(true);
		editPW.setSelectAllOnFocus(true);
//		editUserId.setText("admin");
//		editPW.setText("ftc");
	 
        
		//***********************************************End
	    prefercesService  =new PrefercesService(this);  
        Map<String,String> params=prefercesService.getPreferences2();  
    	editUserId.setText(params.get("USER"));  
		editPW.setText(params.get("PAW"));  
		//控件事件
		//btnConfirm
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					String sUserName = "";
					String sUserId = editUserId.getText().toString().trim();
					String sPW = editPW.getText().toString().trim();
					
					if (sUserId.equals(""))
					{
						MESCommon.showMessage(Login.this, "请输入用户代码!!");
						editUserId.setText("");
						setFocus(editUserId);
						return;
					}
					
					List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
					sPW = db.Encrypt(sPW);
					String sSql = "select * from MUSER where USERID='" + sUserId + "'  and PASSWORD='" + sPW + "'";
					String sResult = db.GetData(sSql, ls);
					if (!sResult.equals(""))
					{
						MESCommon.showMessage(Login.this, sResult);
						editUserId.setText("");
						setFocus(editUserId);
						return;
					}
					if (ls.size() > 0)
					{
						sUserName=ls.get(0).get("USERNAME");
						//设置返回值
						Intent i=getIntent();
						i.putExtra("UserId", sUserId);
						i.putExtra("UserName", sUserName);
						if(cBox.isChecked())
						{
						   prefercesService.save2(sUserId,editPW.getText().toString().trim());  
						}else {							
							prefercesService.save2("","");  
						}
						
						setResult(10, i);
						finish();
					}
					else
					{
						MESCommon.showMessage(Login.this, "用户代码或密码错误，请确认!!");
						editPW.setText("");
						setFocus(editPW);
						return;
					}
					
				} catch (Exception e) {
					MESCommon.showMessage(Login.this,e.toString());
				}
			}
		});
		//btnExit
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(Login.this,e.toString());
				}
			}
		});
	} catch (Exception e) {
		MESCommon.showMessage(Login.this,e.toString());
	}
	}
	

	public void setFocus(EditText editText)
	{
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			
		} catch (Exception e) {
			MESCommon.showMessage(Login.this,e.toString());
		}
		
	}
	
	
}
