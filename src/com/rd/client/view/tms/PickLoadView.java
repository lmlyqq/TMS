package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.action.tms.track.CancleLoadAction;
import com.rd.client.action.tms.track.FinishedLoadAction;
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
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.ds.tms.ShpmDS2;
import com.rd.client.ds.tms.ShpmDetailQSDS;
import com.rd.client.ds.tms.TmsFollowDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RecordSummaryFunctionType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SummaryFunctionType;
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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理-->提货装车
 *
 */
@ClassForNameAble
public class PickLoadView extends SGForm implements PanelFactory {

	private DataSource ds;
	public  SGTable groupTable1;
	//public  SGTable groupTable2;
	private SGPanel searchForm;
	private Window searchWin;
	public SGPanel panel;
	public SGPanel panel1;
	
	public SGTable shpmTable;       //已调作业单表
	public SGTable shpmlstTable;    //已调作业单明细表
	public SGTable damageTable;
	private String shpm_no; //作业单号
	
	private DataSource loadDS;
	public SGTable loadTable;
	
	private Window searchLoadWin;
	private SGPanel searchLoadForm;
	
	private String canModify; 
	
//	private String flag=null;
	public TabSet TabSet;
//	public SGCheck OP_SHPM;
//	public SGCheck OP_LOAD;
	
	private DataSource shpmDS;            //已调作业单数据源
	private DataSource shpmlstDS;         //已调作业但明细数据源
	private String LoadNo;//监听被选中的调度单号
	public Record shpmnorecord;
	public Record grouprecords;
	public ListGridRecord[] loadReocrd;
	private Record re;
	
	private SectionStack sectionStack;
	private SectionStack sectionLoadStack;
	
	private  SectionStackSection  listItem;
	private SectionStackSection listLoadItem;
	
	public IButton recepitButton=new IButton();
	public IButton canReceButton=new IButton();
	
	public IButton finishButton;
	public IButton cancelLoadButton;
	
	public IButton cusRecepitButton;
	public IButton preRecepitButton;
	public IButton cusCanReceButton;
	
	public int tabSelect = 0;
//	public int tabSelectTop=0;
	
	public HashMap<String,IButton> add_dm_map; //新增、
	public HashMap<String,IButton> save_dm_map; //保存、取消按钮
	public HashMap<String, IButton> del_dm_map; //删除按钮

	private boolean isDownMax = false;

	/*public PickLoadView(String id) {
	    super(id);
	}*/
	
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		
		/**
		 * @ds 在途跟踪数据源
		 * @shpmDS 作业单主表数据源
		 * @shpmlstDS 作业单详细表数据源
		 */
		ds = TmsFollowDS.getInstance("V_TRANS_TRACK_TRACE","TRANS_TRACK_TRACE");
		shpmDS = ShpmDS2.getInstance("V_SHIPMENT_HEADER", "TRANS_SHIPMENT_HEADER");
		shpmlstDS = ShpmDetailQSDS.getInstance("V_SHIPMENT_ITEM_QS", "TRANS_SHIPMENT_ITEM");
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		
		loadTable=new SGTable(loadDS);
		
		
	    ToolStrip toolStrip = new ToolStrip();//主布局按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip loadtoolStrip = new ToolStrip();  //提货装车
		loadtoolStrip.setAlign(Alignment.LEFT);
		
		//主布局
		final HStack stack =new HStack();
		stack.setHeight("80%");
		stack.setWidth100();
		
		//上边布局
		TabSet = new TabSet();
		TabSet.setWidth100();
		TabSet.setHeight("100%");
		if(isPrivilege(TrsPrivRef.LOADJOB_P2)){
			Tab tab1=new Tab("作业单信息");
			tab1.setPane(createShmpList());
			tab1.setID("0_pickloadview");
			TabSet.addTab(tab1);
			
		}
		if(isPrivilege(TrsPrivRef.LOADJOB_P4)){
			Tab tab2=new Tab("调度单信息");
			tab2.setPane(createLoadList());
			tab2.setID("1_pickloadview");
			TabSet.addTab(tab2);
		}
		TabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				String tab_flag=TabSet.getSelectedTab().getID();
					if("0_pickloadview".equals(tab_flag)){
						if(ObjUtil.isNotNull(shpmTable.getSelectedRecord())){
							if(StaticRef.LOADED_NAME.equals(shpmTable.getSelectedRecord().getAttribute("LOAD_STAT"))){
								setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
								setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,true);
							}else{
								setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,true);
								setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
							}
						}else{
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
						}
						
					}else if ("1_pickloadview".equals(tab_flag)){
						if(ObjUtil.isNotNull(loadTable.getSelectedRecord())){
							String LOAD_STAT=loadTable.getSelectedRecord().getAttribute("LOAD_STAT");
							String STATUS=loadTable.getSelectedRecord().getAttribute("STATUS");
							if(StaticRef.LOADED.equals(LOAD_STAT)){
								if(!(StaticRef.TRANS_PART_UNLOAD.equals(STATUS) || StaticRef.TRANS_UNLOAD.equals(STATUS))){
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,true);
								}else{
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
								}
							}else{
//								if(StaticRef.TRANS_DEPART.equals(STATUS)){
								if(Integer.valueOf(STATUS)>=Integer.valueOf(StaticRef.TRANS_PART_DEPART)
										&& Integer.valueOf(STATUS)<=Integer.valueOf(StaticRef.TRANS_DEPART)){
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,true);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
								}else{
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
								}
							}
						}else{
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
						}
					}
					}
		});
		
		//下边布局
		final TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("50%");	
		
		initVerify(); 
		//yuanlei 2012-10-25
		stack.addMember(TabSet);
		
//		//作业单第一级列表
//		final Menu menu = new Menu();
//		menu.setWidth(140);
//		MenuItemSeparator itemSeparator =new MenuItemSeparator();
//		
//		//if(isPrivilege(TrsPrivRef.TRACK_P0_02)) {
//		    MenuItem SelectSame = new MenuItem("全选同一车",StaticRef.ICON_NEW);
//		    SelectSame.addClickHandler(new SelectSameAction(shpmTable,this));
//		    KeyIdentifier allSelectKey = new KeyIdentifier();
//		    allSelectKey.setCtrlKey(true);
//		    allSelectKey.setKeyName("B");
//		    menu.addItem(SelectSame);
//		//}
//	    if(isPrivilege(TrsPrivRef.TRACK_P0_03)) {
//		    MenuItem position = new MenuItem("车辆定位",StaticRef.ICON_SEARCH);
//		    position.addClickHandler(new VechPositionAction(shpmTable));
//		    menu.addItem(position);
//	    }
//	    
//	    if(isPrivilege(TrsPrivRef.TRACK_P0_06)) {
//		    MenuItem position = new MenuItem("GPS定位",StaticRef.ICON_SEARCH);
//		    position.addClickHandler(new GPSPositionAction(shpmTable));
//		    menu.addItem(position);
//	    }
//	    
//	    
//	    if(isPrivilege(TrsPrivRef.TRACK_P0_08)) {
//    	    MenuItem removeItem = new MenuItem("移除作业单",StaticRef.ICON_DEL);
//    	    menu.addItem(removeItem);
//    	    removeItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
//				
//				@Override
//				public void onClick(MenuItemClickEvent event) {
//					final ListGridRecord[] records = shpmTable.getSelection();
//					if(records==null || records.length == 0){
//						SC.warn("请选择作业单！");
//						return;
//					}
//					SC.confirm("移除作业单", "确定移除?", new BooleanCallback() {
//						
//						@Override
//						public void execute(Boolean value) {
//							if(value.booleanValue()){
//								final String loadNO = shpmTable.getSelectedRecord().getAttribute("LOAD_NO");
//								Util.async.countChild("TRANS_SHIPMENT_HEADER", "LOAD_NO", loadNO, new AsyncCallback<Integer>() {
//
//									@Override
//									public void onSuccess(Integer result) {
//										if(result == null || result.intValue() < 2){
//											SC.warn("操作失败，移除作业单将造成调拨单无数据！");
//											return;
//										}
//										
//										HashMap<String, Object> listmap = new HashMap<String, Object>();
//										HashMap<String, String> order_map = new HashMap<String, String>(); //托运单 
//										HashMap<String, String> shpm_map = new HashMap<String, String>(); //作业单   
//										
//										for(int i = 0; i < records.length; i++) {
////											if(!records[i].getAttribute("STATUS_NAME").equals(StaticRef.SHPM_DIPATCH_NAME)) {
////												SC.warn("作业单[" + records[i].getAttribute("SHPM_NO") +  "]状态不允许剔除!");
////												return;
////											}
//											order_map.put(Integer.toString(i+1), records[i].getAttribute("ODR_NO"));
//											shpm_map.put(Integer.toString(i+1), records[i].getAttribute("SHPM_NO"));
//										}
//										listmap.put("1", loadNO);
//										listmap.put("2", order_map);
//										listmap.put("3", shpm_map);
//										listmap.put("4", LoginCache.getLoginUser().getUSER_ID());
//										String json = Util.mapToJson(listmap);
//										Util.async.execProcedure(json, "SP_PICK_LOAD_REMOVESHPM(?,?,?,?,?)", new AsyncCallback<String>() {
//											@Override
//											public void onSuccess(String result) {
//												if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
//													MSGUtil.showOperSuccess();
//													Criteria c = shpmTable.getCriteria();
//													c = c==null?new Criteria():c;
//													c.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
//													shpmTable.discardAllEdits();
//													shpmTable.invalidateCache();
//													shpmTable.fetchData(c, new DSCallback() {
//														
//														@Override
//														public void execute(DSResponse response, Object rawData, DSRequest request) {
//															
//														}
//													});
//												}else{
//													MSGUtil.sayError(result.substring(2));
//												}
//											}
//											
//											@Override
//											public void onFailure(Throwable caught) {
//												MSGUtil.sayError(caught.getMessage());
//											}
//										});
//										
//									}
//									
//									@Override
//									public void onFailure(Throwable caught) {
//										MSGUtil.sayError(caught.getMessage());
//									}
//
//								});
//							}
//						}
//					});
//				}
//			});
//	    }
	    
//	    if(shpmTable!=null){
//	    	shpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
//				
//				@Override
//				public void onShowContextMenu(ShowContextMenuEvent event) {
//					menu.showContextMenu();
//	                event.cancel();
//				}
//			});
//	    }
		
		createbottoInfo();
	
	if(isPrivilege(TrsPrivRef.TRACK_P3)) {
		Tab tab2 = new Tab("提货装车");//提货装车
		VLayout recivelay = new VLayout();
		recivelay.addMember(createLoadInfo());
		tab2.setPane(recivelay);
		loadBtnWidget(loadtoolStrip);
		recivelay.addMember(loadtoolStrip);
		bottomTabSet.addTab(tab2);
		
	 }
	/*if(true) {
		Tab tab = new Tab("GPS地图");//提货装车
		
		HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContents("<iframe id=\"mytest\" src=\"mapTest.html\" style=\"width:100%;\"></iframe>"); 
        
		VLayout recivelay = new VLayout();
		recivelay.addMember(htmlPane);
		tab.setPane(recivelay);
		bottomTabSet.addTab(tab);
		
	 }*/
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		layOut.setHeight("100%");
		layOut.addMember(stack);
		layOut.addMember(bottomTabSet);
		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabSelect = event.getTabNum();
				selectHander(tabSelect);
				
			}
		});
		
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(layOut);
		
		return main;
	}
	
    private Canvas createLoadList() {
    	VLayout vlay=new VLayout();
    	loadTable.setShowFilterEditor(false);
    	loadTable.setShowRowNumbers(true);
    	loadTable.setCanEdit(false);
    	loadTable.setSelectionType(SelectionStyle.SINGLE);
    	
    	boolean isDigitCanEdit = false;
		if(ObjUtil.ifNull(canModify,"N").equals("Y")) {
			isDigitCanEdit = true;
		}
    	ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),90);//调度单编号
		LOAD_NO.setShowGridSummary(true);
		LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);//车牌号
		
		ListGridField STATUS=new ListGridField("STATUS",Util.TI18N.STATUS(),50);
		STATUS.setHidden(true);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//状态
		ListGridField DISPATCH_STAT_NAME = new ListGridField("DISPATCH_STAT_NAME", Util.TI18N.DISPATCH_STAT_NAME(), 60);  //配车状态
		
		ListGridField LOAD_STAT=new ListGridField("LOAD_STAT",Util.TI18N.LOAD_STAT());//装车状态
		LOAD_STAT.setHidden(true);
		ListGridField LOAD_STAT_NAME=new ListGridField("LOAD_STAT_NAME","装车状态",60);
//		LOAD_STAT_NAME.setHidden(true);
		
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYP(),60);//车辆类型
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),70);
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField START_AREA = new ListGridField("START_AREA_NAME",Util.TI18N.LOAD_AREA_NAME(),60);//起点区域
		START_AREA_ID.setHidden(true);
		Util.initArea(loadTable,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField END_AREA = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA(),60);//终点区域
		END_AREA_ID.setHidden(true);
		Util.initArea(loadTable,END_AREA, "END_AREA_ID", "END_AREA_NAME", "");
		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		Util.initDateTime(loadTable,DEPART_TIME);
		
		ListGridField DONE_TIME = new ListGridField("DONE_TIME","预计回场时间", 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		Util.initDateTime(loadTable,DONE_TIME);
		
		ListGridField REMAIN_GROSS_W = new ListGridField("REMAIN_GROSS_W","余量",50);//余量
		REMAIN_GROSS_W.setCanEdit(false);
		
		final ListGridField UDF1 = new ListGridField("UDF1",Util.TI18N.LOAD_UDF21(),65);//随车特服
		
		ListGridField UDF2 = new ListGridField("UDF2", Util.TI18N.LOAD_UDF22(), 85);  //电话
		
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 80);  //供应商
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_ID", Util.TI18N.SUPLR_NAME(), 70);  //供应商
		Util.initOrgSupplier(SUPLR_NAME, "");
		ListGridField DRIVER1 = new ListGridField("DRIVER1", Util.TI18N.DRIVER1(), 50);  //司机
		ListGridField MOBILE1 = new ListGridField("MOBILE1", Util.TI18N.MOBILE(), 85);  //电话
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","总数量",50);//总数量
		TOT_QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY.setShowGroupSummary(true); 
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_QNTY", Util.TI18N.TOT_QNTY()));
		}
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),50);//总数量
		TOT_QNTY_EACH.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY_EACH.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY_EACH.setShowGroupSummary(true); 
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		TOT_QNTY_EACH.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY_EACH.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_QNTY_EACH", Util.TI18N.R_EA()));
		}
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),60);//总毛重
		TOT_GROSS_W.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_GROSS_W.setSummaryFunction(SummaryFunctionType.SUM); 
		TOT_GROSS_W.setShowGroupSummary(true); 
		TOT_GROSS_W.setAlign(Alignment.RIGHT);
		TOT_GROSS_W.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_GROSS_W.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_GROSS_W", Util.TI18N.TOT_GROSS_W()));
		}
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),60);//总体积
		//TOT_VOL.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER); 
		TOT_VOL.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_VOL.setAlign(Alignment.RIGHT);
		TOT_VOL.setShowGroupSummary(true); 
		TOT_VOL.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_VOL.addEditorExitHandler(new ChangedTotalQntyAction(loadTable, "TOT_VOL", Util.TI18N.TOT_VOL()));
		}
		final ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),65);//备注
		
		loadTable.setFields(LOAD_NO,STATUS,STATUS_NAME,LOAD_STAT,LOAD_STAT_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME, DONE_TIME, UDF1,UDF2,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);
		
		listLoadItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listLoadItem.setItems(loadTable);
		listLoadItem.setExpanded(true);
		listLoadItem.setControls(new SGPage(loadTable,true).initPageBtn());
		
		sectionLoadStack=new SectionStack();
		sectionLoadStack.addSection(listLoadItem);
		sectionLoadStack.setWidth("100%");
		loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(ObjUtil.isNotNull(event.getRecord())){
					String LOAD_STAT=event.getRecord().getAttribute("LOAD_STAT");
					String STATUS=event.getRecord().getAttribute("STATUS");
					if(StaticRef.LOADED.equals(LOAD_STAT)){
						if(!(StaticRef.TRANS_PART_UNLOAD.equals(STATUS) || StaticRef.TRANS_UNLOAD.equals(STATUS))){
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,true);
						}else{
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
						}
					}else{
//						if(StaticRef.TRANS_DEPART.equals(STATUS)){
						if(Integer.valueOf(STATUS)>=Integer.valueOf(StaticRef.TRANS_PART_DEPART)
								&& Integer.valueOf(STATUS)<=Integer.valueOf(StaticRef.TRANS_DEPART)){
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
						}else{
							setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
						}
					}
				}else{
					setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
					setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
				}
			}
		});
		loadTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				panel1.editRecord(event.getRecord());
			}
		});
		
		vlay.addMember(sectionLoadStack);
		return vlay;
	}

	private Canvas createShmpList() {
    	VLayout vlay=new VLayout();
    	shpmTable = new SGTable(shpmDS,"100%","100%"){			
			public DataSource getRelatedDataSource(ListGridRecord record) {
				
				shpmlstDS = ShpmDetailQSDS.getInstance("V_SHIPMENT_ITEM_QS", "TRANS_SHIPMENT_ITEM");
				shpm_no = record.getAttributeAsString("SHPM_NO");
                return shpmlstDS; 
            }
			
			protected Canvas getExpansionComponent(final ListGridRecord record) {    
				  
                VLayout layout = new VLayout();  
                
                shpmlstTable = new SGTable();
                shpmlstTable.setDataSource(getRelatedDataSource(record));
                shpmlstTable.setWidth("45%");
                shpmlstTable.setHeight(50);
                shpmlstTable.setShowFilterEditor(false);
                shpmlstTable.setShowAllRecords(true);
                shpmlstTable.setAutoFetchData(false);
                shpmlstTable.setCanEdit(true);
                shpmlstTable.setAutoFitData(Autofit.VERTICAL);
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("SHPM_NO", shpm_no);
		        	
		        //作业单明细列表
		    	/** 作业单明细列表						
		        	行号，货品代码，货品名称，规格型号，单位，订单数量，本单量，发货数量，收货数量，毛重[吨]，体积[方]						
		    	 */						
        		ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),45);
        		SHPM_ROW.setCanEdit(false);
        		ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),80);//						
        		SKU_ID.setCanEdit(false);						
        		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);//						
        		SKU_NAME.setCanEdit(false);						
        		ListGridField UOM = new ListGridField("UOM",Util.TI18N.UNIT(),50);//						
        		UOM.setCanEdit(false);						
        		
        		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),60);//本单量，						
        		QNTY.setAlign(Alignment.RIGHT);
        		QNTY.setCanEdit(false);
        		
        		ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.FOLLOW_LD_QNTY(),60);//						
        		LD_QNTY.setAlign(Alignment.RIGHT);
        		LD_QNTY.setCanEdit(true);
        		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);//签收数量						
        		UNLD_QNTY.setAlign(Alignment.RIGHT);
        		UNLD_QNTY.setCanEdit(false);
        		ListGridField G_WGT = new ListGridField("UNLD_GWGT",Util.TI18N.G_WGT(),80);//		签收重量				
        		G_WGT.setAlign(Alignment.RIGHT);
        		G_WGT.setCanEdit(true);
        		ListGridField VOL = new ListGridField("UNLD_VOL",Util.TI18N.VOL(),80);//  签收体积						
        		VOL.setAlign(Alignment.RIGHT);	
        		VOL.setCanEdit(true);
        		
        		ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),50);
        		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);	
        		TOT_QNTY_EACH.setCanEdit(false);
        		
        		//关于 作业单明细  小数为数的处理
        		/*if(ODR_QNTY != null){
        			Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
        		}
        		if(QNTY != null){
        			Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
        		}
        		
        		if(LD_QNTY != null){
        			Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
        		}
        		if(UNLD_QNTY != null){
        			Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
        		}
        		
        		if(G_WGT != null){
        			Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
        		}
        		if(VOL != null){
        			Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
        		}
        		if(TOT_QNTY_EACH != null){
        			Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
        		}*/
        		
        		shpmlstTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,						
        				UOM,QNTY,TOT_QNTY_EACH,				
        				LD_QNTY,UNLD_QNTY,G_WGT,VOL);				
        		
        		//详细列表查询记录回调
        		shpmlstTable.fetchData(findValues);
        		
                layout.addMember(shpmlstTable);
                layout.setLayoutLeftMargin(35);
                
                return layout;   
               } 		
			
		};
		shpmentGrid(shpmTable);	
		shpmTable.setCanExpandRecords(true);
		shpmTable.setShowFilterEditor(false);
		shpmTable.setCanEdit(false);
		
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(shpmTable);
		listItem.setExpanded(true);
		listItem.setControls( new SGPage(shpmTable, true).initPageBtn());
		
		sectionStack = new SectionStack();
		sectionStack.addSection(listItem);
		sectionStack.setWidth("100%");
		
		vlay.addMember(sectionStack);
		
		//showID();
		
		return vlay;
	}

	private void selectHander(final int tabSelect){
    	
    	Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("SHPM_NO", LoadNo);
    	if(tabSelect ==2){
			damageTable.invalidateCache();
			damageTable.fetchData(crit);
    	}

    	shpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				panel1.getItem("START_LOAD_TIME").setValue(getCurTime().toString());
				Criteria crit = new Criteria();
				crit.addCriteria("OP_FLAG", "M");
				Record rec = event.getRecord();
				ListGridRecord[] record = shpmTable.getSelection();
//				ListGridRecord[] recordLoad=loadTable.getSelection();
				loadReocrd = record;				
				
				if(record.length > 0){
					Record reco = shpmTable.getSelectedRecord();
					if(StaticRef.SHPM_UNLOAD.equals(reco.getAttribute("STATUS"))){
						setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
						setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,true);
					}
				}
				if (tabSelect == 0){
					if(record.length > 0){
						for(int i = 0;i<record.length ; i++){
							if(Integer.parseInt(record[i].getAttribute("STATUS")) >= Integer.parseInt(StaticRef.SHPM_DIPATCH)
								&& Integer.parseInt(record[i].getAttribute("STATUS")) < Integer.parseInt(StaticRef.SHPM_UNLOAD)){
								if(ObjUtil.ifNull(record[i].getAttribute("LOAD_STAT"),StaticRef.NO_LOAD_NAME).equals(StaticRef.NO_LOAD_NAME)) {
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,true);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);	
								}
								else {
									setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
									setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,true);
								}
							}
							else {
								setButtonEnabled(TrsPrivRef.TRACK_P3_01,finishButton,false);
								setButtonEnabled(TrsPrivRef.TRACK_P3_02,cancelLoadButton,false);
							}
						}
					}
				}
				else if(tabSelect== 1){
					if(record.length > 0){
						groupTable1.fetchData(crit , new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								LoginCache.setPageResult(groupTable1, new FormItem(), new FormItem());
							}
						});
					} 
				}else if (tabSelect == 2){
					StringBuffer sf1 = new StringBuffer();
					StringBuffer sf2 = new StringBuffer();
					
					if(event.getState() == true){
						re = event.getRecord();
					}
					if(record.length > 1){
						int length = record.length-1;
						if(!(re.getAttribute("LOAD_NO")).equals(record[0].getAttribute("LOAD_NO"))
								&&(re.getAttribute("LOAD_NO")).equals(record[length].getAttribute("LOAD_NO"))){
								MSGUtil.sayError("必须选择同一车的作业单！ ");
								shpmTable.deselectRecord(re);
								return;
							}
						
						for(int i = 0;i<loadReocrd.length ; i++){
							if(StaticRef.SHPM_LOAD.equals(record[i].getAttribute("STATUS"))){
								sf1.append(record[i]);
							}else if(StaticRef.SHPM_UNLOAD.equals(record[i].getAttribute("STATUS"))){
								sf2.append(record[i]);
							}
						}
						
						
						if(sf1.length() > 1 && sf2.length()==0){
							
	//						recepitButton.enable();             wangjun  2010-3-8
	//						canReceButton.disable(); 
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
						}else if(sf2.length() > 1 && sf2.length() == 0){
	//						recepitButton.disable();
	//						canReceButton.enable();
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,true);
							
						}else if(sf2.length() > 1 && sf1.length() > 1){
	//						recepitButton.enable();
	//						canReceButton.enable();
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
						}
						
					}else if(record.length == 1){
						if(StaticRef.SHPM_LOAD.equals(record[0].getAttribute("STATUS"))){
	//						recepitButton.enable(); 
	//						canReceButton.disable();
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
						}else if(StaticRef.SHPM_UNLOAD.equals(record[0].getAttribute("STATUS"))){
							
	//						recepitButton.disable();
	//						canReceButton.enable();
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,true);
						}else {
	//						recepitButton.disable();
	//						canReceButton.disable();
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
							return;
						}
						
					}else {
	//					recepitButton.disable();
	//					canReceButton.disable();
						
						setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
						setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
					}
					}
				else{
					crit.addCriteria("SHPM_NO", rec.getAttribute("SHPM_NO"));
					damageTable.invalidateCache();
					damageTable.fetchData(crit);
				}
				panel1.editRecord(event.getRecord());
			}
		});
    }
	private void createbottoInfo() { //在途跟踪  国际化
		groupTable1 = new SGTable(ds);
		groupTable1.setShowFilterEditor(false);
		groupTable1.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable1.setShowRowNumbers(true);
		
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC", Util.TI18N.CURRENT_LOC(), 200);
		CURRENT_LOC.setTitle(ColorUtil.getRedTitle(Util.TI18N.CURRENT_LOC()));
		ListGridField INFORMATION = new ListGridField("INFORMATION",Util.TI18N.INFORMATION(),100);
		final ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME", Util.TI18N.PRE_UNLOAD_TIME(), 120);	
		
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT(), 100);
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setDefaultValue("5FB42E7D159346C395A2A34E0FE698C1");
		
		ListGridField ABNOMAL_NOTE = new ListGridField("ABNOMAL_NOTE", Util.TI18N.ABNOMAL_NOTE(), 90);
		
		final ListGridField PRE_SOLVE_TIME = new ListGridField("PRE_SOLVE_TIME",Util.TI18N.PRE_SOLVE_TIME(), 120);
		
        final ListGridField SOLVE_TIME = new ListGridField("SOLVE_TIME", Util.TI18N.SOLVE_TIME(), 120);
		
		ListGridField SOLUTION = new ListGridField("SOLUTION", Util.TI18N.SOLUTION(), 70);
		
		ListGridField TRACER = new ListGridField("TRACER", Util.TI18N.TRACER(), 70);
		
		ListGridField TRACE_TIME = new ListGridField("TRACE_TIME", Util.TI18N.TRACE_TIME(), 120);//
		TRACE_TIME.setCanEdit(false);

		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),80);
		EXEC_ORG_ID_NAME.setCanEdit(false);
		
		//ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),50);
		
		//ListGridField DRIVER1 = new ListGridField("DRIVER",Util.TI18N.DRIVER1(),50);//						
								
		//ListGridField MOBILE1 = new ListGridField("MOBILE",Util.TI18N.MOBILE(),80);//	
		
		groupTable1.setFields(CURRENT_LOC,INFORMATION, TRACER, TRACE_TIME, ABNOMAL_STAT, ABNOMAL_NOTE, 
				PRE_SOLVE_TIME,SOLVE_TIME, SOLUTION,EXEC_ORG_ID_NAME);
		//groupTable1.setFields(CURRENT_LOC,INFORMATION, TRACER, TRACE_TIME,PLATE_NO,DRIVER1,MOBILE1,PRE_UNLOAD_TIME, ABNOMAL_STAT, ABNOMAL_NOTE, 
		//		PRE_SOLVE_TIME,SOLVE_TIME, SOLUTION,EXEC_ORG_ID_NAME);
		
		groupTable1.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
			}
		});
		Util.initDateTime(groupTable1,PRE_UNLOAD_TIME);
		Util.initDateTime(groupTable1,PRE_SOLVE_TIME);
		Util.initDateTime(groupTable1,SOLVE_TIME);

	}

	/**
	 * 提货装车页签
	 * @author Administrator
	 * @return
	 */
	private Canvas createLoadInfo() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("40%");
		panel1 = new SGPanel();
		
		SGText ARRIVE_WHSE_TIME = new SGText("ARRIVE_WHSE_TIME",ColorUtil.getRedTitle("到场时间"));//完成装货时间
		//ARRIVE_WHSE_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel1,ARRIVE_WHSE_TIME);
		
		SGText START_LOAD_TIME = new SGText("START_LOAD_TIME",ColorUtil.getRedTitle(Util.TI18N.START_LOAD_TIME()));//开始装货时间
		//START_LOAD_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel1,START_LOAD_TIME);	
		
		SGText END_LOAD_TIME = new SGText("END_LOAD_TIME",ColorUtil.getRedTitle("完成装货时间"));//完成装货时间
		//END_LOAD_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel1,END_LOAD_TIME);
		
		SGText UDF3 = new SGText("UDF3", Util.TI18N.LOAD_UDF1(),true);//开门温度
		SGText UDF4 = new SGText("UDF4", Util.TI18N.LOAD_UDF2());//关门温度 
		UDF4.setVisible(false);
//		VEH_POS=new SGText("VEH_POS",ColorUtil.getRedTitle("车位"));
//		VEH_POS.setValue("1");

		SGText OP_PICKUP_TIME = new SGText("OP_PICKUP_TIME",ColorUtil.getRedTitle("系统更新时间"),true);//完成装货时间
		OP_PICKUP_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel1,OP_PICKUP_TIME);
		OP_PICKUP_TIME.setDisabled(true);
//		OP_SHPM=new SGCheck("OP_SHPM","操作作业单");
//		OP_SHPM.setType(type)
//		OP_SHPM.addChangedHandler(new ChangedHandler() {
//			@Override
//			public void onChanged(ChangedEvent event) {
//				OP_LOAD.setValue(!OP_LOAD.getValueAsBoolean());
//			}
//		});
//		OP_LOAD=new SGCheck("OP_LOAD","操作调度单");
//		OP_LOAD.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				OP_SHPM.setValue(!OP_SHPM.getValueAsBoolean());
//			}
//		});
		
		// 5：备注
		SGLText LOAD_NOTES = new SGLText("LOAD_NOTES", Util.TI18N.NOTES());
//		LOAD_NOTES.setColSpan(4);
//		LOAD_NOTES.setHeight(30);
//		LOAD_NOTES.setWidth(FormUtil.longWidth);
//		LOAD_NOTES.setTitleOrientation(TitleOrientation.TOP);
//		LOAD_NOTES.setTitleVAlign(VerticalAlignment.TOP);

		
		panel1.setWidth("100%");
		panel1.setItems(ARRIVE_WHSE_TIME,START_LOAD_TIME, END_LOAD_TIME, UDF3, UDF4, LOAD_NOTES,OP_PICKUP_TIME);
		hLayout.addMember(panel1);
			
		return hLayout;
		
	}
    
    public void loadBtnWidget(ToolStrip loadtoolStrip){
    	loadtoolStrip.setWidth("100%");
    	loadtoolStrip.setHeight("20");
    	loadtoolStrip.setPadding(2);
    	loadtoolStrip.setSeparatorSize(12);
    	loadtoolStrip.addSeparator();
    	loadtoolStrip.setMembersMargin(4);
    	loadtoolStrip.setAlign(Alignment.LEFT);

		//确认签收按钮
    	finishButton = createUDFBtn(Util.TI18N.FINISH_LOAD(), StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P3_01);
  
    	finishButton.addClickHandler(new FinishedLoadAction(this,false));
        
      //取消签收按钮
    	cancelLoadButton = createUDFBtn("取消装货", StaticRef.ICON_CANCEL,TrsPrivRef.TRACK_P3_02);
    	cancelLoadButton.disable();
    	cancelLoadButton.addClickHandler(new CancleLoadAction(this));
    	
    	IButton saveButton = createUDFBtn(Util.BI18N.SAVE(),StaticRef.ICON_SAVE,TrsPrivRef.LOADJOB_P0_04);
    	saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String tab_flag=TabSet.getSelectedTab().getID();
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				if("0_pickloadview".equals(tab_flag)){
					if(shpmTable.getSelectedRecord()==null){
						SC.say("请选择作业单信息!");
						return;
					}
					sf = new StringBuffer();
					sf.append("update TRANS_SHIPMENT_HEADER set");
					if(ObjUtil.isNotNull(panel1.getItem("ARRIVE_WHSE_TIME").getValue())){
						sf.append(" ARRIVE_WHSE_TIME=to_date('"+panel1.getItem("ARRIVE_WHSE_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					if(ObjUtil.isNotNull(panel1.getItem("START_LOAD_TIME").getValue())){
						sf.append(" START_LOAD_TIME=to_date('"+panel1.getItem("START_LOAD_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					if(ObjUtil.isNotNull(panel1.getItem("END_LOAD_TIME").getValue())){
						sf.append(" END_LOAD_TIME=to_date('"+panel1.getItem("END_LOAD_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					sf.append("EDITTIME=sysdate");
					sf.append(" where ID='"+shpmTable.getSelectedRecord().getAttribute("ID")+"'");
				}else if("1_pickloadview".equals(tab_flag)){
					if(loadTable.getSelectedRecord()==null){
						SC.say("请选择调度单信息!");
						return;
					}
					sf = new StringBuffer();
					sf.append("update TRANS_LOAD_HEADER set");
					if(ObjUtil.isNotNull(panel1.getItem("ARRIVE_WHSE_TIME").getValue())){
						sf.append(" ARRIVE_WHSE_TIME=to_date('"+panel1.getItem("ARRIVE_WHSE_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					if(ObjUtil.isNotNull(panel1.getItem("START_LOAD_TIME").getValue())){
						sf.append(" START_LOAD_TIME=to_date('"+panel1.getItem("START_LOAD_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					if(ObjUtil.isNotNull(panel1.getItem("END_LOAD_TIME").getValue())){
						sf.append(" END_LOAD_TIME=to_date('"+panel1.getItem("END_LOAD_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
					}
					sf.append("EDITTIME=sysdate");
					sf.append(" where ID='"+loadTable.getSelectedRecord().getAttribute("ID")+"'");
				}
				sqlList.add(sf.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
						}else {
							MSGUtil.sayError(result);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		});
        
        loadtoolStrip.setMembersMargin(4);
        loadtoolStrip.setMembers(finishButton,cancelLoadButton,saveButton);
		
    }
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createUDFBtn(Util.BI18N.SEARCHSHPM(),StaticRef.ICON_SEARCH,TrsPrivRef.LOADJOB_P2);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				TabSet.selectTab("0_pickloadview");
				if(searchLoadWin!=null){
					searchLoadWin.hide();
				}
				if(searchWin==null){
				   searchForm=new SGPanel();
					//yuanlei 2012-09-13 数据源传递错误
				    //searchWin = new SearchWin(ds, createSerchForm(searchForm),
					//		sectionStack.getSection(0)).getViewPanel();
					searchWin = new SearchWin(shpmDS, createSerchForm(searchForm),sectionStack.getSection(0)).getViewPanel();
					//yuanlei
//					searchWin.setWidth(616);
					searchWin.setHeight(365);
					//showID();
				}else{
					searchWin.show();
				}
				
			}
		});
		IButton searchLoadButton=createUDFBtn(Util.BI18N.SEARCHLOAD(),StaticRef.ICON_SEARCH,TrsPrivRef.LOADJOB_P4);
		searchLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				TabSet.selectTab("1_pickloadview");
				if(searchWin!=null){
					searchWin.hide();
				}
				if(searchLoadWin==null){
					searchLoadForm=new SGPanel();
					searchLoadForm.setDataSource(loadDS);
				    searchLoadWin = new SearchWin(loadDS, createSerchLoadForm(searchLoadForm),
					sectionLoadStack.getSection(0)).getViewPanel();
				    searchLoadWin.setHeight(365);
				    searchLoadWin.setWidth(616);
				}
				else{
					searchLoadWin.show();
				}			
				
			}
		});
		
	    //导出按钮
	    IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.TRACK_P0_01);
	    expButton.addClickHandler(new ExportAction(shpmTable, "addtime desc"));
	    expButton.setVisible(false);
	    
	    toolStrip.setMembersMargin(2);
	    toolStrip.setMembers(searchButton,searchLoadButton,expButton);
	}
	
	
	protected DynamicForm createSerchLoadForm(SGPanel form) {
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, "");
	
		SGText LOAD_NO=new SGText("LOAD_NO",Util.TI18N.LOAD_NO());//调度单号

		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		  
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());//

		//2
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM", Util.TI18N.STATUS(),true);//状态 从 到 
		SGCombo STATUS_TO=new SGCombo("STATUS_TO", "到");
		Util.initStatus(STATUS_FROM, StaticRef.LOADNO_STAT, "35");
		Util.initStatus(STATUS_TO, StaticRef.LOADNO_STAT, "40");
		
		//二级窗口 SUPLR_ID_NAME
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO",Util.TI18N.PLATE_NO());//

		//3
		ComboBoxItem START_AREA=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());//起点区域
		START_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem START_AREA_ID=new TextItem("START_AREA_ID", Util.TI18N.START_ARAE());
		Util.initArea(START_AREA, START_AREA_ID);
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());//
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		Util.initArea(END_AREA, END_AREA_ID);

		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		
		//4
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME()); 
		
		SGText ADDWHO=new SGText("ADDWHO",Util.TI18N.ADDWHO());//制单人
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());// 包含下级机构	
		C_ORG_FLAG.setWidth(120);
//		C_ORG_FLAG.setColSpan(3);
		C_ORG_FLAG.setValue(true);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME", "", " order by show_seq asc");
		
		SGCombo LOAD_STAT=new SGCombo("LOAD_STAT","装车状态");
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("10", "未装车");
		map.put("15", "部分装车");
		map.put("20", "已装车");
		LOAD_STAT.setValueMap(map);
		
		form.setItems(CUSTOMER,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,
				START_AREA,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,
				EXEC_ORG_ID,EXEC_ORG_ID_NAME,
				ROUTE_ID,
				ADDWHO,LOAD_STAT,C_ORG_FLAG);
		return form;
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		form.setDataSource(shpmDS);
		form.setAutoFetchData(false);
//		form.setWidth100();
//		form.setCellPadding(2);
		//final String pre_time = getPreDay().toString();
//		final String time = getCurTime().toString();              //wangjun 
		
		
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
//		CUSTOM_ODR_NO.setWidth(128);
		
		SGText SHPM_NO =new SGText("SHPM_NO",Util.TI18N.SHPM_NO() );
//		SHPM_NO.setWidth(128);//作业单号
		
		SGText LOAD_NO =new SGText("LOAD_NO",Util.TI18N.LOAD_NO() );
//		LOAD_NO.setWidth(128);//调度单号
		
		//调度单状态 从
		SGCombo STATUS_FROM = new SGCombo("PLAN_STAT_FROM","作业单状态从");
		SGCombo STATUS_TO = new SGCombo("PLAN_STATUS_TO",Util.TI18N.STATUS_TO());
		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT,StaticRef.SHPM_DIPATCH);
		Util.initStatus(STATUS_TO,StaticRef.SHPMNO_STAT,StaticRef.SHPM_LOAD);
//		STATUS_FROM.setWidth(128);
//		STATUS_TO.setWidth(128);
		
		//发运时间 从END_LOAD_TIME
		SGText PRE_LOAD_TIME_FROM = new SGText("PRE_LOAD_TIME_FROM","要求提货时间从");
		SGText PRE_LOAD_TIME_TO = new SGText("PRE_LOAD_TIME_TO", "到");
		Util.initDateTime(searchForm,PRE_LOAD_TIME_FROM);
		Util.initDateTime(searchForm,PRE_LOAD_TIME_TO);
		PRE_LOAD_TIME_FROM.setDefaultValue(getCurInitDay());
		//END_LOAD_TIME_TO.setDefaultValue(getCurTime());
//		END_LOAD_TIME_FROM.setWidth(128);
//		END_LOAD_TIME_TO.setWidth(128);
		//预达时间 从PRE_UNLOAD_TIME
		final SGText PRE_UNLOAD_TIME_FROM = new SGText("PRE_UNLOAD_TIME_FROM", Util.TI18N.PRE_UNLOAD_TIME());
		final SGText PRE_UNLOAD_TIME_TO = new SGText("PRE_UNLOAD_TIME_TO","到");
		//PRE_UNLOAD_TIME_FROM.setDefaultValue(pre_time);//wangjun 2010-4-24
		//PRE_UNLOAD_TIME_TO.setDefaultValue(time);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_TO);
		PRE_UNLOAD_TIME_FROM.setWidth(128);
//		PRE_UNLOAD_TIME_TO.setWidth(128);
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initSupplier(SUPLR_NAME, "");//供应商
//		SUPLR_NAME.setWidth(128);
//		Util.initCodesComboValue(SUPLR_NAME,"BAS_SUPPLIER", "ID","SUPLR_CNAME");
		
		SGText PLATE_NO =new SGText("PLATE_NO",Util.TI18N.PLATE_NO() );
		LOAD_NO.setWidth(128);	//车牌号
//		PLATE_NO.setWidth(128);
		
		//起点区域
		final TextItem AREA_ID = new TextItem("AREA_ID");
		AREA_ID.setVisible(false);
		
		SGLText UNLOAD_NAME = new SGLText("END_UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		UNLOAD_NAME.setColSpan(4);
//		UNLOAD_NAME.setWidth(264);
		
		//SGLText LOAD_NAME = new SGLText("LOAD_NAME", Util.TI18N.LOAD_NAME(),true);//发货方
		//LOAD_NAME.setColSpan(4);
		//LOAD_NAME.setWidth(264);
		
		SGCombo TEMPERATURE=new SGCombo("REFENENCE4","温区");
		Util.initCodesComboValue(TEMPERATURE,"STOR_COND");
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());//执行机构
		EXEC_ORG_ID.setColSpan(2);
//		EXEC_ORG_ID.setWidth(128);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
//		SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
//		HISTORY_FLAG.setColSpan(1);
	
		//yuanlei 2012-12-7
		//SGText CUSTOMER=new SGText("CUSTOMER_NAME",Util.TI18N.CUSTOMER());//客户   二级窗口 ？？
		//CUSTOMER.setWidth(128);
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER());
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
//		CUSTOMER.setWidth(128);
		//yuanlei
		
		/*SGCombo ABNOMAL_STAT = new SGCombo("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT());//运输异常
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setWidth(128);*/
		
		SGText UNLOAD_TIME_FROM = new SGText("UNLOAD_TIME_FROM", "到货时间  从");//
		SGText UNLOAD_TIME_TO = new SGText("UNLOAD_TIME_TO", "到");//
		Util.initDateTime(searchForm,UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,UNLOAD_TIME_TO);
//		UNLOAD_TIME_FROM.setWidth(128);
//		UNLOAD_TIME_TO.setWidth(128);
		
	    SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());//托运单号
	    SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
	    
	    LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
	    map.put("", "");
	    map.put("未装车", "未装车");
	    map.put("已装车", "已装车");
	    SGCombo LOAD_STAT=new SGCombo("LOAD_STAT","装车状态");
//	    STATUS.setVisible(false);
//	    SGCombo STATUS_NAME=new SGCombo("STATUS_NAME","装车状态");
//	    Util.initStatus(LOAD_STAT, StaticRef.LOAD_STAT, StaticRef.NO_LOAD);
//	    Util.initStatus(STATUS_NAME, StaticRef.TRANS_LOAD_JOB, StaticRef.TRANS_EXPECT_NAME);
	    LOAD_STAT.setValueMap(map);
	    LOAD_STAT.setDefaultValue("");
		
	    SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
	    Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		   
		form.setItems(CUSTOM_ODR_NO,SHPM_NO,LOAD_NO,CUSTOMER,STATUS_FROM,STATUS_TO,
				AREA_ID,PRE_LOAD_TIME_FROM,PRE_LOAD_TIME_TO,PRE_UNLOAD_TIME_FROM,PRE_UNLOAD_TIME_TO,
				UNLOAD_TIME_FROM,UNLOAD_TIME_TO,SUPLR_NAME,PLATE_NO,UNLOAD_NAME,ODR_NO,REFENENCE1,LOAD_STAT,BIZ_TYP,TEMPERATURE,
				EXEC_ORG_ID_NAME,EXEC_ORG_ID,C_ORG_FLAG);
		
		return form;
		
	}

	public void createForm(DynamicForm form) {
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		if(searchLoadWin!=null){
			searchLoadWin.destroy();
			searchLoadForm.destroy();
		}
		
		
	}
	
	private void shpmentGrid(final SGTable shpmTable){
        
        
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_SHIPMENT_HEADER运输跟踪");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_PICKLOAD);
		createListField(shpmTable, listMap);
		
		//作业单列表小数尾数处理
		ListGridField TOT_QNTY = shpmTable.getField("TOT_QNTY");
		ListGridField TOT_GROSS_W = shpmTable.getField("TOT_GROSS_W");
		ListGridField TOT_VOL = shpmTable.getField("TOT_VOL");
		if(TOT_QNTY != null){
			Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		}
		if(TOT_GROSS_W != null){
			Util.initFloatListField(TOT_GROSS_W, StaticRef.GWT_FLOAT);
		}
		if(TOT_VOL != null){
			Util.initFloatListField(TOT_VOL, StaticRef.VOL_FLOAT);
		}
	
		shpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				
				final Record clickrecord = event.getRecord();
				shpmnorecord = clickrecord;
				LoadNo = clickrecord.getAttribute("SHPM_NO");
				
			}
		});	
		//刷新签收信息form
//		shpmTable.addRecordClickHandler(new RecordClickHandler() {
//			
//			@Override
//			public void onRecordClick(RecordClickEvent event) {
////				boolean flag=OP_SHPM.getValueAsBoolean();
//				panel1.editRecord(event.getRecord());
////				OP_SHPM.setValue(flag);
////				OP_LOAD.setValue(!flag);
//			}
//		});
		
	}
	
	
	public void initVerify() {
		check_map.put("TABLE", "TRANS_LOAD_HEADER");
		check_map.put("ADDTIME",StaticRef.CHK_DATE+Util.TI18N.ADDTIME());
		check_map.put("ARRIVE_WHSE_TIME", StaticRef.CHK_DATE+"到场时间");
		check_map.put("PRE_UNLOAD_TIME", StaticRef.CHK_DATE+Util.TI18N.PRE_UNLOAD_TIME());
		check_map.put("END_LOAD_TIME", StaticRef.CHK_DATE+"完成卸货时间");
		check_map.put("TRACE_TIME", StaticRef.CHK_DATE+Util.TI18N.TRACE_TIME());
		check_map.put("SOLVE_TIME", StaticRef.CHK_DATE+Util.TI18N.SOLVE_TIME());
		check_map.put("PRE_SOLVE_TIME",StaticRef.CHK_DATE+Util.TI18N.PRE_SOLVE_TIME());
		check_map.put("MAX_PRE_UNLOAD_TIME",StaticRef.CHK_DATE+Util.TI18N.PRE_UNLOAD_TIME());
	}
	
	/*private void shpmlsEdit(boolean boo){
		if(shpmlstTable != null){
			shpmlstTable.getField("UNLD_QNTY").setCanEdit(boo);
			//系统参数，签收时作业单体积，毛重是否可编辑
			if("Y".equals(LoginCache.getParamString("RECE_QNTYCANEDIT"))){
				shpmlstTable.getField("G_WGT").setCanEdit(boo);
				shpmlstTable.getField("VOL").setCanEdit(boo);
			}
		}
	}*/
	
	public void initDMAddBtn(){
		enableOrDisables(add_dm_map, false);
		enableOrDisables(del_dm_map, false);
		enableOrDisables(save_dm_map, true);
	}
	
	public void initDMSaveBtn(){
		enableOrDisables(add_dm_map, true);
		enableOrDisables(del_dm_map, true);
		enableOrDisables(save_dm_map, false);
	}
	public void initDMCancelBtn(){
		enableOrDisables(add_dm_map, true);
		enableOrDisables(del_dm_map, false);
		enableOrDisables(save_dm_map, false);
	}
	
	/**
	 * yuanlei 2012-10-25 作业单列表增加最大化按钮
	 * @author sandy
	 * @param topTabSet
	 * @param downTabSet
	 * @return
	 */
	protected IButton createDownBtn(final HStack topLay, final TabSet downTabSet) {
		
		final IButton maxBtn = new IButton();
		maxBtn.setIcon(StaticRef.ICON_TODOWN);
		maxBtn.setTitle("");
		maxBtn.setPrompt(StaticRef.TO_MAX);
		maxBtn.setWidth(24);
        maxBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(!isDownMax) {
					topLay.setHeight("100%");
					downTabSet.setHeight("0%");		
					maxBtn.setIcon(StaticRef.ICON_TOUP);
					maxBtn.setPrompt(StaticRef.TO_NORMAL);
				}
				else {
					topLay.setHeight("55%");
					downTabSet.setHeight("45%");	
					maxBtn.setIcon(StaticRef.ICON_TODOWN);
					maxBtn.setPrompt(StaticRef.TO_MAX);
				}
				isDownMax = !isDownMax;				
			}      	
        });   
        return maxBtn;
	}
	
	public PickLoadView getThis(){
		return this;
	}
	public static native String getHourMinutes(String time_style) /*-{
	if(time_style =="pre_time"){
	    var now = new Date()-24*60*60*1000;
	} else {
		var now = new Date();
	}
	
	var year = now.getFullYear();
	var month = (now.getMonth()+1);
	var day= now.getDate();
	var hour=now.getHours();
	var minute=now.getMinutes();
	if (minute < 10) {
	    minute = "0" + minute;
	}	
	if(time_style == "pre_time"){
	   return year+'/'+month+'/'+day+' 00:00';
	} else if(time_style == "now_time"){			
	   return year+'/'+month+'/'+day + " " + hour + ':' + minute;
	}

}-*/; 
	public static native String getPreDay() /*-{
	
	var now = new Date(new Date()-24*60*60*1000);
	var year=now.getFullYear();
	var month=now.getMonth()+1;
	var day=now.getDate();	
	var res = year+"/"+month+"/"+ day + " 00:00";
	return res;

}-*/; 
	
	public static native String getCurInitDay() /*-{ 
		var now = new Date();
		var year=now.getFullYear();
		var m=now.getMonth()+1;
		var month = (m < 10) ? '0' + m : m;
		var day=now.getDate();	
		var res = year+"-"+month+"-"+ day + " 00:00";
		return res;
	}-*/;

	public static native String getCurTime() /*-{
	
		var now = new Date();
		var year=now.getFullYear();
		var m=now.getMonth()+1;
		var month = (m < 10) ? '0' + m : m;
		var day=now.getDate();
		var hour=now.getHours();
		var minute=now.getMinutes();
		if (minute < 10) {
		    minute = "0" + minute;
		}
		var res = year+"-"+month+"-"+ day + " " + hour + ":" + minute;
		return res;
	}-*/; 

	public static native void showID() /*-{

		var tt = $wnd.document.getElementById("mytest");
		alert(tt)
		alert(tt.contentWindow)
		tt.contentWindow.testMapFoo(120,30);
	}-*/;

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		PickLoadView view = new PickLoadView();
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