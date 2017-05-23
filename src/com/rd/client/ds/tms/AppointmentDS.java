package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 系统提醒-->到货预约
 * @author 
 *
 */
public class AppointmentDS extends DataSource{

	private static AppointmentDS instance = null;

	public static AppointmentDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new AppointmentDS(id, tableName);
		}
		return instance;
	}

	public static AppointmentDS getInstance(String id) {
		if (instance == null) {
			instance = new AppointmentDS(id, id);
		}
		return instance;
	}

	public AppointmentDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO","托运单号");

		setFields(keyField, ODR_NO);
	
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
