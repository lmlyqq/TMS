package com.rd.client.view.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.vehCapcity.CanlBlacklistAction;
import com.rd.client.action.base.vehCapcity.PullBlacklistAction;
import com.rd.client.action.base.vehCapcity.SaveVehCapacityAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.FreeAction;
import com.rd.client.common.action.FreezeAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BasVehSuplierDS;
import com.rd.client.ds.base.HaulingCapacityManagerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.BasDriverWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运力管理
 * 
 * @author lijun
 * @param DS
 * @param table
 * @param vm
 * @param searchItem
 * @param searchWin
 * @param section
 * @param basicPanel
 *            基础信息面板容器
 * @param manufacturerPanel
 *            车辆档案
 * @param controlPanel
 *            运力管理---控制信息所用容器
 */
@ClassForNameAble
public class BasVehCapacityView extends SGForm implements PanelFactory {
	private DataSource DS;
	private SGTable table;
	private ValuesManager vm;
	private Window searchWin;
	public SGPanel basicPanel;
	private SGPanel manufacturerPanel;
	//private SGPanel controlPanel;
	private SGPanel searchForm = new SGPanel();
	private SectionStack m_section;
	private SectionStackSection listItem;
	private VLayout main_layout;
	public IButton freezeButton;
	public IButton freeButton;
	public IButton PullControl;
	public IButton CanlControl;
	public Record rec;
	//private TileGrid tileGrid;
	private Canvas canvas;
	private Canvas tileCanvas;
	private int tab_number = 0;
	//private String trailer_flag_;
	private SGCombo TRAILER_NO;
	private String plate_no;//fanglm 2011-6-21 车牌号
	private Window uploadWin;

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		main_layout = new VLayout();// 定义全局布局
		main_layout.setWidth100();
		main_layout.setHeight100();

		ToolStrip toolStrip = new ToolStrip();// 定义按钮
		toolStrip.setAlign(Alignment.RIGHT);

		HStack stack = new HStack();// 设置详细信息布局
		stack.setWidth("99%");
		stack.setHeight100();

		DS = HaulingCapacityManagerDS
				.getInstall("V_BAS_VEHICLE", "BAS_VEHICLE");// 实例化数据源

		table = new SGTable(DS, "100%", "100%", true, true, false);
		basicPanel = new SGPanel();
		basicPanel.setWidth("50%");
		manufacturerPanel = new SGPanel();
		manufacturerPanel.setWidth("50%");
		//controlPanel = new SGPanel();
		//controlPanel.setTitleWidth(63);
		createMainListFields(table);
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		m_section = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		m_section.setWidth("100%");
//		listItem.setControls(addMaxBtn(m_section, stack, "200" ,true), new SGPage(table, true).initPageBtn());
		listItem.setControls(new SGPage(table, true).initPageBtn());
		m_section.addSection(listItem);
		// m_section = createSection(table, null, true, true);

		TabSet rightTabSet = new TabSet();
		rightTabSet.setWidth("80%");
		rightTabSet.setHeight("100%");
		rightTabSet.setMargin(0);
		rightTabSet.setVisible(false);

		// 基础信息框架
		if(isPrivilege(BasPrivRef.VEH_P1)) {
			
			Tab mainTab = new Tab(Util.TI18N.MAININFO());
			mainTab.setPane(createMainInfo());
			rightTabSet.addTab(mainTab);
		}
		
		if(isPrivilege(BasPrivRef.VEH_P3)) {
			
			Tab imgInfoTab = new Tab(Util.TI18N.IMG_INFO());
			imgInfoTab.setPane(createImgInfo());
			rightTabSet.addTab(imgInfoTab);
		}
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
				if(isMax) {
					expend();
				}
			}
		});

		vm.addMember(basicPanel);
		//vm.addMember(controlPanel);
		vm.addMember(manufacturerPanel);

		createBtnWidget(toolStrip);
		stack.addMember(m_section);
		addSplitBar(stack);
		stack.addMember(rightTabSet);
		main_layout.addMember(toolStrip);
		main_layout.addMember(stack);
		initVerify();
//		addDoubeclick(table, listItem, rightTabSet, m_section);
		
		rightTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tab_number = event.getTabNum();
				if(tab_number == 1){
					if(table.getSelectedRecord() != null){
						
						if(canvas != null){
							if(canvas.isCreated()){
								canvas.destroy();
							}
						}
						canvas = new Canvas();
						tileCanvas.addChild(canvas);
						canvas.setHeight(163);
						canvas.setWidth(780);
						if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
							plate_no = table.getSelectedRecord().getAttribute("PLATE_NO");
							ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_VEHCAPACITY_PREVIEW_URL,table.getSelectedRecord().getAttribute("PLATE_NO"));
							action.getName();
							
						}
					}
				}
			}
		});
	    table.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					Record record = event.getRecord();
					if(ObjUtil.isNotNull(record)){
						vm.editRecord(record);
					}
					
					initSaveBtn();
					if(record != null){
						if(record.getAttribute("BLACKLIST_FLAG")!=null){
							if(record.getAttribute("BLACKLIST_FLAG").equals("true")){
								PullControl.disable();
								CanlControl.enable();
							}else{
								PullControl.enable();
								CanlControl.disable();
							}
						}else{
							PullControl.enable();
							CanlControl.disable();
						}
					}
					if(event.getRecord() != null){
						rec = event.getRecord();
						String VEHICLE_STAT = rec.getAttribute("VEHICLE_STAT");
						if("D5595E8BF25A4E0D8C46796B772FB1BA".equals(VEHICLE_STAT)){
							freezeButton.enable();
							freeButton.disable();
						} else {
							freezeButton.disable();
							freeButton.enable();
						}
					} else {
						freezeButton.disable();
						freeButton.disable();
					}

				}
			});
		return main_layout;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		// 对CURD的框的布局
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.VEH);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(DS, 
							createSearchForm(searchForm),m_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}

			}
		});

		IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.VEH_P0_01);
		newButton.addClickHandler(new NewMultiFormAction(vm, cache_map,this));

		IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.VEH_P0_02);
		saveButton.addClickHandler(new SaveVehCapacityAction(table, vm,check_map, this));

		IButton deleteButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.VEH_P0_03);
		deleteButton.addClickHandler(new DeleteProAction(table, vm));

		IButton canlButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.VEH_P0_04);
		canlButton.addClickHandler(new CancelMultiFormAction(table, vm,this));

		freezeButton = createBtn(StaticRef.FREEZE_BTN,BasPrivRef.VEH_P0_05);
		freezeButton.addClickHandler(new FreezeAction(table, vm, this));

		freeButton = createBtn(StaticRef.FREE_BTN,BasPrivRef.VEH_P0_06);
		freeButton.addClickHandler(new FreeAction(table, vm, this));
		
		PullControl=createUDFBtn("黑名单",null,BasPrivRef.VEH_P0_07);
		PullControl.addClickHandler(new PullBlacklistAction(table,vm,this));
		
		CanlControl=createUDFBtn("取消黑名单",null,BasPrivRef.VEH_P0_08);
		CanlControl.addClickHandler(new CanlBlacklistAction(table,vm,this));
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.VEH_P0_10);
        expButton.addClickHandler(new ExportAction(table));

		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.VEH_P0_09);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {	
				JavaScriptObject jsobject = m_section.getSection(0).getAttributeAsJavaScriptObject("controls");
				Canvas[] canvas = null;
				DynamicForm pageForm=null;
				if(jsobject != null) {
					canvas = Canvas.convertToCanvasArray(jsobject);
				}
				for(int i = 0; i < canvas.length; i++) {
					if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
						pageForm = (DynamicForm)canvas[i];
						break;
					}
				}
				if(uploadWin == null){
					uploadWin = new UploadFileWin().getViewPanel("vehicle.xls","TMP_VEHICLE_IMPORT","SP_IMPORT_VEHICLE",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});
		
		
        add_map.put(BasPrivRef.VEH_P0_01, newButton);
        del_map.put(BasPrivRef.VEH_P0_03, deleteButton);
        save_map.put(BasPrivRef.VEH_P0_02, saveButton);
        save_map.put(BasPrivRef.VEH_P0_04, canlButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        freezeButton.disable();
        freeButton.disable();
        PullControl.disable();
        CanlControl.disable();
        
        toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, deleteButton,
				canlButton, freezeButton, freeButton,PullControl,CanlControl,expButton,inputButton);

	}

	/**
	 * 主列表信息
	 * 
	 */

	private void createMainListFields(SGTable table) {
    
		table.setShowRowNumbers(true);

		ListGridField PLATE_NO = new ListGridField("PLATE_NO", Util.TI18N
				.PLATE_NO(), 75);
//		PLATE_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()));
		ListGridField VEHICLE_NO = new ListGridField("VEHICLE_NO", Util.TI18N
				.VEHICLE_NO(), 75);
		ListGridField VEHICLE_STA = new ListGridField("VEHICLE_STAT",Util.TI18N.VEHICLE_STAT());
		VEHICLE_STA.setHidden(true);
		ListGridField VEHICLE_STAT = new ListGridField("VEHICLE_STAT",
				Util.TI18N.VEHICLE_STAT(), 75);
		VEHICLE_STAT.setHidden(true);
		ListGridField VEHICLE_STAT_NAME = new ListGridField("VEHICLE_STAT_NAME",
				Util.TI18N.VEHICLE_STAT(), 75);
		
		ListGridField VECHILE_TYP_ID = new ListGridField("VEHICLE_TYP_ID_NAME",
				Util.TI18N.VECHILE_TYP_ID(), 75);
//		VECHILE_TYP_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N
//				.VECHILE_TYP_ID()));
		ListGridField VEHICLE_ATTR = new ListGridField("VEHICLE_ATTR_NAME",
				Util.TI18N.VEHICLE_ATTR(), 75);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID_NAME",
				Util.TI18N.SUPLR_ID(), 75);
//		SUPLR_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.SUPLR_ID()));
		ListGridField DRIVER1 = new ListGridField("DRIVER1_NAME", Util.TI18N
				.DRIVER1(), 65);
		ListGridField MOBILE = new ListGridField("MOBILE1", Util.TI18N.MOBILE(),
				70);
//		ListGridField LOCATION = new ListGridField("LOCATION", Util.TI18N
//				.LOCATION(), 70);
		ListGridField MAX_WEIGHT = new ListGridField("MAX_WEIGHT", Util.TI18N
				.MAX_WEIGHT(), 70);
		MAX_WEIGHT.setShowHover(true);
		ListGridField MAX_VOLUME = new ListGridField("MAX_VOLUME", Util.TI18N
				.MAX_VOLUME(), 75);
		ListGridField ORG_ID = new ListGridField("ORG_ID_NAME", Util.TI18N.ORG_ID(),75);
		ListGridField TRAILER_NO = new ListGridField("TRAILER_NO", Util.TI18N.TRAILER_NO(), 75);
		TRAILER_NO.setHidden(true);
		
		ListGridField TRAILER_FLAG = new ListGridField("TRAILER_FLAG", Util.TI18N.TRAILER_NO(), 75);
		TRAILER_FLAG.setHidden(true);
		
		ListGridField VEH_LOCK_REASON = new ListGridField("VEH_LOCK_REASON",Util.TI18N.VEH_LOCK_REASON());
		VEH_LOCK_REASON.setHidden(true);
		
		ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON());
		REASON.setHidden(true);
		
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),60);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		ListGridField BLACKLIST_FLAG = new ListGridField("BLACKLIST_FLAG",Util.TI18N.BLACKLIST_FLAG(),60);
		BLACKLIST_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(PLATE_NO,VECHILE_TYP_ID, VEHICLE_NO,SUPLR_ID,VEHICLE_STAT,VEHICLE_STAT_NAME, 
				VEHICLE_ATTR, DRIVER1, MOBILE, MAX_WEIGHT,
				MAX_VOLUME, ORG_ID, ENABLE_FLAG,TRAILER_NO,TRAILER_FLAG,VEH_LOCK_REASON,REASON);
		
		
		final Menu menu = new Menu();
	    menu.setWidth(140);

	    table.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
	    
	    MenuItem allSelect2 = new MenuItem("GPS定位",StaticRef.ICON_SEARCH);
	    allSelect2.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(ObjUtil.isNotNull(plate_no)){
					com.google.gwt.user.client.Window.open("http://202.102.112.25/login.aspx?company=sqyhwl&user=sqyhwl&psw=sqyhwl", "", "");
				}else{
					MSGUtil.sayError("请先选择车辆！");
				}
			}
		});
	    menu.addItem(allSelect2);
	}

	// 基础资料中的信息
	public SectionStack createMainInfo() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setBackgroundColor(ColorUtil.BG_COLOR);

		SectionStack section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);

		/**
		 * 基础信息： 第一行：车牌号（普通文本），车辆编号（普通文本），车辆类型（下拉列表）；
		 * 第二行：车辆状态（下拉类表），隶属承运商（下拉列表），所有权机构（下拉列表）
		 * 第三行：车辆属性（下拉表），司机（下拉列表），司机电话（普通文本） 第四行：当前时速（普通文本），长度（普通文本），宽度（普通文本）
		 * 第五行：高度（普通文本），长度单位（下拉列表），额定体积（普通文本）
		 * 第六行：体积单位（下拉表），额定载重（普通文本），重量单位（下拉列表） 第七行：冻结原因（下拉列表），原因描述（长文本）
		 * 第八行：挂车标识（复选框），挂车号（下拉列表）； 第九行：备注（文本框）
		 */
		// 1
		SGText PLATE_NO = new SGText("PLATE_NO", ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()), true);
		
		SGText VEHICLE_NO = new SGText("VEHICLE_NO", Util.TI18N.VEHICLE_NO());
		
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG",Util.TI18N.BLACKLIST_FLAG());
		
		// 2
		SGCombo VECHILE_TYP_ID = new SGCombo("VEHICLE_TYP_ID", ColorUtil.getRedTitle(Util.TI18N.VECHILE_TYP_ID()), true);
		Util.initComboValue(VECHILE_TYP_ID, "BAS_VEHICLE_TYPE", "ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " ORDER BY SHOW_SEQ ASC");

		SGText VEHICLE_STAT = new SGText("VEHICLE_STAT", Util.TI18N.VEHICLE_STAT());
		VEHICLE_STAT.setDisabled(true);
		VEHICLE_STAT.setVisible(false);
		
		SGText VEHICLE_STAT_NAME = new SGText("VEHICLE_STAT_NAME",Util.TI18N.VEHICLE_STAT());
		VEHICLE_STAT_NAME.setDisabled(true);
		
//		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID",ColorUtil.getRedTitle(Util.TI18N.SUPLR_ID()));
//		Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME"," WHERE ENABLE_FLAG = 'Y'", "");
		
		final SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		final SGText SUPLR_ID_NAME = new SGText("SUPLR_ID_NAME",ColorUtil.getRedTitle("隶属承运商"));
		FormItemIcon icon2 = new FormItemIcon();
		SUPLR_ID_NAME.setIcons(icon2);
		SUPLR_ID_NAME.setShowErrorIcon(true);
		icon2.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new SuplrWin(basicPanel,"20%","32%").getViewPanel();
			}
		});
		
		SUPLR_ID_NAME.addKeyDownHandler( new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
					final String sup_name = ObjUtil.ifObjNull(SUPLR_ID_NAME.getValue(),"").toString().toUpperCase();
					if(sup_name.equals("")){
						return;
					}
					System.out.println(123);
					Util.db_async.getRecord("ID,SUPLR_CNAME", "V_SUPPLIER", 
							" where ENABLE_FLAG='Y' and upper(full_index) like upper('%"+sup_name+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							if(size == 1){
								HashMap<String, String> map= result.get(0);
//								SUPLR_ID_NAME.setValue(map.get("SUPLR_CNAME"));
//								SUPLR_ID.setValue(map.get("ID"));
								basicPanel.getItem("SUPLR_ID").setValue(map.get("ID"));
					 			basicPanel.getItem("SUPLR_ID_NAME").setValue(map.get("SUPLR_CNAME"));
							}else if(size > 1){
								new SuplrWin(basicPanel,"20%", "32%",sup_name).getViewPanel();
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
			     }
			}
		});
		
//		SUPLR_ID_NAME.addBlurHandler(new BlurHandler() {
//			
//			@Override
//			public void onBlur(BlurEvent event) {
//				final String name = basicPanel.getItem("SUPLR_ID_NAME").getValue().toString().toUpperCase();
//				Util.db_async.getRecord("ID,SUPLR_CNAME,SUPLR_CODE", "V_SUPPLIER",
//						" where ENABLE_FLAG='Y' and full_index like '%"+name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//					
//					@Override
//					public void onSuccess(ArrayList<HashMap<String, String>> result) {
//						int size = result.size();
//						HashMap<String, String> selectRecord = null;
//						if(size == 1 || selectRecord != null){
//							if(selectRecord == null)selectRecord = result.get(0);
//							basicPanel.getItem("SUPLR_ID").setValue(selectRecord.get("ID"));
//							basicPanel.getItem("SUPLR_ID_NAME").setValue(selectRecord.get("SUPLR_CNAME"));
//						}else if(size > 1){
//							new SuplrWin(basicPanel,"20%", "32%",name).getViewPanel();
//						}
//					}
//					
//					@Override
//					public void onFailure(Throwable caught) {
//						
//					}
//				});
//			}
//		});
		
		SGCheck AVAIL_FLAG = new SGCheck("AVAIL_FLAG",Util.TI18N.AVAIL_FLAG());
		
		// 3
		SGCombo VEHICLE_ATTR = new SGCombo("VEHICLE_ATTR", Util.TI18N.VEHICLE_ATTR(), true);
		Util.initCodesComboValue(VEHICLE_ATTR, "VECHILE_ATTR");
		
		SGCombo TMP_ATTR = new SGCombo("TMP_ATTR", Util.TI18N.TMP_ATTR());
		Util.initCodesComboValue(TMP_ATTR, "TMP_ATTR");
		
		SGText DRIVER1 = new SGText("DRIVER1", Util.TI18N.DRIVER1());
		DRIVER1.setVisible(false);
		
		SGText DRIVER1_NAME = new SGText("DRIVER1_NAME", Util.TI18N.DRIVER1());
		FormItemIcon icon1 = new FormItemIcon();
		DRIVER1_NAME.setIcons(icon1);
		DRIVER1_NAME.setShowErrorIcon(true);
		icon1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new BasDriverWin(basicPanel,"20%", "32%").getViewPanel();
			}
		});
		//Util.initComboValue(DRIVER1, "BAS_STAFF", "ID", "STAFF_NAME"," WHERE STAFF_TYP ='4D279652C2B9423D8AD958E63B9B3547' ", "");
		
		SGText MOBILE = new SGText("MOBILE1", Util.TI18N.MOBILE());
		
		// 4
		final SGCombo LENGTH_UNIT = new SGCombo("LENGTH_UNIT", Util.TI18N.LENGTH_UNIT(), true);
		Util.initComboValue(LENGTH_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME"," WHERE MSRMNT_CODE='LENGHT'", "");
		final SGCombo WEIGHT_UNIT = new SGCombo("WEIGHT_UNIT", Util.TI18N.WEIGHT_UNIT());
		Util.initComboValue(WEIGHT_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME"," WHERE MSRMNT_CODE='WEIGHT'", "");
		
		final SGCombo VOLUME_UNIT = new SGCombo("VOLUME_UNIT", Util.TI18N.VOLUME_UNIT());
		Util.initComboValue(VOLUME_UNIT, "V_BAS_MSRMNT", "UNIT", "UNIT_NAME"," WHERE MSRMNT_CODE='VOLUME'", "");
		
		SGText SPEED = new SGText("SPEED", Util.TI18N.SPEED());
		
		// 5
		final SGText LENGTH = new SGText("LENGTH", "长", true);
		final SGText WIDTH = new SGText("WIDTH", "宽");
		final SGText HEIGHT = new SGText("HEIGHT", "高");
		final SGText MAX_VOLUME = new SGText("MAX_VOLUME", Util.TI18N.MAX_VOLUME());
		
		final SGText MAX_WEIGHT = new SGText("MAX_WEIGHT", Util.TI18N.MAX_WEIGHT(),true);
		SGCombo VEH_LOCK_REASON = new SGCombo("VEH_LOCK_REASON", Util.TI18N.VEH_LOCK_REASON());
		Util.initCodesComboValue(VEH_LOCK_REASON, "VEH_LOCK_REASON");
		VEH_LOCK_REASON.setDisabled(false);
		
		// 6
		SGLText REASON = new SGLText("REASON", Util.TI18N.REASON());
		SGLText NOTES = new SGLText("NOTES", Util.TI18N.NOTES(),true);

		// 7
		TRAILER_NO = new SGCombo("ID", Util.TI18N.TRAILER_NO());
		TRAILER_NO.setDisabled(true);
		TRAILER_NO.setVisible(false);
		final SGCheck TRAILER_FLAG = new SGCheck("TRAILER_FLAG", Util.TI18N.TRAILER_FLAG());
		TRAILER_FLAG.setVisible(false);
		TRAILER_FLAG.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				//trailer_flag_ = event.getValue().toString();
				if("true".equals(event.getValue().toString())){
					TRAILER_NO.setDisabled(false);
					Util.initComboValue(TRAILER_NO, "BAS_VEHICLE", "ID", "PLATE_NO",
							" WHERE TRAILER_FLAG IS NULL AND ENABLE_FLAG='Y'", "");
				}else {
					TRAILER_NO.setDisabled(true);
				}
			}
		});
		
		
		DRIVER1_NAME.addKeyDownHandler( new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
					final String name = basicPanel.getItem("DRIVER1_NAME").getValue().toString();
					Util.db_async.getRecord("ID,STAFF_CODE,STAFF_NAME,MOBILE,DRVR_LIC_NUM,ID_NO", "V_BAS_STAFF",
							" where ENABLE_FLAG='Y' and full_index like '%"+name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							HashMap<String, String> selectRecord = null;
							if(size == 1 || selectRecord != null){
								if(selectRecord == null)selectRecord = result.get(0);
								basicPanel.getItem("DRIVER1").setValue(selectRecord.get("ID"));
								basicPanel.getItem("DRIVER1_NAME").setValue(selectRecord.get("STAFF_NAME"));
								basicPanel.getItem("MOBILE1").setValue(selectRecord.get("MOBILE"));
							}else if(size > 1){
								new BasDriverWin(basicPanel,"20%", "32%",name).getViewPanel();
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
				}
			}
		});

		VECHILE_TYP_ID.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(final ChangedEvent event) {
				
				if(!ObjUtil.isNotNull(event.getValue()))
					return;
				Util.async.getCarInfo(event.getValue().toString(),
						new AsyncCallback<List<HashMap<String, String>>>() {

							@Override
							public void onSuccess(
									List<HashMap<String, String>> result) {
								HashMap<String, String> map = result.get(0);
								LENGTH_UNIT.setValue(map.get("LENGTH_UNIT_NAME"));
								LENGTH.setValue(ObjUtil.ifNull(map.get("LENGTH"),"0"));
								WIDTH.setValue(ObjUtil.ifNull(map.get("WIDTH"),"0"));
								HEIGHT.setValue(ObjUtil.ifNull(map.get("HEIGHT"),"0"));
								WEIGHT_UNIT.setValue(map.get("WEIGHT_UNIT_NAME"));
								MAX_WEIGHT.setValue(ObjUtil.ifNull(map.get("MAX_WEIGHT"),"0"));
								VOLUME_UNIT.setValue(map.get("VOLUME_UNIT_NAME"));
								MAX_VOLUME.setValue(ObjUtil.ifNull(map.get("MAX_VOLUME"),"0"));

							}

							@Override
							public void onFailure(Throwable caught) {

							}
						});

			}
		});

		basicPanel.setWidth("40%");
		basicPanel.setItems(PLATE_NO, VEHICLE_NO, ENABLE_FLAG,BLACKLIST_FLAG , VECHILE_TYP_ID,
				SUPLR_ID,SUPLR_ID_NAME, VEHICLE_STAT,VEHICLE_STAT_NAME,AVAIL_FLAG,VEHICLE_ATTR,TMP_ATTR, DRIVER1,DRIVER1_NAME,
				MOBILE, LENGTH_UNIT, WEIGHT_UNIT, VOLUME_UNIT,SPEED, LENGTH,
				WIDTH, HEIGHT, MAX_VOLUME, MAX_WEIGHT, VEH_LOCK_REASON, REASON,
				TRAILER_NO, TRAILER_FLAG, NOTES);

		/**
		 * 车辆档案 第一行：生产厂家（普通文本），品牌型号（普通文本），车主（普通文本）
		 * 第二行：第二司机（普通文本），发动机号（普通文本），地盘号（普通文本）
		 * 第三行：车架号（普通文本），出厂日期（普通文本），购买日期（普通文本）
		 * 第四行：上牌日期(普通文本），建档日期（普通文本），购买价格（普通文本）
		 * 第五行：月折旧金额（普通文本），百公里空车耗油（普通文本），百公里重车油耗（普通文本）
		 * 第六行：百公里平均油耗（普通文本），当前码表数（普通文本）
		 */

		// 1
		SGText COLD_NO = new SGText("COLD_NO",Util.TI18N.COLD_NO(),true);
		SGText ENGINE_NO = new SGText("ENGINE_NO", Util.TI18N.ENGINE_NO());
		SGText TRAIL_PLATE_NO = new SGText("TRAIL_PLATE_NO", Util.TI18N.TRAIL_PLATE_NO());
		SGText TRAIL_FRAME_NO = new SGText("TRAIL_FRAME_NO", Util.TI18N.TRAIL_FRAME_NO());
		SGText REG_NO = new SGText("REG_NO", Util.TI18N.REG_NO());
		
		//2
		SGText LICENSE_NO = new SGText("LICENSE_NO", Util.TI18N.LICENSE_NO(), true);
		SGText BRAND = new SGText("BRAND", Util.TI18N.BRAND());
		SGText EOC = new SGText("EOC", Util.TI18N.EOC());
		SGText GRADE_EXPIRYDATE = new SGText("GRADE_EXPIRYDATE", Util.TI18N.GRADE_EXPIRYDATE());
		SGText FRAME_NO = new SGText("FRAME_NO", Util.TI18N.FRAME_NO());
		
		//3
		SGText BED_NO = new SGText("BED_NO", Util.TI18N.BED_NO(),true);
		SGText HOC = new SGText("HOC", Util.TI18N.HOC());
		SGText TEMPERATURE1 = new SGText("TEMPERATURE1", "制冷范围从");
		SGText TEMPERATURE2 = new SGText("TEMPERATURE2", "到");
		SGText PCHD_PRICE = new SGText("PCHD_PRICE", Util.TI18N.PCHD_PRICE());
		
		//4
		SGText CREATE_DATE = new SGText("CREATE_DATE", Util.TI18N.CREATE_DATE(),true);
		Util.initDate(vm,CREATE_DATE);
		SGText PRODUCT_DATE = new SGText("PRODUCT_DATE", Util.TI18N.PRODUCT_DATE());
		Util.initDate(vm,PRODUCT_DATE);
		SGText PCHD_DATE = new SGText("PCHD_DATE", Util.TI18N.PCHD_DATE());
		Util.initDate(vm,PCHD_DATE);
		SGText PLATE_DATE = new SGText("PLATE_DATE", Util.TI18N.PLATE_DATE());
		Util.initDate(vm,PLATE_DATE);
		SGText MONTH_DEPRECIATION = new SGText("MONTH_DEPRECIATION", Util.TI18N.MONTH_DEPRECIATION());
		//5
		
		
		
		//manufacturerPanel.setTitleWidth(100);
		manufacturerPanel.setWidth("40%");
		manufacturerPanel.setNumCols(10);
		manufacturerPanel.setItems(COLD_NO,ENGINE_NO,TRAIL_PLATE_NO,TRAIL_FRAME_NO,REG_NO,LICENSE_NO,BRAND,EOC,
				GRADE_EXPIRYDATE,FRAME_NO,BED_NO,HOC,TEMPERATURE1,
				TEMPERATURE2,PCHD_PRICE,CREATE_DATE,PRODUCT_DATE,PCHD_DATE,PLATE_DATE,MONTH_DEPRECIATION);
		
		SectionStackSection basicInfo = new SectionStackSection(Util.TI18N
				.BAS_INFO());
		basicInfo.addItem(basicPanel);
		basicInfo.setExpanded(true);

		SectionStackSection manufacturerPanelInfo = new SectionStackSection(
				Util.TI18N.RECORD());
		manufacturerPanelInfo.addItem(manufacturerPanel);
		manufacturerPanelInfo.setExpanded(true);

		section.addSection(basicInfo);
		section.addSection(manufacturerPanelInfo);

		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);

		return section;
	}


	private DynamicForm createSearchForm(final SGPanel form) {

		form.setTitleWidth(80);
		// 1
//		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
//		txt_global.setTitleOrientation(TitleOrientation.LEFT);
//		txt_global.setWidth(352);
//		txt_global.setColSpan(5);
//		txt_global.setEndRow(true);
//		txt_global.setTitleOrientation(TitleOrientation.TOP);
		final SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
		// 2
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
//		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setColSpan(2);
		EXEC_ORG_ID_NAME.setWidth(FormUtil.Width);
		EXEC_ORG_ID_NAME.setStartRow(true);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, true, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		final SGText SUPLR_ID = new SGText("SUPLR_ID", Util.TI18N.SUPLR_ID());
		SUPLR_ID.setVisible(false);
		final ComboBoxItem SUPLR_ID_NAME = new ComboBoxItem("SUPLR_ID_NAME", Util.TI18N.SUPLR_ID());
		SUPLR_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		SUPLR_ID_NAME.setWidth(FormUtil.Width);
		SUPLR_ID_NAME.setColSpan(2);
		initSuplr(SUPLR_ID_NAME,SUPLR_ID);
		
//		FormItemIcon icon2 = new FormItemIcon();
//		SUPLR_ID_NAME.setIcons(icon2);
//		SUPLR_ID_NAME.setShowErrorIcon(true);
//		icon2.addFormItemClickHandler(new FormItemClickHandler() {
//			
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//				new SuplrWin(form,"20%","50%").getViewPanel();
//			}
//		});
//		SUPLR_ID_NAME.addKeyPressHandler(new KeyPressHandler() {
//			
//			@Override
//			public void onKeyPress(KeyPressEvent event) {
//				if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
//					final String sup_name = ObjUtil.ifObjNull(SUPLR_ID_NAME.getValue(),"").toString();
//					if(sup_name.equals("")){
//						return;
//					}
//					Util.db_async.getRecord("ID,SUPLR_CNAME", "V_SUPPLIER", 
//							" where upper(full_index) like upper('%"+sup_name+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
//						
//						@Override
//						public void onSuccess(ArrayList<HashMap<String, String>> result) {
//							int size = result.size();
//							if(size == 1){
//								HashMap<String, String> map= result.get(0);
//								SUPLR_ID_NAME.setValue(map.get("SUPLR_CNAME"));
//								SUPLR_ID.setValue(map.get("ID"));
//							}else if(size > 1){
//								new SuplrWin(form,"20%", "32%",sup_name).getViewPanel();
//							}
//							
//						}
//						
//						@Override
//						public void onFailure(Throwable caught) {
//							
//						}
//					});
//			     }
//			}
//		});
		
		// 3
		SGCombo VEHICLE_TYPE = new SGCombo("VECHILE_TYP_ID", Util.TI18N
				.VEHICLE_TYPE());
		Util.initComboValue(VEHICLE_TYPE, "BAS_VEHICLE_TYPE", "ID",
				"VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
		SGCheck SUB = new SGCheck("C_ORG_FLAG", "包含下级机构");
		SUB.setValue(true);
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N
				.ENABLE_FLAG(), true);
		ENABLE_FLAG.setValue(true);
		
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG", "黑名单");
		BLACKLIST_FLAG.setValue(false);

		form.setItems(PLATE_NO, EXEC_ORG_ID,EXEC_ORG_ID_NAME,SUPLR_ID_NAME,SUPLR_ID, VEHICLE_TYPE, ENABLE_FLAG,
				SUB,BLACKLIST_FLAG);
		return form;
	}

	private VLayout createImgInfo() {
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);

		HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane
				.setContents("<iframe name='foo' style='position:absolute;width:0;height:0;border:0'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");

		final DynamicForm uploadForm = new DynamicForm();
		uploadForm.setAction("uploadServlet");
		uploadForm.setEncoding(Encoding.MULTIPART);
		uploadForm.setMethod(FormMethod.POST);
		uploadForm.setTarget("foo");

		final UploadItem imageItem = new UploadItem("image", "图片");

		TextItem notes = new TextItem("NOTES", "描述");

		SGButtonItem saveItem = new SGButtonItem("上传","上传");
//		saveItem.setAutoFit(true);
//		saveItem.setIcon(StaticRef.IMPORT_BTN);
	    saveItem.setWidth(60);
	    saveItem.setAutoFit(true);
	    saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord() !=  null){
					if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
						Map<String,String> map = uploadForm.getValues();
						if(map.get("image") != null){
							String image = map.get("image").toString();
//							new ImageUploadAction(table.getSelectedRecord().getAttribute("PLATE_NO").toString(),StaticRef.BAS_VEHCAPACITY_URL,uploadForm.getValues().toString(),uploadForm);	
							new ImageUploadAction(table.getSelectedRecord().getAttribute("PLATE_NO").toString(),StaticRef.BAS_VEHCAPACITY_URL,image,uploadForm).onClick(event);	
							if(table.getSelectedRecord() != null){
								
								if(canvas != null){
									if(canvas.isCreated()){
										canvas.destroy();
									}
								}
								canvas = new Canvas();
								tileCanvas.addChild(canvas);
								canvas.setHeight(163);
								canvas.setWidth(1000);
							    if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
							    	ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_VEHCAPACITY_PREVIEW_URL,table.getSelectedRecord().getAttribute("PLATE_NO"));
							    	action.getName();
									
							    }
							}
						}else{
							MSGUtil.sayWarning("请选择所要上传的图片");
						}
					}
				} else {
					MSGUtil.sayWarning("请选择上传图片对应的车牌号.");
				}
				
			}
		});
	    
//		saveItem.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//						if(table.getSelectedRecord() !=  null){
//							if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
//								if(!"{}".equals(uploadForm.getValues().toString())){
////									new ImageUploadAction(table.getSelectedRecord().getAttribute("PLATE_NO").toString(),StaticRef.BAS_VEHCAPACITY_URL,uploadForm.getValues().toString(),uploadForm);	
//									new ImageUploadAction(table.getSelectedRecord().getAttribute("PLATE_NO").toString(),StaticRef.BAS_VEHCAPACITY_URL,uploadForm.getValues().toString(),uploadForm).onClick(event);	
//								}else{
//									MSGUtil.sayWarning("请选择所要上传的图片");
//								}
//							}
//						} else {
//							MSGUtil.sayWarning("请选择上传图片对应的车牌号.");
//						}
//					}
//				});
//
		uploadForm.setItems(imageItem,notes,saveItem);
		
		 	tileCanvas = new Canvas();
	        tileCanvas.setBorder("1px solid black");
	        tileCanvas.setHeight(200);  
	        tileCanvas.setWidth100();  
	        table.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
					if("Y".equals(table.getSelectedRecord().getAttribute("TRAILER_FLAG"))){
						TRAILER_NO.setDisabled(false);
						
						Util.initComboValue(TRAILER_NO, "BAS_VEHICLE", "ID", "PLATE_NO",
								"  WHERE TRAILER_FLAG IS NULL AND ENABLE_FLAG='Y'", "");
					} else {
						TRAILER_NO.setDisabled(true);
					}
				}
			});
	        table.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					
					if("Y".equals(event.getRecord().getAttribute("TRAILER_FLAG"))){
						TRAILER_NO.setDisabled(false);
						
						Util.initComboValue(TRAILER_NO, "BAS_VEHICLE", "ID", "PLATE_NO",
								"  WHERE TRAILER_FLAG IS NULL AND ENABLE_FLAG='Y'", "");
					} else {
						TRAILER_NO.setDisabled(true);
					}
					
					if(tab_number == 2){
						if(table.getSelectedRecord() != null){
							
							if(canvas != null){
								if(canvas.isCreated()){
									canvas.destroy();
								}
							}
							canvas = new Canvas();
							tileCanvas.addChild(canvas);
							canvas.setHeight(163);
							canvas.setWidth(780);
							if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
								ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_VEHCAPACITY_PREVIEW_URL,table.getSelectedRecord().getAttribute("PLATE_NO"));
								action.getName();
								
							}
						}
					}
				}
			});
	        
			
//	        table.addClickHandler(new ClickHandler() {
//				
//				@Override
//				public void onClick(ClickEvent event) {
//					if(table.getSelectedRecord() != null){
//					
//						if(canvas != null){
//							if(canvas.isCreated()){
//								canvas.destroy();
//							}
//						}
//						canvas = new Canvas();
//						tileCanvas.addChild(canvas);
//						canvas.setHeight(163);
//						canvas.setWidth(1000);
//					    if(table.getSelectedRecord().getAttribute("PLATE_NO") != null){
//					    	ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_VEHCAPACITY_PREVIEW_URL,table.getSelectedRecord().getAttribute("PLATE_NO"));
//					    	action.getName();
//							
//					    }
//					}
//				}
//			});
	        vLay.setMembers(uploadForm,tileCanvas,htmlPane);


		return vLay;
	}
	
	private void initSuplr(final ComboBoxItem suplr_name,final TextItem suplr_id){
		DataSource supDS = BasVehSuplierDS.getInstall("BAS_VEH_SUPPLIER1", "BAS_SUPPLIER");
		ListGridField SUPLR_CODE = new ListGridField("SUPLR_CODE","承运商代码",80);
		ListGridField SUPLR_CNAME = new ListGridField("SUPLR_CNAME","承运商名称",80);
		suplr_name.setOptionDataSource(supDS);  
		suplr_name.setDisabled(false);
		suplr_name.setShowDisabled(false);
		suplr_name.setDisplayField("FULL_INDEX");  
		suplr_name.setPickListBaseStyle("myBoxedGridCell");
		suplr_name.setPickListWidth(180);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		//criteria.addCriteria("LOGIN_USER",LoginCache.getLoginUser().getUSER_ID());
		suplr_name.setPickListCriteria(criteria);
		
		suplr_name.setPickListFields(SUPLR_CODE, SUPLR_CNAME);
		suplr_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				final Record selectedRecord  = suplr_name.getSelectedRecord();
				if(selectedRecord != null){				
					suplr_name.setValue(selectedRecord.getAttribute("SUPLR_CNAME"));
					suplr_id.setValue(selectedRecord.getAttribute("ID"));				
				}
			}
		});
	}
	
	
	
	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		// table.destroy();
		// manufacturerPanel.destroy();
		// controlPanel.destroy();
		// basicPanel.destroy();

	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "V_BAS_VEHICLE");
		check_map
				.put("PLATE_NO", StaticRef.CHK_NOTNULL + Util.TI18N.PLATE_NO());
		check_map.put("PLATE_NO", StaticRef.CHK_UNIQUE + Util.TI18N.PLATE_NO());
//		check_map.put("VEHICLE_STAT", StaticRef.CHK_NOTNULL
//				+ Util.TI18N.VEHICLE_STAT());
		check_map.put("VEHICLE_TYP_ID", StaticRef.CHK_NOTNULL
				+ Util.TI18N.VECHILE_TYP_ID());
		check_map
				.put("SUPLR_ID", StaticRef.CHK_NOTNULL + Util.TI18N.SUPLR_ID());
		check_map.put("PRODUCT_DATE", StaticRef.CHK_DATE
				+ Util.TI18N.PRODUCT_DATE());
		check_map.put("PCHD_DATE", StaticRef.CHK_DATE + Util.TI18N.PCHD_DATE());

		check_map.put("PLATE_DATE", StaticRef.CHK_DATE
				+ Util.TI18N.PLATE_DATE());
		check_map.put("CREATE_DATE", StaticRef.CHK_DATE
				+ Util.TI18N.CREATE_DATE());
		check_map.put("INS_EFCT_DT", StaticRef.CHK_DATE
				+ Util.TI18N.INS_EFCT_DT());
		check_map.put("INS_EXP_DT", StaticRef.CHK_DATE
				+ Util.TI18N.INS_EXP_DT());

		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("ORG_ID", LoginCache.getLoginUser()
				.getDEFAULT_ORG_ID());
		cache_map.put("ORG_ID_NAME", LoginCache.getLoginUser()
				.getDEFAULT_ORG_ID_NAME());
		cache_map.put("MAX_WEIGHT", "0");
		cache_map.put("MAX_VOLUME", "0");
		cache_map.put("LENGTH", "0");
		cache_map.put("WIDTH", "0");
		cache_map.put("HEIGHT", "0");
		cache_map.put("PCHD_PRICE", "0");
		cache_map.put("MONTH_DEPRECIATION","0");
		cache_map.put("EOC", "0");
		cache_map.put("AOC", "0");
		cache_map.put("HOC", "0");
		cache_map.put("INS_AMT", "0");
		cache_map.put("VEHICLE_STAT","D5595E8BF25A4E0D8C46796B772FB1BA");
		cache_map.put("VEHICLE_STAT_NAME","可用");
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasVehCapacityView view = new BasVehCapacityView();
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
