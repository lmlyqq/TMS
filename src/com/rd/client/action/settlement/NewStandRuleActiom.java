package com.rd.client.action.settlement;

import java.util.Map;

import com.rd.client.common.action.NewFormAction;
import com.rd.client.view.settlement.StandRuleView;
import com.smartgwt.client.widgets.form.ValuesManager;

public class NewStandRuleActiom extends NewFormAction{
	
	private StandRuleView view;
	private boolean isTff = true;

	public NewStandRuleActiom(ValuesManager pForm, Map<String, String> map,StandRuleView view) {
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
