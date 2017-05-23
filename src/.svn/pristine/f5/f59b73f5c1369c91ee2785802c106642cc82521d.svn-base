package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ApproveHeadDS extends DataSource {

private static ApproveHeadDS instance = null;
	
	public static ApproveHeadDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ApproveHeadDS(id, tableName);
		}
		return instance;
	}

	public static ApproveHeadDS getInstance(String id) {
		if (instance == null) {
			instance = new ApproveHeadDS(id, id);
		}
		return instance;
	}
	
	public ApproveHeadDS(String id, String tableName) {
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		setFields(keyField);
		setDataURL("tmsQueryServlet?ds_id=" + this.getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
