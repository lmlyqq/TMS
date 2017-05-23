package com.rd.client.action.settlement.tariff;


import java.util.HashMap;

import com.rd.client.common.action.NewAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.SuplrRateManagerView;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * 费用协议--条件新增按钮
 * @author fangliangmeng
 *
 */
public class NewSuplrTariffCondAction extends NewAction{
	
	private SuplrRateManagerView view;

	public NewSuplrTariffCondAction(SGTable pTable,HashMap<String, String> initMap,SuplrRateManagerView view) {
		super(pTable);
		this.view  = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		if(!ObjUtil.isNotNull(view.selectRuleRecord)){
			MSGUtil.sayWarning("请先选择费率信息，再执行新增操作！");
	    	return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TFF_ID", view.selectRuleRecord.getAttributeAsString("TFF_ID"));
		map.put("RUL_ID", view.selectRuleRecord.getAttributeAsString("ID"));
		map.put("LEFT_BRKT", "(");
		map.put("RIGHT_BRKT", ")");
		map.put("LINK", "AND");
		view.initAddCondBtn();
		super.setInitMap(map);
		super.onClick(event);
	}
	
}
