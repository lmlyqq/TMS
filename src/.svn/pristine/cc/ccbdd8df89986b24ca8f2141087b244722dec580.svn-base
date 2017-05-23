package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TmsOrdManageDS extends DataSource{
	private static TmsOrdManageDS instance = null;

	public static TmsOrdManageDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TmsOrdManageDS(id, tableName);
		}
		return instance;
	}

	public static TmsOrdManageDS getInstance(String id) {
		if (instance == null) {
			instance = new TmsOrdManageDS(id, id);
		}
		return instance;
	}

	public TmsOrdManageDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOMER_NAME = new DataSourceTextField("CUSTOMER_NAME",
				Util.TI18N.CUSTOMER());//客户
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO",
				Util.TI18N.ORDER_CODE());//订单编号
		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",
				Util.TI18N.CUSTOM_ODR_NO());//客户单号
		setFields(keyField, CUSTOMER_NAME, ODR_NO,CUSTOM_ODR_NO);

		setFields(keyField);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

