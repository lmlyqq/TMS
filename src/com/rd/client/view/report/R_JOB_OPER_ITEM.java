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
import com.rd.client.ds.report.R_JOB_OPER_ITEM_DS;
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
 * 报表管理-->作业报表-->营运日/周/月明细报表 
 * @author wangjun
 *
 */
@ClassForNameAble
public class R_JOB_OPER_ITEM extends SGForm implements PanelFactory{

	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
//	private RadioGroupItem radioGroupItem;
	private RadioGroupItem radioGroupItem2;
//	private SGDateTime ODR_TIME_FROM;
//	private SGDateTime ODR_TIME_TO;
//	private Object cur_value;
	private Object cur_value2;
	private SGDateTime LOAD_TIME_FROM;
	private SGDateTime LOAD_TIME_TO;
	
	/*public R_JOB_OPER_ITEM(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = R_JOB_OPER_ITEM_DS.getInstance("R_JOB_OPER_ITEM","R_JOB_OPER_ITEM");
		
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
		 * 货品规格	    运输单位	 订单数量  发运数量	到货数量	体积	毛重	供应商	     执行机构	订单类型	
		 */
		/**
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),100);
		ListGridField STATUS = new ListGridField("STATUS_NAME",Util.TI18N.ORDER_STATE2(),60);
		ListGridField ASSIGN_TIME = new ListGridField("ASSIGN_TIME",Util.TI18N.ASSIGN_TIME(),120);
		ListGridField LOAD_TIME = new ListGridField("LOAD_TIME",Util.TI18N.LOAD_TIME(),120);
		ListGridField UNLOAD_TIME = new ListGridField("UNLOAD_TIME",Util.TI18N.UNLOAD_TIME(),120);
		ListGridField UNLOAD_AREA_NAME = new ListGridField("UNLOAD_AREA_NAME",Util.TI18N.UNLOAD_AREA_NAME(),80);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),140);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),140);
		ListGridField UNLOAD_ADDRESS = new ListGridField("UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),140);
		ListGridField UNLOAD_CONTACT = new ListGridField("UNLOAD_CONTACT",Util.TI18N.UNLOAD_CONTACT(),100);
		ListGridField UNLOAD_TEL = new ListGridField("UNLOAD_TEL",Util.TI18N.UNLOAD_TEL(),110);
//		ListGridField PLAN_STAT = new ListGridField("PLAN_STAT_NAME",Util.TI18N.PLAN_STAT(),70);    
		ListGridField LOAD_STAT = new ListGridField("LOAD_STAT_NAME",Util.TI18N.SHPM_STSTUS(),70);
//		ListGridField UNLOAD_STAT = new ListGridField("UNLOAD_STAT_NAME",Util.TI18N.UNLOAD_STAT(),70);
		ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(),80);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),80);
		ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),70);
		ListGridField EA = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),60);
		ListGridField TRANS_UOM = new ListGridField("UOM",Util.TI18N.TRANS_UOM_FLAG(),60);
		ListGridField ODR_QNTY = new ListGridField("QNTY", Util.TI18N.ORD_TOT_QNTY(),60);//订单数量  
		ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
		ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
		
		//fanglm 2013-04-19 增加批号、专供标识字段
		
		ListGridField LOT_ID = new ListGridField("LOT_ID",Util.TI18N.LOT_ID(),60);
		ListGridField LOTATT02 = new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);
		
		
		ListGridField TOT_VOL = new ListGridField("VOL",Util.TI18N.TOT_VOL(),60);
		ListGridField TOT_GROSS_W = new ListGridField("G_WGT",Util.TI18N.TOT_GROSS_W(),60);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),80);
		ListGridField EXEC_ORG_ID = new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),80);
		
		ListGridField DRIVER = new ListGridField("DRIVER1",Util.TI18N.DRIVER1(),60);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),80);
		ListGridField MOBILE = new ListGridField("MOBILE1",Util.TI18N.MOBILE(),80);
		
		ListGridField ODR_TYP = new ListGridField("ODR_TYP_NAME",Util.TI18N.ODR_TYP(),70);
		
		Util.initFloatListField(EA, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(TOT_VOL, StaticRef.VOL_FLOAT);
		Util.initFloatListField(TOT_GROSS_W, StaticRef.GWT_FLOAT);
		*/
		
		
		
		ListGridField SHPM_NO=new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),150);
		ListGridField ODR_NO=new ListGridField("ODR_NO",Util.TI18N.ODR_NO(),150);
		ListGridField REFENENCE1=new ListGridField("REFENENCE1",Util.TI18N.REFENENCE1(),150);
		ListGridField TRANS_SRVC_ID_NAME=new ListGridField("TRANS_SRVC_ID_NAME",Util.TI18N.TRANS_SRVC_ID(),120);
		ListGridField CUSTOMER_NAME=new ListGridField("CUSTOMER_NAME",Util.TI18N.CUSTOMER_NAME(),120);
		ListGridField EXEC_ORG_ID_NAME=new ListGridField("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID(),120);
		ListGridField BIZ_TYP_NAME=new ListGridField("BIZ_TYP_NAME",Util.TI18N.BIZ_TYP(),120);
		ListGridField END_UNLOAD_ADDRESS=new ListGridField("END_UNLOAD_ADDRESS",Util.TI18N.UNLOAD_ADDRESS(),300);
		ListGridField LOAD_AREA_NAME2=new ListGridField("LOAD_AREA_NAME2",Util.TI18N.LOAD_AREA_NAME(),120);
		ListGridField UNLOAD_AREA_NAME2=new ListGridField("UNLOAD_AREA_NAME2",Util.TI18N.UNLOAD_AREA_NAME(),120);
		ListGridField SUPLR_NAME=new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),120);
		ListGridField TOT_QNTY=new ListGridField("TOT_QNTY","总数量",120);
		ListGridField TOT_VOL=new ListGridField("TOT_VOL",Util.TI18N.TOT_VOL(),120);
		ListGridField TOT_GROSS_W=new ListGridField("TOT_GROSS_W",Util.TI18N.TOT_GROSS_W(),120);
		ListGridField ODR_TIME=new ListGridField("ODR_TIME",Util.TI18N.ODR_TIME(),150);
		ListGridField DEPART_TIME=new ListGridField("DEPART_TIME",Util.TI18N.MANAGE_END_LOAD_TIME(),150);
		
		Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(TOT_VOL, StaticRef.VOL_FLOAT);
		Util.initFloatListField(TOT_GROSS_W, StaticRef.GWT_FLOAT);
		
		table.setFields(SHPM_NO,ODR_NO,REFENENCE1,TRANS_SRVC_ID_NAME,CUSTOMER_NAME,EXEC_ORG_ID_NAME,BIZ_TYP_NAME,
				END_UNLOAD_ADDRESS,LOAD_AREA_NAME2,UNLOAD_AREA_NAME2,SUPLR_NAME,TOT_QNTY,
				TOT_VOL,TOT_GROSS_W,ODR_TIME,DEPART_TIME);
		
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

	protected DynamicForm createSerchForm(DynamicForm form) {
		
		/**
		 * 	客户        客户单号	订单时间 从   到       发运时间 从   到     执行机构   订单类型	供应商					
		 * 
		 */
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
		Util.initCustComboValue(CUSTOMER, LoginCache.getDefCustomer().get("CUSTOMER_ID"));
		CUSTOMER.setWidth(127);
//		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO());
		
		SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());
		SGText ODR_NO=new SGText("ODR_NO",Util.TI18N.ODR_NO());
		SGText SHPM_NO=new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		
//		ODR_TIME_FROM =new  SGDateTime("ASSIGN_TIME_FROM", Util.TI18N.ASSIGN_TIME_FROM(),true);//派发时间 2011-4-7
//		ODR_TIME_TO = new  SGDateTime("ASSIGN_TIME_TO", "到");
//		String PreYesDate=Util.getYesPreDay();
//		ODR_TIME_FROM.setDefaultValue(PreYesDate);
//		ODR_TIME_TO.setDefaultValue(Util.getCurTime());
		/**
		radioGroupItem = new RadioGroupItem();  
		radioGroupItem.setShowTitle(false); 
		radioGroupItem.setVertical(false);
		radioGroupItem.setValueMap("昨天", "今天","本周", "本月");  
//	    radioGroupItem.setDefaultValue("昨天");
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
					ODR_TIME_TO.setValue(Predate);
			    } else if(cur_value.equals("本月")){
					ODR_TIME_FROM.setValue(PreMonDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value.equals("本周")){
					ODR_TIME_FROM.setValue(PreWeekDate);
					ODR_TIME_TO.setValue(Util.getCurTime());
			    }
			}
		});
	    */
	    
		LOAD_TIME_FROM = new  SGDateTime("LOAD_TIME_FROM", Util.TI18N.DEPART_TM_FROM(),true);
		LOAD_TIME_TO = new  SGDateTime("LOAD_TIME_TO","到");
//		LOAD_TIME_FROM.setDefaultValue(PreYesDate);
//		LOAD_TIME_TO.setDefaultValue(Util.getCurTime());
		
		radioGroupItem2 = new RadioGroupItem();  
		radioGroupItem2.setShowTitle(false); 
		radioGroupItem2.setVertical(false);
		radioGroupItem2.setValueMap("昨天", "今天","本周", "本月");  
//	    radioGroupItem2.setDefaultValue("昨天");
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
					LOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("昨天")){
			    	LOAD_TIME_FROM.setValue(PreYesDate);
			    	LOAD_TIME_TO.setValue(Predate);
			    } else if(cur_value2.equals("本月")){
			    	LOAD_TIME_FROM.setValue(PreMonDate);
			    	LOAD_TIME_TO.setValue(Util.getCurTime());
			    }else if(cur_value2.equals("本周")){
			    	LOAD_TIME_FROM.setValue(PreWeekDate);
			    	LOAD_TIME_TO.setValue(Util.getCurTime());
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
//		SGCombo ORD_TYP=new SGCombo("ODR_TYP_NAME",Util.TI18N.ODR_TYP());
//		Util.initCodesComboValue(ORD_TYP, "ORD_TYP");
		
//		SGCombo STATUS_FROM = new SGCombo("STATUS_FROM",Util.TI18N.SHPM_STSTUS());
//		STATUS_FROM.setColSpan(2);
//		STATUS_FROM.setWidth(127);
//		Util.initStatus(STATUS_FROM, StaticRef.SHPMNO_STAT, "");
//		SGCombo STATUS_TO = new SGCombo("STATUS_TO","到");//wangjun 2011-4-7
//		STATUS_TO.setColSpan(2);
//		STATUS_TO.setWidth(127);
//		Util.initStatus(STATUS_TO, StaticRef.SHPMNO_STAT, "");
		
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME"," where ID in (select SUPLR_ID from BAS_SUPPLIER_ORG where " +
				"ORG_ID='"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"') and ENABLE_FLAG='Y'");
		
//		SGCheck LOAD_FLAG = new SGCheck("LOAD_FLAG", "已发货",true);	
//		LOAD_FLAG.setValue(false);
//		LOAD_FLAG.setColSpan(2);
		
//		SGCheck UNLOAD_FLAG = new SGCheck("UNLOAD_FLAG", "未发货");	
//		UNLOAD_FLAG.setValue(false);
//		UNLOAD_FLAG.setColSpan(2);
		
//		SGCheck NO_UNLOAD_FLAG = new SGCheck("NO_UNLOAD_FLAG", "未到货(已发未到)");	
//		NO_UNLOAD_FLAG.setValue(false);
		
//		SGCheck C_CX_FLAG = new SGCheck("C_CX_FLAG", "不包含促销品");	
//		C_CX_FLAG.setValue(false);//
//		C_CX_FLAG.setColSpan(2);
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构
		C_RDC_FLAG.setValue(false);
		
		SGCombo BIZ_TYP=new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());
		Util.initCodesComboValue(BIZ_TYP,"BIZ_TYP");
		
		SGCombo TRANS_SRVC_ID=new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());
		Util.initTrsService(TRANS_SRVC_ID, "");
		
//		CUSTOM_ODR_NO.setWidth(127);
//		ODR_TIME_FROM.setWidth(127);
//		ODR_TIME_TO.setWidth(127);
		LOAD_TIME_FROM.setWidth(127);
		LOAD_TIME_TO.setWidth(127);
		SUPLR_ID.setWidth(127);
//		ORD_TYP.setWidth(127);
		
		
		form.setItems(CUSTOMER,
//				CUSTOM_ODR_NO,
				REFENENCE1,ODR_NO,SHPM_NO,BIZ_TYP,TRANS_SRVC_ID,EXEC_ORG_ID_NAME,
//				ORD_TYP,
//				STATUS_FROM,STATUS_TO,
				SUPLR_ID,
//				ODR_TIME_FROM,ODR_TIME_TO,radioGroupItem,
				LOAD_TIME_FROM,LOAD_TIME_TO,radioGroupItem2,
				C_ORG_FLAG,EXEC_ORG_ID,C_RDC_FLAG);
		
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
		R_JOB_OPER_ITEM view = new R_JOB_OPER_ITEM();
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
