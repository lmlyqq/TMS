package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class DamageViewDS extends DataSource {
	private static DamageViewDS instance = null;

	public static DamageViewDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new DamageViewDS(id, tableName);
		}
		return instance;
	}

	public DamageViewDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		setFields(keyField);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
//		setShowPrompt(false);
	}
}
