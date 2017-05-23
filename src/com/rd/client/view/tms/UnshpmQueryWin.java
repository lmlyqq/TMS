package com.rd.client.view.tms;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.smartgwt.client.widgets.Canvas;
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

/**
 * 待调作业单查询窗口
 * @author yuanlei
 *
 */
public class UnshpmQueryWin extends Window{

	private int width = 620;
	private int height = 300;
	private String title = "查询条件";
	public ButtonItem searchItem;
	public Window window = null;
	public DynamicForm form;
	public FilterBuilder filterBuilder;
	public ListGrid table;
	public DynamicForm mainSearch;
	//public DataSource ds ;
	private VLayout lay1;
	public SectionStackSection section;
	public ToolStrip toolStrip;
	private Criteria criteria;
	
	public UnshpmQueryWin(int p_width, int p_height,DataSource ds,DynamicForm form, SectionStackSection p_section) {
		this.width = p_width;
		this.height = p_height;
		this.form = form;
		//this.ds = ds;
		this.section = p_section;
	}

	public void createBtnWidget(ToolStrip strip) {
		
	}
	
	/**
	 * 刷新事件
	 * @author yuanlei
	 */
	@SuppressWarnings("unchecked")
	public void doRefresh() {
		JavaScriptObject jsobject = section.getAttributeAsJavaScriptObject("controls");
		Canvas[] canvas = null;
		if(jsobject != null) {
			canvas = Canvas.convertToCanvasArray(jsobject);
		}
		else {
			canvas = new Canvas[1];
		}
		final DynamicForm sumForm = (DynamicForm)canvas[1];
		final DynamicForm pageForm = (DynamicForm)canvas[2];
		canvas = section.getItems();
		if(canvas[0].getClass().equals(TreeTable.class)) {
			table = (TreeTable)canvas[0];
		}
		else {
			table = (SGTable)canvas[0];
		}
		table.invalidateCache();
		criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		if(filterBuilder != null && filterBuilder.isVisible()) {
			criteria.addCriteria(filterBuilder.getCriteria()); 
		}
		else {
			DynamicForm form1 = (DynamicForm)lay1.getMember(0);
			criteria.addCriteria(form1.getValuesAsCriteria());
		}
		criteria.addCriteria("EMPTY_FLAG","Y");
		criteria.addCriteria("STATUS","20");
		criteria.addCriteria(form.getValuesAsCriteria());
		//final LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
		table.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
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
					//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));					
				}
				LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) criteria.getValues();
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
				Util.db_async.getSHMPNOSum(map, new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						sumForm.setValue("TOT_VOL", ObjUtil.ifObjNull(result.get("TOT_VOL"), "").toString());
						sumForm.setValue("TOT_GROSS_W", ObjUtil.ifObjNull(result.get("TOT_GROSS_W"), "").toString());
						sumForm.setValue("TOT_QNTY", ObjUtil.ifObjNull(result.get("TOT_QNTY"), "").toString());
						
						//table.selectRecord(0);
					}
				});
				window.hide();
			}
			
		});
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
				String[] names = {"LOAD_AREA_NAME", "UNLOAD_AREA_NAME", "LOAD_REGION_NAME", "UNLOAD_REGION_NAME"};
				String[] ids = {"LOAD_AREA_ID", "UNLOAD_AREA_ID", "LOAD_REGION", "UNLOAD_REGION"};
				for (int i = 0; i < names.length; i++) {
					if(!(form.getItem(names[i]) == null || 
							form.getItem(ids[i]) == null || 
							ObjUtil.isNotNull(form.getItem(names[i]).getValue()))){
						form.getItem(ids[i]).setValue("");
					}
				}
//				form.getField("OP_FLAG").setValue("M");
				window.hide();//wangjun 2010-7-14
				doRefresh();
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

	public Window getViewPanel() {
		form.setHeight(height/2);
		form.setWidth(width-30);
		form.setNumCols(8);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		
		createForm(form);
		//filterBuilder = new FilterBuilder();
		//filterBuilder.setDataSource(ds);	
		
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
	    //Tab Tab2 = new Tab("自定义查询"); 
	    
	    //Tab2.setPane(filterBuilder);
	    leftTabSet.addTab(Tab1);   
	    //leftTabSet.addTab(Tab2);
	    
		window = new Window();
		window.setTitle(title);
		window.setLeft("30%");
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
