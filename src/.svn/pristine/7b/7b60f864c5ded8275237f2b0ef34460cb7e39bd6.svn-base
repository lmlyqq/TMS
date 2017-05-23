package com.rd.client.common.action;

import java.util.Map;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * FORM
 * @author yuanlei
 *
 */
public class NewFormAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private DynamicForm form;
	private Map<String, String> initMap;
	private SGForm view;
	private ValuesManager vm;
	public NewFormAction(DynamicForm p_form, Map<String, String> map) {
		form = p_form;
		initMap = map;
	}
	
	public NewFormAction(DynamicForm p_form, Map<String, String> map,SGForm view) {
		form = p_form;
		initMap = map;
		this.view = view;
	}
	
	public NewFormAction(ValuesManager vm,Map<String, String> map){
		this.vm = vm;
		this.initMap = map;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		click();
	}

	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		click();
	}
	
	public void click(){
		if(view != null){
			view.initAddBtn();
			
			if(view.isMax) {
				view.expend();
			}
		}
		if(form != null){
			form.editNewRecord();
			if(initMap != null) {
				Object[] iter = initMap.keySet().toArray();
				String key = "",i18n_key = "";
				String value = "";
				for(int i = 0; i < iter.length; i++) {
					key = (String)iter[i];
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					form.setValue(key, ObjUtil.ifNull(value,initMap.get(i18n_key)));
				}
			}
			form.setValue("OP_FLAG", StaticRef.INS_FLAG);
		}else{
			vm.editNewRecord();
			if(initMap != null){
				if(initMap != null) {
					Object[] iter = initMap.keySet().toArray();
					String key = "",i18n_key = "";
					String value = "";
					for(int i = 0; i < iter.length; i++) {
						key = (String)iter[i];
						i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
						value = initMap.get(key);
						vm.setValue(key, ObjUtil.ifNull(value,initMap.get(i18n_key)));
					}
				}
				vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
			}
		}
	}
}
