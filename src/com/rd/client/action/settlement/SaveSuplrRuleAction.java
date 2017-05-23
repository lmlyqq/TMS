package com.rd.client.action.settlement;

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
import com.rd.client.view.settlement.SuplrRuleView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveSuplrRuleAction implements ClickHandler{
	
	private ValuesManager form;
	private ListGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private SuplrRuleView view;
	
	public SaveSuplrRuleAction(ListGrid p_table, ValuesManager p_form, HashMap<String, String> p_map, SuplrRuleView view) {
		this.view = view;
		table = p_table;
		form = p_form;  
		map = p_map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		String str="LOWER_LMT,UPPER_LMT,BASE_RATE,BASE_AMT,MIN_AMT,MAX_AMT,MIN_UNIT";
		String com_res=checkNumber(str);
		if(com_res.length()>0){
			MSGUtil.sayError(com_res);
			return;
		}
		String op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
		
//		if(!ObjUtil.isNotNull(form.getItem("BIZ_TYP").getValue())){
//			MSGUtil.sayError("业务类型不能为空");
//			return;
//		}
		
		form.clearValue("BIZ_TYP_NAME");
		
        record = form.getValues();
        int num = Integer.parseInt(view.basInfo.getItem("NUM").getValue().toString());
		for(int i=0;i<num;i++){
			if(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getClass().equals(ComboBoxItem.class)) {
				record.put("WHR_UDF" + Integer.toString(i+1), ObjUtil.ifNull(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getDisplayValue(),""));
			}
			else {
				record.put("WHR_UDF" + Integer.toString(i+1), ObjUtil.ifNull(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getValue().toString(),""));
			}
		}
		
        record.remove("BIZ_TYP_NAME");
        form.setValues(record);
        form.getValueAsString("FEE_ID");
        
        
        HashMap<String, String> comb_val = new HashMap<String, String>();
        comb_val.put("FEE_ID", "FEE_NAME");
        comb_val.put("FEE_BASE", "FEE_BASE_NAME");
        comb_val.put("FEE_TYP", "FEE_TYP_NAME");
        
        if(ObjUtil.isNotNull(comb_val)){
        	Object[] obj = comb_val.keySet().toArray();
        	for(int i=0; i<obj.length; i++){
        		String key = obj[i].toString();
        		record.put(comb_val.get(key), form.getValueAsString(comb_val.get(key)));
        	}
        }
        record.remove("OP_FLAG");
        if(id_name != null) {
        	convertNameToId(record, id_name);            
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
				record.put("TFF_ID", view.vm.getItem("TFF_ID").getValue().toString());
				doInsert(record);
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
					MSGUtil.showOperSuccess();
					
					ListGridRecord record = new ListGridRecord();
					record.setAttribute("ID", result.substring(2));
					form.setValue("ID", result.substring(2));
					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
					list.add(0,record);
					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
					//table.selectRecord(record);
					//table.redraw();
					table.selectRecord(record);	
					ArrayList<String> sqlList = new ArrayList<String>();  
					StringBuffer sf = new StringBuffer();
					int num = Integer.parseInt(view.basInfo.getItem("NUM").getValue().toString());
					for(int i=0;i<num;i++){
						sf = new StringBuffer();
						sf.append("insert into TARIFF_CONDITION(ID,RUL_ID,TFF_ID,LEFT_BRKT,OPER_OBJ,OPERATOR,ATTR_VAL,RIGHT_BRKT,LINK,OPER_ATTR,ADDTIME,ADDWHO,EDITTIME,EDITWHO,SHOW_SEQ)");
						sf.append(" values(sys_guid(),");
						sf.append("'"+result.substring(2)+"',");
						sf.append("'"+view.TFFNAME.getItem("TFF_ID").getValue().toString()+"',");
						if(view.basInfo.getItem("LEFT_BRKT"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("LEFT_BRKT"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPERATOR"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						if(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							if(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString().equals("起点城市")){
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("AREA_ID"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}else if(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString().equals("终点城市")){
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("AREA_ID"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}else{
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}
						}
						if(view.basInfo.getItem("RIGHT_BRKT"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("RIGHT_BRKT"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						if(view.basInfo.getItem("LINK"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("LINK"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						if(view.basInfo.getItem("OPER_ATTR"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPER_ATTR"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						sf.append("sysdate,");
						sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
						sf.append("sysdate,");
						sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
						sf.append("'"+(i+1)+"')");
						sqlList.add(sf.toString());
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
								MSGUtil.showOperSuccess();
								form.setValue("OP_FLAG", StaticRef.MOD_FLAG);//wangjun 2010-12-8
								
								if(view != null){
									view.initSaveRuleBtn();
								}
								Criteria cri=new Criteria();
								cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
								cri.addCriteria(view.searchForm.getValuesAsCriteria());
								table.fetchData(cri);
							}else{
								MSGUtil.sayError(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
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
					Util.updateToRecord(form, table, table.getSelectedRecord());   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
					//刷新选中的记录  //异常
					table.updateData(table.getSelectedRecord());
//					table.redraw();
					//
					
					ArrayList<String> sqlList = new ArrayList<String>();  
					StringBuffer sf = new StringBuffer();
					sf.append("delete from TARIFF_CONDITION where RUL_ID ='"+table.getSelectedRecord().getAttribute("ID")+"'");
					sqlList.add(sf.toString());
					int num = Integer.parseInt(view.basInfo.getItem("NUM").getValue().toString());
					for(int i=0;i<num;i++){
						sf = new StringBuffer();
						sf.append("insert into TARIFF_CONDITION(ID,RUL_ID,TFF_ID,LEFT_BRKT,OPER_OBJ,OPERATOR,ATTR_VAL,RIGHT_BRKT,LINK,OPER_ATTR,ADDTIME,ADDWHO,EDITTIME,EDITWHO,SHOW_SEQ)");
						sf.append(" values(sys_guid(),");
						sf.append("'"+table.getSelectedRecord().getAttribute("ID")+"',");
						sf.append("'"+table.getSelectedRecord().getAttribute("TFF_ID")+"',");
						if(view.basInfo.getItem("LEFT_BRKT"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("LEFT_BRKT"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPERATOR"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						if(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							if(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString().equals("起点城市")){
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("AREA_ID"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}else if(view.basInfo.getItem("OPER_OBJ"+Integer.toString(i+1)).getValue().toString().equals("终点城市")){
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("AREA_ID"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}else{
								sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("ATTR_VAL"+Integer.toString(i+1)).getValue().toString(),"")+"',");
							}
						}
						if(view.basInfo.getItem("RIGHT_BRKT"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("RIGHT_BRKT"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						if(view.basInfo.getItem("LINK"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("LINK"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						if(view.basInfo.getItem("OPER_ATTR"+Integer.toString(i+1)).getValue()==null){
							sf.append("'',");
						}else{
							sf.append("'"+ObjUtil.ifNull(view.basInfo.getItem("OPER_ATTR"+Integer.toString(i+1)).getValue().toString(),"")+"',");
						}
						sf.append("sysdate,");
						sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
						sf.append("sysdate,");
						sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
						sf.append("'"+(i+1)+"')");
						sqlList.add(sf.toString());
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
								MSGUtil.showOperSuccess();
								form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
								
								if(view != null){
									view.initSaveRuleBtn();
								}
								Criteria cri=new Criteria();
								cri.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
								cri.addCriteria(view.searchForm.getValuesAsCriteria());
								table.fetchData(cri);
							}else{
								MSGUtil.sayError(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}
					});
					
				}
				else {
					MSGUtil.sayError(result);
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
	
	private String checkNumber(String str){
		boolean flag=false;
		double number = 0;
		String result="";
		HashMap<String,Double> map=new HashMap<String,Double>();
		String[] items=str.split(",");
		for(String item : items){
			try {
				number=Double.valueOf(ObjUtil.ifObjNull(view.isG.getItem(item).getValue(), 0).toString());
				map.put(item, number);
				if(number<0 || number>9999999999.99999999){
					flag=true;
					result += view.isG.getItem(item).getTitle();
					result += ",";
				}
			} catch (Exception e) {
				flag=true;
				result += view.isG.getItem(item).getTitle();
				result += ",";
			}
		}
		if(flag){
			result = result.substring(0,result.length()-1) + " ";
			result += " 内容不合法,";
		}
		result += ObjUtil.isNotNull(map.get("LOWER_LMT")) && ObjUtil.isNotNull(map.get("UPPER_LMT")) ? map.get("UPPER_LMT")>=map.get("LOWER_LMT") ? "" : " 上限值要大于下限值 ," : "";
		result += ObjUtil.isNotNull(map.get("MAX_AMT")) && ObjUtil.isNotNull(map.get("MIN_AMT")) ? map.get("MAX_AMT")>=map.get("MIN_AMT") ? "" : " 最高金额要大于最低金额" : "";
//		double lower_lmt=map.get("LOWER_LMT");
//		double upper_lmt=map.get("UPPER_LMT");
//		double max_amt=map.get("MAX_AMT");
//		double min_amt=map.get("MIN_AMT");
//		result += upper_lmt >= lower_lmt ? "" : "上限值要大于下限值 ,";
//		result += max_amt >= min_amt ? "" : " 最高金额要大于最低金额";
//		return result;
		
		return result;
		
	}

}