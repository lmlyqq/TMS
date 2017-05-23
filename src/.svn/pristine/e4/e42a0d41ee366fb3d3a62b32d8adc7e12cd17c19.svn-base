package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class VehicleInspectionDS extends DataSource{
	
	private static VehicleInspectionDS instance = null;
	
	public static VehicleInspectionDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new VehicleInspectionDS(id, tableName);
        }
        return instance;
    }
    public static VehicleInspectionDS getInstance(String id){
    	if(instance == null){
    		instance = new VehicleInspectionDS(id,id);
    	}
    	return instance;
    }
    public VehicleInspectionDS(String id, String tableName) {
    	setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("TRS_ID", "TRS_ID", 10, true);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        setDataURL("tmsQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
    }
    
}
