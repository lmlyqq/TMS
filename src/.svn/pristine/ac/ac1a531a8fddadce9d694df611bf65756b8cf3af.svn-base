package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 调度配载-->调度单信息-->右键【作业单相关信息】数据源
 * @author wangjun
 *
 */
public class UnShpmHeaderDS extends DataSource{
	private static UnShpmHeaderDS instance = null;

	public static UnShpmHeaderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new UnShpmHeaderDS(id, tableName);
		}
		return instance;
	}

	public static UnShpmHeaderDS getInstance(String id) {
		if (instance == null) {
			instance = new UnShpmHeaderDS(id, id);
		}
		return instance;
	}

	public UnShpmHeaderDS(String id, String tableName) {

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
		setShowPrompt(false);
	}

}

