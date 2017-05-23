package com.rd.client.ds.report;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 报表管理-->作业报表-->干线流向货量预览表  数据源
 * @author wangjun
 *
 */
public class R_TRUNK_MOVEMENT_PREVIEW_DS extends DataSource{
	private static R_TRUNK_MOVEMENT_PREVIEW_DS instance = null;

	public static R_TRUNK_MOVEMENT_PREVIEW_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new R_TRUNK_MOVEMENT_PREVIEW_DS(id, tableName);
		}
		return instance;
	}

	public static R_TRUNK_MOVEMENT_PREVIEW_DS getInstance(String id) {
		if (instance == null) {
			instance = new R_TRUNK_MOVEMENT_PREVIEW_DS(id, id);
		}
		return instance;
	}

	public R_TRUNK_MOVEMENT_PREVIEW_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);

		DataSourceTextField LOAD_NO =new DataSourceTextField("LOAD_NO",Util.TI18N.LOAD_NO());
		setFields(LOAD_NO);

		setDataURL("repQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

