package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 报表管理-->作业报表-->营运日报表 数据源
 * @author wangjun
 *
 */
public class R_SERVICE_DAILY_DS extends DataSource{
	private static R_SERVICE_DAILY_DS instance = null;

	public static R_SERVICE_DAILY_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_SERVICE_DAILY_DS(id, tableName);
		}
		return instance;
	}

	public static R_SERVICE_DAILY_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_SERVICE_DAILY_DS(id, id);
		}
		return instance;
	}

	public R_SERVICE_DAILY_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOM_ODR_NO =new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
		setFields(keyField,CUSTOM_ODR_NO);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

