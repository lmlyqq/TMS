package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理-->托运单管理-->业务日志
 * @author fanglm
 * @createtime 2011-1-4 9:39
 *
 */

public class TransCustLogDS extends DataSource{

	private static TransCustLogDS instance = null;

	public static TransCustLogDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TransCustLogDS(id, tableName);
		}
		return instance;
	}

	public static TransCustLogDS getInstance(String id) {
		if (instance == null) {
			instance = new TransCustLogDS(id, id);
		}
		return instance;
	}

	public TransCustLogDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
		
	}
}
