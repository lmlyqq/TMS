package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 应收补贴单数据源
 * @author cjt
 *
 */
public class RecAllowanceDS extends DataSource{
	
	private static RecAllowanceDS instance = null;

	public static RecAllowanceDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecAllowanceDS(id, tableName);
		}
		return instance;
	}

	public static RecAllowanceDS getInstance(String id) {
		if (instance == null) {
			instance = new RecAllowanceDS(id, id);
		}
		return instance;
	}

	public RecAllowanceDS(String id, String tableName) {

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
