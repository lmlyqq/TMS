package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 审批设置数据源
 * @author Administrator
 *
 */
public class ApproveSetDS extends DataSource{

	private static ApproveSetDS instance = null;
	
	public static ApproveSetDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ApproveSetDS(id, tableName);
		}
		return instance;
	}

	public static ApproveSetDS getInstance(String id) {
		if (instance == null) {
			instance = new ApproveSetDS(id, id);
		}
		return instance;
	}
	
	public ApproveSetDS(String id, String tableName) {
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		setFields(keyField);
		setDataURL("tmsQueryServlet?ds_id=" + this.getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
