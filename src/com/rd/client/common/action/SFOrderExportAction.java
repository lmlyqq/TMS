package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.List;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 原始订单导出
 * @author fanglm
 * @created time 2010-09-03 10：45
 *
 */
public class SFOrderExportAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private ListGrid table;
	
	public SFOrderExportAction(ListGrid table){
		this.table = table;
	}
	
	public SFOrderExportAction(ListGrid table, String orderby) {
		this.table = table;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		if(table.getSelectedRecord()==null){
			SC.say("请选中记录操作");
			return;
		}
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_NAME");
		fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		fieldName = fieldName.replaceAll("ORD_IMA,", "");
		fieldName = fieldName.replaceAll("ODR_TYP_NAME", "ODR_TYP");
		fieldName = fieldName.replaceAll("BIZ_TYP_NAME", "BIZ_TYP");
		String header = list.get(1);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		sql.append(where);
		Util.sfOrderExportUtil(header, fieldName, sql.toString(),table.getSelectedRecord().getAttribute("ODR_NO"));
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		int pos = fieldName.indexOf("SUPLR_NAME");
		if(pos < 0) {
			fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_NAME");
		}
		else {
			fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_ID");
		}
		fieldName = fieldName.replaceAll("ODR_TYP_NAME", "ODR_TYP");
		//String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName"));
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		if(where.indexOf("VEHICLE_TYP_ID_NAME") < 0) {
			fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		}
		String header = list.get(1);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");

		sql.append(where);
		//fanglm 2012-06-18 大批量导出，数据错乱问题，解决方法：增加排序功能
		if(sql.indexOf("ORDER BY") < 0 && sql.indexOf("order by") < 0){
			sql.append(" order by addtime");
		}
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

