package com.rd.client.action.base.tempeq;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.view.base.BasOilCardView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;

public class NewOilCardDetailAction implements ClickHandler{
	
	private ValuesManager form;
	private Map<String, String> initMap;
	private BasOilCardView view;
	private int initBtn =0;
	
	public NewOilCardDetailAction(ValuesManager form,ListGrid downTable,Map<String, String> initMap,BasOilCardView basOilCardView,int initBtn){
		this.form = form;
		this.initMap = initMap;
		this.view = basOilCardView;
		this.initBtn = initBtn;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(view != null) {
			view.isModify = true;
			if(view.isMax) {
				view.expend();
			}
		}
		if(initBtn > 0){
			view.initBtn(initBtn);
		}
		form.editNewRecord();
		if(initMap != null) {
			Object[] iter = initMap.keySet().toArray();
			String key = "",i18n_key = "";
			String value = "";
			for(int i = 0; i < iter.length; i++) {
				key = (String)iter[i];
				
				if(key.indexOf("CUSTOMER_ID") >= 0 ){
					form.setValue("CUSTOMER_ID", LoginCache.getDefCustomer().get("CUSTOMER_ID")); //要求初始化客户时，查找默认客户
					form.setValue("CUSTOMER_NAME", LoginCache.getDefCustomer().get("CUSTOMER_NAME"));
				}else{
					i18n_key = key + "_NAME";   //前台显示的是NAME的值，而后台取的是ID，所以前台展示和后台ID的差别在于对应的字段多出一个_NAME
					value = initMap.get(key);
					form.setValue(key, ObjUtil.ifNull(value, initMap.get(i18n_key)));
				}
			}
		}
		
		Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				form.setValue("RECHAGE_TIME", result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
		
		
//		form.setValue("ABNORMAL_FLAG", false);
		form.setValue("RECHARGER", view.gpsTable.getSelectedRecord().getAttribute("RECHARGER"));
		form.setValue("OP_FLAG", StaticRef.INS_FLAG);
	}
}