package com.rd.client.ds.warning;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--应到未到数据源
 * @author wangjun
 *
 */
public class PreUnloadExcelDS extends DataSource{
	private static PreUnloadExcelDS instance = null;

	public static PreUnloadExcelDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PreUnloadExcelDS(id, tableName);
		}
		return instance;
	}

	public static PreUnloadExcelDS getInstance(String id) {
		if (instance == null) {
			instance = new PreUnloadExcelDS(id, id);
		}
		return instance;
	}

	public PreUnloadExcelDS(String id, String tableName) {

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

