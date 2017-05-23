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
import com.rd.client.ds.settlement.RecAuditDamageDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BussWin;
import com.rd.client.win.SearchWin;
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
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 费用管理--应收管理--待审货损赔偿单
 * @author cjt
 *
 */
@ClassForNameAble
public class RecAuditDamageView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private Window checkWin;
	private SGPanel searchForm;
	private SectionStack section;
	public DynamicForm pageForm;
	
	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		ds = RecAuditDamageDS.getInstance("BILL_REC_DAMAGE1");
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
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		initVerify();
		layout.addMember(toolStrip);
		layout.addMember(section);
		
		doFetchDate();
		return layout;
	}

	private void createListFields(){
		ListGridField DAMAGE_NO = new ListGridField("DAMAGE_NO","赔偿单号",80);
		ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID","客户名称",100);
		Util.initComboValue(CUSTOMER_ID, "BAS_CUSTOMER","ID","CUSTOMER_CNAME","","");
		ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH","所属期",70);
		ListGridField TOTAL_AMOUNT = new ListGridField("TOTAL_AMOUNT","赔偿总金额",80);
		ListGridField COMPANY_AMOUNT = new ListGridField("COMPANY_AMOUNT","本公司承担金额",90);
		ListGridField INSUR_AMOUNT = new ListGridField("INSUR_AMOUNT","保险公司承担金额",90);
		ListGridField SUPLR_AMOUNT = new ListGridField("SUPLR_AMOUNT","承运商/司机承担金额",90);
		ListGridField STAFF_AMOUNT = new ListGridField("STAFF_AMOUNT","本公司员工承担金额",90);
		ListGridField LISTER = new ListGridField("LISTER","送审人",60);
		ListGridField LISTER_TIME = new ListGridField("LISTER_TIME","送审时间",110);
		ListGridField DESCR = new ListGridField("DESCR","货损货差情况说明",120);
		table.setFields(DAMAGE_NO,CUSTOMER_ID,BELONG_MONTH,TOTAL_AMOUNT,TOTAL_AMOUNT,COMPANY_AMOUNT,INSUR_AMOUNT,SUPLR_AMOUNT,STAFF_AMOUNT,LISTER,LISTER_TIME,DESCR);
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setHeight("20");
		strip.setWidth("100%");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		strip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.RECAUDITDAMAGE_P0);
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
		IButton checkButton = createUDFBtn("审批",StaticRef.ICON_SAVE,SettPrivRef.RECAUDITDAMAGE_P0_01);
		checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()!=null){
					if(searchWin != null) {
						searchWin.hide();
					}
					checkWin=getCheckWin();
					checkWin.show();
				}else{
					SC.say("请选择记录");
				}
			}
        	
        });
		
		strip.setMembersMargin(4);
		strip.setMembers(searchButton,checkButton);
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
					
					String DAMAGE_NO = table.getSelectedRecord().getAttribute("DAMAGE_NO");
					
					String proName = "BMS_REC_LOSS_AUDITAGREE(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(DAMAGE_NO);
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
					String DAMAGE_NO = table.getSelectedRecord().getAttribute("DAMAGE_NO");
					String proName = "BMS_REC_LOSS_AUDITBACK(?,?,?,?,?)";
					ArrayList<String> paramList = new ArrayList<String>();
					paramList.add(DAMAGE_NO);
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
		
		form.setItems(ROLE_ID,BUSS_ID,BUSS_NAME,BELONG_MONTH);
		
		return form;
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
		}
	}

	@Override
	public Canvas createCanvas(String id, TabSet tabSet) {
		RecAuditDamageView view = new RecAuditDamageView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}

}
