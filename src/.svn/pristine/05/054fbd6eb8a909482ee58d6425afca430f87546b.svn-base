package com.rd.client.action.settlement;

import java.util.ArrayList;

import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class ExportPayInitAction implements com.smartgwt.client.widgets.menu.events.ClickHandler{

	private ListGrid table;
	private ListGrid itemTable;
	
	public ExportPayInitAction(ListGrid table,ListGrid itemTable) {
		this.table = table;
		this.itemTable = itemTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		if(table.getSelectedRecord()==null)return;
		if(itemTable==null)return;
		ArrayList<String> list = new ArrayList<String>();
		String id="";
		ListGridRecord [] records=itemTable.getSelection();
		if(records!=null&&records.length>0){
			for(int i=0;i<records.length;i++){
				list.add("'"+records[i].getAttribute("ID")+"'");
			}
			id=list.get(0);
		}
		for(int i=1;i<list.size();i++){
			id = id+","+list.get(i);
		}
		String fieldName = "SHORT_NAME,CUSTOMER_CNAME,UDF2,SKU_NAME,LOAD_NO,SUPLR_NAME,PLATE_NO,DRIVER,LOAD_DATE,UNLOAD_DATE,UDF3,CONFIRM_AMOUNT,NOTES,INIT_AMOUNT,TOT_QNTY,TOT_GROSS_W,TOT_PACK_V,VEHICLE_TYP_NAME,OTHER_FEE";
		String where = " where INIT_NO = '"+table.getSelectedRecord().getAttribute("INIT_NO")+"' and ID in("+id+")";
		String header = getFieldName();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName);
		sql.append(" from B_PAYBILL_BYLOAD ");
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
		sf.append("吨位");
		sf.append(",");
		sf.append("立方数");
		sf.append(",");
		sf.append("车型");
		sf.append(",");
		sf.append("额外费用");
		return sf.toString();
	}
	
}
