package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RDamageViewDS extends DataSource {
	private static RDamageViewDS instance = null;

	public static RDamageViewDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RDamageViewDS(id, tableName);
		}
		return instance;
	}

	public RDamageViewDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO","调度单号");
		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO","作业单号");
		setFields(LOAD_NO,SHPM_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
	}
}
