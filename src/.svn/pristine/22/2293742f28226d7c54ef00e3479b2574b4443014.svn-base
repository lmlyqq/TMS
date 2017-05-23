package com.rd.client.action.tms.track;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理--运输执行--运输跟踪--拒收按钮
 * @author fanglm
 *
 */
public class RejectReceiptAction implements ClickHandler {
	
	private TmsTrackView view;
	public Record item;
	
	public RejectReceiptAction(TmsTrackView view){
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		final String tab_flag=view.tabSet.getSelectedTab().getID();
		final String UDF1 = ObjUtil.ifObjNull(view.panel.getItem("UDF1").getValue()," ").toString();//开门温度
		final String UDF2 = ObjUtil.ifObjNull(view.panel.getItem("UDF2").getValue()," ").toString();//关门温度
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		
		HashMap<String, String> shpm_no_list = new HashMap<String, String>(); // 作业单号
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		ListGridRecord[] shpm_list;
		
		switch (Integer.parseInt(tab_flag.substring(0,1))){
		case 0:
			shpm_list= view.shpmTable.getSelection();
			if(shpm_list.length == 0){
				MSGUtil.sayError("未选择作业单，不能执行【确认签收】！");
				return;
			}
			for(int i=0 ; i<shpm_list.length ; i++){
				String status = shpm_list[i].getAttribute("STATUS");
				if(!StaticRef.SHPM_LOAD.equals(status)){
					MSGUtil.sayError("作业单[" + shpm_list[i].getAttribute("SHPM_NO") + "]不能进行拒收操作！");
					return;
				}
				shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
			}
			break;
		case 1:
			shpm_no_list.put("1", " ");
			break;
		case 2:
			shpm_no_list.put("1", " ");
			break;
		default :
			break;
		}
		listMap.put("1", shpm_no_list);
		listMap.put("2", login_user);
		listMap.put("3", UDF1);
		listMap.put("4", UDF2);
		
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_SHPM_REJECT(?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
					if(view.shpmTable != null){
						ListGridRecord[] records = view.shpmTable.getSelection();
						for(int i=0;i<records.length;i++){
							records[i].setAttribute("STATUS", StaticRef.SHPM_REJECT);
							records[i].setAttribute("STATUS_NAME", StaticRef.SHPM_REJECT_NAME);
						}
						view.shpmTable.redraw();
					}
					switch (Integer.parseInt(tab_flag.substring(0,1))){
					case 0:
						view.initButton(false, false, false, true, false);
						break;
					case 1:
						view.initButton(false);
						break;
					case 2:
						view.initButton(false);
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
				// TODO Auto-generated method stub
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
	}

}
