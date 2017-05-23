package com.rd.client.action.settlement.settle;

import java.util.HashMap;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.InvoiceView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 新增发票信息
 * @author fangliangmeng
 *
 */
public class NewInvoiceAction implements ClickHandler{

	private SGTable table = null;
	private HashMap<String, String> initMap = new HashMap<String, String>();
	private InvoiceView view;
	
	
	public NewInvoiceAction(SGTable p_table,InvoiceView view) {
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		newAction();
	}
	
	private void newAction(){
		ListGridRecord record = view.table.getSelectedRecord();
		if(!ObjUtil.isNotNull(record)){
			MSGUtil.sayError("请先选择结算单，再新增核销单据！");
			return;
		}
		view.initInvoiceBtn(2);
		table.startEditingNew();
		table.OP_FLAG = "A";
		
		initMap.put("GRP_ID", record.getAttribute("ID"));
		initMap.put("INVO_TYPE", "PT");
		initMap.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		initMap.put("ADDTIME", Util.getCurTime());
		initMap.put("CASH", String.valueOf((Double.valueOf(record.getAttribute("SETT_CASH")) - Double.valueOf(ObjUtil.ifNull(record.getAttribute("INVO_CASH"),"0")))));
		
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
