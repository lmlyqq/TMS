package com.rd.client.view.alert;

import com.google.gwt.user.client.Cookies;
import com.rd.client.PanelFactory;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.MSGUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.ContactRemindDS;
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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 *系统提醒——合同到期提醒
 * @author 
 *
 */
@ClassForNameAble
public class ContactRemindView extends SGForm implements PanelFactory {

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
	    
	    ds = ContactRemindDS.getInstance("V_CONTACT_REMAIN","V_CONTACT_REMAIN");	
	
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
		criteria.addCriteria("REMAIN_DAYS", "30");
		//criteria.addCriteria("EXEC_ORG_ID", LoginCache.getLoginUser().getDEFAULT_ORG_ID());
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
	
        ListGridField OBJECT_NAME = new ListGridField("OBJECT_NAME", "协议对象", 110);
        ListGridField TFF_NAME = new ListGridField("TFF_NAME", "协议名称", 90);
        ListGridField CONTACT_NO = new ListGridField("CONTACT_NO", "合同号", 100);
        ListGridField START_DATE = new ListGridField("START_DATE", "生效日期", 110);
        ListGridField END_DATE = new ListGridField("END_DATE", "失效日期", 90);
        ListGridField REMAIN_DAYS = new ListGridField("REMAIN_DAYS", "剩余天数", 80);

        table.setFields(OBJECT_NAME,TFF_NAME,CONTACT_NO,START_DATE,END_DATE,REMAIN_DAYS);
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
//					searchWin.setWidth(700);
//					searchWin.setHeight(270);
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
	public DynamicForm createSerchForm(DynamicForm form){
   
		form.setDataSource(ds);
			
		SGText OBJECT_NAME = new SGText("OBJECT_NAME", "协议对象");
		SGText TFF_NAME = new SGText("TFF_NAME", "协议名称");
		SGText CONTACT_NO = new SGText("CONTACT_NO", "合同号");
		SGDate END_DATE_FROM = new SGDate("END_DATE_FROM", "失效日期 从",true);
		END_DATE_FROM.setWidth(FormUtil.Width);
		SGDate END_DATE_TO = new SGDate("END_DATE_TO", "到");
		END_DATE_TO.setWidth(FormUtil.Width);		
		SGText REMAIN_DAYS = new SGText("REMAIN_DAYS", "几天以内");
		//REMAIN_DAYS.setValue("30");
		
		form.setItems(OBJECT_NAME,TFF_NAME,CONTACT_NO,END_DATE_FROM,END_DATE_TO,REMAIN_DAYS);
		
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
		ContactRemindView view = new ContactRemindView();
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
