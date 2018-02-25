package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hanbell_wip.Class.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.app.Dialog;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class STK_OutStock extends Activity {

	private MESDB db = new MESDB();
	private MESSTK moMesStk = new MESSTK();
	
	OutStockAdapter adapter;
	ListView lv;
	Button btnConfirm,btnExit,btnReset,btnDelete;
	EditText editScanId, editTotalQty, editCompid, editItnbr, editItdsc;
	TextView tvUserId;
	TextView h0, h1, h2, h3, h4;
	
	//该物料的HashMap记录
	//List<HashMap<String, String>> mlsMain = new ArrayList<HashMap<String, String>>();	//物料
	List<HashMap<String, String>> mlsDetail = new ArrayList<HashMap<String, String>>();	//物料明细
	//List<HashMap<String, String>> mlsScanId = new ArrayList<HashMap<String, String>>();
	
	List<HashMap<String, String>> mls = new ArrayList<HashMap<String, String>>();
	String msScanId = "";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stk_outstock);
		try{
		//取得控件
		tvUserId = (TextView) findViewById(R.id.stkoutstock_tvUserId);
		editTotalQty = (EditText) findViewById(R.id.stkoutstock_editTotalQty);
		editScanId = (EditText) findViewById(R.id.stkoutstock_editScanId);
		editCompid = (EditText) findViewById(R.id.stkoutstock_editCompid);
		editItnbr = (EditText) findViewById(R.id.stkoutstock_editItnbr);
		editItdsc = (EditText) findViewById(R.id.stkoutstock_editItdsc);
		h0 = (TextView) findViewById(R.id.stkoutstock_h0);
		h1 = (TextView) findViewById(R.id.stkoutstock_h1);
		h2 = (TextView) findViewById(R.id.stkoutstock_h2);
		h3 = (TextView) findViewById(R.id.stkoutstock_h3);
		h4 = (TextView) findViewById(R.id.stkoutstock_h4);
		h0.setTextColor(Color.WHITE); h0.setBackgroundColor(Color.DKGRAY);
		h1.setTextColor(Color.WHITE); h1.setBackgroundColor(Color.DKGRAY);
		h2.setTextColor(Color.WHITE); h2.setBackgroundColor(Color.DKGRAY);
		h3.setTextColor(Color.WHITE); h3.setBackgroundColor(Color.DKGRAY);
		h4.setTextColor(Color.WHITE); h3.setBackgroundColor(Color.DKGRAY);
		lv = (ListView) findViewById(R.id.stkoutstock_lv);
		btnConfirm = (Button) findViewById(R.id.stkoutstock_btnConfirm);
		btnReset = (Button) findViewById(R.id.stkoutstock_btnReset);
		btnDelete = (Button) findViewById(R.id.stkoutstock_btnDelete);
		btnExit = (Button) findViewById(R.id.stkoutstock_btnExit);
		
		//***********************************************Start
		adapter = new OutStockAdapter(mlsDetail, this);
		lv.setAdapter(adapter);
		editScanId.setSelectAllOnFocus(true);
		//editScanId.setText("P1");
		tvUserId.setText(MESCommon.UserId + " " + MESCommon.UserName);
		editTotalQty.setText("0");
		//***********************************************End
		
		//控件事件
		lv.setOnItemClickListener(new OnItemClickListener() {
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            	// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
            	OutStockAdapter.ViewHolder holder = (OutStockAdapter.ViewHolder) arg1.getTag();
                // 改变CheckBox的状态  
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来  
                OutStockAdapter.getIsSelected().put(position, holder.cb.isChecked());
                
                if (holder.cb.isChecked() == true) {
                	mlsDetail.get(position).put("CheckFlag", "Y");
                } else {
                	mlsDetail.get(position).put("CheckFlag", "N");
                }
            }  
        });
		
		//editScanId
		editScanId.setOnKeyListener(new OnKeyListener() {
			@Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
				try {
					if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
						//查询交货单
						String sScanId = editScanId.getText().toString().trim();
						msScanId = sScanId;
						if (sScanId.length() == 0) 
						{
							MESCommon.showMessage(STK_OutStock.this,"请扫描出库条码!");
							return false;
						}
						String sResult = "";
						sResult = GetScanInfo(sScanId, mlsDetail);
						if (!sResult.equals(""))
						{
							MESCommon.showMessage(STK_OutStock.this, sResult);
							return false;
						}
						CheckTotalQty();
						adapter.notifyDataSetChanged();
						editScanId.setText("");
			        } 
				} catch (Exception e) {
					MESCommon.showMessage(STK_OutStock.this,e.toString());
				}
				return false;
			}
		});
		//editTotalQty
		editCompid.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
		    public void onFocusChange(View v, boolean hasFocus) {
				try {
					setFocus(editScanId);
				} catch (Exception e) {
					MESCommon.showMessage(STK_OutStock.this,e.toString());
				}
		    }
		});
		
		// btnConfirm
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					DialogHandler appdialog = new DialogHandler();
			        appdialog.Confirm(STK_OutStock.this, "确认视窗", "确定储存？",
			                "取消", "确认", Confirm(), Cancel());
				} catch (Exception e) {
					MESCommon.showMessage(STK_OutStock.this,e.toString());
				}
			}
		});
		// btnDelete
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					DialogHandler appdialog = new DialogHandler();
			        appdialog.Confirm(STK_OutStock.this, "确认视窗", "确定要删除？",
			                "取消", "确认", ConfirmDelete(), Cancel());
				} catch (Exception e) {
					MESCommon.showMessage(STK_OutStock.this,e.toString());
				}
			}
		});
		// btnReset
		btnReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					DialogHandler appdialog = new DialogHandler();
			        appdialog.Confirm(STK_OutStock.this, "确认视窗", "确定要重置？",
			                "取消", "确认", ConfirmReset(), Cancel());
					
				} catch (Exception e) {
					MESCommon.showMessage(STK_OutStock.this,e.toString());
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
					MESCommon.showMessage(STK_OutStock.this,e.toString());
				}
			}
		});
		} catch (Exception e) {
			MESCommon.showMessage(STK_OutStock.this,e.toString());
		}
	}
	
	private static class OutStockAdapter extends BaseAdapter {
		//物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// 用来控制CheckBox的选中状况 
	    private static HashMap<Integer, Boolean> isSelected; 
	    // 上下文 
	    private Context context; 
	    // 用来导入布局 
	    private LayoutInflater inflater = null; 
	    
		CheckBox cb;
		int iPosition = -1;
		public OutStockAdapter(List<HashMap<String, String>> items, Context context) {
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
	        if (convertView == null) { 
	            // 获得ViewHolder对象 
	            holder = new ViewHolder(); 
	            // 导入布局并赋值给convertview 
	            convertView = inflater.inflate(R.layout.activity_stk_outstock_listview, null);
	            holder.cb = (CheckBox) convertView.findViewById(R.id.stkoutstocklv_cb);
	            holder.tvCompid = (TextView) convertView.findViewById(R.id.stkoutstocklv_tvCompid);
	            holder.tvLotQty = (TextView) convertView.findViewById(R.id.stkoutstocklv_tvLotQty);
	            holder.tvItnbr = (TextView) convertView.findViewById(R.id.stkoutstocklv_tvItnbr);
	            holder.tvItdsc = (TextView) convertView.findViewById(R.id.stkoutstocklv_tvItdsc);
	            // 为view设置标签 
	            convertView.setTag(holder); 
	        } else {
	            // 取出holder 
	            holder = (ViewHolder) convertView.getTag(); 
	        } 
	        // 设置list中TextView的显示 
	        holder.tvCompid.setText(getItem(position).get("Compid").toString());
	        holder.tvLotQty.setText(getItem(position).get("LotQty").toString());
	        holder.tvItnbr.setText(getItem(position).get("Itnbr").toString());
            holder.tvItdsc.setText(getItem(position).get("Itdsc").toString());
            if (getItem(position).get("CheckFlag").toString().equals("Y"))
            {
            	// 将CheckBox的选中状况记录下来  
                getIsSelected().put(position, true);
            }
            else
            {
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
	    	OutStockAdapter.isSelected = isSelected; 
	    } 
	    public static class ViewHolder { 
	    	CheckBox cb;
	    	TextView tvCompid;
	    	TextView tvLotQty;
	    	TextView tvItnbr;
	        TextView tvItdsc;
	    } 
	}
	
	private void setFocus(EditText editText)
	{
		try {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			editText.requestFocus();
			
		} catch (Exception e) {
			MESCommon.showMessage(STK_OutStock.this,e.toString());
		}
		
	}

	private String GetScanInfo(String sScanId, List<HashMap<String, String>> lsDetail)
	{
		String sResult = "";
		String sSql = "";
		
		try {
			//重复扫描检查
			boolean bFind = false;
			for (int i =0; i< lsDetail.size(); i++)
			{
				if (sScanId.equals(lsDetail.get(i).get("ScanId").toString()))
				{
					bFind = true;
					break;
				}
			}
			if (bFind) return "条码「" + sScanId + "」重复扫描!";
			
			//先查STKCOMP
			mls.clear();
			sSql = "SELECT DISTINCT A.STOCKID, A.SUPPLYID, A.ITNBR, A.LOTID, A.LOTIDSEQ, A.COMPID, A.COMPIDSEQ, " +
					"A.LOTQTY, A.STOCKTIME, A.SHIPTYPE, B.ITDSC " +
					"FROM STKINSTOCK A LEFT JOIN MSTKINVMAS B ON A.ITNBR=B.ITNBR WHERE A.COMPIDSEQ='" + sScanId + "'";
			sResult = db.GetData(sSql, mls);
			if (!sResult.equals("")) return sResult;
			if (mls.size() == 0)
			{
				return "条码「" + sScanId + "」查无入库资料!";
			}
			String sStockid = mls.get(0).get("STOCKID").toString();
			String sSupplyId = mls.get(0).get("SUPPLYID").toString();
			String sItnbr = mls.get(0).get("ITNBR").toString();
			String sItdsc = mls.get(0).get("ITDSC").toString();
			String sLotid = mls.get(0).get("LOTID").toString();
			String sCompid = mls.get(0).get("COMPID").toString();
			String sLotidseq = mls.get(0).get("LOTIDSEQ").toString();
			String sCompidseq = mls.get(0).get("COMPIDSEQ").toString();
			String sLotQty = mls.get(0).get("LOTQTY").toString();
			String sStockTime = mls.get(0).get("STOCKTIME").toString();
			String sShipType = mls.get(0).get("SHIPTYPE").toString();
			
			HashMap<String, String> hsDetail = new HashMap<String, String>();
			hsDetail.put("Stockid", sStockid);
			hsDetail.put("SupplyId", sSupplyId);
			hsDetail.put("Itnbr", sItnbr);
			hsDetail.put("Itdsc", sItdsc);
			hsDetail.put("Lotid", sLotid);
			hsDetail.put("Lotidseq", sLotidseq);
			hsDetail.put("Compid", sCompid);
			hsDetail.put("Compidseq", sCompidseq);
			hsDetail.put("LotQty", sLotQty);
			hsDetail.put("StockTime", sStockTime);
			hsDetail.put("ShipType", sShipType);
			hsDetail.put("CheckFlag", "N");
			hsDetail.put("ScanId", sScanId);
			lsDetail.add(hsDetail);
			
			editCompid.setText(sCompid);
			editItnbr.setText(sItnbr);
			editItdsc.setText(sItdsc);
			
			return sResult;
		} catch (Exception e) {
			MESCommon.showMessage(STK_OutStock.this,e.toString());
		}
		return sResult;
	}
	
	private void CheckTotalQty()
	{
		try {
//			int iTotalQty = 0;
//			for (int i=0; i < mlsDetail.size(); i++)
//			{
//				int iLotQty = Integer.parseInt(mlsDetail.get(i).get("LotQty").toString());
//				iTotalQty = iTotalQty + iLotQty;
//			}
			editTotalQty.setText(String.valueOf(mlsDetail.size()));
		} catch (Exception e) {
			MESCommon.showMessage(STK_OutStock.this,e.toString());
		}
		return;
	}
	
	private void ClearData()
	{
		try {
			mlsDetail.clear();
			setFocus(editScanId);
			adapter.notifyDataSetChanged();
			editTotalQty.setText("0");
		} catch (Exception e) {
			MESCommon.showMessage(STK_OutStock.this,e.toString());
		}
	}
	
	public Runnable Confirm(){
        return new Runnable() {
            public void run() {
            	String sResult = "";
            	String sSql = "";
            	try {
            		//检查
            		if (mlsDetail.size() == 0)
            		{
            			MESCommon.showMessage(STK_OutStock.this, "请扫描条码!!");
            			return;
            		}
            		for (int i=0; i < mlsDetail.size(); i++)
        			{
            			String sStockid = mlsDetail.get(i).get("Stockid").toString();
            			String sSupplyId = mlsDetail.get(i).get("SupplyId").toString();
        				String sItnbr = mlsDetail.get(i).get("Itnbr").toString();
        				String sLotid = mlsDetail.get(i).get("Lotid").toString();
        				String sCompid = mlsDetail.get(i).get("Compid").toString();
        				String sLotidseq = mlsDetail.get(i).get("Lotidseq").toString();
        				String sCompidseq = mlsDetail.get(i).get("Compidseq").toString();
        				String sLotQty = mlsDetail.get(i).get("LotQty").toString();
        				String sStockTime = mlsDetail.get(i).get("StockTime").toString();
        				String sShipType = mlsDetail.get(i).get("ShipType").toString();
        				
        				sSql = "INSERT INTO STKINOUTSTOCK (SYSID, STOCKID, INOUTTYPE, STKFUNC, STKFUNCID, " +
        						"SUPPLYID, ITNBR, LOTID, LOTIDSEQ, LOTQTY, COMPID, COMPIDSEQ, SHIPTYPE, " +
        						"STOCKTIME, MODIFYUSERID, MODIFYUSER, MODIFYTIME) VALUES (" +
                    			"" + MESCommon.SysId + "," +
                    			"'" + sStockid + "'," +
                    			"'" + "OUT" + "'," +
                    			"'" + "" + "'," +
                    			"'" + "" + "'," +
                    			"'" + sSupplyId + "'," +
                    			"'" + sItnbr + "'," +
                    			"'" + sLotid + "'," +
                    			"'" + sLotidseq + "'," +
                    			"" + sLotQty + "," +
                    			"'" + sCompid + "'," +
                    			"'" + sCompidseq + "'," +
                    			"'" + sShipType + "'," +
                    			"'" + sStockTime + "'," +
                    			"'" + MESCommon.UserId + "'," +
                    			"'" + MESCommon.UserName + "'," +
                    			"" + MESCommon.ModifyTime + ")";
        				sResult = db.ExecuteSQL(sSql);
        				if (sResult != "")
        				{
        					MESCommon.showMessage(STK_OutStock.this,sResult);
        					return;
        				}
        				sSql = "DELETE FROM STKINSTOCK WHERE STOCKID='" + sStockid + "' AND ITNBR='" + sItnbr + "' AND COMPIDSEQ='" + sCompidseq + "'";
        				sResult = db.ExecuteSQL(sSql);
        				if (sResult != "")
        				{
        					MESCommon.showMessage(STK_OutStock.this,sResult);
        					return;
        				}
        			}
            		ClearData();
            		
            		MESCommon.showMessage(STK_OutStock.this, "出库成功!");
            			
        		} catch (Exception e) {
        			MESCommon.showMessage(STK_OutStock.this,e.toString());
        		}
            }
        };
    }
	
	public Runnable ConfirmDelete(){
        return new Runnable() {
            public void run() {
            	try {
            		for (int i=mlsDetail.size()-1; i>=0; i--)
        			{
        				String sCheckFlag = mlsDetail.get(i).get("CheckFlag").toString();
        				if (sCheckFlag.equals("Y")) mlsDetail.remove(i);
        			}
            		CheckTotalQty();
					adapter.notifyDataSetChanged();
        		} catch (Exception e) {
        			MESCommon.showMessage(STK_OutStock.this,e.toString());
        		}
            }
        };
    }

	public Runnable ConfirmReset(){
        return new Runnable() {
            public void run() {
            	try {
            		ClearData();
        		} catch (Exception e) {
        			MESCommon.showMessage(STK_OutStock.this,e.toString());
        		}
            }
        };
    }
	
    public Runnable Cancel(){
        return new Runnable() {
            public void run() {
            	
            }
          };
    }
    
}
