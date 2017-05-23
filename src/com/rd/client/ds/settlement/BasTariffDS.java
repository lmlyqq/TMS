package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--应回未回数据源
 * @author wangjun
 *
 */
public class BasTariffDS extends DataSource{
	private static BasTariffDS instance = null;

	public static BasTariffDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BasTariffDS(id, tableName);
		}
		return instance;
	}

	public static BasTariffDS getInstance(String id) {
		if (instance == null) {
			instance = new BasTariffDS(id, id);
		}
		return instance;
	}

	public BasTariffDS(String id, String tableName) {

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

