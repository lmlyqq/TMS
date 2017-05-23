package com.rd.client.action.tms;

import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class CancelTrackAction implements ClickHandler {

	private SGTable table;
	private TmsTrackView view;
	
	public CancelTrackAction(SGTable p_table,TmsTrackView view) {
		table = p_table;
		this.view = view;
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		table.discardAllEdits();
		view.enableOrDisables(view.add_tr_map, true);
		view.enableOrDisables(view.save_tr_map, false);
		view.enableOrDisables(view.del_tr_map, true);
	}

}
