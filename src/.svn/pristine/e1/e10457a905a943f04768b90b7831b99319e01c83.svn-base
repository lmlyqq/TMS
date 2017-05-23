package com.rd.client.action.tms.order;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 托运单明细导出
 * @author sandy
 * @created time 2013-05-10 10：45
 *
 */
public class ExportDetailAction implements com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private ListGrid table;
	
	public ExportDetailAction(ListGrid table){
		this.table = table;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
        String fieldName = "custom_odr_no,unload_area_name,unload_name,unload_contact,unload_tel,sku,sku_name,sku_spec,qnty,uom,lot_id,lotatt02";
		String header = "客户单号,收货区域,收货方,收货联系人,收货联系电话,货品代码,货品名称,货品规格,订单数量,单位,批号,专供标示";
        StringBuffer sf = new StringBuffer();
        sf.append("select ");
        sf.append(fieldName);
        sf.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), ""));
		String sql = sf.toString().replaceAll("V_ORDER_HEADER", "v_export_order_item");
		Util.exportUtil(header, fieldName.toUpperCase(), sql);
	}
}

