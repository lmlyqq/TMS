package com.rd.client.action.settlement;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 财务管理--结算管理--供应商结算管理--RDC费用结算
 * @author fanglm
 * @create time 2011-06-12 14:21
 */
public class SettlementAcctAction implements ClickHandler{
	
	private SGTable table;
	private ListGridRecord[] records;
	private SuplrFeeSettView view;
	private String AUDITED = "DC6BDBD2FD1441CF91B30757FAB2F534"; //已审核
	
	public SettlementAcctAction (SGTable table,SuplrFeeSettView view){
		this.table = table;
		this.view = view;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		records = table.getSelection();
		if(records.length == 0)
			return;
		
		if(records[0].getAttribute("AUDIT_STAT").equals(AUDITED)){
			MSGUtil.sayError("费用记录已 【审核】,不能计算费用！");
			return;
		}
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		String shpm_No="";
		String SN = "";
		double mile =0.0;
		
		int row = table.getRecordIndex(records[0]);

		shpm_No = records[0].getAttribute("SHPM_NO");
		mile = Double.parseDouble(
					ObjUtil.ifObjNull(table.getEditValue(row, "MILE"),records[0].getAttribute("MILE")).toString());
				
		SN = ObjUtil.ifObjNull(table.getEditValue(row,"SERIAL_NUM"),
				ObjUtil.ifObjNull(records[0].getAttribute("SERIAL_NUM"),"")).toString();
		
		ArrayList<String> list = new ArrayList<String>();
		list.add(shpm_No);
		list.add(mile+"");
		list.add(SN);
		list.add(loginUser);
		Util.async.execProcedure(list, "SP_SETT_ACCT(?,?,?,?,?)", new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					//刷新数据
					table.discardAllEdits();
					table.invalidateCache();
					Criteria crit = table.getCriteria();
					if(crit == null) {
						crit = new Criteria();
					}
					crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
					crit.addCriteria(view.searchForm.getValuesAsCriteria());
					table.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if (table.getRecordList().getLength() > 0){
								table.selectRecord(0);
							}
						}
					});
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
