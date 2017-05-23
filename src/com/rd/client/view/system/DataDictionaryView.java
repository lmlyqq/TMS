package com.rd.client.view.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.system.CodeDS;
import com.rd.client.ds.system.CodePropDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理->数据字典
 * @author yuanlei
 *
 */
@ClassForNameAble
public class DataDictionaryView extends SGForm implements PanelFactory {

	private DataSource mainDS;
	private DataSource detailDS;
	private SGTable propTable;
	private SGTable codeTable;
	private SectionStack stack;
	private Window searchWin;
	private SGPanel searchForm;
	private HashMap<String, String> detail_ck_map;
	private HashMap<String, String> detail_map;
	private HashMap<String, IButton> add_detail_map;
	private HashMap<String, IButton> save_detail_map;
	private HashMap<String, IButton> del_detail_map;
	@Override
	public void createForm(DynamicForm form) {
		
	}

	/*public DataDictionaryView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		//初始化主界面、按钮布局、Section布局
		VLayout main = new VLayout();
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		detail_map = new HashMap<String, String>();
		detail_ck_map = new HashMap<String, String>();
		
		//创建表格和数据源
		mainDS = CodePropDS.getInstance("BAS_CODEPROP", "BAS_CODEPROP");
		propTable = new SGTable(mainDS, "100%", "65%", false, true, false);
		propTable.setCanEdit(true);
		propTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		propTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record = event.getRecord();
				enableOrDisables(add_detail_map, true);
				Criteria findValues = new Criteria();
	            findValues.addCriteria("PROP_CODE", record.getAttribute("PROP_CODE"));
	            findValues.addCriteria("OP_FLAG", codeTable.OP_FLAG);
	            codeTable.PKEY = "PROP_CODE";
	            codeTable.PVALUE = record.getAttribute("PROP_CODE");
	            codeTable.fetchData(findValues, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						if(codeTable.getRecord(0) != null) {
							codeTable.selectRecord(codeTable.getRecord(0));
						}
					}
	            	
	            });
			}
			
		});
		propTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
		        enableOrDisables(save_map, true);
				
			}
		});
		
		propTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				
				enableOrDisables(del_map, true);
				enableOrDisables(add_detail_map, true);
				enableOrDisables(del_detail_map, false);
				enableOrDisables(save_detail_map, false);
				
			}
		});
		
		detailDS = CodeDS.getInstance("BAS_CODES", "BAS_CODES");
		codeTable = new SGTable(detailDS, "100%", "35%", false, true, false);
		codeTable.setCanEdit(true);
		codeTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		
		codeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_detail_map, false);
				enableOrDisables(del_detail_map, false);
		        enableOrDisables(save_detail_map, true);
				
			}
		});
		
		codeTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_detail_map,true);
				
			}
		});
		
		getConfigList();
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		Canvas[] widget = new Canvas[2];
		String[] title = new String[2];
		widget[0] = propTable;
		widget[1] = codeTable;
		title[0] = "字典主信息";
		title[1] = "字典内容";
		/*searchItem.setIcon(StaticRef.ICON_SEARCH);
		searchItem.setWidth(70);
		searchItem.setColSpan(1);
		searchItem.setStartRow(false);
		searchItem.setEndRow(false);*/
		stack = createUDFSection(widget, title, true, true);
		//创建分页
		//new PageUtil(stack.getSection(0), propTable, searchItem);
        
        main.setWidth100();
        main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(stack);
		
		initVerify();
		
		return main;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SysPrivRef.DICTIONARY);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(mainDS, createSerchForm(searchForm), stack.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.DICTIONARY_P0_01 );
        newButton.addClickHandler(new NewAction(propTable, codeTable, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.DICTIONARY_P0_02);
        saveButton.addClickHandler(new SaveAction(propTable, check_map,this,1));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.DICTIONARY_P0_03);
        delButton.addClickHandler(new DeleteAction(propTable, codeTable, "PROP_CODE",this));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.DICTIONARY_P0_04);
        canButton.addClickHandler(new CancelAction(propTable,codeTable,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SysPrivRef.DICTIONARY_P0_08);
        expButton.addClickHandler(new ExportAction(propTable, "SHOW_SEQ"));
        
        IButton newSubButton = createUDFBtn(Util.BI18N.NEWDETAIL(), StaticRef.ICON_NEW,SysPrivRef.DICTIONARY_P0_05);
        newSubButton.addClickHandler(new NewAction(codeTable, detail_map,this,2));
        
        IButton savSubButton = createUDFBtn(Util.BI18N.SAVEDETAIL(), StaticRef.ICON_SAVE,SysPrivRef.DICTIONARY_P0_06);
        savSubButton.addClickHandler(new SaveAction(codeTable, propTable,detail_ck_map,this,3));
        
        IButton delSubButton = createUDFBtn(Util.BI18N.REMOVEDETAIL(), StaticRef.ICON_DEL,SysPrivRef.DICTIONARY_P0_07);
        delSubButton.addClickHandler(new DeleteAction(codeTable,this,4));
        
        IButton canSubButton = createUDFBtn(Util.BI18N.CANCELDETAIL(), StaticRef.ICON_CANCEL,SysPrivRef.DICTIONARY_P0_07); 
        canSubButton.addClickHandler(new CancelAction(codeTable,this,5));
        
        //主表按钮联动
        add_map.put(SysPrivRef.DICTIONARY_P0_01, newButton);
        del_map.put(SysPrivRef.DICTIONARY_P0_03, delButton);
        save_map.put(SysPrivRef.DICTIONARY_P0_02, saveButton);
        save_map.put(SysPrivRef.DICTIONARY_P0_04, canButton);
//        save_map.put(SysPrivRef.DICTIONARY_P0_05, newSubButton);
//        save_map.put(SysPrivRef.DICTIONARY_P0_07, delSubButton);
        
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
        //从表按钮联动
        add_detail_map = new HashMap<String, IButton>();
        save_detail_map = new HashMap<String, IButton>();
        del_detail_map = new HashMap<String, IButton>();
        add_detail_map.put(SysPrivRef.DICTIONARY_P0_05, newSubButton);
        del_detail_map.put(SysPrivRef.DICTIONARY_P0_07, delSubButton);
        save_detail_map.put(SysPrivRef.DICTIONARY_P0_06, savSubButton);
        save_detail_map.put(SysPrivRef.DICTIONARY_P0_07, canSubButton);
        enableOrDisables(add_detail_map, false);
        enableOrDisables(del_detail_map, false);
        enableOrDisables(save_detail_map, false);
        
            
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, newSubButton, savSubButton, delSubButton,canSubButton);
	}
	
	private void getConfigList() {
		List<String> fldList  = new ArrayList<String>();
		List<String> titList  = new ArrayList<String>();
		List<String> widList = new ArrayList<String>();
		List<ListGridFieldType> typList = new ArrayList<ListGridFieldType>();
		
		String[] fields = {"PROP_CODE", "NAME_C", "NAME_E", "BIZ_TYPE", "ENABLE_FLAG", "SHOW_SEQ", "PARENT_PROP_ID", "UDF1", "UDF2"};
		fldList = Arrays.asList(fields);
		String[] titles = {ColorUtil.getRedTitle(Util.TI18N.PROP_CODE()),Util.TI18N.PROP_NAMEC(), Util.TI18N.PROP_NAMEE(), ColorUtil.getRedTitle(Util.TI18N.BIZ_TYPE()), Util.TI18N.ENABLE_FLAG()
				, Util.TI18N.SHOW_SEQ(), Util.TI18N.PARENT_PROP_ID(), Util.TI18N.PROP_UDF1(), Util.TI18N.PROP_UDF2()};
		titList = Arrays.asList(titles);
		
		String[] width = {"70", "120", "120", "70", "70", "60", "60", "70", "70"};
		widList = Arrays.asList(width);
		
		ListGridFieldType[] types = {null, null, null, null, ListGridFieldType.BOOLEAN, null, null, null, null};
		typList = Arrays.asList(types);
		createListField(propTable, fldList, titList, widList, typList);

		ListGridField biz_type = propTable.getField("BIZ_TYPE");
		Util.initCodesComboValue(biz_type, "SYS_TYP");
		
		fldList  = new ArrayList<String>();
		titList  = new ArrayList<String>();
		widList = new ArrayList<String>();
		typList = new ArrayList<ListGridFieldType>();		
		fields = new String[]{"CODE", "NAME_C", "NAME_E", "ENABLE_FLAG", "DEFAULT_FLAG", "SHOW_SEQ", "UDF1", "UDF2"};
		fldList = Arrays.asList(fields);
		titles = new String[]{ColorUtil.getRedTitle(Util.TI18N.CODE()),ColorUtil.getRedTitle(Util.TI18N.CODE_NAMEC()), Util.TI18N.CODE_NAMEE(), Util.TI18N.ENABLE_FLAG()
				, Util.TI18N.DEFAULT_FLAG(), Util.TI18N.SHOW_SEQ(), Util.TI18N.CODE_UDF1(), Util.TI18N.CODE_UDF2()};
		titList = Arrays.asList(titles);
		
		width = new String[]{"70", "120", "120" , "70", "70", "70", "70", "70"};
		widList = Arrays.asList(width);
		
		types = new ListGridFieldType[]{null, null, null, ListGridFieldType.BOOLEAN, ListGridFieldType.BOOLEAN, null, null, null};
		typList = Arrays.asList(types);

		createListField(codeTable, fldList, titList, widList, typList);
	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		/*form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);*/
		
		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		
		
		SGText txt_id = new SGText("PROP_CODE", Util.TI18N.PROP_CODE());
		SGText txt_name = new SGText("NAME_C", Util.TI18N.PROP_NAMEC());
		SGCombo txt_type = new SGCombo("BIZ_TYPE", Util.TI18N.BIZ_TYPE());
		Util.initCodesComboValue(txt_type, "SYS_TYP");
		txt_type.setTitleOrientation(TitleOrientation.TOP);
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(),true);
		chk_enable.setValue(true);
		
		form.setItems(txt_global, txt_id, txt_name, txt_type, chk_enable);
		
		return form;
	}
	
	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		propTable.destroy();
		codeTable.destroy();
		stack.destroy();
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_CODEPROP");
		
		check_map.put("PROP_CODE", StaticRef.CHK_NOTNULL + Util.TI18N.BIZ_TYPE());
		check_map.put("PROP_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.PROP_CODE());
		check_map.put("BIZ_TYPE", StaticRef.CHK_NOTNULL + Util.TI18N.BIZ_TYPE());
		
		detail_ck_map.put("TABLE", "BAS_CODES");
		
		detail_ck_map.put("CODE", StaticRef.CHK_NOTNULL + Util.TI18N.CODE());
		detail_ck_map.put("NAME_C", StaticRef.CHK_NOTNULL  + Util.TI18N.CODE_NAMEC());
		detail_ck_map.put("PROP_CODE,CODE", StaticRef.CHK_UNIQUE + Util.TI18N.PROP_CODE()+","+Util.TI18N.CODE());
		detail_ck_map.put("PROP_CODE,NAME_C", StaticRef.CHK_UNIQUE + Util.TI18N.PROP_CODE()+","+Util.TI18N.CODE_NAMEC());
		detail_ck_map.put("PROP_CODE,DEFAULT_FLAG", StaticRef.CHK_UNIQUE +Util.TI18N.PROP_CODE()+","+Util.TI18N.DEFAULT_FLAG());
		
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("MODIFY_FLAG", "N");
		
		detail_map.put("ENABLE_FLAG", "Y");
		detail_map.put("MODIFY_FLAG", "N");
	}
	
	@Override
	public void initBtn(int initBtn) {
		if(initBtn == 1){
			enableOrDisables(add_detail_map, true);
		}
		else if(initBtn == 2){
			enableOrDisables(add_detail_map, false);
			enableOrDisables(save_detail_map, true);
			enableOrDisables(del_detail_map, false);
		}
		else if(initBtn ==3){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			enableOrDisables(del_detail_map, true);
		}else if(initBtn ==4){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			enableOrDisables(del_detail_map, false);
		}else if(initBtn ==5){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			if(codeTable.getRecords().length > 0){
				enableOrDisables(del_detail_map, true);
			}else{
				enableOrDisables(del_detail_map, false);
			}
			codeTable.OP_FLAG = StaticRef.MOD_FLAG;
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		DataDictionaryView view = new DataDictionaryView();
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
