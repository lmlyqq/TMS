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


public class FreezeAction implements ClickHandler {
	private ValuesManager valuemanager;
	private ListGrid table;
	private Map<String, String> map;
    private BasVehCapacityView view;
    
	public FreezeAction(){
		
	}
	
	public FreezeAction(ListGrid table,ValuesManager valuemanager){
		this.valuemanager = valuemanager;
		this.table = table;
		
	}
	
	public FreezeAction(ListGrid table,ValuesManager valuemanager,BasVehCapacityView view){
		this.valuemanager = valuemanager;
		this.table = table;
		this.view = view;
		
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		map = valuemanager.getValues();
		
		
          if(map != null){
        	  if(!"D5595E8BF25A4E0D8C46796B772FB1BA".equals(map.get("VEHICLE_STAT"))){
        		  MSGUtil.sayError(Util.MI18N.FREEZE_ERRORMSG());
        		  return;
        	  }
        	  
        	SC.confirm(Util.MI18N.FREEZE_CONFIRM(), new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if(value !=null&&value){
						doFreeze();
					}
				
				}
			});  
          }
	}
	
	private void doFreeze(){
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sf = null;
		//String tableName = table.getDataSource().getAttribute("tableName");
		String tableName = table.getDataSource().getTableName();
		
			if(ObjUtil.isNotNull(map.get("VEH_LOCK_REASON"))){
				sf = new StringBuffer();
				sf.append("update ");
				sf.append(tableName);
				sf.append(" set VEHICLE_STAT='40BC1270A5B240E7819E5A204B89A718',REASON='");
				sf.append(map.get("REASON"));
				sf.append("',VEH_LOCK_REASON='");
				sf.append(map.get("VEH_LOCK_REASON"));
				sf.append("',AVAIL_FLAG='Y'");
				sf.append(" WHERE ID='"+map.get("ID")+"'");
				sqlList.add(sf.toString());
				
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.equals(StaticRef.SUCCESS_CODE)){
//							final ListGridRecord record = table.getSelectedRecord();
							final ListGridRecord record = new ListGridRecord();
							record.setAttribute("ID",map.get("ID"));
//							final Record r = view.rec;
//							r.setAttribute(property, value)
							
							MSGUtil.sayInfo(Util.MI18N.FREEZE_SUCCESS());
							
							ListGridRecord rec = table.getSelectedRecord();
							rec.setAttribute("VEHICLE_STAT", "40BC1270A5B240E7819E5A204B89A718");
							rec.setAttribute("VEHICLE_STAT_NAME", "冻结");
							rec.setAttribute("VEH_LOCK_REASON", view.basicPanel.getItem("VEH_LOCK_REASON").getDisplayValue().toString());
							rec.setAttribute("REASON", view.basicPanel.getItem("REASON").getValue().toString());
							valuemanager.setValue("VEHICLE_STAT", "40BC1270A5B240E7819E5A204B89A718");
							valuemanager.setValue("VEHICLE_STAT_NAME", "冻结");
							rec.setAttribute("AVAIL_FLAG", true);
							valuemanager.setValue("AVAIL_FLAG", true);
							table.redraw();
							view.freeButton.enable();
							view.freezeButton.disable();
//							Criteria criteria = table.getCriteria();
//							table.invalidateCache();
//							if(criteria == null){
//								criteria = new Criteria();
//							}
//							criteria.addCriteria("OP_FLAG","M");
//							table.filterData(criteria,new DSCallback() {
//								
//								@Override
//								public void execute(DSResponse response, Object rawData, DSRequest request) {
//									Util.updateToRecord(valuemanager, table, record);
//									table.updateData(r);
//									table.redraw();
////									view.createMainInfo().seta
//									table.selectRecord(view.rec);
//								}
//							});
							
						}else{
						    MSGUtil.sayError(Util.MI18N.FREEZE_FAILURE());
						}
						
						 
						 					
					}
					
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
						
					}
				});
			}else{
				MSGUtil.sayError("未填写冻结原因描述！");
			}
			
	}
//	private void reRefreshResult(){
//		table.removeData(table.getSelectedRecord());
//		table.deselectAllRecords();
//	}

}
