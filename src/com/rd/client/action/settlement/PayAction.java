package com.rd.client.action.settlement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 财务管理--结算管理--供应商结算管理--付款、取消付款
 * @author fanglm
 * @create time 2011-01-21 20:22
 */
public class PayAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{

	private ListGrid table;
	private boolean isAud = true;
	private String NO_AUDIT = "10"; //未审核
	private String AUDITED = "20"; //已审核
	private ListGridRecord[] records;
	private SuplrFeeSettView view;
	
	public PayAction (ListGrid table,boolean isAud,SuplrFeeSettView view){
		this.table = table;
		this.isAud = isAud;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		records = table.getRecords();
		
		if(records.length > 0  && records[0].getAttributeAsString("AUDIT_STAT").equals("10")){
			MSGUtil.sayError("未选择费用信息，或者选择的费用未审核，不能核销!");
			return;
		}
		
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		if(view.titName.equals("按调度单")){
			records = view.feeTable.getRecords();
			doAsLoad(loginUser);
		}
//			else if(view.titName.equals("按作业单")){
//			records = view.shpmTable.getRecords();
//			doAsShpm(loginUser);
//			
//		}else if(view.titName.equals("按作业单明细")){
//			records = table.getRecords();
//			doAsShpmDetail(loginUser);
//		}
		
		view.disAudit(isAud, !isAud);
		if(isAud){
			view.disAccount(true,true);
		}else{
			view.disAccount(true, false);
		}
		

	}
	
	private void doAsLoad(String loginUser){
		
		StringBuffer feeid = new StringBuffer();
		for(int i=0;i<records.length;i++){
			feeid.append(",'");
			feeid.append(records[i].getAttributeAsString("FEE_ID"));
			feeid.append("'");
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("update trans_bill_pay set pay_stat='");
		if(isAud){
			sql.append(AUDITED);
			sql.append("',pay_fee = due_fee");
			sql.append(",act_pay_time = sysdate");
			sql.append(",payee='");
			sql.append(loginUser);
			sql.append("',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}else{
			sql.append(NO_AUDIT);
			sql.append("',pay_fee=0,act_pay_time = null");
			sql.append(",payee='',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}
		
		sql.append(" where load_no = '");
		sql.append(view.feeTable.getSelectedRecord().getAttributeAsString("LOAD_NO"));
		sql.append("' and fee_id in (");
		sql.append(feeid.substring(1));
		sql.append(")");
		
		Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					for(int i=0;i<records.length;i++){
						if(isAud){
							records[i].setAttribute("PAY_STAT", AUDITED);
							records[i].setAttribute("PAY_STAT_NAME", "已核销");
						}else{
							records[i].setAttribute("PAY_STAT", NO_AUDIT);
							records[i].setAttribute("PAY_STAT_NAME", "未核销");
						}
						view.feeTable.updateData(records[i]);
					}
					view.feeTable.redraw();
					
				}else{
					MSGUtil.sayError(result.substring(2));
					view.disAudit(false, false);
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private void doAsShpm(String loginUser){
		StringBuffer feeid = new StringBuffer();
		for(int i=0;i<records.length;i++){
			feeid.append(",'");
			feeid.append(records[i].getAttributeAsString("SHPM_NO"));
			feeid.append("'");
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("update bill_detail_pay set pay_fee = due_fee,audit_stat='");
		if(isAud){
			sql.append(AUDITED);
			sql.append("',audit_time = sysdate");
			sql.append(",auditor='");
			sql.append(loginUser);
			sql.append("',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}else{
			sql.append(NO_AUDIT);
			sql.append("',audit_time = null");
			sql.append(",auditor='',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}
		
		sql.append(" where  1= 1 ");
		sql.append(" and shpm_no in (");
		sql.append(feeid.substring(1));
		sql.append(")");
		
		Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					for(int i=0;i<records.length;i++){
						if(isAud){
							records[i].setAttribute("AUDIT_STAT", AUDITED);
							records[i].setAttribute("AUDIT_STAT_NAME", "已审核");
						}else{
							records[i].setAttribute("AUDIT_STAT", NO_AUDIT);
							records[i].setAttribute("AUDIT_STAT_NAME", "未审核");
						}
						//view.shpmTable.updateData(records[i]);
					}
					//view.shpmTable.redraw();
					
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

	
	private void doAsShpmDetail(String loginUser){
		StringBuffer accId = new StringBuffer();
		StringBuffer canId = new StringBuffer();
		StringBuffer idStr = new StringBuffer();
		for(int i=0;i<records.length;i++){
			int row = table.getRecordIndex(records[i]) + 1;
			if("未审核".equals(records[i].getAttributeAsString("AUDIT_STAT_NAME"))){
				canId.append(row);
				canId.append(",");
			}else{
				accId.append(row);
				accId.append(",");
			}
			idStr.append("'");
			idStr.append(records[i].getAttribute("ID"));
			idStr.append("',");
		}
		
		if(isAud && accId.length() > 0){
			SC.warn("第" + accId.substring(0, accId.length()-1) + "行记录状态为【已审核】,不能执行审核操作！");
			return;
		}
		
		if(!isAud && canId.length() > 0){
			SC.warn("第" + canId.substring(0, canId.length()-1) + "行记录状态为【未审核】,不能执行取消审核操作！");
			return;
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("update trans_bill_detail set audit_stat='");
		if(isAud){
			sql.append(AUDITED);
			sql.append("',audit_time = sysdate");
			sql.append(",auditor='");
			sql.append(loginUser);
			sql.append("',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}else{
			sql.append(NO_AUDIT);
			sql.append("',audit_time = null");
			sql.append(",auditor='',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}
		
		sql.append(" where id in(");
		sql.append(idStr.substring(0, idStr.length()-1));
		sql.append(")");
		
		Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					for(int i=0;i<records.length;i++){
						if(isAud){
							records[i].setAttribute("AUDIT_STAT", AUDITED);
							records[i].setAttribute("AUDIT_STAT_NAME", "已审核");
						}else{
							records[i].setAttribute("AUDIT_STAT", NO_AUDIT);
							records[i].setAttribute("AUDIT_STAT_NAME", "未审核");
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
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		records = table.getRecords();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		if(view.pageNum ==0){
			records = view.feeTable.getRecords();
			doAsLoad(loginUser);
		}else if(view.pageNum == 1){
			//records = view.shpmTable.getRecords();
			doAsShpm(loginUser);
			
		}else if(view.pageNum == 2){
			records = table.getRecords();
			doAsShpmDetail(loginUser);
		}
		
		view.disAudit(isAud, !isAud);
		if(isAud){
			view.disAccount(true,true);
		}else{
			view.disAccount(true, false);
		}
	}
}
