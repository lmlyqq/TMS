package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BasReceAccountDS extends DataSource{
	 private static PackageDS instance = null;

	    public static PackageDS getInstance(String id) {
	        if (instance == null) {
	            instance = new PackageDS(id, id);
	        }
	        return instance;
	    }
	    
	    public static PackageDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new PackageDS(id, tableName);
	        }
	        return instance;
	    }
	    public BasReceAccountDS(String id, String tableName) {

	        setID(id);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName(tableName);
	        setAttribute("tableName", tableName, false);
	        
	        //id
	        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	        keyField.setPrimaryKey(true);
	        keyField.setRequired(true);
	        keyField.setHidden(true);

			// name
			DataSourceTextField ACCOUNT = new DataSourceTextField("ACCOUNT", "收款账号");
			DataSourceTextField RECEIVER = new DataSourceTextField("RECEIVER", "收款人");

			setFields(keyField, ACCOUNT, RECEIVER);
	        
	        setDataURL("basQueryServlet?is_curr_page=true&ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}
