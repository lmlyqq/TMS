package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SkuDS1 extends DataSource {
	private static SkuDS1 instance = null;

    public static SkuDS1 getInstance(String id) {
        if (instance == null) {
            instance = new SkuDS1(id, id);
        }
        return instance;
    }
    
    public static SkuDS1 getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SkuDS1(id, tableName);
        }
        return instance;
    }
    public SkuDS1(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName("BAS_SKU");
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		// name
		DataSourceTextField PACK = new DataSourceTextField("SKU_CLS", Util.TI18N.SKU_CLS());
		DataSourceTextField DESCR = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER_ID());
		DataSourceTextField SKU = new DataSourceTextField("SKU",Util.TI18N.SKU());
		DataSourceTextField SKU_CNAME = new DataSourceTextField("SKU_CNAME",Util.TI18N.SKU_CNAME());
		DataSourceTextField SKU_ATTR = new DataSourceTextField("SKU_ATTR",Util.TI18N.SKU_ATTR());
//		DataSourceTextField COMMON_FLAG = new DataSourceTextField("COMMON_FLAG",Util.TI18N.COMMON_FLAG());

		setFields(keyField, PACK, DESCR,SKU,SKU_CNAME,SKU_ATTR);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
