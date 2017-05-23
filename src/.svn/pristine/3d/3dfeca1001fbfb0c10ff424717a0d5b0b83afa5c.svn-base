package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 报表管理-->财务报表-->运费支付汇总数据源
 *
 */
public class R_TRANS_PAY_SUM_DS extends DataSource{
	private static R_TRANS_PAY_SUM_DS instance = null;

	public static R_TRANS_PAY_SUM_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_TRANS_PAY_SUM_DS(id, tableName);
		}
		return instance;
	}

	public static R_TRANS_PAY_SUM_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_TRANS_PAY_SUM_DS(id, id);
		}
		return instance;
	}

	public R_TRANS_PAY_SUM_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField SUPLR_NAME =new DataSourceTextField("SUPLR_NAME",Util.TI18N.SUPLR_NAME());
		
		setFields(keyField,SUPLR_NAME);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

