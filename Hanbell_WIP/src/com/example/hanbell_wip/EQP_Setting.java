package com.example.hanbell_wip;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;


@SuppressLint("NewApi") public class EQP_Setting extends Activity {
	private MESDB db = new MESDB();
	Button btnConfirm, btnExit;
	Spinner spProducttype, spStep, spEQP;
	EditText  editEQPName,editStepName;
	PrefercesService	 prefercesService;
	// 该物料的HashMap记录
	
	
	private List<HashMap<String, String>> lsDefProductType = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefStep = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefEQP = new ArrayList<HashMap<String, String>>();
	

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_eqp_setting);	
		// 取得控件
		
		editEQPName = (EditText) findViewById(R.id.eqpset_tvEQPName);
		editStepName = (EditText) findViewById(R.id.eqpset_tvStepName);
		spProducttype = (Spinner) findViewById(R.id.eqpset_spProductType);
		spStep = (Spinner) findViewById(R.id.eqpset_spStep);
		spEQP = (Spinner) findViewById(R.id.eqpset_spEQP);		
		btnConfirm = (Button) findViewById(R.id.eqpset_btnConfirm);	
		btnExit = (Button) findViewById(R.id.eqpset_btnExit);
		
		// ***********************************************Start

	    prefercesService  =new PrefercesService(this);  
        Map<String,String> params=prefercesService.getPreferences();  
        editEQPName.setText(params.get("EQPName"));  
        editStepName.setText(params.get("StepName"));         
    	ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("设备设定");
	    try{
        String sProducttype=params.get("ProductType");
        String sStep=params.get("StepID");
        String sEqp=params.get("EQPID");
     
		// 读取产品类型

		String sSql = "SELECT DISTINCT  PRODUCTTYPE ,PRODUCTTYPE  FROM MPRODUCT  ORDER BY PRODUCTTYPE ";
		String sResult = db.GetData(sSql, lsDefProductType);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Setting.this, sResult);
			finish();
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
			MESCommon.showMessage(EQP_Setting.this, sResult);
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
				EQP_Setting.this,
				android.R.layout.simple_spinner_item, lst);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spStep.setAdapter(Adapter1);
		MESCommon.setSpinnerItemSelectedByValue(spStep,sStep);
		// 读取机台
		 sSql =" SELECT DISTINCT B.EQPID,B.EQPNAME FROM  MSTEP_P A INNER JOIN MEQP B ON A.PARAMETERCODE=B.EQPID WHERE A.PARAMETERNAME='EQP' AND A.STEPID LIKE '%"
				+ sStep + "%'  ORDER BY EQPID ";
		 sResult = db.GetData(sSql, lsDefEQP);
		if (sResult != "") {
			MESCommon.showMessage(EQP_Setting.this, sResult);
			finish();
		}
	    lst = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefEQP.size(); i++) {
			SpinnerData c1 = new SpinnerData(lsDefEQP.get(i)
					.get("EQPID").toString(), lsDefEQP.get(i)
					.get("EQPNAME").toString());
			lst.add(c1);
		}
		Adapter = new ArrayAdapter<SpinnerData>(
				EQP_Setting.this, android.R.layout.simple_spinner_item,
				lst);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spEQP.setAdapter(Adapter);
		MESCommon.setSpinnerItemSelectedByValue(spEQP,sEqp);
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
							MESCommon.showMessage(EQP_Setting.this, sResult);
							finish();
						}
						ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
						for (int i = 0; i < lsDefStep.size(); i++) {
							SpinnerData c = new SpinnerData(lsDefStep.get(i)
									.get("STEPID").toString(), lsDefStep.get(i)
									.get("STEPID").toString());
							lst.add(c);
						}

						ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
								EQP_Setting.this,
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
				String sSql = " SELECT DISTINCT B.EQPID,B.EQPNAME FROM  MSTEP_P A INNER JOIN MEQP B ON A.PARAMETERCODE=B.EQPID WHERE A.PARAMETERNAME='EQP' AND A.STEPID LIKE '%"
						+ step.value + "%'  ORDER BY EQPID ";
				String sResult = db.GetData(sSql, lsDefEQP);
				if (sResult != "") {
					MESCommon.showMessage(EQP_Setting.this, sResult);
					finish();
				}
				ArrayList<SpinnerData> lst1 = new ArrayList<SpinnerData>();
				for (int i = 0; i < lsDefEQP.size(); i++) {
					SpinnerData c1 = new SpinnerData(lsDefEQP.get(i)
							.get("EQPID").toString(), lsDefEQP.get(i)
							.get("EQPNAME").toString());
					lst1.add(c1);
				}
				ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
						EQP_Setting.this, android.R.layout.simple_spinner_item,
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


        

		// btnOK
		Button btnOK = (Button) findViewById(R.id.eqpset_btnConfirm);
		btnOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					SpinnerData sProductType = (SpinnerData) spProducttype.getSelectedItem();
					if (sProductType.value.toString().equals(""))
					{
						MESCommon.showMessage(EQP_Setting.this, "请选择产品分类!");
						return;
					}
					SpinnerData sEQP = (SpinnerData) spEQP.getSelectedItem();	
					SpinnerData sStep = (SpinnerData) spStep.getSelectedItem();	
					prefercesService.save(sEQP.text,sEQP.value,sStep.text,sStep.value,sProductType.text,sProductType.value);  				
					Toast.makeText(EQP_Setting.this, "保存成功!", Toast.LENGTH_SHORT).show();
					editStepName.setText(sStep.text);  
				    editEQPName.setText(sEQP.text);  
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Setting.this, e.toString());
				}
			}
		});

		// btnExit
		Button btnExit = (Button) findViewById(R.id.eqpset_btnExit);
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(EQP_Setting.this, e.toString());
				}
			}
		});
	} catch (Exception e) {
		MESCommon.showMessage(EQP_Setting.this,  e.toString());
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


	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();

		} catch (Exception e) {
			MESCommon.showMessage(EQP_Setting.this, e.toString());
		}

	}
}
