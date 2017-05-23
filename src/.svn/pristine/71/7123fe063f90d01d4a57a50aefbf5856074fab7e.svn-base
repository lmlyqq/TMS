package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;



public class VCAreaDS extends DataSource{
	 private static VCAreaDS instance = null;

	    public static VCAreaDS getInstance(String tableName) {
	        if (instance == null) {
	            instance = new VCAreaDS(tableName);
	        }
	        return instance;
	    }
	    public VCAreaDS(String tableName) {
	    	setID(tableName);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName(tableName);
	        setAttribute("tableName", tableName, false);
	        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	        keyField.setPrimaryKey(true);
	        keyField.setHidden(true);
	        setFields(keyField);
	        
	        DataSourceTextField AREA_CODE = new DataSourceTextField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
	        AREA_CODE.setRequired(true);
	        
	        DataSourceTextField AREA_CNAME = new DataSourceTextField("AREA_CNAME", Util.TI18N.AREA_CNAME());
	        AREA_CNAME.setRequired(true);
	        
	        DataSourceTextField AREA_ENAME = new DataSourceTextField("AREA_ENAME", Util.TI18N.AREA_ENAME());
	        AREA_ENAME.setRequired(true);
	        
	        DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
	        SHORT_NAME.setRequired(true);
	        
	        DataSourceTextField SHOW_NAME = new DataSourceTextField("SHOW_NAME", Util.TI18N.SHORT_NAME(), 90);
	        SHOW_NAME.setRequired(true);
	        
	        DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
	        HINT_CODE.setRequired(true);
	        
	        setFields(AREA_CODE,AREA_CNAME,AREA_ENAME,SHORT_NAME,SHOW_NAME,HINT_CODE);

	        
	        setDataURL("basQueryServlet?ds_id="+getAttribute("tableName")+"&is_curr_page=true");
	        setClientOnly(false);
	    }
}

