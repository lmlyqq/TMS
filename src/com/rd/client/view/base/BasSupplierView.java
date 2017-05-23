package com.rd.client.view.base;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.action.base.customer.NewCustomerAction;
import com.rd.client.action.base.customer.SaveCustOrgAction;
import com.rd.client.action.base.customer.SaveCustSrvcAction;
import com.rd.client.action.base.supplier.DeleteTransAreaAction;
import com.rd.client.action.base.supplier.SaveSupplierAction;
import com.rd.client.action.base.supplier.SaveTransAreaAction;
import com.rd.client.common.action.BlackAction;
import com.rd.client.common.action.BlackzeAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.CellClickAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.DeleteProAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.GetHintAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
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
import com.rd.client.ds.base.SuppAreaDS;
import com.rd.client.ds.base.SuppOrgDS;
import com.rd.client.ds.base.SuppServDS;
import com.rd.client.ds.base.SupplierManagerDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.AreaWin;
import com.rd.client.win.OrgWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.UploadFileWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
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

/**
 * 基础资料---供应商管理
 * @author lijun
 *
 */
@ClassForNameAble
public class BasSupplierView extends SGForm implements PanelFactory {
    /**
     * @param table
     * @param supDS 供应商管理数据源
     * @param tranDs 运输信息--->运输服务数据源
     * @param depDs 运输信息-->执行机构数据源
     * @param stack 折叠下拉表控件
     * @param vm 用来管理整个form
     * @param basInfo 基础信息存放点 
     * @param contactInfo 联系信息
     * @param accountInfo 结算信息
     * @param controlInfo 运输信息---->控制信息
     * @param orgForm     执行机构分配
     * @param transForm2
     * @param searchItem 
     * @param searchWin
     * @param searchForm
     * @param section
     * @param isMax  判断是否最大化
     */
	private SGTable table;
	private DataSource supDS;
	private DataSource tranDs;
	private DataSource depDs;
	private DataSource areaDs;
	private ValuesManager vm;
	private SGPanel basInfo;
	public Record rec;
	private SGPanel accountInfo;
	private SGPanel controlInfo;
	private SGPanel orgForm;
	private SGPanel servForm;
	private SGTable orgTable;
	private SGTable servTable;
	private SGTable transAreaTable;//运输区域
	//private static ButtonItem searchItem = null;
	private Window searchWin;
	private SectionStack section;
    private SGPanel searchForm = new SGPanel();
    private TextItem supplier_Code;
    private String suplr_id;
    private SGCombo TRANS_SRVC;
    private int pageNum=0;
    private SectionStack list_section;
    public ToolStrip toolStrip1;
    public ToolStrip toolStrip2;
    public ToolStrip toolStrip3;
    public IButton blackzeButton;
    public IButton blackButton;
    private Window uploadWin;
	/*public BasSupplierView(String id) {
		super(id);
	}*/
    
    @Override
	public Canvas getViewPanel() {
    	
    	privObj = LoginCache.getUserPrivilege().get(getFUNCID());
    	vm = new ValuesManager();
    	VLayout main = new VLayout();
    	main.setHeight100();
    	main.setWidth100();
    	
    	//按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		supDS = SupplierManagerDS.getInstance("BAS_SUPPLIER");
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(supDS, "100%", "100%",true,true,false);
		createListFields();
		
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    list_section.addSection(listItem);
//	    listItem.setControls(addMaxBtn(list_section, stack, "200",true), new SGPage(table, true).initPageBtn());
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.setWidth("100%");
//	    searchItem = new ButtonItem(Util.BI18N.SEARCH());
//	    if(searchItem != null) {
//	    	new PageUtil(listItem, table, searchItem, false);
//	    }
		stack.addMember(list_section);
		addSplitBar(stack);
        //STACK的右边布局
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        
      //组织明细的FORM布局
        if(isPrivilege(BasPrivRef.SUPLR_P1)) {
        	
	        Tab tab1 = new Tab(Util.TI18N.MAININFO());
	        tab1.setPane(createMainInfo());
	        leftTabSet.addTab(tab1);
        }
        
    	//运输信息  
        if(isPrivilege(BasPrivRef.SUPLR_P2)) {
	       
	        Tab tab2 = new Tab(Util.TI18N.TRANS_INFO());
	        leftTabSet.addTab(tab2);
	        tab2.setPane(ChildTab());
        }
        
//	    //仓库信息
//        if(isPrivilege(BasPrivRef.SUPLR_P3)) {
//        	
//	        Tab tab3 = new Tab(Util.TI18N.WMS_INFO());
//	        leftTabSet.addTab(tab3);
//        }
//        
//      //合同信息
//        if(isPrivilege(BasPrivRef.SUPLR_P4)) {
//        	
//	        Tab tab4 = new Tab(Util.TI18N.CONTRACT_INFO());
//	        leftTabSet.addTab(tab4);
//        }
        
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
        initVerify();
        //addDoubeclick(table, listItem, leftTabSet, list_section);
        
        vm.addMember(basInfo);
        //vm.addMember(contactInfo);
        vm.addMember(accountInfo);
        vm.addMember(controlInfo);
        
        // 创建按钮布局
		stack.addMember(leftTabSet);
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		
		main.addMember(stack);
		return main;
	}
    
	public void initVerify() {
		check_map.put("TABLE", "BAS_SUPPLIER");
		check_map.put("SUPLR_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.SUP_SUPLR_CODE());
//		check_map.put("HINT_CODE", StaticRef.CHK_UNIQUE + Util.TI18N.HINT_CODE());
		//check_map.put("SUPLR_CNAME",StaticRef.CHK_UNIQUE + Util.TI18N.SUP_SUPLR_CNAME());
		//check_map.put("SHORT_NAME",StaticRef.CHK_UNIQUE + Util.TI18N.SHORT_NAME());
		check_map.put("INS_EFCT_DT", StaticRef.CHK_DATE + Util.TI18N.INS_EFCT_DT());
		check_map.put("INS_EXP_DT", StaticRef.CHK_DATE + Util.TI18N.INS_EXP_DT());
		
		cache_map.put("ENABLE_FLAG", "Y");
		cache_map.put("INTL_FLAG", "Y");
		cache_map.put("CURRENCY", "029E5102CFDD4732A9D34FCE73CB82F8");//人民币
		cache_map.put("MAINT_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		
	}
	
    @Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.SUPLR);
        
        toolStrip.addMember(searchButton);
//        toolStrip.addSeparator();
        
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(supDS,
							createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        
        //新增按钮
        IButton newButton =createBtn(StaticRef.CREATE_BTN,BasPrivRef.SUPLR_P0_01);
        toolStrip.addMember(newButton);
        newButton.addClickHandler(new NewCustomerAction(vm, cache_map,supplier_Code,null,this));
        
        //保存按钮
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SUPLR_P0_02);
        toolStrip.addMember(saveButton);
        saveButton.addClickHandler(new SaveSupplierAction(table, vm, check_map, this));
        
        //删除按钮
        IButton deleteButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.SUPLR_P0_03);
        toolStrip.addMember(deleteButton);
        deleteButton.addClickHandler(new DeleteProAction(table, vm));
        
        //取消按钮
        IButton  cancelButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SUPLR_P0_04);
        toolStrip.addMember(cancelButton);
        cancelButton.addClickHandler(new CancelMultiFormAction(table, vm,this));
        //加入黑名单
        blackzeButton = createBtn("黑名单",BasPrivRef.SUPLR_P0_05);
        blackzeButton.addClickHandler(new BlackzeAction(table, vm, this));
        //取消黑名单
		blackButton = createBtn("取消黑名单",BasPrivRef.SUPLR_P0_06);
		blackButton.addClickHandler(new BlackAction(table, vm, this));
        
		IButton inputButton = createBtn(StaticRef.IMPORT_BTN, BasPrivRef.SUPLR_P0_07);
		inputButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(uploadWin == null){
					
					JavaScriptObject jsobject = list_section.getSection(0).getAttributeAsJavaScriptObject("controls");
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
					uploadWin = new UploadFileWin().getViewPanel("suplr.xls","TMP_SUPLR_IMPORT","SP_IMPORT_SUPLR",table,pageForm);
				}else{
					uploadWin.show();
				}
			}
		});
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.SUPLR_P0_09);
        expButton.addClickHandler(new ExportAction(table));
		
        add_map.put(BasPrivRef.SUPLR_P0_01, newButton);
        del_map.put(BasPrivRef.SUPLR_P0_03, deleteButton);
        save_map.put(BasPrivRef.SUPLR_P0_02, saveButton);
        save_map.put(BasPrivRef.SUPLR_P0_04, cancelButton);
        
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        blackzeButton.disable();
        blackButton.disable();
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, deleteButton,
        		cancelButton, blackzeButton, blackButton,expButton,inputButton);
    }
	@Override
	public void createForm(DynamicForm form) {

		
	}
    /**
     * 点击触发时间：点点击时就会将数据保存到vm中，用于在页面中显示
     */
	private  void createListFields(){
       table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(table.getSelectedRecord()==null) return;
			    Record record =	event.getRecord();
			    vm.editRecord(record);
			    OP_FLAG = "M";
			    suplr_id = event.getRecord().getAttributeAsString("ID");
			    TRANS_SRVC.setDefaultValue(record.getAttributeAsString("TRANS_SRVC_ID"));
			    tabChange(pageNum);
			    
			    initSaveBtn();
			    if(event.getRecord() != null){
			    	if(event.getRecord().getAttribute("BLACKLIST_FLAG").equals("false")){						
						blackButton.setDisabled(true);
						blackzeButton.setDisabled(false);
					}else{				
						blackzeButton.setDisabled(true);
						blackButton.setDisabled(false);
					}
//					rec = event.getRecord();
//					String VEHICLE_STAT = rec.getAttribute("VEHICLE_STAT");
//					if("D5595E8BF25A4E0D8C46796B772FB1BA".equals(VEHICLE_STAT)){
//						freezeButton.enable();
//						freeButton.disable();
//					} else {
//						blackzeButton.enable();
//						blackButton.enable();
			    //  }
//				} else {
//					blackButton.disable();
//					blackButton.disable();
				}
			    
			}
			
			
		});
		table.setShowRowNumbers(true);
		
		/**
		 * 主列表显示的字段
		 * @param SUP_SUPLR_CODE 	供应商代码
		 * @paran SUP_SUPLR_CNAME   供应商名称
		 * @param SUP_AREA_ID_NAME  行政区域
		 * @param SUP_ADDRESS       详细地址
		 * @param SUP_CONT_NAME     联系人
		 * @param SUP_CONT_TEL      联系电话
		 * @param SUP_GRADE         等级
		 * @param SUP_SUPLR_TYP     供应商类表
		 * @param SUP_WHSE_ID       隶属仓库
		 */
		
		ListGridField SUP_SUPLR_CODE = new ListGridField("SUPLR_CODE",Util.TI18N.SUP_SUPLR_CODE(),120);
//		SUP_SUPLR_CODE.setTitle(ColorUtil.getRedTitle(Util.TI18N.SUP_SUPLR_CODE()));
		ListGridField SUP_SUPLR_CNAME= new ListGridField("SUPLR_CNAME",Util.TI18N.SUP_SUPLR_CNAME(),120);
//		SUP_SUPLR_CNAME.setTitle(ColorUtil.getRedTitle(Util.TI18N.SUP_SUPLR_CNAME()));
		ListGridField SUP_AREA_ID_NAME = new ListGridField("AREA_ID_NAME",Util.TI18N.AREA_ID_NAME());
		ListGridField SUP_ADDRESS = new ListGridField("ADDRESS" ,Util.TI18N.ADDRESS());
		ListGridField SUP_CONT_NAME = new ListGridField("CONT_NAME",Util.TI18N.CONT_NAME());
		ListGridField SUP_CONT_TEL = new ListGridField("CONT_TEL",Util.TI18N.CONT_TEL());
		ListGridField SUP_GRADE = new ListGridField("GRADE_NAME",Util.TI18N.GRADE());
		ListGridField SUP_SUPLR_TYP = new ListGridField("SUPLR_TYP_NAME",Util.TI18N.SUP_SUPLR_TYP());
		ListGridField SUP_WHSE_ID  = new ListGridField("WHSE_ID",Util.TI18N.SUP_WHSE_ID());
		
		ListGridField UDF2=new ListGridField("UDF2",Util.TI18N.TAX_RATE()+"(%)",60);
//		UDF2.setHidden(true);
		
		table.setFields(SUP_SUPLR_CODE,SUP_SUPLR_CNAME,UDF2,SUP_AREA_ID_NAME,SUP_ADDRESS,SUP_CONT_NAME,SUP_CONT_TEL,SUP_GRADE,SUP_SUPLR_TYP,SUP_WHSE_ID);
	}
	
	//设置主页面信息
	private SectionStack createMainInfo(){
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setBackgroundColor(ColorUtil.BG_COLOR);
		
		/**
		 * 《----------基础信息-----------》
		 *第一行：三个条目：供应商代码（普通文本），激活（复选框） 
		 *第二行：供应商名称（长文本），协议供应商（复选框），内部供应商（复选框）
		 *第三行：英文名称（长文本）
		 *第四行：简称，助记码（普通文本）
		 *第五行：供应商性质（下拉表），供应商等级（下拉表），供应商类别（下拉表）
		 *第六行：结算方（下拉表），运输业务（复选框），仓库业务（复选框）
		 *第七行：备注（文本框）
		 */
		
		//1
	
		supplier_Code = new SGText("SUPLR_CODE",ColorUtil.getRedTitle(Util.TI18N.SUP_SUPLR_CODE()));
		supplier_Code.setDisabled(true);
	
		
		SGText hint_Code = new SGText("HINT_CODE",Util.TI18N.HINT_CODE(),true);
		
		
		final SGText short_Name = new SGText("SHORT_NAME",ColorUtil.getRedTitle(Util.TI18N.SHORT_NAME()));
		short_Name.addBlurHandler(new GetHintAction(short_Name, hint_Code,supplier_Code));
		
		SGCheck ename = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG","黑名单");
	
		
		
		//4
		
		final SGLText  sup_suplr_cname = new SGLText("SUPLR_CNAME",ColorUtil.getRedTitle(Util.TI18N.SUP_SUPLR_CNAME()),true);
		
		
		
		final SGLText sup_suplr_ename = new SGLText("SUPLR_ENAME",Util.TI18N.SUP_SUPLR_ENAME());
		
		
		

		
		final SGCombo sup_Bill_To = new SGCombo("BILL_TO",Util.TI18N.SUP_BILL_TO());
				
		SGCombo sup_Property= new SGCombo("PROPERTY",Util.TI18N.SUP_PROPERTY(),true);		
		
		SGCombo sup_Grade = new SGCombo("GRADE",Util.TI18N.SUP_GRADE());
		
		SGCombo sup_Suplr_TYP = new SGCombo("SUPLR_TYP",Util.TI18N.SUP_SUPLR_TYP());
			
		Util.initCodesComboValue(sup_Grade,"GRADE");
		Util.initCodesComboValue(sup_Property,"PROPERTY");
		Util.initCodesComboValue(sup_Suplr_TYP,"SUPLR_TYP");
		
		//6
		
		SGCheck sup_Transport_Flag = new SGCheck("TRANSPORT_FLAG",Util.TI18N.SUP_TRANSPORT_FLAG());

		SGCheck sup_WareHouse_Flag = new SGCheck("WAREHOUSE_FLAG",Util.TI18N.SUP_WAREHOUSE_FLAG());
		
		
		Util.initComboValue(sup_Bill_To, "BAS_SUPPLIER", "ID", "SHORT_NAME");
		
		//7
		TextAreaItem notes = new TextAreaItem("NOTES", Util.TI18N.NOTES());
		notes.setStartRow(true);
		notes.setColSpan(4);
		notes.setHeight(34);
		notes.setWidth(FormUtil.longWidth);
		notes.setTitleVAlign(VerticalAlignment.TOP);
		notes.setTitleOrientation(TitleOrientation.TOP);
		
		sup_suplr_cname.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				if(sup_suplr_cname.getValue() != null){
					sup_suplr_ename.setValue(sup_suplr_cname.getValue().toString());
					if(!ObjUtil.isNotNull(short_Name.getValue())){ //为空时执行
						short_Name.setValue(sup_suplr_cname.getValue().toString());
					}
					sup_Bill_To.setValue(sup_suplr_cname.getValue().toString());
					
				}
				
			}
		});
		
		
		basInfo = new SGPanel();
		//basInfo.setTitleWidth(75);
		basInfo.setWidth("40%");
		basInfo.setItems(supplier_Code,short_Name,ename,BLACKLIST_FLAG
				         ,sup_suplr_cname,sup_suplr_ename,hint_Code,sup_Bill_To,sup_Suplr_TYP,sup_Grade
				         ,sup_Property,sup_Transport_Flag,sup_WareHouse_Flag,notes);
		/**
		 * 
		 * 第一行：企业法人（普通文本），行政区域（下拉列表），联系人（普通文本）
		 * 第二行：联系电话（普通文本），注册地址（长文本框）
		 * 第三行：传真（普通文本），电子邮件（长文本框）
		 * 第四行：邮编（普通文本），网址（长文本框）
		 */
		
		
		/**
		 * <------控制信息------>
		 * 第一行：可用运力数量（普通文本），需要车辆预报（复选框）
		 * 第二行：保险（复选框），保险金额（普通文本）
		 * 第三行：保险生效日期（普通文本）--保险到期日期（普通文本）
		 */
		
		SGText  EQMT_NUM = new SGText("EQMT_NUM",Util.TI18N.EQMT_NUM(),true);
		//EQMT_NUM.setColSpan(1);
		SGCheck VEHICLE_FOR_FLAG = new SGCheck("VEHICLE_FOR_FLAG",Util.TI18N.VEHICLE_FOR_FLAG(),true);
		//VEHICLE_FOR_FLAG.setColSpan(1);
		
		SGText INS_AMT = new SGText("INS_AMT",Util.TI18N.INS_AMT());
		//INS_AMT.setColSpan(1);
		SGCheck INS_FLAG = new SGCheck("INS_FLAG",Util.TI18N.INS_FLAG());
		//INS_FLAG.setColSpan(1);
		SGCheck SELECT_VEHICLE=new SGCheck("SELECT_VEHICLE_FLAG",Util.TI18N.SELECT_VEHICLE());
		//SELECT_VEHICLE.setColSpan(1);
		SGDate INS_EFCT_DT = new SGDate("INS_EFCT_DT",Util.TI18N.INS_EFCT_DT());
		//INS_EFCT_DT.setColSpan(1);
		SGDate INS_EXP_DT = new SGDate("INS_EXP_DT","到");
		//INS_EXP_DT.setColSpan(1);
		controlInfo = new SGPanel();
		controlInfo.setWidth("40%");
		controlInfo.setItems(EQMT_NUM,INS_AMT,INS_EFCT_DT,INS_EXP_DT,VEHICLE_FOR_FLAG,INS_FLAG,SELECT_VEHICLE);
		
		/**
		 * <-----结算信息------>
		
		 * ：结算账期（下拉框），结算周期，联系人，联系号码
	
		 * 
		 */
	
		
		//3
		SGText sett_cyc = new SGText("SETT_CYC","结算账期");
		//sett_cyc.setColSpan(1);
		//Util.initCodesComboValue(sett_cyc,"SETTLE_CYC");

		SGText ap_deadline = new SGText("AP_DEADLINE","结算周期");
		//ap_deadline.setColSpan(1);
		
		SGText CONT_NAME = new SGText("CONT_NAME", Util.TI18N.CONT_NAME());
		//CONT_NAME.setColSpan(1);
		SGText CONT_TEL = new SGText("CONT_TEL", Util.TI18N.CONT_TEL());
		//CONT_TEL.setColSpan(2);
		SGText UDF2 = new SGText("UDF2", "税率(%)");
		
		SGText UDF4 = new SGText("UDF4", "开票方");
		
		accountInfo = new SGPanel();
		
		accountInfo.setHeight(120);
		
		accountInfo.setItems(sett_cyc,ap_deadline,CONT_NAME,CONT_TEL,UDF2,UDF4);
		
		accountInfo.setWidth("40%");
		section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		
		SectionStackSection basicInfo = new SectionStackSection(Util.TI18N.BAS_INFO());
		basicInfo.addItem(basInfo);
		basicInfo.setExpanded(true);
		section.addSection(basicInfo);
	   
		SectionStackSection contact_Info = new SectionStackSection(Util.TI18N.CONTRO_INFO());
		contact_Info.addItem(controlInfo);
        contact_Info.setExpanded(true);
      
        
        SectionStackSection account_Info = new SectionStackSection(Util.TI18N.ACCOUNT_INFO());
        account_Info.addItem(accountInfo);
        account_Info.setExpanded(true);
        section.addSection(account_Info);
        
        section.addSection(contact_Info);
        
        section.setVisibilityMode(VisibilityMode.MULTIPLE);
        section.setAnimateSections(true);
		
		
		return section;
		
	}
	
	private TabSet ChildTab(){
		TabSet childTab = new TabSet();
		childTab.setTabBarPosition(Side.LEFT);
		
		
		//执行机构分配
		if(isPrivilege(BasPrivRef.SUPLR_P2_P2)) {
			
			Tab tab2 = new Tab(Util.TI18N.ACTUATOR_ASSIGN());
			childTab.addTab(tab2);
			tab2.setPane(childLayout1());
		}
		
		if(isPrivilege(BasPrivRef.SUPLR_P2_P3)) {
			
			//运输服务
			Tab tab3 = new Tab(Util.TI18N.TRANSPORT_SERVICE());
			childTab.addTab(tab3);
			tab3.setPane(childLayout2());
		}
		
		if(isPrivilege(BasPrivRef.SUPLR_P2_P4)) {
			
			Tab tab4 = new Tab("运输区域");
			childTab.addTab(tab4);
			tab4.setPane(createTransArea());
		}
		
		//页签切换事件
		childTab.addTabSelectedHandler(new TabSelectedHandler() {
			
			@Override
			public void onTabSelected(TabSelectedEvent event) {
				int num = event.getTabNum();//页签num
				pageNum = num;
				tabChange(num);
			}
		});
		
		
		return childTab;
	}
	
	private void tabChange(int num){
		if(ObjUtil.isNotNull(suplr_id)){
			Criteria criteria = new Criteria();
            criteria.addCriteria("OP_FLAG",StaticRef.MOD_FLAG);
            criteria.addCriteria("SUPLR_ID", suplr_id);
			if(num == 0){
                orgTable.fetchData(criteria);
			}else if(num == 1){
				servTable.discardAllEdits();
				servTable.fetchData(criteria);
			}else if (num == 2){
				transAreaTable.fetchData(criteria);
			}
		}
	}
	
	//控制信息内容
//	private VLayout ChildControlInfo(){
//		VLayout layout = new VLayout();
//		layout.setWidth100();
//		layout.setBackgroundColor(ColorUtil.BG_COLOR);
//		/**
//		 * 第一行：可用运力数量（普通文本），需要车辆预报（复选框）
//		 * 第二行：保险（复选框），保险金额（普通文本）
//		 * 第三行：保险生效日期（普通文本）--保险到期日期（普通文本）
//		 */
//		//1
//		SGText  EQMT_NUM = new SGText("EQMT_NUM",Util.TI18N.EQMT_NUM(),true);
//		SGCheck VEHICLE_FOR_FLAG = new SGCheck("VEHICLE_FOR_FLAG",Util.TI18N.VEHICLE_FOR_FLAG());
//		
//		//2
//		SGText INS_AMT = new SGText("INS_AMT",Util.TI18N.INS_AMT(),true);
//		SGCheck INS_FLAG = new SGCheck("INS_FLAG",Util.TI18N.INS_FLAG());
//		SGCheck SELECT_VEHICLE=new SGCheck("SELECT_VEHICLE_FLAG",Util.TI18N.SELECT_VEHICLE());
//		//3
//		SGDate INS_EFCT_DT = new SGDate("INS_EFCT_DT",Util.TI18N.INS_EFCT_DT(),true);
//		SGDate INS_EXP_DT = new SGDate("INS_EXP_DT","到");
//		
//		//controlInfo = new SGPanel();
//		//controlInfo.setTitleWidth(80);
//		//controlInfo.setItems(EQMT_NUM,VEHICLE_FOR_FLAG,INS_AMT,INS_FLAG,SELECT_VEHICLE,INS_EFCT_DT,INS_EXP_DT);
//		
//		//layout.addMember(controlInfo);
//		
//		return layout;
//	}
	
	//执行机构分配
	private VLayout childLayout1(){
		
		depDs = SuppOrgDS.getInstance("BAS_SUPPLIER_ORG");
		
		orgForm = new SGPanel();
		final TextItem ORG_ID = new TextItem("ORG_ID");
		ORG_ID.setVisible(false);
		final TextItem ORG_ID_NAME = new TextItem("ORG_NAME", Util.TI18N.C_ORG_ID());
		ORG_ID_NAME.setTitleOrientation(TitleOrientation.LEFT);
		ORG_ID_NAME.setColSpan(1);
		
		CheckboxItem C_ORG_FLAG = new CheckboxItem("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());
		C_ORG_FLAG.setColSpan(1);
		CheckboxItem d_flag = new CheckboxItem("DEFAULT_FLAG", Util.TI18N.DEFAULT_FLAG());
		ORG_ID_NAME.setColSpan(1);
		ORG_ID_NAME.setWidth(160);
        PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new OrgWin(ORG_ID_NAME,ORG_ID,false,"70%","40%").getViewPanel();
			}
		});
        ORG_ID_NAME.setIcons(searchPicker);
		
		orgForm.setDataSource(depDs);
		orgForm.setItems(ORG_ID_NAME,C_ORG_FLAG,d_flag,ORG_ID);
		
		orgTable = childListGrid1();
		
		orgTable.addRecordClickHandler(new CellClickAction(orgForm, OP_FLAG));
		
		
		
		VLayout layout = new VLayout();
//		createChildBtnWidget(tool2,orgTable);
		
		IButton newB2 = createBtn(StaticRef.CREATE_BTN,BasPrivRef.SUPLR_P2_P2_01);
		newB2.setIcon(StaticRef.ICON_NEW);
		newB2.setWidth(60);
//		saveBtn.setEndRow(false);
		newB2.setAutoFit(true);
		newB2.setAlign(Alignment.RIGHT);
		newB2.addClickHandler(new NewFormAction(orgForm,null));
		
		IButton savB2 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SUPLR_P2_P2_02);
		savB2.setIcon(StaticRef.ICON_SAVE);
		savB2.setWidth(60);
//		saveBtn.setEndRow(false);
		savB2.setAutoFit(true);
		savB2.setAlign(Alignment.RIGHT);
		savB2.addClickHandler(new SaveCustOrgAction(orgTable, orgForm, table,false));
		
		IButton delB2 = createBtn(StaticRef.DELETE_BTN,BasPrivRef.SUPLR_P2_P2_03);
		delB2.setIcon(StaticRef.ICON_DEL);
		delB2.setWidth(60);
//		saveBtn.setEndRow(false);
		delB2.setAutoFit(true);
		delB2.setAlign(Alignment.RIGHT);
		delB2.addClickHandler(new DeleteFormAction(orgTable, orgForm));
		
		IButton canB2 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SUPLR_P2_P2_04);
		canB2.setIcon(StaticRef.ICON_CANCEL);
		canB2.setWidth(60);
//		saveBtn.setEndRow(false);
		canB2.setAutoFit(true);
		canB2.setAlign(Alignment.RIGHT);
		canB2.addClickHandler(new CancelFormAction(orgTable, orgForm));
		
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
//		toolStrip.setItems(newB2,savB2,deleteButton,cancelButton);
        toolStrip2 = new ToolStrip();//按钮布局
		toolStrip2.setAlign(Alignment.RIGHT);
		toolStrip2.setWidth("100%");
		toolStrip2.setHeight("20");
		toolStrip2.setPadding(2);
		toolStrip2.setSeparatorSize(12);
		toolStrip2.addSeparator();
		toolStrip2.setMembersMargin(4);
        toolStrip2.setMembers(newB2,savB2,delB2,canB2);
        
		
		layout.addMember(orgTable);
		layout.addMember(orgForm);
		layout.addMember(toolStrip2);
		
		return layout;
	}
	
	//运输服务
	private VLayout childLayout2(){
		
		tranDs = SuppServDS.getInstance("BAS_SUPLR_TRANS_SRVC");
		ToolStrip tool = new ToolStrip();
		tool.setAlign(Alignment.LEFT);
		
		
		servForm = new SGPanel();
//		final SGCombo SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.SERVICE_NAME());
//		SGCheck d_flag = new SGCheck("DEFAULT_FLAG", Util.TI18N.DEFAULT_FLAG());
		
		
//		Util.initTrsService(SRVC_ID, "", true);
		
		
//		SGPanel tool2 = new SGPanel();
		servTable = childListGrid2();
		
		servTable.addRecordClickHandler(new CellClickAction(servForm, OP_FLAG));
		
//		//保存按钮
//        SGButtonItem saveButton = new SGButtonItem(StaticRef.SAVE_BTN,false);
//        saveButton.addClickHandler(new SaveCustSrvcAction(servTable, servForm, table, false));
//        
//        //删除按钮
//        SGButtonItem deleteButton = new SGButtonItem(StaticRef.CANCEL_BTN,false);
////        deleteButton.addClickHandler(new DeleteFormAction(servTable, servForm));
//        deleteButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
//			
//			@Override
//			public void onClick(
//					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				servTable.discardAllEdits();
//				
//			}
//		});
//		servForm.setItems(saveButton,deleteButton);
		
		IButton savB3 = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SUPLR_P2_P3_01);
		savB3.setIcon(StaticRef.ICON_SAVE);
		savB3.setWidth(60);
		savB3.setAutoFit(true);
		savB3.setAlign(Alignment.RIGHT);
		savB3.addClickHandler(new SaveCustSrvcAction(servTable, servForm, table, false));
		
		IButton canB3 = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.SUPLR_P2_P3_02);
		canB3.setIcon(StaticRef.ICON_CANCEL);
		canB3.setWidth(60);
		canB3.setAutoFit(true);
		canB3.setAlign(Alignment.RIGHT);
		
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
        
		VLayout layout = new VLayout();
//		createChildBtnWidget2(tool2,servTable);
		layout.addMember(servTable);
//		layout.addMember(servForm);
		layout.addMember(toolStrip3);
//		layout.addMember(tool2);
		return layout;
		
		
	}
	//执行机构分配----元素布局
	private SGTable childListGrid1(){
		
		SGTable table = new SGTable(depDs);
		table.setShowFilterEditor(false);
		table.setShowRowNumbers(true);
		table.setHeight("90%");
		table.setSelectionType(SelectionStyle.SIMPLE);
		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		
		ListGridField ORG_ID_NAME = new ListGridField("ORG_NAME",Util.TI18N.ORG_ID_NAME(),140);
		
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),60);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(ORG_ID_NAME,DEFAULT_FLAG);
		
		return table;
		
	}
	
	//运输服务布局---元素布局
	private SGTable childListGrid2(){
		
		
		final SGTable table = new SGTable(tranDs);
		table.setShowRowNumbers(true);
		table.setHeight("90%");
		table.setShowFilterEditor(false);
//		table.setSelectionType(SelectionStyle.SIMPLE);
//		table.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		table.setEditEvent(ListGridEditEvent.CLICK);
		ListGridField USE_FLAG = new ListGridField("USE_FLAG","选择",60);
		USE_FLAG.setCanEdit(true);
		USE_FLAG.setType(ListGridFieldType.BOOLEAN);
		ListGridField SERVICE_NAME = new ListGridField("SERVICE_NAME",Util.TI18N.SERVICE_NAME(),140);
		ListGridField DEFAULT_FLAG = new ListGridField("DEFAULT_FLAG",Util.TI18N.DEFAULT_FLAG(),60);
		DEFAULT_FLAG.setCanEdit(true);
		DEFAULT_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		table.setFields(USE_FLAG,SERVICE_NAME,DEFAULT_FLAG);
		
		DEFAULT_FLAG.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				
				if((!table.getRecord(event.getRowNum()).getAttributeAsBoolean("USE_FLAG") && "N".equals(Util.getFlag(table.getEditValue(event.getRowNum(), "USE_FLAG")))) 
						&& Boolean.parseBoolean(event.getNewValue().toString())){
					MSGUtil.sayError(Util.MI18N.MODIFY_DEFAULT_EORRO());
					table.setEditValue(event.getRowNum(), "DEFAULT_FLAG", false);
				}
				
			}
		});
		
		USE_FLAG.addEditorExitHandler(new EditorExitHandler() {
			
			@Override
			public void onEditorExit(EditorExitEvent event) {
				if(!Boolean.parseBoolean(event.getNewValue().toString())){
					table.setEditValue(event.getRowNum(), "DEFAULT_FLAG", false);
				}
				
			}
		});
		
		return table;
	}
	
	private VLayout createTransArea(){
		VLayout lay = new VLayout();
		areaDs = SuppAreaDS.getInstance("V_SUPLR_AREA");
		transAreaTable = new SGTable(areaDs);
		transAreaTable.setShowRowNumbers(true);
		transAreaTable.setSelectionType(SelectionStyle.SIMPLE);  
		transAreaTable.setShowFilterEditor(false);
//		transAreaTable.setSelectionAppearance(SelectionAppearance.CHECKBOX); 
		transAreaTable.setHeight("90%");
		ListGridField AREA_ID_NAME = new ListGridField("UNLOAD_AREA_NAME",Util.TI18N.AREA_ID_NAME(),120);
		ListGridField PROVICE_NAME = new ListGridField("PROVICE_NAME","省份",120);
		ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),120);
		transAreaTable.setFields(AREA_ID_NAME,PROVICE_NAME,TRANS_SRVC_ID_NAME);
		
		final SGPanel transAreaForm = new SGPanel();
		transAreaForm.setTitleWidth(80);
		SGText AREA_WIN = new SGText("AREA_WIN", Util.TI18N.AREA_ID_NAME());
		AREA_WIN.setColSpan(1);
		AREA_WIN.setTitleOrientation(TitleOrientation.LEFT);
		
		final SGCombo TRANS_SRVC = new SGCombo("TRANS_SRVC", Util.TI18N.TRANS_SRVC_ID());
		TRANS_SRVC.setTitleOrientation(TitleOrientation.LEFT);
		
		Util.initTrsService(TRANS_SRVC, "");
		this.TRANS_SRVC = TRANS_SRVC;
//		SGButtonItem saveBtn = new SGButtonItem(StaticRef.SAVE_BTN);
//		SGButtonItem deleteBtn = new SGButtonItem(StaticRef.DELETE_BTN);
		
		IButton saveBtn = createBtn(StaticRef.SAVE_BTN,BasPrivRef.SUPLR_P2_P4_01);
		saveBtn.setIcon(StaticRef.ICON_SAVE);
		saveBtn.setWidth(60);
		saveBtn.setAutoFit(true);
		saveBtn.setAlign(Alignment.RIGHT);
		
		IButton deleteBtn = createBtn(StaticRef.DELETE_BTN,BasPrivRef.SUPLR_P2_P4_02);
		deleteBtn.setIcon(StaticRef.ICON_DEL);
		deleteBtn.setWidth(60);
		deleteBtn.setAutoFit(true);
		deleteBtn.setAlign(Alignment.RIGHT);

		toolStrip1 = new ToolStrip();//按钮布局
		toolStrip1.setAlign(Alignment.RIGHT);
		toolStrip1.setWidth("100%");
		toolStrip1.setHeight("20");
		toolStrip1.setPadding(2);
		toolStrip1.setSeparatorSize(12);
		toolStrip1.addSeparator();
		toolStrip1.setMembersMargin(4);
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
		
        toolStrip1.setMembers(saveBtn,deleteBtn);
        
		
//		transAreaForm.setItems(AREA_WIN,TRANS_SRVC,saveBtn,deleteBtn);
        transAreaForm.setItems(TRANS_SRVC,AREA_WIN);
        
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
        searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new AreaWin(transAreaTable,"24%","30%",TRANS_SRVC).getViewPanel();
			}
		});
        AREA_WIN.setIcons(searchPicker);
        
        saveBtn.addClickHandler(new SaveTransAreaAction(table, transAreaTable));
        deleteBtn.addClickHandler(new DeleteTransAreaAction(transAreaTable));
		
		lay.addMember(transAreaTable);
		lay.addMember(transAreaForm);
		lay.addMember(toolStrip1);
		return lay;
	}
	
	
	public void createChildBtnWidget(ToolStrip toolStrip,SGTable orgTable){
    	
//		toolStrip.setNumCols(24);
//    	
//    	//新增按钮
//        SGButtonItem newButton =new SGButtonItem(StaticRef.CREATE_BTN,true);
//        newButton.setColSpan(2);
//        newButton.addClickHandler(new NewFormAction(orgForm,null));
//        
//        //保存按钮
//        SGButtonItem saveButton = new SGButtonItem(StaticRef.SAVE_BTN,false);
//        saveButton.setColSpan(2);
//        saveButton.addClickHandler(new SaveCustOrgAction(orgTable, orgForm, table,false));
//        
//        //删除按钮
//        SGButtonItem deleteButton = new SGButtonItem(StaticRef.DELETE_BTN,false);
//        deleteButton.setColSpan(2);
//        deleteButton.addClickHandler(new DeleteFormAction(orgTable, orgForm));
//        
//        //取消按钮
//        SGButtonItem  cancelButton = new SGButtonItem(StaticRef.CANCEL_BTN,false);
//        cancelButton.setColSpan(2);
//        cancelButton.addClickHandler(new CancelFormAction(orgTable, orgForm));
//        
//        toolStrip.setItems(newButton,saveButton,deleteButton,cancelButton);
        
		IButton newB2 = new IButton(Util.BI18N.NEW());
		newB2.setIcon(StaticRef.ICON_NEW);
		newB2.setWidth(60);
//		saveBtn.setEndRow(false);
		newB2.setAutoFit(true);
		newB2.setAlign(Alignment.RIGHT);
		newB2.addClickHandler(new NewFormAction(orgForm,null));
		
		IButton savB2 = new IButton(Util.BI18N.SAVE());
		savB2.setIcon(StaticRef.ICON_SAVE);
		savB2.setWidth(60);
//		saveBtn.setEndRow(false);
		savB2.setAutoFit(true);
		savB2.setAlign(Alignment.RIGHT);
		savB2.addClickHandler(new SaveCustOrgAction(orgTable, orgForm, table,false));
		
		IButton delB2 = new IButton(Util.BI18N.DELETE());
		delB2.setIcon(StaticRef.ICON_DEL);
		delB2.setWidth(60);
//		saveBtn.setEndRow(false);
		delB2.setAutoFit(true);
		delB2.setAlign(Alignment.RIGHT);
		delB2.addClickHandler(new DeleteFormAction(orgTable, orgForm));
		
		IButton canB2 = new IButton(Util.BI18N.CANCEL());
		canB2.setIcon(StaticRef.ICON_CANCEL);
		canB2.setWidth(60);
//		saveBtn.setEndRow(false);
		canB2.setAutoFit(true);
		canB2.setAlign(Alignment.RIGHT);
		canB2.addClickHandler(new CancelFormAction(orgTable, orgForm));
		
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
//		toolStrip.setItems(newB2,savB2,deleteButton,cancelButton);
        toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		toolStrip.setMembersMargin(4);
		
//		toolStrip.setBackgroundColor(ColorUtil.TITLE_COLOR);
		
        toolStrip.setMembers(newB2,savB2,delB2,canB2);
    }
	/**
	private void createChildBtnWidget2(SGPanel toolStrip,SGTable servTable){
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        
        //新增按钮
        SGButton newButton =new SGButton(StaticRef.CREATE_BTN,true);
        newButton.addClickHandler(new NewFormAction(servForm,null));
        
        //保存按钮
        SGButton saveButton = new SGButton(StaticRef.SAVE_BTN,false);
        saveButton.addClickHandler(new SaveCustSrvcAction(servTable, servForm, table, false));
        
        //删除按钮
        SGButton deleteButton = new SGButton(StaticRef.DELETE_BTN,false);
        deleteButton.addClickHandler(new DeleteFormAction(servTable, servForm));
        
        //取消按钮
        SGButton  cancelButton = new SGButton(StaticRef.CANCEL_BTN,false);
        cancelButton.addClickHandler(new CancelFormAction(servTable, servForm));
        toolStrip.setItems(newButton,saveButton,deleteButton,cancelButton);
	}
	**/
	//查询的二级窗口视图布局
	public DynamicForm createSerchForm(DynamicForm form){
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(352);
		txt_global.setColSpan(5);
		//txt_global.setEndRow(true);

		
		
		SGCombo SUPLR_TYP = new SGCombo("SUPLR_TYP",Util.TI18N.SUP_SUPLR_TYP(),true);
		Util.initCodesComboValue(SUPLR_TYP, "SUPLR_TYP");
		
		SGText MAINT_ORG_ID_NAME = new SGText("MAINT_ORG_ID_NAME",Util.TI18N.MAINT_ORG_ID());
		TextItem MAINT_ORG_ID = new TextItem("MAINT_ORG_ID");
		MAINT_ORG_ID.setVisible(false);
		
		Util.initOrg(MAINT_ORG_ID_NAME, MAINT_ORG_ID, false, "30%", "38%");
		
		MAINT_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		MAINT_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		
//		SGCheck TRANSPORT_FLAG = new SGCheck("OR_TRANSPORT_FLAG",Util.TI18N.TRANSPORT_FLAG(),true);
//		TRANSPORT_FLAG.setValue(true);
//        SGCheck WAREHOUSE_FLAG = new SGCheck("OR_WAREHOUSE_FLAG",Util.TI18N.C_WAREHOUSE_FLAG());
//        WAREHOUSE_FLAG.setValue(true);
        
        SGCheck INTL_FLAG_FLAG = new SGCheck("INTL_FLAG",Util.TI18N.INTL_FLAG()); 
        INTL_FLAG_FLAG.setValue(true);
        SGCheck ENABLE_FLAG = new SGCheck("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG());     
        ENABLE_FLAG.setValue(true);
		
        SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG(),true);
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
        
		SGCheck BLACKLIST_FLAG = new SGCheck("BLACKLIST_FLAG","黑名单"); 
		BLACKLIST_FLAG.setValue(false);
		
        form.setItems(txt_global,ENABLE_FLAG,SUPLR_TYP,MAINT_ORG_ID_NAME,
        		MAINT_ORG_ID,C_ORG_FLAG,BLACKLIST_FLAG);
		return form;
	}
	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		//	searchItem = null;
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasSupplierView view = new BasSupplierView();
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
