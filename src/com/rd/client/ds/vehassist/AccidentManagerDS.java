package com.rd.client.ds.vehassist;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AccidentManagerDS extends DataSource{
	 private static AccidentManagerDS instance = null;

	    public static AccidentManagerDS getInstance(String id) {
	        if (instance == null) {
	            instance = new AccidentManagerDS(id, id);
	        }
	        return instance;
	    }
	    
	    public static AccidentManagerDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new AccidentManagerDS(id, tableName);
	        }
	        return instance;
	    }
	    public AccidentManagerDS(String id, String tableName) {

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
			DataSourceTextField INSUR_NO = new DataSourceTextField("INSUR_NO","事故编号");
			DataSourceTextField STATUS = new DataSourceTextField("STATUS","状态");

			setFields(keyField, INSUR_NO, STATUS);
	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}
