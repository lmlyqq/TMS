package com.rd.client.ds.vehassist;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class InsPurchaseRecordDS extends DataSource {
	
	private static InsPurchaseRecordDS instance = null;

    public static InsPurchaseRecordDS getInstance(String id) {
        if (instance == null) {
            instance = new InsPurchaseRecordDS(id, id);
        }
        return instance;
    }
    
    public static InsPurchaseRecordDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new InsPurchaseRecordDS(id, tableName);
        }
        return instance;
    }
    
    public InsPurchaseRecordDS(String id, String tableName) {
    	
    	setID(id);
    	setDataFormat(DSDataFormat.JSON);
    	setAttribute("tableName", tableName, false);
         
    	DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
    	keyField.setPrimaryKey(true);
    	keyField.setRequired(true);
    	keyField.setHidden(true);
         
    	DataSourceTextField INS_NO = new DataSourceTextField("INS_NO", Util.TI18N.INS_NO());
    	DataSourceTextField PLATE_NO = new DataSourceTextField("PLATE_NO", Util.TI18N.PLATE_NO());
// 		DataSourceTextField INS_TYPE = new DataSourceTextField("INS_TYPE",Util.TI18N.INS_TYPE());
// 		DataSourceTextField INS_CLS = new DataSourceTextField("INS_CLS",Util.TI18N.INS_CLS());
// 		DataSourceTextField INS_COMPANY = new DataSourceTextField("INS_COMPANY",Util.TI18N.INS_COMPANY());
// 		DataSourceTextField INS_DOCNO = new DataSourceTextField("INS_DOCNO",Util.TI18N.INS_DOCNO());
// 		DataSourceTextField INS_FEE = new DataSourceTextField("INS_FEE",Util.TI18N.INS_FEE());
// 		DataSourceTextField INS_AMOUNT = new DataSourceTextField("INS_AMOUNT",Util.TI18N.INS_AMOUNT());
// 		DataSourceTextField NOTES = new DataSourceTextField("NOTES",Util.TI18N.NOTES());
 		
 		setFields(keyField,INS_NO,PLATE_NO);
    	
 		setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }

}
