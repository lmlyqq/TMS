package com.rd.client.action.tms.rule;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.rule.DispatchRuleView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
/**
 * 时间管理-->过滤条件按钮
 * @author wangjun
 *
 */
public class NewPriorAction implements ClickHandler {
	
	private DispatchRuleView view;
	private SGTable table;
	private int row;
	
	public NewPriorAction(SGTable table, DispatchRuleView view) {
		this.view = view;
		this.table = table;
	}
	

	public void onClick(ClickEvent event) {
		table.OP_FLAG = "A";
		
		if(view.typeclickrecord == null)
			return;
		
		
		if(ObjUtil.isNotNull(table)){
			
			if(ObjUtil.isNotNull(table.getRecords())){
				table.startEditingNew();
				row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				view.itemRow = row;
			}
			table.setEditValue(view.itemRow, "RUL_ID", view.typeclickrecord.getAttribute("ID"));
		}
	}

}
