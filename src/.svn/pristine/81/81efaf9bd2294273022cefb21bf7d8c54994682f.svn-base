package com.rd.client.action.tms.changerecord;

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
import com.rd.client.view.tms.ChangeRecordView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveChangeAction implements ClickHandler {
	
	private ValuesManager form;
	private ListGrid table;
	private ListGrid loadTable;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private ChangeRecordView view;
	
	public SaveChangeAction(ListGrid p_table, ListGrid loadTable,ValuesManager p_form, HashMap<String, String> p_map, ChangeRecordView view) {
		this.table = p_table;
		this.loadTable = loadTable;
		this.form = p_form;  
		this.map = p_map;
		this.view = view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		
        record = form.getValues(); 
        form.getValueAsString("FEE_ID");
        record.remove("OP_FLAG");
        if(id_name != null) {
        	convertNameToId(record, id_name);            //将前台FORM的名称转换成ID
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
						doOperation(op_flag);
					}
				}
				else {
					MSGUtil.sayError(obj.get(1).toString());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void doInsert(Map map) {
		String json = Util.mapToJson(map);
		
		//设置日志信息
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
					
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					form.setValue("ID", result.substring(2));
					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(0,record);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
					table.redraw();
					table.selectRecord(record);	
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);//wangjun 2010-12-8
					if(view.shpmTable!=null){
						HashMap<String,Object> map = new HashMap<String, Object>();
						HashMap<String,String> shpm_no = new HashMap<String,String>();
						ListGridRecord[] records = view.shpmTable.getSelection();
						for(int i = 0 ; i <records.length ; i++){
							shpm_no.put(String.valueOf(i+1), records[i].getAttribute("SHPM_NO"));
						}
						map.put("1", view.shpmTable.getSelectedRecord().getAttribute("LOAD_NO"));
						map.put("2", shpm_no);
						map.put("3", form.getItem("PLATE_NO2").getValue());
						map.put("4", form.getItem("VEHICLE_TYP_ID2").getValue());
						map.put("5", form.getItem("DRIVER2").getValue());
						map.put("6", form.getItem("MOBILE2").getValue());
						map.put("7", LoginCache.getLoginUser().getUSER_ID());
						String json = Util.mapToJson(map);
						Util.async.execProcedure(json, "SP_LOAD_CHANGE_RECORD(?,?,?,?,?,?,?,?)",new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(String result) {
								if(result.equals(StaticRef.SUCCESS_CODE)) {
									MSGUtil.showOperSuccess();
								}else{
									MSGUtil.sayError(result);
									ArrayList<String> sqlList = new ArrayList<String>();
									StringBuffer sf = new StringBuffer();
									sf.append("delete from TRANS_CHANGE_RECORD where ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
									sqlList.add(sf.toString());
									Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

										@Override
										public void onFailure(Throwable caught) {
											
										}

										@Override
										public void onSuccess(String result) {
											if(result.equals(StaticRef.SUCCESS_CODE)) {
												Criteria findValues = new Criteria();
												findValues.addCriteria("OP_FLAG","M");
									            findValues.addCriteria("LOAD_NO1", loadTable.getSelectedRecord().getAttribute("LOAD_NO").toString());
									            table.fetchData(findValues);
											}else{
												MSGUtil.sayError(result);
											}
										}
										
									});
								}
							}
							
						});
					}else{
						ArrayList<String> sqlList = new ArrayList<String>();
						StringBuffer sf = new StringBuffer();
						sf.append("update TRANS_LOAD_HEADER set PLATE_NO=");
						sf.append("'"+form.getItem("PLATE_NO2").getValue()+"',");
						sf.append("VEHICLE_TYP_ID=");
						sf.append("'"+form.getItem("VEHICLE_TYP_ID2").getValue()+"',");
						sf.append("DRIVER1=");
						sf.append("'"+form.getItem("DRIVER2").getValue()+"',");
						sf.append("MOBILE1=");
						sf.append("'"+form.getItem("MOBILE2").getValue()+"'");
						sf.append(" where LOAD_NO=");
						sf.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
						sqlList.add(sf.toString());
						StringBuffer sf1 = new StringBuffer();
						sf1.append("update TRANS_SHIPMENT_HEADER set PLATE_NO=");
						sf1.append("'"+form.getItem("PLATE_NO2").getValue()+"',");
						sf1.append("VEHICLE_TYP_ID=");
						sf1.append("'"+form.getItem("VEHICLE_TYP_ID2").getValue()+"',");
						sf1.append("DRIVER=");
						sf1.append("'"+form.getItem("DRIVER2").getValue()+"',");
						sf1.append("MOBILE=");
						sf1.append("'"+form.getItem("MOBILE2").getValue()+"'");
						sf1.append(" where LOAD_NO=");
						sf1.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
						sqlList.add(sf1.toString());
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(String result) {
								if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
									MSGUtil.showOperSuccess();
									loadTable.getSelectedRecord().setAttribute("PLATE_NO", form.getItem("PLATE_NO2").getValue());
									loadTable.getSelectedRecord().setAttribute("VEHICLE_TYP_ID", form.getItem("VEHICLE_TYP_ID2").getValue());
									loadTable.getSelectedRecord().setAttribute("DRIVER1", form.getItem("DRIVER2").getValue());
									loadTable.getSelectedRecord().setAttribute("MOBILE1", form.getItem("MOBILE2").getValue());
									loadTable.redraw();
									if(view != null){
										view.initSaveBtn();
									}
								}else{
									MSGUtil.sayError(result);
								}
							}
						});
					}
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
					if(view.shpmTable!=null){
						return;
					}else{
						Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
						//刷新选中的记录  //异常
						table.redraw();
						form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
						ArrayList<String> sqlList = new ArrayList<String>();
						StringBuffer sf = new StringBuffer();
						sf.append("update TRANS_LOAD_HEADER set PLATE_NO=");
						sf.append("'"+form.getItem("PLATE_NO2").getValue()+"',");
						sf.append("VEHICLE_TYP_ID=");
						sf.append("'"+form.getItem("VEHICLE_TYP_ID2").getValue()+"',");
						sf.append("DRIVER1=");
						sf.append("'"+form.getItem("DRIVER2").getValue()+"',");
						sf.append("MOBILE1=");
						sf.append("'"+form.getItem("MOBILE2").getValue()+"'");
						sf.append(" where LOAD_NO=");
						sf.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
						sqlList.add(sf.toString());
						StringBuffer sf1 = new StringBuffer();
						sf1.append("update TRANS_SHIPMENT_HEADER set PLATE_NO=");
						sf1.append("'"+form.getItem("PLATE_NO2").getValue()+"',");
						sf1.append("VEHICLE_TYP_ID=");
						sf1.append("'"+form.getItem("VEHICLE_TYP_ID2").getValue()+"',");
						sf1.append("DRIVER=");
						sf1.append("'"+form.getItem("DRIVER2").getValue()+"',");
						sf1.append("MOBILE=");
						sf1.append("'"+form.getItem("MOBILE2").getValue()+"'");
						sf1.append(" where LOAD_NO=");
						sf1.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
						sqlList.add(sf1.toString());
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(String result) {
								if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
									MSGUtil.showOperSuccess();
									loadTable.getSelectedRecord().setAttribute("PLATE_NO", form.getItem("PLATE_NO2").getValue());
									loadTable.getSelectedRecord().setAttribute("VEHICLE_TYP_ID", form.getItem("VEHICLE_TYP_ID2").getValue());
									loadTable.getSelectedRecord().setAttribute("DRIVER1", form.getItem("DRIVER2").getValue());
									loadTable.getSelectedRecord().setAttribute("MOBILE1", form.getItem("MOBILE2").getValue());
									loadTable.redraw();
									if(view != null){
										view.initSaveBtn();
									}
								}else{
									MSGUtil.sayError(result);
								}
							}
						});
					
					}
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
					doOperation(op_flag);
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
	private void doOperation(String op_flag) {
		if(op_flag.equals("M")) {                                  //--修改 
			
			//---设置日志信息
			if(table.getSelectedRecord() != null) {
				Map<String, String> select_map = Util.putRecordToModel(table.getSelectedRecord());   //获取修改前记录
	        	logList = new ArrayList<String>();
	        	logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
			}
			//---设置完毕
			ArrayList<String> sqlList = new ArrayList<String>();
			if(record != null) {
				record.put("TABLE", table.getDataSource().getTableName());
				String json = Util.mapToJson(record);
				sqlList.add(json);
			}
			doUpdate(sqlList);
		}
		else if(op_flag.equals("A")) {                             //--插入		
			if(record != null) {
				record.put("TABLE", table.getDataSource().getTableName());
				doInsert(record);
			}
		}
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
	
}
