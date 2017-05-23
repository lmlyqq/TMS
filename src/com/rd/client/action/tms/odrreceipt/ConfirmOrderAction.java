package com.rd.client.action.tms.odrreceipt;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsOdrReceiptView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 回单管理-->【确认回单】按钮
 * @author wangjun
 *
 */
public class ConfirmOrderAction implements ClickHandler {
	private SGTable orderTable;
	private TmsOdrReceiptView view;
	private Map<String, String> editMap;
	private Record record;
	private Object UNLOAD_TIME;
	private Object FOLLOW_POD_TIME;
	private Record rec;
	
	// 计划回单时间 PRE_POD_TIME
	// 实际回单时间    POD_TIME
	// 实际收货时间 UNLOAD_TIME
	// 预达时间 PRE_UNLOAD_TIME
	// 收获延迟原因 LOAD_DELAY_REASON
	// 回单延迟原因 POD_DELAY_REASON
	// ORDER_STATE
	public ConfirmOrderAction(SGTable orderTable, TmsOdrReceiptView view) {
		this.orderTable = orderTable;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	public void onClick(ClickEvent event) {
		
		ListGridRecord[] records = orderTable.getSelection();
		if(records.length == 0){
			MSGUtil.sayError("请先选择订单后，再执行【确认回单】！");
			return;
		}
		
		for (int j = 0; j < records.length; j++) {
			record = records[j];
			
			if (record != null) {
				editMap = orderTable.getEditValues(view.hRow);
				
				String STATUS_FORM = record.getAttribute("STATUS");//托运单状态
				
				//只有以下状态托运单才能执行  确认回单 ：托运单状态=【已确认】 AND到货状态=【完全到货】
				if (!StaticRef.SO_CONFIRM.equals(STATUS_FORM)) {
					
					MSGUtil.sayError("只有【已确认】的托运单才可做回单确认！");
					return;
				}
				
				
				UNLOAD_TIME = ObjUtil.ifObjNull(editMap.get("UNLOAD_TIME"),record.getAttribute("UNLOAD_TIME"));
				//final String UNLOAD_TIME = record.getAttribute("UNLOAD_TIME");
				
   //			final String UNLOAD_TIME ="";// 实际收货时间 (大于预达时间)/
				if(!ObjUtil.isNotNull(UNLOAD_TIME) ){
//					&& !ObjUtil.isNotNull(record.getAttribute("UNLOAD_TIME"))){
					MSGUtil.sayError("【实际收货时间】不能为空！");
					return;
				}
				
//				String PRE_UNLOAD_TIME = record.getAttribute("PRE_UNLOAD_TIME");// 预达时间
				//String LOAD_DELAY_REASON = record.getAttribute("LOAD_DELAY_REASON");//
				
//				final String LOAD_DELAY_REASON = ObjUtil.ifObjNull(editMap.get("UNLOAD_DELAY_REASON"),record.getAttribute("UNLOAD_TIME")).toString();
//				if (DateUtil.isAfter(PRE_UNLOAD_TIME, UNLOAD_TIME.toString())
//						&& !ObjUtil.isNotNull(LOAD_DELAY_REASON)) {
//					
//					MSGUtil.sayError("实际收货时间晚于预达时间，必须填写 [收货延迟原因]");
//					return;
//					
//				}
				
				
				//final String FOLLOW_POD_TIME = editMap.get("POD_TIME").toString();// 实际回单时间
//				FOLLOW_POD_TIME = editMap.get("POD_TIME");// 实际回单时间
				FOLLOW_POD_TIME = ObjUtil.ifObjNull(editMap.get("POD_TIME"),record.getAttribute("POD_TIME"));
				//final String FOLLOW_POD_TIME = record.getAttribute("POD_TIME");
				if(!ObjUtil.isNotNull(FOLLOW_POD_TIME)){
					MSGUtil.sayError("【实际回单时间】不能为空！");
					return;
				}
//				final String PRE_POD_TIME = record.getAttribute("PRE_POD_TIME");// 计划回单时间
//				final String POD_DELAY_REASON = ObjUtil.ifObjNull(editMap.get("POD_DELAY_REASON"),"").toString();
//				if (DateUtil.isAfter(PRE_POD_TIME, FOLLOW_POD_TIME)
//						&& !ObjUtil.isNotNull(POD_DELAY_REASON)) {
//					
//					MSGUtil.sayError("实际回单时间晚于计划回单时间，必须填写 [回单延迟原因 ]");
//					return;
//					
//				}
				
				
				if(view.orderlstTable != null){
					int[] edit_row = view.orderlstTable.getAllEditRows();
					
					
					//不完全签收情况
					if(edit_row.length > 0){
						StringBuffer msg  = new StringBuffer();
						for(int i=0;i<edit_row.length;i++){
							
							//只有以下状态托运单才能执行  确认回单 ：托运单状态=【已确认】 AND到货状态=【完全到货】
							if (!StaticRef.SO_CONFIRM.equals(STATUS_FORM)) {
								
								MSGUtil.sayError("只有【已确认】的托运单才可做回单确认！");
								return;
							}
							
							//实际收获数量非空
							if(ObjUtil.isNotNull(view.orderlstTable.getEditValue(edit_row[i], "UNLD_QNTY"))){
								String ld_qnty = view.orderlstTable.getEditValue(edit_row[i], "UNLD_QNTY").toString(); //实际收货数量
								String unld_qnty = view.orderlstTable.getRecord(edit_row[i]).getAttribute("LD_QNTY");//实际发货数量
								
								double LD_QNTY = Double.parseDouble(ld_qnty);
								double UNLD_QNTY = Double.parseDouble(unld_qnty);
								
								// 收货数量 > 发货数量
								if (LD_QNTY > UNLD_QNTY) {
									msg.append(edit_row[i] + 1);
									msg.append(",");
								}
							}
						}
						if (ObjUtil.isNotNull(msg.toString())) {
							MSGUtil.sayError("托运单明细第"+msg.substring(0, msg.length() - 1)+ "行收货数量大于发货数量！");
							return;
						}
					}
				}		
				doUpdate();
				
			}
		}
		

	}
	
	private void doUpdate() {

		// 更新作业单：由【已确认】更新为【已回单】
		// public static final String SO_CONFIRM = "20";
		// public static final String SO_CONFIRM_NAME = "已确认";
		// public static final String SO_RECEIPT_NAME = "已回单";
		// public static final String SO_RECEIPT = "60";
		
		final ListGridRecord[] records = view.orderTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		HashMap<String, String> row_map = new HashMap<String, String>(); //托运单明细    
		
		HashMap<String, String> ld_qnty_map = new HashMap<String, String>(); //托运单明细  LD_QNTY
		HashMap<String, String> unld_qnty_map = new HashMap<String, String>(); //托运单明细  
		
		HashMap<String, String> vol_map = new HashMap<String, String>(); //托运单明细   
		HashMap<String, String> gwgt_map = new HashMap<String, String>(); //托运单明细      
		HashMap<String, String> nwgt_map = new HashMap<String, String>(); //托运单明细     
		HashMap<String, String> worth_map = new HashMap<String, String>(); //托运单明细  
		HashMap<String, String> dmgqty_map = new HashMap<String, String>(); //货差货损表
		HashMap<String, String> damage_typ_map = new HashMap<String, String>(); //货差货损表 残损类型 LOSS_DAMAGE_TYP 
		HashMap<String, String> trans_uom_map = new HashMap<String, String>(); //货差货损表
		HashMap<String, String> amount_map = new HashMap<String, String>(); //货差货损表
		
		if(records != null && records.length > 0) {
			//int pos = 0;
		    //final Record record = orderTable.getEditedRecord(orderTable.getRecordIndex(records[0]));
			if(records.length == 1 && view.orderlstTable != null) {
				order_map.put("1", records[0].getAttribute("ODR_NO"));
			    //ListGridRecord[] itemRecords = view.orderlstTable.getRecords();
				ListGridRecord[] itemRecords = view.orderlstTable.getRecords();
				int[] rows = view.orderlstTable.getAllEditRows();
				if(rows.length > 0) {
					for(int i = 0; i < itemRecords.length; i++) {
					  //String row = view.orderlstTable.getEditedRecord(i).getAttribute("ODR_ROW");
						rec = view.orderlstTable.getEditedRecord(i);
						row_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("ODR_ROW"),"0").toString());//明细行号
						ld_qnty_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("LD_QNTY"),"0").toString());//发货数量
						unld_qnty_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_QNTY"),"0").toString());//收货数量
						vol_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("UNLD_VOL"),"0").toString());//收货体积
						gwgt_map.put(Integer.toString(i+1),ObjUtil.ifObjNull( rec.getAttribute("UNLD_GWGT"),"0").toString());//收货毛重
						nwgt_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_NWGT"),"0").toString());//收货净重
						worth_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("UNLD_WORTH"),"0").toString());//收货货值
						dmgqty_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("DAMA_QNTY"),"0").toString());//残损数量
						damage_typ_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("LOSS_DAMAGE_TYP")," ").toString());//残损类型
						trans_uom_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(rec.getAttribute("DAMA_TRANS_UOM"),"0").toString());//残损单位
						amount_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(rec.getAttribute("AMOUNT"),"0").toString());//残损金额
						
					}
				}else {
					for(int i = 0; i< records.length; i++) {
						order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
						row_map.put("1", " ");
						ld_qnty_map.put("1", " ");
						unld_qnty_map.put("1", " ");
						vol_map.put("1", " ");
						gwgt_map.put("1", " ");
						nwgt_map.put("1", " ");
						worth_map.put("1", " ");
						dmgqty_map.put("1", " ");
						damage_typ_map.put("1", " ");
						trans_uom_map.put("1", " ");
						amount_map.put("1", " ");
					
					}
				}
			}
			else {
				for(int i = 0; i< records.length; i++) {
					order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
					row_map.put("1", " ");
					ld_qnty_map.put("1", " ");
					unld_qnty_map.put("1", " ");
					vol_map.put("1", " ");
					gwgt_map.put("1", " ");
					nwgt_map.put("1", " ");
					worth_map.put("1", " ");
					dmgqty_map.put("1", " ");
					damage_typ_map.put("1", " ");
					trans_uom_map.put("1", " ");
					amount_map.put("1", " ");
				}
			}
     		listmap.put("1", order_map);
			listmap.put("2", UNLOAD_TIME);//实际收货时间  
			listmap.put("3", ObjUtil.ifObjNull(editMap.get("UNLOAD_DELAY_REASON"),""));//收货延迟原因
			listmap.put("4", FOLLOW_POD_TIME);//实际回单时间
			listmap.put("5", ObjUtil.ifObjNull(editMap.get("POD_DELAY_REASON"),""));//回单延迟原因
			listmap.put("6", row_map);
			listmap.put("7", ld_qnty_map);
			listmap.put("8", unld_qnty_map);
			listmap.put("9", vol_map);
			listmap.put("10", gwgt_map);
			listmap.put("11", nwgt_map);
			listmap.put("12", worth_map);
			listmap.put("13", dmgqty_map);
			listmap.put("14", damage_typ_map);
			listmap.put("15", trans_uom_map);
			listmap.put("16", amount_map);
			listmap.put("17", LoginCache.getLoginUser().getUSER_ID());
			
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_ORDER_RECLAIM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					  //MSGUtil.sayInfo(result.substring(2));
						MSGUtil.showOperSuccess();
						view.confirmorderButton.disable();
						view.cancelorderButton.enable();
						//STATUS_FORM
						//刷新状态
						ListGridRecord[] records = view.orderTable.getSelection();
						for(int i=0;i<records.length;i++){
							if(rec!=null){
								if(!rec.getAttribute("LD_QNTY").toString().equals(rec.getAttribute("UNLD_QNTY").toString())){
									records[i].setAttribute("LOSDAM_FLAG", "Y");
								}
							}
							records[i].setAttribute("STATUS", StaticRef.SO_RECEIPT);
							records[i].setAttribute("STATUS_NAME", StaticRef.SO_RECEIPT_NAME);
							
							view.UNLOAD_TIME.setCanEdit(false);
							view.UNLOAD_DELAY_REASON.setCanEdit(false);
							view.POD_TIME.setCanEdit(false);
							view.POD_DELAY_REASON.setCanEdit(false);
							
							view.orderTable.updateData(records[i]);
							view.orderTable.redraw();						
					
						}
						view.damageTable.invalidateCache();
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG", "M");
						criteria.addCriteria("ODR_NO", records[0].getAttribute("ODR_NO"));
						view.damageTable.fetchData(criteria);
						
						view.orderTable.deselectAllRecords();
						view.orderTable.selectRecord(records[0]);
						
						
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
}