package com.rd.client.action.tms.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;

import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class LoadGetBackAction implements ClickHandler {
    
	private SGTable loadTable;
	
	public LoadGetBackAction(){
		
	}
	
	public LoadGetBackAction(SGTable loadTable) {
		this.loadTable = loadTable;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		final ListGridRecord record = loadTable.getSelectedRecord();
		if(!record.getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.DISPATCHED_NAME)){
			SC.warn("调度单[" + record.getAttribute("LOAD_NO") + "]配车状态为[" + record.getAttribute("DISPATCH_STAT_NAME") + "],不能执行打回!");
			return;
		}
		
		StringBuffer sf = new StringBuffer();
		sf.append("Update TRANS_LOAD_HEADER set DISPATCH_STAT='F2D45D20FE984D0AAA5284649AD50F7F' where load_no='"+record.getAttribute("LOAD_NO")+"'");
		
		Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				MSGUtil.showOperSuccess();
				record.setAttribute("DISPATCH_STAT_NAME", StaticRef.GET_BACK);
				loadTable.updateData(record);
				loadTable.redraw();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
