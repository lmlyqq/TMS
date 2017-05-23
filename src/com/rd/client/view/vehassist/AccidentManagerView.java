package com.rd.client.view.vehassist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.AccidentSaveAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.vehassist.AccidentLogDS;
import com.rd.client.ds.vehassist.AccidentManagerDS;
import com.rd.client.obj.system.SYS_USER;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
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
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
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
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理->事故管理
 * @author 
 *
 */
@ClassForNameAble
public class AccidentManagerView extends SGForm implements PanelFactory {

	private DataSource mainDS;
	private DataSource detailDS;
	private SGTable propTable;
	private SGTable codeTable;
	private SGPanel main_form;
	private SGPanel main_form1;
	//private SectionStack stack;
	private SectionStack section ;
	private Window searchWin = null;
	private SGPanel searchForm;
	private HashMap<String,String> detail_ck_map ;
	private HashMap<String,String> detail_map;
	public ToolStrip toolStrip2;
	private HashMap<String, IButton> add_detail_map;
	private HashMap<String, IButton> save_detail_map;
	private HashMap<String, IButton> del_detail_map;
	private IButton newButton;
	private IButton searchButton;
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	/*public AccidentManagerView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		//初始化主界面、按钮布局、Section布局
		VLayout main = new VLayout();
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		detail_ck_map = new HashMap<String,String>();
		detail_map = new HashMap<String,String>();
		add_detail_map=new HashMap<String, IButton>();
		save_detail_map=new HashMap<String, IButton>();
		del_detail_map=new HashMap<String, IButton>();
		//创建表格和数据源
		mainDS = AccidentManagerDS.getInstance("INSUR_ACCIDENT_LIST", "INSUR_ACCIDENT_LIST");
		
		HStack Stack = new HStack();// 设置详细信息布局
		Stack.setWidth100();
		Stack.setHeight("60%");
		
		propTable = new SGTable(mainDS, "100%", "55%", false, true, false);
	//	propTable.setCanEdit(true);
	//	propTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		propTable.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionEvent event) {
				Record record = event.getRecord();
				Criteria findValues = new Criteria();
	            findValues.addCriteria("INSUR_ID", record.getAttribute("ID"));
	            findValues.addCriteria("OP_FLAG", codeTable.OP_FLAG);
	            codeTable.PKEY = "ID";
	            codeTable.PVALUE = record.getAttribute("ID");
	            codeTable.fetchData(findValues);
	            
	            Record selectedRecord  = event.getRecord();
            	main_form.editRecord(selectedRecord);
            	main_form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                //enableOrDisables(del_map, true);
                initSaveBtn();
			}
			
		});
		
		detailDS = AccidentLogDS.getInstance("INSUR_ACCIDENT_LOG", "INSUR_ACCIDENT_LOG");
		codeTable = new SGTable(detailDS,"100%","45%",false,true,false);
		codeTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				
	            Record selectedRecord  = event.getRecord();
            	main_form1.editRecord(selectedRecord);
            	main_form1.setValue("OP_FLAG", StaticRef.MOD_FLAG);
            	//main_form1.setValue("STATUS", codeTable.getSelectedRecord().getAttribute("STATUS_NAME"));
                //enableOrDisables(del_map, true);
                initSaveBtn();
			}
		});
		getConfigList();
		
		//创建按钮布局
		newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.ACCIDENT_P0_01);
		
		
		section= new SectionStack();
		final SectionStackSection listItem = new SectionStackSection();
		listItem.setItems(propTable);
		listItem.setExpanded(true);
		//listItem.setControls(new SGPage(propTable, false).initPageBtn());
		section.addSection(listItem);
		section.setWidth("60%");
		section.setHeight("100%");

		SectionStack section1 = new SectionStack();
		section1.setWidth("40%");
		section1.setHeight("100%");
		final SectionStackSection f1 = new SectionStackSection(Util.TI18N.BAS_INFO());
		main_form=new SGPanel();
		createMainForm(main_form);
		f1.setItems(main_form);
		section1.addSection(f1);
		
		
		SectionStack section2= new SectionStack();
		final SectionStackSection t2 = new SectionStackSection();
		t2.setItems(codeTable);
		t2.setExpanded(true);
		//listItem.setControls(new SGPage(propTable, false).initPageBtn());
		section2.addSection(t2);
		section2.setWidth("60%");
		section2.setHeight("100%");
		
		
		SectionStack section3 = new SectionStack();
		section3.setWidth("40%");
		section3.setHeight("100%");
		main_form1=new SGPanel();
		//main_form1.setHeight("43%");
		SectionStackSection f2 = new SectionStackSection(Util.TI18N.CONTRO_INFO());
		f2.addItem(createMainForm1(main_form1));
        f2.setExpanded(true);
        section3.addSection(f2);
		
        HStack Stack1= new HStack();// 设置详细信息布局
		Stack1.setWidth100();
		Stack1.setHeight("40%");
		
		HStack bottomStack = new HStack();// 设置详细信息布局
		bottomStack.setWidth100();
		bottomStack.setHeight100();
		
		bottomStack.addMember(section2);
		bottomStack.addMember(section3);
        
		HStack Stack2= new HStack();// 设置详细信息布局
		Stack2.setWidth100();
		Stack2.setHeight("55%");
		
		HStack bottomStack1 = new HStack();// 设置详细信息布局
		bottomStack1.setWidth100();
		bottomStack1.setHeight100();
		
		bottomStack1.addMember(section);
		bottomStack1.addMember(section1);
		
		
		Stack1.addMember(bottomStack);
		Stack2.addMember(bottomStack1);
		Stack.addMember(section);
		
	    createBtnWidget(toolStrip);
        main.setWidth100();
        main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(Stack2);
		main.addMember(Stack1);
		
		propTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
		        enableOrDisables(save_map, true);
				
			}
		});
//         codeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
//			
//			@Override
//			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
//				enableOrDisables(add_detail_map, false);
//				enableOrDisables(del_detail_map, false);
//		        enableOrDisables(save_detail_map, true);
//				
//			}
//		});
		propTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_map, true);
				enableOrDisables(add_detail_map, true);
				enableOrDisables(del_detail_map, false);
				enableOrDisables(save_detail_map, false);
				
			}
		});
        
		
		codeTable.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_detail_map, false);
				enableOrDisables(del_detail_map, false);
		        enableOrDisables(save_detail_map, true);
		        //Util.initCodesComboValue(main_form1.getField("STATUS"),"ACCIDENT_STATUS",);
		    	//String where=" where PROP_CODE='ACCIDENT_STATUS' and CODE = '"+codeTable.getSelectedRecord().getAttribute("STATUS_CODE")+"' ";
		        // Util.initComboValue(main_form1.getField("STATUS"),"ACCIDENT_STATUS");
		        Util.initCodesComboValue(main_form1.getField("STATUS"),"ACCIDENT_STATUS");
		        main_form1.getField("STATUS").setDisabled(true);
			}
		});
		
		codeTable.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(add_detail_map, true);
				enableOrDisables(del_detail_map, true);
				enableOrDisables(save_detail_map, false);
				Util.initCodesComboValue(main_form1.getField("STATUS"),"ACCIDENT_STATUS");
			}
		});
		
		
		
		initVerify();
		return main;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        searchButton = createBtn(StaticRef.FETCH_BTN);

        newButton.addClickHandler(new NewFormAction(main_form,cache_map,this));
        newButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				main_form.setValue("LOSS_AMOUNT", "0");
				main_form.setValue("CLAIM_AMOUNT", "0");
				main_form.setValue("SETTLE_AMOUNT", "0");
				main_form.setValue("STATUS", "10");
			}
		});
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.ACCIDENT_P0_02);
        saveButton.addClickHandler(new SaveFormAction(propTable, main_form, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.ACCIDENT_P0_03);
        delButton.addClickHandler(new DeleteFormAction(propTable, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.ACCIDENT_P0_04);
        canButton.addClickHandler(new CancelFormAction(propTable, main_form,this));
        
        IButton expButton = createBtn("办事处上报",BasPrivRef.ACCIDENT_P0_05);
       // expButton.addClickHandler(new ExportAction(propTable));
        
        IButton newSubButton = createUDFBtn("案件资料", StaticRef.ICON_NEW,BasPrivRef.ACCIDENT_P0_06);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton,newButton,saveButton,delButton,canButton,expButton,newSubButton);

        
      //主表按钮联动
        add_map.put(BasPrivRef.ACCIDENT_P0_01, newButton);
        del_map.put(BasPrivRef.ACCIDENT_P0_03, delButton);
        save_map.put(BasPrivRef.ACCIDENT_P0_02, saveButton);
        save_map.put(BasPrivRef.ACCIDENT_P0_04, canButton);
        
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
     
        
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(mainDS,createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
	}
	
	private void getConfigList() {
		
		ListGridField ID = new ListGridField("ID","");
		ID.setHidden(true);
		ListGridField SUPLR_ID = new ListGridField("SUPLR_ID","承运商",100);
		//ListGridField INSUR_NO = new ListGridField("INSUR_NO","事故编号",100);
		ListGridField REPORT_NO = new ListGridField("REPORT_NO","报案号",120);
		ListGridField DRIVER = new ListGridField("DRIVER","司机",120);
		ListGridField MOBILE = new ListGridField("MOBILE","联系电话",120);
		//Util.initDate(rangeTable, PURCHASE_DATE);
		ListGridField ORG_ID = new ListGridField("ORG_ID",100);
		ORG_ID.setHidden(true);
		ListGridField STATUS = new ListGridField("STATUS_NAME","状态",100);
		//ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",100);
		ListGridField INSUR_REASON = new ListGridField("INSUR_REASON","出险原因",100);
		ListGridField LOSS_DESCR = new ListGridField("LOSS_DESCR","损失情况",100);
		propTable.setFields(SUPLR_ID,PLATE_NO,DRIVER,MOBILE,REPORT_NO,STATUS,INSUR_REASON,LOSS_DESCR);
		
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME","处理状态",100);
		//STATUS_NAME.setHidden(true);
		ListGridField STATUS1 = new ListGridField("STATUS_CODE","处理状态",100);
		STATUS1.setHidden(true);
		ListGridField ADDTIME = new ListGridField("ADDTIME","处理时间",150);
		//Util.initDate(rangeTable, PURCHASE_DATE);
		ListGridField ADDWHO = new ListGridField("ADDWHO","处理人",100);
		//ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField DESCR = new ListGridField("DESCR","处理情况",100);
		ListGridField LOSS_DESCR1= new ListGridField("LOSS_DESCR_NAME","损失情况",100);
		ListGridField LOSS_AMOUNT = new ListGridField("LOSS_AMOUNT","定损金额",100);
		ListGridField CLAIM_AMOUNT = new ListGridField("CLAIM_AMOUNT","赔偿金额",100);
		ListGridField SETTLE_AMOUNT = new ListGridField("SETTLE_AMOUNT","理赔金额",100);
		
		codeTable.setFields(ORG_ID,STATUS_NAME,ID,STATUS1,ADDTIME,ADDWHO,DESCR,LOSS_DESCR1,LOSS_AMOUNT,CLAIM_AMOUNT,SETTLE_AMOUNT);
	}
	
	//查询窗口
	public DynamicForm createSerchForm(SGPanel form){
		
		form.setDataSource(mainDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		TextItem EXEC_ORG_ID=new TextItem("ORG_ID","执行机构");
		EXEC_ORG_ID.setColSpan(2);
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME=new TextItem("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		//EXEC_ORG_ID_NAME.setWidth();
		EXEC_ORG_ID_NAME.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(FormUtil.Width);
		EXEC_ORG_ID_NAME.setColSpan(2);
		
	
		//2行
		SGText  PLATE_NO = new SGText("PLATE_NO","车牌号",true);
		SGText GOODS_NAME = new SGText("GOODS_NAME", "货物名称",true);
		SGCombo STATUS = new SGCombo("STATUS","处理状态");
        Util.initCodesComboValue(STATUS,"ACCIDENT_STATUS");
		
        SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG","包含下级机构",true);
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
		
		SGText  REPORT_NO = new SGText("REPORT_NO","报案号");
		
		SGDateTime ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		SGDateTime ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		
        form.setItems(EXEC_ORG_ID,PLATE_NO, STATUS,REPORT_NO,GOODS_NAME,ODR_TIME_FROM,ODR_TIME_TO,C_ORG_FLAG,EXEC_ORG_ID_NAME);
        
       
        return form;
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "INSUR_ACCIDENT_LIST");
		///check_map.put("INSUR_TIME", );
		check_map.put("INSUR_TIME", StaticRef.CHK_DATE + "出险时间");
		check_map.put("SUPLR_ID", StaticRef.CHK_NOTNULL + "供应商");
		check_map.put("PLATE_NO", StaticRef.CHK_NOTNULL + "车牌号");
		check_map.put("ORG_ID_NAME", StaticRef.CHK_NOTNULL + "执行机构");
		
		detail_ck_map.put("TABLE", "INSUR_ACCIDENT_LOG");
		
	}
	
	
	@Override
	public void initBtn(int initBtn) {
		if(initBtn == 1){
			enableOrDisables(add_detail_map, true);
		}
		else if(initBtn == 2){
			enableOrDisables(add_detail_map, false);
			enableOrDisables(save_detail_map, true);
			enableOrDisables(del_detail_map, false);
		}
		else if(initBtn ==3){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			enableOrDisables(del_detail_map, true);
		}else if(initBtn ==4){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			enableOrDisables(del_detail_map, false);
		}else if(initBtn ==5){
			enableOrDisables(add_detail_map, true);
			enableOrDisables(save_detail_map, false);
			if(codeTable.getRecords().length > 0){
				enableOrDisables(del_detail_map, true);
			}else{
				enableOrDisables(del_detail_map, false);
			}
			codeTable.OP_FLAG = StaticRef.MOD_FLAG;
		}
	}
	
	private void createMainForm(SGPanel form) {
		
		SGText ID = new SGText("ID","");      
		ID.setVisible(false);
		
		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID","承运商",true);      
		SUPLR_ID.setTitle(ColorUtil.getRedTitle("承运商"));
		Util.initSupplier(SUPLR_ID, "");
		
		final SGText PLATE_NO = new SGText("PLATE_NO","车牌号");	
		PLATE_NO.setTitle(ColorUtil.getRedTitle("车牌号"));
		
		final SGText CAR_OWNER = new SGText("CAR_OWNER","车主",true);
		
		final TextItem STATUS=new TextItem("STATUS","");
		STATUS.setVisible(false);
		
		
		
		TextItem EXEC_ORG_ID=new TextItem("ORG_ID","");
		EXEC_ORG_ID.setVisible(false);
		EXEC_ORG_ID.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		TextItem EXEC_ORG_ID_NAME=new TextItem("ORG_ID_NAME","执行机构");
		//EXEC_ORG_ID_NAME.setWidth();
		EXEC_ORG_ID_NAME.setDefaultValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false,"50%","40%");
		EXEC_ORG_ID_NAME.setTitleOrientation(TitleOrientation.TOP);
		EXEC_ORG_ID_NAME.setWidth(FormUtil.Width);
		EXEC_ORG_ID_NAME.setColSpan(2);
		EXEC_ORG_ID_NAME.setTitle(ColorUtil.getRedTitle("执行机构"));
      
		final SGText DRIVER = new SGText("DRIVER","司机");
        
        final SGText INSUR_STATION = new SGText("INSUR_STATION", "事故地点");	//客户属性
       //ValuesManager vm=new ValuesManager();
       // vm.addMember(form);
        SGText INSUR_TIME = new SGText("INSUR_TIME","出险时间",true);
        Util.initDateTime(form,INSUR_TIME);
        
        SGText MOBILE = new SGText("MOBILE", "联系电话");      
        
        SGText REGION = new SGText("REGION","运输区间");
       
        SGText REPORT_NO = new SGText("REPORT_NO", "报案号");
        
        SGLText GOODS_NAME = new SGLText("GOODS_NAME","货物名称",true);
       
        SGLText INSUR_REASON = new SGLText("INSUR_REASON","出险原因",true);
        
        SGLText LOSS_DESCR = new SGLText("LOSS_DESCR", "损失情况",true);
        
        final  SGLText LOSS_AMOUNT = new SGLText("LOSS_AMOUNT","");
        LOSS_AMOUNT.setVisible(false);
       
       
        final SGLText CLAIM_AMOUNT = new SGLText("CLAIM_AMOUNT","");
		CLAIM_AMOUNT.setVisible(false);
		
      
		final SGLText SETTLE_AMOUNT = new SGLText("SETTLE_AMOUNT", "");
		SETTLE_AMOUNT.setVisible(false);
		
          
        form.setItems(ID,LOSS_AMOUNT,CLAIM_AMOUNT,SETTLE_AMOUNT,STATUS,SUPLR_ID,EXEC_ORG_ID,EXEC_ORG_ID_NAME,PLATE_NO,CAR_OWNER,DRIVER,MOBILE,INSUR_TIME,INSUR_STATION,REGION,GOODS_NAME,REPORT_NO,INSUR_REASON,LOSS_DESCR);
      
        form.setWidth("100%");
       
        
	}
	private VLayout createMainForm1(DynamicForm form) {
		
		SGText ID = new SGText("INSUR_ID","");	
		ID.setVisible(false);
		//form.setHeight("45%");
		
		VLayout layout = new VLayout();
		
		
		final SGCombo STATUS = new SGCombo("STATUS","处理状态",true);
		
		
		Util.initCodesComboValue(STATUS,"ACCIDENT_STATUS");
		

		final SGText DESCR = new SGText("DESCR","处理情况");	
		
		//final SGCheck ACTIVE_FLAG = new SGCheck("SUPLR_ID","激活");
		
		SGCombo LOSS_DESCR = new SGCombo("LOSS_DESCR","损失情况",true);
		
		 Util.initCodesComboValue(LOSS_DESCR,"LOSS_DESCR");
		//Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME"," WHERE ENABLE_FLAG = 'Y'", "");
		
        //final SGLText LOSS_DESCR = new SGLText("LOSS_DESCR","损失情况",true);
        
        final SGText LOSS_AMOUNT = new SGText("LOSS_AMOUNT", "定损金额");	//客户属性
        
        final SGText CLAIM_AMOUNT = new SGText("CLAIM_AMOUNT","赔偿金额",true);
           
        final SGText SETTLE_AMOUNT = new SGText("SETTLE_AMOUNT", "理赔金额");
       
        
       form.setItems(ID,STATUS,DESCR,LOSS_DESCR,LOSS_AMOUNT,CLAIM_AMOUNT,SETTLE_AMOUNT);
        
       form.setWidth("100%");
       form.setHeight("80%");
       
       IButton newSubButton = createUDFBtn("新增",BasPrivRef.ACCIDENT_P0_07);
       newSubButton.addClickHandler(new NewFormAction(form,detail_map));
       newSubButton.addClickHandler(new ClickHandler() {
   		
   		@Override
   		public void onClick(ClickEvent event) {
   			LOSS_AMOUNT.setDefaultValue("0");
   			CLAIM_AMOUNT.setDefaultValue("0");
   			SETTLE_AMOUNT.setDefaultValue("0");
   			enableOrDisables(add_detail_map,false );
   			enableOrDisables(del_detail_map, false);
   			enableOrDisables(save_detail_map, true);
   			STATUS.setDisabled(false);
   			if(codeTable.getRecords().length>0){
   			ListGridRecord rec=codeTable.getRecord(codeTable.getRecords().length-1);
   			//codeTable.getRecords();
   			String status=rec.getAttribute("STATUS_CODE");
   			  if(status.equals("90")){
   				//Util.initCodesComboValue(STATUS,new HashMap());
   				final FormItem combo = STATUS;  
   				combo.setValueMap(new LinkedHashMap<String, String>());
   				//Util.
   			  }else{
   			String where=" where PROP_CODE='ACCIDENT_STATUS' and CODE > '"+status+"' ";
   	        Util.initComboValue(STATUS,"BAS_CODES","ID","NAME_C",where);
   			  }
   			}else{
   				Util.initCodesComboValue(STATUS,"ACCIDENT_STATUS");
   			}
   	      
   		}
   	});
       
       
       IButton savSubButton = createUDFBtn("保存", StaticRef.ICON_SAVE,BasPrivRef.ACCIDENT_P0_08);
       savSubButton.addClickHandler(new AccidentSaveAction(propTable,codeTable,form,detail_ck_map,this));
       
       savSubButton.addClickHandler(new ClickHandler() {
   		
   		@Override
   		public void onClick(ClickEvent event) {
   			enableOrDisables(add_detail_map, true);
   			enableOrDisables(del_detail_map, false);
   			enableOrDisables(save_detail_map, false);
   			//codeTable = new SGTable(detailDS,"100%","45%",false,true,false);
   			
   		}
   	});
       
       
       
       IButton delSubButton = createUDFBtn("删除", StaticRef.ICON_DEL,BasPrivRef.ACCIDENT_P0_09); 
       delSubButton.addClickHandler(new DeleteFormAction(codeTable,form));
       
       IButton canSubButton = createUDFBtn("取消", StaticRef.ICON_CANCEL,BasPrivRef.ACCIDENT_P0_10); 
       canSubButton.addClickHandler(new CancelFormAction(codeTable,form,this));
    
       canSubButton.addClickHandler(new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			enableOrDisables(add_detail_map, true);
			enableOrDisables(del_detail_map, true);
			enableOrDisables(save_detail_map, false);
			 Util.initCodesComboValue(main_form1.getField("STATUS"),"ACCIDENT_STATUS");
		}
	});
       IButton settleSubButton = createUDFBtn("结案",BasPrivRef.ACCIDENT_P0_11); 
       //settleSubButton.addClickHandler(new AccidentSettleAction(propTable,codeTable,form,detail_ck_map,this));
       settleSubButton.addClickHandler(new ClickHandler() {
   		
   		@Override
   		public void onClick(ClickEvent event) {
   			int length=codeTable.getRecords().length;
   			String code="";
   			if(length>0){
   			code=codeTable.getRecord(codeTable.getRecords().length-1).getAttribute("STATUS_CODE");
   			}
   			
   			ArrayList<String> sqlList = new ArrayList<String>();
   			ListGridRecord rec=propTable.getSelectedRecord();
   			if(rec!=null){
   				if(!code.equals("90")){
   			String INSUR_ID=rec.getAttribute("ID");
   			SYS_USER user=LoginCache.getLoginUser();
   			String userID=user.getUSER_ID();
   			String sql1="insert into INSUR_ACCIDENT_LOG(INSUR_ID,ID,STATUS,ADDTIME,ADDWHO) select '"+INSUR_ID+"',sys_guid(),'CE0A98A70CE248FEA8B38C713B903242',sysdate,'"+userID+"' from dual";
            sqlList.add(sql1);
            String sql2="update INSUR_ACCIDENT_LIST set STATUS = '90' where ID = '"+INSUR_ID+"'";
            sqlList.add(sql2);
            Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
				
				@Override
				public void onSuccess(String result) {
					
					if(result.equals(StaticRef.SUCCESS_CODE)){
						MSGUtil.sayInfo("结案操作成功");
						Criteria findValues = new Criteria();
			            findValues.addCriteria("INSUR_ID", propTable.getSelectedRecord().getAttribute("ID"));
			            findValues.addCriteria("OP_FLAG", codeTable.OP_FLAG);
			            codeTable.PKEY = "ID";
			            codeTable.invalidateCache();
			            codeTable.PVALUE = propTable.getSelectedRecord().getAttribute("ID");
			            codeTable.fetchData(findValues);
					}else{
					    MSGUtil.sayError("结案操作失败");
					}
					
					 
					 					
				}
				
				@Override
				public void onFailure(Throwable caught) {
					MSGUtil.sayError(caught.getMessage());
					
				}
			});
   			 }else{
   				MSGUtil.sayError("该记录已结案！");
   			 }
   			} else{
   				MSGUtil.sayError("请选中记录操作！");
            }
   		}
   			
   		
   	});
       
       add_detail_map.put(BasPrivRef.ACCIDENT_P0_07, newSubButton);
       del_detail_map.put(BasPrivRef.ACCIDENT_P0_09, delSubButton);
       save_detail_map.put(BasPrivRef.ACCIDENT_P0_08, savSubButton);
       save_detail_map.put(BasPrivRef.ACCIDENT_P0_10, canSubButton);
       
      // save_detail_map.put(BasPrivRef.MSRMNT_P0_05, newSubButton);
		
        enableOrDisables(add_detail_map, true);
		enableOrDisables(del_detail_map, false);
		enableOrDisables(save_detail_map, false);

        toolStrip2 = new ToolStrip();//按钮布局
		toolStrip2.setAlign(Alignment.RIGHT);
		//toolStrip2.setMargin();
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		toolStrip2.setPadding(2);
		toolStrip2.setSeparatorSize(12);
		toolStrip2.addSeparator();
		toolStrip2.setMembersMargin(4);
        toolStrip2.setMembers(newSubButton,savSubButton,delSubButton,canSubButton,settleSubButton);
        
        layout.addMember(form);
		layout.addMember(toolStrip2);
		
		return layout;
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
		AccidentManagerView view = new AccidentManagerView();
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
