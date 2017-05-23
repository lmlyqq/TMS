package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class GpsEqDS extends DataSource{
	
	private static GpsEqDS instance = null;

    public static GpsEqDS getInstance(String tableName) {
        if (instance == null) {
            instance = new GpsEqDS(tableName);
        }
        return instance;
    }
    
    public GpsEqDS(String tableName) {
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
