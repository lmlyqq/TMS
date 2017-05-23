package com.rd.client.action.base.org;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 调用存储过程DEL_PRO执行删除操作
 * @author fanglm
 *
 */
public class DeleteOrgAction implements ClickHandler,com.smartgwt.client.widgets.menu.events.ClickHandler {

	private TreeGrid table;
	private ValuesManager vm;
	private DataSource ds;
	private ListGridRecord[] records = null;
	public DeleteOrgAction(TreeGrid p_table,ValuesManager vm) {
		table = p_table;
		this.vm = vm;
	}
	@Override
	public void onClick(ClickEvent event) {
		ds= table.getDataSource();
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}else{
			MSGUtil.sayError("请先勾选待删除记录！");
			return;
		}
		
	}
	
	private void doDelete() {
		//String tableName = ds.getAttribute("tableName");
		String tableName = ds.getTableName();
		String id = table.getSelectedRecord().getAttributeAsString("ID");
		if(id == null)
			return;
		
    	Util.db_async.deletePro(id, tableName,LoginCache.getLoginUser().getUSER_ID(), new AsyncCallback<String>() {
			
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.sayError(caught.getMessage());
			}
	
			@Override
			public void onSuccess(String result) {
				if(result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.showOperSuccess();
					if(vm != null){
						vm.clearValues();
					}
					RefreshRecord();
					table.redraw();
				}
				else {
					MSGUtil.sayError(result);
				}
			}
		});
	}
	
	private void RefreshRecord() {
		Tree tree = table.getTree();
		TreeNode[] list = tree.getAllNodes();
		TreeNode node = null;
		for(int i = 0; i < list.length; i++) {
			node = list[i];
            if(node.getAttributeAsString("ID").equals(table.getSelectedRecord().getAttributeAsString("ID")))
            	tree.remove(node);
		}
		if(tree.getAllNodes().length > 0){
			table.selectRecord(0);
		}
	}

	@Override
	public void onClick(MenuItemClickEvent event) {
		ds= table.getDataSource();
		records = table.getSelection();
		if(records  != null && records.length > 0) {
			SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
				public void execute(Boolean value) {
                    if (value != null && value) {
                    	doDelete();
                    }
                }
            });
		}
	}
}
