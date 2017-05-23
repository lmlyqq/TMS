package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SystemListDS extends DataSource{

	private static SystemListDS instance = null;
	
	public static SystemListDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SystemListDS(id, tableName);
        }
        return instance;
    }
    public SystemListDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        setDataURL("sysQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
    }
	
}
