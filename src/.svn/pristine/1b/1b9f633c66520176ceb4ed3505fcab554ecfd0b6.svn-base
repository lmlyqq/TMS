package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 调度配载-->业务日志
 * @author wangjun
 *
 */

public class TansActLogDS extends DataSource{

	private static TansActLogDS instance = null;

	public static TansActLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TansActLogDS(id, tableName);
		}
		return instance;
	}

	public static TansActLogDS getInstance(String id) {
		if (instance == null) {
			instance = new TansActLogDS(id, id);
		}
		return instance;
	}

	public TansActLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
		
	}
}
