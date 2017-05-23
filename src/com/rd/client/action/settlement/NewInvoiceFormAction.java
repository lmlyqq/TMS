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
public class NewInvoiceFormAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{

	private SGTable table;
	private DynamicForm form;
	private Map<String, String> initMap;
	private SGForm view;
	//private ValuesManager vm;
	public NewInvoiceFormAction(DynamicForm p_form, Map<String, String> map) {
		form = p_form;
		initMap = map;
	}
	
	public NewInvoiceFormAction(DynamicForm p_form, Map<String, String> map,SGForm view) {
		form = p_form;
		initMap = map;
		this.view = view;
	}
	public NewInvoiceFormAction(DynamicForm p_form, SGTable sgtable) {
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
			String BUSS_ID=record.getAttribute("BUSS_ID");
			if(BUSS_ID!=null){
				
				Util.db_async.getSingleRecord("BANK,GEN_METHOD,INVOICE_TITLE,ADDRESS,TAXNO,UDF2", "BAS_CUSTOMER", "where ID='"+BUSS_ID+"' ", null, new AsyncCallback<HashMap<String, String>>(){

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(HashMap<String, String> result) {

						if(result != null) {
							
							String BANK=result.get("BANK");
							String GEN_METHOD=result.get("GEN_METHOD");
							String INVOICE_TITLE=result.get("INVOICE_TITLE");
							String ADDRESS=result.get("ADDRESS");
							String TAXNO=result.get("TAXNO");
							String UDF2=result.get("UDF2");
							
							
							form.setValue("BANK_POINT",BANK);
							form.setValue("BANK_ACCOUNT",GEN_METHOD);
							form.setValue("BILL_TO",INVOICE_TITLE);
							form.setValue("INVOICE_ADDRESS",ADDRESS);
							form.setValue("TAX_RATIO",UDF2);
							form.setValue("TAX_NO",TAXNO);
						}
					}
					
				});
						
			}
		}
			form.setValue("INVOICE_BY", LoginCache.getLoginUser().getUSER_ID());
			Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					form.setValue("INVOICE_TIME", result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			form.setValue("INVOICE_NO", table.getSelectedRecord().getAttribute("INVOICE_NO"));
			form.setValue("RECE_AMOUNT","0");
			form.setValue("OP_FLAG", StaticRef.INS_FLAG);
			form.setValue("FEE_NAME", table.getSelectedRecord().getAttribute("BELONG_MONTH")+"运费");
		
			//	form.setValue("REQ_NO", table.getSelectedRecord().getAttribute("REQ_NO"));
		
		}
//			else{
//			vm.editNewRecord();
//			if(initMap != null){
//				if(initMap != null) {
//					Object[] iter = initMap.keySet().toArray();
//					String key = "",i18n_key = "";
//					String value = "";
//					for(int i = 0; i < iter.length; i++) {
//						key = (String)iter[i];
//						i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
//						value = initMap.get(key);
//						vm.setValue(key, ObjUtil.ifNull(value,initMap.get(i18n_key)));
//					}
//				}
//				vm.setValue("OP_FLAG", StaticRef.INS_FLAG);
//			}
//		}
	}
}
