package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 基础资料->油卡管理
 * @author yuanlei
 *
 */
public class BasQilCardDs extends DataSource {

    private static BasQilCardDs instance = null;

    /**
     * 
     * @author lml
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static BasQilCardDs getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new BasQilCardDs(id, tableName);
        }
        return instance;
    }
    public BasQilCardDs(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setForeignKey("ID");
        keyField.setHidden(true);
		
        DataSourceTextField OILCARD = new DataSourceTextField("OILCARD", "油卡号");
        
        setFields(keyField, OILCARD);
        setDataURL("basQueryServlet?ds_id="+this.getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
    public static BasQilCardDs getInstall(String id){
 	   if(instance == null){
 		  instance = new BasQilCardDs(id,id);
 	   }
 	   return instance;
    }
    
    public static BasQilCardDs getInstall(String id,String tableName){
 	   if(instance == null){
 		  instance = new BasQilCardDs(id,tableName);
 	   }
 	   return instance;
    }
}