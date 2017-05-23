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
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.obj.system.SYS_USER;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 通用的FORM保存方法
 * @author yuanlei
 *
 */
public class AccidentSaveAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private DynamicForm form;
	private SGTable table;
	private SGTable ptable;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	//private SGForm view;
	private Map<String,String> comb_val;
	
	public AccidentSaveAction(SGTable p_table, DynamicForm p_form, HashMap<String, String> p_map, Map<String, String > p_corr) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
	}
	
	public AccidentSaveAction(SGTable p_table,SGTable c_table, DynamicForm p_form, HashMap<String, String> p_map, SGForm view) {
		table = c_table;
		ptable = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
//		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
//		/this.view = view;
	}
	public AccidentSaveAction(SGTable p_table, DynamicForm p_form, HashMap<String, String> p_map, SGForm view,Map<String,String> comb_val) {
		table = p_table;
		form = p_form;
		map = p_map;        //系统校验的MAP
//		id_name = p_corr;   //ID和NAME的对应关系，适用于有ID和NAME对应关系，前台显示NAME值的文本
		//this.view = view;
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
		ListGridRecord rec=ptable.getSelectedRecord();
		String id=rec.getAttribute("ID");
		record = form.getValues();
		record.put("INSUR_ID", id);
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
	public void doInsert(Map map){
		final String Status= ObjUtil.ifObjNull(map.get("STATUS"),"").toString();
		final String INSUR_ID= ObjUtil.ifObjNull(map.get("INSUR_ID"),"").toString();
		final int LOSS_AMOUNT=Integer.parseInt((String.valueOf(map.get("LOSS_AMOUNT"))));
		final int CLAIM_AMOUNT=Integer.parseInt((String.valueOf(map.get("CLAIM_AMOUNT"))));
		final int SETTLE_AMOUNT=Integer.parseInt((String.valueOf(map.get("SETTLE_AMOUNT"))));
		SYS_USER user=LoginCache.getLoginUser();
		String userID=user.getUSER_ID();
		
		String DESCR= ObjUtil.ifObjNull(map.get("DESCR"),"").toString();
		
		String LOSS_DESCR= ObjUtil.ifObjNull(map.get("LOSS_DESCR"),"").toString();
		
		String sql1="insert into INSUR_ACCIDENT_LOG(ID,INSUR_ID,STATUS,DESCR,LOSS_DESCR,LOSS_AMOUNT,CLAIM_AMOUNT,SETTLE_AMOUNT,ADDTIME,ADDWHO) select sys_guid(),'"+INSUR_ID+"','"+Status+"','"+DESCR+"','"+LOSS_DESCR+"',"+LOSS_AMOUNT+","+CLAIM_AMOUNT+","+SETTLE_AMOUNT+",sysdate,'"+userID+"' from dual";		
		ArrayList<String> sqlList = new ArrayList<String>();
		sqlList.add(sql1);
		if(Status.equals("AB35208265FF4F1DB9A7360989F515FD")||Status.equals("830FDC7FDF02471CBB21D9A60E9C8E23")||Status.equals("8DE2115FE04F427B8511E441F6B8E892")){
		String sql="";
	      if(Status.equals("AB35208265FF4F1DB9A7360989F515FD")){
	        sql="update INSUR_ACCIDENT_LIST set LOSS_AMOUNT="+LOSS_AMOUNT+" where ID='"+INSUR_ID+"'";
	      }else if(Status.equals("830FDC7FDF02471CBB21D9A60E9C8E23")){
	        sql="update INSUR_ACCIDENT_LIST set CLAIM_AMOUNT="+CLAIM_AMOUNT+" where ID='"+INSUR_ID+"'";
	      }else if(Status.equals("8DE2115FE04F427B8511E441F6B8E892")){
	        sql="update INSUR_ACCIDENT_LIST set SETTLE_AMOUNT="+SETTLE_AMOUNT+" where ID='"+INSUR_ID+"'";
	      }
	        sqlList.add(sql);

			}		
        Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				if(result.equals(StaticRef.SUCCESS_CODE)){
					Criteria findValues = new Criteria();
		            findValues.addCriteria("INSUR_ID", ptable.getSelectedRecord().getAttribute("ID"));
		            findValues.addCriteria("OP_FLAG",table.OP_FLAG);
		            table.PKEY = "ID";
		            table.PVALUE =  ptable.getSelectedRecord().getAttribute("ID");
		            table.invalidateCache();
		            table.fetchData(findValues);
		            table.redraw();
					MSGUtil.sayInfo("更新操作成功");
				}else{
				    MSGUtil.sayError("更新操作失败");
				}
				
				 
				 					
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
				
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
					Criteria findValues = new Criteria();
		            findValues.addCriteria("INSUR_ID", ptable.getSelectedRecord().getAttribute("ID"));
		            findValues.addCriteria("OP_FLAG",table.OP_FLAG);
		            table.PKEY = "ID";
		            table.PVALUE =  ptable.getSelectedRecord().getAttribute("ID");
		            table.invalidateCache();
		            table.fetchData(findValues);
		            table.redraw();
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
