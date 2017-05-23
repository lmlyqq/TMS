package com.rd.client.win;

import com.rd.client.action.system.ListConfigSaveAction;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGButtonItem;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.ModelDS;
import com.rd.client.view.system.ListConfigView;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * 系统管理->列表配置里指定模板
 * @author yuanlei
 *
 */
public class ListCfgWin extends Window{
	
	private int width = 240;
	private int height = 400;
	private String title = "请勾选需配置的功能模块";
	private SGTable skuTable;
	private DynamicForm searchForm;
	public Window window;
	private DataSource modelListDS;
	private SGTable listTable;
	private int seq;
	public String new_view_name;
	public String new_func_model;
	public ListConfigView view;
	public ListCfgWin(SGTable p_table, int p_seq, ListConfigView p_view) {
		listTable = p_table;
		seq = p_seq;
		view = p_view;
	}
	
	public ListCfgWin(SGTable p_table, int p_seq, String p_view_name, ListConfigView p_view) {
		listTable = p_table;
		seq = p_seq;
		new_view_name = p_view_name;
		view = p_view;
	}
	
	public void createBtnWidget(ToolStrip strip) {
	}

	public void createForm(DynamicForm searchForm) {
		
	}

	public Window getViewPanel() {
		
		searchForm = new DynamicForm();
		searchForm.setHeight(30);
		searchForm.setNumCols(5);
		searchForm.setPadding(5);
		searchForm.setAlign(Alignment.LEFT);
		modelListDS = ModelDS.getInstance("BAS_CODES", "BAS_CODES");
		
		TextItem txt_global = new TextItem("FULL_INDEX", "");
		txt_global.setShowTitle(false);
		txt_global.setWidth(160);
		txt_global.setColSpan(3);
		txt_global.setAlign(Alignment.RIGHT);
		SGButtonItem searchBtn = new SGButtonItem(StaticRef.FETCH_BTN);
		
        searchForm.setItems(txt_global,searchBtn);
		
        skuTable = new SGTable(modelListDS,"100%","100%",false,true,false);
        Criteria criteria = new Criteria();
        criteria.addCriteria("PROP_CODE", "FUN_MOD");
        criteria.addCriteria("OP_FLAG", "M");
        if(seq == 2) {
        	criteria.addCriteria("WHERE", " and ID != '" + JSOHelper.getAttribute(listTable.getJsObj(), "FUNC_MODEL") + "'");
        }
        skuTable.setSelectionAppearance(SelectionAppearance.CHECKBOX);
        
        createListField(skuTable);
        skuTable.setDataSource(modelListDS);
        skuTable.fetchData(criteria);
        
        ButtonItem confirm = new ButtonItem(Util.BI18N.CONFIRM());
        confirm.setIcon(StaticRef.ICON_SAVE);
        confirm.setColSpan(1);
        confirm.setStartRow(false);
		confirm.setEndRow(false);
		confirm.setAutoFit(true);
		confirm.addClickHandler(new ListConfigSaveAction(skuTable, listTable, this, seq));
        
        ButtonItem cancel = new ButtonItem(Util.BI18N.CANCEL());
        cancel.setIcon(StaticRef.ICON_CANCEL);
        cancel.setColSpan(1);
        cancel.setAutoFit(true);
        cancel.setStartRow(false);
        cancel.setEndRow(true);
        cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				window.clear();
			}
        	
        });
        
        DynamicForm mainSearch = new DynamicForm();
        mainSearch.setHeight(30);
		mainSearch.setCellPadding(5);
		mainSearch.setNumCols(5);
		mainSearch.setItems(confirm,cancel);
		mainSearch.setBackgroundColor(ColorUtil.BG_COLOR);
        
		window = new Window();
		window.setTitle(title);
		window.setLeft("35%");
		window.setTop("26%");
		window.setWidth(width);  
		window.setHeight(height); 
		window.setCanDragReposition(true);
		window.addItem(searchForm);
		window.addItem(skuTable);
		window.addItem(mainSearch);
		window.show();
		
		return window;
	}
	public void createListField(SGTable table) {
		ListGridField sku_Id = new ListGridField("NAME_C", "模块名称",160);
		table.setFields(sku_Id);
	}
}
