package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.BillSettRecDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 客户结算报表
 * @author Administrator
 *
 */
@ClassForNameAble
public class R_CustomRecView extends SGForm implements PanelFactory{
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public R_CustomRecView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = BillSettRecDS.getInstance("V_BILL_REC1", "TRANS_BILL_RECE");

		table = new SGTable(ds, "100%", "70%");
        createListFields(table);
        table.setShowFilterEditor(false);
        table.setCanEdit(false);
        
        //创建按钮布局
		createBtnWidget(toolStrip);
		section = createSection(table, null, true, true);
		initVerify();  
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		return main;
		
	}
	//布局列表信息按钮
	private void createListFields(SGTable table) {
		
		ListGridField CUSTOMER_CODE=new ListGridField("CUSTOMER_CODE",Util.TI18N.CUSTOMER_CODE(),90);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", Util.TI18N.CUSTOMER(), 90);
		ListGridField PARENT_CUSTOMER_NAME = new ListGridField("PARENT_CUSTOMER_NAME", Util.TI18N.PARENT_CUSTOMER_ID(), 90);		
		ListGridField REFENENCE1 = new ListGridField("REFENENCE1", Util.TI18N.REFENENCE1(), 100);
		ListGridField ODR_NO = new ListGridField("ODR_NO", Util.TI18N.ODR_NO(), 100);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 80);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME",Util.TI18N.FEE_NAME(), 70);
		ListGridField FEE_BASE_NAME = new ListGridField("FEE_BASE_NAME",Util.TI18N.FEE_BASE(), 60);
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 60);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 60);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 65);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", Util.TI18N.DISCOUNT(), 60);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(), 65);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实收费用", 65);
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 110);  
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 60);
		ListGridField RECE_STAT_NAME=new ListGridField("RECE_STAT_NAME",Util.TI18N.COLLECTE_STATE(),80);
  		
		table.setFields(CUSTOMER_CODE,CUSTOMER_NAME, PARENT_CUSTOMER_NAME, REFENENCE1, ODR_NO, CUSTOM_ODR_NO,RECE_STAT_NAME ,FEE_NAME, FEE_BASE_NAME, BAS_VALUE,PRICE, PRE_FEE, DISCOUNT_RATE, DUE_FEE, PAY_FEE, ODR_TIME, NOTES);
	
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
	        //导出按钮
	        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
	        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
	    
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton, expButton);
	  
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		/*if(LoginCache.getLoginUser().getROLE_ID().equals(StaticRef.SUPER_ROLE)) {			
			Util.initCustComboValue(CUSTOMER,"");
		}
		else {
			
			SYS_USER user = LoginCache.getLoginUser();
			String user_customer = user.getUSER_CUSTOMER();
			Util.initCustComboValue(CUSTOMER," and ID IN (" + user_customer + ")","","");
		}*/
		Util.initCustComboValue(CUSTOMER, "");
		SGText ODR_NO = new SGText("DOC_NO", Util.TI18N.ODR_NO());
		ODR_NO.setStartRow(true);
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//yuanlei
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ORD_ADDTIME()+" 从");
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_FROM.setDefaultValue(getCurInitDay());
		ODR_TIME_TO.setDefaultValue(getCurTime());
		ODR_TIME_TO.setWidth(130);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		SGCombo PARENT_CUSTOMER = new SGCombo("PARENT_CUSTOMER_ID",Util.TI18N.PARENT_CUSTOMER_ID(),true);
		/*if(LoginCache.getLoginUser().getROLE_ID().equals(StaticRef.SUPER_ROLE)) {			
			Util.initCustComboValue(CUSTOMER,"");
		}
		else {
			
			SYS_USER user = LoginCache.getLoginUser();
			String user_customer = user.getUSER_CUSTOMER();
			Util.initCustComboValue(CUSTOMER," and ID IN (" + user_customer + ")","","");
		}*/
		Util.initCustComboValue(PARENT_CUSTOMER, "");

		form.setItems(CUSTOMER,CUSTOM_ODR_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,ODR_NO,ODR_TIME_FROM,ODR_TIME_TO,PARENT_CUSTOMER);
		
		return form;
	}
	@Override
	public void createForm(DynamicForm form) {
		
	}
	

	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	public static native String getCurInitDay() /*-{
	
	var now = new Date(new Date()-24*60*60*1000);
	var year=now.getFullYear();
	var month=now.getMonth()+1;
	var day="01";	
	var res = year+"/"+month+"/"+ day + " 00:00";
	return res;

}-*/;

	public static native String getCurTime() /*-{

	var now = new Date();
	var enddate = now;
	enddate.setUTCDate(now.getUTCDate() + 1)
	var year=enddate.getFullYear();
	var month=enddate.getMonth()+1;
	var day=enddate.getDate();
	var res = year+"/"+month+"/"+ day + " 00:00";
	return res;
}-*/;

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_CustomRecView view = new R_CustomRecView();
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