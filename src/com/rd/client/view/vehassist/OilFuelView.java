package com.rd.client.view.vehassist;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.vehassist.OilFuelDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.LoadNewWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
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
 * 基础资料->加油记录
 * @author
 *
 */
@ClassForNameAble
public class OilFuelView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table;
	 private ValuesManager form_groups;
	 private DynamicForm main_form;             //主信息页签布局
	 private SectionStack section;
	 private Window searchWin = null;
	 private DynamicForm searchForm;
	 
	 /*public OilFuelView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		form_groups = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = OilFuelDS.getInstance("TRANS_VEH_OIL");
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
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
        
        
	    Tab tab1 = new Tab(Util.TI18N.MAININFO());
	    main_form = new SGPanel();
		createMainForm(main_form);
		main_form.setWidth("40%");
	    HLayout form_lay = new HLayout();
	    form_lay.setWidth100();
	    form_lay.setHeight100();
	    form_lay.setBackgroundColor(ColorUtil.BG_COLOR);
	    form_lay.addMember(main_form);
	    form_lay.setWidth("40%");
	    tab1.setPane(form_lay);
	    leftTabSet.addTab(tab1);
         
        v_lay.setMembers(leftTabSet); 
		form_groups.addMember(main_form);
		form_groups.setDataSource(ds);
        
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
                form_groups.editRecord(selectedRecord);
                
                enableOrDisables(del_map, true);
                initSaveBtn();
            }
        });
		
        ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATENO(), 150);
        ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单号", 150);
        ListGridField DRIVER1 = new ListGridField("DRIVER", Util.TI18N.DRIVER1(), 100);
        ListGridField MOBILE = new ListGridField("MOBILE", Util.TI18N.MOBILE(), 120);
        ListGridField OIL_TIME = new ListGridField("OIL_TIME", Util.TI18N.OIL_TIME(), 150);
        ListGridField OIL_NAME = new ListGridField("OIL_NAME_NAME", Util.TI18N.OIL_NAME(), 100);
        ListGridField OIL_VOLUME = new ListGridField("OIL_VOLUME", Util.TI18N.OIL_VOLUMEN(), 100);
        ListGridField OIL_PRICE = new ListGridField("OIL_PRICE", Util.TI18N.OIL_PRICE(), 100);
        ListGridField OIL_AMOUNT = new ListGridField("OIL_AMOUNT", Util.TI18N.OIL_AMOUNT(), 120);
        ListGridField OIL_ADDRESS = new ListGridField("OIL_ADDRESS", Util.TI18N.OIL_ADDRESS(),150);
        ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 150);
        
        table.setFields(PLATE_NO, LOAD_NO, DRIVER1, MOBILE, OIL_TIME, OIL_NAME, OIL_VOLUME
        		, OIL_PRICE, OIL_AMOUNT, OIL_ADDRESS, NOTES);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new DynamicForm();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0),form_groups).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.OilFue_P1_01);
        newButton.addClickHandler(new NewMultiFormAction(form_groups,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.OilFue_P1_02);
        saveButton.addClickHandler(new SaveMultiFormAction(table, form_groups, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.OilFue_P1_03);
        delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.OilFue_P1_04);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.OilFue_P1_05);
        expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
    
        add_map.put(BasPrivRef.OilFue_P1_01, newButton);
        del_map.put(BasPrivRef.OilFue_P1_03, delButton);
        save_map.put(BasPrivRef.OilFue_P1_02, saveButton);
        save_map.put(BasPrivRef.OilFue_P1_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton);
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "TRANS_VEH_OIL");
		check_map.put("PLATE_NO", StaticRef.CHK_NOTNULL + Util.TI18N.PLATENO());
		check_map.put("OIL_TIME", StaticRef.CHK_DATE + Util.TI18N.OIL_TIME());
		check_map.put("OIL_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.OIL_NAME());
		check_map.put("OIL_VOLUME", StaticRef.CHK_NOTNULL + Util.TI18N.OIL_VOLUMEN());
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

		TextItem PLATENO = new TextItem("PLATE_NO", Util.TI18N.PLATENO());
		TextItem LOAD_NO = new TextItem("LOAD_NO", "调度单号");
		PLATENO.setTitleOrientation(TitleOrientation.TOP);
		LOAD_NO.setTitleOrientation(TitleOrientation.TOP);

		
		SGDateTime ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", "加油时间 从",true);
		SGDateTime ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_FROM.setColSpan(1);
        form.setItems(PLATENO,LOAD_NO,ODR_TIME_FROM,ODR_TIME_TO);
        
        return form;
	}
	
	private void createMainForm(final DynamicForm form) {
		SGText PLATENO = new SGText("PLATE_NO",Util.TI18N.PLATENO(),true);      
		PLATENO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATENO()));

        
		SGText LOAD_NO = new SGText("LOAD_NO", "调度单号");
        PickerIcon searchPicker1 = new PickerIcon(PickerIcon.SEARCH);
		searchPicker1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new LoadNewWin(form,"30%","40%").getViewPanel();		
			}
		});
		LOAD_NO.setIcons(searchPicker1);

		final SGText DRIVER = new SGText("DRIVER",Util.TI18N.DRIVER1());
      
    
        final SGText MOBILE = new SGText("MOBILE",Util.TI18N.MOBILE());
     

        final SGText OIL_TIME = new SGText("OIL_TIME",Util.TI18N.OIL_TIME(),true);
        OIL_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.OIL_TIME()));
        Util.initDateTime(form_groups,OIL_TIME);
        
        final SGCombo OIL_NAME = new SGCombo("OIL_NAME", ColorUtil.getRedTitle(Util.TI18N.OIL_NAME()));	//客户属性
        OIL_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.OIL_NAME()));
        Util.initCodesComboValue(OIL_NAME,"FUEL");
        
        final SGText OIL_VOLUMEN = new SGText("OIL_VOLUME","加油数量");
        OIL_VOLUMEN.setTitle(ColorUtil.getRedTitle(Util.TI18N.OIL_VOLUMEN()));
        
       

        SGText OIL_PRICE = new SGText("OIL_PRICE", Util.TI18N.OIL_PRICE());
     
        SGText OIL_AMOUNT = new SGText("OIL_AMOUNT", Util.TI18N.OIL_AMOUNT(),true);
        
        SGText OIL_ADDRESS = new SGText("OIL_ADDRESS", Util.TI18N.OIL_ADDRESS());
       
        
        SGText NOTES = new SGText("NOTES",Util.TI18N.NOTES());
    
        
       form.setItems(PLATENO,LOAD_NO,DRIVER,MOBILE,OIL_TIME,OIL_NAME,OIL_VOLUMEN,OIL_PRICE,OIL_AMOUNT,OIL_ADDRESS,NOTES);
  
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
		OilFuelView view = new OilFuelView();
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
