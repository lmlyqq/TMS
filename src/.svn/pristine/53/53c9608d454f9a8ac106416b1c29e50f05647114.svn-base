package com.rd.client.action.base.person;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasPersonView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PersonBlackzeAction implements ClickHandler{

	private ValuesManager valuemanager;
	private ListGrid table;
	private Map<String, String> map;
    private BasPersonView view;
	
    public PersonBlackzeAction(ListGrid table,ValuesManager valuemanager,BasPersonView view){
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
		String tableName = table.getDataSource().getTableName();
		
		
				sf = new StringBuffer();
				sf.append("update ");
				sf.append(tableName);
				sf.append(" set BLACKLIST_FLAG='Y");
				sf.append("' WHERE ID='"+map.get("ID")+"'");
				sqlList.add(sf.toString());
				
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.equals(StaticRef.SUCCESS_CODE)){
							final ListGridRecord record = new ListGridRecord();
							record.setAttribute("ID",map.get("ID"));
							
							MSGUtil.sayInfo("加入黑名单操作成功");
							
							ListGridRecord rec = table.getSelectedRecord();
							rec.setAttribute("BLACKLIST_FLAG", true);
							valuemanager.setValue("BLACKLIST_FLAG", true);
							table.redraw();
							view.blackButton.enable();
							view.blackzeButton.disable();
							
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
    
}
