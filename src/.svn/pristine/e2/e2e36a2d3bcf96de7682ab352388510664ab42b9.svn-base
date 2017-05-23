package com.rd.client.action.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class StopWatch implements ClickHandler{
	private SGTable table;

	public StopWatch(SGTable table){
		this.table=table;

	}
	@Override
	public void onClick(ClickEvent event) {
		String user_id=LoginCache.getLoginUser().getUSER_ID();
		if(ObjUtil.isNotNull(table.getSelection())){
			if(table.getSelection().length>1){
				MSGUtil.sayError("【停止监控】不允许同时对多个目标进行操作");
				return;
			}
		}else{
			MSGUtil.sayError("请先选择一个目标");
			return;
		}
		String plate_no=ObjUtil.ifNull(table.getSelectedRecord().getAttribute("PLATE_NO"), ""); 
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("1", plate_no);
		map.put("2", user_id);
		Util.async.execProcedure(Util.mapToJson(map), "SF_PLSS_STOP_WAATCH(?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				MSGUtil.showOperSuccess();
			}
				
			@Override
			public void onFailure(Throwable caught) {
					
			}
		});
	}

}
