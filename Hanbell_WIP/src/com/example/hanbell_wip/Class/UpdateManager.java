package com.example.hanbell_wip.Class;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.example.hanbell_wip.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
//import hanbell.common.MESCommon;

public class UpdateManager {
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 淇濆瓨瑙ｆ瀽鐨刋ML淇℃伅 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	
	//String msDefaltURL = "http://10.214.226.81/FtcMesWebService/STK_Version.xml";
	//String msDefaltURL = "http://192.168.1.103/FtcMesWebService/STK_Version.xml";
	//String msDefaltURL = "http://172.16.10.94/FtcMesWebService/PDA/WIP_Version.xml";
	String msProjectName = "com.example.hanbell_wip";
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				//mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate() {
		if (isUpdate()) {
			// 显示提示对话框
			showNoticeDialog();
		} else {
			Toast.makeText(mContext, "当前已是最新版本！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	private boolean isUpdate()
	{
		// 鑾峰彇褰撳墠杞欢鐗堟湰
		int versionCode = getVersionCode(mContext);
		URL url;//定义网络中version.xml的连接
		try { //一个测试
			url = new URL(MESCommon.msAppXML);//创建version.xml的连接地址。
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);//请求反应时间
			  
			InputStream inStream = connection.getInputStream();//从输入流获取数据
			ParseXmlService service = new ParseXmlService();
			mHashMap = service.parseXml(inStream);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 鐗堟湰鍒ゆ柇
			if (serviceCode != MESCommon.miVersion)
			{
				return true;
			}
		}
		return false;
	}
	private boolean isUpdate1() {
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// 把version.xml放到网络上，然后获取文件信息

		int serviceCode = 1;
		// 版本判断
		if (serviceCode > versionCode) {
			return true;
		}
		return false;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(msProjectName, 0).versionCode;

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog() {
		// 构造对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage("检测到新版本，立即更新吗?");
		// 更新
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
			}
		});
//		// 稍后更新
//		builder.setNegativeButton("稍后更新", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog() {
		// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("正在更新");
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 鍒ゆ柇SD鍗℃槸鍚﹀瓨鍦紝骞朵笖鏄惁鍏锋湁璇诲啓鏉冮檺
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 鑾峰緱瀛樺偍鍗＄殑璺緞
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 鍒涘缓杩炴帴
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 鑾峰彇鏂囦欢澶у皬
					int length = conn.getContentLength();
					// 鍒涘缓杈撳叆娴�
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, mHashMap.get("name"));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缂撳瓨
					byte buf[] = new byte[1024];
					// 鍐欏叆鍒版枃浠朵腑
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 璁＄畻杩涘害鏉′綅缃�
						progress = (int) (((float) count / length) * 100);
						// 鏇存柊杩涘害
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 涓嬭浇瀹屾垚
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 鍐欏叆鏂囦欢
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 鐐瑰嚮鍙栨秷灏卞仠姝笅杞�
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 鍙栨秷涓嬭浇瀵硅瘽妗嗘樉绀�
			mDownloadDialog.dismiss();
		}
	};
	
	private class downloadApkThread1 extends Thread {
		@Override
		public void run() {
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					
					mSavePath = sdpath + "download";
					URL url = new URL(mHashMap.get("url"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = url.openStream();
					OutputStream os = mContext.openFileOutput(mHashMap.get("name"),Context.MODE_PRIVATE);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						os.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					os.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 閫氳繃Intent瀹夎APK鏂囦欢
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		  android.os.Process.killProcess(android.os.Process.myPid());
		//startActivity(i);
//		String sUrl= mContext.getPackageResourcePath();
//		
//		File apkfile = new File(sUrl);		
//		if (!apkfile.exists()) {
//			return;
//		}
//		// 通过Intent安装APK文件
//		Intent i = new Intent(Intent.ACTION_VIEW);
//		i.setDataAndType(Uri.parse("file://"+sUrl), "application/vnd.android.package-archive");
//		mContext.startActivity(i);
	}
}