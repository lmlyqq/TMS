package com.rd.client.action.base.area;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.base.BasAreaView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 行政区域保存
 * @author fanglm
 *
 */
public class SaveAreaAction  implements ClickHandler {

	private SGTable table;
	private DataSource ds;
	private ArrayList<Record> record = null; 
	private int[] edit_rows;               //列表中所有被编辑过的行号
	private BasAreaView view;
	private HashMap<String, String> map;
	private ArrayList<String> logList;   //日志信息
	public SaveAreaAction(SGTable p_table,BasAreaView view) {
		table = p_table;
		this.view = view;
		ds= table.getDataSource();
	}

	
	public void onClick(ClickEvent event) {
		
		String op_flag =table.OP_FLAG;
		edit_rows = table.getAllEditRows(); 
		doOperation(op_flag);
		
		  //获取所有修改过的记录行
		
		/**
		Record curRecord = table.getEditedRecord(edit_rows[0]);  //获取所有修改的记录
		HashMap<String, String> map = (HashMap<String, String>)table.getEditValues(curRecord);  
		
		map.remove("OP_FLAG");

		if(map != null) {
			ArrayList<Object> obj = Util.getCheckResult(map, view.check_map);
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
		**/
	}
	
	@SuppressWarnings("unchecked")
	private void doOperation(String op_flag){
		try {
			
			               
			if(edit_rows != null && edit_rows.length > 0) {
				ArrayList<String> sqlList = null;
				record = new ArrayList<Record>();
				if(op_flag.equals("M")) {                                  //--修改     
					sqlList = new ArrayList<String>();
			        logList = new ArrayList<String>();
			        HashMap<String, String> ck_map = null;
			        String op_index = view.selectNode.getAttribute("OP_INDEX");
					for(int i = 0; i < edit_rows.length; i++) {
						Record curRecord = table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
						record.add(curRecord);
						HashMap<String, String> map = (HashMap<String, String>)table.getEditValues(curRecord);                //获取记录修改过的值
						if(map != null) {
							map = getFlag(map, "ENABLE_FLAG");
							map = getFlag(map, "MODIFY_FLAG");
							if(ObjUtil.isNotNull(map.get("AREA_CODE"))){
								map.put("OP_INDEX", op_index + "," +map.get("AREA_CODE"));
							}
							//map.put("TABLE", ds.getAttribute("tableName"));
							map.put("TABLE", ds.getTableName());
							//map.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
							String json = Util.mapToJson(map);
							sqlList.add(json);
							
							ck_map = Util.putRecordToModel(table.getRecord(edit_rows[i]));
					        logList.add(Util.getUpdateLog(ck_map, map, table.getDataSource().getTableName()));  //拼装的描述内容
					        //logList.add(Util.getUpdateLog(ck_map, map, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
						}
					}
					doUpdate(sqlList);
				}
				else if(op_flag.equals("A")) {
					//--插入	
					String parent_id = view.selectNode.getAttribute("AREA_CODE");
					String level = view.selectNode.getAttribute("AREA_LEVEL");
					String op_index = view.selectNode.getAttribute("OP_INDEX");
					
					//校验AREA_CODE唯一性
					for(int i = 0; i < edit_rows.length; i++) {
						Record curRecord = table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
						record.add(curRecord);
						map = (HashMap<String, String>)table.getEditValues(edit_rows[i]);             //获取记录修改过的值
						if(map != null) {
							map = getFlag(map, "ENABLE_FLAG");
							map = getFlag(map, "MODIFY_FLAG");
							map.put("OP_INDEX", op_index + "," +map.get("AREA_CODE"));
							map.put("PARENT_AREA_ID", parent_id);
							map.put("AREA_LEVEL", Integer.valueOf(level)+1+"");
							//map.put("TABLE", ds.getAttribute("tableName"));
							map.put("TABLE", ds.getTableName());
							doInsert(map);
						}
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	
	/**
	 * 刷新数据
	 * @author yuanlei
	 */
	private void RefreshRecord(ArrayList<Record> record) {
		for(int i = 0; i < record.size(); i++) {
			table.updateData(record.get(i));
		}
		table.redraw();
	}
	
	
	public void doInsert(HashMap<String, String> map) {
		String json = Util.mapToJson(map);
		String[] titles = Util.getPropTitle(ds.getTableName());
		String[] fields = Util.getPropField(ds.getTableName());
		//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
		//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
		String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
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
					table.OP_FLAG = "M";
					Criteria crit = table.getCriteria();
					table.invalidateCache();
					crit.addCriteria("OP_FLAG", table.OP_FLAG);
					table.fetchData(crit);
					table.discardAllEdits();
					
					view.initSaveBtn();
				}
				else {
					MSGUtil.sayError("区域ID已存在！");
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
					table.OP_FLAG = "M";
					RefreshRecord(record);
					view.initSaveBtn();
					
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private HashMap<String, String> getFlag(HashMap<String, String> map,String key){
		if(map.get(key)!= null){
			String val = String.valueOf(map.get(key));
			if("true".equals(val)){
				map.remove(key);
				map.put(key, "Y");
			}else{
				map.remove(key);
				map.put(key, "N");
			}
		}
		return map;
	}
	
	
	
}
