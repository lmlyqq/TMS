package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ShopRouteManifestDS extends DataSource{
	private static ShopRouteManifestDS instance = null;

	public static ShopRouteManifestDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ShopRouteManifestDS(id, tableName);
		}
		return instance;
	}

	public static ShopRouteManifestDS getInstance(String id) {
		if (instance == null) {
			instance = new ShopRouteManifestDS(id, id);
		}
		return instance;
	}

	public ShopRouteManifestDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO(), 80);
		DataSourceTextField CUSTOMER_NAME = new DataSourceTextField("CUSTOMER_NAME", Util.TI18N.CUSTOMER_NAME(), 120);
		DataSourceTextField ROUTE_NAME = new DataSourceTextField("ROUTE_NAME", Util.TI18N.ROUTE_NAME(), 120);
		DataSourceTextField TOTL_DISTANCE = new DataSourceTextField("TOTL_DISTANCE", "里程", 80);
		DataSourceTextField TOTAL_AMOUNT = new DataSourceTextField("TOTAL_AMOUNT", "金额", 120);
		DataSourceTextField DEPART_TIME = new DataSourceTextField("DEPART_TIME", Util.TI18N.LOAD_TIME(), 100);

		setFields(keyField,LOAD_NO,CUSTOMER_NAME,ROUTE_NAME,TOTL_DISTANCE,TOTAL_AMOUNT,DEPART_TIME);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
