package com.rd.client.action.system;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * 系统管理--用户管理--经销商--保存按钮
 * @author fanglm
 *
 */
public class SaveUserAddrAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	private ListGrid table;
	private ListGrid fTable;
	private String user_id;
	
	public SaveUserAddrAction(ListGrid table,ListGrid fTable){
		this.fTable = fTable;
		this.table = table;
	}
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		click();
	}
	
	private void click() {
		if(fTable.getSelectedRecord() == null){
			MSGUtil.sayError("保存失败，请选择用户");
			return;
		}
		user_id = fTable.getSelectedRecord().getAttribute("USER_ID");
		StringBuffer sql;
		ArrayList<String> sqlList = new ArrayList<String>();
		RecordList recordList = table.getDataAsRecordList();
		for(int i=0;i<recordList.getLength();i++){
			if(!ObjUtil.isNotNull(recordList.get(i).getAttribute("ID"))){
				sql = new StringBuffer();
				sql.append("insert into sys_user_addr(id,user_id,addr_id) values(");
				sql.append("(select sys_guid() from dual),'");
				sql.append(user_id);
				sql.append("','");
				sql.append(recordList.get(i).getAttributeAsString("ADDR_ID"));
				sql.append("')");
				sqlList.add(sql.toString());
			}
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					criteria.addCriteria("USER_ID", user_id);
					table.fetchData(criteria);
				}
				else{
					MSGUtil.showOperError();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}


}
