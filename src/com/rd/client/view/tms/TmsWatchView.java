package com.rd.client.view.tms;


import java.util.Date;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 客户服务-->订单动态监视控
 * @author wangjun
 *
 */
@ClassForNameAble
public class TmsWatchView extends SGForm implements PanelFactory {

	public SGTable table;
	private SGPanel searchForm;
	private Window searchWin;
	//private Window transTrackWin;
	private SectionStack section;
	private DataSource orderDS;
//	private DataSource orderlstDS;
	public SGTable orderTable;
	public SGTable orderlstTable;
//	private String order_no;
	
	public SGTable groupTable1;
	public SGTable groupTable2;
//	public SGTable shpmTable;
	//private DataSource transact_logDS;
	//private DynamicForm pageForm;
	private DataSource loadDS;            //调度单数据源
	private ListGrid loadTable;       //调度单表
	
	/*public TmsWatchView(String id) {
	    super(id);
	}*/
	
	public Canvas getViewPanel() {
		
		/*CountryRecord[] data = new CountryRecord[]{   
                new CountryRecord("US", "United States", 298444215),   
                new CountryRecord("CH", "China", 1313973713),   
                new CountryRecord("JA", "Japan", 127463611)   
        };   
  
        final ListGrid countryGrid = new ListGrid();   
        countryGrid.setWidth(500);   
        countryGrid.setAutoFitMaxRecords(6);   
        countryGrid.setAutoFitData(Autofit.VERTICAL);   
        countryGrid.setCanEdit(true);   
        countryGrid.setEditEvent(ListGridEditEvent.CLICK);   
        countryGrid.setListEndEditAction(RowEndEditAction.NEXT);   
  
        ListGridField countryCodeField = new ListGridField("countryCode", "Country Code");   
        ListGridField nameField = new ListGridField("countryName", "Country");   
        ListGridField populationField = new ListGridField("population", "Population"); 
        ListGridField test1Field = new ListGridField("test1", "test1");
        ListGridField test2Field = new ListGridField("test2", "test2");

        ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 70);  //供应商
		Util.initOrgSupplier(SUPLR_NAME, "");
  
        countryGrid.setFields(countryCodeField, nameField, populationField,test1Field,test2Field,test3Field);   
        countryGrid.setData(data);   */
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		loadTable = new ListGrid(); 
		loadTable.setDataSource(loadDS);
		loadTable.setWidth100();
		loadTable.setHeight100();
		/*loadTable.setShowFilterEditor(false);
		loadTable.setCanDrag(false);
		loadTable.setCanDragRecordsOut(false);
		loadTable.setCanDragReposition(false);
		loadTable.setCanDragResize(false);
		loadTable.setCanDragScroll(false);
		loadTable.setCanDragSelect(false);
		loadTable.setCanDragSelectText(true);
        loadTable.setCanSelectText(true);*/
		loadTable.setBaseStyle("myBoxedGridCell");
		
		loadTable.setCellHeight(21);
		loadTable.setAlternateRecordStyles(true);
		//loadTable.setShowRowNumbers(true);
		loadTable.setShowFilterEditor(true);
		loadTable.setFilterOnKeypress(false);
		/*setShowAllRecords(true);
		setAutoFetchData(false);
		setCanReorderRecords(true);
		setCanEdit(true);
		setWidth(per_width);
		setHeight(per_height);
		setShowHover(true);
		setCanHover(true);
		setCanDragSelectText(true);
        setCanSelectText(true);
		initBindEvent(isBindEvent);*/
		
		//createLoadField(loadTable);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",100);
		ListGridField START_AREA_NAME = new ListGridField("START_AREA_NAME","起点",100);
		ListGridField END_AREA_NAME = new ListGridField("END_AREA_NAME","终点",100);
		ListGridField CONFIRMOR = new ListGridField("CONFIRMOR","确认人",70);
		ListGridField CONFIRM_TIME = new ListGridField("CONFIRM_TIME","确认时间",120);
		ListGridField REF_NO = new ListGridField("REF_NO","参考单号",100);
		loadTable.setFields(LOAD_NO,START_AREA_NAME,END_AREA_NAME,CONFIRMOR,CONFIRM_TIME,REF_NO);
		loadTable.setCanEdit(false);      
		Criteria crit = new Criteria();
		crit.setAttribute("OP_FLAG", "M");
		//crit.setAttribute("STATUS", "10");
		crit.setAttribute("TRANS_SRVC_ID", "2");
		loadTable.fetchData(crit);
        /*IButton button = new IButton("Edit New");   
        button.addClickHandler(new ClickHandler() {   
            public void onClick(ClickEvent event) {   
                countryGrid.startEditingNew();   
            }   
        });*/   
  
        VLayout vLayout = new VLayout(20);   
        vLayout.addMember(loadTable);   
        //vLayout.addMember(button); 
        
        //setListGridBodyCanSelectText(loadTable,true);
    	JSOHelper.setAttribute(loadTable.getOrCreateJsObj(),"canSelectText",true);
        return vLayout;

		/*privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		orderDS = TmsTranOrderDS .getInstance("CUSTOM_ORDER_QUERY");
//		orderlstDS = TranOrderItemDS.getInstance("V_ORDER_ITEM","TRANS_ORDER_ITEM");
		transact_logDS=Transact_logDS.getInstance("V_TRANSACT_LOG_ODR");// 【跟踪历史信息】
		//
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//
		HStack hStack = new HStack();
		hStack.setWidth100();
		hStack.setHeight100();
		
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("100%");
		
		hStack.addMember(section);

		layout.addMember(toolStrip);
		layout.addMember(hStack);
		
		orderTable = new SGTable(orderDS, "100%", "100%");

		createListField(orderTable);
//		orderTable.setCanExpandRecords(true);
		//orderTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		orderTable.setShowFilterEditor(false);
		
		

		orderTable.setCanEdit(true);
		//loadTable.setExpansionCanEdit(false); 
		orderTable.setShowSelectedStyle(true);
		
		
		createBtnWidget(toolStrip);

		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.TRANS_ORDER_LIST());//托运单列表
		listItem.setItems(orderTable);
		listItem.setExpanded(true);
		pageForm = new SGPage(orderTable, true).initPageBtn();
		listItem.setControls(pageForm);
		section.addSection(listItem);
		
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("LOGIN_USER",LoginCache.getLoginUser().getUSER_ID());
		crit.addCriteria("STATUS","40");
		orderTable.fetchData(crit, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				// TODO Auto-generated method stub
				new SGPage(orderTable, true).initPageBtn().getField("CUR_PAGE").setValue("1");
				LoginCache.setPageResult(orderTable, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
			
			}
		});
		
		
		final Menu menu = new Menu();
		menu.setWidth(140);
//	    if(isPrivilege(TrsPrivRef.TRACK_P0_03)) {
		    MenuItem position = new MenuItem("车辆定位",StaticRef.ICON_SEARCH);
		    position.addClickHandler(new VechPositionAction(orderTable));
		    menu.addItem(position);
//	    }
	    
		orderTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				menu.showContextMenu();
                event.cancel();
			}
		});
		

		//下边布局
		TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("40%");	
		
		createbottoInfo();
		Tab tab1 = new Tab(Util.TI18N.TRANSACT_LOG());//跟踪历史信息
		tab1.setPane(groupTable1);
		bottomTabSet.addTab(tab1);
		
		
//		
//		layout.addMember(bottomTabSet);

		return layout;*/
	}

	/*private void createbottoInfo() {
		
		groupTable1 = new SGTable(transact_logDS,"100%", "100%");
		groupTable1.setShowFilterEditor(false);
		groupTable1.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable1.setShowRowNumbers(true);

		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.TRANSACT_NOTES(), 70);//节点信息
		ListGridField OP_TIME = new ListGridField("OP_TIME", Util.TI18N.OCCUR_TIME(), 120);//发生时间
		ListGridField ADDWHO = new ListGridField("USER_NAME", Util.TI18N.OPERATE_PERSON(), 70);
		ListGridField ADDTIME = new ListGridField("ADDTIME", Util.TI18N.OPERATE_TIME(), 120);
		
		groupTable1.setFields(NOTES,OP_TIME, ADDWHO, ADDTIME);

	}*/

	public void createBtnWidget(ToolStrip toolStrip) {
		// 组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		//toolStrip.setPadding(2);
		//toolStrip.setSeparatorSize(12);
		//toolStrip.addSeparator();
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(orderDS, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
//					searchWin.setWidth(600);
					searchWin.setHeight(350);
				} else {
					searchWin.show();
				}

			}
		});
//		toolStrip.setMembersMargin(8);
		toolStrip.setMembers(searchButton);

	}

	// 查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		form.setDataSource(orderDS);
		form.setAutoFetchData(false);
//		form.setWidth(300);
//		form.setHeight(600);
		form.setCellPadding(2);
		
		/**
		 * 客户单号 货品名称 订单时间 从 ...到  未完成订单  销售未提 发货未到  确认未配车  配车未发货  
		 */
	
		//未完成订单：UNLOAD_FLAG
		//销售未提： LOAD_FLAG
		//发货未到：LOADED_FLAG
		//确认未配车：NOPLAN_FLAG
		//配车未发货：PLANED_FLAG
		SGText CUSTOM_ODR_NO =new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(),true);  
		CUSTOM_ODR_NO.setWidth(126);
		CUSTOM_ODR_NO.setColSpan(1);
		
		SGLText UNLOAD_NAME =new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(),true);  
		
		SGText SKU_NAME= new SGText("SKU_NAME", Util.TI18N.SKU_NAME());
		SKU_NAME.setColSpan(2);
		
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
		ODR_TIME_FROM.setWidth(134);
		ODR_TIME_FROM.setColSpan(1);
		
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");//订单时间
		ODR_TIME_TO.setWidth(134);
		ODR_TIME_TO.setColSpan(2);
		
//		SGCheck UNLOAD_FLAG=new SGCheck("UNLOAD_FLAG",Util.TI18N.TRACK_UNLOAD_FLAG(),true);
//		SGCheck LOAD_FLAG=new SGCheck("LOAD_FLAG", Util.TI18N.TRACK_LOAD_FLAG());
		SGCheck LOADED_FLAG=new SGCheck("LD_UNLD_FLAG", Util.TI18N.TRACK_LOADED_FLAG(),true);
		SGCheck NOPLAN_FLAG=new SGCheck("NO_DISPATCH_FLAG", Util.TI18N.TRACK_NOPLAN_FLAG());
		NOPLAN_FLAG.setColSpan(2);
		SGCheck PLANED_FLAG=new SGCheck("DISPATCH_UNLD_FLAG", Util.TI18N.TRACK_PLANED_FLAG());
		TextItem LOGIN_USER = new TextItem("LOGIN_USER");
		LOGIN_USER.setVisible(false);
		LOGIN_USER.setValue(LoginCache.getLoginUser().getUSER_ID());
		
		form.setItems(CUSTOM_ODR_NO,SKU_NAME,UNLOAD_NAME,ODR_TIME_FROM,ODR_TIME_TO,LOADED_FLAG,
				NOPLAN_FLAG,PLANED_FLAG,LOGIN_USER);
	
//		form.setItems(CUSTOM_ODR_NO,UNLOAD_FLAG,SKU_NAME,LOAD_FLAG,LOADED_FLAG,ODR_TIME_FROM,ODR_TIME_TO
//				,NOPLAN_FLAG,PLANED_FLAG);
		return form;
		
		
		
	}
	public void createForm(DynamicForm form) {
		
	}

	public void initVerify() {
		
	}

	public void onDestroy() {
		if (searchWin != null ) {
			searchWin.destroy();
			
		}
	}

	public native void setListGridBodyCanSelectText(ListGrid listGrid,Boolean canSelectText) /*-{
		var lg = listGrid.@com.smartgwt.client.widgets.BaseWidget::getOrCreateJsObj()();
		alert(lg.innerHTML);
		var body = lg.body;
		alert(body);
		body.canSelectText = canSelectText;
	}-*/;

	/*private void createListField(final SGTable orderTable) {
		
		ListGridField LINK = new ListGridField("LINK_TEXT", "查看", 40);// 客户单号
		LINK.setType(ListGridFieldType.LINK);
		LINK.setCanEdit(false);
//		LINK.setLinkText("查看");
		LINK.setLinkText(Canvas.imgHTML("rd/scan.png", 16, 16, "info", "align=center", null));  

		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 90);// 客户单号
		LOAD_NO.setCanEdit(false);
		
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 90);// 客户单号
		CUSTOM_ODR_NO.setCanEdit(false);
		
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 120);// 订单时间      
		ODR_TIME.setCanEdit(false);
		ListGridField PACK_ID_NAME = new ListGridField("STATUS_NAME", Util.TI18N.ORD_PACK_ID(),50);//运输单位  
		PACK_ID_NAME.setCanEdit(false);
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY_EACH", Util.TI18N.ORD_TOT_QNTY(),90);//订单数量  ？？
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(false);
		Util.initFloatListField(TOT_QNTY,StaticRef.QNTY_FLOAT);
		
		ListGridField EA = new ListGridField("TOT_QNTY_EACH", Util.TI18N.R_EA(),60);//发货数量  
		//wangjun 2011/2/18
		Util.initFloatListField(EA,StaticRef.QNTY_FLOAT);
		EA.setCanEdit(false);
		
		ListGridField LD_QNTY = new ListGridField("LOAD_QNTY_EACH", Util.TI18N.FOLLOW_LD_QNTY(),60);//发货数量   LD_QNTY 
		LD_QNTY.setAlign(Alignment.RIGHT);
		LD_QNTY.setCanEdit(false);
		Util.initFloatListField(LD_QNTY,StaticRef.QNTY_FLOAT);
		ListGridField UNLD_QNTY = new ListGridField("UNLOAD_QNTY_EACH", Util.TI18N.UNLD_QNTY(),60);// 收货数量  UNLD_QNTY
		UNLD_QNTY.setAlign(Alignment.RIGHT);
		UNLD_QNTY.setCanEdit(false);
		Util.initFloatListField(UNLD_QNTY,StaticRef.QNTY_FLOAT);
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME", Util.TI18N.PRE_LOAD_TIME(),120);//计划发货时间  
		PRE_LOAD_TIME.setCanEdit(false);
		ListGridField END_LOAD_TIME = new ListGridField("LOAD_TIME",Util.TI18N.MANAGE_END_LOAD_TIME(),120);//
		//实际发运时间  DEPART_TIME//实际发运时间  wangjun 2011/2/18
		END_LOAD_TIME.setCanEdit(false);
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME", Util.TI18N.PRE_UNLOAD_TIME(), 120);// 预达时间
		PRE_UNLOAD_TIME.setCanEdit(false);
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME", Util.TI18N.FOLLOW_UNLOAD_TIME(), 120);// 实际收货时间  ?? 到货时间 
		UNLOAD_TIME.setCanEdit(false);
		//Util.initListGridDateTime(UNLOAD_TIME);
		//UNLOAD_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.FOLLOW_UNLOAD_TIME()));
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 150);// 收货方
		UNLOAD_NAME.setCanEdit(false);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS", Util.TI18N.UNLOAD_ADDRESS(),210);//收货方地址 
		UNLOAD_ADDRESS.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STATUS_NAME", Util.TI18N.STATUS(), 60);// 状态    订单状态
		STATUS.setCanEdit(false);
		ListGridField PLAN_STAT_NAME = new ListGridField("PLAN_STAT_NAME", Util.TI18N.PLAN_STAT(),65);//调度状态 
		PLAN_STAT_NAME.setCanEdit(false);
		ListGridField LOAD_STAT_NAME = new ListGridField("LOAD_STAT_NAME", Util.TI18N.LOAD_STAT(),65);//发运状态
		LOAD_STAT_NAME.setCanEdit(false);
		ListGridField UNLOAD_STAT_NAME = new ListGridField("UNLOAD_STAT_NAME", Util.TI18N.UNLOAD_STAT(),65);//到货状态 
		UNLOAD_STAT_NAME.setCanEdit(false);
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID(),70);//执行机构
		EXEC_ORG_ID_NAME.setCanEdit(false);
		
		orderTable.setFields(LINK,LOAD_NO,CUSTOM_ODR_NO, ODR_TIME,STATUS,TOT_QNTY,PRE_UNLOAD_TIME,UNLOAD_NAME,UNLOAD_ADDRESS);
	
	}*/
	
	/*private String getCustomLogCode() {
		String code = "";
		HashMap<String, String> logCode = LoginCache.getBizCodes().get(StaticRef.LOG_CODE);
		Object[] iter = logCode.keySet().toArray();
		String method = "";
		for(int i = 0; i < iter.length; i++) {
			method = (String)iter[i];
			code += ",'" + method + "'";
		}
		code = code.substring(2);
		return code;
	}*/
	
	/*private void createLoadField(final ListGrid groupTable) {
		boolean isDigitCanEdit = false;
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),90);//调度单编号
		LOAD_NO.setShowGridSummary(true);
		LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		LOAD_NO.setCanEdit(false);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()),60);//车牌号
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//状态
		STATUS_NAME.setCanEdit(false);
		ListGridField DISPATCH_STAT_NAME = new ListGridField("DISPATCH_STAT_NAME", Util.TI18N.DISPATCH_STAT_NAME(), 60);  //配车状态
		DISPATCH_STAT_NAME.setCanEdit(false);
		
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYP(),60);//车辆类型
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", ColorUtil.getRedTitle(Util.TI18N.TRANS_SRVC_ID()),70);
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField START_AREA = new ListGridField("START_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.LOAD_AREA_NAME()),60);//起点区域
		START_AREA_ID.setCanEdit(false);
		START_AREA_ID.setHidden(true);
		//Util.initArea(loadTable,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField END_AREA = new ListGridField("END_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.END_AREA()),60);//终点区域
		END_AREA_ID.setCanEdit(false);
		END_AREA_ID.setHidden(true);
		//Util.initArea(loadTable,END_AREA, "END_AREA_ID", "END_AREA_NAME", "");
		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		//Util.initDateTime(loadTable,DEPART_TIME);
		
		ListGridField DONE_TIME = new ListGridField("DONE_TIME", "预计回场时间", 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		//Util.initDateTime(loadTable,DONE_TIME);
		
		ListGridField REMAIN_GROSS_W = new ListGridField("REMAIN_GROSS_W","余量",50);//余量
		REMAIN_GROSS_W.setCanEdit(false);
		
		final ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.LOAD_UDF21(),65);//随车特服
		UDF1.setCanEdit(true);
		
		ListGridField UDF2 = new ListGridField("UDF2", Util.TI18N.LOAD_UDF22(), 85);  //电话
		
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 80);  //供应商
		EXEC_ORG_ID_NAME.setCanEdit(false);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 70);  //供应商
		Util.initOrgSupplier(SUPLR_NAME, "");
		ListGridField DRIVER1 = new ListGridField("DRIVER1", Util.TI18N.DRIVER1(), 50);  //司机
		ListGridField MOBILE1 = new ListGridField("MOBILE1", Util.TI18N.MOBILE(), 85);  //电话
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","总数量",50);//总数量
		TOT_QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY.setShowGroupSummary(true); 
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(isDigitCanEdit);
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),50);//总数量
		TOT_QNTY_EACH.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY_EACH.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY_EACH.setShowGroupSummary(true); 
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		TOT_QNTY_EACH.setCanEdit(isDigitCanEdit);
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),60);//总毛重
		TOT_GROSS_W.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_GROSS_W.setSummaryFunction(SummaryFunctionType.SUM); 
		TOT_GROSS_W.setShowGroupSummary(true); 
		TOT_GROSS_W.setAlign(Alignment.RIGHT);
		TOT_GROSS_W.setCanEdit(isDigitCanEdit);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),60);//总体积
		//TOT_VOL.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER); 
		TOT_VOL.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_VOL.setAlign(Alignment.RIGHT);
		TOT_VOL.setShowGroupSummary(true); 
		TOT_VOL.setCanEdit(isDigitCanEdit);
		final ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),65);//备注
		NOTES.setCanEdit(true);
		
		groupTable.setFields(LOAD_NO,STATUS_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME, DONE_TIME, UDF1,UDF2,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);	
	}*/
	
	public class CountryRecord extends ListGridRecord {   
		  
	    public CountryRecord() {   
	    }   
	  
	    public CountryRecord(String countryCode, String countryName, String capital, String continent) {   
	        setCountryCode(countryCode);   
	        setCountryName(countryName);   
	        setCapital(capital);   
	        setContinent(continent);   
	    }   
	  
	  
	    public CountryRecord(String countryCode, String countryName, int population) {   
	        setCountryCode(countryCode);   
	        setCountryName(countryName);   
	        setPopulation(population);   
	    }   
	  
	    public CountryRecord(String continent, String countryName, String countryCode, int area, int population, double gdp,   
	                         Date independence, String government, int governmentDesc, String capital, boolean memberG8, String article,   
	                         String background) {   
	  
	        setContinent(continent);   
	        setCountryName(countryName);   
	        setCountryCode(countryCode);   
	        setArea(area);   
	        setPopulation(population);   
	        setGdp(gdp);   
	        setIndependence(independence);   
	        setGovernment(government);   
	        setGovernmentDesc(governmentDesc);   
	        setCapital(capital);   
	        setMemberG8(memberG8);   
	        setArticle(article);   
	        setBackground(background);   
	    }   
	  
	    public void setContinent(String continent) {   
	        setAttribute("continent", continent);   
	    }   
	  
	    public String getContinent() {   
	        return getAttributeAsString("continent");   
	    }   
	  
	    public void setCountryName(String countryName) {   
	        setAttribute("countryName", countryName);   
	    }   
	  
	    public String getCountryName() {   
	        return getAttributeAsString("countryName");   
	    }   
	  
	    public void setCountryCode(String countryCode) {   
	        setAttribute("countryCode", countryCode);   
	    }   
	  
	    public String getCountryCode() {   
	        return getAttributeAsString("countryCode");   
	    }   
	  
	    public void setArea(int area) {   
	        setAttribute("area", area);   
	    }   
	  
	    public int getArea() {   
	        return getAttributeAsInt("area");   
	    }   
	  
	    public void setPopulation(int population) {   
	        setAttribute("population", population);   
	    }   
	  
	    public int getPopulation() {   
	        return getAttributeAsInt("population");   
	    }   
	  
	    public void setGdp(double gdp) {   
	        setAttribute("gdp", gdp);   
	    }   
	  
	    public double getGdp() {   
	        return getAttributeAsDouble("gdp");   
	    }   
	  
	    public void setIndependence(Date independence) {   
	        setAttribute("independence", independence);   
	    }   
	  
	    public Date getIndependence() {   
	        return getAttributeAsDate("independence");   
	    }   
	  
	    public void setGovernment(String government) {   
	        setAttribute("government", government);   
	    }   
	  
	    public String getGovernment() {   
	        return getAttributeAsString("government");   
	    }   
	  
	    public void setGovernmentDesc(int governmentDesc) {   
	        setAttribute("government_desc", governmentDesc);   
	    }   
	  
	    public int getGovernmentDesc() {   
	        return getAttributeAsInt("government_desc");   
	    }   
	  
	    public void setCapital(String capital) {   
	        setAttribute("capital", capital);   
	    }   
	  
	    public String getCapital() {   
	        return getAttributeAsString("capital");   
	    }   
	  
	    public void setMemberG8(boolean memberG8) {   
	        setAttribute("member_g8", memberG8);   
	    }   
	  
	    public boolean getMemberG8() {   
	        return getAttributeAsBoolean("member_g8");   
	    }   
	  
	  
	    public void setArticle(String article) {   
	        setAttribute("article", article);   
	    }   
	  
	    public String getArticle() {   
	        return getAttributeAsString("article");   
	    }   
	  
	    public void setBackground(String background) {   
	        setAttribute("background", background);   
	    }   
	  
	    public String getBackground() {   
	        return getAttributeAsString("background");   
	    }   
	  
	    public String getFieldValue(String field) {   
	        return getAttributeAsString(field);   
	    }   
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TmsWatchView view = new TmsWatchView();
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