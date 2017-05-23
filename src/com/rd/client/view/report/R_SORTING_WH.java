package com.rd.client.view.report;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.report.R_SORTING_WHDS;
import com.rd.client.reflection.ClassForNameAble;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
@ClassForNameAble
public class R_SORTING_WH extends SGForm implements PanelFactory{
	
	private DataSource ds;
	private SGTable table;
	private SectionStack section;
	

	/*public R_SORTING_WH(String id) {
		super(id);
	}*/

	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		ToolStrip toolStrip = new ToolStrip();//按钮布局
		toolStrip.setAlign(Alignment.RIGHT);
		ds=R_SORTING_WHDS.getInstance("SORTING_WH","R_SORTING_WH");

		table = new SGTable(ds, "100%", "70%");
		
        createListFields(table);
        table.setShowFilterEditor(false);
        Criteria criteria = new Criteria();
		criteria.addCriteria("OP_FLAG", "M");
		table.fetchData(criteria,new DSCallback() {
			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if(table.getRecords().length > 0){
					table.selectRecord(0);
				}
			}
		});
       
		
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
	//布局列表信息按钮
	private void createListFields(SGTable table) {
		
		ListGridField CUSTOM_ODR_NO = new ListGridField("CUSTOM_ODR_NO",Util.TI18N.CUSTOM_ODR_NO(),80);
		ListGridField SHPM_NO = new ListGridField("SHPM_NO",Util.TI18N.SHPM_NO(),100);
		ListGridField SKU = new ListGridField("SKU",Util.TI18N.SKU(), 60);
  		ListGridField SKU_NAME = new ListGridField("SKU_NAME",Util.TI18N.SKU_NAME(), 130);
	    ListGridField SKU_SPEC = new ListGridField("SKU_SPEC",Util.TI18N.SKU_SPEC(), 60);
  		ListGridField UOM = new ListGridField("UOM",Util.TI18N.UNIT(),40);
  		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY(),60);
  		ListGridField QNTY_EACH = new ListGridField("QNTY_EACH",Util.TI18N.QNTY_EACH(),60);
  		ListGridField VOL = new ListGridField("VOL",Util.TI18N.VOL(),60);
  		ListGridField G_WGT = new ListGridField("G_WGT",Util.TI18N.G_WGT(),60);
  		ListGridField LOTATT01 =new ListGridField("LOTATT01",Util.TI18N.LOTATT01(),50);
  		ListGridField LOTATT02 =new ListGridField("LOTATT02",Util.TI18N.LOTATT02(),60);
  		ListGridField UNLOAD_NAME =new ListGridField("UNLOAD_NAME",Util.TI18N.UNLOAD_NAME(),80);
  		ListGridField SUPLR_NAME =new ListGridField("SUPLR_NAME",Util.TI18N.SUPLR_NAME(),60);
  		
  		Util.initFloatListField(G_WGT, StaticRef.GWT_FLOAT);
  		Util.initFloatListField(VOL, StaticRef.VOL_FLOAT);
  		Util.initFloatListField(QNTY, StaticRef.QNTY_FLOAT);
  		Util.initFloatListField(QNTY_EACH, StaticRef.QNTY_FLOAT);
  		
		table.setFields(CUSTOM_ODR_NO,SHPM_NO,SKU,SKU_NAME,SKU_SPEC,UOM,QNTY,QNTY_EACH,VOL,
				G_WGT,LOTATT01,LOTATT02,UNLOAD_NAME,SUPLR_NAME);
	
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		//组件按钮
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(12);
		toolStrip.addSeparator();
		 
        //导出按钮
        IButton expButton = createBtn(StaticRef.EXPORT_BTN);
        expButton.addClickHandler(new ExportAction(table, "addtime desc"));
    
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(expButton);
  
	}

	@Override
	public void createForm(DynamicForm form) {
		
	}

	@Override
	public void initVerify() {
		
	}

	@Override
	public void onDestroy() {
		
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		R_SORTING_WH view = new R_SORTING_WH();
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