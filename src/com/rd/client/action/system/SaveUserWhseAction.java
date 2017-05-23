package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 系统管理-用户管理--仓库页签-保存按钮
 * @author fanglm
 *
 */
public class SaveUserWhseAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	private ListGrid list;
	private ListGrid fTable;
	private HashMap<String, Boolean> map;
	private String user_id;

	public SaveUserWhseAction(ListGrid list,ListGrid fTable){
		this.list = list;
		this.fTable = fTable;
	}
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		click();
	}
	
	@SuppressWarnings("unchecked")
	private void click() {
		
		if(fTable.getSelectedRecord() == null){
			MSGUtil.sayError("保存失败，请选择用户");
			return;
		}
		
		int[] edit_rows = list.getAllEditRows();   //获取所有修改过的记录行
		user_id = fTable.getSelectedRecord().getAttribute("USER_ID");
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql;
		int count=0;
		ListGridRecord[] rec = list.getRecords();
		for(int i=0;i<rec.length;i++){
			if("true".equals(ObjUtil.ifObjNull(list.getEditValue(i, "USE_FLAG"),rec[i].getAttribute("USE_FLAG")).toString())
					&&"true".equals(ObjUtil.ifObjNull(list.getEditValue(i, "DEFAULT_FLAG"),ObjUtil.ifObjNull(rec[i].getAttribute("DEFAULT_FLAG"),"")).toString())){
				count = count + 1;
			}
		}
		
		if(count > 1){
			MSGUtil.sayWarning("只能勾选一个默认仓库！");
			return;
		}
		
		for(int i=0;i<edit_rows.length;i++){
			Record curRecord = list.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
			map = (HashMap<String, Boolean>)list.getEditValues(edit_rows[i]);
			sql = new StringBuffer();
			if(ObjUtil.isNotNull(map.get("USE_FLAG")) && map.get("USE_FLAG")){
				sql.append("insert into sys_user_whse(id,user_id,whse_id,default_flag) values(");
				sql.append("(select sys_guid() from dual),'");
				sql.append(user_id);
				sql.append("','");
				sql.append(curRecord.getAttribute("WHSE_ID"));
				sql.append("','");
				sql.append(Util.getFlag(map.get("DEFAULT_FLAG")));
				sql.append("')");
				
				sqlList.add(sql.toString());
				count++;
			}else if(ObjUtil.isNotNull(map.get("DEFAULT_FLAG")) && "true".equals(map.get("DEFAULT_FLAG").toString())){
				sql.append("update sys_user_whse set default_flag='");
				sql.append(Util.getFlag(map.get("DEFAULT_FLAG")));
				sql.append("' where user_id='");
				sql.append(user_id);
				sql.append("' and whse_id='");
				sql.append(curRecord.getAttribute("WHSE_ID"));
				sql.append("'");
				sqlList.add(sql.toString());
				}
			else{
				sql.append("delete from sys_user_whse where user_id='");
				sql.append(user_id);
				sql.append("' and whse_id='");
				sql.append(curRecord.getAttribute("WHSE_ID"));
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
