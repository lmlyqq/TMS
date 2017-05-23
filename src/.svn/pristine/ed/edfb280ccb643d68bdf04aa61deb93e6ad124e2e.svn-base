package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 *  调度配载--未生成调度单--提货单打印
 * @author wangjun
 * 
 */
public class UnShpmPrintAction implements com.smartgwt.client.widgets.menu.events.ClickHandler{

	//调度单信息二级表
	@SuppressWarnings("unused")
	private DispatchView view;
	private SGTable unshpmTable;
	public UnShpmPrintAction(DispatchView view, SGTable p_table){
		this.view = view;
		this.unshpmTable = p_table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		
		HashMap<String,String> shpm_no = new HashMap<String,String>();
		if(unshpmTable.getSelectedRecord() == null){
			return;
		}
		
		final ListGridRecord[] records = unshpmTable.getSelection();
		if(records != null && records.length > 0) {
			for(int i = 0 ; i <records.length ; i++){
				Record record = records[i];
				record.getAttribute("PRINT_FLAG");
				record.getAttribute("PLATE_NO");
				if((record.getAttribute("PLATE_NO"))== null) {
					   SC.warn("作业单[" + record.getAttribute("SHPM_NO") + "]无车牌号，不能执行打印操作 !");
					   return;
					}
			    if(record.getAttribute("PRINT_FLAG").equals("N")) {
				   SC.warn("作业单[" + record.getAttribute("SHPM_NO") + "]已打印提货单,请重新获取授权!");
				   return;
				}
			}
	    }
		
		
		StringBuffer ids = new StringBuffer();
		if(records != null && records.length > 0) {
			for(int i=0;i<records.length;i++){
				ids.append("'");
				ids.append(records[i].getAttribute("SHPM_NO"));
				ids.append("',");
				shpm_no.put(String.valueOf(i+1), records[i].getAttribute("SHPM_NO"));
	
			}
		}
		
		if(ids.length() == 0){
			ids.append(",");
		}
		
		String user = LoginCache.getLoginUser().getUSER_NAME();
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		String load_no=" ";
		
		listmap.put("1", ObjUtil.ifObjNull(load_no," "));
		listmap.put("2", "1");
		listmap.put("3", shpm_no);
		listmap.put("4", user);
		listmap.put("5", " ");
	    String json = Util.mapToJson(listmap);
		
		Util.async.UnShpmPrintView(ids.substring(0,ids.length()-1),json,false, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.FAILURE_CODE)){
					SC.warn(result.substring(2));
				}else{
					com.google.gwt.user.client.Window.open(result, "", "");
					
					for(int i = 0 ; i <records.length ; i++){
						Record record = records[i];
						record.setAttribute("PRINT_FLAG", "N");
					}
					
					unshpmTable.updateData(unshpmTable.getSelectedRecord()); 

				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
