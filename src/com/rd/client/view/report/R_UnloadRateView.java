package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.action.kpi.ExportUnloadRateAction;
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
import com.rd.client.ds.report.UnloadRateDS;
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
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class R_UnloadRateView extends SGForm implements PanelFactory {
	
	/**
	 * 到库及时率报表 
	 * wangJun 2011-10-30
	 * 
	 */
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public R_UnloadRateView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = UnloadRateDS.getInstance("R_UNLOAD_RATE", "R_UNLOAD_RATE");

		table = new SGTable(ds, "100%", "70%");
        createListFields(table);
        table.setShowFilterEditor(false);
        
        //创建按钮布局
		createBtnWidget(toolStrip);
		section = createSection(table, null, true, true);
		initVerify();  
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		
		final Menu menu = new Menu();
		menu.setWidth(140);
	   
	    MenuItem headExport = new MenuItem("作业单导出",StaticRef.ICON_CONFIRM);
	    headExport.addClickHandler(new ExportUnloadRateAction(table));
	    menu.addItem(headExport);
	    
	    table.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				menu.showContextMenu();
                event.cancel();
			}
		});
    
		    
		
		return main;
		
	}
	//布局列表信息按钮
	private void createListFields(SGTable table) {
		
		/**
  		作业单号	客户订单号	调度单号	供应商	司机	司机电话	发货方	收货方	收货区域	收货地址	作业单状态	
  		当前位置	订单时间	派发时间	到库登记时间	发运时间	离库登记时间	预计到货时间	实际到货时间	到货时长	
  		到货预期天数	签收备注	司机服务态度	客户满意度	运输异常	异常描述        
        **/
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),100);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),90);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),100);
		ListGridField SUPLR_NAME =new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),70);
		ListGridField PLATE_NO =new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);
		ListGridField DRIVER1 =new ListGridField("DRIVER",Util.TI18N.DRIVER1(),60);
  		ListGridField MOBILE1 = new ListGridField("MOBILE", Util.TI18N.MOBILE(), 85);  
  		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),120);
		ListGridField UNLOAD_NAME =new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),140);
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME(),70);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),140);
		ListGridField SHPM_STATUS = new ListGridField("SHPM_STATUS_NAME",Util.TI18N.SHPM_STSTUS(),70);
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC",Util.TI18N.CURRENT_LOC(),70);
		ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(),90);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),90);
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),80);
		ListGridField QNTY = new ListGridField("QNTY","包装数量",60);
		ListGridField QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.SHPM_QNTY(),60);
		ListGridField ODR_TIME =new ListGridField("ODR_TIME",Util.TI18N.ODR_TIME(),110);
		ListGridField ASSIGN_TIME = new ListGridField("ASSIGN_TIME", Util.TI18N.ASSIGN_TIME(),110); 
		ListGridField PRE_WHSE_TIME = new ListGridField("PRE_WHSE_TIME","预计到库时间",110);
		ListGridField ARRIVE_WHSE_TIME = new ListGridField("ARRIVE_WHSE_TIME","到库登记时间",110);
  		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME",Util.TI18N.MANAGE_END_LOAD_TIME(),110);
  		ListGridField LEAVE_WHSE_TIME =new ListGridField("LEAVE_WHSE_TIME","离库登记时间",110);
  		ListGridField PRE_UNLOAD_TIME =new ListGridField("PRE_UNLOAD_TIME","预计到达时间",110);
  		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME", "实际到达时间", 110);  
//  		ListGridField DEPART_NEED_TIME = new ListGridField("DEPART_NEED_TIME","发货时长",90);
//  		ListGridField UNLOAD_DAY =new ListGridField("UNLOAD_DAY","到货时长",110);
  		ListGridField OVERDUE_DAY = new ListGridField("UNLOAD_DELAY_DAYS", "到货逾期天数", 110); 
  		ListGridField TRACK_NOTES = new ListGridField("TRACK_NOTES","签收备注",110);
  		ListGridField SERVICE_CODE = new ListGridField("SERVICE_CODE",Util.TI18N.DRIVER_SERVICE(),110);
  		ListGridField SATISFY_CODE = new ListGridField("SATISFY_CODE",Util.TI18N.CUSTOMER_SERVICE(),110);
  		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT",Util.TI18N.ABNOMAL_STAT(),110);
  		ListGridField ABNOMAL_NOTES = new ListGridField("ABNOMAL_NOTES",Util.TI18N.ABNOMAL_NOTE(),110);
//  		ListGridField DISPATCHER = new ListGridField("DISPATCHER","审核员",110);
  		ListGridField DISPATCH_TIME = new ListGridField("DISPATCH_TIME","审核时间",110);
  		ListGridField CONTENT = new ListGridField("UNLOAD_CONTACT","联系人",110);
  		ListGridField TEL = new ListGridField("UNLOAD_TEL","联系电话",110);
  		ListGridField SIGNATARY = new ListGridField("SIGNATARY","跟单员",110);
  		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),110);
  		
  		
		table.setFields(SHPM_NO,CUSTOM_ODR_NO,LOAD_NO,SUPLR_NAME,PLATE_NO,DRIVER1,MOBILE1,
				LOAD_NAME,UNLOAD_NAME,UNLOAD_AREA_NAME,UNLOAD_ADDRESS,SHPM_STATUS,
				CURRENT_LOC,SKU,SKU_NAME,SKU_SPEC,QNTY,QNTY_EACH,ODR_TIME,ASSIGN_TIME,PRE_WHSE_TIME,ARRIVE_WHSE_TIME,DEPART_TIME,
				LEAVE_WHSE_TIME,PRE_UNLOAD_TIME,UNLOAD_TIME,OVERDUE_DAY,
				TRACK_NOTES,SERVICE_CODE,SATISFY_CODE,ABNOMAL_STAT,ABNOMAL_NOTES,DISPATCH_TIME,CONTENT,TEL,SIGNATARY,NOTES);
	
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
					searchWin.setHeight(390);
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
	protected DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		/**
		查询条件			
		客户	客户单号	调度单号	供应商
		作业单状态	到	订单时间	到
		派发时间	到	发运时间	到
		发货方		收货方	
		发货区域	收货区域	含下级机构	
		**/
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		SGText LOAD_NO = new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(120);
		
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		//yuanlei 2012-09-11 去除“发货区域”查询条件
		//SGText UNLOAD_AREA_NAME = new SGText("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());
		//yuanlei
		SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME());
		//yuanlei 2012-09-11 去除“收货区域”查询条件
		//SGText LOAD_AREA_NAME = new SGText("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());
		//yuanlei
		
		SGCombo SHPM_STATUS_FROM = new SGCombo("SHPM_STATUS_FROM", Util.TI18N.SHPM_STSTUS()+" 从");
		SGCombo SHPM_STATUS_TO = new SGCombo("SHPM_STATUS_TO", "到");
		Util.initStatus(SHPM_STATUS_FROM, StaticRef.SHPMNO_STAT, " ");
		Util.initStatus(SHPM_STATUS_TO, StaticRef.SHPMNO_STAT, " ");
		
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", "订单时间"+" 从");
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_FROM.setWidth(130);
		ODR_TIME_TO.setWidth(130);
		
		SGDateTime ARRIVE_WHSE_TIME_FROM = new SGDateTime("ARRIVE_WHSE_TIME_FROM", "到库时间从");
		SGDateTime ARRIVE_WHSE_TIME_TO = new SGDateTime("ARRIVE_WHSE_TIME_TO", "到");
		ARRIVE_WHSE_TIME_FROM.setWidth(130);
		ARRIVE_WHSE_TIME_TO.setWidth(130);
		
		SGDateTime ASSIGN_TIME_FROM = new SGDateTime("ASSIGN_TIME_FROM", Util.TI18N.ASSIGN_TIME()+" 从");
		SGDateTime ASSIGN_TIME_TO = new SGDateTime("ASSIGN_TIME_TO", "到");
		ASSIGN_TIME_FROM.setWidth(130);
		ASSIGN_TIME_TO.setWidth(130);
		SGDateTime DEPART_TIME_FROM = new SGDateTime("DEPART_TIME_FROM","发运时间  从");
		SGDateTime DEPART_TIME_TO = new SGDateTime("DEPART_TIME_TO", "到");
		DEPART_TIME_FROM.setWidth(130);
		DEPART_TIME_TO.setWidth(130);
		
		SGDateTime UNLOAD_TIME_FROM = new SGDateTime("UNLOAD_TIME_FROM","到货时间  从");
		SGDateTime UNLOAD_TIME_TO = new SGDateTime("UNLOAD_TIME_TO", "到");
		DEPART_TIME_FROM.setWidth(130);
		DEPART_TIME_TO.setWidth(130);
		
		SGDateTime UNLOAD_OPER_TIME_FROM = new SGDateTime("UNLOAD_OPER_TIME_FROM","到货操作时间  从");
		SGDateTime UNLOAD_OPER_TIME_TO = new SGDateTime("UNLOAD_OPER_TIME_TO", "到");
		DEPART_TIME_FROM.setWidth(130);
		DEPART_TIME_TO.setWidth(130);
		
		SGCheck ARRIVE_WHSE_FLAG = new SGCheck("ARRIVE_WHSE_FLAG", "未准时到库");
		ARRIVE_WHSE_FLAG.setColSpan(2);
		
		SGCheck LOAD_FLAG = new SGCheck("LOAD_FLAG","未准时发运");
		LOAD_FLAG.setColSpan(2);
		
		SGCheck UNLOAD_FLAG = new SGCheck("UNLOAD_FLAG", "未准时到货");
		UNLOAD_FLAG.setColSpan(2);
		
		SGCheck POD_FLAG = new SGCheck("POD_FLAG", "未准时回单");
		POD_FLAG.setColSpan(2);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(2);
		
		final SGText CurTime = new SGText("CurTime", " ");
		CurTime.setVisible(false);
		final SGCheck shoud_com_flag = new SGCheck("shoud_com_flag","应到未到");
		shoud_com_flag.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				CurTime.setValue(Util.getCurTime());
			}
		});
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构	
		
		C_RDC_FLAG.setColSpan(2);
		
		//yuanlei 2012-09-11 去除“发货区域”和“收货区域”查询条件
		/*form.setItems(CUSTOMER,CUSTOM_ODR_NO,LOAD_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
				LOAD_NAME,LOAD_AREA_NAME,UNLOAD_NAME,UNLOAD_AREA_NAME,
				SHPM_STATUS_FROM,SHPM_STATUS_TO,ODR_TIME_FROM,
				ODR_TIME_TO,ASSIGN_TIME_FROM,ASSIGN_TIME_TO,DEPART_TIME_FROM,
				DEPART_TIME_TO,UNLOAD_TIME_FROM,UNLOAD_TIME_TO,
				UNLOAD_OPER_TIME_FROM,UNLOAD_OPER_TIME_TO,
				ARRIVE_WHSE_FLAG,LOAD_FLAG,UNLOAD_FLAG,POD_FLAG,
				C_ORG_FLAG,C_RDC_FLAG,shoud_com_flag,CurTime);*/
		form.setItems(CUSTOMER,CUSTOM_ODR_NO,LOAD_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
				SHPM_STATUS_FROM,SHPM_STATUS_TO,LOAD_NAME,UNLOAD_NAME,
				ODR_TIME_FROM,ODR_TIME_TO,
				ARRIVE_WHSE_TIME_FROM,ARRIVE_WHSE_TIME_TO,ASSIGN_TIME_FROM,ASSIGN_TIME_TO,DEPART_TIME_FROM,
				DEPART_TIME_TO,UNLOAD_TIME_FROM,UNLOAD_TIME_TO,
				UNLOAD_OPER_TIME_FROM,UNLOAD_OPER_TIME_TO,
				ARRIVE_WHSE_FLAG,LOAD_FLAG,UNLOAD_FLAG,POD_FLAG,
				C_ORG_FLAG,C_RDC_FLAG,shoud_com_flag,CurTime);
		//yuanlei
		
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
		R_UnloadRateView view = new R_UnloadRateView();
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