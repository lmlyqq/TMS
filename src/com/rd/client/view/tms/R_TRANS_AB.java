package com.rd.client.view.tms;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
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
import com.rd.client.ds.tms.R_TRANS_AB_DS;
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

@ClassForNameAble
public class R_TRANS_AB extends SGForm implements PanelFactory {
	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	
	/*public R_TRANS_AB(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_TRANS_AB_DS.getInstance("R_TRANS_AB","R_TRANS_AB");
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
		 * 客户单号 、发货方、收货方、供应商、司机、联系电话、运输异常
		 */
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),160);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),170);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),100);
		ListGridField DRIVER = new ListGridField("DRIVER",Util.TI18N.DRIVER1(),100);
		ListGridField MOBILE = new ListGridField("MOBILE",Util.TI18N.CONT_TEL(),100);
		ListGridField ABNOMAL_STAT_NAME = new ListGridField("ABNOMAL_STAT_NAME",Util.TI18N.ABNOMAL_STAT(),100);
		ListGridField ADDWHO = new ListGridField("ADDWHO","跟踪人",70);
		ListGridField ADDTIME = new ListGridField("TRACK_TIME","跟踪时间",80);
		table.setFields(CUSTOM_ODR_NO,LOAD_NAME,UNLOAD_NAME,SUPLR_NAME,DRIVER,MOBILE,ABNOMAL_STAT_NAME,ADDWHO,ADDTIME);
	
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
					searchWin.setWidth(585);
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
		
		/**
		 * 	客户        客户单号	 执行机构          订单时间 从   到      	供应商	       跟踪事件从  到			
		 * 
		 */
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//1
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		//2
		SGDateTime ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
		SGDateTime ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		
		SGCombo SUPLR_ID =new SGCombo("SUPLR_NAME", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		//3
		SGDateTime TRACE_TIME_FORM = new  SGDateTime("TRACE_TIME_FORM", Util.TI18N.TRACE_TIME(),true);
		SGDateTime TRACE_TIME_TO = new  SGDateTime("TRACE_TIME_TO","到");
		
		SGCombo ABNOMAL_STAT = new SGCombo("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT());//运输异常
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		
		SGText ADDWHO=new SGText("ADDWHO", "跟踪人",true);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG(),false);	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		CUSTOMER.setWidth(127);
		CUSTOM_ODR_NO.setWidth(127);
		EXEC_ORG_ID_NAME.setWidth(127);
		SUPLR_ID.setWidth(127);
		ABNOMAL_STAT.setWidth(127);
		
		form.setItems(CUSTOMER,CUSTOM_ODR_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
				ODR_TIME_FROM,ODR_TIME_TO,SUPLR_ID,
				TRACE_TIME_FORM,TRACE_TIME_TO,ABNOMAL_STAT,ADDWHO,C_ORG_FLAG);
		
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
		R_TRANS_AB view = new R_TRANS_AB();
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
