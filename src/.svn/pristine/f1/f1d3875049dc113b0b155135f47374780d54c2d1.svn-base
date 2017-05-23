package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料--->组织机构---->客户列表
 * @author lijun
 *
 */
public class CustomerListDS extends DataSource {
      private static CustomerListDS Install;
      
      public CustomerListDS(String id,String tableName){
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

	public static CustomerListDS getInstall(String id,String tableName) {
		if(Install == null){
			Install = new CustomerListDS(id,tableName);
		}
		
		return Install;
	}
	
	public static CustomerListDS getInstall(String id){
		if(Install == null){
			Install = new CustomerListDS(id,id);
		}
		return Install;
		
	}
      
      
}
