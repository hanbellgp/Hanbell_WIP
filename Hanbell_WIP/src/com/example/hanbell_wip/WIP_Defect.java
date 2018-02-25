package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Start.SpinnerData;

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

public class WIP_Defect extends Activity {

	private MESDB db = new MESDB();

	ListView lv;
	Button btnConfirm, btnExit, btnDelete, btnAdd;
	Spinner spDefectType, spDefect;
	EditText editDefectName, editDefectNumber;
	EditText editInput,editInputName;
	TextView h0, h1, h2, h3,h4;
	WIPDefectAdapter adapter;
	PrefercesService prefercesService;
	Map<String, String> params;
	String msProductOrderId = "", msProductId, msProductCompId,
			msProductSerialNumber, msStepId, msEqpId, msAnalysisformsID = "",
			msSampletimes = "", msStepSEQ, msSUPPLYLOTID, msSUPPLYID,
			msQC_ITEM, msQCTYPE, msDefectSysid, msTraceType="", msIsComp;
	static int miRowNum = 0;
	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsuser = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefectType = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefectID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefectTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsSysid = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefectName = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();

	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_defect);

		// 取得控件

		spDefectType = (Spinner) findViewById(R.id.wipdefect_spDefectType);
		spDefect = (Spinner) findViewById(R.id.wipdefect_spDefectID);
		editInput = (EditText) findViewById(R.id.wipdefect_tvInput);
		editInputName = (EditText) findViewById(R.id.wipdefect_tvInputName);
		editDefectName = (EditText) findViewById(R.id.wipdefect_tvDefectName);
		editDefectNumber =(EditText) findViewById(R.id.wipdefect_tvDefectNumber);
		h0 = (TextView) findViewById(R.id.wipdefect_h0);
		h1 = (TextView) findViewById(R.id.wipdefect_h1);
		h2 = (TextView) findViewById(R.id.wipdefect_h2);
		h3 = (TextView) findViewById(R.id.wipdefect_h3);
		h4 = (TextView) findViewById(R.id.wipdefect_h4);
		h0.setTextColor(Color.WHITE);
		h0.setBackgroundColor(Color.DKGRAY);
		h1.setTextColor(Color.WHITE);
		h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE);
		h2.setBackgroundColor(Color.DKGRAY);
		h3.setTextColor(Color.WHITE);
		h3.setBackgroundColor(Color.DKGRAY);
		h3.setBackgroundColor(Color.DKGRAY);
		h4.setTextColor(Color.WHITE);
		h4.setBackgroundColor(Color.DKGRAY);
		lv = (ListView) findViewById(R.id.wipdefect_lv);
		btnConfirm = (Button) findViewById(R.id.wipdefect_btnConfirm);
		btnAdd = (Button) findViewById(R.id.wipdefect_Add);
		btnDelete = (Button) findViewById(R.id.wipdefect_Delete);
		btnExit = (Button) findViewById(R.id.wipdefect_btnExit);
		adapter = new WIPDefectAdapter(lsDefectTable, this);
		lv.setAdapter(adapter);
		prefercesService = new PrefercesService(this);
		params = prefercesService.getPreferences();
		String date = sDateFormatShort.format(new java.util.Date());

		// ***********************************************Start
		// 控件事件
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				WIPDefectAdapter.ViewHolder holder = (WIPDefectAdapter.ViewHolder) arg1
						.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				WIPDefectAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				if (holder.cb.isChecked()) {
					lsDefectTable.get(position).put("CHECKFLAG", "Y");
				} else {
					lsDefectTable.get(position).put("CHECKFLAG", "N");
				}
			}
		});

		// 读取缺陷类型
		lsDefectType.clear();
		String sSql = "SELECT DEFECTGROUPID, DEFECTGROUPNAME  FROM MDEFECTGROUP  ORDER BY DEFECTGROUPID";
		String sResult = db.GetData(sSql, lsDefectType);
		if (sResult != "") {
			MESCommon.showMessage(WIP_Defect.this, sResult);

		}
		List<SpinnerData> lst = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefectType.size(); i++) {

			SpinnerData c = new SpinnerData(lsDefectType.get(i)
					.get("DEFECTGROUPID").toString(), lsDefectType.get(i)
					.get("DEFECTGROUPNAME").toString());
			lst.add(c);
		}

		ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,
				android.R.layout.simple_spinner_item, lst);
		Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDefectType.setAdapter(Adapter);

		// 读取不良原因
		lsDefectID.clear();
		sSql = "SELECT  ITEMID   ,ITEMID||  ITEMNAME AS DEFECTNAME  FROM MDEFECTITEM ORDER BY ITEMID";
		sResult = db.GetData(sSql, lsDefectID);
		if (sResult != "") {
			MESCommon.showMessage(WIP_Defect.this, sResult);

		}
		List<SpinnerData> lst1 = new ArrayList<SpinnerData>();
		for (int i = 0; i < lsDefectID.size(); i++) {

			SpinnerData c = new SpinnerData(lsDefectID.get(i).get("ITEMID")
					.toString(), lsDefectID.get(i).get("DEFECTNAME").toString());
			lst1.add(c);
		}

		ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
				this, android.R.layout.simple_spinner_item, lst1);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spDefect.setAdapter(Adapter1);
		SpinnerData Defect = (SpinnerData) spDefect.getSelectedItem();

		// 添加Spinner事件监听
		spDefectType
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						SpinnerData DefectType = (SpinnerData) spDefectType
								.getSelectedItem();

						// 读取不良代码
						lsDefectID.clear();
						String sSql = "SELECT  ITEMID,ITEMID+ITEMNAME AS DEFECTNAME   FROM MDEFECTITEM WHERE ITEMID IN (SELECT DEFECTITEMID   FROM MDEFECTGROUPITEM  WHERE DEFECTGROUPID ='"
								+ DefectType.value + "')";
						String sResult = MESDB.GetData(sSql, lsDefectID);
						if (sResult != "") {
							MESCommon.showMessage(WIP_Defect.this, sResult);

						}
						ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
						for (int i = 0; i < lsDefectID.size(); i++) {
							SpinnerData c = new SpinnerData(lsDefectID.get(i)
									.get("ITEMID").toString(), lsDefectID
									.get(i).get("DEFECTNAME").toString());
							lst.add(c);
						}

						ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
								WIP_Defect.this,
								android.R.layout.simple_spinner_item, lst);
						Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spDefect.setAdapter(Adapter1);

						// 设置显示当前选择的项
						arg0.setVisibility(View.VISIBLE);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		spDefect.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				SpinnerData Defect = (SpinnerData) spDefect.getSelectedItem();
				if (Defect.value.length() > 0) {
					lsDefectName.clear();
					String sSql = "SELECT B.ITEMNAME FROM MDEFECTGROUPITEM A LEFT JOIN MDEFECTITEM B ON A.DEFECTITEMID=B.ITEMID  WHERE  A.DEFECTITEMID='"
							+ Defect.value + "'";
					String sResult = db.GetData(sSql, lsDefectName);
					if (sResult != "") {
						MESCommon.showMessage(WIP_Defect.this, sResult);

					}
					if (lsDefectName.size() > 0) {
						editDefectName.setText(lsDefectName.get(0)
								.get("ITEMNAME").toString());
					}
				}
				// 设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
						EditText txtInput = (EditText) findViewById(R.id.wipdefect_tvInput);

						if (txtInput.getText().toString().trim().length() == 0) {
							MESCommon.show(WIP_Defect.this, "请扫描条码!");
							return false;
						}
						lsCompID.clear();
						String sResult = db.GetProductSerialNumber(txtInput
								.getText().toString().trim(), "",
								"", "QF","","", lsCompID);
						if (sResult != "") {
							MESCommon.showMessage(WIP_Defect.this, sResult);
							setFocus(editInputName);	
							return false;
						}
						if (lsCompID.size() == 0) {
							MESCommon.show(WIP_Defect.this, "该条码信息不存在!");
							txtInput.setText("");
							setFocus(editInputName);	
							return false;
						}
						txtInput.setText(lsCompID.get(0)
								.get("PRODUCTSERIALNUMBER").toString());

						msSUPPLYLOTID = lsCompID.get(0).get("SUPPLYLOTID")
								.toString();
						msProductId = lsCompID.get(0)
								.get("PRODUCTSERIALNUMBER").toString();
						msSUPPLYID = lsCompID.get(0).get("SUPPLYID").toString();
						msProductOrderId = lsCompID.get(0)
								.get("PRODUCTORDERID").toString();
						msProductCompId = lsCompID.get(0).get("PRODUCTCOMPID")
								.toString();
						msProductSerialNumber = lsCompID.get(0)
								.get("PRODUCTSERIALNUMBER").toString();
						msProductId = lsCompID.get(0).get("MATERIALID")
								.toString();
						msTraceType = lsCompID.get(0).get("TRACETYPE")
								.toString();
						msStepId = params.get("StepID").toString();
						msEqpId = params.get("EQPID").toString();
						msQC_ITEM = "自主检验";
						msQCTYPE = "制程检验";
						if (msTraceType.equals("L")) {
							editDefectNumber.setEnabled(true);
						} else {
							editDefectNumber.setEnabled(false);
							editDefectNumber.setText("1");
						}
						if (msTraceType.equals("C")) {
							msIsComp = "Y";
						} else {
							msIsComp = "N";
						}
			
				
					} catch (Exception e) {
						MESCommon.showMessage(WIP_Defect.this, e.toString());
						return false;
					}
				}

				return false;
			}
		});

		editInputName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				try {
					setFocus(editInput);
		
				} catch (Exception e) {

				}
			}
		});

		// btnAdd
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if(editInput.getText().toString().trim().equals(""))
					{
						MESCommon.show(WIP_Defect.this, "请先刷入工件条码！");
						return;
					}
					SpinnerData sdefecttype = (SpinnerData) spDefectType.getSelectedItem();
					SpinnerData sdefect = (SpinnerData) spDefect.getSelectedItem();
					if (editDefectName.getText().toString().trim().equals("")) {
						MESCommon.show(WIP_Defect.this, "请先选择不良代码");
						return;
					}
					if (editDefectNumber.getText().toString().trim().equals("")) {
						MESCommon.show(WIP_Defect.this, "请先输入不良数量");
						return;
					}
					if (msTraceType.equals("C") && lsDefectTable.size() == 1) {
						MESCommon.show(WIP_Defect.this, "一个工件只能记录一笔不良");
						return;
					}
					for(int i=0;i<lsDefectTable.size();i++)
					{
						if( sdefect.value.equals(lsDefectTable.get(i).get("DefectID").toString()))
						{
							MESCommon.show(WIP_Defect.this, "不良代码[" + sdefect.text + "] 已在物料清单中!");
						    return ;
						}								
						
					}
					HashMap<String, String> hs = new HashMap<String, String>();
					hs.put("DefectType", sdefecttype.value);
					hs.put("DefectID", sdefect.value);
					hs.put("DefectName", editDefectName.getText().toString());
					hs.put("DefectNumber", editDefectNumber.getText().toString());
					hs.put("CHECKFLAG", "N");
					lsDefectTable.add(hs);
					adapter.notifyDataSetChanged();

				} catch (Exception e) {
					MESCommon.showMessage(WIP_Defect.this, e.toString());
				}
			}
		});

		// btnDelete
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (lsDefectTable.size() > 0) {
						List<HashMap<String, String>> lsDefectTableCopy = new ArrayList<HashMap<String, String>>();
						Boolean isSelect=false;
					
						for(int i=0;i<lsDefectTable.size();i++)
						{
							if (lsDefectTable.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								isSelect=true;
							
							}
							lsDefectTableCopy.add(lsDefectTable.get(i));
						}
						if(!isSelect)
						{
							MESCommon.show(WIP_Defect.this, "请选择要删除的零部件");
							return;
						}
						for(int i=lsDefectTableCopy.size()-1;i>=0;i--)
						{
							if (lsDefectTableCopy.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								lsDefectTable.remove(i);
								
							}
						}					
						adapter.notifyDataSetChanged();
					
					}	
				} catch (Exception e) {
					// showMessage(e.toString());
				}
			}
		});
		
		// btnOK
		btnConfirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							EditText txtInput = (EditText) findViewById(R.id.wipdefect_tvInput);

							if(txtInput.getText().equals(""))
							{
								MESCommon.show(WIP_Defect.this, "请先扫描条码");
								return;
							}
							if(lsDefectTable.size()>0)
							{
								SaveDefect();
							}else
							{
								MESCommon.show(WIP_Defect.this, "请先选择不良代码");
								return;
							}
							Clear();
						} catch (Exception e) {
							MESCommon.showMessage(WIP_Defect.this, e.toString());
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
					MESCommon.showMessage(WIP_Defect.this, e.toString());
				}
			}
		});

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

	public static class WIPDefectAdapter extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 上下文
		private Context context;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public WIPDefectAdapter(List<HashMap<String, String>> items,
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
			miRowNum = position;
			if (convertView == null) {
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(
						R.layout.activity_wip_defect_listview, null);
				holder.cb = (CheckBox) convertView
						.findViewById(R.id.wipdefectlv_cb);
				holder.tvDefectID = (TextView) convertView
						.findViewById(R.id.wipdefectlv_tvDefectID);
				holder.tvDefectName = (TextView) convertView
						.findViewById(R.id.wipdefectlv_tvDefectName);
				holder.tvDefectNumber = (TextView) convertView
						.findViewById(R.id.wipdefectlv_tvDefectNumber);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
			}

			// 设置list中TextView的显示
			holder.tvDefectID.setText(getItem(position).get("DefectID")
					.toString());
			holder.tvDefectName.setText(getItem(position).get("DefectName")
					.toString());
			holder.tvDefectNumber.setText(getItem(position).get("DefectNumber")
					.toString());

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
			WIPDefectAdapter.isSelected = isSelected;
		}

		public static class ViewHolder {
			CheckBox cb;
			TextView tvDefectID;
			TextView tvDefectName;
			TextView tvDefectNumber;
		}
	}

	private void SaveDefect() {
		try {
			String sSQL = "";
			String sError = "";

			// if (!msDefectSysid.equals("")) {
			// sSQL += "DELETE  ANALYSISRESULT_QCM WHERE SYSID='"
			// + msDefectSysid + "'  ;";
			// sSQL += "DELETE  ANALYSISRESULT_QCM_H WHERE SYSID='"
			// + msDefectSysid + "';  ";
			// sSQL += "DELETE  ANALYSISRESULT_QCD WHERE SYSID='"
			// + msDefectSysid + "';  ";
			// sSQL += "DELETE  ANALYSISRESULT_QCD_H WHERE SYSID='"
			// + msDefectSysid + "' ; ";
			// }
			String sSql = "SELECT " + MESCommon.SysId + " AS SYSID";
			String sresult = db.GetData(sSql, lsSysid);
			if (lsSysid.size() > 0) {
				msDefectSysid = lsSysid.get(0).get("SYSID").toString();
			}
			sSQL += "INSERT INTO ANALYSISRESULT_QCM (SYSID, ANALYSISFORMSID, SAMPLETIMES, SUPPLYLOTID, PRODUCTID, SUPPLYID, PRODUCTORDERID,"
					+ "PRODUCTCOMPID, ISPRINCIPALCOMPID, PRODUCTSERIALNUMBER, STEPID, EQPID, QC_ITEM, QCTYPE, RESPONSIBLESTEPID, RESPONSIBLEQPID,"
					+ "RESPONSIBLETEAM, RESPONSIBLEUSERID, RESPONSIBLEUSERNAME, PRODUCEDP,  QCUSERID, QCUSERNAME, MODIFYUSERID, MODIFYUSER, MODIFYTIME) "
					+ "VALUES ('"+ msDefectSysid+ "','"+ msAnalysisformsID+ "', '"+ msSampletimes	+ "','"	+ msSUPPLYLOTID	+ "', '"+ msProductId+ "','"+ msSUPPLYID+ "','"	+ msProductOrderId+ "',"
					+ "'"+ msProductCompId+ "', '"+ msIsComp+ "','"+ msProductSerialNumber+ "','"
					+ msStepId+ "','"+ msEqpId+ "', '"+ msQC_ITEM	+ "','"	+ msQCTYPE+ "', '','','','','','', '"
					+ MESCommon.UserId+ "','"+ MESCommon.UserName+ "', '"+ MESCommon.UserId	+ "','"
					+ MESCommon.UserName + "'," + MESCommon.ModifyTime + "); ";

			sSQL += "INSERT INTO ANALYSISRESULT_QCM_H (SYSID, ANALYSISFORMSID, SAMPLETIMES, SUPPLYLOTID, PRODUCTID, SUPPLYID, PRODUCTORDERID,"
					+ "PRODUCTCOMPID, ISPRINCIPALCOMPID, PRODUCTSERIALNUMBER, STEPID, EQPID, QC_ITEM, QCTYPE, RESPONSIBLESTEPID, RESPONSIBLEQPID,"
					+ "RESPONSIBLETEAM, RESPONSIBLEUSERID, RESPONSIBLEUSERNAME, PRODUCEDP,  QCUSERID, QCUSERNAME, MODIFYUSERID, MODIFYUSER, MODIFYTIME) "
					+ "VALUES ('"+ msDefectSysid+ "','"+ msAnalysisformsID+ "', '"+ msSampletimes	+ "','"	+ msSUPPLYLOTID	+ "', '"+ msProductId+ "','"+ msSUPPLYID+ "','"	+ msProductOrderId+ "',"
					+ "'"+ msProductCompId+ "', '"+ msIsComp+ "','"+ msProductSerialNumber+ "','"
					+ msStepId+ "','"+ msEqpId+ "', '"+ msQC_ITEM	+ "','"	+ msQCTYPE+ "', '','','','','','', '"
					+ MESCommon.UserId+ "','"+ MESCommon.UserName+ "', '"+ MESCommon.UserId	+ "','"
					+ MESCommon.UserName + "'," + MESCommon.ModifyTime + "); ";
			for (int i = 0; i < lsDefectTable.size(); i++) {
				sSQL += "INSERT INTO ANALYSISRESULT_QCD (SYSID, ANALYSISFORMSID, SUPPLYLOTID, PRODUCTID, SUPPLYID,     PRODUCTORDERID, PRODUCTCOMPID, "
						+ "ISPRINCIPALCOMPID,     PRODUCTSERIALNUMBER, STEPID, EQPID, QC_ITEM,QCTYPE, DEFECTGROUPID, DEFECTID, DEFECTNAME, ISPRINCIPALDEFECT,"
						+ "HANDLETYPE, DEFECTNUM, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ("
						+ "'"+ msDefectSysid+ "','"	+ msAnalysisformsID	+ "','"
						+ msSUPPLYLOTID	+ "', '"+ msProductId+ "','"+ msSUPPLYID+ "','"	+ msProductOrderId+ "',"+ "'"+ msProductCompId	+ "', 'Y','"+ msProductSerialNumber	+ "','"	+ msStepId+ "','"	+ msEqpId+ "', '"+ msQC_ITEM+ "','"	+ msQCTYPE+ "','"	+ lsDefectTable.get(i).get("DefectType").toString()
						+ "','"	+ lsDefectTable.get(i).get("DefectID").toString()+ "','"	+ lsDefectTable.get(i).get("DefectName").toString()
						+ "','Y', '',"+ lsDefectTable.get(i).get("DefectNumber").toString()+ ", '"+ MESCommon.UserId+ "','"+ MESCommon.UserName+ "',"	+ MESCommon.ModifyTime	+ " ); ";

				sSQL += "INSERT INTO ANALYSISRESULT_QCD_H (SYSID, ANALYSISFORMSID, SUPPLYLOTID, PRODUCTID, SUPPLYID,     PRODUCTORDERID, PRODUCTCOMPID, "
						+ "ISPRINCIPALCOMPID,     PRODUCTSERIALNUMBER, STEPID, EQPID, QC_ITEM,QCTYPE, DEFECTGROUPID, DEFECTID, DEFECTNAME, ISPRINCIPALDEFECT,"
						+ "HANDLETYPE, DEFECTNUM, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES ("
						+ "'"+ msDefectSysid+ "','"	+ msAnalysisformsID	+ "','"
						+ msSUPPLYLOTID	+ "', '"+ msProductId+ "','"+ msSUPPLYID+ "','"	+ msProductOrderId+ "',"+ "'"+ msProductCompId	+ "', 'Y','"+ msProductSerialNumber	+ "','"	+ msStepId+ "','"	+ msEqpId+ "', '"+ msQC_ITEM+ "','"	+ msQCTYPE+ "','"	+ lsDefectTable.get(i).get("DefectType").toString()
						+ "','"	+ lsDefectTable.get(i).get("DefectID").toString()+ "','"	+ lsDefectTable.get(i).get("DefectName").toString()
						+ "','Y','',"+ lsDefectTable.get(i).get("DefectNumber").toString()+ ", '"+ MESCommon.UserId+ "','"+ MESCommon.UserName+ "',"	+ MESCommon.ModifyTime	+ " ); ";

			}
			sError = db.ExecuteSQL(sSQL);

			if (sError != "") {
				MESCommon.showMessage(WIP_Defect.this, sError);
			}

		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_Defect.this, e.toString());
		}
	}

	public void Clear() {
		try {
			
			msProductOrderId="";msProductId="";  msProductCompId="";  msProductSerialNumber="";  msStepId="";  
			msEqpId="";msAnalysisformsID="";msSampletimes="";msStepSEQ="";msSUPPLYLOTID="";msSUPPLYID="";msQC_ITEM="";msQCTYPE="";
			msDefectSysid="" ;msTraceType=""; msIsComp="";
            lsCompID .clear();lsDefectTable.clear();
            setFocus(editInput);
            editInput.setText("");
            adapter.notifyDataSetChanged();
		} catch (Exception e) {
			MESCommon.showMessage(WIP_Defect.this, e.toString());
		}

	}
	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(WIP_Defect.this, e.toString());
		}

	}
}
