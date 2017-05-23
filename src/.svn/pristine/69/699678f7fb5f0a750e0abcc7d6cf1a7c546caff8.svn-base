package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 配车确认
 * @author yuanlei
 *
 */
public class LoadCancelConfirmAction implements ClickHandler {

	private SGTable loadTable;
	public LoadCancelConfirmAction(SGTable p_loadTable) {
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		
		ListGridRecord[] records = loadTable.getSelection();
		if(records != null && records.length > 0) {
			HashMap<String, Object> listmap = new HashMap<String, Object>(); 
			HashMap<String, String> load_map = new HashMap<String, String>(); //调度单   
			for(int i = 0; i < records.length; i++) {
				if(!records[i].getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.DISPATCHED_NAME)&&!records[i].getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.GET_BACK)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]配车状态为[" + records[i].getAttribute("DISPATCH_STAT_NAME") + "],不能执行取消确认!");
					return;
				}
				if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]非已创建状态,不能执行取消确认!");
					return;
				}
				load_map.put(Integer.toString(i+1), records[i].getAttribute("LOAD_NO"));
			}
			listmap.put("1", load_map);
			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_LOADNO_CANCELCONFIRM(?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						//刷新调度单
						ListGridRecord[] records = loadTable.getSelection();
						for(int i = 0; i < records.length; i++) {
							records[i].setAttribute("DISPATCH_STAT_NAME", StaticRef.NO_DISPATCH_NAME);
							loadTable.updateData(records[i]);
						}
						loadTable.redraw();
						
					}
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
		}
		else {
			SC.warn("未选择调度单!");
			return;
		}
	}
}
