package com.rd.client.common.action;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 列表右键全选功能
 * @author fanglm
 *
 */
public class AllSelectAction implements ClickHandler {
	
	private ListGrid table;
	
	public AllSelectAction(ListGrid table){
		this.table = table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		table.selectAllRecords();
	}

}
