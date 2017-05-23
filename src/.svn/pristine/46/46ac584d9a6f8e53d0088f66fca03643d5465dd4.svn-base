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
 * 运输管理--运输执行--运输跟踪--滞留按钮
 * @author fanglm
 *
 */
public class HoldToDispatchAction implements ClickHandler {
	
	private TmsTrackView view;
	public Record item;
	private ListGridRecord[] shpm_list;
	
	public HoldToDispatchAction(TmsTrackView view){
		this.view = view;
	}

	//执行确认
	private void doConfirm(final String unload_time){
		String load_no=" ";
		final String tab_flag=view.tabSet.getSelectedTab().getID();
		HashMap<String, String> shpm_no_list = new HashMap<String, String>();
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		final String ABNOMAL_NOTE = ObjUtil.ifObjNull(view.panel.getItem("ABNOMAL_NOTES").getValue()," ").toString();//异常描述
		switch (Integer.parseInt(tab_flag.substring(0,1))) {
		case 0:
			load_no  = view.loadReocrd[0].getAttribute("LOAD_NO"); //选中记录调度单号
			 // 作业单号
			StringBuffer sb = new StringBuffer();
			shpm_list = view.shpmTable.getSelection();
			for(int i=0 ; i<shpm_list.length ; i++){
				String status = shpm_list[i].getAttribute("STATUS");
				if(StaticRef.SHPM_LOAD.equals(status)){
					shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
				}
				else{
					sb.append(shpm_list[i].getAttribute("SHPM_NO"));
					sb.append(",");
				}
			}
			if(sb.length() > 0){
				MSGUtil.sayError("部分作业单非【已发运】状态:"+ sb.substring(0,sb.length()-1) + ",不能进行滞留操作！");
				return;
			}
			
			if(shpm_list.length == 0){
				MSGUtil.sayError("未选择作业单，不能执行【滞留】操作！");
				return ;
			}
			break;
		case 1:
			load_no=view.loadTable.getSelectedRecord().getAttribute("LOAD_NO");
			shpm_no_list.put("1", " ");
			break;
		case 2:
			load_no=view.unloadTable.getSelectedRecord().getAttribute("LOAD_NO");
			shpm_no_list.put("1", " ");
			break;
		default :
			break;
		}
		listMap.put("1", load_no);
		listMap.put("2", shpm_no_list);
		listMap.put("3", unload_time);
		listMap.put("4", ABNOMAL_NOTE);
		listMap.put("5",login_user);
		
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, "SP_LOAD_HOLDTODISPATCH(?,?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
					if(view.shpmTable != null){
						for(int i=0;i<shpm_list.length;i++){							
							view.shpmTable.removeData(shpm_list[i]);
						}
						view.shpmTable.redraw();
					}
					view.initButton(true, false, true, false, false);
					switch (Integer.parseInt(tab_flag.substring(0,1))) {
					case 0:
						//20150310 DAVID  滞留之后所有按钮应该都不可用
						view.initButton(false, false, false, false, false);
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
				// TODO Auto-generated method stub
				SC.say("服务器连接已中断，请重新登录!");
			}
		});
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		final Object unload_time = view.panel.getValue("UNLOAD_TIME"); //实际签收时间
		if(!ObjUtil.isNotNull(unload_time)){
			MSGUtil.sayError("到货签收时间不能为空！");
			return;
		} 
		
		doConfirm(view.panel.getValue("UNLOAD_TIME").toString());
		//校验到货签收时间不能大于当前系统时间
//		Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {
//			
//			@Override
//			public void onSuccess(String result) {
//				 if(DateUtil.isAfter(unload_time.toString(),result)){
//					 doConfirm(view.panel.getValue("UNLOAD_TIME").toString());
//				 }else{
//					 MSGUtil.sayError("到货签收时间不能大于当前系统时间！");
//					 return;
//				 }
//				
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//			}
//		});

	}

}
