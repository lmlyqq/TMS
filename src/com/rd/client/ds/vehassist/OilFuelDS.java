package com.rd.client.ds.vehassist;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class OilFuelDS extends DataSource{
	 private static OilFuelDS instance = null;

	    public static OilFuelDS getInstance(String id) {
	        if (instance == null) {
	            instance = new OilFuelDS(id, id);
	        }
	        return instance;
	    }
	    
	    public static OilFuelDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new OilFuelDS(id, tableName);
	        }
	        return instance;
	    }
	    public OilFuelDS(String id, String tableName) {

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
			DataSourceTextField PLATENO = new DataSourceTextField("PLATENO", Util.TI18N.PLATENO());
			DataSourceTextField TRS_ID = new DataSourceTextField("TRS_ID", Util.TI18N.TRS_ID());

			setFields(keyField, PLATENO, TRS_ID);
	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}
