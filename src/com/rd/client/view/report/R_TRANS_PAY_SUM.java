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
import com.rd.client.ds.report.R_TRANS_PAY_SUM_DS;
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
 * 
 * 报表管理-->财务报表-->运费支付汇总
 *
 */
@ClassForNameAble
public class R_TRANS_PAY_SUM extends SGForm implements PanelFactory {

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
	
	/*public R_TRANS_PAY_SUM(String id) {
		super(id);
	}*/
   
	@Override
	public Canvas getViewPanel() {
	
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_TRANS_PAY_SUM_DS.getInstance("R_TRANS_PAY_SUM","R_TRANS_PAY_SUM");
		
		//放置按钮
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//设置详细信息布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		table= new SGTable(ds, "100%", "100%");
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
	    
        main.addMember(toolStrip);
		main.addMember(section);
		
		return main;
	}

	private void createFields(SGTable table) {
		/**
		 * 供应商	订单数	车次	数量[箱]	瓶数	体积	毛重	应付金额	实付金额	未付金额
		 */
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),90);
		ListGridField ORDER_NUM = new ListGridField("ORDER_NUM",Util.TI18N.ODR_QNTY(),70);
		ListGridField LOAD_NUM= new ListGridField("LOAD_NUM",Util.TI18N.LOAD_NUM(),50);
		
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY_NUM(),90);
		ListGridField QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.QNTY_SUM(),90);
		ListGridField VOL = new ListGridField("VOL",Util.TI18N.PACK_VOLUME(),90);
		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.GROSSWEIGHT(),90);
		
		Util.initFloatListField(ORDER_NUM, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(QNTY_EACH, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		
		ListGridField DUE_FEE = new ListGridField("DUE_FEE",Util.TI18N.DUE_FEE_SUM(),90);
		ListGridField ACCOUNT_FEE = new ListGridField("ACCOUNT_FEE",Util.TI18N.ACCOUNT_FEE(),90);
		ListGridField UNACC_FEE = new ListGridField("UNACC_FEE",Util.TI18N.UNACC_FEE(),90);
		
		
		table.setFields(SUPLR_NAME,ORDER_NUM,LOAD_NUM,QNTY,QNTY_EACH,VOL,G_WGT,DUE_FEE,ACCOUNT_FEE,UNACC_FEE);
		
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
	     *订单时间 从  到	
	     *
	     *客户： 取默认值，如无默认值，则为空，即全部客户							
                                              订单时间 从  到 默认为空 RADIO:本月、本季度、半年度、全年							
                                              发运时间 从  到  默认从本月1日0点到当前时间(本月) RADIO:本月、本季度、半年度、全年							
                                              订单类型(复选) 下拉列表 默认勾选发货订单 (发货订单在数据字典中为默认值)							
                                              执行机构：默认为当前登录机构							
                                              包含下级机构：默认打勾							
                                              包含促销品：默认不打勾							
													
		 */
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		ODR_TIME_FROM = new SGDateTime("ODR_TIME_FROM",Util.TI18N.ODR_TIME_FROM(),true);
		ODR_TIME_FROM.setColSpan(2);
		ODR_TIME_FROM.setTitleOrientation(TitleOrientation.TOP);
		
		ODR_TIME_TO = new SGDateTime("ODR_TIME_TO","到");
		ODR_TIME_TO.setColSpan(2);
		ODR_TIME_TO.setTitleOrientation(TitleOrientation.TOP);
		
		String getMonthPreDay=Util.getMonthPreDay();
		ODR_TIME_FROM.setDefaultValue(getMonthPreDay);
		ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem = new RadioGroupItem();  
		radioGroupItem.setShowTitle(false); 
		radioGroupItem.setVertical(false);
		radioGroupItem.setValueMap("本月", "本季度","半年度", "全年");  
	    radioGroupItem.setDefaultValue("本月");
	    radioGroupItem.setColSpan(4);
	  
	    radioGroupItem.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value=event.getValue();
				
				String PreMonDate=Util.getMonthPreDay();
				
				String YearPreDay=Util.getAllYearPreDay();
				
				String BanYearPreDay=Util.getHalfYearPreDay();
				
				String jiduPreDay=Util.getQuarterPreDay();
				
				if(cur_value.equals("本月")){
					ODR_TIME_FROM.setValue(PreMonDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("本季度")){
					ODR_TIME_FROM.setValue(jiduPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value.equals("半年度")){
					ODR_TIME_FROM.setValue(BanYearPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("全年")){
					ODR_TIME_FROM.setValue(YearPreDay);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    
		LOAD_TIME_FROM = new  SGDateTime("LOAD_TIME_FROM", Util.TI18N.DEPART_TM_FROM(),true);
		LOAD_TIME_TO = new  SGDateTime("LOAD_TIME_TO","到");
		LOAD_TIME_FROM.setDefaultValue(getMonthPreDay);
		LOAD_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem2 = new RadioGroupItem();  
		radioGroupItem2.setShowTitle(false); 
		radioGroupItem2.setVertical(false);
		radioGroupItem2.setValueMap("本月", "本季度","半年度", "全年"); 
		radioGroupItem2.setDefaultValue("本月");
	    radioGroupItem2.setColSpan(4);
	  
	    radioGroupItem2.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				cur_value2=event.getValue();
				
				String PreMonDate=Util.getMonthPreDay();
				
				String YearPreDay=Util.getAllYearPreDay();
				
				String BanYearPreDay=Util.getHalfYearPreDay();
				
				String jiduPreDay=Util.getQuarterPreDay();
				
				if(cur_value2.equals("本月")){
					LOAD_TIME_FROM.setValue(PreMonDate);
					LOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("本季度")){
			    	LOAD_TIME_FROM.setValue(jiduPreDay);
			    	LOAD_TIME_TO.setValue(Util.getCurTime());
			    } else if(cur_value2.equals("半年度")){
			    	LOAD_TIME_FROM.setValue(BanYearPreDay);
					LOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("全年")){
			    	LOAD_TIME_FROM.setValue(YearPreDay);
					LOAD_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
		
		SGCombo ORD_TYP=new SGCombo("ORD_TYP",Util.TI18N.ODR_TYP());
		Util.initCodesComboValue(ORD_TYP, "ORD_TYP");
	
		TextItem  EXEC_ORG_ID= new TextItem("EXEC_ORG_ID");
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

		
		form.setItems(ORD_TYP,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,
				ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,
				LOAD_TIME_FROM,LOAD_TIME_TO,radioGroupItem2
				);
		
		return form;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
		}
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_TRANS_PAY_SUM view = new R_TRANS_PAY_SUM();
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
