package com.rd.client.ds.warning;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--预付款超额预警
 *
 */
public class PreFeeExcelDS extends DataSource{
	private static PreFeeExcelDS instance = null;

	public static PreFeeExcelDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PreFeeExcelDS(id, tableName);
		}
		return instance;
	}

	public static PreFeeExcelDS getInstance(String id) {
		if (instance == null) {
			instance = new PreFeeExcelDS(id, id);
		}
		return instance;
	}

	public PreFeeExcelDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		
		setFields(keyField);

		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

