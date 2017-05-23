package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 应收调整单数据源
 * @author Administrator
 *
 */
public class BillRecjustDS extends DataSource{
	
	private static BillRecjustDS instance = null;

	public static BillRecjustDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillRecjustDS(id, tableName);
		}
		return instance;
	}

	public static BillRecjustDS getInstance(String id) {
		if (instance == null) {
			instance = new BillRecjustDS(id, id);
		}
		return instance;
	}

	public BillRecjustDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField ADJUST_NO = new DataSourceTextField("ADJUST_NO","调整单号");

		setFields(keyField,ADJUST_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
