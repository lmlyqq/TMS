package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 结算管理-期初账单
 * @author wangjun
 *
 */
public class RecInitialDS extends DataSource{
	private static RecInitialDS instance = null;

	public static RecInitialDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecInitialDS(id, tableName);
		}
		return instance;
	}

	public static RecInitialDS getInstance(String id) {
		if (instance == null) {
			instance = new RecInitialDS(id, id);
		}
		return instance;
	}

	public RecInitialDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField INIT_NO = new DataSourceTextField("INIT_NO",
				"期初账单");
		

		setFields(keyField, INIT_NO);
		
		setDataURL("settQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

