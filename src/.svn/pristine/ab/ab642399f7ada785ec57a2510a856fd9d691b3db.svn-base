package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * U8接口错误日志
 * @author fanglm
 * @createtime 2011-1-11 10:12
 *
 */

public class U8InterLogDS extends DataSource{

	private static U8InterLogDS instance = null;

	public static U8InterLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new U8InterLogDS(id, tableName);
		}
		return instance;
	}

	public static U8InterLogDS getInstance(String id) {
		if (instance == null) {
			instance = new U8InterLogDS(id, id);
		}
		return instance;
	}

	public U8InterLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		setFields(keyField);
		
		DataSourceTextField DOC_NO =new DataSourceTextField("DOC_NO","编号");
		setFields(DOC_NO);
		
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
		
	}
}
