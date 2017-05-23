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
 * 取消拆分
 * @author yuanlei
 *
 */
public class CancelSplitChangeAction implements ClickHandler {

	private SGForm view;
	private SGTable unshpmTable;
	private SGTable loadTable;
	private DispatchView dv;
	private TmsShipmentView tv;
	public CancelSplitChangeAction(SGForm form, SGTable p_unshpmTable, SGTable loadTable) {
		this.loadTable=loadTable;
		this.view = form;
		this.unshpmTable = p_unshpmTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		ListGridRecord[] records = unshpmTable.getSelection();
		
		/*if(unshpmTable.getSelectedRecord().getAttribute("PRINT_FLAG").equals("N")){
			
			SC.warn("作业单"+unshpmTable.getSelectedRecord().getAttribute("SHPM_NO")+" 已打印提货单不允许取消拆分！");
			return;
		}*/
		
		if(records != null && records.length > 0) {
			if(records.length > 1) {
				SC.warn("无法批量操作!");
			}
            doCancel();
		}
		else {
			SC.warn("未选择作业单!");
			return;
		}
	}
	
	public void doCancel() {
		ListGridRecord[] records = unshpmTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		
		ListGridRecord rec = records[0];
		listmap.put("1", rec.getAttribute("PARN_SHPM_NO"));
		listmap.put("2", rec.getAttribute("SHPM_NO"));
		listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		Util.async.execProcedure(json, "SP_SHPM_CANCELSPLIT_CHANGEQNTY(?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.sayInfo(result.substring(2));
					
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
					loadTable.collapseRecord(loadTable.getSelectedRecord()); 
					loadTable.expandRecord(loadTable.getSelectedRecord());
					
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
