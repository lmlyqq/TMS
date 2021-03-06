package com.rd.client.view.base;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.org.DeleteOrgAction;
import com.rd.client.action.base.org.NewOrgAction;
import com.rd.client.action.base.org.SaveOrgAction;
import com.rd.client.action.base.org.SaveOrgAreaInfoAction;
import com.rd.client.action.base.org.SyncPrivilegeAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.common.widgets.TreeTable;
import com.rd.client.ds.base.AreaDS1;
import com.rd.client.ds.base.OrgCustomerDS;
import com.rd.client.ds.base.OrgDS;
import com.rd.client.ds.base.SupplierListDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
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
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 基础资料->物流组织
 * 
 * @author yuanlei
 * 
 */
@ClassForNameAble
public class BasOrgView extends SGForm implements PanelFactory {

	private DataSource ds;
	private DataSource cusDS;// lijun添加---->客户列表数据源
	private DataSource SupplierDS;//lijun
	private TreeTable tree;
	private SGTable SupplierTable;//lijun
	private SGTable cusTable;//lijun 
	private DataSource areaDs;
	private TreeTable areaTable;
	private DynamicForm form;
	private SGPanel base_form;
	private ValuesManager form_group;
	private Window searchWin = null;
	private static String ORG_ID;// lijun 组织机构ID
	private SGPanel searchForm = new SGPanel();
	private SectionStack lst_section;
	private ComboBoxItem PARENT_ORG_ID;	
	public TreeNode selectNode;
	private TabSet rightTabSet;
	/*public BasOrgView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		form_group = new ValuesManager();

		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

		ToolStrip toolStrip = new ToolStrip(); // 按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = OrgDS.getInstance("BAS_ORG");
		// 主布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();

		// STACK的左边列表
		tree = new TreeTable(ds, "100%", "100%");
		tree.setSelectionType(SelectionStyle.SINGLE);
		//tree.setShowRoot(true);
		getConfigList();

		lst_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(tree);
		listItem.setExpanded(true);
//		listItem.setControls(addMaxBtn(lst_section, stack, "200"));  
		lst_section.addSection(listItem);
		lst_section.setWidth("30%");
		stack.addMember(lst_section);

		// STACK的右边布局
		rightTabSet = new TabSet();
		rightTabSet.setWidth("70%");
		rightTabSet.setHeight("100%");
		rightTabSet.setMargin(0);

		if(isPrivilege(BasPrivRef.ORG_P1)) {
			Tab tab1 = new Tab("组织明细");
			SectionStack section = new SectionStack();
			section.setWidth100();
			section.setHeight100();
			SectionStackSection mainItem = new SectionStackSection(Util.TI18N
					.MAININFO());
			SectionStackSection baseItem = new SectionStackSection(Util.TI18N
					.BASEINFO());
	
			// 组织明细的FORM布局
			form = new DynamicForm();
			form.setBackgroundColor(ColorUtil.BG_COLOR);
			createForm(form);
			mainItem.setItems(form);
			mainItem.setExpanded(true);
			base_form = new SGPanel();
			base_form.setBackgroundColor(ColorUtil.BG_COLOR);
			createBaseForm(base_form);
			baseItem.setItems(base_form);
			baseItem.setExpanded(true);
	
			form_group.addMember(form);
			form_group.addMember(base_form);
			form_group.setDataSource(ds);
	
			section.setSections(mainItem, baseItem);
			section.setVisibilityMode(VisibilityMode.MULTIPLE);
			section.setAnimateSections(false);
			tab1.setPane(section);
			rightTabSet.addTab(tab1);
		}

		if(isPrivilege(BasPrivRef.ORG_P2)) {
			Tab tab2 = new Tab("客户列表");
			cusDS = OrgCustomerDS.getInstance("V_ORG_CUSTOMER");
			//SGTable table = new SGTable(cusDS, "100%", "70%");
	//		SearchCustList(table);
			tab2.setPane(customerList());
			rightTabSet.addTab(tab2);
		}

		if(isPrivilege(BasPrivRef.ORG_P3)) {
			Tab tab3 = new Tab("承运商列表");
			SupplierDS = SupplierListDS.getInstall("V_ORG_SUPPLIER");
			tab3.setPane(supplierList());
			rightTabSet.addTab(tab3);
		}
		if(isPrivilege(BasPrivRef.ORG_P4)) {
			Tab tab4 = new Tab("行政区域列表");
			tab4.setPane(areaList());
			rightTabSet.addTab(tab4);
		}

		stack.addMember(rightTabSet);

		// 创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);

		initVerify();

		tree.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {				
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}

		});
		
		tree.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				ORG_ID = event.getRecord().getAttribute("ID");
				if(ORG_ID != null) {
					//String index=rightTabSet.getTab(rightTabSet.getTabIndex()).getTitle();
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG", "M");
					criteria.addCriteria("ID", ORG_ID);
					if(rightTabSet.getSelectedTabNumber()==1){
						if(cusTable != null) {
							cusTable.fetchData(criteria);
						}
					}
					if(rightTabSet.getSelectedTabNumber()==2){
						if(SupplierTable != null) {
							SupplierTable.fetchData(criteria);
						}
					}
					if(rightTabSet.getSelectedTabNumber()==3){
						if(areaTable != null) {
							Util.db_async.getOrgAreaInfo(ORG_ID, "", new AsyncCallback<HashMap<String,String>>() {
								
								@Override
								public void onSuccess(HashMap<String, String> result) {
									Tree troo = areaTable.getTree();
									TreeNode[] node = troo.getAllNodes();
									if(node != null) {
										for(int i = 0;i<node.length;i++){
											String area_code = node[i].getAttribute("AREA_CODE");
											if(ObjUtil.isNotNull(result.get(area_code))){
												areaTable.selectRecord(node[i]);
											}else{
												areaTable.deselectRecord(node[i]);
											}
										}
										/*if(troo.getChildren(node[0]) != null) {
											areaTable.getData().closeFolders(troo.getChildren(node[0]));
										}*/
									}
									
								}
								
								@Override
								public void onFailure(Throwable caught) {
									
								}
							});
						}
					}
						
					initSaveBtn();
				}
			}
		});
		

		return main;
	}

	private VLayout customerList() {
		VLayout layout = new VLayout();

		cusTable = new SGTable(cusDS);
		cusTable.setEditEvent(ListGridEditEvent.CLICK);
		cusTable.setShowRowNumbers(true);

		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE",
				Util.TI18N.CUSTOMER_CODE(),180);

		ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_CNAME",
				Util.TI18N.CUSTOMER_CNAME(),230);

		cusTable.setFields(CUSTOMER_CODE, CUSTOMER_CNAME);
		
		layout.addMember(cusTable);

		return layout;
	}
	
	private VLayout supplierList(){
		VLayout layout = new VLayout();
		SupplierTable = new SGTable(SupplierDS);
		
		SupplierTable.setEditEvent(ListGridEditEvent.CLICK);
		SupplierTable.setShowRowNumbers(true);
		
		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE",Util.TI18N.SUP_SUPLR_CODE(),180);
		ListGridField SUPLR_CNAME = new ListGridField("SUPLR_CNAME","承运商名称",230);
		
		SupplierTable.setFields(SUPLR_CODE,SUPLR_CNAME);
		
		layout.addMember(SupplierTable);
		
		
		return layout;
	}
	
	private VLayout areaList() {
		areaDs = AreaDS1.getInstance("BAS_AREA1", "BAS_AREA");
		VLayout layout = new VLayout();
		areaTable = new TreeTable(areaDs,"100%","100%");
		areaTable.setCanEdit(true);	
		areaTable.setCanAcceptDroppedRecords(false);
		areaTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		areaTable.setShowSelectedStyle(false);
		areaTable.setShowPartialSelection(true);
		areaTable.setCascadeSelection(true);
		areaTable.setShowResizeBar(false);
		
        TreeGridField orgField = new TreeGridField();
        orgField.setName("SHORT_NAME");
        areaTable.setFields(orgField);
        //Lang 注释
//		areaTable.addNodeClickHandler(new NodeClickHandler() {
//			
//			@Override
//			public void onNodeClick(NodeClickEvent event) {
//				 TreeNode node = event.getNode();
//				 selectNode = node;
////				 selectNode.setCanAcceptDrop(false);
//				 
//	                try {
//	                	Criteria criteria = new Criteria();
//	            		criteria.addCriteria("PARENT_AREA_ID",node.getAttribute("AREA_CODE"));
//	            		criteria.addCriteria("OP_FLAG","M");
//	            		criteria.addCriteria("is_curr_page","true");
//	            		areaTable.fetchData(criteria);
//	                	
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//			}
//		});
		areaTable.addDrawHandler(new DrawHandler() {  
        	public void onDraw(DrawEvent event) {  
        		Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
        		areaTable.fetchData(criteria);  
        	}  
        }); 
		layout.addMember(areaTable);
		
		IButton savB3 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.ORG_P4_01);
		savB3.setIcon(StaticRef.ICON_SAVE);
		savB3.setWidth(60);
		savB3.setAutoFit(true);
		savB3.setAlign(Alignment.RIGHT);
		savB3.addClickHandler(new SaveOrgAreaInfoAction(tree,areaTable));
		
		IButton canB3 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.ORG_P4_02);
		canB3.setIcon(StaticRef.ICON_CANCEL);
		canB3.setWidth(60);
		canB3.setAutoFit(true);
		canB3.setAlign(Alignment.RIGHT);
		
		canB3.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				areaTable.discardAllEdits();
			}
		});
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(savB3,canB3);
		
		layout.addMember(toolStrip);
		
		return layout;
	}

	@Override
	public void createForm(DynamicForm form) {

		form.setAlign(Alignment.LEFT);
		form.setNumCols(10);
		form.setWidth("690");
		form.setHeight("30%");
		//form.setCellPadding(3);
		form.setTitleSuffix("");
		form.setValuesManager(form_group);
		form.setTitleWidth(70);

		// 1
		final SGText ORG_CNAME = new SGText("ORG_CNAME", ColorUtil.getRedTitle(Util.TI18N.ORG_CNAME()));

		SGText ORG_CODE = new SGText("ORG_CODE","机构代码");
		
		final SGLText ORG_ENAME = new SGLText("ORG_ENAME", Util.TI18N.ORG_ENAME());
	
		// 2
		final SGText SHORT_NAME = new SGText("SHORT_NAME", ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()),true);

		final SGText HINT_CODE = new SGText("HINT_CODE", ColorUtil.getRedTitle(Util.TI18N.HINT_CODE()));

		// 生成助记码
		SHORT_NAME.addBlurHandler(new GetHintAction(SHORT_NAME, HINT_CODE));
	

		SGText ORG_COMPANY = new SGText("ORG_COMPANY", Util.TI18N.ORG_COMPANY());
		
		SGCombo PARENT_ORG_ID = new SGCombo("PARENT_ORG_ID",Util.TI18N.PARENT_ORG_NAME());
		PARENT_ORG_ID.setDisabled(true);
		Util.initComboValue(PARENT_ORG_ID, "V_ORG", "ID", "ORG_CNAME", " ",
				" ORG_LEVEL ASC");
	
		//3
		SGText SHOW_SEQ = new SGText("SHOW_SEQ", Util.TI18N.SHOW_SEQ(),true);
		
		SGCheck SEPACCOUNT_FLAG = new SGCheck("SEPACCOUNT_FLAG",Util.TI18N.SEPACCOUNT_FLAG());
		
		SGCheck CORPORG_FLAG = new SGCheck("CORPORG_FLAG", Util.TI18N.CORPORG_FLAG());//法人机构
		
		SGCheck TRANSORG_FLAG = new SGCheck("TRANSORG_FLAG",//中转机构
				Util.TI18N.TRANSORG_FLAG());
		TRANSORG_FLAG.setEndRow(false);

		CheckboxItem WAREHOUSE_FLAG = new CheckboxItem("WAREHOUSE_FLAG",
				Util.TI18N.WAREHOUSE_FLAG());
		WAREHOUSE_FLAG.setTitleOrientation(TitleOrientation.TOP);
		WAREHOUSE_FLAG.setWidth(50);
		
		//该键盘按键事件去时需要确认是什么情况。
		ORG_CNAME.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getKeyName() != null
						&& event.getKeyName().equals("Tab")) {
					if (ORG_CNAME.getValue() != null) {
						if (SHORT_NAME.getValue() != null) {
							return;
						} else {
							Object obj = ORG_CNAME.getValue();
							SHORT_NAME.setDefaultValue((String) obj);
						}
						if (ORG_ENAME.getValue() != null) {
							return;
						} else {
							String str = (String) ORG_CNAME.getValue();
							ORG_ENAME.setDefaultValue(str);
						}
					}
				}
			}
		});

		form.setFields(ORG_CNAME,ORG_CODE, ORG_ENAME,  SHORT_NAME,HINT_CODE, ORG_COMPANY,PARENT_ORG_ID,  SHOW_SEQ,
				SEPACCOUNT_FLAG, CORPORG_FLAG, TRANSORG_FLAG);
	}

	public void createBaseForm(SGPanel form) {
		
		form.setWidth("690");
    	form.setHeight("70%");
    	form.setNumCols(10);
    	
		// 1
		SGCombo ORG_TYPE = new SGCombo("ORG_TYPE", Util.TI18N.ORG_TYPE(),true);
		Util.initCodesComboValue(ORG_TYPE, "ORG_TYP");
		// 行政区域初始化：lijun
		SGCombo AREA_ID_NAME = new SGCombo("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		Util.initCodesComboValue(AREA_ID_NAME, "AREA_ID");

		SGLText ADDRESS = new SGLText("ADDRESS", Util.TI18N.ORG_ADDRESS());

		// 2
		SGText ORG_LEADER = new SGText("LEADER", Util.TI18N.ORG_LEADER(),true);
		
		SGText CONTACTER = new SGText("CONTACTER", Util.TI18N.ORG_CONTACTER());
		
		SGText ORG_TEL = new SGText("TEL", Util.TI18N.ORG_TEL());
		
		SGText ORG_FAX = new SGText("FAX", Util.TI18N.ORG_FAX());
		
		//3
		SGText ORG_EMAIL = new SGText("EMAIL", Util.TI18N.ORG_EMAIL(),true);
		
		SGText REPORT_TIL = new SGText("REPORT_TIL", Util.TI18N.ORG_REPORT_TIL());
		
		SGText ORG_ZIP = new SGText("ZIP", Util.TI18N.ORG_ZIP());
		
		SGText WEBSITE = new SGText("WEBSITE", Util.TI18N.ORG_WEBSITE());

		
		TextAreaItem NOTES = new TextAreaItem("NOTES", Util.TI18N.ORG_NOTES());
		NOTES.setTitleOrientation(TitleOrientation.TOP);
		NOTES.setColSpan(4);
		NOTES.setWidth(FormUtil.longWidth);
		NOTES.setHeight(40);
		NOTES.setStartRow(true);

		form.setFields(ORG_TYPE, AREA_ID_NAME, ADDRESS, ORG_LEADER, CONTACTER,
				ORG_TEL, ORG_FAX, ORG_EMAIL, REPORT_TIL, ORG_ZIP, WEBSITE,
				 NOTES);
	}

	private void getConfigList() {

		tree.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record selectedRecord = event.getRecord();
				form_group.editRecord(selectedRecord);
				form_group.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				form_group.setValue("PARENT_ORG_ID", selectedRecord
						.getAttribute("PARENT_ORG_ID_NAME"));
				form_group.setValue("AREA_ID", selectedRecord
						.getAttribute("AREA_ID_NAME"));
				cache_map.put("ORG_LEVEL", selectedRecord
						.getAttribute("ORG_LEVEL"));
				cache_map.put("PARENT_ORG_ID", selectedRecord
						.getAttribute("ID"));
				cache_map.put("PARENT_ORG_ID_NAME", selectedRecord
						.getAttribute("ORG_CNAME"));

				ORG_ID = selectedRecord.getAttribute("ID");
			}
		});

		TreeGridField orgField = new TreeGridField();
		orgField.setName("ORG_CNAME");
		tree.setFields(orgField);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN, BasPrivRef.ORG);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchOrgWin(ds, 
							createSerchForm(searchForm), lst_section.getSection(0),form_group).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});

		IButton newButton = createBtn(StaticRef.CREATE_BTN, BasPrivRef.ORG_P0_01);
		newButton.addClickHandler(new NewOrgAction(form_group, cache_map,this));

		IButton saveButton = createBtn(StaticRef.SAVE_BTN, BasPrivRef.ORG_P0_02);
		saveButton.addClickHandler(new SaveOrgAction(tree, form_group,check_map,this));

		IButton delButton = createBtn(StaticRef.DELETE_BTN, BasPrivRef.ORG_P0_03);
		delButton.addClickHandler(new DeleteOrgAction(tree, form_group));

		IButton canButton = createBtn(StaticRef.CANCEL_BTN, BasPrivRef.ORG_P0_04);
		canButton.addClickHandler(new CancelMultiFormAction(tree, form_group,this));
		
		IButton syncButton = createBtn(StaticRef.CONFIRM_BTN, BasPrivRef.ORG_P0_05);
		syncButton.setTitle("同步组织");
		syncButton.addClickHandler(new SyncPrivilegeAction(tree));

        add_map.put(BasPrivRef.ORG_P0_01, newButton);
        del_map.put(BasPrivRef.ORG_P0_03, delButton);
        save_map.put(BasPrivRef.ORG_P0_02, saveButton);
        save_map.put(BasPrivRef.ORG_P0_04, canButton);
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, delButton,
				canButton,syncButton);
	}

	public void initVerify() {

		check_map.put("TABLE", "BAS_ORG");

		check_map.put("ORG_CNAME", StaticRef.CHK_NOTNULL
				+ Util.TI18N.ORG_CNAME());
		check_map.put("ORG_CNAME", StaticRef.CHK_UNIQUE
				+ Util.TI18N.ORG_CNAME());

		check_map.put("HINT_CODE", StaticRef.CHK_NOTNULL
				+ Util.TI18N.HINT_CODE());
		check_map.put("HINT_CODE", StaticRef.CHK_UNIQUE
				+ Util.TI18N.HINT_CODE());

		check_map.put("SHORT_NAME", StaticRef.CHK_NOTNULL
				+ Util.TI18N.SHORT_NAME());
		check_map.put("SHORT_NAME", StaticRef.CHK_UNIQUE
				+ Util.TI18N.SHORT_NAME());

		cache_map.put("ENABLE_FLAG", "true");
	}

	// 客户列表
	/*public void SearchCustList(SGTable table) {
		// table.addRecordClickHandler(new RecordClickHandler() {
		//			
		// @Override
		// public void onRecordClick(RecordClickEvent event) {
		// Record record = table.getSelectedRecord();
		// form_group.editRecord(record);
		// OP_FLAG = "M";
		// cust_id = event.getRecord().getAttributeAsString("ID");
		//				
		// }
		// });
		// table.setShowRowNumbers(true);

		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE",
				Util.TI18N.CUSTOMER_CODE());

		ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_CNAME",
				Util.TI18N.CUSTOMER_CNAME());

		table.setFields(CUSTOMER_CODE, CUSTOMER_CNAME);
	}*/

	// 查询窗口
	public DynamicForm createSerchForm(SGPanel form) {

		// form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(6);
		form.setPadding(5);
		/*
		 * form.setHeight100(); form.setWidth100(); form.setCellPadding(2);
		 */

		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(330);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);

		// 2行
		TextItem PARENT_ORG_ID = new TextItem("PARENT_ORG_ID");
		PARENT_ORG_ID.setVisible(false);
		
		//2015-07-23 yuanlei 
		TextItem FATHER_PARENT_ORG_ID = new TextItem("FATHER_PARENT_ORG_ID");
		FATHER_PARENT_ORG_ID.setVisible(false);
		
		SGText PARENT_ORG_ID_NAME = new SGText("PARENT_ORG_ID_NAME", Util.TI18N.PARENT_ORG_NAME());
		Util.initOrg(PARENT_ORG_ID_NAME, PARENT_ORG_ID, false, "50%", "40%",FATHER_PARENT_ORG_ID);
		
		
		PARENT_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		PARENT_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());

		TextItem AREA_ID = new TextItem("AREA_ID");
		AREA_ID.setVisible(false);
		ComboBoxItem AREA_ID_NAME = new ComboBoxItem("AREA_ID_NAME", Util.TI18N
				.AREA_ID());
		AREA_ID_NAME.setEndRow(true);
		Util.initArea(AREA_ID_NAME, AREA_ID);

		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N
				.ENABLE_FLAG(), true);
		//ENABLE_FLAG.setTitleOrientation(TitleOrientation.TOP);
		ENABLE_FLAG.setValue(true);
		
		SGCheck IS_BASORG_VIEW = new SGCheck("IS_BASORG_VIEW", "");
		IS_BASORG_VIEW.setVisible(false);
		IS_BASORG_VIEW.setValue(true);

		form.setItems(txt_global, PARENT_ORG_ID, PARENT_ORG_ID_NAME,
				ENABLE_FLAG,IS_BASORG_VIEW, FATHER_PARENT_ORG_ID);

		return form;
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}
	
	public void reInitOrg() {
		Util.initComboValue(PARENT_ORG_ID, "V_ORG", "ID", "ORG_CNAME", " "," ORG_LEVEL ASC");
	}
	
	//树形结构数据源
	/*static DataSource getDataSource()
	{
		
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL(GWT.getHostPageBaseURL()+"basQueryServlet?ds_id=BAS_AREA");
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
		DataSourceTextField nameField = new DataSourceTextField("SHORT_NAME");
		nameField.setRequired(true);

		dataSource.setFields(idField, nameField, parentIdField);
		dataSource.setClientOnly(false);

		return dataSource;
	}*/
	
	class SearchOrgWin extends SearchWin{
		public SearchOrgWin(DataSource ds,DynamicForm form, SectionStackSection p_section,ValuesManager vm) {
			super(ds, form, p_section, vm);
		}
		
		@Override
		public void doSearch(){
			if(searchForm != null){
				FormItem PARENT_ORG_ID = searchForm.getItem("PARENT_ORG_ID");
				if(PARENT_ORG_ID != null){
					if(ObjUtil.isNotNull(PARENT_ORG_ID.getValue())){
						DataSourceField field = ds.getField("PARENT_ORG_ID");
						if(field != null){
							//if("SUPER_MAN".equals(LoginCache.getLoginUser().getROLE_ID())){
							if(LoginCache.getLoginUser().getDEFAULT_ORG_ID().equals(PARENT_ORG_ID.getValue())) {
								field.setRootValue(LoginCache.getLoginUser().getDEFAULT_ORG_PARENTID());
							}else{
								FormItem FATHER_PARENT_ORG_ID = searchForm.getItem("FATHER_PARENT_ORG_ID");
								if(FATHER_PARENT_ORG_ID != null) {
									field.setRootValue(ObjUtil.ifObjNull(FATHER_PARENT_ORG_ID.getValue(),PARENT_ORG_ID.getValue()).toString());
								}
								else {
									field.setRootValue(PARENT_ORG_ID.getValue().toString());
								}
							}
						}
					}
				}
			}
			super.doSearch();
		}
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasOrgView view = new BasOrgView();
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
