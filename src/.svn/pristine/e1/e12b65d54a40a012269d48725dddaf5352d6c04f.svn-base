package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class R_SORTING_WHDS extends DataSource {
	private static R_SORTING_WHDS instance = null;
	
	public static R_SORTING_WHDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_SORTING_WHDS(id, tableName);
		}
		return instance;
	}

	public static R_SORTING_WHDS getInstance(String id) {
		if (instance == null) {
			instance = new R_SORTING_WHDS(id, id);
		}
		return instance;
	}
	public R_SORTING_WHDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);

		setDataURL("repQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	
}
