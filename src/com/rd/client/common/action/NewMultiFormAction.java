package com.rd.client.common.action;

import java.util.Map;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * FORM
 * @author yuanlei
 *
 */
public class NewMultiFormAction implements ClickHandler {

	private ValuesManager form;
	private Map<String, String> initMap;
	private SGForm view;
	public NewMultiFormAction(ValuesManager p_form, Map<String, String> map) {
		form = p_form;
		initMap = map;
	}
	public NewMultiFormAction(ValuesManager p_form, Map<String, String> map,SGForm view) {
		form = p_form;
		initMap = map;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		if(view != null) {
			view.isModify = true;
			view.initAddBtn();
			if(view.isMax) {
				view.expend();
			}
		}
		form.editNewRecord();
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					form.setValue("CUSTOMER_ID", LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					form.setValue("CUSTOMER_NAME", LoginCache.getDefCustomer().get("CUSTOMER_NAME"));
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					form.setValue(key, ObjUtil.ifNull(value, initMap.get(i18n_key)));
				}
				
				
				
				
			}
			
			
		}
		
		form.setValue("STATUS", "6D926C9A2826437AAEAE0E14119D9DD9");
		
		form.setValue("OP_FLAG", StaticRef.INS_FLAG);
	}
}
