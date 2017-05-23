package com.rd.client.win;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FilterBuilder;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 查询通用窗口
 * @author fanglm
 *
 */
public class SearchWin extends Window{

	private int width = 594;
	private int height = 310;
	private String title = "查询条件";
	public ButtonItem searchItem;
	public Window window = null;
	private DynamicForm form;
	public FilterBuilder filterBuilder;
	public ListGrid table;
	public DynamicForm mainSearch;
	public DataSource ds ;
	public SectionStackSection section;
	private DynamicForm pageForm;
	private VLayout lay1;
	public ToolStrip toolStrip;
	private ValuesManager vm;
	private TabSet leftTabSet;
	private SectionStack downSectionStack;
	private HLayout hLayout;
	
	
	public SearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}
	
	public SearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section,ValuesManager vm) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.vm = vm;
	}
	
	public SearchWin(int height,DataSource ds,DynamicForm form, SectionStackSection p_section,ValuesManager vm) {
		this.height = height;
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.vm = vm;
	}
	
	public SearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section,SectionStack downSectionStack,HLayout hLayout) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		this.downSectionStack = downSectionStack;
		this.hLayout = hLayout;
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
//				if(form != null && 
//						form.getJsObj() != null && 
//						ObjUtil.isNotNull(JSOHelper.getAttribute(form.getJsObj(), "parentView"))){
//					if("TmsShipmentView".equals(JSOHelper.getAttribute(form.getJsObj(), "parentView")) ||
//							"TmsShipmentCompView".equals(JSOHelper.getAttribute(form.getJsObj(), "parentView"))){
//						String[] names = {"LOAD_AREA_NAME", "UNLOAD_AREA_NAME"};
//						String[] ids = {"LOAD_AREA_ID", "UNLOAD_AREA_ID"};
//						for (int i = 0; i < names.length; i++) {
//							if(!(form.getItem(names[i]) == null || 
//									form.getItem(ids[i]) == null || 
//									ObjUtil.isNotNull(form.getItem(names[i]).getValue()))){
//								form.getItem(ids[i]).setValue("");
//							}
//						}
//					}
//				}
				doSearch();
				if(downSectionStack!=null){
					downSectionStack.setHeight(Page.getHeight()/2-22);
					hLayout.setHeight("44%");
				}
				
//				test();
			}
		});
		
		IButton clearBtn = new IButton(Util.BI18N.CLEAR());
		clearBtn.setIcon(StaticRef.ICON_CANCEL);
		clearBtn.setWidth(60);
		clearBtn.setAutoFit(true);
		clearBtn.setAlign(Alignment.RIGHT);
		
		clearBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
				form.clearValues();
				filterBuilder.clearCriteria();
			}
		});
        
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(20);
		toolStrip.setAlign(Alignment.RIGHT);
        toolStrip.setMembers(searchBtn,clearBtn);
	}
	
	public void test(){
		JavaScriptObject jsobject = section.getAttributeAsJavaScriptObject("controls");
		Canvas[] canvas = null;
		if(jsobject != null) {
			canvas = Canvas.convertToCanvasArray(jsobject);
		}
		else {
			canvas = new Canvas[1];
		}
		for(int i = 0; i < canvas.length; i++) {
			if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
				pageForm = (DynamicForm)canvas[i];
				break;
			}
		}
		canvas = section.getItems();
		if(canvas[0].getClass().equals(TreeTable.class)) {
			table = (TreeTable)canvas[0];
		}
		else {
			table = (SGTable)canvas[0];
		}
		table.discardAllEdits();
		table.invalidateCache();
		
		window.hide();
		table.filterData(filterBuilder.getCriteria());
	}

	public void doSearch(){
		//第一件事情是隐藏查询窗口
		window.hide();
		
		JavaScriptObject jsobject = section.getAttributeAsJavaScriptObject("controls");
		Canvas[] canvas = null;
		if(jsobject != null) {
			canvas = Canvas.convertToCanvasArray(jsobject);
		}
		else {
			canvas = new Canvas[1];
		}
		for(int i = 0; i < canvas.length; i++) {
			if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
				pageForm = (DynamicForm)canvas[i];
				break;
			}
		}
		canvas = section.getItems();
		if(canvas[0].getClass().equals(TreeTable.class)) {
			table = (TreeTable)canvas[0];
		}
		else {
			table = (SGTable)canvas[0];
		}
		table.discardAllEdits();
		table.invalidateCache();
		final Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		if(leftTabSet.getSelectedTabNumber() == 1) {
			criteria.addCriteria(filterBuilder.getCriteria()); 
		}
		else {
			DynamicForm form1 = (DynamicForm)lay1.getMember(0);
			criteria.addCriteria(form1.getValuesAsCriteria());
		}

		table.setFilterEditorCriteria(criteria);
		table.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					String key = Cookies.getCookie("SQLFIELD1");
					String value = Cookies.getCookie("SQLFIELD2");
					String alias = Cookies.getCookie("SQLALIAS");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						table.setProperty("SQLFIELD1", key);
						table.setProperty("SQLFIELD2", value);
						table.setProperty("SQLALIAS", alias);
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				table.setSelectOnEdit(true);
				if(table.getRecords().length > 0){
					table.selectRecord(table.getRecord(0));
					
				}else if(vm != null){
					vm.clearValues();
				}
				
			}
			
		});
	}
	public SectionStackSection getSection() {
		return section;
	}

	public void setSection(SectionStackSection section) {
		this.section = section;
	}

	public Window getViewPanel() {
		form.setHeight(height/2);
		form.setWidth(width-30);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		//form.setHeight100();
		
		createForm(form);
		
		//wangjun 2010-08-08 查询可以使用【Enter】快捷键
		/*form.addItemKeyPressHandler(new ItemKeyPressHandler() {
			
			@Override
			public void onItemKeyPress(ItemKeyPressEvent event) {
				// TODO Auto-generated method stub
				if(ObjUtil.ifObjNull(event.getKeyName()," ").equals("Enter")){
					doSearch();
				}
			}
		});*/
		
		filterBuilder = new FilterBuilder();
		filterBuilder.setDataSource(ds);		
		
		lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.setMembers(form);
		
		leftTabSet = new TabSet();   
        leftTabSet.setTabBarPosition(Side.TOP);
        leftTabSet.setTabBarAlign(Side.LEFT);
        leftTabSet.setWidth100();
		Tab Tab1 = new Tab("查询");
	    Tab1.setPane(lay1);	 
	    //Tab Tab2 = new Tab("自定义查询"); 
	    
	    //Tab2.setPane(filterBuilder);
	    leftTabSet.addTab(Tab1);   
	    //leftTabSet.addTab(Tab2);
		
		window = new Window();
		window.setTitle(title);
//		window.setTitle(ColorUtil.getWhiteTitle("查询条件"));
		window.setLeft("27%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);
		window.addItem(leftTabSet);
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
