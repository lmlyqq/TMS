package com.rd.client.ds.base;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 *  基础资料->包装
 * @author yuanlei
 *
 */
public class PackageDS extends DataSource {

    private static PackageDS instance = null;

    public static PackageDS getInstance(String id) {
        if (instance == null) {
            instance = new PackageDS(id, id);
        }
        return instance;
    }
    
    public static PackageDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new PackageDS(id, tableName);
        }
        return instance;
    }
    public PackageDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        //setTableName(tableName);
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		// name
		DataSourceTextField PACK = new DataSourceTextField("PACK", Util.TI18N.PACK());
		DataSourceTextField DESCR = new DataSourceTextField("DESCR", Util.TI18N.PACK_DESCR());

		setFields(keyField, PACK, DESCR);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}