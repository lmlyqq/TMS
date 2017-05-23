package com.rd.client.action.base.ware;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasWareHouseView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
/**
 * 仓库设置--新增按钮
 * @author wangjun
 *
 */
public class NewWareAction implements ClickHandler {
	
	private BasWareHouseView view;
	private SGTable table;
	private int row;
	
	public NewWareAction(SGTable table,BasWareHouseView view) {
		this.table = table;
		this.view = view;
	}
	

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
			table.setEditValue(view.itemRow, "WHSE_ID", view.clickrecord.getAttribute("ID"));
			view.initAddBtn();
		}
	}

}
