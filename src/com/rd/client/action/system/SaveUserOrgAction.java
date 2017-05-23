package com.rd.client.action.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 * 用户管理--组织机构--调用存储过程执行保存功能
 * @author fanglm
 *
 */
public class SaveUserOrgAction implements ClickHandler,com.smartgwt.client.widgets.form.fields.events.ClickHandler{
	
	private TreeGrid treeGrid;
	private ListGrid table;
	private ValuesManager vm;
	private int righ = 0;
	private String _selection_ = null;
	/**
	 * 构造
	 * @param table 订单类型列表
	 * @param form 订单类型form
	 * @param fTable 客户列表
	 */
	public SaveUserOrgAction(TreeGrid tree,ListGrid fTable,ValuesManager vm) {
		this.treeGrid = tree;
		this.table = fTable;
		this.vm = vm;
	}
	public void onClick(ClickEvent event) {
		click();
	}
	
	public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
		click();
	}
	
	private void click() {
		if(table.getSelectedRecord() == null){
			MSGUtil.sayError("保存失败，请选择用户");
			return;
		}
		final ListGridRecord[] records = treeGrid.getSelection();
		String userId =table.getSelectedRecord().getAttribute("USER_ID");
		Util.db_async.getUserOrg(userId, new AsyncCallback<HashMap<String,String>>() {
			@Override
			public void onSuccess(HashMap<String, String> result) {
				String defaultOrgId = null;
				for (ListGridRecord record : records) {
					String org_id = record.getAttribute("ID");
					if(result.get(org_id) != null){
						if(result.get(org_id).equals("Y")){
							if(ObjUtil.ifObjNull(treeGrid.getEditValue(treeGrid.getRecordIndex(record), "DEFAULT_FLAG"),record.getAttribute("DEFAULT_FLAG")) == null)
								defaultOrgId = org_id;
						}
					}
				}
				save(defaultOrgId);
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
		
		
		

	}
	public void save(String defaultOrgId){
		String user_id = table.getSelectedRecord().getAttributeAsString("USER_ID");
		String login_user = LoginCache.getLoginUser().getUSER_ID();
//		RecordToTree();
//		tree = treeGrid.getTree();
//		allSelected(tree.getRoot());
		final ListGridRecord[] rec = treeGrid.getSelection();
		
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete from sys_user_org where user_id='");
		sql.append(user_id);
//		sql.append("' and default_flag <> 'Y'");
		sql.append("'");
		sqlList.add(sql.toString());
		int count=ObjUtil.isNotNull(defaultOrgId)?1:0;
		righ = 0;
		for(int i=0;i<rec.length;i++){
			Object dValue = ObjUtil.ifObjNull(treeGrid.getEditValue(treeGrid.getRecordIndex(rec[i]), "DEFAULT_FLAG"),rec[i].getAttribute("DEFAULT_FLAG"));
			if(ObjUtil.isNotNull(dValue) && "true".equals(dValue.toString())){
				count = count + 1;
				righ = i;
			}
		}
		
		if(count < 1){
			MSGUtil.sayWarning("请勾选默认机构！");
			return;
		}else if(count > 1){
			MSGUtil.sayWarning("只能勾选一个默认机构！");
			return;
		}
		
		for(int i=0;i<rec.length;i++){
			sql = new StringBuffer();
			if(isInvalid(rec[i])){
				continue;
			}
			sql.append("insert into sys_user_org(id,user_id,org_id,default_flag,parent_org_id,addtime,addwho) values(");
			sql.append("(select sys_guid() from dual),'");
			sql.append(user_id);
			sql.append("','");
			sql.append(rec[i].getAttribute("ID"));
			sql.append("','");
			if(defaultOrgId != null && defaultOrgId.equals(rec[i].getAttribute("ID"))){
				sql.append("Y");
			}else{
				Object dValue = ObjUtil.ifObjNull(treeGrid.getEditValue(treeGrid.getRecordIndex(rec[i]), "DEFAULT_FLAG"),rec[i].getAttribute("DEFAULT_FLAG"));
				sql.append(dValue==null||dValue.toString().equals("false") ? "N":"Y");
			}
			sql.append("','");
			sql.append(rec[i].getAttribute("PARENT_ORG_ID"));
			sql.append("',sysdate,'");
			sql.append(login_user);
			sql.append("')");
		
			sqlList.add(sql.toString());
		}
		
		_selection_ = null;
		
		sql = new StringBuffer();
		sql.append("update sys_user set default_org_id ='");
		sql.append(rec[righ].getAttribute("ID"));
		sql.append("' where user_id='");
		sql.append(user_id);
		sql.append("'");
		sqlList.add(sql.toString());
		
		
		Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(StaticRef.SUCCESS_CODE.equals(result)){
					MSGUtil.showOperSuccess();
					vm.getItem("DEFAULT_ORG_ID").setValue(rec[righ].getAttribute("ID"));
					vm.getItem("DEFAULT_ORG_ID_NAME").setValue(rec[righ].getAttribute("SHORT_NAME"));
					table.getSelectedRecord().setAttribute("DEFAULT_ORG_ID", rec[righ].getAttribute("ID"));
					table.getSelectedRecord().setAttribute("DEFAULT_ORG_ID_NAME", rec[righ].getAttribute("SHORT_NAME"));
					table.updateData(table.getSelectedRecord());
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
	
	/**
	 * 获取树结构选中的记录
	 * 如果当前子节点的所有叶未全选中，则只保留选中的叶，上级节点全部删除，如果子节点的叶全部选中，则保留所有的叶和节点
	 * @author yuanlei
	 * @param node
	 * @return
	 */
	
	/**
	private boolean allSelected(TreeNode node) {
		boolean isAllCheck = false;
		//System.out.println("current:"+ node.getAttribute("ORG_CNAME"));
		if(tree.isFolder(node)) {
			TreeNode[] nodes = tree.getFolders(node);
			if(nodes != null && nodes.length > 0) {
				for(int i = 0; i < nodes.length; i++) {
					isAllCheck = allSelected(nodes[i]);
					if(!isAllCheck) {
						//System.out.println("remove:"+ node.getAttribute("ORG_CNAME"));
						TreeNode[] tmp_node = new TreeNode[1];
						tmp_node[0] = node;
						removeNode(tmp_node);
					}
				}
			}
			else {
				//叶级
				if(!containAll(tree.getLeaves(node))) {
					isAllCheck = false;
					TreeNode[] tmp_node = new TreeNode[1];
					tmp_node[0] = node;
					removeNode(tmp_node);
				}
				else {
					isAllCheck = true;
				}
			}
		}
		return isAllCheck;
	}
	
	private void RecordToTree() {
		treeList = new ArrayList<ListGridRecord>();
		idList = new ArrayList<String>();
		ListGridRecord[] rec = treeGrid.getSelection();
		for(int i = 0; i < rec.length; i++) {
			treeList.add(rec[i]);
			idList.add(rec[i].getAttribute("ID"));
		}
	}
	
	private boolean containAll(TreeNode[] nodes) {
		boolean isContain = true;
		for(int i = 0; i < nodes.length; i++) {
			if(!idList.contains(nodes[i].getAttribute("ID"))) {
				isContain = false;
				break;
			}
		}
		return isContain;
	}
	
	private void removeNode(TreeNode[] nodes) {
		if(nodes != null && nodes.length > 0) {
			for(int i = 0; i < nodes.length; i++) {
				int pos = idList.indexOf(nodes[i].getAttribute("ID"));
				if(pos >= 0) {
					idList.remove(pos);
					treeList.remove(pos);
				}
			}
		}
	}**/
	
	private boolean isInvalid(ListGridRecord record){
		if(!record.getAttributeAsBoolean("isFolder")){
			return false;
		}
		if(_selection_ == null){
			String[] attrs = record.getAttributes();
			for (int i = attrs.length-1; i > 0; i--) {
				if(attrs[i].indexOf("_selection_") > 0){
					_selection_ = attrs[i];
					break;
				}
			}
			if(_selection_ == null) return false;
		}
		return record.getAttributeAsBoolean(_selection_);
	}
}
