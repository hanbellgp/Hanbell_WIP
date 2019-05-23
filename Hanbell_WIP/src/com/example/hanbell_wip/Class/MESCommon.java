package com.example.hanbell_wip.Class;

import android.app.AlertDialog;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
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

  
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class MESCommon {

	public static String msDefaltURL = "http://172.16.10.94/FtcMesWebService_Test/FtcMesWebService.asmx";
	public static String msAppXML = "http://172.16.10.94/FtcMesWebService/PDA/WIP_Version.xml";
	public static int miVersion = 76;
	public static String UserId = "";
	public static String UserName = "";
	public static String SysId = "convert(varchar,getdate(),112) ||  str_replace(convert(varchar,getdate(), 108),':',null) || CASE datalength( convert(VARCHAR,datepart(ms,getdate())) ) WHEN 1 THEN  '00' || convert(VARCHAR,datepart(ms,getdate()))  WHEN 2 THEN  '0' || convert(VARCHAR,datepart(ms,getdate()))  ELSE convert(VARCHAR,datepart(ms,getdate()))  END";
	public static String ModifyTime = "convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108)";
	
	
	public static void showMessage(Context context,String str) {
		new AlertDialog.Builder(context).setTitle("Exception").setMessage(str).setPositiveButton("确定", null).show();
	}
	
	public static void show(Context context,String str) {
		new AlertDialog.Builder(context).setTitle("Information").setMessage(str).setPositiveButton("确定", null).show();
	}
	

	
	public static boolean Confirm(Context context) {
		final boolean[] answer = new boolean[1];
	    AlertDialog dialog = new AlertDialog.Builder(context).create();
	    dialog.setTitle("Confirmation");
	    dialog.setMessage("Choose Yes or No");
	    dialog.setCancelable(false);
	    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int buttonId) {
	            answer[0] = true;
	        }
	    });
	    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int buttonId) {
	            answer[0] = false;
	        }
	    });
	    dialog.setIcon(android.R.drawable.ic_dialog_alert);
	    dialog.show();
	    return answer[0];
	}
	
	/*
	  * 判断是否为整数 
	  * @param str 传入的字符串 
	  * @return 是整数返回true,否则返回false 
	*/
	public static boolean isNumeric(String str) {  
	    Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");  
	    return pattern.matcher(str).matches();
//	    try{
//	    	Integer.parseInt(str);
//	     }catch(NumberFormatException e)
//	     {
//	    	 return false;
//	     }
//	    return true;
	}
	
	/**
	 * 根据值, 设置spinner默认选中:
	 * @param spinner
	 * @param value
	 */
	public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
		SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
	    int k= apsAdapter.getCount();
		for(int i=0;i<k;i++){
			if(value.equals(apsAdapter.getItem(i).toString())){
			
				spinner.setSelection(i,true);// 默认选中项
				return;
			}
		}
		//都没找到就设空白
		spinner.setSelection(0,true);// 默认选中项
	}
	
	public static class SpinnerData {

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

		public void setValue(String sValue)
		{
			value = sValue; 
		}
		public void setText(String sText)
		{
			value = sText; 
		}
		public String getValue() {
			return value;
		}

		public String getText() {
			return text;
		}
	}
	

}
