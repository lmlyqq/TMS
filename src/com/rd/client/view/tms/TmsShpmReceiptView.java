package com.rd.client.view.tms;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.ExplorerTreeNode;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.ImagePreviewWin;
import com.rd.client.action.tms.reclaim.ChangeShpmQntyAction;
import com.rd.client.action.tms.reclaim.ChangeShpmQntyEachAction;
import com.rd.client.action.tms.shpmreceipt.CancelEditAction;
import com.rd.client.action.tms.shpmreceipt.CancelShpmAction;
import com.rd.client.action.tms.shpmreceipt.ConfirmDamageAction;
import com.rd.client.action.tms.shpmreceipt.ConfirmShpmAction;
import com.rd.client.action.tms.shpmreceipt.NewShpmManageAction;
import com.rd.client.action.tms.shpmreceipt.SaveShpmAction;
import com.rd.client.action.tms.shpmreceipt.UploadImageAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteTransFollowAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.ShpmSelectAction;
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
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ShpmDS2;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.rd.client.ds.tms.TranLossDamageDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.reflection.ClassTools;
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
import com.smartgwt.client.types.TextMatchStyle;
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
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangedEvent;
import com.smartgwt.client.widgets.grid.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 运输服务-->作业单回单
 * @author lijun
 *
 */
@ClassForNameAble
public class TmsShpmReceiptView extends SGForm implements PanelFactory {
	private DataSource mainDS;
	private DataSource detailDS;
	private DataSource LossDamageDS;
	public SGTable headTable;
	public SGTable damageTable;
	public SGTable detailTable;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	public int hRow=0;
	public int detail_hRow = 0;
	public String shpment_no;
	public String shpm_no;
	public Record detail_record;
	public ListGridRecord[] detailTableRec;   //
	public IButton searchButton;
	public IButton confirmorderButton;
	public IButton cancelorderButton;
	public IButton canButton;	
	private TabSet mainTabSet;
//	private Map<String, String> editMap;
	
	/*public TmsShpmReceiptView(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		ToolStrip toolStrip2=new ToolStrip();
		toolStrip2.setAlign(Alignment.LEFT);
		
		mainDS = ShpmDS2.getInstance("V_SHIPMENT_HEADER", "TRANS_SHIPMENT_HEADER");
		detailDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM_QS","TRANS_SHIPMENT_ITEM");
		LossDamageDS=TranLossDamageDS.getInstance("V_LOSS_DAMAGE_","TRANS_LOSS_DAMAGE");
		
		createTable();
		createBtnWidget(toolStrip);
		section = createSection(headTable,null,true,true);
		section.setHeight("92%");
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(toolStrip);
		layout.addMember(section);
		
		Canvas[] widget = new Canvas[2];
		widget[0] = headTable;
		widget[1] = damageTable;
		
		damageTable = new SGTable(LossDamageDS,"100%","30%");//
		createBtnWidget2(toolStrip2);
		
		SectionStackSection LossDamageItem=new SectionStackSection("货损货差");
		LossDamageItem.setItems(damageTable,toolStrip2);
		LossDamageItem.setExpanded(false);
		section.addSection(LossDamageItem);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(false);
    	
    	final Menu menu = new Menu();
		menu.setWidth(140);
		
		
		
		if(isPrivilege(TrsPrivRef.SHPMCLAIM_P0_06)) {                              //wangjun 2011-3-8
		    MenuItem SelectSame = new MenuItem("全选同一车",StaticRef.ICON_SEARCH);
		    SelectSame.addClickHandler(new ShpmSelectAction(headTable,getThis()));
		    SelectSame.setKeyTitle("Ctrl+B");
		    KeyIdentifier allSelectKey = new KeyIdentifier();
		    allSelectKey.setCtrlKey(true);
		    allSelectKey.setKeyName("B");
		    menu.setItems(SelectSame);
		    headTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
				
				@Override
				public void onShowContextMenu(ShowContextMenuEvent event) {
					menu.showContextMenu();
	                event.cancel();
				}
			});
		}
    	
		createLossDamageInfo();
		
		return layout;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(16);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
		toolStrip.setAlign(Alignment.RIGHT);
		
		searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					
					searchWin =  new SearchWin(mainDS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
					searchWin.setWidth(616);
					searchWin.setHeight(354);
				} else {
					searchWin.show();
				}
				
			}
		});
		
		confirmorderButton = createBtn(StaticRef.CONFIRM_ORDER_BTN,TrsPrivRef.SHPMCLAIM_P0_01);
		confirmorderButton.addClickHandler(new ConfirmShpmAction(headTable,detailTable,this));
		
		cancelorderButton = createBtn(StaticRef.CANCEL_ORDER_BTN,TrsPrivRef.SHPMCLAIM_P0_02);
		cancelorderButton.addClickHandler(new CancelShpmAction(headTable,this));
		
		canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.SHPMCLAIM_P0_03);
		canButton.addClickHandler(new CancelEditAction(headTable,this));
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,TrsPrivRef.SHPMCLAIM_P0_04);
		expButton.addClickHandler(new ExportAction(headTable));
		
		IButton putimageButton = createBtn(StaticRef.PUT_IMAGE_BTN,TrsPrivRef.SHPMCLAIM_P0_05);
		putimageButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(headTable.getSelectedRecord() != null){
					if(headTable.getSelectedRecord().getAttribute("SHPM_NO")!= null){
						new UploadImageAction(headTable,headTable.getSelectedRecord().getAttribute("SHPM_NO"),StaticRef.SHPM_RECLIM_URL).getViewPanel().show();
					}
				} else {
					MSGUtil.sayWarning("请选择需要上传影像的客户单号.");
				}
			}
		});
		
		IButton equipButton = createBtn("设备回收",TrsPrivRef.SHPMCLAIM_P0_07);
		equipButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GpsReclaimView factory = (GpsReclaimView)ClassTools.newInstance("com.rd.client.view.tms.GpsReclaimView");
				TreeNode node = new ExplorerTreeNode("便携设备回收", "P06_T037", "P06_T03",StaticRef.ICON_NODE, factory, true, "");
				showSample(node);
				if(factory != null) {
					factory.getViewPanel();
					Criteria crit = new Criteria();
					crit.addCriteria("OP_FLAG","M");
					crit.addCriteria("LOAD_NO",headTable.getSelectedRecord().getAttribute("LOAD_NO"));
					factory.loadTable.invalidateCache();
					factory.loadTable.fetchData(crit);
				}
			}
			
		});
		
		//按钮状态
		searchButton.enable();
		confirmorderButton.disable();
		cancelorderButton.disable();
		canButton.disable();
		
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton, confirmorderButton, cancelorderButton,
				canButton, expButton, putimageButton,equipButton);
		
		

	}
	
	private void createTable() {
		headTable = new SGTable(mainDS, "100%", "92%", false, true, false) {
			
			@Override  
            protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {   
                if (colNum == 22) {   
                    return "cursor:pointer";
                } 
                else {   
                    return super.getCellCSSText(record, rowNum, colNum);   
                }  
            }
			
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				VLayout layout = new VLayout();
				detailTable = new SGTable(detailDS, "100%",
						"50", false, true, false);
				detailTable.setCanEdit(true);
				
				detailTable.setAlign(Alignment.RIGHT);
				detailTable.setShowRowNumbers(false);
				detailTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);
				detailTable.setAutoFitData(Autofit.VERTICAL);

				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
				
				
				ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),50);
				SHPM_ROW.setCanEdit(false);
				ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),90);
				SKU_ID.setCanEdit(false);
				ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),100);
				SKU_NAME.setCanEdit(false);
				ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),70);
				SKU_SPEC.setCanEdit(false);
				ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),40);
				UOM.setCanEdit(false);
//				ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),60);
//				Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
//				QNTY.setCanEdit(false);
//				QNTY.setHidden(true);
//				
				ListGridField ODR_QNTY = new ListGridField("QNTY",Util.TI18N.ODR_QNTY(),70);
				Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
				ODR_QNTY.setCanEdit(false);
//				
				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.FOLLOW_LD_QNTY(),70);
				Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
				LD_QNTY.setCanEdit(false);
//				
				ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),70);
				Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
				ListGridField UNLD_QNTY_EACH = new ListGridField("UNLD_QNTY_EACH","最小单位收货数量",100);
				Util.initFloatListField(UNLD_QNTY_EACH, StaticRef.QNTY_FLOAT);
				
				ListGridField LOSDAM_QNTY = new ListGridField("LOSDAM_QNTY",Util.TI18N.LOSDAM_QNTY(),70);
				ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),70);
				ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
				ListGridField LOTATT02 = new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);
				LOTATT02.setCanEdit(false);
				ListGridField LOT_ID  = new ListGridField("LOT_ID",Util.TI18N.LOT_ID(),50);
				LOT_ID.setCanEdit(false);
				
				ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),50);
				TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
				TOT_QNTY_EACH.setCanEdit(false);
				Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
//				ListGridField UNLD_VOL = new ListGridField("UNLD_VOL",Util.TI18N.UNLD_VOL(),70);
//				Util.initFloatListField(UNLD_VOL, StaticRef.VOL_FLOAT);
//				
//				ListGridField UNLD_GWGT = new ListGridField("UNLD_GWGT",Util.TI18N.UNLD_GWGT(),70);
//				Util.initFloatListField(UNLD_GWGT, StaticRef.GWT_FLOAT);
//				
//				ListGridField UNLD_NWGT = new ListGridField("UNLD_NWGT",Util.TI18N.UNLD_NWGT(),70);
//				Util.initFloatListField(UNLD_NWGT, StaticRef.GWT_FLOAT);
//				UNLD_NWGT.setHidden(true);
//				
//				ListGridField UNLD_WORTH = new ListGridField("UNLD_WORTH",Util.TI18N.UNLD_WORTH(),70);
//				Util.initFloatListField(UNLD_WORTH, StaticRef.PRICE_FLOAT);
//				UNLD_WORTH.setHidden(true);
				
				detailTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,TOT_QNTY_EACH,LD_QNTY,
						UNLD_QNTY,UNLD_QNTY_EACH,LOSDAM_QNTY,VOL,G_WGT,LOT_ID,LOTATT02);
				detailTable.fetchData(findValues);
                
                detailTable.fetchData(findValues, new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						detailTableRec = detailTable.getRecords();
					}
				});
                detailTable.addEditorExitHandler(new EditorExitHandler() {
					
					@Override
					public void onEditorExit(EditorExitEvent event) {
						detail_hRow = detailTable.getRecordIndex(event.getRecord());
						detail_record = event.getRecord();
						
					}
				});
                
                UNLD_QNTY.addEditorExitHandler(new ChangeShpmQntyAction(detailTable));
                
                UNLD_QNTY_EACH.addEditorExitHandler(new ChangeShpmQntyEachAction(detailTable));
                
                layout.addMember(detailTable);
                layout.setLayoutLeftMargin(5);
				return layout;
			}
		};
		headTable.setCanExpandRecords(true);
		headTable.setShowRowNumbers(true);
//		headTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		
        getConfigList(headTable);
        final Menu menu = new Menu();
	    menu.setWidth(140);
		
	    //右键功能【全选】【反选】
//	    if(isPrivilege(TrsPrivRef.SHPMCLAIM_P0_06)) {
//	       MenuItem allSelect = new MenuItem("全选");
//	       allSelect.addClickHandler(new AllSelectAction(headTable));
//	       menu.addItem(allSelect);
//	    }
//	    if(isPrivilege(TrsPrivRef.SHPMCLAIM_P0_07)) {
//	       MenuItem unselect = new MenuItem("反选");
//	       unselect.addClickHandler(new UnSelectAction(headTable));
//	       menu.addItem(unselect);
//	    }
////	    menu.setItems(allSelect,unselect);
//	    headTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
//            public void onShowContextMenu(ShowContextMenuEvent event) {
//            	menu.showContextMenu();
//                event.cancel();
//            }
//        });
	}
	private void getConfigList(final SGTable table) {
		
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_SHIPMENT_HEADER作业单回单");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_RECLAIM);
		createListField(table, listMap);
		
		ListGridField TOT_QNTY  = table.getField("TOT_QNTY");
		if(TOT_QNTY != null){
			Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		}
		ListGridField TOT_GROSS_W = table.getField("TOT_GROSS_W");
		if(TOT_GROSS_W != null){
			Util.initFloatListField(TOT_GROSS_W, StaticRef.GWT_FLOAT);
		}
		ListGridField TOT_VOL = table.getField("TOT_VOL");
		if(TOT_VOL != null){
			Util.initFloatListField(TOT_VOL, StaticRef.VOL_FLOAT);
		}
		
		ListGridField SIGNATARY=table.getField("SIGNATARY");
		if(SIGNATARY != null){
			SIGNATARY.setCanEdit(true);
		}
		
		ListGridField POD_TIME = table.getField("POD_TIME");
		if(POD_TIME != null) {
			Util.initDateTime(headTable,POD_TIME);
			POD_TIME.setCanEdit(true);
			POD_TIME.setTitle(ColorUtil.getRedTitle(POD_TIME.getTitle()));
		}
		else {
			SC.warn("列表中必须配置" + Util.TI18N.POD_TIME() + "!");
		}
		
		ListGridField POD_DELAY_REASON = table.getField("POD_DELAY_REASON");
		if(POD_DELAY_REASON != null) {
			POD_DELAY_REASON.setTitle(POD_DELAY_REASON.getTitle());
			POD_DELAY_REASON.setCanEdit(true);
			Util.initCodesComboValue(POD_DELAY_REASON, "POD_DELAY_REASON");	
		}
		else {
			SC.warn("列表中必须配置" + Util.TI18N.POD_DELAY_REASON() + "!");
		}
		
		ListGridField UNLOAD_TIME = table.getField("UNLOAD_TIME");
		if(UNLOAD_TIME != null) {
			Util.initDateTime(headTable,UNLOAD_TIME);
			UNLOAD_TIME.setCanEdit(true);
			UNLOAD_TIME.setTitle(ColorUtil.getRedTitle(UNLOAD_TIME.getTitle()));
		}
		else {
			SC.warn("列表中必须配置" + Util.TI18N.UNLOAD_TIME() + "!");
		}
		
		ListGridField UNLOAD_DELAY_REASON = table.getField("UNLOAD_DELAY_REASON");
		if(UNLOAD_DELAY_REASON != null) {
			Util.initCodesComboValue(UNLOAD_DELAY_REASON, "UNLOAD_DELAY_REASON");
			UNLOAD_DELAY_REASON.setCanEdit(true);
			UNLOAD_DELAY_REASON.setTitle(UNLOAD_DELAY_REASON.getTitle());
		}
		else {
			SC.warn("列表中必须配置" + Util.TI18N.UNLOAD_DELAY_REASON() + "!");
		}
		
		ListGridField ORD_IMA = table.getField("ORD_IMA");//回单影像???????
		if(ORD_IMA != null) {
			ORD_IMA.setType(ListGridFieldType.ICON);
			ORD_IMA.setCellIcon(StaticRef.ICON_IMGLINK);
			ORD_IMA.addRecordClickHandler(new RecordClickHandler() {
	
				@Override
				public void onRecordClick(RecordClickEvent event) {
					ListGridField SHPM_NO = table.getField("SHPM_NO");
					if(SHPM_NO != null) {
						new ImagePreviewWin(headTable,StaticRef.SHPM_RECLIM_PREVIEW_URL,SHPM_NO.getName()).getViewPanel();
					}
					else {
						SC.warn("列表中必须配置" + Util.TI18N.SHPM_NO() + "!");
					}
				}
				
			});
		}
		else {
			SC.warn("列表中必须配置" + Util.TI18N.ORD_IMA() + "!");
		}
//		table.addEditorExitHandler(new EditorExitHandler() {
//			
//			@SuppressWarnings("unchecked")
//			@Override
//			public void onEditorExit(EditorExitEvent event) {
//				editMap = table.getEditValues(hRow);
//				if(editMap != null){
//					canButton.enable();
//				}
//			}
//		});
	    table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				hRow = event.getRecordNum();
	//			Record shpm_record = event.getRecord();
				shpm_no = table.getSelectedRecord().getAttribute("SHPM_NO").toString();
//				shpment_no = shpm_no;
			}
		});
	    
	    table.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				  String cur_time = Util.getCurTime();
				  ListGridRecord rec = table.getSelectedRecord();
				  int row = table.getRecordIndex(rec);//MAX_UNLOAD_TIME 2010-12-12
				  table.setEditValue(row, "POD_TIME" ,cur_time);
				  
//				  canButton.enable();
			}
		});
	    
	    table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				// TODO Auto-generated method stub
				if (event.getRecord() == null) {
					return;
				}
				Record record = event.getRecord();
				Record record_ = table.getEditedRecord(table.getRecordIndex(record));
				if(ObjUtil.isNotNull(record_.getAttribute("POD_TIME"))||ObjUtil.isNotNull(record_.getAttribute("POD_DELAY_REASON"))
//						||ObjUtil.isNotNull(record_.getAttribute("UNLOAD_TIME"))
						||ObjUtil.isNotNull(record_.getAttribute("UNLOAD_DELAY_REASON"))){
//					canButton.enable();
					 setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_03,cancelorderButton,true);
					
				} else {
					
//					canButton.disable();
					 setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_03,cancelorderButton,false);
					 
				}
				ListGridRecord [] gridRecord = table.getSelection();
//				record.getAttribute("STATUS");
				if(gridRecord.length == 1){
					if(StaticRef.SHPM_RECEIPT.equals(record.getAttribute("STATUS"))){
//						confirmorderButton.disable(); wangjun 2011-3-8
//						cancelorderButton.enable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,false);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,true);
						
					}else if(StaticRef.SHPM_UNLOAD.equals(record.getAttribute("STATUS"))){
//						confirmorderButton.enable();
//						cancelorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,true);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,false);
						
					} else {
//						confirmorderButton.disable();
//						cancelorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,false);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,false);
						
					}
				}else if(gridRecord.length > 1) {
					StringBuffer sf1 = new StringBuffer();
					StringBuffer sf2 = new StringBuffer();
					int length = gridRecord.length-1;
					if(!record.getAttribute("LOAD_NO").equals(gridRecord[0].getAttribute("LOAD_NO"))
						&&record.getAttribute("LOAD_NO").equals(gridRecord[length].getAttribute("LOAD_NO"))	){
						MSGUtil.sayError("必须选择同一车的作业单！ ");
						table.deselectRecord(record);
						return;
					}
					for(int i = 0 ; i < gridRecord.length ; i++){
						
						if(StaticRef.SHPM_RECEIPT.equals(gridRecord[i].getAttribute("STATUS"))){
							sf1.append(gridRecord[i].getAttribute("STATUS"));
						} else if(StaticRef.SHPM_UNLOAD.equals(gridRecord[i].getAttribute("STATUS"))){
							sf2.append(gridRecord[i].getAttribute("STATUS"));
						}
					}
					if(sf1.length()>1&&sf2.length() == 0){
//						cancelorderButton.enable();
//						confirmorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,false);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,true);
					}
					if(sf2.length()>1&&sf1.length() == 0){
//						confirmorderButton.enable();
//						cancelorderButton.disable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,true);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,false);
					}
					if(sf2.length()>1&&sf1.length() > 0){
//						confirmorderButton.enable();
//						cancelorderButton.enable();
						
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_01,confirmorderButton,true);
						setButtonEnabled(TrsPrivRef.SHPMCLAIM_P0_02,cancelorderButton,true);
						
					}
				}				
				
				shpment_no = event.getRecord().getAttributeAsString("SHPM_NO");
				table.OP_FLAG = "M";

				Criteria criteria = new Criteria();
				criteria.addCriteria("OP_FLAG", "M");
				criteria.addCriteria("SHPM_NO", shpment_no);
				damageTable.setCriteria(criteria);
				damageTable.setFilterEditorCriteria(criteria);
				
				damageTable.fetchData(criteria);
				
			}
		});
     
	}
	
	private void createBtnWidget2(ToolStrip toolStrip2) {
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		toolStrip2.setMembersMargin(4);
		toolStrip2.setAlign(Alignment.LEFT);
		
		//新增按钮
	    IButton newButton = createBtn(StaticRef.CREATE_BTN,TrsPrivRef.SHPMCLAIM_P1_01);
        newButton.addClickHandler(new NewShpmManageAction(this,damageTable));
        
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.SHPMCLAIM_P1_02);
       //saveButton.addClickHandler(new SaveManageAction(damageTable,check_map));
        saveButton.addClickHandler(new SaveShpmAction(damageTable,check_map,this));
        
        //删除按钮
        IButton delButton = createBtn(StaticRef.DELETE_BTN,TrsPrivRef.SHPMCLAIM_P1_03);
        delButton.addClickHandler(new DeleteTransFollowAction(damageTable,this));
        
        //取消按钮
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.SHPMCLAIM_P1_04);
        canButton.addClickHandler(new CancelAction(damageTable,this));
        
        //确认按钮
        IButton cfmButton = createBtn(StaticRef.CONFIRM_BTN,TrsPrivRef.SHPMCLAIM_P1_05);
        cfmButton.setTitle("确认");
        cfmButton.addClickHandler(new ConfirmDamageAction(damageTable,headTable));
        
        toolStrip2.setMembersMargin(4);
        toolStrip2.setMembers(newButton, saveButton, delButton, canButton,cfmButton);
  
        add_map.put(TrsPrivRef.SHPMCLAIM_P1_01, newButton);
        del_map.put(TrsPrivRef.SHPMCLAIM_P1_03, delButton);
        save_map.put(TrsPrivRef.SHPMCLAIM_P1_02, saveButton);
        save_map.put(TrsPrivRef.SHPMCLAIM_P1_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
	}
	private void createLossDamageInfo() {		
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
		damageTable.setDataSource(LossDamageDS);

		ListGridField ODR_NO = new ListGridField("ODR_NO", "",0);//货品代码
		ODR_NO.setHidden(true);
		ListGridField SKU_ID = new ListGridField("SKU_ID",Util.TI18N.SKU(),70);//货品代码
		SKU_ID.setHidden(true);
		
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),130);//货品名称
		SKU_NAME.setCanEdit(true);
		SKU_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SKU_NAME()));
		
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),80);//货品规格型号
		SKU_SPEC.setCanEdit(true);
		
		final ListGridField DAMA_TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.UOM(),60);
		DAMA_TRANS_UOM.setCanEdit(true); //DAMA_TRANS_UOM
		DAMA_TRANS_UOM.setTitle(ColorUtil.getRedTitle(Util.TI18N.UOM()));
		
		final ListGridField PACK_ID = new ListGridField("PACK_ID",Util.TI18N.PACK_ID(),80);
		PACK_ID.setCanEdit(true); //包装
		PACK_ID.setAlign(Alignment.LEFT);  
		
		final ListGridField TRANS_QNTY = new ListGridField("QNTY",Util.TI18N.TRANS_QNTY(),70);
		TRANS_QNTY.setCanEdit(true);
		TRANS_QNTY.setAlign(Alignment.RIGHT);//货损数量 
//		TRANS_QNTY.setTitle(ColorUtil.getRedTitle(Util.TI18N.TRANS_QNTY()));
		Util.initFloatListField(TRANS_QNTY, StaticRef.QNTY_FLOAT);
		
		final ListGridField LOSS_DAMAGE_TYP = new ListGridField("LOSS_DAMAGE_TYP",Util.TI18N.LOSS_DAMAGE_TYP(),70);
		LOSS_DAMAGE_TYP.setCanEdit(true);
		Util.initCodesComboValue(LOSS_DAMAGE_TYP, "LOSS_DAMAGE_TYP");	
		
		final ListGridField REASON = new ListGridField("REASON",Util.TI18N.REASON(),250);
		REASON.setCanEdit(true);
		REASON.setAlign(Alignment.LEFT);//  原因描述
		
		final ListGridField AMOUNT = new ListGridField("AMOUNT",Util.TI18N.TRANS_AMOUNT(),80);
		AMOUNT.setCanEdit(true);
		AMOUNT.setAlign(Alignment.RIGHT);//  残损金额  
		Util.initFloatListField(AMOUNT, StaticRef.PRICE_FLOAT);
		
		final ListGridField DUTYER = new ListGridField("DUTYER",Util.TI18N.DUTYER(),75);
		DUTYER.setCanEdit(true);
		DUTYER.setAlign(Alignment.LEFT);//  责任人
		
		ListGridField COMPANY_ACOUNT = new ListGridField("COMPANY_ACOUNT","公司承担金额",95);
		COMPANY_ACOUNT.setCanEdit(true);
		
		ListGridField DRIVER_ACOUNT = new ListGridField("DRIVER_ACOUNT","司机承担金额",95);
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
					if(event.getRecord()!=null){
						valList.add(ObjUtil.ifNull(event.getRecord().getAttribute("SKU_ID"),""));
					}else{
						valList.add("");
					}
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
		
		PACK_ID.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				final int row = event.getRowNum();
				String pack_id = ObjUtil.ifObjNull(event.getValue(),"").toString();
				if(ObjUtil.isNotNull(pack_id)) {
					pack_id =" where id='" +pack_id +"'";
					DAMA_TRANS_UOM.setValueMap("");
					Util.async.getComboValue("V_BAS_PACKAGE", "UOM", "DESCR", pack_id,"", new AsyncCallback<LinkedHashMap<String, String>>() {
						
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
					new SkuWin(damageTable,itemRow,"40%", "38%",shpment_no).getViewPanel();
			}
		});
		Util.initComboValue(PACK_ID, "BAS_PACKAGE", "ID", "PACK", "", "");
//		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where 1=1", "");
		Util.initComboValue(DAMA_TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM", " where pack='STANDARD'", "");

	
	}
	
	public DynamicForm createSerchForm(SGPanel form){
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
//		form.setAlign(Alignment.LEFT);
//		form.setNumCols(8);
//		form.setHeight100();
//		form.setWidth100();
		form.setCellPadding(2);
		
		SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),true);
		Util.initComboValue(CUSTOMER_NAME, "BAS_CUSTOMER", "ID", "CUSTOMER_CNAME", " where CUSTOMER_FLAG='Y'");
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
		SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		TextItem SIGN_ORG_ID = new TextItem("SIGN_ORG_ID", "");
		SIGN_ORG_ID.setVisible(false);
		SIGN_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem SIGN_ORG_ID_NAME = new TextItem("SIGN_ORG_ID_NAME", Util.TI18N.SIGN_ORG_ID());
		SIGN_ORG_ID_NAME.setColSpan(2);
		SIGN_ORG_ID_NAME.setWidth(FormUtil.Width);
		SIGN_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(SIGN_ORG_ID_NAME, SIGN_ORG_ID, false, "50%", "40%");
		SIGN_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		SGText LOAD_NO = new SGText("LOAD_NO",Util.TI18N.LOAD_NO(),true);
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_NAME",Util.TI18N.SUPLR_NAME());
		Util.initComboValue(SUPLR_NAME, "BAS_SUPPLIER", "ID","SUPLR_CNAME","");
		
		SGText PLATE_NO = new SGText("PLATE_NO",Util.TI18N.PLATE_NO());
		
		SGCombo SHPM_STATUS = new SGCombo("SHPM_STATUS",Util.TI18N.SHPM_STSTUS());
		Util.initStatus(SHPM_STATUS, StaticRef.SHPMNO_STAT,StaticRef.SHPM_UNLOAD);
		
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());//发货区域
		LOAD_AREA_NAME.setColSpan(2);
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(LOAD_AREA_NAME, null);
		LOAD_AREA_NAME.setStartRow(false);
		LOAD_AREA_NAME.setWidth(FormUtil.Width);
		
		SGLText LOAD_NAME = new SGLText("LOAD_NAME", Util.TI18N.LOAD_NAME());//发货方
		
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());//收货区域
		UNLOAD_AREA_NAME.setColSpan(2);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(UNLOAD_AREA_NAME, null);
		UNLOAD_AREA_NAME.setStartRow(false);
		UNLOAD_AREA_NAME.setWidth(FormUtil.Width);
		
		SGLText UNLOAD_NAME = new SGLText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		
		SGText ODR_TIME_FROM = new SGText("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
		SGText ODR_TIME_TO = new SGText("ODR_TIME_TO", "到");//订单时间
//		ODR_TIME_FROM.setWidth(128);
//		ODR_TIME_TO.setWidth(128);
		Util.initDate(searchForm, ODR_TIME_FROM);
		Util.initDate(searchForm,ODR_TIME_TO);
		
		SGText PRE_POD_TIME_FROM = new SGText("PRE_POD_TIME_FROM", Util.TI18N.FROM_POD_TIME());
		SGText PRE_POD_TIME_TO = new SGText("PRE_POD_TIME_TO", "到");//计划回单时间
//		PRE_POD_TIME_FROM.setWidth(128);
//		PRE_POD_TIME_TO.setWidth(128);
		Util.initDate(searchForm, PRE_POD_TIME_FROM);
		Util.initDate(searchForm,PRE_POD_TIME_TO);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setValue(true);
		C_ORG_FLAG.setColSpan(2);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构
		C_RDC_FLAG.setColSpan(2);

		SGCheck POD_FLAG = new SGCheck("POD_FLAG","");
		POD_FLAG.setValue(true);
		POD_FLAG.setVisible(false);
		
		SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		HISTORY_FLAG.setVisible(false);
		
	    SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());//托运单号
	    SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
		
//	   << Test >>
//	    form.setItems(CUSTOMER_NAME,CUSTOM_ODR_NO,SHPM_NO,SIGN_ORG_ID_NAME,LOAD_NO,SUPLR_NAME,PLATE_NO,SHPM_STATUS,
//				LOAD_AREA_NAME,LOAD_NAME,ODR_NO,UNLOAD_AREA_NAME,UNLOAD_NAME,REFENENCE1,SIGN_ORG_ID
//				,ODR_TIME_FROM,ODR_TIME_TO,PRE_POD_TIME_FROM,PRE_POD_TIME_TO,C_ORG_FLAG,C_RDC_FLAG,POD_FLAG,HISTORY_FLAG);
//		return form;
	    
	    SGCombo UPLOAD_FLAG=new SGCombo("UPLOAD_FLAG","上传标记");
		LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("N", "未上传");
		map.put("Y", "已上传");
		UPLOAD_FLAG.setValueMap(map);
		
		SGCheck LOSDAM_FLAG=new SGCheck("LOSDAM_FLAG", "货损货差");
	    	
	    form.setItems(CUSTOMER_NAME,CUSTOM_ODR_NO,SHPM_NO,SIGN_ORG_ID_NAME,LOAD_NO,SUPLR_NAME,PLATE_NO,SHPM_STATUS,
	    		ODR_NO,REFENENCE1,LOAD_AREA_NAME,UNLOAD_AREA_NAME,LOAD_NAME,UNLOAD_NAME,
	    		SIGN_ORG_ID,ODR_TIME_FROM,ODR_TIME_TO,PRE_POD_TIME_FROM,PRE_POD_TIME_TO,UPLOAD_FLAG,C_ORG_FLAG,C_RDC_FLAG,POD_FLAG,HISTORY_FLAG,LOSDAM_FLAG);
		return form;
		
	}
	
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	public TmsShpmReceiptView getThis(){
		return this;
	}
	@Override
	public void initVerify() {
		check_map.put("TABLE","TRANS_LOSS_DAMAGE");
		check_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		check_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.TRANS_QNTY());

	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}

	}

	@Override
	public Canvas createCanvas(String id, TabSet tabSet) {
		TmsShpmReceiptView view = new TmsShpmReceiptView();
		view.setFUNCID(id);
		view.setTabSet(tabSet);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		System.out.println(getID());
		return getID();
	}
	
	protected void setTabSet(TabSet tabSet) {
		this.mainTabSet = tabSet;
	}
	
	protected TabSet getTabSet() {
		return this.mainTabSet;
	}
	

    protected void showSample(TreeNode node) {
         boolean isExplorerTreeNode = node instanceof ExplorerTreeNode;
        if (isExplorerTreeNode) {
            ExplorerTreeNode explorerTreeNode = (ExplorerTreeNode) node;
            PanelFactory factory = explorerTreeNode.getFactory();
            if (factory != null) {
                String panelID = factory.getCanvasID();
                Tab tab = null;
                if (panelID != null) {
                    String tabID = panelID + "_tab";
                    tab = mainTabSet.getTab(tabID);
                }
                if (tab == null) {
                	final GpsReclaimView panel =(GpsReclaimView)factory.createCanvas(explorerTreeNode.getNodeID(), mainTabSet);
                    tab = new Tab();
                    tab.setID(factory.getCanvasID() + "_tab");
                    tab.setAttribute("historyToken", explorerTreeNode.getNodeID());
                    //tab.setContextMenu(contextMenu);

                    String sampleName = explorerTreeNode.getName();

                    String icon = explorerTreeNode.getIcon();
                    if (icon == null) {
                        icon = "silk/plugin.png";
                    }
                    String imgHTML = Canvas.imgHTML(icon, 16, 16);
                    tab.setTitle("<span>" + imgHTML + "&nbsp;" + sampleName + "</span>");
                    tab.setPane(panel);
                    tab.setCanClose(true);
                    mainTabSet.addTab(tab);
                    mainTabSet.selectTab(tab);
                    Criteria cri=new Criteria();
                    cri.addCriteria("OP_FLAG","M");
                    cri.addCriteria("LOAD_NO",headTable.getSelectedRecord().getAttribute("LOAD_NO") );
                    panel.loadTable.fetchData(cri, new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							JavaScriptObject jsobject = panel.loadSectionStack.getSection(0).getAttributeAsJavaScriptObject("controls");
							Canvas[] canvas = null;
							if(jsobject != null) {
								canvas = Canvas.convertToCanvasArray(jsobject);
							}
							else {
								canvas = new Canvas[1];
							}
							for(int i = 0; i < canvas.length; i++) {
								if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
									DynamicForm pageForm = (DynamicForm)canvas[i];
									if(pageForm != null) {
										pageForm.getField("CUR_PAGE").setValue("1");
										pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
										pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
										String sqlwhere = Cookies.getCookie("SQLWHERE");
										String key = Cookies.getCookie("SQLFIELD1");
										String value = Cookies.getCookie("SQLFIELD2");
										String alias = Cookies.getCookie("SQLALIAS");
										if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
											 panel.loadTable.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
											 panel.loadTable.setProperty("SQLFIELD1", key);
											 panel.loadTable.setProperty("SQLFIELD2", value);
											 panel.loadTable.setProperty("SQLALIAS", alias);
										}
									}
									break;
								}
							}
						}
					});
                } else {
                    mainTabSet.selectTab(tab);
                }
            }
        }
    }
}