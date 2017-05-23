package com.rd.client.view.tms;

import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.tms.UnShpmHeaderDS;
import com.rd.client.ds.tms.UnShpmSkuDS;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.TextMatchStyle;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.MinimizeClickEvent;
import com.smartgwt.client.widgets.events.MinimizeClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VLayout;

public class UnDispLoadWin extends Window{
	
	public Window window = null;
	public String load_no;
	@SuppressWarnings("unused")
	private String where;
	public SGTable loadTable; 
	private SGTable detailTable;
	private DataSource unshpmSkuDS;            //调度单数据源
	private DataSource unshpmHeaderDS;
	
	public UnDispLoadWin(String load_no) {
		// TODO Auto-generated constructor stub
		this.load_no=load_no;
	}

    public Window getViewPanel() {
		
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		unshpmSkuDS = UnShpmSkuDS.getInstance("V_UNSHPM_SKU");
		unshpmHeaderDS = UnShpmHeaderDS.getInstance("V_UNSHPM_HEADER");
		
		HStack hStack = new HStack();
		hStack.setWidth100();
		hStack.setHeight100();
		
		loadTable = new SGTable(unshpmHeaderDS, "100%", "92%", false, true, false) {
			
			@Override
			protected Canvas getExpansionComponent(ListGridRecord record) {
				VLayout layout = new VLayout();
				detailTable = new SGTable(unshpmSkuDS, "100%",
						"50", false, true, false);
				detailTable.setCanEdit(true);
				
				detailTable.setAlign(Alignment.RIGHT);
				detailTable.setShowRowNumbers(false);
				detailTable.setAutoFetchTextMatchStyle(TextMatchStyle.EXACT);
				detailTable.setAutoFitData(Autofit.VERTICAL);

				Criteria findValues = new Criteria();
				findValues.addCriteria("OP_FLAG", StaticRef.MOD_FLAG);
				findValues.addCriteria("SHPM_NO", record.getAttributeAsString("SHPM_NO"));
				ListGridField SKU_ID = new ListGridField("SKU",Util.TI18N.SKU_ID(),60);
				SKU_ID.setCanEdit(false);
				ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(),60);
				SKU_NAME.setCanEdit(false);
				ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(),60);
				SKU_SPEC.setCanEdit(false);
				ListGridField UOM = new ListGridField("UOM",Util.TI18N.UOM(),30);
				UOM.setCanEdit(false);
				ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.SHPM_QNTY(),50);
				Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
				QNTY.setCanEdit(false);
				
				ListGridField ODR_QNTY = new ListGridField("ODR_QNTY",Util.TI18N.ODR_QNTY(),60);
				ODR_QNTY.setCanEdit(false);
				Util.initFloatListField(ODR_QNTY, StaticRef.QNTY_FLOAT);
				
				ListGridField LD_QNTY = new ListGridField("LD_QNTY",Util.TI18N.LD_QNTY(),60);
				LD_QNTY.setCanEdit(false);
				Util.initFloatListField(LD_QNTY, StaticRef.QNTY_FLOAT);
				
				ListGridField UNLD_QNTY = new ListGridField("UNLD_QNTY",Util.TI18N.UNLD_QNTY(),60);
				Util.initFloatListField(UNLD_QNTY, StaticRef.QNTY_FLOAT);
				UNLD_QNTY.setCanEdit(false);
				
				ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),50);
				Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
				G_WGT.setCanEdit(false);
				
				ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),70);
				Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
				VOL.setCanEdit(false);
				
				ListGridField TOT_QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.R_EA(),50);
				TOT_QNTY_EACH.setAlign(Alignment.RIGHT); 
				TOT_QNTY_EACH.setCanEdit(false);
				Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
				
				ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),90);
				LOTATT02.setCanEdit(false);
				
				detailTable.setFields(SKU_ID,SKU_NAME,SKU_SPEC,UOM,ODR_QNTY,QNTY,TOT_QNTY_EACH,LD_QNTY,UNLD_QNTY,G_WGT,VOL,LOTATT02);
				detailTable.fetchData(findValues);
                layout.addMember(detailTable);
                layout.setLayoutLeftMargin(38);
				return layout;
			}
		};
		loadTable.setCanExpandRecords(true);
		loadTable.setCanEdit(false);
		loadTable.setShowRowNumbers(true);
		loadTable.setHeight("100%");
		loadTable.setShowFilterEditor(false);
		loadTable.setEditEvent(ListGridEditEvent.CLICK);
		
		//作业单编号 客户单号  提货点 货品代码  货品名称 货品规格 专供标示  批号  单位 数量 毛重 体积
		
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),100);//
		SHPM_NO.setCanEdit(false);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),70);//
		CUSTOM_ODR_NO.setCanEdit(false);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME",Util.TI18N.LOAD_NAME(),130);//
		LOAD_NAME.setCanEdit(false);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),130);//
		UNLOAD_NAME.setCanEdit(false);
		ListGridField STATUS_NAME = new ListGridField("STATUS_NAME",Util.TI18N.SHPM_STSTUS(),100);//
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY",Util.TI18N.QNTY(),120);// 订单数量
		TOT_QNTY.setCanEdit(false);
		TOT_QNTY.setAlign(Alignment.RIGHT);
		ListGridField TOT_QNTY_EACH = new ListGridField("TOT_QNTY_EACH",Util.TI18N.TOT_QNTY_EACH(),120);// 订单数量
		TOT_QNTY_EACH.setCanEdit(false);
		TOT_QNTY_EACH.setAlign(Alignment.RIGHT);
		
		Util.initFloatListField(TOT_QNTY, StaticRef.QNTY_FLOAT);
		Util.initFloatListField(TOT_QNTY_EACH, StaticRef.QNTY_FLOAT);
	
		loadTable.setFields(SHPM_NO,CUSTOM_ODR_NO,LOAD_NAME,UNLOAD_NAME,STATUS_NAME,TOT_QNTY,TOT_QNTY_EACH);
		
		Criteria crit = new Criteria();
		crit.addCriteria("OP_FLAG", "M");
		crit.addCriteria("GET_GROUP",load_no);
		loadTable.fetchData(crit);
		loadTable.invalidateCache();
		hStack.addMember(loadTable);
		layout.addMember(hStack);
		
		window = new Window();
		window.setTitle("调度单相关货品信息");//调度单货品信息   Util.TI18N.TRANS_SHMP_LIST()
		window.setLeft("11%");
		window.setTop("77%");
		window.setWidth(900);
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
