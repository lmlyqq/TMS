package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class CustomMonthlyDS  extends DataSource{
	private static CustomMonthlyDS instance = null;

	public static CustomMonthlyDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new CustomMonthlyDS(id, tableName);
		}
		return instance;
	}

	public static CustomMonthlyDS getInstance(String id) {
		if (instance == null) {
			instance = new CustomMonthlyDS(id, id);
		}
		return instance;
	}
	
	public CustomMonthlyDS(String id, String tableName) {
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CHARGE_TYPE = new DataSourceTextField("CHARGE_TYPE",
				Util.TI18N.DOC_NO());//费用类别
		DataSourceTextField PRE_FEE = new DataSourceTextField("PRE_FEE",
				Util.TI18N.PRE_FEE());//费用属性
		DataSourceTextField DUE_FEE = new DataSourceTextField("DUE_FEE",
				Util.TI18N.DUE_FEE());//费用属性
		
		setFields(keyField, CHARGE_TYPE, PRE_FEE, DUE_FEE);
	
		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
