package com.rd.client.ds.tms;

import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
/**
 * 运输管理-->费用种类
 * @author wangjun
 *
 */
public class TmsChargeTypeDS extends DataSource{

	private static TmsChargeTypeDS instance = null;

	public static TmsChargeTypeDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TmsChargeTypeDS(id, tableName);
		}
		return instance;
	}

	public static TmsChargeTypeDS getInstance(String id) {
		if (instance == null) {
			instance = new TmsChargeTypeDS(id, id);
		}
		return instance;
	}

	public TmsChargeTypeDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
		//setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);

		DataSourceTextField TRANS_FEE_TYP = new DataSourceTextField("TRANS_FEE_TYP",
				Util.TI18N.TRANS_FEE_TYP());//费用类别
		DataSourceTextField FEE_ATTR = new DataSourceTextField("FEE_ATTR",
				Util.TI18N.FEE_ATTR());//费用属性
		
//		DataSourceTextField FEE_CODE = new DataSourceTextField("FEE_CODE",
//				Util.TI18N.FEE_CODE());
//		DataSourceTextField FEE_ENAME = new DataSourceTextField("FEE_ENAME",
//				Util.TI18N.FEE_ENAME());
//		DataSourceTextField HINT_CODE = new DataSourceTextField("HINT_CODE",
//			Util.TI18N.HINT_CODE());
		
		setFields(keyField, TRANS_FEE_TYP, FEE_ATTR);
	
		setDataURL("tmsQueryServlet?ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}
