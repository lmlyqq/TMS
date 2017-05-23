package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PrintGrantDS extends DataSource {
	//打印授权 数据源
    private static PrintGrantDS instance = null;

    public static PrintGrantDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new PrintGrantDS(id, tableName);
        }
        return instance;
    }
    
    public static PrintGrantDS getInstance(String id){
    	if(instance == null){
    		instance = new PrintGrantDS(id,id);
    	}
    	return instance;
    }
    public PrintGrantDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);
        
        setFields(keyField);
        
        setDataURL("sysQueryServlet?ds_id="+getID());
        
        setClientOnly(false);
        setShowPrompt(false);
    }
}