package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 财务管理--计费管理--协议主信息
 * @author fangliangmeng
 *
 */
public class BillSettleDetailDS extends DataSource{
	private static BillSettleDetailDS instance = null;

	public static BillSettleDetailDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillSettleDetailDS(id, tableName);
		}
		return instance;
	}

	public static BillSettleDetailDS getInstance(String id) {
		if (instance == null) {
			instance = new BillSettleDetailDS(id, id);
		}
		return instance;
	}

	public BillSettleDetailDS(String id, String tableName) {

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

