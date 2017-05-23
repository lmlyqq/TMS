package com.rd.client.action.tms.track;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.view.tms.PickLoadView;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class SelectSameAction implements ClickHandler {

	private ListGrid table;
	private TmsTrackView ttView;
	private PickLoadView plView;
	

	public SelectSameAction(ListGrid table) {
		this.table = table;
	}

	public SelectSameAction(ListGrid table, TmsTrackView ttView) {
		this.table = table;
		this.ttView = ttView;
	}
	
	public SelectSameAction(ListGrid table, PickLoadView plView) {
		this.table = table;
		this.plView = plView;
	}
	

	@Override
	public void onClick(MenuItemClickEvent event) {
		if(ttView != null){
			click(event, ttView.tabSelect);
		}else if(plView != null){
			click(event, plView.tabSelect);
		}
	}
	
	private void click(MenuItemClickEvent event, int tabSelect){
		ListGridRecord record = table.getSelectedRecord();
		ListGridRecord[] re = table.getRecords();
		String status = record.getAttribute("STATUS");
		for (int i = 0; i < re.length; i++) {
			if (tabSelect == 0) {
				if (record.getAttribute("LOAD_NO").equals(
						re[i].getAttribute("LOAD_NO"))) {
					if(status.equals(re[i] .getAttribute("STATUS"))){
						table.selectRecord(re[i]);
					}
				}
			} else {
				if (record.getAttribute("LOAD_NO").equals(re[i].getAttribute("LOAD_NO"))){
					if(StaticRef.SHPM_LOAD.equals(status)||
							StaticRef.SHPM_UNLOAD.equals(status)){
						if(status.equals(re[i] .getAttribute("STATUS"))){
							
							table.selectRecord(re[i]);
						}
					}
					else {
						table.deselectRecord(record);
						MSGUtil.sayWarning("您选择的单证状态不符合操作要求，请重新选择！");
						return;
					}
				}
			}
		}
		
		

		table.redraw();
	}

}
