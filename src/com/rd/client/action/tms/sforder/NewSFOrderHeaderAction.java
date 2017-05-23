package com.rd.client.action.tms.sforder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.SFOrderView;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 运输管理--原始订单--新增按钮
 * @author fanglm
 *
 */
public class NewSFOrderHeaderAction extends NewMultiFormAction {

	private ValuesManager vm;
	private SFOrderView view;
	
	public NewSFOrderHeaderAction(ValuesManager pForm, Map<String, String> map,SFOrderView view) {
		super(pForm, map,view);
		this.vm = pForm;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		view.enableOrDisables(view.add_detail_map, true);
		view.enableOrDisables(view.del_detail_map, false);
		if(StaticRef.INS_FLAG.equals(vm.getValueAsString("OP_FLAG"))){
			MSGUtil.sayError("当前订单没有保存，不能执行新增操作。");
			return;
		}
		
		Util.db_async.getIdSeq("SFORDER", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				//生成订单编号
				vm.setValue("ODR_NO", result);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
		super.onClick(event);
		
		HashMap<String, String> map = LoginCache.getDefCustomer();
		if(map != null){
			vm.getItem("CUSTOMER_NAME").setSelectOnFocus(true);
			vm.setValue("CUSTOMER_ID", map.get("CUSTOMER_ID"));
			vm.setValue("CUSTOMER_NAME", map.get("CUSTOMER_NAME"));
		}else{
			vm.getItem("CUSTOMER_NAME").setSelectOnFocus(true);
			vm.setValue("CUSTOMER_ID", "");
			vm.setValue("CUSTOMER_NAME", "");
		}
		
		
		vm.getItem("CUSTOMER_NAME").setDisabled(false);
		
		
		vm.setValue("PLAN_STAT", StaticRef.PLAN_STATUS_NO);
		vm.setValue("PLAN_STAT_NAME", "未调度");
		vm.setValue("LOAD_STAT", StaticRef.LOAD_STATUS_NO);
		vm.setValue("LOAD_STAT_NAME", "未发货");
		vm.setValue("UNLOAD_STAT", StaticRef.UNLOAD_STATUS_NO);
		vm.setValue("UNLOAD_STAT_NAME", "未收货");
		vm.setValue("DISCOUNT", "1");
		
		vm.setValue("STATUS", StaticRef.SFODR_STATUS_CREATE);
		vm.setValue("STATUS_NAME", StaticRef.SFODR_STATUS_CREATE_NAME);
		vm.setValue("GEN_ORDER_TYP", "Manual");//订单来源
		vm.setValue("EXEC_STAT", StaticRef.NORMAL);
		vm.setValue("EXEC_STAT_NAME", StaticRef.NORMAL_NAME);
		vm.setValue("ALLOW_SPLIT_FLAG", "true");
		//当前服务器系统时间
		Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				vm.setValue("ODR_TIME", result);
				vm.setValue("FROM_LOAD_TIME", result.substring(0, 10) + " 00:00");
				vm.setValue("PRE_LOAD_TIME", result.substring(0, 10) + " 00:00");
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
		
//		view.groupTable.invalidateCache();
		view.groupTable.setData(new RecordList());
		
		vm.getItem("LOAD_AREA_ID2").setDefaultValue("");
		vm.getItem("LOAD_AREA_ID2").setValueMap("");
		
		vm.getItem("LOAD_AREA_ID3").setDefaultValue("");
		vm.getItem("LOAD_AREA_ID3").setValueMap("");
		
		vm.getItem("UNLOAD_AREA_ID2").setDefaultValue("");
		vm.getItem("UNLOAD_AREA_ID2").setValueMap("");
		
		vm.getItem("UNLOAD_AREA_ID3").setDefaultValue("");
		vm.getItem("UNLOAD_AREA_ID3").setValueMap("");
		
		IButton newButton = null;
		if(event.getSource() instanceof IButton){
			newButton = (IButton)event.getSource();
			JSOHelper.setAttribute(newButton.getJsObj(), "newButtonClickFlag", true);
		}
		
		Collection<IButton> newDetialButton = view.add_detail_map.values();
		for (IButton iButton : newDetialButton) {
			ClickEvent.fire(iButton, iButton.getConfig());
			break;
		}
	}
	
}
