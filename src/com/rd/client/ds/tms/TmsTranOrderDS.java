package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 订单动态监控--托运单管理数据源
 * @author wangjun
 *
 */
public class TmsTranOrderDS extends DataSource{

	private static TmsTranOrderDS instance = null;

	public static TmsTranOrderDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TmsTranOrderDS(id, tableName);
		}
		return instance;
	}

	public static TmsTranOrderDS getInstance(String id) {
		if (instance == null) {
			instance = new TmsTranOrderDS(id, id);
		}
		return instance;
	}

	public TmsTranOrderDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField CUSTOM_ODR_NO = new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		setFields(keyField,CUSTOM_ODR_NO);
	

		setFields(keyField);
		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
