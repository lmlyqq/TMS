package com.rd.client.action.base.gpseq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasGpsEqView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SaveDetailAction implements ClickHandler{

	private ValuesManager form;
	private ListGrid table;
	private HashMap<String, String> map;
	private Map<String, String> record;
	private Map<String, String> id_name;
	private ArrayList<String> logList;  //日志信息
	private BasGpsEqView view;
	private int initBtn =0;
	
	public SaveDetailAction(ListGrid p_table,ValuesManager p_form, HashMap<String, String> p_map, BasGpsEqView view,int initBtn) {
		this.table = p_table;
		this.form = p_form;  
		this.map = p_map;
		this.view = view;
		this.initBtn = initBtn;
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
			if(view.ABNORMAL_FLAG.getValue().toString().equals("true")){
				if(!ObjUtil.isNotNull(view.ABNORMAL_REASON.getValue())){
					SC.say("勾选设备异常,必须填写异常原因");
					return;
				}
			}
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
//		String json = Util.mapToJson(map);
//		
//		//设置日志信息
//		String[] titles = Util.getPropTitle(table.getDataSource().getTableName());
//		String[] fields = Util.getPropField(table.getDataSource().getTableName());
//		String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
		//设置完毕
		
		ArrayList<String> sqlList1 = new ArrayList<String>();
		StringBuffer sf = new StringBuffer();
		ListGridRecord[] records = view.gpsTable.getSelection();
		for(int i=0;i<records.length;i++){
			sf = new StringBuffer();
			sf.append("insert into BAS_GPS_RECLAIM(ID,EQUIP_NO,CLAIM_TIME,ABNORMAL_FLAG,");
			sf.append("ABNORMAL_REASON,DUTY_TO,AB_NOTES,ADDTIME,ADDWHO,EDITTIME,EDITWHO)");
			sf.append(" values(");
			sf.append("sys_guid(),");
			sf.append("'"+records[i].getAttribute("ID")+"',");
			sf.append("to_date('"+form.getItem("CLAIM_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
			if(form.getItem("ABNORMAL_FLAG").getValue().toString().equals("true")){
				sf.append("'Y',");
			}else{
				sf.append("'N',");
			}
			sf.append("'"+form.getItem("ABNORMAL_REASON").getValue()+"',");
			sf.append("'"+form.getItem("DUTY_TO").getValue()+"',");
			if(ObjUtil.isNotNull(form.getItem("AB_NOTES").getValue())){
				sf.append("'"+form.getItem("AB_NOTES").getValue()+"',");
			}else{
				sf.append("'',");
			}
			sf.append("sysdate,");
			sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
			sf.append("sysdate,");
			sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"')");
			sqlList1.add(sf.toString());
		}
		for(int j=0;j<records.length;j++){
			sf = new StringBuffer();
			sf.append("update BAS_GPSEQ set STATUS='DF252F0637784E9EA575CCACB64050FC',");
			sf.append(" LOAD_NO='',PLATE_NO='' ");
			sf.append("where EQUIP_NO='"+records[j].getAttribute("EQUIP_NO")+"'");
			sqlList1.add(sf.toString());
		}
		//System.out.println(sqlList1);
		Util.async.excuteSQLList(sqlList1, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				MSGUtil.showOperSuccess();
				ListGridRecord[] records = view.gpsTable.getSelection();
				for(int i=0;i<records.length;i++){
					records[i].setAttribute("STATUS", "DF252F0637784E9EA575CCACB64050FC");
					records[i].setAttribute("LOAD_NO", "");
					records[i].setAttribute("PLATE_NO", "");
				}
				view.gpsTable.redraw();
				table.invalidateCache();
				table.discardAllEdits();
				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG","M");
				findValues.addCriteria("EQUIP_NO", view.gpsTable.getSelectedRecord().getAttribute("ID").toString());
				table.fetchData(findValues);
				if(view != null){
					view.initSaveBtn();
				}
				if(initBtn > 0){
					view.initBtn(initBtn);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
//		Util.async.doInsert(descr, json, new AsyncCallback<String>() {
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//				MSGUtil.sayError(caught.getMessage());
//			}
//	
//			@Override
//			public void onSuccess(String result) {
//				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)) {
//					MSGUtil.showOperSuccess();;
//					
//					ListGridRecord record = new ListGridRecord();
//					record.setAttribute("ID", result.substring(2));
//					form.setValue("ID", result.substring(2));
//					Util.updateToRecord(form, table, record);   //将FORM的值更新到Record,用户操作成功时刷新和重定位数据
//					ArrayList list = new ArrayList(Arrays.asList(table.getRecords()));
//					list.add(0,record);
//					table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
//					table.redraw();
//					table.selectRecord(record);	
//					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);//wangjun 2010-12-8
//							if(view != null){
//								view.initSaveBtn();
//							}
//							if(initBtn > 0){
//								view.initBtn(initBtn);
//							}
//				}
//				else {
//					MSGUtil.sayError(result);
//				}
//			}
//		});
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
					table.redraw();
					form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
					ArrayList<String> sqlList1 = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					sf.append("update BAS_GPSEQ set STATUS='DF252F0637784E9EA575CCACB64050FC',");
					sf.append(" LOAD_NO='',PLATE_NO='' ");
					sf.append("where EQUIP_NO='"+view.gpsTable.getSelectedRecord().getAttribute("EQUIP_NO")+"'");
					sqlList1.add(sf.toString());
					//System.out.println(sqlList1);
					Util.async.excuteSQLList(sqlList1, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							
							MSGUtil.showOperSuccess();
							view.gpsTable.getSelectedRecord().setAttribute("STATUS", "DF252F0637784E9EA575CCACB64050FC");
							view.gpsTable.getSelectedRecord().setAttribute("LOAD_NO", "");
							view.gpsTable.getSelectedRecord().setAttribute("PLATE_NO", "");
							view.gpsTable.redraw();
							Criteria findValues = new Criteria();
							findValues.addCriteria("OP_FLAG","M");
							findValues.addCriteria("EQUIP_NO", view.gpsTable.getSelectedRecord().getAttribute("ID").toString());
							table.fetchData(findValues);
							if(view != null){
								view.initSaveBtn();
							}
							if(initBtn > 0){
								view.initBtn(initBtn);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
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
