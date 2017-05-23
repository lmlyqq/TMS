package com.rd.client.view.tms;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.DispatchLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 *  运输管理->配载结果查询
 * @author yuanlei 
 * @create time 2012-06-12 17:20
 *
 */
@ClassForNameAble
public class DispatchLogView extends SGForm implements PanelFactory {
	
	private SGTable table;//可用车型
	private Window searchWin;
	private SGPanel searchForm;
	private DataSource ds;
	
	private SectionStack section;
	private DynamicForm form;
	
	private IButton delBtn;
	
	/*public DispatchLogView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth100();
		
		ds = DispatchLogDS.getInstance("TRANS_DISPATCH_LOG","TRANS_DISPATCH_LOG");
		
		createTable();
	
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtn(toolStrip);
		main.addMember(toolStrip);

		main.addMember(createPage());
		return main;
	}
	
	/**
	 * 创建按钮
	 * @author yuanlei
	 * @param toolStrip
	 */
	public void createBtn(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN);

		toolStrip.addMember(searchButton);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {				
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds, //600 ,380
							createSearchForm(searchForm), section.getSection(0)).getViewPanel();
					searchWin.setHeight(350);
				} else {
					searchWin.show();
				}
			}

		});
		// 删除按钮
		delBtn = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.DISPATCHRESULT_P1);
		delBtn.addClickHandler(new DeleteAction(table));
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,delBtn);
	}
	
	public DynamicForm createSearchForm(SGPanel form){
		//1第一行：模糊查询
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(6);
		txt_global.setEndRow(true);
		
		
		//2第二行：
		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME_FROM", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		SGText SHPM_NO = new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		
		form.setItems(txt_global, SHPM_NO, ORD_ADDTIME_FROM, ORD_ADDTIME_TO);
		return form;
	}
	@Override
	public void createForm(DynamicForm form) {

	}
	@Override
	public void initVerify() {

	}
	
	/**
	 * 可用车型列表
	 * @author yuanl
	 */
	private void createTable(){
		
		table = new SGTable(ds, "100%","100%");
		table.setCanEdit(false);
		
		ListGridField EXEC_TIME = new ListGridField("EXEC_TIME", Util.TI18N.EXEC_TIME(), 150);
		
		ListGridField EXEC_SEQ = new ListGridField("EXEC_SEQ", Util.TI18N.EXEC_SEQ(), 90);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO", Util.TI18N.SHPM_NO(), 160);
		
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 550);
		
		table.setFields(EXEC_TIME, EXEC_SEQ, SHPM_NO, NOTES);
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
			}
		});
	}
	
	/**
	 * 可用车型页签
	 * @author yuanlei
	 * @return
	 */
	private SectionStack createPage(){
		
		// 主布局		
		section = new SectionStack();
		SectionStackSection listItem2 = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem2.setItems(table);
		listItem2.setExpanded(true);
		section.addSection(listItem2);
		form = new SGPage(table, true).initPageBtn();
		listItem2.setControls(form);
		section.setWidth100();
		section.setHeight100();
		
		return section;
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		table.destroy();
	}
	
	public DispatchLogView getThis(){
		return this;
	}

	@Override
	public void createBtnWidget(ToolStrip strip) {
		;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		DispatchLogView view = new DispatchLogView();
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