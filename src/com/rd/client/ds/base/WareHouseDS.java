package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 基础资料  --仓库管理
 * @author fanglm
 *
 */
public class WareHouseDS extends DataSource {
	
	private static WareHouseDS instance = null;

    public static WareHouseDS getInstance(String id) {
        if (instance == null) {
            instance = new WareHouseDS(id, id);
        }
        return instance;
    }
    
    public static WareHouseDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new WareHouseDS(id, tableName);
        }
        return instance;
    }
    public WareHouseDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName("BAS_WAREHOUSE");
        setAttribute("tableName", "BAS_WAREHOUSE", false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);
        
        DataSourceTextField WHSE_CODE = new DataSourceTextField("WHSE_CODE",Util.TI18N.WHSE_CODE());
        DataSourceTextField WHSE_NAME = new DataSourceTextField("WHSE_NAME",Util.TI18N.WHSE_NAME());
        DataSourceTextField WHSE_ENAME = new DataSourceTextField("WHSE_ENAME",Util.TI18N.WHSE_ENAME());
        DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME",Util.TI18N.SHORT_NAME());
        DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",Util.TI18N.HINT_CODE());
        DataSourceTextField AREA_ID = new DataSourceTextField("AREA_ID",Util.TI18N.AREA_ID_NAME());
        DataSourceTextField ORG_ID = new DataSourceTextField("ORG_ID",Util.TI18N.ORG_ID_NAME());
        DataSourceTextField START_TIME = new DataSourceTextField("START_TIME",Util.TI18N.START_TIME());
        DataSourceTextField END_TIME = new DataSourceTextField("END_TIME",Util.TI18N.END_TIME());

		setFields(keyField,WHSE_CODE,WHSE_NAME,WHSE_ENAME,SHORT_NAME,HINT_CODE,AREA_ID,ORG_ID,START_TIME,END_TIME);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
