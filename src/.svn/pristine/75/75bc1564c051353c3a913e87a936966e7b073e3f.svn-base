package com.rd.client.action.settlement.settle;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.settlement.InvoiceView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * 保存发票信息
 * @author fangliangmeng
 *
 */
public class SaveInvoiceAction implements ClickHandler{

	private SGTable table = null;
	private InvoiceView view;
	private String proName = "SP_SETT_SAV_DEL_INVOICE(?,?,?,?,?,?,?,?)";
	private int[] edit_rows;
	
	public SaveInvoiceAction(SGTable p_table,InvoiceView view) {
		table = p_table;
		this.view = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		saveAction();
	}
	
	@SuppressWarnings("unchecked")
	private void saveAction(){
		edit_rows = table.getAllEditRows();   //获取所有修改过的记录行
		HashMap<String, String> vMap = (HashMap<String, String>)table.getEditValues(edit_rows[0]);
		String op_flag = ObjUtil.isNotNull(vMap.get("ID"))?"UPDATE":"INSERT";
		ArrayList<String> valList = new ArrayList<String>();
		valList.add(vMap.get("ID"));
		valList.add(vMap.get("INVO_TYPE"));
		valList.add(vMap.get("GRP_ID"));
		valList.add(op_flag);
		valList.add(String.valueOf(vMap.get("CASH")));
		valList.add(vMap.get("ADDWHO"));
		valList.add(vMap.get("ADDTIME"));
		
		
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
	}

}
