package com.rd.client.action.tms.order;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.ChangeRDCView;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

public class RDCPrintAction implements ClickHandler{

	// 调度单信息二级表
	private SGTable table;
//	private ChangeRDCView view;
	private String printType;
	private String odr_no = "";

	public RDCPrintAction(ChangeRDCView view,SGTable rdctable, String printType) {
//		this.view = view;
		this.table = rdctable;
		this.printType = printType;
	}

	@Override
	public void onClick(MenuItemClickEvent event) {

		if (table == null)
			return;

		ListGridRecord[] records = table.getSelection();

		if (table.getSelectedRecord() == null) {
			MSGUtil.sayError("请先选择转仓单!");
			return;
		}
		if (records.length > 1) {
			odr_no = "";
			for (ListGridRecord record : records) {
				String odrNo = record.getAttribute("RDC_NO");
				odr_no += odrNo + ",";
			}
			odr_no = odr_no.substring(0, odr_no.length() - 1);
		} else {
			odr_no = table.getSelectedRecord().getAttribute("RDC_NO");
		}

//		view.loadBillButton.setShowDisabled(false);

		Util.async.dispatchPrintView("loadNo", odr_no, printType,
				new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						com.google.gwt.user.client.Window.open(result, "", "");
//						view.loadBillButton.setShowDisabled(true);
					}

					@Override
					public void onFailure(Throwable caught) {

					}
				});
	}

}