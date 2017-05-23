package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 财务管理--计费管理--协议规则信息
 * @author fangliangmeng
 *
 */
public class TariffRuleDS extends DataSource{
	private static TariffRuleDS instance = null;

	public static TariffRuleDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TariffRuleDS(id, tableName);
		}
		return instance;
	}

	public static TariffRuleDS getInstance(String id) {
		if (instance == null) {
			instance = new TariffRuleDS(id, id);
		}
		return instance;
	}

	public TariffRuleDS(String id, String tableName) {

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

