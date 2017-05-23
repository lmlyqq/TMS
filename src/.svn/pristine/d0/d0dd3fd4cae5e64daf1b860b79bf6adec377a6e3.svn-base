package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理-->货损赔偿审核
 * @author 
 *
 */
public class ClaimApproveDS extends DataSource{

	private static ClaimApproveDS instance = null;

	public static ClaimApproveDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new ClaimApproveDS(id, tableName);
		}
		return instance;
	}

	public static ClaimApproveDS getInstance(String id) {
		if (instance == null) {
			instance = new ClaimApproveDS(id, id);
		}
		return instance;
	}

	public ClaimApproveDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOMER_ID = new DataSourceTextField("CUSTOMER_ID",
				"客户");//费用类别
		DataSourceTextField PLATE_NO = new DataSourceTextField("PLATE_NO",
				"车牌号");//费用属性
		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO",
				"调度单");//费用属性

		setFields(keyField, CUSTOMER_ID, PLATE_NO, LOAD_NO);
	
		setDataURL("settQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
