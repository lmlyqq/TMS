package com.rd.client.action.settlement;

import java.util.HashMap;

import com.rd.client.common.action.NewAction;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.ReceRuleView;
import com.smartgwt.client.widgets.events.ClickEvent;

public class NewReceDiscountAction extends NewAction{
	
	private ReceRuleView view;

	public NewReceDiscountAction(SGTable pTable,HashMap<String, String> initMap,ReceRuleView view) {
		super(pTable);
		this.view  = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(view.table.getSelectedRecord()==null){
			return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TFF_ID", view.table.getSelectedRecord().getAttributeAsString("TFF_ID"));
		map.put("RUL_ID", view.table.getSelectedRecord().getAttributeAsString("ID"));
		view.initAddDiscountBtn();
		
		super.setInitMap(map);
		super.onClick(event);
	}
	
}
