package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.Map;

import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.CustomFeeSettView;

/**
 * 供应商费用结算--调度单展现--费用删除按钮
 * @author fangliangmeng
 *
 */
public class DeleteReceFeeAction implements ClickHandler {

	
	private CustomFeeSettView view;
	private ValuesManager form;
	private Map<String, Object> record;
	private String proName="SP_SETT_SAV_REC_FEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public DeleteReceFeeAction(CustomFeeSettView view,ValuesManager form){
		this.view = view;
		this.form = form;
	}
	
	 
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {	
		
		record = form.getValues(); 
		
		if(ObjUtil.isNotNull(record)) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
					if (value != null && value) {
						Util.async.execProcedure(getList(), proName, new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(String result) {
								if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
									MSGUtil.showOperSuccess();
									view.feeTable.invalidateCache();
									Criteria criteria = new Criteria();
									criteria.addCriteria("OP_FLAG","M");
									criteria.addCriteria("ODR_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
									criteria.addCriteria("DOC_NO",view.table.getSelectedRecord().getAttributeAsString("ODR_NO"));
									view.feeTable.fetchData(criteria);
								}else{
									MSGUtil.sayError(result.substring(2));
								}
							}
							
						});
					}	 
                }
            });
		}
		
	}
	
	private ArrayList<String> getList(){
		ArrayList<String> list= new ArrayList<String>();
		list.add(record.get("ID").toString());
		list.add(record.get("DOC_NO").toString());
		list.add(record.get("FEE_ID").toString());
		list.add(form.getItem("FEE_ID").getDisplayValue());
		list.add(record.get("FEE_BAS").toString());
		list.add(record.get("BAS_VALUE").toString());
		list.add(String.valueOf(record.get("PRICE")));
		list.add(ObjUtil.ifObjNull(record.get("DUE_FEE"),"0").toString());
		list.add(ObjUtil.ifObjNull(record.get("PAY_FEE"),"0").toString());
		if(ObjUtil.isNotNull(record.get("PRE_RECE_TIME"))) {
			list.add(record.get("PRE_RECE_TIME").toString());
		}
		else {
			list.add(null);
		}
		list.add(ObjUtil.ifObjNull(record.get("DISCOUNT_RATE"),"1").toString());
		list.add(ObjUtil.ifObjNull(record.get("NOTES"),"").toString());
		list.add("D");
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}
	
}
	
