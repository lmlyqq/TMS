package com.rd.client.action.tms;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.ChangeRDCView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CancelChgRDCAction implements ClickHandler{
	private SGTable table;
	private String status;
	private String status_name;
	private boolean chg_flag;
	private ChangeRDCView view;
	
	public CancelChgRDCAction(SGTable table ,String status,String status_name,boolean chg_flag,ChangeRDCView view){
		this.table = table;
		this.status = status;
		this.status_name = status_name;
		this.chg_flag = chg_flag;
		this.view = view;
	}
	@Override
	public void onClick(ClickEvent event) {
		final ListGridRecord[] recs = table.getSelection();
		StringBuffer sf = new StringBuffer();
		if (recs != null && recs.length > 0) {
			sf.append("update tms_change_rdc set status = '"+status+"',status_name = '"+status_name+"'");
			if (chg_flag) {
				sf.append(",changed_flag = 'N'");
			}
			sf.append(" where rdc_no in(");
			StringBuffer sb = new StringBuffer();
			for(ListGridRecord rec : recs){
				sb.append("'");
				sb.append(rec.getAttribute("RDC_NO"));
				sb.append("',");
			}
			sb = new StringBuffer(sb.substring(0,sb.length()-1));
			sf.append(sb + ")");
		}else {
			MSGUtil.sayError("请选择转仓单");
			return;
		}
		
		Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if (StaticRef.SUCCESS_CODE.equals(result.substring(0, 2))) {
					MSGUtil.showOperSuccess();
					table.invalidateCache();
					table.discardAllEdits();

					for(ListGridRecord rec : recs){
						rec.setAttribute("STATUS", status);
						rec.setAttribute("STATUS_NAME", status_name);
						table.selectRecord(table.getRecordIndex(rec));
					}
					table.redraw();
					view.initButton(!chg_flag, !chg_flag, chg_flag, chg_flag, false);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
		});
	}

}
