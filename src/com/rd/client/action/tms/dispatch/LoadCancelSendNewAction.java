package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.VehicleDispatchView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 确认发运
 * @author yuanlei
 *
 */
public class LoadCancelSendNewAction implements ClickHandler {

	private VehicleDispatchView view;
	private SGTable loadTable;
	private SGTable shpmTable;
	private ListGridRecord lstrecord;
	private int row;
	public LoadCancelSendNewAction(VehicleDispatchView p_view, SGTable p_loadTable, SGTable p_shpmTable) {
		this.view = p_view;
		this.loadTable = p_loadTable;
		this.shpmTable = p_shpmTable;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		SC.confirm("确认取消发运?", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
            		lstrecord = loadTable.getSelectedRecord();
            		row = loadTable.getRecordIndex(lstrecord);
            		Record record = loadTable.getEditedRecord(loadTable.getRecordIndex(lstrecord));
            		HashMap<String, Object> listmap = new HashMap<String, Object>(); 
            		HashMap<String, String> shpmno_map = new HashMap<String, String>();
            		
            		if(record != null) {
            			if(record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
            				SC.warn(Util.TI18N.LOAD_NO() + "未发运,不需要取消发运!");
            				return;
            			}
            			if(shpmTable == null || shpmTable.getSelection().length == shpmTable.getRecords().length) {
            				//按调度单发运
            				shpmno_map.put("1", " ");
            			}
            			else {
            				//按作业单发运
            				ListGridRecord[] rec = shpmTable.getSelection();
            				for(int i = 0; i < rec.length; i++) {
            					shpmno_map.put(Integer.toString(i+1),rec[i].getAttribute("SHPM_NO"));
            				}
            			}
            			listmap.put("1", record.getAttribute("LOAD_NO"));
            			listmap.put("2", shpmno_map);
            			listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
            			String json = Util.mapToJson(listmap);
            			Util.async.execProcedure(json, "SP_LOADNO_CANCELSEND(?,?,?,?)", new AsyncCallback<String>() {
            				@Override
            				public void onSuccess(String result) {
            					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
            						MSGUtil.sayInfo(result.substring(2));
            						//刷新调度单
            						lstrecord.setAttribute("STATUS_NAME", StaticRef.TRANS_CREATE_NAME);
            						//lstrecord.setAttribute("DEPART_TIME", "");
            						loadTable.updateData(lstrecord);
            						loadTable.redraw();
            						
            						loadTable.setEditValue(row, "DEPART_TIME", "");
            						
            						if(view.shpmTable != null) {
            							ListGridRecord[] recs = view.shpmTable.getRecords();
            							for(int i = 0; i < recs.length; i++) {
            								recs[i].setAttribute("DEPART_TIME", "");
            								recs[i].setAttribute("STATUS_NAME", StaticRef.SHPM_DIPATCH_NAME);
            							}
            							view.shpmTable.setRecords(recs);
            							view.shpmTable.redraw();
            						}
            						
    								view.setSendBtnStatus(true);
    								view.setSaveBtnStatus(false);
    								view.setDelBtnStatus(true);
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
                }
            }
        });
	}

}
