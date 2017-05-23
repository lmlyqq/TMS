package com.rd.client.action.system;

import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.system.ListConfigView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 系统管理->列表配置->保存
 * @author yuanlei
 *
 */
public class ListConfigNewAction implements ClickHandler {

	private SGTable table = null;
	private ListConfigView view;
	public ListConfigNewAction(SGTable p_table,ListConfigView p_view) {
		table = p_table;
		view = p_view;
	}

	@Override
	public void onClick(ClickEvent event) {
		try {
			table.setData(new ListGridRecord[0]);
			table.OP_FLAG = "A";
			if(view != null) {
				view.initAddBtn(false);
				if(view.isMax) {
					view.expend("60%");
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
