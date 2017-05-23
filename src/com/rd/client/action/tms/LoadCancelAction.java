package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.tms.LoadJobView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class LoadCancelAction implements ClickHandler {
	private LoadJobView view;
//	private ArrayList<ListGridRecord> cache_list;
	private ArrayList<ListGridRecord> cache_list2;
	private ArrayList<ListGridRecord> cache_list3;
//	private ListGridRecord[] newRecords;
	private ListGridRecord[] newRecords2;
	private ListGridRecord item;
	
	public LoadCancelAction(LoadJobView view){
		this.view = view;
	}
	
	
	@Override
	public void onClick(ClickEvent event) {
		String proName = "CANCEL_LOAD_PRO(?,?,?,?,?,?)";
		final ListGridRecord[] record = view.loadLeftTable.getSelection();
		
		if(ObjUtil.isNotNull(record)){
			String load_status = record[0].getAttribute("LOAD_STATUS");
			String plate_no = record[0].getAttribute("PLATE_NO");
			String load_whse = record[0].getAttribute("LOAD_WHSE");
//			String login_id = LoginCache.getLoginUser().getUSER_ID();
			String Load_no = record[0].getAttribute("LOAD_NO");
			String unload_name = record[0].getAttribute("UNLOAD_NAME");//wangjun 2011-4-27
//			String SHPM_NO = record[0].getAttribute("SHPM_NO");//wangjun 2011-4-27

			
			ArrayList<String> list = new ArrayList<String>();
			list.add(load_status);
			list.add(plate_no);
			list.add(load_whse);
			list.add(Load_no);
			list.add(unload_name);
//			list.add(SHPM_NO);
			
			Util.async.execProcedure(list, proName, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
				    view.loadRightTable.invalidateCache();
				    Criteria crit1 = new Criteria();
				    crit1.addCriteria("OP_FLAG","M");
				    crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
				    crit1.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
				    view.loadRightTable.fetchData(crit1,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
//					        cache_list = new ArrayList<ListGridRecord>();
							cache_list2 = new ArrayList<ListGridRecord>();
							cache_list3 = new ArrayList<ListGridRecord>();
//							cache_list.addAll(Arrays.asList(view.loadRightTable.getRecords()));
//							String queue = cache_list.get(cache_list.size()-1).getAttribute("QUEUE_SEQ");
//							int queue_ = Integer.parseInt(queue)+1;
							cache_list2.addAll(Arrays.asList(view.loadLeftTable.getRecords()));
//					
							for (int i = 0; i < cache_list2.size(); i++) {
								item = cache_list2.get(i);
								if(!record[0].getAttribute("UNLOAD_NAME").equals(cache_list2.get(i).getAttribute("UNLOAD_NAME"))
										|| !record[0].getAttribute("LOAD_NO").equals(cache_list2.get(i).getAttribute("LOAD_NO"))){
									cache_list3.add(item);	
								}
							}
//							cache_list2.remove(record);
//							record.setAttribute("QUEUE_SEQ", queue_);
//							cache_list.add(record);
//							newRecords = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
							newRecords2 = (ListGridRecord[])cache_list3.toArray(new ListGridRecord[cache_list3.size()]);
							view.loadLeftTable.setData(new RecordList());
//							view.loadRightTable.setData(newRecords);
							view.loadLeftTable.setData(newRecords2);
//							view.loadRightTable.redraw();
							view.loadLeftTable.redraw();
							
							view.loadRightTable.selectRecord(view.loadRightTable.getRecord(0));
							view.staLoadButton.enable();
							view.finLoadButton.disable();
							view.cancelButton.disable();
							
						}
					});
				    
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		} else {
			MSGUtil.sayWarning("请选择提货车辆！");
		}
	}

}
