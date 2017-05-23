package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.widgets.SGDate;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.report.RecPayCompareDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.rd.client.win.SuplrPayWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * BMS-报表管理-收付款比较表
 *
 */
@ClassForNameAble
public class RecPayCompareView extends SGForm implements PanelFactory {
	public SGTable table;
	private DataSource ds;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	@Override
	public Canvas getViewPanel() {
		
		//页面总布局
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		
		ds = RecPayCompareDS.getInstance("B_RECPAYCOMPARE");
		
		//放置按钮
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		//设置详细信息布局
		HStack stack = new HStack();
		stack.setWidth100();
		stack.setHeight100();
		
		table= new SGTable(ds, "100%", "100%");
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		createFields(table);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		
		//创建Section
		section = new SectionStack();
		section.setWidth("100%");  
		section.setHeight("100%");
		
		final SectionStackSection listItem = new SectionStackSection("列表信息");//
	    listItem.setItems(table);
	    listItem.setExpanded(true);
	    listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
		
	    main.setWidth100();
        main.setHeight100();
        main.addMember(toolStrip);
		main.addMember(section);
        
		return main;
	}

	private void createFields(SGTable table) {
	
		ListGridField ODR_NO = new ListGridField("ODR_NO","订单",120);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","车次",100);
		ListGridField SHORT_NAME = new ListGridField("SHORT_NAME","项目名称",100);			
		ListGridField CUSTOMER_NAME = new ListGridField("CUSTOMER_NAME","客户名称",120);
		ListGridField TRANS_TYP_NAME  = new ListGridField("TRANS_TYP_NAME ","运输类型",60);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME","运输产品",60);			
		ListGridField SHPM_NO = new ListGridField("SHPM_NO","内单号",120);
		ListGridField REFENENCE1 = new ListGridField("REFENENCE1","运单号",120);
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO","订单号",120);			
		ListGridField LOAD_DATE = new ListGridField("LOAD_DATE","发货日期",80);
		ListGridField UNLOAD_DATE = new ListGridField("UNLOAD_DATE","到货日期",80);
		ListGridField LOAD_NAME = new ListGridField("LOAD_NAME","出发地",100);			
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","到达地",100);
		ListGridField NOTES1 = new ListGridField("NOTES1","备注",130);
		ListGridField TOT_QNTY1 = new ListGridField("TOT_QNTY1","托盘",60);			
		ListGridField TOT_QNTY = new ListGridField("TOT_QNTY","箱数（箱）",60);
		ListGridField TOT_GROSS_W = new ListGridField("TOT_GROSS_W","吨位（T）",65);
		ListGridField TOT_VOL = new ListGridField("TOT_VOL","立数(m³)",65);			
		ListGridField VEHICLE_TYP_NAME1 = new ListGridField("VEHICLE_TYP_NAME1","车型（M）",80);
		ListGridField VEHICLE_TYP_NAME2 = new ListGridField("VEHICLE_TYP_NAME2","实际车型",80);
		ListGridField VEHICLE_ATTR_NAME = new ListGridField("VEHICLE_ATTR_NAME","车辆类型",80);			
		ListGridField VEHICLE_LEN = new ListGridField("VEHICLE_LEN","统计车型",80);
		ListGridField TEMPERATURE1 = new ListGridField("TEMPERATURE1","温度要求",80);
		ListGridField NOTES2 = new ListGridField("NOTES2","注意事项",80);	
		ListGridField SUPLR_ATTR_NAME = new ListGridField("SUPLR_ATTR_NAME","承运性质",80);
		ListGridField SUPLR_CLS_NAME = new ListGridField("SUPLR_CLS_NAME","承运商名称",80);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",75);			
		ListGridField DRIVER1 = new ListGridField("DRIVER1","驾驶员",65);
		ListGridField MOBILE1 = new ListGridField("MOBILE1","联系方式",75);
		ListGridField TRANS_FEE = new ListGridField("TRANS_FEE","外发运费",60);			
		ListGridField OTHER_FEE = new ListGridField("OTHER_FEE","额外费用",60);
		ListGridField OTHER_FEE_NOTES = new ListGridField("OTHER_FEE_NOTES","额外费用说明",85);
		ListGridField REC_AMOUNT = new ListGridField("REC_AMOUNT","AR收款",60);			
		ListGridField PAY_AMOUNT1 = new ListGridField("PAY_AMOUNT1","外租车",60);
		ListGridField PAY_AMOUNT2 = new ListGridField("PAY_AMOUNT2","激励车",60);
		ListGridField PAY_AMOUNT3 = new ListGridField("PAY_AMOUNT3","自有车",60);			
		ListGridField DIFF_AMOUNT = new ListGridField("DIFF_AMOUNT","收入成本匹配",80);
		ListGridField NOTES3 = new ListGridField("NOTES3","备注",80);
	
		table.setFields(ODR_NO,LOAD_NO,SHORT_NAME,CUSTOMER_NAME,TRANS_TYP_NAME,SKU_NAME,
				SHPM_NO,REFENENCE1,CUSTOM_ODR_NO,LOAD_DATE,UNLOAD_DATE,LOAD_NAME,
				UNLOAD_NAME,NOTES1,TOT_QNTY1,TOT_QNTY,TOT_GROSS_W,TOT_VOL,
				VEHICLE_TYP_NAME1,VEHICLE_TYP_NAME2,VEHICLE_ATTR_NAME,VEHICLE_LEN,
				TEMPERATURE1,NOTES2,SUPLR_ATTR_NAME,SUPLR_CLS_NAME,PLATE_NO,DRIVER1,
				MOBILE1,TRANS_FEE,OTHER_FEE,OTHER_FEE_NOTES,REC_AMOUNT,PAY_AMOUNT1,
				PAY_AMOUNT2,PAY_AMOUNT3,DIFF_AMOUNT,NOTES3);		
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {

		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		IButton searchButton=createBtn(StaticRef.FETCH_BTN);//查询
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			   if(searchWin==null){
				   searchForm=new SGPanel();
					searchWin = new SearchWin( ds, createSerchForm(searchForm),
							section.getSection(0)).getViewPanel();
					searchWin.setWidth(600);
					searchWin.setHeight(320);
				}else{
					searchWin.show();
				}
				
			}
		});
		
		 //导出按钮
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
        
		toolStrip.setMembersMargin(5);
		toolStrip.setMembers(searchButton,expButton);
		
	}

	protected DynamicForm createSerchForm(final SGPanel form) {
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);
		
		SGText SUPLR_NAME = new SGText("SUPLR_NAME","承运商");
    	PickerIcon searchPicker = new PickerIcon(PickerIcon.SEARCH);
    	
		searchPicker.addFormItemClickHandler(new FormItemClickHandler() {
			
			@Override
			public void onFormItemClick(FormItemIconClickEvent event) {
				 new SuplrPayWin(form,"20%","50%").getViewPanel();		
			}
		});
		
		SUPLR_NAME.setIcons(searchPicker);
		
		SGText SUPLR_ID = new SGText("SUPLR_ID","承运商");
		SUPLR_ID.setVisible(false);
		
		SGDate LOAD_DATE_FROM = new SGDate("LOAD_DATE_FROM", "发运时间");
		SGDate LOAD_DATE_TO = new SGDate("LOAD_DATE_TO", "到");

		
		form.setItems(SUPLR_ID,SUPLR_NAME,LOAD_DATE_FROM,LOAD_DATE_TO);
		
		return form;
		
	}

	
	@Override
	public void createForm(DynamicForm form) {

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
		RecPayCompareView view = new RecPayCompareView();
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
