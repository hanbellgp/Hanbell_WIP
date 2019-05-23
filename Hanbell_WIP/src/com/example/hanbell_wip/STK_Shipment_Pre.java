package com.example.hanbell_wip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.Class.MESCommon;
import com.example.hanbell_wip.Class.MESDB;
import com.example.hanbell_wip.Class.PrefercesService;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class STK_Shipment_Pre extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button btnConfirm, btnDelete, btnExit, btnTab1;
	EditText editInput, editProudctID, editProductName, editMark, editInCount;

	// CheckBox cb ;
	TextView ht1, ht2, ht3, ht4, ht5;
	LinearLayout tab1;
	stkshipmentPreAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String, String> params;
	String msProductOrderId = "", msProductId, msProductName, msProductModel, msProductCompId,msCustomer,msMark;
	String msLcSeq = "";// 选中的序号
	int miQty;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;
	int milv1RowNum = 0;

	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProcess = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsProduct = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();//列表清单
	private List<HashMap<String, String>> lsLcComp = new ArrayList<HashMap<String, String>>();// LC制造号码
	HashMap<String, String> mapSeqQty = new HashMap<String, String>();// 序号seq与数量的对应关系
	HashMap<String, String> mapSeqItnbr = new HashMap<String, String>();// 序号seq与件号的对应关系

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_stk_shipment_pre);

		// 取得控件

		try {

			editInput = (EditText) findViewById(R.id.stkshipmentpre_tvInput);
			editProudctID = (EditText) findViewById(R.id.stkshipmentpre_tvProudctID); // 件号
			editProductName= (EditText) findViewById(R.id.stkshipmentpre_tvProductName);//品名
			editMark = (EditText) findViewById(R.id.stkshipmentpre_tvMark);//备注
			editInCount = (EditText) findViewById(R.id.stkshipmentpre_tvInCount);//已掃描數

			ht1 = (TextView) findViewById(R.id.stkshipmentpre_ht1);
			ht2 = (TextView) findViewById(R.id.stkshipmentpre_ht2);
			ht3 = (TextView) findViewById(R.id.stkshipmentpre_ht3);
			ht4 = (TextView) findViewById(R.id.stkshipmentpre_ht4);
			ht5 = (TextView) findViewById(R.id.stkshipmentpre_ht5);

			lv0 = (ListView) findViewById(R.id.stkshipmentpre_lv0);

			btnConfirm = (Button) findViewById(R.id.stkshipmentpre_btnConfirm);
			btnDelete = (Button) findViewById(R.id.stkshipmentpre_btnTab0_Delete);
			btnExit = (Button) findViewById(R.id.stkshipmentpre_btnExit);

			adapterTab0 = new stkshipmentPreAdapterTab0(lsCompTable, this);
			lv0.setAdapter(adapterTab0);

			// ***********************************************Start
			prefercesService = new PrefercesService(this);
			params = prefercesService.getPreferences();
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("报工人员：" + MESCommon.UserName);
			actionBar.setTitle("整机预出货");


			//String date = sDateFormatShort.format(new java.util.Date());
			// // 读取报工人员
			// String sSql = "SELECT DISTINCT PROCESSUSERID, PROCESSUSER FROM
			// EQP_RESULT_USER WHERE EQPID ='"
			// + params.get("EQPID") + "' AND ISNULL(WORKDATE,'')='" + date
			// + "' AND ISNULL(LOGINTIME,'')!='' AND ISNULL (LOGOUTTIME,'')='' AND
			// ISNULL(STATUS,'')<>'已删除' AND (PROCESSUSERID='"
			// + MESCommon.UserId + "' OR PROCESSUSERID='" + MESCommon.UserId.toUpperCase()
			// + "') ";
			// String sResult = db.GetData(sSql, lsuser);
			// if (sResult != "") {
			// MESCommon.showMessage(STK_Shipment_Pre.this, sResult);
			// finish();
			// }
			//
			// if (lsuser.size() == 0) {
			// MESCommon.showMessage(STK_Shipment_Pre.this, "请先进行人员设备报工！");
			//
			// }

			
			// 控件事件
			lv0.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
						stkshipmentPreAdapterTab0.ViewHolder holder = (stkshipmentPreAdapterTab0.ViewHolder) arg1.getTag();
						// 改变CheckBox的状态
						holder.cb.toggle();
						// 将CheckBox的选中状况记录下来
						stkshipmentPreAdapterTab0.getIsSelected().put(position, holder.cb.isChecked());
						milv0RowNum = position;
						if (!lsCompTable.get(milv0RowNum).get("IS_INSERT").equals("N")) {
							if (holder.cb.isChecked()) {
								lsCompTable.get(position).put("CHECKFLAG", "Y");
							} else {
								lsCompTable.get(position).put("CHECKFLAG", "N");
							}
						} else {
							holder.cb.setChecked(false);
						}
					} catch (Exception e) {
						MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
					}
				}

			});
			
			editInput.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
						// 查询件号
						try {
							EditText txtInput = (EditText) findViewById(R.id.stkshipmentpre_tvInput);
							String sInput =txtInput.getText().toString().trim();
							if (sInput.length() == 0) {
								//txtInput.setText("");
								MESCommon.show(STK_Shipment_Pre.this, "请扫描条码!");
								return false;
							}					
							String sResult = "";
							msProductCompId=sInput;
							msProductId="";
							// 刷制造号码
							lsCompID.clear();
							//按制造号码先查MES的数据
							 sResult = db.GetProductSerialNumber(sInput,"","", "QF","制造号码","装配", lsCompID);
							 if(lsCompID.size()>0)
							 {			 
								String sOrderID=lsCompID.get(0).get("PRODUCTORDERID").toString();
								lsProcess.clear();
								sResult = db.GetProductProcess(sOrderID,sInput, lsProcess);
								if(lsProcess.size()>0) {
								msMark =lsProcess.get(0).get("COLER").toString();//颜色
								}
								lsProduct.clear();
								String  sSql=" SELECT * FROM MPRODUCT WHERE PRODUCTID ='"+lsProcess.get(0).get("PRODUCTID").toString()+"'  ;";
								String sError= db.GetData(sSql,  lsProduct);
								if (lsProduct.size()>0) {
									msProductId = lsProduct.get(0).get("PRODUCTID").toString();//件号
									msProductName =lsProduct.get(0).get("PRODUCTNAME").toString();//品名
								}
							 }else {
								//按制造号码查ERP库存，是否有库存
								lsCompID.clear();
								String sSql = " SELECT a.* ,b.itdsc FROM (SELECT itnbr,varnr FROM invbat WHERE varnr ='" + sInput + "' AND onhand1>0 )a inner join invmas b on a.itnbr=b.itnbr ";						
								sResult = db.ErpGetData(sSql, lsCompID);

								if (lsCompID.size() > 0) {

									msProductId = lsCompID.get(0).get("itnbr").toString();//件号
									msProductName = lsCompID.get(0).get("itdsc").toString();//品名
									msMark="";
								}
							 }
							 if("".equals(msProductId)) {
								 MESCommon.show(STK_Shipment_Pre.this, "没有找到对应的整机信息，请确认条码【"+sInput+"】正确性!");
								 return false;
							 }
							 
							//第一次刷入
							 if (editProudctID.getText().toString().length() == 0) {
								 queryByProudctId(msProductId);
							 }
							// 件号不同时，是否覆盖添加
							 else if (editProudctID.getText().toString().length() > 0
										&& !editProudctID.getText().toString().equals(msProductId)) {
									AlertDialog alert = new AlertDialog.Builder(STK_Shipment_Pre.this).setTitle("确认")
											.setMessage("此条码的件号与之前的件号不同，是否继续加入,确认将清楚现有数据!")
											.setPositiveButton("确定", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													
													Clear();										
												}
											}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog, int which) {
													// TODO Auto-generated method stub
													editInput.setText("");
													return;
												}
											}).show();
									return false;
								}
									
							// 检查这个制造号码是否已经在当前已经扫描的列表中
							int InLCcount = 0; //當前界面的列表清單的個數
							for (int i = 0; i < lsCompTable.size(); i++) {
								if (sInput.equals(lsCompTable.get(i).get("PRODUCTCOMPID").toString())) {
									MESCommon.show(STK_Shipment_Pre.this,
											"条码[" +sInput + "] 已绑定!");
									return false;
								}								
									InLCcount += 1;								
							}
	
							// 检查制造号码是不是当天已经绑定过
							String errorMsg =CheckInLC(sInput);
							if(!"".equals(errorMsg)) {
								
								AlertDialog alert = new AlertDialog.Builder(STK_Shipment_Pre.this).setTitle("确认")
										.setMessage(errorMsg+",是否继续加入!")
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												exeLstCompTable();
											}
										}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												editInput.setText("");
												return;
											}
										}).show();
							} else {
								exeLstCompTable();
							}

							editProudctID.setText(msProductId);
							editProductName.setText(msProductName);
							editMark.setText(msMark);
							editInput.setText("");
							setFocus(editProudctID);
						
						} catch (Exception e) {
							MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
							return false;
						}
					}
					return false;
				}
			});
			editProudctID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
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
							List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
							Boolean isSelect = false;

							for (int i = 0; i < lsCompTable.size(); i++) {
								if (lsCompTable.get(i).get("CHECKFLAG").toString().equals("Y")) {
									isSelect = true;

								}
								lsCompTableCopy.add(lsCompTable.get(i));
							}
							if (!isSelect) {
								MESCommon.show(STK_Shipment_Pre.this, "请选择要删除的制造号码");
								return;
							}
							for (int i = lsCompTableCopy.size() - 1; i >= 0; i--) {
								if (lsCompTableCopy.get(i).get("CHECKFLAG").toString().equals("Y")) {
									lsCompTable.remove(i);

								}
							}
							adapterTab0.notifyDataSetChanged();
							editInCount.setText(
									String.valueOf(Integer.parseInt(editInCount.getText().toString().trim()) - 1));// 扫描数+1
						}
					} catch (Exception e) {
						MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
					}
				}
			});

			// btnOK
			btnConfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						if (editProudctID.getText().toString().trim().equals("")) {
							MESCommon.show(STK_Shipment_Pre.this, "请先扫描条码在进行报工！");
							return;
						}

						Save();
						Toast.makeText(STK_Shipment_Pre.this, "保存完成!", Toast.LENGTH_SHORT).show();
						Clear();

					} catch (Exception e) {
						MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
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
						MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
					}
				}
			});
		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
		}
	}

	// 添加进列表
	public static class stkshipmentPreAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public stkshipmentPreAdapterTab0(List<HashMap<String, String>> items, Context context) {
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

		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			try {

				if (convertView == null) {

					// 获得ViewHolder对象
					holder = new ViewHolder();
					// 导入布局并赋值给convertview
					convertView = inflater.inflate(R.layout.activity_wip_track_in_tab0_listview, null);
					holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackinlv0_cb);
					holder.tvISSP = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvISSP);
					holder.tvSerialnumberId = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvSerialnumberId);
					holder.tvMaterialID = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialID);
					holder.tvMaterialMame = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialMame);
					// 为view设置标签
					convertView.setTag(holder);
				} else {
					// 取出holder
					holder = (ViewHolder) convertView.getTag();

				}
				// 设置list中TextView的显示
				holder.tvISSP.setText(String.valueOf(position+1)); // 序号
				holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTCOMPID").toString());
				holder.tvMaterialMame.setText(getItem(position).get("PRODUCTNAME").toString());
				holder.tvMaterialID.setText(getItem(position).get("PRODUCTID").toString());//件号
				// 将CheckBox的选中状况记录下来
				if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
					// 将CheckBox的选中状况记录下来
					getIsSelected().put(position, true);
				} else {
					// 将CheckBox的选中状况记录下来
					getIsSelected().put(position, false);
				}
				// 根据isSelected来设置checkbox的选中状况
				holder.cb.setChecked(getIsSelected().get(position));
			} catch (Exception e) {
				// TODO: handle exception
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
			stkshipmentPreAdapterTab0.isSelected = isSelected;
		}

		public static class ViewHolder {
			CheckBox cb;
			TextView tvISSP;
			TextView tvSerialnumberId;
			TextView tvMaterialID;
			TextView tvMaterialMame;
		}
	}

	private void exeLstCompTable() {
		try {

			String sError = "";
			sError = BindHashMap();
			if (sError != "") {
				MESCommon.showMessage(STK_Shipment_Pre.this, sError);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
		}
	}

	public String BindHashMap() {
		String sResult = "";
		try {	

			// 取得提示的零部件编号
			
			HashMap<String, String> hs = new HashMap<String, String>();
		
			hs.put("PRODUCTID", msProductId);//件号
			hs.put("PRODUCTNAME", msProductName);//品名
			
			hs.put("PRODUCTCOMPID", msProductCompId);//制造号码
			hs.put("PRODUCTMODEL", msProductModel);//机型
			hs.put("CUSTOMER", msCustomer);//客户
			hs.put("MARK", msMark);//备注
			hs.put("CHECKFLAG", "N");
			hs.put("IS_INSERT", "Y");
			lsCompTable.add(0, hs);
			adapterTab0.notifyDataSetChanged();
			if(editInCount.getText().toString().length()==0) {
				editInCount.setText("1");// 扫描数+1
			}else {
				editInCount.setText(String.valueOf(Integer.parseInt(editInCount.getText().toString().trim()) + 1));// 扫描数+1
			}
			Toast.makeText(STK_Shipment_Pre.this, "制造号码：【" + msProductCompId + "】,加入成功！", Toast.LENGTH_SHORT).show();
			editInput.setText("");

			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
			return sResult = e.toString();
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

	
	//检查制造号码是不是被LC单号绑定
	public String CheckInLC(String sProductCompId) {
		
		String sSql = " SELECT * FROM HZ_OQC_QCANALYSIS_PRE WHERE  PRODUCTCOMPID ='" + sProductCompId+ "'  and ISDELETE='N' ";
		lsLcComp.clear();
		String sError = db.GetData(sSql, lsLcComp);
		if (sError != "") {
			return sError;
		}
		if (lsLcComp.size() > 0) {
			return "条码[" + msProductCompId + "] 已在日期["+lsLcComp.get(0).get("SCANDATE").toString()+"]扫描过!";
		}
		return "";
	}
		
	//根據出貨單查詢出貨信息
		public void queryByProudctId(String sProductId) {
			
			String sSql = "SELECT count(DISTINCT PRODUCTCOMPID) as InCount  FROM HZ_OQC_QCANALYSIS_PRE  WHERE SCANDATE=convert(varchar,getdate(),111) and PRODUCTID ='" + sProductId + "'";// 查询
			List<HashMap<String, String>> lsInCount = new ArrayList<HashMap<String, String>>();
			String sResult = db.GetData(sSql, lsInCount);
			if (sResult != "") {
				MESCommon.showMessage(STK_Shipment_Pre.this, sResult);
				finish();
			}
			editInCount.setText(lsInCount.get(0).get("InCount").toString());// 當前單號+序號下的已扫描出货数
			
		}
		
		
	
	private void Save() {
		try {
			String sSQL = "";
			for (int i = 0; i < lsCompTable.size(); i++) {
				sSQL += "INSERT INTO HZ_OQC_QCANALYSIS_PRE (SYSID,SCANDATE, PRODUCTCOMPID, PRODUCTID, PRODUCTNAME, PRODUCTMODEL,CUSTOMER,DMARK, MODIFYUSERID, MODIFYUSER, MODIFYTIME)VALUES "
						+ "( " + MESCommon.SysId + ",convert(varchar,getdate(),111)," + "'"					
						+ lsCompTable.get(i).get("PRODUCTCOMPID").toString() + "','"
						+ lsCompTable.get(i).get("PRODUCTID").toString() + "','"
						+ lsCompTable.get(i).get("PRODUCTNAME").toString() + "','"
						+ "','"
						+ "','" 
						+ lsCompTable.get(i).get("MARK").toString() + "','" 
						+ MESCommon.UserId + "','"
						+ MESCommon.UserName
						+ "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) );";
			}
			String sMessage = db.ExecuteSQL(sSQL);
			if (!sMessage.equals("")) {
				MESCommon.showMessage(STK_Shipment_Pre.this, sMessage);
				return;
			}

		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
		}
	}

	public void Clear() {
		try {

			
			editProudctID.setText("");
			editProductName.setText("");
			editMark.setText("");

			msProductOrderId = "";
			msProductId = "";
			msProductCompId = "";

			miQty = 0;
			
			lsCompID.clear();
			lsCompTable.clear();

			adapterTab0.notifyDataSetChanged();
			setFocus(editInput);
			
			editInCount.setText("");

		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
		}

	}

	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment_Pre.this, e.toString());
		}
	}

	// 此方法只是关闭软键盘
	private void hintKbTwo() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

}
