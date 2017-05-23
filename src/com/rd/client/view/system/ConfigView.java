package com.rd.client.view.system;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.system.SysParamDS;
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
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理->系统参数
 * @author fanglm
 *
 */
@ClassForNameAble
public class ConfigView extends SGForm implements PanelFactory {
	
	 private DataSource ds;
	 private SGTable table;
	 private Window searchWin;//lijun 添加
	 private SGPanel searchForm;//lijun 添加
	 //private static ButtonItem searchItem;
	 private SectionStack section;
	 
	 /*public ConfigView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
	    ToolStrip toolStrip = new ToolStrip();
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = SysParamDS.getInstance("SYS_PARAM", "SYS_PARAM");
		   
		table = new SGTable(ds, "100%", "70%"); 
		createListFields(table);
		table.setShowFilterEditor(false);
		section = createSection(table, null, true, true);
	    
		createBtnWidget(toolStrip);
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
	    
		initVerify();
		return main;
	}
	

	@Override
	public void createForm(DynamicForm form) {
		
	}
    //布局列表信息按钮
	private void createListFields(SGTable table) {
		
//		table.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				initSaveBtn();
//			}
//		});
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
		        enableOrDisables(save_map, true);
			}
		});
		  ListGridField CONFIG_ID = new ListGridField("CONFIG_CODE", Util.TI18N.CFG_PARAM(), 160); 
		  CONFIG_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.CFG_PARAM()));
		  ListGridField DESCR = new ListGridField("DESCR", Util.TI18N.CFG_DESCR(), 240);
		  ListGridField VALUE_INT = new ListGridField("VALUE_INT",Util.TI18N.CFG_INT(),80);  
		  ListGridField VALUE_STRING = new ListGridField("VALUE_STRING",Util.TI18N.CFG_STRING(),80);  
		  ListGridField TYPE = new ListGridField("SYS_TYPE",Util.TI18N.CFG_TYPE(),80);
		  Util.initCodesComboValue(TYPE,"SYS_TYP");

		  ListGridField MODEL = new ListGridField("MODEL",Util.TI18N.CFG_MODEL(),80);
		  Util.initCodesComboValue(MODEL,"FUN_MOD");
		  ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.CFG_UDF1(),80);
		  ListGridField UDF2 = new ListGridField("UDF2",Util.TI18N.CFG_UDF2(),80);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),80);
		  ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ENABLE_FLAG.setDefaultValue(true);
		  //ListGridField MODIFY_FLAG = new ListGridField("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG(),80);
		  //MODIFY_FLAG.setType(ListGridFieldType.BOOLEAN);
		  table.setFields(CONFIG_ID, DESCR, VALUE_INT, VALUE_STRING, TYPE, MODEL, ENABLE_FLAG, UDF1, UDF2);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SysPrivRef.PARAM);
        searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
        		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.PARAM_P0_01);
        newButton.addClickHandler(new NewAction(table,null,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.PARAM_P0_02);
        saveButton.addClickHandler(new SaveAction(table,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.PARAM_P0_03);
        delButton.addClickHandler(new DeleteAction(table));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.PARAM_P0_04);
        canButton.addClickHandler(new CancelAction(table,this));
        
//        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SysPrivRef.PARAM_P0_05);
        //expButton.addClickHandler(new ExportAction(table,searchItem,"addtime desc"));
    
        add_map.put(SysPrivRef.PARAM_P0_01, newButton);
        del_map.put(SysPrivRef.PARAM_P0_03, delButton);
        save_map.put(SysPrivRef.PARAM_P0_02, saveButton);
        save_map.put(SysPrivRef.PARAM_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	}
	public DynamicForm createSerchForm(SGPanel form) {
		
		//1第一行：模糊查询
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(6);
		txt_global.setEndRow(true);
		
		
		//2第二行：配置参数，使用下拉列表（ComboBoxItem）
		SGText txt_param = new SGText("CONFIG_CODE", Util.TI18N.CFG_PARAM(),true);
		//描述
		SGText txt_descr = new SGText("DESCR",Util.TI18N.CFG_DESCR());
		
		//3第三行
		SGText txt_CFG = new SGText("VALUE_STRING",Util.TI18N.CFG_STRING(),true);
		
		//SGCheck chk_modify = new SGCheck("MODIFY_FLAG", Util.TI18N.MODIFY_FLAG(),true);
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		chk_enable.setValue(true);
		
		form.setItems(txt_global, txt_param, txt_descr,txt_CFG, chk_enable);
		
		return form;
	}
	
	 //设定需要校验的字段
	 public void initVerify() {
		check_map.put("TABLE", "SYS_PARAM");
		
		check_map.put("CONFIG_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.CFG_PARAM());
	 }
	 
	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
		table.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		ConfigView view = new ConfigView();
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
