package com.rd.client.view.settlement;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelFormAction;
import com.rd.client.common.action.DeleteFormAction;
import com.rd.client.common.action.NewFormAction;
import com.rd.client.common.action.SaveFormAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.RecAllowanceDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.CustomerWin;
import com.rd.client.win.LoadWin;
import com.rd.client.win.OdrBmsWin;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
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
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 费用管理-结算管理-应收补贴单
 * @author cjt
 *
 */
@ClassForNameAble
public class RecAllowanceView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private DynamicForm main_form;             
	private SectionStack section;
	private TabSet leftTabSet;
	private Window searchWin = null;
	private DynamicForm searchForm;
	public IButton canfirmButton;
    public IButton confirmButton;
	
	/*public RecAllowanceView(String id) {
		super(id);
	}*/

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
	    ToolStrip toolStrip = new ToolStrip();  
	    toolStrip.setAlign(Alignment.LEFT);
	    ds = RecAllowanceDS.getInstance("V_BILL_REC_ALLOWANCE","BILL_REC_ALLOWANCE");
		
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
        
        if(isPrivilege(SettPrivRef.RecAllowance_P0)) {
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
        stack.addMember(leftTabSet);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		
		main.addMember(stack);
		
		table.addDoubleClickHandler(new DoubleClickHandler(){

			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
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
		
		initVerify();
        return main;
	}
	
	//主窗口
	private VLayout createMainForm(final DynamicForm form) {
	
		VLayout layOut = new VLayout();
		layOut.setWidth("100%");
		
		final SGText CUSTOMER_ID_NAME = new SGText("CUSTOMER_ID_NAME",ColorUtil.getRedTitle("客户名称"));
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new CustomerWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		CUSTOMER_ID_NAME.setIcons(searchPicker);
		
		final SGText CUSTOMER_ID = new SGText("CUSTOMER_ID","客户代码");
		CUSTOMER_ID.setVisible(false);
		
		CUSTOMER_ID_NAME.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getKeyName()!=null&&(("Enter").equals(event.getKeyName())||("Tab").equals(event.getKeyName()))){
					final String name = ObjUtil.ifObjNull(CUSTOMER_ID_NAME.getValue(),"").toString().toUpperCase();
					if(name.equals("")){
						return;
					}
					System.out.println(123);
					Util.db_async.getRecord("ID,CUSTOMER_CNAME", "V_CUSTOMER", 
							" where ENABLE_FLAG = 'Y' and CUSTOMER_FLAG = 'Y' and full_index like '%"+name+"%'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {
						
						@Override
						public void onSuccess(ArrayList<HashMap<String, String>> result) {
							int size = result.size();
							if(size == 1){
								HashMap<String, String> map= result.get(0);
								CUSTOMER_ID_NAME.setValue(map.get("CUSTOMER_CNAME"));
								CUSTOMER_ID.setValue(map.get("ID"));
							}else if(size > 1){
								new CustomerWin(form,"20%", "32%",name).getViewPanel();
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
		
		SGCombo ALLOWANCE_TYPE = new SGCombo("ALLOWANCE_TYPE",ColorUtil.getRedTitle("补贴类型"));
		Util.initCodesComboValue(ALLOWANCE_TYPE,"ALLOWANCE_TYP");
		
		final SGText ALLOWANCE_AMOUNT = new SGText("ALLOWANCE_AMOUNT",ColorUtil.getRedTitle("补贴金额"),true);
		
		final SGText LOAD_NO = new SGText("LOAD_NO","调度单号");
        PickerIcon searchPicker1 = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker1.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new LoadWin(form,"20%","50%").getViewPanel();		
			}
		});
		LOAD_NO.setIcons(searchPicker1);

        final SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
        PLATE_NO.setDisabled(true);
        
        final SGCombo SHPM_NO = new SGCombo("SHPM_NO", "作业单号",true);	
        
        final SGText ODR_NO = new SGText("ODR_NO", ColorUtil.getRedTitle("托运单号"));
		PickerIcon searchPicker2 = new PickerIcon(PickerIcon.SEARCH);
		searchPicker2.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				if(CUSTOMER_ID_NAME.getValue()==null){
					SC.say("请选择客户");
					return;
				}
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("CUSTOMER_ID_NAME", CUSTOMER_ID_NAME.getValue().toString());
				new OdrBmsWin(form,"20%","50%",map).getViewPanel();		
			}
		});
	    ODR_NO.setIcons(searchPicker2);
	    

        final SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户单号");
        CUSTOM_ODR_NO.setDisabled(true);
        
        SHPM_NO.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
			
				String value=SHPM_NO.getDisplayValue();
				
				Util.db_async.getSingleRecord("ODR_NO", "trans_shipment_header", "where SHPM_NO='"+value+"'",null, new AsyncCallback<HashMap<String,String>>() {
					
					@Override
					public void onSuccess(HashMap<String, String> result) {
						ODR_NO.setValue(result.get("ODR_NO"));
					}
					
					@Override
					public void onFailure(Throwable caught) {
						
					}
				});
			}
		});
        
        TextAreaItem DESCR = new TextAreaItem("DESCR", "补贴说明");
        DESCR.setStartRow(true);
        DESCR.setColSpan(6);
        DESCR.setHeight(50);
        DESCR.setWidth(FormUtil.longWidth+FormUtil.Width);
        DESCR.setTitleOrientation(TitleOrientation.TOP);
        DESCR.setTitleVAlign(VerticalAlignment.TOP);
        
        SGCombo STATUS = new SGCombo("STATUS",ColorUtil.getRedTitle("状态 "));
		Util.initCodesComboValue(STATUS,"TRANS_ODR_STAT");
		STATUS.setVisible(false);

        form.setWidth("40%");
        form.setItems(CUSTOMER_ID_NAME,CUSTOMER_ID,BELONG_MONTH,ALLOWANCE_TYPE,ALLOWANCE_AMOUNT,LOAD_NO,PLATE_NO,SHPM_NO,ODR_NO,CUSTOM_ODR_NO,DESCR,STATUS);
        form.setHeight("30%"); 
       

      
       layOut.addMember(form);
       return layOut;
	}

	private void getConfigList() {
		
        ListGridField CUSTOMER_ID_NAME = new ListGridField("CUSTOMER_ID_NAME", "客户名称", 110);
        ListGridField BELONG_MONTH = new ListGridField("BELONG_MONTH", "所属账期", 90);
        ListGridField STATUS = new ListGridField("STATUS_NAME", "状态", 100);
        ListGridField LOAD_NO = new ListGridField("LOAD_NO", "调度单号", 90);
        ListGridField PLATE_NO = new ListGridField("PLATE_NO", "车牌号", 100);
        ListGridField ALLOWANCE_TYPE = new ListGridField("ALLOWANCE_TYPE_NAME", "补贴类型", 80);
        ListGridField ALLOWANCE_AMOUNT = new ListGridField("ALLOWANCE_AMOUNT", "补贴金额", 75);
        
        table.setFields(CUSTOMER_ID_NAME,BELONG_MONTH,STATUS,ALLOWANCE_TYPE,ALLOWANCE_AMOUNT,LOAD_NO,PLATE_NO);
	}
	
	@Override
	public void createBtnWidget(ToolStrip strip) {
		strip.setWidth("100%");
		strip.setHeight("20");
		strip.setPadding(2);
		strip.setSeparatorSize(12);
		strip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.RecAllowance_P0);
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
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SettPrivRef.RecAllowance_P0_01);
        newButton.addClickHandler(new NewFormAction(main_form,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.RecAllowance_P0_02);
        saveButton.addClickHandler(new SaveFormAction(table, main_form, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SettPrivRef.RecAllowance_P0_03);
        delButton.addClickHandler(new DeleteFormAction(table, main_form));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.RecAllowance_P0_04);
        canButton.addClickHandler(new CancelFormAction(table, main_form,this));
  
        confirmButton = createUDFBtn("确认",StaticRef.ICON_CONFIRM,SettPrivRef.RecAllowance_P0_05);
        confirmButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(table.getSelectedRecord().getAttribute("STATUS_NAME").equals("已确认")){
					SC.say("扣款单已确认");
					return;
				}
				ArrayList<String> sqlList = new ArrayList<String>();
				StringBuffer sf = new StringBuffer();
				sf.append("update BILL_REC_ALLOWANCE set STATUS='C71638421A6C450382CFF8C7C201718F' where ID='"+table.getSelectedRecord().getAttribute("ID")+"'");
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
        
        
        canfirmButton = createUDFBtn("取消确认",StaticRef.ICON_CONFIRM,SettPrivRef.RecAllowance_P0_06);
        canfirmButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				Util.db_async.getRecord("INIT_NO", "BILL_REC_ALLOWANCE", " where ID='"+table.getSelectedRecord().getAttribute("ID")+"'", null, new AsyncCallback<ArrayList<HashMap<String,String>>>() {

					@Override
					public void onFailure(Throwable caught) {
						
					}

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

								Util.async.execProcedure(json, "BMS_REC_ALLOWANCECANCEL(?,?,?)", new AsyncCallback<String>() {
									
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
				});
			}
        });
        
        add_map.put(SettPrivRef.RecAllowance_P0_01, newButton);
		del_map.put(SettPrivRef.RecAllowance_P0_03, delButton);
        save_map.put(SettPrivRef.RecAllowance_P0_02, saveButton);
        save_map.put(SettPrivRef.RecAllowance_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        confirmButton.disable();
        canfirmButton.disable();
        
        strip.setMembersMargin(4);
        strip.setMembers(searchButton,newButton,saveButton,delButton,canButton,confirmButton,canfirmButton);
	}
	
	public DynamicForm createSerchForm(final DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		SGText CUSTOMER_ID_NAME = new SGText("CUSTOMER_ID_NAME","客户");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new CustomerWin(form,"20%","50%").getViewPanel();		
			}
		});
       
		CUSTOMER_ID_NAME.setIcons(searchPicker);
		
		SGText CUSTOMER_ID = new SGText("CUSTOMER_ID","客户");
		CUSTOMER_ID.setVisible(false);
		
		SGText BELONG_MONTH = new SGText("BELONG_MONTH","所属期");
		
		SGCombo ALLOWANCE_TYPE=new SGCombo("ALLOWANCE_TYPE", "补贴类型");
		Util.initCodesComboValue(ALLOWANCE_TYPE,"ALLOWANCE_TYP");
		
		SGCombo STATUS=new SGCombo("STATUS", "状态");
		Util.initComboValue(STATUS,"BAS_CODES","ID","NAME_C","prop_code='TRANS_ODR_STAT' and name_C='已创建' or prop_code='TRANS_ODR_STAT' and name_C='已确认'");
		
		SGText LOAD_NO = new SGText("LOAD_NO","调度单号",true);
		
		SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
		
		SGText ODR_NO = new SGText("ODR_NO", "托运单号");
		
		SGText CUSTOM_ODR_NO = new SGText("CUSTOM_ODR_NO","客户单号");
		
		form.setItems(CUSTOMER_ID,CUSTOMER_ID_NAME,BELONG_MONTH,ALLOWANCE_TYPE,STATUS,LOAD_NO,PLATE_NO,ODR_NO,CUSTOM_ODR_NO);
		
		return form;
	}
	
	@Override
	public void initVerify() {
		check_map.put("TABLE", "BILL_REC_ALLOWANCE");
		check_map.put("CUSTOMER_ID", StaticRef.CHK_NOTNULL + "客户名称");
		check_map.put("BELONG_MONTH", StaticRef.CHK_NOTNULL + "所属账期");
		check_map.put("ALLOWANCE_TYPE", StaticRef.CHK_NOTNULL + "补贴类型");
		check_map.put("ALLOWANCE_AMOUNT", StaticRef.CHK_NOTNULL + "补贴金额");
		check_map.put("ODR_NO", StaticRef.CHK_NOTNULL + "托运单号");
	
		cache_map.put("STATUS", "0B442D1F9B044E73AB891EBA65E28576");
		cache_map.put("INIT_NO", "X");
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
		RecAllowanceView view = new RecAllowanceView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}

}
