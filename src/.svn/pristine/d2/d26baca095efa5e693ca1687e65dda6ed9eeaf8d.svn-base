package com.rd.client.action.tms.dispatch;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsSelfControl;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class SelfConRefreshTable implements ClickHandler{

	private SGTable table;
//	private DynamicForm pageForm;
	private TmsSelfControl view;
	public SelfConRefreshTable(SGTable p_table ,TmsSelfControl view) {
		this.table = p_table;
//		pageForm = p_pageForm;
		this.view = view;
	}
	
	public void doRefresh(final boolean isSelected) {
		table.discardAllEdits();
		table.invalidateCache();
		Criteria crit = table.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria(view.searchForm.getValuesAsCriteria());
		table.fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
//				if(pageForm != null) {
//					pageForm.getField("CUR_PAGE").setValue("1");
//					LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
//				}
				table.setSelectOnEdit(true);
				table.selectRecord(table.getRecord(0));
			}
		});
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doRefresh(false);
	}
}
