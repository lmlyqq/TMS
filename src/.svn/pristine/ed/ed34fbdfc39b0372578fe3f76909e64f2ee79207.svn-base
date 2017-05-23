package com.rd.client.ds.report;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class RecPayCompareDS extends DataSource{
	private static RecPayCompareDS instance;
	
	private RecPayCompareDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO","订单");
		
		setFields(ODR_NO);
		
		setDataURL("repQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static RecPayCompareDS getInstance(String id){
		if (instance == null) {
			instance = new RecPayCompareDS(id, id);
		}
		return instance;
	}
	
	public static RecPayCompareDS getInstance(String id ,String tableName){
		if (instance == null) {
			instance = new RecPayCompareDS(id, tableName);
		}
		return instance;
	}
}
