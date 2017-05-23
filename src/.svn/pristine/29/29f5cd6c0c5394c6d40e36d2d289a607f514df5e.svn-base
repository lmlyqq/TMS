package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PayAdjBillExaDS extends DataSource{

	private static PayAdjBillExaDS instance = null;

    public static PayAdjBillExaDS getInstance(String id) {
        if (instance == null) {
            instance = new PayAdjBillExaDS(id, id);
        }
        return instance;
    }
    
    public static PayAdjBillExaDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new PayAdjBillExaDS(id, tableName);
        }
        return instance;
    }
    
    public PayAdjBillExaDS(String id, String tableName) {
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setHidden(true);
    	
    	DataSourceTextField DOC_NO = new DataSourceTextField("DOC_NO","单据编号");
    	
    	setFields(keyField,DOC_NO);
 		setDataURL("settQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
	
}
