package com.rd.client.action.tms.shpmreceipt;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsShpmReceiptView;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

	/**
	 * 作业单回单-->货损货差-->保存
	 * @author lijun
	 *
	 */
public class NewShpmManageAction implements ClickHandler {
	private TmsShpmReceiptView view;
	private SGTable table;
	private int row;
	private TmsTrackView tView;
	
	public NewShpmManageAction(TmsShpmReceiptView view,SGTable table){
		this.view = view;
		this.table = table;
	}
	public NewShpmManageAction(TmsTrackView view,SGTable table){
		this.tView = view;
		this.table = table;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(view != null){
			if(ObjUtil.isNotNull(view.headTable)){
				if(ObjUtil.isNotNull(view.headTable.getSelection())){
					ListGridRecord[] shpmrecords = view.headTable.getSelection();
					if(!ObjUtil.isNotNull(view.headTable.getSelectedRecord())){
			            MSGUtil.sayError("未选择相应的作业单，请确认！");
				    } else if(view.headTable.getSelection().length != 1) {
				    	MSGUtil.sayError("只能进行单条记录进行！");
				    } else {
				    	
				    	table.startEditingNew();
				    	row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				    	view.itemRow = row;
				    	table.setEditValue(row,"SHPM_NO",shpmrecords[0].getAttribute("SHPM_NO"));
				    	table.setEditValue(row, "LOAD_NO",shpmrecords[0].getAttribute("LOAD_NO"));
				    	table.setEditValue(row, "ODR_NO", shpmrecords[0].getAttribute("ODR_NO"));
				    	table.setEditValue(row, "CUSTOMER_ID",shpmrecords[0].getAttribute("CUSTOMER_ID"));
				    	table.setEditValue(row, "SUPLR_ID", shpmrecords[0].getAttribute("SUPLR_ID"));
				    	table.setEditValue(row, "EXEC_ORG_ID", shpmrecords[0].getAttribute("EXEC_ORG_ID"));
				    	table.OP_FLAG = StaticRef.INS_FLAG;
				    }
				} else {
					SC.warn("请选择所要追踪的记录");
					return;
				}
			} else {
				SC.warn("请选择所要追踪的记录");
				return;
			}
			view.initAddBtn();
		}
		if(tView != null){
			if(ObjUtil.isNotNull(tView.shpmTable)){
				if(ObjUtil.isNotNull(tView.shpmTable.getRecords())){
					ListGridRecord[] shpmrecords = tView.shpmTable.getSelection();
					if(!ObjUtil.isNotNull(tView.shpmTable.getSelectedRecord())){
			            MSGUtil.sayError("未选择相应的作业单，请确认！");
				    } else if(shpmrecords.length != 1) {
				    	MSGUtil.sayError("只能进行单条记录进行！");
				    } else {
				    	
				    	table.startEditingNew();
				    	row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				    	tView.itemRow = row;
				    	table.setEditValue(row,"SHPM_NO",shpmrecords[0].getAttribute("SHPM_NO"));
				    	table.setEditValue(row, "LOAD_NO",shpmrecords[0].getAttribute("LOAD_NO"));
				    	table.setEditValue(row, "ODR_NO", shpmrecords[0].getAttribute("ODR_NO"));
				    	table.setEditValue(row, "CUSTOMER_ID",shpmrecords[0].getAttribute("CUSTOMER_ID"));
				    	table.setEditValue(row, "SUPLR_ID", shpmrecords[0].getAttribute("SUPLR_ID"));
				    	table.setEditValue(row, "EXEC_ORG_ID", shpmrecords[0].getAttribute("EXEC_ORG_ID"));
				    	table.OP_FLAG = StaticRef.INS_FLAG;
				    }
				} else {
					SC.warn("请选择所要追踪的记录");
					return;
				}
			} else {
				SC.warn("请选择所要追踪的记录");
				return;
			}
			tView.initDMAddBtn();
		}
	}

}
