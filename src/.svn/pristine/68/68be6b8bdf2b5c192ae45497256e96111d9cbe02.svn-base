package com.rd.client.view.base;

import java.util.HashMap;
import com.rd.client.action.base.route.CancelSubAction;
import com.rd.client.action.base.route.SaveSubAction;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.base.RouteDetailDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * 基础资料->线路管理（中途点信息二级窗口）
 * @author yuanlei
 *
 */
public class RouteHalfwayWin{

	private DataSource detailDS;
	private SGTable detailTable;
	private DataSource halfwayDS;
	private SGTable halfwayTable;
	private	Record cb_cache;   //（行政区域或地址点）下拉框选中的数据缓存
	private int cur_row;               //当前选中的行
	private static HashMap<String, String> main_map;
	private String customer;
	private String headid;
 
	public RouteHalfwayWin(HashMap<String, String> p_cache_map) {
		main_map = p_cache_map;
	}
	public RouteHalfwayWin(HashMap<String, String> p_cache_map,String cus,String headid) {
		main_map = p_cache_map;
		this.customer = cus;
		this.headid = headid;
	}

	public Window getViewPanel() {
		//创建主界面MAIN
		Window main = new Window();
		main.setTitle("途经点信息");
		main.setTop("25%");
		main.setLeft("25%");
        main.setWidth(610);
        main.setHeight(370);
        main.setCanDragResize(true);		
		
		//创建起点终点界面MAIN_V1
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setHeight(28);
		toolStrip.setPadding(2);
		toolStrip.setAlign(Alignment.LEFT);
        toolStrip.setSeparatorSize(12);
		toolStrip.setMembersMargin(4);
        ImgButton startButton = new ImgButton();
        startButton.setWidth(21);
        startButton.setHeight(21);
        startButton.setSrc(StaticRef.ICON_START);
		toolStrip.addMember(startButton);
		Label start = new Label(ObjUtil.ifNull(main_map.get("START_AREA_NAME2"),"").toString());
		start.setTop(4);
		start.setWidth(60);
		start.setHeight(16);
		start.setValign(VerticalAlignment.CENTER);
		start.setAlign(Alignment.LEFT);
		start.setBaseStyle("btnCheckTitle");
		toolStrip.addMember(start);
		ImgButton toButton = new ImgButton();
		toButton.setWidth(60);
		toButton.setHeight(21);
		toButton.setSrc(StaticRef.ICON_TO);
		toolStrip.addMember(toButton);
		ImgButton endButton = new ImgButton();
		endButton.setWidth(21);
		endButton.setHeight(21);
		endButton.setSrc(StaticRef.ICON_END);
		toolStrip.addMember(endButton);
		Label end = new Label(ObjUtil.ifNull(main_map.get("END_AREA_NAME2"),"").toString());
		end.setTop(4);
		end.setWidth(60);
		end.setHeight(16);
		end.setValign(VerticalAlignment.CENTER);
		end.setAlign(Alignment.LEFT);
		end.setBaseStyle("btnCheckTitle");
		toolStrip.addMember(end);
		
		toolStrip.addSpacer(150);
		IButton saveButton = new IButton(Util.BI18N.SAVE());
		saveButton.setIcon(StaticRef.ICON_SAVE);
		saveButton.setAutoFit(true);
		toolStrip.addMember(saveButton);
		IButton cancelButton = new IButton(Util.BI18N.CANCEL());
		cancelButton.setIcon(StaticRef.ICON_CANCEL);
		cancelButton.setAutoFit(true);
		toolStrip.addMember(cancelButton);
		
		//创建途经点信息和路段信息页签
		TabSet tabSet = new TabSet();   
		tabSet.setTabBarPosition(Side.TOP);  
		tabSet.setWidth100();   
		tabSet.setHeight100();  
		
		//创建途径点页签
        Tab Tab1 = new Tab("途径点信息"); 
        VStack halfway = new VStack();
        halfway.setWidth100();
        halfway.setHeight100();
        createTab1(halfway);
        Tab1.setPane(halfway);
        
		//创建路段信息页签
        Tab Tab2 = new Tab("路段信息"); 
        HStack road = new HStack();
        road.setHeight100();
        createTab2(road);
        Tab2.setPane(road);
        
//        tabSet.addTabSelectedHandler(new TabSelectedHandler() {
//			@Override
//			public void onTabSelected(TabSelectedEvent event) {
//				if(event.getTabNum()==1){
//					Criteria crit = new Criteria();
//					detailTable.invalidateCache();
//					crit.addCriteria("OP_FLAG", detailTable.OP_FLAG);
//					crit.addCriteria("ROUTE_ID", main_map.get("ROUTE_ID"));
//					detailTable.fetchData(crit);
//				}
//			}
//		});
        
        tabSet.addTab(Tab1);   
        tabSet.addTab(Tab2);
        
		saveButton.addClickHandler(new SaveSubAction(halfwayTable, detailTable, tabSet, main_map,customer,headid));
		cancelButton.addClickHandler(new CancelSubAction(halfwayTable, tabSet, detailTable));
	
		main.addItem(toolStrip);
		main.addItem(tabSet);
		main.draw();
		
		initTable();

		return main;
	}
	
	static DataSource getDataSource()
	{	
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL("basQueryServlet?ds_id=ROUTE_HALFWAY&OP_FLAG=M&ROUTE_ID=" + ObjUtil.ifObjNull(main_map.get("ROUTE_ID"),"").toString());
		//dataSource.setDataURL("basQueryServlet?ds_id=ROUTE_HALFWAY");
		dataSource.setClientOnly(true);

        DataSourceTextField keyField = new DataSourceTextField("ID", "ID", 10);
        keyField.setPrimaryKey(true);
        keyField.setRequired(true);
        keyField.setHidden(true);
        
        DataSourceTextField ADDR_ID = new DataSourceTextField("ADDR_ID", "", 10);
        ADDR_ID.setHidden(true);
        ADDR_ID.setRequired(true);
        
		DataSourceTextField AREA_NAME = new DataSourceTextField("AREA_NAME");
		AREA_NAME.setRequired(true);
		DataSourceTextField ADDR_ID_NAME = new DataSourceTextField("ADDR_NAME");
		ADDR_ID_NAME.setRequired(true);
		DataSourceTextField ADDRESS = new DataSourceTextField("ADDRESS");
		ADDRESS.setRequired(true);
		dataSource.setFields(keyField, ADDR_ID, AREA_NAME, ADDR_ID_NAME, ADDRESS);

		return dataSource;
	}
	
	private void createTab1(VStack canvas) {
		//创建下拉框界面MAIN_V2
		DynamicForm qry_form = new DynamicForm();
		createComboForm(qry_form);
		
		//创建列表与按钮的容器MAIN_V3
		HStack stack = new HStack();  
		stack.setWidth100();
		stack.setHeight("90%");
		stack.setAlign(Alignment.LEFT);
  
		//创建MAIN_V3下的列表布局MAIN_V3_H1
		createTable();
		
		//创建MAIN_V3下的按钮布局MAIN_V3_H2
		ToolStrip act_btn = new ToolStrip();
		createBtnWidget(act_btn,qry_form);
		
		stack.addMember(halfwayTable);
		stack.addMember(act_btn);
		canvas.addMember(qry_form);
		canvas.addMember(stack);
	}
	
	private void createTab2(HStack canvas) {
		detailDS = RouteDetailDS.getInstance("V_ROUTE_DETAIL","BAS_ROUTE_DETAIL");
        detailTable = new SGTable(detailDS,"100%","100%",false,true,false);   
        detailTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);   
        detailTable.setShowRowNumbers(false);
		
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ",Util.TI18N.SHOW_SEQ(),35);
		SHOW_SEQ.setCanEdit(false);
		ListGridField AREA_ID = new ListGridField("AREA_ID","", 1);
		AREA_ID.setHidden(true);
		AREA_ID.setCanEdit(false);
		ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.ROUTE_AREA(), 65);
		AREA_NAME.setCanEdit(false);
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID", "",1);
		START_AREA_ID.setHidden(true);
		START_AREA_ID.setCanEdit(false);
		ListGridField START_AREA_NAME = new ListGridField("START_AREA_NAME",Util.TI18N.START_ARAE(),65);
		START_AREA_NAME.setCanEdit(false);
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID", "",1);
		END_AREA_ID.setHidden(true);
		END_AREA_ID.setCanEdit(false);
		ListGridField END_AREA_NAME = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA(),65);
		END_AREA_NAME.setCanEdit(false);
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID",Util.TI18N.TRANSPORT_SERVICE(),65);
		ListGridField ADDR_ID = new ListGridField("ADDR_ID","",1);
		ADDR_ID.setHidden(true);
		ADDR_ID.setCanEdit(false);
		ListGridField ADDR_ID_NAME = new ListGridField("ADDR_ID_NAME",Util.TI18N.ROUTE_ADDR(),90);
		ADDR_ID_NAME.setCanEdit(false);
		ListGridField ADDRESS = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),150);
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID",Util.TI18N.EXEC_ORG_ID(),90);
		ListGridField DISTENCE = new ListGridField("DISTENCE",Util.TI18N.ROUTE_DISTENCE(),50);
		ListGridField RUNTIME = new ListGridField("RUNTIME",Util.TI18N.RUNTIME(),50);
		ListGridField MAINWAY = new ListGridField("MAINWAY",Util.TI18N.MAINWAY(),100);
		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),150);
  
		Util.initTrsService(TRANS_SRVC_ID_NAME, "");
		Util.initOrg(EXEC_ORG_ID_NAME, "", "", false);
		detailTable.setFields(SHOW_SEQ,AREA_ID, AREA_NAME, START_AREA_ID, START_AREA_NAME, END_AREA_ID, END_AREA_NAME, TRANS_SRVC_ID_NAME
				, ADDR_ID, ADDR_ID_NAME, ADDRESS, EXEC_ORG_ID_NAME, DISTENCE, RUNTIME, MAINWAY, NOTES);
		canvas.addMember(detailTable);
	}
	
	private void createTable() {
		halfwayDS = getDataSource();
		halfwayTable = new SGTable(halfwayDS,"546","100%",false,true,false);   
		halfwayTable.setShowRowNumbers(false);
		halfwayTable.setEditEvent(ListGridEditEvent.DOUBLECLICK); 
		ListGridField ID = new ListGridField("ID", "", 1);
		ID.setHidden(true);
		ListGridField ADDR_ID = new ListGridField("ADDR_ID", "", 1);
		ADDR_ID.setHidden(true);
		ListGridField SHOW_SEQ = new ListGridField("SHOW_SEQ", Util.TI18N.SHOW_SEQ(), 50);
		ListGridField AREA_ID_NAME = new ListGridField("AREA_NAME",Util.TI18N.ROUTE_AREA(), 90);
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME",Util.TI18N.ROUTE_ADDR(),130);
		ListGridField ADDRESS = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),220);
		halfwayTable.setFields(ID, SHOW_SEQ, ADDR_ID, AREA_ID_NAME, ADDR_NAME, ADDRESS);
		halfwayTable.addCellClickHandler(new CellClickHandler() {

			@Override
			public void onCellClick(CellClickEvent event) {
				cur_row = event.getRowNum();
				cb_cache = halfwayTable.getRecord(cur_row);
			}
		});
	}
	
	private void createComboForm(DynamicForm qry_form) {
		qry_form.setWidth100();
		qry_form.setNumCols(8);
		qry_form.setHeight(25);
		qry_form.setTitleWidth(75);
//		ComboBoxItem area_cb = new ComboBoxItem("AREA_ID", Util.TI18N.ROUTE_AREA());
		ComboBoxItem addr_cb = new ComboBoxItem("ADDR_ID", Util.TI18N.ROUTE_ADDR());
		qry_form.setItems(addr_cb);
//		initArea(area_cb);
		initAddrId(addr_cb);
	}
	
	public void createBtnWidget(ToolStrip toolStrip,final DynamicForm form) {
		toolStrip.setTop(20);
		toolStrip.setVertical(true);
		toolStrip.setAlign(Alignment.CENTER);
		toolStrip.setAlign(VerticalAlignment.TOP);
		toolStrip.setWidth(30);
		toolStrip.setHeight100();
		
		ToolStripButton  leftButton = new ToolStripButton();
		leftButton.setWidth(24);
		leftButton.setHeight(24);
		leftButton.setIcon(StaticRef.ICON_LEFT);
		leftButton.setActionType(SelectionType.RADIO);  
        toolStrip.addButton(leftButton);
        
        ToolStripButton rightButton = new ToolStripButton();
        rightButton.setWidth(24);
        rightButton.setHeight(24);
        rightButton.setIcon(StaticRef.ICON_RIGHT);
        rightButton.setActionType(SelectionType.RADIO); 
        toolStrip.addButton(rightButton); 
        toolStrip.addSeparator(); 
        
        ToolStripButton upButton = new ToolStripButton();
        upButton.setWidth(24);
        upButton.setHeight(24);
        upButton.setIcon(StaticRef.ICON_UP);
        upButton.setActionType(SelectionType.RADIO);
        toolStrip.addButton(upButton);
        
        ToolStripButton downButton = new ToolStripButton();
        downButton.setWidth(24);
        downButton.setHeight(24);
        downButton.setIcon(StaticRef.ICON_DOWN);
        downButton.setActionType(SelectionType.RADIO);  
        toolStrip.addButton(downButton);
        toolStrip.addFill();
   
        leftButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(cb_cache != null) {
					boolean isExist = false;
					if(halfwayTable.getRecords().length > 0) {
						String cache_id  = cb_cache.getAttribute("ID");
						String cache_addr = cb_cache.getAttribute("ADDR_ID");
						for(int i = 0; i < halfwayTable.getRecords().length; i++) {
							String list_id = halfwayTable.getRecord(i).getAttribute("ID");
							String list_addr = halfwayTable.getRecord(i).getAttribute("ADDR_ID");
							if(cache_id.compareTo(list_id) == 0) {
								if((!ObjUtil.isNotNull(cache_addr) && !ObjUtil.isNotNull(list_addr)) ||
										(ObjUtil.isNotNull(cache_addr) && ObjUtil.isNotNull(list_addr) 
										    &&cache_addr.compareTo(list_addr) == 0)) {
									isExist = true;
									break;
								}
							}
						}
					}
					if(!isExist) {
						addRecord();
					}
					form.getItem("ADDR_ID").clearValue();
				}
			}
        	
        });
        rightButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				removeRecord();
			}
        	
        });
        upButton.addClickHandler(new ClickHandler() {

			@Override 
			public void onClick(ClickEvent event)  {
				ListGridRecord[] records = halfwayTable.getRecords();	
				if(records != null && cur_row > 0) {
					ListGridRecord pre_record = records[cur_row-1];
					String ID = pre_record.getAttribute("ID");
					String AREA_ID_NAME = pre_record.getAttribute("AREA_NAME");
					String ADDR_NAME = pre_record.getAttribute("ADDR_NAME");
					String ADDR_ID = pre_record.getAttribute("ADDR_ID");
					String ADDRESS = pre_record.getAttribute("ADDRESS");
					String EXEC_ORG_ID = pre_record.getAttribute("EXEC_ORG_ID");
					records[cur_row-1].setAttribute("ID", records[cur_row].getAttribute("ID"));
					records[cur_row-1].setAttribute("ADDR_ID", records[cur_row].getAttribute("ADDR_ID"));
					records[cur_row-1].setAttribute("AREA_NAME", records[cur_row].getAttribute("AREA_NAME"));
					records[cur_row-1].setAttribute("ADDR_NAME", records[cur_row].getAttribute("ADDR_NAME"));
					records[cur_row-1].setAttribute("ADDRESS",  records[cur_row].getAttribute("ADDRESS"));
					records[cur_row-1].setAttribute("EXEC_ORG_ID",  records[cur_row].getAttribute("EXEC_ORG_ID"));
					
					records[cur_row].setAttribute("ID", ID);
					records[cur_row].setAttribute("ADDR_ID", ADDR_ID);
					records[cur_row].setAttribute("AREA_NAME", AREA_ID_NAME);
					records[cur_row].setAttribute("ADDR_NAME", ADDR_NAME);
					records[cur_row].setAttribute("ADDRESS", ADDRESS);
					records[cur_row].setAttribute("EXEC_ORG_ID", EXEC_ORG_ID);
					
					halfwayTable.setData(records);
					halfwayTable.selectRecord(records[cur_row-1]);
					cur_row = cur_row - 1;
				}
			}
        	
        });
        downButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] records = halfwayTable.getRecords();	
				if(records != null && cur_row < records.length - 1) {
					ListGridRecord next_record = records[cur_row+1];
					String ID = next_record.getAttribute("ID");
					String AREA_ID_NAME = next_record.getAttribute("AREA_NAME");
					String ADDR_ID = next_record.getAttribute("ADDR_ID");
					String ADDR_NAME = next_record.getAttribute("ADDR_NAME");
					String ADDRESS = next_record.getAttribute("ADDRESS");
					String EXEC_ORG_ID = next_record.getAttribute("EXEC_ORG_ID");
					records[cur_row+1].setAttribute("ID", records[cur_row].getAttribute("ID"));
					records[cur_row+1].setAttribute("ADDR_ID", records[cur_row].getAttribute("ADDR_ID"));
					records[cur_row+1].setAttribute("AREA_NAME", records[cur_row].getAttribute("AREA_NAME"));
					records[cur_row+1].setAttribute("ADDR_NAME", records[cur_row].getAttribute("ADDR_NAME"));
					records[cur_row+1].setAttribute("ADDRESS", records[cur_row].getAttribute("ADDRESS"));
					records[cur_row+1].setAttribute("EXEC_ORG_ID", records[cur_row].getAttribute("EXEC_ORG_ID"));
					
					records[cur_row].setAttribute("ID", ID);
					records[cur_row].setAttribute("ADDR_ID", ADDR_ID);
					records[cur_row].setAttribute("AREA_NAME", AREA_ID_NAME);
					records[cur_row].setAttribute("ADDR_NAME", ADDR_NAME);
					records[cur_row].setAttribute("ADDRESS", ADDRESS);
					records[cur_row].setAttribute("EXEC_ORG_ID", EXEC_ORG_ID);
					
					halfwayTable.setData(records);
					halfwayTable.selectRecord(records[cur_row+1]);
					cur_row = cur_row + 1;
				}
			}       	
        });
	}
	
	private void addRecord(){
		if(cb_cache == null)return;
		RecordList newList = new RecordList();
		newList.addList(halfwayTable.getRecords());
		newList.add(cb_cache);
		halfwayTable.setData(newList);
		halfwayTable.selectSingleRecord(cb_cache);
		cur_row = halfwayTable.getRecords().length;
	}
	
	private void removeRecord(){
		if(cb_cache == null || halfwayTable.getRecordIndex(cb_cache) < 0)return;
		RecordList newList = new RecordList();
		newList.addList(halfwayTable.getRecords());
		newList.remove(cb_cache);
		halfwayTable.setData(newList);
	}
	
	//初始化行政区域下拉框
	/*private void initArea(final ComboBoxItem start_area){
		
		DataSource ds = AreaDS.getInstance("BAS_AREA");
		
		ListGridField ID = new ListGridField("ID", " ", 1);
		ID.setHidden(true);
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		start_area.setWidth(120);  
		start_area.setColSpan(2);
		start_area.setOptionDataSource(ds);  
		start_area.setDisabled(false);
		start_area.setShowDisabled(false);
		start_area.setDisplayField("FULL_INDEX");  
		start_area.setValueField("SHORT_NAME");
		start_area.setPickListWidth(240);
		start_area.setPickListBaseStyle("myBoxedGridCell");
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		start_area.setPickListCriteria(criteria);		
		start_area.setPickListFields(AREA_CODE, SHORT_NAME, HINT_CODE);
		start_area.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = start_area.getSelectedRecord();
				if(selectedRecord != null){
					int seq = 1;
					ListGridRecord last_record = halfwayTable.getRecord(halfwayTable.getTotalRows()-1);
					if(last_record != null && ObjUtil.isNotNull(last_record.getAttribute("SHOW_SEQ"))) {
						seq = Integer.parseInt(last_record.getAttribute("SHOW_SEQ").toString()) + 1;
					}
					cb_cache = new ListGridRecord();
					cb_cache.setAttribute("ID", selectedRecord.getAttribute("ID"));
					cb_cache.setAttribute("SHOW_SEQ", Integer.toString(seq));
					cb_cache.setAttribute("ADDR_ID", selectedRecord.getAttribute("ADDR_ID"));
					cb_cache.setAttribute("AREA_NAME", selectedRecord.getAttribute("SHORT_NAME"));
					cb_cache.setAttribute("ADDR_NAME", selectedRecord.getAttribute("ADDR_NAME"));
					cb_cache.setAttribute("ADDRESS", selectedRecord.getAttribute("ADDRESS"));
					cb_cache.setAttribute("EXEC_ORG_ID", selectedRecord.getAttribute("EXEC_ORG_ID"));
				}
			}
			
		});
	}*/

	//初始化地址点下拉框
	private void initAddrId(final ComboBoxItem load_id){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ID = new ListGridField("ID", " ", 1);
		ID.setHidden(true);
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_ID = new ListGridField("AREA_ID2");
		AREA_ID.setHidden(true);
		ListGridField AREA_NAME = new ListGridField("AREA_NAME2",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
		ListGridField EXEC_ORG_ID = new ListGridField("EXEC_ORG_ID");
		load_id.setWidth(120);  
		load_id.setColSpan(2);
		load_id.setOptionDataSource(ds2);  
		load_id.setDisabled(false);
		load_id.setShowDisabled(false);
		load_id.setDisplayField("FULL_INDEX");  
		load_id.setValueField("ADDR_NAME");
		load_id.setPickListWidth(450);
		load_id.setPickListBaseStyle("myBoxedGridCell");
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("LOAD_FLAG","true");
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("CUSTOMER_ID",customer);
		load_id.setPickListCriteria(criteria);
		
		load_id.setPickListFields(ID, ADDR_CODE, ADDR_NAME,AREA_ID,AREA_NAME,ADDRESS,EXEC_ORG_ID);
		load_id.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_id.getSelectedRecord();
				if(selectedRecord != null){
					int seq = 1;
					ListGridRecord last_record = halfwayTable.getRecord(halfwayTable.getTotalRows()-1);
					if(last_record != null && ObjUtil.isNotNull(last_record.getAttribute("SHOW_SEQ"))) {
						seq = Integer.parseInt(last_record.getAttribute("SHOW_SEQ").toString()) + 1;
					}
					cb_cache = new ListGridRecord();
					cb_cache.setAttribute("ID", selectedRecord.getAttribute("AREA_ID2"));
					cb_cache.setAttribute("SHOW_SEQ", Integer.toString(seq));
					cb_cache.setAttribute("ADDR_ID", selectedRecord.getAttribute("ID"));
					cb_cache.setAttribute("AREA_NAME", selectedRecord.getAttribute("AREA_NAME2"));
					cb_cache.setAttribute("ADDR_NAME", selectedRecord.getAttribute("ADDR_NAME"));
					cb_cache.setAttribute("ADDRESS", selectedRecord.getAttribute("ADDRESS"));
					cb_cache.setAttribute("EXEC_ORG_ID", selectedRecord.getAttribute("EXEC_ORG_ID"));
				}
			}
		});
	}
	
	private void initTable() {
		Criteria crit =  new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ROUTE_ID", ObjUtil.ifObjNull(main_map.get("ROUTE_ID"),"").toString());
		detailTable.fetchData(crit);
		halfwayTable.fetchData();
	}
}
