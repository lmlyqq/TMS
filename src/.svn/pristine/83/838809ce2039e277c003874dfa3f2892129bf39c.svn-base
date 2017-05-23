package com.rd.client.win;

import com.rd.client.action.system.AssignToUserAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.system.SysUserDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 系统管理->列表配置里指定用户二级窗口
 * @author yuanlei
 *
 */
public class UserWin extends Window{
	
	private int width = 600;
	private int height = 380;
	private String title = "用戶";
	private SGTable userTable;
	private DynamicForm searchForm;
	public Window window;
	private DataSource userDS;
	private SGTable listTable;
	
	public UserWin(SGTable p_table) {
		listTable = p_table;
	}
	
	public void createBtnWidget(ToolStrip strip) {
	}

	public void createForm(DynamicForm searchForm) {
		
	}

	public Window getViewPanel() {
		
		searchForm = new DynamicForm();
		searchForm.setHeight(30);
		searchForm.setNumCols(8);
		searchForm.setPadding(5);
		searchForm.setAlign(Alignment.LEFT);
		userDS = SysUserDS.getInstance("SYS_USER", "SYS_USER");
		
		final SGCombo role_name = new SGCombo("ROLE_ID", Util.TI18N.ROLE_NAME());
		role_name.setColSpan(3);
		Util.initComboValue(role_name, "SYS_ROLE", "ID", "ROLE_NAME", " where ENABLE_FLAG = 'Y'");
		
		final SGText user_id = new SGText("FULLINDEX", Util.TI18N.USER());
		user_id.setColSpan(3);
		
		ButtonItem searchItem = new ButtonItem("查询");
		searchItem.setIcon(StaticRef.ICON_SEARCH);
		searchItem.setColSpan(1);
		searchItem.setStartRow(false);
		searchItem.setEndRow(false);
		searchItem.setAutoFit(true);
		searchItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Criteria criteria = new Criteria();
		        criteria.addCriteria("OP_FLAG", "M");
		        criteria.addCriteria("WHERE"," and USER_ID != '" + LoginCache.getLoginUser().getUSER_ID() + "'");
		        criteria.addCriteria("ROLE_ID", ObjUtil.ifObjNull(role_name.getValue(),"").toString());
		        criteria.addCriteria("CONTENT", ObjUtil.ifObjNull(user_id.getValue(),"").toString());
		        userTable.fetchData(criteria);
			}
			
		});
		
        searchForm.setItems(role_name,user_id,searchItem);
		
        userTable = new SGTable(userDS,"100%","100%",false,true,false);
        Criteria criteria = new Criteria();
        criteria.addCriteria("OP_FLAG", "M");
        criteria.addCriteria("WHERE"," and USER_ID != '" + LoginCache.getLoginUser().getUSER_ID() + "'");
        userTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        
        createListField(userTable);
        userTable.fetchData(criteria);
        
        HLayout toolStrip = new HLayout();
        toolStrip.setWidth100();
        toolStrip.setPadding(5);
        ButtonItem confirm = new ButtonItem(Util.BI18N.CONFIRM());
        confirm.setIcon(StaticRef.ICON_SAVE);
        confirm.setColSpan(1);
        confirm.setStartRow(false);
		confirm.setEndRow(false);
		confirm.setAutoFit(true);
        
        ButtonItem cancel = new ButtonItem(Util.BI18N.CANCEL());
        cancel.setIcon(StaticRef.ICON_CANCEL);
        cancel.setColSpan(1);
        cancel.setAutoFit(true);
        cancel.setStartRow(false);
        cancel.setEndRow(true);
        DynamicForm mainSearch = new DynamicForm();
        mainSearch.setHeight(30);
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(confirm,cancel);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
        
		window = new Window();
		window.setTitle(title);
		window.setLeft("25%");
		window.setTop("40%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);
		window.addItem(searchForm);
		window.addItem(userTable);
		window.addItem(mainSearch);
		window.show();
		
		confirm.addClickHandler(new AssignToUserAction(userTable,listTable, this));
		
		return window;
	}
	public void createListField(SGTable table) {
		ListGridField USER_ID = new ListGridField("USER_ID",Util.TI18N.USER_ID(),80);
		ListGridField USER_NAME = new ListGridField("USER_NAME",Util.TI18N.USER_NAME(),60);
		ListGridField DEFAULT_ORG_ID_NAME = new ListGridField("DEFAULT_ORG_ID_NAME",Util.TI18N.USER_ORG_ID(),100);
		ListGridField CUR_STATUS = new ListGridField("CUR_STATUS_NAME",Util.TI18N.CUR_STATUS(),60);
		ListGridField ROLE_ID_NAME = new ListGridField("ROLE_ID_NAME",Util.TI18N.ROLE_NAME(),100);
		ListGridField USER_GROUP = new ListGridField("USERGRP_ID_NAME",Util.TI18N.USER_GROUP(),100);
	    
		table.setFields(USER_ID,USER_NAME,DEFAULT_ORG_ID_NAME,CUR_STATUS, ROLE_ID_NAME, USER_GROUP);
	}
}
