package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  系统管理->数据字典从表(BAS_CODES)
 * @author yuanlei
 *
 */
public class CodeDS extends DataSource {

    private static CodeDS instance = null;

    public static CodeDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new CodeDS(id, tableName);
        }
        return instance;
    }
    public CodeDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        DataSourceTextField CODE = new DataSourceTextField("CODE", Util.TI18N.CODE());
        DataSourceTextField NAME_C = new DataSourceTextField("NAME_C", Util.TI18N.CODE_NAMEC());
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField,CODE,NAME_C);
        setDataURL("sysQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}