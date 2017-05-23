package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料-->时间管理-->FormulaDS
 * @author wangjun
 *
 */

public class FormulaDS extends DataSource {

	private static FormulaDS instance = null;

	public static FormulaDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new FormulaDS(id, tableName);
		}
		return instance;
	}

	public FormulaDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOMER_ID = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER(), 140);
		DataSourceTextField TIME_TYPE = new DataSourceTextField("TIME_TYPE",Util.TI18N.TIME_TYPE(),120);//时间类别
		DataSourceTextField DOC_TYPE = new DataSourceTextField("DOC_TYPE",Util.TI18N.DOC_TYPE(),70);//单据类型
		
		setFields(keyField,CUSTOMER_ID,TIME_TYPE,DOC_TYPE);
		setDataURL("basQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
