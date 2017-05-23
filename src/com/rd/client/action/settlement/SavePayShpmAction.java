package com.rd.client.action.settlement;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.ShpmFeeWin;
import com.rd.client.view.settlement.SuplrFeeSettView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;

/**
 * 供应商费用结算--作业单展现--费用保存按钮
 * @author fangliangmeng
 *
 */
public class SavePayShpmAction implements ClickHandler {

	private ValuesManager form;
	
	@SuppressWarnings("unused")
	private SuplrFeeSettView view;
	
	private Map<String, String> record;
	
	@SuppressWarnings("unused")
	private ShpmFeeWin win;
	
//	private String op_flag;
	private SGTable table;
	
	private String proName="SP_SETT_SAV_PAY_SHPM(?,?,?,?,?,?,?,?,?,?,?)";
	
	public SavePayShpmAction(SuplrFeeSettView view,ValuesManager form,SGTable table,ShpmFeeWin win){
		this.view = view;
		this.form = form;
		this.table = table;
		this.win = win;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {	
//		op_flag = ObjUtil.ifNull(form.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG);
        record = form.getValues(); 
//        record.remove("OP_FLAG");
        
        Util.async.execProcedure(getList(), proName, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
//				MSGUtil.showOperSuccess();
				
				Criteria cc = new Criteria();
				cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				cc.addCriteria("DOC_NO",record.get("DOC_NO"));
				table.invalidateCache();
				table.fetchData(cc,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						MSGUtil.showOperSuccess();
					}
				});
				
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private ArrayList<String> getList(){
		
		ArrayList<String> list= new ArrayList<String>();
		list.add(String.valueOf(form.getItem("DOC_NO").getValue()));
		list.add(String.valueOf(form.getItem("FEE_ID").getValue()));
		list.add(String.valueOf(form.getItem("FEE_BAS").getValue()));
		list.add(String.valueOf(form.getItem("BAS_VALUE").getValue()));
		list.add(String.valueOf(form.getItem("PRICE").getValue()));
		list.add(String.valueOf(form.getItem("MILE").getValue()));
		list.add(String.valueOf(form.getItem("DUE_FEE").getValue()));
		list.add(form.getValueAsString("IS_RDC")==null||form.getValueAsString("IS_RDC").equals("false") ?"N":"Y");
		list.add(form.getValueAsString("UP_DOWN")==null||form.getValueAsString("UP_DOWN").equals("false") ?"N":"Y");
		list.add(LoginCache.getLoginUser().getUSER_ID());
		
		return list;
	}

}
