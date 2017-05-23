package com.rd.client.view.tms;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.obj.FormUtil;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGDateTime;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.tms.DamageViewDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 客户服务 -->货损货差
 * @author lijun
 *
 */
@ClassForNameAble
public class DamageView extends SGForm implements PanelFactory {
	private DataSource DS;
	private SGTable Table;
	private SectionStack section;
	private Window searchWin;
	private SGPanel searchForm;
	
	/*public DamageView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(fid);
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		
		DS = DamageViewDS.getInstance("R_LOSS_DAMAGE_", "TRANS_LOSS_DAMAGE");
		Table = new SGTable(DS,"100%","70%");
		Table.setCanEdit(false);
		Table.setShowFilterEditor(false);
		
		listInfo(Table);
		createBtnWidget(toolStrip);
		section = createSection(Table,null,true,true);
		section.setHeight("92%");
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.addMember(toolStrip);
		layout.addMember(section);
//		initVerify();
		return layout;
		
		
	}
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
		toolStrip.setHeight("20");
		toolStrip.setPadding(2);
		toolStrip.setSeparatorSize(16);
		toolStrip.addSeparator();
		
		IButton searchButton = createBtn(StaticRef.FETCH_BTN);
		searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin =  new SearchWin(DS, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				} else {
					searchWin.show();
				}
				
			}
		});
		
		
		IButton expButton = createBtn(StaticRef.EXPORT_BTN);
	    expButton.addClickHandler(new ExportAction(Table, "addtime desc"));
	    
	    toolStrip.setMembersMargin(4);
	    toolStrip.setMembers(searchButton,expButton);

	}
	private void listInfo(SGTable table){
		ListGridField ADDTIME=new ListGridField("ADDTIME","日期",120);
		ListGridField LOAD_NO = new ListGridField("LOAD_NO","调度单号",120);
		ListGridField SUPLR_ID_NAME = new ListGridField("SUPLR_ID_NAME","承运商",80);
		ListGridField PLATE_NO = new ListGridField("PLATE_NO","车牌号",80);
		ListGridField UNLOAD_NAME = new ListGridField("UNLOAD_NAME","卸货点",120);
		ListGridField SHPM_NO=new ListGridField("SHPM_NO","作业单号",140);
		ListGridField ODR_NO=new ListGridField("ODR_NO","托运单号",120);
		ListGridField SKU_NAME = new ListGridField("SKU_NAME","商品简称",75);
		ListGridField SKU_ID = new ListGridField("SKU_ID","商品代码",200);
		SKU_ID.setHidden(true);
		ListGridField LOSS_DAMAGE_TYP_NAME = new ListGridField("LOSS_DAMAGE_TYP_NAME","货损类型",60);
		ListGridField QNTY = new ListGridField("QNTY",Util.TI18N.QNTY(),50);
		ListGridField AMOUNT = new ListGridField("AMOUNT",Util.TI18N.AMOUNT(),80);
		Util.initFloatListField(AMOUNT, StaticRef.QNTY_FLOAT);
		ListGridField PRICE = new ListGridField("PRICE","单价",80);
		Util.initFloatListField(PRICE, StaticRef.QNTY_FLOAT);
		
		
		table.setFields(ADDTIME,LOAD_NO,SUPLR_ID_NAME,PLATE_NO,UNLOAD_NAME,SHPM_NO,ODR_NO,SKU_NAME
				,SKU_ID,LOSS_DAMAGE_TYP_NAME,QNTY,AMOUNT,PRICE);
		
	}
	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub

	}
	public DynamicForm createSerchForm(SGPanel form){
		form.setDataSource(DS);
		form.setAutoFetchData(false);
		form.setWidth100();
		form.setCellPadding(2);

		SGText LOAD_NO = new SGText("LOAD_NO","调度单号");
		SGText PLATE_NO = new SGText("PLATE_NO","车牌号");
		
		SGText SHPM_NO = new SGText("SHPM_NO",Util.TI18N.SHPM_NO());
		SGText ODR_NO = new SGText("LOAD_NO","托运单号");
		
		SGText SKU_NAME = new SGText("SKU_NAME",Util.TI18N.SKU_NAME());
		
		SGCombo SUPLR_ID=new SGCombo("SUPLR_ID",Util.TI18N.SUPLR_NAME());
		Util.initComboValue(SUPLR_ID, "BAS_SUPPLIER", "ID", "SUPLR_CNAME"," WHERE ENABLE_FLAG = 'Y'", "");
		
		SGDateTime ADDTIME_FROM=new SGDateTime("ADDTIME_FROM","创建时间");
		ADDTIME_FROM.setWidth(FormUtil.Width);
		SGDateTime ADDTIME_TO=new SGDateTime("ADDTIME_TO","到");
		ADDTIME_TO.setWidth(FormUtil.Width);
		
		
		form.setItems(LOAD_NO,PLATE_NO,SHPM_NO,ODR_NO,SKU_NAME,SUPLR_ID,ADDTIME_FROM,
				ADDTIME_TO);
		return form;
		
	}


	@Override
	public void initVerify() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		if(searchWin != null){
			searchWin.destroy();
			searchForm.destroy();
		}

	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		DamageView view = new DamageView();
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
