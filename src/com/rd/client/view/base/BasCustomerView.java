package com.rd.client.view.base;

import java.util.Map;
import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.customer.NewCustomerAction;
import com.rd.client.action.base.customer.SaveCustOrdAction;
import com.rd.client.action.base.customer.SaveCustOrgAction;
import com.rd.client.action.base.customer.SaveCustSrvcAction;
import com.rd.client.action.base.customer.SaveCustomerAction;
import com.rd.client.action.base.customer.SyncPrivilegeAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.CellClickAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLCombo;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.AddrDS;
import com.rd.client.ds.base.CustOrdDS;
import com.rd.client.ds.base.CustOrgDS;
import com.rd.client.ds.base.CustServDS;
import com.rd.client.ds.base.CustomerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.OrgWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditorExitEvent;
import com.smartgwt.client.widgets.grid.events.EditorExitHandler;
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
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 基础资料--客户管理
 * @author fanglm
 *
 */
@ClassForNameAble
public class BasCustomerView extends SGForm implements PanelFactory {
	
	private DataSource ds;
	private DataSource orgDs;
	private DataSource ordDs;
	private DataSource servDs;
	private SGTable table;
	public ValuesManager vm;
	private SGPanel mainForm;
	private SGPanel mainForm2;
	private SGPanel transForm;
	private SGPanel transForm1;
	private SGPanel transForm2;
	private SGTable servTable;
	private SGTable orgTable;
	private SGTable ordTable;
	private SectionStack section;
	private TextItem customer_code;
	private TextItem sku_name;
	private Window searchWin = null;
	public TreeNode selectNode;
	public Record selectRecord;
	public SGPanel searchForm = new SGPanel();
	private String cust_id;
	private int t_pageNum=0;//执行机构二级页签
	private int m_pageNum=0;//全局页签数
	private SectionStack m_section;
	private TabSet leftTabSet;
	public ToolStrip toolStrip1;
	public ToolStrip toolStrip2;
	public ToolStrip toolStrip3;
	public String OP_FLAG="A";
	public Record selectrec;
	private Canvas canvas;
	public  Canvas tileCanvas;
	public SGCombo PARENT_CUSTOMER_ID;
	private Window uploadWin;
	/*public BasCustomerView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = CustomerDS.getInstance("BAS_CUSTOMER");
	    
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
        
        if(isPrivilege(BasPrivRef.CUSTOM_P1)) {
	        Tab tab1 = new Tab(Util.TI18N.MAININFO());
			//组织明细的FORM布局
	
			tab1.setPane(createMainInfo());
	        leftTabSet.addTab(tab1);
        }
        
        if(isPrivilege(BasPrivRef.CUSTOM_P2)) {
	        //运输信息
	        Tab tab2 = new Tab(Util.TI18N.TRANS_INFO());
	        tab2.setPane(createTransInfo());
	        leftTabSet.addTab(tab2);
        }
        
        if(isPrivilege(BasPrivRef.CUSTOM_P3)){
        	//图片信息
        	Tab tab5 = new Tab(Util.TI18N.IMG_INFO());
        	tab5.setPane(createImgInfo());
        	leftTabSet.addTab(tab5);
        }
        
        /*if(isPrivilege(BasPrivRef.CUSTOM_P3)) {
	      //仓库信息
	        Tab tab3 = new Tab(Util.TI18N.WMS_INFO());
	        tab3.setPane(createWMSInfo());
	        leftTabSet.addTab(tab3);
        }
        
        if(isPrivilege(BasPrivRef.CUSTOM_P4)) {
	      //合同信息
	        Tab tab4 = new Tab(Util.TI18N.CONTRACT_INFO());
	        leftTabSet.addTab(tab4);
        }*/
        
		stack.addMember(leftTabSet);
		
		
		
		vm.addMember(mainForm);
		vm.addMember(mainForm2);
		vm.addMember(transForm);
		vm.addMember(transForm1);
		vm.addMember(transForm2);
		vm.setDataSource(ds);
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		//addDoubeclick(table, listItem, leftTabSet, m_section);
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
		
		leftTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				m_pageNum = event.getTabNum(); //主页签数
			    
			}
		});
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(m_pageNum == 2){
					if(table.getSelectedRecord() != null){
						if(canvas != null){
							if(canvas.isCreated()){
								canvas.destroy();
							}
						}
						canvas = new Canvas();
						tileCanvas.addChild(canvas);
						canvas.setHeight(163);
						canvas.setWidth(780);
						if(table.getSelectedRecord().getAttribute("CUSTOMER_CODE") != null){
							ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_SKU_PREVIEW_URL,table.getSelectedRecord().getAttribute("CUSTOMER_CODE"));
							action.getName();
						}
			    
					}
				}
			}
			
		});
		  
		return main;
	}
	
	public void initVerify() {
		check_map.put("TABLE", "BAS_CUSTOMER");
		check_map.put("CUSTOMER_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.CUSTOMER_CODE());
//		check_map.put("HINT_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.HINT_CODE());
		//check_map.put("CUSTOMER_CNAME",StaticRef.CHK_UNIQUE + Util.TI18N.CUSTOMER_CNAME());
		//check_map.put("SHORT_NAME",StaticRef.CHK_UNIQUE + Util.TI18N.SHORT_NAME());
		
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("CUSTOMER_FLAG", "Y");
		cache_map.put("PAYER_FLAG", "Y");
		cache_map.put("SKU_EDIT_FLAG", "Y");
		cache_map.put("CURRENCY", "029E5102CFDD4732A9D34FCE73CB82F8");
		cache_map.put("MAINT_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		cache_map.put("EXEC_ORG_ID_NAME", LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
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
                customer_code.setDisabled(true);
                cust_id = event.getRecord().getAttributeAsString("ID");
                TransInfoTabChange(t_pageNum);
                initSaveBtn();
                
            }
        });
		 table.setShowRowNumbers(true);
		 
		  ListGridField CUSTOMER_CODE = new ListGridField("CUSTOMER_CODE", Util.TI18N.CUSTOMER_CODE(), 80);
//		  CUSTOMER_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CODE()));
		  ListGridField CUSTOMER_CNAME = new ListGridField("CUSTOMER_CNAME", Util.TI18N.CUSTOMER_CNAME(), 120);
//		  CUSTOMER_CNAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CNAME()));
		 // CUSTOMER_CNAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CNAME()));
		  ListGridField PROPERTY = new ListGridField("PROPERTY_NAME",Util.TI18N.PROPERTY(),100);
		  ListGridField INDUSTRY = new ListGridField("INDUSTRY_NAME",Util.TI18N.INDUSTRY(),100);
		  ListGridField BILL_TO = new ListGridField("BILL_TO_NAME",Util.TI18N.BILL_TO(),100);
//		  BILL_TO.setTitle(ColorUtil.getBlueTitle(Util.TI18N.BILL_TO()));
		  ListGridField SHORT_NAME = new ListGridField("SHORT_NAME",Util.TI18N.SHORT_NAME(),100);
//		  SHORT_NAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
		  ListGridField HINT_CODE = new ListGridField("HINT_CODE",Util.TI18N.HINT_CODE(),100);
//		  HINT_CODE.setTitle(ColorUtil.getBlueTitle(Util.TI18N.HINT_CODE()));
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
//		  ListGridField AR_DEADLINE = new ListGridField("AR_DEADLINE", Util.TI18N.AR_DEADLINE(), 90);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG(), 60);
		  ListGridField GRADE = new ListGridField("GRADE_NAME", Util.TI18N.C_GRADE(), 90);
		  ListGridField FOLLOWUP = new ListGridField("FOLLOWUP_NAME", Util.TI18N.FOLLOWUP(), 90);
		  //ListGridField PARENT_ORG = new ListGridField("PARENT_ORG_NAME", Util.TI18N.PARENT_ORG_NAME(), 90);
		  
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
        customer_code = CUSTOMER_CODE;
        final SGText SHORT_NAME = new SGText("SHORT_NAME",ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
        SGCheck ENABLE = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
        SGCheck CONTACTER_FLAG = new SGCheck("CONTACTER_FLAG",Util.TI18N.CONTACTER_FLAG());
        
		//2
        final SGLText CUSTOMER_CNAME = new SGLText("CUSTOMER_CNAME",ColorUtil.getRedTitle(Util.TI18N.CUSTOMER_CNAME()),true);
        final SGLText CUSTOMER_ENAME = new SGLText("CUSTOMER_ENAME",Util.TI18N.CUSTOMER_ENAME());
		
		//3
        final SGCombo CUSTOM_ATTR = new SGCombo("CUSTOM_ATTR", ColorUtil.getRedTitle(Util.TI18N.CUSTOM_ATTR()),true);	//客户属性
        Util.initCodesComboValue(CUSTOM_ATTR,"CUSTOM_ATTR");
        final SGCombo BILL_TO = new SGCombo("BILL_TO", Util.TI18N.BILL_TO());
        Util.initComboValue(BILL_TO, "BAS_CUSTOMER", "ID", "SHORT_NAME", " WHERE ENABLE_FLAG='Y' and PAYER_FLAG='Y'");
        SGText HINT_CODE = new SGText("HINT_CODE",Util.TI18N.HINT_CODE());
        PARENT_CUSTOMER_ID = new SGCombo("PARENT_CUSTOMER_ID",Util.TI18N.PARENT_CUSTOMER_ID());
        Util.initCustComboValue(PARENT_CUSTOMER_ID, "");
        
		//4
        SGCombo INDUSTRY = new SGCombo("INDUSTRY",Util.TI18N.INDUSTRY(),true);
        Util.initCodesComboValue(INDUSTRY,"INDUSTRY");
        SGCombo PROPERTY = new SGCombo("PROPERTY",Util.TI18N.PROPERTY());
        Util.initCodesComboValue(PROPERTY,"PROPERTY");
        SGCombo C_GRADE = new SGCombo("GRADE",Util.TI18N.C_GRADE());
        Util.initCodesComboValue(C_GRADE,"GRADE");
        final SGText VOL_GWT_RATIO = new SGText("VOL_GWT_RATIO","毛重/体积转换比例");
        
		//5
        SGLCombo TARIFF_ID = new SGLCombo("TARIFF_ID","协议模板",true);
		Util.initComboValue(TARIFF_ID, "TARIFF_HEADER", "ID", "TFF_NAME", 
				" WHERE ENABLE_FLAG = 'Y' and TFF_TYP = '42666CA2DE904F6687FC172138CF3E51' " +
				" and EXEC_ORG_ID IN (SELECT ID From BAS_ORG Where id ='"+ 
				LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"' or ORG_INDEX Like '%,"+
				LoginCache.getLoginUser().getDEFAULT_ORG_ID()+",%')");
		SGCombo SETT_CYC = new SGCombo("SETT_CYC",Util.TI18N.SETT_CYC());//结算账期
        Util.initCodesComboValue(SETT_CYC,"SETTLE_CYC");
        SGCombo SETT_WEEKLY = new SGCombo("SETT_WEEKLY","结算周期");//结算周期
        Util.initCodesComboValue(SETT_WEEKLY,"SETT_WEEKLY");
		
		//6
        TextAreaItem notes = new TextAreaItem("NOTES",Util.TI18N.NOTES());
		notes.setStartRow(true);
		notes.setColSpan(4);
		notes.setHeight(34);
		notes.setWidth(FormUtil.longWidth);
		notes.setTitleOrientation(TitleOrientation.TOP);
//		notes.setTitleVAlign(VerticalAlignment.TOP);
		SGCheck CUSTOMER_FLAG = new SGCheck("CUSTOMER_FLAG",Util.TI18N.CUSTOMER_FLAG());     
        final SGCheck PAYER_FLAG = new SGCheck("PAYER_FLAG",Util.TI18N.PAYER_FLAG());
//		SGCheck TRANSPORT_FLAG = new SGCheck("TRANSPORT_FLAG",Util.TI18N.TRANSPORT_FLAG());
//      SGCheck WAREHOUSE_FLAG = new SGCheck("WAREHOUSE_FLAG",Util.TI18N.C_WAREHOUSE_FLAG());
		
        
        SHORT_NAME.addBlurHandler(new GetHintAction(SHORT_NAME, HINT_CODE,CUSTOMER_CODE));
        
        CUSTOMER_CNAME.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(CUSTOMER_CNAME.getValue() != null){
					CUSTOMER_ENAME.setValue(CUSTOMER_CNAME.getValue().toString());
					if(!ObjUtil.isNotNull(SHORT_NAME.getValue())){ //简称为空时初始化中文描述值
						SHORT_NAME.setValue(CUSTOMER_CNAME.getValue().toString());
						BlurEvent.fire(SHORT_NAME, SHORT_NAME.getConfig());
					}
					if(PAYER_FLAG.getValue().equals("Y")){
						BILL_TO.setValue(CUSTOMER_CNAME.getValue().toString());
					}
				}
				
			}
		});
        
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
		
		private TabSet createTransInfo(){
			TabSet chTabSet = new TabSet();  
			chTabSet.setWidth100();   
			chTabSet.setHeight100(); 
			chTabSet.setTabBarPosition(Side.LEFT);
	        
			final TextItem LOAD_ID = new TextItem("LOAD_ID");
			LOAD_ID.setVisible(false);
			
			final SGText LOAD_AREA_NAME = new SGText("START_AREA_ID_NAME", Util.TI18N.PROVINCE());
			LOAD_AREA_NAME.setDisabled(true);
			LOAD_AREA_NAME.setStartRow(true);
			
			final SGText LOAD_AREA_NAME2 = new SGText("START_AREA_NAME2", Util.TI18N.CITY());
			LOAD_AREA_NAME2.setDisabled(true);
			
			final SGText LOAD_AREA_NAME3 = new SGText("START_AREA_NAME3", Util.TI18N.AREA());
			LOAD_AREA_NAME3.setDisabled(true);
			
			final ComboBoxItem LOAD_NAME = new ComboBoxItem("START_ADDR_NAME", Util.TI18N.LOAD_NAME());
			LOAD_NAME.setStartRow(true);
			LOAD_NAME.setWidth(FormUtil.Width);
			LOAD_NAME.setColSpan(2);
			LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
			
			final SGLText LOAD_ADDRESS = new SGLText("LOAD_ID_NAME",Util.TI18N.LOAD_ADDRESS());
			LOAD_ADDRESS.setDisabled(true);
			
			LOAD_NAME.addBlurHandler(new BlurHandler() {
				
				@Override
				public void onBlur(BlurEvent event) {
					if(!ObjUtil.isNotNull(LOAD_NAME.getValue())){
						LOAD_ID.setValue("");
						LOAD_AREA_NAME.setValue("");
						LOAD_AREA_NAME2.setValue("");
						LOAD_AREA_NAME3.setValue("");
						LOAD_NAME.setValue("");
						LOAD_ADDRESS.setValue("");
					}
					
				}
			});
		
			initLoadId(LOAD_NAME, LOAD_ADDRESS,LOAD_AREA_NAME,LOAD_AREA_NAME2,LOAD_AREA_NAME3,LOAD_ID); //初始化提货点
			//2
			final TextItem UNLOAD_ID = new TextItem("UNLOAD_ID");
			UNLOAD_ID.setVisible(false);
			
			final SGText UNLOAD_AREA_NAME = new SGText("END_AREA_ID_NAME", Util.TI18N.PROVINCE(),true);
			UNLOAD_AREA_NAME.setDisabled(true);
			UNLOAD_AREA_NAME.setStartRow(true);
			
			final SGText UNLOAD_AREA_NAME2 = new SGText("END_AREA_NAME2", Util.TI18N.CITY());
			UNLOAD_AREA_NAME2.setDisabled(true);
			
			final SGText UNLOAD_AREA_NAME3 = new SGText("END_AREA_NAME3", Util.TI18N.AREA());
			UNLOAD_AREA_NAME3.setDisabled(true);
			
			final ComboBoxItem UNLOAD_NAME = new ComboBoxItem("END_ADDR_NAME", Util.TI18N.UNLOAD_NAME());
			UNLOAD_NAME.setStartRow(true);
			UNLOAD_NAME.setWidth(FormUtil.Width);
			UNLOAD_NAME.setColSpan(2);
			UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
			
			final SGLText UNLOAD_ADDRESS = new SGLText("UNLOAD_ID_NAME",Util.TI18N.UNLOAD_ADDRESS());
			UNLOAD_ADDRESS.setDisabled(true);
			
			
			UNLOAD_NAME.addBlurHandler(new BlurHandler() {
				
				@Override
				public void onBlur(BlurEvent event) {
					if(!ObjUtil.isNotNull(UNLOAD_NAME.getValue())){
						UNLOAD_ID.setValue("");
						UNLOAD_AREA_NAME.setValue("");
						UNLOAD_AREA_NAME2.setValue("");
						UNLOAD_AREA_NAME3.setValue("");
						UNLOAD_NAME.setValue("");
						UNLOAD_ADDRESS.setValue("");
					}
					
				}
			});
		
			
			initRecvId(UNLOAD_NAME,UNLOAD_ADDRESS,UNLOAD_AREA_NAME,UNLOAD_AREA_NAME2,UNLOAD_AREA_NAME3,UNLOAD_ID); //初始化 卸货点
		
		
		SGCombo DFT_SUPLR_ID = new SGCombo("DFT_SUPLR_ID", Util.TI18N.DFT_SUPLR_ID());//供应商
		//DFT_SUPLR_ID.setWidth(112);
		
		SGCombo FOLLOWUP = new SGCombo("FOLLOWUP", Util.TI18N.FOLLOWUP(),true);//业务担当
		//FOLLOWUP.setWidth(112);
		Util.initComboValue(FOLLOWUP, "BAS_STAFF", "ID", "STAFF_NAME", " where enable_flag='Y' and staff_typ='AC465D51BEAC4B969E9E0AAF214DE1D7'", "", "");

		
		final SGCombo C_PACK_ID = new SGCombo("PACK_ID", Util.TI18N.C_PACK_ID());//包装
		//C_PACK_ID.setColSpan(1);
		//C_PACK_ID.setWidth(80);
		
		final SGCombo TRANS_UOM = new SGCombo("TRANS_UOM",Util.TI18N.TRANS_UOM());//单位
		//TRANS_UOM.setWidth(70);
		//TRANS_UOM.setColSpan(2);
		Util.initSupplier(DFT_SUPLR_ID, ""); //初始化供应商
		
		Util.initComboValue(C_PACK_ID, "BAS_PACKAGE", "ID", "PACK");
		//默认包装单位根据包装联动
		C_PACK_ID.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				String id = C_PACK_ID.getValue().toString();
				id =" where id='" +id +"'";
 				Util.initComboValue(TRANS_UOM, "V_BAS_PACKAGE", "DESCR", "UOM",id,"",null);
 				//RPC取数据
 				
			}
		});
		
		//4
		SGCombo LENGTH_UNIT = new SGCombo("LENGTH_UNIT", Util.TI18N.LENGHT_UNIT(),true);
		SGCombo VOLUME_UNIT = new SGCombo("VOLUME_UNIT", Util.TI18N.VOLUME_UNIT());
		SGCombo WEIGHT_UNIT = new SGCombo("WEIGHT_UNIT", Util.TI18N.WEIGHT_UNIT());
		
		Util.initComboValue(LENGTH_UNIT, "v_BAS_MSRMNT", "UNIT","UNIT_NAME", " WHERE MSRMNT_CODE='LENGHT'");
		Util.initComboValue(VOLUME_UNIT, "v_BAS_MSRMNT", "UNIT","UNIT_NAME", " WHERE MSRMNT_CODE='VOLUME'");
		Util.initComboValue(WEIGHT_UNIT, "v_BAS_MSRMNT", "UNIT","UNIT_NAME", " WHERE MSRMNT_CODE='WEIGHT'");
		//5
		TextItem sku_id = new TextItem("DFT_SKU_ID");
		sku_id.setVisible(false);
		SGText SKU_NAME = new SGText("SKU_NAME", Util.TI18N.DEFAULT_SKU_NAME(),true);//货品名称
		
		Util.initSku(SKU_NAME, sku_id, vm);
		
	    sku_name = SKU_NAME;
		SGCombo SKU_ATTR = new SGCombo("SKU_ATTR", Util.TI18N.SKU_ATTR());//货品性质
		Util.initCodesComboValue(SKU_ATTR,"SKU_ATTR");
		
		TextAreaItem TRANS_DEMAND = new TextAreaItem("TRANS_DEMAND", "运输要求");
		TRANS_DEMAND.setStartRow(true);
		TRANS_DEMAND.setColSpan(6);
		TRANS_DEMAND.setHeight(36);
		TRANS_DEMAND.setWidth(FormUtil.longWidth+FormUtil.Width);
		TRANS_DEMAND.setTitleOrientation(TitleOrientation.TOP);
		TRANS_DEMAND.setTitleVAlign(VerticalAlignment.TOP);
		
		//控制参数
		SGCheck MATCHROUTE_FLAG = new SGCheck("MATCHROUTE_FLAG", Util.TI18N.MATCHROUTE_FLAG(),true);
		MATCHROUTE_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		MATCHROUTE_FLAG.setColSpan(1);
		MATCHROUTE_FLAG.setWidth(FormUtil.Width/2);
		SGCheck UNIQ_CONO_FLAG = new SGCheck("UNIQ_CONO_FLAG", Util.TI18N.UNIQ_CONO_FLAG());
		UNIQ_CONO_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		UNIQ_CONO_FLAG.setColSpan(1);
		UNIQ_CONO_FLAG.setWidth(FormUtil.Width/2);
		SGCheck SKU_EDIT_FLAG = new SGCheck("SKU_EDIT_FLAG", Util.TI18N.SKU_EDIT_FLAG());
		SKU_EDIT_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		SKU_EDIT_FLAG.setColSpan(1);
		SKU_EDIT_FLAG.setWidth(FormUtil.Width/2);
		SGCheck SLF_DELIVER_FLAG = new SGCheck("SLF_DELIVER_FLAG", Util.TI18N.SLF_DELIVER_FLAG(),true);
		SLF_DELIVER_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		SLF_DELIVER_FLAG.setColSpan(1);
		SLF_DELIVER_FLAG.setWidth(FormUtil.Width/2);
		SGCheck SLF_PICKUP_FLAG = new SGCheck("SLF_PICKUP_FLAG", Util.TI18N.SLF_PICKUP_FLAG());
		SLF_PICKUP_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		SLF_PICKUP_FLAG.setColSpan(1);
		SLF_PICKUP_FLAG.setWidth(FormUtil.Width/2);
		SGCheck POD_FLAG = new SGCheck("POD_FLAG", Util.TI18N.ORD_POD_FLAG());
		POD_FLAG.setValue(true);
		POD_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		POD_FLAG.setColSpan(1);
		POD_FLAG.setWidth(FormUtil.Width/2);
		SGCheck COD_FLAG = new SGCheck("COD_FLAG", Util.TI18N.COD_FLAG(),true);
		COD_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		COD_FLAG.setColSpan(1);
		COD_FLAG.setWidth(FormUtil.Width/2);
		SGCheck UNIQ_ADDR_FLAG = new SGCheck("UNIQ_ADDR_FLAG", Util.TI18N.UNIQ_ADDR_FLAG());
		UNIQ_ADDR_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		UNIQ_ADDR_FLAG.setColSpan(1);
		UNIQ_ADDR_FLAG.setWidth(FormUtil.Width/2);
		SGCheck ADDR_EDIT_FLAG = new SGCheck("ADDR_EDIT_FLAG", Util.TI18N.ADDR_EDIT_FLAG());
		ADDR_EDIT_FLAG.setTitleOrientation(TitleOrientation.RIGHT);
		ADDR_EDIT_FLAG.setColSpan(1);
		ADDR_EDIT_FLAG.setWidth(FormUtil.Width/2);
		
		transForm = new SGPanel();
		transForm.setIsGroup(true);
		transForm.setWidth("40%");
		transForm.setBackgroundColor(ColorUtil.BG_COLOR);
		transForm.setGroupTitle(ColorUtil.getTitleNameWithCol("地址信息"));
	/*	transForm.setItems(ADDR_NAME,AREA_ID_NAME,LOAD_ID,LOAD_CODE,LOAD_ID_NAME,END_ADDR_NAME,END_AREA_ID_NAME,UNLOAD_ID,UNLOAD_CODE,UNLOAD_ID_NAME,DFT_SUPLR_ID,C_PACK_ID,TRANS_UOM,
				LENGTH_UNIT,VOLUME_UNIT,WEIGHT_UNIT,SKU_NAME,SKU_ATTR,FOLLOWUP);*/
		
		/*transForm.setItems(LOAD_AREA_NAME,LOAD_NAME,LOAD_ADDRESS,UNLOAD_AREA_NAME,
				UNLOAD_NAME,UNLOAD_ADDRESS,
				DFT_SUPLR_ID,C_PACK_ID,TRANS_UOM,
				LENGTH_UNIT,VOLUME_UNIT,WEIGHT_UNIT,SKU_NAME,SKU_ATTR,FOLLOWUP);*/
		transForm.setItems(LOAD_AREA_NAME,LOAD_AREA_NAME2,LOAD_AREA_NAME3,LOAD_ID,LOAD_NAME,LOAD_ADDRESS,
				UNLOAD_AREA_NAME,UNLOAD_AREA_NAME2,UNLOAD_AREA_NAME3,UNLOAD_ID,UNLOAD_NAME,UNLOAD_ADDRESS
				);
		
		transForm1 = new SGPanel();
		transForm1.setTitleWidth(70);
		transForm1.setWidth("40%");
		transForm1.setItems(SKU_NAME,C_PACK_ID,TRANS_UOM,LENGTH_UNIT,VOLUME_UNIT,
				WEIGHT_UNIT,FOLLOWUP,SKU_ATTR,DFT_SUPLR_ID,sku_id,TRANS_DEMAND
				);
		
		
		transForm2 = new SGPanel();
		transForm2.setIsGroup(true);
		transForm2.setWidth("40%");
		transForm2.setBackgroundColor(ColorUtil.BG_COLOR);
		transForm2.setGroupTitle(ColorUtil.getTitleNameWithCol("控制参数"));
		transForm2.setItems(MATCHROUTE_FLAG,UNIQ_CONO_FLAG,SKU_EDIT_FLAG,SLF_DELIVER_FLAG,SLF_PICKUP_FLAG
				,POD_FLAG,COD_FLAG,UNIQ_ADDR_FLAG,ADDR_EDIT_FLAG);
		VLayout lay = new VLayout();
		lay.setWidth100();
		lay.setHeight100();
		lay.setBackgroundColor(ColorUtil.BG_COLOR);
		lay.addMember(transForm);
		lay.addMember(transForm1);
		lay.addMember(transForm2);
		
		orgDs = CustOrgDS.getInstance("BAS_CUSTOMER_ORG");//执行机构数据源
		
		final SGPanel orgForm = new SGPanel();
		orgForm.setDataSource(orgDs); //设定数据源
		final TextItem ORG_ID = new TextItem("ORG_ID"); //隐藏字段
		ORG_ID.setVisible(false);
		final TextItem ORG_ID_NAME = new TextItem("ORG_NAME", Util.TI18N.C_ORG_ID());
	
		CheckboxItem C_ORG_FLAG = new CheckboxItem("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(1);
		CheckboxItem d_flag = new CheckboxItem("DEFAULT_FLAG", Util.TI18N.DEFAULT_FLAG());
		d_flag.setColSpan(1);
		
		ORG_ID_NAME.setColSpan(1);
		ORG_ID_NAME.setWidth(160);
        PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);		//查询按钮
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new OrgWin(ORG_ID_NAME,ORG_ID,false,"70%","40%").getViewPanel();
			}
		});
        ORG_ID_NAME.setIcons(searchPicker);
        
		orgTable = getOrgTab(orgDs);
		orgTable.addRecordClickHandler(new CellClickAction(orgForm,OP_FLAG)); //列表行点击事件

		//执行机构新增、保存、删除、取消按钮
//		SGPanel btnPanel = new SGPanel();
//		btnPanel.setNumCols(24);
//		SGButtonItem newB = new SGButtonItem(StaticRef.CREATE_BTN, true);
//		newB.setColSpan(2);
//		newB.addClickHandler(new NewFormAction(orgForm, null));
//		
//		SGButtonItem savB = new SGButtonItem(StaticRef.SAVE_BTN,false);
//		savB.setColSpan(2);
//		savB.addClickHandler(new SaveCustOrgAction(orgTable, orgForm, table));
//		SGButtonItem delB = new SGButtonItem(StaticRef.DELETE_BTN,false);
//		delB.setColSpan(2);
//		delB.addClickHandler(new DeleteFormAction(orgTable, orgForm));
//		SGButtonItem canB = new SGButtonItem(StaticRef.CANCEL_BTN,false);
//		canB.setColSpan(2);
//		canB.addClickHandler(new CancelFormAction(orgTable, orgForm));
//		btnPanel.setItems(newB,savB,delB,canB);
		
		
		IButton newB = createBtn(StaticRef.CREATE_BTN,BasPrivRef.CUSTOM_P2_P2_01);//新增按钮
		newB.setIcon(StaticRef.ICON_NEW);
		newB.setWidth(60);
//		saveBtn.setEndRow(false);
		newB.setAutoFit(true);
		newB.setAlign(Alignment.RIGHT);
		newB.addClickHandler(new NewFormAction(orgForm, null));

		IButton savB = createBtn(StaticRef.SAVE_BTN,BasPrivRef.CUSTOM_P2_P2_02);//保存
		savB.setIcon(StaticRef.ICON_SAVE);
		savB.setWidth(60);
//		saveBtn.setEndRow(false);
		savB.setAutoFit(true);
		savB.setAlign(Alignment.RIGHT);
		savB.addClickHandler(new SaveCustOrgAction(orgTable, orgForm, table));

		IButton delB = createBtn(StaticRef.DELETE_BTN,BasPrivRef.CUSTOM_P2_P2_03);//删除
		delB.setIcon(StaticRef.ICON_DEL);
		delB.setWidth(60);
//		saveBtn.setEndRow(false);
		delB.setAutoFit(true);
		delB.setAlign(Alignment.RIGHT);
		delB.addClickHandler(new DeleteFormAction(orgTable, orgForm));

		IButton canB = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.CUSTOM_P2_P2_03);//取消
		canB.setIcon(StaticRef.ICON_CANCEL);
		canB.setWidth(60);
//		saveBtn.setEndRow(false);
		canB.setAutoFit(true);
		canB.setAlign(Alignment.RIGHT);
		canB.addClickHandler(new CancelFormAction(orgTable, orgForm));

		IButton sync = createBtn(StaticRef.CONFIRM_BTN,BasPrivRef.CUSTOM_P2_P2_05);//导出
		sync.setIcon(StaticRef.ICON_CONFIRM);
		sync.setWidth(60);
		sync.setAutoFit(true);
		sync.setTitle("同步客户");
		sync.setAlign(Alignment.RIGHT);
		sync.addClickHandler(new SyncPrivilegeAction(orgTable,table));
		
		toolStrip1 = new ToolStrip();//按钮布局
		toolStrip1.setAlign(Alignment.RIGHT);
		toolStrip1.setWidth("100%");
		toolStrip1.setHeight("20");
		toolStrip1.setPadding(2);
		toolStrip1.setSeparatorSize(12);
		toolStrip1.addSeparator();
		toolStrip1.setMembersMargin(4);
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
		
        toolStrip1.setMembers(newB,savB,delB,canB,sync);
		
		
		
		orgForm.setItems(ORG_ID_NAME,C_ORG_FLAG,d_flag,ORG_ID);
		
		
		
		VLayout lay2 = new VLayout();
		lay2.addMember(orgTable);
		lay2.addMember(orgForm);
//		lay2.addMember(btnPanel);
		lay2.addMember(toolStrip1);
		
		//订单类型
		ordDs = CustOrdDS.getInstance("BAS_CUSTOMER_ORD_TYP");//订单类型数据源
		final SGPanel ordForm = new SGPanel();
		ordForm.setDataSource(ordDs);
		ordForm.setTitleWidth(80);
		SGText ORD_NAME = new SGText("ORD_NAME", Util.TI18N.ORD_NAME());
		ORD_NAME.setTitleOrientation(TitleOrientation.LEFT);
		
		SGCheck default_flag = new SGCheck("DEFAULT_FLAG", Util.TI18N.DEFAULT_FLAG());
		ordForm.setItems(ORD_NAME,default_flag);
		default_flag.setTitleOrientation(TitleOrientation.LEFT);
		
		ordTable = getOrdTyp();
		ordTable.setDataSource(ordDs);
		ordTable.addRecordClickHandler(new CellClickAction(ordForm,OP_FLAG));
		
		//订单类型新增、保存、删除、取消按钮
////		SGPanel btnPanel2 = new SGPanel();
//		SGPanel btnPanel2 = new SGPanel();
//		btnPanel2.setBackgroundColor(ColorUtil.BG_COLOR);
//		btnPanel2.setAlign(Alignment.RIGHT);
//		btnPanel2.setNumCols(24);
////		btnPanel2.setCellPadding(20);
//		SGButtonItem newB2 = new SGButtonItem(StaticRef.CREATE_BTN, true);
//		newB2.addClickHandler(new NewFormAction(ordForm, null));
//		newB2.setColSpan(2);
//		SGButtonItem savB2 = new SGButtonItem(StaticRef.SAVE_BTN);
//		savB2.setColSpan(2);
//		savB2.addClickHandler(new SaveCustOrdAction(ordTable, ordForm, table));
//		SGButtonItem delB2 = new SGButtonItem(StaticRef.DELETE_BTN);
//		delB2.addClickHandler(new DeleteFormAction(ordTable, ordForm));
//		delB2.setColSpan(2);
//		SGButtonItem canB2 = new SGButtonItem(StaticRef.CANCEL_BTN);
//		canB2.setColSpan(2);
//		canB2.addClickHandler(new CancelFormAction(ordTable, ordForm));
//		btnPanel2.setItems(newB2,savB2,delB2,canB2);
		
		IButton newB2 = createBtn(StaticRef.CREATE_BTN,BasPrivRef.CUSTOM_P2_P4_01);
		newB2.setIcon(StaticRef.ICON_NEW);
		newB2.setWidth(60);
//		saveBtn.setEndRow(false);
		newB2.setAutoFit(true);
		newB2.setAlign(Alignment.RIGHT);
		newB2.addClickHandler(new NewFormAction(ordForm, null));
		
		IButton savB2 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.CUSTOM_P2_P4_02);
		savB2.setIcon(StaticRef.ICON_SAVE);
		savB2.setWidth(60);
//		saveBtn.setEndRow(false);
		savB2.setAutoFit(true);
		savB2.setAlign(Alignment.RIGHT);
		savB2.addClickHandler(new SaveCustOrdAction(ordTable, ordForm, table));
		
		IButton delB2 = createBtn(StaticRef.DELETE_BTN,BasPrivRef.CUSTOM_P2_P4_03);
		delB2.setIcon(StaticRef.ICON_DEL);
		delB2.setWidth(60);
//		saveBtn.setEndRow(false);
		delB2.setAutoFit(true);
		delB2.setAlign(Alignment.RIGHT);
		delB2.addClickHandler(new DeleteFormAction(ordTable, ordForm));
		
		IButton canB2 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.CUSTOM_P2_P4_04);
		canB2.setIcon(StaticRef.ICON_CANCEL);
		canB2.setWidth(60);
//		saveBtn.setEndRow(false);
		canB2.setAutoFit(true);
		canB2.setAlign(Alignment.RIGHT);
		canB2.addClickHandler(new CancelFormAction(ordTable, ordForm));
		
		toolStrip2 = new ToolStrip();//按钮布局
		toolStrip2.setAlign(Alignment.RIGHT);
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		toolStrip2.setPadding(2);
		toolStrip2.setSeparatorSize(12);
		toolStrip2.addSeparator();
		toolStrip2.setMembersMargin(4);
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
		
        toolStrip2.setMembers(newB2,savB2,delB2,canB2);
		
		VLayout lay3 = new VLayout();
		lay3.addMember(ordTable);
		lay3.addMember(ordForm);
//		lay3.addMember(btnPanel2);
		lay3.addMember(toolStrip2);

		
		//运输服务
		servDs = CustServDS.getInstance("BAS_CUSTOMER_TRANS_SRVC");
		servTable = getTransServ();
		servTable.setDataSource(servDs);
		
		final SGPanel serForm = new SGPanel();  
		serForm.setDataSource(servDs);
		serForm.setTitleWidth(80);
		SGCombo SERVICE_NAME = new SGCombo("TRANS_SRVC_ID", Util.TI18N.SERVICE_NAME());
		SGCheck default_flag_3 = new SGCheck("DEFAULT_FLAG", Util.TI18N.DEFAULT_FLAG());
		serForm.setItems(SERVICE_NAME,default_flag_3);
		servTable.addRecordClickHandler(new CellClickAction(serForm,OP_FLAG)); //列表行点击事件
		Util.initTrsService(SERVICE_NAME, "");
		
		//运输服务新增、保存、删除、取消按钮
//		SGPanel btnPanel3 = new SGPanel();
////		SGButton newB3 = new SGButton(StaticRef.CREATE_BTN, true);
////		newB3.addClickHandler(new NewFormAction(serForm, null));
//		SGButtonItem savB3 = new SGButtonItem(StaticRef.SAVE_BTN,false);
//		savB3.addClickHandler(new SaveCustSrvcAction(servTable, serForm,table));
////		SGButton delB3 = new SGButton(StaticRef.DELETE_BTN,false);
////		delB3.addClickHandler(new DeleteFormAction(servTable, serForm));
//		SGButtonItem canB3 = new SGButtonItem(StaticRef.CANCEL_BTN,false);
//		canB3.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//			
//			@Override
//			public void onClick(
//					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				servTable.discardAllEdits();
//				
//			}
//		});
//		btnPanel3.setItems(savB3,canB3);
		
		IButton savB3 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.CUSTOM_P2_P3_01);
		savB3.setIcon(StaticRef.ICON_SAVE);
		savB3.setWidth(60);
		savB3.setAutoFit(true);
		savB3.setAlign(Alignment.RIGHT);
		savB3.addClickHandler(new SaveCustSrvcAction(servTable, serForm,table));
		
		IButton canB3 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.CUSTOM_P2_P3_02);
		canB3.setIcon(StaticRef.ICON_CANCEL);
		canB3.setWidth(60);
		canB3.setAutoFit(true);
		canB3.setAlign(Alignment.RIGHT);
//		canB3.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//			
//			@Override
//			public void onClick(
//					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				servTable.discardAllEdits();
//				
//			}
//		});
		
		canB3.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				servTable.discardAllEdits();
			}
		});
		
		toolStrip3 = new ToolStrip();//按钮布局
		toolStrip3.setAlign(Alignment.RIGHT);
		toolStrip3.setWidth("100%");
		toolStrip3.setHeight("20");
		toolStrip3.setPadding(2);
		toolStrip3.setSeparatorSize(12);
		toolStrip3.addSeparator();
		toolStrip3.setMembersMargin(4);
        toolStrip3.setMembers(savB3,canB3);
        
		VLayout lay4 = new VLayout();
		lay4.addMember(servTable);
//		lay4.addMember(serForm);
		
//		lay4.addMember(btnPanel3);
		lay4.addMember(toolStrip3);
		
		
		if(isPrivilege(BasPrivRef.CUSTOM_P2_P1)) {
			Tab tab1 = new Tab(Util.TI18N.CONTROL_INFO());
			tab1.setPane(lay);
			chTabSet.setTabs(tab1);
		}
		
		if(isPrivilege(BasPrivRef.CUSTOM_P2_P2)) {
			Tab tab2 = new Tab("执行机构分配");
			tab2.setPane(lay2);
			chTabSet.setTabs(tab2);
		}
		
		if(isPrivilege(BasPrivRef.CUSTOM_P2_P3)) {
			Tab tab3 = new Tab("运输服务");
			tab3.setPane(lay4);
			chTabSet.setTabs(tab3);
		}
		
		if(isPrivilege(BasPrivRef.CUSTOM_P2_P4)) {
			Tab tab4 = new Tab("运输类型");
			tab4.setPane(lay3);
			chTabSet.setTabs(tab4);
		}
		
		//页签切换事件
		chTabSet.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				int num = event.getTabNum();//页签num
				t_pageNum = num; //保存当前页签数
				TransInfoTabChange(num);
			}
		});
		
        return chTabSet;
	}
		
	private VLayout createImgInfo(){
		VLayout vLay = new VLayout();
        vLay.setWidth100();
        vLay.setBackgroundColor(ColorUtil.BG_COLOR);
        
        HTMLPane htmlPane = new HTMLPane();
		htmlPane.setContentsType(ContentsType.PAGE);
		htmlPane
				.setContents("<iframe name='foo' style='position:absolute;width:0;height:0;border:0'></iframe>");
		htmlPane.setWidth("1");
		htmlPane.setHeight("1");
		
        final DynamicForm uploadForm = new DynamicForm();
        uploadForm.setAction("uploadServlet");
        uploadForm.setEncoding(Encoding.MULTIPART);
        uploadForm.setMethod(FormMethod.POST);
        uploadForm.setTarget("foo");
        
        final UploadItem imageItem = new UploadItem("image","图片");
        
        TextItem notes = new TextItem("NOTES","描述");
        
        SGButtonItem saveItem = new SGButtonItem("上传",StaticRef.ICON_IMPORT);
        setButtonItemEnabled(BasPrivRef.CUSTOM_P3_P1,saveItem,true);
        saveItem.setWidth(60);
        saveItem.setAutoFit(true);
        saveItem.setIcon(StaticRef.ICON_NEW);
        saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord() !=  null){
					if(table.getSelectedRecord().getAttribute("CUSTOMER_CODE") != null){
						Map<String,String> map = uploadForm.getValues();
						if(map.get("image") != null){
							String customer = table.getSelectedRecord().getAttribute("CUSTOMER_CODE").toString();
							String image = map.get("image").toString();
							new ImageUploadAction(customer,StaticRef.BAS_SKU_URL,image,uploadForm).onClick(event);	
							if(table.getSelectedRecord() != null){
								if(canvas != null){
									if(canvas.isCreated()){
										canvas.destroy();
									}
								}
								canvas = new Canvas();
								tileCanvas.addChild(canvas);
								canvas.setHeight(163);
								canvas.setWidth(1000);
							    if(table.getSelectedRecord().getAttribute("CUSTOMER_CODE") != null){
									ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_SKU_PREVIEW_URL,table.getSelectedRecord().getAttribute("CUSTOMER_CODE"));
									action.getName();
							    }
							}
						
						}else{
							MSGUtil.sayWarning("请选择所要上传的图片");
						}
					}
				} else {
					MSGUtil.sayWarning("请选择上传图片对应的货品编号.");
				}
			}
		});
        
       // 下载
        SGButtonItem downLoadButton = new SGButtonItem("下载",StaticRef.ICON_IMPORT);
        setButtonItemEnabled(BasPrivRef.CUSTOM_P3_P2,downLoadButton,true);
        downLoadButton.setWidth(60);
        downLoadButton.setAutoFit(true);
        downLoadButton.setIcon(StaticRef.ICON_NEW);
        downLoadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
				String cusId=record.getAttribute("CUSTOMER_CODE");
					//Util.downLoadUtil(cusId);
				String filePath="test/SKU/"+cusId;
				doDownFile(filePath);
				}
				
			}
			
        });	
//        
        uploadForm.setItems(imageItem,notes,saveItem,downLoadButton);
        
        tileCanvas = new Canvas();
        tileCanvas.setBorder("1px solid black");
        tileCanvas.setHeight(200);  
        tileCanvas.setWidth100(); 
        tileCanvas.setShowResizeBar(true);
     
        vLay.setMembers(uploadForm,tileCanvas,htmlPane);
        
        return vLay;
	}
	
	/*private TabSet createWMSInfo(){
		
		TabSet chTabSet = new TabSet();  
		chTabSet.setWidth100();   
		chTabSet.setHeight100(); 
		chTabSet.setMargin(0);
		chTabSet.setTabBarPosition(Side.BOTTOM);
		
		if(isPrivilege(BasPrivRef.CUSTOM_P3_P1)) {
			Tab tab1 = new Tab(Util.TI18N.CONTROL_INFO());
			chTabSet.setTabs(tab1);
		}
		
		if(isPrivilege(BasPrivRef.CUSTOM_P3_P2)) {
			Tab tab2 = new Tab("客户仓库分配");
			chTabSet.setTabs(tab2);
		}
		
		return chTabSet;
	}*/
	//执行机构分配
	private SGTable getOrgTab(DataSource orgDs){
		
		SGTable table = new SGTable(orgDs, "100%", "90%",true,true,false);
		table.setShowRowNumbers(true);
		table.setSelectionType(SelectionStyle.SIMPLE);  
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
		ListGridField ORG_ID_NAME = new ListGridField("ORG_NAME",Util.TI18N.ORG_ID_NAME(),140);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(ORG_ID_NAME,DEFAULT_FLAG);
		return table;
	}
	//运输服务
	private SGTable getTransServ(){
		final SGTable table1 = new SGTable(ds);
		table1.setShowRowNumbers(true);
		table1.setEditEvent(ListGridEditEvent.CLICK);
//		table1.setSelectionType(SelectionStyle.SIMPLE);  
//		table1.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
		ListGridField USE_FLAG = new ListGridField("USE_FLAG","选择",60);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		USE_FLAG.setCanEdit(true);
		ListGridField SERVICE_NAME = new ListGridField("SERVICE_NAME",Util.TI18N.SERVICE_NAME(),140);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		DEFAULT_FLAG.setCanEdit(true);
		
		table1.setFields(USE_FLAG,SERVICE_NAME,DEFAULT_FLAG);
		
		DEFAULT_FLAG.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				
				if((!table1.getRecord(event.getRowNum()).getAttributeAsBoolean("USE_FLAG") && "N".equals(Util.getFlag(table1.getEditValue(event.getRowNum(), "USE_FLAG")))) 
						&& Boolean.parseBoolean(event.getNewValue().toString())){
					MSGUtil.sayError(Util.MI18N.MODIFY_DEFAULT_EORRO());
					table1.setEditValue(event.getRowNum(), "DEFAULT_FLAG", false);
				}
				
			}
		});
		
		USE_FLAG.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(!Boolean.parseBoolean(event.getNewValue().toString())){
					table1.setEditValue(event.getRowNum(), "DEFAULT_FLAG", false);
				}
				
			}
		});
		return table1;
	}
	
	//订单类型
	private SGTable getOrdTyp(){
//		orgDs = CustOrgDS.getInstance("BAS_CUSTOMER_ORG");
		SGTable table1 = new SGTable(ds);
		table1.setShowRowNumbers(true);
		table1.setHeight("90%");
		table1.setSelectionType(SelectionStyle.SIMPLE);  
//		table1.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
		ListGridField ORD_NAME = new ListGridField("ORD_NAME",Util.TI18N.ORD_NAME(),140);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table1.setFields(ORD_NAME,DEFAULT_FLAG);
		return table1;
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.CUSTOM);
        
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
        	
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.CUSTOM_P0_01);
        toolStrip.addMember(newButton);
        newButton.addClickHandler(new NewCustomerAction(vm, cache_map,customer_code,sku_name,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.CUSTOM_P0_02);
        toolStrip.addMember(saveButton);
        saveButton.addClickHandler(new SaveCustomerAction(table,  vm, check_map,this,PARENT_CUSTOMER_ID));
//        saveButton.disable();
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.CUSTOM_P0_03);
        delButton.addClickHandler(new DeleteProAction(table, vm));
        toolStrip.addMember(delButton);
//        delButton.disable();
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.CUSTOM_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(table, vm,this));
        toolStrip.addMember(canButton);
//        toolStrip.addSeparator();
        
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.CUSTOM_P0_05);
        toolStrip.addMember(expButton);
        expButton.addClickHandler(new ExportAction(table,""));
        
        IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.CUSTOM_P0_06);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					
					JavaScriptObject jsobject = m_section.getSection(0).getAttributeAsJavaScriptObject("controls");
					Canvas[] canvas = null;
					DynamicForm pageForm=null;
					if(jsobject != null) {
						canvas = Canvas.convertToCanvasArray(jsobject);
					}
					for(int i = 0; i < canvas.length; i++) {
						if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
							pageForm = (DynamicForm)canvas[i];
							break;
						}
					}
					uploadWin = new UploadFileWin().getViewPanel("customer.xls","TMP_CUSTOMER_IMPORT","SP_IMPORT_CUSTOMER",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});
		toolStrip.addMember(inputButton);
		 
        add_map.put(BasPrivRef.CUSTOM_P0_01, newButton);
        del_map.put(BasPrivRef.CUSTOM_P0_03, delButton);
        save_map.put(BasPrivRef.CUSTOM_P0_02, saveButton);
        save_map.put(BasPrivRef.CUSTOM_P0_04, canButton);
        
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        toolStrip.setMembersMargin(4);
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
	
		//SGCombo PARENT_CUSTOMER_ID = new SGCombo("PARENT_CUSTOMER_ID",Util.TI18N.PARENT_CUSTOMER_ID(),true);
		//Util.initCustComboValue(PARENT_CUSTOMER_ID, "");
		
		SGText CUSTOMER_CODE = new SGText("CUSTOMER_CODE","客户代码");
		
//		SGText MAINT_ORG_ID = new SGText("MAINT_ORG_ID_NAME",Util.TI18N.MAINT_ORG_ID());
//		Util.initOrg(MAINT_ORG_ID, null, true, "48%", "40%");
////		Util.initComboValue(MAINT_ORG_ID, "BAS_ORG", "ID", "ORG_CNAME"," WHERE ENABLE_FLAG='Y'");
//		MAINT_ORG_ID.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		final TextItem ID=new TextItem("ID");
		ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME = new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, ID);
		
		
		
//		final TextItem PARENT_CUSTOMER_ID=new TextItem("PARENT_CUSTOMER_ID");
//		PARENT_CUSTOMER_ID.setVisible(false);
//		
//		final ComboBoxItem PARENT_CUSTOMER_NAME = new ComboBoxItem("PARENT_CUSTOMER_NAME",Util.TI18N.PARENT_CUSTOMER_ID());
//		PARENT_CUSTOMER_NAME.setWidth(FormUtil.Width);
//		PARENT_CUSTOMER_NAME.setColSpan(2);
//		PARENT_CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
//		PARENT_CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
//		Util.initCustomerByQuery(PARENT_CUSTOMER_NAME, PARENT_CUSTOMER_ID);

		
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
	
	//初始化提货点下拉框
	private void initLoadId(final ComboBoxItem load_code,final SGLText address,final SGText area_id_name,final SGText area_name2,final SGText area_name3,final TextItem load_id){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
//		load_code.setWidth(120);  
//		load_code.setColSpan(2);
		load_code.setOptionDataSource(ds2);  
		load_code.setDisabled(false);
		load_code.setShowDisabled(false);
		load_code.setDisplayField("FULL_INDEX");  
		load_code.setPickListWidth(450);
	
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_LOAD_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		load_code.setPickListCriteria(criteria);
		
		load_code.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		load_code.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_code.getSelectedRecord();
				if(selectedRecord != null){
					load_code.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					area_id_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					load_id.setValue(selectedRecord.getAttribute("ID"));
					if(area_name2 != null) {
						area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					}
					if(area_name3 != null) {
						area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					}
				}
			}
		});
	}
	
	
	//初始化卸货点下拉框
	private void initRecvId(final ComboBoxItem load_id,final SGLText address,final SGText area_id_name,final SGText area_name2,final SGText area_name3,final TextItem unload_id){
		DataSource ds2 = AddrDS.getInstance("BAS_ADDRESS");
		ListGridField ADDR_CODE = new ListGridField("ADDR_CODE");
		ListGridField ADDR_NAME = new ListGridField("ADDR_NAME");
		ListGridField AREA_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField ADDRESS = new ListGridField("ADDRESS");
//		load_id.setWidth(120);  
//		load_id.setColSpan(2);
		load_id.setOptionDataSource(ds2);  
		load_id.setDisplayField("FULL_INDEX");  
		load_id.setPickListWidth(450);
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		criteria.addCriteria("AND_RECV_FLAG","Y");
		criteria.addCriteria("ENABLE_FLAG","Y");
		load_id.setPickListCriteria(criteria);
		
		load_id.setPickListFields(ADDR_CODE, ADDR_NAME,AREA_NAME,ADDRESS);
		
		load_id.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				Record selectedRecord  = load_id.getSelectedRecord();
				if(selectedRecord != null){
					load_id.setValue(selectedRecord.getAttribute("ADDR_NAME"));
					address.setValue(selectedRecord.getAttribute("ADDRESS"));
					area_id_name.setValue(selectedRecord.getAttribute("AREA_ID_NAME"));
					unload_id.setValue(selectedRecord.getAttribute("ID"));
					if(area_name2 != null) {
						area_name2.setValue(selectedRecord.getAttribute("AREA_NAME2"));
					}
					if(area_name3 != null) {
						area_name3.setValue(selectedRecord.getAttribute("AREA_NAME3"));
					}
				}
			}
		});
		
	}
	
	/**
	 * 运输信息中二级页签切换执行页签刷新
	 * @param num
	 * @author fanglm
	 */
	private void TransInfoTabChange(int num){
		if(ObjUtil.isNotNull(cust_id) && num != 0 && m_pageNum == 1){
			Criteria criteria = new Criteria();
            criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
            criteria.addCriteria("CUSTOMER_ID", cust_id);
			if(num == 1){
                orgTable.fetchData(criteria);
			}else if(num == 2){
				servTable.discardAllEdits();
				servTable.fetchData(criteria);
			}
			else if(num == 3){
				ordTable.fetchData(criteria);
			}
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasCustomerView view = new BasCustomerView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		// TODO Auto-generated method stub
		return getID();
	}
	public native static void doDownFile(String fileName)/*-{
		var url = $wnd.location.href;
		url = url.substring(0, url.lastIndexOf('/'));
		url = url+'/images/'+fileName;
		$wnd.open(url);
     }-*/;
}
