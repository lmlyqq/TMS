package com.rd.client.view.report;

import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.BillSettRecDS2;
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

@ClassForNameAble
public class R_RecView extends SGForm implements PanelFactory {
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public R_RecView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = BillSettRecDS2.getInstance("V_BILL_REC2", "TRANS_BILL_RECE");

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
		
		ListGridField BIZ_TYP_NAME=new ListGridField("BIZ_TYP_NAME",Util.TI18N.BIZ_TYP(),80);
		ListGridField CUSTOMER_CODE=new ListGridField("CUSTOMER_CODE",Util.TI18N.CUSTOMER_CODE(),90);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", Util.TI18N.CUSTOMER(), 90);
		ListGridField PARENT_CUSTOMER_NAME = new ListGridField("PARENT_CUSTOMER_NAME", Util.TI18N.PARENT_CUSTOMER_ID(), 90);
		ListGridField REFENENCE1 = new ListGridField("REFENENCE1", Util.TI18N.REFENENCE1(), 100);
		ListGridField ODR_NO = new ListGridField("ODR_NO", Util.TI18N.ODR_NO(), 100);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 80);
		
		ListGridField CUSTOM_ATTR_NAME=new ListGridField("CUSTOM_ATTR_NAME",Util.TI18N.CUSTOM_ATTR(),65);
		ListGridField MON_DEL_ACCOUNT=new ListGridField("MON_DEL_ACCOUNT",Util.TI18N.MON_DEL_ACCOUNT(),80);
		ListGridField UDF2=new ListGridField("UDF2",Util.TI18N.TAX_RATE(),60);
		ListGridField LOAD_AREA_ID2=new ListGridField("LOAD_AREA_ID2");
		LOAD_AREA_ID2.setHidden(true);
		ListGridField LOAD_AREA_NAME2=new ListGridField("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME(),80);
		ListGridField LOAD_AREA_CODE2=new ListGridField("LOAD_AREA_CODE2",Util.TI18N.LOAD_AREA_CODE2(),100);
		ListGridField UNLOAD_AREA_ID2=new ListGridField("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setHidden(true);
		ListGridField UNLOAD_AREA_NAME2=new ListGridField("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME(),80);
		ListGridField UNLOAD_AREA_CODE2=new ListGridField("UNLOAD_AREA_CODE2",Util.TI18N.UNLOAD_AREA_CODE2(),100);
		ListGridField UDF3=new ListGridField("UDF3",Util.TI18N.SELL_BELONG_TO(),80);
		ListGridField UDF4=new ListGridField("UDF4",Util.TI18N.SELL_AREA_CODE(),100);
		
		ListGridField FEE_NAME = new ListGridField("FEE_NAME",Util.TI18N.FEE_NAME(), 70);
		ListGridField FEE_BASE_NAME = new ListGridField("FEE_BASE_NAME",Util.TI18N.FEE_BASE(), 60);
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(), 60);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 60);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", Util.TI18N.PRE_FEE(), 65);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", Util.TI18N.DISCOUNT(), 60);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE(), 65);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实收费用", 65);
		
		ListGridField TAX_VAL=new ListGridField("TAX_VAL",Util.TI18N.TAX_VAL(),65);
		ListGridField VAL_AFT_TAX=new ListGridField("VAL_AFT_TAX",Util.TI18N.VAL_AFT_TAX(),65);
		
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 110);  
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 60);
		ListGridField RECE_STAT_NAME=new ListGridField("RECE_STAT_NAME",Util.TI18N.COLLECTE_STATE(),80);
  		
		table.setFields(BIZ_TYP_NAME,CUSTOMER_CODE,CUSTOMER_NAME, PARENT_CUSTOMER_NAME, REFENENCE1, ODR_NO, CUSTOM_ODR_NO,CUSTOM_ATTR_NAME,MON_DEL_ACCOUNT,UDF2,LOAD_AREA_ID2,LOAD_AREA_NAME2,
				LOAD_AREA_CODE2,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_AREA_CODE2,UDF3,UDF4,
				RECE_STAT_NAME ,FEE_NAME, FEE_BASE_NAME, BAS_VALUE,PRICE, PRE_FEE, DISCOUNT_RATE, DUE_FEE, PAY_FEE,TAX_VAL,VAL_AFT_TAX, ODR_TIME, NOTES);
	
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
	        IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.RECVIEW_P1_01);
	        expButton.addClickHandler(new ExportAction(table));
	    
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton, expButton);
	  
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
//		form.setCellPadding(2);
		
//		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
//		Util.initCustComboValue(CUSTOMER, "");
		
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		SGText ODR_NO = new SGText("DOC_NO", Util.TI18N.ODR_NO());
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		SGText REFENENCE1 =new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
		
		SGCombo RECE_STAT=new SGCombo("RECE_STAT",Util.TI18N.COLLECTE_STATE());//收款状态
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("0", "");
		valueMap.put("10", StaticRef.NO_RECE);
		valueMap.put("15", "部分收款");
		valueMap.put("20", StaticRef.RECE_STAT);
		RECE_STAT.setValueMap(valueMap);
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setColSpan(2);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//yuanlei
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ORD_ADDTIME()+" 从");
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_TO.setWidth(130);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		//业务类型
		SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
		Util.initComboValue(BIZ_TYP, "BAS_CODES", "ID", "NAME_C","id<>'"+StaticRef.B2C+"' and prop_code='BIZ_TYP'");
		
		//客户属性
		SGCombo CUSTOM_ATTR=new SGCombo("CUSTOM_ATTR",Util.TI18N.CUSTOM_ATTR());
		CUSTOM_ATTR.setColSpan(2);
		Util.initCodesComboValue(CUSTOM_ATTR,"CUSTOM_ATTR");
		
		//收发货区域
		TextItem LOAD_AREA_ID2=new TextItem("LOAD_AREA_ID2");
		LOAD_AREA_ID2.setVisible(false);
		ComboBoxItem LOAD_AREA_NAME2=new ComboBoxItem("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(LOAD_AREA_NAME2, LOAD_AREA_ID2);
		
		TextItem UNLOAD_AREA_ID2=new TextItem("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME2=new ComboBoxItem("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME2, UNLOAD_AREA_ID2);
		
		//收发货网点代码
		SGText LOAD_AREA_CODE2=new SGText("LOAD_AREA_CODE2",Util.TI18N.LOAD_AREA_CODE2());
		
		SGText UNLOAD_AREA_CODE2=new SGText("UNLOAD_AREA_CODE2",Util.TI18N.UNLOAD_AREA_CODE2());
		
		//销售归属地
		SGText SELL_BELONG_TO=new SGText("UDF3",Util.TI18N.SELL_BELONG_TO());
		
		//销售区域网点代码
		SGText SELL_AREA_CODE=new SGText("UDF4",Util.TI18N.SELL_AREA_CODE());
		
		final TextItem PARENT_CUSTOMER_ID=new TextItem("PARENT_CUSTOMER_ID");
		PARENT_CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem PARENT_CUSTOMER_NAME=new ComboBoxItem("PARENT_CUSTOMER_NAME",Util.TI18N.PARENT_CUSTOMER_ID());
		PARENT_CUSTOMER_NAME.setStartRow(false);
		PARENT_CUSTOMER_NAME.setWidth(120);
		PARENT_CUSTOMER_NAME.setColSpan(2);
		PARENT_CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		PARENT_CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(PARENT_CUSTOMER_NAME, PARENT_CUSTOMER_ID);

		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,CUSTOM_ODR_NO,ODR_NO,REFENENCE1,BIZ_TYP,CUSTOM_ATTR,LOAD_AREA_ID2,LOAD_AREA_NAME2,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,RECE_STAT,SELL_BELONG_TO,
				ODR_TIME_FROM,ODR_TIME_TO,LOAD_AREA_CODE2,UNLOAD_AREA_CODE2,SELL_AREA_CODE,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG, PARENT_CUSTOMER_ID,PARENT_CUSTOMER_NAME);
		
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
		R_RecView view = new R_RecView();
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
