package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 财务管理--计费管理--协议主信息
 * @author fangliangmeng
 *
 */
public class BillShpmFeeDS extends DataSource{
	private static BillShpmFeeDS instance = null;

	public static BillShpmFeeDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillShpmFeeDS(id, tableName);
		}
		return instance;
	}

	public static BillShpmFeeDS getInstance(String id) {
		if (instance == null) {
			instance = new BillShpmFeeDS(id, id);
		}
		return instance;
	}

	public BillShpmFeeDS(String id, String tableName) {

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

