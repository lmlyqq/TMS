package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RPickupds extends DataSource{
	private static RPickupds instance;
	
	private RPickupds(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		keyField.setRequired(true);
		
		setFields(keyField);
		
		setDataURL("repQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static RPickupds getInstance(String id){
		if (instance == null) {
			instance = new RPickupds(id, id);
		}
		return instance;
	}
	
	public static RPickupds getInstance(String id ,String tableName){
		if (instance == null) {
			instance = new RPickupds(id, tableName);
		}
		return instance;
	}
}
