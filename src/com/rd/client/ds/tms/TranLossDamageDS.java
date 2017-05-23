package com.rd.client.ds.tms;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 回单管理-->货损货差
 * @author wangjun
 *
 */
public class TranLossDamageDS extends DataSource{

		private static TranLossDamageDS instance = null;

		public static TranLossDamageDS getInstance(String id, String tableName) {
			if (instance == null) {
				instance = new TranLossDamageDS(id, tableName);
			}
			return instance;
		}

		public static TranLossDamageDS getInstance(String id) {
			if (instance == null) {
				instance = new TranLossDamageDS(id, id);
			}
			return instance;
		}

		public TranLossDamageDS(String id, String tableName) {

			setID(id);
			setDataFormat(DSDataFormat.JSON);
			//setTableName(tableName);
			setAttribute("tableName", tableName, false);
			DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
					false);
			keyField.setPrimaryKey(true);
			keyField.setHidden(true);
			setFields(keyField);

			setDataURL("tmsQueryServlet?ds_id=" + getID());
			setClientOnly(false);
			setShowPrompt(false);
		}
}
