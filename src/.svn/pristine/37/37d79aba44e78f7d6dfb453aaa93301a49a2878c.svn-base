package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;

public class SaveUserGroAction implements ClickHandler {

	private ListGrid list;
	private ListGrid table;
	private HashMap<String, Boolean> map;
	private String usergrp_id;

	public SaveUserGroAction(ListGrid list, ListGrid table) {
		this.list = list;
		this.table = table;
	}

	@SuppressWarnings("unchecked")
	public void onClick(ClickEvent event) {
		int[] edit_rows = list.getAllEditRows(); // 获取所有修改过的记录行
		usergrp_id = table.getSelectedRecord().getAttribute("ID");
		String login_user = LoginCache.getLoginUser().getUSER_ID();

		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql;

		for (int i = 0; i < edit_rows.length; i++) {
			Record curRecord = list.getEditedRecord(edit_rows[i]); // 获取所有修改的记录
			map = (HashMap<String, Boolean>) list.getEditValues(edit_rows[i]);
			sql = new StringBuffer();
			if (map.get("USE_FLAG")) {
				sql.append("insert into sys_usergrp_role(id,usergrp_id,role_id,addwho,addtime) values(");
				sql.append("(select sys_guid() from dual),'");
				sql.append(usergrp_id);
				sql.append("','");
				sql.append(curRecord.getAttribute("ID"));
				sql.append("','");
				sql.append(login_user);
				sql.append("',sysdate)");
				sqlList.add(sql.toString());
				
			} else {
				sql.append("delete from sys_usergrp_role where role_id='");
				sql.append(curRecord.getAttribute("ID"));
				sql.append("' and usergrp_id='");
				sql.append(usergrp_id);
				sql.append("'");
				sqlList.add(sql.toString());
			}
		}
		
		//调用通用方法批处理数据
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					Criteria criteria = list.getCriteria();
	        		list.invalidateCache();
					list.fetchData(criteria);
					list.discardAllEdits();
				}else{
					MSGUtil.showOperError();
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});

	}

}
