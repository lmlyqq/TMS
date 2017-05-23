package com.rd.client.ds.base;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  系统管理->组织机构
 * @author yuanlei
 *
 */
public class OrgDS extends DataSource {

    private static OrgDS instance = null;

    public static OrgDS getInstance(String id) {
        if (instance == null) {
            instance = new OrgDS(id, id);
        }
        return instance;
    }
    
    public static OrgDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new OrgDS(id, tableName);
        }
        return instance;
    }
    public OrgDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        
        //id`
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);
        
		// parentId
		DataSourceTextField parentIdField = new DataSourceTextField("PARENT_ORG_ID", Util.TI18N.PARENT_ORG_NAME());
		parentIdField.setForeignKey("ID");
//		parentIdField.setRequired(true);
		parentIdField.setHidden(false);
		parentIdField.setRootValue(LoginCache.getLoginUser().getDEFAULT_ORG_PARENTID());

		// name
		DataSourceTextField ORG_CNAME = new DataSourceTextField("ORG_CNAME", Util.TI18N.ORG_CNAME());
		DataSourceTextField SHORT_NAME = new DataSourceTextField("SHORT_NAME", Util.TI18N.SHORT_NAME());
		DataSourceTextField AREA_ID = new DataSourceTextField("AREA_ID", Util.TI18N.AREA_ID());
		DataSourceTextField ENABLE_FLAG = new DataSourceTextField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());

		setFields(keyField, ORG_CNAME, SHORT_NAME, AREA_ID, parentIdField, ENABLE_FLAG);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}