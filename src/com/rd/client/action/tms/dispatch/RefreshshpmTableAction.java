package com.rd.client.action.tms.dispatch;


import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;

/**
 * 作业单管理--拆分取消拆分时刷新数据
 * @author fanglm 2011-4-16 00:40
 *
 */
public class RefreshshpmTableAction implements ClickHandler {

	private SGTable table;
//	private DynamicForm pageForm;
	private TmsShipmentView view;
	public RefreshshpmTableAction(SGTable p_table ,TmsShipmentView view) {
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
