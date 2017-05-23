package com.rd.client.action.settlement.settle;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.SettlementRecView;
import com.rd.client.view.settlement.SettlementView;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 删除结算单费用明细
 * @author fangliangmeng
 *
 */
public class expFeeFromSettleAction implements ClickHandler{
	
	private SettlementView view;
	
	private String tableName="BILL_DETAIL_PAY";
	
	private SettlementRecView rView;
	
	public expFeeFromSettleAction(SettlementView view){
		this.view = view;
	}
	
	public expFeeFromSettleAction(SettlementRecView rview){
		this.rView = rview;
	}

	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] records = ObjUtil.isNotNull(view)?view.feeTable.getRecords():rView.feeTable.getRecords();
		if(records == null || records.length == 0){
			MSGUtil.sayError("请选择需要删除的费用明细!");
			return;
		}
		if(records != null && records.length > 0 ) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    doSth(records);
                }
            });
		}
		
		
	}
	
	private void doSth(ListGridRecord[] records){
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		StringBuffer num = new StringBuffer();
		String sett_no=ObjUtil.isNotNull(view)?view.table.getSelectedRecord().getAttribute("SETT_NO"):rView.table.getSelectedRecord().getAttribute("SETT_NO");
		
		if(ObjUtil.isNotNull(rView)){
			tableName = "BILL_DETAIL_REC";
		}
		
		//合计总费用
		Double tot_cash = 0.00;
		
		final ArrayList<ListGridRecord> list = new ArrayList<ListGridRecord>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		for(int i=0;i<records.length;i++){
			num.append(",'");
			num.append(records[i].getAttributeAsString("ID"));
			num.append("'");
//			tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("DUE_FEE")));
			if(records[i].getAttribute("FEE_TYPE").equals("PLUS")){
				tot_cash = tot_cash + Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}else if(records[i].getAttribute("FEE_TYPE").equals("MINUS")){
				tot_cash = tot_cash - Double.valueOf((records[i].getAttribute("DUE_FEE")));
			}
			map.put(i+"", i+"");
		}
		
		
		//合并结算单原有明细数据
		
		ListGridRecord[] oldRecords =ObjUtil.isNotNull(view)? view.feeTable.getRecords():view.feeTable.getRecords();
		
		for(int i=0;i<oldRecords.length;i++){
			if(!ObjUtil.isNotNull(map.get(i+""))){
				list.add(oldRecords[i]);
			}
		}
		
		tot_cash =Double.valueOf((
				ObjUtil.isNotNull(view)?view.table.getSelectedRecord().getAttribute("SETT_CASH")
						:rView.table.getSelectedRecord().getAttribute("SETT_CASH"))) - tot_cash ;
		
		//费用信息取消关联
		sql.append("update ");
		sql.append(tableName);
		sql.append(" set sett_no=''");
		sql.append(" where id in (");
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
		
		if(ObjUtil.isNotNull(view))
			view.table.getSelectedRecord().setAttribute("SETT_CASH", tot_cash);
		else
			rView.table.getSelectedRecord().setAttribute("SETT_CASH", tot_cash);
		
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
						rView.feeTable.setRecords((ListGridRecord[])list.toArray(new ListGridRecord[list.size()]));
						rView.table.updateData(rView.table.getSelectedRecord());
						rView.table.redraw();
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
