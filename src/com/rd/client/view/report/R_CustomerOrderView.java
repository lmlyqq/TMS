package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.TrackExportDS;
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
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class R_CustomerOrderView extends SGForm implements PanelFactory{
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public R_CustomerOrderView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = TrackExportDS.getInstance("V_SHPM_TRACK");

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
		
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","承运单号",100);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",120);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",120);
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME","要求到达提货点日期",120);
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME","要求到达日期",120);
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W","吨位",60);
		ListGridField UDF4 = new ListGridField("UDF4","开票方（简写）", 80);
  		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME","承运商", 90);
	    ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号", 80);
  		ListGridField DRIVER = new ListGridField("DRIVER","驾驶员姓名",80);
  		ListGridField MOBILE = new ListGridField("MOBILE","驾驶员联系方式",85);
  		ListGridField DUE_FEE = new ListGridField("DUE_FEE","运费（含开票点）",80);
  		ListGridField EQUIPNO = new ListGridField("EQUIPNO","温度记录编号（含GPS和温度记录仪）",90);
  		ListGridField ARRIVE_WHSE_TIME = new ListGridField("ARRIVE_WHSE_TIME","实际到达提货点时间",120);
  		ListGridField START_LOAD_TIME = new ListGridField("START_LOAD_TIME","实际装货时间",120);
  		ListGridField END_LOAD_TIME =new ListGridField("END_LOAD_TIME","实际离开提货点时间",120);
  		ListGridField CURRENT_LOC  =new ListGridField("CURRENT_LOC ","在途跟踪",80);
  		ListGridField TEMPERATURE = new ListGridField("TEMPERATURE", "在途温度", 80);  
  		ListGridField ARRIVE_DATE =new ListGridField("ARRIVE_DATE","实际到达卸货点日期",120);
  		ListGridField CAST_BILL_TIME = new ListGridField("CAST_BILL_TIME", "卸货投单时间",120);  
  		ListGridField START_UNLOAD_TIME = new ListGridField("START_UNLOAD_TIME","卸货开始时间",120);
  		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME","卸货结束时间",120);
  		ListGridField UDF1 =new ListGridField("UDF1","卸货温度",60);
  		ListGridField TRACK_NOTES =new ListGridField("TRACK_NOTES","异常备注",160);
  		ListGridField ADDITIONAL = new ListGridField("ADDITIONAL", "额外费用录入", 90);  
  		
  		
		table.setFields(CUSTOM_ODR_NO,LOAD_NAME,UNLOAD_NAME,PRE_LOAD_TIME,PRE_UNLOAD_TIME,TOT_GROSS_W,UDF4,SUPLR_NAME,PLATE_NO,DRIVER,MOBILE,DUE_FEE,EQUIPNO,ARRIVE_WHSE_TIME,
				START_LOAD_TIME,END_LOAD_TIME,CURRENT_LOC,TEMPERATURE,ARRIVE_DATE,CAST_BILL_TIME,START_UNLOAD_TIME,UNLOAD_TIME,UDF1,TRACK_NOTES,ADDITIONAL);
	
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
		expButton.addClickHandler(new ExportAction(table));
	    
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, expButton);
	  
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setCellPadding(2);

		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
		
		SGText SHPM_NO =new SGText("SHPM_NO",Util.TI18N.SHPM_NO() );
		
		SGText LOAD_NO =new SGText("LOAD_NO",Util.TI18N.LOAD_NO() );
		
		SGText ODR_NO = new SGText("ODR_NO", Util.TI18N.ODR_NO());
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER());
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		
		TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID");
	    EXEC_ORG_ID.setVisible(false);
	    SGText EXEC_ORG_ID_NAME=new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
	    Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","50%");
	    EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
	    EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
	    
	    SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
	    C_ORG_FLAG.setValue(true);
		
		
		form.setItems(CUSTOM_ODR_NO,SHPM_NO,LOAD_NO,ODR_NO,CUSTOMER,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		
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
		R_CustomerOrderView view = new R_CustomerOrderView();
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