package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.AddShpmNoNewAction;
import com.rd.client.action.tms.dispatch.CancelJourneySplitAction;
import com.rd.client.action.tms.dispatch.CancelSplitAction;
import com.rd.client.action.tms.dispatch.ChangedQntyAction;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.action.tms.dispatch.DeleteLoadNoNewAction;
import com.rd.client.action.tms.dispatch.DispatchPrintAction;
import com.rd.client.action.tms.dispatch.ExportShpmItemAction;
import com.rd.client.action.tms.dispatch.LoadCancelAuditAction;
import com.rd.client.action.tms.dispatch.LoadCancelSendNewAction;
import com.rd.client.action.tms.dispatch.LoadDispatchAuditNewAction;
import com.rd.client.action.tms.dispatch.LoadSendConfirmNewAction;
import com.rd.client.action.tms.dispatch.MakeLoadNoNewAction;
import com.rd.client.action.tms.dispatch.QueryUnloadTableAction;
import com.rd.client.action.tms.dispatch.QueryUnshpmTableAction;
import com.rd.client.action.tms.dispatch.SaveLoadNoNewAction;
import com.rd.client.action.tms.dispatch.SplitActionNewWin;
import com.rd.client.action.tms.dispatch.UnShpmPrintNewAction;
import com.rd.client.action.tms.dispatch.UnShpmSaveAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.obj.SysParam;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.GpsEqDS;
import com.rd.client.ds.base.HaulingCapacityManagerDS;
import com.rd.client.ds.base.TempDS;
import com.rd.client.ds.base.VCAreaDS;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.ds.tms.LoadShpmDS;
import com.rd.client.ds.tms.ShpmDS5;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SupplierWin;
import com.rd.client.win.VehicleStaffWin;
import com.rd.client.win.VehicleWin;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理-->车辆调度
 * @author yuanlei
 */
@ClassForNameAble
public class VehicleDispatchView extends SGForm implements PanelFactory {
    
	private DataSource ds;
	//private DataSource carDS;
	private DataSource loadDS;            //调度单数据源
	private DataSource shpmDS;            //已调作业单数据源
	private DataSource unshpmDS;          //待调作业单数据源
	private DataSource unshmplstDS;       //待调作业单明细数据源
	//private DataSource logDS;             //业务日志数据源
	private SectionStack downSectionStack;  //左边按单调度SectionStack布局
	private SectionStack topSectionStack; //右边SectionStack布局
	private SectionStack topSectionStack2;  //
	private Window searchWin;
	private Window shipwin;
	private Window transTrackWin;
	private Window dispLoadWin;
	private Window unDispLoadWin;
	private SGPanel searchForm;
	private SGPanel chooseForm;
	public SGTable unshpmTable;     //待调作业单表
	public SGTable shpmTable;       //已调作业单表
	private SGTable unshpmlstTable;  //待调作业单明细表
	private SGTable loadTable;       //调度单表
	//private SGTable logTable;       //业务日志
	private SGTable carTable;   
	public DynamicForm sumForm;     //汇总FORM
	public DynamicForm pageForm;    //待调分页FORM
	public DynamicForm pageForm1;    //待调分页FORM1
	public DynamicForm pageForm2;    //待调分页FORM1
	public DynamicForm loadPageForm;    //调度单分页FORM
	private VehicleDispatchSubForm subView; //调度配载子页面
	
	public ListGridRecord[] unshpmlstRec;   //待调订单明细的初始值
	private int[] selectRows; //待调订单选中的值
	private int[] selectdetailRows;  //待调订单明细
	public QueryUnshpmTableAction qryUnshpmTableAction;
	public QueryUnloadTableAction qryUnloadTableAction;
	
	private String canModify;   //是否可修改调度单的毛重、体积、净重和货值
	private String canVehInput; //车辆信息是否允许输入
	private IButton shpmButton;
	private IButton saveButton;
	private IButton delButton;
	private IButton canButton;
	private IButton confirmButton;
	private IButton cansendButton;
	
	//private IButton searchButton;
	
	private Record selRecord;   //调度单选中的记录
	private HashMap<String,IButton> sendMap;
	//private ListGridRecord selUnshpmRecord;  //待调作业单选中的记录
	//private ListGridField PLATE_NO;
	
	private ArrayList<ListGridField> suplrLst;
	private ArrayList<ListGridField> vehTypeLst;
	private ArrayList<ListGridField> areaLst;
	private HLayout hLayout;
 	
	private SGPanel carForm;
	
	public String plate_no;
	
	/*public VehicleDispatchView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		suplrLst = new ArrayList<ListGridField>();
		vehTypeLst = new ArrayList<ListGridField>();
		areaLst = new ArrayList<ListGridField>();
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		sendMap = new HashMap<String, IButton>();
		getSysParam();  //获取系统参数
		
		subView = new VehicleDispatchSubForm();	
		//carDS=HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		unshpmDS = ShpmDS5.getInstance("V_SHIPMENT_HEADER_2", "TRANS_SHIPMENT_HEADER");
		shpmDS = LoadShpmDS.getInstance("V_SHIPMENT_HEADER_LOAD", "TRANS_SHIPMENT_HEADER");
		unshmplstDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM_SF", "TRANS_SHIPMENT_ITEM");
		//logDS = TansActLogDS.getInstance("V_CUSTOMACT_LOG_DISPATCH","TRANS_TRANSACTION_LOG");
		ds=HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");
		VLayout main =new VLayout();  //整体布局
		main.setWidth100();
		main.setHeight100();
		
		VStack stack =new VStack();  //主布局(包含待调作业单和调度单上下两个列表)
		stack.setHeight("100%");
		stack.setWidth100();
		
		//上半部分 --yuanlei
		final TabSet topTabSet = new TabSet(); //主布局stack上半部分（包含待调作业单页签）
		//final TabSet downTabSet = new TabSet();
		topTabSet.setWidth100();
		topTabSet.setHeight("50%");
		
	
		//createCarField(carTable);
		
		unshpmTable=new SGTable(unshpmDS, "100%", "100%", false, true, false) {
			
        	//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
                VLayout layout = new VLayout();              
  
                /*unshpmlstTable = new SGTable(unshmplstDS,"100%","50",false,true,false);
                unshpmlstTable.setCanEdit(true);
                unshpmlstTable.setAlign(Alignment.RIGHT);
                unshpmlstTable.setShowRowNumbers(false);
                unshpmlstTable.setAutoFitData(Autofit.VERTICAL);
                unshpmlstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);*/
                

                unshpmlstTable = new SGTable();
                unshpmlstTable.setDataSource(unshmplstDS);
                unshpmlstTable.setWidth("40%");
                unshpmlstTable.setHeight(46);
                unshpmlstTable.setCanEdit(true);
                unshpmlstTable.setAutoFitData(Autofit.VERTICAL);
                unshpmlstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
		        	
		        //作业单明细列表
		        ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.ORD_ROW(), 45);
		        SHPM_ROW.setCanEdit(false);
		        ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(), 110);
		        SKU.setCanEdit(false);
        		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(), 110);
        		SKU_NAME.setCanEdit(false);
		        ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1_NAME",Util.TI18N.TEMPERATURE(), 60);
		        TEMPERATURE1.setCanEdit(false); 
        		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY(),70);
        		QNTY.setAlign(Alignment.RIGHT);
        		QNTY.addEditorExitHandler(new ChangedQntyAction(unshpmlstTable, getView()));
        		ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
        		VOL.setAlign(Alignment.RIGHT);
        		//VOL.addEditorExitHandler(new ChangedVolAction(unshpmlstTable, getView()));
        		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),70);
        		G_WGT.setAlign(Alignment.RIGHT);
        		//G_WGT.addEditorExitHandler(new ChangedGrossWAction(unshpmlstTable, getView()));
        		ListGridField UOM = new ListGridField("UOM",Util.TI18N.UNIT(),70);
        		UOM.setCanEdit(false);
  
        		unshpmlstTable.setFields(SHPM_ROW, SKU, SKU_NAME, QNTY, G_WGT, VOL,UOM, TEMPERATURE1);
        		unshpmlstTable.fetchData(findValues, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						unshpmlstRec = unshpmlstTable.getRecords();
					}
        			
        		});
        		unshpmlstTable.addRecordClickHandler(new RecordClickHandler() {

					@Override
					public void onRecordClick(RecordClickEvent event) {
						ListGridRecord[] records = unshpmlstTable.getSelection();
						selectdetailRows = new int[records.length];
						for(int i = 0; i < records.length; i++) {
							selectdetailRows[i] = unshpmlstTable.getRecordIndex(records[i]);
						}
					}
        			
        		});
        		/*unshpmlstTable.addEditorExitHandler(new EditorExitHandler() {

					@Override
					public void onEditorExit(EditorExitEvent event) {
						unshpmlstTable.selectRecords(selectdetailRows);
					}
        			
        		});*/
                layout.addMember(unshpmlstTable);
                layout.setLayoutLeftMargin(60);
                
                return layout;   
            } 
		};
		unshpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		unshpmTable.setCanEdit(false); 
		unshpmTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		unshpmTable.setCanExpandRecords(true);
		//unshpmTable.setShowGroupSummary(true); 
		createUnshpmField(unshpmTable);
		
		
		topSectionStack =new SectionStack();	  //unshpmTable分页的布局	
		SectionStackSection section=new SectionStackSection(Util.TI18N.UNDISPATCHORDER());
		section.setItems(unshpmTable);
		section.setExpanded(true);
		sumForm = subView.createSumLayout();   //创建汇总布局
		pageForm = new SGPage(unshpmTable, true).initPageBtn();
		section.setControls(sumForm,sumForm, pageForm);
		

		topSectionStack.addSection(section);
    	topSectionStack.setWidth("100%");
    	topSectionStack.setHeight("92%");

    	SGPanel unForm=new SGPanel();
    	unForm.setHeight("20%");
    	unForm.setWidth("80%");
    	createUnForm(unForm);
    	
    	
    	
		DynamicForm topForm1=new DynamicForm();
		topForm1.setIsGroup(true);
		topForm1.setGroupTitle("待调作业单");
		topForm1.setMargin(4);
		topForm1.setPadding(10);
		topForm1.setWidth("50%");
		topForm1.setHeight("100%");
		
		VLayout vlout1=new VLayout();
		vlout1.addMember(unForm);
		vlout1.addMember(topSectionStack);
		vlout1.setWidth("100%");
		vlout1.setHeight("100%");
		topForm1.addChild(vlout1);
   
    	
    	carTable=new SGTable(ds, "100%", "100%", false, true, false);
		carTable.setCanEdit(false); 
    	createVeField(carTable);

    	topSectionStack2=new SectionStack();
    	SectionStackSection section2=new SectionStackSection("车辆调度");
		section2.setItems(carTable);
		section2.setExpanded(true);

		topSectionStack2.addSection(section2);
    	topSectionStack2.setWidth("100%");
    	topSectionStack2.setHeight("100%");
    	
    	carForm=new SGPanel();
    	carForm.setHeight("20%");
    	carForm.setWidth("70%");
    	createCarForm(carForm);
    	
    	
		DynamicForm topForm=new DynamicForm();
		topForm.setIsGroup(true);
		topForm.setGroupTitle("车辆信息");
		topForm.setMargin(4);
//		topForm.setPadding(10);
		topForm.setWidth("45%");
		topForm.setHeight("100%");
		
		
		
		VLayout vlout=new VLayout();
		vlout.addMember(carForm);
		vlout.addMember(topSectionStack2);
		vlout.setWidth("100%");
		vlout.setHeight("100%");
		topForm.addChild(vlout);
        
		hLayout=new HLayout();
    	hLayout.setHeight(Page.getHeight() - 110);
    	hLayout.setWidth100();
    	hLayout.addMember(topForm);
    	hLayout.addMember(topForm1);
    	//上半部分结束   --yuanlei
			
		//下半部分布局  --yuanlei
		
		loadTable = new SGTable(loadDS, "100%", "50%", false, true, false) {
			
//			//第二层表
			protected Canvas getExpansionComponent(final ListGridRecord record) {    
				  
                VLayout layout = new VLayout();              
  
                shpmTable = new SGTable();
                shpmTable.setDataSource(shpmDS);
                shpmTable.setWidth("90%");
                shpmTable.setHeight(50);
                shpmTable.setShowFilterEditor(false);
                shpmTable.setShowAllRecords(true);
                shpmTable.setAutoFetchData(false);
                shpmTable.setAlign(Alignment.RIGHT);
                shpmTable.setShowRowNumbers(true);
                shpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
                shpmTable.setAutoFitData(Autofit.VERTICAL);
                shpmTable.setCanDragRecordsOut(true);   
                shpmTable.setCanAcceptDroppedRecords(true);   
                shpmTable.setCanReorderRecords(true);   
                //shpmTable.setAlternateRecordStyles(true);      
                shpmTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);   
                shpmTable.initBindEvent();
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("LOAD_NO", record.getAttributeAsString("LOAD_NO"));
		        findValues.addCriteria("ALL_FLAG","true");
		        
		        LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_LOAD);
				createListField(shpmTable, listMap);
			
				
				
//        		ListGridField DEPART_TIME = shpmTable.getField("DEPART_TIME");
//        		if(DEPART_TIME != null) {
//	        		DEPART_TIME.setCanEdit(true);
//	        		Util.initDateTime(shpmTable,DEPART_TIME);
//        		}
//        		else {
//        			MSGUtil.sayError("列表中必须配置" + Util.TI18N.END_LOAD_TIME() + "!");
//        		}
        		
     
        		shpmTable.fetchData(findValues, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						Util.db_async.getPageInfo(new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								
								
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								if(result != null && result.size() > 2) {
									shpmTable.setProperty("WHERE", result.get(2));
								}
							}
						});
					}
					
				});
        		
        		final Menu load_menu = new Menu();   //调度单右键
        		load_menu.setWidth(140);
//        		MenuItemSeparator itemSeparator =new MenuItemSeparator();
        		
//        		if(isPrivilege(TrsPrivRef.DISPATCH_P2_28)) {                      //wangjun 2011-3-9
//        			
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_17)) {
//		        	    MenuItem removeItem = new MenuItem("移除作业单",StaticRef.ICON_DEL);
//		        	    load_menu.addItem(removeItem);
//		        	    removeItem.addClickHandler(new RemoveShpmNoAction(shpmTable, unshpmTable, loadTable));
//	        	    }
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_18)) {
//	        	    	load_menu.addItem(itemSeparator);
//		        	    MenuItem sendconfirmItem = new MenuItem(Util.BI18N.SENDCONFIRM(),StaticRef.ICON_CONFIRM);
//		        	    load_menu.addItem(sendconfirmItem);
//		        	    sendconfirmItem.addClickHandler(new ShpmSendConfirmAction(shpmTable, loadTable));
//	        	    }
//	        	    
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_19)) {
//		        	    MenuItem cansendItem = new MenuItem(Util.BI18N.CANCELSEND(),StaticRef.ICON_CANCEL);
//		        	    load_menu.addItem(cansendItem);
//		        	    cansendItem.addClickHandler(new ShpmCancelSendAction(shpmTable, loadTable));
//	        	    }
//	        	    
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_20)) {
//	        	    	load_menu.addItem(itemSeparator);
//		        	    MenuItem saveItem = new MenuItem(Util.BI18N.SAVE(),StaticRef.ICON_SAVE);
//		        	    load_menu.addItem(saveItem);
//		        	    saveItem.addClickHandler(new ShpmSaveAction(shpmTable));
//	        	    }
//	        	    if(true) {
//	        	    	load_menu.addItem(itemSeparator);
//		        	    MenuItem expItem = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);
//		        	    load_menu.addItem(expItem);
//		        	    expItem.addClickHandler(new ExportAction(shpmTable));
//	        	    }
//	        	    shpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
//	                    public void onShowContextMenu(ShowContextMenuEvent event) {
//	                    	load_menu.showContextMenu();
//	                        event.cancel();
//	                    }
//	                });
//        		}
        	    
        		layout.addMember(shpmTable);
        		layout.setLayoutTopMargin(5);
                layout.setLayoutLeftMargin(35);
                
                return layout;   
            }   
		};
		createLoadField(loadTable);
		loadTable.setCanExpandRecords(true);
		//loadTable.setShowGridSummary(true);
		loadTable.setShowFilterEditor(false);
		loadTable.setCanEdit(true);
		loadTable.setShowRowNumbers(true);       
		loadTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		loadTable.initBindEvent();
		
		downSectionStack =new SectionStack();   //loadTable分页的布局	
		loadPageForm = new SGPage(loadTable, true).initPageBtn();
		SectionStackSection listItem=new SectionStackSection(Util.TI18N.LISTINFO());//
		listItem.setItems(loadTable);
		listItem.setExpanded(true);
		
		listItem.setControls(subView.createTPBtn(downSectionStack,hLayout),subView.createTDBtn(downSectionStack,hLayout), loadPageForm);
		downSectionStack.addSection(listItem);
		downSectionStack.setWidth("100%");
		downSectionStack.setHeight(25);
		
		downSectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		downSectionStack.setAnimateSections(false);
		
//		logTable = new SGTable(logDS,"100%", "50%");
//		createTransLogField(logTable);
//		logTable.setShowRowNumbers(true);
//		logTable.setShowFilterEditor(false);
//		logTable.setCanEdit(false);
		

    	
    	ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		//lay.addMember(toolStrip);
        //lay.addMember(topSectionStack);
		
		//下半部分布局结束 --yuanlei
		
		// 按钮布局

		stack.addMember(toolStrip);
		stack.addMember(hLayout);
		stack.addMember(downSectionStack);
		main.addMember(stack);
		
		
		
		unshpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] records = unshpmTable.getSelection();
				if(records != null && records.length > 0) {
					selectRows = new int[records.length];
					double vol = 0.0000, g_w = 0.0000, qnty = 0.0000;
					for(int i = 0; i < records.length; i++) {
						vol += Double.parseDouble(ObjUtil.ifObjNull(records[i].getAttribute("TOT_VOL"), "0").toString());
						g_w += Double.parseDouble(ObjUtil.ifObjNull(records[i].getAttribute("TOT_GROSS_W"), "0").toString());
						qnty += Double.parseDouble(ObjUtil.ifObjNull(records[i].getAttribute("TOT_QNTY"), "0").toString());

						selectRows[i] = unshpmTable.getRecordIndex(records[i]);
					}
					sumForm.setValue("CUR_COUNT", records.length);
					sumForm.setValue("CUR_VOL", vol);
					sumForm.setValue("CUR_GROSS_W", g_w);
					sumForm.setValue("CUR_QNTY", qnty);
					setButtonEnabled(TrsPrivRef.Vehicle_P1_02, shpmButton, true);
				}
			}
			
		});
	
		unshpmTable.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				unshpmTable.selectRecords(selectRows);
			}
			
		});
		initVerify();
		addRightKey();   //待调作业单右键	
		initCombo();

    	//doSearch1(topSectionStack.getSection(0),unForm);
    	doAllSearch(topSectionStack.getSection(0),unForm);
    	
		return main;
			
	}


	private void createLoadField(final SGTable groupTable) {
		boolean isDigitCanEdit = false;
		if(ObjUtil.ifNull(canModify,"N").equals("Y")) {
			isDigitCanEdit = true;
		}
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),90);//调度单编号
		//LOAD_NO.setShowGridSummary(true);
		//LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		LOAD_NO.setCanEdit(false);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()),85);//车牌号
		ListGridField TRAILER_NO = new ListGridField("TRAILER_NO","挂车号",70);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//状态
		STATUS_NAME.setCanEdit(false);
		ListGridField DISPATCH_STAT_NAME = new ListGridField("DISPATCH_STAT_NAME", Util.TI18N.DISPATCH_STAT_NAME(), 60);  //配车状态
		DISPATCH_STAT_NAME.setCanEdit(false);
		
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYP(),60);//车辆类型
		//Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
		vehTypeLst.add(VEHICLE_TYP);
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),60);
		TRANS_SRVC_ID.setHidden(true);
		//Util.initTrsService(TRANS_SRVC_ID, "");
		
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME", Util.TI18N.TRANS_SRVC_ID(),1);
		TRANS_SRVC_ID_NAME.setCanEdit(false);
		
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField START_AREA = new ListGridField("START_AREA_NAME",ColorUtil.getRedTitle("起点城市"),65);//起点区域
		START_AREA_ID.setCanEdit(false);
		START_AREA_ID.setHidden(true);
		initArea(loadTable,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
		areaLst.add(START_AREA);
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField END_AREA = new ListGridField("END_AREA_NAME",ColorUtil.getRedTitle("终点城市"),65);//终点区域
		END_AREA_ID.setCanEdit(false);
		END_AREA_ID.setHidden(true);
		initArea(loadTable,END_AREA, "END_AREA_ID", "END_AREA_NAME", "");
		areaLst.add(END_AREA);
		//ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		//Util.initDateTime(loadTable,DEPART_TIME);
	
		ListGridField GPS_NO1 = new ListGridField("GPS_NO1","GPS设备号", 60);  
		GPS_NO1.setHidden(true);		
		ListGridField GPS_NO1_NAME = new ListGridField("GPS_NO1_NAME","GPS设备号",70);
		initGps(loadTable,GPS_NO1_NAME,"GPS_NO1");
		GPS_NO1_NAME.setCanEdit(true);
		
//		
//		ListGridField DONE_TIME = new ListGridField("DONE_TIME", "预计回场时间", 110);  //发运时间
//		//Util.initListGridDateTime(DEPART_TIME);
//		Util.initDateTime(loadTable,DONE_TIME);
		
		ListGridField REMAIN_GROSS_W = new ListGridField("REMAIN_GROSS_W","余量",45);//余量
		REMAIN_GROSS_W.setCanEdit(false);
		
		ListGridField TEMP_NO1 = new ListGridField("TEMP_NO1","温控设备1",65);
		TEMP_NO1.setHidden(true);
		ListGridField TEMP_NO1_NAME = new ListGridField("TEMP_NO1_NAME","温控设备1",70);
		//Util.initComboValue(TEMP_NO1, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "");
		initTemp(loadTable,TEMP_NO1_NAME,"TEMP_NO1");
		TEMP_NO1_NAME.setCanEdit(true);
		
		ListGridField TEMP_NO2 = new ListGridField("TEMP_NO2","温控设备2",70);		
		//initTemp(loadTable,TEMP_NO2,"ID", "EQUIP_NO");
		TEMP_NO2.setHidden(true);
		//comboWidget[1]=TEMP_NO2;
		//Util.initComboValue(comboWidget, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "","");
		ListGridField TEMP_NO2_NAME = new ListGridField("TEMP_NO2_NAME","温控设备2",65);
		//Util.initComboValue(TEMP_NO1, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "");
		initTemp(loadTable,TEMP_NO2_NAME,"TEMP_NO2");
		TEMP_NO2_NAME.setCanEdit(true);
		
		final ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.LOAD_UDF21(),65);//随车特服
		UDF1.setCanEdit(true);
		
		//ListGridField UDF2 = new ListGridField("UDF2", Util.TI18N.LOAD_UDF22(), 85);  //电话
		
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 80);  //供应商
		EXEC_ORG_ID_NAME.setCanEdit(false);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 85);  //供应商
		SUPLR_ID.setHidden(true);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 70);  //供应商
		FormItemIcon searchPicker = new FormItemIcon();
		SUPLR_NAME.setIcons(searchPicker);
		SUPLR_NAME.setShowSelectedIcon(true);
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SupplierWin(groupTable,itemRow,"20%","32%").getViewPanel();		
			}
		});
		SUPLR_NAME.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				final String sup_name = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
				if(sup_name.equals("")){
					return;
				}
				Util.db_async.getRecord("ID,SUPLR_CNAME", "V_SUPPLIER", 
						" where upper(full_index) like upper('%"+sup_name+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						int size = result.size();
						HashMap<String, String> selectRecord = null;
						if(size > 1){
							for (HashMap<String, String> hashMap : result) {
								if(hashMap.get("ID").equals(groupTable.getEditValue(row, "SUPLR_ID"))){
									selectRecord = hashMap;
									break;
								}
							}
						}
						if(size == 1 || selectRecord != null){
							if(selectRecord == null)selectRecord = result.get(0);
							groupTable.setEditValue(row, "SUPLR_ID", selectRecord.get("ID"));
							groupTable.setEditValue(row, "SUPLR_NAME", selectRecord.get("SUPLR_CNAME"));
						}else if(size > 1){
							//groupTable.setProperty("CUSTOMER_ID", vm.getValueAsString("CUSTOMER_ID"));
							//groupTable.setProperty("FULL_INDEX", sup_name);
							new SupplierWin(groupTable,itemRow,"20%", "32%",sup_name).getViewPanel();
						}else if(size == 0){
							MSGUtil.sayError("未找到承运商信息!");
							groupTable.setEditValue(row, "SUPLR_ID", "");
							groupTable.setEditValue(row, "SUPLR_NAME", "");
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
		});
		
		//Util.initOrgSupplier(SUPLR_NAME, "");
		//suplrLst.add(SUPLR_NAME);
		ListGridField DRIVER_ID = new ListGridField("DRIVER_ID", "", 75);  //司机ID
		DRIVER_ID.setHidden(true);
		
		ListGridField DRIVER1 = new ListGridField("DRIVER1", ColorUtil.getRedTitle(Util.TI18N.DRIVER1()), 75);  //司机
		FormItemIcon icon1 = new FormItemIcon();
		DRIVER1.setIcons(icon1);
		DRIVER1.setShowSelectedIcon(true);
		icon1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new VehicleStaffWin(groupTable,itemRow,"20%", "32%").getViewPanel();
			}
		});		
		DRIVER1.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				final String driver = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
				if(driver.equals("")){
					return;
				}
				Util.db_async.getRecord("ID,STAFF_NAME,MOBILE", "V_BAS_STAFF", 
						" where upper(full_index) like upper('%"+driver+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						int size = result.size();
						HashMap<String, String> selectRecord = null;
						if(size == 1 || selectRecord != null){
							if(selectRecord == null)selectRecord = result.get(0);
							loadTable.getSelectedRecord().setAttribute("DRIVER_ID",  ObjUtil.ifObjNull(selectRecord.get("ID"), "").toString());
							loadTable.setEditValue(row, "DRIVER1", ObjUtil.ifObjNull(selectRecord.get("STAFF_NAME"), "").toString());
							loadTable.setEditValue(row, "MOBILE1", ObjUtil.ifObjNull(selectRecord.get("MOBILE"), "").toString());
						}else if(size > 1){
							new VehicleStaffWin(groupTable,itemRow,"20%", "32%",driver).getViewPanel();
						}else if(size == 0){
							MSGUtil.sayError("未找到司机信息!");
							//groupTable.setEditValue(row, "DRIVER1", "");
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
		});
		ListGridField MOBILE1 = new ListGridField("MOBILE1", ColorUtil.getRedTitle(Util.TI18N.MOBILE()), 85);  //电话
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","总数量",50);//总数量
		//TOT_QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		//TOT_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		//TOT_QNTY.setShowGroupSummary(true); 
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_QNTY", Util.TI18N.TOT_QNTY()));
		}
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),50);//总数量
		//TOT_QNTY_EACH.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		//TOT_QNTY_EACH.setSummaryFunction(SummaryFunctionType.SUM);
		//TOT_QNTY_EACH.setShowGroupSummary(true); 
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		TOT_QNTY_EACH.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY_EACH.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_QNTY_EACH", Util.TI18N.R_EA()));
		}
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),55);//总毛重
		//TOT_GROSS_W.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		//TOT_GROSS_W.setSummaryFunction(SummaryFunctionType.SUM); 
		//TOT_GROSS_W.setShowGroupSummary(true); 
		TOT_GROSS_W.setAlign(Alignment.RIGHT);
		TOT_GROSS_W.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_GROSS_W.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_GROSS_W", Util.TI18N.TOT_GROSS_W()));
		}
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),55);//总体积
		//TOT_VOL.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER); 
		//TOT_VOL.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_VOL.setAlign(Alignment.RIGHT);
		//TOT_VOL.setShowGroupSummary(true); 
		TOT_VOL.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_VOL.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_VOL", Util.TI18N.TOT_VOL()));
		}
		final ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),65);//备注
		NOTES.setCanEdit(true);
		
		//ListGridField VEH_SIGN=new ListGridField("VEH_SIGN","车标号",80);
		
		groupTable.setFields(LOAD_NO,STATUS_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, TRANS_SRVC_ID_NAME, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA,SUPLR_ID, SUPLR_NAME, PLATE_NO,TRAILER_NO,VEHICLE_TYP
				,DRIVER_ID,DRIVER1, MOBILE1, REMAIN_GROSS_W,TEMP_NO1,TEMP_NO1_NAME,TEMP_NO2,TEMP_NO2_NAME,GPS_NO1,GPS_NO1_NAME,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W);	
		
		if(ObjUtil.ifNull(canVehInput,"N").equals("N")) {
			FormItemIcon icon = new FormItemIcon();
			PLATE_NO.setIcons(icon);
			PLATE_NO.setShowSelectedIcon(true);
			icon.addFormItemClickHandler(new FormItemClickHandler() {
				
				@Override
				public void onFormItemClick(FormItemIconClickEvent event) {
					Util.db_async.getSingleRecord("TEMPERATURE1,TEMPERATURE2,TOT_GROSS_W", "TRANS_LOAD_HEADER", 
							" where LOAD_NO = '" + selRecord.getAttribute("LOAD_NO") + "'", null, new AsyncCallback<HashMap<String, String>>() {
								@Override
								public void onFailure(Throwable caught) {
							
								}

								@Override
								public void onSuccess(
										HashMap<String, String> result) {
									if(result != null && result.size() > 0) {
										HashMap<String,String> map = new HashMap<String,String>();
										String TEMPERATURE1 = ObjUtil.ifNull(result.get("TEMPERATURE1"),"");
										String TEMPERATURE2 = ObjUtil.ifNull(result.get("TEMPERATURE2"),"");
										String TOT_GROSS_W = result.get("TOT_GROSS_W");
										String tmp_attr = StaticRef.TMP_SINGLE;
										if (!TEMPERATURE1.equals(TEMPERATURE2)) {
											tmp_attr = StaticRef.TMP_DOUBLE;
										}
										map.put("TMP_ATTR", tmp_attr);
										map.put("REMAIN_GROSS_W",TOT_GROSS_W);
										map.put("AVAIL_FLAG",StaticRef.AVAIL_FLAG);
										if(groupTable.getEditValue(groupTable.getEditRow(), "SUPLR_ID")!=null){
											map.put("SUPLR_ID", ObjUtil.ifObjNull(groupTable.getEditValue(groupTable.getEditRow(), "SUPLR_ID"),"").toString());
										}else{
											map.put("SUPLR_ID", groupTable.getSelectedRecord().getAttribute("SUPLR_ID"));
										}
										new VehicleWin(groupTable,itemRow,"20%", "32%", map).getViewPanel();
									}
									else {
										new VehicleWin(groupTable,itemRow,"20%", "32%").getViewPanel();
									}
								}						
					});					
				}
			});
		}
		PLATE_NO.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				ListGridRecord record=loadTable.getSelectedRecord();
				if(record==null){
					MSGUtil.sayError("请选择记录");
					return;
				}
				final int row = event.getRowNum();
				final String plate_no = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
				if(plate_no.equals("")){
					return;
				}
				Util.db_async.getRecord("PLATE_NO,VEHICLE_TYP_ID,DRIVER1_NAME,TRAIL_PLATE_NO,MOBILE1", "V_BAS_VEHICLE", 
						" where SUPLR_ID = '"+ObjUtil.ifObjNull(loadTable.getSelectedRecord().getAttributeAsObject("SUPLR_ID"),groupTable.getEditValue(groupTable.getEditRow(), "SUPLR_ID") )+"' and upper(PLATE_NO) like upper('%"+plate_no+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						int size = result.size();
						HashMap<String, String> selectRecord = null;
						if(size == 1 || selectRecord != null){
							if(selectRecord == null)selectRecord = result.get(0);
							loadTable.setEditValue(row, "PLATE_NO", ObjUtil.ifObjNull(selectRecord.get("PLATE_NO"), "").toString());
							loadTable.setEditValue(row, "VEHICLE_TYP_ID", ObjUtil.ifObjNull(selectRecord.get("VEHICLE_TYP_ID"), "").toString());
							loadTable.setEditValue(row, "DRIVER1", ObjUtil.ifObjNull(selectRecord.get("DRIVER1_NAME"), "").toString());
							loadTable.setEditValue(row, "MOBILE1", ObjUtil.ifObjNull(selectRecord.get("MOBILE1"), "").toString());
							loadTable.setEditValue(row, "TRAILER_NO", ObjUtil.ifObjNull(selectRecord.get("TRAIL_PLATE_NO"), "").toString());
						}else if(size > 1){
							String sup_id="";
							if(groupTable.getEditValue(groupTable.getEditRow(), "SUPLR_ID")!=null){
								sup_id=ObjUtil.ifObjNull(groupTable.getEditValue(groupTable.getEditRow(), "SUPLR_ID"),"").toString();
							}else{
								sup_id=groupTable.getSelectedRecord().getAttribute("SUPLR_ID");
							}
							new VehicleWin(groupTable,itemRow,"20%", "32%",plate_no,sup_id).getViewPanel();
						}
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
		});		
		
//		PLATE_NO.addEditorExitHandler(new EditorExitHandler() {
//
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				final int row = event.getRowNum();
//				String plate_no = ObjUtil.ifObjNull(event.getNewValue(), "").toString();
//				if(ObjUtil.isNotNull(plate_no)) {
//					Util.db_async.getSingleRecord("PLATE_NO,VEHICLE_TYP_ID,DRIVER1_NAME,MOBILE1", "V_BAS_VEHICLE"
//							, " where ENABLE_FLAG = 'Y' and PLATE_NO = '" + plate_no + "'", null, new AsyncCallback<HashMap<String, String>>() {
//
//								@Override
//								public void onFailure(Throwable caught) {					
//								}
//
//								@Override
//								public void onSuccess(HashMap<String, String> result) {
//									if(result != null && result.size() > 0) {
//										loadTable.setEditValue(row, "PLATE_NO", ObjUtil.ifObjNull(result.get("PLATE_NO"), "").toString());
//										loadTable.setEditValue(row, "VEHICLE_TYP_ID", ObjUtil.ifObjNull(result.get("VEHICLE_TYP_ID"), "").toString());
//										loadTable.setEditValue(row, "DRIVER1", ObjUtil.ifObjNull(result.get("DRIVER1_NAME"), "").toString());
//										loadTable.setEditValue(row, "MOBILE1", ObjUtil.ifObjNull(result.get("MOBILE1"), "").toString());
//									}
//									else {
//										loadTable.setEditValue(row, "VEHICLE_TYP_ID", "");
//										loadTable.setEditValue(row, "DRIVER1", "");
//										loadTable.setEditValue(row, "MOBILE1", "");
//									}
//								}
//						
//					});
//				}
//			}
//			
//		});
		
		
		loadTable.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				//enableOrDisables(save_map, false);
				selRecord = event.getRecord();
				plate_no = selRecord.getAttribute("PLATE_NO");
				loadTable.OP_FLAG = "M";
				itemRow = loadTable.getRecordIndex(selRecord);
			
				if(selRecord != null) {
					if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
						enableOrDisables(del_map, true);
						enableOrDisables(save_map, false);
						setSendBtnStatus(true);
					}
					else if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_DEPART_NAME)){
						enableOrDisables(del_map, false);
						enableOrDisables(save_map, false);
						setSendBtnStatus(false);
					}
					else {
						enableOrDisables(del_map, false);
						enableOrDisables(save_map, false);
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_06, confirmButton, false);
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, false);
					}
				}
			}
			
		});
		loadTable.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(selRecord != null && selRecord.getAttribute("STATUS_NAME").equals("完全发运")){
					loadTable.setCanEdit(false);
					return;
				}
				
				
				if(selRecord != null && selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
					
					if(selRecord.getAttribute("DISPATCH_STAT_NAME")!=null&&selRecord.getAttribute("DISPATCH_STAT_NAME").equals(StaticRef.NO_DISPATCH_NAME)) {
						loadTable.setCanEdit(true);
						loadTable.getField("START_AREA_NAME").setCanEdit(false);
						loadTable.getField("END_AREA_NAME").setCanEdit(false);
						loadTable.getField("TRANS_SRVC_ID_NAME").setCanEdit(false);
						loadTable.getField("SUPLR_NAME").setCanEdit(true);
						loadTable.getField("PLATE_NO").setCanEdit(true);
						loadTable.getField("VEHICLE_TYP_ID").setCanEdit(true);
						loadTable.getField("DRIVER1").setCanEdit(true);
						loadTable.getField("MOBILE1").setCanEdit(true);
						loadTable.getField("TRAILER_NO").setCanEdit(true);
						NOTES.setCanEdit(true);
					}
					else {
						loadTable.setCanEdit(true);
						loadTable.getField("START_AREA_NAME").setCanEdit(false);
						loadTable.getField("END_AREA_NAME").setCanEdit(false);
						loadTable.getField("TRANS_SRVC_ID_NAME").setCanEdit(false);
						loadTable.getField("TEMP_NO2_NAME").setCanEdit(true);
						loadTable.getField("TEMP_NO1_NAME").setCanEdit(true);
						loadTable.getField("GPS_NO1_NAME").setCanEdit(true);
						loadTable.getField("SUPLR_NAME").setCanEdit(false);
						loadTable.getField("PLATE_NO").setCanEdit(false);
						loadTable.getField("VEHICLE_TYP_ID").setCanEdit(false);
						loadTable.getField("DRIVER1").setCanEdit(false);
						loadTable.getField("MOBILE1").setCanEdit(false);
						loadTable.getField("TRAILER_NO").setCanEdit(false);
						loadTable.getField("NOTES").setCanEdit(false);
						
					}
					
					//loadTable.getField("DEPART_TIME").setCanEdit(true);
					enableOrDisables(del_map, false);  //不能删除
					enableOrDisables(save_map, true);  //可以保存和取消
					//enableOrDisables(sendMap, true);   //可以发运确认
					setSendBtnStatus(true);
				}
				else{
					loadTable.setCanEdit(true);
					//loadTable.getField("DEPART_TIME").setCanEdit(true);
					enableOrDisables(del_map, false);
					setButtonEnabled(TrsPrivRef.DISPATCH_P2_03, saveButton, false);
					setButtonEnabled(TrsPrivRef.DISPATCH_P2_05, canButton, false);
					setButtonEnabled(TrsPrivRef.DISPATCH_P2_06, confirmButton, false);
					if(selRecord != null && selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_DEPART_NAME)) {
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, true);
					}
					else {
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, false);
					}
				}
			}
			
		});
	
		
	
		final Menu menu = new Menu();//主界面【调度单信息】页签：右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_01)) {
		    MenuItem printItem = new MenuItem("打印调度单",StaticRef.ICON_PRINT);
		    printItem.addClickHandler(new DispatchPrintAction(loadTable));
			menu.addItem(printItem);
	    }
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_02)) {
			MenuItem printItem2 = new MenuItem("打印调度单",StaticRef.ICON_PRINT);
		    printItem2.addClickHandler(new DispatchPrintAction(loadTable));
			menu.addItem(printItem2);
			Menu m = new Menu();
			MenuItem b2bPrintItem = new MenuItem("打印冷运专运调度单",StaticRef.ICON_PRINT);
			b2bPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "b2bDispatch").onClick(event);
				}
			});
			MenuItem gxPrintItem = new MenuItem("打印干线调度单",StaticRef.ICON_PRINT);
			gxPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "arteryDispatch").onClick(event);
				}
			});
			MenuItem psPrintItem = new MenuItem("打印零担派送调度单",StaticRef.ICON_PRINT);
			psPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "sendDispatch").onClick(event);
				}
			});
	//		MenuItem tfqdPrintItem = new MenuItem("打印提货清单",StaticRef.ICON_PRINT);
	//		tfqdPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "deliveryManifest"));
			MenuItem tfPrintItem = new MenuItem("打印零担提货调度单",StaticRef.ICON_PRINT);
			tfPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "deliveryDispatch").onClick(event);
				}
			});
	
			MenuItem b2cPrintItem = new MenuItem("打印冷运速配汇总出库单",StaticRef.ICON_PRINT);
			b2cPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "b2cArtDispatch").onClick(event);
				}
			});
			MenuItem b2cPrintItem2 = new MenuItem("打印冷运速配明细出库单",StaticRef.ICON_PRINT);
			b2cPrintItem2.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "b2cDispatch").onClick(event);
				}
			});
			
			MenuItem shopPrintItem = new MenuItem("打印冷运速配调度出库单",StaticRef.ICON_PRINT);
			shopPrintItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					new DispatchPrintAction(loadTable, "shopDispatch").onClick(event);
				}
			});
			
			
			m.addItem(b2bPrintItem);
			m.addItem(gxPrintItem);
			m.addItem(psPrintItem);
	//		m.addItem(tfqdPrintItem);
			m.addItem(tfPrintItem);
			m.addItem(b2cPrintItem);
			m.addItem(b2cPrintItem2);
			m.addItem(shopPrintItem);
			//m.addItem(newDispatch);
	//		m.addItem(b2cRoutePrintItem);
			printItem2.setSubmenu(m);
	    }
	    
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_03)) {                                          //wangjun 2010-3-24
			 MenuItem shpmList = new MenuItem("相关作业单信息",StaticRef.ICON_NEW);  //相关作业单信息
		     menu.addItem(shpmList);
		     shpmList.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				  @Override
				  public void onClick(MenuItemClickEvent event) {
					  ListGridRecord loadRec = loadTable.getSelectedRecord();
					  unDispLoadWin = new UnDispLoadWin(loadRec.getAttribute("LOAD_NO")).getViewPanel();
				  }
			 });
		}

	    if(isPrivilege(TrsPrivRef.Vehicle_P3_04)) {
			 MenuItem skuList = new MenuItem(Util.BI18N.GOODS_INFO(),StaticRef.ICON_NEW);  //货品信息
		     menu.addItem(skuList);
		     skuList.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				  @Override
				  public void onClick(MenuItemClickEvent event) {
					  ListGridRecord loadRec = loadTable.getSelectedRecord();
					  Criteria crit = loadTable.getCriteria();
				        if(crit != null && Util.iff(crit.getAttributeAsString("HISTORY_FLAG"),"false").equals("true")) {
				        	dispLoadWin = new DispLoadNewWin(getView(),loadRec.getAttribute("LOAD_NO"),"true").getViewPanel();
				        }
				        else {
				        	dispLoadWin = new DispLoadNewWin(getView(),loadRec.getAttribute("LOAD_NO")).getViewPanel();
				        }
				  }
			 });
		}
	
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_05) || isPrivilege(TrsPrivRef.Vehicle_P3_06)) {
		    if(isPrivilege(TrsPrivRef.Vehicle_P3_05)) {
		    	menu.addItem(itemSeparator);
				MenuItem skuList = new MenuItem(Util.BI18N.DISPATCH_AUDIT(),StaticRef.ICON_CONFIRM); //配车审核
			    menu.addItem(skuList);
			    skuList.addClickHandler(new LoadDispatchAuditNewAction(loadTable,check_map, getView()));
			}
		    if(isPrivilege(TrsPrivRef.Vehicle_P3_06)) {
				MenuItem skuList = new MenuItem(Util.BI18N.CANCEL_AUDIT(),StaticRef.ICON_CANCEL);  //取消审核
			    menu.addItem(skuList);
			    skuList.addClickHandler(new LoadCancelAuditAction(loadTable));
			    
			}

	    }
	    
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_07)) {  //刷新  （调度刷新）
	    	menu.addItem(itemSeparator);
		    MenuItem refreshItem = new MenuItem(Util.BI18N.REFRESH(),StaticRef.ICON_REFRESH);
		    menu.addItem(refreshItem);
		    qryUnloadTableAction = new QueryUnloadTableAction(loadTable, pageForm);
		    refreshItem.addClickHandler(qryUnloadTableAction);
		    
	    }
	    
	    if(isPrivilege(TrsPrivRef.Vehicle_P3_08)) {
			MenuItem expList = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);  //导出
		    menu.addItem(expList);
		    expList.addClickHandler(new ExportAction(loadTable));
	    } 

	    loadTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
	    
	}
	
	private void createCarForm(final SGPanel form){

	    SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
		Util.initSupplier(SUPLR_ID, "");
		SUPLR_ID.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		final SGCombo VEHICLE_TYP_ID = new SGCombo("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYPE());
		Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
		VEHICLE_TYP_ID.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		final SGCombo VEHICLE_STAT = new SGCombo("VEHICLE_STAT",Util.TI18N.VEHICLE_STAT());
		Util.initCodesComboValue(VEHICLE_STAT, "VECHICLE_STAT");
		VEHICLE_STAT.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		SGCombo VEHICLE_ATTR = new SGCombo("VEHICLE_ATTR", Util.TI18N.VEHICLE_ATTR());
		Util.initCodesComboValue(VEHICLE_ATTR, "VECHILE_ATTR");
		VEHICLE_ATTR.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		SGText MAX_WEIGHT = new SGText("MAX_WEIGHT","载重量大于");
		MAX_WEIGHT.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		SGText PLATE_NO = new SGText("PLATE_NO",Util.TI18N.PLATE_NO(),true);
		PLATE_NO.setWidth( new Double(Page.getWidth()*0.08).intValue());
		
		SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
		
	      searchButton1.addClickHandler(
	  		    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
	  			
	  			@Override
	  			public void onClick(
	  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
	  				doCarSearch(topSectionStack2.getSection(0),form);
	  				
	  			}
	  		});
	  		
		
	    form.setItems( SUPLR_ID, VEHICLE_TYP_ID,VEHICLE_STAT,VEHICLE_ATTR, PLATE_NO,MAX_WEIGHT,searchButton1);
	
	}
	
	 private void createUnForm(final SGPanel form){
			final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
			CUSTOMER_ID.setVisible(false);
			
			final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
			CUSTOMER_NAME.setStartRow(true);
			CUSTOMER_NAME.setWidth( new Double(Page.getWidth()*0.08).intValue());
			CUSTOMER_NAME.setColSpan(2);
			CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
			CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
			Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
			
			
			//SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", "运输类型"); 
			//Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
			//BIZ_TYP.setWidth( new Double(Page.getWidth()*0.08).intValue());
			
//			SGCombo ODR_TYP = new SGCombo("ODR_TYP", "订单类型");
//			Util.initCodesComboValue(ODR_TYP, "ORD_TYP");
//			ODR_TYP.setWidth( new Double(Page.getWidth()*0.08).intValue());
			
			
			
			SGCombo VECHILE_TYP_ID = new SGCombo("VEHICLE_TYP_ID","车辆类型");
			Util.initComboValue(VECHILE_TYP_ID, "BAS_VEHICLE_TYPE", "ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", "");
			VECHILE_TYP_ID.setWidth( new Double(Page.getWidth()*0.08).intValue());
			
			
			SGText SHPM_NO=new SGText("SHPM_NO", "作业单号");
			SHPM_NO.setWidth( new Double(Page.getWidth()*0.08).intValue());
			
			SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
			CUSTOM_ODR_NO_NAME.setWidth( new Double(Page.getWidth()*0.08).intValue());
			
            SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME(),true);//发货方
            LOAD_NAME.setWidth( new Double(Page.getWidth()*0.08).intValue());
            
            SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
            UNLOAD_NAME.setWidth( new Double(Page.getWidth()*0.08).intValue());
            
            SGDateTime PRE_LOAD_TIME_FROM=new SGDateTime("PRE_LOAD_TIME_FROM", "要求发货时间");
            PRE_LOAD_TIME_FROM.setWidth( new Double(Page.getWidth()*0.08).intValue());
            SGDateTime PRE_LOAD_TIME_TO=new SGDateTime("PRE_LOAD_TIME_TO", "到");
            PRE_LOAD_TIME_TO.setWidth( new Double(Page.getWidth()*0.08).intValue());
            
    		SGText EXEC_ORG_ID = new SGText("EXEC_ORG_ID",Util.TI18N.EXEC_ORG_ID());
    		EXEC_ORG_ID.setVisible(false);
    	    //二级窗口 EXEC_ORG_ID 执行结构
    		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
    		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
    		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
    		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
    		EXEC_ORG_ID_NAME.setWidth(120);
    		EXEC_ORG_ID_NAME.setVisible(false);
            
    		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
    		C_ORG_FLAG.setColSpan(2);
    		C_ORG_FLAG.setValue(true);
    		C_ORG_FLAG.setVisible(false);
            
            SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
    		
  	        searchButton1.addClickHandler(
  	  		    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
  	  			
  	  			@Override
  	  			public void onClick(
  	  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
  	  			
  	  				doSearch1(topSectionStack.getSection(0),form);
  	  			
  	  			}
  	  		});
  	        
  	    
  	        SGButtonItem searchButton2=new SGButtonItem("高级查询","");
  	        searchButton2.addClickHandler(
  	    		 new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
  	    			@Override
  	  	  			public void onClick(
  	  	  					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(searchWin!=null){
					searchWin.hide();
				}
				if(shipwin==null){
					chooseForm=new SGPanel();
					shipwin= new UnshpmQueryWin(590,425,unshpmDS,subView.createUnshpmQueryForm(chooseForm),topSectionStack.getSection(0)).getViewPanel();
				}
				else{
					shipwin.show();
				}
			}
		});
  	      
  	      form.setNumCols(12);
  	      form.setItems(CUSTOMER_ID,CUSTOMER_NAME,VECHILE_TYP_ID,SHPM_NO,CUSTOM_ODR_NO_NAME,LOAD_NAME,UNLOAD_NAME,PRE_LOAD_TIME_FROM,PRE_LOAD_TIME_TO,searchButton1,searchButton2,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
  	       	     
	 }
	
	
	/**
	 * 业务日志
	 * @author 
	 * @param logTable
	 */
//	private void createTransLogField(SGTable logTable) {
//
//		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.SHPM_NO(),150);
//		ListGridField OCCUR_TIME = new ListGridField("ADDTIME", Util.TI18N.OCCUR_TIME(), 140);
//		ListGridField OPERATE_PERSON = new ListGridField("ADDWHO",Util.TI18N.OPERATE_PERSON(), 80);
//		ListGridField OPERATE_RECODE = new ListGridField("NOTES",Util.TI18N.OPERATE_RECODE(), 170);
//
//		logTable.setFields(DOC_NO, OPERATE_RECODE, OPERATE_PERSON, OCCUR_TIME);
//		
//	}
	
	
	/**
	 * 待调作业单列表
	 * @author yuanlei
	 * @param table
	 */
	private void createUnshpmField(SGTable table) {
		
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_SHIPMENT_HEADER4D125D9FF0BB46BCBAC33D92C1076E0E");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_UNLOAD);
		createListField(table, listMap);
		ListGridField SUPLR_ID_NAME = table.getField("SUPLR_ID");
		if(SUPLR_ID_NAME != null) {
			SUPLR_ID_NAME.setCanEdit(true);
			SUPLR_ID_NAME.setTitle(SUPLR_ID_NAME.getTitle());
			Util.initOrgSupplier(SUPLR_ID_NAME, "");
			suplrLst.add(SUPLR_ID_NAME);
		}
		else {
			MSGUtil.sayError("列表中必须配置" + Util.TI18N.SUPLR_NAME());
		}
		ListGridField EXEC_ORG_ID = table.getField("EXEC_ORG_ID");
		if(EXEC_ORG_ID != null) {
			EXEC_ORG_ID.setHidden(true);
		}
		ListGridField PLATE_NO = table.getField("PLATE_NO");
		if(PLATE_NO != null) {
			PLATE_NO.setCanEdit(true);
			PLATE_NO.setTitle(PLATE_NO.getTitle());
			PLATE_NO.addEditorExitHandler(new EditorExitHandler() {

				@Override
				public void onEditorExit(EditorExitEvent event) {
					final int row = event.getRowNum();
					String plate_no = ObjUtil.ifObjNull(event.getNewValue(), "").toString();
					if(ObjUtil.isNotNull(plate_no)) {
						Util.db_async.getSingleRecord("PLATE_NO,VEHICLE_TYP_ID,DRIVER1_NAME,MOBILE1", "DRIVER1_NAME"
								, " where PLATE_NO = '" + plate_no + "'", null, new AsyncCallback<HashMap<String, String>>() {

									@Override
									public void onFailure(Throwable caught) {					
									}

									@Override
									public void onSuccess(HashMap<String, String> result) {
										if(result != null && result.size() > 0) {
											unshpmTable.setEditValue(row, "PLATE_NO", ObjUtil.ifObjNull(result.get("PLATE_NO"), "").toString());
											unshpmTable.setEditValue(row, "VEHICLE_TYP_ID", ObjUtil.ifObjNull(result.get("VEHICLE_TYP_ID"), "").toString());
											unshpmTable.setEditValue(row, "DRIVER", ObjUtil.ifObjNull(result.get("DRIVER1_NAME"), "").toString());
											unshpmTable.setEditValue(row, "MOBILE", ObjUtil.ifObjNull(result.get("MOBILE1"), "").toString());
										}
										else {
											unshpmTable.setEditValue(row, "VEHICLE_TYP_ID", "");
											unshpmTable.setEditValue(row, "DRIVER", "");
											unshpmTable.setEditValue(row, "MOBILE", "");
										}
									}
							
						});
					}
				}
				
			});
		}
		else {
			MSGUtil.sayError("列表中必须配置" + Util.TI18N.PLATE_NO());
		}
		ListGridField VEHICLE_TYP = table.getField("VEHICLE_TYP_ID");
		if(VEHICLE_TYP != null) {
			VEHICLE_TYP.setCanEdit(true);
			VEHICLE_TYP.setTitle(VEHICLE_TYP.getTitle());
			Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
			vehTypeLst.add(VEHICLE_TYP);
		}
		else {
			MSGUtil.sayError("列表中必须配置" + Util.TI18N.VEHICLE_TYPE());
		}
		ListGridField DRIVER = table.getField("DRIVER");
		if(DRIVER != null) {
			DRIVER.setCanEdit(true);
		}
		ListGridField MOBILE = table.getField("MOBILE");
		if(MOBILE != null) {
			MOBILE.setCanEdit(true);
		}
	}

	   private void initTemp(final SGTable table, final ListGridField TEMP_NAME, String equip_id) {
	    	final String code = equip_id;
			DataSource ds = TempDS.getInstance("BAS_TEMPEQ1");
			
			ListGridField EQU_ID = new ListGridField("ID", "代码", 80);
			EQU_ID.setHidden(true);
			ListGridField EQUIP_NO = new ListGridField("EQUIP_NO", "设备名称", 110);
			final ComboBoxItem temp_no = new ComboBoxItem();
			temp_no.setColSpan(2);
			temp_no.setWidth(90);
			temp_no.setOptionDataSource(ds);  
			temp_no.setDisabled(false);
			temp_no.setShowDisabled(false);
			temp_no.setDisplayField("EQUIP_NO");
			temp_no.setValueField("ID");
			temp_no.setPickListWidth(110);
			temp_no.setPickListHeight(300);
//			temp_no.setPickListBaseStyle("myBoxedGridCell");
			temp_no.setPickListFields(EQU_ID,EQUIP_NO);
			
			Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			temp_no.setPickListCriteria(criteria);
			
			TEMP_NAME.setEditorType(temp_no);
			TEMP_NAME.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					Object obj = event.getValue();
					table.setEditValue(event.getRowNum(), code, ObjUtil.ifObjNull(obj,"").toString());
				}
				
			});
			/*.addEditorExitHandler(new EditorExitHandler() {

				@Override
				public void onEditorExit(EditorExitEvent event) {
					Object obj = event.getNewValue();
					final int rowNum = event.getRowNum();
					if(obj != null) {
						final Record rec = event.getRecord();
						System.out.println("11111");
						System.out.println(obj.toString());
						System.out.println(rec.getAttribute("ID"));
						System.out.println(rec.getAttribute("EQUIP_NO"));
						//if(obj.toString().equals("")) {
							//if(rec != null) {
								table.setEditValue(rowNum, code, ObjUtil.ifObjNull(obj,"").toString());
								//table.setEditValue(rowNum, name, rec.getAttribute("EQUIP_NO"));
							//}else {
							//	table.setEditValue(rowNum, code, "");
								//table.setEditValue(rowNum, name, "");
							//}
						//}
					}
//							else {
//							HashMap<String,String> map = new HashMap<String,String>();
//							Util.db_async.getRecord("ID,EQUIP_NO", "BAS_TEMPEQ", "", map, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//								
//								@Override
//								public void onSuccess(ArrayList<HashMap<String, String>> result) {
//									if(result==null) return;
//									table.setEditValue(rowNum, code, result.get(0).get("ID"));
//									table.setEditValue(rowNum, name, result.get(0).get("EQUIP_NO"));
//									if(rec != null){
//										rec.setAttribute(code, result.get(0).get("ID"));
//										rec.setAttribute(name, result.get(0).get("EQUIP_NO"));
//									}
//								}
//								
//								@Override
//								public void onFailure(Throwable caught) {
//									
//								}
//							});   
//						}
//					}
				}
				
			});*/
	    }
	    private void initGps(final SGTable table, final ListGridField GPS_NAME, String equip_id) {
	    	final String code = equip_id;
			DataSource ds = GpsEqDS.getInstance("BAS_GPSEQ1");
			
			ListGridField EQU_ID = new ListGridField("ID", "代码", 80);
			EQU_ID.setHidden(true);
			ListGridField EQUIP_NO = new ListGridField("EQUIP_NO", "设备名称", 110);
			final ComboBoxItem temp_no = new ComboBoxItem();
			temp_no.setColSpan(2);
			temp_no.setWidth(90);
			temp_no.setOptionDataSource(ds);  
			temp_no.setDisabled(false);
			temp_no.setShowDisabled(false);
			temp_no.setDisplayField("EQUIP_NO");
			temp_no.setValueField("ID");
			temp_no.setPickListWidth(110);
			temp_no.setPickListHeight(300);
//			temp_no.setPickListBaseStyle("myBoxedGridCell");
			temp_no.setPickListFields(EQU_ID,EQUIP_NO);
			
			Criteria criteria = new Criteria();
			criteria.addCriteria("OP_FLAG","M");
			temp_no.setPickListCriteria(criteria);
			
			GPS_NAME.setEditorType(temp_no);
			GPS_NAME.addChangedHandler(new ChangedHandler() {

				@Override
				public void onChanged(ChangedEvent event) {
					Object obj = event.getValue();
					table.setEditValue(event.getRowNum(), code, ObjUtil.ifObjNull(obj,"").toString());
				}
				
			});
	    }
	
	
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
	

		
	    //调度单按钮
	    IButton loadButton = createUDFBtn(Util.BI18N.SEARCHLOAD(), StaticRef.ICON_SEARCH, TrsPrivRef.Vehicle_P1_01);
	    loadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(shipwin!=null){
					shipwin.hide();
				}
			   if(searchWin==null){
				   searchForm=new SGPanel();
				   searchForm.setDataSource(loadDS);
			       searchWin = new SearchWin(loadDS, subView.createSerchForm(searchForm),
				   downSectionStack.getSection(0),downSectionStack,hLayout).getViewPanel();
			       searchWin.setWidth(620);
			       searchWin.setHeight(330);
			   }
			   else{
					searchWin.show();
			   }			
			}
	    });
        
	    //生成调度单按钮
	    shpmButton = createUDFBtn(Util.BI18N.CREATELOAD(), StaticRef.ICON_SAVE, TrsPrivRef.Vehicle_P1_02);
	    shpmButton.addClickHandler(new MakeLoadNoNewAction(unshpmTable, loadTable,carTable,getView()));
        
        //保存按钮
        saveButton = createBtn(StaticRef.SAVE_BTN, TrsPrivRef.Vehicle_P1_03);
        saveButton.addClickHandler(new SaveLoadNoNewAction(loadTable,check_map, getView()));
        
        //删除按钮
        delButton = createBtn(StaticRef.DELETE_BTN, TrsPrivRef.Vehicle_P1_04);
        delButton.addClickHandler(new DeleteLoadNoNewAction(loadTable, this));
        
        //取消按钮
        canButton = createBtn(StaticRef.CANCEL_BTN, TrsPrivRef.Vehicle_P1_05);
        canButton.addClickHandler(new CancelAction(loadTable));
        
	    //确认发运按钮
	    confirmButton = createUDFBtn(Util.BI18N.SENDCONFIRM(), StaticRef.ICON_CONFIRM, TrsPrivRef.Vehicle_P1_06);
	    confirmButton.addClickHandler(new LoadSendConfirmNewAction(getView(), loadTable, shpmTable));
	    
	    //取消发运按钮
	    cansendButton = createUDFBtn(Util.BI18N.CANCELSEND(), StaticRef.ICON_CANCEL, TrsPrivRef.Vehicle_P1_07);
	    cansendButton.addClickHandler(new LoadCancelSendNewAction(getView(), loadTable, shpmTable));
	    

        del_map.put(TrsPrivRef.Vehicle_P1_04, delButton);
        save_map.put(TrsPrivRef.Vehicle_P1_03, saveButton);
        save_map.put(TrsPrivRef.Vehicle_P1_05, canButton);
        sendMap.put(TrsPrivRef.Vehicle_P1_06, confirmButton);
        sendMap.put(TrsPrivRef.Vehicle_P1_07, cansendButton);
        enableOrDisables(del_map, true);
        enableOrDisables(save_map, false);
        setConfirmBtnStatus(false);
        setButtonEnabled(TrsPrivRef.Vehicle_P1_02,shpmButton,true);
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(loadButton, shpmButton, saveButton, delButton, canButton, confirmButton, cansendButton);
	  
	}

	public void createForm(DynamicForm form) {
		
	}

	public void initVerify() {
		check_map.put("TABLE", "TRANS_LOAD_HEADER");		
		check_map.put("START_AREA_ID", StaticRef.CHK_UNIQUE + Util.TI18N.START_ARAE());		
		check_map.put("END_AREA_ID", StaticRef.CHK_NOTNULL + Util.TI18N.END_AREA());
		check_map.put("SUPLR_NAME", StaticRef.CHK_UNIQUE + Util.TI18N.SUPLR_NAME());		
		check_map.put("PLATE_NO", StaticRef.CHK_NOTNULL + Util.TI18N.PLATE_NO());
		//check_map.put("DEPART_TIME", StaticRef.CHK_DATE+ Util.TI18N.END_LOAD_TIME());
		//check_map.put("DONE_TIME", StaticRef.CHK_DATE+ "预计回场时间");
		check_map.put("DRIVER1", StaticRef.CHK_NOTNULL + "司机");
		check_map.put("MOBILE1", StaticRef.CHK_NOTNULL + "手机号");
		//check_map.put("TEMP_NO1", StaticRef.CHK_NOTNULL + "温控设备1");
		//check_map.put("TEMP_NO2", StaticRef.CHK_NOTNULL + "温控设备2");
		
	}

	public void onDestroy() {
	  if (searchWin != null) {
		  searchWin.destroy();
	  }if(transTrackWin !=null){
		  transTrackWin.destroy();
	  }if(dispLoadWin !=null){
		  dispLoadWin.destroy();
	  }if(unDispLoadWin != null){
		  unDispLoadWin.destroy();
	  }
	}
	
	private void addRightKey() {
		final Menu menu = new Menu();   //待调右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
	    if(isPrivilege(TrsPrivRef.Vehicle_P2)){                          //待调右键权限    wangjun 2010-3-9
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_01)) {
			    MenuItem addItem = new MenuItem("加入调度单",StaticRef.ICON_NEW);
			    addItem.setKeyTitle("Alt+A");
			    KeyIdentifier addKey = new KeyIdentifier();
			    addKey.setAltKey(true);
			    addKey.setKeyName("A");
			    addItem.setKeys(addKey);
			    menu.addItem(addItem);
			    addItem.addClickHandler(new AddShpmNoNewAction(this,unshpmTable, loadTable));
		    }
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_02)) {
			    MenuItem refreshItem = new MenuItem(Util.BI18N.REFRESH(),StaticRef.ICON_REFRESH);
			    refreshItem.setKeyTitle("Alt+F");
			    KeyIdentifier refreshKey = new KeyIdentifier();
			    refreshKey.setAltKey(true);
			    refreshKey.setKeyName("F");
			    refreshItem.setKeys(refreshKey);
			    menu.addItem(refreshItem);
			    qryUnshpmTableAction = new QueryUnshpmTableAction(unshpmTable, sumForm, pageForm);
			    refreshItem.addClickHandler(qryUnshpmTableAction);
			    
		    }
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_03)) {//保存
			    MenuItem saveItem = new MenuItem(Util.BI18N.SAVE(),StaticRef.ICON_SAVE);
			    menu.addItem(saveItem);
			    saveItem.addClickHandler(new UnShpmSaveAction(unshpmTable));
			    
		    }
		   

		    if(isPrivilege(TrsPrivRef.Vehicle_P2_04)) {
		    	 MenuItem UnShpmPrintItem = new MenuItem("打印提货单",StaticRef.ICON_PRINT);
		    	 UnShpmPrintItem.addClickHandler(new UnShpmPrintNewAction(this,unshpmTable));
		    	 menu.addItem(UnShpmPrintItem);
		    	 Menu m = new Menu();
		    	 MenuItem b2bPrintItem = new MenuItem("打印冷运专运调度单",StaticRef.ICON_PRINT);
		 		 b2bPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2bDispatch"));
		 		 MenuItem gxPrintItem = new MenuItem("打印干线调度单",StaticRef.ICON_PRINT);
		 		 gxPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "arteryDispatch"));
		 		 MenuItem psPrintItem = new MenuItem("打印零担派送调度单",StaticRef.ICON_PRINT);
		 		 psPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "sendDispatch"));
//		 		 MenuItem tfqdPrintItem = new MenuItem("打印提货清单",StaticRef.ICON_PRINT);
//		 		 tfqdPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "deliveryManifest"));
		 		 MenuItem tfPrintItem = new MenuItem("打印零担提货调度单",StaticRef.ICON_PRINT);
		 		 tfPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "deliveryDispatch"));
//		 		 MenuItem b2cPrintItem = new MenuItem("打印B2C派送调度出库单",StaticRef.ICON_PRINT);
//		 		 b2cPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2cDispatch"));
//		 		 MenuItem b2cRoutePrintItem = new MenuItem("打印B2C直送调度出库单",StaticRef.ICON_PRINT);
//		 		 b2cRoutePrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2cRouteDispatch"));
		 		 
		 		 MenuItem b2cPrintItem = new MenuItem("打印冷运速配调度出库单",StaticRef.ICON_PRINT);
		 		 b2cPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2cDispatch"));
		 		 
//		 		MenuItem newDispatch=new MenuItem("打印不干胶模板",StaticRef.ICON_PRINT);
//				newDispatch.addClickHandler(new DispatchPrintAction(loadTable,"newDispatch"));
//				m.addItem(newDispatch);
				
		    	 m.addItem(b2bPrintItem);
		    	 m.addItem(gxPrintItem);
		    	 m.addItem(psPrintItem);
//		    	 m.addItem(tfqdPrintItem);
		    	 m.addItem(tfPrintItem);
		    	 m.addItem(b2cPrintItem);
//				 m.addItem(newDispatch);
//		    	 m.addItem(b2cRoutePrintItem);
		    	 UnShpmPrintItem.setSubmenu(m);
		    }
		    
		    /*if(true) {
		    	menu.addItem(itemSeparator); 
		    	MenuItem groupItem = new MenuItem("作业单编组",StaticRef.ICON_CONFIRM);
		    	groupItem.addClickHandler(new ShpmGroupAction(getView(),unshpmTable));
		    	menu.addItem(groupItem);
		    	
		    	menu.addItem(itemSeparator); 
		    	MenuItem vehItem = new MenuItem("按运力拆分",StaticRef.ICON_CONFIRM);
		    	vehItem.addClickHandler(new SplitByCapacityAction(getView(),unshpmTable));
		    	menu.addItem(vehItem);
		    }*/
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_05)) {
		    	menu.addItem(itemSeparator); 
			    MenuItem splitItem = new MenuItem("货量拆分",StaticRef.ICON_CONFIRM);
			    splitItem.setKeyTitle("Alt+S");
			    KeyIdentifier splitKey = new KeyIdentifier();
			    splitKey.setAltKey(true);
			    splitKey.setKeyName("S");
			    splitItem.setKeys(splitKey);
			    menu.addItem(splitItem);
			    splitItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
	
					@Override
					public void onClick(MenuItemClickEvent event) {
						
						if(unshpmlstTable != null && unshpmlstTable.getSelection().length > 0){
							if(isSplitValid()) {
								new SplitActionNewWin(unshpmlstTable, unshpmTable, getView()).getViewPanel().show();
							}
							
						} else {
							SC.warn("未勾选作业单明细!");
						}			
					}
			    });
		    }
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_06)) {
			    MenuItem cansplitItem = new MenuItem(Util.BI18N.CANCELSPLIT(),StaticRef.ICON_CANCEL); 
			    cansplitItem.setKeyTitle("Alt+C");
			    KeyIdentifier cansplitKey = new KeyIdentifier();
			    cansplitKey.setAltKey(true);
			    cansplitKey.setKeyName("C");
			    cansplitItem.setKeys(cansplitKey);
			    menu.addItem(cansplitItem);
			    cansplitItem.addClickHandler(new CancelSplitAction(getView(), unshpmTable));
			    
		    }
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_07)) {
		    	menu.addItem(itemSeparator); 
			    MenuItem splitJrnyItem = new MenuItem("行程拆分",StaticRef.ICON_CONFIRM);
			    splitJrnyItem.setKeyTitle("Alt+S");
			    menu.addItem(splitJrnyItem);
			    splitJrnyItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
	
					@Override
					public void onClick(MenuItemClickEvent event) {
						if(unshpmTable.getSelectedRecord().getAttribute("PRINT_FLAG").equals("N")){
							
							SC.warn("作业单"+unshpmTable.getSelectedRecord().getAttribute("SHPM_NO")+" 已打印提货单不允许拆分！");
							return;
						}
						
						if(unshpmTable != null && unshpmTable.getSelection().length > 0){
							//弹出窗口
							new JourneySplitWin(getView(), unshpmTable.getSelection());
							
						} else {
							SC.warn("请选择作业单!");
						}			
					}
			    });
		    }
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_08)) {
			    MenuItem canJnrysplitItem = new MenuItem("取消行程拆分",StaticRef.ICON_CANCEL); 
			    menu.addItem(canJnrysplitItem);
			    canJnrysplitItem.addClickHandler(new CancelJourneySplitAction(getView(), unshpmTable));
			    
		    }
		    
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_09)) {
		    	menu.addItem(itemSeparator);
				MenuItem expList = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);  //导出
				menu.addItem(expList);
			    expList.addClickHandler(new ExportAction(unshpmTable));
		    }   
		    if(isPrivilege(TrsPrivRef.Vehicle_P2_10)) {
				MenuItem expList = new MenuItem("明细导出",StaticRef.ICON_EXPORT);  //导出
				menu.addItem(expList);
//			    expList.addClickHandler(new ExportShpmItemAction(unshpmTable, " order by shpm_no,shpm_row asc "));
				expList.addClickHandler(new ExportShpmItemAction(unshpmTable, " order by UNLOAD_AREA_ID ,nvl(edittime,addtime) desc "));
		    }  
		    
		    unshpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
	            public void onShowContextMenu(ShowContextMenuEvent event) {
	            	menu.showContextMenu();
	                event.cancel();
	            }
	        });
	    }
	    
	}
	
	private VehicleDispatchView getView() {
		return this;
	}
	
	//获取系统参数值
	private void getSysParam() {
		LinkedHashMap<String, SysParam> sysParam = LoginCache.getSysParam();
		if(sysParam != null) {
			SysParam param = sysParam.get("LOAD_QNTYCANEDIT");
			if(param != null) {
				canModify = param.getVALUE_STRING();
			}
			else {
				canModify = "N";
			}
			param = sysParam.get("VEHICLE_INPUT");
			if(param != null) {
				canVehInput = param.getVALUE_STRING();
			}
			else {
				canVehInput = "Y";
			}
		}
		else {
			MSGUtil.sayWarning("系统参数缓存未加载完毕，请重新打开界面!");
			return;
		}
	}
	
	public void setSendBtnStatus(boolean bolSet) {
		setButtonEnabled(TrsPrivRef.Vehicle_P1_06, confirmButton, bolSet);
		setButtonEnabled(TrsPrivRef.Vehicle_P1_07, cansendButton, !bolSet);
	}
	
	public void setConfirmBtnStatus(boolean bolSet) {
		setButtonEnabled(TrsPrivRef.Vehicle_P1_06, confirmButton, bolSet);
		setButtonEnabled(TrsPrivRef.Vehicle_P1_07, cansendButton, bolSet);
	}
	
	public void setSaveBtnStatus(boolean bolSet) {
        enableOrDisables(save_map, bolSet);
	}
	
	public void setDelBtnStatus(boolean bolSet) {
		enableOrDisables(del_map, bolSet);
	}
	
	/**
	 * 判断拆分是否合法
	 * @author yuanlei
	 * @return
	 */
	protected boolean isSplitValid(){
		boolean isAllMaxSplit = true;
		
		if(unshpmlstTable != null) {
			ListGridRecord[] records = unshpmlstTable.getSelection();
			int[] edit_rows = new int[records.length];
			for(int i = 0; i < records.length; i++) {
				edit_rows[i] = unshpmlstTable.getRecordIndex(records[i]);
			}
			Record rec = null;
			
			for(int i = 0; i < edit_rows.length; i++) {
				
				ListGridRecord initRecord = unshpmlstRec[edit_rows[i]];
				rec = unshpmlstTable.getEditedRecord(edit_rows[i]);
				double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("QNTY"),"0").toString());
				double init_qnty = Double.parseDouble(ObjUtil.ifObjNull(initRecord.getAttribute("QNTY"),"0").toString());
				double rate = getRate(cur_qnty, init_qnty);
				if(rate > 1) {
					SC.warn("行号[" + rec.getAttribute("SHPM_ROW") + "]数量不能大于原单量!");
					return false;
				}
				else if(rate < 1) {
					isAllMaxSplit = false;
				}
			}
			if(records.length == unshpmlstTable.getRecords().length && isAllMaxSplit) {
				SC.warn("无效的拆分操作!");
				return false;
			}
			return true;
		}
		else {
			SC.warn("无效的拆分操作!");
			return false;
		}
	}
	
    private double getRate(double douPart, double douTotal) {
    	  
	    double rate = 0.0000;
	    if(douTotal > 0) {
	    	rate = douPart/douTotal;
	    }
	    return rate;
    }
    

    
    private void initArea(final SGTable table, final ListGridField AREA_NAME, String area_code, String area_cname, String defaultValue) {
		final String code = area_code;
		final String name = area_cname;
		DataSource ds = VCAreaDS.getInstance("VC_BAS_AREA");
		
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHOW_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		final ComboBoxItem area_name = new ComboBoxItem();
		area_name.setColSpan(2);
		area_name.setWidth(120);
		area_name.setOptionDataSource(ds);  
		area_name.setDisabled(false);
		area_name.setShowDisabled(false);
		area_name.setDisplayField("CONTENT");
		area_name.setValueField("SHORT_NAME");
		area_name.setPickListWidth(240);
		area_name.setPickListBaseStyle("myBoxedGridCell");
		area_name.setPickListFields(AREA_CODE, SHOW_NAME, HINT_CODE);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		area_name.setPickListCriteria(criteria);
		
		AREA_NAME.setEditorType(area_name);
		AREA_NAME.addEditorExitHandler(new EditorExitHandler() {

			@Override
			public void onEditorExit(EditorExitEvent event) {
				Object obj = event.getNewValue();
				final int rowNum = event.getRowNum();
				if(obj != null) {
					final Record rec = event.getRecord();
					if(obj.toString().equals("")) {
						SC.say("未找到匹配的行政区域!");
						if(rec != null) {
							table.setEditValue(rowNum, code, rec.getAttribute(code));
							table.setEditValue(rowNum, name, rec.getAttribute(name));
						}
						else {
							table.setEditValue(rowNum, code, "");
							table.setEditValue(rowNum, name, "");
						}
					}
					else {
						String curValue = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
						Util.async.queryData("select AREA_CODE,SHORT_NAME from VC_AREA where AREA_CODE='"+
								curValue+"' or upper(AREA_CNAME)=upper('"+curValue+
								"') or upper(SHORT_NAME)=upper('"+curValue+
								"')", false, new AsyncCallback<Map<String,Object>>() {
							@Override
							public void onFailure(Throwable caught) {
								
							}
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Map<String, Object> result) {
								List<List<String>> list = (List<List<String>>)result.get("data");
								if(list == null || list.isEmpty()){
									SC.say("未能找到指定的区域!");
									return;
								}
								table.setEditValue(rowNum, code, list.get(0).get(0));
								table.setEditValue(rowNum, name, list.get(0).get(1));
								if(rec != null){
									rec.setAttribute(code, list.get(0).get(0));
									rec.setAttribute(name, list.get(0).get(1));
								}
							}
						});
					}
				}
			}
			
		});
	}
    private void createVeField(SGTable table) {
		ListGridField PLATE_NO1 = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);
		ListGridField VEHICLE_STAT1 = new ListGridField("VEHICLE_STAT_NAME",Util.TI18N.VEHICLE_STAT(),60);
		//ListGridField CURRENT_AREA = new ListGridField("CURRENT_AREA","当前区域",160);
		//ListGridField TMP_ATTR1 = new ListGridField("TMP_ATTR_NAME",Util.TI18N.TMP_ATTR(),160);
		//ListGridField AVAIL_FLAG = new ListGridField("AVAIL_FLAG_NAME",Util.TI18N.AVAIL_ATTR(),140);
		ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME",Util.TI18N.VEHICLE_TYP(),100);
		ListGridField MAX_WEIGHT1 = new ListGridField("MAX_WEIGHT",Util.TI18N.MAX_WEIGHT(),70);
		//ListGridField REMAIN_GROSS_W1 = new ListGridField("REMAIN_GROSS_W","余量",140);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID_NAME",Util.TI18N.SUPLR_NAME(),80);
		ListGridField VEHICLE_ATTR = new ListGridField("VEHICLE_ATTR_NAME","车辆属性", 70);
		ListGridField DRIVER1 = new ListGridField("DRIVER1","司机", 100);
		DRIVER1.setHidden(true);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","电话", 100);
		MOBILE1.setHidden(true);
		table.setFields(DRIVER1,MOBILE1,SUPLR_NAME,PLATE_NO1,VEHICLE_TYP_ID_NAME,VEHICLE_ATTR,MAX_WEIGHT1,VEHICLE_STAT1);
		//table.setGroupStartOpen(GroupStartOpen.ALL);  
		table.setGroupByField("SUPLR_ID_NAME");  
    
    }   
    
  
    
   
//    private void doFilter1(SGTable tab,DynamicForm form){
//		Criteria criteria = form.getValuesAsCriteria();
//		if(criteria == null) {
//			criteria = new Criteria();
//		}
//		criteria.addCriteria("OP_FLAG","M");
//		criteria.addCriteria("ENABLE_FLAG","Y");
//		tab.invalidateCache();
//		tab.fetchData(criteria);  
//   }
	private void doSearch1(SectionStackSection section,SGPanel form) {
	
//		FilterBuilder filterBuilder1 = new FilterBuilder();
//		filterBuilder1.setDataSource(unshpmDS);	
 
		unshpmTable.invalidateCache();
		final Criteria criteria= new Criteria();
		criteria.addCriteria("OP_FLAG","M");
//		if(filterBuilder1.isVisible()) {
//			criteria.addCriteria(filterBuilder1.getCriteria()); 
//		}
//		else {
		//	criteria.addCriteria(form.getValuesAsCriteria());
		//}
		criteria.addCriteria("EMPTY_FLAG","Y");
		criteria.addCriteria("STATUS","20");
//		criteria.addCriteria("UDF6","Y");
		criteria.addCriteria(form.getValuesAsCriteria());
		System.out.println(criteria.getAttribute("OP_FLAG"));
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
		unshpmTable.fetchData(criteria, new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					//LoginCache.setPageResult(unshpmTable, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					String key = Cookies.getCookie("SQLFIELD1");
					String value = Cookies.getCookie("SQLFIELD2");
					String alias = Cookies.getCookie("SQLALIAS");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						unshpmTable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						unshpmTable.setProperty("SQLFIELD1", key);
						unshpmTable.setProperty("SQLFIELD2", value);
						unshpmTable.setProperty("SQLALIAS", alias);
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
				if(map.get("criteria") != null) {
					map.remove("criteria");
				}
				if(map.get("_constructor") != null) {
					map.remove("_constructor");
				}
				if(map.get("C_ORG_FLAG") != null) {
					Object obj = map.get("C_ORG_FLAG");
					Boolean c_org_flag = (Boolean)obj;
					map.put("C_ORG_FLAG",c_org_flag.toString());
				}			
			
			}
			
		});
		
	}
	
	public void doAllSearch(SectionStackSection section,SGPanel form){
		unshpmTable.invalidateCache();
		final Criteria criteria= new Criteria();
		criteria.addCriteria("OP_FLAG","M");
//		if(filterBuilder1.isVisible()) {
//			criteria.addCriteria(filterBuilder1.getCriteria()); 
//		}
//		else {
		//	criteria.addCriteria(form.getValuesAsCriteria());
		//}
		criteria.addCriteria("EMPTY_FLAG","Y");
		criteria.addCriteria("STATUS","20");
//		criteria.addCriteria("UDF6","Y");
		criteria.addCriteria(form.getValuesAsCriteria());
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
		unshpmTable.fetchData(criteria, new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					//LoginCache.setPageResult(unshpmTable, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					String key = Cookies.getCookie("SQLFIELD1");
					String value = Cookies.getCookie("SQLFIELD2");
					String alias = Cookies.getCookie("SQLALIAS");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						unshpmTable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						unshpmTable.setProperty("SQLFIELD1", key);
						unshpmTable.setProperty("SQLFIELD2", value);
						unshpmTable.setProperty("SQLALIAS", alias);
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
				if(map.get("criteria") != null) {
					map.remove("criteria");
				}
				if(map.get("_constructor") != null) {
					map.remove("_constructor");
				}
				if(map.get("C_ORG_FLAG") != null) {
					Object obj = map.get("C_ORG_FLAG");
					Boolean c_org_flag = (Boolean)obj;
					map.put("C_ORG_FLAG",c_org_flag.toString());
				}			
				doCarSearch(topSectionStack2.getSection(0),carForm);
			}
			
		});
		
	}
	
	
	public void doCarSearch(SectionStackSection section,SGPanel form){

		carTable.discardAllEdits();
		carTable.invalidateCache();
		final Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
	    criteria.addCriteria(form.getValuesAsCriteria());
	    carTable.setFilterEditorCriteria(criteria);
	    carTable.fetchData(criteria);
	}
	
	private void initCombo() {
	    //initArea(loadTable,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
	    Util.initComboBatch(vehTypeLst, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC", null);  //车辆类型
	    Util.initOrgSupplier(suplrLst, "");
	}
	
	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		VehicleDispatchView view = new VehicleDispatchView();
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