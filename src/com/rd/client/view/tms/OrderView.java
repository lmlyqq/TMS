package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.DeleteOrderItemAction;
import com.rd.client.action.tms.order.CancelOrderHeaderAction;
import com.rd.client.action.tms.order.DeleteTransOrderAction;
import com.rd.client.action.tms.order.ExportDetailAction;
import com.rd.client.action.tms.order.FeeCalculateAction;
import com.rd.client.action.tms.order.GenerateOdrGroupAction;
import com.rd.client.action.tms.order.LoadPrintAction;
import com.rd.client.action.tms.order.NewOrderBillAction;
import com.rd.client.action.tms.order.NewOrderHeaderAction;
import com.rd.client.action.tms.order.NewOrderItemAction;
import com.rd.client.action.tms.order.OrdConfirmAction;
import com.rd.client.action.tms.order.OrdFrozenAction;
import com.rd.client.action.tms.order.OrdUnConfirmAction;
import com.rd.client.action.tms.order.SaveOrderBillAction;
import com.rd.client.action.tms.order.SaveOrderHeaderAction;
import com.rd.client.action.tms.order.SaveOrderItemAction;
import com.rd.client.action.tms.order.SetGroupWin;
import com.rd.client.common.action.AllSelectAction;
import com.rd.client.common.action.CopyAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.UnSelectAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.UserCustDS;
import com.rd.client.ds.tms.BillRecDS;
import com.rd.client.ds.tms.TranOrderDS;
import com.rd.client.ds.tms.TranOrderItemDS;
import com.rd.client.ds.tms.TransCustLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.AddrSfWin;
import com.rd.client.win.CountWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SkuWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理->订单管理->托运单管理
 * @author wangjun
 */
@ClassForNameAble
public class OrderView extends SGForm implements PanelFactory {

	public SGTable table;
	public ListGrid itemTable;
	private DataSource ds;
	private DataSource detailDS;
	private DataSource logDS;
	private DataSource billDS;
//	private boolean isMax = true;
	private Window searchWin;
	private Window uploadWin;
	public SGPanel searchForm = new SGPanel();
	public ValuesManager vm;
	private SGPanel basInfo;
	private SGPanel basInfo2;
//	private SGPanel basInfo4;
//	private SectionStack section;
	public SGTable groupTable;
	public SGTable groupTable2;
	public SGTable groupTable3;
	public SGTable groupTable4;
	private SectionStack sectionStack;
	public Record selectRecord;
	public Record customerRecord;
	public NewOrderItemAction newOrderItemAction;
	public SaveOrderItemAction saveOrderItemAction;
//	public int itemRow = 0;
	public int hRow = 0;
	public HashMap<String, String> detail_ck_map;
	private  SectionStackSection  listItem;
	
	public IButton newButton;
	public IButton saveButton;
	public IButton saveButton2;
	public IButton delButton;
	public IButton canButton;
	private IButton confirmBtn;
	private IButton cancelConBtn;
	public IButton loadBillButton;
	public IButton loadBillButton2;
	public IButton loadBillButton3;
	public IButton feeButton;
	private DynamicForm pageForm;
	public IButton copyButton;
	public IButton canlButton;
	public IButton copysButton;
	public IButton generateGroupButton;
	public HashMap<String, IButton> add_detail_map;
	public HashMap<String, IButton> del_detail_map;
	public HashMap<String, IButton> sav_detail_map;
	private VLayout layOut;
	//private int tabPageNum=0; //二级页签
	private String tabName = Util.TI18N.PRO_DETAIL();	
	private Window countWin;
	private ListGridField UOM;
	
	private SGCombo EXEC_STAT;
	private TextItem CUSTOMER_ID;
	//private ComboBoxItem LOAD_NAME;
	//private ComboBoxItem UNLOAD_NAME;
	/*public OrderView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
		vm = new ValuesManager();
		ds = TranOrderDS.getInstance("V_ORDER_HEADER","TRANS_ORDER_HEADER");
		detailDS = TranOrderItemDS.getInstance("TRANS_ORDER_ITEM","TRANS_ORDER_ITEM");
		billDS = BillRecDS.getInstance("V_BILL_REC","TRANS_BILL_RECE");
		logDS = TransCustLogDS.getInstance("V_CUSTOMACT_LOG");
		initVerify();
		
		// 主布局
		TabSet chTabSet = new TabSet();  
		chTabSet.setWidth100();   
		chTabSet.setHeight100(); 
		chTabSet.setTabBarPosition(Side.LEFT);
		
		
		//创建正常订单的TAB
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		// 左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false){
			
	        	//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
	                VLayout layout = new VLayout(5);
	                
	                itemTable = new ListGrid();
	                itemTable.setDataSource(detailDS);
	                itemTable.setWidth("45%");
	                itemTable.setHeight(46);
	                itemTable.setCellHeight(22);
	                itemTable.setCanEdit(false);
	                itemTable.setAutoFitData(Autofit.VERTICAL);
	                
	                ListGridField ROW = new ListGridField("ODR_ROW","行号",30);
	        		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 120);
	        		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 80);
	        		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1", Util.TI18N.TEMPERATURE(), 80);
	        		Util.initCodesComboValue(TEMPERATURE1,"STOR_COND");
	        		final ListGridField UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 50);
	        		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 70);
	        		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 90);
	        		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 90);
	        		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 90);

	        		//Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
	        		//Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
	        		//Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);

	        		itemTable.setFields(ROW, SKU, SKU_NAME, UOM, VOL,G_WGT,QNTY, TEMPERATURE1,NOTES);
	        		
	        		Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("ODR_NO",record.getAttributeAsString("ODR_NO"));
					
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
		//table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		addSplitBar(stack);

		stack.getMember(1).addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				tabChange(Util.TI18N.PRO_DETAIL());
			}
		});
		//创建正常订单结束		
		
		// 右边布局
		// 一
		SectionStack mainSection = createMainInfo();
		// 二
		TabSet bottoTabSet = new TabSet();
		bottoTabSet.setWidth("100%");
		bottoTabSet.setHeight("55%");
		bottoTabSet.setMargin(1);

		createbottoInfo();
		
		VLayout orderItem = new VLayout(); //订单明细栏
		orderItem.setWidth100();
		orderItem.addMember(groupTable);
		orderItem.addMember(createBtnWidgetDetail());
//		orderItem.addMember(totalPanel);
		if(isPrivilege(TrsPrivRef.ODRCREATE_P2)) {
		  Tab tab1 = new Tab(Util.TI18N.PRO_DETAIL());
		  tab1.setPane(orderItem);
		  bottoTabSet.addTab(tab1);
		}
		if(isPrivilege(TrsPrivRef.ODRCREATE_P3)) {
//		  Tab tab2 = new Tab(Util.TI18N.CAP_DEMAND());
//		  tab2.setPane(createbottoInfo2());
//		  bottoTabSet.addTab(tab2);
		}
		if(isPrivilege(TrsPrivRef.ODRCREATE_P4)) {
		  createbottoInfo3();
		  VLayout orderItema = new VLayout(); //订单费用明细栏
		  orderItema.setWidth100();
		  orderItema.addMember(groupTable2);
		  orderItema.addMember(createBtnWidgetDetailF());
		  Tab tab3 = new Tab(Util.TI18N.EXP_DETAIL());
		  tab3.setPane(orderItema);
		  bottoTabSet.addTab(tab3);
		}
		//if(isPrivilege(TrsPrivRef.ODRCREATE_P5)) {
		  //Tab tab4 = new Tab(Util.TI18N.COMPLAINT());
		  // tab4.setPane(createbottoInfo4());
		  //bottoTabSet.addTab(tab4);
		//}
		if(isPrivilege(TrsPrivRef.ODRCREATE_P6)) {
		  createbottoInfo5();
		  Tab tab5 = new Tab(Util.TI18N.BUS_DIARY());
		  tab5.setPane(groupTable3);
		  bottoTabSet.addTab(tab5);
		}
		//if(isPrivilege(TrsPrivRef.ODRCREATE_P7)) {
		// createbottoInfo6();
		//  Tab tab6 = new Tab(Util.TI18N.ORDER_HISTORY());
		//  tab6.setPane(groupTable4);
		//  bottoTabSet.addTab(tab6);
		//}
		bottoTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				//tabPageNum = event.getTabNum();
				tabName = event.getTab().getTitle();
				tabChange(tabName);
				
			}
		});
		
		vm.addMember(basInfo);
		vm.addMember(basInfo2);
		vm.setDataSource(ds);

	

		layOut = new VLayout();
		layOut.setWidth("80%");
		layOut.setHeight("100%");
		layOut.addMember(mainSection);
		layOut.addMember(bottoTabSet);
		layOut.setVisible(false);

		stack.addMember(layOut);
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		
		
		createBtnWidget(toolStrip);
		
		main.addMember(toolStrip);
		main.addMember(stack);
		

		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				if(StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS")) || StaticRef.SO_CONFIRM.equals(selectRecord.getAttribute("STATUS"))){
					if(StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
						enableOrDisables(add_map, false);
						enableOrDisables(del_map, false);
						enableOrDisables(save_map, true);
						enableOrDisables(add_detail_map, true);
						enableOrDisables(del_detail_map, false);
					}else{
						enableOrDisables(add_map, true);
						enableOrDisables(del_map, false);
						enableOrDisables(save_map, false);
						enableOrDisables(add_detail_map, false);
						enableOrDisables(del_detail_map, false);
					}
				}
				
				
				vm.getItem("CUSTOMER_NAME").setDisabled(true);
				
				/*Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID") + "'", "");
				Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
				Util.initComboValue(basInfo2.getItem("LOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
				
				Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID") + "'", "");
				Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
				Util.initComboValue(basInfo2.getItem("UNLOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
			
				//提货点联动
				
				Record record=event.getRecord();
				Criteria criteria = LOAD_NAME.getPickListCriteria();
				criteria.addCriteria("CUSTOMER_ID",record.getAttribute("CUSTOMER_ID"));
				LOAD_NAME.setPickListCriteria(criteria);
				//提货点联动
				
				Criteria criteria2 = UNLOAD_NAME.getPickListCriteria();
				criteria2.addCriteria("CUSTOMER_ID",record.getAttribute("CUSTOMER_ID"));
				UNLOAD_NAME.setPickListCriteria(criteria2);*/
				
				
				initAddBtn();
					
				if(isMax) {
					expend();
				}
			}
		});
		
		return main;
	}
	
	//页签切换
	/*private void tabChange(int num){
		
		if(StaticRef.MOD_FLAG.equals(vm.getValueAsString("OP_FLAG"))) {
			Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			if(selectRecord == null || 
					JSOHelper.getAttributeAsBoolean(newButton.getJsObj(), "newButtonClickFlag"))
				return;
			
			if(num == 0 && !this.isMax){
				groupTable.invalidateCache();
				criteria.addCriteria("ODR_NO",selectRecord.getAttributeAsString("ODR_NO"));
				groupTable.fetchData(criteria);
			}
			else if(num == 2) {
				groupTable2.invalidateCache();
				criteria.addCriteria("DOC_NO",selectRecord.getAttributeAsString("ODR_NO"));
				groupTable2.fetchData(criteria);
			}
	 		else if(num == 3){
	 			groupTable3.invalidateCache();
				criteria.addCriteria("DOC_TYP", "ODR_NO");
				criteria.addCriteria("DOC_NO", selectRecord.getAttributeAsString("ODR_NO"));
				groupTable3.fetchData(criteria);
			}
		}
	}*/
	
	private void tabChange(String tabName){
		
		if(StaticRef.MOD_FLAG.equals(vm.getValueAsString("OP_FLAG"))) {
			Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			if(selectRecord == null || 
					JSOHelper.getAttributeAsBoolean(newButton.getJsObj(), "newButtonClickFlag"))
				return;
			
			if(tabName.equals(Util.TI18N.PRO_DETAIL()) && !this.isMax){
				groupTable.invalidateCache();
				criteria.addCriteria("ODR_NO",selectRecord.getAttributeAsString("ODR_NO"));
				groupTable.fetchData(criteria);
			}
			else if(tabName.equals(Util.TI18N.EXP_DETAIL())) {
				groupTable2.invalidateCache();
				criteria.addCriteria("DOC_NO",selectRecord.getAttributeAsString("ODR_NO"));
				groupTable2.fetchData(criteria);
			}
	 		else if(tabName.equals(Util.TI18N.BUS_DIARY())){
	 			groupTable3.invalidateCache();
				criteria.addCriteria("DOC_TYP", "ODR_NO");
				criteria.addCriteria("DOC_NO", selectRecord.getAttributeAsString("ODR_NO"));
				groupTable3.fetchData(criteria);
			}
		}
	}

	private SectionStack createMainInfo() {

		basInfo = new SGPanel();
		basInfo.setNumCols(10);
		basInfo.setWidth("40%");
		basInfo.setCellPadding(0);
		/**
		 * 基本信息
		 * 
		 */
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		//1
		SGText ORDER_CODE=new SGText("ODR_NO", ColorUtil.getRedTitle(Util.TI18N.ODR_NO()),true);
		ORDER_CODE.setDisabled(true);
		
		CUSTOMER_ID = new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		final ComboBoxItem CUSTOMER_NAME = new ComboBoxItem("CUSTOMER_NAME", ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()));
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		
		final SGLText TRANS_DEMAND = new SGLText("TRANS_DEMAND", "客户运输要求");
		
		initCustomer(CUSTOMER_NAME, CUSTOMER_ID, TRANS_DEMAND);
		//客户运输要求根据客户联动
		final SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", ColorUtil.getRedTitle("订单类型"));
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		CUSTOMER_NAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(CUSTOMER_NAME.getValue())){
					//CUSTOMER_NAME.setValue("");
					TRANS_DEMAND.setValue("");
					BIZ_TYP.setValue("574037A56F7041428364041D1D4B2BA8");
				}
			}
		});
		//initDemand(CUSTOMER_NAME,TRANS_DEMAND);
		
		SGText STATUS = new SGText("STATUS","状态");
		STATUS.setVisible(false);
//		final SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_NAME", ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()));
//		Util.initCodesComboValue(CUSTOMER_NAME, "CUSTOMER_ID");
		CUSTOMER_NAME.setDisabled(true);
	
		SGCombo TRANS_TYPE = new SGCombo("ODR_TYP", ColorUtil.getRedTitle("运输类型"));
		Util.initCodesComboValue(TRANS_TYPE, "TRS_TYP");
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", ColorUtil.getRedTitle(Util.TI18N.EXEC_ORG_ID()));
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "40%");
		
		 DateTimeItem dateTimeField = new DateTimeItem("dateTimeItem", "Date Time");  
	     dateTimeField.setUseTextField(true);  
	     dateTimeField.setUseMask(true);
		
		//2
	    
	    SGText ODR_TIME = new SGText("ODR_TIME", "下单时间",true);
		ODR_TIME.setWidth(FormUtil.Width);
		Util.initDateTime(basInfo, ODR_TIME);
		SGText POD_TIME3 = new SGText("FROM_LOAD_TIME","要求发货时间");
		POD_TIME3.setWidth(FormUtil.Width);
		Util.initDateTime(basInfo, POD_TIME3);

		SGText POD_TIME4 = new SGText("PRE_LOAD_TIME", "到");
		POD_TIME4.setWidth(FormUtil.Width);
		Util.initDateTime(basInfo, POD_TIME4);

		SGText POD_TIME5 = new SGText("FROM_UNLOAD_TIME", "要求收货时间");
		POD_TIME5.setWidth(FormUtil.Width);
		Util.initDateTime(basInfo, POD_TIME5);
		SGText POD_TIME6 = new SGText("PRE_UNLOAD_TIME", "到");
		POD_TIME6.setWidth(FormUtil.Width);
		Util.initDateTime(basInfo, POD_TIME6);
		//3
		SGText REFENENCE1 = new SGText("REFENENCE1","运单号"); //内单号
//		REFENENCE1.setDisabled(true);
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户订单号",true);//运单号
		SGCombo VEHICLE_TYP = new SGCombo("VEHICLE_TYP","要求车型");
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE","ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'"," order by SHOW_SEQ ASC");
		final SGText TEMPERATURE1 = new SGText("TEMPERATURE1","温度下限");
		TEMPERATURE1.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(TEMPERATURE1.getValue())){
					String regex = "^(-|\\+)?\\d+$";
		        	if(!TEMPERATURE1.getValue().toString().matches(regex)){
		        		SC.say("温度从只能输入整数");
		        		return;
		        	}
				}
			}
		});
		
		final SGText TEMPERATURE2 = new SGText("TEMPERATURE2","温度上限");
		TEMPERATURE2.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(TEMPERATURE2.getValue())){
					String regex = "^(-|\\+)?\\d+$";
		        	if(!TEMPERATURE2.getValue().toString().matches(regex)){
		        		SC.say("温度到只能输入整数");
		        		return;
		        	}
				}
			}
		});
		
		final SGText DISCOUNT = new SGText("DISCOUNT", "重泡比");
		
		SGText TOT_WORTH = new SGText("GOODS_WORTH", "货值");
		
		SGCombo ORD_PRO_LEVER=new SGCombo("UGRT_GRD","优先级");//优先级
		Util.initCodesComboValue(ORD_PRO_LEVER, "UGRT_GRD");
		
	
		//5
		SGLText notes = new SGLText("NOTES", Util.TI18N.NOTES(),true);
		SGCheck UNLOAD_FLAG = new SGCheck("UNLOAD_FLAG", "需卸货",true);
		UNLOAD_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		UNLOAD_FLAG.setColSpan(1);
		SGCheck BUK_FLAG = new SGCheck("BUK_FLAG", "粗订单");
		BUK_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		BUK_FLAG.setColSpan(1);
		SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG());
		SLF_PICKUP_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		SLF_PICKUP_FLAG.setColSpan(1);
		SGCheck SLF_DELIVER_FLAG = new SGCheck("SLF_DELIVER_FLAG", Util.TI18N.SLF_DELIVER_FLAG());
		SLF_DELIVER_FLAG.setTitleOrientation(TitleOrientation.LEFT);
		SLF_DELIVER_FLAG.setColSpan(1);

		final SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", ColorUtil.getRedTitle(Util.TI18N.TRANS_SRVC_ID()));
		TRANS_SRVC_ID.setVisible(false);
		
		BIZ_TYP.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				Object value = event.getValue();
				if(value != null) {
					if(value.toString().equals(StaticRef.B2B)) {
						TRANS_SRVC_ID.setValue(StaticRef.TRANS_GX);
					}else if(value.toString().equals(StaticRef.B2C)) {
						TRANS_SRVC_ID.setValue(StaticRef.TRANS_PS);
					}else{
						TRANS_SRVC_ID.setValue(StaticRef.TRANS_LD);
					}
				}
			}
			
		});
		
		
		//从日期小于到日期
//		POD_TIME3.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
//			
//			@Override
//			public void onChanged(
//					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
//				Object dateTo = event.getValue();
//				Object dateFrom = ODR_TIME.getValue();
//				if(ObjUtil.isNotNull(dateTo) && ObjUtil.isNotNull(dateFrom)){
//					if(!DateUtil.isAfter(dateTo.toString(), dateFrom.toString())){
//						POD_TIME3.setValue(dateFrom.toString());
//					}
//				}
//				
//			}
//		});
//		Util.initDateFromTO(POD_TIME3, POD_TIME4);
		
		
		
//		SGText GOODS_WORTH = new SGText("GOODS_WORTH", Util.TI18N.GOODS_WORTH());	//总货值
		
		//双日期空间判断
//		Util.initDateFromTO(POD_TIME5, POD_TIME6);
//		//从日期小于到日期
//		POD_TIME5.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
//			
//			@Override
//			public void onChanged(
//					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
//				Object dateTo = event.getValue();
//				Object dateFrom = POD_TIME3.getValue();
//				if(ObjUtil.isNotNull(dateTo) && ObjUtil.isNotNull(dateFrom)){
//					if(!DateUtil.isAfter(dateFrom.toString(), dateTo.toString())){
//						POD_TIME5.setValue(dateFrom.toString());
//					}
//				}
//				
//			}
//		});
		
		basInfo.setItems(ORDER_CODE,CUSTOMER_NAME,BIZ_TYP,TRANS_TYPE,EXEC_ORG_ID_NAME,CUSTOM_ODR_NO,REFENENCE1,VEHICLE_TYP,
				TEMPERATURE1,TEMPERATURE2,ODR_TIME,POD_TIME3,POD_TIME4,POD_TIME5,POD_TIME6,TOT_WORTH,DISCOUNT,
				ORD_PRO_LEVER,TRANS_DEMAND,notes,UNLOAD_FLAG,BUK_FLAG,SLF_PICKUP_FLAG,SLF_DELIVER_FLAG,CUSTOMER_ID,
				EXEC_ORG_ID,TRANS_SRVC_ID,STATUS);
		
		
		/**
		 * 起止地信息
		 */

		//1
//		SGCombo AREA_ID = new SGCombo("LOAD_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()),true);
//		SGCombo AREA_ID2 = new SGCombo("LOAD_AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
//		SGCombo AREA_ID3 = new SGCombo("LOAD_AREA_ID3",Util.TI18N.AREA());
//		SGCombo LOAD_REGION = new SGCombo("LOAD_REGION",Util.TI18N.LOAD_REGION());
//		LOAD_REGION.setVisible(false);
//		SGText AREA_NAME = new SGText("LOAD_AREA_NAME",Util.TI18N.PROVINCE());
//      AREA_NAME.setVisible(false);
//      SGText AREA_NAME2 = new SGText("LOAD_AREA_NAME2",Util.TI18N.CITY());
//      AREA_NAME2.setVisible(false);
//      final SGText AREA_NAME3 = new SGText("LOAD_AREA_NAME3",Util.TI18N.AREA());
//      AREA_NAME3.setVisible(false);
//      TextItem LOAD_ID = new TextItem("LOAD_ID");
//		LOAD_ID.setVisible(false);
		//LOAD_NAME = new ComboBoxItem("LOAD_NAME", Util.TI18N.LOAD_NAME());
		//LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		//LOAD_NAME.setColSpan(4);
		//LOAD_NAME.setWidth(FormUtil.longWidth);
		
		//2
//		SGLText LOAD_ADDRESS = new SGLText("LOAD_ADDRESS", ColorUtil.getRedTitle(Util.TI18N.LOAD_ADDRESS()),true);
//		SGText LOAD_CONTACT = new SGText("LOAD_CONTACT", ColorUtil.getRedTitle(Util.TI18N.CONT_NAME()));
//		SGText LOAD_TEL = new SGText("LOAD_TEL", ColorUtil.getRedTitle(Util.TI18N.CONT_TEL()));
//		SGText LOAD_CRED = new SGText("LOAD_CRED","证件");
		
		//3
//		SGCombo AREA_ID4 = new SGCombo("UNLOAD_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()),true);
//		SGCombo AREA_ID5 = new SGCombo("UNLOAD_AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
//		SGCombo AREA_ID6 = new SGCombo("UNLOAD_AREA_ID3",Util.TI18N.AREA());
//		SGCombo UNLOAD_REGION = new SGCombo("UNLOAD_REGION",Util.TI18N.UNLOAD_REGION());
//		UNLOAD_REGION.setVisible(false);
//		SGText AREA_NAME4 = new SGText("UNLOAD_AREA_NAME",Util.TI18N.PROVINCE());
//      AREA_NAME4.setVisible(false);
//      SGText AREA_NAME5 = new SGText("UNLOAD_AREA_NAME2",Util.TI18N.CITY());
//      AREA_NAME5.setVisible(false);
//      final SGText AREA_NAME6 = new SGText("UNLOAD_AREA_NAME3",Util.TI18N.AREA());
//      AREA_NAME6.setVisible(false);
//      UNLOAD_NAME = new ComboBoxItem("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
//		UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
//		UNLOAD_NAME.setWidth(FormUtil.longWidth);
//		UNLOAD_NAME.setColSpan(4);
		
		//4
//		SGLText UNLOAD_ADDRESS = new SGLText("UNLOAD_ADDRESS", ColorUtil.getRedTitle(Util.TI18N.UNLOAD_ADDRESS()),true);
//		SGText UNLOAD_CONTACT = new SGText("UNLOAD_CONTACT", ColorUtil.getRedTitle(Util.TI18N.CONT_NAME()));
//		SGText UNLOAD_TEL = new SGText("UNLOAD_TEL", ColorUtil.getRedTitle(Util.TI18N.CONT_TEL()));
//		SGText UNLOAD_CRED = new SGText("LOAD_CRED","证件");
        
//      SGText LOAD_CODE = new SGText("LOAD_CODE", "发货方CODE");
//      LOAD_CODE.setVisible(false);
//		SGText UNLOAD_CODE = new SGText("UNLOAD_CODE","收货方CODE");
//		UNLOAD_CODE.setVisible(false);
		
		
		//初始化发货方下拉框
		//initLoadId(LOAD_NAME, LOAD_ADDRESS, LOAD_ID, AREA_ID, AREA_NAME, AREA_ID2, AREA_NAME2, AREA_ID3, AREA_NAME3,LOAD_CONTACT, LOAD_TEL,LOAD_REGION,LOAD_CODE);
        
		//ArrayList comboList = new ArrayList();
		//comboList.add(AREA_ID);
		//comboList.add(AREA_ID4);
		//SGCombo[] combos = (SGCombo[])comboList.toArray(new SGCombo[comboList.size()]);
		//Util.initComboValue(combos, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		
//		TextItem UNLOAD_ID = new TextItem("UNLOAD_ID");
//		UNLOAD_ID.setVisible(false);
		
		
		//初始化收货方下拉框
		//initUnLoadId(UNLOAD_NAME, UNLOAD_ADDRESS, UNLOAD_ID, AREA_ID4, AREA_NAME4, AREA_ID5, AREA_NAME5, AREA_ID6, AREA_NAME6, UNLOAD_CONTACT, UNLOAD_TEL,UNLOAD_REGION,UNLOAD_CODE);
		
		basInfo2 = new SGPanel();
		basInfo2.setWidth("40%");
		basInfo2.setNumCols(10);
		basInfo2.setCellPadding(1);
		basInfo2.setVisible(false);
		basInfo2.setItems();//隐藏字段
					
		CUSTOMER_NAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(CUSTOMER_NAME.getValue()) && 
						ObjUtil.isNotNull(vm.getValueAsString("CUSTOMER_ID"))){
						putMapToVM();
						
						custChanged(vm.getValueAsString("CUSTOMER_ID"), ObjUtil.ifNull(vm.getValueAsString("ODR_TYP"),LoginCache.getDefCustomer().get("DEFAULT_ORD_TYP")));
						//提货点联动
						/*Criteria criteria = LOAD_NAME.getPickListCriteria();
						criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
						LOAD_NAME.setPickListCriteria(criteria);
						//提货点联动
						Criteria criteria2 = UNLOAD_NAME.getPickListCriteria();
						criteria2.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
						UNLOAD_NAME.setPickListCriteria(criteria2);*/
						
						
						
				}
				
				if("A".equals(vm.getValueAsString("OP_FLAG")) && groupTable.getRecords().length == 0
						&& groupTable.getAllEditRows().length == 0 && ObjUtil.isNotNull(LoginCache.getDefCustomer().get("SKU_ID"))
						&& customerRecord == null){
					newOrderItemAction.create(true);
				}
			}
		});
		


		SectionStack section = new SectionStack();
		section.setVisible(true);
		section.setBackgroundColor(ColorUtil.BG_COLOR);

		// 1，基本信息
		SectionStackSection basicInfo = new SectionStackSection(Util.TI18N.BASE_MESSAGE());
		basInfo.setHeight("30%");
		basicInfo.addItem(basInfo);
		basicInfo.setAttribute("height", "22");
		basicInfo.setExpanded(true);
		section.addSection(basicInfo);
		// 2，起止点信息
		//SectionStackSection contact_Info = new SectionStackSection(Util.TI18N.START_END_MES());
		basInfo2.setHeight("0%");
		basicInfo.addItem(basInfo2);
		//contact_Info.setAttribute("height", "22");
		//contact_Info.setExpanded(false);
		//contact_Info.setAttribute("hidden", true);
		//section.addSection(contact_Info);

		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);

		return section;
	}

	private void createbottoInfo() {
		// 货品明细
		groupTable = new SGTable(detailDS);
		groupTable.setHeight("80%");
		groupTable.setShowFilterEditor(false);
		groupTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable.setShowRowNumbers(false);
		groupTable.setCanEdit(true);
		groupTable.addCellClickHandler(new CellClickHandler() {
			
			@Override
			public void onCellClick(CellClickEvent event) {
				groupTable.setProperty("selectedRowNum", event.getRowNum());
				
			}
		});
		groupTable.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if((selectRecord != null && 
						StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))) || 
						"A".equals(vm.getValueAsString("OP_FLAG"))){
					enableOrDisables(del_detail_map, true);
				}else{
					enableOrDisables(del_detail_map, false);
				}
			}
		});
		
		
		ListGridField ROW = new ListGridField("ODR_ROW","行号",30);
		ROW.setCanEdit(false);
		//ListGridField ID = new ListGridField("ID","ID");
		//ID.setHidden(true);
		ListGridField LOAD_ID = new ListGridField("LOAD_NAME",  ColorUtil.getRedTitle(Util.TI18N.LOAD_ID()), 120);
		
		ListGridField UNLOAD_ID = new ListGridField("UNLOAD_NAME",ColorUtil.getRedTitle("卸货点"), 120);
		
		ListGridField REQ_LOAD_TIME = new ListGridField("REQ_LOAD_TIME","要求发货时间", 115);
		Util.initDateTime(groupTable, REQ_LOAD_TIME);
		
		ListGridField REQ_UNLOAD_TIME = new ListGridField("REQ_UNLOAD_TIME","要求到货时间", 115);
		Util.initDateTime(groupTable, REQ_UNLOAD_TIME);
		
		ListGridField SKU_NAME = new ListGridField("SKU_NAME", ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()), 120);
		
		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 80);
		SKU.setCanEdit(false);
		
		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1",ColorUtil.getRedTitle("温区"), 55);
		Util.initCodesComboValue(TEMPERATURE1,"STOR_COND");
		
		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK(), 70);
		PACK_ID.setHidden(true);
		
		UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 50);
		
		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 55);
		QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.PACK_QTY()));
		
		QNTY.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(ObjUtil.isNotNull(event.getNewValue())){
					String regex = "^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])|([0])|([0]\\.\\d*)$";
		        	if(!event.getNewValue().toString().matches(regex)){
		        		SC.say("数量只能输入数字");
		        		return;
		        	}
				}
			}
		});
		
		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 70);
		VOL.setCanEdit(true);
		VOL.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(ObjUtil.isNotNull(event.getNewValue())){
					String regex = "^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])|([0])|([0]\\.\\d*)$";
		        	if(!event.getNewValue().toString().matches(regex)){
		        		SC.say("体积只能输入数字");
		        		return;
		        	}
				}
			}
		});
		
		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 70);
		G_WGT.setCanEdit(true);
		G_WGT.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(ObjUtil.isNotNull(event.getNewValue())){
					String regex = "^([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])|([0])|([0]\\.\\d*)$";
		        	if(!event.getNewValue().toString().matches(regex)){
		        		SC.say("毛重只能输入数字");
		        		return;
		        	}
				}
			}
		});
		
		ListGridField LOTATT01 = new ListGridField("LOTATT01", "批次属性", 70);
		LOTATT01.setHidden(true);
		LOTATT01.setCanEdit(true);
		
		ListGridField N_WGT = new ListGridField("N_WGT", "计费毛重", 90);
		N_WGT.setCanEdit(false);
		N_WGT.setHidden(true);
		
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 90);
		NOTES.setCanEdit(true);
		NOTES.setHidden(true);
		
		ListGridField VOL_GWT_RATIO = new ListGridField("VOL_GWT_RATIO", "体积毛重折算比例", 90);
		VOL_GWT_RATIO.setCanEdit(true);
		VOL_GWT_RATIO.setHidden(true);
		

		//Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		//Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		//Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
		//Util.initFloatListField(QNTY_EACH, StaticRef.QNTY_FLOAT);
		
		groupTable.setFields(ROW, LOAD_ID,UNLOAD_ID,REQ_LOAD_TIME,REQ_UNLOAD_TIME,SKU_NAME,QNTY,TEMPERATURE1,UOM,PACK_ID,VOL, G_WGT, N_WGT, NOTES, SKU,LOTATT01,VOL_GWT_RATIO);
		
		FormItemIcon icon = new FormItemIcon();
		LOAD_ID.setIcons(icon);
		LOAD_ID.setShowSelectedIcon(true);
		icon.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new AddrSfWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),vm.getItem("CUSTOMER_NAME").getValue().toString(),itemRow,"35%", "38%","AND_LOAD_FLAG").getViewPanel();
			}
		});
		
		LOAD_ID.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				final String load_id = ObjUtil.ifObjNull(event.getNewValue(),"").toString().toUpperCase();
				if(ObjUtil.isNotNull(load_id)) {
					Util.db_async.getRecord("ID,AREA_ID,AREA_NAME,ADDR_CODE,ADDR_NAME,AREA_ID2,AREA_NAME2,AREA_ID3,AREA_NAME3,ADDRESS,CONT_NAME,CONT_TEL,ADDR_CODE", "V_ADDRESS", 
							" where ENABLE_FLAG='Y' and LOAD_FLAG='Y' and (CUSTOMER_ID='"+vm.getValueAsString("CUSTOMER_ID")+"' or CUSTOMER_NAME IS NULL) and full_index like '%"+load_id+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							HashMap<String, String> selectRecord = null;
							if(size == 1 || selectRecord != null){
								if(selectRecord == null)selectRecord = result.get(0);
								groupTable.setEditValue(row, "LOAD_NAME", selectRecord.get("ADDR_NAME"));
								groupTable.setEditValue(row, "LOAD_ID", selectRecord.get("ID"));
								groupTable.setEditValue(row, "LOAD_AREA_ID", selectRecord.get("AREA_ID"));
								groupTable.setEditValue(row, "LOAD_AREA_NAME", selectRecord.get("AREA_NAME"));
								groupTable.setEditValue(row, "LOAD_AREA_ID2", selectRecord.get("AREA_ID2"));
								groupTable.setEditValue(row, "LOAD_AREA_NAME2", selectRecord.get("AREA_NAME2"));
								groupTable.setEditValue(row, "LOAD_AREA_ID3", selectRecord.get("AREA_ID3"));
								groupTable.setEditValue(row, "LOAD_AREA_NAME3", selectRecord.get("AREA_NAME3"));
								groupTable.setEditValue(row, "LOAD_ADDRESS", selectRecord.get("ADDRESS"));
								groupTable.setEditValue(row, "LOAD_CONTACT", selectRecord.get("CONT_NAME"));
								groupTable.setEditValue(row, "LOAD_TEL", selectRecord.get("CONT_TEL"));
								groupTable.setEditValue(row, "LOAD_CODE", selectRecord.get("ADDR_CODE"));
							}else if(size > 1){
								new AddrSfWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),vm.getItem("CUSTOMER_NAME").getValue().toString(),itemRow,"35%", "38%","AND_LOAD_FLAG",load_id).getViewPanel();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});
		
		FormItemIcon icon2 = new FormItemIcon();
		UNLOAD_ID.setIcons(icon2);
		UNLOAD_ID.setShowSelectedIcon(true);
		icon2.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new AddrSfWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),vm.getItem("CUSTOMER_NAME").getValue().toString(),itemRow,"35%", "38%","AND_RECV_FLAG").getViewPanel();
			}
		});
		
		UNLOAD_ID.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				final String unload_id = ObjUtil.ifObjNull(event.getNewValue(),"").toString().toUpperCase();
				if(ObjUtil.isNotNull(unload_id)) {
					Util.db_async.getRecord("ID,AREA_ID,AREA_NAME,ADDR_CODE,ADDR_NAME,AREA_ID2,AREA_NAME2,AREA_ID3,AREA_NAME3,ADDRESS,CONT_NAME,CONT_TEL,ADDR_CODE", "V_ADDRESS", 
							" where ENABLE_FLAG='Y' and RECV_FLAG='Y' and (CUSTOMER_ID='"+vm.getValueAsString("CUSTOMER_ID")+"' or CUSTOMER_NAME IS NULL) and full_index like '%"+unload_id+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							HashMap<String, String> selectRecord = null;
							if(size == 1 || selectRecord != null){
								if(selectRecord == null)selectRecord = result.get(0);
								groupTable.setEditValue(row, "UNLOAD_NAME", selectRecord.get("ADDR_NAME"));
								groupTable.setEditValue(row, "UNLOAD_ID", selectRecord.get("ID"));
								groupTable.setEditValue(row, "UNLOAD_AREA_ID", selectRecord.get("AREA_ID"));
								groupTable.setEditValue(row, "UNLOAD_AREA_NAME", selectRecord.get("AREA_NAME"));
								groupTable.setEditValue(row, "UNLOAD_AREA_ID2", selectRecord.get("AREA_ID2"));
								groupTable.setEditValue(row, "UNLOAD_AREA_NAME2", selectRecord.get("AREA_NAME2"));
								groupTable.setEditValue(row, "UNLOAD_AREA_ID3", selectRecord.get("AREA_ID3"));
								groupTable.setEditValue(row, "UNLOAD_AREA_NAME3", selectRecord.get("AREA_NAME3"));
								groupTable.setEditValue(row, "UNLOAD_ADDRESS", selectRecord.get("ADDRESS"));
								groupTable.setEditValue(row, "UNLOAD_CONTACT", selectRecord.get("CONT_NAME"));
								groupTable.setEditValue(row, "UNLOAD_TEL", selectRecord.get("CONT_TEL"));
								groupTable.setEditValue(row, "UNLOAD_CODE", selectRecord.get("ADDR_CODE"));
							}else if(size > 1){
								new AddrSfWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),vm.getItem("CUSTOMER_NAME").getValue().toString(),itemRow,"35%", "38%","AND_RECV_FLAG",unload_id).getViewPanel();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});
		
		FormItemIcon icon1 = new FormItemIcon();
		SKU_NAME.setIcons(icon1);
		SKU_NAME.setShowSelectedIcon(true);
		icon1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
//				if(skuWin != null){
//					skuWin.show();
//				}else{
					new SkuWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),itemRow,"40%", "38%",getThis(),null).getViewPanel();
//				}
			}
		});
		
		
		SKU_NAME.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				if(!ObjUtil.isNotNull(event.getNewValue())){
					return;
				}
				final String sku_name = ObjUtil.ifObjNull(event.getNewValue(),"").toString().toUpperCase();
				Util.db_async.getRecord("ID,SKU,TRANS_COND,SKU_CNAME,PACK_ID,TRANS_UOM,GROSSWEIGHT,VOLUME,NETWEIGHT,VOL_GWT_RATIO", "V_SKU", 
						" where (customer_id = '" + vm.getValueAsString("CUSTOMER_ID") + "' or common_flag = 'Y') and full_index like '%"+sku_name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						int size = result.size();
						HashMap<String, String> selectRecord = null;
						if(size == 1 || selectRecord != null){
							if(selectRecord == null)selectRecord = result.get(0);
							groupTable.setEditValue(row, "SKU_ID", selectRecord.get("ID"));
							groupTable.setEditValue(row, "SKU_NAME", selectRecord.get("SKU_CNAME"));
							groupTable.setEditValue(row, "SKU", selectRecord.get("SKU"));
							groupTable.setEditValue(row, "TEMPERATURE1", selectRecord.get("TRANS_COND"));
							groupTable.setEditValue(row, "PACK_ID", selectRecord.get("PACK_ID"));
							groupTable.setEditValue(row, "UOM", selectRecord.get("TRANS_UOM"));
							groupTable.setEditValue(row, "QNTY", "1");
							groupTable.setEditValue(row, "QNTY_EACH", "1");
							groupTable.setEditValue(row, "VOL", ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00"));
							groupTable.setEditValue(row, "G_WGT",ObjUtil.ifNull(selectRecord.get("GROSSWEIGHT"),"0.00"));
//							double nWgt = Double.valueOf(ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00")) * 
//								Double.valueOf(ObjUtil.ifNull(selectRecord.get("VOL_GWT_RATIO"),"0.00"));
							groupTable.setEditValue(row, "LOTATT01", ObjUtil.ifNull(selectRecord.get("LOTATT01"),""));
							groupTable.setEditValue(row, "N_WGT", ObjUtil.ifNull(selectRecord.get("N_WGT"),"0"));
							groupTable.setEditValue(row, "VOL_GWT_RATIO",ObjUtil.ifNull(selectRecord.get("VOL_GWT_RATIO"),"0.00"));
							JSOHelper.setAttribute(groupTable.getField("QNTY").getJsObj(), "QNTY_sourceValue"+row, "1");
							JSOHelper.setAttribute(groupTable.getField("G_WGT").getJsObj(), "G_WGT_sourceValue"+row, ObjUtil.ifNull(selectRecord.get("GROSSWEIGHT"),"0.00"));
							JSOHelper.setAttribute(groupTable.getField("VOL").getJsObj(), "VOL_sourceValue"+row, ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00"));
							com.smartgwt.client.widgets.grid.events.ChangedEvent.fire(PACK_ID, PACK_ID.getJsObj());
						}else if(size > 1){
							groupTable.setProperty("CUSTOMER_ID", vm.getValueAsString("CUSTOMER_ID"));
							groupTable.setProperty("FULL_INDEX", sku_name);
							new SkuWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),itemRow,"40%", "38%",getThis(),sku_name).getViewPanel();
						}else if(size == 0){
							groupTable.setEditValue(row, "SKU", "");
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
		});
		
		SKU_NAME.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				final int row = event.getRowNum();
				final String sku_name = ObjUtil.ifObjNull(event.getValue(),"").toString().toUpperCase();
				if(!ObjUtil.isNotNull(sku_name)){
					groupTable.setEditValue(row, "SKU", "");
					groupTable.setEditValue(row, "SKU_NAME", "");
					groupTable.setEditValue(row, "TEMPERATURE1","");
					groupTable.setEditValue(row, "PACK_ID", "");
					groupTable.setEditValue(row, "UOM", "");
					groupTable.setEditValue(row, "QNTY", "1");
					groupTable.setEditValue(row, "QNTY_EACH", "1");
					groupTable.setEditValue(row, "VOL", "0");
					groupTable.setEditValue(row, "G_WGT","0");
					groupTable.setEditValue(row, "LOTATT01", "");
					return;
				}
//				Util.db_async.getRecord("ID,SKU,TRANS_COND,SKU_CNAME,PACK_ID,TRANS_UOM,GROSSWEIGHT,VOLUME,NETWEIGHT,VOL_GWT_RATIO", "V_SKU", 
//						" where (customer_id = '" + vm.getValueAsString("CUSTOMER_ID") + "' or common_flag = 'Y') and full_index like '%"+sku_name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//
//							@Override
//							public void onFailure(Throwable caught) {
//								
//							}
//
//							@Override
//							public void onSuccess(ArrayList<HashMap<String, String>> result) {
//								int size = result.size();
//								HashMap<String, String> selectRecord = null;
//								if(size > 1){
//									for (HashMap<String, String> hashMap : result) {
//										if(hashMap.get("ID").equals(groupTable.getEditValue(row, "SKU_ID"))){
//											selectRecord = hashMap;
//											break;
//										}
//									}
//								}
//								if(size == 1 || selectRecord != null){
//									if(selectRecord == null)selectRecord = result.get(0);
//									groupTable.setEditValue(row, "SKU_ID", selectRecord.get("ID"));
//									groupTable.setEditValue(row, "SKU_NAME", selectRecord.get("SKU_CNAME"));
//									groupTable.setEditValue(row, "SKU", selectRecord.get("SKU"));
//									groupTable.setEditValue(row, "TEMPERATURE1", selectRecord.get("TRANS_COND"));
//									groupTable.setEditValue(row, "PACK_ID", selectRecord.get("PACK_ID"));
//									groupTable.setEditValue(row, "UOM", selectRecord.get("TRANS_UOM"));
//									groupTable.setEditValue(row, "QNTY", "1");
//									groupTable.setEditValue(row, "QNTY_EACH", "1");
//									groupTable.setEditValue(row, "VOL", ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00"));
//									groupTable.setEditValue(row, "G_WGT",ObjUtil.ifNull(selectRecord.get("GROSSWEIGHT"),"0.00"));
////									double nWgt = Double.valueOf(ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00")) * 
////										Double.valueOf(ObjUtil.ifNull(selectRecord.get("VOL_GWT_RATIO"),"0.00"));
//									groupTable.setEditValue(row, "LOTATT01", ObjUtil.ifNull(selectRecord.get("LOTATT01"),""));
//									groupTable.setEditValue(row, "N_WGT", ObjUtil.ifNull(selectRecord.get("N_WGT"),"0"));
//									groupTable.setEditValue(row, "VOL_GWT_RATIO",ObjUtil.ifNull(selectRecord.get("VOL_GWT_RATIO"),"0.00"));
//									JSOHelper.setAttribute(groupTable.getField("QNTY").getJsObj(), "QNTY_sourceValue"+row, "1");
//									JSOHelper.setAttribute(groupTable.getField("G_WGT").getJsObj(), "G_WGT_sourceValue"+row, ObjUtil.ifNull(selectRecord.get("GROSSWEIGHT"),"0.00"));
//									JSOHelper.setAttribute(groupTable.getField("VOL").getJsObj(), "VOL_sourceValue"+row, ObjUtil.ifNull(selectRecord.get("VOLUME"),"0.00"));
//									com.smartgwt.client.widgets.grid.events.ChangedEvent.fire(PACK_ID, PACK_ID.getJsObj());
//								}else if(size > 1){
//									groupTable.setProperty("CUSTOMER_ID", vm.getValueAsString("CUSTOMER_ID"));
//									groupTable.setProperty("FULL_INDEX", sku_name);
//									new SkuWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),itemRow,"40%", "38%",getThis(),sku_name).getViewPanel();
//								}else if(size == 0){
//									groupTable.setEditValue(row, "SKU", "");
//								}
//							}
//					
//				});
			}
		});
		
		
		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
		Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");
		UOM.setDefaultValue("件");
		
		PACK_ID.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				String value = "";
				FormItem item = event.getItem();
				if(item != null){
					value = ObjUtil.ifNull(item.getDisplayValue(), "");
				}
				if(!ObjUtil.isNotNull(value)){
					value = ObjUtil.ifObjNull(groupTable.getEditValue(itemRow, "PACK_ID"), "").toString();
					Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack=(select pack from bas_package where id = '"+value+"')", "");
				}else{
					Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='"+value+"'", "");
				}
			}
		});
		
		QNTY.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				Record r = event.getRecord();
				ListGridField QNTY_FIELD = groupTable.getField("QNTY");
				ListGridField VOL_FIELD = groupTable.getField("VOL");
				ListGridField G_WGT_FIELD = groupTable.getField("G_WGT");
				if(r == null){
					r = groupTable.getEditedRecord(row);
				}
				if(event.getNewValue() == null || r == null)
					return;
				String qty = event.getNewValue().toString();
				String oldQty = JSOHelper.getAttribute(QNTY_FIELD.getJsObj(), "QNTY_sourceValue"+row);
				String vol = JSOHelper.getAttribute(VOL_FIELD.getJsObj(), "VOL_sourceValue"+row);
				String gwgt = JSOHelper.getAttribute(G_WGT_FIELD.getJsObj(), "G_WGT_sourceValue"+row);
//				String vol_gwt_ratio = r.getAttribute("VOL_GWT_RATIO");
				if(!ObjUtil.isNotNull(oldQty)){
					oldQty = r.getAttribute("QNTY");
				}
				if("0".equals(oldQty.trim())){
					oldQty = "1";
				}
				if(!ObjUtil.isNotNull(vol)){
					vol = r.getAttribute("VOL");
					if(ObjUtil.isNotNull(vol)){
						vol = String.valueOf((Double.parseDouble(vol) / Double.parseDouble(oldQty)));
						JSOHelper.setAttribute(VOL_FIELD.getJsObj(), "VOL_sourceValue", vol);
					}else{
						return;
					}
				}
				if(!ObjUtil.isNotNull(gwgt)){
					gwgt = r.getAttribute("G_WGT");
					if(ObjUtil.isNotNull(gwgt)){
						gwgt = String.valueOf((Double.parseDouble(gwgt) / Double.parseDouble(oldQty)));
						JSOHelper.setAttribute(G_WGT_FIELD.getJsObj(), "G_WGT_sourceValue", gwgt);
					}else{
						return;
					}
				}
//				if(!ObjUtil.isNotNull(vol_gwt_ratio)){
//					vol_gwt_ratio = "0.0";
//				}
				double d_qty = Double.parseDouble(qty);
				double new_vol = Double.parseDouble(vol);
				double new_gwgt = Double.parseDouble(gwgt);
//				double volGwtRatio = Double.parseDouble(vol_gwt_ratio);
				new_vol =  d_qty * new_vol;
				new_gwgt = d_qty * new_gwgt;
//				double nWgt = new_vol * volGwtRatio;
				groupTable.setEditValue(row, "VOL", new_vol);
				groupTable.setEditValue(row, "G_WGT", new_gwgt);
//				groupTable.setEditValue(row, "N_WGT", nWgt);
				
			}
		});
		
		VOL.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				Object eValue = event.getValue();
				if(ObjUtil.isNotNull(eValue)){
//					Object volGwtRatio = groupTable.getEditValue(event.getRowNum(), "VOL_GWT_RATIO");
//					if(ObjUtil.isNotNull(volGwtRatio)){
//						double vol = Double.valueOf(eValue.toString());
//						double volGwt = Double.valueOf(volGwtRatio.toString());
//						double nGwt = vol * volGwt;
//						groupTable.setEditValue(event.getRowNum(), "N_WGT", nGwt);
//					}
				}
				
			}
		});
		
		groupTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				int row = groupTable.getRecordIndex(event.getRecord());
				JSOHelper.setAttribute(groupTable.getField("G_WGT").getJsObj(), "G_WGT_sourceValue"+row, "");
				JSOHelper.setAttribute(groupTable.getField("VOL").getJsObj(), "VOL_sourceValue"+row, "");
				JSOHelper.setAttribute(groupTable.getField("QNTY").getJsObj(), "QNTY_sourceValue"+row, "");
			}
		});

		groupTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
				if((selectRecord != null && 
						StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))) || 
						"A".equals(vm.getValueAsString("OP_FLAG"))){
					enableOrDisables(del_detail_map, true);
					if(groupTable.getSelectedRecord() != null && groupTable.getRecord(itemRow) == null){
						groupTable.deselectRecord(groupTable.getSelectedRecord());
					}
				}else{
					enableOrDisables(del_detail_map, false);
				}
			}
		});
		groupTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
//				if(!saveButton.isDisabled() && StaticRef.MOD_FLAG.equals(ObjUtil.ifNull(vm.getValueAsString("OP_FLAG"), StaticRef.MOD_FLAG))){
//					MSGUtil.sayError("订单主信息未保存，不能修改明细!");
//					return;
//				}
				
				if(selectRecord != null && StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
//					enableOrDisables(add_map, true);
//					enableOrDisables(del_map, true);
					enableOrDisables(add_detail_map, false);
					enableOrDisables(sav_detail_map, true);
					enableOrDisables(del_detail_map, false);
//					setButtonEnabled(TrsPrivRef.ODRCREATE_P1_04, canButton, true);
				}else if(selectRecord !=null && !StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
//					enableOrDisables(add_map, true);
//					enableOrDisables(del_map, true);
//					enableOrDisables(save_map, true);
					enableOrDisables(add_detail_map, false);
					enableOrDisables(sav_detail_map, false);
					enableOrDisables(del_detail_map, false);
				}
			}
		});
	}

	private void createbottoInfo3() {
		// 费用明细
		groupTable2 = new SGTable(billDS);
		groupTable2.setHeight("80%");
		groupTable2.setShowFilterEditor(false);
		groupTable2.setEditEvent(ListGridEditEvent.CLICK);
		groupTable2.setShowRowNumbers(true);
		groupTable2.setCanEdit(true);
		groupTable2.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				groupTable2.setProperty("selectedRowNum", event.getRowNum());
			}
		});
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 100);
		FEE_NAME.setHidden(true);
		ListGridField FEE_ID = new ListGridField("FEE_ID", Util.TI18N.FEE_NAME(), 100);
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " WHERE FEE_ATTR = '53EB6809BFCC436799F735AAE23658B9'", "");
		ListGridField FEE_BAS = new ListGridField("FEE_BAS",Util.TI18N.FEE_BASE(), 80);
		Util.initComboValue(FEE_BAS, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 80);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 80);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 80);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", Util.TI18N.DISCOUNT(), 80);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(), 80);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实收费用", 80);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 80);
		ListGridField TFF_NAME=new ListGridField("TFF_NAME",Util.TI18N.TFF_NAME(),100);
		TFF_NAME.setCanEdit(false);
		
		FEE_ID.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				FormItem item = event.getItem();
				int rowNum = event.getRowNum();
				if(item != null){
					groupTable2.setEditValue(rowNum, "FEE_NAME", item.getDisplayValue());
				}
			}
		});
		
		groupTable2.setFields(FEE_NAME, FEE_ID, FEE_BAS, BAS_VALUE,PRICE, PRE_FEE, DISCOUNT_RATE, DUE_FEE, PAY_FEE, NOTES,TFF_NAME);

	}


	private void createbottoInfo5() {
		// 业务日志
		groupTable3 = new SGTable(logDS);
		groupTable3.setShowFilterEditor(false);
		groupTable3.setShowRowNumbers(true);

		ListGridField OCCUR_TIME = new ListGridField("ADDTIME", Util.TI18N.OCCUR_TIME(), 110);
		ListGridField OPERATE_PERSON = new ListGridField("ADDWHO",Util.TI18N.OPERATE_PERSON(), 100);
		ListGridField OPERATE_RECODE = new ListGridField("NOTES",Util.TI18N.OPERATE_RECODE(), 200);
		ListGridField OPERATE_TIME = new ListGridField("OP_TIME",Util.TI18N.OPERATE_TIME(), 80);

		groupTable3.setFields(OCCUR_TIME, OPERATE_PERSON,OPERATE_RECODE, OPERATE_TIME);

	}
	
	private void createListField() {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				selectionChanged(event);
			}
		});
	

		table.setShowRowNumbers(true);

		/**
		 * 主列表显示的字段
		 * @param ORD_NO  托运单编号
		 * 托运单列表
		 */
		table.setCanEdit(false);
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_ORDER_HEADER托运单管理");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_ORDER_HEADER_ODR);
		createListField(table, listMap);
		ListGridField SLF_PICKUP_FLAG = table.getField("SLF_PICKUP_FLAG");
		if (SLF_PICKUP_FLAG != null) {
			SLF_PICKUP_FLAG.setType(ListGridFieldType.BOOLEAN);
		}
		ListGridField SLF_DELIVER_FLAG = table.getField("SLF_DELIVER_FLAG");
		if (SLF_DELIVER_FLAG != null) {
			SLF_DELIVER_FLAG.setType(ListGridFieldType.BOOLEAN);
		}
		ListGridField ODR_NO = table.getField("ODR_NO");
		if(ODR_NO != null){
			ODR_NO.setTitle(Util.TI18N.ODR_NO());
		}
		ListGridField TOT_QNTY = table.getField("TOT_QNTY");  
		if(TOT_QNTY != null) {
			Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		}
		/*ListGridField TOT_QNTY_EACH = table.getField("TOT_QNTY_EACH");  
		if(TOT_QNTY_EACH != null) {
			Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
		}*/
		
		if (isPrivilege(TrsPrivRef.ODRCREATE_P1_09)){
			final Menu menu = new Menu();
		    menu.setWidth(140);
		    MenuItemSeparator itemSeparator =new MenuItemSeparator();
		    
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_06)) {                                  //wangjun 2010-3-24
				   if(true){
					   MenuItem orderItem = new MenuItem("按区域排序",StaticRef.ICON_CONFIRM);
					   orderItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
						
						@Override
						public void onClick(MenuItemClickEvent event) {
							table.discardAllEdits();
							table.invalidateCache();
							Criteria criteria;
							if(searchForm != null){
								criteria = searchForm.getValuesAsCriteria();
								criteria.addCriteria("OP_FLAG","M");
							}else{
								criteria = new Criteria();
								criteria.addCriteria("OP_FLAG","M");
							}
							criteria.addCriteria("ORDER_BY_AREA","Y");
							table.fetchData(criteria,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									if(pageForm != null) {
										pageForm.getField("CUR_PAGE").setValue("1");
										LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
									}
									table.setSelectOnEdit(true);
									
									if(table.getRecords().length > 0){
										table.selectRecord(0);
									}
								}
							});
						}
					   });
					   menu.addItem(orderItem); 
				   }
				   
				   
			   }
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_07)){
		    	MenuItem setGroup = new MenuItem("多家配车",StaticRef.ICON_CONFIRM);
		    	setGroup.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					
					@Override
					public void onClick(MenuItemClickEvent event) {
						countWin = new SetGroupWin(table).getViewPanel();
						
					}
				});
		    	menu.addItem(setGroup);
		    }
		    
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_01)) {
		    	   menu.addItem(itemSeparator);
				   MenuItem countItem = new MenuItem("汇总",StaticRef.ICON_CONFIRM);
				   countItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					
					@Override
					public void onClick(MenuItemClickEvent event) {
						countWin = new CountWin(table).getViewPanel();
					}
				   });
				   menu.addItem(countItem); 
		   }
//		   if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_09)) {
//			   menu.addItem(itemSeparator);
//			   menu.addItem(itemSeparator);
//			   MenuItem detailExport = new MenuItem("导入",StaticRef.ICON_EXPORT);
//			   detailExport.addClickHandler(new ImportOrderAction(table));
//		       menu.addItem(detailExport);
//		   }
		   if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_08)) {
			   menu.addItem(itemSeparator);
			   MenuItem detailExport = new MenuItem("明细导出",StaticRef.ICON_EXPORT);
			   detailExport.addClickHandler(new ExportDetailAction(table));
		       menu.addItem(detailExport);
		   }
		    
		   if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_02)) {
			   menu.addItem(itemSeparator);
		       MenuItem allSelect = new MenuItem("全选",StaticRef.ICON_NEW);
		       allSelect.addClickHandler(new AllSelectAction(table));
		       menu.addItem(allSelect);
		       
		    } if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_03)) {
		       MenuItem unselect = new MenuItem("反选",StaticRef.ICON_CONFIRM);
		       unselect.addClickHandler(new UnSelectAction(table));
		       menu.addItem(unselect);
		    }
		   
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_04)) {
		       menu.addItem(itemSeparator);
		       MenuItem forzentem = new MenuItem("冻结",StaticRef.ICON_DEL);
		       forzentem.addClickHandler(new OrdFrozenAction(table, true));
		       menu.addItem(forzentem);
		    }
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_05)) {
		       MenuItem unforzenItem = new MenuItem("取消冻结",StaticRef.ICON_CANCEL);
		       unforzenItem.addClickHandler(new OrdFrozenAction(table, false));
		       menu.addItem(unforzenItem);
		    }
		    
		    if(isPrivilege(TrsPrivRef.ODRCREATE_P1_09_10)) {
		    	menu.addItem(itemSeparator);
		    	MenuItem addLoadItem = new MenuItem("加入调度单",StaticRef.ICON_NEW);
		    	addLoadItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					
					@Override
					public void onClick(MenuItemClickEvent event) {
						ListGridRecord selectedRecord = table.getSelectedRecord();
						if(selectedRecord == null)return;
						new MinWindow(selectedRecord);
					}
				});
		    	menu.addItem(addLoadItem);
		    }
		    table.addShowContextMenuHandler(new ShowContextMenuHandler() {
	            public void onShowContextMenu(ShowContextMenuEvent event) {
	            	menu.showContextMenu();
	                event.cancel();
	            }
	        });
		}
	}
	
	//选中记录变化
	protected void selectionChanged(SelectionEvent event){
		if(table.getSelectedRecord()==null) return;
		hRow = table.getRecordIndex(event.getRecord());
		Record record = event.getRecord();
		selectRecord = record;
		vm.editRecord(record);
		vm.setValue("OP_FLAG",StaticRef.MOD_FLAG);
		
		if(ObjUtil.isNotNull(record.getAttribute("BILL_PRICE_FLAG"))){
			if(Boolean.parseBoolean(record.getAttribute("BILL_PRICE_FLAG")) && 
					vm.getItem("BILL_PRICE") != null){
				vm.getItem("BILL_PRICE").setDisabled(false);
			}
		}
		
		tabChange(tabName);
		
//		addrDisabled(!ObjUtil.flagToBoolean(record.getAttribute("ADDR_EDIT_FLAG")));
		
		
		if(StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
			enableOrDisables(del_map, true);
			enableOrDisables(add_map, true);
			enableOrDisables(save_map, false);
			enableOrDisables(del_detail_map, false);
			enableOrDisables(sav_detail_map, false);
			enableOrDisables(add_detail_map, true);
			confirmBtnEnable(true, false);
		}else{
			if(StaticRef.SO_CONFIRM.equals(selectRecord.getAttribute("STATUS"))){
				confirmBtnEnable(false, true);
			}else{
				confirmBtnEnable(false, false);
				if(StaticRef.SO_CLOSED.equals(selectRecord.getAttribute("STATUS"))){
				}
			}
			enableOrDisables(del_map, false);
			enableOrDisables(add_map, true);
			enableOrDisables(save_map, false);
			enableOrDisables(del_detail_map, false);
			enableOrDisables(sav_detail_map,false);
			enableOrDisables(add_detail_map, false);
		}
		
//		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
//		if(ObjUtil.isNotNull(selectRecord.getAttribute("LOAD_AREA_ID2"))) {
//			map = new LinkedHashMap<String,String>();
//			map.put(selectRecord.getAttribute("LOAD_AREA_ID2"), selectRecord.getAttribute("LOAD_AREA_NAME2"));
//			basInfo2.getItem("LOAD_AREA_ID2").setValueMap(map);
//			basInfo2.getItem("LOAD_AREA_ID2").setValue(selectRecord.getAttribute("LOAD_AREA_ID2"));
//		}
//		else {
//			basInfo2.getItem("LOAD_AREA_ID2").setValue("");
//			basInfo2.getItem("LOAD_AREA_ID2").setValueMap("");
//		}
//		if(ObjUtil.isNotNull(selectRecord.getAttribute("LOAD_AREA_ID3"))) {
//			map = new LinkedHashMap<String,String>();
//			map.put(selectRecord.getAttribute("LOAD_AREA_ID3"), selectRecord.getAttribute("LOAD_AREA_NAME3"));
//			basInfo2.getItem("LOAD_AREA_ID3").setValueMap(map);
//			basInfo2.getItem("LOAD_AREA_ID3").setValue(selectRecord.getAttribute("LOAD_AREA_ID3"));
//		}
//		else {
//			basInfo2.getItem("LOAD_AREA_ID3").setValue("");
//			basInfo2.getItem("LOAD_AREA_ID3").setValueMap("");
//		}
//		if(ObjUtil.isNotNull(selectRecord.getAttribute("UNLOAD_AREA_ID2"))) {
//			map = new LinkedHashMap<String,String>();
//			map.put(selectRecord.getAttribute("UNLOAD_AREA_ID2"), selectRecord.getAttribute("UNLOAD_AREA_NAME2"));
//			basInfo2.getItem("UNLOAD_AREA_ID2").setValueMap(map);
//			basInfo2.getItem("UNLOAD_AREA_ID2").setDefaultValue(selectRecord.getAttribute("UNLOAD_AREA_ID2"));
//		}
//		else {
//			basInfo2.getItem("UNLOAD_AREA_ID2").setDefaultValue("");
//			basInfo2.getItem("UNLOAD_AREA_ID2").setValueMap("");
//		}
//		if(ObjUtil.isNotNull(selectRecord.getAttribute("UNLOAD_AREA_ID3"))) {
//			map = new LinkedHashMap<String,String>();
//			map.put(selectRecord.getAttribute("UNLOAD_AREA_ID3"), selectRecord.getAttribute("UNLOAD_AREA_NAME3"));
//			basInfo2.getItem("UNLOAD_AREA_ID3").setValueMap(map);
//			basInfo2.getItem("UNLOAD_AREA_ID3").setDefaultValue(selectRecord.getAttribute("UNLOAD_AREA_ID3"));
//		}
//		else {
//			basInfo2.getItem("UNLOAD_AREA_ID3").setDefaultValue("");
//			basInfo2.getItem("UNLOAD_AREA_ID3").setValueMap("");
//		}
//		if(!(vm.getItem("ODR_TIME") == null || 
//				ObjUtil.isNotNull(vm.getItem("ODR_TIME").getValue()))){
//			Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
//				
//				@Override
//				public void onSuccess(String result) {
//					vm.setValue("ODR_TIME", result);
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					
//				}
//			});
//		}
	    table.discardAllEdits();
	    groupTable.discardAllEdits();
	    groupTable2.discardAllEdits();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void enableOrDisables(HashMap tmps, boolean b) {
		if (tmps != null) {
			Iterator it = tmps.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry m=(Map.Entry)it.next();
				String key = (String)m.getKey();
				if (tmps.get(key) instanceof IButton) {
					IButton action = (IButton) tmps.get(key);
					setButtonEnabled(key, action, b);
					enableOrDisablesCallback(action);
				} else if (tmps.get(key) instanceof ButtonItem) {
					ButtonItem button = (ButtonItem) tmps.get(key);
					setButtonItemEnabled(key, button, b);
				}

			}
		}
	}
	
	public ToolStrip createBtnWidgetDetailF(){
		ToolStrip toolStripDetail = new ToolStrip();
		toolStripDetail.setAlign(Alignment.RIGHT);
		toolStripDetail.setWidth("100%");
		toolStripDetail.setHeight("20");
		toolStripDetail.setPadding(1);
		toolStripDetail.setSeparatorSize(5);
		
		//新增费用明细
		IButton createDetail = createUDFBtn(Util.BI18N.NEWDETAIL(), StaticRef.ICON_NEW,TrsPrivRef.ODRCREATE_P2_01);
		
		createDetail.addClickHandler(new NewOrderBillAction(groupTable2, this));
		
		IButton saveDetail = createUDFBtn(Util.BI18N.SAVEDETAIL(), StaticRef.ICON_SAVE,TrsPrivRef.ODRCREATE_P2_02);
		saveDetail.addClickHandler(new SaveOrderBillAction(groupTable2, this, detail_ck_map));
		//删除费用明细
		IButton delDetail = createUDFBtn(Util.BI18N.REMOVEDETAIL(), StaticRef.ICON_DEL,TrsPrivRef.ODRCREATE_P2_03);
		delDetail.addClickHandler(new DeleteAction(groupTable2));
		
		IButton canDetail = createUDFBtn(Util.BI18N.CANCELDETAIL(),StaticRef.ICON_CANCEL,TrsPrivRef.ODRCREATE_P2_04);
		canDetail.addClickHandler(new CancelOrderHeaderAction(this));
		
		feeButton = createUDFBtn("计费", StaticRef.ICON_CONFIRM, TrsPrivRef.ODRCREATE_P1_10);
		feeButton.addClickHandler(new FeeCalculateAction(table,this));
		
		toolStripDetail.setMembersMargin(4);
		toolStripDetail.setMembers(createDetail,saveDetail,delDetail,canDetail,feeButton);
		return toolStripDetail;
	}

	public ToolStrip createBtnWidgetDetail(){
		ToolStrip toolStripDetail = new ToolStrip();
		toolStripDetail.setAlign(Alignment.RIGHT);
		toolStripDetail.setWidth("100%");
		toolStripDetail.setHeight("20");
		toolStripDetail.setPadding(1);
		toolStripDetail.setSeparatorSize(5);
		
		//新增明细
		IButton createDetail = createUDFBtn(Util.BI18N.NEWDETAIL(), StaticRef.ICON_NEW,TrsPrivRef.ODRCREATE_P2_01);
		
		newOrderItemAction  = new NewOrderItemAction(groupTable, this);
		createDetail.addClickHandler(newOrderItemAction);
		
		IButton saveDetail = createUDFBtn(Util.BI18N.SAVEDETAIL(), StaticRef.ICON_SAVE,TrsPrivRef.ODRCREATE_P2_02);
		saveOrderItemAction = new SaveOrderItemAction(groupTable, this, detail_ck_map);
		saveDetail.addClickHandler(saveOrderItemAction);
		//删除明细
		IButton delDetail = createUDFBtn(Util.BI18N.REMOVEDETAIL(), StaticRef.ICON_DEL,TrsPrivRef.ODRCREATE_P2_03);
		delDetail.addClickHandler(new DeleteOrderItemAction(groupTable,this));
		
		IButton canDetail = createUDFBtn(Util.BI18N.CANCELDETAIL(),StaticRef.ICON_CANCEL,TrsPrivRef.ODRCREATE_P2_04);
		canDetail.addClickHandler(new CancelOrderHeaderAction(this));
		
		// 保存按钮
		saveButton2 = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ODRCREATE_P1_02);
		saveButton2.addClickHandler(new SaveOrderHeaderAction(table, vm, check_map, null,this));
		
		add_detail_map = new HashMap<String, IButton>();
		del_detail_map = new HashMap<String, IButton>();
		sav_detail_map = new HashMap<String, IButton>();

		add_detail_map.put(TrsPrivRef.ODRCREATE_P2_01, createDetail);
		del_detail_map.put(TrsPrivRef.ODRCREATE_P2_03, delDetail);
		sav_detail_map.put(TrsPrivRef.ODRCREATE_P2_02, saveDetail);
		sav_detail_map.put(TrsPrivRef.ODRCREATE_P2_04, canDetail);
		
		this.enableOrDisables(add_detail_map, false);
		this.enableOrDisables(sav_detail_map, false);
		this.enableOrDisables(del_detail_map, false);
		
		toolStripDetail.setMembersMargin(4);
		toolStripDetail.setMembers(saveButton2,createDetail,saveDetail,delDetail,canDetail);
		return toolStripDetail;
	}
	
	/**
	 * 设置BUTTON属性
	 */
	public void enableOrDisablesCallback(IButton button){
		String buttonName = button.getID();
		if(buttonName != null){
			if(saveButton2 !=null && buttonName.endsWith("_saveButton")){
				saveButton2.setDisabled(button.getDisabled());
				return;
			}
			if(JSOHelper.getAttributeAsBoolean(
					button.getJsObj(), "newButtonClickFlag")){
				JSOHelper.setAttribute(newButton.getJsObj(), "newButtonClickFlag", false);
				enableOrDisables(sav_detail_map, true);
			}
		}
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(1);
		toolStrip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN);

		toolStrip.addMember(searchButton);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(420,ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		//复制新增
//		copysButton = createBtn(StaticRef.COPY_BTN,TrsPrivRef.ODRCREATE_P1_14);
//		copysButton.addClickHandler(new CopyAction(null, this));
		
		// 新增按钮
		newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.ODRCREATE_P1_01);
		newButton.addClickHandler(new NewOrderHeaderAction(vm, cache_map,this));

		// 保存按钮
		saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ODRCREATE_P1_02);
		saveButton.setID(saveButton.getID()+"_saveButton");//标识
		saveButton.addClickHandler(new SaveOrderHeaderAction(table, vm, check_map, null,this));

		// 删除按钮
		delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.ODRCREATE_P1_03);
		delButton.addClickHandler(new DeleteTransOrderAction(table,vm,this));
		
		//关闭按钮
//		closeButton = createBtn(StaticRef.CANCEL_BTN, TrsPrivRef.ODRCREATE_P1_03);
//		closeButton.setTitle("关闭");
//        closeButton.addClickHandler(new CloseOrderAction(table));

		// 取消按钮
		canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ODRCREATE_P1_04);
		canButton.addClickHandler(new CancelOrderHeaderAction(this));
		
		
		
		confirmBtn = createUDFBtn("审核", StaticRef.ICON_SAVE,TrsPrivRef.ODRCREATE_P1_05);
		confirmBtn.addClickHandler(new OrdConfirmAction(table, vm,this));
		
		cancelConBtn = createUDFBtn("取消审核", StaticRef.ICON_CANCEL,TrsPrivRef.ODRCREATE_P1_06);
		cancelConBtn.addClickHandler(new OrdUnConfirmAction(table, vm,this));

		//打印冷运运单
	    loadBillButton = createUDFBtn("打印冷运运单", StaticRef.ICON_PRINT, TrsPrivRef.ODRCREATE_P1_07);
	    loadBillButton.setDisabled(true);
	    loadBillButton.addClickHandler(new LoadPrintAction(this, "coldTransport"));
	    
	    loadBillButton2=createUDFBtn("打印电子运单",StaticRef.ICON_PRINT,TrsPrivRef.ODRCREATE_P1_11);
	    loadBillButton2.setDisabled(true);
	    loadBillButton2.addClickHandler(new LoadPrintAction(this,"newDispatch"));
	    
	    loadBillButton3 = createUDFBtn("打印子件标贴", StaticRef.ICON_PRINT, TrsPrivRef.ODRCREATE_P1_12);
	    loadBillButton3.setDisabled(true);
	    loadBillButton3.addClickHandler(new LoadPrintAction(this, "coldTransport_label"));
	    
	    copyButton = createBtn(StaticRef.COPY_BTN,SettPrivRef.CUSTOM_RATE_P1_05);
	    copyButton.setVisible(false);
		copyButton.addClickHandler(new CopyAction(basInfo2, this));
		
		canlButton = createUDFBtn("订单关闭",StaticRef.ICON_CONFIRM,TrsPrivRef.ODRCREATE_P1_13);
		canlButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()==null){
					MSGUtil.sayError("未选择托运单信息");
					return;
				}
				if(table.getSelectedRecord().getAttribute("STATUS").equals("90")){
					MSGUtil.sayError("订单已关闭");
					return;
				}else{
					new OrderCanlWin(table);
				}
				
			}
			
		});
		
	    final IButton refresh = createUDFBtn("刷新",StaticRef.ICON_REFRESH);
	    refresh.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				table.discardAllEdits();
				table.invalidateCache();
				Criteria criteria;
				if(searchForm != null){
					criteria = searchForm.getValuesAsCriteria();
				}else{
					criteria = new Criteria();
				}
				if(criteria.getValues().isEmpty()){
					criteria.addCriteria("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
					criteria.addCriteria("EXEC_STAT","B41F05C6B85A4221813B833B103D2452");
					criteria.addCriteria("STATUS_FORM", 10);
					criteria.addCriteria("STATUS_TO", 10);
					criteria.addCriteria("C_ORG_FLAG", true);
					criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				}
				criteria.addCriteria("OP_FLAG","M");
				table.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						if(pageForm != null) {
							pageForm.getField("CUR_PAGE").setValue("1");
							pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
							pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
						}
						table.setSelectOnEdit(true);
						
						if(table.getRecords().length > 0){
							table.selectRecord(0);
						}
					}
				});
			}
		});
		
		add_map.put(TrsPrivRef.ODRCREATE_P1_01, newButton);
//		add_map.put(TrsPrivRef.ODRCREATE_P1_14, copysButton);
		del_map.put(TrsPrivRef.ODRCREATE_P1_03, delButton);
		save_map.put(TrsPrivRef.ODRCREATE_P1_02, saveButton);
		save_map.put(TrsPrivRef.ODRCREATE_P1_04, canButton);
		
		
		//按钮状态控制
		this.enableOrDisables(add_map, true);
		this.enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
		
		confirmBtnEnable(false, false);
		// 导出按钮
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.ODRCREATE_P1_08);
		expButton.addClickHandler(new ExportAction(table));
		
		// 导入按钮
		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, TrsPrivRef.ODRCREATE_P1_09_09);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					uploadWin = new UploadFileWin(refresh).getViewPanel("TMP_ORDER_IMPORT","SP_IMPORT_ORDER");
				}else{
					uploadWin.show();
				}
			}
		});
		
		generateGroupButton = createUDFBtn("生成订单组", StaticRef.ICON_SAVE, TrsPrivRef.ODRCREATE_P1_15);
		generateGroupButton.addClickHandler(new GenerateOdrGroupAction(table,this));

		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,refresh, newButton, saveButton, delButton,canButton,inputButton,
		confirmBtn,cancelConBtn,copyButton,canlButton,expButton,generateGroupButton);
	}

	protected DynamicForm createSearchForm(DynamicForm form) {
		
	
		form.setDataSource(ds);
		
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
//		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
//		CUSTOMER.setDisabled(true);
		 
		SGText ORDER_CODE=new SGText("ODR_NO",Util.TI18N.ORDER_CODE());//订单编号
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
	    //二级窗口 EXEC_ORG_ID 执行结构
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", "订单类型");
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		SGCombo ODR_TYP = new SGCombo("ODR_TYP", "运输类型");
		Util.initCodesComboValue(ODR_TYP, "TRS_TYP");
		
		//SGCombo TRANS_SRVC_ID= new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());//运输服务
		//Util.initTrsService(TRANS_SRVC_ID, "");
		//TRANS_SRVC_ID.setWidth(127);
		EXEC_STAT = new SGCombo("EXEC_STAT", Util.TI18N.STATUS());//执行状态
		Util.initCodesComboValue(EXEC_STAT,"EXEC_STAT",StaticRef.NORMAL);
		
		SGCombo ORD_PRO_LEVER = new SGCombo("UGRT_GRD", Util.TI18N.ORD_PRO_LEVER());
		Util.initCodesComboValue(ORD_PRO_LEVER, "UGRT_GRD");
		
		SGLText LOAD_NAME = new SGLText("LOAD_NAME", Util.TI18N.LOAD_NAME(),true);//发货方
		LOAD_NAME.setColSpan(4);
		
		SGLText UNLOAD_NAME = new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		UNLOAD_NAME.setColSpan(4);
		
		SGCombo ORDER_STATE_FROM = new SGCombo("STATUS_FORM", Util.TI18N.ORDER_STATE());//下单状态
		Util.initStatus(ORDER_STATE_FROM, StaticRef.ODRNO_STAT,"10");
		
		SGCombo ORDER_STATE_TO = new SGCombo("STATUS_TO", "到");//到
		Util.initStatus(ORDER_STATE_TO, StaticRef.ODRNO_STAT,"10");
		
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		ODR_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");//订单时间
		ODR_TIME_TO.setWidth(FormUtil.Width);
		
		
		SGDateTime ORD_ADDTIME_FROM=new SGDateTime("ADDTIME_FROM",Util.TI18N.ORD_ADDTIME_FROM());
		ORD_ADDTIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ORD_ADDTIME_TO=new SGDateTime("ADDTIME_TO","到");//创建时间
		ORD_ADDTIME_TO.setWidth(FormUtil.Width);
		
		SGDateTime PRE_LOAD_TIME_FROM=new SGDateTime("PRE_LOAD_TIME_FROM","要求发货时间");
		PRE_LOAD_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime PRE_LOAD_TIME_TO=new SGDateTime("PRE_LOAD_TIME_TO","到");//创建时间
		PRE_LOAD_TIME_TO.setWidth(FormUtil.Width);
		
		SGCombo MANAGE_STATE = new SGCombo("PLAN_STAT", Util.TI18N.PLAN_STAT());
		Util.initStatus(MANAGE_STATE, StaticRef.PLAN_STAT,"");
		
		SGCombo DISPATCH_STATE = new SGCombo("LOAD_STAT", Util.TI18N.LOAD_STAT());
		Util.initStatus(DISPATCH_STATE, StaticRef.LOAD_STAT,"");
		
		SGCombo ARRIVE_STATE = new SGCombo("UNLOAD_STAT", Util.TI18N.UNLOAD_STAT());
		Util.initStatus(ARRIVE_STATE, StaticRef.UNLOAD_STAT,"");
		
//		final TextItem LOAD_AREA_ID2=new TextItem("LOAD_AREA_ID2");
//		LOAD_AREA_ID2.setVisible(false);
//		ComboBoxItem LOAD_AREA_NAME2=new ComboBoxItem("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME());
//		LOAD_AREA_NAME2.setColSpan(1);
//		Util.initArea(LOAD_AREA_NAME2, LOAD_AREA_ID2);
//		LOAD_AREA_NAME2.setTitleAlign(Alignment.LEFT);
//		LOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		//收货区域
		final TextItem UNLOAD_AREA_ID2=new TextItem("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME2=new ComboBoxItem("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME2.setColSpan(1);
		Util.initArea(UNLOAD_AREA_NAME2, UNLOAD_AREA_ID2);
		UNLOAD_AREA_NAME2.setTitleAlign(Alignment.LEFT);
		UNLOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		SGText REFENENCE1 =new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
		
		SGText ADDWHO=new SGText("ADDWHO","制单人");//制单人
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		//C_ORG_FLAG.setStartRow(true);
		
		//SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG());	
		//SLF_PICKUP_FLAG.setValue(false);//包含下级机构
//		SLF_PICKUP_FLAG.setColSpan(2);
		
//		SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据*/
		
		//SGCombo BUK_FLAG = new SGCombo("BUK_FLAG", "SSS处理标识");
		//BUK_FLAG.setStartRow(true);
		//BUK_FLAG.setColSpan(2);
		//LinkedHashMap<String, String> bukValueMap = new LinkedHashMap<String, String>();
		//bukValueMap.put("", "  ");
		//bukValueMap.put("Y", "已处理");
		//bukValueMap.put("N", "未处理");
		//BUK_FLAG.setValueMap(bukValueMap);
		
		SGText UNLOAD_CONTACT = new SGText("UNLOAD_CONTACT", "收件人");
		SGText UNLOAD_TEL = new SGText("UNLOAD_TEL", Util.TI18N.CONT_TEL());
		
		form.setItems(CUSTOMER_ID, CUSTOMER_NAME,ORDER_CODE,CUSTOM_ODR_NO
				,EXEC_ORG_ID_NAME,
				BIZ_TYP,ODR_TYP,EXEC_STAT,ORD_PRO_LEVER,LOAD_NAME,UNLOAD_NAME,
				ODR_TIME_FROM,ODR_TIME_TO,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,PRE_LOAD_TIME_FROM,PRE_LOAD_TIME_TO,MANAGE_STATE,DISPATCH_STATE,ARRIVE_STATE,UNLOAD_CONTACT,
				ORDER_STATE_FROM,ORDER_STATE_TO,REFENENCE1,ADDWHO,EXEC_ORG_ID, UNLOAD_TEL, C_ORG_FLAG);
		return form;
		
	}

	public void createForm(DynamicForm form) {

	}

	public void initVerify() {
		check_map.put("TABLE", "TRANS_ORDER_HEADER");
		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL + Util.TI18N.CUSTOMER());
//		check_map.put("ODR_TIME", StaticRef.CHK_NOTNULL + Util.TI18N.ODR_TIME());
		check_map.put("TRANS_SRVC_ID", StaticRef.CHK_NOTNULL + Util.TI18N.TRANS_SRVC_ID());
		check_map.put("BIZ_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.BIZ_TYP());
		check_map.put("EXEC_ORG_ID", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("EXEC_ORG_ID_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		//check_map.put("LOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		//check_map.put("LOAD_AREA_ID2",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_NAME()+ Util.TI18N.CITY());
		//check_map.put("UNLOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		//check_map.put("UNLOAD_AREA_ID2",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_NAME()+ Util.TI18N.CITY());
		check_map.put("BILL_FLAG", StaticRef.CHK_NOTNULL + Util.TI18N.BILL_FLAG());
		check_map.put("ODR_TYP", StaticRef.CHK_NOTNULL + "运输类型");
//		check_map.put("CUSTOM_ODR_NO", StaticRef.CHK_UNIQUE + "客户单号");
		
		//check_map.put("LOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		//check_map.put("LOAD_AREA_NAME2",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_NAME()+ Util.TI18N.CITY());
		//check_map.put("UNLOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		//check_map.put("UNLOAD_AREA_NAME2",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_NAME()+ Util.TI18N.CITY());
		//check_map.put("LOAD_ADDRESS", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_ADDRESS());
		//check_map.put("UNLOAD_ADDRESS", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_ADDRESS());
		check_map.put("ODR_TIME", StaticRef.CHK_DATE + Util.TI18N.ODR_TIME());
		check_map.put("FROM_LOAD_TIME", StaticRef.CHK_DATE +"要求发货时间");
		check_map.put("PRE_LOAD_TIME", StaticRef.CHK_DATE + "到");
		check_map.put("PRE_AUDIT_TIME", StaticRef.CHK_DATE + Util.TI18N.PRE_AUDIT_TIME());
		check_map.put("AUDIT_TIME", StaticRef.CHK_DATE + Util.TI18N.AUDIT_TIME());
		check_map.put("FROM_UNLOAD_TIME", StaticRef.CHK_DATE + "要求收货时间");
		check_map.put("PRE_UNLOAD_TIME", StaticRef.CHK_DATE + "到");
//		check_map.put("FROM_POD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_POD_TIME());
		check_map.put("PRE_POD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_POD_TIME());
		//check_map.put("LOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_CONTACT());
		//check_map.put("LOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_TEL());
		//check_map.put("UNLOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_CONTACT());
		//check_map.put("UNLOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_TEL());
		
		
		detail_ck_map = new HashMap<String, String>();
		detail_ck_map.put("TABLE", "TRANS_ORDER_ITEM");
		detail_ck_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		detail_ck_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("LOAD_ID", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_NAME());
		detail_ck_map.put("UNLOAD_ID", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_NAME());
		detail_ck_map.put("TEMPERATURE1", StaticRef.CHK_NOTNULL + "温区");
		detail_ck_map.put("REQ_LOAD_TIME", StaticRef.CHK_DATE + "要求发货时间");
		detail_ck_map.put("REQ_UNLOAD_TIME", StaticRef.CHK_DATE + "要求到货时间");
		
		cache_map.put("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("CREATE_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("CREATE_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		cache_map.put("UGRT_GRD", StaticRef.UGRT_GRD);
		cache_map.put("POD_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_STAT", StaticRef.NORMAL);
		
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
		
		if(countWin != null){
			countWin.destroy();
		}
	}

	//根据当前登录用户初始化客户
	private void initCustomer(final ComboBoxItem customer_name,final TextItem customer_id,final TextItem trans_demand){
		DataSource custDS = UserCustDS.getInstance("VC_CUSTOMER");
		ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE",Util.TI18N.CUSTOMER_CODE(),80);
		ListGridField CUSTOM_ATTR = new ListGridField("CUSTOM_ATTR",Util.TI18N.CUSTOM_ATTR());
		CUSTOM_ATTR.setHidden(true);
		customer_name.setOptionDataSource(custDS);  
		customer_name.setDisabled(false);
		customer_name.setShowDisabled(false);
		customer_name.setDisplayField("FULL_INDEX");  
		customer_name.setPickListBaseStyle("myBoxedGridCell");
		customer_name.setPickListWidth(230);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("LOGIN_USER",LoginCache.getLoginUser().getUSER_ID());
		customer_name.setPickListCriteria(criteria);
		
		customer_name.setPickListFields(CUSTOMER_CODE, CUSTOMER_CNAME,CUSTOM_ATTR);
		customer_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				final Record selectedRecord  = customer_name.getSelectedRecord();
				customerRecord = selectedRecord;
				if(selectedRecord != null){
					
					/*if(ObjUtil.isNotNull(selectedRecord.getAttribute("LOAD_AREA_ID"))) {
						Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("LOAD_AREA_ID") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("LOAD_AREA_ID2"))) {
						Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("UNLOAD_AREA_ID"))) {
						Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("UNLOAD_AREA_ID") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("UNLOAD_AREA_ID2"))) {
						Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
					}*/
					customer_name.setValue(selectedRecord.getAttribute("CUSTOMER_NAME"));
					customer_id.setValue(selectedRecord.getAttribute("CUSTOMER_ID"));
					trans_demand.setValue(selectedRecord.getAttribute("TRANS_DEMAND"));
					vm.setValue("LOAD_AREA_ID", selectedRecord.getAttribute("LOAD_AREA_ID"));
					vm.setValue("LOAD_AREA_NAME", selectedRecord.getAttribute("LOAD_AREA_NAME"));
					vm.setValue("LOAD_AREA_ID2", selectedRecord.getAttribute("LOAD_AREA_ID2"));
					vm.setValue("LOAD_AREA_NAME2", selectedRecord.getAttribute("LOAD_AREA_NAME2"));
					vm.setValue("LOAD_AREA_ID3", selectedRecord.getAttribute("LOAD_AREA_ID3"));
					vm.setValue("LOAD_REGION", selectedRecord.getAttribute("LOAD_REGION"));
					vm.setValue("LOAD_AREA_NAME3", selectedRecord.getAttribute("LOAD_AREA_NAME3"));
					vm.setValue("LOAD_ID", selectedRecord.getAttribute("LOAD_ID"));
					vm.setValue("LOAD_NAME", selectedRecord.getAttribute("LOAD_NAME"));
					vm.setValue("LOAD_ADDRESS", selectedRecord.getAttribute("LOAD_ADDRESS"));
					vm.setValue("LOAD_CONTACT", selectedRecord.getAttribute("LOAD_CONTACT"));
					vm.setValue("LOAD_TEL", selectedRecord.getAttribute("LOAD_TEL"));
					vm.setValue("LOAD_CODE", selectedRecord.getAttribute("LOAD_CODE"));
					vm.setValue("UNLOAD_AREA_ID", selectedRecord.getAttribute("UNLOAD_AREA_ID"));
					vm.setValue("UNLOAD_AREA_NAME", selectedRecord.getAttribute("UNLOAD_AREA_NAME"));
					vm.setValue("UNLOAD_AREA_ID2", selectedRecord.getAttribute("UNLOAD_AREA_ID2"));
					vm.setValue("UNLOAD_AREA_NAME2", selectedRecord.getAttribute("UNLOAD_AREA_NAME2"));
					vm.setValue("UNLOAD_AREA_ID3", selectedRecord.getAttribute("UNLOAD_AREA_ID3"));
					vm.setValue("UNLOAD_REGION", selectedRecord.getAttribute("UNLOAD_REGION"));
					vm.setValue("UNLOAD_AREA_NAME3", selectedRecord.getAttribute("UNLOAD_AREA_NAME3"));
					vm.setValue("UNLOAD_ID", selectedRecord.getAttribute("UNLOAD_ID"));
					vm.setValue("UNLOAD_NAME", selectedRecord.getAttribute("UNLOAD_NAME"));
					vm.setValue("UNLOAD_ADDRESS", selectedRecord.getAttribute("UNLOAD_ADDRESS"));
					vm.setValue("UNLOAD_CONTACT", selectedRecord.getAttribute("UNLOAD_CONTACT"));
					vm.setValue("UNLOAD_TEL", selectedRecord.getAttribute("UNLOAD_TEL"));
					vm.setValue("UNLOAD_CODE", selectedRecord.getAttribute("LOAD_CODE"));
//					vm.setValue("SLF_DELIVER_FLAG", selectedRecord.getAttributeAsBoolean("SLF_DELIVER_FLAG"));
//					vm.setValue("SLF_PICKUP_FLAG", selectedRecord.getAttributeAsBoolean("SLF_PICKUP_FLAG"));
//					vm.setValue("POD_FLAG", selectedRecord.getAttributeAsBoolean("POD_FLAG"));
					vm.setValue("TRANS_SRVC_ID", selectedRecord.getAttribute("DEFAULT_TRANS_SRVC_ID"));
					vm.setValue("TRANS_SRVC_ID_NAME", selectedRecord.getAttribute("TRANS_SRVC_ID_NAME"));
//					vm.setValue("ODR_TYP", selectedRecord.getAttribute("DEFAULT_ORD_TYP"));
					vm.setValue("TRANS_UOM", selectedRecord.getAttribute("TRANS_UOM"));
//					vm.setValue("SALES_MAN", selectedRecord.getAttribute("FOLLOWUP"));
//					vm.setValue("WHSE_ID", selectedRecord.getAttribute("WHSE_ID"));
//					vm.setValue("UNIQ_CONO_FLAG", selectedRecord.getAttribute("UNIQ_CONO_FLAG"));
//					vm.setValue("REFENENCE3", selectedRecord.getAttribute("CUSTOM_ATTR"));
//					addrDisabled(!selectedRecord.getAttributeAsBoolean("ADDR_EDIT_FLAG"));S
//					if(!(ObjUtil.isNotNull(vm.getItem("LOAD_AREA_ID").getValue()) || 
//							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_NAME").getValue()) ||
//							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_ID2").getValue()) ||
//							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_NAME2").getValue()))){
//						initOrgAreaInfo();
//					}
					if("A".equals(vm.getValueAsString("OP_FLAG"))){
						groupTable.discardAllEdits();
						newOrderItemAction.create(true);				
					}
				}
			}
		});
	}
	
	public void initOrgAreaInfo(){
		//获取用户初始的组织机构
		Util.db_async.getOrgAreaInfo(LoginCache.getLoginUser().getDEFAULT_ORG_ID(), " and (area_level = '3' or area_level = '4')", new AsyncCallback<HashMap<String,String>>() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(HashMap<String, String> result) {
				Object[] iter = result.keySet().toArray();
				if(iter.length > 0) {
					String area_id = "", area_level = "";
					for(int i = 0; i < iter.length; i++) {
						area_id = (String)iter[i];
						area_level = result.get(area_id);
						if(area_level.equals("3")) {
							vm.getItem("LOAD_AREA_ID").setValue(area_id);
							Map<String, String> map = vm.getItem("LOAD_AREA_ID").getAttributeAsMap("valueMap");
							vm.getItem("LOAD_AREA_NAME").setValue(map.get(area_id));
							final String area_id2 = ((i+1) < iter.length) ? (String)iter[i+1]:null;
							Util.initComboValue(vm.getItem("LOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + area_id + "'", "", new AsyncCallback<LinkedHashMap<String,String>>() {
								@Override
								public void onFailure(
										Throwable caught) {
								}
								@Override
								public void onSuccess(
										LinkedHashMap<String, String> result) {
									if(area_id2 != null){
										Map<String, String> map = vm.getItem("LOAD_AREA_ID2").getAttributeAsMap("valueMap");
										if(ObjUtil.isNotNull(area_id2) && ObjUtil.isNotNull(map.get(area_id2))){
											vm.getItem("LOAD_AREA_ID2").setValue(area_id2);
											vm.getItem("LOAD_AREA_NAME2").setValue(map.get(area_id2));
										}
									}
									
								}
							}, true);
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				;
			}
		});
	}
	
//	private void addrDisabled(Boolean disabled){
//		vm.getItem("LOAD_AREA_NAME").setDisabled(disabled);
//		vm.getItem("UNLOAD_AREA_NAME").setDisabled(disabled);
//	}
	
//	//初始化提货点下拉框
//	//新方法：带省市区
//	private void initLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
//			,final SGCombo load_area_id,final TextItem load_area_name
//			,final SGCombo load_area_id2,final TextItem load_area_name2
//			,final SGCombo load_area_id3,final TextItem load_area_name3
//			,final SGText cont_name,final SGText cont_tel,final SGCombo load_region,final SGText load_code){
//		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
//		
//		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
//		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
//		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
//		ListGridField ADDRESS = new ListGridField("ADDRESS");
//		load_name.setOptionDataSource(ds2);  
//		load_name.setDisabled(false);
//		load_name.setShowDisabled(false);
//		load_name.setDisplayField("FULL_INDEX");  
//		load_name.setPickListBaseStyle("myBoxedGridCell");
//		load_name.setPickListWidth(450);
//	
//		Criteria criteria = new Criteria();
//		criteria.addCriteria("OP_FLAG","M");
//		criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
//		criteria.addCriteria("AND_LOAD_FLAG","Y");
//		criteria.addCriteria("ENABLE_FLAG","Y");
//		criteria.addCriteria("ISLIST","Y");
//		load_name.setPickListCriteria(criteria);
//		
//		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
//		load_name.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				Record selectedRecord  = load_name.getSelectedRecord();
//				if(selectedRecord != null){
//					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
//					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
//					Util.initComboValue(load_region, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
//					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
//					address.setValue(selectedRecord.getAttribute("ADDRESS"));
//					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
//					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
//					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
//					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
//					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
//					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
//					load_region.setValue(selectedRecord.getAttribute("AREA_ID3"));
//					load_id.setValue(selectedRecord.getAttribute("ID"));
//					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
//					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
//					load_code.setValue(selectedRecord.getAttribute("ADDR_CODE"));
//					vm.setValue("WHSE_ID", selectedRecord.getAttribute("WHSE_ID"));
//				}
//			}
//		});
//	}
//	
//	//初始化收货方下拉框
//	private void initUnLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
//			,final SGCombo load_area_id,final TextItem load_area_name
//			,final SGCombo load_area_id2,final TextItem load_area_name2
//			,final SGCombo load_area_id3,final TextItem load_area_name3
//			,final SGText cont_name,final SGText cont_tel,final SGCombo unload_region,final SGText unload_code){
//		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
//		
//		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
//		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
//		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
//		ListGridField ADDRESS = new ListGridField("ADDRESS");
//		load_name.setOptionDataSource(ds2);  
//		load_name.setDisabled(false);
//		load_name.setShowDisabled(false);
//		load_name.setDisplayField("FULL_INDEX");
//		load_name.setPickListBaseStyle("myBoxedGridCell");
//		load_name.setPickListWidth(450);
//	
//		Criteria criteria = new Criteria();
//		criteria.addCriteria("OP_FLAG","M");
//		criteria.addCriteria("AND_RECV_FLAG","Y");
//		criteria.addCriteria("ENABLE_FLAG","Y");
//		criteria.addCriteria("ISLIST","Y");
//		load_name.setPickListCriteria(criteria);
//		
//		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
//		load_name.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				Record selectedRecord  = load_name.getSelectedRecord();
//				if(selectedRecord != null){
//					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
//					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
//					Util.initComboValue(unload_region, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
//					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
//					address.setValue(selectedRecord.getAttribute("ADDRESS"));
//					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
//					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
//					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
//					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
//					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
//					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
//					unload_region.setValue(selectedRecord.getAttribute("AREA_ID3"));
//					load_id.setValue(selectedRecord.getAttribute("ID"));
//					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
//					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
//					unload_code.setValue(selectedRecord.getAttribute("ADDR_CODE"));
//					//vm.setValue("SLF_PICKUP_FLAG",ObjUtil.ifNull(selectedRecord.getAttribute("SELF_PKUP_FLAG"),"N"));
//				}
//			}
//		});
//	}
	
	public void putMapToVM(){
		HashMap<String, String> map = LoginCache.getDefCustomer();
		if(map != null && vm.getValueAsString("CUSTOMER_ID") != null && 
				vm.getValueAsString("CUSTOMER_ID").equals(map.get("CUSTOMER_ID"))) {
//			vm.setValue("CUSTOMER_ID", map.get("CUSTOMER_ID"));
//			vm.setValue("CUSTOMER_NAME", map.get("CUSTOMER_NAME"));
			vm.setValue("LOAD_AREA_ID", map.get("LOAD_AREA_ID"));
			vm.setValue("LOAD_AREA_NAME", map.get("LOAD_AREA_NAME"));
			vm.setValue("LOAD_ID", map.get("LOAD_ID"));
			vm.setValue("LOAD_NAME", map.get("LOAD_NAME"));
			vm.setValue("LOAD_ADDRESS", map.get("LOAD_ADDRESS"));
			vm.setValue("LOAD_CONTACT", map.get("LOAD_CONTACT"));
			vm.setValue("LOAD_TEL", map.get("LOAD_TEL"));
			vm.setValue("UNLOAD_AREA_ID", map.get("UNLOAD_AREA_ID"));
			vm.setValue("UNLOAD_AREA_NAME", map.get("UNLOAD_AREA_NAME"));
			vm.setValue("UNLOAD_ID", map.get("UNLOAD_ID"));
			vm.setValue("UNLOAD_NAME", map.get("UNLOAD_NAME"));
			vm.setValue("UNLOAD_ADDRESS", map.get("UNLOAD_ADDRESS"));
			vm.setValue("UNLOAD_CONTACT", map.get("UNLOAD_CONTACT"));
			vm.setValue("UNLOAD_TEL", map.get("UNLOAD_TEL"));
//			vm.setValue("SLF_DELIVER_FLAG", ObjUtil.flagToBoolean(map.get("SLF_DELIVER_FLAG")));
			vm.setValue("SLF_PICKUP_FLAG", ObjUtil.flagToBoolean(map.get("SLF_PICKUP_FLAG")));
			vm.setValue("POD_FLAG", ObjUtil.flagToBoolean(map.get("POD_FLAG")));
			vm.setValue("TRANS_SRVC_ID", map.get("DEFAULT_TRANS_SRVC_ID"));
			vm.setValue("TRANS_SRVC_ID_NAME", map.get("TRANS_SRVC_ID_NAME"));
//			vm.setValue("ODR_TYP", map.get("DEFAULT_ORD_TYP"));
			vm.setValue("TRANS_UOM", map.get("TRANS_UOM"));
			vm.setValue("SALES_MAN", map.get("FOLLOWUP"));
			vm.setValue("WHSE_ID", map.get("WHSE_ID"));
			vm.setValue("UNIQ_CONO_FLAG", map.get("UNIQ_CONO_FLAG"));//客户订单号唯一
//			addrDisabled(!ObjUtil.flagToBoolean(map.get("ADDR_EDIT_FLAG")));
			
		}
	}
	
	/**
	 *  确认/取消确认按钮可用状态控制
	 * @author Administrator
	 * @param b1
	 * @param b2
	 */
	public void confirmBtnEnable(boolean b1,boolean b2){
		if(b1){
//			confirmBtn.enable();
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_05,confirmBtn,true);//wangjun 2010-3-8
		}else{
//			confirmBtn.disable();
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_05,confirmBtn,false);
		}
		
		if(b2){
//			cancelConBtn.enable();
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_06,cancelConBtn,true);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_07,loadBillButton,true);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_11,loadBillButton2,true);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_12,loadBillButton3,true);
		}else{
//			cancelConBtn.disable();
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_06,cancelConBtn,false);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_07,loadBillButton,false);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_11,loadBillButton2,false);
			setButtonEnabled(TrsPrivRef.ODRCREATE_P1_12,loadBillButton3,false);
		}
	}
	
	private OrderView getThis(){
		return this;
	}
	
	/**
	 * 客户变化时联动变化订单类型
	 * @author fanglm
	 * @param cust_id
	 * @param val
	 */
	private void custChanged(String cust_id,String val){
		Util.initComboValue(vm.getItem("ODR_TYP"), "BAS_CUSTOMER_ORD_TYP", "ORD_TYP", "ORD_NAME", " where customer_id='" + cust_id + "'", "",val);
	}
	
	@Override
	public void initAddBtn(){
		enableOrDisables(add_map, false);
		enableOrDisables(del_map, false);
		enableOrDisables(save_map, true);
	} 
	
	@Override
	public void expend(String p_width) {
		super.expend(p_width);
		if(vm.getItem("ODR_TIME") == null)return;
//		if(!ObjUtil.isNotNull(vm.getItem("ODR_TIME").getValue())){
//			//当前服务器系统时间
//			Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
//				
//				@Override
//				public void onSuccess(String result) {
//					vm.setValue("ODR_TIME", result);
//				}
//				
//				@Override
//				public void onFailure(Throwable caught) {
//					
//				}
//			});
//		}
	}
	
	class MinWindow extends Window{
		
		private SGPanel panel;
		private TextItem text;
		private ListGridRecord selectedRecord;
		
		public MinWindow getThis(){
			return this;
		}
		
		public MinWindow(ListGridRecord selectedRecord){
			this.selectedRecord = selectedRecord;
			setTitle("加入调度单");
			setTop("50%");
			setLeft("50%");
			setWidth(280);
	        setHeight(100);
	        
			setCanDragResize(true);
			
			panel = new SGPanel();
			panel.setWidth100();
			panel.setHeight100();
			panel.setTitleWidth(75);
			panel.setNumCols(2);
			
			text = new TextItem("textValue", "调度单号:");
			text.setColSpan(2);
			text.setWidth(200);
			text.setStartRow(true);
			
			IButton submitButton = new IButton("确定");
				
			submitButton.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					Object loadNo = text.getValue();
					if(!ObjUtil.isNotNull(loadNo)){
						MSGUtil.sayWarning("调度单号不能为空!");
						return;
					}
					String odrNo = getThis().selectedRecord.getAttribute("ODR_NO");
					if(!ObjUtil.isNotNull(odrNo)){
						MSGUtil.sayWarning("托运单号为空!");
						return;
					}
					getThis().hide();
					String proName = "p_order_add_load(?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(odrNo);
					paramList.add(loadNo.toString());
					paramList.add(LoginCache.getLoginUser().getUSER_ID());
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if(result.startsWith(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
							}else{
								MSGUtil.sayError(result);
							}
						}
					});
				}
			});
			
			ToolStrip toolStrip = new ToolStrip();//按钮布局
			toolStrip.setWidth("100%");
			toolStrip.setHeight("20");
			toolStrip.setPadding(2);
			toolStrip.setSeparatorSize(8);
			toolStrip.setMembersMargin(5);
			toolStrip.setAlign(Alignment.RIGHT);
			toolStrip.setAlign(VerticalAlignment.BOTTOM);
	        toolStrip.setMembers(submitButton);
			

	        panel.setItems(text);
			
			addItem(panel);
			
			addItem(toolStrip);
			draw();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		OrderView view = new OrderView();
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
