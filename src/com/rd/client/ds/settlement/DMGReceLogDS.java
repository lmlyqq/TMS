package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 核销表数据源
 * @author 
 *
 */
public class DMGReceLogDS extends DataSource{
	private static DMGReceLogDS instance = null;

	public static DMGReceLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new DMGReceLogDS(id, tableName);
		}
		return instance;
	}

	public static DMGReceLogDS getInstance(String id) {
		if (instance == null) {
			instance = new DMGReceLogDS(id, id);
		}
		return instance;
	}

	public DMGReceLogDS(String id, String tableName) {

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

