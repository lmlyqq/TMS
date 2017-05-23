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
import com.rd.client.ds.report.R_JOB_OPER_HEADER_DS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
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
 * 报表管理-->作业报表-->营运日/周/月汇总报表
 * @author wangjun
 *
 */
@ClassForNameAble
public class R_JOB_OPER_HEADER extends SGForm implements PanelFactory {
	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	private RadioGroupItem radioGroupItem;
	private RadioGroupItem radioGroupItem2;
	private SGDateTime ODR_TIME_FROM;
	private SGDateTime ODR_TIME_TO;
	private Object cur_value;
	private Object cur_value2;
	private SGDateTime LOAD_TIME_FROM;
	private SGDateTime LOAD_TIME_TO;
	
	/*public R_JOB_OPER_HEADER(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_JOB_OPER_HEADER_DS.getInstance("R_JOB_OPER_HEADER","R_JOB_OPER_HEADER");
		
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
		 * 客户单号   状态	     订单时间	发运时间	到货时间	收货区域  收货方	 调度状态	发运状态	到货状态	
		 * 运输单位	 订单数量  发运数量	到货数量	体积	毛重	供应商	     执行机构	订单类型	运输服务	预估运费	备注
		 */	
		ListGridField EXEC_ORG_ID_NAME = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID_NAME(),90);
		ListGridField BIZ_TYP_NAME = new ListGridField("BIZ_TYP_NAME",Util.TI18N.BIZ_TYP(),90);
		//ListGridField TRANS_SRVC_ID_NAME = new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),90);	
		ListGridField LOAD_AREA_NAME2 = new ListGridField("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME(),90);	
		ListGridField UNLOAD_AREA_NAME2 = new ListGridField("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME(),90);	
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY", Util.TI18N.LD_QNTY(),80);//订单数量  
		ListGridField TOT_VOL = new ListGridField("TOT_VOL",Util.TI18N.LD_VOL(),80);
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W",Util.TI18N.LD_GWGT(),80);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),90);
		
		table.setFields(EXEC_ORG_ID_NAME,BIZ_TYP_NAME,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,TOT_QNTY,TOT_VOL,TOT_GROSS_W,SUPLR_NAME);
		
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
					searchWin = new SearchWin( ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
					searchWin.setHeight(320);
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
		 * 	客户        客户单号	订单时间 从   到       发运时间 从   到     执行机构   订单类型	供应商	 未完成订单	销售未提    确认未配车    配车未发货	发货未到				
		 * 
		 */
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		//Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		//CUSTOMER.setWidth(127);
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		ODR_TIME_FROM =new  SGDateTime("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true);
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
	    
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setWidth(127);
		SGCombo SUPLR_ID =new SGCombo("SUPLR_NAME", Util.TI18N.SUPLR_NAME());//供应商
		Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME"," where ID in (select SUPLR_ID from BAS_SUPPLIER_ORG where " +
				"ORG_ID='"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"') and ENABLE_FLAG='Y'");
		
		SGCombo BIZ_TYP =new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP(),true);//供应商
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		ComboBoxItem LOAD_AREA_NAME=new ComboBoxItem("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME());//起点区域
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		TextItem LOAD_AREA_ID=new TextItem("LOAD_AREA_ID2", Util.TI18N.START_ARAE());
		Util.initArea(LOAD_AREA_NAME, LOAD_AREA_ID);
		
		ComboBoxItem UNLOAD_AREA_NAME=new ComboBoxItem("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME());//
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		TextItem UNLOAD_AREA_ID=new TextItem("UNLOAD_AREA_ID2", Util.TI18N.UNLOAD_AREA_ID());
		Util.initArea(UNLOAD_AREA_NAME, UNLOAD_AREA_ID);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		form.setItems(CUSTOM_ODR_NO,EXEC_ORG_ID_NAME,SUPLR_ID,
				LOAD_TIME_FROM,LOAD_TIME_TO,radioGroupItem2,BIZ_TYP,LOAD_AREA_NAME,UNLOAD_AREA_NAME,EXEC_ORG_ID,C_ORG_FLAG);
		
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
		R_JOB_OPER_HEADER view = new R_JOB_OPER_HEADER();
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
