package com.example.hanbell_wip.Class;

import java.io.Serializable;

 /**
  * People实体类，含有 name,sex,age三个属性  ，并实现类的封装
  */
 public class CompInformation implements Serializable {
	 public  String MaterialId;
	 public  String MaterialMame;
	 public  String SEQ;
	 public  String PRODUCTSERIALNUMBER;
     public  String MaterialType;
     public  String CHECKFLAG;
     public  String ISCOMPEXIST;
     public  String RAWPROCESSID;
     public  String FINEPROCESSID;
     public  String SUPPLYID;
     public  String FURNACENO;
     public  String IS_INSERT;
     public  String TRACETYPE;
     public  String  LOTID;
     public  String ISCOMPREPEATED;
     
     public String getISCOMPREPEATED() {
         return ISCOMPREPEATED;
     }
     public String getTRACETYPE() {
         return TRACETYPE;
     }
     public String getLOTID() {
         return LOTID;
     }
     public String getMaterialId() {
         return MaterialId;
     }
     public String getMaterialMame() {
         return MaterialMame;
     }
     public String getSEQ() {
         return SEQ;
     }
     public String getPRODUCTSERIALNUMBER() {
         return PRODUCTSERIALNUMBER;
     }
     
     public String getMaterialType() {
         return MaterialType;
     }    
     public String getCHECKFLAG() {
         return CHECKFLAG;
     }
     public String getISCOMPEXIST()
     {
         return ISCOMPEXIST;
     }
     public String getFINEPROCESSID() 
     {
         return FINEPROCESSID;
     }
     public String getRAWPROCESSID()
     {
         return RAWPROCESSID;
     }
     public String getSUPPLYID() 
     {
         return SUPPLYID;
     }     
     public String getFURNACENO() 
     {
         return FURNACENO;
     }    
     public String getIS_INSERT() 
     {
         return IS_INSERT;
     }
     
     public void setISCOMPREPEATED(String ISCOMPREPEATED) {
         this.ISCOMPREPEATED = ISCOMPREPEATED;
     }
     public void setTRACETYPE(String TRACETYPE) {
         this.TRACETYPE = TRACETYPE;
     }
     public void setLOTID(String LOTID) {
         this.LOTID = LOTID;
     }
     
     public void setMaterialId(String MaterialId) {
         this.MaterialId = MaterialId;
     }

     public void setMaterialMame(String MaterialMame) {
         this.MaterialMame = MaterialMame;
     }

     public void setSEQ(String SEQ) {
         this.SEQ = SEQ;
     }
     public void setPRODUCTSERIALNUMBER(String PRODUCTSERIALNUMBER) {
         this.PRODUCTSERIALNUMBER = PRODUCTSERIALNUMBER;
     }

     public void setMaterialType(String MaterialType) {
         this.MaterialType = MaterialType;
     }

     public void setCHECKFLAG(String CHECKFLAG) {
         this.CHECKFLAG = CHECKFLAG;
     }
     public void setISCOMPEXIST(String ISCOMPEXIST) {
         this.ISCOMPEXIST = ISCOMPEXIST;
     }

     public void setRAWPROCESSID(String RAWPROCESSID) {
         this.RAWPROCESSID = RAWPROCESSID;
     }

     public void setFINEPROCESSID(String FINEPROCESSID) {
         this.FINEPROCESSID = FINEPROCESSID;
     }
     public void setSUPPLYID(String SUPPLYID) {
         this.SUPPLYID = SUPPLYID;
     }

     public void setFURNACENO(String FURNACENO) {
         this.FURNACENO = FURNACENO;
     }

     public void setIS_INSERT(String IS_INSERT) {
         this.IS_INSERT = IS_INSERT;
     }
 }
