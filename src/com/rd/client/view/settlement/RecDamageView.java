package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.NewDmgInvoiceAction;
import com.rd.client.action.settlement.SaveDmgInvoiceAction;
import com.rd.client.common.action.DeleteFormAction;
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
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillRecDamageDS;
import com.rd.client.ds.settlement.DMGInvoiceDS;
import com.rd.client.ds.settlement.ReceLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BussWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
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
 * BMS-应收管理-货损赔偿单
 */
@ClassForNameAble
public class RecDamageView extends SGForm implements PanelFactory {
   private DataSource ds;
   private SGTable table;
   private Window searchWin;
   private SGPanel searchForm;
   private SectionStack section;
   public DynamicForm pageForm; 
   private IButton confirmButton ;
   private IButton cancelButton;
   public SGTable invoiceTable;
   public SGTable itemTable;
   private DataSource invoiceDS;
   private DataSource verifiDs;
   private SGPanel InvoiceForm;	
   public HashMap<String, IButton> ins_invo_btn;
   public HashMap<String, IButton> sav_invo_btn;
   public HashMap<String, IButton> del_invo_btn;
  // public IButton checkBut;
   public IButton delB ;
   public Window checkWin;
   public SGPanel checkForm;
   public IButton delBtn;
   public IButton checkBtn; 
   public TabSet mainTab;
	//页面的整体布局
	/* (non-Javadoc)
	 * @see com.rd.client.common.widgets.SGForm#getViewPanel()
	 */
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		ds = BillRecDamageDS.getInstance("BILL_REC_DAMAGE","BILL_REC_DAMAGE");
		invoiceDS = DMGInvoiceDS.getInstance("BILL_REC_DMGINVOICEINFO","BILL_REC_DMGINVOICEINFO");
		verifiDs =ReceLogDS.getInstance("BILL_REC_DMGRECELOG", "BILL_REC_DMGRECELOG");
		table=new SGTable(ds, "100%", "100%", false, false, false);
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createListFields();	
		
		section= new SectionStack();
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		section.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		
		createBtnWidget(toolStrip);
		
		Tab invTab = new Tab("发票信息");
		VLayout vlay1 = new VLayout();
		vlay1.setWidth100();
		vlay1.setHeight100();
        createInvoiceTable();
        HLayout hlout=new HLayout();  
        invoiceTable.setWidth("69%");
        invoiceTable.setHeight("100%");
        hlout.addMember(invoiceTable);     
        hlout.addMember(createInvoiceForm());
		vlay1.addMember(hlout);
		vlay1.addMember(createInvoBtn());
		invTab.setPane(vlay1);
		
		VLayout vlay = new VLayout();
		vlay.setWidth100();
		vlay.setHeight100();
		vlay.addMember(createVerifiTable());
		
		Tab verifi = new Tab("核销信息");
		verifi.setPane(vlay);
		mainTab = new TabSet();
		mainTab.setWidth100();
		mainTab.setHeight("50%");
		mainTab.setMargin(1);
		
		mainTab.addTab(invTab);
		mainTab.addTab(verifi);
		
		mainTab.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				int m_pageNum = event.getTabNum();
				if(table.getSelectedRecord()!=null){
				final Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("DAMAGE_NO",table.getSelectedRecord().getAttribute("DAMAGE_NO"));
				
				if(m_pageNum==1){
					itemTable.invalidateCache();
					itemTable.fetchData(criteria);	
				}
				
				if(m_pageNum==0){
					invoiceTable.invalidateCache();
					invoiceTable.fetchData(criteria);
				}
			  }
			}
		});
		
		
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		initVerify();
		layout.addMember(toolStrip);
		layout.addMember(section);
		layout.addMember(mainTab);
		return layout;
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
		ListGridField  COLLECTION_TIME = new ListGridField("COLLECTION_TIME","收款日期", 120);
			
		itemTable.setFields(INVOICE_NO,RECE_AMOUNT,RECE_BY,RECE_TIME,VOUCHER_NO,COLLECTION_TIME);

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

		ListGridField INVOICE_NUM = new ListGridField("INVOICE_NUM", "发票号", 110);
		//ListGridField INVOICE_NO = new ListGridField("INVOICE_NO", "申请单编号", 110);
		//INVOICE_NO.setHidden(true);
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
		invoiceTable.setFields(RECE_AMOUNT,INVOICE_NUM,INVOICE_TYPE,BILL_TO,FEE_NAME,ACT_AMOUNT,TAX_AMOUNT,TAX_RATIO,AMOUNT);
				
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
					//delBtn.setDisabled(true);				
				}
				if(recAmount1>0){
					delBtn.setDisabled(true);
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
				if(recAmount1>0){
					return;
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
	
	private ToolStrip createInvoBtn(){
		
		IButton newBtn =createBtn("新增",SettPrivRef.DAMAGE_P0_03);
        newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(60);
        newBtn.setAutoFit(true);
        newBtn.setAlign(Alignment.RIGHT);
        newBtn.addClickHandler(new NewDmgInvoiceAction(InvoiceForm,table,this));
        
        IButton saveBtn = createBtn("保存",SettPrivRef.DAMAGE_P0_04);
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		saveBtn.addClickHandler(new SaveDmgInvoiceAction(table,invoiceTable, InvoiceForm, check_map, this));
		
		delBtn = createBtn("删除",SettPrivRef.DAMAGE_P0_05);
        delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(60);
		delBtn.setAutoFit(true);
		delBtn.setAlign(Alignment.RIGHT);
		delBtn.addClickHandler(new DeleteFormAction(invoiceTable, InvoiceForm));
		
		IButton cancelBtn = createBtn("取消",SettPrivRef.DAMAGE_P0_06);
		cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(60);
		cancelBtn.setAutoFit(true);
		cancelBtn.setAlign(Alignment.RIGHT);
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				InvoiceForm.clearValues();
				ListGridRecord record = invoiceTable.getSelectedRecord();
				if(record != null){
					InvoiceForm.editRecord(record);			   
				}
				 initInvoiceBtn(1);
			}
		});
		checkBtn= createBtn("核销",SettPrivRef.DAMAGE_P0_07);	
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
		ins_invo_btn.put(SettPrivRef.DAMAGE_P0_03, newBtn);	
		
		del_invo_btn=new HashMap<String, IButton>();
		del_invo_btn.put(SettPrivRef.DAMAGE_P0_05, delBtn);
        
        sav_invo_btn=new HashMap<String, IButton>();
        sav_invo_btn.put(SettPrivRef.DAMAGE_P0_04, saveBtn);
        sav_invo_btn.put(SettPrivRef.DAMAGE_P0_06, cancelBtn);
        
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
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.DAMAGE);
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
		confirmButton= createUDFBtn("提交确认",StaticRef.CONFIRM_BTN,SettPrivRef.DAMAGE_P0_01);
		confirmButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()==null){
					MSGUtil.sayError("请选择记录！");
					return;
				}
				if(table.getSelectedRecord().getAttribute("STATUS").equals("20")){
					MSGUtil.sayError("该记录已提交！");
					return;
				}
				ArrayList<String> list= new ArrayList<String>();
				list.add(table.getSelectedRecord().getAttribute("DAMAGE_NO"));
				list.add(LoginCache.getLoginUser().getUSER_ID());
				Util.async.execProcedure(list, "BMS_REC_DAMAGE_COMMIT(?,?,?)", new AsyncCallback<String>() {
        			@Override
					public void onFailure(Throwable caught) {
							caught.printStackTrace();
					}
        			@Override
        			public void onSuccess(String result) {
        				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							table.getSelectedRecord().setAttribute("STATUS_NAME", "审批中");
							cancelButton.setDisabled(false);
							confirmButton.setDisabled(true);
						}
        				else {
        					MSGUtil.sayError(result.substring(2));         					
        				}
        				
        			}
        			
        		});
			}
		});
		
		cancelButton = createUDFBtn("取消确认",StaticRef.CANCEL_BTN,SettPrivRef.DAMAGE_P0_02);
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()==null){
					MSGUtil.sayError("请选择记录！");
					return;
				}
				if(table.getSelectedRecord().getAttribute("STATUS").equals("10")){
					MSGUtil.sayError("该记录还未提交！");
					return;
				}
				ArrayList<String> list= new ArrayList<String>();
				list.add(table.getSelectedRecord().getAttribute("DAMAGE_NO"));
				Util.async.execProcedure(list, "BMS_REC_DAMAGE_CANCEL(?,?)", new AsyncCallback<String>() {
        			@Override
					public void onFailure(Throwable caught) {
							caught.printStackTrace();
					}
        			@Override
        			public void onSuccess(String result) {
        				if(result.substring(0,2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							table.getSelectedRecord().setAttribute("STATUS_NAME", "待审批");
							cancelButton.setDisabled(true);
							confirmButton.setDisabled(false);
						}
        				else {
        					MSGUtil.sayError(result.substring(2));         					
        				}
        				
        			}
        			
        		});
			}
		});

		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,confirmButton,cancelButton);
		
		
		
	}
	
	private void createListFields(){
        
		final ListGridField DAMAGE_NO = new ListGridField("DAMAGE_NO","赔偿单号",120);
		ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID","客户名称",160);
		Util.initComboValue(CUSTOMER_ID, "BAS_CUSTOMER", "ID", "CUSTOMER_CNAME", " where ENABLE_FLAG='Y' ", "");
		ListGridField STATUS = new ListGridField("STATUS_NAME","审批状态",70);
		//Util.initComboValue(STATUS, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE='APPROVE_STS' ", "");
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",70);
		ListGridField TOTAL_AMOUNT = new ListGridField("TOTAL_AMOUNT","赔偿总金额",80);
		ListGridField COMPANY_AMOUNT = new ListGridField("COMPANY_AMOUNT","本公司承担金额",90);
		ListGridField INSUR_AMOUNT = new ListGridField("INSUR_AMOUNT","保险公司承担金额",110);
		ListGridField SUPLR_AMOUNT = new ListGridField("SUPLR_AMOUNT","承运商/司机承担金额",130);
		ListGridField STAFF_AMOUNT = new ListGridField("STAFF_AMOUNT","本公司员工承担金额",130);
		ListGridField DESCR = new ListGridField("DESCR","货损货差情况说明",150);

		table.setFields(DAMAGE_NO,CUSTOMER_ID,STATUS,BELONG_MONTH,TOTAL_AMOUNT,COMPANY_AMOUNT
				        ,INSUR_AMOUNT,SUPLR_AMOUNT,STAFF_AMOUNT,DESCR);

		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record=event.getRecord();
				if(record==null){
					return;
				}
				if(("10").equals(record.getAttribute("STATUS"))){
					confirmButton.setDisabled(false);
					cancelButton.setDisabled(true);
				}
				if(("20").equals(record.getAttribute("STATUS"))){
					confirmButton.setDisabled(true);
					cancelButton.setDisabled(false);
				}

			}
		});
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				  ListGridRecord record=table.getSelectedRecord();
					int tb=mainTab.getSelectedTabNumber();
					String damage_no=record.getAttribute("DAMAGE_NO");
					if(damage_no==null||damage_no.equals("")){
						return;
					}
					if(tb==0){
						invoiceTable.invalidateCache();
						Criteria cri=new Criteria();
						cri.addCriteria("OP_FLAG","M");
						cri.addCriteria("DAMAGE_NO",damage_no);
						invoiceTable.fetchData(cri);
					}else if(tb==1){
						itemTable.invalidateCache();
						Criteria cri=new Criteria();
						cri.addCriteria("OP_FLAG","M");
						cri.addCriteria("DAMAGE_NO",damage_no);
						itemTable.fetchData(cri);
					}
			 }
		});	
	}
	

	public DynamicForm createSerchForm(final SGPanel form) {
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
				
		final SGText BUSS_NAME = new SGText("BUSS_NAME","客户");
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
//		BUSS_NAME.addBlurHandler(new BlurHandler() {			
//			@Override
//			public void onBlur(BlurEvent event) {
//				//event.get
////				final String sup_name = ObjUtil.ifObjNull(event.(),"").toString();
////				if(sup_name.equals("")){
////					return;
////				}
//				final String customer=ObjUtil.ifObjNull(BUSS_NAME.getValue(),"").toString();
//				Util.db_async.getRecord("ID,CUSTOMER_CNAME", "BAS_CUSTOMER", 
//						" where upper(full_index) like upper('%"+customer+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//					
//					@Override
//					public void onSuccess(ArrayList<HashMap<String, String>> result) {
//						int size = result.size();
//						HashMap<String, String> selectRecord = null;
//						if(size > 1){
//							for (HashMap<String, String> hashMap : result) {
//								if(hashMap.get("ID").equals(""){
//									selectRecord = hashMap;
//									break;
//								}
//							}
//						}
//						if(size == 1 || selectRecord != null){
//							if(selectRecord == null)selectRecord = result.get(0);
//							groupTable.setEditValue(row, "SUPLR_ID", selectRecord.get("ID"));
//							groupTable.setEditValue(row, "SUPLR_NAME", selectRecord.get("SUPLR_CNAME"));
//						}else if(size > 1){
//							new SupplierWin(groupTable,itemRow,"20%", "32%",sup_name).getViewPanel();
//						}else if(size == 0){
//							MSGUtil.sayError("未找到承运商信息!");
//							groupTable.setEditValue(row, "SUPLR_ID", "");
//							groupTable.setEditValue(row, "SUPLR_NAME", "");
//						}
//						
//					}
//					
//					@Override
//					public void onFailure(Throwable caught) {
//						
//					}
//				});
//			}
//		});
		
		
		
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");	
		
		SGText DAMAGE_NO = new SGText("DAMAGE_NO","货损赔偿单号");
	
		SGCombo STATUS = new SGCombo("STATUS","审批状态",true);
		Util.initComboValue(STATUS, "BAS_CODES", "CODE", "NAME_C"," where PROP_CODE='APPROVE_STS' ");	
        
		form.setItems(BUSS_ID,BUSS_NAME,BELONG_MONTH,DAMAGE_NO,STATUS);
		
		return form;
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
				//final String invoiceNo=record1.getAttribute("INVOICE_NUM");
				final String damage_no=record1.getAttribute("DAMAGE_NO");
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
							MSGUtil.sayError("请填写凭证号码");
							return;
						}
						if(COLLECTION_TIME.getValue()==null||COLLECTION_TIME.getValue().equals("")){
							MSGUtil.sayError("请填写核销金额");
							return;
						}
					String proName = "BMS_DAMAGE_VERIFICATED(?,?,?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(record1.getAttribute("DAMAGE_NO"));
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
					            criteria.addCriteria("DAMAGE_NO",damage_no);
					           
					            invoiceTable.invalidateCache();
					            invoiceTable.fetchData(criteria);
					            invoiceTable.redraw();	
					          
//								Util.db_async.getSingleRecord("REC_STATUS,RECE_BY,RECE_TIME", " V_REC_INVOICE "," where INVOICE_NO='"+invoiceNo+"' ", null, new AsyncCallback<HashMap<String, String>>(){
//
//									@Override
//									public void onFailure(Throwable caught) {
//										
//									}
//
//									@Override
//									public void onSuccess(HashMap<String, String> result) {
//
//										if(result != null) {
//											ListGridRecord record=table.getSelectedRecord();
//											
//											record.setAttribute("REC_STATUS", result.get("REC_STATUS"));		
//											record.setAttribute("RECE_BY", result.get("RECE_BY"));
//											record.setAttribute("RECE_TIME", result.get("RECE_TIME"));
//											
//											table.redraw();
//											
//
//										}
//									}
//									
//								});
								if(mainTab.getSelectedTabNumber()==1){
									itemTable.fetchData(criteria);
								}
		
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
	
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	@Override
	public void initVerify() {
		check_map.put("TABLE", "BILL_REC_INVOICEINFO");
//		check_map.put("RECE_TIME", StaticRef.CHK_DATE+ "核销时间");
		check_map.put("INVOICE_TIME", StaticRef.CHK_DATE+ "开票时间");
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
		RecDamageView view = new RecDamageView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}
		
	

}
