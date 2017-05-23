package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.List;

import com.rd.client.action.tms.dispatch.SplitJouryShpmAction;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.base.AddrDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
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
 * 行程拆分二级窗口
 * @author yuanlei
 *
 */
public class JourneySplitWin extends Window{

	private DataSource halfwayDS;
	private SGTable halfwayTable;
	private	Record cb_cache;   //（行政区域或地址点）下拉框选中的数据缓存
	private int cur_row;               //当前选中的行
	private static ListGridRecord[] main_map;
	private SGForm view;
	
	public static interface CloseHandler{
		void onClosed(String message);
	}
	private List<CloseHandler> closeHandlers=new ArrayList<CloseHandler>();
	public void addCloseHandler(CloseHandler handler){
		closeHandlers.add(handler);
	}
	public void removeHandler(CloseHandler handler){
		closeHandlers.remove(handler);
	}
	public void fireCloseEvent(String message){
		for(CloseHandler handler:closeHandlers)
			handler.onClosed(message);
	} 
	public JourneySplitWin(SGForm form,ListGridRecord[] p_cache_map) {
		main_map = p_cache_map;
		view = form;		
		setTitle("途经点信息");
		setTop("25%");
		setLeft("25%");
        setWidth(610);
        setHeight(370);
        setCanDragResize(true);	
		
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
		Label start = new Label(Util.iff(main_map[0].getAttribute("LOAD_AREA_NAME"),"").toString());
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
		Label end = new Label(Util.iff(main_map[0].getAttribute("UNLOAD_AREA_NAME"),"").toString());
		end.setTop(4);
		end.setWidth(60);
		end.setHeight(16);
		end.setValign(VerticalAlignment.CENTER);
		end.setAlign(Alignment.LEFT);
		end.setBaseStyle("btnCheckTitle");
		toolStrip.addMember(end);
		
		toolStrip.addSpacer(150);
		IButton confirmButton = new IButton(Util.BI18N.CONFIRM());
		confirmButton.setIcon(StaticRef.ICON_SAVE);
		confirmButton.setAutoFit(true);
		toolStrip.addMember(confirmButton);
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
        tabSet.addTab(Tab1);   
        
        confirmButton.addClickHandler(new SplitJouryShpmAction(this, view, main_map, halfwayTable));
        cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				halfwayTable.removeSelectedData();
			}
			
		});
	
		addItem(toolStrip);
		addItem(tabSet);
		draw();
		
		halfwayTable.fetchData();
	}
	
	static DataSource getDataSource()
	{	
		DataSource dataSource = new DataSource();
		dataSource.setDataFormat(DSDataFormat.JSON);
		dataSource.setDataURL("basQueryServlet?ds_id=ROUTE_HALFWAY&OP_FLAG=M&ROUTE_ID=" + ObjUtil.ifObjNull(main_map[0].getAttribute("SHPM_NO"),"").toString());
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
		createBtnWidget(act_btn);
		
		stack.addMember(halfwayTable);
		stack.addMember(act_btn);
		canvas.addMember(qry_form);
		canvas.addMember(stack);
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
		ListGridField ADDRESS = new ListGridField("ADDRESS",Util.TI18N.ADDRESS(),180);
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
		ComboBoxItem addr_cb = new ComboBoxItem("ADDR_ID", Util.TI18N.ROUTE_ADDR());
		qry_form.setItems(addr_cb);
		initAddrId(addr_cb);
	}
	
	public void createBtnWidget(ToolStrip toolStrip) {
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
						halfwayTable.addData(cb_cache);
						halfwayTable.selectSingleRecord(cb_cache);
						cur_row = halfwayTable.getRecords().length;
					}
				}
			}
        	
        });
        rightButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(halfwayTable.getRecordIndex(cb_cache) >= 0) {
					halfwayTable.removeData(cb_cache);
				}
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
					records[cur_row-1].setAttribute("ID", records[cur_row].getAttribute("ID"));
					records[cur_row-1].setAttribute("ADDR_ID", records[cur_row].getAttribute("ADDR_ID"));
					records[cur_row-1].setAttribute("AREA_NAME", records[cur_row].getAttribute("AREA_NAME"));
					records[cur_row-1].setAttribute("ADDR_NAME", records[cur_row].getAttribute("ADDR_NAME"));
					records[cur_row-1].setAttribute("ADDRESS",  records[cur_row].getAttribute("ADDRESS"));
					
					records[cur_row].setAttribute("ID", ID);
					records[cur_row].setAttribute("ADDR_ID", ADDR_ID);
					records[cur_row].setAttribute("AREA_NAME", AREA_ID_NAME);
					records[cur_row].setAttribute("ADDR_NAME", ADDR_NAME);
					records[cur_row].setAttribute("ADDRESS", ADDRESS);
					
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
					records[cur_row+1].setAttribute("ID", records[cur_row].getAttribute("ID"));
					records[cur_row+1].setAttribute("ADDR_ID", records[cur_row].getAttribute("ADDR_ID"));
					records[cur_row+1].setAttribute("AREA_NAME", records[cur_row].getAttribute("AREA_NAME"));
					records[cur_row+1].setAttribute("ADDR_NAME", records[cur_row].getAttribute("ADDR_NAME"));
					records[cur_row+1].setAttribute("ADDRESS", records[cur_row].getAttribute("ADDRESS"));
					
					records[cur_row].setAttribute("ID", ID);
					records[cur_row].setAttribute("ADDR_ID", ADDR_ID);
					records[cur_row].setAttribute("AREA_NAME", AREA_ID_NAME);
					records[cur_row].setAttribute("ADDR_NAME", ADDR_NAME);
					records[cur_row].setAttribute("ADDRESS", ADDRESS);
					
					halfwayTable.setData(records);
					halfwayTable.selectRecord(records[cur_row+1]);
					cur_row = cur_row + 1;
				}
			}       	
        });
	}

	//初始化地址点下拉框
	private void initAddrId(final ComboBoxItem load_id){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ID = new ListGridField("ID", " ", 1);
		ID.setHidden(true);
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_ID = new ListGridField("AREA_ID");
		AREA_ID.setHidden(true);
		ListGridField AREA_NAME = new ListGridField("AREA_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
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
		criteria.addCriteria("ENABLE_FLAG","Y");
		criteria.addCriteria("TRANSFER_FLAG","true");
		load_id.setPickListCriteria(criteria);
		
		load_id.setPickListFields(ID, ADDR_CODE, ADDR_NAME,AREA_ID,AREA_NAME,ADDRESS);
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
					cb_cache.setAttribute("ID", selectedRecord.getAttribute("AREA_ID"));
					cb_cache.setAttribute("SHOW_SEQ", Integer.toString(seq));
					cb_cache.setAttribute("ADDR_ID", selectedRecord.getAttribute("ID"));
					cb_cache.setAttribute("AREA_NAME", selectedRecord.getAttribute("AREA_NAME"));
					cb_cache.setAttribute("ADDR_NAME", selectedRecord.getAttribute("ADDR_NAME"));
					cb_cache.setAttribute("ADDRESS", selectedRecord.getAttribute("ADDRESS"));
				}
			}
		});
	}
}
