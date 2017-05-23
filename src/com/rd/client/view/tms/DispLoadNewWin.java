package com.rd.client.view.tms;

import com.rd.client.action.tms.dispatch.SplitByLoadQntyAction;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.TrsPrivRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.ShpmSkuDS;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.events.ShowContextMenuEvent;
import com.smartgwt.client.widgets.events.ShowContextMenuHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItem;

public class DispLoadNewWin extends Window{
	
	public Window window = null;
	public String load_no;
	@SuppressWarnings("unused")
	private String where;
	public SGTable loadTable; 
	private DataSource shpmSkuDS;            //调度单数据源
	private VehicleDispatchView view;
	private String history_flag;
	
	public DispLoadNewWin(VehicleDispatchView view,String load_no) {
		this.view = view;
		this.load_no=load_no;
		this.history_flag = "false";
	}
	
	public DispLoadNewWin(VehicleDispatchView view,String load_no,String h_flag) {
		this.view = view;
		this.load_no=load_no;
		this.history_flag = h_flag;
	}

    public Window getViewPanel() {
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		shpmSkuDS = ShpmSkuDS.getInstance("V_SHPM_SKU");
		
		HStack hStack = new HStack();
		hStack.setWidth100();
		hStack.setHeight100();
		
		loadTable = new SGTable(shpmSkuDS);
		loadTable.setHeight("100%");
		loadTable.setShowFilterEditor(false);
		loadTable.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		
		//作业单编号 客户单号  提货点 货品代码  货品名称 货品规格 专供标示  批号  单位 数量 毛重 体积
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),100);//
		SHPM_NO.setCanEdit(false);
		ListGridField SHPM_ROW = new ListGridField("SHPM_ROW",Util.TI18N.SHPM_ROW(),60);//
		SHPM_ROW.setCanEdit(false);
		ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(),70);//货品代码
		SKU.setCanEdit(false);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),90);//货品名称
		SKU_NAME.setCanEdit(false);
		ListGridField TEMPERATURE = new ListGridField("TEMPERATURE1",Util.TI18N.TEMPERATURE(),52);//规格型号
		TEMPERATURE.setCanEdit(false);
		
		final ListGridField LOTATT01 = new ListGridField("LOTATT01",Util.TI18N.LOTATT01(),60);// 批号 
		final ListGridField LOTATT02 = new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);//专供标识

		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY(),60);// 订单数量
		QNTY.setCanEdit(true);
		QNTY.setAlign(Alignment.RIGHT);
		
		//毛重[吨]，体积[方]		
		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),60);//重量				
		G_WGT.setAlign(Alignment.RIGHT);
		G_WGT.setCanEdit(false);
		ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),60);//  体积						
		VOL.setAlign(Alignment.RIGHT);	
		VOL.setCanEdit(false);
		
		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
	
		loadTable.setFields(SHPM_NO,SKU,SKU_NAME,TEMPERATURE,QNTY,VOL,G_WGT,LOTATT01,LOTATT02);
		
		final Menu load_menu = new Menu();   //调度单右键
		load_menu.setWidth(140);
		 if(view.isPrivilege(TrsPrivRef.DISPATCH_P2_21)) {
     	    MenuItem saveItem = new MenuItem(Util.BI18N.SPLITBYLDQNTY(),StaticRef.ICON_CONFIRM);
     	    saveItem.setKeyTitle("Alt+T");
     	    KeyIdentifier saveKey = new KeyIdentifier();
     	    saveKey.setAltKey(true);
     	    saveKey.setKeyName("T");
     	    saveItem.setKeys(saveKey);
     	    load_menu.addItem(saveItem);
     	    saveItem.addClickHandler(new SplitByLoadQntyAction(loadTable));
 	    } 
		loadTable.addShowContextMenuHandler(new ShowContextMenuHandler() {
             public void onShowContextMenu(ShowContextMenuEvent event) {
             	load_menu.showContextMenu();
                 event.cancel();
             }
         });
		
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("LOAD_NO",load_no);
		crit.addCriteria("HISTORY_FLAG",history_flag);
		
		loadTable.fetchData(crit);
		loadTable.invalidateCache();
		hStack.addMember(loadTable);
		layout.addMember(hStack);
		
		window = new Window();
		window.setTitle("调度单货品信息");//调度单货品信息   Util.TI18N.TRANS_SHMP_LIST()
		window.setLeft("13%");
		window.setTop("60%");
		window.setWidth(840);
		window.setHeight(180);
		window.setCanDragReposition(true);
		window.setCanDragResize(true);
		
		window.addItem(layout);
		
		window.setShowCloseButton(true);
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
