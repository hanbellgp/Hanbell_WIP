package com.example.hanbell_wip.Class;

import java.util.HashMap;  
import java.util.Map;  
  
import android.content.Context;  
import android.content.SharedPreferences;  
import android.content.SharedPreferences.Editor;  
  
public class PrefercesService {  
    private Context context;  
    public PrefercesService(Context context) {  
        super();  
        this.context = context;  
    }  
  /** 
   * 保存参数  
   * @param EQPName 设备名称
   * @param EQPID  设备ID
   * @param StepName 工序名称
   * @param StepID  工序ID
   */  
    public void save(String EQPName, String EQPID,String StepName, String StepID,String ProductTypeName, String ProductType) {  
        //第一个参数 指定名称 不需要写后缀名 第二个参数文件的操作模式   
        SharedPreferences preferences=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);  
        //取到编辑器   
        Editor editor=preferences.edit();  
        editor.putString("EQPName", EQPName);  
        editor.putString("EQPID", EQPID);  
        editor.putString("StepName", StepName);  
        editor.putString("StepID", StepID);  
        editor.putString("ProductTypeName", ProductTypeName);  
        editor.putString("ProductType", ProductType);  
        editor.putString("USER", EQPName);  
        editor.putString("PAW", EQPID);  
        //把数据提交给文件中   
        editor.commit();  
    }  
    
    public void save2(String USER, String PAW) {  
        //第一个参数 指定名称 不需要写后缀名 第二个参数文件的操作模式   
        SharedPreferences preferences=context.getSharedPreferences("itcast2", Context.MODE_PRIVATE);  
        //取到编辑器   
        Editor editor=preferences.edit();  
        editor.putString("USER", USER);  
        editor.putString("PAW", PAW);  
        //把数据提交给文件中   
        editor.commit();  
    }  
    /** 
     * 获取各项配置参数 
     * @return 
     */  
   public Map<String,String> getPreferences(){  
     SharedPreferences pre=context.getSharedPreferences("itcast", Context.MODE_PRIVATE);  
     //如果得到的name没有值则设置为空 pre.getString("name", "");   
     Map<String,String> params=new HashMap<String,String>();  
     params.put("EQPName", pre.getString("EQPName", ""));  
     params.put("EQPID", pre.getString("EQPID", ""));  
     params.put("StepName", pre.getString("StepName", ""));  
     params.put("StepID", pre.getString("StepID", "")); 
     params.put("ProductTypeName", pre.getString("ProductTypeName", ""));  
     params.put("ProductType", pre.getString("ProductType", "")); 
     params.put("USER", pre.getString("USER", ""));  
     params.put("PAW", pre.getString("PAW", "")); 
       return params;  
         
         
   }  
   
   /** 
    * 获取各项配置参数 
    * @return 
    */  
  public Map<String,String> getPreferences2(){  
    SharedPreferences pre=context.getSharedPreferences("itcast2", Context.MODE_PRIVATE);  
    //如果得到的name没有值则设置为空 pre.getString("name", "");   
    Map<String,String> params=new HashMap<String,String>();  
   
    params.put("USER", pre.getString("USER", ""));  
    params.put("PAW", pre.getString("PAW", "")); 
      return params;  
        
        
  }  
}  
