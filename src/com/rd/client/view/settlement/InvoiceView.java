package com.rd.client.view.settlement;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.DeleteInvoiceAction;
import com.rd.client.action.settlement.NewInvoiceFormAction;
import com.rd.client.action.settlement.SaveInvoiceAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillSettleDS;
import com.rd.client.ds.settlement.PayReqBillExaDS;
import com.rd.client.ds.settlement.ReceLogDS;
import com.rd.client.ds.settlement.TransBillInvoiceDS;
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
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
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
 * 财务管理--结算管理--发票管理
 * @author fanglm 
 * @create time 2012-06-13 10:55
 *
 */
@ClassForNameAble
public class InvoiceView extends SGForm implements PanelFactory {
	
	public SGTable table;
	public SGTable itemTable;
	public SGTable invoiceTable;
	public ListGrid feeTable;
	private SGTable exatable;
	private Window searchWin;
//	private Window excelWin;
	public SGPanel searchForm = new SGPanel();
	private SectionStack sectionStack;
	private  SectionStackSection  listItem;
	private SectionStack sectionStack1;
	private  SectionStackSection  listItem1;
	private DataSource ds;	
	private SGPanel InvoiceForm;	
	public String load_no;
	public String odr_no;
	public String shpm_no;
	private DynamicForm pageForm;	
	private DynamicForm pageForm1;
	private DynamicForm pageForm2;
	public SGPanel feeInfo;	
	public SGTable headTable;
	public Record selectRecord;	
	public int pageNum = 0;
	private DataSource billDS;
	private DataSource invoiceDS;
	private DataSource verifiDs;
	private DataSource exaDS;
	private TabSet mainTab;
	
	//按钮权限
	public HashMap<String, IButton> ins_fee_btn;
	public HashMap<String, IButton> sav_fee_btn;
	public HashMap<String, IButton> del_fee_btn;
	
	//按钮权限
	public HashMap<String, IButton> ins_invo_btn;
	public HashMap<String, IButton> sav_invo_btn;
	public HashMap<String, IButton> del_invo_btn;
	public IButton checkBut;
	public IButton delB ;
	public Window checkWin;
	public SGPanel checkForm;
	public IButton checkBtn; 
	public IButton delBtn;
	private int m_pageNum=0;//全局页签数
	public HashMap<String, String> checkMap=new HashMap<String, String>();
	
	public  Canvas tileCanvas;
	private Canvas canvas;
	/*public InvoiceView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		ds = BillSettleDS.getInstance("BILL_REC_INVOICE","BILL_REC_INVOICE");
		billDS = TransBillReceDS.getInstance("BILL_REC_INVOICEDETAILS","BILL_REC_INVOICEDETAILS");
		invoiceDS = TransBillInvoiceDS.getInstance("BILL_REC_INVOICEINFO","BILL_REC_INVOICEINFO");
		verifiDs =ReceLogDS.getInstance("BILL_REC_RECELOG", "BILL_REC_RECELOG");
		exaDS=PayReqBillExaDS.getInstance("SYS_APPROVE_LOG_INV");
		
		//主布局
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
		
		// 左边列表
		table = new SGTable(ds, "100%", "100%", false, true, false);
		//table.setID("DDtable");
		createListField();
		table.setCanExpandRecords(false);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection("申请单列表");
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");

		HLayout hOut = new HLayout();
		hOut.setWidth100();
		hOut.setHeight("70%");
		
		
		VLayout layOut = new VLayout();
		layOut.setWidth("69%");
		layOut.setHeight("100%");
		layOut.addMember(sectionStack);
		
		VLayout layOut2 = new VLayout();
		layOut2.setWidth("30%");
		layOut2.setHeight("100%");
		layOut2.setAlign(Alignment.CENTER);
		createInfo();
		
		feeInfo.setHeight("40%");
		layOut2.addMember(feeInfo);
		DynamicForm exeform=new DynamicForm();
		exeform.setHeight("10%");
		layOut2.addMember(exeform);
		
		hOut.addMember(layOut);
		hOut.addMember(layOut2);
		
		mainTab = new TabSet();
		mainTab.setWidth100();
		mainTab.setHeight100();
		mainTab.setMargin(1);
		
		if(isPrivilege(SettPrivRef.Invoice_P1)){
			createfeeTable();
			sectionStack1 = new SectionStack();
			listItem1 = new SectionStackSection("明细列表");
			listItem1.setItems(feeTable);
			listItem1.setExpanded(true);
			sectionStack1.addSection(listItem1);
			pageForm1 = new SGPage(feeTable,true).initPageBtn();
			listItem1.setControls(pageForm1);
			sectionStack1.setWidth("100%");
			Tab item = new Tab("申请单明细");
			item.setPane(sectionStack1);
			mainTab.addTab(item);
		}
		
		if(isPrivilege(SettPrivRef.Invoice_P2)){
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

		if(isPrivilege(SettPrivRef.Invoice_P3)){
			VLayout vlay = new VLayout();
			vlay.setWidth100();
			vlay.setHeight100();
			vlay.addMember(createVerifiTable());
			Tab verifi = new Tab("核销信息");
			verifi.setPane(vlay);
			mainTab.addTab(verifi);
		}
		
		if(isPrivilege(SettPrivRef.Invoice_P4)){
			Tab tab2 = new Tab("发票图片");       
			tab2.setPane(createImgInfo());	       
			mainTab.addTab(tab2); 
		}

		if(isPrivilege(SettPrivRef.Invoice_P5)){
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
		
		mainTab.addTabSelectedHandler(new TabSelectedHandler() {

			@Override	 
			public void onTabSelected(TabSelectedEvent event) {
				m_pageNum = event.getTabNum();
				if(table.getSelectedRecord()!=null){
				final Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("INVOICE_NO",table.getSelectedRecord().getAttribute("INVOICE_NO"));
				
				if(m_pageNum==0){
					System.out.println("0");
					feeTable.invalidateCache();
					feeTable.fetchData(criteria, new DSCallback() {

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
				
				if(m_pageNum==2){
					itemTable.invalidateCache();
					itemTable.fetchData(criteria,new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {

						}
					      
					});			
				}
				
				if(m_pageNum==1){
					invoiceTable.invalidateCache();
					invoiceTable.fetchData(criteria, new DSCallback() {
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
						
						}
					});
				}
				if(m_pageNum==3){
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
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		main.addMember(toolStrip);
		main.addMember(hOut);
		main.addMember(mainTab);
		
		initVerify();
		
		return main;
	}
	
	private void createListField() {
		
		table.setShowRowNumbers(true);
		table.setCanEdit(false);
		
		ListGridField BUSS_ID = new ListGridField("BUSS_ID", "客户ID",100);
		BUSS_ID.setHidden(true);
		ListGridField BELONG_BUSS_NAME = new ListGridField("BELONG_BUSS_NAME", "客户名称",100);
		ListGridField BUSS_NAME = new ListGridField("BUSS_NAME","项目名称",100);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属期",60);
		ListGridField BILL_TO = new ListGridField("BILL_TO", "开票对象",100);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","应收金额",60);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "税金",60);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT", "应收金额（不含税）",110);
		ListGridField STATUS = new ListGridField("STATUS","审核状态",60);
		Util.initCodesComboValue(STATUS,"APPROVE_STS");
		ListGridField BILL_STATUS= new ListGridField("BILL_STATUS","开票状态",60);
		Util.initComboValue(BILL_STATUS, "BAS_CODES", "CODE", "NAME_C", " prop_code='INVOICE_STS'", " order by show_seq asc","");
		ListGridField REC_STATUS = new ListGridField("REC_STATUS","核销状态",60);
		//Util.initCodesComboValue(REC_STATUS,"BAS_CODES","CODE","NAME_C"," where PROP_CODE='RECE_STAT' ","","");
		Util.initComboValue(REC_STATUS, "BAS_CODES", "CODE", "NAME_C", " prop_code='RECE_STAT'", " order by show_seq asc","");
		ListGridField BILL_BY = new ListGridField("BILL_BY", "开票人",60);
		ListGridField BILL_TIME = new ListGridField("BILL_TIME1", "开票时间",100);
		ListGridField RECE_BY = new ListGridField("RECE_BY","核销人",60);
		ListGridField RECE_TIME= new ListGridField("RECE_TIME1","核销时间",100);
		ListGridField NOTES = new ListGridField("NOTES","备注",80);
		ListGridField INVOICE_NO = new ListGridField("INVOICE_NO","发票编号",100);
		INVOICE_NO.setHidden(true);
	
		table.setFields(BUSS_ID,INVOICE_NO,BELONG_BUSS_NAME,BUSS_NAME,BELONG_MONTH,BILL_TO,ACT_AMOUNT,
				TAX_AMOUNT,SUBTAX_AMOUNT,STATUS,BILL_STATUS,REC_STATUS,BILL_BY
                ,BILL_TIME,RECE_BY,RECE_TIME,NOTES);
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				initBtn(event.getRecord());
				 if("20".equals(event.getRecord().getAttribute("REC_STATUS"))){	
					enableOrDisables(del_map, false);
				}else{
					enableOrDisables(del_map, true);
				}
					
				 if(canvas != null){
					
					 if(canvas.isCreated()){
						canvas.destroy();
					}
				}
				canvas = new Canvas();
				tileCanvas.addChild(canvas);
				canvas.setHeight(163);
				canvas.setWidth(780);
				if(event.getRecord().getAttribute("ID") != null){
					ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.SETTLE_INVOICE_PREVIEW_URL,event.getRecord().getAttribute("ID"));
					action.getName();
				}
			}
		});

		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				 enableOrDisables(add_map, false);
			     enableOrDisables(del_map, false);
			     if("20".equals(event.getRecord().getAttribute("REC_STATUS"))){					
			    	 enableOrDisables(save_map, false);					
					}else{						
						enableOrDisables(save_map, true);							
					}
			     
			     
			}
		});
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				feeInfo.editRecord(event.getRecord());
				feeInfo.setValue("OP_FLAG", "M");
				final Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("INVOICE_NO",event.getRecord().getAttributeAsString("INVOICE_NO"));
				int index=mainTab.getSelectedTabNumber();
				if(index==0){
					feeTable.invalidateCache();
					feeTable.fetchData(criteria, new DSCallback() {

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
				}else if(index==4){
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
				

				
//				itemTable.fetchData(criteria,new DSCallback() {
//					
//					
//					@Override
//					public void execute(DSResponse response, Object rawData, DSRequest request) {
//					
//					
//					}
//				});
//				invoiceTable.fetchData(criteria, new DSCallback() {
//					
//					@Override
//					public void execute(DSResponse response, Object rawData, DSRequest request) {
//						
//						
//						
//						
//					}
//				});
				
				if("10".equals(event.getRecord().getAttribute("REC_STATUS"))){
				
					checkBut.setDisabled(false);
				
				}else{
					
					checkBut.setDisabled(true);
					
				}
				
				if("20".equals(event.getRecord().getAttribute("REC_STATUS"))){
					
					delB.setDisabled(true);
				
				}else{
					
					delB.setDisabled(false);
					
				}
				
				
			}
		
			
		});
	}
	
	private SGPanel createInfo(){
		/**
		 * 基本信息
		 * 
		 */
		feeInfo = new SGPanel();
		SGText BUSS_NAME = new SGText("BUSS_NAME","项目名称");
		BUSS_NAME.setDisabled(true);
		SGText BELONG_MONTH = new SGText("BELONG_MONTH", "所属期");	
		BELONG_MONTH.setDisabled(true);
		SGText BILL_TO = new SGText("BILL_TO","开票对象");		
		SGText ACT_AMOUNT = new SGText("ACT_AMOUNT","应收金额",true);		
		SGText TAX_AMOUNT = new SGText("TAX_AMOUNT", "税金");		
		SGText SUBTAX_AMOUNT = new SGText("SUBTAX_AMOUNT", "应收金额（不含税）");			
		TextAreaItem NOTES = new TextAreaItem("NOTES", "描述");
		NOTES.setStartRow(true);
		NOTES.setColSpan(6);
		NOTES.setHeight(40);
		NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
		NOTES.setTitleOrientation(TitleOrientation.TOP);
		NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		feeInfo.setNumCols(8);
		feeInfo.setTitleWidth(75);
		feeInfo.setItems(BUSS_NAME,BELONG_MONTH,BILL_TO,ACT_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);
		
		return feeInfo;
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
					searchWin = new SearchWin(ds,
							createSearchForm(searchForm), sectionStack.getSection(0)).getViewPanel();
					searchWin.setHeight(350);
				} else {
						searchWin.show();
				}
			}

		});
		
		IButton saveB = createBtn(StaticRef.SAVE_BTN,SettPrivRef.Invoice_P0_02);
		saveB.addClickHandler(new SaveFormAction(table, feeInfo,checkMap, this));
		
		delB = createBtn(StaticRef.DELETE_BTN,SettPrivRef.Invoice_P0_03);
		delB.addClickHandler(new DeleteInvoiceAction(table, feeInfo));
		
		IButton calB = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.Invoice_P0_04);
		calB.addClickHandler(new CancelFormAction(table, feeInfo));
		calB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				 enableOrDisables(add_map, true);
			     enableOrDisables(del_map, true);
			     enableOrDisables(save_map, false);	
			}
		});
		
		IButton commit = createBtn(StaticRef.CONFIRM_BTN,SettPrivRef.Invoice_P0_12);
		commit.setTitle("申请审批");
		commit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				String STATUS=table.getSelectedRecord().getAttribute("REC_STATUS");
				if("10".equals(STATUS)){
				String proName = "BMS_INVOICE_COMMIT(?,?,?)";		
				ArrayList<String> paramList = new ArrayList<String>();
				String INVOICE_NO=table.getSelectedRecord().getAttribute("INVOICE_NO");
				if(INVOICE_NO!=null){
				
					paramList.add(INVOICE_NO);		

					paramList.add(LoginCache.getLoginUser().getUSER_ID());	
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
						
					            table.invalidateCache();
					            
								table.fetchData(criteria);
								
								table.redraw();
							}else{					
							
								MSGUtil.sayError(result);				
						
							}			
					
						}					
				
					});	
			
					checkBut.setDisabled(true);
				}									
				else{
				
			
				}
				
				
			 }else{
				 
				 SC.say("该记录已确认过");
			 }
			}
			
		});
		IButton cancel = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.Invoice_P0_13);
		cancel.setTitle("取消申请");

		checkBut= createBtn("收款确认", SettPrivRef.Invoice_P0_10);
		checkBut.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String STATUS=table.getSelectedRecord().getAttribute("REC_STATUS");
				if("10".equals(STATUS)){
				String proName = "REC_INVOICE_CHECK(?,?,?)";		
				ArrayList<String> paramList = new ArrayList<String>();
				String INVOICE_NO=table.getSelectedRecord().getAttribute("INVOICE_NO");
				if(INVOICE_NO!=null){
				
					paramList.add(INVOICE_NO);		

					paramList.add(LoginCache.getLoginUser().getUSER_ID());	
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
						
					            table.invalidateCache();
					            
								table.fetchData(criteria);
								
								table.redraw();
							}else{					
							
								MSGUtil.sayError(result);				
						
							}			
					
						}					
				
					});	
			
					checkBut.setDisabled(true);
				}									
				else{
				
			
				}
				
				
			 }else{
				 
				 SC.say("该记录已确认过");
			 }
			}
		});
        del_map.put(SettPrivRef.Invoice_P0_03, delB);
        save_map.put(SettPrivRef.Invoice_P0_02, saveB);
        save_map.put(SettPrivRef.Invoice_P0_04, calB);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
		
        IButton exportButton = createBtn("导出",SettPrivRef.Invoice_P0_14);
        exportButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record==null){
					MSGUtil.sayError("请选择记录");
					return;
				}
				if(record.getAttribute("ID")==null){
					MSGUtil.sayError("该记录无效");
					return;
				}
				Util.InvoiceExportUtil(record.getAttribute("ID"));
			}
		});
        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,saveB,delB,calB,commit,cancel,checkBut,exportButton);
	}
	
	
	public DynamicForm createSearchForm(final SGPanel form){

		SGText BUSS_NAME = new SGText("BUSS_NAME","项目名称");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);  	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BussWin(form,"20%","50%").getViewPanel();		
			}
		});      
		BUSS_NAME.setIcons(searchPicker);		
		SGText BUSS_ID = new SGText("BUSS_ID","项目ID");
		BUSS_ID.setVisible(false);		
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		SGText BILL_TO = new SGText("BILL_TO","开票对象");
		SGCombo STATUS = new SGCombo("STATUS","审批状态");
		Util.initCodesComboValue(STATUS,"APPROVE_STS");
		
		SGCombo BILL_STATUS = new SGCombo("BILL_STATUS","开票状态",true);
		Util.initComboValue(BILL_STATUS, "BAS_CODES", "CODE", "NAME_C", " prop_code='INVOICE_STS'", " order by show_seq asc","");
		SGDateTime BILL_TIME_FROM = new SGDateTime("BILL_TIME_FROM","开票时间从");
		BILL_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime BILL_TIME_TO = new SGDateTime("BILL_TIME_TO","到");
		BILL_TIME_TO.setWidth(FormUtil.Width);
		
		SGCombo REC_STATUS = new SGCombo("REC_STATUS","核销状态",true);
		Util.initComboValue(REC_STATUS, "BAS_CODES", "CODE", "NAME_C", " prop_code='RECE_STAT'", " order by show_seq asc","");
		SGDateTime RECE_TIME_FROM= new SGDateTime("RECE_TIME_FROM","核销时间从");
		RECE_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime RECE_TIME_TO = new SGDateTime("RECE_TIME_TO","到");
		RECE_TIME_TO.setWidth(FormUtil.Width);

		form.setItems(BUSS_NAME,BUSS_ID,BELONG_MONTH,BILL_TO,STATUS,
				BILL_STATUS,BILL_TIME_FROM,BILL_TIME_TO,REC_STATUS,RECE_TIME_FROM,RECE_TIME_TO);
		
		return form;
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
	
	@Override
	public void createForm(DynamicForm form) {

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

		ListGridField INVOICE_NUM = new ListGridField("INVOICE_NUM", "发票号", 110);
		ListGridField INVOICE_NO = new ListGridField("INVOICE_NO", "申请单编号", 110);
		INVOICE_NO.setHidden(true);
		ListGridField INVOICE_TYPE = new ListGridField("INVOICE_TYPE","发票类型", 100);
		Util.initCodesComboValue(INVOICE_TYPE,"INVOICE_TYP");
		ListGridField BILL_TO = new ListGridField("BILL_TO", "开票单位", 150);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", "费用名称", 100);
		ListGridField AMOUNT = new ListGridField("AMOUNT", "金额（不含税）", 100);
		ListGridField TAX_RATIO = new ListGridField("TAX_RATIO", "税率", 60);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT", "税额", 70);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT", "金额（含税）", 80);
		ListGridField RECE_AMOUNT = new ListGridField("RECE_AMOUNT", "核销金额", 100);
		RECE_AMOUNT.setHidden(true);
		invoiceTable.setFields(INVOICE_NO,RECE_AMOUNT,INVOICE_NUM,INVOICE_TYPE,BILL_TO,FEE_NAME,ACT_AMOUNT,TAX_AMOUNT,TAX_RATIO,AMOUNT);
				
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
	//发票信息form
	private SGPanel createInvoiceForm(){
		
		InvoiceForm=new SGPanel();
		
		SGText INVOICE_NUM=new SGText("INVOICE_NUM", "发票号");		
		SGText BILL_TO=new SGText("BILL_TO", "发票抬头");		
		SGLText INVOICE_ADDRESS=new SGLText("INVOICE_ADDRESS", "地址电话");
		
		SGText TAX_NO=new SGText("TAX_NO", "税号",true);
		SGText BANK_ACCOUNT=new SGText("BANK_ACCOUNT", "银行账号");
		SGLText BANK_POINT=new SGLText("BANK_POINT", "开户银行");
		
		SGCombo INVOICE_TYPE=new SGCombo("INVOICE_TYPE", "发票类型",true);
		Util.initCodesComboValue(INVOICE_TYPE,"INVOICE_TYP");
		SGText FEE_NAME=new SGText("FEE_NAME", "费用名称");
		SGText AMOUNT=new SGText("AMOUNT", "金额（不含税）");
		SGText TAX_AMOUNT=new SGText("TAX_AMOUNT", "税额");
		
		SGText TAX_RATIO=new SGText("TAX_RATIO", "税率");
		SGText ACT_AMOUNT=new SGText("ACT_AMOUNT", "金额（含税）");
		ACT_AMOUNT.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
			
				String ACT_AMOUNT=ObjUtil.ifObjNull(InvoiceForm.getValue("ACT_AMOUNT"),0).toString();
				String TAX_RATIO=ObjUtil.ifObjNull(InvoiceForm.getValue("TAX_RATIO"),0).toString();
			    double act_amount1=Double.parseDouble(ACT_AMOUNT);
			    //System.out.println(act_amount1);
			    double tax_ratio=Double.parseDouble(TAX_RATIO);
			    //System.out.println(tax_ratio);
			    if(act_amount1!=0&&tax_ratio!=0){
			    	
			       double tax_amount=(act_amount1*(tax_ratio/100))/(1+tax_ratio/100);
			       //System.out.println(tax_amount);
			       double tax_amount1=Math.round(tax_amount * 100)*0.01d;
			       String a=Double.toString(tax_amount1);
//			       if(a.indexOf(".")>0){
			    	   
				      // String tax_amount2 = a.substring(0,a.indexOf(".")+3);
			           
			           String am=a;
			           if(a.substring(a.indexOf(".")).length()>3){
			        	  am=a.substring(0,(a.substring(0,a.indexOf(".")).length()+3));
			        	   InvoiceForm.setValue("TAX_AMOUNT",am);
			           }else{
			        	   InvoiceForm.setValue("TAX_AMOUNT", am);
			           }
				       //double amount=Double.parseDouble(tax_amount1);
				       double p=act_amount1-Double.parseDouble(am) ;
				      
				       String m=Double.toString(p);

				       InvoiceForm.setValue("AMOUNT",m);
//			       }else{
//			    	 
//			    	   double p= Math.round((act_amount1-tax_amount1) * 100)*0.01d;
//					      
//				       String m=Double.toString(p);
//				       String intNumber=m;
//				       if(m.indexOf(".")>0){
//				    	   intNumber = m.substring(0,m.indexOf(".")+3);
//				       }
//				       InvoiceForm.setValue("AMOUNT",intNumber);
//			    	   
//			       }
			    }
			
			}
		});
		SGText INVOICE_BY=new SGText("INVOICE_BY", "开票人");	
		SGDateTime INVOICE_TIME=new SGDateTime("INVOICE_TIME", "开票时间");	
		SGText EXPRESS_NO=new SGText("EXPRESS_NO", "快递单号",true);
		SGLText NOTES = new SGLText("NOTES", "备注");
		//NOTES.setWidth(FormUtil.longWidth+FormUtil.longWidth);
		
		InvoiceForm.setNumCols(8);
		InvoiceForm.setWidth("30%");
		InvoiceForm.setHeight("100%");
		InvoiceForm.setItems(INVOICE_NUM,BILL_TO,INVOICE_ADDRESS,TAX_NO,BANK_ACCOUNT,BANK_POINT,INVOICE_TYPE,
				FEE_NAME,INVOICE_BY,INVOICE_TIME,ACT_AMOUNT,TAX_AMOUNT,TAX_RATIO,AMOUNT,EXPRESS_NO,NOTES);
		return InvoiceForm;
	}

	private ListGrid createVerifiTable(){
		itemTable = new SGTable(verifiDs);
		itemTable.setCanExpandRecords(false);
		itemTable.setShowFilterEditor(false);
		itemTable.setCanEdit(false);

		ListGridField INVOICE_NO = new ListGridField("INVOICE_NUM","发票号", 120);
		ListGridField RECE_AMOUNT = new ListGridField("RECE_AMOUNT","核销金额", 100);
		Util.initFloatListField(RECE_AMOUNT, StaticRef.PRICE_FLOAT);
		ListGridField RECE_BY = new ListGridField("RECE_BY","核销人", 120);
		ListGridField RECE_TIME = new ListGridField("RECE_TIME","核销时间", 120);
		ListGridField  VOUCHER_NO = new ListGridField("VOUCHER_NO","凭证号", 120);
		ListGridField  COLLECTION_TIME = new ListGridField("COLLECTION_TIME","核销日期", 120);
		
		
		itemTable.setFields(INVOICE_NO,RECE_AMOUNT,RECE_BY,RECE_TIME,VOUCHER_NO,COLLECTION_TIME);

		return itemTable;
	}
	
	private ListGrid createfeeTable(){
		feeTable = new SGTable(billDS);
		feeTable.setCanExpandRecords(false);
		feeTable.setShowFilterEditor(false);
		feeTable.setCanEdit(false);
		
		
		ListGridField ODR_NO = new ListGridField("ODR_NO","订单号", 95);
        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号", 65);
        //ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 80);
        ListGridField VEHICLE_TYP_ID_NAME = new ListGridField("VEHICLE_TYP_ID_NAME","车型", 70);
        ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
        ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期", 70);
        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地", 90);
        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地", 90);
        ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","确认金额", 70);
		
		feeTable.setFields(ODR_NO,CUSTOM_ODR_NO,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,VEHICLE_TYP_ID_NAME,ACT_AMOUNT);
	    
		
		return feeTable;
	}
	
	private ToolStrip createInvoBtn(){
		IButton newBtn =createBtn("新增",SettPrivRef.Invoice_P0_05);
        newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(60);
        newBtn.setAutoFit(true);
        newBtn.setAlign(Alignment.RIGHT);
        newBtn.addClickHandler(new NewInvoiceFormAction(InvoiceForm,table));
        newBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initInvoiceBtn(2);
			}
		});
        
        IButton saveBtn = createBtn("保存",SettPrivRef.Invoice_P0_06);
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SaveInvoiceAction(table,invoiceTable, InvoiceForm, check_map, this));
		saveBtn.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					initInvoiceBtn(1);
				}
			});
		
		delBtn = createBtn("删除",SettPrivRef.Invoice_P0_07);
        delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(60);
		delBtn.setAutoFit(true);
		delBtn.setAlign(Alignment.RIGHT);
		delBtn.addClickHandler(new DeleteFormAction(invoiceTable, InvoiceForm));
		
		IButton cancelBtn = createBtn("取消",SettPrivRef.Invoice_P0_08);
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				invoiceTable.discardAllEdits();
				initInvoiceBtn(1);
			}
		});
		checkBtn= createBtn("核销",SettPrivRef.Invoice_P0_09);	
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
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	
	/**
	 * 调度单页签 费用按钮状态变化
	 * @author fangliangmeng
	 * @param typ
	 */
	public void initVerifiBtn(int typ){
		if(typ == 1){
			enableOrDisables(ins_fee_btn, true);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, false);
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
	
	public void initBtn(Record record){
		
		enableOrDisables(add_map, true);
		enableOrDisables(save_map, false);
		
		
		
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "BILL_REC_INVOICEINFO");
//		check_map.put("RECE_TIME", StaticRef.CHK_DATE+ "核销时间");
		check_map.put("INVOICE_TIME", StaticRef.CHK_DATE+ "开票时间");
	
		checkMap.put("TABLE", "BILL_REC_INVOICE");
		checkMap.put("LISTER_TIME",StaticRef.CHK_DATE+ "");
	}
	
	public Window getCheckWin() {
		final Window win=new Window();
		win.setWidth(400);
		checkForm=new SGPanel();

		final SGText RECE_AMOUNT=new SGText("RECE_AMOUNT","核销金额");
		final SGText VOUCHER_NO  = new SGText("VOUCHER_NO", "凭证号码",true);
		VOUCHER_NO.setColSpan(4);//设置单格
		VOUCHER_NO.setWidth(FormUtil.longWidth);
		final SGDate COLLECTION_TIME = new SGDate("COLLECTION_TIME", "核销日期");
		
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
				final String invoiceNo=record1.getAttribute("INVOICE_NO");
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
							return;
						}
						if(COLLECTION_TIME.getValue()==null||COLLECTION_TIME.getValue().equals("")){
							return;
						}
					String proName = "INVOICE_VERIFICATED(?,?,?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(record1.getAttribute("INVOICE_NO"));
					paramList.add(record1.getAttribute("INVOICE_NUM"));
					//System.out.println(record1.getAttribute("VOUCHER_NO"));
					paramList.add(VOUCHER_NO.getValue().toString());
					//System.out.println();
					paramList.add(COLLECTION_TIME.getValue().toString());
					//System.out.println(record.getAttribute("INVOICE_NUM"));
					String user_id=LoginCache.getLoginUser().getUSER_ID();
					paramList.add(user_id);
					//System.out.println(LoginCache.getLoginUser().getUSER_ID());
					paramList.add(amount);
					//System.out.println(amount);

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
					            criteria.addCriteria("INVOICE_NO",invoiceNo);
					           
					            invoiceTable.invalidateCache();
					            invoiceTable.fetchData(criteria);
					            invoiceTable.redraw();	
					          
								Util.db_async.getSingleRecord("REC_STATUS,RECE_BY,RECE_TIME", " V_REC_INVOICE "," where INVOICE_NO='"+invoiceNo+"' ", null, new AsyncCallback<HashMap<String, String>>(){

									@Override
									public void onFailure(Throwable caught) {
										
									}

									@Override
									public void onSuccess(HashMap<String, String> result) {

										if(result != null) {
											ListGridRecord record=table.getSelectedRecord();
											
											record.setAttribute("REC_STATUS", result.get("REC_STATUS"));		
											record.setAttribute("RECE_BY", result.get("RECE_BY"));
											record.setAttribute("RECE_TIME", result.get("RECE_TIME"));
											
											table.redraw();
											

										}
									}
									
								});
								
								itemTable.fetchData(criteria,new DSCallback() {
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {

									}
								      
								});
		
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
		win.setTitle("请输入核销金额"); 
		win.setWidth("30%");
		win.setHeight("30%");
		win.setTop("40%");
		win.setLeft("35%");
		return win;  
	  }
	
	private VLayout createImgInfo(){
		VLayout vLay = new VLayout();
        vLay.setWidth100();
        vLay.setBackgroundColor(ColorUtil.BG_COLOR);
        
        HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane
				.setContents("<iframe name='foo' style='position:absolute;width:0;height:0;border:0'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");
		
        final DynamicForm uploadForm = new DynamicForm();
        uploadForm.setAction("uploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
        
        final UploadItem imageItem = new UploadItem("image","图片");
        
        TextItem notes = new TextItem("NOTES","描述");
        
        SGButtonItem saveItem = new SGButtonItem("上传",StaticRef.ICON_IMPORT);
        setButtonItemEnabled(BasPrivRef.Complaint_P1_P0,saveItem,true);
        saveItem.setWidth(60);
        saveItem.setAutoFit(true);
        saveItem.setIcon(StaticRef.ICON_NEW);
        saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord() !=  null){
					if(table.getSelectedRecord().getAttribute("ID") != null){
						Map<String,String> map = uploadForm.getValues();
						if(map.get("image") != null){
							String customer = table.getSelectedRecord().getAttribute("ID").toString();
							String image = map.get("image").toString();
							new ImageUploadAction(customer,StaticRef.SETTLE_INVOICE_URL,image,uploadForm).onClick(event);	
							if(table.getSelectedRecord() != null){
								if(canvas != null){
									if(canvas.isCreated()){
										canvas.destroy();
									}
								}
								canvas = new Canvas();
								tileCanvas.addChild(canvas);
								canvas.setHeight(163);
								canvas.setWidth(1000);
							    if(table.getSelectedRecord().getAttribute("ID") != null){
									ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.SETTLE_INVOICE_PREVIEW_URL,table.getSelectedRecord().getAttribute("ID"));
									action.getName();
							    }
							}
						
						}else{
							MSGUtil.sayWarning("请选择所要上传的图片");
						}
					}
				} else {
					MSGUtil.sayWarning("请选择上传图片对应的货品编号.");
				}
			}
		});
        
       // 下载
        SGButtonItem downLoadButton = new SGButtonItem("下载",StaticRef.ICON_IMPORT);
        setButtonItemEnabled(BasPrivRef.Complaint_P1_P1,downLoadButton,true);
        downLoadButton.setWidth(60);
        downLoadButton.setAutoFit(true);
        downLoadButton.setIcon(StaticRef.ICON_NEW);
        downLoadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
				String cusId=record.getAttribute("ID");
				String filePath="test/INVOICE/"+cusId;
				doDownFile(filePath);
				}
				
			}
			
        });	
//        
        uploadForm.setItems(imageItem,notes,saveItem,downLoadButton);
        
        tileCanvas = new Canvas();
        tileCanvas.setBorder("1px solid black");
        tileCanvas.setHeight(200);  
        tileCanvas.setWidth100(); 
        tileCanvas.setShowResizeBar(true);
     
        vLay.setMembers(uploadForm,tileCanvas,htmlPane);
        
        return vLay;
	}
	
	
	public InvoiceView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		InvoiceView view = new InvoiceView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}

	public native static void doDownFile(String fileName)/*-{
		var url = $wnd.location.href;
		url = url.substring(0, url.lastIndexOf('/'));
		url = url+'/images/'+fileName;
		$wnd.open(url);
	}-*/;
}
