package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 通用的FORM保存方法
 * @author yuanlei
 *
 */
public class SaveOilFormAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private DynamicForm form;
	private ListGrid table;
	private ListGrid tables;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private SGForm view;
	private int initBtn =0;
	private Map<String,String> comb_val;
	
	public SaveOilFormAction(ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, Map<String, String > p_corr) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
	}
	
	public SaveOilFormAction(ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, SGForm view) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
//		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
		this.view = view;
	}
	public SaveOilFormAction(ListGrid p_table, ListGrid d_table, DynamicForm p_form, HashMap<String, String> p_map, SGForm view,int initBtn) {
		tables = d_table;  //wenjian
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
		initBtn = this.initBtn;
//		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
		this.view = view;
	}
	public SaveOilFormAction(ListGrid p_table, DynamicForm p_form, HashMap<String, String> p_map, SGForm view,Map<String,String> comb_val) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
//		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
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
		String op_flag = ObjUtil.ifObjNull(form.getValue("OP_FLAG"), StaticRef.MOD_FLAG).toString();
		System.out.println(form);
		
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
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
				
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
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
					list.add(0,record);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
					table.redraw();
					table.selectRecord(record);	
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);//wangjun 2010-12-8
					if(view != null){
						view.initSaveBtn();
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	public void doUpdate(ArrayList<String> sqlList) {
		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					//刷新选中的记录  //异常
					Record record=table.getSelectedRecord();
					if("true".equalsIgnoreCase(form.getValueAsString("COM_FLAG"))){
						record.setAttribute("OBJECT_NAME", "");
					}
					table.updateData(record);
					table.redraw();
					table.getSelectedRecord().getAttribute("ID");
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					
					if(view != null){
						view.initSaveBtn();
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
	        record.put("ID", table.getSelectedRecord().getAttribute("ID"));
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
			System.out.println(sqlList);
		}
		else if(op_flag.equals("A")) {                             //--插入		
			if(record != null) {
				record.put("TABLE", table.getDataSource().getTableName());
				record.put("OILCARD", tables.getSelectedRecord().getAttribute("ID"));
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