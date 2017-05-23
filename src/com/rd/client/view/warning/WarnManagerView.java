package com.rd.client.view.warning;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.SysPrivRef;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.warning.WarnSiteDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 预警管理->预警设置
 * @author fanglm
 * @create time 2011-01-12 20:08
 *
 */
@ClassForNameAble
public class WarnManagerView extends SGForm implements PanelFactory {
	
	 private DataSource ds;
	 private SGTable table;
	 //private SGPanel searchForm;
	 private SectionStack section;
	 
	 /*public WarnManagerView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
	    ToolStrip toolStrip = new ToolStrip();
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = WarnSiteDS.getInstance("WARNING_SITE", "WARNING_SITE");
		   
		table = new SGTable(ds, "100%", "100%"); 
		createListFields(table);
		
		section = createSection(table, null, true, true);
		
		Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG","M");
		table.fetchData(criteria);
	    
		createBtnWidget(toolStrip);
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
	    
		initVerify();
		return main;
	}
	 //设定需要校验的字段
	 public void initVerify() {
			
	 }

	@Override
	public void createForm(DynamicForm form) {
		
	}
    //布局列表信息按钮
	private void createListFields(SGTable table) {
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_map, true);
			}
		});
		  table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		  ListGridField TIMER = new ListGridField("MODULE_NAME", "功能模块", 100); 
		  TIMER.setCanEdit(false);
		  ListGridField MOTHED = new ListGridField("METHOD", "执行模式", 70);
		  ListGridField RUN_TIME = new ListGridField("START_TIME", "启动时间", 100);
		  ListGridField RUN_TIME1 = new ListGridField("SPACE_TIME", "间隔时长(分钟)", 100);
		  ListGridField RUN_TIME2 = new ListGridField("SEND_MESSAGE","是否发送短信",100);  
		  ListGridField ENABLE_FLAG = new ListGridField("MOBILE","手机号码",100);  
		  
		  ListGridField SEND_MAIL = new ListGridField("SEND_EMAIL", "是否发送邮件", 100);
		  ListGridField MAIL = new ListGridField("MAIL","邮箱",100);  
		  ListGridField MEASURE = new ListGridField("MEASURE","单位",100);  
		  ListGridField RAGE_VALUE = new ListGridField("RAGE_VALUE","范围值(0-N)",100);  
		  
		  table.setFields(TIMER,MOTHED, RUN_TIME,RUN_TIME1, RUN_TIME2, ENABLE_FLAG,SEND_MAIL,MAIL,MEASURE,RAGE_VALUE);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
         
        		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SysPrivRef.PARAM_P0_01);
        newButton.addClickHandler(new NewAction(table,check_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SysPrivRef.PARAM_P0_02);
        saveButton.addClickHandler(new SaveAction(table,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SysPrivRef.PARAM_P0_03);
        delButton.addClickHandler(new DeleteAction(table));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SysPrivRef.PARAM_P0_04);
        canButton.addClickHandler(new CancelAction(table,this));
        
    
        save_map.put(SysPrivRef.PARAM_P0_02, saveButton);
        save_map.put(SysPrivRef.PARAM_P0_04, canButton);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(saveButton, canButton);
	}
	@Override
	public void onDestroy() {
		table.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		WarnManagerView view = new WarnManagerView();
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
