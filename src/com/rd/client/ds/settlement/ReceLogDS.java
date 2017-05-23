package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 核销表数据源
 * @author fangliangmeng
 *
 */
public class ReceLogDS extends DataSource{
	private static ReceLogDS instance = null;

	public static ReceLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ReceLogDS(id, tableName);
		}
		return instance;
	}

	public static ReceLogDS getInstance(String id) {
		if (instance == null) {
			instance = new ReceLogDS(id, id);
		}
		return instance;
	}

	public ReceLogDS(String id, String tableName) {

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

