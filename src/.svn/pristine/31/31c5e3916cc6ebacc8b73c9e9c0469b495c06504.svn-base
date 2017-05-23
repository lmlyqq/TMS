package com.rd.client.action.tms.track;

import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 运输跟踪 --> 新增记录
 * @author lijun
 *
 */
public class NewTrasFollowAction implements ClickHandler{

	private TmsTrackView view;
	private SGTable table;
	private int row;

	public NewTrasFollowAction(SGTable table, TmsTrackView view) {
		this.view = view;
		this.table = table;
	}

	private void initverify(ListGridRecord[] shpmrecords) {
		table.setEditValue(row, "TRACER", LoginCache.getLoginUser()
				.getUSER_NAME().toString());
		table.setEditValue(row, "PLATE_NO", view.shpmnorecord
				.getAttribute("PLATE_NO"));
		table.setEditValue(row, "DRIVER", view.shpmnorecord
				.getAttribute("DRIVER"));
		table.setEditValue(row, "MOBILE", view.shpmnorecord
				.getAttribute("MOBILE"));
		table.setEditValue(row, "EXEC_ORG_ID_NAME",LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		table.setEditValue(row, "EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		table.setEditValue(row, "ODR_NO", view.shpmnorecord
				.getAttribute("ODR_NO"));
		if (shpmrecords.length > 1) {
			table.setEditValue(row, "PRE_UNLOAD_TIME", view.shpmnorecord
					.getAttribute("PRE_UNLOAD_TIME"));
		} else if (shpmrecords.length == 1) {
			table.setEditValue(row, "PRE_UNLOAD_TIME", view.shpmnorecord
					.getAttribute("PRE_UNLOAD_TIME"));
		}

	}

	@Override
	public void onClick(ClickEvent event) {
		view.initAddBtn();
		if(ObjUtil.isNotNull(view.shpmTable)){
			if(ObjUtil.isNotNull(view.shpmTable.getSelection())){
				ListGridRecord[] shpmrecords = view.shpmTable.getSelection();
				table.startEditingNew();
				row = table.getAllEditRows()[table.getAllEditRows().length - 1];
				view.itemRow = row;
				initverify(shpmrecords);
			} else {
				MSGUtil.sayWarning("请选择所要追踪的记录");
				return;
			}
		} else {
			MSGUtil.sayWarning("请选择所要追踪的记录");
			return;
		}
			
			
		

	}

}
