package com.rd.client.action.tms.dispatch;

import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
//import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.util.ObjUtil;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 保存调度单
 * @author yuanlei
 *
 */
public class SaveLoadNoAction implements ClickHandler {

	private SGTable loadTable;
	private Record curRecord;
	private HashMap<String, String> check_map;
	
	private HashMap<String, String> map;
	private HashMap<String, String> ck_map;
	private DispatchView view;
	private int[] edit_rows = null;    
	//private ArrayList<String> logList;   //日志信息
	private String plate_no;
	
	private int pos;
	public SaveLoadNoAction(SGTable p_loadTable, HashMap<String, String> p_map, DispatchView p_view) {
		this.check_map = p_map;
		this.loadTable = p_loadTable;
		this.view = p_view;
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
	
	//@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		plate_no = view.plate_no;
	edit_rows = loadTable.getAllEditRows();   //获取所有修改过的记录行 wangjun
	if(edit_rows != null && edit_rows.length > 0) {
 			pos = loadTable.getRecordIndex(loadTable.getSelectedRecord());
			//Map<String, String> value_map = loadTable.getField("SUPLR_ID").getAttributeAsMap("valueMap");
			if(pos >= 0) {
				curRecord = loadTable.getEditedRecord(pos);  //获取所有修改的记录
//				map = (HashMap<String, String>)loadTable.getEditValues(pos);    //获取记录修改过的值
				map = loadTable.getEditValuesMap().get(pos);
				map.put("EXEC_ORG_ID", curRecord.getAttributeAsString("EXEC_ORG_ID"));
				if(map.keySet().size() > 0) {
					//if(ObjUtil.isNotNull(map.get("SUPLR_ID"))) {
					//	map.put("SUPLR_NAME", value_map.get(map.get("SUPLR_ID")));	
					//}
					
					
					
					ck_map = Util.putRecordToModel(loadTable.getRecord(pos));
					checkValue();
	
					//logList.add(Util.getUpdateLog(ck_map, map, loadTable.getDataSource().getTableName()));  //拼装的描述内容
					
					if(map != null) {
						if(map.get("MOBILE1")==null||map.get("MOBILE1").length()!=11){
							SC.say("手机号码必须为11位");
							return;
						}
						if(map.get("PLATE_NO")==null||map.get("PLATE_NO").length()!=7){
							SC.say("车牌号必须为7位");
							return;
						}
						if(ObjUtil.isNotNull(map.get("TRAILER_NO"))&&map.get("TRAILER_NO").length()!=7){
							SC.say("挂车号必须为7位");
							return;
						}
//						if(map.get("TEMP_NO1")==null&&map.get("TEMP_NO2")==null&&map.get("GPS_NO1")==null){
//							if(curRecord.getAttribute("TEMP_NO1")==null&&curRecord.getAttribute("TEMP_NO2")==null&&curRecord.getAttribute("GPS_NO1")==null){
//								SC.say("必须填写温控设备或者GPS设备");
//								return;
//							}
//						}
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
		}else{
			return;
		}
	}
	
	/**
	 * 执行更新操作
	 * @author yuanlei
	 * @param op_flag
	 */
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
}
