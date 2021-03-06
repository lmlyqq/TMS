package com.rd.client.view.system;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.system.PrintGrantDS;
import com.rd.client.ds.tms.PrintShpmDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 打印授权，
 * @author wangjun
 *
 */
@ClassForNameAble
public class PrintGrantView extends SGForm implements PanelFactory {

	private DataSource ds;
	private DataSource ds2;
	private SGTable table;
	private SGTable table2;
	private Window searchWin;
	private Window searchWin2;
	private SGPanel searchForm;
	private SGPanel searchForm2;
	private SectionStack section;
	private SectionStack section2;
	
	/*public PrintGrantView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		// TODO Auto-generated method stub
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		ds = PrintShpmDS.getInstance("V_PRINT_SHIPMENT", "V_SHIPMENT_HEADER");
		ds2 = PrintGrantDS.getInstance("V_PRINT_LOGS", "PRINT_GRANT");
		
		TabSet leftTabSet = new TabSet();
		leftTabSet.setWidth("100%");
		leftTabSet.setHeight("100%");
		leftTabSet.setMargin(0);

		Tab tab = new Tab("授权信息");
		tab.setPane(PrintGrantInfo());
		leftTabSet.addTab(tab);
		
		Tab tab2 = new Tab("日志信息");
		tab2.setPane(PrintLogInfo());
		leftTabSet.addTab(tab2);
		 
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		main.addMember(leftTabSet);
		return main;

	}
	private Canvas PrintGrantInfo() {

		 
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		
		SGPanel panel = new SGPanel();
		createForm(panel);
		panel.setHeight("4%");
		
		
		table = new SGTable(ds, "100%", "70%");
//		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
        createListFields(table);
        table.setShowFilterEditor(false);
        table.deselectAllRecords();
        
		section = createSection(table, null, true, true);
		initVerify(); 
		
		
		main.addMember(panel);
		main.addMember(section);
		return main;
		
	}
	
	public Canvas PrintLogInfo(){
		
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		table2 = new SGTable(ds2, "100%", "70%");
        createListFields2(table2);
        table2.setShowFilterEditor(false);
		
        ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
        createBtnWidget(toolStrip);
		section2 = createSection(table2, null, true, true);
		
		
		main.addMember(toolStrip);
		main.addMember(section2);
		return main;
		
	}
	
	
	public void createBtnWidget(ToolStrip toolStrip) {
		// TODO Auto-generated method stub
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("30");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			   if(searchWin2==null){
				   searchForm2=new SGPanel();
					searchWin2 = new SearchWin(ds2, createSerchForm2(searchForm2),
							section2.getSection(0)).getViewPanel();
				}else{
					searchWin2.show();
				}
				
			}
		});
		
	        
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton);
		
	}

	private void createListFields(SGTable table) {
		
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),140);
		
		ListGridField LOAD_AREA_NAME = new ListGridField("LOAD_AREA_NAME",Util.TI18N.LOAD_AREA_NAME(),100);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),120);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),170);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),190);
//		ListGridField PRINT_FLAG = new ListGridField("PRINT_FLAG","是否已打印",80);
		
		table.setFields(LOAD_NO,SHPM_NO,LOAD_AREA_NAME,LOAD_NAME,UNLOAD_NAME,UNLOAD_ADDRESS);
	
		
	}
	private void createListFields2(SGTable table) {
		
//		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);
		ListGridField SYSTEM_OPERATION = new ListGridField("SYSTEM_OPERATION","系统操作",120);
		ListGridField LOAD_PRINT_COUNT = new ListGridField("PRINT_COUNT",Util.TI18N.LOAD_PRINT_COUNT(),120);
		ListGridField PRINT_TIME = new ListGridField("ADDTIME","操作时间",120);
		ListGridField PRINTER = new ListGridField("ADDWHO","操作人员",70);
		ListGridField GRANT_REASON = new ListGridField("NOTES",Util.TI18N.NOTES(),270);
		table.setFields(SHPM_NO,SYSTEM_OPERATION,LOAD_PRINT_COUNT,PRINT_TIME,PRINTER,GRANT_REASON);
	
		
	}

	private DynamicForm createSerchForm2(SGPanel form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds2);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//第一行
		TextItem LOAD_NO=new TextItem("LOAD_NO", Util.TI18N.LOAD_NO());
		LOAD_NO.setWidth(130);
		LOAD_NO.setTitleOrientation(TitleOrientation.TOP);
		
		//第2行
		SGText SHPM_NO=new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		SHPM_NO.setWidth(130);
		SHPM_NO.setTitleOrientation(TitleOrientation.TOP);
		
		form.setItems(LOAD_NO,SHPM_NO);
		
		return form;
	}
	
	
	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//第一行
		SGText LOAD_NO=new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		LOAD_NO.setWidth(130);
		LOAD_NO.setColSpan(2);
		LOAD_NO.setTitleOrientation(TitleOrientation.TOP);
		
		//第2行
		SGText SHPM_NO=new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		SHPM_NO.setWidth(130);
		SHPM_NO.setTitleOrientation(TitleOrientation.TOP);
		
		form.setItems(LOAD_NO,SHPM_NO);
		return form;
	}
	
	@Override
	public void createForm(DynamicForm form) {
		
//		final SGText LOAD_NO = new SGText("LOAD_NO",Util.TI18N.LOAD_NO());
//		LOAD_NO.setVisible(true);
//		LOAD_NO.setTitleOrientation(TitleOrientation.LEFT);
//		LOAD_NO.setWidth(150);
		
		
		final SGLText GRANT_REASON = new SGLText("GRANT_REASON",Util.TI18N.GRANT_REASON());

//		
		GRANT_REASON.setTitleOrientation(TitleOrientation.LEFT);
		GRANT_REASON.setColSpan(9);
		GRANT_REASON.setHeight(25);
		
		
		ButtonItem searchButton=new ButtonItem("查询");
		searchButton.setIcon(StaticRef.ICON_SEARCH);
		searchButton.setAutoFit(true);
		searchButton.setStartRow(false);
		searchButton.setEndRow(false);  
		searchButton.setColSpan(1);
		
		searchButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				// TODO Auto-generated method stub
				if(searchWin==null){
					  searchForm=new SGPanel();
				      searchWin = new SearchWin(ds, createSerchForm(searchForm),
								section.getSection(0)).getViewPanel();
			    }else{
					  searchWin.show();
				}
			}
		});
		
		
	    //授权按钮
		ButtonItem ReasonButton = new ButtonItem("授权");
		ReasonButton.setIcon(StaticRef.ICON_SAVE);
		ReasonButton.setAutoFit(true);
		ReasonButton.setAlign(Alignment.RIGHT);
		ReasonButton.setStartRow(false);    
		ReasonButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				if(ObjUtil.isNotNull(LOAD_NO.getValue())){
					HashMap<String, Object> listmap = new HashMap<String, Object>();
					HashMap<String,String> shpm_no = new HashMap<String,String>();
					
					ListGridRecord[] records = table.getSelection();
					for(int i = 0 ; i <records.length ; i++){
						shpm_no.put(String.valueOf(i+1), records[i].getAttribute("SHPM_NO"));
					}
					
					String load_no= table.getSelectedRecord().getAttribute("LOAD_NO");
					
					listmap.put("1", ObjUtil.ifObjNull(load_no," "));
					listmap.put("2", "2");
					listmap.put("3", shpm_no);
					listmap.put("4", LoginCache.getLoginUser().getUSER_NAME());
					listmap.put("5", ObjUtil.ifObjNull(GRANT_REASON.getValue()," ").toString());
					
					String json = Util.mapToJson(listmap);
					
					Util.async.execProcedure(json, "SP_PRINT_LOG(?,?,?,?,?,?)",new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if("00".equals(result)){
								MSGUtil.showOperSuccess();
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}
					});
				}
//			}
		});
		
	    form.setItems(GRANT_REASON,searchButton,ReasonButton);
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
		PrintGrantView view = new PrintGrantView();
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
