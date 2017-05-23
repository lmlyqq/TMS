package com.rd.client.ds.system;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 系统管理->系统参数
 * @author yuanlei
 *
 */
public class SysParamDS extends DataSource {

    private static SysParamDS instance = null;

    public static SysParamDS getInstance(String id) {
        return getInstance(id, id);
    }
    public static SysParamDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new SysParamDS(id, tableName);
        }
        return instance;
    }
    public SysParamDS(String id, String tableName) {
        
    	
    	//id 将其设为隐藏
        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setHidden(true);
        
        /**
         * name
         * param:配置参数
         * descr：  描述
         * intr：参数整数值
         * str：参数字符值
         */
        DataSourceTextField param = new DataSourceTextField("CONFIG_CODE",Util.TI18N.CFG_PARAM());
        DataSourceTextField descr = new DataSourceTextField("DESCR",Util.TI18N.CFG_DESCR());
        DataSourceTextField str  = new DataSourceTextField("VALUE_STRING",Util.TI18N.CFG_STRING());
        DataSourceTextField intr = new DataSourceTextField("VALUE_INT",Util.TI18N.CFG_INT());
        setFields(keyField ,param,descr,intr,str);
        
        setDataURL("sysQueryServlet?is_curr_page=true&&ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}