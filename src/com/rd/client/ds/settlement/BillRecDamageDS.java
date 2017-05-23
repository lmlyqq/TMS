package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BillRecDamageDS extends DataSource{

	private static BillRecDamageDS instance = null;

    public static BillRecDamageDS getInstance(String id) {
        if (instance == null) {
            instance = new BillRecDamageDS(id, id);
        }
        return instance;
    }
    
    public static BillRecDamageDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BillRecDamageDS(id, tableName);
        }
        return instance;
    }
    
    public BillRecDamageDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	setFields(keyField);
 		setDataURL("settQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
