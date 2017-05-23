package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;



public class AreaDS1 extends DataSource{
	 private static AreaDS1 instance = null;

	    public static AreaDS1 getInstance(String id,String tableName) {
	        if (instance == null) {
	            instance = new AreaDS1(id,tableName);
	        }
	        return instance;
	    }
	    public AreaDS1(String id, String tableName) {
	    	setID(id);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName(tableName);
	        setAttribute("tableName", tableName, false);
	        
	        DataSourceTextField keyField = new DataSourceTextField("AREA_CODE");
	        keyField.setPrimaryKey(true);
	        keyField.setRequired(true);
	        keyField.setHidden(true);
	        
	        DataSourceTextField parentIdField = new DataSourceTextField("PARENT_AREA_ID", Util.TI18N.PARENT_ORG_NAME());
	        parentIdField.setForeignKey("AREA_CODE");
	        //parentIdField.setRequired(true);
	        parentIdField.setHidden(false);
	        parentIdField.setRootValue(1);
	        
	        DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
	        SHORT_NAME.setRequired(true);
	        
	        setFields(keyField,SHORT_NAME,parentIdField);

	        
	        setDataURL("basQueryServlet?ds_id="+getID());
	        setClientOnly(false);
	        setShowPrompt(false);
	    }
}

