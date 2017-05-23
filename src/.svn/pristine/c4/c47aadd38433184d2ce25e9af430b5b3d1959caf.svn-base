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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteRDCAction implements ClickHandler{

	private SGTable shpmTable;
	private SGTable rdcTable;
//	private ChangeRDCView view;
	
	public DeleteRDCAction(SGTable table,SGTable table2,ChangeRDCView view){
		this.shpmTable = table;
		this.rdcTable = table2;
//		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] recs = rdcTable.getSelection();
		if(recs.length>0){
			if (!"10".equals(recs[0].getAttribute("STATUS"))) {
				MSGUtil.sayError("转仓单非已创建状态，无法执行【删除】操作");
				return;
			}
			SC.confirm("是否删除指定记录", new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if (value != null && value) {
						HashMap<String,String> rdc_list = new HashMap<String, String>();
						String user_id = LoginCache.getLoginUser().getUSER_ID();
						String exec_org_id = LoginCache.getLoginUser().getDEFAULT_ORG_ID();
						for (int i = 0; i < recs.length; i++) {
							rdc_list.put(Integer.toString(i+1), ObjUtil.ifNull(recs[0].getAttribute("RDC_NO"), ""));
						}
						
						HashMap<String,Object> map =new HashMap<String, Object>();
						map.put("1", rdc_list);
						map.put("2", user_id);
						map.put("3", exec_org_id);
						
						Util.async.execProcedure(Util.mapToJson(map), "SP_RDC_DELETE(?,?,?,?)", new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								if (StaticRef.SUCCESS_CODE.equals(result.substring(0,2))) {
									shpmTable.invalidateCache();
									rdcTable.invalidateCache();
									Criteria cri1 = new Criteria();
									cri1.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
									cri1.addCriteria("DS_FLAG","CHANGED_RDC");
									shpmTable.fetchData(cri1);
									
									Criteria cri = new Criteria();
									cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
									cri.addCriteria("CHANGED_FLAG","N");
									rdcTable.fetchData(cri,new DSCallback() {
										
										@Override
										public void execute(DSResponse response, Object rawData, DSRequest request) {
											if (rdcTable.getRecords().length>0) {
												rdcTable.selectRecord(0);
											}
										}
									});
								}else{
									MSGUtil.sayError(result);
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								MSGUtil.sayError(caught.getMessage());
							}
						});
					}
				}
			});
		}else{
			MSGUtil.sayError("请选择转仓单");
		}
	}

}
