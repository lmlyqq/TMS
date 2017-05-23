package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 系统管理--用户管理--经销商--删除按钮
 * @author fanglm
 *
 */
public class DeleteUserAddrAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	

	private ListGrid table;
	private ListGrid fTable;
	
	public DeleteUserAddrAction(ListGrid table,ListGrid fTable){
		this.table = table;
		this.fTable = fTable;
	}
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		click();
	}
	
	private void click() {
		if(fTable.getSelectedRecord() == null)
			return;
		ListGridRecord[] records = table.getSelection();
		
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}else{
			MSGUtil.sayError("请先勾选待删除记录！");
			return;
		}
		
		
	}
	
	private void doDelete(){
		StringBuffer sql;
		ArrayList<String> sqlList = new ArrayList<String>();
		ListGridRecord[] recordList = table.getSelection();
//		list = table.getDataAsRecordList();
		for(int i=0;i<recordList.length;i++){
			if(ObjUtil.isNotNull(recordList[i].getAttribute("ID"))){
				sql = new StringBuffer();
				sql.append("delete from sys_user_addr where id='");
				sql.append(recordList[i].getAttributeAsString("ID"));
				sql.append("'");
				sqlList.add(sql.toString());
			}
//			list.remove((Record)recordList[i]);
		}
		
		//需要删除数据库数据
		if(sqlList.size() > 0){
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						RefreshRecord();
					}
					else{
						MSGUtil.showOperError();
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}else{ //只删除缓存数据
			RefreshRecord();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void RefreshRecord() {
		ListGridRecord[] records = table.getSelection();
		ArrayList<ListGridRecord> list = new ArrayList(Arrays.asList(records));
		ListGridRecord[]  rec = table.getRecords();
		for(int i=0;i<rec.length;i++){
			int pos = table.getRecordIndex(rec[i]);
			list.remove(pos);
		}
		
		table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
		table.redraw();
	}


}
