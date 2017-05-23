package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AddrDSSF extends DataSource{
	private static AddrDSSF instance=null;
	
	public static AddrDSSF getInstance(String id){
		if (instance==null){
			instance=new AddrDSSF(id,id);
		}
		return instance;
	}
	public static AddrDSSF getInstance(String id,String tableName){
		if (instance==null){
			instance=new AddrDSSF(id,tableName);
		}
		return instance;
	}
	
	public AddrDSSF(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName",tableName,false);
		//name
		DataSourceTextField keyField=new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setRequired(true);
		keyField.setHidden(true);
		
		DataSourceTextField ADDR_CODE=new DataSourceTextField("ADDR_CODE",Util.TI18N.ADDR_CODE());
		DataSourceTextField ADDR_NAME=new DataSourceTextField("ADDR_NAME",Util.TI18N.ADDR_NAME());
		DataSourceTextField AREA_ID=new DataSourceTextField("AREA_ID",Util.TI18N.PROVINCE());
		DataSourceTextField AREA_NAME=new DataSourceTextField("AREA_NAME",Util.TI18N.PROVINCE());
		DataSourceTextField AREA_ID2=new DataSourceTextField("AREA_ID2",Util.TI18N.CITY());
		DataSourceTextField AREA_NAME2=new DataSourceTextField("AREA_NAME2",Util.TI18N.CITY());
		DataSourceTextField AREA_ID3=new DataSourceTextField("AREA_ID3",Util.TI18N.AREA_ID());
		DataSourceTextField AREA_NAME3=new DataSourceTextField("AREA_NAME3",Util.TI18N.AREA_NAME());
		DataSourceTextField HINT_CODE=new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
		DataSourceTextField ADDRESS=new DataSourceTextField("ADDRESS",Util.TI18N.ADDRESS());
		DataSourceTextField EXEC_ORG_ID=new DataSourceTextField("EXEC_ORG_ID",Util.TI18N.EXEC_ORG_ID());
		DataSourceBooleanField ENABLE_FLAG=new DataSourceBooleanField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		DataSourceTextField ADDR_TYPE=new DataSourceTextField("ADDR_TYPE",Util.TI18N.ADDR_TYP());
		DataSourceTextField LEVEL1_CODE=new DataSourceTextField("LEVEL1_CODE",Util.TI18N.LEVEL1_CODE());
		DataSourceTextField LEVEL2_CODE=new DataSourceTextField("LEVEL2_CODE",Util.TI18N.LEVEL2_CODE());
		DataSourceTextField LEVEL3_CODE=new DataSourceTextField("LEVEL3_CODE",Util.TI18N.LEVEL3_CODE());
		
		setFields(keyField,ADDR_CODE,ADDR_NAME,AREA_ID,AREA_NAME,AREA_ID2,AREA_NAME2,AREA_ID3,AREA_NAME3,HINT_CODE,ADDRESS,
				EXEC_ORG_ID,ENABLE_FLAG,ADDR_TYPE,LEVEL1_CODE,LEVEL2_CODE,LEVEL3_CODE);
		
		setDataURL("basQueryServlet?ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
