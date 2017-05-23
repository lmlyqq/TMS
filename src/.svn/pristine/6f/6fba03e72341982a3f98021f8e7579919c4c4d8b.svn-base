package com.rd.client.action.tms.dispatch;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 明细导出（待调）
 * @author yuanl
 * @created time 2010-09-03 10：45
 */
public class ExportShpmItemAction implements com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private ListGrid table;
	private String orderby;
	public ExportShpmItemAction(ListGrid table, String orderby) {
		this.table = table;
		this.orderby = orderby;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		String fieldName = "SHPM_NO,REFENENCE1,LOAD_NAME,UNLOAD_NAME,UNLOAD_AREA_NAME2,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,DRIVER,MOBILE,PLATE_NO,EXEC_ORG_ID_NAME,SKU,SKU_NAME,TEMPERATURE1_NAME,QNTY,VOL,G_WGT,N_WGT";
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "");
		int m = where.toLowerCase().indexOf("order by");
		if(m > 0) {
			where = where.substring(0,m);
		}
		String header = getFieldName();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName);
		sql.append(" from V_SHIPMENT_ITEM WHERE SHPM_NO IN (select SHPM_NO ");
		sql.append(where);
	    sql.append(") ");
	    sql.append(orderby);
	    //System.out.println(sql.toString());
		Util.exportUtil(header, fieldName, sql.toString());
	}
	
	private String getFieldName(){
		StringBuffer sf = new StringBuffer();
		sf.append(Util.TI18N.SHPM_NO());
		sf.append(",");
		sf.append(Util.TI18N.REFENENCE1());
		sf.append(",");
		sf.append(Util.TI18N.LOAD_NAME());
		sf.append(",");
		sf.append(Util.TI18N.UNLOAD_NAME());
		sf.append(",");
		sf.append("收货方");
		sf.append(",");
		sf.append(Util.TI18N.UNLOAD_ADDRESS());
		sf.append(",");
		sf.append(Util.TI18N.CONT_NAME());
		sf.append(",");
		sf.append(Util.TI18N.CONT_TEL());
		sf.append(",");
		sf.append(Util.TI18N.DRIVER1());
		sf.append(",");
		sf.append(Util.TI18N.MOBILE());
		sf.append(",");
		sf.append(Util.TI18N.PLATE_NO());
		sf.append(",");
		sf.append(Util.TI18N.EXEC_ORG_ID_NAME());
		sf.append(",");
		sf.append(Util.TI18N.SKU());
		sf.append(",");
		sf.append(Util.TI18N.SKU_NAME());
		sf.append(",");
		sf.append(Util.TI18N.TEMPERATURE());
		sf.append(",");
		sf.append(Util.TI18N.QNTY());
		sf.append(",");
		sf.append(Util.TI18N.VOL());
		sf.append(",");
		sf.append(Util.TI18N.G_WGT());
		sf.append(",");
		sf.append(Util.TI18N.N_WGT());
		return sf.toString();
	}
}

