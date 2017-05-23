package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TranReportDS extends DataSource{
	private static TranReportDS instance = null;

	public static TranReportDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TranReportDS(id, tableName);
		}
		return instance;
	}

	public static TranReportDS getInstance(String id) {
		if (instance == null) {
			instance = new TranReportDS(id, id);
		}
		return instance;
	}

	public TranReportDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号

		setFields(keyField,CUSTOM_ODR_NO);
		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
