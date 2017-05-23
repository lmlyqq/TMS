package com.rd.client.action.tms.order;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
//import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--右键--确认操作
 * @author fanglm
 *
 */
public class OrdConfirmAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private ListGrid table;
	
//	private ValuesManager vm;
	
	public StringBuffer msg;
	
	public OrderView view;
	
	private ListGridRecord[] list;
	public OrdConfirmAction(ListGrid table,ValuesManager vm,OrderView view){
		this.table = table;
//		this.vm = vm;
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
	
	private void doSome() {
		list = table.getSelection();
		
		if (list.length == 0)
			return;
		if(view.isMax){
			//RecordDoubleClickEvent.fire(view.table, view.table.getConfig());
			if(view.isMax) {
				view.isMax=false;
			}
			if(view.groupTable != null){
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				view.groupTable.invalidateCache();
				criteria.addCriteria("ODR_NO",table.getSelectedRecord().getAttributeAsString("ODR_NO"));
				view.groupTable.fetchData(criteria, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						doSome();
					}
				});
			}
			return;
		}
		if(view.groupTable == null || view.groupTable.getRecords().length < 1) {
			SC.warn("审核失败，托运单无明细");
			return;
		}
		StringBuffer sf = new StringBuffer();
		StringBuffer no_area = new StringBuffer();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		//存储过程名称
		String proName="SP_ORDER_CONFIRM(?,?,?)"; //odr_no,user_id,output_result
		
		ArrayList<String> idList;
		HashMap<String,ArrayList<String>> procesList = new HashMap<String,ArrayList<String>>();
		for(int i=0;i<list.length;i++){
			if(!StaticRef.ORD_STATUS_CREATE.equals(list[i].getAttribute("STATUS"))){
				sf.append(list[i].getAttribute("ODR_NO"));
				sf.append(",");
			}
			else if(!ObjUtil.isNotNull(list[i].getAttribute("UNLOAD_AREA_NAME2"))){
				no_area.append(list[i].getAttribute("ODR_NO"));
				no_area.append(",");
			}
			else{
				idList = new ArrayList<String>();
				idList.add(list[i].getAttribute("ODR_NO"));
				idList.add(loginUser);
				procesList.put(list[i].getAttribute("ODR_NO"), idList);
			}
		}
		if(sf.toString().length() > 0){
			SC.warn("订单 "+sf.substring(0,sf.length()-1) +",非【" + StaticRef.SO_CREATE_NAME + "】状态, 不能审核！");
			return;
		}
		
		if(no_area.toString().length() > 0){
			SC.warn("订单 "+no_area.substring(0,no_area.length()-1) +",请填写收货区域，再执行确认！");
			return;
		}
		
		Util.async.execPro(procesList, proName, new AsyncCallback<String>() {			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					
					MSGUtil.showOperSuccess();	
					
					for(int i=0;i<list.length;i++){
						
						list[i].setAttribute("STATUS", StaticRef.SO_CONFIRM);
						list[i].setAttribute("STATUS_NAME", StaticRef.SO_CONFIRM_NAME);
					}
					view.confirmBtnEnable(false,true);
					table.redraw();
					
					/*RecordList rList = table.getRecordList();
					RecordList newList = new  RecordList();
					int row = 0; //fanglm 2011-3-3 删除记录后定位至上一条记录
					for(int i = 0; i < rList.getLength(); i++) {
						if(successId.indexOf(rList.get(i).getAttributeAsString("ODR_NO")) < 0)
			            	newList.add(rList.get(i));
			            else if(i > 0 )
			            	row = i - 1 ;
					}
					table.setData(newList);
					table.selectRecord(row);
					*/
				}else{
					String msg = result.split("@")[0];
					MSGUtil.sayWarning(msg);
					String[] msgList = msg.split(",");
					for(int i=0;i<msgList.length;i++){
						
						if(msgList[i].substring(0, 2).equals(StaticRef.WARNING_CODE)) {
							list[i].setAttribute("STATUS", StaticRef.SO_CONFIRM);
							list[i].setAttribute("STATUS_NAME", StaticRef.SO_CONFIRM_NAME);
							if(i == 0) {
								view.confirmBtnEnable(false,true);
							}
						}
					}
					table.redraw();
				}
				view.enableOrDisables(view.add_detail_map, false);
				view.enableOrDisables(view.del_map, false);
				view.enableOrDisables(view.save_map, false);
				view.vm.setValue("STATUS", StaticRef.SO_CONFIRM);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});

	}

	

}
