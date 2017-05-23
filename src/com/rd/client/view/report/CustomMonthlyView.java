package com.rd.client.view.report;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CustomExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.CustomMonthlyDS;
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
 * 客户月结清算
 * @author Lang
 *
 */
@ClassForNameAble
public class CustomMonthlyView extends SGForm implements PanelFactory {
	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private TextItem CUSTOMER_ID;
	private TextItem PARENT_CUSTOMER_ID;
	
	/*public CustomMonthlyView(String id) {
		super(id);
	}*/

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
        expButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				HashMap<String, String> paramMap = new HashMap<String, String>();
				String odrTime = "";
				if(ObjUtil.isNotNull(ODR_TIME_FROM.getValue())){
					odrTime += ODR_TIME_FROM.getValue().toString();
				}
				if(ObjUtil.isNotNull(ODR_TIME_TO.getValue())){
					odrTime += " - "+ ODR_TIME_TO.getValue().toString();
				}
				paramMap.put("CUSTOMER_ID", (String)CUSTOMER_ID.getValue());
				paramMap.put("ODR_TIME", odrTime);
				new CustomExportAction(table, paramMap).onClick(event);
			}
		});
    
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
		
//		CUSTOMER=new SGCombo("CUSTOMER_ID",ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()),true);
		CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);

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
		
		SGText REFENENCE1 = new SGText("REFENENCE1", Util.TI18N.REFENENCE1());
		
		//yuanlei
		ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ORD_ADDTIME()+" 从");
		ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_FROM.setDefaultValue(getCurInitDay());
		ODR_TIME_TO.setDefaultValue(getCurTime());
		ODR_TIME_TO.setWidth(130);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		PARENT_CUSTOMER_ID=new TextItem("PARENT_CUSTOMER_ID");
		PARENT_CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem PARENT_CUSTOMER_NAME=new ComboBoxItem("PARENT_CUSTOMER_NAME",Util.TI18N.PARENT_CUSTOMER_ID());
		PARENT_CUSTOMER_NAME.setStartRow(true);
		PARENT_CUSTOMER_NAME.setWidth(120);
		PARENT_CUSTOMER_NAME.setColSpan(2);
		PARENT_CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		PARENT_CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(PARENT_CUSTOMER_NAME, PARENT_CUSTOMER_ID);
		
		SGCombo BILLING = new SGCombo("BILLING", "是否计费");
		LinkedHashMap<String, String> billingMap = new LinkedHashMap<String, String>();
		billingMap.put("", "");
		billingMap.put("true", "已计费");
		billingMap.put("false", "未计费");
		BILLING.setValueMap(billingMap);
		BILLING.setWidth(120);
		BILLING.setColSpan(2);
		BILLING.setDefaultValue("false");

		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,CUSTOM_ODR_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,ODR_NO,REFENENCE1,ODR_TIME_FROM,ODR_TIME_TO,PARENT_CUSTOMER_ID,PARENT_CUSTOMER_NAME,BILLING);
		
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = CustomMonthlyDS.getInstance("V_CUSTOM_MONTHLY", "V_CUSTOM_MONTHLY");

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
		
		ListGridField SEQ=new ListGridField("SEQ","序号",50);
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", "日期", 100);
		ListGridField REFENENCE1 = new ListGridField("REFENENCE1", Util.TI18N.REFENENCE1(), 100);
		ListGridField PARENT_CUSTOMER_NAME = new ListGridField("PARENT_CUSTOMER_NAME", "隶属客户", 100);
		ListGridField GEN_METHOD = new ListGridField("GEN_METHOD", "月结帐号", 100);
		ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2", "寄件地区", 100);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", "寄件公司名", 80);
		ListGridField LOAD_TEL = new ListGridField("LOAD_TEL","寄件公司电话", 80);
		ListGridField UNLOAD_AREA_NAME2 = new ListGridField("UNLOAD_AREA_NAME2","到件地区", 60);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到件客户名称", 80);
		ListGridField UNLOAD_TEL = new ListGridField("UNLOAD_TEL", "到件客户电话", 80);
		ListGridField BIZ_TYP = new ListGridField("BIZ_TYP", "产品类型", 65);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME", "货物名称", 100);
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","发运数量", 65);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL","发运体积", 65);
		ListGridField TOT_G_WGT = new ListGridField("TOT_G_WGT", "发运重量", 110);  
		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1","温度要求", 60);
		ListGridField PAYMENT = new ListGridField("PAYMENT","付款方式", 60);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", "费用明细", 60);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE", "费用", 65);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", "折扣", 60);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE","应收费用", 65);
		ListGridField SIGNATARY = new ListGridField("SIGNATARY","经手人", 65);
		ListGridField UDF1 = new ListGridField("UDF1", "收派员", 110); 
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 60);
  		
		table.setFields(SEQ,ODR_TIME, REFENENCE1, PARENT_CUSTOMER_NAME, GEN_METHOD, LOAD_AREA_NAME2, LOAD_NAME,LOAD_TEL ,UNLOAD_AREA_NAME2, UNLOAD_NAME, 
				UNLOAD_TEL,BIZ_TYP, SKU_NAME, TOT_QNTY, TOT_VOL, TOT_G_WGT, TEMPERATURE1, PAYMENT, FEE_NAME, 
				PRE_FEE, DISCOUNT_RATE, DUE_FEE, SIGNATARY, UDF1, NOTES);
	
	}

	@Override
	public void initVerify() {
		// TODO Auto-generated method stub
		
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
		CustomMonthlyView view = new CustomMonthlyView();
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
