package com.rd.client.action.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.ChangeRDCView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ChangeRDCSuccAction implements ClickHandler{

	private SGTable rdctable;
	private ChangeRDCView view;
	
	public ChangeRDCSuccAction(SGTable rdctable , ChangeRDCView view){
		this.rdctable = rdctable;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] recs = rdctable.getSelection();
		if (recs.length>0) {
			HashMap<String , String> rdc_list = new HashMap<String, String>();
			String user_id = LoginCache.getLoginUser().getUSER_ID();
			String exec_org_id = LoginCache.getLoginUser().getDEFAULT_ORG_ID();
			for (int i = 0; i < recs.length; i++) {
				rdc_list.put(Integer.toString(i+1), ObjUtil.ifNull(recs[i].getAttribute("RDC_NO"), ""));
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("1", rdc_list);
			map.put("2", user_id);
			map.put("3", exec_org_id);
			
			Util.async.execProcedure(Util.mapToJson(map), "SP_RDC_CHANGED(?,?,?,?)", new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if (StaticRef.SUCCESS_CODE.equals(result.substring(0,2))) {
						MSGUtil.showOperSuccess();
						rdctable.invalidateCache();
						rdctable.discardAllEdits();
						for(ListGridRecord rec : recs){
							rec.setAttribute("STATUS", "30");
							rec.setAttribute("STATUS_NAME", "已转仓");
							rdctable.selectRecord(rdctable.getRecordIndex(rec));
						}
						rdctable.redraw();
						view.initButton(false, false, false, false, true);
					}else {
						MSGUtil.sayError(result);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}

}
