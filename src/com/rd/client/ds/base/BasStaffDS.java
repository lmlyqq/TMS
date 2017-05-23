package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 人员管理
 * @author lijun
 *
 */
public class BasStaffDS extends DataSource {
    private static BasStaffDS install;
    
    public BasStaffDS(String id,String tableName){
    	this.setID(id);
    	//this.setTableName(tableName);
    	setAttribute("tableName", tableName, false);
    	this.setDataFormat(DSDataFormat.JSON);
    	
    	DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
    	keyField.setPrimaryKey(true);
    	keyField.setRequired(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField STAFF_CODE = new DataSourceTextField("STAFF_CODE",Util.TI18N.STAFF_CODE());
    	DataSourceTextField STAFF_NAME = new DataSourceTextField("STAFF_NAME",Util.TI18N.STAFF_NAME());
    	DataSourceTextField STAFF_ENAME = new DataSourceTextField("STAFF_ENAME",Util.TI18N.STAFF_ENAME());
    	DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
    	
    	this.setFields(keyField,STAFF_CODE,STAFF_NAME,STAFF_ENAME,HINT_CODE);
    	this.setDataURL("basQueryServlet?ds_id="+getID());
    	this.setShowPrompt(false);
    	this.setClientOnly(false);
    	
    	
    }
    
    public static BasStaffDS getInstall(String id,String tableName){
    	if(install == null){
    		install = new BasStaffDS(id,tableName);
    	}
    	
    	return install;
    }
    public static BasStaffDS getInstall(String id){
    	if(install == null){
    		install = new BasStaffDS(id,id);
    	}
    	
    	return install;
    }
	
	
}
