package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 承运商结算管理-从表
 * @author wangjun
 *
 */
public class BmsShipmentDS extends DataSource{
	private static BmsShipmentDS instance = null;

	public static BmsShipmentDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BmsShipmentDS(id, tableName);
		}
		return instance;
	}

	public static BmsShipmentDS getInstance(String id) {
		if (instance == null) {
			instance = new BmsShipmentDS(id, id);
		}
		return instance;
	}

	public BmsShipmentDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO","调度单号");
		setFields(LOAD_NO);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

