package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.action.tms.order.CloseOrderAction;
import com.rd.client.action.tms.order.ExportDetailAction;
import com.rd.client.action.tms.order.NewOrderItemAction;
import com.rd.client.action.tms.order.OrdFrozenAction;
import com.rd.client.action.tms.order.SaveOrderItemAction;
import com.rd.client.action.tms.order.SetGroupWin;
import com.rd.client.common.action.AllSelectAction;
import com.rd.client.common.action.UnSelectAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.DateUtil;
import com.rd.client.common.util.ObjUtil;
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
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.base.UserCustDS;
import com.rd.client.ds.tms.BillRecDS;
import com.rd.client.ds.tms.TranOrderDS;
import com.rd.client.ds.tms.TranOrderItemDS;
import com.rd.client.ds.tms.TransCustLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.CountWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SkuWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
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
public class OrderCloseView extends SGForm implements PanelFactory {

	public SGTable table;
	public ListGrid itemTable;
	private DataSource ds;
	private DataSource detailDS;
	private DataSource logDS;
	private DataSource billDS;
//	private boolean isMax = true;
	private Window searchWin;
	private SGPanel searchForm = new SGPanel();
	public ValuesManager vm;
	private SGPanel basInfo;
	private SGPanel basInfo2;
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
	
	private IButton closeButton;
	private IButton openButton;
	private DynamicForm pageForm;
	
	public HashMap<String, IButton> add_detail_map;
	public HashMap<String, IButton> del_detail_map;
	public HashMap<String, IButton> sav_detail_map;
	
	private VLayout layOut;
	//private int tabPageNum=0; //二级页签
	private String tabName = Util.TI18N.PRO_DETAIL();
	
	private Window countWin;
	private ListGridField UOM;
	
	private SGCombo EXEC_STAT;
	
	/*public OrderCloseView(String id) {
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
		stack.setWidth100();
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
	        		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1_NAME", Util.TI18N.TEMPERATURE(), 80);
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
		sectionStack.setWidth("99%");
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
		bottoTabSet.setHeight("30%");
		bottoTabSet.setMargin(1);

		createbottoInfo();
		
		VLayout orderItem = new VLayout(); //订单明细栏
		orderItem.setWidth100();
		orderItem.addMember(groupTable);
		
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

		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);

		layOut = new VLayout();
		layOut.setWidth("80%");
		layOut.setHeight("100%");
		layOut.addMember(mainSection);
		layOut.addMember(bottoTabSet);
		layOut.setVisible(false);

		stack.addMember(layOut);
		main.addMember(toolStrip);
		main.addMember(stack);

		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				
				if(StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, true);
					enableOrDisables(save_map, true);
					enableOrDisables(add_detail_map, true);
					enableOrDisables(del_detail_map, true);
				}else{
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, true);
					enableOrDisables(save_map, true);
					enableOrDisables(add_detail_map, true);
					enableOrDisables(del_detail_map, true);
				}
				
				vm.getItem("CUSTOMER_NAME").setDisabled(true);
				
				Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID") + "'", "");
				Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
				Util.initComboValue(basInfo2.getItem("LOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
				
				Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID") + "'", "");
				Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
				Util.initComboValue(basInfo2.getItem("UNLOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
				
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

	@SuppressWarnings("unchecked")
	private SectionStack createMainInfo() {

		/**
		 * 基本信息
		 * 
		 */
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		// 1：订单编号，客户，下单时间，运输服务,客户单号,订单优先级
		SGText ORDER_CODE=new SGText("ODR_NO", Util.TI18N.ODR_NO());
		ORDER_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.ODR_NO()));
		ORDER_CODE.setDisabled(true);
		
		TextItem CUSTOMER_ID = new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME = new ComboBoxItem("CUSTOMER_NAME", Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		CUSTOMER_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()));
		initCustomer(CUSTOMER_NAME, CUSTOMER_ID);
		CUSTOMER_NAME.setDisabled(true);
		

		final SGDateTime ODR_TIME = new SGDateTime("ODR_TIME", Util.TI18N.ORDER_TIME());
		ODR_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.ORDER_TIME()));
		ODR_TIME.setVisible(false);

		final SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", ColorUtil.getRedTitle(Util.TI18N.TRANS_SRVC_ID()));
		TRANS_SRVC_ID.setVisible(false);
		Util.initTrsService(TRANS_SRVC_ID, "");

		SGCombo ORD_PRO_LEVER=new SGCombo("UGRT_GRD", Util.TI18N.ORD_PRO_LEVER());//订单优先级
		Util.initCodesComboValue(ORD_PRO_LEVER, "UGRT_GRD");
		
		// 2：客户单号、预留单号、业务类型、订单类型,执行机构，业务员
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), true);
		
		SGText REFENENCE1 = new SGText("REFENENCE1", Util.TI18N.REFENENCE1()); //运单号
		REFENENCE1.setDisabled(true);
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", ColorUtil.getRedTitle(Util.TI18N.BIZ_TYP()),false);
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		BIZ_TYP.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				Object value = event.getValue();
				if(value != null) {
					if(value.toString().equals(StaticRef.B2B)) {
						TRANS_SRVC_ID.setValue(StaticRef.TRANS_GX);
					}
					else if(value.toString().equals(StaticRef.B2C)) {
						TRANS_SRVC_ID.setValue(StaticRef.TRANS_PS);
					}
				}
			}
			
		});
		
		SGCombo ODR_TYP = new SGCombo("ODR_TYP", Util.TI18N.ODR_TYP());
		Util.initComboValue(ODR_TYP, "V_CUST_ORD_TYP", "ORD_TYP", "ORD_NAME");
		ODR_TYP.setVisible(false);

		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		//EXEC_ORG_ID_NAME.setWidth(125);
		EXEC_ORG_ID_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.EXEC_ORG_ID()));
		
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "40%");
		
		SGText DISCOUNT = new SGText("DISCOUNT", Util.TI18N.DISCOUNT());	//折扣
//		DISCOUNT.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				String regx = "(1(\\.[0]{0,2})?)|(0(\\.\\d{0,2}))";
//				Object o = event.getValue();
//				if(!(o == null || o.toString().matches(regx))){
//					SC.say("请输入正确的折扣格式且不能大于1");
//				}
//			}
//		});
		DISCOUNT.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				String regx = "(1(\\.[0]{0,2})?)|(0(\\.\\d{0,2}))";
				Object o = event.getItem().getValue();
				if(!(o == null || o.toString().matches(regx))){
					SC.say("请输入正确的折扣格式且不能大于1");
				}
			}
		});
		
		final SGCombo CUSTOM_ATTR = new SGCombo("REFENENCE3", Util.TI18N.CUSTOM_ATTR());	//客户属性
        Util.initCodesComboValue(CUSTOM_ATTR,"CUSTOM_ATTR");
        CUSTOM_ATTR.setVisible(false);
        
        SGCheck COD_FLAG = new SGCheck("COD_FLAG", "需要收款");
        COD_FLAG.setColSpan(2);
		
		//SGCombo SALES_MAN = new SGCombo("SALES_MAN", Util.TI18N.SALES_MAN());
		//Util.initComboValue(SALES_MAN, "BAS_STAFF", "ID", "STAFF_NAME", " where enable_flag='Y' and staff_typ='AC465D51BEAC4B969E9E0AAF214DE1D7'", "", "");
		SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG(), true);
		SLF_PICKUP_FLAG.setColSpan(2);
		
		SGCheck SLF_DELIVER_FLAG = new SGCheck("SLF_DELIVER_FLAG", Util.TI18N.SLF_DELIVER_FLAG());
		SLF_DELIVER_FLAG.setColSpan(2);
		
		final SGDateTime POD_TIME3 = new SGDateTime("FROM_LOAD_TIME", Util.TI18N.ORD_PLAN_TIME(),true);
		SGDateTime POD_TIME4 = new SGDateTime("PRE_LOAD_TIME", "到");
		
		//从日期小于到日期
		POD_TIME3.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Object dateTo = event.getValue();
				Object dateFrom = ODR_TIME.getValue();
				if(ObjUtil.isNotNull(dateTo) && ObjUtil.isNotNull(dateFrom)){
					if(!DateUtil.isAfter(dateFrom.toString(), dateTo.toString())){
						POD_TIME3.setValue(dateFrom.toString());
					}
				}
				
			}
		});
		Util.initDateFromTO(POD_TIME3, POD_TIME4);
		
		final SGDateTime POD_TIME5 = new SGDateTime("FROM_UNLOAD_TIME", Util.TI18N.FROM_UNLOAD_TIME());
		SGDateTime POD_TIME6 = new SGDateTime("PRE_UNLOAD_TIME", "到");
		
		//双日期空间判断
		Util.initDateFromTO(POD_TIME5, POD_TIME6);
		//从日期小于到日期
		POD_TIME5.addChangedHandler(new com.smartgwt.client.widgets.form.fields.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.form.fields.events.ChangedEvent event) {
				Object dateTo = event.getValue();
				Object dateFrom = POD_TIME3.getValue();
				if(ObjUtil.isNotNull(dateTo) && ObjUtil.isNotNull(dateFrom)){
					if(!DateUtil.isAfter(dateFrom.toString(), dateTo.toString())){
						POD_TIME5.setValue(dateFrom.toString());
					}
				}
				
			}
		});
		
		SGText BTCH_NUM = new SGText("BTCH_NUM", Util.TI18N.OREDER_GRO_ID());
		BTCH_NUM.setVisible(false);
		
		SGText REFENENCE2 = new SGText("REFENENCE2", Util.TI18N.REFENENCE2(),true);
		REFENENCE2.setVisible(false);
//		SGText REFENENCE3 = new SGText("REFENENCE3", Util.TI18N.REFENENCE3());
//		REFENENCE3.setVisible(false);
		
		TextItem CREATE_ORG_ID = new TextItem("CREATE_ORG_ID");
		CREATE_ORG_ID.setVisible(false);
		
		SGText CREATE_ORG_ID_NAME = new SGText("CREATE_ORG_ID_NAME", Util.TI18N.ORDER_ORG());
		CREATE_ORG_ID_NAME.setVisible(false);

		SGCheck POD_FLAG = new SGCheck("POD_FLAG", Util.TI18N.ORD_POD_FLAG());
		POD_FLAG.setVisible(false);

		SGText ADDWHO = new SGText("ADDWHO", Util.TI18N.ORDER_PER());
		ADDWHO.setVisible(false);

		SGDateTime TO_POD_TIME = new SGDateTime("PRE_POD_TIME", Util.TI18N.FROM_POD_TIME());
		TO_POD_TIME.setVisible(false);
		
		// 5：备注
		SGLText notes = new SGLText("NOTES", Util.TI18N.NOTES());
		notes.setColSpan(2);
		notes.setTitleVAlign(VerticalAlignment.TOP);
		
		basInfo = new SGPanel();
		basInfo.setTitleWidth(75);
		basInfo.setItems(ORDER_CODE,CUSTOMER_NAME,BIZ_TYP, ODR_TIME, TRANS_SRVC_ID,EXEC_ORG_ID_NAME,CUSTOM_ATTR, CUSTOM_ODR_NO,REFENENCE1,
				ODR_TYP, ORD_PRO_LEVER,DISCOUNT,POD_TIME3,POD_TIME4,POD_TIME5,POD_TIME6,BTCH_NUM,REFENENCE2,SLF_PICKUP_FLAG,SLF_DELIVER_FLAG,COD_FLAG,
				notes, POD_FLAG,EXEC_ORG_ID,CUSTOMER_ID,TO_POD_TIME,CREATE_ORG_ID,CREATE_ORG_ID_NAME,ADDWHO);
		
		
		/**
		 * 起止地信息
		 */

		// 1 发货区域 ，发货方 ，发货地址
		/*TextItem LOAD_AREA_ID = new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID.setVisible(false);
		
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_AREA_NAME.setWidth(120);
		LOAD_AREA_NAME.setColSpan(2);
		LOAD_AREA_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.LOAD_AREA_NAME()));
		Util.initArea(LOAD_AREA_NAME, LOAD_AREA_ID);
		*/
		SGCombo AREA_ID = new SGCombo("LOAD_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()));
		//Util.initComboValue(AREA_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		SGCombo AREA_ID2 = new SGCombo("LOAD_AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
		SGCombo AREA_ID3 = new SGCombo("LOAD_AREA_ID3",Util.TI18N.AREA());
		SGCombo LOAD_REGION = new SGCombo("LOAD_REGION",Util.TI18N.LOAD_REGION());
		LOAD_REGION.setVisible(false);
		SGText AREA_NAME = new SGText("LOAD_AREA_NAME",Util.TI18N.PROVINCE());
        AREA_NAME.setVisible(false);
        SGText AREA_NAME2 = new SGText("LOAD_AREA_NAME2",Util.TI18N.CITY());
        AREA_NAME2.setVisible(false);
        final SGText AREA_NAME3 = new SGText("LOAD_AREA_NAME3",Util.TI18N.AREA());
        AREA_NAME3.setVisible(false);
        AREA_ID.addChangedHandler(new AreaChangeAction(AREA_NAME,AREA_ID2,AREA_NAME2));
        AREA_ID2.addChangedHandler(new AreaChangeAction(AREA_NAME2,AREA_ID3,AREA_NAME3,LOAD_REGION));
        AREA_ID3.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME3 != null){
					AREA_NAME3.setValue(value);
				}
			}
        	
        });
		
		TextItem LOAD_ID = new TextItem("LOAD_ID");
		LOAD_ID.setVisible(false);
		
		final ComboBoxItem LOAD_NAME = new ComboBoxItem("LOAD_NAME", Util.TI18N.LOAD_NAME());
		LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_NAME.setColSpan(4);
		LOAD_NAME.setWidth(250);
		SGLText LOAD_ADDRESS = new SGLText("LOAD_ADDRESS", ColorUtil.getRedTitle(Util.TI18N.LOAD_ADDRESS()),true);
		LOAD_ADDRESS.setWidth(380);
		
		// 2 发货联系人，联系电话 ， 计划发货时间，客户自送
		SGText LOAD_CONTACT = new SGText("LOAD_CONTACT", ColorUtil.getRedTitle(Util.TI18N.CONT_NAME()));
		SGText LOAD_TEL = new SGText("LOAD_TEL", ColorUtil.getRedTitle(Util.TI18N.CONT_TEL()));
		LOAD_TEL.setColSpan(3);
		
		//初始化发货方下拉框
		initLoadId(LOAD_NAME, LOAD_ADDRESS, LOAD_ID, AREA_ID, AREA_NAME, AREA_ID2, AREA_NAME2, AREA_ID3, AREA_NAME3,LOAD_CONTACT, LOAD_TEL,LOAD_REGION);

		// 4 收货区域 ，收货方 ，收货地址
		
		/*TextItem UNLOAD_AREA_ID = new TextItem("UNLOAD_AREA_ID");
		UNLOAD_AREA_ID.setVisible(false);
		
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.UNLOAD_AREA_NAME()));
		UNLOAD_AREA_NAME.setWidth(120);
		UNLOAD_AREA_NAME.setColSpan(2);
		UNLOAD_AREA_NAME.setStartRow(true);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);*/
		SGCombo AREA_ID4 = new SGCombo("UNLOAD_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()));
		SGCombo AREA_ID5 = new SGCombo("UNLOAD_AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
		SGCombo AREA_ID6 = new SGCombo("UNLOAD_AREA_ID3",Util.TI18N.AREA());
		SGCombo UNLOAD_REGION = new SGCombo("UNLOAD_REGION",Util.TI18N.UNLOAD_REGION());
		UNLOAD_REGION.setVisible(false);
		SGText AREA_NAME4 = new SGText("UNLOAD_AREA_NAME",Util.TI18N.PROVINCE());
        AREA_NAME4.setVisible(false);
        SGText AREA_NAME5 = new SGText("UNLOAD_AREA_NAME2",Util.TI18N.CITY());
        AREA_NAME5.setVisible(false);
        final SGText AREA_NAME6 = new SGText("UNLOAD_AREA_NAME3",Util.TI18N.AREA());
        AREA_NAME6.setVisible(false);
        
		ArrayList comboList = new ArrayList();
		comboList.add(AREA_ID);
		comboList.add(AREA_ID4);
		SGCombo[] combos = (SGCombo[])comboList.toArray(new SGCombo[comboList.size()]);
		Util.initComboValue(combos, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		
        AREA_ID4.addChangedHandler(new AreaChangeAction(AREA_NAME4,AREA_ID5,AREA_NAME5));
        AREA_ID5.addChangedHandler(new AreaChangeAction(AREA_NAME5,AREA_ID6,AREA_NAME6,UNLOAD_REGION));
        AREA_ID6.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME6 != null){
					AREA_NAME6.setValue(value);
				}
			}
        	
        });
		
		TextItem UNLOAD_ID = new TextItem("UNLOAD_ID");
		UNLOAD_ID.setVisible(false);
		
		final ComboBoxItem UNLOAD_NAME = new ComboBoxItem("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		UNLOAD_NAME.setWidth(250);
		UNLOAD_NAME.setColSpan(4);
		SGLText UNLOAD_ADDRESS = new SGLText("UNLOAD_ADDRESS", ColorUtil.getRedTitle(Util.TI18N.UNLOAD_ADDRESS()),true);
		UNLOAD_ADDRESS.setWidth(380);
		// 5 收货联系人，联系电话， 计划收货时间，客户自提
		SGText UNLOAD_CONTACT = new SGText("UNLOAD_CONTACT", ColorUtil.getRedTitle(Util.TI18N.CONT_NAME()));
		SGText UNLOAD_TEL = new SGText("UNLOAD_TEL", ColorUtil.getRedTitle(Util.TI18N.CONT_TEL()));
		//UNLOAD_TEL.setColSpan(3);
		
		SGText BIZ_CODE = new SGText("BIZ_CODE",Util.TI18N.BIZ_CODE());
		
		//初始化收货方下拉框
		initUnLoadId(UNLOAD_NAME, UNLOAD_ADDRESS, UNLOAD_ID, AREA_ID4, AREA_NAME4, AREA_ID5, AREA_NAME5, AREA_ID6, AREA_NAME6, UNLOAD_CONTACT, UNLOAD_TEL,UNLOAD_REGION);
		
		basInfo2 = new SGPanel();
		basInfo2.setTitleWidth(75);
		basInfo2.setItems(AREA_ID,AREA_NAME,AREA_ID2,AREA_NAME2,AREA_ID3,AREA_NAME3, LOAD_NAME, LOAD_REGION,LOAD_ADDRESS,LOAD_CONTACT, LOAD_TEL
				,AREA_ID4,AREA_NAME4,AREA_ID5,AREA_NAME5,AREA_ID6,AREA_NAME6, UNLOAD_NAME,UNLOAD_REGION,UNLOAD_ADDRESS,UNLOAD_CONTACT, UNLOAD_TEL,BIZ_CODE
                ,LOAD_ID,UNLOAD_ID);//隐藏字段
					
		CUSTOMER_NAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(CUSTOMER_NAME.getValue()) && 
						ObjUtil.isNotNull(vm.getValueAsString("CUSTOMER_ID"))){
						putMapToVM();
						
						custChanged(vm.getValueAsString("CUSTOMER_ID"), ObjUtil.ifNull(vm.getValueAsString("ODR_TYP"),LoginCache.getDefCustomer().get("DEFAULT_ORD_TYP")));
						//提货点联动
						Criteria criteria = LOAD_NAME.getPickListCriteria();
						criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
						LOAD_NAME.setPickListCriteria(criteria);
						//提货点联动
						Criteria criteria2 = UNLOAD_NAME.getPickListCriteria();
						criteria2.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
						UNLOAD_NAME.setPickListCriteria(criteria2);
						
						
						
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
		basicInfo.setExpanded(true);
		section.addSection(basicInfo);
		// 2，起止点信息
		SectionStackSection contact_Info = new SectionStackSection(Util.TI18N.START_END_MES());
		basInfo2.setHeight("40%");
		contact_Info.addItem(basInfo2);
		contact_Info.setExpanded(true);
		section.addSection(contact_Info);

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
		ListGridField SKU_NAME = new ListGridField("SKU_NAME", ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()), 120);
		
		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 80);
		SKU.setCanEdit(false);
		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1", ColorUtil.getRedTitle(Util.TI18N.TEMPERATURE()), 90);
		Util.initCodesComboValue(TEMPERATURE1,"TRANS_COND");
		//final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK(), 70);
		UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 50);
		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 70);
		QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.PACK_QTY()));
		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 90);
		VOL.setCanEdit(true);
		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 90);
		G_WGT.setCanEdit(true);
		
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 90);
		NOTES.setCanEdit(true);

		//Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		//Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		//Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
		//Util.initFloatListField(QNTY_EACH, StaticRef.QNTY_FLOAT);
		
		groupTable.setFields(ROW, SKU_NAME, QNTY, TEMPERATURE1, VOL, G_WGT, UOM, SKU,NOTES);
		
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
				final String sku_name = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
				Util.db_async.getRecord("ID,SKU,TRANS_COND,SKU_CNAME,TRANS_UOM,GROSSWEIGHT,VOLUME", "V_SKU", 
						" where (customer_id = '" + vm.getValueAsString("CUSTOMER_ID") + "' or common_flag = 'Y') and full_index like '%"+sku_name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						int size = result.size();
						if(size == 1){
							groupTable.setEditValue(row, "SKU_ID", result.get(0).get("ID"));
							groupTable.setEditValue(row, "SKU_NAME", result.get(0).get("SKU_CNAME"));
							groupTable.setEditValue(row, "SKU", result.get(0).get("SKU"));
							groupTable.setEditValue(row, "TEMPERATURE1", result.get(0).get("TRANS_COND"));
							groupTable.setEditValue(row, "UOM", result.get(0).get("TRANS_UOM"));
							groupTable.setEditValue(row, "QNTY", "1");
							groupTable.setEditValue(row, "QNTY_EACH", "1");
							groupTable.setEditValue(row, "VOL", ObjUtil.ifNull(result.get(0).get("VOLUME"),"0.00"));
							groupTable.setEditValue(row, "G_WGT",ObjUtil.ifNull(result.get(0).get("GROSSWEIGHT"),"0.00"));
							JSOHelper.setAttribute(groupTable.getField("QNTY").getJsObj(), "QNTY_sourceValue", "1");
							JSOHelper.setAttribute(groupTable.getField("G_WGT").getJsObj(), "G_WGT_sourceValue", ObjUtil.ifNull(result.get(0).get("GROSSWEIGHT"),"0.00"));
							JSOHelper.setAttribute(groupTable.getField("VOL").getJsObj(), "VOL_sourceValue", ObjUtil.ifNull(result.get(0).get("VOLUME"),"0.00"));
						}else if(size > 1){
							groupTable.setProperty("CUSTOMER_ID", vm.getValueAsString("CUSTOMER_ID"));
							groupTable.setProperty("FULL_INDEX", sku_name);
							new SkuWin(groupTable,vm.getValueAsString("CUSTOMER_ID"),itemRow,"40%", "38%",getThis(),sku_name).getViewPanel();
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
				
			}
		});
		
		
		//Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
		Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");
		UOM.setDefaultValue("件");
		
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
				String oldQty = JSOHelper.getAttribute(QNTY_FIELD.getJsObj(), "QNTY_sourceValue");
				String vol = JSOHelper.getAttribute(VOL_FIELD.getJsObj(), "VOL_sourceValue");
				String gwgt = JSOHelper.getAttribute(G_WGT_FIELD.getJsObj(), "G_WGT_sourceValue");
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
				double d_qty = Double.parseDouble(qty);
				double new_vol = Double.parseDouble(vol);
				double new_gwgt = Double.parseDouble(gwgt);
				new_vol =  d_qty * new_vol;
				new_gwgt = d_qty * new_gwgt;
				groupTable.setEditValue(row, "VOL", new_vol);
				groupTable.setEditValue(row, "G_WGT", new_gwgt);
				
			}
		});
		
		groupTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				JSOHelper.setAttribute(groupTable.getField("G_WGT").getJsObj(), "G_WGT_sourceValue", "");
				JSOHelper.setAttribute(groupTable.getField("VOL").getJsObj(), "VOL_sourceValue", "");
				JSOHelper.setAttribute(groupTable.getField("QNTY").getJsObj(), "QNTY_sourceValue", "");
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
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, true);
					enableOrDisables(sav_detail_map, true);
					enableOrDisables(del_detail_map, true);
//					setButtonEnabled(TrsPrivRef.ODRCREATE_P1_04, canButton, true);
				}else if(selectRecord !=null && !StaticRef.SO_CREATE.equals(selectRecord.getAttribute("STATUS"))){
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, true);
					enableOrDisables(save_map, true);
					enableOrDisables(add_detail_map, true);
					enableOrDisables(del_detail_map, true);
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
		groupTable2.setFields(FEE_ID, FEE_BAS, BAS_VALUE,PRICE, PRE_FEE, DISCOUNT_RATE, DUE_FEE, PAY_FEE, NOTES);

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
	
	//选中记录变化
	protected void selectionChanged(SelectionEvent event){
		hRow = table.getRecordIndex(event.getRecord());
		final Record record = event.getRecord();
		selectRecord = record;
		vm.editRecord(record);
		vm.setValue("OP_FLAG",StaticRef.MOD_FLAG);
		
		tabChange(tabName);
		
		addrDisabled(!ObjUtil.flagToBoolean(record.getAttribute("ADDR_EDIT_FLAG")));

		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		if(ObjUtil.isNotNull(selectRecord.getAttribute("LOAD_AREA_ID2"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("LOAD_AREA_ID2"), selectRecord.getAttribute("LOAD_AREA_NAME2"));
			basInfo2.getItem("LOAD_AREA_ID2").setValueMap(map);
			basInfo2.getItem("LOAD_AREA_ID2").setValue(selectRecord.getAttribute("LOAD_AREA_ID2"));
		}
		else {
			basInfo2.getItem("LOAD_AREA_ID2").setValue("");
			basInfo2.getItem("LOAD_AREA_ID2").setValueMap("");
		}
		if(ObjUtil.isNotNull(selectRecord.getAttribute("LOAD_AREA_ID3"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("LOAD_AREA_ID3"), selectRecord.getAttribute("LOAD_AREA_NAME3"));
			basInfo2.getItem("LOAD_AREA_ID3").setValueMap(map);
			basInfo2.getItem("LOAD_AREA_ID3").setValue(selectRecord.getAttribute("LOAD_AREA_ID3"));
		}
		else {
			basInfo2.getItem("LOAD_AREA_ID3").setValue("");
			basInfo2.getItem("LOAD_AREA_ID3").setValueMap("");
		}
		if(ObjUtil.isNotNull(selectRecord.getAttribute("LOAD_REGION"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("LOAD_REGION"), selectRecord.getAttribute("LOAD_AREA_NAME3"));
			basInfo2.getItem("LOAD_REGION").setValueMap(map);
			basInfo2.getItem("LOAD_REGION").setValue(selectRecord.getAttribute("LOAD_REGION"));
		}
		else {
			basInfo2.getItem("LOAD_REGION").setValue("");
			basInfo2.getItem("LOAD_REGION").setValueMap("");
		}
		if(ObjUtil.isNotNull(selectRecord.getAttribute("UNLOAD_AREA_ID2"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("UNLOAD_AREA_ID2"), selectRecord.getAttribute("UNLOAD_AREA_NAME2"));
			basInfo2.getItem("UNLOAD_AREA_ID2").setValueMap(map);
			basInfo2.getItem("UNLOAD_AREA_ID2").setDefaultValue(selectRecord.getAttribute("UNLOAD_AREA_ID2"));
		}
		else {
			basInfo2.getItem("UNLOAD_AREA_ID2").setDefaultValue("");
			basInfo2.getItem("UNLOAD_AREA_ID2").setValueMap("");
		}
		if(ObjUtil.isNotNull(selectRecord.getAttribute("UNLOAD_AREA_ID3"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("UNLOAD_AREA_ID3"), selectRecord.getAttribute("UNLOAD_AREA_NAME3"));
			basInfo2.getItem("UNLOAD_AREA_ID3").setValueMap(map);
			basInfo2.getItem("UNLOAD_AREA_ID3").setDefaultValue(selectRecord.getAttribute("UNLOAD_AREA_ID3"));
		}
		else {
			basInfo2.getItem("UNLOAD_AREA_ID3").setDefaultValue("");
			basInfo2.getItem("UNLOAD_AREA_ID3").setValueMap("");
		}
		if(ObjUtil.isNotNull(selectRecord.getAttribute("UNLOAD_REGION"))) {
			map = new LinkedHashMap<String,String>();
			map.put(selectRecord.getAttribute("UNLOAD_REGION"), selectRecord.getAttribute("UNLOAD_AREA_NAME3"));
			basInfo2.getItem("UNLOAD_REGION").setValueMap(map);
			basInfo2.getItem("UNLOAD_REGION").setDefaultValue(selectRecord.getAttribute("UNLOAD_REGION"));
		}
		else {
			basInfo2.getItem("UNLOAD_REGION").setDefaultValue("");
			basInfo2.getItem("UNLOAD_REGION").setValueMap("");
		}
		if(!(vm.getItem("ODR_TIME") == null || 
				ObjUtil.isNotNull(vm.getItem("ODR_TIME").getValue()))){
			Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					vm.setValue("ODR_TIME", result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	    table.discardAllEdits();
	    groupTable.discardAllEdits();
	    groupTable2.discardAllEdits();
	}

	private void createListField() {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(final SelectionEvent event) {
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
		    table.addShowContextMenuHandler(new ShowContextMenuHandler() {
	            public void onShowContextMenu(ShowContextMenuEvent event) {
	            	menu.showContextMenu();
	                event.cancel();
	            }
	        });
		}
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
	
	/**
	 * 设置BUTTON属性
	 */
	public void enableOrDisablesCallback(IButton button){
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
					searchWin = new SearchWin(460,ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		//关闭按钮
		closeButton = createBtn(StaticRef.CANCEL_BTN, TrsPrivRef.ORDERCLOSE_P1_01);
		closeButton.setTitle("关闭");
        closeButton.addClickHandler(new CloseOrderAction(table, 0));
        
      //打开按钮
		openButton = createBtn(StaticRef.CANCEL_BTN, TrsPrivRef.ORDERCLOSE_P1_02);
		openButton.setTitle("取消关闭");
		openButton.addClickHandler(new CloseOrderAction(table, 1));
	    
	    final IButton refresh = createUDFBtn("刷新",StaticRef.ICON_REFRESH,TrsPrivRef.ORDERCLOSE_P1_03);
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
					criteria.addCriteria("STATUS_FORM", 20);
					criteria.addCriteria("STATUS_TO", 20);
					criteria.addCriteria("C_ORG_FLAG", true);
					criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				}
				criteria.addCriteria("OP_FLAG","M");
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

		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, closeButton, openButton, refresh);
	}

	protected DynamicForm createSearchForm(DynamicForm form) {
		
		form.setDataSource(ds);
		
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
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
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		SGCombo ODR_TYP = new SGCombo("ODR_TYP", Util.TI18N.ODR_TYP());
		Util.initCodesComboValue(ODR_TYP, "ORD_TYP");
		
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
		
		SGCombo ORDER_STATE_FROM = new SGCombo("STATUS_FORM", Util.TI18N.ORDER_STATE(),true);//下单状态
		Util.initStatus(ORDER_STATE_FROM, StaticRef.ODRNO_STAT,"20");
		
		SGCombo ORDER_STATE_TO = new SGCombo("STATUS_TO", "到");//到
		Util.initStatus(ORDER_STATE_TO, StaticRef.ODRNO_STAT,"20");
		
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");//订单时间
		
		SGDateTime ORD_ADDTIME_FROM=new SGDateTime("ADDTIME_FROM",Util.TI18N.ORD_ADDTIME_FROM());
		SGDateTime ORD_ADDTIME_TO=new SGDateTime("ADDTIME_TO","到");//创建时间
		
		SGCombo MANAGE_STATE = new SGCombo("PLAN_STAT", Util.TI18N.PLAN_STAT());
		Util.initStatus(MANAGE_STATE, StaticRef.PLAN_STAT,"");
		
		SGCombo DISPATCH_STATE = new SGCombo("LOAD_STAT", Util.TI18N.LOAD_STAT());
		Util.initStatus(DISPATCH_STATE, StaticRef.LOAD_STAT,"");
		
		SGCombo ARRIVE_STATE = new SGCombo("UNLOAD_STAT", Util.TI18N.UNLOAD_STAT());
		Util.initStatus(ARRIVE_STATE, StaticRef.UNLOAD_STAT,"");
		
//		final TextItem UNLOAD_AREA_ID=new TextItem("UNLOAD_AREA_ID");
//		UNLOAD_AREA_ID.setVisible(false);
//		ComboBoxItem UNLOAD_AREA_NAME=new ComboBoxItem("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME());
//		UNLOAD_AREA_NAME.setColSpan(1);
//		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);
//		UNLOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
//		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		
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
		C_ORG_FLAG.setStartRow(true);
		
		SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG());	
		SLF_PICKUP_FLAG.setValue(false);//包含下级机构
//		SLF_PICKUP_FLAG.setColSpan(2);
		
//		SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据*/
		
		SGCombo BUK_FLAG = new SGCombo("BUK_FLAG", "SSS处理标识");
		BUK_FLAG.setStartRow(true);
		BUK_FLAG.setColSpan(2);
		LinkedHashMap<String, String> bukValueMap = new LinkedHashMap<String, String>();
		bukValueMap.put("", "  ");
		bukValueMap.put("Y", "已处理");
		bukValueMap.put("N", "未处理");
		BUK_FLAG.setValueMap(bukValueMap);
		
		SGText UNLOAD_CONTACT = new SGText("UNLOAD_CONTACT", "收件人");
		SGText UNLOAD_TEL = new SGText("UNLOAD_TEL", Util.TI18N.CONT_TEL());
		
		form.setItems(CUSTOMER_ID, CUSTOMER_NAME,ORDER_CODE,CUSTOM_ODR_NO
				,EXEC_ORG_ID_NAME,
				BIZ_TYP,ODR_TYP,EXEC_STAT,ORD_PRO_LEVER,LOAD_NAME,UNLOAD_NAME,
				ODR_TIME_FROM,ODR_TIME_TO,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,MANAGE_STATE,DISPATCH_STATE,ARRIVE_STATE,UNLOAD_AREA_ID2,
				UNLOAD_AREA_NAME2,ORDER_STATE_FROM,ORDER_STATE_TO,REFENENCE1,ADDWHO,EXEC_ORG_ID,BUK_FLAG, UNLOAD_CONTACT, UNLOAD_TEL, C_ORG_FLAG,SLF_PICKUP_FLAG);
		return form;
		
	}

	public void createForm(DynamicForm form) {

	}

	public void initVerify() {
		check_map.put("TABLE", "TRANS_ORDER_HEADER");
		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL + Util.TI18N.CUSTOMER_ID());
		check_map.put("ODR_TIME", StaticRef.CHK_NOTNULL + Util.TI18N.ODR_TIME());
		check_map.put("TRANS_SRVC_ID", StaticRef.CHK_NOTNULL + Util.TI18N.TRANS_SRVC_ID());
		check_map.put("BIZ_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.BIZ_TYP());
		check_map.put("EXEC_ORG_ID", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("EXEC_ORG_ID_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("LOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		check_map.put("LOAD_AREA_ID2",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_NAME()+ Util.TI18N.CITY());
		check_map.put("UNLOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		check_map.put("UNLOAD_AREA_ID2",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_NAME()+ Util.TI18N.CITY());
		
		check_map.put("LOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		check_map.put("LOAD_AREA_NAME2",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_NAME()+ Util.TI18N.CITY());
		check_map.put("UNLOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		check_map.put("UNLOAD_AREA_NAME2",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_NAME()+ Util.TI18N.CITY());
		check_map.put("LOAD_ADDRESS", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_ADDRESS());
		check_map.put("UNLOAD_ADDRESS", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_ADDRESS());
		check_map.put("ODR_TIME", StaticRef.CHK_DATE + Util.TI18N.ODR_TIME());
		check_map.put("FROM_LOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_LOAD_TIME());
		check_map.put("PRE_LOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_LOAD_TIME());
		check_map.put("FROM_UNLOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_UNLOAD_TIME());
		check_map.put("PRE_UNLOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_UNLOAD_TIME());
//		check_map.put("FROM_POD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_POD_TIME());
		check_map.put("PRE_POD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_POD_TIME());
		check_map.put("LOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_CONTACT());
		check_map.put("LOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_TEL());
		check_map.put("UNLOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_CONTACT());
		check_map.put("UNLOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_TEL());
		
		
		detail_ck_map = new HashMap<String, String>();
		detail_ck_map.put("TABLE", "TRANS_ORDER_ITEM");
		detail_ck_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		detail_ck_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("TEMPERATURE1", StaticRef.CHK_NOTNULL + Util.TI18N.TEMPERATURE());
		
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
	private void initCustomer(final ComboBoxItem customer_name,final TextItem customer_id){
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
					
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("LOAD_AREA_ID2"))) {
						Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("LOAD_AREA_ID") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("LOAD_AREA_ID3"))) {
						Util.initComboValue(basInfo2.getItem("LOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("LOAD_REGION"))) {
						Util.initComboValue(basInfo2.getItem("LOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("LOAD_AREA_ID2") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("UNLOAD_AREA_ID2"))) {
						Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("UNLOAD_AREA_ID") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("UNLOAD_AREA_ID3"))) {
						Util.initComboValue(basInfo2.getItem("UNLOAD_AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
					}
					if(ObjUtil.isNotNull(selectedRecord.getAttribute("UNLOAD_REGION"))) {
						Util.initComboValue(basInfo2.getItem("UNLOAD_REGION"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("UNLOAD_AREA_ID2") + "'", "");
					}
					customer_name.setValue(selectedRecord.getAttribute("CUSTOMER_NAME"));
					customer_id.setValue(selectedRecord.getAttribute("CUSTOMER_ID"));
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
//					vm.setValue("SLF_DELIVER_FLAG", selectedRecord.getAttributeAsBoolean("SLF_DELIVER_FLAG"));
//					vm.setValue("SLF_PICKUP_FLAG", selectedRecord.getAttributeAsBoolean("SLF_PICKUP_FLAG"));
					vm.setValue("POD_FLAG", selectedRecord.getAttributeAsBoolean("POD_FLAG"));
					vm.setValue("TRANS_SRVC_ID", selectedRecord.getAttribute("DEFAULT_TRANS_SRVC_ID"));
					vm.setValue("TRANS_SRVC_ID_NAME", selectedRecord.getAttribute("TRANS_SRVC_ID_NAME"));
					vm.setValue("ODR_TYP", selectedRecord.getAttribute("DEFAULT_ORD_TYP"));
					vm.setValue("TRANS_UOM", selectedRecord.getAttribute("TRANS_UOM"));
					vm.setValue("SALES_MAN", selectedRecord.getAttribute("FOLLOWUP"));
					vm.setValue("WHSE_ID", selectedRecord.getAttribute("WHSE_ID"));
					vm.setValue("UNIQ_CONO_FLAG", selectedRecord.getAttribute("UNIQ_CONO_FLAG"));
					vm.setValue("REFENENCE3", selectedRecord.getAttribute("CUSTOM_ATTR"));
					addrDisabled(!selectedRecord.getAttributeAsBoolean("ADDR_EDIT_FLAG"));
					if(!(ObjUtil.isNotNull(vm.getItem("LOAD_AREA_ID").getValue()) || 
							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_NAME").getValue()) ||
							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_ID2").getValue()) ||
							ObjUtil.isNotNull(vm.getItem("LOAD_AREA_NAME2").getValue()))){
						initOrgAreaInfo();
					}
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
	
	private void addrDisabled(Boolean disabled){
		vm.getItem("LOAD_AREA_NAME").setDisabled(disabled);
		vm.getItem("UNLOAD_AREA_NAME").setDisabled(disabled);
	}
	
	//初始化提货点下拉框
	//新方法：带省市区
	private void initLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
			,final SGCombo load_area_id,final TextItem load_area_name
			,final SGCombo load_area_id2,final TextItem load_area_name2
			,final SGCombo load_area_id3,final TextItem load_area_name3
			,final SGText cont_name,final SGText cont_tel,final SGCombo load_region){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		load_name.setOptionDataSource(ds2);  
		load_name.setDisabled(false);
		load_name.setShowDisabled(false);
		load_name.setDisplayField("FULL_INDEX");  
		load_name.setPickListBaseStyle("myBoxedGridCell");
		load_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
		criteria.addCriteria("AND_LOAD_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		load_name.setPickListCriteria(criteria);
		
		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		load_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_name.getSelectedRecord();
				if(selectedRecord != null){
					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					Util.initComboValue(load_region, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_region.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_id.setValue(selectedRecord.getAttribute("ID"));
					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
					vm.setValue("WHSE_ID", selectedRecord.getAttribute("WHSE_ID"));
				}
			}
		});
	}
	
	//初始化收货方下拉框
	private void initUnLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
			,final SGCombo load_area_id,final TextItem load_area_name
			,final SGCombo load_area_id2,final TextItem load_area_name2
			,final SGCombo load_area_id3,final TextItem load_area_name3
			,final SGText cont_name,final SGText cont_tel,final SGCombo unload_region){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		load_name.setOptionDataSource(ds2);  
		load_name.setDisabled(false);
		load_name.setShowDisabled(false);
		load_name.setDisplayField("FULL_INDEX");
		load_name.setPickListBaseStyle("myBoxedGridCell");
		load_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		load_name.setPickListCriteria(criteria);
		
		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		load_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_name.getSelectedRecord();
				if(selectedRecord != null){
					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					Util.initComboValue(unload_region, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
					unload_region.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_id.setValue(selectedRecord.getAttribute("ID"));
					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
					//vm.setValue("SLF_PICKUP_FLAG",ObjUtil.ifNull(selectedRecord.getAttribute("SELF_PKUP_FLAG"),"N"));
				}
			}
		});
	}
	
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
			addrDisabled(!ObjUtil.flagToBoolean(map.get("ADDR_EDIT_FLAG")));
			
		}
	}
	
	/**
	 *  确认/取消确认按钮可用状态控制
	 * @author Administrator
	 * @param b1
	 * @param b2
	 */
	public void confirmBtnEnable(boolean b1,boolean b2){
		
	}
	
	private OrderCloseView getThis(){
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
		enableOrDisables(add_map, true);
		enableOrDisables(del_map, true);
		enableOrDisables(save_map, true);
	} 
	
	@Override
	public void expend(String p_width) {
		super.expend(p_width);
		if(vm.getItem("ODR_TIME") == null)return;
		if(!ObjUtil.isNotNull(vm.getItem("ODR_TIME").getValue())){
			//当前服务器系统时间
			Util.async.getServTime("yyyy/MM/dd HH:mm:ss",new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					vm.setValue("ODR_TIME", result);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		OrderCloseView view = new OrderCloseView();
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
