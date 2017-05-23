package com.rd.client.action.settlement.settle;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;

/**
 * 财务管理--结算管理--结算单--保存功能
 * @author fanglm
 * @create time 2011-01-21 20:22
 */
public class SaveSettleAction implements ClickHandler{

	private ListGrid table;
	private Map<String, String> record;
	private ValuesManager vm;
	private String proName="SP_SETT_SAVE_SETTLE(?,?,?,?,?,?,?,?,?)";
	
	public SaveSettleAction (ListGrid table,ValuesManager vm){
		this.table = table;
		this.vm = vm;
	}

	
private ArrayList<String> getList(){
		
		ArrayList<String> list= new ArrayList<String>();
		list.add(record.get("SETT_TYPE"));
		list.add(record.get("EXEC_ORG_ID"));
		list.add(record.get("EXEC_ORG_ID_NAME"));
		list.add(record.get("SETT_NAME"));
		list.add(record.get("SETT_ORG_ID"));
		list.add(record.get("SETT_ORG_ID_NAME"));
		list.add(ObjUtil.ifNull(String.valueOf(record.get("LOW_CASH")).trim(),"0.00"));
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}

@SuppressWarnings("unchecked")
@Override
public void onClick(ClickEvent event) {
	record = vm.getValues();
	
	Util.async.execProcedure(getList(), proName, new AsyncCallback<String>() {
		
		@Override
		public void onSuccess(String result) {
			if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
				MSGUtil.showOperSuccess();
				table.invalidateCache();
				table.fetchData(table.getCriteria());
				
				
			}else{
				MSGUtil.sayError(result.substring(2));
			}
			
			
		}
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}
	});
}

}
