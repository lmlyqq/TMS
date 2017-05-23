package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 系统管理->用户配置->经销商信息
 * @author cjt
 *
 */
public class SystemUserDs extends DataSource{

	private static SystemUserDs instance = null;
	
	public static SystemUserDs getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SystemUserDs(id, tableName);
        }
        return instance;
    }
    public SystemUserDs(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        DataSourceTextField PRI_SYSTEM = new DataSourceTextField("PRI_SYSTEM", "PRI_SYSTEM");
        DataSourceTextField USER_ID = new DataSourceTextField("USER_ID", "USER_ID");
        setFields(keyField,PRI_SYSTEM,USER_ID);
        setDataURL("sysQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
    }
	
}
