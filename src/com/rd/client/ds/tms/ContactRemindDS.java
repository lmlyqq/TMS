package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 系统提醒——合同到期提醒
 * @author 
 *
 */
public class ContactRemindDS extends DataSource{

	private static ContactRemindDS instance = null;

	public static ContactRemindDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ContactRemindDS(id, tableName);
		}
		return instance;
	}

	public static ContactRemindDS getInstance(String id) {
		if (instance == null) {
			instance = new ContactRemindDS(id, id);
		}
		return instance;
	}

	public ContactRemindDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CONTACT_NO = new DataSourceTextField("CONTACT_NO","合同号");

		setFields(keyField, CONTACT_NO);
	
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
