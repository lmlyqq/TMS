package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**计费日志
 * 
 * @author 
 *
 */
public class BillLogDS extends DataSource{
	private static BillLogDS instance = null;

	public static BillLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillLogDS(id, tableName);
		}
		return instance;
	}

	public static BillLogDS getInstance(String id) {
		if (instance == null) {
			instance = new BillLogDS(id, id);
		}
		return instance;
	}

	public BillLogDS(String id, String tableName) {

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

