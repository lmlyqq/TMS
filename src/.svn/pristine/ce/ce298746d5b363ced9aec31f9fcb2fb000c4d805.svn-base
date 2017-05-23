package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SysUserDS extends DataSource {

    private static SysUserDS instance = null;

    /**
     * 
     * @author fanglm
     * @param id 数据源的名称，默认用表名
     * @param tableName 表名称（可为空，增删改时需要用到）
     * @return
     */
    public static SysUserDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SysUserDS(id, tableName);
        }
        return instance;
    }
    public SysUserDS(String id, String tableName) {
        
    	//id 设为隐藏
        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, true);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        /**
         * userName:用户名称
         * user_Org_id：默认组织id
         * user_group:默认id
         * cru_status:用户状态
         */
        DataSourceTextField userName = new DataSourceTextField("USER_NAME",Util.TI18N.USER_NAME());
        DataSourceTextField user_Org_Id = new DataSourceTextField("DEFAULT_ORG_ID",Util.TI18N.USER_ORG_ID());
        
        DataSourceTextField user_group = new DataSourceTextField("USERGRP_ID",Util.TI18N.USER_GROUP());
        DataSourceTextField cru_status = new DataSourceTextField("CUR_STATUS",Util.TI18N.CUR_STATUS());
        setFields(keyField,userName,user_Org_Id,user_group,cru_status);
        setDataURL("sysQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}