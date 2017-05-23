package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SupplierListDS extends DataSource {
    private static SupplierListDS Install;
    
    
    public SupplierListDS(String id,String tableName){
  	  this.setID(id);
  	  this.setDataFormat(DSDataFormat.JSON);
  	  //this.setTableName(tableName);
  	  setAttribute("tableName", tableName, false);
  	  
  	  //id
  	  DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
  	  keyField.setPrimaryKey(true);
  	  keyField.setHidden(true);
  	  
  	  
  	  setFields(keyField);
  	  
  	  setDataURL("basQueryServlet?ds_id="+getID());
  	  setClientOnly(false);
  	  setShowPrompt(false);
    }
    
    public static SupplierListDS getInstall(String id,String tableName) {
		if(Install == null){
			Install = new SupplierListDS(id,tableName);
		}
		
		return Install;
	}
	
	public static SupplierListDS getInstall(String id){
		if(Install == null){
			Install = new SupplierListDS(id,id);
		}
		return Install;
		
	}
      
}
