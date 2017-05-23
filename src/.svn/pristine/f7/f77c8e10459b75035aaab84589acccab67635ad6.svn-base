package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 投诉管理数据源
 * @author Administrator
 *
 */
public class ComplaintDS extends DataSource{
	
	private static ComplaintDS instance = null;

    public static ComplaintDS getInstance(String id) {
        if (instance == null) {
            instance = new ComplaintDS(id, id);
        }
        return instance;
    }
    
    public static ComplaintDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new ComplaintDS(id, tableName);
        }
        return instance;
    }
    
    public ComplaintDS(String id, String tableName) {
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
