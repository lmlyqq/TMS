package com.rd.client.view.tmsinterface;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.DateUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.LSCMLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class LSCMLogView extends SGForm implements PanelFactory {
	
	private DataSource mainDS;
	
	private SGTable headTable;
	
	private Window searchWin = null;
	private SGPanel searchForm;
	public ValuesManager vm;
	private SectionStack lst_section;

	/*public LSCMLogView(String id) {
		super(id);
	}*/

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.ROUTE);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(mainDS,
							createSerchForm(searchForm), lst_section.getSection(0),vm).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        toolStrip.addMember(searchButton);
	}

	@Override
	public void createForm(DynamicForm form) {
		
		
	}

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		//初始化主界面、按钮布局、Section布局
		VLayout main = new VLayout();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("100%");
		stack.setHeight100();
		mainDS = LSCMLogDS.getInstance("SFLSCM_LOG", "SFLSCM_LOG");
		
		//创建表格和数据源		
		createTable();
		
		lst_section = new SectionStack();
		final SectionStackSection lst_listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		lst_listItem.setItems(headTable);
		lst_listItem.setExpanded(true);
		lst_listItem.setControls(new SGPage(headTable, true).initPageBtn());
		lst_section.addSection(lst_listItem);
		lst_section.setWidth("100%");
		stack.addMember(lst_section);

		createBtnWidget(toolStrip);
		
		main.setWidth100();
        main.setHeight100();
        main.addMember(toolStrip);
		main.addMember(stack);
		
		final Menu menu = new Menu();
		menu.setWidth(140);
		menu.setVisible(true);
		
		MenuItem searchItem = new  MenuItem(StaticRef.FETCH_BTN,"Ctrl+Q");
		KeyIdentifier searchKey = new KeyIdentifier();
		searchKey.setCtrlKey(true);
		searchKey.setKeyName("Q");
		searchItem.setKeys(searchKey);
		menu.setItems(searchItem);
		
		return main;
	}
	
	//查询窗口
	public DynamicForm createSerchForm(SGPanel form){
		
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(8);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		
//		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
//		txt_global.setTitleOrientation(TitleOrientation.LEFT);
//		txt_global.setWidth(315);
//		txt_global.setColSpan(5);
//		txt_global.setEndRow(true);
//		txt_global.setTitleOrientation(TitleOrientation.TOP);
		
		//2行
		
		SGDateTime LGDATETIME_FROM = new SGDateTime("LGDATETIME_FROM", "日志时间");
		LGDATETIME_FROM.setDefaultValue(DateUtil.currentDate());
		LGDATETIME_FROM.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
		SGDateTime LGDATETIME_TO = new SGDateTime("LGDATETIME_TO", "到");
		LGDATETIME_TO.setDefaultValue(DateUtil.currentDate());
		LGDATETIME_TO.setDateFormatter(DateDisplayFormat.TOJAPANSHORTDATE);
		
		SGCombo LGINTERFACE=new SGCombo("INTERFACE_NAME","接口名称");
//        Util.initCodesComboValue(LGINTERFACE,"INTERFACE_NAME",true);
//        Util.initCodesComboValue(LGINTERFACE,"INTERFACE_NAME");
//        LGINTERFACE.setValueField("CODE");
		Util.initComboValue(LGINTERFACE, "BAS_CODES", "CODE", "NAME_C","PROP_CODE='INTERFACE_NAME'");
        SGCombo LGMODULE=new SGCombo("INTERFACE_TYPE","日志分类");
//        Util.initCodesComboValue(LGMODULE,"INTERFACE_TYPE",true);
        Util.initComboValue(LGMODULE, "BAS_CODES", "CODE", "NAME_C","PROP_CODE='INTERFACE_TYPE'");
        
        SGText LGATT1=new SGText("LGATT1","单号");
        LGATT1.setColSpan(4);
        form.setItems(LGINTERFACE,LGMODULE,LGATT1,LGDATETIME_FROM, LGDATETIME_TO);
        
        return form;
	}
	
	private ListGrid createTable(){
		headTable = new SGTable(mainDS, "100%", "92%", true, true, false);
		headTable.setCanEdit(false);
		
		//ListGridField DOC_TYP = new ListGridField("DOC_TYP", Util.TI18N.DOC_TYP(), 90);
		ListGridField LGMODULE = new ListGridField("LGMODULE", "模块", 150);
		ListGridField LGDATETIME = new ListGridField("LGDATETIME", "日志时间", 150);
		ListGridField LGMESSAGE = new ListGridField("LGMESSAGE", "备注", 660);
		headTable.setFields(LGMODULE, LGDATETIME, LGMESSAGE);
		return headTable;
	}
	

	@Override
	public void initVerify() {
		
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		LSCMLogView view = new LSCMLogView();
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
