package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.RecAuditDS;
import com.rd.client.ds.settlement.RecAuditdetailDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BussWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 费用管理-结算管理-待审批开票申请
 * @author cjt
 */
@ClassForNameAble
public class RecAuditInvoiceView extends SGForm implements PanelFactory {

	private DataSource recAuditDS;
	private DataSource detailDS;
	private SGTable table;
	public ListGrid itemTable;
	private SectionStack list_section;
	private Window searchWin;
	private Window checkWin;
	private SGPanel searchForm;
	public DynamicForm pageForm; 
	
	/*public RecAuditInvoiceView(String id) {
		super(id);
	}*/

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		recAuditDS = RecAuditDS.getInstance("BILL_REC_INVOICE1","BILL_REC_INVOICE");
		detailDS = RecAuditdetailDS.getInstance("BILL_REC_INVOICEDETAILS1","BILL_REC_INVOICEDETAILS");
		
		table = new SGTable(recAuditDS,"100%", "100%", false, false, false){
			//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {
				VLayout layout = new VLayout();
				
				itemTable = new ListGrid();
				itemTable.setDataSource(detailDS);
				itemTable.setWidth("97%");
				itemTable.setHeight(46);
				itemTable.setCellHeight(22);
				itemTable.setCanEdit(false);
				itemTable.setShowRowNumbers(false);
				itemTable.setAutoFitData(Autofit.VERTICAL);
				ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号",100);
				ListGridField ODR_NO = new ListGridField("ODR_NO","托运单号",110);
				ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","应收金额",90);
				ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",80);
				ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","应收金额（不含税）",110);
				ListGridField NOTES = new ListGridField("NOTES", "备注", 150);
				
				itemTable.setFields(CUSTOM_ODR_NO,ODR_NO,ACT_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("INVOICE_NO",record.getAttributeAsString("INVOICE_NO"));
				itemTable.fetchData(criteria);
				
				layout.addMember(itemTable);
				layout.setLayoutLeftMargin(35);
				
				return layout;
			};
		};
		createListField();
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);
		table.setCanEdit(false);
		
		list_section= new SectionStack();
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		list_section.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		
		doFetch();
		
		createBtnWidget(toolStrip);
		initVerify();
		main.addMember(toolStrip);
		main.addMember(list_section);
		
		return main;
	}

	private void createListField() {
		ListGridField BUSS_NAME = new ListGridField("BUSS_NAME","客户名称",100);
		ListGridField BELONG_BUSS_NAME = new ListGridField("BELONG_BUSS_NAME","项目名称",100);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",80);
		ListGridField BILL_TO = new ListGridField("BILL_TO","开票对象",130);
		ListGridField ACT_AMOUNT = new ListGridField("ACT_AMOUNT","应收金额",100);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",80);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","应收金额（不含税）",120);
		ListGridField LISTER = new ListGridField("LISTER","送审人",70);
		ListGridField LISTER_TIME = new ListGridField("LISTER_TIME","送审时间",120);
		ListGridField NOTES = new ListGridField("NOTES","备注",150);
		table.setFields(BUSS_NAME,BELONG_BUSS_NAME,BELONG_MONTH,BILL_TO,ACT_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,LISTER,LISTER_TIME,NOTES);
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setHeight("20");
		strip.setWidth("100%");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		strip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.RecAuditInvoice_P0);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(recAuditDS,createSerchForm(searchForm), list_section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
		IButton checkButton = createUDFBtn("审批",StaticRef.ICON_SAVE,SettPrivRef.RecAuditInvoice_P0_P01);
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()!=null){
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
		
		
		
		
		
		strip.setMembersMargin(4);
		strip.setMembers(searchButton,checkButton);
	}
	
	public DynamicForm createSerchForm(final SGPanel form) {
		form.setDataSource(recAuditDS);
		form.setAutoFetchData(false);
		
		SGText BUSS_NAME = new SGText("BUSS_NAME","客户名称");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new BussWin(form,"20%","50%").getViewPanel();		
			}
		});
		BUSS_NAME.setIcons(searchPicker);
		SGText BUSS_ID = new SGText("BUSS_ID","客户ID");
		BUSS_ID.setVisible(false);
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		SGText BILL_TO = new SGText("BILL_TO","开票对象");
//		SGCombo STATUS = new SGCombo("STATUS","审批状态");
//		Util.initCodesComboValue(STATUS,"APPROVE_STS");
//		
//		SGCombo BILL_STATUS = new SGCombo("BILL_STATUS","开票状态",true);
//		Util.initCodesComboValue(BILL_STATUS,"INVOICE_STS");
//		SGDateTime BILL_TIME_FROM = new SGDateTime("BILL_TIME_FROM","开票时间从");
//		SGDateTime BILL_TIME_TO = new SGDateTime("BILL_TIME_TO", "到");
//		
//		SGCombo REC_STATUS = new SGCombo("REC_STATUS","收款状态",true);
//		Util.initCodesComboValue(REC_STATUS,"RECE_STAT");
//		SGDateTime RECE_TIME_FROM = new SGDateTime("RECE_TIME_FROM","收款时间从");
//		SGDateTime RECE_TIME_TO = new SGDateTime("RECE_TIME_TO", "到");
		
		SGText ROLE_ID = new SGText("ROLE_ID","");
		//System.out.println(LoginCache.getLoginUser().getROLE_ID());
		if(!LoginCache.getLoginUser().getROLE_ID().equals("SUPER_MAN")){
			ROLE_ID.setValue(LoginCache.getLoginUser().getROLE_ID());
		}
		ROLE_ID.setVisible(false);
		
		form.setItems(BUSS_NAME,BUSS_ID,BELONG_MONTH,BILL_TO,ROLE_ID);
		return form;
	}
	
	public Window getCheckWin() {
		final Window win=new Window();
		SGPanel checkForm=new SGPanel();
		StaticTextItem APPROVER=new StaticTextItem("APPROVER","送审人");
		APPROVER.setWidth(FormUtil.Width);
		APPROVER.setValue(table.getSelectedRecord().getAttribute("LISTER"));
		
		final RadioGroupItem rgItem=new RadioGroupItem("STATUS","审批");
		rgItem.setStartRow(true);
		rgItem.setValueMap("同意","打回");
		rgItem.setColSpan("*");  
		rgItem.setRowSpan(1);
		rgItem.setVertical(false); 
		rgItem.setWidth("30%");
		rgItem.setDefaultValue("同意");
		
		StaticTextItem APPROVE_TIME = new StaticTextItem("APPROVE_TIME", "送审时间");
		APPROVE_TIME.setStartRow(true);
		APPROVE_TIME.setWidth(FormUtil.Width);
		APPROVE_TIME.setValue(table.getSelectedRecord().getAttribute("LISTER_TIME"));
		
		final TextAreaItem checknotes = new TextAreaItem("NOTES", "审批意见");
		checknotes.setStartRow(true);
		checknotes.setColSpan(6);
		checknotes.setHeight(70);
		checknotes.setWidth(FormUtil.longWidth);
		
		checkForm.setItems(APPROVER,APPROVE_TIME,rgItem,checknotes);
		checkForm.setIsGroup(true);  
		checkForm.setGroupTitle("赔偿单审批");
		checkForm.setMargin(3);
		checkForm.setPadding(20);
		checkForm.setHeight("70%");
		checkForm.setWidth("99%");
		checkForm.setLeft("10%");
		checkForm.setAlign(Alignment.CENTER);
		checkForm.setTitleSuffix(":");
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
				String status=rgItem.getValue().toString();
				if(status.equals("同意")){
					String INVOICE_NO = table.getSelectedRecord().getAttribute("INVOICE_NO");
					String proName = "BMS_REC_INVOICE_AUDITAGREE(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(INVOICE_NO);
					paramList.add(LoginCache.getLoginUser().getROLE_ID());
					paramList.add(LoginCache.getLoginUser().getUSER_ID());
					String note="";
					if(checknotes.getValue()!=null){
						note=checknotes.getValue().toString();
					}
					paramList.add(note);
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								MSGUtil.showOperSuccess();
								doFetch();
								table.redraw();
							}else{
								MSGUtil.sayError(result);
							}
						}
						
					});
				}else{
					String INVOICE_NO = table.getSelectedRecord().getAttribute("INVOICE_NO");
					String proName = "BMS_REC_INVOICE_AUDITBACK(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(INVOICE_NO);
					paramList.add(LoginCache.getLoginUser().getROLE_ID());
					paramList.add(LoginCache.getLoginUser().getUSER_ID());
					String note="";
					if(checknotes.getValue()!=null){
						note=checknotes.getValue().toString();
					}
					paramList.add(note);
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								MSGUtil.showOperSuccess();
								doFetch();
								table.redraw();
							}else{
								MSGUtil.sayError(result);
							}
						}
					});
				}
                win.hide();
			}
		});
		
		recivetoolStrip.setMembers(saveButton,cancelButton);
		
		win.addItem(recivetoolStrip);
		win.setTitle("审批操作"); 
		win.setWidth("38%");
		win.setHeight("50%");
		win.setTop("20%");
		win.setLeft("40%");
		return win;  
	}
	
	public void doFetch(){
		table.invalidateCache();
		final Criteria criteria= new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("STATUS","20");
		if(!LoginCache.getLoginUser().getROLE_ID().equals("SUPER_MAN")){
			criteria.addCriteria("ROLE_ID",LoginCache.getLoginUser().getROLE_ID());
		}
		table.fetchData(criteria, new DSCallback() {

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
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
		if(checkWin!=null){
			checkWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		RecAuditInvoiceView view = new RecAuditInvoiceView();
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
