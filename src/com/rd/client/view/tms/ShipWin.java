package com.rd.client.view.tms;

import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGSText;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public class ShipWin extends Window {

	private int width;
	private int height;
	private String title = "窗口";
	public ButtonItem splitItem;
	public Window window = null;
	public DynamicForm form;
	public ListGrid table;
	public DynamicForm mainItem;
	public DataSource ds;
	public SectionStackSection section;
	private SGTable groupTable;

	public ShipWin(SGTable p_table, ButtonItem splitItem, int p_width,
			int p_height, String p_title) {
		this.width = p_width;
		this.height = p_height;
		this.title = p_title;
		this.splitItem = splitItem;
		this.table = p_table;
	}

	public ShipWin(ButtonItem splitItem, int p_width, int p_height,
			DataSource ds, DynamicForm form) {
		this.width = p_width;
		this.height = p_height;
		this.splitItem = splitItem;
		this.form = form;
		this.ds = ds;
	}

	public ShipWin(ButtonItem splitItem, DataSource ds, DynamicForm form) {
		this.splitItem = splitItem;
		this.form = form;
		this.ds = ds;
	}

	public ShipWin(DataSource ds, DynamicForm form,
			SectionStackSection p_section) {
		this.form = form;
		this.ds = ds;
		this.section = p_section;
	}

	public ShipWin(int p_width, int p_height, DataSource ds, DynamicForm form,
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
		
		ButtonItem splitItem = new ButtonItem(Util.BI18N.SPLIT());//拆分
		splitItem.setIcon(StaticRef.ICON_SAVE);
		splitItem.setTitleOrientation(TitleOrientation.TOP);
		splitItem.setColSpan(1);
		splitItem.setStartRow(false);
		splitItem.setEndRow(false);
		splitItem.setAutoFit(true);

		ButtonItem cancelItem = new ButtonItem(Util.BI18N.CANCELSPLIT());//取消拆分
		cancelItem.setIcon(StaticRef.ICON_SAVE);
		cancelItem.setColSpan(1);
		cancelItem.setAutoFit(true);
		cancelItem.setStartRow(false);
		cancelItem.setEndRow(false);

		ButtonItem moveItem = new ButtonItem(Util.BI18N.MOVEDETAIL());//移除明细
		moveItem.setIcon(StaticRef.ICON_SAVE);
		moveItem.setColSpan(1);
		moveItem.setAutoFit(true);
		moveItem.setStartRow(false);
		moveItem.setEndRow(false);
	
		mainItem = new SGPanel();
		mainItem.setItems(splitItem,cancelItem,moveItem);
		mainItem.setBackgroundColor(ColorUtil.BG_COLOR);
	}

	public Window getViewPanel() {
		
		/**
		   1行：调度单号（LOAD_NO），作业单号（SHPM_NO），客户单号（CUSTOM_ODR_NO）
           2行：发货区域（LOAD_AREA_NAME）,发货方(LOAD_NAME),收货区域（UNLOAD_AREA_NAME）,收货方（UNLOAD_NAME）
           3行：执行机构（EXEC_ORG_ID），数量（TOT_QNTY）,毛重（TOT_GROSS_W），体积(TOT_VOL)
		 */
		//1
		SGText LOAD_NO=new SGText("LOAD_NO", Util.TI18N.LOAD_NO());
		LOAD_NO.setWidth(116);
		LOAD_NO.setColSpan(2);
		SGText SHPM_NO=new SGText("SHPM_NO", Util.TI18N.SHPM_NO());
		SHPM_NO.setWidth(116);
		SHPM_NO.setColSpan(2);
		SGText CUSTOM_ODR_NO=new SGText("CUSTOM_ODR_NO", Util.TI18N.CUSTOM_ODR_NO(),true);
		CUSTOM_ODR_NO.setWidth(116);
		CUSTOM_ODR_NO.setColSpan(2);
		SGText EXEC_ORG_ID=new SGText("EXEC_ORG_ID", Util.TI18N.EXEC_ORG_ID());
		EXEC_ORG_ID.setWidth(116);
		EXEC_ORG_ID.setColSpan(2);
		//2
		SGText LOAD_AREA_NAME = new SGText("LOAD_AREA_NAME", Util.TI18N.LOAD_AREA_NAME());
		LOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_AREA_NAME.setWidth(116);
		LOAD_AREA_NAME.setColSpan(2);
		
		SGText LOAD_NAME = new SGText("LOAD_NAME", Util.TI18N.LOAD_NAME());
		LOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		LOAD_NAME.setColSpan(3);
		LOAD_NAME.setWidth(190);
		
		SGText UNLOAD_AREA_NAME = new SGText("UNLOAD_AREA_NAME", Util.TI18N.UNLOAD_AREA_NAME());
		UNLOAD_AREA_NAME.setTitleOrientation(TitleOrientation.TOP);
		UNLOAD_AREA_NAME.setWidth(116);
		UNLOAD_AREA_NAME.setColSpan(2);
		
		SGText UNLOAD_NAME = new SGText("UNLOAD_NAME", Util.TI18N.UNLOAD_NAME());
		UNLOAD_NAME.setTitleOrientation(TitleOrientation.TOP);
		UNLOAD_NAME.setColSpan(3);
		UNLOAD_NAME.setWidth(190);
		
		//3
		
		SGSText TOT_QNTY=new SGSText("TOT_QNTY", Util.TI18N.QNTY(),true);
		TOT_QNTY.setColSpan(2);
		//TOT_QNTY.setWidth(50);
		SGSText TOT_GROSS_W=new SGSText("TOT_GROSS_W", Util.TI18N.PACK_WEIGHT());
		TOT_GROSS_W.setColSpan(2);
		//TOT_GROSS_W.setWidth(50);
		
		SGSText TOT_VOL=new SGSText("TOT_VOL", Util.TI18N.VOL());
		TOT_VOL.setColSpan(2);
		//TOT_VOL.setWidth(78);
		form = new SGPanel(); //
		form.setItems(LOAD_NO, SHPM_NO,LOAD_AREA_NAME,LOAD_NAME,CUSTOM_ODR_NO,EXEC_ORG_ID,
				UNLOAD_AREA_NAME,UNLOAD_NAME,TOT_QNTY,TOT_GROSS_W,TOT_VOL);// //
		form.setHeight(height);
		form.setWidth(width);
		form.setNumCols(14);
//		form.setPadding(9);
//		form.setTitleWidth(75);
//		form.setAlign(Alignment.LEFT);
		form.setHeight("35%");
		createForm(form);
		
		//列表
		groupTable = new SGTable(ds);
		groupTable.setHeight("60%");
		groupTable.setShowFilterEditor(false);
		groupTable.setEditEvent(ListGridEditEvent.CLICK);
		groupTable.setShowRowNumbers(false);
		ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.ORD_ROW(), 50);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(), 110);
		ListGridField UOM_NAME = new ListGridField("UOM_NAME",Util.TI18N.UNIT(),70);
		ListGridField ODR_QNTY = new ListGridField("ODR_QNTY",Util.TI18N.ODR_QNTY(),70);
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY(),70);
		ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),70);
		ListGridField N_WGT = new ListGridField("N_WGT",Util.TI18N.N_WGT(),70);
		ListGridField SP_QNTY = new ListGridField("SP_QNTY",Util.TI18N.QNTY(),70);
		ListGridField SP_VOL = new ListGridField("SP_VOL",Util.TI18N.VOL(),70);
		ListGridField SP_GWGT = new ListGridField("SP_GWGT",Util.TI18N.G_WGT(),70);
		ListGridField SP_NWGT = new ListGridField("SP_NWGT",Util.TI18N.N_WGT(),70);
		
		groupTable.setFields(SHPM_ROW,SKU_NAME, UOM_NAME, ODR_QNTY, 
				QNTY,VOL, G_WGT, N_WGT, SP_QNTY,SP_VOL,SP_GWGT,SP_NWGT);
		
  
		VLayout lay1 = new VLayout();
		lay1.setWidth100();
		lay1.setHeight100();
		lay1.addMember(form);
		lay1.addMember(groupTable);

		window = new Window();
		window.setTitle(title);
		window.setLeft("15%");
		window.setTop("25%");
		window.setWidth(900);
		window.setHeight(410);
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
