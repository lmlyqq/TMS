package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料-->时间管理-->ConditionDS
 * @author wangjun
 *
 */


public class ConditionDS extends DataSource {

	private static ConditionDS instance = null;

	public static ConditionDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ConditionDS(id, tableName);
		}
		return instance;
	}

	public static ConditionDS getInstance(String id) {
		if (instance == null) {
			instance = new ConditionDS(id, id);
		}
		return instance;
	}

	public ConditionDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		setFields(keyField);
		setDataURL("basQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
