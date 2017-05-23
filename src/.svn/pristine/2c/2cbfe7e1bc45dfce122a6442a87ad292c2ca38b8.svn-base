package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 供应商费用结算--调度单展现--费用删除按钮
 * @author fangliangmeng
 *
 */
public class DeletePayFeeAction implements ClickHandler {

	private ValuesManager form;
	
	private SuplrFeeSettView view;
	
	private Map<String, String> record;
	
	private String proName="SP_SETT_SAV_PAY_FEE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	public DeletePayFeeAction(SuplrFeeSettView view,ValuesManager form){
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
									criteria.addCriteria("LOAD_NO", view.loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
									view.feeTable.fetchData(criteria);
									form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
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
		list.add(view.loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
		list.add(record.get("DOC_NO"));
		list.add(record.get("FEE_ID"));
		list.add(form.getItem("FEE_ID").getDisplayValue());
		list.add(record.get("FEE_BAS"));
		list.add(record.get("BAS_VALUE").toString());
		list.add(String.valueOf(record.get("PRICE")));
		list.add(ObjUtil.ifObjNull(record.get("DUE_FEE"),"0").toString());
		list.add(ObjUtil.ifObjNull(record.get("PAY_FEE"),"0").toString());
		if(ObjUtil.isNotNull(record.get("PRE_PAY_TIME"))) {
			list.add(record.get("PRE_PAY_TIME").toString());
		}
		else {
			list.add(null);
		}
		list.add(record.get("NOTES"));
		list.add("D");
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}
	
}
