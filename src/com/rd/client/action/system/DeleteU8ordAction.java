package com.rd.client.action.system;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * U8日志删除按钮
 * @createTime 2011-09-22
 * @author wangJun
 *
 */
public class DeleteU8ordAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	

	private ListGrid table;
	private HashMap<String, String> recMap= null;
	
	public DeleteU8ordAction(ListGrid table){
		this.table = table;
	}
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		click();
	}
	
	private void click() {
		ListGridRecord[] records = table.getSelection();
		
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	del();
                    }
                }
            });
		}else{
			MSGUtil.sayError("请先勾选待删除记录！");
			return;
		}
		
	}
	
	private void del(){
		ListGridRecord[] recordList = table.getSelection();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> id_map = new HashMap<String, String>();
		HashMap<String, String> mid_map = new HashMap<String, String>();
		HashMap<String, String> cust_map = new HashMap<String, String>();
		HashMap<String, String> dtype_map = new HashMap<String, String>();
		
		recMap = new HashMap<String, String>();
		
		if(recordList != null && recordList.length > 0) {
			for(int i=0;i<recordList.length;i++){
				
				String id = recordList[i].getAttribute("ID");
				String mid = recordList[i].getAttribute("MID_ID");
				String customer_name = recordList[i].getAttribute("CUSTOMER_NAME");
				String dtype = recordList[i].getAttribute("DTYPE");
				
				recMap.put(recordList[i].getAttribute("ID"), "1");
				
				id_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(id," ").toString());
				mid_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(mid," ").toString());
				cust_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(customer_name," ").toString());
				dtype_map.put(Integer.toString(i+1), ObjUtil.ifObjNull(dtype," ").toString());
				
			}
			
			listmap.put("1", id_map);
			listmap.put("2", mid_map);
			listmap.put("3", cust_map);
			listmap.put("4", dtype_map);
			String json = Util.mapToJson(listmap);
			Util.async.execProcedure(json, "SP_DEL_U8MID(?,?,?,?,?)", new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
					
				}

				@Override
				public void onSuccess(String result) {
					MSGUtil.showOperSuccess();
					RefreshRecord();
					
				}
			});
				
		}
	}
	
	private void RefreshRecord() {
		RecordList list = table.getRecordList();
		RecordList newList = new  RecordList();
		int row = 0; 
		for(int i = 0; i < list.getLength(); i++) {
			
			
            if(recMap.get(list.get(i).getAttributeAsString("ID")) == null)
            	newList.add(list.get(i));
            else if(i > 0 )
            	row = i - 1 ;
		}
		table.setData(newList);
	
		if(newList.getLength() > 0){
			table.selectRecord(row);
		}
		
	}
}
