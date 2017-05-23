package com.rd.client.action.tms.shpmreceipt;

import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShpmReceiptView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class CancelEditAction  implements ClickHandler {
	private SGTable table = null;
	private TmsShpmReceiptView view;
	
	public CancelEditAction(SGTable p_table,TmsShpmReceiptView view) {
		this.table = p_table;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		table.discardAllEdits();
		view.canButton.disable();
		
	}

	

}
