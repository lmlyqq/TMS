package com.rd.client.action.tms.order;


import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Map;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.Criteria;
//import com.smartgwt.client.data.DSCallback;
//import com.smartgwt.client.data.DSRequest;
//import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
//import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--托运单明细--右键--保存
 * @author fanglm
 *
 */
public class SaveOrderItemAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{

	private SGTable table = null;
	private OrderView view;
	private HashMap<String, String> map;
	private String odr_no;
	private ArrayList<String> sqlList;
	private boolean boo = true;
	private HashMap<String, String> map1;
	
	private HashMap<String, String> ck_map = new HashMap<String, String>();
	
	public SaveOrderItemAction(SGTable p_table,OrderView view,HashMap<String, String> ck_map) {
		table = p_table;
		this.view = view;
		this.ck_map = ck_map;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		doSave(true);
		//是否与头信息一起保存
	}
	
	public void doSave(final boolean asyn){
		doSave(asyn, null);
	}
	
	@SuppressWarnings("unchecked")
	public void doSave(final boolean asyn, final AsyncCallback<String> callBack){
		if(!ObjUtil.isNotNull(view.vm.getValueAsString("ID"))){
			MSGUtil.sayError("订单主信息未保存,不能保存明细！");
			return;
		}
		if(!(view.selectRecord == null || 
				StaticRef.ORD_STATUS_CREATE.equals(view.selectRecord.getAttribute("STATUS"))) && 
				view.vm.getValueAsString("STATUS") != null && 
				view.vm.getValueAsString("STATUS").equals(view.selectRecord.getAttribute("STATUS"))){
			MSGUtil.sayError("订单非" + StaticRef.SO_CREATE_NAME + "状态，不能修改！");
			return;
		}
		
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			odr_no = view.vm.getValueAsString("ODR_NO");
			ArrayList<String> sqlList = new ArrayList<String>();
			StringBuffer sf = new StringBuffer();
			sf.append("UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2," +
					"LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID," +
					"LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE " +
					" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
					"(select min(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
			sqlList.add(sf.toString());
			StringBuffer sf1 = new StringBuffer();
			sf1.append("UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2," +
					"UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID," +
					"UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE " +
					" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
					"(select max(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
			sqlList.add(sf1.toString());
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					
				}

				@Override
				public void onSuccess(String result) {
					if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						view.table.getSelectedRecord().setAttribute("LOAD_NAME", table.getRecord(0).getAttribute("LOAD_NAME"));
						view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", table.getRecord(0).getAttribute("LOAD_AREA_NAME2"));
						view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_NAME"));
						view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_ADDRESS"));
						view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_AREA_NAME2"));
						view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_TEL"));
						view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_CONTACT"));
						view.table.redraw();
					}
				}
			});
			if(callBack != null){
				callBack.onSuccess(StaticRef.SUCCESS_CODE);
			}
			return;
		}
//		if(checkTemperature()){
//			MSGUtil.sayError("同一个托运单不能有2个温层的商品");
//			return;
//		}
		
		String msg = "";
		if(asyn){//同步保存，不需要校验生成list
			msg = checkDetail();
		}
		odr_no = view.vm.getValueAsString("ODR_NO");
		if(!boo){
			MSGUtil.sayError(msg);
			return;
		}

		String appRow = " and ODR_NO='" + odr_no + "'";
		Util.async.doSave(null, sqlList,appRow, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					if(asyn){
						MSGUtil.showOperSuccess();
					}
					
					view.enableOrDisables(view.sav_detail_map, false);
					view.enableOrDisables(view.add_detail_map, true);
					view.enableOrDisables(view.add_map, true);
//								view.enableOrDisables(view.save_map, false);
					view.enableOrDisables(view.del_map, true);
					table.OP_FLAG = "M";
//					Criteria crit = table.getCriteria();
//					crit = crit == null ? new Criteria() : crit;
//					table.discardAllEdits();
//					table.invalidateCache();
//					crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//					crit.addCriteria("ODR_NO",odr_no);
//					table.fetchData(crit,new DSCallback() {
//						
//						@Override
//						public void execute(DSResponse response, Object rawData, DSRequest request) {
//										ListGridRecord[] records = table.getRecords();
//										double qty=0.00,vol=0.00,gwt = 0.00;
//										for(int i=0;i<records.length;i++){
//											qty = qty + Double.parseDouble(records[i].getAttributeAsString("QNTY"));
//											vol = vol + Double.parseDouble(ObjUtil.ifNull(records[i].getAttributeAsString("VOL"),"0"));
//											gwt = gwt + Double.parseDouble(ObjUtil.ifNull(records[i].getAttributeAsString("G_WGT"),"0"));
//										}
							
//										//汇总信息
//										view.totalPanel.setValue("TOT_QNTY_SUM", qty);
//										view.totalPanel.setValue("TOT_VOL_SUM", vol + "立方米");
//										view.totalPanel.setValue("TOT_GROSS_W_SUM", gwt + "吨");
//						}
//					});
					
					
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					sf.append("UPDATE TRANS_ORDER_HEADER SET (LOAD_ID,LOAD_NAME,LOAD_AREA_ID,LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2," +
							"LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE)= ( select LOAD_ID,LOAD_NAME,LOAD_AREA_ID," +
							"LOAD_AREA_NAME,LOAD_AREA_ID2,LOAD_AREA_NAME2,LOAD_AREA_ID3,LOAD_AREA_NAME3,LOAD_ADDRESS,LOAD_CONTACT,LOAD_TEL,LOAD_CODE " +
							" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
							"(select min(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
					sqlList.add(sf.toString());
					StringBuffer sf1 = new StringBuffer();
					sf1.append("UPDATE TRANS_ORDER_HEADER SET (UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2," +
							"UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE)= ( select UNLOAD_ID,UNLOAD_NAME,UNLOAD_AREA_ID," +
							"UNLOAD_AREA_NAME,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_ID3,UNLOAD_AREA_NAME3,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL,UNLOAD_CODE " +
							" from TRANS_ORDER_ITEM where TRANS_ORDER_HEADER.ODR_NO = TRANS_ORDER_ITEM.ODR_NO and TRANS_ORDER_ITEM.ODR_ROW = " +
							"(select max(odr_row) from trans_order_item where ODR_NO = '"+odr_no+"')) where TRANS_ORDER_HEADER.ODR_NO = '"+odr_no+"'");
					sqlList.add(sf1.toString());
					
					int[] edit_row = table.getAllEditRows();
					for(int i=0;i<edit_row.length;i++){
						map1 =(HashMap<String, String>) table.getEditValues(edit_row[i]);
					}
					
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
								if(ObjUtil.isNotNull(map1.get("ID"))){
									System.out.println(map1);
									if(ObjUtil.isNotNull(map1.get("LOAD_NAME"))){
										view.table.getSelectedRecord().setAttribute("LOAD_NAME", map1.get("LOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", map1.get("LOAD_AREA_NAME2"));
									}else{
										view.table.getSelectedRecord().setAttribute("LOAD_NAME", table.getRecord(0).getAttribute("LOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", table.getRecord(0).getAttribute("LOAD_AREA_NAME2"));
									}
									if(ObjUtil.isNotNull(map1.get("UNLOAD_NAME"))){
										view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", map1.get("UNLOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", map1.get("UNLOAD_ADDRESS"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", map1.get("UNLOAD_AREA_NAME2"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", map1.get("UNLOAD_TEL"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", map1.get("UNLOAD_CONTACT"));
									}else{
										view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_ADDRESS"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_AREA_NAME2"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_TEL"));
										view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", table.getRecord(table.getRecords().length-1).getAttribute("UNLOAD_CONTACT"));
									}
								}else{
									if(ObjUtil.isNotNull(table.getRecord(0).getAttribute("LOAD_NAME"))){
										view.table.getSelectedRecord().setAttribute("LOAD_NAME", table.getRecord(0).getAttribute("LOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", table.getRecord(0).getAttribute("LOAD_AREA_NAME2"));
									}else{
										view.table.getSelectedRecord().setAttribute("LOAD_NAME", map1.get("LOAD_NAME"));
										view.table.getSelectedRecord().setAttribute("LOAD_AREA_NAME2", map1.get("LOAD_AREA_NAME2"));
									}
									view.table.getSelectedRecord().setAttribute("UNLOAD_NAME", map1.get("UNLOAD_NAME"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_ADDRESS", map1.get("UNLOAD_ADDRESS"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_AREA_NAME2", map1.get("UNLOAD_AREA_NAME2"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_TEL", map1.get("UNLOAD_TEL"));
									view.table.getSelectedRecord().setAttribute("UNLOAD_CONTACT", map1.get("UNLOAD_CONTACT"));
								}
								view.table.redraw();
								table.discardAllEdits();
								table.invalidateCache();
								Criteria crit = table.getCriteria();
								crit = crit == null ? new Criteria() : crit;
								crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
								crit.addCriteria("ODR_NO",odr_no);
								table.fetchData(crit);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
					
					if(view.table.getSelectedRecord() != null){
						view.table.collapseRecord(view.table.getSelectedRecord());
					}
				}else {
					MSGUtil.sayError(result);
					
				}
				if(callBack != null){
					callBack.onSuccess(result);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if(callBack != null){
					callBack.onFailure(caught);
				}
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public String checkDetail(){
		boo = true;
//		ListGridRecord[] edit_row = table.getRecords();
//		table.invalidateCache();
		
		
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			return "";
		}
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String sql;
		sqlList = new ArrayList<String>();
		StringBuffer msg = new StringBuffer();
		for(int i=0;i<edit_row.length;i++){
			map =(HashMap<String, String>) table.getEditValues(edit_row[i]);
			//fanglm 2011-2-27
			if(table.getRecord(edit_row[i]) != null){
				HashMap<String, String> record_map = Util.putRecordToModel(table.getRecord(edit_row[i]));
				Object[] key = map.keySet().toArray();
				for(int j=0;j<key.length;j++){
					record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),0.00).toString());
				}
				map = record_map;
			}
			
			calcNGwt();//计算体积毛重,计费毛重
			
			ArrayList<Object> obj = Util.getCheckResult(map, ck_map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(!result.equals(StaticRef.SUCCESS_CODE)) {
					msg.append(obj.get(1).toString());
					boo = false;
				}
			}
			/*if(map.get("QNTY") != null && Double.valueOf(String.valueOf(map.get("QNTY"))) <= 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行数量必须大于0！");
				boo = false;
			}*/
			map.put("TABLE", "TRANS_ORDER_ITEM");
			map.put("ADDWHO", login_user);
			map.put("ODR_NO", view.vm.getValueAsString("ODR_NO"));
			/*if(rList.get(edit_row[i]) != null){
				map.put("ID", rList.get(edit_row[i]).getAttribute("ID"));
			}*/
			sql = Util.mapToJson(map);
			sqlList.add(sql);
		}
		return msg.toString();
	}
	
	
	/**
	 * 检查是否有两个不同温层存在
	 * true有,false没有
	 */
	/*@SuppressWarnings("unchecked")
	private boolean checkTemperature(){
		ListGridRecord[] records = table.getRecords();
		int[] edit_row = table.getAllEditRows();
		Map<String, String> tempMap=new HashMap<String, String>();
		Map<String, String> idTMap=new HashMap<String, String>();
		if(records.length + edit_row.length < 2){
			return false;
		}
		for (ListGridRecord r : records) {
			tempMap.put(r.getAttributeAsString("TEMPERATURE1"), r.getAttribute("ID"));
			idTMap.put(r.getAttribute("ID"), r.getAttributeAsString("TEMPERATURE1"));
		}
		for (int i : edit_row) {
			String temperature1 = ((HashMap<String, String>)table.getEditValues(i)).get("TEMPERATURE1");
			if(!ObjUtil.isNotNull(temperature1)){
				continue;
			}
			String id = ((HashMap<String, String>)table.getEditValues(i)).get("ID");
			id = id==null?"true":id;
			if(id != null && ObjUtil.isNotNull(idTMap.get(id))){
				if(!idTMap.get(id).equals(temperature1) 
						&& (tempMap.size() > 1 || records.length + edit_row.length == 2)){
					tempMap.remove(idTMap.get(id));
				}
			}
			tempMap.put(temperature1, "true");
		}
		if(tempMap.size() > 1){
			return true;
		}
		return false;
	}*/
	
	private void calcNGwt(){
		try {
			double vol = toDouble(map.get("VOL"));
			double volGwtRatio = toDouble(map.get("VOL_GWT_RATIO"));
			double lotatt01 = vol * volGwtRatio;
			double baseGwt = toDouble(LoginCache.getParamString("BASE_GWT"));
			double gGwt = toDouble(map.get("G_WGT"));
			double nGwt = 0;
			if(lotatt01 > baseGwt){
				nGwt = gGwt > lotatt01 ? gGwt : lotatt01;
			}else{
				nGwt = gGwt;
			}
			map.put("LOTATT01", String.valueOf(lotatt01));
			map.put("N_WGT", String.valueOf(nGwt));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double toDouble(Object value){
		String str = (value == null || value.toString().length() == 0)
						? "0.00" : value.toString();
		return str.indexOf(".") == -1 ? 
				Integer.valueOf(str).doubleValue() : Double.parseDouble(str);
	}

	@Override
	public void onClick(ClickEvent event) {
		doSave(true);
	}
}