package com.rd.client.ds.settlement;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;

/**
 * 财务管理--计费管理--协议主信息
 * @author fangliangmeng
 *
 */
public class TariffHeaderRecDS extends DataSource{
	private static TariffHeaderRecDS instance = null;

	public static TariffHeaderRecDS getInstance(String id, String tableName) {
		if (instance == null) {
			instance = new TariffHeaderRecDS(id, tableName);
		}
		return instance;
	}

	public static TariffHeaderRecDS getInstance(String id) {
		if (instance == null) {
			instance = new TariffHeaderRecDS(id, id);
		}
		return instance;
	}

	public TariffHeaderRecDS(String id, String tableName) {

		setID(id);
		setDataFormat(DSDataFormat.JSON);
//		setTableName(tableName);
		setAttribute("tableName", tableName, false);
		DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10,
				false);
		keyField.setPrimaryKey(true);
		keyField.setHidden(true);
		
		/**
		DataSourceTextField TFF_NAME = new DataSourceTextField("TFF_NAME",Util.TI18N.TFF_NAME(), 100);
		DataSourceTextField CONTACT_NO = new DataSourceTextField("CONTACT_NO", Util.TI18N.CONTACT_NO(), 120);
		DataSourceTextField START_DATE = new DataSourceTextField("START_DATE", Util.TI18N.FEE_START_DATE(), 120);
		DataSourceTextField END_DATE = new DataSourceTextField("END_DATE", Util.TI18N.FEE_END_DATE(), 80);
		DataSourceTextField SIGN_ORG_ID = new DataSourceTextField("SIGN_ORG_ID", Util.TI18N.SIGN_ORG_ID(), 120);
		DataSourceTextField EXEC_ORG_ID = new DataSourceTextField("EXEC_ORG_ID", Util.TI18N.EXEC_ORG_ID(), 100);
		DataSourceTextField TFF_TYP = new DataSourceTextField("TFF_TYP", Util.TI18N.TFF_TYP(), 100);
		DataSourceTextField OBJECT_ID = new DataSourceTextField("OBJECT_ID", Util.TI18N.OBJECT_ID(), 160);
		DataSourceTextField OBJECT_NAME = new DataSourceTextField("OBJECT_NAME", Util.TI18N.OBJECT_NAME(), 70);
		DataSourceTextField DOC_TYP = new DataSourceTextField("DOC_TYP", Util.TI18N.DOC_TYP(), 80);
//		DataSourceTextField NOTES = new DataSourceTextField("NOTES", Util.TI18N.NOTES(), 80);
		DataSourceTextField SORTORDER = new DataSourceTextField("SORTORDER", Util.TI18N.SORTORDER(), 80);
		DataSourceBooleanField ENABLE_FLAG = new DataSourceBooleanField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(),80);
		DataSourceBooleanField INCLUDE_SUB_FLAG = new DataSourceBooleanField("INCLUDE_SUB_FLAG","包含下级机构" ,80);**/
		
		setFields(keyField);

		setDataURL("settQueryServlet?is_curr_page=true&&ds_id=" + getID());
		setClientOnly(false);
		setShowPrompt(false);
	}

}

