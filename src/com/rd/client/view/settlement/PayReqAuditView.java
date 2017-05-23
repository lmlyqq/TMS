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
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.PayReqBillDS;
import com.rd.client.ds.settlement.PayReqdetailsDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrWin;
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
 * 费用管理---结算管理-待审应付请款单
 */
@ClassForNameAble
public class PayReqAuditView extends SGForm implements PanelFactory {
   private DataSource ds;
   private DataSource detailsDS;
   private SGTable table;
   private Window searchWin;
   private Window checkWin;
   private SGPanel searchForm;
   private SectionStack section;
   private ListGrid countryGrid;
   public DynamicForm pageForm; 
   /*public PayReqAuditView(String id) {
	   super(id);
   }*/
   
	//页面的整体布局
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		ds = PayReqBillDS.getInstance("V_PAY_REQUEST1","BILL_PAY_REQUEST");
		detailsDS=PayReqdetailsDS.getInstance("V_PAY_REQDETAILS1","BILL_PAY_REQDETAILS");
		table=new SGTable(ds, "100%", "100%", false, false, false) {
			
        	//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
                VLayout layout = new VLayout();              
                countryGrid = new ListGrid() {  
   	        	 @Override  
   	             protected String getBaseStyle(ListGridRecord record, int rowNum, int colNum) {  
   	                 if (getFieldName(colNum).equals("DIFF_FEE")) {  
   	                     if (!("0".equals(record.getAttribute("DIFF_FEE")))) {  
   	                         return "myHighGridCell";       
   	                     } else {  
   	                         return super.getBaseStyle(record, rowNum, colNum);  
   	                     }  
   	                 } else {  
   	                     return super.getBaseStyle(record, rowNum, colNum);  
   	                 }  
   	             }  
		        };
                countryGrid.setDataSource(detailsDS);
                countryGrid.setWidth("96%");
                countryGrid.setHeight(60);
                countryGrid.setCanEdit(false);
                countryGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
                countryGrid.setAutoFitData(Autofit.VERTICAL);
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("REQ_NO", record.getAttributeAsString("REQ_NO"));

		        
		        ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号", 120);
		        ListGridField ID = new ListGridField("ID","", 120);
		        ID.setHidden(true);
		        ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌", 60);
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
//		        ListGridField VEHICLE_TYPE = new ListGridField("VEHICLE_TYPE","车型", 80);
//		        ListGridField INITITAL_FEE = new ListGridField("INITITAL_FEE","期初金额", 80);
//		        ListGridField PAY_FEE = new ListGridField("PAY_FEE","实付金额", 90);
//		        ListGridField DIFF_FEE = new ListGridField("DIFF_FEE","亏损金额", 100);  
//		        ListGridField NOTES = new ListGridField("NOTES","异常说明", 150);
//		        NOTES.setCanEdit(true);
		        countryGrid.setFields(ID,LOAD_NO,PLATE_NO,VEHICLE_TYP_ID,DRIVER,MOIBLE,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,UNLOAD_NAME,INIT_AMOUNT,CONFIRM_AMOUNT);
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
		IButton checkButton = createUDFBtn("审批",StaticRef.ICON_SAVE,SettPrivRef.PayReqAudiBill_P0_01);
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
        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,checkButton);
		
		
		
	}
	
	private void createListFields(){
        
		final ListGridField REQ_NO = new ListGridField("REQ_NO","请款单号",120);
		ListGridField STATUS = new ListGridField("STATUS","审批状态",80);
		Util.initCodesComboValue(STATUS,"APPROVE_STS");
		ListGridField PAY_STATUS = new ListGridField("PAY_STATUS","核销状态",80);
		Util.initCodesComboValue(PAY_STATUS,"PAY_STAT");
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商",110);
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",70);
		ListGridField INITITAL_AMOUNT = new ListGridField("INITITAL_AMOUNT","期初金额",80);
		ListGridField PAY_AMOUNT = new ListGridField("PAY_AMOUNT","实付",80);
		ListGridField TAX_AMOUNT = new ListGridField("TAX_AMOUNT","税金",70);
		final ListGridField SUBTAX_AMOUNT = new ListGridField("SUBTAX_AMOUNT","实付金额（不含税）",120);
		ListGridField NOTES = new ListGridField("NOTES","摘要",160);
		NOTES.setCanEdit(true);
		ListGridField ID = new ListGridField("ID","",120);
		ID.setHidden(true);
		
		
		table.setFields(ID,REQ_NO,STATUS,PAY_STATUS,SUPLR_NAME,BELONG_MONTH
				        ,INITITAL_AMOUNT,PAY_AMOUNT,TAX_AMOUNT,SUBTAX_AMOUNT,NOTES);

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
		SGText ROLE_ID = new SGText("ROLE_ID","");
		if(!LoginCache.getLoginUser().getROLE_ID().equals("SUPER_MAN")){
			ROLE_ID.setValue(LoginCache.getLoginUser().getROLE_ID());
		}
		ROLE_ID.setVisible(false);
		SGCombo STATUS = new SGCombo("STATUS","审批状态");
		Util.initCodesComboValue(STATUS,"APPROVE_STS");		
        form.setItems(ROLE_ID,SUPLR_ID_NAME,SUPLR_ID,BELONG_MONTH,STATUS);
		
		return form;
	}
	
	public Window getCheckWin() {
		final Window win=new Window();
		SGPanel checkForm=new SGPanel();
		StaticTextItem APPROVER=new StaticTextItem("APPROVER","送审人");
		APPROVER.setWidth(FormUtil.Width);
		APPROVER.setValue(LoginCache.getLoginUser().getUSER_ID());
		
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
					
					String REQ_NO = table.getSelectedRecord().getAttribute("REQ_NO");
					
					String proName = "BMS_PAY_REQNO_AUDITAGREE(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(REQ_NO);
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
				}else{
					String REQ_NO = table.getSelectedRecord().getAttribute("REQ_NO");
					String proName = "BMS_PAY_REQNO_AUDITBACK(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(REQ_NO);
					paramList.add(LoginCache.getLoginUser().getROLE_ID());
					//System.out.println(LoginCache.getLoginUser().getROLE_ID());
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
		criteria.addCriteria("STATUS","10");
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
			checkWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PayReqAuditView view = new PayReqAuditView();
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