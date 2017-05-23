package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 换车记录从表数据源
 * @author Administrator
 *
 */
public class ChangeDownDS extends DataSource{

	private static ChangeDownDS instance = null;

	public static ChangeDownDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ChangeDownDS(id, tableName);
		}
		return instance;
	}

	public static ChangeDownDS getInstance(String id) {
		if (instance == null) {
			instance = new ChangeDownDS(id, id);
		}
		return instance;
	}
	
	public ChangeDownDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO1","LOAD_NO1", 10, false);//调度单号
		setFields(keyField, LOAD_NO);

		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
