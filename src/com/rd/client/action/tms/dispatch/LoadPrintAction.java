package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *  调度配载--提货单打印预览
 * @author fanglm
 * @createtime 2010-12-31 10:48
 */
public class LoadPrintAction implements ClickHandler{

	//调度单信息二级表
	private SGTable table;
	private DispatchView view;
	private SGTable loadTable;
	private String json;
	public LoadPrintAction(DispatchView view, SGTable p_table){
		this.view = view;
		this.loadTable = p_table;
	}
	@Override
	public void onClick(ClickEvent event) {
		
		table  = view.shpmTable;
		String load_no="";
//		String shpm_no="";
		HashMap<String,String> shpm_no = new HashMap<String,String>();
		
		if(loadTable.getSelectedRecord() == null){
			return;
		}
		
		StringBuffer ids = new StringBuffer();
		
		if(table == null){
			load_no = loadTable.getSelectedRecord().getAttributeAsString("LOAD_NO");
			shpm_no.put("1", " ");
		}else{
			ListGridRecord[] records = table.getSelection();
			if(records.length < 1){
				load_no = loadTable.getSelectedRecord().getAttributeAsString("LOAD_NO");
				shpm_no.put("1", " ");
			}else{
				for(int i=0;i<records.length;i++){
					ids.append("'");
					ids.append(records[i].getAttribute("SHPM_NO"));
					ids.append("',");
					
					load_no = records[i].getAttribute("LOAD_NO");
					shpm_no.put(String.valueOf(i+1), records[i].getAttribute("SHPM_NO"));
					
				}
			}
		}
		
		if(ids.length() == 0){
			ids.append(",");
		}
		
//		Util.async.loadPrintView(ids.substring(0,ids.length()-1),load_no,shpm_no, true,new AsyncCallback<String>() {
//			
//			@Override
//			public void onSuccess(String result) {
//				if(result.substring(0,2).equals(StaticRef.FAILURE_CODE)){
//					SC.warn(result.substring(2));
//				}else{
//					com.google.gwt.user.client.Window.open(result, "", "");
//				}
//			}
//			
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		
		String user = LoginCache.getLoginUser().getUSER_NAME();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		listmap.put("1", load_no);
		listmap.put("2", "1");
		listmap.put("3", shpm_no);
		listmap.put("4", user);
		listmap.put("5", " ");
	    json = Util.mapToJson(listmap);
		
		Util.async.loadPrintView(ids.substring(0,ids.length()-1),json,load_no, true,new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				
				/*String [] res = result.split("@");
				
				if(result.substring(0,2).equals(StaticRef.FAILURE_CODE)){
					SC.warn(result.substring(2));
				}else{
					com.google.gwt.user.client.Window.open(res[0], "", "");
				}*/
				new LoadPrintWin(result, json, loadTable, table, false);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
}
