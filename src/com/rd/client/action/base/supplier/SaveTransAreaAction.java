package com.rd.client.action.base.supplier;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
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
 * 基础资料-供应商管理-运输信息-运输区域-保存按钮
 * @author fanglm
 *
 */
public class SaveTransAreaAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	

	private ListGrid table;
	private ListGrid fTable;
	private String suplr_id;
	
	public SaveTransAreaAction(ListGrid fTable,ListGrid table){
		this.fTable = fTable;
		this.table = table;
	}
	@Override
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		click();
	}
	
	private void click() {
		if(fTable.getSelectedRecord() == null){
			MSGUtil.sayError("保存失败，请选择供应商！");
			return;
		}
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		suplr_id = fTable.getSelectedRecord().getAttributeAsString("ID");
		
		StringBuffer sql;
		ArrayList<String> sqlList = new ArrayList<String>();
		RecordList recordList = table.getDataAsRecordList();
		HashMap<String, String> keyMap = new HashMap<String, String>();
		StringBuffer msg = new StringBuffer();
//		sql.append("delete from bas_suplr_area where ")
		for(int i=0;i<recordList.getLength();i++){
			if(keyMap.get(recordList.get(i).getAttributeAsString("UNLOAD_AREA_ID")+ recordList.get(i).getAttributeAsString("TRANS_SRVC_ID")) == null){
				if(!ObjUtil.isNotNull(recordList.get(i).getAttribute("ID"))){
					sql = new StringBuffer();
					sql.append("insert into bas_suplr_area(id,suplr_id,unload_area_id,unload_area_name,trans_srvc_id,addwho,addtime) values(");
					sql.append("(select sys_guid() from dual),'");
					sql.append(suplr_id);
					sql.append("','");
					sql.append(recordList.get(i).getAttributeAsString("UNLOAD_AREA_ID"));
					sql.append("','");
					sql.append(recordList.get(i).getAttributeAsString("UNLOAD_AREA_NAME"));
					sql.append("','");
					sql.append(recordList.get(i).getAttributeAsString("TRANS_SRVC_ID"));
					sql.append("','");
					sql.append(login_user);
					sql.append("',sysdate)");
					sqlList.add(sql.toString());
				}
				keyMap.put(recordList.get(i).getAttributeAsString("UNLOAD_AREA_ID")+ recordList.get(i).getAttributeAsString("TRANS_SRVC_ID"), "ABC");
			}else{
				msg.append(recordList.get(i).getAttributeAsString("UNLOAD_AREA_NAME")+" ");
			}
		}
		
		if(msg.length() > 0){
			MSGUtil.sayError("存在重复数据，请清除:"+msg.toString());
			return;
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG","M");
					crit.addCriteria("SUPLR_ID", suplr_id);
					table.fetchData(crit);
				}else
					MSGUtil.showOperError();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}


}
