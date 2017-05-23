package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料->线路管理(BAS_ROUTER_HEADER)
 * @author yuanlei
 *
 */
public class RouteHeaderDS extends DataSource {

    private static RouteHeaderDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static RouteHeaderDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new RouteHeaderDS(id, tableName);
        }
        return instance;
    }
    public RouteHeaderDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("ROUTE_ID");
        keyField.setHidden(true);
        
		DataSourceTextField ROUTE_CODE = new DataSourceTextField("ROUTE_CODE", Util.TI18N.ROUTE_CODE());
		DataSourceTextField ROUTE_NAME = new DataSourceTextField("ROUTE_NAME", Util.TI18N.ROUTE_NAME());
		DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME", Util.TI18N.SHORT_NAME());	
		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE", Util.TI18N.HINT_CODE());
//		DataSourceTextField ENABLE_FLAG = new DataSourceTextField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		
        setFields(keyField, SHORT_NAME, ROUTE_NAME, ROUTE_CODE, HINT_CODE);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}