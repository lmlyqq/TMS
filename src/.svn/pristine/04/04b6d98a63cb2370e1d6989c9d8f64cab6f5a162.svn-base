package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输计划 --提货装车   数据源
 * @author wangjun
 *
 */
public class LoadJobDS extends DataSource{
	private static LoadJobDS instance = null;

	public static LoadJobDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new LoadJobDS(id, tableName);
		}
		return instance;
	}

	public static LoadJobDS getInstance(String id) {
		if (instance == null) {
			instance = new LoadJobDS(id, id);
		}
		return instance;
	}

	public LoadJobDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO(),110);//调度单号
		
		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO", Util.TI18N.SHPM_NO(),110);//调度单号
		
		DataSourceTextField LOAD_NAME = new DataSourceTextField("LOAD_NAME", Util.TI18N.LOAD_NAME(),120);//调度单号
		
		DataSourceTextField LOAD_STATUS_NAME = new DataSourceTextField("LOAD_STATUS_NAME", Util.TI18N.STATUS(),50);//调度单号
		
		DataSourceTextField PLATE_NO = new DataSourceTextField("PLATE_NO", Util.TI18N.PLATE_NO(),60);//调度单号
		
		DataSourceTextField VEHICLE_TYP_NAME = new DataSourceTextField("VEHICLE_TYP_NAME", Util.TI18N.VEHL_TYP(),60);//调度单号
		
		DataSourceTextField UNLOAD_NAME = new DataSourceTextField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(),140);//调度单号
		
		DataSourceTextField TOT_QNTY = new DataSourceTextField("TOT_QNTY", Util.TI18N.TOT_QNTY(),70);//调度单号
		DataSourceTextField SUPLR_NAME = new DataSourceTextField("SUPLR_NAME", Util.TI18N.SUPLR_NAME(),140);//调度单号
		
		
		setFields(keyField, LOAD_NO,SHPM_NO,LOAD_NAME,LOAD_STATUS_NAME,PLATE_NO,VEHICLE_TYP_NAME,UNLOAD_NAME,TOT_QNTY,SUPLR_NAME);

		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

