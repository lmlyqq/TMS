package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BasAddrAreaDS extends DataSource{
	private static BasAddrAreaDS instance = null;
	
	private BasAddrAreaDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setRequired(true);
		keyField.setHidden(true);
		
		DataSourceTextField ADDR_ID = new DataSourceTextField("ADDR_ID", "地址点ID");
		DataSourceTextField AREA_ID = new DataSourceTextField("AREA_ID", "行政区域ID");
		DataSourceTextField AREA_NAME = new DataSourceTextField("AREA_NAME","行政区域名称");
		DataSourceTextField ORG_ID = new DataSourceTextField("ORG_ID","组织机构ID");
		DataSourceTextField ORG_NAME = new DataSourceTextField("ORG_NAME","组织机构名称");
		
		setFields(keyField,ADDR_ID,AREA_ID,AREA_NAME,ORG_ID,ORG_NAME);
		
		setDataURL("basQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static BasAddrAreaDS getInstance(String id){
		if(instance == null){
			instance = new BasAddrAreaDS(id,id);
		}
		return instance;
	}
	
	public static BasAddrAreaDS getInstance(String id,String tableName){
		if(instance == null){
			instance = new BasAddrAreaDS(id,tableName);
		}
		return instance;
	}
}
