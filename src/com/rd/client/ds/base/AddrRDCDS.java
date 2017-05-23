package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AddrRDCDS extends DataSource{
	private static AddrRDCDS instance = null;
	
	private AddrRDCDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("TABLE", tableName, false);
		
		DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setRequired(true);
		keyField.setHidden(true);
		
		DataSourceTextField ADDR_CODE = new DataSourceTextField("ADDR_CODE", Util.TI18N.ADDR_CODE());
		DataSourceTextField ADDR_NAME = new DataSourceTextField("ADDR_NAME", Util.TI18N.ADDR_NAME());
		DataSourceTextField ADDRESS = new DataSourceTextField("ADDRESS",Util.TI18N.ADDRESS());
		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
		DataSourceTextField DIRECT_FLAG = new DataSourceTextField("DIRECT_FLAG",Util.TI18N.DIRECT_FLAG());
		DataSourceTextField CUSTOMER_NAME = new DataSourceTextField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		DataSourceTextField WHSE_ID = new DataSourceTextField("WHSE_ID",Util.TI18N.WHSE_ID());
		DataSourceTextField REGION_ID_NAME = new DataSourceTextField("REGION_ID_NAME",Util.TI18N.REGION_ID());
		DataSourceTextField EXEC_ORG_ID_NAME = new DataSourceTextField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		
		setFields(keyField,ADDR_CODE,ADDR_NAME,ADDRESS ,HINT_CODE,DIRECT_FLAG,CUSTOMER_NAME,WHSE_ID,REGION_ID_NAME,EXEC_ORG_ID_NAME);
		
		setDataURL("basQueryServlet?is_curr_page=true&ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
	public static AddrRDCDS getInstance(String id){
		if(instance == null){
			instance = new AddrRDCDS(id,id);
		}
		return instance;
	}
	
	public static AddrRDCDS getInstance(String id,String tableName){
		if(instance == null){
			instance = new AddrRDCDS(id,tableName);
		}
		return instance;
	}
}
