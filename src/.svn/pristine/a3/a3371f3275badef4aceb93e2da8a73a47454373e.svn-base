package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.VehicleDispatchView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 生成调度单
 * @author yuanlei
 *
 */
public class MakeLoadNoNewAction implements ClickHandler {

	private SGTable unshpmTable;
	private SGTable loadTable;
	private SGTable carTable;
	private ListGridRecord[] newRecords;
	private VehicleDispatchView view;
	private boolean hasQuery = true;
	public Record clickrecord;
	//private Map<String, String> editMap;
	public int hRow=0;
	
	public MakeLoadNoNewAction(SGTable p_unshpmTable, SGTable p_loadTable,SGTable p_carTable, VehicleDispatchView p_view) {
		this.unshpmTable = p_unshpmTable;
		this.loadTable = p_loadTable;
		this.carTable=p_carTable;
		this.view = p_view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		
		clickrecord = unshpmTable.getSelectedRecord();
		//editMap = unshpmTable.getEditValues(clickrecord);
		
		
		
//		String get=LoginCache.getSysParam().get("VEHICLE_POSITIONING").getVALUE_STRING();
//		if (get.equals("Y")){
//			Object PLATE_NO = ObjUtil.ifObjNull(editMap.get("PLATE_NO"),clickrecord.getAttribute("PLATE_NO"));
//			Object MOBILE = ObjUtil.ifObjNull(editMap.get("MOBILE"),clickrecord.getAttribute("MOBILE"));
//			Object DRIVER = ObjUtil.ifObjNull(editMap.get("DRIVER"),clickrecord.getAttribute("DRIVER"));
//			if(ObjUtil.isNotNull(PLATE_NO)){
//				if(ObjUtil.isNotNull(DRIVER)){
//					if(!ObjUtil.isNotNull(MOBILE)){
//						MSGUtil.sayError("请填写手机号！");
//						return;
//					}
//				}else{
//					MSGUtil.sayError("请填写司机！");
//					return;
//				}
//			}
//		}
		
		//Object PLATE_NO = ObjUtil.ifObjNull(editMap.get("PLATE_NO"),clickrecord.getAttribute("PLATE_NO"));
		//Object MOBILE = ObjUtil.ifObjNull(editMap.get("MOBILE"),clickrecord.getAttribute("MOBILE"));
		/*if (get.equals("Y")){	
			if(ObjUtil.isNotNull(PLATE_NO)){
				if(!ObjUtil.isNotNull(MOBILE)){
					MSGUtil.sayError("请填写手机号！");
					return;
				}
			}
		}*/	
		
		ListGridRecord[] records = unshpmTable.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
		HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
		HashMap<String, String> veh_map = new HashMap<String, String>(); //车辆   
		HashMap<String, String> typ_map = new HashMap<String, String>(); //车辆类型 
		HashMap<String, String> driver_map = new HashMap<String, String>(); //司机   
		HashMap<String, String> mobile_map = new HashMap<String, String>(); //手机号   
		/*ListGridField suplr_id = unshpmTable.getField("SUPLR_ID");
		Map<String, String> value_map = null;
		if(suplr_id != null) {
			value_map = suplr_id.getAttributeAsMap("valueMap");
		}*/
		ListGridRecord carRecord=carTable.getSelectedRecord();
		
		if(records != null && records.length > 0) {
			
			if(null!=carRecord){
			   
			ArrayList list = new ArrayList(Arrays.asList(unshpmTable.getRecords()));
			ArrayList cache_list = new ArrayList<ListGridRecord>();
			int pos = 0;
			//String parent_shpm_no = "";
			for(int i = 0; i < records.length; i++) {
				//Map selMap = unshpmTable.getEditValues(records[i]);
				order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
				shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
				veh_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(carRecord.getAttribute("PLATE_NO")," ").toString());
				typ_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(carRecord.getAttribute("VEHICLE_TYP_ID"), " ").toString());
				driver_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(carRecord.getAttribute("DRIVER1_NAME"), " ").toString());
				mobile_map.put(Integer.toString(i+1),ObjUtil.ifObjNull(carRecord.getAttribute("MOBILE1"), " ").toString());
					
				pos = unshpmTable.getRecordIndex(records[i]);
				cache_list.add(list.get(pos));								
			}

			listmap.put("1", order_map);
			listmap.put("2", shpm_map);			
			listmap.put("3", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
			listmap.put("4", carRecord.getAttribute("SUPLR_ID"));
			listmap.put("5", carRecord.getAttribute("SUPLR_ID_NAME"));
			listmap.put("6", veh_map);
			listmap.put("7", typ_map);
			listmap.put("8", driver_map);
			listmap.put("9", mobile_map);
			listmap.put("10", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(listmap);
			newRecords = (ListGridRecord[])cache_list.toArray(new ListGridRecord[cache_list.size()]);
			list.removeAll(Arrays.asList(newRecords));
			newRecords = (ListGridRecord[])list.toArray(new ListGridRecord[list.size()]);
			Util.async.execProcedure(json, "SP_LOAD_CREATE(?,?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
				@Override
				public void onSuccess(String result) {
					if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.sayInfo(result.substring(2));
						
						//刷新待调订单列表	
						unshpmTable.setRecords(newRecords);
						unshpmTable.redraw();
						unshpmTable.selectRecord(0);
						
						//获取待调订单汇总信息
						Criteria crit = unshpmTable.getCriteria();
						if(crit == null) {
							crit = new Criteria();
						}
						crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
						crit.addCriteria("STATUS", StaticRef.SHPM_CONFIRM);
						crit.addCriteria("EMPTY_FLAG","Y");
								
						//调度单列表增加记录
						if(loadTable.getRecords().length < 1) {
							hasQuery = false;
						}
						loadTable.discardAllEdits();
						loadTable.invalidateCache();
						loadTable.refreshFields();
						Criteria criteria = loadTable.getCriteria();
						if(criteria == null) {
							criteria = new Criteria();
							criteria.addCriteria("STATUS_FROM", StaticRef.TRANS_CREATE);
							criteria.addCriteria("STATUS_TO", StaticRef.TRANS_CREATE);
							//criteria.addCriteria("SUPLR_NAME",record.getAttribute("SUPLR_NAME"));
							//fanglm 2011-3-15 刷新当前组织机构下的调度单
							criteria.addCriteria("EXEC_ORG_ID",LoginCache.getLoginUser().getDEFAULT_ORG_ID());
						}
						criteria.addCriteria("OP_FLAG","M");
						loadTable.fetchData(criteria, new DSCallback() {

							@Override
							public void execute(DSResponse response,
									Object rawData, DSRequest request) {
								loadTable.setSelectOnEdit(true);
								//loadTable.selectRecord(loadTable.getRecord(0));
								
								//调度单未执行过查询时，获取数据库设置分页页码信息
								if(!hasQuery) {
									if(view.loadPageForm != null) {
										view.loadPageForm.getField("CUR_PAGE").setValue("1");
										LoginCache.setPageResult(loadTable, view.loadPageForm.getField("TOTAL_COUNT"), view.loadPageForm.getField("SUM_PAGE"));
									}
									loadTable.setSelectOnEdit(true);
									if(loadTable.getRecords().length > 0) {
										loadTable.selectRecord(loadTable.getRecord(0));
									}
								}
								else {
									loadTable.selectRecord(loadTable.getRecord(0));
								}
								
								view.enableOrDisables(view.save_map, false);
								ListGridRecord selRecord = loadTable.getRecord(0);
								if(selRecord != null) {
									if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
										view.enableOrDisables(view.del_map, true);
										view.setSendBtnStatus(true);
									}
									else if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_DEPART_NAME)){
										view.enableOrDisables(view.del_map, false);
										view.setSendBtnStatus(false);
									}
									else {
										view.enableOrDisables(view.del_map, false);
										view.setConfirmBtnStatus(false);
									}
								}
							}
							
						});
						
						int[] rows = loadTable.getAllEditRows();
						if(rows != null && rows.length > 0) {
							for(int i = 0; i < rows.length; i++) {
								loadTable.clearEditValue(rows[i], "DEPART_TIME");
								/*if(i == 0) {
									loadTable.clearEditValue(rows[i], "PLATE_NO");
									loadTable.clearEditValue(rows[i], "VEHICLE_TYP_ID");
									loadTable.clearEditValue(rows[i], "DRIVER1");
									loadTable.clearEditValue(rows[i], "MOBILE1");
								}*/
							}
						}
					}
					else{
						MSGUtil.sayError(result);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
			});
		 }else{
			 MSGUtil.sayWarning("未选择车辆!");
			 return;
		 }
		}
		else {
			MSGUtil.sayWarning("未选择作业单!");
			return;
		}
	}

}
