package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 预警管理
 *
 */
public class AlterSettMailDS extends DataSource {

    private static AlterSettMailDS instance = null;

    public static AlterSettMailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new AlterSettMailDS(id, tableName);
        }
        return instance;
    }
    public AlterSettMailDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        DataSourceTextField EMAIL = new DataSourceTextField("EMAIL","邮箱");
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField,EMAIL);
        setDataURL("sysQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}