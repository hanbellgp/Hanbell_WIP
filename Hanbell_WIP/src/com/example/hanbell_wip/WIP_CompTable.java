package com.example.hanbell_wip;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.R;
import com.example.hanbell_wip.Class.*;
import com.example.hanbell_wip.WIP_TrackIn.wiptrackinAdapterTab0;
import com.example.hanbell_wip.WIP_TrackIn.wiptrackinAdapterTab0.ViewHolder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.LinearLayout;
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

public class WIP_CompTable extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button btnConfirm, btnExit;
	TextView   h6,h7,h8,h9;
	//wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	Map<String,String> params;
	wiptrackinAdapterTab0 adapterTab0;
	// 该物料的HashMap记录
	static int milv0RowNum = 0;
	List<HashMap<String, String>> lsCompID= new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
    ArrayList<CompInformation> arrayList = new ArrayList<CompInformation>();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip__comptable);

		// 取得控件
try{
	
		btnConfirm = (Button) findViewById(R.id.wipcomptable_btnConfirm);	
		btnExit = (Button) findViewById(R.id.wipcomptable_btnExit);	
	
		h6 = (TextView) findViewById(R.id.wiptrackin_h6);
		h7= (TextView) findViewById(R.id.wiptrackin_h7);
		h8= (TextView) findViewById(R.id.wiptrackin_h8);
		h9= (TextView) findViewById(R.id.wiptrackin_h9);
		
		h6.setTextColor(Color.WHITE);
		h6.setBackgroundColor(Color.DKGRAY);
		h7.setTextColor(Color.WHITE);
		h7.setBackgroundColor(Color.DKGRAY);
		h8.setTextColor(Color.WHITE);
		h8.setBackgroundColor(Color.DKGRAY);
		h9.setTextColor(Color.WHITE);
		h9.setBackgroundColor(Color.DKGRAY);
		lv0 = (ListView) findViewById(R.id.wiptrackin_lv0);
		adapterTab0 = new wiptrackinAdapterTab0(lsCompTable, this);
		lv0.setAdapter(adapterTab0);
		// ***********************************************Start
		prefercesService  =new PrefercesService(this);  
	    params=prefercesService.getPreferences();  
		ActionBar actionBar=getActionBar();
	    actionBar.setSubtitle("报工人员："+MESCommon.UserName); 
	    actionBar.setTitle("零部件列表");
	    String MaterialId="";
	    String PRODUCTSERIALNUMBER="";
	    Intent intent = this.getIntent();
	     MaterialId = intent.getStringExtra("MaterialId");
	     PRODUCTSERIALNUMBER = intent.getStringExtra("PRODUCTSERIALNUMBER"); 
	 
	 // 控件事件
	 		lv0.setOnItemClickListener(new OnItemClickListener() {
	 			@Override
	 			public void onItemClick(AdapterView<?> arg0, View arg1,
	 					int position, long arg3) {
	 				try{
	 				// 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
	 				wiptrackinAdapterTab0.ViewHolder holder = (wiptrackinAdapterTab0.ViewHolder) arg1.getTag();
	 				// 改变CheckBox的状态
	 				holder.cb.toggle();
	 				// 将CheckBox的选中状况记录下来
	 				wiptrackinAdapterTab0.getIsSelected().put(position,
	 						holder.cb.isChecked());
	 				milv0RowNum=position;
					if(holder.cb.isChecked())
					{
					  lsCompTable.get(position).put("CHECKFLAG","Y"); 
					}else
					{
						 lsCompTable.get(position).put("CHECKFLAG","N"); 
					}
					for(int i=0;i<lsCompTable.size();i++)
					{ if(i!=position)
						{
					      lsCompTable.get(i).put("CHECKFLAG","N"); 
						}
					}
					adapterTab0.notifyDataSetChanged();
	 			} catch (Exception e) {
	 				MESCommon.showMessage(WIP_CompTable.this, e.toString());
	 			}
	 			}
	 			
	 		});
	    
				try {	
			    	String	sResult = db.GetProductSerialNumber_PDA(PRODUCTSERIALNUMBER,MaterialId,"" , "QF","零部件", "装配",lsCompID);
					if(lsCompID.size()>1)
					{   
						for(int i=0;i<lsCompID.size();i++)
					 {
						HashMap<String, String> hs = new HashMap<String, String>();		
			
						hs.put("MaterialId",lsCompID.get(i).get("MATERIALID").toString());
						hs.put("MaterialMame",  lsCompID.get(i).get("MATERIALNAME").toString());
						hs.put("SEQ", lsCompID.get(i).get("PRODUCTSERIALNUMBER").toString());
						  if (! lsCompID.get(i).get("FINEPROCESSID").toString().equals(""))
							{
								if(!lsCompID.get(i).get("MATERIALID").toString().equals(""))
								{
							      String sFinId=getCompid(lsCompID.get(i).get("MATERIALID").toString());
							      if(!sFinId.equals(""))
							      {
							    	  if(sFinId.equals("半成品方型件"))
							    	  {
							    		  hs.put("PRODUCTSERIALNUMBER",lsCompID.get(i).get("RAWPROCESSID").toString());	
							    	  }else {
							    		  hs.put("PRODUCTSERIALNUMBER",lsCompID.get(i).get("FINEPROCESSID").toString());			
									}				    	 
							      }
								}else {
									hs.put("PRODUCTSERIALNUMBER",lsCompID.get(i).get("FINEPROCESSID").toString());			
								}
								
							}
							  else  if (! lsCompID.get(i).get("RAWPROCESSID").toString().equals("")&&lsCompID.get(i).get("FINEPROCESSID").toString().equals("")) {
									hs.put("PRODUCTSERIALNUMBER", lsCompID.get(i).get("RAWPROCESSID").toString());
									
								}
							else if (!lsCompID.get(i).get("LOTID").toString().equals("")&&lsCompID.get(i).get("RAWPROCESSID").toString().equals("")&&lsCompID.get(i).get("FINEPROCESSID").toString().equals(""))
							{
								hs.put("PRODUCTSERIALNUMBER",lsCompID.get(i).get("LOTID").toString());

							}
						hs.put("MaterialType", lsCompID.get(i).get("MATERIALMAINTYPE").toString());
						hs.put("CHECKFLAG", "N");
						hs.put("ISCOMPEXIST", "Y");
						hs.put("RAWPROCESSID", lsCompID.get(i).get("RAWPROCESSID").toString());
						hs.put("FINEPROCESSID", lsCompID.get(i).get("FINEPROCESSID").toString());
						hs.put("LOTID", lsCompID.get(i).get("LOTID").toString());
						hs.put("SUPPLYID", lsCompID.get(i).get("SUPPLYID").toString());
						hs.put("FURNACENO",lsCompID.get(i).get("FURNACENO").toString());
						hs.put("IS_INSERT", "Y");
						hs.put("TRACETYPE", lsCompID.get(i).get("TRACETYPE").toString());
						lsCompTable.add(hs);	
					 }
					}
					adapterTab0.notifyDataSetChanged();		

				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompTable.this, e.toString());
				}

	  
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
					if (lsCompTable.size() == 0) {
						MESCommon.show(WIP_CompTable.this, "请先选择条码编号!");
						return ;
					}	
					CompInformation myClass = new CompInformation(); 					
					
					myClass.MaterialId=lsCompTable.get(milv0RowNum).get("MaterialId").toString(); 
					myClass.MaterialMame= lsCompTable.get(milv0RowNum).get("MaterialMame").toString();
					myClass.SEQ=  lsCompTable.get(milv0RowNum).get("SEQ").toString();
					myClass.PRODUCTSERIALNUMBER=  lsCompTable.get(milv0RowNum).get("PRODUCTSERIALNUMBER").toString();
					myClass.MaterialType=  lsCompTable.get(milv0RowNum).get("MaterialType").toString();
					myClass.CHECKFLAG= "N";
					myClass.ISCOMPEXIST=  lsCompTable.get(milv0RowNum).get("ISCOMPEXIST").toString();
					myClass.RAWPROCESSID=  lsCompTable.get(milv0RowNum).get("RAWPROCESSID").toString();
					myClass.FINEPROCESSID=  lsCompTable.get(milv0RowNum).get("FINEPROCESSID").toString();
					myClass.SUPPLYID= lsCompTable.get(milv0RowNum).get("SUPPLYID").toString();
					myClass.FURNACENO=  lsCompTable.get(milv0RowNum).get("FURNACENO").toString();
					myClass.IS_INSERT=  lsCompTable.get(milv0RowNum).get("IS_INSERT").toString();
					myClass.TRACETYPE=  lsCompTable.get(milv0RowNum).get("TRACETYPE").toString();
					myClass.LOTID=  lsCompTable.get(milv0RowNum).get("LOTID").toString();
					   
					arrayList.add(myClass); 
				    
					close(v,arrayList);
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompTable.this, e.toString());
				}
			}
		});

        // btnExit

		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					 finish(); // 关闭Activity  
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompTable.this, e.toString());
				}
			}
		});
} catch (Exception e) {
	MESCommon.showMessage(WIP_CompTable.this, e.toString());
}
	}

	
	
	public void close(View v, ArrayList<CompInformation> result)  
	{  
	 Intent intent = new Intent();  
	 intent.putExtra("key", result);  
	 this.setResult(RESULT_OK, intent); // 设置结果数据  
	 this.finish(); // 关闭Activity  
	}  


	public void Clear() {
		try {
            lsCompTable.clear();
            arrayList .clear();
		} catch (Exception e) {
			MESCommon.showMessage(WIP_CompTable.this, e.toString());
		}

	}
	
  
	public static class wiptrackinAdapterTab0 extends BaseAdapter {
		// 物料记娽
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// 上下文
		private Context context;
		// 用来控制CheckBox的选中状况
		private static HashMap<Integer, Boolean> isSelected;
		// 用来导入布局
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapterTab0(List<HashMap<String, String>> items,
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
			try {
				
			
			if (convertView == null) {
			
				// 获得ViewHolder对象
				holder = new ViewHolder();
				// 导入布局并赋值给convertview
				convertView = inflater.inflate(R.layout.activity_wip_comptable_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wipcomptable_cb);
				holder.tvSerialnumberId = (TextView) convertView.findViewById(R.id.wipcomptable_tvSerialnumberId);
				holder.tvMaterialMame = (TextView) convertView.findViewById(R.id.wipcomptable_tvMaterialMame);
				holder.tvMaterialID = (TextView) convertView.findViewById(R.id.wipcomptable_tvMaterialID);
				holder.tvRId = (TextView) convertView.findViewById(R.id.wipcomptable_RId);
				holder.tvFId = (TextView) convertView.findViewById(R.id.wipcomptable_FId);
				holder.tvLId = (TextView) convertView.findViewById(R.id.wipcomptable_LId);
				// 为view设置标签
				convertView.setTag(holder);
			} else {
				// 取出holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// 设置list中TextView的显示
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvRId.setText(getItem(position).get("RAWPROCESSID").toString());	
			holder.tvFId.setText(getItem(position).get("FINEPROCESSID").toString());
			holder.tvLId.setText(getItem(position).get("LOTID").toString());
			holder.tvMaterialMame.setText(getItem(position).get("MaterialMame").toString());	
			holder.tvMaterialID.setText(getItem(position).get("MaterialId").toString());

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
			wiptrackinAdapterTab0.isSelected = isSelected;
		}
		
		public static class ViewHolder {
			CheckBox cb;
			TextView tvSerialnumberId;
			TextView tvRId;
			TextView tvFId;
			TextView tvLId;
			TextView tvMaterialMame;
			TextView tvMaterialID;
		}
	}

	private  String getCompid(String sProductid)
	{
		try {		
			String sResult="";
			List<HashMap<String, String>> lsProductType = new ArrayList<HashMap<String, String>>();
			String sSQL = "SELECT PRODUCTTYPE FROM MPRODUCT WHERE  PRODUCTID='"+sProductid+"'   ";
			db.GetData(sSQL, lsProductType);
			if(lsProductType.size()>0)
			{
			    return sResult=lsProductType.get(0).get("PRODUCTTYPE").toString();
			}
		    return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompTable.this, e.toString());
			return e.toString();
		}
	}
	
	//此方法只是关闭软键盘
	private void hintKbTwo() {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
		 if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		}
		
		
		
}

