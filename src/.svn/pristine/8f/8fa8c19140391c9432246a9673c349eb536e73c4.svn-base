package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.rd.client.view.tms.JourneySplitWin;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 行程拆分
 * 2012/03/28
 * @author yuanlei
 *
 */
public class SplitJouryShpmAction implements ClickHandler {
	
	private SGForm view;
	private SGTable halfTable;
	private ListGridRecord[] allRecords;
	private DispatchView dv;
	private TmsShipmentView tv;
	private JourneySplitWin win;
	
	public SplitJouryShpmAction(JourneySplitWin window, SGForm form, ListGridRecord[] records, SGTable halfWayTable) {
		this.allRecords = records;
		this.halfTable = halfWayTable;
		this.view = form;
		this.win  = window;
	}
	
	@Override
	public void onClick(ClickEvent event) {	
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单 
		HashMap<String, String> addr_map = new HashMap<String, String>(); //地址点   
		ListGridRecord[] records = halfTable.getRecords();
		if(allRecords != null && allRecords.length > 0 && records != null && records.length > 0) {
			for(int i = 0; i < allRecords.length; i++) {
				shpm_map.put(Integer.toString(i+1), allRecords[i].getAttribute("SHPM_NO"));
			}
			for(int i = 0; i < records.length; i++) {
				addr_map.put(Integer.toString(i+1), records[i].getAttribute("ADDR_ID"));
			}
			listmap.put("1", shpm_map);
			listmap.put("2", addr_map);
			listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_SHPM_JNRY_SPLIT(?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						
						win.destroy();
						win.fireCloseEvent("Popup window has been destroyed");
						//刷新待调订单列表	
						if(view instanceof DispatchView){
							if(dv == null)
								dv = (DispatchView)view;
							dv.qryUnshpmTableAction.doRefresh(true);	
						}else if(view instanceof TmsShipmentView){
							if(tv == null)
								tv = (TmsShipmentView)view;
							tv.refreshTableAction.doRefresh(true);	
						}
					}
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
			});
		}
		else {
			MSGUtil.sayWarning("无效的拆分操作：未选择作业单或未添加中途点!");
			return;
		}
	}

}
