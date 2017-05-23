package com.rd.client.win;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class ListWin extends Window{

	private int width = 0;
	private int height = 0;
	private String title = "查询条件";
	private Window window = null;
	private SGTable r_table;
	private SGTable l_table;
	private DynamicForm searchForm;
	private Label left_lab;
	private Label right_lab;
	public ListWin(int p_width, int p_height, String p_title) {
		this.width = p_width;
		this.height = p_height;
		this.title = p_title;
	}
	
	public ListWin(SGTable table1, SGTable table2, int p_width, int p_height, Label p_lab1, Label p_lab2) {
		this.width = p_width;
		this.height = p_height;
		this.r_table = table1;
		this.l_table = table2;
		this.left_lab = p_lab1;
		this.right_lab = p_lab2;
	}

	public void createBtnWidget(ToolStrip strip) {
	}

	public void createForm(DynamicForm form) {
		TextItem txt_global = new TextItem("CONTENT");
		txt_global.setTitleOrientation(TitleOrientation.LEFT);
		txt_global.setWidth(216);
		txt_global.setColSpan(3);
		txt_global.setEndRow(false);
		txt_global.setShowTitle(false);
		txt_global.setAlign(Alignment.RIGHT);
		
		ButtonItem searchItem = new ButtonItem(Util.BI18N.SEARCH());
		searchItem.setIcon(StaticRef.ICON_SEARCH);
		searchItem.setWidth(70);
		searchItem.setColSpan(1);
		searchItem.setStartRow(false);
		searchItem.setEndRow(false);
		ButtonItem clearItem = new ButtonItem(Util.BI18N.CLEAR());
		clearItem.setIcon(StaticRef.ICON_CANCEL);
		clearItem.setWidth(70);
		clearItem.setColSpan(1);
		clearItem.setStartRow(false);
		clearItem.setEndRow(true);
		
		final ComboBoxItem view_name = new ComboBoxItem("VIEW_NAME");
		view_name.setTitle("<font style=\"color: #ff0000;font-size:12px;\">视图名</font>");
		Util.initCodesComboValue(view_name, " where PROP_CODE = 'LIST_TYP'");
		view_name.setTitleOrientation(TitleOrientation.LEFT);
		
		final ComboBoxItem func_model = new ComboBoxItem("FUNC_MODEL", "<font style=\"color: #ff0000;font-size:12px;\">功能模块</font>");
		Util.initCodesComboValue(func_model, " where PROP_CODE = 'FUN_MOD'");
		func_model.setTitleOrientation(TitleOrientation.LEFT);
		func_model.setColSpan(3);
		func_model.setEndRow(true);
		
		searchForm.setItems(txt_global, searchItem, clearItem, view_name, func_model);
		searchItem.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Criteria criteria = new Criteria();
				criteria.addCriteria(searchForm.getValuesAsCriteria());
				criteria.addCriteria("OP_FLAG", r_table.OP_FLAG);
				r_table.setCriteria(criteria);
				r_table.fetchData(criteria);
				l_table.fetchData(criteria);
				window.hide();
				left_lab.setContents("<font style=\"color: #ff0000;font-size:12px;\">功能模块->" + func_model.getDisplayValue() + "</font>");
				right_lab.setContents("<font style=\"color: #ff0000;font-size:12px;\">视图名->" + view_name.getDisplayValue() + "</font>");

			}			
		});
	}

	public Window getViewPanel() {
		
		searchForm = new DynamicForm();
		searchForm.setHeight(height);
		searchForm.setWidth(width);
		searchForm.setNumCols(6);
		searchForm.setPadding(5);
		searchForm.setAlign(Alignment.LEFT);
		createForm(searchForm);
		
		window = new Window(); 
		window.setAutoSize(true);
		window.setTitle(title);
		window.setLeft("20%");
		window.setTop("25%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);  
		window.setCanDragResize(true);  
		window.addItem(searchForm);
		window.setShowCloseButton(false);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.hide();
			}
			
		});
		
		return window;
	}
}
