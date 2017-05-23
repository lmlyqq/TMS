package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class LSCMLogDS extends DataSource {
	private static LSCMLogDS instance = null;
	
	public static LSCMLogDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new LSCMLogDS(id, tableName);
        }
        return instance;
    }
    public LSCMLogDS(String id, String tableName) {
    	setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        //DataSourceTextField DOC_TYP = new DataSourceTextField("DOC_TYP", Util.TI18N.DOC_TYP());
        DataSourceTextField LGMODULE = new DataSourceTextField("LGMODULE", Util.TI18N.DOC_NO());
        DataSourceTextField LGDATETIME = new DataSourceTextField("LGDATETIME", Util.TI18N.ADDTIME());
        DataSourceTextField LGMESSAGE = new DataSourceTextField("LGMESSAGE", Util.TI18N.ADDWHO());
        
        setFields(keyField,LGMODULE,LGDATETIME,LGMESSAGE);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }

}
