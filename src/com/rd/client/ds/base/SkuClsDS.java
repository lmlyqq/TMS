package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SkuClsDS extends DataSource {
	 private static SkuClsDS instance = null;

	    public static SkuClsDS getInstance(String tableName) {
	        if (instance == null) {
	            instance = new SkuClsDS(tableName,tableName);
	        }
	        return instance;
	    }
	    public static SkuClsDS getInstance(String id,String tableName) {
	        if (instance == null) {
	            instance = new SkuClsDS(id,tableName);
	        }
	        return instance;
	    }
	    public SkuClsDS(String id,String tableName) {
	    	setID(id);
//	    	setTableName(tableName);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName("BAS_SKU_CLS");
	        setAttribute("tableName", tableName, false);
	        
	        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	        keyField.setPrimaryKey(true);
	        keyField.setRequired(true);
	        keyField.setHidden(true);
//	        setFields(keyField);
	        
	        DataSourceTextField code = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER_ID());
	        code.setRequired(true);
	        
	        DataSourceTextField cname = new DataSourceTextField("SKUCLS", Util.TI18N.SKU_CLS());
	        cname.setRequired(true);
	        
	        DataSourceTextField ename = new DataSourceTextField("DESCR_C", Util.TI18N.DESCRC());
	        ename.setRequired(true);
	        
	        DataSourceTextField name = new DataSourceTextField("DESCR_E", Util.TI18N.DESCRE());
	        name.setRequired(true);
	        
	        DataSourceTextField hint = new DataSourceTextField("FACTOR", Util.TI18N.FACTOR());
	        hint.setRequired(true);
	        
	        setFields(keyField,code,cname,ename,name,hint);

	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	    }
}
