package com.rd.client.view.rule;

import java.util.HashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.tms.rule.NewGroupAction;
import com.rd.client.action.tms.rule.NewPriorAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.DeleteMultiFormAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.RulPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.rule.RulGroupDS;
import com.rd.client.ds.rule.RulHeaderDS;
import com.rd.client.ds.rule.RulPriorDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 业务规则->配载规则
 * @author yuanlei
 *
 */
@ClassForNameAble
public class DispatchRuleView extends SGForm implements PanelFactory{

	private DataSource groupDS;
	private DataSource priorDS;
	private DataSource headerDS;
	private SGTable groupTable;
	private SGTable priorTable;
	private SGTable headerTable;
	private SectionStack section;
	private SectionStack bottleftsection;
	private SectionStack bottrightsection;
	private Window searchWin;
	//private Window timeWin;
	private SGPanel searchForm;
    public Record typeclickrecord;
	public Record formulaclickrecord;
	private HashMap<String,String> group_ck_map ;
	private HashMap<String,String> prior_ck_map ;
	private IButton newB;
	private IButton delB;
	private IButton savB;
	private IButton canB;
	private IButton newB2;
	private IButton delB2;
	private IButton savB2;
	private IButton canB2;
	private IButton newButton;
	private IButton delButton;
	private IButton saveButton;
	private IButton canButton;
	
	private VLayout layOut;
	public ValuesManager vm;
	private SGPanel basInfo;
	
	/*public DispatchRuleView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth("99%");
		main.setHeight100();

		group_ck_map = new HashMap<String,String>();
		prior_ck_map = new HashMap<String,String>();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		headerDS = RulHeaderDS.getInstance("V_RUL_HEADER","RUL_DISPATCH_HEADER");
		groupDS = RulGroupDS.getInstance("RUL_DISPATCH_GROUP","RUL_DISPATCH_GROUP");
		priorDS = RulPriorDS.getInstance("RUL_DISPATCH_PRIOR","RUL_DISPATCH_PRIOR");
		
		// 主布局
		HStack Stack = new HStack();// 设置详细信息布局
		Stack.setWidth100();
		Stack.setHeight("68%");
		
		headerTable=new SGTable(headerDS, "100%", "100%");
		headerTable.setCanEdit(false);
		groupTable = new SGTable(groupDS, "100%", "40%");
		priorTable = new SGTable(priorDS, "100%", "60%");
		
		headerTable.setShowFilterEditor(false);
		groupTable.setShowFilterEditor(false);
		priorTable.setShowFilterEditor(false);
		
		createHeaderFields();
		createGroupFields();
		createPriorFields();
		
		headerTable.addRecordClickHandler(new RecordClickHandler() {
				
				@Override
				public void onRecordClick(RecordClickEvent event) {
					initSaveBtn();
					
				}
			});
	        
		headerTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					enableOrDisables(add_map, false);
					enableOrDisables(del_map, false);
					enableOrDisables(save_map, true);
					
				}
			});
	        
		groupTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				delB.enable();
				newB.enable();
			}
		});
		groupTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				newB.disable();
			    savB.enable();
			    delB.disable();
			    canB.enable();	
				
			}
		});
		priorTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				delB2.enable();
				newB2.enable();
			}
		});
		priorTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				newB2.disable();
			    savB2.enable();
			    delB2.disable();
			    canB2.enable();
				
			}
		});
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		ToolStrip bottleftStrip = new ToolStrip();//按钮布局
		bottleftStrip.setAlign(Alignment.RIGHT);
		bottleftStrip.setWidth("100%");
		bottleftStrip.setHeight("20");
		bottleftStrip.setPadding(2);
		bottleftStrip.setSeparatorSize(12);
		bottleftStrip.addSeparator();
		
	    //新增按钮
	    newB = createBtn(StaticRef.CREATE_BTN,RulPrivRef.DISPATCH_P1_01);
	    newB.addClickHandler(new NewGroupAction(groupTable, this));
	    newB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(headerTable.getSelectedRecord())){
				 newB.disable();
			     savB.enable();
			     delB.disable();
			     canB.enable();	
			     newB2.disable();
				}else{
				    MSGUtil.sayWarning("无主信息，无法新增[分组方式]!");
				    return;
				}
			}
		});
        //保存按钮
        savB = createBtn(StaticRef.SAVE_BTN,RulPrivRef.DISPATCH_P1_02);
        savB.addClickHandler(new SaveAction(groupTable,group_ck_map,this,1));

        //删除按钮
        delB = createBtn(StaticRef.DELETE_BTN,RulPrivRef.DISPATCH_P1_03);
        delB.addClickHandler(new DeleteAction(groupTable));
        
        //取消按钮
        canB = createBtn(StaticRef.CANCEL_BTN,RulPrivRef.DISPATCH_P1_02);
        canB.addClickHandler(new CancelAction(groupTable,this));
        canB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
					newB.enable();
			        savB.disable();
			        delB.enable();
			        canB.disable();
			        newB2.enable();
				
			}
		});
        
        newB.enable();
        savB.disable();
        delB.disable();
        canB.disable();
        
        bottleftStrip.setMembersMargin(4);
        bottleftStrip.setMembers(newB, savB, delB, canB);
        
    	ToolStrip botrightStrip = new ToolStrip();//按钮布局
    	botrightStrip.setAlign(Alignment.RIGHT);
    	botrightStrip.setWidth("100%");
    	botrightStrip.setHeight("20");
    	botrightStrip.setPadding(2);
    	botrightStrip.setSeparatorSize(12);
    	botrightStrip.addSeparator();
		
	    //新增按钮
	    newB2 = createBtn(StaticRef.CREATE_BTN,RulPrivRef.DISPATCH_P2_01);
	    
	    newB2.addClickHandler(new NewPriorAction(priorTable, this));
		newB2.addClickHandler(new ClickHandler() {
				
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(ObjUtil.isNotNull(headerTable.getSelectedRecord())){
					 newB2.disable();
				     savB2.enable();
				     delB2.disable();
				     canB2.enable();	
				}else{
				    MSGUtil.sayWarning("无主信息，无法新增[排序规则]!");
				    priorTable.discardAllEdits();
				    return;
				}
			}
		});
		
		    
        //保存按钮
        savB2 = createBtn(StaticRef.SAVE_BTN,RulPrivRef.DISPATCH_P2_02);
        savB2.addClickHandler(new SaveAction(priorTable,prior_ck_map,this,2));
  
        //删除按钮
        delB2 = createBtn(StaticRef.DELETE_BTN,RulPrivRef.DISPATCH_P2_03);
        delB2.addClickHandler(new DeleteAction(priorTable));
        
        
        //取消按钮
        canB2 = createBtn(StaticRef.CANCEL_BTN,RulPrivRef.DISPATCH_P2_04);
        canB2.addClickHandler(new CancelAction(priorTable,this));
        canB2.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				newB2.enable();
		        savB2.disable();
		        delB2.enable();
		        canB2.disable();
			}
		});
        newB2.enable();
        savB2.disable();
        delB2.disable();
        canB2.disable();
        
        botrightStrip.setMembersMargin(4);
        botrightStrip.setMembers(newB2, savB2, delB2, canB2);
  
        
    	VLayout leftlay = new VLayout();
    	leftlay.addMember(groupTable);
    	leftlay.addMember(bottleftStrip);
    	
    	VLayout rightlay = new VLayout();
    	rightlay.addMember(priorTable);
    	rightlay.addMember(botrightStrip);
    	
    	
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight("32%");
		
		//创建Section
		section = new SectionStack();
		section.setWidth100();
		section.setHeight100();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.MAININFO());//主信息
	    listItem.setItems(headerTable);
	    listItem.setExpanded(true);
	    section.addSection(listItem);
	    stack.addMember(section);
		addSplitBar(stack,"30%");
		
		layOut = new VLayout();
		layOut.setWidth("69%");
		layOut.setHeight("100%");
		layOut.addMember(createMainInfo());
		layOut.setVisible(false);
        stack.addMember(layOut);
	    
		HStack bottomStack = new HStack();// 设置详细信息布局
		bottomStack.setWidth100();
		bottomStack.setHeight100();
		
		bottleftsection = new SectionStack();
		bottleftsection.setWidth("40%");
		bottleftsection.setHeight("100%");
		final SectionStackSection leftlistItem = new SectionStackSection("配载分组方式");//表达式
		leftlistItem.setItems(leftlay);
		leftlistItem.setExpanded(true);
		leftlistItem.setControls(new SGPage(groupTable, false).initPageBtn());

		bottleftsection.addSection(leftlistItem);
		
	    bottrightsection = new SectionStack();
	    bottrightsection.setWidth("60%");
	    bottrightsection.setHeight("100%");
		final SectionStackSection rightlistItem = new SectionStackSection("配载优先级");//过滤条件
		rightlistItem.setItems(rightlay);
		rightlistItem.setExpanded(true);
		rightlistItem.setControls(new SGPage(priorTable, false).initPageBtn());
		bottrightsection.addSection(rightlistItem);
	    
		main.setWidth100();
        main.setHeight100();
        Stack.addMember(bottomStack);
        
		bottomStack.addMember(bottleftsection);
		bottomStack.addMember(bottrightsection);
		main.addMember(toolStrip);
		main.addMember(stack);
		main.addMember(Stack);
		
		vm.addMember(basInfo);
		vm.setDataSource(headerDS);
		
		initVerify();
		
		return main;
	}
	
	private void createHeaderFields() {
		ListGridField RUL_CODE = new ListGridField("RUL_CODE", Util.TI18N.RUL_CODE(), 70);
		ListGridField RUL_DESCR = new ListGridField("RUL_DESCR",Util.TI18N.RUL_DESCR(),140);//时间类别
		ListGridField FROM_DATE = new ListGridField("FROM_DATE",Util.TI18N.FROM_DATE(),120);//生效期
		ListGridField TO_DATE = new ListGridField("TO_DATE",Util.TI18N.TO_DATE(),120);//失效期
		ListGridField LOAD_RATE = new ListGridField("LOAD_RATE",Util.TI18N.LOAD_RATE(),70);//装载率
		ListGridField WARP_RATE = new ListGridField("WARP_RATE",Util.TI18N.WARP_RATE(),70);//误差率
		ListGridField DISPATCH_RULE = new ListGridField("DISPATCH_RULE_NAME",Util.TI18N.DISPATCH_RULE(),100);//配载规则
		ListGridField RUL_MODE = new ListGridField("RUL_MODE_NAME",Util.TI18N.RUL_MODE(),80);//配载方式
		ListGridField SPLIT_MODE = new ListGridField("SPLIT_MODE",Util.TI18N.SPLIT_MODE(),100);//拆分方式
		ListGridField UDF_LIMIT1 = new ListGridField("UDF_LIMIT1",Util.TI18N.UDF_LIMIT1(),140);//限制条件1
		ListGridField UDF_LIMIT2 = new ListGridField("UDF_LIMIT2",Util.TI18N.UDF_LIMIT2(),140);//限制条件2
		ListGridField UDF_LIMIT3 = new ListGridField("UDF_LIMIT3",Util.TI18N.UDF_LIMIT3(),140);//限制条件3
		ListGridField UDF_LIMIT4 = new ListGridField("UDF_LIMIT4",Util.TI18N.UDF_LIMIT4(),140);//限制条件4
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),50);//
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		headerTable.setFields(RUL_CODE,RUL_DESCR,FROM_DATE,TO_DATE,LOAD_RATE,WARP_RATE,DISPATCH_RULE,RUL_MODE,SPLIT_MODE,UDF_LIMIT1,UDF_LIMIT2,UDF_LIMIT3,UDF_LIMIT4,ENABLE_FLAG);
		
		headerTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				typeclickrecord = event.getRecord();
				headerTable.OP_FLAG = "M";

				findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", "M");
				findValues.addCriteria("RUL_ID", event.getRecord().getAttributeAsString("ID"));
				
				groupTable.fetchData(findValues);
				priorTable.fetchData(findValues);
				
				if(ObjUtil.isNotNull(typeclickrecord)){
            		vm.editRecord(typeclickrecord);
            	}
			}
		});
	}
	
	private void createGroupFields() {
	
		final ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),80);//表达式
		SHOW_SEQ.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHOW_SEQ()));
		
		final ListGridField GROUP_CONDITION = new ListGridField("GROUP_CONDITION",ColorUtil.getRedTitle(Util.TI18N.GROUP_CONDITION()),120);//时间单位
		Util.initComboValue(GROUP_CONDITION, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'DISPATCH_FIELD'", " SHOW_SEQ ASC");

		groupTable.setFields(SHOW_SEQ,GROUP_CONDITION);	
	}	
	private void createPriorFields() {
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),80);//字段值--字段
		SHOW_SEQ.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHOW_SEQ()));
		ListGridField CONDITION = new ListGridField("CONDITION",Util.TI18N.CONDITION(),120);//操作符--运算符
		Util.initComboValue(CONDITION, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'DISPATCH_FIELD'", " SHOW_SEQ ASC");
		CONDITION.setTitle(ColorUtil.getRedTitle(Util.TI18N.CONDITION()));
		
		ListGridField SORT_BY = new ListGridField("SORT_BY",Util.TI18N.SORT_BY(),80);//条件值
		Util.initComboValue(SORT_BY, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'SORT_BY'", " SHOW_SEQ ASC");
		SORT_BY.setTitle(ColorUtil.getRedTitle(Util.TI18N.SORT_BY()));
		priorTable.setFields(SHOW_SEQ,CONDITION,SORT_BY);
		
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) { //主信息控制按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(headerDS, createSerchForm(searchForm), section.getSection(0), vm).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		    
	    //新增按钮
	    newButton = createBtn(StaticRef.CREATE_BTN,RulPrivRef.DISPATCH_P0_01);
	    newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));
	    newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				newB.disable();
				newB2.disable();
			}
		});
	    
	    //保存按钮
	    saveButton = createBtn(StaticRef.SAVE_BTN,RulPrivRef.DISPATCH_P0_02);
	    saveButton.addClickHandler(new SaveMultiFormAction(headerTable, vm, check_map,this));
	    saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				newB.enable();
				newB2.enable();
			}
		});
	    
	    //删除按钮
	    delButton = createBtn(StaticRef.DELETE_BTN,RulPrivRef.DISPATCH_P0_03);
	//	        delForlaButton.addClickHandler(new DeleteAction(typeTable,this));
	    delButton.addClickHandler(new DeleteMultiFormAction(headerTable, vm));
	    
	    //取消按钮
	    canButton = createBtn(StaticRef.CANCEL_BTN,RulPrivRef.DISPATCH_P0_04);
	    canButton.addClickHandler(new CancelMultiFormAction(headerTable,vm,this));
	    canButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				newB.enable();
				newB2.enable();
			}
		});
	    
	    add_map.put(RulPrivRef.DISPATCH_P0_01, newButton);
	    del_map.put(RulPrivRef.DISPATCH_P0_03, delButton);
	    save_map.put(RulPrivRef.DISPATCH_P0_02, saveButton);
	    save_map.put(RulPrivRef.DISPATCH_P0_04, canButton);
	    this.enableOrDisables(add_map, true);
	    enableOrDisables(del_map, false);
	    this.enableOrDisables(save_map, false);
	  
	    toolStrip.setMembersMargin(5);
	    toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	        
	}

	protected DynamicForm createSerchForm(SGPanel form) {
		form.setDataSource(groupDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(6);
		txt_global.setEndRow(false);
		
		SGCombo RUL_CODE=new SGCombo("RUL_CODE", Util.TI18N.RUL_CODE(),true);
		SGDateTime FROM_DATE = new SGDateTime("FROM_DATE", Util.TI18N.FROM_DATE());  
		SGDateTime TO_DATE = new SGDateTime("TO_DATE", Util.TI18N.TO_DATE());
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(), true);	
		chk_enable.setValue(true);
		form.setItems(txt_global,RUL_CODE,FROM_DATE,TO_DATE,chk_enable);
		return form;
	}
	
	private SectionStack createMainInfo() {

		/**
		 * 基本信息
		 * 
		 */
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		SGText RUL_CODE=new SGText("RUL_CODE", Util.TI18N.RUL_CODE());
		SGText RUL_DESCR = new SGText("RUL_DESCR",Util.TI18N.RUL_DESCR());
		
		final SGDateTime FROM_DATE = new SGDateTime("FROM_DATE", Util.TI18N.FROM_DATE());
		FROM_DATE.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_START_DATE()));
		
		final SGDateTime TO_DATE = new SGDateTime("TO_DATE", Util.TI18N.TO_DATE());
		TO_DATE.setTitle(ColorUtil.getRedTitle(Util.TI18N.TO_DATE()));
		
		SGText LOAD_RATE = new SGText("LOAD_RATE", "要求装载率(%)",true);
		
		SGText WARP_RATE = new SGText("WARP_RATE", "浮动范围(%)");
		
		SGCombo DISPATCH_RULE = new SGCombo("DISPATCH_RULE", Util.TI18N.DISPATCH_RULE());
		Util.initCodesComboValue(DISPATCH_RULE, "DISPATCH_RULE");
		
		SGCombo DISPATCH_FASHION = new SGCombo("RUL_MODE", Util.TI18N.RUL_MODE());
		Util.initCodesComboValue(DISPATCH_FASHION, "DISPATCH_FASHION");
		
		SGCombo SPLIT_MODE = new SGCombo("SPLIT_MODE", Util.TI18N.SPLIT_MODE(),true);
		Util.initCodesComboValue(SPLIT_MODE, "SPLIT_MODE");
		
		SGCheck UNION_FLAG = new SGCheck("UNION_FLAG", "是否自动拆分",true);	
		UNION_FLAG.setValue(false);//激活
		UNION_FLAG.setColSpan(2);
		
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());	
		ENABLE_FLAG.setValue(false);//激活
		ENABLE_FLAG.setColSpan(2);
		
		SGCombo UDF_LIMIT1 = new SGCombo("UDF_LIMIT1", Util.TI18N.UDF_LIMIT1(),true);
		Util.initCodesComboValue(UDF_LIMIT1, "DISPATCH_LIMIT");
		
		SGCombo UDF_LIMIT2 = new SGCombo("UDF_LIMIT2", Util.TI18N.UDF_LIMIT2());
		Util.initCodesComboValue(UDF_LIMIT2, "DISPATCH_LIMIT");
		
		SGCombo UDF_LIMIT3 = new SGCombo("UDF_LIMIT3", Util.TI18N.UDF_LIMIT3());
		Util.initCodesComboValue(UDF_LIMIT3, "DISPATCH_LIMIT");
		
		SGCombo UDF_LIMIT4 = new SGCombo("UDF_LIMIT4", Util.TI18N.UDF_LIMIT4());
		Util.initCodesComboValue(UDF_LIMIT4, "DISPATCH_LIMIT");
		
		
		basInfo = new SGPanel();
		basInfo.setTitleWidth(75);
		basInfo.setItems(RUL_CODE,RUL_DESCR,FROM_DATE,TO_DATE,LOAD_RATE,WARP_RATE,DISPATCH_RULE,DISPATCH_FASHION,
				UNION_FLAG,ENABLE_FLAG,UDF_LIMIT1,UDF_LIMIT2,UDF_LIMIT3,UDF_LIMIT4);
		
		
		SectionStack section = new SectionStack();
		section.setVisible(true);
		section.setBackgroundColor(ColorUtil.BG_COLOR);

		// 1，基本信息
		SectionStackSection basicInfo = new SectionStackSection(Util.TI18N.BASE_MESSAGE());
		basInfo.setHeight("100%");
		basicInfo.addItem(basInfo);
		basicInfo.setExpanded(true);
		section.addSection(basicInfo);

		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);

		return section;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "RUL_DISPATCH_HEADER");		
		check_map.put("FROM_DATE", StaticRef.CHK_NOTNULL + Util.TI18N.FROM_DATE());
		check_map.put("TO_DATE", StaticRef.CHK_NOTNULL + Util.TI18N.TO_DATE());	
		check_map.put("FROM_DATE", StaticRef.CHK_DATE + Util.TI18N.FROM_DATE());
		check_map.put("TO_DATE", StaticRef.CHK_DATE + Util.TI18N.TO_DATE());	
		
		group_ck_map.put("TABLE", "RUL_DISPATCH_GROUP");
		group_ck_map.put("SHOW_SEQ", StaticRef.CHK_NOTNULL + Util.TI18N.SHOW_SEQ());
		group_ck_map.put("GROUP_CONDITION", StaticRef.CHK_NOTNULL + Util.TI18N.GROUP_CONDITION());
	
		prior_ck_map.put("TABLE", "RUL_DISPATCH_PRIOR");
		prior_ck_map.put("SHOW_SEQ", StaticRef.CHK_NOTNULL + Util.TI18N.SHOW_SEQ());
		prior_ck_map.put("SORT_BY", StaticRef.CHK_NOTNULL + Util.TI18N.SORT_BY());
		prior_ck_map.put("CONDITION", StaticRef.CHK_NOTNULL + Util.TI18N.CONDITION());	
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}

	}
	
	@Override
	public void initBtn(int initBtn) {
		if(initBtn == 1){
			newB.enable();
		    savB.disable();
		    delB.enable();
		    canB.disable();	
		    newB2.enable();
		}else{
			newB2.enable();
		    savB2.disable();
		    delB2.enable();
		    canB2.disable();	
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		DispatchRuleView view = new DispatchRuleView();
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
