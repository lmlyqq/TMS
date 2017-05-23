package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 预警管理
 *
 */
public class AlterSettSmsDS extends DataSource {

    private static AlterSettSmsDS instance = null;

    public static AlterSettSmsDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new AlterSettSmsDS(id, tableName);
        }
        return instance;
    }
    public AlterSettSmsDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        DataSourceTextField MOBILE = new DataSourceTextField("MOBILE","手机号码");
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField,MOBILE);
        setDataURL("sysQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}