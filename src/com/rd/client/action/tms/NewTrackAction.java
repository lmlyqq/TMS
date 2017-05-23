package com.rd.client.action.tms;

import java.util.Map;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class NewTrackAction implements ClickHandler{

	private SGTable table;
	private SGTable main_table;
	private TmsTrackView view;
	private Map<String, String> initMap;
	
	public NewTrackAction(SGTable p_table, SGTable p_maintable,Map<String, String> initMap,TmsTrackView view){
		this.table = p_table;
		this.main_table = p_maintable;
		this.initMap = initMap;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		view.enableOrDisables(view.add_tr_map, false);
		view.enableOrDisables(view.save_tr_map, true);
		view.enableOrDisables(view.del_tr_map, false);
		table.startEditingNew();
		table.OP_FLAG = "A";
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					table.setEditValue(table.getRecords().length,key, LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					if(value.equals("Y") || value.equals("N")) {
						table.setEditValue(table.getRecords().length, key, value.equals("Y"));
					}
					else {
						table.setEditValue(table.getRecords().length, key, ObjUtil.ifNull(initMap.get(i18n_key), value));
					}
				}
				table.setEditValue(table.getRecords().length,"TRACE_TIME",getCurTime());
				table.setEditValue(table.getRecords().length,"LOAD_NO",main_table.getSelectedRecord().getAttribute("LOAD_NO"));
			}
		}
	}

	
	public static native String getCurTime() /*-{

	var now = new Date();
	var year=now.getFullYear();
	var m=now.getMonth()+1;
	var month = (m < 10) ? '0' + m : m;
	var day=now.getDate();
	var hour=now.getHours();
	var minute=now.getMinutes();
	if (minute < 10) {
	    minute = "0" + minute;
	}
	var res = year+"-"+month+"-"+ day + " " + hour + ":" + minute;
	return res;
}-*/;
}
