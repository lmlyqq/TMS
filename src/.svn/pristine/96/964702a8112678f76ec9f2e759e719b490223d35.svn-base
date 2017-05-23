package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 移除作业单
 * @author yuanlei
 *
 */
public class RemoveShpmNoAction implements ClickHandler {

	private SGTable shpmTable;
	private SGTable unshpmTable;
	private SGTable loadTable;
	private ListGridRecord[] newRecords;
	private DynamicForm pageForm;
	private Criteria crit;
	
	@SuppressWarnings("unchecked")
	private ArrayList cache_list;
	public RemoveShpmNoAction(SGTable p_shpmTable, SGTable p_unshpmTable, SGTable p_loadTable) {
		this.shpmTable = p_shpmTable;
		this.unshpmTable = p_unshpmTable;
		this.loadTable = p_loadTable;
	}
	
	public RemoveShpmNoAction(SGTable p_shpmTable, SGTable p_unshpmTable, SGTable p_loadTable,DynamicForm pageForm) {
		this.shpmTable = p_shpmTable;
		this.unshpmTable = p_unshpmTable;
		this.loadTable = p_loadTable;
		this.pageForm = pageForm;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		ListGridRecord[] records = shpmTable.getSelection();
		if(records != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
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
		ListGridRecord[] records = shpmTable.getSelection();
		int ALL_ROW = shpmTable.getRecords().length;
		
		if(records.length == ALL_ROW){
			SC.warn("操作失败，移除作业单将造成调拨单无数据！");
			return;
		}
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		
		ArrayList list = new ArrayList(Arrays.asList(shpmTable.getRecords()));
		cache_list = new ArrayList<ListGridRecord>();
		int pos = 0;
		/*if(!loadTable.getSelectedRecord().getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.NO_DISPATCH_NAME)) {
			SC.warn("调度单" + loadTable.getSelectedRecord().getAttribute("DISPATCH_STAT_NAME") + ",不允许剔除作业单!");
			return;
		}*/
		
		for(int i = 0; i < records.length; i++) {
			if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.SHPM_DIPATCH_NAME)) {
				SC.warn("作业单[" + records[i].getAttribute("SHPM_NO") +  "]状态不允许剔除!");
				return;
			}
			order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
			shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
			//fanglm 2011-3-15 剔除作业单时删除车牌号等信息
			pos = shpmTable.getRecordIndex(records[i]);
//			records[i].setAttribute("PLATE_NO", "");
			cache_list.add(list.get(pos));
		}
		listmap.put("1", loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
		listmap.put("2", order_map);
		listmap.put("3", shpm_map);
		listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		newRecords = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
		list.removeAll(Arrays.asList(newRecords));
		newRecords = (ListGridRecord[])list.toArray(new ListGridRecord[list.size()]);
		
		cache_list.addAll(Arrays.asList(unshpmTable.getRecords()));
		Util.async.execProcedure(json, "SP_LOAD_REMOVESHPM(?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					//刷新待调订单列表   wangjun 2010-7-12
					//unshpmTable.setRecords((ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]));
					ListGridRecord[] newRecords2 = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
					for(int i = 0; i < newRecords2.length; i++) {
						newRecords2[i].setAttribute("PLATE_NO", " ");
						newRecords2[i].setAttribute("VEHICLE_TYP_ID", " ");
						newRecords2[i].setAttribute("STATUS_NAME", StaticRef.SHPM_CONFIRM_NAME);
						newRecords2[i].setAttribute("DRIVER", " ");
						newRecords2[i].setAttribute("MOBILE", " ");
					}
					
//					unshpmTable.setRecords(newRecords2);
//					unshpmTable.redraw();
					unshpmTable.invalidateCache();
					crit = unshpmTable.getCriteria();
					if(crit == null) {
						crit = new Criteria();
					}
					crit.addCriteria("OP_FLAG","M");
					crit.addCriteria("EMPTY_FLAG","Y");
					crit.addCriteria("STATUS","20");
					unshpmTable.fetchData(crit, new DSCallback() {

		    			@Override
		    			public void execute(DSResponse response, Object rawData,
		    					DSRequest request) {
		    				if(pageForm != null) {
		    					pageForm.getField("CUR_PAGE").setValue("1");
		    					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
		    					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
		    					String sqlwhere = Cookies.getCookie("SQLWHERE");
		    					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
		    						unshpmTable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
		    					}
		    				}
		    				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crit.getValues();
		    				if(map.get("criteria") != null) {
		    					map.remove("criteria");
		    				}
		    				if(map.get("_constructor") != null) {
		    					map.remove("_constructor");
		    				}
		    				if(map.get("C_ORG_FLAG") != null) {
		    					Object obj = map.get("C_ORG_FLAG");
		    					Boolean c_org_flag = (Boolean)obj;
		    					map.put("C_ORG_FLAG",c_org_flag.toString());
		    				}			
		    			
		    			}
		    			
		    		});
					
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
						if(result.length() > 3) {
							String[] arys = result.substring(2).split(",");
							record.setAttribute("REMAIN_GROSS_W", arys[0]);
							record.setAttribute("REMAIN_VOL", arys[1]);
							if(arys.length > 2) {
								record.setAttribute("STATUS_NAME", arys[2]);
								record.setAttribute("DEPART_TIME", arys[3]);
							}
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
