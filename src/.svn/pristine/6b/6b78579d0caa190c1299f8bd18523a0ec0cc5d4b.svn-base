package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**BMS-应付管理-应付请款单-请款单明细

 * 
 * @author wangjun
 *
 */
public class PayInitdetailDS extends DataSource{
	private static PayInitdetailDS instance = null;

	public static PayInitdetailDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PayInitdetailDS(id, tableName);
		}
		return instance;
	}

	public static PayInitdetailDS getInstance(String id) {
		if (instance == null) {
			instance = new PayInitdetailDS(id, id);
		}
		return instance;
	}

	public PayInitdetailDS(String id, String tableName) {

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

