package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.ExportPayInAction;
import com.rd.client.action.settlement.ExportPayInitAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.PayInitBillDS;
import com.rd.client.ds.settlement.PayInitBilldetailDS;
import com.rd.client.ds.settlement.PayInitBilldetailDS2;
import com.rd.client.ds.settlement.PayInitDS;
import com.rd.client.ds.settlement.PayReqBillDS;
import com.rd.client.ds.settlement.PayReqdetailsDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrPayWin;
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
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
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
 * 费用管理-结算管理-应付对账单
 * @author cjt
 *
 */
@ClassForNameAble
public class PayInitBillView extends SGForm implements PanelFactory {

	private DataSource payinitDS;
	private DataSource detailDS;
	private DataSource detailDS2;
	private DataSource RayInitBillDS;
	private SGTable table;
	private SGTable payTable;
	private ListGrid itemTable;
	private ListGrid itemTable2;
	private SGTable unshpmTable1;
	private SectionStack list_section;
	private SectionStack list_section2;
	private ValuesManager vm;
	private Window searchWin;
	private SGPanel searchForm;
	private IButton addButton;
	private IButton generateAdjButton;
	private IButton confButton;
	private IButton canfButton;
	private IButton generateReqButton;
	private IButton addtoInvoiceButton;
	public DynamicForm pageForm; 
	public DynamicForm pageForm2; 
	
	private Window searchWin2 = null;
	private SGPanel searchForm2;
	
	private DataSource ds;
	private DataSource detailsDS3;
	private Double min;
	private Double max;
	private Double sum;
	private Double num;
	
	/*public PayInitBillView(String id) {
		super(id);
	}*/

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
		
		TabSet tabSet = new TabSet();  
		tabSet.setWidth("100%");   
		tabSet.setHeight("100%"); 
		tabSet.setMargin(0);
		if(isPrivilege(SettPrivRef.PayInitBill_P0)) {
			VLayout tobe = new VLayout();
			tobe.setWidth100();
			tobe.setHeight100();
		
	        Tab tab1 = new Tab("待对账");
	        tab1.setPane(tobe);
	        tabSet.addTab(tab1);
	        
			ToolStrip toolStrip = new ToolStrip();
			toolStrip.setAlign(Alignment.RIGHT);
			payinitDS = PayInitBillDS.getInstance("BILL_PAY_INITIAL","BILL_PAY_INITIAL");
			detailDS = PayInitBilldetailDS.getInstance("BILL_PAY_INITDETAILS","BILL_PAY_INITDETAILS");
			RayInitBillDS = PayInitDS.getInstance("V_PAY_INIT");
			
			HStack stack = new HStack();
			stack.setWidth("100%");
			stack.setHeight100();
			
			table = new SGTable(payinitDS,"100%", "100%", true, true, false){
				//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {
					VLayout layout = new VLayout(5);
					
					itemTable = new ListGrid() {  
			            @Override  
			            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {  
			                if (getFieldName(colNum).equals("ADJ_AMOUNT")) { 
			                	if(record.getAttribute("ADJ_AMOUNT")!=null){
			                		if (!record.getAttribute("ADJ_AMOUNT").equals("0")) {  
			                			return "font-weight:bold; color:#d64949;";  
			                		}else{  
					                	return super.getCellCSSText(record, rowNum, colNum);  
					                }   
			                	}else{
			                		return super.getCellCSSText(record, rowNum, colNum);  
			                	}
			                } else {  
			                    return super.getCellCSSText(record, rowNum, colNum);  
			                }  
			            }  
			        };
					itemTable.setDataSource(detailDS);
					itemTable.setWidth("100%");
					itemTable.setHeight(46);
					itemTable.setCellHeight(22);
					itemTable.setCanEdit(true);
					itemTable.setShowRowNumbers(true);
					itemTable.setCanSelectText(true);
					itemTable.setCanDragSelectText(true);
					itemTable.setAutoFitData(Autofit.VERTICAL);
					itemTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
					
					ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",105);
					LOAD_NO.setCanEdit(false);
					ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",60);
					PLATE_NO.setCanEdit(false);
					ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","车型",65);
					Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", "", "");
					VEHICLE_TYP_ID.setCanEdit(false);
					ListGridField DRIVER = new ListGridField("DRIVER","司机",50);
					DRIVER.setCanEdit(false);
					ListGridField MOIBLE = new ListGridField("MOBILE","联系电话",70);
					MOIBLE.setCanEdit(false);
					ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
					LOAD_DATE.setCanEdit(false);
					//Util.initDate(table, LOAD_DATE);
					ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期",70);
					UNLOAD_DATE.setCanEdit(false);
					//Util.initDate(table, UNLOAD_DATE);
					ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",80);
					LOAD_NAME.setCanEdit(false);
					ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",80);
					UNLOAD_NAME.setCanEdit(false);
					//ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","托数",60);
					//ListGridField TOT_NET_W = new ListGridField("TOT_NET_W","箱数",60);
					//ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W","吨位",60);
					//ListGridField TOT_PACK_V = new ListGridField("TOT_PACK_V","体积",60);
					
					ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额", 60);
					INIT_AMOUNT.setCanEdit(false);
					ListGridField TOT_AMOUNT = new ListGridField("TOT_AMOUNT","调前金额", 60);
			        TOT_AMOUNT.setCanEdit(false);
			        ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额",60);
			        ADJ_AMOUNT.setCanEdit(false);
			        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额", 60);
			        CONFIRM_AMOUNT.setCanEdit(false);
			        ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因", 120);
			        ADJ_REASON.setCanEdit(true);
					//ListGridField NOTES = new ListGridField("NOTES","备注",200);
					
					itemTable.setFields(LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOIBLE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,TOT_AMOUNT,ADJ_AMOUNT,CONFIRM_AMOUNT,ADJ_REASON);
					Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria("INIT_NO",record.getAttributeAsString("INIT_NO"));
					itemTable.fetchData(criteria);
					
					final Menu menu = new Menu();
					menu.setWidth(140);
					    
					if(isPrivilege(SettPrivRef.PayInitBill_P0_11)) {
						MenuItem exp = new MenuItem("导出作业单",StaticRef.ICON_EXPORT);  
						menu.addItem(exp);
						exp.addClickHandler(new ExportPayInAction(table));
					}
					if(isPrivilege(SettPrivRef.PayInitBill_P0_12)) {
						MenuItem expList = new MenuItem("导出调度单",StaticRef.ICON_EXPORT);  
						menu.addItem(expList);
						expList.addClickHandler(new ExportPayInitAction(table,itemTable));
					}
						
					itemTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
						public void onShowContextMenu(ShowContextMenuEvent event) {
							menu.showContextMenu();
							event.cancel();
						}
					});
					
					layout.addMember(itemTable);
					layout.setLayoutLeftMargin(30);
					
					return layout;
				};
			};

			table.setShowFilterEditor(false);
			table.setCanExpandRecords(true);
			table.setConfirmDiscardEdits(false);
			table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
			table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			createListField();
			
	        list_section = new SectionStack();
			final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		    listItem.setItems(table);
		    listItem.setExpanded(true);
		    pageForm=new SGPage(table, true).initPageBtn();
		    listItem.setControls(pageForm);
		    list_section.addSection(listItem);
		    list_section.setWidth("100%");		    
	        stack.addMember(list_section);
	        
	        final Menu menu = new Menu();
			menu.setWidth(140);
			    
			if(isPrivilege(SettPrivRef.PayInitBill_P0_11)) {
				MenuItem exp = new MenuItem("导出作业单",StaticRef.ICON_EXPORT);  
				menu.addItem(exp);
				exp.addClickHandler(new ExportPayInAction(table));
			}
			if(isPrivilege(SettPrivRef.PayInitBill_P0_12)) {
				MenuItem expList = new MenuItem("导出调度单",StaticRef.ICON_EXPORT);  
				menu.addItem(expList);
				expList.addClickHandler(new ExportPayInitAction(table,itemTable));
			}
				
			table.addShowContextMenuHandler(new ShowContextMenuHandler() {
				public void onShowContextMenu(ShowContextMenuEvent event) {
					menu.showContextMenu();
					event.cancel();
				}
			});
	        
		    createBtnWidget(toolStrip);
		    tobe.addMember(toolStrip);
		    
		    tobe.addMember(stack);
		}
		
		if(isPrivilege(SettPrivRef.PayInitBill_P1)) {
			VLayout be = new VLayout();
			be.setWidth100();
			be.setHeight100();
		
	        Tab tab1 = new Tab("已对账");
	        tab1.setPane(be);
	        tabSet.addTab(tab1);
			
			ToolStrip toolStrip2 = new ToolStrip();
			toolStrip2.setAlign(Alignment.RIGHT);
			detailDS2 = PayInitBilldetailDS2.getInstance("BILL_PAY_INITDETAILS2","BILL_PAY_INITDETAILS");
			
			//主布局
			HStack stack2 = new HStack();
			stack2.setWidth("100%");
			stack2.setHeight100();
			
			itemTable2=new SGTable(detailDS2, "100%", "100%", false, false, false);
			itemTable2.setWidth("100%");
			itemTable2.setHeight100();
			itemTable2.setCellHeight(22);
			itemTable2.setCanEdit(false);
			itemTable2.setShowRowNumbers(false);
			itemTable2.setAutoFitData(Autofit.VERTICAL);
			itemTable2.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			
			ListGridField INIT_NO = new ListGridField("INIT_NO","对账单号", 90);
	        ListGridField ID = new ListGridField("ID","", 100);
	        ID.setHidden(true);
			ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",110);
			ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",60);
			ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","车型",65);
			Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", "", "");
			ListGridField DRIVER = new ListGridField("DRIVER","司机",50);
			ListGridField MOIBLE = new ListGridField("MOIBLE","联系电话",70);
			ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
			//Util.initDate(table, LOAD_DATE);
			ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期",70);
			//Util.initDate(table, UNLOAD_DATE);
			ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",80);
			ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",80);
			//ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","托数",60);
			//ListGridField TOT_NET_W = new ListGridField("TOT_NET_W","箱数",60);
			//ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W","吨位",60);
			//ListGridField TOT_PACK_V = new ListGridField("TOT_PACK_V","体积",60);
			ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额", 60);
			//ListGridField TOT_AMOUNT = new ListGridField("TOT_AMOUNT","期初金额",60);
			//ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调前金额",60);
			ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额",60);
			ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因", 110);		
			
	        itemTable2.setFields(INIT_NO,ID,LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOIBLE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,CONFIRM_AMOUNT,ADJ_REASON);
			
			list_section2 = new SectionStack();
			final SectionStackSection listItem = new SectionStackSection("对账单信息");
		    listItem.setItems(itemTable2);
		    listItem.setExpanded(true);
		    pageForm2=new SGPage(itemTable2, true).initPageBtn();
		    listItem.setControls(pageForm2);
		    list_section2.addSection(listItem);
		    list_section2.setWidth("100%");
		    stack2.addMember(list_section2);
		   
		    
		    ds = PayReqBillDS.getInstance("V_PAY_REQUEST","BILL_PAY_REQUEST");
			payTable=new SGTable(ds, "100%", "100%", false, false, false) {
				
	        	//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {    	
					detailsDS3=PayReqdetailsDS.getInstance("V_PAY_REQDETAILS2","BILL_PAY_REQDETAILS");
					
	                VLayout layout = new VLayout();              
	                SGTable countryGrid = new SGTable();
	                
	                countryGrid.setDataSource(detailsDS3);
	                countryGrid.setWidth("100%");
	                countryGrid.setHeight(50);
	                countryGrid.setCanEdit(false);
	                countryGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
	                countryGrid.setAutoFitData(Autofit.VERTICAL);
	               
	                Criteria findValues = new Criteria();
	                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
			        findValues.addCriteria("REQ_NO", record.getAttributeAsString("REQ_NO"));

			        
			        ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号", 120);
			        LOAD_NO.setCanEdit(false);
			        ListGridField ID = new ListGridField("ID","", 120);
			        ID.setHidden(true);
			        ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 60);
			        PLATE_NO.setCanEdit(false);
			        
					ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","车型",65);
					Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", "", "");
					ListGridField DRIVER = new ListGridField("DRIVER","司机",50);
					ListGridField MOIBLE = new ListGridField("MOIBLE","联系电话",70);
					ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
					ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期",70);
					ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",80);
					ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",80);
					
			        ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额", 70);
			        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额", 80);

			        countryGrid.setFields(ID,LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOIBLE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,CONFIRM_AMOUNT);
			        countryGrid.fetchData(findValues);

	                layout.addMember(countryGrid);
	                layout.setLayoutLeftMargin(30);
	                
	                return layout;   
	            } 
			};
			payTable.setCanEdit(true);
			payTable.setShowFilterEditor(false);
			payTable.setCanExpandRecords(true);
			//table.setID("DDtable");
			createListFields();
			payTable.setCanExpandRecords(true);
			SectionStack section = new SectionStack();
			SectionStackSection listItem2 = new SectionStackSection("付款申请单列表");
			listItem2.setItems(payTable);
			listItem2.setExpanded(true);
			section.addSection(listItem2);
			DynamicForm pageForm = new SGPage(payTable,true).initPageBtn();
			listItem.setControls(pageForm);
			section.setWidth("100%");
			section.setHeight100();
			HStack stack3 = new HStack();
			stack3.setWidth("100%");
			stack3.setHeight("55%");
			stack3.addMember(section);
		    
		    createBtnWidget2(toolStrip2);
		    be.addMember(toolStrip2);
			be.addMember(stack2);
			be.addMember(stack3);
		}
		
		tabSet.addTabSelectedHandler(new TabSelectedHandler() {

			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(event.getTabNum() == 0) {
					fetchTable1();
				}
				else {
					fetchTable2(true);
				}
			}
			
		});
		
		main.addMember(tabSet);
		return main;
	}

	private void createListField() {
		
		ListGridField INIT_NO = new ListGridField("INIT_NO","对账单号",120);	
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商名称",120);
		ListGridField BELONG_MONETH = new ListGridField("BELONG_MONTH","所属帐期",90);
		ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额",100);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","本次调前金额",100);
		ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT", "调整金额", 90);
        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT", "本次确认金额", 100);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",100);
		TAX_AMOUNT.setCanEdit(true);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","确认金额（不含税）",120);
		ListGridField ACCOUNT_STAT_NAME = new ListGridField("ACCOUNT_STAT_NAME","对账状态",110);
		ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT","对账状态",100);
		ACCOUNT_STAT.setHidden(true);
		//Util.initCodesComboValue(ACCOUNT_STAT, "ACCOUNT_STAT");
		TAX_AMOUNT.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				
				if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("TAX_AMOUNT"))){
					min = Double.parseDouble(ObjUtil.ifNull(table.getSelectedRecord().getAttribute("TAX_AMOUNT").toString(),"0"))-Double.parseDouble("5");
					max = Double.parseDouble(ObjUtil.ifNull(table.getSelectedRecord().getAttribute("TAX_AMOUNT").toString(),"0"))+Double.parseDouble("5");
				}else{
					min = Double.parseDouble("0")-Double.parseDouble("5");
					max = Double.parseDouble("0")+Double.parseDouble("5");
				}
				num =  Double.parseDouble(table.getEditValues(table.getSelectedRecord()).get("TAX_AMOUNT").toString());
				if(num>=min && num <=max){
					sum = Double.parseDouble(table.getSelectedRecord().getAttribute("CONFIRM_AMOUNT").toString())-Double.parseDouble(num.toString());
					
				}else{
					SC.say("税金上下调整在5元范围以内");
					table.redraw();
					return;
				}
			}
		});
		
		table.setFields(INIT_NO,SUPLR_NAME,BELONG_MONETH,INIT_AMOUNT,INITITAL_AMOUNT,ADJ_AMOUNT,CONFIRM_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,ACCOUNT_STAT,ACCOUNT_STAT_NAME);
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(3);
		strip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.PayInitBill_P0);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(payinitDS,
							createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		//添加费用
		addButton = createUDFBtn("添加费用",StaticRef.ICON_COPY,SettPrivRef.PayInitBill_P0_01);
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(table.getSelectedRecord())){
					if(table.getSelection().length==1){
						getShpTable().show();
					}else{
						SC.say("只能选择一条记录添加！");
						return;
					}
				}else{
					return;
				}
			}
		});
		
		IButton saveButton1 = createUDFBtn("保存",StaticRef.ICON_SAVE,SettPrivRef.PayInitBill_P0_08);
		saveButton1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getRecords()==null)return;
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("update BILL_PAY_INITIAL set SUBTAX_AMOUNT='"+sum+"'");
				sf.append(",TAX_AMOUNT='"+num+"'");
				sf.append(" where ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
				sqlList.add(sf.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
							
					}

					@Override
					public void onSuccess(String result) {
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							table.getSelectedRecord().setAttribute("SUBTAX_AMOUNT", sum);
							table.getSelectedRecord().setAttribute("TAX_AMOUNT", num);
							table.redraw();
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
						
				});
			}
		});
		
		IButton saveButton = createUDFBtn("保存明细",StaticRef.ICON_SAVE,SettPrivRef.PayInitBill_P0_02);
        saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getRecords()==null)return;
				if(itemTable==null){
					SC.say("请展开明细");
					return;
				}else if(itemTable.getRecords()==null){
					return;
				}else{
					int[] record = itemTable.getAllEditRows();
					String n="";
					for(int j=0;j<record.length;j++){
						n=n+record[j]+",";
					}
					ListGridRecord[] records=itemTable.getRecords();
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					int a=0;
					for(int i = 0; i < records.length; i++) {
						Record rec = records[i];
						if(n.contains(i+",")){
							String rea = itemTable.getEditedRecord(record[a]).getAttribute("ADJ_REASON");
							sf = new StringBuffer();
							sf.append("update BILL_PAY_INITDETAILS set ADJ_REASON=");
							if(ObjUtil.isNotNull(rea)){
								sf.append("'"+rea+"'");
							}else{
								sf.append("''");
							}
							sf.append(" where ID='"+rec.getAttribute("ID")+"'");
							sqlList.add(sf.toString());
							a=a+1;
						}
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							
						}

						@Override
						public void onSuccess(String result) {
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
								itemTable.redraw();
							}else{
								MSGUtil.sayError(result.substring(2));
							}
						}
						
					});
				}
			}
		});
		
		//移除费用
		/*removeButton = createUDFBtn("移除费用",StaticRef.ICON_DEL,SettPrivRef.PayInitBill_P0_02);
		removeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table != null && table.getSelection().length>0) {
					if(itemTable != null && itemTable.getSelection().length>0){
						if(itemTable.getSelection().length==itemTable.getRecords().length){
							SC.say("只有一条或选中全部的时候不能移除！");
							return;
						}else{
							SC.confirm("确定移除勾选的费用？", new BooleanCallback() {
							public void execute(Boolean value) {
			                    if (value != null && value) {
			                    	ArrayList<String> sqlList = new ArrayList<String>();
			                    	StringBuffer sf = new StringBuffer();
			                    	ListGridRecord[] rec = itemTable.getSelection();
			                    	for(int i=0;i<rec.length;i++){
			                    		sf = new StringBuffer();
			                    		sf.append("delete from BILL_PAY_INITDETAILS where LOAD_NO='"+rec[i].getAttribute("LOAD_NO")+"'");
			                    		sqlList.add(sf.toString());
			                    	}
			                    	String sql="UPDATE BILL_PAY_INITIAL set(INITITAL_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT)=(select SUM(TOT_AMOUNT),SUM(TOT_AMOUNT)*BILL_PAY_INITIAL.TAX/(100+BILL_PAY_INITIAL.TAX),SUM(TOT_AMOUNT)-SUM(TOT_AMOUNT)*BILL_PAY_INITIAL.TAX/(100+BILL_PAY_INITIAL.TAX) from BILL_PAY_INITDETAILS where BILL_PAY_INITDETAILS.INIT_NO=BILL_PAY_INITIAL.INIT_NO group by INIT_NO)  where INIT_NO='"+table.getSelectedRecord().getAttribute("INIT_NO")+"'";
						        	
			                    	sqlList.add(sql);
			                    	Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
										
										@Override
										public void onSuccess(String result) {
											if(result.equals(StaticRef.SUCCESS_CODE)) {
												MSGUtil.showOperSuccess();
												table.collapseRecord(table.getSelectedRecord());
												table.invalidateCache();
												Criteria findValues = new Criteria();
												findValues.addCriteria("OP_FLAG", "M");
												table.fetchData(findValues);
											}else{
												MSGUtil.sayError(result);
											}
										}
										
										@Override
										public void onFailure(Throwable caught) {
											
										}
										
									});
			                    }
			                }
							});
						}
					}else{
						SC.say("请选择从表的数据！");
						return;
					}
				}else{
					SC.say("请选择主表的数据！");
					return;
				}
			}
		});*/
		
		//生成调整账单
		generateAdjButton = createUDFBtn("生成调整账单",StaticRef.ICON_EXPORT,SettPrivRef.PayInitBill_P0_03);
        generateAdjButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> no_list = new HashMap<String, String>(); //期初单号
				ListGridRecord [] records=table.getSelection();
				if(records!=null&&records.length>0){
					for(int i=0;i<records.length;i++){
						no_list.put(String.valueOf(i+1), records[i].getAttribute("INIT_NO"));
					}
					
				}
				String loginUser = LoginCache.getLoginUser().getUSER_ID();
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				//list.add(shpm_No);
				listMap.put("1", no_list);
				listMap.put("2",loginUser);
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_PAY_CREATE_ADJNO(?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//刷新数据
							table.discardAllEdits();
							table.invalidateCache();
							Criteria crit = table.getCriteria();
							if(crit == null) {
								crit = new Criteria();
							}
							crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
							if(searchForm != null) {
								crit.addCriteria(searchForm.getValuesAsCriteria());
							}
							table.fetchData(crit,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									if (table.getRecordList().getLength() > 0){
										table.selectRecord(0);
									}
									addButton.setDisabled(true);
									//removeButton.setDisabled(true);
									generateAdjButton.setDisabled(true);
								}
							});
						
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
		//确认对账
		confButton = createUDFBtn("确认对账",StaticRef.ICON_SAVE,SettPrivRef.PayInitBill_P0_04);
		confButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				final ListGridRecord[] rec = table.getSelection();
				for(int i=0;i<rec.length;i++){
					
					double inititalAmount=Double.parseDouble(ObjUtil.ifNull(rec[i].getAttribute("ADJ_AMOUNT"),"0"));
					
					if(inititalAmount != 0){						
						SC.say("账单金额已发生变化，请先生成调整账单！");
						sqlList=null;
						return;
					}
					if(rec[i].getAttribute("ACCOUNT_STAT").equals("20")){
						SC.say("该记录已经确认对账");
						sqlList=null;
	                    return;
	                    
					}else{
						sf = new StringBuffer();
						sf.append("update BILL_PAY_INITIAL set ACCOUNT_STAT='20' where ID=");
						sf.append("'"+rec[i].getAttribute("ID")+"'");
						sqlList.add(sf.toString());
						
						sf = new StringBuffer();
						sf.append("update bill_pay_initdetails set ACCOUNT_STAT = '20' where INIT_NO = '");
						sf.append(rec[i].getAttribute("INIT_NO"));
						sf.append("'");
						sqlList.add(sf.toString());
						
						sf = new StringBuffer();
						sf.append("update TRANS_LOAD_HEADER set ACCOUNT_STAT = '20' where LOAD_NO in (select LOAD_NO FROM bill_pay_initdetails where INIT_NO = '");
						sf.append(rec[i].getAttribute("INIT_NO"));
						sf.append("')");
						sqlList.add(sf.toString());
					}
				}
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
							
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)) {
							MSGUtil.showOperSuccess();
							fetchTable1();
						}else{
							MSGUtil.sayError(result);
						}
					}
						
				});
			}
			
		});
		
		//导出
        IButton expButton = createBtn("导出账单（项目）",SettPrivRef.PayInitBill_P0_06);
        expButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				Util.PayInitExportUtil1();
			}
		});
        
        IButton expButton1 = createBtn("导出账单（承运商）",SettPrivRef.PayInitBill_P0_10);
        expButton1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Util.PayInitExportUtil2();
			}
		});
	
        strip.setMembersMargin(4);
        strip.setMembers(searchButton,addButton,saveButton1,saveButton,generateAdjButton,confButton,expButton,expButton1);
	}
	
	public void createBtnWidget2(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(3);
		strip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.PayInitBill_P0);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin2 == null) {
					searchForm2 = new SGPanel();
					searchWin2 = new SearchWin(detailDS2,
							createSerchForm2(searchForm2),list_section2.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		//取消对账
		canfButton = createUDFBtn("取消对账",StaticRef.ICON_CANCEL,SettPrivRef.PayInitBill_P1_07);
		canfButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				final ListGridRecord[] record=itemTable2.getSelection();
				
				if(record!=null&&record.length>0){
					
					String recs="'"+record[0].getAttribute("INIT_NO")+"'";
					if(record.length>1){
					
						for(int k=1;k<record.length;k++){
							recs=recs+",'"+record[k].getAttribute("INIT_NO")+"'";
					
						}
					}
					
					Util.db_async.getRecord("INVOICE_STAT", "BILL_PAY_INITDETAILS", " where INIT_NO in ("+recs+")", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							ArrayList<String> sqlList = new ArrayList<String>();
							
							for(int i=0;i<record.length;i++){
								if(result.get(i).get("INVOICE_STAT").toString().equals("20")){
									SC.say("账单已生成开票申请单，无法取消对账！");
									return;
								}
								if(record[i].getAttribute("ACCOUNT_STAT").equals("10")){
								
									SC.say("该记录还没有确认对账");
								
									sqlList=null;
			                    
									return;
									
								}else{
							
									String ID=record[i].getAttribute("INIT_NO");
									String sql="update BILL_PAY_INITIAL set ACCOUNT_STAT='10' where INIT_NO='"+ID+"'";
											
									sqlList.add(sql);
									
									sql =" update BILL_PAY_INITDETAILS set ACCOUNT_STAT = '10' where ID = '" + record[i].getAttribute("ID") + "'";
									sqlList.add(sql);
							    }
							}
							Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
							
								@Override
								public void onFailure(Throwable caught) {
									MSGUtil.sayError(caught.getMessage());
								}
			
								@Override
								public void onSuccess(String result) {
									
									fetchTable2(false);
									//removeButton.setDisabled(false);
								
								}
							
						
							});
						}
						
						@Override
						public void onFailure(Throwable caught) {
							System.out.println(123);
						
						}
					
					});
					
				}
				
				
				
				/*ArrayList<String> sqlList = new ArrayList<String>();
				if(records!=null&&records.length>0){
					
					StringBuffer sf = new StringBuffer();
					for(int i=0;i<records.length;i++){
						if(records[i].getAttribute("ACCOUNT_STAT").equals("10")){
							SC.say("该记录不需要取消对账");
							sqlList=null;
		                    return;
		                    
						}else{
							sf = new StringBuffer();
							sf.append("update BILL_PAY_INITIAL set ACCOUNT_STAT='10' where INIT_NO=");
							sf.append("'"+records[i].getAttribute("INIT_NO")+"'");
							sqlList.add(sf.toString());
							
							sf = new StringBuffer();
							sf.append("update bill_pay_initdetails set ACCOUNT_STAT = '10' where INIT_NO = '");
							sf.append(records[i].getAttribute("INIT_NO"));
							sf.append("'");
							sqlList.add(sf.toString());
							
//							sf = new StringBuffer();
//							sf.append("update TRANS_LOAD_HEADER set ACCOUNT_STAT = '10' where LOAD_NO = '");
//							sf.append(records[i].getAttribute("LOAD_NO"));
//							sf.append("'");
//							sqlList.add(sf.toString());
						}
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
								
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								fetchTable2(false);
							}else{
								MSGUtil.sayError(result);
							}
						}
							
					});
				}*/
			}
			
		});
		
		//生成请款单
		generateReqButton = createUDFBtn("生成请款单",StaticRef.ICON_EXPORT,SettPrivRef.PayInitBill_P1_05);
        generateReqButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> no_list = new HashMap<String, String>(); //期初单号
				ListGridRecord [] records=itemTable2.getSelection();
				if(records!=null&&records.length>0){
					for(int i=0;i<records.length;i++){
						no_list.put(String.valueOf(i+1), records[i].getAttribute("ID"));
					}
					
				}
				String loginUser = LoginCache.getLoginUser().getUSER_ID();
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				//list.add(shpm_No);
				listMap.put("1", no_list);
				listMap.put("2",loginUser);
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_CONFIRM_REQUEST(?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//刷新数据
							itemTable2.discardAllEdits();
							itemTable2.invalidateCache();
							Criteria crit = itemTable2.getCriteria();
							if(crit == null) {
								crit = new Criteria();
							}
							crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
							if(searchForm2 != null) {
								crit.addCriteria(searchForm2.getValuesAsCriteria());
							}
							else {
								crit.addCriteria("INVOICE_STAT", "10");
							}
							itemTable2.fetchData(crit,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									Criteria findValues = new Criteria();
						            findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						            findValues.addCriteria("STATUS", "10");
							        payTable.invalidateCache();
							        payTable.fetchData(findValues);
								}
							});
						
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
        
        //加入请款单
        addtoInvoiceButton = createUDFBtn("加入请款单",StaticRef.ICON_SAVE,SettPrivRef.PayInitBill_P1_09);
        addtoInvoiceButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(payTable.getSelectedRecord() == null) {
					MSGUtil.sayError("请选择开票申请单");
					return;
				}
				HashMap<String, String> no_list = new HashMap<String, String>(); //期初单号
				ListGridRecord [] records=itemTable2.getSelection();
				if(records!=null&&records.length>0){
					for(int i=0;i<records.length;i++){
						no_list.put(String.valueOf(i+1), records[i].getAttribute("ID"));
					}
					
				}
				String loginUser = LoginCache.getLoginUser().getUSER_ID();
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				//list.add(shpm_No);
				listMap.put("1", no_list);
				listMap.put("2", payTable.getSelectedRecord().getAttribute("REQ_NO"));
				listMap.put("3",loginUser);
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_PAY_ADDTO_REQUEST(?,?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//刷新数据
							itemTable2.discardAllEdits();
							itemTable2.invalidateCache();
							Criteria crit = itemTable2.getCriteria();
							if(crit == null) {
								crit = new Criteria();
							}
							crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
							if(searchForm2 != null) {
								crit.addCriteria(searchForm.getValuesAsCriteria());
							}
							else {
								crit.addCriteria("INVOICE_STAT","10");
							}
							itemTable2.fetchData(crit,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									
									Criteria findValues = new Criteria();
						            findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						            findValues.addCriteria("STATUS", "10");
							        payTable.invalidateCache();
						    		payTable.fetchData(findValues);
								}
							});
						
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		});
        
        
		//导出
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.PayInitBill_P0_06);
        expButton.addClickHandler(new ExportAction(table,"addtime desc"));
	
        strip.setMembersMargin(4);
        strip.setMembers(searchButton,canfButton,generateReqButton,addtoInvoiceButton,expButton);
	}
	
	public DynamicForm createSerchForm(final SGPanel form){
		form.setDataSource(payinitDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGText SUPLR_NAME = new SGText("SUPLR_NAME","承运商");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrPayWin(form,"20%","50%").getViewPanel();		
			}
		});
		
		SUPLR_NAME.setIcons(searchPicker);
		
		SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SGText BELONG_MONETH = new SGText("BELONG_MONTH","所属期");
		
		SGCombo ACCOUNT_STAT = new SGCombo("ACCOUNT_STAT","对账状态");
		Util.initComboValue(ACCOUNT_STAT, "BAS_CODES","CODE","NAME_C"," where prop_code='ACCOUNT_STAT'","","10");
		
		SGCombo SUPLR_TYP = new SGCombo("SUPLR_TYP","承运商类别");
		Util.initCodesComboValue(SUPLR_TYP, "SUPLR_TYP");
		
		form.setItems(SUPLR_ID,SUPLR_NAME,BELONG_MONETH,ACCOUNT_STAT,SUPLR_TYP);
		return form;
	}
	
	public DynamicForm createSerchForm2(final SGPanel form){
		form.setDataSource(detailDS2);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGText SUPLR_NAME = new SGText("SUPLR_NAME","承运商");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrPayWin(form,"20%","50%").getViewPanel();		
			}
		});
		
		SUPLR_NAME.setIcons(searchPicker);
		
		SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SGText BELONG_MONETH = new SGText("BELONG_MONTH","所属期");
		
		SGCombo ACCOUNT_STAT = new SGCombo("ACCOUNT_STAT","对账状态");
		Util.initComboValue(ACCOUNT_STAT, "BAS_CODES","CODE","NAME_C"," where prop_code='ACCOUNT_STAT'","","20");
		
		SGCombo SUPLR_TYP = new SGCombo("SUPLR_TYP","承运商类别");
		Util.initCodesComboValue(SUPLR_TYP, "SUPLR_TYP");
		
		form.setItems(SUPLR_ID,SUPLR_NAME,BELONG_MONETH,ACCOUNT_STAT,SUPLR_TYP);
		return form;
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}
	
	public Window getShpTable() {
		
    	unshpmTable1=new SGTable(RayInitBillDS, "100%", "100%", false, true, false);
		unshpmTable1.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		unshpmTable1.setCanEdit(false); 
		createUnshpmField(unshpmTable1);
	
		final SectionStack shpSectionStack =new SectionStack();	 	
		SectionStackSection shpsection=new SectionStackSection("费用信息");
		final DynamicForm pageform=new SGPage(unshpmTable1, true).initPageBtn();		
		shpsection.setItems(unshpmTable1);
		shpsection.setExpanded(true);
		shpsection.setControls(pageform);
		shpSectionStack.addSection(shpsection);
		final Window win=new Window();
		VLayout shpLay = new VLayout();
		final SGPanel form=new SGPanel();
		
		SGText SUPLR_CNAME = new SGText("SUPLR_CNAME","承运商");
		SUPLR_CNAME.setValue(table.getSelectedRecord().getAttribute("SUPLR_NAME"));
		SUPLR_CNAME.setDisabled(true);
		//Util.initComboValue(SUPLR_CNAME, "BAS_SUPPLIER", "SUPLR_CNAME", "SUPLR_CNAME","", "");
		SGText LOAD_NO = new SGText("LOAD_NO", "调度单号");
		SGText PLATE_NO = new SGText("PLATE_NO", "车牌号");
		SGCombo FEE_TYPE = new SGCombo("FEE_TYPE", "费用类型");
		Util.initComboValue(FEE_TYPE,"BAS_CODES","NAME_C","NAME_C"," where prop_code='INITBILL_TYPE' ","");
        SGText BELONG_MONTH = new SGText("BELONG_MONTH", "所属账期");
		//SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", "收货方");

		SGButtonItem searchButton1=new SGButtonItem(StaticRef.FETCH_BTN);
		searchButton1.setWidth(80);
		searchButton1.setColSpan(2);
		searchButton1.addClickHandler(
			    new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
				
				@Override
				public void onClick(
						com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
					
					   
					unshpmTable1.discardAllEdits();
					unshpmTable1.invalidateCache();
					final Criteria criteria = new Criteria();
					criteria.addCriteria("OP_FLAG","M");
					criteria.addCriteria(form.getValuesAsCriteria());
					unshpmTable1.setFilterEditorCriteria(criteria);
					unshpmTable1.fetchData(criteria, new DSCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void execute(DSResponse response, Object rawData,
								DSRequest request) {
							if(pageform != null) {
								pageform.getField("CUR_PAGE").setValue("1");
								pageform.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageform.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								String sqlwhere = Cookies.getCookie("SQLWHERE");
								if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
									table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
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
			});
		SGButtonItem confirmButton=new SGButtonItem("确定",StaticRef.ICON_SAVE);
		confirmButton.setWidth(80);
		confirmButton.setColSpan(2);
		confirmButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord().getAttribute("ACCOUNT_STAT").equals("20")){
					SC.say("已经确认的对账单，无法添加费用！");
					return;
				}
				final ListGridRecord[] records=unshpmTable1.getSelection();
				if(records == null || records.length == 0) {
					MSGUtil.sayError("请勾选费用信息进行操作");
					return;
				}
				
				HashMap<String, String> odr_list = new HashMap<String, String>(); // 托运单号
				HashMap<String, String> fee_list = new HashMap<String, String>(); // 费用类型
				HashMap<String, String> id_list = new HashMap<String, String>(); // 费用类型
				for(int i = 0; i < records.length; i++) {
					id_list.put(String.valueOf(i+1), records[i].getAttribute("ID"));
					odr_list.put(String.valueOf(i+1), records[i].getAttribute("LOAD_NO"));
					fee_list.put(String.valueOf(i+1), records[i].getAttribute("FEE_TYPE"));
				}
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				listMap.put("1", table.getSelectedRecord().getAttribute("INIT_NO"));
				listMap.put("2", id_list);
				listMap.put("3", odr_list);
				listMap.put("4", fee_list);
				listMap.put("5", LoginCache.getLoginUser().getUSER_ID());
			
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_PAY_ADDFEE(?,?,?,?,?,?)", new AsyncCallback<String>() {
					 	        					
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						if(result.substring(0,2).equals("00")){
						//System.out.println(table.getSelectedRecord());
							
							win.destroy();
							table.collapseRecord(table.getSelectedRecord());
							final Criteria criteria = new Criteria();
							criteria.addCriteria("OP_FLAG","M");
							table.invalidateCache();
							table.fetchData(criteria);
							table.redraw();
							MSGUtil.showOperSuccess();
						}
					}				
	
				});	

			}
		
		});
		
		
		
		
		form.setItems(SUPLR_CNAME,LOAD_NO,PLATE_NO,FEE_TYPE,BELONG_MONTH,searchButton1,confirmButton);
		form.setNumCols(10);
		form.setWidth("40%");
		shpLay.addMember(form);

		shpLay.addMember(shpSectionStack);		
		win.addItem(shpLay);
		win.setTitle("添加费用");
		win.setWidth("65%");
		win.setHeight("70%");
		win.setTop("20%");
		win.setLeft("25%");
		return win;
		
	}
	
	public void createUnshpmField(SGTable addTable){
		
		ListGridField ID = new ListGridField("ID","",30);
		ID.setHidden(true);
		ListGridField FEE_TYPE = new ListGridField("FEE_TYPE", "费用类型", 90);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属账期", 80);
		//ListGridField SUPLR_CNAME = new ListGridField("SUPLR_CNAME", "承运商", 100);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单号", 100);
		//ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", "客户单号", 100);
		//ListGridField LOAD_DATE = new ListGridField("LOAD_DATE", "发货日期", 100);
		//ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE", "收货日期", 100);
		//ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", "发货方", 120);
		//ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", "收货方", 120);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 80);
		ListGridField VEHICLE_TYPE = new ListGridField("VEHICLE_TYPE", "车型", 90);
		ListGridField AMOUNT = new ListGridField("AMOUNT", "应付金额（含税）", 100);
		ListGridField TYPE_NAME = new ListGridField("TYPE_NAME", "类型说明", 80);
		ListGridField DESCR = new ListGridField("DESCR", "备注", 180);
		
		addTable.setFields(ID,FEE_TYPE,BELONG_MONTH,LOAD_NO,PLATE_NO,VEHICLE_TYPE,AMOUNT,TYPE_NAME,DESCR);
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PayInitBillView view = new PayInitBillView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}

	private void createListFields(){
		   
		final ListGridField REQ_NO = new ListGridField("REQ_NO","请款单号",120);
		REQ_NO.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STATUS","审批状态",80);
		STATUS.setCanEdit(false);
		Util.initCodesComboValue(STATUS,"APPROVE_STS");
		ListGridField PAY_STATUS = new ListGridField("PAY_STATUS","核销状态",80);
		PAY_STATUS.setCanEdit(false);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",110);
		SUPLR_NAME.setCanEdit(false);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",70);
		BELONG_MONTH.setCanEdit(false);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",80);
		INITITAL_AMOUNT.setCanEdit(false);
		ListGridField PAY_AMOUNT = new ListGridField("PAY_AMOUNT","实付",80);
		PAY_AMOUNT.setCanEdit(false);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",70);
		TAX_AMOUNT.setCanEdit(false);
		final ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","实付金额（不含税）",120);
		SUBTAX_AMOUNT.setCanEdit(false);
		ListGridField NOTES = new ListGridField("NOTES","摘要",160);
		NOTES.setCanEdit(true);
		
		ListGridField ID = new ListGridField("ID","",120);
		ID.setHidden(true);
		
		
		payTable.setFields(ID,REQ_NO,STATUS,PAY_STATUS,SUPLR_NAME,BELONG_MONTH
				        ,INITITAL_AMOUNT,PAY_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);
		
		

	}
	
	private void fetchTable1() {
		table.discardAllEdits();
		table.invalidateCache();
		
		Criteria crit = table.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ACCOUNT_STAT", "10");
		final Criteria crits = crit;
		table.fetchData(crits, new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crits.getValues();
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
	
	private void fetchTable2(final boolean bolRefreshBill) {
		itemTable2.discardAllEdits();
		itemTable2.invalidateCache();
		
		Criteria crit = itemTable2.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ACCOUNT_STAT", "20");
		crit.addCriteria("INVOICE_STAT", "10");
		final Criteria crits = crit;
		itemTable2.fetchData(crits,new DSCallback() {

			@SuppressWarnings("unchecked")
			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				if(pageForm2 != null) {
					pageForm2.getField("CUR_PAGE").setValue("1");
					pageForm2.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm2.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						itemTable2.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) crits.getValues();
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
				if(bolRefreshBill) {
					Criteria findValues = new Criteria();
		            findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
			        findValues.addCriteria("STATUS", "10");
			        payTable.invalidateCache();
			        payTable.fetchData(findValues);
				}
			}
			
		});
	}
}