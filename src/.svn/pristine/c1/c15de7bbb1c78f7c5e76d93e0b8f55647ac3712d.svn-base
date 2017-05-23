package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**发票管理（TRANS_BILL_SETTLE）
 * 
 * @author 
 *
 */
public class TransBillInvoiceDS extends DataSource{
	private static TransBillInvoiceDS instance = null;

	public static TransBillInvoiceDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TransBillInvoiceDS(id, tableName);
		}
		return instance;
	}

	public static TransBillInvoiceDS getInstance(String id) {
		if (instance == null) {
			instance = new TransBillInvoiceDS(id, id);
		}
		return instance;
	}

	public TransBillInvoiceDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);		
		setFields(keyField);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

