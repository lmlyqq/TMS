package com.rd.client.view.tms;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.SelfConCloseHeadAction;
import com.rd.client.action.tms.SelfConPayoutWin;
import com.rd.client.action.tms.dispatch.CancelJourneySplitAction;
import com.rd.client.action.tms.dispatch.CancelSplitAction;
import com.rd.client.action.tms.dispatch.ChangedQntyAction;
import com.rd.client.action.tms.dispatch.ExportShpmItemAction;
import com.rd.client.action.tms.dispatch.SelfConRefreshTable;
import com.rd.client.action.tms.dispatch.SelfConShpmSplitWin;
import com.rd.client.common.action.AllSelectAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.UnSelectAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ShpmDS4;
import com.rd.client.ds.tms.ShpmDetailDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.CountWin;
import com.rd.client.win.ModifyShpmWin;
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
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
@ClassForNameAble
public class TmsSelfControl extends SGForm implements PanelFactory {
	private DataSource mainDS;
	private DataSource detailDS;
	private SGTable headTable;
	private SGTable detailTable;
	private SectionStack section;
	private Window searchWin;
	public SGPanel searchForm = new SGPanel();
	private ListGridRecord [] record;
	@SuppressWarnings("unused")
	private Window countWin;
	public IButton payoutButton;
	//public IButton freezeButton;
	//public IButton freeButton;
	public IButton closeButton;
	public ListGridRecord[] unshpmlstRec;   
	public SelfConRefreshTable refreshTableAction;

	/*public TmsSelfControl(String id) {
	    super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		mainDS = ShpmDS4.getInstance("V_SHIPMENT_HEADER4", "TRANS_SHIPMENT_HEADER");
		detailDS = ShpmDetailDS.getInstance("V_SHIPMENT_ITEM_SF","TRANS_SHIPMENT_ITEM");
		
		createTable();
		createBtnWidget(toolStrip);
		section = createSection(headTable,null,true,true);
		section.setHeight("92%");
//		section.setTitle("作业单信息");
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(toolStrip);
		layout.addMember(section);
//		layout.setMembers(section);
		
		initVerify();
		return layout;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(16);
		toolStrip.addSeparator();
        
		//查询
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchWin = new SearchWin(mainDS, createSerchForm(searchForm),section.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
				    searchWin.setHeight(400);
//				    if(!(searchForm == null && searchForm.getJsObj() == null)){
//				    	JSOHelper.setAttribute(searchForm.getJsObj(), "parentView", "TmsShipmentView");
//				    }
				}else{
					searchWin.show();
				}
			}
		});
		
		//派发
		payoutButton = createBtn(StaticRef.PAYOUT_BTN,TrsPrivRef.SLFCON_P1_01);
//		payoutButton.addClickHandler(new PayoutShpmentWin(headTable,true));
		payoutButton.setVisible(false);
		payoutButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(headTable.getSelection().length > 0){
				     new SelfConPayoutWin(headTable, true,getThis()).getViewPanel().show();
					
				} else {
					MSGUtil.sayWarning("请选择作业单，再执行派发！");
				}
				
			}
		});
		
		closeButton = createBtn(StaticRef.CLOSE_BTN,TrsPrivRef.SLFCON_P1_04);
		closeButton.setVisible(false);
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				record = headTable.getSelection();	
				if(ObjUtil.isNotNull(record) && record.length > 0) {  //yuanlei 2011-2-14
				//if(record.length > 0 && record != null && record.toString().trim().length()>0){
					new SelfConCloseHeadAction(headTable,record,getThis()).getviewPanel().show();

				} else {
					MSGUtil.sayWarning("请勾选作业单后再执行关闭！");
				}
			}
		});
		
		//freeButton.disable();
		//freezeButton.disable();
		closeButton.disable();
		payoutButton.disable();
		
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton, payoutButton, closeButton);

	}

	private void createTable() {
		headTable = new SGTable(mainDS, "100%", "92%", false, true, false) {
			
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				VLayout layout = new VLayout();
				
				detailTable = new SGTable();
				detailTable.setDataSource(detailDS);
				detailTable.setWidth("35%");
				detailTable.setHeight(50);
				detailTable.setShowFilterEditor(false);
				detailTable.setShowAllRecords(true);
                detailTable.setAutoFetchData(false);
                detailTable.setAutoFitData(Autofit.VERTICAL);
                

				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
				ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),50);
				SHPM_ROW.setCanEdit(false);
				ListGridField SKU_ID = new ListGridField("SKU_CODE",Util.TI18N.SKU_ID(),60);
				SKU_ID.setCanEdit(false);
				ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),60);
				SKU_NAME.setCanEdit(false);
				ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1_NAME",Util.TI18N.TEMPERATURE(),60);
				TEMPERATURE1.setCanEdit(false);
				ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),30);
				UOM.setCanEdit(false);
				ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),50);
				Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
				QNTY.addEditorExitHandler(new ChangedQntyAction(detailTable, getThis()));
				
				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
				LD_QNTY.setCanEdit(false);
				Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
				
				ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
				Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
				UNLD_QNTY.setCanEdit(false);
				
				ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),50);
				Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
				G_WGT.setCanEdit(false);
				
				ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
				Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
				VOL.setCanEdit(false);
				
				detailTable.setFields(SHPM_ROW,SKU_ID,SKU_NAME,TEMPERATURE1,UOM,QNTY,LD_QNTY,UNLD_QNTY,G_WGT,VOL);
				detailTable.fetchData(findValues,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						unshpmlstRec = detailTable.getRecords();
					}
				});
                layout.addMember(detailTable);
                layout.setLayoutLeftMargin(38);
				return layout;
			}
		};
		headTable.setCanExpandRecords(true);
		headTable.setCanEdit(false);
		headTable.setShowRowNumbers(true);
//		headTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		
        getConfigList(headTable);
        
        
        headTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record = event.getRecord();
//				record.getAttribute("STATUS");

				if(StaticRef.SHPM_CONFIRM.equals(record.getAttribute("STATUS"))){
//					freeButton.disable();
//					freezeButton.enable();
//					closeButton.enable();
//					payoutButton.enable();
					
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_03,freeButton,false);//wangjun 2011-3-8
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_02,freezeButton,true);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_04,closeButton,true);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_01,payoutButton,true);

				}
				else if(StaticRef.SHPM_FROZEN.equals(record.getAttribute("STATUS"))){
//					freeButton.enable();
//					freezeButton.disable();
//					closeButton.disable();
//					payoutButton.disable();
					
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_03,freeButton,true);//wangjun 2011-3-8
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_02,freezeButton,false);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_04,closeButton,false);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_01,payoutButton,false);
				}
				else {
//					freeButton.disable();
//					freezeButton.disable();
//					closeButton.disable();
//					payoutButton.disable();
					
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_03,freeButton,false);//wangjun 2011-3-8
					//setButtonEnabled(TrsPrivRef.SLFCON_P1_02,freezeButton,false);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_04,closeButton,false);
					setButtonEnabled(TrsPrivRef.SLFCON_P1_01,payoutButton,false);
				}
			}
		});
        
        final Menu menu = new Menu();
	    menu.setWidth(140);
	    MenuItemSeparator itemSeparator =new MenuItemSeparator();
	    
//	    getQuickKey();
	    
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_10)) {                         //wangjun 2011-5-3
	    	MenuItem split = new MenuItem("拆分",StaticRef.ICON_CONFIRM);
	    	split.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					if(headTable.getSelectedRecord().getAttribute("ODR_TYP").equals(StaticRef.ODR_TYP_DB)){
						SC.warn("调拨单不允许拆分！");
						return;
					}
					if(headTable.getSelectedRecord().getAttribute("PRINT_FLAG").equals("N")){
						
						SC.warn("作业单"+headTable.getSelectedRecord().getAttribute("SHPM_NO")+" 已打印提货单不允许拆分！");
						return;
					}
					if(detailTable != null && detailTable.getSelection().length > 0){
						if(isSplitValid()) {
							new SelfConShpmSplitWin(detailTable,headTable,getThis()).getViewPanel().show();
						}
						
					} else {
						SC.warn("未勾选作业单明细!");
					}		
				}
			});
	    	menu.addItem(split);
	    	
	    	refreshTableAction = new SelfConRefreshTable(headTable, getThis());
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_11)) {
		    MenuItem cansplitItem = new MenuItem(Util.BI18N.CANCELSPLIT(),StaticRef.ICON_CANCEL); 
		    cansplitItem.setKeyTitle("Alt+C");
		    KeyIdentifier cansplitKey = new KeyIdentifier();
		    cansplitKey.setAltKey(true);
		    cansplitKey.setKeyName("C");
		    cansplitItem.setKeys(cansplitKey);
		    menu.addItem(cansplitItem);
		    cansplitItem.addClickHandler(new CancelSplitAction(getThis(), headTable));
		    
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_12)) {                         //wangjun 2011-5-3
	    	MenuItem jrny_split = new MenuItem("行程拆分",StaticRef.ICON_CONFIRM);
	    	jrny_split.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				
				@Override
				public void onClick(MenuItemClickEvent event) {
					/*if(headTable != null && headTable.getSelectedRecord().getAttribute("PRINT_FLAG").equals("N")){
						
						SC.warn("作业单"+headTable.getSelectedRecord().getAttribute("SHPM_NO")+" 已打印提货单不允许拆分！");
						return;
					}*/
					if(headTable != null && headTable.getSelection().length > 0){
						//弹出窗口
						new JourneySplitWin(getThis(),headTable.getSelection());
						
					} else {
						SC.warn("请选择作业单!");
					}		
				}
			});
	    	menu.addItem(jrny_split);
	    	
	    	refreshTableAction = new SelfConRefreshTable(headTable, getThis());
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_13)) {
		    MenuItem canjrnySplitItem = new MenuItem("取消行程拆分",StaticRef.ICON_CANCEL); 
		    menu.addItem(canjrnySplitItem);
		    canjrnySplitItem.addClickHandler(new CancelJourneySplitAction(getThis(), headTable));	    
	    }
	    
	    /*if(isPrivilege(TrsPrivRef.SLFCON_P1_16)) { 
		    MenuItem unionItem = new MenuItem("订单合并",StaticRef.ICON_CONFIRM); 
		    menu.addItem(unionItem);
		    unionItem.addClickHandler(new ShpmUnionAction(getThis(), headTable));	    
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_17)) { 
		    MenuItem unionItem = new MenuItem("取消合并",StaticRef.ICON_CANCEL); 
		    menu.addItem(unionItem);
		    unionItem.addClickHandler(new ShpmCancelUnionAction(getThis(), headTable));	    
	    }*/
	    
	    //yuanlei 2012-09-12 增加更新批号功能
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_14)) { 
	    	menu.addItem(itemSeparator); 
	    	MenuItem unselect = new MenuItem("更新作业单明细",StaticRef.ICON_CONFIRM);
	    	unselect.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

				@Override
				public void onClick(MenuItemClickEvent event) {
					int[] edit_rows = detailTable.getAllEditRows();
					if(edit_rows.length == 0){
						return;
					}
					Record rec = null;
					String shpm_no = "";
					HashMap<String, Object> listmap = new HashMap<String, Object>();
					HashMap<String, String> rows_map = new HashMap<String, String>();   //专供标识
					HashMap<String, String> lot2_map = new HashMap<String, String>();   //专供标识
					HashMap<String, String> lotid_map = new HashMap<String, String>();  //批号
					for(int i = 0; i < edit_rows.length; i++) {
						rec = detailTable.getEditedRecord(edit_rows[i]);
						shpm_no = rec.getAttribute("SHPM_NO");
						rows_map.put(Integer.toString(i+1), rec.getAttribute("SHPM_ROW"));
						lot2_map.put(Integer.toString(i+1), rec.getAttribute("LOTATT02"));
						lotid_map.put(Integer.toString(i+1), rec.getAttribute("LOT_ID"));
					}
					listmap.put("1", shpm_no);
					listmap.put("2", rows_map);
					listmap.put("3", lot2_map);
					listmap.put("4", lotid_map);
					listmap.put("5", LoginCache.getLoginUser(). getUSER_ID());
					String json = Util.mapToJson(listmap);
					Util.async.execProcedure(json, "SP_MODIFY_LOT(?,?,?,?,?,?)", new AsyncCallback<String>() {
						@Override
						public void onSuccess(String result) {
							if(result.substring(0, 2).equals(StaticRef.SUCCESS_CODE)){
								MSGUtil.showOperSuccess();
							}
							else{
								MSGUtil.sayError(result.substring(2));
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
							MSGUtil.sayError(caught.getMessage());
						}
					});
				}
	    		
	    	});
	    	menu.addItem(unselect);
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_15)) {
	    	
	    	menu.addItem(itemSeparator); 
		    MenuItem inItem = new MenuItem("修改作业单信息",StaticRef.ICON_CONFIRM); 
		    menu.addItem(inItem);
		    inItem.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {

		    	@Override
				public void onClick(MenuItemClickEvent event) {
					// TODO Auto-generated method stub
					if(headTable != null && headTable.getSelection().length > 0){
						//弹出窗口
						new ModifyShpmWin(headTable, headTable.getSelection(),false);
						
					} else {
						SC.warn("请选择作业单!");
					}
				}
		    	
		    });		    
	    }
//	    if(isPrivilege(TrsPrivRef.SLFCON_P1_10)) {
//	    	
//	    	menu.addItem(itemSeparator); 
//		    MenuItem inItem = new MenuItem("入分拣库",StaticRef.ICON_CONFIRM); 
//		    menu.addItem(inItem);
//		    inItem.addClickHandler(new InWareHouseAction(headTable));
//		    
//	    }
//	    
//	    if(isPrivilege(TrsPrivRef.SLFCON_P1_10)) {
//	    	
//		    MenuItem outItem = new MenuItem("出分拣库",StaticRef.ICON_CANCEL); 
//		    menu.addItem(outItem);
//		    outItem.addClickHandler(new OutWareHouseAction(headTable));
//		    
//	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_05)) {                         //wangjun 2011-3-8
	    	
	    	menu.addItem(itemSeparator); 
	    	MenuItem allSelect = new MenuItem("全选",StaticRef.ICON_CONFIRM);
	    	allSelect.addClickHandler(new AllSelectAction(headTable));
	    	allSelect.setKeyTitle("Ctrl+B");
	    	KeyIdentifier allSelectKey = new KeyIdentifier();
	    	allSelectKey.setCtrlKey(true);
	    	allSelectKey.setKeyName("B");
	    	menu.addItem(allSelect);
	    }
	    
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_06)) { 
	    	MenuItem unselect = new MenuItem("反选",StaticRef.ICON_CANCEL);
	    	unselect.addClickHandler(new UnSelectAction(headTable));
	    	menu.addItem(unselect);
	    }
	    
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_07)) {
	    	
	    	menu.addItem(itemSeparator); 
	    	
		    MenuItem sumInfo = new MenuItem("汇总信息",StaticRef.ICON_NEW);
		    sumInfo.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
				@Override
				public void onClick(MenuItemClickEvent event) {
					countWin = new CountWin(headTable).getViewPanel();
				}
			});
		    menu.addItem(sumInfo);
	    
//	    sumInfo.addClickHandler(new)
//	    sumInfo.addClickHandler(new SumInfoAction(headTable));
//	    menu.setItems(allSelect,unselect,sumInfo); 
		    
	    }
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_09)) {
			MenuItem expList = new MenuItem("明细导出",StaticRef.ICON_EXPORT);  //wangjun 2011-4-2     明细导出
			menu.addItem(expList);
			expList.addClickHandler(new ExportShpmItemAction(headTable, " order by UNLOAD_AREA_ID ,nvl(edittime,addtime) desc "));
	    }
	    
	    if(isPrivilege(TrsPrivRef.SLFCON_P1_18)) {
	    	MenuItem exp = new MenuItem("导出",StaticRef.ICON_EXPORT);  //wangjun 2011-4-2     明细导出
			menu.addItem(exp);
			exp.addClickHandler(new ExportAction(headTable));
	    }
	    
	    headTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
            public void onShowContextMenu(ShowContextMenuEvent event) {
            	menu.showContextMenu();
                event.cancel();
            }
        });
	    
	}

	private void getConfigList(SGTable table) {

		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_SHIPMENT_HEADER作业单管理");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_SHIPMENT_HEADER_SHPM);
		createListField(table, listMap);	
	}

	public DynamicForm createSerchForm(final SGPanel form){
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(8);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		

		//1
//		SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER_NAME(),true);
//		Util.initComboValue(CUSTOMER_NAME, "BAS_CUSTOMER", "ID", "CUSTOMER_CNAME", " where CUSTOMER_FLAG='Y'");
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
		
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID", "");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		//2
		
		final TextItem LOAD_AREA_ID = new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID.setVisible(false);
		
		ComboBoxItem LOAD_AREA_NAME = new ComboBoxItem ("LOAD_AREA_NAME",Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME.setStartRow(true);
		Util.initArea(LOAD_AREA_NAME, LOAD_AREA_ID);
		LOAD_AREA_NAME.setColSpan(2);
		LOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
//		Util.initComboValue(LOAD_AREA_NAME, "BAS_AREA", "ID", "AREA_CNAME");
		
		final TextItem UNLOAD_AREA_ID = new TextItem("UNLOAD_AREA_ID");
		UNLOAD_AREA_ID.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME = new ComboBoxItem("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME());
//		Util.initComboValue(UNLOAD_AREA_NAME, "BAS_AREA", "ID", "AREA_CNAME");
		UNLOAD_AREA_NAME.setColSpan(2);
		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);
		UNLOAD_AREA_NAME.setTitleAlign(Alignment.LEFT);
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
		Util.initComboValue(TRANS_SRVC_ID, "BAS_TRANS_SERVICE","ID","SRVC_NAME");
		
		SGCombo SUPLR_NAME = new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initComboValue(SUPLR_NAME, "BAS_SUPPLIER", "ID", "SUPLR_CNAME");
		
		//3
		SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME());//发货方
		LOAD_NAME.setStartRow(true);
		
		SGText LOAD_ID = new SGText("LOAD_ID", Util.TI18N.LOAD_NAME_ID());//发货方代码
		
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		
		SGText UNLOAD_ID = new SGText("UNLOAD_ID", Util.TI18N.UNLOAD_NAME_ID());//收货方代码
		
		SGCombo STATUS_FROM = new SGCombo("STATUS_FROM",Util.TI18N.SHPM_STSTUS());
		STATUS_FROM.setColSpan(2);
		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT, "20");
		SGCombo STATUS_TO = new SGCombo("STATUS_TO","到");//wangjun 2011-4-6
		STATUS_TO.setColSpan(2);
		Util.initStatus(STATUS_TO, StaticRef.SHPMNO_STAT, "40");
		
		SGCombo ASSIGN_STAT_NAME = new SGCombo("ASSIGN_STAT_NAME",Util.TI18N.ASSIGN_STAT());
//		Util.initCodesComboValue(ASSIGN_STAT_NAME, "ASSIGN_STAT");
		Util.initStatus(ASSIGN_STAT_NAME, StaticRef.ASSIGN_STAT,"");
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());	//业务类型
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME");
		
		SGCombo TRANS_COND = new SGCombo("REFENENCE4",Util.TI18N.TRANS_COND());
        Util.initCodesComboValue(TRANS_COND, "TRANS_COND");
        
        SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());//托运单号
        SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());//运单号
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
//		C_ORG_FLAG.setColSpan(1);
//		C_ORG_FLAG.setWidth(120);
		C_ORG_FLAG.setValue(true);
		
		final TextItem EMPTY_FLAG = new TextItem("EMPTY_FLAG", "");
		EMPTY_FLAG.setValue("Y");
//		EMPTY_FLAG.setDefaultValue("Y");
		EMPTY_FLAG.setVisible(false);
		TextItem SHPM_FLAG = new TextItem("SHPM_FLAG","");
		SHPM_FLAG.setValue("Y");
		SHPM_FLAG.setVisible(false);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGText ODR_TIME_FROM = new SGText("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());	//订单时间
		SGText ODR_TIME_TO = new SGText("ODR_TIME_TO", "到");
		Util.initDateTime(searchForm,ODR_TIME_FROM);
		Util.initDateTime(searchForm,ODR_TIME_TO);

//		STATUS.addChangedHandler(new ChangedHandler() {
//			
//			@Override
//			public void onChanged(ChangedEvent event) {
//				System.out.println(form.getItem("STATUS").getDisplayValue());
//				if("&nbsp;".equals(form.getItem("STATUS").getDisplayValue())){
//					EMPTY_FLAG.clearValue();
//					EMPTY_FLAG.setValue("");
//				}else {
//					EMPTY_FLAG.setValue("Y");	
//				}
//			}
//		});
		
//		ASSIGN_STAT_NAME.setDefaultValue("未派发");
		
		SGCheck SLF_DELIVER_FLAG=new SGCheck("SLF_DELIVER_FLAG",Util.TI18N.SLF_DELIVER_FLAG());
//		SLF_DELIVER_FLAG.setStartRow(true);
		SLF_DELIVER_FLAG.setValue(false);
		
		SGCheck SLF_PICKUP_FLAG=new SGCheck("SLF_PICKUP_FLAG",Util.TI18N.SLF_PICKUP_FLAG());
		SLF_PICKUP_FLAG.setValue(false);
      
	    form.setItems(CUSTOMER_ID,CUSTOMER_NAME,SHPM_NO,CUSTOM_ODR_NO,EXEC_ORG_ID,EXEC_ORG_ID_NAME,
			      LOAD_AREA_NAME,LOAD_AREA_ID,UNLOAD_AREA_NAME,UNLOAD_AREA_ID,TRANS_SRVC_ID,SUPLR_NAME,
			      LOAD_NAME,LOAD_ID,UNLOAD_NAME,UNLOAD_ID,
			      STATUS_FROM,STATUS_TO,ASSIGN_STAT_NAME,BIZ_TYP,ODR_TIME_FROM,ODR_TIME_TO,TRANS_COND, 			     
				   ROUTE_ID,ODR_NO,REFENENCE1,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG,C_ORG_FLAG,EMPTY_FLAG,SHPM_FLAG);
		return form;
		
	}
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initVerify() {
	  check_map.put("TABLE", "TRANS_SHPMENT_HEADER");
	  check_map.put("ODR_TIME", StaticRef.CHK_DATE+Util.TI18N.ODR_TIME());

	}

	@Override
	public void onDestroy() {
		if(searchWin != null){
			searchWin.destroy();
		}
	}
	
//	     boolean stopDefault(event e) {   
//		    // Prevent the default browser action (W3C)   
//		    if ( e && e.preventDefault )   
//		    e.preventDefault();   
//		    // A shortcut for stoping the browser action in IE   
//		    else   
//		    window.event.returnValue = false;   
//		    return false;   
//		    }   
	public TmsSelfControl getThis(){
		return this;
	}
	
	/**
	 * 判断拆分是否合法
	 * @author yuanlei
	 * @return
	 */
	protected boolean isSplitValid(){
		boolean isAllMaxSplit = true;
		
		if(detailTable != null) {
			ListGridRecord[] records = detailTable.getSelection();
			int[] edit_rows = new int[records.length];
			for(int i = 0; i < records.length; i++) {
				edit_rows[i] = detailTable.getRecordIndex(records[i]);
			}
			Record rec = null;
			
			for(int i = 0; i < edit_rows.length; i++) {
				
				ListGridRecord initRecord = unshpmlstRec[edit_rows[i]];
				rec = detailTable.getEditedRecord(edit_rows[i]);
				double cur_qnty = Double.parseDouble(ObjUtil.ifObjNull(rec.getAttribute("QNTY"),"0").toString());
				double init_qnty = Double.parseDouble(ObjUtil.ifObjNull(initRecord.getAttribute("QNTY"),"0").toString());
				double rate = getRate(cur_qnty, init_qnty);
				if(rate > 1) {
					SC.warn("行号[" + rec.getAttribute("SHPM_ROW") + "]数量不能大于原单量!");
					return false;
				}
				else if(rate < 1) {
					isAllMaxSplit = false;
				}
			}
			if(records.length == detailTable.getRecords().length && isAllMaxSplit) {
				SC.warn("无效的拆分操作!");
				return false;
			}
			return true;
		}
		else {
			SC.warn("无效的拆分操作!");
			return false;
		}
	}
	
	private double getRate(double douPart, double douTotal) {
  	  
	    double rate = 0.0000;
	    if(douTotal > 0) {
	    	rate = douPart/douTotal;
	    }
	    return rate;
    }

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		TmsSelfControl view = new TmsSelfControl();
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
