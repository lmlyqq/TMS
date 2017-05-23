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
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
/**
 * 加入作业单
 * @author yuanlei
 *
 */
public class AddShpmNoAction implements ClickHandler {

	private DispatchView view;
	private SGTable unshpmTable;
	private SGTable loadTable;
	private ListGridRecord[] newRecords;
	@SuppressWarnings("unchecked")
	private ArrayList cache_list;
	private ListGridRecord record;
	private int[] edit_rows = null;               //列表中所有被编辑过的行号
	private String PLATE_NO;
	private String DRIVER;
	private String VEHICLE_TYP_ID;
	private String MOBILE;
	
	public AddShpmNoAction(DispatchView form, SGTable p_unshpmTable, SGTable p_loadTable) {
		this.view = form;
		this.unshpmTable = p_unshpmTable;
		this.loadTable = p_loadTable;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) { 
		record = loadTable.getSelectedRecord();
		if(record != null) {
			if(!record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME) && 
					!record.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_PART_DEPART_NAME)) {
				SC.warn("调度单[" + record.getAttribute("LOAD_NO") + "]状态不允许添加作业单!");
				return;
			}
			/*if(!record.getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.NO_DISPATCH_NAME)) {
				SC.warn("调度单" + record.getAttribute("DISPATCH_STAT_NAME") + ",不允许添加作业单!");
				return;
			}*/
            doAdd(record.getAttribute("LOAD_NO"));
		}
		else {
			MSGUtil.sayWarning("未选择调度单!");
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doAdd(String load_no) {
		ListGridRecord[] records = unshpmTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		HashMap<String, String> veh_map = new HashMap<String, String>(); //车辆   
		HashMap<String, String> typ_map = new HashMap<String, String>(); //车辆类型 
		HashMap<String, String> driver_map = new HashMap<String, String>(); //司机   
		HashMap<String, String> mobile_map = new HashMap<String, String>(); //手机号 
		double sum_grossw = 0.00;
		
		ArrayList list = new ArrayList(Arrays.asList(unshpmTable.getRecords()));
		cache_list = new ArrayList<ListGridRecord>();
		int pos = 0;
		edit_rows = loadTable.getAllEditRows();   //获取所有修改过的记录行        wangjun
		if(edit_rows != null && edit_rows.length > 0){
			for(int y = 0; y < edit_rows.length; y++) {
				for(int i = 0; i < records.length; i++) {
					sum_grossw += Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("TOT_GROSS_W"),"0"));
					Map map = loadTable.getEditValues(edit_rows[y]);    //获取记录修改过的值
					Map selMap = unshpmTable.getEditValues(records[i]);
					record.getAttribute("PLATE_NO");
					if(ObjUtil.ifObjNull(selMap.get("PLATE_NO"),"").equals("")){
						PLATE_NO = ObjUtil.ifObjNull(map.get("PLATE_NO")," ").toString();
					}else{
						PLATE_NO = ObjUtil.ifObjNull(selMap.get("PLATE_NO")," ").toString();
					}
					
					if(ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID"),"").equals("")){
						VEHICLE_TYP_ID = ObjUtil.ifObjNull(map.get("VEHICLE_TYP_ID")," ").toString();
					}else{
						VEHICLE_TYP_ID = ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID")," ").toString();
					}
					
					if(ObjUtil.ifObjNull(selMap.get("DRIVER"),"").equals("")){
						DRIVER = ObjUtil.ifObjNull(map.get("DRIVER1")," ").toString();
					}else{
						DRIVER = ObjUtil.ifObjNull(selMap.get("DRIVER")," ").toString();
					}
					
					if(ObjUtil.ifObjNull(selMap.get("MOBILE"),"").equals("")){
						MOBILE = ObjUtil.ifObjNull(map.get("MOBILE1")," ").toString();
					}else{
						MOBILE = ObjUtil.ifObjNull(selMap.get("MOBILE")," ").toString();
					}
					order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
					shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
					veh_map.put(Integer.toString(i+1), PLATE_NO);
					typ_map.put(Integer.toString(i+1), VEHICLE_TYP_ID);
					driver_map.put(Integer.toString(i+1),DRIVER);
					mobile_map.put(Integer.toString(i+1), MOBILE);
					
	//				order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
	//				shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
	//				veh_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("PLATE_NO"), ObjUtil.ifObjNull(map.get("PLATE_NO")," ")).toString());
	//				typ_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID"), ObjUtil.ifObjNull(map.get("VEHICLE_TYP_ID")," ")).toString());
	//				driver_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("DRIVER"),ObjUtil.ifObjNull(map.get("DRIVER1")," ")).toString());
	//				mobile_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("MOBILE"), ObjUtil.ifObjNull(map.get("MOBILE1")," ")).toString());
					pos = unshpmTable.getRecordIndex(records[i]);
					((ListGridRecord)list.get(pos)).setAttribute("STATUS_NAME", StaticRef.SHPM_DIPATCH_NAME);
					cache_list.add(list.get(pos));
				}
			}
			double remain_gross_w = Double.parseDouble(ObjUtil.ifNull(loadTable.getSelectedRecord().getAttribute("REMAIN_GROSS_W"),"0"));
			if(sum_grossw > remain_gross_w && ObjUtil.isNotNull(PLATE_NO)) {
				MSGUtil.sayError("车辆余量不足!");
				return;
			}
		}else{
		
			for(int i = 0; i < records.length; i++) {
				sum_grossw += Double.parseDouble(ObjUtil.ifNull(records[i].getAttribute("TOT_GROSS_W"),"0"));
				Map selMap = unshpmTable.getEditValues(records[i]);
				order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
				shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
				
				if(ObjUtil.ifObjNull(selMap.get("PLATE_NO"),"").equals("")){
					veh_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(record.getAttribute("PLATE_NO")," ").toString());
				}else{
					veh_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("PLATE_NO"), ObjUtil.ifObjNull(record.getAttribute("PLATE_NO")," ")).toString());
				}
				
				if(ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID"),"").equals("")){
					typ_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(record.getAttribute("VEHICLE_TYP_ID")," ").toString());

				}else{
					typ_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("VEHICLE_TYP_ID"), ObjUtil.ifObjNull(record.getAttribute("VEHICLE_TYP_ID")," ")).toString());
				}
				
				if(ObjUtil.ifObjNull(selMap.get("DRIVER"),"").equals("")){
					driver_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(record.getAttribute("DRIVER1")," ").toString());
				}else{
					driver_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("DRIVER"), ObjUtil.ifObjNull(record.getAttribute("DRIVER1")," ")).toString());
				}
				
				if(ObjUtil.ifObjNull(selMap.get("MOBILE"),"").equals("")){
					mobile_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(record.getAttribute("MOBILE1")," ").toString());
				}else{
					mobile_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(selMap.get("MOBILE"), ObjUtil.ifObjNull(record.getAttribute("MOBILE1")," ")).toString());
				}
				
				pos = unshpmTable.getRecordIndex(records[i]);
				((ListGridRecord)list.get(pos)).setAttribute("STATUS_NAME", StaticRef.SHPM_DIPATCH_NAME);
				cache_list.add(list.get(pos));
			}
			double remain_gross_w = Double.parseDouble(ObjUtil.ifNull(loadTable.getSelectedRecord().getAttribute("REMAIN_GROSS_W"),"0"));
			if(sum_grossw > remain_gross_w && ObjUtil.isNotNull(PLATE_NO)) {
				MSGUtil.sayError("车辆余量不足!");
				return;
			}
		}
		listmap.put("1", load_no);
		listmap.put("2", order_map);
		listmap.put("3", shpm_map);
		listmap.put("4", veh_map);
		listmap.put("5", typ_map);
		listmap.put("6", driver_map);
		listmap.put("7", mobile_map);
		listmap.put("8", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		newRecords = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
		list.removeAll(Arrays.asList(newRecords));
		newRecords = (ListGridRecord[])list.toArray(new ListGridRecord[list.size()]);
		
		Util.async.execProcedure(json, "SP_LOAD_ADDSHPM(?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					//刷新待调订单列表
					unshpmTable.setRecords(newRecords);
					unshpmTable.redraw();
					
					//刷新已调订单列表	
					if(view.shpmTable != null) {
						if(view.shpmTable.getRecords().length > 0) {
							cache_list.addAll(Arrays.asList(view.shpmTable.getRecords()));
						}
						view.shpmTable.setRecords((ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]));
						view.shpmTable.redraw();
					}
					
					//重新计算调度单数量、体积、毛重、净重
					if(cache_list != null && cache_list.size() > 0) {
						double qnty = 0.00, vol = 0.00, g_w = 0.00, worth = 0.00,qnty_each=0.00; 
						ListGridRecord tmp_rec = null;
						for(int i = 0; i < cache_list.size(); i++) {
							tmp_rec = (ListGridRecord)cache_list.get(i);
							qnty += Double.parseDouble(ObjUtil.ifObjNull(tmp_rec.getAttribute("TOT_QNTY"),"0").toString());
							qnty_each += Double.parseDouble(ObjUtil.ifObjNull(tmp_rec.getAttribute("TOT_QNTY_EACH"),"0").toString());
							vol += Double.parseDouble(ObjUtil.ifObjNull(tmp_rec.getAttribute("TOT_VOL"),"0").toString());
							g_w += Double.parseDouble(ObjUtil.ifObjNull(tmp_rec.getAttribute("TOT_GROSS_W"),"0").toString());
							worth += Double.parseDouble(ObjUtil.ifObjNull(tmp_rec.getAttribute("TOT_WORTH"),"0").toString());
						}
						ListGridRecord record = loadTable.getSelectedRecord();
						if(view.shpmTable == null) {							
							qnty += Double.parseDouble(ObjUtil.ifObjNull(record.getAttribute("TOT_QNTY"),"0").toString());
							qnty_each += Double.parseDouble(ObjUtil.ifObjNull(record.getAttribute("TOT_QNTY_EACH"),"0").toString());
							vol += Double.parseDouble(ObjUtil.ifObjNull(record.getAttribute("TOT_VOL"),"0").toString());
							g_w += Double.parseDouble(ObjUtil.ifObjNull(record.getAttribute("TOT_GROSS_W"),"0").toString());
							worth += Double.parseDouble(ObjUtil.ifObjNull(record.getAttribute("TOT_WORTH"),"0").toString());
						}
						record.setAttribute("TOT_QNTY", Double.toString(qnty));
						record.setAttribute("TOT_QNTY_EACH", Double.toString(qnty_each));
						record.setAttribute("TOT_VOL", Double.toString(vol));
						record.setAttribute("TOT_GROSS_W", Double.toString(g_w));
						record.setAttribute("TOT_WORTH", Double.toString(worth));
						
						String[] remain = result.substring(2).split(",");
						record.setAttribute("REMAIN_GROSS_W", remain[0]);
						//record.setAttribute("REMAIN_VOL", remain[1]);
						
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
