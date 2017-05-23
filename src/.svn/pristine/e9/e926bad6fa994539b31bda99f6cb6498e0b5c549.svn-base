package com.rd.client.ds.tms.sforder;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--原始订单--原始订单明细数据源
 * @author fanglm
 *
 */
public class SFOrderItemDS extends DataSource{

	private static SFOrderItemDS instance = null;

	public static SFOrderItemDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new SFOrderItemDS(id, tableName);
		}
		return instance;
	}

	public static SFOrderItemDS getInstance(String id) {
		if (instance == null) {
			instance = new SFOrderItemDS(id, id);
		}
		return instance;
	}

	public SFOrderItemDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField SFODR_NO = new DataSourceTextField("SFODR_NO",Util.TI18N.ODR_NO());//客户单号

		setFields(keyField,SFODR_NO);
		
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		//setShowPrompt(false);
	}

}
