package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料  --客户管理
 * @author fanglm
 *
 */
public class CustomerDS extends DataSource {
	
	private static CustomerDS instance = null;

    public static CustomerDS getInstance(String id) {
        if (instance == null) {
            instance = new CustomerDS(id, id);
        }
        return instance;
    }
    
    public static CustomerDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new CustomerDS(id, tableName);
        }
        return instance;
    }
    public CustomerDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName("BAS_CUSTOMER");
        setAttribute("tableName", "BAS_CUSTOMER", false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		// name
		DataSourceTextField CUSTOMER_CODE = new DataSourceTextField("CUSTOMER_CODE", Util.TI18N.CUSTOMER_CODE());
		DataSourceTextField CUSTOMER_CNAME = new DataSourceTextField("CUSTOMER_CNAME", Util.TI18N.CUSTOMER_CNAME());
		DataSourceTextField CUSTOMER_ENAME = new DataSourceTextField("CUSTOMER_ENAME",Util.TI18N.CUSTOMER_ENAME());
		DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME",Util.TI18N.SHORT_NAME());
		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
		DataSourceTextField PARENT_CUSTOMER_ID = new DataSourceTextField("PARENT_CUSTOMER_ID",Util.TI18N.PARENT_CUSTOMER_ID());
		DataSourceTextField MAINT_ORG_ID = new DataSourceTextField("MAINT_ORG_ID",Util.TI18N.MAINT_ORG_ID());
		DataSourceBooleanField TRANSPORT_FLAG = new DataSourceBooleanField("TRANSPORT_FLAG",Util.TI18N.TRANSPORT_FLAG());
		DataSourceBooleanField WAREHOUSE_FLAG = new DataSourceBooleanField("WAREHOUSE_FLAG",Util.TI18N.WAREHOUSE_FLAG());
		DataSourceBooleanField PAYER_FLAG = new DataSourceBooleanField("PAYER_FLAG",Util.TI18N.PAYER_FLAG());
		DataSourceBooleanField CUSTOMER_FLAG = new DataSourceBooleanField("CUSTOMER_FLAG",Util.TI18N.CUSTOMER_FLAG());
		DataSourceBooleanField ENABLE_FLAG = new DataSourceBooleanField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());

		setFields(keyField, CUSTOMER_CODE, CUSTOMER_CNAME,CUSTOMER_ENAME,SHORT_NAME,HINT_CODE,
				PARENT_CUSTOMER_ID,MAINT_ORG_ID,TRANSPORT_FLAG, WAREHOUSE_FLAG, PAYER_FLAG,CUSTOMER_FLAG,ENABLE_FLAG);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
