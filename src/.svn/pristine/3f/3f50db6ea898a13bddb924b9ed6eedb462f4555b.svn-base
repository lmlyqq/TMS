package com.rd.client.action.tms.changerecord;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteChangeAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private ValuesManager form;
	private SGTable table;
	private ListGridRecord[] records;
	public DeleteChangeAction(SGTable table, ValuesManager form) {
		this.table = table;
		this.form = form;
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

	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
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
    	//String tableName = table.getDataSource().getAttribute("tableName");
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
					form.clearValues();
					RefreshRecord();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		ListGridRecord[] recordAlls = table.getRecords();
		int pos = recordAlls.length - records.length;
		if(pos > 0){
			if(records.length > 0){
				ArrayList<ListGridRecord> newRecords = new ArrayList<ListGridRecord>();
				for (ListGridRecord listGridRecord : records) {
					recordAlls[table.getRecordIndex(listGridRecord)] = null;
				}
				for (ListGridRecord listGridRecord : recordAlls) {
					if(listGridRecord != null){
						newRecords.add(listGridRecord);
					}
				}
				table.setRecords((ListGridRecord[])newRecords.toArray(new ListGridRecord[newRecords.size()])); 
				table.redraw();
				table.selectRecord(pos-1);
			}
		}else{
			table.setRecords(new ListGridRecord[0]);
			table.redraw();
		}
		
	}
	
}
