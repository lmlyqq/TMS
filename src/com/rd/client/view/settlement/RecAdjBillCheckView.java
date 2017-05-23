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
import com.rd.client.ds.settlement.RecAdjustDS;
import com.rd.client.ds.settlement.RecAdjustDetailsDS;
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
 * 费用管理---结算管理-待审应收调整单
 */
@ClassForNameAble
public class RecAdjBillCheckView extends SGForm implements PanelFactory {
   private DataSource ds;
   private DataSource detailsDS;
   private SGTable table;
   private Window searchWin;
   private Window checkWin;
   private SGPanel searchForm;
   private SectionStack section;
   private ListGrid countryGrid;
   public DynamicForm pageForm; 
   /*public RecAdjBillCheckView(String id) {
	   super(id);
   }*/
   
	//页面的整体布局
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		ds = RecAdjustDS.getInstance("V_REC_ADJUST1","BILL_REC_ADJUST");
		detailsDS=RecAdjustDetailsDS.getInstance("V_REC_DETAILS_ADJUST1","BILL_REC_ADJDETAILS");
		table=new SGTable(ds, "100%", "100%", false, false, false) {			
        	//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
                VLayout layout = new VLayout();              
    	        countryGrid = new ListGrid() {  
		            @Override  
		            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {  
		                if (getFieldName(colNum).equals("ADJ_AMOUNT1")||getFieldName(colNum).equals("ADJ_AMOUNT2")) {  
		                    if (!"0".equals(record.getAttribute("ADJ_AMOUNT1"))) {  
		                        return "font-weight:bold; color:#d64949;";  
		                    }  
		                    if (!"0".equals(record.getAttributeAsInt("ADJ_AMOUNT2"))) {  
		                        return "font-weight:bold; color:#287fd6;";  
		                    } 
		                    else{  
		                        return super.getCellCSSText(record, rowNum, colNum);  
		                    }  
		                } else {  
		                    return super.getCellCSSText(record, rowNum, colNum);  
		                }  
		            }  
		        };
           
                countryGrid.setDataSource(detailsDS);
                countryGrid.setWidth("100%");
                countryGrid.setHeight(50);
                countryGrid.setCanEdit(false);
                countryGrid.setShowRowNumbers(true);
                countryGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
                countryGrid.setAutoFitData(Autofit.VERTICAL);
               // lstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("ADJ_NO", record.getAttributeAsString("ADJ_NO"));		        	
		        
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
		        ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额", 70);
		        ListGridField ADJ_AMOUNT1 = new ListGridField("ADJ_AMOUNT1","一次调整金额", 80);
		        ListGridField ADJ_REASON1 = new ListGridField("ADJ_REASON1","一次调整原因 ", 120);
		        ADJ_REASON1.setCanEdit(true);
		        ListGridField ADJ_AMOUNT2 = new ListGridField("ADJ_AMOUNT2","二次调整金额", 80);
		        ListGridField ADJ_REASON2 = new ListGridField("ADJ_REASON2","二次调整原因", 120);
		        //ADJ_REASON2.setCanEdit(true);
		        countryGrid.setFields(ID,ODR_NO,CUSTOM_ODR_NO,VEHICLE_TYP_ID_NAME,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INITITAL_AMOUNT,ADJ_AMOUNT1,ADJ_REASON1,ADJ_AMOUNT2,ADJ_REASON2);
		        countryGrid.fetchData(findValues);

                layout.addMember(countryGrid);
                layout.setLayoutLeftMargin(30);
                
                return layout;   
            } 
		};
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);		
		createListFields();	
		
		section= new SectionStack();
		SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		section.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		
		createBtnWidget(toolStrip);
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		initVerify();
		layout.addMember(toolStrip);
		layout.addMember(section);

		doFetchDate();
		return layout;
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
		IButton checkButton = createUDFBtn("审批",StaticRef.ICON_SAVE,SettPrivRef.RecAdjAudit_P0_01);
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
					}else{
						checkWin.show();
					}					
				}else{
					SC.say("请选择记录");
				}				
        	}        	
        });
		       
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,checkButton);	
	}
	
	private void createListFields(){
        
		final ListGridField ADJ_NO = new ListGridField("ADJ_NO","调整单号",100);
		//ADJ_NO.setCanEdit(false);
		//STATUS.setCanEdit(false);
//		Util.initCodesComboValue(STATUS, "APPROVE_STS");
		ListGridField BUSS_NAME = new ListGridField("BUSS_NAME","客户名称",90);
		//BUSS_NAME.setCanEdit(false);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",80);
		//BELONG_MONTH.setCanEdit(false);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",90);
		//INITITAL_AMOUNT.setCanEdit(false);
		ListGridField CONFIRM_AMOUNT = new ListGridField("CONFIRM_AMOUNT","确认金额",80);
		//CONFIRM_AMOUNT.setCanEdit(false);
		ListGridField ADJ_AMOUNT = new ListGridField("ADJ_AMOUNT","调整金额（含税）",110);
		//ADJ_AMOUNT.setCanEdit(false);
		ListGridField BILL_TO = new ListGridField("BILL_TO","开票对象",100);
		ListGridField ADJ_REASON = new ListGridField("ADJ_REASON","调整原因",120);
		Util.initCodesComboValue(ADJ_REASON, "ADJ_REASON");
		final ListGridField LISTER = new ListGridField("LISTER","送审人",80);
		ListGridField NOTES = new ListGridField("NOTES","备注",120);
	
		ListGridField ID = new ListGridField("ID","",120);
		ID.setHidden(true);
		ListGridField LISTER_TIME = new ListGridField("LISTER_TIME","送审时间",120);
		
		table.setFields(ID,ADJ_NO,BUSS_NAME,BELONG_MONTH,INITITAL_AMOUNT,CONFIRM_AMOUNT
				        ,ADJ_AMOUNT,BILL_TO,ADJ_REASON,LISTER,LISTER_TIME,NOTES);
	}
	
	public DynamicForm createSerchForm(final SGPanel form) {
		
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
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");	
		SGText ROLE_ID = new SGText("ROLE_ID","");
		if(!LoginCache.getLoginUser().getROLE_ID().equals("SUPER_MAN")){
			ROLE_ID.setValue(LoginCache.getLoginUser().getROLE_ID());
		}
		ROLE_ID.setVisible(false);
//		SGCombo STATUS = new SGCombo("STATUS","审批状态");
//		Util.initCodesComboValue(STATUS,"APPROVE_STS");		
        form.setItems(ROLE_ID,BUSS_ID,BUSS_NAME,BELONG_MONTH);
		
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
		//APPROVE_TIME.setTitleOrientation(TitleOrientation.LEFT);
		APPROVE_TIME.setValue(table.getSelectedRecord().getAttribute("LISTER_TIME"));
		//Util.initDateTime(checkForm,APPROVE_TIME);
		//APPROVE_TIME.setDisabled(true);
		
		final TextAreaItem checknotes = new TextAreaItem("NOTES", "审批意见");
		checknotes.setStartRow(true);
		checknotes.setColSpan(6);
		checknotes.setHeight(70);
		checknotes.setWidth(FormUtil.longWidth);
		
		checkForm.setItems(APPROVER,APPROVE_TIME,rgItem,checknotes);
		checkForm.setIsGroup(true);  
		checkForm.setGroupTitle("调整单审批");
		checkForm.setMargin(3);
		checkForm.setPadding(20);
		checkForm.setHeight("70%");
		checkForm.setWidth("99%");
		checkForm.setLeft("10%");
		checkForm.setAlign(Alignment.CENTER);
		checkForm.setTitleSuffix(":");
		
		win.addItem(checkForm);

		ToolStrip recivetoolStrip = new ToolStrip();
		//recivetoolStrip.setAlign(Alignment.LEFT);
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
				System.out.println(LoginCache.getLoginUser().getUSER_ID());
				System.out.println(LoginCache.getLoginUser().getROLE_ID());
				
				String status=rgItem.getValue().toString();
				if(status.equals("同意")){
					
					String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
					
					String proName = "BMS_REC_ADJNO_AUDITAGREE(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(ADJ_NO);
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
								checkWin.destroy();
								doFetchDate();
								table.redraw();
							}else{
								MSGUtil.sayError(result);
							}
						}
						
					});
				}else{
					String ADJ_NO = table.getSelectedRecord().getAttribute("ADJ_NO");
					String proName = "BMS_REC_ADJNO_AUDITBACK(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(ADJ_NO);
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
								doFetchDate();
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

	public void doFetchDate(){
	
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
	public void createForm(DynamicForm form) {
		
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
			searchWin.destroy();
			//searchForm.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		RecAdjBillCheckView view = new RecAdjBillCheckView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}
		
	

}
