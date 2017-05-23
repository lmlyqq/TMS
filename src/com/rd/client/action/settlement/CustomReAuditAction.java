package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.CustomFeeSettView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

public class CustomReAuditAction implements ClickHandler{

	private SGTable table;
	private boolean isAud = true;
	private String NO_AUDIT = "10"; //未审核
	//private String AUDITED = "20"; //已审核
	private CustomFeeSettView cView;
	private HashMap<String, Boolean> map1;
	
	public CustomReAuditAction (SGTable table,boolean isAud,CustomFeeSettView view){
		this.table = table;
		this.isAud = isAud;
		this.cView = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		String loginUser = LoginCache.getLoginUser().getUSER_ID();
		doAsOrder(loginUser);
	}

	@SuppressWarnings("unchecked")
	private void doAsOrder(String loginUser){
		int[] records = cView.table.getAllEditRows();
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer sql2 = new StringBuffer();
		StringBuffer sql3 = new StringBuffer();
		for(int i=0;i<records.length;i++){
			Record rec = cView.table.getEditedRecord(records[i]);
			map1 = (HashMap<String, Boolean>)table.getEditValues(records[i]);
			if(map1.get("USE_FLAG") && rec.getAttributeAsString("AUDIT_STAT").equals("20")){
				sql = new StringBuffer();
				sql.append("update TRANS_BILL_RECE set audit_stat='");
				sql.append(NO_AUDIT);
				sql.append("',audit_time = null");
				sql.append(",auditor='',editwho='");
				sql.append(loginUser);
				sql.append("',edittime=sysdate ");
				sql.append(" where DOC_NO = '");
				sql.append(rec.getAttributeAsString("ODR_NO"));
				sql.append("'");
				sqlList.add(sql.toString());
					
				sql2 = new StringBuffer();
				sql2.append("update TRANS_ORDER_HEADER set AUDIT_STAT ='");
				sql2.append(NO_AUDIT);
				sql2.append("',audit_time = null");
				sql2.append(" where ODR_NO = '");
				sql2.append(rec.getAttributeAsString("ODR_NO"));
				sql2.append("'");
				sqlList.add(sql2.toString());
					
				sql3 = new StringBuffer();
				sql3.append("update BMS_ORDER_HEADER set AUDIT_STAT ='");
				sql3.append(NO_AUDIT);
				sql3.append("',audit_time = null");
				sql3.append(" where ODR_NO = '");
				sql3.append(rec.getAttributeAsString("ODR_NO"));
				sql3.append("'");
				sqlList.add(sql3.toString());
			}else if(map1.get("USE_FLAG")&& !rec.getAttributeAsString("AUDIT_STAT").equals("20")){
				SC.say(""+rec.getAttributeAsString("ODR_NO")+"托运单号未审核");
				return;
			}
		}
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
					MSGUtil.showOperSuccess();
					int[] records = cView.table.getAllEditRows();
					for(int i=0;i<records.length;i++){
						Record rec = cView.table.getRecord(records[i]);
						rec.setAttribute("AUDIT_STAT", NO_AUDIT);
						rec.setAttribute("AUDIT_STAT_NAME", "未审核");
					}
					cView.initLoadFeeBtn(4);
					cView.table.redraw();
					cView.disAudit(isAud, !isAud);
				}else{
					MSGUtil.sayError(result.substring(2));
					cView.disAudit(!isAud, isAud);
				}
			}
			
		});
	}
	
}
