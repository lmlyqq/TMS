package com.rd.client.view.base;

import java.util.HashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.base.ware.NewWareAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.DeleteMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.DockDS;
import com.rd.client.ds.base.WareHouseDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
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
 * 基础资料--仓库管理
 * @author fanglm
 *
 */
@ClassForNameAble
public class BasWareHouseView extends SGForm implements PanelFactory{
	
	 private DataSource ds;
     private SGTable table;
     private Window searchWin;
	 private SGPanel searchForm;
	// private static ButtonItem searchItem = null;
	 private SectionStack section;
	 private SGPanel mainForm;
	// private SGPanel mainForm2;
	 public ValuesManager vm;
	 private SectionStack list_section;
	 private TabSet rightTabSet;
	 public SGTable dockTable;
	 private DataSource dockDS;
	 public Record clickrecord;
	 public Criteria criteria;
	 private HashMap<String,String> detail_ck_map ;
	 
	/*public BasWareHouseView(String id) {
		super(id);
	}*/
	 
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		detail_ck_map = new HashMap<String,String>();
		
	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = WareHouseDS.getInstance("V_WAREHOUSE");
	    dockDS=DockDS.getInstance("BAS_WAREHOUSE_DOCK","BAS_WAREHOUSE_DOCK");
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%",true,true,false);
		createListFields(table);
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
//	    listItem.setControls(addMaxBtn(list_section, stack, "200",true), new SGPage(table, true).initPageBtn());
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.addSection(listItem);
	   
	    list_section.setWidth("100%");
//	    searchItem =  new ButtonItem(Util.BI18N.SEARCH());
//	    if(searchItem != null) {
//	    	new PageUtil(listItem, table, searchItem,false);
//	    }
		stack.addMember(list_section);
		addSplitBar(stack);

		// STACK的右边布局
		rightTabSet = new TabSet();
		rightTabSet.setWidth("80%");
		rightTabSet.setHeight("100%");
		rightTabSet.setMargin(0);
		
//		 Tab tab1 = new Tab(Util.TI18N.MAININFO());
//		 tab1.setPane(createMainInfo());
//		 rightTabSet.addTab(tab1);
		createMainInfo();
		rightTabSet.setVisible(false);
		
		//STACK的右边
		stack.addMember(rightTabSet);
		
		vm.addMember(mainForm);
		//vm.addMember(mainForm2);
		vm.setDataSource(ds);
		//创建按钮布局
		
//		stack.addMember(rightTabSet);
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		
		initVerify();
//		addDoubeclick(table, listItem, rightTabSet, list_section);
		
//		table.addDoubleClickHandler(new SGDoubleClickAction(this));
		
		table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				
				enableOrDisables(add_map, false);//fanglm 2010-12-10
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			}
			
		});
		  
		return main;
	}
	
	private void createListFields(final SGTable table){
//		final SGTable table2 = table;
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event){
            	OP_FLAG = "M";
                vm.editRecord(event.getRecord());

                initSaveBtn();
                
                clickrecord = event.getRecord();
                
//              table.OP_FLAG = "M";
//				criteria = new Criteria();
//				criteria.addCriteria("OP_FLAG", "M");
//              criteria.addCriteria("WHSE_ID", event.getRecord().getAttributeAsString("WHSE_CODE"));
//              dockTable.fetchData(criteria);
//              dockTable.setFilterEditorCriteria(criteria);
                
                findValues = new Criteria();
	            findValues.addCriteria("WHSE_ID", clickrecord.getAttribute("ID"));
	            findValues.addCriteria("OP_FLAG", dockTable.OP_FLAG);
	            dockTable.PKEY = "ID";
	            dockTable.PVALUE = clickrecord.getAttribute("ID");
	            dockTable.fetchData(findValues);
//	            dockTable.setFilterEditorCriteria(findValues);
			}
		});
		 table.setShowRowNumbers(true);
		ListGridField WHSE_CODE = new ListGridField("WHSE_CODE",Util.TI18N.WHSE_CODE(),78);
//		WHSE_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.WHSE_CODE()));
		ListGridField WHSE_NAME = new ListGridField("WHSE_NAME",Util.TI18N.WHSE_NAME(),140);
//		WHSE_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.WHSE_NAME()));
		ListGridField WHSE_ENAME = new ListGridField("WHSE_ENAME",Util.TI18N.WHSE_ENAME(),140);
		
        
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME",Util.TI18N.SHORT_NAME(),78);
//		SHORT_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
		ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),78);
//		HINT_CODE.setTitle(ColorUtil.getBlueTitle(Util.TI18N.HINT_CODE()));
	    ListGridField WHSE_ATTR = new ListGridField("WHSE_ATTR_NAME",Util.TI18N.WHSE_ATTR(),78);
		ListGridField WHSE_TYP = new ListGridField("WHSE_TYP_NAME",Util.TI18N.WHSE_TYP(),78);
		
		
		ListGridField WHSE_CLS = new ListGridField("WHSE_CLS_NAME",Util.TI18N.WHSE_CLS(),78);
		ListGridField WHSE_TMP = new ListGridField("WHSE_TMP_NAME",Util.TI18N.WHSE_TMP(),78);
		
		ListGridField ORG_NAME = new ListGridField("ORG_ID_NAME",Util.TI18N.ORG_NAME(),78);
		ListGridField AREA_ID_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME(),78);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),78);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		ListGridField START_TIME = new ListGridField("START_TIME",Util.TI18N.START_TIME(),78);
		START_TIME.setType(ListGridFieldType.DATE);
		ListGridField END_TIME = new ListGridField("END_TIME",Util.TI18N.END_TIME(),78);
		END_TIME.setType(ListGridFieldType.DATE);
		
		ListGridField WHSE_ADDRESS = new ListGridField("ADDRESS",Util.TI18N.WHSE_ADDRESS(),140);
		ListGridField ZIP = new ListGridField("ZIP",Util.TI18N.ZIP(),80);
		ListGridField WHSE_BOSS = new ListGridField("WHSE_BOSS",Util.TI18N.WHSE_BOSS(),80);
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),80);
		ListGridField WHSE_MANAGER = new ListGridField("WHSE_MANAGER",Util.TI18N.WHSE_MANAGER(),80);
		ListGridField CONT_TEL = new ListGridField("CONT_TEL",Util.TI18N.CONT_TEL(),80);
		ListGridField CONT_FAX = new ListGridField("CONT_FAX",Util.TI18N.CONT_FAX(),80);
		ListGridField CONT_EMAIL = new ListGridField("CONT_EMAIL",Util.TI18N.CONT_EMAIL(),120);
		
		table.setFields(WHSE_CODE,WHSE_NAME,WHSE_ENAME,SHORT_NAME,HINT_CODE,WHSE_ATTR,WHSE_TYP
				        ,WHSE_CLS,WHSE_TMP,ORG_NAME,AREA_ID_NAME,ENABLE_FLAG,START_TIME,END_TIME
				        ,WHSE_ADDRESS,ZIP,WHSE_BOSS,SHOW_SEQ,WHSE_MANAGER,CONT_TEL,CONT_FAX,CONT_EMAIL);
		
//		table =table2;
		
		
	}
	
	//数据新增初始化与保存校验
	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_WAREHOUSE");
		check_map.put("WHSE_CODE",StaticRef.CHK_NOTNULL+Util.TI18N.WHSE_CODE());
		check_map.put("WHSE_CODE",StaticRef.CHK_UNIQUE+Util.TI18N.WHSE_CODE());
		check_map.put("WHSE_NAME",StaticRef.CHK_NOTNULL+Util.TI18N.WHSE_NAME());
		check_map.put("AREA_ID",StaticRef.CHK_NOTNULL+Util.TI18N.AREA_ID_NAME());
		check_map.put("ORG_ID",StaticRef.CHK_NOTNULL+Util.TI18N.ORG_ID_NAME());
//		check_map.put("HINT_CODE",StaticRef.CHK_UNIQUE+Util.TI18N.HINT_CODE());
//		check_map.put("HINT_CODE",StaticRef.CHK_NOTNULL+Util.TI18N.HINT_CODE());
		check_map.put("START_TIME", StaticRef.CHK_DATE+Util.TI18N.START_TIME());
		check_map.put("END_TIME", StaticRef.CHK_DATE+Util.TI18N.END_TIME());
		
		cache_map.put("ENABLE_FLAG", "true");

		detail_ck_map.put("TABLE", "BAS_WAREHOUSE_DOCK");
		detail_ck_map.put("WHSE_ID,DOCK",StaticRef.CHK_UNIQUE+"DOCK");
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.WHSE);
        searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds, 
							createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
        		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.WHSE_P0_01);
        newButton.addClickHandler(new NewMultiFormAction(vm, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.WHSE_P0_02);
        saveButton.addClickHandler(new SaveMultiFormAction(table,vm,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.WHSE_P0_03);
        delButton.addClickHandler(new DeleteMultiFormAction(table,vm));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.WHSE_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(table,vm,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.WHSE_P0_05);
        expButton.addClickHandler(new ExportAction(table,"addtime desc"));
        
        add_map.put(BasPrivRef.WHSE_P0_01, newButton);
        del_map.put(BasPrivRef.WHSE_P0_03, delButton);
        save_map.put(BasPrivRef.WHSE_P0_02, saveButton);
        save_map.put(BasPrivRef.WHSE_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
    
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton);
	}
	//创建主信息页签
	private SectionStack createMainInfo(){
		SGText WHSE_CODE = new SGText("WHSE_CODE", ColorUtil.getRedTitle(Util.TI18N.WHSE_CODE()),true);
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		
		final SGLText WHSE_NAME = new SGLText("WHSE_NAME", ColorUtil.getRedTitle(Util.TI18N.WHSE_NAME()));
		//WHSE_NAME.setWidth(250);
		final SGLText WHSE_ENAME = new SGLText("WHSE_ENAME",Util.TI18N.WHSE_ENAME(),true);
		//WHSE_ENAME.setWidth(250);
		
		SGLText ADDRESS = new SGLText("ADDRESS", Util.TI18N.WHSE_ADDRESS());
		//ADDRESS.setWidth(379);
		
		final SGText SHORT_NAME = new SGText("SHORT_NAME", ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()),true);
		SGText HINT_CODE = new SGText("HINT_CODE", Util.TI18N.HINT_CODE());
		
		WHSE_NAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(WHSE_NAME.getValue() != null){
					WHSE_ENAME.setValue(WHSE_NAME.getValue().toString());
					SHORT_NAME.setValue(WHSE_NAME.getValue().toString());
				}
			}
		});
		SHORT_NAME.addBlurHandler(new GetHintAction(SHORT_NAME, HINT_CODE));
		
		SGCombo WHSE_ATTR = new SGCombo("WHSE_ATTR", Util.TI18N.WHSE_ATTR(),true);
		SGCombo WHSE_TYP = new SGCombo("WHSE_TYP", Util.TI18N.WHSE_TYP());
		Util.initCodesComboValue(WHSE_ATTR,"WHSE_ATTR");
		Util.initCodesComboValue(WHSE_TYP,"WHSE_TYP");
		
		SGCombo WHSE_CLS = new SGCombo("WHSE_CLS", Util.TI18N.WHSE_CLS());
		SGCombo WHSE_TMP = new SGCombo("WHSE_TMP", Util.TI18N.WHSE_TMP());
		Util.initCodesComboValue(WHSE_CLS,"WHSE_CLS");
		Util.initCodesComboValue(WHSE_TMP,"WHSE_TMP");
		
		final TextItem ORG_ID = new TextItem("ORG_ID");
		final TextItem AREA_ID = new TextItem("AREA_ID");
		ORG_ID.setVisible(false);
		AREA_ID.setVisible(false);
		final SGText ORG_NAME = new SGText("ORG_ID_NAME",ColorUtil.getRedTitle(Util.TI18N.ORG_NAME()));
		ComboBoxItem AREA_ID_NAME = new ComboBoxItem("AREA_ID_NAME",ColorUtil.getRedTitle(Util.TI18N.AREA_ID_NAME()));
		AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
	
//      Util.initComboValue(ORG_NAME,ORG_ID,false,"50%","40%");
        Util.initArea(AREA_ID_NAME, AREA_ID);
    	AREA_ID_NAME.setColSpan(2);
		AREA_ID_NAME.setWidth(FormUtil.Width);
        Util.initOrg(ORG_NAME, ORG_ID, false, "50%", "40%");
		
		SGDate START_TIME = new SGDate("START_TIME", Util.TI18N.START_TIME(),true);
		SGDate END_TIME = new SGDate("END_TIME", Util.TI18N.END_TIME());
		
		
		
		
		SGText WHSE_BOSS = new SGText("WHSE_BOSS", Util.TI18N.WHSE_BOSS());
//		WHSE_BOSS.setColSpan(1);
		SGText WHSE_MANAGER = new SGText("WHSE_MANAGER", Util.TI18N.WHSE_MANAGER());
		SGText CONT_TEL = new SGText("CONT_TEL", Util.TI18N.CONT_TEL(),true);
		SGText CONT_FAX = new SGText("CONT_FAX", Util.TI18N.CONT_FAX());
		//CONT_FAX.setColSpan(2);
		SGLText CONT_EMAIL = new SGLText("CONT_EMAIL", Util.TI18N.CONT_EMAIL());
		//CONT_EMAIL.setWidth(249);
		
		mainForm = new SGPanel();
		mainForm.setWidth("37%");
		mainForm.setItems(WHSE_CODE,WHSE_NAME,ENABLE_FLAG,WHSE_ENAME,ADDRESS,SHORT_NAME,HINT_CODE,ORG_NAME,AREA_ID_NAME,WHSE_ATTR,WHSE_TYP,WHSE_CLS,WHSE_TMP
					,START_TIME,END_TIME,ORG_ID,AREA_ID,WHSE_BOSS,WHSE_MANAGER,CONT_TEL,CONT_FAX,CONT_EMAIL);
		
		//mainForm2 = new SGPanel();
		//mainForm2.setItems(,,CONT_TEL,CONT_FAX,CONT_EMAIL);
		
		section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		section.setWidth("100%");
		
		
		SectionStackSection mainS = new SectionStackSection("基础信息");
		mainS.addItem(mainForm);
		mainS.setExpanded(true); 
//		section.addSection(mainS);
		
		//SectionStackSection mainS2 = new SectionStackSection("其他信息");
		//mainS2.addItem(mainForm2);
		//mainS2.setExpanded(true); 
//		section.addSection(mainS2);
		
		section.setSections(mainS);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(true);

    	createDockInfo();
		
		
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		
	    //新增按钮
	    IButton newB = createBtn(StaticRef.CREATE_BTN,BasPrivRef.WHSE_P1_01);
	    newB.addClickHandler(new NewWareAction(dockTable,this));
	    
        //保存按钮
        IButton savB = createBtn(StaticRef.SAVE_BTN,BasPrivRef.WHSE_P1_02);
     // savB.addClickHandler(new SaveWareAction(dockTable,this));
        savB.addClickHandler(new SaveAction(dockTable,detail_ck_map,this));
     // savB.addClickHandler(new SaveAction(table,dockTable,detail_ck_map));
        //删除按钮
        IButton delB = createBtn(StaticRef.DELETE_BTN,BasPrivRef.WHSE_P1_03);
        delB.addClickHandler(new DeleteAction(dockTable));
        
        //取消按钮
        IButton canB = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.WHSE_P1_04);
        canB.addClickHandler(new CancelAction(dockTable,this));
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(newB, savB, delB, canB);
  
        
    	VLayout lay2 = new VLayout();
//    	lay2.setWidth("80%");
    	lay2.addMember(dockTable);
    	lay2.addMember(toolStrip);
    	
    	if(isPrivilege(BasPrivRef.WHSE_P0)) {
    		Tab tab1 = new Tab("仓库设置");
    		tab1.setPane(section);
    		rightTabSet.addTab(tab1);
    	}
    	if(isPrivilege(BasPrivRef.WHSE_P1)) {
    		Tab tab2 = new Tab("DOCK设置");
    		tab2.setPane(lay2);
    		rightTabSet.addTab(tab2);
    	}
        return section;
	}

	private void createDockInfo() {

		dockTable = new SGTable(dockDS);
		dockTable.setHeight("80%");
		dockTable.setWidth("100%");
		dockTable.setShowFilterEditor(false);
		dockTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		dockTable.setShowRowNumbers(false);
//		dockTable.setShowGridSummary(true);
		dockTable.setCanEdit(true);
		
//		ListGridField WHSE_ID = new ListGridField("WHSE_ID",Util.TI18N.WAREHOUSE_FLAG(),140);
		ListGridField DOCK = new ListGridField("DOCK","DOCK",80);
		DOCK.setTitle(ColorUtil.getRedTitle("DOCK"));
		ListGridField DOCK_TYP = new ListGridField("DOCK_TYP", Util.TI18N.DOCK_TYP(), 80);
        Util.initCodesComboValue(DOCK_TYP, "DOCK_TYP");
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ", Util.TI18N.SHOW_SEQ(), 60);
		
		dockTable.setFields(DOCK,DOCK_TYP, SHOW_SEQ);
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}
	//查询二级窗口
	public DynamicForm createSerchForm(DynamicForm form) {
		
		//模糊查询
		TextItem txt_global = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY()); 
		txt_global.setTitleOrientation(TitleOrientation.LEFT);
		txt_global.setWidth(320);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		
//		SGCombo ORG_ID = new SGCombo("ORG_ID", Util.TI18N.ORG_ID_NAME(),true);
//		Util.initOrg(ORG_ID, "", "");
		
		TextItem  ORG_ID= new TextItem("ORG_ID");
		ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, ORG_ID, false, "50%", "50%");
		ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setWidth(120);
		
		ComboBoxItem AREA_ID = new ComboBoxItem("AREA_ID_NAME", Util.TI18N.AREA_ID_NAME());
		Util.initArea(AREA_ID, null);
		AREA_ID.setTitleOrientation(TitleOrientation.TOP);
		
		SGDate START_TIME = new SGDate("START_TIME",Util.TI18N.START_TIME(),true);
		SGDate END_TIME = new SGDate("END_TIME",Util.TI18N.END_TIME());
		
		//激活
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),true);
		ENABLE_FLAG.setValue(true);
		ENABLE_FLAG.setColSpan(2);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		form.setItems(txt_global,ORG_ID,EXEC_ORG_ID_NAME,AREA_ID,START_TIME,END_TIME,C_ORG_FLAG,ENABLE_FLAG);
		return form;
	}
	

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		//	searchItem = null;
		}
//		table.destroy();
//		mainForm.destroy();
//		mainForm2.destroy();
//		vm.destroy();

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasWareHouseView view = new BasWareHouseView();
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
