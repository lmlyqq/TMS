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


public class BlackzeAction implements ClickHandler {
	private ValuesManager valuemanager;
	private ListGrid table;
	private Map<String, String> map;
    private BasSupplierView view;
    
	public BlackzeAction(){
		
	}
	
	public BlackzeAction(ListGrid table,ValuesManager valuemanager){
		this.valuemanager = valuemanager;
		this.table = table;
		
	}
	
	public BlackzeAction(ListGrid table,ValuesManager valuemanager,BasSupplierView view){
		this.valuemanager = valuemanager;
		this.table = table;
		this.view = view;
		
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {
		map = valuemanager.getValues();
		
		
          if(map != null){
        	 
        	SC.confirm("确认将该承应商加入黑名单？", new BooleanCallback() {
				
				@Override
				public void execute(Boolean value) {
					if(value !=null&&value){
						doBlackze();
					}
				
				}
			});  
          }
	}
	
	private void doBlackze(){
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sf = null;
		//String tableName = table.getDataSource().getAttribute("tableName");
		String tableName = table.getDataSource().getTableName();
		
		
				sf = new StringBuffer();
				sf.append("update ");
				sf.append(tableName);
				sf.append(" set BLACKLIST_FLAG='Y");
				//sf.append(map.get("REASON"));
				//sf.append("',VEH_LOCK_REASON='");
				//sf.append(map.get("VEH_LOCK_REASON"));
				sf.append("' WHERE ID='"+map.get("ID")+"'");
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
							
							MSGUtil.sayInfo("加入黑名单操作成功");
							
							ListGridRecord rec = table.getSelectedRecord();
							//rec.setAttribute("VEHICLE_STAT", "40BC1270A5B240E7819E5A204B89A718");
							rec.setAttribute("BLACKLIST_FLAG", true);
							//rec.setAttribute("VEH_LOCK_REASON", view.basicPanel.getItem("VEH_LOCK_REASON").getDisplayValue().toString());
							//rec.setAttribute("REASON", view.basicPanel.getItem("REASON").getValue().toString());
							//valuemanager.setValue("VEHICLE_STAT", "40BC1270A5B240E7819E5A204B89A718");
							valuemanager.setValue("BLACKLIST_FLAG", true);
							table.redraw();
							view.blackButton.enable();
							view.blackzeButton.disable();
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
						    MSGUtil.sayError("加入黑名单操作失败！");
						}
						
						 
						 					
					}
					
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
						
					}
				});
			
			
	}
//	private void reRefreshResult(){
//		table.removeData(table.getSelectedRecord());
//		table.deselectAllRecords();
//	}

}
