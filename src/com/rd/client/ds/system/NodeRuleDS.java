package com.rd.client.ds.system;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 业务规则->运输规则->节点规则
 * @author yuanlei
 * @create time 2012-06-05 20:10
 *
 */
public class NodeRuleDS extends DataSource {

    private static NodeRuleDS instance = null;

    public static NodeRuleDS getInstance(String id) {
        return getInstance(id, id);
    }
    public static NodeRuleDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new NodeRuleDS(id, tableName);
        }
        return instance;
    }
    public NodeRuleDS(String id, String tableName) {
        
    	
    	//id 将其设为隐藏
        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        setFields(keyField);
        
        setDataURL("rulQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}