package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->列表配置->用户配置(左）
 * @author yuanlei
 *
 */
public class ModelDS extends DataSource {

    private static ModelDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static ModelDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new ModelDS(id, tableName);
        }
        return instance;
    }
    public ModelDS(String id, String tableName) {

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