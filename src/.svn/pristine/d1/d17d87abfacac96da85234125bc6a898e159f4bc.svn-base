package com.rd.client.action.base.customer;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 客户管理--组织机构--同步权限功能
 * @author fanglm
 *
 */
public class SyncPrivilegeAction implements ClickHandler{
	
	private ListGrid table;
	private ListGrid customTable;
	/**
	 * 构造
	 */
	public SyncPrivilegeAction(ListGrid in_table,ListGrid in_customTable) {
		this.table = in_table;
		customTable = in_customTable;
	}
	
	@Override
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		SC.confirm("所选机构下的用户都将获取该客户权限，确定执行？", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
            		click();
                }
            }
        });
	}
	
	private void click() {

		ListGridRecord[] records = table.getSelection();               //执行机构列表选中的记录
		ListGridRecord[] cust_records = customTable.getSelection();  //客户列表选中的记录
		if(cust_records == null || cust_records.length > 1) {
			MSGUtil.sayWarning("请勾选一个客户!");
			return;
		}
		if(records == null || records.length == 0) {
			MSGUtil.sayWarning("请勾选组织机构!");
			return;
		}
		if(records.length > 50) {
			MSGUtil.sayWarning("一次性最多勾选50条记录!");
			return;
		}
		HashMap<String, String> org_id_list = new HashMap<String, String>(); // 组织机构ID
		for(int i = 0; i < records.length; i++) {
			org_id_list.put(String.valueOf(i+1), records[i].getAttribute("ORG_ID"));
		}
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		listMap.put("1", cust_records[0].getAttribute("ID"));
		listMap.put("2", org_id_list);
		listMap.put("3", LoginCache.getLoginUser().getUSER_ID());
		
		String json = Util.mapToJson(listMap);
		
		Util.async.execProcedure(json, "SF_CUSTOMER_SYNC(?,?,?,?)", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
					MSGUtil.showOperSuccess();
				}
				else {
					MSGUtil.sayError(result.substring(2));
				}
			}
			
		});
	}
}
