package com.rd.client.action.tms;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.ChangeRDCView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CreateRDCAction implements ClickHandler{

	private SGTable shpmTable;
	private SGTable rdcTable;
	private ChangeRDCView view;
	
	public CreateRDCAction(SGTable table,SGTable table2,ChangeRDCView view){
		this.shpmTable = table;
		this.rdcTable = table2;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] recs = shpmTable.getSelection();
		String load_id =recs[0].getAttribute("LOAD_ID");
		if(recs.length > 0){
			HashMap<String,String> shpm_list = new HashMap<String, String>();
			String user = LoginCache.getLoginUser().getUSER_ID();
			String exec_org_id = LoginCache.getLoginUser().getDEFAULT_ORG_ID();
			for(int i=0 ;i<recs.length ;i++){
				if (!load_id.equals(recs[i].getAttribute("LOAD_ID"))) {
					MSGUtil.sayError("原RDC不一致，请重新选择");
					return;
				}
				shpm_list.put(Integer.toString(i+1), ObjUtil.ifNull(recs[i].getAttribute("SHPM_NO"), ""));
			}
			HashMap<String,Object> map = new HashMap<String, Object>();
			map.put("1", shpm_list);
			map.put("2", user);
			map.put("3", exec_org_id);
			Util.async.execProcedure(Util.mapToJson(map), "SP_RDC_CREATE(?,?,?,?)", new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
						MSGUtil.showOperSuccess();
						shpmTable.invalidateCache();
						rdcTable.invalidateCache();
						Criteria cri1 = null;
						if(view.seaShpmForm != null){
							cri1 = view.seaShpmForm.getValuesAsCriteria();
						}else{
							cri1 = new Criteria();
						}
						cri1.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
						cri1.addCriteria("DS_FLAG","CHANGED_RDC");
						shpmTable.fetchData(cri1);
						
						Criteria cri = null;
						if(view.seaRDCForm != null){
							cri = view.seaRDCForm.getValuesAsCriteria();
						}else{
							cri = new Criteria();
						}
						cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
						cri.addCriteria("CHANGED_FLAG","N");
						rdcTable.fetchData(cri,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								rdcTable.selectRecord(0);
							}
						});
					}else {
						if (result.startsWith("01ORA-01403")) {
							MSGUtil.sayError("订单已更新，请重新查询");
						}else {
							MSGUtil.sayError(result);
						}
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
			
		}else{
			MSGUtil.sayError("请先选择作业单");
		}
	}

}
