package com.rd.client.action.tms.dispatch;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *  调度配载--送货单打印,随附单打印
 * @author fanglm
 * @createtime 2010-12-30 10:17
 */
public class ShipPrintAction implements ClickHandler{

	//调度单信息二级表
	private SGTable table;
	private DispatchView view;
	private SGTable loadTable;
	private String PRT_TYPE;
	private String id = "";
	private String load_no = "";
	public ShipPrintAction(DispatchView view,SGTable loadTable,String prt_type){
		this.view = view;
		this.loadTable = loadTable;
		this.PRT_TYPE = prt_type;
	}
	@Override
	public void onClick(ClickEvent event) {
		
		table  = view.shpmTable;
		StringBuffer ids = new StringBuffer();
		
		
		if(table == null){
			load_no = loadTable.getSelectedRecord().getAttribute("LOAD_NO");
		}else{
			ListGridRecord[] records = table.getSelection();
			
			if(records.length < 1){
				load_no = loadTable.getSelectedRecord().getAttribute("LOAD_NO");
			}else{
				for(int i=0;i<records.length;i++){
					ids.append("'");
					ids.append(records[i].getAttribute("CUSTOM_ODR_NO"));
					ids.append("',");
					load_no = records[i].getAttribute("LOAD_NO");
				}
			}
		}
		if(ids.length() > 0){
			id = ids.substring(0, ids.length()-1);
		}
		
		if("THDAN".equals(PRT_TYPE) && Integer.parseInt(loadTable.getSelectedRecord().getAttribute("STATUS")) < 50){
			StringBuffer buff = new StringBuffer();
			//fanglm 2011-8-15 租赁仓库例外
			buff.append("select count(1) as NUM from v_shipment_header where 1=1 ");
			/*if(id.length() > 1){
				buff.append("and CUSTOM_ODR_NO in (");
	        	buff.append(id);
	 	        buff.append(") ");
			}*/
			buff.append("and LOAD_NO='");
		    buff.append(load_no);
		    buff.append("' and NVL(whse_attr,' ') <> 'FE5F6CB985BE4F26BCADDB1F54CEC8DE' and status < 40");
		    
		    Util.db_async.getRecordCount(buff.toString(), new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					if(Integer.parseInt(result) > 0){
						SC.warn("调度单[" + loadTable.getSelectedRecord().getAttribute("LOAD_NO") + "]未完全发运,不能打印送货单!");
						return;
					}else{
						doSth();
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		    
			
		}else{
			doSth();
		}
		
		
	}
	
	private void doSth(){
		Util.async.shpmPrintView(id,load_no,PRT_TYPE, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0,2).equals(StaticRef.FAILURE_CODE)){
					MSGUtil.sayError(result.substring(2));
				}else{
					com.google.gwt.user.client.Window.open(result, "", "");
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
