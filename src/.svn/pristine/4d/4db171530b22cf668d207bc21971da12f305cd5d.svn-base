package com.rd.client.action.base.time;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasTimeMangeView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
/**
 * 时间管理-->过滤条件按钮
 * @author wangjun
 *
 */
public class NewConditionAction implements ClickHandler {
	
	private BasTimeMangeView view;
	private SGTable table;
	private int row;
	
	public NewConditionAction(SGTable table, BasTimeMangeView view) {
		this.view = view;
		this.table = table;
	}
	

	public void onClick(ClickEvent event) {
		table.OP_FLAG = "A";
		
		if(view.formulaclickrecord == null)
			return;
		
		
		if(ObjUtil.isNotNull(table)){
			
			if(ObjUtil.isNotNull(table.getSelection())){
				table.startEditingNew();
				row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				view.itemRow = row;
			}
			table.setEditValue(view.itemRow, "FORMULA_ID", view.formulaclickrecord.getAttribute("ID"));
			table.setEditValue(view.itemRow, "OP_METHOD", "AND");
//			view.initAddBtn();
		}
	}

}
