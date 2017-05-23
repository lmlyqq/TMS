package com.rd.client.action.tms.dispatch;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 调度配载--调度刷新
 * @author wangjun
 *
 */
public class QueryUnloadTableAction implements ClickHandler {

	private SGTable table;
	private DynamicForm pageForm;
	public QueryUnloadTableAction(SGTable p_table, DynamicForm p_pageForm) {
		this.table = p_table;
		pageForm = p_pageForm;
	}
	
	public void doRefresh(final boolean isSelected) {
		table.discardAllEdits();
		table.invalidateCache();
		Criteria crit = table.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		//crit.addCriteria("STATUS", StaticRef.SHPM_CONFIRM);
		crit.addCriteria("EMPTY_FLAG","Y");
		if(!ObjUtil.isNotNull(crit.getAttribute("EXEC_ORG_ID"))) {
			crit.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
			crit.addCriteria("C_ORG_FLAG","Y");
		}
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crit.getValues();
		table.fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
				}
			}
		});
		table.setSelectOnEdit(true);
		table.selectRecord(table.getRecord(0));
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doRefresh(false);
	}
}
