package com.rd.client.action.settlement;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteInvoiceAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler {

	private DynamicForm form;
	private ListGrid table;
	private ListGridRecord[] records;
	public DeleteInvoiceAction(ListGrid p_table, DynamicForm p_form) {
		table = p_table;
		form = p_form;
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
					ArrayList<String> list=new ArrayList<String>();
					String sql1="delete from BILL_REC_INVOICEDETAILS where INVOICE_NO='"+table.getSelectedRecord().getAttribute("INVOICE_NO")+"'";
					list.add(sql1);
					String sql2="delete from BILL_REC_INVOICEINFO where INVOICE_NO='"+table.getSelectedRecord().getAttribute("INVOICE_NO")+"'";
					list.add(sql2);
					String sql3="delete from BILL_REC_RECELOG where INVOICE_NO='"+table.getSelectedRecord().getAttribute("INVOICE_NO")+"'";
					list.add(sql3);
					String sql4="update bill_rec_initdetails set INVOICE_STAT = '10' where init_no ='"+table.getSelectedRecord().getAttribute("INIT_NO")+"'";
					list.add(sql4);
					String sql5="update bill_rec_initial set INVOICE_FLAG = 'N' where init_no ='"+table.getSelectedRecord().getAttribute("INIT_NO")+"'";
					list.add(sql5);
					Util.async.excuteSQLList(list, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {						
							MSGUtil.showOperSuccess();
							form.clearValues();
							RefreshRecord();
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
