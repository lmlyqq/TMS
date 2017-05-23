package com.rd.client.view.tms;

import java.util.HashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.TranOrderDS;
import com.rd.client.ds.tms.TranOrderItemDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
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
 * 运输管理-->客户回访
 * @author fanglm
 *
 */
@ClassForNameAble
public class CustomerVisitView extends SGForm implements PanelFactory {

	private DataSource ds;
	public  SGTable groupTable1;
	private SGPanel searchForm;
	private Window searchWin;
	public SGPanel panel;
	
	public SGTable table;       //已调作业单表
	public SGTable itemTable;    //已调作业单明细表
	
	
	private DataSource detailDS;
	public Record shpmnorecord;
	public Record grouprecords;
	public ListGridRecord[] loadReocrd;
	
	private SectionStack sectionStack;
	private  SectionStackSection  listItem;
	
	public IButton recepitButton;
	public IButton canReceButton;
	
	public IButton cusRecepitButton;
	public IButton cusCanReceButton;
	
	public int tabSelect = 0;
	
	public HashMap<String,IButton> add_dm_map; //新增、
	public HashMap<String,IButton> save_dm_map; //保存、取消按钮
	public HashMap<String, IButton> del_dm_map; //删除按钮


	/*public CustomerVisitView(String id) {
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
		ds = TranOrderDS.getInstance("V_ORDER_HEADER","TRANS_ORDER_HEADER");
		detailDS = TranOrderItemDS.getInstance("V_ORDER_ITEM","TRANS_ORDER_ITEM");
		
		
	    ToolStrip toolStrip = new ToolStrip();//主布局按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		
		//在途跟踪按钮布局
		ToolStrip followtoolStrip = new ToolStrip();
		followtoolStrip.setAlign(Alignment.LEFT);
		
		//到货签收按钮布局
		ToolStrip recivetoolStrip = new ToolStrip();
		recivetoolStrip.setAlign(Alignment.LEFT);
		
		//主布局
		HStack stack =new HStack();
		stack.setHeight("80%");
		stack.setWidth100();
		
		//上边布局
		TabSet TabSet = new TabSet();
		TabSet.setWidth100();
		TabSet.setHeight("55%");	
		
		initVerify(); 
		
		 table = new SGTable(ds,"100%","100%"){
			
			protected Canvas getExpansionComponent(final ListGridRecord record) {    
				  
				VLayout layout = new VLayout();
                
                itemTable = new SGTable(detailDS,"100%","50",false,true,false);
                itemTable.setCanEdit(false);
                itemTable.setAutoFitData(Autofit.VERTICAL);
                
                ListGridField ROW = new ListGridField("ODR_ROW","行号",30);
        		ListGridField LOAD_ID = new ListGridField("LOAD_NAME", Util.TI18N.LOAD_ID(), 150);
        		LOAD_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.LOAD_ID()));
        		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 120);
        		SKU_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()));
        		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 80);
        		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC", Util.TI18N.MODEL(), 80);
        		final ListGridField UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 50);
        		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 70);
        		ListGridField QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.QNTY_EACH(),70);
        		QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.PACK_QTY()));
        		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 90);
        		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 90);
        		ListGridField LOTATT01 =new ListGridField("LOT_ID",Util.TI18N.LOTATT01(),90);
        		ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),90);

        		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
        		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
        		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
        		Util.initFloatListField(QNTY_EACH, StaticRef.QNTY_FLOAT);
        		
        		itemTable.setFields(ROW,LOAD_ID, SKU_NAME,SKU, SKU_SPEC,QNTY,UOM, QNTY_EACH, VOL, G_WGT,LOTATT01,LOTATT02);
        		
        		Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("ODR_NO",record.getAttributeAsString("ODR_NO"));
				
				itemTable.fetchData(criteria);
        		
        		layout.addMember(itemTable);
                layout.setLayoutLeftMargin(5);
				return layout;     
			}
			
			
			
		};
		
		//作业单第一级列表
		shpmentGrid(table);
		table.setCanExpandRecords(true);
//		shpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		table.setShowFilterEditor(false);
		
		table.setCanEdit(false);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		listItem.setControls(new SGPage(table, true).initPageBtn());
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		
	    
		
		//下边布局
		TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("45%");	
		
		createbottoInfo();
		
	 if(isPrivilege(TrsPrivRef.TRACK_P1)) {
		Tab tab1 = new Tab("回访记录");//在途跟踪
		
		VLayout followlay = new VLayout();
		followlay.addMember(groupTable1);
		tab1.setPane(followlay);
		createTransFollowBtnWidget(followtoolStrip);
		followlay.addMember(followtoolStrip);
		bottomTabSet.addTab(tab1);
		
	 }
	 
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		layOut.setHeight("100%");
		layOut.addMember(stack);
		layOut.addMember(bottomTabSet);
		bottomTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				tabSelect = event.getTabNum();
//				selectHander(tabSelect);
				
			}
		});
		
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(layOut);
		
		
		
	    
		
		return main;
	}

   
	private void createbottoInfo() { //在途跟踪  国际化
		groupTable1 = new SGTable(ds);
		groupTable1.setShowFilterEditor(false);
		groupTable1.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable1.setShowRowNumbers(true);
		
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC", "实际回访时间", 100);
		CURRENT_LOC.setTitle(ColorUtil.getRedTitle(Util.TI18N.CURRENT_LOC()));
		ListGridField INFORMATION = new ListGridField("INFORMATION","回访人",100);
		final ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME", "回访情况", 240);	
		
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT", "回访分类", 100);
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setDefaultValue("5FB42E7D159346C395A2A34E0FE698C1");
		
		ListGridField ABNOMAL_NOTE = new ListGridField("ABNOMAL_NOTE", "司机服务态度", 90);
		
		final ListGridField PRE_SOLVE_TIME = new ListGridField("PRE_SOLVE_TIME","客户满意度", 120);
		
       
		
		groupTable1.setFields(CURRENT_LOC,INFORMATION, PRE_UNLOAD_TIME, ABNOMAL_STAT, ABNOMAL_NOTE,
				PRE_SOLVE_TIME);
		
		groupTable1.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
			}
		});
		Util.initDateTime(groupTable1,PRE_UNLOAD_TIME);
		Util.initDateTime(groupTable1,PRE_SOLVE_TIME);

	}

	
	/**
	 * 在途跟踪按钮布局
	 */
    public  void createTransFollowBtnWidget(ToolStrip followtoolStrip){
    	followtoolStrip.setWidth("100%");
    	followtoolStrip.setHeight("20");
    	followtoolStrip.setPadding(2);
    	followtoolStrip.setSeparatorSize(12);
    	followtoolStrip.addSeparator();
    	followtoolStrip.setMembersMargin(4);
    	followtoolStrip.setAlign(Alignment.LEFT);
    	
		//新增按钮
	    IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.TRACK_P1_01);
//        newButton.addClickHandler(new NewTrasFollowAction(groupTable1,this));
        
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.TRACK_P1_02);
//        saveButton.addClickHandler(new SaveTrasFollowAction(shpmTable,groupTable1,this,check_map));
        
        
        //取消按钮
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.TRACK_P1_03);
        canButton.addClickHandler(new CancelAction(groupTable1,this));
        
        followtoolStrip.setMembersMargin(3);
        followtoolStrip.setMembers(newButton, saveButton, canButton);
        add_map.put(TrsPrivRef.TRACK_P1_01, newButton);
        save_map.put(TrsPrivRef.TRACK_P1_02, saveButton);
        save_map.put(TrsPrivRef.TRACK_P1_03, canButton);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(save_map, false);
        
    }
    
   
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm),
							sectionStack.getSection(0)).getViewPanel();
//					searchWin.setWidth(616);
					searchWin.setHeight(354);
				}else{
					searchWin.show();
				}
				
			}
		});
	        //导出按钮
	        IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.TRACK_P0_01);
	        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
	    
	        toolStrip.setMembersMargin(2);
	        toolStrip.setMembers(searchButton,expButton);
	  
		
	}
	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(final DynamicForm form) {
		form.setDataSource(ds);
		form.setAutoFetchData(false);
//		form.setWidth100();
		form.setCellPadding(2);
		final String pre_time = getPreDay().toString();
//		final String time = getCurTime().toString();              //wangjun 
		
		
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
		CUSTOM_ODR_NO.setWidth(128);
		
		SGText SHPM_NO =new SGText("SHPM_NO",Util.TI18N.SHPM_NO() );
		SHPM_NO.setWidth(128);//作业单号
		
		SGText LOAD_NO =new SGText("LOAD_NO",Util.TI18N.LOAD_NO() );
		LOAD_NO.setWidth(128);//调度单号
		
		//调度单状态 从
		SGCombo STATUS_FROM = new SGCombo("PLAN_STAT_FROM","作业单状态从");
		SGCombo STATUS_TO = new SGCombo("PLAN_STATUS_TO",Util.TI18N.STATUS_TO());
		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT,StaticRef.SHPM_LOAD);
		Util.initStatus(STATUS_TO,StaticRef.SHPMNO_STAT,StaticRef.SHPM_LOAD);
		STATUS_FROM.setWidth(128);
		STATUS_TO.setWidth(128);
		
		//发运时间 从END_LOAD_TIME
		SGText END_LOAD_TIME_FROM = new SGText("END_LOAD_TIME_FROM", Util.TI18N.END_LOAD_TIME()+"从");
		SGText END_LOAD_TIME_TO = new SGText("END_LOAD_TIME_TO", "到");
		Util.initDateTime(searchForm,END_LOAD_TIME_FROM);
		Util.initDateTime(searchForm,END_LOAD_TIME_TO);
		END_LOAD_TIME_FROM.setDefaultValue(getCurInitDay());
		END_LOAD_TIME_TO.setDefaultValue(getCurTime());
		END_LOAD_TIME_FROM.setWidth(128);
		END_LOAD_TIME_TO.setWidth(128);
		//预达时间 从PRE_UNLOAD_TIME
		final SGText PRE_UNLOAD_TIME_FROM = new SGText("PRE_UNLOAD_TIME_FROM", Util.TI18N.PRE_UNLOAD_TIME());
		final SGText PRE_UNLOAD_TIME_TO = new SGText("PRE_UNLOAD_TIME_TO","到");
		//PRE_UNLOAD_TIME_FROM.setDefaultValue(pre_time);//wangjun 2010-4-24
		//PRE_UNLOAD_TIME_TO.setDefaultValue(time);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,PRE_UNLOAD_TIME_TO);
		PRE_UNLOAD_TIME_FROM.setWidth(128);
		PRE_UNLOAD_TIME_TO.setWidth(128);
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initSupplier(SUPLR_NAME, "");//供应商
		SUPLR_NAME.setWidth(128);
//		Util.initCodesComboValue(SUPLR_NAME,"BAS_SUPPLIER", "ID","SUPLR_CNAME");
		
		SGText PLATE_NO =new SGText("PLATE_NO",Util.TI18N.PLATE_NO() );
		LOAD_NO.setWidth(128);	//车牌号
		PLATE_NO.setWidth(128);
		
		//起点区域
		final TextItem AREA_ID = new TextItem("AREA_ID");
		AREA_ID.setVisible(false);
		ComboBoxItem START_AREA_ID = new ComboBoxItem("START_AREA_ID",Util.TI18N.START_ARAE());
		Util.initArea(START_AREA_ID, AREA_ID);
		START_AREA_ID.setWidth(128);
		START_AREA_ID.setColSpan(2);
		START_AREA_ID.setTitleOrientation(TitleOrientation.TOP);
		
		//终点区域
		ComboBoxItem END_AREA_ID = new ComboBoxItem("END_AREA_ID",Util.TI18N.END_AREA());
		Util.initArea(END_AREA_ID, AREA_ID);
		END_AREA_ID.setWidth(128);
		END_AREA_ID.setColSpan(2);
		END_AREA_ID.setTitleOrientation(TitleOrientation.TOP);
		
		
		SGLText LOAD_NAME = new SGLText("LOAD_NAME", Util.TI18N.LOAD_NAME(),true);//发货方
		LOAD_NAME.setColSpan(4);
		LOAD_NAME.setWidth(264);
		
		SGLText UNLOAD_NAME = new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		UNLOAD_NAME.setColSpan(4);
		UNLOAD_NAME.setWidth(264);
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());//执行机构
		EXEC_ORG_ID_NAME.setColSpan(2);
		EXEC_ORG_ID_NAME.setWidth(128);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(1);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构
		
		final SGCheck shoud_com_flag = new SGCheck("shoud_com_flag","应到未到");
		shoud_com_flag.setColSpan(1);
		shoud_com_flag.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if("true".equals(event.getValue().toString())){
					PRE_UNLOAD_TIME_FROM.setValue("");
					PRE_UNLOAD_TIME_TO.setValue("");
				} else {
					PRE_UNLOAD_TIME_FROM.setValue(pre_time);
					PRE_UNLOAD_TIME_TO.setValue(getCurTime().toString());
				}
				
			}
		});
	
		SGText CUSTOMER=new SGText("CUSTOMER_NAME",Util.TI18N.CUSTOMER());//客户   二级窗口 ？？
		CUSTOMER.setWidth(128);
		
		SGCombo ABNOMAL_STAT = new SGCombo("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT());//运输异常
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setWidth(128);
		
		SGText UNLOAD_TIME_FROM = new SGText("UNLOAD_TIME_FROM", "到货时间  从");//
		SGText UNLOAD_TIME_TO = new SGText("UNLOAD_TIME_TO", "到");//
		Util.initDateTime(searchForm,UNLOAD_TIME_FROM);
		Util.initDateTime(searchForm,UNLOAD_TIME_TO);
		UNLOAD_TIME_FROM.setWidth(128);
		UNLOAD_TIME_TO.setWidth(128);
		
//		//卸货机构  UNLOAD_ORG_ID
//		TextItem UNLOAD_ORG_ID = new TextItem("UNLOAD_ORG_ID",Util.TI18N.UNLOAD_ORG_ID());
//		UNLOAD_ORG_ID.setVisible(false);
//		SGText UNLOAD_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.UNLOAD_ORG_ID());
//		Util.initOrg(UNLOAD_ORG_ID_NAME, UNLOAD_ORG_ID, false, "50%", "50%");
//		UNLOAD_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
//		UNLOAD_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());//执行机构
//		UNLOAD_ORG_ID_NAME.setWidth(128);
		
		form.setItems(CUSTOM_ODR_NO,SHPM_NO,LOAD_NO,EXEC_ORG_ID_NAME,STATUS_FROM,STATUS_TO,
				AREA_ID,END_LOAD_TIME_FROM,END_LOAD_TIME_TO,PRE_UNLOAD_TIME_FROM,PRE_UNLOAD_TIME_TO,
				UNLOAD_TIME_FROM,UNLOAD_TIME_TO,START_AREA_ID,END_AREA_ID,
				SUPLR_NAME,PLATE_NO,LOAD_NAME,UNLOAD_NAME,
				CUSTOMER,ABNOMAL_STAT,EXEC_ORG_ID,shoud_com_flag,C_ORG_FLAG,C_RDC_FLAG);
		
		return form;
		
	}

	public void createForm(DynamicForm form) {
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	private void shpmentGrid(final SGTable shpmTable){
        
        
		table.setShowRowNumbers(true);

		/**
		 * 主列表显示的字段
		 * @param ORD_NO  托运单编号
		 * 托运单列表
		 */
		
//		table.setSelectionType(SelectionStyle.SIMPLE);  
//		table.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
		table.setCanEdit(false);
//		table.setCanReorderRecords(true);
		ListGridField ORD_NO = new ListGridField("ODR_NO", Util.TI18N.ORDER_CODE(), 100);//订单编号
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 100);//客户单号
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(),110);//订单时间
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME(),90);//收货区域
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(),120);//收货方
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(),70);//订单状态 
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME", Util.TI18N.ORDER_STATE2(),70);//订单状态 
		ListGridField SLF_PICKUP_FLAG = new ListGridField("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG(),60);//客户自提
		SLF_PICKUP_FLAG.setType(ListGridFieldType.BOOLEAN);
		 
//		ListGridField ASSIGN_STAT_NAME = new ListGridField("ASSIGN_STAT_NAME", Util.TI18N.ASSIGN_STAT(),70);//派发状态
		ListGridField PLAN_STAT_NAME = new ListGridField("PLAN_STAT_NAME", Util.TI18N.PLAN_STAT(),70);//调度状态 
		ListGridField LOAD_STAT_NAME = new ListGridField("LOAD_STAT_NAME", Util.TI18N.LOAD_STAT(),70);//发运状态
		ListGridField UNLOAD_STAT_NAME = new ListGridField("UNLOAD_STAT_NAME", Util.TI18N.UNLOAD_STAT(),70);//到货状态 
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID(),110);//执行机构
		ListGridField TO_LOAD_TIME = new ListGridField("PRE_LOAD_TIME", Util.TI18N.FROM_LOAD_TIME(),90);//计划发货时间  
//		ListGridField PACK_ID_NAME = new ListGridField("TRANS_UOM", Util.TI18N.ORD_PACK_ID(),70);//运输单位  
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY", "总数量",90);//订单数量  
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH", Util.TI18N.QNTY_EACH(),90);//订单数量  
		
		//ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(),90);//实发数量
		//ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(),90);//未发数量   
		ListGridField ADDWHO = new ListGridField("ADDWHO", Util.TI18N.ORD_PERSON(),70);//接单员  
//		ListGridField ADDTIME = new ListGridField("ADDTIME", Util.TI18N.ADDTIME(),90);//创建时间 
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME", Util.TI18N.DFT_SUPLR_ID(),90);//供应商
		ListGridField LOAD_AREA_NAME = new ListGridField("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME(),120);//发货区域
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", Util.TI18N.LOAD_NAME(),120);//发货方 
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS", Util.TI18N.UNLOAD_ADDRESS(),120);//收货方地址 
		ListGridField UNLOAD_CONTACT = new ListGridField("UNLOAD_CONTACT", Util.TI18N.CONT_NAME(),90);//联系人	
		ListGridField UNLOAD_TEL = new ListGridField("UNLOAD_TEL", Util.TI18N.CONT_TEL(),100);//联系电话 
		table.setFields(ORD_NO, CUSTOM_ODR_NO,UNLOAD_AREA_NAME,UNLOAD_NAME,ADDWHO,LOAD_NAME,NOTES,SLF_PICKUP_FLAG,TOT_QNTY,TOT_QNTY_EACH,
				STATUS_NAME,PLAN_STAT_NAME,LOAD_STAT_NAME,UNLOAD_STAT_NAME,EXEC_ORG_ID_NAME,
				TO_LOAD_TIME,SUPLR_NAME,ODR_TIME,
				LOAD_AREA_NAME,UNLOAD_ADDRESS,UNLOAD_CONTACT,UNLOAD_TEL);
		
		Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
		
		
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
	}
	
	
	
	public CustomerVisitView getThis(){
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
	var month=now.getMonth()+1;
	var day="01";	
	var res = year+"/"+month+"/"+ day + " 00:00";
	return res;

}-*/;

public static native String getCurTime() /*-{

	var now = new Date();
	var year=now.getFullYear();
	var month=now.getMonth()+1;
	var day=now.getDate();
	var hour=now.getHours();
	var minute=now.getMinutes();
	if (minute < 10) {
	    minute = "0" + minute;
	}
	var res = year+"/"+month+"/"+ day + " " + hour + ":" + minute;
	return res;
}-*/;

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		CustomerVisitView view = new CustomerVisitView();
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
