package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tab.TabSet;

public class OrderSaveFormAction implements ClickHandler{

	private ValuesManager form;
	private ListGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
//	private ArrayList<String> logList;  //日志信息
	private TabSet bottoTabSet;
	private String flag;
	
	public OrderSaveFormAction(ListGrid p_table, ValuesManager p_form, HashMap<String, String> p_map,TabSet bottoTabSet,String flag) {
		table = p_table;
		form = p_form;  
		map = p_map;
		this.bottoTabSet = bottoTabSet;
		this.flag = flag;
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
			public void onSuccess(final String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();;
					table.discardAllEdits();
					table.invalidateCache();
					Criteria criteria = new Criteria();
					if(flag.equals("AND_RECV_FLAG")){
		        		criteria.addCriteria("OP_FLAG","M");
		        		criteria.addCriteria("ENABLE_FLAG","Y");
		        		criteria.addCriteria("AND_RECV_FLAG","Y");
		        		if(ObjUtil.isNotNull(form.getValueAsString("CUSTOMER_ID"))){
		        			criteria.addCriteria("CUSTOMER_ID",form.getValueAsString("CUSTOMER_ID"));
		        		}
					}else{
		        		criteria.addCriteria("OP_FLAG","M");
		        		criteria.addCriteria("ENABLE_FLAG","Y");
		        		criteria.addCriteria("AND_LOAD_FLAG","Y");
		        		if(ObjUtil.isNotNull(form.getValueAsString("CUSTOMER_ID"))){
		        			criteria.addCriteria("CUSTOMER_ID",form.getValueAsString("CUSTOMER_ID"));
		        		}
					}
					table.fetchData(criteria,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							System.out.println(12345);
							ListGridRecord record = new ListGridRecord();
							record.setAttribute("ID", result.substring(2));
							form.setValue("ID", result.substring(2));
							Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
							ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
							table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
							table.redraw();
							table.selectRecord(0);	
							bottoTabSet.selectTab(0);
						}
					}); 
					
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
//	private void doUpdate(ArrayList<String> sqlList) {
//        
//		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				MSGUtil.sayError(caught.getMessage());
//			}
//	
//			@Override
//			public void onSuccess(String result) {
//				if(result.equals(StaticRef.SUCCESS_CODE)) {
//					MSGUtil.showOperSuccess();
//					Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
//					//刷新选中的记录  //异常
//					table.redraw();
//					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
//				}
//				else {
//					MSGUtil.sayError(result);
//				}
//			}
//		});
//	}

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
//		if(op_flag.equals("M")) {                                  //--修改 
//			
//			//---设置日志信息
//			if(table.getSelectedRecord() != null) {
//				Map<String, String> select_map = Util.putRecordToModel(table.getSelectedRecord());   //获取修改前记录
//	        	logList = new ArrayList<String>();
//	        	logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
//			}
//			//---设置完毕
//			ArrayList<String> sqlList = new ArrayList<String>();
//			if(record != null) {
//				record.put("TABLE", table.getDataSource().getTableName());
//				String json = Util.mapToJson(record);
//				sqlList.add(json);
//			}
//			doUpdate(sqlList);
//		}
//		else if(op_flag.equals("A")) {                             //--插入		
			if(record != null) {
				record.put("TABLE", table.getDataSource().getTableName());
				doInsert(record);
			}
//		}
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