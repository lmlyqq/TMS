package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 调度单数据源
 * @author yuanlei
 *
 */
public class Load2DS extends DataSource{
	private static Load2DS instance = null;

	public static Load2DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new Load2DS(id, tableName);
		}
		return instance;
	}

	public static Load2DS getInstance(String id) {
		if (instance == null) {
			instance = new Load2DS(id, id);
		}
		return instance;
	}

	public Load2DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO());//调度单号
		DataSourceTextField SUPLR_ID = new DataSourceTextField("SUPLR_ID", Util.TI18N.SUPLR_ID());//二级窗口 SUPLR_ID_NAME
		DataSourceTextField EXEC_ORG_ID = new DataSourceTextField("EXEC_ORG_ID", Util.TI18N.EXEC_ORG_ID());//EXEC_ORG_ID 执行结构
		DataSourceTextField CUSTOMER_ID = new DataSourceTextField("CUSTOMER_ID", Util.TI18N.CUSTOMER());//二级窗口  客户 
		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		//DataSourceTextField CHECK_FLAG = new DataSourceTextField("CHECK_FLAG","车辆检查标记");
		setFields(keyField, LOAD_NO, CUSTOM_ODR_NO, SUPLR_ID, EXEC_ORG_ID, CUSTOMER_ID);

		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

