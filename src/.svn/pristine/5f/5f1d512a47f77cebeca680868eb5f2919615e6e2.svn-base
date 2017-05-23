package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 应收调整单从表数据源
 * @author Administrator
 *
 */
public class BillRecAdjItemDS extends DataSource{
	
	private static BillRecAdjItemDS instance = null;

	public static BillRecAdjItemDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillRecAdjItemDS(id, tableName);
		}
		return instance;
	}

	public static BillRecAdjItemDS getInstance(String id) {
		if (instance == null) {
			instance = new BillRecAdjItemDS(id, id);
		}
		return instance;
	}

	public BillRecAdjItemDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField ADJUST_NO = new DataSourceTextField("ADJUST_NO","调整单号");

		setFields(keyField,ADJUST_NO);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
