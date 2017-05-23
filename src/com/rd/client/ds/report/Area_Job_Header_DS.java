package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class Area_Job_Header_DS extends DataSource {
	private static Area_Job_Header_DS instance = null;
	
	public Area_Job_Header_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static Area_Job_Header_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new Area_Job_Header_DS(id, tableName);
		}
		return instance;
	}

	public static Area_Job_Header_DS getInstance(String id) {
		if (instance == null) {
			instance = new Area_Job_Header_DS(id, id);
		}
		return instance;
	}
}
