package com.rd.client.ds.tms.sforder;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--顺丰原始订单数据源
 * @author fanglm
 *
 */
public class SFOrderDS extends DataSource{

	private static SFOrderDS instance = null;

	public static SFOrderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new SFOrderDS(id, tableName);
		}
		return instance;
	}

	public static SFOrderDS getInstance(String id) {
		if (instance == null) {
			instance = new SFOrderDS(id, id);
		}
		return instance;
	}

	public SFOrderDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号

		setFields(keyField,CUSTOM_ODR_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
