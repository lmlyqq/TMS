package com.rd.client.view.tms;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.ShmpTrackTraceDS;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.rd.client.ds.tms.ShpmTrackDS;
import com.rd.client.ds.tms.Transact_shpm_logDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
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
/**
 * 客户服务-->订单动态监控-->双击弹出对应【作业单】的二级窗口
 * @author wang2
 *
 */
public class TransTrackWin extends Window{

	//private String title;
	public Window window = null;
	public DynamicForm mainItem;
	private DataSource traceDS;
	public SectionStackSection sectionStackSection;
	public SectionStack section;
	
	
	public SGTable table;
	private SGTable groupTable1;
	private SGTable groupTable2;
	private DataSource shpmDS;            //已调作业单数据源
	private DataSource shpmlstDS;         //已调作业但明细数据源
	private String shpm_no;   //作业单号 
	//private String shpm_no2;
	private DataSource transact_logDS;
	public SGTable shpmTable;       //已调作业单表
	public SGTable shpmlstTable;    //已调作业单明细表
	public String order_no;
	private String where;
	
	public TransTrackWin(String order_no, String strWhere) {
		this.order_no=order_no;
		this.where = strWhere;
	}
	public Window getViewPanel() {
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		
		
		shpmDS = ShpmTrackDS.getInstance("V_ODR_SHPM");
		shpmlstDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM", "TRANS_SHIPMENT_ITEM");
		traceDS = ShmpTrackTraceDS.getInstance("V_TRANS_TRACK_TRACE_SHMP");//【在途信息】
		transact_logDS=Transact_shpm_logDS.getInstance("V_TRANSACT_LOG_SHIP");// 【跟踪历史信息】
		//
		HStack hStack = new HStack();
		hStack.setWidth100();
		hStack.setHeight100();
		
//		section = new SectionStack();
//		section.setWidth("100%");
//		section.setHeight("100%");
	
		
		
		shpmTable = new SGTable(shpmDS, "100%", "100%") {
			public DataSource getRelatedDataSource(ListGridRecord record) {
				shpmlstDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM","TRANS_SHIPMENT_ITEM");
     			shpm_no = record.getAttributeAsString("SHPM_NO");
				return shpmlstDS;

			}

			// 第2层表
			protected Canvas getExpansionComponent(final ListGridRecord record) {

				VLayout layout = new VLayout();

				shpmlstTable = new SGTable(getRelatedDataSource(record),"100%", "50", false, true, false);
				shpmlstTable.setCanEdit(false);
				shpmlstTable.setAlign(Alignment.RIGHT);
				shpmlstTable.setShowRowNumbers(false);
				shpmlstTable.setAutoFitData(Autofit.VERTICAL);
				//shpmlstTable.setShowFilterEditor(true);
				//shpmlstTable.setShowSelectedStyle(false);
				//shpmlstTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);

				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("SHPM_NO", shpm_no);

				/**
				 * 行号 货品代码  货品名称 规格型号 单位 订单数量 发货数量 收货数量  毛重 体积   批号 专供标识 
				 */
				ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.ORD_ROW(),30);//行号
				SHPM_ROW.setCanEdit(false);
				ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU(),70);//货品代码
				SKU_ID.setCanEdit(false);
				ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),90);//货品名称
				SKU_NAME.setCanEdit(false);
				ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),52);//规格型号
				SKU_SPEC.setCanEdit(false);
				ListGridField UOM = new ListGridField("UOM",Util.TI18N.UNIT(),40);//单位
				UOM.setCanEdit(false);
				ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.ODR_QNTY(),60);// 订单数量
				QNTY.setCanEdit(false);
				QNTY.setAlign(Alignment.RIGHT);
				ListGridField EA = new ListGridField("QNTY_EACH", Util.TI18N.R_EA(),60);//
				//wangjun 2011/3/8
				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.FOLLOW_LD_QNTY(),60);//发货数量
				LD_QNTY.setCanEdit(false);
				LD_QNTY.setAlign(Alignment.RIGHT);
				//LD_QNTY.addEditorExitHandler(new ChangeQntyAction(shpmlstTable, getView()));
				final ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);//收货数量
				UNLD_QNTY.setCanEdit(true);
				UNLD_QNTY.setAlign(Alignment.RIGHT);
				final ListGridField TRANS_QNTY = new ListGridField("DAMA_QNTY",Util.TI18N.TRANS_QNTY(),70);
				TRANS_QNTY.setCanEdit(true);
				TRANS_QNTY.setAlign(Alignment.RIGHT);//货损数量
				Util.initFloatListField(TRANS_QNTY, StaticRef.QNTY_FLOAT);
				
				//毛重[吨]，体积[方]		
				ListGridField G_WGT = new ListGridField("UNLD_GWGT",Util.TI18N.G_WGT(),60);//		签收重量				
        		G_WGT.setAlign(Alignment.RIGHT);
        		G_WGT.setCanEdit(false);
        		ListGridField VOL = new ListGridField("UNLD_VOL",Util.TI18N.VOL(),60);//  签收体积						
        		VOL.setAlign(Alignment.RIGHT);	
        		VOL.setCanEdit(false);
        		//关于 作业单明细  小数为数的处理
        		//Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
        		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
        		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
        		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
        		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
        		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
        		Util.initFloatListField(EA, StaticRef.QNTY_FLOAT);
        		
				final ListGridField LOTATT01 = new ListGridField("LOTATT01",Util.TI18N.LOTATT01(),60);// 批号 
				final ListGridField LOTATT02 = new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);//专供标识
				
				shpmlstTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,QNTY,EA,LD_QNTY,UNLD_QNTY,TRANS_QNTY,G_WGT,VOL,LOTATT01,LOTATT02);
				layout.addMember(shpmlstTable);
				shpmlstTable.fetchData(findValues);
				layout.setLayoutLeftMargin(5);
				return layout;
						
						
			}

		};
		
//		TabSet ShipTabSet = new TabSet();
//		ShipTabSet.setWidth100();
//		ShipTabSet.setHeight100();	
//		
//		createListField();
//		
//		Tab Shiptab = new Tab(Util.TI18N.TRANS_SHMP_LIST());//作业单列表
//		Shiptab.setPane(shpmTable);
//		ShipTabSet.addTab(Shiptab);
		
		createListField(shpmTable);
		
		shpmTable.setCanExpandRecords(true);
		//shpmTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		shpmTable.setShowFilterEditor(false);
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("ODR_NO",order_no);
		crit.addCriteria("CONDITION", where);
		shpmTable.fetchData(crit,new DSCallback() {
		
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				// TODO Auto-generated method stub
				shpmTable.setSelectOnEdit(true);
				shpmTable.selectRecord(shpmTable.getRecord(0));
			}
		});

		shpmTable.setCanEdit(false);
		// loadTable.setExpansionCanEdit(false); 
		shpmTable.setShowSelectedStyle(true);
		shpmTable.setTitle(Util.TI18N.TRANS_SHMP_LIST());
		
//		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.TRANS_SHMP_LIST());//作业单列表
//		listItem.setItems(shpmTable);
//		listItem.setExpanded(true);
//		section.addSection(listItem);
		
		hStack.addMember(shpmTable);
		layout.addMember(hStack);

		//下边布局
		TabSet bottomTabSet = new TabSet();
		bottomTabSet.setWidth100();
		bottomTabSet.setHeight("160%");	
		
		createbottoInfo();
		Tab tab1 = new Tab(Util.TI18N.TRANSACT_LOG());//跟踪历史信息
		tab1.setPane(groupTable1);
		bottomTabSet.addTab(tab1);

		createbottoInfo2();
		Tab tab2 = new Tab(Util.TI18N.TRANS_TRACK_TRACE());//在途信息
		tab2.setPane(groupTable2);
		bottomTabSet.addTab(tab2);
		layout.addMember(bottomTabSet);
		

		
		window = new Window();
		window.setTitle(Util.TI18N.TRANS_SHMP_LIST());
		window.setLeft("13%");
		window.setTop("37%");
		window.setWidth(990);
		window.setHeight(360);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(layout);
		
		window.setShowCloseButton(true);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}

		});
		return window;
		
	}



	private void createbottoInfo() {
		/**
		 * V_TRANSACT_LOG 【跟踪历史信息】：节点信息，发生时间，操作人，操作时间
		 */
		
		groupTable1 = new SGTable(transact_logDS);
		groupTable1.setShowFilterEditor(false);
		groupTable1.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable1.setShowRowNumbers(true);
		//groupTable1.setAutoFetchData(true);
		
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.TRANSACT_NOTES(), 70);//节点信息
		ListGridField OP_TIME = new ListGridField("OP_TIME", Util.TI18N.OCCUR_TIME(), 120);//发生时间
		ListGridField ADDWHO = new ListGridField("USER_NAME", Util.TI18N.OPERATE_PERSON(), 70);
		ListGridField ADDTIME = new ListGridField("ADDTIME", Util.TI18N.OPERATE_TIME(), 120);
		
		groupTable1.setFields(NOTES,OP_TIME, ADDWHO, ADDTIME);
	}

	private void createbottoInfo2() {
		/**
		 *当前位置 预达时间 运输异常 异常描述 预计解决时间  实际解决时间 解决措施 跟踪人 执行机构 车牌号 司机 司机电话
		 */
		
		groupTable2 = new SGTable(traceDS);//V_TRANS_TRACK_TRACE
		groupTable2.setShowFilterEditor(false);
		groupTable2.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable2.setShowRowNumbers(true);
		
	    ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC", Util.TI18N.CURRENT_LOC(), 60);
		ListGridField INFORMATION = new ListGridField("INFORMATION",Util.TI18N.INFORMATION(),100);
		
		ListGridField TRACER = new ListGridField("TRACER", Util.TI18N.TRACER(), 65);
		
		ListGridField TRACE_TIME = new ListGridField("TRACE_TIME", Util.TI18N.TRACE_TIME(), 120);//
		TRACE_TIME.setCanEdit(false);
		
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME", Util.TI18N.PRE_UNLOAD_TIME(), 120);	
		Util.initListGridDateTime(PRE_UNLOAD_TIME);
		
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT", Util.TI18N.ABNOMAL_STAT(), 60);
		Util.initCodesComboValue(ABNOMAL_STAT, "ABNORMAL_STAT");
		ABNOMAL_STAT.setDefaultValue("5FB42E7D159346C395A2A34E0FE698C1");
		
		ListGridField ABNOMAL_NOTE = new ListGridField("ABNOMAL_NOTE", Util.TI18N.ABNOMAL_NOTE(), 90);
		
		final ListGridField PRE_SOLVE_TIME = new ListGridField("PRE_SOLVE_TIME",Util.TI18N.PRE_SOLVE_TIME(), 120);
        Util.initListGridDateTime(PRE_SOLVE_TIME);
		
        final ListGridField SOLVE_TIME = new ListGridField("SOLVE_TIME", Util.TI18N.SOLVE_TIME(), 120);
		Util.initListGridDateTime(SOLVE_TIME);
		
		ListGridField SOLUTION = new ListGridField("SOLUTION", Util.TI18N.SOLUTION(), 65);
		
		

		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),80);
		EXEC_ORG_ID_NAME.setCanEdit(false);
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),50);
		
		ListGridField DRIVER1 = new ListGridField("DRIVER",Util.TI18N.DRIVER1(),50);//						
								
		ListGridField MOBILE1 = new ListGridField("MOBILE",Util.TI18N.MOBILE(),90);//	
		
		groupTable2.setFields(CURRENT_LOC,INFORMATION,TRACER, TRACE_TIME,PRE_UNLOAD_TIME, ABNOMAL_STAT, ABNOMAL_NOTE, 
				PRE_SOLVE_TIME,SOLVE_TIME, SOLUTION, EXEC_ORG_ID_NAME,
				PLATE_NO,DRIVER1,MOBILE1);

	}


	private void createListField(final SGTable shpmTable) {
		//序号 作业单编号          供应商 车牌号 司机 司机电话 当前位置          运输单位 订单数量 发货数量 到货数量 状态 收货方 收货地址 执行机构
		/**    
		 *  V_ODR_SHPM
		 */
		
//		shpmTable = new SGTable(shpmDS);//V_ODR_SHPM
//		shpmTable.setShowFilterEditor(false);
//		shpmTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
//		shpmTable.setShowRowNumbers(true);
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),110);// 作业单编号
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),65);// 供应商 
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),50);// 车牌号
		ListGridField DRIVER1 = new ListGridField("DRIVER1",Util.TI18N.DRIVER1(),45);// 司机
		ListGridField MOBILE1 = new ListGridField("MOBILE1",Util.TI18N.MOBILE(),88);//  司机电话 
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC",Util.TI18N.CURRENT_LOC(),60);// 当前位置
//		ListGridField TRANS_UOM = new ListGridField("TRANS_UOM", Util.TI18N.ORD_PACK_ID(),55);//运输单位  
		ListGridField TOT_QNTY = new ListGridField("QNTY", Util.TI18N.ORD_TOT_QNTY(),55);//订单数量  
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(false);
		ListGridField EA = new ListGridField("TOT_QNTY_EACH", Util.TI18N.R_EA(),60);//
		//wangjun 2011/3/8
		ListGridField LD_QNTY = new ListGridField("LD_QNTY", Util.TI18N.FOLLOW_LD_QNTY(),55);//发货数量   LD_QNTY 
		LD_QNTY.setAlign(Alignment.RIGHT);
		LD_QNTY.setCanEdit(false);
		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY", Util.TI18N.UNLD_QNTY(),55);// 收货数量  UNLD_QNTY
		UNLD_QNTY.setAlign(Alignment.RIGHT);
		UNLD_QNTY.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STAT_NAME", Util.TI18N.STATUS(), 40);// 状态    订单状态
		STATUS.setCanEdit(false);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 70);// 收货方
		UNLOAD_NAME.setCanEdit(false);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS", Util.TI18N.UNLOAD_ADDRESS(),125);//收货方地址 
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXCE_ORG_NAME", Util.TI18N.EXEC_ORG_ID(),65);//执行机构
		
		//关于 作业单明细  小数为数的处理
		Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		//Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(EA, StaticRef.QNTY_FLOAT);
		//Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		//Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
		
		shpmTable.setFields(SHPM_NO,SUPLR_NAME,PLATE_NO,MOBILE1,CURRENT_LOC,DRIVER1,TOT_QNTY,EA, LD_QNTY,UNLD_QNTY,
				UNLOAD_NAME,UNLOAD_ADDRESS,EXEC_ORG_ID_NAME);
		
		shpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				shpm_no = event.getRecord().getAttributeAsString("SHPM_NO");
				shpmTable.OP_FLAG = "M";

				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG", "M");
				criteria.addCriteria("DOC_NO", shpm_no);
//				groupTable1.fetchData(criteria);
				groupTable1.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						
						Criteria criteria = new Criteria();
						criteria.addCriteria("OP_FLAG", "M");
						criteria.addCriteria("SHPM_NO", shpm_no);
						groupTable2.fetchData(criteria);
						
					}
				});
				
				
			}
		});
//        shpmTable.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				shpm_no2 = event.getRecord().getAttributeAsString("SHPM_NO");
//				shpmTable.OP_FLAG = "M";
//
//				Criteria criteria2 = new Criteria();
//				criteria2.addCriteria("OP_FLAG", "M");
//				criteria2.addCriteria("SHPM_NO", shpm_no2);
//				groupTable2.fetchData(criteria2);
//			}
//		});
				
	}
}
