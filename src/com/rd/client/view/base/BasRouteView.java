package com.rd.client.view.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.action.base.address.AreaChangeAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteMultiFormAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
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
import com.rd.client.ds.base.RouteDetailDS;
import com.rd.client.ds.base.RouteHeaderDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 基础资料->线路管理
 * @author yuanlei
 *
 */
@ClassForNameAble
public class BasRouteView extends SGForm implements PanelFactory {

	private DataSource mainDS;
	private DataSource detailDS;
	private SGTable headTable;
	private SGTable detailTable;
	private Window searchWin = null;
	private SGPanel searchForm;
	private String route_id;
	private SGPanel mainForm;
	private SGPanel mainForm2;
	private SectionStack section;
	public ValuesManager vm;
	private  SectionStack lst_section;
	
	/*public BasRouteView(String id) {
		super(id);
	}*/
	
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		//初始化主界面、按钮布局、Section布局
		VLayout main = new VLayout();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		mainDS = RouteHeaderDS.getInstance("V_ROUTE_HEAD", "BAS_ROUTE_HEAD");
		detailDS = RouteDetailDS.getInstance("V_ROUTE_DETAIL","BAS_ROUTE_DETAIL");
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//创建表格和数据源		
		createTable();
		
		//创建Section
		lst_section = new SectionStack();
		final SectionStackSection lst_listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		lst_listItem.setItems(headTable);
		lst_listItem.setExpanded(true);
//		lst_listItem.setControls(addMaxBtn(lst_section, stack, "200" , true), new SGPage(headTable, true).initPageBtn());
		lst_listItem.setControls(new SGPage(headTable, true).initPageBtn());
		lst_section.addSection(lst_listItem);
	    lst_section.setWidth("100%");
		stack.addMember(lst_section);
		addSplitBar(stack);
		
		//STACK的右边布局 
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
        
        if(isPrivilege(BasPrivRef.ROUTE_P1)) {
	        
        	Tab tab1 = new Tab(Util.TI18N.MAININFO());
			//FORM布局
			tab1.setPane(createMainInfo());
	        leftTabSet.addTab(tab1);
        }
        
        stack.addMember(leftTabSet);
        
		//创建按钮布局
		createBtnWidget(toolStrip);
        
		vm.addMember(mainForm);
		vm.addMember(mainForm2);
		vm.setDataSource(mainDS);
		
        main.setWidth100();
        main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(stack);
		
		headTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				Record selectedRecord  = event.getRecord();
				Util.initComboValue(mainForm.getItem("START_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("START_AREA_ID") + "'", "");

				Util.initComboValue(mainForm.getItem("END_AREA_ID2"), "BAS_AREA", "AREA_CODE", "AREA_CNAME", " parent_area_id = '" + selectedRecord.getAttribute("END_AREA_ID") + "'", "");
				
				initAddBtn();
				
				if(isMax) {
					expend();
				}
			}
			
		});
		
		initVerify();
		
		final Menu menu = new Menu();
		menu.setWidth(140);
		menu.setVisible(true);
		
		MenuItem searchItem = new  MenuItem(StaticRef.FETCH_BTN,"Ctrl+Q");
		KeyIdentifier searchKey = new KeyIdentifier();
		searchKey.setCtrlKey(true);
		searchKey.setKeyName("Q");
		searchItem.setKeys(searchKey);
		menu.setItems(searchItem);
		
		return main;
	}
	
	private void createTable() {
		headTable = new SGTable(mainDS, "100%", "92%", true, true, false) {
			public DataSource getRelatedDataSource(ListGridRecord record) {
				detailDS = RouteDetailDS.getInstance("V_ROUTE_DETAIL", "BAS_ROUTE_DETAIL");
				
				route_id = record.getAttributeAsString("ID");
                return detailDS;   
            }
			protected Canvas getExpansionComponent(final ListGridRecord record) {    
				  
                VLayout layout = new VLayout();   
  
                detailTable = new SGTable(getRelatedDataSource(record),"100%","50",false,true,false) {
        			@Override  
                    protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {   
                        if (colNum == 1) {   
                            return "font-weight:bold; color:#FF0000;";   
                        } 
                        else {   
                            return super.getCellCSSText(record, rowNum, colNum);   
                        }  
                    } 
                };      
                detailTable.setCanEdit(false);

                detailTable.setAlign(Alignment.RIGHT);
                detailTable.setShowRowNumbers(false);
                detailTable.setAutoFitData(Autofit.VERTICAL);
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("ROUTE_ID", route_id);
		        
		        
        		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),40);
        		ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.ROUTE_AREA(), 70);
        		//ListGridField START_AREA_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_ARAE(),70);
        		//ListGridField END_AREA_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA(),70);
        		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANSPORT_SERVICE(),80);
        		ListGridField ADDR_ID_NAME = new ListGridField("ADDR_ID_NAME",Util.TI18N.ROUTE_ADDR(),120);
        		ListGridField ADDRESS = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),150);
        		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);
        		ListGridField DISTENCE = new ListGridField("DISTENCE",Util.TI18N.ROUTE_DISTENCE(),80);
        		ListGridField RUNTIME = new ListGridField("RUNTIME",Util.TI18N.RUNTIME(),80);
        		ListGridField MAINWAY = new ListGridField("MAINWAY",Util.TI18N.MAINWAY(),100);
        		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),150);
  
        		detailTable.setFields(SHOW_SEQ,AREA_NAME, ADDR_ID_NAME, ADDRESS
        				, EXEC_ORG_ID_NAME, TRANS_SRVC_ID_NAME, DISTENCE, RUNTIME, MAINWAY, NOTES);
        		detailTable.setDrawAheadRatio(8.0f);
        		detailTable.fetchData(findValues);
                layout.addMember(detailTable);
                layout.setLayoutLeftMargin(5);
                
                return layout;   
            }   
		};
		headTable.setCanExpandRecords(true);
		headTable.setCanEdit(false);
		headTable.setShowFilterEditor(false);
		headTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	OP_FLAG = "M";
                vm.editRecord(selectedRecord);
                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                
                //enableOrDisables(del_map, true);
                LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
        		if(ObjUtil.isNotNull(selectedRecord.getAttribute("START_AREA_ID2"))) {
        			map = new LinkedHashMap<String,String>();
        			map.put(selectedRecord.getAttribute("START_AREA_ID2"), selectedRecord.getAttribute("START_AREA_NAME2"));
        			mainForm.getItem("START_AREA_ID2").setValueMap(map);
        			mainForm.getItem("START_AREA_ID2").setValue(selectedRecord.getAttribute("START_AREA_ID2"));
        		}
        		else {
        			mainForm.getItem("START_AREA_ID2").setDefaultValue("");
        			mainForm.getItem("START_AREA_ID2").setValueMap("");
        		}
        		if(ObjUtil.isNotNull(selectedRecord.getAttribute("END_AREA_ID2"))) {
        			map = new LinkedHashMap<String,String>();
        			map.put(selectedRecord.getAttribute("END_AREA_ID2"), selectedRecord.getAttribute("END_AREA_NAME2"));
        			mainForm.getItem("END_AREA_ID2").setValueMap(map);
        			mainForm.getItem("END_AREA_ID2").setValue(selectedRecord.getAttribute("END_AREA_ID2"));
        		}
        		else {
        			mainForm.getItem("END_AREA_ID2").setDefaultValue("");
        			mainForm.getItem("END_AREA_ID2").setValueMap("");
        		}
        		//System.out.println("ENABLE_FLAG: "+selectedRecord.getAttribute("ENABLE_FLAG"));
                initSaveBtn();
            }
        });
		headTable.addCellClickHandler(new CellClickHandler() {
			@Override
			public void onCellClick(CellClickEvent event) {
				if(event.getRecord() != null) {
					ListGridRecord record = event.getRecord();
					
					cache_map = new HashMap<String, String>();
					cache_map.put("ROUTE_ID", record.getAttributeAsString("ID"));
					cache_map.put("TRANS_SRVC_ID", record.getAttributeAsString("TRANS_SRVC_ID"));
					cache_map.put("START_AREA_ID", record.getAttributeAsString("START_AREA_ID"));
					cache_map.put("START_AREA_NAME", record.getAttributeAsString("START_AREA_NAME"));
					cache_map.put("END_AREA_ID", record.getAttributeAsString("END_AREA_ID"));
					cache_map.put("END_AREA_NAME", record.getAttributeAsString("END_AREA_NAME"));
					cache_map.put("START_AREA_ID2", record.getAttributeAsString("START_AREA_ID2"));
					cache_map.put("START_AREA_NAME2", record.getAttributeAsString("START_AREA_NAME2"));
					cache_map.put("END_AREA_ID2", record.getAttributeAsString("END_AREA_ID2"));
					cache_map.put("END_AREA_NAME2", record.getAttributeAsString("END_AREA_NAME2"));
					cache_map.put("EXEC_ORG_ID", record.getAttributeAsString("EXEC_ORG_ID"));
				}
			}
			
		});
		
		//创建主列表
		getConfigList();
	}
	
	//创建主信息页签
	@SuppressWarnings("unchecked")
	private SectionStack createMainInfo(){
        //1
        final SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", "");
        TRANS_SRVC_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRANSPORT_SERVICE()));
        Util.initTrsService(TRANS_SRVC_ID, "");
        
        final SGText CUSTOMER_ID=new SGText("CUSTOMER_ID","");
		CUSTOMER_ID.setVisible(false);
		//CUSTOMER_ID.setColSpan(2);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		//CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
        
        SGCheck ENABLE = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG()); 
        
        
        //2
        /**
        TextItem START_AREA_ID = new TextItem("START_AREA_ID", "");
        START_AREA_ID.setVisible(false);
        
        final ComboBoxItem START_AREA_ID_NAME = new ComboBoxItem("START_AREA_NAME", "");
        START_AREA_ID_NAME.setWidth(120);
        START_AREA_ID_NAME.setColSpan(2);
        START_AREA_ID_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.START_ARAE()));
        START_AREA_ID_NAME.setStartRow(true);
        START_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
        Util.initArea(START_AREA_ID_NAME, START_AREA_ID);
        */
        SGCombo AREA_ID = new SGCombo("START_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.START_ARAE())+"<br />"+Util.TI18N.PROVINCE());
        AREA_ID.setTitleOrientation(TitleOrientation.TOP);
        //AREA_ID.setColSpan(2);
        AREA_ID.setStartRow(true);
		SGCombo AREA_ID2 = new SGCombo("START_AREA_ID2","<br />"+Util.TI18N.CITY());
		AREA_ID2.setTitleOrientation(TitleOrientation.TOP);
		SGText AREA_NAME = new SGText("START_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.START_ARAE())+"<br />"+Util.TI18N.PROVINCE());
        AREA_NAME.setVisible(false);
        final SGText AREA_NAME2 = new SGText("START_AREA_NAME2","<br />"+Util.TI18N.CITY());
        AREA_NAME2.setVisible(false);
        AREA_ID.addChangedHandler(new AreaChangeAction(AREA_NAME,AREA_ID2,AREA_NAME2));
        AREA_ID2.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME2 != null){
					AREA_NAME2.setValue(value);
				}
			}
        	
        });
        
        SGCombo AREA_ID4 = new SGCombo("END_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.END_AREA())+"<br />"+Util.TI18N.PROVINCE());
        AREA_ID4.setTitleOrientation(TitleOrientation.TOP);
		SGCombo AREA_ID5 = new SGCombo("END_AREA_ID2","<br />"+Util.TI18N.CITY());
		AREA_ID5.setTitleOrientation(TitleOrientation.TOP);
		SGText AREA_NAME4 = new SGText("END_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.END_AREA())+"<br />"+Util.TI18N.PROVINCE());
        AREA_NAME4.setVisible(false);
        final SGText AREA_NAME5 = new SGText("END_AREA_NAME2","<br />"+Util.TI18N.CITY());
        AREA_NAME5.setVisible(false);
        
		ArrayList comboList = new ArrayList();
		comboList.add(AREA_ID);
		comboList.add(AREA_ID4);
		SGCombo[] combos = (SGCombo[])comboList.toArray(new SGCombo[comboList.size()]);
		Util.initComboValue(combos, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
        AREA_ID4.addChangedHandler(new AreaChangeAction(AREA_NAME4,AREA_ID5,AREA_NAME5));
        AREA_ID5.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME5 != null){
					AREA_NAME5.setValue(value);
				}
			}
        	
        });
        
        /**
        TextItem END_AREA_ID = new TextItem("END_AREA_ID", "");
        END_AREA_ID.setVisible(false);
        final ComboBoxItem END_AREA_ID_NAME = new ComboBoxItem("END_AREA_NAME", "");
        END_AREA_ID_NAME.setWidth(120);
        END_AREA_ID_NAME.setColSpan(2);
        END_AREA_ID_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.END_AREA()));
        END_AREA_ID_NAME.setAlign(Alignment.LEFT);
        END_AREA_ID_NAME.setTitleAlign(Alignment.LEFT);
        END_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
        Util.initArea(END_AREA_ID_NAME, END_AREA_ID);
        */

        //3
        final SGLText ROUTE_NAME = new SGLText("ROUTE_NAME", "",true);
        ROUTE_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.ROUTE_NAME()));
        //ROUTE_NAME.setWidth(268);
        
        //4
        final SGText SHORT_NAME = new SGText("SHORT_NAME",ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
       // SHORT_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
        
        final SGText HINT_CODE = new SGText("HINT_CODE",Util.TI18N.HINT_CODE());
       // HINT_CODE.setTitle(ColorUtil.getBlueTitle(Util.TI18N.HINT_CODE()));
        
        HINT_CODE.setAlign(Alignment.LEFT);
        HINT_CODE.setTitleAlign(Alignment.LEFT);
        SHORT_NAME.addBlurHandler(new GetHintAction(SHORT_NAME, HINT_CODE));
        PickerIcon clearPicker = new PickerIcon(PickerIcon.REFRESH, new FormItemClickHandler() {   
            public void onFormItemClick(FormItemIconClickEvent event) {
            	if(AREA_NAME2.getDisplayValue() != null && AREA_NAME5.getDisplayValue() != null && TRANS_SRVC_ID.getValue() != null) {
            		String trans_srvc = TRANS_SRVC_ID.getDisplayValue();
            		String start_area = AREA_NAME2.getDisplayValue();
            		String end_area = AREA_NAME5.getDisplayValue();
            		//ROUTE_NAME.setValue("【" + trans_srvc + "】:【" + start_area + "】-->【" + end_area + "】");  
            		ROUTE_NAME.setValue("【" + start_area + "】-->【" + end_area + "】(" + trans_srvc + ")"); 
            		SHORT_NAME.focusInItem();
            		SHORT_NAME.setValue(ROUTE_NAME.getDisplayValue());
            		HINT_CODE.focusInItem();
            	}
            }   
        });
        ROUTE_NAME.setIcons(clearPicker);
        
        mainForm = new SGPanel();
        mainForm.setWidth("40%");
        mainForm.setItems(TRANS_SRVC_ID,CUSTOMER_ID, CUSTOMER_NAME, ENABLE,AREA_ID, AREA_NAME,AREA_ID2,AREA_NAME2, 
        		AREA_ID4,AREA_NAME4,AREA_ID5,AREA_NAME5,ROUTE_NAME, SHORT_NAME,HINT_CODE);
        
        //5
        SGText EXEC_ORG_ID = new SGText("EXEC_ORG_ID", "");
        EXEC_ORG_ID.setVisible(false);
        //EXEC_ORG_ID.setColSpan(2);
        SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
        Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, true, "50%", "40%");
        SGText SHOW_SEQ = new SGText("SHOW_SEQ", Util.TI18N.SHOW_SEQ());
        SHOW_SEQ.setTitleOrientation(TitleOrientation.TOP);
        
        //6
        
		SGText START_DATE = new SGText("START_DATE",Util.TI18N.START_DATE(), true);
		SGText END_DATE = new SGText("END_DATE",Util.TI18N.END_DATE());
		Util.initDate(vm,START_DATE);
		Util.initDate(vm,END_DATE);
        
		//7
		SGCombo TIME_UNIT = new SGCombo("TIME_UNIT", Util.TI18N.TIME_UNIT(), true);
		SGText OTD_TIME = new SGText("OTD_TIME",Util.TI18N.OTD_TIME());
		//OTD_TIME.setColSpan(2);
		OTD_TIME.setTitleOrientation(TitleOrientation.TOP);
        Util.initComboValue(TIME_UNIT, "BAS_MSRMNT_UNIT", "UNIT", "UNIT_NAME", "MSRMNT_CODE = 'TIME'", " SHOW_SEQ");
        
        //8
		SGText TOTL_DISTANCE = new SGText("TOTL_DISTANCE", Util.TI18N.TOTL_DISTANCE());
		//TOTL_DISTANCE.setColSpan(2);
		TOTL_DISTANCE.setTitleOrientation(TitleOrientation.TOP);
		
		//8
		SGText TOTAL_AMOUNT = new SGText("TOTAL_AMOUNT", Util.TI18N.TOTAL_AMOUNT());	//总金额
		//TOTAL_AMOUNT.setColSpan(2);
		TOTAL_AMOUNT.setDefaultValue(0);
		TOTAL_AMOUNT.setTitleOrientation(TitleOrientation.TOP);
		
		SGCheck POINTS_FLAG = new SGCheck("POINTS_FLAG",Util.TI18N.POINTS_FLAG());	//是否串点
		//POINTS_FLAG.setColSpan(2);
		POINTS_FLAG.setDefaultValue(true);
		
		SGText POINTS_NUM = new SGText("POINTS_NUM", Util.TI18N.POINTS_NUM());	//串点数量
		//POINTS_NUM.setColSpan(2);
		POINTS_NUM.setDefaultValue(0);
		POINTS_NUM.setTitleOrientation(TitleOrientation.TOP);
		
		SGCheck BILL_LINE_FLAG = new SGCheck("BILL_LINE_FLAG",Util.TI18N.BILL_LINE_FLAG(),true);	//是否按线路计费
		//BILL_LINE_FLAG.setColSpan(2);
		BILL_LINE_FLAG.setDefaultValue(false);

        //9
        TextAreaItem NOTES = new TextAreaItem("NOTES",Util.TI18N.NOTES());
        NOTES.setStartRow(true);
        NOTES.setColSpan(6);
        NOTES.setHeight(50);
        NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
        NOTES.setTitleVAlign(VerticalAlignment.TOP);
        NOTES.setTitleOrientation(TitleOrientation.TOP);
		
		mainForm2 = new SGPanel();
		mainForm2.setWidth("40%");
		mainForm2.setItems(START_DATE,END_DATE,EXEC_ORG_ID, EXEC_ORG_ID_NAME, SHOW_SEQ, TIME_UNIT,
				OTD_TIME,TOTL_DISTANCE, TOTAL_AMOUNT,BILL_LINE_FLAG,POINTS_FLAG, POINTS_NUM,NOTES);
        
        section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SectionStackSection mainS = new SectionStackSection(Util.TI18N.MAININFO());
		mainS.addItem(mainForm);
		mainS.setExpanded(true); 
		section.addSection(mainS);
		
		SectionStackSection mainS2 = new SectionStackSection(Util.TI18N.BASEINFO());
		mainS2.addItem(mainForm2);
		mainS2.setExpanded(true); 
		section.addSection(mainS2);
		
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(true);
        
        return section;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.ROUTE);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(mainDS,
							createSerchForm(searchForm), lst_section.getSection(0),vm).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.ROUTE_P0_01);
        newButton.addClickHandler(new NewMultiFormAction(vm, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.ROUTE_P0_02);
        saveButton.addClickHandler(new SaveMultiFormAction(headTable, vm, check_map, this));
        
        HashMap<String, String> tab_map = new HashMap<String, String>(); //需要删除的从表名称与对应主表字段id
        tab_map.put("BAS_ROUTE_DETAIL", "ROUTE_ID");
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.ROUTE_P0_03);
        delButton.addClickHandler(new DeleteMultiFormAction(headTable, vm, tab_map));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.ROUTE_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(headTable, vm,this));
        
        //IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.ROUTE_P0_06);
        //expButton.addClickHandler(new ExportAction(headTable, "ADDTIME"));
        
        IButton modifyButton = createUDFBtn("维护途径点", StaticRef.ICON_WIN,BasPrivRef.ROUTE_P0_05);
        modifyButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(cache_map != null && event != null && event.getSource() != null) {
					if(cache_map.get("START_AREA_NAME2") != null) {
						new RouteHalfwayWin(cache_map,headTable.getSelectedRecord().getAttribute("CUSTOMER_ID"),headTable.getSelectedRecord().getAttributeAsString("ID")).getViewPanel();
					}
					else {
						if(headTable.getRecords().length == 0)
							return;
						ListGridRecord record = headTable.getSelectedRecord();
						cache_map = new HashMap<String, String>();
						cache_map.put("ROUTE_ID", record.getAttributeAsString("ID"));
						cache_map.put("TRANS_SRVC_ID", record.getAttributeAsString("TRANS_SRVC_ID"));
						cache_map.put("START_AREA_ID", record.getAttributeAsString("START_AREA_ID"));
						cache_map.put("START_AREA_NAME", record.getAttributeAsString("START_AREA_NAME"));
						cache_map.put("END_AREA_ID", record.getAttributeAsString("END_AREA_ID"));
						cache_map.put("END_AREA_NAME", record.getAttributeAsString("END_AREA_NAME"));
						cache_map.put("START_AREA_ID2", record.getAttributeAsString("START_AREA_ID2"));
						cache_map.put("START_AREA_NAME2", record.getAttributeAsString("START_AREA_NAME2"));
						cache_map.put("END_AREA_ID2", record.getAttributeAsString("END_AREA_ID2"));
						cache_map.put("END_AREA_NAME2", record.getAttributeAsString("END_AREA_NAME2"));
						cache_map.put("EXEC_ORG_ID", record.getAttributeAsString("EXEC_ORG_ID"));
						new RouteHalfwayWin(cache_map,headTable.getSelectedRecord().getAttribute("CUSTOMER_ID"),headTable.getSelectedRecord().getAttributeAsString("ID")).getViewPanel();
					}
				}
			}  	
        });
        
        add_map.put(BasPrivRef.ROUTE_P0_01, newButton);
        del_map.put(BasPrivRef.ROUTE_P0_03, delButton);
        del_map.put(BasPrivRef.ROUTE_P0_05, modifyButton);
        save_map.put(BasPrivRef.ROUTE_P0_02, saveButton);
        save_map.put(BasPrivRef.ROUTE_P0_04, canButton);
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
    
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, modifyButton);
	}
	
	private void getConfigList() {
		
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME", Util.TI18N.TRANSPORT_SERVICE(), 90);
		ListGridField START_AREA_NAME = new ListGridField("START_AREA_NAME2", Util.TI18N.START_ARAE(), 100);
		ListGridField END_AREA_NAME = new ListGridField("END_AREA_NAME2", Util.TI18N.END_AREA(), 100);
		ListGridField ROUTE_NAME = new ListGridField("ROUTE_NAME", Util.TI18N.ROUTE_NAME(), 150);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 150);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 70);
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 90);
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(), 45);
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField START_DATE = new ListGridField("START_DATE", Util.TI18N.START_DATE(), 80);
		ListGridField END_DATE = new ListGridField("END_DATE", Util.TI18N.END_DATE(), 80);
		ListGridField TOTL_DISTANCE = new ListGridField("TOTL_DISTANCE", Util.TI18N.TOTL_DISTANCE(), 50);
		ListGridField TOTAL_AMOUNT = new ListGridField("TOTAL_AMOUNT", Util.TI18N.TOTAL_AMOUNT(), 50);
		ListGridField OTD_TIME = new ListGridField("OTD_TIME", Util.TI18N.OTD_TIME(), 50);
		ListGridField TIME_UNIT_NAME = new ListGridField("TIME_UNIT_NAME", Util.TI18N.TIME_UNIT(), 70);
		ListGridField POINTS_FLAG = new ListGridField("POINTS_FLAG", Util.TI18N.POINTS_FLAG(), 45);
		POINTS_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField BILL_LINE_FLAG = new ListGridField("BILL_LINE_FLAG", Util.TI18N.BILL_LINE_FLAG(), 45);
		BILL_LINE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 150);
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ", Util.TI18N.SHOW_SEQ(), 45);
		ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID");
		CUSTOMER_ID.setHidden(true);
		headTable.setFields(TRANS_SRVC_ID_NAME,START_AREA_NAME,END_AREA_NAME,ROUTE_NAME,SHORT_NAME,HINT_CODE,EXEC_ORG_ID_NAME,ENABLE_FLAG,START_DATE,
				END_DATE,TOTL_DISTANCE,TOTAL_AMOUNT,OTD_TIME,TIME_UNIT_NAME,POINTS_FLAG,BILL_LINE_FLAG,NOTES,SHOW_SEQ,CUSTOMER_ID);
	}
	
	//查询窗口
	@SuppressWarnings("unchecked")
	public DynamicForm createSerchForm(SGPanel form){
		
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(12);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		
		TextItem txt_global = new TextItem("FULL_IN", "途经点代码");
		txt_global.setTitleOrientation(TitleOrientation.LEFT);
		txt_global.setWidth(250);
		txt_global.setColSpan(4);
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		
		TextItem ADDR_NAME = new TextItem("ADDR_NAME", "途经点名称");
		ADDR_NAME.setTitleOrientation(TitleOrientation.LEFT);
		ADDR_NAME.setWidth(250);
		ADDR_NAME.setColSpan(4);
		ADDR_NAME.setEndRow(true);
		ADDR_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//2行
		SGCombo AREA_ID = new SGCombo("START_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.START_ARAE())+"<br />"+Util.TI18N.PROVINCE());
        AREA_ID.setTitleOrientation(TitleOrientation.TOP);
        AREA_ID.setStartRow(true);
		SGCombo AREA_ID2 = new SGCombo("START_AREA_ID2","<br />"+Util.TI18N.CITY());
		AREA_ID2.setTitleOrientation(TitleOrientation.TOP);
		SGText AREA_NAME = new SGText("START_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.START_ARAE())+"<br />"+Util.TI18N.PROVINCE());
        AREA_NAME.setVisible(false);
        final SGText AREA_NAME2 = new SGText("START_AREA_NAME2","<br />"+Util.TI18N.CITY());
        AREA_NAME2.setVisible(false);
        AREA_ID.addChangedHandler(new AreaChangeAction(AREA_NAME,AREA_ID2,AREA_NAME2));
        AREA_ID2.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME2 != null){
					AREA_NAME2.setValue(value);
				}
			}
        	
        });
        
        SGCombo AREA_ID4 = new SGCombo("END_AREA_ID",ColorUtil.getRedTitle(Util.TI18N.END_AREA())+"<br />"+Util.TI18N.PROVINCE());
        AREA_ID4.setTitleOrientation(TitleOrientation.TOP);
		SGCombo AREA_ID5 = new SGCombo("END_AREA_ID2","<br />"+Util.TI18N.CITY());
		AREA_ID5.setTitleOrientation(TitleOrientation.TOP);
		SGText AREA_NAME4 = new SGText("END_AREA_NAME",ColorUtil.getRedTitle(Util.TI18N.END_AREA())+"<br />"+Util.TI18N.PROVINCE());
        AREA_NAME4.setVisible(false);
        final SGText AREA_NAME5 = new SGText("END_AREA_NAME2","<br />"+Util.TI18N.CITY());
        AREA_NAME5.setVisible(false);
        
		ArrayList comboList = new ArrayList();
		comboList.add(AREA_ID);
		comboList.add(AREA_ID4);
		SGCombo[] combos = (SGCombo[])comboList.toArray(new SGCombo[comboList.size()]);
		Util.initComboValue(combos, "BAS_AREA", "AREA_CODE", "AREA_CNAME", " AREA_LEVEL = '3'", "");
        AREA_ID4.addChangedHandler(new AreaChangeAction(AREA_NAME4,AREA_ID5,AREA_NAME5));
        AREA_ID5.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				SGCombo fatherItem = (SGCombo)event.getSource();
				String value = fatherItem.getDisplayValue();
				if(ObjUtil.isNotNull(value) && AREA_NAME5 != null){
					AREA_NAME5.setValue(value);
				}
			}
        	
        });
		
 		//3行
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANSPORT_SERVICE());
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME = new TextItem("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setColSpan(2);
		EXEC_ORG_ID_NAME.setWidth(120);
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, true, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		SGCheck POINTS_FLAG = new SGCheck("POINTS_FLAG",Util.TI18N.POINTS_FLAG());	//是否串点
		POINTS_FLAG.setColSpan(2);
		POINTS_FLAG.setDefaultValue(false);
		
		SGCheck BILL_LINE_FLAG = new SGCheck("BILL_LINE_FLAG",Util.TI18N.BILL_LINE_FLAG());	//是否按线路计费
		BILL_LINE_FLAG.setDefaultValue(false);
		
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		ENABLE_FLAG.setValue(true);
		ENABLE_FLAG.setColSpan(2);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setValue(true);
		
		TextItem CUSTOMER_ID = new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		ComboBoxItem CUSTOMER_NAME = new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
        form.setItems(txt_global, ADDR_NAME, AREA_ID, AREA_NAME, AREA_ID2, AREA_NAME2, AREA_ID4,AREA_NAME4,
        		AREA_ID5,AREA_NAME5,CUSTOMER_ID,CUSTOMER_NAME,TRANS_SRVC_ID, EXEC_ORG_ID, EXEC_ORG_ID_NAME,  
        		BILL_LINE_FLAG,POINTS_FLAG, ENABLE_FLAG, C_ORG_FLAG);
        
        return form;
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "BAS_ROUTE_HEAD");
		check_map.put("TRANS_SRVC_ID", StaticRef.CHK_NOTNULL + Util.TI18N.TRANSPORT_SERVICE());
		check_map.put("START_AREA_ID", StaticRef.CHK_NOTNULL + Util.TI18N.START_ARAE());
		check_map.put("END_AREA_ID", StaticRef.CHK_NOTNULL + Util.TI18N.END_AREA());
		check_map.put("START_AREA_ID2", StaticRef.CHK_NOTNULL + Util.TI18N.START_ARAE());
		check_map.put("END_AREA_ID2", StaticRef.CHK_NOTNULL + Util.TI18N.END_AREA());
		check_map.put("SHORT_NAME", StaticRef.CHK_UNIQUE + Util.TI18N.SHORT_NAME());
		check_map.put("ROUTE_NAME", StaticRef.CHK_UNIQUE + Util.TI18N.ROUTE_NAME());
		check_map.put("HINT_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.HINT_CODE());
		check_map.put("START_DATE", StaticRef.CHK_DATE + Util.TI18N.START_DATE());
		check_map.put("END_DATE", StaticRef.CHK_DATE + Util.TI18N.END_DATE());
		
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("TOTL_DISTANCE", "0");
		cache_map.put("OTD_TIME", "0");
		cache_map.put("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasRouteView view = new BasRouteView();
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
