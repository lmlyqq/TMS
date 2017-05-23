package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.view.system.ListConfigView;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 系统管理->列表配置->保存
 * @author yuanlei
 *
 */
public class ListConfigModifyAction implements ClickHandler {

	private SGTable listTable;
	private ArrayList<String> extraList;
	private ArrayList<String> sqlList;
	private String func_model;
	private String view_name;
	private ListConfigView view;
	public ListConfigModifyAction(SGTable p_listTable, ListConfigView p_view) {
		listTable = p_listTable;
		view = p_view;
	}

	@Override
	public void onClick(ClickEvent event) {
		try {
			//执行修改保存	
			if(listTable.getRecords().length > 0) {
				func_model = JSOHelper.getAttribute(listTable.getJsObj(), "FUNC_MODEL");  //复制生成时，取查询条件选择的值
				view_name = JSOHelper.getAttribute(listTable.getJsObj(), "VIEW_NAME");  //复制生成时，取查询条件选择的值
				extraList = new ArrayList<String>();
				String user_id = LoginCache.getLoginUser().getUSER_ID();
				StringBuffer sf = new StringBuffer();
				sf.append("delete from SYS_LIST_FUNC WHERE USER_ID = '");
				sf.append(user_id);
				sf.append("' and FUNC_MODEL = '");
				sf.append(func_model);
				sf.append("'");
				extraList.add(sf.toString());
				doSave(func_model);
			}			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doSave(String func_model) {
		sqlList = new ArrayList<String>();
		ListGridRecord[] records = listTable.getRecords();
		String user_id = LoginCache.getLoginUser().getUSER_ID();
		
		String json = "";
		Map edit_map = null;
		for(int i = 0; i < records.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			edit_map = listTable.getEditValues(records[i]);
			map.put("VIEW_NAME", view_name);
			map.put("COLUMN_ID", ObjUtil.ifObjNull(records[i].getAttribute("COLUMN_ID"), records[i].getAttribute("ID")).toString());
			map.put("SHOW_SEQ", Integer.toString(i+1));
			map.put("SHOW_FLAG", Util.boolToStr(ObjUtil.ifObjNull(edit_map.get("SHOW_FLAG"),records[i].getAttribute("SHOW_FLAG")).toString()));
			map.put("FIELD_WIDTH", ObjUtil.ifObjNull(edit_map.get("FIELD_WIDTH"), records[i].getAttribute("FIELD_WIDTH")).toString());
			map.put("FUNC_MODEL", ObjUtil.ifObjNull(func_model, "").toString());
			map.put("USER_ID", ObjUtil.ifObjNull(user_id, "").toString());
			if(map != null) {
				map.put("TABLE", "SYS_LIST_FUNC");
				json = Util.mapToJson(map);
				sqlList.add(json);
			}
		}
		doInsert(sqlList, extraList);
	}
	
	private void doInsert(ArrayList<String> sqlList, ArrayList<String> extraList) {
		Util.async.doInsert(null, sqlList, extraList, false, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					listTable.OP_FLAG = "M";
					view.initAddBtn(true);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
}
