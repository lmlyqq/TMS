package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 剔除部分发运状态调度单下勾选的未发运作业单
 * @author yuanlei
 *
 */
public class DelUnsendShpmAction implements ClickHandler {

	private SGTable shpmTable;
	private SGTable unshpmTable;
	private SGTable loadTable;
	private ListGridRecord[] newRecords;
	@SuppressWarnings("unchecked")
	private ArrayList cache_list;
	public DelUnsendShpmAction(SGTable p_shpmTable, SGTable p_unshpmTable, SGTable p_loadTable) {
		this.shpmTable = p_shpmTable;
		this.unshpmTable = p_unshpmTable;
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		ListGridRecord[] records = shpmTable.getSelection();
		if(records != null && records.length > 0) {
			SC.confirm("确定剔除勾选的作业单?", new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doRemove();
                    }
                }
            });
		}
		else {
			MSGUtil.sayWarning("未选择作业单!");
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doRemove() {
		ListGridRecord rec = loadTable.getSelectedRecord();
		if(!rec.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_PART_DEPART_NAME)) {
			SC.warn("只有部分发运状态的调度单才可以执行操作!");
			return;
		}
		ListGridRecord[] records = shpmTable.getSelection();
		
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		
		ArrayList list = new ArrayList(Arrays.asList(shpmTable.getRecords()));
		cache_list = new ArrayList<ListGridRecord>();
		int pos = 0;
		
		for(int i = 0; i < records.length; i++) {
			if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.SHPM_DIPATCH_NAME)) {
				SC.warn("作业单[" + records[i].getAttribute("SHPM_NO") +  "]状态不允许剔除!");
				return;
			}
			order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
			shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
			pos = shpmTable.getRecordIndex(records[i]);
			cache_list.add(list.get(pos));
		}
		listmap.put("1", rec.getAttribute("LOAD_NO"));
		listmap.put("2", order_map);
		listmap.put("3", shpm_map);
		listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		newRecords = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
		list.removeAll(Arrays.asList(newRecords));
		newRecords = (ListGridRecord[])list.toArray(new ListGridRecord[list.size()]);
		
		cache_list.addAll(Arrays.asList(unshpmTable.getRecords()));
		Util.async.execProcedure(json, "SP_PARTLOAD_REMOVESHPM(?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					//刷新待调订单列表
					unshpmTable.setRecords((ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]));
					unshpmTable.redraw();
					
					//刷新已调订单列表	
					if(newRecords != null && newRecords.length > 0) {
						for(int i = 0; i < newRecords.length; i++) {
							Map selMap = shpmTable.getEditValues(newRecords[i]);
							if(selMap != null && ObjUtil.isNotNull(selMap.get("DEPART_TIME"))) {
								newRecords[i].setAttribute("DEPART_TIME", selMap.get("DEPART_TIME"));
							}
						}
						shpmTable.setRecords(newRecords);
						shpmTable.redraw();
					}
					
					//重新调度单数量、体积、毛重、净重
					if(newRecords != null) {
						double qnty = 0.00, vol = 0.00, g_w = 0.00, worth = 0.00; 
						for(int i = 0; i < newRecords.length; i++) {
							qnty += Double.parseDouble(ObjUtil.ifObjNull(newRecords[i].getAttribute("TOT_QNTY"),"0").toString());
							vol += Double.parseDouble(ObjUtil.ifObjNull(newRecords[i].getAttribute("TOT_VOL"),"0").toString());
							g_w += Double.parseDouble(ObjUtil.ifObjNull(newRecords[i].getAttribute("TOT_GROSS_W"),"0").toString());
							worth += Double.parseDouble(ObjUtil.ifObjNull(newRecords[i].getAttribute("TOT_WORTH"),"0").toString());
						}
						ListGridRecord record = loadTable.getSelectedRecord();
						record.setAttribute("TOT_QNTY", Double.toString(qnty));
						record.setAttribute("TOT_VOL", Double.toString(vol));
						record.setAttribute("TOT_GROSS_W", Double.toString(g_w));
						record.setAttribute("TOT_WORTH", Double.toString(worth));
						if(result.length() > 2) {
							String[] arys = result.substring(2).split(",");
							record.setAttribute("STATUS_NAME", arys[0]);
							record.setAttribute("DEPART_TIME", arys[1]);
						}
						loadTable.updateData(loadTable.getSelectedRecord());
						loadTable.redraw();						
					}
					
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
