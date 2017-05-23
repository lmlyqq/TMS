package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.VehicleDispatchView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 删除调度单
 * @author yuanlei
 *
 */
public class DeleteLoadNoNewAction implements ClickHandler {

	private SGTable loadTable;
	private VehicleDispatchView view;
	private ListGridRecord lstrecord;
	public DeleteLoadNoNewAction(SGTable p_loadTable, VehicleDispatchView p_view) {
		this.loadTable = p_loadTable;
		this.view = p_view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		final int num = loadTable.getRecords().length;
		if(num<=0){
			MSGUtil.sayError("请至少选择一条调度单");
			return;
		}
		SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
			public void execute(Boolean value) {
                if (value != null && value) {
                	
            		lstrecord = loadTable.getSelectedRecord();
            		Record record = loadTable.getEditedRecord(loadTable.getRecordIndex(lstrecord));
            		HashMap<String, Object> listmap = new HashMap<String, Object>(); 
            		
            		if(record != null) {
            			if(!record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME) 
            					|| !record.getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.NO_DISPATCH_NAME)) {
            				SC.warn(Util.TI18N.LOAD_NO() + "当前状态不允许删除!");
            				return;
            			}
            			listmap.put("1", record.getAttribute("LOAD_NO"));
            			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
            			String json = Util.mapToJson(listmap);
            			Util.async.execProcedure(json, "SP_LOADNO_DELETE(?,?,?)", new AsyncCallback<String>() {
            				@SuppressWarnings("unchecked")
							@Override
            				public void onSuccess(String result) {
            					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
            						MSGUtil.sayInfo(result.substring(2));    
            						
            						//刷新调度单列表
            						if(num > 1) {
	            						ListGridRecord[] records = loadTable.getRecords();
										ArrayList list = new ArrayList(Arrays.asList(records));
										int pos = loadTable.getRecordIndex(loadTable.getSelectedRecord());
										list.remove(pos);
										loadTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
	            						loadTable.redraw();
            						}
            						else {
            							loadTable.invalidateCache();
            							Criteria criteria = loadTable.getCriteria();
            							if(criteria == null) {
            								criteria = new Criteria();
            							}
            							criteria.addCriteria("OP_FLAG","M");
            							criteria.addCriteria("STATUS_FROM", StaticRef.TRANS_CREATE);
            							criteria.addCriteria("STATUS_TO", StaticRef.TRANS_CREATE);
            							//fanglm 2011-3-15 只刷新出当前登录用户的调度单
            							criteria.addCriteria("EXEC_ORG_ID",LoginCache.getLoginUser().getDEFAULT_ORG_ID());
            							loadTable.fetchData(criteria);
            							loadTable.redraw();
            						}
            						if(loadTable.getRecords().length > 0) {
            							loadTable.selectRecord(0);
            						}
            						
            						//刷新待调订单
            						new QueryUnshpmTableAction(view.unshpmTable, view.sumForm, view.pageForm).doRefresh(true);
            						
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
