package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->列表配置(模块配置左列表)
 * @author yuanlei
 *
 */
public class ListConfigDS extends DataSource {

    private static ListConfigDS instance = null;

    public static ListConfigDS getInstance(String id) {
        return getInstance(id, id);
    }
    public static ListConfigDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new ListConfigDS(id, tableName);
        }
        return instance;
    }
    public ListConfigDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        setDataURL("sysQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
    
    /*@Override
    protected Object transformRequest(DSRequest dsRequest) {
    	JavaScriptObject data = dsRequest.getData();
    	if (dsRequest.getOperationType() == DSOperationType.FETCH) {
    		JSOHelper.setAttribute (data, "OP_FLAG", DSOperationType.FETCH);
    		JSOHelper.setat
        }
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