package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 用户-客户对应关系视图(托运单管理使用)
 * @author fanglm
 *
 */
public class UserCustDS extends DataSource{

	private static UserCustDS instance = null;

	public static UserCustDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new UserCustDS(id, tableName);
		}
		return instance;
	}

	public static UserCustDS getInstance(String id) {
		if (instance == null) {
			instance = new UserCustDS(id, id);
		}
		return instance;
	}

	public UserCustDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField CUSTOMER_CODE = new DataSourceTextField("CUSTOMER_CODE",
				Util.TI18N.CUSTOMER_CODE());//费用类别
		
		DataSourceTextField CUSTOMER_CNAME = new DataSourceTextField("CUSTOMER_CNAME",
				Util.TI18N.CUSTOMER_CNAME());//费用类别
		
		setFields(keyField,CUSTOMER_CODE, CUSTOMER_CNAME);
	
		setDataURL("basQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
