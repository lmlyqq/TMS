package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ScreenWinDS extends DataSource {
	private static ScreenWinDS instance=null;
	
	public static ScreenWinDS getInstance(String id){
		
        if(instance == null){
        	instance=new ScreenWinDS(id,id);
        }
        return instance;
		
	}
	
	public static ScreenWinDS getInstance(String id,String tableName){
		if(instance == null){
        	instance=new ScreenWinDS(id,tableName);
        }
        return instance;
		
	}
	
	public ScreenWinDS (String id,String tableName){
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
