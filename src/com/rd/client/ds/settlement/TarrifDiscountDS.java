package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 费用管理->计费管理->供应商（客户）计费协议-折扣附件
 * @author wangjun
 *
 */
public class TarrifDiscountDS extends DataSource{
	private static TarrifDiscountDS instance = null;

	public static TarrifDiscountDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TarrifDiscountDS(id, tableName);
		}
		return instance;
	}

	public static TarrifDiscountDS getInstance(String id) {
		if (instance == null) {
			instance = new TarrifDiscountDS(id, id);
		}
		return instance;
	}

	public TarrifDiscountDS(String id, String tableName) {

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

