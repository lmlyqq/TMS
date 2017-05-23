package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 数据源
 * @author wangjun
 *
 */
public class CustomReportDS extends DataSource{
	private static CustomReportDS instance = null;

	public static CustomReportDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new CustomReportDS(id, tableName);
		}
		return instance;
	}

	public static CustomReportDS getInstance(String id) {
		if (instance == null) {
			instance = new CustomReportDS(id, id);
		}
		return instance;
	}

	public CustomReportDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		setFields(keyField);

		setDataURL("repQueryServlet?is_curr_page=true&ds_id=" + getID()+"&tableName=" + tableName);
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public CustomReportDS(String id, String tableName,String[] cols) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		setFields(keyField);
		
		for(int i=0;i<cols.length;i++){
			addField(new DataSourceTextField(cols[i], cols[i]));
		}
		
		setDataURL("repQueryServlet?is_curr_page=true&ds_id=" + getID()+"&tableName=" + tableName);
		setClientOnly(false);
		setShowPrompt(false);
	}
}

