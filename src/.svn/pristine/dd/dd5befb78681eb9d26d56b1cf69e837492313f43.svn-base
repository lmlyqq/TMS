package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BasGpsEqDS extends DataSource{
	
	private static BasGpsEqDS instance = null;
	
	public static BasGpsEqDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasGpsEqDS(id, tableName);
        }
        return instance;
    }
	
	public BasGpsEqDS(String id, String tableName) {
		setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("ID");
        keyField.setHidden(true);
        
        setFields(keyField);
        setDataURL("basQueryServlet?is_curr_page=true&ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
        
	}
	
}
