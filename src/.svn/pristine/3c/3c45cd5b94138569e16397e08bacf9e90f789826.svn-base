package com.rd.client.common.action;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasVehCapacityView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FreeAction implements ClickHandler {
	private ValuesManager value;
	private ListGrid table;
	private Map<String, String> map;
	private BasVehCapacityView view;
	
	public FreeAction(){
		
	}

	public FreeAction(ListGrid table, ValuesManager value) {
		this.value = value;
		this.table = table;
	}
	
	public FreeAction(ListGrid table, ValuesManager value,BasVehCapacityView view) {
		this.value = value;
		this.table = table;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
        map = value.getValues();
        if(map!=null){
        	if(!"40BC1270A5B240E7819E5A204B89A718".equals(map.get("VEHICLE_STAT"))){
        		MSGUtil.sayError(Util.MI18N.FREE_ERRORMSG());
        		return;
        	}
        	SC.confirm(Util.MI18N.FREE_CONFIRM(), new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if(value!=null&&value){
					 doFree();	
					}
					
				}
			});
        }
	}
	
	private void doFree(){
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sf = new StringBuffer();
		//String tableName = table.getDataSource().getAttribute("tableName");
		String tableName = table.getDataSource().getTableName();
		if(ObjUtil.isNotNull(map.get("VEH_LOCK_REASON"))){
			sf.append("update ");
			sf.append(tableName);
			sf.append(" set VEHICLE_STAT='D5595E8BF25A4E0D8C46796B772FB1BA' ,VEH_LOCK_REASON='',REASON='',AVAIL_FLAG='N' ");
			sf.append(" where ID='");
			sf.append(map.get("ID")+"'");
			sqlList.add(sf.toString());
			
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.equals(StaticRef.SUCCESS_CODE)){
					   MSGUtil.sayInfo(Util.MI18N.FREE_SUCCESS());
					   ListGridRecord record = table.getSelectedRecord();
					   record.setAttribute("VEHICLE_STAT", "D5595E8BF25A4E0D8C46796B772FB1BA");
					   record.setAttribute("VEHICLE_STAT_NAME", "可用");
					   record.setAttribute("VEH_LOCK_REASON"," " );
					   record.setAttribute("REASON", " ");
					   record.setAttribute("AVAIL_FLAG", false);
					   value.setValue("AVAIL_FLAG", false);
					   value.setValue("VEH_LOCK_REASON", " ");
					   value.setValue("REASON", " ");
					   value.setValue("VEHICLE_STAT_NAME", "可用");
					   table.redraw();
					   view.freeButton.disable();
					   view.freezeButton.enable();
					 
					   
					}else{
						MSGUtil.sayError(Util.MI18N.FREE_FAILURE());
					}
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
					
				}
			});
		}
		else {
			MSGUtil.sayError(Util.MI18N.FREE_FAILURE());
		}
		
		
	}
//	private void refreshResult(){
//		table.removeData(table.getSelectedRecord());
//		table.deselectAllRecords();
//	}

}
