package com.rd.client.view.base;

import java.util.HashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.base.time.NewConditionAction;
import com.rd.client.action.base.time.NewformulaAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.ConditionDS;
import com.rd.client.ds.base.FormulaDS;
import com.rd.client.ds.tms.TimeTypeDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.TimeWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
 * 基础资料->时间管理
 * @author yuanlei
 *
 */
@ClassForNameAble
public class BasTimeMangeView extends SGForm implements PanelFactory {

	private DataSource formulaDS;
	private DataSource conditionDS;
	private DataSource typeDS;
	private SGTable formulaTable;
	private SGTable conditionTable;
	private SGTable typeTable;
	private SectionStack section;
	private SectionStack bottleftsection;
	private SectionStack bottrightsection;
	private Window searchWin;
	private Window timeWin;
	private SGPanel searchForm;
	public Record typeclickrecord;
	public Record formulaclickrecord;
	private HashMap<String,String> formula_ck_map ;
	private HashMap<String,String> condition_ck_map ;
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
	
	/*public BasTimeMangeView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

		formula_ck_map = new HashMap<String,String>();
		condition_ck_map = new HashMap<String,String>();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		typeDS=TimeTypeDS.getInstance("V_TIME_TYPE","BAS_TIME_TYPE");
		formulaDS = FormulaDS.getInstance("V_TIME_FORMULA","BAS_TIME_FORMULA");
		conditionDS=ConditionDS.getInstance("V_TIME_CONDITION","BAS_TIME_CONDITION");
		
		// 主布局
		HStack Stack = new HStack();// 设置详细信息布局
		Stack.setWidth100();
		Stack.setHeight("68%");
		
		typeTable=new SGTable(typeDS, "100%", "100%");
		formulaTable = new SGTable(formulaDS, "100%", "60%");
		conditionTable = new SGTable(conditionDS, "100%", "40%");
		
		typeTable.setShowFilterEditor(false);
		formulaTable.setShowFilterEditor(false);
		conditionTable.setShowFilterEditor(false);
		
		createTypeFields(typeTable);
		createForlaFields(formulaTable);
		createCondFields(conditionTable);
		
		typeTable.addRecordClickHandler(new RecordClickHandler() {
				
				@Override
				public void onRecordClick(RecordClickEvent event) {
//					enableOrDisables(del_map, true);
					initSaveBtn();
					
				}
			});
	        
		typeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					enableOrDisables(add_map, false);
					enableOrDisables(del_map, false);
					enableOrDisables(save_map, true);
					
				}
			});
	        
		formulaTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				delB.enable();
				newB.enable();
			}
		});
		formulaTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				newB.disable();
			    savB.enable();
			    delB.disable();
			    canB.enable();	
				
			}
		});
		conditionTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				delB2.enable();
				newB2.enable();
			}
		});
		conditionTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
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
	    newB = createBtn(StaticRef.CREATE_BTN,BasPrivRef.TIME_P1_01);
	    newB.addClickHandler(new NewformulaAction(formulaTable,this));
	    newB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(ObjUtil.isNotNull(typeTable.getSelectedRecord())){
				 newB.disable();
			     savB.enable();
			     delB.disable();
			     canB.enable();	
			     newB2.disable();
				}else{
				    MSGUtil.sayWarning("[主信息]没有数据，[表达式]不能新增!");
				    return;
				}
			}
		});
        //保存按钮
        savB = createBtn(StaticRef.SAVE_BTN,BasPrivRef.TIME_P1_02);
        savB.addClickHandler(new SaveAction(formulaTable,formula_ck_map,this,1));

        //删除按钮
        delB = createBtn(StaticRef.DELETE_BTN,BasPrivRef.TIME_P1_03);
//        delB.addClickHandler(new DeleteAction(formulaTable,conditionTable,"FORMULA_ID"));
        delB.addClickHandler(new DeleteProAction(formulaTable,conditionTable));
//        delB.addClickHandler(new DeleteAction(formulaTable));
//        delB.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				conditionTable.setData(new RecordList());
//			}
//		});
        
        //取消按钮
        canB = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.TIME_P1_02);
        canB.addClickHandler(new CancelAction(formulaTable,this));
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
	    newB2 = createBtn(StaticRef.CREATE_BTN,BasPrivRef.TIME_P2_01);
	    
	    newB2.addClickHandler(new NewConditionAction(conditionTable,this));
		    newB2.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					if(ObjUtil.isNotNull(formulaTable.getSelectedRecord())){
						 newB2.disable();
					     savB2.enable();
					     delB2.disable();
					     canB2.enable();	
					}else{
					    MSGUtil.sayWarning("[表达式]没有数据，[过滤条件]不能新增!");
					    conditionTable.discardAllEdits();
					    return;
					}
				}
			});
		
		    
        //保存按钮
        savB2 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.TIME_P2_02);
        savB2.addClickHandler(new SaveAction(conditionTable,condition_ck_map,this,2));
  
        //删除按钮
        delB2 = createBtn(StaticRef.DELETE_BTN,BasPrivRef.TIME_P2_03);
        delB2.addClickHandler(new DeleteAction(conditionTable));
        
        
        //取消按钮
        canB2 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.TIME_P2_04);
        canB2.addClickHandler(new CancelAction(conditionTable,this));
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
    	leftlay.addMember(formulaTable);
    	leftlay.addMember(bottleftStrip);
    	
    	VLayout rightlay = new VLayout();
    	rightlay.addMember(conditionTable);
    	rightlay.addMember(botrightStrip);
    	
		//创建Section
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("32%");
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.MAININFO());//主信息
	    listItem.setItems(typeTable);
	    listItem.setExpanded(true);
	    section.addSection(listItem);
		
	    
		HStack bottomStack = new HStack();// 设置详细信息布局
		bottomStack.setWidth100();
		bottomStack.setHeight100();
		
		bottleftsection = new SectionStack();
		bottleftsection.setWidth("60%");
		bottleftsection.setHeight("100%");
		final SectionStackSection leftlistItem = new SectionStackSection(Util.TI18N.FORMULA());//表达式
//		leftlistItem.setItems(formulaTable);
		leftlistItem.setItems(leftlay);
		leftlistItem.setExpanded(true);
//		leftlistItem.setControls(addMaxBtn(bottleftsection, bottomStack, "60%"), new SGPage(formulaTable, false).initPageBtn());
		leftlistItem.setControls(new SGPage(formulaTable, false).initPageBtn());

		bottleftsection.addSection(leftlistItem);
		
	    bottrightsection = new SectionStack();
	    bottrightsection.setWidth("40%");
	    bottrightsection.setHeight("100%");
		final SectionStackSection rightlistItem = new SectionStackSection(Util.TI18N.SELECT_BY());//过滤条件
//		rightlistItem.setItems(conditionTable);
		rightlistItem.setItems(rightlay);
		rightlistItem.setExpanded(true);
//		rightlistItem.setControls(addMaxBtn(bottomStack, "40%"), new SGPage(conditionTable, false).initPageBtn());
		rightlistItem.setControls(new SGPage(conditionTable, false).initPageBtn());
		bottrightsection.addSection(rightlistItem);
	    
		main.setWidth100();
        main.setHeight100();
        Stack.addMember(bottomStack);
		bottomStack.addMember(bottleftsection);
//		bottomStack.addMember(toolStrip);
		bottomStack.addMember(bottrightsection);
		main.addMember(toolStrip);
		main.addMember(section);
		main.addMember(Stack);
		
		initVerify();
		
		return main;
	}
	
	private void createTypeFields(final SGTable typeTable) {
		ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID", Util.TI18N.CUSTOMER_NAME(), 140);
		CUSTOMER_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_ID()));
		Util.initComboValue(CUSTOMER_ID, "BAS_CUSTOMER","ID","SHORT_NAME"," where enable_flag='Y'", "");
		ListGridField TIME_TYPE = new ListGridField("TIME_TYPE",Util.TI18N.TIME_TYPE(),120);//时间类别
		Util.initComboValue(TIME_TYPE, "BAS_CODES","CODE", "NAME_C", " where PROP_CODE = 'TIME_TYP'", " SHOW_SEQ ASC");
		TIME_TYPE.setTitle(ColorUtil.getRedTitle(Util.TI18N.TIME_TYPE()));
		ListGridField DOC_TYPE = new ListGridField("DOC_TYPE",Util.TI18N.DOC_TYPE(),70);//单据类型
		Util.initCodesComboValue(DOC_TYPE, "DOC_TYP");
		DOC_TYPE.setTitle(ColorUtil.getRedTitle(Util.TI18N.DOC_TYPE()));
		
		ListGridField FROM_DATE = new ListGridField("FROM_DATE",Util.TI18N.FROM_DATE(),120);//生效期
		ListGridField TO_DATE = new ListGridField("TO_DATE",Util.TI18N.TO_DATE(),120);//失效期
//		Util.initDateTime(typeTable,FROM_DATE);
//		Util.initDateTime(typeTable,TO_DATE);
		Util.initDate(typeTable, FROM_DATE);
		Util.initDate(typeTable, TO_DATE);
		
		ListGridField EXEC_SEQ = new ListGridField("EXEC_SEQ",Util.TI18N.SHOW_SEQ(),60);//顺序
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),50);//
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		typeTable.setFields(CUSTOMER_ID,TIME_TYPE,DOC_TYPE,FROM_DATE,TO_DATE,EXEC_SEQ,ENABLE_FLAG);
		
		typeTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				typeclickrecord = event.getRecord();
				typeTable.OP_FLAG = "M";

				findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", "M");
				findValues.addCriteria("TYP_ID", event.getRecord().getAttributeAsString("ID"));
				
				formulaTable.fetchData(findValues,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						// TODO Auto-generated method stub
						if(formulaTable.getRecords().length > 0){
						   formulaTable.selectRecord(formulaTable.getRecord(0));
						}else{
							conditionTable.setData(new RecordList());
						}
					}
				}) ;
				
//				ListGridRecord[] records = formulaTable.getSelection();
//				formulaTable.selectRecord(records[0]);
//				initSaveBtn();
			}
		});
	}
	
	private void createForlaFields(final SGTable formulaTable) {
	
//		final ListGridField FORMULA = new ListGridField("FORMULA","",1);//表达式
//		FORMULA.setHidden(true);
		final ListGridField FORMULA_DESCR = new ListGridField("FORMULA",Util.TI18N.FORMULA(),300);//表达式
		FORMULA_DESCR.setTitle(ColorUtil.getRedTitle(Util.TI18N.FORMULA()));
		
		ListGridField TIME_UNIT = new ListGridField("TIME_UNIT",ColorUtil.getRedTitle(Util.TI18N.TIME_UNIT()),60);//时间单位
		Util.initComboValue(TIME_UNIT, "BAS_MSRMNT_UNIT", "UNIT", "UNIT_NAME", " where MSRMNT_CODE = 'TIME'", " show_seq asc");
		
		ListGridField UNIT_FROM = new ListGridField("UNIT_FROM",Util.TI18N.UNIT_FROM(),50);//从
		ListGridField UNIT_TO = new ListGridField("UNIT_TO",Util.TI18N.UNIT_TO(),50);//到
		ListGridField UNIT_FIELD = new ListGridField("UNIT_FIELD",Util.TI18N.UNIT_FIELD(),70);//级差单位

		formulaTable.setFields(FORMULA_DESCR,TIME_UNIT,UNIT_FROM,UNIT_TO,UNIT_FIELD);
		
		FormItemIcon icon1 = new FormItemIcon();
		FORMULA_DESCR.setIcons(icon1);
		FORMULA_DESCR.setShowSelectedIcon(true);
	    icon1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				if(typeTable != null && typeTable.getSelectedRecord() != null) {
					ListGridRecord rec = typeTable.getSelectedRecord();
					if(rec != null) {
						timeWin=new TimeWin(formulaTable,formulaTable.getEditRow(),"40%", "38%", rec.getAttribute("DOC_TYPE")).getViewPanel();
					}
				}
			}
		});
		formulaTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				
				formulaclickrecord = event.getRecord();
				formulaTable.OP_FLAG = "M";
//				Criteria criteria=new Criteria();
//				criteria.addCriteria("OP_FLAG", "M");
//				criteria.addCriteria("FORMULA_ID", event.getRecord().getAttributeAsString("ID"));
//				conditionTable.fetchData(criteria);
				findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", "M");
				findValues.addCriteria("FORMULA_ID", event.getRecord().getAttributeAsString("ID"));
				
				conditionTable.fetchData(findValues);
				
				
//				delB.enable();
			}
		});
	    
		
		
	}

	
	private void createCondFields(SGTable conditionTable) {
		ListGridField C_FIELD = new ListGridField("C_FIELD",Util.TI18N.C_FIELD(),90);//字段值--字段
		Util.initComboValue(C_FIELD, "USER_TAB_COLUMNS", "COLUMN_NAME", "COLUMN_NAME", " where TABLE_NAME = 'V_TIME_SHPM'", "");
		C_FIELD.setTitle(ColorUtil.getRedTitle(Util.TI18N.C_FIELD()));
		ListGridField O_OPERATOR = new ListGridField("O_OPERATOR",Util.TI18N.O_OPERATOR(),70);//操作符--运算符
		Util.initComboValue(O_OPERATOR, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPERATOR'", " SHOW_SEQ ASC");
		O_OPERATOR.setTitle(ColorUtil.getRedTitle(Util.TI18N.O_OPERATOR()));
		ListGridField C_VALUE = new ListGridField("C_VALUE",Util.TI18N.C_VALUE(),230);//条件值
		C_VALUE.setTitle(ColorUtil.getRedTitle(Util.TI18N.C_VALUE()));
		ListGridField OP_METHOD = new ListGridField("OP_METHOD",Util.TI18N.OP_METHOD(),80);//操作方式
		Util.initComboValue(OP_METHOD, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPER_METHOD'", " SHOW_SEQ ASC");
		OP_METHOD.setTitle(ColorUtil.getRedTitle(Util.TI18N.OP_METHOD()));
		conditionTable.setFields(C_FIELD,O_OPERATOR,C_VALUE,OP_METHOD);
		
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
					searchWin = new SearchWin(formulaDS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		    
		    //新增按钮
		    newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.TIME_P0_01);
		    newButton.addClickHandler(new NewAction(typeTable,cache_map,this));
		    newButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					newB.disable();
					newB2.disable();
				}
			});
	        
	        //保存按钮
	        saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.TIME_P0_02);
	        saveButton.addClickHandler(new SaveAction(typeTable,check_map,this));
	        saveButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					newB.enable();
					newB2.enable();
				}
			});
	        
	        //删除按钮
	        delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.TIME_P0_03);
//	        delForlaButton.addClickHandler(new DeleteAction(typeTable,this));
	        delButton.addClickHandler(new DeleteProAction(typeTable));
	        
	        //取消按钮
	        canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.TIME_P0_04);
	        canButton.addClickHandler(new CancelAction(typeTable,formulaTable,this));
	        canButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					newB.enable();
					newB2.enable();
				}
			});
//	        //导出按钮
//	        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.TIME_P0_05);
//	        expButton.addClickHandler(new ExportAction(typeTable, "addtime desc"));
	        
	        add_map.put(BasPrivRef.TIME_P0_01, newButton);
	        del_map.put(BasPrivRef.TIME_P0_03, delButton);
	        save_map.put(BasPrivRef.TIME_P0_02, saveButton);
	        save_map.put(BasPrivRef.TIME_P0_04, canButton);
	        this.enableOrDisables(add_map, true);
	        enableOrDisables(del_map, false);
	        this.enableOrDisables(save_map, false);
	        
//	        newButton.enable();
//	        saveButton.disable();
//	        delButton.disable();
//	        canButton.disable();
	      
	        toolStrip.setMembersMargin(5);
	        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	        
	}

	protected DynamicForm createSerchForm(SGPanel form) {
		form.setDataSource(formulaDS);
		form.setAutoFetchData(false);
		form.setWidth(460);
//		form.setAlign(Alignment.LEFT);
		form.setNumCols(4);
		
		
		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
//		txt_global.setColSpan(6);
//		txt_global.setEndRow(false);
		txt_global.setVisible(false);
		
		TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
//		SGCombo CUSTOMER_ID=new SGCombo("CUSTOMER_ID", Util.TI18N.CUSTOMER_ID(),true);
//	    Util.initCustComboValue(CUSTOMER_ID, "");
		
		SGCombo TIME_TYPE=new SGCombo("TIME_TYPE", Util.TI18N.TIME_TYPE());
		TIME_TYPE.setColSpan(2);
//		Util.initCodesComboValue(TIME_TYPE, "TIME_TYP");
//		Util.initComboValue(TIME_TYPE, "V_TIME_TYPE", "TIME_TYPE", "TIME_TYPE_NAME", "", "");
		Util.initComboValue(TIME_TYPE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'TIME_TYP'", " SHOW_SEQ ASC");
		
		SGCombo DOC_TYPE=new SGCombo("DOC_TYPE", Util.TI18N.DOC_TYPE());
		Util.initCodesComboValue(DOC_TYPE, "DOC_TYP");
		
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());	
		chk_enable.setValue(true);
		chk_enable.setVisible(false);
		form.setItems(txt_global,CUSTOMER_ID,CUSTOMER_NAME,TIME_TYPE,DOC_TYPE,chk_enable);
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_TIME_TYPE");
		cache_map.put("ENABLE_FLAG", "Y");
		
		check_map.put("FROM_DATE", StaticRef.CHK_DATE + Util.TI18N.FROM_DATE());
		check_map.put("TO_DATE", StaticRef.CHK_DATE + Util.TI18N.TO_DATE());
		check_map.put("CUSTOMER_ID,TIME_TYPE,DOC_TYPE", StaticRef.CHK_UNIQUE+Util.TI18N.CUSTOMER_ID()+","+Util.TI18N.TIME_TYPE()+"," +Util.TI18N.DOC_TYPE());
		
		
		formula_ck_map.put("TABLE", "BAS_TIME_FORMULA");
		formula_ck_map.put("FORMULA", StaticRef.CHK_NOTNULL + Util.TI18N.FORMULA());
//		formula_ck_map.put("TYP_ID,FORMULA", StaticRef.CHK_UNIQUE+Util.TI18N.FORMULA());
		formula_ck_map.put("TIME_UNIT", StaticRef.CHK_NOTNULL + Util.TI18N.TIME_UNIT());
//		formula_ck_map.put("TYP_ID,FORMULA,TIME_UNIT", StaticRef.CHK_UNIQUE + Util.TI18N.FORMULA()+"," +Util.TI18N.TIME_UNIT());
	
		condition_ck_map.put("TABLE", "BAS_TIME_CONDITION");
		condition_ck_map.put("C_FIELD", StaticRef.CHK_NOTNULL + Util.TI18N.C_FIELD());
		condition_ck_map.put("O_OPERATOR", StaticRef.CHK_NOTNULL + Util.TI18N.O_OPERATOR());
		condition_ck_map.put("C_VALUE", StaticRef.CHK_NOTNULL + Util.TI18N.C_VALUE());
		condition_ck_map.put("OP_METHOD", StaticRef.CHK_NOTNULL + Util.TI18N.OP_METHOD());
//		condition_ck_map.put("FORMULA_ID,C_FIELD,O_OPERATOR,C_VALUE,OP_METHOD", StaticRef.CHK_UNIQUE+ 
//				 Util.TI18N.C_FIELD() + "," + Util.TI18N.O_OPERATOR()+"," + Util.TI18N.C_VALUE()+"," + Util.TI18N.OP_METHOD());
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}if(timeWin != null) {
		    timeWin.destroy();
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
		BasTimeMangeView view = new BasTimeMangeView();
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
