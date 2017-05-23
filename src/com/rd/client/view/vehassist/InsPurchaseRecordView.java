package com.rd.client.view.vehassist;

import com.google.gwt.core.client.JavaScriptObject;
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
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.vehassist.InsPurchaseRecordDS;
import com.rd.client.reflection.ClassForNameAble;
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
 * 基础资料--保险购买记录
 * @author Administrator
 *
 */
@ClassForNameAble
public class InsPurchaseRecordView extends SGForm implements PanelFactory {
	
	private DataSource ds;
	private SGTable table;
	public ValuesManager vm;
	private DynamicForm mainForm;
	private SectionStack list_section;
	private SectionStack section;
	private HStack stack;
	private TabSet leftTabSet; 
	private Window searchWin;
	private SGPanel searchForm;
	private Window uploadWin;
	
	/*public InsPurchaseRecordView(String id) {
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
	    ds = InsPurchaseRecordDS.getInstance("INSUR_PURCHASE_RECORD","INS_PURCHASE_RECORD");
		
		//主布局
		stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%",true,true,false);
		createListFields();
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
        vm.setDataSource(ds);
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

	private void createListFields() {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
                vm.editRecord(selectedRecord);
                initSaveBtn();
            }
        });
		
        
		ListGridField INS_NO = new ListGridField("INS_NO", Util.TI18N.INS_NO(), 80); 
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATE_NO(), 80);
		ListGridField INS_TYPE = new ListGridField("INS_TYPE_NAME", Util.TI18N.INS_TYPE(), 80);
		ListGridField INS_COMPANY = new ListGridField("INS_COMPANY_NAME", Util.TI18N.INS_COMPANY(), 80);
		ListGridField INS_DOCNO = new ListGridField("INS_DOCNO", Util.TI18N.INS_DOCNO(), 80);
		ListGridField INS_CLS = new ListGridField("INS_CLS_NAME", Util.TI18N.INS_CLS(), 80);
		ListGridField INS_FEE = new ListGridField("INS_FEE", Util.TI18N.INS_FEE(), 80);
		ListGridField INS_AMOUNT = new ListGridField("INS_AMOUNT", Util.TI18N.INS_AMOUNT(), 80);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 80);
		
		table.setFields(INS_NO,PLATE_NO,INS_TYPE,INS_COMPANY,INS_DOCNO,INS_CLS,INS_FEE,INS_AMOUNT,NOTES);	
		table.setCanDragRecordsOut(true);
		table.setCanReorderRecords(true);
		table.setCanAcceptDroppedRecords(true);
		table.setDragDataAction(DragDataAction.MOVE);
	}

	private SectionStack createHeader() {
        
        SGText INS_NO = new SGText("INS_NO",ColorUtil.getRedTitle("保单编号"),true);
        SGText PLATE_NO = new SGText("PLATE_NO",ColorUtil.getRedTitle("车牌号"));
        SGCombo INS_TYPE = new SGCombo("INS_TYPE","保险类别");
        Util.initCodesComboValue(INS_TYPE,"INS_TYP");
        SGCombo INS_COMPANY = new SGCombo("INS_COMPANY","保险公司",true);
        Util.initCodesComboValue(INS_COMPANY,"INS_COMP");
        SGText INS_DOCNO = new SGText("INS_DOCNO","保险单号");
        SGText INS_DATE = new SGText("INS_DATE","投保日期");
        Util.initDate(vm, INS_DATE);
        SGText INS_FROM = new SGText("INS_FROM","有效期从",true);
        Util.initDate(vm, INS_FROM);
        SGText INS_TO = new SGText("INS_TO","有效期到");
        Util.initDate(vm, INS_TO);
        SGCombo INS_CLS = new SGCombo("INS_CLS","保险种类");
        Util.initCodesComboValue(INS_CLS,"INS_CLS");
        SGText INS_FEE = new SGText("INS_FEE","保费",true);
        SGText INS_AMOUNT = new SGText("INS_AMOUNT","保险金额");
        SGText NOTES = new SGText("NOTES","备注");
        
        
        mainForm = new SGPanel();
        mainForm.setWidth("40%");
		mainForm.setItems(INS_NO,PLATE_NO,INS_TYPE,INS_COMPANY,INS_DOCNO,INS_DATE,INS_FROM,INS_TO,INS_CLS,INS_FEE,INS_AMOUNT,NOTES);
		
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
	        
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.INSPURCHASE);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds,createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}
		});
	        
			
		IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.INSPURCHASE_P0_01);
		newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));
		
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.INSPURCHASE_P0_02);
		saveButton.addClickHandler(new SaveMultiFormAction(table,vm,check_map, this));
	        
		IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.INSPURCHASE_P0_03);
		delButton.addClickHandler(new DeleteFormAction(table,mainForm));
	        
		IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.INSPURCHASE_P0_04);
		canButton.addClickHandler(new CancelFormAction(table,mainForm,this));
	        
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.INSPURCHASE_P0_05);
        expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
	        
        IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.INSPURCHASE_P0_06);
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
					uploadWin = new UploadFileWin().getViewPanel("insur.xls","TMP_INSUR_IMPORT","SP_IMPORT_INSUR",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});   
		
		add_map.put(BasPrivRef.INSPURCHASE_P0_01, newButton);
		del_map.put(BasPrivRef.INSPURCHASE_P0_03, delButton);
		save_map.put(BasPrivRef.INSPURCHASE_P0_02, saveButton);
		save_map.put(BasPrivRef.INSPURCHASE_P0_04, canButton);
		this.enableOrDisables(add_map, true);
		enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
	        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton,expButton,inputButton);
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "INS_PURCHASE_RECORD");
		check_map.put("INS_NO", StaticRef.CHK_UNIQUE + Util.TI18N.INS_NO());
		//check_map.put("PLATE_NO", StaticRef.CHK_UNIQUE + Util.TI18N.PLATE_NO());
		check_map.put("PLATE_NO", StaticRef.CHK_NOTNULL + Util.TI18N.PLATE_NO());
		check_map.put("INS_DATE", StaticRef.CHK_DATE + Util.TI18N.INS_DATE());
		check_map.put("INS_FROM", StaticRef.CHK_DATE + Util.TI18N.INS_FROM());
		check_map.put("INS_TO", StaticRef.CHK_DATE + Util.TI18N.INS_TO());
	}
	
	public DynamicForm createSerchForm(SGPanel form){
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGText INS_NO = new SGText("INS_NO",Util.TI18N.INS_NO(),true);
        SGText PLATE_NO = new SGText("PLATE_NO",Util.TI18N.PLATE_NO());
        SGCombo INS_TYPE = new SGCombo("INS_TYPE",Util.TI18N.INS_TYPE());
        Util.initCodesComboValue(INS_TYPE,"INS_TYP");
        SGCombo INS_CLS = new SGCombo("INS_CLS",Util.TI18N.INS_CLS());
        Util.initCodesComboValue(INS_CLS,"INS_CLS");
        SGCombo INS_COMPANY = new SGCombo("INS_COMPANY",Util.TI18N.INS_COMPANY(),true);
        Util.initCodesComboValue(INS_COMPANY,"INS_COMP");
        SGText INS_FROM = new SGText("INS_FROM",Util.TI18N.INS_FROM());
        Util.initDate(vm, INS_FROM);
        SGText INS_TO = new SGText("INS_TO",Util.TI18N.INS_TO());
        Util.initDate(vm, INS_TO);
        
        form.setItems(INS_NO,PLATE_NO,INS_TYPE,INS_CLS,INS_COMPANY,INS_FROM,INS_TO);
		return form;
	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		InsPurchaseRecordView view = new InsPurchaseRecordView();
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