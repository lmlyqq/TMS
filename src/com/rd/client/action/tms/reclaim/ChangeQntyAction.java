package com.rd.client.action.tms.reclaim;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.TmsOdrReceiptView;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
/**
 * 
 * @author wangjun
 *
 */
public class ChangeQntyAction implements EditorExitHandler{
	private SGTable table;
	private TmsOdrReceiptView view;
	public ChangeQntyAction(SGTable p_table, TmsOdrReceiptView p_view) {
		this.table = p_table;
		this.view = p_view;
	}
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			ListGridRecord rec = view.unorderTablelstRec[row];
			double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),rec.getAttribute("UNLD_QNTY")).toString());
			double init_qnty = Double.parseDouble(rec.getAttribute("LD_QNTY").toString());//发货数量
			double rate = getRate(cur_qnty, init_qnty);
			if(cur_qnty < 0) {
				MSGUtil.sayError("数量不能小于0!");
				return;
			}
			if(cur_qnty > init_qnty){
				MSGUtil.sayError("收货数量不能大于发货数量!");
				return;
			}
			if(cur_qnty > 0 && rate > 0) {
			    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("LD_VOL"),"0").toString());
				double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("LD_GWGT"),"0").toString());

				table.setEditValue(row, "UNLD_VOL", rate*vol);
				table.setEditValue(row, "UNLD_GWGT", rate*g_wgt);
			}
			
			table.setEditValue(row, "DAMA_QNTY", init_qnty - cur_qnty);
			table.setEditValue(row, "DAMA_TRANS_UOM", rec.getAttribute("UOM"));
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
