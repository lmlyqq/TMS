package com.rd.client.action.base.route;

import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * 路线二级窗口的取消按钮
 * @author yuanlei
 *
 */
public class CancelSubAction implements ClickHandler {

	private SGTable table;
	private SGTable detail_table;
	private TabSet tabSet;
	public CancelSubAction(SGTable p_table,	TabSet p_tabSet, SGTable p_detailTable) {
		table = p_table;
		tabSet = p_tabSet;
		detail_table = p_detailTable;
	}
	@Override
	public void onClick(ClickEvent event) {
		try {
			if(tabSet.getSelectedTabNumber() == 0) {
				table.removeSelectedData();
			}
			else if(tabSet.getSelectedTabNumber() == 1) {
				Criteria crit =  new Criteria();
				crit.addCriteria("OP_FLAG", "M");
				detail_table.fetchData();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
