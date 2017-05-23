package com.rd.client.action.kpi;

import java.util.ArrayList;
import java.util.List;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class ExportUnloadRateAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{
   
	/**
	 * 到库及时率报表--导出功能
	 * wangJun 2011-10-30
	 * 
	 */
	
	private ListGrid table;
	
	public ExportUnloadRateAction(ListGrid table){
		this.table = table;
	}
	
	public ExportUnloadRateAction(ListGrid table, String orderby) {
		this.table = table;
	}

	@Override
	public void onClick(ClickEvent event) {
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		String header = list.get(1);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");
		sql.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+"v_export_unload_rate"));
		Util.exportUtil(header, fieldName, sql.toString());
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		String header = "作业单编号,客户单号,调度单号,供应商,车牌号,司机,手机号,发货方,收货方,收货区域,收货方地址,作业单状态,当前位置,本单量," +
				"订单时间,派发时间,预计到库时间,到库登记时间,实际发运时间,离库登记时间,预计到达时间,实际到达时间,到货逾期天数,签收备注,司机服务态度" +
				",客户满意度,运输异常,异常描述,联系人,联系电话,跟单员";

		String fieldName= "SHPM_NO,CUSTOM_ODR_NO,LOAD_NO,SUPLR_NAME,PLATE_NO,DRIVER,MOBILE," +
				"LOAD_NAME,UNLOAD_NAME,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,SHPM_STATUS_NAME,CURRENT_LOC,TOT_QNTY_EACH," +
				"ODR_TIME,ASSIGN_TIME,PRE_WHSE_TIME,ARRIVE_WHSE_TIME,DEPART_TIME,LEAVE_WHSE_TIME,PRE_UNLOAD_TIME," +
				"UNLOAD_TIME,UNLOAD_DELAY_DAYS,TRACK_NOTES,SERVICE_CODE," +
				"SATISFY_CODE,ABNOMAL_STAT,ABNOMAL_NOTES,UNLOAD_CONT,UNLOAD_TEL,USER_NAME";
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");
		
		String g = JSOHelper.getAttribute(table.getJsObj(), "WHERE");
		String g1[] = g.split(" ");
		g1[2] = "v_export_unload_rate" ;
		String h2 = null;
		String h3 = null;
		h2 = g1[1];
		for(int i =1 ; i < g1.length-1; i++){
		  h3 = " "+g1[i+1];
		  h2 = h2 + h3 ;	
		}
		//System.out.println(h2);
		String gh = (ObjUtil.ifNull(h2,"from v_export_unload_rate"));
		sql.append(gh);
		Util.exportUtil(header, fieldName, sql.toString());
	}
	
	private List<String> getFieldName(){
		ListGridField[] grid = table.getFields();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		String add_char = "$72v";
		String add_char2 = "$63n";
		for(int i=1;i<grid.length;i++){

			if(!grid[i].getName().equals(add_char) && !grid[i].getName().equals(add_char2)) {
				if(grid[i].getAttribute("valueMap") != null) {
					sql.append(grid[i].getName() + "_NAME");
				}
				else {
					sql.append(grid[i].getName());
				}
				sql1.append(replaceStyle(grid[i].getTitle()));
				sql.append(",");
				sql1.append(",");
			}
		}
		List<String> list = new ArrayList<String>();
		list.add(sql.substring(0, sql.length()-1));
		list.add(sql1.substring(0, sql1.length()-1));
		
		return list;
	}
	
	private String replaceStyle(String header){
		if(header.indexOf("<") > 0 || header.lastIndexOf(">") > 0){
			header = header.substring(header.indexOf(">")+1, header.lastIndexOf("<"));
		}
		
		return header;
	}
}

