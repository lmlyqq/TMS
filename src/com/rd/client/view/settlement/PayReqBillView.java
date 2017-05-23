package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.DeletePayReqAction;
import com.rd.client.action.settlement.NewPayReqBillAction;
import com.rd.client.action.settlement.NewPayReqInvoiceAction;
import com.rd.client.action.settlement.PayReqExportAction;
import com.rd.client.action.settlement.SavePayInvoiceAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.PayInitdetailDS;
import com.rd.client.ds.settlement.PayInvoiceInfoDS;
import com.rd.client.ds.settlement.PayLogDS;
import com.rd.client.ds.settlement.PayReqBillDS;
import com.rd.client.ds.settlement.PayReqBillExaDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrPayWin;
import com.rd.client.win.SuplrWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 费用管理---结算管理---应付请款单
 */
@ClassForNameAble
public class PayReqBillView extends SGForm implements PanelFactory {
   private DataSource ds;
   private DataSource itemDS;
   private DataSource invoiceDS;
   private DataSource checkDS;
   private DataSource exaDS;
   private SGTable table;
   private SGPanel mainform;
   private Window searchWin;
   private SGPanel searchForm;
   private SectionStack section;
   public DynamicForm pageForm; 
   private IButton confimButton;
   private IButton saveButton;
   private IButton canConfimButton;
   private SGTable itemTable;
   private SGTable invoiceTable;
   private SGTable checkTable;
   private SGTable exatable;
   private SGPanel InvoiceForm;	
   public HashMap<String, IButton> ins_invo_btn;
   public HashMap<String, IButton> sav_invo_btn;
   public HashMap<String, IButton> del_invo_btn;
   public Window checkWin;
   public SGPanel checkForm;
   public IButton checkBtn; 
   public IButton delBtn;
   private TabSet mainTab;
   private DynamicForm pageForm1;
   private DynamicForm pageForm2;
   public HashMap<String, String> check_invo_map;
   private int m_pageNum=0;
   /*public PayReqBillView(String id) {
	   super(id);
   }*/
   
	//页面的整体布局
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		initVerify();
		ds = PayReqBillDS.getInstance("V_PAY_REQUEST","BILL_PAY_REQUEST");
		itemDS= PayInitdetailDS.getInstance("V_PAY_REQDETAILS","V_PAY_REQDETAILS");
		invoiceDS=PayInvoiceInfoDS.getInstance("BILL_PAY_INVOICEINFO","BILL_PAY_INVOICEINFO");
		checkDS=PayLogDS.getInstance("BILL_PAY_PAYLOG","BILL_PAY_PAYLOG");
		exaDS=PayReqBillExaDS.getInstance("SYS_APPROVE_LOG2");
		
		table=new SGTable(ds, "100%", "100%", false, false, false);
		table.setCanEdit(true);
		table.setShowFilterEditor(false);
		//table.setCanExpandRecords(true);		
		createListFields();	
		table.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(table.getSelectedRecord() == null)return;
				OP_FLAG = "M";
				initSaveBtn();				
				mainform.editRecord(event.getRecord());
				mainform.setValue("OP_FLAG", "M");
				final Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("REQ_NO",table.getSelectedRecord().getAttributeAsString("REQ_NO"));
				
				int index=mainTab.getSelectedTabNumber();
				if(index==0){
					itemTable.invalidateCache();
					itemTable.fetchData(criteria, new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData,
								DSRequest request) {
							if(pageForm1 != null) {
								pageForm1.getField("CUR_PAGE").setValue("1");
								pageForm1.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm1.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));			
								//String sqlwhere = Cookies.getCookie("SQLWHERE");
							}		
						
						}
						
					});

				}
				else if(index==1){
					invoiceTable.invalidateCache();
					invoiceTable.fetchData(criteria);	
				}
				else if(index==2){
					checkTable.invalidateCache();
					checkTable.fetchData(criteria);	
				}
				else if(index==3){
					exatable.invalidateCache();
					exatable.fetchData(criteria, new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData,
								DSRequest request) {
							if(pageForm2 != null) {
								pageForm2.getField("CUR_PAGE").setValue("1");
								pageForm2.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm2.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));			
								//String sqlwhere = Cookies.getCookie("SQLWHERE");
							}		
						}
					});
				}
			}
			
		});

		
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddBtn();	
			}
		});
		section= new SectionStack();
		section.setWidth("70%");
		section.setHeight("100%");
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		section.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
				
		HLayout hOut = new HLayout();
		hOut.setWidth100();
		hOut.setHeight("60%");
		
		hOut.addMember(section);
		createMainForm();
		hOut.addMember(mainform);
		
		mainTab = new TabSet();
		mainTab.setWidth100();
		mainTab.setHeight100();
		mainTab.setMargin(1);
		
		if(isPrivilege(SettPrivRef.PayReqBill_P1)){
			createItemTable();
			SectionStack sectionStack1 = new SectionStack();
			SectionStackSection listItem1 = new SectionStackSection("明细列表");
			listItem1.setItems(itemTable);
			listItem1.setExpanded(true);
			sectionStack1.addSection(listItem1);
			pageForm1 = new SGPage(itemTable,true).initPageBtn();
			listItem1.setControls(pageForm1);
			sectionStack1.setWidth("100%");
			Tab item = new Tab("申请单明细");
			item.setPane(sectionStack1);
			mainTab.addTab(item);
		}
				
		if(isPrivilege(SettPrivRef.PayReqBill_P2)){
			VLayout vlay1 = new VLayout();
			vlay1.setWidth100();
			vlay1.setHeight100();
			Tab invTab = new Tab("发票信息");
	        createInvoiceTable();
	        HLayout hlout=new HLayout();  
	        invoiceTable.setWidth("69%");
	        invoiceTable.setHeight("100%");
	        hlout.addMember(invoiceTable);     
	        hlout.addMember(createInvoiceForm());
			vlay1.addMember(hlout);
			vlay1.addMember(createInvoBtn());
			invTab.setPane(vlay1);
			mainTab.addTab(invTab);
		}
		
		if(isPrivilege(SettPrivRef.PayReqBill_P3)){
			VLayout vlay = new VLayout();
			vlay.setWidth100();
			vlay.setHeight100();
			vlay.addMember(createVerifiTable());
			Tab verifi = new Tab("核销信息");
			verifi.setPane(vlay);
			mainTab.addTab(verifi);
		}
		
		if(isPrivilege(SettPrivRef.PayReqBill_P4)){
			createVeTable();
			SectionStack sectionStack2 = new SectionStack();
			SectionStackSection listItem2 = new SectionStackSection("审批日志");
			listItem2.setItems(exatable);
			listItem2.setExpanded(true);
			sectionStack2.addSection(listItem2);
			pageForm2 = new SGPage(exatable,true).initPageBtn();
			listItem2.setControls(pageForm2);
			sectionStack2.setWidth("100%");
			Tab item2 = new Tab("审批日志");
			item2.setPane(sectionStack2);
			mainTab.addTab(item2);
		}
		
		createBtnWidget(toolStrip);
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth("100%");
		layout.addMember(toolStrip);	
		layout.addMember(hOut);
		layout.addMember(mainTab);
		
		mainTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override	 
			public void onTabSelected(TabSelectedEvent event) {
				m_pageNum = event.getTabNum();
				if(table.getSelectedRecord()!=null){
				final Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("REQ_NO",table.getSelectedRecord().getAttribute("REQ_NO"));
				
				if(m_pageNum==0){
					itemTable.discardAllEdits();
					itemTable.invalidateCache();
					itemTable.fetchData(criteria, new DSCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void execute(DSResponse response, Object rawData,
								DSRequest request) {
							if(pageForm1 != null) {
								pageForm1.getField("CUR_PAGE").setValue("1");
								pageForm1.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm1.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
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
				
				if(m_pageNum==1){
					invoiceTable.discardAllEdits();
					invoiceTable.invalidateCache();
					invoiceTable.fetchData(criteria,new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {

						}
					      
					});			
				}
				
				if(m_pageNum==2){
					checkTable.discardAllEdits();
					checkTable.invalidateCache();
					checkTable.fetchData(criteria, new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
						
						}
					});
				}
				if(m_pageNum==3){
					exatable.discardAllEdits();
					exatable.invalidateCache();
					exatable.fetchData(criteria, new DSCallback() {

						@SuppressWarnings("unchecked")
						@Override
						public void execute(DSResponse response, Object rawData,
								DSRequest request) {
							if(pageForm2 != null) {
								pageForm2.getField("CUR_PAGE").setValue("1");
								pageForm2.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm2.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								String sqlwhere = Cookies.getCookie("SQLWHERE");
								if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
									exatable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
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
			  }
			}
		});
		
		return layout;
	}
	private ListGrid createItemTable(){
		itemTable = new SGTable(itemDS);
		itemTable.setCanExpandRecords(false);
		itemTable.setShowFilterEditor(false);
		itemTable.setCanEdit(false);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号", 110);
        ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 60);
        ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型", 70);
        ListGridField DRIVER = new ListGridField("DRIVER","司机",60);
        ListGridField MOBILE = new ListGridField("MOBILE","联系电话", 80);
        ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
        ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","到货日期", 70);
        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地", 80);
        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地", 80);
        ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额", 80);	
        itemTable.setFields(LOAD_NO,PLATE_NO,VEHICLE_TYP_ID_NAME,DRIVER,MOBILE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,CONFIRM_AMOUNT);
	    	
		return itemTable;
	}
	
	private ListGrid createVeTable(){
		exatable = new SGTable(exaDS);
		exatable.setCanExpandRecords(false);
		exatable.setShowFilterEditor(false);
		exatable.setCanEdit(false);
		ListGridField DOC_NO = new ListGridField("DOC_NO","单据编号",150);
		ListGridField ROLE_ID = new ListGridField("ROLE_ID","审核角色",150);
		ListGridField APPROVE_TIME = new ListGridField("APPROVE_TIME","审核时间",150);
		ListGridField APPROVER = new ListGridField("APPROVER","审核人",80);
		ListGridField APPROVER_RESULT = new ListGridField("APPROVER_RESULT","审核结果",100);
		ListGridField NOTES = new ListGridField("NOTES","审核意见",400);
		exatable.setFields(DOC_NO,ROLE_ID,APPROVE_TIME,APPROVER,APPROVER_RESULT,NOTES);
	    	
		return itemTable;
	}
	
	private ListGrid createInvoiceTable(){
		invoiceTable = new SGTable(invoiceDS);
		invoiceTable.setCanExpandRecords(false);
		invoiceTable.setShowFilterEditor(false);
		invoiceTable.setCanEdit(false);		
		invoiceTable.addRecordClickHandler(new RecordClickHandler() {			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				InvoiceForm.editRecord(event.getRecord());
				
			}
		});

		ListGridField INVOICE_NUM = new ListGridField("INVOICE_NUM", "发票号", 80);
		ListGridField INVOICE_BY = new ListGridField("INVOICE_BY", "开票人", 60);
		ListGridField INVOICE_TIME = new ListGridField("INVOICE_TIME", "开票时间", 90);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","开票金额", 70);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "税额", 60);
		ListGridField TAX_RATIO = new ListGridField("TAX_RATIO", "税率", 60);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT", "开票金额（不含税）", 100);
		ListGridField NOTES = new ListGridField("NOTES", "备注", 100);

		invoiceTable.setFields(INVOICE_NUM,INVOICE_BY,INVOICE_TIME,ACT_AMOUNT,TAX_AMOUNT,TAX_RATIO,SUBTAX_AMOUNT,NOTES);
				
		invoiceTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				initInvoiceBtn(4);
				
				String recAmount=event.getRecord().getAttribute("RECE_AMOUNT");
				if(recAmount==null){
					recAmount="0";
				}
				double recAmount1=Double.parseDouble(recAmount);
				
				String actAmount=event.getRecord().getAttribute("ACT_AMOUNT");
				if(actAmount==null){
					actAmount="0";
				}
				final double actAmount1=Double.parseDouble(actAmount);
		          
				if(checkForm!=null){
				
					checkForm.setValue("RECE_AMOUNT", actAmount1-recAmount1);
				
				}
				if(actAmount1==recAmount1){
					checkBtn.setDisabled(true);
					delBtn.setDisabled(true);
					
				}else{
					
					checkBtn.setDisabled(false);
					delBtn.setDisabled(false);
				}
				
			}
		});

		invoiceTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {			
				String recAmount=event.getRecord().getAttribute("RECE_AMOUNT");
				if(recAmount==null){
					recAmount="0";
				}
				double recAmount1=Double.parseDouble(recAmount);
				
				String actAmount=event.getRecord().getAttribute("ACT_AMOUNT");
				if(actAmount==null){
					actAmount="0";
				}
				final double actAmount1=Double.parseDouble(actAmount);
		          
				if(checkForm!=null){
				
					checkForm.setValue("RECE_AMOUNT", actAmount1-recAmount1);
				
				}
				if(actAmount1==recAmount1){
					initInvoiceBtn(1);
				}else{
					initInvoiceBtn(2);
				}
				
			}
		});
		
		return invoiceTable;
	}
		
	private ListGrid createVerifiTable(){
		checkTable = new SGTable(checkDS);
		checkTable.setCanExpandRecords(false);
		checkTable.setShowFilterEditor(false);
		checkTable.setCanEdit(false);

		ListGridField INVOICE_NO = new ListGridField("INVOICE_NUM","发票号", 120);
		ListGridField RECE_TIME = new ListGridField("RECE_TIME","核销时间", 120);
		ListGridField  VOUCHER_NO = new ListGridField("VOUCHER_NO","凭证号", 120);
		ListGridField RECE_AMOUNT = new ListGridField("RECE_AMOUNT","核销金额", 100);
		Util.initFloatListField(RECE_AMOUNT, StaticRef.PRICE_FLOAT);
		ListGridField RECE_BY = new ListGridField("RECE_BY","核销人", 120);
		ListGridField COLLECTION_TIME = new ListGridField("COLLECTION_TIME","收款日期", 100);
		checkTable.setFields(INVOICE_NO,RECE_TIME,RECE_AMOUNT,RECE_BY,VOUCHER_NO,COLLECTION_TIME);

		return checkTable;
	}
	//发票信息form
	private SGPanel createInvoiceForm(){
		
		InvoiceForm=new SGPanel();		
		SGText INVOICE_NUM=new SGText("INVOICE_NUM", "发票号");		
		SGText INVOICE_BY=new SGText("INVOICE_BY", "开票人");		
		SGDateTime INVOICE_TIME=new SGDateTime("INVOICE_TIME", "开票时间");
		INVOICE_TIME.setWidth(FormUtil.Width);
		SGDateTime INVOICE_RECTIME=new SGDateTime("INVOICE_RECTIME", "收到发票日期");
		INVOICE_RECTIME.setWidth(FormUtil.Width);
		SGText ACT_AMOUNT=new SGText("ACT_AMOUNT", "开票金额");
		SGText TAX_AMOUNT=new SGText("TAX_AMOUNT", "税额");
		SGText TAX_RATIO=new SGText("TAX_RATIO", "税率");	
		SGText SUBTAX_AMOUNT=new SGText("SUBTAX_AMOUNT", "开票金额（不含税）");
		SGText EXPRESS_NO=new SGText("EXPRESS_NO", "快递单号");
		SGText NOTES=new SGText("NOTES", "备注");
		NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
		NOTES.setColSpan(6);
		ACT_AMOUNT.addBlurHandler(new BlurHandler() {			
			@Override
			public void onBlur(BlurEvent event) {			
				String ACT_AMOUNT1=ObjUtil.ifObjNull(InvoiceForm.getValue("ACT_AMOUNT"),0).toString();
				String TAX_RATIO=ObjUtil.ifObjNull(InvoiceForm.getValue("TAX_RATIO"),0).toString();
			    double act_amount1=Double.parseDouble(ACT_AMOUNT1);
			    double tax_ratio=Double.parseDouble(TAX_RATIO);
			    if(act_amount1!=0&&tax_ratio!=0){			    	
			       double tax_amount=(act_amount1*(tax_ratio/100))/(1+tax_ratio/100);
			       NumberFormat nbf=NumberFormat.getFormat("#,##0.00");			      		        
			       String am=nbf.format(tax_amount);
			       InvoiceForm.setValue("TAX_AMOUNT",am);			           			   			       
			       double am1=Double.parseDouble(am);				   
			       double p=Math.round((act_amount1-am1)*100)*0.01d ;					       
				   String m=nbf.format(p);				       
				   InvoiceForm.setValue("SUBTAX_AMOUNT",m);
			    }			
			}
		});	
		InvoiceForm.setNumCols(8);
		InvoiceForm.setWidth("30%");
		InvoiceForm.setHeight("100%");
		InvoiceForm.setItems(INVOICE_NUM,INVOICE_BY,INVOICE_TIME,INVOICE_RECTIME,ACT_AMOUNT,TAX_AMOUNT,TAX_RATIO,SUBTAX_AMOUNT,EXPRESS_NO,NOTES);
		return InvoiceForm;
	}
	
	private ToolStrip createInvoBtn(){
		IButton newBtn =createBtn("新增",SettPrivRef.PayReqBill_P0_09);
        newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(60);
        newBtn.setAutoFit(true);
        newBtn.setAlign(Alignment.RIGHT);
        newBtn.addClickHandler(new NewPayReqInvoiceAction(InvoiceForm,table));
        newBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initInvoiceBtn(2);
			}
		});
        
        IButton saveBtn = createBtn("保存",SettPrivRef.PayReqBill_P0_10);
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SavePayInvoiceAction(table,invoiceTable, InvoiceForm, check_invo_map, this));
		
		delBtn = createBtn("删除",SettPrivRef.PayReqBill_P0_11);
        delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(60);
		delBtn.setAutoFit(true);
		delBtn.setAlign(Alignment.RIGHT);
		delBtn.addClickHandler(new DeleteFormAction(invoiceTable, InvoiceForm));
		
		IButton cancelBtn = createBtn("取消",SettPrivRef.PayReqBill_P0_12);
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new CancelFormAction(invoiceTable, InvoiceForm,this));
		cancelBtn.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				initInvoiceBtn(1);
			}
		});
		checkBtn= createBtn("核销",SettPrivRef.PayReqBill_P0_05);	
		checkBtn.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if(invoiceTable.getSelectedRecord()!=null){					
						if(searchWin != null) {
						searchWin.hide();					
						}				
						if(checkWin == null) {						
							checkWin=getCheckWin();						
							checkWin.show();					
						}					
						else {						
							checkWin.show();				
						}									
					}else{					
						SC.say("请选择记录");				
					}				
				}	        	
	        });
		
		
		ins_invo_btn=new HashMap<String, IButton>();
		ins_invo_btn.put(SettPrivRef.Invoice_P0_05, newBtn);	
		
		del_invo_btn=new HashMap<String, IButton>();
		del_invo_btn.put(SettPrivRef.Invoice_P0_07, delBtn);
        
        sav_invo_btn=new HashMap<String, IButton>();
        sav_invo_btn.put(SettPrivRef.Invoice_P0_06, saveBtn);
        sav_invo_btn.put(SettPrivRef.Invoice_P0_08, cancelBtn);
        
        this.enableOrDisables(ins_invo_btn, true);
        enableOrDisables(del_invo_btn, false);
        this.enableOrDisables(sav_invo_btn, false);
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(newBtn,saveBtn,delBtn,cancelBtn,checkBtn); 
	    
	    return toolStrip;
	}
	
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setHeight("20");
		toolStrip.setWidth("100%");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,"");
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds,createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		IButton addButton = createBtn("新增",SettPrivRef.PayReqBill_P0_08);
		addButton.addClickHandler(new NewPayReqBillAction(mainform, cache_map,this));
		addButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				mainform.getField("SUPLR_NAME").setDisabled(false);
				mainform.getField("BELONG_MONTH").setDisabled(false);
				mainform.getField("PRE_AMOUNT").setDisabled(false);
			}
		});
		IButton delButton = createBtn("删除",SettPrivRef.PayReqBill_P0_06);
		delButton.addClickHandler(new DeletePayReqAction(table, mainform));
		
	    saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.PayReqBill_P0_01);
	    saveButton.addClickHandler(new SaveFormAction(table, mainform, check_map, this));

	    IButton canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.PayReqBill_P0_07);
        canButton.addClickHandler(new CancelFormAction(table, mainform,this));
	    
//		saveButton.addClickHandler(new ClickHandler() {
//			
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onClick(ClickEvent event) {
//				
//				ArrayList<String> sqlList = new ArrayList<String>();
//				
//				Record record=table.getSelectedRecord();
//				if(record!=null){
//				Map<String,String> map=table.getEditValues(record);
//				String ID=record.getAttribute("ID");
//				String NOTES= map.get("NOTES");
//				String BILL_NO=map.get("BILL_NO");
//				String INVOICE_TIME=map.get("INVOICE_TIME");
//				String RECE_TIME=map.get("RECE_TIME");
//	
//				
//				String REQ_NO=record.getAttribute("REQ_NO");
//				String sql="update BILL_PAY_REQUEST set REQ_NO='"+REQ_NO+"'";
//				
//				if(BILL_NO!=null){					
//					sql=sql+",BILL_NO='"+BILL_NO+"'";
//				}
//
//				if(NOTES!=null){					
//					sql=sql+",NOTES='"+NOTES+"'";				
//				}
//
//				if(INVOICE_TIME!=null){		
//					sql=sql+",INVOICE_TIME=to_date('" + INVOICE_TIME.substring(0,10) + "','YYYY-MM-DD') ";
//				}
//				
//				if(RECE_TIME!=null){		
//					sql=sql+",RECE_TIME=to_date('" + RECE_TIME.substring(0,10) + "','YYYY-MM-DD') ";
//				}
//				sql=sql+" where ID='"+ID+"'";
//				
//				sqlList.add(sql);
//			
//				}
//				
//				if(countryGrid!=null){
//					
//				ListGridRecord[] listRecord=countryGrid.getRecords();
//
//			
//				for(int i=0;i<listRecord.length;i++){
//				
//					Record lstRecord=listRecord[i];
//				
//					Map<String,String> map=countryGrid.getEditValues(lstRecord);
//							
//					String ID=listRecord[i].getAttribute("ID");
//					String REQ_NO=listRecord[i].getAttribute("REQ_NO");
//					String DIFF_FEE=listRecord[i].getAttribute("DIFF_FEE");
//					String NOTES1=listRecord[i].getAttribute("NOTES");
//					String NOTES=map.get("NOTES");
//					if(DIFF_FEE!=null){
//					
//						if(NOTES==null&&NOTES1==null)
//										
//						{
//
//							sqlList=null;
//						
//							SC.say("亏损金额不为零，请补充说明");
//						
//							return;
//					
//						}else{
//							
//							String sql="update BILL_PAY_REQDETAILS set REQ_NO='"+REQ_NO+"'";
//							
//							if(NOTES!=null){								
//								sql=sql+",NOTES='"+NOTES+"'";							
//							}
//
//							sql=sql+" where ID='"+ID+"'";
//							
//							sqlList.add(sql);
//							
//						}	
//						
//					}				
//				}	
//				
//			}
//				
//				if(sqlList!=null){
//					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
//						
//						@Override
//						public void onFailure(Throwable caught) {
//							MSGUtil.sayError(caught.getMessage());
//						}
//
//						@Override
//						public void onSuccess(String result) {
//							if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
//								table.collapseRecord(table.getSelectedRecord());
//								MSGUtil.showOperSuccess();
//							}else{
//								MSGUtil.sayError("更新失败");
//							}
//							
//						}
//						
//					});
//					
//				}
//				
//			}
//		});
		IButton verificaButton = createBtn("核销",SettPrivRef.PayReqBill_P0_05);
		verificaButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
					String status=record.getAttribute("STATUS");
					System.out.println(status);
					if("审批完成".equals(status) && !"已核销".equals(record.getAttribute("PAY_STATUS"))){
						String sql="update BILL_PAY_REQUEST set PAY_STATUS='20'  where ID='"+record.getAttribute("ID")+"'";
						ArrayList<String> sqlList = new ArrayList<String>();
						sqlList.add(sql);
						if(sqlList!=null){
							Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
								
								@Override
								public void onFailure(Throwable caught) {
									MSGUtil.sayError(caught.getMessage());
								}

								@Override
								public void onSuccess(String result) {	
									if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
									    record.setAttribute("PAY_STATUS", "已核销");
									    table.redraw();
										MSGUtil.showOperSuccess();
									}else{
										MSGUtil.sayError("操作失败");
									}
								}
								
							});
							
						}
					}else{
						SC.say("该记录未审批完成或者已核销!");
					}
					
				}
			}
		});
		
		
		
		
		confimButton= createBtn("提交确认",SettPrivRef.PayReqBill_P0_02);
		confimButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				String REQ_NO = table.getSelectedRecord().getAttribute("REQ_NO");
				String proName = "BMS_PAY_REQUEST_CONFIRM(?,?,?,?)";
				ArrayList<String> paramList = new ArrayList<String>();
				paramList.add(REQ_NO);
				paramList.add(LoginCache.getLoginUser().getROLE_ID());
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
							table.getSelectedRecord().setAttribute("STATUS", "20");
							table.redraw();
							canConfimButton.setDisabled(false);
							confimButton.setDisabled(true);
						}else{
							MSGUtil.sayError(result);
						}
					}
					
				});
			}
		});
		canConfimButton = createBtn("取消确认",SettPrivRef.PayReqBill_P0_03);
		canConfimButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String REQ_NO = table.getSelectedRecord().getAttribute("REQ_NO");
				String proName = "BMS_PAY_REQUEST_CANCEL(?,?)";
				ArrayList<String> paramList = new ArrayList<String>();
				paramList.add(REQ_NO);
				Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)) {
							MSGUtil.showOperSuccess();
							table.getSelectedRecord().setAttribute("STATUS", "10");
							table.redraw();
							canConfimButton.setDisabled(true);
							confimButton.setDisabled(false);
						}else{
							MSGUtil.sayError(result);
						}
					}
					
				});
			}
			
		});
		//导出
		IButton exportButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.PayReqBill_P0_04);
		exportButton.addClickHandler(new PayReqExportAction(table, "addtime desc"));
		
		add_map.put(SettPrivRef.PayReqBill_P0_08, addButton);
	    del_map.put(SettPrivRef.PayReqBill_P0_06, delButton);
	    save_map.put(SettPrivRef.PayReqBill_P0_01, saveButton);
	    save_map.put(SettPrivRef.PayReqBill_P0_07, canButton);
	    this.enableOrDisables(add_map, true);
	    enableOrDisables(del_map, false);
	    this.enableOrDisables(save_map, false);
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,addButton,saveButton,delButton,canButton,confimButton,canConfimButton,exportButton);
		
		
		
	}
	
	private void createListFields(){
	   
		final ListGridField REQ_NO = new ListGridField("REQ_NO","请款单号",120);
		REQ_NO.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STATUS","审批状态",80);
		Util.initComboValue(STATUS,"BAS_CODES","CODE","NAME_C"," where prop_code='APPROVE_STS'","","");
		STATUS.setCanEdit(false);
		ListGridField PAY_STATUS = new ListGridField("PAY_STATUS","核销状态",80);
		Util.initComboValue(PAY_STATUS,"BAS_CODES","CODE","NAME_C"," where prop_code='PAY_STAT'","","");
		PAY_STATUS.setCanEdit(false);
		ListGridField BILL_STATUS = new ListGridField("BILL_STATUS","开票状态",80);
		Util.initComboValue(BILL_STATUS,"BAS_CODES","CODE","NAME_C"," where prop_code='INVOICE_STS'","","");
		BILL_STATUS.setCanEdit(false);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",110);
		SUPLR_NAME.setCanEdit(false);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",70);
		BELONG_MONTH.setCanEdit(false);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","应付金额",80);
		ACT_AMOUNT.setCanEdit(false);
		ListGridField PRE_AMOUNT = new ListGridField("PRE_AMOUNT","预付款",80);
		PRE_AMOUNT.setCanEdit(false);		
		ListGridField PAY_AMOUNT = new ListGridField("PAY_AMOUNT","实付金额",80);
		PAY_AMOUNT.setCanEdit(false);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",70);
		TAX_AMOUNT.setCanEdit(false);
		final ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","实付金额（不含税）",120);
		SUBTAX_AMOUNT.setCanEdit(false);
		ListGridField NOTES = new ListGridField("NOTES","摘要",160);
		NOTES.setCanEdit(false);
		ListGridField LATEST_PAY_TIME = new ListGridField("LATEST_PAY_TIME","最迟付款日期",80);
		LATEST_PAY_TIME.setCanEdit(false);
		table.setFields(REQ_NO,STATUS,BILL_STATUS,PAY_STATUS,SUPLR_NAME,BELONG_MONTH,
				ACT_AMOUNT,PRE_AMOUNT,PAY_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES,LATEST_PAY_TIME);

	}
	
	public DynamicForm createSerchForm(final SGPanel form) {
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
				
		SGText SUPLR_ID_NAME = new SGText("SUPLR_ID_NAME","承运商");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);  	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrWin(form,"20%","50%").getViewPanel();		
			}
		});      
		SUPLR_ID_NAME.setIcons(searchPicker);		
		SGText SUPLR_ID = new SGText("SUPLR_ID","客户");
		SUPLR_ID.setVisible(false);		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");	
		SGText ROLE_ID = new SGText("USER_ID","");
		ROLE_ID.setValue(LoginCache.getLoginUser().getUSER_ID());
		ROLE_ID.setVisible(false);
		SGCombo STATUS = new SGCombo("STATUS","审批状态");
		Util.initCodesComboValue(STATUS,"APPROVE_STS");
		SGText INVOICE_NUM = new SGText("INVOICE_NUM","发票号",true);
        form.setItems(ROLE_ID,SUPLR_ID_NAME,SUPLR_ID,BELONG_MONTH,STATUS,INVOICE_NUM);
		
		return form;
	}
	public void createMainForm(){
		
		mainform=new SGPanel();
		mainform.setWidth("30%");
		mainform.setHeight("100%");
		SGText SUPLR_NAME = new SGText("SUPLR_NAME","承运商");
		SUPLR_NAME.setDisabled(true);	
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);   	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {		
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrPayWin(mainform,"20%","50%").getViewPanel();		
			}
		});		
		SUPLR_NAME.setIcons(searchPicker);
		SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		BELONG_MONTH.setDisabled(true);	
		SGText PRE_AMOUNT = new SGText("PRE_AMOUNT","预付款");
		PRE_AMOUNT.setDisabled(true);	
		SGText ACT_AMOUNT = new SGText("ACT_AMOUNT","应付金额",true);
		SGText TAX_AMOUNT = new SGText("TAX_AMOUNT","税金");
		SGText SUBTAX_AMOUNT = new SGText("SUBTAX_AMOUNT","实付金额（不含税）");
		SGText NOTES = new SGText("NOTES","摘要",true);
		NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
		NOTES.setColSpan(6);
		mainform.setFields(SUPLR_NAME,SUPLR_ID,BELONG_MONTH,PRE_AMOUNT,ACT_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);
	}
	
	public Window getCheckWin() {
		final Window win=new Window();
		win.setWidth(400);
		checkForm=new SGPanel();

		final SGText RECE_AMOUNT=new SGText("RECE_AMOUNT","核销金额");
		final SGText VOUCHER_NO  = new SGText("VOUCHER_NO", "凭证号码",true);
		VOUCHER_NO.setColSpan(4);//设置单格
		VOUCHER_NO.setWidth(FormUtil.longWidth);
		final SGDate COLLECTION_TIME = new SGDate("COLLECTION_TIME", "收款日期");
		
		final ListGridRecord record=invoiceTable.getSelectedRecord();

		String recAmount=record.getAttribute("RECE_AMOUNT");
		if(recAmount==null){
			recAmount="0";
		}
		double recAmount1=Double.parseDouble(recAmount);
		
		String actAmount=record.getAttribute("ACT_AMOUNT");
		if(actAmount==null){
			actAmount="0";
		}
		final double actAmount1=Double.parseDouble(actAmount);
	
		RECE_AMOUNT.setValue(actAmount1-recAmount1);
		COLLECTION_TIME.setValue("");
		VOUCHER_NO.setValue("");
		checkForm.setItems(RECE_AMOUNT,COLLECTION_TIME,VOUCHER_NO);
		
		checkForm.setHeight("70%");
		checkForm.setWidth("99%");
		checkForm.setLeft("10%");

		win.addItem(checkForm);
		
		 
		
		ToolStrip recivetoolStrip = new ToolStrip();
		recivetoolStrip.setWidth("100%");
		recivetoolStrip.setHeight("20");
		recivetoolStrip.setPadding(2);
		recivetoolStrip.setSeparatorSize(12);
		recivetoolStrip.addSeparator();
		recivetoolStrip.setMembersMargin(4);
		recivetoolStrip.setAlign(Alignment.RIGHT);
		
		
		IButton cancelButton=createUDFBtn("取消", StaticRef.ICON_CANCEL,"");
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				win.hide();
			}
		});
		IButton saveButton=createUDFBtn("确定", StaticRef.ICON_SAVE,"");
	    saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord record1=invoiceTable.getSelectedRecord();
				final String req_no=record1.getAttribute("REQ_NO");
				String amount="0";
				if(RECE_AMOUNT.getValue()!=null){
					amount=RECE_AMOUNT.getValue().toString();
				}
				double amount1=Double.parseDouble(amount);
				
				if(actAmount1>=amount1){
					String rece_amount=record1.getAttribute("RECE_AMOUNT");
					double rece=Double.parseDouble(rece_amount);
					String act_amount=record1.getAttribute("ACT_AMOUNT");
					double act=Double.parseDouble(act_amount);
					if(((rece+amount1)<=act)&&(amount1>0))
					{
						if(VOUCHER_NO.getValue()==null||VOUCHER_NO.getValue().equals("")){
							SC.say("凭证号为空");
							return;
						}
						if(COLLECTION_TIME.getValue()==null||COLLECTION_TIME.getValue().equals("")){
							SC.say("核销日期为空");
							return;
						}
					String proName = "SP_PAY_VERIFICATED(?,?,?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(record1.getAttribute("REQ_NO"));
					paramList.add(record1.getAttribute("INVOICE_NUM"));
					paramList.add(VOUCHER_NO.getValue().toString());
					paramList.add(COLLECTION_TIME.getValue().toString());
					String user_id=LoginCache.getLoginUser().getUSER_ID();
					paramList.add(user_id);
					paramList.add(amount);
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								MSGUtil.showOperSuccess();

								Criteria criteria = new Criteria();
					            criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
					            criteria.addCriteria("REQ_NO",req_no);
					           
					            invoiceTable.invalidateCache();
					            invoiceTable.fetchData(criteria);
					            invoiceTable.redraw();	
					          
								Util.db_async.getSingleRecord("PAY_STATUS,PAY_BY,PAY_TIME", " BILL_PAY_REQUEST "," where REQ_NO='"+req_no+"' ", null, new AsyncCallback<HashMap<String, String>>(){

									@Override
									public void onFailure(Throwable caught) {
										
									}

									@Override
									public void onSuccess(HashMap<String, String> result) {

										if(result != null) {
											ListGridRecord record=table.getSelectedRecord();
											
											record.setAttribute("PAY_STATUS", result.get("PAY_STATUS"));		
//											record.setAttribute("RECE_BY", result.get("RECE_BY"));
//											record.setAttribute("RECE_TIME", result.get("RECE_TIME"));											
											table.redraw();
											

										}
									}
									
								});
								
//								itemTable.fetchData(criteria,new DSCallback() {
//									@Override
//									public void execute(DSResponse response, Object rawData, DSRequest request) {
//
//									}
//								      
//								});
		
							}else{
								MSGUtil.sayError(result);
							}
						}					
					});
					}else{
						
						SC.say("核销金额不能大于开票金额!");	
					}
				}else{
					SC.say("核销金额不能大于开票金额!");	
				}
                 win.hide();
			}
			
		});
		
		
		recivetoolStrip.setMembers(saveButton,cancelButton);
		
		win.addItem(recivetoolStrip);
		win.setTitle("请输入核销信息"); 
		win.setWidth("30%");
		win.setHeight("30%");
		win.setTop("40%");
		win.setLeft("35%");
		return win;  
	  }
	

	@Override
	public void createForm(DynamicForm form) {
		
	}

	
	@Override
	public void initVerify() {	
	
		check_invo_map=new HashMap<String, String>();
	
		check_invo_map.put("table", "BILL_PAY_INVOICEINFO");
	
		check_invo_map.put("INVOICE_TIME", StaticRef.CHK_DATE+ "开票时间");
		check_invo_map.put("INVOICE_RECTIME", StaticRef.CHK_DATE+ "收到发票日期");
		
		cache_map.put("STATUS", "10");
		cache_map.put("PAY_STATUS", "10");
		cache_map.put("BILL_STATUS", "10");
		
		check_map.put("TABLE", "BILL_PAY_REQUEST");		
	
	
	}

	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PayReqBillView view = new PayReqBillView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;

	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
		
	public void initInvoiceBtn(int typ){
		if(typ == 1){
			enableOrDisables(ins_invo_btn, true);
			enableOrDisables(sav_invo_btn, false);
			enableOrDisables(del_invo_btn, true);
		}else if(typ == 2){
			enableOrDisables(ins_invo_btn, false);
			enableOrDisables(sav_invo_btn, true);
			enableOrDisables(del_invo_btn, false);
		}else if(typ == 3){
			enableOrDisables(ins_invo_btn, false);
			enableOrDisables(sav_invo_btn, false);
			enableOrDisables(del_invo_btn, true);
		}else if(typ == 4){
			enableOrDisables(ins_invo_btn, true);
			enableOrDisables(sav_invo_btn, false);
			enableOrDisables(del_invo_btn, true);
		}else if(typ == 5){
			enableOrDisables(ins_invo_btn, false);
			enableOrDisables(sav_invo_btn, false);
			enableOrDisables(del_invo_btn, false);
		}
	}
	
}