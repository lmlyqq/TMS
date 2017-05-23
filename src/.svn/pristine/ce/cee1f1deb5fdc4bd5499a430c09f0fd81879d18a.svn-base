package com.rd.client.action.base.supplier;


import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class DeleteTransAreaAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	

	private SGTable table;
	
	public DeleteTransAreaAction(SGTable table){
		this.table = table;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		// TODO Auto-generated method stub
		click();
	}
	
	private void click() {
		final ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql;
		ListGridRecord[] list = table.getSelection();
		RecordList recordList = table.getDataAsRecordList();
		final RecordList valList = new RecordList();
		HashMap<String, String> keyMap = new HashMap<String, String>();
		for(int i=0;i<list.length;i++){
			keyMap.put(list[i].getAttributeAsString("ID")+list[i].getAttributeAsString("UNLOAD_AREA_ID"), list[i].getAttributeAsString("UNLOAD_AREA_ID"));
			if(ObjUtil.isNotNull(list[i].getAttributeAsString("ID"))){
				sql = new StringBuffer();
				sql.append("delete from bas_suplr_area where ID='");
				sql.append(list[i].getAttributeAsString("ID"));
				sql.append("'");
				sqlList.add(sql.toString());
			}
		}
		
		for(int i=0;i<recordList.getLength();i++){
			Record record = recordList.get(i);
			if(!ObjUtil.isNotNull(keyMap.get(record.getAttributeAsString("ID")+record.getAttributeAsString("UNLOAD_AREA_ID")))){
				valList.add(record);
			}
		}
		
		if(sqlList.size() > 0){
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
	                 if (value != null && value) {
	                	 table.setData(valList);
	                	 doDelete(sqlList);
	                 }
	            }
	        });
		}else{
			table.setData(valList);
		}
	}
	
	private void doDelete(ArrayList<String> sqlList){
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE))
					MSGUtil.showOperSuccess();
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
