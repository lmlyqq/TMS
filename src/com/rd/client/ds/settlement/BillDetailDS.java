package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--应回未回数据源
 * @author wangjun
 *
 */
public class BillDetailDS extends DataSource{
	private static BillDetailDS instance = null;

	public static BillDetailDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillDetailDS(id, tableName);
		}
		return instance;
	}

	public static BillDetailDS getInstance(String id) {
		if (instance == null) {
			instance = new BillDetailDS(id, id);
		}
		return instance;
	}

	public BillDetailDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField SERIAL_NUM = new DataSourceTextField("SERIAL_NUM", "回单序列号", 80);
		DataSourceTextField LOAD_NAME = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO(), 120);
		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO", Util.TI18N.SHPM_NO(), 120);
		DataSourceTextField STATUS = new DataSourceTextField("STATUS_NAME", Util.TI18N.STATUS(), 80);
		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 120);
		DataSourceTextField ODR_TIME = new DataSourceTextField("ODR_TIME", Util.TI18N.ODR_TIME(), 100);
		DataSourceTextField UNLOAD_TIME = new DataSourceTextField("UNLOAD_TIME", "实际到货时间", 100);
		DataSourceTextField UNLOAD_NAME = new DataSourceTextField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 160);
		DataSourceTextField SKU = new DataSourceTextField("SKU", Util.TI18N.SKU(), 70);
		DataSourceTextField SKU_NAME = new DataSourceTextField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);

		setFields(keyField,SERIAL_NUM,LOAD_NAME,SHPM_NO,STATUS,CUSTOM_ODR_NO,ODR_TIME,UNLOAD_TIME,UNLOAD_NAME,SKU,SKU_NAME);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

