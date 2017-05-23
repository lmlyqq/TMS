package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class OdrKPIds extends DataSource{
	private static OdrKPIds instance;
	
	private OdrKPIds(String id,String tableName){
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
	
	public static OdrKPIds getInstance(String id){
		if (instance == null) {
			instance = new OdrKPIds(id, id);
		}
		return instance;
	}
	
	public static OdrKPIds getInstance(String id ,String tableName){
		if (instance == null) {
			instance = new OdrKPIds(id, tableName);
		}
		return instance;
	}
}
