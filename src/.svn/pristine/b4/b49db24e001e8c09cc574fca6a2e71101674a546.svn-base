package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class DemandDS extends DataSource{
	
	private static DemandDS instance = null;
	
	public static DemandDS getInstance(String id) {
        if (instance == null) {
            instance = new DemandDS(id, id);
        }
        return instance;
    }
    
    public static DemandDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new DemandDS(id, tableName);
        }
        return instance;
    }
    
    public DemandDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
         
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setRequired(true);
    	keyField.setHidden(true);
         
    	setFields(keyField);
    	setDataURL("tmsQueryServlet?ds_id=" + getID());
    	setClientOnly(false);
    	setShowPrompt(false);
    }
}
