package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class ImportInfoDS extends DataSource{
	private static ImportInfoDS instance = null;
	
	private ImportInfoDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		//DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		//keyField.setPrimaryKey(true);
		//keyField.setRequired(true);
		//keyField.setHidden(true);
		
		DataSourceTextField EXCEL_NAME = new DataSourceTextField("EXCEL_NAME", "excel列名");
		DataSourceTextField FIELD_NAME = new DataSourceTextField("FIELD_NAME", "表列名");
		DataSourceTextField TABLE_NAME= new DataSourceTextField("TABLE_NAME","表名");
		DataSourceTextField SHOW_SEQ = new DataSourceTextField("SHOW_SEQ","顺序");
		//DataSourceTextField ORG_NAME = new DataSourceTextField("ORG_NAME","组织机构名称");
		
		setFields(EXCEL_NAME,FIELD_NAME,TABLE_NAME,SHOW_SEQ);
		
		setDataURL("basQueryServlet?is_curr_page=false&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static ImportInfoDS getInstance(String id){
		if(instance == null){
			instance = new ImportInfoDS(id,id);
		}
		return instance;
	}
	
	public static ImportInfoDS getInstance(String id,String tableName){
		if(instance == null){
			instance = new ImportInfoDS(id,tableName);
		}
		return instance;
	}
}
