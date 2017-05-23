package com.rd.client.action.tms.sforder;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.SFOrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--原始订单--右键--取消确认操作
 * @author fanglm
 *
 */
public class SFOrdUnConfirmAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private ListGrid table;
	private ValuesManager vm;
	public StringBuffer msg;
	public SFOrderView view;
	
	private ListGridRecord[] list;
	public SFOrdUnConfirmAction(ListGrid table,ValuesManager vm,SFOrderView view){
		this.table = table;
		this.vm = vm;
		this.view = view;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		doSome();
	}

	@Override
	public void onClick(ClickEvent event) {
		doSome();
	}
	
	protected void doSome(){
		list = table.getSelection();
		
		if (list.length == 0)
			return;
		
		StringBuffer sf = new StringBuffer();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		//存储过程名称
		String proName="SP_SFORDER_CANCELCONFIRM(?,?,?)"; //odr_no,user_id,output_result
		
		ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for(int i=0;i<list.length;i++){
			if(!StaticRef.SFODR_STATUS_CONFIRM.equals(list[i].getAttribute("STATUS"))){
				sf.append(list[i].getAttribute("ODR_NO"));
				sf.append(",");
			}else{
				idList = new ArrayList<String>();
				idList.add(list[i].getAttribute("ODR_NO"));
				idList.add(loginUser);
				procesList.put(list[i].getAttribute("ODR_NO"), idList);
			}
		}
		if(sf.toString().length() > 0){
			MSGUtil.sayError("订单 "+sf.substring(0,sf.length()-1) +",不需要执行取消确认操作!");
			return;
		}
		
		view.confirmBtnEnable(false, false);
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				String errorId = "";
				if(result.split("@").length > 1){
					errorId = result.split("@")[1];
				}
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					view.confirmBtnEnable(false, false);
					for(int i=0;i<list.length;i++){
						if(errorId.indexOf(list[i].getAttributeAsString("ODR_NO")) >= 0){ //wangjun 2010-07-19
							list[i].setAttribute("STATUS", StaticRef.SFODR_STATUS_CREATE);
							list[i].setAttribute("STATUS_NAME", StaticRef.SFODR_STATUS_CREATE_NAME);
							table.updateData(list[i]);
						}
					}
					//FANGLM 2010-12-23 form刷新
					vm.setValue("STATUS", StaticRef.SFODR_STATUS_CREATE);
					table.redraw();
					view.confirmBtnEnable(true, false);
					view.enableOrDisables(view.add_detail_map, true);
					view.enableOrDisables(view.del_map, true);
					view.enableOrDisables(view.save_map, true);
				}else{
					MSGUtil.sayWarning(result.split("@")[0]);
					view.confirmBtnEnable(false, true);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
