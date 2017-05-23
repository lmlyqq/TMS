package com.rd.client.action.tms.dispatch;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;

public class QueryUnshpmTableAction implements ClickHandler {

	private SGTable table;
	private DynamicForm sumForm;
	private DynamicForm pageForm;
	private Criteria crit;
	public QueryUnshpmTableAction(SGTable p_table, DynamicForm p_sumForm, DynamicForm p_pageForm) {
		this.table = p_table;
		sumForm = p_sumForm;
		pageForm = p_pageForm;
	}
	
	@SuppressWarnings("unchecked")
	public void doRefresh(final boolean isSelected) {
		table.discardAllEdits();
		table.invalidateCache();
		crit = table.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria("STATUS", StaticRef.SHPM_CONFIRM);
		crit.addCriteria("EMPTY_FLAG","Y");
		//fanglm 2011-3-18 按区域排序
//		crit.addCriteria("ORDER_BY_AREA","Y");
		if(!ObjUtil.isNotNull(crit.getAttribute("EXEC_ORG_ID"))) {
			crit.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		}
		crit.addCriteria("C_ORG_FLAG","true");
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crit.getValues();
		table.fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
				}
				table.setSelectOnEdit(true);
				table.selectRecord(table.getRecord(0));
				
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crit.getValues();
				if(map.get("criteria") != null) {
					map.remove("criteria");
				}
				if(map.get("_constructor") != null) {
					map.remove("_constructor");
				}
				/*if(map.get("C_ORG_FLAG") != null) {
					Object obj = map.get("C_ORG_FLAG");
					Boolean c_org_flag = (Boolean)obj;
					map.put("C_ORG_FLAG",c_org_flag.toString());
				}*/
				Util.db_async.getSHMPNOSum(map, new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						sumForm.setValue("TOT_VOL", ObjUtil.ifObjNull(result.get("TOT_VOL"), "").toString());
						sumForm.setValue("TOT_GROSS_W", ObjUtil.ifObjNull(result.get("TOT_GROSS_W"), "").toString());
						sumForm.setValue("TOT_QNTY", ObjUtil.ifObjNull(result.get("TOT_QNTY"), "").toString());
						
						//if(isSelected) {
						//}
					}
				});
			}
		});
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doRefresh(false);
	}
}
