package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasSupplierView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class BlackAction implements ClickHandler {
	private ValuesManager value;
	private ListGrid table;
	private Map<String, String> map;
	private BasSupplierView view;
	
	public BlackAction(){
		
	}

	public BlackAction(ListGrid table, ValuesManager value) {
		this.value = value;
		this.table = table;
	}
	
	public BlackAction(ListGrid table, ValuesManager value,BasSupplierView view) {
		this.value = value;
		this.table = table;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
        map = value.getValues();
        if(map!=null){
        	
        	SC.confirm("确定取消黑名单?", new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if(value!=null&&value){
					 doBlack();	
					}
					
				}
			});
        }
	}
	
	private void doBlack(){
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sf = new StringBuffer();
		//String tableName = table.getDataSource().getAttribute("tableName");
		String tableName = table.getDataSource().getTableName();
	
			sf.append("update ");
			sf.append(tableName);
			sf.append(" set BLACKLIST_FLAG='N");
			sf.append("' where ID='");
			sf.append(map.get("ID")+"'");
			sqlList.add(sf.toString());
			
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
					   MSGUtil.sayInfo("取消黑名单操作成功!");
					   ListGridRecord record = table.getSelectedRecord();
					   //record.setAttribute("VEHICLE_STAT", "D5595E8BF25A4E0D8C46796B772FB1BA");
					   record.setAttribute("BLACKLIST_FLAG", false);
					  // record.setAttribute("VEH_LOCK_REASON"," " );
					   //record.setAttribute("REASON", " ");
					   //value.setValue("VEH_LOCK_REASON", " ");
					   //value.setValue("REASON", " ");
					   value.setValue("BLACKLIST_FLAG", false);
					   table.redraw();
					   view.blackButton.disable();
					   view.blackzeButton.enable();
					 
					   
					}else{
						MSGUtil.sayError("取消黑名单操作失败!");
					}
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
					
				}
			});
		
		
		
	}
//	private void refreshResult(){
//		table.removeData(table.getSelectedRecord());
//		table.deselectAllRecords();
//	}

}
