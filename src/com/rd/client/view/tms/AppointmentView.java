package com.rd.client.view.tms;

import com.google.gwt.user.client.Cookies;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
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
import com.rd.client.ds.tms.AppointmentDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 运输管理->到货预约提醒
 * @author lml
 *
 */
@ClassForNameAble
public class AppointmentView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table; 
	 private SectionStack section;
	 private Window searchWin = null;
	 private SGPanel searchForm;
	 private DynamicForm pageForm;
	 /*public AppointmentView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.LEFT);
	    
	    ds = AppointmentDS.getInstance("V_RECE_APPOINTMENT","TRANS_ORDER_HEADER");	
	
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection("列表信息");
	    listItem.setItems(table);
	    listItem.setExpanded(true);

	    pageForm=new SGPage(table, true).initPageBtn();
		listItem.setControls(pageForm);
	    section.addSection(listItem);
	    section.setWidth("100%");
	   
	    getConfigList();
	
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(section);
       
		initVerify();
		
		Criteria criteria=new Criteria();
		criteria.addCriteria("OP_FLAG", "M");
		criteria.addCriteria("REMAIN_HOURS", "12");
		criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		table.setFilterEditorCriteria(criteria);
		table.fetchData(criteria, new DSCallback() {

			@Override
			public void execute(DSResponse response, Object rawData,
					DSRequest request) {
				if(rawData.toString().indexOf("HTTP code: 0") > 0 || rawData.toString().indexOf("404") >= 0) {
					MSGUtil.sayError("服务器连接已中断，请重新登录!");
				}
				if(pageForm != null) {
					pageForm.getField("CUR_PAGE").setValue("1");
					pageForm.getField("TOTAL_COUNT").setValue(Cookies.getCookie("TOTALROWS"));
					pageForm.getField("SUM_PAGE").setValue(Cookies.getCookie("TOTALPAGES"));
					String sqlwhere = Cookies.getCookie("SQLWHERE");
					String key = Cookies.getCookie("SQLFIELD1");
					String value = Cookies.getCookie("SQLFIELD2");
					String alias = Cookies.getCookie("SQLALIAS");
					if(ObjUtil.isNotNull(sqlwhere) && sqlwhere.length() > 1) {
						table.setProperty("WHERE", sqlwhere.substring(1,sqlwhere.length()-1));
						table.setProperty("SQLFIELD1", key);
						table.setProperty("SQLFIELD2", value);
						table.setProperty("SQLALIAS", alias);
						//LoginCache.setPageResult(table, pageForm.getField("TOTAL_COUNT"), pageForm.getField("SUM_PAGE"));
					}
				}
				table.setSelectOnEdit(true);
				if(table.getRecords().length > 0){
					table.selectRecord(table.getRecord(0));
					
				}
				
			}
			
		});
		return main;
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}
	

	 
	private void getConfigList() {
	
        ListGridField ODR_NO = new ListGridField("ODR_NO", "托运单号", 110);
        ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO", "客户单号", 90);
        ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME", "客户", 110);
        ListGridField VEHICLE_TYP_NAME = new ListGridField("VEHICLE_TYP_NAME", "车型要求", 90);
        ListGridField ODR_TYP_NAME = new ListGridField("ODR_TYP_NAME", "运输类型", 80);
        ListGridField BIZ_TYP_NAME = new ListGridField("BIZ_TYP_NAME", "业务类型", 80);
        ListGridField LOAD_NAME = new ListGridField("LOAD_NAME", "发货方", 120);
        ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME", "收货方", 120);
        ListGridField PRE_UNLOAD_TIME = new ListGridField("PRE_UNLOAD_TIME", "预计到货时间", 120);
        ListGridField REQ_HOURS = new ListGridField("APPOINT_HOURS", "提前预约时长", 100);
        ListGridField REMAIN_HOURS = new ListGridField("REMAIN_HOURS", "剩余时长", 70);  
        
        
        table.setFields(ODR_NO,CUSTOM_ODR_NO,CUSTOMER_NAME,VEHICLE_TYP_NAME,ODR_TYP_NAME,BIZ_TYP_NAME
        		,LOAD_NAME,UNLOAD_NAME,PRE_UNLOAD_TIME,REQ_HOURS,REMAIN_HOURS);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.setAlign(Alignment.RIGHT);
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.PACK);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0)).getViewPanel();
					searchWin.setWidth(700);
					searchWin.setHeight(270);
				}
				else {
					searchWin.show();
				}
			}
        	
        });
	
     

        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton);
	}
	
	public void initVerify() {
		
		
	}
	
	//查询窗口
	public SGPanel createSerchForm(SGPanel form){
   
		form.setDataSource(ds);
			
		SGText ODR_NO = new SGText("ODR_NO", "托运单号");
		SGCombo CUSTOMER_NAME = new SGCombo("CUSTOMER_NAME", "客户");
		Util.initCustComboValue(CUSTOMER_NAME, "");	
		SGDateTime PRE_UNLOAD_TIME_FROM = new SGDateTime("PRE_UNLOAD_TIME_FROM", "预计到货时间 从");
		PRE_UNLOAD_TIME_FROM.setWidth(FormUtil.Width);
		SGDateTime PRE_UNLOAD_TIME_TO = new SGDateTime("PRE_UNLOAD_TIME_TO", "到");
		PRE_UNLOAD_TIME_TO.setWidth(FormUtil.Width);
		
		SGText REMAIN_HOURS = new SGText("REMAIN_HOURS", "几小时以内",true);
		//REMAIN_HOURS.setValue("12");
		
		TextItem EXEC_ORG_ID = new TextItem("EXEC_ORG_ID");
		EXEC_ORG_ID.setVisible(false);
	    //二级窗口 EXEC_ORG_ID 执行结构
		SGText EXEC_ORG_ID_NAME = new SGText("EXEC_ORG_ID_NAME",Util.TI18N.EXEC_ORG_ID());
//		EXEC_ORG_ID_NAME.setDisabled(true);
		Util.initOrg(EXEC_ORG_ID_NAME, EXEC_ORG_ID, false, "50%", "50%");
		EXEC_ORG_ID.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID());
		EXEC_ORG_ID_NAME.setValue(LoginCache.getLoginUser().getDEFAULT_ORG_ID_NAME());
		
		SGCheck C_ORG_FLAG = new SGCheck("C_ORG_FLAG", Util.TI18N.C_ORG_FLAG());	
		C_ORG_FLAG.setValue(true);//包含下级机构
		C_ORG_FLAG.setColSpan(2);
		
		form.setItems(ODR_NO,CUSTOMER_NAME,PRE_UNLOAD_TIME_FROM,PRE_UNLOAD_TIME_TO,REMAIN_HOURS,EXEC_ORG_ID,EXEC_ORG_ID_NAME,C_ORG_FLAG);
		
        return form;
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
		AppointmentView view = new AppointmentView();
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