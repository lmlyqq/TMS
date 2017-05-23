package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输计划--车辆登记    作业单头数据源
 * @author wangjun
 *
 */
public class ShpmHeadDS extends DataSource{
	private static ShpmHeadDS instance = null;

	public static ShpmHeadDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ShpmHeadDS(id, tableName);
		}
		return instance;
	}

	public static ShpmHeadDS getInstance(String id) {
		if (instance == null) {
			instance = new ShpmHeadDS(id, id);
		}
		return instance;
	}

	public ShpmHeadDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOMER = new DataSourceTextField("CUSTOMER_ID",
				Util.TI18N.CUSTOMER());//客户
		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO",
				Util.TI18N.SHPM_NO());//
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO",
				Util.TI18N.ORDER_CODE());//订单编号
		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",
				Util.TI18N.CUSTOM_ODR_NO());//客户单号
		setFields(keyField, CUSTOMER, SHPM_NO, ODR_NO, CUSTOM_ODR_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
