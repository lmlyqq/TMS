package com.rd.client.action.base.org;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 组织机构--同步权限功能
 * @author fanglm
 *
 */
public class SyncPrivilegeAction implements ClickHandler{
	
	private TreeTable table;
	/**
	 * 构造
	 */
	public SyncPrivilegeAction(TreeTable in_table) {
		this.table = in_table;
	}
	
	@Override
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		SC.confirm("确定执行同步组织？", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
            		click();
                }
            }
        });
	}
	
	private void click() {

		ListGridRecord record = table.getSelectedRecord();               //执行机构列表选中的记录
		if(record == null) {
			MSGUtil.sayWarning("请选择组织机构!");
			return;
		}
		HashMap<String, Object> listMap = new HashMap<String, Object>();
		listMap.put("1", record.getAttribute("ID"));
		listMap.put("2", LoginCache.getLoginUser().getUSER_ID());
		
		String json = Util.mapToJson(listMap);
		
		Util.async.execProcedure(json, "SF_ORG_SYNC(?,?,?)", new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
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
