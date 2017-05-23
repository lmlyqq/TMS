package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 取消发运（按作业单）
 * @author yuanlei
 *
 */
public class ShpmCancelSendAction implements ClickHandler {

	private SGTable shpmTable;
	private SGTable loadTable;
	private ListGridRecord[] rec;
	private int[] rows;
	public ShpmCancelSendAction(SGTable p_shpmTable, SGTable p_loadTable) {
		this.shpmTable = p_shpmTable;
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		SC.confirm("确认取消发运?", new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
    				rec = shpmTable.getSelection();
            		HashMap<String, Object> listmap = new HashMap<String, Object>(); 
            		HashMap<String, String> shpmno_map = new HashMap<String, String>();
            		String load_no = "";
            		
            		if(rec != null) {
            			if(shpmTable == null) {
            				SC.warn("无效操作!");
            				return;
            			}
            			if(shpmTable.getSelection().length == shpmTable.getRecords().length) {
            				SC.warn("请选择调度单做取消发运!");
            				return;
            			}
        				//按作业单取消发运
            			rows = new int[rec.length];
        				for(int i = 0; i < rec.length; i++) {
        					load_no = rec[i].getAttribute("LOAD_NO");
        					shpmno_map.put(Integer.toString(i+1),rec[i].getAttribute("SHPM_NO"));
                			if(!rec[i].getAttribute("STATUS_NAME").equals(StaticRef.SHPM_LOAD_NAME)) {
                				SC.warn(Util.TI18N.LOAD_NO() + "状态不允许取消发运!");
                				return;
                			}
                			rows[i] = shpmTable.getRecordIndex(rec[i]);
        				}
            			listmap.put("1", load_no);
            			listmap.put("2", shpmno_map);
            			listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
            			String json = Util.mapToJson(listmap);
            			Util.async.execProcedure(json, "SP_LOADNO_CANCELSEND(?,?,?,?)", new AsyncCallback<String>() {
            				@Override
            				public void onSuccess(String result) {
            					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
            						MSGUtil.showOperSuccess();
            						//刷新作业单
            						for(int i = 0; i < rec.length; i++) {
            							rec[i].setAttribute("STATUS_NAME", StaticRef.SHPM_DIPATCH_NAME);
            							shpmTable.updateData(rec[i]);
            							shpmTable.setEditValue(rows[i], "DEPART_TIME", "");
            						}
            						shpmTable.redraw();
            						
        							ListGridRecord rec = loadTable.getSelectedRecord();
            						//刷新调度单
            						if(result.substring(2,4).equals(StaticRef.TRANS_CREATE)) {
            							//未发运
            							rec.setAttribute("STATUS_NAME", StaticRef.TRANS_CREATE_NAME);
            							rec.setAttribute("DEPART_TIME", "");
            						}
            						else {
            							//部分发运
            							rec.setAttribute("STATUS_NAME", StaticRef.TRANS_PART_DEPART_NAME);
            							rec.setAttribute("DEPART_TIME", "");
            						}
        							loadTable.updateData(rec);
        							loadTable.redraw();
        							
        							//loadTable.selectRecord(rec);
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
                }
            }
        });
	}

}
