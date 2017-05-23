package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.PayReqBillView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 应付请款单--发票页签--保存按钮
 * @author 
 * @create time 2011-03-01 14:21
 */
public class SavePayInvoiceAction  implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	private DynamicForm form;
	private ListGrid table;
	private ListGrid Ttable;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private PayReqBillView view;
	private Map<String,String> comb_val;
	private String SUPLR_ID;
	
	public SavePayInvoiceAction(ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, Map<String, String > p_corr) {
		table = p_table;
		form = p_form;
		map = p_map;        
		id_name = p_corr;   
	}
	
	public SavePayInvoiceAction(ListGrid t_table,ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, PayReqBillView view) {
		Ttable=t_table;
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
		this.view = view;
	}
	public SavePayInvoiceAction(ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, PayReqBillView view,Map<String,String> comb_val) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
		this.view = view;
		this.comb_val = comb_val;
	}


	@Override
	public void onClick(ClickEvent event) {
		click();
	}
	
	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		click();
		
	}
	
	@SuppressWarnings("unchecked")
	private void click(){
		SUPLR_ID=Ttable.getSelectedRecord().getAttribute("SUPLR_ID");
		String ACT_AMOUNT=ObjUtil.ifObjNull(form.getValue("ACT_AMOUNT"),0).toString();
		String AMOUNT=ObjUtil.ifObjNull(form.getValue("SUBTAX_AMOUNT"),0).toString();
		String TAX_AMOUNT=ObjUtil.ifObjNull(form.getValue("TAX_AMOUNT"),0).toString();
		
		String T_ACT_AMOUNT=ObjUtil.ifObjNull(Ttable.getSelectedRecord().getAttribute("ACT_AMOUNT"),0).toString();
		double ACT_AMOUNT1=Double.parseDouble(ACT_AMOUNT);
		double AMOUNT1=Double.parseDouble(AMOUNT);
		double TAX_AMOUNT1=Double.parseDouble(TAX_AMOUNT);
		double T_ACT_AMOUNT1=Double.parseDouble(T_ACT_AMOUNT);
		if(T_ACT_AMOUNT1>=ACT_AMOUNT1){
		
		if(ACT_AMOUNT1==(AMOUNT1+TAX_AMOUNT1)){
			
			
		String op_flag = ObjUtil.ifObjNull(form.getValue("OP_FLAG"), StaticRef.MOD_FLAG).toString();
        record = form.getValues();
        form.getField("OBJECT_ID");
        if(ObjUtil.isNotNull(comb_val)){
        	Object[] obj = comb_val.keySet().toArray();
        	for(int i=0; i<obj.length; i++){
        		String key = obj[i].toString();
        		record.put(comb_val.get(key), form.getItem(key).getDisplayValue());
        	}
        }
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
		}else{				
			MSGUtil.sayError("金额输入有误，金额（含税）应等于金额（不含税）与税额之和");	
		}	
	}else{
		MSGUtil.sayError("含税金额输入有误,确保值小于选中记录的应收金额!");		
  }
}

	@SuppressWarnings("unchecked")
	public void doInsert(Map map) {
		String json = Util.mapToJson(map);
		
		//设置日志信息
		//String[] titles = Util.getPropTitle(table.getDataSource().getAttribute("tableName"));
		//String[] fields = Util.getPropField(table.getDataSource().getAttribute("tableName"));
		String[] titles = Util.getPropTitle(table.getDataSource().getTableName());
		String[] fields = Util.getPropField(table.getDataSource().getTableName());
		String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
		//设置完毕
		final String REQ_NO=Ttable.getSelectedRecord().getAttribute("REQ_NO");
		Util.async.doInsert(descr, json, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					final ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					form.setValue("ID", result.substring(2));
					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					if(ObjUtil.isNotNull(comb_val)){
			        	Object[] obj = comb_val.keySet().toArray();
			        	for(int i=0; i<obj.length; i++){
			        		String key = obj[i].toString();
			        		record.setAttribute(comb_val.get(key), form.getItem(key).getDisplayValue());
			        	}
			        }
					final ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(0,record);
					Util.db_async.getSingleRecord("SETT_CYC", "BAS_SUPPLIER", " where ID='"+SUPLR_ID+"'", null, new AsyncCallback<HashMap<String,String>>() {					
						@Override
						public void onSuccess(HashMap<String, String> result) {
							if(result==null){
								result=new HashMap<String, String>();
								result.put("SETT_CYC", "45");
							}
							if(result!=null){
								if(form.getValue("INVOICE_RECTIME")!=null){									
									String date=form.getValue("INVOICE_RECTIME").toString();
									if(result.get("SETT_CYC")!=null&&!result.get("SETT_CYC").equals("")){
										String days=result.get("SETT_CYC");
										String sql="update BILL_PAY_REQUEST set LATEST_PAY_TIME=(to_date('"+date+"','yyyy/mm/dd HH24:MI')+"+days+") where REQ_NO='"+REQ_NO+"'";
										Util.async.excuteSQL(sql, new AsyncCallback<String>() {
											
											@Override
											public void onSuccess(String result){

											}
											
											@Override
											public void onFailure(Throwable caught) {
												MSGUtil.sayError(caught.getMessage());
											}
										});
										
										
									}	
								}
							}else{
								//System.out.println("nnnnnn");
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());		
						}
					});
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);				
					if(view != null){
						view.initInvoiceBtn(1);
					}
//					final ListGridRecord selectRecord =new ListGridRecord();
//					selectRecord.setAttribute("ID", Ttable.getSelectedRecord().getAttribute("ID"));
					Ttable.invalidateCache();									
					final Criteria criteria1 = new Criteria();
					criteria1.addCriteria("OP_FLAG","M");	
					Ttable.fetchData(criteria1,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							MSGUtil.showOperSuccess();
							Ttable.selectRecord(0);
							table.invalidateCache();	
							Criteria criteria = new Criteria();
							criteria.addCriteria("OP_FLAG","M");
							criteria.addCriteria("REQ_NO",record.getAttributeAsString("REQ_NO"));
							table.fetchData(criteria);
						}
					});
					
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	public void doUpdate(ArrayList<String> sqlList) {
		final String REQ_NO=Ttable.getSelectedRecord().getAttribute("REQ_NO");
		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {					
					Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					//刷新选中的记录  //异常
					final Record record=table.getSelectedRecord();
					if("true".equalsIgnoreCase(form.getValueAsString("COM_FLAG"))){
						record.setAttribute("OBJECT_NAME", "");
					}
					Util.db_async.getSingleRecord("SETT_CYC", "BAS_SUPPLIER", " where ID='"+SUPLR_ID+"'", null, new AsyncCallback<HashMap<String,String>>() {						
						@Override
						public void onSuccess(HashMap<String, String> result) {
							if(result==null){
								result=new HashMap<String, String>();
								result.put("SETT_CYC", "45");
							}
							if(result!=null){
								if(form.getValue("INVOICE_RECTIME")!=null){									
									String date=form.getValue("INVOICE_RECTIME").toString();
									if(result.get("SETT_CYC")!=null&&!result.get("SETT_CYC").equals("")){
										String days=result.get("SETT_CYC");
										String sql="update BILL_PAY_REQUEST set LATEST_PAY_TIME=(to_date('"+date+"','yyyy/mm/dd HH24:MI')+"+days+") where REQ_NO='"+REQ_NO+"'";
										System.out.println(sql);
										Util.async.excuteSQL(sql, new AsyncCallback<String>() {
											
											@Override
											public void onSuccess(String result) {
	
											}
											
											@Override
											public void onFailure(Throwable caught) {
												MSGUtil.sayError(caught.getMessage());
											}
										});								
									}	
								}
							}
						}					
						@Override
						public void onFailure(Throwable caught) {	
							MSGUtil.sayError(caught.getMessage());
						}
					});
					MSGUtil.showOperSuccess();
					table.updateData(record);
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);				
					if(view != null){
						view.initInvoiceBtn(1);
					}
					
					Ttable.invalidateCache();									
					final Criteria criteria1 = new Criteria();
					criteria1.addCriteria("OP_FLAG","M");
					Ttable.fetchData(criteria1,new DSCallback() {	
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							Ttable.selectRecord(0);
							table.invalidateCache();	
							Criteria criteria = new Criteria();
							criteria.addCriteria("OP_FLAG","M");
							criteria.addCriteria("REQ_NO",record.getAttribute("REQ_NO"));
							table.fetchData(criteria);
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
	public void chkUnique(HashMap<String, String> map, String flag) {
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
			
			//设置日志信息
	        Map<String, String> select_map = Util.putRecordToModel(table.getSelectedRecord());   //获取修改前记录
	        logList = new ArrayList<String>();
	        //logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
	        logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
	        //设置完毕
			
			Util.updateToRecord(form, table, table.getSelectedRecord(),null);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
			
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
			if(record != null) {
				//record.put("TABLE", table.getDataSource().getAttribute("tableName"));
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
	
	public DynamicForm getForm() {
		return form;
	}

	public void setForm(DynamicForm form) {
		this.form = form;
	}
	
}
