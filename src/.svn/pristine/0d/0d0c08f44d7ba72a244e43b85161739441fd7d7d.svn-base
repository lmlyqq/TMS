package com.rd.client.action.settlement;



import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.ShpmFeeWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
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
public class DeletePayShpmAction implements ClickHandler {

//	private SuplrFeeSettView view;
	
	private Map<String,String> record;
	
	private ValuesManager vm;
	
	private SGTable table;
	
	private ShpmFeeWin win;
	
	public DeletePayShpmAction(ValuesManager vm,SGTable table,ShpmFeeWin win){
//		this.view = view;
		this.vm = vm;
		this.table = table;
		this.win = win;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(ClickEvent event) {	
		
		record = vm.getValues();
		
		if(ObjUtil.isNotNull(record)) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                   doDel();
                }
            });
		}
		
	}
	
	private void doDel(){
		Util.async.excuteSQL(getSQL(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				
				
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
				MSGUtil.showOperSuccess();
				win.hide();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	private String getSQL(){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from bill_detail_pay where doc_no='");
		sql.append(record.get("DOC_NO"));
		sql.append("' and fee_id='");
		sql.append(record.get("FEE_ID"));
		sql.append("'");
		
		return sql.toString();
	}

}
