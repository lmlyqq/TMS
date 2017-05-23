package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 确认发运(按调度单发运)
 * @author yuanlei
 *
 */
public class LoadSendConfirmAction implements ClickHandler {

	private DispatchView view;
	private SGTable loadTable;
	private SGTable shpmTable;
	private ListGridRecord lstrecord;
	public LoadSendConfirmAction(DispatchView p_view, SGTable p_loadTable, SGTable p_shpmTable) {
		this.view = p_view;
		this.loadTable = p_loadTable;
		this.shpmTable = p_shpmTable;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		/*Util.async.getServTime("yyyy/MM/dd HH:mm", new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				;
			}

			@Override
			public void onSuccess(String result) {*/
				lstrecord = loadTable.getSelectedRecord();
				final Record record = loadTable.getEditedRecord(loadTable.getRecordIndex(lstrecord));
				HashMap<String, Object> listmap = new HashMap<String, Object>(); 
				HashMap<String, String> shpmno_map = new HashMap<String, String>();
				
				if(record != null) {
					/*if(!ObjUtil.isNotNull(record.getAttribute("DEPART_TIME"))) {
						SC.warn(Util.TI18N.LOAD_TIME() + Util.MI18N.CHK_NOTNULL());
						return;
					}*/
					if(!record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)
							&& !record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_PART_DEPART_NAME)) {
						SC.warn(Util.TI18N.LOAD_NO() + "已发运!");
						return;
					}
					if(shpmTable == null || shpmTable.getSelection().length == shpmTable.getRecords().length) {
						//按调度单发运
						shpmno_map.put("1", " ");
					}
					else {
						//按作业单发运
						SC.warn("无效操作，请在作业单使用右键进行发运确认!");
						return;
					}
					listmap.put("1", record.getAttribute("LOAD_NO"));
					listmap.put("2", record.getAttribute("DEPART_TIME"));
					listmap.put("3", shpmno_map);
					listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(listmap);
					Util.async.execProcedure(json, "SP_LOADNO_SENDCONFIRM(?,?,?,?,?)", new AsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
								MSGUtil.sayInfo(result.substring(2));
								//刷新调度单
								lstrecord.setAttribute("STATUS_NAME", StaticRef.TRANS_DEPART_NAME);
								loadTable.updateData(lstrecord);
								loadTable.redraw();
								
        						if(view.shpmTable != null) {
        							ListGridRecord[] recs = view.shpmTable.getRecords();
        							for(int i = 0; i < recs.length; i++) {
        								recs[i].setAttribute("DEPART_TIME", record.getAttribute("DEPART_TIME"));
        								recs[i].setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
        							}
        							view.shpmTable.setRecords(recs);
        							view.shpmTable.redraw();
        						}
								
								view.setSendBtnStatus(false);
								view.setSaveBtnStatus(false);
								view.setDelBtnStatus(false);
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
					SC.warn("未选择调度单!");
					return;
				} 
		//	}
		//});
	}

}
