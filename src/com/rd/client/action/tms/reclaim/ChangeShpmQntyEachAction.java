package com.rd.client.action.tms.reclaim;

import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
/**
 * 作业单回单-->明细计算
 * @author lijun
 *
 */
public class ChangeShpmQntyEachAction implements EditorExitHandler {
	private SGTable table;
	
	public ChangeShpmQntyEachAction(SGTable table){
		this.table = table;
	}
	
	@Override
	public void onEditorExit(EditorExitEvent event) {
		if(event != null) {
			int row = event.getRowNum();
			ListGridRecord rec = table.getRecord(row);
			double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),0).toString());
			double init_qnty = Double.parseDouble(rec.getAttribute("LD_QNTY").toString());//发货数量
			double init_qnty_each = Double.parseDouble(rec.getAttribute("QNTY_EACH").toString());//发货数量
			double rate = getRate(cur_qnty, init_qnty_each);
			if(cur_qnty < 0) {
				MSGUtil.sayError("最小单位收货数量不能小于0!");
				return;
			}
			if(cur_qnty > init_qnty*init_qnty_each){
				MSGUtil.sayError("收货数量不能大于发货数量!");
				return;
			}
			if(cur_qnty > 0 && rate > 0) {
			    double vol = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("VOL"),"0").toString());
				double g_wgt = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("G_WGT"),"0").toString());
//				double QNTY = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("QNTY"), "0").toString());
//				double QNTY_EACH = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("QNTY_EACH"), "0").toString());
				double LD_QNTY = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("LD_QNTY"),"0").toString());
				table.setEditValue(row, "VOL", rate*vol);
				table.setEditValue(row, "G_WGT", rate*g_wgt);
				table.setEditValue(row, "LOSDAM_QNTY", LD_QNTY-init_qnty*rate);
				table.setEditValue(row, "UNLD_QNTY", init_qnty*rate);
//				table.setEditValue(row,"UNLD_WORTH",rate*UNLD_WORTH);
			}
//			table.setEditValue(row, "DAMA_QNTY", init_qnty - cur_qnty);
//			table.setEditValue(row, "DAMA_TRANS_UOM", rec.getAttribute("UOM"));

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
