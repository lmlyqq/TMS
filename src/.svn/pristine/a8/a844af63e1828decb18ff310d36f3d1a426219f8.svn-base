package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ChangeWMSDS extends DataSource {
    private static ChangeWMSDS install;
    
    public ChangeWMSDS(String id,String tableName){
    	this.setID(id);
    	this.setDataFormat(DSDataFormat.JSON);
    	//this.setTableName(tableName);
    	setAttribute("tableName", tableName, false);
    	
    	DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
    	keyField.setPrimaryKey(true);
    	keyField.setRequired(true);
    	keyField.setHidden(true);
    	
    	setFields(keyField);
    	             
    	setDataURL("initDataServlet?ds_id="+getID());
    	setClientOnly(false);
    	setShowPrompt(false);
    	
    }
    public static ChangeWMSDS getInstall(String id){
    	if(install == null){
    		install = new ChangeWMSDS(id,id);
    	}
    	return install;
    }
}
