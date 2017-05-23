package com.rd.client.action.tms;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteTrackAction implements ClickHandler {

	private SGTable table;
	private SGTable detail_table;
	private String PKEY;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private HashMap<String, String> recMap= null;
	private TmsTrackView view;
	
	public DeleteTrackAction(SGTable p_table,TmsTrackView view) {
		this.table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String rowNum = JSOHelper.getAttribute(table.getJsObj(), "selectedRowNum");
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
		}else if(ObjUtil.isNotNull(rowNum)){
			table.discardAllEdits(new int[]{Integer.parseInt(rowNum)}, false);
			table.setProperty("selectedRowNum", "");
		}
	}

	private void doDelete() {
    	ArrayList<String> sqlList = new ArrayList<String>();  
    	ArrayList<String> descrList = new ArrayList<String>();//仅用作写入用户登录日志
    	String descr = "";                     //仅用作写入用户登录日志
    	StringBuffer sf = null;
    	recMap = new HashMap<String, String>();
    	for(int i = 0; i < records.length; i++) {
    		recMap.put(records[i].getAttributeAsString("ID"), "1");
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
    		
    		if(detail_table != null) {
	        	//String tableName = detail_table.getDataSource().getAttribute("tableName");
	        	
	        	String tableNames = table.getDataSource().getTableName();
	        	if(ObjUtil.isNotNull(tableNames) && ObjUtil.isNotNull(PKEY)) {
	        		sf = new StringBuffer();
	        		sf.append(" delete from ");
	        		sf.append(tableNames);
	        		sf.append(" where ");
	        		sf.append(PKEY);
	        		sf.append(" = '");
	        		sf.append(rec.getAttribute(PKEY));
	        		sf.append("'");
	        	System.out.println("SQL:::"+sf);
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
					table.OP_FLAG = "M";
					RefreshRecord();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		RecordList list = table.getRecordList();
		RecordList newList = new  RecordList();
		int row = 0; //fanglm 2011-3-3 删除记录后定位至上一条记录
		for(int i = 0; i < list.getLength(); i++) {
            if(recMap.get(list.get(i).getAttributeAsString("ID")) == null)
            	newList.add(list.get(i));
            else if(i > 0 )
            	row = i - 1 ;
		}
		table.setData(newList);
	
		if(newList.getLength() > 0){
			table.selectRecord(row);
		}else{
			view.enableOrDisables(view.add_tr_map, true);
			view.enableOrDisables(view.save_tr_map, false);
			view.enableOrDisables(view.del_tr_map, true);
		}

	}
	
}