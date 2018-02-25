package com.example.hanbell_wip;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CheckBox;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.hanbell_wip.Class.*;

import android.widget.Toast;

public class SEMI_ReWork  extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button  btnExit,btnOk,btnDelete ;
	EditText editInput,editProductORderID, editQty;
	TextView h0, h8,h9;
	wiptrackinAdapter adapter;	
	PrefercesService prefercesService;

	// 该物料的HashMap记录
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semi_rework);
		// 取得控件
		editInput = (EditText) findViewById(R.id.semireworkedit_tvInput);		
		editProductORderID = (EditText) findViewById(R.id.semireworkedit_tvProductOrderid);
		editQty= (EditText) findViewById(R.id.semireworkedit_tvQty);
		h0= (TextView) findViewById(R.id.semireworkedit_h0);
		h8= (TextView) findViewById(R.id.semireworkedit_h8);
		h9= (TextView) findViewById(R.id.semireworkedit_h9);
		h0.setTextColor(Color.WHITE);
		h0.setBackgroundColor(Color.DKGRAY);
		h8.setTextColor(Color.WHITE);
		h8.setBackgroundColor(Color.DKGRAY);
		h9.setTextColor(Color.WHITE);
		h9.setBackgroundColor(Color.DKGRAY);
		h9.setBackgroundColor(Color.DKGRAY);
		
		
		lv0 = (ListView) findViewById(R.id.semireworkedit_lv0);
		btnOk = (Button) findViewById(R.id.semireworkedit_btnOK);	
		btnDelete = (Button) findViewById(R.id.semireworkedit_btnDelete);	
	    btnExit = (Button) findViewById(R.id.semireworkedit_btnExit);			
		adapter = new wiptrackinAdapter(lsCompTable, this);
		lv0.setAdapter(adapter);		
		lv0.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
				wiptrackinAdapter.ViewHolder holder = (wiptrackinAdapter.ViewHolder) arg1
						.getTag();
				// 改变CheckBox的状态
				holder.cb.toggle();
				// 将CheckBox的选中状况记录下来
				wiptrackinAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				// 将CheckBox的选中状况记录下来
				if (holder.cb.isChecked()) {
					lsCompTable.get(position).put("CHECKFLAG", "Y");
				} else {
					lsCompTable.get(position).put("CHECKFLAG", "N");
				}
			}
		});
		editInput.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
					// 查询交货单
					try {
					EditText txtInput = (EditText) findViewById(R.id.semireworkedit_tvInput);
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(SEMI_ReWork.this, "请扫描条码!");
						txtInput.setText("");
						setFocus(editProductORderID );
						return false;
					}
					editInput.setText(editInput.getText().toString().trim().toUpperCase()); 
					if(editProductORderID.getText().toString().trim().equals(""))
					{
						List<HashMap<String, String>> lsPlan = new ArrayList<HashMap<String, String>>();
						lsPlan.clear();
						String sSQL = "SELECT DISTINCT A.PRODUCTORDERID,A.PRODUCTID,B.PRODUCTNAME,B.PRODUCTMODEL,A.PROCESSSTATE, A.QTY,  COUNT(C.PRODUCTORDERID) LEFTQTY,A.ISEND,A.PRODUCTORDERTYPE FROM PROCESS_PRE A  "
									  + " LEFT JOIN MPRODUCT B ON A.PRODUCTID=B.PRODUCTID "
									   + " LEFT JOIN  PROCESS C ON A.PRODUCTORDERID= C.PRODUCTORDERID  AND  ISNULL( PRODUCTCOMPID,'') =''  "
									   + "  WHERE  A.PRODUCTORDERID='" + editInput.getText().toString().trim().toUpperCase() + "'  GROUP BY A.PRODUCTORDERID,A.PRODUCTID,B.PRODUCTNAME,B.PRODUCTMODEL ";
						
					
					    String sError = db.GetData(sSQL, lsPlan);
						if (!sError.equals("")) {
							MESCommon.show(SEMI_ReWork.this, sError);
							return false;
						}
						if(lsPlan.size()>0)
						{
							
							 //可报工数是0
	                        if (lsPlan.get(0).get("LEFTQTY").toString() == "0")
	                        {
	                            if (lsPlan.get(0).get("PROCESSSTATE").toString() == "未下线")
	                            {
	                            	MESCommon.show(SEMI_ReWork.this, "制令[" + editInput.getText().toString().trim().toUpperCase() + "]还没下线，不能报工!");
	                                editInput.setText("");
	                                  setFocus(editProductORderID);
	                                return false;
	                            }
	                            else
	                            {
	                                MESCommon.show(SEMI_ReWork.this, "制令[" + editInput.getText().toString().trim().toUpperCase() + "]可报工数是0，不能报工！");
	                                editInput.setText("");
	                                  setFocus(editProductORderID);
	                                return false;
	                            }

	                        }


	                        if (lsPlan.get(0).get("PRODUCTORDERTYPE").toString() == "一般制令")
	                        {
	                            MESCommon.show(SEMI_ReWork.this, "制令[" + editInput.getText().toString().trim().toUpperCase() + "]是一般制令，不能在重工绑定报工！");
                                editInput.setText("");
                                  setFocus(editProductORderID);
                                return false;
	                        }
	                        
	                        List<HashMap<String, String>> lsBOM = new ArrayList<HashMap<String, String>>();
	                        lsBOM.clear();
	                      
	                        sSQL = "SELECT * FROM ERP_MBOM WHERE  PRODUCTORDERID ='" + editInput.getText().toString().trim().toUpperCase() + "' ";
	                        sError = db.GetData(sSQL, lsBOM);
							if (!sError.equals("")) {
								MESCommon.show(SEMI_ReWork.this, sError);
							    setFocus(editProductORderID);
								return false;
							}
	                      

	                        if (lsBOM.size() == 0)
	                        {
	                        	MESCommon.show(SEMI_ReWork.this,"制令[" + editInput.getText().toString().trim().toUpperCase() + "]还没领料确认，请先通知仓库领料确认!");
	                        	setFocus(editProductORderID);
	                        	return false;
	                        }  
	                        
	                        editProductORderID.setText(editInput.getText().toString());
							editQty.setText(lsPlan.get(0).get("QTY").toString());
							
	                        
						}else
						{
							MESCommon.show(SEMI_ReWork.this, "制令号码：【"+editInput.getText().toString().trim()+"】,在MES系统中不存在！");
							return false;
						}
						editInput.setText("");						
					}else
					{	
						if(lsCompTable.size()<= (Integer.parseInt(editQty.getText().toString().trim())-1))
						{
							for (int i = 0; i < lsCompTable.size(); i++) {
								if (lsCompTable.get(i).get("SerialnumberID").toString().equals(txtInput.getText().toString().trim())) {
									MESCommon.show(SEMI_ReWork.this, "工件条码["+ txtInput.getText().toString().trim()+ "] 已在清单中,请选择新的工件条码!");
									setFocus(editProductORderID);
									return false;
								}
							}
		
							lsCompID.clear();
							String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","半成品","", lsCompID);
						    if (!sResult.equals(""))
		                    {   MESCommon.show(SEMI_ReWork.this,sResult);
		                        setFocus(editProductORderID);
						    	return false;
		                    }	
						    List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
					        lsProcess.clear();
						    String sSQL = "SELECT * FROM PROCESS WHERE  PRODUCTORDERID ='" + editProductORderID.getText().toString().trim() + "' AND   PRODUCTSERIALNUMBER ='" + lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString() + "'";
						    String sError = db.GetData(sSQL, lsProcess);
							if (!sError.equals("")) {
								MESCommon.show(SEMI_ReWork.this, sError);
								setFocus(editProductORderID);
								return false;
							}
	                      
	                        if (lsProcess.size() > 0)
	                        {
	                        	MESCommon.show(SEMI_ReWork.this, "工件[" +lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+ "]已经绑定了制令["+editProductORderID.getText().toString().trim()+"]，不能重复绑定!");
	                        	setFocus(editProductORderID);
	                        	return false;
	                        } 
	                        
						    
							HashMap<String, String> hs = new HashMap<String, String>();	
							hs.put("OrderId",editProductORderID.getText().toString());
							if(!lsCompID.get(0).get("FINEPROCESSID").toString().equals(""))
							{
								hs.put("Serialnumber",lsCompID.get(0).get("FINEPROCESSID").toString());
							}else if(!lsCompID.get(0).get("RAWPROCESSID").toString().equals(""))
							{
								hs.put("Serialnumber",lsCompID.get(0).get("RAWPROCESSID").toString());
							}				
							hs.put("SerialnumberID",lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
							hs.put("CHECKFLAG", "N");			
							lsCompTable.add(hs);	
						    
							adapter.notifyDataSetChanged();	
						}
						else
						{
							MESCommon.show(SEMI_ReWork.this, "制令号码：【"+editProductORderID.getText().toString()+"】,已经全部绑定完！请选择新的制令");
							return false;
						}
					    editInput.setText("");
						setFocus(editProductORderID);
				   }
				} catch (Exception e) {
					MESCommon.showMessage(SEMI_ReWork.this, e.toString());
					return false;
				}
					
			  }
				return false;
			}
		});
		// btnRemove
		btnDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							if (lsCompTable.size() > 0) {
								List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
								Boolean isSelect = false;

								for (int i = 0; i < lsCompTable.size(); i++) {
									if (lsCompTable.get(i).get("CHECKFLAG").toString().equals("Y")) {
										isSelect = true;
									}
									lsCompTableCopy.add(lsCompTable.get(i));
								}
								if (!isSelect) {
									MESCommon.show(SEMI_ReWork .this,	"请选择要删除的零部件");
									return;
								}
								for (int i = lsCompTableCopy.size() - 1; i >= 0; i--) {
									if (lsCompTableCopy.get(i).get("CHECKFLAG")	.toString().equals("Y")) 
									{
										lsCompTable.remove(i);
									}
								}
								adapter.notifyDataSetChanged();

							}
						} catch (Exception e) {
							// showMessage(e.toString());
						}
					}
				});
		editProductORderID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
						String sProductOrderId=editProductORderID.getText().toString().trim();
						String sCompIds="";
						String sResult="";
						if(lsCompTable.size()==0)
						{
							MESCommon.show(SEMI_ReWork.this, "请先添加要绑定的工件条码，在进行重工制令绑定!");
							return ;
						}
						for (int i = 0; i < lsCompTable.size(); i++) {
							sCompIds=sCompIds+ lsCompTable.get(i).get("SerialnumberID").toString() +";";
						}
						if(sCompIds.length()>0)
						{
							sCompIds=sCompIds.substring(0, sCompIds.length()-1);
						}
						sResult=db.BindReworkComp(sProductOrderId, sCompIds, MESCommon.UserId, MESCommon.UserName);
						if(!sResult.equals(""))
						{
							MESCommon.show(SEMI_ReWork.this, sResult);
							return ;
						}
						Toast.makeText(SEMI_ReWork.this, "绑定成功!",Toast.LENGTH_SHORT).show();
					
						Clear();
					} catch (Exception e) {
						MESCommon.showMessage(SEMI_ReWork.this, e.toString());
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
					MESCommon.showMessage(SEMI_ReWork.this, e.toString());
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
					convertView = inflater.inflate(R.layout.activity_semi_rework_listview, null);
					holder.cb = (CheckBox) convertView.findViewById(R.id.semiReworklv_cb);
					holder.tvOrderId = (TextView) convertView.findViewById(R.id.semiRework_tvOrderId);
					holder.tvSerialnumber = (TextView) convertView.findViewById(R.id.semiRework_tvSerialnumber);
				
					// 为view设置标签
					convertView.setTag(holder);
				} else {
					// 取出holder
					holder = (ViewHolder) convertView.getTag();
					
				}
				// 设置list中TextView的显示
				holder.tvOrderId.setText(getItem(position).get("OrderId").toString());	
				holder.tvSerialnumber.setText(getItem(position).get("Serialnumber").toString());	
				
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
			TextView tvOrderId;
			TextView tvSerialnumber;

		}
	}
	

	public void Clear() {
		try {	
			editInput.setText("");
			editProductORderID.setText("");		
			editQty.setText("");	
            lsCompID .clear(); lsCompTable.clear();
            adapter.notifyDataSetChanged();
            setFocus(editProductORderID);
		} catch (Exception e) {
			MESCommon.showMessage(SEMI_ReWork.this, e.toString());
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
	

}

