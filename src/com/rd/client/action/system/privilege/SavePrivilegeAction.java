package com.rd.client.action.system.privilege;

import java.util.ArrayList;

import com.rd.client.view.system.PrivilegeView;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * 权限保存事件
 * @author yuanlei
 *
 */
public class SavePrivilegeAction implements ClickHandler{

	private PrivilegeView view;
//	private Tree tree;
//	private List<TreeNode> remain;              //剩余未处理的记录数 
//    private List<TreeNode> commit;              //最后提交的数据
//    private SGCombo ROLE_ID;
    //private TextItem ROLE_ID;
    private ArrayList<String> list;
	
    
//	public SavePrivilegeAction(PrivilegeView p_treeGrid,SGCombo ROLE_ID) {
//		view = p_treeGrid;
//		this.ROLE_ID = ROLE_ID;
//	}
	
	public SavePrivilegeAction(PrivilegeView p_treeGrid,TextItem ROLE_ID) {
		view = p_treeGrid;
		//this.ROLE_ID = ROLE_ID;
	}
	@Override
	public void onClick(ClickEvent event) {
		if(event != null) {
			//String login_user = LoginCache.getLoginUser().getUSER_ID();
//			tree = view.tree.getTree();
			ListGridRecord[] records = view.tree.getSelection();
			/**
			convertRecordToNode(records);   //将ListGridRecord转换成TreeNode,放入到remain当中
			commit = new ArrayList<TreeNode>();
			if(records != null && records.length > 0) {				
				TreeNode node = Tree.nodeForRecord(records[0]);
				allSelected(node);
				//commit.add(node);
			}
			list = new ArrayList<String>();
			for(int i=0;i<commit.size();i++){
				list.add(commit.get(i).getAttributeAsString("FUNCTION_ID"));
			}**/
			list  = new ArrayList<String>();
			for(int i=0 ;i<records.length;i++){
				list.add(records[i].getAttributeAsString("FUNCTION_ID"));
			}
			
//			Util.async.savePrivilege(list,ROLE_ID.getValue().toString(),login_user,view.func_id, new AsyncCallback<String>() {
//				
//				@Override
//				public void onSuccess(String result) {
//					if(result.equals(StaticRef.SUCCESS_CODE))
//						MSGUtil.showOperSuccess();
//					else 
//						MSGUtil.showOperError();
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
		}
	}
	
	/**
	 * 递归判断当前节点的下级节点是否全部勾选
	 * @author yuanlei
	 * @param node
	 * @return
	 */
	/**
	private boolean allSelected(TreeNode node) {
		boolean isSelected = true;                     //当前判断节点是否全部选中
		String type = node.getAttribute("MENU_TYPE");  //F-菜单;P-页签
		if(node.getAttributeAsBoolean("isFolder")) {   //FOLDER（菜单或页签）
			//System.out.println(node.getAttribute("FUNCTION_ID") + "|" + node.getAttribute("PARENT_FUNCTION_ID"));
			TreeNode[] child_nodes = tree.getChildren(node);    //获取当前节点的下一级节点
			if(child_nodes != null && child_nodes.length > 0) {
				boolean isAllSelected = true;          //当前功能的下一级（页签）是否全部选中，如果全部选中，只需要录入当前功能的信息，不需要录入页签信息
				boolean isExistSelected = false;        //当前功能的下一级（页签）是否存在选中记录
				for(int i = 0; i < child_nodes.length; i++) {
					isSelected = allSelected(child_nodes[i]);
					if(!isSelected) {
						isAllSelected = false;
						//printTreeNode(commit);
						if(!child_nodes[i].getAttribute("MENU_TYPE").equals("F")) {
							commit.remove(child_nodes[i]);
						}
					}
					else {
						isExistSelected = true;
					}
				}
				if(isAllSelected && !child_nodes[0].getAttribute("MENU_TYPE").equals("F") && ObjUtil.isNotNull(child_nodes[0].getAttribute("FUNCTION_FORMNAME"))) {
					//将当前功能的信息加入 到commit中，移除下一级（页签）信息
					//printTreeNode(commit);
					
					commit.add(node);
					removeChild(node.getAttribute("FUNCTION_ID"));
				}
				else {
					if(isExistSelected && type.equals("F")) {
						commit.add(node);
					}
				}
			}
		}
		else {
			
			//无下级节点（页签或功能）
			isSelected = false;
			for(int i = 0; i < remain.size(); i++) {
				if(ObjUtil.isNotNull(remain.get(i)) && remain.get(i).getAttribute("FUNCTION_ID").equals(node.getAttribute("FUNCTION_ID"))) {
					commit.add(node);
					isSelected = true;
					break;
				}
			}
		}
 		return isSelected;
	}
	**/
	/**
	 * 获取指定菜单ID的所有下级菜单
	 * @author yuanlei
	 * @param func_id
	 * @return
	 */
	/*private List<TreeNode> getChildrenSelection(String func_id) {
		TreeNode cur = null;
		List<TreeNode> remain_list = new ArrayList<TreeNode>();
		List<TreeNode> retList = new ArrayList<TreeNode>();
		ListGridRecord rec = null;
		for(int i = 0; i < remain.size(); i++) {
			rec = remain.get(i);
			if(ObjUtil.ifNull(rec.getAttributeAsString("PARENT_FUNCTION_ID"),"").compareTo(func_id) == 0) {
				retList.add(Tree.nodeForRecord(rec));
			}
			else {
				if(rec.getAttributeAsString("FUNCTION_ID").compareTo(func_id) == 0) {
					cur = Tree.nodeForRecord(rec);
				}
				else {
					remain_list.add(Tree.nodeForRecord(rec));
				}
			}
		}
		remain_list.add(0, cur);  //将当前节点的记录放在第一个位置方便移除
		remain = remain_list;
		return retList;
	}*/
	
	/**
	 * 移除指定菜单ID的所有下级菜单
	 * @author yuanlei
	 * @param func_id
	 * @return
	 */
	
	/***
	private void removeChild(String func_id) {
		ListGridRecord rec = null;
		int i = 0;
		while(i < commit.size()) {
			rec = commit.get(i);
			if(ObjUtil.ifNull(rec.getAttributeAsString("PARENT_FUNCTION_ID"),"").compareTo(func_id) == 0) {
				commit.remove(i);
				i = -1;
			}
			i++;
		}
	}
	
	private void convertRecordToNode(ListGridRecord[] records) {
		remain = new ArrayList<TreeNode>();
		if(records != null && records.length > 0) {
			for(int i = 0; i < records.length; i++) {
				remain.add(Tree.nodeForRecord(records[i]));
			}
		}
	}
	
	/*private void printTreeNode(List<TreeNode> node) {
		for(int i = 0; i < node.size(); i++) {
			System.out.println(node.get(i).getAttribute("FUNCTION_ID") + "," + node.get(i).getAttribute("FUNCTION_NAME"));
		}
	}*/
}
