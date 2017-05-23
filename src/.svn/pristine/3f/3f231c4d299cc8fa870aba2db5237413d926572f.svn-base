package com.rd.client.ds.vehassist;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AccidentLogDS extends DataSource{
	 private static AccidentLogDS instance = null;

	    public static AccidentLogDS getInstance(String id) {
	        if (instance == null) {
	            instance = new AccidentLogDS(id, id);
	        }
	        return instance;
	    }
	    
	    public static AccidentLogDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new AccidentLogDS(id, tableName);
	        }
	        return instance;
	    }
	    public AccidentLogDS(String id, String tableName) {

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
			DataSourceTextField INSUR_ID = new DataSourceTextField("INSUR_ID","事故编号");
			DataSourceTextField DESCR = new DataSourceTextField("DESCR","处理情况");

			setFields(keyField, INSUR_ID, DESCR);
	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}
