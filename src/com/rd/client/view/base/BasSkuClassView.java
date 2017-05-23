package com.rd.client.view.base;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.SkuClsDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
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
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 基础资料 --货品分类管理
 * @author fanglm
 *
 */
@ClassForNameAble
public class BasSkuClassView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin = null;
	public TreeNode selectNode;
	public Record selectRecord;
	public SGPanel searchForm = new SGPanel();
	private SectionStack section;
    
	/*public BasSkuClassView() {
		super(id);
	}*/
	
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		initVerify();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		ds = SkuClsDS.getInstance("V_BAS_SKU_CLS","BAS_SKU_CLS");
		
		table = new SGTable(ds, "100%", "100%");
		table.setShowFilterEditor(false);
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				initSaveBtn();
			}
		});
        
		createListFields(table);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		  
		section = createSection(table, null, true, true);
		section.setWidth("100%");
		section.setHeight100();
		
	        
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		  
		return main;
	}
	
	//创建列表
	private void createListFields(SGTable table){
		  ListGridField SKU_CLS = new ListGridField("SKUCLS", Util.TI18N.SKU_CLS(), 80); 
		  SKU_CLS.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_CLS()));
		  ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID", Util.TI18N.CUSTOMER(), 140);
		  Util.initComboValue(CUSTOMER_ID, "BAS_CUSTOMER","ID","CUSTOMER_CNAME"," where enable_flag='Y' and customer_flag='Y'", "");
		  ListGridField DESCRC = new ListGridField("DESCR_C",Util.TI18N.DESCRC(),120);  
		  DESCRC.setTitle(ColorUtil.getRedTitle(Util.TI18N.DESCRC()));
		  ListGridField DESCRE = new ListGridField("DESCR_E",Util.TI18N.DESCRE(),120); 
		  ListGridField FACTOR = new ListGridField("FACTOR",Util.TI18N.FACTOR(),60);
		  ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),60);
		  ListGridField ISMIX = new ListGridField("MIX_FLAG",Util.TI18N.ISMIX(),60);
		  ISMIX.setType(ListGridFieldType.BOOLEAN);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),60);
		  ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ENABLE_FLAG.setDefaultValue(true);
//		  ListGridField MODIFY_FLAG = new ListGridField("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG(),60);
//		  MODIFY_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.SKU_CLS_UDF1(),100);
		  ListGridField UDF2 = new ListGridField("UDF2",Util.TI18N.SKU_CLS_UDF2(),100);
		  table.setFields(SKU_CLS, CUSTOMER_ID, DESCRC, DESCRE,FACTOR , SHOW_SEQ ,ISMIX,ENABLE_FLAG,UDF1, UDF2);		
//		  HashMap<String, String> tMap = new HashMap<String, String>();
//		  tMap.put("001", "合肥海尔");
//		  tMap.put("002", "合肥美菱");
//		  tMap.put("003", "合肥格力");
//		  tMap.put("004", "合肥小天鹅");
//		  CUSTOMER_ID.setValueMap(tMap);
//		  CUSTOMER_ID.setEditorType(new ComboBoxItem());  //设置下拉框类型，可编辑下拉框
//		  CUSTOMER_ID.setFilterOnKeypress(true);
		  
	}
	
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.SKUCLS);       
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(ds,createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.SKUCLS_P0_01);
        newButton.addClickHandler(new NewAction(table,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SKUCLS_P0_02);
        saveButton.addClickHandler(new SaveAction(table,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.SKUCLS_P0_03);
        delButton.addClickHandler(new DeleteAction(table));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SKUCLS_P0_04);
        canButton.addClickHandler(new CancelAction(table,this));
        
        add_map.put(BasPrivRef.SKUCLS_P0_01,newButton);
        del_map.put(BasPrivRef.SKUCLS_P0_03, delButton);
        save_map.put(BasPrivRef.SKUCLS_P0_02, saveButton);
        save_map.put(BasPrivRef.SKUCLS_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	}

	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(352);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		
		SGCombo txt_factor = new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(txt_factor,LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		
		SGCheck chk_mix = new SGCheck("MIX_FLAG",Util.TI18N.ISMIX());
		
		SGCheck txt_enable = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),true);
		txt_enable.setValue(true);
		
//		SGCheck txt_mofify = new SGCheck("MODIFY_FLAG",Util.TI18N.MODIFY_FLAG());
//		txt_mofify.setColSpan(1);
		
        form.setItems(txt_global,txt_factor,chk_mix,txt_enable);
        
        return form;
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	public void initVerify() {
		check_map.put("TABLE", "BAS_SKU_CLS");
		check_map.put("SKUCLS", StaticRef.CHK_UNIQUE+Util.TI18N.SKU_CLS());
		check_map.put("DESCR_C", StaticRef.CHK_NOTNULL + Util.TI18N.DESCRC());
		
		cache_map.put("CUSTOMER_ID", "");
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("MIX_FLAG", "Y");
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasSkuClassView view = new BasSkuClassView();
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
