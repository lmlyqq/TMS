package com.rd.client.action.settlement.settle;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.FeeItemWin;
import com.rd.client.view.settlement.SettlementRecView;
import com.rd.client.view.settlement.SettlementView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 添加结算单费用明细
 * @author fangliangmeng
 *
 */
public class ImpFeeToSettleAction implements ClickHandler{
	
	private SettlementView view;
	
	private SettlementRecView rview; 
	
	private FeeItemWin win;
	
	private String tableName="BILL_DETAIL_PAY";
	
	public ImpFeeToSettleAction(SettlementView view,FeeItemWin win){
		this.view = view;
		this.win = win;
	}
	
	public ImpFeeToSettleAction(SettlementRecView rview,FeeItemWin win){
		this.rview = rview;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer num = new StringBuffer();
		
		Record record = ObjUtil.isNotNull(view)?view.table.getSelectedRecord():rview.table.getSelectedRecord();
		
		String sett_no=record.getAttribute("SETT_NO");
		
		if(record.getAttribute("SETT_TYPE").equals("应收费用")){
			tableName = "BILL_DETAIL_REC";
		}
		
		//合计总费用
		Double tot_cash = 0.00;
		
		final ArrayList<ListGridRecord> list = new ArrayList<ListGridRecord>();
		
		ListGridRecord[] records = win.addrList.getRecords();
		for(int i=0;i<records.length;i++){
			num.append(",'");
			num.append(records[i].getAttribute("ID"));
			num.append("'");
			if(records[i].getAttribute("FEE_TYPE").equals("PLUS")){
				tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}else if(records[i].getAttribute("FEE_TYPE").equals("MINUS")){
				tot_cash = tot_cash - Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}
			list.add(records[i]);
		}
		
		
		//合并结算单原有明细数据
		
		ListGridRecord[] oldRecords = view.feeTable.getRecords();
		for(int i=0;i<oldRecords.length;i++){
//			tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("DUE_FEE")));
			if(records[i].getAttribute("FEE_TYPE").equals("PLUS")){
				tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}else if(records[i].getAttribute("FEE_TYPE").equals("MINUS")){
				tot_cash = tot_cash - Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}
			list.add(oldRecords[i]);
		}
		
//		tot_cash = tot_cash + Double.valueOf((record.getAttribute("SETT_CASH")));
		
		//费用信息关联到结算单
		sql.append("update ");
		sql.append(tableName);
		sql.append(" set sett_no='");
		sql.append(sett_no);
		sql.append("' where id in (");
		sql.append(num.substring(1));
		sql.append(")");
		
		sqlList.add(sql.toString());
		
		//修改结算单汇总信息
		sql = new StringBuffer();
		sql.append("update bill_settle_info set sett_cash=");
		sql.append(tot_cash);
		sql.append(" where sett_no='");
		sql.append(sett_no);
		sql.append("'");
		sqlList.add(sql.toString());
		
		if(ObjUtil.isNotNull(view)){
			view.table.getSelectedRecord().setAttribute("SETT_CASH", tot_cash);
		}else{
			rview.table.getSelectedRecord().setAttribute("SETT_CASH", tot_cash);
		}
		sql = null;
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					if(ObjUtil.isNotNull(view)){
						view.feeTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
						view.table.updateData(view.table.getSelectedRecord());
						view.table.redraw();
					}else{
						rview.feeTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
						rview.table.updateData(rview.table.getSelectedRecord());
						rview.table.redraw();
					}
					
					win.window.hide();
					win.destroy();
					win.fireCloseEvent("Popup window has been destroyed");
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
