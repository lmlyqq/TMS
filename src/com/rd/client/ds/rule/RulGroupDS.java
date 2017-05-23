package com.rd.client.ds.rule;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料-->时间管理-->FormulaDS
 * @author wangjun
 *
 */

public class RulGroupDS extends DataSource {

	private static RulGroupDS instance = null;

	public static RulGroupDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RulGroupDS(id, tableName);
		}
		return instance;
	}

	public RulGroupDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);
		setDataURL("rulQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
