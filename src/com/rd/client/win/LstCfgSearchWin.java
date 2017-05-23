package com.rd.client.win;

import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.TreeTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理->列表配置里查询二级窗口（特殊处理）
 * @author yuanlei
 *
 */
public class LstCfgSearchWin extends Window{

	private int width = 620;
	private int height = 300;
	private String title = "查询条件";
	public ButtonItem searchItem;
	public Window window = null;
	private DynamicForm form;
//	public FilterBuilder filterBuilder;
	public ListGrid table;
	public DynamicForm mainSearch;
	public DataSource ds ;
	public SectionStackSection section;
	private DynamicForm pageForm;
	private VLayout lay1;
	private HashMap<String, String> chk_map;
	public ToolStrip toolStrip;
	
	public LstCfgSearchWin(SGTable p_table,ButtonItem p_searchItem, int p_width, int p_height, String p_title) {
		this.width = p_width;
		this.height = p_height;
		this.title = p_title;
		this.searchItem = p_searchItem;
		this.table = p_table;
	}
	
	public LstCfgSearchWin(ButtonItem p_searchItem, int p_width, int p_height,DataSource ds,DynamicForm form) {
		this.width = p_width;
		this.height = p_height;
		this.searchItem = p_searchItem;
		this.form = form;
		this.ds = ds;
	}
	
	public LstCfgSearchWin(ButtonItem p_searchItem,DataSource ds,DynamicForm form) {
		this.searchItem = p_searchItem;
		this.form = form;
		this.ds = ds;
	}
	
	public LstCfgSearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}
	
	public LstCfgSearchWin(int p_width, int p_height,DataSource ds,DynamicForm form, SectionStackSection p_section) {
		this.width = p_width;
		this.height = p_height;
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}
	
	public LstCfgSearchWin(DataSource ds,DynamicForm form, SectionStackSection p_section, HashMap<String, String> p_checkMap) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
		chk_map = p_checkMap;
	}

	public void createBtnWidget(ToolStrip strip) {
		
	}

	public void createForm(DynamicForm searchForm) {
		IButton searchBtn = new IButton(Util.BI18N.SEARCH());
		searchBtn.setIcon(StaticRef.ICON_SEARCH);
		searchBtn.setWidth(60);
//		saveBtn.setEndRow(false);
		searchBtn.setAutoFit(true);
		searchBtn.setAlign(Alignment.RIGHT);
		
		searchBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(chk_map != null) {
					String key = "", value = "";
					Object[] iter = chk_map.keySet().toArray();
					for(int i = 0; i < iter.length; i++) {
						key = (String)iter[i];
						value = chk_map.get(key);
						if(!ObjUtil.isNotNull(form.getField(key).getValue())) {
							SC.warn("[" + value + "]" + Util.MI18N.CHK_NOTNULL() + "\r\n");
							return;
						}
					}
				}
				JavaScriptObject jsobject = section.getAttributeAsJavaScriptObject("controls");
				Canvas[] canvas = null;
				if(jsobject != null) {
					canvas = Canvas.convertToCanvasArray(jsobject);
				}
				else {
					canvas = new Canvas[1];
				}
				for(int i = 0; i < canvas.length; i++) {
					if(canvas[i] != null && canvas[i].getClass().equals(SGPanel.class)) {
						pageForm = (SGPanel)canvas[i];
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
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
//				if(filterBuilder.isVisible()) {
//					criteria.addCriteria(filterBuilder.getCriteria()); 
//				}
//				else {
					DynamicForm form1 = (DynamicForm)lay1.getMember(0);
					criteria.addCriteria(form1.getValuesAsCriteria());
//				}
				table.setFilterEditorCriteria(criteria);
				table.fetchData(criteria, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						pageForm.setValue("FUNC_MODEL", form.getField("FUNC_MODEL").getDisplayValue());
						pageForm.setValue("VIEW_NAME", form.getField("VIEW_NAME").getDisplayValue());
						table.setProperty("FUNC_MODEL", form.getField("FUNC_MODEL").getValue().toString());
						table.setProperty("VIEW_NAME", form.getField("VIEW_NAME").getValue().toString());
						table.setProperty("USER_ID", form.getField("USER_ID").getValue().toString());
						table.setSelectOnEdit(true);
						window.hide();
					}
				});
			}
			
		});
		
		IButton clearBtn = new IButton(Util.BI18N.CLEAR());
		clearBtn.setIcon(StaticRef.ICON_CANCEL);
		clearBtn.setWidth(60);
//		clearBtn.setStartRow(false);
		clearBtn.setAutoFit(true);
		clearBtn.setAlign(Alignment.RIGHT);
		
		clearBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				
				form.clearValues();
//				filterBuilder.clearCriteria();
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

	public Window getViewPanel() {
		form.setHeight(height/2);
		form.setWidth(width-30);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		
		createForm(form);
		
//		filterBuilder = new FilterBuilder();
//		filterBuilder.setDataSource(ds);		
		
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
//	    Tab Tab2 = new Tab("自定义查询"); 
	    
//	    Tab2.setPane(filterBuilder);
	    leftTabSet.addTab(Tab1);   
//	    leftTabSet.addTab(Tab2);
		
		window = new Window();
		window.setTitle(title);
		window.setLeft("20%");
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