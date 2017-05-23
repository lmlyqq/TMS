package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料->服务范围(头表)
 * @author yuanlei
 *
 */
public class BasRangeHeadDS extends DataSource {

    private static BasRangeHeadDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static BasRangeHeadDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasRangeHeadDS(id, tableName);
        }
        return instance;
    }
    public BasRangeHeadDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("ID");
        keyField.setHidden(true);
		
		DataSourceTextField PROP_CODE = new DataSourceTextField("NAME", Util.TI18N.PROP_CODE());
		DataSourceTextField NAME_C = new DataSourceTextField("NAME_C", Util.TI18N.PROP_NAMEC());
		DataSourceTextField NAME_E = new DataSourceTextField("NAME_E", Util.TI18N.PROP_NAMEE());
		DataSourceTextField BIZ_TYPE = new DataSourceTextField("BIZ_TYPE", Util.TI18N.BIZ_TYPE());
        
        setFields(keyField, PROP_CODE, NAME_C, NAME_E, BIZ_TYPE);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}