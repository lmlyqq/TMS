package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.system.SaveUserGroAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.system.SysRoleDS;
import com.rd.client.ds.system.SysUserGroupDS;
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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
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
 * 用户组管理
 * 
 * @author wangjun
 */
@ClassForNameAble
public class UserGroupView extends SGForm implements PanelFactory {

	private SGTable table;
	private DataSource ds;
	private DataSource usergroupDS;
//	private boolean isMax = false;
	private String group_id;
	private SGTable groupTable;
	private Window searchWin;//
    private SGPanel searchForm;//
    private SectionStack sectionStack;
    private ValuesManager vm;

	/*public UserGroupView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth100();

		ds = SysUserGroupDS.getInstance("U_BAS_CODES");
		usergroupDS = SysRoleDS.getInstance("V_USERGROUP");
		vm = new ValuesManager();

		// 主布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();

		// 左边列表
		table = new SGTable(ds, "100%", "100%");
		table.setShowFilterEditor(false);
		createListField();
//		Criteria criteria = new Criteria();
//		criteria.addCriteria("OP_FLAG", "M");
//		table.fetchData(criteria,new DSCallback() {
//			
//			@Override
//			public void execute(DSResponse response, Object rawData, DSRequest request) {
//				if(table.getRecords().length > 0){
//					table.selectRecord(0);
//				}
//			}
//		});
		sectionStack = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		sectionStack.setWidth("35%");

		stack.addMember(sectionStack);

		// 右边布局
		TabSet leftTabSet = new TabSet();
		leftTabSet.setWidth("65%");
		leftTabSet.setHeight("100%");
		leftTabSet.setMargin(0);
		
		if(isPrivilege(SysPrivRef.USERGROUP_P1)) {
			
			Tab tab = new Tab(Util.TI18N.USERGROP_ROLE());
			tab.setPane(createMainInfo());
			leftTabSet.addTab(tab);
		}

		ToolStrip strip = new ToolStrip();
		strip.setAlign(Alignment.RIGHT);
		createBtnWidget(strip);

		stack.addMember(leftTabSet);
		main.addMember(strip);
		main.addMember(stack);

		table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
//				JavaScriptObject canvas = listItem
//						.getAttributeAsJavaScriptObject("controls");
//				Canvas[] page_form = Canvas.convertToCanvasArray(canvas);
//
//				if (isMax) {
//					sectionStack.setWidth(320);
//					page_form[0].setVisible(false);
//				} else {
//					sectionStack.setWidth100();
//					page_form[0].setVisible(true);
//				}
//				isMax = !isMax;
				
			}

		});

		table.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				table.setProperty("selectedRowNum", event.getRecordNum());
				if(table.getSelectedRecord() != null && table.getRecord(event.getRecordNum()) == null){
					table.deselectRecord(table.getSelectedRecord());
				}
//				if(event.getRecord()==null)return ;
//				group_id = event.getRecord().getAttributeAsString("GRP_CODE");
//
//				Criteria criteria = new Criteria();
//				criteria.addCriteria("OP_FLAG", "M");
//				criteria.addCriteria("USERGRP_ID", group_id);
//				groupTable.invalidateCache();
//				groupTable.fetchData(criteria);
//				groupTable.discardAllEdits();

			}
		});
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(event.getRecord()==null)return ;
				group_id = event.getRecord().getAttributeAsString("GRP_CODE");

				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG", "M");
				criteria.addCriteria("USERGRP_ID", group_id);
				groupTable.invalidateCache();
				groupTable.discardAllEdits();
				groupTable.fetchData(criteria);
				
			}
		});

		return main;
	}

	private void createListField() {

		ListGridField USER_ID = new ListGridField("GRP_CODE", Util.TI18N
				.UGROUP_CODE(), 130);
		ListGridField USER_NAME = new ListGridField("GRP_NAME", Util.TI18N
				.UGROUP_NAME_C(), 130);
		table.setAutoSaveEdits(false);
		table.setAutoFetchData(false);
		table.setFields(USER_ID, USER_NAME);
	}

	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SysPrivRef.USERGROUP);
        searchButton.addClickHandler(new ClickHandler() {
			/**
			 * 嵌套，事件，点击事件触发，获取记录集。
			 */
			@Override
			public void onClick(ClickEvent event) {
				
				
				if(searchWin==null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds, 
							createSerchForm(searchForm), sectionStack.getSection(0),vm).getViewPanel();
				}else{
					searchWin.show();
				}
			}

			private DynamicForm createSerchForm(DynamicForm searchForm) {
				
				//第一行：模糊查询
				TextItem USER_GLOBAL = new TextItem("CONTENT", Util.TI18N.FUZZYQRY());
				USER_GLOBAL.setTitleOrientation(TitleOrientation.TOP);
				USER_GLOBAL.setWidth(375);
				USER_GLOBAL.setColSpan(5);
				USER_GLOBAL.setEndRow(true);
				
				//第二行：用户编号查询：
				SGText GRP_CODE = new SGText("GRP_CODE",Util.TI18N.UGROUP_CODE(),true);
//				USER_ID.setTitleOrientation(TitleOrientation.LEFT);
				
				//第二行：用户名查询：
				SGText GRP_NAME = new SGText("GRP_NAME",Util.TI18N.UGROUP_NAME_C());
//				USER_NAME.setEndRow(true);
				
				//第三行：默认组织
				SGText USER_ORG_ID_NAME = new  SGText("USER_ORG_ID_NAME",Util.TI18N.USER_ORG_ID(),true);
				USER_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
				
				TextItem USER_ORG_ID = new TextItem("ORG_ID");
				USER_ORG_ID.setVisible(false);
				USER_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				
				Util.initOrg(USER_ORG_ID_NAME, USER_ORG_ID, false, "30%", "38%");
				
//				Util.initComboValue(USER_ORG_ID, "BAS_ORG", "ID", "ORG_CNAME", " WHERE ENABLE_FLAG='Y'");
				
				//第三行：激活
//				SGCheck ACTIVE_FLAG = new SGCheck("ACTIVE_FLAG", Util.TI18N.ENABLE_FLAG());
//				ACTIVE_FLAG.setValue(true);
				
				SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
				C_ORG_FLAG.setValue(true);//包含下级机构
				C_ORG_FLAG.setColSpan(2);
				
				searchForm.setItems(USER_GLOBAL,GRP_CODE,GRP_NAME,USER_ORG_ID_NAME,USER_ORG_ID,C_ORG_FLAG);
				
				return searchForm;
			}
		});
        
        strip.addMember(searchButton);
		
		IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.USERGROUP_P0_01);
		newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				table.startEditingNew();
				table.setProperty("selectedRowNum", table.getEditRow());
			}
		});
		strip.addMember(newButton);
		
		// 保存按钮
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERGROUP_P0_01);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int[] ers = table.getAllEditRows();
				String[] checkFields = {"ID","GRP_CODE"};
				if(ers.length > 0 ){
					List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
					for (int rowNum : ers) {
						Object id = table.getEditValue(rowNum, "ID");
						Object code = table.getEditValue(rowNum, "GRP_CODE");
						if(code == null && table.getRecord(rowNum) != null){
							code = table.getRecord(rowNum).getAttribute("GRP_CODE");
						}
						Object nameC = table.getEditValue(rowNum, "GRP_NAME");
						if(nameC == null && table.getRecord(rowNum) != null){
							nameC = table.getRecord(rowNum).getAttribute("GRP_NAME");
						}
						if(ObjUtil.isNotNull(code) && ObjUtil.isNotNull(nameC)){
							Map<String, String> dataMap = new HashMap<String, String>();
							dataMap.put("ID", (String)id);
							dataMap.put("GRP_CODE", code.toString());
							dataMap.put("GRP_NAME", nameC.toString());
							dataMap.put("ACTIVE_FLAG", "Y");
							if(ObjUtil.isNotNull(id)){
								dataMap.put("EDITWHO", "wpsadmin");
								dataMap.put("EDITTIME", "sysdate");
							}else{
								dataMap.put("ADDTIME", "sysdate");
								dataMap.put("ADDWHO", "wpsadmin");
							}
							dataMap.put("ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
							dataMap.put("TableName", "SYS_USERGRP");
							dataList.add(dataMap);
						}
					}
					
					Util.async.excuteSQLListByMap(dataList, checkFields, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result)){
								MSGUtil.showOperSuccess();
								table.invalidateCache();
								table.discardAllEdits();
								Criteria criteria = table.getCriteria();
								if(criteria == null) {
									criteria = new Criteria();
								}
								criteria.addCriteria("OP_FLAG", "M");
								table.fetchData(criteria,new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(table.getRecords().length > 0){
											table.selectRecord(0);
										}
										table.redraw();
									}
								});
								
							}else if("uniquene".equals(result)){
								MSGUtil.sayError("用户组必须唯一");
							}else{
								MSGUtil.sayError(result);
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}
					});
				}
			}
		});
		strip.addMember(saveButton);

		IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.USERGROUP_P0_01);
		delButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] lgrs = table.getSelection();
				String rowNum = JSOHelper.getAttribute(table.getJsObj(), "selectedRowNum");
				if(lgrs != null && lgrs.length > 0){
					SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
		                    if (value != null && value) {
		                    	Util.db_async.deletePro(table.getSelectedRecord().getAttribute("ID"), 
		    							"SYS_USERGRP",LoginCache.getLoginUser().getUSER_ID(), new AsyncCallback<String>() {
		    						
		    						@Override
		    						public void onFailure(Throwable caught) {
		    							MSGUtil.sayError(caught.getMessage());
		    						}
		    				
		    						@Override
		    						public void onSuccess(String result) {
		    							if(result.equals(StaticRef.SUCCESS_CODE)) {
		    								MSGUtil.showOperSuccess();
		    								table.invalidateCache();
		    								table.discardAllEdits();
		    								Criteria criteria = table.getCriteria();
		    								if(criteria == null) {
		    									criteria = new Criteria();
		    								}
		    								criteria.addCriteria("OP_FLAG", "M");
		    								table.fetchData(criteria,new DSCallback() {
		    									
		    									@Override
		    									public void execute(DSResponse response, Object rawData, DSRequest request) {
		    										if(table.getRecords().length > 0){
		    											table.selectRecord(0);
		    											table.redraw();
		    										}
		    									}
		    								});
		    							}
		    							else {
		    								MSGUtil.sayError(result);
		    							}
		    						}
		    					});
		                    }
		                }
		            });
					
				}else if(ObjUtil.isNotNull(rowNum)){
					table.discardAllEdits(new int[]{Integer.parseInt(rowNum)}, false);
					table.setProperty("selectedRowNum", "");
				}else{
					MSGUtil.sayError("请先选择待删除记录！");
				}
			}
		});
		strip.addMember(delButton);
		
		strip.setMembersMargin(4);
	}

	private VLayout createMainInfo() {
		 VLayout layOut = new VLayout();
		 layOut.setWidth100();
		 layOut.setHeight100();

		groupTable = new SGTable(usergroupDS);
		groupTable.setShowFilterEditor(false);
		groupTable.setEditEvent(ListGridEditEvent.CLICK);
		groupTable.setShowRowNumbers(true);

		ListGridField USE_FLAG = new ListGridField("USE_FLAG", "选择", 60);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.setCanEdit(true);
		ListGridField USER = new ListGridField("ROLE_ID", Util.TI18N.ROLE_ID(),
				140);
		ListGridField USER_NAME = new ListGridField("ROLE_NAME", Util.TI18N
				.ROLE_NAME(), 140);
		
		groupTable.setFields(USE_FLAG, USER, USER_NAME);
		
		ToolStrip strip = new ToolStrip();
		strip.setAlign(Alignment.RIGHT);
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		
		// 保存按钮
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.USERGROUP_P0_01);
		saveButton.addClickHandler(new SaveUserGroAction(groupTable, table));
		strip.addMember(saveButton);

		IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.USERGROUP_P0_02);
		canButton.addClickHandler(new CancelAction(groupTable));
		strip.addMember(canButton);
		
		layOut.addMember(groupTable);
		layOut.addMember(strip);
		strip.setMembersMargin(4);
		return layOut;

	}

	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public void initVerify() {

	}

	@Override
	public void onDestroy() {

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		UserGroupView view = new UserGroupView();
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
