package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RecAuditdetailDS extends DataSource{
	
	private static RecAuditdetailDS instance = null;

    public static RecAuditdetailDS getInstance(String id) {
        if (instance == null) {
            instance = new RecAuditdetailDS(id, id);
        }
        return instance;
    }
    
    public static RecAuditdetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new RecAuditdetailDS(id, tableName);
        }
        return instance;
    }
    
    public RecAuditdetailDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField INVOICE_NO = new DataSourceTextField("INVOICE_NO","发票编号");
    	
    	setFields(keyField,INVOICE_NO);
 		setDataURL("settQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
