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
 * 保存核销单据
 * @author fangliangmeng
 *
 */
public class SaveVerifiAction implements ClickHandler{

	private SGTable table = null;
	private SGTable ftable = null;
	private String proName = "SP_SETT_SAV_DEL_VERIFI(?,?,?,?,?,?,?,?)";
	private int[] edit_rows;
	private InvoiceView invoView;
	
	public SaveVerifiAction(SGTable p_table, SGTable f_table) {
		table = p_table;
		this.ftable = f_table;
	}
	
	public SaveVerifiAction(SGTable p_table,InvoiceView view) {
		table = p_table;
		this.invoView = view;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(invoView != null){
			saveInvAction();
		}else{
			saveAction();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveAction(){
		edit_rows = table.getAllEditRows();   //获取所有修改过的记录行
		HashMap<String, String> vMap = (HashMap<String, String>)table.getEditValues(edit_rows[0]);
		String op_flag = ObjUtil.isNotNull(vMap.get("ID"))?"UPDATE":"INSERT";
		ArrayList<String> valList = new ArrayList<String>();
		valList.add(vMap.get("ID"));
		valList.add(vMap.get("SETT_NO"));
		valList.add("");
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
					ftable.getSelectedRecord().setAttribute("VERIFI_STAT", res[1]);
					ftable.getSelectedRecord().setAttribute("VERIFI_STAT_NAME", res[2]);
					ftable.getSelectedRecord().setAttribute("VERIFI_CASH",res[3]);
					ftable.updateData(ftable.getSelectedRecord());
					ftable.redraw();
					
					
					Criteria cc = new Criteria();
					cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					cc.addCriteria("SETT_NO",ftable.getSelectedRecord().getAttributeAsString("SETT_NO"));
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
	}

	@SuppressWarnings("unchecked")
	private void saveInvAction(){
		edit_rows = table.getAllEditRows();   //获取所有修改过的记录行
		HashMap<String, String> vMap = (HashMap<String, String>)table.getEditValues(edit_rows[0]);
		String op_flag = ObjUtil.isNotNull(vMap.get("ID"))?"UPDATE":"INSERT";
		ArrayList<String> valList = new ArrayList<String>();
		valList.add(vMap.get("ID"));
		valList.add("");
		valList.add(vMap.get("INVO_GRP_ID"));
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
					invoView.table.getSelectedRecord().setAttribute("VERIFI_STAT", res[1]);
					invoView.table.getSelectedRecord().setAttribute("VERIFI_STAT_NAME", res[2]);
					invoView.table.getSelectedRecord().setAttribute("VERIFI_CASH",res[3]);
					invoView.table.updateData(invoView.table.getSelectedRecord());
					invoView.table.redraw();
					
					
					Criteria cc = new Criteria();
					cc.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
					cc.addCriteria("INVO_GRP_ID",invoView.table.getSelectedRecord().getAttributeAsString("ID"));
					invoView.itemTable.invalidateCache();
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
	}

}
