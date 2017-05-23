package com.rd.client.ds.whse;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 作业单头数据源
 * @author yuanlei
 *
 */
public class UserWhseOrderDS extends DataSource{
	private static UserWhseOrderDS instance = null;

	public static UserWhseOrderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new UserWhseOrderDS(id, tableName);
		}
		return instance;
	}

	public static UserWhseOrderDS getInstance(String id) {
		if (instance == null) {
			instance = new UserWhseOrderDS(id, id);
		}
		return instance;
	}

	public UserWhseOrderDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
//		setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		setFields(keyField);
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
