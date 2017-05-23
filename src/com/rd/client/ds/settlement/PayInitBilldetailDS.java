package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PayInitBilldetailDS extends DataSource{
	
	private static PayInitBilldetailDS instance = null;

    public static PayInitBilldetailDS getInstance(String id) {
        if (instance == null) {
            instance = new PayInitBilldetailDS(id, id);
        }
        return instance;
    }
    
    public static PayInitBilldetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new PayInitBilldetailDS(id, tableName);
        }
        return instance;
    }
    
    public PayInitBilldetailDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField INIT_NO = new DataSourceTextField("INIT_NO","期初单号");
    	
    	setFields(keyField,INIT_NO);
 		setDataURL("settQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
    
}
