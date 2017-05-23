package com.rd.client.action.tms.odrreceipt;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConfirmDamageAction implements ClickHandler{
	private SGTable damageTable;
	private SGTable headerTable;
	private String recProcName = "SP_SETT_SAV_REC_FEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private String payProcName = "SP_SETT_SAV_PAY_FEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private double rec_amount = 0.00;
	private double pay_amount = 0.00;
	private String notes;
	
	public ConfirmDamageAction(SGTable damageTable,SGTable headerTable){
		this.damageTable = damageTable;
		this.headerTable = headerTable;
	}

	@Override
	public void onClick(ClickEvent event) {
		SC.confirm("确认后，系统会自动产生客户和承运商的货损货差扣款费用，确认继续?", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
                	ListGridRecord[] records = damageTable.getRecords();
                	final ListGridRecord selRecord = headerTable.getSelectedRecord();
                	if(records == null || records.length == 0) {
                		MSGUtil.sayWarning("无货损货差信息！");			
    			        return;
                	}
                	if(selRecord == null || !ObjUtil.isNotNull(selRecord.getAttribute("ODR_NO"))) {
                		MSGUtil.sayWarning("未选择作业单信息或作业单列表未配置托运单号！");			
    			        return;
                	}
                	for(int i = 0; i < records.length; i++) {
                		rec_amount += Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("AMOUNT"),"0"));
                		pay_amount += Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("DRIVER_AMOUNT"),"0"));
                		notes = records[i].getAttribute("REASON");
                	}
                	if(rec_amount <= 0) {
                		MSGUtil.sayWarning("货损金额为0,无法确认！");			
    			        return;
                	}
                	Util.async.execProcedure(getRecList(selRecord,rec_amount,notes), recProcName, new AsyncCallback<String>() {
            			
            			@Override
            			public void onSuccess(String result) {
            				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
            					
            					Util.db_async.getRecord("LOAD_NO", "TRANS_SHIPMENT_HEADER", " where ODR_NO = '" + selRecord.getAttribute("ODR_NO") + "'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(ArrayList<HashMap<String, String>> result) {
										if(result != null && result.size() == 1) {
											Util.async.execProcedure(getPayList(selRecord,pay_amount,notes), payProcName, new AsyncCallback<String>() {

												@Override
												public void onFailure(Throwable caught) {
													caught.printStackTrace();
												}

												@Override
												public void onSuccess(String result) {
													if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
														MSGUtil.showOperSuccess();
													}
													else {
														MSGUtil.sayError(result.substring(2));
													}
												}
			            						
			            					});
										}
									}
            						
            					});
            					
            				}else{
            					MSGUtil.sayError(result.substring(2));
            				}	
            			}
            			
            			@Override
            			public void onFailure(Throwable caught) {
            				
            			}
                	});
                }
            }
        });
		/*for(int i = 0; i <records.length ;i++){
			Record edit_record_ = headTable.getEditedRecord(headTable.getRecordIndex(records[i]));
				if(edit_record_ != null) {
				Object STATUS = ObjUtil.ifObjNull(edit_record_.getAttribute("STATUS"),edit_record.getAttribute("STATUS"));
				Object UNLOAD_TIME = ObjUtil.ifObjNull(edit_record_.getAttribute("UNLOAD_TIME"),edit_record.getAttribute("UNLOAD_TIME"));
				Object SHPM_NO = ObjUtil.ifObjNull(edit_record_.getAttribute("SHPM_NO"),edit_record.getAttribute("SHPM_NO"));
				Object POD_TIME = ObjUtil.ifObjNull(edit_record_.getAttribute("POD_TIME"),edit_record.getAttribute("POD_TIME"));
				if (edit_record_ != null) {
					if (!StaticRef.SHPM_UNLOAD.equals(STATUS)) {
						MSGUtil.sayError("只有【已到货】 作业单才可做回单确认！");
						return;
					}
						if(!ObjUtil.isNotNull(UNLOAD_TIME)){
							MSGUtil.sayError("到货签收时间不能为空！");
							return;
					}
					if(!ObjUtil.isNotNull(SHPM_NO)){
						MSGUtil.sayError("托运单号不能为空！");
						return;
					}
					if(!ObjUtil.isNotNull(POD_TIME)){
							MSGUtil.sayError("实际回单时间不能为空！");
							return;
					}
					
					String unload_time = UNLOAD_TIME.toString().replace("-", "/").substring(0,19); 
					String pod_time = POD_TIME.toString();
					
					if(DateUtil.isAfter(pod_time,unload_time)){
						MSGUtil.sayError("【实际回单时间】不能早于【到货签收时间】");
						return;
					}
					
				}
			}
		}
		doUpdate();*/
	}
	
	private ArrayList<String> getRecList(ListGridRecord selRecord,double rec_amount,String notes){
		
		ArrayList<String> list= new ArrayList<String>();
		list.add(null);
		list.add(selRecord.getAttribute("LOAD_NO"));
		list.add("957E9AFAA9F34B64BCD3B375DB125E9D");
		list.add("货损货差扣款");
		list.add("VEHICLE");
		list.add("1");
		list.add(Double.toString(rec_amount));
		list.add(Double.toString(rec_amount));
		list.add("0");
		list.add(null);
		list.add("1");
		list.add(notes);
		list.add("A");
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}
	
	private ArrayList<String> getPayList(ListGridRecord selRecord,double pay_amount,String notes){
	
		ArrayList<String> list= new ArrayList<String>();
		list.add(null);
		list.add(selRecord.getAttribute("LOAD_NO"));
		list.add(selRecord.getAttribute("SHPM_NO"));
		list.add("CC287EF11DD643F3BC9728F0AE785391");
		list.add("货损货差扣款");
		list.add("VEHICLE");
		list.add("1");
		list.add(Double.toString(pay_amount));
		list.add(Double.toString(pay_amount));
		list.add("0");
		list.add(null);
		list.add(notes);
		list.add("A");
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}
	
}
