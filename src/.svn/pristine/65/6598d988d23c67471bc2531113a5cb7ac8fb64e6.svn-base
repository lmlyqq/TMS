package com.rd.client.common.ds;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 树型结构弹出窗口数据源（目前使用组织机构）
 * @author fanglm
 *
 */
public class TreeListDS extends DataSource {
	
    private static TreeListDS instance = null;

    public static TreeListDS getInstance(String id) {
        if (instance == null) {
            instance = new TreeListDS(id, id);
        }
        return instance;
    }
    
    public static TreeListDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new TreeListDS(id, tableName);
        }
        return instance;
    }
    
    public TreeListDS(String id, String tableName) {

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
		parentIdField.setHidden(false);
//		parentIdField.setRootValue(1);

		setFields(keyField,parentIdField);
        
        setDataURL("initDataServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
