package com.rd.client.action.settlement;

import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CancelTariffAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler {

	private DynamicForm form;
	private ListGrid table;
	private SGForm view;
	public CancelTariffAction(ListGrid p_table, DynamicForm p_form) {
		form = p_form;
		table = p_table;
	}
	
	public CancelTariffAction(ListGrid p_table, DynamicForm p_form, SGForm view) {
		form = p_form;
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		form.clearValues();
		ListGridRecord record = table.getSelectedRecord();
		if(record != null){
			form.editRecord(record);
		}
		if(view != null){
			view.initCancelBtn();
		}
	}

	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		
		form.clearValues();
		ListGridRecord record = table.getSelectedRecord();
		if(record != null){
			form.editRecord(record);
		}
		if(view != null){
			view.initCancelBtn();
		}
	}	
}