/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package com.rd.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.common.obj.FUNCTION;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.obj.system.SYS_USER;
import com.smartgwt.client.types.SortArrow;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class SideNavTree extends TreeGrid {
	
	private String idSuffix = "";
	private ExplorerTreeNode[] showcaseData ;

    public SideNavTree() {
        setWidth100();
        setHeight100();
        setCustomIconProperty("icon");
        setAnimateFolderTime(100);
        setAnimateFolders(true);
        setAnimateFolderSpeed(1000);
        setNodeIcon("silk/application_view_list.png");
        setShowSortArrow(SortArrow.CORNER);
        setShowAllRecords(true);
        setLoadDataOnDemand(false);
        setCanSort(false);
        
        TreeGridField field = new TreeGridField();
        field.setCanFilter(true);
        field.setName("nodeTitle");
        field.setTitle("<b>主菜单</b>");
        setFields(field);
        
        getTreeData();
    }
    
    public void getTreeData(){
    	SYS_USER user = LoginCache.getLoginUser();
    	Util.async.getFuncList(user.getROLE_ID(),user.getLOGIN_SYSTEM(),new AsyncCallback<List<FUNCTION>>() {
			
			@Override
			public void onSuccess(List<FUNCTION> result) {
				int i = 0;
				ExplorerTreeNode[] treeData = new ExplorerTreeNode[result.size()];
				for(FUNCTION power : result){
					treeData[i] = new ExplorerTreeNode(power.getFUNCTION_NAME(), power.getFUNCTION_ID(), power.getPARENT_FUNCTION_ID(),StaticRef.ICON_NODE, power.getUserPanel(), true, idSuffix);
					i++;
				}
				Tree tree = new Tree();
		        tree.setModelType(TreeModelType.PARENT);
		        tree.setNameProperty("nodeTitle");
		        tree.setOpenProperty("isOpen");
		        tree.setIdField("nodeID");
		        tree.setParentIdField("parentNodeID");
		        tree.setRootValue("root" + idSuffix);
		        
				tree.setData(treeData);
				//showcaseData = treeData;
				setShowcaseData(treeData);
		        setData(tree);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				SC.say("获取用户功能列表失败！！！");
			}
		});
    }

    public ExplorerTreeNode[] getShowcaseData() {
        return showcaseData;
    }
    
    public void setShowcaseData(ExplorerTreeNode[] data) {
    	this.showcaseData = data;
    }
}
