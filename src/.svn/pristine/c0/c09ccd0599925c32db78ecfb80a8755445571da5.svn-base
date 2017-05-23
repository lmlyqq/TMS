package com.rd.client.view.settlement;

import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.settlement.BillLogDS;
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
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 费用管理->计费管理->计费日志
 * @author
 */
@ClassForNameAble
public class BillLogView extends SGForm implements PanelFactory {

	public SGTable table;
	private DataSource ds;
	private Window searchWin;
	private SGPanel searchForm = new SGPanel();
	private SectionStack sectionStack;	
	private DynamicForm pageForm;
	private SectionStackSection  listItem;

	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		
		VLayout main = new VLayout();
		main.setHeight100();
		main.setWidth("99%");
		ds = BillLogDS.getInstance("BILL_LOG","BILL_LOG");
				
		
		// 主布局
		
		VStack vsk = new VStack();
		vsk.setWidth100();
		vsk.setHeight100();
		
	

		// 列表
		table = new SGTable(ds, "100%", "100%", false, true, false);
		
		createListField();
		sectionStack = new SectionStack();
		listItem = new SectionStackSection("协议列表");
		listItem.setItems(table);
		sectionStack.addSection(listItem);
		pageForm = new SGPage(table,true).initPageBtn();
		listItem.setControls(pageForm);
		sectionStack.setWidth("100%");
		
		// 按钮布局
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		createBtnWidget(toolStrip);
		
		
		main.setMembers(toolStrip,sectionStack);
		
		initVerify();
		
		return main;
	}
	

	private void createListField() {
	
		table.setShowRowNumbers(true);

		table.setCanEdit(false);
		ListGridField OBJECT_NAME = new ListGridField("OBJECT_NAME","业务对象", 110);
		ListGridField OBJECT_TYPE = new ListGridField("OBJECT_TYPE","业务类型", 80);
		Util.initCodesComboValue(OBJECT_TYPE, "TRANS_TFF_TYP");
		ListGridField DOC_NO = new ListGridField("DOC_NO", "单据编号", 120);
		ListGridField BILL_RESULT = new ListGridField("BILL_RESULT","计费结果",80);
		ListGridField NOTES = new ListGridField("NOTES", "计费说明",450);
		ListGridField ADDTIME = new ListGridField("ADDTIME","计费时间",120);
		ListGridField ADDWHO=new ListGridField("ADDWHO","计费对象",80);
		ListGridField BILL_TYPE=new ListGridField("BILL_TYPE","计费方式",80);
		
		table.setFields(OBJECT_NAME,OBJECT_TYPE,DOC_NO,BILL_RESULT,NOTES,ADDTIME,ADDWHO,BILL_TYPE);
		
	}


	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(3);
		toolStrip.setSeparatorSize(5);

		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		
		toolStrip.addMember(searchButton);
		toolStrip.addSeparator();

		searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (searchWin == null) {
					searchWin = new SearchWin(380,ds, //600 ,380
							createSearchForm(searchForm), sectionStack.getSection(0),null).getViewPanel();
				} else {
					searchWin.show();
				}
			}

		});
		IButton exportButton = createBtn(StaticRef.EXPORT_BTN,SettPrivRef.BillLog_P0_01);
		exportButton.addClickHandler(new ExportAction(table));
		
		toolStrip.setMembersMargin(4);
		toolStrip.setMembers(searchButton,exportButton);

		
	}
	
	
	
	
	
	protected DynamicForm createSearchForm(DynamicForm form) {
		
		form.setDataSource(ds);
		
		//第一行：模糊查询
		SGText OBJECT_NAME=new SGText("OBJECT_NAME", "业务对象");
		
		SGText DOC_NO=new SGText("DOC_NO", "单据编号");
		
		SGDateTime FROM_TIME = new SGDateTime("FROM_TIME","计费时间从",true);
		FROM_TIME.setDefaultValue(getCurInitDay());
		
		SGDateTime END_TIME = new SGDateTime("END_TIME","到");
		END_TIME.setDefaultValue(getCurTime());
		
		SGCombo BILL_RESULT=new SGCombo("BILL_RESULT", "计费结果");
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("", "");
		map.put("Y", "成功");
		map.put("N", "失败");
		BILL_RESULT.setValueMap(map);
		BILL_RESULT.setDefaultValue("N");
		
		form.setItems(OBJECT_NAME,DOC_NO,FROM_TIME,END_TIME,BILL_RESULT);
		
		return form;
		
	}

	public void initVerify() {
		
		check_map.put("TABLE", "BILL_LOG");
		check_map.put("ADDTIME", StaticRef.CHK_DATE+ "计费时间");
	}

	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}	
	public BillLogView getThis(){
		return this;
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BillLogView view = new BillLogView();
		view.setFUNCID(id);
		view.addMember(view.getViewPanel());
		return view;
	}

	@Override
	public String getCanvasID() {
		return getID();
	}


	@Override
	public void createForm(DynamicForm form) {
		
	}
	
	public static native String getCurInitDay() /*-{ 
	var now = new Date();
	var year=now.getFullYear();
	var month=now.getMonth()+1;
	var day=now.getDate();	
	var res = year+"/"+month+"/"+ day + " 00:00";
	return res;

}-*/;
	
	public static native String getCurTime() /*-{

	var now = new Date();
	var year=now.getFullYear();
	var m=now.getMonth()+1;
	var month = (m < 10) ? '0' + m : m;
	var day=now.getDate();
	var hour=now.getHours();
	var minute=now.getMinutes();
	if (minute < 10) {
	    minute = "0" + minute;
	}
	var res = year+"-"+month+"-"+ day + " " + hour + ":" + minute;
	return res;
}-*/;
	
}
