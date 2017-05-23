package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RoleUserGroupDS extends DataSource {

    private static RoleUserGroupDS instance = null;

    /**
     * 
     * @author wangjun
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static RoleUserGroupDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new RoleUserGroupDS(id, tableName);
        }
        return instance;
    }
    
    public static RoleUserGroupDS getInstance(String id){
    	if(instance == null){
    		instance = new RoleUserGroupDS(id,id);
    	}
    	return instance;
    }
    public RoleUserGroupDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, true);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        setDataURL("sysQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
    }
}