package com.rd.client.action.settlement.settle;

import java.util.HashMap;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.InvoiceView;
import com.rd.client.view.settlement.SettlementRecView;
import com.rd.client.view.settlement.SettlementView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 新增核销单据
 * @author fangliangmeng
 *
 */
public class NewVerifiAction implements ClickHandler{

	private SGTable table = null;
	private HashMap<String, String> initMap = new HashMap<String, String>();
	private SettlementView view;
	private InvoiceView invoView;
	private SettlementRecView rview;
	
	
	public NewVerifiAction(SGTable p_table,SettlementView view) {
		table = p_table;
		this.view = view;
	}
	
	public NewVerifiAction(SGTable p_table,InvoiceView view) {
		table = p_table;
		this.invoView = view;
	}
	
	
	public NewVerifiAction(SGTable p_table,SettlementRecView view){
		table = p_table;
		this.rview = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(view != null || rview != null){
			newAction();
		}else if(invoView != null){
			newInvAction();
		}
	}
	
	private void newAction(){
		ListGridRecord record = view.table.getSelectedRecord();
		if(!ObjUtil.isNotNull(record)){
			MSGUtil.sayError("请先选择结算单，再新增核销单据！");
			return;
		}
		if(ObjUtil.isNotNull(view)){
			view.initVerifiBtn(2);
		}else{
			rview.initVerifiBtn(2);
		}
		table.startEditingNew();
		table.OP_FLAG = "A";
		
		initMap.put("SETT_NO", record.getAttribute("SETT_NO"));
		initMap.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		initMap.put("ADDTIME", Util.getCurTime());
		initMap.put("CASH", String.valueOf((Double.valueOf(record.getAttribute("SETT_CASH")) - Double.valueOf(record.getAttribute("VERIFI_CASH")))));
		
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					table.setEditValue(table.getRecords().length,key, LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					if(value.equals("Y") || value.equals("N")) {
						table.setEditValue(table.getRecords().length, key, value.equals("Y"));
					}
					else {
						table.setEditValue(table.getRecords().length, key, ObjUtil.ifNull(initMap.get(i18n_key), value));
					}
				}
			}
		}
		
	}
	
	private void newInvAction(){
		ListGridRecord record = invoView.table.getSelectedRecord();
		if(!ObjUtil.isNotNull(record)){
			MSGUtil.sayError("请先选择发票组，再新增核销单据！");
			return;
		}
		invoView.initVerifiBtn(2);
		table.startEditingNew();
		table.OP_FLAG = "A";
		
		initMap.put("INVO_GRP_ID", record.getAttribute("ID"));
		initMap.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		initMap.put("ADDTIME", Util.getCurTime());
		initMap.put("CASH", String.valueOf((Double.valueOf(record.getAttribute("INVO_CASH")) - Double.valueOf(ObjUtil.ifNull(record.getAttribute("VERIFI_CASH"),"0")))));
		
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					table.setEditValue(table.getRecords().length,key, LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					if(value.equals("Y") || value.equals("N")) {
						table.setEditValue(table.getRecords().length, key, value.equals("Y"));
					}
					else {
						table.setEditValue(table.getRecords().length, key, ObjUtil.ifNull(initMap.get(i18n_key), value));
					}
				}
			}
		}
		
	}

}
