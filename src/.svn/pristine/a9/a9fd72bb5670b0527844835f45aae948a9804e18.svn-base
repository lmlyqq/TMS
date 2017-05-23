package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BasAddrRDCView extends DataSource{
	private static BasAddrRDCView instance = null;
	
	private BasAddrRDCView(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setRequired(true);
		keyField.setHidden(true);
		
		setFields(keyField);
		
		setDataURL("basQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static BasAddrRDCView getInstance(String id){
		if(instance == null){
			instance = new BasAddrRDCView(id,id);
		}
		return instance;
	}
	
	public static BasAddrRDCView getInstance(String id,String tableName){
		if(instance == null){
			instance = new BasAddrRDCView(id,tableName);
		}
		return instance;
	}
}
