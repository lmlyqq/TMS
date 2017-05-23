package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 结算管理--客户费用结算--应收费用数据源
 * @author wangjun
 *
 */
public class BillReceDS extends DataSource{
	private static BillReceDS instance = null;

	public static BillReceDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillReceDS(id, tableName);
		}
		return instance;
	}

	public static BillReceDS getInstance(String id) {
		if (instance == null) {
			instance = new BillReceDS(id, id);
		}
		return instance;
	}

	public BillReceDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField DOC_NO = new DataSourceTextField("DOC_NO",
				Util.TI18N.DOC_NO());//费用类别
		DataSourceTextField FEE_NAME = new DataSourceTextField("FEE_NAME",
				Util.TI18N.FEE_NAME());//费用属性
		DataSourceTextField FEE_BAS = new DataSourceTextField("FEE_BAS",
				Util.TI18N.FEE_BASE());//费用属性
//		
//		DataSourceTextField SERIAL_NUM = new DataSourceTextField("SERIAL_NUM", "回单序列号", 80);
//		DataSourceTextField LOAD_NAME = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO(), 120);
//		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO", Util.TI18N.SHPM_NO(), 120);
//		DataSourceTextField STATUS = new DataSourceTextField("STATUS_NAME", Util.TI18N.STATUS(), 80);
//		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 120);
//		DataSourceTextField ODR_TIME = new DataSourceTextField("ODR_TIME", Util.TI18N.ODR_TIME(), 100);
//		DataSourceTextField UNLOAD_TIME = new DataSourceTextField("UNLOAD_TIME", "实际到货时间", 100);
//		DataSourceTextField UNLOAD_NAME = new DataSourceTextField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 160);
//		DataSourceTextField SKU = new DataSourceTextField("SKU", Util.TI18N.SKU(), 70);
//		DataSourceTextField SKU_NAME = new DataSourceTextField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);

		setFields(keyField, DOC_NO, FEE_NAME, FEE_BAS);
		//,SERIAL_NUM,LOAD_NAME,SHPM_NO,STATUS,CUSTOM_ODR_NO,ODR_TIME,UNLOAD_TIME,UNLOAD_NAME,SKU,SKU_NAME);

		setDataURL("settQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

