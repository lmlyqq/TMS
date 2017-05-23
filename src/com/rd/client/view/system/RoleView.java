package com.rd.client.view.system;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.RoleDS;
import com.rd.client.ds.system.RoleUserDS;
import com.rd.client.ds.system.RoleUserGroupDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 安全->角色管理
 * 
 * @author fanglm
 * 
 */
@ClassForNameAble
public class RoleView extends SGForm implements PanelFactory {

	private SGTable table;
	private DataSource roleDS;
	private SGTable roleUserTable;
	private SGTable roleUserGroupTable;
	private DataSource usergroupDS;
	private DataSource userDS;
	private Window searchWin;
	private SGPanel searchForm;
//	private boolean isMax = false;
	private String role_id;
	private SectionStack section;

	/*public RoleView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		initVerify();
		// 创建按钮布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);

		roleDS = RoleDS.getInstance("SYS_ROLE");

		// 主布局
		HStack Stack = new HStack();// 设置详细信息布局
		Stack.setWidth100();
		Stack.setHeight100();

		// STACK的左边列表
//		table = new SGTable(roleDS, "100%", "100%", true, true, true);
		table = new SGTable(roleDS, "100%", "100%");
		table.setShowFilterEditor(false);
		//table.setListEndEditAction(RowEndEditAction.NEXT);
		//table.setAutoFitData(Autofit.VERTICAL);  
		// getConfigList();
		createListField();
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG", "M");
//		table.setCriteria(criteria);
		table.fetchData(criteria,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(table.getRecords().length > 0){
					table.selectRecord(0);
				}
			}
		});

		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N
				.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		listItem.setControls(new SGPage(table, false).initPageBtn());
		section.addSection(listItem);
		section.setWidth("35%");

		Stack.addMember(section);

		// STACK的右边布局

		TabSet leftTabSet = new TabSet();
		leftTabSet.setWidth("65%");
		leftTabSet.setHeight("100%");
		leftTabSet.setMargin(0);

	
		if(isPrivilege(SysPrivRef.ROLE_P1)) {	
			
			Tab tab1 = new Tab("授权用户");
			// 组织明细的FORM布局
			tab1.setPane(createMainInfo());
			leftTabSet.addTab(tab1);
		}
	
		if(isPrivilege(SysPrivRef.ROLE_P2)) {
			
			Tab tab2 = new Tab("授权用户组");
			tab2.setPane(createCustTable());
			leftTabSet.addTab(tab2);
		}
		
		Stack.addMember(leftTabSet);

		createBtnWidget(toolStrip);

		main.addMember(toolStrip);
		main.addMember(Stack);

		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

//				JavaScriptObject canvas = listItem
//						.getAttributeAsJavaScriptObject("controls");
//				Canvas[] page_form = Canvas.convertToCanvasArray(canvas);
//
//				if (isMax) {
//					section.setWidth(300);
//					page_form[0].setVisible(false);
//				} else {
//					section.setWidth100();
//					page_form[0].setVisible(true);
//				}
//				isMax = !isMax;
				
				

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				// TODO Auto-generated method stub
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}

		});
		table.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if (event.getRecord() == null) {
					return;
				}
				role_id = event.getRecord().getAttributeAsString("ID");
				table.OP_FLAG = "M";
//				enableOrDisables(del_map, true);
//				initSaveBtn();
				
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG", "M");
				criteria.addCriteria("ROLE_ID", role_id);
				roleUserTable.fetchData(criteria, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG", "M");
						criteria.addCriteria("ROLE_ID", role_id);
						roleUserGroupTable.invalidateCache();
						roleUserGroupTable.fetchData(criteria);
					}
					
				});
			}
			
		});
//		table.addRecordClickHandler(new RecordClickHandler() {
//
//			@Override
//			public void onRecordClick(RecordClickEvent event) {
//				/*if (event.getRecord() == null) {
//					return;
//				}
//				role_id = event.getRecord().getAttributeAsString("ID");
//				table.OP_FLAG = "M";
////				enableOrDisables(del_map, true);*/
////				initSaveBtn();
//				
//				/*Criteria criteria = new Criteria();
//				criteria.addCriteria("OP_FLAG", "M");
//				criteria.addCriteria("ROLE_ID", role_id);
//				roleUserTable.fetchData(criteria, new DSCallback() {
//
//					@Override
//					public void execute(DSResponse response, Object rawData,
//							DSRequest request) {
//						Criteria criteria = new Criteria();
//						criteria.addCriteria("OP_FLAG", "M");
//						criteria.addCriteria("ROLE_ID", role_id);
//						roleUserGroupTable.fetchData(criteria);
//					}
//					
//				});*/
//			}
//		});

		return main;
	}

	@Override
	public void createForm(DynamicForm form) {
		/*
		 * TextItem ROLE_ID = new TextItem("ROLE_ID", Util.TI18N.ROLE_ID());
		 * ROLE_ID.setColSpan(3); TextItem ROLE_NAME = new TextItem("ROLE_NAME",
		 * Util.TI18N.ROLE_NAME()); ROLE_NAME.setColSpan(3); CheckboxItem
		 * ACTIVE_FLAG = new
		 * CheckboxItem("ACTIVE_FLAG",Util.TI18N.ENABLE_FLAG()); CheckboxItem
		 * MODIFY_FLAG = new
		 * CheckboxItem("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG());
		 * 
		 * 
		 * form.setFields(ROLE_ID, ROLE_NAME,ACTIVE_FLAG, MODIFY_FLAG);
		 * form.setAutoFetchData(true);
		 */

	}

	/* private void getConfigList() {
	 List<String> fldList = new ArrayList<String>();
	 List<String> titList = new ArrayList<String>();
	 List<String> widList = new ArrayList<String>();
	 List<ListGridFieldType> typList = new ArrayList<ListGridFieldType>();
	
	 String[] fields = { "ROLE_ID", "ROLE_NAME", "ENABLE_FLAG" };
	 fldList = Arrays.asList(fields);
	 String[] titles = { Util.TI18N.ROLE_ID(), Util.TI18N.ROLE_NAME(),
	 Util.TI18N.ENABLE_FLAG() };
	 titList = Arrays.asList(titles);
	
	 String[] width = { "100", "144", "50" };
	 widList = Arrays.asList(width);
	
	 ListGridFieldType[] types = { null, null, ListGridFieldType.BOOLEAN,
	 ListGridFieldType.BOOLEAN, null, null };
	 typList = Arrays.asList(types);
	
	 createListField(table, fldList, titList, widList, typList);
	 }*/

	public void createListField() {

		ListGridField ROLE_ID = new ListGridField("ROLE_ID", Util.TI18N
				.ROLE_ID(), 110);
		ListGridField ROLE_NAME = new ListGridField("ROLE_NAME", Util.TI18N
				.ROLE_NAME(), 140);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG", Util.TI18N
				.ENABLE_FLAG(), 50);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);

		table.setFields(ROLE_ID, ROLE_NAME, ENABLE_FLAG);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();

		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		toolStrip.addMember(searchButton);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(roleDS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});

		/*
		 * IButton newButton = createBtn(StaticRef.CREATE_BTN);
		 * newButton.addClickHandler(new NewAction(table,cache_map));
		 * toolStrip.addMember(newButton);
		 * 
		 * IButton saveButton = createBtn(StaticRef.SAVE_BTN);
		 * saveButton.addClickHandler(new SaveAction(table));
		 * toolStrip.addMember(saveButton);
		 * 
		 * IButton delButton = createBtn(StaticRef.DELETE_BTN);
		 * delButton.addClickHandler(new DeleteAction(table));
		 * toolStrip.addMember(delButton);
		 * 
		 * IButton canButton = createBtn(StaticRef.CANCEL_BTN);
		 * canButton.addClickHandler(new CancelAction(table));
		 * toolStrip.addMember(canButton);
		 * 
		 * IButton expButton = createBtn(StaticRef.EXPORT_BTN);
		 * expButton.addClickHandler(new ExportAction(table, "ADDTIME"));
		 * toolStrip.addMember(expButton);
		 */

		// 新增按钮
		IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.ROLE_P0_01);
		newButton.addClickHandler(new NewAction(table, cache_map,this));

		// 保存按钮
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.ROLE_P0_02);
		saveButton.addClickHandler(new SaveAction(table, check_map,this));

		// 删除按钮
		IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.ROLE_P0_03);
		delButton.addClickHandler(new DeleteProAction(table));

		// 取消按钮
		IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.ROLE_P0_04);
		canButton.addClickHandler(new CancelAction(table,this));

		// 导出按钮
		//IButton expButton = createBtn(StaticRef.EXPORT_BTN,SysPrivRef.ROLE_P0_05);
		//expButton.addClickHandler(new ExportAction(table, "addtime desc"));

        add_map.put(SysPrivRef.ROLE_P0_01, newButton);
        del_map.put(SysPrivRef.ROLE_P0_03, delButton);
        save_map.put(SysPrivRef.ROLE_P0_02, saveButton);
        save_map.put(SysPrivRef.ROLE_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, delButton,
				canButton);

	}

	private DynamicForm createSerchForm(DynamicForm searchForm) {

		searchForm.setDataSource(roleDS);
		searchForm.setAutoFetchData(false);
		searchForm.setWidth100();
		searchForm.setCellPadding(2);

		// 第一行：模糊查询
		TextItem USER_GLOBAL = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		USER_GLOBAL.setTitleOrientation(TitleOrientation.LEFT);
		USER_GLOBAL.setWidth(375);
		USER_GLOBAL.setColSpan(5);
		USER_GLOBAL.setEndRow(true);

		// 第二行：激活
		CheckboxItem CHK_ENABLE = new CheckboxItem("ENABLE_FLAG", Util.TI18N
				.ENABLE_FLAG());
		CHK_ENABLE.setValue(true);
		searchForm.setItems(USER_GLOBAL, CHK_ENABLE);

		return searchForm;
	}

	private VLayout createMainInfo() {
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		userDS = RoleUserDS.getInstance("V_ROLE_USER");
		roleUserTable = new SGTable(userDS);
		roleUserTable.setEditEvent(ListGridEditEvent.CLICK);
		roleUserTable.setShowFilterEditor(false);
		roleUserTable.setShowRowNumbers(true);
		ListGridField USER = new ListGridField("USER_ID", Util.TI18N
				.ROLE_USER(), 140);
		ListGridField USER_NAME = new ListGridField("USER_NAME", Util.TI18N
				.ROLE_USER_NAME(), 140);

		roleUserTable.setFields(USER, USER_NAME);

		layOut.addMember(roleUserTable);

		return layOut;

	}

	private VLayout createCustTable() {
		VLayout layOut = new VLayout();
		layOut.setWidth100();
		layOut.setHeight100();
		
		usergroupDS = RoleUserGroupDS.getInstance("V_ROLE_USERGRP");
		roleUserGroupTable = new SGTable(usergroupDS);
		roleUserGroupTable.setEditEvent(ListGridEditEvent.CLICK);
		roleUserGroupTable.setShowFilterEditor(false);
		roleUserGroupTable.setShowRowNumbers(true);
		ListGridField USER_GROUP = new ListGridField("USERGRP_ID", Util.TI18N
				.ROLE_USER_GROUP(), 140);
		ListGridField USER_GROUP_NAME = new ListGridField("USERGRP_ID_NAME",
				Util.TI18N.ROLE_USER_GROUP_NAME(), 140);

		roleUserGroupTable.setFields(USER_GROUP, USER_GROUP_NAME);

		layOut.addMember(roleUserGroupTable);

		return layOut;

	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "SYS_ROLE");
		check_map.put("ROLE_ID", StaticRef.CHK_NOTNULL+Util.TI18N.ROLE_ID());
		check_map.put("ROLE_NAME", StaticRef.CHK_NOTNULL+Util.TI18N.ROLE_NAME());
		cache_map.put("ENABLE_FLAG", "Y");
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		RoleView view = new RoleView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
}
