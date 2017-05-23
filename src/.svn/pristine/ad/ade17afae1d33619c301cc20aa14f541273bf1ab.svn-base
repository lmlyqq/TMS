package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BmsOrderDS extends DataSource{
	
	private static BmsOrderDS instance = null;

	public static BmsOrderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BmsOrderDS(id, tableName);
		}
		return instance;
	}

	public static BmsOrderDS getInstance(String id) {
		if (instance == null) {
			instance = new BmsOrderDS(id, id);
		}
		return instance;
	}

	public BmsOrderDS(String id, String tableName) {

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
		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
