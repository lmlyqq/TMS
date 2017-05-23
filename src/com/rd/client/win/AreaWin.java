package com.rd.client.win;



import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 行政区域公用弹出窗口
 * @author Administrator
 * 
 */
public class AreaWin extends Window {
	
	private int width = 300;
	private int height = 400;
	private String top = "38%";
	private String left = "40%";
	private String title = "行政区域";
	public Window window;
	private DataSource areaDS;
	private Tree tree;
	private List<TreeNode> commit;              //最后提交的数据
	private List<TreeNode> remain;              //剩余未处理的记录数 
	private ListGrid table;
	private SGCombo TRANS_SERV;
	/**
	 * 
	 * @param where 过滤条件 机构权限
	 */

	
	
	public AreaWin(ListGrid table,String top,String left,SGCombo TRANS_SERV){
		this.table = table;
		this.top = top;
		this.left = left;
		this.TRANS_SERV = TRANS_SERV;
	}
	
	public Window getViewPanel() {
	
		VLayout lay = new VLayout();
		
		//树形结构
		areaDS = getDataSource();
		final TextItem FULL_INDEX = new TextItem("CONTENT","");
		FULL_INDEX.setShowTitle(false);
		FULL_INDEX.setWidth(220);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		final SGPanel searchPanel = new SGPanel();
		searchPanel.setItems(FULL_INDEX,searchBtn);
		
		
		final TreeTable treeGrid = new TreeTable(areaDS, "100%", "90%"); 
		treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		treeGrid.setShowSelectedStyle(true);
		treeGrid.setShowPartialSelection(true);
		treeGrid.setCascadeSelection(true);
		treeGrid.setShowResizeBar(false);
		
        TreeGridField orgField = new TreeGridField("SHORT_NAME");
        treeGrid.setFields(orgField);
        
        //确认和取消按钮
        SGPanel btnForm = new SGPanel();
        SGButtonItem confirmBtn = new SGButtonItem(StaticRef.CONFIRM_BTN);
        SGButtonItem cancelBtn = new SGButtonItem(StaticRef.CANCEL_BTN);
        btnForm.setItems(confirmBtn,cancelBtn);
        
        //取消按钮点击事件
        cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				treeGrid.discardAllEdits();
				
			}
		});
        searchBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
					Criteria criteria = new Criteria();
	        		criteria.addCriteria("OP_FLAG","M");
	        		if(FULL_INDEX.getValue() != null){
	        			criteria.addCriteria(searchPanel.getValuesAsCriteria());
	        		}
	        		treeGrid.fetchData(criteria);  
	        		//treeGrid.redraw();
				
				
			}
		});
        //确认按钮点击事件
        confirmBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				tree = treeGrid.getTree();
				ListGridRecord[] records = treeGrid.getSelection();
				convertRecordToNode(records);   //将ListGridRecord转换成TreeNode,放入到remain当中
				commit = new ArrayList<TreeNode>();
				if(records != null && records.length > 0) {				
					TreeNode node = Tree.nodeForRecord(records[0]);
					allSelected(node);
//					commit.add(node);
				}
				RecordList recordList = new RecordList();
				ListGridRecord rec ;
				RecordList rList = table.getDataAsRecordList(); //已存在数据
				for(int i=0;i<commit.size();i++){
					rec = new ListGridRecord();
					rec.setAttribute("UNLOAD_AREA_ID", commit.get(i).getAttributeAsString("AREA_CODE"));
					rec.setAttribute("UNLOAD_AREA_NAME", commit.get(i).getAttributeAsString("AREA_CNAME"));
					rec.setAttribute("PROVICE_NAME", commit.get(i).getAttributeAsString("PROVICE_NAME"));
					rec.setAttribute("TRANS_SRVC_ID_NAME", TRANS_SERV.getDisplayValue());
					rec.setAttribute("TRANS_SRVC_ID", TRANS_SERV.getValue());
					
					recordList.add(rec);
				}
				
				for(int j=0;j<rList.getLength();j++){
					recordList.add(rList.get(j));
				}
				
				table.setData(recordList);
				window.hide();
			}
		});
        
        lay.addMember(searchPanel);
        lay.addMember(treeGrid);
        lay.addMember(btnForm);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft(left);
		window.setTop(top);
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(lay);
		window.setShowCloseButton(true);
		window.show();
		
		return window;
	}
	
	//树形结构数据源
	static DataSource getDataSource()
	{
		
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(GWT.getHostPageBaseURL()+"basQueryServlet?ds_id=VC_BAS_AREA");
		dataSource.setClientOnly(true);

		// id
		DataSourceTextField idField = new DataSourceTextField("AREA_CODE");
		idField.setPrimaryKey(true);
		idField.setRequired(true);
		idField.setHidden(true);

		// parentId
		DataSourceTextField parentIdField = new DataSourceTextField("PARENT_AREA_ID");
		parentIdField.setForeignKey("Area.AREA_CODE");
		parentIdField.setRequired(true);
		parentIdField.setHidden(true);
		parentIdField.setRootValue(0);

		// name
		DataSourceTextField nameField = new DataSourceTextField("AREA_CNAME");
		nameField.setRequired(true);

		dataSource.setFields(idField, nameField, parentIdField);
		dataSource.setClientOnly(false);

		return dataSource;
	}
	
	private void convertRecordToNode(ListGridRecord[] records) {
		remain = new ArrayList<TreeNode>();
		if(records != null && records.length > 0) {
			for(int i = 0; i < records.length; i++) {
				remain.add(Tree.nodeForRecord(records[i]));
			}
		}
	}
	
	/**
	 * 递归判断当前节点的下级节点是否全部勾选
	 * @author yuanlei
	 * @param node
	 * @return
	 */
	private boolean allSelected(TreeNode node) {
		boolean isSelected = true;                     //当前判断节点是否全部选中
		if(node.getAttributeAsBoolean("isFolder")) {   
			TreeNode[] child_nodes = tree.getChildren(node);    //获取当前节点的下一级节点
			
				//功能级权限
				if(child_nodes != null && child_nodes.length > 0) {
					boolean isAllSelected = true;          //当前区域的下一级是否全部选中，如果全部选中，只需要录入当前区域的信息，不需要录入下级区域信息
					for(int i = 0; i < child_nodes.length; i++) {
						isSelected = allSelected(child_nodes[i]);
						if(!isSelected) {
							isAllSelected = false;
							commit.remove(child_nodes[i]);
						}
					}
					if(isAllSelected) {
						//将当前功能的信息加入 到commit中，移除下一级区域信息
						commit.add(node);
						removeChild(node.getAttribute("AREA_CODE"));
					}
				}
		}
		else {
			
			//无下级节点
			isSelected = false;
			for(int i = 0; i < remain.size(); i++) {
				if(remain.get(i).getAttribute("AREA_CODE").equals(node.getAttribute("AREA_CODE"))) {
					commit.add(node);
					isSelected = true;
					break;
				}
			}
		}
 		return isSelected;
	}

	
	
	/**
	 * 移除指定行政区域ID的所有下级区域
	 * @author yuanlei
	 * @param func_id
	 * @return
	 */
	private void removeChild(String func_id) {
		ListGridRecord rec = null;
		int i = 0;
		while(i < commit.size()) {
			rec = commit.get(i);
			if(ObjUtil.ifNull(rec.getAttributeAsString("PARENT_AREA_ID"),"").compareTo(func_id) == 0) {
				commit.remove(i);
				i = -1;
			}
			i++;
		}
	}
}