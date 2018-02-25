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

public class WIP_CompAdd extends Activity {

	private MESDB db = new MESDB();

	ListView lv0;
	Button btnConfirm, btnAdd,btnDelete;
	EditText editProductserialnumber,editSupplyID,editProductlName,editMaterialid,editLNO;
	TextView   h6,h7,h8,h9;
	//wiptrackinAdapter adapter;	
	PrefercesService prefercesService;
	Map<String,String> params;
	wiptrackinAdapterTab0 adapterTab0;
	// �����ϵ�HashMap��¼
	static int milv0RowNum = 0;

	private List<HashMap<String, String>> lsExistcheck=new ArrayList<HashMap<String, String>>();
	
	List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
    ArrayList<CompInformation> arrayList = new ArrayList<CompInformation>();
	SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	SimpleDateFormat sDateFormatShort = new SimpleDateFormat("yyyy/MM/dd");


	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setContentView(R.layout.activity_wip_comp_add);

		// ȡ�ÿؼ�
try{
		editProductserialnumber = (EditText) findViewById(R.id.wipcompadd_tvProductserialnumber);	
		editProductlName= (EditText) findViewById(R.id.wipcompadd_tvProductName);	
		editSupplyID = (EditText) findViewById(R.id.wipcompadd_tvSupplyid);	
		editMaterialid=(EditText) findViewById(R.id.wipcompadd_tvMaterialid);	
		editLNO=(EditText) findViewById(R.id.wipcompadd_tvLNO);	
		btnConfirm = (Button) findViewById(R.id.wipcompreplace_btnConfirm);	
			
		btnAdd = (Button) findViewById(R.id.wipcompreplace_btnAdd);	
	    btnDelete  = (Button) findViewById(R.id.wipcompreplace_btnDelete);	
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
	    actionBar.setSubtitle("������Ա��"+MESCommon.UserName); 
	    actionBar.setTitle("�㲿���ֶ�����");
		
	 // �ؼ��¼�
	 		lv0.setOnItemClickListener(new OnItemClickListener() {
	 			@Override
	 			public void onItemClick(AdapterView<?> arg0, View arg1,
	 					int position, long arg3) {
	 				try{
	 				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
	 				wiptrackinAdapterTab0.ViewHolder holder = (wiptrackinAdapterTab0.ViewHolder) arg1.getTag();
	 				// �ı�CheckBox��״̬
	 				holder.cb.toggle();
	 				// ��CheckBox��ѡ��״����¼����
	 				wiptrackinAdapterTab0.getIsSelected().put(position,
	 						holder.cb.isChecked());
	 				milv0RowNum=position;
	 				if(holder.cb.isChecked())
					{
					lsCompTable	.get(position).put("CHECKFLAG","Y"); 
					}else
					{
					lsCompTable.get(position).put("CHECKFLAG","N"); 
					}
	 			} catch (Exception e) {
	 				MESCommon.showMessage(WIP_CompAdd.this, e.toString());
	 			}
	 			}
	 			
	 		});
	    
	 		editProductserialnumber.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_ENTER&& event.getAction() == KeyEvent.ACTION_DOWN) {
						// ��ѯ������
						try {
							if (editProductserialnumber.getText().toString().trim().length() == 0) {
								editProductserialnumber.setText("");
								MESCommon.show(WIP_CompAdd.this, "��ɨ������!");
								return false;
							}
							lsCompID.clear();
							String  sResult = db.GetProductSerialNumber_PDA(editProductserialnumber.getText().toString().trim(),editMaterialid.getText().toString().trim(),"", "QF","�㲿��","װ��", lsCompID);
							if (sResult.equals(""))
		                    {
							  if(lsCompID.size()==1)
							  {
								 
								  editProductlName.setText(lsCompID.get(0).get("MATERIALNAME").toString());
							      editSupplyID.setText(lsCompID.get(0).get("SUPPLYID").toString());
							      editMaterialid .setText(lsCompID.get(0).get("MATERIALID").toString());
							      editLNO.setText(lsCompID.get(0).get("FURNACENO").toString());
							      
							  }else if (lsCompID.size()>1) {
								  openNewActivity2(v);
							  }
		                    }
							
						} catch (Exception e) {
							MESCommon.showMessage(WIP_CompAdd.this, e.toString());
							return false;
						}
					}
						
					return false;
				}
			});
		
	    btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
					if (editProductserialnumber.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_CompAdd.this, "������������!");
						editProductserialnumber.setText("");
						return ;
					}
					if (editProductlName.getText().toString().trim().length() == 0) {
						MESCommon.show(WIP_CompAdd.this, "������Ʒ��,�ڽ������!");
						editProductlName.setText("");
						return ;
					}
					for(int i=0;i<lsCompTable.size();i++)
					{
						if(editProductserialnumber.getText().toString().trim().equals(lsCompTable.get(i).get("PRODUCTSERIALNUMBER").toString())||editProductserialnumber.getText().toString().trim().equals(lsCompTable.get(i).get("SEQ").toString()))
						{
							
							MESCommon.show(WIP_CompAdd.this, "��������[" + editProductserialnumber.getText().toString().trim() + "] ���������嵥��!");
							return  ;
						}
					
					}
					lsCompID.clear();
					String  sResult = db.GetProductSerialNumber_PDA(editProductserialnumber.getText().toString().trim(),editMaterialid.getText().toString().trim(),"", "QF","�㲿��","װ��", lsCompID);
					if (sResult.equals("")) {

						if(lsCompID.get(0).get("TRACETYPE").toString().equals("C"))
						{
							sResult= checkExist(lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());	
						}
					
						if(!sResult.equals(""))
						{
							
							AlertDialog alert=	new AlertDialog.Builder(WIP_CompAdd.this).setTitle("ȷ��").setMessage(sResult+",�Ƿ��������!")
									.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {  
					            @Override  
					            public void onClick(DialogInterface dialog,int which) {  
					                // TODO Auto-generated method stub  
					    
					            	InsertCompTable("Y");			
					            }  
					        })  
							.setNeutralButton("ȡ��",new DialogInterface.OnClickListener() {  
							            @Override  
							            public void onClick(DialogInterface dialog,int which) {  
							                // TODO Auto-generated method stub  
							       
							            	return  ;
							            }  
							 }).show();
						}
						else {
							InsertCompTable("N");			
						}
					}else {
						//�ֶ���ӣ�������Ƿ��ظ�����Ϊ����ȷ���Ƿ�Ϊ����ģʽ����������ģʽ��
						InsertCompTable("N");	
					}
						
				    editProductserialnumber.setText("");
			            editMaterialid.setText("");
			            editProductlName.setText("");
			            editSupplyID.setText("");
			            editLNO.setText("");
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompAdd.this, e.toString());
				}
			}
		});
	    btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
					if (lsCompTable.size() > 0) {
						List<HashMap<String, String>> lsCompTableCopy = new ArrayList<HashMap<String, String>>();
						Boolean isSelect=false;
					
						for(int i=0;i<lsCompTable.size();i++)
						{
							if (lsCompTable.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								isSelect=true;
							
							}
							lsCompTableCopy.add(lsCompTable.get(i));
						}
						if(!isSelect)
						{
							MESCommon.show(WIP_CompAdd.this, "��ѡ��Ҫɾ�����㲿��");
							return;
						}
						for(int i=lsCompTableCopy.size()-1;i>=0;i--)
						{
							if (lsCompTableCopy.get(i).get("CHECKFLAG").toString()
									.equals("Y")) {
								lsCompTable.remove(i);
								
							}
						}					
						adapterTab0.notifyDataSetChanged();
					}
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompAdd.this, e.toString());
				}
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {	
					if (lsCompTable.size() == 0) {
						MESCommon.show(WIP_CompAdd.this, "�������������!");
						editProductserialnumber.setText("");
						return ;
					}	
					close(v,arrayList);
					Clear();
				} catch (Exception e) {
					MESCommon.showMessage(WIP_CompAdd.this, e.toString());
				}
			}
		});

} catch (Exception e) {
	MESCommon.showMessage(WIP_CompAdd.this, e.toString());
}
	
	}
	private  Boolean InsertCompTable(String sISCOMPREPEATED)
	{
		try {
	
			HashMap<String, String> hs = new HashMap<String, String>();				
			CompInformation myClass = new CompInformation();  
			
			hs.put("MaterialId", editMaterialid.getText().toString().trim().toUpperCase());
			hs.put("MaterialMame", editProductlName.getText().toString().trim().toUpperCase());
			myClass.MaterialId=editMaterialid.getText().toString().trim().toUpperCase(); 
			myClass.MaterialMame=editProductlName.getText().toString().trim().toUpperCase();
			
			if(lsCompID.size()>0)
			{  
				hs.put("SEQ", lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString());
				myClass.SEQ=lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString();
				  if (! lsCompID.get(0).get("FINEPROCESSID").toString().equals(""))
				{
					hs.put("PRODUCTSERIALNUMBER", lsCompID.get(0).get("FINEPROCESSID").toString());
					myClass.PRODUCTSERIALNUMBER=lsCompID.get(0).get("FINEPROCESSID").toString();
				}
				  else  if (! lsCompID.get(0).get("RAWPROCESSID").toString().equals("")&&lsCompID.get(0).get("FINEPROCESSID").toString().equals("")) {
						hs.put("PRODUCTSERIALNUMBER", lsCompID.get(0).get("RAWPROCESSID").toString());
						myClass.PRODUCTSERIALNUMBER=lsCompID.get(0).get("RAWPROCESSID").toString();
					}
				else if (!lsCompID.get(0).get("LOTID").toString().equals("")&&lsCompID.get(0).get("RAWPROCESSID").toString().equals("")&&lsCompID.get(0).get("FINEPROCESSID").toString().equals(""))
				{
					hs.put("PRODUCTSERIALNUMBER",lsCompID.get(0).get("LOTID").toString());
					myClass.PRODUCTSERIALNUMBER=lsCompID.get(0).get("LOTID").toString();
				}
				hs.put("MaterialType", lsCompID.get(0).get("MATERIALMAINTYPE").toString());
				hs.put("CHECKFLAG", "N");
				hs.put("ISCOMPEXIST", "Y");
				hs.put("RAWPROCESSID", lsCompID.get(0).get("RAWPROCESSID").toString());
				hs.put("FINEPROCESSID", lsCompID.get(0).get("FINEPROCESSID").toString());
				hs.put("SUPPLYID", lsCompID.get(0).get("SUPPLYID").toString());
				hs.put("FURNACENO",lsCompID.get(0).get("FURNACENO").toString());
				hs.put("IS_INSERT", "Y");
				hs.put("TRACETYPE", lsCompID.get(0).get("TRACETYPE").toString());
				hs.put("ISCOMPREPEATED", sISCOMPREPEATED);
				hs.put("LOTID", lsCompID.get(0).get("LOTID").toString());
				
				myClass.MaterialType= lsCompID.get(0).get("MATERIALMAINTYPE").toString();
				myClass.CHECKFLAG= "N";
				myClass.ISCOMPEXIST= "Y";
				myClass.RAWPROCESSID= lsCompID.get(0).get("RAWPROCESSID").toString();
				myClass.FINEPROCESSID= lsCompID.get(0).get("FINEPROCESSID").toString();
				myClass.SUPPLYID=lsCompID.get(0).get("SUPPLYID").toString();
				myClass.FURNACENO= lsCompID.get(0).get("FURNACENO").toString();
				myClass.IS_INSERT= "Y";
				myClass.TRACETYPE=lsCompID.get(0).get("TRACETYPE").toString();
				myClass.ISCOMPREPEATED=sISCOMPREPEATED;
				myClass.LOTID=lsCompID.get(0).get("LOTID").toString();
				arrayList.add(myClass); 
			}else {
				
				hs.put("SEQ", editProductserialnumber.getText().toString().trim().toUpperCase());
				hs.put("PRODUCTSERIALNUMBER", editProductserialnumber.getText().toString().trim().toUpperCase());
				hs.put("MaterialType", "");
				hs.put("CHECKFLAG", "N");
				hs.put("ISCOMPEXIST", "N");
				hs.put("RAWPROCESSID", editProductserialnumber.getText().toString().trim().toUpperCase());
				hs.put("FINEPROCESSID", editProductserialnumber.getText().toString().trim().toUpperCase());
				hs.put("SUPPLYID",editSupplyID.getText().toString().trim());
				hs.put("FURNACENO", "");
				hs.put("IS_INSERT", "Y");
				hs.put("TRACETYPE", "");
				hs.put("ISCOMPREPEATED", sISCOMPREPEATED);
				hs.put("LOTID", editProductserialnumber.getText().toString().trim().toUpperCase());

				myClass.SEQ= editProductserialnumber.getText().toString().trim().toUpperCase();
				myClass.PRODUCTSERIALNUMBER= editProductserialnumber.getText().toString().trim().toUpperCase();
				myClass.MaterialType= "";
				myClass.CHECKFLAG= "N";
				myClass.ISCOMPEXIST= "N";
				myClass.RAWPROCESSID= editProductserialnumber.getText().toString().trim().toUpperCase();
				myClass.FINEPROCESSID= editProductserialnumber.getText().toString().trim().toUpperCase();
				myClass.SUPPLYID=editSupplyID.getText().toString().trim();
				myClass.FURNACENO= "";
				myClass.IS_INSERT= "Y";
				myClass.TRACETYPE="";
				myClass.ISCOMPREPEATED=sISCOMPREPEATED;
				myClass.LOTID=editProductserialnumber.getText().toString().trim().toUpperCase();
			    arrayList.add(myClass); 
			}
			
			lsCompTable.add(0,  hs);	
			adapterTab0.notifyDataSetChanged();	
			
			return true ;
		
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompAdd.this, e.toString());
			return false ;
		}
	}

	// �з���ֵ��Activity  
		public void openNewActivity2(View v)  
		{  try{
		 Intent intent = new Intent();  
		 intent.setClass(this.getApplicationContext(), WIP_CompTable.class);  
		 intent.putExtra("MaterialId",editMaterialid.getText().toString());  
		 intent.putExtra("PRODUCTSERIALNUMBER", editProductserialnumber.getText().toString());  
	
		 startActivityForResult(intent, 1);  
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompAdd.this, e.toString());
			
		}
		}  

		@Override  
		protected void onActivityResult(int requestCode, int resultCode, Intent data)  
		{  
		 // requestCode��������ҵ��  
		 // resultCode��������ĳ��ҵ���ִ�����  
			try{
		 if (1 == requestCode && RESULT_OK == resultCode)  
		 {  
			 ArrayList<CompInformation> arrayList2 = (ArrayList<CompInformation>) data.getSerializableExtra("key");  
	         String result = "" ;  

	         for (CompInformation myClass : arrayList2) {  

				        editProductserialnumber.setText(myClass.PRODUCTSERIALNUMBER);
			            editMaterialid.setText(myClass.MaterialId);
			            editProductlName.setText( myClass.MaterialMame);
			            editSupplyID.setText(myClass.SUPPLYID);
			            editLNO.setText(myClass.FURNACENO);
			        	btnAdd.performClick();

	                  }  
		 }
		   }  
		 catch (Exception e) {
				// TODO: handle exception
				MESCommon.showMessage(WIP_CompAdd.this, e.toString());
				
			}
		}  
	
	public void close(View v, ArrayList<CompInformation> result)  
	{  
	 Intent intent = new Intent();  
	 intent.putExtra("key", result);  
	 this.setResult(RESULT_OK, intent); // ���ý������  
	 this.finish(); // �ر�Activity  
	}  

	public String checkExist(String sSerialNumber)
	{
		String sResult="";
		String sSql="";
		try {
			lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_P WHERE PRODUCTSERIALNUMBER = '" + sSerialNumber + "' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���װ��ʹ��";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')='' ";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���������װ��ʹ��";
            }
            lsExistcheck.clear();
            sSql = "SELECT * FROM PROCESS_STEP_PF WHERE SERIALNUMBER_P = '" + sSerialNumber + "'  AND isnull(PRODUCTCOMPID,'')!=''";
            db.GetData(sSql, lsExistcheck);
            if (lsExistcheck.size() > 0)
            {
                return sResult = "ˢ�����룺��" + sSerialNumber + "��,�Ѿ���װ��ʹ��";
            }
			 return sResult;
		} catch (Exception e) {
			// TODO: handle exception
			MESCommon.showMessage(WIP_CompAdd.this, e.toString());
			return sResult=e.toString();
		}
		
	}

	public void Clear() {
		try {
            lsCompTable.clear();
            arrayList .clear();
            editProductserialnumber.setText("");
            editMaterialid.setText("");
            editProductlName.setText("");
            editSupplyID.setText("");
            editLNO.setText("");
		} catch (Exception e) {
			MESCommon.showMessage(WIP_CompAdd.this, e.toString());
		}

	}
	
  
	public static class wiptrackinAdapterTab0 extends BaseAdapter {
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		
		// ������
		private Context context;
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// �������벼��
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapterTab0(List<HashMap<String, String>> items,
				Context context) {
			this.items = items;
			this.context = context;
			inflater = LayoutInflater.from(context);	
			isSelected = new HashMap<Integer, Boolean>();
			// ��ʼ������
			initData();

		}
		// ��ʼ��isSelected������
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
			
				// ���ViewHolder����
				holder = new ViewHolder();
				// ���벼�ֲ���ֵ��convertview
				convertView = inflater.inflate(R.layout.activity_wip_track_in_tab0_listview, null);
				holder.cb = (CheckBox) convertView.findViewById(R.id.wiptrackinlv0_cb);
				holder.tvSerialnumberId = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvSerialnumberId);
				holder.tvMaterialMame = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialMame);
				holder.tvMaterialID = (TextView) convertView.findViewById(R.id.wiptrackinlv0_tvMaterialID);
				// Ϊview���ñ�ǩ
				convertView.setTag(holder);
			} else {
				// ȡ��holder
				holder = (ViewHolder) convertView.getTag();
				
			}
			// ����list��TextView����ʾ
			holder.tvSerialnumberId.setText(getItem(position).get("PRODUCTSERIALNUMBER").toString());	
			holder.tvMaterialMame.setText(getItem(position).get("MaterialMame").toString());	
			holder.tvMaterialID.setText(getItem(position).get("MaterialId").toString());

			// ��CheckBox��ѡ��״����¼����
			if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
				// ��CheckBox��ѡ��״����¼����
				getIsSelected().put(position, true);
			} else {
				// ��CheckBox��ѡ��״����¼����
				getIsSelected().put(position, false);
			}
			// ����isSelected������checkbox��ѡ��״��
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
			TextView tvMaterialMame;
			TextView tvMaterialID;
		}
	}

	//�˷���ֻ�ǹر������
	private void hintKbTwo() {
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
		 if(imm.isActive()&&getCurrentFocus()!=null){
			if (getCurrentFocus().getWindowToken()!=null) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}			  
		 }
		}
		
		
		
}

