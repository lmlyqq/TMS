package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.R_JOB_TRANS_TRACK_DS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.RadioGroupItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 报表管理-->作业报表-->托运单全程运输跟踪表
 * @author wangjun
 *
 */
@ClassForNameAble
public class R_JOB_TRANS_TRACK extends SGForm implements PanelFactory {
	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	private RadioGroupItem radioGroupItem;
	private RadioGroupItem radioGroupItem2;
	private RadioGroupItem radioGroupItem3;
	private RadioGroupItem radioGroupItem4;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private Object cur_value;
	private Object cur_value2;
	private Object cur_value3;
	private Object cur_value4;
	private SGDateTime LOAD_TIME_FROM;
	private SGDateTime LOAD_TIME_TO;
	private SGDateTime UNLOAD_TIME_FROM;
	private SGDateTime UNLOAD_TIME_TO;
	private SGDateTime POD_TIME_FROM;
	private SGDateTime POD_TIME_TO;
	
	/*public R_JOB_TRANS_TRACK(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_JOB_TRANS_TRACK_DS.getInstance("R_JOB_TRANS_TRACK","R_JOB_TRANS_TRACK");
		
		//放置按钮
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//设置详细信息布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		table= new SGTable(ds, "100%", "100%");
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createFields(table);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		section = new SectionStack();
		section.setWidth("100%");
		section.setHeight("100%");
		
		final SectionStackSection listItem = new SectionStackSection("列表信息");//
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
		
	    main.setWidth100();
        main.setHeight100();
        main.addMember(toolStrip);
		main.addMember(section);
        
		return main;
	}

	private void createFields(SGTable table) {
		
		/**
		 * 客户单号	 作业单编号	状态		运输单位	订单数量	本单量	发运数量	到货数量   发货方	  收货区域 收货方	供应商	车牌号	货损货差	
		 * 当前位置	运输异常	发运时间	预达时间	到货时间	到货延迟天数	到货延迟原因	预计回单时间	回单时间	回单延迟天数	回单延迟原因	订单类型	
		 * 运输服务	执行机构	应付运费	备注
		 * 
		 * 
		 */
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),120);
		ListGridField STATUS = new ListGridField("STATUS_NAME",Util.TI18N.STATUS(),60);
		ListGridField EA = new ListGridField("TOT_QNTY_EACH",Util.TI18N.R_EA(),60);
		ListGridField TRANS_UOM = new ListGridField("TRANS_UOM",Util.TI18N.TRANS_UOM_FLAG(),60);
		ListGridField ODR_QNTY = new ListGridField("ODR_QNTY", Util.TI18N.ORD_TOT_QNTY(),60);//订单数量  
		ListGridField QNTY = new ListGridField("TOT_QNTY", Util.TI18N.SHPM_QNTY(),60);//本单量
		ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.R_LD_QNTY(),60);//发运数量
		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY","收货EA数",60);//到货数量
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),140);
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME(),80);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),140);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),80);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);
		ListGridField LOSDAM_FLAG = new ListGridField("LOSDAM_FLAG",Util.TI18N.LOSDAM_FLAG(),80);
		ListGridField CURRENT_LOC = new ListGridField("CURRENT_LOC",Util.TI18N.CURRENT_LOC(),80);
		ListGridField ABNOMAL_STAT = new ListGridField("ABNOMAL_STAT",Util.TI18N.ABNOMAL_STAT(),100);
		
		ListGridField LOAD_TIME = new ListGridField("END_LOAD_TIME",Util.TI18N.LOAD_TIME(),120);//发运时间
		ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME,",Util.TI18N.PRE_UNLOAD_TIME(),120);//预达时间
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME",Util.TI18N.UNLOAD_TIME(),120);
		
		ListGridField UNLOAD_DELAY_DAYS = new ListGridField("UNLOAD_DELAY_DAYS",Util.TI18N.UNLOAD_DELAY_DAYS(),120);
		ListGridField UNLOAD_DELAY_REASON = new ListGridField("UNLOAD_DELAY_REASON",Util.TI18N.UNLOAD_DELAY_REASON(),120);
		ListGridField PRE_POD_TIME = new ListGridField("PRE_POD_TIME",Util.TI18N.PRE_POD_TIME(),120);
		ListGridField POD_TIME = new ListGridField("POD_TIME",Util.TI18N.POD_TIME(),120);
		ListGridField POD_DELAY_DAYS = new ListGridField("POD_DELAY_DAYS",Util.TI18N.POD_DELAY_DAYS(),120);
		ListGridField POD_DELAY_REASON = new ListGridField("POD_DELAY_REASON",Util.TI18N.POD_DELAY_REASON(),120);
		ListGridField ODR_TYP = new ListGridField("ODR_TYP_NAME",Util.TI18N.ODR_TYP(),70);
		ListGridField TRANS_SRVC_ID = new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),70);
		ListGridField EXEC_ORG_ID = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),80);
		//ListGridField NEED_FEE = new ListGridField("NEED_FEE",Util.TI18N.NEED_FEE(),70);
		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),120);
		
		Util.initFloatListField(EA, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
		
		table.setFields(CUSTOM_ODR_NO,SHPM_NO,STATUS,EA,TRANS_UOM,ODR_QNTY,QNTY,LD_QNTY,UNLD_QNTY
				,LOAD_NAME,UNLOAD_AREA_NAME,UNLOAD_NAME,SUPLR_NAME,PLATE_NO,
				LOSDAM_FLAG,CURRENT_LOC,ABNOMAL_STAT,LOAD_TIME,PRE_UNLOAD_TIME,UNLOAD_TIME,UNLOAD_DELAY_DAYS
				,UNLOAD_DELAY_REASON,PRE_POD_TIME,POD_TIME,POD_DELAY_DAYS,POD_DELAY_REASON,ODR_TYP
				,TRANS_SRVC_ID,EXEC_ORG_ID,NOTES);
		
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {

		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);//查询
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
					searchWin.setWidth(585);
					searchWin.setHeight(360);
				}else{
					searchWin.show();
				}
				
			}
		});
		
		 //导出按钮
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
        
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton,expButton);
		
	}

	protected DynamicForm createSerchForm(SGPanel form) {
		
		/**
		 * 	客户        客户单号	订单时间 从   到       发运时间 从   到      到货时间 从            到    
		 *  回单时间 从  到      执行机构   包含下级机构    订单类型	供应商					
		 * 
		 */
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		CUSTOMER.setWidth(127);
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		ODR_TIME_TO =new  SGDateTime("ODR_TIME_TO", "到");
		
		LOAD_TIME_FROM =new  SGDateTime("END_LOAD_TIME_FROM", Util.TI18N.DEPART_TM_FROM());
		LOAD_TIME_TO =new  SGDateTime("END_LOAD_TIME_TO","到");
		
		UNLOAD_TIME_FROM =new  SGDateTime("UNLOAD_TIME_FROM", Util.TI18N.UNLOAD_TIME_FROM());
		UNLOAD_TIME_TO =new  SGDateTime("UNLOAD_TIME_TO","到");
		
		POD_TIME_FROM =new  SGDateTime("POD_TIME_FROM", Util.TI18N.POD_TIME_FROM());
		POD_TIME_TO =new  SGDateTime("POD_TIME_TO","到");
		
		
		ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM());
		ODR_TIME_TO = new  SGDateTime("ODR_TIME_TO", "到");
		String PreYesDate=Util.getYesPreDay();
		ODR_TIME_FROM.setDefaultValue(PreYesDate);
		ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem = new RadioGroupItem();  
		radioGroupItem.setShowTitle(false); 
		radioGroupItem.setVertical(false);
		radioGroupItem.setValueMap("昨天", "今天","本周", "本月");  
	    radioGroupItem.setDefaultValue("昨天");
	    radioGroupItem.setColSpan(4);
	  
	    radioGroupItem.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value=event.getValue();
				
				String Predate=Util.getPreDay();
				String PreYesDate=Util.getYesPreDay();
				String PreMonDate=Util.getMonthPreDay();
				String PreWeekDate=Util.getWeekPreDay();
				
				if(cur_value.equals("今天")){
					ODR_TIME_FROM.setValue(Predate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("昨天")){
					ODR_TIME_FROM.setValue(PreYesDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value.equals("本月")){
					ODR_TIME_FROM.setValue(PreMonDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("本周")){
					ODR_TIME_FROM.setValue(PreWeekDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    
	    
		LOAD_TIME_FROM = new  SGDateTime("LOAD_TIME_FROM", Util.TI18N.DEPART_TM_FROM(),true);
		LOAD_TIME_TO = new  SGDateTime("LOAD_TIME_TO","到");
		LOAD_TIME_FROM.setDefaultValue(PreYesDate);
		LOAD_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem2 = new RadioGroupItem();  
		radioGroupItem2.setShowTitle(false); 
		radioGroupItem2.setVertical(false);
		radioGroupItem2.setValueMap("昨天", "今天","本周", "本月");  
	    radioGroupItem2.setDefaultValue("昨天");
	    radioGroupItem2.setColSpan(4);
	  
	    radioGroupItem2.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value2=event.getValue();
				
				String Predate=Util.getPreDay();
				String PreYesDate=Util.getYesPreDay();
				String PreMonDate=Util.getMonthPreDay();
				String PreWeekDate=Util.getWeekPreDay();
				
				if(cur_value2.equals("今天")){
					LOAD_TIME_FROM.setValue(Predate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("昨天")){
			    	LOAD_TIME_FROM.setValue(PreYesDate);
			    	ODR_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value2.equals("本月")){
			    	LOAD_TIME_FROM.setValue(PreMonDate);
			    	ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("本周")){
			    	LOAD_TIME_FROM.setValue(PreWeekDate);
			    	ODR_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    
	    UNLOAD_TIME_FROM =new  SGDateTime("UNLOAD_TIME_FROM", Util.TI18N.UNLOAD_TIME_FROM());
	    UNLOAD_TIME_TO = new  SGDateTime("UNLOAD_TIME_TO", "到");
	    UNLOAD_TIME_FROM.setDefaultValue(PreYesDate);
	    UNLOAD_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem3 = new RadioGroupItem();  
		radioGroupItem3.setShowTitle(false); 
		radioGroupItem3.setVertical(false);
		radioGroupItem3.setValueMap("昨天", "今天","本周", "本月");  
	    radioGroupItem3.setDefaultValue("昨天");
	    radioGroupItem3.setColSpan(4);
	  
	    radioGroupItem3.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value3=event.getValue();
				
				String Predate=Util.getPreDay();
				String PreYesDate=Util.getYesPreDay();
				String PreMonDate=Util.getMonthPreDay();
				String PreWeekDate=Util.getWeekPreDay();
				
				if(cur_value3.equals("今天")){
					UNLOAD_TIME_FROM.setValue(Predate);
					UNLOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value3.equals("昨天")){
			    	UNLOAD_TIME_FROM.setValue(PreYesDate);
					UNLOAD_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value3.equals("本月")){
			    	UNLOAD_TIME_FROM.setValue(PreMonDate);
					UNLOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value3.equals("本周")){
			    	UNLOAD_TIME_FROM.setValue(PreWeekDate);
					UNLOAD_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    
	    
	    POD_TIME_FROM = new  SGDateTime("POD_TIME_FROM", Util.TI18N.POD_TIME_FROM(),true);
	    POD_TIME_TO = new  SGDateTime("POD_TIME_TO","到");
		POD_TIME_FROM.setDefaultValue(PreYesDate);
		POD_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem4 = new RadioGroupItem();  
		radioGroupItem4.setShowTitle(false); 
		radioGroupItem4.setVertical(false);
		radioGroupItem4.setValueMap("昨天", "今天","本周", "本月");  
	    radioGroupItem4.setDefaultValue("昨天");
	    radioGroupItem4.setColSpan(4);
	  
	    radioGroupItem4.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value4=event.getValue();
				
				String Predate=Util.getPreDay();
				String PreYesDate=Util.getYesPreDay();
				String PreMonDate=Util.getMonthPreDay();
				String PreWeekDate=Util.getWeekPreDay();
				
				if(cur_value4.equals("今天")){
					POD_TIME_FROM.setValue(Predate);
					POD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value4.equals("昨天")){
			    	POD_TIME_FROM.setValue(PreYesDate);
			    	POD_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value4.equals("本月")){
			    	POD_TIME_FROM.setValue(PreMonDate);
			    	POD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value4.equals("本周")){
			    	POD_TIME_FROM.setValue(PreWeekDate);
			    	POD_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    
	    
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setWidth(127);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(false);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		SGCheck C_RDC_FLAG = new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());
		
		SGCombo ORD_TYP=new SGCombo("ODR_TYP_NAME",Util.TI18N.ODR_TYP());
		Util.initCodesComboValue(ORD_TYP, "ORD_TYP");
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
//		Util.initCodesComboValue(SUPLR_NAME, "SUPLR_TYP");
		CUSTOM_ODR_NO.setWidth(127);
		ODR_TIME_FROM.setWidth(127);
		ODR_TIME_TO.setWidth(127);
		LOAD_TIME_FROM.setWidth(127);
		LOAD_TIME_TO.setWidth(127);
		SUPLR_ID.setWidth(127);
		ORD_TYP.setWidth(127);
		form.setItems(CUSTOMER,CUSTOM_ODR_NO,EXEC_ORG_ID_NAME,ORD_TYP,ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,
				LOAD_TIME_FROM,LOAD_TIME_TO,radioGroupItem2,UNLOAD_TIME_FROM,UNLOAD_TIME_TO,radioGroupItem3,
				POD_TIME_FROM,POD_TIME_TO,radioGroupItem4,SUPLR_ID,C_ORG_FLAG,C_RDC_FLAG);
		
		return form;
		
	}

	@Override
	public void createForm(DynamicForm form) {

	}

	@Override
	public void initVerify() {

	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_JOB_TRANS_TRACK view = new R_JOB_TRANS_TRACK();
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
