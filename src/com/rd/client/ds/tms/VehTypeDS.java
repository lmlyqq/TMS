package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 可用车型数据源
 * @author yuanlei
 *
 */
public class VehTypeDS extends DataSource{
	private static VehTypeDS instance = null;

	public static VehTypeDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new VehTypeDS(id, tableName);
		}
		return instance;
	}

	public static VehTypeDS getInstance(String id) {
		if (instance == null) {
			instance = new VehTypeDS(id, id);
		}
		return instance;
	}

	public VehTypeDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

//		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO());//调度单号
		setFields(keyField);

		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

