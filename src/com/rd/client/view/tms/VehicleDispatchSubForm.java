package com.rd.client.view.tms;

import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;

public class VehicleDispatchSubForm {

	private int i=0;
	//创建车辆查询条件布局
//	protected SGPanel createVehicleQueryForm(SGPanel form){
//		
//		final SGCombo VEHICLE_STAT = new SGCombo("VEHICLE_STAT",Util.TI18N.VEHICLE_STAT());
//		Util.initCodesComboValue(VEHICLE_STAT, "VECHICLE_STAT");
//		
//		final SGCombo VEHICLE_TYP_ID = new SGCombo("VEHICLE_TYP_ID",Util.TI18N.VEHICLE_TYPE());
//		Util.initComboValue(VEHICLE_TYP_ID, "BAS_VEHICLE_TYPE", "ID", "VEHICLE_TYPE", " WHERE  ENABLE_FLAG = 'Y'", "");
//		
//		SGCombo SUPLR_ID = new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());  //供应商
//		Util.initSupplier(SUPLR_ID, "");
//		
//		final SGText CUR_LOCATION = new SGText("CURRENT_AREA","当前区域");
//		
//		SGCombo TMP_ATTR = new SGCombo("TMP_ATTR", Util.TI18N.TMP_ATTR(), true);
//		Util.initCodesComboValue(TMP_ATTR, "TMP_ATTR");
//		
//		SGCombo AVAIL_ATTR = new SGCombo("AVAIL_ATTR", Util.TI18N.AVAIL_ATTR());
//		Util.initCodesComboValue(AVAIL_ATTR, "AVAIL_ATTR");
//		
//		SGText MAX_WEIGHT = new SGText("MAX_WEIGHT","载重量大于");
//		
//		SGText PLATE_NO = new SGText("PLATE_NO",Util.TI18N.PLATE_NO());
//	
//		form.setItems(VEHICLE_STAT, VEHICLE_TYP_ID, SUPLR_ID, CUR_LOCATION, TMP_ATTR, AVAIL_ATTR, MAX_WEIGHT, PLATE_NO);
//		
//		return  form;
//		
//	}
	//创建作业单查询条件布局
	protected SGPanel createUnshpmQueryForm(SGPanel form){
	
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
		CUSTOMER_NAME.setStartRow(true);
		CUSTOMER_NAME.setWidth(FormUtil.Width);
		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		
		
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		
		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		
		SGText EXEC_ORG_ID = new SGText("EXEC_ORG_ID",Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID.setVisible(false);
	    //二级窗口 EXEC_ORG_ID 执行结构
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		EXEC_ORG_ID_NAME.setWidth(FormUtil.Width);
		
		final TextItem LOAD_AREA_ID2 = new TextItem("LOAD_AREA_ID");
		LOAD_AREA_ID2.setVisible(false);
		
		ComboBoxItem LOAD_AREA_NAME2=new ComboBoxItem("LOAD_AREA_NAME2","发货城市");//起点区域
		LOAD_AREA_NAME2.setColSpan(2);
		Util.initArea(LOAD_AREA_NAME2, LOAD_AREA_ID2);
		LOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		final TextItem UNLOAD_AREA_ID2 = new TextItem("UNLOAD_AREA_ID2");
		UNLOAD_AREA_ID2.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME2=new ComboBoxItem("UNLOAD_AREA_NAME2","收货城市");//
		UNLOAD_AREA_NAME2.setColSpan(2);
		Util.initArea(UNLOAD_AREA_NAME2, UNLOAD_AREA_ID2);
		UNLOAD_AREA_NAME2.setTitleOrientation(TitleOrientation.TOP);
		
		
		final TextItem LOAD_AREA_ID3 = new TextItem("LOAD_AREA_ID3");
		LOAD_AREA_ID3.setVisible(false);
		
		ComboBoxItem LOAD_AREA_NAME3=new ComboBoxItem("LOAD_AREA_NAME3",Util.TI18N.LOAD_AREA_NAME());//起点区域
		LOAD_AREA_NAME3.setColSpan(2);
		Util.initArea(LOAD_AREA_NAME3, LOAD_AREA_ID3);
		LOAD_AREA_NAME3.setTitleOrientation(TitleOrientation.TOP);
		
		final TextItem UNLOAD_AREA_ID3 = new TextItem("UNLOAD_AREA_ID3");
		UNLOAD_AREA_ID3.setVisible(false);
		ComboBoxItem UNLOAD_AREA_NAME3=new ComboBoxItem("UNLOAD_AREA_NAME3",Util.TI18N.UNLOAD_AREA_NAME());//
		UNLOAD_AREA_NAME3.setColSpan(2);
		Util.initArea(UNLOAD_AREA_NAME3, UNLOAD_AREA_ID3);
		UNLOAD_AREA_NAME3.setTitleOrientation(TitleOrientation.TOP);
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());
		TRANS_SRVC_ID.setTitle(Util.TI18N.TRANS_SRVC_ID());
		Util.initTrsService(TRANS_SRVC_ID, "");

		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGCombo BIZ_TYP = new SGCombo("BIZ_TYP", Util.TI18N.BIZ_TYP());	//业务类型
		Util.initCodesComboValue(BIZ_TYP, "BIZ_TYP");
		
		SGText LOAD_ID = new SGText("LOAD_CODE", Util.TI18N.LOAD_NAME_ID());//收货方代码
		
		SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME());//发货方
		
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());//收货方
		
		SGText UNLOAD_ID = new SGText("UNLOAD_CODE", Util.TI18N.UNLOAD_NAME_ID());//收货方代码

		SGCombo STATUS =new SGCombo("SHPM_STATUS", Util.TI18N.SHPM_STSTUS());//
		Util.initCodesComboValue(STATUS, "SHPM_STAT");
		STATUS.setVisible(false);
		
		SGCombo EXEC_STAT = new SGCombo("EXEC_STAT", Util.TI18N.STATUS());//执行状态
		Util.initCodesComboValue(EXEC_STAT,"EXEC_STAT","");
		
		SGText Queue = new SGText("QUEUE","QUEUE");
		Queue.setVisible(false);
		Queue.setValue("Y");
		
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//路线
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME",
				" where EXEC_ORG_ID in (select id from bas_org where id='"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+"' " +
						"or org_index like '%,"+LoginCache.getLoginUser().getDEFAULT_ORG_ID()+",%')", 
				" order by show_seq asc");
		
		SGCombo TRANS_COND = new SGCombo("REFENENCE4",Util.TI18N.TRANS_COND());
        Util.initCodesComboValue(TRANS_COND, "TRANS_COND");
        
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
		
		SGText ODR_TIME_FROM = new SGText("ODR_TIME_FROM", Util.TI18N.ODR_TIME_FROM(),true); //璁㈠崟鏃堕棿
		SGText ODR_TIME_TO = new SGText("ODR_TIME_TO", "到");
		Util.initDateTime(form,ODR_TIME_FROM);
		Util.initDateTime(form,ODR_TIME_TO);
		//ODR_TIME_FROM.setWidth(128);
		//ODR_TIME_TO.setWidth(128);
		
		SGText ODR_NO = new SGText("ODR_NO", Util.TI18N.ODR_NO());
		SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());
		
		SGText POD_TIME5 = new SGText("PRE_UNLOAD_TIME_FROM", "计划派送时间");
		SGText POD_TIME6 = new SGText("PRE_UNLOAD_TIME_TO", "到");
		Util.initDateTime(form,POD_TIME5);
		Util.initDateTime(form,POD_TIME6);
			
		SGText UDF6 = new SGText("UDF6",Util.TI18N.SHPM_UDF6());
		UDF6.setValue("N");
		UDF6.setVisible(false);
		
		SGText ADD_TIME_FROM = new SGText("ADDTIME_FROM", Util.TI18N.ORD_ADDTIME_FROM()); //
		SGText ADD_TIME_TO = new SGText("ADDTIME_TO", "到");
		Util.initDateTime(form,ADD_TIME_FROM);
		Util.initDateTime(form,ADD_TIME_TO);
		//ADD_TIME_FROM.setWidth(128);
		//ADD_TIME_TO.setWidth(128);
		
		SGText PRE_LOAD_TIME_FROM = new SGText("PRE_LOAD_TIME_FROM", "要求发货时间从"); 
		SGText PRE_LOAD_TIME_TO = new SGText("PRE_LOAD_TIME_TO", "到");
		Util.initDateTime(form,PRE_LOAD_TIME_FROM);
		Util.initDateTime(form,PRE_LOAD_TIME_TO);

		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,SHPM_NO,CUSTOM_ODR_NO_NAME,EXEC_ORG_ID,EXEC_ORG_ID_NAME,LOAD_AREA_NAME2,LOAD_AREA_ID2,
				UNLOAD_AREA_NAME2,UNLOAD_AREA_ID2,TRANS_SRVC_ID,SUPLR_ID,
				LOAD_NAME,UNLOAD_NAME,LOAD_ID, UNLOAD_ID,
				STATUS,ODR_TIME_FROM,ODR_TIME_TO,BIZ_TYP,ROUTE_ID,LOAD_AREA_NAME3,LOAD_AREA_ID3,
				UNLOAD_AREA_NAME3,UNLOAD_AREA_ID3, Queue, ODR_NO,REFENENCE1 ,POD_TIME5, POD_TIME6, ADD_TIME_FROM, ADD_TIME_TO,PRE_LOAD_TIME_FROM,PRE_LOAD_TIME_TO, UDF6, C_ORG_FLAG);
		return form;
	}
	
	//调度单查询窗口布局
	protected DynamicForm createSerchForm(DynamicForm form) {
	
		/**
		 * 调度单号 LOAD_NO   供应商 SUPLR_ID   车牌号 PLATE_NO   状态 从 到 STATUS   执行机构 EXEC_ORD_ID  
		 * 包含下级机构 C_ORG_FLAG  客户  CUSTOMER_ID   客户单号   CUSTOM_ODR_NO  起点区域  START_AREA
		 * 创建时间 从  到  ORD_ADDTIME_FROM    发运时间 从到    ORD_PLAN_TIME   到货时间 从到  UNLOAD_TIME_FRON
		 */
//		SGCombo CUSTOMER=new SGCombo("CUSTOMER_ID",Util.TI18N.CUSTOMER(),true);
//		Util.initCustComboValue(CUSTOMER, "");
		final TextItem CUSTOMER_ID=new TextItem("CUSTOMER_ID");
		CUSTOMER_ID.setVisible(false);
		
		final ComboBoxItem CUSTOMER_NAME=new ComboBoxItem("CUSTOMER_NAME",Util.TI18N.CUSTOMER());
//		CUSTOMER_NAME.setStartRow(true);
//		CUSTOMER_NAME.setWidth(120);
//		CUSTOMER_NAME.setColSpan(2);
		CUSTOMER_NAME.setTitleAlign(Alignment.LEFT);
		CUSTOMER_NAME.setTitleOrientation(TitleOrientation.TOP);
		Util.initCustomerByQuery(CUSTOMER_NAME, CUSTOMER_ID);
		CUSTOMER_NAME.setVisible(false);
	
		SGText LOAD_NO=new SGText("LOAD_NO",Util.TI18N.LOAD_NO());//调度单号

		SGText CUSTOM_ODR_NO_NAME=new SGText("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO());//客户单号
		  
		SGText SHPM_NO=new SGText("SHPM_NO",Util.TI18N.SHPM_NO());//
		
		SGText REFENENCE1=new SGText("REFENENCE1",Util.TI18N.REFENENCE1());

		//2
		SGCombo STATUS_FROM=new SGCombo("STATUS_FROM", Util.TI18N.STATUS(),true);//状态 从 到 
		SGCombo STATUS_TO=new SGCombo("STATUS_TO", "到");
		Util.initStatus(STATUS_FROM, StaticRef.LOADNO_STAT, "10");
		Util.initStatus(STATUS_TO, StaticRef.LOADNO_STAT, "10");
		
		//二级窗口 SUPLR_ID_NAME
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO",Util.TI18N.PLATE_NO());//

		//3
		ComboBoxItem START_AREA=new ComboBoxItem("START_AREA_NAME",Util.TI18N.START_AREA_ID_NAME());//起点区域
		START_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem START_AREA_ID=new TextItem("START_AREA_ID", Util.TI18N.START_ARAE());
		START_AREA_ID.setVisible(false);
		Util.initArea(START_AREA, START_AREA_ID);
		
		ComboBoxItem END_AREA=new ComboBoxItem("END_AREA_NAME",Util.TI18N.END_AREA_ID_NAME());//
		END_AREA.setTitleOrientation(TitleOrientation.TOP);
		TextItem END_AREA_ID=new TextItem("END_AREA_ID", Util.TI18N.END_AREA());
		END_AREA_ID.setVisible(false);
		Util.initArea(END_AREA, END_AREA_ID);

		SGDateTime ORD_ADDTIME_FROM = new SGDateTime("ADDTIME", Util.TI18N.ORD_ADDTIME_FROM());//创建时间 从  到  
		SGDateTime ORD_ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		
		//4
		
		SGCombo DISPATCH_STAT= new SGCombo("DISPATCH_STAT",Util.TI18N.DISPATCH_STAT_NAME(),true);
		Util.initCodesComboValue(DISPATCH_STAT, "DISPATCH_STAT");//--wangjun 2010-2-27
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME()); 
		
		SGText ADDWHO=new SGText("ADDWHO",Util.TI18N.ADDWHO());//制单人
		
		SGCheck C_ORG_FLAG=new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());// 包含下级机构	
//		C_ORG_FLAG.setWidth(120);
		C_ORG_FLAG.setColSpan(2);
		C_ORG_FLAG.setValue(true);
		
		SGCheck C_RDC_FLAG=new SGCheck("C_RDC_FLAG", Util.TI18N.C_RDC_FLAG());// 包含下级机构	
//		C_RDC_FLAG.setColSpan(3);
		
		//SGCheck HISTORY_FLAG=new SGCheck("HISTORY_FLAG", "查看历史数据");// 包含历史数据
		
		SGCombo ROUTE_ID = new SGCombo("ROUTE_ID", Util.TI18N.ROUTE_NAME());	//业务类型
		Util.initComboValue(ROUTE_ID, "BAS_ROUTE_HEAD", "ID", "ROUTE_NAME", "", " order by show_seq asc");
		
		SGCombo TRANS_SRVC_ID = new SGCombo("TRANS_SRVC_ID", Util.TI18N.TRANS_SRVC_ID());
		TRANS_SRVC_ID.setTitle(Util.TI18N.TRANS_SRVC_ID());
		Util.initTrsService(TRANS_SRVC_ID, "");
		
		form.setItems(CUSTOMER_ID,CUSTOMER_NAME,LOAD_NO,CUSTOM_ODR_NO_NAME,SHPM_NO,REFENENCE1,
				STATUS_FROM,STATUS_TO,SUPLR_ID,PLATE_NO,START_AREA_ID,
				START_AREA,END_AREA_ID,END_AREA,ORD_ADDTIME_FROM,ORD_ADDTIME_TO,
				DISPATCH_STAT,ADDWHO,ROUTE_ID,TRANS_SRVC_ID,
				EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG,C_RDC_FLAG);
		return form;
	}
	
	//创建汇总布局
	protected DynamicForm createSumLayout() {
		DynamicForm form = new DynamicForm();
		form.setTitleSuffix("");
		form.setPadding(0);
		form.setMargin(3);
		form.setCellPadding(1);
		form.setNumCols(25);
		form.setAlign(Alignment.RIGHT);
		form.setWidth("95%");
		
        StaticTextItem COUNT = new StaticTextItem();  
        COUNT.setWrap(false);  
        COUNT.setWidth(35);
        COUNT.setShowTitle(false);
        COUNT.setDefaultValue("<font style=\"color: #000000;\">记录</font>");
        COUNT.setAlign(Alignment.RIGHT); 
        
		TextItem CUR_COUNT = new TextItem("CUR_COUNT"); 
		CUR_COUNT.setDefaultValue("0");
		CUR_COUNT.setShowTitle(false);
		CUR_COUNT.setWidth(38);
		CUR_COUNT.setHeight(18);
        
        StaticTextItem QNTY = new StaticTextItem();  
        QNTY.setWrap(false);  
        QNTY.setWidth(45);
        QNTY.setShowTitle(false);
        QNTY.setDefaultValue("<font style=\"color: #000000;\">" + Util.TI18N.QNTY() + "</font>");
        QNTY.setAlign(Alignment.RIGHT); 
        
		TextItem CUR_QNTY = new TextItem("CUR_QNTY"); 
		CUR_QNTY.setDefaultValue("0");
		CUR_QNTY.setShowTitle(false);
		CUR_QNTY.setWidth(45);
		CUR_QNTY.setHeight(18); 
		CUR_QNTY.setShowHint(true);
        
        StaticTextItem GROSS_W = new StaticTextItem();  
        GROSS_W.setWrap(false);  
        GROSS_W.setWidth(45);
        GROSS_W.setShowTitle(false);
        GROSS_W.setDefaultValue("<font style=\"color: #000000;\">" + Util.TI18N.G_WGT() + "</font>");
        GROSS_W.setAlign(Alignment.RIGHT); 
        
		TextItem CUR_GROSS_W = new TextItem("CUR_GROSS_W"); 
		CUR_GROSS_W.setDefaultValue("0");
		CUR_GROSS_W.setShowTitle(false);
		CUR_GROSS_W.setWidth(45);
		CUR_GROSS_W.setHeight(18); 

        StaticTextItem VOL = new StaticTextItem();  
        VOL.setWrap(false);  
        VOL.setWidth(45);
        VOL.setShowTitle(false);
        VOL.setDefaultValue("<font style=\"color: #000000;\">" + Util.TI18N.VOL() + "</font>");
        VOL.setAlign(Alignment.RIGHT); 
        
		TextItem CUR_VOL = new TextItem("CUR_VOL"); 
		CUR_VOL.setDefaultValue("0");
		CUR_VOL.setShowTitle(false);
		CUR_VOL.setWidth(45);
		CUR_VOL.setHeight(18); 

        form.setItems( QNTY, CUR_QNTY,  GROSS_W, CUR_GROSS_W,
        		VOL, CUR_VOL);
        return form;
	}
	
	
	protected IButton createTDBtn(final SectionStack sectionStack,final HLayout hLayout) {
		
		final IButton maxBtn = new IButton();
		maxBtn.setIcon(StaticRef.ICON_TODOWN);
		maxBtn.setTitle("");
		maxBtn.setPrompt("缩小");
		maxBtn.setWidth(24);
        maxBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(i==2){
					sectionStack.setHeight(Page.getHeight()/2-22);
					hLayout.setHeight("44%");
					hLayout.setVisible(true);
					i=i-1;
				}else if(i==1){
					sectionStack.setHeight(25);
					hLayout.setHeight(Page.getHeight() - 110);
					hLayout.setVisible(true);
					i=i-1;
				}
			}      	
        });   
        return maxBtn;
	}

	protected IButton createTPBtn(final SectionStack sectionStack,final HLayout hLayout) {
		
		final IButton maxBtn = new IButton();
		maxBtn.setIcon(StaticRef.ICON_TOUP);
		maxBtn.setTitle("");
		maxBtn.setPrompt("放大");
		maxBtn.setWidth(24);
	    maxBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				//System.out.println(sectionStack);
				//System.out.println(sectionStack.getSection(0));
//				sectionStack.r
				sectionStack.getSection(0).setExpanded(true);
				if(i==0){
					sectionStack.setHeight(Page.getHeight()/2-22);
					hLayout.setHeight("44%");
					i=i+1;
				}else if(i==1){
					sectionStack.setHeight(Page.getHeight() - 85);
					hLayout.setVisible(false);
					i=i+1;
				}
			}      	
	    });   
	    return maxBtn;
	}
}
