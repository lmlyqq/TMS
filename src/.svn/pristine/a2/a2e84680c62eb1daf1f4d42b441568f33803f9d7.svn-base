package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TmsSupplierDS extends DataSource {

	private static TmsSupplierDS Install;
	    
	public TmsSupplierDS(String id,String tableName){
		this.setID(id);
		this.setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
	  	  
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
	  	  
		setFields(keyField);
	  	  
		setDataURL("basQueryServlet?ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	    
	public static TmsSupplierDS getInstall(String id,String tableName) {
		if(Install == null){
			Install = new TmsSupplierDS(id,tableName);
		}
			
		return Install;
	}
		
	public static TmsSupplierDS getInstall(String id){
		if(Install == null){
			Install = new TmsSupplierDS(id,id);
		}
		return Install;
			
	}
	
}
