package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class Odr_Job_Header_DS extends DataSource {
private static Odr_Job_Header_DS instance = null;
	
	public Odr_Job_Header_DS(String id, String tableName) {

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
	
	public static Odr_Job_Header_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new Odr_Job_Header_DS(id, tableName);
		}
		return instance;
	}

	public static Odr_Job_Header_DS getInstance(String id) {
		if (instance == null) {
			instance = new Odr_Job_Header_DS(id, id);
		}
		return instance;
	}
}
