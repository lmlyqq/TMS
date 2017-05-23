package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RoleDS extends DataSource {

    private static RoleDS instance = null;

    public static RoleDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new RoleDS(id, tableName);
        }
        return instance;
    }
    
    public static RoleDS getInstance(String id){
    	if(instance == null){
    		instance = new RoleDS(id,id);
    	}
    	return instance;
    }
    public RoleDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);
        
        DataSourceTextField ROLE_ID = new DataSourceTextField("ROLE_ID",Util.TI18N.ROLE_ID());
        DataSourceTextField ROLE_NAME = new DataSourceTextField("ROLE_NAME",Util.TI18N.ROLE_NAME());
        DataSourceTextField ENABLE_FLAG = new DataSourceTextField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
        
        setFields(keyField,ROLE_ID,ROLE_NAME,ENABLE_FLAG);
        
        setDataURL("sysQueryServlet?ds_id="+getID());
        
        setClientOnly(false);
        setShowPrompt(false);
    }
    
    /*@Override
    protected Object transformRequest(DSRequest dsRequest) {
		return super.transformRequest(dsRequest); 	
    }
	@Override
	protected void transformResponse(DSResponse response, DSRequest request,
			Object data) {
		super.transformResponse(response, request, data);
		//response.setTotalRows(response.getEndRow());
		response.setInvalidateCache(false);
		updateCaches(response);
	}*/
}