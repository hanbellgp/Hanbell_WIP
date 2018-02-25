package com.example.hanbell_wip.Class;

import android.app.AlertDialog;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.ArrayList;
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

public class MESSTK {
	MESDB db = new MESDB();

    public static String Erp_HashmapToXML(List<HashMap<String, String>> ls){
		String sXML = "";
		try {
			DocumentBuilder builder = null;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			
			Element root = document.createElement("DataList");
			for (int i=0;i<ls.size();i++)
			{
				HashMap<String, String> map = ls.get(i);
				Element newNode = document.createElement("data");
				Element facno = document.createElement("facno");
				Element prono = document.createElement("prono");
				Element vdrno = document.createElement("vdrno");
				Element userid = document.createElement("userid");
				Element username = document.createElement("username");
				Element stno = document.createElement("stno");
				Element stnotype = document.createElement("stnotype");
				Element pono = document.createElement("pono");
				Element itnbr = document.createElement("itnbr");
				Element trseq = document.createElement("trseq");
				Element ponotrseq = document.createElement("ponotrseq");
				Element schseq = document.createElement("schseq");
				Element trqty = document.createElement("trqty");
				Element accqty = document.createElement("accqty");
				Element wareh = document.createElement("wareh");
				facno.setTextContent(map.get("FACNO").toString().trim()); newNode.appendChild(facno);
				prono.setTextContent(map.get("PRONO").toString().trim()); newNode.appendChild(prono);
				vdrno.setTextContent(map.get("SUPPLYID").toString().trim()); newNode.appendChild(vdrno);
				userid.setTextContent(MESCommon.UserId); newNode.appendChild(userid);
				username.setTextContent(MESCommon.UserName); newNode.appendChild(username);
				stno.setTextContent(map.get("STNO").toString().trim()); newNode.appendChild(stno);
				stnotype.setTextContent(map.get("STNOTYPE").toString().trim()); newNode.appendChild(stnotype);
				pono.setTextContent(map.get("ORDERID").toString().trim()); newNode.appendChild(pono);
				itnbr.setTextContent(map.get("ITNBR").toString().trim()); newNode.appendChild(itnbr);
				trseq.setTextContent(map.get("STNOSEQ").toString().trim()); newNode.appendChild(trseq);
				ponotrseq.setTextContent(map.get("PONOTRSEQ").toString().trim()); newNode.appendChild(ponotrseq);
				schseq.setTextContent(map.get("SCHSEQ").toString().trim()); newNode.appendChild(schseq);
				trqty.setTextContent(map.get("SDQY1").toString().trim()); newNode.appendChild(trqty);
				accqty.setTextContent(map.get("ACCQY1").toString().trim()); newNode.appendChild(accqty);
				wareh.setTextContent(map.get("WAREH").toString().trim()); newNode.appendChild(wareh);
				
				root.appendChild(newNode);
			}
			document.appendChild(root);
			
			TransformerFactory  tf  =  TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			//t.setOutputProperty(\"encoding\",\"GB23121\"); //����������⣬�Թ���GBK����
			ByteArrayOutputStream  bos  =  new  ByteArrayOutputStream();
			t.transform(new DOMSource(document), new StreamResult(bos));
			sXML = bos.toString();
			
//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			Source source = new DOMSource(document);
//			File file = new File("persons.xml");
//			Result result = new StreamResult(file);
//			transformer.transform(source,result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sXML;
	}
    
    public static String Mes_HashmapToXML(List<HashMap<String, String>> ls){
		String sXML = "";
		try {
			DocumentBuilder builder = null;
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.newDocument();
			
			Element root = document.createElement("DataList");
			for (int i=0;i<ls.size();i++)
			{
				HashMap<String, String> map = ls.get(i);
				Element newNode = document.createElement("data");
				Element stno = document.createElement("stno");
				Element itnbr = document.createElement("itnbr");
				Element lotid = document.createElement("lotid");
				Element requestqty = document.createElement("RequestQty");
				Element actualqty = document.createElement("ActualQty");
				Element tracetype = document.createElement("TraceType");
				stno.setTextContent(map.get("STNO").toString().trim()); newNode.appendChild(stno);
				itnbr.setTextContent(map.get("ITNBR").toString().trim()); newNode.appendChild(itnbr);
				lotid.setTextContent(map.get("TLOTID").toString().trim()); newNode.appendChild(lotid);
				requestqty.setTextContent(map.get("REQUESTQTY").toString().trim()); newNode.appendChild(requestqty);
				actualqty.setTextContent(map.get("ACTUALQTY").toString().trim()); newNode.appendChild(actualqty);
				tracetype.setTextContent(map.get("TRACETYPE").toString().trim()); newNode.appendChild(tracetype);
				
				root.appendChild(newNode);
			}
			document.appendChild(root);
			
			TransformerFactory  tf  =  TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			//t.setOutputProperty(\"encoding\",\"GB23121\"); //����������⣬�Թ���GBK����
			ByteArrayOutputStream  bos  =  new  ByteArrayOutputStream();
			t.transform(new DOMSource(document), new StreamResult(bos));
			sXML = bos.toString();
			
//			Transformer transformer = TransformerFactory.newInstance().newTransformer();
//			Source source = new DOMSource(document);
//			File file = new File("persons.xml");
//			Result result = new StreamResult(file);
//			transformer.transform(source,result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sXML;
	}
    
    
    //���ɨ�����������Ƿ�Ϸ�
    //1.STKINSTOCK_PALT������ֵ
    //2.STKINSTOCK���������
    public String CheckScanId_InStock(String sScanId, List<HashMap<String, String>> lsData, List<HashMap<String, String>> lsMain, List<HashMap<String, String>> lsDetail)
    {
    	String sSql = "";
    	String sResult = "";
    	List<HashMap<String, String>> ls = new ArrayList<HashMap<String, String>>();
    	boolean bFindItnbr = false;
    	boolean bFindCompid = false;
    	try {
    		if (lsData.size() == 0) return "���������޿��������!";
    		
    		String sWhere = "";
//    		if (lsData.size() == 1)
//    		{
//    			//�򽻻������󶨹������ʲ����ֱ������
//    			return "";
//    			//���ShipType, AcceptNo�ĺϷ���
////    			String sItnbr = lsData.get(0).get("ITNBR").toString();
////    			String sShipType = lsData.get(0).get("SHIPTYPE").toString();
////    			//String sAcceptNo = lsData.get(0).get("ACCEPTNO").toString();
////    			String sCompid = lsData.get(0).get("COMPID").toString();
////    			if (sCompid.equals("")) return "���롸" + sScanId + "���ǹ���׷�����ϡ�" + sItnbr + "��!";
////    			if (sShipType.indexOf("ASRS") < 0) return "���롸" + sCompid + "�������Զ��ֿ��" + sShipType + "��!";
////    			if (!sShipType.equals(sWareh)) return "���롸" + sCompid + "��֮���" + sShipType + "�������!";
////    			sResult = CheckErpDataByCompInfo(sCompid, sItnbr, sShipType, sAcceptNo, lsErpData);
////				if (!sResult.equals("")) return sResult;
//    		}
    		for (int i =0; i< lsData.size(); i++)
			{
				String sCompid = lsData.get(i).get("COMPID").toString();
				String sCompidseq = lsData.get(i).get("COMPIDSEQ").toString();
				//ֻ��Ҫ���Compid
				if (!sCompid.equals(""))
				{
					if (sWhere.equals(""))
						sWhere = " COMPIDSEQ='" + sCompidseq + "' ";
					else
						sWhere += " or COMPIDSEQ='" + sCompidseq + "' ";
					
					bFindItnbr = true;
				}
				else
					lsData.get(i).put("IsConfirm", "N");
			}
    		if (!bFindItnbr) return "���롸" + sScanId + "�����޿��������!";
    		
    		if (sWhere.equals("")) return "";	//���޹�����������
    		//1.STKINSTOCK_PALT������ֵ
    		ls.clear();
			sSql = "SELECT DISTINCT COMPID, COMPIDSEQ FROM STKINSTOCK_PALT WHERE 1=1 and STATUS='N' and (" + sWhere + ")";
			sResult = db.GetData(sSql, ls);
			if (!sResult.equals("")) return sResult;
			String sCompids = "";
			for (int i =0; i< ls.size(); i++)
			{
				String sCompid = ls.get(i).get("COMPID").toString();
				if (sCompids.equals(""))
					sCompids = "���¹�������������ջ���¼�������ظ�������" + "\n" + sCompid;
				else
					sCompids = sCompids + "\n" + sCompid;
				if (lsData.size() == 1 && ls.size() != 0)
					return "���롸" + sCompid + "���������ջ���¼�������ظ�����!";
				else
				{
					for (int j =0; j< ls.size(); j++)
					{
						if (ls.get(j).get("COMPID").toString().equals(sCompid))
						{
							lsData.get(i).put("IsConfirm", "N");
							break;
						}
					}
				}
			}
			if (!sCompids.equals("")) return sCompids;
			
			//2.STKINSTOCK���������
			ls.clear();
			sSql = "SELECT DISTINCT COMPID, COMPIDSEQ FROM STKINSTOCK WHERE 1=1 and (" + sWhere + ")";
			sResult = db.GetData(sSql, ls);
			if (!sResult.equals("")) return sResult;
			for (int i =0; i< ls.size(); i++)
			{
				String sCompid = ls.get(i).get("COMPID").toString();
				if (sCompids.equals(""))
					sCompids = "���¹����������⣬�����ظ���⣺" + "\n" + sCompid;
				else
					sCompids = sCompids + "\n" + sCompid;
				if (lsData.size() == 1 && ls.size() != 0)
					return "���롸" + sCompid + "������⣬�����ظ����!";
				else
				{
					for (int j =0; j< ls.size(); j++)
					{
						if (ls.get(j).get("COMPID").toString().equals(sCompid))
						{
							lsData.get(i).put("IsConfirm", "N");
							break;
						}
					}
				}
			}
			if (!sCompids.equals("")) return sCompids;
			
			//����Ƿ��п��������
//			bFindCompid = false;
//			for (int i =0; i< lsData.size(); i++)
//			{
//				String sIsConfirm = lsData.get(i).get("IsConfirm").toString();
//				if (sIsConfirm.equals("Y"))
//				{
//					bFindCompid = true;
//					return "";
//				}
//			}
//			if (!bFindCompid) return "���������޿��������!";
			
			
		} catch (Exception e) {
			return e.toString();
		}
    	return sResult;
    }
    
    public String CheckErpDataByCompInfo(String sCompid, String sItnbr, String sShipType, String sAcceptNo, List<HashMap<String, String>> lsErpData)
    {
    	String sSql = "";
    	String sResult = "";
    	boolean bFindItnbr = false;
    	try {
    		//���ShipType, AcceptNo�ĺϷ���
			for (int i =0; i< lsErpData.size(); i++)
			{
				if (!lsErpData.get(i).get("doc_no").equals(sAcceptNo) && !sAcceptNo.equals(""))
					return "���롸" + sCompid + "��֮���ݱ�š�" + sAcceptNo + "��������!";
				else if (lsErpData.get(i).get("item_no").equals(sItnbr))
				{
					if (!lsErpData.get(i).get("wareh").equals(sShipType) && !sShipType.equals(""))
						return "���롸" + sCompid + "��֮���" + sShipType + "��������!";
					bFindItnbr = true;
					break;
				}
			}
			if (!bFindItnbr) return "���롸" + sCompid + "��֮Ʒ�š�" + sItnbr + "���ڵ��ݱ�š�" + lsErpData.get(0).get("doc_no").toString() + "���Ҳ���, ��ȷ�����Ͽ���Ƿ����!";
		} catch (Exception e) {
			return e.toString();
		}
    	return sResult;
    }


    public String GetStkFuncid(String sStkType, String sShipType, List<HashMap<String, String>> ls)
    {
    	String sSql = "";
    	String sResult = "";
    	try {
    		if (sStkType.equals("puracd"))
    		{
    			sSql = "select distinct acceptno stkfuncid, 'N' CheckFlag from puracd where okqy1 > asrs_qty and accsta = 'Y' AND wareh ='" + sShipType + "' and asrs_sta=0";
    		}
    		sResult = db.ErpGetData(sSql, ls);
			if (!sResult.equals("")) return sResult;
			
		} catch (Exception e) {
			return e.toString();
		}
    	return sResult;
    }
    
    public String GetErpData(String sStkType, String sShipType, String sStkFuncid, List<HashMap<String, String>> ls)
    {
    	String sSql = "";
    	String sResult = "";
    	try {
    		if (sStkType.equals("puracd"))
    		{
    			sSql = "select (okqy1-asrs_qty) currentqty, acceptno doc_no,trseq,itnbr item_no,(okqy1-asrs_qty) trn_qty, " +
                        "fixnr,varnr,vdrno ownship,wareh " +
                        " FROM puracd " +
                        " where  okqy1 > asrs_qty and accsta = 'Y' and wareh='" + sShipType + "' and acceptno='" + sStkFuncid + "' " +
                        " order by acceptno, trseq";
    		}
    		sResult = db.ErpGetData(sSql, ls);
			if (!sResult.equals("")) return sResult;
			
		} catch (Exception e) {
			return e.toString();
		}
    	return sResult;
    }




}
