package com.rd.client.action.tms.track;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CancelRecepitAction implements ClickHandler{
	private TmsTrackView view;
	
	public CancelRecepitAction(TmsTrackView view){
		this.view=view;
	}
	@Override
	public void onClick(ClickEvent event) {
		final String tab_flag=view.tabSet.getSelectedTab().getID();
		ListGridRecord[] records;
		HashMap<String,String> shpm_list=new HashMap<String,String>();
		
		HashMap<String,Object> listMap=new HashMap<String,Object>();
		String user_id=LoginCache.getLoginUser().getUSER_ID();
		final String UDF1=ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue(), " ").toString();
		final String UDF2=ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString();
		
		switch(Integer.parseInt(tab_flag.substring(0,1))){
		case 0:
			records=view.shpmTable.getSelection();
			if(records==null || records.length==0){
				MSGUtil.sayError("未选择作业单，不能进行【取消拒收】");
				return;
			}
			for(int i=0;i<records.length;i++){
				String status=records[i].getAttribute("STATUS");
				if(!StaticRef.SHPM_REJECT.equals(status)){
					MSGUtil.sayError("作业单【"+records[i].getAttribute("SHPM_NO")+"】不能取消拒收");
					return;
				}
				shpm_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(records[i].getAttribute("SHPM_NO"), " ").toString());
			}
			break;
		case 1:
			shpm_list.put("1", " ");
			break;
		case 2:
			shpm_list.put("1", " ");
			break;
		default :
			break;
		}
		
		listMap.put("1", shpm_list);
		listMap.put("2", user_id);
		listMap.put("3", UDF1);
		listMap.put("4", UDF2);
		
		
		String json=Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_SHPM_CANCELREJECT(?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
					if(view.shpmTable!=null){
						ListGridRecord[] records=view.shpmTable.getSelection();
						for(int i=0;i<records.length;i++){
							records[i].setAttribute("STATUS", StaticRef.SHPM_LOAD);
							records[i].setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
						}
						view.shpmTable.redraw();
					}
					switch(Integer.parseInt(tab_flag.substring(0,1))){
					case 0:
						view.initButton(true, false, true, false, true);
						break;
					case 1:
						view.initButton(true, false, false, false, false);
						break;
					case 2:
						view.initButton(true, false, false, false, false);
						break;
					default :
						break;
					}
				}else{
					MSGUtil.sayError(result);
				}
			}
			@Override
			public void onFailure(Throwable caught) {
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
	}
	
}
