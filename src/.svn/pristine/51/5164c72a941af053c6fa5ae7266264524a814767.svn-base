package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料  --客户管理---运输信息--订单类型
 * @author fanglm
 *
 */
public class CustOrdDS extends DataSource {
	
	private static CustOrdDS instance = null;

    public static CustOrdDS getInstance(String id) {
        if (instance == null) {
            instance = new CustOrdDS(id, id);
        }
        return instance;
    }
    
    public static CustOrdDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new CustOrdDS(id, tableName);
        }
        return instance;
    }
    public CustOrdDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		setFields(keyField);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
