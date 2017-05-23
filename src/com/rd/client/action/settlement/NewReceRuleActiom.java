package com.rd.client.action.settlement;

import java.util.Map;

import com.rd.client.common.action.NewFormAction;
import com.rd.client.view.settlement.ReceRuleView;
import com.smartgwt.client.widgets.form.ValuesManager;

public class NewReceRuleActiom extends NewFormAction{
	
	private ReceRuleView view;
	private boolean isTff = true;

	public NewReceRuleActiom(ValuesManager pForm, Map<String, String> map,ReceRuleView view) {
		super(pForm, map);
		this.view = view;
	}

	public void click() {
		if(isTff){
			view.initAddRuleBtn();
		}
		if(view.isMax) {
			view.expend();
		}
		super.click();
	}
	
}
