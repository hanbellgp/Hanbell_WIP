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

public class SEMI_TEMP  extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button  btnOk ;
	EditText editInput, editMaterialID,editMaterialName,editStep;
	Spinner  spStep;
	TextView h7, h8,h9,h10;
	wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	String msTime="";
	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsDefStep = new ArrayList<HashMap<String, String>>();
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semi_temp);
		// 取得控件
		editInput = (EditText) findViewById(R.id.semitempedit_tvInput);		
		editMaterialID = (EditText) findViewById(R.id.semitempedit_tveditMaterialID);
		editMaterialName = (EditText) findViewById(R.id.semitempedit_tveditMaterialName);
		editStep = (EditText) findViewById(R.id.semitempedit_tvStepID);
		spStep = (Spinner) findViewById(R.id.semitempedit_spStep);
		btnOk= (Button) findViewById(R.id.semitempedit_btnConfirm);
		h7= (TextView) findViewById(R.id.semitempedit_h7);
		h8= (TextView) findViewById(R.id.semitempedit_h8);
		h9= (TextView) findViewById(R.id.semitempedit_h9);
		h10= (TextView) findViewById(R.id.semitempedit_h10);
		h7.setTextColor(Color.WHITE);
		h7.setBackgroundColor(Color.DKGRAY);
		h8.setTextColor(Color.WHITE);
		h8.setBackgroundColor(Color.DKGRAY);
		h9.setTextColor(Color.WHITE);
		h9.setBackgroundColor(Color.DKGRAY);
		h10.setTextColor(Color.WHITE);
		h10.setBackgroundColor(Color.DKGRAY);
		
		lv0 = (ListView) findViewById(R.id.semitempedit_lv0);				
		adapter = new wiptrackinAdapter(lsCompTable, this);
		lv0.setAdapter(adapter);	
		
		
		// 读取工序
		lsDefStep.clear();

		ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
		
		SpinnerData c0 = new SpinnerData("", "");
		lst.add(c0);
			SpinnerData c = new SpinnerData("方型件精加工", "方型件精加工");
			lst.add(c);
			SpinnerData c1 = new SpinnerData("方型件清洗", "方型件清洗");
			lst.add(c1);
			SpinnerData c2 = new SpinnerData("圆型件齿形粗铣NSM", "圆型件齿形粗铣NSM");
			lst.add(c2);	
			SpinnerData c3 = new SpinnerData("圆型件轴径精车NL", "圆型件轴径精车NL");
			lst.add(c3);
			SpinnerData c4 = new SpinnerData("圆型件轴径研磨CG", "圆型件轴径研磨CG");
			lst.add(c4);
			SpinnerData c5 = new SpinnerData("圆型件精研KAPP", "圆型件精研KAPP");
			lst.add(c5);
			SpinnerData c6 = new SpinnerData("圆型件清洗包装", "圆型件清洗包装");
			lst.add(c6);
			
		ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(
				SEMI_TEMP.this,
				android.R.layout.simple_spinner_item, lst);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spStep.setAdapter(Adapter1);
		
	
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
			 try {
					EditText txtInput = (EditText) findViewById(R.id.semitempedit_tvInput);
				      
					RadioButton	radio1=(RadioButton)findViewById(R.id.radio1);     
					RadioButton	radio2=(RadioButton)findViewById(R.id.radio2); 
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(SEMI_TEMP.this, "请扫描条码!");
						txtInput.setText("");
						setFocus(editMaterialID );
						return false;
					}
					editInput.setText(editInput.getText().toString().trim().toUpperCase()); 
					lsCompID.clear();
					msTime="";
					if(radio1.isChecked())
					{
						msTime="1";
					}else if(radio2.isChecked())
					{
						msTime="2";
					}
					
				    String sError = db.BindCOMPTEMP(editInput.getText().toString() , MESCommon.UserId, MESCommon.UserName,msTime,lsCompID);
					if (!sError.equals("")) {
						MESCommon.show(SEMI_TEMP.this, sError);
						return false;
					}	
					editMaterialID.setText(lsCompID.get(0).get("MATERIALID").toString()); 
					editMaterialName.setText(lsCompID.get(0).get("MATERIALNAME").toString()); 
					editStep.setText(lsCompID.get(0).get("STEPID").toString()); 
		            if(lsCompID.get(0).get("STEPID").toString().equals(""))
		            {
		                spStep.setEnabled(false);
		            
		            }else {
		            	spStep.setEnabled(true);
					}
		    		MESCommon.setSpinnerItemSelectedByValue(spStep,lsCompID.get(0).get("STEPID").toString());
//					HashMap<String, String> hs = new HashMap<String, String>();	
//					hs.put("PRODUCTSERIALNUMBER",lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());				
//					hs.put("MATERIALID",lsCompID.get(0).get("MATERIALID").toString());
//					hs.put("MATERIALNAME",lsCompID.get(0).get("MATERIALNAME").toString());
//					hs.put("STEPID",lsCompID.get(0).get("STEPID").toString());
//					hs.put("NEWSTEPID","");
//					hs.put("CHECKFLAG", "N");			
//					lsCompTable.add(0,  hs);					    
//					adapter.notifyDataSetChanged();	
//					Toast.makeText(SEMI_TEMP.this, "条码【"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"】,加入盘点列表成功!",Toast.LENGTH_SHORT).show(); 
//				    editInput.setText("");
			
					setFocus(editMaterialID);
					
		
				} catch (Exception e) {
					MESCommon.showMessage(SEMI_TEMP.this, e.toString());
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


		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					
					SpinnerData STEP = (SpinnerData) spStep .getSelectedItem();
					
					if(lsCompID.size()>0)
					{						
					if (!editStep.getText().toString().equals(STEP.text.toString())) {
						
					 AlertDialog alert=	new AlertDialog.Builder(SEMI_TEMP.this).setTitle("确认").setMessage("条码【" +lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString() + "】目前工序【"+lsCompID.get(0).get("STEPID").toString()+"】,是否修改成【"+STEP.text+"】!")
								.setPositiveButton("确定",new DialogInterface.OnClickListener() {  
				            @Override  
				            public void onClick(DialogInterface dialog,int which) {  
				            	SpinnerData STEP = (SpinnerData) spStep .getSelectedItem();
								String sSql="INSERT INTO COMP_TEMP (SYSID,  PRODUCTID, PRODUCTORDERID,PRODUCTCOMPID, PRODUCTSERIALNUMBER,MATERIALID,RAWPROCESSID, STEPID, STEPSEQ,NEWSTEPID,TIMES,USERID,USERNAME, MODIFYTIME) VALUES ( "
										    + MESCommon.SysId + ",'"
				                            + lsCompID.get(0).get("PRODUCTID").toString() + "','"
				                            + lsCompID.get(0).get("PRODUCTORDERID").toString() + "','"
				                            + lsCompID.get(0).get("PRODUCTCOMPID").toString() + "','"
				                            + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+ "','"
				                            + lsCompID.get(0).get("MATERIALID").toString()+ "','"
				                            + lsCompID.get(0).get("RAWPROCESSID").toString()+ "','"
				                            + lsCompID.get(0).get("STEPID").toString()+ "','"
								            + lsCompID.get(0).get("STEPSEQ").toString()+ "','"
								            +STEP.text+ "','"
								            +msTime+ "','"
								            + MESCommon.UserId + "','"
				                            +MESCommon.UserName + "',"
				                            + MESCommon.ModifyTime + ");";
						        String sError=db.ExecuteSQL(sSql);
						    	if (sError != "") {
									MESCommon.showMessage(SEMI_TEMP.this, sError);
									return ;
								}						    	
						    	HashMap<String, String> hs = new HashMap<String, String>();	
								hs.put("PRODUCTSERIALNUMBER",lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());				
								hs.put("MATERIALID",lsCompID.get(0).get("MATERIALID").toString());
								hs.put("MATERIALNAME",lsCompID.get(0).get("MATERIALNAME").toString());
								hs.put("STEPID",lsCompID.get(0).get("STEPID").toString());
								hs.put("NEWSTEPID",STEP.text);
								hs.put("CHECKFLAG", "N");			
								lsCompTable.add(0,  hs);					    
							
						    	adapter.notifyDataSetChanged();	
						    	Toast.makeText(SEMI_TEMP.this, "条码【"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"】,工序更新成功!",Toast.LENGTH_SHORT).show(); 
						    	setFocus(editMaterialID );
						    	Clear();
				            }  
				        })  
						.setNeutralButton("取消",new DialogInterface.OnClickListener() {  
						            @Override  
						            public void onClick(DialogInterface dialog,int which) {  
						                // TODO Auto-generated method stub  
										editInput.setText("");
										setFocus(editMaterialID);		
						            	return ;
						            }  
						 }).show();  
					return  ;
					}
					else {
					
						String sSql="INSERT INTO COMP_TEMP (SYSID,  PRODUCTID, PRODUCTORDERID,PRODUCTCOMPID, PRODUCTSERIALNUMBER,MATERIALID,RAWPROCESSID, STEPID, STEPSEQ,NEWSTEPID,TIMES,USERID,USERNAME, MODIFYTIME) VALUES (  "
							    + MESCommon.SysId + ",'"
	                            + lsCompID.get(0).get("PRODUCTID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTORDERID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTCOMPID").toString() + "','"
	                            + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+ "','"
	                            + lsCompID.get(0).get("MATERIALID").toString()+ "','"
	                            + lsCompID.get(0).get("RAWPROCESSID").toString()+ "','"
	                            + lsCompID.get(0).get("STEPID").toString()+ "','"
					            + lsCompID.get(0).get("STEPSEQ").toString()+ "','','"
					            +msTime+ "','"
					            + MESCommon.UserId + "','"
	                            +MESCommon.UserName + "',"
	                            + MESCommon.ModifyTime + ");";
			        String sError=db.ExecuteSQL(sSql);
				    	if (sError != "") {
							MESCommon.showMessage(SEMI_TEMP.this, sError);
							return ;
						}						    	
				    	HashMap<String, String> hs = new HashMap<String, String>();	
						hs.put("PRODUCTSERIALNUMBER",lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());				
						hs.put("MATERIALID",lsCompID.get(0).get("MATERIALID").toString());
						hs.put("MATERIALNAME",lsCompID.get(0).get("MATERIALNAME").toString());
						hs.put("STEPID",lsCompID.get(0).get("STEPID").toString());
						hs.put("NEWSTEPID","");
						hs.put("CHECKFLAG", "N");			
						lsCompTable.add(0,  hs);					    
				
				    	adapter.notifyDataSetChanged();	
				    	//Toast.makeText(SEMI_TEMP.this, "条码【"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"】,工序更新成功!",Toast.LENGTH_SHORT).show(); 
				    	Toast.makeText(SEMI_TEMP.this, "条码【"+lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+"】,加入盘点列表成功!",Toast.LENGTH_SHORT).show(); 
				    	setFocus(editMaterialID );
				    	Clear();
					}
					}
				} catch (Exception e) {
					MESCommon.showMessage(SEMI_TEMP.this, e.toString());
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
					convertView = inflater.inflate(R.layout.activity_semi_temp_listview, null);
					
					holder.tvSerialnumberseq = (TextView) convertView.findViewById(R.id.semitemp_tvSerialnumberseq);
					holder.tvStepId = (TextView) convertView.findViewById(R.id.semitemp_tvstepid);
					holder.tvMaterialId = (TextView) convertView.findViewById(R.id.semitemp_tvMaterialId);
					holder.tvMaterialName = (TextView) convertView.findViewById(R.id.semitemp_tvMaterialName);
					holder.tvNewStep = (TextView) convertView.findViewById(R.id.semitemp_tvNewStep);
		
					// 为view设置标签
					convertView.setTag(holder);
				} else {
					// 取出holder
					holder = (ViewHolder) convertView.getTag();
					
				}
				// 设置list中TextView的显示
				holder.tvSerialnumberseq.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
				holder.tvStepId.setText(getItem(position).get("STEPID").toString());
				holder.tvMaterialId.setText(getItem(position).get("MATERIALID").toString());	
				holder.tvMaterialName.setText(getItem(position).get("MATERIALNAME").toString());
				holder.tvNewStep.setText(getItem(position).get("NEWSTEPID").toString());
				
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
			TextView tvSerialnumberseq;
			TextView tvStepId;
			TextView tvMaterialId;
			TextView tvMaterialName;
			TextView tvNewStep;
		}
	}
	

	public void Clear() {
		try {	
			editInput.setText("");
			editMaterialID.setText("");		
			editMaterialName.setText("");		
		    editStep.setText("");		
			editMaterialName.setText("");		
            lsCompID .clear(); 
            adapter.notifyDataSetChanged();
            setFocus(editMaterialID);
            MESCommon.setSpinnerItemSelectedByValue(spStep,"");
		} catch (Exception e) {
			MESCommon.showMessage(SEMI_TEMP.this, e.toString());
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

