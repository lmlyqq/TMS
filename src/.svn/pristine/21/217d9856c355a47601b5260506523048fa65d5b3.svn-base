package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理-->gps车辆信息
 * @author wangjun
 *
 */
public class EsbGpsDS extends DataSource{

	private static EsbGpsDS instance = null;

	public static EsbGpsDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new EsbGpsDS(id, tableName);
		}
		return instance;
	}

	public static EsbGpsDS getInstance(String id) {
		if (instance == null) {
			instance = new EsbGpsDS(id, id);
		}
		return instance;
	}

	public EsbGpsDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField VEHICLE = new DataSourceTextField("VEHICLE",
				"车牌号");
		DataSourceTextField GPS_TIME = new DataSourceTextField("GPS_TIME",
				"跟踪时间");
	

		
		setFields(keyField, VEHICLE, GPS_TIME);
	
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
