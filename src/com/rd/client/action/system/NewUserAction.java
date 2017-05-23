package com.rd.client.action.system;

import java.util.Map;

import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 用户管理--新增按钮
 * @author fanglm
 *
 */
public class NewUserAction extends NewMultiFormAction {
	
	private ValuesManager vm;
	
	public NewUserAction(ValuesManager pForm, Map<String, String> map,SGForm form) {
		super(pForm, map,form);
		this.vm = pForm;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		//将USER_ID置为可编辑状态
		vm.getItem("USER_ID").setDisabled(false);
		super.onClick(event);
		vm.setValue("USER_ID","");
	}
}
