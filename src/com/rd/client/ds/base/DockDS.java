package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  基础资料--仓库设置
 * @author wangjun
 *
 */
public class DockDS extends DataSource {

    private static DockDS instance = null;

    public static DockDS getInstance(String id) {
        if (instance == null) {
            instance = new DockDS(id, id);
        }
        return instance;
    }
    
    public static DockDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new DockDS(id, tableName);
        }
        return instance;
    }
    public DockDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        
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