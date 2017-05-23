package com.rd.client.common.action;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteAction2 implements ClickHandler {

	private SGTable table;
	//private SGTable detail_table;
	//private String PKEY;
	private DataSource ds;
	private ListGridRecord[] records = null;
	private HashMap<String, String> recMap= null;
	private int initBtn = 0;
	private SGForm view;
	public DeleteAction2(SGTable p_table) {
		table = p_table;
	}
	public DeleteAction2(SGTable p_table,SGForm view,int initBtn) {
		table = p_table;
		this.initBtn = initBtn;
		this.view = view;
	}
	public DeleteAction2(SGTable p_maintable, SGTable p_detailTable, String p_key) {
		table = p_maintable;
		//detail_table = p_detailTable;
		//PKEY = p_key;
	}
	
	public DeleteAction2(SGTable p_maintable, SGTable p_detailTable, String p_key,SGForm view) {
		table = p_maintable;
		//detail_table = p_detailTable;
		//PKEY = p_key;
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
		
		Record record_new = new Record();
		LinkedHashMap<String,String> map = null;
		for(int i = 0; i < records.length; i++) {
			Record record = records[i];
			map = new LinkedHashMap<String,String>();
			map.put(ds.getPrimaryKeyFieldName(), record.getAttribute(ds.getPrimaryKeyFieldName()));
			//object2.put(Integer.toString(i+1), record);
			record_new.setAttribute(Integer.toString(i+1), map);
		}
		table.removeData(record_new,new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				// TODO Auto-generated method stub
				if(response.getStatus() == 0) {				
						MSGUtil.showOperSuccess();
						table.OP_FLAG = "M";
						RefreshRecord();
				}
				else {
					MSGUtil.sayError(response.getErrors().get("errorMessage").toString());
				}
			}
			
		});
		
    	/*ArrayList<String> sqlList = new ArrayList<String>();  
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
	        	String tableName = detail_table.getDataSource().getTableName();
	        	if(ObjUtil.isNotNull(tableName) && ObjUtil.isNotNull(PKEY)) {
	        		sf = new StringBuffer();
	        		sf.append(" delete from ");
	        		sf.append(tableName);
	        		sf.append(" where ");
	        		sf.append(PKEY);
	        		sf.append(" = '");
	        		sf.append(rec.getAttribute(PKEY));
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
					table.OP_FLAG = "M";
					RefreshRecord();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});*/
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
			if(view != null && initBtn ==0){
				view.enableOrDisables(view.del_map, false);
			}else if(view != null && initBtn > 0){
				view.initBtn(initBtn);
			}
				
		}

	}
}
