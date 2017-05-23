package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料->运输服务
 * 
 * @author wangjun
 * 
 */
public class TransportServiceDS extends DataSource {

	private static TransportServiceDS instance = null;

	public static TransportServiceDS getInstance(String id, String tablename) {
		// TODO Auto-generated method stub
		if (instance == null) {
			instance = new TransportServiceDS(id, tablename);
		}

		return instance;
	}

	public TransportServiceDS(String id, String tableName) {
     
		setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        setFields(keyField);
	        
        /**
         * srvc_name  运输服务名
         * srvc_ename 英文名称
         * short_name 简称
         * hint_code  助记码
         * 
         */
		 
		DataSourceTextField srvc_name =new DataSourceTextField("SRVC_NAME",Util.TI18N.SRVC_NAME());
		DataSourceTextField srvc_ename =new DataSourceTextField("SRVC_ENAME",Util.TI18N.SRVC_ENAME()); 
		DataSourceTextField short_name =new DataSourceTextField("SHORT_NAME",Util.TI18N.SHORT_NAME()); 
		DataSourceTextField hint_code=new DataSourceTextField("HINT_COD",Util.TI18N.HINT_CODE()); 
		
		setFields(srvc_name,srvc_ename,short_name,hint_code);
		setDataURL("basQueryServlet?ds_id="+getID());
	    setClientOnly(false);
	    setShowPrompt(false);
	}

}
