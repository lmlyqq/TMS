package com.rd.client.action.settlement.tariff;

import java.util.Map;

import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.view.settlement.RateManagerRecView;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 费用协议--规则新增按钮
 * @author fangliangmeng
 *
 */
public class NewTariffRuleRecAction extends NewFormAction{
	private RateManagerRecView view;
	public NewTariffRuleRecAction(DynamicForm pForm, Map<String, String> map,RateManagerRecView view) {
		super(pForm, map);
		this.view = view;
	}
	
	public NewTariffRuleRecAction(ValuesManager pForm, Map<String, String> map,RateManagerRecView view) {
		super(pForm, map);
		this.view = view;
	}
	
	public void click() {
		
		if(!ObjUtil.isNotNull(view.selectRecord)){
			MSGUtil.sayWarning("请先选择合同信息，再执行新增操作！");
	    	return;
		}
		view.initAddRuleBtn();
		view.fee_cache_map.put("TFF_ID", view.selectRecord.getAttributeAsString("ID"));
	
		
		super.click();
	}
}
