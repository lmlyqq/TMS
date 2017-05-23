package com.rd.client.action.tms.changerecord;


import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CancelChangeAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	private ValuesManager form;
	private SGTable table;
	private SGForm view;
	private int initBtn =0;
	
	public CancelChangeAction(ValuesManager form,SGTable table,SGForm view){
		this.table = table;
		this.form = form;
		this.view = view;
	}
	
	public CancelChangeAction(ValuesManager form,SGTable table,SGForm view,int initBtn){
		this.table = table;
		this.form = form;
		this.view = view;
		this.initBtn = initBtn;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		form.clearValues();
		ListGridRecord record = table.getSelectedRecord();
		if(record != null){
			form.editRecord(record);
		}
		if(view != null){
			if(initBtn ==0){
				if(table.getRecords().length > 0){
					view.initSaveBtn();
				}else{
					view.initCancelBtn();
				}
			}else{
				view.initBtn(initBtn);
			}
		}
	}

	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		
		form.clearValues();
		form.getItem("ABNORMAL_REASON").setDisabled(true);
		ListGridRecord record = table.getSelectedRecord();
		if(record != null){
			form.editRecord(record);
		}
		if(view != null){
			if(initBtn ==0){
				if(table.getRecords().length > 0){
					view.initSaveBtn();
				}else{
					view.initCancelBtn();
				}
			}else{
				view.initBtn(initBtn);
			}
		}
	}

}