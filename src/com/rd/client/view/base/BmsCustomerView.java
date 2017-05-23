package com.rd.client.view.base;

import com.rd.client.PanelFactory;
import com.rd.client.action.base.customer.SaveCustomerAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLCombo;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.BmsCustomerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
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
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
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
 * bms--客户信息
 * @author fanglm
 *
 */
@ClassForNameAble
public class BmsCustomerView extends SGForm implements PanelFactory {
	
	private DataSource ds;
	private SGTable table;
	public ValuesManager vm;
	private SGPanel mainForm;
	private SGPanel mainForm2;
	private SectionStack section;
	private Window searchWin = null;
	public SGPanel searchForm = new SGPanel();
	private SectionStack m_section;
	private TabSet leftTabSet;
	public ToolStrip toolStrip1;
	public String OP_FLAG="A";
	public SGCombo PARENT_CUSTOMER_ID;
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = BmsCustomerDS.getInstance("BAS_CUSTOMER2","BAS_CUSTOMER");
	    
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%",false,true,false);
		createListFields();
		
		table.setCanEdit(false);
		m_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
//	    listItem.setControls(addMaxBtn(m_section, stack, "200",true), new SGPage(table, true).initPageBtn());
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    m_section.addSection(listItem);
	    m_section.setWidth("100%");
		stack.addMember(m_section);
		addSplitBar(stack);
		
		//STACK的右边布局
		
		leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
        
        initVerify();
        
        Tab tab1 = new Tab(Util.TI18N.MAININFO());
		//组织明细的FORM布局
		tab1.setPane(createMainInfo());
	    leftTabSet.addTab(tab1);
             
		stack.addMember(leftTabSet);

		vm.addMember(mainForm);
		vm.addMember(mainForm2);
		vm.setDataSource(ds);
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {				
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
				if(isMax) {
					expend();
				}
			}
			
		});

		  
		return main;
	}
	
	public void initVerify() {
		check_map.put("TABLE", "BAS_CUSTOMER");
		check_map.put("CUSTOMER_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.CUSTOMER_CODE());		
	}
	
	//创建列表
	private void createListFields(){
        table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	OP_FLAG = "A";
                vm.editRecord(selectedRecord);
                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);     
                initSaveBtn();
                
            }
        });
		 table.setShowRowNumbers(true);
		 
		  ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE", Util.TI18N.CUSTOMER_CODE(), 80);
		  ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_CNAME", Util.TI18N.CUSTOMER_CNAME(), 120);
		  ListGridField PROPERTY = new ListGridField("PROPERTY_NAME",Util.TI18N.PROPERTY(),100);
		  ListGridField INDUSTRY = new ListGridField("INDUSTRY_NAME",Util.TI18N.INDUSTRY(),100);
		  ListGridField BILL_TO = new ListGridField("BILL_TO_NAME",Util.TI18N.BILL_TO(),100);
		  ListGridField SHORT_NAME = new ListGridField("SHORT_NAME",Util.TI18N.SHORT_NAME(),100);
		  ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),100);
		  ListGridField CUSTOMER_FLAG = new ListGridField("CUSTOMER_FLAG",Util.TI18N.CUSTOMER_FLAG(),60);
		  CUSTOMER_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField CONTACTER_FLAG = new ListGridField("CONTACTER_FLAG",Util.TI18N.CONTACTER_FLAG(),70);
		  CONTACTER_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField ADDRESS = new ListGridField("ADDRESS", Util.TI18N.ADDRESS(), 140);
		  ListGridField CONT_TEL = new ListGridField("CONT_TEL", Util.TI18N.CONT_TEL(), 100);
		  ListGridField CONT_NAME = new ListGridField("CONT_NAME", Util.TI18N.CONT_NAME(), 100);
		  ListGridField CONT_EMAIL = new ListGridField("CONT_EMAIL", Util.TI18N.CONT_EMAIL(), 120);
		  ListGridField URL = new ListGridField("URL", Util.TI18N.URL(), 60);
		  ListGridField TAXNO = new ListGridField("TAXNO", Util.TI18N.TAXNO(), 100);
		  ListGridField BANK = new ListGridField("BANK", Util.TI18N.BANK(), 120);
		  ListGridField CUSTOMER_ENAME = new ListGridField("CUSTOMER_ENAME", Util.TI18N.CUSTOMER_ENAME(), 120);
		  ListGridField INVOICE_TITLE = new ListGridField("INVOICE_TITLE", Util.TI18N.INVOICE_TITLE(), 100);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(), 60);
		  ListGridField GRADE = new ListGridField("GRADE_NAME", Util.TI18N.C_GRADE(), 90);
		  ListGridField FOLLOWUP = new ListGridField("FOLLOWUP_NAME", Util.TI18N.FOLLOWUP(), 90);
		  ListGridField TAX_RATE=new ListGridField("UDF2",Util.TI18N.TAX_RATE()+"(%)",60);
		  ListGridField UDF3=new ListGridField("UDF3","邮寄地址",120);
		  table.setFields(CUSTOMER_CODE,CUSTOMER_CNAME,SHORT_NAME,HINT_CODE,BILL_TO,PROPERTY,INDUSTRY,CONTACTER_FLAG,TAX_RATE,CUSTOMER_FLAG
				  ,ADDRESS,UDF3,CONT_TEL,CONT_NAME,CONT_EMAIL,URL,TAXNO,BANK,CUSTOMER_ENAME
				  ,INVOICE_TITLE,ENABLE_FLAG,GRADE,FOLLOWUP);	
	}
	//创建主信息页签
	private SectionStack createMainInfo(){
        //1
		SGText CUSTOMER_CODE = new SGText("CUSTOMER_CODE",ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CODE()),true);      
        CUSTOMER_CODE.setDisabled(true);
        final SGText SHORT_NAME = new SGText("SHORT_NAME",ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
        SHORT_NAME.setDisabled(true);
        SGCheck ENABLE = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
        ENABLE.setDisabled(true);
        SGCheck CONTACTER_FLAG = new SGCheck("CONTACTER_FLAG",Util.TI18N.CONTACTER_FLAG());
        CONTACTER_FLAG.setDisabled(true);
		//2
        final SGLText CUSTOMER_CNAME = new SGLText("CUSTOMER_CNAME",ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CNAME()),true);
        CUSTOMER_CNAME.setDisabled(true);
        final SGLText CUSTOMER_ENAME = new SGLText("CUSTOMER_ENAME",Util.TI18N.CUSTOMER_ENAME());
        CUSTOMER_ENAME.setDisabled(true);
		//3
        final SGCombo CUSTOM_ATTR = new SGCombo("CUSTOM_ATTR", ColorUtil.getRedTitle(Util.TI18N.CUSTOM_ATTR()),true);	//客户属性
        Util.initCodesComboValue(CUSTOM_ATTR,"CUSTOM_ATTR");
        CUSTOM_ATTR.setDisabled(true);
        final SGCombo BILL_TO = new SGCombo("BILL_TO", Util.TI18N.BILL_TO());
        Util.initComboValue(BILL_TO, "BAS_CUSTOMER", "ID", "SHORT_NAME", " WHERE ENABLE_FLAG='Y' and PAYER_FLAG='Y'");
        BILL_TO.setDisabled(true);
        SGText HINT_CODE = new SGText("HINT_CODE",Util.TI18N.HINT_CODE());
        HINT_CODE.setDisabled(true);
        PARENT_CUSTOMER_ID = new SGCombo("PARENT_CUSTOMER_ID",Util.TI18N.PARENT_CUSTOMER_ID());
        Util.initCustComboValue(PARENT_CUSTOMER_ID, "");
        PARENT_CUSTOMER_ID.setDisabled(true);
		//4
        SGCombo INDUSTRY = new SGCombo("INDUSTRY",Util.TI18N.INDUSTRY(),true);
        Util.initCodesComboValue(INDUSTRY,"INDUSTRY");
        INDUSTRY.setDisabled(true);
        SGCombo PROPERTY = new SGCombo("PROPERTY",Util.TI18N.PROPERTY());
        PROPERTY.setDisabled(true);
        Util.initCodesComboValue(PROPERTY,"PROPERTY");
        SGCombo C_GRADE = new SGCombo("GRADE",Util.TI18N.C_GRADE());
        Util.initCodesComboValue(C_GRADE,"GRADE");
        C_GRADE .setDisabled(true);
        final SGText VOL_GWT_RATIO = new SGText("VOL_GWT_RATIO","毛重/体积转换比例");
        VOL_GWT_RATIO.setDisabled(true);
		//5
        SGLCombo TARIFF_ID = new SGLCombo("TARIFF_ID","协议模板",true);
		Util.initComboValue(TARIFF_ID, "TARIFF_HEADER", "ID", "TFF_NAME", 
				" WHERE ENABLE_FLAG = 'Y' and TFF_TYP = '42666CA2DE904F6687FC172138CF3E51' " +
				" and EXEC_ORG_ID IN (SELECT ID From BAS_ORG Where id ='"+ 
				LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"' or ORG_INDEX Like '%,"+
				LoginCache.getLoginUser().getDEFAULT_ORG_ID()+",%')");
		TARIFF_ID.setDisabled(true);
		SGCombo SETT_CYC = new SGCombo("SETT_CYC",Util.TI18N.SETT_CYC());//结算账期
        Util.initCodesComboValue(SETT_CYC,"SETTLE_CYC");
        SETT_CYC.setDisabled(true);
        SGCombo SETT_WEEKLY = new SGCombo("SETT_WEEKLY","结算周期");//结算周期
        Util.initCodesComboValue(SETT_WEEKLY,"SETT_WEEKLY");
        SETT_WEEKLY.setDisabled(true);
		//6
        TextAreaItem notes = new TextAreaItem("NOTES",Util.TI18N.NOTES());
		notes.setStartRow(true);
		notes.setColSpan(4);
		notes.setHeight(34);
		notes.setWidth(FormUtil.longWidth);
		notes.setTitleOrientation(TitleOrientation.TOP);
		notes.setDisabled(true);
		
		SGCheck CUSTOMER_FLAG = new SGCheck("CUSTOMER_FLAG",Util.TI18N.CUSTOMER_FLAG());     
		CUSTOMER_FLAG.setDisabled(true);
		final SGCheck PAYER_FLAG = new SGCheck("PAYER_FLAG",Util.TI18N.PAYER_FLAG());
		PAYER_FLAG.setDisabled(true);
        SHORT_NAME.addBlurHandler(new GetHintAction(SHORT_NAME, HINT_CODE,CUSTOMER_CODE));
       
//        CUSTOMER_CNAME.addBlurHandler(new BlurHandler() {
//			
//			@Override
//			public void onBlur(BlurEvent event) {
//				if(CUSTOMER_CNAME.getValue() != null){
//					CUSTOMER_ENAME.setValue(CUSTOMER_CNAME.getValue().toString());
//					if(!ObjUtil.isNotNull(SHORT_NAME.getValue())){ //简称为空时初始化中文描述值
//						SHORT_NAME.setValue(CUSTOMER_CNAME.getValue().toString());
//						BlurEvent.fire(SHORT_NAME, SHORT_NAME.getConfig());
//					}
//					if(PAYER_FLAG.getValue().equals("Y")){
//						BILL_TO.setValue(CUSTOMER_CNAME.getValue().toString());
//					}
//				}
//				
//			}
//		});
        
        mainForm = new SGPanel();
        mainForm.setWidth("40%");
        mainForm.setItems(CUSTOMER_CODE,SHORT_NAME,ENABLE,CONTACTER_FLAG,CUSTOMER_CNAME,CUSTOMER_ENAME,CUSTOM_ATTR,
        		BILL_TO,HINT_CODE,PARENT_CUSTOMER_ID,INDUSTRY,PROPERTY,C_GRADE,VOL_GWT_RATIO,TARIFF_ID,SETT_CYC,SETT_WEEKLY,
        		notes,CUSTOMER_FLAG,PAYER_FLAG);
        
        //1
        SGLText BANK =new SGLText("BANK",Util.TI18N.BANK(),true); //开户银行
        SGLText BANK_POINT =new SGLText("BANK_POINT","开户网点"); //开户网点
        SGText GEN_METHOD = new SGText("GEN_METHOD","月结帐号");
        //2
        SGLText INVOICE_TITLE =new SGLText("INVOICE_TITLE",Util.TI18N.INVOICE_TITLE(),true);//发票
        SGLText C_ADDRESS = new SGLText("ADDRESS",Util.TI18N.C_ADDRESS());
        SGText TAX_RATE=new SGText("UDF2",Util.TI18N.TAX_RATE()+"(%)");  //税率
        //3
        SGText TAXNO =new SGText("TAXNO",Util.TI18N.TAXNO());//税号
        SGText CONT_NAME = new SGText("CONT_NAME",Util.TI18N.CONT_NAME());
        SGText CONT_TEL = new SGText("CONT_TEL",Util.TI18N.CONT_TEL());
        SGText CONT_EMAIL =new SGText("CONT_EMAIL",Util.TI18N.CONT_EMAIL(),true);
        SGText URL =new SGText("URL",Util.TI18N.URL());
        SGText UDF1 =new SGText("UDF1","应收类型代码");
        SGText UDF4 =new SGText("UDF4","贷方科目");
        //4
        SGCombo CURRENCY = new SGCombo("CURRENCY",Util.TI18N.CURRENCY(),true);//币种
        Util.initCodesComboValue(CURRENCY,"CURRENCY");
        SGCheck INVOICE_FLAG = new SGCheck("INVOICE_FLAG", Util.TI18N.INVOICE_FLAG());//开票
       
        SGLText UDF3 =new SGLText("UDF3","邮寄地址",true);
        mainForm2 = new SGPanel();
        mainForm2.setNumCols(10);
        mainForm2.setWidth("40%");
        mainForm2.setItems(BANK,BANK_POINT,GEN_METHOD,INVOICE_TITLE,C_ADDRESS,TAX_RATE,
        		UDF3,CONT_NAME,CONT_TEL,TAXNO,CONT_EMAIL,URL,UDF1,UDF4,CURRENCY,INVOICE_FLAG);
        
        
        
        
        
        section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SectionStackSection mainS = new SectionStackSection("基础信息");
		mainS.addItem(mainForm);
		mainForm.setHeight("50%");
		mainS.setExpanded(true); 
		section.addSection(mainS);
		
		SectionStackSection mainS2 = new SectionStackSection("结算信息");
		mainS2.addItem(mainForm2);
		mainForm2.setHeight("60%");
		mainS2.setExpanded(true); 
		section.addSection(mainS2);
		
		
		section.setVisibilityMode(VisibilityMode.MULTIPLE);
    	section.setAnimateSections(true);
        
        return section;
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.CUSTOM);
        
        toolStrip.addMember(searchButton);
        
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm), m_section.getSection(0),vm).getViewPanel();
					searchWin.setWidth(600);
					searchWin.setHeight(360);
				}
				else {					
					searchWin.show();
				}
			}
        	
        });
        	
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.CUSTOM_P0_01);
        toolStrip.addMember(saveButton);
        saveButton.addClickHandler(new SaveCustomerAction(table,  vm, check_map,this,PARENT_CUSTOMER_ID));
 
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.CUSTOM_P0_02);
        canButton.addClickHandler(new CancelMultiFormAction(table, vm,this));
        toolStrip.addMember(canButton);
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.CUSTOM_P0_03);
        toolStrip.addMember(expButton);
        expButton.addClickHandler(new ExportAction(table,""));
        
        save_map.put(SettPrivRef.CUSTOM_P0_01, saveButton);
        save_map.put(SettPrivRef.CUSTOM_P0_02, canButton);
        
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(save_map, false);
        toolStrip.setMembersMargin(4);
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){

		SGText CUSTOMER_CODE = new SGText("CUSTOMER_CODE","客户代码");
		final TextItem ID=new TextItem("ID");
		ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME = new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, ID);	
		
		SGText MAINT_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		TextItem MAINT_ORG_ID = new TextItem("EXEC_ORG_ID");
		MAINT_ORG_ID.setVisible(false);
		Util.initOrg(MAINT_ORG_ID_NAME, MAINT_ORG_ID, false, "30%", "38%");
		MAINT_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		MAINT_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		
		SGCheck TRANSPORT_FLAG = new SGCheck("TRANSPORT_FLAG",Util.TI18N.TRANSPORT_FLAG());
        //SGCheck WAREHOUSE_FLAG = new SGCheck("WAREHOUSE_FLAG",Util.TI18N.C_WAREHOUSE_FLAG());
        
        SGCheck ENABLE = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG()); 
        ENABLE.setValue(true);
        SGCheck CUSTOMER_FLAG = new SGCheck("CUSTOMER_FLAG",Util.TI18N.CUSTOMER_FLAG(),true);   
        CUSTOMER_FLAG.setValue(true);
        SGCheck PAYER_FLAG = new SGCheck("PAYER_FLAG",Util.TI18N.PAYER_FLAG());
		
        SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG(),true);
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
        
        form.setItems(ID,CUSTOMER_NAME,CUSTOMER_CODE,MAINT_ORG_ID,MAINT_ORG_ID_NAME,
        		CUSTOMER_FLAG,PAYER_FLAG,TRANSPORT_FLAG,C_ORG_FLAG,ENABLE);
        form.setWidth("80%");
        return form;
       
	}
	
	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}

	}


	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BmsCustomerView view = new BmsCustomerView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}
	
}
