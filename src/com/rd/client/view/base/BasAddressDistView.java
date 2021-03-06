package com.rd.client.view.base;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.base.AddrDistDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

@ClassForNameAble
public class BasAddressDistView extends SGForm implements PanelFactory{
	
	private DataSource ds;
	private SGTable table;
	public ValuesManager vm;
	private DynamicForm mainForm;
	private SectionStack section;
	private SectionStack sectionStack;
	private Window searchWin = null;
	public SGPanel searchForm = new SGPanel();

	/*public BasAddressDistView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip(); // 按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = AddrDistDS.getInstance("V_ADDRESS_DIST", "BAS_ADDRESS_DIST");
		
		// 主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%",true,true,false);
		table.setShowHover(true);
		table.setShowFilterEditor(false);
		createListFields();
		table.setCanEdit(false);
		sectionStack = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    sectionStack.addSection(listItem);
//	    listItem.setControls(addMaxBtn(sectionStack, stack, "200",true), new SGPage(table,true).initPageBtn());
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		addSplitBar(stack);
		
		//STACK的右边布局
		
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
  
        Tab tab1 = new Tab(Util.TI18N.MAININFO());
		//组织明细的FORM布局
	
		tab1.setPane(createHeader());
        leftTabSet.addTab(tab1);
        stack.addMember(leftTabSet);
		
		vm.addMember(mainForm);
		vm.setDataSource(ds);
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		
		initVerify();
		return main;
	}
	
	private void createListFields(){
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord selRecord = table.getSelectedRecord();
				if(selRecord == null)return;
				vm.editRecord(selRecord);
				vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				initSaveBtn();
				mainForm.getItem("ADDR_NAME1").setValue(ObjUtil.ifNull(selRecord.getAttribute("ADDR_NAME1"), ""));
				mainForm.getItem("ADDR_NAME2").setValue(ObjUtil.ifNull(selRecord.getAttribute("ADDR_NAME2"), ""));
				mainForm.getItem("ADDR_ID1").setValue(ObjUtil.ifNull(selRecord.getAttribute("ADDR_ID1"), ""));
				mainForm.getItem("ADDR_ID2").setValue(ObjUtil.ifNull(selRecord.getAttribute("ADDR_ID2"), ""));
				mainForm.getItem("AREA_ID1").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_ID1"), ""));
				mainForm.getItem("AREA_ID2").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_ID2"), ""));
				
				mainForm.getItem("AREA_ID22").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_NAME2"), ""));
				if(ObjUtil.isNotNull(selRecord.getAttribute("AREA_ID1"))){
					Util.initComboValue(mainForm.getItem("AREA_ID21"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + table.getSelectedRecord().getAttribute("AREA_ID1") + "'", "", table.getSelectedRecord().getAttribute("AREA_ID21"));
				}else if(!ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("AREA_ID21"))){
					mainForm.getItem("AREA_ID21").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_NAME1"), ""));
				}
				if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("AREA_ID2"))){
					Util.initComboValue(mainForm.getItem("AREA_ID22"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + table.getSelectedRecord().getAttribute("AREA_ID2") + "'", "", table.getSelectedRecord().getAttribute("AREA_ID22"));
				}else if(!ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("AREA_ID22"))){
					mainForm.getItem("AREA_ID22").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_NAME2"), ""));
				}
				mainForm.getItem("AREA_NAME1").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_NAME1"), ""));
				mainForm.getItem("AREA_NAME2").setValue(ObjUtil.ifNull(selRecord.getAttribute("AREA_NAME2"), ""));
				mainForm.getItem("MILEAGE").setValue(selRecord.getAttribute("MILEAGE"));
				mainForm.getItem("ADDR_FLAG").setValue(Boolean.valueOf(selRecord.getAttribute("ADDR_FLAG")));
				
			}
		});
		
		table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("AREA_ID1"))){
					Util.initComboValue(mainForm.getItem("AREA_ID21"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + table.getSelectedRecord().getAttribute("AREA_ID1") + "'", "");
					mainForm.getItem("AREA_ID21").setValue(table.getSelectedRecord().getAttribute("AREA_ID21"));
				}
				if(ObjUtil.isNotNull(table.getSelectedRecord().getAttribute("AREA_ID2"))){
					Util.initComboValue(mainForm.getItem("AREA_ID22"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + table.getSelectedRecord().getAttribute("AREA_ID2") + "'", "");
					mainForm.getItem("AREA_ID22").setValue(table.getSelectedRecord().getAttribute("AREA_ID22"));
				}
				if(isMax) {
					expend();
				}
			}
			
		});
		table.setShowRowNumbers(true);
		 
		ListGridField ADDR_ID1 = new ListGridField("ADDR_ID1", "起运地ID", 80);
		ADDR_ID1.setHidden(true);
		ListGridField ADDR_ID2 = new ListGridField("ADDR_ID2", "目的地ID", 80);
		ADDR_ID2.setHidden(true);
		ListGridField ADDR_NAME1 = new ListGridField("ADDR_NAME1", "起运地");
		ListGridField ADDR_NAME2 = new ListGridField("ADDR_NAME2", "目的地");
		ListGridField AREA_ID1 = new ListGridField("AREA_ID1", "起运省", 80);
		AREA_ID1.setHidden(true);
		ListGridField AREA_ID2 = new ListGridField("AREA_ID2", "目的省", 80);
		AREA_ID2.setHidden(true);
		ListGridField AREA_ID21 = new ListGridField("AREA_ID21", "起运市ID", 80);
		AREA_ID21.setHidden(true);
		ListGridField AREA_ID22 = new ListGridField("AREA_ID22", "目的市ID", 80);
		AREA_ID22.setHidden(true);
		ListGridField AREA_NAME21 = new ListGridField("AREA_NAME1", "起运城市", 80);
		ListGridField AREA_NAME22 = new ListGridField("AREA_NAME2", "目的城市", 80);
		ListGridField MILEAGE = new ListGridField("MILEAGE", "公里数", 80);
		ListGridField ADDR_FLAG = new ListGridField("ADDR_FLAG", "按地址点", 60);
		ADDR_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(ADDR_ID1, ADDR_ID2, ADDR_NAME1, AREA_ID1, AREA_ID21, 
				AREA_NAME21, ADDR_NAME2, AREA_ID2, AREA_ID22, AREA_NAME22, MILEAGE,ADDR_FLAG);
	}
	
	//创建主信息页签
	@SuppressWarnings("unchecked")
	private SectionStack createHeader(){
		VLayout vLay = new VLayout();
        vLay.setWidth100();
        vLay.setBackgroundColor(ColorUtil.BG_COLOR);
        
        
        SGText ADDR_ID1 = new SGText("ADDR_ID1","ADDR_ID1");
        ADDR_ID1.setVisible(false);
		
		final ComboBoxItem ADDR_NAME1 = new ComboBoxItem("ADDR_NAME1", "起运地");
		ADDR_NAME1.setTitleOrientation(TitleOrientation.TOP);
		ADDR_NAME1.setWidth(250);
		ADDR_NAME1.setColSpan(4);
		ADDR_NAME1.setTitle("起运地");
		ADDR_NAME1.setStartRow(true);
		
		final SGText AREA_NAME21 = new SGText("AREA_NAME1", "起运城市");
		AREA_NAME21.setTitleOrientation(TitleOrientation.TOP);
		AREA_NAME21.setVisible(false);
		
		SGText ADDR_ID2 = new SGText("ADDR_ID2", "ADDR_ID2");
		ADDR_ID2.setVisible(false);
		
		final ComboBoxItem ADDR_NAME2 = new ComboBoxItem("ADDR_NAME2", "目的地");
		ADDR_NAME2.setTitleOrientation(TitleOrientation.TOP);
		ADDR_NAME2.setWidth(250);
		ADDR_NAME2.setColSpan(4);
		ADDR_NAME2.setTitle("目的地");
		ADDR_NAME2.setStartRow(true);
		
		SGCombo AREA_ID1 = new SGCombo("AREA_ID1","起运"+Util.TI18N.PROVINCE());
		SGCombo AREA_ID2 = new SGCombo("AREA_ID2","目的"+Util.TI18N.PROVINCE());
		SGCombo AREA_ID21 = new SGCombo("AREA_ID21","起运"+Util.TI18N.CITY());
		SGCombo AREA_ID22 = new SGCombo("AREA_ID22","目的"+Util.TI18N.CITY());
		
		final SGText AREA_NAME22 = new SGText("AREA_NAME2", "目的城市");
		AREA_NAME22.setTitleOrientation(TitleOrientation.TOP);
		AREA_NAME22.setVisible(false);
		
		ArrayList comboList = new ArrayList();
		comboList.add(AREA_ID1);
		comboList.add(AREA_ID2);
		SGCombo[] combos = (SGCombo[])comboList.toArray(new SGCombo[comboList.size()]);
		Util.initComboValue(combos, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
		
		AREA_ID1.addChangedHandler(new AreaChangeAction((SGText)null,AREA_ID21,AREA_NAME21));
		AREA_ID2.addChangedHandler(new AreaChangeAction((SGText)null,AREA_ID22,AREA_NAME22));
		
		AREA_ID21.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(AREA_NAME21 != null){
					AREA_NAME21.setValue(ObjUtil.ifNull(value, ""));
				}
			}
		});
		
		AREA_ID22.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(AREA_NAME22 != null){
					AREA_NAME22.setValue(ObjUtil.ifNull(value, ""));
				}
			}
		});
		
		initAddrComb(ADDR_NAME1, ADDR_ID1, AREA_NAME21, AREA_ID1, AREA_ID21);
		
		initAddrComb(ADDR_NAME2, ADDR_ID2, AREA_NAME22, AREA_ID2, AREA_ID22);
		
		SGCheck ADDR_FLAG = new SGCheck("ADDR_FLAG", "是否按地址点");
		ADDR_FLAG.setValue(false);
		
		SGText MILEAGE = new SGText("MILEAGE", ColorUtil.getRedTitle("公里数"));
		MILEAGE.setTitleOrientation(TitleOrientation.TOP);
		
        mainForm = new SGPanel();
        mainForm.setItems(ADDR_ID1,ADDR_NAME1,AREA_ID1,AREA_ID21,AREA_NAME21,ADDR_FLAG, ADDR_ID2,ADDR_NAME2,AREA_ID2,AREA_ID22,AREA_NAME22,MILEAGE);
        
        section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SectionStackSection mainS = new SectionStackSection("基础信息");
		mainS.addItem(mainForm);
		mainS.setExpanded(true); 
		section.addSection(mainS);
		
		
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(true);
        
        return section;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.ADDRDIST);
        
        toolStrip.addMember(searchButton);
        
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),sectionStack.getSection(0),vm).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.ADDRDIST_P0_01);
        toolStrip.addMember(newButton);
        newButton.addClickHandler(new NewMultiFormAction(vm, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.ADDRDIST_P0_02);
        toolStrip.addMember(saveButton);
        saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(vm.getItem("MILEAGE")!=null){
					if(ObjUtil.isNotNull(vm.getItem("MILEAGE").getValue())){
						if(!ObjUtil.isNumber(vm.getItem("MILEAGE").getValue())){
							MSGUtil.sayWarning("公里数只能是数字!");
							return;
						}
					}
				}
				if(mainForm.getItem("ADDR_FLAG")!= null && 
						ObjUtil.isNotNull(mainForm.getItem("ADDR_FLAG").getValue())){
					Object addrFlag = mainForm.getItem("ADDR_FLAG").getValue();
					if(Boolean.parseBoolean(addrFlag.toString())){
						if(mainForm.getItem("ADDR_ID1") != null && 
								mainForm.getItem("ADDR_ID2") != null){
							if(!(ObjUtil.isNotNull(mainForm.getItem("ADDR_ID1").getValue()) && 
									ObjUtil.isNotNull(mainForm.getItem("ADDR_ID2").getValue()))){
								MSGUtil.sayWarning("按地址点勾选时, [起运地]和[目的地]不能为空!");
								return;
							}
						}
					}else{
						if(mainForm.getItem("AREA_ID21") != null && 
								mainForm.getItem("AREA_ID22") != null){
							if(!(ObjUtil.isNotNull(mainForm.getItem("AREA_ID21").getValue()) && 
									ObjUtil.isNotNull(mainForm.getItem("AREA_ID22").getValue()))){
								MSGUtil.sayWarning("按地址点不勾选时, [起运市]和[目的市]不能为空!");
								return;
							}
						}
					}
				}
				new SaveMultiFormAction(table, vm, check_map, getThis()).onClick(event);
			}
		});
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.ADDRDIST_P0_03);
        delButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord selRecord = table.getSelectedRecord();
				if(selRecord == null)return;
				String id = selRecord.getAttribute("ID");
				if(!ObjUtil.isNotNull(id))return;
				String sql = "delete from bas_address_dist where id = '"+id+"'";
				Util.async.excuteSQL(sql, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						if(result.startsWith(StaticRef.SUCCESS_CODE)){
							MSGUtil.sayInfo("删除成功!");
							Criteria c = table.getCriteria();
							c = c == null ? new Criteria() : c;
							c.addCriteria("OP_FLAG", "M");
							table.fetchData(c, new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									table.draw();
								}
							});
							
						}else{
							MSGUtil.sayError(result);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						MSGUtil.sayError(caught.getMessage());
						
					}
				});
				
			}
		});
        toolStrip.addMember(delButton);
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.ADDRDIST_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(table, vm,this));
        toolStrip.addMember(canButton);
        
        add_map.put(BasPrivRef.ADDRDIST_P0_01, newButton);
        del_map.put(BasPrivRef.ADDRDIST_P0_03, delButton);
        save_map.put(BasPrivRef.ADDRDIST_P0_02, saveButton);
        save_map.put(BasPrivRef.ADDRDIST_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        toolStrip.setMembersMargin(4);
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
//		check_map.put("ADDR_ID1", StaticRef.CHK_NOTNULL + "起运地");
//		check_map.put("ADDR_ID2", StaticRef.CHK_NOTNULL + "目的地");
		check_map.put("MILEAGE", StaticRef.CHK_NOTNULL + "公里数");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		SGText txt_global = new SGText("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(5);
		txt_global.setVisible(false);
		
		SGText ADDR_ID1 = new SGText("ADDR_ID1","");
		ADDR_ID1.setVisible(false);
		ComboBoxItem ADDR_NAME1 = new ComboBoxItem("ADDR_NAME1","起运地");
//		SGText ADDR_NAME1 = new SGText("ADDR_NAME1","起运地", true);
		ADDR_NAME1.setTitleOrientation(TitleOrientation.TOP);
		initAddrComb(ADDR_NAME1, ADDR_ID1, null,null,null);
		
		TextItem AREA_ID1 = new TextItem("AREA_ID1");
		AREA_ID1.setVisible(false);
		ComboBoxItem AREA_NAME1 = new ComboBoxItem("AREA_NAME1","起运城市");
//		SGText AREA_NAME1 = new SGText("AREA_NAME1","起运城市");
		AREA_NAME1.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(AREA_NAME1, AREA_ID1);
		
		SGText ADDR_ID2 = new SGText("ADDR_ID2","");
		ADDR_ID2.setVisible(false);
		ComboBoxItem ADDR_NAME2 = new ComboBoxItem("ADDR_NAME2","目的地");
//		SGText ADDR_NAME2 = new SGText("ADDR_NAME2","目的地", true);
		ADDR_NAME2.setTitleOrientation(TitleOrientation.TOP);
		ADDR_NAME2.setStartRow(true);
		initAddrComb(ADDR_NAME2, ADDR_ID2, null,null,null);
		
		TextItem AREA_ID2 = new TextItem("AREA_ID2");
		AREA_ID2.setVisible(false);
		ComboBoxItem AREA_NAME2 = new ComboBoxItem("AREA_NAME2","目的城市");
//		SGText AREA_NAME2 = new SGText("AREA_NAME2","目的城市");
		AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(AREA_NAME2, AREA_ID2);
	    
        form.setItems(ADDR_ID1,ADDR_NAME1,AREA_ID1,AREA_NAME1,ADDR_ID2,ADDR_NAME2,AREA_ID2,AREA_NAME2);
        
        return form;
	}
	
	//初始化地址点下拉框
	private void initAddrComb(final ComboBoxItem addr_name,
			final SGText addr_id, final SGText area_name,
			final SGCombo load_area_id,final SGCombo load_area_id2){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField AREA_NAME2 = new ListGridField("AREA_NAME2", Util.TI18N.CITY());
		AREA_NAME2.setHidden(true);
		ListGridField AREA_ID2 = new ListGridField("AREA_ID2", Util.TI18N.CITY()+"ID");
		AREA_ID2.setHidden(true);
		addr_name.setOptionDataSource(ds2);  
		addr_name.setDisabled(false);
		addr_name.setShowDisabled(false);
		addr_name.setDisplayField("FULL_INDEX");
		addr_name.setPickListBaseStyle("myBoxedGridCell");
		addr_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		addr_name.setPickListCriteria(criteria);
		
		addr_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,AREA_NAME2);
		addr_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = addr_name.getSelectedRecord();
				if(selectedRecord != null){
					if (ObjUtil.isNotNull(selectedRecord.getAttribute("ADDR_NAME"))) {
						addr_id.setValue(selectedRecord.getAttribute("ID"));
					}else {
						addr_id.setValue("");
					}
					addr_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					if (load_area_id != null) {
						load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
					}
					if(area_name != null){
						area_name.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					}
					if (load_area_id2 != null) {
						load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
						Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
					}
				}else {
					addr_id.setValue("");
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
		}
	}
	
	private BasAddressDistView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasAddressDistView view = new BasAddressDistView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}

}
