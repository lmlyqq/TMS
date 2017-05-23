package com.rd.client.action.settlement;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.CustomFeeSettView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 财务管理--结算管理--客户结算管理--核销、取消核销
 * @author fanglm
 * @create time 2012-06-11 10:22
 */
public class ReceAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{

	private ListGrid table;
	private boolean isAud = true;
	private String NO_RECE = "10"; //未审核
	private String ALL_RECE = "20"; //已审核
	private ListGridRecord[] records;
	private CustomFeeSettView view;
	
	public ReceAction (ListGrid table,boolean isAud,CustomFeeSettView view){
		this.table = table;
		this.isAud = isAud;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		records = table.getSelection();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		doAsOrder(loginUser);
		
		view.disAudit(isAud, !isAud);
		if(isAud){
			view.disAccount(true,true);
		}else{
			view.disAccount(true, false);
		}
		

	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		records = table.getSelection();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		doAsOrder(loginUser);
		
		view.disAudit(isAud, !isAud);
		if(isAud){
			view.disAccount(true,true);
		}else{
			view.disAccount(true, false);
		}
	}
	
	private void doAsOrder(String loginUser){
		
		if(table.getSelectedRecord() == null){
			MSGUtil.sayError("未选择费用，不能执行相关操作！");
			return;
		}
		
		/*if(isAud && view.feeTable.getSelectedRecord().getAttributeAsString("AUDIT_STAT").equals("10")){
			MSGUtil.sayError("费用未审核，不能核销！");
			return;
		}*/
		if(isAud && ALL_RECE.equals(table.getSelectedRecord().getAttributeAsString("RECE_STAT"))){
			MSGUtil.sayError("费用已经收款确认，不能再次【确认收款】！");
			return;
		}
		
		if(!isAud && NO_RECE.equals(table.getSelectedRecord().getAttributeAsString("RECE_STAT"))){
			MSGUtil.sayError("费用未收款，不能【取消收款】！");
			return;
		}
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		ListGridRecord rec = null;
		for(int i = 0; i < records.length; i++) {
			rec = records[i];
			sql = new StringBuffer();
			sql.append("update trans_bill_rece set rece_stat='");
			if(isAud){
				sql.append(ALL_RECE);
				sql.append("',rece_time = sysdate");
				sql.append(",recer='");
				sql.append(loginUser);
				sql.append("',editwho='");
				sql.append(loginUser);
				sql.append("',edittime=sysdate ");
			}else{
				sql.append(NO_RECE);
				sql.append("',rece_time = null");
				sql.append(",recer=null,editwho='");
				sql.append(loginUser);
				sql.append("',edittime=sysdate ");
			}			
			sql.append(" where doc_no = '");
			sql.append(rec.getAttributeAsString("ODR_NO"));
			sql.append("'");
			sqlList.add(sql.toString());
			
			if(isAud){
				sql = new StringBuffer();
				sql.append("update trans_order_header set rece_stat = '");
				sql.append(ALL_RECE);
				sql.append("',rece_stat_name = '");
				sql.append(StaticRef.RECE_STAT);
				sql.append("' where odr_no = '");
				sql.append(rec.getAttributeAsString("ODR_NO"));
				sql.append("'");
				sqlList.add(sql.toString());
			}
			else {
				sql = new StringBuffer();
				sql.append("update trans_order_header set rece_stat = '");
				sql.append(NO_RECE);
				sql.append("',rece_stat_name = '");
				sql.append(StaticRef.NO_RECE);
				sql.append("' where odr_no = '");
				sql.append(rec.getAttributeAsString("ODR_NO"));
				sql.append("'");
				sqlList.add(sql.toString());
			}
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					for(int i=0;i<records.length;i++){
						if(isAud){
							records[i].setAttribute("RECE_STAT", ALL_RECE);
							records[i].setAttribute("RECE_STAT_NAME", StaticRef.RECE_STAT);
						}else{
							records[i].setAttribute("RECE_STAT", NO_RECE);
							records[i].setAttribute("RECE_STAT_NAME", StaticRef.NO_RECE);
						}
						table.updateData(records[i]);
					}
					table.redraw();
					
				}else{
					MSGUtil.sayError(result.substring(2));
					view.disAudit(!isAud, isAud);
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
