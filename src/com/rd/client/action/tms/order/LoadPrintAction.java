package com.rd.client.action.tms.order;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.OrderView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 *  托运单--提货单打印
 * @author fanglm
 * @createtime 2011-1-18 10:48
 */
public class LoadPrintAction implements ClickHandler{

	//调度单信息二级表
	private SGTable table;
	private OrderView view;
	private String printType;
	private String odr_no = "";
	public LoadPrintAction(OrderView view, String printType){
		this.view = view;
		this.printType = printType;
	}
	@Override
	public void onClick(ClickEvent event) {
		
		table  = view.table;
		
		if(table == null)
			return;
		
		ListGridRecord[] records = table.getSelection();

		if(table.getSelectedRecord() == null){
			MSGUtil.sayError("请先选择作业单!");
			return;
		}
		if(records.length > 1){
			odr_no = "";
			for (ListGridRecord record : records) {
				String odrNo = record.getAttribute("ODR_NO");
				odr_no += odrNo + ",";
			}
			odr_no = odr_no.substring(0, odr_no.length() - 1);
		}else{
			 if(!"coldTransport_label".equals(printType)){
				 odr_no = "'"+table.getSelectedRecord().getAttribute("ODR_NO")+"'";
			 }else{
				 odr_no = table.getSelectedRecord().getAttribute("ODR_NO");
			 }
		}
		
		view.loadBillButton.setShowDisabled(false);
		
		Util.async.dispatchPrintView("odrNo",odr_no,printType, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				com.google.gwt.user.client.Window.open(result, "", "");
				view.loadBillButton.setShowDisabled(true);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}

}
