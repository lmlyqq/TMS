package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 费用管理---结算管理-应收调整账单-调整账单总表
 * @author 
 *
 */
public class RecAdjustDS extends DataSource{
	private static RecAdjustDS instance = null;

	public static RecAdjustDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecAdjustDS(id, tableName);
		}
		return instance;
	}

	public static RecAdjustDS getInstance(String id) {
		if (instance == null) {
			instance = new RecAdjustDS(id, id);
		}
		return instance;
	}

	public RecAdjustDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField ADJ_NO = new DataSourceTextField("ADJ_NO", "调整单号", 80);
		

		setFields(keyField,ADJ_NO);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

