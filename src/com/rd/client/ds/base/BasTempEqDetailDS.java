package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料->温控设备
 * @author yuanlei
 *
 */
public class BasTempEqDetailDS extends DataSource {

    private static BasTempEqDetailDS instance = null;

    /**
     * 
     * @author lml
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static BasTempEqDetailDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasTempEqDetailDS(id, tableName);
        }
        return instance;
    }
    public BasTempEqDetailDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("ID");
        keyField.setHidden(true);
		
        DataSourceTextField EQUIP_NO = new DataSourceTextField("EQUIP_NO", Util.TI18N.EQUIP_NO());
//		DataSourceTextField PURCHASE_DATE = new DataSourceTextField("PURCHASE_DATE", Util.TI18N.PURCHASE_DATE());
        
        setFields(keyField, EQUIP_NO);
        setDataURL("basQueryServlet?is_curr_page=true&ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}