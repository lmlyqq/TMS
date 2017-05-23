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
import com.rd.client.ds.tms.BillSettlePayDS;
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
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 成本结算报表
 * @author Administrator
 *
 */
@ClassForNameAble
public class R_CustomPayView extends SGForm implements PanelFactory{
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public R_CustomPayView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = BillSettlePayDS.getInstance("V_BILL_LOAD_PAY", "TRANS_BILL_PAY");

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
		
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", Util.TI18N.SUPLR_NAME(), 110);
		ListGridField ODR_NO = new ListGridField("DOC_NO", Util.TI18N.LOAD_NO(), 120);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME",Util.TI18N.FEE_NAME(), 70);
		ListGridField FEE_BASE_NAME = new ListGridField("FEE_BAS_NAME",Util.TI18N.FEE_BASE(), 60);
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 60);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 60);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 65);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE","应付费用", 70);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实付费用", 70);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 60);
		ListGridField START_AREA_NAME=new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),100);
		ListGridField END_AREA_NAME=new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),100);
  		
		table.setFields(SUPLR_NAME, ODR_NO, FEE_NAME, FEE_BASE_NAME, BAS_VALUE,PRICE, PRE_FEE, DUE_FEE,START_AREA_NAME,END_AREA_NAME, PAY_FEE, NOTES);
	
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
		
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText ODR_NO = new SGText("DOC_NO", Util.TI18N.LOAD_NO());
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//yuanlei
		SGDateTime DEPART_TIME_FROM = new SGDateTime("DEPART_TIME_FROM", Util.TI18N.END_LOAD_TIME()+" 从");
		SGDateTime DEPART_TIME_TO = new SGDateTime("DEPART_TIME_TO", "到");
		DEPART_TIME_FROM.setDefaultValue(getCurInitDay());
//		DEPART_TIME_FROM.setStartRow(true);
		DEPART_TIME_TO.setDefaultValue(getCurTime());
//		DEPART_TIME_TO.setWidth(130);
		
		TextItem START_AREA_ID=new TextItem("START_AREA_ID");
		START_AREA_ID.setVisible(false);
		ComboBoxItem START_AREA_NAME=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());
		START_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(START_AREA_NAME,START_AREA_ID);
		
		TextItem END_AREA_ID=new TextItem("END_AREA_ID");
		END_AREA_ID.setVisible(false);
		ComboBoxItem END_AREA_NAME=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());
		END_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(END_AREA_NAME, END_AREA_ID);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);

		form.setItems(SUPLR_ID,ODR_NO, EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,START_AREA_ID,START_AREA_NAME,END_AREA_ID,END_AREA_NAME,
				DEPART_TIME_FROM,DEPART_TIME_TO);
		
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
		R_CustomPayView view = new R_CustomPayView();
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