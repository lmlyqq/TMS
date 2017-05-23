package com.rd.client.view.tms;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.SaveComplaintAction;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ComplaintDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.LoadNewWin;
import com.rd.client.win.NewOdrWin;
import com.rd.client.win.OdrWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
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
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
 * 运输管理--运输执行--投诉管理
 * @author Administrator
 *
 */
@ClassForNameAble
public class ComplaintView extends SGForm implements PanelFactory {

	private DataSource complaintDS;
	private SGTable table;
	private TabSet leftTabSet;
	private HStack stack;
	private SectionStack list_section;
	private SectionStack section;
	private DynamicForm mainForm;
	private ValuesManager vm;
	private Window searchWin;
	private SGPanel searchForm;
	private IButton saveButton ;
	private IButton delButton; 
	private IButton canButton; 
	private IButton finishButton;
	private Canvas canvas;
	public  Canvas tileCanvas;
	// private Window loadWin;
	/*public ComplaintView(String id) {
		super(id);
	}*/
	
	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		vm = new ValuesManager();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    complaintDS = ComplaintDS.getInstance("TRANS_COMPLAINT");
		
	    stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(complaintDS, "100%", "100%",true,true,false);
		createListField();
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		
		table.addDoubleClickHandler(new DoubleClickHandler() {

			@Override
			public void onDoubleClick(DoubleClickEvent event) {		
				ListGridRecord record=table.getSelectedRecord();
				if(record==null){
					return;
				}
				if(record.getAttribute("STATUS_NAME").equals("已完结")){
			
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, false);
					enableOrDisables(save_map, false);
					finishButton.setDisabled(true);
				}else{
					enableOrDisables(add_map, false);
					enableOrDisables(del_map, false);
					enableOrDisables(save_map, true);
					finishButton.setDisabled(false);
					if(isMax) {
						expend();
					}
					
				}
		
			}
			
		});
       
//		table.addSelectionChangedHandler(new SelectionChangedHandler() {
//			
//			@Override
//			public void onSelectionChanged(SelectionEvent event) {
//				Record record = event.getRecord();
//				vm.editRecord(record);
//                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
//				OP_FLAG ="M";
//				if(record.getAttribute("STATUS_NAME").equals("已完结")){
//					enableOrDisables(add_map, true);
//					enableOrDisables(del_map, false);
//					enableOrDisables(save_map, false);
//					finishButton.setDisabled(true);
//				}else{
//					initSaveBtn();
//				}
//			}
//		}); 
		
		
		
		list_section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    list_section.addSection(listItem);
	    list_section.setWidth("100%");
	    stack.addMember(list_section);
		addSplitBar(stack);
		
		//STACK的右边布局
        leftTabSet = new TabSet();  
        leftTabSet.setWidth("80%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        leftTabSet.setVisible(false);
        
        initVerify();
        
        Tab tab1 = new Tab(Util.TI18N.MAININFO());
		tab1.setPane(createHeader());
		leftTabSet.addTab(tab1);
		
		if(isPrivilege(BasPrivRef.Complaint_P1)){
			Tab tab2 = new Tab("附件信息");       
			tab2.setPane(createImgInfo());	       
			leftTabSet.addTab(tab2);    
		}
	
        stack.addMember(leftTabSet);        
        vm.addMember(mainForm);
        vm.setDataSource(complaintDS);
        //创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
	    
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
			 
				Record record = event.getRecord();
				if(record==null){
					return;
				}
				vm.editRecord(record);
                vm.setValue("OP_FLAG", StaticRef.MOD_FLAG);
				OP_FLAG ="M";
				System.out.println(1);
				System.out.println(record.getAttribute("STATUS_NAME"));
				if(record.getAttribute("STATUS_NAME").equals("已完结")){
					enableOrDisables(add_map, true);
					enableOrDisables(del_map, false);
					enableOrDisables(save_map, false);
					finishButton.setDisabled(true);
				}else{
					initSaveBtn();
					finishButton.setDisabled(false);						
				}			
					
				if(canvas != null){
						
					if(canvas.isCreated()){
						canvas.destroy();
					}
				}
				canvas = new Canvas();
				tileCanvas.addChild(canvas);
				canvas.setHeight(163);
				canvas.setWidth(780);
				if(record.getAttribute("ID") != null){
					ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_COMPLAINT_PREVIEW_URL,record.getAttribute("ID"));
					action.getName();
				}
		    
			}

		});
		
		
		return main;
	}
	
	private void createListField() {
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户名称",100);
		ListGridField STATUS = new ListGridField("STATUS","状态",80);
		Util.initComboValue(STATUS, "BAS_CODES", "ID", "NAME_C","","");
		ListGridField ODR_NO = new ListGridField("ODR_NO","托运单号",150);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",150);
		ListGridField LOAD_TIME = new ListGridField("LOAD_TIME","发货日期",150);
		ListGridField RECE_TIME = new ListGridField("RECE_TIME","收货日期",150);
		ListGridField LOAD_ID = new ListGridField("LOAD_ADDRESS","发货地",150);
		ListGridField UNLOAD_ID = new ListGridField("UNLOAD_ADDRESS","收货地",150);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车号",120);
		ListGridField DRIVER = new ListGridField("DRIVER","驾驶员",80);
		ListGridField MOBILE = new ListGridField("MOBILE","联系方式",100);
		ListGridField CUSTOM_SERVICE = new ListGridField("CUSTOM_SERVICE","客服",80);
		ListGridField DUTY_TO = new ListGridField("DUTY_TO","责任人",80);
		ListGridField ID = new ListGridField("ID","ID",80);
		ID.setHidden(true);
		
		table.setFields(ID,CUSTOMER_NAME,STATUS,ODR_NO,LOAD_NO,LOAD_TIME,RECE_TIME,LOAD_ID,UNLOAD_ID,PLATE_NO,
				DRIVER,MOBILE,CUSTOM_SERVICE,DUTY_TO);	
		table.setCanDragRecordsOut(true);
		table.setCanReorderRecords(true);
		table.setCanAcceptDroppedRecords(true);
		table.setDragDataAction(DragDataAction.MOVE);
	}
	
	private SectionStack createHeader() {
		//1
		mainForm = new SGPanel();

		final SGText ODR_NO = new SGText("ODR_NO", "托运单号",true);
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				new NewOdrWin(mainForm,"20%","20%").getViewPanel();		
			}
		});
	   
		ODR_NO.setIcons(searchPicker);
//	    ODR_NO.addBlurHandler(new BlurHandler() {
//			
//			@Override
//			public void onBlur(BlurEvent event) {
//				String odr_no=ObjUtil.ifNull(ODR_NO.getValue(),"").toString();
//				new OdrWin(mainForm,"20%","50%",odr_no).getViewPanel();
//			}
//		});
		
		SGText CUSTOMER_ID = new SGText("CUSTOMER_ID", "客户ID");
		CUSTOMER_ID.setVisible(false);
		
		SGText CUSTOMER_NAME = new SGText("CUSTOMER_NAME", "客户名称");
		CUSTOMER_NAME.setDisabled(true);
		
		SGDateTime LOAD_TIME = new SGDateTime("LOAD_TIME", "发货日期");
		LOAD_TIME.setWidth(FormUtil.Width);
		
		SGDateTime RECE_TIME = new SGDateTime("RECE_TIME", "到货日期");
		RECE_TIME.setWidth(FormUtil.Width);
		//2
		SGLText LOAD_ADDRESS = new SGLText("LOAD_ADDRESS", "出发地2",true);
		LOAD_ADDRESS.setDisabled(true);
		
		SGLText UNLOAD_ADDRESS = new SGLText("UNLOAD_ADDRESS", "到达地2");
		UNLOAD_ADDRESS.setDisabled(true);
		//3   
		final SGText LOAD_NO = new SGText("LOAD_NO", "调度单号",true);
		LOAD_NO.setDisabled(true);
//		PickerIcon searchPicker1 = new PickerIcon(PickerIcon.SEARCH);
//		searchPicker1.addFormItemClickHandler(new FormItemClickHandler() {
//			
//			@Override
//			public void onFormItemClick(FormItemIconClickEvent event) {
//				 new LoadNewWin(mainForm,"20%","50%").getViewPanel();		
//			}
//		});
//		LOAD_NO.setIcons(searchPicker1);
		
//		LOAD_NO.addBlurHandler(new BlurHandler() {
//			
//			@Override
//			public void onBlur(BlurEvent event) {
//				String load_no=ObjUtil.ifNull(LOAD_NO.getValue(),"").toString();
//				new LoadNewWin(mainForm,"20%","50%",load_no).getViewPanel();
//			}
//		});
		
		SGText PLATE_NO = new SGText("PLATE_NO", "车号");
		PLATE_NO.setDisabled(true);
		SGText DRIVER = new SGText("DRIVER", "驾驶员");
		DRIVER.setDisabled(true);
		SGText MOBILE = new SGText("MOBILE", "联系方式");
		MOBILE.setDisabled(true);
		//4
		SGText CUSTOM_SERVICE = new SGText("CUSTOM_SERVICE", "客服",true);
		SGText DUTY_TO = new SGText("DUTY_TO", "责任人");
		
		SGCombo STATUS = new SGCombo("STATUS", "状态");
		Util.initCodesComboValue(STATUS,"COMPLAINT_STS");
		
		//5
		TextAreaItem NOTES = new TextAreaItem("NOTES","投诉内容");
		NOTES.setStartRow(true);
		NOTES.setColSpan(8);
		NOTES.setHeight(50);
		NOTES.setWidth(FormUtil.longWidth+FormUtil.longWidth);
		NOTES.setTitleOrientation(TitleOrientation.TOP);
		NOTES.setTitleVAlign(VerticalAlignment.TOP);
		TextAreaItem REASON_DESCR = new TextAreaItem("REASON_DESCR","原因分析（营运）");
		//REASON_DESCR.setStartRow(true);
		REASON_DESCR.setColSpan(4);
		REASON_DESCR.setHeight(50);
		REASON_DESCR.setWidth(FormUtil.longWidth);
		REASON_DESCR.setTitleOrientation(TitleOrientation.TOP);
		REASON_DESCR.setTitleVAlign(VerticalAlignment.TOP);
		
		TextAreaItem SERVICE_DESCR = new TextAreaItem("SERVICE_DESCR","原因分析（客服）");
		SERVICE_DESCR.setColSpan(4);
		SERVICE_DESCR.setHeight(50);
		SERVICE_DESCR.setWidth(FormUtil.longWidth);
		SERVICE_DESCR.setTitleOrientation(TitleOrientation.TOP);
		SERVICE_DESCR.setTitleVAlign(VerticalAlignment.TOP);
		
		//6
		TextAreaItem DEAL_METHOD = new TextAreaItem("DEAL_METHOD","处理方法");
		DEAL_METHOD.setStartRow(true);
		DEAL_METHOD.setColSpan(4);
		DEAL_METHOD.setHeight(50);
		DEAL_METHOD.setWidth(FormUtil.longWidth);
		DEAL_METHOD.setTitleOrientation(TitleOrientation.TOP);
		DEAL_METHOD.setTitleVAlign(VerticalAlignment.TOP);
		TextAreaItem PROMPT_METHOD = new TextAreaItem("PROMPT_METHOD","改进方案");
		PROMPT_METHOD.setColSpan(4);
		PROMPT_METHOD.setHeight(50);
		PROMPT_METHOD.setWidth(FormUtil.longWidth);
		PROMPT_METHOD.setTitleOrientation(TitleOrientation.TOP);
		PROMPT_METHOD.setTitleVAlign(VerticalAlignment.TOP);
		//7
		TextAreaItem CUSTOM_FEEBACK = new TextAreaItem("CUSTOM_FEEBACK","客户反馈");
		CUSTOM_FEEBACK.setStartRow(true);
		CUSTOM_FEEBACK.setColSpan(8);
		CUSTOM_FEEBACK.setHeight(50);
		CUSTOM_FEEBACK.setWidth(FormUtil.longWidth+FormUtil.longWidth);
		CUSTOM_FEEBACK.setTitleOrientation(TitleOrientation.TOP);
		CUSTOM_FEEBACK.setTitleVAlign(VerticalAlignment.TOP);
				
        mainForm.setWidth("40%");
		mainForm.setItems(ODR_NO,CUSTOMER_ID,CUSTOMER_NAME,LOAD_TIME,RECE_TIME,LOAD_ADDRESS,UNLOAD_ADDRESS,
				LOAD_NO,PLATE_NO,DRIVER,MOBILE,CUSTOM_SERVICE,DUTY_TO,STATUS,NOTES,SERVICE_DESCR,
				REASON_DESCR,DEAL_METHOD,PROMPT_METHOD,CUSTOM_FEEBACK);
		
        section = new SectionStack();
		section.setBackgroundColor(ColorUtil.BG_COLOR);
		SectionStackSection mainS = new SectionStackSection("主信息");
		mainS.addItem(mainForm);
		mainS.setExpanded(true); 
		section.addSection(mainS);
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
	        
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(complaintDS,createSerchForm(searchForm),list_section.getSection(0),vm).getViewPanel();
				} else {
					searchWin.show();
				}
			}
		});
	        
			
		IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.Complaint_P0_01);
		newButton.addClickHandler(new NewMultiFormAction(vm,cache_map,this));

		saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.Complaint_P0_02);
		saveButton.addClickHandler(new SaveComplaintAction(table,vm,check_map, this));
	        
		delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.Complaint_P0_03);
		delButton.addClickHandler(new DeleteFormAction(table,mainForm));
	        
		canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.Complaint_P0_04);
		canButton.addClickHandler(new CancelFormAction(table,mainForm,this));
	        
		finishButton = createBtn("处理完毕",BasPrivRef.Complaint_P0_05);
		finishButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final ListGridRecord record=table.getSelectedRecord();
				if(record==null){
					SC.say("请选择记录");
					return;
				}
				SC.confirm("是否执行处理完毕操作？", new BooleanCallback() {
					public void execute(Boolean value) {
	                    if (value != null && value) {
	                    	String sql="update TRANS_COMPLAINT SET STATUS='B2F67CD5A0E1491E8824DDA86E9BED46' where ID='"+record.getAttribute("ID")+"' ";
	        				
	        				Util.async.excuteSQL(sql, new AsyncCallback<String>() {
	        					@Override
	        					public void onFailure(Throwable caught) {
	        						MSGUtil.sayError(caught.getMessage());
	        					}
	        			
	        					@Override
	        					public void onSuccess(String result) {
	        						if(result.equals(StaticRef.SUCCESS_CODE)) {
	        							MSGUtil.showOperSuccess();
	        							record.setAttribute("STATUS","B2F67CD5A0E1491E8824DDA86E9BED46");
	        							mainForm.getField("STATUS").setValue("B2F67CD5A0E1491E8824DDA86E9BED46");
	        							record.setAttribute("STATUS_NAME","已完结");
	        							delButton.setDisabled(true);
	        							finishButton.setDisabled(true);
	        							table.redraw();
	        							
	        						} else {
	        								
	        							MSGUtil.sayError(result);
	        								
	        						}
	        						
	        					}
	        					
	        				});
	                    }
	                }
	            });
				
				
			}
			
		});    
		
		
		add_map.put(BasPrivRef.Complaint_P0_01, newButton);
		del_map.put(BasPrivRef.Complaint_P0_03, delButton);
		save_map.put(BasPrivRef.Complaint_P0_02, saveButton);
		save_map.put(BasPrivRef.Complaint_P0_04, canButton);
		this.enableOrDisables(add_map, true);
		this.enableOrDisables(del_map, false);
		this.enableOrDisables(save_map, false);
	        
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton,finishButton);
	}

	public DynamicForm createSerchForm(final SGPanel form){
		form.setDataSource(complaintDS);
		form.setAutoFetchData(false);
		form.setAlign(Alignment.LEFT);
		
		SGText ODR_NO = new SGText("ODR_NO", "托运单号");
		PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
			searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
				
				@Override
				public void onFormItemClick(FormItemIconClickEvent event) {
					 new OdrWin(form,"20%","50%").getViewPanel();		
				}
			});
		ODR_NO.setIcons(searchPicker);
		
		SGText LOAD_NO = new SGText("LOAD_NO", "调度单号");
        PickerIcon searchPicker1 = new PickerIcon(PickerIcon.SEARCH);
		searchPicker1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				
				 new LoadNewWin(form,"20%","50%").getViewPanel();		
			}
		});
		LOAD_NO.setIcons(searchPicker1);
		
		
		SGCombo STATUS = new SGCombo("STATUS", "状态");
		Util.initCodesComboValue(STATUS,"COMPLAINT_STS");
		
        final SGText CUSTOMER_ID=new SGText("CUSTOMER_ID","");
		CUSTOMER_ID.setVisible(false);
		//CUSTOMER_ID.setColSpan(2);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		//CUSTOMER_NAME.setWidth(120);
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		
		SGText CUSTOM_SERVICE = new SGText("CUSTOM_SERVICE", "客服");
		
		SGText DUTY_TO = new SGText("DUTY_TO", "责任人");
		
		form.setItems(ODR_NO,LOAD_NO,STATUS,CUSTOMER_ID,CUSTOMER_NAME,CUSTOM_SERVICE,DUTY_TO);
		
		
		return form;
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
        setButtonItemEnabled(BasPrivRef.Complaint_P1_P0,saveItem,true);
        saveItem.setWidth(60);
        saveItem.setAutoFit(true);
        saveItem.setIcon(StaticRef.ICON_NEW);
        saveItem.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				if(table.getSelectedRecord() !=  null){
					if(table.getSelectedRecord().getAttribute("ID") != null){
						Map<String,String> map = uploadForm.getValues();
						if(map.get("image") != null){
							String customer = table.getSelectedRecord().getAttribute("ID").toString();
							String image = map.get("image").toString();
							new ImageUploadAction(customer,StaticRef.BAS_COMPLAINT_URL,image,uploadForm).onClick(event);	
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
							    if(table.getSelectedRecord().getAttribute("ID") != null){
									ImageViewAction action = new ImageViewAction(canvas,table,StaticRef.BAS_COMPLAINT_PREVIEW_URL,table.getSelectedRecord().getAttribute("ID"));
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
        setButtonItemEnabled(BasPrivRef.Complaint_P1_P1,downLoadButton,true);
        downLoadButton.setWidth(60);
        downLoadButton.setAutoFit(true);
        downLoadButton.setIcon(StaticRef.ICON_NEW);
        downLoadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

			@Override
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				ListGridRecord record=table.getSelectedRecord();
				if(record!=null){
				String cusId=record.getAttribute("ID");
					//Util.downLoadUtil(cusId);
				String filePath="test/COMPLAINT/"+cusId;
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
	
	
	@Override
	public void initVerify() {
		check_map.put("LOAD_TIME", StaticRef.CHK_DATE + "发货日期");
		check_map.put("RECE_TIME", StaticRef.CHK_DATE + "到货日期");
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		ComplaintView view = new ComplaintView();
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
