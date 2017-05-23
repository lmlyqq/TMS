package com.rd.client.action.tms.dispatch;

import com.rd.client.common.util.ObjUtil;
/**
 * 调度配载待调作业单明细体积事件
 */
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
public class ChangedVolAction implements EditorExitHandler {

	private SGTable table;
	private DispatchView view;
	public ChangedVolAction(SGTable p_table, DispatchView p_view) {
		this.table = p_table;
		this.view = p_view;
	}
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			ListGridRecord rec = view.unshpmlstRec[row];
			if(ObjUtil.isNotNull(event.getNewValue())) {
				double cur_vol = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
				double init_vol = Double.parseDouble(rec.getAttribute("VOL").toString());
				double rate = getRate(cur_vol, init_vol);
				if(cur_vol <= 0) {
					SC.warn("无效操作,体积不能小于0!");
					table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
					table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
					table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
					table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
					return;
				}
				else if(rate > 1) {
					SC.warn("无效操作,体积不能大于原体积!");
					table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
					table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
					table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
					table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
					return;
				}
				if(cur_vol > 0 && rate > 0) {
					double qnty = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("QNTY"),"0").toString());
				    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
					double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
					double n_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("N_WGT"),"0").toString());
					
					double new_qnty = Math.ceil(rate*qnty);
					rate = getRate(new_qnty,qnty);
	
					table.setEditValue(row, "QNTY", new_qnty); 
					table.setEditValue(row, "VOL", rate*vol);  //四舍五入
					table.setEditValue(row, "G_WGT", rate*g_wgt);
					table.setEditValue(row, "N_WGT", rate*n_wgt);
				}
			}
		}
	}
	
    private double getRate(double douPart, double douTotal) {
	  
	    double rate = 0.0000;
	    if(douTotal > 0) {
	    	rate = douPart/douTotal;
	    }
	    return rate;
    }
}
