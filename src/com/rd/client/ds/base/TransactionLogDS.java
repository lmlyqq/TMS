package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class TransactionLogDS extends DataSource {
	private static TransactionLogDS instance = null;
	
	public static TransactionLogDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new TransactionLogDS(id, tableName);
        }
        return instance;
    }
    public TransactionLogDS(String id, String tableName) {
    	setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        //DataSourceTextField DOC_TYP = new DataSourceTextField("DOC_TYP", Util.TI18N.DOC_TYP());
        DataSourceTextField DOC_NO = new DataSourceTextField("DOC_NO", Util.TI18N.DOC_NO());
        DataSourceTextField ADDTIME = new DataSourceTextField("ADDTIME", Util.TI18N.ADDTIME());
        DataSourceTextField ADDWHO = new DataSourceTextField("ADDWHO", Util.TI18N.ADDWHO());
        DataSourceTextField NOTES = new DataSourceTextField("NOTES", Util.TI18N.NOTES());
        
        setFields(keyField,DOC_NO,ADDTIME,ADDWHO,NOTES);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }

}
