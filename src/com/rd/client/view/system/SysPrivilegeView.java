package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.system.SysPrivilegeRoleDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 安全--权限管理
 * @author Administrator
 *
 */
@ClassForNameAble
public class SysPrivilegeView extends SGForm implements PanelFactory {

	public TreeGrid tree = new TreeGrid();
	private TreeGrid treeGrid;
	private DataSource menu;
	private String role_id;
	public String func_id;
	private String str="";
	private ButtonItem searchButton;
	private Criteria criteria;
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		menu = getDataSource(str);
		treeGrid = createTreeGrid(menu);
		
		SGPanel detailForm = new SGPanel();
	    detailForm.setHeight("4%");
	    detailForm.setWidth("50%");
	    createForm(detailForm);
	    
        VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(detailForm);
		main.addMember(treeGrid);
		
		return main;
	}
	
	@Override
	public void createForm(DynamicForm form) {
		final TextItem ROLE_ID=new TextItem("ID");
		ROLE_ID.setVisible(false);
		final ComboBoxItem ROLE_ID_NAME=new ComboBoxItem("ROLE_ID_NAME",Util.TI18N.ROLE_ID());
		ROLE_ID_NAME.setTitleOrientation(TitleOrientation.LEFT);
		ROLE_ID_NAME.setWidth(120);
		initSysRole(ROLE_ID_NAME,ROLE_ID);
		
		final SelectItem SYSTEM = new SelectItem("SYSTEM","系统");
		SYSTEM.setTitleOrientation(TitleOrientation.LEFT);
		Util.initComboValue(SYSTEM,"BAS_CODES","CODE","NAME_C"," where PROP_CODE='SYS_TYP'");
		TextItem SUBSYSTEM_TYPE=new TextItem("SUBSYSTEM_TYPE");
		SUBSYSTEM_TYPE.setVisible(false);
		
		searchButton = new ButtonItem(Util.BI18N.SEARCH());
		searchButton.setIcon(StaticRef.ICON_SEARCH);
		searchButton.setAutoFit(true);
		searchButton.setStartRow(false);
		searchButton.setEndRow(false);
		searchButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(ROLE_ID.getValue() != null && SYSTEM.getValue() != null){
					str = SYSTEM.getValue().toString();
					System.out.println(str);
					
					criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("SUBSYSTEM_TYPE",str);
					criteria.addCriteria("ds_id","USER_FUNCTION");
					criteria.addCriteria("FILTER_FLAG","Y");
//					treeGrid.fetchData(criteria);

					tree = treeGrid;
					System.out.println(tree.getData());
					System.out.println(tree.getData().getAllNodes());
					System.out.println(tree.getData().getAllNodes().length);
//					if(!(tree.getData()==null || 
//							tree.getData().getAllNodes()==null 
//							|| tree.getData().getAllNodes().length == 0)){
						role_id = ROLE_ID.getValue().toString();
						tabChange(tree);
//					}
				}
			}
		});
		
		ButtonItem saveButton = new ButtonItem(Util.BI18N.SAVE());
		setButtonItemEnabled(SysPrivRef.SYSPRIVILEGE_P0_01,saveButton,true);
		saveButton.setIcon(StaticRef.ICON_SAVE);
		saveButton.setAutoFit(true);
		saveButton.setStartRow(false);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(event != null) {
					String login_user = LoginCache.getLoginUser().getUSER_ID();
					ListGridRecord[] records = treeGrid.getSelection();
					ArrayList<String> list  = new ArrayList<String>();
					for(int i=0 ;i<records.length;i++){
						list.add(records[i].getAttributeAsString("FUNCTION_ID"));
					}
					str = SYSTEM.getValue().toString();
					Util.async.savePrivilege(list,ROLE_ID.getValue().toString(),login_user,func_id,str,new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE))
								MSGUtil.showOperSuccess();
							else 
								MSGUtil.showOperError();
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});
		
		form.setItems(ROLE_ID,ROLE_ID_NAME,SYSTEM,SUBSYSTEM_TYPE,searchButton,saveButton);
	}
	
	public void tabChange(final TreeGrid treeGrid){
		treeGrid.invalidateCache();
//		treeGrid.draw();
		treeGrid.fetchData(criteria, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				System.out.println("in");
				Util.db_async.getRolePrivilege(role_id,str, new AsyncCallback<HashMap<String,String>>() {
					
					@Override
					public void onSuccess(HashMap<String, String> result) {
						System.out.println(result);
						if(result.size()>0){
							Tree troo = treeGrid.getTree();
							TreeNode[] node = troo.getAllNodes();
							for(int i = 0;i<node.length;i++){
								String org_id = node[i].getAttribute("FUNCTION_ID");
								if(ObjUtil.isNotNull(result.get(org_id))){
									treeGrid.selectRecord(node[i]);
								}else{
									treeGrid.deselectRecord(node[i]);
								}
							}
							treeGrid.getData().openAll();
							if(node.length > 0){
								treeGrid.getData().closeFolders(troo.getChildren(node[0]));
							}
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						System.out.println(caught.getMessage());
					}
				});
			}
		}); 
		
	}
	
	private TreeGrid createTreeGrid(DataSource ds){
		final TreeGrid treeGrid = new TreeTable(ds, "100%", "100%");
		treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		treeGrid.setShowSelectedStyle(false);
		treeGrid.setShowPartialSelection(true);
		//fanglm 2011-2-21 树形结构级联操作
		treeGrid.setCascadeSelection(true);
		treeGrid.setShowResizeBar(false);
//		treeGrid.addDrawHandler(new DrawHandler() {  
//        	public void onDraw(DrawEvent event) {
//        		treeGrid.fetchData(); 
//        	}  
//        }); 
		
		
		TreeGridField funcField = new TreeGridField();
        funcField.setName("FUNCTION_NAME");
        
        TreeGridField funcField1 = new TreeGridField();
        funcField1.setName("FUNCTION_ID");
        
        treeGrid.setFields(funcField);
        return treeGrid;
	}

	/**
	 * 获取权限数据
	 * @author fanglm
	 * @return
	 */
	static DataSource getDataSource(String SUBSYSTEM_TYPE)
	{
		String url = "initDataServlet";
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(url);
		dataSource.setClientOnly(false);

		// id
		DataSourceTextField idField = new DataSourceTextField("FUNCTION_ID");
		idField.setPrimaryKey(true);
		idField.setRequired(true);
		idField.setHidden(true);

		// parentId
		DataSourceTextField parentIdField = new DataSourceTextField("PARENT_FUNCTION_ID");
		parentIdField.setForeignKey("FUNCTION.FUNCTION_ID");
		parentIdField.setRequired(true);
		parentIdField.setHidden(true);
		parentIdField.setRootValue(0);

		// name
		DataSourceTextField nameField = new DataSourceTextField("FUNCTION_NAME");
		nameField.setRequired(true);

		dataSource.setFields(idField, nameField, parentIdField);

		return dataSource;
	}
	
	private void initSysRole(final ComboBoxItem role_id_name,final TextItem role_id){
		DataSource ds2=SysPrivilegeRoleDS.getInstance("SYS_ROLE1");
		
		ListGridField ID=new ListGridField("ID");
		ID.setHidden(true);
		ListGridField ROLE_ID=new ListGridField("ROLE_ID",Util.TI18N.ROLE_ID(),100);
		ListGridField ROLE_NAME=new ListGridField("ROLE_NAME",Util.TI18N.ROLE_NAME(),100);
		
		role_id_name.setOptionDataSource(ds2);
		role_id_name.setDisabled(false);
		role_id_name.setShowDisabled(false);
		role_id_name.setDisplayField("FULL_INDEX");
		role_id_name.setPickListBaseStyle("myBoxedGridCell");
		role_id_name.setPickListWidth(220);
		role_id_name.setPickListFields(ROLE_ID,ROLE_NAME);
		
		Criteria criteria=new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("ENABLE_FLAG","Y");
		
		role_id_name.setPickListCriteria(criteria);
		
		role_id_name.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				Record record=role_id_name.getSelectedRecord();
				if(record !=null){
					role_id_name.setValue(record.getAttribute("ROLE_NAME"));
					role_id.setValue(record.getAttribute("ID"));
				}
			}
		});
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		SysPrivilegeView view = new SysPrivilegeView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}

}