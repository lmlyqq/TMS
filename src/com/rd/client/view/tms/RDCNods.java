package com.rd.client.view.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RDCNods extends DataSource{
	private static RDCNods instance = null;
	
	private RDCNods(String id,String tableName){
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
	
	public static RDCNods getInstance(String id){
		if(instance == null){
			instance = new RDCNods(id, id);
		}
		return instance;
	}
	
	public static RDCNods getInstance(String id,String tableName){
		if(instance == null){
			instance = new RDCNods(id, tableName);
		}
		return instance;
	}
}
