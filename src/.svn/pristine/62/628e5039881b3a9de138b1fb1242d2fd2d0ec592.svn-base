package com.rd.client.common.action;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShpmReceiptView;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteTransFollowAction implements ClickHandler {

	private SGTable table;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private TmsShpmReceiptView view;
	private TmsTrackView tView;
	private String shpmNo;
	private String skuId;

	public DeleteTransFollowAction(SGTable p_table,TmsShpmReceiptView view) {
		table = p_table;
		this.view = view;
	}
	public DeleteTransFollowAction(SGTable p_table,TmsTrackView view) {
		table = p_table;
		this.tView = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		if(view != null){
			shpmNo = view.shpment_no;
		}
		if(tView != null){
			shpmNo = tView.shpmTable.getSelectedRecord().getAttribute("SHPM_NO");
		}
		skuId = table.getSelectedRecord().getAttributeAsString("SKU_ID");
		
		ds= table.getDataSource();
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
    	StringBuffer sf = null;
    	String descr = "";                     //仅用作写入用户登录日志
    	for(int i = 0; i < records.length; i++) {
    		ListGridRecord rec = records[i]; 
    		sf = new StringBuffer();
    		sf.append("delete from ");
    		//sf.append(ds.getAttribute("tableName"));
    		sf.append(ds.getTableName());
    		sf.append(" where ");
    		sf.append(ds.getPrimaryKeyFieldName());
    		sf.append(" = '");
    		sf.append(rec.getAttribute(ds.getPrimaryKeyFieldName()));
    		sf.append("'");
    		sqlList.add(sf.toString());
    		
    		//String[] titles = Util.getPropTitle(ds.getAttribute("tableName"));
    		//String[] fields = Util.getPropField(ds.getAttribute("tableName"));
    		String[] titles = Util.getPropTitle(ds.getTableName());
    		String[] fields = Util.getPropField(ds.getTableName());
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
					table.OP_FLAG = "M";
					RefreshRecord();
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG", table.OP_FLAG);
					criteria.addCriteria("SHPM_NO",shpmNo);
					table.invalidateCache();
					table.fetchData(criteria);
					
					ArrayList<String> list= new ArrayList<String>();
					list.add(shpmNo);
					list.add(skuId);
					Util.async.execProcedure(list, "SP_SETT_SAVE_DAMAGE_FEE(?,?,?)", new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
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
		for(int i = 0; i < records.length; i++) {
            table.removeData(records[i]);
            table.deselectAllRecords();
		}
	}
}
