package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 供应商结算管理-汇总数据源
 * @author wangjun
 *
 */
public class SupFeeCoutWinlDS extends DataSource{
	private static SupFeeCoutWinlDS instance = null;

	public static SupFeeCoutWinlDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new SupFeeCoutWinlDS(id, tableName);
		}
		return instance;
	}

	public static SupFeeCoutWinlDS getInstance(String id) {
		if (instance == null) {
			instance = new SupFeeCoutWinlDS(id, id);
		}
		return instance;
	}

	public SupFeeCoutWinlDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField SKU = new DataSourceTextField("SKU", Util.TI18N.SKU(), 70);
		DataSourceTextField SKU_NAME = new DataSourceTextField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);

		setFields(keyField,SKU,SKU_NAME);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

