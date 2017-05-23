package com.rd.client.view.warning;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.warning.PrePodExcelDS;
import com.rd.client.ds.warning.PreUnloadExcelDS;
import com.rd.client.ds.warning.PreloadExcelDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 
 * 运输管理--预警信息
 * @author wangjun
 *
 */
@ClassForNameAble
public class TimeWarnManagerView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource unloadDS;
	private DataSource podDS;
	private SGTable loadtable;
	private SGTable unloadtable;
	private SGTable podtable;
	//private Window searchWin;  --yuanlei 2011-2-16
	//private SGPanel searchForm;  yuanlei 2011-2-15
	private SectionStack loadsection;
	private SectionStack unloadsection;
	private SectionStack podsection;
	private DynamicForm pageForm1;
	private DynamicForm pageForm2;
	private DynamicForm pageForm3;
	private int tabNum = 4;
	/**
	 * 	应发未发  PRE_lOAD_EXCEL
   	 *	应到未到  PRE_UNLOAD_EXCEL
  	 *	应回未回 PRE_POD_EXCEL
	 */
	
	/*public TimeWarnManagerView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		loadDS = PreloadExcelDS.getInstance("PRE_LOAD_EXCEL", "PRE_LOAD_EXCEL");
		unloadDS = PreUnloadExcelDS.getInstance("PRE_UNLOAD_EXCEL", "PRE_UNLOAD_EXCEL");
		podDS = PrePodExcelDS.getInstance("PRE_POD_EXCEL", "PRE_POD_EXCEL");
		
		// 主布局
		HStack stack = new HStack();// 设置详细信息布局
		stack.setWidth100();
		stack.setHeight100();
		
		loadtable= new SGTable(loadDS, "100%", "100%");
		unloadtable= new SGTable(unloadDS, "100%", "100%");
		podtable= new SGTable(podDS, "100%", "100%");
		
		loadtable.setShowFilterEditor(false);
		unloadtable.setShowFilterEditor(false);
		podtable.setShowFilterEditor(false);
		
		TabSet tabset=new TabSet();
		tabset.setWidth("100%");
		tabset.setHeight("100%");
		tabset.setMargin(0);
		
		//创建Section
		loadsection = new SectionStack();
		loadsection.setWidth("100%");
		loadsection.setHeight("100%");
		
		createloadFields(loadtable);
		Tab tab1 = new Tab(Util.TI18N.LOAD_OR());//页签名称1：应发未发
		tab1.setPane(loadsection);
		tabset.addTab(tab1);
		
		final SectionStackSection listItem = new SectionStackSection();//
	    listItem.setItems(loadtable);
	    listItem.setExpanded(true);
	    pageForm1 = new SGPage(loadtable, true).initPageBtn();
	    listItem.setControls(pageForm1);
	    loadsection.addSection(listItem);
	    
	    unloadsection = new SectionStack();
	    unloadsection.setWidth("100%");
	    unloadsection.setHeight("100%");
		
		createunloadFields(unloadtable);
		Tab tab2 = new Tab(Util.TI18N.ARRIVE_OR());//页签名称1：应到未到
		tab2.setPane(unloadsection);
		tabset.addTab(tab2);
		
		final SectionStackSection unloadlistItem = new SectionStackSection();//
		unloadlistItem.setItems(unloadtable);
		unloadlistItem.setExpanded(true);
		pageForm2 = new SGPage(unloadtable, true).initPageBtn();
		unloadlistItem.setControls(pageForm2);
		unloadsection.addSection(unloadlistItem);
	    
	    podsection = new SectionStack();
		podsection.setWidth("100%");
		podsection.setHeight("100%");
		
		createpodFields(podtable);
		Tab tab3 = new Tab(Util.TI18N.BACK_OR());//页签名称1：应回未回
		tab3.setPane(podsection);
		tabset.addTab(tab3);
		
		final SectionStackSection podlistItem = new SectionStackSection();//
		podlistItem.setItems(podtable);
		podlistItem.setExpanded(true);
		pageForm3 = new SGPage(podtable, true).initPageBtn();
		podlistItem.setControls(pageForm3);
	    podsection.addSection(podlistItem);
		
	    
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		
		//创建按钮布局
//		createBtnWidget(toolStrip);
		
//		main.addMember(toolStrip);
		main.addMember(tabset);
		
		
		tabset.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabChanged(event.getTabNum());
			}
		});
		
		findValues.addCriteria("OP_FLAG","M");
		
		tabChanged(0);
		
		initVerify(); 
		return main;
		
	}
	private void tabChanged(int num){
		
		if(num != tabNum){
			if(num == 0){
				loadtable.filterData(findValues,new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
							pageForm1.getField("CUR_PAGE").setValue("1");
							LoginCache.setPageResult(loadtable, pageForm1.getField("TOTAL_COUNT"), pageForm1.getField("SUM_PAGE"));
						}
						
					}
					
				);
			}else if(num == 1){
				unloadtable.filterData(findValues,new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
							pageForm2.getField("CUR_PAGE").setValue("1");
							LoginCache.setPageResult(unloadtable, pageForm2.getField("TOTAL_COUNT"), pageForm2.getField("SUM_PAGE"));
						}
						
					}
					
				);
			}else if(num ==2){
				podtable.filterData(findValues,new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
							pageForm3.getField("CUR_PAGE").setValue("1");
							LoginCache.setPageResult(podtable, pageForm3.getField("TOTAL_COUNT"), pageForm3.getField("SUM_PAGE"));
						}
						
					}
					
				);
			}
			tabNum = num;
		}
	}
	private void createloadFields(SGTable loadtable) {
		
		//CUSTOMER_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_NAME()));
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);//
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),50);//
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),50);//
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SERVICE_NAME",Util.TI18N.TRANS_SRVC_ID(),100);//
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);//运作机构
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),140);//
		ListGridField START_ID_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),140);//
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),140);//
		ListGridField END_ID_NAME = new ListGridField("END_NAME",Util.TI18N.UNLOAD_NAME(),140);//
		ListGridField ODR_TIME = new ListGridField("ODR_TIME",Util.TI18N.ODR_TIME_WARN(),120);//接单时间
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME",Util.TI18N.PRE_LOAD_TIME_WARN(),120);//要求发运时间
		ListGridField LOAD_DELAYS = new ListGridField("LOAD_DELAYS",ColorUtil.getRedTitle("延迟时间(小时)"),120);//要求发运时间
		
		
		loadtable.setFields(SHPM_NO,CUSTOM_ODR_NO,STATUS_NAME,CUSTOMER_NAME,SUPLR_NAME,TRANS_SRVC_ID_NAME,
				EXEC_ORG_ID_NAME,START_AREA_ID_NAME,START_ID_NAME,END_AREA_ID_NAME
				,END_ID_NAME,ODR_TIME,PRE_LOAD_TIME,LOAD_DELAYS);
		
	}
	private void createunloadFields(SGTable unloadtable) {
		
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);//
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),50);//
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),50);//
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SERVICE_NAME",Util.TI18N.TRANS_SRVC_ID(),100);//
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);//运作机构
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),140);//
		ListGridField START_ID_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),140);//
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),140);//
		ListGridField END_ID_NAME = new ListGridField("END_NAME",Util.TI18N.UNLOAD_NAME(),140);//
		ListGridField ODR_TIME = new ListGridField("ODR_TIME",Util.TI18N.ODR_TIME_WARN(),120);//接单时间
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_UNLOAD_TIME",Util.TI18N.PRE_UNLOAD_TIME_WARN(),120);//要求发运时间
		ListGridField UNLOAD_DELAYS = new ListGridField("UNLOAD_DELAYS",ColorUtil.getRedTitle("延迟时间(小时)"),120);//要求发运时间
		
		
		unloadtable.setFields(SHPM_NO,CUSTOM_ODR_NO,STATUS_NAME,CUSTOMER_NAME,SUPLR_NAME,TRANS_SRVC_ID_NAME,
				EXEC_ORG_ID_NAME,START_AREA_ID_NAME,START_ID_NAME,END_AREA_ID_NAME
				,END_ID_NAME,ODR_TIME,PRE_LOAD_TIME,UNLOAD_DELAYS);
		
		
		
	}
	private void createpodFields(SGTable podtable) {
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);//
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),50);//
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),50);//
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SERVICE_NAME",Util.TI18N.TRANS_SRVC_ID(),100);//
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);//运作机构
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),140);//
		ListGridField START_ID_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),140);//
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),140);//
		ListGridField END_ID_NAME = new ListGridField("END_NAME",Util.TI18N.UNLOAD_NAME(),140);//
		ListGridField ODR_TIME = new ListGridField("ODR_TIME",Util.TI18N.ODR_TIME_WARN(),120);//接单时间
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_POD_TIME",Util.TI18N.PRE_POD_TIME_WARN(),120);//要求发运时间
		ListGridField POD_DELAYS = new ListGridField("POD_DELAYS",ColorUtil.getRedTitle("延迟时间(小时)"),120);//要求发运时间
		
		podtable.setFields(SHPM_NO,CUSTOM_ODR_NO,STATUS_NAME,CUSTOMER_NAME,SUPLR_NAME,TRANS_SRVC_ID_NAME,
				EXEC_ORG_ID_NAME,START_AREA_ID_NAME,START_ID_NAME,END_AREA_ID_NAME
				,END_ID_NAME,ODR_TIME,PRE_LOAD_TIME,POD_DELAYS);
		
		
		
	}
	
	
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN, BasPrivRef.ROUTE);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			}
		});
		
		toolStrip.setMembers(searchButton);
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		//作业单编号   客户单号  状态（下拉） 运输服务（下拉）
		//收货区域（动态下拉） 收货方  收货方地址  执行机构
		                             
		form.setDataSource(loadDS);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGText SHPM_NO = new SGText("SHPM_NO", Util.TI18N.SHPM_NO(),true);	
		SGText CUSTOM_ODR_NO =new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		SGCombo TRANS_SRVC_ID_NAME =new SGCombo("TRANS_SRVC_ID_NAME", Util.TI18N.TRANS_SRVC_ID());
		SGCombo STATUS_NAME =new SGCombo("STATUS_NAME", Util.TI18N.STATUS());
		
		//SGCombo END_AREA_ID_NAME =new SGCombo("END_AREA_ID_NAME", Util.TI18N.END_AREA_ID_NAME());
        ComboBoxItem END_AREA_ID_NAME = new ComboBoxItem("END_AREA_ID_NAME",Util.TI18N.END_AREA_ID_NAME());
        END_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
        END_AREA_ID_NAME.setStartRow(true);
        TextItem END_AREA_ID = new TextItem("END_AREA_ID");
        END_AREA_ID.setVisible(false);
        Util.initArea(END_AREA_ID_NAME, END_AREA_ID);
		SGText END_ID_NAME = new SGText("END_ID_NAME", Util.TI18N.UNLOAD_NAME());
		SGText END_ADDRESS = new SGText("END_ADDRESS", Util.TI18N.UNLOAD_ADDRESS());
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		
		form.setItems(SHPM_NO,CUSTOM_ODR_NO,STATUS_NAME,TRANS_SRVC_ID_NAME,END_AREA_ID_NAME,END_ID_NAME,END_ADDRESS,EXEC_ORG_ID_NAME);
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
		// --yuanlei 2011-2-16
		/*if (searchWin != null) {
			searchWin.destroy();
		}*/
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TimeWarnManagerView view = new TimeWarnManagerView();
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