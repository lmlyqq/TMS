package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.ImageUploadAction;
import com.rd.client.common.action.ImageViewAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.PayDeductDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.LoadBmsWin;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ContentsType;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.types.FormMethod;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
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
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownHandler;
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
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 
 * 应付扣款单
 *
 */
@ClassForNameAble
public class PayDeductView extends SGForm implements PanelFactory{
	
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
	 private IButton canfirmButton;
	 public IButton confirmButton;
	 
	 /*public PayDeductView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.LEFT);
	    ds = PayDeductDS.getInstance("V_PAYDEDUCT","BILL_PAY_DEDUCT");
		
	    //主布局
		HStack stack = new HStack();
		stack.setWidth("100%");
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
	    section.setWidth("50%");
		stack.addMember(section);
	
		//右表
		leftTabSet = new TabSet();  
        leftTabSet.setWidth("50%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        
        if(isPrivilege(BasPrivRef.PayDeduct_P0)) {
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
        if(isPrivilege(BasPrivRef.PayDeduct_P1)){
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
				enableOrDisables(add_map, false);   
				enableOrDisables(del_map, false); 
				if(isMax) {
					expend();
				}
				if(table.getSelectedRecord().getAttribute("STATUS_NAME").equals("已确认")){
					initCancelBtn();
				}
		 	}
				
		});
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
            	main_form.editRecord(selectedRecord);
            	main_form.setValue("OP_FLAG", StaticRef.MOD_FLAG);
                initSaveBtn();
                if(event.getRecord().getAttribute("STATUS_NAME").equals("已确认")){
                	initCancelBtn();
                	canfirmButton.enable();
					confirmButton.disable();
				} else {
					canfirmButton.disable();
					confirmButton.enable();
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
		
		
		
		
		initVerify();
        return main;
              
	}

 
	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	private void getConfigList() {
		
        ListGridField SUPLR_ID_NAME = new ListGridField("SUPLR_ID_NAME", "承运商", 100);
        ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属账期", 80);
        ListGridField STATUS = new ListGridField("STATUS_NAME", "状态", 60);
        ListGridField DEDUCT_TYPE = new ListGridField("DEDUCT_TYPE_NAME", "扣款类型", 80);
        ListGridField DEDUCT_AMOUNT = new ListGridField("DEDUCT_AMOUNT", "扣款金额", 75);
        ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单", 100);
        ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 90);
        ListGridField SHPM_NO = new ListGridField("SHPM_NO", "托运单号", 90);
        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", "客户单号", 100);
        table.setFields(SUPLR_ID_NAME, BELONG_MONTH,STATUS,DEDUCT_TYPE,DEDUCT_AMOUNT,LOAD_NO, PLATE_NO, SHPM_NO, CUSTOM_ODR_NO);
	}
		

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.PayDeduct);
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
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.PayDeduct_P0_01);
        newButton.addClickHandler(new NewFormAction(main_form,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.PayDeduct_P0_02);
        saveButton.addClickHandler(new SaveFormAction(table, main_form, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.PayDeduct_P0_03);
        delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.PayDeduct_P0_04);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
        
        confirmButton = createUDFBtn("确认",StaticRef.ICON_CONFIRM,BasPrivRef.PayDeduct_P0_05);
        confirmButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord().getAttribute("STATUS_NAME").equals("已确认")){
					SC.say("扣款单已确认");
					return;
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("update BILL_PAY_DEDUCT set STATUS='C71638421A6C450382CFF8C7C201718F' where ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
				sqlList.add(sf.toString());
				Util.async.excuteSQLList(sqlList, new AsyncCallback<String>() {
					
					@Override
					public void onSuccess(String result) {
						MSGUtil.showOperSuccess();
						table.getSelectedRecord().setAttribute("STATUS_NAME","已确认");
						table.redraw();
						canfirmButton.enable();
						confirmButton.disable();
						enableOrDisables(add_map, true);
				        enableOrDisables(del_map, false);
				        enableOrDisables(save_map, false);
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
        	
        });
        
        
        canfirmButton = createUDFBtn("取消确认",StaticRef.ICON_CONFIRM,BasPrivRef.PayDeduct_P0_06);
        canfirmButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				Util.db_async.getRecord("INIT_NO", "BILL_PAY_DEDUCT", " where ID='"+table.getSelectedRecord().getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
					
					@Override
					public void onSuccess(ArrayList<HashMap<String, String>> result) {
						if(result!=null&&result.size()>0){
							if(!result.get(0).get("INIT_NO").toString().equals("X")){
								SC.say("扣款单已被引用,无法取消确认");
								return;
							}else{
								ListGridRecord record = table.getSelectedRecord();
								if(record.getAttribute("STATUS_NAME").equals("已创建")){
									SC.say("补贴单未确认");
									return;
								}
								
								HashMap<String, Object> listmap = new HashMap<String, Object>(); 
								listmap.put("1", record.getAttribute("ID"));
								listmap.put("2", LoginCache.getLoginUser().getUSER_ID());
								String json = Util.mapToJson(listmap);

								Util.async.execProcedure(json, "BMS_PAY_DEDUCTCANCEL(?,?,?)", new AsyncCallback<String>() {
									
									@Override
									public void onSuccess(String result) {
										MSGUtil.showOperSuccess();
										table.getSelectedRecord().setAttribute("STATUS_NAME","已创建");
										table.redraw();
										confirmButton.enable();
										canfirmButton.disable();
										enableOrDisables(add_map, true);
								        enableOrDisables(del_map, true);
								        enableOrDisables(save_map, false);
									}
									
									@Override
									public void onFailure(Throwable caught) {
										
									}
								});
							}
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
				
			}
        	
        });
      
        save_map.put(BasPrivRef.PayDeduct_P0_02, saveButton);
        save_map.put(BasPrivRef.PayDeduct_P0_04, canButton);
        
        add_map.put(BasPrivRef.PayDeduct_P0_01, newButton);
        del_map.put(BasPrivRef.PayDeduct_P0_03, delButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        confirmButton.disable();
        canfirmButton.disable();
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton,newButton,saveButton,delButton,canButton,confirmButton,canfirmButton);
	}
	
	public void initVerify() {
		check_map.put("TABLE", "BILL_PAY_DEDUCT");	
		check_map.put("SUPLR_ID", StaticRef.CHK_NOTNULL + "承运商");
		check_map.put("BELONG_MONTH", StaticRef.CHK_NOTNULL + "所属账期");
		check_map.put("DEDUCT_TYPE", StaticRef.CHK_NOTNULL + "扣款类型");
		check_map.put("DEDUCT_AMOUNT", StaticRef.CHK_NOTNULL + "扣款金额");
		
		cache_map.put("STATUS", "0B442D1F9B044E73AB891EBA65E28576");
		cache_map.put("INIT_NO", "X");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(final DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		SGText SUPLR_ID_NAME = new SGText("SUPLR_ID_NAME","承运商");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		SUPLR_ID_NAME.setIcons(searchPicker);
		
		
		SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		
		SGText LOAD_NO = new SGText("LOAD_NO","调度单号",true);
		
		SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
		
		SGCombo DEDUCT_TYPE=new SGCombo("DEDUCT_TYPE", "扣款类型");
		Util.initCodesComboValue(DEDUCT_TYPE,"DEDUCT_TYP");
		
		SGText SHPM_NO = new SGText("SHPM_NO","托运单号");
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户单号");
		
		SGCombo STATUS = new SGCombo("STATUS","状态");
		Util.initComboValue(STATUS,"BAS_CODES","ID","NAME_C","PROP_CODE='TRANS_ODR_STAT' and NAME_C='已创建' or PROP_CODE='TRANS_ODR_STAT' and NAME_C='已确认'");
		
        form.setItems(SUPLR_ID,SUPLR_ID_NAME,BELONG_MONTH,DEDUCT_TYPE,STATUS,LOAD_NO,PLATE_NO,SHPM_NO,CUSTOM_ODR_NO);
        
        return form;
	}
	//主窗口
	private VLayout createMainForm(final DynamicForm form) {
	
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		
		final SGText SUPLR_ID_NAME = new SGText("SUPLR_ID_NAME",ColorUtil.getRedTitle("承运商"));
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		SUPLR_ID_NAME.setIcons(searchPicker);
		
		final SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SUPLR_ID_NAME.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				
				if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
					final String sup_name = ObjUtil.ifObjNull(SUPLR_ID_NAME.getValue(),"").toString();
					if(sup_name.equals("")){
						return;
					}
					System.out.println(123);
					Util.db_async.getRecord("ID,SUPLR_CNAME", "V_SUPPLIER", 
							" where upper(full_index) like upper('%"+sup_name+"%')", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							if(size == 1){
								HashMap<String, String> map= result.get(0);
								SUPLR_ID_NAME.setValue(map.get("SUPLR_CNAME"));
								SUPLR_ID.setValue(map.get("ID"));
							}else if(size > 1){
								new SuplrWin(form,"20%", "32%",sup_name).getViewPanel();
							}
							
						}
						
						@Override
						public void onFailure(Throwable caught) {
							
						}
					});
			     }
			}
		});
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH",ColorUtil.getRedTitle("所属账期"));
		
		SGCombo DEDUCT_TYPE = new SGCombo("DEDUCT_TYPE",ColorUtil.getRedTitle("扣款类型"));
		Util.initCodesComboValue(DEDUCT_TYPE,"DEDUCT_TYP");
		
		final SGText DEDUCT_AMOUNT = new SGText("DEDUCT_AMOUNT",ColorUtil.getRedTitle("扣款金额"),true);
		
		final SGText LOAD_NO = new SGText("LOAD_NO","调度单号");
        PickerIcon searchPicker1 = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				if(SUPLR_ID_NAME.getValue()==null){
					SC.say("请选择承运商");
					return;
				}
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("SUPLR_ID_NAME", SUPLR_ID_NAME.getValue().toString());
				map.put("SUPLR_ID", SUPLR_ID.getValue().toString());
				new LoadBmsWin(form,"20%","50%",map).getViewPanel();		
			}
		});
		LOAD_NO.setIcons(searchPicker1);

        final SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
        PLATE_NO.setDisabled(true);
        
        final SGCombo SHPM_NO = new SGCombo("SHPM_NO","作业单号",true);	
        
        final SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户单号");
        CUSTOM_ODR_NO.setDisabled(true);
        
        SHPM_NO.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
			
				String value=SHPM_NO.getDisplayValue();
				
				Util.db_async.getSingleRecord("CUSTOM_ODR_NO", "trans_shipment_header", "where SHPM_NO='"+value+"'",null, new AsyncCallback<HashMap<String,String>>() {
					
					@Override
					public void onSuccess(HashMap<String, String> result) {
						CUSTOM_ODR_NO.setValue(result.get("CUSTOM_ODR_NO"));
					}
					
					@Override
					public void onFailure(Throwable caught) {
					
						
						
					}
				});
			}
		});
        
        TextAreaItem DESCR = new TextAreaItem("DESCR", "扣款说明");
        DESCR.setStartRow(true);
        DESCR.setColSpan(6);
        DESCR.setHeight(50);
        DESCR.setWidth(FormUtil.longWidth+FormUtil.Width);
        DESCR.setTitleOrientation(TitleOrientation.TOP);
        DESCR.setTitleVAlign(VerticalAlignment.TOP);
        
        SGCombo STATUS = new SGCombo("STATUS",ColorUtil.getRedTitle("状态 "));
		Util.initCodesComboValue(STATUS,"TRANS_ODR_STAT");
		STATUS.setVisible(false);
        
        SGText VEHICLE_TYP_ID = new SGText("VEHICLE_TYP_ID","");
        VEHICLE_TYP_ID.setVisible(false);

        form.setWidth("40%");
        form.setItems(SUPLR_ID_NAME,SUPLR_ID,BELONG_MONTH,DEDUCT_TYPE,DEDUCT_AMOUNT,LOAD_NO,PLATE_NO,SHPM_NO,CUSTOM_ODR_NO,DESCR,STATUS,VEHICLE_TYP_ID);
        form.setHeight("30%"); 
       

      
       layOut.addMember(form);
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
        setButtonItemEnabled(BasPrivRef.PayDeduct_P1_01,saveItem,true);
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
		PayDeductView view = new PayDeductView();
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
