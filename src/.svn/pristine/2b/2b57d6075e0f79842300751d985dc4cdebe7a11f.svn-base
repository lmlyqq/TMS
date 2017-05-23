package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--调整单管理数据源（从）
 * @author fanglm
 *
 */
public class BillAdjItemDS extends DataSource{

	private static BillAdjItemDS instance = null;

	public static BillAdjItemDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillAdjItemDS(id, tableName);
		}
		return instance;
	}

	public static BillAdjItemDS getInstance(String id) {
		if (instance == null) {
			instance = new BillAdjItemDS(id, id);
		}
		return instance;
	}

	public BillAdjItemDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField ADJUST_NO = new DataSourceTextField("ADJUST_NO","调整单号");

		setFields(keyField,ADJUST_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
