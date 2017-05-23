package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BillPaymentDS extends DataSource{
	
	private static BillPaymentDS instance = null;

    public static BillPaymentDS getInstance(String id) {
        if (instance == null) {
            instance = new BillPaymentDS(id, id);
        }
        return instance;
    }
    
    public static BillPaymentDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BillPaymentDS(id, tableName);
        }
        return instance;
    }
    
    public BillPaymentDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	setFields(keyField);
 		setDataURL("tmsQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
