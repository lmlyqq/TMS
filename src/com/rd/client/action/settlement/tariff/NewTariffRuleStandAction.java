package com.rd.client.action.settlement.tariff;

import java.util.Map;

import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.view.settlement.PoleManagerView;
import com.rd.client.view.settlement.StandardRateView;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 费用协议--规则新增按钮
 * @author fangliangmeng
 *
 */
public class NewTariffRuleStandAction extends NewFormAction{
	private StandardRateView view;
	private PoleManagerView poleView;
	private boolean isTff = true;
	public NewTariffRuleStandAction(ValuesManager pForm, Map<String, String> map,StandardRateView view) {
		super(pForm, map);
		this.view = view;
	}
	
	public NewTariffRuleStandAction(ValuesManager pForm, Map<String, String> map,PoleManagerView view,boolean isTff) {
		super(pForm, map);
		this.poleView = view;
		this.isTff = isTff;
	}
	public NewTariffRuleStandAction(DynamicForm pForm, Map<String, String> map,PoleManagerView view,boolean isTff) {
		super(pForm, map);
		this.poleView = view;
		this.isTff = isTff;
	}
	
	
	public void click() {
		
		if(isTff){
			if(!ObjUtil.isNotNull(view.selectRecord)){
				MSGUtil.sayWarning("请先选择合同信息，再执行新增操作！");
		    	return;
			}
			view.initAddRuleBtn();
			view.fee_cache_map.put("TFF_ID", view.selectRecord.getAttributeAsString("ID"));
		}else{
			if(!ObjUtil.isNotNull(poleView.selectRecord)){
				MSGUtil.sayWarning("请先选择主信息，再执行新增操作！");
		    	return;
			}
			poleView.initAddRuleBtn();
			poleView.fee_cache_map.put("TFF_ID", poleView.selectRecord.getAttributeAsString("ID"));
		}
		
		super.click();
	}
}
