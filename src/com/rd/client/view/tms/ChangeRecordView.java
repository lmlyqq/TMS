package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.canConfirmChangeAction;
import com.rd.client.action.tms.changerecord.ConfirmChangeAction;
import com.rd.client.action.tms.dispatch.CancelSplitChangeAction;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ChangeDownDS;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.ds.tms.LoadShpmDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.ChangeSuplrWin;
import com.rd.client.win.ChangeVehicleWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.core.KeyIdentifier;
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
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
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
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理-->换车记录
 * @author wangjun
 *
 */
@ClassForNameAble
public class ChangeRecordView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource shpmDS;
	private DataSource downDS;
	public ValuesManager vm;
	public SGPanel panel;
	private SGPanel searchLoadForm;
	private Window searchLoadWin;
	public SGTable shpmTable;       //已调作业单表
	public SGTable loadTable;
	public SGTable downTable;
	public Record record;
	private String canVehInput;
	public ListGridRecord[] loadReocrd;
	public IButton recepitButton;
	public IButton canReceButton;
	public int tabSelect = 0;
	//public TabSet tabSet;
	private SectionStackSection loadListItem;
	private SectionStack loadSectionStack;
    private Window separateWin;
	public DynamicForm pageForm;    //待调分页FORM
	
	
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		shpmDS = LoadShpmDS.getInstance("V_SHIPMENT_HEADER_LOAD", "TRANS_SHIPMENT_HEADER");
		downDS = ChangeDownDS.getInstance("V_TRANS_CHANGE_RECORD","TRANS_CHANGE_RECORD");
		//主布局
		
		
		//ToolStrip recivetoolStrip = new ToolStrip();
		//recivetoolStrip.setAlign(Alignment.LEFT);
		//tab1 = new Tab("换成记录");
		VLayout recivelay = new VLayout();
		HLayout layout=new HLayout();
		//layout.setWidth("80%");
		DynamicForm form1=new DynamicForm();
		createLittleForm1(form1);
		DynamicForm form2=new DynamicForm();
		createLittleForm2(form2);
		layout.addMember(form1);
		layout.addMember(form2);
		layout.setHeight("30%");
		recivelay.addMember(layout);
		recivelay.addMember(createbottoInfo2());
		recivelay.setWidth("30%");
		vm.addMember(form1);
		vm.addMember(form2);
		vm.addMember(panel);
		vm.setDataSource(downDS);
		downTable = new SGTable(downDS,"70%","375",false,true,false);
		createDownTable(downTable);
		
		HLayout hlayout=new HLayout();
		hlayout.addMember(downTable);
		hlayout.addMember(recivelay);
		
		
		
		hlayout.setHeight("40%");
		
		main.addMember(createLoadList());
		main.addMember(hlayout);
		
		
		//recivelay.addMember(recivetoolStrip);
		
		initVerify(); 
		return main;
	}
 
	private Canvas createLoadList(){

    	VLayout vlay=new VLayout();
    	
    	loadTable=new SGTable(loadDS,"100%","100%",false,true,false){
    		//第二层表
			protected Canvas getExpansionComponent(final ListGridRecord record) {
				  
                VLayout layout = new VLayout();              
  
                shpmTable = new SGTable();
                shpmTable.setDataSource(shpmDS);
                shpmTable.setWidth("90%");
                shpmTable.setHeight(50);
                shpmTable.setShowFilterEditor(false);
                shpmTable.setShowAllRecords(true);
                shpmTable.setAutoFetchData(false);
                shpmTable.setAlign(Alignment.RIGHT);
                shpmTable.setShowRowNumbers(true);
                shpmTable.setCanEdit(false);
                shpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
                shpmTable.setAutoFitData(Autofit.VERTICAL);
                shpmTable.setCanDragRecordsOut(true);   
                shpmTable.setCanAcceptDroppedRecords(true);   
                shpmTable.setCanReorderRecords(true);   
                //shpmTable.setAlternateRecordStyles(true);      
                shpmTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);   
                shpmTable.initBindEvent();
               
                Criteria findValues = new Criteria();
                findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
		        findValues.addCriteria("LOAD_NO", record.getAttributeAsString("LOAD_NO"));
		        findValues.addCriteria("ALL_FLAG","true");
		        
		        LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_LOAD);
				createListField(shpmTable, listMap);
				
//        		ListGridField DEPART_TIME = shpmTable.getField("DEPART_TIME");
//        		if(DEPART_TIME != null) {
//	        		DEPART_TIME.setCanEdit(true);
//	        		DEPART_TIME.setTitle(Util.TI18N.END_LOAD_TIME());
//	        		Util.initDateTime(shpmTable,DEPART_TIME);
//        		}
//        		else {
//        			MSGUtil.sayError("列表中必须配置" + Util.TI18N.END_LOAD_TIME() + "!");
//        		}
        		shpmTable.fetchData(findValues, new DSCallback() {

					@Override
					public void execute(DSResponse response, Object rawData,
							DSRequest request) {
						Util.db_async.getPageInfo(new AsyncCallback<ArrayList<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								
							}

							@Override
							public void onSuccess(ArrayList<String> result) {
								if(result != null && result.size() > 2) {
									shpmTable.setProperty("WHERE", result.get(2));
								}
							}
						});
					}
					
				});
        		
        		final Menu load_menu = new Menu();   //调度单右键
        		load_menu.setWidth(140);
        		//MenuItemSeparator itemSeparator =new MenuItemSeparator();
        		
        		if(isPrivilege(TrsPrivRef.ChangeRecord_P0_02)) {                   
        			
	        	    if(isPrivilege(TrsPrivRef.ChangeRecord_P0_03)) {
		        	    MenuItem separateItem = new MenuItem("订单拆分",StaticRef.ICON_DEL);
		        	    load_menu.addItem(separateItem);
		        	    separateItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
							
							@Override
							public void onClick(MenuItemClickEvent event) {
								if(shpmTable != null && shpmTable.getSelection().length > 0){
									if(shpmTable.getSelection().length==1){
										separateWin=new SeparateWin(loadTable,shpmTable,getView()).getViewPanel();
										separateWin.setWidth(670);
										separateWin.setHeight(220);
										separateWin.show();
									}else{
										SC.warn("只能勾选一条数据!");
									}
								}else{
									SC.warn("未勾选作业单!");
								}
								
							}
						});
	        	    }
	    		   
	        	    if(isPrivilege(TrsPrivRef.ChangeRecord_P0_04)) {
	    			    MenuItem cansplitItem = new MenuItem(Util.BI18N.CANCELSPLIT(),StaticRef.ICON_CANCEL); 
	    			    cansplitItem.setKeyTitle("Alt+C");
	    			    KeyIdentifier cansplitKey = new KeyIdentifier();
	    			    cansplitKey.setAltKey(true);
	    			    cansplitKey.setKeyName("C");
	    			    cansplitItem.setKeys(cansplitKey);
	    			    load_menu.addItem(cansplitItem);
	    			    cansplitItem.addClickHandler(new CancelSplitChangeAction(getView(), shpmTable,loadTable));
	    			    
	    		    }
	        	  
	        	    
	        	    shpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
                    
	        	    	public void onShowContextMenu(ShowContextMenuEvent event) {
                    	
	        	    		load_menu.showContextMenu();
                        
	        	    		event.cancel();
                        
	        	    	}
                
	        	    });
	        	    
	        	    
        		}
//	        	    
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_19)) {
//		        	    MenuItem cansendItem = new MenuItem(Util.BI18N.CANCELSEND(),StaticRef.ICON_CANCEL);
//		        	    load_menu.addItem(cansendItem);
//		        	    cansendItem.addClickHandler(new ShpmCancelSendAction(shpmTable, loadTable));
//	        	    }
//	        	    
//	        	    if(isPrivilege(TrsPrivRef.DISPATCH_P2_20)) {
//	        	    	load_menu.addItem(itemSeparator);
//		        	    MenuItem saveItem = new MenuItem(Util.BI18N.SAVE(),StaticRef.ICON_SAVE);
//		        	    load_menu.addItem(saveItem);
//		        	    saveItem.addClickHandler(new ShpmSaveAction(shpmTable));
//	        	    }
//	        	    if(true) {
//	        	    	load_menu.addItem(itemSeparator);
//		        	    MenuItem expItem = new MenuItem(Util.BI18N.EXPORT(),StaticRef.ICON_EXPORT);
//		        	    load_menu.addItem(expItem);
//		        	    expItem.addClickHandler(new ExportAction(shpmTable));
//	        	    }

//        		}
        	    
        		layout.addMember(shpmTable);
        		layout.setLayoutTopMargin(5);
                layout.setLayoutLeftMargin(35);
                
                return layout;   
            }
    	};
    	loadTable.setShowRowNumbers(true);
    	loadTable.setSelectionType(SelectionStyle.SINGLE);
    	loadTable.setCanExpandRecords(true);
		//loadTable.setShowGridSummary(true);
		loadTable.setShowFilterEditor(false);
		loadTable.setCanEdit(false);
		loadTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		loadTable.initBindEvent();
		
		loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				record = event.getRecord();
				Criteria findValues = new Criteria();
	            findValues.addCriteria("LOAD_NO1", record.getAttribute("LOAD_NO"));
	            findValues.addCriteria("OP_FLAG", "M");
//	            downTable.PKEY = "LOAD_NO1";
//	            downTable.PVALUE = record.getAttribute("LOAD_NO1");
	            downTable.fetchData(findValues);
	            vm.clearValues();
	            vm.setValue("LOAD_NO1", record.getAttribute("LOAD_NO"));
	            vm.setValue("SUPLR_ID1", record.getAttribute("SUPLR_NAME"));
	            vm.setValue("PLATE_NO1", record.getAttribute("PLATE_NO"));
	            vm.setValue("VEHICLE_TYP_ID1", record.getAttribute("VEHICLE_TYP_ID"));
	            vm.setValue("DRIVER1", record.getAttribute("DRIVER1"));
	            vm.setValue("MOBILE1", record.getAttribute("MOBILE1"));
			}
		});
		
		downTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record selectedRecord  = event.getRecord();
                vm.editRecord(selectedRecord);
			}
		});
    	createTableList(loadTable,false);
		
		loadListItem = new SectionStackSection(Util.TI18N.LISTINFO());
		loadListItem.setItems(loadTable);
		loadListItem.setExpanded(true);
		pageForm=new SGPage(loadTable,true).initPageBtn();
		loadListItem.setControls(pageForm);
		
		loadSectionStack=new SectionStack();
		loadSectionStack.addSection(loadListItem);
		loadSectionStack.setWidth("100%");		
		//ToolStrip load=new ToolStrip();
		//load.setWidth("100%");
		//load.setHeight("20");
		//load.setAlign(Alignment.RIGHT);
		
		
		ToolStrip recivetoolStrip=new ToolStrip();
		reciveBtnWidget(recivetoolStrip);
		
		//load.addMember(searchLoadButton);
		vlay.setMembers(recivetoolStrip,loadSectionStack);
		vlay.setHeight("60%");
		return vlay;
	
	}
	
	private void createTableList(SGTable table,boolean stat_flag){
		boolean isDigitCanEdit = false;
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),120);//调度单编号
		LOAD_NO.setShowGridSummary(true);
		LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);//车牌号
		
		ListGridField STATUS=new ListGridField("STATUS",Util.TI18N.STATUS(),50);
		STATUS.setHidden(true);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),50);//状态
		STATUS_NAME.setHidden(stat_flag);
		ListGridField DISPATCH_STAT_NAME = new ListGridField("DISPATCH_STAT_NAME", Util.TI18N.DISPATCH_STAT_NAME(), 60);  //配车状态
		DISPATCH_STAT_NAME.setHidden(true);
		ListGridField LOAD_STAT=new ListGridField("LOAD_STAT","装车状态");//装车状态
		LOAD_STAT.setHidden(true);
		ListGridField LOAD_STAT_NAME=new ListGridField("LOAD_STAT_NAME","装车状态",60);
//		LOAD_STAT_NAME.setHidden(true);
		
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYP(),60);//车辆类型
		Util.initComboValue(VEHICLE_TYP, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " SHOW_SEQ ASC");  //车辆类型
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),100);
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		ListGridField START_AREA_ID = new ListGridField("START_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField START_AREA = new ListGridField("START_AREA_NAME",Util.TI18N.LOAD_AREA_NAME(),60);//起点区域
		START_AREA_ID.setHidden(true);
		Util.initArea(table,START_AREA,"START_AREA_ID", "START_AREA_NAME", "");
		ListGridField END_AREA_ID = new ListGridField("END_AREA_ID",Util.TI18N.AREA_NAME(),0);
		ListGridField END_AREA = new ListGridField("END_AREA_NAME",Util.TI18N.END_AREA(),60);//终点区域
		END_AREA_ID.setHidden(true);
		Util.initArea(table,END_AREA, "END_AREA_ID", "END_AREA_NAME", "");
		ListGridField DEPART_TIME = new ListGridField("DEPART_TIME", Util.TI18N.END_LOAD_TIME(), 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		Util.initDateTime(table,DEPART_TIME);
		
		ListGridField DONE_TIME = new ListGridField("DONE_TIME","预计回场时间", 110);  //发运时间
		//Util.initListGridDateTime(DEPART_TIME);
		Util.initDateTime(table,DONE_TIME);
		
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
			TOT_QNTY.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_QNTY", Util.TI18N.TOT_QNTY()));
		}
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),50);//总数量
		TOT_QNTY_EACH.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_QNTY_EACH.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_QNTY_EACH.setShowGroupSummary(true); 
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		TOT_QNTY_EACH.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_QNTY_EACH.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_QNTY_EACH", Util.TI18N.R_EA()));
		}
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),60);//总毛重
		TOT_GROSS_W.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		TOT_GROSS_W.setSummaryFunction(SummaryFunctionType.SUM); 
		TOT_GROSS_W.setShowGroupSummary(true); 
		TOT_GROSS_W.setAlign(Alignment.RIGHT);
		TOT_GROSS_W.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_GROSS_W.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_GROSS_W", Util.TI18N.TOT_GROSS_W()));
		}
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),60);//总体积
		//TOT_VOL.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER); 
		TOT_VOL.setSummaryFunction(SummaryFunctionType.SUM);
		TOT_VOL.setAlign(Alignment.RIGHT);
		TOT_VOL.setShowGroupSummary(true); 
		TOT_VOL.setCanEdit(isDigitCanEdit);
		if(isDigitCanEdit) {
			TOT_VOL.addEditorExitHandler(new ChangedTotalQntyAction(table, "TOT_VOL", Util.TI18N.TOT_VOL()));
		}
		final ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),65);//备注
		
		
		table.setFields(LOAD_NO,STATUS,STATUS_NAME,LOAD_STAT,LOAD_STAT_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME, DONE_TIME, UDF1,UDF2,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);
	}

	private void createDownTable(SGTable table){
		
		ListGridField SUPLR_ID1_NAME = new ListGridField("SUPLR_ID1_NAME","原承运商",100);
		ListGridField PLATE_NO1 = new ListGridField("PLATE_NO1","原车车牌",100);
		ListGridField VEHICLE_TYP_ID1 = new ListGridField("VEHICLE_TYP_ID1_NAME","车辆类型",80);
		ListGridField DRIVER1 = new ListGridField("DRIVER1","联系人",60);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","联系电话",100);
		ListGridField SUPLR_ID2_NAME = new ListGridField("SUPLR_ID2_NAME","目标承运商",100);
		ListGridField PLATE_NO2 = new ListGridField("PLATE_NO2","目标车牌",80);
		ListGridField VEHICLE_TYP_ID2 = new ListGridField("VEHICLE_TYP_ID2_NAME","车辆类型",80);
		ListGridField DRIVER2 = new ListGridField("DRIVER2","联系人",60);
		ListGridField MOBILE2 = new ListGridField("MOBILE2","联系电话",100);	
		ListGridField CHANGE_TIME = new ListGridField("CHANGE_TIME","换车时间",100);
		ListGridField CHANGE_AREA_ID = new ListGridField("CHANGE_AREA_ID","换车地点",80);
		ListGridField CHANGE_REASON = new ListGridField("CHANGE_REASON_NAME","换车原因",80);
		ListGridField CARD_NO2 = new ListGridField("CARD_NO2","驾驶证",80);
		
		table.setFields(SUPLR_ID1_NAME,PLATE_NO1,VEHICLE_TYP_ID1,DRIVER1,MOBILE1,SUPLR_ID2_NAME,PLATE_NO2,VEHICLE_TYP_ID2,DRIVER2,MOBILE2,CHANGE_TIME,CHANGE_AREA_ID,CHANGE_REASON,CARD_NO2);

		
	}
	
  //查询窗口
	private DynamicForm createSearchLoadForm(final DynamicForm form,boolean flag){
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, "");
	
		SGText LOAD_NO=new SGText("LOAD_NO",Util.TI18N.LOAD_NO());//调度单号

		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		  
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());//

		//2
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM", Util.TI18N.STATUS(),true);//状态 从 到 
		SGCombo STATUS_TO=new SGCombo("STATUS_TO", "到");
		Util.initStatus(STATUS_FROM, StaticRef.LOADNO_STAT, "40");
		Util.initStatus(STATUS_TO, StaticRef.LOADNO_STAT, "40");
		
		//二级窗口 SUPLR_ID_NAME
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO",Util.TI18N.PLATE_NO());//

		//3
		ComboBoxItem START_AREA=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());//起点区域
		START_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem START_AREA_ID=new TextItem("START_AREA_ID", Util.TI18N.START_ARAE());
		START_AREA_ID.setVisible(false);
		Util.initArea(START_AREA, START_AREA_ID);
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());//
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		END_AREA_ID.setVisible(false);
		Util.initArea(END_AREA, END_AREA_ID);
		
		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		
		//4
		
//		SGCombo DISPATCH_STAT= new SGCombo("DISPATCH_STAT",Util.TI18N.DISPATCH_STAT_NAME());
//		Util.initCodesComboValue(DISPATCH_STAT, "DISPATCH_STAT");//--wangjun 2010-2-27
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME()); 
		
		SGText ADDWHO=new SGText("ADDWHO",Util.TI18N.ADDWHO());//制单人
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());// 包含下级机构	
		C_ORG_FLAG.setWidth(120);
//		C_ORG_FLAG.setColSpan(3);
		C_ORG_FLAG.setValue(true);
		
		SGCheck EXEC_FLAG = new SGCheck("EXEC_FLAG", "参与执行");	
		EXEC_FLAG.setColSpan(3);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构	
		C_RDC_FLAG.setColSpan(3);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME", "", " order by show_seq asc");
		
		final SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		BIZ_TYP.setVisible(flag);
		if(flag){
			BIZ_TYP.setValue(StaticRef.B2C);
		}
		BIZ_TYP.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(BIZ_TYP)){
				// biz_typ=ObjUtil.isNotNull(BIZ_TYP.getValue())? BIZ_TYP.getValue().toString() : "";
				}
			}
		});
		
//		SGCombo LOAD_STAT = new SGCombo("LOAD_STAT","装车状态");
//		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
//		map.put("", "");
//		map.put("10", "未装车");
//		map.put("20", "已装车");
//		LOAD_STAT.setValueMap(map);
		
		form.setItems(CUSTOMER,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,START_AREA_ID,
				START_AREA,END_AREA_ID,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,BIZ_TYP,
				ROUTE_ID,ADDWHO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,EXEC_FLAG);
		return form;
	}


	
	public void createForm(DynamicForm form) {
		
	}

	public void onDestroy() {
		
		if(searchLoadWin!=null){
			searchLoadWin.destroy();
			searchLoadForm.destroy();
		}
	}

	public void initVerify() {
		check_map.put("TABLE", "V_TRANS_CHANGE_RECORD");
		check_map.put("PLATE_NO2", StaticRef.CHK_NOTNULL + "车牌");
		check_map.put("VEHICLE_TYP_ID2", StaticRef.CHK_UNIQUE + "车辆类型");
		check_map.put("DRIVER2", StaticRef.CHK_NOTNULL + "司机");
		check_map.put("MOBILE2", StaticRef.CHK_NOTNULL + "联系电话");
		check_map.put("CHANGE_TIME", StaticRef.CHK_DATE + "换车时间");
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		
	} 

	private Canvas createbottoInfo2() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("40%");
		panel = new SGPanel();
		
		//【到货签收】左布局
		SGText CARD_NO1 = new SGText("CARD_NO1","身份证");//到货签收时间
	
		SGText CARD_NO2 = new SGText("CARD_NO2", "驾驶证");//归还数量
	
		SGText CARD_NO3 = new SGText("CARD_NO3", "营运证");//损坏数量
	
		SGText CHANGE_AREA_ID = new SGText("CHANGE_AREA_ID", "换车地点");//关门温度 
		
		SGText CHANGE_TIME = new SGText("CHANGE_TIME","换车时间",true);
		Util.initDateTime(panel,CHANGE_TIME);
		
		SGCombo CHANGE_REASON = new SGCombo("CHANGE_REASON","换车原因");
		Util.initCodesComboValue(CHANGE_REASON,"CHANGE_REASON");
		// 5：备注
		TextAreaItem TRACK_NOTES = new TextAreaItem("TRACK_NOTES", "原因描述");
		TRACK_NOTES.setStartRow(true);
		TRACK_NOTES.setColSpan(6);
		TRACK_NOTES.setHeight(40);
		TRACK_NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
		TRACK_NOTES.setTitleOrientation(TitleOrientation.TOP);
		TRACK_NOTES.setTitleVAlign(VerticalAlignment.TOP);
	
		SGText LOAD_NO1 = new SGText("LOAD_NO1","");
		LOAD_NO1.setVisible(false);
		
		panel.setWidth("30%");
		panel.setItems(CARD_NO1, CARD_NO2, CARD_NO3,CHANGE_TIME,CHANGE_REASON,CHANGE_AREA_ID,TRACK_NOTES,LOAD_NO1);
		hLayout.addMember(panel);

		return hLayout;
	

	}

    public void createLittleForm1(DynamicForm form){
    	
    	SGText SUPLR_ID1 = new SGText("SUPLR_ID1","承运商");
    	SUPLR_ID1.setDisabled(true);
    	
    	SGText PLATE_NO1 = new SGText("PLATE_NO1","车牌");
    	PLATE_NO1.setDisabled(true);
    	//PLATE_NO1.setAlign(Alignment.CENTER);
    	SGCombo VEHICLE_TYP_ID1 = new SGCombo("VEHICLE_TYP_ID1","车辆类型");
    	Util.initComboValue(VEHICLE_TYP_ID1, "BAS_VEHICLE_TYPE", "ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", "");
    	VEHICLE_TYP_ID1.setDisabled(true);
    	//VEHICLE_TYP_ID1.setAlign(Alignment.CENTER);
    	SGText DRIVER1 = new SGText("DRIVER1","司机");
    	DRIVER1.setDisabled(true);
    	//DRIVER1.setAlign(Alignment.CENTER);
    	SGText MOBILE1 = new SGText("MOBILE1","联系电话");
    	MOBILE1.setDisabled(true);
    	//MOBILE1.setAlign(Alignment.CENTER);
    	form.setItems(SUPLR_ID1,PLATE_NO1,VEHICLE_TYP_ID1,DRIVER1,MOBILE1);
    	form.setIsGroup(true);  
   	    form.setGroupTitle("原始车辆");
	    form.setWidth("45%");	
	    form.setPadding(3); 
	    form.setMargin(3);
	    form.setTitleSuffix("");
	    
	  }
    
    public void createLittleForm2(final DynamicForm form){
		final SGText SUPLR_ID2 = new SGText("SUPLR_ID2", ColorUtil.getRedTitle("承运商"));
		SUPLR_ID2.setVisible(false);
		final SGText SUPLR_ID_NAME = new SGText("SUPLR_ID_NAME","承运商");
		
		SGText PLATE_NO2 = new SGText("PLATE_NO2",ColorUtil.getRedTitle("车牌"));
    	//PLATE_NO2.setAlign(Alignment.CENTER);
    	SGCombo VEHICLE_TYP_ID2 = new SGCombo("VEHICLE_TYP_ID2",ColorUtil.getRedTitle("车辆类型"));
    	Util.initComboValue(VEHICLE_TYP_ID2, "BAS_VEHICLE_TYPE", "ID","VEHICLE_TYPE", " WHERE ENABLE_FLAG = 'Y'", " order by show_seq asc");
    	//VEHICLE_TYP_ID2.setAlign(Alignment.CENTER);
    	SGText DRIVER2 = new SGText("DRIVER2",ColorUtil.getRedTitle("司机"));
    	//DRIVER2.setAlign(Alignment.CENTER);
    	SGText MOBILE2 = new SGText("MOBILE2",ColorUtil.getRedTitle("联系电话"));
    	//MOBILE2.setAlign(Alignment.CENTER);
    	
    	form.setItems(SUPLR_ID2,SUPLR_ID_NAME,PLATE_NO2,VEHICLE_TYP_ID2,DRIVER2,MOBILE2);
    	form.setIsGroup(true);  
   	    form.setGroupTitle("目标车辆");
   	    form.setWidth("45%");
   	    form.setPadding(3); 
   	    form.setMargin(3);
   	    form.setTitleSuffix("");
   	    
   	    if(ObjUtil.ifNull(canVehInput,"N").equals("N")) {
   	    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
			PLATE_NO2.setIcons(searchPicker);
//			PLATE_NO2.setShowSelectedIcon(true);
			searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
				
				@Override
				public void onFormItemClick(FormItemIconClickEvent event) {
					if(loadTable.getSelectedRecord()==null)return;
					Util.db_async.getSingleRecord("TEMPERATURE1,TEMPERATURE2,TOT_GROSS_W", "TRANS_LOAD_HEADER", 
							" where LOAD_NO = '" + loadTable.getSelectedRecord().getAttribute("LOAD_NO") + "'", null, new AsyncCallback<HashMap<String, String>>() {
								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(
										HashMap<String, String> result) {
									if(result != null && result.size() > 0) {
										HashMap<String,String> map = new HashMap<String,String>();
										String TEMPERATURE1 = ObjUtil.ifNull(result.get("TEMPERATURE1"),"");
										String TEMPERATURE2 = ObjUtil.ifNull(result.get("TEMPERATURE2"),"");
										String TOT_GROSS_W = result.get("TOT_GROSS_W");
										String tmp_attr = StaticRef.TMP_SINGLE;
										if (!TEMPERATURE1.equals(TEMPERATURE2)) {
											tmp_attr = StaticRef.TMP_DOUBLE;
										}
										map.put("TMP_ATTR", tmp_attr);
										map.put("REMAIN_GROSS_W",TOT_GROSS_W);
										map.put("AVAIL_FLAG",StaticRef.AVAIL_FLAG);
										map.put("SUPLR_ID",SUPLR_ID2.getValue().toString());
										new ChangeVehicleWin(vm,"20%", "32%", map).getViewPanel();
									}
									else {
										new ChangeVehicleWin(vm,"20%", "32%").getViewPanel();
									}
								}						
					});					
				}
			});
			
   	    }
			
			//承运商 查询按钮
   	    PickerIcon searchPickers = new PickerIcon(PickerIcon.SEARCH);
   	    SUPLR_ID_NAME.setIcons(searchPickers);
   	    searchPickers.addFormItemClickHandler(new FormItemClickHandler() {
				
   	    	@Override
   	    	public void onFormItemClick(FormItemIconClickEvent event) {
   	    		new ChangeSuplrWin(form,"20%","50%").getViewPanel();	
   	    	}
   	    });
			
   	    SUPLR_ID_NAME.addKeyDownHandler( new KeyDownHandler() {
				
   	    	@Override
   	    	public void onKeyDown(KeyDownEvent event) {
   	    		if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
   	    			final String name = ObjUtil.ifObjNull(SUPLR_ID_NAME.getValue(),"").toString().toUpperCase();
   	   	    		Util.db_async.getRecord("ID,SUPLR_CNAME,SUPLR_CODE", "V_SUPPLIER",
   	   	    				" where ENABLE_FLAG='Y' and full_index like '%"+name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
   							
   	   	    			@Override
   	   	    			public void onSuccess(ArrayList<HashMap<String, String>> result) {
   	   	    				int size = result.size();
   	   	    				HashMap<String, String> selectRecord = null;
   	   	    				if(size == 1 || selectRecord != null){
   	   	    					if(selectRecord == null)selectRecord = result.get(0);
   									form.getItem("SUPLR_ID2").setValue(selectRecord.get("ID"));
   									form.getItem("SUPLR_ID_NAME").setValue(selectRecord.get("SUPLR_CNAME"));
   								}else if(size > 1){
   									new ChangeSuplrWin(form,"20%", "32%",name).getViewPanel();
   								}
   							}
   							
   	   	    			@Override
   	   	    			public void onFailure(Throwable caught) {
   								
   	   	    			}
   	   	    		});
   	    		}
   	    	}
   	    });
		
   	    PLATE_NO2.addKeyDownHandler( new KeyDownHandler() {
		
   	    	@Override
   	    	public void onKeyDown(KeyDownEvent event) {
   	    		if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
   	    			final String plate_no = ObjUtil.ifObjNull(form.getItem("PLATE_NO2").getValue(),"").toString().toUpperCase();
   	   	    		Util.db_async.getRecord("PLATE_NO,VEHICLE_TYP_ID,DRIVER1_NAME,TRAIL_PLATE_NO,MOBILE1", "V_BAS_VEHICLE", 
   							" where SUPLR_ID = '"+form.getItem("SUPLR_ID2").getValue()+"' and upper(PLATE_NO) like upper('%"+plate_no+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

   	   	    			@Override
   	   	    			public void onFailure(Throwable caught) {
   									
   	   	    			}

   	   	    			@Override
   	   	    			public void onSuccess(ArrayList<HashMap<String, String>> result) {
   	   	    				int size = result.size();
   	   	    				HashMap<String, String> selectRecord = null;
   	   	    				if(size == 1 || selectRecord != null){
   	   	    					if(selectRecord == null)selectRecord = result.get(0);
   	   	    					form.getItem("PLATE_NO2").setValue(selectRecord.get("PLATE_NO"));
   	   	    					form.getItem("VEHICLE_TYP_ID2").setValue(selectRecord.get("VEHICLE_TYP_ID"));
   	   	    					form.getItem("DRIVER2").setValue(selectRecord.get("DRIVER1_NAME"));
   	   	    					form.getItem("MOBILE2").setValue(selectRecord.get("MOBILE1"));
   	   	    				}else if(size > 1){
   	   	    					String sup_id=ObjUtil.ifObjNull(form.getItem("SUPLR_ID2").getValue(), "").toString();
   	   	    					new ChangeVehicleWin(vm,"20%", "32%",plate_no,sup_id).getViewPanel();
   	   	    				}
   	   	    			}
   	   	    			
   	   	    		});
   	    		}
   	    		
   	    	}
   	    });
		
	}


	public void reciveBtnWidget(ToolStrip recivetoolStrip){
	
		recivetoolStrip.setWidth("100%");
		recivetoolStrip.setHeight("20");
		recivetoolStrip.setPadding(2);
		recivetoolStrip.setSeparatorSize(12);
		recivetoolStrip.addSeparator();
		recivetoolStrip.setMembersMargin(4);
		recivetoolStrip.setAlign(Alignment.RIGHT);
		
		IButton searchLoadButton=createBtn(StaticRef.FETCH_BTN,TrsPrivRef.ChangeRecord_P0);
		searchLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchLoadWin==null){
					searchLoadForm=new SGPanel();
					searchLoadForm.setDataSource(loadDS);
					searchLoadWin=new SearchWin(loadDS,createSearchLoadForm(searchLoadForm,false),loadSectionStack.getSection(0)).getViewPanel();
					searchLoadWin.setWidth(620);
					searchLoadWin.setHeight(330);
				}else{
					searchLoadWin.show();
				}
			}
		});
		
		
		IButton confirmButton = createUDFBtn("换车确认",StaticRef.ICON_CONFIRM,TrsPrivRef.ChangeRecord_P0_01);
		confirmButton.addClickHandler(new ConfirmChangeAction(loadTable,vm, this));
		
		IButton canconfirmButton = createUDFBtn("取消换车",StaticRef.ICON_CONFIRM,TrsPrivRef.ChangeRecord_P0_05);
		canconfirmButton.addClickHandler(new canConfirmChangeAction(loadTable,vm));
		
	    recivetoolStrip.setMembersMargin(4);
	    recivetoolStrip.setMembers(searchLoadButton,confirmButton,canconfirmButton);
	    
	}
	private ChangeRecordView getView() {
		return this;
	}



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

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		ChangeRecordView view = new ChangeRecordView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}
	
	@Override
	public String getCanvasID() {
		return getID();
	} 
}
