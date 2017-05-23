package com.rd.client.view.system;

import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.OnlineUserDS;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.grid.ListGridField;

public class OnlineUserView extends Window{
	
/**
 * 系统管理 -> 在线用户
 * @author wangjun
 */
	
	private int width = 665;
	private int height = 300;
	private String top = "25%";
	private String left = "22%";
	private String title = "在线用户";
	public  Window window;
	private DataSource ds;
	private SGTable table;



	public OnlineUserView() {
	
	}
	

	public OnlineUserView(int width, int height, String top, String left,
			String title, Window window, DataSource ds) {
		this.width = width;
		this.height = height;
		this.top = top;
		this.left = left;
		this.title = title;
		this.window = window;
		this.ds = ds;
	}

	public Window getViewPanel(){	
		ds=OnlineUserDS.getInstance("SYS_USER_ONLINE","V_USER");
		table = new SGTable(ds, "100%", "70%", false, true, false);
		table.fetchData();
		table.setCanEdit(false);
	    createListFields(table);	
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.setShowCloseButton(true);
		window.addItem(table);
		window.show();
		return window;
	}


	private void createListFields(SGTable table) {
		ListGridField USER_ID = new ListGridField("USER_ID",Util.TI18N.USER_ID(),80);
		ListGridField USER_NAME = new ListGridField("USER_NAME",Util.TI18N.USER_NAME(),80);
		ListGridField DEFAULT_ORG_ID_NAME = new ListGridField("DEFAULT_ORG_ID_NAME",Util.TI18N.USER_ORG_ID(),100);
		ListGridField CUR_STATUS = new ListGridField("CUR_STATUS_NAME",Util.TI18N.CUR_STATUS(),60);
		ListGridField IP_ADDR = new ListGridField("IP_ADDR", Util.TI18N.IP_ADDR(), 90);
		ListGridField TIME = new ListGridField("LOGIN_TIME",Util.TI18N.LOGIN_TIME(),100);
		ListGridField ROLE_ID_NAME = new ListGridField("ROLE_ID_NAME",Util.TI18N.ROLE_NAME(),100);
//		ListGridField USER_GROUP = new ListGridField("USERGRP_ID_NAME",Util.TI18N.USER_GROUP(),100);
//		ListGridField ACTIVE_FLAG = new ListGridField("ACTIVE_FLAG",Util.TI18N.ACTIVE_FLAG(),50);
//	    ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);
		table.setFields(USER_ID,USER_NAME,DEFAULT_ORG_ID_NAME,CUR_STATUS, IP_ADDR, TIME , ROLE_ID_NAME);	
	}
}
