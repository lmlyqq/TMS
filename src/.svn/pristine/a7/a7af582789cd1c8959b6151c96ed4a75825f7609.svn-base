package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 系统管理->数据字典主表(BAS_CODEPROP)
 * @author yuanlei
 *
 */
public class CodePropDS extends DataSource {

    private static CodePropDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static CodePropDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new CodePropDS(id, tableName);
        }
        return instance;
    }
    public CodePropDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("PROP_CODE");
        keyField.setHidden(true);
		
		DataSourceTextField PROP_CODE = new DataSourceTextField("PROP_CODE", Util.TI18N.PROP_CODE());
		DataSourceTextField NAME_C = new DataSourceTextField("NAME_C", Util.TI18N.PROP_NAMEC());
		DataSourceTextField NAME_E = new DataSourceTextField("NAME_E", Util.TI18N.PROP_NAMEE());
		DataSourceTextField BIZ_TYPE = new DataSourceTextField("BIZ_TYPE", Util.TI18N.BIZ_TYPE());
        
        setFields(keyField, PROP_CODE, NAME_C, NAME_E, BIZ_TYPE);
        setDataURL("sysQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}