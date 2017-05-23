package com.rd.client.action.settlement.settle;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.InvoiceView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 发票组审核，取消审核
 * @author fangliangmeng
 *
 */
public class InvoiceAuditAction implements ClickHandler{
	
	private InvoiceView view;
	private Boolean isAudit = true;
	private String NO_AUDIT = "10"; //未审核
	private String AUDITED = "20"; //已审核
	
	public InvoiceAuditAction(InvoiceView view,boolean isAudit){
		this.view = view;
		this.isAudit = isAudit;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		if(!isAudit){
			if(view.table.getSelectedRecord().getAttribute("INVO_STAT").equals("20")){
				MSGUtil.sayError("发票组已开票，不能取消审核！");
				return;
			}
		}
	
		StringBuffer sql = new StringBuffer();
		sql.append("update bill_invo_grp set audit_stat='");
		if(isAudit){
			sql.append(AUDITED);
			sql.append("',audit_time = sysdate");
			sql.append(",auditer='");
			sql.append(loginUser);
			sql.append("',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}else{
			sql.append(NO_AUDIT);
			sql.append("',audit_time = null");
			sql.append(",auditer='',editwho='");
			sql.append(loginUser);
			sql.append("',edittime=sysdate ");
		}
		
		sql.append(" where id = '");
		sql.append(view.table.getSelectedRecord().getAttributeAsString("ID"));
		sql.append("'");
		
		if(isAudit){
			view.table.getSelectedRecord().setAttribute("AUDIT_STAT", "20");
			view.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "已审核");
		}else{
			view.table.getSelectedRecord().setAttribute("AUDIT_STAT", "10");
			view.table.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "未审核");
		}
		Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					view.table.updateData(view.table.getSelectedRecord());
					view.table.redraw();
					
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
