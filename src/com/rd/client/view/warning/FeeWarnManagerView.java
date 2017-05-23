package com.rd.client.view.warning;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.warning.PreFeeDS;
import com.rd.client.ds.warning.SettFeeDS;
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
 * 运输管理--预警信息--费用预警信息
 * @author wangjun
 *
 */
@ClassForNameAble
public class FeeWarnManagerView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource podDS;
	private SGTable loadtable;
	private SGTable podtable;
	private SectionStack loadsection;
	private SectionStack podsection;
	private DynamicForm pageForm1;
	private DynamicForm pageForm3;
	private int tabNum = 4;
	/**
	 * 	应发未发  PRE_lOAD_EXCEL
   	 *	应到未到  PRE_UNLOAD_EXCEL
  	 *	应回未回 PRE_POD_EXCEL
	 */
	
	/*public FeeWarnManagerView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		loadDS = PreFeeDS.getInstance("WARNING_PRE_FEE", "WARNING_PRE_FEE");
		podDS = SettFeeDS.getInstance("WARNING_SETT_FEE", "WARNING_SETT_FEE");
		
		// 主布局
		HStack stack = new HStack();// 设置详细信息布局
		stack.setWidth100();
		stack.setHeight100();
		
		loadtable= new SGTable(loadDS, "100%", "100%");
		podtable= new SGTable(podDS, "100%", "100%");
		
		loadtable.setShowFilterEditor(false);
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
		Tab tab1 = new Tab("预付款超额");//页签名称1：应发未发
		tab1.setPane(loadsection);
		tabset.addTab(tab1);
		
		final SectionStackSection listItem = new SectionStackSection();//
	    listItem.setItems(loadtable);
	    listItem.setExpanded(true);
	    pageForm1 = new SGPage(loadtable, true).initPageBtn();
	    listItem.setControls(pageForm1);
	    loadsection.addSection(listItem);
	    
	    
	    podsection = new SectionStack();
		podsection.setWidth("100%");
		podsection.setHeight("100%");
		
		createpodFields(podtable);
		Tab tab3 = new Tab("运费超额");//页签名称1：应回未回
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
			}else if(num ==1){
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
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);//
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);//运作机构
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),120);//运作机构
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),120);//运作机构
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),140);//
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),140);//
		ListGridField SETT_FEE = new ListGridField("SETT_FEE",Util.TI18N.SETT_CASH(),100);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE","预付款金额",100);//
		ListGridField OUT_FEE = new ListGridField("OUT_FEE","超出金额",100);//
		ListGridField TRANS_NOTES = new ListGridField("TRANS_NOTES",Util.TI18N.NOTES(),120);//要求发运时间
		
		
		loadtable.setFields(LOAD_NO,SHPM_NO,
				EXEC_ORG_ID_NAME,SUPLR_NAME,PLATE_NO,START_AREA_ID_NAME,END_AREA_ID_NAME
				,SETT_FEE,PRE_FEE,OUT_FEE,TRANS_NOTES);
		
	}
	
	private void createpodFields(SGTable podtable) {
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);//
		ListGridField sett_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);//运作机构
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),120);//运作机构
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),120);//运作机构
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME(),140);//
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME(),140);//
		ListGridField FEE_BAS = new ListGridField("FEE_BAS",Util.TI18N.FEE_BASE(),100);
		ListGridField BAS_VALUE = new ListGridField("BAS_VALUE",Util.TI18N.BAS_VALUE(),100);//
		ListGridField CONT_PRICE = new ListGridField("CONT_PRICE",Util.TI18N.SETT_CONT_PRICE(),100);
		ListGridField CONT_CASH = new ListGridField("CONT_FEE",Util.TI18N.SETT_CONT_CASH(),100);//
		ListGridField PRICE = new ListGridField("PRICE",Util.TI18N.PRICE(),100);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.SETT_CASH(),100);//
		ListGridField OUT_FEE = new ListGridField("OUT_FEE","超出金额",100);//
		ListGridField TRANS_NOTES = new ListGridField("TRANS_NOTES",Util.TI18N.NOTES(),120);//要求发运时间
		
		
		podtable.setFields(LOAD_NO,SHPM_NO,sett_ORG_ID_NAME
			,SUPLR_NAME,PLATE_NO,START_AREA_ID_NAME,END_AREA_ID_NAME
				,FEE_BAS,BAS_VALUE,CONT_PRICE,CONT_CASH,PRICE,DUE_FEE,OUT_FEE,TRANS_NOTES);
		
		
		
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
		FeeWarnManagerView view = new FeeWarnManagerView();
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
