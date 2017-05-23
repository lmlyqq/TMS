package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**费用修改日志
 * 
 * @author 
 *
 */
public class BillModifyLogDS extends DataSource{
	private static BillModifyLogDS instance = null;

	public static BillModifyLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillModifyLogDS(id, tableName);
		}
		return instance;
	}

	public static BillModifyLogDS getInstance(String id) {
		if (instance == null) {
			instance = new BillModifyLogDS(id, id);
		}
		return instance;
	}

	public BillModifyLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField DOC_NO = new DataSourceTextField("DOC_NO", "DOC_NO", 10,
				false);
		//keyField.setPrimaryKey(true);
		//keyField.setHidden(true);		
		setFields(DOC_NO);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

