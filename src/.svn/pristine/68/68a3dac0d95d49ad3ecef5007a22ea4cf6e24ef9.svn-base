package com.rd.client.view.tms;

import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class OrdManageWin extends Window{
	private int width;
	private int height;
	private String title;
	public ButtonItem confirmItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	public SectionStackSection section;

	public OrdManageWin(SGTable p_table, ButtonItem splitItem, int p_width,
			int p_height, String p_title) {
		this.width = p_width;
		this.height = p_height;
		this.title = p_title;
		this.confirmItem = splitItem;
		this.table = p_table;
	}

	public OrdManageWin(ButtonItem splitItem, int p_width, int p_height,
			DataSource ds, DynamicForm form) {
		this.width = p_width;
		this.height = p_height;
		this.confirmItem = splitItem;
		this.form = form;
		this.ds = ds;
	}

	public OrdManageWin(ButtonItem splitItem, DataSource ds, DynamicForm form) {
		this.confirmItem = splitItem;
		this.form = form;
		this.ds = ds;
	}

	public OrdManageWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}

	public OrdManageWin(int p_width, int p_height, DataSource ds, DynamicForm form,
			SectionStackSection p_section) {
		this.width = p_width;
		this.height = p_height;
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}

	public void createBtnWidget(ToolStrip strip) {

	}

	public void createForm(DynamicForm searchForm) {
		
		ButtonItem confirmItem = new ButtonItem(Util.BI18N.CONFIRM());//
		confirmItem.setIcon(StaticRef.ICON_SAVE);
		confirmItem.setTitleOrientation(TitleOrientation.TOP);
		confirmItem.setColSpan(1);
		confirmItem.setStartRow(false);
		confirmItem.setEndRow(false);
		confirmItem.setAutoFit(true);

		ButtonItem cancelItem = new ButtonItem(Util.BI18N.CANCEL());//取消
		cancelItem.setIcon(StaticRef.ICON_SAVE);
		cancelItem.setColSpan(1);
		cancelItem.setAutoFit(true);
		cancelItem.setStartRow(false);
		cancelItem.setEndRow(false);

		mainItem = new SGPanel();
		mainItem.setItems(confirmItem,cancelItem);
		mainItem.setBackgroundColor(ColorUtil.BG_COLOR);
	}

	public Window getViewPanel() {

		
		/**
		 *    
		 */
	
		//1
		SGDate POD_TIME = new SGDate("POD_TIME", Util.TI18N.FOLLOW_POD_TIME());//实际回单时间
		POD_TIME.setTitle(ColorUtil.getRedTitle(Util.TI18N.FOLLOW_POD_TIME())); //
		SGCombo POD_DELAY_REASON = new SGCombo("POD_DELAY_REASON",Util.TI18N.POD_DELAY_REASON());//回单延迟原因 

		// 5：备注
		TextAreaItem notes = new TextAreaItem("NOTES", Util.TI18N.NOTES());
		notes.setStartRow(true);
		notes.setColSpan(7);
		notes.setHeight(60);
		notes.setWidth(400);
		notes.setTitleOrientation(TitleOrientation.TOP);
		notes.setTitleVAlign(VerticalAlignment.TOP);

		form = new SGPanel();
		form.setItems(POD_TIME,POD_DELAY_REASON,notes);
		
		form.setHeight(height / 2);
		form.setWidth(width - 20);
		form.setNumCols(7);
		form.setPadding(5);
		form.setTitleWidth(75);
		form.setAlign(Alignment.LEFT);
		form.setHeight("35%");
		createForm(form);
		
		TabSet tabSet=new TabSet();
		tabSet.setWidth("100%");
		tabSet.setHeight("100%");
		
		Tab tab=new Tab(Util.TI18N.ORDER_OPERATION());//回单操作
		tab.setPane(form);
		tabSet.addTab(tab);
	
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(tabSet);

		window = new Window();
		window.setTitle(title);
		window.setLeft("30%");
		window.setTop("35%");
		window.setWidth(550);
		window.setHeight(250);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(lay1);
		window.addItem(mainItem);

		window.setShowCloseButton(false);
		window.show();
		window.addMinimizeClickHandler(new MinimizeClickHandler() {

			@Override
			public void onMinimizeClick(MinimizeClickEvent event) {
				window.setMinimized(false);
				window.hide();
				event.cancel();
			}

		});

		return window;
	}
}
