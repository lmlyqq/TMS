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
import com.rd.client.ds.base.AccountAreaDS;
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
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 结算区域 
 * @author lijun
 * @param AccountDS  结算区域数据源
 * @param table   存放字段容器，继承自listGrid
 * @param searchWin 查询的二级窗口
 * @param searchForm 查询窗口
 * @param searchItem 分页管理
 */
@ClassForNameAble
public class BasChargeRegionview extends SGForm implements PanelFactory 
{
   private DataSource AccountDS;
   private SGTable table;
   private Window searchWin;
   private SGPanel searchForm;
   private SectionStack section;
   
   /*public BasChargeRegionview(String id) {
	   super(id);
   }*/
   
	//页面的整体布局
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		AccountDS = AccountAreaDS.getInstall("V_CHARGE_REGION");
		table = new SGTable(AccountDS,"100%","70%");
		createListFields(table);
		table.setShowFilterEditor(false);
        
		section = createSection(table, null, true, true);
		createBtnWidget(toolStrip);
		VLayout layout = new VLayout();
		layout.setHeight100();
		layout.setWidth100();
		initVerify();
		layout.addMember(toolStrip);
		layout.addMember(section);
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		
		return layout;
	}

	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setHeight("20");
		toolStrip.setWidth("100%");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.REGION);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(AccountDS,createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
		IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.REGION_P0_01);
		newButton.addClickHandler(new NewAction(table,cache_map,this));
		
		IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.REGION_P0_02);
		saveButton.addClickHandler(new SaveAction(table,check_map,this));
		
		IButton deleteButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.REGION_P0_03);
		deleteButton.addClickHandler(new DeleteAction(table));
		
		IButton canlButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.REGION_P0_04);
		canlButton.addClickHandler(new CancelAction(table,this));
		
		IButton exportButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.REGION_P0_05);
		exportButton.addClickHandler(new ExportAction(table, "addtime desc"));
		
        add_map.put(BasPrivRef.REGION_P0_01, newButton);
        del_map.put(BasPrivRef.REGION_P0_03, deleteButton);
        save_map.put(BasPrivRef.REGION_P0_02, saveButton);
        save_map.put(BasPrivRef.REGION_P0_04, canlButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,newButton,saveButton,deleteButton,canlButton,exportButton);
		
		
		
	}
	
	private void createListFields(final SGTable table){
//		table.addSelectionChangedHandler(new SelectionChangedHandler() {
//			public void onSelectionChanged(SelectionEvent event) {
//                
//                initSaveBtn();
//            }
//        });
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				OP_FLAG = "M";
//				enableOrDisables(del_map, true);
				initSaveBtn();
			}
		});
		final ListGridField CHARGE_REGION_NAME = new ListGridField("CHARGE_REGION_NAME",ColorUtil.getRedTitle(Util.TI18N.CHARGE_REGION_NAME()),80);
		
		final ListGridField CHARGE_REGION_ENAME = new ListGridField("CHARGE_REGION_ENAME",Util.TI18N.CHARGE_REGION_ENAME(),120);
		
		final ListGridField AREA_CUSTOMER_ID = new ListGridField("CUSTOMER_ID",Util.TI18N.CUSTOMER(),150);

//		ListGridField AREA_CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER(),150);
//		AREA_CUSTOMER_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()));
		Util.initComboValue(AREA_CUSTOMER_ID, "BAS_CUSTOMER", "ID", "CUSTOMER_CNAME"," WHERE ENABLE_FLAG='Y' AND CUSTOMER_FLAG='Y'","");
		AREA_CUSTOMER_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER()));
		AREA_CUSTOMER_ID.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				int row = event.getRowNum();
				table.setEditValue(row, "CUSTOMER_NAME", AREA_CUSTOMER_ID.getDisplayField());
			}
		});
		
		ListGridField AREA_ID = new ListGridField("AREA_ID",Util.TI18N.AREA_NAME(),120);
		ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.AREA_NAME(),120);
		AREA_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.AREA_NAME()));
		//Util.initComboValue(AREA_ID, "BAS_AREA", "ID", "AREA_CNAME"," WHERE ENABLE_FLAG='Y'","");
		ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),120);
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),80);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),40);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.UDF1(),80);
		ListGridField UDF2 = new ListGridField("UDF2",Util.TI18N.UDF2(),80);
		
		CHARGE_REGION_NAME.addEditorExitHandler(new GetHintAction(table));
		table.setFields(CHARGE_REGION_NAME,AREA_CUSTOMER_ID,AREA_NAME,AREA_ID,CHARGE_REGION_ENAME,HINT_CODE
				        ,SHOW_SEQ,ENABLE_FLAG,UDF1,UDF2);
		AREA_ID.setCanEdit(false);
		AREA_ID.setHidden(true);
		
		Util.initArea(table,AREA_NAME,"ARA_ID", "AREA_NAME", "");
	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		
		//1
		TextItem FULL_INDEX = new TextItem("FULL_INDEX",Util.TI18N.FUZZYQRY()); 
		FULL_INDEX.setTitleOrientation(TitleOrientation.LEFT);
		FULL_INDEX.setWidth(352);
		FULL_INDEX.setColSpan(5);
		FULL_INDEX.setEndRow(true);
	
		FULL_INDEX.setTitleOrientation(TitleOrientation.TOP);
		
		//2
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		ENABLE_FLAG.setValue(true);
		
		form.setItems(FULL_INDEX,ENABLE_FLAG);
		
		
		
		return form;
	}
	

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void initVerify() {
		check_map.put("TABLE", "BAS_CHARGE_REGION");
	
		
		//CHARGE_REGION_NAME 唯一且非空
		check_map.put("CHARGE_REGION_NAME",StaticRef.CHK_NOTNULL+Util.TI18N.CHARGE_REGION_NAME());
		check_map.put("CHARGE_REGION_NAME", StaticRef.CHK_UNIQUE+Util.TI18N.CHARGE_REGION_NAME());
		
		//CUSTOMER_ID 唯一且非空
		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL+Util.TI18N.AREA_CUSTOMER_ID());
		
		//AERA_ID 唯一且非空
		check_map.put("AREA_NAME", StaticRef.CHK_NOTNULL+Util.TI18N.AREA_NAME());//wangjun
		check_map.put("AREA_NAME", StaticRef.CHK_UNIQUE+Util.TI18N.AREA_NAME());// 
		
//		//HINT_CODE 
//		check_map.put("HINT_CODE", StaticRef.CHK_NOTNULL+Util.TI18N.HINT_CODE());
//		check_map.put("HINT_CODE", StaticRef.CHK_NOTNULL+Util.TI18N.HINT_CODE());
		
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("CUSTOMER_ID", "");
		
	}

	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasChargeRegionview view = new BasChargeRegionview();
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
