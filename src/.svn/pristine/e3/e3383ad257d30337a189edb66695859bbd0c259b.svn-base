package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 时间管理 --主信息   数据源
 * @author wangjun
 *
 */
public class TimeTypeDS extends DataSource{
	private static TimeTypeDS instance = null;

	public static TimeTypeDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TimeTypeDS(id, tableName);
		}
		return instance;
	}

	public static TimeTypeDS getInstance(String id) {
		if (instance == null) {
			instance = new TimeTypeDS(id, id);
		}
		return instance;
	}

	public TimeTypeDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

//		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO());//调度单号
		setFields(keyField);

		setDataURL("basQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

