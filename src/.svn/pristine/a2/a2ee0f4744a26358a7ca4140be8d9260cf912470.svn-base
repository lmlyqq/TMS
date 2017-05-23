package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.win.UserWin;
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
public class AssignToUserAction implements ClickHandler {

	private SGTable winTable;
	private SGTable listTable;
	private ArrayList<String> extraList;
	private ArrayList<String> sqlList;
	private UserWin win;
	public AssignToUserAction(SGTable p_winTable, SGTable p_listTable, UserWin p_win) {
		winTable = p_winTable;
		listTable = p_listTable;
		win = p_win;
	}

	@Override
	public void onClick(ClickEvent event) {
		try {
			//执行新增保存或复制生成
			ListGridRecord[] recList = winTable.getSelection();
			if(recList == null || recList.length <= 0) {
				SC.warn("未选择用户!");
				return;
			}
			String func_model = JSOHelper.getAttribute(listTable.getJsObj(), "FUNC_MODEL");  //复制生成时，取查询条件选择的值
			String view_name = JSOHelper.getAttribute(listTable.getJsObj(), "VIEW_NAME");  //复制生成时，取查询条件选择的值
			if(!ObjUtil.isNotNull(func_model)) {
				SC.warn("未指定功能模块!");
			}
			String user_id = JSOHelper.getAttribute(listTable.getJsObj(), "USER_ID");  //复制生成时，取查询条件选择的值

			String users = "";
			extraList = new ArrayList<String>();
			for(int i = 0; i < recList.length; i++) {	
				if(!recList[i].getAttribute("USER_ID").equals(user_id)) {
					
					users += ",'" + recList[i].getAttribute("USER_ID") + "'"; //保存时，取所有选中且不包含当前模块的ID作为校验条件
					
					StringBuffer sf = new StringBuffer();
					sf.append("delete from SYS_LIST_FUNC WHERE USER_ID = '");
					sf.append(recList[i].getAttribute("USER_ID"));
					sf.append("' and FUNC_MODEL = '");
					sf.append(func_model);
					sf.append("' and VIEW_NAME = '");
					sf.append(view_name);
					sf.append("'");
					extraList.add(sf.toString());
					
					sf = new StringBuffer();
					sf.append("insert into SYS_LIST_FUNC(ID,COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,FUNC_MODEL,VIEW_NAME,ADDTIME,USER_ID,ADDWHO)");
					sf.append(" select sys_guid(),COLUMN_ID,FIELD_WIDTH,SHOW_SEQ,SHOW_FLAG,FUNC_MODEL,VIEW_NAME,sysdate,'");
					sf.append(recList[i].getAttribute("USER_ID"));
					sf.append("','");
					sf.append(LoginCache.getLoginUser().getUSER_ID());
					sf.append("' from  SYS_LIST_FUNC WHERE USER_ID = '");
					sf.append(user_id);
					sf.append("' and FUNC_MODEL = '");
					sf.append(func_model);
					sf.append("' and VIEW_NAME = '");
					sf.append(view_name);
					sf.append("'");
					extraList.add(sf.toString());
				}
				else {
					SC.warn("不能指定自己!");
					return;
				}
			}
			//判断已勾选的模块是否已配置过
			Util.db_async.getRecord("distinct USER_ID", "V_LIST_FUNC", " where USER_ID IN (" + users.substring(1) + ") and FUNC_MODEL = '" + func_model + "'" , null, new AsyncCallback<ArrayList<HashMap<String, String>>>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					if(result != null && result.size() > 0) {
						String user_id = "";
						StringBuffer sf = new StringBuffer();
						for(int i = 0; i < result.size(); i++) {
							sf.append("|");
							sf.append(result.get(i).get("USER_ID"));
							//user_id += "|" + result.get(i).get("USER_ID");
						}
						user_id = sf.toString().substring(1);
						SC.confirm("【" + user_id + "】已配置该功能模块,是否覆盖原有?", new BooleanCallback() {
							public void execute(Boolean value) {
			                    if (value != null && value) {
			                    	doCopy();
			                    }
			                }
			            });
					}
					else {
						doCopy();
					}
				}
				
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doCopy() {
		sqlList = new ArrayList<String>();
		doInsert(sqlList, extraList);
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
					win.window.destroy();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
}
