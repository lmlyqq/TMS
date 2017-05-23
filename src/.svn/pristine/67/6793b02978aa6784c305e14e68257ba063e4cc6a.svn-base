package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AddrDS extends DataSource {
	private static AddrDS instance = null;

    public static AddrDS getInstance(String id) {
        if (instance == null) {
            instance = new AddrDS(id, id);
        }
        return instance;
    }
    
    public static AddrDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new AddrDS(id, tableName);
        }
        return instance;
    }
    public AddrDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName("BAS_ADDRESS");
        setAttribute("tableName", "BAS_ADDRESS", false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		// name
		DataSourceTextField ADDR_CODE = new DataSourceTextField("ADDR_CODE", Util.TI18N.ADDR_CODE());
		DataSourceTextField ADDR_NAME = new DataSourceTextField("ADDR_NAME", Util.TI18N.ADDR_NAME());
		DataSourceTextField ADDRESS = new DataSourceTextField("ADDRESS",Util.TI18N.ADDRESS());
		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
		DataSourceTextField DIRECT_FLAG = new DataSourceTextField("DIRECT_FLAG",Util.TI18N.DIRECT_FLAG());
		DataSourceTextField CUSTOMER_NAME = new DataSourceTextField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		DataSourceTextField WHSE_ID = new DataSourceTextField("WHSE_ID",Util.TI18N.WHSE_ID());
		DataSourceTextField REGION_ID_NAME = new DataSourceTextField("REGION_ID_NAME",Util.TI18N.REGION_ID());
		DataSourceTextField EXEC_ORG_ID_NAME = new DataSourceTextField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());

		setFields(keyField,ADDR_CODE,ADDR_NAME,ADDRESS ,HINT_CODE,DIRECT_FLAG,CUSTOMER_NAME,WHSE_ID,REGION_ID_NAME,EXEC_ORG_ID_NAME);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
