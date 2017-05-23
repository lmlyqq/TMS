package com.rd.client.action.settlement.settle;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.InvoiceView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 删除发票信息
 * @author fangliangmeng
 *
 */
public class DeleteInvoiceAction implements ClickHandler{

	private SGTable table = null;
	private InvoiceView view;
	private String proName = "SP_SETT_SAV_DEL_INVOICE(?,?,?,?,?,?,?,?)";
	private ListGridRecord record = null;
	
	public DeleteInvoiceAction(SGTable p_table,InvoiceView view) {
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		record = table.getSelectedRecord();
		if(ObjUtil.isNotNull(record)) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                   doSth();
                }
            });
		}
	}
	
	private void doSth(){
		if(ObjUtil.isNotNull(record.getAttribute("ID"))){
			ArrayList<String> valList = new ArrayList<String>();
			valList.add(record.getAttribute("ID"));
			valList.add(" ");
			valList.add(record.getAttribute("GRP_ID"));
			valList.add("DELETE");
			valList.add(String.valueOf(record.getAttribute("CASH")));
			valList.add(LoginCache.getLoginUser().getUSER_ID());
			valList.add("");
			
			
			Util.async.execProcedure(valList, proName, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.showOperSuccess();
						
						String[] res = result.split(",");
						view.table.getSelectedRecord().setAttribute("INVO_STAT", res[1]);
						view.table.getSelectedRecord().setAttribute("INVO_STAT_NAME", res[2]);
						view.table.getSelectedRecord().setAttribute("INVO_CASH",res[3]);
						view.table.updateData(view.table.getSelectedRecord());
						view.table.redraw();
						view.table.selectRecord(0);
						
						
						Criteria cc = new Criteria();
						cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						cc.addCriteria("GRP_ID",view.table.getSelectedRecord().getAttributeAsString("GRP_ID"));
						view.invoiceTable.invalidateCache();
						view.invoiceTable.fetchData(cc);
						
						
					}else{
						MSGUtil.sayError(result.substring(2));
					}
					
					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}else{
			table.discardAllEdits();
		}
	}
	

}
