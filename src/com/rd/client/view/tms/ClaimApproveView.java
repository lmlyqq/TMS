package com.rd.client.view.tms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ClaimApproveDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
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
 * 运输管理->货损赔偿单
 * @author lml
 *
 */
@ClassForNameAble
public class ClaimApproveView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table;
	 private DynamicForm main_form;             //主信息页签布局
	 private SectionStack section;
	 private Window searchWin = null;
	 private Window checkWin = null;
	 private DynamicForm searchForm;
	 private DynamicForm checkForm;
	 private TabSet leftTabSet;
	 private Canvas canvas;
	 public  Canvas tileCanvas;
	 private int m_pageNum=0;//全局页签数
	 
	 /*public ClaimApproveView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.LEFT);
	    ds = ClaimApproveDS.getInstance("TRANS_CLAIM_APPROVE");
		
	    //主布局
		HStack stack = new HStack();
		stack.setWidth("62%");
		stack.setHeight100();
	
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		getConfigList();
		section = new SectionStack();
		SectionStackSection listItem = new SectionStackSection("列表信息");
	    listItem.setItems(table);
	    listItem.setExpanded(true);
		listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
	    section.setWidth("99%");
		stack.addMember(section);
	
		//右表
		leftTabSet = new TabSet();  
        leftTabSet.setWidth("60%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        
        if(isPrivilege(TrsPrivRef.ClaimApprove_P1)) {
	        Tab tab1 = new Tab(Util.TI18N.MAININFO());
			//主信息
	        main_form = new SGPanel();
		    main_form.setWidth("50%");
	        VLayout v_lay = createMainForm(main_form);
	        v_lay.setWidth("40%");
	        v_lay.setHeight("80%");
			tab1.setPane(v_lay);
	        leftTabSet.addTab(tab1);
        }
        if(isPrivilege(TrsPrivRef.ClaimApprove_P2)){
        	//图片信息
        	Tab tab2 = new Tab(Util.TI18N.IMG_INFO());
        	tab2.setPane(createImgInfo());
        	leftTabSet.addTab(tab2);
        }
        stack.addMember(leftTabSet);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		
		main.addMember(stack);
		
		table.addDoubleClickHandler(new DoubleClickHandler(){

		 @Override
		  public void onDoubleClick(DoubleClickEvent event) {
					
             enableOrDisables(save_map, true);
			        
				
				}
				
			});
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	main_form.editRecord(selectedRecord);
            	main_form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                initSaveBtn();
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
				if(m_pageNum == 1){
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
		
		table.addDoubleClickHandler(new DoubleClickHandler(){

		 @Override
		  public void onDoubleClick(DoubleClickEvent event) {
					
             enableOrDisables(save_map, true);
			        
				
				}
				
			});
		
		
		initVerify();
        return main;
        
        
        
	}

	
	 
	 
	 
	 
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	
	
    
	 
	private void getConfigList() {
		
        ListGridField CUSTOMER_ID = new ListGridField("CUSTOMER_ID", "客户", 120);
        ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属周期", 120);
        ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单", 100);
       // ACTIVE_FLAG.setType(ListGridFieldType.BOOLEAN);
        ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 120);
        ListGridField DRIVER = new ListGridField("DRIVER", "司机", 120);
        ListGridField STATUS = new ListGridField("STATUS", "审核状态", 120);
        ListGridField APPROVE_TIME = new ListGridField("APPROVE_TIME", "审核时间", 120);
//        ListGridField CONT_TEL = new ListGridField("CONT_TEL", "联系电话", 80);
//        ListGridField OIL_AMOUNT = new ListGridField("OIL_AMOUNT", Util.TI18N.OIL_AMOUNT(), 80);
//        ListGridField OIL_ADDRESS = new ListGridField("OIL_ADDRESS", Util.TI18N.OIL_ADDRESS(), 80);
//        ListGridField NOTES = new ListGridField("NOTES", Util.TI18N.NOTES(), 80);
//        ListGridField qty5Field = new ListGridField("QTY5", Util.TI18N.PACK_QTY(), 80);
        table.setFields(CUSTOMER_ID, BELONG_MONTH,LOAD_NO, PLATE_NO, DRIVER, STATUS, APPROVE_TIME);
	}
	
	

	
	
	

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,TrsPrivRef.ClaimApprove);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new DynamicForm();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
       // IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.ACCOUNT_P0_01);
       // newButton.addClickHandler(new NewFormAction(main_form,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,TrsPrivRef.ClaimApprove_P0_03);
        saveButton.addClickHandler(new SaveFormAction(table, main_form, check_map, this));
        
       // IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.ACCOUNT_P0_03);
       //  delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,TrsPrivRef.ClaimApprove_P0_04);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
        
        IButton checkButton = createBtn("审核",TrsPrivRef.ClaimApprove_P0_05);
        checkButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin != null) {
					searchWin.hide();
				}
				
				if(checkWin == null) {
					checkForm = new DynamicForm();
					checkWin=getCheckWin();
					checkWin.show();
				}
				else {
					checkWin.show();
				}
			}
        	
        });
        //expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
    
      //  add_map.put(BasPrivRef.ACCOUNT_P0_01, newButton);
      //  del_map.put(BasPrivRef.ACCOUNT_P0_03, delButton);
        save_map.put(TrsPrivRef.ClaimApprove_P0_03, saveButton);
        save_map.put(TrsPrivRef.ClaimApprove_P0_04, canButton);
      //  this.enableOrDisables(add_map, true);
      //  enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton,saveButton,canButton,checkButton);
	}
	
	public void initVerify() {
		
		//check_map.put("TABLE", "BAS_RECE_ACCOUNT");
		//check_map.put("ACCOUNT", StaticRef.CHK_UNIQUE + "收款账号");	
		//check_map.put("RECEIVER", StaticRef.CHK_NOTNULL + "收款人");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		TextItem CUSTOMER_ID = new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		SGText CUSTOMER_NAME = new SGText("CUSTOMER_NAME","客户名称");
		Util.initOrg(CUSTOMER_NAME, CUSTOMER_ID, false, "30%", "45%");
		CUSTOMER_NAME.setTitle(ColorUtil.getRedTitle("客户名称"));		
		
		
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		
		SGText LOAD_NO = new SGText("LOAD_NO","调度单号",true);
		
		SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
		
		//SGDateTime ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
		//SGDateTime ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		//ODR_TIME_FROM.setColSpan(1);
		//String PreYesDate=Util.getYesPreDay();
		//ODR_TIME_FROM.setDefaultValue(PreYesDate);
		//ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		//txt_global.setWidth(300);
		//txt_global.setColSpan(5);
		//txt_global.setEndRow(true);
         form.setItems(CUSTOMER_NAME,CUSTOMER_ID,BELONG_MONTH,LOAD_NO,PLATE_NO);
        
        return form;
	}
	
	private VLayout createMainForm(DynamicForm form) {
		SGText CUSTOMER_ID = new SGText("CUSTOMER_ID",ColorUtil.getRedTitle("客户名称"),true);      
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		
		SGText CUSTOMER_NAME = new SGText("CUSTOMER_NAME","客户名称");
		Util.initOrg(CUSTOMER_NAME, CUSTOMER_ID, false, "30%", "45%");
		CUSTOMER_NAME.setTitle(ColorUtil.getRedTitle("客户名称"));	
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		
		SGText CLAIM_AMOUNT = new SGText("CLAIM_AMOUNT","赔偿总金额");
		
		final SGText LOAD_NO = new SGText("LOAD_NO","调度单号",true);

        final SGText PLATE_NO = new SGText("PLATE_NO","车牌号");

        final SGText DRIVER = new SGText("DRIVER", "司机");	

        final SGText MOBILE = new SGText("MOBILE","联系电话",true);
  
        SGText GOODS = new SGText("GOODS", "货品名称");
        
        SGText QTY = new SGText("QTY", "数量");
        
        SGCombo DUTY_TO = new SGCombo("DUTY_TO", "责任方",true);
        
        SGText DUTYER = new SGText("DUTYER", "责任人");
        
        TextAreaItem notes = new TextAreaItem("NOTES", "货损货差情况说明");
    	notes.setStartRow(true);
    	notes.setColSpan(6);
    	notes.setHeight(50);
    	notes.setWidth(FormUtil.longWidth+FormUtil.Width);
    	notes.setTitleOrientation(TitleOrientation.TOP);
    	notes.setTitleVAlign(VerticalAlignment.TOP);
        
    	TextAreaItem DUTY_INSURER = new TextAreaItem("DUTY_INSURER", "保险公司");
    	DUTY_INSURER.setColSpan(2);
    	DUTY_INSURER.setWidth(FormUtil.Width);
    	DUTY_INSURER.setHeight(180);
    	DUTY_INSURER.setTitleOrientation(TitleOrientation.TOP);
    	DUTY_INSURER.setTitleVAlign(VerticalAlignment.TOP);
       // DUTY_INSURER.setHeight(100);
       // DUTY_INSURER.setStartRow(false);
        TextAreaItem DUTY_CARRIER = new TextAreaItem("DUTY_CARRIER", "承运商/货车司机");
        DUTY_CARRIER.setColSpan(2);
        DUTY_CARRIER.setWidth(FormUtil.Width);
        DUTY_CARRIER.setHeight(180);
    	DUTY_CARRIER.setTitleOrientation(TitleOrientation.TOP);
        DUTY_CARRIER.setTitleVAlign(VerticalAlignment.TOP);
       // DUTY_CARRIER.setHeight(100);
       // DUTY_CARRIER.setStartRow(false);
        TextAreaItem DUTY_STAFF = new TextAreaItem("DUTY_STAFF", "本公司责任员工");
        DUTY_STAFF.setColSpan(2);
        DUTY_STAFF.setWidth(FormUtil.Width);
        DUTY_STAFF.setHeight(180);
        DUTY_STAFF.setTitleOrientation(TitleOrientation.TOP);
        DUTY_STAFF.setTitleVAlign(VerticalAlignment.TOP);
       // DUTY_STAFF.setHeight(100);
       // DUTY_STAFF.setStartRow(false);
        
        SGLText DUTY_COMPANY = new SGLText("DUTY_COMPANY", "本公司承担金额");
        DUTY_COMPANY.setWidth(FormUtil.longWidth+FormUtil.Width);
        DUTY_COMPANY.setHeight(50);
        
        DynamicForm inForm =new DynamicForm();
        
        inForm.setItems(DUTY_INSURER,DUTY_CARRIER,DUTY_STAFF);
        inForm.setGroupTitle("责任人/保险公司承担金额");
        inForm.setIsGroup(true);
        inForm.setNumCols(12);
        inForm.setMargin(2);
        inForm.setPadding(2);
        inForm.setWidth("75%");
        inForm.setHeight("45%");
        // inForm.setCanHover(true);
        // inForm.setShowHover(true);
        inForm.setTitleSuffix("");
        
        form = new SGPanel();
        form.setWidth("40%");
        form.setItems(CUSTOMER_NAME,CUSTOMER_ID,BELONG_MONTH,CLAIM_AMOUNT,LOAD_NO,PLATE_NO,DRIVER,MOBILE,GOODS,QTY,DUTY_TO,DUTYER,notes);
        form.setHeight("30%"); 
       
       LOAD_NO.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
   				
   				Util.db_async.getRecord("PLATE_NO,DRIVER1,MOBILE1"," trans_load_header"," where LOAD_NO='"+LOAD_NO.getValue()+"' ", null, new AsyncCallback<ArrayList<HashMap<String, String>>>(){

					@Override
					public void onFailure(Throwable caught) {
						
					}

					@Override
					public void onSuccess(
							ArrayList<HashMap<String, String>> result) {
						if(result!=null&&result.size()>0){
						PLATE_NO.setValue(result.get(0).get("PLATE_NO"));
						DRIVER.setValue(result.get(0).get("DRIVER1"));
						MOBILE.setValue(result.get(0).get("MOBILE1"));
						
						}
						
					}
   					
   					
   					
   				});
			}
		});
       DynamicForm form1=new DynamicForm();
       form1.setItems(DUTY_COMPANY);
       form1.setHeight("20%");
       form1.setTitleSuffix("");
       
       layOut.addMember(form);
       layOut.addMember(inForm);
       layOut.addMember(form1);
       
       return layOut;
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
        
        uploadForm.setItems(imageItem,notes,saveItem);
        
        tileCanvas = new Canvas();
        tileCanvas.setBorder("1px solid black");
        tileCanvas.setHeight(200);  
        tileCanvas.setWidth100(); 
        tileCanvas.setShowResizeBar(true);
     
        vLay.setMembers(uploadForm,tileCanvas,htmlPane);
        
        return vLay;
	}
	
	public Window getCheckWin() {
		Window win=new Window();
		SGPanel checkForm=new SGPanel();
		StaticTextItem APPROVER=new StaticTextItem("APPROVER","送审人");
		//APPROVER.setTitle("送审人");
		//APPROVER.setHeight(30);
		//APPROVER.setWidth(200);
		APPROVER.setValue("wpsAdmin");
		
		RadioGroupItem rgItem=new RadioGroupItem("STATUS","审批");
		rgItem.setStartRow(true);
		rgItem.setValueMap("同意","打回");
		rgItem.setColSpan("*");  
		rgItem.setRowSpan(1);
		rgItem.setVertical(false); 
		rgItem.setWidth("30%");
		SGText APPROVE_TIME = new SGText("APPROVE_TIME", "时间");//损坏数量
		//TextAreaItem APPROVE_TIME = new TextAreaItem("APPROVE_TIME","时间"); 
		APPROVE_TIME.setStartRow(true);
		APPROVE_TIME.setTitleOrientation(TitleOrientation.LEFT);
		//APPROVE_TIME.setTitleAlign(Alignment.LEFT);
		//APPROVE_TIME.setLeft(50);
		//APPROVE_TIME.setHeight(30);
		Util.initDateTime(checkForm,APPROVE_TIME);

//		APPROVE_TIME.setWidth(FormUtil.longWidth);
//		Date date=new Date();
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		APPROVE_TIME.setDefaultValue(sdf.format(date));
		
		TextAreaItem checknotes = new TextAreaItem("NOTES", "审批意见");
		checknotes.setStartRow(true);
		checknotes.setColSpan(6);
		checknotes.setHeight(70);
		checknotes.setWidth(FormUtil.longWidth);
		//checknotes.setTitleOrientation(TitleOrientation.TOP);
//		  checknotes.setTitleVAlign(VerticalAlignment.TOP);
		
		//Util.initDateTime(checkForm,APPROVE_TIME);
		
		checkForm.setItems(APPROVER,rgItem,APPROVE_TIME,checknotes);
		checkForm.setIsGroup(true);  
		checkForm.setGroupTitle("赔偿单审批");
		checkForm.setMargin(3);
		checkForm.setPadding(20);
		checkForm.setHeight("70%");
		checkForm.setWidth("99%");
		checkForm.setLeft("10%");
		checkForm.setAlign(Alignment.CENTER);
		checkForm.setTitleSuffix(":");
		//checkForm.set
		win.addItem(checkForm);
		
		 
		
		ToolStrip recivetoolStrip = new ToolStrip();
		//recivetoolStrip.setAlign(Alignment.LEFT);
		recivetoolStrip.setWidth("100%");
		recivetoolStrip.setHeight("20");
		recivetoolStrip.setPadding(2);
		recivetoolStrip.setSeparatorSize(12);
		recivetoolStrip.addSeparator();
		recivetoolStrip.setMembersMargin(4);
		recivetoolStrip.setAlign(Alignment.RIGHT);
		
		
		IButton cancelButton=createUDFBtn("取消", StaticRef.ICON_CANCEL,TrsPrivRef.TRACK_P2_02);
		    
		IButton saveButton=createUDFBtn("确定", StaticRef.ICON_SAVE,TrsPrivRef.TRACK_P2_02);
	    
		recivetoolStrip.setMembers(saveButton,cancelButton);
		
		win.addItem(recivetoolStrip);
		win.setTitle("审批操作"); 
		win.setWidth("30%");
		win.setHeight("40%");
		win.setTop("20%");
		win.setLeft("40%");
		return win;  
	  }


	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
		if(checkWin != null) {
			checkWin.destroy();
			checkForm.destroy();
		}
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		ClaimApproveView view = new ClaimApproveView();
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