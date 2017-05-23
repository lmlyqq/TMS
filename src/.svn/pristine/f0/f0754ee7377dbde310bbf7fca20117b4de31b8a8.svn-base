package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BillPaymentItemDS extends DataSource{
	
	private static BillPaymentItemDS instance = null;

    public static BillPaymentItemDS getInstance(String id) {
        if (instance == null) {
            instance = new BillPaymentItemDS(id, id);
        }
        return instance;
    }
    
    public static BillPaymentItemDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BillPaymentItemDS(id, tableName);
        }
        return instance;
    }
    
    public BillPaymentItemDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField PAYMENT_NO = new DataSourceTextField("PAYMENT_NO","请款单号");
    	
    	setFields(keyField,PAYMENT_NO);
 		setDataURL("tmsQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
