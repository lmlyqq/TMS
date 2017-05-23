package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.VehicleDispatchView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 配车确认
 * @author yuanlei
 *
 */
public class LoadDispatchAuditNewAction implements com.smartgwt.client.widgets.menu.events.ClickHandler {

	private SGTable loadTable;
	private Record curRecord;
	private HashMap<String, String> check_map;
	
	private HashMap<String, String> map;
	private HashMap<String, String> ck_map;
	private VehicleDispatchView view;
	private int[] edit_rows = null;    
	//private ArrayList<String> logList;   //日志信息
	private String plate_no;
//	private ListGridRecord[] records;
	
	private int pos;
	
	public LoadDispatchAuditNewAction(SGTable p_loadTable) {
		this.loadTable = p_loadTable;
	}
	
	public LoadDispatchAuditNewAction(SGTable p_loadTable, HashMap<String, String> p_map, VehicleDispatchView p_view) {
		this.check_map = p_map;
		this.loadTable = p_loadTable;
		this.view = p_view;
	}
	
	
	private void doIt(String json){
		
		Util.async.execProcedure(json, "SP_LOADNO_DISPATCHAUDIT(?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					//刷新调度单
					ListGridRecord[] records = loadTable.getSelection();
					for(int i = 0; i < records.length; i++) {
						records[i].setAttribute("DISPATCH_STAT_NAME", StaticRef.AUDITED_NAME);
						loadTable.updateData(records[i]);
					}
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
	
	private void checkValue(){
		HashMap<String, String> valMap = map;
		Object[] key = check_map.keySet().toArray();
		for(int i = 0;i<check_map.size();i++){
			String[] arrayKey = key[i].toString().split(",");
			for(int j = 0;j<arrayKey.length;j++){
				if(!arrayKey[j].equals("TABLE") && valMap.get(arrayKey[j]) == null){
					valMap.put(arrayKey[j].toString(), ck_map.get(arrayKey[j]));
				}
			}
		}
		ck_map = valMap;
	}
	
	private void doOperation() {                                 //--修改 
		ArrayList<String> sqlList = new ArrayList<String>();
		if(map != null) {
			//map.put("TABLE", loadTable.getDataSource().getAttribute("tableName"));
			map.put("TABLE", loadTable.getDataSource().getTableName());
			//map.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(map);
			sqlList.add(json);
		}
		doUpdate(sqlList);
	}
	
	private void doUpdate(ArrayList<String> sqlList) {
		Util.async.doUpdate(null, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					
					HashMap<String, Object> vMap = new HashMap<String, Object>();
					vMap.put("1", curRecord.getAttribute("LOAD_NO"));
					vMap.put("2", curRecord.getAttribute("VEHICLE_TYP_ID"));
					vMap.put("3", plate_no);
					vMap.put("4", curRecord.getAttribute("PLATE_NO"));
					vMap.put("5", curRecord.getAttribute("DRIVER1"));
					vMap.put("6", curRecord.getAttribute("MOBILE1"));
					vMap.put("7", curRecord.getAttribute("SUPLR_ID"));
					vMap.put("8", curRecord.getAttribute("DRIVER_ID"));
					vMap.put("9", LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(vMap);
					Util.async.execProcedure(json, "SP_LOADNO_UPDATE(?,?,?,?,?,?,?,?,?,?)", new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){ 
								loadTable.getEditValuesMap().remove(pos);//删除编辑过的值
								String[] remain = result.substring(2).split(",");
								
								ListGridRecord rec = loadTable.getSelectedRecord();
								if(remain != null && remain.length > 1) {
									rec.setAttribute("REMAIN_GROSS_W", remain[0]);
									if(remain.length > 1) {
										rec.setAttribute("REMAIN_VOL", remain[1]);
									}
									rec.setAttribute("PLATE_NO",curRecord.getAttribute("PLATE_NO"));
									rec.setAttribute("VEHICLE_TYP_ID",curRecord.getAttribute("VEHICLE_TYP_ID"));
									rec.setAttribute("DRIVER1",curRecord.getAttribute("DRIVER1"));
									rec.setAttribute("MOBILE1",curRecord.getAttribute("MOBILE1"));
								}
								
								//审核
								doConfirm();
								
								view.initSaveBtn();
								loadTable.OP_FLAG = "M";
								//loadTable.selectRecord(curRecord);
								//loadTable.updateData(curRecord);
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
								loadTable.fetchData(criteria);
//								loadTable.redraw();
							}
							else {
								MSGUtil.sayError(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
		
       
		
		//fanglm 2011-4-28 调用存储过程，插入驾驶员信息到人员表中
//		if(ObjUtil.isNotNull(map.get("MOBILE1")) && ObjUtil.isNotNull(map.get("EXEC_ORG_ID"))){
//			HashMap<String, Object> vMap = new HashMap<String, Object>();
//			vMap.put("1", ObjUtil.ifObjNull(map.get("MOBILE1"),""));
//			vMap.put("2", ObjUtil.ifObjNull(map.get("DRIVER1"),curRecord.getAttribute("DRIVER1")));
//			vMap.put("3", ObjUtil.ifObjNull(map.get("EXEC_ORG_ID"),""));
//			vMap.put("4", "CLIENT");
//			String json = Util.mapToJson(vMap);
//			Util.async.execProcedure(json, "SP_CHECK_MOBILE(?,?,?,?,?)", new AsyncCallback<String>() {
//				
//				@Override
//				public void onSuccess(String result) {
//					
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					
//				}
//			});
//		}
	}
	
	private void doConfirm(){
		ListGridRecord[] records = loadTable.getSelection();
		//String load_no = "";
		if(records != null && records.length > 0) {
			HashMap<String, Object> listmap = new HashMap<String, Object>(); 
			HashMap<String, String> load_map = new HashMap<String, String>(); //调度单   
			//StringBuffer hasDis = new StringBuffer();
			for(int i = 0; i < records.length; i++) {
				/*if(!records[i].getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.DISPATCHED_NAME)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]配车状态为[" + records[i].getAttribute("DISPATCH_STAT_NAME") + "],不能执行配车审核!");
					return;
				}*/
				if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
					SC.warn("调度单[" + records[i].getAttribute("LOAD_NO") + "]非已创建状态,不能执行配车审核!");
					return;
				}
				/*if(ObjUtil.ifObjNull(records[i].getAttribute("BTCH_NUM"),"").toString().length() > 0) {
					load_no = records[i].getAttribute("LOAD_NO");
					hasDis.append("调度单[" + records[i].getAttribute("LOAD_NO") + "]下包含指定配车信息,是否确认审核通过？\n");
				}*/
				load_map.put(Integer.toString(i+1), records[i].getAttribute("LOAD_NO"));
			}
			listmap.put("1", load_map);
			listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
			final String json = Util.mapToJson(listmap);
			doIt(json);
		}
		else {
			SC.warn("未选择调度单!");
			return;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(MenuItemClickEvent event) {
		plate_no = loadTable.getSelectedRecord().getAttribute("PLATE_NO");
		edit_rows = loadTable.getAllEditRows();   //获取所有修改过的记录行 wangjun
		if(edit_rows != null && edit_rows.length > 0) {
 			pos = loadTable.getRecordIndex(loadTable.getSelectedRecord());
			Map<String, String> value_map = loadTable.getField("SUPLR_ID").getAttributeAsMap("valueMap");
			if(pos >= 0) {
				curRecord = loadTable.getEditedRecord(pos);  //获取所有修改的记录
//				map = (HashMap<String, String>)loadTable.getEditValues(pos);    //获取记录修改过的值
				map = loadTable.getEditValuesMap().get(pos);
				map.put("EXEC_ORG_ID", curRecord.getAttributeAsString("EXEC_ORG_ID"));
				if(map.keySet().size() > 0) {
					if(ObjUtil.isNotNull(map.get("SUPLR_ID"))) {
						map.put("SUPLR_NAME", value_map.get(map.get("SUPLR_ID")));	
					}
					ck_map = Util.putRecordToModel(loadTable.getRecord(pos));
					checkValue();
					//logList.add(Util.getUpdateLog(ck_map, map, loadTable.getDataSource().getTableName()));  //拼装的描述内容
					if(map != null) {
						if(check_map != null) {
						ArrayList<Object> obj = Util.getCheckResult(ck_map, check_map);
							if(obj != null && obj.size() > 1) {
								String result = obj.get(0).toString();
								if(result.equals(StaticRef.SUCCESS_CODE)) {
									doOperation();
								}
								else {
									MSGUtil.sayError(obj.get(1).toString());
								}
							}
						}
						else {
							doOperation();
						}
					}
				}
				else {
					return;
				}
			}
			else {
				SC.warn("未选中调度单!");
				return;
			}
		}else if(ObjUtil.isNotNull(loadTable.getSelectedRecord()) && ObjUtil.isNotNull(loadTable.getSelectedRecord().getAttribute("PLATE_NO"))){
			doConfirm();
		}else {
			return;
		}
	}
}
