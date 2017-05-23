package com.rd.client.action.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ApproveCustomerDS extends DataSource {

	private static ApproveCustomerDS instance = null;
	
	public static ApproveCustomerDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ApproveCustomerDS(id, tableName);
		}
		return instance;
	}

	public static ApproveCustomerDS getInstance(String id) {
		if (instance == null) {
			instance = new ApproveCustomerDS(id, id);
		}
		return instance;
	}
	
	public ApproveCustomerDS(String id, String tableName) {
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
