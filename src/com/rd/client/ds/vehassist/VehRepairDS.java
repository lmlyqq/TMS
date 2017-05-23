package com.rd.client.ds.vehassist;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class VehRepairDS extends DataSource{
	 private static VehRepairDS instance = null;

	    public static VehRepairDS getInstance(String id) {
	        if (instance == null) {
	            instance = new VehRepairDS(id, id);
	        }
	        return instance;
	    }
	    
	    public static VehRepairDS getInstance(String id, String tableName) {
	        if (instance == null) {
	            instance = new VehRepairDS(id, tableName);
	        }
	        return instance;
	    }
	    public VehRepairDS(String id, String tableName) {

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
			DataSourceTextField PLATENO = new DataSourceTextField("PLATE_NO", "车牌号");
			DataSourceTextField SUPLR_ID = new DataSourceTextField("SUPLR_ID", "承运商");

			setFields(keyField, PLATENO, SUPLR_ID);
	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}
