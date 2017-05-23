package com.rd.client.action.settlement;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class ExportPayInAction implements com.smartgwt.client.widgets.menu.events.ClickHandler {

	private ListGrid table;
	
	public ExportPayInAction(ListGrid table) {
		this.table = table;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		if(table.getSelectedRecord()==null)return;
		String fieldName = "SHORT_NAME,CUSTOMER_CNAME,UDF2,SKU_NAME,LOAD_NO,SUPLR_NAME,PLATE_NO,DRIVER,LOAD_DATE,UNLOAD_DATE,UDF3,CONFIRM_AMOUNT,NOTES,INIT_AMOUNT,TOT_QNTY,TOT_GROSS_W,TOT_PACK_V,VEHICLE_TYP_NAME,OTHER_FEE";
		String header = getFieldName();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName);
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE").replaceAll("V_BILL_PAY_INITIAL t", "B_PAYBILL_BYSHPM"), "FROM B_PAYBILL_BYSHPM ");
		sql.append(where);
		Util.exportUtil(header, fieldName, sql.toString());
	}

	private String getFieldName(){
		StringBuffer sf = new StringBuffer();
		sf.append("项目名称");
		sf.append(",");
		sf.append("客户名称");
		sf.append(",");
		sf.append("运输类型");
		sf.append(",");
		sf.append("货品");
		sf.append(",");
		sf.append("内单号");
		sf.append(",");
		sf.append("承运商简称");
		sf.append(",");
		sf.append("车牌号");
		sf.append(",");
		sf.append("司机");
		sf.append(",");
		sf.append("发货日期");
		sf.append(",");
		sf.append("到货日期");
		sf.append(",");
		sf.append("起止地");
		sf.append(",");
		sf.append("运费");
		sf.append(",");
		sf.append("备注");
		sf.append(",");
		sf.append("月初金额");
		sf.append(",");
		sf.append("托盘");
		sf.append(",");
		sf.append("吨位(T)");
		sf.append(",");
		sf.append("立方数");
		sf.append(",");
		sf.append("车型");
		sf.append(",");
		sf.append("额外费用");
		return sf.toString();
	}
	
}
