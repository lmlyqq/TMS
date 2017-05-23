package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.RefreshshpmTableAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.tms.ShpmCompDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.RowMouseDownEvent;
import com.smartgwt.client.widgets.grid.events.RowMouseDownHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * @table: TRANS_SHPMENT_HEADER
 * @TABLE: V_SHPMENT_HEADER
 * 运输管理---运输计划--作业单补码管理
 * @author Lang
 *
 */
@ClassForNameAble
public class TmsShipmentCompView1 extends SGForm implements PanelFactory{
	private DataSource mainDS;
	private SGTable headTable;
	private SGTable failureTable;
	private SGTable successTable;
	private SectionStack section;
	private SectionStack fSection;
	private SectionStack sSection;
	private DynamicForm fPageForm;
	private DynamicForm sPageForm;
	private Window searchWin;
	public SGPanel searchForm = new SGPanel();
	private ListGridRecord [] record;
	@SuppressWarnings("unused")
	private Window countWin;
	public IButton saveButton;
	public IButton execButton;
	public IButton expButton;
	public ListGridRecord[] unshpmlstRec;   
	public RefreshshpmTableAction refreshTableAction;
	private SGPanel form;
	private boolean isTrue = false;
	private ListGridRecord[] selecteds;
	
	private TabSet tabSet;
	private int tabNum;
	private ExportAction headExport;
	private ExportAction failureExport;
	private ExportAction successExport;
	private HashMap<String, Integer> idMap;
	private String route_id="";

	/*public TmsShipmentCompView1(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		mainDS = ShpmCompDS.getInstance("V_SHPMCOMP_HEADER", "TRANS_SHIPMENT_HEADER");
		
		tabSet = new TabSet();
		tabSet.setWidth100();
		tabSet.setHeight("100%");
		
		idMap = new HashMap<String, Integer>();
		
		if(isPrivilege(TrsPrivRef.SHPMCODE_P0)){
			headTable = new SGTable(mainDS, "100%", "92%", false, true, false);
			createTable(headTable);
			headTable.addSelectionChangedHandler(new SelectionChangedHandler() {
				
				@Override
				public void onSelectionChanged(SelectionEvent event) {
					if(headTable.getSelection()!=null&&
							headTable.getSelection().length>0){
						if(saveButton.getDisabled()){
							saveButton.enable();
						}
						if(execButton.getDisabled()){
							execButton.enable();
						}
						if(tabSet.getSelectedTabNumber() > 0){
							tabSet.selectTab(0);
						}
					}
				}
			});
			section = createSection(headTable,null,true,true);
			section.setHeight("92%");
			
			Tab tab1=new Tab("作业单补码");
			tab1.setPane(section);
			tab1.setID("headTable_0");
			tabSet.addTab(tab1);
			idMap.put("headTable_0", 0);
		}
		
		if(isPrivilege(TrsPrivRef.SHPMCODE_P1)){
			failureTable = new SGTable(mainDS, "100%", "92%", false, true, false);
			createTable(failureTable);
			setFailureTable();
			fSection = createSection(failureTable,null,true,true);
			fSection.setHeight("92%");
			Tab tab2=new Tab("补码失败");
			tab2.setPane(fSection);
			tab2.setID("failureTable_1");
			tabSet.addTab(tab2);
			idMap.put("failureTable_1", 1);
		}
		
		if(isPrivilege(TrsPrivRef.SHPMCODE_P2)){
			successTable = new SGTable(mainDS, "100%", "92%", false, true, false);
			createTable(successTable);
			sSection = createSection(successTable,null,true,true);
			sSection.setHeight("92%");
			Tab tab3=new Tab("已补码");
			tab3.setPane(sSection);
			tab3.setID("successTable_2");
			tabSet.addTab(tab3);
			idMap.put("successTable_2", 2);
		}
		
		createBtnWidget(toolStrip);
		createForm();
		
		tabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabNum = idMap.get(event.getID());
				if(((isPrivilege(TrsPrivRef.SHPMCODE_P0_03) && tabNum == 0) || 
						(isPrivilege(TrsPrivRef.SHPMCODE_P1_03) && tabNum == 1) || 
						(isPrivilege(TrsPrivRef.SHPMCODE_P2_03) && tabNum == 2))){
					expButton.enable();
				}else{
					expButton.disable();
				}
				if(tabNum == 1){
					failureTable.invalidateCache();
					Criteria criteria = new Criteria();
					criteria.addCriteria("BIZ_TYP", "ADAEE2F25B39487FA778AC78065CE373");
					criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
					criteria.addCriteria("UDF6", "Y");
					criteria.addCriteria("C_ORG_FLAG", "true");
					criteria.addCriteria("OP_FLAG", "M");
					criteria.addCriteria("ABNOMAL_STAT", "10");
					criteria.addCriteria("STATUS_FROM", "20");
					criteria.addCriteria("STATUS_TO", "20");
					
					JavaScriptObject jsobject = fSection.getSection(0).getAttributeAsJavaScriptObject("controls");
					Canvas[] canvas = null;
					if(jsobject != null) {
						canvas = Canvas.convertToCanvasArray(jsobject);
					}
					else {
						canvas = new Canvas[1];
					}
					for(int i = 0; i < canvas.length; i++) {
						if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
							fPageForm = (DynamicForm)canvas[i];
							break;
						}
					}
						
					failureTable.setFilterEditorCriteria(criteria);
					
					failureTable.fetchData(criteria, new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
								MSGUtil.sayError("服务器连接已中断，请重新登录!");
							}
							if(fPageForm != null) {
								fPageForm.getField("CUR_PAGE").setValue("1");
								LoginCache.setPageResult(failureTable, fPageForm.getField("TOTAL_COUNT"), fPageForm.getField("SUM_PAGE"));
							}
						}
					});
				}else if(tabNum == 2){
					successTable.invalidateCache();
					Criteria criteria = new Criteria();
					criteria.addCriteria("BIZ_TYP", "ADAEE2F25B39487FA778AC78065CE373");
					criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
					criteria.addCriteria("UDF6", "S");
					criteria.addCriteria("C_ORG_FLAG", "true");
					criteria.addCriteria("OP_FLAG", "M");
					criteria.addCriteria("STATUS_FROM", "20");
					criteria.addCriteria("STATUS_TO", "20");
					
					JavaScriptObject jsobject = sSection.getSection(0).getAttributeAsJavaScriptObject("controls");
					Canvas[] canvas = null;
					if(jsobject != null) {
						canvas = Canvas.convertToCanvasArray(jsobject);
					}
					else {
						canvas = new Canvas[1];
					}
					for(int i = 0; i < canvas.length; i++) {
						if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
							sPageForm = (DynamicForm)canvas[i];
							break;
						}
					}
						
					successTable.setFilterEditorCriteria(criteria);
					
					successTable.fetchData(criteria, new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
								MSGUtil.sayError("服务器连接已中断，请重新登录!");
							}
							if(sPageForm != null) {
								sPageForm.getField("CUR_PAGE").setValue("1");
								LoginCache.setPageResult(successTable, sPageForm.getField("TOTAL_COUNT"), sPageForm.getField("SUM_PAGE"));
							}
						}
					});
				}
				
			}
		});
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(toolStrip);
//		layout.addMember(section);
		layout.addMember(form);
		layout.addMember(tabSet);

		initVerify();
		return layout;
	}
	
	public void createForm(){
		form = new SGPanel();
		form.setTitleWidth(75);
		form.setVisible(isTrue);
		
		SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		SHPM_NO.setDisabled(true);
		//SHPM_NO.setVisible(false);
		
		SGText CUSTOMER_NAME = new SGText("CUSTOMER_NAME", Util.TI18N.CUSTOMER_CNAME());
		CUSTOMER_NAME.setDisabled(true);
		//CUSTOMER_NAME.setVisible(false);
		
		SGText LOAD_AREA_NAME2 = new SGText("LOAD_AREA_NAME2", "发货区域");
		LOAD_AREA_NAME2.setDisabled(true);
		//LOAD_AREA_NAME2.setVisible(false);
        
		SGText UNLOAD_ADDRESS = new SGText("END_UNLOAD_ADDRESS", "收货地址");
        UNLOAD_ADDRESS.setDisabled(true);
        //UNLOAD_ADDRESS.setVisible(false);
		
        SGText UNLOAD_ID = new SGText("UNLOAD_ID", ColorUtil.getRedTitle(Util.TI18N.UNLOAD_NAME()));
		UNLOAD_ID.setVisible(false);
		
		final ComboBoxItem UNLOAD_NAME = new ComboBoxItem("UNLOAD_NAME", "分点部");
		UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		//UNLOAD_NAME.setWidth(250);
		UNLOAD_NAME.setTitle(ColorUtil.getRedTitle("分点部"));
		//UNLOAD_NAME.setStartRow(true);
		
		SGText UNLOAD_AREA_ID2 = new SGText("UNLOAD_AREA_ID2", "UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		
		SGCheck UDF5 = new SGCheck("UDF5", "直送标记");
		
		SGText ABNOMAL_NOTES = new SGText("ABNOMAL_NOTES", "异常标记");
		ABNOMAL_NOTES.setDisabled(true);
		//ABNOMAL_NOTES.setVisible(false);
		
		form.setItems(SHPM_NO, CUSTOMER_NAME, LOAD_AREA_NAME2, UNLOAD_ADDRESS, UNLOAD_ID, 
				UNLOAD_NAME, UNLOAD_AREA_ID2, UDF5, ABNOMAL_NOTES);
	}
	
	//初始化收货方下拉框
	private void initUnLoadId(final ComboBoxItem load_name, final TextItem load_id, String UNLOAD_AREA_ID2){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		load_name.setOptionDataSource(ds2);  
		load_name.setDisabled(false);
		load_name.setShowDisabled(false);
		load_name.setDisplayField("FULL_INDEX");
		load_name.setPickListBaseStyle("myBoxedGridCell");
		load_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("ADDR_TYP","383297659DAA4C3CB084C9AB93A11F73,9E0057747CC24A4BB998749752D2C42E");
		criteria.addCriteria("AREA_ID2",UNLOAD_AREA_ID2);
		load_name.setPickListCriteria(criteria);
		
		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		load_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_name.getSelectedRecord();
				if(selectedRecord != null){
					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					load_id.setValue(selectedRecord.getAttribute("ID"));
				}
			}
		});
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(16);
		toolStrip.addSeparator();
        
		//查询
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchWin = new SearchWin(mainDS, createSerchForm(searchForm),section.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
				    searchWin.setHeight(400);
//				    if(!(searchForm == null && searchForm.getJsObj() == null)){
//				    	JSOHelper.setAttribute(searchForm.getJsObj(), "parentView", "TmsShipmentCompView"); // 用于当名称为空时清除ID的数据
//				    }
				}else{
					searchWin.show();
				}
			}
		});
		
		//保存
		saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.SHPMCODE_P0_01);
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(getCurrTable().getSelection() == null)
					return;
				if(getCurrTable().getSelection().length > 0){
					updates(false);
				} else {
					MSGUtil.sayWarning("请选择作业单，再执行保存！");
				}
				
			}
		});
		
		execButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.SHPMCODE_P0_02);
		execButton.setTitle("异常标记");
		execButton.addClickHandler(new ClickHandler() {	
			@Override
			public void onClick(ClickEvent event) {
				record = getCurrTable().getSelection();	
				if(ObjUtil.isNotNull(record) && record.length > 0) {
					updates(true);
				} else {
					MSGUtil.sayWarning("请勾选作业单后再执行异常标记！");
				}
			}
		});
		execButton.disable();
		saveButton.disable();
		
		expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.SHPMCODE_P0_03);
		expButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(tabNum == 0){
					if(headExport == null)
						headExport = new ExportAction(getCurrTable());
					headExport.onClick(event);
				}else if(tabNum == 1){
					if(failureExport == null)
						failureExport = new ExportAction(getCurrTable());
					failureExport.onClick(event);
				}else if(tabNum ==2){
					if(successExport == null)
						successExport = new ExportAction(getCurrTable());
					successExport.onClick(event);
				}
			}
		});
		
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton, saveButton, execButton, expButton);

	}
	
	private void createTable(final SGTable table) {
//		failureTable = new SGTable(mainDS, "100%", "92%", false, true, false);
		table.setCanExpandRecords(false);
		table.setCanEdit(!isTrue);
		if(!isTrue){ 
			table.setEditEvent(ListGridEditEvent.CLICK);
		}else{
			table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
				
				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {
					fillData(event.getRecord());
				}
			});
		}
		
        
        ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO());
        SHPM_NO.setCanEdit(!isTrue);
        SHPM_NO.setWidth(150);
        
//        ListGridField ODR_NO = new ListGridField("ODR_NO",Util.TI18N.ODR_NO());
//        ODR_NO.setCanEdit(!isTrue);
//        ODR_NO.setHidden(true);
        
        ListGridField REFENENCE1 = new ListGridField("REFENENCE1",Util.TI18N.REFENENCE1());
        REFENENCE1.setCanEdit(!isTrue);
        REFENENCE1.setWidth(100);
        
        ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", Util.TI18N.CUSTOMER_NAME());
        CUSTOMER_NAME.setCanEdit(!isTrue);
        CUSTOMER_NAME.setWidth(180);
//        CUSTOMER_NAME.setHidden(true);
        
        ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2", "发货区域");
        LOAD_AREA_NAME2.setCanEdit(!isTrue);
        LOAD_AREA_NAME2.setWidth(80);
        
        ListGridField UNLOAD_ADDRESS = new ListGridField("END_UNLOAD_ADDRESS", "收货地址");
        UNLOAD_ADDRESS.setCanEdit(!isTrue);
		
		ListGridField UNLOAD_ID = new ListGridField("UNLOAD_ID", ColorUtil.getRedTitle(Util.TI18N.UNLOAD_NAME()),280);
		UNLOAD_ID.setCanEdit(!isTrue);
		UNLOAD_ID.setHidden(true);
		
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", "分点部");
		UNLOAD_NAME.setWidth(300);
		UNLOAD_NAME.setCanEdit(!isTrue);
		
		ListGridField UNLOAD_AREA_ID2 = new ListGridField("UNLOAD_AREA_ID2", "UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setHidden(!isTrue);
		
		
		UNLOAD_NAME.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				String unloadName = table.getSelectedRecord().getAttribute("UNLOAD_NAME");
				Object obj = event.getNewValue();
				final int rowNum = event.getRowNum();
				if(obj != null) {
					final Record rec = event.getRecord();
					if(obj.equals("") || obj.toString().equals(unloadName)){
						table.setEditValue(rowNum, "UNLOAD_ID", rec.getAttribute("UNLOAD_ID"));
						table.setEditValue(rowNum, "UNLOAD_NAME", rec.getAttribute("UNLOAD_NAME"));
					}else{
						String curValue = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
						Util.async.queryData("select id,addr_name from BAS_ADDRESS where id='"+
								curValue+"' or upper(addr_code)=upper('"+curValue+
								"') or upper(hint_code)=upper('"+curValue+
								"')", false, new AsyncCallback<Map<String,Object>>() {
							@Override
							public void onFailure(Throwable caught) {
								
							}
							@SuppressWarnings("unchecked")
							@Override
							public void onSuccess(Map<String, Object> result) {
								List<List<String>> list = (List<List<String>>)result.get("data");
								if(list == null || list.isEmpty()){
									SC.say("未能找到指定的收货方!");
									return;
								}
								rec.setAttribute("UNLOAD_ID", list.get(0).get(0));
								table.setEditValue(rowNum, "UNLOAD_ID", list.get(0).get(0));
								rec.setAttribute("UNLOAD_NAME", list.get(0).get(1));
								table.setEditValue(rowNum, "UNLOAD_NAME", list.get(0).get(1));
							}
						});
					}
					
				}
			}
		});
		
		ListGridField UDF5 = new ListGridField("UDF5", "直送标记",100);
		UDF5.setType(ListGridFieldType.BOOLEAN);
		UDF5.setCanEdit(true);
		UDF5.setHidden(true);
		UDF5.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				if(table.getSelection()!=null&&
						table.getSelection().length>0){
					table.setEditValue(event.getRowNum(), "UDF5", "true".equals(event.getItem().getValue().toString())?true:false);
					table.getRecord(event.getRowNum()).setAttribute("UDF5", "true".equals(event.getItem().getValue().toString())?true:false);
				}
				
			}
		});
		
		ListGridField ABNOMAL_NOTES = new ListGridField("ABNOMAL_NOTES", "异常原因",80);
		ABNOMAL_NOTES.setCanEdit(false);
		ABNOMAL_NOTES.setHidden(true);
		
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT", "异常标记",60);
		ABNOMAL_STAT.setCanEdit(false);
		ABNOMAL_STAT.setHidden(true);
		
		table.setFields(SHPM_NO,REFENENCE1,CUSTOMER_NAME,LOAD_AREA_NAME2,UNLOAD_ADDRESS, UNLOAD_ID,UNLOAD_NAME,UDF5,UNLOAD_AREA_ID2,ABNOMAL_STAT,ABNOMAL_NOTES);
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(table.getSelection()!=null&&
						table.getSelection().length>0){
					if(saveButton.getDisabled() && 
							((isPrivilege(TrsPrivRef.SHPMCODE_P0_01) && tabNum == 0) || 
							(isPrivilege(TrsPrivRef.SHPMCODE_P1_01) && tabNum == 1) || 
							(isPrivilege(TrsPrivRef.SHPMCODE_P2_01) && tabNum == 2))){
						saveButton.enable();
					}
					if(execButton.getDisabled() && 
							((isPrivilege(TrsPrivRef.SHPMCODE_P0_02) && tabNum == 0) || 
							(isPrivilege(TrsPrivRef.SHPMCODE_P1_02) && tabNum == 1) || 
							(isPrivilege(TrsPrivRef.SHPMCODE_P2_02) && tabNum == 2))){
						execButton.enable();
					}
				}
			}
		});
		
		table.addRowMouseDownHandler(new RowMouseDownHandler() {
			
			@Override
			public void onRowMouseDown(RowMouseDownEvent event) {
				if(event.getRecord() != null){
					initComboBoxItem(event.getRecord(), table);
				}
			}
		});
		
		table.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(selecteds != null){
					table.selectRecords(selecteds, true);
					selecteds = null;
				}
			}
		});
	    
	}
	
	private void initComboBoxItem(Record r, final SGTable table){
		String UNLOAD_AREA_ID2 = r.getAttribute("UNLOAD_AREA_ID2");
		ListGridField field = table.getField("UNLOAD_NAME");
		final ComboBoxItem unload_name = new ComboBoxItem();
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		unload_name.setOptionDataSource(ds2);  
		unload_name.setDisabled(false);
		unload_name.setShowDisabled(false);
		unload_name.setDisplayField("FULL_INDEX");
		unload_name.setPickListBaseStyle("myBoxedGridCell");
		unload_name.setPickListWidth(450);
		unload_name.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				FormItem item  = event.getItem();
				Record editRecord = table.getSelectedRecord();
				if(item != null && editRecord != null){
					editRecord.setAttribute("UNLOAD_ID", event.getValue());
				}
			}
		});
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("ADDR_TYP","383297659DAA4C3CB084C9AB93A11F73,9E0057747CC24A4BB998749752D2C42E");
		criteria.addCriteria("AREA_ID2",UNLOAD_AREA_ID2);
		unload_name.setPickListCriteria(criteria);
		unload_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		
		field.setEditorType(unload_name);
		selecteds = table.getSelection();
	}
	
	public void fillData(Record r){
		if(r == null) return;
		for (FormItem item : form.getFields()) {
			if("UDF5".equals(item.getName())){
				item.setValue(r.getAttributeAsBoolean(item.getName()));
				continue;
			}
			item.setValue(r.getAttribute(item.getName()));
		}
		initUnLoadId((ComboBoxItem)form.getField("UNLOAD_NAME"), 
				(TextItem)form.getField("UNLOAD_ID"), r.getAttribute("UNLOAD_AREA_ID2"));
	}

	public DynamicForm createSerchForm(final SGPanel form){
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(8);
		form.setHeight100();
		form.setWidth100();
//		form.setCellPadding(2);
		

		//1
//		SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER_NAME(),true);
//		Util.initComboValue(CUSTOMER_NAME, "BAS_CUSTOMER", "ID", "CUSTOMER_CNAME", " where CUSTOMER_FLAG='Y'");
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
		
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//2
	
		final TextItem LOAD_AREA_ID = new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID.setVisible(false);
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem ("LOAD_AREA_NAME",Util.TI18N.LOAD_AREA_NAME());
//		LOAD_AREA_NAME.setStartRow(true);
		Util.initArea(LOAD_AREA_NAME, LOAD_AREA_ID);
//		LOAD_AREA_NAME.setColSpan(2);
		LOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
//		Util.initComboValue(LOAD_AREA_NAME, "BAS_AREA", "ID", "AREA_CNAME");
		
		final TextItem UNLOAD_AREA_ID = new TextItem("UNLOAD_AREA_ID");
		UNLOAD_AREA_ID.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME());
//		Util.initComboValue(UNLOAD_AREA_NAME, "BAS_AREA", "ID", "AREA_CNAME");
//		UNLOAD_AREA_NAME.setColSpan(2);
		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);
		UNLOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		UNLOAD_AREA_NAME.setVisible(false);
		
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
		Util.initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE","ID","SRVC_NAME");
		TRANS_SRVC_ID.setVisible(false);
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initComboValue(SUPLR_NAME, "BAS_SUPPLIER", "ID", "SUPLR_CNAME");
		SUPLR_NAME.setVisible(false);
		
		//3
		SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME());//发货方
		LOAD_NAME.setStartRow(true);
		LOAD_NAME.setVisible(false);
		
		SGText LOAD_ID = new SGText("LOAD_ID", Util.TI18N.LOAD_NAME_ID());//发货方代码
		LOAD_ID.setVisible(false);
		
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		
		SGText UNLOAD_ID = new SGText("UNLOAD_ID", Util.TI18N.UNLOAD_NAME_ID());//收货方代码
		
		SGCombo STATUS_FROM = new SGCombo("STATUS_FROM",Util.TI18N.SHPM_STSTUS()+" 从");
		STATUS_FROM.setColSpan(2);
		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT, "20");
		SGCombo STATUS_TO = new SGCombo("STATUS_TO","到");//wangjun 2011-4-6
		STATUS_TO.setColSpan(2);
		Util.initStatus(STATUS_TO, StaticRef.SHPMNO_STAT, "20");
		
		SGCombo ASSIGN_STAT_NAME = new SGCombo("ASSIGN_STAT_NAME",Util.TI18N.ASSIGN_STAT());
//		Util.initCodesComboValue(ASSIGN_STAT_NAME, "ASSIGN_STAT");
		Util.initStatus(ASSIGN_STAT_NAME, StaticRef.ASSIGN_STAT,"");
		ASSIGN_STAT_NAME.setVisible(false);
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());	//业务类型
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP", "ADAEE2F25B39487FA778AC78065CE373");
		
		final SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//线路名称
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME");
		
		SGCombo TRANS_COND = new SGCombo("REFENENCE4",Util.TI18N.TRANS_COND());
        Util.initCodesComboValue(TRANS_COND, "TRANS_COND");
		
        SGCombo UDF6 = new SGCombo("UDF6", "补码状态");	//未补码
        LinkedHashMap<String, String> udf6ValueMap = new LinkedHashMap<String, String>();
        udf6ValueMap.put("Y|S", "  ");
        udf6ValueMap.put("S", "已补码");
        udf6ValueMap.put("Y", "未补码");
		UDF6.setValue("Y");
		UDF6.setValueMap(udf6ValueMap);
		
		SGCombo BUK_FLAG = new SGCombo("BUK_FLAG", "SSS处理标识");
//		BUK_FLAG.setColSpan(2);
		LinkedHashMap<String, String> bukValueMap = new LinkedHashMap<String, String>();
		bukValueMap.put("", "  ");
		bukValueMap.put("Y", "已处理");
		bukValueMap.put("N", "未处理");
		BUK_FLAG.setValueMap(bukValueMap);
		
		final SGCheck NULLROUTE_FLAG=new SGCheck("NULLROUTE_FLAG", "空线路");// 空线路
		NULLROUTE_FLAG.setValue(false);
//		NULLROUTE_FLAG.setColSpan(2);
		
		ROUTE_ID.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(ROUTE_ID.getValue())){
					NULLROUTE_FLAG.setValue(false);
				}
			}
		});
		NULLROUTE_FLAG.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(ROUTE_ID.getValue())){
					route_id=ROUTE_ID.getValue().toString();
				}
				if(ObjUtil.isNotNull(NULLROUTE_FLAG.getValue()) && "TRUE".equalsIgnoreCase(NULLROUTE_FLAG.getValue().toString())){
					ROUTE_ID.setValue("");
				}else{
					ROUTE_ID.setValue(route_id);
				}
			}
		});
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setWidth(120);
		C_ORG_FLAG.setValue(true);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构
		C_RDC_FLAG.setColSpan(2);
		
		final TextItem EMPTY_FLAG = new TextItem("EMPTY_FLAG", "");
		EMPTY_FLAG.setValue("Y");
//		EMPTY_FLAG.setDefaultValue("Y");
		EMPTY_FLAG.setVisible(false);
		TextItem SHPM_FLAG = new TextItem("SHPM_FLAG","");
		SHPM_FLAG.setValue("Y");
		SHPM_FLAG.setVisible(false);
		
//		SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGText ODR_TIME_FROM = new SGText("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());	//订单时间
		SGText ODR_TIME_TO = new SGText("ODR_TIME_TO", "到");
		Util.initDateTime(searchForm,ODR_TIME_FROM);
		Util.initDateTime(searchForm,ODR_TIME_TO);
		
		SGText ADDTIME_FROM=new SGText("ADDTIME_FROM",Util.TI18N.ORD_ADDTIME_FROM());
		SGText ADDTIME_TO=new SGText("ADDTIME_TO","到");
		Util.initDateTime(searchForm, ADDTIME_FROM);
		Util.initDateTime(searchForm, ADDTIME_TO);
		
        SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());//托运单号
        SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
      
		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,SHPM_NO,REFENENCE1,CUSTOM_ODR_NO,ODR_NO,TRANS_COND,TRANS_SRVC_ID,SUPLR_NAME,
				      LOAD_NAME,LOAD_ID,UNLOAD_NAME,UNLOAD_ID,
				      STATUS_FROM,STATUS_TO,ASSIGN_STAT_NAME,ODR_TIME_FROM,ODR_TIME_TO,BIZ_TYP, 
				      LOAD_AREA_NAME,LOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID,EMPTY_FLAG,SHPM_FLAG,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG, 
				      ADDTIME_FROM,ADDTIME_TO,ROUTE_ID,NULLROUTE_FLAG,UDF6,BUK_FLAG,C_RDC_FLAG);
		
		return form;
		
	}
	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public void initVerify() {
	  check_map.put("TABLE", "TRANS_SHPMENT_HEADER");

	}

	@Override
	public void onDestroy() {
		if(searchWin != null){
			searchWin.destroy();
		}
	}
	
	private boolean checkPass(){
		int[] edits = getCurrTable().getAllEditRows();
		if(edits.length > 0){
			for (int i : edits) {
				ListGridRecord record = getCurrTable().getRecord(i);
				if(StaticRef.SHPM_DIPATCH.equals(record.getAttribute("STATUS"))){
					return false;
				}
			}
		}
		return true;
	}
	
	
	private ArrayList<String> getSqls(boolean execFlag){
		ArrayList<String> sqls = new ArrayList<String>();
		int[] edits = getCurrTable().getAllEditRows();
		if(edits.length > 0) {
			String firstUnloadId = null;
			String firstUnloadName = null;
			Boolean firstUdf5 = null;
			String startStr = "update trans_shipment_header set";
			StringBuffer sb = null;
			for (int i : edits) {
				ListGridRecord record = getCurrTable().getRecord(i);
				firstUnloadId = record.getAttribute("UNLOAD_ID");
				firstUnloadName = record.getAttribute("UNLOAD_NAME");
				firstUdf5 = record.getAttributeAsBoolean("UDF5");
				if((!ObjUtil.isNotNull(firstUnloadId) || 
						"X".equalsIgnoreCase(firstUnloadName)) && !execFlag){
					continue;
				}
				sb = new StringBuffer(startStr.intern());
				if(execFlag){
					sb.append(" ABNOMAL_NOTES = '02无法补码'");
				}else{
					sb.append(" UNLOAD_ID = '");
					sb.append(firstUnloadId);
					sb.append("'");
					sb.append(", UNLOAD_NAME = '");
					sb.append(firstUnloadName);
					sb.append("'");
					sb.append(", UDF6 = 'S', UDF5 = '");
					sb.append((firstUdf5?"Y":"N"));
					sb.append("'");
				}
				sb.append(" where SHPM_NO = '");
				sb.append(record.getAttribute("SHPM_NO"));
				sb.append("'");
				sb.append(" and (select count(1) from bas_address where id = '");
				sb.append(firstUnloadId);
				sb.append("') > 0");
				sqls.add(sb.toString());
			}
		}
		else {
			String firstUnloadId = null;
			String firstUnloadName = null;
			Boolean firstUdf5 = null;
			String startStr = "update trans_shipment_header set";
			StringBuffer sb = null;
			ListGridRecord[] records = getCurrTable().getSelection();
			for (int i = 0; i < records.length; i++) {
				ListGridRecord record = records[i];
				firstUnloadId = record.getAttribute("UNLOAD_ID");
				firstUnloadName = record.getAttribute("UNLOAD_NAME");
				firstUdf5 = record.getAttributeAsBoolean("UDF5");
				if((!ObjUtil.isNotNull(firstUnloadId) || 
						"X".equalsIgnoreCase(firstUnloadName)) && !execFlag){
					continue;
				}
				sb = new StringBuffer(startStr.intern());
				if(execFlag){
					sb.append(" ABNOMAL_NOTES = '02无法补码'");
				}else{
					sb.append(" UDF6 = 'S', UDF5 = '");
					sb.append((firstUdf5?"Y":"N"));
					sb.append("'");
				}
				sb.append(" where SHPM_NO = '");
				sb.append(record.getAttribute("SHPM_NO"));
				sb.append("'");
				sb.append(" and (select count(1) from bas_address where id = '");
				sb.append(firstUnloadId);
				sb.append("') > 0");
				sqls.add(sb.toString());
			}
		}
		return sqls;
	}

	private void updates(boolean execFlag){
		if(!checkPass()){
			MSGUtil.sayWarning("不能修改已调度的作业单!");
			return;
		}
		Util.async.excuteSQLList(getSqls(execFlag), new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(!result.equals(StaticRef.SUCCESS_CODE)) {
					MSGUtil.sayError(result);
					return;
				}
				MSGUtil.showOperSuccess();
				
				HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单 
				HashMap<String, String> unload_map = new HashMap<String, String>(); //收货方 
				HashMap<String, String> udf5_map = new HashMap<String, String>(); //直送
				HashMap<String, Object> listmap = new HashMap<String, Object>();
				int[] edits = getCurrTable().getAllEditRows();
				if(edits.length > 0) {
					for(int i = 0; i < edits.length; i++) {
						ListGridRecord record = getCurrTable().getRecord(edits[i]);
						String unload_id = record.getAttribute("UNLOAD_ID");
						String unload_name = record.getAttribute("UNLOAD_NAME");
						if(ObjUtil.isNotNull(unload_id) && !"X".equalsIgnoreCase(unload_name)) {
							shpm_map.put(Integer.toString(i+1), record.getAttribute("SHPM_NO"));
							if(isTrue){
								unload_map.put(Integer.toString(i+1), form.getItem("UNLOAD_ID").getValue().toString());
								udf5_map.put(Integer.toString(i+1), form.getItem("UDF5").getValue().toString());
							}else{
								unload_map.put(Integer.toString(i+1), record.getAttribute("UNLOAD_ID"));
								udf5_map.put(Integer.toString(i+1), record.getAttribute("UDF5"));
							}
						}
					}
					listmap.put("1", shpm_map);
					listmap.put("2", unload_map);
					listmap.put("3", udf5_map);
					listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
					String json = Util.mapToJson(listmap);
					Util.async.execProcedure(json, "SP_SHPM_MODIFY(?,?,?,?,?)", new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}

						@Override
						public void onSuccess(String result) {
							if(result.equals(StaticRef.SUCCESS_CODE)) {
								RefreshRecord();
								MSGUtil.sayInfo("补码成功!");
							}else{
								MSGUtil.sayError(result);
							}
						}
						
					});
				}
				else {
					ListGridRecord[] records = getCurrTable().getSelection();
					for(int i = 0; i < records.length; i++) {
						ListGridRecord record = records[i];
						String unload_id = record.getAttribute("UNLOAD_ID");
						String unload_name = record.getAttribute("UNLOAD_NAME");
						if(ObjUtil.isNotNull(unload_id) && !"X".equalsIgnoreCase(unload_name)) {
							shpm_map.put(Integer.toString(i+1), record.getAttribute("SHPM_NO"));
							if(isTrue){
								unload_map.put(Integer.toString(i+1), form.getItem("UNLOAD_ID").getValue().toString());
								udf5_map.put(Integer.toString(i+1), form.getItem("UDF5").getValue().toString());
							}else{
								unload_map.put(Integer.toString(i+1), record.getAttribute("UNLOAD_ID"));
								udf5_map.put(Integer.toString(i+1), record.getAttribute("UDF5"));
							}
						}
					}
					listmap.put("1", shpm_map);
					listmap.put("2", unload_map);
					listmap.put("3", udf5_map);
					listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
					if(shpm_map.keySet().size() > 0) {
						String json = Util.mapToJson(listmap);
						Util.async.execProcedure(json, "SP_SHPM_MODIFY(?,?,?,?,?)", new AsyncCallback<String>() {
	
							@Override
							public void onFailure(Throwable caught) {
								MSGUtil.sayError(caught.getMessage());
							}
	
							@Override
							public void onSuccess(String result) {
								if(result.equals(StaticRef.SUCCESS_CODE)) {
									RefreshRecord();
									MSGUtil.sayInfo("补码成功!");
								}else{
									MSGUtil.sayError(result);
								}
							}
							
						});
					}
				}
				
			}
			@Override
			public void onFailure(Throwable caught) {
				MSGUtil.showOperError();
			}
		});
	}
	
	public void RefreshRecord(){
		getCurrTable().discardAllEdits();
		getCurrTable().invalidateCache();
		Criteria c = getCurrTable().getCriteria();
		JavaScriptObject jsobject = getCurrSection().getSection(0).getAttributeAsJavaScriptObject("controls");
		Canvas[] canvas = null;
		if(jsobject != null) {
			canvas = Canvas.convertToCanvasArray(jsobject);
		}
		else {
			canvas = new Canvas[1];
		}
		for(int i = 0; i < canvas.length; i++) {
			if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
				sPageForm = (DynamicForm)canvas[i];
				break;
			}
		}
		
		getCurrTable().setFilterEditorCriteria(c);
		
		getCurrTable().fetchData(c, new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
				if(sPageForm != null) {
					sPageForm.getField("CUR_PAGE").setValue("1");
					LoginCache.setPageResult(getCurrTable(), sPageForm.getField("TOTAL_COUNT"), sPageForm.getField("SUM_PAGE"));
				}
			}
		});
//		int[] edits = getCurrTable().getAllEditRows();
//	    if(edits.length > 0) {
//			RecordList list = getCurrTable().getRecordList();
//			//ListGridRecord[] selectedList = headTable.getSelection();
//			RecordList newList = new  RecordList();
//			for(int i = 0; i < list.getLength(); i++) {
//				boolean flag = false;
//				for(int j = 0; j < edits.length; j++) {
//					ListGridRecord record = getCurrTable().getRecord(edits[j]);
//					if(list.get(i).getAttribute("ID").equals(record.getAttribute("ID"))){
//						if(!"X".equalsIgnoreCase(record.getAttribute("UNLOAD_NAME"))){
//							flag = true;
//							break;
//						}
//					}
//		            	
//				}
//				if(!flag){
//					newList.add(list.get(i));
//				}
//			}
//			
//			getCurrTable().setData(newList);
//	    }
//	    else {
//	    	RecordList list = getCurrTable().getRecordList();
//			ListGridRecord[] selectedList = getCurrTable().getSelection();
//			RecordList newList = new  RecordList();
//			for(int i = 0; i < list.getLength(); i++) {
//				boolean flag = false;
//				for (ListGridRecord listGridRecord : selectedList) {
//					if(list.get(i).getAttribute("ID").equals(listGridRecord.getAttribute("ID"))){
//						if(!"X".equalsIgnoreCase(listGridRecord.getAttribute("UNLOAD_NAME"))){
//							flag = true;
//							break;
//						}
//					}
//		            	
//				}
//				if(!flag){
//					newList.add(list.get(i));
//				}
//			}
//			
//			getCurrTable().setData(newList);
//	    }
	}
	
	private SGTable getCurrTable(){
		if(tabNum == 0)
			return headTable;
		else if(tabNum == 1)
			return failureTable;
		else if(tabNum == 2)
			return successTable;
		else
			return headTable;
	}
	
	private SectionStack getCurrSection(){
		if(tabNum == 0)
			return section;
		else if(tabNum == 1)
			return fSection;
		else if(tabNum == 2)
			return sSection;
		else
			return section;
	}
	
	private void setFailureTable(){
		ListGridField UNLOAD_NAME = failureTable.getField("UNLOAD_NAME");
		ListGridField ABNOMAL_STAT = failureTable.getField("ABNOMAL_STAT");
		ListGridField ABNOMAL_NOTES = failureTable.getField("ABNOMAL_NOTES");
		if(!(UNLOAD_NAME == null || 
				ABNOMAL_STAT == null ||
				ABNOMAL_NOTES == null)){
			UNLOAD_NAME.setWidth(260);
			ABNOMAL_STAT.setHidden(false);
			ABNOMAL_NOTES.setHidden(false);
		}
	}
	
	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TmsShipmentCompView1 view = new TmsShipmentCompView1();
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
