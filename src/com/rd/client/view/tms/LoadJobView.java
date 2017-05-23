package com.rd.client.view.tms;

import com.google.gwt.core.client.JavaScriptObject;
import com.rd.client.PanelFactory;
import com.rd.client.action.tms.LoadCancelAction;
import com.rd.client.action.tms.dispatch.ChangedTotalQntyAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.LoadJobDS_1;
import com.rd.client.ds.tms.LoadJobDS_2;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.RecordSummaryFunctionType;
import com.smartgwt.client.types.SummaryFunctionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 运输管理--运输执行 --提货装车
 * @author wangjun
 *
 */
@ClassForNameAble
public class LoadJobView extends SGForm implements PanelFactory {

	public DataSource loadJobDS;
	public DataSource loadJobDS_;
	public SGTable loadLeftTable;
	public SGTable loadRightTable;
	//private Window leftWin;   yuanlei 2011-2-15
	//private Window rightWin;
	public Record clickrecord;
	private SectionStack section;
	private SectionStack section2;
	private boolean rmax = false;
	
	public IButton staLoadButton;
	public IButton finLoadButton;
	public IButton cancelButton;
	
	public IButton refeshButton;
	public Record finish_record;
	public Record record;
	public boolean crit_flag = true;
	public boolean crit_flag_ = true;
	/*public LoadJobView(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		initVerify();
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		loadJobDS = LoadJobDS_1.getInstance("V_TRANS_LOAD_JOB_2","TRANS_LOAD_JOB");
		loadJobDS_ = LoadJobDS_2.getInstance("V_TRANS_LOAD_JOB_3","TRANS_LOAD_JOB");
		// 主布局
		HStack stack = new HStack();// 设置详细信息布局
		stack.setWidth100();
		stack.setHeight100();
		
		loadLeftTable= new SGTable(loadJobDS, "100%", "100%", false , true, false);
		loadRightTable= new SGTable(loadJobDS_, "100%", "100%", false, true , false);
		
		createLeftFields(loadLeftTable);
		createRightFields(loadRightTable);
		
		loadLeftTable.setShowGridSummary(true);
		loadRightTable.setShowGridSummary(true);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		section = new SectionStack();
		section.setWidth("50%");
		section.setHeight("100%");
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LOAD_GOOD());//LOAD_GOOD=正在装货
	    listItem.setItems(loadLeftTable);
	    listItem.setExpanded(true);
	    listItem.setControls(addMaxBtn2(stack, "50%"), new SGPage(loadLeftTable, false).initPageBtn());
	    section.addSection(listItem);
		
		section2 = new SectionStack();
		section2.setWidth("50%");
		section2.setHeight("100%");
		final SectionStackSection listItem2 = new SectionStackSection(Util.TI18N.WAIT_VECH());//WAIT_VECH=等待车辆
	    listItem2.setItems(loadRightTable);
	    listItem2.setExpanded(true);
	    listItem2.setControls(addMaxBtn(stack, "50%"), new SGPage(loadRightTable, false).initPageBtn());
	    section2.addSection(listItem2);
	  
	    Criteria crit2 = new Criteria();
	    crit2.addCriteria("OP_FLAG","M");
	    crit2.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
	    crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
	    crit2.addCriteria("ORDER_BY", "DOCK_NAME");//正在装货按照DOCK排序
	    loadLeftTable.setCriteria(crit2);
	    loadLeftTable.fetchData(crit2,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				  	Criteria crit1 = new Criteria();
				    crit1.addCriteria("OP_FLAG","M");
				    crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
				    crit1.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
				    loadRightTable.fetchData(crit1 , new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
//							if(loadRightTable.getRecords().length >=1 ){
//								loadRightTable.selectRecord(loadRightTable.getRecord(0));
//							}
							
							if(loadRightTable.getRecords().length >= 1){
								loadRightTable.selectRecord(loadRightTable.getRecord(0));
								
								if(isPrivilege(TrsPrivRef.LOADJOB_P0_02)){
									staLoadButton.enable();
								}
								finLoadButton.disable();
								cancelButton.disable();
							}
						}
					});
				    loadRightTable.setCriteria(crit1);
			}
		});
	    
		main.setWidth100();
        main.setHeight100();
        main.addMember(toolStrip);
        main.addMember(stack);
        stack.addMember(section);
        stack.addMember(section2);
		return main;
	}
	protected IButton addMaxBtn2(final Layout stack, final String percent) {
		
		final IButton maxBtn = new IButton();
		if(!rmax) {
			
			maxBtn.setIcon(StaticRef.ICON_TORIGHT);
			maxBtn.setPrompt(StaticRef.TO_MAX);
		}
		else {
			maxBtn.setIcon(StaticRef.ICON_TOLEFT);
			maxBtn.setPrompt(StaticRef.TO_NORMAL);
		}
		maxBtn.setTitle("");
		maxBtn.setWidth(24);
        maxBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				JavaScriptObject jsobject = section.getSection(0).getAttributeAsJavaScriptObject("controls");
				Canvas[] canvas = null;
				DynamicForm pageForm = null;
				if(jsobject != null) {
					canvas = Canvas.convertToCanvasArray(jsobject);
				}
				else {
					canvas = new Canvas[1];
				}
				for(int i = 0; i < canvas.length; i++) {
					if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
						pageForm = (DynamicForm)canvas[i];
						break;
					}
				}
				if(!rmax) {
					section.setWidth100();	
					
					maxBtn.setIcon(StaticRef.ICON_TOLEFT);
					maxBtn.setPrompt(StaticRef.TO_NORMAL);
					stack.getMember(1).setVisible(false);
					if(pageForm != null) {
						pageForm.setVisible(true);
					}
				}
				else {
					section.setWidth(percent);
					maxBtn.setIcon(StaticRef.ICON_TORIGHT);
					maxBtn.setPrompt(StaticRef.TO_MAX);
					stack.getMember(1).setVisible(true);
					if(pageForm != null) {
						pageForm.setVisible(false);
					}
				}
				rmax = !rmax;
				jsobject = null;
				canvas = null;
			}      	
        });   
        return maxBtn;
    }
	protected IButton addMaxBtn(final Layout stack, final String percent) {
		
		final IButton maxBtn = new IButton();
		if(!rmax) {
			maxBtn.setIcon(StaticRef.ICON_TOLEFT);
			maxBtn.setPrompt(StaticRef.TO_MAX);
		}
		else {
			maxBtn.setIcon(StaticRef.ICON_TORIGHT);
			maxBtn.setPrompt(StaticRef.TO_NORMAL);
		}
		maxBtn.setTitle("");
		maxBtn.setWidth(24);
	    maxBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				JavaScriptObject jsobject = section2.getSection(0).getAttributeAsJavaScriptObject("controls");
				Canvas[] canvas = null;
				DynamicForm pageForm = null;
				if(jsobject != null) {
					canvas = Canvas.convertToCanvasArray(jsobject);
				}
				else {
					canvas = new Canvas[1];
				}
				for(int i = 0; i < canvas.length; i++) {
					if(canvas[i] != null && canvas[i].getClass().equals(DynamicForm.class)) {
						pageForm = (DynamicForm)canvas[i];
						break;
					}
				}
				if(!rmax) {
					section2.setWidth100();	
					maxBtn.setIcon(StaticRef.ICON_TORIGHT);
					maxBtn.setPrompt(StaticRef.TO_NORMAL);
					stack.getMember(0).setVisible(false);
					if(pageForm != null) {
						pageForm.setVisible(true);
					}
				}
				else {
					section2.setWidth(percent);
					maxBtn.setIcon(StaticRef.ICON_TOLEFT);
					maxBtn.setPrompt(StaticRef.TO_MAX);
					stack.getMember(0).setVisible(true);
					if(pageForm != null) {
						pageForm.setVisible(false);
					}
				}
				rmax = !rmax;
				jsobject = null;
				canvas = null;
			}      	
        });   
        return maxBtn;
	}


	private void createLeftFields(SGTable loadLeftTable) {
		/**
		 * 正在装货
		 * DOCK 状态 车牌号 车型 发货去向 数量[箱] 发货员  开始装货时间 预计完成时间 
		 */
		ListGridField LOAD_WHSE = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),120);//
		ListGridField DOCK = new ListGridField("DOCK_NAME","DOCK",70);
		ListGridField LOAD_STATUS = new ListGridField("LOAD_STATUS",Util.TI18N.STATUS());
		LOAD_STATUS.setHidden(true);
		ListGridField LOAD_STATUS_NAME = new ListGridField("LOAD_STATUS_NAME",Util.TI18N.STATUS(),70);//
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);//
		ListGridField VEHL_TYP = new ListGridField("VEHICLE_TYP_NAME",Util.TI18N.VEHL_TYP(),70);//
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME_LOAD(),120);//
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY_NUM(),70);
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		QNTY.setShowGroupSummary(true); 
		QNTY.setAlign(Alignment.RIGHT);
		QNTY.addEditorExitHandler(new ChangedTotalQntyAction(loadLeftTable, "QNTY", Util.TI18N.TOT_QNTY()));

		
		ListGridField CONSIGNER = new ListGridField("CONSIGNER_NAME",Util.TI18N.CONSIGNER(),100);//
		ListGridField START_LOAD_TIME = new ListGridField("START_LOAD_TIME",Util.TI18N.START_LOAD_TIME(),120);//
		ListGridField END_LOAD_TIME = new ListGridField("END_LOAD_TIME",Util.TI18N.END_LOAD_TIME_LOAD(),120);//
//		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),99);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),109);
		LOAD_NO.setHidden(true);
		ListGridField ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),110);
		ListGridField DRIVER = new ListGridField("MOBILE","司机电话",110);
//		Util.initDateTime(loadLeftTable,START_LOAD_TIME);
//		Util.initDateTime(loadLeftTable,END_LOAD_TIME);
		
		
		
		loadLeftTable.setFields(LOAD_WHSE,DOCK,LOAD_STATUS,LOAD_STATUS_NAME,PLATE_NO,VEHL_TYP,UNLOAD_AREA_NAME,QNTY,CONSIGNER,START_LOAD_TIME,END_LOAD_TIME,LOAD_NO,ODR_NO,DRIVER);
		loadLeftTable.setCanEdit(false);
		loadLeftTable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadRightTable.deselectAllRecords();
				staLoadButton.disable();
			}
		});
		
		loadLeftTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				finish_record = event.getRecord();
				if("30".equals(finish_record.getAttribute("LOAD_STATUS"))){
					cancelButton.disable();
					finLoadButton.disable();
				}else {
					if(isPrivilege(TrsPrivRef.LOADJOB_P0_03)){
						cancelButton.enable();
						finLoadButton.enable();
					}
				}
			}
		});
		
	}

	private void createRightFields(SGTable loadRightTable) {
		
		/**
		 * 等候车辆 
		 *  车牌号 提货仓库 排队号 到库时间 发货去向 数量[箱]
		 */
		
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),90);//
		PLATE_NO.setTitle(ColorUtil.getRedTitle(Util.TI18N.PLATE_NO()));
		ListGridField LOAD_WHSE = new ListGridField("LOAD_WHSE",Util.TI18N.LOAD_WHSE(),100);
		LOAD_WHSE.setHidden(true);
		ListGridField LOAD_WHSE_NAME = new ListGridField("LOAD_WHSE_NAME",Util.TI18N.LOAD_WHSE(),170);//
		ListGridField QUEUE = new ListGridField("QUEUE_SEQ",Util.TI18N.QUEUE(),75);
		ListGridField ARRIVE_WHSE_TIME = new ListGridField("ARRIVE_WHSE_TIME",Util.TI18N.ARRIVE_WHSE_TIME(),120);
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_AREA_NAME_LOAD(),220);//
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY_NUM(),60);//
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),110);//
//		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),70);
		ListGridField DRIVER = new ListGridField("MOBILE","司机电话",117);
//		SHPM_NO.setHidden(true);
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		QNTY.setRecordSummaryFunction(RecordSummaryFunctionType.MULTIPLIER);   
		QNTY.setSummaryFunction(SummaryFunctionType.SUM);
		QNTY.setShowGroupSummary(true); 
		QNTY.setAlign(Alignment.RIGHT);
		QNTY.addEditorExitHandler(new ChangedTotalQntyAction(loadRightTable, "QNTY", Util.TI18N.TOT_QNTY()));
		
		Util.initDateTime(loadRightTable,ARRIVE_WHSE_TIME);
		Util.initDateTime(loadRightTable,UNLOAD_AREA_NAME);
		
		loadRightTable.setFields(PLATE_NO,LOAD_WHSE,LOAD_WHSE_NAME,QUEUE,ARRIVE_WHSE_TIME,UNLOAD_AREA_NAME,QNTY,CUSTOM_ODR_NO,DRIVER);
		loadRightTable.setCanEdit(false);
		loadRightTable.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadLeftTable.deselectAllRecords();
				if(isPrivilege(TrsPrivRef.LOADJOB_P0_02)){
					staLoadButton.enable();
				}
				
				finLoadButton.disable();
				cancelButton.disable();
				
			}
		});
		
		loadRightTable.addSelectionChangedHandler(new SelectionChangedHandler() {
			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				if(event.getRecord() != null){
				   record = event.getRecord();
				}
			
			}
		});
		
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}
	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		/*if (leftWin != null) {
			leftWin.destroy();
		}
		if (rightWin != null) {
			rightWin.destroy();
		}*/
	}
    
	private LoadJobView getThis(){
		return this;
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		
//		refeshButton = createBtn(StaticRef.REFRESH_BTN,TrsPrivRef.LOADJOB_P0_01);
		refeshButton=createUDFBtn(Util.BI18N.REFRESH(),StaticRef.ICON_CANCEL ,TrsPrivRef.LOADJOB_P0_01);//
		refeshButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				loadLeftTable.invalidateCache();
				 Criteria crit2 = new Criteria();
				    crit2.addCriteria("OP_FLAG","M");
				    crit2.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
				    crit2.addCriteria("LOAD_STATUS",StaticRef.TRANS_LOADING);
				    crit2.addCriteria("ORDER_BY", "DOCK_NAME");//正在装货按照DOCK排序
				    
				    loadLeftTable.fetchData(crit2,new DSCallback() {
						
						@Override
						public void execute(DSResponse response, Object rawData, DSRequest request) {
							  	loadRightTable.invalidateCache();
								Criteria crit1 = new Criteria();
							    crit1.addCriteria("OP_FLAG","M");
							    crit1.addCriteria("LOAD_STATUS",StaticRef.TRANS_EXPECT);
							    crit1.addCriteria("ORDER_BY", "QUEUE_SEQ");//排队装货按照排队号排序
							    crit1.addCriteria("USER_ID",LoginCache.getLoginUser().getUSER_ID());
							    loadRightTable.fetchData(crit1,new DSCallback() {
									
									@Override
									public void execute(DSResponse response, Object rawData, DSRequest request) {
										if(loadRightTable.getRecords().length > 0){
											loadRightTable.selectRecord(loadRightTable.getRecord(0));
											
											if(isPrivilege(TrsPrivRef.LOADJOB_P0_02)){
												staLoadButton.enable();
											}
											finLoadButton.disable();
											cancelButton.disable();
										}
									}
								});
							    
							
						}
					});
			}
		});
		
		staLoadButton=createUDFBtn(Util.BI18N.STALOAD(),StaticRef.ICON_CANCEL ,TrsPrivRef.LOADJOB_P0_02);//
		staLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] record = loadRightTable.getSelection();
				Record item= record[0];
				StringBuffer sf = new StringBuffer();
				for (int i = 1; i < record.length; i++) {
					if(!item.getAttribute("PLATE_NO").equals(record[i].getAttribute("PLATE_NO"))){
						sf.append(record[i].getAttribute("PLATE_NO"));
						
					}
				}
				if(sf.length() > 0 ){
					MSGUtil.sayWarning("请选择同一车货进入DOCK装货！");
					return;
				}
				new LoadLeftWin(loadJobDS, null,getThis(),loadLeftTable,loadRightTable).getViewPanel();
			}
        	
        });
		
		finLoadButton=createUDFBtn(Util.BI18N.FINLOAD(),StaticRef.ICON_CANCEL ,TrsPrivRef.LOADJOB_P0_03);//
		finLoadButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				ListGridRecord[] record = loadLeftTable.getSelection();
				Record item= record[0];
				StringBuffer sf = new StringBuffer();
				for (int i = 1; i < record.length; i++) {
					if(!item.getAttribute("PLATE_NO").equals(record[i].getAttribute("PLATE_NO"))){
						sf.append(record[i].getAttribute("PLATE_NO"));
						
					}
				}
				if(sf.length() > 0 ){
					MSGUtil.sayWarning("请选择同一车进行【完成装货】！");
					return;
				}
				new LoadRightWin(loadJobDS, null, section2.getSection(0),getThis(),loadLeftTable,loadRightTable).getViewPanel();
				//new CopyOfLoadRightWin(loadJobDS, null, section2.getSection(0),getThis()).getViewPanel();
			}
        	
        });
		cancelButton = createBtn(StaticRef.CANCEL_LOAD_BTN);
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				new LoadCancelAction(getThis()).onClick(event);
			}
		});
		staLoadButton.disable();
		finLoadButton.disable();
		if(loadLeftTable.getRecords().length >=1){
			
			if(isPrivilege(TrsPrivRef.LOADJOB_P0_03)){
				cancelButton.enable();
			}
			
		}else {
			cancelButton.disable();
		}
		toolStrip.setMembersMargin(3);
		toolStrip.setMembers(refeshButton,staLoadButton,finLoadButton,cancelButton);

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		LoadJobView view = new LoadJobView();
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
