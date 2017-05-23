package com.rd.client.action.settlement.settle;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.InvoiceView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 删除结算单费用明细
 * @author fangliangmeng
 *
 */
public class expSettFromInvoAction implements ClickHandler{
	
	private InvoiceView view;
	
	public expSettFromInvoAction(InvoiceView view){
		this.view = view;
	}

	@Override
	public void onClick(ClickEvent event) {
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer num = new StringBuffer();
		String ID=view.table.getSelectedRecord().getAttribute("ID");
		
		
		//合计总费用
		Double tot_cash = 0.00;
		
		final ArrayList<ListGridRecord> list = new ArrayList<ListGridRecord>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		ListGridRecord[] records = view.feeTable.getRecords();
		if(records.length == 0){
			MSGUtil.sayError("请选择需要剔除的结算单!");
			return;
		}
		for(int i=0;i<records.length;i++){
			num.append(",'");
			num.append(records[i].getAttributeAsString("ID"));
			num.append("'");
			tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("SETT_CASH")));
			map.put(i+"", i+"");
		}
		
		
		//合并结算单原有明细数据
		
		ListGridRecord[] oldRecords = view.feeTable.getRecords();
		
		for(int i=0;i<oldRecords.length;i++){
			if(!ObjUtil.isNotNull(map.get(i+""))){
				list.add(oldRecords[i]);
			}
		}
		
		tot_cash =Double.valueOf((view.table.getSelectedRecord().getAttribute("SETT_CASH"))) - tot_cash ;
		
		//费用信息取消关联
		sql.append("update BILL_SETTLE_INFO");
		sql.append(" set INVO_GRP_ID='',bill_stat='10'");
		sql.append(" where id in (");
		sql.append(num.substring(1));
		sql.append(")");
		
		sqlList.add(sql.toString());
		
		//修改结算单汇总信息
		sql = new StringBuffer();
		sql.append("update bill_INVO_GRP set sett_cash=");
		sql.append(tot_cash);
		sql.append(" where ID='");
		sql.append(ID);
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
