package com.rd.client.view.vehassist;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.vehassist.VehVerifyDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BasVehicleWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragDataAction;
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
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
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
 * 年审管理
 * @author Administrator
 *
 */
@ClassForNameAble
public class VehVerifyView extends SGForm implements PanelFactory {
	
	private DataSource vehVerifyDS;
	private SGTable table;
	private TabSet leftTabSet;
	private HStack stack;
	private SectionStack list_section;
	private SectionStack section;
	private DynamicForm mainForm;
	private ValuesManager vm;
	private Window searchWin;
	private SGPanel searchForm;
	private Window uploadWin;
	
	/*public VehVerifyView(String id) {
		super(id);
	}*/

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    vehVerifyDS = VehVerifyDS.getInstance("V_TRANS_VEH","TRANS_VEH_VERIFY");
		
	    stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(vehVerifyDS, "100%", "100%",true,true,false);
		createListField();
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.addSection(listItem);
	    list_section.setWidth("100%");
	    stack.addMember(list_section);
		addSplitBar(stack);
		
		//STACK的右边布局
        leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
        
        initVerify();
        
        Tab tab1 = new Tab(Util.TI18N.MAININFO());
		tab1.setPane(createHeader());
		leftTabSet.addTab(tab1);
        stack.addMember(leftTabSet);
        
        vm.addMember(mainForm);
        vm.setDataSource(vehVerifyDS);
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
		
		return main;
	}
	
	private void createListField() {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	vm.editRecord(selectedRecord);
                initSaveBtn();
            }
        });
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号 ",90);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID","承运商",80);
		Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME", "", "");
		ListGridField VERIFY_GRADE = new ListGridField("VERIFY_GRADE_NAME","年审等级",80);
		ListGridField VERIFY_DATE = new ListGridField("VERIFY_DATE","年审日期",90);
		ListGridField VERIFY_AMOUNT = new ListGridField("VERIFY_AMOUNT","年审金额",60);
		ListGridField NEXT_DATE = new ListGridField("NEXT_DATE","下次年审日期",90);
		ListGridField VERIFY_ADDRESS = new ListGridField("VERIFY_ADDRESS","年审地",120);
		ListGridField NOTES = new ListGridField("NOTES","备注",120);
		
		table.setFields(PLATE_NO,SUPLR_ID,VERIFY_GRADE,VERIFY_DATE,VERIFY_AMOUNT,NEXT_DATE,VERIFY_ADDRESS,NOTES);	
		table.setCanDragRecordsOut(true);
		table.setCanReorderRecords(true);
		table.setCanAcceptDroppedRecords(true);
		table.setDragDataAction(DragDataAction.MOVE);
	}
	
	private SectionStack createHeader() {
		//1
		SGText PLATE_NO = new SGText("PLATE_NO",ColorUtil.getRedTitle("车牌号"),true);
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BasVehicleWin(mainForm,"20%","50%").getViewPanel();		
			}
		});
		PLATE_NO.setIcons(searchPicker);
		
		
		
		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGCombo VERIFY_GRADE = new SGCombo("VERIFY_GRADE","年审等级");
		Util.initCodesComboValue(VERIFY_GRADE, "VERIFY_GRADE");
		//2
		SGDate VERIFY_DATE = new SGDate("VERIFY_DATE","年审日期",true);
//		Util.initDate(vm,VERIFY_DATE);
		SGText VERIFY_AMOUNT = new SGText("VERIFY_AMOUNT","年审金额");
		SGDate NEXT_DATE = new SGDate("NEXT_DATE","下次年审日期");
//		Util.initDate(vm,NEXT_DATE);
		//3
		SGText VERIFY_ADDRESS = new SGText("VERIFY_ADDRESS","年审地",true);
		SGLText NOTES = new SGLText("NOTES","备注");
		
		mainForm = new SGPanel();
        mainForm.setWidth("40%");
		mainForm.setItems(PLATE_NO,SUPLR_ID,VERIFY_GRADE,VERIFY_DATE,VERIFY_AMOUNT,NEXT_DATE,VERIFY_ADDRESS,NOTES);
		
        section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		SectionStackSection mainS = new SectionStackSection("主信息");
		mainS.addItem(mainForm);
		mainS.setExpanded(true); 
		section.addSection(mainS);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(true);
		
		return section;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);
	        
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(vehVerifyDS,createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}
		});
	        
			
		IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.VehVerify_P1_01);
		newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));
		
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.VehVerify_P1_02);
		saveButton.addClickHandler(new SaveMultiFormAction(table,vm,check_map, this));
	        
		IButton delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.VehVerify_P1_03);
		delButton.addClickHandler(new DeleteFormAction(table,mainForm));
	        
		IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.VehVerify_P1_04);
		canButton.addClickHandler(new CancelFormAction(table,mainForm,this));
	    
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.VehVerify_P1_05);
		expButton.addClickHandler(new ExportAction(table));
		
		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, TrsPrivRef.VehVerify_P1_06);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					JavaScriptObject jsobject = list_section.getSection(0).getAttributeAsJavaScriptObject("controls");
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
					uploadWin = new UploadFileWin().getViewPanel("verify.xls","TMP_VEHVERIFY_IMPORT","SP_IMPORT_VEHVERIFY",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});   
		
		add_map.put(TrsPrivRef.VehVerify_P1_01, newButton);
		del_map.put(TrsPrivRef.VehVerify_P1_03, delButton);
		save_map.put(TrsPrivRef.VehVerify_P1_02, saveButton);
		save_map.put(TrsPrivRef.VehVerify_P1_04, canButton);
		this.enableOrDisables(add_map, true);
		enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
	        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton,inputButton);
	}
	
	public DynamicForm createSerchForm(SGPanel form){
		form.setDataSource(vehVerifyDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGText PLATE_NO = new SGText("PLATE_NO","车牌号",true);
		SGCombo VERIFY_GRADE = new SGCombo("VERIFY_GRADE","年审等级");
		Util.initCodesComboValue(VERIFY_GRADE, "VERIFY_GRADE");
		SGDate VERIFY_DATE = new SGDate("VERIFY_DATE","年审日期");
	
		form.setItems(PLATE_NO,VERIFY_GRADE,VERIFY_DATE);
		return form;
	}
	
	@Override
	public void initVerify() {
		check_map.put("PLATE_NO", StaticRef.CHK_NOTNULL + "车牌号");
		check_map.put("VERIFY_DATE", StaticRef.CHK_DATE + "年审日期");
		check_map.put("NEXT_DATE", StaticRef.CHK_DATE + "下次年审日期");
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		VehVerifyView view = new VehVerifyView();
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
