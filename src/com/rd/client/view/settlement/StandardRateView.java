package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.CancelTariffAction;
import com.rd.client.action.settlement.NewTariffStandAction;
import com.rd.client.action.settlement.saveTaffRulStandAction;
import com.rd.client.action.settlement.tariff.NewTariffCondStandAction;
import com.rd.client.action.settlement.tariff.NewTariffRuleStandAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.CopyAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.StandTariffCondDS;
import com.rd.client.ds.settlement.StandTariffRuleDS;
import com.rd.client.ds.settlement.StandTrriffHeaderDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.FeeRuleWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.ChangeEvent;
import com.smartgwt.client.widgets.grid.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 费用管理->计费管理->标准报价
 * @author 
 */
@ClassForNameAble
public class StandardRateView extends SGForm implements PanelFactory {

	public SGTable table;
	public SGTable feeTable;
	private DataSource ds;
	private DataSource detailDS;
	private DataSource conditionDS;
	private Window searchWin;
	private SGPanel searchForm = new SGPanel();
	public ValuesManager vm;
	private SGPanel basInfo;
	private SGPanel basInfo2;
	private SectionStack section;
	public SGTable groupTable;
	private SectionStack sectionStack;
	public Record selectRecord;
	public Record selectRuleRecord;
	public int hRow = 0;
	public DynamicForm totalPanel;
	public SGPanel isG;
	public HashMap<String, String> detail_ck_map;
	public HashMap<String, String> fee_cache_map;
	public SGCheck EXPRESS_FLAG;
	
	public IButton newButton;
	public IButton copyButton;
	public IButton saveButton;
	public IButton delButton;
	public IButton canButton;
	
	private IButton newB;
	private IButton delB;
	private IButton savB;
	private IButton canB;
	
	private IButton newC;
	private IButton delC;
	private IButton savC;
	private IButton canC;
	
	private DynamicForm pageForm;
	
	public HashMap<String, IButton> add_detail_map;
	public HashMap<String, IButton> del_detail_map;
	public HashMap<String, IButton> sav_detail_map;
	
	public HashMap<String, IButton> add_cond_map;
	public HashMap<String, IButton> del_cond_map;
	public HashMap<String, IButton> sav_cond_map;
	
	private VLayout layOut;
	private VLayout layOut2;
	
	private SectionStackSection  listItem;
	
	public boolean isMax = true;
	
	private ListGridRecord[] nullRecord =  new ListGridRecord[0];
	
	public Criteria crit =new Criteria();
	
	
	private ListGrid listGrid;
//	private String START_AREA;
//	private String END_AREA;
//	private String TRANS_SRVC;
//	private String BIZ;
	
	/*public RateManagerView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("99%");
		vm = new ValuesManager();
		ds = StandTrriffHeaderDS.getInstance("V_TARIFF_HEADER_PAY1","TARIFF_HEADER");
		detailDS = StandTariffRuleDS.getInstance("V_TARIFF_RULE1","TARIFF_RULE");
		conditionDS = StandTariffCondDS.getInstance("TARIFF_CONDITION1", "TARIFF_CONDITION");
		
		initVerify();
		
		
		// 主布局
		
		VStack vsk = new VStack();
		vsk.setWidth100();
		vsk.setHeight100();
		
		//上部布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight("60%");

		// 左上角列表
		table = new SGTable(ds, "100%", "100%", false, true, false);
		
		createListField();
		sectionStack = new SectionStack();
		listItem = new SectionStackSection("协议列表");
		listItem.setItems(table);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		stack.addMember(sectionStack);
		
		addSplitBar(stack);
//		ba= new Splitbar();
//		ba.setWidth(10);
//		
//		ba.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				if(isMax) {
//					sectionStack.setWidth("40%");
//					ba.setBackgroundImage(StaticRef.ICON_GORIGHT);
//					layOut.setVisible(true);
//				}
//				else {
//					sectionStack.setWidth("100%");
//					ba.setBackgroundImage(StaticRef.ICON_GOLEFT);
//					layOut.setVisible(false);
//				}
//				isMax = !isMax;
//			}
//			
//		});	
//		stack.addMember(ba);
		
		
		//下部布局
		HStack dsk = new HStack();
		dsk.setWidth100();
		dsk.setHeight("35%");
//		feeStack=new SectionStack();
//		feeStack.setHeight100();

		// 左下角列表
		feeTable = new SGTable(detailDS, "100%", "100%", false, true, false);
		createFeeList();
//		dsk.addMember(feeTable);
//		feeStack.setMembers(createFeeSearch(),feeTable);
		dsk.addMember(listGrid);
//		feeStack.setWidth("100%");
		
//		bar = new Splitbar();
//		bar.setWidth(10);
//		
//		bar.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				if(isMax1) {
//					feeStack.setWidth("40%");
//					bar.setBackgroundImage(StaticRef.ICON_GORIGHT);
//					layOut2.setVisible(true);
//				}
//				else {
//					feeStack.setWidth("100%");
//					bar.setBackgroundImage(StaticRef.ICON_GOLEFT);
//					layOut2.setVisible(false);
//				}
//				isMax1 = !isMax1;
//			}
//			
//		});		
//		dsk.addMember(bar);
		
		// 二
		TabSet bottoTabSet = new TabSet();
		bottoTabSet.setWidth100();
		bottoTabSet.setHeight100();
		bottoTabSet.setMargin(1);
		
		//下半部分列表
		createbottoInfo();
		
		VLayout FeeInfo = new VLayout(); //订单明细栏
		FeeInfo.setWidth100();
		FeeInfo.addMember(createFeeInfo());
		FeeInfo.addMember(createFeeBtn());
		
		VLayout orderItem = new VLayout(); //条件栏
		orderItem.setWidth100();
		orderItem.setHeight100();
		orderItem.addMember(groupTable);
		orderItem.addMember(createConditionBtn());
		
		Tab tab2 = new Tab("费率设置");
		tab2.setPane(FeeInfo);
		bottoTabSet.addTab(tab2);
		
	    Tab tab1 = new Tab("过滤条件");
	    tab1.setPane(orderItem);
	    bottoTabSet.addTab(tab1);
		

		layOut = new VLayout();
		layOut.setWidth("80%");
		layOut.setHeight("100%");
		createMainInfo();
		layOut.addMember(basInfo);
		layOut.setVisible(false);
		
		layOut2 = new VLayout();
		layOut2.setWidth("60%");
		layOut2.setHeight("100%");
		layOut2.addMember(bottoTabSet);
		layOut2.setVisible(false);
//		layOut2.setVisible(false);
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		stack.addMember(layOut);
		dsk.addMember(layOut2);
		
		main.setMembers(toolStrip,stack,dsk);
		return main;
	}
	
//	private Canvas createFeeSearch(){
//		HLayout vlay=new HLayout();
//		vlay.setWidth("35%");
//		vlay.setHeight(20);
//		final DynamicForm form=new DynamicForm();
//		form.setNumCols(8);
//		form.setWidth("35%");
//		vlay.setMembersMargin(1);
//		
//		
//		final TextItem WHR_START_AREA_ID=new TextItem("WHR_START_AREA_ID");
//		WHR_START_AREA_ID.setVisible(false);
//		ComboBoxItem WHR_START_AREA_NAME=new ComboBoxItem("WHR_START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());
//		WHR_START_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
////		WHR_START_AREA_NAME.setTitleOrientation(TitleOrientation.LEFT);
//		WHR_START_AREA_NAME.setWidth(FormUtil.Width);
//		Util.initArea(WHR_START_AREA_NAME, WHR_START_AREA_ID);
//		
//		final TextItem WHR_END_AREA_ID=new TextItem("WHR_END_AREA_ID");
//		WHR_END_AREA_ID.setVisible(false);
//		ComboBoxItem WHR_END_AREA_NAME=new ComboBoxItem("WHR_END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());
//		WHR_END_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
//		WHR_END_AREA_NAME.setWidth(FormUtil.Width);
////		WHR_END_AREA_NAME.setTitleOrientation(TitleOrientation.LEFT);
//		Util.initArea(WHR_END_AREA_NAME, WHR_END_AREA_ID);
//		
//		//final SGCombo TRANS_SRVC_ID=new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
////		TRANS_SRVC_ID.setTitleOrientation(TitleOrientation.LEFT);
//		//Util.initTrsService(TRANS_SRVC_ID, "");
//		
//		final SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
////		BIZ_TYP.setTitleOrientation(TitleOrientation.LEFT);
//		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
//		
//		IButton search=createUDFBtn("", StaticRef.ICON_SEARCH);
//		search.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				if(selectRecord!=null){
//					Criteria criteria=new Criteria();
//					criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
//	        		criteria.addCriteria("TFF_ID",selectRecord.getAttributeAsString("ID"));
//	        		crit=form.getValuesAsCriteria();
//	        		criteria.addCriteria(crit);
//	        		
//	        		feeTable.fetchData(criteria);
//				}
//			}
//		});
//		search.setLayoutAlign(VerticalAlignment.BOTTOM);
//		
//		form.setItems(WHR_START_AREA_ID,WHR_START_AREA_NAME,WHR_END_AREA_ID,WHR_END_AREA_NAME,BIZ_TYP);
////		form.setAlign(Alignment.LEFT);
////		form.setitems
//		vlay.addMember(form);
//		vlay.addMember(search);
//		return vlay;
//	}

	private SectionStack createMainInfo() {

		/**
		 * 基本信息
		 * 
		 */
		basInfo = new SGPanel();
		basInfo.setTitleWidth(75);
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		// 1、 协议编号，合同号，生效时间，失效服务,单据类型 
		SGText TFF_NAME=new SGText("TFF_NAME", ColorUtil.getRedTitle(Util.TI18N.TFF_NAME()));
//		TFF_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.ORDER_CODE()));
		
		//SGText CONTACT_NO = new SGText("CONTACT_NO",Util.TI18N.CONTACT_NO());
//		CONTACT_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.ORDER_CODE()));
		
		final SGText START_DATE = new SGText("START_DATE", ColorUtil.getRedTitle(Util.TI18N.FEE_START_DATE()));
		Util.initDate(basInfo,START_DATE);
		
		final SGText END_DATE = new SGText("END_DATE", ColorUtil.getRedTitle(Util.TI18N.FEE_END_DATE()));
		Util.initDate(basInfo,END_DATE);
		
//		SGCombo DOC_TYP = new SGCombo("DOC_TYP", Util.TI18N.DOC_TYP(),false);
//		DOC_TYP.setTitle(ColorUtil.getRedTitle(Util.TI18N.DOC_TYP()));
//		Util.initCodesComboValue(DOC_TYP, "DOC_TYP");
//		
		
		//2、协议类型，协议对象，签订机构，执行机构，包含下级机构
//		final SGCombo TFF_TYP = new SGCombo("TFF_TYP", Util.TI18N.TFF_TYP());
//		Util.initCodesComboValue(TFF_TYP, "TRANS_TFF_TYP");
//		TFF_TYP.setTitle(ColorUtil.getRedTitle(Util.TI18N.TFF_TYP()));
//		TFF_TYP.setDisabled(true);
		
//		TextItem OBJECT_ = new TextItem("OBJECT_ID");
//		OBJECT_ID.setVisible(false);
		
//		OBJECT_ID = new SGCombo("OBJECT_ID", Util.TI18N.OBJECT_NAME());
//		OBJECT_ID.setTitleOrientation(TitleOrientation.TOP);
//		OBJECT_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.OBJECT_NAME()));
//		Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "SHORT_NAME"," WHERE ENABLE_FLAG = 'Y' " +
//				"and id in (select customer_id from BAS_CUSTOMER_ORG where org_id in (select id from bas_org where id = '"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"' " +
//						"or org_index like '%,"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"%'))", "");
		
		SGCombo COUNT_TYP = new SGCombo("COUNT_TYP", Util.TI18N.COUNT_TYP(),true);
		Util.initComboValue(COUNT_TYP, "bas_codes", "CODE", "NAME_C", " where prop_code='COUNT_TYP'");
		COUNT_TYP.setVisible(false);
		
		//签订机构
		TextItem SIGN_ORG_ID = new TextItem("SIGN_ORG_ID");
		SIGN_ORG_ID.setVisible(false);
		
		SGText SIGN_ORG_ID_NAME = new SGText("SIGN_ORG_ID_NAME", Util.TI18N.SIGN_ORG_ID());
//		SIGN_ORG_ID_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SIGN_ORG_ID()));
		
		Util.initOrg(SIGN_ORG_ID_NAME, SIGN_ORG_ID, false, "30%", "40%");
		
		//执行机构
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "40%");
		
		
		SGCheck C_ORG_FLAG = new SGCheck("INCLUDE_SUB_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		
		//3、排序，备注，激活
		//SGText SORTORDER = new SGText("SORTORDER",Util.TI18N.SORTORDER());
		
		SGText NOTES = new SGText("NOTES",Util.TI18N.NOTES());
		NOTES.setColSpan(6);
		NOTES.setWidth(FormUtil.longWidth+FormUtil.Width);

		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());	
		ENABLE_FLAG.setValue(false);//激活
		ENABLE_FLAG.setColSpan(2);
		
		TextItem TFF_ID = new TextItem("TFF_ID");
		TFF_ID.setVisible(false);
		
//		COM_FLAG =new SGCheck("COM_FLAG","通用");
//		COM_FLAG.setValue(false);
		
//		OBJECT_ID.addChangedHandler(new ChangedHandler() {
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if(ObjUtil.isNotNull(OBJECT_ID.getValue())){
//					COM_FLAG.setValue(false);
//				}else{
//					COM_FLAG.setValue(true);
//				}
//			}
//		});
//		COM_FLAG.addChangedHandler(new ChangedHandler() {
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if(ObjUtil.isNotNull(OBJECT_ID.getValue())){
//					object_name=OBJECT_ID.getValue().toString();
//				}
//				if(ObjUtil.isNotNull(COM_FLAG.getValue()) && "TRUE".equalsIgnoreCase(COM_FLAG.getValue().toString())){
//					OBJECT_ID.clearValue();
//				}else{
//					OBJECT_ID.setValue(object_name);
//				}
//			}
//		});
		
		basInfo.setItems(TFF_NAME,START_DATE,END_DATE,COUNT_TYP,SIGN_ORG_ID_NAME,EXEC_ORG_ID_NAME,
				C_ORG_FLAG,ENABLE_FLAG,NOTES,SIGN_ORG_ID,EXEC_ORG_ID,TFF_ID);
		
		
		section = new SectionStack();
		section.setVisible(true);
		section.setBackgroundColor(ColorUtil.BG_COLOR);

		// 1，基本信息
		SectionStackSection basicInfo = new SectionStackSection(Util.TI18N.BASE_MESSAGE());
		basInfo.setHeight("100%");
		basInfo.setWidth("40%");
		basicInfo.addItem(basInfo);
		basicInfo.setExpanded(true);
		section.addSection(basicInfo);

		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);
		/*TFF_TYP.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(basInfo.getValue("TFF_TYP").equals(StaticRef.TRANS_TFF_TYP)){
					Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "SHORT_NAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}else{
					Util.initComboValue(OBJECT_ID, "BAS_SUPPLIER", "ID", "SHORT_NAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}
			}
		});*/
		//Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "CUSTOMER_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");
		//Util.initComboValue(OBJECT_ID, "BAS_SUPPLIER", "ID", "SUPLR_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");

		return section;
	}
	
	//费率设置也签
	private SectionStack createFeeInfo(){
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setHeight("30%");
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		//1、起点区域，终点区域，运输服务
		TextItem WHR_START_AREA_ID = new TextItem("WHR_START_AREA_ID");
		WHR_START_AREA_ID.setVisible(false);
		
		ComboBoxItem START_AREA_ID_NAME = new ComboBoxItem("START_AREA_ID_NAME", Util.TI18N.START_AREA_ID_NAME());//发货区域
		START_AREA_ID_NAME.setWidth(FormUtil.Width);
		START_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(START_AREA_ID_NAME, WHR_START_AREA_ID);
		
		TextItem WHR_END_AREA_ID = new TextItem("WHR_END_AREA_ID");
		WHR_END_AREA_ID.setVisible(false);
		
		ComboBoxItem END_AREA_ID_NAME = new ComboBoxItem("END_AREA_ID_NAME", Util.TI18N.END_AREA_ID_NAME());//收货区域
		END_AREA_ID_NAME.setWidth(FormUtil.Width);
		END_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(END_AREA_ID_NAME, WHR_END_AREA_ID);
		
		SGCombo TRANS_SRVC_ID = new SGCombo("WHR_TRANS_SRVC_ID", "车辆类型");
		Util.initComboValue(TRANS_SRVC_ID, "bas_vehicle_type", "id", "vehicle_type");
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", ColorUtil.getRedTitle(Util.TI18N.BIZ_TYP()));
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		//2、费用属性，费用名称，计费基准，进位方式
		SGCombo FEE_ATTR = new SGCombo("FEE_ATTR", Util.TI18N.FEE_ATTR(),true);
		FEE_ATTR.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_ATTR()));
		Util.initCodesComboValue(FEE_ATTR, "FEE_ATTR");
		FEE_ATTR.setDisabled(true);
		
		final SGCombo FEE_ID = new SGCombo("FEE_ID", Util.TI18N.FEE_NAME());
		FEE_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_NAME()));
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " WHERE FEE_ATTR = '53EB6809BFCC436799F735AAE23658B9'");
		final SGText FEE_NAME = new SGText("FEE_NAME",Util.TI18N.FEE_NAME());
		FEE_NAME.setVisible(false);
		FEE_ID.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				FEE_NAME.setValue(FEE_ID.getDisplayValue());
			}
		});
		
		final SGCombo FEE_BASE = new SGCombo("FEE_BASE", Util.TI18N.FEE_BASE());
		FEE_BASE.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_BASE()));
		//Util.initCodesComboValue(FEE_BASE, "FEE_BASE", true);		
		Util.initComboValue(FEE_BASE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		final SGText FEE_BASE_NAME = new SGText("FEE_BASE_NAME",Util.TI18N.FEE_BASE_NAME());
		FEE_BASE_NAME.setVisible(false);
		FEE_BASE.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				FEE_BASE_NAME.setValue(FEE_BASE.getDisplayValue());
			}
		});
		
		SGCombo CARRY_TYP = new SGCombo("CARRY_TYP", Util.TI18N.CARRY_TYP());
//		CARRY_TYP.setTitle(ColorUtil.getRedTitle(Util.TI18N.CARRY_TYP()));
		Util.initCodesComboValue(CARRY_TYP, "CARRY_TYP");
		
		//3、数量单位，重量单位，体积单位，计费方式
		SGCombo UOM_UNIT = new SGCombo("UOM", Util.TI18N.UOM_UNIT(),true);
		Util.initComboValue(UOM_UNIT, "V_BAS_UOM", "DESCR", "UOM");
		
		SGCombo WEIGHT_UNIT = new SGCombo("WEIGHT_UNIT", Util.TI18N.WEIGHT_UNIT());
		Util.initComboValue(WEIGHT_UNIT, "BAS_MSRMNT_UNIT", "UNIT", "UNIT_NAME", " MSRMNT_CODE = 'WEIGHT'", " SHOW_SEQ");
		
		SGCombo VOLUME_UNIT = new SGCombo("VOLUME_UNIT", Util.TI18N.VOLUME_UNIT());
		Util.initComboValue(VOLUME_UNIT, "BAS_MSRMNT_UNIT", "UNIT", "UNIT_NAME", " MSRMNT_CODE = 'VOLUME'", " SHOW_SEQ");
		
		final SGCombo FEE_TYP = new SGCombo("FEE_TYP", Util.TI18N.FEE_TYP());
		FEE_TYP.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_TYP()));
		Util.initCodesComboValue(FEE_TYP, "FEE_TYP");
		final SGText FEE_TYP_NAME = new SGText("FEE_TYP_NAME",Util.TI18N.FEE_TYP_NAME());
		FEE_TYP_NAME.setVisible(false);
		FEE_TYP.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				FEE_TYP_NAME.setValue(FEE_TYP.getDisplayValue());
			}
		});
		
		//4、优先级，分级标准
		SGText PRIORTY = new SGText("PRIORTY", Util.TI18N.PRIORTY());
		
		final SGCombo GRADE_BASE = new SGCombo("GRADE_BASE", Util.TI18N.GRADE_BASE(),true);
		GRADE_BASE.setTitle(ColorUtil.getRedTitle(Util.TI18N.GRADE_BASE()));
		//Util.initCodesComboValue(GRADE_BASE, "FEE_BASE",true);
		Util.initComboValue(GRADE_BASE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		
		final SGCombo APPORT_BASE = new SGCombo("APPORT_BASE", "分摊基准");
		//Util.initCodesComboValue(APPORT_BASE, "FEE_BASE", true);
		Util.initComboValue(APPORT_BASE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		
		//5、上限运算符，上限值，下限运算符，下限值
		SGCombo LOWER_LMT_OPRT = new SGCombo("LOWER_LMT_OPRT", Util.TI18N.LOWER_LMT_OPRT(),true);
		//Util.initCodesComboValue(LOWER_LMT_OPRT, "OPERATOR_DOWN",true);
		Util.initComboValue(LOWER_LMT_OPRT, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPERATOR_DOWN'", "");
		
		SGText LOWER_LMT = new SGText("LOWER_LMT", Util.TI18N.LOWER_LMT());
		
		SGCombo UPPER_LMT_OPRT = new SGCombo("UPPER_LMT_OPRT", Util.TI18N.UPPER_LMT_OPRT());
		//Util.initCodesComboValue(UPPER_LMT_OPRT, "OPERATOR_DOWN",true);
		Util.initComboValue(UPPER_LMT_OPRT, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPERATOR_DOWN'", "");
		
		SGText UPPER_LMT = new SGText("UPPER_LMT", Util.TI18N.UPPER_LMT());
		
		//6、基本费率，基数金额，最低金额，最高金额
		final SGLText BASE_RATE = new SGLText("BASE_RATE", Util.TI18N.BASE_RATE(),false);
		BASE_RATE.setTitle(ColorUtil.getRedTitle(Util.TI18N.BASE_RATE()));
		BASE_RATE.setWidth(FormUtil.longWidth);
		BASE_RATE.setColSpan(4);
		BASE_RATE.setValue("0.00");
		
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new FeeRuleWin(BASE_RATE, "40%", "38%").getViewPanel();
			}
		});
        BASE_RATE.setIcons(searchPicker);
        
        
		
		SGText BASE_AMT = new SGText("BASE_AMT", Util.TI18N.BASE_AMT(),true);
		BASE_AMT.setValue("0.00");
		
//		SGText CONT_PRICE = new SGText("CONT_PRICE", Util.TI18N.SETT_CONT_PRICE());
//		CONT_PRICE.setValue("0.00");
		
		SGText MIN_AMT = new SGText("MIN_AMT", Util.TI18N.MIN_AMT(),true);
		MIN_AMT.setValue("0.00");
		
		SGText MAX_AMT = new SGText("MAX_AMT", Util.TI18N.MAX_AMT());
		MAX_AMT.setValue("0.00");
		
		
		//7、最小计数单位，激活
		SGText MIN_UNIT = new SGText("MIN_UNIT", Util.TI18N.MIN_UNIT());
		MIN_UNIT.setValue("0.00");
		
		SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		
		EXPRESS_FLAG = new SGCheck("EXPRESS_FLAG", "计算公式");
//		EXPRESS_FLAG.setValue(false);
//		EXPRESS_FL
		
		FEE_BASE.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
//				GRADE_BASE.setValue(Util.iff(event.getValue(),"").toString());
				APPORT_BASE.setValue(Util.iff(event.getValue(),"").toString());
			}
		});
		
		
		basInfo2 = new SGPanel();
		basInfo2.setWidth("40%");
		basInfo2.setCellPadding(0);
		basInfo2.setTitleWidth(75);
		basInfo2.setItems(START_AREA_ID_NAME, END_AREA_ID_NAME, TRANS_SRVC_ID, BIZ_TYP, FEE_ATTR,FEE_ID,FEE_BASE,
				CARRY_TYP,UOM_UNIT,WEIGHT_UNIT,VOLUME_UNIT,FEE_TYP,GRADE_BASE,PRIORTY,
				APPORT_BASE,
				ENABLE_FLAG,WHR_START_AREA_ID,WHR_END_AREA_ID,FEE_NAME,FEE_BASE_NAME,FEE_TYP_NAME);
		
		isG = new SGPanel();
		isG.setTitleWidth(75);
		isG.setWidth("40%");
		isG.setIsGroup(true);
		isG.setGroupTitle("级差");
		isG.setItems(LOWER_LMT_OPRT,LOWER_LMT,UPPER_LMT_OPRT,UPPER_LMT,BASE_AMT,BASE_RATE,EXPRESS_FLAG,MIN_AMT,MAX_AMT,MIN_UNIT);
	
		section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		section.addMember(basInfo2);
		section.addMember(isG);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);
		vLay.addMember(section);
		
		vm = new ValuesManager();
		vm.addMember(basInfo2);
		vm.addMember(isG);
		
		return section;
	}


	/**
	 * 
	 * @author fangliangmeng
	 */
	private void createListField() {
	
		table.setShowRowNumbers(true);

		table.setCanEdit(false);
		ListGridField TFF_NAME = new ListGridField("TFF_NAME", Util.TI18N.TFF_NAME(), 90);//执行机构
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID_NAME(), 100);//执行机构
		ListGridField START_DATE = new ListGridField("START_DATE", Util.TI18N.FEE_START_DATE(), 100);//生效日期
		ListGridField END_DATE = new ListGridField("END_DATE", Util.TI18N.FEE_END_DATE(),110);//失效日期
		//ListGridField DOC_TYP = new ListGridField("DOC_TYP_NAME", Util.TI18N.DOC_TYP(),90);//单据类型
		ListGridField TFF_TYP = new ListGridField("TFF_TYP_NAME", Util.TI18N.TFF_TYP(),120);//协议类型
		//ListGridField OBJECT_NAME = new ListGridField("OBJECT_NAME", Util.TI18N.OBJECT_NAME(),90);//协议对象
		ListGridField SORTORDER = new ListGridField("SORTORDER", Util.TI18N.SORTORDER(),70);//排序
		ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(),60);//激活
		ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		//ListGridField COM_FLAG=new ListGridField("COM_FLAG","通用",60);
		//COM_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(TFF_NAME,EXEC_ORG_ID_NAME,START_DATE,END_DATE,TFF_TYP,SORTORDER,ENABLE_FLAG);
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				selectRecord = event.getRecord();
				
				/*if(selectRecord.getAttribute("TFF_TYP").equals(StaticRef.TRANS_TFF_TYP)){
					Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "CUSTOMER_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}else{
					Util.initComboValue(OBJECT_ID, "BAS_SUPPLIER", "ID", "SUPLR_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}*/
				
                basInfo.editRecord(selectRecord);
                
                enableOrDisables(add_map, true);
				enableOrDisables(del_map, true);
				enableOrDisables(save_map, false);
				
				Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
        		if(selectRecord == null)
        			return;
        		
        		criteria.addCriteria("TFF_ID",selectRecord.getAttributeAsString("ID"));
//        		criteria.addCriteria("WHR_START_AREA_ID",START_AREA);
//        		criteria.addCriteria("WHR_END_AREA_ID",END_AREA);
//        		criteria.addCriteria("TRANS_SRVC_ID",TRANS_SRVC);
//        		criteria.addCriteria("BIZ_TYP",BIZ);
        		
    			feeTable.fetchData(criteria,new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						if(feeTable.getRecords().length > 0){
		    				feeTable.selectRecord(feeTable.getRecord(0));
						}else{
							basInfo2.clearValues();
							groupTable.setRecords(nullRecord);
						}
					}
				});
			}
		});
		
//		table.addRecordClickHandler(new RecordClickHandler() {
//			
//			@Override
//			public void onRecordClick(RecordClickEvent event) {
//				Criteria criteria = new Criteria();
//        		criteria.addCriteria("OP_FLAG","M");
//        		if(selectRecord == null)
//        			return;
//                criteria.addCriteria("TFF_ID",selectRecord.getAttributeAsString("ID"));
//    			feeTable.fetchData(criteria,new DSCallback() {
//					
//					@Override
//					public void execute(DSResponse response, Object rawData, DSRequest request) {
//						if(feeTable.getRecords().length > 0){
//		    				feeTable.selectRecord(feeTable.getRecord(0));
//						}else{
//							basInfo2.clearValues();
//							groupTable.setRecords(nullRecord);
//						}
//					}
//				});
//			}
//		});
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				OP_FLAG = "M";
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				basInfo.setValue("OP_FLAG", "M");
				
				if(isMax) {
					expend();
				}
			}
		});
		
		
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
					final ListGridRecord []records=listGrid.getRecords();
					for(int k=0;k<records.length;k++){
						records[k].setAttribute("YN_FLAG",false);
						records[k].setAttribute("SHOW_SEQ","");
					}
					listGrid.redraw();
					Util.db_async.getRecord("FACTOR_ID,SHOW_SEQ", "TARIFF_HEADER_FACTOR", " where TFF_ID='"+record.getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							if(result!=null&&result.size()>0){
								for(int i=0;i<result.size();i++){
									HashMap<String,String> map=result.get(i);
									
									for(int j=0;j<records.length;j++){
									
										if(records[j].getAttribute("ID").equals(map.get("FACTOR_ID"))){
											
											records[j].setAttribute("YN_FLAG",true);
											records[j].setAttribute("SHOW_SEQ",map.get("SHOW_SEQ"));
										}		
										
									}	
								}			
								listGrid.redraw();
							}							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
							
						}
					});
			
				}
			}
		});
		
	} 
	
	/**
	 * 
	 * @author fangliangmeng
	 */
	private void createFeeList(){
		

		listGrid=new ListGrid();
		listGrid.setWidth("100%");
		listGrid.setCanEdit(true);
		
		ListGridField YN_FLAG=new ListGridField("YN_FLAG","选择",60);
		YN_FLAG.setType(ListGridFieldType.BOOLEAN);
		//YN_FLAG.setCanEdit(false);
		
		ListGridField RecordNO=new ListGridField("RecordNO","行号",80);
		RecordNO.setCanEdit(false);
		
		ListGridField FEE_FACTOR=new ListGridField("FEE_FACTOR","计费要素名称",180);
		FEE_FACTOR.setCanEdit(false);
			
//		ListGridField FEE_FIELD=new ListGridField("FEE_FIELD","映射费率字段",120);
//		FEE_FIELD.setCanEdit(false);
		
		ListGridField SHOW_SEQ=new ListGridField("SHOW_SEQ","显示顺序",120);
		SHOW_SEQ.setCanEdit(true);
		
		ListGridField ID=new ListGridField("ID","ID");
		ID.setHidden(true);
		
		//listGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		listGrid.setFields(YN_FLAG,RecordNO,ID,FEE_FACTOR,SHOW_SEQ);
		
		
		
		//String sql="select * from TARIFF_FACTOR where FEE_TYPE='42666CA2DE904F6687FC172138CF3E51'";
		
		Util.db_async.getRecord("ID,FEE_FACTOR", "TARIFF_FACTOR", " where FEE_TYPE='42666CA2DE904F6687FC172138CF3E51' ", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				if(result!=null&&result.size()>0){
					
					ListGridRecord [] records=new ListGridRecord[result.size()];		
					
					
					for(int i=0;i<result.size();i++){
					
						HashMap<String,String> map=result.get(i);
						ListGridRecord record=new ListGridRecord();
						record.setAttribute("RecordNO",i+1);
						record.setAttribute("FEE_FACTOR", map.get("FEE_FACTOR"));
						//record.setAttribute("FEE_FIELD", map.get("FEE_FIELD"));
						//record.setAttribute("SHOW_SEQ",i+1);
						record.setAttribute("ID", map.get("ID"));
						//record.
						records[i]=record;
					}
					
					listGrid.setRecords(records);
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
		
		
		
		
		
		
		
		feeTable.setCanEdit(false);
		feeTable.setShowFilterEditor(false);
		feeTable.setFilterOnKeypress(true);
		feeTable.setSelectionType(SelectionStyle.SINGLE);
		
		//ListGridField PRIORTY = new ListGridField("PRIORTY", Util.TI18N.PRIORTY(), 100);//优先级
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 80);//费用名城
		ListGridField FEE_BASE_NAME = new ListGridField("FEE_BASE_NAME", Util.TI18N.FEE_BASE_NAME(),110);//计费基准
		ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_ID_NAME", Util.TI18N.START_AREA_ID_NAME(),70);//起点区域
		ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_ID_NAME", Util.TI18N.END_AREA_ID_NAME(),70);//协议类型
		ListGridField GRADE_BASE = new ListGridField("GRADE_BASE_NAME", Util.TI18N.GRADE_BASE(),120);//协议对象
		ListGridField LOWER_LMT_OPRT = new ListGridField("LOWER_LMT_OPRT_NAME", Util.TI18N.LOWER_LMT_OPRT(),90);//上限运算符
		ListGridField LOWER_LMT = new ListGridField("LOWER_LMT", Util.TI18N.LOWER_LMT(),80);//上限值
		ListGridField UPPER_LMT_OPRT = new ListGridField("UPPER_LMT_OPRT_NAME", Util.TI18N.UPPER_LMT_OPRT(),90);//下线运算符
		ListGridField UPPER_LMT = new ListGridField("UPPER_LMT", Util.TI18N.UPPER_LMT(),80);//下限值
		
		ListGridField BASE_RATE = new ListGridField("BASE_RATE", Util.TI18N.BASE_RATE(),80);//基本费率
		ListGridField BASE_AMT = new ListGridField("BASE_AMT", Util.TI18N.BASE_AMT(),80);//基数金额
		ListGridField MIN_UNIT = new ListGridField("MIN_UNIT", Util.TI18N.MIN_UNIT(),80);//最小计算单位
		ListGridField MIN_AMT = new ListGridField("MIN_AMT", Util.TI18N.MIN_AMT(),80);//最低金额
		ListGridField MAX_AMT = new ListGridField("MAX_AMT", Util.TI18N.MAX_AMT(),80);//最高金额
		ListGridField BIZ_TYP = new ListGridField("BIZ_TYP_NAME", Util.TI18N.BIZ_TYP(),65);//最高金额
		
		ListGridField WHR_TRANS_SRVC_ID = new ListGridField("WHR_TRANS_SRVC_ID", "车辆类型",80);//最高金额
		Util.initComboValue(WHR_TRANS_SRVC_ID, "bas_vehicle_type", "id", "vehicle_type","","");
		//ListGridField TRANS_SRVC_ID_NAME=new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),65);
		
		Util.initFloatListField(LOWER_LMT, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UPPER_LMT, StaticRef.GWT_FLOAT);
		Util.initFloatListField(BASE_AMT, StaticRef.PRICE_FLOAT);
		Util.initFloatListField(MIN_AMT, StaticRef.PRICE_FLOAT);
		Util.initFloatListField(MAX_AMT, StaticRef.PRICE_FLOAT);
		
		feeTable.setFields(START_AREA_ID_NAME,END_AREA_ID_NAME,WHR_TRANS_SRVC_ID,BIZ_TYP,FEE_NAME,FEE_BASE_NAME,GRADE_BASE,LOWER_LMT_OPRT,
				LOWER_LMT,UPPER_LMT_OPRT,UPPER_LMT,BASE_RATE,BASE_AMT,MIN_UNIT,MIN_AMT,MAX_AMT);
		feeTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				selectRuleRecord = event.getRecord();
				vm.editRecord(event.getRecord());
				vm.setValue("OP_FLAG",StaticRef.MOD_FLAG);
	            initSaveRuleBtn();
	            
	            Criteria criteria = new Criteria();
        		criteria.addCriteria("OP_FLAG","M");
                criteria.addCriteria("RUL_ID",selectRuleRecord.getAttributeAsString("ID"));
                groupTable.fetchData(criteria);
			}
		});
//		feeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//			
//			@Override
//			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
//				initAddRuleBtn();
//				//basInfo2.setValue("OP_FLAG", StaticRef.MOD_FLAG);
//				vm.setValue("OP_FLAG",StaticRef.MOD_FLAG);
//				
//				if(isMax1) {
//					feeStack.setWidth("40%");
//					bar.setBackgroundImage(StaticRef.ICON_GORIGHT);
//					layOut2.setVisible(true);
//				}
//				isMax1 = !isMax1;
//			}
//		});
	}
	

	private void createbottoInfo() {
		// 货品明细
		groupTable = new SGTable(conditionDS);
		groupTable.setHeight("80%");
		groupTable.setShowFilterEditor(false);
		groupTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupTable.setShowRowNumbers(false);
		groupTable.setShowGridSummary(true);
		groupTable.setCanEdit(true);
		
		
		ListGridField ROW = new ListGridField("SHOW_SEQ","次序",30);
		ListGridField LEFT_BRKT = new ListGridField("LEFT_BRKT", Util.TI18N.LEFT_BRKT(), 80);
		final ListGridField OPER_OBJ = new ListGridField("OPER_OBJ", Util.TI18N.OPER_OBJ(), 120);
		final ListGridField OPER_ATTR = new ListGridField("OPER_ATTR", Util.TI18N.OPER_ATTR(), 120);
		ListGridField OPERATOR = new ListGridField("OPERATOR", Util.TI18N.OPERATOR(), 100);
		ListGridField ATTR_VAL = new ListGridField("ATTR_VAL", Util.TI18N.ATTR_VAL(), 100);
		ListGridField RIGHT_BRKT = new ListGridField("RIGHT_BRKT",Util.TI18N.RIGHT_BRKT(),80);
		ListGridField LINK = new ListGridField("LINK", Util.TI18N.LINK(), 90);

		groupTable.setFields(ROW,LEFT_BRKT, OPER_OBJ,OPER_ATTR, OPERATOR,ATTR_VAL,RIGHT_BRKT, LINK);
		
		LinkedHashMap<String, String> lb_hm = new LinkedHashMap<String, String>();
		lb_hm.put(" ", "");
		lb_hm.put("(", "(");
		LEFT_BRKT.setValueMap(lb_hm);
		
		LinkedHashMap<String, String> rb_hm = new LinkedHashMap<String, String>();
		rb_hm.put(" ", "");
		rb_hm.put(")", ")");
		RIGHT_BRKT.setValueMap(rb_hm);
		
		LinkedHashMap<String, String> lj_hm = new LinkedHashMap<String, String>();
		lj_hm.put(" ", "");
		lj_hm.put("AND", "AND");
		lj_hm.put("OR", "OR");
		LINK.setValueMap(lj_hm);
		
		Util.initComboValue(OPERATOR, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPERATOR'", "");
		
		Util.initComboValue(OPER_OBJ, "BAS_CODES", "ID", "NAME_C", " WHERE PROP_CODE='VIEW_TYP'", "");
		
		Util.initDefinalCombo(OPER_ATTR, "select COLUMN_NAME from user_tab_columns where TABLE_NAME = 'V_FEE_ORDER'", "COLUMN_NAME", "COLUMN_NAME", "");
		
		OPER_OBJ.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Util.initDefinalCombo(OPER_ATTR, "select COLUMN_NAME from user_tab_columns where TABLE_NAME = '" + event.getValue().toString() + "'", "COLUMN_NAME", "COLUMN_NAME", "");
			}
		});
		
		groupTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddCondBtn();
				table.OP_FLAG = "M";
			}
		});
		groupTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_cond_map, true);
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
					searchWin = new SearchWin(380,ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		
		// 新增按钮
		newButton = createBtn(StaticRef.CREATE_BTN,SettPrivRef.StandardRate_P0_01);
		newButton.addClickHandler(new NewTariffStandAction(basInfo, cache_map,this));
		
		final HashMap<String, String> map = new HashMap<String, String>();
		map.put("OBJECT_ID", "OBJECT_NAME");
		
		// 保存按钮
		saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.StandardRate_P0_02);
//		saveButton.addClickHandler(new SaveFormAction(table, basInfo, check_map, this,map));
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
//				if(!ObjUtil.isNotNull(OBJECT_ID.getValue()) && "FALSE".equalsIgnoreCase(COM_FLAG.getValue().toString())){
//					MSGUtil.sayError("非通用的协议对象不能为空");
//					return;
//				}
				new SaveFormAction(table, basInfo, check_map, getThis(),map).onClick(event);
			}
		});

		// 删除按钮
		delButton = createBtn(StaticRef.DELETE_BTN,SettPrivRef.StandardRate_P0_03);
		delButton.addClickHandler(new DeleteFormAction(table, basInfo));

		// 取消按钮
		canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.StandardRate_P0_04);
		canButton.addClickHandler(new CancelTariffAction(table, basInfo, this));
		
		
		IButton jfSaveButton=createBtn("保存设置",SettPrivRef.StandardRate_P0_05);
		jfSaveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord tRecord=table.getSelectedRecord();
				
				if(tRecord!=null){
					ArrayList<String> sqlList=new ArrayList<String>();
					
					String TFF_ID=tRecord.getAttribute("ID");
			
					String sql="delete from TARIFF_HEADER_FACTOR where TFF_ID='"+TFF_ID+"'";
					
					sqlList.add(sql);
					ListGridRecord[] records=listGrid.getRecords();
				
					if(records!=null&&records.length>0){
				
						for(int i=0;i<records.length;i++){
							if(records[i].getAttribute("YN_FLAG").equals("true"))
							{
								String FACTOR_ID=records[i].getAttribute("ID");
								//String FACTOR_FIELD=records[i].getAttribute("FEE_FIELD");
								String SHOW_SEQ=records[i].getAttribute("SHOW_SEQ");
								
								String sqlL="insert into TARIFF_HEADER_FACTOR(TFF_ID,FACTOR_ID,SHOW_SEQ) VALUES ('"+TFF_ID+"','"+FACTOR_ID+"',"+SHOW_SEQ+")";
								
								sqlList.add(sqlL);
							}
						}
						
						
					}
					Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
						
						@Override
						public void onSuccess(String result) {
							if(result.subSequence(0, 2).equals("00")){
							    MSGUtil.showOperSuccess();
								
							}else{
								MSGUtil.showOperError();
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
					
				}
				
				
			}
		});
		
		
		
		add_map.put(SettPrivRef.StandardRate_P0_01, newButton);
		del_map.put(SettPrivRef.StandardRate_P0_02, delButton);
		save_map.put(SettPrivRef.StandardRate_P0_03, saveButton);
		save_map.put(SettPrivRef.StandardRate_P0_04, canButton);
	
		//按钮状态控制
		this.enableOrDisables(add_map, true);
		this.enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,newButton,saveButton,delButton,canButton,jfSaveButton);

	}
	
	private ToolStrip createFeeBtn(){
		ToolStrip bottleftStrip = new ToolStrip();//按钮布局
		bottleftStrip.setAlign(Alignment.RIGHT);
		bottleftStrip.setWidth("100%");
		bottleftStrip.setHeight("20");
		bottleftStrip.setPadding(2);
		bottleftStrip.setSeparatorSize(12);
		bottleftStrip.addSeparator();
		
		copyButton = createBtn(StaticRef.COPY_BTN,SettPrivRef.StandardRate_P1_01);
		copyButton.addClickHandler(new CopyAction(basInfo2, this));
		
		newB = createBtn(StaticRef.CREATE_BTN,SettPrivRef.StandardRate_P1_02);
		newB.addClickHandler(new NewTariffRuleStandAction(vm, fee_cache_map,getThis()));
		
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("FEE_ID", "FEE_NAME");
//		map.put("FEE_BASE", "FEE_BASE_NAME");
//		map.put("FEE_TYP", "FEE_TYP_NAME");
	
		savB = createBtn(StaticRef.SAVE_BTN,SettPrivRef.StandardRate_P1_03);
		savB.addClickHandler(new saveTaffRulStandAction(feeTable, vm, detail_ck_map,this));
		
		delB = createBtn(StaticRef.DELETE_BTN,SettPrivRef.StandardRate_P1_04);
		delB.addClickHandler(new DeleteFormAction(feeTable, basInfo2));
		
		canB = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.StandardRate_P1_05);
		canB.addClickHandler(new CancelFormAction(feeTable, basInfo2));
		canB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initCancelRuleBtn();
			}
		});	
		
		
		add_detail_map = new HashMap<String, IButton>();
		del_detail_map = new HashMap<String, IButton>();
		sav_detail_map = new HashMap<String, IButton>();
		
		add_detail_map.put(SettPrivRef.StandardRate_P1_01, newB);
		add_detail_map.put(SettPrivRef.StandardRate_P1_02, copyButton);
		del_detail_map.put(SettPrivRef.StandardRate_P1_03, delB);
		sav_detail_map.put(SettPrivRef.StandardRate_P1_04, savB);
		sav_detail_map.put(SettPrivRef.StandardRate_P1_05, canB);
		
		this.enableOrDisables(add_detail_map, true);
		this.enableOrDisables(sav_detail_map, false);
		this.enableOrDisables(del_detail_map, false);
		
		bottleftStrip.setMembersMargin(4);
	    bottleftStrip.setMembers(copyButton,newB, savB, delB, canB);
	    
	    return bottleftStrip;
	}
	
	private ToolStrip createConditionBtn(){
		ToolStrip bottleftStrip = new ToolStrip();//按钮布局
		bottleftStrip.setAlign(Alignment.RIGHT);
		bottleftStrip.setWidth("100%");
		bottleftStrip.setHeight("20");
		bottleftStrip.setPadding(2);
		bottleftStrip.setSeparatorSize(12);
		bottleftStrip.addSeparator();
		
		newC = createBtn(StaticRef.CREATE_BTN,SettPrivRef.StandardRate_P2_01);
		newC.addClickHandler(new NewTariffCondStandAction(groupTable, null, this));
		
		savC = createBtn(StaticRef.SAVE_BTN,SettPrivRef.StandardRate_P2_02);
		savC.addClickHandler(new SaveAction(groupTable));
		savC.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initSaveCondBtn();
			}
		});
		
		delC = createBtn(StaticRef.DELETE_BTN,SettPrivRef.StandardRate_P2_03);
		delC.addClickHandler(new DeleteAction(groupTable));
		
		canC = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.StandardRate_P2_04);
		canC.addClickHandler(new CancelAction(groupTable));
		canC.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initCancelCondBtn();
			}
		});
		
		add_cond_map = new HashMap<String, IButton>();
		del_cond_map = new HashMap<String, IButton>();
		sav_cond_map = new HashMap<String, IButton>();
		
		add_cond_map.put(SettPrivRef.StandardRate_P2_01, newC);
		del_cond_map.put(SettPrivRef.StandardRate_P2_02, delC);
		sav_cond_map.put(SettPrivRef.StandardRate_P2_03, savC);
		sav_cond_map.put(SettPrivRef.StandardRate_P2_04, canC);
		
		this.enableOrDisables(add_cond_map, true);
		this.enableOrDisables(del_cond_map, false);
		this.enableOrDisables(sav_cond_map, false);
		
		
		bottleftStrip.setMembersMargin(4);
	    bottleftStrip.setMembers(newC, savC, delC, canC);
	    
	    return bottleftStrip;
	}
	
	protected DynamicForm createSearchForm(DynamicForm form) {
		
		form.setDataSource(ds);
		
		//第一行：模糊查询
		SGText txt_global=new SGText("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(249);
		txt_global.setColSpan(2);
		txt_global.setVisible(false);
//		txt_global.setEndRow(true);
		
		final TextItem WHR_START_AREA_ID=new TextItem("WHR_START_AREA_ID");
		WHR_START_AREA_ID.setVisible(false);
		ComboBoxItem WHR_START_AREA_NAME=new ComboBoxItem("WHR_START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());
		WHR_START_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(WHR_START_AREA_NAME, WHR_START_AREA_ID);
		WHR_START_AREA_NAME.setVisible(false);
//		WHR_START_AREA_NAME.addBlurHandler(new BlurHandler() {
//			@Override
//			public void onBlur(BlurEvent event) {
//				START_AREA=WHR_START_AREA_ID.getDisplayValue();
//			}
//		});
		
		final TextItem WHR_END_AREA_ID=new TextItem("WHR_END_AREA_ID");
		WHR_END_AREA_ID.setVisible(false);
		ComboBoxItem WHR_END_AREA_NAME=new ComboBoxItem("WHR_END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());
		WHR_END_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initArea(WHR_END_AREA_NAME, WHR_END_AREA_ID);
		WHR_END_AREA_NAME.setVisible(false);
//		WHR_END_AREA_NAME.addBlurHandler(new BlurHandler() {
//			@Override
//			public void onBlur(BlurEvent event) {
//				END_AREA=WHR_END_AREA_ID.getDisplayValue();
//			}
//		});
		
		final SGCombo TRANS_SRVC_ID=new SGCombo("TRANS_SRVC_ID",Util.TI18N.TRANS_SRVC_ID());
		Util.initTrsService(TRANS_SRVC_ID, "");
		TRANS_SRVC_ID.setVisible(false);
//		TRANS_SRVC_ID.addBlurHandler(new BlurHandler() {
//			@Override
//			public void onBlur(BlurEvent event) {
//				TRANS_SRVC=TRANS_SRVC_ID.getValue().toString();
//			}
//		});
		
		final SGCombo BIZ_TYP=new SGCombo("BIZ_TYP",Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		BIZ_TYP.setVisible(false);
//		BIZ_TYP.addBlurHandler(new BlurHandler() {
//			@Override
//			public void onBlur(BlurEvent event) {
//				BIZ=BIZ_TYP.getValue().toString();
//			}
//		});
		
		final SGCombo TFF_TYP = new SGCombo("TFF_TYP", Util.TI18N.TFF_TYP(),true);
		Util.initCodesComboValue(TFF_TYP, "TRANS_TFF_TYP","42666CA2DE904F6687FC172138CF3E51");
		TFF_TYP.setDisabled(true);
		TFF_TYP.setVisible(false);
		
		final ComboBoxItem OBJECT_ID = new ComboBoxItem("OBJECT_ID", Util.TI18N.OBJECT_NAME());
		OBJECT_ID.setColSpan(2);
		OBJECT_ID.setWidth(FormUtil.Width);
		OBJECT_ID.setTitleOrientation(TitleOrientation.TOP);
		Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "SHORT_NAME"," WHERE ENABLE_FLAG = 'Y'", "");
		
		/*TFF_TYP.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(TFF_TYP.getValue().equals(StaticRef.TRANS_TFF_TYP)){
					Util.initComboValue(OBJECT_ID, "BAS_CUSTOMER", "ID", "CUSTOMER_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}else{
					Util.initComboValue(OBJECT_ID, "BAS_SUPPLIER", "ID", "SUPLR_ENAME"," WHERE ENABLE_FLAG = 'Y'", "");
				}
			}
		});*/
		
		SGDate START_DATE = new SGDate("START_DATE", Util.TI18N.FEE_START_DATE(),true);
		
		SGDate END_DATE = new SGDate("END_DATE", Util.TI18N.FEE_END_DATE());
		
		SGCombo DOC_TYP = new SGCombo("DOC_TYP", Util.TI18N.DOC_TYP(),false);
		Util.initCodesComboValue(DOC_TYP, "DOC_TYP");
		
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG(),true);	
		C_ORG_FLAG.setValue(true);//包含下级机构
		
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "30%", "40%");

		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());	
		chk_enable.setValue(true);
		
		SGCheck COM_FLAG = new SGCheck("COM_FLAG", "标识");	
		COM_FLAG.setValue("Y");
		COM_FLAG.setVisible(false);
//		chk_enable.setColSpan(2);
		
		TextItem TFF_FLAG = new TextItem("TFF_FLAG");
		TFF_FLAG.setValue("Y");
		TFF_FLAG.setVisible(false);
		
//		final SGCheck COM_FLAG=new SGCheck("COM_FLAG","通用");
//		COM_FLAG.setValue(false);
		
//		OBJECT_ID.addChangedHandler(new ChangedHandler() {
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if(ObjUtil.isNotNull(OBJECT_ID.getValue())){
//					COM_FLAG.setValue(false);
//				}
//			}
//		});
//		COM_FLAG.addChangedHandler(new ChangedHandler() {
//			@Override
//			public void onChanged(ChangedEvent event) {
//				if("TRUE".equalsIgnoreCase(COM_FLAG.getValueAsBoolean().toString())){
//					OBJECT_ID.setValue("");
//				}
//			}
//		});
		SGText TFF_NAME=new SGText("TFF_NAME",Util.TI18N.TFF_NAME());
		
		form.setItems(COM_FLAG,txt_global,WHR_START_AREA_ID,WHR_START_AREA_NAME,WHR_END_AREA_ID,WHR_END_AREA_NAME,TRANS_SRVC_ID,BIZ_TYP,
				TFF_TYP,TFF_NAME,OBJECT_ID,DOC_TYP,START_DATE,END_DATE,EXEC_ORG_ID_NAME,C_ORG_FLAG,EXEC_ORG_ID,TFF_FLAG,chk_enable);
		return form;
		
	}

	public void createForm(DynamicForm form) {

	}

	/**
	 * 检查，初始化
	 * fangliangmeng
	 */
	public void initVerify() {
		check_map.put("TABLE", "TARIFF_HEADER");
		check_map.put("START_DATE", StaticRef.CHK_DATE + Util.TI18N.FEE_START_DATE());
		check_map.put("END_DATE", StaticRef.CHK_DATE + Util.TI18N.FEE_END_DATE());
		//check_map.put("DOC_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.DOC_TYP());
		//check_map.put("EXEC_ORG_ID", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		//check_map.put("EXEC_ORG_ID_NAME", StaticRef.CHK_NOTNULL + Util.TI18N.EXEC_ORG_ID());
		check_map.put("TFF_TYP",  StaticRef.CHK_NOTNULL + Util.TI18N.TFF_TYP());
//		check_map.put("OBJECT_ID",  StaticRef.CHK_NOTNULL + Util.TI18N.OBJECT_ID());
		check_map.put("TFF_NAME", StaticRef.CHK_NOTNULL+Util.TI18N.TFF_NAME());
		
		detail_ck_map = new HashMap<String, String>();
		detail_ck_map.put("TABLE", "TARIFF_RULE");
		detail_ck_map.put("FEE_ATTR", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_ATTR());
		detail_ck_map.put("FEE_ID", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_ID());
		detail_ck_map.put("FEE_BASE", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_BASE());
//		detail_ck_map.put("CARRY_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.CARRY_TYP());
		detail_ck_map.put("FEE_TYP", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_TYP());
		detail_ck_map.put("GRADE_BASE", StaticRef.CHK_NOTNULL + Util.TI18N.GRADE_BASE());
		detail_ck_map.put("BASE_RATE", StaticRef.CHK_NOTNULL + Util.TI18N.BASE_RATE());

		cache_map.put("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("SIGN_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		cache_map.put("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("SIGN_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("ADDWHO", LoginCache.getLoginUser().getUSER_ID());
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("TFF_FLAG", "Y");
		cache_map.put("COM_FLAG", "Y");
		cache_map.put("TFF_TYP", "42666CA2DE904F6687FC172138CF3E51");  //客户

		fee_cache_map = new HashMap<String, String>();
		fee_cache_map.put("LOWER_LMT_OPRT", ">=");
		fee_cache_map.put("LOWER_LMT", "0.00");
		fee_cache_map.put("UPPER_LMT_OPRT", "<");
		fee_cache_map.put("UPPER_LMT", "999999.99");
		fee_cache_map.put("MIN_AMT", "0.00");
		fee_cache_map.put("MAX_AMT","999999.99");
		fee_cache_map.put("ENABLE_FLAG", "Y");
		fee_cache_map.put("FEE_ATTR","53EB6809BFCC436799F735AAE23658B9");  //应收费用
		
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}
	
	public void initAddRuleBtn(){
		enableOrDisables(add_detail_map, false);
		enableOrDisables(del_detail_map, false);
		enableOrDisables(sav_detail_map, true);
	}
	
	public void initSaveRuleBtn(){
		enableOrDisables(add_detail_map, true);
		enableOrDisables(del_detail_map, true);
		enableOrDisables(sav_detail_map, false);
	}
	public void initCancelRuleBtn(){
		enableOrDisables(add_detail_map, true);
		enableOrDisables(del_detail_map, false);
		enableOrDisables(sav_detail_map, false);
	}
	
	public void initAddCondBtn(){
		enableOrDisables(add_cond_map, false);
		enableOrDisables(del_cond_map, false);
		enableOrDisables(sav_cond_map, true);
	}
	
	public void initSaveCondBtn(){
		enableOrDisables(add_cond_map, true);
		enableOrDisables(del_cond_map, true);
		enableOrDisables(sav_cond_map, false);
	}
	public void initCancelCondBtn(){
		enableOrDisables(add_cond_map, true);
		enableOrDisables(del_cond_map, false);
		enableOrDisables(sav_cond_map, false);
	}
	
	public StandardRateView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		StandardRateView view = new StandardRateView();
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
