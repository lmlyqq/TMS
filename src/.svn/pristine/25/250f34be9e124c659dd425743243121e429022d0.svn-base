package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.List;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * 调用存储过程实现导出功能
 * @author fanglm
 * @created time 2011-7-13 14:45
 *
 */
public class ExportByProAction implements ClickHandler{
	
	private ListGrid table;
	
	public ExportByProAction(ListGrid table){
		this.table = table;
	}
	
	public ExportByProAction(ListGrid table, String orderby) {
		this.table = table;
	}

	@Override
	public void onClick(ClickEvent event) {
		List<String>  list = getFieldName();
		String fieldName = list.get(0);
		String header = list.get(1);
		//String pro_name = table.getDataSource().getAttribute("tableName"); //存储过程名称
		String pro_name = table.getDataSource().getTableName(); //存储过程名称
		StringBuffer sql = new StringBuffer();
		sql.append("select * ");
		//sql.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName")));
		sql.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName()));
		Util.exportUtil(header, fieldName, sql.toString(),pro_name);
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

