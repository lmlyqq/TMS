package com.rd.client.view.tms;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.TranReportDS;
import com.rd.client.ds.tms.CustomActLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
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
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运单状态查询
 * @author xuweibin
 *
 */
@ClassForNameAble
public class OdrStatusView extends SGForm implements PanelFactory {

	private HashMap<String, String> detail_ck_map;
//	private TabSet bottomTabSet;
	private SGTable table;
//	private boolean isDownMax=false;
	private SGTable logTable;
	private DataSource logDS;
	private Window searchWin;
	private SGPanel searchForm;
	private DataSource ds;
	private SectionStack section;
//	private ToolStrip toolStrip;

	/*public OdrStatusView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		privObj=LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main=new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		section=new SectionStack();
		ds=TranReportDS.getInstance("V_ORDER_HEADER","TRANS_ORDER_HEADER");
		logDS=CustomActLogDS.getInstance("SF_STATUS_LOG");
		table=new SGTable(ds,"100%","100%",false,true,false);
		logTable=new SGTable(logDS,"100%","100%",false,true,false);
		
		ToolStrip toolStrip=new ToolStrip();// 主布局按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
//		IButton searchButton =createBtn(StaticRef.FETCH_BTN,StaticRef.ICON_SEARCH);
		IButton searchButton =createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin==null){
					searchForm=new SGPanel();
					searchWin=new SearchWin(ds,createSearchForm(searchForm),section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
			}
		});
		toolStrip.addMember(searchButton);
		main.addMember(toolStrip);
		
		//上边布局
		final SectionStackSection listItem=new SectionStackSection(Util.TI18N.LISTINFO());
		
		listItem.setControls(new SGPage(table,true).initPageBtn());
		createFieldList(table);
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				logTable.invalidateCache();
				Criteria crit=new Criteria();
				crit.addCriteria("OP_FLAG","M");
				Record rec=event.getRecord();
				ListGridRecord[] records=table.getSelection();
				if(records.length>0){
					crit.addCriteria("ODR_NO",rec.getAttribute("ODR_NO"));
					logTable.fetchData(crit,new DSCallback(){
						@Override
						public void execute(DSResponse response,
								Object rawData, DSRequest request) {
							LoginCache.setPageResult(logTable, new FormItem(), new FormItem());
						}
					});
				}
			}
		});
		listItem.setItems(table);
		
		listItem.setExpanded(true);
		section.addSection(listItem);
		//下边布局
		
		ListGridField ADDTIME=new ListGridField("ADDTIME","日期");
		ADDTIME.setWidth(150);
		//ListGridField STATUS_NAME=new ListGridField("STATUS_NAME","节点名称");
		//STATUS_NAME.setWidth(150);
		ListGridField NOTES=new ListGridField("NOTES",Util.TI18N.NOTES());
		NOTES.setWidth(250);
		ListGridField DESCR=new ListGridField("DESCR","描述");
		DESCR.setWidth(500);
		ListGridField ADDWHO=new ListGridField("ADDWHO","操作员");
		ADDWHO.setWidth(80);
		logTable.setFields(ADDTIME,ADDWHO,NOTES,DESCR);
		logTable.setCanEdit(false);
		logTable.setShowFilterEditor(false);
		logTable.setHeight("150%");
		logTable.setWidth("100%");
		section.addMember(logTable);
		
		main.addMember(section);
		return main;
	}

	private void createFieldList(SGTable table) {
		//LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get("V_ORDER_HEADERODR_VIEW");
		LinkedHashMap<String, String> listMap = LoginCache.getListConfig().get(StaticRef.V_ORDER_HEADER_ODR);
		createListField(table, listMap);	
	}

//	private IButton createDownBtn(final SectionStack topLay, final TabSet downTabSet) {
//		final IButton maxBtn=new IButton();
//		maxBtn.setIcon(StaticRef.ICON_TODOWN);
//		maxBtn.setTitle("");
//		maxBtn.setPrompt(StaticRef.TO_MAX);
//		maxBtn.setWidth(24);
//		maxBtn.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				if(!isDownMax){
//					topLay.setHeight("100%");
//					downTabSet.setHeight("0%");
//					maxBtn.setIcon(StaticRef.ICON_TOUP);
//					maxBtn.setPrompt(StaticRef.TO_NORMAL);
//				}else{
//					topLay.setHeight("55%");
//					downTabSet.setHeight("45%");	
//					maxBtn.setIcon(StaticRef.ICON_TODOWN);
//					maxBtn.setPrompt(StaticRef.TO_MAX);
//				}
//				isDownMax=!isDownMax;
//			}
//		});
//		return maxBtn;
//	}

	@Override
	public void initVerify() {

		check_map.put("TABLE", "TRANS_ORDER_HEADER");
		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL + Util.TI18N.CUSTOMER_ID());
		check_map.put("ODR_TIME", StaticRef.CHK_NOTNULL + Util.TI18N.ODR_TIME());
		check_map.put("TRANS_SRVC_ID", StaticRef.CHK_NOTNULL + Util.TI18N.TRANS_SRVC_ID());
		check_map.put("BIZ_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.BIZ_TYP());
		check_map.put("EXEC_ORG_ID", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("EXEC_ORG_ID_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("LOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		check_map.put("UNLOAD_AREA_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		check_map.put("LOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_AREA_NAME());
		check_map.put("UNLOAD_AREA_NAME",  StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_AREA_NAME());
		check_map.put("ODR_TIME", StaticRef.CHK_DATE + Util.TI18N.ODR_TIME());
		check_map.put("FROM_LOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_LOAD_TIME());
		check_map.put("PRE_LOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_LOAD_TIME());
		check_map.put("FROM_UNLOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_UNLOAD_TIME());
		check_map.put("PRE_UNLOAD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_UNLOAD_TIME());
		check_map.put("PRE_POD_TIME", StaticRef.CHK_DATE + Util.TI18N.FROM_POD_TIME());
		check_map.put("LOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_CONTACT());
		check_map.put("LOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.LOAD_TEL());
		check_map.put("UNLOAD_CONTACT", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_CONTACT());
		check_map.put("UNLOAD_TEL", StaticRef.CHK_NOTNULL + Util.TI18N.UNLOAD_TEL());
		
		detail_ck_map = new HashMap<String, String>();
		detail_ck_map.put("TABLE", "TRANS_ORDER_ITEM");
		detail_ck_map.put("SKU_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.SKU_NAME());
		detail_ck_map.put("QNTY", StaticRef.CHK_NOTNULL + Util.TI18N.PACK_QTY());
		detail_ck_map.put("TEMPERATURE1", StaticRef.CHK_NOTNULL + Util.TI18N.TEMPERATURE());
		
		cache_map.put("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("CREATE_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("CREATE_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		cache_map.put("UGRT_GRD", StaticRef.UGRT_GRD);
		cache_map.put("POD_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_STAT", StaticRef.NORMAL);
	}

	private DynamicForm createSearchForm(final DynamicForm form) {
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());
		SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());
		SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());
		
		final TextItem LOAD_AREA_ID2=new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID2.setVisible(false);
		ComboBoxItem LOAD_AREA_NAME2=new ComboBoxItem("LOAD_AREA_NAME2","发货城市");
		LOAD_AREA_NAME2.setColSpan(2);
		Util.initArea(LOAD_AREA_NAME2, LOAD_AREA_ID2);
		LOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		final TextItem UNLOAD_AREA_ID2=new TextItem("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME2=new ComboBoxItem("UNLOAD_AREA_NAME2","收货城市");
		Util.initArea(UNLOAD_AREA_NAME2, UNLOAD_AREA_ID2);
		UNLOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		final TextItem CREATE_ORG_ID=new TextItem("CREATE_ORG_ID");
		CREATE_ORG_ID.setVisible(false);
		SGText CREATE_ORG_ID_NAME=new SGText("CREATE_ORG_ID_NAME",Util.TI18N.ORDER_ORG());
		Util.initOrg(CREATE_ORG_ID_NAME, CREATE_ORG_ID,false,"30%","40%");
		
		TextItem EXEC_ORG_ID=new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME=new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"30%","40%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG",Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
		
		SGCombo ORD_PRO_LEVER=new SGCombo("UGRT_GRD",Util.TI18N.ORD_PRO_LEVER());//订单优先级(紧急程度)
		Util.initCodesComboValue(ORD_PRO_LEVER,"UGRT_GRD");
		
//		SGCheck COD_FLAG=new SGCheck("COD_FLAG","需要收款");
		SGCombo COD_FLAG=new SGCombo("COD_FLAG","需要收款");
		LinkedHashMap<String,String>map=new LinkedHashMap<String,String>();
		map.put("", "");
		map.put("Y", "是");
		map.put("N", "否");
		COD_FLAG.setValueMap(map);
		COD_FLAG.setDefaultValue("");
		
		final SGCombo CUSTOM_ATTR=new SGCombo("REFENENCE3",Util.TI18N.CUSTOM_ATTR());
		Util.initCodesComboValue(CUSTOM_ATTR,"CUSTOM_ATTR");
		
		SGDate ADDTIME_FROM=new SGDate("ADDTIME_FROM",Util.TI18N.ODR_TIME_FROM());
		SGDate ADDTIME_TO=new SGDate("ADDTIME_TO","到");
		
		form.setFields(ODR_NO,REFENENCE1,CUSTOM_ODR_NO,CUSTOM_ATTR,BIZ_TYP,LOAD_AREA_ID2,LOAD_AREA_NAME2,UNLOAD_AREA_ID2,UNLOAD_AREA_NAME2,ORD_PRO_LEVER,
				ADDTIME_FROM,ADDTIME_TO,COD_FLAG,CREATE_ORG_ID,CREATE_ORG_ID_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		return form;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		OdrStatusView view = new OdrStatusView();
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