package com.rd.client.view.settlement;

import java.util.LinkedHashMap;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.SettPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.settlement.BasTariffDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * 计费协议-标准报价
 * @author fanglm
 * @create time 2013-1-14
 *
 */
@ClassForNameAble
public class BasTariffView extends SGForm implements PanelFactory {

	private DataSource ds;
	private SGTable table;
	private Window searchWin = null;
	public TreeNode selectNode;
	public Record selectRecord;
	public SGPanel searchForm = new SGPanel();
	private SectionStack section;
    
	/*public BasTariffView(String id) {
		super(id);
	}*/
	
	@Override
	public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		LoginCache.getDefCustomer();//获取默认客户
		initVerify();
		
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setAlign(Alignment.RIGHT);
		 ds = BasTariffDS.getInstance("TMP_FEE_SET","TMP_FEE_SET");
		
		table = new SGTable(ds, "100%", "100%");
		table.setShowFilterEditor(false);
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				initSaveBtn();
			}
		});
        
		createListFields(table);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		  
		section = createSection(table, null, true, true);
		section.setWidth("100%");
		section.setHeight100();
		
	        
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
		  
		return main;
	}
	
	//创建列表
	private void createListFields(SGTable table){

        ListGridField descrField = new ListGridField("START_AREA_ID", Util.TI18N.START_AREA_ID_NAME(), 10);
        ListGridField START_AREA = new ListGridField("START_AREA_ID_NAME","发货地",120);//起点区域
        descrField.setCanEdit(false);
        descrField.setHidden(true);
        //descrField.setCanEdit(false);
        Util.initArea(table,START_AREA,"START_AREA_ID", "START_AREA_ID_NAME", "");
        ListGridField descr1Field = new ListGridField("AREA_ID", Util.TI18N.END_AREA_ID_NAME(), 10);
        ListGridField END_AREA = new ListGridField("END_AREA_ID_NAME",Util.TI18N.END_AREA_ID_NAME(),120);//起点区域
        descr1Field.setCanEdit(false);
        descr1Field.setHidden(true);
        //descr1Field.setCanEdit(false);
        Util.initArea(table,END_AREA,"AREA_ID", "END_AREA_ID_NAME", "");
        ListGridField qty1Field = new ListGridField("FROM_DATE", "起始时间", 100);
        //qty1Field.setCanEdit(false);
        ListGridField descr2Field = new ListGridField("TO_DATE", "结束时间", 100);
        //descr2Field.setCanEdit(false);
        ListGridField price = new ListGridField("PRICE", "单价", 100);
        
        ListGridField ODR_TYP = new ListGridField("ODR_TYP", Util.TI18N.ODR_TYP(),80);
        LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
        map.put("1","发货订单");
        map.put("2","调拨单");
        ODR_TYP.setValueMap(map);
        table.setFields(descrField, START_AREA,descr1Field, END_AREA,qty1Field, descr2Field,price,ODR_TYP);
		  
	}
	
	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
		toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(3);
        toolStrip.setSeparatorSize(5);
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,SettPrivRef.BASE_TARIFF_P0);       
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchWin = new SearchWin(ds,createSerchForm(searchForm),section.getSection(0)).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
        
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,SettPrivRef.BASE_TRAIFF_P0_11);
        newButton.addClickHandler(new NewAction(table,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,SettPrivRef.BASE_TRAIFF_P0_12);
        saveButton.addClickHandler(new SaveAction(table,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,SettPrivRef.BASE_TRAIFF_P0_13);
        delButton.addClickHandler(new DeleteAction(table));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,SettPrivRef.BASE_TRAIFF_P0_14);
        canButton.addClickHandler(new CancelAction(table,this));
        
        add_map.put(SettPrivRef.BASE_TRAIFF_P0_11,newButton);
        del_map.put(SettPrivRef.BASE_TRAIFF_P0_13, delButton);
        save_map.put(SettPrivRef.BASE_TRAIFF_P0_12, saveButton);
        save_map.put(SettPrivRef.BASE_TRAIFF_P0_14, canButton);
        this.enableOrDisables(add_map, true);
        this.enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	}

	@Override
	public void createForm(DynamicForm form) {
		// TODO Auto-generated method stub
		
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(352);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);

		
        form.setItems(txt_global);
        
        return form;
	}

	@Override
	public void onDestroy() {
		if (searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
		}
	}

	@Override
	public void initVerify() {
		check_map.put("TABLE", "TMP_FEE_SET");
		check_map.put("FROM_DATE", StaticRef.CHK_DATE + "起始时间");
		check_map.put("TO_DATE", StaticRef.CHK_DATE + "结束时间");
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasTariffView view = new BasTariffView();
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
