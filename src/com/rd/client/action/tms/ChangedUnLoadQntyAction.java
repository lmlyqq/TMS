package com.rd.client.action.tms;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.view.tms.TmsTrackView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;

/**
 * 运输管理--运输执行--运输跟踪--货损货差签收下收货数量更改
 * @author Administrator
 *
 */
public class ChangedUnLoadQntyAction implements EditorExitHandler {

//	private SGTable table;
	private TmsTrackView view;
	public ChangedUnLoadQntyAction(TmsTrackView p_view) {
//		this.table = p_table;
		this.view = p_view;
	}
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			Record rec = event.getRecord();
			if(rec == null)return ;
			double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
			double init_qnty = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("LD_QNTY"),0).toString());
			//double rate = getRate(cur_qnty, init_qnty);
			if(cur_qnty < 0) {
				MSGUtil.sayError("收货数量不能小于0!");
				view.shpmlstTable.setEditValue(row, "UNLD_QNTY", init_qnty);
				return;
			}
			if(cur_qnty > 0) {
			    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("UNLD_VOL"),"0").toString());
				double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("UNLD_GWGT"),"0").toString());
				ListGridRecord record = view.shpmlstTable.getSelectedRecord();
				view.shpmlstTable.setEditValue(row, "UNLD_VOL", vol);
				view.shpmlstTable.setEditValue(row, "UNLD_GWGT", g_wgt);
				view.shpmlstTable.setEditValue(row, "UNLD_QNTY", cur_qnty);
				record.setAttribute("UNLD_QNTY", cur_qnty);
				record.setAttribute("UNLD_GWGT", g_wgt);
				record.setAttribute("UNLD_VOL", vol);
			}
			
		}
	}
}
