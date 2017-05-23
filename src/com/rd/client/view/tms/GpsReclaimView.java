package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.common.obj.FormUtil;
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
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.GpsEqReclaimDS;
import com.rd.client.ds.tms.ReclaimDS;
import com.rd.client.ds.tms.TempEqReclaimDS;
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
import com.smartgwt.client.types.RecordSummaryFunctionType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理-->设备回收管理
 * @author wangjun
 *
 */
@ClassForNameAble
public class GpsReclaimView extends SGForm implements PanelFactory {

	private DataSource loadDS;
	private DataSource downGpsDS;
	private DataSource downTempDS;
	public  SGTable groupTable1;
	public SGPanel panel;
	public SGPanel panel1;
	private SGPanel searchLoadForm;
	private Window searchLoadWin;
	public SGTable shpmTable;       //已调作业单表
	public SGTable loadTable;
	public SGTable downGpsTable;
	public SGTable downTempTable;
	public SGTable unloadTable;
	public SGTable shpmlstTable;    //已调作业单明细表
	public SGTable damageTable;
	public Record shpmnorecord;
	public Record grouprecords;
	public ListGridRecord[] loadReocrd;
	public IButton recepitButton;
	public IButton canReceButton;
	public IButton recepitButton1;
	public IButton canReceButton1;
	public int tabSelect = 0;
	public Record record;
	public TabSet tabSet;
	private SectionStackSection loadListItem;
	public SectionStack loadSectionStack;
	private Tab tab1;
	private Tab tab2;
	public ValuesManager vm;
	public ValuesManager vm1;
    //private String biz_typ;
	/*public GpsReclaimView(String id) {
	    super(id);
	}*/
	
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		vm = new ValuesManager();
		vm1 = new ValuesManager();

		//loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		loadDS = ReclaimDS.getInstance("V_LOAD_HEADER_Recl", "TRANS_LOAD_HEADER");
		//ds = TmsFollowDS.getInstance("V_TRANS_TRACK_TRACE","TRANS_TRACK_TRACE");
		downGpsDS = GpsEqReclaimDS.getInstance("V_BAS_GPS_RECLAIM1","BAS_GPS_RECLAIM");
		downGpsTable = new SGTable(downGpsDS,"65%","100%",false,true,false);
		downGpsTable.setCanEdit(false);	
		
		downTempDS = TempEqReclaimDS.getInstance("V_BAS_TEMP_RECLAIM1","BAS_TEMP_RECLAIM");
		downTempTable = new SGTable(downTempDS,"65%","100%",false,true,false);
		downTempTable.setCanEdit(false);	
		
		//主布局
		
		final VStack stack =new VStack();
		stack.setHeight("100%");
		stack.setWidth100();
		
		//上边布局
		tabSet = new TabSet();
		tabSet.setWidth100();
		tabSet.setHeight("55%");
		
		//下边布局
		final TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("45%");	
		
		initVerify(); 
		ToolStrip recivetoolStrip = new ToolStrip();
		recivetoolStrip.setAlign(Alignment.LEFT);
		tab1 = new Tab("温控设备回收");
		VLayout recivelay = new VLayout();
		recivelay.addMember(createbottoInfo2());
		vm1.addMember(panel);
		vm1.setDataSource(downTempDS);
		createTempTable(downTempTable);
		HLayout hlayout=new HLayout();
		hlayout.addMember(downTempTable);
		hlayout.addMember(recivelay);
		tab1.setPane(hlayout);
		reciveBtnWidget(recivetoolStrip);
		recivelay.addMember(recivetoolStrip);
		bottomTabSet.addTab(tab1);

		
		ToolStrip recivetoolStrip1 = new ToolStrip();
		recivetoolStrip1.setAlign(Alignment.LEFT);
		tab2 = new Tab("GPS设备回收");
		VLayout recivelay1 = new VLayout();
		recivelay1.addMember(createbottoInfo3());
		vm.addMember(panel1);
		vm.setDataSource(downGpsDS);
		createDownTable(downGpsTable);
		HLayout hlayout1=new HLayout();
		hlayout1.addMember(downGpsTable);
		hlayout1.addMember(recivelay1);
		tab2.setPane(hlayout1);
		reciveBtnWidget1(recivetoolStrip1);
		recivelay1.addMember(recivetoolStrip1);
		bottomTabSet.addTab(tab2);
		
//	 if(isPrivilege(TrsPrivRef.TRACK_P5)){
//		 Tab tab4=new Tab("调度单");
//		 tab4.setPane();
//		// tab4.setID("1_tmstrackview");
//		 tabSet.setTabs(tab4);
//	 }

		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		layOut.setHeight("100%");
		layOut.addMember(createLoadList());
		layOut.addMember(bottomTabSet);

		main.addMember(layOut);
		
		loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				record  = event.getRecord();
                if(ObjUtil.isNotNull(record.getAttribute("GPS_NO1"))){
                	Util.initComboValue(panel1.getItem("EQUIP_NO"), "BAS_GPSEQ", "ID", "EQUIP_NO", " where ID='"+record.getAttribute("GPS_NO1")+"'", "",record.getAttribute("GPS_NO1"));
				}
                if(ObjUtil.isNotNull(record.getAttribute("TEMP_NO1")) || ObjUtil.isNotNull(record.getAttribute("TEMP_NO2"))){
                	Util.initComboValue(panel.getItem("EQUIP_NO"), "BAS_TEMPEQ", "ID", "EQUIP_NO", " where ID in ('"+record.getAttribute("TEMP_NO1")+"','"+record.getAttribute("TEMP_NO2")+"')", "");
                }
                if(bottomTabSet.getSelectedTabNumber()==0){
                	downTempTable.invalidateCache();
                	downTempTable.discardAllEdits();
					Criteria findValues = new Criteria();
		            findValues.addCriteria("EQUIP_NO1", record.getAttribute("TEMP_NO1"));
		            findValues.addCriteria("EQUIP_NO2", record.getAttribute("TEMP_NO2"));
		            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
		            findValues.addCriteria("OP_FLAG", "M");
		            downTempTable.fetchData(findValues);
		            panel.clearValues();
		            recepitButton.enable();
	        	    canReceButton.disable();
                }else if(bottomTabSet.getSelectedTabNumber()==1){
					downGpsTable.invalidateCache();
					downGpsTable.discardAllEdits();
					Criteria findValues = new Criteria();
		            findValues.addCriteria("EQUIP_NO", record.getAttribute("GPS_NO1"));
		            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
		            findValues.addCriteria("OP_FLAG", "M");
		            downGpsTable.fetchData(findValues,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							if(downGpsTable.getRecords().length==0){
				            	panel1.clearValues();
								recepitButton1.enable();
			                	canReceButton1.disable();
				            }else{
				            	recepitButton1.disable();
			                	canReceButton1.enable();
				            }
						}
					});
				}
			}
		});
		
		downGpsTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record selectedRecord  = event.getRecord();
                vm.editRecord(selectedRecord);
			}
		});
		
		downTempTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record selectedRecord  = event.getRecord();
                vm1.editRecord(selectedRecord);
                recepitButton.disable();
        	    canReceButton.enable();
			}
		});
		

		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				String title=event.getTab().getTitle();
				if(record!=null){
					if(title.equals("温控设备回收")){
						downTempTable.invalidateCache();
	                	downTempTable.discardAllEdits();
						Criteria findValues = new Criteria();
			            findValues.addCriteria("EQUIP_NO1", record.getAttribute("TEMP_NO1"));
			            findValues.addCriteria("EQUIP_NO2", record.getAttribute("TEMP_NO2"));
			            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
			            findValues.addCriteria("OP_FLAG", "M");
			            downTempTable.fetchData(findValues);
					}else if(title.equals("GPS设备回收")){
						downGpsTable.invalidateCache();
						downGpsTable.discardAllEdits();
						Criteria findValues = new Criteria();
			            findValues.addCriteria("EQUIP_NO", record.getAttribute("GPS_NO1"));
			            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
			            findValues.addCriteria("OP_FLAG", "M");
			            downGpsTable.fetchData(findValues,new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								if(downGpsTable.getRecords().length==0){
					            	panel1.clearValues();
									recepitButton1.enable();
				                	canReceButton1.disable();
					            }else{
					            	recepitButton1.disable();
				                	canReceButton1.enable();
					            }
							}
						});
					}
				}
			}
		});
		
		return main;
	}
 
	private Canvas createLoadList(){

    	VLayout vlay=new VLayout();
    	final Menu menu=new Menu();
    	menu.setWidth(140);
    	
    	loadTable=new SGTable(loadDS,"100%","100%",false,true,false);
    	loadTable.setShowRowNumbers(true);
    	loadTable.setCanEdit(false);
    	loadTable.setSelectionType(SelectionStyle.SINGLE);
    	
    	createTableList(loadTable,false);
		
		loadListItem = new SectionStackSection(Util.TI18N.LISTINFO());
		loadListItem.setItems(loadTable);
		loadListItem.setExpanded(true);
		loadListItem.setControls(new SGPage(loadTable,true).initPageBtn());
		
		loadSectionStack=new SectionStack();
		loadSectionStack.addSection(loadListItem);
		loadSectionStack.setWidth("100%");		
		
		
		ToolStrip load=new ToolStrip();
		load.setWidth("100%");
		load.setHeight("20");
		load.setAlign(Alignment.RIGHT);
		IButton searchLoadButton=createBtn(StaticRef.FETCH_BTN,TrsPrivRef.GpsReclaim_P1);
		searchLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				tabSet.selectTab("1");
//				if(searchWin!=null){
//					searchWin.hide();
//				}
//				if(searchUnloadWin!=null){
//					searchUnloadWin.hide();
//				}
				if(searchLoadWin==null){
					searchLoadForm=new SGPanel();
					searchLoadForm.setDataSource(loadDS);
					searchLoadWin=new SearchWin(340,loadDS,createSearchLoadForm(searchLoadForm,false),loadSectionStack.getSection(0),new ValuesManager()).getViewPanel();
				}else{
					searchLoadWin.show();
				}
			}
		});
		load.addMember(searchLoadButton);
		vlay.setMembers(load,loadSectionStack);
		return vlay;
	
	}
	
	private void createTableList(SGTable table,boolean stat_flag){
		boolean isDigitCanEdit = false;
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),90);//调度单编号
		LOAD_NO.setShowGridSummary(true);
		LOAD_NO.setSummaryFunction(SummaryFunctionType.COUNT);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),60);//车牌号
		
		ListGridField STATUS=new ListGridField("STATUS",Util.TI18N.STATUS(),50);
		STATUS.setHidden(true);
		ListGridField GPS_NO1=new ListGridField("GPS_NO1","GPS设备编号",60);
		GPS_NO1.setHidden(true);
		ListGridField TEMP_NO1=new ListGridField("TEMP_NO1","温控设备编号",60);
		TEMP_NO1.setHidden(true);
		ListGridField TEMP_NO2=new ListGridField("TEMP_NO2","温控设备编号",60);
		TEMP_NO2.setHidden(true);
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
		
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID(),70);
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
		
		
		table.setFields(LOAD_NO,STATUS,GPS_NO1,TEMP_NO1,TEMP_NO2,STATUS_NAME,LOAD_STAT,LOAD_STAT_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME, DONE_TIME, UDF1,UDF2,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);
	}


	
	/**
	 * 按钮布局
	 */
    public  void createTransFollowBtnWidget(ToolStrip followtoolStrip){
    	followtoolStrip.setWidth("100%");
    	followtoolStrip.setHeight("20");
    	followtoolStrip.setPadding(2);
    	followtoolStrip.setSeparatorSize(12);
    	followtoolStrip.addSeparator();
    	followtoolStrip.setMembersMargin(4);
    	followtoolStrip.setAlign(Alignment.LEFT);      
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.GpsReclaim_P1_01);
       // saveButton.addClickHandler(new SaveTrasFollowAction(shpmTable,groupTable1,this,check_map));
        
        //取消按钮
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.GpsReclaim_P1_02);
      //  canButton.addClickHandler(new CancelAction(groupTable1,this));
      
        followtoolStrip.setMembersMargin(3);
        followtoolStrip.setMembers(saveButton, canButton);
        //add_map.put(TrsPrivRef.TRACK_P1_01, newButton);
        save_map.put(TrsPrivRef.GpsReclaim_P1_01, saveButton);
        save_map.put(TrsPrivRef.GpsReclaim_P1_02, canButton);
        //this.enableOrDisables(add_map, true);
        this.enableOrDisables(save_map, false);
        
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
		
		SGCombo DISPATCH_STAT= new SGCombo("DISPATCH_STAT",Util.TI18N.DISPATCH_STAT_NAME());
		Util.initCodesComboValue(DISPATCH_STAT, "DISPATCH_STAT");//--wangjun 2010-2-27
		
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
		/*BIZ_TYP.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(BIZ_TYP)){
				 biz_typ=ObjUtil.isNotNull(BIZ_TYP.getValue())? BIZ_TYP.getValue().toString() : "";
				}
			}
		});*/
		
		SGCombo LOAD_STAT = new SGCombo("LOAD_STAT","装车状态");
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("10", "未装车");
		map.put("20", "已装车");
		LOAD_STAT.setValueMap(map);
		form.setItems(CUSTOMER,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,START_AREA_ID,
				START_AREA,END_AREA_ID,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,BIZ_TYP,
				DISPATCH_STAT,ROUTE_ID,ADDWHO,LOAD_STAT,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,EXEC_FLAG);
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
		
	}
	
@Override
public void createBtnWidget(ToolStrip strip) {
	
} 

	private Canvas createbottoInfo3() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("45%");
		panel1 = new SGPanel();
		
		SGCombo EQUIP_NO = new SGCombo("EQUIP_NO","设备号");
		EQUIP_NO.setDisabled(true);
		SGText CLAIM_TIME = new SGText("CLAIM_TIME", ColorUtil.getRedTitle("设备归还时间"));
		CLAIM_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel1, CLAIM_TIME);
		SGCheck ABNORMAL_FLAG = new SGCheck("ABNORMAL_FLAG", "设备异常");
		
		SGCombo ABNORMAL_REASON = new SGCombo("ABNORMAL_REASON", "异常原因",true);
		Util.initCodesComboValue(ABNORMAL_REASON,"GPS_ABNORMAL");
		//ABNORMAL_REASON.setDisabled(true);
		SGCombo DUTY_TO = new SGCombo("DUTY_TO", "责任方");
		Util.initCodesComboValue(DUTY_TO,"DUTY_TO");
		
		TextAreaItem AB_NOTES = new TextAreaItem("AB_NOTES", "异常描述");
		AB_NOTES.setStartRow(true);
		AB_NOTES.setColSpan(4);
		AB_NOTES.setHeight(50);
		AB_NOTES.setWidth(FormUtil.longWidth);
		AB_NOTES.setTitleOrientation(TitleOrientation.TOP);
		AB_NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		panel1.setItems(EQUIP_NO,CLAIM_TIME,ABNORMAL_FLAG,ABNORMAL_REASON,DUTY_TO,AB_NOTES);
		hLayout.addMember(panel1);
		
		return hLayout;
	}
	
	private void createDownTable(SGTable table) {
		ListGridField CLAIM_TIME = new ListGridField("CLAIM_TIME","设备归还时间",120);
		Util.initDate(downGpsTable, CLAIM_TIME);
		ListGridField ADDWHO = new ListGridField("ADDWHO","操作人",60);
		ListGridField ABNORMAL_FLAG = new ListGridField("ABNORMAL_FLAG","设备异常",60);
		ABNORMAL_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField ABNORMAL_REASON = new ListGridField("ABNORMAL_REASON_NAME","异常原因",100);
		ListGridField DUTY_TO = new ListGridField("DUTY_TO_NAME","责任方",100);
		ListGridField AB_NOTES = new ListGridField("AB_NOTES","异常描述",160);
		
		table.setFields(CLAIM_TIME,ADDWHO,ABNORMAL_FLAG,ABNORMAL_REASON,DUTY_TO,AB_NOTES);
	}

	private void createTempTable(SGTable table) {
		ListGridField CLAIM_TIME = new ListGridField("CLAIM_TIME","设备归还时间",120);
		Util.initDate(downTempTable, CLAIM_TIME);
		ListGridField ADDWHO = new ListGridField("ADDWHO","操作人",60);
		ListGridField ABNORMAL_FLAG = new ListGridField("ABNORMAL_FLAG","设备异常",60);
		ABNORMAL_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField ABNORMAL_REASON = new ListGridField("ABNORMAL_REASON_NAME","异常原因",100);
		ListGridField DUTY_TO = new ListGridField("DUTY_TO_NAME","责任方",100);
		ListGridField NOTES = new ListGridField("NOTES","异常描述",160);
		
		table.setFields(CLAIM_TIME,ADDWHO,ABNORMAL_FLAG,ABNORMAL_REASON,DUTY_TO,NOTES);
	}
	
	private Canvas createbottoInfo2() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("45%");
		panel = new SGPanel();
		
		SGCombo EQUIP_NO = new SGCombo("EQUIP_NO",ColorUtil.getRedTitle("设备号"));
		
		final SGText CLAIM_TIME = new SGText("CLAIM_TIME", "设备归还时间");
		CLAIM_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel, CLAIM_TIME);
		final SGCheck ABNORMAL_FLAG = new SGCheck("ABNORMAL_FLAG", "设备异常");
		
		final SGCombo ABNORMAL_REASON = new SGCombo("ABNORMAL_REASON", "异常原因",true);
		Util.initCodesComboValue(ABNORMAL_REASON,"GPS_ABNORMAL");
		final SGCombo DUTY_TO = new SGCombo("DUTY_TO", "责任方");
		Util.initCodesComboValue(DUTY_TO,"DUTY_TO");
		
		final TextAreaItem NOTES = new TextAreaItem("NOTES", "异常描述");
		NOTES.setStartRow(true);
		NOTES.setColSpan(4);
		NOTES.setHeight(50);
		NOTES.setWidth(FormUtil.longWidth);
		NOTES.setTitleOrientation(TitleOrientation.TOP);
		NOTES.setTitleVAlign(VerticalAlignment.TOP);
		
		panel.setItems(EQUIP_NO,CLAIM_TIME,ABNORMAL_FLAG,ABNORMAL_REASON,DUTY_TO,NOTES);
		hLayout.addMember(panel);
		
		EQUIP_NO.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				CLAIM_TIME.setValue(getCurTime().toString());
				ABNORMAL_FLAG.setValue("");
				ABNORMAL_REASON.setValue("");
				DUTY_TO.setValue("");
				NOTES.setValue("");
				recepitButton.enable();
				canReceButton.disable();
			}
		});
		
		return hLayout;
		
	}	
	
	public void reciveBtnWidget1(ToolStrip recivetoolStrip1){
		recivetoolStrip1.setWidth("100%");
		recivetoolStrip1.setHeight("20");
		recivetoolStrip1.setPadding(2);
		recivetoolStrip1.setSeparatorSize(12);
		recivetoolStrip1.addSeparator();
		recivetoolStrip1.setMembersMargin(4);
		recivetoolStrip1.setAlign(Alignment.LEFT);
		//确认按钮
	    recepitButton1 = createUDFBtn("确认", StaticRef.ICON_SAVE,TrsPrivRef.GpsReclaim_P1_03);
	    recepitButton1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(loadTable.getRecords()==null) return;
				if(panel1.getItem("ABNORMAL_FLAG").getValue()!=null){
					if(panel1.getItem("ABNORMAL_FLAG").getValue().toString().equals("true")){
						if(!ObjUtil.isNotNull(panel1.getItem("ABNORMAL_REASON").getValue())){
							SC.say("设备异常必须填写异常原因");
							return;
						}
					}
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("insert into BAS_GPS_RECLAIM(ID,EQUIP_NO,CLAIM_TIME,ABNORMAL_FLAG,");
				sf.append("ABNORMAL_REASON,DUTY_TO,AB_NOTES,ADDTIME,ADDWHO,EDITTIME,EDITWHO,LOAD_NO)");
				sf.append(" values(");
				sf.append("sys_guid(),");
				sf.append("'"+loadTable.getSelectedRecord().getAttribute("GPS_NO1")+"',");
				sf.append("to_date('"+panel1.getItem("CLAIM_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
				if(panel1.getItem("ABNORMAL_FLAG").getValue()!=null){
					if(panel1.getItem("ABNORMAL_FLAG").getValue().toString().equals("true")){
						sf.append("'Y',");
					}else{
						sf.append("'N',");
					}
				}else{
					sf.append("'',");
				}
				sf.append("'"+panel1.getItem("ABNORMAL_REASON").getValue()+"',");
				sf.append("'"+panel1.getItem("DUTY_TO").getValue()+"',");
				if(ObjUtil.isNotNull(panel1.getItem("AB_NOTES").getValue())){
					sf.append("'"+panel1.getItem("AB_NOTES").getValue()+"',");
				}else{
					sf.append("'',");
				}
				sf.append("sysdate,");
				sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
				sf.append("sysdate,");
				sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
				sf.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"')");
				sqlList.add(sf.toString());
				StringBuffer sf2 = new StringBuffer();
				sf2.append("update BAS_GPSEQ set STATUS='DF252F0637784E9EA575CCACB64050FC' where id = '"+loadTable.getSelectedRecord().getAttribute("GPS_NO1")+"'");
				sqlList.add(sf2.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							downGpsTable.invalidateCache();
							downGpsTable.discardAllEdits();
							Criteria findValues = new Criteria();
				            findValues.addCriteria("EQUIP_NO", loadTable.getSelectedRecord().getAttribute("GPS_NO1"));
				            findValues.addCriteria("LOAD_NO", loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
				            findValues.addCriteria("OP_FLAG", "M");
				            downGpsTable.fetchData(findValues);
							recepitButton1.disable();
							canReceButton1.enable();
						}
					}
					
				});
			}
		});
	    //取消按钮
	    canReceButton1 = createUDFBtn("取消确认", StaticRef.ICON_CANCEL,TrsPrivRef.GpsReclaim_P1_04);
	    canReceButton1.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(loadTable.getRecords()==null) return;
				if(downGpsTable.getSelectedRecord()==null){
					SC.say("请选择一条记录");
					return;
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("delete from BAS_GPS_RECLAIM where EQUIP_NO='"+loadTable.getSelectedRecord().getAttribute("GPS_NO1")+"' and LOAD_NO='"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
				sqlList.add(sf.toString());
				//StringBuffer sf1 = new StringBuffer();
				//sf1.append("update TRANS_LOAD_HEADER set UDF1='N' where LOAD_NO='"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
				//sqlList.add(sf1.toString());
				StringBuffer sf2 = new StringBuffer();
				sf2.append("update BAS_GPSEQ set STATUS='3A60CB9A86884871A8C8EF734B1973F6' where id = '"+loadTable.getSelectedRecord().getAttribute("GPS_NO1")+"'");
				sqlList.add(sf2.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							downGpsTable.invalidateCache();
							downGpsTable.discardAllEdits();
							Criteria findValues = new Criteria();
				            findValues.addCriteria("EQUIP_NO", loadTable.getSelectedRecord().getAttribute("GPS_NO1"));
				            findValues.addCriteria("LOAD_NO", loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
				            findValues.addCriteria("OP_FLAG", "M");
				            downGpsTable.fetchData(findValues);
							panel1.clearValues();
							recepitButton1.enable();
							canReceButton1.disable();
						}
					}
					
				});
			}
		});

	    recepitButton1.enable();
	    canReceButton1.disable();
	    recivetoolStrip1.setMembersMargin(2);
	    recivetoolStrip1.setMembers(recepitButton1,canReceButton1);
	}
	
	public void reciveBtnWidget(ToolStrip recivetoolStrip){
		recivetoolStrip.setWidth("100%");
		recivetoolStrip.setHeight("20");
		recivetoolStrip.setPadding(2);
		recivetoolStrip.setSeparatorSize(12);
		recivetoolStrip.addSeparator();
		recivetoolStrip.setMembersMargin(4);
		recivetoolStrip.setAlign(Alignment.LEFT);
		//确认按钮
	    recepitButton = createUDFBtn("确认", StaticRef.ICON_SAVE,TrsPrivRef.GpsReclaim_P1_01);
	    recepitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(loadTable.getRecords()==null) return;
				Record[] rec = downTempTable.getRecords();
				if(!ObjUtil.isNotNull(panel.getItem("EQUIP_NO").getValue())){
					SC.say("设备号不能为空");
					return;
				}
				for(int i=0;i<rec.length;i++){
					if(panel.getItem("EQUIP_NO").getValue().equals(rec[i].getAttribute("EQUIP_NO"))){
						SC.say("这个设备号已回收");
						return;
					}
				}
				if(panel.getItem("ABNORMAL_FLAG").getValue()!=null){
					if(panel.getItem("ABNORMAL_FLAG").getValue().toString().equals("true")){
						if(!ObjUtil.isNotNull(panel.getItem("ABNORMAL_REASON").getValue())){
							SC.say("设备异常必须填写异常原因");
							return;
						}
					}
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("insert into BAS_TEMP_RECLAIM(ID,EQUIP_NO,CLAIM_TIME,ABNORMAL_FLAG,");
				sf.append("ABNORMAL_REASON,DUTY_TO,NOTES,ADDTIME,ADDWHO,EDITTIME,EDITWHO,LOAD_NO)");
				sf.append(" values(");
				sf.append("sys_guid(),");
				sf.append("'"+panel.getItem("EQUIP_NO").getValue()+"',");
				sf.append("to_date('"+panel.getItem("CLAIM_TIME").getValue()+"','YYYY-MM-DD HH24:MI:SS'),");
				if(panel.getItem("ABNORMAL_FLAG").getValue()!=null){
					if(panel.getItem("ABNORMAL_FLAG").getValue().toString().equals("true")){
						sf.append("'Y',");
					}else{
						sf.append("'N',");
					}
				}else{
					sf.append("'',");
				}
				sf.append("'"+panel.getItem("ABNORMAL_REASON").getValue()+"',");
				sf.append("'"+panel.getItem("DUTY_TO").getValue()+"',");
				if(ObjUtil.isNotNull(panel.getItem("NOTES").getValue())){
					sf.append("'"+panel.getItem("NOTES").getValue()+"',");
				}else{
					sf.append("'',");
				}
				sf.append("sysdate,");
				sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
				sf.append("sysdate,");
				sf.append("'"+LoginCache.getLoginUser().getUSER_ID()+"',");
				sf.append("'"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"')");
				sqlList.add(sf.toString());
				StringBuffer sf2 = new StringBuffer();
				sf2.append("update BAS_TEMPEQ set STATUS='DF252F0637784E9EA575CCACB64050FC' where id = '"+panel.getItem("EQUIP_NO").getValue()+"'");
				sqlList.add(sf2.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							downTempTable.invalidateCache();
		                	downTempTable.discardAllEdits();
							Criteria findValues = new Criteria();
				            findValues.addCriteria("EQUIP_NO1", record.getAttribute("TEMP_NO1"));
				            findValues.addCriteria("EQUIP_NO2", record.getAttribute("TEMP_NO2"));
				            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
				            findValues.addCriteria("OP_FLAG", "M");
				            downTempTable.fetchData(findValues);
							recepitButton.disable();
							canReceButton.enable();
						}
					}
					
				});
			}
		});
	
	    //取消按钮
	    canReceButton = createUDFBtn("取消确认", StaticRef.ICON_CANCEL,TrsPrivRef.GpsReclaim_P1_02);
	    canReceButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(loadTable.getRecords()==null) return;
				if(downTempTable.getSelectedRecord()==null){
					SC.say("请选择一条记录");
					return;
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("delete from BAS_TEMP_RECLAIM where EQUIP_NO='"+panel.getItem("EQUIP_NO").getValue()+"' and LOAD_NO='"+loadTable.getSelectedRecord().getAttribute("LOAD_NO")+"'");
				sqlList.add(sf.toString());
				StringBuffer sf2 = new StringBuffer();
				sf2.append("update BAS_TEMPEQ set STATUS='3A60CB9A86884871A8C8EF734B1973F6' where id = '"+panel.getItem("EQUIP_NO").getValue()+"'");
				sqlList.add(sf2.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(String result) {
						if(result.equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							downTempTable.invalidateCache();
		                	downTempTable.discardAllEdits();
							Criteria findValues = new Criteria();
				            findValues.addCriteria("EQUIP_NO1", record.getAttribute("TEMP_NO1"));
				            findValues.addCriteria("EQUIP_NO2", record.getAttribute("TEMP_NO2"));
				            findValues.addCriteria("LOAD_NO", record.getAttribute("LOAD_NO"));
				            findValues.addCriteria("OP_FLAG", "M");
				            downTempTable.fetchData(findValues);
							panel.clearValues();
							recepitButton.enable();
							canReceButton.disable();
						}
					}
					
				});
			}
		});
	
	    recepitButton.enable();
	    canReceButton.disable();
	    recivetoolStrip.setMembersMargin(2);
	    recivetoolStrip.setMembers(recepitButton,canReceButton);
		
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
		GpsReclaimView view = new GpsReclaimView();
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
