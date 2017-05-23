package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 手机定位数据源
 * @author wangjun
 *
 */
public class PositionLogDS extends DataSource{
	private static PositionLogDS instance = null;

	public static PositionLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PositionLogDS(id, tableName);
		}
		return instance;
	}

	public static PositionLogDS getInstance(String id) {
		if (instance == null) {
			instance = new PositionLogDS(id, id);
		}
		return instance;
	}

	public PositionLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
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

