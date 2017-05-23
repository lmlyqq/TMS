package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料---供应商管理
 * @author lijun
 *
 */
public class SupplierManagerDS extends DataSource{
    private static SupplierManagerDS instance = null;
    
   public SupplierManagerDS(){
	   
   }
   
   public SupplierManagerDS(String id,String tableName){
	   this.setID(id);
	   this.setDataFormat(DSDataFormat.JSON);//设置数据的传送形式：json
	   //this.setTableName("BAS_SUPPLIER");
	   setAttribute("tableName", "BAS_SUPPLIER", false);
	   DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
	   keyField.setPrimaryKey(true);
	   keyField.setHidden(true);
	   
	   
//	   //name
	   DataSourceTextField SUPLR_CODE = new DataSourceTextField("SUPLR_CODE",Util.TI18N.SUP_SUPLR_CODE());
	   DataSourceTextField SUPLR_CNAME = new DataSourceTextField("SUPLR_CNAME",Util.TI18N.SUP_SUPLR_CNAME());
	   DataSourceTextField SUPLR_ENAME = new DataSourceTextField("SUPLR_ENAME",Util.TI18N.SUP_SUPLR_ENAME());
	   DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME",Util.TI18N.SHORT_NAME());
	   DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
       DataSourceTextField SUPLR_TYP = new DataSourceTextField("SUPLR_TYP",Util.TI18N.SUP_SUPLR_TYP());	   
	   DataSourceTextField PROPERTY = new DataSourceTextField("PROPERTY",Util.TI18N.PROPERTY());
	   DataSourceBooleanField TRANSPORT_FLAG = new DataSourceBooleanField("TRANSPORT_FLAG",Util.TI18N.TRANSPORT_FLAG());
	   DataSourceBooleanField WAREHOUSE_FLAG = new DataSourceBooleanField("WAREHOUSE_FLAG",Util.TI18N.WAREHOUSE_FLAG());
	   DataSourceTextField INTL_FLAG = new DataSourceTextField("INTL_FLAG",Util.TI18N.INTL_FLAG());
	   DataSourceBooleanField ENABLE_FLAG = new DataSourceBooleanField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
	   DataSourceBooleanField BLACKLIST_FLAG = new DataSourceBooleanField("BLACKLIST_FLAG","黑名单");
	   setFields(keyField,SUPLR_CODE,SUPLR_CNAME,SUPLR_ENAME,SHORT_NAME,HINT_CODE,SUPLR_TYP,PROPERTY,TRANSPORT_FLAG,WAREHOUSE_FLAG,INTL_FLAG,ENABLE_FLAG,BLACKLIST_FLAG);
	   
	   setDataURL("basQueryServlet?ds_id="+getID());
	   setClientOnly(false);
	   setShowPrompt(false);
	   
   }
    
    public static SupplierManagerDS getInstance(String id){
		if(instance == null){
			instance = new SupplierManagerDS(id,id);
		}
		return instance;
    	
    }
    public static SupplierManagerDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SupplierManagerDS(id, tableName);
        }
        return instance;
    }
}
