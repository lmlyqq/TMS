package com.rd.client.action.tms.order;

import com.rd.client.common.util.StaticRef;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 托运单管理--取消按钮
 * @author fanglm
 *
 */
public class CancelOrderHeaderAction implements ClickHandler{
	
	private OrderView view;
	
	public CancelOrderHeaderAction(OrderView view) {
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		view.enableOrDisables(view.add_map, true);
		view.enableOrDisables(view.del_map, false);
		view.enableOrDisables(view.save_map, false);
		view.enableOrDisables(view.del_detail_map, false);
		view.enableOrDisables(view.add_detail_map, true);
		view.enableOrDisables(view.sav_detail_map, false);
		view.vm.clearValues();
//		view.vm.setValue("UNLOAD_NAME", "");
//		view.vm.setValue("UNLOAD_ID", "");
		ListGridRecord record = view.table.getSelectedRecord();
		if(record != null){
			view.vm.editRecord(record);
			view.enableOrDisables(view.del_map, true);
		}
		view.groupTable.discardAllEdits();
		view.vm.getItem("CUSTOMER_NAME").setDisabled(true);
		view.vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
		
		if(view.table.getRecords().length > 0){
			int row = view.table.getRecordIndex(view.table.getSelectedRecord());
			view.table.setData(view.table.getRecords());
			view.table.invalidateCache();
			view.table.selectRecord(row);
		}
//		view.isMax = !view.isMax;
	}	
}
