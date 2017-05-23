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
import com.rd.client.ds.settlement.PayAdjAuditDS;
import com.rd.client.ds.settlement.PayAdjAuditdetailDS;
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
 * 费用管理-结算管理-待审应付调整单
 * @author cjt
 *
 */
@ClassForNameAble
public class PayAdjAuditView extends SGForm implements PanelFactory {
	
	private DataSource payadjDS;
	private DataSource detailDS;
	private SGTable table;
	private ListGrid itemTable;
	private SectionStack list_section;
	private Window searchWin;
	private SGPanel searchForm;
	private IButton exaButton;
	private Window addWin;
	public DynamicForm pageForm; 

	/*public PayAdjAuditView(String id) {
		super(id);
	}*/

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("100%");
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		payadjDS = PayAdjAuditDS.getInstance("BILL_PAY_ADJAUDIT","BILL_PAY_ADJUST");
		detailDS = PayAdjAuditdetailDS.getInstance("BILL_PAY_ADJAUDITDETAILS","BILL_PAY_ADJDETAILS");
		
		table = new SGTable(payadjDS,"100%", "100%", false, false, false){
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
				itemTable.setWidth("97%");
				itemTable.setHeight(46);
				itemTable.setCellHeight(22);
				itemTable.setCanEdit(false);
				itemTable.setShowRowNumbers(true);
				itemTable.setAutoFitData(Autofit.VERTICAL);
				
				ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",105);
				ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",60);
				ListGridField VEHICLE_TYP_ID = new ListGridField("VEHICLE_TYP_ID","车型",65);
				Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", "", "");
				ListGridField DRIVER = new ListGridField("DRIVER","司机",50);
				ListGridField MOBILE = new ListGridField("MOBILE","联系电话",70);
				ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",70);
				ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","收货日期",70);
				ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",80);
				ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",80);
				ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",65);
				ListGridField ADJ_AMOUNT1 = new ListGridField("ADJ_AMOUNT1","一次调整金额",75);
				ListGridField ADJ_REASON1 = new ListGridField("ADJ_REASON1","一次调整原因",75);
				Util.initCodesComboValue(ADJ_REASON1,"ADJ_REASON");
				ListGridField ADJ_AMOUNT2 = new ListGridField("ADJ_AMOUNT2","二次调整金额",75);
				ListGridField ADJ_REASON2 = new ListGridField("ADJ_REASON2","二次调整原因",75);
				Util.initCodesComboValue(ADJ_REASON2,"ADJ_REASON");
				
				itemTable.setFields(LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOBILE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INITITAL_AMOUNT,ADJ_AMOUNT1,ADJ_REASON1,ADJ_AMOUNT2,ADJ_REASON2);
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("ADJ_NO",record.getAttributeAsString("ADJ_NO"));
				itemTable.fetchData(criteria);
				
				layout.addMember(itemTable);
				layout.setLayoutLeftMargin(35);
				
				return layout;
			};
		};
		
		createListField();
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);
		table.setConfirmDiscardEdits(false);
		table.setCanEdit(false);
		table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		doFetchDate();
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.addSection(listItem);
	    list_section.setWidth("100%");
	    pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		
	    /*table.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
					if(record.getAttribute("ADJ_AMOUNT")!=null && (record.getAttribute("ADJ_AMOUNT")!="0")){
						table.expandRecord(table.getSelectedRecord());
					}
				}
			}
		});*/
	    
	    createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(list_section);
	    
		return main;
	}
	
	private void createListField() {
		
		ListGridField ADJ_NO = new ListGridField("ADJ_NO","调整单号",90);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",90);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",55);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",65);
		ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额",65);
		ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额",65);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",55);
		ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","调整金额（不含税）",100);
		ListGridField BILL_TO = new ListGridField("BILL_TO","开票对象",100);
		ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因",90);
		Util.initCodesComboValue(ADJ_REASON, "ADJ_REASON");
		ListGridField LISTER = new ListGridField("LISTER","送审人",80);
		ListGridField LISTER_TIME = new ListGridField("LISTER_TIME","送审时间",120);
		ListGridField NOTES = new ListGridField("NOTES","备注",120);
		
		
		table.setFields(ADJ_NO,SUPLR_NAME,BELONG_MONTH,INITITAL_AMOUNT,CONFIRM_AMOUNT,ADJ_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,BILL_TO,ADJ_REASON,LISTER,LISTER_TIME,NOTES);
	}

	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(3);
		strip.setSeparatorSize(5);
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.PayAdjAudit_P0);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(payadjDS,
							createSerchForm(searchForm),list_section.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		//审核
		exaButton = createUDFBtn("审核",StaticRef.ICON_SAVE,SettPrivRef.PayAdjAudit_P0_01);
		exaButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(ObjUtil.isNotNull(table.getSelectedRecord())){
					if(addWin==null){
						addWin=getShpTable(table);
						addWin.show();
					}
					/*else{
						addWin.show();
					}*/
				}else{
					SC.say("请选择记录");
					return;
				}
			}
		});
		
		strip.setMembersMargin(4);
        strip.setMembers(searchButton,exaButton);
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
		
//		SGCombo STATUS = new SGCombo("STATUS","审批状态");
//		Util.initCodesComboValue(STATUS, "APPROVE_STS");
		
		SGText ROLE_ID = new SGText("ROLE_ID","");
		if(!LoginCache.getLoginUser().getROLE_ID().equals("SUPER_MAN")){
			ROLE_ID.setValue(LoginCache.getLoginUser().getROLE_ID());
		}
		ROLE_ID.setVisible(false);
		//System.out.println("1:"+LoginCache.getLoginUser().getROLE_ID().toString());
		form.setItems(SUPLR_ID,SUPLR_NAME,BELONG_MONTH,ROLE_ID);
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
	
	public Window getShpTable(final SGTable table) {
		final Window win=new Window();
		SGPanel form=new SGPanel();
		StaticTextItem LISTER=new StaticTextItem("LISTER","送审人");
		LISTER.setWidth(FormUtil.Width);
		LISTER.setValue(table.getSelectedRecord().getAttribute("LISTER"));
		
		final RadioGroupItem radio=new RadioGroupItem("STATUS","审批");
		radio.setStartRow(true);
		radio.setValueMap("同意","打回");
		radio.setDefaultValue("同意");
		radio.setColSpan("*");  
		radio.setRowSpan(1);
		radio.setVertical(false); 
		radio.setWidth("30%");
		
		StaticTextItem LISTER_TIME = new StaticTextItem("LISTER_TIME", "送审时间");
		LISTER_TIME.setStartRow(true);
		LISTER_TIME.setWidth(FormUtil.Width);
		String time = table.getSelectedRecord().getAttribute("LISTER_TIME");
		LISTER_TIME.setValue(time);
		
		final TextAreaItem notes = new TextAreaItem("notes", "审批意见");
		notes.setStartRow(true);
		notes.setColSpan(6);
		notes.setHeight(70);
		notes.setWidth(FormUtil.longWidth);
		
		form.setItems(LISTER,LISTER_TIME,radio,notes);
		form.setIsGroup(true);  
		form.setGroupTitle("赔偿单审批");
		form.setMargin(3);
		form.setPadding(20);
		form.setHeight("70%");
		form.setWidth("99%");
		form.setLeft("10%");
		form.setAlign(Alignment.CENTER);
		form.setTitleSuffix(":");

   	    ToolStrip recivetoolStrip = new ToolStrip();
		recivetoolStrip.setWidth("100%");
		recivetoolStrip.setHeight("20");
		recivetoolStrip.setPadding(2);
		recivetoolStrip.setSeparatorSize(12);
		recivetoolStrip.addSeparator();
		recivetoolStrip.setMembersMargin(4);
		recivetoolStrip.setAlign(Alignment.RIGHT);
		
		IButton saveButton=createUDFBtn("确定", StaticRef.ICON_SAVE,SettPrivRef.PayAdjAudit_P0_02);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(radio.getValue().toString().equals("同意")){
					String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
					String note = "";
					if(ObjUtil.isNotNull(notes.getValue())){
						note = notes.getValue().toString();
					}
					String proName = "BMS_PAY_ADJNO_AUDITAGREE(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(ADJ_NO);
					paramList.add(LoginCache.getLoginUser().getROLE_ID());
					paramList.add(LoginCache.getLoginUser().getUSER_ID());
					paramList.add(note);
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {
	
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}
	
						@Override
						public void onSuccess(String result) {
							if(result.startsWith(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
								addWin.destroy();
								table.invalidateCache();
								doFetchDate();
							}else{
								MSGUtil.sayError(result);
							}
						}
						
					});
				}else if(radio.getValue().toString().equals("打回")){
					String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
					String note = "";
					if(ObjUtil.isNotNull(notes.getValue())){
						note = notes.getValue().toString();
					}
					String proName = "BMS_PAY_ADJNO_AUDITBACK(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(ADJ_NO);
					paramList.add(LoginCache.getLoginUser().getROLE_ID());
					paramList.add(LoginCache.getLoginUser().getUSER_ID());
					paramList.add(note);
					Util.async.execProcedure(paramList, proName, new AsyncCallback<String>() {
	
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}
	
						@Override
						public void onSuccess(String result) {
							if(result.startsWith(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
								win.hide();
								table.invalidateCache();
								doFetchDate();
							}else{
								MSGUtil.sayError(result);
							}
						}
						
					});
				}
				
			}
		});
		
		IButton cancelButton=createUDFBtn("取消", StaticRef.ICON_CANCEL,SettPrivRef.PayAdjAudit_P0_03);
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				win.hide();
			}
			
		});
		
		recivetoolStrip.setMembers(saveButton,cancelButton);
   	    
		win.addItem(form);
		win.addItem(recivetoolStrip);

		win.setTitle("审批操作");
		win.setWidth("38%");
		win.setHeight("50%");
		win.setTop("20%");
		win.setLeft("30%");
		return win;
	}
	
	public void doFetchDate(){
		final Criteria criteria = new Criteria();
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
	public Canvas createCanvas(String id,TabSet tabSet) {
		PayAdjAuditView view = new PayAdjAuditView();
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
