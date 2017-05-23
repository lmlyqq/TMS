package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 结算管理-期初账单明细
 * @author wangjun
 *
 */
public class RecInitdetailsDS2 extends DataSource{
	private static RecInitdetailsDS2 instance = null;

	public static RecInitdetailsDS2 getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecInitdetailsDS2(id, tableName);
		}
		return instance;
	}

	public static RecInitdetailsDS2 getInstance(String id) {
		if (instance == null) {
			instance = new RecInitdetailsDS2(id, id);
		}
		return instance;
	}

	public RecInitdetailsDS2(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
//		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
//				false);
//		keyField.setPrimaryKey(true);
//		keyField.setHidden(true);
		
		DataSourceTextField INIT_NO = new DataSourceTextField("INIT_NO",
				"期初单号");
		DataSourceTextField PLATE_NO = new DataSourceTextField("PLATE_NO",
				"车牌号");
		

		setFields(INIT_NO, PLATE_NO);

		setDataURL("settQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

