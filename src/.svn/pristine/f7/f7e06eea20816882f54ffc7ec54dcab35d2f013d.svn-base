package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--托运单管理--托运单明细数据源
 * @author fanglm
 *
 */
public class TranOrderItemDS extends DataSource{

	private static TranOrderItemDS instance = null;

	public static TranOrderItemDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TranOrderItemDS(id, tableName);
		}
		return instance;
	}

	public static TranOrderItemDS getInstance(String id) {
		if (instance == null) {
			instance = new TranOrderItemDS(id, id);
		}
		return instance;
	}

	public TranOrderItemDS(String id, String tableName) {

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
		
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		//setShowPrompt(false);
	}

}
