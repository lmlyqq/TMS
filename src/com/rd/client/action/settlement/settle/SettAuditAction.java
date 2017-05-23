package com.rd.client.action.settlement.settle;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.SettlementRecView;
import com.rd.client.view.settlement.SettlementView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 结算单审核，取消审核
 * @author fangliangmeng
 *
 */
public class SettAuditAction implements ClickHandler{
	
	private SettlementView view;
	private SettlementRecView rview;
	private Boolean isAudit = true;
	private String NO_AUDIT = "10"; //未审核
	private String AUDITED = "20"; //已审核
	private boolean isRec = false;
	
	public SettAuditAction(SettlementView view,boolean isAudit){
		this.view = view;
		this.isAudit = isAudit;
	}
	public SettAuditAction(SettlementRecView view,boolean isAudit,boolean isRec){
		this.rview = view;
		this.isAudit = isAudit;
		this.isRec = isRec;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		ArrayList<String> sqlList = new ArrayList<String>();
		String stat = "10";
		String addwho = "";
		String addtime ="";
		
		Record record = isRec?rview.table.getSelectedRecord():view.table.getSelectedRecord();
		
		
		if(!isAudit){
			if(record.getAttribute("BILL_STAT").equals("20")){
				MSGUtil.sayError("结算单已开票，不能取消审核！");
				return;
			}
			
			if(!record.getAttribute("VERIFI_STAT").equals("10")){
				MSGUtil.sayError("结算单已核销，不能取消审核！");
				return;
			}
		}
	
		StringBuffer sql = new StringBuffer();
		sql.append("update bill_settle_info set audit_stat='");
		if(isAudit){
			stat = AUDITED;
			addwho = loginUser;
			addtime ="sysdate";
			sql.append(AUDITED);
			sql.append("',audit_time = sysdate");
			sql.append(",auditor='");
			sql.append(loginUser);
			sql.append("',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}else{
			stat = NO_AUDIT;
			addwho = "";
			addtime = "null";
			
			sql.append(NO_AUDIT);
			sql.append("',audit_time = null");
			sql.append(",auditor='',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}
		
		sql.append(" where id = '");
		sql.append(record.getAttributeAsString("ID"));
		sql.append("'");
		
		sqlList.add(sql.toString());
		
		sql = new StringBuffer();
		sql.append("update bill_detail_pay set audit_stat='");
		sql.append(stat);
		sql.append("',audit_time =");
		sql.append(addtime);
		sql.append(",auditor='");
		sql.append(addwho);
		sql.append("' where sett_no='");
		sql.append(record.getAttributeAsString("SETT_NO"));
		sql.append("'");
		sqlList.add(sql.toString());
		
		sql = new StringBuffer();
		sql.append("update bill_detail_rec set audit_stat='");
		sql.append(stat);
		sql.append("',audit_time =");
		sql.append(addtime);
		sql.append(",auditor='");
		sql.append(addwho);
		sql.append("' where sett_no='");
		sql.append(record.getAttributeAsString("SETT_NO"));
		sql.append("'");
		sqlList.add(sql.toString());
		
		if(!isRec){
			if(isAudit){
				view.table.getSelectedRecord().setAttribute("AUDIT_STAT", "20");
				view.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "已审核");
			}else{
				view.table.getSelectedRecord().setAttribute("AUDIT_STAT", "10");
				view.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "未审核");
			}
		}else{
			if(isAudit){
				rview.table.getSelectedRecord().setAttribute("AUDIT_STAT", "20");
				rview.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "已审核");
			}else{
				rview.table.getSelectedRecord().setAttribute("AUDIT_STAT", "10");
				rview.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "未审核");
			}
		}
		
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					if(!isRec){
						view.table.updateData(view.table.getSelectedRecord());
						view.table.redraw();
					}else{
						rview.table.updateData(rview.table.getSelectedRecord());
						rview.table.redraw();
					}
					
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
