package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TempDS extends DataSource{
	
	private static TempDS instance = null;

    public static TempDS getInstance(String tableName) {
        if (instance == null) {
            instance = new TempDS(tableName);
        }
        return instance;
    }
    
    public TempDS(String tableName) {
    	setID(tableName);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        
        setDataURL("basQueryServlet?ds_id="+getAttribute("tableName")+"&is_curr_page=true");
        setClientOnly(false);
    }
}
