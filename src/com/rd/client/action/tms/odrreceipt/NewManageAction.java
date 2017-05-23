package com.rd.client.action.tms.odrreceipt;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsOdrReceiptView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 运输管理-->托运单回单-->【货损货差】新增按钮
 * @author wangjun
 *
 */
public class NewManageAction implements ClickHandler{
    
	private TmsOdrReceiptView view;
	private SGTable table;
	private int row;
	
	public NewManageAction(SGTable table, TmsOdrReceiptView view) {
		this.view = view;
		this.table = table;
	}
	

	@Override
	public void onClick(ClickEvent event) {
		table.OP_FLAG = "A";
		
		if(view.clickrecord == null)
			return;
		
		
		if(ObjUtil.isNotNull(table)){
			
			if(ObjUtil.isNotNull(table.getSelection())){
				table.startEditingNew();
				row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				view.itemRow = row;
			}
			table.setEditValue(view.itemRow, "ODR_NO", view.clickrecord.getAttribute("ODR_NO"));
			//table.setEditValue(view.itemRow, "DUTYER", LoginCache.getLoginUser().getUSER_ID());
			table.setEditValue(view.itemRow, "CUSTOMER_ID", view.clickrecord.getAttribute("CUSTOMER_ID"));

			view.initAddBtn();
		}
	}

}
