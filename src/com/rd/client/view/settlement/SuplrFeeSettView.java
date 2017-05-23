package com.rd.client.view.settlement;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.DeletePayFeeAction;
import com.rd.client.action.settlement.PayAction;
import com.rd.client.action.settlement.SavePayFeeAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillLoadFeeDS;
import com.rd.client.ds.settlement.BillShpmFeeDS;
import com.rd.client.ds.tms.Load2DS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.RecordSummaryFunctionType;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.types.TitleOrientation;
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
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 财务管理--结算管理--承运商结算管理
 * @author fanglm 
 * @create time 2011-01-20 10:55
 *
 */
@ClassForNameAble
public class SuplrFeeSettView extends SGForm implements PanelFactory {
	
	//private SGTable table;
	public SGTable loadTable;//调度单列表
//	public SGTable shpmTable;//作业单列表
	public ListGrid feeTable;//费用列表
//	public SGTable damageTable;
	private Window searchWin;
	//private Window searchWin2;
	private Window searchWin3;
	public SGPanel searchForm = new SGPanel();
	public SGPanel searchForm2 = new SGPanel();
	public SGPanel searchForm3 = new SGPanel();
	//private SectionStack sectionStack;
	//private  SectionStackSection  listItem;
	//private DataSource ds;
	private DataSource loadDs;
//	private DataSource shpmDs;
//	private DataSource shpmMainDS;
//	private DataSource detailDS;
	//private IButton saveButton;
	//private IButton delButton;
//	private IButton confirmAcc;
//	private IButton cancelAcc;
	private IButton confirmAudit;
	//private IButton cancelAudit;
	//private IButton dReturn;
	@SuppressWarnings("unused")
	private Window SupFeeCoutWin;
	@SuppressWarnings("unused")
	private Window SupOrderDetailWin;
	public String load_no;
	public String odr_no;
	public String shpm_no;
	//private DynamicForm pageForm;
	
	public SectionStack sectionStack2;
	private  SectionStackSection  listItem2;
	private DynamicForm pageForm2;
	
//	private SectionStack sectionStack3;
//	private  SectionStackSection  listItem3;
//	private DynamicForm pageForm3;
	
	private SGPanel feeInfo;
	private ToolStrip toolStrip;
	
	public SGTable headTable;
//	private Window shpmFeeWin;
	
	private SGCombo SHPM_NO;
	private SGCombo SETT_ID;
	private SGCombo FEE_ID;
	private SGText PRE_FEE;
	private SGText PAY_FEE;
	
	public int pageNum = 0;
	public String titName ="";
	private ValuesManager vm;
	private DataSource billLoadDS;
	private DataSource billShpmDS;
	//private DataSource lossDamageDS;
	public Record selectLoad;
	
	
	//按钮权限
	public HashMap<String, IButton> ins_fee_btn;
	public HashMap<String, IButton> sav_fee_btn;
	public HashMap<String, IButton> del_fee_btn;
	
	/*public SuplrFeeSettView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("99%");
//		ds = BillDetailDS.getInstance("V_BILL_DETAIL","TRANS_BILL_DETAIL");
		billLoadDS = BillLoadFeeDS.getInstance("V_BILL_LOAD_PAY3","BILL_DETAIL_PAY");
		loadDs = Load2DS.getInstance("V_LOAD_HEADER2","TRANS_LOAD_HEADER");
		billShpmDS = BillShpmFeeDS.getInstance("V_BILL_SHPM_PAY");
//		shpmDs = ShpmFeeDS.getInstance("SETT_SHPM_FEE","TRANS_SHIPMENT_HEADER");
//		shpmMainDS = ShpmDS2.getInstance("V_SHIPMENT_HEADER", "TRANS_SHIPMENT_HEADER");
//		detailDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM","TRANS_SHIPMENT_ITEM");
//		lossDamageDS=TranLossDamageDS.getInstance("V_LOSS_DAMAGE_","TRANS_LOSS_DAMAGE");
		
		
//		table = new SGTable(ds, "100%", "100%", true, true, false){
//			@Override  
//            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {   
//                if ("Y".equals(record.getAttribute("MODIFY_FLAG")) && colNum == 12) {   
//                    return "font-weight:bold;background:red";
//                } 
//                else {   
//                    return super.getCellCSSText(record, rowNum, colNum);   
//                }  
//            }
//			
//		};
		
		
		//创建列表
		createSettAsLoad();
//		createListField();
		
//		sectionStack = new SectionStack();
//		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
//		listItem.setItems(table);
//		listItem.setExpanded(true);
//		sectionStack.addSection(listItem);
//		pageForm = new SGPage(table, true).initPageBtn();
//		listItem.setControls(pageForm);
//		sectionStack.setWidth("100%");
		
		
	
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		main.addMember(toolStrip);
//		main.addMember(sectionStack);
		
		TabSet bottoTabSet = new TabSet();
		bottoTabSet.setWidth100();
		bottoTabSet.setHeight100();
		bottoTabSet.setMargin(1);
		
//		if(isPrivilege(SettPrivRef.SUPLRFEE_P2)) {
//			Tab tab2 = new Tab("按调度单");
//			tab2.setPane();
//			bottoTabSet.addTab(tab2);
//		}
		
		/*if(isPrivilege(SettPrivRef.SUPLRFEE_P3)) {
			Tab tab1 = new Tab("按作业单");
		  	tab1.setPane(createShpmPage());
			bottoTabSet.addTab(tab1);
		}
		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_01)){
			Tab tab = new Tab("按作业单明细");
			tab.setPane(sectionStack);
			bottoTabSet.addTab(tab);
		}*/
		main.addMember(createSettAsLoadPage());
		
		bottoTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				pageNum = event.getTabNum();
				titName = event.getTab().getTitle();
			}
		});
		
//		main.addMember(sectionStack);
		return main;
	}
	
	
//	private void createListField(){
//		
//		table.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				initAccBtn(event.getRecord());
//			}
//		});
//		/*table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//			
//			@Override
//			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
//				if("已审核".equals(event.getRecord().getAttributeAsString("AUDIT_STAT_NAME"))){
//					SC.warn("费用已审核，不能修改");
//					saveButton.setDisabled(true);
//					return;
//				}
//				
//				if(isPrivilege(SettPrivRef.SUPLRFEE_P0_01)){
//					saveButton.setDisabled(false);
//				}
//			}
//		});*/
//		table.setShowRowNumbers(true);
//		table.setCanEdit(true);
//		table.setShowGroupSummary(true); 
//		
//		ListGridField SERIAL_NUM = new ListGridField("SERIAL_NUM", "回单序列号", 80);
//		SERIAL_NUM.setCanEdit(true);
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 120);
//		LOAD_NO.setCanEdit(false);
//		ListGridField SHPM_NO = new ListGridField("SHPM_NO", Util.TI18N.SHPM_NO(), 120);
////		SHPM_NO.setFrozen(true);
//		SHPM_NO.setCanEdit(false);
//		ListGridField STATUS = new ListGridField("STATUS_NAME", Util.TI18N.STATUS(), 80);
//		STATUS.setCanEdit(false);
//		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 120);
////		CUSTOM_ODR_NO.setFrozen(true);
//		CUSTOM_ODR_NO.setCanEdit(false);
//		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME(), 80);
//		UNLOAD_AREA_NAME.setCanEdit(false);
//		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 160);
//		UNLOAD_NAME.setCanEdit(false);
//		ListGridField SHPM_ROW = new ListGridField("SHPM_ROW", Util.TI18N.SHPM_ROW(), 60);
//		SHPM_ROW.setCanEdit(false);
//		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 70);
//		SKU.setCanEdit(false);
//		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);
//		SKU_NAME.setCanEdit(false);
//		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC", Util.TI18N.SKU_SPEC(), 70);
//		SKU_SPEC.setCanEdit(false);
//		//ListGridField TRANS_UOM = new ListGridField("UOM", Util.TI18N.TRANS_UOM(), 70);
//		//TRANS_UOM.setCanEdit(false);
//		ListGridField ODR_QNTY = new ListGridField("ODR_QNTY", Util.TI18N.ODR_QNTY(), 70);
//		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
//		ODR_QNTY.setShowGridSummary(false);
//		ODR_QNTY.setCanEdit(false);
//		
//		
//		ListGridField QNTY = new ListGridField("QNTY_EACH", Util.TI18N.SHPM_QNTY(), 70);
//		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
//		QNTY.setShowGridSummary(true);
//		QNTY.setSummaryFunction(SummaryFunctionType.SUM);
//		QNTY.setCanEdit(false);
//		
//		ListGridField LD_QNTY = new ListGridField("LD_QNTY", Util.TI18N.LD_QNTY(), 70);
//		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
//		LD_QNTY.setShowGridSummary(true);
//		LD_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
//		
//		LD_QNTY.setCanEdit(false);
//		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY", Util.TI18N.UNLD_QNTY(), 70);
//		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
//		UNLD_QNTY.setShowGridSummary(true);
//		UNLD_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
//		
//		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE", "吨位", 70);
//		BAS_VALUE.setCanEdit(false);
//		Util.initFloatListField(BAS_VALUE, StaticRef.QNTY_FLOAT);
//		BAS_VALUE.setShowGridSummary(true);
//		BAS_VALUE.setSummaryFunction(SummaryFunctionType.SUM);
//		
//		
//		/*UNLD_QNTY.setCanEdit(false);
//		ListGridField UNSEND_QNTY = new ListGridField("UNSEND_QNTY","未发货数量", 70);
////		Util.initFloatListField(UNSEND_QNTY, StaticRef.QNTY_FLOAT);
//		UNSEND_QNTY.setCanEdit(false);*/
//		
//		ListGridField RATION = new ListGridField("RATIO", Util.TI18N.RATION(), 70);
//		
//		ListGridField ROUTE_MILE = new ListGridField("ROUTE_MILE",Util.TI18N.ROUTE_MILE(), 70);
////		Util.initFloatListField(ROUTE_MILE, StaticRef.QNTY_FLOAT);
//		ROUTE_MILE.setCanEdit(false);
//		
//		ListGridField MILE = new ListGridField("MILE",Util.TI18N.MILE(), 70);
////		Util.initFloatListField(MILE, StaticRef.QNTY_FLOAT);
//		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 70);
////		Util.initFloatListField(PRICE, StaticRef.PRICE_FLOAT);
//		RATION.addEditorExitHandler(new EditorExitHandler() {
//			
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				int row = event.getRowNum();
//				double qnty = Double.parseDouble(ObjUtil.ifNull(event.getRecord().getAttribute("UNLD_QNTY"),"0").toString());
//				double ratio = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(), event.getRecord().getAttributeAsString("RATIO")).toString());
//				double mile = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "MILE"), event.getRecord().getAttributeAsString("MILE")).toString());
//				double price = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "PRICE"),event.getRecord().getAttributeAsString("PRICE")).toString());
//				table.setEditValue(row, "DUE_FEE", qnty*ratio*mile*price*0.001);
//			}
//		});
//		MILE.addEditorExitHandler(new EditorExitHandler() {
//			
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				int row = event.getRowNum();
//				double qnty = Double.parseDouble(ObjUtil.ifNull(event.getRecord().getAttribute("UNLD_QNTY"),"0").toString());
//				double ratio = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "RATIO"), event.getRecord().getAttributeAsString("RATIO")).toString());
//				double mile = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(), event.getRecord().getAttributeAsString("MILE")).toString());
//				double price = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "PRICE"),event.getRecord().getAttributeAsString("PRICE")).toString());
//				table.setEditValue(row, "DUE_FEE", qnty*ratio*mile*price*0.001);
//			}
//		});
//		PRICE.addEditorExitHandler(new EditorExitHandler() {
//			
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				int row = event.getRowNum();
//				double qnty = Double.parseDouble(ObjUtil.ifNull(event.getRecord().getAttribute("UNLD_QNTY"),"0").toString());
//				double ratio = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "RATIO"), event.getRecord().getAttributeAsString("RATIO")).toString());
//				double mile = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "MILE"), event.getRecord().getAttributeAsString("MILE")).toString());
//				double price = Double.parseDouble(ObjUtil.ifObjNull(event.getNewValue(),event.getRecord().getAttributeAsString("PRICE")).toString());
//				table.setEditValue(row, "DUE_FEE", qnty*ratio*mile*price*0.001);
//			}
//		});
//		
//		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.SETT_CASH(), 70);
//		Util.initFloatListField(DUE_FEE, StaticRef.PRICE_FLOAT);
//		DUE_FEE.setShowGridSummary(true);
//		DUE_FEE.setSummaryFunction(SummaryFunctionType.SUM);
//		
//		DUE_FEE.addEditorExitHandler(new EditorExitHandler() {
//			
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				int row = event.getRowNum();
//				double qnty = Double.parseDouble(ObjUtil.ifNull(event.getRecord().getAttribute("UNLD_QNTY"),"0").toString());
//				double ratio = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "RATIO"), event.getRecord().getAttributeAsString("RATIO")).toString());
//				double mile = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "MILE"), event.getRecord().getAttributeAsString("MILE")).toString());
//				double price = Double.parseDouble(ObjUtil.ifObjNull(table.getEditValue(row, "PRICE"),event.getRecord().getAttributeAsString("PRICE")).toString());
//				if(ObjUtil.isNotNull(event.getNewValue()) && (Double.parseDouble(event.getNewValue().toString()) - qnty*ratio*mile*price*0.001 != 0)){
//					table.setEditValue(row, "MODIFY_FLAG", "Y");
//				}else{
//					table.setEditValue(row, "MODIFY_FLAG", "N");
//				}
//				
//			}
//		});
//		ListGridField PRE_PRICE = new ListGridField("PRE_FEE", Util.TI18N.PLAN_PRICE(), 70);
//		Util.initFloatListField(PRE_PRICE, StaticRef.PRICE_FLOAT);
//		PRE_PRICE.setShowGridSummary(true);
//		PRE_PRICE.setSummaryFunction(SummaryFunctionType.SUM);
//		
//		PRE_PRICE.setCanEdit(false);
//		ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT_NAME", Util.TI18N.ACCOUNT_STAT(), 70);
//		ACCOUNT_STAT.setCanEdit(false);
//		ListGridField AUDIT_STAT = new ListGridField("AUDIT_STAT_NAME", Util.TI18N.AUDIT_STAT(), 70);
//		AUDIT_STAT.setCanEdit(false);
//		
//		
//		ListGridField TRANS_NOTES = new ListGridField("TRANS_NOTES", "配车备注", 120);
//		TRANS_NOTES.setCanEdit(false);
//		
//		ListGridField NOTES = new ListGridField("NOTES", "配车家数", 120);
//		NOTES.setCanEdit(true);
//		
//		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 100);
//		ODR_TIME.setCanEdit(false);
//		
//
//		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME", "实际到货时间", 100);
//		UNLOAD_TIME.setCanEdit(false);
//
//		ListGridField ADDTIME = new ListGridField("DISPATCH_TIME", "调度配载时间", 100);
//		ADDTIME.setCanEdit(false);
//		
//		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.OP_LOAD_TIME(), 100);
//		DEPART_TIME.setCanEdit(false);
//		
//		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS", Util.TI18N.UNLOAD_ADDRESS(), 120);
//		UNLOAD_ADDRESS.setCanEdit(false);
//		
//		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", Util.TI18N.SUPLR_NAME(), 80);
//		SUPLR_NAME.setCanEdit(false);
//		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID(), 120);
//		EXEC_ORG_ID_NAME.setCanEdit(false);
//		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATE_NO(), 80);
//		PLATE_NO.setCanEdit(false);
//		ListGridField DIRVER = new ListGridField("DRIVER", Util.TI18N.DRIVER1(), 80);
//		DIRVER.setCanEdit(false);
//		ListGridField MOBILE = new ListGridField("MOBILE", Util.TI18N.MOBILE(), 100);
//		MOBILE.setCanEdit(false);
//		
////		table.setFields(SERIAL_NUM,LOAD_NAME,SHPM_NO,CUSTOM_ODR_NO,STATUS,UNLOAD_NAME,SHPM_ROW,SKU,SKU_NAME,SKU_SPEC,
////				TRANS_UOM,ODR_QNTY,QNTY,LD_QNTY,UNSEND_QNTY,UNLD_QNTY,RATION,ROUTE_MILE,MILE,PRICE,DUE_FEE,PRE_PRICE,
////				TRANS_NOTES,NOTES,ACCOUNT_STAT,AUDIT_STAT,ODR_TIME,ADDTIME,DEPART_TIME,UNLOAD_TIME,UNLOAD_ADDRESS,
////				SUPLR_NAME,EXEC_ORG_ID_NAME,PLATE_NO,DIRVER,MOBILE);
//		
//		table.setFields(SERIAL_NUM,LOAD_NO,CUSTOM_ODR_NO,UNLOAD_AREA_NAME,UNLOAD_NAME,SKU,SKU_NAME,SKU_SPEC,RATION,ODR_QNTY,LD_QNTY,BAS_VALUE,DUE_FEE,
//				UNLD_QNTY,MILE,PRICE,TRANS_NOTES,NOTES,SHPM_NO,STATUS,SHPM_ROW,
//				QNTY,ROUTE_MILE,PRE_PRICE,ACCOUNT_STAT,AUDIT_STAT,ODR_TIME,ADDTIME,DEPART_TIME,
//				UNLOAD_TIME,UNLOAD_ADDRESS,SUPLR_NAME,EXEC_ORG_ID_NAME,PLATE_NO,DIRVER,MOBILE);
//		
//		
////		if(isPrivilege(SettPrivRef.SUPLRFEE_P1)){
////		
////			final Menu menu = new Menu();
////		    menu.setWidth(140);
////		    MenuItemSeparator itemSeparator =new MenuItemSeparator();
////		    
////		    table.addShowContextMenuHandler(new ShowContextMenuHandler() {
////	            public void onShowContextMenu(ShowContextMenuEvent event) {
////	            	menu.showContextMenu();
////	                event.cancel();
////	            }
////	        });
////		    
////		    table.addSelectionChangedHandler(new SelectionChangedHandler() {
////				
////				@Override
////				public void onSelectionChanged(SelectionEvent event) {				
////					load_no = event.getRecord().getAttribute("LOAD_NO");
////					odr_no = event.getRecord().getAttribute("ODR_NO");
////					shpm_no = event.getRecord().getAttribute("SHPM_NO");				
////				}
////			});
////		    
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_04)) {
////			    MenuItem allSelect2 = new MenuItem("序列号排序",StaticRef.ICON_CONFIRM);
////			    allSelect2.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
////					
////					@Override
////					public void onClick(MenuItemClickEvent event) {
////						table.discardAllEdits();
////						table.invalidateCache();
////						Criteria criteria;
////						if(searchForm != null){
////							criteria = searchForm.getValuesAsCriteria();
////							criteria.addCriteria("OP_FLAG","M");
////						}else{
////							criteria = new Criteria();
////							criteria.addCriteria("OP_FLAG","M");
////						}
////						criteria.addCriteria("ORDER_BY_NUM","Y");
////						table.fetchData(criteria,new DSCallback() {
////							
////							@Override
////							public void execute(DSResponse response, Object rawData, DSRequest request) {
////								if(pageForm != null) {
////									pageForm.getField("CUR_PAGE").setValue("1");
////									LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
////								}
////								table.setSelectOnEdit(true);
////								
////								if(table.getRecords().length > 0){
////									table.selectRecord(0);
////								}
////							}
////						});
////					}
////				});
////			    menu.addItem(allSelect2);
////		    }
////		  
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_01)) {
////		    	menu.addItem(itemSeparator);
////			    MenuItem allSelect2 = new MenuItem("全选同一作业单",StaticRef.ICON_CLEAR);
////			    allSelect2.addClickHandler(new SameChoseAction(table, "SHPM_NO"));
////			    menu.addItem(allSelect2);
////		    }
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_02)) {
////			    MenuItem allSelect = new MenuItem("全选同一车",StaticRef.ICON_CANCEL);
////			    allSelect.addClickHandler(new SameChoseAction(table, "LOAD_NO"));
////			    menu.addItem(allSelect);
////			    
////		    }
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_02)) {
////		    	
////		    	menu.addItem(itemSeparator);   
////		    	MenuItem sumInfo = new MenuItem("汇总信息",StaticRef.ICON_REFRESH);
////			    sumInfo.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
////					@Override
////					public void onClick(MenuItemClickEvent event) {
////						SupFeeCoutWin = new SupFeeCoutWin(load_no,table).getViewPanel();
////					}
////				});
////			    menu.addItem(sumInfo);
////			    
////		    }
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_04)) {
////		    	
////		    	   
////		    	MenuItem sumInfo = new MenuItem("相关订单信息",StaticRef.ICON_DEL);
////			    sumInfo.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
////					@Override
////					public void onClick(MenuItemClickEvent event) {
////						SupOrderDetailWin = new SupOrderDetailWin(odr_no,shpm_no,table).getViewPanel();
////					}
////				});
////			    menu.addItem(sumInfo);
////			    
////		    }
////		    if(isPrivilege(SettPrivRef.SUPLRFEE_P1_03)) {
////		    	menu.addItem(itemSeparator);
////			    MenuItem sett = new MenuItem("RDC费用计算",StaticRef.ICON_NEW);
////			    sett.addClickHandler(new SettlementAcctAction(table, this));
////			    menu.addItem(sett);
////			    
////		    }
////		}
//	}

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
				/*if(titName.equals("按作业单明细")){
					if (searchWin == null) {
						searchWin = new SearchWin(ds, //600 ,380
								createSearchForm(searchForm), sectionStack.getSection(0)).getViewPanel();
						searchWin.setHeight(380);
					} else {
						searchWin.show();
					}
				}else if(titName.equals("按作业单")){
					if (searchWin2 == null) {
						searchWin2 = new SearchWin(shpmDs, //600 ,380
								createSearchForm(searchForm2), sectionStack3.getSection(0)).getViewPanel();
						searchWin2.setHeight(380);
					} else {
						searchWin2.show();
					}
				}else{*/
					if (searchWin3 == null) {
						searchWin3 = new SearchWin(loadDs, //600 ,380
								createSearchForm(searchForm3), sectionStack2.getSection(0)).getViewPanel();
						searchWin3.setWidth(600);
						searchWin3.setHeight(420);
					} else {
						searchWin3.show();
					}
				//}
			}

		});

		// 保存按钮
		//saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.SUPLRFEE_P0_01);
		//saveButton.addClickHandler(new SaveAcctAction(table,this));

		// 删除按钮
		//delButton = createBtn(StaticRef.DELETE_BTN,SettPrivRef.SUPLRFEE_P0_02);
		//delButton.addClickHandler(new DeleteSuplrFeeAction(table));
		
		//confirmAcc = createUDFBtn("确认对账",StaticRef.ICON_SAVE,SettPrivRef.SUPLRFEE_P0_03);
		//confirmAcc.addClickHandler(new AccountAction(table, true,this));
		
		//cancelAcc = createUDFBtn("取消对账",StaticRef.ICON_CANCEL,SettPrivRef.SUPLRFEE_P0_04);
		//cancelAcc.addClickHandler(new AccountAction(table, false,this));
		
		confirmAudit = createUDFBtn("提交",StaticRef.ICON_SAVE,SettPrivRef.SUPLRFEE_P0_05);
		confirmAudit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] records = loadTable.getSelection();
				if(records != null && records.length > 0) {
					
					HashMap<String, String> load_no_list = new HashMap<String, String>(); // 调度单号
					for(int i = 0; i < records.length; i++) {
						/*if("已对账".equals(records[i].getAttribute("ACCOUNT_STAT_NAME"))) {
							MSGUtil.sayError("已对账状态的调度单不允许提交审批!");
							return;
						}*/
						load_no_list.put(String.valueOf(i+1),records[i].getAttribute("LOAD_NO"));
					}
					HashMap<String, Object> listMap = new HashMap<String, Object>();
					listMap.put("1", load_no_list);
					listMap.put("2",LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(listMap);
					Util.async.execProcedure(json, "SP_FEE_COMMIT(?,?,?)", new AsyncCallback<String>() {
	
						@Override
						public void onFailure(Throwable caught) {
							
						}
	
						@Override
						public void onSuccess(String result) {
							if(StaticRef.SUCCESS_CODE.equals(result.substring(0,2))){
								MSGUtil.showOperSuccess();
								loadTable.getSelectedRecord().setAttribute("AUDIT_STAT", "20");
								loadTable.getSelectedRecord().setAttribute("AUDIT_STAT_NAME", "审批中");
								loadTable.redraw();
							}else{
								MSGUtil.sayError(result.substring(2));
							}
						}
						
					});
				}
				else {
					MSGUtil.sayError("请选择调度单!");
					return;
				}
			}
		});
		
		//cancelAudit = createUDFBtn("取消审核",StaticRef.ICON_CANCEL,SettPrivRef.SUPLRFEE_P0_06);
		//cancelAudit.addClickHandler(new AuditAction(loadTable, false, this,"PAY"));
		
		//dReturn = createUDFBtn("打回",StaticRef.ICON_CANCEL,SettPrivRef.SUPLRFEE_P0_07);
		//dReturn.addClickHandler(new ReturnAction(table,this));
		
		//IButton export  = createBtn(StaticRef.EXPORT_BTN, SettPrivRef.SUPLRFEE_P0_08);
		//export.addClickHandler(new ExportAction(table));
		
		//saveDel(true, true);
		//disAccount(false,true);
		disAudit(false, true);
		//save_map.put(SettPrivRef.SUPLRFEE_P0_01, saveButton);
		
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,confirmAudit);
	}
	public DynamicForm createSearchForm(SGPanel form){	
		/**
		 * 调度单号 LOAD_NO   供应商 SUPLR_ID   车牌号 PLATE_NO   状态 从 到 STATUS   执行机构 EXEC_ORD_ID  
		 * 包含下级机构 C_ORG_FLAG  客户  CUSTOMER_ID   客户单号   CUSTOM_ODR_NO  起点区域  START_AREA
		 * 创建时间 从  到  ORD_ADDTIME_FROM    发运时间 从到    ORD_PLAN_TIME   到货时间 从到  UNLOAD_TIME_FRON
		 */
//		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
//		Util.initCustComboValue(CUSTOMER, "");
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
//		CUSTOMER_NAME.setStartRow(true);
//		CUSTOMER_NAME.setWidth(120);
//		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		CUSTOMER_NAME.setVisible(false);
	
		SGText LOAD_NO=new SGText("LOAD_NO",Util.TI18N.LOAD_NO());//调度单号

		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		  
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());//
		
		//SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());

		//2
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM", Util.TI18N.STATUS(),true);//状态 从 到 
		SGCombo STATUS_TO=new SGCombo("STATUS_TO", "到");
		Util.initStatus(STATUS_FROM, StaticRef.LOADNO_STAT, "40");
		Util.initStatus(STATUS_TO, StaticRef.LOADNO_STAT, "");
		
		//二级窗口 SUPLR_ID_NAME
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO",Util.TI18N.PLATE_NO());//

		//3
		ComboBoxItem START_AREA=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());//起点区域
		START_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem START_AREA_ID=new TextItem("START_AREA_ID", Util.TI18N.START_ARAE());
		START_AREA_ID.setVisible(false);
		Util.initArea(START_AREA, START_AREA_ID);
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());//
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		END_AREA_ID.setVisible(false);
		Util.initArea(END_AREA, END_AREA_ID);
		
		//SGCombo ACCOUNT_STAT =new SGCombo("ACCOUNT_STAT", Util.TI18N.ACCOUNT_STAT());//
		//Util.initCodesComboValue(ACCOUNT_STAT, "ACCOUNT_STAT", true);
		
		SGCombo AUDIT_STAT =new SGCombo("AUDIT_STAT",Util.TI18N.AUDIT_STAT());//
		Util.initComboValue(AUDIT_STAT,"BAS_CODES","CODE", "NAME_C"," PROP_CODE = 'APPROVE_STS'"," order by show_seq asc","");

		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		ORD_ADDTIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		ORD_ADDTIME_TO.setWidth(FormUtil.Width);
		//4
		
		SGCombo DISPATCH_STAT= new SGCombo("DISPATCH_STAT",Util.TI18N.DISPATCH_STAT_NAME());
		Util.initCodesComboValue(DISPATCH_STAT, "DISPATCH_STAT");//--wangjun 2010-2-27
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME()); 
		
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());// 包含下级机构	
//		C_ORG_FLAG.setWidth(120);
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME", "", " order by show_seq asc");
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());
		TRANS_SRVC_ID.setTitle(Util.TI18N.TRANS_SRVC_ID());
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		//发运时间 从END_LOAD_TIME
		//SGDate END_LOAD_TIME_FROM = new SGDate("END_LOAD_TIME_FROM", Util.TI18N.END_LOAD_TIME()+" 从");
		//SGDate END_LOAD_TIME_TO = new SGDate("END_LOAD_TIME_TO", "到");
		
		//SGDate ACCOUNT_TIME_FROM = new SGDate("ACCOUNT_TIME_FROM", Util.TI18N.ACCOUNT_TIME() + "从");//
		//SGDate ACCOUNT_TIME_TO = new SGDate("ACCOUNT_TIME_TO", "到");
		
		SGDate AUDIT_TIME_FROM = new SGDate("AUDIT_TIME_FROM", Util.TI18N.AUDIT_TIME() + "从");//
		SGDate AUDIT_TIME_TO = new SGDate("AUDIT_TIME_TO", "到");
		
		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,AUDIT_STAT,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,START_AREA_ID,
				START_AREA,END_AREA_ID,END_AREA,
				DISPATCH_STAT,ROUTE_ID,TRANS_SRVC_ID,
				EXEC_ORG_ID,EXEC_ORG_ID_NAME,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,
				AUDIT_TIME_FROM,AUDIT_TIME_TO,C_ORG_FLAG);
		return form;
	}
	@Override
	public void createForm(DynamicForm form) {

	}
	@Override
	public void initVerify() {

	}
	
	/**
	 * 按调度结算页签列表
	 * @author fangliangmeng
	 */
	private void createSettAsLoad(){
		loadTable = new SGTable(loadDs,"100%", "100%", false, false, false);
		loadTable.setShowRowNumbers(true);
		loadTable.setShowFilterEditor(false);
		loadTable.setCanEdit(false);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 110);//调度单
		ListGridField STATUS = new ListGridField("STATUS_NAME", Util.TI18N.LOAD_STAT(), 60);//状态
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", "承运商", 100);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 60);
		ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID_NAME", "车型", 70);
		ListGridField DRIVER1 = new ListGridField("DRIVER1", "司机", 60);
		//ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT_NAME", Util.TI18N.ACCOUNT_STAT(), 100);//对账状态
		ListGridField AUDIT_STAT = new ListGridField("AUDIT_STAT_NAME", Util.TI18N.AUDIT_STATUS(), 70);//审核状态
		ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT_NAME","对账状态",70);
		ListGridField START_AREA_NAME = new ListGridField("START_AREA_NAME","起点城市", 70);
		START_AREA_NAME.setHidden(true);	
		ListGridField END_AREA_NAME = new ListGridField("END_AREA_NAME", "终点城市", 70);
		END_AREA_NAME.setHidden(true);	
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W","吨位", 60);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL", "体积", 60);
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","数量", 60);
		ListGridField AUDIT_NOTES = new ListGridField("AUDIT_NOTES","审核备注", 120);
		ListGridField TOT_AMOUNT = new ListGridField("TOT_AMOUNT","总成本", 60);
		ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额", 80);
		ListGridField UDF2 = new ListGridField("UDF2","运输类型", 65);
		UDF2.setShowHover(true);
		ListGridField UDF3 = new ListGridField("UDF3","起止地", 130);
		UDF3.setShowHover(true);
		loadTable.setFields(LOAD_NO,STATUS,SUPLR_NAME,PLATE_NO,VEHICLE_TYP_ID,START_AREA_NAME,END_AREA_NAME,DRIVER1,TOT_GROSS_W,TOT_VOL,TOT_QNTY,AUDIT_STAT,AUDIT_NOTES,ACCOUNT_STAT,TOT_AMOUNT,ADJ_AMOUNT,UDF2,UDF3);
		loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Criteria criteria = new Criteria();
	        	criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("LOAD_NO", event.getRecord().getAttribute("LOAD_NO"));
	        	//shpmTable.fetchData(criteria);
	        	selectLoad = event.getRecord();
	        	vm.clearValues();
	        	//damageTable.fetchData(criteria);
				
				feeTable.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						feeTable.selectRecord(0);
					}
				});
				
	        	Util.initComboValue(SHPM_NO, "trans_shipment_header", "shpm_no", "shpm_no", " where load_no='" + event.getRecord().getAttribute("LOAD_NO")+"'");
	        	SHPM_NO.setDisabled(true);
	        	
	        	Record rec = event.getRecord();
				if(rec.getAttribute("ACCOUNT_STAT_NAME").equals("已对账")) {
					initLoadFeeBtn(5);
				}else {
					initLoadFeeBtn(1);
				}
			}
			
		});
		
		loadTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				
				Record rec = event.getRecord();
				if(rec.getAttribute("ACCOUNT_STAT_NAME").equals("已对账")) {
					initLoadFeeBtn(5);
				}else {
					initLoadFeeBtn(1);
				}
			}
		});
		
		loadTable.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(isMax) {
					expend();
				}
			}
			
		});
		
	}
	
	/**
	 * 调度单页签，左边导航
	 * @author fangliangmeng
	 * @return
	 */
	private HStack createSettAsLoadPage(){
		// 主布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		vm = new ValuesManager();
		
		sectionStack2 = new SectionStack();
		listItem2 = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem2.setItems(loadTable);
		listItem2.setExpanded(true);
		sectionStack2.addSection(listItem2);
		pageForm2 = new SGPage(loadTable, true).initPageBtn();
		listItem2.setControls(pageForm2);
		sectionStack2.setWidth("100%");
		
		stack.addMember(sectionStack2);
		//isMax = true;
		addSplitBar(stack,"25%");
				
		
		VLayout layOut = new VLayout();
		layOut.setWidth("75%");
		layOut.setHeight("100%");
		layOut.setVisible(false);
		
//		TabSet taSet = new TabSet();
//		taSet.setWidth100();
//		taSet.setHeight("20%");
//		taSet.setMargin(1);
//		
//		Tab tab = new Tab("作业单信息");
//		tab.setPane(createShpmTable());
//		taSet.addTab(tab);
		
	//	layOut.addMember(taSet);
		
		TabSet taSet2 = new TabSet();
		taSet2.setWidth100();
		taSet2.setHeight("20%");
		taSet2.setMargin(1);
		
		Tab tab3 = new Tab("费用信息");
		tab3.setPane(createfeeTable());
		
//		Tab lossTab = new Tab("货损货差");
//		lossTab.setPane(createLossDamageInfo());
		
		taSet2.addTab(tab3);
//	    taSet2.addTab(lossTab);
		
		layOut.addMember(taSet2);
		createFeeInfo();
		vm.addMember(feeInfo);
		layOut.addMember(feeInfo);
		layOut.addMember(createFeeBtn());
		
		stack.addMember(layOut);
		
		return stack;
	}
	
//	private SectionStack createShpmPage(){
//		
//		createShpmHeaderTable();
//		
//		sectionStack3 = new SectionStack();
//		listItem3 = new SectionStackSection(Util.TI18N.LISTINFO());
//		listItem3.setItems(headTable);
//		listItem3.setExpanded(true);
//		sectionStack3.addSection(listItem3);
//		pageForm3 = new SGPage(headTable, true).initPageBtn();
//		listItem3.setControls(pageForm3);
//		sectionStack3.setWidth("100%");
//		
//		return sectionStack3;
//	}
	
	/**
	 * 调度单页签  --- 右边作业单列表
	 * @author fangliangmeng
	 * @return
	 */
//	private SGTable createShpmTable(){
//		
//		shpmTable = new SGTable(shpmMainDS, "100%", "92%", false, true, false) {
//			
//			@Override
//			protected Canvas getExpansionComponent(ListGridRecord record) {
//				VLayout layout = new VLayout();
//				SGTable detailTable = new SGTable(detailDS, "100%",
//						"50", false, true, false);
//				detailTable.setCanEdit(false);
//				
//				detailTable.setAlign(Alignment.RIGHT);
//				detailTable.setShowRowNumbers(false);
//				detailTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);
//				detailTable.setAutoFitData(Autofit.VERTICAL);
//
//				Criteria findValues = new Criteria();
//				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//				findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
//				ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),50);
//				ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),60);
//				ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),60);
//				ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),60);
//				ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),30);
//				ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),50);
//				Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
//				
//				ListGridField ODR_QNTY = new ListGridField("ODR_QNTY",Util.TI18N.ODR_QNTY(),60);
//				Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
//				
//				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
//				Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
//				
//				ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
//				Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
//				
//				ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),50);
//				Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
//				
//				ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
//				Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
//				
//				ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),50);
//				TOT_QNTY_EACH.setAlign(Alignment.RIGHT); 
//				Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
//				
//				ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),90);
//				
//				ListGridField LOT_ID =new ListGridField("LOT_ID",Util.TI18N.LOT_ID(),90);
//				
//				detailTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,QNTY,TOT_QNTY_EACH,LD_QNTY,UNLD_QNTY,G_WGT,VOL,LOTATT02,LOT_ID);
//				detailTable.fetchData(findValues);
//                layout.addMember(detailTable);
//                layout.setLayoutLeftMargin(38);
//				return layout;
//			}
//		};
//		shpmTable.setCanExpandRecords(true);
//		shpmTable.setShowFilterEditor(false);
//		shpmTable.setCanEdit(false);
//		
//		getTableList(shpmTable,false);
//		
//		return shpmTable;
//	}
	
	private ListGrid createfeeTable(){
		feeTable = new SGTable(billLoadDS){
			@Override
			protected Canvas getExpansionComponent(final ListGridRecord record) {
				VLayout layout = new VLayout();
				SGTable detailTable = new SGTable(billShpmDS, "99%",
						"50", false, true, false);
				detailTable.setCanEdit(false);
				
				detailTable.setAlign(Alignment.RIGHT);
				detailTable.setShowRowNumbers(false);
				detailTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);
				detailTable.setAutoFitData(Autofit.VERTICAL);
	
				ListGridField SHPM_ROW = new ListGridField("DOC_NO",Util.TI18N.SHPM_NO(),130);
				ListGridField SKU_ID = new ListGridField("FEE_NAME",Util.TI18N.FEE_NAME(),60);
				ListGridField SKU_NAME = new ListGridField("FEE_BAS_NAME",Util.TI18N.FEE_BASE(),60);
				ListGridField SKU_SPEC = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(),60);
				Util.initFloatListField(SKU_SPEC, StaticRef.GWT_FLOAT);
				ListGridField UOM = new ListGridField("PRICE",Util.TI18N.PRICE(),70);
				Util.initFloatListField(UOM, StaticRef.PRICE_FLOAT);
				ListGridField QNTY = new ListGridField("DUE_FEE","应付金额",80);
				Util.initFloatListField(QNTY, StaticRef.PRICE_FLOAT);
				
				ListGridField ODR_QNTY = new ListGridField("PRE_FEE",Util.TI18N.PRE_FEE(),80);
				Util.initFloatListField(ODR_QNTY, StaticRef.PRICE_FLOAT);
				
				ListGridField VOL = new ListGridField("PAY_FEE","实付费用",80);
				Util.initFloatListField(VOL, StaticRef.PRICE_FLOAT);
							
				detailTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,QNTY,VOL);
				
				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("LOAD_NO", record.getAttributeAsString("LOAD_NO"));
				findValues.addCriteria("FEE_NAME",record.getAttribute("FEE_NAME"));
				
				detailTable.fetchData(findValues);
				
	            layout.addMember(detailTable);
	            layout.setLayoutLeftMargin(38);
	            detailTable.addRecordClickHandler(new RecordClickHandler() {
	    			
	    			@Override
	    			public void onRecordClick(RecordClickEvent event) {
	    				vm.editRecord(event.getRecord());
	    				initLoadFeeBtn(4);
	    			}
	    		});
	            detailTable.addDoubleClickHandler(new DoubleClickHandler() {
					
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						
						initLoadFeeBtn(2);

					}
				});
				return layout;
			}
		};
		feeTable.setCanExpandRecords(true);
		feeTable.setShowFilterEditor(false);
		feeTable.setShowAllRecords(true);
		feeTable.setAutoFetchData(false);
		feeTable.setCanEdit(false);
		//feeTable.setShowGroupSummary(true); 
		//feeTable.setShowGridSummary(true); 
		
		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.DOC_NO(), 120);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 120);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 80);
		//ListGridField PAY_STS = new ListGridField("PAY_STAT_NAME", Util.TI18N.SETT_VERIFI_STAT(), 80);
		ListGridField BAS_VAL = new ListGridField("BAS_VALUE", Util.TI18N.BAS_VALUE(), 65);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 65);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 65);
		PRE_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		PRE_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		PRE_FEE.setShowGridSummary(true); 
		PRE_FEE.setAlign(Alignment.RIGHT);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE", "应付金额", 65);
		DUE_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		DUE_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		DUE_FEE.setShowGridSummary(true); 
		DUE_FEE.setAlign(Alignment.RIGHT);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实付金额", 80);
		PAY_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		PAY_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		PAY_FEE.setShowGridSummary(true); 
		PAY_FEE.setAlign(Alignment.RIGHT);
		ListGridField NOTES = new ListGridField("NOTES","备注", 160);
		
		feeTable.setFields(DOC_NO,LOAD_NO,FEE_NAME,BAS_VAL,PRICE,PRE_FEE,DUE_FEE,PAY_FEE,NOTES);
		
		
		final Menu menu = new Menu();
	    menu.setWidth(140);
	    
	    MenuItem pay = new MenuItem("核销",StaticRef.ICON_CONFIRM);
	    pay.addClickHandler(new PayAction(feeTable, true, this));
	    MenuItem cPay = new MenuItem("取消核销",StaticRef.ICON_CONFIRM);
	    cPay.addClickHandler(new PayAction(feeTable, false, this));
	    
	    menu.addItem(pay);
	    menu.addItem(cPay);
	    
	    
	    
	    feeTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
	    
	    feeTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(feeTable.getSelectedRecord()==null)return;
				vm.editRecord(event.getRecord());
				if(feeTable.getSelectedRecord().getAttribute("INIT_FLAG").equals("N")){
					initLoadFeeBtn(4);
				}else{
					initLoadFeeBtn(1);
				}
				SHPM_NO.setDisabled(true);
			}
		});
	    
		feeTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				vm.editRecord(event.getRecord());
				if(feeTable.getSelectedRecord().getAttribute("INIT_FLAG").equals("N")){
					initLoadFeeBtn(4);
				}else{
					initLoadFeeBtn(1);
				}
//				String aud_status = loadTable.getSelectedRecord().getAttributeAsString("AUDIT_STAT_NAME");
//				if("待审批".equals(aud_status)){
//					initLoadFeeBtn(4);
//					disAudit(false, true);
//				}else{
//					initLoadFeeBtn(5);
//					disAudit(true, false);
//				}
				SHPM_NO.setDisabled(true);
//				initAccBtn(event.getRecord());
			}
		});
		
		feeTable.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(feeTable.getSelectedRecord().getAttribute("INIT_FLAG").equals("N")){
					initLoadFeeBtn(2);
				}else{
					initLoadFeeBtn(1);
				}
//				String aud_status = loadTable.getSelectedRecord().getAttributeAsString("AUDIT_STAT_NAME");
//				if("待审批".equals(aud_status)){
//					initLoadFeeBtn(2);
//					disAudit(false, true);
//				}else{
//					initLoadFeeBtn(5);
//					disAudit(true, false);
//				}
				SHPM_NO.setDisabled(true);
			}
		});
		
		return feeTable;
	}
	
	private SGPanel createFeeInfo(){
		/**
		 * 基本信息
		 * 
		 */
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		// 1：订单编号，客户，下单时间，运输服务,客户单号,运输方式
		FEE_ID = new SGCombo("FEE_ID", Util.TI18N.FEE_NAME());
		FEE_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_NAME()));
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " where FEE_ATTR = 'E43845662CE04B80995D3AE8FB41D11F'");
		
		SETT_ID = new SGCombo("SETT_ID", Util.TI18N.SETT_NAME());
		SETT_ID.setVisible(false);
		
		SHPM_NO = new SGCombo("DOC_NO", Util.TI18N.DOC_NO());
		
//		SGCombo DOC_TYPE = new SGCombo("DOC_TYPE", Util.TI18N.DOC_TYPE());
		
//		Util.initCodesComboValue(DOC_TYPE, "BAS_CODES", "CODE", "NAME_C", "DOC_TYP", " order by show_seq asc","");

		final SGCombo FEE_BAS = new SGCombo("FEE_BAS", Util.TI18N.FEE_BASE());
		FEE_BAS.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_BASE()));
		Util.initCodesComboValue(FEE_BAS, "FEE_BASE");

		final SGText BAS_VALUE = new SGText("BAS_VALUE", Util.TI18N.BAS_VALUE());
//		Util.initFloatTextItem(BAS_VALUE, StaticRef.GWT_FLOAT);

		final SGText PRICE=new SGText("PRICE", Util.TI18N.PRICE());
//		Util.initFloatTextItem(PRICE, StaticRef.PRICE_FLOAT);
		
//		SGText CONT_PRICE = new SGText("CONT_PRICE", Util.TI18N.SETT_CONT_PRICE());
//		CONT_PRICE.setDisabled(true);
		
		final SGText DUE_FEE = new SGText("DUE_FEE","应付金额");
//		Util.initFloatTextItem(DUE_FEE, StaticRef.PRICE_FLOAT);
		
		PRE_FEE = new SGText("PRE_FEE",Util.TI18N.PRE_FEE());
//		Util.initFloatTextItem(PRE_FEE, StaticRef.PRICE_FLOAT);
		PRE_FEE.setDisabled(true);
		
		PAY_FEE = new SGText("PAY_FEE", "实付金额");
//		Util.initFloatTextItem(PAY_FEE, StaticRef.PRICE_FLOAT);
		
		
		// 2：订单类型,执行机构，业务员
		SGDateTime PRE_PAY_TIME = new SGDateTime("PRE_PAY_TIME", Util.TI18N.SETT_VERIFI_PRE_TIME());
		PRE_PAY_TIME.setWidth(FormUtil.Width);

		SGText ACT_PAY_TIME = new SGText("ACT_PAY_TIME", Util.TI18N.SETT_VERIFI_TIME());
		ACT_PAY_TIME.setDisabled(true);
		
		SGText PAYEE = new SGText("PAYEE",Util.TI18N.SETT_VERIFICATER());
		PAYEE.setDisabled(true);
		
		SGText NOTES = new SGText("NOTES", Util.TI18N.NOTES());
		NOTES.setColSpan(8);
		NOTES.setWidth(FormUtil.longWidth*2);
		
		PRICE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(PRICE.getValue()==null){
					DUE_FEE.setValue(Double.parseDouble(ObjUtil.ifNull(BAS_VALUE.getValue().toString(),"0")) * Double.parseDouble("0"));
				}else{
					DUE_FEE.setValue(Double.parseDouble(ObjUtil.ifNull(BAS_VALUE.getValue().toString(),"0")) * Double.parseDouble(ObjUtil.ifNull(PRICE.getValue().toString(),"0")));
				}
			}
		});
		
		DUE_FEE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(DUE_FEE.getValue()==null){
					PRICE.setValue("0");
				}else{
					PRICE.setValue(Double.parseDouble(DUE_FEE.getValue().toString()) / Double.parseDouble(BAS_VALUE.getValue().toString()));
				}
			}
		});
		
		SHPM_NO.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				initBasVal();
			}
		});
		
		FEE_BAS.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				initBasVal();
			}
		});
			
		feeInfo = new SGPanel();
		feeInfo.setTitleWidth(75);
		feeInfo.setItems(FEE_ID,SHPM_NO,SETT_ID,FEE_BAS,BAS_VALUE,PRICE,DUE_FEE,PRE_FEE,PAY_FEE,
				PRE_PAY_TIME,ACT_PAY_TIME,PAYEE,NOTES);
		feeInfo.setWidth("40%");
		return feeInfo;
	}
	
	private ToolStrip createFeeBtn(){
		IButton newBtn = createBtn(StaticRef.CREATE_BTN,SettPrivRef.SUPLRFEE_P2_01);
        //newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(80);
        newBtn.setAutoFit(false);
        
        newBtn.addClickHandler(new NewMultiFormAction(vm, null));
        newBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(selectLoad != null){
					initLoadFeeBtn(2);
					FEE_ID.setDisabled(false);
					SHPM_NO.setDisabled(false);
					SETT_ID.setValue(loadTable.getSelectedRecord().getAttributeAsString("SUPLR_NAME"));
					PRE_FEE.setValue(0);
					PAY_FEE.setValue(0);
					SGCombo FEE_ID = (SGCombo)feeInfo.getItem("FEE_ID");
					if(ObjUtil.isNotNull(loadTable.getSelectedRecord().getAttribute("INIT_FLAG"))){
						if(loadTable.getSelectedRecord().getAttribute("INIT_FLAG").toString().equals("N")){
							Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " where FEE_ATTR = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_CODE not in ('1200','1300')");
						}else{
							Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " where FEE_ATTR = 'E43845662CE04B80995D3AE8FB41D11F' and FEE_CODE in ('1200','1300')");
						}
					}
				}else{
					MSGUtil.sayError("请先选择调度单，再执行新增操作！");
				}
			}
		});
        
		IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SettPrivRef.SUPLRFEE_P2_02);
		//saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(80);
		saveBtn.setAutoFit(false);
		saveBtn.addClickHandler(new SavePayFeeAction(this,vm));
//		saveBtn.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				initLoadFeeBtn(4);
//			}
//		});
		
        IButton delBtn = createBtn(StaticRef.DELETE_BTN,SettPrivRef.SUPLRFEE_P2_03);
        //delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(80);
		delBtn.setAutoFit(false);
		delBtn.addClickHandler(new DeletePayFeeAction(this, vm));
		
		IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.SUPLRFEE_P2_04);
		//cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(80);
		cancelBtn.setAutoFit(false);
		cancelBtn.addClickHandler(new CancelMultiFormAction(feeTable, vm));
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initLoadFeeBtn(1);
			}
		});
		
		ins_fee_btn = new HashMap<String, IButton>();
		sav_fee_btn = new HashMap<String, IButton>();
		del_fee_btn = new HashMap<String, IButton>();
		
		ins_fee_btn.put(SettPrivRef.SUPLRFEE_P2_01, newBtn);
        del_fee_btn.put(SettPrivRef.SUPLRFEE_P2_03, delBtn);
        sav_fee_btn.put(SettPrivRef.SUPLRFEE_P2_02, saveBtn);
        sav_fee_btn.put(SettPrivRef.SUPLRFEE_P2_04, cancelBtn);
        
        initLoadFeeBtn(1);
        
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(newBtn,saveBtn,delBtn,cancelBtn); 
	    
	    return toolStrip;
	}
	
//	private SGTable createShpmHeaderTable(){
//			headTable = new SGTable(shpmDs, "100%", "92%", false, true, false) {
//				
//				@Override
//				protected Canvas getExpansionComponent(ListGridRecord record) {
//					VLayout layout = new VLayout();
//					SGTable detailTable = new SGTable(detailDS, "100%",
//							"50", false, true, false);
//					detailTable.setCanEdit(true);
//					
//					detailTable.setAlign(Alignment.RIGHT);
//					detailTable.setShowRowNumbers(false);
//					detailTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);
//					detailTable.setAutoFitData(Autofit.VERTICAL);
//
//					Criteria findValues = new Criteria();
//					findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//					findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
//					ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),50);
//					SHPM_ROW.setCanEdit(false);
//					ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),60);
//					SKU_ID.setCanEdit(false);
//					ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),60);
//					SKU_NAME.setCanEdit(false);
//					ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),60);
//					SKU_SPEC.setCanEdit(false);
//					ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),30);
//					UOM.setCanEdit(false);
//					ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),50);
//					Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
//					
//					ListGridField ODR_QNTY = new ListGridField("ODR_QNTY",Util.TI18N.ODR_QNTY(),60);
//					ODR_QNTY.setCanEdit(false);
//					Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
//					
//					ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
//					LD_QNTY.setCanEdit(false);
//					Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
//					
//					ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
//					Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
//					UNLD_QNTY.setCanEdit(false);
//					
//					ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),50);
//					Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
//					G_WGT.setCanEdit(false);
//					
//					ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
//					Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
//					VOL.setCanEdit(false);
//					
//					ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),50);
//					TOT_QNTY_EACH.setAlign(Alignment.RIGHT); 
//					TOT_QNTY_EACH.setCanEdit(false);
//					Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
//					
//					ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),90);
//					LOTATT02.setCanEdit(false);
//					
//					ListGridField LOT_ID =new ListGridField("LOT_ID",Util.TI18N.LOT_ID(),90);
//					LOT_ID.setCanEdit(false);
//					
//					detailTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,QNTY,TOT_QNTY_EACH,LD_QNTY,UNLD_QNTY,G_WGT,VOL,LOTATT02,LOT_ID);
//					detailTable.fetchData(findValues);
//	                layout.addMember(detailTable);
//	                layout.setLayoutLeftMargin(38);
//					return layout;
//				}
//			};
//			headTable.setCanExpandRecords(true);
//			headTable.setCanEdit(false);
//			headTable.setShowRowNumbers(true);
//			headTable.setShowGridSummary(true);
//			
//			getTableList(headTable,true);
//			final Menu menu = new Menu();
//		    menu.setWidth(140);
////		    MenuItemSeparator itemSeparator =new MenuItemSeparator();
//		    
//		    MenuItem feeInsert = new MenuItem("费用信息",StaticRef.ICON_NEW);
//		    feeInsert.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//				
//				@Override
//				public void onClick(MenuItemClickEvent event) {
////					if(shpmFeeWin == null)
////						shpmFeeWin = 
//							new ShpmFeeWin(headTable,getThis()).getViewPanel();
////					else
////						shpmFeeWin.show();
//				}
//			});
//	    	menu.addItem(feeInsert);
//	    	
//	        headTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
//		            public void onShowContextMenu(ShowContextMenuEvent event) {
//		            	menu.showContextMenu();
//		                event.cancel();
//		            }
//		        });
//	    	headTable.addRecordClickHandler(new RecordClickHandler() {
//				
//				@Override
//				public void onRecordClick(RecordClickEvent event) {
//					initAccBtn(event.getRecord());
//				}
//			});
//			return headTable;
//	}
	
//	private void getTableList(SGTable table,boolean isRDC){
//		
//		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 80);
//		Util.initFloatListField(PRICE, StaticRef.PRICE_FLOAT);
//		
//		ListGridField SHPM_NO = new ListGridField("SHPM_NO", Util.TI18N.SHPM_NO(), 120);
//		ListGridField STATUS = new ListGridField("STATUS_NAME", Util.TI18N.STATUS(), 60);
//		ListGridField REFENENCE1 = new ListGridField("REFENENCE1", Util.TI18N.REFENENCE1(), 100);
//		ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2", "发货城市", 75);
//		ListGridField UNLOAD_AREA_NAME2 = new ListGridField("UNLOAD_AREA_NAME2", "收货城市", 75);
//		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 120);
//		
//		ListGridField ODR_QNTY = new ListGridField("TOT_QNTY", Util.TI18N.QNTY(), 70);
//		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
//		
//		ListGridField TOT_VOL = new ListGridField("TOT_VOL", Util.TI18N.TOT_VOL(), 70);
//		Util.initFloatListField(TOT_VOL, StaticRef.QNTY_FLOAT);
//		
//		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W", Util.TI18N.TOT_GROSS_W(), 70);
//		Util.initFloatListField(TOT_GROSS_W, StaticRef.QNTY_FLOAT);
//		
//		ListGridField BIZ_TYP_NAME = new ListGridField("BIZ_TYP_NAME", Util.TI18N.BIZ_TYP(), 70);
//		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME", Util.TI18N.TRANS_SRVC_ID(), 70);
//		ListGridField REFENENCE4_NAME = new ListGridField("REFENENCE4_NAME", Util.TI18N.TEMPERATURE(), 70);
//		
//		table.setFields(SHPM_NO,STATUS,REFENENCE1,BIZ_TYP_NAME, TRANS_SRVC_ID_NAME, REFENENCE4_NAME, LOAD_AREA_NAME2,UNLOAD_NAME,UNLOAD_AREA_NAME2,ODR_QNTY,TOT_VOL,TOT_GROSS_W);
//	}
	
//	private SGTable createLossDamageInfo() {	
//		damageTable = new SGTable(lossDamageDS,"100%","100%");
//		damageTable.addRecordClickHandler(new RecordClickHandler() {
//			@Override
//			public void onRecordClick(RecordClickEvent event) {
//				itemRow = event.getRecordNum();
//				initCancelBtn();
//			}
//		});
//		damageTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//			@Override
//			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
//				initAddBtn();
//			}
//		});
//		damageTable.setShowFilterEditor(false);
//		damageTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
//		damageTable.setShowRowNumbers(true);
//		damageTable.setDataSource(lossDamageDS);
//
//		ListGridField ODR_NO = new ListGridField("ODR_NO", "",0);//货品代码
//		ODR_NO.setHidden(true);
//		ListGridField SKU_ID = new ListGridField("SKU_ID",Util.TI18N.SKU(),70);//货品代码
//		SKU_ID.setHidden(true);
//		
//		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);//货品名称
//		SKU_NAME.setCanEdit(true);
//		SKU_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()));
//		
//		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),80);//货品规格型号
//		SKU_SPEC.setCanEdit(true);
//		
//		final ListGridField DAMA_TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.UOM(),60);
//		DAMA_TRANS_UOM.setCanEdit(true); //DAMA_TRANS_UOM
//		DAMA_TRANS_UOM.setTitle(ColorUtil.getRedTitle(Util.TI18N.UOM()));
//		
//		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK_ID(),80);
//		PACK_ID.setCanEdit(true); //包装
//		PACK_ID.setAlign(Alignment.LEFT);  
//		
//		final ListGridField TRANS_QNTY = new ListGridField("QNTY",Util.TI18N.TRANS_QNTY(),70);
//		TRANS_QNTY.setCanEdit(true);
//		TRANS_QNTY.setAlign(Alignment.RIGHT);//货损数量 
////		TRANS_QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRANS_QNTY()));
//		Util.initFloatListField(TRANS_QNTY, StaticRef.QNTY_FLOAT);
//		
//		final ListGridField LOSS_DAMAGE_TYP = new ListGridField("LOSS_DAMAGE_TYP",Util.TI18N.LOSS_DAMAGE_TYP(),70);
//		LOSS_DAMAGE_TYP.setCanEdit(true);
//		Util.initCodesComboValue(LOSS_DAMAGE_TYP, "LOSS_DAMAGE_TYP");	
//		
//		final ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON(),250);
//		REASON.setCanEdit(true);
//		REASON.setAlign(Alignment.LEFT);//  原因描述
//		
//		final ListGridField AMOUNT = new ListGridField("AMOUNT",Util.TI18N.TRANS_AMOUNT(),80);
//		AMOUNT.setCanEdit(true);
//		AMOUNT.setAlign(Alignment.RIGHT);//  残损金额  
//		Util.initFloatListField(AMOUNT, StaticRef.PRICE_FLOAT);
//		
//		final ListGridField DUTYER = new ListGridField("DUTYER",Util.TI18N.DUTYER(),75);
//		DUTYER.setCanEdit(true);
//		DUTYER.setAlign(Alignment.LEFT);//  责任人
//		
//		final ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),70);
//		SHPM_NO.setHidden(true);
//		final ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),70);
//		LOAD_NO.setHidden(true);
//		final ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),50);
//		CUSTOMER_NAME.setHidden(true);
//		
//		PACK_ID.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
//			
//			@Override
//			public void onChanged(
//					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
//				final int row = event.getRowNum();
//				String pack_id = ObjUtil.ifObjNull(event.getValue(),"").toString();
//				if(ObjUtil.isNotNull(pack_id)) {
//					pack_id =" where id='" +pack_id +"'";
//					DAMA_TRANS_UOM.setValueMap("");
//					Util.async.getComboValue("V_BAS_PACKAGE", "UOM", "DESCR", pack_id, "", new AsyncCallback<LinkedHashMap<String, String>>() {
//						
//						public void onFailure(Throwable caught) {	
//							;
//						}
//						public void onSuccess(LinkedHashMap<String, String> result) {
//							if(result != null && result.size() > 0) {
//								Object[] obj = result.keySet().toArray();
//								DAMA_TRANS_UOM.setValueMap(result);
//								damageTable.setEditValue(row,"TRANS_UOM", ObjUtil.ifObjNull(result.get(obj[1]), "").toString());
//							}
//						}					
//					});
//				}
//			}
//		});
//		
//		
//		damageTable.setFields(ODR_NO, SKU_ID,SKU_NAME, SKU_SPEC,PACK_ID,DAMA_TRANS_UOM, TRANS_QNTY,
//				LOSS_DAMAGE_TYP, AMOUNT, DUTYER,REASON,SHPM_NO,LOAD_NO);
//		
//
//		FormItemIcon icon1 = new FormItemIcon();
//		SKU_NAME.setIcons(icon1);
//		SKU_NAME.setShowSelectedIcon(true);
//		icon1.addFormItemClickHandler(new FormItemClickHandler() {
//			
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//					new SkuWin(damageTable,itemRow,"40%", "38%","").getViewPanel();
//			}
//		});
//		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
////		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where 1=1", "");
//		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");
//		
//		return damageTable;
//	
//	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	public void initBasVal(){
		//final SGText val = basVal;
		if("VEHICLE".equals(vm.getItem("FEE_BAS").getValue())) {
			vm.setValue("BAS_VALUE", "1");
		}
		else {
			Util.async.getBasVal(selectLoad.getAttribute("LOAD_NO"),ObjUtil.ifObjNull(vm.getItem("DOC_NO").getValue(),"").toString(), vm.getItem("FEE_BAS").getDisplayValue(), new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					;
				}
	
				@Override
				public void onSuccess(String result) {
					vm.setValue("BAS_VALUE", result);
				}
				
			});
		}
	}
	
	/**
	 * 页签 费用按钮状态变化
	 * @author fangliangmeng
	 * @param typ
	 */
	public void initLoadFeeBtn(int typ){
		if(feeTable.getSelectedRecord() != null && feeTable.getSelectedRecord().getAttribute("ACCOUNT_STAT_NAME").equals("已对账")){
			enableOrDisables(ins_fee_btn, false);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, false);
		}else{
			if(typ == 1){
				enableOrDisables(ins_fee_btn, true);
				enableOrDisables(sav_fee_btn, false);
				enableOrDisables(del_fee_btn, false);
				FEE_ID.setDisabled(true);
			}else if(typ == 2){
				enableOrDisables(ins_fee_btn, false);
				enableOrDisables(sav_fee_btn, true);
				enableOrDisables(del_fee_btn, false);
			}else if(typ == 3){
				enableOrDisables(ins_fee_btn, false);
				enableOrDisables(sav_fee_btn, false);
				enableOrDisables(del_fee_btn, true);
			}else if(typ == 4){
				enableOrDisables(ins_fee_btn, true);
				enableOrDisables(sav_fee_btn, false);
				enableOrDisables(del_fee_btn, true);
			}else if(typ == 5){
				enableOrDisables(ins_fee_btn, false);
				enableOrDisables(sav_fee_btn, false);
				enableOrDisables(del_fee_btn, false);
			}
		}
	}
	

	/*public void saveDel(boolean b1,boolean b2){
		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_01)){
			saveButton.setDisabled(b1);
		}
		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_02)){
			delButton.setDisabled(b2);
		}
	}*/
	

	public void disAudit(boolean b1,boolean b2){
		
//		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_05)){
//			confirmAudit.setDisabled(b1);
//		}
//		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_06)){
//			cancelAudit.setDisabled(b2);
//		}
		/*if(isPrivilege(SettPrivRef.SUPLRFEE_P0_07)){
			dReturn.setDisabled(b1);
		}*/
	}
	
	public void disAccount(boolean b1,boolean b2){
		
//		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_03)){
//			confirmAcc.setDisabled(b1);
//		}
//		if(isPrivilege(SettPrivRef.SUPLRFEE_P0_04)){
//			cancelAcc.setDisabled(b2);
//		}
	}
	
	
	public void initAccBtn(Record record){
//		String acc_status = record.getAttributeAsString("ACCOUNT_STAT_NAME");
		String aud_status = record.getAttributeAsString("AUDIT_STAT_NAME");
		String status_name = record.getAttributeAsString("STATUS_NAME");
		
		if("已回单".equals(status_name)){
			
//			if("未对账".equals(acc_status) || "已打回".equals(acc_status)){
//				//disAccount(false,true);
//				disAudit(true, true);
//				/*if(isPrivilege(SettPrivRef.SUPLRFEE_P0_02)){
//					delButton.setDisabled(false);
//				}*/
//			}else{
				if("待审批".equals(aud_status)){
					//disAccount(true,false);
					disAudit(false, true);
					
				}else{
					//disAccount(true,true);
					disAudit(true, false);
				}
				/*if(isPrivilege(SettPrivRef.SUPLRFEE_P0_02)){
					delButton.setDisabled(true);
				}*/
			}
//		}
	}
	
	
	public SuplrFeeSettView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		SuplrFeeSettView view = new SuplrFeeSettView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
	
	public void disableFeeName() {
		FEE_ID.setDisabled(true);
		SHPM_NO.setDisabled(true);
	}
}
