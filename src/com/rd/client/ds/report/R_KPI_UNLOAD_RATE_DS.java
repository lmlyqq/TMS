package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 报表管理-->KPI报表-->准时到货率数据源
 *
 */
public class R_KPI_UNLOAD_RATE_DS extends DataSource{
	private static R_KPI_UNLOAD_RATE_DS instance = null;

	public static R_KPI_UNLOAD_RATE_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_KPI_UNLOAD_RATE_DS(id, tableName);
		}
		return instance;
	}

	public static R_KPI_UNLOAD_RATE_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_KPI_UNLOAD_RATE_DS(id, id);
		}
		return instance;
	}

	public R_KPI_UNLOAD_RATE_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField SUPLR_NAME =new DataSourceTextField("SUPLR_NAME",Util.TI18N.SUPLR_NAME());
		
		setFields(keyField,SUPLR_NAME);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

