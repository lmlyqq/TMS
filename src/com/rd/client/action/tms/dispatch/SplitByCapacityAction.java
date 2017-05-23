package com.rd.client.action.tms.dispatch;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 按运力拆分
 * @author yuanlei
 *
 */
public class SplitByCapacityAction implements ClickHandler {

	private SGForm view;
	private SGTable unshpmTable;
	private DispatchView dv;
	private TmsShipmentView tv;
	public SplitByCapacityAction(SGForm form, SGTable p_unshpmTable) {
		this.view = form;
		this.unshpmTable = p_unshpmTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		ListGridRecord[] records = unshpmTable.getSelection();
		
		if(records != null && records.length > 0) {
            doCancel();
		}
		else {
			SC.warn("未选择作业单!");
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doCancel() {
		ListGridRecord[] records = unshpmTable.getSelection();
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> typ_map = new HashMap<String, String>(); //车辆类型 
		
		if(records != null && records.length > 0) {
			final Record record = unshpmTable.getEditedRecord(unshpmTable.getRecordIndex(records[0]));
			for(int i = 0; i < records.length; i++) {
				Map selMap = unshpmTable.getEditValues(records[i]);				
				shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
				typ_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID"), ObjUtil.ifObjNull(record.getAttribute("VEHICLE_TYP_ID"), " ")).toString());
			}
		}
		
		listmap.put("1", shpm_map);
		listmap.put("2", typ_map);
		listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		Util.async.execProcedure(json, "sp_shpm_splitby_capacity(?,?,?,?)", new AsyncCallback<String>() {
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
