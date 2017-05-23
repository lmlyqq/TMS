package com.rd.client.ds.base;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class SUPPLIERDS extends DataSource{
	
	private static SUPPLIERDS instance = null;

	public static SUPPLIERDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new SUPPLIERDS(id, tableName);
		}
		return instance;
	}

	public static SUPPLIERDS getInstance(String id) {
		if (instance == null) {
			instance = new SUPPLIERDS(id, id);
		}
		return instance;
	}

	public SUPPLIERDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		DataSourceTextField SUPLR_CODE = new DataSourceTextField("SUPLR_CODE",
				"SUPLR_CODE");
		
		DataSourceTextField SUPLR_CNAME = new DataSourceTextField("SUPLR_CNAME",
				"SUPLR_CNAME");
		
		setFields(keyField,SUPLR_CODE, SUPLR_CNAME);
	
		setDataURL("basQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
}
