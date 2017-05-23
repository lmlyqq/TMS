package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TrackSkuDS extends DataSource {
	private static TrackSkuDS instance = null;

    public static TrackSkuDS getInstance(String id) {
        if (instance == null) {
            instance = new TrackSkuDS(id, id);
        }
        return instance;
    }
    
    public static TrackSkuDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new TrackSkuDS(id, tableName);
        }
        return instance;
    }
    public TrackSkuDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName("BAS_SKU");
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField SKU = new DataSourceTextField("SKU", "SKU", 10, false);
        //keyField.setPrimaryKey(true);
        //keyField.setRequired(true);
        //keyField.setHidden(true);

		setFields(SKU);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
