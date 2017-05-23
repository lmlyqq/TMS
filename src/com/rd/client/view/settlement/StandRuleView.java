package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.settlement.DeleteStandRuleAction;
import com.rd.client.action.settlement.NewStandDiscountAction;
import com.rd.client.action.settlement.NewStandRuleActiom;
import com.rd.client.action.settlement.SaveStandRuleAction;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.CopyAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGSCombo;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.VCAreaDS;
import com.rd.client.ds.settlement.TariffRuleDS;
import com.rd.client.ds.settlement.TarrifDiscountDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.FeeRuleWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VisibilityMode;
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
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 计费管理系统--应收管理--标准费率设置
 * @author Administrator
 *
 */
@ClassForNameAble
public class StandRuleView extends SGForm implements PanelFactory {

	private DataSource ds;
	private DataSource discountDS;
	public SGTable table;
	public SGTable discountTable;
	private SectionStack section;
	private SectionStack sectionStack;
	public SGPanel basInfo;
	private SGPanel basInfo2;
	public SGPanel isG;
	public SGPanel TFFNAME;
	public ValuesManager vm;
	private Window searchWin;
	public SGPanel searchForm = new SGPanel();
	
	private IButton newB;
	private IButton delB;
	private IButton savB;
	private IButton canB;
	
	private IButton newD;
	private IButton delD;
	private IButton savD;
	private IButton canD;
	public IButton copyButton;
	
	public HashMap<String, String> discount_ck_map;
	
	public HashMap<String, IButton> add_detail_map;
	public HashMap<String, IButton> del_detail_map;
	public HashMap<String, IButton> sav_detail_map;
	
	public HashMap<String, IButton> add_discount_map;
	public HashMap<String, IButton> del_discount_map;
	public HashMap<String, IButton> sav_discount_map;
	
	private ArrayList<HashMap<String, String>> maps;
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = TariffRuleDS.getInstance("V_TARIFF_RULE","TARIFF_RULE");
	    discountDS=TarrifDiscountDS.getInstance("TARIFF_ADDITIONAL","TARIFF_ADDITIONAL");
	    
	    initVerify();
	    
	    HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
	    
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		sectionStack = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection("列表信息");
	    listItem.setItems(table);
	    listItem.setExpanded(true);
		listItem.setControls(new SGPage(table, true).initPageBtn());
		sectionStack.addSection(listItem);
		sectionStack.setWidth("100%");
	    
	    createListField();
		stack.addMember(sectionStack);
		addSplitBar(stack);
		
//		VLayout v_lay = new VLayout();
//		v_lay.setWidth("80%");
//		v_lay.setHeight100();
//		v_lay.setBackgroundColor(ColorUtil.BG_COLOR);
//		v_lay.setVisible(false);
		 
		TabSet leftTabSet = new TabSet();  
		leftTabSet.setWidth("100%");   
		leftTabSet.setHeight("100%"); 
		leftTabSet.setMargin(0);
		
		VLayout FeeInfo = new VLayout(); //订单明细栏
		FeeInfo.setWidth100();
		FeeInfo.addMember(createFeeInfo());
		
		VLayout discountItem = new VLayout(); //折扣 附件
		discountItem.setWidth100();
		discountItem.addMember(createDiscountTable());
		discountItem.addMember(createDiscountBtn());
		
		Tab tab1 = new Tab("费率设置");
		tab1.setPane(FeeInfo);
		leftTabSet.addTab(tab1);
		
	    Tab tab2 = new Tab("折扣与附加");
	    tab2.setPane(discountItem);
	    leftTabSet.addTab(tab2);
		
	    VLayout layOut = new VLayout();
		layOut.setWidth("80%");
		layOut.setHeight("100%");
		layOut.addMember(leftTabSet);
		layOut.setVisible(false);
		stack.addMember(layOut);
		
		leftTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				if(table!=null){
					ListGridRecord record=table.getSelectedRecord();
					if(record!=null){
						
						Criteria criteria=new Criteria();
						criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
		        		criteria.addCriteria("TFF_ID",record.getAttributeAsString("TFF_ID"));
		        		criteria.addCriteria("RUL_ID",record.getAttributeAsString("ID"));
		        		
		        		discountTable.fetchData(criteria);
								
					}
				}
			}
		});
		
		table.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				enableOrDisables(add_detail_map, false);
				enableOrDisables(del_detail_map, false);
				enableOrDisables(sav_detail_map, true);
				
				if(isMax) {
					expend();
				}
			}
		});
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record rec = event.getRecord();
				vm.editRecord(rec);
				enableOrDisables(add_detail_map, true);
				enableOrDisables(del_detail_map, true);
				enableOrDisables(sav_detail_map, false);
				
				if(rec!=null){
					Criteria criteria=new Criteria();
					criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
	        		criteria.addCriteria("TFF_ID",rec.getAttributeAsString("TFF_ID"));
	        		criteria.addCriteria("RUL_ID",rec.getAttributeAsString("ID"));
	        		discountTable.fetchData(criteria);
				}
				
				if(event.getRecord().getAttribute("TFF_ID")!=null){
					dosearch();
					/*HashMap<String,String> map = new HashMap<String,String>();
					Util.db_async.getRecord("FACTOR_ID,FEE_FACTOR,OBJ_TYPE,DICT_PARAM,DATA_FROM,DATA_ID,DATA_NAME", "TARIFF_FACTOR T,TARIFF_HEADER_FACTOR T1", " where T.ID=T1.FACTOR_ID and T1.TFF_ID='"+event.getRecord().getAttribute("TFF_ID")+"' and T.FEE_TYPE = '42666CA2DE904F6687FC172138CF3E51'", map,new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							dosearch();
							if(result != null && result.size() > 0 && maps != null && maps.size()>0) {
								if(basInfo.getFields().length > 0) {
									FormItem item = new FormItem();
									basInfo.setItems(item);
								}
								
								System.out.println(basInfo.getFields().length);
								ArrayList<FormItem> list = new ArrayList<FormItem>();
								SGSCombo[] leftCombos = new SGSCombo[result.size()];  //左括号
								SGText[] operCombos = new SGText[result.size()];   //计费因子名称
								SGCombo[] operatorCombos = new SGCombo[result.size()];  //操作符号
								SGSCombo[] rightCombos = new SGSCombo[result.size()];  //右括号
								SGSCombo[] linkCombos = new SGSCombo[result.size()];   //连接符AND/OR
								ComboBoxItem[] attTexts = new ComboBoxItem[result.size()];
								for(int j = 0; j < result.size(); j++){
									HashMap<String, String> map = (HashMap<String, String>)result.get(j);
									if(basInfo.getItem("LEFT_BRKT"+Integer.toString(j+1)) != null) {
										leftCombos[j] = (SGSCombo)basInfo.getItem("LEFT_BRKT"+Integer.toString(j+1));
									}
									else {
										leftCombos[j] = new SGSCombo("LEFT_BRKT"+Integer.toString(j+1), "左括号"); 
									}
									leftCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("LEFT_BRKT"),""));
									leftCombos[j].setShowTitle(false);
									leftCombos[j].setVisible(true);
									System.out.println(map.get("FEE_FACTOR"));
									if(basInfo.getItem("OPER_OBJ"+Integer.toString(j+1)) != null) {
										System.out.println("Create IN!");
										operCombos[j] = (SGText)basInfo.getItem("OPER_OBJ"+Integer.toString(j+1));
									}
									else {
										System.out.println("NEW TEXT!");
										operCombos[j] = new SGText("OPER_OBJ"+Integer.toString(j+1), "计费因子");
									}
									operCombos[j].setShowTitle(false);
									operCombos[j].setDisabled(true);
									operCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("OPER_OBJ"),""));
									operCombos[j].setVisible(true);
									if(basInfo.getItem("OPERATOR"+Integer.toString(j+1)) != null) {
										operatorCombos[j] = (SGCombo)basInfo.getItem("OPERATOR"+Integer.toString(j+1));
									}
									else {
										operatorCombos[j] = new SGCombo("OPERATOR"+Integer.toString(j+1), "操作符");
									}								
									operatorCombos[j].setShowTitle(false);
									operatorCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("OPERATOR"),""));
									operatorCombos[j].setVisible(true);
									if(map.get("OBJ_TYPE").equals("C9595DE73AC34B3B8E715637C2C846DC")){
										if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
											attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
										}
										else {
											attTexts[j] = new ComboBoxItem();
											attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
											attTexts[j].setTitle("值");
										}
										attTexts[j].setType("TextItem");
										attTexts[j].setShowTitle(false);
										attTexts[j].setColSpan(2);
										attTexts[j].setWidth(FormUtil.Width);
										attTexts[j].setTitleOrientation(TitleOrientation.TOP);
										attTexts[j].setValue(ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
										attTexts[j].setVisible(true);
									}else{
										if(map.get("DATA_FROM").equals("BAS_AREA")){
											if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
												attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
											}
											else {
												attTexts[j] = new ComboBoxItem();
												attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
												attTexts[j].setTitle("值");
											}
											attTexts[j].setType("ComboBoxItem");
											attTexts[j].setShowTitle(false);
											attTexts[j].setColSpan(2);
											attTexts[j].setWidth(FormUtil.Width);
											attTexts[j].setTitleOrientation(TitleOrientation.TOP);
											TextItem AREA_ID = new TextItem("AREA_ID"+Integer.toString(j+1));
											AREA_ID.setVisible(false);
											Util.initArea(attTexts[j],AREA_ID);
										}else if(map.get("DATA_FROM").equals("BAS_CODES")){
											if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
												attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
											}
											else {
												attTexts[j] = new ComboBoxItem();
												attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
												attTexts[j].setTitle("值");
											}
											attTexts[j].setType("SelectItem");
											attTexts[j].setShowTitle(false);
											attTexts[j].setColSpan(2);
											attTexts[j].setWidth(FormUtil.Width);
											attTexts[j].setTitleOrientation(TitleOrientation.TOP);
											Util.initCodesComboValue(attTexts[j],map.get("DICT_PARAM"));
										}
										else {
											if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
												attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
											}
											else {
												attTexts[j] = new ComboBoxItem();
												attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
												attTexts[j].setTitle("值");
											}
											attTexts[j].setType("SelectItem");
											attTexts[j].setShowTitle(false);
											attTexts[j].setColSpan(2);
											attTexts[j].setWidth(FormUtil.Width);
											attTexts[j].setTitleOrientation(TitleOrientation.TOP);
											Util.initComboValue(attTexts[j],map.get("DATA_FROM"),map.get("DATA_ID"),map.get("DATA_NAME"));
										}
										attTexts[j].setVisible(true);
										attTexts[j].setValue(ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
									}
									if(basInfo.getItem("RIGHT_BRKT"+Integer.toString(j+1)) != null) {
										rightCombos[j] = (SGSCombo)basInfo.getItem("RIGHT_BRKT"+Integer.toString(j+1));
									}
									else {
										rightCombos[j] = new SGSCombo("RIGHT_BRKT"+Integer.toString(j+1), "右括号");
									}
									rightCombos[j].setShowTitle(false);
									rightCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("RIGHT_BRKT"),""));
									rightCombos[j].setVisible(true);
									if(basInfo.getItem("LINK"+Integer.toString(j+1)) != null) {
										linkCombos[j] = (SGSCombo)basInfo.getItem("LINK"+Integer.toString(j+1));
									}
									else {
										linkCombos[j] = new SGSCombo("LINK"+Integer.toString(j+1), "连接符");
									}
									linkCombos[j].setShowTitle(false);
									linkCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("LINK"),""));
									linkCombos[j].setVisible(true);
									
									LinkedHashMap<String, String> lb_hm = new LinkedHashMap<String, String>();
									lb_hm.put(" ", "");
									lb_hm.put("(", "(");
									leftCombos[j].setValueMap(lb_hm);
									
									LinkedHashMap<String, String> rb_hm = new LinkedHashMap<String, String>();
									rb_hm.put(" ", "");
									rb_hm.put(")", ")");
									rightCombos[j].setValueMap(rb_hm);
									
									LinkedHashMap<String, String> lj_hm = new LinkedHashMap<String, String>();
									lj_hm.put(" ", "");
									lj_hm.put("AND", "AND");
									lj_hm.put("OR", "OR");
									linkCombos[j].setValueMap(lj_hm);
									
									if(leftCombos[j]!=null){
										if(j==0)leftCombos[0].setShowTitle(true);
										list.add(leftCombos[j]);
									}
									if(operCombos[j]!=null){
										if(j==0)operCombos[0].setShowTitle(true);
										list.add(operCombos[j]);
									}
									if(operatorCombos[j]!=null){
										if(j==0)operatorCombos[0].setShowTitle(true);
										list.add(operatorCombos[j]);
									}
									if(attTexts[j]!=null){
										if(j==0)attTexts[0].setShowTitle(true);
										list.add(attTexts[j]);
									}
									if(rightCombos[j]!=null){
										if(j==0)rightCombos[0].setShowTitle(true);
										list.add(rightCombos[j]);
									}
									if(linkCombos[j]!=null){
										if(j==0)linkCombos[0].setShowTitle(true);
										list.add(linkCombos[j]);
									}
								}
								Util.initComboValue(operatorCombos, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'OPERATOR'", "");
								FormItem[] lists = (FormItem[])list.toArray(new FormItem[list.size()]);
								basInfo.reset();
								basInfo.setItems(lists);
								basInfo.redraw();
							}
						}

						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});*/
				}
				
			}
		});
		
		createBtnWidget(toolStrip);
		
		main.addMember(toolStrip);
		main.addMember(stack);
		return main;
	}
	
	private void dosearch(){
		if(table.getSelectedRecord() != null) {
			Util.db_async.getRecord("LEFT_BRKT,OPER_OBJ,OPERATOR,ATTR_VAL,RIGHT_BRKT,LINK,OPER_ATTR", "TARIFF_CONDITION", " where TFF_ID = '"+table.getSelectedRecord().getAttribute("TFF_ID")+"' and RUL_ID= '"+table.getSelectedRecord().getAttribute("ID")+"' order by SHOW_SEQ", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
				
				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					maps = result;
					SGCombo TFF_NAME = (SGCombo)TFFNAME.getItem("TFF_ID");
					doInitFactor(TFF_NAME.getValue(), maps);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
		}
	}
	
	private void createListField() {
		ListGridField PRIORTY = new ListGridField("PRIORTY", Util.TI18N.PRIORTY(), 100);//优先级
		ListGridField FEE_NAME = new ListGridField("FEE_NAME", Util.TI18N.FEE_NAME(), 80);//费用名城
		ListGridField FEE_BASE_NAME = new ListGridField("FEE_BASE_NAME", Util.TI18N.FEE_BASE_NAME(),110);//计费基准
		//ListGridField START_AREA_ID_NAME = new ListGridField("START_AREA_ID_NAME", Util.TI18N.START_AREA_ID_NAME(),70);//起点区域
		//ListGridField END_AREA_ID_NAME = new ListGridField("END_AREA_ID_NAME", Util.TI18N.END_AREA_ID_NAME(),70);//协议类型
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
		//ListGridField BIZ_TYP = new ListGridField("BIZ_TYP_NAME", Util.TI18N.BIZ_TYP(),65);//最高金额
		//ListGridField TRANS_SRVC_ID_NAME=new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),65);
		//String ord_id=LoginCache.getLoginUser().getDEFAULT_ORG_ID();
		ListGridField TFF_NAME=new ListGridField("TFF_NAME","计费协议",65);
		//Util.initComboValue(TFF_ID,"TARIFF_HEADER","ID","TFF_NAME","  where tff_typ = '42666CA2DE904F6687FC172138CF3E51' and COM_FLAG = 'N' and EXEC_ORG_ID IN (SELECT ID From BAS_ORG Where id ='"+ord_id+"'or ORG_INDEX Like '%,"+ord_id+",%') ","");
		//ListGridField TFF_ID=new ListGridField("TFF_ID","计费协议",65);
		//TFF_ID.setHidden(true);
		
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		table.setSelectionType(SelectionStyle.SINGLE);
		table.setFields(TFF_NAME,FEE_NAME,FEE_BASE_NAME,GRADE_BASE,LOWER_LMT_OPRT,
				LOWER_LMT,UPPER_LMT_OPRT,UPPER_LMT,BASE_RATE,BASE_AMT,MIN_UNIT,MIN_AMT,MAX_AMT,PRIORTY);
		
		
	
	}
	
	private SectionStack createFeeInfo(){
		
		VLayout vLay = new VLayout();
		vLay.setWidth100();
		vLay.setBackgroundColor(ColorUtil.BG_COLOR);
		//1、起点区域，终点区域，运输服务
		
		String ord_id=LoginCache.getLoginUser().getDEFAULT_ORG_ID();
		final SGCombo TFF_NAME = new SGCombo("TFF_ID","计费协议");
		Util.initComboValue(TFF_NAME,"TARIFF_HEADER","ID","TFF_NAME","  where COM_FLAG = 'Y' and EXEC_ORG_ID IN (SELECT ID From BAS_ORG Where id ='"+ord_id+"'or ORG_INDEX Like '%,"+ord_id+",%') ","");
		
//		TextItem WHR_START_AREA_ID = new TextItem("WHR_START_AREA_ID");
//		WHR_START_AREA_ID.setVisible(false);
//		
//		ComboBoxItem START_AREA_ID_NAME = new ComboBoxItem("START_AREA_ID_NAME", Util.TI18N.START_AREA_ID_NAME());//发货区域
//		START_AREA_ID_NAME.setWidth(FormUtil.Width);
//		START_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
//		Util.initArea(START_AREA_ID_NAME, WHR_START_AREA_ID);
//		
//		TextItem WHR_END_AREA_ID = new TextItem("WHR_END_AREA_ID");
//		WHR_END_AREA_ID.setVisible(false);
//		
//		ComboBoxItem END_AREA_ID_NAME = new ComboBoxItem("END_AREA_ID_NAME", Util.TI18N.END_AREA_ID_NAME());//收货区域
//		END_AREA_ID_NAME.setWidth(FormUtil.Width);
//		END_AREA_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
//		Util.initArea(END_AREA_ID_NAME, WHR_END_AREA_ID);
		
//		SGCombo TRANS_SRVC_ID = new SGCombo("WHR_TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());
//		Util.initTrsService(TRANS_SRVC_ID, "");
//		
//		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", ColorUtil.getRedTitle(Util.TI18N.BIZ_TYP()));
//		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		//2、费用属性，费用名称，计费基准，进位方式
		SGCombo FEE_ATTR = new SGCombo("FEE_ATTR", Util.TI18N.FEE_ATTR(),true);
		FEE_ATTR.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_ATTR()));
		Util.initCodesComboValue(FEE_ATTR, "FEE_ATTR");
		FEE_ATTR.setDisabled(true);
		FEE_ATTR.setVisible(false);
		
		SGCombo SUM_MODE = new SGCombo("SUM_MODE", "汇总方式");
		//FEE_ATTR.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_ATTR()));
		Util.initCodesComboValue(SUM_MODE, "FEE_BASE");

		
		
		final SGCombo FEE_ID = new SGCombo("FEE_ID", Util.TI18N.FEE_NAME(),true);
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
		
		//final SGCombo APPORT_BASE = new SGCombo("APPORT_BASE", "分摊基准");
		//Util.initComboValue(APPORT_BASE, "BAS_CODES", "CODE", "NAME_C", " where PROP_CODE = 'FEE_BASE'", "");
		
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
		final SGLText BASE_RATE = new SGLText("BASE_RATE", Util.TI18N.BASE_RATE(),true);
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
        
        
		
		SGText BASE_AMT = new SGText("BASE_AMT", Util.TI18N.BASE_AMT());
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
		
		SGCheck EXPRESS_FLAG = new SGCheck("EXPRESS_FLAG", "表达式");
//		EXPRESS_FLAG.setValue(false);
//		EXPRESS_FL
		
		FEE_BASE.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				GRADE_BASE.setValue(Util.iff(event.getValue(),"").toString());
//				APPORT_BASE.setValue(Util.iff(event.getValue(),"").toString());
			}
		});
		
		basInfo = new SGPanel();
		basInfo.setNumCols(9);
		basInfo.setWidth("65%");
		basInfo.setIsGroup(true);
		basInfo.setGroupTitle("限制条件");
		basInfo.setCellPadding(0);
		basInfo.setTitleWidth(75);
		
		
		TFF_NAME.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				doInitFactor(TFF_NAME.getValue(),null);
			}
		});
		TFFNAME = new  SGPanel();
		TFFNAME.setWidth("65%");
		TFFNAME.setCellPadding(0);
		TFFNAME.setTitleWidth(75);
		TFFNAME.setItems(TFF_NAME);
		
		basInfo2 = new SGPanel();
		basInfo2.setWidth("65%");
		basInfo2.setIsGroup(true);
		basInfo2.setGroupTitle("计费协议");
		basInfo2.setCellPadding(0);
		basInfo2.setTitleWidth(75);
		basInfo2.setItems(FEE_ATTR,FEE_ID,FEE_BASE,CARRY_TYP,SUM_MODE,UOM_UNIT,WEIGHT_UNIT,VOLUME_UNIT,FEE_TYP,GRADE_BASE,PRIORTY,
				ENABLE_FLAG,FEE_NAME,FEE_BASE_NAME,FEE_TYP_NAME);
		
		isG = new SGPanel();
		isG.setCellPadding(0);
		isG.setTitleWidth(75);
		isG.setWidth("65%");
		isG.setIsGroup(true);
		isG.setGroupTitle("级差");
		isG.setItems(LOWER_LMT_OPRT,LOWER_LMT,UPPER_LMT_OPRT,UPPER_LMT,BASE_RATE,EXPRESS_FLAG,BASE_AMT,MIN_AMT,MAX_AMT,MIN_UNIT);
	
		section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		section.addMember(TFFNAME);
		section.addMember(basInfo);
		section.addMember(basInfo2);
		section.addMember(isG);
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
		section.setAnimateSections(true);
		vLay.addMember(section);
		
		vm = new ValuesManager();
		vm.addMember(TFFNAME);
//		vm.addMember(basInfo);
		vm.addMember(basInfo2);
		vm.addMember(isG);
		
		return section;
	}
	
	private SGTable createDiscountTable(){
		
		discountTable= new SGTable(discountDS, "100%", "100%", false, true, false);
		
		ListGridField BIZ_DATEOBJ=new ListGridField("BIZ_DATEOBJ","基准日期",100);
		ListGridField ADJ_DATEOBJ=new ListGridField("ADJ_DATEOBJ","调整日期",100);
		Util.initDate(discountTable,ADJ_DATEOBJ);
		ListGridField ADJ_RATIO=new ListGridField("ADJ_RATIO","调整比例",100);
		ListGridField AVAIL_BY=new ListGridField("AVAIL_BY","生效期",100);
		Util.initCodesComboValue(AVAIL_BY,"AVAILABLE_DATE");
		
		
		discountTable.setFields(BIZ_DATEOBJ,ADJ_DATEOBJ,ADJ_RATIO,AVAIL_BY);
		discountTable.setCanEdit(true);
		
		discountTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddDiscountBtn();
				table.OP_FLAG = "M";
			}
		});
		
		discountTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_discount_map, true);
			}
		});
		
		return discountTable;
	}
	
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(3);
		strip.setSeparatorSize(5);
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		
		strip.addSeparator();

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
		
		newB = createBtn(StaticRef.CREATE_BTN,SettPrivRef.StandRule_P0_01);
		newB.addClickHandler(new NewStandRuleActiom(vm, cache_map,this));
		
		savB = createBtn(StaticRef.SAVE_BTN,SettPrivRef.StandRule_P0_02);
		savB.addClickHandler(new SaveStandRuleAction(table,vm,check_map,this));
		
		delB = createBtn(StaticRef.DELETE_BTN,SettPrivRef.StandRule_P0_03);
		delB.addClickHandler(new DeleteStandRuleAction(table, vm));
		
		canB = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.StandRule_P0_04);
		canB.addClickHandler(new CancelFormAction(table, basInfo2));
		canB.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initCancelRuleBtn();
			}
		});	
		
		copyButton = createBtn(StaticRef.COPY_BTN,SettPrivRef.StandRule_P0_09);
		copyButton.addClickHandler(new CopyAction(basInfo, this));
		
		
		add_detail_map = new HashMap<String, IButton>();
		del_detail_map = new HashMap<String, IButton>();
		sav_detail_map = new HashMap<String, IButton>();
		
		add_detail_map.put(SettPrivRef.StandRule_P0_01, newB);
		add_detail_map.put(SettPrivRef.StandRule_P0_09, copyButton);
		del_detail_map.put(SettPrivRef.StandRule_P0_03, delB);
		sav_detail_map.put(SettPrivRef.StandRule_P0_02, savB);
		sav_detail_map.put(SettPrivRef.StandRule_P0_04, canB);
		
		this.enableOrDisables(add_detail_map, true);
		this.enableOrDisables(sav_detail_map, false);
		this.enableOrDisables(del_detail_map, false);
		
		
		strip.setMembersMargin(4);
		strip.setMembers(searchButton,newB,copyButton, savB, delB, canB);
	}
	
	private ToolStrip createDiscountBtn(){
		
		ToolStrip botStrip = new ToolStrip();//按钮布局
		botStrip.setAlign(Alignment.RIGHT);
		botStrip.setWidth("100%");
		botStrip.setHeight("20");
		botStrip.setPadding(2);
		botStrip.setSeparatorSize(12);
		botStrip.addSeparator();
		
		newD = createBtn(StaticRef.CREATE_BTN,SettPrivRef.StandRule_P0_05);
		newD.addClickHandler(new NewStandDiscountAction(discountTable, null, this));
		newD.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initAddDiscountBtn();
			}
		});
		
		savD = createBtn(StaticRef.SAVE_BTN,SettPrivRef.StandRule_P0_06);
		savD.addClickHandler(new SaveAction(discountTable,discount_ck_map));
		savD.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initSaveDiscountBtn();
			}
		});
		
		delD = createBtn(StaticRef.DELETE_BTN,SettPrivRef.StandRule_P0_07);
		delD.addClickHandler(new DeleteAction(discountTable));
		
		canD = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.StandRule_P0_08);
		canD.addClickHandler(new CancelAction(discountTable));
		canD.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				initCancelDiscountBtn();
			}
		});
		
		add_discount_map = new HashMap<String, IButton>();
		del_discount_map = new HashMap<String, IButton>();
		sav_discount_map = new HashMap<String, IButton>();
		
		add_discount_map.put(SettPrivRef.StandRule_P0_05, newD);
		del_discount_map.put(SettPrivRef.StandRule_P0_07, delD);
		sav_discount_map.put(SettPrivRef.StandRule_P0_06, savD);
		sav_discount_map.put(SettPrivRef.StandRule_P0_08, canD);
		
		this.enableOrDisables(add_discount_map, true);
		this.enableOrDisables(del_discount_map, false);
		this.enableOrDisables(sav_discount_map, false);
		
		
		botStrip.setMembersMargin(4);
	    botStrip.setMembers(newD, savD, delD, canD);
	    
	    return botStrip;
	};
	

	protected DynamicForm createSearchForm(DynamicForm form) {
		form.setDataSource(ds);
		
		final SGCombo TFF_TYP = new SGCombo("TFF_TYP", Util.TI18N.TFF_TYP(),true);
		Util.initCodesComboValue(TFF_TYP, "TRANS_TFF_TYP","42666CA2DE904F6687FC172138CF3E51");
		TFF_TYP.setDisabled(true);
		
		String ord_id=LoginCache.getLoginUser().getDEFAULT_ORG_ID();
		final SGCombo TFF_NAME = new SGCombo("TFF_ID","计费协议");
		Util.initComboValue(TFF_NAME,"TARIFF_HEADER","ID","TFF_NAME","  where COM_FLAG = 'Y' and EXEC_ORG_ID IN (SELECT ID From BAS_ORG Where id ='"+ord_id+"'or ORG_INDEX Like '%,"+ord_id+",%') ","");
		
		final SGCombo FEE_ID = new SGCombo("FEE_ID", Util.TI18N.FEE_NAME());
		//FEE_ID.setTitle(ColorUtil.getRedTitle(Util.TI18N.FEE_NAME()));
		Util.initComboValue(FEE_ID, "V_TRANS_CHARGE_TYPE", "ID", "FEE_NAME", " WHERE FEE_ATTR = '53EB6809BFCC436799F735AAE23658B9'");
		
		final SGCheck COM_FLAG=new SGCheck("COM_FLAG","标识");
		COM_FLAG.setDefaultValue("Y");
		COM_FLAG.setVisible(false);
		
		form.setItems(TFF_TYP,TFF_NAME, FEE_ID,COM_FLAG);
		
		return form;
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "TARIFF_RULE");
		check_map.put("FEE_ATTR", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_ATTR());
		check_map.put("FEE_ID", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_ID());
		check_map.put("FEE_BASE", StaticRef.CHK_NOTNULL + Util.TI18N.FEE_BASE());
		check_map.put("GRADE_BASE", StaticRef.CHK_NOTNULL + Util.TI18N.GRADE_BASE());
		check_map.put("BASE_RATE", StaticRef.CHK_NOTNULL + Util.TI18N.BASE_RATE());
		
		cache_map.put("LOWER_LMT_OPRT", ">=");
		cache_map.put("LOWER_LMT", "0.00");
		cache_map.put("UPPER_LMT_OPRT", "<");
		cache_map.put("UPPER_LMT", "999999.99");
		cache_map.put("MIN_AMT", "0.00");
		cache_map.put("MAX_AMT","999999.99");
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("FEE_ATTR","53EB6809BFCC436799F735AAE23658B9");  //应收费用
	
		discount_ck_map=new HashMap<String, String>();
		discount_ck_map.put("TABLE", "TARIFF_ADDITIONAL");
		discount_ck_map.put("ADJ_DATEOBJ",StaticRef.CHK_DATE +"调整日期");
		discount_ck_map.put("ADJ_RATIO", StaticRef.CHK_NOTNULL +"比例");
		discount_ck_map.put("BIZ_DATEOBJ", StaticRef.CHK_NOTNULL +"基准日期");
		discount_ck_map.put("AVAIL_BY", StaticRef.CHK_NOTNULL +"生效期");
	}

	@Override
	public void onDestroy() {
		
	}
	
	public void initAddDiscountBtn(){
		enableOrDisables(add_discount_map, false);
		enableOrDisables(del_discount_map, false);
		enableOrDisables(sav_discount_map, true);
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
	
	public void initSaveDiscountBtn(){
		enableOrDisables(add_discount_map, true);
		enableOrDisables(del_discount_map, true);
		enableOrDisables(sav_discount_map, false);
	}
	
	public void initCancelDiscountBtn(){
		enableOrDisables(add_discount_map, true);
		enableOrDisables(del_discount_map, false);
		enableOrDisables(sav_discount_map, false);
	}

	@Override
	public Canvas createCanvas(String id, TabSet tabSet) {
		StandRuleView view = new StandRuleView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}

	protected void doInitFactor(Object tff_name, final ArrayList<HashMap<String,String>> maps) {
		HashMap<String,String> map = new HashMap<String,String>();
		Util.db_async.getRecord("FACTOR_ID,FEE_FACTOR,OBJ_TYPE,DICT_PARAM,DATA_FROM,DATA_ID,DATA_NAME,FM_FIELD", "TARIFF_FACTOR T,TARIFF_HEADER_FACTOR T1", " where T.ID=T1.FACTOR_ID and T1.TFF_ID='"+tff_name+"' and T.FEE_TYPE = '42666CA2DE904F6687FC172138CF3E51' order by T1.SHOW_SEQ", map,new AsyncCallback<ArrayList<HashMap<String,String>>>() {
			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				if(basInfo.getFields().length > 0) {
					FormItem item = new FormItem();
					basInfo.setItems(item);
				}
				if(result != null && result.size() > 0) {					

					ArrayList<FormItem> list = new ArrayList<FormItem>();
					SGSCombo[] leftCombos = new SGSCombo[result.size()];  //左括号
					SGText[] operCombos = new SGText[result.size()];   //计费因子名称
					SGCombo[] operatorCombos = new SGCombo[result.size()];  //操作符号
					SGSCombo[] rightCombos = new SGSCombo[result.size()];  //右括号
					SGSCombo[] linkCombos = new SGSCombo[result.size()];   //连接符AND/OR
					ComboBoxItem[] attTexts = new ComboBoxItem[result.size()]; //计费因子的值
					SGText[] operText = new SGText[result.size()];   //计费因子ID
					TextItem[] AREA = new TextItem[result.size()];
					TextItem NUM = new TextItem("NUM");
					NUM.setValue(result.size());
					NUM.setVisible(false);
					for(int j = 0; j < result.size(); j++){
						HashMap<String, String> map = (HashMap<String, String>)result.get(j);
						if(basInfo.getItem("LEFT_BRKT"+Integer.toString(j+1)) != null) {
							leftCombos[j] = (SGSCombo)basInfo.getItem("LEFT_BRKT"+Integer.toString(j+1));
						}
						else {
							leftCombos[j] = new SGSCombo("LEFT_BRKT"+Integer.toString(j+1), "左括号"); 
						}
						if(maps != null&&maps.size()>0) {
							leftCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("LEFT_BRKT"),""));
						}
						leftCombos[j].setShowTitle(false);
						leftCombos[j].setVisible(true);
						if(basInfo.getItem("OPER_OBJ"+Integer.toString(j+1)) != null) {
							operCombos[j] = (SGText)basInfo.getItem("OPER_OBJ"+Integer.toString(j+1));
						}
						else {
							operCombos[j] = new SGText("OPER_OBJ"+Integer.toString(j+1), "计费因子");
						}
						operCombos[j].setShowTitle(false);
						operCombos[j].setDisabled(true);
						if(maps != null&&maps.size()>0) {
							operCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("OPER_OBJ"),""));
						}else{
							operCombos[j].setValue(map.get("FEE_FACTOR"));
						}
						operCombos[j].setVisible(true);
						if(basInfo.getItem("OPERATOR"+Integer.toString(j+1)) != null) {
							operatorCombos[j] = (SGCombo)basInfo.getItem("OPERATOR"+Integer.toString(j+1));
						}
						else {
							operatorCombos[j] = new SGCombo("OPERATOR"+Integer.toString(j+1), "操作符");
						}
						if(maps != null&&maps.size()>0) {
							Util.initComboValue(operatorCombos[j], "BAS_CODES","CODE","NAME_C"," WHERE PROP_CODE = 'OPERATOR'", "", ObjUtil.ifNull(maps.get(j).get("OPERATOR"),""));
						}
						else {
							Util.initComboValue(operatorCombos[j], "BAS_CODES","CODE","NAME_C"," WHERE PROP_CODE = 'OPERATOR'", "", "");
						}
						operatorCombos[j].setShowTitle(false);
						operatorCombos[j].setVisible(true);
						if(map.get("OBJ_TYPE").equals("C9595DE73AC34B3B8E715637C2C846DC")){
							if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
								attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
							}
							else {
								attTexts[j] = new ComboBoxItem();
								attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
								attTexts[j].setTitle("值");
							}
							attTexts[j].setType("TextItem");
							attTexts[j].setShowTitle(false);
							attTexts[j].setColSpan(2);
							attTexts[j].setWidth(FormUtil.Width);
							attTexts[j].setTitleOrientation(TitleOrientation.TOP);
							if(maps != null&&maps.size()>0) {
								attTexts[j].setValue(ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
							}
							attTexts[j].setVisible(true);
						}else{
							if(map.get("DATA_FROM").equals("BAS_AREA")){
								if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
									attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
									AREA[j] = (TextItem)basInfo.getItem("AREA_ID"+Integer.toString(j+1));
								}
								else {
									attTexts[j] = new ComboBoxItem();
									attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
									attTexts[j].setTitle("值");
									AREA[j] = new TextItem();
									AREA[j].setName("AREA_ID"+Integer.toString(j+1));
								}
								attTexts[j].setType("ComboBoxItem");
								attTexts[j].setShowTitle(false);
								attTexts[j].setColSpan(2);
								attTexts[j].setWidth(FormUtil.Width);
								attTexts[j].setTitleOrientation(TitleOrientation.TOP);
								AREA[j].setVisible(false);
								if(maps != null&&maps.size()>0) {
									initArea(attTexts[j],AREA[j],ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
									AREA[j].setValue(ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
								}else{
									initArea(attTexts[j],AREA[j],"");
								}
							}else if(map.get("DATA_FROM").equals("BAS_CODES")){
								if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
									attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
								}
								else {
									attTexts[j] = new ComboBoxItem();
									attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
									attTexts[j].setTitle("值");
								}
								attTexts[j].setType("SelectItem");
								attTexts[j].setShowTitle(false);
								attTexts[j].setColSpan(2);
								attTexts[j].setWidth(FormUtil.Width);
								attTexts[j].setTitleOrientation(TitleOrientation.TOP);
								Util.initCodesComboValue(attTexts[j],map.get("DICT_PARAM"));
								if(maps != null&&maps.size()>0) {
									attTexts[j].setValue(ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
								}
							}
							else {
								if(basInfo.getItem("ATTR_VAL"+Integer.toString(j+1)) != null) {
									attTexts[j] = (ComboBoxItem)basInfo.getItem("ATTR_VAL"+Integer.toString(j+1));
								}
								else {
									attTexts[j] = new ComboBoxItem();
									attTexts[j].setName("ATTR_VAL"+Integer.toString(j+1));
									attTexts[j].setTitle("值");
								}
								attTexts[j].setType("SelectItem");
								attTexts[j].setShowTitle(false);
								attTexts[j].setColSpan(2);
								attTexts[j].setWidth(FormUtil.Width);
								attTexts[j].setTitleOrientation(TitleOrientation.TOP);
								if(maps != null&&maps.size()>0){
									Util.initComboValue(attTexts[j],map.get("DATA_FROM"),map.get("DATA_ID"),map.get("DATA_NAME"),"","",ObjUtil.ifNull(maps.get(j).get("ATTR_VAL"),""));
								}else{
									Util.initComboValue(attTexts[j],map.get("DATA_FROM"),map.get("DATA_ID"),map.get("DATA_NAME"),"","");
								}
							}
							attTexts[j].setVisible(true);
						}
						if(basInfo.getItem("RIGHT_BRKT"+Integer.toString(j+1)) != null) {
							rightCombos[j] = (SGSCombo)basInfo.getItem("RIGHT_BRKT"+Integer.toString(j+1));
						}
						else {
							rightCombos[j] = new SGSCombo("RIGHT_BRKT"+Integer.toString(j+1), "右括号");
						}
						rightCombos[j].setShowTitle(false);
						if(maps != null&&maps.size()>0) {
							rightCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("RIGHT_BRKT"),""));
						}
						rightCombos[j].setVisible(true);
						if(basInfo.getItem("LINK"+Integer.toString(j+1)) != null) {
							linkCombos[j] = (SGSCombo)basInfo.getItem("LINK"+Integer.toString(j+1));
						}
						else {
							linkCombos[j] = new SGSCombo("LINK"+Integer.toString(j+1), "连接符");
						}
						linkCombos[j].setShowTitle(false);
						if(maps != null&&maps.size()>0) {
							linkCombos[j].setValue(ObjUtil.ifNull(maps.get(j).get("LINK"),""));
						}
						linkCombos[j].setVisible(true);
						
						if(basInfo.getItem("OPER_ATTR"+Integer.toString(j+1)) != null){
							operText[j] = (SGText)basInfo.getItem("OPER_ATTR"+Integer.toString(j+1));
						}
						else{
							operText[j] = new SGText("OPER_ATTR"+Integer.toString(j+1), "计费因子ID");
						}
						operText[j].setShowTitle(false);
						operText[j].setDisabled(true);
						operText[j].setVisible(false);
						if(maps != null&&maps.size()>0) {
							operText[j].setValue(ObjUtil.ifNull(maps.get(j).get("OPER_ATTR"),""));
						}else{
							operText[j].setValue(ObjUtil.ifNull(result.get(j).get("FM_FIELD"),""));
						}
						
						LinkedHashMap<String, String> lb_hm = new LinkedHashMap<String, String>();
						lb_hm.put(" ", "");
						lb_hm.put("(", "(");
						leftCombos[j].setValueMap(lb_hm);
						
						LinkedHashMap<String, String> rb_hm = new LinkedHashMap<String, String>();
						rb_hm.put(" ", "");
						rb_hm.put(")", ")");
						rightCombos[j].setValueMap(rb_hm);
						
						LinkedHashMap<String, String> lj_hm = new LinkedHashMap<String, String>();
						lj_hm.put(" ", "");
						lj_hm.put("AND", "AND");
						lj_hm.put("OR", "OR");
						linkCombos[j].setValueMap(lj_hm);
						
						if(leftCombos[j]!=null){
							if(j==0)leftCombos[0].setShowTitle(true);
							list.add(leftCombos[j]);
						}
						if(operCombos[j]!=null){
							if(j==0)operCombos[0].setShowTitle(true);
							list.add(operCombos[j]);
						}
						if(operatorCombos[j]!=null){
							if(j==0)operatorCombos[0].setShowTitle(true);
							list.add(operatorCombos[j]);
						}
						if(attTexts[j]!=null){
							if(j==0)attTexts[0].setShowTitle(true);
							list.add(attTexts[j]);
						}
						if(AREA[j]!=null){
							list.add(AREA[j]);
						}
						if(rightCombos[j]!=null){
							if(j==0)rightCombos[0].setShowTitle(true);
							list.add(rightCombos[j]);
						}
						if(linkCombos[j]!=null){
							if(j==0)linkCombos[0].setShowTitle(true);
							list.add(linkCombos[j]);
						}
						if(operText[j]!=null){
							if(j==0)operText[0].setShowTitle(true);
							list.add(operText[j]);
						}
					}
					list.add(NUM);
					FormItem[] lists = (FormItem[])list.toArray(new FormItem[list.size()]);
					basInfo.reset();
					basInfo.setItems(lists);						
					//basInfo.redraw();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				
			}
		});
	}
	
	public static void initArea(final ComboBoxItem area_name,final TextItem area_id,final String defaultValue){
		DataSource ds = VCAreaDS.getInstance("VC_BAS_AREA");
		
		ListGridField AREA_CODE = new ListGridField("AREA_CODE", Util.TI18N.AREA_CODE(), 70);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME", Util.TI18N.SHORT_NAME(), 90);
		ListGridField HINT_CODE = new ListGridField("HINT_CODE", Util.TI18N.HINT_CODE(), 60);
		ListGridField AREA_LEVEL = new ListGridField("AREA_LEVEL", Util.TI18N.AREA_LEVEL(), 60);
		area_name.setColSpan(2);
		area_name.setWidth(FormUtil.Width);
		area_name.setOptionDataSource(ds);  
		area_name.setDisabled(false);
		area_name.setShowDisabled(false);
		area_name.setDisplayField("CONTENT");
		area_name.setValueField("SHORT_NAME");
		area_name.setPickListWidth(240);
		area_name.setPickListBaseStyle("myBoxedGridCell");
		area_name.setPickListFields(AREA_CODE, SHORT_NAME,AREA_LEVEL,HINT_CODE);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		area_name.setPickListCriteria(criteria);
		
		if(defaultValue != null){
			Util.db_async.getRecord("AREA_CNAME", " VC_AREA", " where AREA_CODE = '"+defaultValue+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
				
				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					if(result!=null && result.size()>0){
						area_name.setDefaultValue(result.get(0).get("AREA_CNAME"));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					
				}
			});
			
		}
		
		area_name.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = area_name.getSelectedRecord();
				if(selectedRecord != null && area_id != null){
					area_id.setValue(selectedRecord.getAttribute("AREA_CODE"));
				}
				
			}
		});
		area_name.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if(ObjUtil.ifObjNull(area_name.getValue(),"").toString().length() < 1) {
					if(area_id != null){
						area_id.setValue("");
					}
				}
			}
			
		});
		
	}
	
}
