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
public class LoadJob_search_DS extends DataSource{
	private static LoadJob_search_DS instance = null;

	public static LoadJob_search_DS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new LoadJob_search_DS(id, tableName);
		}
		return instance;
	}

	public static LoadJob_search_DS getInstance(String id) {
		if (instance == null) {
			instance = new LoadJob_search_DS(id, id);
		}
		return instance;
	}

	public LoadJob_search_DS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField LOAD_NO = new DataSourceTextField("LOAD_NO", Util.TI18N.LOAD_NO(),110);//调度单号
//		LOAD_NO.setHidden(true);
		DataSourceTextField SHPM_NO = new DataSourceTextField("SHPM_NO", Util.TI18N.SHPM_NO(),110);//调度单号
		
		DataSourceTextField LOAD_WHSE_NAME = new DataSourceTextField("WHSE_ID_NAME", Util.TI18N.LOAD_WHSE(),140);//调度单号
//		DataSourceTextField QUEUE_SEQ = new DataSourceTextField("QUEUE_SEQ", Util.TI18N.QUEUE(),140);//调度单号
		
//		DataSourceTextField ARRIVE_WHSE_TIME = new DataSourceTextField("ARRIVE_WHSE_TIME", Util.TI18N.ARRIVE_WHSE_TIME(),50);//调度单号
		
		DataSourceTextField LOAD_NAME = new DataSourceTextField("LOAD_NAME", Util.TI18N.LOAD_NAME(),120);//调度单号
		LOAD_NAME.setHidden(true);
		
		DataSourceTextField LOAD_STATUS_NAME = new DataSourceTextField("LOAD_STATUS_NAME", Util.TI18N.STATUS(),50);//调度单号
		
		DataSourceTextField PLATE_NO = new DataSourceTextField("TRANS_PLATE_NO", Util.TI18N.PLATE_NO(),60);//调度单号
		
		DataSourceTextField VEHICLE_TYP_NAME = new DataSourceTextField("TRANS_VEHICLE_TYP_ID_NAME", Util.TI18N.VEHL_TYP(),60);//调度单号
		
		DataSourceTextField UNLOAD_NAME = new DataSourceTextField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(),140);//调度单号
		
//		DataSourceTextField TOT_QNTY = new DataSourceTextField("TOT_QNTY", Util.TI18N.TOT_QNTY(),70);//调度单号
		DataSourceTextField SUPLR_NAME = new DataSourceTextField("SUPLR_NAME", Util.TI18N.SUPLR_NAME(),140);//调度单号
		SUPLR_NAME.setHidden(true);
		
		setFields(keyField, LOAD_NO,SHPM_NO,PLATE_NO,LOAD_WHSE_NAME,LOAD_STATUS_NAME,LOAD_NAME,VEHICLE_TYP_NAME,UNLOAD_NAME,SUPLR_NAME);

		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

