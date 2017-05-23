package com.rd.client.view.settlement;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillModifyLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 计费管理->费用修改日志
 * @author
 */
@ClassForNameAble
public class BillModifyLogView extends SGForm implements PanelFactory {

	public SGTable table;
	private DataSource ds;
	private Window searchWin;
	private SGPanel searchForm = new SGPanel();
	private SectionStack sectionStack;	
	private DynamicForm pageForm;
	private SectionStackSection  listItem;

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("99%");
		ds = BillModifyLogDS.getInstance("BILL_MODIFY_LOG","BILL_MODIFY_LOG");
				
		
		// 主布局
		
		VStack vsk = new VStack();
		vsk.setWidth100();
		vsk.setHeight100();
		
	

		// 列表
		table = new SGTable(ds, "100%", "100%", false, true, false);
		
		createListField();
		sectionStack = new SectionStack();
		listItem = new SectionStackSection("列表信息");
		listItem.setItems(table);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		
		main.setMembers(toolStrip,sectionStack);
		
		initVerify();
		
		return main;
	}
	

	private void createListField() {
	
		table.setShowRowNumbers(true);

		table.setCanEdit(false);
		ListGridField DOC_NO = new ListGridField("DOC_NO","单据编号", 120);
		ListGridField OPERATION_NAME = new ListGridField("OPERATION_NAME","操作类型", 80);
		ListGridField FROM_BASE = new ListGridField("FROM_BASE", "调整前基准值", 120);
		Util.initFloatListField(FROM_BASE, StaticRef.PRICE_FLOAT);
		ListGridField TO_BASE = new ListGridField("TO_BASE","调整后基准值",120);
		Util.initFloatListField(TO_BASE, StaticRef.PRICE_FLOAT);
		ListGridField FROM_PRICE = new ListGridField("FROM_PRICE", "调整前单价",100);
		Util.initFloatListField(FROM_PRICE, StaticRef.PRICE_FLOAT);
		ListGridField TO_PRICE = new ListGridField("TO_PRICE","调整后单价",100);
		Util.initFloatListField(TO_PRICE, StaticRef.PRICE_FLOAT);
		ListGridField FROM_AMOUNT=new ListGridField("FROM_AMOUNT","调整前金额",120);
		Util.initFloatListField(FROM_AMOUNT, StaticRef.PRICE_FLOAT);
		ListGridField TO_AMOUNT=new ListGridField("TO_AMOUNT","调整后金额",120);
		Util.initFloatListField(TO_AMOUNT, StaticRef.PRICE_FLOAT);
		ListGridField ADDTIME=new ListGridField("ADDTIME","调整时间",120);
		ListGridField ADDWHO=new ListGridField("ADDWHO","调整人",100);
		
		table.setFields(DOC_NO,OPERATION_NAME,FROM_BASE,TO_BASE,FROM_PRICE,TO_PRICE,FROM_AMOUNT,TO_AMOUNT,ADDTIME,ADDWHO);
		
	}


	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
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
					searchWin = new SearchWin(380,ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0),null).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		IButton exportButton = createBtn(StaticRef.EXPORT_BTN);
		exportButton.addClickHandler(new ExportAction(table));
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,exportButton);

		
	}
	
	
	
	
	
	protected DynamicForm createSearchForm(DynamicForm form) {
		
		form.setDataSource(ds);
		
		//第一行：模糊查询
		SGText DOC_NO=new SGText("DOC_NO", "单据编号");
		
		SGDateTime FROM_TIME = new SGDateTime("FROM_TIME","计费时间从");
		
		SGDateTime END_TIME = new SGDateTime("END_TIME","到");
		
		
		form.setItems(DOC_NO,FROM_TIME,END_TIME);
		
		return form;
		
	}

	public void initVerify() {
		
		check_map.put("TABLE", "BILL_MODIFY_LOG");
		check_map.put("ADDTIME", StaticRef.CHK_DATE+ "计费时间");
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}	
	public BillModifyLogView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BillModifyLogView view = new BillModifyLogView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}


	@Override
	public void createForm(DynamicForm form) {
		
	}
}
