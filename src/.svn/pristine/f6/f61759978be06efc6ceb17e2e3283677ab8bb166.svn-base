package com.rd.client.action.tms.dispatch;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.tms.DispatchView;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 按发货量拆分
 * @author yuanlei
 *
 */
public class SplitByLoadQntyAction implements ClickHandler {

	private SGTable table = null;
	private SGTable loadTable;
	private DispatchView view;
	public SplitByLoadQntyAction(SGTable p_table, SGTable p_loadTable, DispatchView p_view) {
		table = p_table;
		loadTable = p_loadTable;
		view = p_view;
	}
	public SplitByLoadQntyAction(SGTable p_table) {
		table = p_table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		try {			
			final ListGridRecord[] rec = table.getSelection();
			Record roo = null;
			if(rec != null) {
//				if(!rec.getAttribute("STATUS_NAME").equals(StaticRef.SHPM_DIPATCH_NAME)) {
//					SC.warn("作业单状态为" + rec.getAttribute("STATUS_NAME") + ",无法拆分!\r\n");
//					return;
//				}
				roo = table.getEditedRecord(table.getRecordIndex(table.getSelectedRecord()));
				HashMap<String, Object> listmap = new HashMap<String, Object>();
				
				//HashMap<String, String> id_map = new HashMap<String, String>();   //id
				//HashMap<String, String> qnty_map = new HashMap<String, String>();  //数量
				
				for(int i =0;i<rec.length;i++){
					
				}
//				listmap.put("1", rec.getAttribute("SHPM_NO"));
				listmap.put("2", roo.getAttribute("QNTY"));
				listmap.put("3", LoginCache.getLoginUser().getUSER_ID());
				String json = Util.mapToJson(listmap);
				
				Util.async.execProcedure(json, "SP_SHPM_SPLITBY_LDQNTY(?,?,?,?)", new AsyncCallback<String>() {
					@Override
					public void onSuccess(String result) {
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//String[] ret = result.substring(2).split(",");
//							rec.setAttribute("STATUS_NAME", StaticRef.SHPM_LOAD_NAME);
//							rec.setAttribute("ID", ret[1]);
//							rec.setAttribute("TOT_QNTY_EACH", roo.getAttribute("TOT_QNTY_EACH"));
//							rec.setAttribute("SHPM_NO", rec.getAttribute("SHPM_NO") + "_S1");
//							table.updateData(rec);
							table.redraw();
							
							//刷新待调订单列表
							new QueryUnshpmTableAction(view.unshpmTable, view.sumForm, view.pageForm);
							
							ListGridRecord rec = loadTable.getSelectedRecord();
							loadTable.updateData(rec);
							loadTable.redraw();
						}
						else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}
				});
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
