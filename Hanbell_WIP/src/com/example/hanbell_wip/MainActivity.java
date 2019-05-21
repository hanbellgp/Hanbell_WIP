package com.example.hanbell_wip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hanbell_wip.Class.MESCommon;
import com.example.hanbell_wip.Class.MESDB;
import com.example.hanbell_wip.Class.PrefercesService;
import com.example.hanbell_wip.Class.UpdateManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button btnLogin, btnLogout, btnTrackIn,  btnEqpStart,btnVersion,
			   btnEQPSetting,btnSEQEdit, btnWipPaintingEnd,btnAnalysisitem,
			btnWIPDefect,btnTrackIn_pre,btnBarcodeUpdate,btnWIPInstock,btnCompEdit,btnOQC,btnOUTStock,btnCompTemp,btnFinishTemp,btnShipment,btnShipmentPre;
	PrefercesService prefercesService;
	Map<String,String> params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// // android 3.0以上联网需要加上下面2句
try{
	   
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()
        .penaltyLog().penaltyDeath().build());  
		 // 初始化参数到配置文件
		 Context ctx = MainActivity.this;
		 SharedPreferences sp = ctx
		 .getSharedPreferences("Hanbell", MODE_PRIVATE);
		 Editor editor = sp.edit();
		 // 判定是否有,若没有则为第一次加载初始化
		 // String sURL = sp.getString("WebServiceURL", "");
		 String sURL = "";
		 if (sURL == "") {
		 editor.putString("WebServiceURL", MESCommon.msDefaltURL);
		 editor.commit();
		 }
		 // 初始化参数
		 MESDB.serviceURL = sp.getString("WebServiceURL", "");
		 // 版本更新
		 UpdateManager clsUpdate = new UpdateManager(ctx);
		 clsUpdate.checkUpdate();

		// 取得控件
		btnBarcodeUpdate= (Button) findViewById(R.id.btnBarcodeUpdate);
		btnEQPSetting = (Button) findViewById(R.id.btnEQPSetting);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnVersion = (Button) findViewById(R.id.btnVersion);
		btnTrackIn_pre= (Button) findViewById(R.id.btnTrackIn_pre);
		btnTrackIn = (Button) findViewById(R.id.btnTrackIn);
		btnEqpStart = (Button) findViewById(R.id.btnEqpStart);		
		btnWipPaintingEnd = (Button) findViewById(R.id.btnWipPaintingEnd);
		btnWIPDefect = (Button) findViewById(R.id.btnWIPDefect);
		btnWIPInstock= (Button) findViewById(R.id.btnWIPInstock);
		btnCompEdit=(Button) findViewById(R.id.btnCompEdit);
		btnSEQEdit=(Button) findViewById(R.id.btnSEQEdit);
		btnOQC=(Button) findViewById(R.id.btnOQC);
		btnShipment=(Button) findViewById(R.id.btnShipment); //20190321 zhanghui 针对整机出货，仓库使用
		btnShipmentPre=(Button) findViewById(R.id.btnShipmentPre); //20190506 zhanghui 针对整机预出货，仓库使用
		btnOUTStock=(Button) findViewById(R.id.btnOUTStock);
		btnCompTemp=(Button) findViewById(R.id.btnCompTemp);
		btnFinishTemp=(Button) findViewById(R.id.btnFinishTemp);
		btnAnalysisitem=(Button) findViewById(R.id.btnAnalysisitem);
		prefercesService  =new PrefercesService(this);  
	    params=prefercesService.getPreferences();  
	    btnWIPDefect.setVisibility(8);
//	    btnEQPSetting.setVisibility(0);
//		btnEqpStart.setVisibility(0);	
//		btnVersion.setVisibility(0);	
//		btnBarcodeUpdate.setVisibility(8);
//		btnTrackIn_pre .setVisibility(8);
//		btnTrackIn .setVisibility(8);
//		btnWipPaintingEnd.setVisibility(8);
//		btnWIPDefect.setVisibility(8);
//		btnWIPInstock.setVisibility(8);
//		btnCompEdit.setVisibility(8);
//		btnSEQEdit.setVisibility(8);
//		
//		 if (params.get("StepName").contains("领料"))
//		{
//			
//			btnBarcodeUpdate.setVisibility(0);
//			btnCompEdit.setVisibility(0);
//			
//		}
//		else if (params.get("StepName").contains("轴承座")||params.get("StepName").contains("马达壳次组立"))
//		{
//			
//			btnTrackIn_pre .setVisibility(0);			
//			btnSEQEdit.setVisibility(0);
//			
//		}
//	 	else if (params.get("StepName").contains("清洗")||params.get("StepName").contains("滑块")||params.get("StepName").contains("涂装")||params.get("StepName").contains("入库"))
//		{
//			
//	 		btnWipPaintingEnd .setVisibility(0);
//			btnCompEdit.setVisibility(0);
//		}else {
//			btnTrackIn .setVisibility(0);
//			btnCompEdit.setVisibility(0);
//		}
		InitActivity();
		
		// btnLogin
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity,并传参数
					Intent i = new Intent(MainActivity.this, Login.class);
					i.putExtra("ID", "admin");
					// startActivity(i);
					startActivityForResult(i, 0);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		
		btnLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InitActivity();
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});

		btnVersion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					 // 初始化参数到配置文件
					 Context ctx = MainActivity.this;
					 SharedPreferences sp = ctx
					 .getSharedPreferences("Hanbell", MODE_PRIVATE);
					 Editor editor = sp.edit();
					 // 判定是否有,若没有则为第一次加载初始化
					 // String sURL = sp.getString("WebServiceURL", "");
					 String sURL = "";
					 if (sURL == "") {
					 editor.putString("WebServiceURL", MESCommon.msDefaltURL);
					 editor.commit();
					 }
					 // 初始化参数
					 MESDB.serviceURL = sp.getString("WebServiceURL", "");
					 // 版本更新
					 UpdateManager clsUpdate = new UpdateManager(ctx);
					 clsUpdate.checkUpdate();

				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		// btnBarcodeUpdate:领料
		btnBarcodeUpdate.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				// 开启新Activity
				Intent i = new Intent(MainActivity.this, WIP_BarCode_Update.class);
				startActivity(i);
			} catch (Exception e) {
				MESCommon.showMessage(MainActivity.this, e.toString());
			}
		 }
    	});
		// btnEQPSetting:设定设备
		btnEQPSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this, EQP_Setting.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		// btnEqpStart:人员设备报工
		btnEqpStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this, EQP_Start.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		
		// btnTrackIn_pre:次组立装配报工
		btnTrackIn_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this, WIP_TrackIn_Pre.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		// btnTrackIn:装配报工
		btnTrackIn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					params=prefercesService.getPreferences();
					//若是「服务维修」，则开启WIP_TrackIn_CRM
					String sProductType = params.get("ProductType");
					if (!sProductType.equals("服务维修"))
					{
						// 开启新Activity
						Intent i = new Intent(MainActivity.this, WIP_TrackIn.class);
						startActivity(i);
					}
					else
					{
						// 开启新Activity
						Intent i = new Intent(MainActivity.this, WIP_TrackIn_CRM.class);
						startActivity(i);
					}
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		
		// btnCompEdit:装配信息
		btnCompEdit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this, WIP_COMPEDIT.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		
		// btnSEQEdit:组立装配信息
		btnSEQEdit.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this, WIP_SEQEdit.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
						
//		// btnTrackInPreNew
//		btnTrackInPreNew.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						try {
//							// 开启新Activity
//							Intent i = new Intent(MainActivity.this,
//									WIP_TrackIn_PreNew.class);
//							startActivity(i);
//						} catch (Exception e) {
//							MESCommon.showMessage(MainActivity.this, e.toString());
//						}
//					}
//				});


		// btnWipPaintingEnd:整机报工
		btnWipPaintingEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this,
							WIP_PaintingEnd.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});

		// btnWIPDefect:不良录入
		btnWIPDefect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this, WIP_Defect.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		
		
		//btnWIPInstock:制造号码入库
		btnWIPInstock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this,
							WIP_InStock.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
		
		//btnOQC:机体出厂检验
		btnOQC.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this,
									WIP_OQC_QCANALYSIS.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		//btnShipmentPre:整机预出货 zhanghui 20190521 Add
		btnShipmentPre.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
								try {
									// 开启新Activity
									Intent i = new Intent(MainActivity.this,
											STK_Shipment_Pre.class);
									startActivity(i);
								} catch (Exception e) {
									MESCommon.showMessage(MainActivity.this, e.toString());
								}
							}
						});
		//btnShipment:整机出货 zhanghui 20190321 Add
		btnShipment.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this,
									STK_Shipment.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		
		//btnOUTStock:自动仓出库作业
		btnOUTStock.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this,
									STK_OutStock.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		
		//btnCompTemp：半成品盘点作业
		btnCompTemp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this,
								SEMI_TEMP.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		//btnFinishTemp：整机在制盘点作业
		btnFinishTemp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							// 开启新Activity
							Intent i = new Intent(MainActivity.this,
								FINISH_TEMP.class);
							startActivity(i);
						} catch (Exception e) {
							MESCommon.showMessage(MainActivity.this, e.toString());
						}
					}
				});
		//btnAnalysisitem：装配检验
		btnAnalysisitem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 开启新Activity
					Intent i = new Intent(MainActivity.this,
					WIP_ANALYSISITEM.class);
					startActivity(i);
				} catch (Exception e) {
					MESCommon.showMessage(MainActivity.this, e.toString());
				}
			}
		});
	} catch (Exception e) {
		MESCommon.showMessage(MainActivity.this,e.toString());
	}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		try{
		// 可以根据多个请求代码来作相应的操作
		if (requestCode == 0 && resultCode == 10) {
			Bundle data = intent.getExtras();
			String strUserId = data.getString("UserId");
			String strName = data.getString("UserName");
			MESCommon.UserId = strUserId;
			MESCommon.UserName = strName;
			Toast.makeText(this, strUserId + ":" + strName, Toast.LENGTH_SHORT)
					.show();
			btnLogin.setEnabled(false);
			btnTrackIn.setEnabled(true);
			btnLogout.setEnabled(true);
			btnEqpStart.setEnabled(true);
			btnEQPSetting.setEnabled(true);
			btnWipPaintingEnd.setEnabled(true);
			btnWIPDefect.setEnabled(true);
			//btnTrackInPreNew.setEnabled(true);
			btnTrackIn_pre.setEnabled(true);
			btnCompEdit.setEnabled(true);
			btnBarcodeUpdate.setEnabled(true);
			btnWIPInstock.setEnabled(true);
            btnSEQEdit.setEnabled(true);
            btnOQC.setEnabled(true);
            btnShipment.setEnabled(true);
			btnOUTStock.setEnabled(true);
			btnCompTemp.setEnabled(true);
			btnFinishTemp.setEnabled(true);
			btnAnalysisitem.setEnabled(true);
			// MESCommon.showMessage(MainActivity.this, "登录人：" + strName);
		}
		} catch (Exception e) {
			MESCommon.showMessage(MainActivity.this,e.toString());
		}
		
	}

	protected void InitActivity() {
		try {
			MESCommon.UserId = "";
			MESCommon.UserName = "";
			btnLogin.setEnabled(true);
			btnTrackIn.setEnabled(false);
			btnLogout.setEnabled(false);
			btnEqpStart.setEnabled(false);
			btnEQPSetting.setEnabled(false);
			btnWipPaintingEnd.setEnabled(false);
			btnWIPDefect.setEnabled(false);
			//btnTrackInPreNew.setEnabled(false);
			btnTrackIn_pre.setEnabled(false);
			btnCompEdit.setEnabled(false);
			btnBarcodeUpdate.setEnabled(false);
			btnWIPInstock.setEnabled(false);
			btnSEQEdit.setEnabled(false);
			btnOQC.setEnabled(false);
			btnShipment.setEnabled(false);
			btnOUTStock.setEnabled(false);
			btnCompTemp.setEnabled(false);
			btnFinishTemp.setEnabled(false);
			btnAnalysisitem.setEnabled(false);
		} catch (Exception e) {
			MESCommon.showMessage(MainActivity.this, e.toString());
		}
	}

	protected boolean CheckLogin() {
		try {
			if (MESCommon.UserId.equals("")) {
				MESCommon.showMessage(MainActivity.this, "请先登入再操作!!");
				return false;
			}
		} catch (Exception e) {
			MESCommon.showMessage(MainActivity.this, e.toString());
		}
		return true;
	}

}
