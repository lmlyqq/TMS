package com.rd.client.action.base.vehCapcity;

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
import com.rd.client.common.widgets.SGForm;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveVehCapacityAction implements ClickHandler{

	private ValuesManager form;
	private ListGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private SGForm view;
	
	public SaveVehCapacityAction(ListGrid p_table, ValuesManager p_form, HashMap<String, String> p_map, SGForm view) {
		table = p_table;
		form = p_form;  
		map = p_map;
		this.view = view;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		
        record = form.getValues(); 
        //yuanlei for smartgwt3.0
        //form.getValue("FEE_ID");
        //yuanlei
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
					if(form.getValueAsString("MOBILE1")!=null){
						if(form.getValueAsString("MOBILE1").length()!=11){
							SC.say("手机号码必须为11位");
							return;
						}
					}
					if(form.getValueAsString("PLATE_NO")!=null){
						if( form.getValueAsString("PLATE_NO").length()!=7){
							SC.say("车牌号必须为7位");
							return;
						}
					}
					if(form.getValueAsString("TRAIL_PLATE_NO")!=null){
						if( form.getValueAsString("TRAIL_PLATE_NO").length()!=7){
							SC.say("挂车牌号必须为7位");
							return;
						}
					}
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
					MSGUtil.showOperSuccess();;
					
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
					Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					//刷新选中的记录  //异常
//					table.updateData(table.getSelectedRecord());
					table.redraw();
					//
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					if(view != null)
						view.initSaveBtn();
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
	        	//logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
	        	logList.add(Util.getUpdateLog(select_map, record, table.getDataSource().getTableName()));  //拼装的描述内容
			}
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
			if(record != null) {
				//record.put("TABLE", table.getDataSource().getAttribute("tableName"));
				record.put("TABLE", table.getDataSource().getTableName());
				doInsert(record);
			}
		}
		String dri = "";
		if(ObjUtil.isNotNull(form.getItem("DRIVER1_NAME"))){
			dri = ObjUtil.ifNull(form.getItem("DRIVER1_NAME").getValue().toString(), "");
		}else{
			dri = "";
		}
		String mob = "";
		if(ObjUtil.isNotNull(form.getItem("MOBILE1"))){
			mob = ObjUtil.ifNull(form.getItem("MOBILE1").getValue().toString(), "");
		}else{
			mob = "";
		}
		String where = " where STAFF_NAME='"+dri+"' and MOBILE = '"+mob+"'";
		Util.db_async.getRecord("count(*)", "BAS_STAFF", where, null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				if(result!=null&&result.size()>0){
					if(result.get(0).get("count(*)").toString().equals("0")){
						ArrayList<String> sqlList = new ArrayList<String>();  
				    	StringBuffer sf = new StringBuffer();
				    	sf.append("insert into BAS_STAFF(ID,STAFF_CODE,STAFF_NAME,MOBILE,ORG_ID,STAFF_TYP,ENABLE_FLAG,ADDTIME,ADDWHO) values(");
				    	sf.append("sys_guid(),");
				    	sf.append("'"+form.getItem("DRIVER1_NAME").getValue().toString()+"',");
				    	sf.append("'"+form.getItem("DRIVER1_NAME").getValue().toString()+"',");
				    	sf.append("'"+form.getItem("MOBILE1").getValue().toString()+"',");
				    	sf.append("'A00',");
				    	sf.append("'4D279652C2B9423D8AD958E63B9B3547',");
				    	sf.append("'Y',");
				    	sf.append("sysdate,");
				    	sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"')");
				    	sqlList.add(sf.toString());
						Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
							
							@Override
							public void onSuccess(String result) {
								if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
									MSGUtil.showOperSuccess();
								}else{
									MSGUtil.sayError(result);
								}
							}
							
							@Override
							public void onFailure(Throwable caught) {
								
							}
						});
					}
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
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
	
}
