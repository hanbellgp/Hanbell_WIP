package com.example.hanbell_wip;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.CheckBox;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.EQP_Start.SpinnerData;


import android.widget.Toast;

public class FINISH_TEMP  extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button  btnOk;
	EditText editScanId, editItnbr,editItdsc,editStep;
	Spinner  spWrcode;
	TextView h1, h2,h3,h4;
	wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	String msTime="", msSelWrcode = "";
	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_finish_temp);
		// 取得控件
		spWrcode = (Spinner) findViewById(R.id.finishtempedit_spWrcode);
		editScanId = (EditText) findViewById(R.id.finishtempedit_tvScanId);		
		editItnbr = (EditText) findViewById(R.id.finishtempedit_tvItnbr);
		editItdsc = (EditText) findViewById(R.id.finishtempedit_tvItdsc);
		editStep = (EditText) findViewById(R.id.finishtempedit_tvStepId);
		btnOk= (Button) findViewById(R.id.finishtempedit_btnConfirm);
		h1= (TextView) findViewById(R.id.finishtempedit_h1);
		h2= (TextView) findViewById(R.id.finishtempedit_h2);
		h3= (TextView) findViewById(R.id.finishtempedit_h3);
		h4= (TextView) findViewById(R.id.finishtempedit_h4);
		h1.setTextColor(Color.WHITE);
		h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE);
		h2.setBackgroundColor(Color.DKGRAY);
		h3.setTextColor(Color.WHITE);
		h3.setBackgroundColor(Color.DKGRAY);
		h4.setTextColor(Color.WHITE);
		h4.setBackgroundColor(Color.DKGRAY);
		
		lv0 = (ListView) findViewById(R.id.finishtempedit_lv0);				
		adapter = new wiptrackinAdapter(lsCompTable, this);
		lv0.setAdapter(adapter);	
		
		
		// 读取工作单位
		List<HashMap<String, String>> lsWrcode = new ArrayList<HashMap<String, String>>();
		String sSql = "", sResult = "";
		sSql = "SELECT DISTINCT PHRASEID FROM MPHRASE WHERE PHRASETYPE='PD_WRCODE' ORDER BY CAST(SEQ AS INT)";
		sResult = db.GetData(sSql, lsWrcode);
		if (sResult != "") {
			MESCommon.showMessage(FINISH_TEMP.this, sResult);
			finish();
		}
		List<SpinnerData> lst = new ArrayList<SpinnerData>();
		SpinnerData c = new SpinnerData("", "");
		lst.add(c);
		for (int i = 0; i < lsWrcode.size(); i++) {
			c = new SpinnerData(lsWrcode.get(i).get("PHRASEID").toString(), lsWrcode.get(i).get("PHRASEID").toString());
			lst.add(c);
		}
		ArrayAdapter<SpinnerData> AdapterSP = new ArrayAdapter<SpinnerData>(FINISH_TEMP.this,	android.R.layout.simple_spinner_item, lst);
		AdapterSP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spWrcode.setAdapter(AdapterSP);
		
		editScanId.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
			 try {
					EditText editScanId = (EditText) findViewById(R.id.finishtempedit_tvScanId);
					SpinnerData oWrcode = (SpinnerData) spWrcode.getSelectedItem();
					msSelWrcode = oWrcode.text.toString();
					
					if (msSelWrcode.equals("")) {
						MESCommon.show(FINISH_TEMP.this, "请先选择工作单位!");
						editScanId.setText("");
						setFocus(editItnbr );
						return false;
					}
					
					RadioButton	radio1=(RadioButton)findViewById(R.id.finishtempedit_radio1);     
					RadioButton	radio2=(RadioButton)findViewById(R.id.finishtempedit_radio2);
					
					if (editScanId.getText().toString().trim().length() == 0) {
						MESCommon.show(FINISH_TEMP.this, "请扫描制造号码!");
						editScanId.setText("");
						setFocus(editItnbr );
						return false;
					}
					editScanId.setText(editScanId.getText().toString().trim().toUpperCase()); 
					lsCompID.clear();
					msTime="";
					if(radio1.isChecked())
					{
						msTime="1";
					}else if(radio2.isChecked())
					{
						msTime="2";
					}
					List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
					String sSql = "", sResult = "";
					sSql = "SELECT * FROM COMP_TEMP_WIP WHERE PRODUCTCOMPID='" + editScanId.getText().toString() + "' AND TIMES='" + msTime + "'";
					sResult = db.GetData(sSql, ls);
					if (sResult != "") {
						MESCommon.showMessage(FINISH_TEMP.this, sResult);
						return false;
					}
					if (ls.size() > 0)
					{
						MESCommon.showMessage(FINISH_TEMP.this, "制造号码「" + editScanId.getText().toString() + "」已有盘点记录，不能重复扫描！\n盘点人员：" + ls.get(0).get("USERNAME").toString() + "\n盘点时间：" + ls.get(0).get("MODIFYTIME").toString());
						return false;
					}
					
				    String sError = db.BindFINISHTEMP(editScanId.getText().toString(), msSelWrcode, lsCompID);
					if (!sError.equals("")) {
						MESCommon.show(FINISH_TEMP.this, sError);
						return false;
					}
					editItnbr.setText(lsCompID.get(0).get("PRODUCTID").toString()); 
					editItdsc.setText(lsCompID.get(0).get("PRODUCTNAME").toString()); 
					editStep.setText(lsCompID.get(0).get("STEPID").toString());
		    		
					setFocus(editItnbr);
					
		
				} catch (Exception e) {
					MESCommon.showMessage(FINISH_TEMP.this, e.toString());
					return false;
				}
					
			  }
				return false;
			}
		});
		
		editItnbr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				  setFocus(editScanId);
				  
			}
		});

		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
					if(lsCompID.size()>0)
					{
						String sSql= "";
						sSql = "DELETE FROM COMP_TEMP_WIP WHERE PRODUCTCOMPID='" + lsCompID.get(0).get("PRODUCTCOMPID").toString() + "' AND TIMES='" + msTime + "';";
						sSql += "INSERT INTO COMP_TEMP_WIP (SYSID, PRODUCTID, PRODUCTORDERID, PRODUCTCOMPID, PRODUCTSERIALNUMBER, MATERIALID, " +
								"RAWPROCESSID, STEPID, STEPSEQ, EQPID, WRCODE, USERID, USERNAME, MODIFYTIME, NEWSTEPID, TIMES, SELWRCODE) VALUES(  "
							    + MESCommon.SysId + ",'"
	                            + lsCompID.get(0).get("PRODUCTID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTORDERID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTCOMPID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTCOMPID").toString()+ "','"
	                            + lsCompID.get(0).get("PRODUCTID").toString()+ "','"
	                            + ""+ "','"
	                            + lsCompID.get(0).get("STEPID").toString()+ "','"
					            + lsCompID.get(0).get("STEPSEQ").toString()+ "','"
					            + lsCompID.get(0).get("EQPID").toString()+ "','"
			            		+ lsCompID.get(0).get("WRCODE").toString()+ "','"
					            + MESCommon.UserId + "','"
	                            + MESCommon.UserName + "',"
	                            + MESCommon.ModifyTime + ",'','"
	                            + msTime+ "','" + msSelWrcode + "');";
						String sError=db.ExecuteSQL(sSql);
				    	if (sError != "") {
							MESCommon.showMessage(FINISH_TEMP.this, sError);
							return ;
						}
				    	
						HashMap<String, String> hs = new HashMap<String, String>();
						hs.put("PRODUCTCOMPID",lsCompID.get(0).get("PRODUCTCOMPID").toString());
						hs.put("PRODUCTID",lsCompID.get(0).get("PRODUCTID").toString());
						hs.put("PRODUCTNAME",lsCompID.get(0).get("PRODUCTNAME").toString());
						hs.put("STEPID",lsCompID.get(0).get("STEPID").toString());
						hs.put("NEWSTEPID","");
						hs.put("CHECKFLAG", "N");
						lsCompTable.add(0,  hs);
						
				    	adapter.notifyDataSetChanged();	
				    	//Toast.makeText(FINISH_TEMP.this, "条码【"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"】,工序更新成功!",Toast.LENGTH_SHORT).show(); 
				    	Toast.makeText(FINISH_TEMP.this, "制造号码【"+lsCompID.get(0).get("PRODUCTCOMPID").toString()+"】,加入盘点列表成功!",Toast.LENGTH_SHORT).show(); 
				    	setFocus(editItnbr );
				    	Clear();
					}
				} catch (Exception e) {
					MESCommon.showMessage(FINISH_TEMP.this, e.toString());
				}
			}
		});
		
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
			try{
				if (convertView == null) {
					// 获得ViewHolder对象
					holder = new ViewHolder();
					// 导入布局并赋值给convertview
					convertView = inflater.inflate(R.layout.activity_finish_temp_listview, null);
					
					holder.tvProductCompId = (TextView) convertView.findViewById(R.id.finishtemp_tvProductCompId);
					holder.tvStepId = (TextView) convertView.findViewById(R.id.finishtemp_tvStepId);
					holder.tvItnbr = (TextView) convertView.findViewById(R.id.finishtemp_tvItnbr);
					holder.tvItdsc = (TextView) convertView.findViewById(R.id.finishtemp_tvItdsc);
		
					// 为view设置标签
					convertView.setTag(holder);
				} else {
					// 取出holder
					holder = (ViewHolder) convertView.getTag();
					
				}
				// 设置list中TextView的显示
				holder.tvProductCompId.setText(getItem(position).get("PRODUCTCOMPID").toString());	
				holder.tvStepId.setText(getItem(position).get("STEPID").toString());
				holder.tvItnbr.setText(getItem(position).get("PRODUCTID").toString());	
				holder.tvItdsc.setText(getItem(position).get("PRODUCTNAME").toString());
				
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
			catch (Exception e) {
			
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
			TextView tvProductCompId;
			TextView tvStepId;
			TextView tvItnbr;
			TextView tvItdsc;
		}
	}
	
	public void Clear() {
		try {	
			editScanId.setText("");
			editItnbr.setText("");		
			editItdsc.setText("");		
		    editStep.setText("");		
			editItdsc.setText("");		
            lsCompID .clear(); 
            adapter.notifyDataSetChanged();
            setFocus(editItnbr);
            //MESCommon.setSpinnerItemSelectedByValue(spWrcode,"");
		} catch (Exception e) {
			MESCommon.showMessage(FINISH_TEMP.this, e.toString());
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

	
}

