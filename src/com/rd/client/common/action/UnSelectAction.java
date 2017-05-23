package com.rd.client.common.action;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 列表右键反选功能
 * @author fanglm
 *
 */
public class UnSelectAction implements ClickHandler {
	
	private ListGrid table;
	
	public UnSelectAction(ListGrid table){
		this.table = table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		ListGridRecord[] records = table.getSelection();
		table.selectAllRecords();
		table.deselectRecords(records);
	}

}
