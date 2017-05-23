package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理->基础资料->度量衡(BAS_MSRMNT)
 * @author yuanlei
 *
 */
public class MsrmntDS extends DataSource {

    private static MsrmntDS instance = null;

    /**
     * 
     * @author yuanlei
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static MsrmntDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new MsrmntDS(id, tableName);
        }
        return instance;
    }
    public MsrmntDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("MSRMNT_CODE");
        keyField.setHidden(true);
        
		DataSourceTextField MSRMNT_CODE = new DataSourceTextField("MSRMNT_CODE", Util.TI18N.MSRMNT());
		DataSourceTextField MSRMNT_NAME = new DataSourceTextField("MSRMNT_NAME", Util.TI18N.MSRMNT_NAME());
		DataSourceTextField ENABLE_FLAG = new DataSourceTextField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		
        setFields(keyField, MSRMNT_CODE, MSRMNT_NAME, ENABLE_FLAG);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}