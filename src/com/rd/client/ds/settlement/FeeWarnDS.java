package com.rd.client.ds.settlement;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class FeeWarnDS extends DataSource{
	private static FeeWarnDS instance=null;
	
	private FeeWarnDS(String id,String table){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("table", table, false);
		
		DataSourceTextField keyField=new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField DOC_TYP=new DataSourceTextField("DOC_TYP",Util.TI18N.DOC_TYP());
		setFields(keyField,DOC_TYP);
		
		setDataURL("settQueryServlet?is_curr_page=true&ds_id=" + getID());
		
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static FeeWarnDS getInstance(String id){
		if(instance == null){
			instance = new FeeWarnDS(id,id);
		}
		return instance;
	}
	
	public static FeeWarnDS getInstance(String id,String table){
		if(instance == null){
			instance = new FeeWarnDS(id,table);
		}
		return instance;
	}
}
