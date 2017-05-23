package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 确认发运（按作业单）
 * @author yuanlei
 *
 */
public class ShpmSendConfirmAction implements ClickHandler {

	private SGTable shpmTable;
	private SGTable loadTable;
	public ShpmSendConfirmAction(SGTable p_shpmTable, SGTable p_loadTable) {
		this.shpmTable = p_shpmTable;
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		/*Util.async.getServTime("yyyy/MM/dd HH:mm", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				; 
			}

			@Override
			public void onSuccess(String result) {*/
				final ListGridRecord[] records = shpmTable.getSelection();
				HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
				
				final Record record = shpmTable.getEditedRecord(shpmTable.getRecordIndex(records[0]));
				/*if(record == null || !ObjUtil.isNotNull(record.getAttribute("DEPART_TIME"))) {
					SC.warn(Util.TI18N.END_LOAD_TIME() + Util.MI18N.CHK_NOTNULL());
					return;
				}*/
				for(int i = 0; i < records.length; i++) {
					shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
					if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.SHPM_DIPATCH_NAME)) {
						SC.warn(Util.TI18N.SHPM_NO() + "已发运!");
						return;
					}
					/*if(records[i].getAttribute("ODR_TIME")!=null){
						if(DateUtil.isAfter(record.getAttribute("DEPART_TIME"), records[i].getAttribute("ODR_TIME"))) {
							SC.warn("发运时间小于订单时间，不能发运!");
							return;
						}
					}
					if(DateUtil.isAfter2(result, record.getAttribute("DEPART_TIME"))) {
						SC.warn("发运时间不能大于当前时间!");
						return;
					}*/
				}
				
				HashMap<String, Object> listmap = new HashMap<String, Object>(); 
				if(record != null) {
					if(shpmTable == null || shpmTable.getSelection().length == 0) {
						SC.warn("无效的操作!");
						return;
					}
					listmap.put("1", record.getAttribute("LOAD_NO"));
					listmap.put("2", record.getAttribute("DEPART_TIME"));
					listmap.put("3", shpm_map);
					listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(listmap);
					Util.async.execProcedure(json, "SP_LOADNO_SENDCONFIRM(?,?,?,?,?)", new AsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
								//刷新作业单
								for(int i = 0; i < records.length; i++) {
									records[i].setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
									shpmTable.updateData(records[i]);
								}
								shpmTable.redraw();
								
								ListGridRecord rec = loadTable.getSelectedRecord();
								//刷新调度单
								if(result.substring(2,4).equals(StaticRef.TRANS_DEPART)) {
									//完全发运
									rec.setAttribute("STATUS_NAME", StaticRef.TRANS_DEPART_NAME);
									rec.setAttribute("DEPART_TIME", result.substring(4));
								}
								//yuanlei 2012-9-7 
								//更新部分到货状态调度单下作业单发运时，调度单状态变成部分发运状态的BUG，存储过程已修改
								/*else {
									rec.setAttribute("STATUS_NAME", StaticRef.TRANS_PART_DEPART_NAME);
								}*/
								else if(result.substring(2, 4).equals(StaticRef.TRANS_PART_DEPART)){
									//部分发运
									rec.setAttribute("STATUS_NAME", StaticRef.TRANS_PART_DEPART_NAME);
								}
								else if(result.substring(2, 4).equals(StaticRef.TRANS_PART_UNLOAD)){
									//部分到货
									rec.setAttribute("STATUS_NAME", StaticRef.TRANS_PART_UNLOAD_NAME);
								}
								//yuanlei 
								loadTable.updateData(rec);
								//loadTable.setEditValue(row, "DEPART_TIME", result.substring(4));
								loadTable.redraw();
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
				else {
					SC.warn("未选择作业单!");
					return;
				}
			/*}
		});*/
	}

}
