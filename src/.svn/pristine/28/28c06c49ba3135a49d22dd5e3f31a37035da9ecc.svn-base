package com.rd.client.ds.warning;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 运输管理--预警信息--应发未发数据源
 * @author wangjun
 *
 */
public class PreloadExcelDS extends DataSource{
	private static PreloadExcelDS instance = null;

	public static PreloadExcelDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new PreloadExcelDS(id, tableName);
		}
		return instance;
	}

	public static PreloadExcelDS getInstance(String id) {
		if (instance == null) {
			instance = new PreloadExcelDS(id, id);
		}
		return instance;
	}

	public PreloadExcelDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField SHPM_NO =new DataSourceTextField("SHPM_NO",Util.TI18N.SHPM_NO());
		DataSourceTextField CUSTOM_ODR_NO =new DataSourceTextField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO()); 
		DataSourceTextField TRANS_SRVC_ID_NAME =new DataSourceTextField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID()); 
		DataSourceTextField STATUS_NAME=new DataSourceTextField("STATUS_NAME",Util.TI18N.STATUS()); 
		
		setFields(keyField,SHPM_NO,CUSTOM_ODR_NO,TRANS_SRVC_ID_NAME,STATUS_NAME);

		setDataURL("tmsQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

