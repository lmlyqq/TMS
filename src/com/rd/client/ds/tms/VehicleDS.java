package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 可用车辆数据源
 * @author yuanlei
 *
 */
public class VehicleDS extends DataSource{
	private static VehicleDS instance = null;

	public static VehicleDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new VehicleDS(id, tableName);
		}
		return instance;
	}

	public static VehicleDS getInstance(String id) {
		if (instance == null) {
			instance = new VehicleDS(id, id);
		}
		return instance;
	}

	public VehicleDS(String id, String tableName) {

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

