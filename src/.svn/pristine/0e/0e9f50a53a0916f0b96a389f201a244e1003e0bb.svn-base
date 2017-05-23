package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  运输管理->基础资料->度量衡从表(BAS_MSRMNT_UNIT)
 * @author yuanlei
 *
 */
public class MsrmntUnitDS extends DataSource {

    private static MsrmntUnitDS instance = null;

    public static MsrmntUnitDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new MsrmntUnitDS(id, tableName);
        }
        return instance;
    }
    public MsrmntUnitDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        DataSourceTextField msrmntField = new DataSourceTextField("MSRMNT_CODE", "MSRMNT_CODE", 10, false);
        msrmntField.setForeignKey("BAS_MSRMNT.MSRMNT_CODE");
        
        setFields(keyField, msrmntField);
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}