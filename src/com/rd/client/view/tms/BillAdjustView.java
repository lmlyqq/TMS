package com.rd.client.view.tms;

import java.util.HashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.tms.order.NewOrderItemAction;
import com.rd.client.action.tms.order.SaveOrderItemAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.BillAdjItemDS;
import com.rd.client.ds.tms.BillAdjustDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理->运输执行->调整单管理
 * @author 
 */
@ClassForNameAble
public class BillAdjustView extends SGForm implements PanelFactory {

	public SGTable table;
	public ListGrid itemTable;
	private DataSource ds;
	private DataSource detailDS;
	private Window searchWin;
	private SGPanel searchForm = new SGPanel();
	private SectionStack sectionStack;
	public Record selectRecord;
	public NewOrderItemAction newOrderItemAction;
	public SaveOrderItemAction saveOrderItemAction;
	public int hRow = 0;
	public HashMap<String, String> detail_ck_map;
	private  SectionStackSection  listItem;	
	private IButton createBillButton;
	private DynamicForm pageForm;
	
	/*public BillAdjustView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
	
		ds = BillAdjustDS.getInstance("TRANS_BILL_ADJUST","TRANS_BILL_ADJUST");
		detailDS = BillAdjItemDS.getInstance("TRANS_BILL_ADJITEM","TRANS_BILL_ADJITEM");
	
		initVerify();
		
		// 主布局
		TabSet chTabSet = new TabSet();  
		chTabSet.setWidth100();   
		chTabSet.setHeight100(); 
		chTabSet.setTabBarPosition(Side.LEFT);
		
		
		//创建正常订单的TAB
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();

		// 左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false){
			
	        	//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
	                VLayout layout = new VLayout(5);
	                
	                itemTable = new ListGrid();
	                itemTable.setDataSource(detailDS);
	                itemTable.setWidth("80%");
	                itemTable.setHeight(46);
	                itemTable.setCellHeight(22);
	                itemTable.setCanEdit(false);
	                itemTable.setAutoFitData(Autofit.VERTICAL);
	                
	                ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商名称",80);
	        		ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单号", 120);
	        		ListGridField LOAD_AREA_NAME = new ListGridField("LOAD_AREA_NAME", "发货区域",120);
	        		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME","收货区域",120);
	        		ListGridField FEE_NAME = new ListGridField("FEE_NAME","费用名称", 80);
	        		ListGridField FEE_BAS = new ListGridField("FEE_BAS", "计费基准", 100);
	        		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE", "基准值", 90);
	        		ListGridField PRICE = new ListGridField("PRICE", "单价", 90);
	        		ListGridField DUE_FEE = new ListGridField("DUE_FEE", "调整前费用", 120);
	        		ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT", "调整后费用", 120);
	        		ListGridField ADJUST_AMOUNT = new ListGridField("ADJUST_AMOUNT", "调整金额（不含税）",150);
	        		ListGridField TAX = new ListGridField("TAX", "税金", 90);
	        		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "调整金额（含税）", 150);
	        		ListGridField NOTES = new ListGridField("NOTES","调整原因)",100);
	        		
	        		itemTable.setFields(SUPLR_NAME, LOAD_NO, LOAD_AREA_NAME, UNLOAD_AREA_NAME, FEE_NAME,FEE_BAS,BAS_VALUE,
	        				PRICE,DUE_FEE,CONFIRM_AMOUNT,ADJUST_AMOUNT,TAX,TAX_AMOUNT,NOTES);
	        		
	        		Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("ADJUST_NO",record.getAttributeAsString("ADJUST_NO"));
					
					itemTable.fetchData(criteria);
	        		
	        		layout.addMember(itemTable);
	                layout.setLayoutLeftMargin(35);
					return layout;     
				}
		};

		createListField();
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);
		table.setConfirmDiscardEdits(false);
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		//addSplitBar(stack);



		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);

	


		main.addMember(toolStrip);
		main.addMember(stack);
		
		return main;
	}
	
	public void	createListField(){
				
	    ListGridField ADJUST_NO = new ListGridField("ADJUST_NO","调整单号",150);	
	    ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",120);	
	    ListGridField BELONG_MONETH = new ListGridField("BELONG_MONETH","所属期",150);	
	    ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初账单",120);
	    
	    ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认账单",100);	
	    ListGridField ADJUST_AMOUNT = new ListGridField("ADJUST_AMOUNT","调整金额(不含税)",150);	
	    ListGridField TAX = new ListGridField("TAX","税金",120);	
	    ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","调整金额(含税)",150);	
	    ListGridField ADJUST_REASON = new ListGridField("ADJUST_REASON","调整原因)",150);
				
				
		table.setFields(ADJUST_NO,SUPLR_NAME,BELONG_MONETH,INIT_AMOUNT,CONFIRM_AMOUNT,ADJUST_AMOUNT,TAX,TAX_AMOUNT,ADJUST_REASON);		
				
				
				
	};
	

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
					searchWin = new SearchWin(ds,
							createSearchForm(searchForm), sectionStack.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		//生成请款单
		createBillButton = createBtn(StaticRef.CREATE_BTN, TrsPrivRef.BillAdjust_P0_01);
		createBillButton.setTitle("生成请款单");
		//createBillButton.addClickHandler(new CloseOrderAction(table, 0));
        
		//导出
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.BillAdjust_P0_02);
        expButton.addClickHandler(new ExportAction(table,"addtime desc"));
        

		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, createBillButton, expButton);
	}

	protected DynamicForm createSearchForm(DynamicForm form) {
		
		form.setDataSource(ds);
		
		
		
		form.setItems();
		return form;
		
	}

	public void createForm(DynamicForm form) {

	}

	public void initVerify() {
//		check_map.put("TABLE", "TRANS_ORDER_HEADER");
//		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL + Util.TI18N.CUSTOMER_ID());
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}

	}

	
	
	
	

	
	

	
	@Override
	public void initAddBtn(){
		enableOrDisables(add_map, true);
		enableOrDisables(del_map, true);
		enableOrDisables(save_map, true);
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BillAdjustView view = new BillAdjustView();
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
