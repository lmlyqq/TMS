package com.rd.client.view.settlement;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillFactoryDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 费用管理--计费管理--计费要素管理
 * @author 
 *
 */
@ClassForNameAble
public class BillFactorView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table; //列表信息
	 private DynamicForm main_form;  //主信息页签布局
	 private SectionStack section;
	 private Window searchWin = null;
	 private DynamicForm searchForm;
	 

	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = BillFactoryDS.getInstance("TARIFF_FACTOR","TARIFF_FACTOR");
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection("列表信息");
	    listItem.setItems(table);
	    listItem.setExpanded(true);
		listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
	    section.setWidth("100%");
	    


		getConfigList();
		stack.addMember(section);
		addSplitBar(stack);
		
		
		//STACK的右边布局v_lay
        VLayout v_lay = new VLayout();
        v_lay.setWidth("80%");
        v_lay.setHeight100();
        v_lay.setBackgroundColor(ColorUtil.BG_COLOR);
        v_lay.setVisible(false);
     
		
		//右表
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("100%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        
        
	    Tab tab1 = new Tab("主信息");
	    main_form = new SGPanel();
	    main_form.setWidth("45%");
		createMainForm(main_form);
	    HLayout form_lay = new HLayout();
	    form_lay.setWidth("45%");
	    form_lay.setHeight100();
	    form_lay.setBackgroundColor(ColorUtil.BG_COLOR);
	    form_lay.addMember(main_form);
	    tab1.setPane(form_lay);
	    leftTabSet.addTab(tab1);
         
        v_lay.setMembers(leftTabSet); 
        
		stack.addMember(v_lay);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
        table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
		        enableOrDisables(save_map, true);
		        
				if(isMax) {
					expend();
				}
			}
			
		});
		initVerify();
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddBtn();
			}
			
		});
		  
		return main;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}
	

	 
	private void getConfigList() {
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	main_form.editRecord(selectedRecord);
            	main_form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                initSaveBtn();
            }
        });
		ListGridField FEE_TYPE = new ListGridField("FEE_TYPE", "对象类别", 120);
        Util.initCodesComboValue(FEE_TYPE,"TRANS_TFF_TYP");
		ListGridField FEE_FACTOR = new ListGridField("FEE_FACTOR", "计费要素名称", 120);
		ListGridField BIZ_OBJECT = new ListGridField("BIZ_OBJECT", "业务对象", 120);
        Util.initCodesComboValue(BIZ_OBJECT,"VIEW_TYP");
        ListGridField FM_TABLE = new ListGridField("FM_TABLE", "业务表", 100);
        ListGridField FM_FIELD = new ListGridField("FM_FIELD", "业务字段", 100);
        ListGridField FEE_TABLE = new ListGridField("FEE_TABLE", "映射费率表", 100);
        FEE_TABLE.setHidden(true);
        ListGridField FEE_FIELD = new ListGridField("FEE_FIELD", "映射费率字段", 100);
        FEE_FIELD.setHidden(true);
        ListGridField OBJ_TYPE = new ListGridField("OBJ_TYPE", "控件类型", 120);
        Util.initCodesComboValue(OBJ_TYPE,"CONTROL_TYP");
        ListGridField DICT_PARAM = new ListGridField("DICT_PARAM", "数据字典参数", 120);
        ListGridField OTHER_CONDITION = new ListGridField("OTHER_CONDITION", "附件条件", 120);
        ListGridField ADDTIME = new ListGridField("ADDTIME", "创建时间", 100);
        ListGridField ADDWHO = new ListGridField("ADDWHO", "创建人", 80);
        ListGridField ACTIVE_FLAG = new ListGridField("ACTIVE_FLAG", "激活", 80);
        ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);
        
        table.setFields(FEE_TYPE,FEE_FACTOR, BIZ_OBJECT,FM_TABLE,FM_FIELD, FEE_TABLE, FEE_FIELD,OBJ_TYPE,DICT_PARAM, OTHER_CONDITION
        		, ADDTIME,ADDWHO,ACTIVE_FLAG);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.BillFactory);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new DynamicForm();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SettPrivRef.BillFactory_P0_01);
        newButton.addClickHandler(new NewFormAction(main_form,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.BillFactory_P0_01);
        saveButton.addClickHandler(new SaveFormAction(table, main_form, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SettPrivRef.BillFactory_P0_01);
        delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.BillFactory_P0_01);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
        
 
        add_map.put(SettPrivRef.BillFactory_P0_01, newButton);
        del_map.put(SettPrivRef.BillFactory_P0_03, delButton);
        save_map.put(SettPrivRef.BillFactory_P0_02, saveButton);
        save_map.put(SettPrivRef.BillFactory_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	}
	
	public void initVerify() {
	
		check_map.put("TABLE", "TARIFF_FACTOR");
		check_map.put("FEE_FACTOR", StaticRef.CHK_NOTNULL + "计费要素");
		
		check_map.put("BIZ_OBJECT", StaticRef.CHK_NOTNULL + "业务对象");
		check_map.put("FEE_TYPE", StaticRef.CHK_NOTNULL + "对象类别");
		//check_map.put("FEE_FACTOR", StaticRef.CHK_NOTNULL + "计费要素");
		
		cache_map.put("ACTIVE_FLAG", "Y");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");

		SGText FEE_FACTOR = new SGText("FEE_FACTOR", "要素名称");
		
		SGCheck ACTIVE_FLAG=new SGCheck("ACTIVE_FLAG", "激活");
		
		SGDateTime FROM_TIME = new SGDateTime("FROM_TIME","创建时间 从",true);
		FROM_TIME.setWidth(FormUtil.Width);
		
		SGDateTime END_TIME = new SGDateTime("END_TIME","到");
		END_TIME.setWidth(FormUtil.Width);
		
		SGCombo FEE_TYPE = new SGCombo("FEE_TYPE","对象类别");
		Util.initCodesComboValue(FEE_TYPE,"TRANS_TFF_TYP");

        form.setItems(FEE_TYPE,FEE_FACTOR,ACTIVE_FLAG,FROM_TIME,END_TIME);
        
        return form;
	}
	
	private void createMainForm(DynamicForm form) {
		
		SGText FEE_FACTOR = new SGText("FEE_FACTOR",ColorUtil.getRedTitle("计费要素名称"));      
		       
		SGCombo FEE_TYPE = new SGCombo("FEE_TYPE",ColorUtil.getRedTitle("所属对象"));
		Util.initCodesComboValue(FEE_TYPE,"TRANS_TFF_TYP");
		
		SGCheck ACTIVE_FLAG = new SGCheck("ACTIVE_FLAG","激活");
		
		SGCombo BIZ_OBJECT = new SGCombo("BIZ_OBJECT",ColorUtil.getRedTitle("业务对象"),true);
		
		Util.initCodesComboValue(BIZ_OBJECT,"VIEW_TYP");
		
		SGText FM_TABLE = new SGText("FM_TABLE","业务表");
		
		SGText FM_FIELD = new SGText("FM_FIELD","业务字段");
		
		
		
        SGText FEE_TABLE = new SGText("FEE_TABLE","映射费率表",true);      
        FEE_TABLE.setVisible(false);
        
        SGText FEE_FIELD = new SGText("FEE_FIELD", "映射费率字段");	
        FEE_FIELD.setVisible(false);
 
        SGCombo OBJ_TYPE = new SGCombo("OBJ_TYPE","控件类型",true);
		Util.initCodesComboValue(OBJ_TYPE,"CONTROL_TYP");
        
		SGText DICT_PARAM = new SGText("DICT_PARAM","数据字典参数 ");
		
		SGText DATA_FROM = new SGText("DATA_FROM","数据来源 ",true);
		
		SGText DATA_ID = new SGText("DATA_ID","下拉框代码");
		
		SGText DATA_NAME = new SGText("DATA_NAME","下拉框名称 ");
		
        SGLText OTHER_CONDITION = new SGLText("OTHER_CONDITION","附件条件");
       // OTHER_CONDITION.setWidth(FormUtil.longWidth);
        OTHER_CONDITION.setColSpan(4);
        
       
        form.setItems(FEE_TYPE,FEE_FACTOR,ACTIVE_FLAG,BIZ_OBJECT,FM_TABLE,FM_FIELD,FEE_TABLE,FEE_FIELD,DATA_FROM,DATA_ID,DATA_NAME,OTHER_CONDITION,OBJ_TYPE,DICT_PARAM);
        
	}
	


	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BillFactorView view = new BillFactorView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}
}