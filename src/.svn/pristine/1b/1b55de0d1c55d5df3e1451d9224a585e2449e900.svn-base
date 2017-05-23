package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->列表配置->用户配置
 * @author yuanlei
 *
 */
public class ConfigNameDS extends DataSource {

    private static ConfigNameDS instance = null;

    public static ConfigNameDS getInstance(String id) {
    	return getInstance(id, id);
    }
    public static ConfigNameDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new ConfigNameDS(id, tableName);
        }
        return instance;
    }
    public ConfigNameDS(String id, String tableName) {

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
}