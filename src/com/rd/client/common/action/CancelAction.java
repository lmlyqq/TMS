package com.rd.client.common.action;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class CancelAction implements ClickHandler {

	private SGTable table = null;
	private SGTable detail_table = null;
	private SGForm view;
	private int initBtn = 0;
	public CancelAction(SGTable p_table) {
		table = p_table;
	}
	
	public CancelAction(SGTable p_table,SGForm view) {
		table = p_table;
		this.view = view;
		
	}
	public CancelAction(SGTable p_table,SGForm view,int initBtn) {
		table = p_table;
		this.view = view;
		this.initBtn = initBtn;
		
	}
	
	public CancelAction(SGTable p_table,SGTable detail_table,SGForm view) {
		table = p_table;
		this.view = view;
		this.detail_table = detail_table;
		
	}

	@Override
	public void onClick(ClickEvent event) {
		table.discardAllEdits();
		
		if(detail_table != null){
			detail_table.discardAllEdits();
			detail_table.OP_FLAG = StaticRef.MOD_FLAG;
			if(table.getSelectedRecord() != null){
				int row = table.getRecordIndex(table.getSelectedRecord());
				table.setData(table.getRecords());
				table.invalidateCache();
				table.selectRecord(row);
			}
		}else{
//			table.invalidateCache();
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
