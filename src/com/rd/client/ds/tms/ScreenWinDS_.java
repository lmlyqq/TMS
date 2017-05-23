package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ScreenWinDS_ extends DataSource {
	private static ScreenWinDS_ instance=null;
	
	public static ScreenWinDS_ getInstance(String id){
		
        if(instance == null){
        	instance=new ScreenWinDS_(id,id);
        }
        return instance;
		
	}
	
	public static ScreenWinDS_ getInstance(String id,String tableName){
		if(instance == null){
        	instance=new ScreenWinDS_(id,tableName);
        }
        return instance;
		
	}
	
	public ScreenWinDS_ (String id,String tableName){
		setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
        
        setDataURL("tmsQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
	}
}
