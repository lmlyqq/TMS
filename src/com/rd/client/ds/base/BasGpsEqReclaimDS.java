package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BasGpsEqReclaimDS extends DataSource{
	
	private static BasGpsEqReclaimDS instance = null;
	
	public static BasGpsEqReclaimDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasGpsEqReclaimDS(id, tableName);
        }
        return instance;
    }
    public BasGpsEqReclaimDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        DataSourceTextField msrmntField = new DataSourceTextField("EQUIP_NO", "EQUIP_NO", 10, false);
        
        setFields(keyField,msrmntField);
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
