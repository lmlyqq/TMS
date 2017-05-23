package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteApproveAction implements ClickHandler{

	
	private ValuesManager form;
	private ListGrid table;
	private ListGridRecord[] records;
	private HashMap<String, String> map;
	public DeleteApproveAction(ListGrid p_table, ValuesManager p_form) {
		table = p_table;
		form = p_form;
		this.map = null;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}
	}

	
	private void doDelete() {
    	ArrayList<String> sqlList = new ArrayList<String>();
    	ArrayList<String> descrList = new ArrayList<String>();//仅用作写入用户登录日志
    	String descr = "";                     //仅用作写入用户登录日志
    	StringBuffer sf = null;
    	String tableName = table.getDataSource().getTableName();
    	String pkey = table.getDataSource().getPrimaryKeyFieldName();
    	for(int i = 0; i < records.length; i++) {
    		ListGridRecord rec = records[i]; 
    		sf = new StringBuffer();
    		sf.append("delete from ");
    		sf.append(tableName);
    		sf.append(" where ");
    		sf.append(pkey);
    		sf.append(" = '");
    		sf.append(rec.getAttribute(pkey));
    		sf.append("'");
    		sqlList.add(sf.toString());
    		
    		String[] titles = Util.getPropTitle(tableName);
    		String[] fields = Util.getPropField(tableName);
    		descr = StaticRef.ACT_DELETE + titles[0] + "【" + rec.getAttribute(fields[0]) + "】";
    		descrList.add(descr);
    		
    		if(map != null){  //多从表删除
    			for(String table:map.keySet()){ //循环解析map
    				sf = new StringBuffer();
    				sf.append("delete from ");
    				sf.append(table);
    				sf.append(" where ");
    				sf.append(map.get(table));
    				sf.append(" = '");
    				sf.append(rec.getAttribute(pkey));
    				sf.append("'");
    				sqlList.add(sf.toString());
    				
    			}
    		}
    	}
    	sf = null;
    	
    	Util.async.doDelete(descrList, sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					sf.append("delete from SYS_APPROVE_SET where HEAD_ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
					sqlList.add(sf.toString());
					StringBuffer sf1 = new StringBuffer();
					sf1.append("delete from SYS_APPROVE_CUSTOMER where HEAD_ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
					sqlList.add(sf1.toString());
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								MSGUtil.showOperSuccess();
							}
							else {
								MSGUtil.sayError(result);
							}
						}
						
					});
					form.clearValues();
					RefreshRecord();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void RefreshRecord() {
		ListGridRecord[] lstRecords = table.getRecords();
		ArrayList<ListGridRecord> list = new ArrayList(Arrays.asList(lstRecords));
		int index = 0;
		if(records != null && records.length > 0) {
			for(int i = 0; i < records.length; i++) {
				int pos = table.getRecordIndex(records[i]);
				list.remove(pos-i);
				if(i == 0) {
					index = pos;
				}
			}
		}
		table.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()])); //wangjun
		table.redraw();
		if(table.getRecords().length > 0) {
			if (index > 0) 
				table.selectRecord(index - 1);
			else 
				table.selectRecord(0);
		}else{
			form.clearValues();
		}
	}
	
}
