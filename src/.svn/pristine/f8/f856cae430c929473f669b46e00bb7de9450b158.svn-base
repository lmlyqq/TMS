package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class AddrDistDS extends DataSource{
	private static AddrDistDS instance = null;

    public static AddrDistDS getInstance(String id) {
        if (instance == null) {
            instance = new AddrDistDS(id, id);
        }
        return instance;
    }
    
    public static AddrDistDS getInstance(String id, String tableName) {
        if (instance == null) {
            instance = new AddrDistDS(id, tableName);
        }
        return instance;
    }
    
    public AddrDistDS(String id, String tableName) {

        setID(id);
        setDataFormat(DSDataFormat.JSON);
        setAttribute("tableName", tableName, false);
        
        //id
        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10, false);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);

		// name
		DataSourceTextField ADDR_ID1 = new DataSourceTextField("ADDR_ID1", "起运地ID");
		DataSourceTextField ADDR_ID2 = new DataSourceTextField("ADDR_ID2", "目的地ID");
		DataSourceTextField ADDR_NAME1 = new DataSourceTextField("ADDR_NAME1","起运地");
		DataSourceTextField ADDR_NAME2 = new DataSourceTextField("ADDR_NAME2","目的地");
		DataSourceTextField AREA_NAME1 = new DataSourceTextField("AREA_NAME21","起运城市");
		DataSourceTextField AREA_NAME2 = new DataSourceTextField("AREA_NAME22","目的城市");
		DataSourceTextField AREA_ID1 = new DataSourceTextField("AREA_ID1","起运省ID");
		DataSourceTextField AREA_ID2 = new DataSourceTextField("AREA_ID2","目的省ID");
		DataSourceTextField AREA_ID21 = new DataSourceTextField("AREA_ID21","起运城市ID");
		DataSourceTextField AREA_ID22 = new DataSourceTextField("AREA_ID22","目的城市ID");
		DataSourceFloatField MILEAGE = new DataSourceFloatField("MILEAGE","公里数");
//		DataSourceBooleanField ADDR_FLAG = new DataSourceBooleanField("ADDR_FLAG","按地址点计算");
		
		setFields(keyField,ADDR_ID1,ADDR_ID2,ADDR_NAME1 ,ADDR_NAME2,AREA_ID1,
				AREA_ID2,AREA_NAME1,AREA_ID21,AREA_NAME2,AREA_ID22,MILEAGE);
        
        setDataURL("basQueryServlet?ds_id="+getID());
        setClientOnly(false);
        setShowPrompt(false);
    }
}
