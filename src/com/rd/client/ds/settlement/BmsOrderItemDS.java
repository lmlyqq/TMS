package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 应收管理-客户费用管理从表
 * @author fanglm
 *
 */
public class BmsOrderItemDS extends DataSource{

	private static BmsOrderItemDS instance = null;

	public static BmsOrderItemDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BmsOrderItemDS(id, tableName);
		}
		return instance;
	}

	public static BmsOrderItemDS getInstance(String id) {
		if (instance == null) {
			instance = new BmsOrderItemDS(id, id);
		}
		return instance;
	}

	public BmsOrderItemDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO",Util.TI18N.ODR_NO());//客户单号

		setFields(keyField,ODR_NO);
		
		setDataURL("settQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		//setShowPrompt(false);
	}

}
