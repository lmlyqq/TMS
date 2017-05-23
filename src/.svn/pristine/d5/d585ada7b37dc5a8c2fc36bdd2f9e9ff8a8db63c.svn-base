package com.rd.client.action.base.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tab.TabSet;

/**
 * 路线二级窗口的保存按钮
 * @author yuanlei
 *
 */
public class SaveSubAction implements ClickHandler {

	private SGTable table = null;
	private SGTable detail_table = null;
	private DataSource ds = null;
	private TabSet tabSet;
	private ArrayList<Record> record = null; 
	private int[] edit_rows = null;               //列表中所有被编辑过的行号
	private HashMap<String, String> map;
	private ArrayList<String> logList;   //日志信息
	private String customerid;
	private String headid;
	
	public SaveSubAction(SGTable p_table, TabSet p_tabSet, HashMap<String, String> cache_map) {
		table = p_table;
		tabSet = p_tabSet;
		map = cache_map;
	}
	public SaveSubAction(SGTable p_table, SGTable p_detail, TabSet p_tabSet, HashMap<String, String> cache_map) {
		table = p_table;
		tabSet = p_tabSet;
		map = cache_map;
		detail_table = p_detail;
		ds = detail_table.getDataSource();
	}
	
	public SaveSubAction(SGTable p_table, SGTable p_detail, TabSet p_tabSet, HashMap<String, String> cache_map ,String customerid,String headid) {
		table = p_table;
		tabSet = p_tabSet;
		map = cache_map;
		detail_table = p_detail;
		ds = detail_table.getDataSource();
		this.customerid = customerid;
		this.headid = headid;
	}

	@Override
	public void onClick(ClickEvent event) {
		//判断客户是否为空，空，不做校验，不空，则校验客户和卸货点
		final ListGridRecord[] recs = table.getRecords();
		if (ObjUtil.isNotNull(customerid)) {
			//校验唯一性
			StringBuffer sf = new StringBuffer();
			for(ListGridRecord rec : recs){
				sf.append("'");
				sf.append(rec.getAttribute("ADDR_ID"));
				sf.append("',");
			}
			sf = sf.length() == 0 ? new StringBuffer("''") : new StringBuffer(sf.substring(0,sf.length()-1));
			StringBuffer sb = new StringBuffer();
			sb.append("select count(1) from bas_route_head t1 ,bas_route_detail t2 where t1.id = t2.route_id " +
					" and t1.id <> '"+headid+"' and t1.CUSTOMER_ID = '"+customerid+"' and t2.addr_id in ("+sf+")");
			Util.async.queryData(sb.toString(), true, new AsyncCallback<Map<String,Object>>() {
				
				@Override
				public void onSuccess(Map<String, Object> result) {
					Object obj = result.get("singleData");
					if (Integer.valueOf(obj.toString()) > 0) {
						MSGUtil.sayWarning("该客户的一条线路下已经存在该地址点，是否继续保存?");
						SC.confirm("该客户的一条线路下已经存在该地址点，是否继续保存?", new BooleanCallback() {
							
							@Override
							public void execute(Boolean value) {
								if (value != null && value) {
									doOpera(recs);
								}
							}
						});
					}else {
						doOpera(recs);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
				}
			});
		}else {
			doOpera(recs);
		}
	}
	
	private void doOpera(final ListGridRecord[] recs){
		try {
			if(tabSet.getSelectedTabNumber() == 0) {
				//中途点
				//if(records != null && records.length > 0) {
					doHalfwayInsert(recs);
				//}
			}
			else if(tabSet.getSelectedTabNumber() == 1) {
				//路段信息
				doDetailUpdate();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 中途点的保存功能
	 * @author yuanlei
	 * @param records
	 */
	private void doHalfwayInsert(ListGridRecord[] records) {
		ListGridRecord rec = null;
		HashMap<String, String> sqlMap = new HashMap<String, String>();
		String start_area_id = "", start_area_name = "", end_area_id = "", end_area_name = "", area_id = "", area_name = "";
		int show_seq = 0;
		ArrayList<String> sqlList = new ArrayList<String>();
        logList = new ArrayList<String>();
		for(int i = 0; i < records.length; i++) {
			rec = records[i];
			if(i == 0) {
				show_seq++;
				start_area_id = map.get("START_AREA_ID");
				start_area_name = map.get("START_AREA_NAME");
				end_area_id = rec.getAttributeAsString("ID");
				end_area_name = rec.getAttributeAsString("AREA_NAME");
				area_id = end_area_id;
				area_name = end_area_name;

				sqlMap = new HashMap<String, String>();
				sqlMap.put("TABLE", "BAS_ROUTE_DETAIL");
				sqlMap.put("ROUTE_ID", map.get("ROUTE_ID"));
				sqlMap.put("AREA_ID",area_id);
				sqlMap.put("AREA_NAME", area_name);
				sqlMap.put("START_AREA_ID", start_area_id);
				sqlMap.put("START_AREA_NAME", start_area_name);
				sqlMap.put("END_AREA_ID", end_area_id);
				sqlMap.put("END_AREA_NAME", end_area_name);
				sqlMap.put("TRANS_SRVC_ID", map.get("TRANS_SRVC_ID"));
				//sqlMap.put("EXEC_ORG_ID", map.get("EXEC_ORG_ID"));
				sqlMap.put("SHOW_SEQ", Integer.toString(show_seq));
				sqlMap.put("ENABLE_FLAG", "Y");
				if(ObjUtil.isNotNull(rec.getAttributeAsString("ADDR_ID"))) {
					sqlMap.put("ADDR_ID", rec.getAttributeAsString("ADDR_ID"));
					sqlMap.put("ADDRESS", rec.getAttributeAsString("ADDRESS"));
					sqlMap.put("EXEC_ORG_ID", rec.getAttributeAsString("EXEC_ORG_ID"));
				}
				sqlList.add(Util.mapToJson(sqlMap));
			}
			else {
				show_seq++;
				start_area_id = end_area_id;
				start_area_name = end_area_name;
				end_area_id = rec.getAttributeAsString("ID");
				end_area_name = rec.getAttributeAsString("AREA_NAME");
				area_id = end_area_id;
				area_name = end_area_name;
				
				sqlMap = new HashMap<String, String>();
				sqlMap.put("TABLE", "BAS_ROUTE_DETAIL");
				sqlMap.put("ROUTE_ID", map.get("ROUTE_ID"));
				sqlMap.put("AREA_ID",area_id);
				sqlMap.put("AREA_NAME", area_name);
				sqlMap.put("START_AREA_ID", start_area_id);
				sqlMap.put("START_AREA_NAME", start_area_name);
				sqlMap.put("END_AREA_ID", end_area_id);
				sqlMap.put("END_AREA_NAME", end_area_name);
				sqlMap.put("TRANS_SRVC_ID", map.get("TRANS_SRVC_ID"));
				//sqlMap.put("EXEC_ORG_ID", map.get("EXEC_ORG_ID"));
				sqlMap.put("SHOW_SEQ", Integer.toString(show_seq));
				sqlMap.put("ENABLE_FLAG", "Y");
				if(ObjUtil.isNotNull(rec.getAttributeAsString("ADDR_ID"))) {
					sqlMap.put("ADDR_ID", rec.getAttributeAsString("ADDR_ID"));
					sqlMap.put("ADDRESS", rec.getAttributeAsString("ADDRESS"));
					sqlMap.put("EXEC_ORG_ID", rec.getAttributeAsString("EXEC_ORG_ID"));
				}
				sqlList.add(Util.mapToJson(sqlMap));
			}
			/*if(i == records.length - 1){
				show_seq++;
				start_area_id = end_area_id;
				start_area_name = end_area_name;
				end_area_id = map.get("END_AREA_ID");
				end_area_name = map.get("END_AREA_NAME");
				area_id = null;
				area_name = null;
				
				sqlMap = new HashMap<String, String>();
				sqlMap.put("TABLE", "BAS_ROUTE_DETAIL");
				sqlMap.put("ROUTE_ID", map.get("ROUTE_ID"));
				sqlMap.put("AREA_ID",area_id);
				sqlMap.put("AREA_NAME", area_name);
				sqlMap.put("START_AREA_ID", start_area_id);
				sqlMap.put("START_AREA_NAME", start_area_name);
				sqlMap.put("END_AREA_ID", end_area_id);
				sqlMap.put("END_AREA_NAME", end_area_name);
				sqlMap.put("TRANS_SRVC_ID", map.get("TRANS_SRVC_ID"));
				sqlMap.put("EXEC_ORG_ID", map.get("EXEC_ORG_ID"));
				sqlMap.put("SHOW_SEQ", Integer.toString(show_seq));
				sqlMap.put("ENABLE_FLAG", "Y");
				sqlList.add(Util.mapToJson(sqlMap));
			}*/
			//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
			//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
			String[] titles = Util.getPropTitle(ds.getTableName());
			String[] fields = Util.getPropField(ds.getTableName());
			String descr = StaticRef.ACT_INSERT + titles[0] + "【" + map.get(fields[0]) + "】";
	        logList.add(descr);  //拼装的描述内容
		}
		ArrayList<String> extra = new ArrayList<String>();
		StringBuffer sf = new StringBuffer();
		sf.append("delete from BAS_ROUTE_DETAIL WHERE ROUTE_ID = '");
		sf.append(map.get("ROUTE_ID"));
		sf.append("'");
		extra.add(sf.toString());
		
		/*sf = new StringBuffer();
		sf.append("update BAS_ROUTE_DETAIL set BAS_ROUTE_DETAIL.exec_org_id = (");
		sf.append(" select exec_org_id from bas_address where BAS_ROUTE_DETAIL.addr_id = bas_address.id");
		sf.append(") where BAS_ROUTE_DETAIL.route_id = '");
		sf.append(map.get("ROUTE_ID"));
		sf.append("'");
		extra.add(sf.toString());*/
		Util.async.doInsert(logList, sqlList, extra, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}

			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					
					Criteria crit = new Criteria();
					detail_table.invalidateCache();
					crit.addCriteria("OP_FLAG", detail_table.OP_FLAG);
					crit.addCriteria("ROUTE_ID", map.get("ROUTE_ID"));
					detail_table.fetchData(crit);
					//tabSet.setSelectedTab(1);
					//tabSet.redraw();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	private void doDetailUpdate() {
		
		ds= detail_table.getDataSource();
		
		edit_rows = detail_table.getAllEditRows();   //获取所有修改过的记录行
		               
		if(edit_rows != null && edit_rows.length > 0) {
			record = new ArrayList<Record>();                               //--修改     

			HashMap<String, String> valueMap;
	        logList = new ArrayList<String>();
	        HashMap<String, String> ck_map = null;
			for(int i = 0; i < edit_rows.length; i++) {
				Record curRecord = detail_table.getEditedRecord(edit_rows[i]);  //获取所有修改的记录
				record.add(curRecord);
				valueMap = (HashMap<String, String>)detail_table.getEditValues(edit_rows[i]);                //获取记录修改过的值
				if(valueMap != null) {
					ArrayList<String> sqlList = new ArrayList<String>();
					if(valueMap != null) {
						//valueMap.put("TABLE", ds.getAttribute("tableName"));
						valueMap.put("TABLE", ds.getTableName());
						//valueMap.put("EDITWHO", LoginCache.getLoginUser().getUSER_ID());
						String json = Util.mapToJson(valueMap);
						sqlList.add(json);
						
						ck_map = Util.putRecordToModel(table.getRecord(edit_rows[i]));
				        //logList.add(Util.getUpdateLog(ck_map, map, table.getDataSource().getAttribute("tableName")));  //拼装的描述内容
				        logList.add(Util.getUpdateLog(ck_map, map, table.getDataSource().getTableName()));  //拼装的描述内容
					}
					doUpdate(sqlList);
				}
			}
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
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
}
