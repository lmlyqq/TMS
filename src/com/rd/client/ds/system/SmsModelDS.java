package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->短信设置
 * @author yuanlei
 * @create time 2012-06-05 20:10
 *
 */
public class SmsModelDS extends DataSource {

    private static SmsModelDS instance = null;

    public static SmsModelDS getInstance(String id) {
        return getInstance(id, id);
    }
    public static SmsModelDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SmsModelDS(id, tableName);
        }
        return instance;
    }
    public SmsModelDS(String id, String tableName) {
        
    	
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