package com.rd.client.action.base.address;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveAddrAreaAction implements ClickHandler {

	private SGTable table;
	private SGTable areaTable;
	private SGPanel form;

	public SaveAddrAreaAction(SGTable table, SGTable areaTable,SGPanel form) {
		this.table = table;
		this.areaTable = areaTable;
		this.form=form;
	}

	@Override
	public void onClick(ClickEvent event) {
		ListGridRecord selectedRecord = table.getSelectedRecord();
		if (selectedRecord == null) {
			MSGUtil.sayError("请选择一个地址点");
			return;
		}
		if(!ObjUtil.isNotNull(form.getItem("AREA_ID").getDisplayValue())){
			MSGUtil.sayError("行政区域不能为空");
			return;
		}
		final String addrId = selectedRecord.getAttribute("ID");
		if(!ObjUtil.isNotNull(addrId)){
			MSGUtil.sayError("请选择一个地址点");
			return;
		}
		String sql = "select count(1) from BAS_ADDRESS_AREA where addr_id = '"+
						addrId+"' and area_id = '"+
						form.getItem("AREA_ID").getDisplayValue()+"'";
		ArrayList<String> sqlList = new ArrayList<String>();
		sqlList.add(getInsertSQL(addrId));
		Util.async.excuteSQLListCheckUn1(sqlList, sql, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					areaTable.invalidateCache();
					Criteria cri = new Criteria();
					cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
					cri.addCriteria("ADDR_ID",addrId);
					areaTable.fetchData(cri);
					form.clearValues();
				}else if("uniquene".equals(result)){
					MSGUtil.sayError("行政区域已存在");
				}else{
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private String getInsertSQL(String addrId){
		StringBuffer sf=new StringBuffer();
		sf.append("insert into BAS_ADDRESS_AREA(");
		sf.append("ID,ADDR_ID,AREA_ID,AREA_NAME,ORG_ID,ORG_NAME,ADDTIME,ADDWHO");
		sf.append(")");
		sf.append(" values(sys_guid(),'");
		sf.append(addrId);
		sf.append("','");
		sf.append(form.getItem("AREA_ID").getDisplayValue());
		sf.append("','");
		sf.append(form.getItem("AREA_NAME").getDisplayValue());
		sf.append("','");
		sf.append(form.getItem("ORG_ID").getDisplayValue());
		sf.append("','");
		sf.append(form.getItem("ORG_NAME").getDisplayValue());
		sf.append("',sysdate,'");
		sf.append(LoginCache.getLoginUser().getUSER_ID()+"')");
		return sf.toString();
	}

}
