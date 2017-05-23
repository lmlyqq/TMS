package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**应收扣款单
 * 
 * @author 
 *
 */
public class RecDeductDS extends DataSource{
	private static RecDeductDS instance = null;

	public static RecDeductDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecDeductDS(id, tableName);
		}
		return instance;
	}

	public static RecDeductDS getInstance(String id) {
		if (instance == null) {
			instance = new RecDeductDS(id, id);
		}
		return instance;
	}

	public RecDeductDS(String id, String tableName) {

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

