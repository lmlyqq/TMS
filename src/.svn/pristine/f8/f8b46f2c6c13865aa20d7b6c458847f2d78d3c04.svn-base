package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 人员管理
 * @author lijun
 *
 */
public class BasStaff1DS extends DataSource {
    private static BasStaff1DS install;
    
    public BasStaff1DS(String id,String tableName){
    	this.setID(id);
    	//this.setTableName(tableName);
    	setAttribute("tableName", tableName, false);
    	this.setDataFormat(DSDataFormat.JSON);
    	
    	DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
    	keyField.setPrimaryKey(true);
    	keyField.setRequired(true);
    	keyField.setHidden(true);
    	
    	this.setFields(keyField);
    	this.setDataURL("basQueryServlet?ds_id="+getID());
    	this.setShowPrompt(false);
    	this.setClientOnly(false);
    	
    	
    }
    
    public static BasStaff1DS getInstall(String id,String tableName){
    	if(install == null){
    		install = new BasStaff1DS(id,tableName);
    	}
    	
    	return install;
    }
    public static BasStaff1DS getInstall(String id){
    	if(install == null){
    		install = new BasStaff1DS(id,id);
    	}
    	
    	return install;
    }
	
	
}
