package com.rd.client.view.tms;

import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.ImagePreviewWin;
import com.rd.client.action.tms.odrreceipt.CancelOrderAction;
import com.rd.client.action.tms.odrreceipt.ConfirmDamageAction;
import com.rd.client.action.tms.odrreceipt.ConfirmOrderAction;
import com.rd.client.action.tms.odrreceipt.DeleteManageAction;
import com.rd.client.action.tms.odrreceipt.NewManageAction;
import com.rd.client.action.tms.odrreceipt.SaveManageAction;
import com.rd.client.action.tms.reclaim.ChangeQntyAction;
import com.rd.client.action.tms.shpmreceipt.UploadImageAction;
import com.rd.client.common.action.AllSelectAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.OrdSelectAction;
import com.rd.client.common.action.UnSelectAction;
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
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.TranLossDamageDS;
import com.rd.client.ds.tms.TranOrderDS1;
import com.rd.client.ds.tms.TranOrderItemDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SkuWin;
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
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理-->托运单回单
 * @author wangjun
 *
 */
@ClassForNameAble
public class TmsOdrReceiptView extends SGForm implements PanelFactory {

	private SGPanel searchForm;
	private Window searchWin;
	private Window ordManageWin;
//	private Window manShipWin;
	private Window transTrackWin;
	private SectionStack section;
//	private SectionStack LossDamageSection;
	private DataSource orderDS;
	private DataSource orderlstDS;
	private DataSource LossDamageDS;
	
	public SGTable orderTable;
	public SGTable orderlstTable;
    public SGTable damageTable;
	
	public String order_no;
	public Record clickrecord;
	public int hRow=0;
	public ListGridRecord[] unorderTablelstRec;   //
	public ValuesManager vm;
	public IButton searchButton;
	public IButton confirmorderButton;
	public IButton cancelorderButton;
	public IButton canButton;
	public ListGridField UNLOAD_TIME;
	public ListGridField UNLOAD_DELAY_REASON;
	public ListGridField POD_TIME;
	public ListGridField POD_DELAY_REASON;
	
	/*public TmsOdrReceiptView(String id) {
	    super(id);
	}*/
	
	public Canvas getViewPanel() {

		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		
		orderDS = TranOrderDS1.getInstance("V_ORDER_HEADER2", "TRANS_ORDER_HEADER");
		orderlstDS = TranOrderItemDS.getInstance("V_ORDER_ITEM","TRANS_ORDER_ITEM");
		LossDamageDS=TranLossDamageDS.getInstance("V_LOSS_DAMAGE_ORDER","TRANS_LOSS_DAMAGE");
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip toolStrip2=new ToolStrip();
		toolStrip2.setAlign(Alignment.LEFT);
		toolStrip2.setMembersMargin(20);
//		toolStrip2.setAlign(Alignment.RIGHT);
		
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("100%");
			
		layout.addMember(toolStrip);
		layout.addMember(section);
		
		orderTable = new SGTable(orderDS, "100%", "80%") {
			
			@Override  
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {   
                if (colNum == 22) {   
                    return "cursor:pointer";
                } 
                else {   
                    return super.getCellCSSText(record, rowNum, colNum);   
                }  
            }

			public DataSource getRelatedDataSource(ListGridRecord record) {
				orderlstDS = TranOrderItemDS.getInstance("V_ORDER_ITEM","TRANS_ORDER_ITEM");
				order_no = record.getAttributeAsString("ODR_NO");
				return orderlstDS;

			}

			// 第2层表
			protected Canvas getExpansionComponent(final ListGridRecord record) {

				VLayout layout = new VLayout();

				orderlstTable = new SGTable(getRelatedDataSource(record),"100%", "50", false, true, false);
				orderlstTable.setCanEdit(true);
				orderlstTable.setAlign(Alignment.LEFT);
				orderlstTable.setShowRowNumbers(false);
				orderlstTable.setAutoFitData(Autofit.VERTICAL);

				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("ODR_NO", order_no);

				/**
				 * 托运单明细列表
		         * 行号,货品代码，货品名称，规格型号，单位，订单数量，发货数量，收货数量， 货损数量  ,毛重[吨]，体积[方]
		         */
				ListGridField ODR_ROW = new ListGridField("ODR_ROW",Util.TI18N.ORD_ROW(),45);//行号
				ODR_ROW.setCanEdit(false);
				ListGridField SKU_ID = new ListGridField("SKU",Util.TI18N.SKU(),70);//货品代码
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
				Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
				ListGridField EA = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),60);//
				EA.setCanEdit(false);
				EA.setAlign(Alignment.RIGHT);
				Util.initFloatListField(EA, StaticRef.QNTY_FLOAT);
				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.FOLLOW_LD_QNTY(),60);//发货数量
				LD_QNTY.setCanEdit(false);
				LD_QNTY.setAlign(Alignment.RIGHT);
				Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
				//LD_QNTY.addEditorExitHandler(new ChangeQntyAction(orderlstTable, getView()));
				final ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);//收货数量
				UNLD_QNTY.setCanEdit(true);
				UNLD_QNTY.setAlign(Alignment.RIGHT);
				Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
				UNLD_QNTY.addEditorExitHandler(new ChangeQntyAction(orderlstTable, getView()));
				final ListGridField G_WGT = new ListGridField("UNLD_GWGT",Util.TI18N.G_WGT(),70);//毛重[吨]
				G_WGT.setCanEdit(true);
				G_WGT.setAlign(Alignment.RIGHT);
				Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
				final ListGridField VOL = new ListGridField("UNLD_VOL",Util.TI18N.VOL(),70);//  体积
				VOL.setCanEdit(true);
				VOL.setAlign(Alignment.RIGHT);
				Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
				final ListGridField DAMA_QNTY = new ListGridField("DAMA_QNTY",Util.TI18N.TRANS_QNTY(),70);
				DAMA_QNTY.setCanEdit(true);
				DAMA_QNTY.setAlign(Alignment.RIGHT);//货损数量
				Util.initFloatListField(DAMA_QNTY, StaticRef.QNTY_FLOAT);
				
				
				final ListGridField LOTATT01 = new ListGridField("LOTATT01",Util.TI18N.LOTATT01(),60);// 批号 
				final ListGridField LOTATT02 = new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);//专供标识
				
				
				orderlstTable.addDoubleClickHandler(new DoubleClickHandler() {
					
					@Override
					public void onDoubleClick(DoubleClickEvent event) {
						if (StaticRef.UNLOADED.equals(clickrecord.getAttribute("UNLOAD_STAT"))
								&& StaticRef.SO_CONFIRM.equals(clickrecord.getAttribute("STATUS"))) {
							UNLD_QNTY.setCanEdit(true);
							G_WGT.setCanEdit(true);
							VOL.setCanEdit(true);
							DAMA_QNTY.setCanEdit(true);
							
						}else{
							UNLD_QNTY.setCanEdit(false);
							G_WGT.setCanEdit(false);
							VOL.setCanEdit(false);
							DAMA_QNTY.setCanEdit(false);
						}
						
						
					}
				});
				
				orderlstTable.setFields(ODR_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,QNTY,EA,LD_QNTY,UNLD_QNTY,DAMA_QNTY,VOL,G_WGT,
						LOTATT01,LOTATT02);//,LOSS_DAMAGE_TYP,TRANS_UOM,AMOUNT);
				
						orderlstTable.fetchData(findValues,new DSCallback(){

							@Override
							public void execute(DSResponse response,
									Object rawData, DSRequest request) {
								unorderTablelstRec = orderlstTable.getRecords();
							}
							
						});
						layout.addMember(orderlstTable);
						layout.setLayoutLeftMargin(5);
		
						return layout;
			}

		};

		createListField(orderTable);
		setShowHover(false);
		setCanHover(false);
		orderTable.setCanExpandRecords(true);
		//orderTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		orderTable.setShowFilterEditor(false);
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		//orderTable.fetchData(crit);

		orderTable.setCanEdit(true);
		// loadTable.setExpansionCanEdit(false); 
		orderTable.setShowSelectedStyle(true);
		orderTable.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {	
				
				
				  
				  Util.async.getServTime("yyyy/MM/dd HH:mm",new AsyncCallback<String>() {
					  @Override
					public void onSuccess(String result) {
						  ListGridRecord rec = orderTable.getSelectedRecord();
							int row = orderTable.getRecordIndex(rec);//MAX_UNLOAD_TIME 2010-12-12
							
							  if(!ObjUtil.isNotNull(rec.getAttribute("UNLOAD_TIME")) 
									&& !ObjUtil.isNotNull(orderTable.getEditValue(row, "UNLOAD_TIME"))){
								
							  	 orderTable.setEditValue(row, "UNLOAD_TIME" ,rec.getAttribute("MAX_UNLOAD_TIME"));
							  }
							  if(!ObjUtil.isNotNull(rec.getAttribute("POD_TIME"))
										&& !ObjUtil.isNotNull(orderTable.getEditValue(row, "POD_TIME"))){
//									 orderTable.setEditValue(row, "POD_TIME" ,rec.getAttribute("MAX_UNLOAD_TIME"));
								  orderTable.setEditValue(row, "POD_TIME" ,result);
							  }	
								//orderTable.setEditValue(row, "UNLOAD_TIME" ,"2010-12-12");
								//orderTable.setEditValue(row, "POD_TIME" ,"2010-12-12");
						  
				  	}
					
					public void onFailure(Throwable caught) {
						
						
				  	}
				  	
				  });
				  
//				  canButton.enable(); wangjun 2011-3-8
				  
				  if(StaticRef.SO_CONFIRM_NAME.equals(clickrecord.getAttribute("STATUS_NAME"))&&StaticRef.UNLOADED.equals(clickrecord.getAttribute("UNLOAD_STAT"))){
					  setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_03,canButton,true);
				  }
			}
			
		});
		
		orderTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				clickrecord = event.getRecord();
				hRow = orderTable.getRecordIndex(event.getRecord());
				
				if (event.getRecord() == null) {
					return;
				}
				ListGridRecord [] gridRecord = orderTable.getSelection();
				if(gridRecord.length == 1){
					if(StaticRef.SO_RECEIPT_NAME.equals(clickrecord.getAttribute("STATUS_NAME"))){
//						confirmorderButton.disable();wangjun 2011-3-8
//						cancelorderButton.enable();
						
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_01,confirmorderButton,false);
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_02,cancelorderButton,true);
						
						UNLOAD_TIME.setCanEdit(false);
						UNLOAD_DELAY_REASON.setCanEdit(false);
						POD_TIME.setCanEdit(false);
						POD_DELAY_REASON.setCanEdit(false);
						
					}else if(StaticRef.SO_CONFIRM_NAME.equals(clickrecord.getAttribute("STATUS_NAME"))){
//						confirmorderButton.enable();
//						cancelorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_01,confirmorderButton,true);
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_02,cancelorderButton,false);
						
						UNLOAD_TIME.setCanEdit(true);
						UNLOAD_DELAY_REASON.setCanEdit(true);
						POD_TIME.setCanEdit(true);
						POD_DELAY_REASON.setCanEdit(true);
						
					} else {
						
//						confirmorderButton.disable();
//						cancelorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_01,confirmorderButton,false);
						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_02,cancelorderButton,false);
						
						UNLOAD_TIME.setCanEdit(false);
						UNLOAD_DELAY_REASON.setCanEdit(false);
						POD_TIME.setCanEdit(false);
						POD_DELAY_REASON.setCanEdit(false);
						
					}
			}
					
//				orderTable.addClickHandler(new ClickHandler() {
//					
//					@Override
//					public void onClick(ClickEvent event) {
//						setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_03,canButton,false);
//						
//					}
//				});
				
//				}else if(gridRecord.length > 1) {
//					StringBuffer sf1 = new StringBuffer();
//					StringBuffer sf2 = new StringBuffer();
//				
//					for(int i = 0 ; i < gridRecord.length ; i++){
//						
//						if(StaticRef.SO_RECEIPT.equals(gridRecord[i].getAttribute("STATUS_FORM"))){
//							sf1.append(gridRecord[i].getAttribute("STATUS_FORM"));
//						} else if(StaticRef.SO_CONFIRM.equals(gridRecord[i].getAttribute("STATUS_FORM"))&&StaticRef.UNLOADED.equals(gridRecord[i].getAttribute("UNLOAD_STAT"))){
//							sf2.append(gridRecord[i].getAttribute("UNLOAD_STAT"));
//							sf2.append(gridRecord[i].getAttribute("STATUS_FORM"));
//						}
//					}
//					if(sf1.length()>1&&sf2.length() == 0){
//						cancelorderButton.enable();
//						confirmorderButton.disable();
//					}
//					if(sf2.length()>1&&sf1.length() == 0){
//						confirmorderButton.enable();
//						cancelorderButton.disable();
//					}
//					if(sf2.length()>1&&sf1.length() > 0){
//						confirmorderButton.enable();
//						cancelorderButton.enable();
//					}
//				}
				order_no = event.getRecord().getAttributeAsString("ODR_NO");
				orderTable.OP_FLAG = "M";
				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG", "M");
				criteria.addCriteria("ODR_NO", order_no);
				damageTable.fetchData(criteria);
			}
		});
		
		// loadTable.setShowRowNumbers(false);
		
		createBtnWidget(toolStrip);

		Canvas[] widget = new Canvas[2];
		widget[0] = orderTable;
		widget[1] = damageTable;
		
    	SectionStackSection listItem = new SectionStackSection(Util.TI18N.TRANS_ORDER_LIST());//托运单列表
		listItem.setItems(orderTable);
		listItem.setExpanded(true);
		listItem.setControls(new SGPage(orderTable, true).initPageBtn());
		section.addSection(listItem);

	    damageTable = new SGTable(LossDamageDS,"100%","30%");//
		createBtnWidget2(toolStrip2);
		
		SectionStackSection LossDamageItem=new SectionStackSection(Util.TI18N.LOSDAM_FLAG());//货损货差
		LossDamageItem.setItems(damageTable,toolStrip2);
		LossDamageItem.setExpanded(false);
		section.addSection(LossDamageItem);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(false);
    	
    	//section.addMember(toolStrip2);
        //table = new SGTable(orderDS, "100%", "70%");
		createLossDamageInfo();
		initVerify();
		
		return layout;
	}


	private void createLossDamageInfo() {
		
//		damageTable = new SGTable(LossDamageDS,"100%","50%");
		damageTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				itemRow = event.getRecordNum();
				initSaveBtn();
				
				
			}
		});
		damageTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddBtn();
			}
		});
		damageTable.setShowFilterEditor(false);
		damageTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		damageTable.setShowRowNumbers(true);

		ListGridField ODR_NO = new ListGridField("ODR_NO", "",0);//货品代码
		ODR_NO.setHidden(true);
		ListGridField SKU_ID = new ListGridField("SKU_ID",Util.TI18N.SKU(),70);//货品代码
		SKU_ID.setHidden(true);
		
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);//货品名称
		SKU_NAME.setCanEdit(true);
		SKU_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()));
		//Util.initComboValue(SKU_NAME, "BAS_SKU","SKU","SKU_CNAME","","");
		
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),80);//货品规格型号
		SKU_SPEC.setCanEdit(true);
		SKU_SPEC.setAlign(Alignment.LEFT); 
		//Util.initComboValue(SKU_SPEC, "V_SKU","SKU_SPEC","","","");
		
		final ListGridField DAMA_TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.UOM(),50);
		DAMA_TRANS_UOM.setCanEdit(true); //DAMA_TRANS_UOM
		//TRANS_UOM.setAlign(Alignment.RIGHT);//  单位  
		//Util.initComboValue(UOM, "V_BAS_UOM", "UOM", "DESCR", "", "");
		DAMA_TRANS_UOM.setTitle(ColorUtil.getRedTitle(Util.TI18N.UOM()));
		
		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK_ID(),80);
		PACK_ID.setCanEdit(true); //包装
		PACK_ID.setAlign(Alignment.RIGHT);  
		
		
		final ListGridField TRANS_QNTY = new ListGridField("QNTY",Util.TI18N.TRANS_QNTY(),70);
		TRANS_QNTY.setCanEdit(true);
		TRANS_QNTY.setAlign(Alignment.RIGHT);//货损数量 
		//TRANS_QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRANS_QNTY()));
		Util.initFloatListField(TRANS_QNTY, StaticRef.QNTY_FLOAT);
		
		final ListGridField LOSS_DAMAGE_TYP = new ListGridField("LOSS_DAMAGE_TYP",Util.TI18N.LOSS_DAMAGE_TYP(),70);
		LOSS_DAMAGE_TYP.setCanEdit(true);
		//LOSS_DAMAGE_TYP.setAlign(Alignment.RIGHT);// 残损类型  
		Util.initCodesComboValue(LOSS_DAMAGE_TYP, "LOSS_DAMAGE_TYP");	
		
		
		
		final ListGridField AMOUNT = new ListGridField("AMOUNT",Util.TI18N.TRANS_AMOUNT(),80);
		AMOUNT.setCanEdit(true);
		AMOUNT.setAlign(Alignment.RIGHT);//  残损金额  
		Util.initFloatListField(AMOUNT, StaticRef.PRICE_FLOAT);
		
		final ListGridField DUTYER = new ListGridField("DUTYER",Util.TI18N.DUTYER(),75);
		DUTYER.setCanEdit(true);
		DUTYER.setAlign(Alignment.LEFT);//  责任人
		//LoginCache.getLoginUser().getUSER_ID();
//		DUTYER.setDefaultValue(LoginCache.getLoginUser().getUSER_ID());
		
		ListGridField COMPANY_ACOUNT = new ListGridField("COMPANY_ACOUNT","公司承担金额",95);
		COMPANY_ACOUNT.setCanEdit(true);
		
		ListGridField DRIVER_ACOUNT = new ListGridField("DRIVER_ACOUNT","司机承担金额",95);
		DRIVER_ACOUNT.setCanEdit(true);
		
		ListGridField DUTY_TO = new ListGridField("DUTY_TO","货损责任",95);
		Util.initCodesComboValue(DUTY_TO, "DUTY_TO");
		DUTY_TO.setCanEdit(true);
		
		final ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON(),250);
		REASON.setCanEdit(true);
		REASON.setAlign(Alignment.LEFT);//  原因描述
		
		final ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),70);
		SHPM_NO.setHidden(true);
		final ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),70);
		LOAD_NO.setHidden(true);
		PACK_ID.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
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
					new SkuWin(damageTable,itemRow,"40%", "38%",order_no).getViewPanel();
			}
		});
		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
//		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where 1=1", "");
		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");

	}

	public void createBtnWidget(ToolStrip toolStrip) {
		// 组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		//toolStrip.setPadding(2);
		//toolStrip.setSeparatorSize(12);
		//toolStrip.addSeparator();
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(orderDS, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
					searchWin.setWidth(616);
					searchWin.setHeight(404);
				} else {
					searchWin.show();
				}

			}
		});

		// 【确认回单】按钮
		confirmorderButton = createBtn(StaticRef.CONFIRM_ORDER_BTN,TrsPrivRef.ODRCLAIM_P0_01);
		confirmorderButton.addClickHandler(new ConfirmOrderAction(orderTable,this));
		   
		// 【批量回单】按钮
		IButton manyorderButton = createBtn(StaticRef.MANY_ORDER_BTN); //   
		manyorderButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (ordManageWin == null) {
					ordManageWin = new OrdManageWin(orderDS, null,
							section.getSection(0)).getViewPanel();
				} else {
					ordManageWin.show();
				}

			}
		});
		// 【取消回单】按钮
		cancelorderButton = createBtn(StaticRef.CANCEL_ORDER_BTN,TrsPrivRef.ODRCLAIM_P0_02);
		cancelorderButton.addClickHandler(new CancelOrderAction(orderTable,this));
		// 取消按钮
		canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ODRCLAIM_P0_03);
		canButton.addClickHandler(new CancelAction(orderTable));

		// 导出按钮
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.ODRCLAIM_P0_04);
		expButton.addClickHandler(new ExportAction(orderTable, " addtime desc"));

		// 【上传影像】按钮
		IButton putimageButton = createBtn(StaticRef.PUT_IMAGE_BTN,TrsPrivRef.ODRCLAIM_P0_05);
		putimageButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(orderTable.getSelectedRecord() != null){
					if(orderTable.getSelectedRecord().getAttribute("ODR_NO")!= null){
						new UploadImageAction(orderTable,orderTable.getSelectedRecord().getAttribute("ODR_NO"),StaticRef.ORDER_RECLIM_URL).getViewPanel().show();
					}
				} else {
					SC.say("请选择需要上传影像的客户单号.");
				}
			}
		});
		//按钮状态
		searchButton.enable();
//		confirmorderButton.disable(); wangjun 2011-3-8
//    	cancelorderButton.disable();
//		canButton.disable();
		
		setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_01,confirmorderButton,false);
		setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_02,cancelorderButton,false);
		setButtonEnabled(TrsPrivRef.ODRCLAIM_P0_03,canButton,false);
		
		canButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				canButton.disable();
			}
		});
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton, confirmorderButton, cancelorderButton,
				canButton, expButton, putimageButton);

	}


	private void createBtnWidget2(ToolStrip toolStrip2) {
		
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		//新增按钮
	    IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.ODRCLAIM_P1_01);
        newButton.addClickHandler(new NewManageAction(damageTable,this));
        
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ODRCLAIM_P1_02);
       //saveButton.addClickHandler(new SaveManageAction(damageTable,check_map));
        saveButton.addClickHandler(new SaveManageAction(damageTable,check_map,this));
        
        //删除按钮
        IButton delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.ODRCLAIM_P1_03);
        delButton.addClickHandler(new DeleteManageAction(damageTable,this));
        
        //取消按钮
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ODRCLAIM_P1_04);
        canButton.addClickHandler(new CancelAction(damageTable,this));
        
        //确认按钮
        IButton cfmButton = createBtn(StaticRef.CONFIRM_BTN,TrsPrivRef.ODRCLAIM_P1_05);
        cfmButton.setTitle("确认");
        cfmButton.addClickHandler(new ConfirmDamageAction(damageTable,orderTable));
        
        toolStrip2.setMembersMargin(4);
        toolStrip2.setMembers(newButton, saveButton, delButton, canButton,cfmButton);
  
        add_map.put(TrsPrivRef.ODRCLAIM_P1_01, newButton);
        del_map.put(TrsPrivRef.ODRCLAIM_P1_03, delButton);
        save_map.put(TrsPrivRef.ODRCLAIM_P1_02, saveButton);
        save_map.put(TrsPrivRef.ODRCLAIM_P1_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
		
	}
	// 查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		form.setDataSource(orderDS);
		form.setAutoFetchData(false);
//		form.setWidth(300);
//		form.setHeight(600);
		form.setCellPadding(2);

		// 第一行：模糊查询
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setWidth(300);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		txt_global.setTitleOrientation(TitleOrientation.TOP);

	    final SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
	    Util.initCustComboValue(CUSTOMER_NAME,LoginCache.getDefCustomer().get("CUSTOMER_ID"));
	    
		SGText ORD_NO=new SGText("ODR_NO",Util.TI18N.ORDER_CODE());//订单编号
	
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO() );//客户单号
		
		TextItem  EXEC_ORG_ID= new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());//发货区域
		LOAD_AREA_NAME.setColSpan(2);
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(LOAD_AREA_NAME, null);
		LOAD_AREA_NAME.setWidth(FormUtil.Width);
		
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());//收货区域
		UNLOAD_AREA_NAME.setColSpan(2);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME, null);
		UNLOAD_AREA_NAME.setWidth(FormUtil.Width);

		SGCombo TRANS_SRVC_ID= new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());//运输服务
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		SGLText LOAD_NAME = new SGLText("LOAD_NAME", Util.TI18N.LOAD_NAME(),true);//发货方
		
		SGLText UNLOAD_NAME = new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方

		SGCombo ORDER_STATE = new SGCombo("STATUS_FORM", Util.TI18N.ORDER_STATE());//订单状态从 到  
		Util.initStatus(ORDER_STATE, StaticRef.ODRNO_STAT,StaticRef.SO_CONFIRM);
		
		SGCombo ORDER_STATE_TO = new SGCombo("STATUS_TO", "到");
		Util.initStatus(ORDER_STATE_TO, StaticRef.ODRNO_STAT,StaticRef.SO_CONFIRM);
		
		SGDateTime ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		SGDateTime ODR_TIME_TO = new SGDateTime("ODR_TIME_TO", "到");//订单时间
		ODR_TIME_FROM.setWidth(FormUtil.Width);
		ODR_TIME_TO.setWidth(FormUtil.Width);
		
		SGDateTime PRE_POD_TIME_FROM = new SGDateTime("PRE_POD_TIME_FROM", Util.TI18N.FROM_POD_TIME());
		SGDateTime PRE_POD_TIME_TO = new SGDateTime("PRE_POD_TIME_TO", "到");//计划回单时间
		PRE_POD_TIME_FROM.setWidth(FormUtil.Width);
		PRE_POD_TIME_TO.setWidth(FormUtil.Width);
		
		SGDateTime ADDTIME_FROM = new SGDateTime("ADDTIME_FROM", Util.TI18N.ORD_ADDTIME());
		ADDTIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");//创建时间 
		ADDTIME_TO.setWidth(FormUtil.Width);
		
		SGCombo ARRIVE_STATE = new SGCombo("UNLOAD_STAT", Util.TI18N.UNLOAD_STAT(),true); //到货状态
		Util.initStatus(ARRIVE_STATE, StaticRef.UNLOAD_STAT,StaticRef.UNLOADED);
		//ARRIVE_STATE.setValue(20);
		//ARRIVE_STATE.setColSpan(1);
		
		SGCombo ORD_PRO_LEVER=new SGCombo("UGRT_GRD", Util.TI18N.ORD_PRO_LEVER());//订单优先级
		Util.initCodesComboValue(ORD_PRO_LEVER, "UGRT_GRD");
		//UNLOAD_AREA_NAME.setWidth(128);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_NAME",Util.TI18N.SUPLR_NAME());
		Util.initSupplier(SUPLR_NAME, "");
		SGText PLATE_NO =new SGText("PLATE_NO",Util.TI18N.PLATE_NO() );
		
		SGCheck ORD_LIST = new SGCheck("ORD_LIST", Util.TI18N.ORD_LIST());	//同一车托运单
		ORD_LIST.setColSpan(2);
		
		SGCheck BACK_OR = new SGCheck("BACK_OR", Util.TI18N.BACK_OR());	//应回未回
		BACK_OR.setColSpan(2);
		
		SGCombo UPLOAD_FLAG=new SGCombo("UPLOAD_FLAG","上传标记");
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("N", "未上传");
		map.put("Y", "已上传");
		UPLOAD_FLAG.setValueMap(map);
		
		SGCheck LOSDAM_FLAG=new SGCheck("LOSDAM_FLAG", "货损货差");
		
		form.setItems(CUSTOMER_NAME,ORD_NO,CUSTOM_ODR_NO,EXEC_ORG_ID_NAME,LOAD_AREA_NAME,UNLOAD_AREA_NAME,TRANS_SRVC_ID,ORD_PRO_LEVER
				,LOAD_NAME,UNLOAD_NAME,ADDTIME_FROM,ADDTIME_TO,ODR_TIME_FROM,ODR_TIME_TO,PRE_POD_TIME_FROM,PRE_POD_TIME_TO,ORDER_STATE,
				ORDER_STATE_TO,ARRIVE_STATE,SUPLR_NAME,PLATE_NO,UPLOAD_FLAG,ORD_LIST,C_ORG_FLAG,BACK_OR,EXEC_ORG_ID,LOSDAM_FLAG);
		return form;
		
		

	}

	public void createForm(DynamicForm form) {

	}

	public void initVerify() {
		check_map.put("TABLE", "TRANS_ORDER_HEADER");
		cache_map.put("ORD_LIST", "Y");
		
		//check_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		//check_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.TRANS_QNTY());
		
	}



	private void createListField(final SGTable groupTable) {
//		final ListGridField ODR_NO  = new ListGridField("ODR_NO" ,"");
//		ODR_NO.setHidden(true);
		final ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(), 60);// 客户单号
		CUSTOM_ODR_NO.setCanEdit(false);
		ListGridField STATUS = new ListGridField("STATUS_NAME", Util.TI18N.STATUS(), 60);// 状态    订单状态
		STATUS.setCanEdit(false);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME(), 90);// 收货方
		UNLOAD_NAME.setCanEdit(false);
		UNLOAD_TIME = new ListGridField("UNLOAD_TIME", Util.TI18N.FOLLOW_UNLOAD_TIME(), 120);// 实际收货时间  
		//UNLOAD_TIME.setCanEdit(false);
		//Util.initListGridDateTime(UNLOAD_TIME);
		Util.initDateTime(orderTable,UNLOAD_TIME);
		UNLOAD_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.FOLLOW_UNLOAD_TIME()));
		UNLOAD_DELAY_REASON = new ListGridField("UNLOAD_DELAY_REASON", Util.TI18N.UNLOAD_DELAY_REASON(),90);// 收货延迟原因  
		//LOAD_DELAY_REASON.setCanEdit(false);
        Util.initCodesComboValue(UNLOAD_DELAY_REASON, "UNLOAD_DELAY_REASON");	
        ListGridField PRE_POD_TIME = new ListGridField("PRE_POD_TIME", Util.TI18N.FROM_POD_TIME(), 120);//计划回单时间
		PRE_POD_TIME.setCanEdit(false);
		POD_TIME = new ListGridField("POD_TIME",Util.TI18N.FOLLOW_POD_TIME(),120);// 实际回单时间
		//Util.initListGridDateTime(POD_TIME);
		Util.initDateTime(orderTable,POD_TIME);
		POD_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.FOLLOW_POD_TIME()));
		POD_DELAY_REASON = new ListGridField("POD_DELAY_REASON",  Util.TI18N.POD_DELAY_REASON(),90);// 回单延迟原因
		//POD_DELAY_REASON.setCanEdit(false);
		Util.initCodesComboValue(POD_DELAY_REASON, "POD_DELAY_REASON");	
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY", Util.TI18N.ORD_TOT_QNTY(),60);//订单数量 
		TOT_QNTY.setAlign(Alignment.RIGHT);
		TOT_QNTY.setCanEdit(false);
		
		ListGridField ODR_TIME = new ListGridField("ODR_TIME", Util.TI18N.ODR_TIME(), 120);// 订单时间      
		ODR_TIME.setCanEdit(false);
		ListGridField UOM = new ListGridField("TRANS_UOM", Util.TI18N.UNIT(),50);//单位   UOM
		UOM.setCanEdit(false);
		ListGridField LD_QNTY = new ListGridField("TOT_LD_QNTY", Util.TI18N.FOLLOW_LD_QNTY(),60);//发货数量   LD_QNTY 
		LD_QNTY.setAlign(Alignment.RIGHT);
		LD_QNTY.setCanEdit(false);
		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
		ListGridField UNLD_QNTY = new ListGridField("TOT_UNLD_QNTY", Util.TI18N.UNLD_QNTY(),60);// 收货数量  UNLD_QNTY
		UNLD_QNTY.setAlign(Alignment.RIGHT);
		UNLD_QNTY.setCanEdit(false);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
	    ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.G_WGT(),60);//毛重  总重量 	//毛重[吨] 
	    TOT_GROSS_W.setAlign(Alignment.RIGHT);
	    TOT_GROSS_W.setCanEdit(false);
	    Util.initFloatListField(TOT_GROSS_W, StaticRef.GWT_FLOAT);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.VOL(),60);//体积[方]  
		TOT_VOL.setAlign(Alignment.RIGHT);
		TOT_VOL.setCanEdit(false);
		Util.initFloatListField(TOT_VOL, StaticRef.VOL_FLOAT);
		ListGridField PRE_LOAD_TIME = new ListGridField("PRE_LOAD_TIME", Util.TI18N.PRE_LOAD_TIME(),120);//计划发货时间  
		PRE_LOAD_TIME.setCanEdit(false);
		ListGridField LOAD_TIME = new ListGridField("LOAD_TIME",Util.TI18N.MANAGE_END_LOAD_TIME(),120);//
		//实际发运时间  DEPART_TIME//实际发运时间 
		LOAD_TIME.setCanEdit(false);
		ListGridField OP_POD_TIME = new ListGridField("OP_POD_TIME",Util.TI18N.OP_POD_TIME(),120);//回单登记时间
		OP_POD_TIME.setCanEdit(false);
		final ListGridField ORD_IMA = new ListGridField("ORD_IMA",Util.TI18N.ORD_IMA(), 60);//回单影像    ORD_IMA
		ORD_IMA.setType(ListGridFieldType.LINK);   
		ORD_IMA.setAlign(Alignment.CENTER);  
		ORD_IMA.setLinkText(Canvas.imgHTML("images/rd/scan.png", 16, 16, "info", "align=center", null));   

//		ORD_IMA.addEditorExitHandler(new EditorExitHandler() {
//			
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//           	 new ImagePreviewWin(orderTable,StaticRef.ORDER_RECLIM_PREVIEW_URL,CUSTOM_ODR_NO.getName()).getViewPanel();				
//			}
//		});
		 
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID_NAME", Util.TI18N.TRANS_SRVC_ID(), 60);
		TRANS_SRVC_ID.setCanEdit(false);
		//TRANS_SRVC_ID_NAME
		ListGridField ORD_NO = new ListGridField("ODR_NO", Util.TI18N.ORD_NO(), 110);//订单编号    托运单编号
		ORD_NO.setCanEdit(false);
		ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2", Util.TI18N.CITY(),70);//发货区域
		LOAD_AREA_NAME2.setCanEdit(false);
		ListGridField UNLOAD_AREA_NAME2 = new ListGridField("UNLOAD_AREA_NAME2", Util.TI18N.CITY(),70);//收货区域
		UNLOAD_AREA_NAME2.setCanEdit(false);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", Util.TI18N.LOAD_NAME(),70);//发货方 
		LOAD_NAME.setCanEdit(false);
	    ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", Util.TI18N.CUSTOMER(), 60);//客户
	    CUSTOMER_NAME.setCanEdit(false);
		//ListGridField LOSDAM_FLAG = new ListGridField("LOSDAM_FLAG", Util.TI18N.LOSDAM_FLAG(),90);//货损货差 
		//orderTable.setCanEdit(false);
		ListGridField LOSDAM_FLAG = new ListGridField("LOSDAM_FLAG",Util.TI18N.LOSDAM_FLAG(),60);//货损货差
		LOSDAM_FLAG.setCanEdit(false);
		//LOSDAM_FLAG.setType(ListGridFieldType.BOOLEAN);
		ORD_IMA.setType(ListGridFieldType.ICON);
		ORD_IMA.setCellIcon(StaticRef.ICON_IMGLINK);
		ORD_IMA.setCanEdit(false);
		ORD_IMA.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				ListGridField ODR_NO = groupTable.getField("ODR_NO");
				if(ODR_NO != null) {
					new ImagePreviewWin(groupTable,StaticRef.ORDER_RECLIM_PREVIEW_URL,ODR_NO.getName()).getViewPanel();
				}
				else {
					SC.warn("列表中必须配置" + Util.TI18N.SHPM_NO() + "!");
				}
			}
			
		});
		
		groupTable.setFields(ORD_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,STATUS,LOAD_NAME,UNLOAD_NAME,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,
				UNLOAD_TIME,UNLOAD_DELAY_REASON,PRE_POD_TIME,POD_TIME,POD_DELAY_REASON,
//				TOT_QNTY,EA,ODR_TIME,UOM,LD_QNTY
				ODR_TIME,TOT_QNTY,LD_QNTY
				,UNLD_QNTY,TOT_GROSS_W,TOT_VOL,PRE_LOAD_TIME,LOAD_TIME,
				OP_POD_TIME,ORD_IMA,TRANS_SRVC_ID,LOSDAM_FLAG);
		
		final Menu menu = new Menu();   //待调右键
	    menu.setWidth(140);
	    
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    itemSeparator.setIcon(StaticRef.ICON_CANCEL);
//	    itemSeparator.setIconHeight(100);
//	    menu.addItem(itemSeparator);
	    
	    if(isPrivilege(TrsPrivRef.ODRCLAIM_P0_10)) {
	    
		    if(isPrivilege("")) {
		       MenuItem allSelect = new MenuItem("全选",StaticRef.ICON_CANCEL);
	//	       allSelect.setIcon(StaticRef.ICON_CONFIRM);
		       allSelect.addClickHandler(new AllSelectAction(groupTable));
		       menu.addItem(allSelect);
		    }
		    
	//	       MenuItem all = new MenuItem("",StaticRef.ICON_LINE);
	//	       menu.addItem(all);
		       
		       
		    if(isPrivilege("")) {
		       MenuItem unselect = new MenuItem("反选",StaticRef.ICON_CONFIRM);
		       unselect.addClickHandler(new UnSelectAction(groupTable));
		       menu.addItem(unselect);
		       
		    }
		    
		    
		    
		    if(isPrivilege(TrsPrivRef.ODRCLAIM_P0_08)) {
		       menu.addItem(itemSeparator);
		       MenuItem shipList = new MenuItem("调度相关信息",StaticRef.ICON_SEARCH);
		       shipList.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
		    
				  @Override
				  public void onClick(MenuItemClickEvent event) {
					 // TODO Auto-generated method stub
				
				     //if(transTrackWin == null){
					   
				        if(orderTable.getSelection().length == 1) {
					       transTrackWin = new TransTrackWin(order_no," ").getViewPanel();
				        }
				        else{
					       SC.warn("请选择一个托运单！");
				        }
				     
				      //
			       }
				
			    });
		       
		       menu.addItem(shipList);
		       
		    }
		    
		    if(isPrivilege(TrsPrivRef.ODRCLAIM_P0_09)) {
			    MenuItem SelectSame = new MenuItem("全选同一车",StaticRef.ICON_NEW);
			    SelectSame.addClickHandler(new OrdSelectAction(groupTable,getView()));
			    SelectSame.setKeyTitle("Ctrl+B");
			    KeyIdentifier allSelectKey = new KeyIdentifier();
			    allSelectKey.setCtrlKey(true);
			    allSelectKey.setKeyName("B");
			    
			    menu.addItem(itemSeparator);
			    menu.addItem(SelectSame);
	
		    }
			//    menu.setItems(allSelect, unselect,shipList);
		    
		    groupTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
	            public void onShowContextMenu(ShowContextMenuEvent event) {
	            	menu.showContextMenu();
	                event.cancel();
	            }
	        });
	    }
	    
	}
	private TmsOdrReceiptView getView() {
		return this;
	}
	public void onDestroy() {
		if (searchWin != null ) {
			searchWin.destroy();
			
		}
//		if (ordManageWin != null ) {
//			ordManageWin.destroy();
//		}
		if (transTrackWin != null ) {
			transTrackWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TmsOdrReceiptView view = new TmsOdrReceiptView();
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

