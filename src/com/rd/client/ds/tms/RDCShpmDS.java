package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RDCShpmDS extends DataSource{
	private static RDCShpmDS instance = null;
	
	private RDCShpmDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("TABLE", tableName, false);
		
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);
		
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static RDCShpmDS getInstance(String id){
		if(instance == null){
			instance = new RDCShpmDS(id, id);
		}
		return instance;
	}
	
	public static RDCShpmDS getInstance(String id,String tableName){
		if(instance == null){
			instance = new RDCShpmDS(id, tableName);
		}
		return instance;
	}
}