package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**BMS-应付管理-应付请款单-核销信息

 * 
 * @author wangjun
 *
 */
public class PayLogDS extends DataSource{
	private static PayLogDS instance = null;

	public static PayLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PayLogDS(id, tableName);
		}
		return instance;
	}

	public static PayLogDS getInstance(String id) {
		if (instance == null) {
			instance = new PayLogDS(id, id);
		}
		return instance;
	}

	public PayLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		setFields(keyField);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

