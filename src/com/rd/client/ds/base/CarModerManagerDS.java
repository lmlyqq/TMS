package com.rd.client.ds.base;


import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 车型管理数据源
 * @author lijun
 *
 */
public class CarModerManagerDS  extends DataSource{
	 private static CarModerManagerDS instance = null;

	    public static CarModerManagerDS getInstance(String id) {
	        if (instance == null) {
	            instance = new CarModerManagerDS(id,id);
	        }
	        return instance;
	    }
	    
	    public static CarModerManagerDS getInstance(String id,String tableName){
	    	if(instance == null){
	    		instance = new CarModerManagerDS(id, tableName);
	    	}
	    	return instance;
	    }
	    
	    public CarModerManagerDS(String id,String tableName){
	    	setID(id);
	    	setDataFormat(DSDataFormat.JSON);
	    	//setTableName(tableName);
	    	setAttribute("tableName", tableName, false);
	    	
	    	DataSourceTextField textField = new DataSourceTextField("ID", "ID", 10, false);
	    	textField.setPrimaryKey(true);
	    	textField.setRequired(true);
	    	textField.setHidden(true);
	    	
	    	DataSourceTextField VEHICLE_TYPE = new DataSourceTextField("VEHICLE_TYPE",Util.TI18N.VEHICLE_TYPE());
	    	DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
	    	
	    	setFields(textField,VEHICLE_TYPE,HINT_CODE);
	    	setDataURL("basQueryServlet?ds_id="+getID());
	    	setClientOnly(false);
	        setShowPrompt(false);
	    }
}
