package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料  -- 供应商管理---运输信息--运输服务
 * @author fanglm
 *
 */
public class SuppServDS extends DataSource {
	
	private static SuppServDS instance = null;

    public static SuppServDS getInstance(String id) {
        if (instance == null) {
            instance = new SuppServDS(id, id);
        }
        return instance;
    }
    
    public static SuppServDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SuppServDS(id, tableName);
        }
        return instance;
    }
    public SuppServDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		setFields(keyField);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
