package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->定时器管理
 * @author fanglm
 * @create time 2011-01-12 20:10
 *
 */
public class SysTimerDS extends DataSource {

    private static SysTimerDS instance = null;

    public static SysTimerDS getInstance(String id) {
        return getInstance(id, id);
    }
    public static SysTimerDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SysTimerDS(id, tableName);
        }
        return instance;
    }
    public SysTimerDS(String id, String tableName) {
        
    	
    	//id 将其设为隐藏
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