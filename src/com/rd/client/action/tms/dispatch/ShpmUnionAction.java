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
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 取消行程拆分
 * @author yuanlei
 *
 */
public class ShpmUnionAction implements ClickHandler {

	private SGForm view;
	private SGTable unshpmTable;
	private DispatchView dv;
	private TmsShipmentView tv;
	public ShpmUnionAction(SGForm form, SGTable p_unshpmTable) {
		this.view = form;
		this.unshpmTable = p_unshpmTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		ListGridRecord[] records = unshpmTable.getSelection();
		
		if(records != null && records.length > 0) {
			if(records.length > 1) {
	            doCancel();
			}

		}
		else {
			SC.warn("请选择作业单!");
			return;
		}
	}
	
	public void doCancel() {
		ListGridRecord[] records = unshpmTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		for(int i = 0; i < records.length; i++) {
			shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
		}
		listmap.put("1", shpm_map);
		listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		Util.async.execProcedure(json, "SP_SHPM_UNION(?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
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
					
					//unshpmTable.selectRecord(0);
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

}
