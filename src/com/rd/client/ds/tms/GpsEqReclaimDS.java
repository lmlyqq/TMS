package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class GpsEqReclaimDS extends DataSource {
	
	private static GpsEqReclaimDS instance = null;
	
	public static GpsEqReclaimDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new GpsEqReclaimDS(id, tableName);
        }
        return instance;
    }
    public GpsEqReclaimDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        DataSourceTextField EQUIP_NO = new DataSourceTextField("EQUIP_NO", "EQUIP_NO");
        DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", "LOAD_NO");
        
        setFields(keyField,EQUIP_NO,LOAD_NO);
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
