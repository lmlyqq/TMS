package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 保存的通用ACTION
 * @author yuanlei
 *
 */
public class SaveAction2 implements ClickHandler {

	private SGTable table = null;
	private SGTable main_table = null;
	private DataSource ds = null;
	private ArrayList<Record> record = null; 
	private int[] edit_rows = null;               //列表中所有被编辑过的行号
	//private HashMap<String, String> add_map = null;   //附加传入的参数值，insert用
	private HashMap<String, String> check_map;
	private HashMap<String, String> map;
	private HashMap<String, String> ck_map;
	private ArrayList<String> logList;   //日志信息
	private SGForm view;
	private int initBtn =0;
	private String proName = null;
	private ArrayList<String> valList = null;
	public SaveAction2(SGTable p_table) {
		table = p_table;
	}
	public SaveAction2(SGTable p_table, HashMap<String, String> p_map) {
		table = p_table;
		check_map = p_map;
	}
	
	public SaveAction2(SGTable p_table, HashMap<String, String> p_map,SGForm view) {
		table = p_table;
		check_map = p_map;
		this.view = view;
	}
	public SaveAction2(SGTable p_table, HashMap<String, String> p_map,SGForm view,int initBtn) {
		table = p_table;
		check_map = p_map;
		this.view = view;
		this.initBtn = initBtn;
	}
	public SaveAction2(SGTable p_table, SGTable p_maintable,HashMap<String, String> p_map) {
		table = p_table;
		main_table = p_maintable;
		check_map = p_map;
	}
	
	public SaveAction2(SGTable p_table, SGTable p_maintable,HashMap<String, String> p_map,SGForm view,int initBtn) {
		table = p_table;
		main_table = p_maintable;
		check_map = p_map;
		this.view  = view;
		this.initBtn = initBtn;
	}
	public SaveAction2(SGTable p_table, SGTable p_maintable,HashMap<String, String> p_map,SGForm view) {
		table = p_table;
		main_table = p_maintable;
		check_map = p_map;
		this.view = view;
	}
	
	public void setProName(String proName){
		this.proName = proName;
	}
	
	public void setValList(ArrayList<String> list){
		this.valList = list;
	}
	
	private HashMap<String, String> checkValue(){
		HashMap<String, String> valMap = map;
		if(ObjUtil.isNotNull(check_map)){
			Object[] key = check_map.keySet().toArray();
			for(int i = 0;i<check_map.size();i++){
				String[] arrayKey = key[i].toString().split(",");
				for(int j = 0;j<arrayKey.length;j++){
					if(!arrayKey[j].equals("TABLE") && valMap.get(arrayKey[j]) == null){
						valMap.put(arrayKey[j].toString(), ck_map.get(arrayKey[j]));
						
					}
				}
			}
		}
		return valMap;
	}
	
	/**
	 * 返回记录中被修改的值集合
	 * @author fanglm
	 * @param row
	 * @return 被修改的值
	 */
	@SuppressWarnings("unchecked")
	private HashMap<String, String> getEditValues(int row){
		HashMap<String, String> vMap = (HashMap<String, String>)table.getEditValues(row);
		Object[] key = vMap.keySet().toArray();
		for(int i=0 ; i<vMap.size() ; i++){
			if(vMap.get(key[i]) == null)
				vMap.put(key[i].toString(), "");
		}
		return vMap;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		try {
		    if(!table.hasErrors()) {
				ds= table.getDataSource();
				
				String op_flag = table.OP_FLAG;
				edit_rows = table.getAllEditRows();   //获取所有修改过的记录行
				               
				if(edit_rows != null && edit_rows.length > 0) {
					record = new ArrayList<Record>();                               //--修改     
			        logList = new ArrayList<String>();
					for(int i = 0; i < edit_rows.length; i++) {
						Record curRecord = table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
						record.add(curRecord);
						map = getEditValues(edit_rows[i]);    //获取记录修改过的值
						if(op_flag.equals(StaticRef.MOD_FLAG)){
							ck_map = Util.putRecordToModel(table.getRecord(edit_rows[i]));
							ck_map = checkValue();
							
						}else{
							if(main_table != null) {
								addMainTableID(map);
							}
							ck_map = map;
						}
						//String log = Util.getUpdateLog(ck_map, map, table.getDataSource().getAttribute("tableName"));
						String log = Util.getUpdateLog(ck_map, map, table.getDataSource().getTableName());
						if(ObjUtil.isNotNull(log)) {
							logList.add(log);  //拼装的描述内容
						}
						
						if(map != null) {
							if(check_map != null) {
							ArrayList<Object> obj = Util.getCheckResult(ck_map, check_map);
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
							else {
								doOperation(op_flag);
							}
						}
					}
				}
		    }
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public void doInsert(HashMap<String, String> map) {
		String json = Util.mapToJson(map);
		//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
		//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
		String[] titles = Util.getPropTitle(ds.getTableName());
		String[] fields = Util.getPropField(ds.getTableName());
		String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
		Util.async.doInsert(descr, json, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MSGUtil.sayError(caught.getMessage());
			}
	
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					
					Record rec = record.get(0);
					rec.setAttribute("ID", result.substring(2));
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(list.size(),rec);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
					//table.redraw();
					table.selectRecord(list.size()-1);	
					
					if(view != null){
						view.initSaveBtn();
					}
					if(initBtn > 0){
						view.initBtn(initBtn);
					}
					
					if(ObjUtil.isNotNull(proName)){
						Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
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
	
	public void doInsert2(HashMap<String, String> map) {
		//String json = Util.mapToJson(map);
		//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
		//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
		//String[] titles = Util.getPropTitle(ds.getTableName());
		//String[] fields = Util.getPropField(ds.getTableName());
		//String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
		for(int i = 0; i < edit_rows.length; i++) {
			Record record1 = table.getEditedRecord(edit_rows[i]);
			record1.setAttribute("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
	    	table.addData(record1, new DSCallback() {
	
				@SuppressWarnings("unchecked")
				@Override
				public void execute(DSResponse response, Object rawData,
						DSRequest request) {
					if(response.getStatus() == 0) {				
						MSGUtil.showOperSuccess();
						table.OP_FLAG = "M";
						
						Record rec = record.get(0);
						//rec.setAttribute("ID", result.substring(2));
						ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
						list.add(list.size(),rec);
						table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
						//table.redraw();
						table.selectRecord(list.size()-1);	
						
						if(view != null){
							view.initSaveBtn();
						}
						if(initBtn > 0){
							view.initBtn(initBtn);
						}
						
						if(ObjUtil.isNotNull(proName)){
							Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}
							});
						}
					}
					else {
						MSGUtil.sayError(response.getErrors().get("errorMessage").toString());
					}
				}
	    		
	    	});
		}
		/*Util.async.doInsert(descr, json, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				MSGUtil.sayError(caught.getMessage());
			}
	
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					
					Record rec = record.get(0);
					rec.setAttribute("ID", result.substring(2));
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(list.size(),rec);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
					//table.redraw();
					table.selectRecord(list.size()-1);	
					
					if(view != null){
						view.initSaveBtn();
					}
					if(initBtn > 0){
						view.initBtn(initBtn);
					}
					
					if(ObjUtil.isNotNull(proName)){
						Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});*/
	}
	
	/*private void doUpdate(ArrayList<String> sqlList) {
		Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					ListGridRecord[] records = table.getRecords();
					
					for(int i=0;i<edit_rows.length;i++){
						Record rec = table.getEditedRecord(edit_rows[i]);
						String[] att = rec.getAttributes();
						for(int j=0;j<att.length;j++){
							if("false".equals(rec.getAttributeAsString(att[j]))){
								records[edit_rows[i]].setAttribute(att[j], rec.getAttributeAsBoolean(att[j]));
							}else{
								records[edit_rows[i]].setAttribute(att[j], rec.getAttribute(att[j]));
							}
						}
					}
					ArrayList list = new ArrayList(Arrays.asList(records));
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
					
					table.selectRecord(edit_rows[0]);
					
					if(ObjUtil.isNotNull(proName)){
						Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
						});
					}
					
					if(view != null){
						view.initSaveBtn();
					}
					if(initBtn > 0){
						view.initBtn(initBtn);
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}*/
	
	@SuppressWarnings("unchecked")
	private void doUpdate2(ArrayList<String> sqlList) {
		for(int i=0;i<edit_rows.length;i++){
			HashMap<String,String> record = (HashMap<String,String>)table.getEditValues(table.getRecord(edit_rows[i]));
			//HashMap<String,String> record = (HashMap<String,String>)table.getEditValues(edit_rows[i]);
			ListGridRecord record_new = new ListGridRecord();
			Object[] iter = record.keySet().toArray();
			for(int j = 0; j < iter.length; j++) {
				String key = (String)iter[j];
				Object value = record.get(key);
				if(value.getClass().equals(Boolean.class)) {
					record_new.setAttribute(key, Boolean.toString((Boolean)value));
				}
				else {
					record_new.setAttribute(key, value);
				}
			}
			//record_new.setAttribute("_selection_22", true);
			//Record new1 = table.getEditedRecord(edit_rows[i]);
			//new1.setAttribute("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
			//String[] strs = JSOHelper.getProperties(new1.getJsObj());
			//for(int m = 0; m < strs.length; m++ ) {
			//	System.out.println(strs[m]);
			//}
			record_new.setAttribute("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
			table.updateData(record_new,new DSCallback() {

				@Override
				public void execute(DSResponse response, Object rawData,
						DSRequest request) {
					if(response.getStatus() == 0) {				
						MSGUtil.showOperSuccess();
						table.OP_FLAG = "M";
						ListGridRecord[] records = table.getRecords();
						
						for(int i=0;i<edit_rows.length;i++){
							Record rec = table.getEditedRecord(edit_rows[i]);
							String[] att = rec.getAttributes();
							for(int j=0;j<att.length;j++){
								if("false".equals(rec.getAttributeAsString(att[j]))){
									records[edit_rows[i]].setAttribute(att[j], rec.getAttributeAsBoolean(att[j]));
								}else{
									records[edit_rows[i]].setAttribute(att[j], rec.getAttribute(att[j]));
								}
							}
						}
						ArrayList list = new ArrayList(Arrays.asList(records));
						table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
						
						table.selectRecord(edit_rows[0]);
						
						if(ObjUtil.isNotNull(proName)){
							Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
								
								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}
							});
						}
						
						if(view != null){
							view.initSaveBtn();
						}
						if(initBtn > 0){
							view.initBtn(initBtn);
						}
					}
					else {
						MSGUtil.sayError(response.getErrors().get("errorMessage").toString());
					}
				}
				
			});
		}
		/*Util.async.doUpdate(logList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					table.OP_FLAG = "M";
					ListGridRecord[] records = table.getRecords();
					
					for(int i=0;i<edit_rows.length;i++){
						Record rec = table.getEditedRecord(edit_rows[i]);
						String[] att = rec.getAttributes();
						for(int j=0;j<att.length;j++){
							if("false".equals(rec.getAttributeAsString(att[j]))){
								records[edit_rows[i]].setAttribute(att[j], rec.getAttributeAsBoolean(att[j]));
							}else{
								records[edit_rows[i]].setAttribute(att[j], rec.getAttribute(att[j]));
							}
						}
					}
					ArrayList list = new ArrayList(Arrays.asList(records));
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); 
					
					table.selectRecord(edit_rows[0]);
					
					if(ObjUtil.isNotNull(proName)){
						Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
						});
					}
					
					if(view != null){
						view.initSaveBtn();
					}
					if(initBtn > 0){
						view.initBtn(initBtn);
					}
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});*/
	}
	
	/**
	 * 加入主表的主键（对主从结构有效）
	 * @author yuanlei
	 * @param map 
	 **/
	private void addMainTableID(HashMap<String, String> map) {
		String pkey = main_table.getDataSource().getPrimaryKeyFieldName();
		String key = main_table.getDataSource().getField(pkey).getForeignKey();
		ListGridRecord record = main_table.getSelectedRecord();
		String value = record.getAttributeAsString(key);
		map.put(key, value);
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
			ArrayList<String> sqlList = new ArrayList<String>();
			if(map != null) {
				//map.put("TABLE", ds.getAttribute("tableName"));
				map.put("TABLE", ds.getTableName());
				//map.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
				String json = Util.mapToJson(map);
				sqlList.add(json);
			}
			doUpdate2(sqlList);
		}
		else if(op_flag.equals("A")) {                             //--插入		
			if(map != null) {
				//map.put("TABLE", ds.getAttribute("tableName"));
				map.put("TABLE", ds.getTableName());
				//map.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
//				if(main_table != null) {
//					addMainTableID(map);
//				}
				doInsert2(map);
			}
		}
	}
	
}
