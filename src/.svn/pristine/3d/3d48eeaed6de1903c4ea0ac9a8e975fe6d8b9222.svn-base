package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 调度单数据源
 * @author yuanlei
 *
 */
public class PayDeductDS extends DataSource{
	private static PayDeductDS instance = null;

	public static PayDeductDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PayDeductDS(id, tableName);
		}
		return instance;
	}

	public static PayDeductDS getInstance(String id) {
		if (instance == null) {
			instance = new PayDeductDS(id, id);
		}
		return instance;
	}

	public PayDeductDS(String id, String tableName) {

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
	
		setFields(keyField, LOAD_NO, SUPLR_ID);

		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

