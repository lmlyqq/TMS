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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 回单管理--【取消回单】按钮
 * @author wangjun
 *
 */
public class CancelOrderAction implements ClickHandler{

	private SGTable orderTable;
	private TmsOdrReceiptView view;
	@SuppressWarnings("unused")
	private Map<String, String> editMap;
	private Record record;
	
	public CancelOrderAction(SGTable orderTable, TmsOdrReceiptView view) {
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
		
		record = records[0];
		
		if (record != null) {
			editMap = orderTable.getEditValues(view.hRow);
			String STATUS_FORM = record.getAttribute("STATUS");//托运单状态
			//final String STATUS_FORM = ObjUtil.ifObjNull(editMap.get("STATUS"),record.getAttribute("STATUS")).toString();
			
			//只有以下状态托运单才能执行  确认回单 ：托运单状态=【已回单】 
			if (!StaticRef.SO_RECEIPT.equals(STATUS_FORM)) {
				
				MSGUtil.sayError("托运单状态不等于【已回单】 ");
				return;
			}
				//SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
		    if(record!= null) {
		        SC.confirm("请确认是否执行【取消回单】",new BooleanCallback() {
					public void execute(Boolean value) {
	                    if (value != null && value) {
	                    	doClear();
	                    }
	                }
	            });
		        
			}
		   
		    
		}
	}



	private void doClear() {
	
		
		        //SP_ORDER_CANCELRECLAIM(
			    //in_odr_no LST,                  --托运单号
				//in_user_id VARCHAR2,            --用户ID
				//output_result OUT VARCHAR2
		
        	final ListGridRecord[] records = view.orderTable.getSelection();
        	HashMap<String, Object> listmap = new HashMap<String, Object>();
        	
        	HashMap<String, Object> odr_no_map = new HashMap<String, Object>();
        	
			if(records.length > 0) {
				for(int i = 0; i < records.length; i++) {
					odr_no_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(records[i].getAttribute("ODR_NO"),"0").toString());//
				}
			}
	
			
			listmap.put("1", odr_no_map);
			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_ORDER_CANCELRECLAIM(?,?,?)", new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						
						//将托运单明细中收货数量/毛重/体积清空；
						//实际收货时间，收货延迟原因，实际回单时间，回单延迟原因，清空； ""
						//托运单状态置为【已确认】
						//将托运单主表货损货差标识置为N  "N"
						
						//删除对应托运单的货损货差记录
						MSGUtil.showOperSuccess();
						
						view.confirmorderButton.enable();
						view.cancelorderButton.disable();
						
						ListGridRecord [] listrecords = orderTable.getSelection();
						
						for(int i=0;i<listrecords.length;i++){
							int row = orderTable.getRecordIndex(listrecords[i]);
							
//							listrecords[i].setAttribute("UNLOAD_TIME", " ");
//							listrecords[i].setAttribute("UNLOAD_DELAY_REASON", " ");
//							listrecords[i].setAttribute("POD_TIME", " ");
//							listrecords[i].setAttribute("POD_DELAY_REASON", " ");
							
							listrecords[i].setAttribute("STATUS", StaticRef.SO_CONFIRM);
							listrecords[i].setAttribute("STATUS_NAME", StaticRef.SO_CONFIRM_NAME);
							listrecords[i].setAttribute("LOSDAM_FLAG", "N");
							
							//orderTable.setEditValue(row, "UNLOAD_TIME", "");
							//orderTable.setEditValue(row, "UNLOAD_DELAY_REASON", "");
							orderTable.setEditValue(row, "POD_TIME", "");
							orderTable.setEditValue(row, "POD_DELAY_REASON", "");
							orderTable.setEditValue(row, "POD_DELAY_DAYS", "");
							
							
							if(StaticRef.SO_RECEIPT_NAME.equals(record.getAttribute("STATUS_NAME"))){
//								confirmorderButton.disable();
//								cancelorderButton.enable();
								view.UNLOAD_TIME.setCanEdit(false);
								view.UNLOAD_DELAY_REASON.setCanEdit(false);
								view.POD_TIME.setCanEdit(false);
								view.POD_DELAY_REASON.setCanEdit(false);
								
							}else if(StaticRef.SO_CONFIRM_NAME.equals(record.getAttribute("STATUS_NAME"))&&StaticRef.UNLOADED.equals(record.getAttribute("UNLOAD_STAT"))){
//								confirmorderButton.enable();
//								cancelorderButton.disable();
								view.UNLOAD_TIME.setCanEdit(true);
								view.UNLOAD_DELAY_REASON.setCanEdit(true);
								view.POD_TIME.setCanEdit(true);
								view.POD_DELAY_REASON.setCanEdit(true);
							} 
							orderTable.updateData(listrecords[i]);
							
						}
						
						if(ObjUtil.isNotNull(view.orderlstTable)){
						   ListGridRecord [] listrecords2 = view.orderlstTable.getSelection();
					       for(int j=0;j<listrecords2.length;j++){
						     int row2 = view.orderlstTable.getRecordIndex(listrecords2[j]);
						     view.orderlstTable.setEditValue(row2, "DAMA_QNTY", 0);
						     view.orderlstTable.updateData(listrecords[j]);
						   }
						
						}
						orderTable.redraw();
//						view.orderTable.deselectRecord(records[0]);
//						view.orderTable.selectRecord(records[0]);
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
