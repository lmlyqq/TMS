package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PayAdjAuditdetailDS extends DataSource{

	private static PayAdjAuditdetailDS instance = null;

    public static PayAdjAuditdetailDS getInstance(String id) {
        if (instance == null) {
            instance = new PayAdjAuditdetailDS(id, id);
        }
        return instance;
    }
    
    public static PayAdjAuditdetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new PayAdjAuditdetailDS(id, tableName);
        }
        return instance;
    }
    
    public PayAdjAuditdetailDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField ADJ_NO = new DataSourceTextField("ADJ_NO","调整单号");
    	
    	setFields(keyField,ADJ_NO);
 		setDataURL("settQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
