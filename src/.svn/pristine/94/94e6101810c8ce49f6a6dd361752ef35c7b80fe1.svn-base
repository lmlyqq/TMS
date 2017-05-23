package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

public class SkuClsDS2 extends RestDataSource {
	 private static SkuClsDS2 instance = null;

	    public static SkuClsDS2 getInstance(String tableName) {
	        if (instance == null) {
	            instance = new SkuClsDS2(tableName,tableName);
	        }
	        return instance;
	    }
	    public static SkuClsDS2 getInstance(String id,String tableName) {
	        if (instance == null) {
	            instance = new SkuClsDS2(id,tableName);
	        }
	        return instance;
	    }
	    public SkuClsDS2(String id,String tableName) {
	    	setID(id);
	        setDataFormat(DSDataFormat.JSON);
	        //setTableName("BAS_SKU_CLS");
	        //setAttribute("_tableName", tableName, false);
	        
	        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	        keyField.setPrimaryKey(true);
	        keyField.setRequired(true);
	        keyField.setHidden(true);
//	        setFields(keyField);
	        
	        DataSourceTextField code = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER_ID());
	        code.setRequired(true);
	        
	        DataSourceTextField cname = new DataSourceTextField("SKUCLS", Util.TI18N.SKU_CLS());
	        cname.setRequired(true);
	        
	        DataSourceTextField ename = new DataSourceTextField("DESCR_C", Util.TI18N.DESCRC());
	        ename.setRequired(true);
	        
	        DataSourceTextField name = new DataSourceTextField("DESCR_E", Util.TI18N.DESCRE());
	        name.setRequired(true);
	        
	        DataSourceTextField hint = new DataSourceTextField("FACTOR", Util.TI18N.FACTOR());
	        hint.setRequired(true);
	        
	        setFields(keyField,code,cname,ename,name,hint);
	        
	        OperationBinding fetch = new OperationBinding();  
	        fetch.setOperationType(DSOperationType.FETCH);  
	        fetch.setDataProtocol(DSProtocol.POSTPARAMS);  
	        OperationBinding add = new OperationBinding();  
	        add.setOperationType(DSOperationType.ADD);  
	        add.setDataProtocol(DSProtocol.POSTPARAMS);  
	        OperationBinding update = new OperationBinding();  
	        update.setOperationType(DSOperationType.UPDATE);  
	        update.setDataProtocol(DSProtocol.POSTPARAMS);  
	        OperationBinding remove = new OperationBinding();  
	        remove.setOperationType(DSOperationType.REMOVE);  
	        remove.setDataProtocol(DSProtocol.POSTPARAMS);  
	        setOperationBindings(remove,update,add,fetch);
	        
	        setFetchDataURL("/grss/db/sto?_BCK=MIX_FLAG,ENABLE_FLAG&_FNK=ID&_TN=BAS_SKU_CLS");  
	        setAddDataURL("/grss/db/sto?_BCK=MIX_FLAG,ENABLE_FLAG&_FNK=ID&_TN=BAS_SKU_CLS");  
	        setUpdateDataURL("/grss/db/sto?_BCK=MIX_FLAG,ENABLE_FLAG&_FNK=ID&_TN=BAS_SKU_CLS");  
	        setRemoveDataURL("/grss/db/sto?_BCK=MIX_FLAG,ENABLE_FLAG&_FNK=ID&_TN=BAS_SKU_CLS"); 
	        //setDataURL("/grss/db/sto?_BCK=MIX_FLAG,ENABLE_FLAG&_FNK=ID&_TN=BAS_SKU_CLS"); 
	        //setDataProtocol(DSProtocol.POSTPARAMS);
	        
	        setClientOnly(false);
	        //setSendMetaData(true);
	    }
	    
	    @Override  
        protected Object transformRequest(DSRequest dsRequest) {  
            return super.transformRequest(dsRequest);  
        }  
        @Override  
        protected void transformResponse(DSResponse response, DSRequest request, Object data) {  
            super.transformResponse(response, request, data);  
        }
}
