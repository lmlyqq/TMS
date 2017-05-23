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
import com.rd.client.win.ListCfgWin;

import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 系统管理->列表配置->保存
 * @author yuanlei
 *
 */
public class ListConfigSaveAction implements ClickHandler {

	private SGTable winTable;
	private SGTable listTable;
	private ArrayList<String> extraList;
	private ArrayList<String> delList;
	private ArrayList<String> sqlList;
	private ListCfgWin win;
	private int seq;  //seq=1:表示执行保存按钮的功能;seq=2：表示执行复制生成的功能
	private String func_model;
	private String view_name;
	public ListConfigSaveAction(SGTable p_winTable, SGTable p_listTable, ListCfgWin p_win, int p_seq) {
		winTable = p_winTable;
		listTable = p_listTable;
		win = p_win;
		seq = p_seq;
	}

	@Override
	public void onClick(ClickEvent event) {
		try {
			//执行新增保存或复制生成
			ListGridRecord[] recList = winTable.getSelection();
			if(recList == null || recList.length <= 0) {
				SC.warn("未选择功能模块!");
				return;
			}
			if(seq == 1) {
				func_model = recList[0].getAttribute("ID");  //保存时，取第一个功能模块ID，作为批量插入SQL的条件
				view_name = win.new_view_name;
			}
			else {
				func_model = JSOHelper.getAttribute(listTable.getJsObj(), "FUNC_MODEL");  //复制生成时，取查询条件选择的值
				view_name = JSOHelper.getAttribute(listTable.getJsObj(), "VIEW_NAME"); 
			}
			String user_id = LoginCache.getLoginUser().getUSER_ID();

			String models = "";
			extraList = new ArrayList<String>();
			delList = new ArrayList<String>();
			//生成批量插入的语句，避免SQL过多抛出异常
			for(int i = 0; i < recList.length; i++) {
				
				if(seq == 1) {
					if(i > 0) {
						StringBuffer sf = new StringBuffer();
						sf.append("insert into SYS_LIST_FUNC(ID,COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,USER_ID,VIEW_NAME,ADDTIME,FUNC_MODEL,ADDWHO)");
						sf.append(" select sys_guid(),COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,USER_ID,VIEW_NAME,sysdate,'");
						sf.append(recList[i].getAttribute("ID"));
						sf.append("','");
						sf.append(user_id);
						sf.append("' from  SYS_LIST_FUNC WHERE USER_ID = '");
						sf.append(user_id);
						sf.append("' and FUNC_MODEL = '");
						sf.append(func_model);
						sf.append("' and VIEW_NAME = '");
						sf.append(view_name);
						sf.append("'");
						extraList.add(sf.toString());
					}
					
					models += ",'" + recList[i].getAttribute("ID") + "'"; //保存时，取所有功能模块的ID作为校验条件
					
					StringBuffer sf = new StringBuffer();
					sf.append("delete from SYS_LIST_FUNC WHERE USER_ID = '");
					sf.append(user_id);
					sf.append("' and FUNC_MODEL = '");
					sf.append(recList[i].getAttribute("ID"));
					sf.append("' and VIEW_NAME = '");
					sf.append(view_name);
					sf.append("'");
					delList.add(sf.toString());
				}
				else {
					if(!recList[i].getAttribute("ID").equals(func_model)) {
						StringBuffer sf = new StringBuffer();
						sf.append("insert into SYS_LIST_FUNC(ID,COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,USER_ID,VIEW_NAME,ADDTIME,FUNC_MODEL,ADDWHO)");
						sf.append(" select sys_guid(),COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,USER_ID,VIEW_NAME,sysdate,'");
						sf.append(recList[i].getAttribute("ID"));
						sf.append("','");
						sf.append(user_id);
						sf.append("' from  SYS_LIST_FUNC WHERE USER_ID = '");
						sf.append(user_id);
						sf.append("' and FUNC_MODEL = '");
						sf.append(func_model);
						sf.append("' and VIEW_NAME = '");
						sf.append(view_name);
						sf.append("'");
						extraList.add(sf.toString());
						
						models += ",'" + recList[i].getAttribute("ID") + "'"; //保存时，取所有选中且不包含当前模块的ID作为校验条件
						
						sf = new StringBuffer();
						sf.append("delete from SYS_LIST_FUNC WHERE USER_ID = '");
						sf.append(user_id);
						sf.append("' and FUNC_MODEL = '");
						sf.append(recList[i].getAttribute("ID"));
						sf.append("' and VIEW_NAME = '");
						sf.append(view_name);
						sf.append("'");
						delList.add(sf.toString());
					}
				}
			}
			//判断已勾选的模块是否已配置过
			Util.db_async.getRecord("distinct FUNC_MODEL_NAME", "V_LIST_FUNC", " where FUNC_MODEL IN (" + models.substring(1) + ")", null, new AsyncCallback<ArrayList<HashMap<String, String>>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					if(result != null && result.size() > 0) {
						String model_name = "";
						StringBuffer sf = new StringBuffer();
						for(int i = 0; i < result.size(); i++) {
							sf.append("|");
							sf.append(result.get(i));
							//model_name += "|" + result.get(i).get("FUNC_MODEL_NAME");
						}
						model_name = sf.toString().substring(1);
						SC.confirm("【" + model_name + "】已配置,是否覆盖原有?", new BooleanCallback() {
							public void execute(Boolean value) {
			                    if (value != null && value) {
			                    	if(seq == 1) {
			                    		doSave(func_model,"M");
			                    	}
			                    	else {
			                    		doUpdate();
			                    	}
			                    }
			                }
			            });
					}
					else {
						if(seq == 1) {
							doSave(func_model,"A");
						}
						else {
							doUpdate();
						}
					}
				}
				
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void doSave(String func_model, String flag) {
		sqlList = new ArrayList<String>();
		ListGridRecord[] records = listTable.getRecords();
		String user_id = LoginCache.getLoginUser().getUSER_ID();
		
		String json = ""; 
		Map edit_map = null;
		for(int i = 0; i < records.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			edit_map = listTable.getEditValues(records[i]);
			map.put("COLUMN_ID", ObjUtil.ifObjNull(records[i].getAttribute("ID"), "").toString());
			map.put("SHOW_SEQ", ObjUtil.ifObjNull(records[i].getAttribute("SHOW_SEQ"), "").toString());
			map.put("SHOW_FLAG", Util.boolToStr(ObjUtil.ifObjNull(edit_map.get("SHOW_FLAG"),records[i].getAttribute("SHOW_FLAG")).toString()));
			map.put("FIELD_WIDTH", ObjUtil.ifObjNull(edit_map.get("FIELD_WIDTH"), records[i].getAttribute("FIELD_WIDTH")).toString());
			map.put("FUNC_MODEL", ObjUtil.ifObjNull(func_model, "").toString());
			map.put("USER_ID", ObjUtil.ifObjNull(user_id, "").toString());
			map.put("VIEW_NAME", view_name);
			if(map != null) {
				map.put("TABLE", "SYS_LIST_FUNC");
				json = Util.mapToJson(map);
				sqlList.add(json);
			}
		}
		if(flag.equals("A")) {
			doInsert(sqlList, extraList);
		}
		else {
			Util.async.excuteSQLList(delList, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					doInsert(sqlList, extraList);
				}
				
			});
		}
	}
	
	private void doUpdate() {
		for(int i = 0; i < extraList.size(); i++) {
			delList.add(extraList.get(i));
		}
		
		Util.async.excuteSQLList(delList, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					listTable.OP_FLAG = "M";
					win.window.destroy();
					win.view.initAddBtn(true);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
			
		});
	}
	
	private void doInsert(ArrayList<String> sqlList, ArrayList<String> extraList) {
		Util.async.doInsert(null, sqlList, extraList, true, new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					listTable.OP_FLAG = "M";
					if(ObjUtil.isNotNull(JSOHelper.getAttribute(listTable.getJsObj(), "VIEW_NAME")) 
							|| ObjUtil.isNotNull(JSOHelper.getAttribute(listTable.getJsObj(), "FUNC_MODEL"))) {
						listTable.setProperty("VIEW_NAME", view_name);
						listTable.setProperty("FUNC_MODEL", func_model);
					}
					win.window.destroy();
					win.view.initAddBtn(true);
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
}
