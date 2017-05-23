package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  基础资料->服务范围从表(BAS_RANGE_DETAIL)
 * @author yuanlei
 *
 */
public class BasRangeDetailDS extends DataSource {

    private static BasRangeDetailDS instance = null;

    public static BasRangeDetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasRangeDetailDS(id, tableName);
        }
        return instance;
    }
    public BasRangeDetailDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}