package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 作业单明细数据源  主要用于数据源
 * @author fanglm
 *
 */
public class ShpmDetailQSDS extends DataSource{
	private static ShpmDetailQSDS instance = null;

	public static ShpmDetailQSDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ShpmDetailQSDS(id, tableName);
		}
		return instance;
	}

	public static ShpmDetailQSDS getInstance(String id) {
		if (instance == null) {
			instance = new ShpmDetailQSDS(id, id);
		}
		return instance;
	}

	public ShpmDetailQSDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO",
				Util.TI18N.SHPM_NO());//作业单编号
		DataSourceTextField SHPM_ROW = new DataSourceTextField("SHPM_ROW",
				Util.TI18N.ORD_ROW());//行号
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO",
				Util.TI18N.ORDER_CODE());//原始托运单
		DataSourceTextField SKU_ID = new DataSourceTextField("SKU_ID",
				Util.TI18N.SKU_ID());//货品名称
		DataSourceTextField SKU_NAME = new DataSourceTextField("SKU_NAME",
				Util.TI18N.SKU_NAME());//货品名称
		setFields(keyField, SHPM_ROW, SHPM_NO, ODR_NO, SKU_ID, SKU_NAME);
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
