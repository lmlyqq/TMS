package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料->线路管理(BAS_ROUTER_DETAIL)
 * @author yuanlei
 *
 */
public class RouteDetailDS extends DataSource {

    private static RouteDetailDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static RouteDetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new RouteDetailDS(id, tableName);
        }
        return instance;
    }
    public static RouteDetailDS getInstance(String id) {
        if (instance == null) {
            instance = new RouteDetailDS(id, id);
        }
        return instance;
    }
    public RouteDetailDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        setFields(keyField);
        
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}