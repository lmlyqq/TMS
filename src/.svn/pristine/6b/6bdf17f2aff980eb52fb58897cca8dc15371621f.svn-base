package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RecAuditDamageDS extends DataSource {

	private static RecAuditDamageDS instance = null;

	public static RecAuditDamageDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecAuditDamageDS(id, tableName);
		}
		return instance;
	}

	public static RecAuditDamageDS getInstance(String id) {
		if (instance == null) {
			instance = new RecAuditDamageDS(id, id);
		}
		return instance;
	}

	public RecAuditDamageDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
