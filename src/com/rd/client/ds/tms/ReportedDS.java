package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 【车辆上报】已报车型车辆数据源
 * @author yuanlei
 *
 */
public class ReportedDS extends DataSource{
	private static ReportedDS instance = null;

	public static ReportedDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ReportedDS(id, tableName);
		}
		return instance;
	}

	public static ReportedDS getInstance(String id) {
		if (instance == null) {
			instance = new ReportedDS(id, id);
		}
		return instance;
	}

	public ReportedDS(String id, String tableName) {
		
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

		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

