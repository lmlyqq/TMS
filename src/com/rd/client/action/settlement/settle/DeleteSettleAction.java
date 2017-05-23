package com.rd.client.action.settlement.settle;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteSettleAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler {

	private DynamicForm form;
	private ListGrid table;
	private ListGridRecord[] records;
	public DeleteSettleAction(ListGrid p_table, DynamicForm p_form) {
		table = p_table;
		form = p_form;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		records = table.getRecords();
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
		records = table.getRecords();
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
    		
    		sf = new StringBuffer();
    		sf.append("update bill_detail_pay set sett_no='' where sett_no='");
    		sf.append(records[i].getAttribute("SETT_NO"));
    		sf.append("'");
    		sqlList.add(sf.toString());
    		
    		sf = new StringBuffer();
    		sf.append("update bill_detail_rec set sett_no='' where sett_no='");
    		sf.append(records[i].getAttribute("SETT_NO"));
    		sf.append("'");
    		sqlList.add(sf.toString());
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
					Criteria criteria = table.getCriteria();
					table.invalidateCache();
					if(criteria == null) {
						criteria = new Criteria();
					}
					criteria.addCriteria("OP_FLAG", "M");
					table.filterData(criteria);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		//for(int i = 0; i < records.length; i++) {
            table.removeData(table.getSelectedRecord());
            table.deselectAllRecords();
            
            table.selectRecord(table.getRecord(0));
		//}
	}


}
