package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AccountAreaDS extends DataSource {
   private static AccountAreaDS install = null;
   
   public AccountAreaDS(String id,String tableName){
	   
	   this.setID(id);
	   this.setDataFormat(DSDataFormat.JSON);
	   //this.setTableName("BAS_CHARGE_REGION");
	   setAttribute("tableName", "BAS_CHARGE_REGION", false);
	   
	   DataSourceTextField keyField = new DataSourceTextField("ID","ID",10,false);
	   keyField.setPrimaryKey(true);
	   keyField.setRequired(true);
	   keyField.setHidden(true);
	   
	   DataSourceTextField  CHARGE_REGION_NAME = new DataSourceTextField("CHARGE_REGION_NAME",Util.TI18N.CHARGE_REGION_NAME());
	   DataSourceTextField CHARGE_REGION_ENAME = new DataSourceTextField("CHARGE_REGION_ENAME",Util.TI18N.CHARGE_REGION_ENAME());
	   DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
	   
	   setFields(keyField,CHARGE_REGION_NAME,CHARGE_REGION_ENAME,HINT_CODE);
	   
	   setDataURL("basQueryServlet?ds_id="+getID());
       setClientOnly(false);
       setShowPrompt(false);
   }
   
   public static AccountAreaDS getInstall(String id){
	   if(install == null){
		   install = new AccountAreaDS(id,id);
	   }
	   return install;
   }
   
   public static AccountAreaDS getInstall(String id,String tableName){
	   if(install == null){
		   install = new AccountAreaDS(id,id);
	   }
	   return install;
   }
}
