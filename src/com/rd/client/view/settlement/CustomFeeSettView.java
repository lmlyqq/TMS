package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.CustomAuditAction;
import com.rd.client.action.settlement.CustomReAuditAction;
import com.rd.client.action.settlement.DeleteReceFeeAction;
import com.rd.client.action.settlement.SaveReceFeeAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillOrderHeaderDS;
import com.rd.client.ds.settlement.BmsOrderItemDS;
import com.rd.client.ds.tms.BillRecDS;
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
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
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
 * 财务管理--结算管理--客户费用管理
 * @author fanglm 
 * @create time 2011-01-20 10:55
 *
 */
@ClassForNameAble
public class CustomFeeSettView extends SGForm implements PanelFactory {
	
	public SGTable table;
	public SGTable itemTable;//作业单列表
	public SGTable feeTable;//费用列表
	private Window searchWin;
	public SGPanel searchForm = new SGPanel();
	private SectionStack sectionStack;
	private  SectionStackSection  listItem;
	private DataSource ds;
	private DataSource detailDS;
	private IButton confirmAudit;
	private IButton cancelAudit;
	public String load_no;
	public String odr_no;
	public String shpm_no;
	private DynamicForm pageForm;
	private SGPanel feeInfo;
	private ToolStrip toolStrip;
	public SGTable headTable;
	public Record selectRecord;	
	public int pageNum = 0;	
	private ValuesManager vm;
	private DataSource billDS;	
	private DataSource bmsItemDS;	
	//按钮权限
	public HashMap<String, IButton> ins_fee_btn;
	public HashMap<String, IButton> sav_fee_btn;
	public HashMap<String, IButton> del_fee_btn;	
	private SGCombo FEE_ID;	
	private SGCombo SETT_ID;


	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		
		ds = BillOrderHeaderDS.getInstance("V_BMS_ORDER_HEADER1","BMS_ORDER_HEADER");
		detailDS = TranOrderItemDS.getInstance("V_ORDER_ITEM","TRANS_ORDER_ITEM");
		billDS = BillRecDS.getInstance("V_BILL_REC","TRANS_BILL_RECE");
		bmsItemDS=BmsOrderItemDS.getInstance("BMS_ORDER_ITEM","BMS_ORDER_ITEM");
		//主布局
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("99%");
		
		// 订单主信息列表
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();

		
		// 左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false){		
        	//明细表
			protected Canvas getExpansionComponent(final ListGridRecord record) {    				  
                VLayout layout = new VLayout(5);              
           
               // bmsItemTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
                ListGrid bmsItemTable = new ListGrid() {  
		            @Override  
		            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		            	return super.getCellCSSText(record, rowNum, colNum); 	              
		            }  
		        };       
		        bmsItemTable.setDataSource(bmsItemDS);
                bmsItemTable.setWidth("75%");
                bmsItemTable.setHeight(46);
                bmsItemTable.setCellHeight(22);
                bmsItemTable.setCanEdit(false);
                bmsItemTable.setShowRowNumbers(true);
                bmsItemTable.setCanSelectText(true);
                bmsItemTable.setCanDragSelectText(true);
                bmsItemTable.setAutoFitData(Autofit.VERTICAL);
                
                
                ListGridField ROW = new ListGridField("ODR_ROW","行号",30);
                ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","提货点",110);
                ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","卸货点",110);
        		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 80);
        		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 70);
        		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1", Util.TI18N.TEMPERATURE(), 70);
        		Util.initCodesComboValue(TEMPERATURE1,"STOR_COND");
        		final ListGridField UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 60);
        		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 70);
        		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 90);
        		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 70);
        		//ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 90);

        		bmsItemTable.setFields(ROW, SKU, SKU_NAME,LOAD_NAME,UNLOAD_NAME, UOM, VOL,G_WGT,QNTY,TEMPERATURE1);
        		
        		Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("ODR_NO",record.getAttributeAsString("ODR_NO"));				
				bmsItemTable.fetchData(criteria);      		
        		layout.addMember(bmsItemTable);
                layout.setLayoutLeftMargin(35);
                //layout.setLayoutTopMargin(10);
				return layout;     
			}
	};
		createListField();
		table.setShowFilterEditor(false);
		table.setShowFilterEditor(false);
		table.setCanExpandRecords(true);
		table.setConfirmDiscardEdits(false);
		sectionStack = new SectionStack();
		listItem = new SectionStackSection(Util.TI18N.LISTINFO());
		listItem.setItems(table);
		listItem.setExpanded(true);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		addSplitBar(stack,"25%");
		// 右边布局
		// 一
		VLayout layOut = new VLayout();
		layOut = new VLayout();
		layOut.setWidth("75%");
		layOut.setHeight("100%");
		layOut.setVisible(false);
		
		
//		TabSet taSet = new TabSet();
//		taSet.setWidth100();
//		taSet.setHeight("20%");
//		taSet.setMargin(1);
		
		itemTable = createShpmTable();
		
//		Tab tab = new Tab("明细信息");
//		tab.setPane(itemTable);
//		taSet.addTab(tab);
		
		TabSet taSet2 = new TabSet();
		taSet2.setWidth100();
		taSet2.setHeight("20%");
		taSet2.setMargin(1);
		
		Tab tab3 = new Tab("费用信息");
		tab3.setPane(createfeeTable());
		taSet2.addTab(tab3);
		
		createFeeInfo();
		feeInfo.setWidth("40%");
		//layOut.addMember(taSet);
		layOut.addMember(taSet2);
		layOut.addMember(feeInfo);
		vm.addMember(feeInfo);
		
		layOut.addMember(createFeeBtn());
		
		
		stack.addMember(layOut);
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		main.addMember(toolStrip);
		main.addMember(stack);
		
		initVerify();
		
		return main;
	}
	
	private void createListField() {
		
		table.setShowRowNumbers(true);

		/**
		 * 主列表显示的字段
		 * @param ORD_NO  托运单编号
		 * 托运单列表
		 */
		table.setCanEdit(true);
//		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		table.setSelectionType(SelectionStyle.MULTIPLE);
		table.setEditEvent(ListGridEditEvent.CLICK);
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_ORDER_HEADER客户费用结算管理");
//		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_ORDER_HEADER_FEE);
//		createListField(table, listMap);
//		
//		ListGridField TOT_FEE=new ListGridField("TOT_FEE",Util.TI18N.TOT_FEE(),100);
//		ListGridField[] fields=table.getFields();
//		ListGridField[] fields2=new ListGridField[fields.length+1];
//		System.arraycopy(fields, 0, fields2, 0, fields.length);
//		fields2[fields.length]=TOT_FEE;
//		table.setFields(fields2);
		ListGridField USE_FLAG = new ListGridField("USE_FLAG", "选择", 40);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				Record record=event.getRecord();
				if(record==null){
					return;
				}
				if(record.getAttribute("USE_FLAG")!=null&&("true").equals(record.getAttribute("USE_FLAG"))){
					record.setAttribute("USE_FLAG",false);
				}else{
					record.setAttribute("USE_FLAG",true);
				}
			}
		});
			
		ListGridField ODR_NO = new ListGridField("ODR_NO", "托运单号", 110);
		ODR_NO.setCanEdit(false);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", "客户单号", 70);
		CUSTOM_ODR_NO.setCanEdit(false);
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", "客户名称", 100);
		CUSTOMER_NAME.setCanEdit(false);
		ListGridField PARENT_CUSTOMER_NAME = new ListGridField("PARENT_CUSTOMER_NAME", "隶属客户", 100);
		PARENT_CUSTOMER_NAME.setCanEdit(false);
		ListGridField VEHICLE_TYP = new ListGridField("VEHICLE_TYP_NAME", "车辆类型", 70);
		VEHICLE_TYP.setCanEdit(false);
		ListGridField ODR_TYP = new ListGridField("ODR_TYP_NAME", "运输类型", 60);
		ODR_TYP.setCanEdit(false);
		ListGridField BIZ_TYP = new ListGridField("BIZ_TYP_NAME", "业务类型", 60);
		BIZ_TYP.setCanEdit(false);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", "发货地", 115);
		LOAD_NAME.setHidden(true);
		LOAD_NAME.setCanEdit(false);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", "收货地", 115);
		UNLOAD_NAME.setCanEdit(false);
		UNLOAD_NAME.setHidden(true);
		ListGridField BIZ_CODE = new ListGridField("BIZ_CODE", "起止地", 230);
		BIZ_CODE.setCanEdit(false);
		BIZ_CODE.setShowHover(true);
		ListGridField AUDIT_STAT = new ListGridField("AUDIT_STAT_NAME", "审核状态", 65);
		AUDIT_STAT.setCanEdit(false);
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY", "数量", 70);
		//TOT_QNTY.setShowHover(true);
		TOT_QNTY.setCanEdit(false);
		ListGridField TRANS_UOM = new ListGridField("TRANS_UOM", "单位", 50);
		TRANS_UOM.setCanEdit(false);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL", "体积", 70);
		TOT_VOL.setCanEdit(false);
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W", "毛重", 70);
		TOT_GROSS_W.setCanEdit(false);
		ListGridField BILL_PRICE = new ListGridField("BILL_PRICE", "总金额", 70);
		BILL_PRICE.setCanEdit(false);
		table.setFields(USE_FLAG,ODR_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,PARENT_CUSTOMER_NAME,VEHICLE_TYP,ODR_TYP,BIZ_TYP,BIZ_CODE,LOAD_NAME,UNLOAD_NAME,AUDIT_STAT,TOT_QNTY,TRANS_UOM,TOT_VOL,TOT_GROSS_W,BILL_PRICE);
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				vm.clearValues();
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG","M");
				criteria.addCriteria("ODR_NO",event.getRecord().getAttributeAsString("ODR_NO"));
				criteria.addCriteria("DOC_NO",event.getRecord().getAttributeAsString("ODR_NO"));
				
				itemTable.fetchData(criteria);
				
				feeTable.fetchData(criteria);
				
				Record rec = event.getRecord();
	        	if(rec.getAttribute("AUDIT_STAT_NAME").equals("未审核")) {
	        		disAudit(false,true);
	        		initLoadFeeBtn(1);
	        	}
	        	else {
	        		initLoadFeeBtn(5);
	        		disAudit(true,false);
	        	}
	        	FEE_ID.setDisabled(true);
			}
		});
		table.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				if(isMax) {
					expend();
				}
			}
			
		});
	}
	

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN);

		toolStrip.addMember(searchButton);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
					searchWin.setHeight(380);
				} else {
						searchWin.show();
				}
			}

		});
		
		//confirmAcc = createUDFBtn("确认对账",StaticRef.ICON_SAVE,SettPrivRef.CUSTOMERFEE_P0_05);
		//confirmAcc.addClickHandler(new AccountAction(table, true,this,"RECE"));
		
		//cancelAcc = createUDFBtn("取消对账",StaticRef.ICON_CANCEL,SettPrivRef.CUSTOMERFEE_P0_06);
		//cancelAcc.addClickHandler(new AccountAction(table, false,this,"RECE"));
		
		IButton charging = createUDFBtn("计费",StaticRef.ICON_SAVE,SettPrivRef.CUSTOMERFEE_P0_10);
		charging.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord()==null){
					SC.say("请选择一条记录!");
					return;
				}
				HashMap<String, Object> listMap = new HashMap<String, Object>();
				listMap.put("1","TMS");
				listMap.put("2","REC");
				listMap.put("3",table.getSelectedRecord().getAttribute("ODR_NO"));
				listMap.put("4","system");
				
				String json = Util.mapToJson(listMap);
				Util.async.execProcedure(json, "BMS_REC_CALCUALTE(?,?,?,?,?)", new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(String result) {
						if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
							MSGUtil.showOperSuccess();
							Util.db_async.getRecord("BILL_PRICE", "BMS_ORDER_HEADER", " where ODR_NO='"+table.getSelectedRecord().getAttribute("ODR_NO")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

								@Override
								public void onFailure(Throwable caught) {
									
								}

								@Override
								public void onSuccess(ArrayList<HashMap<String, String>> result) {
									if(result!=null&&result.size()>0){
										table.getSelectedRecord().setAttribute("BILL_PRICE", result.get(0).get("BILL_PRICE"));
										table.redraw();
									}
								}
								
							});
						}else{
							MSGUtil.sayError(result.substring(2));
						}
					}
					
				});
			}
		});
		
		confirmAudit = createUDFBtn("审核",StaticRef.ICON_SAVE,SettPrivRef.CUSTOMERFEE_P0_05);
		confirmAudit.addClickHandler(new CustomAuditAction(table, true, this));
		
		cancelAudit = createUDFBtn("取消审核",StaticRef.ICON_CANCEL,SettPrivRef.CUSTOMERFEE_P0_06);
		cancelAudit.addClickHandler(new CustomReAuditAction(table, false, this));
		
		//confirmAudit = createUDFBtn("审核",StaticRef.ICON_SAVE,SettPrivRef.CUSTOMERFEE_P0_07);
		//confirmAudit.addClickHandler(new AuditAction(table, true, this,"RECE"));
		
		//cancelAudit = createUDFBtn("取消审核",StaticRef.ICON_CANCEL,SettPrivRef.CUSTOMERFEE_P0_08);
		//cancelAudit.addClickHandler(new AuditAction(table, false, this,"RECE"));

		IButton export  = createBtn(StaticRef.EXPORT_BTN, SettPrivRef.CUSTOMERFEE_P0_09);
		export.addClickHandler(new ExportAction(table));
		
		
//		IButton settleButton  = createBtn(StaticRef.CREATE_BTN, SettPrivRef.CUSTOMERFEE_P0_12);
//		settleButton.setTitle("生成结算单");
		//		saveDel(true, true);
//		disAccount(true,true);
		disAudit(false, true);
		
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,charging,confirmAudit, cancelAudit,export);
	}
	public DynamicForm createSearchForm(SGPanel form){
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "45%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());
		
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		
		//SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//客户单号
		
		//SGCombo RECE_STAT =new SGCombo("RECE_STAT", "收款状态");//
		//Util.initComboValue(RECE_STAT, "BAS_CODES", "CODE", "NAME_C", " where prop_code = 'RECE_STAT'", "");
		
		SGCombo ACCOUNT_STAT =new SGCombo("ACCOUNT_STAT", Util.TI18N.ACCOUNT_STAT());//
		ACCOUNT_STAT.setVisible(false);
		Util.initCodesComboValue(ACCOUNT_STAT, "ACCOUNT_STAT");
		
		SGCombo AUDIT_STAT =new SGCombo("AUDIT_STAT", Util.TI18N.AUDIT_STAT());//
		Util.initComboValue(AUDIT_STAT,"BAS_CODES","CODE", "NAME_C"," PROP_CODE = 'AUDIT_STAT'"," order by show_seq asc","10");
		
		//SGText SERIAL_NUM_FROM = new SGText("SERIAL_NUM_FROM", "回单序列号 从");
		//SGText SERIAL_NUM_TO = new SGText("SERIAL_NUM_TO", "到");
		
		SGDate ODR_TIME_FROM = new SGDate("ODR_TIME_FROM", Util.TI18N.ODR_TIME());//订单时间 从  到  
		SGDate ODR_TIME_TO = new SGDate("ODR_TIME_TO", "到");
		
		//SGDate ACCOUNT_TIME_FROM = new SGDate("ACCOUNT_TIME_FROM", Util.TI18N.ACCOUNT_TIME()+"从");//
		//SGDate ACCOUNT_TIME_TO = new SGDate("ACCOUNT_TIME_TO", "到");
		
		SGDate AUDIT_TIME_FROM = new SGDate("AUDIT_TIME_FROM", Util.TI18N.AUDIT_TIME()+"从");//
		SGDate AUDIT_TIME_TO = new SGDate("AUDIT_TIME_TO", "到");
		
		SGLText UNLOAD_NAME = new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		
		TextItem LOAD_AREA_ID2=new TextItem("LOAD_AREA_ID2");
		LOAD_AREA_ID2.setVisible(false);
		ComboBoxItem LOAD_AREA_NAME2=new ComboBoxItem("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME2.setWidth(FormUtil.Width);
		LOAD_AREA_NAME2.setColSpan(2);
		LOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(LOAD_AREA_NAME2, LOAD_AREA_ID2);
		
		TextItem UNLOAD_AREA_ID2=new TextItem("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME2 = new ComboBoxItem("UNLOAD_AREA_NAME2", Util.TI18N.UNLOAD_AREA_NAME());//收货区域
		UNLOAD_AREA_NAME2.setWidth(FormUtil.Width);
		UNLOAD_AREA_NAME2.setColSpan(2);
		UNLOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME2, UNLOAD_AREA_ID2);
//		UNLOAD_AREA_NAME.setWidth(127);
		
		SGText DATE_FROM=new SGText("DATE_FROM",Util.TI18N.ORD_ADDTIME_FROM());
		Util.initDateTime(form, DATE_FROM);
		SGText DATE_TO=new SGText("DATE_TO"," 到");
		Util.initDateTime(form, DATE_TO);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		
		TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		SGText view_flag=new SGText("view_flag","");
		view_flag.setValue("CustomFeeSettView");
		view_flag.setVisible(false);
		
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM",Util.TI18N.ORDER_STATE());
		Util.initStatus(STATUS_FROM, StaticRef.ODRNO_STAT, "20");
		
		SGCombo STATUS_TO=new SGCombo("STATUS_TO","到");
		Util.initStatus(STATUS_TO, StaticRef.ODRNO_STAT,"");
		
		TextItem PARENT_CUSTOMER_ID=new TextItem("PARENT_CUSTOMER_ID");
		PARENT_CUSTOMER_ID.setVisible(false);
		ComboBoxItem PARENT_CUSTOMER_NAME=new ComboBoxItem("PARENT_CUSTOMER_NAME",Util.TI18N.PARENT_CUSTOMER_ID());
		PARENT_CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		PARENT_CUSTOMER_NAME.setWidth(FormUtil.Width);
		PARENT_CUSTOMER_NAME.setColSpan(2);
		Util.initCustomerByQuery(PARENT_CUSTOMER_NAME, PARENT_CUSTOMER_ID);
		
		//SGText GEN_METHOD = new SGText("GEN_METHOD", "月结帐号");
		
		form.setItems(ODR_NO,CUSTOM_ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,
				AUDIT_STAT,LOAD_AREA_ID2,LOAD_AREA_NAME2,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,UNLOAD_NAME,EXEC_ORG_ID_NAME,EXEC_ORG_ID,
				ODR_TIME_FROM,ODR_TIME_TO,AUDIT_TIME_FROM,AUDIT_TIME_TO,
				DATE_FROM,DATE_TO,PARENT_CUSTOMER_NAME,STATUS_FROM,STATUS_TO,ACCOUNT_STAT,C_ORG_FLAG,view_flag,PARENT_CUSTOMER_ID);
		
		return form;
	}
	@Override
	public void createForm(DynamicForm form) {

	}
	@Override
	public void initVerify() {
		cache_map.put("DISCOUNT_RATE", "1");
		cache_map.put("PRICE", "1");
	}
	
	
	/**
	 * 调度单页签  --- 右边作业单列表
	 * @author fangliangmeng
	 * @return
	 */
	private SGTable createShpmTable(){
		
		itemTable = new SGTable(detailDS,"100%","100%",false,true,false);
        itemTable.setCanEdit(false);
        itemTable.setAutoFitData(Autofit.VERTICAL);
         
        ListGridField ROW = new ListGridField("ODR_ROW","行号",30);
 		ListGridField SKU_NAME = new ListGridField("SKU_NAME", Util.TI18N.SKU_NAME(), 120);
 		ListGridField SKU = new ListGridField("SKU", Util.TI18N.SKU(), 80);
 		final ListGridField UOM = new ListGridField("UOM", Util.TI18N.UNIT(), 50);
 		ListGridField QNTY = new ListGridField("QNTY", Util.TI18N.PACK_QTY(), 70);
 		ListGridField VOL = new ListGridField("VOL", Util.TI18N.VOL(), 90);
 		ListGridField G_WGT = new ListGridField("G_WGT", Util.TI18N.G_WGT(), 90);
 		
 		itemTable.setFields(ROW, SKU_NAME,SKU,QNTY,UOM, G_WGT,VOL);
		
		return itemTable;
	}
	
	private ListGrid createfeeTable(){
		feeTable = new SGTable(billDS);
		feeTable.setCanExpandRecords(false);
		feeTable.setShowFilterEditor(false);
		feeTable.setCanEdit(false);
		//feeTable.setShowGroupSummary(true); 
		//feeTable.setShowGridSummary(true);  
		
		ListGridField DOC_NO = new ListGridField("DOC_NO", Util.TI18N.DOC_NO(), 100);
//		ListGridField SHPM_NO = new ListGridField("SHPM_NO", "作业单号", 70);
		//ListGridField CHARGE_TYPE_NAME = new ListGridField("CHARGE_TYPE_NAME", Util.TI18N.SETT_TYPE(), 70);
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 70);
		//ListGridField ACCOUNT_STS_NAME = new ListGridField("ACCOUNT_STAT_NAME", Util.TI18N.ACCOUNT_STAT(), 70);
		//ListGridField AUDIT_STS_NAME = new ListGridField("AUDIT_STAT_NAME", Util.TI18N.AUDIT_STAT(), 60);
		//ListGridField PAY_STS = new ListGridField("RECE_STAT_NAME", Util.TI18N.SETT_VERIFI_STAT(), 60);
		ListGridField BAS_VAL = new ListGridField("BAS_VALUE", Util.TI18N.BAS_VALUE(), 60);
		ListGridField PRICE = new ListGridField("PRICE", Util.TI18N.PRICE(), 60);
		ListGridField DISCOUNT_RATE = new ListGridField("DISCOUNT_RATE", Util.TI18N.DISCOUNT(), 60);
		ListGridField DUE_FEE = new ListGridField("DUE_FEE","应收金额", 60);
		DUE_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		DUE_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		DUE_FEE.setShowGridSummary(true); 
		DUE_FEE.setAlign(Alignment.RIGHT);
		ListGridField PRE_FEE = new ListGridField("PRE_FEE","合同金额", 60);
		PRE_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		PRE_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		PRE_FEE.setShowGridSummary(true); 
		PRE_FEE.setAlign(Alignment.RIGHT);
		ListGridField PAY_FEE = new ListGridField("PAY_FEE","实收金额", 80);
		PAY_FEE.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		PAY_FEE.setSummaryFunction(SummaryFunctionType.SUM);
		PAY_FEE.setShowGridSummary(true); 
		PAY_FEE.setAlign(Alignment.RIGHT);
		ListGridField FEE_BAS = new ListGridField("FEE_BAS",Util.TI18N.FEE_BASE(), 80);
		Util.initCodesComboValue(FEE_BAS, "FEE_BASE");
		//ListGridField PRE_RECE_TIME = new ListGridField("PRE_RECE_TIME", Util.TI18N.SETT_VERIFI_PRE_TIME(), 80);
		//ListGridField ACT_PAY_TIME = new ListGridField("ACT_RECE_TIME", Util.TI18N.SETT_VERIFI_TIME(), 80);
		//ListGridField PAYEE = new ListGridField("RECER",Util.TI18N.SETT_VERIFICATER(), 80);
		ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 80);
		
		feeTable.setFields(DOC_NO,FEE_NAME,FEE_BAS,BAS_VAL,PRICE,DISCOUNT_RATE,DUE_FEE,PRE_FEE,PAY_FEE,NOTES);
	    
		feeTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				selectRecord = event.getRecord();
				vm.editRecord(event.getRecord());
				String aud_status = table.getSelectedRecord().getAttributeAsString("AUDIT_STAT_NAME");
				if("未审核".equals(aud_status)){
					initLoadFeeBtn(4);
					disAudit(false, true);
				}else{
					initLoadFeeBtn(5);
					disAudit(true, false);
				}
//				initAccBtn(event.getRecord());
				FEE_ID.setDisabled(true);
			}
		});
		
		feeTable.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				String aud_status = table.getSelectedRecord().getAttributeAsString("AUDIT_STAT_NAME");
				if("未审核".equals(aud_status)){
					initLoadFeeBtn(2);
					disAudit(false, true);
				}else{
					initLoadFeeBtn(5);
					disAudit(true, false);
				}
				vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				FEE_ID.setDisabled(true);
			}
		});
		
		/*final Menu menu = new Menu();
	    menu.setWidth(140);
	    
	    if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_10)){
	    	final MenuItem pay = new MenuItem("核销",StaticRef.ICON_CONFIRM);
	    	pay.addClickHandler(new ReceAction(feeTable, true, this));
	    	menu.addItem(pay);
	    }
	    
	    if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_11)){
	    	final MenuItem cPay = new MenuItem("取消核销",StaticRef.ICON_CONFIRM);
	    	cPay.addClickHandler(new ReceAction(feeTable, false, this));
	    	menu.addItem(cPay);
	    }
	    
	    feeTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });*/
		
		return feeTable;
	}
	
	private SGPanel createFeeInfo(){
		/**
		 * 基本信息
		 * 
		 */
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		TextItem DOC_NO = new TextItem("DOC_NO");
		DOC_NO.setVisible(false);
		
		// 1：订单编号，客户，下单时间，运输服务,客户单号,运输方式
		FEE_ID=new SGCombo("FEE_ID", Util.TI18N.FEE_NAME());
		FEE_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_NAME()));
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " where FEE_ATTR = '53EB6809BFCC436799F735AAE23658B9'");
		
		SETT_ID = new SGCombo("SETT_ID", Util.TI18N.SETT_NAME());
		SETT_ID.setVisible(false);
		
		final SGCombo FEE_BAS = new SGCombo("FEE_BAS", Util.TI18N.FEE_BASE());
		FEE_BAS.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_BASE()));
		Util.initCodesComboValue(FEE_BAS, "FEE_BASE");

		final SGText BAS_VALUE = new SGText("BAS_VALUE", Util.TI18N.BAS_VALUE());

		final SGText PRICE=new SGText("PRICE", Util.TI18N.PRICE());
		
		final SGText DISCOUNT=new SGText("DISCOUNT_RATE", Util.TI18N.DISCOUNT());

		final SGText PRE_FEE = new SGText("PRE_FEE","合同金额");
		PRE_FEE.setDisabled(true);
		
		final SGText DUE_FEE = new SGText("DUE_FEE","应收金额");
		
		final SGText PAY_FEE = new SGText("PAY_FEE", "实收金额");		
		
		SGDateTime PRE_RECE_TIME = new SGDateTime("PRE_RECE_TIME", Util.TI18N.SETT_VERIFI_PRE_TIME());
		PRE_RECE_TIME.setWidth(FormUtil.Width);
		
		SGText ACT_PAY_TIME = new SGText("ACT_RECE_TIME", Util.TI18N.SETT_VERIFI_TIME());
		ACT_PAY_TIME.setDisabled(true);
		
		SGText PAYEE = new SGText("RECER",Util.TI18N.SETT_VERIFICATER());
		PAYEE.setDisabled(true);
		
		SGText NOTES = new SGText("NOTES", Util.TI18N.NOTES());
		NOTES.setColSpan(6);
		NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);
		
		PRICE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				PRE_FEE.setValue(0);
				PAY_FEE.setValue(0);
				DUE_FEE.setValue(Double.parseDouble(BAS_VALUE.getValue().toString()) * Double.parseDouble(PRICE.getValue().toString()) + "");
				//PAY_FEE.setValue(Double.parseDouble(BAS_VALUE.getValue().toString()) * Double.parseDouble(PRICE.getValue().toString()) + "");
			}
		});
		
		DUE_FEE.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.isNotNull(PRE_FEE.getValue())) {
					PRICE.setValue(Double.parseDouble(DUE_FEE.getValue().toString()) / Double.parseDouble(BAS_VALUE.getValue().toString()));
				}
			}
		});
		
		FEE_BAS.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				initBasVal();
			}
		});
			
		feeInfo = new SGPanel();
		feeInfo.setTitleWidth(75);
		feeInfo.setItems(FEE_ID,FEE_BAS,BAS_VALUE,PRICE,DISCOUNT,DUE_FEE,PRE_FEE,PAY_FEE,
				PRE_RECE_TIME,ACT_PAY_TIME,PAYEE,NOTES,DOC_NO,SETT_ID);
		
		return feeInfo;
	}
	
	private ToolStrip createFeeBtn(){
		IButton newBtn = createBtn(StaticRef.CREATE_BTN,SettPrivRef.CUSTOMERFEE_P0_01);
        //newBtn.setIcon(StaticRef.ICON_NEW);
        newBtn.setWidth(80);
        newBtn.setAutoFit(false);
        newBtn.addClickHandler(new NewMultiFormAction(vm, cache_map,null));
        newBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initLoadFeeBtn(2);
				FEE_ID.setDisabled(false);
				SETT_ID.setValue(table.getSelectedRecord().getAttributeAsString("CUSTOMER_NAME"));
				vm.setValue("DOC_NO", table.getSelectedRecord().getAttributeAsString("ODR_NO"));
			}
		});
        
		IButton saveBtn = createBtn(StaticRef.SAVE_BTN,SettPrivRef.CUSTOMERFEE_P0_03);
		//saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(80);
		saveBtn.setAutoFit(false);
		saveBtn.addClickHandler(new SaveReceFeeAction(this,vm));
//		saveBtn.addClickHandler(new ClickHandler() {
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				initLoadFeeBtn(4);
//			}
//		});
		
        IButton delBtn = createBtn(StaticRef.DELETE_BTN,SettPrivRef.CUSTOMERFEE_P0_02);
        //delBtn.setIcon(StaticRef.ICON_DEL);
		delBtn.setWidth(80);
		delBtn.setAutoFit(false);
		delBtn.addClickHandler(new DeleteReceFeeAction(this,vm));
		
		IButton cancelBtn = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.CUSTOMERFEE_P0_04);
		//cancelBtn.setIcon(StaticRef.ICON_CANCEL);
		cancelBtn.setWidth(80);
		cancelBtn.setAutoFit(false);
		cancelBtn.addClickHandler(new CancelMultiFormAction(feeTable, vm));
		cancelBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initLoadFeeBtn(1);
			}
		});
		
		ins_fee_btn = new HashMap<String, IButton>();
		sav_fee_btn = new HashMap<String, IButton>();
		del_fee_btn = new HashMap<String, IButton>();
		
		ins_fee_btn.put(SettPrivRef.CUSTOMERFEE_P0_01, newBtn);
        del_fee_btn.put(SettPrivRef.CUSTOMERFEE_P0_02, delBtn);
        sav_fee_btn.put(SettPrivRef.CUSTOMERFEE_P0_03, saveBtn);
        sav_fee_btn.put(SettPrivRef.CUSTOMERFEE_P0_04, cancelBtn);
        
        initLoadFeeBtn(1);
        
		toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(newBtn,saveBtn,delBtn,cancelBtn); 
	    
	    return toolStrip;
	}
	

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	public void initBasVal(){
		//final SGText val = basVal;
		if("VEHICLE".equals(vm.getItem("FEE_BAS").getValue())) {
			vm.setValue("BAS_VALUE", "1");
		}
		else {
			Util.async.getBasVal(table.getSelectedRecord().getAttributeAsString("ODR_NO"),vm.getItem("FEE_BAS").getDisplayValue(), new AsyncCallback<String>() {
	
				@Override
				public void onFailure(Throwable caught) {
					;
				}
	
				@Override
				public void onSuccess(String result) {
					vm.setValue("BAS_VALUE", result);
				}
				
			});
		}
	}
	
	/**
	 * 调度单页签 费用按钮状态变化
	 * @author fangliangmeng
	 * @param typ
	 */
	public void initLoadFeeBtn(int typ){
		if(typ == 1){
			enableOrDisables(ins_fee_btn, true);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, false);
		}else if(typ == 2){
			enableOrDisables(ins_fee_btn, false);
			enableOrDisables(sav_fee_btn, true);
			enableOrDisables(del_fee_btn, false);
		}else if(typ == 3){
			enableOrDisables(ins_fee_btn, false);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, true);
		}else if(typ == 4){
			enableOrDisables(ins_fee_btn, true);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, true);
		}else if(typ == 5){
			enableOrDisables(ins_fee_btn, false);
			enableOrDisables(sav_fee_btn, false);
			enableOrDisables(del_fee_btn, false);
		}
	}
	
	
	public void disAudit(boolean b1,boolean b2){
		if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_05)){
			confirmAudit.setDisabled(b1);
		}
		if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_06)){
			cancelAudit.setDisabled(b2);
		}
	}
	
	public void disAccount(boolean b1,boolean b2){
		/**
		if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_05)){
			confirmAcc.setDisabled(b1);
		}
		if(isPrivilege(SettPrivRef.CUSTOMERFEE_P0_06)){
			cancelAcc.setDisabled(b2);
		}**/
	}
	
	
	public void initAccBtn(Record record){
//		String acc_status = record.getAttributeAsString("ACCOUNT_STAT_NAME");
		String aud_status = record.getAttributeAsString("AUDIT_STAT_NAME");
//		String status_name = event.getRecord().getAttributeAsString("STATUS_NAME");
		
//		if("已回单".equals(status_name)){
			
//			if("未对账".equals(acc_status) || "已打回".equals(acc_status)){
//				disAccount(false,true);
//				disAudit(true, true);
//				initLoadFeeBtn(1);
//			}else{
				if("未审核".equals(aud_status)){
					disAccount(true,false);
					disAudit(false, true);
					
				}else{
					disAccount(true,true);
					disAudit(true, false);
				}
//				initLoadFeeBtn(5);
//			}
//		}
	}
	
	public CustomFeeSettView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		CustomFeeSettView view = new CustomFeeSettView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
	
	public void disableFeeName() {
		FEE_ID.setDisabled(true);
	}
}
