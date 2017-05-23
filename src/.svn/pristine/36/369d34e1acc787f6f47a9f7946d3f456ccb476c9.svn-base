package com.rd.client.action.settlement.tariff;


import java.util.HashMap;

import com.rd.client.common.action.NewAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.PoleManagerView;
import com.rd.client.view.settlement.RateManagerView;
import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * 费用协议--条件新增按钮
 * @author fangliangmeng
 *
 */
public class NewTariffCondAction extends NewAction{
	
	private RateManagerView view;
	
	private PoleManagerView poleView;
	
	private boolean isTff = true;
	
	public NewTariffCondAction(SGTable pTable,HashMap<String, String> initMap,RateManagerView view) {
		super(pTable);
		this.view  = view;
	}
	
	public NewTariffCondAction(SGTable pTable,HashMap<String, String> initMap,PoleManagerView view,boolean isTff) {
		super(pTable);
		this.poleView  = view;
		this.isTff = isTff;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		if(!ObjUtil.isNotNull(isTff?view.selectRuleRecord:poleView.selectRuleRecord)){
			MSGUtil.sayWarning("请先选择费率信息，再执行新增操作！");
	    	return;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TFF_ID", view.selectRuleRecord.getAttributeAsString("TFF_ID"));
		map.put("RUL_ID", view.selectRuleRecord.getAttributeAsString("ID"));
		map.put("LEFT_BRKT", "(");
		map.put("RIGHT_BRKT", ")");
		map.put("LINK", "AND");
		if(isTff)
			view.initAddCondBtn();
		else
			poleView.initAddCondBtn();
		super.setInitMap(map);
		super.onClick(event);
	}
	
}
