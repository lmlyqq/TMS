package com.rd.client.action.tms.dispatch;

import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.rd.client.view.tms.TmsShipmentView;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
public class ChangedQntyAction implements EditorExitHandler {

	private SGTable table;
	private SGForm view;
	public ChangedQntyAction(SGTable p_table, SGForm p_view) {
		this.table = p_table;
		this.view = p_view;
	}
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			ListGridRecord rec = new ListGridRecord();
			if(view instanceof DispatchView){
				DispatchView dv = (DispatchView)view;
				rec = dv.unshpmlstRec[row];
			}else if(view instanceof TmsShipmentView){
				TmsShipmentView dv = (TmsShipmentView)view;
				rec = dv.unshpmlstRec[row];
			}
			if(ObjUtil.isNotNull(event.getNewValue())) {
				double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
				double init_qnty = Double.parseDouble(rec.getAttribute("QNTY").toString());
				double rate = getRate(cur_qnty, init_qnty);
				if(cur_qnty <= 0) {
					SC.warn("无效拆分,数量不能小于或等于0!");
					table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
					table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
					table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
					table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
					return;
				}
				else if(rate > 1) {
					SC.warn("无效拆分,数量不能大于原单量!");
					table.setEditValue(row, "QNTY", rec.getAttribute("QNTY"));
					table.setEditValue(row, "VOL", rec.getAttribute("VOL"));
					table.setEditValue(row, "G_WGT", rec.getAttribute("G_WGT"));
					table.setEditValue(row, "N_WGT", rec.getAttribute("N_WGT"));
					return;
				}
				if(cur_qnty > 0 && rate > 0) {
				    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
					double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
					double n_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("N_WGT"),"0").toString());
	
					table.setEditValue(row, "VOL", rate*vol);
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
