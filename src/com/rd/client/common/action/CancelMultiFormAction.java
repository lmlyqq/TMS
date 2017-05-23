package com.rd.client.common.action;


import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 多Form布局取消按钮公共方法
 * @author fanglm
 *
 */
public class CancelMultiFormAction implements ClickHandler {

	private ValuesManager form;
	private ListGrid table;
	private SGForm view;
//	private HashMap<String, IButton> ins_map;
//	private HashMap<String, IButton> sav_map; 
	
	
	public CancelMultiFormAction(ListGrid p_table, ValuesManager p_form) {
		form = p_form;
		table = p_table;
	}
	
	public CancelMultiFormAction(ListGrid p_table, ValuesManager p_form,SGForm view) {
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
			if(view != null)
				view.initSaveBtn();
		}else{
			if(view !=null)
				view.initCancelBtn();
		}
		if(view !=null)
			view.isModify = true;
//		view.isMax = !view.isMax;
	}	
}