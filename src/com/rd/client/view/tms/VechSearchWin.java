package com.rd.client.view.tms;



import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class VechSearchWin extends Window{

	private int width = 620;
	private int height = 300;
	private String title = "查询条件";
	public ButtonItem searchItem;
	public Window window = null;
	private DynamicForm form;
	public FilterBuilder filterBuilder;
	public ListGrid table;
	public DynamicForm mainSearch;
	public DataSource ds ;
	public SectionStackSection section;
	private VLayout lay1;
//	private boolean crit_flag = true;
	private VechRegistView view;
	public ToolStrip toolStrip;
//	private ArrayList<ListGridRecord> cache_list;
	
	public VechSearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section,VechRegistView view) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.view = view;
	}
	

	public void createBtnWidget(ToolStrip strip) {
		
	}

	public void createForm(DynamicForm searchForm) {
		
		IButton searchBtn = new IButton(Util.BI18N.SEARCH());
		searchBtn.setIcon(StaticRef.ICON_SEARCH);
		searchBtn.setWidth(60);
		searchBtn.setAutoFit(true);
		searchBtn.setAlign(Alignment.RIGHT);
		searchBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//				if(form.getItem("LOAD_NO").getValue() != null){
//					cache_list = new ArrayList<ListGridRecord>();
					view.loadRightTable.invalidateCache();
//					ds = LoadJob_search_DS.getInstance("V_TRANS_LOAD_JOB_4");
//					view.loadRightTable.setDataSource(ds);
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG","M");
					crit.addCriteria(form.getValuesAsCriteria());
					/**
					if(ObjUtil.isNotNull(form.getItem("LOAD_NO").getValue())){
						crit.addCriteria("LOAD_NO", form.getItem("LOAD_NO").getValue().toString());
						
					}
					if(ObjUtil.isNotNull(form.getItem("LOAD_STATUS").getValue())){
						crit.addCriteria("LOAD_STATUS",form.getItem("LOAD_STATUS").getValue().toString());
						
					}
					if(ObjUtil.isNotNull(form.getItem("ARRIVE_WHSE_TIME_FROM").getValue())){
						crit.addCriteria("ARRIVE_WHSE_TIME_FROM",form.getItem("ARRIVE_WHSE_TIME_FROM").getValue().toString());
						
					}
					if(ObjUtil.isNotNull(form.getItem("ARRIVE_WHSE_TIME_END").getValue())){
						crit.addCriteria("ARRIVE_WHSE_TIME_END",form.getItem("ARRIVE_WHSE_TIME_END").getValue().toString());
						
					})**/
					
					crit.addCriteria("USER_ID", LoginCache.getLoginUser().getUSER_ID());
				
					view.loadRightTable.fetchData(crit,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(view.loadLeftTable.getRecords().length > 0) {
								view.loadRightTable.selectRecord(0);
								
							}
						}
					});
					window.hide();
					
//				} else {
//					MSGUtil.sayWarning("请输入所要查询的调度单号！");
//				}
			}
		});
//		ButtonItem searchItem = new ButtonItem(Util.BI18N.SEARCH());
//		searchItem.setIcon(StaticRef.ICON_SEARCH);
//		searchItem.setColSpan(1);
//		searchItem.setStartRow(false);
//		searchItem.setEndRow(false);
//		searchItem.setAutoFit(true);
//		searchItem.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				if(form.getItem("PLATE_NO").getValue() != null){
//					view.loadRightTable.invalidateCache();
//					Criteria crit = new Criteria();
//					crit.addCriteria("OP_FLAG","M");
//					crit.addCriteria("PLATE_NO",form.getItem("PLATE_NO").getValue().toString());
////					crit.addCriteria("A",!crit_flag);
////					crit_flag = !crit_flag;
//					view.loadRightTable.fetchData(crit);
//					window.hide();
//					
//				} else {
//					MSGUtil.sayWarning("请输入所要查询的车牌号！");
//				}
//			   
//			}
//			
//		});
		
		IButton clearBtn = new IButton(Util.BI18N.CLEAR());
		clearBtn.setIcon(StaticRef.ICON_CANCEL);
		clearBtn.setWidth(60);
//		clearBtn.setStartRow(false);
		clearBtn.setAutoFit(true);
		clearBtn.setAlign(Alignment.RIGHT);
		clearBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				form.clearValues();
			}
		});
		
//		ButtonItem clearItem = new ButtonItem(Util.BI18N.CLEAR());
//		clearItem.setIcon(StaticRef.ICON_CANCEL);
//		clearItem.setColSpan(1);
//		clearItem.setAutoFit(true);
//		clearItem.setStartRow(false);
//		clearItem.setEndRow(true);
//		clearItem.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				form.clearValues();
//			}
//			
//		});
		
        
//		mainSearch = new DynamicForm();
//		mainSearch.setCellPadding(5);
//		mainSearch.setNumCols(5);
//		mainSearch.setItems(searchItem,clearItem);
//		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
		
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchBtn,clearBtn);
	}

	public Window getViewPanel() {
		form.setHeight(height/2);
		form.setWidth(width-30);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		
		createForm(form);
		
		
		lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
		
		final TabSet leftTabSet = new TabSet();   
        leftTabSet.setTabBarPosition(Side.TOP);
        leftTabSet.setTabBarAlign(Side.LEFT);
        leftTabSet.setWidth100();
		Tab Tab1 = new Tab("查询");
	    Tab1.setPane(lay1);	 
	    
	    leftTabSet.addTab(Tab1);   
		
		window = new Window();
		window.setTitle(title);
		window.setLeft("20%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.addItem(leftTabSet);
//		window.addItem(mainSearch);
		window.addItem(toolStrip);
		window.setShowCloseButton(true);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}
			
		});
		
		return window;
	}
}
