package com.rd.client.view.tmsinterface;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.TransactionLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
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
public class TransactionLogView extends SGForm implements PanelFactory{
	
	private DataSource mainDS;
	
	private SGTable headTable;
	
	private Window searchWin = null;
	private SGPanel searchForm;
	public ValuesManager vm;
	private SectionStack lst_section;

	/*public TransactionLogView(String id) {
		super(id);
	}*/

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
		mainDS = TransactionLogDS.getInstance("TRANS_TRANSACTION_LOG", "TRANS_TRANSACTION_LOG");
		
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
//		form.setAlign(Alignment.LEFT);
//		form.setNumCols(8);
//		form.setHeight100();
//		form.setWidth100();
		form.setCellPadding(2);
		
//		TextItem txt_global = new TextItem("FULL_INDEX", "模糊查询");
//		txt_global.setTitleOrientation(TitleOrientation.LEFT);
//		txt_global.setWidth(315);
//		txt_global.setColSpan(5);
//		txt_global.setEndRow(true);
//		txt_global.setTitleOrientation(TitleOrientation.TOP);
		
		//2行
		SGCombo DOC_TYP_NAME = new SGCombo("DOC_TYP", Util.TI18N.DOC_TYP());
 		Util.initCodesComboValue(DOC_TYP_NAME, "DOC_TYP", false);
 		
 		SGText DOC_NO = new SGText("DOC_NO", Util.TI18N.DOC_NO());
 		
 		SGDateTime ORD_ADDTIME_FROM=new SGDateTime("ADDTIME_FROM",Util.TI18N.ORD_ADDTIME_FROM(),true);
 		ORD_ADDTIME_FROM.setWidth(FormUtil.Width);
 		SGDateTime ORD_ADDTIME_TO=new SGDateTime("ADDTIME_TO","到");//创建时间
 		ORD_ADDTIME_TO.setWidth(FormUtil.Width);
		
 		SGCombo ACTION_TYPE=new SGCombo("ACTION_TYP","节点名称");
// 		Util.initCodesComboValue(ACTION_TYPE,"TRANSACTION_TYP",false);
 		Util.initComboValue(ACTION_TYPE, "BAS_CODES", "CODE", "NAME_C"," PROP_CODE='TRANSACTION_TYP' "," order by show_seq asc");
 		
        form.setItems( ACTION_TYPE,DOC_TYP_NAME, DOC_NO,ORD_ADDTIME_FROM,ORD_ADDTIME_TO);
        
        return form;
	}
	
	private ListGrid createTable(){
		headTable = new SGTable(mainDS, "100%", "92%", true, true, false);
		headTable.setCanEdit(false);
		headTable.setShowFilterEditor(false);
		
		//ListGridField DOC_TYP = new ListGridField("DOC_TYP", Util.TI18N.DOC_TYP(), 90);
		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.DOC_NO(), 150);
		ListGridField ADDTIME = new ListGridField("ADDTIME", Util.TI18N.ADDTIME(), 150);
		ListGridField ADDWHO = new ListGridField("ADDWHO", Util.TI18N.ADDWHO(), 100);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 540);
		headTable.setFields(DOC_NO, ADDTIME, ADDWHO, NOTES);
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
		TransactionLogView view = new TransactionLogView();
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
