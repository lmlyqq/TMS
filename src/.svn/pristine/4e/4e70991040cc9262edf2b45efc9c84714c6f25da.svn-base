package com.rd.client.view.tms;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.PositionLogDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
@ClassForNameAble
public class PositionLogView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin;
	private SGPanel searchForm;
	private SectionStack section;
	
	/*public PositionLogView(String id) {
		super(id);
	}*/

	public Canvas getViewPanel() {
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds = PositionLogDS.getInstance("V_EXT_DRCT_LOG", "EXT_DRCT_LOG");

		table = new SGTable(ds, "100%", "70%");
        createListFields(table);
        table.setShowFilterEditor(false);
		
        //创建按钮布局
		createBtnWidget(toolStrip);
		section = createSection(table, null, true, true);
		initVerify();  
		VLayout main = new VLayout();//定义全局布局
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		return main;
		
	}
	
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}
	
	private void createListFields(SGTable table) {
		ListGridField LOAD_NO = new ListGridField("LOAD_NO",Util.TI18N.LOAD_NO(),110);
		ListGridField SUPLR_NAME = new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),70);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO",Util.TI18N.PLATE_NO(),70);
		ListGridField DRIVER = new ListGridField("DRIVER",Util.TI18N.DRIVER1(),70);
		ListGridField MOBILE = new ListGridField("MOBILE",Util.TI18N.MOBILE(),110);
		ListGridField SUCCESS_FLAG = new ListGridField("SUCCESS_FLAG",Util.TI18N.SUCCESS_FLAG(),80);
		SUCCESS_FLAG.setType(ListGridFieldType.BOOLEAN);
		
		ListGridField OPERATOR_TYPE = new ListGridField("OPERATOR_TYPE_NAME",Util.TI18N.OPERATOR_TYPE(),80);
		
		ListGridField DRCT_FEE = new ListGridField("DRCT_FEE",Util.TI18N.FEE(),70);
		Util.initFloatListField(DRCT_FEE, StaticRef.QNTY_FLOAT);
		
		ListGridField NOTES = new ListGridField("NOTES",Util.TI18N.NOTES(),110);
		ListGridField ADDTIME = new ListGridField("ADDTIME",Util.TI18N.POSITION_TIME(),120);
		Util.initDate(table, ADDTIME);
		
		ListGridField ADDWHO = new ListGridField("ADDWHO",Util.TI18N.POSITION_PERSON(),70);	
		table.setFields(LOAD_NO,SUPLR_NAME,PLATE_NO,DRIVER,MOBILE,SUCCESS_FLAG,
				OPERATOR_TYPE,DRCT_FEE,ADDTIME,ADDWHO,NOTES);
	
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
		
		// 导出按钮
		IButton expButton = createBtn(StaticRef.EXPORT_BTN);
		expButton.addClickHandler(new ExportAction(table, " addtime desc"));
		
		 toolStrip.setMembersMargin(4);
	     toolStrip.setMembers(searchButton,expButton);
	}

	//查询窗口（二级窗口）
	protected DynamicForm createSerchForm(DynamicForm form) {
		// TODO Auto-generated method stub
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		//第一行：模糊查询
		SGText LOAD_NO=new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		
		SGCombo SUPLR_ID =new SGCombo("SUPLR_ID", Util.TI18N.SUPLR_NAME());//供应商
		Util.initSupplier(SUPLR_ID, "");
		
		SGText PLATE_NO=new SGText("PLATE_NO", Util.TI18N.PLATE_NO());
		
		SGText MOBILE=new SGText("MOBILE", Util.TI18N.MOBILE());
		
		SGCombo OPERATOR_TYPE=new SGCombo("OPERATOR_TYPE", Util.TI18N.OPERATOR_TYPE());
		Util.initCodesComboValue(OPERATOR_TYPE, "OPER_TYPE");
		
		SGDateTime ADDTIME_FROM = new SGDateTime("ADDTIME_FROM", Util.TI18N.POSITION_TIME()+"  从");
		SGDateTime ADDTIME_TO = new SGDateTime("ADDTIME_TO", "到");
		
		SGCheck SUCCESS_FLAG=new SGCheck("SUCCESS_FLAG", Util.TI18N.SUCCESS_FLAG());
		
		form.setItems(LOAD_NO,SUPLR_ID,PLATE_NO,MOBILE,OPERATOR_TYPE,ADDTIME_FROM,ADDTIME_TO,SUCCESS_FLAG);
		return form;
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
		PositionLogView view = new PositionLogView();
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

