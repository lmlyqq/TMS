package com.rd.client.view.system;

import com.rd.client.PanelFactory;
import com.rd.client.common.action.CancelAction;
import com.rd.client.common.action.DeleteAction;
import com.rd.client.common.action.NewAction;
import com.rd.client.common.action.SaveAction;
import com.rd.client.common.obj.LoginCache;
import com.rd.client.common.util.ColorUtil;
import com.rd.client.common.util.RulPrivRef;
import com.rd.client.common.util.StaticRef;
import com.rd.client.common.util.Util;
import com.rd.client.common.widgets.SGCheck;
import com.rd.client.common.widgets.SGCombo;
import com.rd.client.common.widgets.SGForm;
import com.rd.client.common.widgets.SGPanel;
import com.rd.client.common.widgets.SGTable;
import com.rd.client.ds.system.NodeRuleDS;
import com.rd.client.reflection.ClassForNameAble;
import com.rd.client.win.SearchWin;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
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

/**
 * 业务规则->运输规则->节点规则
 * @author yuanlei
 * @create time 2012-06-26 20:08
 *
 */
@ClassForNameAble
public class NodeRuleView extends SGForm implements PanelFactory {
	
	 private DataSource ds;
	 private SGTable table;
	 private SectionStack section;
	 
	 private Window searchWin;
	 private SGPanel searchForm;
	 
	 /*public NodeRuleView(String id) {
		 super(id);
	 }*/
	 
	 public Canvas getViewPanel() {
		
		privObj = LoginCache.getUserPrivilege().get(getFUNCID());
	    ToolStrip toolStrip = new ToolStrip();
	    toolStrip.setAlign(Alignment.RIGHT);
	    ds = NodeRuleDS.getInstance("TRANS_NODE_RULE", "TRANS_NODE_RULE");
		   
		table = new SGTable(ds, "100%", "100%", false, true, false); 
		table.setCanEdit(true);
		table.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		createListFields(table);
		
		section = createSection(table, null, true, true);
	    
		createBtnWidget(toolStrip);
		
		VLayout main = new VLayout();
		main.setWidth100();
		main.setHeight100();
		main.addMember(toolStrip);
		main.addMember(section);
	    
		initVerify();
		return main;
	}
	 //设定需要校验的字段
	 public void initVerify() {
		check_map.put("TABLE", "TRANS_NODE_RULE");		
		check_map.put("NODE_MODEL", StaticRef.CHK_NOTNULL + Util.TI18N.SMS_MODEL());

		cache_map.put("ENABLE_FLAG", "Y");
	 }

	@Override
	public void createForm(DynamicForm form) {
		
	}
    //布局列表信息按钮
	private void createListFields(SGTable table) {
		
		table.addRecordClickHandler(new RecordClickHandler() {
			
			@Override
			public void onRecordClick(RecordClickEvent event) {
				enableOrDisables(del_map, true);
			}
		});
		  table.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				enableOrDisables(add_map, false);
				enableOrDisables(del_map, false);
				enableOrDisables(save_map, true);
				
			}
		});
		  ListGridField MODEL_TYP = new ListGridField("NODE_MODEL", Util.TI18N.SMS_MODEL(), 120); 
		  MODEL_TYP.setTitle(ColorUtil.getRedTitle(Util.TI18N.SMS_MODEL()));
		  Util.initCodesComboValue(MODEL_TYP,"TRANSACTION_TYP");
		  ListGridField SMS_FLAG = new ListGridField("SMS_FLAG",Util.TI18N.SMS_FLAG(),80);
		  SMS_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField REC_FEE_FLAG = new ListGridField("REC_FEE_FLAG",Util.TI18N.REC_FEE_FLAG(),120);  
		  REC_FEE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField PAY_FEE_FLAG = new ListGridField("PAY_FEE_FLAG",Util.TI18N.PAY_FEE_FLAG(),120);  
		  PAY_FEE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField TIME_FLAG = new ListGridField("TIME_FLAG",Util.TI18N.TIME_FLAG(),80);  
		  TIME_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField BIZ_LOG_FLAG = new ListGridField("BIZ_LOG_FLAG",Util.TI18N.BIZ_LOG_FLAG(),120);   
		  BIZ_LOG_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField OPT_LOG_FLAG = new ListGridField("OPT_LOG_FLAG",Util.TI18N.OPT_LOG_FLAG(),120);  
		  OPT_LOG_FLAG.setType(ListGridFieldType.BOOLEAN);
		  ListGridField ENABLE_FLAG = new ListGridField("ENABLE_FLAG",Util.TI18N.ENABLE_FLAG(),80); 
		  ENABLE_FLAG.setType(ListGridFieldType.BOOLEAN);
		  table.setFields(MODEL_TYP, SMS_FLAG, REC_FEE_FLAG, PAY_FEE_FLAG, TIME_FLAG, BIZ_LOG_FLAG, OPT_LOG_FLAG, ENABLE_FLAG);
	}

	@Override
	public void createBtnWidget(ToolStrip toolStrip) {
        toolStrip.setWidth("100%");
        toolStrip.setHeight("20");
        toolStrip.setPadding(2);
        toolStrip.setSeparatorSize(12);
        toolStrip.addSeparator();
        
        IButton searchButton = createBtn(StaticRef.FETCH_BTN);
        searchButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(searchWin == null){
					searchForm = new SGPanel();
					searchWin = new SearchWin(ds, createSerchForm(searchForm), section.getSection(0)).getViewPanel();
				}else{
					searchWin.show();
				}
				
			}
		});
        		
        IButton newButton = createBtn(StaticRef.CREATE_BTN,RulPrivRef.NODE_P0_01);
        newButton.addClickHandler(new NewAction(table,cache_map,this));
        
        IButton saveButton = createBtn(StaticRef.SAVE_BTN,RulPrivRef.NODE_P0_02);
        saveButton.addClickHandler(new SaveAction(table,check_map,this));
        
        IButton delButton = createBtn(StaticRef.DELETE_BTN,RulPrivRef.NODE_P0_03);
        delButton.addClickHandler(new DeleteAction(table));
        
        IButton canButton = createBtn(StaticRef.CANCEL_BTN,RulPrivRef.NODE_P0_04);
        canButton.addClickHandler(new CancelAction(table,this));
        
        add_map.put(RulPrivRef.NODE_P0_01, newButton);
        del_map.put(RulPrivRef.NODE_P0_03, delButton);
        save_map.put(RulPrivRef.NODE_P0_02, saveButton);
        save_map.put(RulPrivRef.NODE_P0_04, canButton);
        enableOrDisables(add_map, true);
        enableOrDisables(del_map, false);
        enableOrDisables(save_map, false);
        
        toolStrip.setMembersMargin(4);
        toolStrip.setMembers(searchButton, newButton, saveButton, delButton, canButton);
	}
	
	public DynamicForm createSerchForm(SGPanel form) {
		
		//1第一行：模糊查询
		TextItem txt_global = new TextItem("FULL_INDEX", Util.TI18N.FUZZYQRY());
		txt_global.setTitleOrientation(TitleOrientation.TOP);
		txt_global.setWidth(323);
		txt_global.setColSpan(6);
		txt_global.setEndRow(true);
		
		
		//2第二行：配置参数，使用下拉列表（ComboBoxItem）
		SGCombo txt_model = new SGCombo("NODE_MODEL", Util.TI18N.SMS_MODEL(),true);
		Util.initCodesComboValue(txt_model,"TRANSACTION_TYP");
		
		//3第三行
		SGCheck chk_enable = new SGCheck("ENABLE_FLAG", Util.TI18N.ENABLE_FLAG());
		chk_enable.setValue(true);
		
		form.setItems(txt_global, txt_model, chk_enable);
		
		return form;
	}
	
	@Override
	public void onDestroy() {
		table.destroy();
	}

	@Override
	public Canvas createCanvas(String id,TabSet tabSet) {
		NodeRuleView view = new NodeRuleView();
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
