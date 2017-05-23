package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TransOrderDS2 extends DataSource{
	private static TransOrderDS2 instance;
	
	public TransOrderDS2(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField keyField=new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		setFields(keyField);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static TransOrderDS2 getInstance(String id,String tableName){
		if(instance==null){
			instance=new TransOrderDS2(id,tableName);
		}
		return instance;
	}
	
	public static TransOrderDS2 getInstance(String id){
		if(instance==null){
			instance=new TransOrderDS2(id,id);
		}
		return instance;
	}
}
