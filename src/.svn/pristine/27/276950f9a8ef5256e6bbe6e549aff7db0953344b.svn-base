package com.rd.client.view.warning;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.HaulingCapacityManagerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
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
 * 运输管理--车辆保养提醒
 * @author wangjun
 *
 */
@ClassForNameAble
public class VehRepairWarnManagerView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource unloadDS;
	private DataSource podDS;
	private SGTable loadtable;
	private SGTable unloadtable;
	private SGTable podtable;
	//private Window searchWin;  --yuanlei 2011-2-16
	//private SGPanel searchForm;  yuanlei 2011-2-15
	private SectionStack loadsection;
	private DynamicForm pageForm1;
	private DynamicForm pageForm2;
	private DynamicForm pageForm3;
	private int tabNum = 4;
	/**
	 * 	应发未发  PRE_lOAD_EXCEL
   	 *	应到未到  PRE_UNLOAD_EXCEL
  	 *	应回未回 PRE_POD_EXCEL
	 */
	
	/*public VehRepairWarnManagerView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		loadDS = HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");
		unloadDS = HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");;
		podDS = HaulingCapacityManagerDS.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");;
		
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
		Tab tab1 = new Tab("车辆到期保养提醒");//页签名称1：应发未发
		tab1.setPane(loadsection);
		tabset.addTab(tab1);
		
		final SectionStackSection listItem = new SectionStackSection();//
	    listItem.setItems(loadtable);
	    listItem.setExpanded(true);
	    pageForm1 = new SGPage(loadtable, true).initPageBtn();
	    listItem.setControls(pageForm1);
	    loadsection.addSection(listItem);
	    
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
		
		loadtable.setShowRowNumbers(true);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N.PLATE_NO(), 75);
		ListGridField VEHICLE_NO = new ListGridField("VEHICLE_NO", Util.TI18N.VEHICLE_NO(), 75);
		ListGridField VEHICLE_STAT_NAME = new ListGridField("VEHICLE_STAT_NAME",Util.TI18N.VEHICLE_STAT(), 75);
		ListGridField VECHILE_TYP_ID = new ListGridField("VEHICLE_TYP_ID_NAME",Util.TI18N.VECHILE_TYP_ID(), 75);
		ListGridField VEHICLE_ATTR = new ListGridField("VEHICLE_ATTR_NAME",Util.TI18N.VEHICLE_ATTR(), 75);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID_NAME",Util.TI18N.SUPLR_ID(), 75);
		ListGridField DRIVER1 = new ListGridField("DRIVER1_NAME", Util.TI18N.DRIVER1(), 65);
		ListGridField MOBILE = new ListGridField("MOBILE1", Util.TI18N.MOBILE(),70);
		ListGridField LOCATION = new ListGridField("LOCATION", Util.TI18N.LOCATION(), 70);
		ListGridField MAX_WEIGHT = new ListGridField("MAX_WEIGHT", Util.TI18N.MAX_WEIGHT(), 70);
		MAX_WEIGHT.setShowHover(true);
		ListGridField MAX_VOLUME = new ListGridField("MAX_VOLUME", Util.TI18N.MAX_VOLUME(), 75);
		ListGridField ORG_ID = new ListGridField("ORG_ID_NAME", Util.TI18N.ORG_ID(),75);
		ListGridField TRAILER_NO = new ListGridField("TRAILER_NO", Util.TI18N.TRAILER_NO(), 75);
		ListGridField TRAILER_FLAG = new ListGridField("TRAILER_FLAG", Util.TI18N.TRAILER_NO(), 75);
		ListGridField VEH_LOCK_REASON = new ListGridField("VEH_LOCK_REASON",Util.TI18N.VEH_LOCK_REASON());
		ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON());		
		
		loadtable.setFields(PLATE_NO, VEHICLE_NO, VECHILE_TYP_ID, SUPLR_ID,VEHICLE_STAT_NAME, 
				VEHICLE_ATTR, DRIVER1, MOBILE, LOCATION, MAX_WEIGHT,
				MAX_VOLUME, ORG_ID, TRAILER_NO,TRAILER_FLAG,VEH_LOCK_REASON,REASON);
		
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
		VehRepairWarnManagerView view = new VehRepairWarnManagerView();
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
