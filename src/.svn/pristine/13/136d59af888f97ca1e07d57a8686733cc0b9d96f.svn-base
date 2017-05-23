package com.rd.client.common.action;

import com.rd.client.view.tms.TmsShpmReceiptView;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class ShpmSelectAction implements ClickHandler {
	private ListGrid table;
	
	public ShpmSelectAction(ListGrid table, TmsShpmReceiptView view) {
		this.table = table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {

		ListGridRecord record = table.getSelectedRecord();

		ListGridRecord[] re = table.getRecords();
		for (int i = 0; i < re.length; i++) {
			if (record.getAttribute("LOAD_NO").equals(
					re[i].getAttribute("LOAD_NO"))) {
				table.selectRecord(re[i]);
			}
		}
		table.redraw();

	
	}


}
