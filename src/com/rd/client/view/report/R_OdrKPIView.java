package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.OdrKPIds;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
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
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class R_OdrKPIView extends SGForm implements PanelFactory {
	private DataSource ds ;
	private SGTable table;
	private SectionStack stack;
	private SGPanel form;
	private Window searchWin;

	/*public R_OdrKPIView(String id) {
		super(id);
	}*/

	@Override
	public void createBtnWidget(ToolStrip strip) {
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(ds, createSearchForm(form), stack.getSection(0)).getViewPanel();
				}else {
					searchWin.show();
				}
			}
		});
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN);
		expButton.addClickHandler(new ExportAction(table));
		
		strip.setMembers(searchButton,expButton);
	}
	
	private DynamicForm createSearchForm(SGPanel form){
		form.setDataSource(ds);
		
		TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO", "客户单号");
		SGText ODR_NO = new SGText("ODR_NO", Util.TI18N.ODR_NO());
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		
		SGText ADDTIME_FROM=new SGText("ADDTIME_FROM",Util.TI18N.ORD_ADDTIME_FROM());
		Util.initDateTime(form, ADDTIME_FROM);
		SGText ADDTIME_TO=new SGText("ADDTIME_TO"," 到");
		Util.initDateTime(form, ADDTIME_TO);
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setValue(true);
		
		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,CUSTOM_ODR_NO,ODR_NO,BIZ_TYP,ADDTIME_FROM,ADDTIME_TO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout lay = new VLayout();
		ds = OdrKPIds.getInstance("R_KPI_ORDER","R_KPI_ORDER");
		form = new SGPanel();
		
		createList();
		
		ToolStrip strip =new ToolStrip();
		strip.setWidth("100%");
		strip.setHeight(20);
		strip.setAlign(Alignment.RIGHT);
		strip.setMembersMargin(4);
		createBtnWidget(strip);
		
		lay.setMembers(strip,stack);
		
		return lay;
	}
	
	private void createList(){
		table = new SGTable(ds,"100%","100%",false,true,false);
		
		ListGridField ODR_NO = new ListGridField("ODR_NO",Util.TI18N.ODR_NO(),110);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.ODR_NO(),120);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","客户单号",80);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),150);
		ListGridField BIZ_TYP_NAME = new ListGridField("BIZ_TYP_NAME",Util.TI18N.BIZ_TYP(),60);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),60);
		ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2","起点",80);
		ListGridField LOAD_ADDRESS = new ListGridField("LOAD_ADDRESS",Util.TI18N.LOAD_ADDRESS(),200);
		ListGridField UNLOAD_AREA_NAME2 = new ListGridField("UNLOAD_AREA_NAME2","终点",80);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),200);
		ListGridField UNLOAD_CONTACT = new ListGridField("UNLOAD_CONTACT",Util.TI18N.UNLOAD_CONTACT(),80);
		ListGridField UNLOAD_TEL = new ListGridField("UNLOAD_TEL",Util.TI18N.UNLOAD_TEL(),100);
//		ListGridField AUDIT_FLAG = new ListGridField("AUDIT_FLAG","审核及时",80);
//		AUDIT_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField LOAD_FLAG = new ListGridField("LOAD_FLAG","发货及时",80);
		LOAD_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField UNLOAD_FLAG = new ListGridField("UNLOAD_FLAG","收货及时",80);
		UNLOAD_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField POD_FLAG = new ListGridField("POD_FLAG","回单及时",80);
		POD_FLAG.setType(ListGridFieldType.BOOLEAN);
//		ListGridField PRE_AUDIT_TIME = new ListGridField("PRE_AUDIT_TIME",Util.TI18N.PRE_AUDIT_TIME(),150);
//		ListGridField AUDIT_TIME = new ListGridField("AUDIT_TIME",Util.TI18N.AUDIT_TIME(),150);
		
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME","要求发货时间",120);
		ListGridField LOAD_TIME = new ListGridField("LOAD_TIME","实际发货时间",120);
		ListGridField LOAD_HOURS = new ListGridField("LOAD_HOURS","发货延时",80);
		
		ListGridField ARRIVE_WHSE_TIME = new ListGridField("ARRIVE_WHSE_TIME","到场时间",120);
		ListGridField START_LOAD_TIME = new ListGridField("START_LOAD_TIME","开始装车时间",120);
		ListGridField END_LOAD_TIME = new ListGridField("END_LOAD_TIME","完成装车时间",120);	
		ListGridField LOAD_WAIT_HOURS = new ListGridField("LOAD_WAIT_HOURS","装货等待时长",80);
		ListGridField PICKLOAD_HOURS = new ListGridField("PICKLOAD_HOURS","装货耗时",80);
		
		ListGridField CAST_BILL_TIME = new ListGridField("CAST_BILL_TIME","投单时间",120);
		ListGridField START_UNLOAD_TIME = new ListGridField("START_UNLOAD_TIME","开始卸货时间",120);
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME","完成卸货时间",120);
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME","要求到货时间",120);
		ListGridField UNLOAD_HOURS = new ListGridField("UNLOAD_HOURS","到货延时",80);
		ListGridField UNLOAD_WAIT_HOURS = new ListGridField("UNLOAD_WAIT_HOURS","卸货等待时长",80);
		ListGridField UNLOADING_HOURS = new ListGridField("UNLOADING_HOURS","卸货耗时",80);
		
		ListGridField PRE_POD_TIME = new ListGridField("PRE_POD_TIME","预计回单时间",120);
		ListGridField POD_TIME = new ListGridField("POD_TIME","实际回单时间",120);
		ListGridField POD_HOURS = new ListGridField("POD_HOURS","回单延时",80);
		



		
		table.setFields(ODR_NO,SHPM_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,BIZ_TYP_NAME,STATUS_NAME,LOAD_AREA_NAME2,LOAD_ADDRESS,UNLOAD_AREA_NAME2,UNLOAD_ADDRESS,UNLOAD_CONTACT,
				UNLOAD_TEL,LOAD_FLAG,UNLOAD_FLAG,POD_FLAG,PRE_LOAD_TIME,LOAD_TIME,LOAD_HOURS,ARRIVE_WHSE_TIME,START_LOAD_TIME,END_LOAD_TIME,LOAD_WAIT_HOURS,
				PICKLOAD_HOURS,CAST_BILL_TIME,START_UNLOAD_TIME,UNLOAD_TIME,PRE_UNLOAD_TIME,UNLOAD_HOURS,UNLOAD_WAIT_HOURS,UNLOADING_HOURS,PRE_POD_TIME,POD_TIME,POD_HOURS);
		
		stack = new SectionStack();
		stack.setWidth("100%");
		stack.setHeight("100%");
		SectionStackSection list = new SectionStackSection(Util.TI18N.LISTINFO());
		list.addItem(table);
		list.setExpanded(true);
		list.setControls(new SGPage(table, true).initPageBtn());
		
		stack.addSection(list);
	}
	
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			form.destroy();
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_OdrKPIView view = new R_OdrKPIView();
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
