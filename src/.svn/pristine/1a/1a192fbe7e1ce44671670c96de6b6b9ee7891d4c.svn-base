package com.rd.client.action.settlement;

import java.util.Map;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.view.settlement.RateManagerView;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;

/**
 * FORM
 * @author yuanlei
 *
 */
public class NewTariffAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private DynamicForm form;
	private Map<String, String> initMap;
	private RateManagerView view;

	public NewTariffAction(DynamicForm p_form, Map<String, String> map,RateManagerView view) {
		form = p_form;
		initMap = map;
		this.view = view;
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
			if(view.feeTable!=null) {
				view.feeTable.setData(new RecordList());
				view.vm.clearValues();
			}
			if(view.groupTable!=null) {
				view.groupTable.setData(new RecordList());
			}
		}
	}
}
