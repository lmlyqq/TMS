package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 客户服务-->订单动态监控-->【在途信息】
 * @author wangjun
 *
 */
public class ShmpTrackTraceDS extends DataSource {
	private static ShmpTrackTraceDS instance = null;

	public static ShmpTrackTraceDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ShmpTrackTraceDS(id, tableName);
		}
		return instance;
	}

	public static ShmpTrackTraceDS getInstance(String id) {
		if (instance == null) {
			instance = new ShmpTrackTraceDS(id, id);
		}
		return instance;
	}

	public ShmpTrackTraceDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		setFields(keyField);
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
//     	setShowPrompt(false);
	}

}
