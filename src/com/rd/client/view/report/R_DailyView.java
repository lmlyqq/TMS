package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.R_SERVICE_DAILY_DS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 报表管理-->作业报表-->营运日报表
 * @author wangjun
 *
 */
@ClassForNameAble
public class R_DailyView extends SGForm implements PanelFactory {
	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private SGDateTime DEPART_TIME_FROM;
	private SGDateTime DEPART_TIME_TO;

	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_SERVICE_DAILY_DS.getInstance("R_SERVICE_DAILY","R_SERVICE_DAILY");
		
		//放置按钮
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//设置详细信息布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		table= new SGTable(ds, "100%", "100%");
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createFields(table);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("100%");
		
		final SectionStackSection listItem = new SectionStackSection("列表信息");//
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
		
	    main.setWidth100();
        main.setHeight100();
        main.addMember(toolStrip);
		main.addMember(section);
        
		return main;
	}

	private void createFields(SGTable table) {
		/**
		 * 客户单号   状态	     订单时间	发运时间	到货时间	收货区域  收货方	 调度状态	发运状态	到货状态	
		 * 运输单位	 订单数量  发运数量	到货数量	体积	毛重	供应商	     执行机构	订单类型	运输服务	预估运费	备注
		 */	
		ListGridField PARENT_CUSTOMER_NAME = new ListGridField("PARENT_CUSTOMER_NAME","项目名称",80);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户名称",80);
		ListGridField ODR_TYP_NAME = new ListGridField("ODR_TYP_NAME","运输类型",80);	
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO","内单号",120);
		ListGridField REFENENCE1  = new ListGridField("REFENENCE1 ","运单号",80);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","订单号",110);	
		
		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME","发货日期",110);
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME","到货日期",110);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",110);	
		
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",110);
		ListGridField NOTES = new ListGridField("NOTES","备注",110);
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","数量",60);	
		
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",110);
		ListGridField VEHICLE_TYPE = new ListGridField("VEHICLE_TYPE","车型",80);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商名称",80);	
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",65);
		ListGridField DRIVER1 = new ListGridField("DRIVER1","驾驶员",65);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","联系方式",80);	
		
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME","要求装货时间",110);
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME","要求到货时间",110);
		//ListGridField MOBILE1 = new ListGridField("MOBILE1","联系方式",80);	
		
		table.setFields(PARENT_CUSTOMER_NAME,CUSTOMER_NAME,ODR_TYP_NAME,SHPM_NO,REFENENCE1,CUSTOM_ODR_NO
		,DEPART_TIME,UNLOAD_TIME,LOAD_NAME,UNLOAD_NAME,NOTES,TOT_QNTY,LOAD_NO,VEHICLE_TYPE,SUPLR_NAME,PLATE_NO,	
		DRIVER1,MOBILE1,PRE_LOAD_TIME,PRE_UNLOAD_TIME);
		
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {

		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);//查询
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin( ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
					searchWin.setHeight(320);
				}else{
					searchWin.show();
				}
				
			}
		});
		
		 //导出按钮
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
        
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton,expButton);
		
	}

	protected DynamicForm createSerchForm(SGPanel form) {
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));

		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO","订单号");
		
		SGText REFENENCE1=new SGText("REFENENCE1","运单号");
		
		SGText SHPM_NO=new SGText("SHPM_NO", "内单号");
		
		ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
		ODR_TIME_FROM.setWidth(FormUtil.Width);
		ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_TO.setWidth(FormUtil.Width);
		String PreYesDate=Util.getYesPreDay();
		ODR_TIME_FROM.setDefaultValue(PreYesDate);
		ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		 
		DEPART_TIME_FROM = new  SGDateTime("DEPART_TIME_FROM", "发货日期 从");
		DEPART_TIME_FROM.setWidth(FormUtil.Width);
		DEPART_TIME_TO = new  SGDateTime("DEPART_TIME_TO","到");
		DEPART_TIME_TO.setWidth(FormUtil.Width);
		//DEPART_TIME_FROM.setDefaultValue(PreYesDate);
		//DEPART_TIME_TO.setDefaultValue(Util.getCurTime());
		
		SGDateTime UNLOAD_TIME_FROM = new  SGDateTime("UNLOAD_TIME_FROM", "到货日期 从",true);
		UNLOAD_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime UNLOAD_TIME_TO = new  SGDateTime("UNLOAD_TIME_TO","到");
		UNLOAD_TIME_TO.setWidth(FormUtil.Width);
		//UNLOAD_TIME_FROM.setDefaultValue(PreYesDate);
		//UNLOAD_TIME_TO.setDefaultValue(Util.getCurTime());

	    
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setWidth(FormUtil.Width);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		form.setItems(CUSTOMER,CUSTOM_ODR_NO,REFENENCE1,SHPM_NO,ODR_TIME_FROM,ODR_TIME_TO,DEPART_TIME_FROM,
				DEPART_TIME_TO,UNLOAD_TIME_FROM,UNLOAD_TIME_TO,EXEC_ORG_ID_NAME,EXEC_ORG_ID,C_ORG_FLAG);
		
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

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_DailyView view = new R_DailyView();
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