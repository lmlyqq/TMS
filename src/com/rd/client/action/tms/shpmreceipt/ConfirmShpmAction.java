package com.rd.client.action.tms.shpmreceipt;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.DateUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShpmReceiptView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConfirmShpmAction implements ClickHandler{
	private SGTable headTable;
	private TmsShpmReceiptView view;
//	private Record record;
	private Record rec;
	private Record edit_record;
	private boolean cur_flag = true;
	
	public ConfirmShpmAction(SGTable headTable,SGTable detailTable,TmsShpmReceiptView view){
		this.headTable = headTable;
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		ListGridRecord[] records = headTable.getSelection();
		int[] edit_row = headTable.getAllEditRows();
		if(edit_row.length == 0){
			MSGUtil.sayWarning("实际回单时间不能为空！");	
			return;
		}
		if(edit_row.length == 0){
			if(!ObjUtil.isNotNull(records[0].getAttribute("POD_TIME"))){
                	MSGUtil.sayWarning("实际回单时间不能为空！");			
			        return;
			}
			if(!ObjUtil.isNotNull(records[0].getAttribute("UNLOAD_TIME"))){
				MSGUtil.sayWarning("实际到货时间不能为空！");			
		        return;
			}
		}
		edit_record = headTable.getEditedRecord(edit_row[0]);
		for(int i = 0; i <records.length ;i++){
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
		doUpdate();
	}
	
	private void doUpdate() {
		final ListGridRecord[] records = headTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		
		HashMap<String, Object> in_shpm_no = new HashMap<String, Object>();
		HashMap<String, String> in_UNLOAD_TIME = new HashMap<String, String>();
		HashMap<String, String> in_UNLOAD_DELAY_REASON = new HashMap<String, String>();    
		
		HashMap<String, String> in_POD_TIME = new HashMap<String, String>(); 
		HashMap<String, String> in_POD_DELAY_REASON = new HashMap<String, String>();   
		
		HashMap<String, String> in_shpm_ROW = new HashMap<String, String>(); 
		HashMap<String, String> in_LD_QNTY  = new HashMap<String, String>(); 
		HashMap<String, String> in_UNLD_QNTY = new HashMap<String, String>();
//		HashMap<String, String> in_UNLD_QNTY_EACH = new HashMap<String, String>(); 
		HashMap<String, String> in_UNLD_VOL = new HashMap<String, String>(); 
		HashMap<String, String> in_UNLD_GWGT = new HashMap<String, String>(); 
		HashMap<String, String> in_UNLD_NWGT = new HashMap<String, String>(); 
		HashMap<String, String> in_UNLD_WORTH = new HashMap<String, String>();
		HashMap<String, String> in_sign_atary = new HashMap<String, String>();
		
		if(records.length == 1&& view.detailTable != null) {
			Record	edit_record_ = headTable.getEditedRecord(0);
			in_shpm_no.put("1", records[0].getAttribute("SHPM_NO"));
			in_UNLOAD_TIME.put("1", records[0].getAttribute("UNLOAD_TIME").replace("-", "/").substring(0,19));
			in_UNLOAD_DELAY_REASON.put("1",ObjUtil.ifObjNull(edit_record.getAttribute("UNLOAD_DELAY_REASON")," ").toString());
			String POD_TIME = ObjUtil.ifObjNull(edit_record_.getAttribute("POD_TIME"),edit_record.getAttribute("POD_TIME")).toString();
			in_POD_TIME.put("1",POD_TIME);
			in_POD_DELAY_REASON.put("1",ObjUtil.ifNull(edit_record.getAttribute("POD_DELAY_REASON")," "));
			in_sign_atary.put("1", ObjUtil.ifObjNull(records[0].getAttribute("SIGNATARY"), " ").toString());
			
			ListGridRecord[] itemRecords = view.detailTable.getRecords();
			int[] rows = view.detailTable.getAllEditRows();
			if(rows.length > 0 ) {
				for(int i = 0; i < itemRecords.length; i++) {
					rec = view.detailTable.getEditedRecord(i);
					if(records[0].getAttribute("SHPM_NO").equals(rec.getAttribute("SHPM_NO"))){
						in_UNLOAD_DELAY_REASON.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("UNLOAD_DELAY_REASON"),"0").toString());//发货数量
						in_POD_DELAY_REASON.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("POD_DELAY_REASON"),"0").toString());//收货体积
						in_shpm_ROW.put(Integer.toString(i+1),ObjUtil.ifObjNull( rec.getAttribute("SHPM_ROW"),"0").toString());//收货毛重
						in_LD_QNTY.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("LD_QNTY"),"0").toString());//收货净重
						in_UNLD_QNTY.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_QNTY"),"0").toString());//收货货值
//						in_UNLD_QNTY_EACH.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_QNTY_EACH"),"0").toString());//收货货值
						in_UNLD_VOL.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("UNLD_VOL"),"0").toString());//残损数量
						in_UNLD_GWGT.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("UNLD_GWGT"),"0").toString());//残损类型
						in_UNLD_NWGT.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_NWGT"),"0").toString());//残损单位
						in_UNLD_WORTH.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("UNLD_WORTH"),"0").toString());//残损金额
					}
				}
			} else {
				in_shpm_ROW.put("1", " ");
				in_LD_QNTY.put("1", " ");
				in_UNLD_QNTY.put("1", " ");
//				in_UNLD_QNTY_EACH.put("1", " ");
				in_UNLD_VOL.put("1", " ");
				in_UNLD_GWGT.put("1", " ");
				in_UNLD_NWGT.put("1", " ");
				in_UNLD_WORTH.put("1", " ");
			}
		} else {//同时选中多条单子默认为批量回单
			    for(int i = 0; i <records.length ;i++){
			    	Record edit_record_ = headTable.getEditedRecord(headTable.getRecordIndex(records[i]));
			    	String UNLOAD_TIME = ObjUtil.ifObjNull(edit_record_.getAttribute("UNLOAD_TIME"),edit_record.getAttribute("UNLOAD_TIME")).toString().replace("-", "/").substring(0,19);
			    	String POD_TIME = ObjUtil.ifObjNull(edit_record_.getAttribute("POD_TIME"),edit_record.getAttribute("POD_TIME")).toString();
			    	String POD_DELAY_REASON = ObjUtil.ifObjNull(edit_record_.getAttribute("UNLOAD_DELAY_REASON")," ").toString();
			    	String UNLOAD_DELAY_REASON = ObjUtil.ifObjNull(edit_record_.getAttribute("POD_DELAY_REASON")," ").toString();
			    	in_shpm_no.put(Integer.toString(i+1),records[i].getAttribute("SHPM_NO"));
			    	in_UNLOAD_TIME.put(Integer.toString(i+1), UNLOAD_TIME);
			    	in_sign_atary.put(Integer.toString(i+1), ObjUtil.ifObjNull(edit_record_.getAttribute("SIGNATARY"), " ").toString());
			    	if(!ObjUtil.isNotNull(UNLOAD_DELAY_REASON)){
			    		in_UNLOAD_DELAY_REASON.put(Integer.toString(i+1)," ");
			    	} else {
			    		in_UNLOAD_DELAY_REASON.put(Integer.toString(i+1), POD_DELAY_REASON);
			    	}
			    	in_POD_TIME.put(Integer.toString(i+1), POD_TIME);
			    	if(!ObjUtil.isNotNull(POD_DELAY_REASON)){
			    		in_POD_DELAY_REASON.put(Integer.toString(i+1)," ");
			    	} else {
			    		in_POD_DELAY_REASON.put(Integer.toString(i+1), UNLOAD_DELAY_REASON);
			    	}
			    }
				in_shpm_ROW.put("1", " ");
				in_LD_QNTY.put("1", " ");
				in_UNLD_QNTY.put("1", " ");
//				in_UNLD_QNTY_EACH.put("1", " ");
				in_UNLD_VOL.put("1", " ");
				in_UNLD_GWGT.put("1", " ");
				in_UNLD_NWGT.put("1", " ");
				in_UNLD_WORTH.put("1", " ");
		}
			listmap.put("1", in_shpm_no);
			listmap.put("2", in_UNLOAD_TIME);//实际收货时间  
			listmap.put("3", in_UNLOAD_DELAY_REASON);
			listmap.put("4", in_POD_TIME);
			listmap.put("5", in_POD_DELAY_REASON);
			listmap.put("6", in_shpm_ROW);
			listmap.put("7", in_LD_QNTY);
			listmap.put("8", in_UNLD_QNTY);
//			listmap.put("8", in_UNLD_QNTY_EACH);
			listmap.put("9", in_UNLD_VOL);
			listmap.put("10", in_UNLD_GWGT);
			listmap.put("11", in_UNLD_NWGT);
			listmap.put("12", in_UNLD_WORTH);
			listmap.put("13", in_sign_atary);
			listmap.put("14", LoginCache.getLoginUser().getUSER_ID());
			
			String json = Util.mapToJson(listmap);
			
			Util.async.execProcedure(json, "SP_SHPM_RECLAIM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						view.confirmorderButton.disable();
						view.cancelorderButton.enable();
						//刷新状态
						ListGridRecord[] records = headTable.getSelection();
						for(int i=0;i<records.length;i++){
							Record edit_record_ = headTable.getEditedRecord(headTable.getRecordIndex(records[i]));
							if(rec!=null){
								if(ObjUtil.isNotNull(rec.getAttribute("LOSDAM_QNTY"))){
									records[i].setAttribute("LOSDAM_FLAG", "Y");
								}
							}
							records[i].setAttribute("POD_TIME", ObjUtil.ifObjNull(edit_record_.getAttribute("POD_TIME"),edit_record.getAttribute("POD_TIME")).toString());
							records[i].setAttribute("UNLOAD_TIME", ObjUtil.ifObjNull(edit_record_.getAttribute("UNLOAD_TIME"),edit_record.getAttribute("UNLOAD_TIME")).toString());
							records[i].setAttribute("STATUS", StaticRef.SO_RECEIPT);
							records[i].setAttribute("STATUS_NAME", StaticRef.SO_RECEIPT_NAME);
							records[i].setAttribute("SIGNATARY", ObjUtil.ifObjNull(edit_record_.getAttribute("SIGNATARY"), edit_record_.getAttribute("SIGNATARY")).toString());
							if(!ObjUtil.isNotNull(edit_record_.getAttribute("POD_DELAY_REASON")) && !ObjUtil.isNotNull(edit_record.getAttribute("POD_DELAY_REASON"))){
								records[i].setAttribute("POD_DELAY_REASON", " ");
							} else {
								records[i].setAttribute("POD_DELAY_REASON", ObjUtil.ifObjNull(edit_record_.getAttribute("POD_DELAY_REASON"),edit_record.getAttribute("POD_DELAY_REASON")).toString());
							}
							if(!ObjUtil.isNotNull(edit_record_.getAttribute("UNLOAD_DELAY_REASON")) && !ObjUtil.isNotNull(edit_record.getAttribute("UNLOAD_DELAY_REASON"))){
								records[i].setAttribute("UNLOAD_DELAY_REASON", " ");
							} else {
								records[i].setAttribute("UNLOAD_DELAY_REASON", ObjUtil.ifObjNull(edit_record_.getAttribute("UNLOAD_DELAY_REASON"),edit_record.getAttribute("UNLOAD_DELAY_REASON")).toString());
							}

							//headTable.updateData(records[i]);
						}
						Criteria crit = new Criteria();
						crit.addCriteria("OP_FLAG","M");
						crit.addCriteria("SHPM_NO",edit_record.getAttribute("SHPM_NO"));
						crit.addCriteria("A",cur_flag);
						cur_flag  = !cur_flag;
						view.damageTable.fetchData(crit);
						
						
						headTable.discardAllEdits();
						headTable.redraw();
						
						
						
					}
					
					else{
						MSGUtil.sayError(result.substring(2));
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
		}
		
	}

