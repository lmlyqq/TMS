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
 * 删除核销单据
 * @author fangliangmeng
 *
 */
public class DeleteVerifiAction implements ClickHandler{

	private SGTable table = null;
	private SGTable ftable = null;
	private String proName = "SP_SETT_SAV_DEL_VERIFI(?,?,?,?,?,?,?,?)";
	private ListGridRecord record = null;
	private InvoiceView invoView;
	
	public DeleteVerifiAction(SGTable p_table, SGTable table) {
		this.table = p_table;
		this.ftable = table;
	}
	
	public DeleteVerifiAction(SGTable p_table,InvoiceView view) {
		this.table = p_table;
		this.invoView = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		record = table.getSelectedRecord();
		if(ObjUtil.isNotNull(record)) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	if(ftable != null){
                			delAction();
                		}else{
                			delInvAction();
                		}
                    }
                }
            });
		}
		
	}
	
	private void delAction(){
		
		record = table.getSelectedRecord();
		
		if(ObjUtil.isNotNull(record.getAttribute("ID"))){
			ArrayList<String> valList = new ArrayList<String>();
			valList.add(record.getAttribute("ID"));
			valList.add(record.getAttribute("SETT_NO"));
			valList.add("");
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
						String id = ftable.getSelectedRecord().getAttributeAsString("SETT_NO");
						ftable.getSelectedRecord().setAttribute("VERIFI_STAT", res[1]);
						ftable.getSelectedRecord().setAttribute("VERIFI_STAT_NAME", res[2]);
						ftable.getSelectedRecord().setAttribute("VERIFI_CASH",res[3]);
						ftable.updateData(ftable.getSelectedRecord());
						ftable.redraw();
						ftable.selectRecord(0);
						
						
						Criteria cc = new Criteria();
						cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						cc.addCriteria("SETT_NO",id);
						table.invalidateCache();
						table.discardAllEdits();
						table.fetchData(cc);
						
						
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
	
	private void delInvAction(){
		
		record = table.getSelectedRecord();
		
		if(ObjUtil.isNotNull(record.getAttribute("ID"))){
			ArrayList<String> valList = new ArrayList<String>();
			valList.add(record.getAttribute("ID"));
			valList.add("");
			valList.add(record.getAttribute("INVO_GRP_ID"));
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
						String id = invoView.table.getSelectedRecord().getAttributeAsString("ID");
						invoView.table.getSelectedRecord().setAttribute("VERIFI_STAT", res[1]);
						invoView.table.getSelectedRecord().setAttribute("VERIFI_STAT_NAME", res[2]);
						invoView.table.getSelectedRecord().setAttribute("VERIFI_CASH",res[3]);
						invoView.table.updateData(invoView.table.getSelectedRecord());
						invoView.table.redraw();
						invoView.table.selectRecord(0);
						
						
						Criteria cc = new Criteria();
						cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						cc.addCriteria("INVO_GRP_ID",id);
						invoView.itemTable.invalidateCache();
						invoView.itemTable.discardAllEdits();
						invoView.itemTable.fetchData(cc);
						
						
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
