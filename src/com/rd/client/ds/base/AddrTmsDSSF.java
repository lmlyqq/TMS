package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 
 * @author xuweibin
 *
 */
public class AddrTmsDSSF extends DataSource{
private static AddrTmsDSSF instance=null;
	
	public static AddrTmsDSSF getInstance(String id){
		if (instance==null){
			instance=new AddrTmsDSSF(id,id);
		}
		return instance;
	}
	public static AddrTmsDSSF getInstance(String id,String tableName){
		if (instance==null){
			instance=new AddrTmsDSSF(id,tableName);
		}
		return instance;
	}
	
	public AddrTmsDSSF(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName",tableName,false);
		//name
		DataSourceTextField keyField=new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setRequired(true);
		keyField.setHidden(true);
		DataSourceTextField SF_ADDR_CODE=new DataSourceTextField("ADDR_CODE",Util.TI18N.ADDR_CODE());
		DataSourceTextField SF_ADDR_NAME=new DataSourceTextField("ADDR_NAME",Util.TI18N.ADDR_NAME());
		DataSourceTextField SF_AREA_NAME=new DataSourceTextField("AREA_NAME2",Util.TI18N.PROVINCE());
		DataSourceTextField TMS_ADDR_CODE=new DataSourceTextField("TMS_ADDR_CODE",Util.TI18N.ADDR_CODE());
		DataSourceTextField TMS_ADDR_NAME=new DataSourceTextField("TMS_ADDR_NAME",Util.TI18N.ADDR_NAME());
		DataSourceTextField TMS_ADDR_ID=new DataSourceTextField("TMS_ADDR_ID","冷运网点ID");
		DataSourceTextField SF_UNIT_CODE=new DataSourceTextField("UNIT_CODE","单元区域");
		
		setFields(keyField,SF_ADDR_CODE,SF_ADDR_NAME,SF_AREA_NAME,TMS_ADDR_CODE,TMS_ADDR_NAME,TMS_ADDR_ID,SF_UNIT_CODE);
		
		setDataURL("basQueryServlet?ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
