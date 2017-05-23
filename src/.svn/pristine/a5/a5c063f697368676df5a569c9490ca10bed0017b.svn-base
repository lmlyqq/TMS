package com.rd.client.ds.vehassist;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 年审管理数据源
 * @author Administrator
 *
 */
public class VehVerifyDS extends DataSource{
	
	private static VehVerifyDS instance = null;

    public static VehVerifyDS getInstance(String id) {
        if (instance == null) {
            instance = new VehVerifyDS(id, id);
        }
        return instance;
    }
    
    public static VehVerifyDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new VehVerifyDS(id, tableName);
        }
        return instance;
    }
    
    public VehVerifyDS(String id, String tableName) {
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
