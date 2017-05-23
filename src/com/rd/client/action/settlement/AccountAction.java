package com.rd.client.action.settlement;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.CustomFeeSettView;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 财务管理--结算管理--供应商结算管理--对账、取消对账按钮
 * @author fanglm
 * @create time 2011-01-21 20:21
 */
public class AccountAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler{
	
	private boolean isAcc = true;
	private String NO_ACCOUNT = "10"; //未对账
	private String ACCOUNTED = "20"; //已对账
	private ListGridRecord[] records;
	private SuplrFeeSettView view;
	private CustomFeeSettView cView;
	private String FEE_TYP = "PAY";
	
	public AccountAction (boolean isAcc,SuplrFeeSettView view){
		this.isAcc = isAcc;
		this.view = view;
	}
	
	public AccountAction(){
		
	}
	public AccountAction (boolean isAcc,CustomFeeSettView cView,String fee_tyP){
		this.isAcc = isAcc;
		this.cView = cView;
		this.FEE_TYP = fee_tyP;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		
		if(FEE_TYP.equals("PAY")){
			//if(view.titName == "按调度单"){
				records = view.loadTable.getSelection();
				doAsLoad(loginUser);
			/*}
			else if(view.titName.equals("按作业单")){
				records = view.headTable.getSelection();
				doAsShpm(loginUser);
			}
			else if(view.titName.equals("按作业单明细")){
				records = table.getSelection();
				doAsShpmDetail(loginUser);
			}*/
			view.disAccount(isAcc, !isAcc);
			if(isAcc){
				view.disAudit(false, true);
			}else{
				view.disAudit(true, true);
			}
		}else{
			doAsOrder(loginUser);
			cView.disAccount(isAcc, !isAcc);
			if(isAcc){
				cView.disAudit(false, true);
			}else{
				cView.disAudit(true, true);
			}
		}
				
	}
	
	private void doAsLoad(String loginUser){
		ArrayList<String> sqlList = new ArrayList<String>();
		if(view.selectLoad == null){
			SC.warn("未选择调度单，不能执行【确认对账】！");
			return;
		}else{
			for(int i=0;i<records.length;i++){
				StringBuffer sql = new StringBuffer();
				sql.append("update TRANS_BILL_PAY set account_stat='");
				if(isAcc){
					sql.append(ACCOUNTED);
					sql.append("',account_time = sysdate");
					sql.append(",accounter='");
					sql.append(loginUser);
					sql.append("',editwho='");
					sql.append(loginUser);
					sql.append("',edittime=sysdate ");
				}else{
					sql.append(NO_ACCOUNT);
					sql.append("',account_time = null");
					sql.append(",accounter='',editwho='");
					sql.append(loginUser);
					sql.append("',edittime=sysdate ");
				}
				
				sql.append(" where load_no = '");
				sql.append(records[i].getAttributeAsString("LOAD_NO"));
				//sql.append("' and fee_id in (");
				//sql.append(feeid.substring(1));
				//sql.append(")");
				sql.append("'");
				sqlList.add(sql.toString());
				
				sql = new StringBuffer();
				sql.append("update TRANS_LOAD_HEADER set account_stat ='");
				if(isAcc){
					sql.append(ACCOUNTED);
					sql.append("',account_time = sysdate");
				}else{
					sql.append(NO_ACCOUNT);
					sql.append("',account_time = null");
				}
				sql.append(" where load_no = '");
				sql.append(records[i].getAttributeAsString("LOAD_NO"));
				sql.append("'");
				sqlList.add(sql.toString());
			}

			
			Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
						MSGUtil.showOperSuccess();
						
						for(int i=0;i<records.length;i++){
							if(isAcc){
								records[i].setAttribute("ACCOUNT_STAT", NO_ACCOUNT);
								records[i].setAttribute("ACCOUNT_STAT_NAME", "已对账");
							}else{
								records[i].setAttribute("ACCOUNT_STAT", ACCOUNTED);
								records[i].setAttribute("ACCOUNT_STAT_NAME", "未对账");
							}
							view.loadTable.updateData(records[i]);
						}
						view.loadTable.redraw();
						
					}else{
						MSGUtil.sayError(result.substring(2));
						view.disAccount(!isAcc, isAcc);
					}
					
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	private void doAsOrder(String loginUser){
		if(cView.feeTable.getSelectedRecord() == null){
			SC.warn("没有选择费用信息，不能执行【确认对账】！");
			return;
		}else{
			StringBuffer sql = new StringBuffer();
			sql.append("update bill_detail_rec set account_stat='");
			if(isAcc){
				sql.append(ACCOUNTED);
				sql.append("',account_time = sysdate");
				sql.append(",accounter='");
				sql.append(loginUser);
				sql.append("',editwho='");
				sql.append(loginUser);
				sql.append("',edittime=sysdate ");
			}else{
				sql.append(NO_ACCOUNT);
				sql.append("',account_time = null");
				sql.append(",accounter='',editwho='");
				sql.append(loginUser);
				sql.append("',edittime=sysdate ");
			}
			
			sql.append(" where doc_no = '");
			sql.append(cView.feeTable.getSelectedRecord().getAttributeAsString("DOC_NO"));
			sql.append("'");
			
			
			Util.async.excuteSQL(sql.toString(), new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
						MSGUtil.showOperSuccess();
						records = cView.feeTable.getSelection();
						for(int i=0;i<records.length;i++){
							if(isAcc){
								records[i].setAttribute("ACCOUNT_STAT", NO_ACCOUNT);
								records[i].setAttribute("ACCOUNT_STAT_NAME", "已对账");
							}else{
								records[i].setAttribute("ACCOUNT_STAT", ACCOUNTED);
								records[i].setAttribute("ACCOUNT_STAT_NAME", "未对账");
							}
							cView.feeTable.updateData(records[i]);
						}
						cView.feeTable.redraw();
						
					}else{
						MSGUtil.sayError(result.substring(2));
						cView.disAccount(!isAcc, isAcc);
					}
					
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	

	@Override
	public void onClick(MenuItemClickEvent event) {
		// TODO Auto-generated method stub
		
	}
}
