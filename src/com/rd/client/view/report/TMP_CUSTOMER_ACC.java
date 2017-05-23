package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportByProAction;
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
import com.rd.client.ds.report.TmpCustomerAccDS;
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
 * 经销商对账报表
 *
 */
@ClassForNameAble
public class TMP_CUSTOMER_ACC extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	
	/*public TMP_CUSTOMER_ACC(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = TmpCustomerAccDS.getInstance("TMP_CUSTOMER_ACC", "QUERY_CUSTOMER_ACC");

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
		return main;

	}

	@Override
	public void createForm(DynamicForm form) {

	}

	//布局列表信息按钮
	private void createListFields(SGTable table) {
		//回单序列号       调度单号           客户单号	  作业单号	收货方	货品代码	货品名称	规格	单位	订单数量	实发数量	
		//实收数量	      未收数量	  作业单状态	订单日期	发货日期	收货日期	回单日期	供应商	执行机构	车牌号				

		ListGridField SERIAL_NUM = new ListGridField("SERIAL_NUM", "回单序列号", 80);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO", Util.TI18N.LOAD_NO(), 120);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO", Util.TI18N.SHPM_NO(), 120);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 120);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 160);
		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 70);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC", Util.TI18N.SKU_SPEC(), 70);
		ListGridField TRANS_UOM = new ListGridField("UOM", Util.TI18N.UOM(), 70);
		ListGridField ODR_QNTY = new ListGridField("ODR_QNTY", Util.TI18N.ODR_QNTY(), 70);
		ListGridField LOAD_QNTY = new ListGridField("LOAD_QNTY", Util.TI18N.LD_QNTY(), 70);
		ListGridField UNLD_QNTY = new ListGridField("UNLOAD_QNTY", "实收数量", 70);
		ListGridField NO_UNLD_QNTY = new ListGridField("NO_UNLOAD_QNTY","未收数量", 70);
		ListGridField SHPM_STAT_NAME = new ListGridField("SHPM_STAT_NAME", "作业单状态", 80);
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 100);
		ListGridField LOAD_TIME = new ListGridField("LOAD_TIME", "发货时间", 100);
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME", "收货时间", 100);
		ListGridField POD_TIME = new ListGridField("POD_TIME", Util.TI18N.POD_TIME(), 100);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID_NAME", Util.TI18N.SUPLR_NAME(), 80);
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID(), 120);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATE_NO(), 80);
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME(), 80);
		ListGridField TRANS_NOTES = new ListGridField("TRANS_NOTES", "配车备注", 80);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", Util.TI18N.LOAD_NAME(), 80);
		
		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(LOAD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(NO_UNLD_QNTY, StaticRef.QNTY_FLOAT);
		
		table.setFields(SERIAL_NUM,LOAD_NO,CUSTOM_ODR_NO,SHPM_NO,UNLOAD_NAME,
				SKU,SKU_NAME,SKU_SPEC,TRANS_UOM,ODR_QNTY,LOAD_QNTY,UNLD_QNTY,
				NO_UNLD_QNTY,SHPM_STAT_NAME,ODR_TIME,LOAD_TIME,UNLOAD_TIME,
				POD_TIME,SUPLR_NAME,EXEC_ORG_ID_NAME,PLATE_NO,UNLOAD_AREA_NAME,TRANS_NOTES,LOAD_NAME);
		
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
	        expButton.addClickHandler(new ExportByProAction(table, "addtime desc"));
	    
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton,expButton);
		
	}
	
	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//-
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		CUSTOMER.setWidth(130);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initComboValue(SUPLR_NAME, "BAS_SUPPLIER", "ID", "SUPLR_CNAME");
		SUPLR_NAME.setWidth(130);
		SGText EXEC_ORG_ID = new SGText("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(130);
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		CUSTOM_ODR_NO.setWidth(130);
		
		//2
		SGCombo SHPM_STAT_FROM = new SGCombo("SHPM_STAT_FROM", Util.TI18N.SHPM_STSTUS()+" 从");
		SGCombo SHPM_STAT_TO = new SGCombo("SHPM_STAT_TO", "到");
		Util.initStatus(SHPM_STAT_FROM, StaticRef.SHPMNO_STAT, " ");
		Util.initStatus(SHPM_STAT_TO, StaticRef.SHPMNO_STAT, " ");
		SHPM_STAT_FROM.setWidth(130);
		SHPM_STAT_TO.setWidth(130);
		final TextItem AREA_ID = new TextItem("AREA_ID");
		AREA_ID.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME.setColSpan(2);
		Util.initArea(UNLOAD_AREA_NAME, AREA_ID);
		UNLOAD_AREA_NAME.setWidth(130);
		UNLOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//3
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		UNLOAD_NAME.setWidth(130);
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", "订单时间"+" 从");
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");
		ODR_TIME_TO.setWidth(130);
		ODR_TIME_TO.setWidth(130);
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(2);
		
		SGCombo AUDIT_STAT =new SGCombo("AUDIT_STAT", "审核状态");//
		Util.initCodesComboValue(AUDIT_STAT, "AUDIT_STAT");
		
		form.setItems(CUSTOMER,SUPLR_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,CUSTOM_ODR_NO,
				SHPM_STAT_FROM,SHPM_STAT_TO,UNLOAD_AREA_NAME,UNLOAD_NAME,ODR_TIME_FROM,
				ODR_TIME_TO,AUDIT_STAT,C_ORG_FLAG);
		return form;
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
		TMP_CUSTOMER_ACC view = new TMP_CUSTOMER_ACC();
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
