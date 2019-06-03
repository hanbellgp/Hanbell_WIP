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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class STK_Shipment extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button btnConfirm, btnDelete, btnExit, btnTab1;
	Spinner spLcSeq;
	EditText editInput, editLCID, editProductModel, editCustomer, editSumCount, editInCount;

	// CheckBox cb ;
	TextView ht1, ht2, ht3, ht4, ht5;
	LinearLayout tab1;
	stkshipmentAdapterTab0 adapterTab0;
	PrefercesService prefercesService;
	Map<String, String> params;
	String msProductOrderId = "", msProductId, msProductName, msProductModel, msProductCompId,msCustomer;
	String msLcSeq = "";// 选中的序号
	int miQty;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;
	int milv1RowNum = 0;

	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();//列表清单
	private List<HashMap<String, String>> lsLcSeq = new ArrayList<HashMap<String, String>>();// LC序号
	private List<HashMap<String, String>> lsLcComp = new ArrayList<HashMap<String, String>>();// LC制造号码
	HashMap<String, String> mapSeqQty = new HashMap<String, String>();// 序号seq与数量的对应关系
	HashMap<String, String> mapSeqItnbr = new HashMap<String, String>();// 序号seq与件号的对应关系
	HashMap<String, String> mapSeqProductModel = new HashMap<String, String>();// 序号seq与机型的对应关系
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_stk_shipment);

		// 取得控件

		try {

			editInput = (EditText) findViewById(R.id.stkshipment_tvInput);
			editLCID = (EditText) findViewById(R.id.stkshipment_tvLCID); // LC單號
			spLcSeq = (Spinner) findViewById(R.id.stkshipment_spLcSeq);//LC單的序號
			editProductModel = (EditText) findViewById(R.id.stkshipment_tvProductModel);
			editCustomer = (EditText) findViewById(R.id.stkshipment_tvCustomer);//客戶名稱
			editSumCount = (EditText) findViewById(R.id.stkshipment_tvSumCount);//總數
			editInCount = (EditText) findViewById(R.id.stkshipment_tvInCount);//已掃描數

			ht1 = (TextView) findViewById(R.id.stkshipment_ht1);
			ht2 = (TextView) findViewById(R.id.stkshipment_ht2);
			ht3 = (TextView) findViewById(R.id.stkshipment_ht3);
			ht4 = (TextView) findViewById(R.id.stkshipment_ht4);
			ht5 = (TextView) findViewById(R.id.stkshipment_ht5);

			lv0 = (ListView) findViewById(R.id.stkshipment_lv0);

			btnConfirm = (Button) findViewById(R.id.stkshipment_btnConfirm);
			btnDelete = (Button) findViewById(R.id.stkshipment_btnTab0_Delete);
			btnExit = (Button) findViewById(R.id.stkshipment_btnExit);

			adapterTab0 = new stkshipmentAdapterTab0(lsCompTable, this);
			lv0.setAdapter(adapterTab0);

			// ***********************************************Start
			prefercesService = new PrefercesService(this);
			params = prefercesService.getPreferences();
			ActionBar actionBar = getActionBar();
			actionBar.setSubtitle("报工人员：" + MESCommon.UserName);
			actionBar.setTitle("整机出货");


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
			// MESCommon.showMessage(STK_Shipment.this, sResult);
			// finish();
			// }
			//
			// if (lsuser.size() == 0) {
			// MESCommon.showMessage(STK_Shipment.this, "请先进行人员设备报工！");
			//
			// }

			
			// 控件事件
			lv0.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
						stkshipmentAdapterTab0.ViewHolder holder = (stkshipmentAdapterTab0.ViewHolder) arg1.getTag();
						// 改变CheckBox的状态
						holder.cb.toggle();
						// 将CheckBox的选中状况记录下来
						stkshipmentAdapterTab0.getIsSelected().put(position, holder.cb.isChecked());
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
						MESCommon.showMessage(STK_Shipment.this, e.toString());
					}
				}

			});
			
			editInput.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
						// 查询交货单
						try {
							EditText txtInput = (EditText) findViewById(R.id.stkshipment_tvInput);
							String sInput =txtInput.getText().toString().trim();
							if (sInput.length() == 0) {
								txtInput.setText("");
								MESCommon.show(STK_Shipment.this, "请扫描条码!");
								return false;
							}
							// 覆盖添加
							if (editLCID.getText().toString().length() > 0
									&& txtInput.getText().toString().trim().contains("LC")) {
								AlertDialog alert = new AlertDialog.Builder(STK_Shipment.this).setTitle("确认")
										.setMessage("已有LC单号是否继续加入,确认将清除现有数据!")
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												Clear();	
												queryByCdr(((EditText) findViewById(R.id.stkshipment_tvInput)).getText().toString().trim());
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
							String sResult = "";

							// 查询ERP的LC单
							if (editLCID.getText().toString().equals("")) {
								lsLcSeq.clear();
								queryByCdr(sInput);
							} else {// 刷制造号码
								SpinnerData LcSeq = (SpinnerData) spLcSeq.getSelectedItem();
								msLcSeq = LcSeq.text;//當前LC序號
								//按制造号码查ERP库存，是否有库存
								lsCompID.clear();
								String sSql = " SELECT a.* ,b.itdsc FROM (SELECT itnbr,varnr FROM invbat WHERE varnr ='" + sInput + "' AND onhand1>0 )a inner join invmas b on a.itnbr=b.itnbr ";						
								sResult = db.ErpGetData(sSql, lsCompID);

								if (lsCompID.size() <= 0) {
									MESCommon.show(STK_Shipment.this,
											"制造号码【" + sInput + "】不存在或未入库!");
									return false;
								} else {
									

									msProductId = lsCompID.get(0).get("itnbr").toString();//件号
									msProductName = lsCompID.get(0).get("itdsc").toString();//品名
									
									// 检查这个制造号码对应的件号是不是与该序号的件号一致
									String errorMsg = CheckItnbrSame(msLcSeq,msProductId);
									if(!"".equals(errorMsg)) {
										MESCommon.showMessage(STK_Shipment.this, errorMsg);
										return false;
									}
									
									// 检查这个制造号码是否已经在当前已经扫描的列表中
									int InLCcount = 0; //當前界面的列表清單的個數
									for (int i = 0; i < lsCompTable.size(); i++) {
										if (sInput.equals(lsCompTable.get(i).get("PRODUCTCOMPID").toString())
												&& msLcSeq.equals(lsCompTable.get(i).get("LCSEQ").toString())) {
											MESCommon.show(STK_Shipment.this,
													"条码[" +sInput + "] 已绑定,LC序号["
															+ lsCompTable.get(i).get("LCSEQ").toString() + "]!");
											return false;

										}
										if (lsCompTable.get(i).get("LCSEQ").toString().equals(msLcSeq)) {
											InLCcount += 1;
										}

									}
	
									
									// 是不是到达最大个数
									errorMsg = CheckToMax(editLCID.getText().toString(),msLcSeq,InLCcount);
									if(!"".equals(errorMsg)) {
										MESCommon.showMessage(STK_Shipment.this, errorMsg);
										return false;
									}

									// 检查制造号码是不是被LC单号绑定
									errorMsg =CheckInLC(sInput);
									if(!"".equals(errorMsg)) {
										
										AlertDialog alert = new AlertDialog.Builder(STK_Shipment.this).setTitle("确认")
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

								}

								editInput.setText("");
								setFocus(editLCID);
							}
						} catch (Exception e) {
							MESCommon.showMessage(STK_Shipment.this, e.toString());
							return false;
						}
					}
					return false;
				}
			});
			editLCID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					setFocus(editInput);
				}
			});
			// 添加Spinner事件监听
			spLcSeq.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub

					String sLCID = editLCID.getText().toString();
					SpinnerData LcSeq = (SpinnerData) spLcSeq.getSelectedItem();
					msLcSeq = LcSeq.text.toString().trim(); // 当前的LC序号
					// 检查之前已经保存到数据库的数量
					String sSql = "SELECT count(DISTINCT PRODUCTCOMPID) as InCount  FROM HZ_OQC_QCANALYSIS  WHERE LCNO='"
							+ sLCID + "' and LCSEQ ='" + msLcSeq + "' and ISDELETE='N' ";// 查询
					List<HashMap<String, String>> lsInCount = new ArrayList<HashMap<String, String>>();
					String sResult = db.GetData(sSql, lsInCount);
					if (sResult != "") {
						MESCommon.showMessage(STK_Shipment.this, sResult);
						finish();
					}
					// 检查列表里该序号已有的数量
					// 检查这个制造号码是否已经在当前已经扫描的列表中
					int InLCcount = Integer.parseInt(lsInCount.get(0).get("InCount").toString());
					for (int i = 0; i < lsCompTable.size(); i++) {
						String tmpSeqTable = lsCompTable.get(i).get("LCSEQ").toString();// 列表里标识的序号
						if (tmpSeqTable.equals(msLcSeq)) {
							InLCcount += 1;
						}
					}
					msProductModel = String.valueOf(mapSeqProductModel.get(msLcSeq).toString());// 当前序号的机型
					editProductModel.setText(String.valueOf(mapSeqProductModel.get(msLcSeq).toString()));// 当前序号的机型
					editInCount.setText(String.valueOf(InLCcount));// LC已扫描出货数
					editSumCount.setText(String.valueOf(mapSeqQty.get(msLcSeq).toString()));// 当前序号的总台数
				}
				
				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
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
								MESCommon.show(STK_Shipment.this, "请选择要删除的制造号码");
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
						MESCommon.showMessage(STK_Shipment.this, e.toString());
					}
				}
			});

			// btnOK
			btnConfirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						if (editLCID.getText().toString().trim().equals("")) {
							MESCommon.show(STK_Shipment.this, "请先扫描条码在进行报工！");
							return;
						}

						Save();
						Toast.makeText(STK_Shipment.this, "确认完成!", Toast.LENGTH_SHORT).show();
						Clear();

					} catch (Exception e) {
						MESCommon.showMessage(STK_Shipment.this, e.toString());
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
						MESCommon.showMessage(STK_Shipment.this, e.toString());
					}
				}
			});
		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment.this, e.toString());
		}
	}

	// 添加进列表
	public static class stkshipmentAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();

		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public stkshipmentAdapterTab0(List<HashMap<String, String>> items, Context context) {
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
				holder.tvISSP.setText(getItem(position).get("LCSEQ").toString()); // 是否特采
				holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTCOMPID").toString());
				holder.tvMaterialID.setText(getItem(position).get("PRODUCTID").toString());
				holder.tvMaterialMame.setText(getItem(position).get("PRODUCTNAME").toString());

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
			stkshipmentAdapterTab0.isSelected = isSelected;
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
				MESCommon.showMessage(STK_Shipment.this, sError);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment.this, e.toString());
		}
	}

	public String BindHashMap() {
		String sResult = "";
		try {
	
			String sPRODUCTCOMPID = lsCompID.get(0).get("varnr").toString();

			// 取得提示的零部件编号
			
			HashMap<String, String> hs = new HashMap<String, String>();
			//hs.put("LCNO", sSEQ);//LC单号
			hs.put("LCSEQ", msLcSeq);//lc序号
			hs.put("PRODUCTID", msProductId);//件号
			hs.put("PRODUCTNAME", msProductName);//品名
			
			hs.put("PRODUCTCOMPID", sPRODUCTCOMPID);//制造号码
			hs.put("PRODUCTMODEL", msProductModel);//机型
			hs.put("CUSTOMER", msCustomer);//客户
			hs.put("CHECKFLAG", "N");
			hs.put("IS_INSERT", "Y");
			lsCompTable.add(0, hs);
			adapterTab0.notifyDataSetChanged();
			editInCount.setText(String.valueOf(Integer.parseInt(editInCount.getText().toString().trim()) + 1));// 扫描数+1
			Toast.makeText(STK_Shipment.this, "制造号码：【" + sPRODUCTCOMPID + "】,加入成功！", Toast.LENGTH_SHORT).show();
			editInput.setText("");

			return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment.this, e.toString());
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

	//檢查是当前制造号码的件号与选择的出货是否对应
	public String CheckItnbrSame(String Lcseq,String ProductId) {

		String LcItnbr = mapSeqItnbr.get(Lcseq).toString();//當前序号对应的件号
		if (!ProductId.equals(LcItnbr)) {
			return "序号[" + Lcseq + "]的件号为【" + LcItnbr + "】与制造号码的件号【"+ProductId+"】不一致";
		}
		return "";
	}
	
	//檢查是否達到當前戳貨單的最大數
	public String CheckToMax(String Lcno,String Lcseq,int InLCcount) {
		String sSql = " SELECT COUNT(*) AS INCOUNT FROM HZ_OQC_QCANALYSIS WHERE LCNO='"+ Lcno+ "' AND LCSEQ ='" + Lcseq + "' and ISDELETE='N' ";
		lsLcComp.clear();
		String sError = db.GetData(sSql, lsLcComp);
		if (sError != "") {
			return sError;
		}
		int InCountDB = Integer.parseInt(lsLcComp.get(0).get("INCOUNT").toString());//當前序號的已掃描數
		int maxCount = Integer.parseInt(mapSeqQty.get(msLcSeq).toString());//當前序号可以的最大數
		if (maxCount < InLCcount 
				|| maxCount <= InCountDB
				|| maxCount <= InCountDB + InLCcount) {
			return "LC序号[" + msLcSeq + "]已达最大出货数【" + maxCount + "】!";
		}
		return "";
	}
	
	//检查制造号码是不是被LC单号绑定
	public String CheckInLC(String msProductCompId) {
		
		String sSql = " SELECT * FROM HZ_OQC_QCANALYSIS WHERE  PRODUCTCOMPID ='" + msProductCompId+ "'  and ISDELETE='N' ";
		lsLcComp.clear();
		String sError = db.GetData(sSql, lsLcComp);
		if (sError != "") {
			return sError;
		}
		if (lsLcComp.size() > 0) {
			return "条码[" + msProductCompId + "] 已绑定当前LC单号["+lsLcComp.get(0).get("LCNO").toString()+"]!";
		}
		return "";
	}
		
	//根據出貨單查詢出貨信息
	public void queryByCdr(String sLCID) {
		
		String sSql = "SELECT trseq,itnbr,itdsc,shpqy1,cusna,itnbrcus FROM  cdrdta "
				+ "LEFT JOIN cdrhad ON cdrdta.shpno=cdrhad.shpno "
				+ "    LEFT JOIN   cdrcus ON cdrhad.cusno=cdrcus.cusno "
				+ "WHERE cdrdta.shpno='" + sLCID + "' ORDER BY  cdrdta.trseq";// 查询
		String sResult = db.ErpGetData(sSql, lsLcSeq);
		if (sResult != "") {
			MESCommon.showMessage(STK_Shipment.this, sResult);
			finish();
		}
		if (lsLcSeq.size() > 0) {
			editLCID.setText(sLCID);
			msCustomer = lsLcSeq.get(0).get("cusna");
			msProductModel = lsLcSeq.get(0).get("itnbrcus");
			int sumCount = Integer.parseInt(lsLcSeq.get(0).get("shpqy1").toString());
			editCustomer.setText(msCustomer);// 客户
			editProductModel.setText(msProductModel);// 产品型号
			editSumCount.setText(String.valueOf(sumCount));// 当前序号的总台数
		} else {
			editLCID.setText("");
			MESCommon.show(STK_Shipment.this, "请确认LC单是否正确!");
			return ;
		}
		ArrayList<SpinnerData> lst = new ArrayList<SpinnerData>();
		// int sumCount = 0;
		for (int i = 0; i < lsLcSeq.size(); i++) {
			SpinnerData c = new SpinnerData(lsLcSeq.get(i).get("trseq").toString(),
					lsLcSeq.get(i).get("trseq").toString());
			lst.add(c);
			// sumCount += Integer.parseInt(lsLcSeq.get(i).get("shpqy1").toString());
			mapSeqQty.put(lsLcSeq.get(i).get("trseq").toString(),
					lsLcSeq.get(i).get("shpqy1").toString());
			mapSeqItnbr.put(lsLcSeq.get(i).get("trseq").toString(),
					lsLcSeq.get(i).get("itnbr").toString());
			mapSeqProductModel.put(lsLcSeq.get(i).get("trseq").toString(),
					lsLcSeq.get(i).get("itnbrcus").toString());
		}
		//
		
		ArrayAdapter<SpinnerData> Adapter1 = new ArrayAdapter<SpinnerData>(STK_Shipment.this,
				android.R.layout.simple_spinner_item, lst);
		Adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spLcSeq.setAdapter(Adapter1);
		// 设置显示当前选择的项
		MESCommon.setSpinnerItemSelectedByValue(spLcSeq, "trseq");

		SpinnerData LcSeq = (SpinnerData) spLcSeq.getSelectedItem();
		msLcSeq = LcSeq.text; // 当前的LC序号
		sSql = "SELECT count(DISTINCT PRODUCTCOMPID) as InCount  FROM HZ_OQC_QCANALYSIS  WHERE LCNO='"
				+ sLCID + "' and LCSEQ ='" + msLcSeq + "'";// 查询
		List<HashMap<String, String>> lsInCount = new ArrayList<HashMap<String, String>>();
		sResult = db.GetData(sSql, lsInCount);
		if (sResult != "") {
			MESCommon.showMessage(STK_Shipment.this, sResult);
			finish();
		}
		editInCount.setText(lsInCount.get(0).get("InCount").toString());// 當前單號+序號下的已扫描出货数
		editSumCount.setText(String.valueOf(mapSeqQty.get(msLcSeq)));//當前單號+序號下的总出货数
		
		editInput.setText("");
	}
	
	
	private void Save() {
		try {
			String sSQL = "";
			for (int i = 0; i < lsCompTable.size(); i++) {
				sSQL += "INSERT INTO HZ_OQC_QCANALYSIS (SYSID,LCNO, LCSEQ, PRODUCTCOMPID, PRODUCTID, PRODUCTNAME, PRODUCTMODEL,CUSTOMER, MODIFYUSERID, MODIFYUSER, MODIFYTIME)VALUES "
						+ "( " + MESCommon.SysId + "," + "'" + editLCID.getText().toString() + "'," + "'"
						+ lsCompTable.get(i).get("LCSEQ").toString() + "'," + "'"
						+ lsCompTable.get(i).get("PRODUCTCOMPID").toString() + "','"
						+ lsCompTable.get(i).get("PRODUCTID").toString() + "','"
						+ lsCompTable.get(i).get("PRODUCTNAME").toString() + "','"
						+ lsCompTable.get(i).get("PRODUCTMODEL").toString() + "','"
						+ lsCompTable.get(i).get("CUSTOMER").toString() + "','" + MESCommon.UserId + "'," + "'"
						+ MESCommon.UserName
						+ "',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) );";
			}
			String sMessage = db.ExecuteSQL(sSQL);
			if (!sMessage.equals("")) {
				MESCommon.showMessage(STK_Shipment.this, sMessage);
				return;
			}

		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(STK_Shipment.this, e.toString());
		}
	}

	public void Clear() {
		try {
			List<SpinnerData> lst = new ArrayList<SpinnerData>();
			ArrayAdapter<SpinnerData> Adapter = new ArrayAdapter<SpinnerData>(this,
					android.R.layout.simple_spinner_item, lst);
			Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spLcSeq.setAdapter(Adapter);
			
			editLCID.setText("");
			editProductModel.setText("");
			editCustomer.setText("");

			msProductOrderId = "";
			msProductId = "";
			msProductCompId = "";

			miQty = 0;
			lsCompID.clear();


			lsCompTable.clear();

			adapterTab0.notifyDataSetChanged();
			setFocus(editInput);
			
			editInCount.setText("");
			editSumCount.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment.this, e.toString());
		}

	}

	public void setFocus(EditText editText) {
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			editText.setSelectAllOnFocus(true);

		} catch (Exception e) {
			MESCommon.showMessage(STK_Shipment.this, e.toString());
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
