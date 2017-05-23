package com.rd.client.action.tms.track;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.PickLoadView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理--运输执行--运输跟踪--取消到货按钮
 * @author fanglm
 *
 */
public class CancleLoadAction implements ClickHandler {
	
	private PickLoadView view;
	
	public CancleLoadAction(PickLoadView view){
		this.view = view;
	}

	//执行确认
	private void doConfirm(){
		final String tab_flag=view.TabSet.getSelectedTab().getID();
		String load_no="";
		 //选中记录调度单号
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		
		HashMap<String, String> shpm_no_list = new HashMap<String, String>(); // 作业单号
		
		switch (Integer.parseInt(tab_flag.substring(0,1))){
		case 0:
			load_no  = view.loadReocrd[0].getAttribute("LOAD_NO");
			StringBuffer sb = new StringBuffer();
			ListGridRecord[] shpm_list = view.shpmTable.getSelection();
			for(int i=0 ; i<shpm_list.length ; i++){
				String status = shpm_list[i].getAttribute("STATUS");
				if(Integer.parseInt(StaticRef.SHPM_DIPATCH) <= Integer.parseInt(status)
						&& Integer.parseInt(StaticRef.SHPM_UNLOAD) > Integer.parseInt(status)){
					shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
				}
				else{
					sb.append(shpm_list[i].getAttribute("SHPM_NO"));
					sb.append(",");
				}
			}
			if(sb.length() > 0){
				MSGUtil.sayError("作业单:"+ sb.substring(0,sb.length()-1) + "未装车,不能执行【取消】操作！");
				return;
			}
			
			if(shpm_list.length == 0){
				MSGUtil.sayError("未选择作业单，不能执行【取消】！");
				return ;
			}
			break;
		case 1:
			load_no=view.loadTable.getSelectedRecord().getAttribute("LOAD_NO");
			shpm_no_list.put("1", " ");
			break;
		default :
			break;
		}
		listMap.put("1", load_no);
		listMap.put("2", shpm_no_list);
		listMap.put("3",login_user);
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_SHPM_CANCELLOAD(?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
					MSGUtil.showOperSuccess();
					switch (Integer.parseInt(tab_flag.substring(0,1))){
					case 0:
						if(view.shpmTable != null){
							ListGridRecord[] shpm_list = view.shpmTable.getSelection();
							for(int i=0;i<shpm_list.length;i++){
								
								shpm_list[i].setAttribute("LOAD_STAT", StaticRef.NO_LOAD_NAME);
								//shpm_list[i].setAttribute("ARRIVE_WHSE_TIME","");
								shpm_list[i].setAttribute("START_LOAD_TIME", "");
								shpm_list[i].setAttribute("END_LOAD_TIME", "");
								shpm_list[i].setAttribute("LOAD_NOTES", "");
							    shpm_list[i].setAttribute("UDF3", "");
								shpm_list[i].setAttribute("UDF4", "");
							}
							view.shpmTable.redraw();
						}
						break;
					case 1:
						if(view.loadTable!=null){
							ListGridRecord[] shpm_list = view.loadTable.getSelection();
							for(int i=0;i<shpm_list.length;i++){
								shpm_list[i].setAttribute("LOAD_STAT", StaticRef.NO_LOAD);
								shpm_list[i].setAttribute("LOAD_STAT_NAME", StaticRef.NO_LOAD_NAME);
//								shpm_list[i].setAttribute("START_LOAD_TIME", "");
//								shpm_list[i].setAttribute("END_LOAD_TIME", "");
//								shpm_list[i].setAttribute("LOAD_NOTES", "");
//							    shpm_list[i].setAttribute("UDF3", "");
//								shpm_list[i].setAttribute("UDF4", "");
							}
							view.loadTable.redraw();
						}
						break;
					default :
						break;
					}
					view.panel1.clearValues();
					view.finishButton.enable();
					view.cancelLoadButton.disable();
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		doConfirm();
	}

}
