package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 导出功能通用方法实现
 * @author fanglm
 * @created time 2010-09-03 10：45
 *
 */
public class CustomExportAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private ListGrid table;
	private HashMap<String, String> paramMap;
	
	public CustomExportAction(ListGrid table){
		this.table = table;
	}
	
	public CustomExportAction(ListGrid table, HashMap<String, String> paramMap) {
		this.table = table;
		this.paramMap = paramMap;
	}

	@Override
	public void onClick(ClickEvent event) {
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_NAME");
		fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		String header = list.get(1);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");
		//String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName"));
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		sql.append(where);
		if(paramMap == null || paramMap.isEmpty() || 
				!ObjUtil.isNotNull(paramMap.get("CUSTOMER_ID"))){
			MSGUtil.sayError("查询条件: 客户不能为空!");
			return;
		}
		Util.customExportUtil(header, fieldName, sql.toString(), paramMap);
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_NAME");
		fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		String header = list.get(1);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		sql.append(" ");
		//String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName"));
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		
		sql.append(where);
		//fanglm 2012-06-18 大批量导出，数据错乱问题，解决方法：增加排序功能
		if(sql.indexOf("ORDER BY") < 0 && sql.indexOf("order by") < 0){
			sql.append(" order by odr_time");
		}
		if(paramMap == null || paramMap.isEmpty() || 
				!ObjUtil.isNotNull(paramMap.get("CUSTOMER_ID"))){
			MSGUtil.sayError("查询条件: 客户不能为空!");
			return;
		}
		Util.customExportUtil(header, fieldName, sql.toString(), paramMap);
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

