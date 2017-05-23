package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
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
import com.rd.client.ds.settlement.BillSettleDS2;
import com.rd.client.ds.settlement.RecInitBillDS;
import com.rd.client.ds.settlement.RecInitdetailsDS;
import com.rd.client.ds.settlement.RecInitdetailsDS2;
import com.rd.client.ds.settlement.RecInitialDS;
import com.rd.client.ds.settlement.TransBillReceDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BussWin;
import com.rd.client.win.SearchWin;
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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 
 *费用管理---结算管理-应收对账单
 *
 */
@ClassForNameAble
public class RecInitBillView extends SGForm implements PanelFactory {
	
	 private DataSource ds;
	 private DataSource lstDS; 
	 private DataSource RecInitBillDs;
	 //private DataSource ds2;
	 private DataSource billds;
	 private DataSource billDetailds;
	 private DataSource lstDS2; 
	 private SGTable table;
	 //private SGTable table2;
	 private SGTable billTable;
	 private ListGrid unshpmlstTable;
	 private ListGrid unshpmlstTable2;
	 private SectionStack section;
	 private SectionStack section2;
	 private SectionStack section3;
	 private Window searchWin = null;
	 private DynamicForm searchForm;
	 private Window searchWin2 = null;
	 private DynamicForm searchForm2;
     private IButton confButton;
     private IButton confCanButton;
     private IButton addButton;
     //private IButton removeButton;
     private IButton generateReqButton ;
     private IButton addtoInvoiceButton;
     private IButton generateAdjButton ;
     public DynamicForm pageForm; 
     public DynamicForm pageForm2; 
     private Double min;
     private Double max;
     private Double sum;
     private Double num;
     
	 
	 /*public RecInitBillView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		TabSet tabSet = new TabSet();  
		tabSet.setWidth("100%");   
		tabSet.setHeight("100%"); 
		tabSet.setMargin(0);
		if(isPrivilege(SettPrivRef.RecInitBill_P0)) {
			VLayout tobe = new VLayout();
			tobe.setWidth100();
			tobe.setHeight100();
		
	        Tab tab1 = new Tab("待对账");
	        tab1.setPane(tobe);
	        tabSet.addTab(tab1);

		    ToolStrip toolStrip = new ToolStrip();  //按钮布局
		    toolStrip.setAlign(Alignment.LEFT);
		    ds = RecInitialDS.getInstance("V_REC_INITIAL","BILL_REC_INITIAL");
		    lstDS=RecInitdetailsDS.getInstance("V_REC_INITDETAILS","BILL_REC_INITDETAILS");
		    RecInitBillDs=RecInitBillDS.getInstance("V_REC_INIT");
		    //主布局
			HStack stack = new HStack();
			stack.setWidth("100%");
			stack.setHeight100();
		
			//STACK的左边列表
			table=new SGTable(ds, "100%", "100%", false, false, false) {
				
	        	//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
	                VLayout layout = new VLayout();              
	  
	                unshpmlstTable  = new ListGrid() {  
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
	                unshpmlstTable.setDataSource(lstDS);
	                unshpmlstTable.setWidth("100%");
	                unshpmlstTable.setHeight(60);
	                unshpmlstTable.setCanEdit(true);
	                unshpmlstTable.setAutoFitData(Autofit.VERTICAL);
	                unshpmlstTable.setShowRowNumbers(true);
	                unshpmlstTable.setCanSelectText(true);
	                unshpmlstTable.setCanDragSelectText(true);
	                unshpmlstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	               
	                Criteria findValues = new Criteria();
	                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
			        findValues.addCriteria("INIT_NO", record.getAttributeAsString("INIT_NO"));
			        findValues.addCriteria("ACCOUNT_STAT", "10");
			        	
			        //作业单明细列表
			        ListGridField ODR_NO = new ListGridField("ODR_NO","订单号", 95);
			        ODR_NO.setCanEdit(false);
			        ListGridField ID = new ListGridField("ID","", 100);
			        ID.setHidden(true);
			        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号", 65);
			        CUSTOM_ODR_NO.setCanEdit(false);
			        //ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 80);
			        ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型", 70);
			        VEHICLE_TYP_ID_NAME.setCanEdit(false);
			        ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
			        LOAD_DATE.setCanEdit(false);
			        ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期", 70);
			        UNLOAD_DATE.setCanEdit(false);
			        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地", 80);
			        LOAD_NAME.setCanEdit(false);
			        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地", 80);
			        UNLOAD_NAME.setCanEdit(false);
			        //ListGridField FEE_BAS = new ListGridField("FEE_BAS_NAME","计费基准", 60);
			       // ListGridField BAS_VALUE = new ListGridField("BAS_VALUE","基准值", 60);
			        ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额", 65);
			        INIT_AMOUNT.setCanEdit(false);
			        ListGridField TOT_AMOUNT = new ListGridField("TOT_AMOUNT","期初金额", 65);
			        TOT_AMOUNT.setCanEdit(false);
			        ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额",65);
			        ADJ_AMOUNT.setCanEdit(false);
			        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额", 65);
			        CONFIRM_AMOUNT.setCanEdit(false);
			        ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因", 120);
			        ADJ_REASON.setCanEdit(true);
			        //ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金", 60);
			        //ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","应收金额（不含税）", 120);
			        //ListGridField NOTES = new ListGridField("NOTES","备注", 140);
			        unshpmlstTable.setFields(ID,ODR_NO, CUSTOM_ODR_NO,VEHICLE_TYP_ID_NAME,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,TOT_AMOUNT,ADJ_AMOUNT,CONFIRM_AMOUNT,ADJ_REASON);
			        unshpmlstTable.fetchData(findValues);
	
	        		
	                layout.addMember(unshpmlstTable);
	                layout.setLayoutLeftMargin(60);
	                
	                return layout;   
	            } 
			};
			table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
			table.setCanEdit(false); 
			table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
			table.setCanExpandRecords(true);
			getConfigList();
			section = new SectionStack();
			SectionStackSection listItem = new SectionStackSection("列表信息");
		    listItem.setItems(table);
		    listItem.setExpanded(true);
		    pageForm=new SGPage(table, true).initPageBtn();
			listItem.setControls(pageForm);
		    section.addSection(listItem);
		    section.setWidth("100%");
			stack.addMember(section);
			
			//创建按钮布局
			createBtnWidget(toolStrip);
			tobe.addMember(toolStrip);
			
			tobe.addMember(stack);
		}
		if(isPrivilege(SettPrivRef.RecInitBill_P1)) {
			VLayout be = new VLayout();
			be.setWidth100();
			be.setHeight100();
		
	        Tab tab1 = new Tab("已对账");
	        tab1.setPane(be);
	        tabSet.addTab(tab1);

		    ToolStrip toolStrip = new ToolStrip();  //按钮布局
		    toolStrip.setAlign(Alignment.LEFT);
		    //ds2 = RecInitialDS2.getInstance("V_REC_INITIAL2","BILL_REC_INITIAL");
		    lstDS2=RecInitdetailsDS2.getInstance("V_REC_INITDETAILS2","BILL_REC_INITDETAILS");
		    //主布局
			HStack stack = new HStack();
			stack.setWidth("100%");
			stack.setHeight100();
		
			//STACK的左边列表
			unshpmlstTable2=new SGTable(lstDS2, "100%", "100%", false, false, false);
			unshpmlstTable2.setDataSource(lstDS2);
            unshpmlstTable2.setWidth("100%");
            unshpmlstTable2.setHeight100();
            unshpmlstTable2.setCanEdit(false);
            unshpmlstTable2.setAutoFitData(Autofit.VERTICAL);
            unshpmlstTable2.setSelectionAppearance(SelectionAppearance.CHECKBOX);

	        	
	        //作业单明细列表
	        ListGridField INIT_NO = new ListGridField("INIT_NO","对账单号", 100);
	        ListGridField ODR_NO = new ListGridField("ODR_NO","订单号", 100);
	        ListGridField ID = new ListGridField("ID","", 100);
	        ID.setHidden(true);
	        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号", 70);
	        //ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 80);
	        ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型", 70);
	        ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
	        ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期", 70);
	        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地", 80);
	        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地", 80);
	        //ListGridField FEE_BAS = new ListGridField("FEE_BAS_NAME","计费基准", 60);
	       // ListGridField BAS_VALUE = new ListGridField("BAS_VALUE","基准值", 60);
	        ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT","月初金额", 65);
	        //ListGridField TOT_AMOUNT = new ListGridField("TOT_AMOUNT","调前金额", 65);
	        //ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额",65);
	        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额", 65);
	        ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因", 120);
	        //ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金", 60);
	        //ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","应收金额（不含税）", 120);
	        //ListGridField NOTES = new ListGridField("NOTES","备注", 140);
	        unshpmlstTable2.setFields(INIT_NO,ID,ODR_NO, CUSTOM_ODR_NO,VEHICLE_TYP_ID_NAME,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,CONFIRM_AMOUNT,ADJ_REASON);
				
			section2 = new SectionStack();
			SectionStackSection listItem = new SectionStackSection("对账单信息");
		    listItem.setItems(unshpmlstTable2);
		    listItem.setExpanded(true);
		    pageForm2=new SGPage(unshpmlstTable2, true).initPageBtn();
			listItem.setControls(pageForm2);
		    section2.addSection(listItem);
		    section2.setWidth("100%");
			stack.addMember(section2);
			
			// 左边列表
			billds = BillSettleDS2.getInstance("BILL_REC_INVOICE2","BILL_REC_INVOICE");
			billTable = new SGTable(billds, "100%", "100%", false, true, false) {
				//明细表
				protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
	                VLayout layout = new VLayout();   
	                
	                billDetailds = TransBillReceDS.getInstance("BILL_REC_INVOICEDETAILS","BILL_REC_INVOICEDETAILS");
	                SGTable detailGrid = new SGTable();
	                 // lstTable = new SGTable();
	                detailGrid.setDataSource(billDetailds);
	                detailGrid.setWidth("100%");
	                detailGrid.setHeight(50);
	               // lstTable.setCanEdit(false);
	                detailGrid.setAutoFetchData(false);
	                detailGrid.setShowRowNumbers(true);
	                detailGrid.setCanDragRecordsOut(true);   
	                detailGrid.setCanAcceptDroppedRecords(true);   
	                detailGrid.setCanReorderRecords(true);   
	                detailGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
	                detailGrid.setAutoFitData(Autofit.VERTICAL);
	                detailGrid.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);  
	               // lstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	               
	                Criteria findValues = new Criteria();
	                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
			        findValues.addCriteria("ADJ_NO", record.getAttributeAsString("ADJ_NO"));
			        	
			        //作业单明细列表
			        ListGridField ODR_NO = new ListGridField("ODR_NO","订单号", 95);
			        ListGridField ID = new ListGridField("ID","", 100);
			        ID.setHidden(true);
			        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号", 65);
			        //ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 80);
			        ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型", 70);
			        ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
			        ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期", 70);
			        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地", 90);
			        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地", 90);
			        ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","确认金额", 70);
			        detailGrid.setFields(ID,ODR_NO,CUSTOM_ODR_NO,VEHICLE_TYP_ID_NAME,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,ACT_AMOUNT);
			        detailGrid.fetchData(findValues);

	                layout.addMember(detailGrid);
	                layout.setLayoutTopMargin(0);
	                layout.setLayoutLeftMargin(30);
	                
	                return layout;   
	            } 
			};
			//table.setID("DDtable");
			createBillListField();
			billTable.setCanExpandRecords(true);
			section3 = new SectionStack();
			SectionStackSection listItem2 = new SectionStackSection("开票申请单列表");
			listItem2.setItems(billTable);
			listItem2.setExpanded(true);
			section3.addSection(listItem2);
			DynamicForm pageForm = new SGPage(billTable,true).initPageBtn();
			listItem.setControls(pageForm);
			section3.setWidth("100%");
			section3.setHeight100();
			HStack stack3 = new HStack();
			stack3.setWidth("100%");
			stack3.setHeight("55%");
			stack3.addMember(section3);
			
			//创建按钮布局
			createBtnWidget2(toolStrip);
			be.addMember(toolStrip);
			
			be.addMember(stack);
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

 
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	private void getConfigList() {
		
        ListGridField INIT_NO = new ListGridField("INIT_NO", "对账单号", 130);
        INIT_NO.setCanEdit(false);
        ListGridField ID = new ListGridField("ID", "", 200);
        ID.setHidden(true);
        ID.setCanEdit(false);
        ListGridField ACCOUNT_STAT = new ListGridField("ACCOUNT_STAT", "", 90);
        ACCOUNT_STAT.setHidden(true);
        ACCOUNT_STAT.setCanEdit(false);
        ListGridField BUSS_NAME = new ListGridField("BUSS_NAME", "客户", 110);
        BUSS_NAME.setCanEdit(false);
        ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属账期", 80);
        BELONG_MONTH.setCanEdit(false);
        ListGridField INIT_AMOUNT = new ListGridField("INIT_AMOUNT", "月初金额", 100);
        INIT_AMOUNT.setCanEdit(false);
        ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT", "本次调前金额", 100);
        INITITAL_AMOUNT.setCanEdit(false);
        ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT", "本次调整金额", 90);
        ADJ_AMOUNT.setCanEdit(false);
        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT", "本次确认金额", 100);
        CONFIRM_AMOUNT.setCanEdit(false);
        ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "税金", 90);
        TAX_AMOUNT.setCanEdit(true);
        ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT", "确认金额（不含税）", 120);
        SUBTAX_AMOUNT.setCanEdit(false);
        ListGridField ACCOUNT_STAT_NAME = new ListGridField("ACCOUNT_STAT_NAME", "对账状态",100);
        ACCOUNT_STAT_NAME.setCanEdit(false);
        
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
        
        table.setFields(ACCOUNT_STAT,ID,INIT_NO, BUSS_NAME,BELONG_MONTH,INIT_AMOUNT, INITITAL_AMOUNT,ADJ_AMOUNT, CONFIRM_AMOUNT,TAX_AMOUNT, SUBTAX_AMOUNT,ACCOUNT_STAT_NAME);

        table.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord record=table.getSelectedRecord();
                //Record  rec=event.getRecord();
                //System.out.println(rec.getAttribute("ACCOUNT_STAT"));
				if(record!=null){				
					if(record.getAttribute("ACCOUNT_STAT")!=null){				
						if(record.getAttribute("ACCOUNT_STAT").equals("20")){				
							confButton.setDisabled(true);
							//confCanButton.setDisabled(false);	
							addButton.setDisabled(true);
							//removeButton.setDisabled(true);
							generateAdjButton.setDisabled(true);
						}			
						else if(record.getAttribute("ACCOUNT_STAT").equals("10")){					
							//confCanButton.setDisabled(true);	
							confButton.setDisabled(false);
//							addButton.setDisabled(false);
//							removeButton.setDisabled(false);
//							generateAdjButton.setDisabled(false);
							if(("Y").equals(record.getAttribute("HOLD_FLAG"))){
								addButton.setDisabled(true);
								//removeButton.setDisabled(true);
								generateAdjButton.setDisabled(true);
							}
							else if(("N").equals(record.getAttribute("HOLD_FLAG"))){
								addButton.setDisabled(false);
								//removeButton.setDisabled(false);
								generateAdjButton.setDisabled(false);
							}							
						}				
					}								
					if(record.getAttribute("INVOICE_FLAG")!=null){					
						if(record.getAttribute("INVOICE_FLAG").equals("Y")){
							generateReqButton.setDisabled(true);					
						}else{				
							generateReqButton.setDisabled(false);
						}		
					}	
				}		
			}			
		});	
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.RecInitBill_P0);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new DynamicForm();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
      //添加费用
        addButton = createUDFBtn("添加费用",StaticRef.ICON_COPY,SettPrivRef.RecInitBill_P0_01);
        addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelection().length==1){
					if(table.getSelectedRecord().getAttribute("ACCOUNT_STAT").equals("20")){
            			SC.say("该记录已经确认对账");
            			return;
            		}
					getShpTable().show();	
				}else if(table.getSelection().length==0){
					SC.say("请先勾选期初账单！");
				}else if(table.getSelection().length>1){
					SC.say("只允许对单个期初单添加费用！");
				}
			}
		});
        
        IButton saveButton1 = createUDFBtn("保存",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P0_08);
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
        
        IButton saveButton = createUDFBtn("保存明细",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P0_02);
        saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getRecords()==null) return;
				if(unshpmlstTable==null){
					SC.say("请展开明细");
					return;
				}else if(unshpmlstTable.getRecords()==null){
					return;
				}else{
					int[] record = unshpmlstTable.getAllEditRows();
					String n="";
					for(int j=0;j<record.length;j++){
						n=n+record[j]+",";
					}
					ListGridRecord[] records=unshpmlstTable.getRecords();
					ArrayList<String> sqlList = new ArrayList<String>();
					StringBuffer sf = new StringBuffer();
					int a=0;
					for(int i = 0; i < records.length; i++) {
						Record rec = records[i];
						if(n.contains(i+",")){
							String rea = unshpmlstTable.getEditedRecord(record[a]).getAttribute("ADJ_REASON");
							sf = new StringBuffer();
							sf.append("update BILL_REC_INITDETAILS set ADJ_REASON=");
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
						        unshpmlstTable.redraw();
							}else{
								MSGUtil.sayError(result.substring(2));
							}
						}
						
					});
				}
			}
		});
		//移除费用
        /*removeButton = createUDFBtn("移除费用",StaticRef.ICON_DEL,SettPrivRef.RecInitBill_P0_02);
        removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(unshpmlstTable!=null){
					if(unshpmlstTable.getRecords().length!=unshpmlstTable.getSelection().length){
						final ListGridRecord[] records=unshpmlstTable.getSelection();
						if(records == null || records.length == 0) {
							MSGUtil.sayError("请勾选费用信息进行操作");
							return;
						}
						SC.confirm("是否移除勾选的费用?", new BooleanCallback() {
							public void execute(Boolean value) {
			                    if (value != null && value) {
			                    	
			        				HashMap<String, String> id_list = new HashMap<String, String>(); 
			        				for(int i = 0; i < records.length; i++) {
			        					id_list.put(String.valueOf(i+1), records[i].getAttribute("ID"));
			        				}
			        				HashMap<String, Object> listMap = new HashMap<String, Object>();
			        				listMap.put("1", table.getSelectedRecord().getAttribute("INIT_NO"));
			        				listMap.put("2", id_list);
			        				listMap.put("5", LoginCache.getLoginUser().getUSER_ID());
			        				
			        				
			        				String json = Util.mapToJson(listMap);
			        				Util.async.execProcedure(json, "BMS_REC_DELFEE(?,?,?,?)", new AsyncCallback<String>() {
			        					
			        					@Override
			        					public void onSuccess(String result) {
			        						
			        						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
			        							table.collapseRecord(table.getSelectedRecord());
		    	        						
		    	        						final Criteria criteria = new Criteria();
		    	        						criteria.addCriteria("OP_FLAG","M");
		    	        						table.invalidateCache();
		    	        						table.fetchData(criteria);
		    	        						table.redraw();
		    	        						
		    	        						MSGUtil.showOperSuccess();
			        						
			        						}else{
			        							MSGUtil.sayError(result.substring(2));
			        						}
			        					}
			        					
			        					@Override
			        					public void onFailure(Throwable caught) {
			        						
			        					}
			        				});
			                    }
			                }
			            });
				
					}else{
					  SC.say("不允许移除所有的费用明细");
				  
					}
			
				}else{
					 SC.say("请展开期初账单，并勾选订单号再进行移除费用操作");
				}	
			}
		});*/
        
        
        
		//生成调整账单
        generateAdjButton = createUDFBtn("生成调整账单",StaticRef.ICON_EXPORT,SettPrivRef.RecInitBill_P0_03);
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
				Util.async.execProcedure(json, "BMS_REC_CREATE_ADJNO(?,?,?)", new AsyncCallback<String>() {
					
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
        confButton= createUDFBtn("确认对账",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P0_04);
        confButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final ListGridRecord []record=table.getSelection();
				ArrayList<String> sqlList = new ArrayList<String>();
				for(int i=0;i<record.length;i++){
					
					double inititalAmount=Double.parseDouble(ObjUtil.ifNull(record[i].getAttribute("ADJ_AMOUNT"),"0"));
					if(inititalAmount != 0){						
						SC.say("账单已发生调整，请先生成调整账单！");
						sqlList=null;
						return;
					}
					
					if(record[i].getAttribute("ACCOUNT_STAT").equals("20")){
						SC.say("该记录已经确认对账");
						sqlList=null;
	                    return;
	                    
					}else{
						String ID=record[i].getAttribute("ID");	
						String sql="update BILL_REC_INITIAL set ACCOUNT_STAT='20' where Id='"+ID+"'";				
				
						sqlList.add(sql);
						sql =" update BILL_REC_INITDETAILS set ACCOUNT_STAT = '20' where INIT_NO = '" + record[i].getAttribute("INIT_NO") + "'";
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
						fetchTable1();
						//removeButton.setDisabled(true);
					}
					
				});
		  
			}
		
        });
		//导出
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.RecInitBill_P0_07);
        expButton.setTitle("导出期初账单");
        expButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Util.RecInitExportUtil();
			}
		});
       
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton,addButton,saveButton1,saveButton,generateAdjButton,confButton,expButton);
	}
	
	public void createBtnWidget2(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.RecInitBill_P1);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin2 == null) {
					searchForm2 = new DynamicForm();
					searchWin2 = new SearchWin(lstDS2,
							createSerchForm2(searchForm2),section2.getSection(0)).getViewPanel();
				}
				else {
					searchWin2.show();
				}
			}
        	
        });
		
   //     取消对账
        confCanButton= createUDFBtn("取消对账",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P1_01);
        confCanButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				final ListGridRecord []record=unshpmlstTable2.getSelection();
				
				if(record!=null&&record.length>0){
					
					String recs="'"+record[0].getAttribute("INIT_NO")+"'";
					if(record.length>1){
					
						for(int k=1;k<record.length;k++){
							recs=recs+",'"+record[k].getAttribute("INIT_NO")+"'";
					
						}
					}
					
					Util.db_async.getRecord("INVOICE_FLAG", "BILL_REC_INITIAL", " where INIT_NO in ("+recs+")", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result.get(0).get("INVOICE_FLAG").equals("Y")){
								SC.say("账单已生成开票申请单，无法取消对账！");
								return;
							}
							
							ArrayList<String> sqlList = new ArrayList<String>();
							
							for(int i=0;i<record.length;i++){
								
								if(record[i].getAttribute("ACCOUNT_STAT").equals("10")){
								
									SC.say("该记录还没有确认对账");
								
									sqlList=null;
			                    
									return;
									
								}else{
							
									String ID=record[i].getAttribute("INIT_NO");
									String sql="update BILL_REC_INITIAL set ACCOUNT_STAT='10' where INIT_NO='"+ID+"'";
											
									sqlList.add(sql);
									
									sql =" update BILL_REC_INITDETAILS set ACCOUNT_STAT = '10' where ID = '" + record[i].getAttribute("ID") + "'";
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
							
						
						}
					
					});
					
				}
				
			}
		
        });
		//申请开票
        generateReqButton = createUDFBtn("申请开票",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P1_02);
        generateReqButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> no_list = new HashMap<String, String>(); //期初单号
				ListGridRecord [] records=unshpmlstTable2.getSelection();
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
				Util.async.execProcedure(json, "BMS_CONFIRM_INVOICE(?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//刷新数据
							unshpmlstTable2.discardAllEdits();
							unshpmlstTable2.invalidateCache();
							Criteria crit = unshpmlstTable2.getCriteria();
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
							unshpmlstTable2.fetchData(crit,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									
									//generateReqButton.setDisabled(true);
									Criteria findValues = new Criteria();
						            findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						            findValues.addCriteria("STATUS", "10");
						    		billTable.invalidateCache();
						    		billTable.fetchData(findValues);
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
        
        //加入申请单
        addtoInvoiceButton = createUDFBtn("加入申请单",StaticRef.ICON_SAVE,SettPrivRef.RecInitBill_P1_03);
        addtoInvoiceButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(billTable.getSelectedRecord() == null) {
					MSGUtil.sayError("请选择开票申请单");
					return;
				}
				HashMap<String, String> no_list = new HashMap<String, String>(); //期初单号
				ListGridRecord [] records=unshpmlstTable2.getSelection();
				if(records!=null&&records.length>0){
					for(int i=0;i<records.length;i++){
						no_list.put(String.valueOf(i+1), records[i].getAttribute("ID"));
					}
					
				}
				String loginUser = LoginCache.getLoginUser().getUSER_ID();
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				//list.add(shpm_No);
				listMap.put("1", no_list);
				listMap.put("2", billTable.getSelectedRecord().getAttribute("INVOICE_NO"));
				listMap.put("3",loginUser);
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_REC_ADDTO_INVOICE(?,?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							
							//刷新数据
							unshpmlstTable2.discardAllEdits();
							unshpmlstTable2.invalidateCache();
							Criteria crit = unshpmlstTable2.getCriteria();
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
							unshpmlstTable2.fetchData(crit,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									
									generateReqButton.setDisabled(true);
									Criteria findValues = new Criteria();
						            findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
						            findValues.addCriteria("STATUS", "10");
						    		billTable.invalidateCache();
						    		billTable.fetchData(findValues);
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
       
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton,confCanButton,generateReqButton,addtoInvoiceButton);
	}
	
	public void initVerify() {
	}
	
	//查询窗口
	public DynamicForm createSerchForm(final DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		SGText BUSS_NAME = new SGText("BUSS_NAME","客户");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BussWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		BUSS_NAME.setIcons(searchPicker);
		
		
		SGText BUSS_ID = new SGText("BUSS_ID","客户");
		BUSS_ID.setVisible(false);
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属账期");
		
		SGText ACCOUNT_STAT = new SGText("ACCOUNT_STAT","对账状态");
		ACCOUNT_STAT.setValue("10");
		ACCOUNT_STAT.setVisible(false);
		//Util.initComboValue(ACCOUNT_STAT, "BAS_CODES","CODE","NAME_C"," where prop_code='ACCOUNT_STAT'");
		
        form.setItems(BUSS_ID,BUSS_NAME,BELONG_MONTH,ACCOUNT_STAT);
        
        return form;
	}
	
	//查询窗口
	public DynamicForm createSerchForm2(final DynamicForm form){
		
		form.setDataSource(lstDS2);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		SGText BUSS_NAME = new SGText("BUSS_NAME","客户");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BussWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		BUSS_NAME.setIcons(searchPicker);
		
		
		SGText BUSS_ID = new SGText("BUSS_ID","客户");
		BUSS_ID.setVisible(false);
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属账期");
		
		SGText ACCOUNT_STAT = new SGText("ACCOUNT_STAT","对账状态");
		ACCOUNT_STAT.setValue("20");
		ACCOUNT_STAT.setVisible(false);
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户单号");
		SGText ODR_NO = new SGText("ODR_NO","托运单号",true);
		SGText INIT_NO = new SGText("INIT_NO","对账单号");
		
		SGCombo BILL_STATUS = new SGCombo("INVOICE_STAT","开票状态");
		Util.initComboValue(BILL_STATUS, "BAS_CODES", "CODE", "NAME_C", " prop_code='INVOICE_STS'", " order by show_seq asc","10");
		//Util.initComboValue(ACCOUNT_STAT, "BAS_CODES","CODE","NAME_C"," where prop_code='ACCOUNT_STAT'");
		
        form.setItems(BUSS_ID,BUSS_NAME,BELONG_MONTH,ACCOUNT_STAT,CUSTOM_ODR_NO,ODR_NO,INIT_NO,BILL_STATUS);
        
        return form;
	}
	
    private Window getShpTable() {
    	  	
    	final SGTable unshpmTable1;
    	unshpmTable1=new SGTable(RecInitBillDs, "100%", "100%", false, true, false);
		unshpmTable1.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		unshpmTable1.setCanEdit(false); 
		createUnshpmField(unshpmTable1);
	
		final SectionStack shpSectionStack =new SectionStack();	 	
		SectionStackSection shpsection=new SectionStackSection("费用信息");
		final DynamicForm unpageform=new SGPage(unshpmTable1, true).initPageBtn();		
		shpsection.setItems(unshpmTable1);
		shpsection.setExpanded(true);
		shpsection.setControls(unpageform);
		shpSectionStack.addSection(shpsection);
		final Window win=new Window();
		VLayout shpLay = new VLayout();
		final SGPanel form=new SGPanel();
		
		SGText CUSTOMER_CNAME = new SGText("CUSTOMER_CNAME","客户");
		CUSTOMER_CNAME.setValue(table.getSelectedRecord().getAttribute("BUSS_NAME"));
		CUSTOMER_CNAME.setDisabled(true);
//    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
//    	
//		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
//			
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//				 new CustomerWin(form,"20%","50%").getViewPanel();		
//			}
//		});
//       
//      CUSTOMER_CNAME.setIcons(searchPicker);
		
		SGText ODR_NO = new SGText("ODR_NO", "托运单号");
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", "客户单号");
		SGCombo FEE_TYPE = new SGCombo("FEE_TYPE", "费用类型");
		Util.initComboValue(FEE_TYPE,"BAS_CODES","NAME_C","NAME_C"," where prop_code='INITBILL_TYPE' ","");
        SGText BELONG_MONTH = new SGText("BELONG_MONTH", "所属账期");
		//SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", "收货方");
		
		SGButtonItem searchButton1=new SGButtonItem("查询",StaticRef.SAVE_BTN);
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
							if(unpageform != null) {
								unpageform.getField("CUR_PAGE").setValue("1");
								unpageform.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								unpageform.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
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
		//searchButton1.setTop(10);
		
		
		
		SGButtonItem confirmButton=new SGButtonItem("确定",StaticRef.SAVE_BTN);
		confirmButton.setWidth(80);
		confirmButton.setColSpan(2);
		confirmButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord().getAttribute("ACCOUNT_STAT").equals("20")){
					SC.say("对账单已做过对账确认，无法添加费用！");
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
					odr_list.put(String.valueOf(i+1), records[i].getAttribute("ODR_NO"));
					fee_list.put(String.valueOf(i+1), records[i].getAttribute("FEE_TYPE"));
				}
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				listMap.put("1", table.getSelectedRecord().getAttribute("INIT_NO"));
				listMap.put("2", id_list);
				listMap.put("3", odr_list);
				listMap.put("4", fee_list);
				listMap.put("5", LoginCache.getLoginUser().getUSER_ID());
				
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_REC_ADDFEE(?,?,?,?,?,?)", new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							win.destroy();
							table.collapseRecord(table.getSelectedRecord());
							final Criteria criteria = new Criteria();
							criteria.addCriteria("OP_FLAG","M");
							table.invalidateCache();
							table.fetchData(criteria);
							table.redraw();
						
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
		
		});
		
		
		
		
		//confirmButton.setTextAlign(Alignment.CENTER);
		//confirmButton.setWidth(FormUtil.Width);
		form.setItems(CUSTOMER_CNAME,ODR_NO,CUSTOM_ODR_NO,FEE_TYPE,BELONG_MONTH,searchButton1,confirmButton);
		form.setNumCols(8);
		form.setWidth("40%");
		form.setMargin(1);
		//form.setHeight("14%");
		shpLay.addMember(form);

		shpLay.addMember(shpSectionStack);		
		win.addItem(shpLay);
		win.setTitle("添加费用");
		win.setWidth("60%");
		win.setHeight("70%");
		win.setTop("20%");
		win.setLeft("25%");
		return win;
	}
	public void createUnshpmField(SGTable addTable){
		
		ListGridField FEE_TYPE = new ListGridField("FEE_TYPE", "费用类型", 80);
		ListGridField ODR_NO = new ListGridField("ODR_NO", "托运单号", 110);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", "客户单号", 90);
		//ListGridField LOAD_DATE = new ListGridField("LOAD_DATE", "发货日期", 120);
		//ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE", "收货日期", 120);
		//ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", "发货方", 120);
		//ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", "收货方", 120);
		//ListGridField VEHICLE_TYPE = new ListGridField("VEHICLE_TYPE", "车型", 60);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属账期", 80);
		ListGridField TYPE_NAME = new ListGridField("TYPE_NAME", "类型说明", 100);
		ListGridField AMOUNT = new ListGridField("AMOUNT", "应收金额（含税）", 100);
		ListGridField DESCR = new ListGridField("DESCR", "备注", 140);
		
		addTable.setFields(FEE_TYPE,BELONG_MONTH,CUSTOM_ODR_NO,ODR_NO,AMOUNT,TYPE_NAME,DESCR);
	}	
	
	private void createBillListField() {
		
		billTable.setShowRowNumbers(true);
		billTable.setCanEdit(false);
		
		ListGridField BUSS_ID = new ListGridField("BUSS_ID", "客户ID",100);
		BUSS_ID.setHidden(true);
		ListGridField BUSS_NAME = new ListGridField("BUSS_NAME", "客户名称",110);
		ListGridField BELONG_BUSS_NAME = new ListGridField("BELONG_BUSS_NAME","项目名称",110);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属期",60);
		ListGridField BILL_TO = new ListGridField("BILL_TO", "开票对象",120);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","应收金额",60);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "税金",60);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT", "应收金额（不含税）",110);
		ListGridField NOTES = new ListGridField("NOTES","备注",120);
		ListGridField INVOICE_NO = new ListGridField("INVOICE_NO","发票编号",100);
		INVOICE_NO.setHidden(true);
	
		billTable.setFields(BUSS_ID,INVOICE_NO,BUSS_NAME,BELONG_BUSS_NAME,BELONG_MONTH,BILL_TO,ACT_AMOUNT,
				TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);
	}
	
	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}	
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		RecInitBillView view = new RecInitBillView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;

	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
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
		unshpmlstTable2.discardAllEdits();
		unshpmlstTable2.invalidateCache();
		
		Criteria crit = unshpmlstTable2.getCriteria();
		if(crit == null) {
			crit = new Criteria();
		}
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ACCOUNT_STAT", "20");
		crit.addCriteria("INVOICE_STAT", "10");
		final Criteria crits = crit;
		unshpmlstTable2.fetchData(crits,new DSCallback() {

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
						unshpmlstTable2.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
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
		    		billTable.invalidateCache();
		    		billTable.fetchData(findValues);
				}
			}
			
		});
	}
}
