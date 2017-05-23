package com.rd.client.action.base.gpseq;

import java.util.Map;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class NewGpsAtcion implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{

	private SGTable table = null;
	private SGTable main_table = null;
	private Map<String, String> initMap = null;
	private SGForm view;
	private int initBtn =0;
	
	public NewGpsAtcion(SGTable p_table,Map<String, String> initMap,SGForm view) {
		table = p_table;
		this.initMap = initMap;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		newAction();
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		newAction();
	}

	private void newAction(){
		if(view != null && initBtn == 0){
			view.initAddBtn();
		}
		if(initBtn > 0){
			view.initBtn(initBtn);
		}
		table.startEditingNew();
		table.OP_FLAG = "A";
		if(main_table != null) {
			main_table.setData(new ListGridRecord[0]);
			//main_table.invalidateCache();
		}
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
						table.setEditValue(table.getRecords().length,"STATUS","DF252F0637784E9EA575CCACB64050FC");
						table.setEditValue(table.getRecords().length, key, value.equals("Y"));
					}
					else {
						table.setEditValue(table.getRecords().length,"STATUS","DF252F0637784E9EA575CCACB64050FC");
						table.setEditValue(table.getRecords().length, key, ObjUtil.ifNull(initMap.get(i18n_key), value));
					}
				}
			}
		}
	}
	public Map<String, String> getInitMap() {
		return initMap;
	}
	public void setInitMap(Map<String, String> initMap) {
		this.initMap = initMap;
	}
	
}
