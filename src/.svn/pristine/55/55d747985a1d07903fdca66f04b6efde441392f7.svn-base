package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class PayInitDS extends DataSource{
	
	private static PayInitDS instance = null;

	public static PayInitDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PayInitDS(id, tableName);
		}
		return instance;
	}

	public static PayInitDS getInstance(String id) {
		if (instance == null) {
			instance = new PayInitDS(id, id);
		}
		return instance;
	}

	public PayInitDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
	
		setAttribute("tableName", tableName, false);
		
		DataSourceTextField FEE_TYPE = new DataSourceTextField("FEE_TYPE","费用类型");

		setFields(FEE_TYPE);
		
		setDataURL("settQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}
	
}
