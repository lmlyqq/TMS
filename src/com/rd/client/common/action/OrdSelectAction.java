package com.rd.client.common.action;

import com.rd.client.view.tms.TmsOdrReceiptView;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;



public class OrdSelectAction implements ClickHandler {
	private ListGrid table;
	@SuppressWarnings("unused")
	private TmsOdrReceiptView view;
	
	public OrdSelectAction(ListGrid table, TmsOdrReceiptView view) {
		this.table = table;
		this.view = view;
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
