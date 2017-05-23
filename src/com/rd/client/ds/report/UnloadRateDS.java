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
public class UnloadRateDS extends DataSource{
	private static UnloadRateDS instance = null;

	public static UnloadRateDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new UnloadRateDS(id, tableName);
		}
		return instance;
	}

	public static UnloadRateDS getInstance(String id) {
		if (instance == null) {
			instance = new UnloadRateDS(id, id);
		}
		return instance;
	}

	public UnloadRateDS(String id, String tableName) {

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
		//yuanlei 2012-09-11 自定义查询增加‘到库时间'
		DataSourceTextField ARRIVE_WHSE_TIME = new DataSourceTextField("ARRIVE_WHSE_TIME",Util.TI18N.ARRIVE_WHSE_TIME());//客户单号
		//yuanlei 2012-09-11
		setFields(keyField, LOAD_NO, CUSTOM_ODR_NO, SUPLR_ID, EXEC_ORG_ID, CUSTOMER_ID, ARRIVE_WHSE_TIME);

		setDataURL("repQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

