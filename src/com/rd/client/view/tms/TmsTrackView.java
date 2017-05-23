package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.CancelTrackAction;
import com.rd.client.action.tms.ChangedUnLoadQntyAction;
import com.rd.client.action.tms.DeleteTrackAction;
import com.rd.client.action.tms.NewTrackAction;
import com.rd.client.action.tms.SaveTrackAction;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.action.tms.shpmreceipt.NewShpmManageAction;
import com.rd.client.action.tms.shpmreceipt.SaveShpmAction;
import com.rd.client.action.tms.track.CancelRecepitAction;
import com.rd.client.action.tms.track.CancleReceiptAction;
import com.rd.client.action.tms.track.ConfirmReceiptAction;
import com.rd.client.action.tms.track.GPSPositionAction;
import com.rd.client.action.tms.track.HoldToDispatchAction;
import com.rd.client.action.tms.track.RejectReceiptAction;
import com.rd.client.action.tms.track.SelectSameAction;
import com.rd.client.action.tms.track.VechPositionAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.DeleteTransFollowAction;
import com.rd.client.common.action.ExportAction;
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
import com.rd.client.ds.tms.EsbGpsDS;
import com.rd.client.ds.tms.LoadDS;
import com.rd.client.ds.tms.ShpmDS2;
import com.rd.client.ds.tms.ShpmDS3;
import com.rd.client.ds.tms.ShpmDetailQSDS;
import com.rd.client.ds.tms.TmsFollowDS;
import com.rd.client.ds.tms.TrackDS;
import com.rd.client.ds.tms.TranLossDamageDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SkuWin;
import com.rd.client.win.TrackSkuWin;
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
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.RightMouseDownEvent;
import com.smartgwt.client.widgets.events.RightMouseDownHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
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
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabClickEvent;
import com.smartgwt.client.widgets.tab.events.TabIconClickHandler;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理-->运输跟踪
 * @author 
 *
 */
@ClassForNameAble
public class TmsTrackView extends SGForm implements PanelFactory {

	private DataSource ds;
	private DataSource EsbDS;
	private DataSource loadDS;
	public  SGTable groupTable1;
	//public  SGTable groupTable2;
	private SGPanel searchForm;
	private Window searchWin;
	public SGPanel panel;
	public SGPanel panel1;
	
	private SGPanel searchLoadForm;
	private Window searchLoadWin;
	
	public SGTable shpmTable;       //已调作业单表
	public SGTable loadTable;
	public SGTable unloadTable;
	public SGTable shpmlstTable;    //已调作业单明细表
	public SGTable damageTable;
	private String shpm_no;          //作业单号
	private String transSrvcId;	//运输服务
	private String bizTyp;	//业务类型
	public String status;	//状态
	private ArrayList<String> logList;
	
	
	private DataSource shpmDS;            //已调作业单数据源
	private DataSource shpmlstDS;         //已调作业但明细数据源
	private DataSource LossDamageDS;
	private String LoadNo;//监听被选中的调度单号
	public Record shpmnorecord;
	public Record grouprecords;
	public ListGridRecord[] loadReocrd;
	private Record re;
	
	private SectionStack sectionStack;
	private  SectionStackSection  listItem;
	
	public IButton recepitButton;
	public IButton canReceButton;
	
	public IButton cusRecepitButton;
	public IButton cancelRecepitButton;
	public IButton preRecepitButton;
	public IButton cusCanReceButton;
	
	public int tabSelect = 0;
	
	public HashMap<String,IButton> add_dm_map; //新增、
	public HashMap<String,IButton> save_dm_map; //保存、取消按钮
	public HashMap<String, IButton> del_dm_map; //删除按钮
	
	//yuanlei 2012-10-25  在途跟踪列表增加最大化按钮
	private boolean isTopMax = false;
	private boolean isDownMax = false;	
	public TabSet tabSet;
	private SGPanel searchUnloadForm;
	private Window searchUnloadWin;
	public SGTable unloadList;
	private DataSource unloadDS;
	private String load_no;
	private SectionStackSection unloadListItem;
	private SectionStack unloadSectionStack;
	private SectionStackSection loadListItem;
	private SectionStack loadSectionStack;
	private Tab tab1;
	private Tab tab2;
	private Tab tab3;	
	private String biz_typ="";
	public SGTable trackTable;      //跟踪信息
	private DataSource trackDS;
	
	public HashMap<String,IButton> add_tr_map; //新增、
	public HashMap<String,IButton> save_tr_map; //保存、取消按钮
	public HashMap<String, IButton> del_tr_map; //删除按钮
	private HashMap<String,String> detail_map;
	private HashMap<String,String> detail_tr_map;
	
	/*public TmsTrackView(String id) {
	    super(id);
	}*/
	
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		detail_map = new HashMap<String,String>();
		detail_tr_map = new HashMap<String,String>();
		/**
		 * @ds 在途跟踪数据源
		 * @shpmDS 作业单主表数据源
		 * @shpmlstDS 作业单详细表数据源
		 */
		loadDS = LoadDS.getInstance("V_LOAD_HEADER", "TRANS_LOAD_HEADER");
		ds = TmsFollowDS.getInstance("V_TRANS_TRACK_TRACE","TRANS_TRACK_TRACE");
		EsbDS=EsbGpsDS.getInstance("ESB_GPS_POSITION","ESB_GPS_POSITION");
		shpmDS = ShpmDS2.getInstance("V_SHIPMENT_HEADER", "TRANS_SHIPMENT_HEADER");
		shpmlstDS = ShpmDetailQSDS.getInstance("V_SHIPMENT_ITEM_QS", "TRANS_SHIPMENT_ITEM");
		LossDamageDS=TranLossDamageDS.getInstance("V_LOSS_DAMAGE_","TRANS_LOSS_DAMAGE");
		unloadDS=ShpmDS3.getInstance("V_SHIPMENT_HEADER2","TRANS_SHIPMENT_HEADER");
		trackDS=TrackDS.getInstance("TRANS_TRACK_TRACE","TRANS_TRACK_TRACE");
		
		//在途跟踪按钮布局
		ToolStrip followtoolStrip = new ToolStrip();
		followtoolStrip.setAlign(Alignment.LEFT);
		
		//到货签收按钮布局
		ToolStrip recivetoolStrip = new ToolStrip();
		recivetoolStrip.setAlign(Alignment.LEFT);
		
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
		
		shpmTable = new SGTable(shpmDS,"100%","100%"){			
			public DataSource getRelatedDataSource(ListGridRecord record) {
				
				shpmlstDS = ShpmDetailQSDS.getInstance("V_SHIPMENT_ITEM_QS", "TRANS_SHIPMENT_ITEM");
				shpm_no = record.getAttributeAsString("SHPM_NO");
				transSrvcId = record.getAttributeAsString("TRANS_SRVC_ID");
				bizTyp = record.getAttributeAsString("BIZ_TYP");
				status = record.getAttributeAsString("STATUS");
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
                shpmlstTable.setAutoFitData(Autofit.VERTICAL);
                shpmlstTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
                
                /*if(Integer.parseInt(shpmnorecord.getAttribute("STATUS")) > Integer.parseInt(StaticRef.SHPM_LOAD)) {
                	shpmlstTable.setCanEdit(false);
                }
                else {
                	shpmlstTable.setCanEdit(true);
                }*/
                shpmlstTable.setAlign(Alignment.RIGHT);
                shpmlstTable.setShowRowNumbers(false);
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
        		
        		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK(), 70);
        		PACK_ID.setCanEdit(false);
        		PACK_ID.setHidden(true);
        		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
        		
        		final ListGridField UOM = new ListGridField("UOM",Util.TI18N.UNIT(),50);//						
        		UOM.setCanEdit(false);
        		Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");
        		UOM.setDefaultValue("件");
        		
        		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),60);//本单量，						
        		QNTY.setAlign(Alignment.RIGHT);
        		QNTY.setCanEdit(false);
        		
        		ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.FOLLOW_LD_QNTY(),60);//						
        		LD_QNTY.setAlign(Alignment.RIGHT);
        		LD_QNTY.setCanEdit(false);
        		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);//签收数量						
        		UNLD_QNTY.setAlign(Alignment.RIGHT);
        		UNLD_QNTY.setCanEdit(true);
        		ListGridField G_WGT = new ListGridField("UNLD_GWGT",Util.TI18N.G_WGT(),80);//		签收重量				
        		G_WGT.setAlign(Alignment.RIGHT);
        		G_WGT.setCanEdit(true);
        		ListGridField VOL = new ListGridField("UNLD_VOL",Util.TI18N.VOL(),80);//  签收体积						
        		VOL.setAlign(Alignment.RIGHT);	
        		VOL.setCanEdit(true);
        		
        		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1", Util.TI18N.TEMPERATURE(), 90);
        		TEMPERATURE1.setCanEdit(false);
        		Util.initCodesComboValue(TEMPERATURE1,"STOR_COND");
        		
        		ListGridField VOL_GWT_RATIO = new ListGridField("VOL_GWT_RATIO","体积毛重转换比",60);//体积毛重转换比例，						
        		VOL_GWT_RATIO.setHidden(true);
        		VOL_GWT_RATIO.setCanEdit(false);
        		
        		FormItemIcon icon1 = new FormItemIcon();
        		SKU_NAME.setIcons(icon1);
        		SKU_NAME.setShowSelectedIcon(true);
        		icon1.addFormItemClickHandler(new FormItemClickHandler() {
        			
        			@Override
        			public void onFormItemClick(FormItemIconClickEvent event) {
    					new SkuWin(shpmlstTable,null,itemRow,"40%", "38%",getThis(),null).getViewPanel();
        			}
        		});
        		SKU_NAME.addEditorExitHandler(new EditorExitHandler() {
        			
        			@Override
        			public void onEditorExit(EditorExitEvent event) {
        				final int row = event.getRowNum();
        				if(!ObjUtil.isNotNull(event.getNewValue())){
        					return;
        				}
        				final String sku_name = ObjUtil.ifObjNull(event.getNewValue(),"").toString();
        				Util.db_async.getRecord("ID,SKU,TRANS_COND,SKU_CNAME,PACK_ID,TRANS_UOM,GROSSWEIGHT,VOLUME", "V_SKU", 
        						" where common_flag = 'Y' and full_index like '%"+sku_name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
        					
        					@Override
        					public void onSuccess(ArrayList<HashMap<String, String>> result) {
        						int size = result.size();
        						if(size == 1){
        							shpmlstTable.setEditValue(row, "SKU_ID", result.get(0).get("ID"));
        							shpmlstTable.setEditValue(row, "SKU_NAME", result.get(0).get("SKU_CNAME"));
        							shpmlstTable.setEditValue(row, "SKU", result.get(0).get("SKU"));
        							shpmlstTable.setEditValue(row, "TEMPERATURE1", result.get(0).get("TRANS_COND"));
        							shpmlstTable.setEditValue(row, "PACK_ID", result.get(0).get("PACK_ID"));
        							shpmlstTable.setEditValue(row, "UOM", result.get(0).get("TRANS_UOM"));
        							shpmlstTable.setEditValue(row, "QNTY", "1");
        							shpmlstTable.setEditValue(row, "QNTY_EACH", "1");
        							shpmlstTable.setEditValue(row, "UNLD_VOL", ObjUtil.ifNull(result.get(0).get("VOLUME"),"0.00"));
        							shpmlstTable.setEditValue(row, "UNLD_GWGT",ObjUtil.ifNull(result.get(0).get("GROSSWEIGHT"),"0.00"));
        						}else if(size > 1){
        							shpmlstTable.setProperty("FULL_INDEX", sku_name);
        							new SkuWin(shpmlstTable,null,itemRow,"40%", "38%",getThis(),sku_name).getViewPanel();
        						}
        						
        					}
        					
        					@Override
        					public void onFailure(Throwable caught) {
        						
        					}
        				});
        				
        			}
        		});
        		
        		PACK_ID.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
        			
        			@Override
        			public void onChanged(
        					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
        				String value = "";
        				FormItem item = event.getItem();
        				if(item != null){
        					value = ObjUtil.ifNull(item.getDisplayValue(), "");
        				}
        				if(!ObjUtil.isNotNull(value)){
        					value = ObjUtil.ifObjNull(shpmlstTable.getEditValue(itemRow, "PACK_ID"), "").toString();
        					Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack=(select pack from bas_package where id = '"+value+"')", "");
        				}else{
        					Util.initComboValue(UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='"+value+"'", "");
        				}
        			}
        		});
        		
        		//关于 作业单明细  小数为数的处理
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
        		
        		//计算货损货差事件
        		UNLD_QNTY.addEditorExitHandler(new ChangedUnLoadQntyAction(getThis()));
        		
        		shpmlstTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,PACK_ID,						
        				UOM,QNTY,LD_QNTY,UNLD_QNTY,G_WGT,VOL,TEMPERATURE1,VOL_GWT_RATIO);	
        		
        		addRight(findValues);	//添加作业单明细右键
        		//详细列表查询记录回调
        		shpmlstTable.fetchData(findValues, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
							shpmlsEdit(true);
					}
				});
        		shpmlstTable.addDoubleClickHandler(new DoubleClickHandler() {

					@Override
					public void onDoubleClick(DoubleClickEvent event) {
		                if(Integer.parseInt(shpmnorecord.getAttribute("STATUS")) > Integer.parseInt(StaticRef.SHPM_LOAD)) {
		                	shpmlstTable.setCanEdit(false);
		                }
		                else {
		                	shpmlstTable.setCanEdit(true);
		                }
					}
        			
        		});
        		
        		shpmlstTable.addCellClickHandler(new CellClickHandler() {
        			
        			@Override
        			public void onCellClick(CellClickEvent event) {
        				shpmlstTable.setProperty("selectedRowNum", event.getRowNum());
        				
        			}
        		});
        		
                layout.addMember(shpmlstTable);
                layout.setLayoutLeftMargin(35);
                
                /*if(rec != null) {
                	collapseRecord(rec);
                }
                rec = record;*/
                
                return layout;   
               } 		
			
		};
		
		//作业单第一级列表
		shpmentGrid(shpmTable);
		shpmTable.setCanExpandRecords(true);
		shpmTable.setCanExpandMultipleRecords(false);
//		shpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		shpmTable.setShowFilterEditor(false);
		
		shpmTable.setCanEdit(false);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(shpmTable);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		//yuanlei 2012-10-25  列表增加最大化按钮
		listItem.setControls(new SGPage(shpmTable, true).initPageBtn());
		//yuanlei 2012-10-25
		sectionStack.setWidth("100%");
		sectionStack.setHeight("90%");
		ToolStrip shpmstrip=new ToolStrip();
		shpmstrip.setWidth("100%");
		shpmstrip.setHeight("20");
		shpmstrip.setAlign(Alignment.RIGHT);
		IButton searchButton=createBtn(StaticRef.FETCH_BTN,TrsPrivRef.TRACK_P4);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
			   if(searchWin==null){
				   searchForm=new SGPanel();
					//yuanlei 2012-09-13 数据源传递错误
				    //searchWin = new SearchWin(ds, createSerchForm(searchForm),
					//		sectionStack.getSection(0)).getViewPanel();
					searchWin = new SearchWin(shpmDS, createSerchForm(searchForm),
							sectionStack.getSection(0)).getViewPanel();
					//yuanlei
					searchWin.setWidth(660);
					searchWin.setHeight(400);
				}else{
					searchWin.show();
				}
				
			}
		});
		
		shpmstrip.addMember(searchButton);
		stack.setMembers(shpmstrip,sectionStack);
		
		final Menu menu = new Menu();
		menu.setWidth(140);
		MenuItemSeparator itemSeparator =new MenuItemSeparator();
		
		if(isPrivilege(TrsPrivRef.TRACK_P0_02)) {
		    MenuItem SelectSame = new MenuItem("全选同一车",StaticRef.ICON_NEW);
		    SelectSame.addClickHandler(new SelectSameAction(shpmTable,this));
		    KeyIdentifier allSelectKey = new KeyIdentifier();
		    allSelectKey.setCtrlKey(true);
		    allSelectKey.setKeyName("B");
		    menu.addItem(SelectSame);
		}
	    if(isPrivilege(TrsPrivRef.TRACK_P0_03)) {
		    MenuItem position = new MenuItem("车辆定位",StaticRef.ICON_SEARCH);
		    position.addClickHandler(new VechPositionAction(shpmTable));
		    menu.addItem(position);
	    }
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P0_06)) {
		    MenuItem position = new MenuItem("GPS定位",StaticRef.ICON_SEARCH);
		    position.addClickHandler(new GPSPositionAction(shpmTable));
		    menu.addItem(position);
	    }
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P0_01)){
	    	menu.addItem(itemSeparator);
	    	MenuItem expItem=new MenuItem("导出",StaticRef.ICON_EXPORT);
	    	menu.addItem(expItem);
	    	expItem.addClickHandler(new ExportAction(shpmTable));
	    }
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P0_01) || isPrivilege(TrsPrivRef.TRACK_P0_01)){
	    	menu.addItem(itemSeparator);
	    }
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P5)){
	    	MenuItem switchDisItem=new MenuItem("切换到调度单",StaticRef.ICON_SEARCH);
	    	menu.addItem(switchDisItem);
	    	switchDisItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					ListGridRecord selectedRecord = shpmTable.getSelectedRecord();
					if(selectedRecord == null)return;
					String loadNo = selectedRecord.getAttributeAsString("LOAD_NO");
					if(!ObjUtil.isNotNull(loadNo))return;
					if(!isPrivilege(TrsPrivRef.TRACK_P5))return;
					Criteria cri = loadTable.getCriteria();
					cri = cri == null ? new Criteria() : cri;
					cri.addCriteria("OP_FLAG","M");
					cri.addCriteria("LOAD_NO",loadNo);
					loadTable.fetchData(cri);
					tabSet.selectTab("1_tmstrackview");
				}
			});
	    }
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P6)){
	    	MenuItem switchUNItem=new MenuItem("切换到卸货地",StaticRef.ICON_SEARCH);
	    	menu.addItem(switchUNItem);
	    	switchUNItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					ListGridRecord selectedRecord = shpmTable.getSelectedRecord();
					if(selectedRecord == null)return;
					String loadNo = selectedRecord.getAttributeAsString("LOAD_NO");
					if(!ObjUtil.isNotNull(loadNo))return;
					if(!isPrivilege(TrsPrivRef.TRACK_P6))return;
					Criteria cri = unloadTable.getCriteria();
					cri = cri == null ? new Criteria() : cri;
					cri.addCriteria("OP_FLAG","M");
					cri.addCriteria("LOAD_NO",loadNo);
					unloadTable.fetchData(cri);
					tabSet.selectTab("2_tmstrackview");
					
				}
			});
	    }
	    
	    shpmTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
			
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				menu.showContextMenu();
                event.cancel();
			}
		});
	    
	    if(isPrivilege(TrsPrivRef.TRACK_P4)){
		 	Tab odrTab1 = new Tab("作业单");
			odrTab1.setPane(stack);
			odrTab1.setID("0_tmstrackview");
			tabSet.setTabs(odrTab1);
		 }
			
		 if(isPrivilege(TrsPrivRef.TRACK_P5)){
			 Tab tab4=new Tab("调度单");
			 tab4.setPane(createLoadList());
			 tab4.setID("1_tmstrackview");
			 tabSet.setTabs(tab4);
		 }
		if(isPrivilege(TrsPrivRef.TRACK_P6)){
			Tab tab5=new Tab("卸货地");
			tab5.setPane(createUnloadList());
			tab5.setID("2_tmstrackview");
			tabSet.setTabs(tab5);
		}
		
	    if(isPrivilege(TrsPrivRef.TRACK_P7)){
			Tab tab6 = new Tab("跟踪信息");
			VLayout trackLay = new VLayout();
			trackLay.addMember(createTrackInfo());
			trackLay.addMember(createTrackBtn());
			tab6.setPane(trackLay);
			tab6.setID("3_tmstrackview");
			bottomTabSet.addTab(tab6);
		 }
		createbottoInfo();
	 if(isPrivilege(TrsPrivRef.TRACK_P1)) {  
		tab1 = new Tab("GPS信息");//在途跟踪
		tab1.setIcon(StaticRef.ICON_TOUP);
		
		VLayout followlay = new VLayout();
		followlay.addMember(groupTable1);
		tab1.setPane(followlay);
		tab1.setID("4_tmstrackview");
		//createTransFollowBtnWidget(followtoolStrip);
		//followlay.addMember(followtoolStrip);
		bottomTabSet.addTab(tab1);
		//yuanlei 2012-10-25 在途跟踪增加最大化功能
		bottomTabSet.addTabIconClickHandler(new TabIconClickHandler() {

			@Override
			public void onTabIconClick(TabClickEvent event) {
				if(!isTopMax) {
					tabSet.setHeight("9%");
					bottomTabSet.setHeight("90%");	
					tab1.setIcon(StaticRef.ICON_TODOWN);
				}
				else {
					tabSet.setHeight("55%");
					bottomTabSet.setHeight("45%");
					tab1.setIcon(StaticRef.ICON_TOUP);
				}
				isTopMax = !isTopMax;
			}
			
		});
		//yuanlei
		
	 }
	 if(isPrivilege(TrsPrivRef.TRACK_P2)) {
		tab2 = new Tab(Util.TI18N.UNLOAD_RECIVE());//到货签收 
		VLayout recivelay = new VLayout();
		recivelay.addMember(createbottoInfo2());
		tab2.setPane(recivelay);
		tab2.setID("5_tmstrackview");
		reciveBtnWidget(recivetoolStrip);
		recivelay.addMember(recivetoolStrip);
		bottomTabSet.addTab(tab2);
		
	 }
	 if(isPrivilege(TrsPrivRef.TRACK_P7)){
		 tab3 = new Tab("货损货差");
		 VLayout damaLay = new VLayout();
		 damaLay.addMember(createLossDamageInfo());
		 damaLay.addMember(createDamageBtn());
		 tab3.setPane(damaLay);
		 tab3.setID("6_tmstrackview");
		 bottomTabSet.addTab(tab3);
	 }
	 
	
	
	tabSet.addTabSelectedHandler(new TabSelectedHandler() {
		@Override
		public void onTabSelected(TabSelectedEvent event) {
			String tab_flag=tabSet.getSelectedTab().getID();
			switch (Integer.valueOf(tab_flag.substring(0,1))) {
			case 0:
				if(searchLoadWin!=null){
					searchLoadWin.hide();
				}
				if(searchUnloadWin!=null){
					searchUnloadWin.hide();
				}
				bottomTabSet.selectTab("3_tmstrackview");
				bottomTabSet.enableTab("4_tmstrackview");
				bottomTabSet.enableTab("6_tmstrackview");
				bottomTabSet.enableTab("3_tmstrackview");
				initShpmButton();
				break;
			case 1:
				if(searchWin!=null){
					searchWin.hide();
				}
				if(searchUnloadWin!=null){
					searchUnloadWin.hide();
				}
				bottomTabSet.enableTab("5_tmstrackview");
				bottomTabSet.enableTab("4_tmstrackview");
				bottomTabSet.disableTab("6_tmstrackview");
				bottomTabSet.enableTab("3_tmstrackview");
				bottomTabSet.selectTab("3_tmstrackview");
				initLoadButton();
				break;
			case 2:
				if(searchLoadWin!=null){
					searchLoadWin.hide();
				}
				if(searchWin!=null){
					searchWin.hide();
				}
				bottomTabSet.selectTab("5_tmstrackview");
				bottomTabSet.disableTab("4_tmstrackview");
				bottomTabSet.disableTab("6_tmstrackview");
				bottomTabSet.disableTab("3_tmstrackview");
				initUnloadButton();
				break;
			default:
				break;
			}
		}
	});
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		layOut.setHeight("100%");
		layOut.addMember(tabSet);
		layOut.addMember(bottomTabSet);
		
		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if("4_tmstrackview".equals(bottomTabSet.getSelectedTab().getID())){
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG", "M");
					Record rec = loadTable.getSelectedRecord();
					ListGridRecord[] record = loadTable.getSelection();
					if(record.length > 0){
						if(rec.getAttribute("PLATE_NO")!=null&&(!rec.getAttribute("PLATE_NO").equals(""))){
							crit.addCriteria("VEHICLE", rec.getAttribute("PLATE_NO"));
							crit.addCriteria("LOAD_NO", rec.getAttribute("LOAD_NO"));
							groupTable1.invalidateCache();
							groupTable1.fetchData(crit , new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									LoginCache.setPageResult(groupTable1, new FormItem(), new FormItem());
								}
							});
						}
					} 
				}
				if("3_tmstrackview".equals(bottomTabSet.getSelectedTab().getID())){
					if(loadTable.getSelection().length > 0){
						Criteria crit = new Criteria();
						crit.addCriteria("OP_FLAG", "M");
						crit.addCriteria("LOAD_NO",loadTable.getSelectedRecord().getAttribute("LOAD_NO"));
						trackTable.fetchData(crit);
					}
					if(shpmTable.getSelection().length > 0){
						Criteria crit = new Criteria();
						crit.addCriteria("OP_FLAG", "M");
						crit.addCriteria("LOAD_NO",shpmTable.getSelectedRecord().getAttribute("LOAD_NO"));
						trackTable.fetchData(crit);
					}
				}
			}
		});
		
		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(ObjUtil.isNotNull(tabSet.getSelectedTab())){
					tabSelect=Integer.valueOf(event.getTab().getID().substring(0,1));
				}else{
					tabSelect=0;
				}
				if("0_tmstrackview".equals(tabSet.getSelectedTab().getID()) || "1_tmstrackview".equals(tabSet.getSelectedTab().getID())){
					selectHander(tabSelect);
				}else{
					bottomTabSet.selectTab("5_tmstrackview");
				}
			}
		});
		main.addMember(layOut);
		
		
		return main;
	}
	
	private void addRight(final Criteria criteria){
		final Menu menu = new Menu();   //明细右键
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
	    MenuItem addItem = new MenuItem("新增明细",StaticRef.ICON_NEW);
	    addItem.setKeyTitle("Alt+N");
	    KeyIdentifier addKey = new KeyIdentifier();
	    addKey.setAltKey(true);
	    addKey.setKeyName("N");
	    addItem.setKeys(addKey);
	    menu.addItem(addItem);
	    addItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(!("1".equals(transSrvcId) || 
						StaticRef.B2B.equals(bizTyp)) || 
						StaticRef.SHPM_UNLOAD.equals(status)){
					MSGUtil.sayWarning("运输服务或业务类型或状态不符合,不允许新增明细!");
					return;
				}
				resetFieldCanEdit(true);
				shpmlstTable.OP_FLAG = "A";
				shpmlstTable.startEditingNew();
				itemRow = shpmlstTable.getAllEditRows()[shpmlstTable.getAllEditRows().length-1];
				shpmlstTable.setEditValue(itemRow, "OP_FLAG", "A");
				shpmlstTable.setEditValue(itemRow, "SHPM_NO", shpm_no);
				shpmlstTable.setEditValue(itemRow, "QNTY", "1");
				shpmlstTable.setEditValue(itemRow, "QNTY_EACH", "1");
				shpmlstTable.setEditValue(itemRow, "LD_QNTY", "0");
				shpmlstTable.setEditValue(itemRow, "UNLD_QNTY", "0");
				shpmlstTable.setEditValue(itemRow, "UNLD_GWGT", "0");
				shpmlstTable.setEditValue(itemRow, "UNLD_VOL", "0");
				
			}
		});
	    
	    shpmlstTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				resetFieldCanEdit(event.getRecord() == null);
			}
		});
	    
	    MenuItem delItem = new MenuItem("删除明细",StaticRef.ICON_DEL);
	    delItem.setKeyTitle("Alt+D");
	    KeyIdentifier delKey = new KeyIdentifier();
	    delKey.setAltKey(true);
	    delKey.setKeyName("D");
	    delItem.setKeys(delKey);
	    menu.addItem(delItem);
	    
	    delItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				if(!("1".equals(transSrvcId) || 
						StaticRef.B2B.equals(bizTyp)) || 
						StaticRef.SHPM_UNLOAD.equals(status)){
					MSGUtil.sayWarning("运输服务或业务类型或状态不符合,不允许删除明细!");
					return;
				}
				new DeleteProAction(shpmlstTable).onClick(event);
			}
		});
	    
	    menu.addItem(itemSeparator);
	    
	    MenuItem saveItem = new MenuItem("保存明细",StaticRef.ICON_SAVE);
	    saveItem.setKeyTitle("Alt+S");
	    KeyIdentifier saveKey = new KeyIdentifier();
	    saveKey.setAltKey(true);
	    saveKey.setKeyName("S");
	    saveItem.setKeys(saveKey);
	    menu.addItem(saveItem);
	    
	    saveItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				int[] edit_row = shpmlstTable.getAllEditRows();
				if(edit_row.length == 0){
					return;
				}
				if(checkTemperature()){
					MSGUtil.sayError("同一个托运单不能有2个温层的商品");
					return;
				}
				String msg = checkDetail();//不为空校验
				if(ObjUtil.isNotNull(msg)){
					MSGUtil.sayError(msg);
					return;
				}
				String appRow = " and SHPM_NO='" + shpm_no + "'";
				ArrayList<String> sqlArray = getSqlList(shpmlstTable);
				Util.async.doSave(logList, sqlArray,appRow, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)) {
							MSGUtil.showOperSuccess();
							shpmlstTable.OP_FLAG = "M";
							shpmlstTable.discardAllEdits();
							shpmlstTable.invalidateCache();
							shpmlstTable.fetchData(criteria,new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									
								}
							});
							
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
	    
	    MenuItem cancelItem = new MenuItem("取消新增",StaticRef.ICON_SAVE);
	    cancelItem.setKeyTitle("Alt+C");
	    KeyIdentifier cancelKey = new KeyIdentifier();
	    cancelKey.setAltKey(true);
	    cancelKey.setKeyName("C");
	    cancelItem.setKeys(cancelKey);
	    menu.addItem(cancelItem);
	    
	    cancelItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
			
			@Override
			public void onClick(MenuItemClickEvent event) {
				shpmlstTable.discardAllEdits();
			}
		});
	    
	    shpmlstTable.addRightMouseDownHandler(new RightMouseDownHandler() {
			
			@Override
			public void onRightMouseDown(RightMouseDownEvent event) {
				menu.showContextMenu();
				event.cancel();
				
			}
		});
	}
	
	private void resetFieldCanEdit(boolean flag){
		for (ListGridField field : shpmlstTable.getFields()) {
			if((field.getName().startsWith("UNLD") || flag) && !field.getName().equals("SHPM_ROW")){
				field.setCanEdit(true);
			}else{
				field.setCanEdit(false);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSqlList(SGTable table){
		ListGridRecord selectedRecord = shpmTable.getSelectedRecord();
		ArrayList<String> sqlList = new ArrayList<String>();
		logList = new ArrayList<String>();
		int[] edit_row = table.getAllEditRows();
		if(edit_row.length == 0){
			return sqlList;
		}
		String login_user = LoginCache.getLoginUser().getUSER_ID();
		String sql;
		HashMap<String, String> map;
		for(int i=0;i<edit_row.length;i++){
			map =(HashMap<String, String>) table.getEditValues(edit_row[i]);
			Map<String, String> logMap = new HashMap<String, String>();
			if(table.getRecord(edit_row[i]) != null){
				HashMap<String, String> record_map = Util.putRecordToModel(table.getRecord(edit_row[i]));
				Object[] key = map.keySet().toArray();
				for(int j=0;j<key.length;j++){
					record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),0.00).toString());
				}
				map = record_map;
			}
			
			map.put("TABLE", "TRANS_SHIPMENT_ITEM");
			map.put("ADDWHO", login_user);
			map.put("SHPM_NO", shpm_no);
			if(selectedRecord != null){
				map.put("ODR_NO", selectedRecord.getAttribute("ODR_NO"));
				logMap.put("DOC_NO", selectedRecord.getAttribute("SHPM_NO"));
			}
			sql = Util.mapToJson(map);
			sqlList.add(sql);
			
			//生成业条日志
			logMap.put("TABLE", "TRANS_TRANSACTION_LOG");
			logMap.put("ADDTIME", "sysdate");
			logMap.put("ADDWHO", login_user);
			if(ObjUtil.isNotNull(map.get("ID"))){//修改作业单明细
				logMap.put("ACTION_TYP", StaticRef.ODR_ITEM_UPDATE);
				logMap.put("DOC_TYP", StaticRef.SHPM_NO);
				logMap.put("NOTES", "修改作业单明细");
			}else{	//新增作业单明细
				logMap.put("ACTION_TYP", StaticRef.ODR_ITEM_ADD);
				logMap.put("DOC_TYP", StaticRef.SHPM_NO);
				logMap.put("NOTES", "新增作业单明细");
			}
			logList.add(Util.mapToJson(logMap));
			logMap.put("DOC_NO", selectedRecord.getAttribute("ODR_NO"));
			logMap.put("DOC_TYP", StaticRef.ODR_NO);
			logList.add(Util.mapToJson(logMap));
		}
		return sqlList;
	}
	
	private Canvas createUnloadList(){
	    VLayout vlay=new VLayout();
	    unloadTable=new SGTable(loadDS,"100%","100%",false,true,false){
	    	public DataSource getRelatedDataSource(ListGridRecord record){
	    		unloadDS=ShpmDS3.getInstance("V_SHIPMENT_HEADER2","TRANS_SHIPMENT_HEADER");
	    		load_no=record.getAttributeAsString("LOAD_NO");
	    		return unloadDS;
	    	}
	    	protected Canvas getExpansionComponent(final ListGridRecord record){
	    		VLayout vlay=new VLayout();
	    		unloadList=new SGTable();
	    		unloadList.setDataSource(getRelatedDataSource(record));
	    		unloadList.setShowFilterEditor(false);
	    		unloadList.setShowRowNumbers(true);
	    		unloadList.setAutoFetchData(false);
	    		unloadList.setAutoFitData(Autofit.VERTICAL);
	    		unloadList.setWidth("40%");
	    		unloadList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	    		unloadList.setHeight(50);
	    		unloadList.setAlign(Alignment.RIGHT);
	    		
	    		Criteria findValue=new Criteria();
	    		findValue.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
	    		findValue.addCriteria("LOAD_NO",load_no);
	    		if(biz_typ.length()>0){
	    			findValue.addCriteria("BIZ_TYP",biz_typ);
	    		}
	    		
	    		ListGridField UNLOAD_ID=new ListGridField("UNLOAD_ID");
	    		UNLOAD_ID.setHidden(true);
	    		ListGridField UNLOAD_NAME=new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),100);
	    		ListGridField QNTY=new ListGridField("QNTY","作业单票数",100);
	    		ListGridField STATUS=new ListGridField("STATUS",Util.TI18N.STATUS());
	    		STATUS.setHidden(true);
	    		ListGridField STATUS_NAME=new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),60);
	    		
	    		unloadList.setFields(UNLOAD_ID,UNLOAD_NAME,QNTY,STATUS,STATUS_NAME);
	    		vlay.addMember(unloadList);
	    		unloadList.fetchData(findValue);
	    		
	    		unloadList.addSelectionChangedHandler(new SelectionChangedHandler() {
					@Override
					public void onSelectionChanged(SelectionEvent event) {
						initUnloadButton();
					}
				});
	    		
	    		return vlay;
	    	}
	    };
	    
	    createTableList(unloadTable,false);
	    
	    unloadTable.setCanExpandRecords(true);
	    
	    unloadListItem=new SectionStackSection(Util.TI18N.LISTINFO());
	    unloadListItem.setItems(unloadTable);
	    unloadListItem.setExpanded(true);
	    unloadListItem.setControls(new SGPage(unloadTable,true).initPageBtn());
	    
	    unloadSectionStack=new SectionStack();
	    unloadSectionStack.addSection(unloadListItem);
	    unloadSectionStack.setWidth("100%");
	    
	    ToolStrip unload=new ToolStrip();
	    unload.setWidth("100%");
	    unload.setHeight("20");
	    unload.setAlign(Alignment.RIGHT);
	    IButton searchUnloadButton=createBtn(StaticRef.FETCH_BTN,TrsPrivRef.TRACK_P6);
		searchUnloadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(searchUnloadWin==null){
					searchUnloadForm=new SGPanel();
					searchUnloadForm.setDataSource(loadDS);
					searchUnloadWin=new SearchWin(loadDS,createSearchLoadForm(searchUnloadForm,true),unloadSectionStack.getSection(0)).getViewPanel();
					searchUnloadWin.setWidth(660);
					searchUnloadWin.setHeight(400);
				}else{
					searchUnloadWin.show();
				}
			}
		});
		unload.addMember(searchUnloadButton);
		vlay.setMembers(unload,unloadSectionStack);
	    	
	    return vlay;
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
		
		if(isPrivilege(TrsPrivRef.TRACK_P0_07)){
    		MenuItem expItem=new MenuItem("导出",StaticRef.ICON_EXPORT);
    		menu.addItem(expItem);
    		expItem.addClickHandler(new ExportAction(loadTable));
    	}
		
		loadTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
			@Override
			public void onShowContextMenu(ShowContextMenuEvent event) {
				menu.showContextMenu();
				event.cancel();
				if(menu.getItems().length==0){
					menu.hide();
				}
			}
		});
		loadTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				initLoadButton();
				enableOrDisables(add_tr_map, true);
				enableOrDisables(save_tr_map, false);
				enableOrDisables(del_tr_map, false);
				Criteria crit = new Criteria();
				crit.addCriteria("OP_FLAG", "M");
				Record rec = event.getRecord();
				ListGridRecord[] record = loadTable.getSelection();
				if(tabSelect== 4){
					if(record.length > 0){
						if(rec.getAttribute("PLATE_NO")!=null&&(!rec.getAttribute("PLATE_NO").equals(""))){
						crit.addCriteria("VEHICLE", rec.getAttribute("PLATE_NO"));
						crit.addCriteria("LOAD_NO", rec.getAttribute("LOAD_NO"));
						groupTable1.invalidateCache();
						groupTable1.fetchData(crit , new DSCallback() {
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								LoginCache.setPageResult(groupTable1, new FormItem(), new FormItem());
							}
						});
						}
					} 
				}else if(tabSelect== 3){
					if(record.length > 0){
						crit.addCriteria("LOAD_NO",rec.getAttribute("LOAD_NO"));
						trackTable.fetchData(crit);
					}
				}
			}
		});
		loadTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				panel.editRecord(event.getRecord());
			}
		});
		
		ToolStrip load=new ToolStrip();
		load.setWidth("100%");
		load.setHeight("20");
		load.setAlign(Alignment.RIGHT);
		IButton searchLoadButton=createBtn(StaticRef.FETCH_BTN,TrsPrivRef.TRACK_P5);
		searchLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {

				if(searchLoadWin==null){
					searchLoadForm=new SGPanel();
					searchLoadForm.setDataSource(loadDS);
					searchLoadWin=new SearchWin(loadDS,createSearchLoadForm(searchLoadForm,false),loadSectionStack.getSection(0)).getViewPanel();
					searchLoadWin.setWidth(660);
					searchLoadWin.setHeight(400);
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
		
		
		table.setFields(LOAD_NO,STATUS,STATUS_NAME,LOAD_STAT,LOAD_STAT_NAME, DISPATCH_STAT_NAME, TRANS_SRVC_ID, START_AREA_ID,START_AREA, END_AREA_ID, END_AREA, SUPLR_NAME, PLATE_NO,VEHICLE_TYP
				,DRIVER1, MOBILE1, REMAIN_GROSS_W,DEPART_TIME, DONE_TIME, UDF1,UDF2,TOT_QNTY,TOT_QNTY_EACH,NOTES, TOT_VOL, TOT_GROSS_W,EXEC_ORG_ID_NAME);
	}
	
    private void selectHander(final int tabSelect){
    	
    	Criteria crit = new Criteria();
    	crit.addCriteria("OP_FLAG", "M");
    	crit.addCriteria("SHPM_NO", LoadNo);
        if(tabSelect ==6 && ObjUtil.isNotNull(LoadNo)){
    		damageTable.invalidateCache();
    		damageTable.fetchData(crit);
       	}	

    	shpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
	    		enableOrDisables(add_tr_map, true);
				enableOrDisables(save_tr_map, false);
				enableOrDisables(del_tr_map, false);
				Criteria crit = new Criteria();
				crit.addCriteria("OP_FLAG", "M");
				Record rec = event.getRecord();
				ListGridRecord[] record = shpmTable.getSelection();
				loadReocrd = record;
				initShpmButton();
				if(tabSelect== 4){
					if(record.length > 0){
						if(rec.getAttribute("PLATE_NO")!=null&&(!rec.getAttribute("PLATE_NO").equals(""))){
						crit.addCriteria("VEHICLE", rec.getAttribute("PLATE_NO"));
						crit.addCriteria("LOAD_NO",rec.getAttribute("LOAD_NO"));
						groupTable1.invalidateCache();
						groupTable1.fetchData(crit , new DSCallback() {
							
							@Override
							public void execute(DSResponse response, Object rawData, DSRequest request) {
								LoginCache.setPageResult(groupTable1, new FormItem(), new FormItem());
							}
						});
						}
					} 
				}else if(tabSelect== 3){
					if(record.length > 0){
						crit.addCriteria("LOAD_NO",rec.getAttribute("LOAD_NO"));
						trackTable.fetchData(crit);
					}
				}else if (tabSelect == 5){
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

							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
						}else if(sf2.length() > 1 && sf2.length() == 0){
							
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,true);
							
						}else if(sf2.length() > 1 && sf1.length() > 1){

							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
						}
						
					}else if(record.length == 1){
						if(StaticRef.SHPM_LOAD.equals(record[0].getAttribute("STATUS"))){
						
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,true);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
						}else if(StaticRef.SHPM_UNLOAD.equals(record[0].getAttribute("STATUS"))){
											
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,true);
						}else {
			
							setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
							setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
							
							return;
						}
						
					}else {
					
						setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,false);
						setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,false);
					}
					}
				else{
					crit.addCriteria("SHPM_NO", rec.getAttribute("SHPM_NO"));
					damageTable.invalidateCache();
					damageTable.fetchData(crit);
				}
				panel.editRecord(event.getRecord());
			}
		});
    }
	private void createbottoInfo() { //在途跟踪  国际化
		groupTable1 = new SGTable(EsbDS);
		groupTable1.setShowFilterEditor(false);
		groupTable1.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable1.setShowRowNumbers(true);
		

		ListGridField VEHICLE = new ListGridField("VEHICLE","车牌",150);
		
		ListGridField SPEED = new ListGridField("SPEED","速度",100);
		
		ListGridField GPS_TIME = new ListGridField("GPS_TIME","跟踪时间",180);
		
		ListGridField OIL = new ListGridField("OIL","油量（L）",100);
		
		ListGridField OIL2 = new ListGridField("OIL2","油量2（L）",100);
		
		ListGridField TMP1 = new ListGridField("TMP1","温度1",100);
		
		ListGridField TMP2 = new ListGridField("TMP2","温度2",100);
		
		ListGridField TMP3 = new ListGridField("TMP3","温度3",100);
		
		ListGridField TMP4 = new ListGridField("TMP4","温度4",100);
		
		ListGridField PLACE_NAME = new ListGridField("PLACE_NAME","路名",260);
		
		ListGridField POWER = new ListGridField("POWER","电量（%）",100);
		
		groupTable1.setFields(VEHICLE,SPEED, GPS_TIME, OIL, OIL2, TMP1, 
				TMP2,TMP3, TMP4,PLACE_NAME,POWER);
		
		groupTable1.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
			}
		});


	}

	private Canvas createbottoInfo2() {
		HLayout hLayout =new HLayout();
		hLayout.setHeight("100%");
		hLayout.setWidth("40%");
		panel = new SGPanel();
		
		//【到货签收】左布局
		
		SGText START_UNLOAD_TIME = new SGText("START_UNLOAD_TIME",ColorUtil.getRedTitle("开始卸货时间"));
		//START_UNLOAD_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel,START_UNLOAD_TIME);
		
		SGText CAST_BILL_TIME = new SGText("CAST_BILL_TIME",ColorUtil.getRedTitle("投单时间"));
		//CAST_BILL_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel,CAST_BILL_TIME);
		
		SGText UNLOAD_TIME = new SGText("UNLOAD_TIME",ColorUtil.getRedTitle("完成卸货时间"));
		//UNLOAD_TIME.setDefaultValue(getCurTime().toString());
		Util.initDateTime(panel,UNLOAD_TIME);
		
		SGCombo ABNOMAL_STAT = new SGCombo("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT(),true);//运输异常
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setDefaultValue("5FB42E7D159346C395A2A34E0FE698C1");
		
		SGText ABNOMAL_NOTE = new SGText("ABNOMAL_NOTES", Util.TI18N.ABNOMAL_NOTE());//异常描述 
		//ABNOMAL_NOTE.setColSpan(4);
		
		SGText UDF1 = new SGText("UDF1", Util.TI18N.LOAD_UDF1(),true);//开门温度
		
		SGText UDF2 = new SGText("UDF2", Util.TI18N.LOAD_UDF2());//关门温度 
		
		SGCombo DRIVER_SERVICE = new SGCombo("SERVICE_CODE",Util.TI18N.DRIVER_SERVICE());
		Util.initCodesComboValue(DRIVER_SERVICE,"SERVICE_CODE");
		DRIVER_SERVICE.setDefaultValue("3ADB5A68660E48949AA4BC93942D54B0");
		
		SGCombo CUSTOMER_SERVICE = new SGCombo("SATISFY_CODE",Util.TI18N.CUSTOMER_SERVICE());
		Util.initCodesComboValue(CUSTOMER_SERVICE,"SATISFY_CODE");
		CUSTOMER_SERVICE.setDefaultValue("49C2B98FAFD845AB8C7CBE429B2E4F20");
		// 5：备注
		TextAreaItem notes = new TextAreaItem("TRACK_NOTES", "签收备注");
		notes.setStartRow(true);
		notes.setColSpan(6);
		notes.setHeight(40);
		notes.setWidth(3*FormUtil.Width);
		notes.setTitleOrientation(TitleOrientation.TOP);
		notes.setTitleVAlign(VerticalAlignment.TOP);
		
//		SGText SIGNATARY=new SGText("SIGNATARY","签收人");

		
		panel.setItems(CAST_BILL_TIME,START_UNLOAD_TIME,UNLOAD_TIME, ABNOMAL_STAT, DRIVER_SERVICE,CUSTOMER_SERVICE,UDF1,UDF2,ABNOMAL_NOTE,notes);
		hLayout.addMember(panel);
		
	
		return hLayout;
		
	}
	
	/**
	 * 在途跟踪按钮布局
	 */
//    public  void createTransFollowBtnWidget(ToolStrip followtoolStrip){
//    	followtoolStrip.setWidth("100%");
//    	followtoolStrip.setHeight("20");
//    	followtoolStrip.setPadding(2);
//    	followtoolStrip.setSeparatorSize(12);
//    	followtoolStrip.addSeparator();
//    	followtoolStrip.setMembersMargin(4);
//    	followtoolStrip.setAlign(Alignment.LEFT);
//    	
//		//新增按钮
//	    IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.TRACK_P1_01);
//        newButton.addClickHandler(new NewTrasFollowAction(groupTable1,this));
//        
//        //保存按钮
//        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.TRACK_P1_02);
//        saveButton.addClickHandler(new SaveTrasFollowAction(shpmTable,groupTable1,this,check_map));
//        
//        //取消按钮
//        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.TRACK_P1_03);
//        canButton.addClickHandler(new CancelAction(groupTable1,this));
//        
//        //yuanlei 2012-09-24 跟踪信息列表增加"导出"功能 
//        IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.TRACK_P1_04);
//        expButton.addClickHandler(new ExportAction(groupTable1));
//	    //yuanlei 
//        
//        followtoolStrip.setMembersMargin(3);
//        followtoolStrip.setMembers(newButton, saveButton, canButton, expButton);
//        add_map.put(TrsPrivRef.TRACK_P1_01, newButton);
//        save_map.put(TrsPrivRef.TRACK_P1_02, saveButton);
//        save_map.put(TrsPrivRef.TRACK_P1_03, canButton);
//        this.enableOrDisables(add_map, true);
//        this.enableOrDisables(save_map, false);
//        
//    }
    
    public void reciveBtnWidget(ToolStrip recivetoolStrip){
    	recivetoolStrip.setWidth("16%");
    	recivetoolStrip.setHeight("20");
    	recivetoolStrip.setPadding(2);
    	recivetoolStrip.setSeparatorSize(12);
    	recivetoolStrip.addSeparator();
    	recivetoolStrip.setMembersMargin(4);
    	recivetoolStrip.setAlign(Alignment.LEFT);
		//确认签收按钮
        recepitButton = createUDFBtn("确认收货", StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P2_01);
  
        recepitButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				checkVolGwgt(event);
			}
		});
        
      //取消签收按钮
        canReceButton = createUDFBtn("取消签收", StaticRef.ICON_CANCEL,TrsPrivRef.TRACK_P2_02);
        canReceButton.disable();
        canReceButton.addClickHandler(new CancleReceiptAction(this));
        
      //确认签收按钮
        cusRecepitButton = createUDFBtn("客户拒收", StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P2_03);
  
        cusRecepitButton.addClickHandler(new RejectReceiptAction(this));
        
        cancelRecepitButton=createUDFBtn("取消拒收",StaticRef.ICON_CANCEL,TrsPrivRef.TRACK_P2_05);
        cancelRecepitButton.disable();
        cancelRecepitButton.addClickHandler(new CancelRecepitAction(this));
        
        //标记签收按钮
        //preRecepitButton = createUDFBtn("标记签收", StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P2_04);
        //preRecepitButton.addClickHandler(new SignConfirmReceiptAction(this,false));
        
        //滞留按钮
        preRecepitButton = createUDFBtn("滞留", StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P2_04);
        preRecepitButton.addClickHandler(new HoldToDispatchAction(this));
        
        recivetoolStrip.setMembersMargin(4);
        recivetoolStrip.setMembers(recepitButton,canReceButton,cusRecepitButton,cancelRecepitButton,preRecepitButton);
//        recivetoolStrip.setMembers(canReceButton,recepitButton);
		
    }
    
	public void createBtnWidget(ToolStrip toolStrip) {
		
	}
	//调度单===》查询二级窗口
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
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		END_AREA_ID.setVisible(false);
		Util.initArea(END_AREA, END_AREA_ID);
		
		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		ORD_ADDTIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		ORD_ADDTIME_TO.setWidth(FormUtil.Width);
		
		SGDateTime END_LOAD_TIME_FROM = new SGDateTime("PRE_UNLOAD_TIME_FROM", "预计到货时间"); 
		END_LOAD_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime END_LOAD_TIME_TO = new SGDateTime("PRE_UNLOAD_TIME_TO", "到");
		END_LOAD_TIME_TO.setWidth(FormUtil.Width);
		
		END_LOAD_TIME_FROM.setDefaultValue(getCurTimes());
		END_LOAD_TIME_TO.setDefaultValue(getCurInitDays());
		
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
		BIZ_TYP.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if(ObjUtil.isNotNull(BIZ_TYP)){
					biz_typ=ObjUtil.isNotNull(BIZ_TYP.getValue())? BIZ_TYP.getValue().toString() : "";
				}
			}
		});
		
		SGText ODR_NO = new SGText("ODR_NO", Util.TI18N.ODR_NO());//托运单号
		
		SGCombo LOAD_STAT = new SGCombo("LOAD_STAT","装车状态");
		LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("10", "未装车");
		map.put("20", "已装车");
		LOAD_STAT.setValueMap(map);
		form.setItems(CUSTOMER,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,START_AREA_ID,
				START_AREA,END_AREA_ID,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,BIZ_TYP,
				DISPATCH_STAT,ROUTE_ID,ADDWHO,LOAD_STAT,EXEC_ORG_ID,END_LOAD_TIME_FROM,END_LOAD_TIME_TO,EXEC_ORG_ID_NAME,ODR_NO,C_ORG_FLAG,EXEC_FLAG);
		return form;
	}
	
	
	//作业单===》查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		form.setDataSource(ds);
		form.setAutoFetchData(false);
//		form.setWidth100();
		form.setCellPadding(2);
		//final String pre_time = getPreDay().toString();
//		final String time = getCurTime().toString();              //wangjun 
		
		
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
		
		SGText SHPM_NO =new SGText("SHPM_NO",Util.TI18N.SHPM_NO() );
		
		SGText LOAD_NO =new SGText("LOAD_NO",Util.TI18N.LOAD_NO() );
		
		//调度单状态 从
		SGCombo STATUS_FROM = new SGCombo("PLAN_STAT_FROM","作业单状态从");
		SGCombo STATUS_TO = new SGCombo("PLAN_STATUS_TO",Util.TI18N.STATUS_TO());
		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT,StaticRef.SHPM_LOAD);
		Util.initStatus(STATUS_TO,StaticRef.SHPMNO_STAT,StaticRef.SHPM_LOAD);
		
		//发运时间 从END_LOAD_TIME
		SGText END_LOAD_TIME_FROM = new SGText("END_LOAD_TIME_FROM", Util.TI18N.END_LOAD_TIME()+"从");
		SGText END_LOAD_TIME_TO = new SGText("END_LOAD_TIME_TO", "到");
		Util.initDateTime(searchForm,END_LOAD_TIME_FROM);
		Util.initDateTime(searchForm,END_LOAD_TIME_TO);
		END_LOAD_TIME_FROM.setDefaultValue(getCurInitDay());
		//END_LOAD_TIME_TO.setDefaultValue(getCurTime());
		//预达时间 从
		final SGText PRE_UNLOAD_TIME_FROM = new SGText("PRE_UNLOAD_TIME_FROM", Util.TI18N.PRE_UNLOAD_TIME());
		final SGText PRE_UNLOAD_TIME_TO = new SGText("PRE_UNLOAD_TIME_TO","到");
		//PRE_UNLOAD_TIME_FROM.setDefaultValue(pre_time);//wangjun 2010-4-24
		//PRE_UNLOAD_TIME_TO.setDefaultValue(time);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_TO);
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initSupplier(SUPLR_NAME, "");//供应商
//		Util.initCodesComboValue(SUPLR_NAME,"BAS_SUPPLIER", "ID","SUPLR_CNAME");
		
		SGText PLATE_NO =new SGText("PLATE_NO",Util.TI18N.PLATE_NO() );
		
		//起点区域
		final TextItem AREA_ID = new TextItem("AREA_ID");
		AREA_ID.setVisible(false);
		
		SGText UNLOAD_NAME = new SGText("END_UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
		Util.initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE","ID","SRVC_NAME");
		
		SGCombo TRANS_COND = new SGCombo("REFENENCE4",Util.TI18N.TRANS_COND());
        Util.initCodesComboValue(TRANS_COND, "TRANS_COND");
		
		TextItem SIGN_ORG_ID = new TextItem("SIGN_ORG_ID");
		SIGN_ORG_ID.setVisible(false);
		SGText SIGN_ORG_ID_NAME = new SGText("SIGN_ORG_ID_NAME",Util.TI18N.ORG_CNAME());
		Util.initOrg(SIGN_ORG_ID_NAME, SIGN_ORG_ID, false, "50%", "50%");
		SIGN_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		SIGN_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());//执行机构
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		
		SGCheck EXEC_FLAG = new SGCheck("EXEC_FLAG", "参与执行");	
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构

		//yuanlei 2012-12-7
		//SGText CUSTOMER=new SGText("CUSTOMER_NAME",Util.TI18N.CUSTOMER());//客户   二级窗口 ？？
		//CUSTOMER.setWidth(128);
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER());
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		//yuanlei
		
		SGText UNLOAD_TIME_FROM = new SGText("UNLOAD_TIME_FROM", "到货时间  从");//
		SGText UNLOAD_TIME_TO = new SGText("UNLOAD_TIME_TO", "到");//
		Util.initDateTime(searchForm,UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,UNLOAD_TIME_TO);
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME");
		
		SGText REFENENCE1 = new SGText("REFENENCE1", Util.TI18N.REFENENCE1());//收货方
		
		SGText ODR_NO = new SGText("ODR_NO", Util.TI18N.ODR_NO(),true);//托运单号
		
		form.setItems(CUSTOM_ODR_NO,SHPM_NO,LOAD_NO,CUSTOMER,STATUS_FROM,STATUS_TO,
				AREA_ID,END_LOAD_TIME_FROM,END_LOAD_TIME_TO,PRE_UNLOAD_TIME_FROM,PRE_UNLOAD_TIME_TO,
				UNLOAD_TIME_FROM,UNLOAD_TIME_TO,SUPLR_NAME,PLATE_NO,UNLOAD_NAME,TRANS_SRVC_ID,TRANS_COND,ROUTE_ID,SIGN_ORG_ID_NAME,
				SIGN_ORG_ID,REFENENCE1, ODR_NO,C_ORG_FLAG, EXEC_FLAG,C_RDC_FLAG);
		
		return form;
		
	}
	
	public void createForm(DynamicForm form) {
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
		if(searchUnloadWin!=null){
			searchUnloadWin.destroy();
			searchUnloadForm.destroy();
		}
		if(searchLoadWin!=null){
			searchLoadWin.destroy();
			searchLoadForm.destroy();
		}
	}

	private void shpmentGrid(final SGTable shpmTable){
        
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_TRACK);
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
		
	}
	
	private SGTable createLossDamageInfo() {
		damageTable = new SGTable(LossDamageDS,"100%","100%");
		damageTable.addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
				initCancelBtn();
			}
		});
		damageTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initDMAddBtn();
			}
		});
		damageTable.setShowFilterEditor(false);
		damageTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		damageTable.setShowRowNumbers(true);
//		damageTable.setDataSource(LossDamageDS);

		ListGridField ODR_NO = new ListGridField("ODR_NO", "",0);//货品代码
		ODR_NO.setHidden(true);
		ListGridField SKU_ID = new ListGridField("SKU_ID",Util.TI18N.SKU(),70);//货品代码
		SKU_ID.setHidden(true);
		
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);//货品名称
		SKU_NAME.setCanEdit(true);
		SKU_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()));
		
//		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),80);//货品规格型号
//		SKU_SPEC.setCanEdit(true);
		
		final ListGridField DAMA_TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.UOM(),60);
		DAMA_TRANS_UOM.setCanEdit(true); //DAMA_TRANS_UOM
		DAMA_TRANS_UOM.setTitle(ColorUtil.getRedTitle(Util.TI18N.UOM()));
		
		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK_ID(),80);
		PACK_ID.setCanEdit(true); //包装
		PACK_ID.setAlign(Alignment.LEFT);  
		
		final ListGridField TRANS_QNTY = new ListGridField("QNTY","货损数量",70);
		TRANS_QNTY.setCanEdit(true);
		TRANS_QNTY.setAlign(Alignment.RIGHT);//货损数量 
//		TRANS_QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRANS_QNTY()));
		Util.initFloatListField(TRANS_QNTY, StaticRef.QNTY_FLOAT);
		
		final ListGridField LOSS_DAMAGE_TYP = new ListGridField("LOSS_DAMAGE_TYP","货损类型",70);
		LOSS_DAMAGE_TYP.setCanEdit(true);
		Util.initCodesComboValue(LOSS_DAMAGE_TYP, "LOSS_DAMAGE_TYP");	
		
		final ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON(),250);
		REASON.setCanEdit(true);
		REASON.setAlign(Alignment.LEFT);//  原因描述
		
		final ListGridField AMOUNT = new ListGridField("AMOUNT","货损金额",80);
		AMOUNT.setCanEdit(true);
		AMOUNT.setAlign(Alignment.RIGHT);//  货损金额 
		Util.initFloatListField(AMOUNT, StaticRef.PRICE_FLOAT);
		
		final ListGridField DUTYER = new ListGridField("DUTYER",Util.TI18N.DUTYER(),75);
		DUTYER.setCanEdit(true);
		DUTYER.setAlign(Alignment.LEFT);//  责任人
		
		ListGridField COMPANY_ACOUNT = new ListGridField("COMPANY_AMOUNT","公司承担金额",95);
		COMPANY_ACOUNT.setCanEdit(true);
		
		ListGridField DRIVER_ACOUNT = new ListGridField("DRIVER_AMOUNT","司机承担金额",95);
		DRIVER_ACOUNT.setCanEdit(true);
		
		ListGridField DUTY_TO = new ListGridField("DUTY_TO","货损责任",95);
		Util.initCodesComboValue(DUTY_TO, "DUTY_TO");
		DUTY_TO.setCanEdit(true);
		
		final ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),70);
		SHPM_NO.setHidden(true);
		final ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),70);
		LOAD_NO.setHidden(true);
		final ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),50);
		CUSTOMER_NAME.setHidden(true);
		
		TRANS_QNTY.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				final int row = event.getRowNum();
				if(event.getNewValue() == null)
					return;
				String qty = event.getNewValue().toString();
				ArrayList<String> valList = new ArrayList<String>();
				if(damageTable.getEditValue(row, "PACK_ID") != null){
					valList.add(damageTable.getEditValue(row, "PACK_ID").toString());
				}else{
					valList.add(event.getRecord().getAttribute("PACK_ID"));
				}
				if(damageTable.getEditValue(row, "TRANS_UOM") != null){
					valList.add(damageTable.getEditValue(row, "TRANS_UOM").toString());
				}else{
					valList.add(event.getRecord().getAttribute("TRANS_UOM"));
				}
				if(damageTable.getEditValue(row, "SKU_ID") != null){
					valList.add(damageTable.getEditValue(row, "SKU_ID").toString());
				}else{
					valList.add(event.getRecord().getAttribute("SKU_ID"));
				}
				valList.add(qty);
				Util.db_async.QTY_CONVER(valList, new AsyncCallback<ArrayList<String>>() {
					
					@Override
					public void onSuccess(ArrayList<String> result) {
						damageTable.setEditValue(row, "QNTY_EACH", result.get(2)); //最小包装数量
						
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
				
				
			}
		});
		
		PACK_ID.addChangedHandler(new com.smartgwt.client.widgets.grid.events.ChangedHandler() {
			
			@Override
			public void onChanged(
					com.smartgwt.client.widgets.grid.events.ChangedEvent event) {
				final int row = event.getRowNum();
				String pack_id = ObjUtil.ifObjNull(event.getValue(),"").toString();
				if(ObjUtil.isNotNull(pack_id)) {
					pack_id =" where id='" +pack_id +"'";
					DAMA_TRANS_UOM.setValueMap("");
					Util.async.getComboValue("V_BAS_PACKAGE", "UOM", "DESCR", pack_id, "", new AsyncCallback<LinkedHashMap<String, String>>() {
						
						public void onFailure(Throwable caught) {	
							;
						}
						public void onSuccess(LinkedHashMap<String, String> result) {
							if(result != null && result.size() > 0) {
								Object[] obj = result.keySet().toArray();
								DAMA_TRANS_UOM.setValueMap(result);
								damageTable.setEditValue(row,"TRANS_UOM", ObjUtil.ifObjNull(result.get(obj[1]), "").toString());
							}
						}					
					});
				}
			}
		});
		
		damageTable.setFields(ODR_NO, SKU_ID,SKU_NAME,PACK_ID,DAMA_TRANS_UOM, TRANS_QNTY,
				LOSS_DAMAGE_TYP, AMOUNT,COMPANY_ACOUNT,DRIVER_ACOUNT, DUTYER,DUTY_TO,REASON,SHPM_NO,LOAD_NO);
		

		FormItemIcon icon1 = new FormItemIcon();
		SKU_NAME.setIcons(icon1);
		SKU_NAME.setShowSelectedIcon(true);
		icon1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
					new TrackSkuWin(damageTable,itemRow,"40%", "38%",LoadNo).getViewPanel();
			}
		});
		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
//		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where 1=1", "");
		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");
		
		damageTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				initDMSaveBtn();
			}
		});

		return damageTable;
	}
	
	private SGTable createTrackInfo(){
		trackTable  = new SGTable(trackDS,"100%","100%",false,true,false);
		//trackTable.setShowFilterEditor(false);
		//trackTable.setShowRowNumbers(true);
		trackTable.setCanEdit(true);
		
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC",ColorUtil.getRedTitle("当前位置"),220);
		ListGridField TEMPERATURE = new ListGridField("TEMPERATURE",ColorUtil.getRedTitle("当前温度"),70);
		ListGridField TRACER = new ListGridField("TRACER","跟踪人",70);
		TRACER.setCanEdit(false);
		ListGridField TRACE_TIME = new ListGridField("TRACE_TIME","跟踪时间",120);
		TRACE_TIME.setCanEdit(false);
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT","当前状态",80);
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ListGridField ABNOMAL_NOTE = new ListGridField("ABNOMAL_NOTE","异常备注",80);
		ListGridField PRE_SOLVE_TIME = new ListGridField("PRE_SOLVE_TIME","预计解决时间",120);
		Util.initDateTime(trackTable,PRE_SOLVE_TIME);
		ListGridField SOLVE_TIME = new ListGridField("SOLVE_TIME","实际解决时间",120);
		Util.initDateTime(trackTable,SOLVE_TIME);
		ListGridField SOLUTION = new ListGridField("SOLUTION","解决措施",80);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","",80);
		LOAD_NO.setHidden(true);
		
		trackTable.setFields(CURRENT_LOC,TEMPERATURE,TRACER,TRACE_TIME,ABNOMAL_STAT,ABNOMAL_NOTE,PRE_SOLVE_TIME,SOLVE_TIME,SOLUTION,LOAD_NO);
		
//		trackTable.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				
//			}
//		});
		
		trackTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(add_tr_map, true);
		        enableOrDisables(del_tr_map, true);
		        enableOrDisables(save_tr_map, false);
			}
		});
		
		trackTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_tr_map, false);
				enableOrDisables(del_tr_map, false);
		        enableOrDisables(save_tr_map, true);
			}
		});
		
		return trackTable;
	}
	
	private ToolStrip createTrackBtn() {
		ToolStrip Strip = new ToolStrip();
		Strip.setWidth("100%");
		Strip.setHeight("20");
		Strip.setMembersMargin(4);
		Strip.setAlign(Alignment.LEFT);
		
		//新增按钮
	    IButton newButton1 = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.TRACK_P1_01);
	    newButton1.addClickHandler(new NewTrackAction(trackTable,loadTable,detail_tr_map,this));
        
        //保存按钮
	    IButton saveButton1= createBtn(StaticRef.SAVE_BTN,TrsPrivRef.TRACK_P1_02);
        saveButton1.addClickHandler(new SaveTrackAction(trackTable,detail_map,this));
        
        //删除按钮
        IButton delButton1 = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.TRACK_P1_03);
        delButton1.addClickHandler(new DeleteTrackAction(trackTable,this));
        
        //取消按钮
        IButton canButton1 = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.TRACK_P1_04);
        canButton1.addClickHandler(new CancelTrackAction(trackTable,this));
        
        Strip.setMembersMargin(4);
        Strip.setMembers(newButton1, saveButton1, delButton1, canButton1);
        
        add_tr_map = new HashMap<String, IButton>();
        save_tr_map = new HashMap<String, IButton>();
        del_tr_map = new HashMap<String, IButton>();
        
        add_tr_map.put(TrsPrivRef.TRACK_P1_01, newButton1);
        del_tr_map.put(TrsPrivRef.TRACK_P1_03, delButton1);
        save_tr_map.put(TrsPrivRef.TRACK_P1_02, saveButton1);
        save_tr_map.put(TrsPrivRef.TRACK_P1_04, canButton1);
        
        this.enableOrDisables(add_tr_map, false);
        enableOrDisables(del_tr_map, false);
        this.enableOrDisables(save_tr_map, false);
        
        return Strip;
	}
	
	public void initVerify() {
		check_map.put("TABLE", "TRANS_LOAD_HEADER");
		check_map.put("ADDTIME",StaticRef.CHK_DATE+Util.TI18N.ADDTIME());
		check_map.put("END_LOAD_TIME", StaticRef.CHK_DATE+Util.TI18N.END_LOAD_TIME());
		check_map.put("PRE_UNLOAD_TIME", StaticRef.CHK_DATE+Util.TI18N.PRE_UNLOAD_TIME());
		check_map.put("END_LOAD_TIME", StaticRef.CHK_DATE+Util.TI18N.END_LOAD_TIME());
		check_map.put("TRACE_TIME", StaticRef.CHK_DATE+Util.TI18N.TRACE_TIME());
		check_map.put("SOLVE_TIME", StaticRef.CHK_DATE+Util.TI18N.SOLVE_TIME());
		check_map.put("PRE_SOLVE_TIME",StaticRef.CHK_DATE+Util.TI18N.PRE_SOLVE_TIME());
		check_map.put("MAX_PRE_UNLOAD_TIME",StaticRef.CHK_DATE+Util.TI18N.PRE_UNLOAD_TIME());
		//check_map.put("CAST_BILL_TIME",StaticRef.CHK_DATE+"投单时间");
	
		detail_map.put("TABLE", "TRANS_TRACK_TRACE");
		detail_map.put("CURRENT_LOC", StaticRef.CHK_NOTNULL+"当前位置");
		detail_map.put("TEMPERATURE", StaticRef.CHK_NOTNULL+"当前温度");
		detail_map.put("TRACE_TIME", StaticRef.CHK_DATE+"跟踪时间");
		detail_map.put("PRE_SOLVE_TIME", StaticRef.CHK_DATE+"预计解决时间");
		detail_map.put("SOLVE_TIME", StaticRef.CHK_DATE+"实际解决时间");
		
		detail_tr_map.put("TRACER",LoginCache.getLoginUser().getUSER_NAME().replace("\"", ""));
		detail_tr_map.put("ABNOMAL_STAT","5FB42E7D159346C395A2A34E0FE698C1");
	}
	
	private void shpmlsEdit(boolean boo){
		if(shpmlstTable != null){
			shpmlstTable.getField("UNLD_QNTY").setCanEdit(boo);
			//系统参数，签收时作业单体积，毛重是否可编辑
			if("Y".equals(LoginCache.getParamString("RECE_QNTYCANEDIT"))){
				shpmlstTable.getField("UNLD_GWGT").setCanEdit(boo);
				shpmlstTable.getField("UNLD_VOL").setCanEdit(boo);
			}
		}
	}
	
	private ToolStrip createDamageBtn() {
		ToolStrip toolStrip2 = new ToolStrip();
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		toolStrip2.setMembersMargin(4);
		toolStrip2.setAlign(Alignment.LEFT);
		
		//新增按钮
	    IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.TRACK_P7_01);
        newButton.addClickHandler(new NewShpmManageAction(this,damageTable));
        
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.TRACK_P7_02);
       //saveButton.addClickHandler(new SaveManageAction(damageTable,check_map));
        saveButton.addClickHandler(new SaveShpmAction(damageTable,null,this));
        
        //删除按钮
        IButton delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.TRACK_P7_03);
        delButton.addClickHandler(new DeleteTransFollowAction(damageTable,this));
        
        //取消按钮
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.TRACK_P7_04);
        canButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				damageTable.discardAllEdits();
				initDMCancelBtn();
			}
		});
        
        //IButton confButton = createUDFBtn("确认",StaticRef.ICON_CONFIRM,TrsPrivRef.TRACK_P7_05);
        
        toolStrip2.setMembersMargin(4);
        toolStrip2.setMembers(newButton, saveButton, delButton, canButton);
        
        add_dm_map = new HashMap<String, IButton>();
        save_dm_map = new HashMap<String, IButton>();
        del_dm_map = new HashMap<String, IButton>();
        add_dm_map.put(TrsPrivRef.TRACK_P7_01, newButton);
        del_dm_map.put(TrsPrivRef.TRACK_P7_03, delButton);
        save_dm_map.put(TrsPrivRef.TRACK_P7_02, saveButton);
        save_dm_map.put(TrsPrivRef.TRACK_P7_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        return toolStrip2;
	}
	
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
	
	@SuppressWarnings("unchecked")
	public String checkDetail(){
		HashMap<String, String> map;
		HashMap<String, String> detail_ck_map = new HashMap<String, String>();
		detail_ck_map.put("TABLE", "TRANS_ORDER_ITEM");
		//detail_ck_map.put("SKU_CODE", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_ID());
		detail_ck_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		detail_ck_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("LD_QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("UNLD_QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("UNLD_GWGT", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("UNLD_VOL", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("TEMPERATURE1", StaticRef.CHK_NOTNULL + Util.TI18N.TEMPERATURE());
		int[] edit_row = shpmlstTable.getAllEditRows();
		if(edit_row.length == 0){
			return "";
		}
		StringBuffer msg = new StringBuffer();
		for(int i=0;i<edit_row.length;i++){
			map =(HashMap<String, String>) shpmlstTable.getEditValues(edit_row[i]);
			
			//fanglm 2011-2-27
			if(shpmlstTable.getRecord(edit_row[i]) != null){
				HashMap<String, String> record_map = Util.putRecordToModel(shpmlstTable.getRecord(edit_row[i]));
				Object[] key = map.keySet().toArray();
				for(int j=0;j<key.length;j++){
					record_map.put(key[j].toString(), ObjUtil.ifObjNull(map.get(key[j]),0.00).toString());
				}
				map = record_map;
			}
			
			
			ArrayList<Object> obj = Util.getCheckResult(map, detail_ck_map);
			if(obj != null && obj.size() > 1) {
				String result = obj.get(0).toString();
				if(!result.equals(StaticRef.SUCCESS_CODE)) {
					msg.append(obj.get(1).toString());

				}
			}
			if(map.get("QNTY") != null && Double.valueOf(String.valueOf(map.get("QNTY"))) <= 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行本单量必须大于0！");
			}
			
			if(map.get("LD_QNTY") != null && Double.valueOf(String.valueOf(map.get("LD_QNTY"))) < 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行发货数量必须大于等于0！");
			}
			
			if(map.get("UNLD_QNTY") != null && Double.valueOf(String.valueOf(map.get("UNLD_QNTY"))) < 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行收货数量必须大于等于0！");
			}
			if(map.get("UNLD_GWGT") != null && Double.valueOf(String.valueOf(map.get("UNLD_GWGT"))) < 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行毛重必须大于等于0！");
			}
			if(map.get("UNLD_VOL") != null && Double.valueOf(String.valueOf(map.get("UNLD_VOL"))) < 0){
				msg.append("货品明细第");
				msg.append(edit_row[i]+1);
				msg.append("行体积必须大于等于0！");
			}
		}
		return msg.toString();
	}
	
	/**
	 * 检查是否有两个不同温层存在
	 * true有,false没有
	 */
	@SuppressWarnings("unchecked")
	private boolean checkTemperature(){
		ListGridRecord[] records = shpmlstTable.getRecords();
		int[] edit_row = shpmlstTable.getAllEditRows();
		Map<String, String> tempMap=new HashMap<String, String>();
		Map<String, String> idTMap=new HashMap<String, String>();
		if(records.length + edit_row.length < 2){
			return false;
		}
		for (ListGridRecord r : records) {
			tempMap.put(r.getAttributeAsString("TEMPERATURE1"), r.getAttribute("ID"));
			idTMap.put(r.getAttribute("ID"), r.getAttributeAsString("TEMPERATURE1"));
		}
		for (int i : edit_row) {
			String temperature1 = ((HashMap<String, String>)shpmlstTable.getEditValues(i)).get("TEMPERATURE1");
			if(!ObjUtil.isNotNull(temperature1)){
				continue;
			}
			String id = ((HashMap<String, String>)shpmlstTable.getEditValues(i)).get("ID");
			id = id==null?"true":id;
			if(id != null && ObjUtil.isNotNull(idTMap.get(id))){
				if(!idTMap.get(id).equals(temperature1) 
						&& (tempMap.size() > 1 || records.length + edit_row.length == 2)){
					tempMap.remove(idTMap.get(id));
				}
			}
			tempMap.put(temperature1, "true");
		}
		if(tempMap.size() > 1){
			return true;
		}
		return false;
	}
	public void initButton(boolean bo1,boolean bo2,boolean bo3,boolean bo4,boolean bo5){
		setButtonEnabled(TrsPrivRef.TRACK_P2_01,recepitButton,bo1);
        setButtonEnabled(TrsPrivRef.TRACK_P2_02,canReceButton,bo2);
        setButtonEnabled(TrsPrivRef.TRACK_P2_03,cusRecepitButton,bo3);
        setButtonEnabled(TrsPrivRef.TRACK_P2_05,cancelRecepitButton,bo4);
        setButtonEnabled(TrsPrivRef.TRACK_P2_04,preRecepitButton,bo5);
	}
	public void initButton(boolean bo){
		initButton(bo, bo, bo, bo, bo);
	}
	private void initShpmButton(){
		Record[] record=shpmTable.getSelection();
		if(record.length > 0){
			Record reco = shpmTable.getSelectedRecord();
			if(Integer.valueOf(StaticRef.SHPM_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))
					&& Integer.valueOf(StaticRef.SHPM_PART_UNLOAD) <= Integer.valueOf(reco.getAttribute("STATUS"))){
				initButton(false, true, false, false, false);
			}else if(Integer.valueOf(StaticRef.SHPM_PART_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))
					&& Integer.valueOf(StaticRef.SHPM_LOAD) <= Integer.valueOf(reco.getAttribute("STATUS"))){
				initButton(true, false, true, false, true);
			}else if(StaticRef.SHPM_REJECT.equals(reco.getAttribute("STATUS"))){
				initButton(false,false,false,true,false);
			}else{
				initButton(false);
			}
		}else{
			initButton(false);
		}
	}
	
	private void initLoadButton(){
		Record reco=loadTable.getSelectedRecord();
		if(ObjUtil.isNotNull(reco)){
			if(Integer.valueOf(StaticRef.TRANS_DEPART) <= Integer.valueOf(reco.getAttribute("STATUS"))
					&& Integer.valueOf(StaticRef.TRANS_PART_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))){
				initButton(true, false, false, false, false);
			}else if(Integer.valueOf(StaticRef.TRANS_PART_UNLOAD) <= Integer.valueOf(reco.getAttribute("STATUS"))
					&& Integer.valueOf(StaticRef.TRANS_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))){
				initButton(false, true, false, false, false);
			}else{
				initButton(false);
			}
		}else{
			initButton(false);
		}
	}
	
	private void initUnloadButton(){
		if(ObjUtil.isNotNull(unloadList)){
			Record reco=unloadList.getSelectedRecord();
			if(ObjUtil.isNotNull(reco)){
				if(Integer.valueOf(StaticRef.SHPM_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))
						&& Integer.valueOf(StaticRef.SHPM_PART_UNLOAD) <= Integer.valueOf(reco.getAttribute("STATUS"))){
					initButton(false, true, false, false, false);
				}else if(Integer.valueOf(StaticRef.SHPM_PART_UNLOAD) >= Integer.valueOf(reco.getAttribute("STATUS"))
						&& Integer.valueOf(StaticRef.SHPM_LOAD) <= Integer.valueOf(reco.getAttribute("STATUS"))){
					initButton(true, false, false, false, false);
				}else{
					initButton(false);
				}
			}else{
				initButton(false);
			}
		}else{
			initButton(false);
		}
	}

	private void checkVolGwgt(final ClickEvent event){
		//20150310 DAVID 修改bug增加了非空判断
		if (ObjUtil.isNotNull(shpmlstTable) && ObjUtil.isNotNull(shpmTable)){
			ListGridRecord[] shpmList = shpmTable.getSelection();
			ListGridRecord[] itemList  = shpmlstTable.getRecords();
			if(shpmList.length > 0){
				for (ListGridRecord shpmRecord : shpmList) {
					for(int i=0 ; i<itemList.length ; i++){
						String shpmNo = ObjUtil.ifObjNull(shpmlstTable.getEditValue(i, "SHPM_NO"), itemList[i].getAttribute("SHPM_NO")).toString();
						String volStr = ObjUtil.ifObjNull(shpmlstTable.getEditValue(i, "UNLD_VOL"),itemList[i].getAttribute("UNLD_VOL")).toString(); //20150310 DAVID 修改bug字段名字
						String gwgtStr = ObjUtil.ifObjNull(shpmlstTable.getEditValue(i, "UNLD_GWGT"), itemList[i].getAttribute("UNLD_GWGT")).toString();//20150310 DAVID 修改bug字段名字
						if(shpmRecord.getAttribute("SHPM_NO").equals(shpmNo)){
							Double vol = Double.parseDouble(ObjUtil.ifNull(volStr,"0"));
							Double gwgt = Double.parseDouble(ObjUtil.ifNull(gwgtStr,"0"));
							if(vol == 0 && gwgt == 0){
								SC.confirm("警告", "作业单号["+shpmNo+"]毛重和体积都为零，是否继续?", new BooleanCallback() {
									@Override
									public void execute(Boolean value) {
										if(value.booleanValue()){
											new ConfirmReceiptAction(getThis(), false).onClick(event);
										}
									}
								});
								return;
							}
						}
					}
				}
			}
		}else if("2_tmstrackview".equals(tabSet.getSelectedTab().getID())){//增加逻辑
			if(unloadList == null || 
					unloadList.getSelection() == null || 
					unloadList.getSelection().length == 0)
				return;
			StringBuffer sb = new StringBuffer();
			sb.append("select instr(org_index,'");
			sb.append(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
			sb.append("') as pos from bas_org where id in(select exec_org_id from bas_address where id in(");
			for (ListGridRecord r : unloadList.getSelection()) {
				sb.append("'");
				sb.append(r.getAttributeAsString("UNLOAD_ID"));
				sb.append("',");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append("))");
			Util.async.queryData(sb.toString(), false, new AsyncCallback<Map<String,Object>>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@SuppressWarnings("unchecked")
				@Override
				public void onSuccess(Map<String, Object> result) {
					List<List<String>> resultList = (List<List<String>>)result.get("data");
					if(resultList != null){
						for (List<String> list : resultList) {
							if(!ObjUtil.isNotNull(list.get(0)) || Integer.parseInt(list.get(0)) == 0){
								MSGUtil.sayError("当前用户所属组织机构及其下级机构不包含所选的卸货地所在的组织机构！");
								return;
							}
						}
						new ConfirmReceiptAction(getThis(), false).onClick(event);
					}
				}
			});
			return;
		}
		new ConfirmReceiptAction(getThis(), false).onClick(event);
	}
	
	public TmsTrackView getThis(){
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
	var now = new Date(new Date()-24*60*60*1000);
	var year=now.getFullYear();
	var m=now.getMonth()+1;
	var month = (m < 10) ? '0' + m : m;
	var day="01";	
	var res = year+"-"+month+"-"+ day + " 00:00";
	return res;

}-*/;
	/**
	 * 明天（年,月,日   时:分:秒）
	 * @return
	 */
	public static native String getCurInitDays() /*-{ 
	
	var now = new Date();
	var year=now.getFullYear();
	var m=now.getMonth()+1;
	var month = (m < 10) ? '0' + m : m;
	var day=now.getDate()+1;
	var hour=now.getHours();
	var minute=now.getMinutes();
	if (minute < 10) {
	    minute = "0" + minute;
	}
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
/**
 * 今天（年,月,日   时:分:秒）
 * @return
 */
public static native String getCurTimes() /*-{

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
	var res = year+"-"+month+"-"+ day + " 00:00";
	return res;
}-*/;

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TmsTrackView view = new TmsTrackView();
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
