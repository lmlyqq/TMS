package com.rd.client.action.tms.order;



import java.util.HashMap;
import java.util.Map;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 运输管理--托运单管理--托运单明细--右键--新增
 * @author fanglm
 *
 */
public class NewOrderBillAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{

	private SGTable table = null;
	private OrderView view;
	private int row;
	
	
	public NewOrderBillAction(SGTable p_table,OrderView view) {
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(MenuItemClickEvent event) {
		table.OP_FLAG = "A";
		
		if(!StaticRef.ORD_STATUS_CREATE.equals(view.vm.getValueAsString("STATUS"))){
			MSGUtil.sayError("订单非【" + StaticRef.SO_CREATE_NAME + "】状态，不能新增费用明细！");
			return;
		}
		
		create(false);
	}
	public void create(boolean bool){
		Map<String,String> map = new HashMap<String, String>();
		map.put("BAS_VALUE", "0");
		map.put("PRICE", "0");
		map.put("PRE_FEE", "0");
		map.put("DISCOUNT_RATE", "1");
		map.put("DUE_FEE", "0");
		map.put("PAY_FEE", "0");
		table.OP_FLAG = "A";
		table.startEditingNew(map);
		row = table.getAllEditRows()[table.getAllEditRows().length-1];
		view.itemRow = row;

		table.setEditValue(row, "PACK_ID", "CDA046CDE9824B648E27273CF9656CA4");
		
		table.setEditValue(view.itemRow, "OP_FLAG", "A");
		table.setEditValue(view.itemRow, "DOC_NO", view.vm.getValueAsString("ODR_NO"));
	}

	@Override
	public void onClick(ClickEvent event) {
		table.OP_FLAG = "A";
		if(!StaticRef.ORD_STATUS_CREATE.equals(view.vm.getValueAsString(("STATUS")))){
			MSGUtil.sayError("订单非【" + StaticRef.SO_CREATE_NAME + "】状态，不能新增明细！");
			return;
		}
		
		create(false);
	}
}
