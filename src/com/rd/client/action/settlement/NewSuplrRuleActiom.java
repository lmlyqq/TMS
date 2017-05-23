package com.rd.client.action.settlement;

import java.util.Map;

import com.rd.client.common.action.NewFormAction;
import com.rd.client.view.settlement.SuplrRuleView;
import com.smartgwt.client.widgets.form.ValuesManager;

public class NewSuplrRuleActiom extends NewFormAction{
	
	private SuplrRuleView view;
	private boolean isTff = true;

	public NewSuplrRuleActiom(ValuesManager pForm, Map<String, String> map,SuplrRuleView view) {
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
