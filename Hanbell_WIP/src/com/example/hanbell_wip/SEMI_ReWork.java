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

	// �����ϵ�HashMap��¼
	private List<HashMap<String, String>> lsCompID = new ArrayList<HashMap<String, String>>();
	private List<HashMap<String, String>> lsCompTable = new ArrayList<HashMap<String, String>>();
	
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semi_rework);
		// ȡ�ÿؼ�
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
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				wiptrackinAdapter.ViewHolder holder = (wiptrackinAdapter.ViewHolder) arg1
						.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				wiptrackinAdapter.getIsSelected().put(position,
						holder.cb.isChecked());
				// ��CheckBox��ѡ��״����¼����
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
					// ��ѯ������
					try {
					EditText txtInput = (EditText) findViewById(R.id.semireworkedit_tvInput);
					
					if (txtInput.getText().toString().trim().length() == 0) {
						MESCommon.show(SEMI_ReWork.this, "��ɨ������!");
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
							
							 //�ɱ�������0
	                        if (lsPlan.get(0).get("LEFTQTY").toString() == "0")
	                        {
	                            if (lsPlan.get(0).get("PROCESSSTATE").toString() == "δ����")
	                            {
	                            	MESCommon.show(SEMI_ReWork.this, "����[" + editInput.getText().toString().trim().toUpperCase() + "]��û���ߣ����ܱ���!");
	                                editInput.setText("");
	                                  setFocus(editProductORderID);
	                                return false;
	                            }
	                            else
	                            {
	                                MESCommon.show(SEMI_ReWork.this, "����[" + editInput.getText().toString().trim().toUpperCase() + "]�ɱ�������0�����ܱ�����");
	                                editInput.setText("");
	                                  setFocus(editProductORderID);
	                                return false;
	                            }

	                        }


	                        if (lsPlan.get(0).get("PRODUCTORDERTYPE").toString() == "һ������")
	                        {
	                            MESCommon.show(SEMI_ReWork.this, "����[" + editInput.getText().toString().trim().toUpperCase() + "]��һ������������ع��󶨱�����");
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
	                        	MESCommon.show(SEMI_ReWork.this,"����[" + editInput.getText().toString().trim().toUpperCase() + "]��û����ȷ�ϣ�����֪ͨ�ֿ�����ȷ��!");
	                        	setFocus(editProductORderID);
	                        	return false;
	                        }  
	                        
	                        editProductORderID.setText(editInput.getText().toString());
							editQty.setText(lsPlan.get(0).get("QTY").toString());
							
	                        
						}else
						{
							MESCommon.show(SEMI_ReWork.this, "������룺��"+editInput.getText().toString().trim()+"��,��MESϵͳ�в����ڣ�");
							return false;
						}
						editInput.setText("");						
					}else
					{	
						if(lsCompTable.size()<= (Integer.parseInt(editQty.getText().toString().trim())-1))
						{
							for (int i = 0; i < lsCompTable.size(); i++) {
								if (lsCompTable.get(i).get("SerialnumberID").toString().equals(txtInput.getText().toString().trim())) {
									MESCommon.show(SEMI_ReWork.this, "��������["+ txtInput.getText().toString().trim()+ "] �����嵥��,��ѡ���µĹ�������!");
									setFocus(editProductORderID);
									return false;
								}
							}
		
							lsCompID.clear();
							String sResult = db.GetProductSerialNumber(txtInput.getText().toString().trim(),"","", "QF","���Ʒ","", lsCompID);
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
	                        	MESCommon.show(SEMI_ReWork.this, "����[" +lsCompID.get(0).get("PRODUCTSERIALNUMBER").toString()+ "]�Ѿ���������["+editProductORderID.getText().toString().trim()+"]�������ظ���!");
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
							MESCommon.show(SEMI_ReWork.this, "������룺��"+editProductORderID.getText().toString()+"��,�Ѿ�ȫ�����꣡��ѡ���µ�����");
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
									MESCommon.show(SEMI_ReWork .this,	"��ѡ��Ҫɾ�����㲿��");
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
							MESCommon.show(SEMI_ReWork.this, "�������Ҫ�󶨵Ĺ������룬�ڽ����ع������!");
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
						Toast.makeText(SEMI_ReWork.this, "�󶨳ɹ�!",Toast.LENGTH_SHORT).show();
					
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
		// ���ϼǊ�
		private List<HashMap<String, String>> items = new ArrayList<HashMap<String, String>>();
		// ������
		private Context context;
		// ��������CheckBox��ѡ��״��
		private static HashMap<Integer, Boolean> isSelected;
		// �������벼��
		private LayoutInflater inflater = null;
		int iPosition = -1;

		public wiptrackinAdapter(List<HashMap<String, String>> items,
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
			try{
				if (convertView == null) {
					// ���ViewHolder����
					holder = new ViewHolder();
					// ���벼�ֲ���ֵ��convertview
					convertView = inflater.inflate(R.layout.activity_semi_rework_listview, null);
					holder.cb = (CheckBox) convertView.findViewById(R.id.semiReworklv_cb);
					holder.tvOrderId = (TextView) convertView.findViewById(R.id.semiRework_tvOrderId);
					holder.tvSerialnumber = (TextView) convertView.findViewById(R.id.semiRework_tvSerialnumber);
				
					// Ϊview���ñ�ǩ
					convertView.setTag(holder);
				} else {
					// ȡ��holder
					holder = (ViewHolder) convertView.getTag();
					
				}
				// ����list��TextView����ʾ
				holder.tvOrderId.setText(getItem(position).get("OrderId").toString());	
				holder.tvSerialnumber.setText(getItem(position).get("Serialnumber").toString());	
				
				if (getItem(position).get("CHECKFLAG").toString().equals("Y")) {
					// ��CheckBox��ѡ��״����¼����
					getIsSelected().put(position, true);
				} else {
					// ��CheckBox��ѡ��״����¼����
					getIsSelected().put(position, false);
				}
				// ����isSelected������checkbox��ѡ��״��
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

