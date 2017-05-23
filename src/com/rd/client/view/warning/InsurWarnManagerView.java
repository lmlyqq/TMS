package com.rd.client.view.warning;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.Cookies;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.HaulingCapacityManagerDS;
import com.rd.client.ds.base.HaulingCapacityManagerDsDB;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
 * 运输管理--保险到期提醒
 * @author wangjun
 *
 */
@ClassForNameAble
public class InsurWarnManagerView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource podDS;
	private ListGrid loadtable;
	private ListGrid podtable;
	//private Window searchWin;  --yuanlei 2011-2-16
	//private SGPanel searchForm;  yuanlei 2011-2-15
	private SectionStack loadsection;

	private SectionStack podsection;
	private DynamicForm pageForm1;
	private DynamicForm pageForm3;
	private int tabNum = 4;
	public SGTable table;
	
	private com.smartgwt.client.widgets.Window searchWin;
	private com.smartgwt.client.widgets.Window searchWins;
	private SGPanel searchForm;
	private SGPanel searchForms;
	/**
	 * 	应发未发  PRE_lOAD_EXCEL
   	 *	应到未到  PRE_UNLOAD_EXCEL
  	 *	应回未回 PRE_POD_EXCEL
	 */
	
	/*public InsurWarnManagerView(String id) {
		super(id);
	}*/
	@Override
	public Canvas getViewPanel() {
		
		podDS = HaulingCapacityManagerDS.getInstall("V_VERIFY_REMIND");
		
		loadDS = HaulingCapacityManagerDsDB.getInstall("V_INS_REMIND");

		
		// 主布局
		HStack stack = new HStack();// 设置详细信息布局
		stack.setWidth100();
		stack.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip toolStrips = new ToolStrip();//按钮布局
		toolStrips.setAlign(Alignment.RIGHT);
		
		
		podtable= new ListGrid(){
			@Override  
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {  
                if (getFieldName(colNum).equals("REMAIN_DAYS")||getFieldName(colNum).equals("NEXT_DATE")) {  
                    if (Double.parseDouble(record.getAttribute("REMAIN_DAYS"))<=30) {  
                        return "font-weight:bold; color:#d64949;";  
                    }  
                   
                    else{  
                        return super.getCellCSSText(record, rowNum, colNum);  
                    }  
                } else {  
                    return super.getCellCSSText(record, rowNum, colNum);  
                }  
            }  
		};
		podtable.setDataSource(podDS);
		podtable.setWidth("100%");
		podtable.setHeight("100%");
		
		loadtable = new ListGrid() {  
            @Override  
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {  
                if (getFieldName(colNum).equals("REMAIN_DAYS")) {  
                    if (Double.parseDouble(record.getAttribute("REMAIN_DAYS"))<=30) {  
                        return "font-weight:bold; color:#d64949;";  
                    }  
                   
                    else{  
                        return super.getCellCSSText(record, rowNum, colNum);  
                    }  
                } else {  
                    return super.getCellCSSText(record, rowNum, colNum);  
                }  
            }  
        };
        loadtable.setDataSource(loadDS);
        loadtable.setWidth("100%");
        loadtable.setHeight("100%");
		podtable.setShowFilterEditor(false);
		loadtable.setShowFilterEditor(false);
		
		TabSet tabset=new TabSet();
		tabset.setWidth("100%");
		tabset.setHeight("100%");
		tabset.setMargin(0);
		
		//创建Section
		loadsection = new SectionStack();
		loadsection.setWidth("100%");
		loadsection.setHeight("100%");
		
		VLayout lay = new VLayout();
		
		createloadFields(loadtable);
		Tab tab1 = new Tab("保险逾期提醒");//页签名称1：应发未发
		tab1.setPane(lay);
		tabset.addTab(tab1);
		createBtnWidget(toolStrip);
		
		lay.addMember(toolStrip);
		lay.addMember(loadsection);
		
		final SectionStackSection listItem = new SectionStackSection();//
	    listItem.setItems(loadtable);
	    listItem.setExpanded(true);
	    pageForm1 = new SGPage(loadtable, true).initPageBtn();
	    listItem.setControls(pageForm1);
	    loadsection.addSection(listItem);
	    
		//创建按钮布局
		
	    
	    /** 商业保险逾期提醒 已经删除 **/
	    
	    podsection = new SectionStack();
		podsection.setWidth("100%");
		podsection.setHeight("100%");
		
		VLayout lays = new VLayout();
		
		createpodFields(podtable);
		Tab tab3 = new Tab("年审逾期提醒");//页签名称1：应回未回
		tab3.setPane(lays);
		tabset.addTab(tab3);
	
		createBtnWidgets(toolStrips);
		
		lays.addMember(toolStrips);
		lays.addMember(podsection);
		
		final SectionStackSection podlistItem = new SectionStackSection();//
		podlistItem.setItems(podtable);
		podlistItem.setExpanded(true);
		pageForm3 = new SGPage(podtable, true).initPageBtn();
		podlistItem.setControls(pageForm3);
	    podsection.addSection(podlistItem);
		
	  	    
	    privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		main.addMember(tabset);
        
		tabset.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabChanged(event.getTabNum());
			}
		});
		
		initVerify(); 
		return main;
		
	}
	
	private void tabChanged(int num){
		
		if(num != tabNum){
			if(num == 0){
				loadtable.filterData(findValues,new DSCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
							if(pageForm1 != null) {
								pageForm1.getField("CUR_PAGE").setValue("1");
								pageForm1.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm1.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								
							}
							LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) findValues.getValues();
							if(map.get("criteria") != null) {
								map.remove("criteria");
							}
							if(map.get("_constructor") != null) {
								map.remove("_constructor");
							}
							if(map.get("C_ORG_FLAG") != null) {
								Object obj = map.get("C_ORG_FLAG");
								Boolean c_org_flag = (Boolean)obj;
								map.put("C_ORG_FLAG",c_org_flag.toString());
							}

						}
						
					}
					
				);
			}else{
				podtable.filterData(findValues,new DSCallback() {

					@SuppressWarnings("unchecked")
					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
							if(pageForm3 != null) {
								pageForm3.getField("CUR_PAGE").setValue("1");
								pageForm3.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
								pageForm3.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
								
									
							}
							LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) findValues.getValues();
							if(map.get("criteria") != null) {
								map.remove("criteria");
							}
							if(map.get("_constructor") != null) {
								map.remove("_constructor");
							}
							if(map.get("C_ORG_FLAG") != null) {
								Object obj = map.get("C_ORG_FLAG");
								Boolean c_org_flag = (Boolean)obj;
								map.put("C_ORG_FLAG",c_org_flag.toString());
							}
						}
						
					} 	
					
				);
			}
			tabNum = num;
		}
	}
	private void createloadFields(ListGrid loadtable) {
		
        
//        countryGrid.setDataSource(loadDS);
//        countryGrid.setWidth("96%");
//        countryGrid.setHeight(60);
//       // lstTable.setCanEdit(false);
//        countryGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
//        countryGrid.setAutoFitData(Autofit.VERTICAL);
       // lstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
       
//        Criteria findValues = new Criteria();
//        findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
//        findValues.addCriteria("ADJ_NO", record.getAttributeAsString("ADJ_NO"));
        
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
		ListGridField INS_TO = new ListGridField("INS_TO",Util.TI18N.INS_EXP_DT(),110);
		ListGridField REMAIN_DAYS = new ListGridField("REMAIN_DAYS","剩余天数",75);
		loadtable.setFields(PLATE_NO, VEHICLE_NO, VECHILE_TYP_ID, SUPLR_ID,VEHICLE_STAT_NAME, 
				VEHICLE_ATTR, DRIVER1, MOBILE, LOCATION, MAX_WEIGHT,
				MAX_VOLUME,INS_TO,REMAIN_DAYS);
      
	}

	private void createpodFields(ListGrid podtable2) {
		podtable2.setShowRowNumbers(true);
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
		ListGridField  NEXT_DATE = new ListGridField("NEXT_DATE","年审到期时间",120);
		ListGridField REMAIN_DAYS = new ListGridField("REMAIN_DAYS","剩余天数",75);
		
		podtable2.setFields(PLATE_NO, VEHICLE_NO, VECHILE_TYP_ID, SUPLR_ID,VEHICLE_STAT_NAME, 
				VEHICLE_ATTR, DRIVER1, MOBILE, LOCATION, MAX_WEIGHT,
				MAX_VOLUME,NEXT_DATE,REMAIN_DAYS);
	}
	
	
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		
		//查询: 创建按钮 绑定事件
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin(loadDS, createSerchForm(searchForm),
							loadsection.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
		//导出
		 IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.PACK_P0_05);
	     expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
		
        toolStrip.setMembersMargin(2);
        toolStrip.setMembers(searchButton,expButton);
		
	}
	

	
	private void createBtnWidgets(ToolStrip toolStrips) {

		toolStrips.setWidth("100%");
		toolStrips.setHeight("20");
		toolStrips.setPadding(2);
		toolStrips.setSeparatorSize(12);
		
		
		toolStrips.addSeparator();
		
		//查询: 创建按钮 绑定事件
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			   if(searchWins==null){
				   searchForms=new SGPanel();
					searchWins = new SearchWin(podDS, createSerchForms(searchForms),
						    podsection.getSection(0)).getViewPanel();
				}else{
					searchWins.show();
				}
				
			}
		});
		
		//导出
		 IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.PACK_P0_05);
	     expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
		
	     toolStrips.setMembersMargin(2);
	     toolStrips.setMembers(searchButton,expButton);
		
	}
	
	
	
	//查询窗口（二级窗口）保险逾期提醒
	public DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		//1.
		SGText PLATE_NO = new SGText("PLATE_NO", "车牌号");
	
		//2.

		SGCombo VEHICLE_ATTR = new SGCombo("VEHICLE_ATTR", Util.TI18N.VEHICLE_ATTR(), true);
		Util.initCodesComboValue(VEHICLE_ATTR, "VECHILE_ATTR");
		//3.
		SGText WITHIN_FEW_DAYS  = new SGText("WITHIN_FEW_DAYS", "几天以内");
		
		form.setItems(PLATE_NO, VEHICLE_ATTR,WITHIN_FEW_DAYS);
	
		return form;
	}
	
	//查询窗口 （二级窗口）年审逾期提醒
	private DynamicForm createSerchForms(DynamicForm searchForms) {
		
		searchForms.setAutoFetchData(false);
		searchForms.setWidth100();
		searchForms.setCellPadding(2);
		//1.
		SGText PLATE_NO = new SGText("PLATE_NO", "车牌号");
	
		//2.
//		SGCombo VEHICLE_ATTR_NAME = new SGCombo("VECHILE_TYP_ID", Util.TI18N.VEHICLE_ATTR(), true);
//		
//		Util.initComboValue(VEHICLE_ATTR_NAME, "V_VERIFY_REMIND", "PlATE_NO","VEHICLE_ATTR_NAME");
		SGCombo VEHICLE_ATTR = new SGCombo("VEHICLE_ATTR", Util.TI18N.VEHICLE_ATTR(), true);
		Util.initCodesComboValue(VEHICLE_ATTR, "VECHILE_ATTR");
		//3.
		SGText WITHIN_FEW_DAYS  = new SGText("WITHIN_FEW_DAYS", "几天以内");
	
		searchForms.setItems(PLATE_NO, VEHICLE_ATTR,WITHIN_FEW_DAYS);
	
		return searchForms;
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
		InsurWarnManagerView view = new InsurWarnManagerView();
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