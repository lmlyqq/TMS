package com.rd.client.action.tms;

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
import com.rd.client.view.tms.ChangeRDCView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
/**
 * 保存转仓单
 * @author yuanlei
 *
 */
public class ChangingRDCAction implements ClickHandler {

	private SGTable loadTable;
	private Record curRecord;
	private HashMap<String, String> check_map;
	
//	private HashMap<String, String> map;
//	private HashMap<String, String> ck_map;
	private ChangeRDCView view;
//	private int[] edit_rows = null;    
	//private ArrayList<String> logList;   //日志信息
	private String plate_no;
//	private HashMap<String, String> map;
	
	private int pos;
	public ChangingRDCAction(SGTable p_loadTable, HashMap<String, String> p_map,HashMap<String, String> map, ChangeRDCView p_view) {
		this.check_map = p_map;
		this.loadTable = p_loadTable;
		this.view = p_view;
//		this.map = map;
	}
	
//	private void checkValue(){
//		HashMap<String, String> valMap = map;
//		Object[] key = check_map.keySet().toArray();
//		for(int i = 0;i<check_map.size();i++){
//			String[] arrayKey = key[i].toString().split(",");
//			for(int j = 0;j<arrayKey.length;j++){
//				if(!arrayKey[j].equals("TABLE") && valMap.get(arrayKey[j]) == null){
//					valMap.put(arrayKey[j].toString(), ck_map.get(arrayKey[j]));
//				}
//			}
//		}
//		ck_map = valMap;
//	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		curRecord = loadTable.getSelectedRecord();
		if (!"10".equals(curRecord.getAttribute("STATUS"))) {
			MSGUtil.sayError("转仓单非已创建状态，不能执行【转仓】操作");
			return;
		}
		Map<String, String> map = new HashMap<String, String>();
//		Map<String,String> map2 = new HashMap<String, String>();
//		int[] edit_rows = loadTable.getAllEditRows();
		map.put("SUPLR_ID", ObjUtil.ifNull(curRecord.getAttribute("SUPLR_ID"), ""));
		map.put("SUPLR_NAME", ObjUtil.ifNull(curRecord.getAttribute("SUPLR_NAME"), ""));
		map.put("PLATE_NO", ObjUtil.ifNull(curRecord.getAttribute("PLATE_NO"), ""));
		map.put("VEHICLE_TYP_ID", ObjUtil.ifNull(curRecord.getAttribute("VEHICLE_TYP_ID"), ""));
		map.put("DRIVER1", ObjUtil.ifNull(curRecord.getAttribute("DRIVER1"), ""));
		map.put("MOBILE1", ObjUtil.ifNull(curRecord.getAttribute("MOBILE1"), ""));
		map.put("NOTES", ObjUtil.ifNull(curRecord.getAttribute("NOTES"), ""));
		map.put("ID", ObjUtil.ifNull(curRecord.getAttributeAsString("ID"), ""));
		if (ObjUtil.isNotNull(map.get("SUPLR_ID")) && !ObjUtil.isNotNull(map.get("SUPLR_NAME"))) {
			map.put("SUPLR_NAME", loadTable.getField("SUPLR_ID").getAttributeAsMap("valueMap").get(loadTable.getEditValuesMap().get(loadTable.getRecordIndex(loadTable.getSelectedRecord())).get("SUPLR_ID")).toString());
		}
		if (map != null) {
			if (check_map != null) {
				ArrayList<Object> obj = Util.getCheckResult(map, check_map);
				if (obj != null && obj.size() > 1) {
					if (StaticRef.SUCCESS_CODE.equals(obj.get(0).toString().substring(0,2))) {
						if (obj.get(1) != null) {
							chkUnique((HashMap<String, String>)obj.get(1),map);
						}else {
							doOperation(map);
						}
					}else {
						MSGUtil.sayError(obj.get(1).toString());
					}
				}
			}else {
				doOperation(map);
			}
		}
	}
	
	private void chkUnique(HashMap<String, String> map,final Map<String,String> map2){
		Util.async.getCheckResult(Util.mapToJson(map), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(!ObjUtil.isNotNull(result)) {
					doOperation(map2);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
		});
	}
	
	/**
	 * 执行更新操作
	 * @author yuanlei
	 * @param op_flag
	 */
	private void doOperation(Map<String, String> map) {                                 //--修改 
		ArrayList<String> sqlList = new ArrayList<String>();
		if(map != null) {
			//map.put("TABLE", loadTable.getDataSource().getAttribute("tableName"));
//			map.put("TABLE", loadTable.getDataSource().getTableName());
			map.put("TABLE", "TMS_CHANGE_RDC");
			//map.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
			String json = Util.mapToJson(map);
			sqlList.add(json);
		}
		doUpdate(sqlList,map);
	}
	
	
	private void doUpdate(ArrayList<String> sqlList,Map<String, String> map) {
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
					vMap.put("1", curRecord.getAttribute("RDC_NO"));
					vMap.put("2", curRecord.getAttribute("VEHICLE_TYP_ID"));
					vMap.put("3", plate_no);
					vMap.put("4", curRecord.getAttribute("PLATE_NO"));
					vMap.put("5", LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(vMap);
					Util.async.execProcedure(json, "SP_CHGRDC_UPDATE(?,?,?,?,?,?)", new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))){
								loadTable.getEditValuesMap().remove(pos);//删除编辑过的值
								String[] remain = result.substring(2).split(",");
								
								ListGridRecord rec = loadTable.getSelectedRecord();
								final int index = loadTable.getRecordIndex(rec);
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
								loadTable.fetchData(criteria,new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										loadTable.selectRecord(index);
									}
								});
								view.initButton(false, false, true, true, false);
								view.initButton(false, false, true, true, false);
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
		if(ObjUtil.isNotNull(map.get("MOBILE1")) && ObjUtil.isNotNull(map.get("EXEC_ORG_ID"))){
			HashMap<String, Object> vMap = new HashMap<String, Object>();
			vMap.put("1", ObjUtil.ifObjNull(map.get("MOBILE1"),""));
			vMap.put("2", ObjUtil.ifObjNull(map.get("DRIVER1"),curRecord.getAttribute("DRIVER1")));
			vMap.put("3", ObjUtil.ifObjNull(map.get("EXEC_ORG_ID"),""));
			vMap.put("4", "CLIENT");
			String json = Util.mapToJson(vMap);
			Util.async.execProcedure(json, "SP_CHECK_MOBILE(?,?,?,?,?)", new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}
}