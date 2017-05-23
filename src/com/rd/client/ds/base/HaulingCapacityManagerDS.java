package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class HaulingCapacityManagerDS extends DataSource {
	private static HaulingCapacityManagerDS instanll = null;
	
   public HaulingCapacityManagerDS(){
	   
   }
   
   public HaulingCapacityManagerDS(String id,String tableName){
	  this.setID(id);
	  this.setDataFormat(DSDataFormat.JSON);
	  //this.setTableName(tableName);
	  setAttribute("tableName", tableName, false);
	  DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
	  keyField.setPrimaryKey(true);
	  keyField.setRequired(true);
	  keyField .setHidden(true);
	  
	  
	  DataSourceTextField PLATE_NO = new DataSourceTextField("PLATE_NO",Util.TI18N.PLATE_NO());
	  DataSourceTextField VEHICLE_NO = new DataSourceTextField("VEHICLE_NO",Util.TI18N.VEHICLE_NO());
	  this.setFields(keyField,PLATE_NO,VEHICLE_NO);
	  this.setDataURL("basQueryServlet?is_curr_page=true&OP_FLAG=M&ds_id="+getID());
	  this.setClientOnly(false);
	  this.setShowPrompt(false);
	  
   }
   
   public static HaulingCapacityManagerDS getInstall(String id){
	   if(instanll == null){
		   instanll = new HaulingCapacityManagerDS(id,id);
	   }
	   return instanll;
   }
   
   public static HaulingCapacityManagerDS getInstall(String id,String tableName){
	   if(instanll == null){
		   instanll = new HaulingCapacityManagerDS(id,tableName);
	   }
	   return instanll;
   }
}
