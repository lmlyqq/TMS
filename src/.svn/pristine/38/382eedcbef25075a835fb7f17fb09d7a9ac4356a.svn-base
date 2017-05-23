package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 结算管理-
 * @author 
 *
 */
public class RecInitBillDS extends DataSource{
	private static RecInitBillDS instance = null;

	public static RecInitBillDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new RecInitBillDS(id, tableName);
		}
		return instance;
	}

	public static RecInitBillDS getInstance(String id) {
		if (instance == null) {
			instance = new RecInitBillDS(id, id);
		}
		return instance;
	}

	public RecInitBillDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
	
		setAttribute("tableName", tableName, false);
		
		
		DataSourceTextField FEE_TYPE = new DataSourceTextField("FEE_TYPE",
				"账单类型");
		

		setFields(FEE_TYPE);
		
		setDataURL("settQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

