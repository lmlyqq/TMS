package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理-->投诉建议二级窗口
 * @author wangjun
 *
 */
public class OdrComplaintDS extends DataSource{

	private static OdrComplaintDS instance = null;

	public static OdrComplaintDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new OdrComplaintDS(id, tableName);
		}
		return instance;
	}

	public static OdrComplaintDS getInstance(String id) {
		if (instance == null) {
			instance = new OdrComplaintDS(id, id);
		}
		return instance;
	}

	public OdrComplaintDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO",
				Util.TI18N.LOAD_NO());
		DataSourceTextField ODR_NO = new DataSourceTextField("ODR_NO",
				Util.TI18N.ODR_NO());

		
		setFields( LOAD_NO, ODR_NO);
	
		setDataURL("tmsQueryServlet?is_curr_page=true&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
