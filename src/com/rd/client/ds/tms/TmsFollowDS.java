package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TmsFollowDS extends DataSource{
	private static TmsFollowDS instance = null;

	public static TmsFollowDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TmsFollowDS(id, tableName);
		}
		return instance;
	}

	public static TmsFollowDS getInstance(String id) {
		if (instance == null) {
			instance = new TmsFollowDS(id, id);
		}
		return instance;
	}

	public TmsFollowDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);


		setFields(keyField);
		setDataURL("tmsQueryServlet?ds_id=" + getID());
//		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
