package com.rd.client.action.system;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 用户管理--组织机构--设置默认
 * @author fanglm
 *
 */
public class SaveUserDefOrgAction implements ClickHandler,com.smartgwt.client.widgets.events.ClickHandler{
	
	private TreeGrid treeGrid;
	private ListGrid fTable;
	private TextItem org_id;
	
	
	/**
	 * 构造
	 * @param table 订单类型列表
	 * @param form 订单类型form
	 * @param fTable 客户列表
	 */
	public SaveUserDefOrgAction(TreeGrid tree,ListGrid fTable,TextItem org_id) {
		this.treeGrid = tree;
		this.fTable = fTable;
		this.org_id = org_id;
	}
	
	public void onClick(ClickEvent event) {
		doSave();
	
	}

	@Override
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		doSave();
	}
	
	private void doSave(){
		String user_id = fTable.getSelectedRecord().getAttributeAsString("USER_ID");
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		
		if(!ObjUtil.isNotNull(user_id)){
			return;
		}
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("update sys_user_org set default_flag='N',edittime=sysdate,editwho='");
		sql.append(login_user);
		sql.append("' where default_flag='Y' and user_id='");
		sql.append(user_id);
		sql.append("'");
		sqlList.add(sql.toString());
		
		sql = new StringBuffer();
		sql.append("update sys_user_org set default_flag='Y',edittime=sysdate,editwho='");
		sql.append(login_user);
		sql.append("' where user_id='");
		sql.append(user_id);
		sql.append("' and org_id='");
		sql.append(org_id.getValue().toString());
		sql.append("'");
		sqlList.add(sql.toString());
		
		sql = new StringBuffer();
		sql.append("update sys_user set default_org_id='");
		sql.append(org_id.getValue().toString());
		sql.append("' where user_id='");
		sql.append(user_id);
		sql.append("'");
		sqlList.add(sql.toString());

		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result)){
					Tree tree = treeGrid.getTree();
					TreeNode[] node = tree.getAllNodes();
					for(int i = 0;i<node.length;i++){
						String org_str = node[i].getAttribute("ID");
						if(org_str.equals(org_id.getValue())){
							node[i].setAttribute("DEFAULT_FLAG", true);
						}else{
							node[i].setAttribute("DEFAULT_FLAG", false);
						}
					}
					treeGrid.refreshFields();
					
					MSGUtil.showOperSuccess();
				}else{
					MSGUtil.showOperError();
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
