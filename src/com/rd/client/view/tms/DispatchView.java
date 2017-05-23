package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.ExplorerTreeNode;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.AddShpmNoAction;
import com.rd.client.action.tms.dispatch.CancelJourneySplitAction;
import com.rd.client.action.tms.dispatch.CancelSplitAction;
import com.rd.client.action.tms.dispatch.ChangedQntyAction;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.action.tms.dispatch.DeleteLoadNoAction;
import com.rd.client.action.tms.dispatch.DispatchPrintAction;
import com.rd.client.action.tms.dispatch.ExportShpmItemAction;
import com.rd.client.action.tms.dispatch.LoadCancelAuditAction;
import com.rd.client.action.tms.dispatch.LoadCancelSendAction;
import com.rd.client.action.tms.dispatch.LoadDispatchAuditAction;
import com.rd.client.action.tms.dispatch.LoadSendConfirmAction;
import com.rd.client.action.tms.dispatch.MakeLoadNoAction;
import com.rd.client.action.tms.dispatch.QueryUnloadTableAction;
import com.rd.client.action.tms.dispatch.QueryUnshpmTableAction;
import com.rd.client.action.tms.dispatch.RemoveShpmNoAction;
import com.rd.client.action.tms.dispatch.SaveLoadNoAction;
import com.rd.client.action.tms.dispatch.ShpmCancelSendAction;
import com.rd.client.action.tms.dispatch.ShpmSaveAction;
import com.rd.client.action.tms.dispatch.ShpmSendConfirmAction;
import com.rd.client.action.tms.dispatch.SplitActionWin;
import com.rd.client.action.tms.dispatch.UnShpmPrintAction;
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
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.GpsEqDS;
import com.rd.client.ds.base.TempDS;
import com.rd.client.ds.base.VCAreaDS;
import com.rd.client.ds.tms.BillPayDetailDS;
import com.rd.client.ds.tms.BillSettlePayDS;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.ds.tms.LoadShpmDS;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.rd.client.ds.tms.TansActLogDS;
import com.rd.client.ds.tms.UnShpmDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.reflection.ClassTools;
import com.rd.client.view.settlement.SuplrFeeSettView;
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
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
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
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.TreeNode;
/**
 * 运输管理-->调度配载
 * @author yuanlei
 */
@ClassForNameAble
public class DispatchView extends SGForm implements PanelFactory {
    
	private DataSource loadDS;            //调度单数据源
	private DataSource shpmDS;            //已调作业单数据源
	private DataSource unshpmDS;          //待调作业单数据源
	private DataSource unshmplstDS;       //待调作业单明细数据源
	private DataSource billDS;
	private DataSource billItemDS;
	private DataSource logDS;             //业务日志数据源
	private SectionStack downSectionStack;  //左边按单调度SectionStack布局
	private SectionStack topSectionStack; //右边SectionStack布局
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
	private SGTable logTable;       //业务日志
	private SGTable feeTable;
	private ListGrid feeitemTable;
	public DynamicForm sumForm;     //汇总FORM
	public DynamicForm pageForm;    //待调分页FORM
	public DynamicForm loadPageForm;    //调度单分页FORM
	private DispatchSubForm subView; //调度配载子页面	
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
	private IButton searchButton;	
	private Record selRecord;   //调度单选中的记录
	private HashMap<String,IButton> sendMap;
	//private ListGridRecord selUnshpmRecord;  //待调作业单选中的记录
	//private ListGridField PLATE_NO;	
	private ArrayList<ListGridField> suplrLst;
	private ArrayList<ListGridField> vehTypeLst;
	private ArrayList<ListGridField> areaLst;	
	public String plate_no;
	private TabSet mainTabSet;
	/*public DispatchView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		suplrLst = new ArrayList<ListGridField>();
		vehTypeLst = new ArrayList<ListGridField>();
		areaLst = new ArrayList<ListGridField>();
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		sendMap = new HashMap<String, IButton>();
		getSysParam();  //获取系统参数
		
		subView = new DispatchSubForm();
		
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		unshpmDS = UnShpmDS.getInstance("V_SHIPMENT_HEADER_1", "TRANS_SHIPMENT_HEADER");
		//unshpmDS1 = ShpmDS6.getInstance("V_SHIPMENT_HEADER_6", "TRANS_SHIPMENT_HEADER");
		shpmDS = LoadShpmDS.getInstance("V_SHIPMENT_HEADER_LOAD", "TRANS_SHIPMENT_HEADER");
		unshmplstDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM_SF", "TRANS_SHIPMENT_ITEM");
		billDS = BillSettlePayDS.getInstance("V_BILL_LOAD_PAY","TRANS_BILL_PAY");
		billItemDS = BillPayDetailDS.getInstance("V_BILL_SHPM_PAY","TRANS_BILL_PAY");
		logDS = TansActLogDS.getInstance("V_CUSTOMACT_LOG_DISPATCH","TRANS_TRANSACTION_LOG");
		VLayout main =new VLayout();  //整体布局
		main.setWidth100();
		main.setHeight100();
		
		VStack stack =new VStack();  //主布局(包含待调作业单和调度单上下两个列表)
		stack.setHeight100();
		stack.setWidth100();
		
		//上半部分 --yuanlei
		//final TabSet topTabSet = new TabSet(); //主布局stack上半部分（包含待调作业单页签）
		final TabSet downTabSet = new TabSet();
		//topTabSet.setWidth100();
		//topTabSet.setHeight("50%");
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
		unshpmTable.setCanEdit(true); 
		unshpmTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		unshpmTable.setCanExpandRecords(true);
		//unshpmTable.setShowGroupSummary(true); 
		createUnshpmField(unshpmTable);
		
		
		
    	//topSectionStack.setShowExpandControls(true);
    	if(isPrivilege(TrsPrivRef.DISPATCH_P1)) {
    		
    		topSectionStack =new SectionStack();	  //unshpmTable分页的布局	
    		SectionStackSection section=new SectionStackSection(Util.TI18N.UNDISPATCHORDER());
    		section.setItems(unshpmTable);
    		section.setExpanded(true);
    		sumForm = subView.createSumLayout();   //创建汇总布局
    		pageForm = new SGPage(unshpmTable, true).initPageBtn();
    		section.setControls(subView.createTopBtn(topSectionStack, downTabSet), sumForm, pageForm);
    		
    		topSectionStack.addSection(section);
        	topSectionStack.setWidth100();
        	topSectionStack.setHeight("50%");
    	} 	
    	
    	//上半部分结束   --yuanlei
			
		//下半部分布局  --yuanlei
		downTabSet.setWidth100();
		downTabSet.setHeight("49%");	
		
		loadTable = new SGTable(loadDS, "100%", "100%", false, true, false) {
			
			//第二层表
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
				
		        //作业单列表
//		        ListGridField UNLOAD_SEQ = shpmTable.getField("UNLOAD_SEQ");
//		        if(UNLOAD_SEQ != null) {
//		        	UNLOAD_SEQ.setCanEdit(true);
//		        }
//		        else {
//		        	MSGUtil.sayError("列表中必须配置" + Util.TI18N.UNLOAD_SEQ() + "!");
//		        }
				/*ListGridField TOT_QNTY_EACH = shpmTable.getField("TOT_QNTY_EACH");
				if(TOT_QNTY_EACH !=null){
					TOT_QNTY_EACH.setCanEdit(true);
				}*/				
				
//        		ListGridField DEPART_TIME = shpmTable.getField("DEPART_TIME");
//        		if(DEPART_TIME != null) {
//	        		DEPART_TIME.setCanEdit(true);
//	        		DEPART_TIME.setTitle(Util.TI18N.END_LOAD_TIME());
//	        		Util.initDateTime(shpmTable,DEPART_TIME);
//        		}
//        		else {
//        			MSGUtil.sayError("列表中必须配置" + Util.TI18N.END_LOAD_TIME() + "!");
//        		}
        		
        		/*ListGridField PLATE_NO = shpmTable.getField("PLATE_NO");
 		        if(PLATE_NO != null) {
 		        	PLATE_NO.setCanEdit(true);
 		        }
 		        else {
 		        	MSGUtil.sayError("列表中必须配置" + Util.TI18N.PLATE_NO() + "!");
 		        }*/
        		
        		
        		//shpmTable.fetchData(findValues);
        		shpmTable.fetchData(findValues, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						Util.db_async.getPageInfo(new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
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
        		MenuItemSeparator itemSeparator =new MenuItemSeparator();
        		
        		if(isPrivilege(TrsPrivRef.DISPATCH_P2_28)) {                      //wangjun 2011-3-9
        			
	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_17)) {
		        	    MenuItem removeItem = new MenuItem("移除作业单",StaticRef.ICON_DEL);
		        	    load_menu.addItem(removeItem);
		        	    removeItem.addClickHandler(new RemoveShpmNoAction(shpmTable, unshpmTable, loadTable,pageForm));
	        	    }
	        	    /*if(isPrivilege(TrsPrivRef.DISPATCH_P2_32)) {
		        	    MenuItem removeItem = new MenuItem("踢单",StaticRef.ICON_DEL);
		        	    load_menu.addItem(removeItem);
		        	    //removeItem.addClickHandler(new RemoveUnloadAction(shpmTable, unshpmTable, loadTable));//原剔除未发货
		        	    removeItem.addClickHandler(new DelUnsendShpmAction(shpmTable, unshpmTable, loadTable));
	        	    }*/
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_32)) {
//		        	    MenuItem removeItem = new MenuItem("移除明细",StaticRef.ICON_DEL);
//		        	    load_menu.addItem(removeItem);
//		        	    removeItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//		        	    	
//							@Override
//							public void onClick(MenuItemClickEvent event) {
//								String shpm_no = shpmTable.getSelectedRecord().getAttribute("SHPM_NO");
//								//String custom_odr_no = shpmTable.getSelectedRecord().getAttribute("CUSTOM_ODR_NO");
//								String status = shpmTable.getSelectedRecord().getAttribute("STATUS_NAME"); 
//								if(!StaticRef.SHPM_DIPATCH_NAME.equals(status)){
//									SC.warn("作业单["+shpm_no+"]状态不允许拆分！");
//									return;
//								}
//								
//								if(shpmTable != null && shpmTable.getSelection().length > 0){
//									new SplitShpmItemWin(loadTable, unshpmTable, shpmTable, sumForm, pageForm).getViewPanel().show();
//									
//								} else {
//									SC.warn("未勾选作业单明细!");
//								}			
//							}
//					    });
//	        	    }
	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_18)) {
	        	    	load_menu.addItem(itemSeparator);
		        	    MenuItem sendconfirmItem = new MenuItem("发车确认",StaticRef.ICON_CONFIRM);
		        	    load_menu.addItem(sendconfirmItem);
		        	    sendconfirmItem.addClickHandler(new ShpmSendConfirmAction(shpmTable, loadTable));
	        	    }
	        	    
	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_19)) {
		        	    MenuItem cansendItem = new MenuItem("取消发车",StaticRef.ICON_CANCEL);
		        	    load_menu.addItem(cansendItem);
		        	    cansendItem.addClickHandler(new ShpmCancelSendAction(shpmTable, loadTable));
	        	    }
	        	    
	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_20)) {
	        	    	load_menu.addItem(itemSeparator);
		        	    MenuItem saveItem = new MenuItem(Util.BI18N.SAVE(),StaticRef.ICON_SAVE);
		        	    load_menu.addItem(saveItem);
		        	    saveItem.addClickHandler(new ShpmSaveAction(shpmTable));
	        	    }
	        	    /*if(isPrivilege(TrsPrivRef.DISPATCH_P2_21)) {
		        	    MenuItem saveItem = new MenuItem(Util.BI18N.SPLITBYLDQNTY(),StaticRef.ICON_CONFIRM);
		        	    saveItem.setKeyTitle("Alt+T");
		        	    KeyIdentifier saveKey = new KeyIdentifier();
		        	    saveKey.setAltKey(true);
		        	    saveKey.setKeyName("T");
		        	    saveItem.setKeys(saveKey);
		        	    load_menu.addItem(saveItem);
		        	    saveItem.addClickHandler(new SplitByLoadQntyAction(shpmTable,loadTable, getView()));
	        	    }*/ 
	        	    if(true) {
	        	    	load_menu.addItem(itemSeparator);
		        	    MenuItem expItem = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);
		        	    load_menu.addItem(expItem);
		        	    expItem.addClickHandler(new ExportAction(shpmTable));
	        	    }
	        	    shpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
	                    public void onShowContextMenu(ShowContextMenuEvent event) {
	                    	load_menu.showContextMenu();
	                        event.cancel();
	                    }
	                });
        		}
        	    
        		layout.addMember(shpmTable);
        		layout.setLayoutTopMargin(0);
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
		listItem.setControls(subView.createDownBtn(topSectionStack, downTabSet), loadPageForm);
		downSectionStack.addSection(listItem);
		
		logTable = new SGTable(logDS,"100%", "100%");
		createTransLogField(logTable);
		logTable.setShowRowNumbers(true);
		logTable.setShowFilterEditor(false);
		logTable.setCanEdit(false);
		
		/*loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				System.out.println("SelectChanged");
				if (event.getRecord() == null) {
					return;
				}
				loadTable.OP_FLAG = "M";
				itemRow = loadTable.getRecordIndex(event.getRecord());
			
				initSaveBtn();			
			}
		});*/
		downTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				ListGridRecord record = loadTable.getSelectedRecord();
				if(record != null && ObjUtil.isNotNull(record.getAttribute("LOAD_NO"))) {
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG", "M");
					criteria.addCriteria("DOC_TYP", "SHPM_NO");
					if(event.getTabNum() == 2){
						criteria.addCriteria("DOC_NO", record.getAttribute("LOAD_NO"));
						logTable.fetchData(criteria);
					}
					else if(event.getTabNum() == 1) {
						criteria.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
						feeTable.fetchData(criteria);
					}
				}
			}
		});
	
		if(isPrivilege(TrsPrivRef.DISPATCH_P2)) {
			Tab tab1 = new Tab(Util.TI18N.LOADINFO());//页签名称1：调度信息
			tab1.setPane(downSectionStack);
			downTabSet.addTab(tab1);
		}
		if(isPrivilege(TrsPrivRef.DISPATCH_P3)) {
			createFeeInfo();
			Tab tab2 = new Tab(Util.TI18N.EXP_DETAIL());//页签名称2：费用明细
			tab2.setPane(feeTable);
			downTabSet.addTab(tab2);
		}
		if(isPrivilege(TrsPrivRef.DISPATCH_P4)) {
			Tab tab3 = new Tab(Util.TI18N.BUS_DIARY());//页签名称2：业务日志
			downTabSet.addTab(tab3);
			tab3.setPane(logTable);
		
		}
    	
    	//ToolStrip toolStrip = new ToolStrip();
		//toolStrip.setAlign(Alignment.RIGHT);
		//createBtnWidget(toolStrip);
		//lay.addMember(toolStrip);
        //lay.addMember(topSectionStack);
		
		//下半部分布局结束 --yuanlei
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		stack.addMember(topSectionStack);
		stack.addMember(downTabSet);
		main.addMember(toolStrip);
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
					setButtonEnabled(TrsPrivRef.DISPATCH_P2_02, shpmButton, true);
				}
			}
			
		});
		/*unshpmTable.addRecordClickHandler(new RecordClickHandler() {

			@Override
			public void onRecordClick(RecordClickEvent event) {
				//selUnshpmRecord = unshpmTable.getSelectedRecord();
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
					
				}
			}
			
		});*/
		unshpmTable.addEditorExitHandler(new EditorExitHandler() {
			@Override
			public void onEditorExit(EditorExitEvent event) {
				unshpmTable.selectRecords(selectRows);
			}
			
		});
		initVerify();
		addRightKey();   //待调作业单右键
		
		initCombo();
		
		//new LoadPrintWin("./user/wpsadmin/JMLoadmentPrint.pdf","", loadTable,shpmTable, true);
		//com.google.gwt.user.client.Window.open("../user/wpsadmin/JMLoadmentPrint.pdf", "", "");
		return main;
	}


	private void createLoadField(final SGTable groupTable) {
		
		boolean isDigitCanEdit = false;
		if(ObjUtil.ifNull(canModify,"N").equals("Y")) {
			isDigitCanEdit = true;
		}
		ListGridField ADDWHO = new ListGridField("ADDWHO","创建人",50);//调度单编号
		ADDWHO.setCanEdit(false);
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
		//ListGridField DEPART_TIME = new ListGridField("DEPART_TIME",Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		//Util.initDateTime(loadTable,DEPART_TIME);
		
		ListGridField GPS_NO1 = new ListGridField("GPS_NO1","GPS设备号", 60);  
		GPS_NO1.setHidden(true);		
		ListGridField GPS_NO1_NAME = new ListGridField("GPS_NO1_NAME","GPS设备号",70);
		initGps(loadTable,GPS_NO1_NAME,"GPS_NO1");
		GPS_NO1_NAME.setCanEdit(true);
//		ListGridField DONE_TIME = new ListGridField("DONE_TIME", "预计回场时间", 110);  //发运时间
//		//Util.initListGridDateTime(DEPART_TIME);
//		Util.initDateTime(loadTable,DONE_TIME);
		
		ListGridField REMAIN_GROSS_W = new ListGridField("REMAIN_GROSS_W","余量",45);//余量
		REMAIN_GROSS_W.setCanEdit(false);
		//ListGridField[] comboWidget=new ListGridField[2];
		
		ListGridField TEMP_NO1 = new ListGridField("TEMP_NO1","温控设备1",65);
		TEMP_NO1.setHidden(true);
		ListGridField TEMP_NO1_NAME = new ListGridField("TEMP_NO1_NAME","温控设备1",70);
		//Util.initComboValue(TEMP_NO1, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "");
		initTemp(loadTable,TEMP_NO1_NAME,"TEMP_NO1");
		TEMP_NO1_NAME.setCanEdit(true);
		
		/*FormItemIcon icon = new FormItemIcon();
		TEMP_NO1.setIcons(icon);
		TEMP_NO1.setShowSelectedIcon(true);
		icon.addFormItemClickHandler(new FormItemClickHandler() {
				
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				Util.db_async.getSingleRecord("TEMPERATURE1,TEMPERATURE2,TOT_GROSS_W", "TRANS_LOAD_HEADER", 
						" where LOAD_NO = '" + selRecord.getAttribute("LOAD_NO") + "'", null, new AsyncCallback<HashMap<String, String>>() {
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub		
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
									new VehicleWin(groupTable,itemRow,"20%", "32%", map).getViewPanel();
								}
								else {
									new VehicleWin(groupTable,itemRow,"20%", "32%").getViewPanel();
								}
							}						
				});					
			}
		});
		
		TEMP_NO1.addEditorExitHandler(new EditorExitHandler() {

			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				String plate_no = ObjUtil.ifObjNull(event.getNewValue(), "").toString();
				if(ObjUtil.isNotNull(plate_no)) {
					Util.db_async.getSingleRecord("PLATE_NO,VEHICLE_TYP_ID,DRIVER1_NAME,MOBILE1", "V_BAS_VEHICLE"
							, " where ENABLE_FLAG = 'Y' and PLATE_NO = '" + plate_no + "'", null, new AsyncCallback<HashMap<String, String>>() {

								@Override
								public void onFailure(Throwable caught) {					
								}

								@Override
								public void onSuccess(HashMap<String, String> result) {
									if(result != null && result.size() > 0) {
										loadTable.setEditValue(row, "PLATE_NO", ObjUtil.ifObjNull(result.get("PLATE_NO"), "").toString());
										loadTable.setEditValue(row, "VEHICLE_TYP_ID", ObjUtil.ifObjNull(result.get("VEHICLE_TYP_ID"), "").toString());
										loadTable.setEditValue(row, "DRIVER1", ObjUtil.ifObjNull(result.get("DRIVER1_NAME"), "").toString());
										loadTable.setEditValue(row, "MOBILE1", ObjUtil.ifObjNull(result.get("MOBILE1"), "").toString());
									}
									else {
										loadTable.setEditValue(row, "VEHICLE_TYP_ID", "");
										loadTable.setEditValue(row, "DRIVER1", "");
										loadTable.setEditValue(row, "MOBILE1", "");
									}
								}
						
					});
				}
			}
			
		});*/
		//comboWidget[0]=TEMP_NO1;

		ListGridField TEMP_NO2 = new ListGridField("TEMP_NO2","温控设备2",70);		
		//initTemp(loadTable,TEMP_NO2,"ID", "EQUIP_NO");
		TEMP_NO2.setHidden(true);
		//comboWidget[1]=TEMP_NO2;
		//Util.initComboValue(comboWidget, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "","");
		ListGridField TEMP_NO2_NAME = new ListGridField("TEMP_NO2_NAME","温控设备2",65);
		//Util.initComboValue(TEMP_NO1, "BAS_TEMPEQ", "ID", "EQUIP_NO", "", "");
		initTemp(loadTable,TEMP_NO2_NAME,"TEMP_NO2");
		TEMP_NO2_NAME.setCanEdit(true);
		
		
		final ListGridField SUPLR_ID = new ListGridField("SUPLR_ID", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 70);  //供应商
		SUPLR_ID.setHidden(true);
		//Util.initOrgSupplier(SUPLR_NAME, "");
		//suplrLst.add(SUPLR_NAME);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", ColorUtil.getRedTitle(Util.TI18N.SUPLR_NAME()), 85);  //供应商
//		initSUPPLIERByQuery(loadTable,SUPLR_NAME,"SUPLR_ID");
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
				String dri2 = loadTable.getSelectedRecord().getAttribute("DRIVER_ID");
				Util.db_async.getRecord("STAFF_NAME", "V_BAS_STAFF", " where ID='"+dri2+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						if(result!=null && result.size()>0){ 
							if(result.get(0).get("STAFF_NAME").toString().equals(driver)){
								return;
							}else{
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
						}else{
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
				
		groupTable.setFields(ADDWHO,LOAD_NO,STATUS_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, TRANS_SRVC_ID_NAME, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA,SUPLR_ID, SUPLR_NAME, PLATE_NO,TRAILER_NO,VEHICLE_TYP
				,DRIVER_ID,DRIVER1,MOBILE1, REMAIN_GROSS_W,TEMP_NO1,TEMP_NO1_NAME,TEMP_NO2,TEMP_NO2_NAME,GPS_NO1,GPS_NO1_NAME,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W);	
		
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
									// TODO Auto-generated method stub		
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
		
//		
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
		
		/*loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			public void onSelectionChanged(SelectionEvent event) {
				enableOrDisables(save_map, false);
				selRecord = event.getRecord();
				if(selRecord != null) {
					if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_CREATE_NAME)) {
						enableOrDisables(del_map, true);
						setSendBtnStatus(true);
					}
					else if(selRecord.getAttribute("STATUS_NAME").equals(StaticRef.TRANS_DEPART_NAME)){
						enableOrDisables(del_map, false);
						setSendBtnStatus(false);
					}
					else {
						enableOrDisables(del_map, false);
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_06, confirmButton, false);
						setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, false);
					}
				}
			}
		});*/
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
		/*loadTable.addEditorExitHandler(new EditorExitHandler() {

			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(saveButton != null) {
					saveButton.fireEvent(new ClickEvent(saveButton.getJsObj()));
				}
			}
			
		});*/
		
	
		final Menu menu = new Menu();//主界面【调度单信息】页签：右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
	    MenuItem printItem = new MenuItem("打印调度单",StaticRef.ICON_PRINT);
	    printItem.addClickHandler(new DispatchPrintAction(loadTable));
		menu.addItem(printItem);
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
//		MenuItem b2cPrintItem = new MenuItem("打印B2C派送调度出库单",StaticRef.ICON_PRINT);
//		b2cPrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2cDispatch"));
//		MenuItem b2cRoutePrintItem = new MenuItem("打印B2C直送调度出库单",StaticRef.ICON_PRINT);
//		b2cRoutePrintItem.addClickHandler(new DispatchPrintAction(loadTable, "b2cRouteDispatch"));
		
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
		
		//MenuItem newDispatch=new MenuItem("打印不干胶模板",StaticRef.ICON_PRINT);
		//newDispatch.addClickHandler(new DispatchPrintAction(loadTable,"newDispatch"));
		
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
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_29)) {                                          //wangjun 2010-3-24
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

	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_11)) {
			 MenuItem skuList = new MenuItem(Util.BI18N.GOODS_INFO(),StaticRef.ICON_NEW);  //货品信息
		     menu.addItem(skuList);
		     skuList.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				  @Override
				  public void onClick(MenuItemClickEvent event) {
					  ListGridRecord loadRec = loadTable.getSelectedRecord();
					  Criteria crit = loadTable.getCriteria();
				        if(crit != null && Util.iff(crit.getAttributeAsString("HISTORY_FLAG"),"false").equals("true")) {
				        	dispLoadWin = new DispLoadWin(getView(),loadRec.getAttribute("LOAD_NO"),"true").getViewPanel();
				        }
				        else {
				        	dispLoadWin = new DispLoadWin(getView(),loadRec.getAttribute("LOAD_NO")).getViewPanel();
				        }
				  }
			 });
		}
	    /*if(isPrivilege(TrsPrivRef.DISPATCH_P2_12)) {
	    	menu.addItem(itemSeparator);
			 MenuItem skuList = new MenuItem("路线预览",StaticRef.ICON_SEARCH);  //行车预览
		     menu.addItem(skuList);
		     skuList.addClickHandler(new ViewRouteAction(loadTable));
		    
		}*/
	    
	    /*if(isPrivilege(TrsPrivRef.DISPATCH_P2_13) || isPrivilege(TrsPrivRef.DISPATCH_P2_14)) {
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_13)) {
		    	menu.addItem(itemSeparator);
			    MenuItem skuList = new MenuItem(Util.BI18N.DISPATCH_CONFIRM(),StaticRef.ICON_CONFIRM); //配车确认
		        menu.addItem(skuList);
		        skuList.addClickHandler(new LoadDispatchConfirmAction(loadTable));
		    }
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_14)) {
			    MenuItem skuList = new MenuItem(Util.BI18N.CANCEL_DISPATCH(),StaticRef.ICON_CANCEL); //取消确认
		        menu.addItem(skuList);
		        skuList.addClickHandler(new LoadCancelConfirmAction(loadTable));
		       
			}
		   
	    }*/
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_15) || isPrivilege(TrsPrivRef.DISPATCH_P2_16)) {
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_15)) {
		    	menu.addItem(itemSeparator);
				MenuItem skuList = new MenuItem(Util.BI18N.DISPATCH_AUDIT(),StaticRef.ICON_CONFIRM); //配车审核
			    menu.addItem(skuList);
			    skuList.addClickHandler(new LoadDispatchAuditAction(loadTable,check_map, getView()));
			}
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_16)) {
				MenuItem skuList = new MenuItem(Util.BI18N.CANCEL_AUDIT(),StaticRef.ICON_CANCEL);  //取消审核
			    menu.addItem(skuList);
			    skuList.addClickHandler(new LoadCancelAuditAction(loadTable));
			    
			}
		    
		    /*if(isPrivilege(TrsPrivRef.DISPATCH_P2_31)) {
		        MenuItem getBack = new MenuItem("打回",StaticRef.ICON_CANCEL);
		        menu.addItem(getBack); 
		        getBack.addClickHandler(new LoadGetBackAction(loadTable));
		    }*/   

	    }
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_30)) {  //刷新  （调度刷新）
	    	menu.addItem(itemSeparator);
		    MenuItem refreshItem = new MenuItem(Util.BI18N.REFRESH(),StaticRef.ICON_REFRESH);
		    menu.addItem(refreshItem);
		    qryUnloadTableAction = new QueryUnloadTableAction(loadTable, pageForm);
		    refreshItem.addClickHandler(qryUnloadTableAction);
		    
	    }
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_10)) {
			MenuItem expList = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);  //导出
		    menu.addItem(expList);
		    expList.addClickHandler(new ExportAction(loadTable));
	    } 
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_35)) {
			MenuItem skuList = new MenuItem("费用管理",StaticRef.ICON_NEW);  //费用管理
		    menu.addItem(skuList);
		    skuList.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					SuplrFeeSettView factory = (SuplrFeeSettView)ClassTools.newInstance("com.rd.client.view.settlement.SuplrFeeSettView");
					TreeNode node = new ExplorerTreeNode("承运商费用管理", "P06_T066", "P06_T03",StaticRef.ICON_NODE, factory, true, "");
					showSample(node);
					if(factory != null) {
						factory.getViewPanel();
						Criteria crit = new Criteria();
						crit.addCriteria("OP_FLAG","M");
						crit.addCriteria("LOAD_NO",loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
						factory.loadTable.invalidateCache();
						factory.loadTable.fetchData(crit);
					}
				}
			});
		    
		}
	    loadTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
	    
	}
	

	/**
	 * 业务日志
	 * @author wangjun
	 * @param logTable
	 */
	private void createTransLogField(SGTable logTable) {

		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.SHPM_NO(),150);
		ListGridField OCCUR_TIME = new ListGridField("ADDTIME", Util.TI18N.OCCUR_TIME(), 140);
		ListGridField OPERATE_PERSON = new ListGridField("ADDWHO",Util.TI18N.OPERATE_PERSON(), 80);
		ListGridField OPERATE_RECODE = new ListGridField("NOTES",Util.TI18N.OPERATE_RECODE(), 170);

		logTable.setFields(DOC_NO, OPERATE_RECODE, OPERATE_PERSON, OCCUR_TIME);
		
	}
	
	
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
//			SUPLR_ID_NAME.setCanEdit(true);
//			SUPLR_ID_NAME.setTitle(ColorUtil.getRedTitle(SUPLR_ID_NAME.getTitle()));
			//Util.initOrgSupplier(SUPLR_ID_NAME, "");
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
//			PLATE_NO.setCanEdit(true);
//			PLATE_NO.setTitle(PLATE_NO.getTitle());
			/*PLATE_NO.addEditorExitHandler(new EditorExitHandler() {

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
				
			});*/
		}
		else {
			MSGUtil.sayError("列表中必须配置" + Util.TI18N.PLATE_NO());
		}
		ListGridField VEHICLE_TYP = table.getField("VEHICLE_TYP_ID");
		if(VEHICLE_TYP != null) {
//			VEHICLE_TYP.setCanEdit(true);
//			VEHICLE_TYP.setTitle(VEHICLE_TYP.getTitle());
			//Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
			vehTypeLst.add(VEHICLE_TYP);
		}
		else {
			MSGUtil.sayError("列表中必须配置" + Util.TI18N.VEHICLE_TYPE());
		}
		ListGridField DRIVER = table.getField("DRIVER");
		if(DRIVER != null) {
//			DRIVER.setCanEdit(true);
		}
		ListGridField MOBILE = table.getField("MOBILE");
		if(MOBILE != null) {
//			MOBILE.setCanEdit(true);
		}
	}
	
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		//作业单按钮
		searchButton=createUDFBtn(Util.BI18N.SEARCHSHPM(), StaticRef.ICON_SEARCH, TrsPrivRef.DISPATCH_P1_01);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin!=null){
					searchWin.hide();
				}
				if(shipwin==null){
					chooseForm=new SGPanel();
					shipwin= new UnshpmQueryWin(620,440,unshpmDS,subView.createUnshpmQueryForm(chooseForm),topSectionStack.getSection(0)).getViewPanel();
				}
				else{
					shipwin.show();
				}
			}
		});
		
	    //调度单按钮
	    IButton loadButton = createUDFBtn(Util.BI18N.SEARCHLOAD(), StaticRef.ICON_SEARCH, TrsPrivRef.DISPATCH_P2_01);
	    loadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(shipwin!=null){
					shipwin.hide();
				}
			   if(searchWin==null){
				   searchForm=new SGPanel();
				   //searchForm.setDataSource(unshpmDS);
			       searchWin = new SearchWin(loadDS, subView.createSerchForm(searchForm),
				   downSectionStack.getSection(0)).getViewPanel();
			       searchWin.setWidth(620);
			       searchWin.setHeight(330);
			   }
			   else{
					searchWin.show();
			   }			
			}
	    });
        
	    //生成调度单按钮
	    shpmButton = createUDFBtn(Util.BI18N.CREATELOAD(), StaticRef.ICON_SAVE, TrsPrivRef.DISPATCH_P2_02);
	    shpmButton.addClickHandler(new MakeLoadNoAction(unshpmTable, loadTable, getView()));
        
        //保存按钮
        saveButton = createBtn(StaticRef.SAVE_BTN, TrsPrivRef.DISPATCH_P2_03);
        saveButton.addClickHandler(new SaveLoadNoAction(loadTable,check_map, getView()));
        
        //删除按钮
        delButton = createBtn(StaticRef.DELETE_BTN, TrsPrivRef.DISPATCH_P2_04);
        delButton.addClickHandler(new DeleteLoadNoAction(loadTable, this));
        
        //取消按钮
        canButton = createBtn(StaticRef.CANCEL_BTN, TrsPrivRef.DISPATCH_P2_05);
        canButton.addClickHandler(new CancelAction(loadTable));
        
	    //确认发运按钮
	    confirmButton = createUDFBtn("发车确认", StaticRef.ICON_CONFIRM, TrsPrivRef.DISPATCH_P2_06);
	    confirmButton.addClickHandler(new LoadSendConfirmAction(getView(), loadTable, shpmTable));
	    
	    //取消发运按钮
	    cansendButton = createUDFBtn("取消发车", StaticRef.ICON_CANCEL, TrsPrivRef.DISPATCH_P2_07);
	    cansendButton.addClickHandler(new LoadCancelSendAction(getView(), loadTable, shpmTable));
	    
	    //提货单打印预览
//	    loadBillButton = createUDFBtn("打印调度单", StaticRef.ICON_PRINT, TrsPrivRef.DISPATCH_P2_08);
//	    loadBillButton.addClickHandler(new ShipPrintAction(this,loadTable, "b2b调度单"));
//	    
//	    //提货单打印
//	    loadDoBillButton = createUDFBtn("打印调度单", StaticRef.ICON_PRINT, TrsPrivRef.DISPATCH_P2_08);
//	    loadDoBillButton.addClickHandler(new ShipPrintAction(this,loadTable, "干线调度单"));
//	    //送货单打印
//	    sendBillButton = createUDFBtn("打印调度单", StaticRef.ICON_PRINT, TrsPrivRef.DISPATCH_P2_09);
//	    sendBillButton.addClickHandler(new ShipPrintAction(this,loadTable,"派送调度单"));
//        
//	    IButton suihdButton = createUDFBtn("打印调度单", StaticRef.ICON_PRINT, TrsPrivRef.DISPATCH_P2_09);
//	    suihdButton.addClickHandler(new ShipPrintAction(this,loadTable,"提货调度单"));
        //导出按钮
        //IButton expButton = createBtn(StaticRef.EXPORT_BTN, TrsPrivRef.DISPATCH_P2_10);
        //expButton.addClickHandler(new ExportAction(loadTable));
    
        del_map.put(TrsPrivRef.DISPATCH_P2_04, delButton);
        save_map.put(TrsPrivRef.DISPATCH_P2_03, saveButton);
        save_map.put(TrsPrivRef.DISPATCH_P2_05, canButton);
        sendMap.put(TrsPrivRef.DISPATCH_P2_06, confirmButton);
        sendMap.put(TrsPrivRef.DISPATCH_P2_07, cansendButton);
        enableOrDisables(del_map, true);
        enableOrDisables(save_map, false);
        setConfirmBtnStatus(false);
        setButtonEnabled(TrsPrivRef.DISPATCH_P2_02,shpmButton,true);
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, loadButton, shpmButton, saveButton, delButton, canButton, confirmButton, cansendButton);
	  
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
	    
	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_24)){                          //待调右键权限    wangjun 2010-3-9
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_02)) {
			    MenuItem addItem = new MenuItem("加入调度单",StaticRef.ICON_NEW);
			    addItem.setKeyTitle("Alt+A");
			    KeyIdentifier addKey = new KeyIdentifier();
			    addKey.setAltKey(true);
			    addKey.setKeyName("A");
			    addItem.setKeys(addKey);
			    menu.addItem(addItem);
			    addItem.addClickHandler(new AddShpmNoAction(this,unshpmTable, loadTable));
		    }
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_03)) {
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
		    
//		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_34)) {//保存
//			    MenuItem saveItem = new MenuItem(Util.BI18N.SAVE(),StaticRef.ICON_SAVE);
//			    menu.addItem(saveItem);
//			    saveItem.addClickHandler(new UnShpmSaveAction(unshpmTable));
//			    
//		    }
		   
		    
		    /*if(isPrivilege(TrsPrivRef.DISPATCH_P1_04)) {
		    	menu.addItem(itemSeparator);
			    MenuItem viewItem = new MenuItem(Util.BI18N.VIEWSHPM(),StaticRef.ICON_SEARCH);
			    viewItem.setKeyTitle("Alt+V");
			    KeyIdentifier viewKey = new KeyIdentifier();
			    viewKey.setAltKey(true);
			    viewKey.setKeyName("V");
			    viewItem.setKeys(viewKey);
			    menu.addItem(viewItem);
			    viewItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
	
					@Override
					public void onClick(MenuItemClickEvent event) {
						 //new ShipWin(550,420,unshpmDS,null,topSectionStack.getSection(0)).getViewPanel();
						if(unshpmTable.getSelection().length == 1) {
							ListGridRecord rec = unshpmTable.getSelectedRecord();
							String strWhere = " and SHPM_NO != '" + rec.getAttribute("SHPM_NO") + "'";
							transTrackWin=new TransTrackWin(rec.getAttribute("ODR_NO"), strWhere).getViewPanel();
						}
						else {
							SC.warn("请选择一个作业单！");
						}
					}
			    	
			    });
			    
			    
		    }*/
		    /*if(isPrivilege(TrsPrivRef.DISPATCH_P1_04)) {
		    	menu.addItem(itemSeparator); 
			    MenuItem inItem = new MenuItem("作业单补码",StaticRef.ICON_CONFIRM); 
			    menu.addItem(inItem);
			    inItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

			    	@Override
					public void onClick(MenuItemClickEvent event) {
						// TODO Auto-generated method stub
						if(unshpmTable != null && unshpmTable.getSelection().length > 0){
							//弹出窗口
							new ModifyShpmWin(unshpmTable, unshpmTable.getSelection());
							
						} else {
							SC.warn("请选择作业单!");
						}
					}
			    	
			    });		    
		    }*/
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_33)) {
		    	 MenuItem UnShpmPrintItem = new MenuItem("打印提货单",StaticRef.ICON_PRINT);
		    	 UnShpmPrintItem.addClickHandler(new UnShpmPrintAction(this,unshpmTable));
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
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_05)) {
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
								new SplitActionWin(unshpmlstTable, unshpmTable, getView()).getViewPanel().show();
							}
							
						} else {
							SC.warn("未勾选作业单明细!");
						}			
					}
			    });
		    }
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_06)) {
			    MenuItem cansplitItem = new MenuItem(Util.BI18N.CANCELSPLIT(),StaticRef.ICON_CANCEL); 
			    cansplitItem.setKeyTitle("Alt+C");
			    KeyIdentifier cansplitKey = new KeyIdentifier();
			    cansplitKey.setAltKey(true);
			    cansplitKey.setKeyName("C");
			    cansplitItem.setKeys(cansplitKey);
			    menu.addItem(cansplitItem);
			    cansplitItem.addClickHandler(new CancelSplitAction(getView(), unshpmTable));
			    
		    }
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_07)) {
		    	menu.addItem(itemSeparator); 
			    MenuItem splitJrnyItem = new MenuItem("行程拆分",StaticRef.ICON_CONFIRM);
			    splitJrnyItem.setKeyTitle("Alt+S");
			    menu.addItem(splitJrnyItem);
			    splitJrnyItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
	
					@Override
					public void onClick(MenuItemClickEvent event) {
						/*if(unshpmTable.getSelectedRecord().getAttribute("PRINT_FLAG").equals("N")){
							
							SC.warn("作业单"+unshpmTable.getSelectedRecord().getAttribute("SHPM_NO")+" 已打印提货单不允许拆分！");
							return;
						}*/
						
						if(unshpmTable != null && unshpmTable.getSelection().length > 0){
							//弹出窗口
							new JourneySplitWin(getView(), unshpmTable.getSelection());
							
						} else {
							SC.warn("请选择作业单!");
						}			
					}
			    });
		    }
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P1_08)) {
			    MenuItem canJnrysplitItem = new MenuItem("取消行程拆分",StaticRef.ICON_CANCEL); 
			    menu.addItem(canJnrysplitItem);
			    canJnrysplitItem.addClickHandler(new CancelJourneySplitAction(getView(), unshpmTable));
			    
		    }
		    
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_25)) {
		    	menu.addItem(itemSeparator);
				MenuItem expList = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);  //导出
				menu.addItem(expList);
			    expList.addClickHandler(new ExportAction(unshpmTable));
		    }   
		    if(isPrivilege(TrsPrivRef.DISPATCH_P2_26)) {
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
	
	private DispatchView getView() {
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
		setButtonEnabled(TrsPrivRef.DISPATCH_P2_06, confirmButton, bolSet);
		setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, !bolSet);
	}
	
	public void setConfirmBtnStatus(boolean bolSet) {
		setButtonEnabled(TrsPrivRef.DISPATCH_P2_06, confirmButton, bolSet);
		setButtonEnabled(TrsPrivRef.DISPATCH_P2_07, cansendButton, bolSet);
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
    
    private void createFeeInfo() {
		// 费用明细
    	feeTable = new SGTable(billDS) {
    		protected Canvas getExpansionComponent(final ListGridRecord record) { 
    			VLayout layout = new VLayout(5);
                
                feeitemTable = new ListGrid();
                feeitemTable.setDataSource(billItemDS);
                feeitemTable.setWidth("78%");
                feeitemTable.setHeight(46);
                feeitemTable.setCellHeight(22);
                feeitemTable.setCanEdit(false);
                feeitemTable.setAutoFitData(Autofit.VERTICAL);
                
                ListGridField SHPM_NO = new ListGridField("DOC_NO", Util.TI18N.SHPM_NO(), 100);
        		ListGridField FEE_ID = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 80);
        		ListGridField FEE_BAS = new ListGridField("FEE_BAS_NAME",Util.TI18N.FEE_BASE(), 80);
        		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 70);
        		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 60);
        		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 80);
        		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(), 80);
        		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实付费用", 80);
        		feeitemTable.setFields(SHPM_NO,FEE_ID, FEE_BAS, BAS_VALUE, PRICE, PRE_FEE,DUE_FEE,PAY_FEE);
        		
        		Record feeRecord = feeTable.getSelectedRecord();
        		Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("LOAD_NO",feeRecord.getAttributeAsString("LOAD_NO"));
				criteria.addCriteria("FEE_NAME",feeRecord.getAttributeAsString("FEE_NAME"));
				
				feeitemTable.fetchData(criteria);
        		
        		layout.addMember(feeitemTable);
                layout.setLayoutLeftMargin(35);
				return layout;     
    		}
    	};
    	feeTable.setCanExpandRecords(true);
    	feeTable.setShowFilterEditor(false);
    	feeTable.setShowRowNumbers(true);

        ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 100);
		ListGridField FEE_ID = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 100);
		//Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", "", "");
		ListGridField FEE_BAS = new ListGridField("FEE_BAS_NAME",Util.TI18N.FEE_BASE(), 80);
		//Util.initComboValue(FEE_BAS, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 80);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 80);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 80);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", Util.TI18N.DISCOUNT(), 80);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(), 80);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实付费用", 80);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 80);
		feeTable.setFields(LOAD_NO, FEE_ID, FEE_BAS, BAS_VALUE,PRICE, PRE_FEE, DISCOUNT_RATE, DUE_FEE, PAY_FEE, NOTES);

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
//		temp_no.setPickListBaseStyle("myBoxedGridCell");
		temp_no.setPickListFields(EQU_ID,EQUIP_NO);
		
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");		
		criteria.addCriteria("STATUS","DF252F0637784E9EA575CCACB64050FC");
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
//						else {
//						HashMap<String,String> map = new HashMap<String,String>();
//						Util.db_async.getRecord("ID,EQUIP_NO", "BAS_TEMPEQ", "", map, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//							
//							@Override
//							public void onSuccess(ArrayList<HashMap<String, String>> result) {
//								if(result==null) return;
//								table.setEditValue(rowNum, code, result.get(0).get("ID"));
//								table.setEditValue(rowNum, name, result.get(0).get("EQUIP_NO"));
//								if(rec != null){
//									rec.setAttribute(code, result.get(0).get("ID"));
//									rec.setAttribute(name, result.get(0).get("EQUIP_NO"));
//								}
//							}
//							
//							@Override
//							public void onFailure(Throwable caught) {
//								
//							}
//						});   
//					}
//				}
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
//		temp_no.setPickListBaseStyle("myBoxedGridCell");
		temp_no.setPickListFields(EQU_ID,EQUIP_NO);
		
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("STATUS","DF252F0637784E9EA575CCACB64050FC");
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
    
//    private void initSUPPLIERByQuery(final SGTable table,final ListGridField SUPLRNAME,final String suplrid) {
//    	DataSource custDS = SUPPLIERDS.getInstance("VC_SUPPLIER");
//    	
//    	ListGridField ID = new ListGridField("ID","ID");
//    	ID.setHidden(true);
//		ListGridField SUPLR_CNAME = new ListGridField("SUPLR_CNAME",Util.TI18N.CUSTOMER_NAME());
//		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE",Util.TI18N.CUSTOMER_CODE(),80);
//		final ComboBoxItem suplr_name = new ComboBoxItem();
//		suplr_name.setOptionDataSource(custDS);  
//		suplr_name.setDisabled(false);
//		suplr_name.setShowDisabled(false);
//		suplr_name.setDisplayField("FULL_INDEX"); 
//		suplr_name.setValueField("SUPLR_CNAME");
//		suplr_name.setPickListBaseStyle("myBoxedGridCell");
//		suplr_name.setPickListWidth(230);
//		suplr_name.setPickListFields(ID,SUPLR_CODE, SUPLR_CNAME);
//		Criteria criteria = new Criteria();
//		criteria.addCriteria("OP_FLAG","M");
//		suplr_name.setPickListCriteria(criteria);
//		SUPLRNAME.setEditorType(suplr_name);
//		SUPLRNAME.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				Object obj = event.getValue();
//				System.out.println(obj);
//				table.setEditValue(event.getRowNum(), suplrid, ObjUtil.ifObjNull(obj,"").toString());
//			}
//		});
//		
//		
//	}
    
	protected void setTabSet(TabSet tabSet) {
		this.mainTabSet = tabSet;
	}
	
	protected TabSet getTabSet() {
		return this.mainTabSet;
	}
    
    protected void showSample(TreeNode node) {
        boolean isExplorerTreeNode = node instanceof ExplorerTreeNode;
       if (isExplorerTreeNode) {
           ExplorerTreeNode explorerTreeNode = (ExplorerTreeNode) node;
           PanelFactory factory = explorerTreeNode.getFactory();
           if (factory != null) {
               String panelID = factory.getCanvasID();
               Tab tab = null;
               if (panelID != null) {
                   String tabID = panelID + "_tab";
                   tab = mainTabSet.getTab(tabID);
               }
               if (tab == null) {
               	final SuplrFeeSettView panel =(SuplrFeeSettView)factory.createCanvas(explorerTreeNode.getNodeID(), mainTabSet);
                   tab = new Tab();
                   tab.setID(factory.getCanvasID() + "_tab");
                   tab.setAttribute("historyToken", explorerTreeNode.getNodeID());
                   //tab.setContextMenu(contextMenu);

                   String sampleName = explorerTreeNode.getName();

                   String icon = explorerTreeNode.getIcon();
                   if (icon == null) {
                       icon = "silk/plugin.png";
                   }
                   String imgHTML = Canvas.imgHTML(icon, 16, 16);
                   tab.setTitle("<span>" + imgHTML + "&nbsp;" + sampleName + "</span>");
                   tab.setPane(panel);
                   tab.setCanClose(true);
                   mainTabSet.addTab(tab);
                   mainTabSet.selectTab(tab);
                   Criteria cri=new Criteria();
                   cri.addCriteria("OP_FLAG","M");
                   cri.addCriteria("LOAD_NO",loadTable.getSelectedRecord().getAttribute("LOAD_NO") );
                   panel.loadTable.fetchData(cri, new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							JavaScriptObject jsobject = panel.sectionStack2.getSection(0).getAttributeAsJavaScriptObject("controls");
							Canvas[] canvas = null;
							if(jsobject != null) {
								canvas = Canvas.convertToCanvasArray(jsobject);
							}
							else {
								canvas = new Canvas[1];
							}
							for(int i = 0; i < canvas.length; i++) {
								if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
									DynamicForm pageForm = (DynamicForm)canvas[i];
									if(pageForm != null) {
										pageForm.getField("CUR_PAGE").setValue("1");
										pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
										pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
										String sqlwhere = Cookies.getCookie("SQLWHERE");
										String key = Cookies.getCookie("SQLFIELD1");
										String value = Cookies.getCookie("SQLFIELD2");
										String alias = Cookies.getCookie("SQLALIAS");
										if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
											 panel.loadTable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
											 panel.loadTable.setProperty("SQLFIELD1", key);
											 panel.loadTable.setProperty("SQLFIELD2", value);
											 panel.loadTable.setProperty("SQLALIAS", alias);
										}
									}
									break;
								}
							}
						}
					});
               } else {
                   mainTabSet.selectTab(tab);
               }
           }
       }
   } 

    private void initCombo() {
    	//initArea(loadTable,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
    	Util.initComboBatch(vehTypeLst, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC", null);  //车辆类型
    	Util.initOrgSupplier(suplrLst, "");
    }

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		//setFUNCID(id);
		DispatchView view = new DispatchView();
		view.setFUNCID(id);
		view.setTabSet(tabSet);
		view.addMember(view.getViewPanel());		
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
    
}