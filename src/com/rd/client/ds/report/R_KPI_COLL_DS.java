package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class R_KPI_COLL_DS extends DataSource {
private static R_KPI_COLL_DS instance = null;
	
	public static R_KPI_COLL_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_KPI_COLL_DS(id, tableName);
		}
		return instance;
	}

	public static R_KPI_COLL_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_KPI_COLL_DS(id, id);
		}
		return instance;
	}
	
	public R_KPI_COLL_DS(String id, String tableName) {

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
	
}
