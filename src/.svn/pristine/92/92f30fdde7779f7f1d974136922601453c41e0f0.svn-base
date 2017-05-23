package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class CustomActLogDS extends DataSource {
	private static CustomActLogDS instance=null;
	
	public CustomActLogDS(String id,String tableName){
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		setAttribute("tableName",tableName,false);
		DataSourceTextField keyField=new DataSourceTextField("ID","ID",10,false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField ADDTIME=new DataSourceTextField("ADDTIME","日期");
		DataSourceTextField STATUS=new DataSourceTextField("STATUS",Util.TI18N.STATUS());
		DataSourceTextField DESCR=new DataSourceTextField("DESCR",Util.TI18N.NOTES());
		DataSourceTextField ADDWHO=new DataSourceTextField("ADDDWHO",Util.TI18N.ADDWHO());
		DataSourceTextField REFENENCE1=new DataSourceTextField("REFENENCE1",Util.TI18N.ADDWHO());
		DataSourceTextField ODR_NO=new DataSourceTextField("ODR_NO",Util.TI18N.ADDWHO());
		DataSourceTextField QNTY=new DataSourceTextField("QNTY",Util.TI18N.ADDWHO());
		DataSourceTextField TEMPERATURE=new DataSourceTextField("TEMPERATURE",Util.TI18N.ADDWHO());
		DataSourceTextField PLATE_NO=new DataSourceTextField("PLATE_NO",Util.TI18N.ADDWHO());
		DataSourceTextField LOAD_NO=new DataSourceTextField("LOAD_NO",Util.TI18N.ADDWHO());
		DataSourceTextField NOTES=new DataSourceTextField("NOTES",Util.TI18N.ADDWHO());
		
		setFields(keyField,REFENENCE1,ODR_NO,STATUS,QNTY,TEMPERATURE,PLATE_NO,ADDTIME,ADDWHO,NOTES,LOAD_NO,DESCR);
		setDataURL("tmsQueryServlet?ds_id="+getID());
		setClientOnly(false);
		setShowPrompt(false);
		
	}
	
	public static CustomActLogDS getInstance(String id,String tableName){
		if(instance==null){
			instance=new CustomActLogDS(id,tableName);
		}
		return instance;
	}
	
	public static CustomActLogDS getInstance(String id){
		if(instance==null){
			instance=new CustomActLogDS(id,id);
		}
		return instance;
	}
}
