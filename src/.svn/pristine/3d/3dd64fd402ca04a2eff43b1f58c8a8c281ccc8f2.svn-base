package com.rd.client.action.settlement;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 财务管理--结算管理--供应商结算管理--打回按钮
 * @author fanglm
 * @create time 2011-01-25 10:22
 */
public class ReturnAction implements ClickHandler{

	private SGTable table;
	private String CALLBACK = "15"; //已打回
	private ListGridRecord[] records;
	private SuplrFeeSettView view;
	
	public ReturnAction (SGTable table,SuplrFeeSettView view){
		this.table = table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		records = table.getSelection();
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		StringBuffer accId = new StringBuffer();
		StringBuffer canId = new StringBuffer();
		StringBuffer idStr = new StringBuffer();
		for(int i=0;i<records.length;i++){
			int row = table.getRecordIndex(records[i]) + 1;
			if(!"未审核".equals(records[i].getAttributeAsString("AUDIT_STAT_NAME"))){
				canId.append(row);
				canId.append(",");
			}
			idStr.append("'");
			idStr.append(records[i].getAttribute("ID"));
			idStr.append("',");
		}
		
		if(canId.length() > 0){
			SC.warn("第" + accId.substring(0, accId.length()-1) + "行记录状态为【已审核】,不能执行打回操作！");
			return;
		}
		
		
		view.disAudit(true, true);
		view.disAccount(false,true);
		StringBuffer sql = new StringBuffer();
		sql.append("update trans_bill_detail set account_stat='");
		sql.append(CALLBACK);
		sql.append("',editwho='");
		sql.append(loginUser);
		sql.append("',edittime=sysdate ");
		sql.append(" where id in(");
		sql.append(idStr.substring(0, idStr.length()-1));
		sql.append(")");
		
		Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					
					for(int i=0;i<records.length;i++){
						records[i].setAttribute("ACCOUNT_STAT", CALLBACK);
						records[i].setAttribute("ACCOUNT_STAT_NAME", "已打回");
						table.updateData(records[i]);
					}
					table.redraw();
					
				}else{
					MSGUtil.sayError(result.substring(2));
					view.disAudit(false, true);
				}
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
