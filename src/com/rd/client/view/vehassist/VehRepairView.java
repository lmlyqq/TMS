package com.rd.client.view.vehassist;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.vehassist.VehRepairDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BasVehicleWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
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
 * 运输管理->维修保养管理
 * @author lml
 *
 */
@ClassForNameAble
public class VehRepairView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table;
	 private ValuesManager form_groups;   
	 private DynamicForm main_form;            
	 private SectionStack section;
	 private Window searchWin = null;
	 private SGPanel searchForm;
	 private Window uploadWin;
	 
	 /*public VehRepairView(String id) {
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
	    ds = VehRepairDS.getInstance("TRANS_VEH_REPAIR");
		
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
		//form_groups.addMember(pack_form);
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
                initSaveBtn();
            }
        });
		
        ListGridField REPAIR_TYPE = new ListGridField("REPAIR_TYPE_NAME","维修类型", 100);
       //REPAIR_TYPE.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATENO()));
        
        ListGridField SUPLR_ID = new ListGridField("SUPLR_ID","承运商", 80);
        Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME", "", "");
       //TRS_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRS_ID()));
        ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 80);
        ListGridField DRIVER = new ListGridField("DRIVER", "司机", 80);
        ListGridField REPAIR_OBJECT = new ListGridField("REPAIR_OBJECT_NAME", "维修项目", 80);
        ListGridField PRE_START_TIME = new ListGridField("PRE_START_TIME", "预计开始时间", 150);
        ListGridField PRE_END_TIME = new ListGridField("PRE_END_TIME", "预计结束时间", 150);
        ListGridField TOTAL_AMOUNT = new ListGridField("TOTAL_AMOUNT", "总费用", 80);
        ListGridField REPAIR_COMPANY = new ListGridField("REPAIR_COMPANY","维修单位", 120);
        ListGridField REPAIR_ADDRESS = new ListGridField("REPAIR_ADDRESS", "维修地址", 120);
        ListGridField NOTES = new ListGridField("NOTES", "备注", 80);
  
        table.setFields(REPAIR_TYPE, SUPLR_ID, PLATE_NO, DRIVER, REPAIR_OBJECT, PRE_START_TIME, PRE_END_TIME
        		, TOTAL_AMOUNT, REPAIR_COMPANY, REPAIR_ADDRESS, NOTES);
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
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0),form_groups).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.VehPepair_P0_01);
        newButton.addClickHandler(new NewMultiFormAction(form_groups,cache_map,this));
        
        String[] str = new String[2];
        str[0] = "REPAIR_TYPE";
        str[1] = "REPAIR_OBJECT";
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.VehPepair_P0_02);
        saveButton.addClickHandler(new SaveMultiFormAction(table,form_groups,check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.VehPepair_P0_03);
        delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.VehPepair_P0_04);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.VehPepair_P0_05);
        expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
    
        IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.VehPepair_P0_06);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					JavaScriptObject jsobject = section.getSection(0).getAttributeAsJavaScriptObject("controls");
					Canvas[] canvas = null;
					DynamicForm pageForm=null;
					if(jsobject != null) {
						canvas = Canvas.convertToCanvasArray(jsobject);
					}
					for(int i = 0; i < canvas.length; i++) {
						if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
							pageForm = (DynamicForm)canvas[i];
							break;
						}
					}
					uploadWin = new UploadFileWin().getViewPanel("repair.xls","TMP_REPAIR_IMPORT","SP_IMPORT_VEHREPAIR",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});   
        
        
        add_map.put(BasPrivRef.VehPepair_P0_01, newButton);
        del_map.put(BasPrivRef.VehPepair_P0_03, delButton);
        save_map.put(BasPrivRef.VehPepair_P0_02, saveButton);
        save_map.put(BasPrivRef.VehPepair_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton,expButton,inputButton);
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "TRANS_VEH_REPAIR");
		check_map.put("PRE_START_TIME", StaticRef.CHK_DATE +"预计开始时间");
		check_map.put("PRE_END_TIME", StaticRef.CHK_DATE +"预计结束时间");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(SGPanel form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		

		SGText PLATE_NO = new SGText("PLATE_NO", "车牌号");
		SGText DRIVER = new SGText("DRIVER", "司机");
		final SGCombo REPAIR_TYPE = new SGCombo("REPAIR_TYPE", "维修类型",true);	
	    Util.initCodesComboValue(REPAIR_TYPE,"REPAIR_TYP");
	        
	    final SGCombo REPAIR_OBJECT = new SGCombo("REPAIR_OBJECT", "维修项目");	
	    Util.initCodesComboValue(REPAIR_OBJECT,"REPAIR_OBJ");
		
	    SGText PRE_START_TIME =new  SGText("PRE_START_TIME","开始时间",true);
	    Util.initDate(form, PRE_START_TIME);
		
	    SGText PRE_END_TIME = new  SGText("PRE_END_TIME", "结束时间");
		Util.initDate(form, PRE_END_TIME);
		
		SGText REPAIR_COMPANY = new SGText("REPAIR_COMPANY", "维修单位",true);
        form.setItems(PLATE_NO,DRIVER,REPAIR_TYPE,REPAIR_OBJECT,PRE_START_TIME,PRE_END_TIME,REPAIR_COMPANY);
        
        return form;
	}
	
	private void createMainForm(DynamicForm form) {
	
        
        final SGCombo REPAIR_TYPE = new SGCombo("REPAIR_TYPE", "维修类型");

        Util.initCodesComboValue(REPAIR_TYPE,"REPAIR_TYP");
        
        final SGCombo REPAIR_OBJECT = new SGCombo("REPAIR_OBJECT", "维修项目",true);	
        Util.initCodesComboValue(REPAIR_OBJECT,"REPAIR_OBJ");
        
        final SGText PLATE_NO = new SGText("PLATE_NO",ColorUtil.getRedTitle("车牌号"));
        PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BasVehicleWin(main_form,"20%","50%").getViewPanel();		
			}
		});
		PLATE_NO.setIcons(searchPicker);
        
        
        
        
    	SGText DRIVER = new SGText("DRIVER","司机");      
		
        
    	SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
		Util.initSupplier(SUPLR_ID, "");
		
		
		final SGText PRE_START_TIME = new SGText("PRE_START_TIME","预计开始时间");
		 Util.initDateTime(form_groups,PRE_START_TIME);
    

        final SGText PRE_END_TIME = new SGText("PRE_END_TIME","预计结束时间");
        Util.initDateTime(form_groups,PRE_END_TIME);

        SGText REPAIR_DAYS=new SGText("REPAIR_DAYS", "维修天数",true);
        
        SGText REPAIR_COMPANY = new SGText("REPAIR_COMPANY", "维修单位");
     
        SGLText REPAIR_ADDRESS = new SGLText("REPAIR_ADDRESS","维修地址");  
        
        SGText TOTAL_AMOUNT = new SGText("TOTAL_AMOUNT","总费用");
      
        TextAreaItem NOTES = new TextAreaItem("NOTES", "备注");
        NOTES.setStartRow(true);
        NOTES.setColSpan(8);
        NOTES.setHeight(60);
        NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
        NOTES.setTitleOrientation(TitleOrientation.TOP);
        NOTES.setTitleVAlign(VerticalAlignment.TOP);
        
       form.setItems(PLATE_NO,REPAIR_TYPE,DRIVER,SUPLR_ID,REPAIR_OBJECT,PRE_START_TIME,PRE_END_TIME,TOTAL_AMOUNT,REPAIR_DAYS,REPAIR_COMPANY,REPAIR_ADDRESS,NOTES);
    
	}
	


	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
			//searchItem = null;
		}
		//table.destroy();
		//main_form.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		VehRepairView view = new VehRepairView();
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
