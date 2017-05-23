package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

public class BillSettlePayDS2 extends DataSource{

	private static BillSettlePayDS instance = null;

	public static BillSettlePayDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new BillSettlePayDS(id, tableName);
		}
		return instance;
	}

	public static BillSettlePayDS getInstance(String id) {
		if (instance == null) {
			instance = new BillSettlePayDS(id, id);
		}
		return instance;
	}

	public BillSettlePayDS2(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField DOC_NO = new DataSourceTextField("DOC_NO",
				Util.TI18N.DOC_NO());//费用类别
		DataSourceTextField FEE_NAME = new DataSourceTextField("FEE_NAME",
				Util.TI18N.FEE_NAME());//费用属性
		DataSourceTextField FEE_BAS = new DataSourceTextField("FEE_BAS",
				Util.TI18N.FEE_BASE());//费用属性
		
//		DataSourceTextField FEE_CODE = new DataSourceTextField("FEE_CODE",
//				Util.TI18N.FEE_CODE());
//		DataSourceTextField FEE_ENAME = new DataSourceTextField("FEE_ENAME",
//				Util.TI18N.FEE_ENAME());
//		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",
//			Util.TI18N.HINT_CODE());
		
		setFields(keyField, DOC_NO, FEE_NAME, FEE_BAS);
	
		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
