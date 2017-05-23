package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class HaulingCapacityManagerDsDB extends DataSource {
	private static HaulingCapacityManagerDsDB instanll = null;
	
   public HaulingCapacityManagerDsDB(){
	   
   }
   
   public HaulingCapacityManagerDsDB(String id,String tableName){
	  this.setID(id);
	  this.setDataFormat(DSDataFormat.JSON);
	  //this.setTableName(tableName);
	  setAttribute("tableName", tableName, false);
	  DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
	  keyField.setPrimaryKey(true);
	  keyField.setRequired(true);
	  keyField .setHidden(true);
	  
	  this.setFields(keyField);
	  
	  this.setDataURL("basQueryServlet?is_curr_page=true&OP_FLAG=M&ds_id="+getID());
	  this.setClientOnly(false);
	  this.setShowPrompt(false);
	  
   }
   
   public static HaulingCapacityManagerDsDB getInstall(String id){
	   if(instanll == null){
		   instanll = new HaulingCapacityManagerDsDB(id,id);
	   }
	   return instanll;
   }
   
   public static HaulingCapacityManagerDsDB getInstall(String id,String tableName){
	   if(instanll == null){
		   instanll = new HaulingCapacityManagerDsDB(id,tableName);
	   }
	   return instanll;
   }
}
