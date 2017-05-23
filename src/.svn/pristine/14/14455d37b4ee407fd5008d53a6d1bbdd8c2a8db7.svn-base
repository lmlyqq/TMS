package com.rd.client.action.tms.shpmreceipt;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShpmReceiptView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CancelShpmAction implements ClickHandler{
	private SGTable headTable;
	private TmsShpmReceiptView view;
//	private Map<String, String> editMap;
	private Record record;
	
	public CancelShpmAction(SGTable headTable,TmsShpmReceiptView view ){
		this.headTable = headTable;
		this.view = view;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] records = headTable.getSelection();
		if(!ObjUtil.isNotNull(records)){
			MSGUtil.sayError("请先选择订单后，再执行【确认回单】！");
			return;
		}
	
		 SC.confirm("请确认是否执行取消回单？",new BooleanCallback() {
				public void execute(Boolean value) {
                 if (value != null && value) {
//                 	doCancel();
                 	dooperate(records);
                 }
             }
         });
		
		
	}
	private  void dooperate(ListGridRecord[] records){
		StringBuffer sf = new StringBuffer();
		for(int i = 0; i < records.length ; i++){
			record = records[i];
			String STATUS = record.getAttribute("STATUS");
			if (!StaticRef.SHPM_RECEIPT.equals(STATUS)) {
				sf.append(record.getAttribute("SHPM_NO"));
				continue;
			}
		}
		if(sf.length()>0){
			MSGUtil.sayError("作业单："+sf.substring(0,sf.length()-1)+" 状态不等于【已回单】,不能进行回单操作 ");
		}
			
		doCancel();
	}
	private void doCancel(){
		final ListGridRecord[] records = headTable.getSelection();
		
    	HashMap<String, Object> listmap = new HashMap<String, Object>();
    	HashMap<String, Object> odr_no_map = new HashMap<String, Object>();
    	
    	if(records.length > 0) {
			for(int i = 0; i < records.length; i++) {
				if(StaticRef.SHPM_RECEIPT.equals(records[i].getAttribute("STATUS"))){
					odr_no_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(records[i].getAttribute("SHPM_NO"),"0").toString());//
				}
			}
		}
    	
    	listmap.put("1", odr_no_map);
		listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
    	
		String proName="SP_SHPM_CANCELRECLAIM(?,?,?)";
		
		Util.async.execProcedure(json, proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					
					MSGUtil.showOperSuccess();
					
					view.confirmorderButton.enable();
					view.cancelorderButton.disable();
					
					ListGridRecord [] listrecords = headTable.getSelection();
					
					for(int i=0;i<listrecords.length;i++){
						if(StaticRef.SHPM_RECEIPT.equals(listrecords[i].getAttribute("STATUS"))){
							int row = headTable.getRecordIndex(listrecords[i]);
							
							listrecords[i].setAttribute("STATUS", StaticRef.SHPM_UNLOAD);
							listrecords[i].setAttribute("STATUS_NAME", StaticRef.SHPM_UNLOAD_NAME);
							listrecords[i].setAttribute("LOSDAM_FLAG", "N");
							
							headTable.setEditValue(row, "POD_TIME", "");
							headTable.setEditValue(row, "POD_DELAY_REASON", "");
							headTable.setEditValue(row, "POD_DELAY_DAYS", "");
							
							headTable.updateData(listrecords[i]);
							
						}
						
					}
					view.damageTable.setData(new RecordList());
					headTable.redraw();
				}else{
					MSGUtil.sayError(result);
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
