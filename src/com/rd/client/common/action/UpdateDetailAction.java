package com.rd.client.common.action;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;

/**
 * 2012-09-12
 * 列表右键更新明细功能
 * @author yuanlei
 *
 */
public class UpdateDetailAction implements ClickHandler {
	
	private ListGrid table;
	
	public UpdateDetailAction(ListGrid table){
		this.table = table;
	}
	@Override
	public void onClick(MenuItemClickEvent event) {
		int[] edit_rows = table.getAllEditRows();
		if(edit_rows.length == 0){
			return;
		}
		Record rec = null;
		String shpm_no = "";
		HashMap<String, Object> listmap = new HashMap<String, Object>();
		HashMap<String, String> rows_map = new HashMap<String, String>();   //专供标识
		HashMap<String, String> lot2_map = new HashMap<String, String>();   //专供标识
		HashMap<String, String> lotid_map = new HashMap<String, String>();  //批号
		for(int i = 0; i < edit_rows.length; i++) {
			rec = table.getEditedRecord(edit_rows[i]);
			shpm_no = rec.getAttribute("SHPM_NO");
			rows_map.put(Integer.toString(i+1), rec.getAttribute("SHPM_ROW"));
			lot2_map.put(Integer.toString(i+1), rec.getAttribute("LOTATT02"));
			lotid_map.put(Integer.toString(i+1), rec.getAttribute("LOT_ID"));
		}
		listmap.put("1", shpm_no);
		listmap.put("2", rows_map);
		listmap.put("3", lot2_map);
		listmap.put("4", lotid_map);
		listmap.put("5", LoginCache.getLoginUser().getUSER_ID());
		String json = Util.mapToJson(listmap);
		Util.async.execProcedure(json, "SP_MODIFY_LOT(?,?,?,?,?,?)", new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
					MSGUtil.sayInfo(result.substring(2));
					
					table.discardAllEdits();
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
