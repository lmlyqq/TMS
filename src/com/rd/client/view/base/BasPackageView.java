package com.rd.client.view.base;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelMultiFormAction;
import com.rd.client.common.action.DeleteMultiFormAction;
import com.rd.client.common.action.ExportAction;
import com.rd.client.common.action.NewMultiFormAction;
import com.rd.client.common.action.SaveMultiFormAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.BasPrivRef;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.ObjUtil;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGLText;
import com.rd.client.common.widgets.SGPage;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.common.widgets.SGText;
import com.rd.client.ds.base.PackageDS;
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
import com.smartgwt.client.widgets.form.ValuesManager;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
/**
 * 基础资料->包装
 * @author yuanlei
 *
 */
@ClassForNameAble
public class BasPackageView extends SGForm implements PanelFactory {

	 private DataSource ds;
	 private SGTable table;
	 private ValuesManager form_groups;
	 private DynamicForm pack_form;             //包装FORM布局
	 private DynamicForm main_form;             //主信息页签布局
	 private DynamicForm extra_form;            //附件信息页签布局
	 private SectionStack section;
	 private Window searchWin = null;
	// private static ButtonItem searchItem;
	 private DynamicForm searchForm;
	 
	 /*public BasPackageView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
		form_groups = new ValuesManager();
		//setValuesManager(form_groups);
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();

	    ToolStrip toolStrip = new ToolStrip();  //按钮布局
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = PackageDS.getInstance("BAS_PACKAGE");
		
		//主布局
		HStack stack = new HStack();
		stack.setWidth("99%");
		stack.setHeight100();
		
		//STACK的左边列表
		table = new SGTable(ds, "100%", "100%", true, true, false); 
		table.setCanEdit(false);
		table.setShowFilterEditor(false);
		section = new SectionStack();
		final SectionStackSection listItem = new SectionStackSection(Util.TI18N.LISTINFO());
	    listItem.setItems(table);
	    listItem.setExpanded(true);
//	    listItem.setControls(addMaxBtn(section, stack, "200" ,true), new SGPage(table, true).initPageBtn());
		listItem.setControls(new SGPage(table, true).initPageBtn());
	    section.addSection(listItem);
	    section.setWidth("100%");
	    
//	    searchItem = new ButtonItem(Util.BI18N.SEARCH());
//	    new PageUtil(listItem, table, searchItem, false);

		getConfigList();
		stack.addMember(section);
		addSplitBar(stack);
		
		
		//STACK的右边布局v_lay
        VLayout v_lay = new VLayout();
        v_lay.setWidth("80%");
        v_lay.setHeight100();
        v_lay.setBackgroundColor(ColorUtil.BG_COLOR);
        v_lay.setVisible(false);
        //右上
		pack_form = new DynamicForm();
		createForm(pack_form);
		//右下
        TabSet leftTabSet = new TabSet();  
        leftTabSet.setWidth("100%");   
        leftTabSet.setHeight("100%"); 
        leftTabSet.setMargin(0);
        
        if(isPrivilege(BasPrivRef.PACK_P1)) {
	        Tab tab1 = new Tab(Util.TI18N.MAININFO());   //第一个页签（主信息）
			main_form = new DynamicForm();
			createMainForm(main_form);
	        HLayout form_lay = new HLayout();
	        form_lay.setWidth100();
	        form_lay.setHeight100();
	        form_lay.setBackgroundColor(ColorUtil.BG_COLOR);
	        form_lay.addMember(main_form);
			tab1.setPane(form_lay);
	        leftTabSet.addTab(tab1);
        }
        
        if(isPrivilege(BasPrivRef.PACK_P2)) {
	        Tab tab2 = new Tab(Util.TI18N.EXTRAINFO());  //第二个页签(附件信息)
			extra_form = new DynamicForm();
			createExtraForm(extra_form);
	        HLayout form_lay2 = new HLayout();
	        form_lay2.setWidth100();
	        form_lay2.setHeight100();
	        form_lay2.setBackgroundColor(ColorUtil.BG_COLOR);
	        form_lay2.addMember(extra_form);
			tab2.setPane(form_lay2);
	        leftTabSet.addTab(tab2);
        }
        
        v_lay.setMembers(pack_form, leftTabSet);
        
		form_groups.addMember(pack_form);
		form_groups.addMember(main_form);
		form_groups.setDataSource(ds);
        
		stack.addMember(v_lay);
		
		//创建按钮布局
		createBtnWidget(toolStrip);
		main.addMember(toolStrip);
		main.addMember(stack);
		
		initVerify();
		
		table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				initAddBtn();
			}
			
		});
		  
		return main;
	}

	@Override
	public void createForm(DynamicForm form) {
		
		//form.setDataSource(ds);
		//form.setAutoFetchData(true);
		form.setAlign(Alignment.LEFT);
		form.setHeight100();
		form.setWidth100();
		form.setHeight("18%");
		form.setCellPadding(3);
		form.setMargin(10);
		form.setTitleSuffix("");
		form.setValuesManager(form_groups);

		//1行
		
		SGText PACK= new SGText("PACK", ColorUtil.getRedTitle(Util.TI18N.PACK()));
		
		SGLText PACK_DESCR = new SGLText("DESCR", ColorUtil.getRedTitle(Util.TI18N.PACK_DESCR()));
		
	
		form.setFields(PACK, PACK_DESCR);
	}
	

	 
	private void getConfigList() {
		
		table.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
            	Record selectedRecord  = event.getRecord();
                form_groups.editRecord(selectedRecord);
                
                //enableOrDisables(del_map, true);
                initSaveBtn();
            }
        });
		
        ListGridField packField = new ListGridField("PACK", Util.TI18N.PACK(), 60);
        ListGridField descrField = new ListGridField("DESCR", Util.TI18N.PACK_DESCR(), 80);
        ListGridField descr1Field = new ListGridField("DESCR1", Util.TI18N.PACK_EACH(), 80);
        ListGridField qty1Field = new ListGridField("QTY1", Util.TI18N.PACK_QTY(), 80);
        ListGridField descr2Field = new ListGridField("DESCR2", Util.TI18N.PACK_IP(), 80);
        ListGridField qty2Field = new ListGridField("QTY2", Util.TI18N.PACK_QTY(), 80);
        ListGridField descr3Field = new ListGridField("DESCR3", Util.TI18N.PACK_CS(), 80);
        ListGridField qty3Field = new ListGridField("QTY3", Util.TI18N.PACK_QTY(), 80);
        ListGridField descr4Field = new ListGridField("DESCR4", Util.TI18N.PACK_PL(), 80);
        ListGridField qty4Field = new ListGridField("QTY4", Util.TI18N.PACK_QTY(), 80);
        ListGridField descr5Field = new ListGridField("DESCR5", Util.TI18N.PACK_OTHER(), 80);
        ListGridField qty5Field = new ListGridField("QTY5", Util.TI18N.PACK_QTY(), 80);
        table.setFields(packField, descrField, descr1Field, qty1Field, descr2Field, qty2Field, descr3Field
        		, qty3Field, descr4Field, qty4Field, descr5Field, qty5Field);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
         
        IButton searchButton = createBtn(StaticRef.FETCH_BTN,BasPrivRef.PACK);
        searchButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null) {
					searchForm = new DynamicForm();
					searchWin = new SearchWin(ds,
							createSerchForm(searchForm),section.getSection(0),form_groups).getViewPanel();
				}
				else {
					searchWin.show();
				}
			}
        	
        });
		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,BasPrivRef.PACK_P0_01);
        newButton.addClickHandler(new NewMultiFormAction(form_groups, cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,BasPrivRef.PACK_P0_02);
        saveButton.addClickHandler(new SaveMultiFormAction(table, form_groups, check_map, this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,BasPrivRef.PACK_P0_03);
        delButton.addClickHandler(new DeleteMultiFormAction(table, form_groups));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,BasPrivRef.PACK_P0_04);
        canButton.addClickHandler(new CancelMultiFormAction(table, form_groups,this));
        
        IButton expButton = createBtn(StaticRef.EXPORT_BTN,BasPrivRef.PACK_P0_05);
        expButton.addClickHandler(new ExportAction(table,"ADDTIME"));
    
        add_map.put(BasPrivRef.PACK_P0_01, newButton);
        del_map.put(BasPrivRef.PACK_P0_03, delButton);
        save_map.put(BasPrivRef.PACK_P0_02, saveButton);
        save_map.put(BasPrivRef.PACK_P0_04, canButton);
        this.enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        this.enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton, expButton);
	}
	
	public void initVerify() {
		
		check_map.put("TABLE", "BAS_PACKAGE");
		check_map.put("PACK", StaticRef.CHK_UNIQUE + Util.TI18N.PACK());
		check_map.put("DESCR", StaticRef.CHK_UNIQUE + Util.TI18N.PACK_DESCR());
		
		cache_map.put("QTY1", "1");
		cache_map.put("QTY2", "0");
		cache_map.put("QTY3", "0");
		cache_map.put("QTY4", "0");
		cache_map.put("QTY5", "0");
	}
	
	//查询窗口
	public DynamicForm createSerchForm(DynamicForm form){
		
		form.setDataSource(ds);
		form.setAutoFetchData(false);
		form.setNumCols(6);
		form.setHeight100();
		form.setWidth100();
		form.setCellPadding(2);
		form.setTitleSuffix("");
		
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(300);
		txt_global.setColSpan(5);
		txt_global.setEndRow(true);
		
		//2行
		//TextItem  PACK = new TextItem("PACK", Util.TI18N.PACK());
		//TextItem  PACK_DESCR = new TextItem("DESCR", Util.TI18N.PACK_DESCR());
		
        form.setItems(txt_global);
        
        return form;
	}
	
	private void createMainForm(DynamicForm form) {
		//form.setDataSource(ds);
		//form.setAutoFetchData(true);
		form.setAlign(Alignment.LEFT);
		form.setNumCols(14);
		form.setMargin(15);
		form.setHeight100();
		form.setWidth("617");
		form.setHeight("80%");
		form.setCellPadding(1);
		form.setTitleSuffix("");
		form.setTitleWidth("65");
		form.setValuesManager(form_groups);
		
		//1行
		StaticTextItem space_lab = new StaticTextItem();
		space_lab.setDisabled(true);
		space_lab.setTitle(" ");
		space_lab.setWidth(10);
		StaticTextItem qty_lab = new StaticTextItem();
		qty_lab.setDisabled(true);
		qty_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_QTY() + "</font>");
		StaticTextItem descr_lab = new StaticTextItem();
		descr_lab.setDisabled(true);
		descr_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_DESCR() + "</font>");
		StaticTextItem material_lab = new StaticTextItem();
		material_lab.setDisabled(true);
		material_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_MATERIAL() + "</font>");
		StaticTextItem cartonize_lab = new StaticTextItem();
		cartonize_lab.setDisabled(true);
		cartonize_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.CARTONIZE() + "</font>");
		StaticTextItem in_lab = new StaticTextItem();
		in_lab.setDisabled(true);
		in_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.IN_LABEL() + "</font>");
		StaticTextItem out_lab = new StaticTextItem();
		out_lab.setDisabled(true);
		out_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.OUT_LABEL() + "</font>");
		
		//2行
		StaticTextItem each_lab = new StaticTextItem ();
		each_lab.setWidth(1);
		each_lab.setDisabled(true);
		each_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_EACH() + "</font>");
		each_lab.setTitleAlign(Alignment.RIGHT);
		
		TextItem  QTY1 = new TextItem("QTY1", "");
		QTY1.setValue("1");
		QTY1.setShowTitle(false);
		QTY1.setDisabled(true);
		QTY1.setWidth(75);
		QTY1.setAlign(Alignment.RIGHT);
		QTY1.setColSpan(2);
		
		TextItem  DESCR1 = new TextItem("DESCR1", "");
		DESCR1.setShowTitle(false);
		DESCR1.setWidth(100);
		DESCR1.setAlign(Alignment.RIGHT);
		DESCR1.setColSpan(2);
		
		ComboBoxItem  MATERIAL1 = new ComboBoxItem("MATERIAL1", "");
		MATERIAL1.setShowTitle(false);
		MATERIAL1.setWidth(110);
		MATERIAL1.setColSpan(2);
		MATERIAL1.setAlign(Alignment.RIGHT);
		
		CheckboxItem CARTONIZE1 = new CheckboxItem("CARTONIZE1", "");
		CARTONIZE1.setShowTitle(false);
		CARTONIZE1.setColSpan(2);
		CARTONIZE1.setWidth("70");
		CARTONIZE1.setAlign(Alignment.CENTER);
		CheckboxItem IN_LABEL1 = new CheckboxItem("IN_LABEL1", "");
		IN_LABEL1.setShowTitle(false);
		IN_LABEL1.setColSpan(2);
		IN_LABEL1.setWidth("70");
		IN_LABEL1.setAlign(Alignment.CENTER);
		CheckboxItem OUT_LABEL1 = new CheckboxItem("OUT_LABEL1", "");
		OUT_LABEL1.setShowTitle(false);
		OUT_LABEL1.setColSpan(2);
		OUT_LABEL1.setWidth("70");
		OUT_LABEL1.setAlign(Alignment.CENTER);
		
		//3行
		StaticTextItem ip_lab = new StaticTextItem ();
		ip_lab.setWidth(1);
		ip_lab.setDisabled(true);
		ip_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_IP() + "</font>");
		ip_lab.setTitleAlign(Alignment.RIGHT);
		
		TextItem  QTY2 = new TextItem("QTY2", "");
		QTY2.setShowTitle(false);
		QTY2.setWidth(75);
		QTY2.setAlign(Alignment.RIGHT);
		QTY2.setColSpan(2);
		
		TextItem  DESCR2 = new TextItem("DESCR2", "");
		DESCR2.setShowTitle(false);
		DESCR2.setWidth(100);
		DESCR2.setAlign(Alignment.RIGHT);
		DESCR2.setColSpan(2);
		
		ComboBoxItem  MATERIAL2 = new ComboBoxItem("MATERIAL2", "");
		MATERIAL2.setShowTitle(false);
		MATERIAL2.setWidth(110);
		MATERIAL2.setColSpan(2);
		MATERIAL2.setAlign(Alignment.RIGHT);
		
		CheckboxItem CARTONIZE2 = new CheckboxItem("CARTONIZE2", "");
		CARTONIZE2.setShowTitle(false);
		CARTONIZE2.setColSpan(2);
		CARTONIZE2.setWidth("70");
		CARTONIZE2.setAlign(Alignment.CENTER);
		CheckboxItem IN_LABEL2 = new CheckboxItem("IN_LABEL2", "");
		IN_LABEL2.setShowTitle(false);
		IN_LABEL2.setColSpan(2);
		IN_LABEL2.setWidth("70");
		IN_LABEL2.setAlign(Alignment.CENTER);
		CheckboxItem OUT_LABEL2 = new CheckboxItem("OUT_LABEL2", "");
		OUT_LABEL2.setShowTitle(false);
		OUT_LABEL2.setColSpan(2);
		OUT_LABEL2.setWidth("70");
		OUT_LABEL2.setAlign(Alignment.CENTER);
		
		//4行
		StaticTextItem cs_lab = new StaticTextItem ();
		cs_lab.setWidth(1);
		cs_lab.setDisabled(true);
		cs_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_CS() + "</font>");
		cs_lab.setTitleAlign(Alignment.RIGHT);
		
		TextItem  QTY3 = new TextItem("QTY3", "");
		QTY3.setShowTitle(false);
		QTY3.setWidth(75);
		QTY3.setAlign(Alignment.RIGHT);
		QTY3.setColSpan(2);
		
		TextItem  DESCR3 = new TextItem("DESCR3", "");
		DESCR3.setShowTitle(false);
		DESCR3.setWidth(100);
		DESCR3.setAlign(Alignment.RIGHT);
		DESCR3.setColSpan(2);
		
		ComboBoxItem  MATERIAL3 = new ComboBoxItem("MATERIAL3", "");
		MATERIAL3.setShowTitle(false);
		MATERIAL3.setWidth(110);
		MATERIAL3.setColSpan(2);
		MATERIAL3.setAlign(Alignment.RIGHT);
		
		CheckboxItem CARTONIZE3 = new CheckboxItem("CARTONIZE3", "");
		CARTONIZE3.setShowTitle(false);
		CARTONIZE3.setColSpan(2);
		CARTONIZE3.setWidth("70");
		CARTONIZE3.setAlign(Alignment.CENTER);
		
		CheckboxItem IN_LABEL3 = new CheckboxItem("IN_LABEL3", "");
		IN_LABEL3.setShowTitle(false);
		IN_LABEL3.setColSpan(2);
		IN_LABEL3.setWidth("70");
		IN_LABEL3.setAlign(Alignment.CENTER);
		
		CheckboxItem OUT_LABEL3 = new CheckboxItem("OUT_LABEL3", "");
		OUT_LABEL3.setShowTitle(false);
		OUT_LABEL3.setColSpan(2);
		OUT_LABEL3.setWidth("70");
		OUT_LABEL3.setAlign(Alignment.CENTER);
		
		//5行
		StaticTextItem pl_lab = new StaticTextItem ();
		pl_lab.setWidth(1);
		pl_lab.setDisabled(true);
		pl_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_PL() + "</font>");
		pl_lab.setTitleAlign(Alignment.RIGHT);
		
		TextItem  QTY4 = new TextItem("QTY4", "");
		QTY4.setShowTitle(false);
		QTY4.setWidth(75);
		QTY4.setAlign(Alignment.RIGHT);
		QTY4.setColSpan(2);
		
		TextItem  DESCR4 = new TextItem("DESCR4", "");
		DESCR4.setShowTitle(false);
		DESCR4.setWidth(100);
		DESCR4.setAlign(Alignment.RIGHT);
		DESCR4.setColSpan(2);
		
		ComboBoxItem  MATERIAL4 = new ComboBoxItem("MATERIAL4", "");
		MATERIAL4.setShowTitle(false);
		MATERIAL4.setWidth(110);
		MATERIAL4.setColSpan(2);
		MATERIAL4.setAlign(Alignment.RIGHT);
		
		CheckboxItem CARTONIZE4 = new CheckboxItem("CARTONIZE4", "");
		CARTONIZE4.setShowTitle(false);
		CARTONIZE4.setColSpan(2);
		CARTONIZE4.setWidth("70");
		CARTONIZE4.setAlign(Alignment.CENTER);
		
		CheckboxItem IN_LABEL4 = new CheckboxItem("IN_LABEL4", "");
		IN_LABEL4.setShowTitle(false);
		IN_LABEL4.setColSpan(2);
		IN_LABEL4.setWidth("70");
		IN_LABEL4.setAlign(Alignment.CENTER);
		
		CheckboxItem OUT_LABEL4 = new CheckboxItem("OUT_LABEL4", "");
		OUT_LABEL4.setShowTitle(false);
		OUT_LABEL4.setColSpan(2);
		OUT_LABEL4.setWidth("70");
		OUT_LABEL4.setAlign(Alignment.CENTER);
		
		//6行
		StaticTextItem other_lab = new StaticTextItem ();
		other_lab.setWidth(1);
		other_lab.setDisabled(true);
		other_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_OTHER() + "</font>");
		other_lab.setTitleAlign(Alignment.RIGHT);
		
		TextItem  QTY5 = new TextItem("QTY5", "");
		QTY5.setShowTitle(false);
		QTY5.setWidth(75);
		QTY5.setAlign(Alignment.RIGHT);
		QTY5.setColSpan(2);
		
		TextItem  DESCR5 = new TextItem("DESCR5", "");
		DESCR5.setShowTitle(false);
		DESCR5.setWidth(100);
		DESCR5.setAlign(Alignment.RIGHT);
		DESCR5.setColSpan(2);
		
		ComboBoxItem  MATERIAL5 = new ComboBoxItem("MATERIAL5", "");
		MATERIAL5.setShowTitle(false);
		MATERIAL5.setWidth(110);
		MATERIAL5.setColSpan(2);
		MATERIAL5.setAlign(Alignment.RIGHT);
		
		CheckboxItem CARTONIZE5 = new CheckboxItem("CARTONIZE5", "");
		CARTONIZE5.setShowTitle(false);
		CARTONIZE5.setColSpan(2);
		CARTONIZE5.setWidth("70");
		CARTONIZE5.setAlign(Alignment.CENTER);
		
		CheckboxItem IN_LABEL5 = new CheckboxItem("IN_LABEL5", "");
		IN_LABEL5.setShowTitle(false);
		IN_LABEL5.setColSpan(2);
		IN_LABEL5.setWidth("70");
		IN_LABEL5.setAlign(Alignment.CENTER);
		
		CheckboxItem OUT_LABEL5 = new CheckboxItem("OUT_LABEL5", "");
		OUT_LABEL5.setShowTitle(false);
		OUT_LABEL5.setColSpan(2);
		OUT_LABEL5.setWidth("70");
		OUT_LABEL5.setAlign(Alignment.CENTER);
		
		form.setFields(space_lab, qty_lab, descr_lab, material_lab, cartonize_lab, in_lab, out_lab
				, each_lab, QTY1, DESCR1, MATERIAL1, CARTONIZE1, IN_LABEL1, OUT_LABEL1
				, ip_lab, QTY2, DESCR2, MATERIAL2, CARTONIZE2, IN_LABEL2, OUT_LABEL2
				, cs_lab, QTY3, DESCR3, MATERIAL3, CARTONIZE3, IN_LABEL3, OUT_LABEL3
				, pl_lab, QTY4, DESCR4, MATERIAL4, CARTONIZE4, IN_LABEL4, OUT_LABEL4
				, other_lab, QTY5, DESCR5, MATERIAL5, CARTONIZE5, IN_LABEL5, OUT_LABEL5
				);
		
		ComboBoxItem[] comboList = new ComboBoxItem[5];
		comboList[0]= MATERIAL1;
		comboList[1]= MATERIAL2;
		comboList[2]= MATERIAL3;
		comboList[3]= MATERIAL4;
		comboList[4]= MATERIAL5;
		Util.initCodesComboValue(comboList, "PAC_MAT");
	}
	
	private void createExtraForm(DynamicForm form) {
		form.setAlign(Alignment.LEFT);
		form.setNumCols(15);
		form.setMargin(15);
		form.setHeight100();
		form.setWidth("580");
		form.setHeight("80%");
		form.setCellPadding(1);
		form.setTitleSuffix("");
		form.setTitleWidth("65");
		form.setValuesManager(form_groups);
		
		//1行
		StaticTextItem space_lab = new StaticTextItem();
		space_lab.setDisabled(true);
		space_lab.setTitle(" ");
		space_lab.setWidth(1);
		StaticTextItem qty_lab = new StaticTextItem();
		qty_lab.setDisabled(true);
		qty_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_LEN() + "</font>");
		StaticTextItem descr_lab = new StaticTextItem();
		descr_lab.setDisabled(true);
		descr_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_WIDTH() + "</font>");
		StaticTextItem material_lab = new StaticTextItem();
		material_lab.setDisabled(true);
		material_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_HEIGHT() + "</font>");
		StaticTextItem cartonize_lab = new StaticTextItem();
		cartonize_lab.setDisabled(true);
		cartonize_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_VOLUME() + "</font>");
		StaticTextItem in_lab = new StaticTextItem();
		in_lab.setDisabled(true);
		in_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_WEIGHT() + "</font>");
		StaticTextItem out_lab = new StaticTextItem();
		out_lab.setDisabled(true);
		out_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_TIHI() + "</font>");
		
		//2行
		StaticTextItem each_lab = new StaticTextItem ();
		each_lab.setWidth(1);
		each_lab.setDisabled(true);
		each_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_EACH() + "</font>");
		each_lab.setTitleAlign(Alignment.RIGHT);
		
		final TextItem  LENGTH1 = new TextItem("LENGTH1", "");
		LENGTH1.setShowTitle(false);
		LENGTH1.setWidth(75);
		LENGTH1.setAlign(Alignment.RIGHT);
		LENGTH1.setColSpan(2);
		
		final TextItem  WIDTH1 = new TextItem("WIDTH1", "");
		WIDTH1.setShowTitle(false);
		WIDTH1.setWidth(75);
		WIDTH1.setAlign(Alignment.RIGHT);
		WIDTH1.setColSpan(2);
		
		final TextItem  HEIGHT1 = new TextItem("HEIGHT1", "");
		HEIGHT1.setShowTitle(false);
		HEIGHT1.setWidth(75);
		HEIGHT1.setColSpan(2);
		HEIGHT1.setAlign(Alignment.RIGHT);
		
		final TextItem VOLUME1 = new TextItem("VOLUME1", "");
		VOLUME1.setShowTitle(false);
		VOLUME1.setColSpan(2);
		VOLUME1.setWidth(75);
		VOLUME1.setAlign(Alignment.CENTER);
		TextItem WEIGHT1 = new TextItem("WEIGHT1", "");
		WEIGHT1.setShowTitle(false);
		WEIGHT1.setColSpan(2);
		WEIGHT1.setWidth(75);
		WEIGHT1.setAlign(Alignment.CENTER);
		WEIGHT1.setEndRow(true);
		
		//3行
		StaticTextItem ip_lab = new StaticTextItem ();
		ip_lab.setWidth(1);
		ip_lab.setDisabled(true);
		ip_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_IP() + "</font>");
		ip_lab.setTitleAlign(Alignment.RIGHT);
		
		final TextItem  LENGTH2 = new TextItem("LENGTH2", "");
		LENGTH2.setShowTitle(false);
		LENGTH2.setWidth(75);
		LENGTH2.setAlign(Alignment.RIGHT);
		LENGTH2.setColSpan(2);
		
		final TextItem  WIDTH2 = new TextItem("WIDTH2", "");
		WIDTH2.setShowTitle(false);
		WIDTH2.setWidth(75);
		WIDTH2.setAlign(Alignment.RIGHT);
		WIDTH2.setColSpan(2);
		
		final TextItem  HEIGHT2 = new TextItem("HEIGHT2", "");
		HEIGHT2.setShowTitle(false);
		HEIGHT2.setWidth(75);
		HEIGHT2.setColSpan(2);
		HEIGHT2.setAlign(Alignment.RIGHT);
		
		final TextItem VOLUME2 = new TextItem("VOLUME2", "");
		VOLUME2.setShowTitle(false);
		VOLUME2.setColSpan(2);
		VOLUME2.setWidth(75);
		VOLUME2.setAlign(Alignment.CENTER);
		TextItem WEIGHT2 = new TextItem("WEIGHT2", "");
		WEIGHT2.setShowTitle(false);
		WEIGHT2.setColSpan(2);
		WEIGHT2.setWidth(75);
		WEIGHT2.setAlign(Alignment.CENTER);
		WEIGHT2.setEndRow(true);
		
		//4行
		StaticTextItem cs_lab = new StaticTextItem ();
		cs_lab.setWidth(1);
		cs_lab.setDisabled(true);
		cs_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_CS() + "</font>");
		cs_lab.setTitleAlign(Alignment.RIGHT);
		
		final TextItem  LENGTH3 = new TextItem("LENGTH3", "");
		LENGTH3.setShowTitle(false);
		LENGTH3.setWidth(75);
		LENGTH3.setAlign(Alignment.RIGHT);
		LENGTH3.setColSpan(2);
		
		final TextItem  WIDTH3 = new TextItem("WIDTH3", "");
		WIDTH3.setShowTitle(false);
		WIDTH3.setWidth(75);
		WIDTH3.setAlign(Alignment.RIGHT);
		WIDTH3.setColSpan(2);
		
		final TextItem  HEIGHT3 = new TextItem("HEIGHT3", "");
		HEIGHT3.setShowTitle(false);
		HEIGHT3.setWidth(75);
		HEIGHT3.setColSpan(2);
		HEIGHT3.setAlign(Alignment.RIGHT);
		
		final TextItem VOLUME3 = new TextItem("VOLUME3", "");
		VOLUME3.setShowTitle(false);
		VOLUME3.setColSpan(2);
		VOLUME3.setWidth(75);
		VOLUME3.setAlign(Alignment.CENTER);
		
		TextItem WEIGHT3 = new TextItem("WEIGHT3", "");
		WEIGHT3.setShowTitle(false);
		WEIGHT3.setColSpan(2);
		WEIGHT3.setWidth(75);
		WEIGHT3.setAlign(Alignment.CENTER);
		WEIGHT3.setEndRow(true);
		
		//5行
		StaticTextItem pl_lab = new StaticTextItem ();
		pl_lab.setWidth(1);
		pl_lab.setDisabled(true);
		pl_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_PL() + "</font>");
		pl_lab.setTitleAlign(Alignment.RIGHT);
		
		final TextItem LENGTH4 = new TextItem("LENGTH4", "");
		LENGTH4.setShowTitle(false);
		LENGTH4.setWidth(75);
		LENGTH4.setAlign(Alignment.RIGHT);
		LENGTH4.setColSpan(2);
		
		final TextItem WIDTH4 = new TextItem("WIDTH4", "");
		WIDTH4.setShowTitle(false);
		WIDTH4.setWidth(75);
		WIDTH4.setAlign(Alignment.RIGHT);
		WIDTH4.setColSpan(2);
		
		final TextItem HEIGHT4 = new TextItem("HEIGHT4", "");
		HEIGHT4.setShowTitle(false);
		HEIGHT4.setWidth(75);
		HEIGHT4.setColSpan(2);
		HEIGHT4.setAlign(Alignment.RIGHT);
		
		final TextItem VOLUME4 = new TextItem("VOLUME4", "");
		VOLUME4.setShowTitle(false);
		VOLUME4.setColSpan(2);
		VOLUME4.setWidth(75);
		VOLUME4.setAlign(Alignment.CENTER);
		
		TextItem WEIGHT4 = new TextItem("WEIGHT4", "");
		WEIGHT4.setShowTitle(false);
		WEIGHT4.setColSpan(2);
		WEIGHT4.setWidth(75);
		WEIGHT4.setAlign(Alignment.CENTER);
		
		TextItem PALLETTI = new TextItem("PALLETTI", "");
		PALLETTI.setShowTitle(false);
		PALLETTI.setWidth("35");
		PALLETTI.setAlign(Alignment.CENTER);
		
		TextItem PALLETHI = new TextItem("PALLETHI", "*");
		PALLETHI.setTitleAlign(Alignment.LEFT);
		PALLETHI.setTitleOrientation(TitleOrientation.LEFT);
		PALLETHI.setWidth("35");
		PALLETHI.setAlign(Alignment.CENTER);
		
		//6行
		StaticTextItem other_lab = new StaticTextItem ();
		other_lab.setWidth(1);
		other_lab.setDisabled(true);
		other_lab.setTitle("<font color = \"#000000\">" + Util.TI18N.PACK_OTHER() + "</font>");
		other_lab.setTitleAlign(Alignment.RIGHT);
		
		final TextItem  LENGTH5 = new TextItem("LENGTH5", "");
		LENGTH5.setShowTitle(false);
		LENGTH5.setWidth(75);
		LENGTH5.setAlign(Alignment.RIGHT);
		LENGTH5.setColSpan(2);
		
		final TextItem  WIDTH5 = new TextItem("WIDTH5", "");
		WIDTH5.setShowTitle(false);
		WIDTH5.setWidth(75);
		WIDTH5.setAlign(Alignment.RIGHT);
		WIDTH5.setColSpan(2);
		
		final TextItem  HEIGHT5 = new TextItem("HEIGHT5", "");
		HEIGHT5.setShowTitle(false);
		HEIGHT5.setWidth(75);
		HEIGHT5.setColSpan(2);
		HEIGHT5.setAlign(Alignment.RIGHT);
		
		final TextItem VOLUME5 = new TextItem("VOLUME5", "");
		VOLUME5.setShowTitle(false);
		VOLUME5.setColSpan(2);
		VOLUME5.setWidth(75);
		VOLUME5.setAlign(Alignment.CENTER);
		
		TextItem WEIGHT5 = new TextItem("WEIGHT5", "");
		WEIGHT5.setShowTitle(false);
		WEIGHT5.setColSpan(2);
		WEIGHT5.setWidth(75);
		WEIGHT5.setAlign(Alignment.CENTER);
		WEIGHT5.setEndRow(true);
		
		form.setFields(space_lab, qty_lab, descr_lab, material_lab, cartonize_lab, in_lab, out_lab
				, each_lab, LENGTH1, WIDTH1, HEIGHT1, VOLUME1, WEIGHT1
				, ip_lab, LENGTH2, WIDTH2, HEIGHT2, VOLUME2, WEIGHT2
				, cs_lab, LENGTH3, WIDTH3, HEIGHT3, VOLUME3, WEIGHT3
				, pl_lab, LENGTH4, WIDTH4, HEIGHT4, VOLUME4, WEIGHT4, PALLETTI, PALLETHI
				, other_lab, LENGTH5, WIDTH5, HEIGHT5, VOLUME5, WEIGHT5
				);
		
		HEIGHT1.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				double loo = Double.parseDouble(ObjUtil.ifObjNull(LENGTH1.getValue(), "0").toString());
				double woo = Double.parseDouble(ObjUtil.ifObjNull(WIDTH1.getValue(), "0").toString());
				double hoo = Double.parseDouble(ObjUtil.ifObjNull(HEIGHT1.getValue(), "0").toString());
				VOLUME1.setValue(loo*woo*hoo);
			}
		});
		HEIGHT2.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				double loo = Double.parseDouble(ObjUtil.ifObjNull(LENGTH2.getValue(), "0").toString());
				double woo = Double.parseDouble(ObjUtil.ifObjNull(WIDTH2.getValue(), "0").toString());
				double hoo = Double.parseDouble(ObjUtil.ifObjNull(HEIGHT2.getValue(), "0").toString());
				VOLUME2.setValue(loo*woo*hoo);
			}
		});
		HEIGHT3.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				double loo = Double.parseDouble(ObjUtil.ifObjNull(LENGTH3.getValue(), "0").toString());
				double woo = Double.parseDouble(ObjUtil.ifObjNull(WIDTH3.getValue(), "0").toString());
				double hoo = Double.parseDouble(ObjUtil.ifObjNull(HEIGHT3.getValue(), "0").toString());
				VOLUME3.setValue(loo*woo*hoo);
			}
		});
		HEIGHT4.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				double loo = Double.parseDouble(ObjUtil.ifObjNull(LENGTH4.getValue(), "0").toString());
				double woo = Double.parseDouble(ObjUtil.ifObjNull(WIDTH4.getValue(), "0").toString());
				double hoo = Double.parseDouble(ObjUtil.ifObjNull(HEIGHT4.getValue(), "0").toString());
				VOLUME4.setValue(loo*woo*hoo);
			}
		});
		HEIGHT5.addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				double loo = Double.parseDouble(ObjUtil.ifObjNull(LENGTH5.getValue(), "0").toString());
				double woo = Double.parseDouble(ObjUtil.ifObjNull(WIDTH5.getValue(), "0").toString());
				double hoo = Double.parseDouble(ObjUtil.ifObjNull(HEIGHT5.getValue(), "0").toString());
				VOLUME5.setValue(loo*woo*hoo);
			}
		});
	}

	@Override
	public void onDestroy() {
		if(searchWin != null) {
			searchWin.destroy();
			searchForm.destroy();
			//searchItem = null;
		}
		//table.destroy();
		//main_form.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		BasPackageView view = new BasPackageView();
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
