package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Start.SpinnerData;


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

public class WIP_TrackIn_SP extends Activity {

	private MESDB db = new MESDB();
	
	String msCompid = "", msCompidseq = "";
	Button btnExit;
	EditText editCompid,editProjectId,editDefectName;

	PrefercesService prefercesService;
	Map<String, String> params;
		
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_trackin_sp);
		try{
		prefercesService = new PrefercesService(this);
		params = prefercesService.getPreferences();
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("不合格单查看");
		// 取得控件
	    editCompid = (EditText) findViewById(R.id.wiptrackinsp_tvCompid);
		editProjectId = (EditText) findViewById(R.id.wiptrackinsp_tvProjectId);
		editDefectName= (EditText) findViewById(R.id.wiptrackinsp_tvDefectName);
		btnExit = (Button) findViewById(R.id.wiptrackinsp_btnExit);
		
		//取参数值
		Intent a = getIntent();
		msCompid = a.getStringExtra("COMPID");
		msCompidseq = a.getStringExtra("COMPIDSEQ");

		// ***********************************************Start
		List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
		String sSql = "SELECT A.PROJECTID, A.MODIFYTIME FINISHTIME, B.DEFECTNAME FROM FLOW_FORM_UQF_S_NOW A INNER JOIN ANALYSISRESULT_QCD B ON A.PROJECTID=B.PROJECTID " +
				" WHERE A.ANALYSISJUDGEMENTRESULT='特采' AND B.COMPIDSEQ='"+msCompidseq+"' ORDER BY A.MODIFYTIME DESC";
		String sResult = db.GetData(sSql, ls);		
		if (sResult != "") {
			MESCommon.showMessage(WIP_TrackIn_SP.this, sResult);
			finish();
		}
	
		if ( ls.size()==0) {
			MESCommon.showMessage(WIP_TrackIn_SP.this, "不合格单查无资料！");
			finish();
		}
		editCompid.setText(msCompid);
		editProjectId.setText(ls.get(0).get("PROJECTID").toString());
		editDefectName.setText(ls.get(0).get("DEFECTNAME").toString());
		
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					finish();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_TrackIn_SP.this, e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn_SP.this, e.toString());
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
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(WIP_TrackIn_SP.this, e.toString());
		}

	}

}
