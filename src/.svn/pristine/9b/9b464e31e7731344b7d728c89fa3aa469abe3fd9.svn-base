package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 客户服务-->订单动态监控-->【托运单】界面的【跟踪历史信息】
 * @author wangjun
 *
 */

public class Transact_logDS extends DataSource{

	private static Transact_logDS instance = null;

	public static Transact_logDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new Transact_logDS(id, tableName);
		}
		return instance;
	}

	public static Transact_logDS getInstance(String id) {
		if (instance == null) {
			instance = new Transact_logDS(id, id);
		}
		return instance;
	}

	public Transact_logDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
		
	}
}
