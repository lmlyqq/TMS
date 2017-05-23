package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 报表管理-->作业报表--干线车装载数据统计 数据源
 * @author wangjun
 *
 */
public class R_VEHICLE_LOAD_VIEW_DS extends DataSource{
	private static R_VEHICLE_LOAD_VIEW_DS instance = null;

	public static R_VEHICLE_LOAD_VIEW_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_VEHICLE_LOAD_VIEW_DS(id, tableName);
		}
		return instance;
	}

	public static R_VEHICLE_LOAD_VIEW_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_VEHICLE_LOAD_VIEW_DS(id, id);
		}
		return instance;
	}

	public R_VEHICLE_LOAD_VIEW_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);

		DataSourceTextField SHPM_NO =new DataSourceTextField("SHPM_NO",Util.TI18N.SHPM_NO());
		setFields(SHPM_NO);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

