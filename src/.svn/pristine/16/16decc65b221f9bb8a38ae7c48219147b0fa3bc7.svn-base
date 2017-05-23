package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统参数->在线用户
 * @author wangjun
 */


public class OnlineUserDS extends DataSource {
   
	private static OnlineUserDS instance=null;
	
	public static OnlineUserDS getInstance(String id){
		
        if(instance == null){
        	instance=new OnlineUserDS(id,id);
        }
        return instance;
		
	}
	
	public static OnlineUserDS getInstance(String id,String tableName){
		if(instance == null){
        	instance=new OnlineUserDS(id,tableName);
        }
        return instance;
		
	}
   
	public OnlineUserDS (String id,String tableName){
	    	
		setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);

		/*DataSourceTextField user_id =new DataSourceTextField("USER_ID",Util.TI18N.USER_ID());
		DataSourceTextField user_name =new DataSourceTextField("USER_NAME",Util.TI18N.USER_NAME()); 
		DataSourceTextField default_org_id_name=new DataSourceTextField("DEFAULT_ORG_ID_NAME",Util.TI18N.USER_ORG_ID()); 
		DataSourceTextField cur_status_name =new DataSourceTextField("CUR_STATUS_NAME",Util.TI18N.CUR_STATUS()); 
		DataSourceTextField tel=new DataSourceTextField("TEL",Util.TI18N.TEL()); 
		DataSourceTextField user_group=new DataSourceTextField("USRGRP_ID",Util.TI18N.USER_GROUP()); 
		DataSourceTextField active_flag =new DataSourceTextField("ACTIVE_FLAG",Util.TI18N.ACTIVE_FLAG()); 
		setFields(user_id,user_name,active_flag,default_org_id_name,cur_status_name,tel,user_group);*/
   
        setDataURL("initDataServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
        
        
       
		
	}
}
