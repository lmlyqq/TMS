package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料  -- 供应商管理---运输信息--执行机构分配
 * @author fanglm
 *
 */
public class SuppOrgDS extends DataSource {
	
	private static SuppOrgDS instance = null;

    public static SuppOrgDS getInstance(String id) {
        if (instance == null) {
            instance = new SuppOrgDS(id, id);
        }
        return instance;
    }
    
    public static SuppOrgDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SuppOrgDS(id, tableName);
        }
        return instance;
    }
    public SuppOrgDS(String id, String tableName) {

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
