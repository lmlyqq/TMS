package com.rd.client.action.tms.order;

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
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输管理--托运单管理--修改保存
 * @author fanglm
 *
 */
public class SaveOrderHeaderAction implements ClickHandler{
	private ValuesManager form;
	private ListGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private OrderView view;
	private boolean exceFeeFlag = false;

	public SaveOrderHeaderAction(ListGrid pTable, ValuesManager pForm,
			HashMap<String, String> pMap, Map<String, String> pCorr,OrderView view) {
		table = pTable;
		form = pForm;  
		map = pMap;
		id_name = pCorr;
		this.view = view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		if("JF".equals(JSOHelper.getAttribute(((IButton)event.getSource()).getJsObj(), "eventSource"))){
			JSOHelper.setAttribute(((IButton)event.getSource()).getJsObj(), "eventSource", "");
			exceFeeFlag = true;
		}else{
			exceFeeFlag = false;
		}
		
		if(form.getValueAsString("STATUS") == null )
			return;
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		if(op_flag.equals(StaticRef.MOD_FLAG)) {
			ListGridRecord rec = table.getSelectedRecord();
			if(!(rec == null || StaticRef.ORD_STATUS_CREATE.equals(rec.getAttribute("STATUS"))) && 
					view.vm.getValueAsString("STATUS")!=null && 
					view.vm.getValueAsString("STATUS").equals(rec.getAttribute("STATUS"))){
				MSGUtil.sayError("订单非" + StaticRef.SO_CREATE_NAME + "状态，不能修改！");
				return;
			}
		}
		
        record = form.getValues(); 
//        if(ObjUtil.isNotNull(record.get("TOT_WORTH"))){
//        	double worth = toDouble(record.get("TOT_WORTH"));
//        	record.put("TOT_WORTH", String.valueOf(worth));
//        }
        if(!ObjUtil.isNotNull(record.get("LOAD_NAME"))) {
        	record.put("LOAD_ID", "");
        	record.put("LOAD_NAME", "");
        }
        if(!ObjUtil.isNotNull(record.get("UNLOAD_NAME"))) {
        	record.put("UNLOAD_ID", "");
        	record.put("UNLOAD_NAME", "");
        }
        record.remove("OP_FLAG");
        if(ObjUtil.isNotNull(record.get("CUSTOM_ODR_NO")) 
        		&& ObjUtil.isNotNull(form.getValueAsString("UNIQ_CONO_FLAG")) && "Y".equals(form.getValueAsString("UNIQ_CONO_FLAG"))){//根据客户控制参数判断是否校验客户订单号唯一性
        	map.put("CUSTOM_ODR_NO", StaticRef.CHK_UNIQUE + Util.TI18N.CUSTOM_ODR_NO());
        }else{
        	map.remove("CUSTOM_ODR_NO");
        }

        if(id_name != null) {
        	convertNameToId(record, id_name);            //将前台FORM的名称转换成ID
        }
        if(!ObjUtil.isNotNull(record.get("TRANS_SRVC_ID"))){
        	record.put("TRANS_SRVC_ID", StaticRef.TRANS_LD);
        }
		if(record != null) {
			ArrayList<Object> obj = Util.getCheckResult(record, map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					if(obj.get(1) != null) {
						//需要校验唯一性
						chkUnique((HashMap<String, String>)obj.get(1), op_flag);
					}
					else {
						if(StaticRef.INS_FLAG.equals(op_flag)){
							String msg = view.saveOrderItemAction.checkDetail();
							if(msg.length() > 0){
								MSGUtil.sayError(msg);
							}else{
								doOperation(op_flag);
							}
						}else{
							doOperation(op_flag);
						}
					}
				}
				else {
					MSGUtil.sayError(obj.get(1).toString());
				}
			}
		}
		
//		view.isMax = !view.isMax;
	}

//	private double toDouble(Object value) {
//		String str = (value == null || value.toString().length() == 0)
//		? "0.00" : value.toString();
//		return str.indexOf(".") == -1 ? 
//				Integer.valueOf(str).doubleValue() : Double.parseDouble(str);
//	}

	@SuppressWarnings("unchecked")
	public void doInsert(final Map map) {
		String json = Util.mapToJson(map);
		
		//设置日志信息
		//String[] titles = Util.getPropTitle(table.getDataSource().getAttribute("tableName"));
		//String[] fields = Util.getPropField(table.getDataSource().getAttribute("tableName"));
		String[] titles = Util.getPropTitle(table.getDataSource().getTableName());
		String[] fields = Util.getPropField(table.getDataSource().getTableName());
		String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
		//设置完毕
		
		Util.async.doInsert(descr, json, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					final ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					form.setValue("ID", result.substring(2));
					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					
					view.enableOrDisables(view.add_map, true);
					view.enableOrDisables(view.del_map, true);
					view.enableOrDisables(view.save_map, true);
					
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					
					view.vm.getItem("CUSTOMER_NAME").setDisabled(true);
					//同时执行明细保存
					view.saveOrderItemAction.doSave(true, new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(String result) {							
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
								ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
								list.add(0,record);
								table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
								view.groupTable.discardAllEdits();
								table.redraw();
								table.selectRecord(record);
								/*Criteria criteria = new Criteria();
								criteria.addCriteria(view.searchForm.getValuesAsCriteria());
								criteria.addCriteria("OP_FLAG","M");
								table.fetchData(criteria);*/
//								Criteria crit = new Criteria();
//								crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//								crit.addCriteria("ODR_NO",table.getSelectedRecord().getAttribute("ODR_NO"));
//								view.groupTable.fetchData(crit);
//								Criteria criteria = new Criteria();
//								criteria.addCriteria(view.searchForm.getValuesAsCriteria());
//								criteria.addCriteria("OP_FLAG","M");
//								table.fetchData(criteria);
//								view.groupTable.invalidateCache();
//								Criteria crit = new Criteria();
//								crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//								crit.addCriteria("ODR_NO",view.vm.getItem("ODR_NO").getValue().toString());
//								view.groupTable.fetchData(crit);
								if(ObjUtil.isNotNull(map.get("BILL_PRICE_FLAG")) && 
										Boolean.parseBoolean(map.get("BILL_PRICE_FLAG").toString())){
									saveBill();
								}
							}else{
								MSGUtil.sayError(result);
							}
						}
					});
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void doUpdate(ArrayList<String> sqlList) {
        
		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					//同时执行明细保存
					view.saveOrderItemAction.doSave(true, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(String result) {
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
								Util.updateToRecord(form, table, table.getRecord(view.hRow));   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
								//刷新选中的记录
//								table.updateData(table.getRecord(view.hRow));
								table.redraw();
								form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
								
								view.enableOrDisables(view.add_map, true);
								view.enableOrDisables(view.del_map, true);
								view.enableOrDisables(view.save_map, true);
								Object BILL_PRICE_FLAG = record.get("BILL_PRICE_FLAG");
								if(ObjUtil.isNotNull(BILL_PRICE_FLAG) && 
										Boolean.parseBoolean(BILL_PRICE_FLAG.toString())){
									saveBill();
								}
								if(exceFeeFlag){
									exceFeeFlag = false;
									JSOHelper.setAttribute(view.feeButton.getJsObj(),"eventSource", "BC");
									ClickEvent.fire(view.feeButton, view.feeButton.getConfig());
								}
							}else{
								MSGUtil.sayError(result);
							}
						}
					});
					
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	/**
	 * 查询数据的唯一性校验
	 * @author yuanlei
	 * @param map
	 * @param flag
	 */
	private void chkUnique(HashMap<String, String> map, String flag) {
		final String op_flag = flag;
		Util.async.getCheckResult(Util.mapToJson(map), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(!ObjUtil.isNotNull(result)) {
					String msg = view.saveOrderItemAction.checkDetail();
					if(msg.length() > 0){
						MSGUtil.sayError(msg);
					}else{
						doOperation(op_flag);
						//同时执行明细保存
						view.saveOrderItemAction.doSave(false);
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}		
		});
	}
	
	/**
	 * 执行插入或更新操作
	 * @author yuanlei
	 * @param op_flag
	 */
	private void doOperation(final String op_flag) {
		//setRefenence1Value();
		route2BillHandler(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(op_flag.equals("M")) {                                  //--修改 
					
					//---设置日志信息
					Map<String, String> select_map = Util.putRecordToModel(table.getRecord(view.hRow));   //获取修改前记录
			        logList = new ArrayList<String>();
			        //logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
			        logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
					//---设置完毕
					ArrayList<String> sqlList = new ArrayList<String>();
					if(record != null) {
						//record.put("TABLE", table.getDataSource().getAttribute("tableName"));
						record.put("TABLE", table.getDataSource().getTableName());
						//record.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
						String json = Util.mapToJson(record);
						sqlList.add(json);
					}
					doUpdate(sqlList);
				}
				else if(op_flag.equals("A")) {                             //--插入		
					record.put("TABLE", table.getDataSource().getTableName());
					doInsert(record);
				}
			}
		});
		
	}
	
	private void convertNameToId(Map<String, String> map, Map<String, String> id_name) {
		Object[] iter = id_name.keySet().toArray();
		String key = "", value = "";
		for(int i = 0; i < iter.length; i++) {
			key = (String)iter[i];
			value = id_name.get(key);
			if((key.indexOf("_NAME") <= 0) || !key.substring(key.length() -5).equals("_NAME")) {
				map.put(key, value);
			}
		}
	}
	
	private void route2BillHandler(final AsyncCallback<String> callback){
		if(callback == null)return;
		Object billPriceFlag = record.get("BILL_PRICE_FLAG");
		if (!StaticRef.SHOP.equals(record.get("BIZ_TYP")) && ObjUtil.isNotNull(billPriceFlag) && !Boolean.parseBoolean(billPriceFlag.toString())) {
			callback.onSuccess(StaticRef.SUCCESS_CODE);
			return;
		}
		String unloadId = ObjUtil.ifNull(record.get("UNLOAD_ID"), "");	//收货方ID;
		String billFlag = ObjUtil.ifNull(record.get("BILL_FLAG"), "");
		if(StaticRef.BILL_FLAG_N.equals(billFlag) || (ObjUtil.isNotNull(billPriceFlag) && Boolean.parseBoolean(billPriceFlag.toString()))){
			record.put("BILL_FLAG", StaticRef.BILL_FLAG_N);
			form.setValue("BILL_FLAG", StaticRef.BILL_FLAG_N);
			callback.onSuccess(StaticRef.SUCCESS_CODE);
			return;
		}
		if(!ObjUtil.isNotNull(unloadId)){
			callback.onSuccess(StaticRef.SUCCESS_CODE);
			return;
		}
		record.put("BILL_FLAG", StaticRef.BILL_FLAG_N);
		form.setValue("BILL_FLAG", StaticRef.BILL_FLAG_N);
		callback.onSuccess(StaticRef.SUCCESS_CODE);
	}
	
	private void saveBill(){
		ArrayList<String> sqlList = new ArrayList<String>();
		Object billPrice = record.get("BILL_PRICE");
		if(!ObjUtil.isNotNull(billPrice))return;
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		final String odrNO = view.vm.getValueAsString("ODR_NO");
		String id = null;
		ListGridRecord[] lgrs = view.groupTable2.getRecords();
		if(!(lgrs == null || lgrs.length == 0)){
			for (ListGridRecord r : lgrs) {
				if("ED42A02E81414C3ABE673A84DFD6DDA2".equals(r.getAttribute("FEE_ID"))){
					id = r.getAttribute("ID");
					break;
				}
			}
		}
		HashMap<String, String> record_map = new HashMap<String, String>();
		record_map.put("TABLE", "TRANS_BILL_RECE");
		if(ObjUtil.isNotNull(id)){
			record_map.put("ID", id);
		}
		record_map.put("ADDWHO", login_user);
		record_map.put("DOC_NO", odrNO);
		record_map.put("CHARGE_TYPE", "53EB6809BFCC436799F735AAE23658B9");
		record_map.put("BAS_VALUE", "1");
		record_map.put("PRICE", billPrice.toString());
		record_map.put("FEE_BAS", "TOT_GROSS_W");
		record_map.put("PRE_FEE", billPrice.toString());
		record_map.put("DUE_FEE", billPrice.toString());
		record_map.put("FEE_NAME", "应收运费");
		record_map.put("FEE_ID", "ED42A02E81414C3ABE673A84DFD6DDA2");
		record_map.put("DISCOUNT_RATE", "1");
		record_map.put("DISCOUNT_FEE", billPrice.toString());
		record_map.put("PAY_FEE", "");
		record_map.put("TFF_NAME", "");
		record_map.put("TFF_ID", "");
		record_map.put("NOTES", "");
		sqlList.add(Util.mapToJson(record_map));
		String appRow = " and DOC_NO='" + odrNO + "'";
		Util.async.doSave(null, sqlList,appRow, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					view.groupTable2.OP_FLAG = "M";
					Criteria crit = view.groupTable2.getCriteria();
					crit = crit == null ? new Criteria() : crit;
					view.groupTable2.discardAllEdits();
					view.groupTable2.invalidateCache();
					crit.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					crit.addCriteria("DOC_NO", odrNO);
					view.groupTable2.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							view.groupTable2.draw();
						}
					});
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
	}
}
