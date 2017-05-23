package com.rd.client.action.base.org;

import java.util.Map;

import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.view.base.BasOrgView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;

public class NewOrgAction implements ClickHandler {

	private ValuesManager form;
	private Map<String, String> initMap;
	private BasOrgView view;
	public NewOrgAction(ValuesManager p_form, Map<String, String> map, BasOrgView p_view) {
		form = p_form;
		initMap = map;
		view = p_view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String level = initMap.get("ORG_LEVEL");
		if(level == null) {
			SC.warn("请先查询数据后再添加数据!");
			return;
		}
		new NewMultiFormAction(form, initMap).onClick(event);
		form.setValue("ENABLE_FLAG", "Y");

		int int_lv = Integer.parseInt(level);
		form.setValue("ORG_LEVEL", Integer.toString(int_lv + 1));
		
		view.initAddBtn();
	}
}
