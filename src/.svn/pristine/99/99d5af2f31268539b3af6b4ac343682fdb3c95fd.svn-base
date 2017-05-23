package com.rd.client.ds.warning;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--应回未回数据源
 * @author wangjun
 *
 */
public class PrePodExcelDS extends DataSource{
	private static PrePodExcelDS instance = null;

	public static PrePodExcelDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PrePodExcelDS(id, tableName);
		}
		return instance;
	}

	public static PrePodExcelDS getInstance(String id) {
		if (instance == null) {
			instance = new PrePodExcelDS(id, id);
		}
		return instance;
	}

	public PrePodExcelDS(String id, String tableName) {

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

