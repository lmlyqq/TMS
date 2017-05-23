package com.rd.client.view.base;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.action.base.address.DeleteAddrAction;
import com.rd.client.action.base.address.SaveAddrAction;
import com.rd.client.action.base.address.SaveSFAddrAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewMultiFormAction;
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
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.base.AddrDSSF;
import com.rd.client.ds.base.AddrTmsDSSF;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
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
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
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

/**
 * 顺丰网点管理
 * @author xuweibin
 *
 */
@ClassForNameAble
public class SFAddressView extends SGForm implements PanelFactory {
	
	private ValuesManager vm;
	private DataSource ds;
	private DataSource tmsds;
	private SGTable table;
	private SGTable tmsTable;
	private Record selectRecord;
	private DynamicForm mainForm;
	public DynamicForm tmsForm;
	private DynamicForm tmsForm2;
	private SectionStack sectionStack;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm=new SGPanel();
//	private HashMap<String,IButton>new_map2;
	private HashMap<String,IButton>del_map2;
	private HashMap<String,IButton> save_map2;
	private HashMap<String,IButton> can_map2;
	private Record record=new Record();
//	private Record tmsRecord=new Record();
	private SGText UNIT_CODE;
	private ComboBoxItem LOAD_NAME;
	public String OP_FLAG="A";
	private HashMap<String,String> check_map2;
	private Window uploadWin;
	private DynamicForm pageForm;
	
	/*public SFAddressView(String id){
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj=LoginCache.getUserPrivilege().get(getFUNCID());
		check_map2=new HashMap<String,String>();
		vm=new ValuesManager();
		VLayout main=new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip=new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds=AddrDSSF.getInstance("SSS_ADDR");
		tmsds=AddrTmsDSSF.getInstance("TRANS_SSS_ADDR");
		tmsTable=new SGTable(tmsds,"100%","100%",false,true,false);
		
		//主布局
		HStack stack=new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table=new SGTable(ds,"100%","100%",false,true,false);
		table.setShowHover(true);
		createListFields();
		table.setCanEdit(false);
//		table.setSelectionType(SelectionStyle.SINGLE);
		sectionStack=new SectionStack();
		final SectionStackSection listItem=new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		listItem.setControls(new SGPage(table,true).initPageBtn());
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		addSplitBar(stack);
		
		//STACK的右边布局
		TabSet leftTabSet=new TabSet();
		leftTabSet.setWidth("80%");
		leftTabSet.setHeight("100%");
		leftTabSet.setMargin(0);
		leftTabSet.setVisible(false);
		if(isPrivilege(BasPrivRef.SFADDR_P1)){
			Tab tab1=new Tab(Util.TI18N.MAININFO());
			tab1.setPane(createHeader());
			leftTabSet.addTab(tab1);
			
		}
		if(isPrivilege(BasPrivRef.SFADDR_P2)){
			Tab tab2=new Tab("冷运网点信息");
			tab2.setPane(createContralInfo());
			leftTabSet.addTab(tab2);
		}
		
		stack.addMember(leftTabSet);
		
		vm.addMember(mainForm);
		vm.setDataSource(ds);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		initVerify();
		
		table.addDoubleClickHandler(new DoubleClickHandler(){

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
			
				Util.initComboValue(mainForm.getItem("AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME","parent_area_id= '"+selectRecord.getAttribute("AREA_ID")+"'");
				Util.initComboValue(mainForm.getItem("AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME","parent_area_id= '"+selectRecord.getAttribute("AREA_ID2")+"'");
				if(isMax){
					expend();
				}
			}
			
		});
		return main;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);
		
		IButton searchButton=createBtn(StaticRef.FETCH_BTN,BasPrivRef.SFADDR);
		toolStrip.addMember(searchButton);
		searchButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin==null){
					searchWin=new SearchWin(ds,createSearchForm(searchForm),sectionStack.getSection(0),vm).getViewPanel();
				}else{
					searchWin.show();
				}
			}
		});
		
		IButton newButton=createBtn(StaticRef.CREATE_BTN,BasPrivRef.SFADDR_P0_01);
		toolStrip.addMember(newButton);
		newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));
		
		final IButton refresh = createUDFBtn("刷新",StaticRef.ICON_REFRESH);
	    refresh.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				table.discardAllEdits();
				table.invalidateCache();
				Criteria criteria;
				if(searchForm != null){
					criteria = searchForm.getValuesAsCriteria();
				}else{
					criteria = new Criteria();
				}
				if(criteria.getValues().isEmpty()){
					criteria.addCriteria("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
					criteria.addCriteria("ENABLE_FLAG", true);
					criteria.addCriteria("C_ORG_FLAG", true);
					criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
				}
				criteria.addCriteria("OP_FLAG","M");
				table.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						if(pageForm != null) {
							pageForm.getField("CUR_PAGE").setValue("1");
							LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
						}
						table.setSelectOnEdit(true);
						
						if(table.getRecords().length > 0){
							table.selectRecord(0);
						}
					}
				});
			}
		});
		
		// 导入按钮
		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.SFADDR_P2_06);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					uploadWin = new UploadFileWin(refresh, "sfaddress_tpl.xls").getViewPanel("SFADDRESS");
				}else{
					uploadWin.show();
				}
			}
		});
		toolStrip.addMember(inputButton);
		
		IButton saveButton=createBtn(StaticRef.SAVE_BTN,BasPrivRef.SFADDR_P0_02);
		toolStrip.addMember(saveButton);
		saveButton.addClickHandler(new SaveSFAddrAction(table,vm,check_map,mainForm,this));
		
		IButton delButton=createBtn(StaticRef.DELETE_BTN,BasPrivRef.SFADDR_P0_03);
		delButton.addClickHandler(new DeleteAddrAction(table,vm));
		toolStrip.addMember(delButton);
		
		IButton canButton=createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SFADDR_P0_04);
		canButton.addClickHandler(new CancelMultiFormAction(table,vm,this));
		toolStrip.addMember(canButton);
		
//		IButton impButton=createBtn(StaticRef.IMPORT_BTN,BasPrivRef.SFADDR_P0_06);
//		toolStrip.addMember(impButton);
//		impButton.addClickHandler(null);
		
		IButton expButton=createBtn(StaticRef.EXPORT_BTN,BasPrivRef.SFADDR_P0_05);
		toolStrip.addMember(expButton);
		expButton.addClickHandler(new ExportAction(table));
		
		add_map.put(BasPrivRef.SFADDR_P0_01, newButton);
		del_map.put(BasPrivRef.SFADDR_P0_03, delButton);
		save_map.put(BasPrivRef.SFADDR_P0_02, saveButton);
		save_map.put(BasPrivRef.SFADDR_P0_04, canButton);
		this.enableOrDisables(add_map, true);
		this.enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
		toolStrip.setMembersMargin(4);
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	

	//创建主信息页签
	private SectionStack createHeader() {
		VLayout vlay=new VLayout();
		vlay.setWidth100();
		vlay.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SGText ADDR_CODE=new SGText("ADDR_CODE",Util.TI18N.ADDR_CODE());
		ADDR_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.ADDR_CODE()));
		
		SGText ADDR_NAME=new SGText("ADDR_NAME",ColorUtil.getRedTitle(Util.TI18N.ADDR_NAME()),true);
		SGText HINT_CODE=new SGText("HINT_CODE",Util.TI18N.HINT_CODE());
		ADDR_NAME.addBlurHandler(new GetHintAction(ADDR_NAME, HINT_CODE));
		
		SGCheck ENABLE=new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		ENABLE.setDefaultValue(true);
		
		SGCombo AREA_ID=new SGCombo("AREA_ID",Util.TI18N.PROVINCE());
		AREA_ID.setTitleOrientation(TitleOrientation.TOP);
		AREA_ID.setStartRow(true);
		Util.initComboValue(AREA_ID, "BAS_AREA", "AREA_CODE", "AREA_CNAME","AREA_LEVEL= '3' ","");
		SGText AREA_NAME=new SGText("AREA_NAME",Util.TI18N.PROVINCE());
		AREA_NAME.setVisible(false);
		SGText AREA_NAME2=new SGText("AREA_NAME2", Util.TI18N.CITY());
		AREA_NAME2.setVisible(false);
		final SGText AREA_NAME3=new SGText("AREA_NAME3", Util.TI18N.AREA());
		AREA_NAME3.setVisible(false);
		SGCombo AREA_ID2=new SGCombo("AREA_ID2",Util.TI18N.CITY());
		SGCombo AREA_ID3=new SGCombo("AREA_ID3",Util.TI18N.AREA());
		AREA_ID3.setColSpan(8);
		AREA_ID.addChangedHandler(new AreaChangeAction(AREA_NAME, AREA_ID2, AREA_NAME2,AREA_ID3));
		AREA_ID2.addChangedHandler(new AreaChangeAction(AREA_NAME2, AREA_ID3, AREA_NAME3));
		AREA_ID3.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem=(SGCombo)event.getSource();
				String value=fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value)&&AREA_NAME3!=null){
					AREA_NAME3.setValue(value);
				}
			}
		});
		SGText ADDRESS=new SGText("ADDRESS",ColorUtil.getRedTitle(Util.TI18N.ADDRESS()),true);
		ADDRESS.setWidth(260);
		ADDRESS.setColSpan(8);
		
		SGCombo ADDR_TYP=new SGCombo("ADDR_TYPE",Util.TI18N.ADDR_TYP(),true);
		Util.initCodesComboValue(ADDR_TYP, "SFADDR_TYP");
		
		TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID","");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME=new TextItem("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(120);
		EXEC_ORG_ID_NAME.setColSpan(10);
		
		SGText LEVEL1_CODE=new SGText("LEVEL1_CODE","经营本部");
		LEVEL1_CODE.setWidth(120);
		SGText LEVEL2_CODE=new SGText("LEVEL2_CODE","区部");
		LEVEL2_CODE.setWidth(120);
		SGText LEVEL3_CODE=new SGText("LEVEL3_CODE","分点部");
		LEVEL3_CODE.setWidth(120);
		
//		TextAreaItem NOTES=new TextAreaItem("NOTES",Util.TI18N.NOTES());
//		NOTES.setStartRow(true);
//		NOTES.setColSpan(8);
//		NOTES.setHeight(30);
//		NOTES.setWidth(520);
//		NOTES.setTitleOrientation(TitleOrientation.TOP);
//		NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		mainForm=new SGPanel();
		mainForm.setItems(ADDR_CODE,ENABLE,ADDR_NAME,HINT_CODE,AREA_ID,AREA_ID2,AREA_ID3,AREA_NAME,
				AREA_NAME2,AREA_NAME3,LEVEL1_CODE,LEVEL2_CODE,LEVEL3_CODE,ADDRESS,ADDR_TYP,EXEC_ORG_ID,EXEC_ORG_ID_NAME);
		
		section=new SectionStack();
		SectionStackSection mainS=new SectionStackSection("基础信息");
		mainS.addItem(mainForm);
		mainS.setExpanded(true);
		
		section.addSection(mainS);
		
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);
		
		return section;
	}
	
	//第二个页签
	private VLayout createContralInfo() {
		VLayout vlay=new VLayout();
		
		ListGridField TMS_ADDR_CODE=new ListGridField("TMS_ADDR_CODE","冷运网点代码",140);
		ListGridField TMS_ADDR_NAME=new ListGridField("TMS_ADDR_NAME","冷运网点",140);
		ListGridField SF_UNIT_CODE=new ListGridField("UNIT_CODE","单元区域",140);
		SF_UNIT_CODE.setCanEdit(true);
		tmsTable.setFields(SF_UNIT_CODE,TMS_ADDR_NAME,TMS_ADDR_CODE);
		
		UNIT_CODE=new SGText("UNIT_CODE",ColorUtil.getRedTitle("单元区域"));
//		UNIT_CODE.setDisabled(true);
		
		LOAD_NAME=new ComboBoxItem("LOAD_NAME",ColorUtil.getRedTitle("冷运网点"));
		LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_NAME.setColSpan(4);
		LOAD_NAME.setWidth(250);
		final TextItem LOAD_ID = new TextItem("LOAD_ID");
		LOAD_ID.setVisible(false);
		LOAD_NAME.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if(LOAD_NAME.getDisplayValue().length()==0){
					LOAD_ID.setValue("");
				}
			}
		});
		
		SGText ADDR_ID=new SGText("ADDR_ID","代码ID");
		ADDR_ID.setVisible(false);
		
		SGLText LOAD_ADDRESS = new SGLText("LOAD_ADDRESS", ColorUtil.getRedTitle(Util.TI18N.LOAD_ADDRESS()),true);
		SGCombo AREA_ID = new SGCombo("LOAD_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.PROVINCE()));
		SGText AREA_NAME = new SGText("LOAD_AREA_NAME",Util.TI18N.PROVINCE());
		SGCombo AREA_ID2 = new SGCombo("LOAD_AREA_ID2",ColorUtil.getRedTitle(Util.TI18N.CITY()));
		AREA_ID2.setVisible(false);
		SGText AREA_NAME2 = new SGText("LOAD_AREA_NAME2",Util.TI18N.CITY());
		SGCombo AREA_ID3 = new SGCombo("LOAD_AREA_ID3",Util.TI18N.AREA());
		SGText AREA_NAME3 = new SGText("LOAD_AREA_NAME3",Util.TI18N.AREA());
		SGText LOAD_CONTACT = new SGText("LOAD_CONTACT", ColorUtil.getRedTitle(Util.TI18N.CONT_NAME()));
		SGText LOAD_TEL = new SGText("LOAD_TEL", ColorUtil.getRedTitle(Util.TI18N.CONT_TEL()));
		SGCombo LOAD_REGION = new SGCombo("LOAD_REGION",Util.TI18N.LOAD_REGION());
		
		tmsForm2=new SGPanel();
		tmsForm2.setFields(LOAD_ADDRESS, AREA_ID, AREA_NAME, AREA_NAME2, AREA_ID3, AREA_NAME3,LOAD_CONTACT, 
				LOAD_TEL,LOAD_REGION);
		tmsForm2.setVisible(false);
		
		initLoadId(LOAD_NAME, LOAD_ADDRESS, LOAD_ID, AREA_ID, AREA_NAME, AREA_ID2, AREA_NAME2,
				AREA_ID3, AREA_NAME3,LOAD_CONTACT, LOAD_TEL,LOAD_REGION,ADDR_ID);
		ToolStrip toolStrip=new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight(20);
		
//		IButton newButton=createBtn(StaticRef.CREATE_BTN,BasPrivRef.SFADDR_P2_01);
//		toolStrip.addMember(newButton);
//		toolStrip.setMembersMargin(4);
//		newButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				UNIT_CODE.setDisabled(false);
//				LOAD_NAME.setDisabled(false);
//				enableOrDisables(new_map2, false);
//				enableOrDisables(del_map2, false);
//				enableOrDisables(save_map2, true);
//				enableOrDisables(can_map2, true);
//			}
//		});
		
		IButton saveButton=createBtn(StaticRef.SAVE_BTN,BasPrivRef.SFADDR_P2_02);
//		saveButton.setTitle("加入");
		toolStrip.addMember(saveButton);
//		saveButton.addClickHandler(new SaveAddrAction(table,tmsTable,LOAD_NAME,LOAD_ID,ADDR_ID,UNIT_CODE,this));
		saveButton.addClickHandler(new SaveAddrAction(table,tmsTable,check_map2,this));
		
		IButton delButton=createBtn(StaticRef.DELETE_BTN,BasPrivRef.SFADDR_P2_03);
		toolStrip.addMember(delButton);
		delButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SC.confirm(Util.MI18N.DELETE_CONFIRM(), new BooleanCallback() {
					public void execute(Boolean value) {
	                    if (value != null && value) {
	                    	ListGridRecord record1=table.getSelectedRecord();
	        				final ListGridRecord record2=tmsTable.getSelectedRecord();
	        				StringBuffer sf=new StringBuffer();
	        				sf.append("delete from TRANS_SSS_ADDR");
	        				sf.append(" where SF_ADDR_CODE='"+record1.getAttribute("ADDR_CODE")+"' and " +
	        						"TMS_ADDR_CODE='"+record2.getAttribute("TMS_ADDR_CODE")+"' and UNIT_CODE='"+record2.getAttribute("UNIT_CODE")+"'");
	        				Util.async.excuteSQL(sf.toString(), new AsyncCallback<String>() {
	        					@Override
	        					public void onSuccess(String result) {
	        						if (result.equals(StaticRef.SUCCESS_CODE)) {
	        							MSGUtil.showOperSuccess();
	        							freshTmsTable();
	        							record2.setAttribute("LOAD_ID", "");
	        						}
	        					}
	        					@Override
	        					public void onFailure(Throwable caught) {
	        					}
	        				});
	        				initBtn2();
	                    }
	                }
	            });
			}
		});
		
		IButton canButton=createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SFADDR_P2_04);
		toolStrip.addMember(canButton);
		canButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				initBtn2();
			}
		});
		tmsTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				enableOrDisables(del_map2, true);
			}
		});
		tmsTable.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(tmsTable.getRecords().length>0){
					OP_FLAG="M";
					UNIT_CODE.disable();
					LOAD_NAME.disable();
					enableOrDisables(can_map2, true);
				}
			}
		});
		
//		new_map2=new HashMap<String,IButton>();
		del_map2=new HashMap<String,IButton>();
		save_map2=new HashMap<String,IButton>();
		can_map2=new HashMap<String,IButton>();
		
//		new_map2.put(BasPrivRef.SFADDR_P0_01, newButton);
		del_map2.put(BasPrivRef.SFADDR_P0_03, delButton);
		save_map2.put(BasPrivRef.SFADDR_P0_02, saveButton);
		can_map2.put(BasPrivRef.SFADDR_P0_04, canButton);
		
		initBtn2();
		tmsForm=new SGPanel();
		tmsForm.setItems(UNIT_CODE,LOAD_NAME,LOAD_ID,ADDR_ID,AREA_ID2);
		
		vlay.addMember(tmsTable);
		vlay.addMember(tmsForm);
		vlay.addMember(tmsForm2);
		vlay.addMember(toolStrip);
		return vlay;
	}
	
	private void initBtn2(){
//		enableOrDisables(new_map2, true);
		enableOrDisables(del_map2, false);
//		enableOrDisables(save_map2, false);
		enableOrDisables(can_map2, false);
		OP_FLAG="A";
		tmsTable.discardAllEdits();
		LOAD_NAME.setDisabled(false);
		UNIT_CODE.setDisabled(false);
	}
	public void initBtn3(){
//		enableOrDisables(new_map2, true);
		enableOrDisables(del_map2, false);
//		enableOrDisables(save_map2, false);
		enableOrDisables(can_map2, false);
		OP_FLAG="A";
		LOAD_NAME.setDisabled(false);
		UNIT_CODE.setDisabled(false);
	}

	private void createListFields() {
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				initBtn2();
				record=event.getRecord();
			
				selectRecord=event.getRecord();
				vm.editRecord(selectRecord);
				vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				initSaveBtn();
				
				LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
				if(ObjUtil.isNotNull(selectRecord.getAttribute("ADDR_CODE"))){
					map=new LinkedHashMap<String,String>();
					map.put(selectRecord.getAttribute("ADDR_CODE"), selectRecord.getAttribute("ADDR_CODE"));
					mainForm.getItem("ADDR_CODE").setValueMap(map);
					mainForm.getItem("ADDR_CODE").setValue(selectRecord.getAttribute("ADDR_CODE"));
				}else{
					mainForm.getItem("ADDR_CODE").setDefaultValue("");
					mainForm.getItem("ADDR_CODE").setValueMap("");
				}
				
				Util.initComboValue(mainForm.getItem("AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME","parent_area_id= '"+selectRecord.getAttribute("AREA_ID")+"'");
				Util.initComboValue(mainForm.getItem("AREA_ID3"), "BAS_AREA", "AREA_CODE", "AREA_CNAME","parent_area_id= '"+selectRecord.getAttribute("AREA_ID2")+"'");
				
				freshTmsTable();
			}
		});
		table.setShowRowNumbers(true);
		
		
		ListGridField ADDR_CODE=new ListGridField("ADDR_CODE",Util.TI18N.ADDR_CODE(),80);
		ADDR_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.ADDR_CODE()));
		ListGridField ADDR_NAME=new ListGridField("ADDR_NAME",Util.TI18N.ADDR_NAME(),120);
		ADDR_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.ADDR_NAME()));
		ListGridField ADDRESS=new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),140);
		ADDRESS.setTitle(ColorUtil.getRedTitle(Util.TI18N.ADDRESS()));
		
		ListGridField AREA_NAME=new ListGridField("AREA_NAME",Util.TI18N.PROVINCE());
		ListGridField AREA_ID2 = new ListGridField("AREA_ID2");
		AREA_ID2.setHidden(true);
		ListGridField AREA_NAME2=new ListGridField("AREA_NAME2",Util.TI18N.CITY());
		ListGridField AREA_NAME3=new ListGridField("AREA_NAME3",Util.TI18N.AREA());
		
		ListGridField ADDR_TYPE=new ListGridField("NAME_C",Util.TI18N.ADDR_TYP(),80);
		
		ListGridField LEVEL1_CODE=new ListGridField("LEVEL1_CODE",Util.TI18N.LEVEL1_CODE());
		ListGridField LEVEL2_CODE=new ListGridField("LEVEL2_CODE",Util.TI18N.LEVEL2_CODE());
		ListGridField LEVEL3_CODE=new ListGridField("LEVEL3_CODE",Util.TI18N.LEVEL3_CODE());
		
		ListGridField EXEC_ORG_ID=new ListGridField("ORG_CNAME",Util.TI18N.EXEC_ORG_ID(),80);
		ListGridField ENABLE_FLAG=new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),60);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(ADDR_CODE,ADDR_NAME,ADDRESS,AREA_NAME,AREA_ID2,AREA_NAME2,AREA_NAME3,ADDR_TYPE,
				LEVEL1_CODE,LEVEL2_CODE,LEVEL3_CODE,EXEC_ORG_ID,ENABLE_FLAG);
		
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "SSS_ADDR");
		check_map.put("ADDR_CODE", StaticRef.CHK_NOTNULL+Util.TI18N.ADDR_CODE());
		check_map.put("ADDR_CODE", StaticRef.CHK_UNIQUE+Util.TI18N.ADDR_CODE());
		check_map.put("ADDR_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.ADDR_NAME());
		check_map.put("ADDRESS", StaticRef.CHK_NOTNULL+Util.TI18N.ADDRESS());
		
		check_map2.put("TABLE", "TRANS_SSS_ADDR");
		check_map2.put("UNIT_CODE,TMS_ADDR_CODE", StaticRef.CHK_UNIQUE+"单元区域,冷运网点代码");
	}

	@Override
	public void onDestroy() {
		if(searchWin!=null){
			searchForm.destroy();
			searchWin.destroy();
		}
		
	}
	private DynamicForm createSearchForm(DynamicForm form) {
		SGText ADDR_CODE=new SGText("ADDR_CODE",Util.TI18N.ADDR_CODE());
		ADDR_CODE.setTitleOrientation(TitleOrientation.TOP);
		SGText ADDR_NAME=new SGText("ADDR_NAME",Util.TI18N.ADDR_NAME());
		ADDR_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		SGCheck ENABLE_FLAG=new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		ENABLE_FLAG.setValue(true);
		
//		ComboBoxItem AREA_ID3=new ComboBoxItem("AREA_ID_NAME3",Util.TI18N.AREA_ID());
//		AREA_ID3.setTitleOrientation(TitleOrientation.TOP);
//		Util.initArea(AREA_ID3, null);
//		AREA_ID3.setWidth(120);
		
		TextItem AREA_ID2=new TextItem("AREA_ID2");
		AREA_ID2.setVisible(false);
		ComboBoxItem AREA_NAME2=new ComboBoxItem("AREA_NAME2","所在城市");
		AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(AREA_NAME2, AREA_ID2);
		AREA_NAME2.setWidth(120);
		
		TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID","");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME=new TextItem("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setWidth(130);
		EXEC_ORG_ID_NAME.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(120);
		EXEC_ORG_ID_NAME.setStartRow(true);
		EXEC_ORG_ID_NAME.setColSpan(2);
		
		SGText LEVEL1_CODE=new SGText("LEVEL1_CODE","经营本部");
		SGText LEVEL2_CODE=new SGText("LEVEL2_CODE","区部");
		SGText LEVEL3_CODE=new SGText("LEVEL3_CODE","分点部");
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setValue(true);
		
		form.setItems(ADDR_CODE,ADDR_NAME,AREA_ID2,AREA_NAME2,ENABLE_FLAG,LEVEL1_CODE,
				LEVEL2_CODE,LEVEL3_CODE,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		form.setCellPadding(4);
		form.setCellSpacing(4);
		
		return form;
	}
	
	private void initLoadId(final ComboBoxItem load_name,final SGLText address,final TextItem load_id
			,final SGCombo load_area_id,final TextItem load_area_name
			,final SGCombo load_area_id2,final TextItem load_area_name2
			,final SGCombo load_area_id3,final TextItem load_area_name3
			,final SGText cont_name,final SGText cont_tel,final SGCombo load_region,final SGText addr_id){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		ListGridField AREA_ID2=new ListGridField("AREA_ID2");
		AREA_ID2.setHidden(true);
		load_name.setOptionDataSource(ds2);  
		load_name.setDisabled(false);
		load_name.setShowDisabled(false);
		load_name.setDisplayField("FULL_INDEX");  
		load_name.setPickListBaseStyle("myBoxedGridCell");
		load_name.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		criteria.addCriteria("CUSTOMER_ID",vm.getValueAsString("CUSTOMER_ID"));
		criteria.addCriteria("AND_LOAD_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("VDIRECT","SFAddress");
		criteria.addCriteria("DEF_ORG_ID",LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		load_name.setPickListCriteria(criteria);
		
		load_name.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS,AREA_ID2);
		load_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_name.getSelectedRecord();
				if(selectedRecord != null){
					Util.initComboValue(load_area_id2, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID") + "'", "");
					Util.initComboValue(load_area_id3, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					Util.initComboValue(load_region, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("AREA_ID2") + "'", "");
					load_name.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					load_area_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					load_area_id.setValue(selectedRecord.getAttribute("AREA_ID"));
					load_area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					load_area_id2.setValue(selectedRecord.getAttribute("AREA_ID2"));
					load_area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					load_area_id3.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_region.setValue(selectedRecord.getAttribute("AREA_ID3"));
					load_id.setValue(selectedRecord.getAttribute("ADDR_CODE"));
					cont_name.setValue(selectedRecord.getAttribute("CONT_NAME"));
					cont_tel.setValue(selectedRecord.getAttribute("CONT_TEL"));
					addr_id.setValue(selectedRecord.getAttribute("ID"));
					vm.setValue("WHSE_ID", selectedRecord.getAttribute("WHSE_ID"));
				}
			}
		});
	}
	public void freshTmsTable(){
		Criteria crit=new Criteria();
		tmsTable.invalidateCache();
		crit.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		crit.addCriteria("VDIRECT", "SF");
		crit.addCriteria("ADDR_CODE",record.getAttribute("ADDR_CODE"));
		crit.addCriteria("ADDR_NAME",record.getAttribute("ADDR_NAME"));
		crit.addCriteria("AREA_NAME",record.getAttribute("AREA_NAME2"));
		tmsTable.fetchData(crit,new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				LoginCache.setPageResult(tmsTable, new FormItem(), new FormItem());
			}
		});
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		SFAddressView view = new SFAddressView();
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
