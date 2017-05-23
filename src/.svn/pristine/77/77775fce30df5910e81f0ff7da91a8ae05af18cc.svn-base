package com.rd.client.action.tms.dispatch;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
public class ChangedTotalQntyAction implements EditorExitHandler {

	private SGTable table;
	private String field_name;
	private String field_title;
	public ChangedTotalQntyAction(SGTable p_table, String fieldName, String title) {
		this.table = p_table;
		this.field_name = fieldName;
		this.field_title = title;
	}
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			ListGridRecord rec = table.getSelectedRecord();
			if(ObjUtil.isNotNull(event.getNewValue())) {
				double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
				double init_qnty = Double.parseDouble(rec.getAttribute(field_name).toString());
				if(cur_qnty < 0) {
					SC.warn("无效操作," + field_title + "不能小于0!");
					table.setEditValue(row, field_name, init_qnty);
					return;
				}
			}
		}
	}
}
