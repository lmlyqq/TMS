package com.rd.client.ds.rule;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 业务规则 --主信息   数据源
 * @author wangjun
 *
 */
public class RulHeaderDS extends DataSource{
	private static RulHeaderDS instance = null;

	public static RulHeaderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RulHeaderDS(id, tableName);
		}
		return instance;
	}

	public static RulHeaderDS getInstance(String id) {
		if (instance == null) {
			instance = new RulHeaderDS(id, id);
		}
		return instance;
	}

	public RulHeaderDS(String id, String tableName) {

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

		setDataURL("rulQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

