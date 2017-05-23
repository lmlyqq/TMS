package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 应付补贴单数据源
 * @author Administrator
 *
 */
public class PayAllowanceDS extends DataSource{

	private static PayAllowanceDS instance = null;

	public static PayAllowanceDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PayAllowanceDS(id, tableName);
		}
		return instance;
	}

	public static PayAllowanceDS getInstance(String id) {
		if (instance == null) {
			instance = new PayAllowanceDS(id, id);
		}
		return instance;
	}

	public PayAllowanceDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO());//调度单号
//		DataSourceTextField SUPLR_ID = new DataSourceTextField("SUPLR_ID", Util.TI18N.SUPLR_ID());//二级窗口 SUPLR_ID_NAME
	
		setFields(keyField,LOAD_NO);

		setDataURL("settQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
