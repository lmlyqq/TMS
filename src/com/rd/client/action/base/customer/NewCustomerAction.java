package com.rd.client.action.base.customer;

import java.util.Map;

import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;

/**
 * 客户管理 -- 新增客户按钮
 * 供应商管理--新增按钮
 * @author fanglm
 *
 */
public class NewCustomerAction extends NewMultiFormAction{
	
	private TextItem customer_code; //客户代码控件、供应商代码控件
	private TextItem sku_name;
	
	public NewCustomerAction(ValuesManager pForm, Map<String, String> map,TextItem cust_code,TextItem sku_name,SGForm view) {
		super(pForm, map,view);
		this.customer_code = cust_code;
		this.sku_name = sku_name;
	}

	@Override
	public void onClick(ClickEvent event) {
		super.onClick(event);
		//设置代码控件可编辑
		customer_code.setDisabled(false);
		if(sku_name != null){
			sku_name.setDisabled(false);
		}
	}

}
