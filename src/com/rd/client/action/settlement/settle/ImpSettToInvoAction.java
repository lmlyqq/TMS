package com.rd.client.action.settlement.settle;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.InvoiceView;
import com.rd.client.view.settlement.SettleItemWin;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 添加结算单费用明细
 * @author fangliangmeng
 *
 */
public class ImpSettToInvoAction implements ClickHandler{
	
	private InvoiceView view;
	
	private SettleItemWin win;
	
	
	public ImpSettToInvoAction(InvoiceView view,SettleItemWin win){
		this.view = view;
		this.win = win;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer num = new StringBuffer();
		String id=view.table.getSelectedRecord().getAttribute("ID");
		
		
		//合计总费用
		Double tot_cash = 0.00;
		
		final ArrayList<ListGridRecord> list = new ArrayList<ListGridRecord>();
		
		ListGridRecord[] records = win.addrList.getRecords();
		for(int i=0;i<records.length;i++){
			num.append(",'");
			num.append(records[i].getAttributeAsString("ID"));
			num.append("'");
			tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("SETT_CASH")));
			list.add(records[i]);
		}
		
		
		//合并结算单原有明细数据
		
		ListGridRecord[] oldRecords = view.feeTable.getRecords();
		for(int i=0;i<oldRecords.length;i++){
			tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("SETT_CASH")));
			list.add(oldRecords[i]);
		}
		
		tot_cash = tot_cash + Double.valueOf((view.table.getSelectedRecord().getAttribute("SETT_CASH")));
		
		//费用信息关联到结算单
		sql.append("update bill_settle_info");
		sql.append(" set invo_grp_id='");
		sql.append(id);
		sql.append("',bill_stat='20' where id in (");
		sql.append(num.substring(1));
		sql.append(")");
		
		sqlList.add(sql.toString());
		
		//修改结算单汇总信息
		sql = new StringBuffer();
		sql.append("update bill_invo_grp set sett_cash=");
		sql.append(tot_cash);
		sql.append(" where ID='");
		sql.append(id);
		sql.append("'");
		sqlList.add(sql.toString());
		
		view.table.getSelectedRecord().setAttribute("SETT_CASH", tot_cash);
		sql = null;
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.showOperSuccess();
					
					view.feeTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
					
					view.table.updateData(view.table.getSelectedRecord());
					view.table.redraw();
					
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
