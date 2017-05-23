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
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理--运输执行--运输跟踪--取消签收按钮
 * @author fanglm
 *
 */
public class CancleReceiptAction implements ClickHandler {
	
	private TmsTrackView view;
	
	public CancleReceiptAction(TmsTrackView view){
		this.view = view;
	}

	//执行确认
	private void doConfirm(){
		final String tab_flag=view.tabSet.getSelectedTab().getID();
		String load_no  = ""; //选中记录调度单号
		String unload_id="";
		String proName="";
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		
		HashMap<String, String> shpm_no_list = new HashMap<String, String>(); // 作业单号
		StringBuffer sb = new StringBuffer();
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		switch (Integer.parseInt(tab_flag.substring(0,1))){
		case 0:
			load_no=view.loadReocrd[0].getAttribute("LOAD_NO");
			ListGridRecord[] shpm_list = view.shpmTable.getSelection();
			for(int i=0 ; i<shpm_list.length ; i++){
				String status = shpm_list[i].getAttribute("STATUS");
				if(StaticRef.SHPM_UNLOAD.equals(status)){
					shpm_no_list.put(String.valueOf(i+1), ObjUtil.ifObjNull(shpm_list[i].getAttribute("SHPM_NO")," ").toString());
				}else{
					sb.append(shpm_list[i].getAttribute("SHPM_NO"));
					sb.append(",");
				}
			}
			
			if(sb.length() > 0){
				MSGUtil.sayError("部分作业单非【已到货】状态:"+ sb.substring(0,sb.length()-1));
				return;
			}
			
			if(shpm_list.length == 0){
				MSGUtil.sayError("未选择作业单，不能执行【取消签收】！");
				return ;
			}
			listMap.put("1", load_no);
			listMap.put("2", shpm_no_list);
			listMap.put("3",login_user);
			proName="SP_LOAD_CANCELRECEIPT(?,?,?,?)";
			break;
		case 1:
			if(view.loadTable.getSelection().length>0){
				load_no=view.loadTable.getSelectedRecord().getAttribute("LOAD_NO");
				shpm_no_list.put("1", " ");
			}else{
				MSGUtil.sayError("未选择调度单，不能执行【取消签收】！");
				return ;
			}
			listMap.put("1", load_no);
			listMap.put("2", shpm_no_list);
			listMap.put("3",login_user);
			proName="SP_LOAD_CANCELRECEIPT(?,?,?,?)";
			break;
		case 2:
			ListGridRecord[] records2=view.unloadTable.getSelection();
			for(Record rec:records2){
				if(StaticRef.NO_LOAD.equals(rec.getAttribute("LOAD_STAT"))){
					MSGUtil.sayError("未装车，不能执行【取消签收】");
					return;
				}
			}
			ListGridRecord[] records=view.unloadList.getSelection();
			if(records.length>0){
				load_no=view.unloadTable.getSelectedRecord().getAttribute("LOAD_NO");
				unload_id=view.unloadList.getSelectedRecord().getAttribute("UNLOAD_ID");
			}else{
				MSGUtil.sayError("未选择卸货地，不能执行【取消签收】");
				return;
			}
			listMap.put("1", load_no);
			listMap.put("2", unload_id);
			listMap.put("3", login_user);
			proName="SP_LOAD_CANCELRECEIPT_BYUNLOAD(?,?,?,?)";
			break;
		default :
			break;
		}
		String json = Util.mapToJson(listMap);
		Util.async.execProcedure(json, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
					MSGUtil.showOperSuccess();
//					String[] load_status = result.substring(2).split(",");
					if(view.shpmTable != null){
						ListGridRecord[] shpm_list=null;
						switch (Integer.parseInt(tab_flag.substring(0,1))){
						case 0:
							shpm_list = view.shpmTable.getSelection();
							for(int i=0;i<shpm_list.length;i++){
								
								//wangjun 更新【调度单状态】
								if(StaticRef.TRANS_PART_UNLOAD_NAME.equals(result.substring(5, 9))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_PART_UNLOAD_NAME);
									
								}if(StaticRef.TRANS_DEPART_NAME.equals(result.substring(5, 9))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_DEPART_NAME);
								}	
								
								shpm_list[i].setAttribute("STATUS", StaticRef.SHPM_LOAD);
								shpm_list[i].setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
								shpm_list[i].setAttribute("UNLOAD_TIME", "");
								shpm_list[i].setAttribute("ABNOMAL_STAT", "5FB42E7D159346C395A2A34E0FE698C1");
								shpm_list[i].setAttribute("ABNOMAL_NOTES", "");
							    shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS", " ");
								shpm_list[i].setAttribute("LOSDAM_FLAG", "N");
								shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS", "");
								shpm_list[i].setAttribute("TRACK_NOTES", "");
//								view.shpmTable.updateData(shpm_list[i]);
							}
							view.shpmTable.redraw();
							view.initButton(true, false, true, false, true);
							break;
						case 1:
							shpm_list=view.loadTable.getSelection();
							for(int i=0;i<shpm_list.length;i++){
								
								if(StaticRef.TRANS_PART_UNLOAD_NAME.equals(result.substring(5, 9))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_PART_UNLOAD_NAME);
									
								}if(StaticRef.TRANS_DEPART_NAME.equals(result.substring(5, 9))){ 
									shpm_list[i].setAttribute("LOAD_STATUS_NAME", StaticRef.TRANS_DEPART_NAME);
								}	
								
								shpm_list[i].setAttribute("STATUS", StaticRef.TRANS_DEPART);
								shpm_list[i].setAttribute("STATUS_NAME", StaticRef.TRANS_DEPART_NAME);
								shpm_list[i].setAttribute("UNLOAD_TIME", "");
								shpm_list[i].setAttribute("ABNOMAL_STAT", "5FB42E7D159346C395A2A34E0FE698C1");
								shpm_list[i].setAttribute("ABNOMAL_NOTES", "");
							    shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS", " ");
								shpm_list[i].setAttribute("LOSDAM_FLAG", "N");
								shpm_list[i].setAttribute("UNLOAD_DELAY_DAYS", "");
								shpm_list[i].setAttribute("TRACK_NOTES", "");
							}
							view.loadTable.redraw();
							view.initButton(true, false, false, false, false);
							break;
						case 2:
							Record rec = view.unloadList.getSelectedRecord();
							rec.setAttribute("STATUS", StaticRef.SHPM_LOAD);
							rec.setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
							view.initButton(true, false, false, false, false);
							view.unloadList.redraw();
							break;
						default :
							 break;
						}
					}
					view.panel.clearValues();
				}
				else {
					MSGUtil.sayError(result.substring(2));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}
	
	@Override
	public void onClick(ClickEvent event) {
		doConfirm();
	}

}
