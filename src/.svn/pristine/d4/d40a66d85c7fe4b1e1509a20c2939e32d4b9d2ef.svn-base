package com.rd.client.action.settlement;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * FORM
 * @author yuanlei
 *
 */
public class NewPayReqInvoiceAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private SGTable table;
	private DynamicForm form;
	private Map<String, String> initMap;
	private SGForm view;
	public NewPayReqInvoiceAction(DynamicForm p_form, Map<String, String> map) {
		form = p_form;
		initMap = map;
	}
	
	public NewPayReqInvoiceAction(DynamicForm p_form, Map<String, String> map,SGForm view) {
		form = p_form;
		initMap = map;
		this.view = view;
	}
	public NewPayReqInvoiceAction(DynamicForm p_form, SGTable sgtable) {
		form = p_form;
		table=sgtable;
		
	}

	@Override
	public void onClick(ClickEvent event) {
		click();
	}

	@Override
	public void onClick(
			com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
		click();
	}
	
	public void click(){
		if(view != null){
			view.initAddBtn();
			
			if(view.isMax) {
				view.expend();
			}
		}
		if(form != null){
			form.editNewRecord();		
			if(initMap != null) {
				Object[] iter = initMap.keySet().toArray();
				String key = "",i18n_key = "";
				String value = "";
				for(int i = 0; i < iter.length; i++) {
					key = (String)iter[i];
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					form.setValue(key, ObjUtil.ifNull(value,initMap.get(i18n_key)));
				}
			}
			ListGridRecord record=table.getSelectedRecord();
			if(record!=null){
				form.setValue("INVOICE_BY", LoginCache.getLoginUser().getUSER_ID());
				form.setValue("REQ_NO", table.getSelectedRecord().getAttribute("REQ_NO"));
				form.setValue("RECE_AMOUNT","0");
				form.setValue("OP_FLAG", StaticRef.INS_FLAG);
				Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						form.setValue("INVOICE_TIME", result);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				Util.db_async.getSingleRecord("UDF2", "BAS_SUPPLIER", " where ID='"+table.getSelectedRecord().getAttribute("SUPLR_ID")+"'", null, new AsyncCallback<HashMap<String,String>>() {
					
					@Override
					public void onSuccess(HashMap<String, String> result) {
						if(result!=null){
							String tax_ratio=result.get("UDF2");
							if(tax_ratio!=null){
								form.setValue("TAX_RATIO", tax_ratio);
							}
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}	
		}
	}
}
