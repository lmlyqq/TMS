package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

//用户组管理->角色数据源

public class SysRoleDS extends DataSource {

	private static SysRoleDS instance = null;

	public static SysRoleDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new SysRoleDS(id, tableName);
		}
		return instance;
	}

	public static SysRoleDS getInstance(String id) {
		if (instance == null) {
			instance = new SysRoleDS(id, id);
		}
		return instance;
	}

	public SysRoleDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

//		DataSourceTextField ROLE_ID = new DataSourceTextField("ROLE_ID",
//				Util.TI18N.ROLE_ID());
//		DataSourceTextField UserGrop_ID = new DataSourceTextField("USERGROP_ID",
//				Util.TI18N.USERGROP_ID());
//		setFields(keyField, ROLE_ID, UserGrop_ID);

		setFields(keyField);
		setDataURL("sysQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
