package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class ExportAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private ListGrid table;
	
	public ExportAction(ListGrid table){
		this.table = table;
	}
	
	public ExportAction(ListGrid table, String orderby) {
		this.table = table;
	}

	@Override
	public void onClick(ClickEvent event) {
		//Util.downLoadUtil(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD1"));
		//Util.downLoadUtil(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD2"));
		String key = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD1"),"");
		String value = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD2"),"");	
		String alias = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLALIAS"),"");
		List<String>  list = getFieldName2(key,value,alias);

		String fieldName = list.get(0);
		//fieldName = fieldName.replaceAll("SUPLR_ID_NAME", "SUPLR_NAME");
		//fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		fieldName = fieldName.replaceAll("t.ORD_IMA,", "").replaceAll("ORD_IMA,", "");
		String header = list.get(1);
		String fields = list.get(2);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		//System.out.println("99999999999999999999"+fieldName.toString());
		//Log4j.info("fieldName", fieldName.toString());
		//Util.downLoadUtil(fieldName);
		sql.append(" ");
		//String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName"));
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		//sql.append(ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getAttribute("tableName")));
		sql.append(where);
		Util.exportUtil(header, fields, sql.toString());
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		String key = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD1"),"");
		String value = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLFIELD2"),"");
		String alias = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "SQLALIAS"),"");
		System.out.println(key);
		
//		ListGridField[] grid = table.getFields();
//		List<String> list1=new ArrayList<String>();
//		for(int i=0;i<grid.length;i++){
//			list1.add(grid[i].getName().toString());
//		}
//		Util.getFieldName2(list1,key,value,alias);
		
		List<String>  list = getFieldName2(key,value,alias);
		String fieldName = list.get(0);
		fieldName = fieldName.replaceAll("t.ORD_IMA,", "").replaceAll("ORD_IMA,", "");
		String where = ObjUtil.ifNull(JSOHelper.getAttribute(table.getJsObj(), "WHERE"), "FROM "+table.getDataSource().getTableName());
		/*if(where.indexOf("VEHICLE_TYP_ID_NAME") < 0) {
			fieldName = fieldName.replaceAll("VEHICLE_TYP_ID_NAME", "VEHICLE_TYP_ID");
		}*/
		String header = list.get(1);
		String fields = list.get(2);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(fieldName.toString());
		//System.out.println("99999999999999999999"+fieldName.toString());
		//Log4j.info("fieldName", fieldName.toString());
		sql.append(" ");
		//Util.downLoadUtil(fieldName);
		sql.append(where);
		//fanglm 2012-06-18 大批量导出，数据错乱问题，解决方法：增加排序功能
		if(sql.indexOf("ORDER BY") < 0 && sql.indexOf("order by") < 0){
			sql.append(" order by addtime");
		}
		Util.exportUtil(header, fields, sql.toString());
	}
	
	/*private List<String> getFieldName(){
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
	}*/
	
	private List<String> getFieldName2(String key, String value, String alias){
		HashMap<String,String> map = new HashMap<String,String>();
		System.out.println(key);
		System.out.println(value);
		if(key.indexOf(",") > 0) {
			String[] tmpkey = key.split(",");
			String[] tmpval = value.split(",");
			for(int i = 0;i < tmpkey.length; i++) {
				System.out.println(tmpkey[i]);
				System.out.println(tmpval[i]);
				String k= tmpkey[i];
				k=k.replace("\"", "");
				String v=tmpval[i];
				v=v.replace("\"", "");
				map.put(k,v);
			}
		}
		else {
			key=key.replace("\"", "");
			value=value.replace("\"", "");
			map.put(key, value);
		}
		ListGridField[] grid = table.getFields();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql1 = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		String add_char = "$72v";
		String add_char2 = "$63n";
		for(int i=1;i<grid.length;i++){

			if(!grid[i].getName().equals(add_char) && !grid[i].getName().equals(add_char2)) {
				if(grid[i].getName().indexOf("_NAME") > 0 && key.toUpperCase().indexOf(grid[i].getName().toUpperCase()) >= 0) {
					System.out.println(grid[i].getName());
					sql.append(map.get(grid[i].getName()) + " AS " + grid[i].getName());
				}
				else {
					if(ObjUtil.isNotNull(alias)) {
						sql.append(alias + "." + grid[i].getName());
					}
					else {
						sql.append(grid[i].getName());
					}
				}
				sql1.append(replaceStyle(grid[i].getTitle()));
				sql.append(",");
				sql1.append(",");
				sql2.append(grid[i].getName());
				sql2.append(",");
			}
		}
		List<String> list = new ArrayList<String>();
		list.add(sql.substring(0, sql.length()-1));
		list.add(sql1.substring(0, sql1.length()-1));
		list.add(sql2.substring(0, sql2.length()-1));
		
		return list;
	}
	
	private String replaceStyle(String header){
		if(header.indexOf("<") > 0 || header.lastIndexOf(">") > 0){
			header = header.substring(header.indexOf(">")+1, header.lastIndexOf("<"));
		}
		
		return header;
	}
}

