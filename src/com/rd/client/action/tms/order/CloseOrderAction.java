package com.rd.client.action.tms.order;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CloseOrderAction implements ClickHandler{
	private SGTable table;
	private int butNum;
	
	public CloseOrderAction(SGTable table){
		this.table = table;
	}
	
	public CloseOrderAction(SGTable table, int butNum){
		this.table = table;
		this.butNum = butNum;
	}
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord selectedRecord = table.getSelectedRecord();
		if(selectedRecord == null) {
			SC.warn("未选择任何托运单!");
			return;
		}
		String odrNo = selectedRecord.getAttribute("ODR_NO");
		if(!ObjUtil.isNotNull(odrNo)){
			SC.warn("托运单号为空!");
			return;
		}
		final String status = selectedRecord.getAttribute("STATUS");
		if(butNum == 0 && !StaticRef.SO_CONFIRM.equals(status)){
			SC.warn("不能关闭["+selectedRecord.getAttribute("STATUS_NAME")+"]状态的托运单!");
			return;
		}else if(butNum == 1 && !StaticRef.SO_CLOSED.equals(status)){
			SC.warn("不能取消关闭["+selectedRecord.getAttribute("STATUS_NAME")+"]状态的托运单!");
			return;
		}
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		String proName="SP_ORDER_CLOSE(?,?,?)";	//ODR_NO, RESULT
		
		ArrayList<String> paramList = new ArrayList<String>();
		paramList.add(odrNo);
		paramList.add(loginUser);
		Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					if(StaticRef.SO_CONFIRM.equals(status)){
						selectedRecord.setAttribute("STATUS", StaticRef.SO_CLOSED);
						selectedRecord.setAttribute("STATUS_NAME", StaticRef.SO_CLOSED_NAME);
					}else{
						selectedRecord.setAttribute("STATUS", StaticRef.SO_CONFIRM);
						selectedRecord.setAttribute("STATUS_NAME", StaticRef.SO_CONFIRM_NAME);
					}
					
					table.redraw();
				}else{
					MSGUtil.sayError(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
