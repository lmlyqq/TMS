package com.rd.client.view.base;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.TransportServiceDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 基础资料->运输服务
 * 
 * @author wangjun
 * @param DS
 * @param table
 * @param searchWin
 * @param searchForm
 * @param searchItem
 * 
 */
@ClassForNameAble
public class BasTransServiceView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public BasTransServiceView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = TransportServiceDS.getInstance("V_BAS_TRANS_SERVICE", "BAS_TRANS_SERVICE");

		table = new SGTable(ds, "100%", "70%");
        createListFields(table);
        table.setShowFilterEditor(false);
        
        table.addRecordClickHandler(new RecordClickHandler() {
			
			public void onRecordClick(RecordClickEvent event) {
				   initSaveBtn();
//				enableOrDisables(del_map, true);
			}
		});
        
        
        
//        table.addSelectionChangedHandler(new SelectionChangedHandler() {
//			public void onSelectionChanged(SelectionEvent event) {
//                
//        		enableOrDisables(del_map, true);
//            }
//        });
		
        
        table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		
        //创建按钮布局
		createBtnWidget(toolStrip);
		section = createSection(table, null, true, true);
		initVerify();  
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		return main;

	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	//布局列表信息按钮
	private void createListFields(SGTable table) {
		
		ListGridField SRVC_NAME = new ListGridField("SRVC_NAME",ColorUtil.getRedTitle(Util.TI18N.SRVC_NAME()),110);
		ListGridField SRVC_ENAME = new ListGridField("SRVC_ENAME",Util.TI18N.SRVC_ENAME(),120);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME",ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()),70);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),52);
		table.addEditorExitHandler(new GetHintAction(table));
		SRVC_NAME.addEditorExitHandler(new GetHintAction(table));
		ListGridField TRANS_TYPE = new ListGridField("TRANS_TYPE",Util.TI18N.TRANS_TYPE(),70);
		Util.initCodesComboValue(TRANS_TYPE,"TRANS_TYP");
//		Util.initComboValue(TRANS_TYPE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'TRANS_TYP'", " SHOW_SEQ ASC");
//		Util.initComboValue(TRANS_TYPE, "V_BAS_TRANS_SERVICE", id, "TRANS_TYPE_NAME", "", "");
		ListGridField FOR_CUSTOMER_FLAG = new ListGridField("FOR_CUSTOMER_FLAG",Util.TI18N.FOR_CUSTOMER_FLAG(),52);
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),52);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),50);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),50);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.UDF1(),80);
//		Util.initListGridDateTime(UDF1);
		ListGridField UDF2 = new ListGridField("UDF2",Util.TI18N.UDF2(),80);
		ListGridField UDF3 = new ListGridField("UDF3",Util.TI18N.UDF3(),80);
		ListGridField UDF4 = new ListGridField("UDF4",Util.TI18N.UDF4(),80);
		table.setFields(SRVC_NAME,SHORT_NAME,SRVC_ENAME,HINT_CODE,TRANS_TYPE,FOR_CUSTOMER_FLAG,SHOW_SEQ,ENABLE_FLAG,DEFAULT_FLAG,UDF1,UDF2,UDF3,UDF4);
	
	}
	
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
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
		
		    //新增按钮
		    IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.SRVC_P0_01);
	        newButton.addClickHandler(new NewAction(table,cache_map,this));
	        
	        //保存按钮
	        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SRVC_P0_02);
	        saveButton.addClickHandler(new SaveAction(table,check_map,this));
	        
	        //删除按钮
	        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.SRVC_P0_03);
	        delButton.addClickHandler(new DeleteAction(table));
	        
	        //取消按钮
	        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SRVC_P0_04);
	        canButton.addClickHandler(new CancelAction(table,this));
	        
	        //导出按钮
	        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.SRVC_P0_05);
	        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
	    
	        toolStrip.setMembersMargin(4);
	        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton);
	  
	        add_map.put(BasPrivRef.SRVC_P0_01, newButton);
	        del_map.put(BasPrivRef.SRVC_P0_03, delButton);
	        save_map.put(BasPrivRef.SRVC_P0_02, saveButton);
	        save_map.put(BasPrivRef.SRVC_P0_04, canButton);
	        this.enableOrDisables(add_map, true);
	        enableOrDisables(del_map, false);
	        this.enableOrDisables(save_map, false);
		
	}
	
	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//第一行：模糊查询
		TextItem txt_global=new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setWidth(300);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		
		//第二行:激活 （复选框）
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());	
		chk_enable.setValue(true);
		form.setItems(txt_global,chk_enable);
		return form;
	}

	

	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_TRANS_SERVICE");	
		cache_map.put("ENABLE_FLAG", "Y");
		check_map.put("SRVC_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SRVC_NAME());
		check_map.put("SRVC_NAME", StaticRef.CHK_UNIQUE+Util.TI18N.SRVC_NAME());
		check_map.put("SHORT_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SHORT_NAME());
		check_map.put("SHORT_NAME", StaticRef.CHK_UNIQUE+Util.TI18N.SHORT_NAME());
		check_map.put("DEFAULT_FLAG", StaticRef.CHK_UNIQUE+Util.TI18N.DEFAULT_FLAG());
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasTransServiceView view = new BasTransServiceView();
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
