package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 数据源
 * @author wangjun
 *
 */
public class R_CustomerOrderDS extends DataSource{
	private static R_CustomerOrderDS instance = null;

	public static R_CustomerOrderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_CustomerOrderDS(id, tableName);
		}
		return instance;
	}

	public static R_CustomerOrderDS getInstance(String id) {
		if (instance == null) {
			instance = new R_CustomerOrderDS(id, id);
		}
		return instance;
	}

	public R_CustomerOrderDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO());//调度单号
		DataSourceTextField SUPLR_ID = new DataSourceTextField("SUPLR_ID", Util.TI18N.SUPLR_ID());//二级窗口 SUPLR_ID_NAME
		DataSourceTextField EXEC_ORG_ID = new DataSourceTextField("EXEC_ORG_ID", Util.TI18N.EXEC_ORG_ID());//EXEC_ORG_ID 执行结构
		DataSourceTextField CUSTOMER_ID = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER());//二级窗口  客户 
		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		DataSourceTextField UNLOAD_TEL = new DataSourceTextField("UNLOAD_TEL",Util.TI18N.UNLOAD_TEL());//收货方手机号码
		DataSourceTextField UNLOAD_ID = new DataSourceTextField("UNLOAD_ID", Util.TI18N.UNLOAD_NAME());
		setFields(keyField, LOAD_NO, CUSTOM_ODR_NO, SUPLR_ID, EXEC_ORG_ID, CUSTOMER_ID, UNLOAD_TEL, UNLOAD_ID);

		setDataURL("repQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

