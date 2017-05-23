package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统参数->列表配置->指定用户二级窗口
 * @author yuanlei
 *
 */
public class UserListDS extends DataSource {

    private static UserListDS instance = null;

    public static UserListDS getInstance(String id) {
    	return getInstance(id, id);
    }
    public static UserListDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new UserListDS(id, tableName);
        }
        return instance;
    }
    public UserListDS(String id, String tableName) {

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