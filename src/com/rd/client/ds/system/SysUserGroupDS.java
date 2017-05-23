package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

//用户组管理->用户组数据源

public class SysUserGroupDS extends DataSource{

	    private static SysUserGroupDS instance = null;

	    public static SysUserGroupDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new SysUserGroupDS(id, tableName);
	        }
	        return instance;
	    }
	    
	    public static SysUserGroupDS getInstance(String id){
	    	if(instance == null){
	    		instance = new SysUserGroupDS(id,id);
	    	}
	    	return instance;
	    }
	    public SysUserGroupDS(String id, String tableName) {

	        setID(id);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName(tableName);
	        setAttribute("tableName", tableName, false);
	        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	        keyField.setPrimaryKey(true);
	        keyField.setHidden(true);
	        
	        DataSourceTextField GRP_CODE = new DataSourceTextField("GRP_CODE",Util.TI18N.UGROUP_CODE());
	        DataSourceTextField GRP_NAME = new DataSourceTextField("GRP_NAME",Util.TI18N.UGROUP_NAME_C());
	        DataSourceTextField ACTIVE_FLAG = new DataSourceTextField("ACTIVE_FLAG",Util.TI18N.ENABLE_FLAG());
	        DataSourceTextField ORG_ID = new DataSourceTextField("ORG_ID",Util.TI18N.UGROUP_NAME_C());
	        setFields(keyField,GRP_CODE,GRP_NAME,ACTIVE_FLAG,ORG_ID);
  
//	        setFields(keyField);
	        setDataURL("sysQueryServlet?OP_FLAG=M&&ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
	    
}
	
