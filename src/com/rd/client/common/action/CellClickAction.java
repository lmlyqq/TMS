package com.rd.client.common.action;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;


/**
 * 列表与表单的关联关系
 * @author fanglm
 *
 */
public class CellClickAction implements RecordClickHandler {

	private DynamicForm form;  //表单
	
	@SuppressWarnings("unused")
	private String op_flag;
	
	public CellClickAction(DynamicForm form,String op_flag){
		this.form = form;
		this.op_flag = op_flag;
	}

	@Override
	public void onRecordClick(RecordClickEvent event) {
		form.editRecord(event.getRecord());
		op_flag="M";
	}


}
