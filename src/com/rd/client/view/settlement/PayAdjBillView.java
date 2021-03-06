package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.PayAdjExportAction;
import com.rd.client.action.settlement.SavePayAdjBillAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.PayAdjBillDS;
import com.rd.client.ds.settlement.PayAdjBillExaDS;
import com.rd.client.ds.settlement.PayAdjBilldetailDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrPayWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 费用管理-结算管理-应付调整账单
 * @author cjt
 *
 */
@ClassForNameAble
public class PayAdjBillView extends SGForm implements PanelFactory{

	private DataSource payadjDS;
	private DataSource detailDS;
	private DataSource exaDS;
	private SGTable table;
	private SGTable exatable;
	public ListGrid itemTable;
	private SectionStack list_section;
	private SectionStack list_section2;
	private ValuesManager vm;
	private Window searchWin;
	private SGPanel searchForm;
	private IButton confButton;
	private IButton canfButton;
	public DynamicForm pageForm; 
	
	/*public PayAdjBillView(String id) {
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
		
		VStack stack =new VStack();
		stack.setHeight100();
		stack.setWidth100();
		
		TabSet TabSet = new TabSet(); 
		TabSet.setHeight100();
		TabSet.setWidth100();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		payadjDS = PayAdjBillDS.getInstance("BILL_PAY_ADJUST","BILL_PAY_ADJUST");
		detailDS = PayAdjBilldetailDS.getInstance("BILL_PAY_ADJDETAILS","BILL_PAY_ADJDETAILS");
		exaDS = PayAdjBillExaDS.getInstance("SYS_APPROVE_LOG","SYS_APPROVE_LOG");
		
		table = new SGTable(payadjDS,"100%", "100%", false, true, false){
			//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {
				VLayout layout = new VLayout();
				
				itemTable = new ListGrid() {  
		            @Override  
		            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {  
		                if (getFieldName(colNum).equals("ADJ_AMOUNT1")||getFieldName(colNum).equals("ADJ_AMOUNT2")) { 
		                	if(record.getAttribute("ADJ_AMOUNT1")!=null){
		                		if (!record.getAttribute("ADJ_AMOUNT1").equals("0")) {  
		                			return "font-weight:bold; color:#d64949;";  
		                		}else{  
				                	return super.getCellCSSText(record, rowNum, colNum);  
				                }   
		                	}
			                if(record.getAttribute("ADJ_AMOUNT2")!=null){
			                	if (!record.getAttributeAsInt("ADJ_AMOUNT2").equals("0")) {  
			                        return "font-weight:bold; color:#287fd6;";  
			                    }else{  
				                	return super.getCellCSSText(record, rowNum, colNum);  
				                }  
			                }
			                else{  
			                	return super.getCellCSSText(record, rowNum, colNum);  
			                } 
		                } else {  
		                    return super.getCellCSSText(record, rowNum, colNum);  
		                }  
		            }  
		        };
				
				itemTable.setDataSource(detailDS);
				itemTable.setWidth("99%");
				itemTable.setHeight(46);
				itemTable.setCellHeight(22);
				itemTable.setCanEdit(true);
				itemTable.setShowRowNumbers(true);
				itemTable.setAutoFitData(Autofit.VERTICAL);
				
				ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",105);
				LOAD_NO.setCanEdit(false);
				ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",60);
				PLATE_NO.setCanEdit(false);
				ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","车型",65);
				Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", "", "");
				VEHICLE_TYP_ID.setCanEdit(false);
				ListGridField DRIVER = new ListGridField("DRIVER","司机",50);
				DRIVER.setCanEdit(false);
				ListGridField MOBILE = new ListGridField("MOBILE","联系电话",70);
				MOBILE.setCanEdit(false);
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
				ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",65);
				INITITAL_AMOUNT.setCanEdit(false);
				ListGridField ADJ_AMOUNT1 = new ListGridField("ADJ_AMOUNT1","一次调整金额",75);
				ADJ_AMOUNT1.setCanEdit(false);
				ListGridField ADJ_REASON1 = new ListGridField("ADJ_REASON1","一次调整原因",75);
				Util.initCodesComboValue(ADJ_REASON1,"ADJ_REASON");
				ListGridField ADJ_AMOUNT2 = new ListGridField("ADJ_AMOUNT2","二次调整金额",75);
				ADJ_AMOUNT2.setCanEdit(false);
				ListGridField ADJ_REASON2 = new ListGridField("ADJ_REASON2","二次调整原因",75);
				Util.initCodesComboValue(ADJ_REASON2,"ADJ_REASON");
				
				itemTable.setFields(LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOBILE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INITITAL_AMOUNT,ADJ_AMOUNT1,ADJ_REASON1,ADJ_AMOUNT2,ADJ_REASON2);
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("ADJ_NO",record.getAttributeAsString("ADJ_NO"));
				itemTable.fetchData(criteria);
				
				layout.addMember(itemTable);
				layout.setLayoutTopMargin(0);
				layout.setLayoutLeftMargin(35);
				
				return layout;
			};
		};
		
		createListField();
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);
		table.setConfirmDiscardEdits(false);
		table.setCanEdit(true);
		table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		
		list_section = new SectionStack();
		SectionStackSection listItem = new SectionStackSection("调整单列表");
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.addSection(listItem);
	    list_section.setWidth("100%");
	    
	    VLayout lay=new VLayout();
    	if(isPrivilege(SettPrivRef.PayAdjBill_P0)) {
    		Tab unshpmTab = new Tab("调整单列表");
			unshpmTab.setPane(lay);
			TabSet.addTab(unshpmTab);
    	}
    	createBtnWidget(toolStrip);
    	lay.addMember(toolStrip);
    	lay.addMember(list_section);
    	
    	exatable=new SGTable(exaDS, "100%", "100%", false, true, false);
    	createVeField(exatable);
    	exatable.setShowFilterEditor(false);
    	exatable.setCanEdit(false);
    	
    	list_section2 = new SectionStack();
		SectionStackSection listItem2 = new SectionStackSection("审批日志");
	    listItem2.setItems(exatable);
	    listItem2.setExpanded(true);
	    listItem2.setControls(new SGPage(exatable, true).initPageBtn());
	    list_section2.addSection(listItem2);
	    list_section2.setWidth("100%");
	    pageForm = new SGPage(exatable,true).initPageBtn();
		listItem2.setControls(pageForm);
	    
	    VLayout lay1=new VLayout();
    	if(isPrivilege(SettPrivRef.PayAdjBill_P1)) {
    		
    		Tab unshpmTab1 = new Tab("审批日志");            
			unshpmTab1.setPane(lay1);
			TabSet.addTab(unshpmTab1);
    	}
    	lay1.addMember(list_section2);
		
	    table.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
					/*if(record.getAttribute("ADJ_AMOUNT")!=null && (record.getAttribute("ADJ_AMOUNT")!="0")){
						table.expandRecord(table.getSelectedRecord());
					}*/
					if(event.getRecord().getAttribute("STATUS").equals("10") || event.getRecord().getAttribute("STATUS").equals("15")){
						confButton.enable();
						canfButton.disable();
						enableOrDisables(save_map, true);
					}else if(event.getRecord().getAttribute("STATUS").equals("20")){
						canfButton.enable();
						confButton.disable();
						enableOrDisables(save_map, false);
					}else if(event.getRecord().getAttribute("STATUS").equals("30")){
						confButton.disable();
						canfButton.disable();
						enableOrDisables(save_map, false);
					}
//					final Criteria criteria = new Criteria();
//			    	criteria.addCriteria("OP_FLAG","M");
//			    	criteria.addCriteria("DOC_NO",table.getSelectedRecord().getAttributeAsString("ADJ_NO"));
//			    	exatable.fetchData(criteria, new DSCallback() {
//
//						@SuppressWarnings("unchecked")
//						@Override
//						public void execute(DSResponse response, Object rawData,
//								DSRequest request) {
//							if(pageForm != null) {
//								pageForm.getField("CUR_PAGE").setValue("1");
//								pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
//								pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
//								String sqlwhere = Cookies.getCookie("SQLWHERE");
//								if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
//									table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
//									//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
//								}
//							}
//							LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
//							if(map.get("criteria") != null) {
//								map.remove("criteria");
//							}
//							if(map.get("_constructor") != null) {
//								map.remove("_constructor");
//							}
//							if(map.get("C_ORG_FLAG") != null) {
//								Object obj = map.get("C_ORG_FLAG");
//								Boolean c_org_flag = (Boolean)obj;
//								map.put("C_ORG_FLAG",c_org_flag.toString());
//							}			
//						
//						}
//						
//					});
				}
			}
	    	
	    });
	    
	    TabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(table.getSelectedRecord()==null){
					return;
				}
				if(event.getTabNum()==0){
					return;
				}
				final Criteria criteria = new Criteria();
		    	criteria.addCriteria("OP_FLAG","M");
		    	criteria.addCriteria("DOC_NO",table.getSelectedRecord().getAttributeAsString("ADJ_NO"));
		    	exatable.fetchData(criteria, new DSCallback() {

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
		});
	    
	    stack.addMember(TabSet);
		main.addMember(stack);
	    
		return main;
	}
	

	private void createListField() {
		
		ListGridField ADJ_NO = new ListGridField("ADJ_NO","调整单号",90);
		ADJ_NO.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STATUS","审批状态",65);
		Util.initCodesComboValue(STATUS, "APPROVE_STS");
		STATUS.setCanEdit(false);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",100);
		SUPLR_NAME.setCanEdit(false);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",55);
		BELONG_MONTH.setCanEdit(false);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",65);
		INITITAL_AMOUNT.setCanEdit(false);
		ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额",65);
		CONFIRM_AMOUNT.setCanEdit(false);
		ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额",65);
		ADJ_AMOUNT.setCanEdit(false);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",55);
		TAX_AMOUNT.setCanEdit(false);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","调整金额（不含税）",100);
		SUBTAX_AMOUNT.setCanEdit(false);
		ListGridField BILL_TO = new ListGridField("BILL_TO","开票对象",145);
		BILL_TO.setCanEdit(true);
		ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因",90);
		//Util.initCodesComboValue(ADJ_REASON, "ADJ_REASON");
		ADJ_REASON.setCanEdit(true);
		ListGridField NOTES = new ListGridField("NOTES","备注",190);
		NOTES.setCanEdit(true);
		ListGridField ID = new ListGridField("ID","",120);
		ID.setHidden(true);
		
		table.setFields(ID,ADJ_NO,STATUS,SUPLR_NAME,BELONG_MONTH,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,BILL_TO,ADJ_REASON,NOTES);
	
	}

	private void createVeField(SGTable exatable) {
		ListGridField DOC_NO = new ListGridField("DOC_NO","单据编号",150);
		ListGridField ROLE_ID = new ListGridField("ROLE_ID","审核角色",150);
		ListGridField APPROVE_TIME = new ListGridField("APPROVE_TIME","审核时间",150);
		ListGridField APPROVER = new ListGridField("APPROVER","审核人",80);
		ListGridField APPROVER_RESULT = new ListGridField("APPROVER_RESULT","审核结果",100);
		ListGridField NOTES = new ListGridField("NOTES","审核意见",400);
		exatable.setFields(DOC_NO,ROLE_ID,APPROVE_TIME,APPROVER,APPROVER_RESULT,NOTES);
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(3);
		strip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.PayAdjBill_P0);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(payadjDS,
							createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.PayAdjBill_P0_01);
		saveButton.addClickHandler(new SavePayAdjBillAction(table,this));
		
		//提交确认
		confButton = createUDFBtn("提交确认",StaticRef.ICON_SAVE,SettPrivRef.PayAdjBill_P0_02);
		confButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
				String proName = "BMS_PAY_ADJNO_CONFIRM(?,?,?)";
				ArrayList<String> paramList = new ArrayList<String>();
				paramList.add(ADJ_NO);
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
							table.getSelectedRecord().setAttribute("STATUS", "审批中");
							table.redraw();
							confButton.disable();
							canfButton.enable();
						}else{
							MSGUtil.sayError(result);
						}
					}
					
				});
			}
			
		});
		//取消确认
		canfButton = createUDFBtn("取消确认",StaticRef.ICON_CANCEL,SettPrivRef.PayAdjBill_P0_03);
		canfButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
				String proName = "BMS_PAY_ADJNO_CANCEL(?,?)";
				ArrayList<String> paramList = new ArrayList<String>();
				paramList.add(ADJ_NO);
				Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)) {
							MSGUtil.showOperSuccess();
							table.getSelectedRecord().setAttribute("STATUS", "待审批");
							table.redraw();
							canfButton.disable();
							confButton.enable();
						}else{
							MSGUtil.sayError(result);
						}
					}
					
				});
			}
			
		});
		//导出
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.PayAdjBill_P0_04);
        expButton.addClickHandler(new PayAdjExportAction(table,"addtime desc"));
        
        save_map.put(SettPrivRef.PayAdjBill_P0_01, saveButton);
        enableOrDisables(save_map, false);
        
        strip.setMembersMargin(4);
        strip.setMembers(searchButton,saveButton,confButton,canfButton,expButton);
	}
	
	public DynamicForm createSerchForm(final SGPanel form){
		form.setDataSource(payadjDS);
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
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		
		SGCombo STATUS = new SGCombo("STATUS","审批状态");
		Util.initCodesComboValue(STATUS, "APPROVE_STS");
		
//		SGText USERID = new SGText("USERID", "");
//		USERID.setValue(LoginCache.getLoginUser().getUSER_ID());
//		USERID.setVisible(false);
		
		form.setItems(SUPLR_ID,SUPLR_NAME,BELONG_MONTH,STATUS);
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

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PayAdjBillView view = new PayAdjBillView();
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
