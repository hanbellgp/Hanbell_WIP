package com.example.hanbell_wip.Class;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.R.string;

public class MESDB {
	private static final String serviceNameSpace = "http://tempuri.org/";
	public static String serviceURL = "";
	public MESDB(){
		serviceURL = MESCommon.msDefaltURL;
	}
	public MESDB(String sURL) {
		if (sURL != null) {
			if (sURL.trim() != "") {
				serviceURL = sURL;
			}
		}
	}

	public static String GetData(String sSql, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetData");
			// 2.设置调用参数
			rpc.addProperty("sSQL", sSql);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetData", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		
		return sError;
	}

	public static String InsertSqlLog(String sName,String sSql)
	{
		String sResult = "";
		String sSqlString=" INSERT INTO PDALOG (FUNCTIONNAME, FUNCTIONSQL,TIMES) VALUES ('"+sName+"','"+sSql.replace("\'","").toString()+"',convert(varchar,getdate(),111) || ' ' || convert(varchar,getdate(),108) ) ";
		ExecuteSQL(sSqlString);
		return sResult;
		
	}

	
	public static String ExecuteSQL(String sSql) {

		String sResult = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "ExecuteSQL");
			// 2.设置调用参数
			rpc.addProperty("sSQL", sSql);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "ExecuteSQL", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sResult = obj.toString();
				if (sResult.equals("anyType{}")) sResult = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sResult= e.toString();
		}
		return sResult;
	}
	
	public static String Encrypt(String sToEncrypt) {
		String sResult = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "Encrypt");
			// 2.设置调用参数
			rpc.addProperty("sToEncrypt", sToEncrypt);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "Encrypt", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sResult = obj.toString();
				//if (sResult.equals("anyType{}")) sResult = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sResult= e.toString();
		}
		return sResult;
	}

	public static String GetERP_ASSEMBLESPECIFICATION(String sProductID,String sProductCompID, String sOrderID,String sMName,String sBName,String sStepID, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
	    String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetERP_ASSEMBLESPECIFICATION");
			// 2.设置调用参数
			rpc.addProperty("sProductID", sProductID);
			rpc.addProperty("sProductCompID", sProductCompID);
			rpc.addProperty("sOrderID", sOrderID);
			rpc.addProperty("sMName", sMName);
			rpc.addProperty("sBName", sBName);
			rpc.addProperty("sStepID", sStepID);

			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetERP_ASSEMBLESPECIFICATION", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String GetProductSerialNumber(String sBarCode,String sITNBR,String sPRODUCTORDERID ,String sSTATUS,String sProductType, String sType, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		  String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetDatatableProductSerialNumber");
			// 2.设置调用参数
			rpc.addProperty("sBarCode", sBarCode);
			rpc.addProperty("sITNBR", sITNBR);
			rpc.addProperty("sPRODUCTORDERID", sPRODUCTORDERID);
			rpc.addProperty("sSTATUS", sSTATUS);
			rpc.addProperty("sSUPPLYLOTID", "");
			rpc.addProperty("sProductType", sProductType);
			rpc.addProperty("sType", sType);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetDatatableProductSerialNumber", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
		
	}
	
	public static String GetProductSerialNumber_PDA(String sBarCode,String sITNBR,String sPRODUCTORDERID ,String sSTATUS,String sProductType, String sType, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		  String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetDatatableSerialNumber");
			// 2.设置调用参数
			rpc.addProperty("sBarCode", sBarCode);
			rpc.addProperty("sITNBR", sITNBR);
			rpc.addProperty("sPRODUCTORDERID", sPRODUCTORDERID);
			rpc.addProperty("sSTATUS", sSTATUS);
			rpc.addProperty("sSUPPLYLOTID", "");
			rpc.addProperty("sProductType", sProductType);
			rpc.addProperty("sType", sType);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetDatatableSerialNumber", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}  
	
	public static String GetProductProcess(String sOrderid,String sProductCompId, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetProcess");
			// 2.设置调用参数
			rpc.addProperty("sOrderid", sOrderid);
			rpc.addProperty("sProductCompId", sProductCompId);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetProcess", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	public static String GetAnalysisData(String sProductOrderId, String sProductId, String sProductCompId, String sProductSerialNumber, String sStepId, String sUserId, String sEqpId,String sQCItem, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetAnalysisData");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductId", sProductId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sProductSerialNumber", sProductSerialNumber);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sQCItem", sQCItem);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetAnalysisData", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String GetAnalysisDatabyProductSerialNumber(String sProductOrderId, String sProductId, String sProductSerialNumber, String sStepId, String sUserId, String sEqpId,String sQCItem, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetAnalysisDatabyProductSerialNumber");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductId", sProductId);
			rpc.addProperty("sProductSerialNumber", sProductSerialNumber);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sQCItem", sQCItem);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetAnalysisDatabyProductSerialNumber", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	
	public static String GetAnalysisData_Out(String sProductOrderId, String sProductId, String sProductCompId, String sProductSerialNumber, String sStepId, String sUserId, String sEqpId,String sProductType, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetAnalysisData_Out");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductId", sProductId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sProductSerialNumber", sProductSerialNumber);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sProductType", sProductType);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetAnalysisData_Out", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String FinalSaveData(String sAnalysisId, String sSampleTimes, String sResult, String sCheckUserId, String sCheckUserName, String sTreatment) {
		String sError = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_FinalSaveData");
			// 2.设置调用参数
			rpc.addProperty("sAnalysisId", sAnalysisId);
			rpc.addProperty("sSampleTimes", sSampleTimes);
			rpc.addProperty("sResult", sResult);
			rpc.addProperty("sCheckUserId", sCheckUserId);
			rpc.addProperty("sCheckUserName", sCheckUserName);
			rpc.addProperty("sTreatment", sTreatment);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_FinalSaveData", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String SetStepInbyJWJ(String sProductOrderId ,String sProductCompId, String sProductSerialNumber ,String sStepId, String sStepSEQ, String sEqpId, String sRawPocessId, String sFinePocessId, String sRawWheel, String sFineWheel, String sUserId, String sUserName, int iQty, String sSampleIndex) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_SetStepIn");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sProductSerialNumber", sProductSerialNumber);			
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sStepSEQ", sStepSEQ);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sRawPocessId", sRawPocessId);
			rpc.addProperty("sFinePocessId", sFinePocessId);
			rpc.addProperty("sRawWheel", sRawWheel);
			rpc.addProperty("sFineWheel", sFineWheel);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
			rpc.addProperty("iQty", iQty);
			rpc.addProperty("sSampleIndex", sSampleIndex);
						
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_SetStepIn", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String SetStepOutbyJWJ(String sProductOrderId ,String sProductCompId, String sStepId, String sStepSEQ, String sEqpId, String sRawPocessId, String sFinePocessId, String sRawWheel, String sFineWheel, String sUserId, String sUserName, int iTrackOutQty, int iScrapQty, int iReworkQty, int iOutReworkQty,String sOK, boolean IsOnlyStepOut) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_SetStepOut");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sStepSEQ", sStepSEQ);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sRawPocessId", sRawPocessId);
			rpc.addProperty("sFinePocessId", sFinePocessId);
			rpc.addProperty("sRawWheel", sRawWheel);
			rpc.addProperty("sFineWheel", sFineWheel);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
			rpc.addProperty("iTrackOutQty", iTrackOutQty);
			rpc.addProperty("iScrapQty", iScrapQty);
			rpc.addProperty("iReworkQty", iReworkQty);
			rpc.addProperty("iOutReworkQty", iOutReworkQty);
			rpc.addProperty("sOK", sOK);
			rpc.addProperty("IsOnlyStepOut", IsOnlyStepOut);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_SetStepOut", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String CheckMaterial(String sMainTypelist, String sStepID, String sProductID, String sProductCompID) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_CheckMaterial");
			// 2.设置调用参数
			rpc.addProperty("sMainTypelist", sMainTypelist);
			rpc.addProperty("sStepID", sStepID);
			rpc.addProperty("sProductID", sProductID);
			rpc.addProperty("sProductCompID", sProductCompID);	
			
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_CheckMaterial", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	public static String ERPInStorage(String sProductOrderId, String sProductCompId, String sAppUser, String sWrcode, String sWareh) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_ERPInstorage");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sWrcode", sWrcode);	
			rpc.addProperty("sAppUser", sAppUser);		
			rpc.addProperty("sWareh", sWareh);	
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_ERPInstorage", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	public static String ErpGetData(String sSql, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_Erp_GetData");
			// 2.设置调用参数
			rpc.addProperty("sSQL", sSql);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_Erp_GetData", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String BindReworkComp(String sProductOrderId,String sCompIds,String sUserID,String sUserName) {
		String sResult = "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_BindReworkComp");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sCompIds", sCompIds);
			rpc.addProperty("sUserID", sUserID);
			rpc.addProperty("sUserName", sUserName);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_BindReworkComp", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sResult = obj.toString();
				if (sResult.equals("anyType{}")) sResult = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sResult;
	}
	
	public static String BindCOMPTEMP(String sSeq,String sUserID,String sUserName,String sTIMES, List<HashMap<String, String>> ol) {
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_COMP_TEMP_ADD");
			// 2.设置调用参数
			rpc.addProperty("sSEQ", sSeq);
			rpc.addProperty("sUserID", sUserID);
			rpc.addProperty("sUserName", sUserName);
			rpc.addProperty("sTIMES", sTIMES);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_COMP_TEMP_ADD", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	//服务维修报工专用
	//取得MES单号或零部件资讯
	public static String GetProductSerialNumber_CRM(String sBarCode,String sITNBR,String sPRODUCTORDERID ,String sSTATUS,String sProductType, String sType, List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		  String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetDatatableProductSerialNumber_CRM");
			// 2.设置调用参数
			rpc.addProperty("sBarCode", sBarCode);
			rpc.addProperty("sITNBR", sITNBR);
			rpc.addProperty("sPRODUCTORDERID", sPRODUCTORDERID);
			rpc.addProperty("sSTATUS", sSTATUS);
			rpc.addProperty("sSUPPLYLOTID", "");
			rpc.addProperty("sProductType", sProductType);
			rpc.addProperty("sType", sType);
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetDatatableProductSerialNumber_CRM", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
		
	}
	
	//服务维修报工专用
	//检查MES单号的状态，并回传结果
	public static String GetProductProcess_CRM(String sCrmno,List<HashMap<String, String>> ol) {
		//List<HashMap<String, String>> ol = new ArrayList<HashMap<String, String>>();
		//ol = new ArrayList<HashMap<String, String>>();
		String sXML = "";
		String sError= "";
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_GetProcess_CRM");
			// 2.设置调用参数
			rpc.addProperty("sCrmno", sCrmno);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
		
			ht.call(serviceNameSpace + "PDA_GetProcess_CRM", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sXML = obj.toString().replace("\n", "");
				int iResult = sXML.indexOf("<DataList");
				if (sXML.indexOf("<DataList>") >= 0) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					// 创建一个新的字符串
					StringReader read = new StringReader(sXML);
					// 创建新的输入源 解析器将使用 InputSource 对象来确定如何读取 XML 输入
					InputSource source = new InputSource(read);
					Document doc = db.parse(source);
					Node nd = doc.getFirstChild();
					NodeList lst = nd.getChildNodes();
					for (int i = 0; i < lst.getLength(); i++) {
						Node nd0 = lst.item(i);
						if (nd0.getNodeName().trim() != "#text") {
							NodeList lst0 = nd0.getChildNodes();
							// List<String> sl = new ArrayList<String>();
							HashMap<String, String> sh = new HashMap<String, String>();
							for (int j = 0; j < lst0.getLength(); j++) {
								Node nd1 = lst0.item(j);
								if (nd1.getNodeName().trim() == "SHIPTYPE") {
									String s = nd1.getNodeName().trim();
								}
								if (nd1.getNodeName().trim() != "#text") {
									// sl.add(nd1.getTextContent());
									sh.put(nd1.getNodeName(), nd1.getTextContent());
								}
							}
							// ol.add(sl);
							ol.add(sh);
						}
					}
				}
				else if (iResult < 0)
				{
					return sXML;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	//服务维修报工TrackIn
	public static String SetStepInbyCRM(String sProductOrderId ,String sProductCompId,String sStepId, String sEqpId, String sUserId, String sUserName) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_SetStepInCRM");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);		
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
						
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_SetStepInCRM", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}

	//服务维修报工TrackIn
	public static String SetStepOutbyCRM(String sProductOrderId ,String sProductCompId, String sStepId, String sEqpId, String sUserId, String sUserName,String sOK) {
		String sError = "";
		
		try {
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, "PDA_SetStepOutCRM");
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
			rpc.addProperty("sOK", sOK);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + "PDA_SetStepOutCRM", envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	//报工暂停
	public static String SetStepPause(String sProductOrderId, String sProductCompId, String sStepId, String sStepSEQ, String sEqpId, String sUserId, String sUserName) {
		String sError = "";
		
		try {
			String sWebServiceName = "PDA_SetStepPause";
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, sWebServiceName);
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sStepSEQ", sStepSEQ);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + sWebServiceName, envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	//解除暂停
	public static String SetStepResume(String sProductOrderId, String sProductCompId, String sStepId, String sStepSEQ, String sEqpId, String sUserId, String sUserName) {
		String sError = "";
		
		try {
			String sWebServiceName = "PDA_SetStepResume";
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, sWebServiceName);
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sStepSEQ", sStepSEQ);
			rpc.addProperty("sEqpId", sEqpId);
			rpc.addProperty("sUserId", sUserId);
			rpc.addProperty("sUserName", sUserName);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + sWebServiceName, envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
		
	//取得报工暂停状态
	//True:报工暂停
	//False:否
	public static String IsStepPause(String sProductOrderId, String sProductCompId, String sStepId, String sEqpId) {
		String sError = "";
		
		try {
			String sWebServiceName = "PDA_IsStepPause";
			// 1.实例化SoapObject对象
			SoapObject rpc = new SoapObject(serviceNameSpace, sWebServiceName);
			// 2.设置调用参数
			rpc.addProperty("sProductOrderId", sProductOrderId);
			rpc.addProperty("sProductCompId", sProductCompId);
			rpc.addProperty("sStepId", sStepId);
			rpc.addProperty("sEqpId", sEqpId);
			
			// 3.设置Soap请求信息
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true;
			envelope.setOutputSoapObject(rpc);
			// 4.构建传输对象
			HttpTransportSE ht = new HttpTransportSE(serviceURL);
			// 5.调用Webservice
			ht.call(serviceNameSpace + sWebServiceName, envelope);
			// 6.返回结果
			Object obj = envelope.getResponse();
			if (obj != null) {
				sError = obj.toString();
				if (sError.equals("anyType{}")) sError = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			sError= e.toString();
		}
		return sError;
	}
	
	
		
		
	
}
