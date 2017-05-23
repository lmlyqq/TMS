package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 费用管理--计费管理--计费要素管理
 * @author wangjun
 *
 */
public class BillFactoryDS extends DataSource{
	private static BillFactoryDS instance = null;

	public static BillFactoryDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillFactoryDS(id, tableName);
		}
		return instance;
	}

	public static BillFactoryDS getInstance(String id) {
		if (instance == null) {
			instance = new BillFactoryDS(id, id);
		}
		return instance;
	}

	public BillFactoryDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
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

